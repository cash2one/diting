package com.diting.service;

import com.diting.model.Synonym;
import com.diting.model.WordBase;
import com.diting.model.options.SynonymOptions;
import com.diting.model.options.WordBaseOptions;
import com.diting.model.result.Results;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface SynonymService {
    Synonym create(Synonym synonym);

    Synonym update(Synonym synonym);

    Boolean delete(Integer id);

    Results<Synonym> searchForPage(SynonymOptions synonymOptions);

    Synonym get(Integer id);

    Map<String, List<Synonym>> getSynonymMap();
}
