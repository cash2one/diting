package com.diting;

import com.diting.model.options.ExternalOptions;
import com.diting.service.ExternalApplicationService;
import com.diting.util.APIUtils;
import com.diting.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.annotations.Test;

/**
 * TestAPIUtils.
 */
public class TestAPIUtils extends BaseTest {
    @Autowired
    ExternalApplicationService externalApplicationService;

    @Test(enabled = false)
    public void testBVT() {
        ExternalOptions options = new ExternalOptions();
        options.setUrl("http://101.201.74.133:8181/intel/ditingServlet");
        options.setQuestion("今天星期几");
        options.setKw1("一");
        options.setKw2("一");
        options.setKw3("一");
        options.setKw4("一");
        options.setKw5("一");

        String answer = APIUtils.post(options);
        Utils.str2json(answer);

        System.out.println(Utils.str2json(answer));

    }

    @Test(enabled = false)
    public void testService() {
        ExternalOptions options = new ExternalOptions();
//        options.setUrl("http://101.201.74.133:8181/intel/ditingServlet");
        options.setScene("麻辣烫");
        options.setQuestion("今天星期几");
        options.setKw1("一");
        options.setKw2("一");
        options.setKw3("一");
        options.setKw4("一");
        options.setKw5("一");

        String answer = externalApplicationService.invoke(options);
        System.out.println(answer);

    }



}
