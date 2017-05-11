

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
            url: '/api/company/invalidquestion/batchdelete?id_str=' + newstart
        }).done(function (data) {
            layer.msg(data.message, {icon: 6})
            window.location.reload(false);
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });

    }, function () {

    });

}


function success() {//无效问题返回值成功获取--后执行的函数

    // 编辑弹窗<juning>
    $(".edits").click(function () {
        var knowledgeId = $(this).attr('data-value');
        var question = $(this).attr('data-question').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width:30%;min-width: 400px;height: 280px;background: #fff;position: fixed;top: 30%;left: 37%;"> <div style="height: 42px;background: #00c4c2;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">无效问题编辑 <div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <form> <input type="hidden" name="id" id="id" value=""> <div style="padding-top: 13px;"> <table style="font-size: 16px;"> <tbody> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">问题:</span></td> <td> <input type="text"  name="question"  readonly="readonly" id="editquestion" style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;" title="问题" placeholder="这里输入问题" maxlength="60" value="" /> </td> </tr> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">答案:</span></td> <td> <textarea placeholder="这里输入答案" name="answer" required="required" title="答案" id="editanswer" style="width:95%; height: 100px; overflow-y: auto; resize: none;border:1px solid #fafafa;line-height: 30px;font-size: 14px;outline: none;    margin: 5px 0;" rows="5" cols="37" maxlength="240"></textarea> </td> </tr>  <tr> <td style="display: block;"></td> <td class="btns"><a class="btn3">取消</a><a class="btn2">保存</a></td> </tr> </tbody> </table> </div> </form> </div> </div>'
        $('#tanc').html(str)
        $('#editquestion').val(question);

        $(".close_update_c").click(function () {//叉掉
            closeXIN()
        })

        $('.btn2').click(function () {//保存
            var newquestion = $('#editquestion').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            var newanswer = $('#editanswer').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            var editknows = {
                question: newquestion,
                answer: newanswer

            };
            if (newquestion != "" && newanswer != "") {
                //保存把数据传到后台
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/api/company/invalidquestion/update',
                    data: JSON.stringify(editknows)
                }).done(function (data) {
                    layer.msg(data.message, {time:500,icon: 6},function () {
                        closeXIN()
                        window.location.reload(false);
                    })
                    // parent.history.go(0);//保存post传出数据后刷新页面
                }).fail(function (data) {
                    console.log(data);
                    var message = JSON.parse(data.responseText).message;
                    layer.msg(message, {icon: 5})
                });
            } else {
                layer.msg("答案写了么？", {icon: 5})
            }

        })
        $('.btn3').click(function () {//取消
            closeXIN()
        })
        function closeXIN() {
            $('#tanc').html("")
        }
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


//初始化无效问题界面
function csh_Inva(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/invalidquestion/searchpage?pageNo=" + 1

    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr><th Name="checkb" style="min-width: 50px;text-align: center;"><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th style="min-width: 100px">序号</th><th  Name="Question" style="min-width: 600px;">问题</th> <th   Name="Frequency" style="text-align: center;min-width: 100px;" >频次</th> <th  Name="Handle" style="text-align: center;min-width: 100px;">添加答案</th> </tr>'
        for (var i = 0; i < data.items.length; i++) {
            str += '<tr> <td style="text-align: center"><input type="checkbox" name="checkbox2" value="' + data.items[i].id + '" /></td><td>'+(i+1)+'</td> <td >' + data.items[i].question + '</td> <td  style="text-align: center;">' + data.items[i].num + '</td> <td  style="text-align: center"> <input title="编辑" class="edit0 edits"  type="button" name="edits" value="" style="background: url(/static/images/diting1.0/pc/icon_add2.png);width: 30px;height: 30px;" data-question="' + data.items[i].question + '" data-value="' + data.items[i].id + '"/> </td> </tr>'
            $('#invalid').html(str)
        }
        if(data.items.length==0){
            var strx = '<tr ><th Name="checkb" style="min-width: 50px;width:5%;text-align: center;"><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th style="min-width: 100px">序号</th><th  Name="Question" style="min-width: 600px;width: 75%">问题</th> <th   Name="Frequency" style="text-align: center;min-width: 100px;width: 10%" >频次</th> <th  Name="Handle" style="text-align: center;min-width: 100px;width: 10%">添加答案</th> </tr>'
            layer.msg("没有搜索到相应数据");
            $('#invalid').html(strx)
        }else {
            $('#invalid').html(str)
        }
        success();

    }).fail(function (data) {
        console.log(data);

    });
}

