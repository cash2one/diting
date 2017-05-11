package com.diting.service.impl;

import com.diting.dao.ApiStoreMapper;
import com.diting.dao.ParameterMapper;
import com.diting.model.ApiStore;
import com.diting.model.Parameter;
import com.diting.model.options.ApiStoreOptions;
import com.diting.model.result.Results;
import com.diting.resources.request.ApiSaveRequest;
import com.diting.service.ApiStoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("apiStoreService")
@Transactional
public class ApiStoreServiceImpl implements ApiStoreService {
    @Autowired
    private ApiStoreMapper apiStoreMapper;

    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public ApiStore create(ApiStore apiStore) {
        apiStoreMapper.create(apiStore);
        return apiStore;
    }

    @Override
    public ApiStore update(ApiStore apiStore) {
        apiStoreMapper.update(apiStore);
        return apiStore;
    }

    @Override
    public void switchUpdate(Integer apiStoreId, Integer status) {
        apiStoreMapper.switchUpdate(apiStoreId, status);
    }

    @Override
    public void approvalUpdate(Integer apiStoreId, Integer status) {
        apiStoreMapper.approvalUpdate(apiStoreId, status);
    }

    @Override
    public void delete(Integer apiStoreId) {
        apiStoreMapper.delete(apiStoreId);
    }

    @Override
    public ApiStore get(Integer apiStoreId) {
        return apiStoreMapper.get(apiStoreId);
    }

    @Override
    public ApiStore save(ApiSaveRequest apiSaveRequest) {
        ApiStore apiStore = apiSaveRequest.getApiStore();
        apiStore.setEnable(0);
        apiStore.setApprovalEnable(0);
        apiStoreMapper.create(apiStore);

        List<Parameter> parameters = apiSaveRequest.getParameters();
        for (Parameter parameter : parameters) {
            parameter.setApiId(apiStore.getId());
            parameterMapper.create(parameter);
        }
        return apiStore;
    }

    @Override
    public ApiStore getByScene(String scene, Integer userId) {
        return apiStoreMapper.getByScene(scene, userId);
    }

    @Override
    public Results<ApiStore> searchForPage(ApiStoreOptions options) {
        Results results = new Results();
        List<ApiStore> apis =  apiStoreMapper.searchForPage(options);
        results.setItems(apis);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    @Override
    public List<ApiStore> searchForValidity(ApiStoreOptions options) {
        return apiStoreMapper.searchForValidity(options);
    }

}
