package com.diting.dao;

import com.diting.model.Parameter;
import org.apache.ibatis.annotations.Param;

/**
 * ParameterMapper
 */
public interface ParameterMapper {
    void create(Parameter parameter);

    Parameter get(@Param("parameterId") Integer parameterId);

}
