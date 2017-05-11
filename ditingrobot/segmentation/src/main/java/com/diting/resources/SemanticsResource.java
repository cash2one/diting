package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Semantics;
import com.diting.model.options.SemanticsOptions;
import com.diting.service.yuyi.YuyiTest;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.util.Date;

import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/3.
 */
@Path("/yuyi")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class SemanticsResource {

    @Autowired
    private YuyiTest yuyiTest;

    @GET
    @Timed
    @Path("/search")
    public Semantics yuyiApiTest(@Context UriInfo uriInfo) throws Exception {
        SemanticsOptions semanticsOptions=buildSemanticsOptions(uriInfo);
        Date date1 = new Date();
        JSONObject jsomshuru = yuyiTest.shuruchuliJason(semanticsOptions.getShuru(),semanticsOptions.getBeforegjz(),semanticsOptions.getScene());
        Date date2 = new Date();
        String gjz = jsomshuru.get("qieci").toString();
        JSONObject gjzjson = yuyiTest.guanjianzijsJason(gjz);
        Date date3 = new Date();
        String xiangshi1 = String.valueOf(semanticsOptions.getShuru());
        String xiangshi2 = String.valueOf(semanticsOptions.getShuru() + "fen!ge" + semanticsOptions.getShuru() + "fen!ge" + semanticsOptions.getShuru());
        JSONObject yuyijinshijson = yuyiTest.yuyixiangsijsJason(xiangshi1, xiangshi2);
        Date date4 = new Date();
        Long shurutime = date2.getTime() - date1.getTime();
        Long gjztime = date3.getTime() - date2.getTime();
        Long yuyijinshitime = date4.getTime() - date3.getTime();
        Semantics semantics = new Semantics();
        semantics.setShuruAnswer(String.valueOf(jsomshuru));
        semantics.setGjzAnswer(String.valueOf(gjzjson));
        semantics.setXiangshiAnswer(String.valueOf(yuyijinshijson));
        semantics.setShuruAnswertime(shurutime);
        semantics.setGjzAnswertime(gjztime);
        semantics.setXiangshiAnswertime(yuyijinshitime);
        semantics.setGiz(gjz);
        semantics.setXiangshi1(xiangshi1);
        semantics.setXiangshi2(xiangshi2);
        return semantics;
    }
    private SemanticsOptions buildSemanticsOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        SemanticsOptions options = new SemanticsOptions();
        options.setShuru(nullIfEmpty(params.getFirst("shuru")));
        options.setGjz(nullIfEmpty(params.getFirst("gjz")));
        options.setXiangshi1(nullIfEmpty(params.getFirst("xiangshi1")));
        options.setXiangshi2(nullIfEmpty(params.getFirst("xiangshi2")));
        options.setBeforegjz(nullIfEmpty(params.getFirst("beforegjz")));
        options.setScene(nullIfEmpty(params.getFirst("scene")));
        return options;
    }
}
