
function success(){ //返回值成功获取--后执行的函数
//B库键
//跳转B库
    $("._Bku").click(function () {
        var companiesId = $(this).attr('data-id');
        window.location.href = "/knowledge/b?company="+companiesId
    });
    
// 充值弹窗
    $("._Jfei").click(function () {
        var Jid = $(this).attr('data-id');
        var Jphone = $(this).attr('data-phone');
        var Jyu = $(this).attr('data-yu');
        var JcompanyName = $(this).attr('data-companyName');
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width: 450px;height: 350px;background: #fff;position: fixed;top: 12%;left: 30%;"> <div style="height: 42px;background: #4898D5;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">谛听机器人充值窗口<div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> <div style="height: 475px;width: 95%; margin: 10px auto;color: #000;overflow: auto;"> <p><span>&nbsp;&nbsp;&nbsp;公司名：</span>&nbsp;'+ JcompanyName +'</p> <p><span>&nbsp;&nbsp;&nbsp;用户名：</span>&nbsp;'+ Jphone +' </p>  <p><span>&nbsp;&nbsp;&nbsp;剩余点数：</span>&nbsp;'+ Jyu +'点</p> <p><span>&nbsp;&nbsp;&nbsp;充值原因：</span><input type="text" id="RechargeBecause" value="" style="height: 32px;width: 260px;"/> </p><p><span>&nbsp;&nbsp;&nbsp;充值点数：</span><input id="RechargeTalks" type="text" value="" style="width: 100px;height: 40px;"/>点</p><p><input type="button" name="JfeiBtn" class="JfeiBtn" data-id="'+ Jid +'" title="提交充值" value="提交" style="width: 72px;height: 32px;border: 2px solid #4898D5;background: #4898D5;border-radius: 5px;font-size: 16px;color: #fff;cursor: pointer;line-height: 32px;" /></p> </div></div></div> </div>'

        $('#tanc').html(str);
        $(".close_update_c").click(function () {//叉掉
            $('#tanc').html("")
        });
    //提交充值
        $('.JfeiBtn').click(function () { //提交充值
            var RechargeBecause = $('#RechargeBecause').val();
            var RechargeTalks = $('#RechargeTalks').val();
            var id = $(this).attr('data-id');
            var Recharge = {
                userId: id,
                lotType: 'DIBI_D',
                amount: RechargeTalks,
                reason: RechargeBecause,
                event:'充值'
            };
            if ($('#RechargeBecause').val() != "" && $('#RechargeTalks').val() != "") {
                //保存把数据传到后台
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/api/credit/wallet',
                    data: JSON.stringify(Recharge)
                }).done(function (data) {
                    B_company();
                    layer.msg(data.message, {time:500,icon: 6},function(){
                        $('#tanc').html("")
                    });
                }).fail(function (data) {
                    console.log(data);
                    var message = JSON.parse(data.responseText).message;
                    layer.msg(message, {icon: 5})
                });
            } else {
                layer.msg("充值原因和充值次数都要填写哦！", {icon: 5})
            }

        });


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
                });
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

//初始化企业信息
function csh_business() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var timeOrDian = $('#timeOrDian').val();
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/companies/admin/search-page?pageNo="+1
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data)
        Paging_device(data.total);
        var str = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="110"  Name="user_name_" >用户名</th> <th width="40">来源</th><th width="220"  Name="name_" >公司名称</th> <th width="80"  Name="create" >创建时间</th>  <th width="80"  Name="state" >状态</th> <th width="70"  Name="moreuse" >累计使用（次）</th><th width="70"  Name="alltalk" >剩余点数（点）</th><th width="70"  Name="yester" >昨日使用（次）</th><th width="90"  Name="trues" >正确率</th> <th width="130"  Name="handle" >操作</th> </tr>'
        for (var i = 0; i < data.items.length; i++) {
           
            var times1 = timess(data.items[i].createdTime);
            // var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
            // var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            if(data.items[i].deleted == false){
                var deleted = "使用中"
            }else {
                var deleted = "已禁用"
            }
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td>'+ n +'</td> <td >'+ data.items[i].createdBy+'</td> <td >'+ data.items[i].source+'</td> <td >'+ data.items[i].name +'</td> <td  >'+ times1 +'</td>  <td  >'+ deleted +'</td> <td  >'+ data.items[i].chatLogAccount +'</td><td  >'+ data.items[i].residualFrequency +'</td> <td  >'+ data.items[i].chatLogYesterdayAccount +'</td> <td  >'+ data.items[i].accuracyRate +'</td> <td  class="cz_btn">  <input class="_Bku" title="B库" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="Bku" value="B库"  data-id="' + data.items[i].id + '" /> <input class="_Jfei" title="充值" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="Jfei" value="充值"  data-id="' + data.items[i].id + '"  data-phone="' + data.items[i].createdBy + '" data-companyName="' + data.items[i].name + '" data-yu="' + data.items[i].residualFrequency  + '"  /></td> </tr>'
        }
       
        $('#business_admin').html(str);
        success();
    }).fail(function (data) {
        console.log(data);
    });

}

//回车键搜索功能
function enterB_company(ev){
    var oEvent = ev || event;
    if (oEvent.keyCode == 13 && oEvent.ctrlKey || oEvent.keyCode == 13) {
        B_company()
    }
}

