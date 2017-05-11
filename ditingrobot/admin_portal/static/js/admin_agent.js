//全选框
function checkAll(name) {//全选中
    var el = document.getElementsByTagName('input');
    var len = el.length;
    for (var i = 0; i < len; i++) {
        if ((el[i].type == "checkbox") && (el[i].name == name)) {
            el[i].checked = true;
        }
    }
}

function clearAll(name) {//取消选中
    var el = document.getElementsByTagName('input');
    var len = el.length;
    for (var i = 0; i < len; i++) {
        if ((el[i].type == "checkbox") && (el[i].name == name)) {
            el[i].checked = false;
        }
    }
}

function success() {
    //全选框
    $("#allcheckeds").click(function () {
        if (this.checked == true) {
            checkAll('checkbox2');
        } else {
            clearAll('checkbox2');
        }
    });
    $("#alldeletes").click(function () {
        // dataupid();
    })
}

function addAgent() {
    //添加代理账号
    var str = "";
    str +=' <div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width: 400px;height: 250px;background: #fff;position: fixed;top: 30%;left: 37%;"> <div style="height: 42px;background: #4898D5;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">添加代理 <div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <form> <input type="hidden" name="id" id="id" value=""> <div style="padding-top: 13px;"> <table style="font-size: 16px;"> <tbody> <tr> <td style="width:77px;text-align: right;padding-top: 13px;display: block;">账号:</td> <td style="padding-top: 13px;"> <input type="text" name="account" id="admin_account" style="width:255px;font-size: 13px" title="账号" placeholder="这里输入账号" maxlength="30" value="" /> </td> </tr> <tr> <td style="display: block;width:77px;text-align: right;padding-top: 13px;;">密码:</td> <td style="padding-top: 13px;"> <input type="text" name="password"  id="admin_pwd" style="width:255px;font-size: 13px" title="密码" placeholder="这里输入密码" maxlength="30" value="" /> </td> </tr> <tr> <td style="display: block;width:77px;text-align: right;padding-top: 13px;">联系方式:</td> <td style="padding-top: 13px;"> <input type="text" name="phone" id="admin_phone" style="width:255px;font-size: 13px" title="手机号" placeholder="这里输入手机号" maxlength="11" value="" /> </td> </tr> <tr> <td style="display: block;"></td> <td class="btns"> <a class="btn2" style="cursor: pointer;">添加</a> <a class="btn3" style="cursor: pointer;">取消</a> </td> </tr> </tbody> </table> </div> </form> </div> </div>'
    $('#tanc').html(str)
    $(".close_update_c").click(function () {//叉掉
        closeXI()
    })

    $('.btn2').click(function () {//保存
        var admin_account = $('#admin_account').val();
        var admin_pwd = $('#admin_pwd').val();
        var admin_phone = $('#admin_phone').val();
        var editknows = {
            userName: admin_account,
            password: admin_pwd,
            mobile: admin_phone
        };
        if (admin_account != "" && admin_pwd != "" && admin_phone != "") {
            //保存把数据传到后台
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/agents/register',
                data: JSON.stringify(editknows)
            }).done(function (data) {
                window.location.reload(false);
                layer.msg(data.message, {time:500,icon: 6},function(){
                    closeXI()
                    
                });
            }).fail(function (data) {
                console.log(data);
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        } else {
            layer.msg("请全部填好！", {icon: 5})
        }

    })
    $('.btn3').click(function () {//取消
        closeXI()
    })

    function closeXI() {
        $('#tanc').html("")
    }
}



