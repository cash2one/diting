package com.diting.service.impl;

import com.diting.dao.ParameterMapper;
import com.diting.model.Parameter;
import com.diting.service.ParameterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("parameterService")
@Transactional
public class ParameterServiceImpl implements ParameterService {
    @Autowired
    private ParameterMapper parameterMapper;

    @Override
    public Parameter create(Parameter parameter){
        parameterMapper.create(parameter);
        return parameter;
    }

    @Override
    public Parameter get(Integer parameterId) {
        return parameterMapper.get(parameterId);
    }

}
