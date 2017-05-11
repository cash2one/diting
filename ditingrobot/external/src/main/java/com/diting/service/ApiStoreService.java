package com.diting.service;

import com.diting.model.ApiStore;
import com.diting.model.options.ApiStoreOptions;
import com.diting.model.result.Results;
import com.diting.resources.request.ApiSaveRequest;

import java.util.List;

/**
 * ApiStoreService.
 */
public interface ApiStoreService {

    ApiStore create(ApiStore apiStore);

    ApiStore update(ApiStore apiStore);

    void switchUpdate(Integer apiStoreId, Integer status);

    void approvalUpdate(Integer apiStoreId, Integer status);

    void delete(Integer apiStoreId);

    ApiStore get(Integer apiStoreId);

    Results<ApiStore> searchForPage(ApiStoreOptions options);

    List<ApiStore> searchForValidity(ApiStoreOptions options);

    ApiStore save(ApiSaveRequest apiSaveRequest);

    ApiStore getByScene(String scene, Integer userId);
}
