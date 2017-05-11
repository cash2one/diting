package com.diting.service;

import com.diting.model.TeleOther;
import com.diting.model.options.TeleOtherOptions;
import com.diting.model.result.Results;

/**
 * Created by shenkun on 2017/02/20.
 */
public interface TeleOtherService {

    TeleOther create(TeleOther teleOther);

    TeleOther update(TeleOther teleOther);

    Boolean delete(Integer id);

    TeleOther get(Integer id);

    Results<TeleOther> search(TeleOtherOptions options);

}
