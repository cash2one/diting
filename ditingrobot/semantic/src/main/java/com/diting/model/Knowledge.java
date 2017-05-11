package com.diting.model;


/**
 * Knowledge
 */
public class Knowledge extends BaseModel {
    //account info
    private Integer accountId;
    private Integer companyId;   //公司id

    //knowledge info
    private String question;
    private String handleQuestion;
    private String answer;
    private String kw1;
    private String kw2;
    private String kw3;
    private String kw4;
    private String kw5;
    private String scene;  //场景
    private int emotion; //情感
    private int frequency;  //调用次数
    private String  ids;
    private String actionOption;
    private String synonymQuestion;
    private String img_url;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getHandleQuestion() {
        return handleQuestion;
    }

    public void setHandleQuestion(String handleQuestion) {
        this.handleQuestion = handleQuestion;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getKw1() {
        return kw1;
    }

    public void setKw1(String kw1) {
        this.kw1 = kw1;
    }

    public String getKw2() {
        return kw2;
    }

    public void setKw2(String kw2) {
        this.kw2 = kw2;
    }

    public String getKw3() {
        return kw3;
    }

    public void setKw3(String kw3) {
        this.kw3 = kw3;
    }

    public String getKw4() {
        return kw4;
    }

    public void setKw4(String kw4) {
        this.kw4 = kw4;
    }

    public String getKw5() {
        return kw5;
    }

    public void setKw5(String kw5) {
        this.kw5 = kw5;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getActionOption() {
        return actionOption;
    }

    public void setActionOption(String actionOption) {
        this.actionOption = actionOption;
    }

    public String getSynonymQuestion() {
        return synonymQuestion;
    }

    public void setSynonymQuestion(String synonymQuestion) {
        this.synonymQuestion = synonymQuestion;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }
}
