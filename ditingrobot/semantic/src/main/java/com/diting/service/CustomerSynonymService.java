package com.diting.service;

import com.diting.model.CustomerSynonym;
import com.diting.model.options.CustomerSynonymOptions;
import com.diting.model.result.Results;

/**
 * Created by liufei on 2016/9/8.
 */
public interface CustomerSynonymService {

    CustomerSynonym create(CustomerSynonym customerSynonym);

    CustomerSynonym update(CustomerSynonym customerSynonym);

    CustomerSynonym get(Integer customerSynonymId);

    void delete(Integer customerSynonymId);

    Results<CustomerSynonym> searchForPage(CustomerSynonymOptions options);
}
