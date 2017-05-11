function addq() { //添加问题弹窗
    layer.open({
        type: 2,
        title: ['添加同义词', 'font-size:18px;'],
        //btn: ['保存', '取消'],
        closeBtn: 1,
        shade: 0.3,
        skin: 'demo-class400',
        content: '/add_synonym2'
    });
}

function success() {//知识库返回值成功获取--后执行的函数
    // 编辑弹窗<juning>
    $(".edits").click(function () {
        var Id = $(this).attr('data-value');
        var interchange = $(this).attr('data-word_old');
        var baseWord = $(this).attr('data-word_new');
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="min-width: 400px;height: 300px;width: 30%;background: #fff;position: fixed;top: 30%;left: 37%;"> <div style="height: 42px;background: #00c4c2;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">编辑同义词 <div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <form> <input type="hidden" name="id" id="id" value=""> <div style="padding-top: 13px;"><table style="font-size: 16px;"> <tbody> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">被替换词:</span></td> <td > <textarea  name="interchange" required="required" id="interchange" placeholder="(被替换词不能为空，可以添加多个被替换词，每个被替换词之间以“；”号隔开)" style="width: 95%;min-width: 300px; height: 130px; overflow-y: auto; resize: none;border:1px solid #fafafa;line-height: 30px;font-size: 14px;outline: none;    margin: 5px 0;"  maxlength="400"></textarea> </td> </tr> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">标准词:</span></td> <td> <input type="text" class="" name="baseWord" required="required" id="baseWord" placeholder="(标准词最好不要比替换词长！)" style="width: 95%;min-width:300px;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;" maxlength="60"> </td> </tr> <tr> <td >  </td> <td class="btns" align="center"> <a class="btn3">取消</a> <a class="btn2">保存</a></td> </tr> </tbody> </table> </div> </form> </div> </div>'

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
function csh_Syn2(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/customersynonym/search-page?pageNo=" + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr>  <th Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="text-align: center;min-width: 350px;"  Name="interchange" >被替换词</th> <th style="text-align: center;min-width: 150px"  Name="baseWord" >标准词</th> <th  Name="ChangeTime" style="text-align: center;min-width: 150px;">更新时间</th> <th  style="text-align: center;min-width: 100px">修改</th><th  style="text-align: center;min-width: 100px">删除</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
          
            var d2 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times1 = (d2.getFullYear()) + "/" + (d2.getMonth() + 1) + "/" + (d2.getDate());
            var n = (i + 1);
            str += '<tr > <td  style="text-align: center;">' + n + '</td><td style="text-align: center;"><p >' + data.items[i].word_old + '</p></td><td style="max-height: 43px;text-align: center;"><p >' + data.items[i].word_new + '</p></td><td  style="text-align: center;">' + times1 + '</td><td style="text-align: center;"><input class="edits"  type="button" name="edits" data-word_old="' + data.items[i].word_old + '" data-word_new="' + data.items[i].word_new + '"  data-value="' + data.items[i].id + '"/></td><td><input class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
               $('#tabProduct').html(str)
        }
        if (data.items.length == 0) {
            var strxx ='<tr>  <th Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="text-align: center;min-width: 350px;"  Name="interchange" >被替换词</th> <th style="text-align: center;min-width: 150px"  Name="baseWord" >标准词</th> <th  Name="ChangeTime" style="text-align: center;min-width: 150px;">更新时间</th> <th  style="text-align: center;min-width: 100px">修改</th><th  style="text-align: center;min-width: 100px">删除</th> </tr>';
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
        var str = '<tr>  <th Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="text-align: center;min-width: 350px;"  Name="interchange" >被替换词</th> <th style="text-align: center;min-width: 150px"  Name="baseWord" >标准词</th> <th  Name="ChangeTime" style="text-align: center;min-width: 150px;">更新时间</th> <th  style="text-align: center;min-width: 100px">修改</th><th  style="text-align: center;min-width: 100px">删除</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {

            var d2 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times1 = (d2.getFullYear()) + "/" + (d2.getMonth() + 1) + "/" + (d2.getDate());
            var n = (i + 1);
            str += '<tr > <td  style="text-align: center;">' + n + '</td><td style="text-align: center;"><p >' + data.items[i].word_old + '</p></td><td style="max-height: 43px;text-align: center;"><p >' + data.items[i].word_new + '</p></td><td  style="text-align: center;">' + times1 + '</td><td style="text-align: center;"><input class="edits"  type="button" name="edits" data-word_old="' + data.items[i].word_old + '" data-word_new="' + data.items[i].word_new + '"  data-value="' + data.items[i].id + '"/></td><td><input class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
            $('#tabProduct').html(str)
        }
        if (data.items.length == 0) {
            var strxx ='<tr>  <th Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="text-align: center;min-width: 350px;"  Name="interchange" >被替换词</th> <th style="text-align: center;min-width: 150px"  Name="baseWord" >标准词</th> <th  Name="ChangeTime" style="text-align: center;min-width: 150px;">更新时间</th> <th  style="text-align: center;min-width: 100px">修改</th><th  style="text-align: center;min-width: 100px">删除</th> </tr>';
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
                var str = '<tr>  <th Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="text-align: center;min-width: 350px;"  Name="interchange" >被替换词</th> <th style="text-align: center;min-width: 150px"  Name="baseWord" >标准词</th> <th  Name="ChangeTime" style="text-align: center;min-width: 150px;">更新时间</th> <th  style="text-align: center;min-width: 100px">修改</th><th  style="text-align: center;min-width: 100px">删除</th> </tr>';
                for (var i = 0; i < data.items.length; i++) {

                    var d2 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
                    var times1 = (d2.getFullYear()) + "/" + (d2.getMonth() + 1) + "/" + (d2.getDate());
                    var n = (i + 1);
                    str += '<tr > <td  style="text-align: center;">' + n + '</td><td style="text-align: center;"><p >' + data.items[i].word_old + '</p></td><td style="max-height: 43px;text-align: center;"><p >' + data.items[i].word_new + '</p></td><td  style="text-align: center;">' + times1 + '</td><td style="text-align: center;"><input class="edits"  type="button" name="edits" data-word_old="' + data.items[i].word_old + '" data-word_new="' + data.items[i].word_new + '"  data-value="' + data.items[i].id + '"/></td><td><input class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
                    $('#tabProduct').html(str)
                }
                success();
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

