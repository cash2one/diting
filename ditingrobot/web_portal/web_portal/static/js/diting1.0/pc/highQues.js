function success2() { //知识库返回值成功获取--后执行的函数
    // 修改弹窗<juning>
    $(".edits").click(function () {
        var knowledgeId = $(this).attr('data-value');
        var question = $(this).attr('data-question').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        var answer = $(this).attr('data-answer').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        var scene = $(this).attr('data-scene');
        var addImgUrl = $(this).attr('data-img_url');
        var actionOption = $(this).attr('data-actionOption').replace("action_","");
        var str = "";
        str += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div style="width:45%;min-width: 600px;height: 450px;background: #fff;position: fixed;top: 20%;left: 32%;"> <div style="height: 42px;background: #00c4c2;color:#fff;font-size: 18px;padding: 0 10px 0 20px;line-height: 42px;">修改知识 <div class="close_update_c" style="height: 29px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;"></div> </div> <form> <input type="hidden" name="id" id="id" value=""> <div style="padding-top: 13px;"> <table style="font-size: 16px;"> <tbody> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">问题:</span></td> <td> <input type="text"  name="question" required="required" id="editquestion" style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;" title="问题" placeholder="这里输入问题" maxlength="60" value="" /> </td> </tr> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">答案:</span></td> <td> <textarea placeholder="这里输入答案" name="answer" required="required" title="答案" id="editanswer" style="width: 95%; height: 100px; overflow-y: auto; resize: none;border:1px solid #fafafa;line-height: 30px;font-size: 14px;outline: none;    margin: 5px 0;" rows="5" cols="37" maxlength="240"></textarea> </td> </tr> <tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">图片预览:</span></td> <td><div style="margin-left: 5px;">  <div style="width: 280px;height: 76px;"><div style="width: 140px;height: 76px;position: relative;font-size: 14px;line-height: 76px;text-align: center;float: right;">添加图片<input type="file" accept="image/gif,image/jpeg,image/png" id="inputImage" name="file" multiple=""  onchange="uploadImageAdd()" name="file" multiple style=" position: absolute;top: 0; left: 0; bottom: 0;border: 0;padding: 0; margin: 0;  height: 100%; width: 140px;cursor: pointer; border: solid 1px #ddd; opacity: 0;"> </div> <img id="img-yuLan"  src="/static/images/yuLan.jpg" alt="/static/images/yuLan.jpg"style="max-width: 140px;max-height: 76px;"  onclick="answerImgBig(this.alt)"> <div style="display: none;"><input id="addImgUrl" type="text"></div> </div> </div></td> </tr><tr> <td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">场景:</span></td> <td> <input type="text" name="scene" id="editscene" required="required" maxlength="20" value="" title="场景" style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;    margin: 5px 0;"> </td> </tr><tr><td><span style="width:80px;text-align: center;padding-top: 1px;display: block;">动作:</span></td> <td><div style="width:95%;border:1px solid #fafafa;height: 40px;line-height: 40px;font-size: 14px;outline: none;">action_<input type="text" name="action_change" required="required" id="action_change"  style="width:390px;height: 26px;line-height: 26px;font-size: 14px; outline: none;border:0;    margin: 5px 0;"  placeholder="可以输入动作指令,例如:点头,跳舞" value="0" maxlength="10" /> </td> </tr> <tr> <td style="display: block;"></td> <td class="btns"><a class="btn3" >取消</a><a class="btn2" href="#" >保存</a><a class="btn1" href="#" >重置场景</a></td> </tr> </tbody> </table> </div> </form> </div> </div>'
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


}
function csh_highQues(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    //初始化知识库
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + 1 +"&queryCriteria=" + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th style="min-width: 80px;" Name="Num">序号</th> <th style="min-width:180px;"   Name="Question" EditType="TextBox" maxlength="60">用户提问</th> <th style="min-width: 220px"  Name="Answer" EditType="TextBox" maxlength="240">机器人回复</th> <th style="min-width: 120px;" Name="Scene" EditType="TextBox">场景</th> <th style="min-width: 120px"  Name="actionOption" EditType="TextBox">动作</th> <th style="min-width: 100px" Name="nums">次数</th> <th style="min-width: 100px;">修改</th></tr>';
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
            str += '<tr style="border-bottom:1px solid #fafafa ;"> <td >' + n + '</td><td >' + data.items[i].question + '</td><td style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td>' + data.items[i].scene + '</td><td>' + actionOption + '</td><td>' + data.items[i].frequency + '</td><td><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td></tr>';
            $('#highQues').html(str)
        }
        if (data.items.length == 0) {
            var strxx = '<tr> <th style="min-width: 80px;" Name="Num">序号</th> <th style="min-width:180px;"   Name="Question" EditType="TextBox" maxlength="60">用户提问</th> <th style="min-width: 220px"  Name="Answer" EditType="TextBox" maxlength="240">机器人回复</th> <th style="min-width: 120px;" Name="Scene" EditType="TextBox">场景</th> <th style="min-width: 120px"  Name="actionOption" EditType="TextBox">动作</th> <th style="min-width: 100px" Name="nums">次数</th> <th style="min-width: 100px;">修改</th></tr>';
            layer.msg("没有搜索到相应数据");
            $('#highQues').html(strxx)
        } else {
            $('#highQues').html(str)
        }
        success2()
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
        datacontainer: 'highQues',
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
                url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + 1 +"&queryCriteria=" + 1 + "&starttime=" + starttime + "&endtime=" + endtime 
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
console.log(data);
                var str = '<tr> <th style="min-width: 80px;" Name="Num">序号</th> <th style="min-width:180px;"   Name="Question" EditType="TextBox" maxlength="60">用户提问</th> <th style="min-width: 220px"  Name="Answer" EditType="TextBox" maxlength="240">机器人回复</th> <th style="min-width: 120px;" Name="Scene" EditType="TextBox">场景</th> <th style="min-width: 120px"  Name="actionOption" EditType="TextBox">动作</th> <th style="min-width: 100px" Name="nums">次数</th> <th style="min-width: 100px;">修改</th></tr>';
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
                    str += '<tr style="border-bottom:1px solid #fafafa ;"><td>' + n + '</td><td >' + data.items[i].question + '</td><td style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td >' + data.items[i].scene + '</td><td >' + actionOption + '</td><td>' + data.items[i].frequency + '</td><td><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td></tr>';
                    $('#highQues').html(str)
                }
                success2()
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}


