package com.diting.service;

import com.diting.model.Thesaurus;
import com.diting.model.options.ThesaurusOptions;
import com.diting.model.result.Results;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface ThesaurusService {
    Thesaurus create(Thesaurus thesaurus);

    Thesaurus update(Thesaurus thesaurus);

    Boolean delete(Integer id);

    Results<Thesaurus> searchForPage(ThesaurusOptions thesaurusOptions);

    Thesaurus get(Integer id);
}
