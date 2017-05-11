package com.diting.model;

/**
 * 口语处理
 * 切词前词语替换
 * word 词
 * word_size长度
 * place位置
 * eliminate排除的句子，用‘，’隔开
 * Created by Administrator on 2017/1/3.
 */
public class OralTreatment extends BaseModel{
    private String word;
    private String word_place;
    private Integer word_size;
    private String eliminate;


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWord_place() {
        return word_place;
    }

    public void setWord_place(String word_place) {
        this.word_place = word_place;
    }

    public Integer getWord_size() {
        return word_size;
    }

    public void setWord_size(Integer word_size) {
        this.word_size = word_size;
    }

    public String getEliminate() {
        return eliminate;
    }

    public void setEliminate(String eliminate) {
        this.eliminate = eliminate;
    }
}