//公司搜索功能
function B_company() {
    var companies_in = $('#companies_in').val();
    var timeOrDian = $('#timeOrDian').val();
    var starttime = $("#startdate").val();
    var endtime = $("#enddate").val();
    var source = $('#source').val();
    console.log(timeOrDian)
    $("#pageNo").val(1)
    var url = "/api/companies/admin/search-page?pageNo=" + $("#pageNo").val() + "&keyword=" + companies_in + "&type="+timeOrDian+ "&starttime=" + starttime + "&endtime=" + endtime +"&source=" + source
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: url
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="110"  Name="user_name_" >用户名</th><th width="40">来源</th> <th width="220"  Name="name_" >公司名称</th> <th width="80"  Name="create" >创建时间</th>  <th width="80"  Name="state" >状态</th> <th width="70"  Name="moreuse" >累计使用（次）</th><th width="70"  Name="alltalk" >剩余点数（点）</th><th width="70"  Name="yester" >昨日使用（次）</th><th width="90"  Name="trues" >正确率</th> <th width="130"  Name="handle" >操作</th> </tr>'
        for (var i = 0; i < data.items.length; i++) {
            var times1 = timess(data.items[i].createdTime);
            // var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
            // var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            if(data.items[i].deleted == false){
                var deleted = "使用中"
            }else {
                var deleted = "已禁用"
            }
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td>'+ n +'</td> <td >'+ data.items[i].createdBy+'</td> <td >'+ data.items[i].source+'</td> <td >'+ data.items[i].name +'</td> <td  >'+ times1 +'</td> <td  >'+ deleted +'</td> <td  >'+ data.items[i].chatLogAccount +'</td><td  >'+ data.items[i].residualFrequency  +'</td> <td  >'+ data.items[i].chatLogYesterdayAccount +'</td> <td  >'+ data.items[i].accuracyRate +'</td> <td  class="cz_btn"><input class="_Bku" title="B库" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="Bku" value="B库"  data-id="' + data.items[i].id + '" />  <input class="_Jfei" title="充值" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="Jfei" value="充值"   data-id="' + data.items[i].id + '"  data-phone="' + data.items[i].createdBy + '" data-companyName="' + data.items[i].name + '" data-yu="' + data.items[i].residualFrequency  + '"  /></td> </tr>'
        }

        if (data.items.length == 0) {
            var strxx = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="110"  Name="user_name_" >用户名</th> <th width="40">来源</th><th width="220"  Name="name_" >公司名称</th> <th width="80"  Name="create" >创建时间</th><th width="80"  Name="state" >状态</th> <th width="70"  Name="moreuse" >累计使用（次）</th><th width="70"  Name="alltalk" >剩余点数（点）</th><th width="70"  Name="yester" >昨日使用（次）</th><th width="90"  Name="trues" >正确率</th> <th width="130"  Name="handle" >操作</th> </tr>'
            layer.msg("没有搜索到相应数据");
            
            $('#business_admin').html(strxx)
        } else {
           
            $('#business_admin').html(str)
        }
        
        success();

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
        datacontainer: 'business_admin',
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
            var timeOrDian = $('#timeOrDian').val();
            var companies_in = $('#companies_in').val();
            var starttime = $("#startdate").val();
            var endtime = $("#enddate").val();
            var source = $('#source').val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/companies/admin/search-page?pageNo="+ $("#pageNo").val()  + "&keyword=" + companies_in + "&type="+timeOrDian+ "&starttime=" + starttime + "&endtime=" + endtime +"&source=" + source
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="110"  Name="user_name_" >用户名</th><th width="40">来源</th> <th width="220"  Name="name_" >公司名称</th> <th width="80"  Name="create" >创建时间</th>  <th width="80"  Name="state" >状态</th> <th width="70"  Name="moreuse" >累计使用（次）</th><th width="70"  Name="alltalk" >剩余点数（点）</th><th width="70"  Name="yester" >昨日使用（次）</th><th width="90"  Name="trues" >正确率</th> <th width="130"  Name="handle" >操作</th> </tr>'
                for (var i = 0; i < data.items.length; i++) {
                    var times1 = timess(data.items[i].createdTime);
                    // var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
                    // var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
                    var n = ((($("#pageNo").val()-1)* 15 )+(i+1));
                    if(data.items[i].deleted == false){
                        var deleted = "使用中"
                    }else {
                        var deleted = "已禁用"
                    }
                    str += '<tr style="border-bottom:1px solid #ccc ;"> <td>'+ n +'</td> <td >'+ data.items[i].createdBy+'</td>  <td >'+ data.items[i].source+'</td> <td >'+ data.items[i].name +'</td> <td  >'+ times1 +'</td>  <td  >'+ deleted +'</td> <td  >'+ data.items[i].chatLogAccount +'</td><td  >'+ data.items[i].residualFrequency  +'</td> <td  >'+ data.items[i].chatLogYesterdayAccount +'</td> <td  >'+ data.items[i].accuracyRate +'</td> <td  class="cz_btn"> <input class="_Bku" title="B库" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="Bku" value="B库"  data-id="' + data.items[i].id + '" />  <input class="_Jfei" title="充值" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="Jfei" value="充值"   data-id="' + data.items[i].id + '"  data-phone="' + data.items[i].createdBy + '" data-companyName="' + data.items[i].name + '" data-yu="' + data.items[i].residualFrequency  + '"  /></td> </tr>'
                   
                    $('#business_admin').html(str)
                }
                success();
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

