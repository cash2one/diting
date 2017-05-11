package com.diting.dao;

import com.diting.model.StaAccount;
import com.diting.model.options.PageableOptions;

import java.util.List;

/**
 * StaAccountMapper.
 */
public interface StaAccountMapper {

    void create(StaAccount staAccount);

    void update(StaAccount staAccount);

    List<StaAccount> searchForPage(PageableOptions pageableOptions);

    List<StaAccount> findAll();

}
