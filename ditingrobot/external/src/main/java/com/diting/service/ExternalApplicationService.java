package com.diting.service;

import com.diting.model.options.ExternalOptions;

import java.util.List;

/**
 * ExternalApplicationService.
 */
public interface ExternalApplicationService {

    String invoke(ExternalOptions options);

    void setting(ExternalOptions options);

    List<Integer> searchForChecked();

}
