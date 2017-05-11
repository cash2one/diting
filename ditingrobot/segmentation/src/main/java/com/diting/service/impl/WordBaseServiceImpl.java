package com.diting.service.impl;

import com.diting.dao.WordBaseMapper;
import com.diting.model.ReplaceWord;
import com.diting.model.WordBase;
import com.diting.model.options.WordBaseOptions;
import com.diting.model.result.Results;
import com.diting.service.WordBaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/12/30.
 */
@SuppressWarnings("ALL")
@Service("wordBaseService")
public class WordBaseServiceImpl implements WordBaseService{

    @Autowired
    private WordBaseMapper wordBaseMapper;

    @Override
    public WordBase create(WordBase wordBase) {
        wordBaseMapper.create(wordBase);
        return wordBase;
    }

    @Override
    public WordBase update(WordBase wordBase) {
        wordBaseMapper.update(wordBase);
        return wordBase;
    }

    @Override
    public Boolean delete(Integer id) {
        return wordBaseMapper.delete(id);
    }

    @Override
    public Results<WordBase> searchForPage(WordBaseOptions wordBaseOptions) {
        Results results = new Results();
        List<WordBase> internalUsers = wordBaseMapper.searchForPage(wordBaseOptions);
        results.setItems(internalUsers);
        results.setTotal(wordBaseOptions.getTotalRecord());
        return results;
    }

    @Override
    public WordBase get(Integer id){
        return wordBaseMapper.get(id);
    }

    @Override
    public Boolean adminBatchDelete(String[] strings){
        List<WordBase> list = new ArrayList<>();
        for (String string : strings) {
            WordBase wordBase = new WordBase();
            wordBase.setId(Integer.valueOf(string));
            list.add(wordBase);
        }
        return wordBaseMapper.batchDelete(list);
    }

    @Override
    public Map<String, List<WordBase>> getWordBaseMap(){
        Map<String, List<WordBase>> wordBaseMap =new HashMap<>();
        List<WordBase> wordBases=wordBaseMapper.searchAll();
        for (WordBase wordBase:wordBases) {
            List<WordBase> list=new ArrayList<>();
            if (wordBaseMap.get(wordBase.getWord()) != null) {
                list = wordBaseMap.get(wordBase.getWord());
                list.add(wordBase);
            } else {
                list.add(wordBase);
            }
            wordBaseMap.put(wordBase.getWord(), list);
        }
        return wordBaseMap;
    }
}
