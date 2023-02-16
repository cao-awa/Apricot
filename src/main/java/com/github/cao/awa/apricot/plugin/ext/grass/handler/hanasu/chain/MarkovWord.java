package com.github.cao.awa.apricot.plugin.ext.grass.handler.hanasu.chain;

import com.github.cao.awa.apricot.util.collection.ApricotCollectionFactor;

import java.util.Map;

public class MarkovWord {
    private int index = - 1;

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    private String word;
    private Map<String, MarkovWordWeight> weights = ApricotCollectionFactor.newConcurrentHashMap();

    public MarkovWord(String word) {
        this.word = word;
    }

    public String getWord() {
        return this.word;
    }

    public MarkovWord setWord(String word) {
        this.word = word;
        return this;
    }

    public Map<String, MarkovWordWeight> getWeights() {
        return this.weights;
    }

    public Map<String, Integer> weightMap() {
        Map<String, Integer> weights = ApricotCollectionFactor.newHashMap();
        getWeights().forEach((k, v) -> {
            weights.put(k,
                        v.getWeight()
            );
        });
        return weights;
    }

    public MarkovWord setWeights(Map<String, MarkovWordWeight> weights) {
        this.weights = weights;
        return this;
    }

    public String toString() {
        return this.word + "/" + this.weights.toString();
    }

    public MarkovWord setWeight(String word, MarkovWordWeight weight) {
        this.weights.put(word,
                         weight
        );
        return this;
    }
}
