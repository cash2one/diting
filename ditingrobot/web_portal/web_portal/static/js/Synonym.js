function addq() { //添加问题弹窗
    layer.open({
        type: 2,
        title: ['添加同义词', 'font-size:18px;'],
        //btn: ['保存', '取消'],
        closeBtn: 1,
        shade: 0.3,
        skin: 'demo-class9',
        content: '/add_synonym'
    });
}

function success() {//知识库返回值成功获取--后执行的函数
    // 编辑弹窗<juning>
    $(".edits").click(function () {
        var Id = $(this).attr('data-value');
        var interchange = $(this).attr('data-word_old');
        var baseWord = $(this).attr('data-word_new');
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width: 400px;height: 250px;background: #fff;position: fixed;top: 30%;left: 37%;"> <div style="height: 42px;background: #4898D5;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">编辑同义词 <div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <form> <input type="hidden" name="id" id="id" value=""> <div style="padding-top: 13px;"><table> <tbody> <tr> <td style="display: block;text-align: right;width: 136px;height: 50px;line-height: 50px;">被替换词:</td> <td style="text-align: left"> <input type="text" class="" name="interchange" required="required" id="interchange" placeholder="(被替换词不能为空)" style="margin-right: 90px;height: 20px;"  maxlength="60"> </td> </tr> <tr> <td style="display: block;text-align: right;width: 136px;height: 50px;line-height: 50px;">标准词:</td> <td  style="text-align: left"> <input type="text" class="" name="baseWord" required="required" id="baseWord" placeholder="(标准词最好不要比替换词长！)" style="margin-right: 90px;height: 20px;" maxlength="60"> </td> </tr> <tr> <td class="btns" align="center"> <a class="btn2" style="margin-left: 40%;">保存</a> </td> <td class="btns" align="center"> <a class="btn3" style="cursor: pointer;">取消</a> </td> </tr> </tbody> </table> </div> </form> </div> </div>'
        $('#tanc').html(str)
        $('#interchange').val(interchange);
        $('#baseWord').val(baseWord);
        $(".close_update_c").click(function () {//叉掉
            closeSY()
        })

        $('.btn2').click(function () {//保存
            var newinterchange = $('#interchange').val();
            var newbaseWord = $('#baseWord').val();
            var editknows = {
                word_old: newinterchange,
                word_new: newbaseWord,
                id: Id
            };
            // if (newinterchange != "" && newbaseWord != "" && newinterchange.length >= newbaseWord.length ) {
            if (newinterchange != "" && newbaseWord != "" ) {
                //保存把数据传到后台
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/api/customersynonym/update',
                    data: JSON.stringify(editknows)
                }).done(function (data) {
                    layer.msg(data.message, {time:500,icon: 6},function(){
                        closeSY()
                        window.location.reload(false);
                    });
                    // parent.history.go(0);//保存post传出数据后刷新页面
                }).fail(function (data) {
                    console.log(data);
                    var message = JSON.parse(data.responseText).message;
                    layer.msg(message, {icon: 5})
                });
            } else {
                layer.msg("标准词和替换词不能为空！", {icon: 5})
            }

        })
        $('.btn3').click(function () {//取消
            closeSY()
        })

        function closeSY() {
            $('#tanc').html("")
        }
    });
    //删除这条数据：初始加载页面
    $(".deletes").click(function () {
        var Id = $(this).attr('data-value');
        layer.confirm('您真的确定要删除了吗？', {
            btn: ['确定', '再考虑一下'] //按钮
        }, function () {
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/customersynonym/delete/' + Id
            }).done(function (data) {
                layer.msg(data.message, {icon: 6})
                window.location.reload(false);
                // parent.history.go(0);//取消键刷新页面
            }).fail(function (data) {
                console.log(data);
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        }, function () {

        });
    })
}

//初始化知识库
function csh_Syn(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/customersynonym/search-page?pageNo=" + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="160" style="text-align: center;"  Name="interchange" EditType="TextBox" maxlength="60">被替换词</th> <th style="text-align: center;" width="160"  Name="baseWord" EditType="TextBox" maxlength="60">标准词</th> <th width="200"  Name="addTime" EditType="TextBox" style="text-align: center;">添加时间</th> <th width="200"  Name="ChangeTime" style="text-align: center;">更新时间</th> <th width="150"  Name="Handle" style="text-align: center;">操作</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d1 = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
            var times = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var d2 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times1 = (d2.getFullYear()) + "/" + (d2.getMonth() + 1) + "/" + (d2.getDate());
            var n = (i + 1);
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td width="50"  style="text-align: center;">' + n + '</td><td width="160" style="text-align: center;"><p style="width: 160px">' + data.items[i].word_old + '</p></td><td width="160" style="max-height: 43px;text-align: center;"><p style="width: 160px">' + data.items[i].word_new + '</p></td><td width="200" style="text-align: center;">' + times + '</td><td width="200" style="text-align: center;">' + times1 + '</td><td width="150" style="text-align: center;"><input class="edits"  type="button" name="edits" data-word_old="' + data.items[i].word_old + '" data-word_new="' + data.items[i].word_new + '"  data-value="' + data.items[i].id + '"/>&nbsp;&nbsp;<input class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
               $('#tabProduct').html(str)
        }
        if (data.items.length == 0) {
            var strxx = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="160"  Name="interchange" EditType="TextBox" maxlength="60" style="text-align: center;">被替换词</th> <th width="160" style="text-align: center;"  Name="baseWord" EditType="TextBox" maxlength="60">标准词</th> <th width="200"  Name="addTime" EditType="TextBox" style="text-align: center;">添加时间</th> <th width="200"  Name="ChangeTime" style="text-align: center;">更新时间</th> <th width="150"  Name="Handle" style="text-align: center;">操作</th> </tr>';
            layer.msg("没有搜索到相应数据");
            $('#tabProduct').html(strxx)
        } else {
            $('#tabProduct').html(str)
        }
        success();

    }).fail(function (data) {
        console.log(data);
    });

}



