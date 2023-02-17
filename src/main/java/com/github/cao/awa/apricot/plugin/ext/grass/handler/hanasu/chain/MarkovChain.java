package com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain;

import com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain.leveldb.LevelDbChainMap;
import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;
import com.github.zhuaidadaya.rikaishinikui.handler.universal.entrust.EntrustEnvironment;

import java.util.Map;

public class MarkovChain {
    private static final MarkovWordWeight ZERO_WEIGHT = new MarkovWordWeight("",
                                                                             0
    );
    private final LevelDbChainMap wordsStore;
    private final Map<String, MarkovWord> words = ApricotCollectionFactor.newConcurrentHashMap();

    public MarkovChain(LevelDbChainMap map) {
        this.wordsStore = map;
    }

    public void add(String str, MarkovWord word) {
        this.words.put(str,
                       word
        );
    }

    public int length() {
        return this.wordsStore.checkIndex();
    }

    public void addWeight(String str, MarkovWordWeight inputWeight) {
        MarkovWord word = this.words.get(str);
        if (word == null) {
            word = new MarkovWord(str);
            add(str,
                word
            );
        }
        if (inputWeight != null) {
            MarkovWordWeight sourceWeight = word.getWeights()
                                                .get(inputWeight.getWord());
            if (sourceWeight == null) {
                word.setWeight(inputWeight.getWord(),
                               inputWeight
                );
            } else {
                sourceWeight.setWeight(sourceWeight.getWeight() + inputWeight.getWeight());
            }
        }

        add(str,
            word
        );
    }

    public void save() {
        for (Map.Entry<String, MarkovWord> entry : this.words.entrySet()) {
            String key = entry.getKey();
            MarkovWord value = entry.getValue();
            MarkovWord weight = this.wordsStore.get(key);
            compound(value,
                     weight
            );

            this.wordsStore.put(key,
                                value
            );
        }

        this.words.clear();
    }

    public void compound(MarkovWord source, MarkovWord input) {
        if (input == null || source == null) {
            return;
        }
        for (MarkovWordWeight target : source.getWeights()
                                             .values()) {
            source.setWeight(target.getWord(),
                             new MarkovWordWeight(input.getWord(),
                                                  EntrustEnvironment.getNotNull(input.getWeights()
                                                                                     .get(target.getWord()),
                                                                                ZERO_WEIGHT
                                                                    )
                                                                    .getWeight() +
                                                          target.getWeight()
                             )
            );
        }
    }

    public MarkovWord get(String str) {
        MarkovWord word = this.words.get(str);
        MarkovWord store = this.wordsStore.get(str);
        if (word != null) {
            compound(store,
                     word
            );
            add(str,
                store
            );
            this.words.remove(str);
        }
        return store;
    }

    public LevelDbChainMap get() {
        return this.wordsStore;
    }

    public Map<String, MarkovWord> getNav() {
        return this.words;
    }
}
