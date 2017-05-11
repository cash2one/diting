/**
 * Created by Administrator on 2017/5/2/002.
 */
function showDate() {
    if($("#selectques").val()=="微信端"){
        $("#rTj0").css("display","block")
        $("#rTj1").css("display","none")
        wx_show()
    }else if($("#selectques").val()=="web端"){
        $("#rTj0").css("display","none")
        $("#rTj1").css("display","block")
        //web_show()
    }
}
function wx_show() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/wechat/sta_wechat_chat_log'
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        //console.log(dataObj)
        $("#singleDay").html(dataObj.weChatDayRate)
        $("#addUp").html(dataObj.weChatAllRate)
        $("#wxAccount").html(dataObj.weChatAccountCount)
        $("#wd_zl").html(dataObj.weChatAllCount)
        $("#wd_valid").html(dataObj.weChatValidAllCount)
        $("#wd_invalid").html(dataObj.weChatInvalidAllCount)
    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
    });
}
function web_show() {
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: ""
    }).done(function (data) {
        var dataObj = JSON.parse(data);

    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
    });
}
var myChart = echarts.init(document.getElementById('main'));
function initChart(arrX,arrY) {
    option = {
        tooltip: {
            trigger: 'axis'
        },
        toolbox: {
            show: true,
            feature: {
                magicType: {show: true, type: ['stack', 'tiled']},
                saveAsImage: {show: true}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: arrX
        },
        yAxis: {
            type: 'value'
        },
        series: [
        {
            name: '',
            type: 'line',
            smooth: true,
            data: arrY
        }]
    };
  myChart.setOption(option);
}
$("#yesterday").click(function () {
    if($("#selectques").val()=="微信端"){
        chart0("yesterday")
    }else if($("#selectques").val()=="web端"){
        console.log("web")
    }
})
$("#sevenDay").click(function () {
    if($("#selectques").val()=="微信端"){
        chart1("week")
    }else if($("#selectques").val()=="web端"){
        console.log("web")
    }
})
$("#monthDay").click(function () {
    if($("#selectques").val()=="微信端"){
        chart1("month")
    }else if($("#selectques").val()=="web端"){
        console.log("web")
    }
})

function chart0(type) {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/wechat/search-all-count-time?type=' + type
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        var arrTime = [];
        var arrVal = [];
        $.each(dataObj,function (i,val) {
            arrTime.push(val.endTime);
            arrVal.push(val.count)
        })
        initChart(arrTime,arrVal);
    }).fail(function (data) {
        layer.msg(data, {icon: 5})
    });
}
function chart1(type) {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: '/api/wechat/search-all-count-time?type=' + type
    }).done(function (data) {
        var dataObj = JSON.parse(data);
        var arrTime = [];
        var arrVal = [];
        $.each(dataObj,function (i,val) {
            arrTime.push(val.beginTime);
            arrVal.push(val.count)
        })
        initChart(arrTime,arrVal);
    }).fail(function (data) {
        console.log(data);
        layer.msg(data, {icon: 5})
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
            searchByTime(date1Str,date2Str)
        }else{
            layer.msg("起始时间要小于截止时间！")
        }
    }
}
function searchByTime(t1,t2) {
         $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: '/api/wechat/search-all-count-time?starttime='+ t1 +'&endtime=' + t2
        }).done(function (data) {
            var dataObj = JSON.parse(data);
            var arrTime = [];
            var arrVal = [];
            $.each(dataObj,function (i,val) {
                arrTime.push(val.beginTime);
                arrVal.push(val.count)
            })
            initChart(arrTime,arrVal);
        }).fail(function (data) {
            console.log(data);
            layer.msg(data, {icon: 5})
        });
}