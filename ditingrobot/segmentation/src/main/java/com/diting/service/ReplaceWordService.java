package com.diting.service;

import com.diting.model.ReplaceWord;
import com.diting.model.options.ReplaceWordOptions;
import com.diting.model.result.Results;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface ReplaceWordService {
    ReplaceWord create(ReplaceWord replaceWord);

    ReplaceWord update(ReplaceWord replaceWord);

    Boolean delete(Integer id);

    Results<ReplaceWord> searchForPage(ReplaceWordOptions replaceWordOptions);

    ReplaceWord get(Integer id);

    Boolean adminBatchDelete(String[] strings);

    Map<String, List<ReplaceWord>> getReplaceWordMap();
}
