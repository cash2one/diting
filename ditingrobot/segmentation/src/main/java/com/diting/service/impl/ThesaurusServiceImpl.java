package com.diting.service.impl;

import com.diting.dao.ThesaurusMapper;
import com.diting.model.Thesaurus;
import com.diting.model.options.ThesaurusOptions;
import com.diting.model.result.Results;
import com.diting.service.ThesaurusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by liufei on 2017/1/3.
 */

@SuppressWarnings("ALL")
@Service
public class ThesaurusServiceImpl implements ThesaurusService{

    @Autowired
    private ThesaurusMapper thesaurusMapper;

    @Override
    public Thesaurus create(Thesaurus thesaurus) {
        thesaurusMapper.create(thesaurus);
        return thesaurus;
    }

    @Override
    public Thesaurus update(Thesaurus thesaurus) {
        thesaurusMapper.update(thesaurus);
        return thesaurus;
    }

    @Override
    public Boolean delete(Integer id) {
        return thesaurusMapper.delete(id);
    }

    @Override
    public Results<Thesaurus> searchForPage(ThesaurusOptions thesaurusOptions) {
        Results results = new Results();
        List<Thesaurus> thesaurusList = thesaurusMapper.searchForPage(thesaurusOptions);
        results.setItems(thesaurusList);
        results.setTotal(thesaurusOptions.getTotalRecord());
        return results;
    }

    @Override
    public Thesaurus get(Integer id){
        return thesaurusMapper.get(id);
    }
}
