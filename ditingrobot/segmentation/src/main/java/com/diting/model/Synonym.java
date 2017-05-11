package com.diting.model;

/**
 * 同义词
 * word_old 词
 * word_new用于替换word_old
 * word_old_frequency词出现次数
 * word_new_frequency 必须保证表里word_new_frequency>word_old_frequency
 * Created by Administrator on 2017/1/3.
 */
public class Synonym {
    private Integer id;
    private String word_old;
    private String word_new;
    private Integer word_old_frequency;
    private Integer word_new_frequency;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getWord_old() {
        return word_old;
    }

    public void setWord_old(String word_old) {
        this.word_old = word_old;
    }

    public String getWord_new() {
        return word_new;
    }

    public void setWord_new(String word_new) {
        this.word_new = word_new;
    }

    public Integer getWord_old_frequency() {
        return word_old_frequency;
    }

    public void setWord_old_frequency(Integer word_old_frequency) {
        this.word_old_frequency = word_old_frequency;
    }

    public Integer getWord_new_frequency() {
        return word_new_frequency;
    }

    public void setWord_new_frequency(Integer word_new_frequency) {
        this.word_new_frequency = word_new_frequency;
    }
}
