/**
 * Created by Administrator on 2017/4/26/026.
 */
function feedback(num) {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/admin/message/searchpage?pageNo=" + num
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        var total = dataObj.total;
        var pagecount;
        if(total%13==0){
            pagecount= parseInt(total/13)
        }else{
            pagecount= parseInt(total/13)+1
        }
        $("#pageCount").html(pagecount)
        $("#page").initPage(dataObj.total,num,changePage);

    }).fail(function (data) {
        console.log(data);
    });
}
function changePage(data){
    clickChange(data)
}
function clickChange(num) {
     $("#real_table").html("")
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/admin/message/searchpage?pageNo=' + num
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        $.each(dataObj.items,function (i,val) {
            var str = "<tr>"
             str+="<td class='tit0'><input type='checkbox'  name='checkbox2' value='"+val.id+"'/></td>"
             str+="<td class='tit_ans'>"+parseInt(i+1)+"</td>"
             str+="<td class='tit_time disUser'>"+val.createdBy+"</td>"
             str+="<td class='changjing disCon'>"+val.suggestion+"</td>"
             str+="<td class='tit_time disTime'>"+transDate(val.createdTime)+"</td>"
             str+="<td class='tit_time disPhone'>"+val.contactInformation+"</td>"
             str+="<td class='tit_control'>"
             str+="<span class='lookOver' data-id='"+val.id+"'feedbackList='"+JSON.stringify(val.feedbackMessageList)+"'>评论</span></td></tr>"
             $("#real_table").html($("#real_table").html()+str);
            $(".lookOver").unbind("click").click(function(){
                var dataId = $(this).attr("data-id");
                $("#messUl").html("")
                var discuss = JSON.parse($(this).attr("feedbackList"));
                $.each(discuss,function (i,val) {
                    var str ="<li><div style='overflow: hidden;margin-top: 10px'>"
                    str+="<lable style='float: left;width: 80px;'>回复内容:</lable>"
                    str+="<p style='float: left;width: 300px;'>"+val.feedback+"</p></div>"
                    str+="<div style='overflow: hidden;margin-top: 10px'>"
                    str+="<lable style='float: left;width: 80px;'>回复人:</lable>"
                    str+="<p style='float: left;width: 300px;'>"+val.adminName+"</p></div></li>"
                    $("#messUl").html($("#messUl").html()+str)

                })
                $("#discuss_user").html($(this).parent().siblings(".disUser").html())
                $("#discuss_content").html($(this).parent().siblings(".disCon").html())
                $("#discuss_phone").html($(this).parent().siblings(".disPhone").html())
                $("#discuss_time").html($(this).parent().siblings(".disTime").html())
                $("#wrapEdits").css("display","block")

                $("#editSave").unbind("click").click(function () {
                    if($("#textF").val()==""){
                        layer.msg("评论不能为空哦！", {time: 1000, icon: 5})
                    }else{
                        var agree = {
				            messageId:dataId,
				            feedback:$("#textF").val()
			            };
                        $.ajax({
					        type: "POST",
					        contentType: "application/json",
					        url: '/api/message/feedback_message/create',
					        data: JSON.stringify(agree)
				        }).done(function (data) {
					        layer.msg(data.message, {time: 500, icon: 6}, function () {
						        window.location.reload(true);
					        })
				        }).fail(function (data) {
					        var message = JSON.parse(data.responseText).message;
					        layer.msg(message, {icon: 5})
				        });
                    }


                })
            })
        })
    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
    });
}
function transDate(hms) {   //毫秒转化指定日期格式
    var date = new Date(hms);
    var year = date.getFullYear();
    var mouth = date.getMonth()+1;
    var day = date.getDate();
    return year+"/"+mouth+"/"+day;
}
$("#allcheckeds").click(function () {
    if (this.checked == true) {
        checkAll('checkbox2');
    } else {
        clearAll('checkbox2');
    }
});
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
$("#alldeletes").click(function () {
    dataupid()
})
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
        }).fail(function (data) {
            // console.info(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });

    }, function () {

    });

}
function closeEditBox() {
    $("#wrapEdits").css("display","none")
}

    $("#link_page").unbind('click').click(function() {
        var num = $("#numPage").val();
        var countpage = $("#pageCount").html()
        if(parseInt(num)>parseInt(countpage)||num==""){
            layer.msg("请输入正确的页码！")
        }else{
            feedback(num)
        }
    })