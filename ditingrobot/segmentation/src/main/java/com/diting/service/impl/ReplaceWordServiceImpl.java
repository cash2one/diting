package com.diting.service.impl;

import com.diting.dao.ReplaceWordMapper;
import com.diting.model.ReplaceWord;
import com.diting.model.WordBase;
import com.diting.model.options.ReplaceWordOptions;
import com.diting.model.result.Results;
import com.diting.service.ReplaceWordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
@SuppressWarnings("ALL")
@Service
public class ReplaceWordServiceImpl implements ReplaceWordService{

    @Autowired
    private ReplaceWordMapper replaceWordMapper;

    @Override
    public ReplaceWord create(ReplaceWord replaceWord) {
        replaceWordMapper.create(replaceWord);
        return replaceWord;
    }

    @Override
    public ReplaceWord update(ReplaceWord replaceWord) {
        replaceWordMapper.update(replaceWord);
        return replaceWord;
    }

    @Override
    public Boolean delete(Integer id) {
        return replaceWordMapper.delete(id);
    }

    @Override
    public Results<ReplaceWord> searchForPage(ReplaceWordOptions replaceWordOptions) {
        Results results = new Results();
        List<ReplaceWord> thesaurusList = replaceWordMapper.searchForPage(replaceWordOptions);
        results.setItems(thesaurusList);
        results.setTotal(replaceWordOptions.getTotalRecord());
        return results;
    }

    @Override
    public ReplaceWord get(Integer id){
        return replaceWordMapper.get(id);
    }

    @Override
    public Boolean adminBatchDelete(String[] strings){
        List<ReplaceWord> list = new ArrayList<>();
        for (String string : strings) {
            ReplaceWord replaceWord = new ReplaceWord();
            replaceWord.setId(Integer.valueOf(string));
            list.add(replaceWord);
        }
        return replaceWordMapper.batchDelete(list);
    }

    @Override
    public Map<String, List<ReplaceWord>> getReplaceWordMap(){
        Map<String, List<ReplaceWord>> replaceWordMap =new HashMap<>();
        List<ReplaceWord> replaceWords=replaceWordMapper.searchAll();
        for (ReplaceWord replaceWord:replaceWords) {
            List<ReplaceWord> list=new ArrayList<>();
            if (replaceWordMap.get(replaceWord.getPre_replace_word()) != null) {
                list = replaceWordMap.get(replaceWord.getPre_replace_word());
                list.add(replaceWord);
            } else {
                list.add(replaceWord);
            }
            replaceWordMap.put(replaceWord.getPre_replace_word(), list);
        }
        return replaceWordMap;
    }
}
