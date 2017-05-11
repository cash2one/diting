package com.diting.service;

import com.diting.model.SensitiveWord;
import com.diting.model.options.SensitiveWordOptions;
import com.diting.model.result.Results;

/**
 * Created by Administrator on 2017/5/3.
 */
public interface SensitiveWordService {
    SensitiveWord create(SensitiveWord sensitiveWord);

    SensitiveWord update(SensitiveWord sensitiveWord);

    Boolean delete(Integer id);

    Results<SensitiveWord> searchForPage(SensitiveWordOptions sensitiveWordOptions);

    SensitiveWord get(Integer id);
}
