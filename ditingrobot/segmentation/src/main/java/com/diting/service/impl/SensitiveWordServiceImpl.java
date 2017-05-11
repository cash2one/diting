package com.diting.service.impl;

import com.diting.dao.SensitiveWordMapper;
import com.diting.model.SensitiveWord;
import com.diting.model.options.SensitiveWordOptions;
import com.diting.model.result.Results;
import com.diting.service.SensitiveWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
@SuppressWarnings("ALL")
@Service
public class SensitiveWordServiceImpl implements SensitiveWordService {

    @Autowired
    private SensitiveWordMapper sensitiveWordMapper;

    @Override
    public SensitiveWord create(SensitiveWord sensitiveWord) {
        sensitiveWordMapper.create(sensitiveWord);
        return sensitiveWord;
    }

    @Override
    public SensitiveWord update(SensitiveWord sensitiveWord) {
        sensitiveWordMapper.update(sensitiveWord);
        return sensitiveWord;
    }

    @Override
    public Boolean delete(Integer id) {
        return sensitiveWordMapper.delete(id);
    }

    @Override
    public Results<SensitiveWord> searchForPage(SensitiveWordOptions sensitiveWordOptions) {
        Results results = new Results();
        List<SensitiveWord> sensitiveWordList = sensitiveWordMapper.searchForPage(sensitiveWordOptions);
        results.setItems(sensitiveWordList);
        results.setTotal(sensitiveWordOptions.getTotalRecord());
        return results;
    }
    @Override
    public SensitiveWord get(Integer id){
        return sensitiveWordMapper.get(id);
    }
}
