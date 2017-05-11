package com.diting.model;

/**
 * VoteLog
 */
public class VoteLog extends BaseModel  {

    public VoteLog(String vote, String voted) {
        this.vote = vote;
        this.voted = voted;
    }

    private String vote;
    private String voted;

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public String getVoted() {
        return voted;
    }

    public void setVoted(String voted) {
        this.voted = voted;
    }
}
