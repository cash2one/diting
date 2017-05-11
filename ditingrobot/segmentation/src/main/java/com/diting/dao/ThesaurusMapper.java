package com.diting.dao;

import com.diting.model.Thesaurus;
import com.diting.model.options.ThesaurusOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface ThesaurusMapper {
    void create(Thesaurus thesaurus);

    List<Thesaurus> searchForPage(ThesaurusOptions thesaurusOptions);

    void update(Thesaurus thesaurus);

    boolean delete(@Param("id") Integer id);

    Thesaurus get(@Param("id") Integer id);
}
