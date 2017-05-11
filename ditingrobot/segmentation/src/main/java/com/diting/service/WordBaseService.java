package com.diting.service;

import com.diting.model.WordBase;
import com.diting.model.options.WordBaseOptions;
import com.diting.model.result.Results;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */
public interface WordBaseService {

    WordBase create(WordBase wordBase);

    WordBase update(WordBase wordBase);

    Boolean delete(Integer id);

    Results<WordBase> searchForPage(WordBaseOptions wordBaseOptions);

    WordBase get(Integer id);

    Boolean adminBatchDelete(String[] strings);

    Map<String, List<WordBase>> getWordBaseMap();
}
