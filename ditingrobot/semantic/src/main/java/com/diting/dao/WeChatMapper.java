package com.diting.dao;

import com.diting.model.WeChatAccount;
import com.diting.model.options.WeChatAccountOptions;
import org.apache.ibatis.annotations.Param;

/**
 * WeChatMapper.
 */
public interface WeChatMapper {
    void create(WeChatAccount weChatAccount);

    boolean delete(WeChatAccount weChatAccount);

    WeChatAccount get(@Param("originalId") String originalId);

    Integer searchAllCountByTime(WeChatAccountOptions options);
}
