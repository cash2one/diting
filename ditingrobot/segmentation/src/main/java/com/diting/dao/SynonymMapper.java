package com.diting.dao;

import com.diting.model.Synonym;
import com.diting.model.options.SynonymOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2017/1/3.
 */
public interface SynonymMapper {
    void create(Synonym synonym);

    List<Synonym> searchForPage(SynonymOptions synonymOptions);

    void update(Synonym synonym);

    boolean delete(@Param("id") Integer id);

    Synonym get(@Param("id") Integer id);

    List<Synonym> searchAll();
}
