package com.diting.util;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

import java.io.IOException;

import static com.diting.util.Utils.isEmpty;

/**
 * Created by Administrator on 2017/1/21.
 */
public class QiniuUtil {
    //设置好账号的ACCESS_KEY和SECRET_KEY
     String ACCESS_KEY = "uitmSQ6vcOJzagNSf_O1r3Hgc14EIWSLwoaGA8GW";
     String SECRET_KEY = "f9gzwmMZo73VtvsvhTVAShw87zFjezU2TPWK9XAw";
    //要上传的空间
//     String bucketname = "diting-picture";
     public String BUCKET_DOMAIN = "http://diting-picture.pingxingren.cn";
    //上传到七牛后保存的文件名
//    String key = "my-java12131243.png";
    //上传文件的路径
//    String FilePath = "C:\\Users\\Administrator\\Desktop\\pengzong\\25705952872900103.jpg";

    //密钥配置
     Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    ///////////////////////指定上传的Zone的信息//////////////////
    //第一种方式: 指定具体的要上传的zone
    //注：该具体指定的方式和以下自动识别的方式选择其一即可
    //要上传的空间(bucket)的存储区域为华东时
    // Zone z = Zone.zone0();
    //要上传的空间(bucket)的存储区域为华北时
    // Zone z = Zone.zone1();
    //要上传的空间(bucket)的存储区域为华南时
    // Zone z = Zone.zone2();

    //第二种方式: 自动识别要上传的空间(bucket)的存储区域是华东、华北、华南。
     Zone z = Zone.autoZone();
     Configuration c = new Configuration(z);

    //创建上传对象
     UploadManager uploadManager = new UploadManager(c);

    public static void main(String args[]) throws IOException {
        new QiniuUtil().upload("","","");
    }

    //简单上传，使用默认策略，只需要设置上传的空间名就可以了
    public  String getUpToken(String bucketname) {
        return auth.uploadToken(bucketname);
    }

    public  void upload(String FilePath,String key,String bucketname) throws IOException {
        try {
            if (isEmpty(bucketname)){
                bucketname=getUpToken("diting-picture");
            }else {
                bucketname=getUpToken(bucketname);
            }
            //调用put方法上传
            Response res = uploadManager.put(FilePath, key,bucketname);
            //打印返回的信息
            System.out.println(res.bodyString());
        } catch (QiniuException e) {
            Response r = e.response;
            // 请求失败时打印的异常的信息
            System.out.println(r.toString());
            try {
                //响应的文本信息
                System.out.println(r.bodyString());
            } catch (QiniuException e1) {
                //ignore
            }
        }
    }
}