//初始化代理商信息界面
function csh_agent() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: 'api/agents/search-page?pageNo=' + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        Paging_device(data.total);
        var str = '<tr><th width="50"  Name="checkb" ><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th width="80"  Name="num" >序号</th> <th width="200"  Name="tell" >联系方式</th> <th width="200"  Name="tell" >账号</th> <th width="200"  Name="tell" >密码</th> <th width="230"  Name="tell" >邀请码</th>  </tr>'
        for (var i = 0; i < data.items.length; i++) {
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td width="50"><input type="checkbox" name="checkbox2" value="' + data.items[i].id + '" /></td> <td width="80">' + (i+1) + '</td> <td >' + data.items[i].mobile + '</td><td >' + data.items[i].userName + '</td> <td >' + data.items[i].password + '</td> <td >' + data.items[i].invitationCode + '</td></tr>'
           
            $('#admin_agent').html(str)
        }
        success();
    }).fail(function (data) {
        console.log(data);

    });

}

//回车键搜索功能
function enterAgentSearch(ev){
    var oEvent = ev || event;
    if (oEvent.keyCode == 13 && oEvent.ctrlKey || oEvent.keyCode == 13) {
        agentSearch()
    }
}

function agentSearch(){
   
    var agentSearchInput = $('#agentSearchInput').val();
    $("#pageNo").val(1);
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: 'api/agents/search-page?pageNo=' + $("#pageNo").val() + '&keywords=' + agentSearchInput
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr><th width="50"  Name="checkb" ><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th width="80"  Name="num" >序号</th> <th width="200"  Name="tell" >联系方式</th> <th width="200"  Name="tell" >账号</th> <th width="200"  Name="tell" >密码</th> <th width="230"  Name="tell" >邀请码</th>  </tr>'
        for (var i = 0; i < data.items.length; i++) {
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td width="50"><input type="checkbox" name="checkbox2" value="' + data.items[i].id + '" /></td> <td width="80">' + (i+1) + '</td> <td >' + data.items[i].mobile + '</td><td >' + data.items[i].userName + '</td> <td >' + data.items[i].password + '</td> <td >' + data.items[i].invitationCode + '</td></tr>'
        }
        if(data.items.length==0){
            var strx = '<tr><th width="50"  Name="checkb" ><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th width="80"  Name="num" >序号</th> <th width="200"  Name="tell" >联系方式</th> <th width="200"  Name="tell" >账号</th> <th width="200"  Name="tell" >密码</th> <th width="230"  Name="tell" >邀请码</th>  </tr>'
            layer.msg("没有搜索到相应数据");
            
            $('#admin_agent').html(strx)
        }else {
         
            $('#admin_agent').html(str)
        }
        success();
    }).fail(function (data) {
        console.info(data);

    });
}







//分页器，页码切换执行
function Paging_device(strips) { //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var x = $("#pageNo").val();
    $('#red').smartpaginator({
        totalrecords: strips,
        datacontainer: 'invalid',
        recordsperpage: 15,
        dataelement: 'tr',
        length: 4,
        next: '下一页',
        prev: '上一页',
        first: '首页',
        last: '尾页',
        theme: 'red',
        initval: x,
        controlsalways: true,
        onchange: function () {
            var agentSearchInput = $('#agentSearchInput').val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url:  'api/agents/search-page?pageNo=' + $("#pageNo").val() + '&keywords=' + agentSearchInput
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr><th width="50"  Name="checkb" ><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th width="80"  Name="num" >序号</th> <th width="200"  Name="tell" >联系方式</th> <th width="200"  Name="tell" >账号</th> <th width="200"  Name="tell" >密码</th> <th width="230"  Name="tell" >邀请码</th>  </tr>'
                for (var i = 0; i < data.items.length; i++) {
                    str += '<tr style="border-bottom:1px solid #ccc ;"> <td width="50"><input type="checkbox" name="checkbox2" value="' + data.items[i].id + '" /></td> <td width="80">' + (i+1) + '</td> <td >' + data.items[i].mobile + '</td><td >' + data.items[i].userName + '</td> <td >' + data.items[i].password + '</td> <td >' + data.items[i].invitationCode + '</td></tr>'
                    $('#admin_agent').html(str)
                }
                success();
            }).fail(function (data) {
                console.log(data);

            });
        }
    });
}

