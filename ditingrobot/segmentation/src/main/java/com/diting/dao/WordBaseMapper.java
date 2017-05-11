package com.diting.dao;

import com.diting.model.WordBase;
import com.diting.model.options.WordBaseOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2016/12/30.
 */
public interface WordBaseMapper {

    void create(WordBase wordBase);

    List<WordBase> searchForPage(WordBaseOptions wordBaseOptions);

    void update(WordBase wordBase);

    boolean delete(@Param("id") Integer id);

    WordBase get(@Param("id") Integer id);

    Boolean batchDelete(List<WordBase> list);

    List<WordBase> searchAll();
}
