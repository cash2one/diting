package com.diting.service.impl;

import com.diting.core.Universe;
import com.diting.dao.ApiPersonalStoreMapper;
import com.diting.dao.ApiStoreMapper;
import com.diting.error.AppErrors;
import com.diting.model.ApiPersonalStore;
import com.diting.model.ApiStore;
import com.diting.model.options.ApiPersonalStoreOptions;
import com.diting.model.result.Results;
import com.diting.service.ApiPersonalStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.diting.util.Utils.isEmpty;

/**
 * Created by Administrator on 2017/3/7.
 */
@SuppressWarnings("ALL")
@Service
public class ApiPersonalStoreServiceImpl implements ApiPersonalStoreService{

    @Autowired
    private ApiPersonalStoreMapper apiPersonalStoreMapper;

    @Autowired
    private ApiStoreMapper apiStoreMapper;

    @Override
    public ApiPersonalStore create(ApiPersonalStore apiPersonalStore) {
        Integer userId= Universe.current().getUserId();
        apiPersonalStore.setUserId(userId);
        List<ApiPersonalStore> apiPersonalStores=apiPersonalStoreMapper.searchPersonal(userId);
        ApiPersonalStore apiPersonalStore1=apiPersonalStoreMapper.findByScene(apiPersonalStore);
        ApiStore apiStore=apiStoreMapper.findByScene(apiPersonalStore.getScene());
        if (userId==null)
            throw AppErrors.INSTANCE.common("用户未登录").exception();
        if (null!=apiPersonalStores&&apiPersonalStores.size()>=10)
            throw AppErrors.INSTANCE.common("应用最多添加十条").exception();
        if (null!=apiPersonalStore1&&!isEmpty(apiPersonalStore1.getScene()))
            throw AppErrors.INSTANCE.common("场景已存在，请重新设置场景").exception();
        apiPersonalStore.setUserId(userId);
        apiPersonalStore.setEnable(0);
        apiPersonalStore.setApprovalEnable(0);
        apiPersonalStoreMapper.create(apiPersonalStore);
        return apiPersonalStore;
    }

    @Override
    public List<ApiPersonalStore> searchPersonal() {
        Integer userId= Universe.current().getUserId();
        if (userId==null)
            throw AppErrors.INSTANCE.common("用户未登录").exception();
        List<ApiPersonalStore> apiPersonalStores=apiPersonalStoreMapper.searchPersonal(userId);
        return apiPersonalStores;
    }

    @Override
    public Results<ApiPersonalStore> searchPersonalForPage(ApiPersonalStoreOptions options) {
        Results results = new Results();
        List<ApiPersonalStore> apis =  apiPersonalStoreMapper.searchPersonalForPage(options);
        results.setItems(apis);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public ApiPersonalStore update(ApiPersonalStore apiPersonalStore) {
        apiPersonalStoreMapper.update(apiPersonalStore);
        return apiPersonalStore;
    }

    @Override
    public ApiPersonalStore get(Integer apiPersonalStoreId) {
        return apiPersonalStoreMapper.get(apiPersonalStoreId);
    }

    @Override
    public Boolean batchDelete(String[] strings) {
        List<Integer> list = new ArrayList<>();
        for (String string : strings) {
//            ApiPersonalStore apiPersonalStore = apiPersonalStoreMapper.get(Integer.valueOf(string));
            list.add(Integer.valueOf(string));
        }
        return apiPersonalStoreMapper.batchDelete(list);
    }

    @Override
    public Boolean batchSubmit(String[] strings) {
        for (String string:strings) {
            ApiPersonalStore store=apiPersonalStoreMapper.get(Integer.valueOf(string));
            ApiStore apiStores=apiStoreMapper.findByScene(store.getScene());
            if (null!=apiStores){
                continue;
            }
            if (null!=store){
                ApiStore apiStore=new ApiStore();
                apiStore.setAddress(store.getAddress());
                apiStore.setDescription(store.getDescription());
                apiStore.setEnable(0);
                apiStore.setApprovalEnable(0);
                apiStore.setEmail(store.getEmail());
                apiStore.setKeywords(store.getKeywords());
                apiStore.setEnName(store.getEnName());
                apiStore.setMethod(store.getMethod());
                apiStore.setMobile(store.getMobile());
                apiStore.setName(store.getName());
                apiStore.setScene(store.getScene());
                apiStore.setProviderEnName(store.getProviderEnName());
                apiStore.setServerName(store.getServerName());
                apiStore.setServerEnName(store.getServerEnName());
                apiStore.setProviderName(store.getProviderName());
                apiStore.setUrl(store.getUrl());
                apiStore.setVersion(store.getVersion());
                apiStoreMapper.create(apiStore);
            }
        }
        return true;
    }

    @Override
    public void switchUpdate(Integer apiStoreId, Integer status) {
        apiPersonalStoreMapper.switchUpdate(apiStoreId, status);
    }

    @Override
    public ApiPersonalStore findApiPersonalStoreBySceneAndUserId( String scene,  Integer userId) {
        return apiPersonalStoreMapper.findApiPersonalStoreBySceneAndUserId(scene,userId);
    }
}
