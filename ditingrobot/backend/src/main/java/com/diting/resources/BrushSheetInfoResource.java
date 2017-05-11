package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Account;
import com.diting.model.BrushSheetInfo;
import com.diting.service.BrushSheetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Administrator on 2017/3/10.
 */
@Path("/brush")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class BrushSheetInfoResource {

    @Autowired
    private BrushSheetInfoService brushSheetInfoService;

    @POST
    @Timed
    @Path("/sheet/info")
    public BrushSheetInfo create(BrushSheetInfo  brushSheetInfo) {
        brushSheetInfoService.create(brushSheetInfo);
        return brushSheetInfo;
    }
}
