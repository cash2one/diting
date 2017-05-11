
//初始化代理商企业管理信息
function csh_agent() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/dl/api/agent/companies/search-page?pageNo="+ 1 + '&invitationCode='+ $('#invitationCode0').val()
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data)
        Paging_device(data.total);
        var str = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="260"  Name="name_" >公司名称</th> <th width="100"  Name="create" >创建时间</th> <th width="100"  Name="state" >状态</th> <th width="110"  Name="moreuse" >累计使用(次)</th><th width="110"  Name="yester" >昨日使用(次)</th><th width="100"  Name="trues" >正确率</th>  </tr>'
        for (var i = 0; i < data.items.length; i++) {
            var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
            var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            if(data.items[i].name == null){
                var name = "无"
            }else {
                var name = data.items[i].name
            }
            if(data.items[i].chatLogAccount == null){
                var chatLogAccount = "0"
            }else {
                var chatLogAccount = data.items[i].chatLogAccount
            }
            if(data.items[i].chatLogYesterdayAccount == null){
                var chatLogYesterdayAccount = "0"
            }else {
                var chatLogYesterdayAccount = data.items[i].chatLogYesterdayAccount
            }
            if(data.items[i].accuracyRate == null){
                var accuracyRate = "0.00%"
            }else {
                var accuracyRate = data.items[i].accuracyRate
            }

            if(data.items[i].deleted == false){
                var deleted = "使用中"
            }else {
                var deleted = "已禁用"
            }
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td>'+ n +'</td> <td >'+ data.items[i].createdBy+'</td> <td >'+ name +'</td> <td  >'+ times1 +'</td> <td  >'+ deleted +'</td> <td  >'+ chatLogAccount +'</td><td  >'+ chatLogYesterdayAccount +'</td> <td  >'+accuracyRate +'</td></tr>'
        }
        $('#agent_admin').html(str);

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
    $("#pageNo").val(1)
    var url = "/dl/api/agent/companies/search-page?pageNo="+ 1 + '&invitationCode='+ $('#invitationCode0').val()
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: url
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="260"  Name="name_" >公司名称</th> <th width="100"  Name="create" >创建时间</th> <th width="100"  Name="state" >状态</th> <th width="110"  Name="moreuse" >累计使用(次)</th><th width="110"  Name="yester" >昨日使用(次)</th><th width="100"  Name="trues" >正确率</th> </tr>'
        for (var i = 0; i < data.items.length; i++) {

            var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
            var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            if(data.items[i].name == null){
                var name = "无"
            }else {
                var name = data.items[i].name
            }
            if(data.items[i].chatLogAccount == null){
                var chatLogAccount = "0"
            }else {
                var chatLogAccount = data.items[i].chatLogAccount
            }
            if(data.items[i].chatLogYesterdayAccount == null){
                var chatLogYesterdayAccount = "0"
            }else {
                var chatLogYesterdayAccount = data.items[i].chatLogYesterdayAccount
            }
            if(data.items[i].accuracyRate == null){
                var accuracyRate = "0.00%"
            }else {
                var accuracyRate = data.items[i].accuracyRate
            }

            if(data.items[i].deleted == false){
                var deleted = "使用中"
            }else {
                var deleted = "已禁用"
            }
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td>'+ n +'</td> <td >'+ data.items[i].createdBy+'</td> <td >'+name +'</td> <td  >'+ times1 +'</td> <td  >'+ deleted +'</td> <td  >'+ chatLogAccount +'</td><td  >'+ chatLogYesterdayAccount +'</td> <td  >'+ accuracyRate +'</td> </tr>'
        }

        if (data.items.length == 0) {
            var strxx = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="260"  Name="name_" >公司名称</th> <th width="100"  Name="create" >创建时间</th> <th width="100"  Name="state" >状态</th> <th width="110"  Name="moreuse" >累计使用(次)</th><th width="110"  Name="yester" >昨日使用(次)</th><th width="100"  Name="trues" >正确率</th></tr>'
            layer.msg("没有搜索到相应数据");
            
            $('#agent_admin').html(strxx)
        } else {
           
            $('#agent_admin').html(str)
        }
    }).fail(function (data) {
        console.log(data);
    });
}


//分页器，页码切换执行
function Paging_device(strips){ //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    // var companies_in = $('#companies_in').val();
    var x=$("#pageNo").val();
    $('#red').smartpaginator({

        totalrecords: strips,
        datacontainer: 'agent_admin',
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

            // var companies_in = $('#companies_in').val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url:"/dl/api/agent/companies/search-page?pageNo="+ $("#pageNo").val() + '&invitationCode='+ $('#invitationCode0').val()
                // url: "/api/companies/admin/search-page?pageNo="+ $("#pageNo").val()  + "&keyword=" + companies_in
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="260"  Name="name_" >公司名称</th> <th width="100"  Name="create" >创建时间</th> <th width="100"  Name="state" >状态</th> <th width="110"  Name="moreuse" >累计使用(次)</th><th width="110"  Name="yester" >昨日使用(次)</th><th width="100"  Name="trues" >正确率</th>  </tr>'
                for (var i = 0; i < data.items.length; i++) {
                    var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
                    var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
                    var n = ((($("#pageNo").val()-1)* 15 )+(i+1));
                    if(data.items[i].name == null){
                        var name = "无"
                    }else {
                        var name = data.items[i].name
                    }
                    if(data.items[i].chatLogAccount == null){
                        var chatLogAccount = "0"
                    }else {
                        var chatLogAccount = data.items[i].chatLogAccount
                    }
                    if(data.items[i].chatLogYesterdayAccount == null){
                        var chatLogYesterdayAccount = "0"
                    }else {
                        var chatLogYesterdayAccount = data.items[i].chatLogYesterdayAccount
                    }
                    if(data.items[i].accuracyRate == null){
                        var accuracyRate = "0.00%"
                    }else {
                        var accuracyRate = data.items[i].accuracyRate
                    }

                    if(data.items[i].deleted == false){
                        var deleted = "使用中"
                    }else {
                        var deleted = "已禁用"
                    }
                    str += '<tr style="border-bottom:1px solid #ccc ;"> <td>'+ n +'</td> <td >'+ data.items[i].createdBy+'</td> <td >'+ name +'</td> <td  >'+ times1 +'</td>  <td  >'+ deleted +'</td> <td  >'+ chatLogAccount +'</td> <td  >'+chatLogYesterdayAccount +'</td> <td  >'+ accuracyRate +'</td> </tr>'
                   
                    $('#agent_admin').html(str)
                }

            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

