package com.diting.dao;

import com.diting.model.ApiPersonalStore;
import com.diting.model.options.ApiPersonalStoreOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2017/3/7.
 */
public interface ApiPersonalStoreMapper {
    void create(ApiPersonalStore apiPersonalStore);
    List<ApiPersonalStore> searchPersonalForPage(ApiPersonalStoreOptions options);
    List<ApiPersonalStore> searchPersonal(@Param("userId") Integer userId);
    ApiPersonalStore findByScene(ApiPersonalStore apiPersonalStore);
    void update(ApiPersonalStore apiPersonalStore);
    ApiPersonalStore get(@Param("apiPersonalStoreId") Integer apiPersonalStoreId);
    boolean batchDelete(@Param("ids") List<Integer> list);
    void delete(@Param("id") Integer id);
    void switchUpdate(@Param("apiPersonalStoreId") Integer apiPersonalStoreId, @Param("status") Integer status);
    ApiPersonalStore findApiPersonalStoreBySceneAndUserId(@Param("scene") String scene, @Param("userId") Integer userId);
}
