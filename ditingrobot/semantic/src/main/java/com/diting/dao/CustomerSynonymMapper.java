package com.diting.dao;

import com.diting.model.CustomerSynonym;
import com.diting.model.options.CustomerSynonymOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2016/9/8.
 */
public interface CustomerSynonymMapper {

    void create(CustomerSynonym customerSynonym);

    void update(CustomerSynonym customerSynonym);

    CustomerSynonym get(@Param("customerSynonymId")Integer customerSynonymId);

    List<CustomerSynonym> searchForPage(CustomerSynonymOptions options);

    List<CustomerSynonym> getCustomerSynonym();

    List<CustomerSynonym> getCustomerSynonymByUserId(@Param("accountId")Integer accountId);

    void delete(@Param("customerSynonymId")Integer customerSynonymId);
}
