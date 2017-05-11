package com.diting.resources;

import com.codahale.metrics.annotation.Timed;
import com.diting.model.Fans;
import com.diting.model.options.FansOptions;
import com.diting.model.result.Flag;
import com.diting.model.result.Results;
import com.diting.service.FansService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import java.util.List;

import static com.diting.util.Utils.buildPageableOptions;

/**
 * AccountResource
 */
@Path("/fans")
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FansResource {

    @Autowired
    private FansService fanService;



    /**
     * 新增粉丝
     *
     * @return flg          返回为1的话 表示自己关注数已经达到20 不允许在关注
     *          flg_count    为当前聊天页面用户的粉丝总数
     *
     * @param  fans        需要接受参数 own_phone，传入的参数为当前聊天页面用户的id
     *
     * */
    @POST
    @Timed
    @Path("/save_fans")
    public Flag save_fans(Fans fans) {
        return fanService.save_fans(fans);
    }

    /**
     * 删除粉丝
     *
     * @return flg          默认返回0，没有实际意义的参数
     *          flg_count    为当前聊天页面用户的粉丝总数
     *
     * @param  fans        需要接受参数 own_phone，传入的参数为当前聊天页面用户的id
     *
     * */
    @POST
    @Timed
    @Path("/del_fans")
    public Flag del_fans(Fans fans) {
       return  fanService.del_fans(fans);
    }



    /**
     * 查看自己是不是他的粉丝/自己有没有关注聊天页面用户
     *
     * @return flg          1的话已经关注 0的话就没有关注
     *          flg_count    为当前聊天页面用户的粉丝总数
     *
     * @param  fans        需要接受参数 own_phone，传入的参数为当前聊天页面用户的id
     *
     * */
    @POST
    @Timed
    @Path("/find_fans")
    public Flag find_fans(Fans fans) {
      return  fanService.find_fans(fans);
    }


    /**
     * 按照粉丝多少进行排序的排行榜。只显示前20条数据
     *
     * @return robot_name    机器人名称
     *          cc            粉丝数
     *          company_name  公司名称
     *
     * */
    @GET
    @Timed
    @Path("/search_fans")
    public List<Fans> search_fans() {
       return fanService.search_fans();
    }


    /**
     * 查询自己的粉丝的详细信息
     *
     * @return robot_name    自己关注用户的机器人名称
     *          ownerType     自己关注用户的粉丝数
     *          company_name  自己关注用户的公司名称
     *          updatedBy     自己关注用户的机器人简介
     *
     * */
    @GET
    @Timed
    @Path("/search_count_fans")
    public List<Fans> search_count_fans() {
        return fanService.search_count_fans();
    }

    @GET
    @Timed
    @Path("/search_concern_list")
    public List<Fans> search_concern_list() {
        return fanService.search_concern_list();
    }

    @POST
    @Timed
    @Path("/search_my_fans")
    public List<Fans> search_my_fans(Fans fans) {
        return fanService.search_my_fans(fans);
    }

    @GET
    @Timed
    @Path("/login-user/search-page")
    public Results<Fans> findFansByLogin(@Context UriInfo uriInfo){
        return fanService.findFansByLogin(buildAccountOptions(uriInfo));
    }
    private FansOptions buildAccountOptions(@Context UriInfo uriInfo) {
        MultivaluedMap<String, String> params = uriInfo.getQueryParameters();

        FansOptions options = new FansOptions();
        buildPageableOptions(uriInfo, options);
        return options;
    }
}
