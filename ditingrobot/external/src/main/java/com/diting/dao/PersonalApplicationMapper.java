package com.diting.dao;

import com.diting.model.PersonalApplication;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PersonalApplicationMapper.
 */
public interface PersonalApplicationMapper {

    void create(PersonalApplication personalApplication);

    void delete(@Param("userId") Integer userId, @Param("appId") Integer appId);

    List<Integer> searchForChecked(@Param("userId") Integer userId);

}
