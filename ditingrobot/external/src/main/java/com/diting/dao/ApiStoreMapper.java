package com.diting.dao;

import com.diting.model.ApiStore;
import com.diting.model.options.ApiStoreOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ApiStoreMapper
 */
public interface ApiStoreMapper {
    void create(ApiStore apiStore);

    void switchUpdate(@Param("apiStoreId") Integer apiStoreId, @Param("status") Integer status);

    void approvalUpdate(@Param("apiStoreId") Integer apiStoreId, @Param("status") Integer status);

    void update(ApiStore apiStore);

    void delete(@Param("apiStoreId") Integer apiStoreId);

    ApiStore get(@Param("apiStoreId") Integer apiStoreId);

    ApiStore getByScene(@Param("scene") String scene, @Param("userId") Integer userId);

    List<ApiStore> searchForPage(ApiStoreOptions options);

    List<ApiStore> searchForValidity(ApiStoreOptions options);

    ApiStore findByScene(@Param("scene") String scene);

}
