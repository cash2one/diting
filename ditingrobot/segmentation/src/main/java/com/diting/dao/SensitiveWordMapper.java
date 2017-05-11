package com.diting.dao;

import com.diting.model.SensitiveWord;
import com.diting.model.options.SensitiveWordOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2017/1/3.
 */
public interface SensitiveWordMapper {
    void create(SensitiveWord sensitiveWord);

    List<SensitiveWord> searchForPage(SensitiveWordOptions sensitiveWordOptions);

    void update(SensitiveWord sensitiveWord);

    boolean delete(@Param("id") Integer id);

    SensitiveWord get(@Param("id") Integer id);
}
