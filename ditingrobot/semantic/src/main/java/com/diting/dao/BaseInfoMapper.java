package com.diting.dao;

import com.diting.model.BaseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by liufei on 2016/9/28.
 */
public interface BaseInfoMapper {

    void create(BaseInfo baseInfo);

    void update(BaseInfo baseInfo);

    List<BaseInfo> get(@Param("companyId")Integer companyId);
}
