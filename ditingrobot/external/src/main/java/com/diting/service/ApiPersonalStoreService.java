package com.diting.service;

import com.diting.model.ApiPersonalStore;
import com.diting.model.options.ApiPersonalStoreOptions;
import com.diting.model.result.Results;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public interface ApiPersonalStoreService {
    ApiPersonalStore create(ApiPersonalStore apiPersonalStore);
    List<ApiPersonalStore> searchPersonal();
    Results<ApiPersonalStore> searchPersonalForPage(ApiPersonalStoreOptions options);
    ApiPersonalStore update(ApiPersonalStore apiPersonalStore);
    ApiPersonalStore get(Integer apiPersonalStoreId);
    Boolean batchDelete(String[] strings);
    Boolean batchSubmit(String[] strings);
    void switchUpdate(Integer apiStoreId, Integer status);
    ApiPersonalStore findApiPersonalStoreBySceneAndUserId(String scene, Integer userId);
}
