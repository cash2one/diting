package com.diting.dao;

import com.diting.model.ReplaceWord;
import com.diting.model.options.ReplaceWordOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;


/**
 * Created by liufei on 2017/1/3.
 */
public interface ReplaceWordMapper {
    void create(ReplaceWord replaceWord);

    List<ReplaceWord> searchForPage(ReplaceWordOptions replaceWordOptions);

    void update(ReplaceWord replaceWord);

    boolean delete(@Param("id") Integer id);

    ReplaceWord get(@Param("id") Integer id);

    Boolean batchDelete(List<ReplaceWord> list);

    List<ReplaceWord> searchAll();
}
