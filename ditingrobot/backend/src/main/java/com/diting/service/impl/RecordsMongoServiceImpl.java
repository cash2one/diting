package com.diting.service.impl;

import com.diting.model.mongo.Records;
import com.diting.service.RecordsMongoService;
import com.diting.service.mongo.BaseMongoServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("recordsMongoService")
@Transactional
public class RecordsMongoServiceImpl extends BaseMongoServiceImpl<Records> implements RecordsMongoService {

}
