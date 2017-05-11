/**
 * Created by Administrator on 2017/4/18/018.
 */
var isTime = 0;
var yL;
var starttime = "";
var endtime = "";

function init_ZSK(num,size,initPage) {  //查询页码  页每页条数  分页器初始页码
    giveTime()            //赋值起止时间
    var select;
    if($('#select').val()=="模糊搜索"){
        select=0;
    }else{
        select=1;
    }
    var selectques;
    if($('#selectques').val()=="搜索问题"){
        selectques=0;
    }else{
        selectques=1;
    }
    var navsearchinput = $('#nav-search-input').val();
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $("#real_table").html("")
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/base/knowledge/search-page?pageNo=" + num + '&pageSize=' + size+ "&queryCriteria=" + isTime+ "&queryState=" + select + "&type=" + selectques+ "&keywords=" + navsearchinput + "&starttime=" + starttime + "&endtime=" + endtime
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        var total = dataObj.total;
        var pagecount;
        if(total%15==0){
            pagecount= parseInt(total/15)
        }else{
            pagecount= parseInt(total/15)+1
        }
        $("#pageCount").html(pagecount)
        if(isTime==0){
            $("#page").initPage(dataObj.total,initPage,initShow_order_time);
        }else if(isTime==1){
            $("#page").initPage(dataObj.total,initPage,initShow_order_pinlv);
        }
    }).fail(function (data) {
        console.log(data);
    });

    $("#link_page").unbind('click').click(function() {
        var num = $("#numPage").val();
        var countpage = $("#pageCount").html()
        if(parseInt(num)>parseInt(countpage)||num==""){
            layer.msg("请输入正确的页码！")
        }else{
            init_ZSK(num,15,num)
        }
    })
}
function init_next(num,size) {     //点击页码重新请求
    $("#real_table").html("")
    var allcheckeds = document.getElementById("allcheckeds");
    allcheckeds.checked=false;
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var select;
    if($('#select').val()=="模糊搜索"){
        select=0;
    }else{
        select=1;
    }
    var selectques;
    if($('#selectques').val()=="搜索问题"){
        selectques=0;
    }else{
        selectques=1;
    }
    var navsearchinput = $('#nav-search-input').val();
    $("#real_table").html("")
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/base/knowledge/search-page?pageNo=" + num + '&pageSize=' + size+ "&queryCriteria=" + isTime+ "&queryState=" + select + "&type=" + selectques+ "&keywords=" + navsearchinput + "&starttime=" + starttime + "&endtime=" + endtime
    }).done(function (data) {
        //console.log(isTime)
        var dataObj = JSON.parse(data);
        total = dataObj.total;
        //console.log(dataObj.items)
        $.each(dataObj.items,function (i,val) {
            var str = "<tr><td class='tit0'><input type='checkbox' name='checkbox2' value='"+val.id+"'/></td>"
            str+="<td class='tit_ans ques'>"+val.question+"</td>"
            str+="<td class='tit_ans answ'>"+val.answer+"</td>"
            str+="<td class='kw'>"+val.kw1+"</td>"
            str+="<td class='kw'>"+val.kw2+"</td>"
            str+="<td class='kw'>"+val.kw3+"</td>"
            str+="<td class='kw'>"+val.kw4+"</td>"
            str+="<td class='kw'>"+val.kw5+"</td>"
            str+="<td class='changjing scen'>"+val.scene+"</td>"
            str+="<td class='changjing actionOption'>"+val.actionOption+"</td>"
            str+="<td  class='tit_time'>"+transDate(val.updatedTime)+"</td>"
            str+="<td class='tit_control'>"
            str+="<button class='edit_item'name='edits' data-question='" + val.question + "' data-answer='"+val.answer+"' data-actionoption='"+val.actionOption+"' data-scene='"+val.scene+"' data-value='"+val.id+"' data-img_url='"+val.img_url+"'></button>"
            str+="<button class='dele_item'data-value='"+val.id+"'></button></td></tr>"
            $("#real_table").html($("#real_table").html()+str)
            $(".edit_item").click(function () {          //获取旧数据
                yL=true;
                var obj = {
                    question :$(this).attr('data-question'),
                    answer:$(this).attr('data-answer'),
                    scene : $(this).attr('data-scene'),
                    id:$(this).attr('data-value'),
                    img_url :$(this).attr('data-img_url'),
                    actionOption: $(this).attr('data-actionoption').replace("action_","")
                }
                edits(obj);
            })
            $(".dele_item").click(function () {
                 var knowledgeId = $(this).attr('data-value');
                layer.confirm('您真的确定要删除了吗？', {
                     btn: ['确定', '再考虑一下'] //按钮
                }, function () {
                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: '/api/knowledge/admin/delete/' + knowledgeId
                    }).done(function (data) {
                        layer.msg(data.message, {icon: 6})
                        window.location.reload()
                    }).fail(function (data) {
                        console.log(data);
                        var message = JSON.parse(data.responseText).message;
                        layer.msg(message, {icon: 5})
                    });
                }, function () {

                });
            })
        })
    }).fail(function (data) {
        console.log(data);
    });
}
function transDate(hms) {   //毫秒转化指定日期格式
    var date = new Date(hms);
    var year = date.getFullYear();
    var mouth = date.getMonth()+1;
    var day = date.getDate();
    return year+"/"+mouth+"/"+day;
}
function initShow_order_time(data){   //点击页码回调函数
    init_next(data,15,0)
}

