package com.diting.dao;

import com.diting.model.dict.Dictionary;
import com.diting.model.options.DictionaryOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DictionaryMapper.
 */
public interface DictionaryMapper {

    void create(Dictionary dictionary);

    void update(Dictionary dictionary);

    Dictionary get(@Param("dictId") Integer dictId);

    List<Dictionary> search(DictionaryOptions options);

    boolean checkExists(Dictionary dictionary);

}
