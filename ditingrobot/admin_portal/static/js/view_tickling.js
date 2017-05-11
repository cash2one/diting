//	---------------------


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
function dataupid() {//批量删除
    var checkedNum = $("input[name='checkbox2']:checked").length;
    if (checkedNum == 0) {
        layer.msg("请选择至少一项！")
        return;
    }
    layer.confirm('确定要删除所选无效问题？', {
        btn: ['确定', '再考虑一下'] //按钮
    }, function () {
        var checkedList = new Array();
        $("input[name='checkbox2']:checked").each(function () {
            checkedList.push($(this).val());
        });
        var newstart = "id=" + checkedList.join("|id=");
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: '/api/admin/message/batchdelete?id_str=' + newstart
            // data: JSON.stringify({'ids': checkedList.toString()})
        }).done(function (data) {
            layer.msg(data.message, {icon: 6})
            window.location.reload(false);
        }).fail(function (data) {
            // console.info(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });

    }, function () {

    });

}

function success() { //返回值成功获取--后执行的函数
//查看
    $("._talkO").click(function () {
        var companiesId = $(this).attr('data-id');
        window.location.href = "/api/search-message/" + companiesId
    });
    
    //全选框
    $("#allcheckeds").click(function () {
        if (this.checked == true) {
            checkAll('checkbox2');
        } else {
            clearAll('checkbox2');
        }
    });
    $("#alldeletes").click(function () {
        dataupid();
    })
}


//客户反馈信息
function csh_view(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/admin/message/searchpage?pageNo=" + 1

    }).done(function (data) {
      
        var data = JSON.parse(data);

        Paging_device(data.total);
        var str = '<tr> <th width="32" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="500"  Name="view_main" >反馈内容</th> <th width="110"  Name="create" >反馈时间</th> <th width="150"  Name="tell" >联系方式</th> <th width="110"  Name="handle" >查看&评论</th> </tr>'
        for (var i = 0; i < data.items.length; i++) {
            var d1 = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
            var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            str += '<tr style="border-bottom:1px solid #ccc ;"><td width="32" align="center" ><input style="margin-left:10px;" type="checkbox" name="checkbox2" value="' + data.items[i].id + '"/></td>  <td>' + n + '</td> <td >' + data.items[i].createdBy + '</td> <td class="suggestion"> ' + data.items[i].suggestion + '</td><td  >' + times1 + '</td> <td> ' + data.items[i].contactInformation + '</td> <td  class="cz_btn"><input class="_talkO" title="评论" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="talkO" value="详情"  data-id="' + data.items[i].id + '" /> </td> </tr>'
        }

        if (data.items.length == 0) {
            var strxx = '<tr> <th width="32" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="500"  Name="view_main" >反馈内容</th> <th width="110"  Name="create" >反馈时间</th> <th width="150"  Name="tell" >联系方式</th> <th width="110"  Name="handle" >查看&评论</th> </tr>'
            layer.msg("没有搜索到相应数据");
             $('#view_tickling').html(strxx)
        } else {
            $('#view_tickling').html(str)
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
        datacontainer: 'business_admin',
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
            //页面转换后内容加载

            var companies_in = $('#companies_in').val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/admin/message/searchpage?pageNo=" + $("#pageNo").val()
            }).done(function (data) {
                var data = JSON.parse(data);
                console.info(data);
                Paging_device(data.total);
                var str = '<tr> <th width="32" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th width="50"  Name="_num_" >序号</th> <th width="150"  Name="user_name_" >用户名</th> <th width="500"  Name="view_main" >反馈内容</th> <th width="110"  Name="create" >反馈时间</th> <th width="150"  Name="tell" >联系方式</th> <th width="110"  Name="handle" >查看&评论</th> </tr>'

                for (var i = 0; i < data.items.length; i++) {
                    var d1 = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                    var times1 = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());

                    var n = ((($("#pageNo").val() - 1) * 15 ) + (i + 1));
                    str += '<tr style="border-bottom:1px solid #ccc ;"><td width="32" align="center" ><input style="margin-left:10px;" type="checkbox" name="checkbox2" value="' + data.items[i].id + '"/></td>  <td>' + n + '</td> <td >' + data.items[i].createdBy + '</td> <td class="suggestion"> ' + data.items[i].suggestion + '</td><td  >' + times1 + '</td> <td> ' + data.items[i].contactInformation + '</td> <td  class="cz_btn"><input class="_talkO" title="评论" style="background:#87b87f ;width: 36px;height: 26px;color:#fff;font-size: 12px;" type="button" name="talkO" value="详情"  data-id="' + data.items[i].id + '" /> </td> </tr>'
                    $('#view_tickling').html(str)
                }
                success();

            }).fail(function (data) {
                console.info(data);

            });
        }
    });
}

