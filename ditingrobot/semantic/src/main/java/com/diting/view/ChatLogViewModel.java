package com.diting.view;

/**
 * Created by Administrator on 2016/11/28.
 */
public class ChatLogViewModel {

    private String question;
    private String answer;
    private String deleted;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }
}
