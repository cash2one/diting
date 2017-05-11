package com.diting.service.impl;

import com.diting.dao.DictionaryMapper;
import com.diting.model.dict.Dictionary;
import com.diting.model.options.DictionaryOptions;
import com.diting.service.DictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("dictionaryService")
@Transactional
public class DictionaryServiceImpl implements DictionaryService {
    @Autowired
    private DictionaryMapper dictionaryMapper;

    @Override
    public Dictionary create(Dictionary dictionary){
        if(checkExists(dictionary)) return dictionary;
        dictionaryMapper.create(dictionary);
        return dictionary;
    }

    @Override
    public Dictionary update(Dictionary dictionary) {
        dictionaryMapper.update(dictionary);
        return dictionary;
    }

    @Override
    public Dictionary get(Integer dictId) {
        return dictionaryMapper.get(dictId);
    }

    @Override
    public List<Dictionary> search(DictionaryOptions options) {
        return dictionaryMapper.search(options);
    }

    private Boolean checkExists(Dictionary dictionary) {
        return dictionaryMapper.checkExists(dictionary);
    }

}
