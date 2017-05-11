package com.diting.model.mongo;

import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Records
 */
@Document(collection = "diting")
public class Records extends BaseMongoModel {

    public Records() {
    }

    private String question;
    private String answer;

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
