package com.diting.service;

import com.diting.model.Agent;
import com.diting.model.Company;
import com.diting.model.options.CompanyOptions;
import com.diting.model.result.Results;

import java.util.List;

/**
 * CompanyService.
 */
public interface CompanyService {
    Company create(Company company);

    Company save(Company company);

    Company update(Company company);

    Company get(Integer companyId);

    Boolean checkCompanyNameExists(String companyName);

    Results<Company> searchForPage(CompanyOptions options);

    Results<Company> searchForPageBytime(CompanyOptions options);

    Results<Company> searchAccountByWhereForPage(CompanyOptions options);

    Results<Company> searchAgentCompanyForPage(CompanyOptions options);

    List<Company> searchForKeyWord(String keyWord);
}
