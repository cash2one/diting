package com.diting.dao;

import com.diting.model.VoteLog;
import org.apache.ibatis.annotations.Param;

/**
 * VoteLogMapper
 */
public interface VoteLogMapper {

    void create(VoteLog voteLog);

    Integer getVoteCount(@Param("vote")String vote);

    boolean checkVoteExists(@Param("vote") String vote,@Param("voted") String voted);

}
