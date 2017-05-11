package com.diting.service;

import com.diting.model.OralTreatment;
import com.diting.model.options.OralTreatmentOptions;
import com.diting.model.result.Results;

import java.util.List;
import java.util.Map;

/**
 * Created by liufei on 2017/1/3.
 */
public interface OralTreatmentService {
    OralTreatment create(OralTreatment oralTreatment);

    OralTreatment update(OralTreatment oralTreatment);

    Boolean delete(Integer id);

    Results<OralTreatment> searchForPage(OralTreatmentOptions oralTreatmentOptions);

    OralTreatment get(Integer id);

    Map<String, List<OralTreatment>> getOralTreatmentMap();
}
