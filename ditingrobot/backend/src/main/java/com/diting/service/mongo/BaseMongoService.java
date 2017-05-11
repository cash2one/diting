package com.diting.service.mongo;

import com.diting.model.BaseModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * BaseMongoService
 */
public interface BaseMongoService<MODEL extends BaseModel>  {
    Integer getNextSequenceId();

    MODEL create(MODEL model);

    MODEL get(Integer id);

    void delete(@Param("id") Integer id);

    boolean exists(Integer id);

    List<MODEL> getAll();

}
