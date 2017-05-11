/**
 * Created by Administrator on 2017/4/25/025.
 */
function agency(num) {            //初始化页码
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var keyword = $("#nav-search-input").val()
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: 'api/agents/search-page?pageNo=' + num + '&keywords=' + keyword
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
    var keyword = $("#nav-search-input").val()
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: 'api/agents/search-page?pageNo=' + num + '&keywords=' + keyword
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        console.log(dataObj)
        $.each(dataObj.items,function (i,val) {
            var str = "<tr>"
             str+="<td class='tit0'><input type='checkbox'  name='checkbox2' value='checkbox'/></td>"
             str+="<td class='tit_ans'>"+parseInt(i+1)+"</td>"
             str+="<td id='change_tit' class='changjing'>"+val.mobile+"</td>"
             str+="<td  class='changjing'>"+val.password+"</td>"
             str+="<td  class='changjing'>"+val.invitationCode+"</td>"
             str+="<td  class='tit_time'>"+transDate(val.updatedTime)+"</td>"
             str+="<td class='tit_control'>"
             str+="<button class='edit_item'name='edits' data-question='" + val.question + "' data-answer='"+val.answer+"' data-actionoption='"+val.actionOption+"' data-scene='"+val.scene+"' data-value='"+val.id+"' data-img_url='"+val.img_url+"'></button>"
             str+="<button class='dele_item'data-value='"+val.id+"'></button></td></tr>"
             $("#real_table").html($("#real_table").html()+str)
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
function addQ(){
    $("#wrapAddQ").css("display","block")
}
function closeQ() {
    $("#wrapAddQ").css("display","none")
}
function addagency(){
    var admin_account = $('#admin_account').val();
    var admin_pwd = $('#admin_pwd').val();
    var admin_phone = $('#admin_phone').val();
    var editknows = {
        userName: admin_account,
        password: admin_pwd,
        mobile: admin_phone
    };
    if (admin_account != "" && admin_pwd != "" && admin_phone != "") {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/agents/register',
            data: JSON.stringify(editknows)
        }).done(function (data) {
            layer.msg(data.message, {time:500,icon: 6},function(){
                closeQ()
            });
        }).fail(function (data) {
            console.log(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    } else {
        layer.msg("请填写完整！", {icon: 5})
    }
}