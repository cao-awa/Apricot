package com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu;

import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain.MarkovChain;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain.MarkovWord;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain.MarkovWordWeight;
import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain.leveldb.LevelDbChainMap;
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

    public HanasuModel() {
        this.chain = EntrustEnvironment.trys(() -> new MarkovChain(new LevelDbChainMap(
                new Iq80DBFactory().open(
                        new File("test/hanasu/markov"),
                        new Options().createIfMissing(true)
                                     .writeBufferSize(1048560)
                                     .compressionType(CompressionType.SNAPPY)
                ),
                new Iq80DBFactory().open(
                        new File("test/hanasu/markov_c"),
                        new Options().createIfMissing(true)
                                     .writeBufferSize(1048560)
                                     .compressionType(CompressionType.SNAPPY)
                )
        )));
    }

    public String text(@NotNull String preGen) {
        try {
            this.chain.save();

            MarkovWord word = null;
            if (preGen.equals("")) {
                word = this.chain.get()
                                 .random(RANDOM);
            } else {
                int index = 0;
                while (word == null) {
                    if (this.chain.length() > index) {
                        word = this.chain.get(this.analysis.parseStr(preGen)
                                                           .get(index++)
                                                           .getName());
                    } else {
                        word = this.chain.get()
                                         .random(RANDOM);
                    }
                }
            }
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < RANDOM.nextInt(10,
                                               25
            ); i++) {
                if (word == null) {
                    break;
                }
                builder.append(word.getWord());
                Map<String, MarkovWordWeight> weights = word.getWeights();
                if (weights.size() > 0) {
                    String nextWord = WeightRandom.getWordWeight(weights)
                                                  .getWord();
                    word = this.chain.get(nextWord);
                } else {
                    break;
                }
            }

            return builder.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
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
            e.printStackTrace();
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