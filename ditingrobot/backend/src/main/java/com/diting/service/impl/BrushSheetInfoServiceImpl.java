package com.diting.service.impl;

import com.diting.dao.BrushSheetInfoMapper;
import com.diting.model.BrushSheetInfo;
import com.diting.service.BrushSheetInfoService;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2017/3/13.
 */
@Service("brushSheetInfoService")
public class BrushSheetInfoServiceImpl implements BrushSheetInfoService{
    private BrushSheetInfoMapper brushSheetInfoMapper;

    public BrushSheetInfo create(BrushSheetInfo brushSheetInfo){
        brushSheetInfoMapper.create(brushSheetInfo);
        return brushSheetInfo;
    }
}
