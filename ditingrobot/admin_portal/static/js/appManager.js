/**
 * Created by Administrator on 2017/4/27/027.
 */
function appManager(num) {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/apis/search-page?pageNo="+ num
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
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/apis/search-page?pageNo=' + num
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        console.log(dataObj)
        $.each(dataObj.items,function (i,val) {
            var color;
            if(val.approvalName=="审批通过"){
                color="green";
            }else{
                color="red";
            }
            var str = "<tr><td class='tit_ans'>"+val.id+"</td>"
            str+="<td class='tit_name'>"+val.name+"</td>"
            str+="<td  class='changjing'>"+val.providerName+"</td>"
            str+="<td  class='tit_adr'>"+val.url+"</td>"
            str+="<td  class='tit_time'>"+val.scene+"</td>"
            str+="<td  class='tit_time checkItem' style='color:#00C4C2;cursor: pointer;' app-companyname='"+val.serverName+"' app-phone='"+val.mobile+"' app-email='"+val.email+"' app-name='"+val.name+"' app-scene='"+val.scene+"' app-descript='"+val.description+"' app-api='"+val.name+"' app-version='"+val.version+"' app-url='"+val.url+"' app-post='"+val.method+"'>查看</td>"
            str+="<td  class='tit_time' style='color: "+color+"'>"+val.approvalName+"</td>"
            str+="<td class='tit_time'><button class='appControl'data-id='"+val.id+"'></button></td></tr>"
            $("#real_table").html($("#real_table").html()+str);
            
            $(".checkItem").unbind("click").click(function () {
                $("#app_companyName").html($(this).attr("app-companyname"))
                $("#app_phone").html($(this).attr("app-phone"))
                $("#app_email").html($(this).attr("app-email"))
                $("#app_name").html($(this).attr("app-name"))
                $("#app_scene").html($(this).attr("app-scene"))
                $("#app_descript").html($(this).attr("app-descript"))
                $("#app_api").html($(this).attr("app-name"))
                $("#app_version").html($(this).attr("app-version"))
                $("#app_url").html($(this).attr("app-url"))
                $("#app_post").html($(this).attr("app-post"))
                $("#wrapEdits").css("display","block")
            })
             $(".appControl").click(function () {
                var apiStoreId = $(this).attr('data-id');
                layer.confirm('您真的确定要删除了吗？', {
                    btn: ['确定', '再考虑一下'] //按钮
                }, function () {
                    $.ajax({
                        type: "POST",
                        contentType: "application/json",
                        url: '/api/apis/delete/' + apiStoreId
                    }).done(function (data) {
                        layer.msg(data.message, {time:500,icon: 6},function(){
                           window.location.reload(false);
                        })
                    }).fail(function (data) {
                        var message = JSON.parse(data.responseText).message;
                        layer.msg(message, {icon: 5})
                    });
                }, function () {});
            })
        })
    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
    });
}
 $("#link_page").unbind('click').click(function() {
        var num = $("#numPage").val();
        var countpage = $("#pageCount").html()
        if(parseInt(num)>parseInt(countpage)||num==""){
            layer.msg("请输入正确的页码！")
        }else{
            appManager(num)
        }
    })

function closeEditBox() {
    $("#wrapEdits").css("display","none")
}
