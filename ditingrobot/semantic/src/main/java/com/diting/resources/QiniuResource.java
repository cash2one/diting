package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Qiniu;
import com.diting.model.options.QiniuOptions;
import com.diting.util.QiniuUtil;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

import static com.diting.util.Utils.isEmpty;
import static com.diting.util.Utils.nullIfEmpty;

/**
 * Created by Administrator on 2017/1/21.
 */
@Path("/qiniu")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class QiniuResource {

    @POST
    @Timed
    @Path("/upload-image")
    public Qiniu upload_image(Qiniu qiniu) {
        try {
            QiniuUtil qiniuUtil=new QiniuUtil();
            if (!isEmpty(qiniu.getUpload_url())&&!isEmpty(qiniu.getRnd_name())){
                qiniuUtil.upload(qiniu.getUpload_url(),qiniu.getRnd_name(),null);
                qiniu.setImg_url(qiniuUtil.BUCKET_DOMAIN+"/"+qiniu.getRnd_name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qiniu;
    }

}
