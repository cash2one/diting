package com.diting;

import com.diting.elasticsearch.EditIndex;
import com.diting.model.Knowledge;
import com.diting.service.KnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

import java.util.List;

/**
 * Created by Administrator on 2016/8/21.
 */
public class TestDelIndex extends BaseTest{

    @Autowired
    private KnowledgeService knowledgeService;

    @Test(enabled = false)
    public void delIndex(){
        EditIndex editIndex=new EditIndex();
        List<Knowledge> knowledgeList=knowledgeService.findKnowledgesByAccountId(273);
        for (Knowledge aKnowledgeList : knowledgeList) {
            int id = aKnowledgeList.getId();
            editIndex.delete(String.valueOf(273), String.valueOf(id));
        }
        System.out.println(knowledgeList.size());
    }

}
