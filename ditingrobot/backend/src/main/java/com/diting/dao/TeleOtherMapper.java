package com.diting.dao;

import com.diting.model.TeleOther;
import com.diting.model.options.TeleOtherOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * TeleOtherMapper.
 */
public interface TeleOtherMapper {

     void create(TeleOther teleOther);

     void update(TeleOther teleOther);

     boolean delete(@Param("id") Integer id);

     TeleOther get(@Param("id") Integer id);

     List<TeleOther> searchForPage(TeleOtherOptions options);

     List<TeleOther> getCount(TeleOther teleOther);
}
