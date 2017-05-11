package com.diting.service;

import com.diting.model.dict.Dictionary;
import com.diting.model.options.DictionaryOptions;

import java.util.List;

/**
 * EmployeeService.
 */
public interface DictionaryService {

    Dictionary create(Dictionary employee);

    Dictionary update(Dictionary employee);

    Dictionary get(Integer dictId);

    List<Dictionary> search(DictionaryOptions options);
}
