package com.diting.service.impl;

import com.diting.dao.LianxiangReplaceWordMapper;
import com.diting.model.LianxiangReplaceWord;
import com.diting.model.options.LianxiangReplaceWordOptions;
import com.diting.model.result.Results;
import com.diting.service.LianxiangReplaceWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Administrator on 2017/1/3.
 */
@SuppressWarnings("ALL")
@Service
public class LianxiangReplaceWordServiceImpl implements LianxiangReplaceWordService {

    @Autowired
    private LianxiangReplaceWordMapper lianxiangReplaceWordMapper;

    @Override
    public LianxiangReplaceWord create(LianxiangReplaceWord lianxiangReplaceWord) {
        lianxiangReplaceWordMapper.create(lianxiangReplaceWord);
        return lianxiangReplaceWord;
    }

    @Override
    public LianxiangReplaceWord update(LianxiangReplaceWord lianxiangReplaceWord) {
        lianxiangReplaceWordMapper.update(lianxiangReplaceWord);
        return lianxiangReplaceWord;
    }

    @Override
    public Boolean delete(Integer id) {
        return lianxiangReplaceWordMapper.delete(id);
    }

    @Override
    public Results<LianxiangReplaceWord> searchForPage(LianxiangReplaceWordOptions lianxiangReplaceWordOptions) {
        Results results = new Results();
        List<LianxiangReplaceWord> thesaurusList = lianxiangReplaceWordMapper.searchForPage(lianxiangReplaceWordOptions);
        results.setItems(thesaurusList);
        results.setTotal(lianxiangReplaceWordOptions.getTotalRecord());
        return results;
    }
    @Override
    public LianxiangReplaceWord get(Integer id){
        return lianxiangReplaceWordMapper.get(id);
    }
}
