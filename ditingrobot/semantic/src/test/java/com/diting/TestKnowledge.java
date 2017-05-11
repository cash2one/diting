package com.diting;

import com.diting.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * Created by liufei on 2016/9/9.
 */
public class TestKnowledge extends BaseTest{

    @Autowired
    KnowledgeService knowledgeService;

    @Test(enabled = false)
    public void testUpdateAllKeys() {
        try {
            knowledgeService.updateAllKeys();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
