package com.diting.service.impl;

import com.diting.model.Qiniu;
import com.diting.service.QiniuService;
import com.diting.util.QiniuUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.diting.util.Utils.isEmpty;

/**
 * Created by Administrator on 2017/2/22.
 */
@Service
public class QiniuServiceImpl implements QiniuService {
    @Override
    public Qiniu upload_image(Qiniu qiniu) {
        try {
            QiniuUtil qiniuUtil=new QiniuUtil();
            if (!isEmpty(qiniu.getUpload_url())&&!isEmpty(qiniu.getRnd_name())){
                qiniuUtil.upload(qiniu.getUpload_url(),qiniu.getRnd_name(),qiniu.getBucketName());
                qiniu.setImg_url(qiniu.getBucket_domail()+"/"+qiniu.getRnd_name());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return qiniu;
    }
}
