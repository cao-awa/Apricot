package com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model;

import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.MarkovChain;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.MarkovWord;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.MarkovWordWeight;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.leveldb.LevelDbChainMap;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.option.BiOption;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.NlpAnalysis;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.iq80.leveldb.CompressionType;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class HanasuModel {
    private static final Random RANDOM = new Random();
    private static final Logger LOGGER = LogManager.getLogger();
    private final MarkovChain chain;
    private final NlpAnalysis analysis = new NlpAnalysis();

    public HanasuModel(String name) {
        this.chain = EntrustEnvironment.trys(() -> new MarkovChain(new LevelDbChainMap(
                new Iq80DBFactory().open(
                        new File("hanasu/" + name + "/markov"),
                        new Options().createIfMissing(true)
                                     .writeBufferSize(1048560)
                                     .compressionType(CompressionType.SNAPPY)
                ),
                new Iq80DBFactory().open(
                        new File("hanasu/" + name + "/markov_c"),
                        new Options().createIfMissing(true)
                                     .writeBufferSize(1048560)
                                     .compressionType(CompressionType.SNAPPY)
                )
        )));
    }

    public String text(@NotNull String preGen) {
        try {
            this.chain.save();

            MarkovWord word;
            if (preGen.equals("")) {
                word = this.chain.get()
                                 .random(RANDOM);
            } else {
                int index = 0;
                Result result = this.analysis.parseStr(preGen);
                word = this.chain.get(result.get(RANDOM.nextInt(0,
                                                                result.size()
                                            ))
                                            .getName());
                while (word == null) {
                    if (result.size() > index) {
                        word = this.chain.get(result
                                                      .get(index++)
                                                      .getName());
                    } else {
                        word = this.chain.get()
                                         .random(RANDOM);
                    }
                }
            }
            StringBuilder builder = new StringBuilder();
            String last = "";
            for (int i = 0; i < RANDOM.nextInt(10,
                                               35
            ); i++) {
                if (word == null) {
                    break;
                }
                if (last.equals(word.getWord())) {
                    word = swap(word);
                    if (word == null) {
                        break;
                    }
                    continue;
                }
                builder.append(word.getWord());
                word = swap(word);
                if (word == null) {
                    break;
                }
                last = word.getWord();
            }

            return builder.toString();
        } catch (Exception e) {
            return "";
        }
    }

    public MarkovWord swap(MarkovWord word) {
        Map<String, MarkovWordWeight> weights = word.getWeights();
        if (weights.size() > 0) {
            String nextWord = WeightRandom.getWordWeight(weights)
                                          .getWord();
            return this.chain.get(nextWord);
        } else {
            return null;
        }
    }

    public void model(@NotNull String line) {
        try {
            if (line.strip()
                    .trim()
                    .equals("")) {
                return;
            }

            Result result = this.analysis.parseStr(line);

            String last = "";

            for (Term term : result) {
                if (last.equals("")) {
                    last = term.getName();
                    this.chain.addWeight(last,
                                         null
                    );
                    continue;
                }
                this.chain.addWeight(last,
                                     new MarkovWordWeight(term.getName(),
                                                          1
                                     )
                );
                last = term.getName();
            }

            this.chain.save();
        } catch (Exception e) {

        }
    }

    static class WeightRandom {
        public static <T> T get(Map<T, Integer> map) {
            Set<T> keySet = map.keySet();
            Range<T> ranges = new Range<>();
            for (T element : keySet) {
                int weight = map.get(element);
                ranges.next(weight,
                            element
                );
            }
            int idx = RANDOM.nextInt(ranges.range());
            return ranges.get(idx);
        }

        public static MarkovWordWeight getWordWeight(Map<String, MarkovWordWeight> map) {
            Range<MarkovWordWeight> ranges = new Range<>();
            for (MarkovWordWeight element : map.values()) {
                ranges.next(element.getWeight(),
                            element
                );
            }
            int idx = RANDOM.nextInt(ranges.range());
            return ranges.get(idx);
        }

        private static class Range<T> {
            private final Map<BiOption<Integer>, T> ranges = ApricotCollectionFactor.newHashMap();
            private int current;

            public void next(Integer to, T target) {
                this.ranges.put(BiOption.of(this.current,
                                            this.current += to
                                ),
                                target
                );
            }

            public int range() {
                return this.current;
            }

            public T get(Integer number) {
                for (Map.Entry<BiOption<Integer>, T> entry : this.ranges.entrySet()) {
                    BiOption<Integer> range = entry.getKey();
                    T value = entry.getValue();

                    if (number >= range.first() && number <= range.second()) {
                        return value;
                    }
                }
                return null;
            }
        }
    }
}