function timess(timestamp3) {
    var d = new Date(timestamp3);    //根据时间戳生成的时间对象
    var Hour = d.getHours() > 9 ? d.getHours() : "0" + d.getHours();
    var Min = d.getMinutes() > 9 ? d.getMinutes() : "0" + d.getMinutes();
    var Sec = d.getSeconds() > 9 ? d.getSeconds() : "0" + d.getSeconds();
    var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate()) + " " + (Hour) + ":" + (Min) + ":" + (Sec);
    return times
}


//初始化用户管理界面（问答与统计）
function csh_user_cou() {
    $.ajaxSetup({cache: false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/chatlog/search-group?pageNo=" + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_ques_count(data.total);
        var str = '<tr> <th width="156">用户姓名</th> <th width="185">用户类别</th> <th width="187">对话总次数</th> <th width="153">记录</th> <th width="317">最近一次使用时间</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var userClass = '';
            var times = timess(data.items[i].createdTime);

            if (data.items[i].extra4 == "1") {
                userClass = 'web机器人'
            } else {
                userClass = '微信机器人'
            }
            var loginUserName = '';
            if (data.items[i].loginUsername == undefined) {
                loginUserName = '游客'+(i + 1);
            } else {
                loginUserName = data.items[i].loginUsername;
            }
            str += '<tr> <td>'+loginUserName +'</td> <td>' + userClass + '</td> <td>' + data.items[i].count + '</td> <td> <a  class="newsRecord" style="text-decoration:none;cursor: pointer" onclick="bnn(this)" data-id="' + data.items[i].uuid + '">消息记录</a> </td> <td>' + times + '</td> </tr>';

        }

        if (data.items.length == 0) {
            var strxx = '<tr> <th width="156">用户姓名</th> <th width="185">用户类别</th> <th width="187">对话总次数</th> <th width="153">记录</th> <th width="317">最近一次使用时间</th> </tr>';

            layer.msg("没有搜索到相应数据");
            $('#mt').html(strxx)

        } else {
            $('#mt').html(str)
        }

    }).fail(function (data) {
        console.info(data);

    });

}


//分页器，页码切换执行(用户管理页)
function Paging_ques_count(strips) { //参数是总计多少条
    $.ajaxSetup({cache: false})  //禁止ie浏览器读取缓存的ajax
    var x = $("#pageNo").val();
    $('#red').smartpaginator({

        totalrecords: strips,
        datacontainer: 'mt',
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
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/company/chatlog/search-group?pageNo=" + $("#pageNo").val()
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_ques_count(data.total);
                var str = '<tr> <th width="156">用户姓名</th> <th width="185">用户类别</th> <th width="187">对话总次数</th> <th width="153">记录</th> <th width="317">最近一次使用时间</th> </tr>';
                for (var i = 0; i < data.items.length; i++) {
                    var userClass = '';
                    var times = timess(data.items[i].createdTime);
                    if (data.items[i].extra4 == "1") {
                        userClass = 'web机器人'
                    } else {
                        userClass = '微信机器人'
                    }
                    var loginUserName = '';
                    if (data.items[i].loginUsername == undefined) {
                        loginUserName = '游客'+(i+1);
                    } else {
                        loginUserName = data.items[i].loginUsername;
                    }
                    str += '<tr> <td>' + loginUserName + '</td> <td>' + userClass + '</td> <td>' + data.items[i].count + '</td> <td> <a  class="newsRecord" style="text-decoration:none;cursor: pointer" onclick="bnn(this)" data-id="' + data.items[i].uuid + '">消息记录</a> </td> <td>' + times + '</td> </tr>';

                    $('#mt').html(str)
                }


            }).fail(function (data) {
                console.info(data);

            });
        }
    });
}


//点击消息记录

function bnn(obj) {
    $('#mt').css('overflow', 'hidden')
    var newsrecordid = $(obj).attr('data-id');
    $('#thisuuid').val(newsrecordid);
    var str = '<div style="height: 36px;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 36px;"><div class="close_update_c" onclick="closenews()" style="height: 29px;width: 15px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <div style="height: 452px;overflow-y:scroll;"> <input type="hidden" name="id" id="id" value=""> '
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/chatlog/searchpage?pageNo=" + 1 + "&uuid=" + newsrecordid
    }).done(function (data) {
        var data = JSON.parse(data);
        $("#pageNo").val(1);
        Paging_record(data.total);
        for (var i = 0; i < data.items.length; i++) {
            var times = timess(data.items[i].createdTime);
            str += '<p style="color: #00c4c2;font-size: 14px;margin: 5px 125px;">问：' + data.items[i].question + '&nbsp;&nbsp;&nbsp;&nbsp;' + times + '</p><p style="font-size: 14px;margin: 5px 125px;">答：' + data.items[i].answer + '</p>'

        }
        str += '</div>'

        $('#mt').html(str)

    }).fail(function (data) {
        console.info(data);

    });

}
function closenews() {
    window.location.reload(false);
}


//分页器，页码切换执行（用户管理--消息记录窗口)
function Paging_record(strips) { //参数是总计多少条
    $.ajaxSetup({cache: false})  //禁止ie浏览器读取缓存的ajax
    var x = $("#pageNo").val();
    $('#red').smartpaginator({

        totalrecords: strips,
        datacontainer: 'mt',
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
            var newsrecordid = $('#thisuuid').val();
            var str = '<div style="height: 36px;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 36px;"><div class="close_update_c" onclick="closenews()" style="height: 29px;width: 15px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <div style="height: 452px;overflow-y:scroll;"> <input type="hidden" name="id" id="id" value=""> '
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/company/chatlog/searchpage?pageNo=" + $("#pageNo").val() + "&uuid=" + newsrecordid
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_record(data.total);
                for (var i = 0; i < data.items.length; i++) {
                    var times = timess(data.items[i].createdTime);
                    str += '<p style="color: #00c4c2;font-size: 14px;margin: 5px 125px;">问：' + data.items[i].question + '&nbsp;&nbsp;&nbsp;&nbsp;' + times + '</p><p style="font-size: 14px;margin: 5px 125px;">答：' + data.items[i].answer + '</p>'

                }
                str += '</div>'

                $('#mt').html(str)
            }).fail(function (data) {
                console.info(data);

            });
        }
    })
}


