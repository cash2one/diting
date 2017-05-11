package com.diting.model;

/**
 * Created by liufei on 2017/1/3.
 */
public class Thesaurus {
    private Integer id;
    private String word;//词
    private Integer word_frequency;//词频

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getWord_frequency() {
        return word_frequency;
    }

    public void setWord_frequency(Integer word_frequency) {
        this.word_frequency = word_frequency;
    }
}
