package com.github.cao.awa.apricot.plugin.ext.hasnasu.handler.model.chain;

public class MarkovWordWeight {
    private String word;
    private int weight;

    public MarkovWordWeight(String word, int weight) {
        this.word = word;
        this.weight = weight;
    }

    public String getWord() {
        return this.word;
    }

    public MarkovWordWeight setWord(String word) {
        this.word = word;
        return this;
    }

    public int getWeight() {
        return this.weight;
    }

    public MarkovWordWeight setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public String toString() {
        return this.word + ":" + this.weight;
    }
}