function initShow_order_pinlv(data){   //点击页码回调函数
    init_next(data,15,1)
}


function checkAll(name) {   //全选中
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
$("#allcheckeds").click(function () {
    if (this.checked == true) {
        checkAll('checkbox2');
    } else {
        clearAll('checkbox2');
    }
});
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
            url: '/api/knowledge/admin/batchdelete',
            data: JSON.stringify({'ids': checkedList.toString()})
        }).done(function (data) {
            layer.msg(data.message, {icon: 6})
            window.location.reload()
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    }, function () {

    });
}

$("#alldeletes").click(function () {
    dataupid();
})

$("#high_p").click(function () {
    $("#change_tit").html("答案")
    isTime=1;
    init_ZSK(1,15,1);
})
$("#zskLearn").click(function () {
    $("#change_tit").html("答案")
    isTime=0;
    init_ZSK(1,15,1);
})

function edits(obj){
    $.imageFileVisible({wrapSelector: "#img-yuLan2",   //预览编辑图片
        fileSelector: "#inputImage2",
        width: 100,
        height: 50
    });
    var editId = obj.id;
    //console.log(editId)
    $('#editquestion').val(obj.question);
    $('#editanswer').val(obj.answer);
    $('#editscene').val(obj.scene);

    if(obj.actionOption == "null"){
        $("#action_change").val("0")
    }else {
        $("#action_change").val(obj.actionOption);
    }
    if(obj.img_url != "null"){
        $('#img-yuLan2').attr("src",obj.img_url+'?imageMogr2/thumbnail/150x150')
    }else {
        $('#img-yuLan2').attr("src","/static/images/yuLan.jpg")
    }
    $("#wrapEdits").css("display","block")

    $("#editSave").unbind('click').click(function() {
        addquestion2('inputImage2',editId)
    })
}
function closeEditBox() {
    $("#wrapEdits").css("display","none")
}
function addQ(){
    yL=false;
    $.imageFileVisible({wrapSelector: "#img-yuLan",
        fileSelector: "#inputImage",
        width: 100,
        height: 50
    });
    $("#wrapAddQ").css("display","block")
}
function closeaddQBox() {
    $("#wrapAddQ").css("display","none")
}
//点击保存 上传图片
function addquestion(element) {
    $("#add_load").css("display","block")
    $.ajaxFileUpload({
        type: 'post',
        url: '/upload/' + element, //你处理上传文件的服务端
        secureuri: false, //是否需要安全协议，一般设置为false
        fileElementId: element,//与页面处理代码中file相对应的ID值
        async: false,
        dataType: 'json', //返回数据类型:text，xml，json，html,scritp,jsonp五种
        success: function (data) {
            //console.log(data.url)   // 上图片成功后返回图片地址
            addQuest(data.url)
        },
        error: function (data, status, e){
            layer.msg(data, {icon: 5})
        }
    })

}
function addquestion2(element,id) {
    var sid = id;
    $("#add_load2").css("display","block")
    $.ajaxFileUpload({
        type: 'post',
        url: '/upload/' + element, //你处理上传文件的服务端
        secureuri: false, //是否需要安全协议，一般设置为false
        fileElementId: element,//与页面处理代码中file相对应的ID值
        async: false,
        dataType: 'json', //返回数据类型:text，xml，json，html,scritp,jsonp五种
        success: function (data) {
            editQuest(data.url,sid)
            //console.log(data.url,sid)       //一次
        },
        error: function (data, status, e){
            layer.msg(data, {icon: 5})
        }
    })

}
function addQuest(imgUrl) {
    var question = $("#question").val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    var answer = $("#answer").val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    var addImgUrl = imgUrl;
    var actionOption = "action_" + $("#action_add").val();
    var add_scene = $("#add_scene").val()
    var knows = {
        question:question,
        actionOption:actionOption,
        answer:answer,
        img_url:addImgUrl,
        scene:add_scene
    };
    if(question !="" && answer !="" ){
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/knowledge/admin/save',
            data: JSON.stringify(knows)
        }).done(function (data) {
             $("#add_load").css("display","none")
            layer.msg(data.message, {time:1000,icon: 6},function(){
                closeaddQBox()
            })
        }).fail(function (data) {
            var message=JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    }else {
        layer.msg("问题和答案都是必填项哦！", {icon: 5})
    }
}

function editQuest(imgUrl,xid) {            //被掉了两次
    var newquestion = $('#editquestion').val();
    var newanswer = $('#editanswer').val();
    var newscene = $('#editscene').val();
    var actionOption = "action_"+$("#action_change").val();
    var changeImgUrl =  imgUrl;
    var editknows = {
        question: newquestion,
        actionOption:actionOption,
        answer: newanswer,
        scene: newscene,
        img_url:changeImgUrl,
        id: xid
    };
    if ($('#editanswer').val() != "" && $('#editquestion').val() != "" && $("#action_change").val() != "") {
        upEdit(editknows);
    } else {
        layer.msg("问题和答案还有动作是必填项哦！", {icon: 5})
    }
}

