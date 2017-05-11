function addq() { //添加问题弹窗
    layer.open({
        type: 2,
        title: ['添加知识', 'font-size:18px;'],
        //btn: ['保存', '取消'],
        closeBtn: 1,
        shade: 0.3,
        skin: 'demo-class720',
        content: '/add_quesK2'
    });
}

function leading() {  //导入EXCEL弹框
    layer.open({
        type: 2,
        title: ['EXCEL 导入到数据库', 'font-size:18px;'],
        closeBtn: 1,
        shade: 0.3,
        skin: 'demo-class2',
        content: '/leading_quesK2'
    });
}

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
    layer.confirm('确定要删除所选项目？', {
        btn: ['确定', '再考虑一下'] //按钮
    }, function () {
        var checkedList = new Array();
        $("input[name='checkbox2']:checked").each(function () {
            checkedList.push($(this).val());
        });

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/knowledge/batchdelete',
            data: JSON.stringify({'ids': checkedList.toString()})
        }).done(function (data) {
            layer.msg(data.message, {time:500,icon: 6},function(){
                parent.window.location.reload(false);
            })
            //     changess();
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    }, function () {

    });
}
function success() { //知识库返回值成功获取--后执行的函数
    // 编辑弹窗<juning>
    $(".edits").click(function () {
        var knowledgeId = $(this).attr('data-value');
        var question = $(this).attr('data-question').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        var answer = $(this).attr('data-answer').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        var scene = $(this).attr('data-scene');
        var addImgUrl = $(this).attr('data-img_url');
        var actionOption = $(this).attr('data-actionOption').replace("action_","");
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width:45%;min-width: 600px;height: 450px;background: #fff;position: fixed;top: 20%;left: 32%;"> <div style="height: 42px;background: #00c4c2;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">修改知识 <div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <form> <input type="hidden" name="id" id="id" value=""> <div style="padding-top: 13px;"> <table style="font-size: 16px;"> <tbody> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">问题:</span></td> <td> <input type="text"  name="question" required="required" id="editquestion" style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;" title="问题" placeholder="这里输入问题" maxlength="60" value="" /> </td> </tr> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">答案:</span></td> <td> <textarea placeholder="这里输入答案" name="answer" required="required" title="答案" id="editanswer" style="width: 95%; height: 100px; overflow-y: auto; resize: none;border:1px solid #fafafa;line-height: 30px;font-size: 14px;outline: none; margin: 5px 0;" rows="5" cols="37" maxlength="240"></textarea> </td> </tr> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">图片预览:</span></td> <td><div style="margin-left: 5px;">  <div style="width: 280px;height: 76px;"><div style="width: 140px;height: 76px;position: relative;font-size: 14px;line-height: 76px;text-align: center;float: right;">添加图片<input type="file" accept="image/gif,image/jpeg,image/png" id="inputImage" name="file" multiple=""  onchange="uploadImageAdd()" name="file" multiple style=" position: absolute;top: 0; left: 0; bottom: 0;border: 0;padding: 0; margin: 0;  height: 100%; width: 140px;cursor: pointer; border: solid 1px #ddd; opacity: 0;"> </div> <img id="img-yuLan"  src="/static/images/yuLan.jpg" alt="/static/images/yuLan.jpg"style="max-width: 140px;max-height: 76px;"  onclick="answerImgBig(this.alt)"> <div style="display: none;"><input id="addImgUrl" type="text"></div> </div> </div></td> </tr><tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">场景:</span></td> <td> <input type="text" name="scene" id="editscene" required="required" maxlength="20" value="" title="场景" style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;    margin: 5px 0;"> </td> </tr><tr><td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">动作:</span></td> <td><div style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;">action_<input type="text" name="action_change" required="required" id="action_change"  style="width:390px;height: 26px;line-height: 26px;font-size: 14px; outline: none;border:0;    margin: 5px 0;"  placeholder="可以输入动作指令,例如:点头,跳舞" value="0" maxlength="10" /> </td> </tr> <tr> <td style="display: block;"></td> <td class="btns"><a class="btn3" >取消</a><a class="btn2" href="#" >保存</a><a class="btn1" href="#" >重置场景</a></td> </tr> </tbody> </table> </div> </form> </div> </div>'
        $('#tanc').html(str);
        $('#editquestion').val(question);
        $('#editanswer').val(answer);
        $('#addImgUrl').val(addImgUrl);
        if($(this).attr('data-img_url') != "null" && $(this).attr('data-img_url') != "" && $(this).attr('data-img_url') != null){
            $('#img-yuLan').attr("src",addImgUrl+'?imageMogr2/thumbnail/150x150')
            $('#img-yuLan').attr("alt",addImgUrl)
        }else {
            $('#img-yuLan').attr("src","/static/images/yuLan.jpg")
            $('#img-yuLan').attr("alt","/static/images/yuLan.jpg")
        }
        if($(this).attr('data-actionOption') == "null"){
            $("#action_change").val("0")
        }else {
            $("#action_change").val(actionOption);
        }
        $('#editscene').val(scene);
        $(".close_update_c").click(function () {//叉掉
            closeX();
        });
        $('.btn1').click(function () { //重置
            var ques = $('#editquestion').val();
            var editscene = {
                question: ques
            };
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/knowledge/scene',
                data: JSON.stringify(editscene)
            }).done(function (data) {
                var data = JSON.parse(data);
                $('#editscene').val(data.scene);
            }).fail(function (data) {
                console.log(data);
            });
        });
        $('.btn2').click(function () {//保存
            var newquestion = $('#editquestion').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            var newanswer = $('#editanswer').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            var newscene = $('#editscene').val();
            var actionOption = "action_"+$("#action_change").val();
            var changeImgUrl =  $('#addImgUrl').val();
            var editknows = {
                question: newquestion,
                actionOption:actionOption,
                answer: newanswer,
                scene: newscene,
                img_url:changeImgUrl,
                id: knowledgeId
            };
            if ($('#editquestion').val() != "" && $('#editanswer').val() != "" && actionOption !="" && $("#action_change").val() != "") {
                //保存把数据传到后台
                // $("#action_change").val().indexOf("action_")==0
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/api/knowledge/update',
                    data: JSON.stringify(editknows)
                }).done(function (data) {
                    layer.msg(data.message, {time:500,icon: 6},function(){
                        closeX();
                        changess();
                    })
                }).fail(function (data) {
                    console.log(data);
                    var message = JSON.parse(data.responseText).message;
                    layer.msg(message, {icon: 5})
                });
            } else {
                layer.msg("问题和答案还有动作是必填项哦！", {icon: 5})
            }
          
        });
        $('.btn3').click(function () {//取消
            closeX()
        });
        function closeX(){
            $('#tanc').html("")
        }
    });
    //删除这条数据
    $(".deletes").click(function () {
        var knowledgeId = $(this).attr('data-value');
        layer.confirm('您真的确定要删除了吗？', {
            btn: ['确定', '再考虑一下'] //按钮
        }, function () {
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/knowledge/delete/' + knowledgeId

            }).done(function (data) {
                layer.msg(data.message, {icon: 6})
                changess();
                // parent.history.go(0);//取消键刷新页面
            }).fail(function (data) {
                console.log(data);
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });

        }, function () {

        });
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
function csh_knows(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "api/home/yesterday/new_knowledge_num"
    }).done(function (data) {
        var data = JSON.parse(data);
        $('#yesterdayNewKnowledgeNum').html(data.yesterdayNewKnowledgeNum)
    }).fail(function (data) {
        console.log(data);
    });
    //初始化知识库
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
            var n = (i + 1);
            var actionOption = "";
            if(data.items[i].actionOption == null){
                actionOption = "action_0";
            }else {
                actionOption = data.items[i].actionOption;
            }
            str += '<tr ><td align="center" ><input style="margin-left:10px;" type="checkbox" name="checkbox2" value="' + data.items[i].id + '"/></td> <td  style="text-align: center;">' + n + '</td><td >' + data.items[i].question + '</td><td  style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td  style="text-align: center;">' + data.items[i].scene + '</td><td  style="text-align: center;">' + actionOption + '</td><td style="text-align: center;">' + times + '</td><td  style="text-align: center;">' + data.items[i].frequency + '</td><td style="text-align: center;"><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td><td style="text-align: center;"><input title="删除" class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
            $('#tabProduct').html(str)
        }
        if (data.items.length == 0) {
            var strxx = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
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
            var order = $('#order').val();
            var select = $('#select').val();
            var selectques = $('#selectques').val();
            var navsearchinput = encodeURI($('#nav-search-input').val());
            var starttime = $("#startdate").val();
            var endtime = $("#enddate").val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + 1 + "&keywords=" + navsearchinput + "&queryState=" + select + "&queryCriteria=" + order + "&starttime=" + starttime + "&endtime=" + endtime + "&type=" + selectques
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
                for (var i = 0; i < data.items.length; i++) {
                    var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
                    var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
                    var n = ((($("#pageNo").val() - 1) * 15) + (i + 1));
                    var actionOption = "";
                    if(data.items[i].actionOption == null){
                        actionOption = "action_0";
                    }else {
                        actionOption = data.items[i].actionOption;
                    }
                    str += '<tr ><td align="center" ><input style="margin-left:10px;" type="checkbox" name="checkbox2" value="' + data.items[i].id + '"/></td> <td  style="text-align: center;">' + n + '</td><td >' + data.items[i].question + '</td><td  style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td  style="text-align: center;">' + data.items[i].scene + '</td><td  style="text-align: center;">' + actionOption + '</td><td style="text-align: center;">' + times + '</td><td  style="text-align: center;">' + data.items[i].frequency + '</td><td style="text-align: center;"><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td><td style="text-align: center;"><input title="删除" class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';
                    $('#tabProduct').html(str)
                }
                success();
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}



