package com.diting.service.impl;

import com.diting.cache.Cache;
import com.diting.core.Universe;
import com.diting.dao.CustomerSynonymMapper;
import com.diting.error.AppErrors;
import com.diting.model.CustomerSynonym;
import com.diting.model.options.CustomerSynonymOptions;
import com.diting.model.result.Results;
import com.diting.service.CustomerSynonymService;
import com.diting.service.InferenceEngineService;
import com.diting.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liufei on 2016/9/8.
 */

@SuppressWarnings("ALL")
@Service("customerSynonymService")
@Transactional
public class CustomerSynonymServiceImpl implements CustomerSynonymService {

    @Autowired
    CustomerSynonymMapper customerSynonymMapper;

    @Autowired
    InferenceEngineService inferenceEngineService;

    @Autowired
    @Qualifier("redisCache")
    private Cache redisCache;

    @Override
    public CustomerSynonym create(CustomerSynonym customerSynonym) {
        if (this.checkCustomerSynonym(customerSynonym))
            throw AppErrors.INSTANCE.common("标准词或替换词不能为空").exception();
        String key = "CustomerSynonym" + Universe.current().getUserName();
        customerSynonym.setAccount_id(Universe.current().getUserId());
        customerSynonymMapper.create(customerSynonym);
        List<CustomerSynonym> customerSynonymList = new ArrayList<>();
        customerSynonymList = customerSynonymMapper.getCustomerSynonymByUserId(Universe.current().getUserId());
        redisCache.put(key, customerSynonymList);
        return customerSynonym;
    }

    @Override
    public CustomerSynonym update(CustomerSynonym customerSynonym) {
        if (this.checkCustomerSynonym(customerSynonym))
            throw AppErrors.INSTANCE.common("标准词或替换词不能为空").exception();
        String key = "CustomerSynonym" + Universe.current().getUserName();
        customerSynonym.setAccount_id(Universe.current().getUserId());
        customerSynonymMapper.update(customerSynonym);
        List<CustomerSynonym> customerSynonymList = new ArrayList<>();
        customerSynonymList = customerSynonymMapper.getCustomerSynonymByUserId(Universe.current().getUserId());
        redisCache.put(key, customerSynonymList);

//        Iterator<CustomerSynonym> iterator=InferenceEngineServiceImpl.customerSynonymList.iterator();
//        int i=0;
//        while (iterator.hasNext()){
//            CustomerSynonym customerSynonym1=iterator.next();
//            if (customerSynonym1.getId().equals(customerSynonym.getId())){
//                iterator.remove();
//                InferenceEngineServiceImpl.customerSynonymList.add(customerSynonym);
//                break;
//            }
//        }
        return customerSynonym;
    }

    @Override
    public CustomerSynonym get(Integer customerSynonymId) {
        return customerSynonymMapper.get(customerSynonymId);
    }

    @Override
    public void delete(Integer customerSynonymId) {
        String key = "CustomerSynonym" + Universe.current().getUserName();

//        Iterator<CustomerSynonym> iterator=InferenceEngineServiceImpl.customerSynonymList.iterator();
//        while (iterator.hasNext()){
//            CustomerSynonym customerSynonym1=iterator.next();
//            if (customerSynonym1.getId().equals(customerSynonymId)){
//                iterator.remove();
//            }
//        }
        customerSynonymMapper.delete(customerSynonymId);

        List<CustomerSynonym> customerSynonymList = new ArrayList<>();
        customerSynonymList = customerSynonymMapper.getCustomerSynonymByUserId(Universe.current().getUserId());
        redisCache.put(key, customerSynonymList);
    }

    @Override
    public Results<CustomerSynonym> searchForPage(CustomerSynonymOptions options) {
        Results results = new Results();
        List<CustomerSynonym> accounts = customerSynonymMapper.searchForPage(options);
        results.setItems(accounts);
        results.setTotal(options.getTotalRecord());
        return results;
    }

    private boolean checkCustomerSynonym(CustomerSynonym customerSynonym) {
        if (Utils.isEmpty(customerSynonym.getWord_old()) || Utils.isEmpty(customerSynonym.getWord_new())) {
            return true;
//        } else if (customerSynonym.getWord_old().length() < customerSynonym.getWord_new().length()) {
//            return true;
        } else {
            return false;
        }
    }
}