//按时间搜索
function searchs() {
    var starttime = $("#startdate").val();
    var endtime = $("#enddate").val();

    $("#pageNo").val(1)
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + 1 + "&starttime=" + starttime + "&endtime=" + endtime +"&queryCriteria=" + 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr> <th style="min-width: 100px;" Name="Num">序号</th> <th style="min-width:240px;"   Name="Question" EditType="TextBox" maxlength="60">用户提问</th> <th style="min-width: 320px"  Name="Answer" EditType="TextBox" maxlength="240">机器人回复</th> <th style="min-width: 120px;" Name="Scene" EditType="TextBox">场景</th> <th style="min-width: 120px"  Name="actionOption" EditType="TextBox">动作</th> <th style="min-width: 100px" Name="nums">次数</th> <th style="min-width: 100px;">修改</th></tr>';
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
            str += '<tr style="border-bottom:1px solid #fafafa ;"><td>' + n + '</td><td >' + data.items[i].question + '</td><td style="max-height: 50px;display: block;">' + data.items[i].answer + '</td><td >' + data.items[i].scene + '</td><td >' + actionOption + '</td><td>' + data.items[i].frequency + '</td><td><input title="修改" class="edits"  type="button" name="edits" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '"  data-img_url="'+ data.items[i].img_url +'"/></td></tr>';
        }
        if (data.items.length == 0) {
            var stryy = '<tr> <th style="min-width: 80px;" Name="Num">序号</th> <th style="min-width:180px;"   Name="Question" EditType="TextBox" maxlength="60">用户提问</th> <th style="min-width: 220px"  Name="Answer" EditType="TextBox" maxlength="240">机器人回复</th> <th style="min-width: 120px;" Name="Scene" EditType="TextBox">场景</th> <th style="min-width: 120px"  Name="actionOption" EditType="TextBox">动作</th> <th style="min-width: 100px" Name="nums">次数</th> <th style="min-width: 100px;">修改</th></tr>';
            layer.msg("没有搜索到相应数据");
            $('#highQues').html(stryy)
        } else {
            $('#highQues').html(str)
        }
        success2()
    }).fail(function (data) {
        console.log(data);

    });
}

