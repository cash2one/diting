package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.core.Universe;
import com.diting.model.Account;
import com.diting.model.BaseInfo;
import com.diting.model.Knowledge;
import com.diting.model.options.KnowledgeOptions;
import com.diting.model.result.Results;
import com.diting.model.views.AccountViews;
import com.diting.model.views.KnowledgeViews;
import com.diting.service.AccountService;
import com.diting.service.KnowledgeService;
import com.diting.util.DateUtil;
import com.diting.util.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;


import java.util.List;
import java.util.Map;

import static com.diting.util.Utils.buildPageableOptions;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * KnowledgeResource
 */
@Path("/knowledge")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class KnowledgeResource {

    @Autowired
    private KnowledgeService knowledgeService;

    @Autowired
    private AccountService accountService;

    @POST
    @Timed
    @Path("/")
    public Knowledge create(Knowledge knowledge) {
        return knowledgeService.create(knowledge);
    }

    @POST
    @Timed
    @Path("/scene")
    public Knowledge getScene(Knowledge knowledge) {
        return knowledgeService.getScene(knowledge);
    }

    @POST
    @Timed
    @Path("/temp/create")
    public Map<String, String> complexTempCreate(Knowledge knowledge) {
        return knowledgeService.complexTempCreate(knowledge);
    }

    @POST
    @Timed
    @Path("/create")
    public Map<String, String> complexCreate(Knowledge knowledge) {
        return knowledgeService.complexCreate(knowledge);
    }

    @POST
    @Timed
    @Path("/admin/create")
    public Map<String, String> baseKnowledgeCreate(Knowledge knowledge) {
        return knowledgeService.baseKnowledgeCreate(knowledge);
    }

    @GET
    @Timed
    @Path("/{knowledgeId}")
    public Knowledge get(@PathParam("knowledgeId") Integer knowledgeId) {
        return knowledgeService.get(knowledgeId);
    }

    @PUT
    @Timed
    @Path("/admin/update")
    public Knowledge baseKnowledgeUpdate(Knowledge knowledge) {
        return knowledgeService.baseKnowledgeUpdate(knowledge);
    }

    @PUT
    @Timed
    @Path("/update")
    public Knowledge update(Knowledge knowledge) {
        return knowledgeService.update(knowledge);
    }

    @PUT
    @Timed
    @Path("/delete/{knowledgeId}")
    public Boolean delete(@PathParam("knowledgeId") Integer knowledgeId) {
        return knowledgeService.delete(knowledgeId);
    }

    @PUT
    @Timed
    @Path("/batchdelete")
    public Response batchDelete(Knowledge knowledge) {
        String[] strings = knowledge.getIds().split(",");
        knowledgeService.batchDelete(strings);
        return Response.status(200).build();
    }

    @PUT
    @Timed
    @Path("/admin/batchdelete")
    public Response adminBatchDelete(Knowledge knowledge) {
        String[] strings = knowledge.getIds().split(",");
        knowledgeService.adminBatchDelete(strings);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/search-page")
    public Results<Knowledge> searchForPage(@Context UriInfo uriInfo) {
        Account account=accountService.get(Universe.current().getUserId());
        KnowledgeOptions options = buildKnowledgeOptions(uriInfo);
        options.setAccountId(account.getId());
        options.setCompanyId(account.getCompanyId());
        return knowledgeService.searchForPage(options);
    }

    @GET
    @Timed
    @Path("/admin/search-page")
    public Results<Knowledge> searchBaseKnowledgeForPage(@Context UriInfo uriInfo) {
        KnowledgeOptions options = buildKnowledgeOptions(uriInfo);
        options.setAccountId(-1);
        return knowledgeService.searchForPage(options);
    }

    @GET
    @Timed
    @Path("/admin/search-company-page")
    public Results<Knowledge> searchCompanyKnowledgeForPage(@Context UriInfo uriInfo) {
        KnowledgeOptions options = buildKnowledgeOptions(uriInfo);
        return knowledgeService.searchCompanyKnowledgeForPage(options);
    }

    @SuppressWarnings("unchecked")
    @GET
    @Timed
    @Path("/upload")
    public Map<String, String> uploadKnowledge(@Context UriInfo uriInfo) {
        List<Map> valueList = ExcelUtil.readExcel(buildKnowledgeOptions(uriInfo).getUploadExcelPath());
        return (Map<String, String>) knowledgeService.batchInsert(valueList);
    }

    @SuppressWarnings("unchecked")
    @GET
    @Timed
    @Path("/admin/upload")
    public Map<String, String> uploadBaseKnowledge(@Context UriInfo uriInfo) {
        List<Map> valueList = ExcelUtil.readExcel(buildKnowledgeOptions(uriInfo).getUploadExcelPath());
        return (Map<String, String>) knowledgeService.baseBatchInsert(valueList);
    }

    @GET
    @Timed
    @Path("/exportknowledges")
    public List<Knowledge> companyKnowledgeExcelList(@Context UriInfo uriInfo) {
        return knowledgeService.findCompanyKnowledgeList(buildKnowledgeOptions(uriInfo));
    }

    @GET
    @Timed
    @Path("/admin/exportknowledges")
    public List<Knowledge> baseKnowledgeExcelList(@Context UriInfo uriInfo) {
        return knowledgeService.findBaseKnowledgeList(buildKnowledgeOptions(uriInfo));
    }

    @POST
    @Timed
    @Path("/update_keys")
    public Response update_all_keys() {
        knowledgeService.updateAllKeys();
        return Response.status(200).build();
    }

    @POST
    @Timed
    @Path("/company/baseknowledgeinfo")
    public Response baseKnowledgeInfo(BaseInfo baseInfo) {
        knowledgeService.createBaseKnowledgeInfo(baseInfo);
        return Response.status(200).build();
    }

    @GET
    @Timed
    @Path("/company/baseinfo")
    public BaseInfo baseInfo() {
        return knowledgeService.getBaseInfoByCompanyId();
    }

    @GET
    @Timed
    @Path("/search-knowledge-num/{type}")
    public List<KnowledgeViews> searchLoginUserStatistics(@PathParam("type") String type){
        return knowledgeService.searchKnowledgeNum(type);
    }

    private KnowledgeOptions buildKnowledgeOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        KnowledgeOptions options = new KnowledgeOptions();
        buildPageableOptions(uriInfo, options);
        if (nullIfEmpty(params.getFirst("companyId")) != null && !nullIfEmpty(params.getFirst("companyId")).contains("-1")) {
            options.setCompanyId(Integer.parseInt(params.getFirst("companyId")));
        }
        if (nullIfEmpty(params.getFirst("keywords")) != null) {
            options.setKeywords(params.getFirst("keywords"));
        }
        if (nullIfEmpty(params.getFirst("queryState")) != null) {
            options.setQueryState(Integer.parseInt(params.getFirst("queryState")));
        }
        if (nullIfEmpty(params.getFirst("queryCriteria")) != null) {
            options.setQueryCriteria(Integer.parseInt(params.getFirst("queryCriteria")));
        }
        if (nullIfEmpty(params.getFirst("upload_path")) != null) {
            options.setUploadExcelPath(params.getFirst("upload_path"));
        }
        if (nullIfEmpty(params.getFirst("starttime")) != null) {
            options.setStartTime(DateUtil.getStartTime(params.getFirst("starttime")));
        }
        if (nullIfEmpty(params.getFirst("endtime")) != null) {
            options.setEndTime(DateUtil.getEndTime(params.getFirst("endtime")));
        }
        if (nullIfEmpty(params.getFirst("type")) != null) {
            options.setType(Integer.parseInt(params.getFirst("type")));
        }
        return options;
    }
}
