/**
 * Created by Administrator on 2017/5/2/002.
 */
function userManager(num) {
    $("#real_table").html("");
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/companies/admin/search-page?pageNo="+1
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
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/companies/admin/search-page?pageNo=' + num
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        $.each(dataObj.items,function (i,val) {
            var deleted = "";
            if(val.deleted==false){
                deleted = "使用中"
            }else{
                deleted = "已禁用"
            }
            var str = "<tr><td class='tit0'>"+i+"</td>"
             str+="<td class='tit_ans'>"+val.createdBy+"</td>"
             str+="<td class='tit_name'>"+val.name+"</td>"
             str+="<td class='kw'>"+val.source+"</td>"
             str+="<td class='kw'></td>"
             str+="<td class='kw'>"+transDate(val.createdTime)+"</td>"
             str+="<td class='kw'>"+deleted+"</td>"
             str+="<td class='kw'>"+val.chatLogAccount+"</td>"
             str+="<td  class='kw'>"+val.residualFrequency+"</td>"
             str+="<td  class='kw'>"+val.chatLogYesterdayAccount+"</td>"
             str+="<td  class='kw'>"+val.accuracyRate+"</td>"
             str+="<td class='tit_control'><a href='/knowledge/b?company='"+val.id+" class='libraryB'>B库</a><button class='recharge' data-id='"+val.id+"' data-userN='"+val.createdBy+"' data-owner='"+val.name+"' data-residue='"+val.residualFrequency+"'></button></td></tr>"
            $("#real_table").html($("#real_table").html()+str);
            $(".recharge").unbind("click").click(function () {
                $("#wrapEdits").css("display","block");
                var obj = {
                    oId :$(this).attr('data-id'),
                    userN :$(this).attr('data-userN'),
                    owner:$(this).attr('data-owner'),
                    residue: $(this).attr('data-residue'),
                }
                editc(obj)
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

var starttime = ""
var endtime = ""
function giveTime() {     //更改全局变量
    if($("#date1").val()==""&&$("#date2").val()==""){
        layer.msg("请将起止日期填写完整！")
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
        }else{
            layer.msg("起始时间要小于截止时间！")
        }
    }
    console.log(starttime,endtime)
}



//搜索
function B_company(num) {
     $("#real_table").html("")
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var companies_in = $('#companies_in').val();
    var timeOrDian = $('#timeOrDian').val();
    var level = $('#level').val();
    //console.log(timeOrDian)
    var url = "/api/companies/admin/search-page?pageNo=" + 1 + "&keyword=" + companies_in + "&type="+timeOrDian + "&level=" + level + "&starttime=" + starttime + "&endtime=" + endtime
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: url
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
        $("#page").initPage(dataObj.total,num,changePageT);

    }).fail(function (data) {
        console.log(data);
    });
}
function changePageT(data){
    clickChangeT(data)
}
function clickChangeT(num) {
    //$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var companies_in = $('#companies_in').val();
    var timeOrDian = $('#timeOrDian').val();
    var level = $('#level').val();
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/companies/admin/search-page?pageNo=" + 1 + "&keyword=" + companies_in + "&type="+timeOrDian + "&level=" + level + "&starttime=" + starttime + "&endtime=" + endtime
    }).done(function (data) {
         var dataObj = JSON.parse(data);
        $.each(dataObj.items,function (i,val) {
            var deleted = "";
            if(val.deleted==false){
                deleted = "使用中"
            }else{
                deleted = "已禁用"
            }
            var str = "<tr><td class='tit0'>"+i+"</td>"
             str+="<td class='tit_ans'>"+val.createdBy+"</td>"
             str+="<td class='tit_name'>"+val.name+"</td>"
             str+="<td class='kw'>"+val.source+"</td>"
             str+="<td class='kw'></td>"
             str+="<td class='kw'>"+transDate(val.createdTime)+"</td>"
             str+="<td class='kw'>"+deleted+"</td>"
             str+="<td class='kw'>"+val.chatLogAccount+"</td>"
             str+="<td  class='kw'>"+val.residualFrequency+"</td>"
             str+="<td  class='kw'>"+val.chatLogYesterdayAccount+"</td>"
             str+="<td  class='kw'>"+val.accuracyRate+"</td>"
             str+="<td class='tit_control'><a href='/knowledge/b?company='"+val.id+" class='libraryB'>B库</a><button class='recharge' data-id='"+val.id+"' data-userN='"+val.createdBy+"' data-owner='"+val.name+"' data-residue='"+val.residualFrequency+"'></button></td></tr>"
            $("#real_table").html($("#real_table").html()+str);
            $(".recharge").unbind("click").click(function () {
                $("#wrapEdits").css("display","block");
                var obj = {
                    oId :$(this).attr('data-id'),
                    userN :$(this).attr('data-userN'),
                    owner:$(this).attr('data-owner'),
                    residue: $(this).attr('data-residue'),
                }
                editc(obj)
            })

        })
    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
    });
}

$("#li_xiugai").click(function () {
    $(this).css({
        "border-bottom":"1px solid #00C4C2",
        "color":"#00C4C2"
    }).siblings().css({
        "border-bottom":"1px solid white",
        "color":"black"
    })
    $("#xiugai").css("display","block").siblings(".tabLi").css("display","none")
})
$("#li_xiugaiJl").click(function () {
    $(this).css({
        "border-bottom":"1px solid #00C4C2",
        "color":"#00C4C2"
    }).siblings().css({
        "border-bottom":"1px solid white",
        "color":"black"
    })
    $("#xiugaiJl").css("display","block").siblings(".tabLi").css("display","none")
})
$("#li_chongzhi").click(function () {
    $(this).css({
        "border-bottom":"1px solid #00C4C2",
        "color":"#00C4C2"
    }).siblings().css({
        "border-bottom":"1px solid white",
        "color":"black"
    })
    $("#chongzhi").css("display","block").siblings(".tabLi").css("display","none")
})
function closeEditBox() {
    $("#wrapEdits").css("display","none")
}
function editc(obj) {
    var pid = obj.oId;
    //修改部分
    $("#xg_user").val(obj.userN)
    $("#xg_owner").val(obj.owner)
    $("#xg_save").unbind("click").click(function () {
        var xg_obj = {
            id:pid,
            userName: $("#xg_user").val(),
            companyName:$("#xg_owner").val(),
            password:$("#xg_psd").val(),
            reason:$("#xg_why").val(),
        }
        console.log(xg_obj)
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/accounts/update-admin',
            data: JSON.stringify(xg_obj)
        }).done(function (data) {
            layer.msg(data, {icon: 6})
        }).fail(function (data) {
            console.log(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    })

    //修改记录部分
     $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/accountLog/search"
    }).done(function (data) {
         var dataObj = JSON.parse(data);
         console.log(dataObj)
    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
    });

    //充值部分
    $("#cz_user").html(obj.userN)
    $("#cz_owner").html(obj.owner)
    $("#cz_residue").html(obj.residue)
    $("#cz_save").unbind("click").click(function () {
        if($("#cz_why").val()!==""&&$("#cz_count").val()!==""){
            var Recharge = {
                userId: pid,
                lotType: 'DIBI_D',
                amount: $("#cz_count").val(),
                reason: $("#cz_why").val(),
                event:'充值'
            };
            //console.log(Recharge)
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/credit/wallet',
                data: JSON.stringify(Recharge)
            }).done(function (data) {
                layer.msg(data.message, {time:500,icon: 6},function(){});
            }).fail(function (data) {
                console.log(data);
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        }else{
            layer.msg("充值原因和充值点数都要填写哦！", {icon: 5})
        }
    })

}