package com.diting.dao;

import com.diting.model.ChatStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public interface ChatStatisticsMapper {
    void create(ChatStatistics chatStatistics);
    void deleteAll();
    List<ChatStatistics> getAllRanking();
    ChatStatistics findByCompanyId(@Param("companyId") Integer companyId);
}