//同义词搜索功能
//	------------------------
function changess_Synonym(){
    var navsearchinput = encodeURI($('#nav-search-input').val());
    $("#pageNo").val(1)
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/customersynonym/search-page?pageNo="+$("#pageNo").val()+"&keywords="+ navsearchinput +""
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data)
        Paging_device(data.total);
        var str = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="160"  Name="interchange" EditType="TextBox" maxlength="60" style="text-align: center;">被替换词</th> <th width="160"  Name="baseWord" EditType="TextBox" maxlength="60" style="text-align: center;">标准词</th> <th width="200"  Name="addTime" EditType="TextBox" style="text-align: center;">添加时间</th> <th width="200"  Name="ChangeTime" style="text-align: center;">更新时间</th> <th width="150"  Name="Handle" style="text-align: center;">操作</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d1 = new Date(data.items[i].createdTime );    //根据时间戳生成的时间对象
            var times = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var d2 = new Date(data.items[i].updatedTime );    //根据时间戳生成的时间对象
            var times1 = (d2.getFullYear()) + "/" + (d2.getMonth() + 1) + "/" + (d2.getDate());
            var n = (i + 1);
            str += '<tr style="border-bottom:1px solid #ccc ;"> <td width="50"  style="text-align: center;">' + n + '</td><td width="160" style="text-align: center;"><p style="width: 160px">' + data.items[i].word_old + '</p></td><td width="160" style="max-height: 43px;text-align: center;"><p style="width: 160px">' + data.items[i].word_new + '</p></td><td width="200" style="text-align: center;">' + times + '</td><td width="200" style="text-align: center;">' + times1 + '</td><td width="150" style="text-align: center;"><input class="edits"  type="button" name="edits" data-word_old="' + data.items[i].word_old + '" data-word_new="' + data.items[i].word_new + '"  data-value="' + data.items[i].id + '"/>&nbsp;&nbsp;<input class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
        }
        if(data.items.length==0){
            var strxx = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="160"  Name="interchange" EditType="TextBox" maxlength="60" style="text-align: center;">被替换词</th> <th width="160" style="text-align: center;"  Name="baseWord" EditType="TextBox" maxlength="60">标准词</th> <th width="200"  Name="addTime" EditType="TextBox" style="text-align: center;">添加时间</th> <th width="200"  Name="ChangeTime" style="text-align: center;">更新时间</th> <th width="150"  Name="Handle" style="text-align: center;">操作</th> </tr>';
            layer.msg("没有搜索到相应数据");
            $('#tabProduct').html(strxx)
        }else {
            $('#tabProduct').html(str)
        }
        success();
    }).fail(function (data) {
        console.log(data);
    });
}


//分页器，页码切换执行
function Paging_device(strips) { //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var x = $("#pageNo").val();
    $('#red').smartpaginator({
        totalrecords: strips,
        datacontainer: 'tabProduct',
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
            //页面转换后知识内容加载
            var navsearchinput = encodeURI($('#nav-search-input').val());
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/customersynonym/search-page?pageNo=" + $("#pageNo").val() + "&keywords=" + navsearchinput + ""
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="160" style="text-align: center;" Name="interchange" EditType="TextBox" maxlength="60">被替换词</th> <th width="160" style="text-align: center;" Name="baseWord" EditType="TextBox" maxlength="60">标准词</th> <th width="200"  Name="addTime" EditType="TextBox" style="text-align: center;">添加时间</th> <th width="200"  Name="ChangeTime" style="text-align: center;">更新时间</th> <th width="150"  Name="Handle" style="text-align: center;">操作</th> </tr>';
                for (var i = 0; i < data.items.length; i++) {
                    var d1 = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                    var times = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
                    var d2 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
                    var times1 = (d2.getFullYear()) + "/" + (d2.getMonth() + 1) + "/" + (d2.getDate());
                    var n = ((($("#pageNo").val() - 1) * 15) + (i + 1));
                    str += '<tr style="border-bottom:1px solid #ccc ;"> <td width="50"  style="text-align: center;">' + n + '</td><td width="160" style="text-align: center;"><p style="160px">' + data.items[i].word_old + '</p></td><td width="160" style="max-height: 43px;text-align: center;"><p style="width:160px;">' + data.items[i].word_new + '</p></td><td width="200" style="text-align: center;">' + times + '</td><td width="200" style="text-align: center;">' + times1 + '</td><td width="150" style="text-align: center;"><input class="edits"  type="button" name="edits" data-word_old="' + data.items[i].word_old + '" data-word_new="' + data.items[i].word_new + '"  data-value="' + data.items[i].id + '"/>&nbsp;&nbsp;<input class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
                  
                    $('#tabProduct').html(str)
                }
                success();
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

