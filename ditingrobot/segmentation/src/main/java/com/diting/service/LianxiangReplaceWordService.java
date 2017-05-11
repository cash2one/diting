package com.diting.service;

import com.diting.model.LianxiangReplaceWord;
import com.diting.model.options.LianxiangReplaceWordOptions;
import com.diting.model.result.Results;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface LianxiangReplaceWordService {
    LianxiangReplaceWord create(LianxiangReplaceWord lianxiangReplaceWord);

    LianxiangReplaceWord update(LianxiangReplaceWord lianxiangReplaceWord);

    Boolean delete(Integer id);

    Results<LianxiangReplaceWord> searchForPage(LianxiangReplaceWordOptions lianxiangReplaceWordOptions);

    LianxiangReplaceWord get(Integer id);
}
