package com.diting.service.impl;

import com.diting.dao.OralTreatmentMapper;
import com.diting.model.OralTreatment;
import com.diting.model.ReplaceWord;
import com.diting.model.WordBase;
import com.diting.model.options.OralTreatmentOptions;
import com.diting.model.result.Results;
import com.diting.service.OralTreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/1/3.
 */
@SuppressWarnings("ALL")
@Service
public class OralTreatmentServiceImpl implements OralTreatmentService {

    @Autowired
    private OralTreatmentMapper oralTreatmentMapper;

    @Override
    public OralTreatment create(OralTreatment oralTreatment) {
        oralTreatmentMapper.create(oralTreatment);
        return oralTreatment;
    }

    @Override
    public OralTreatment update(OralTreatment oralTreatment) {
        oralTreatmentMapper.update(oralTreatment);
        return oralTreatment;
    }

    @Override
    public Boolean delete(Integer id) {
        return oralTreatmentMapper.delete(id);
    }

    @Override
    public Results<OralTreatment> searchForPage(OralTreatmentOptions oralTreatmentOptions) {
        Results results = new Results();
        List<OralTreatment> thesaurusList = oralTreatmentMapper.searchForPage(oralTreatmentOptions);
        results.setItems(thesaurusList);
        results.setTotal(oralTreatmentOptions.getTotalRecord());
        return results;
    }
    @Override
    public OralTreatment get(Integer id){
        return oralTreatmentMapper.get(id);
    }
    @Override
    public Map<String, List<OralTreatment>> getOralTreatmentMap(){
        Map<String, List<OralTreatment>> oralTreatmentMap =new HashMap<>();
        List<OralTreatment> oralTreatments=oralTreatmentMapper.searchAll();
        for (OralTreatment oralTreatment:oralTreatments) {
            List<OralTreatment> list=new ArrayList<>();
            if (oralTreatmentMap.get(oralTreatment.getWord()) != null) {
                list = oralTreatmentMap.get(oralTreatment.getWord());
                list.add(oralTreatment);
            } else {
                list.add(oralTreatment);
            }
            oralTreatmentMap.put(oralTreatment.getWord(), list);
        }
        return oralTreatmentMap;
    }
}
