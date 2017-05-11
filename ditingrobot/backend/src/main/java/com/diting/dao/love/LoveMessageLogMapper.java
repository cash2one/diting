package com.diting.dao.love;

import com.diting.model.love.LoveMessageLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * LoveMessageLogMapper.
 */
public interface LoveMessageLogMapper {
    List<LoveMessageLog> selectByUserId(Integer userId);
    void create(LoveMessageLog loveMessageLog);
    Integer selectCountByUserId(Integer userId);
    void updateByUserId(Integer userId);
    Integer selectCountByLoveId(@Param("loveId")Integer loveId);
    List<Map<String ,Object>> selectCountMap();
}