function invalidSearch() {
    var starttime = $("#startdate").val();
    var endtime = $("#enddate").val();
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/company/invalidquestion/searchpage?pageNo=' + 1 + '&starttime=' + starttime + '&endtime=' + endtime
    }).done(function (data) {
        var data = JSON.parse(data);
      
        Paging_device(data.total);
        var str = '<tr><th Name="checkb" style="min-width: 50px;text-align: center;"><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th style="min-width: 100px">序号</th><th  Name="Question" style="min-width: 600px;">问题</th> <th   Name="Frequency" style="text-align: center;min-width: 100px;" >频次</th> <th  Name="Handle" style="text-align: center;min-width: 100px;">添加答案</th> </tr>'
        for (var i = 0; i < data.items.length; i++) {
            str += '<tr> <td style="text-align: center"><input type="checkbox" name="checkbox2" value="' + data.items[i].id + '" /></td> <td>'+(i+1)+'</td><td >' + data.items[i].question + '</td> <td  style="text-align: center;">' + data.items[i].num + '</td> <td  style="text-align: center"> <input title="编辑" class="edit0 edits"  type="button" name="edits" value="" style="background: url(/static/images/diting1.0/pc/icon_add2.png);width: 30px;height: 30px;"  data-question="' + data.items[i].question + '" data-value="' + data.items[i].id + '"/> </td> </tr>'
        }
        if(data.items.length==0){
            var strx = '<tr><th Name="checkb" style="min-width: 50px;width:5%;text-align: center;"><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th><th style="min-width: 100px">序号</th> <th  Name="Question" style="min-width: 600px;width: 75%">问题</th> <th   Name="Frequency" style="text-align: center;min-width: 100px;width: 10%" >频次</th> <th  Name="Handle" style="text-align: center;min-width: 100px;width: 10%">添加答案</th> </tr>'
            layer.msg("没有搜索到相应数据");
            $('#invalid').html(strx)
        }else {
            $('#invalid').html(str)
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
            //页面转换后知识内容加载
            var starttime = $("#startdate").val();
            var endtime = $("#enddate").val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: '/api/company/invalidquestion/searchpage?pageNo=' + $("#pageNo").val() + '&starttime=' + starttime + '&endtime=' + endtime
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr><th Name="checkb" style="min-width: 50px;text-align: center;"><input type="checkbox" id="allcheckeds" name="checkbox" value="checkbox" /></th> <th style="min-width: 100px">序号</th><th  Name="Question" style="min-width: 600px;">问题</th> <th   Name="Frequency" style="text-align: center;min-width: 100px;" >频次</th> <th  Name="Handle" style="text-align: center;min-width: 100px;">添加答案</th> </tr>'
                for (var i = 0; i < data.items.length; i++) {
                    str += '<tr> <td style="text-align: center"><input type="checkbox" name="checkbox2" value="' + data.items[i].id + '" /></td> <td>'+(i+1)+'</td> <td >' + data.items[i].question + '</td> <td  style="text-align: center;">' + data.items[i].num + '</td> <td  style="text-align: center"> <input title="编辑" class="edit0 edits"  type="button" name="edits" value="" style="background: url(/static/images/diting1.0/pc/icon_add2.png);width: 30px;height: 30px;"  data-question="' + data.items[i].question + '" data-value="' + data.items[i].id + '"/> </td> </tr>'
                    $('#invalid').html(str)
                }
                success();
            }).fail(function (data) {
                console.log(data);

            });
        }
    });
}

