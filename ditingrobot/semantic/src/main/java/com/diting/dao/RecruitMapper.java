package com.diting.dao;

import com.diting.model.Recruit;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface RecruitMapper {
    void create(Recruit recruit);

    void update(Recruit recruit);

    boolean delete(@Param("id") Integer id);
}
