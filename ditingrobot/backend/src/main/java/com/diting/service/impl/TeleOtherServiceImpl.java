package com.diting.service.impl;

import com.diting.dao.TeleOtherMapper;
import com.diting.model.TeleOther;
import com.diting.model.options.TeleOtherOptions;
import com.diting.model.result.Results;
import com.diting.service.TeleOtherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by shenkun on 2017/02/20.
 */
@SuppressWarnings("ALL")
@Service("teleOtherService")
public class TeleOtherServiceImpl implements TeleOtherService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TeleOtherServiceImpl.class);

    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Autowired
    private TeleOtherMapper teleOtherMapper;

    @Override
    public TeleOther create(TeleOther teleOther) {
        teleOtherMapper.create(teleOther);
        return teleOther;
    }

    @Override
    public TeleOther update(TeleOther teleOther){
        teleOtherMapper.update(teleOther);
        teleOther=teleOtherMapper.get(teleOther.getId());
        return teleOther;
    }
    @Override
    public Boolean delete(Integer id) {
        return teleOtherMapper.delete(id);
    }

    @Override
    public TeleOther get(Integer id) {
        return teleOtherMapper.get(id);
    }

    @Override
    public Results<TeleOther> search(TeleOtherOptions options) {
        Results results = new Results();
        List<TeleOther> accounts = teleOtherMapper.searchForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }
}