//知识库学习页面的搜索功能
function changess() {
    var order = $('#order').val();
    var select = $('#select').val();
    var selectques = $('#selectques').val();
    var navsearchinput = encodeURI($('#nav-search-input').val())
    $("#pageNo").val(1)
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + 1 + "&keywords=" + navsearchinput + "&queryState=" + select + "&queryCriteria=" + order + "&type=" + selectques
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
            var n = (i + 1);
            var actionOption = "";
            if(data.items[i].actionOption == null){
                actionOption = "action_0";
            }else {
                actionOption = data.items[i].actionOption;
            }
            str += '<tr ><td align="center" ><input style="margin-left:10px;" type="checkbox" name="checkbox2" value="' + data.items[i].id + '"/></td> <td  style="text-align: center;">' + n + '</td><td >' + data.items[i].question + '</td><td  style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td  style="text-align: center;">' + data.items[i].scene + '</td><td  style="text-align: center;">' + actionOption + '</td><td style="text-align: center;">' + times + '</td><td  style="text-align: center;">' + data.items[i].frequency + '</td><td style="text-align: center;"><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td><td  style="text-align: center;"><input title="删除" class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';

        }
        if (data.items.length == 0) {
            var strxx = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
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

//导出excel
function toExcel() {
    var starttime = $("#startdate").val();
    var endtime = $("#enddate").val();
    var select = $('#select').val();
    var selectques = $('#selectques').val();
    var navsearchinput = encodeURI($('#nav-search-input').val());
    window.location.href = '/api/company/exportknowledge?starttime=' + starttime + '&endtime=' + endtime + "&keywords=" + navsearchinput + "&queryState=" + select + "&type=" + selectques
      
}

//按时间搜索
function searchs() {
    var starttime = $("#startdate").val();
    var endtime = $("#enddate").val();

    $("#pageNo").val(1)
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + 1 + "&starttime=" + starttime + "&endtime=" + endtime
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
            var n = (i + 1);
            var actionOption = "";
            if(data.items[i].actionOption == null){
                actionOption = "action_0";
            }else {
                actionOption = data.items[i].actionOption;
            }
            str += '<tr ><td align="center" ><input style="margin-left:10px;" type="checkbox" name="checkbox2" value="' + data.items[i].id + '"/></td> <td  style="text-align: center;">' + n + '</td><td >' + data.items[i].question + '</td><td  style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td  style="text-align: center;">' + data.items[i].scene + '</td><td  style="text-align: center;">' + actionOption + '</td><td style="text-align: center;">' + times + '</td><td  style="text-align: center;">' + data.items[i].frequency + '</td><td style="text-align: center;"><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td><td  style="text-align: center;"><input title="删除" class="deletes" type="button" name="deletes" data-value="' + data.items[i].id + '"/></td></tr>';

        }
        if (data.items.length == 0) {
            var stryy = '<tr> <th style="min-width: 40px;" align="center" Name="checkb"><input style="margin-left:10px ;" type="checkbox" id="allcheckeds" name="checkbox" value="checkbox"  /></th> <th  Name="Num" style="text-align: center;min-width: 50px;">序号</th> <th style="min-width: 150px" Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th style="min-width: 240px"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th> <th Name="Scene" EditType="TextBox" style="text-align: center; min-width: 100px">场景</th> <th  Name="actionOption" EditType="TextBox" style="text-align: center;min-width: 100px">动作</th> <th Name="ChangeTime" style="text-align: center;min-width: 100px">修改时间</th> <th Name="nums" style="text-align: center;min-width: 80px">调用次数</th> <th Name="Handle" style="text-align: center; min-width: 80px">修改</th> <th Name="Handle" style="text-align: center; min-width: 80px">删除</th> </tr>';
            layer.msg("没有搜索到相应数据");
            $('#tabProduct').html(stryy)
        } else {
            $('#tabProduct').html(str)
        }
        success();
    }).fail(function (data) {
        console.log(data);

    });
}

