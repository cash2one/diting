package com.diting.service;

import com.diting.model.Parameter;

/**
 * ParameterService.
 */
public interface ParameterService {

    Parameter create(Parameter parameter);

    Parameter get(Integer parameterId);
}
