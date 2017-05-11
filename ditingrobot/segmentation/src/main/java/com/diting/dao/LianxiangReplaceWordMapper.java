package com.diting.dao;

import com.diting.model.LianxiangReplaceWord;
import com.diting.model.options.LianxiangReplaceWordOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2017/1/3.
 */
public interface LianxiangReplaceWordMapper {
    void create(LianxiangReplaceWord lianxiangReplaceWord);

    List<LianxiangReplaceWord> searchForPage(LianxiangReplaceWordOptions lianxiangReplaceWordOptions);

    void update(LianxiangReplaceWord lianxiangReplaceWord);

    boolean delete(@Param("id") Integer id);

    LianxiangReplaceWord get(@Param("id") Integer id);
}
