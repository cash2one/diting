package com.diting.service.impl;

import com.diting.dao.SynonymMapper;
import com.diting.model.Synonym;
import com.diting.model.WordBase;
import com.diting.model.options.SynonymOptions;
import com.diting.model.result.Results;
import com.diting.service.SynonymService;
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
public class SynonymServiceImpl implements SynonymService{

    @Autowired
    private SynonymMapper synonymMapper;

    @Override
    public Synonym create(Synonym synonym) {
        synonymMapper.create(synonym);
        return synonym;
    }

    @Override
    public Synonym update(Synonym synonym) {
        synonymMapper.update(synonym);
        return synonym;
    }

    @Override
    public Boolean delete(Integer id) {
        return synonymMapper.delete(id);
    }

    @Override
    public Results<Synonym> searchForPage(SynonymOptions synonymOptions) {
        Results results = new Results();
        List<Synonym> thesaurusList = synonymMapper.searchForPage(synonymOptions);
        results.setItems(thesaurusList);
        results.setTotal(synonymOptions.getTotalRecord());
        return results;
    }

    @Override
    public Synonym get(Integer id){
        return synonymMapper.get(id);
    }

    @Override
    public Map<String, List<Synonym>> getSynonymMap(){
        Map<String, List<Synonym>> synonymMap =new HashMap<>();
        List<Synonym> synonyms=synonymMapper.searchAll();
        for (Synonym synonym:synonyms) {
            List<Synonym> list=new ArrayList<>();
            if (synonymMap.get(synonym.getWord_old()) != null) {
                list = synonymMap.get(synonym.getWord_old());
                list.add(synonym);
            } else {
                list.add(synonym);
            }
            synonymMap.put(synonym.getWord_old(), list);
        }
        return synonymMap;
    }
}
