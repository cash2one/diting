package com.diting.dao;

import com.diting.model.Company;
import com.diting.model.options.CompanyOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * CompanyMapper.
 */
public interface CompanyMapper {

    void create(Company company);

    void update(Company company);

    Company get(@Param("companyId")Integer companyId);

    boolean checkNameExists(@Param("companyId") Integer companyId, @Param("name") String name);

    boolean checkCompanyNameExists(@Param("companyName") String companyName);

    List<Company> searchForPage(CompanyOptions companyOptions);

    List<Company> searchForKeyWord(@Param("keyWord") String keyWord);
}