function upEdit(obj) {
    closeEditBox();
    $("#add_load2").css("display","none")
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/admin/knowledge/update',
        data: JSON.stringify(obj)
    }).done(function (data) {
        layer.msg(data.message, {time:500,icon: 6},function(){
            window.location.reload()
            // closeEditBox();
            // $("#add_load2").css("display","none")
        });
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        layer.msg(message, {icon: 5})
    });
}

//预览图片        与上传函数无关
(function($) {
    $.imageFileVisible = function(options) {
        var defaults = {
			wrapSelector: null,
  			fileSelector:  null ,
  			width : '100%',
  			height: 'auto',
  			errorMessage: "不是图片"
 		};
 		var opts = $.extend(defaults, options);
 		$(opts.fileSelector).on("change",function(){
		var file = this.files[0];
		var imageType = /image.*/;
		if (file.type.match(imageType)) {
		var reader = new FileReader();
		reader.onload = function(){
		    if(yL==false){
		        $("#img-yuLan").attr("src",reader.result)
            }else if (yL==true){
		        $("#img-yuLan2").attr("src",reader.result)
            }else {}

		};
		reader.readAsDataURL(file);
		}else{
		    alert(opts.errorMessage);
		}
	});
	};
})($);

//初始化无效问题界面
function csh_invalid() {
    $("#alldeletes").css("display","none")
    var totalx;
    $("#change_tit").html("频次")
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/admin/invalidquestion/searchpage?pageNo=" + 1
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        totalx = dataObj.total;
        console.log(dataObj)
        var pagecount;
        if(total%10==0){
            pagecount= parseInt(totalx/15)
        }else{
            pagecount= parseInt(totalx/15)+1
        }
        $("#pageCount").html(pagecount)

        $("#page").initPage(totalx,1,show_invalid);
    }).fail(function (data) {
        console.log(data);

    });


    $("#link_page").unbind('click').click(function() {
        var num = $("#numPage").val();
        var countpage = $("#pageCount").html()
        if(parseInt(num)>parseInt(countpage)||num==""){
            layer.msg("请输入正确的页码！")
        }else{
            $("#page").initPage(totalx,num,show_invalid);
        }
    })
}
 function show_invalid(data){
    next_invalid(data)
}
function next_invalid(num) {     //点击页码重新请求
    $("#real_table").html("")
    var allcheckeds = document.getElementById("allcheckeds");
    allcheckeds.checked=false;
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    //var navsearchinput = $('#nav-search-input').val();
    $("#real_table").html("")
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/admin/invalidquestion/searchpage?pageNo=" + num
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        total = dataObj.total;
        //console.log(dataObj.items)
        $.each(dataObj.items,function (i,val) {
            var str = "<tr><td class='tit0'><input type='checkbox' name='checkbox2' value='"+val.id+"'/></td>"
            str+="<td class='tit_ans ques'>"+val.question+"</td>"
            str+="<td class='tit_ans answ'>"+val.num+"</td>"
            str+="<td class='kw'></td>"
            str+="<td class='kw'></td>"
            str+="<td class='kw'></td>"
            str+="<td class='kw'></td>"
            str+="<td class='kw'></td>"
            str+="<td class='changjing scen'></td>"
            str+="<td class='changjing actionOption'></td>"
            str+="<td  class='tit_time'></td>"
            str+="<td class='tit_control'></td></tr>"
            $("#real_table").html($("#real_table").html()+str)
        })
    }).fail(function (data) {
        console.log(data);
    });
}

function giveTime() {     //更改全局变量
    if($("#date1").val()==""&&$("#date2").val()==""){
        //layer.msg("请将起止日期填写完整！")
    }else{
        if($("#date1").val()<$("#date2").val()){
            var date1_arr = $("#date1").val().split("-")
            var first_ele = date1_arr.shift();
            date1_arr.push(first_ele)
            var date1Str = date1_arr.join("/")
            var date2_arr = $("#date2").val().split("-");
            first_ele = date2_arr.shift();
            date2_arr.push(first_ele)
            var date2Str = date2_arr.join("/")
            starttime = date1Str;
            endtime = date2Str;
            //console.log(date1Str,date2Str)
        }else{
            layer.msg("起始时间要小于截止时间！")
        }
    }
}

//导出excel
function toExcel(){
     giveTime()            //赋值起止时间
     var select;
    if($('#select').val()=="模糊搜索"){
        select=0;
    }else{
        select=1;
    }
    var selectques;
    if($('#selectques').val()=="搜索问题"){
        selectques=0;
    }else{
        selectques=1;
    }
    var navsearchinput = $('#nav-search-input').val();
    window.location.href='/api/base/exportknowledge?starttime=' + starttime + '&endtime=' + endtime + "&keywords=" + navsearchinput + "&queryState=" + select + "&type=" + selectques
}
