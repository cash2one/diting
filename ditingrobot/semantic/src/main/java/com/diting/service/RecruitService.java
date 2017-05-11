package com.diting.service;

import com.diting.model.Recruit;

/**
 * Created by Administrator on 2017/1/4.
 */
public interface RecruitService {
    Recruit create(Recruit recruit);

    Recruit update(Recruit recruit);

    Boolean delete(Integer id);

}
