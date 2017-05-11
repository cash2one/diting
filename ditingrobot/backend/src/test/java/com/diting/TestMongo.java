package com.diting;

import com.diting.model.mongo.Records;
import com.diting.service.ChatLogCountMongoService;
import com.diting.service.RecordsMongoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * TestCache.
 */
public class TestMongo extends BaseTest {
    @Autowired
    private RecordsMongoService recordsMongoService;

    @Autowired
    private ChatLogCountMongoService chatLogCountMongoService;

    @Test(enabled = false)
    public void testBVT() {
        //create
        Records records = new Records();
        records.setQuestion("what is your name?");
        records.setAnswer("Ooo");
        recordsMongoService.create(records);

        //get
//        Records records = recordsMongoService.get(4);
//        System.out.println(Utils.toJSON(records));

        //search all
//        List<Records> recordses = recordsMongoService.getAll();
//        System.out.println(recordses.size());
    }

    @Test(enabled = false)
    public void testChatLogCount(){
        Integer n=chatLogCountMongoService.getChatLogCountByUsername("13552141271");
        Integer y_n=chatLogCountMongoService.getChatLogYesterdayCountByUsername("13552141271");
        Integer invalid_n=chatLogCountMongoService.getChatLogInvalidCountByUsername("13552141271");
        System.out.println("问答统计================:"+n);
        System.out.println("昨日问答统计================:"+y_n);
        System.out.println("有效问答统计================:"+invalid_n);
    }
}
