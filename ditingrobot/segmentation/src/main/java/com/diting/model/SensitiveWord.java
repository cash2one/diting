package com.diting.model;

/**
 * 敏感词
 * Created by Administrator on 2017/5/3.
 */
public class SensitiveWord extends BaseModel{
    private String wordName;

    public String getWordName() {
        return wordName;
    }

    public void setWordName(String wordName) {
        this.wordName = wordName;
    }
}
