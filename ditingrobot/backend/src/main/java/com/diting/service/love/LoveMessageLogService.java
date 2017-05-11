package com.diting.service.love;

import com.diting.model.love.LoveMessageLog;

import java.util.List;
import java.util.Map;

/**
 * Created by shenkun on 2017/3/10.
 */
public interface LoveMessageLogService {
    List<LoveMessageLog> selectByUserId();
    LoveMessageLog create(LoveMessageLog loveMessageLog);
    Integer selectCountByUserId();
    void updateByUserId();
    Integer selectCountByLoveId(Integer loveId);
    List<Map<String,Object>> selectCountMap();
}
