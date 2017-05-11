package com.diting.dao;

import com.diting.model.OralTreatment;
import com.diting.model.options.OralTreatmentOptions;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
public interface OralTreatmentMapper {
    void create(OralTreatment oralTreatment);

    List<OralTreatment> searchForPage(OralTreatmentOptions oralTreatmentOptions);

    void update(OralTreatment oralTreatment);

    boolean delete(@Param("id") Integer id);

    OralTreatment get(@Param("id") Integer id);

    List<OralTreatment> searchAll();
}
