function admin(){
    // 编辑弹窗
    $(".allmore").click(function () {
        var tname = $(this).attr('data-name');
        var tperson = $(this).attr('data-person');
        var turl = $(this).attr('data-url');
        var tta = $(this).attr('data-ta');
        var ttell = $(this).attr('data-tell');
        var tsay = $(this).attr('data-say');
        var tid = $(this).attr('data-idd');
        var tscene = $(this).attr('data-scene');
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width: 600px;height: 550px;background: #fff;position: fixed;top: 12%;left: 30%;"> <div style="height: 42px;background: #4898D5;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">谛听机器人外部应用详情窗口<div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> <div style="height: 475px;width: 95%; margin: 10px auto;color: #000;overflow: auto;"> <p><span>&nbsp;&nbsp;&nbsp;应用名：</span>&nbsp;'+ tname +'</p> <p><span>&nbsp;&nbsp;&nbsp;开发者：</span>&nbsp;'+ tperson +' </p> <p><span>应用地址：</span>&nbsp;'+ turl +' </p> <p><span>&nbsp;&nbsp;&nbsp;联系人：</span>&nbsp;'+ tta +' </p> <p><span>联系电话：</span>&nbsp;'+ ttell +' </p> <p><span>应用场景：</span>&nbsp;'+ tscene +' </p> <p><span>应用说明：</span>&nbsp;'+ tsay +' </p><p><span>审核操作：</span> <input class="btnzt btn0_1 btnsh" data-k="1" data-id="'+ tid +'" type="button" title="审批通过" value="审批通过">  <input class="btnzt btn0_2 btnsh" type="button" data-k="0" data-id="'+ tid +'" title="审批未过" value="审批未过" onclick=""> </p></div></div></div> </div>'

        $('#tanc').html(str);
        $(".close_update_c").click(function () {//叉掉
            $('#tanc').html("")

        });
        //审核
        $(".btnsh").click(function () {
            var status = $(this).attr('data-k');
            var apiStoreId= $(this).attr('data-id');
            var quessh = {
                apiStoreId:apiStoreId,
                status:status
            }
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/approval/update',
                data: JSON.stringify(quessh)
            }).done(function (data) {
                var message=data.message;
                layer.msg(message, {icon: 6})
            }).fail(function (data) {
                var message=JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})

            });
        })

        //开关
        $(".btnkg").click(function () {
            var status = $(this).attr('data-k');
            var  apistoreid= $(this).attr('data-id');
            var queskg = {
                apiStoreId:apistoreid,
                status:status
            };
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/switch/update',
                data: JSON.stringify(queskg)
            }).done(function (data) {
                var message=data.message;
                layer.msg(message, {icon: 6})
            }).fail(function (data) {
                var message=JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})

            });
        })

    });

    //删除这条数据
    $(".deleteStore1").click(function () {
        var apiStoreId = $(this).attr('data-value');
        layer.confirm('您真的确定要删除了吗？', {
            btn: ['确定', '再考虑一下'] //按钮
        }, function () {
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/apis/delete/' + apiStoreId

            }).done(function (data) {
                layer.msg(data.message, {time:500,icon: 6},function(){
                    //关闭后的操作
                    parent.window.location.reload(false);
                })
                // parent.history.go(0);//取消键刷新页面
            }).fail(function (data) {
                console.log(data);
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        }, function () {
            //不删了
            //time: 20000, //20s后自动关闭
            //btn: ['明白了', '知道了']
        });
    })

}
//初始化管理员第三方应用页面
function csh_store() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/apis/search-page?pageNo="+ 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        console.log(data)
        var str = '<tr> <th style="width: 60px;">ID</th> <th style="width: 120px;">名称</th> <th style="width: 200px;">提供者</th> <th >地址</th> <th style="width: 80px;">场景</th> <th style="width: 80px">详情</th> <th style="width: 80px">审批状态</th> <th style="width: 80px">删除</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            str += '<tr> <td style="text-align: center;">'+ data.items[i].id +'</td> <td>'+ data.items[i].name +'</td> <td>'+ data.items[i].providerName +'</td> <td>'+ data.items[i].url +'</td> <td style="text-align: center;">'+ data.items[i].scene +'</td> <td class="allmore" style="text-align: center;color: rebeccapurple;cursor: pointer;" data-idd="' +data.items[i].id + '" data-scene="' +data.items[i].scene + '" data-name="' +data.items[i].serverName + '" data-person="' +data.items[i].providerName + '"  data-url="' +data.items[i].url + '"  data-ta="' +data.items[i].providerName + '"  data-tell="' +data.items[i].mobile + '"  data-say="' + data.items[i].description+ '" >查看详细</td> <td style="text-align: center;">'+ data.items[i].approvalName +'</td> <td class="deleteStore1" style="text-align: center;color: rebeccapurple;cursor: pointer;" data-value="' + data.items[i].id + '">删除</td> </tr>';

            $('#admin_store_tab').html(str)
        }
        admin();

    }).fail(function (data) {
        console.log(data);

    });
}


//分页器，页码切换执行
function Paging_device(strips){ //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var x=$("#pageNo").val();
    $('#red').smartpaginator({
        totalrecords: strips,
        datacontainer: 'admin_store_tab',
        recordsperpage: 15,
        dataelement: 'tr',
        length: 4,
        next: '下一页',
        prev: '上一页',
        first: '首页',
        last: '尾页',
        theme: 'red',
        initval:x,
        controlsalways: true,
        onchange:function(){
            //页面转换后内容加载
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/apis/search-page?pageNo="+ $("#pageNo").val()
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr> <th style="width: 60px">ID</th> <th style="width: 120px">名称</th> <th style="width: 200px">提供者</th> <th >地址</th> <th style="width: 80px;">场景</th> <th style="width: 80px">详情</th> <th style="width: 80px">审批状态</th> <th style="width: 80px">删除</th> </tr>';
                for (var i = 0; i < data.items.length; i++) {
                    str += '<tr> <td style="text-align: center;">'+ data.items[i].id +'</td> <td>'+ data.items[i].name +'</td> <td>'+ data.items[i].providerName +'</td> <td>'+ data.items[i].url +'</td> <td style="text-align: center;">'+ data.items[i].scene +'</td> <td class="allmore" style="text-align: center;color: rebeccapurple;cursor: pointer;" data-idd="' +data.items[i].id + '" data-scene="' +data.items[i].scene + '" data-name="' +data.items[i].serverName + '" data-person="' +data.items[i].providerName + '"  data-url="' +data.items[i].url + '"  data-ta="' +data.items[i].providerName + '"  data-tell="' +data.items[i].mobile + '"  data-say="' + data.items[i].description+ '" >查看详细</td> <td style="text-align: center;">'+ data.items[i].approvalName +'</td> <td class="deleteStore1" style="text-align: center;color: rebeccapurple;cursor: pointer;" data-value="' + data.items[i].id + '">删除</td> </tr>';

                    $('#admin_store_tab').html(str)
                }
                admin();

            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

