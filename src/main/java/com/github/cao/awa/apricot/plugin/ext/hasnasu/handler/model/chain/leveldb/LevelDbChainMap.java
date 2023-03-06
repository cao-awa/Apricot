package com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.leveldb;

import com.github.cao.awa.apricot.io.bytes.reader.BytesReader;
import com.github.cao.awa.apricot.mathematic.base.Base256;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import org.iq80.leveldb.DB;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.MarkovWord;
import com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain.MarkovWordWeight;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;

public class LevelDbChainMap {
    private final DB head;
    private final DB convert;

    public LevelDbChainMap(DB head, DB convert) {
        this.head = head;
        this.convert = convert;
    }

    public MarkovWord random(Random random) {
        int rand = random.nextInt(checkIndex());
        return get(getConvert(rand));
    }

    public MarkovWord put(String key, MarkovWord value) {
        int index;
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        int chain = getIndex(keyBytes);
        if (chain == - 1) {
            index = upIndex();
        } else {
            index = chain;
        }
        value.setIndex(index);
        setMarkov(keyBytes, value);
        this.convert.put(Base256.intToBuf(index),
                         keyBytes
        );
        return value;
    }

    public void setMarkov(byte[] key, MarkovWord word) {
        word.getWeights()
            .values()
            .forEach(wordWeight -> {
                checkWord(wordWeight.getWord());
            });
        this.head.put(key,
                      toBytes(word)
        );
    }

    public void checkWord(String key) {
        int index;
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        if (getIndex(keyBytes) == - 1) {
            index = upIndex();
        } else {
            return;
        }
        MarkovWord word = new MarkovWord(key);
        word.setIndex(index);
        setMarkov(keyBytes, word);
        this.convert.put(Base256.intToBuf(index),
                         keyBytes
        );
    }

    public int checkIndex() {
        byte[] bytes = this.convert.get("INDEX".getBytes(StandardCharsets.UTF_8));
        int result;
        if (bytes == null) {
            this.convert.put("INDEX".getBytes(StandardCharsets.UTF_8),
                             Base256.intToBuf(1)
            );
            result = 1;
        } else {
            result = Base256.intFromBuf(bytes);
        }
        return result;
    }

    private int upIndex() {
        byte[] bytes = this.convert.get("INDEX".getBytes(StandardCharsets.UTF_8));
        int result;
        if (bytes == null) {
            this.convert.put("INDEX".getBytes(StandardCharsets.UTF_8),
                             Base256.intToBuf(1)
            );
            result = 1;
        } else {
            result = Base256.intFromBuf(bytes) + 1;
            this.convert.put("INDEX".getBytes(StandardCharsets.UTF_8),
                             Base256.intToBuf(result)
            );
        }
        return result;
    }

    private byte[] toBytes(MarkovWord markovWord) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            output.write(Base256.intToBuf(markovWord.getIndex()));

            byte[] word = markovWord.getWord()
                                    .getBytes(StandardCharsets.UTF_8);
            output.write(Base256.intToBuf(word.length));
            output.write(word);
            for (Map.Entry<String, MarkovWordWeight> entry : markovWord.getWeights()
                                                                       .entrySet()) {
                String key = entry.getKey();
                MarkovWordWeight value = entry.getValue();
                output.write(Base256.intToBuf(getIndex(key.getBytes(StandardCharsets.UTF_8))));
                output.write(Base256.intToBuf(value.getWeight()));
            }
            return output.toByteArray();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    private MarkovWord toMarkovWord(byte[] bytes) {
        try {
            BytesReader reader = new BytesReader(bytes);
            int index = Base256.intFromBuf(reader.read(4));
            String word = new String(reader.read(Base256.intFromBuf(reader.read(4))),
                                     StandardCharsets.UTF_8
            );
            Map<String, MarkovWordWeight> weights = ApricotCollectionFactor.newHashMap();
            while (reader.readable(8)) {
                String key = getConvert(Base256.intFromBuf(reader.read(4)));
                int weight = Base256.intFromBuf(reader.read(4));
                weights.put(key,
                            new MarkovWordWeight(key,
                                                 weight
                            )
                );
            }

            MarkovWord markovWord = new MarkovWord(word);
            markovWord.setWeights(weights);
            markovWord.setIndex(index);
            return markovWord;
        } catch (Exception e) {
            return null;
        }
    }

    private int toIndex(byte[] bytes) {
        try {
            BytesReader reader = new BytesReader(bytes);
            return Base256.intFromBuf(reader.read(4));
        } catch (Exception e) {
            return - 1;
        }
    }

    private int getIndex(byte[] word) {
        return toIndex(this.head.get(word));
    }

    public MarkovWord get(String key) {
        return toMarkovWord(this.head.get(key.getBytes(StandardCharsets.UTF_8)));
    }

    public String getConvert(int index) {
        return new String(this.convert.get(Base256.intToBuf(index)),
                          StandardCharsets.UTF_8
        );
    }
}
