
//页面初始化==========================================
function cshVisitIndex() {
    // 头部数据===========================================================
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "api/statistical-data/all"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        $('#DataRAll').html(data.numberOfVisitors);
        $('#QuesAll').html(data.questionNumber);

    }).fail(function (data) {
        console.log(data);
    });
    //访客数据24==================================================================
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/chat/record/sta/search-uuid-count/type?type=yesterday"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
     //折线图数据
        timeList24 =  [];
        guestCountList24 =  [];
        suestionStatisticsCountList24 =  [];

        for (var j = 0; j < data.length; j++) {
            timeList24.push(data[j].endTime);
            guestCountList24.push(data[j].guestCount);
            suestionStatisticsCountList24.push(data[j].suestionStatisticsCount);
        }
        visit24()
        ques24()
    //表数据
        var str = '<tr> <th style="min-width: 100px;" Name="Num">序号</th> <th style="min-width:240px;">时间</th> <th style="min-width: 320px" >访问人数</th> <th style="min-width: 120px;" >问答数量</th> </tr>';
        for (var i = 0; i < data.length; i++) {
            str += '<tr style="border-bottom:1px solid #fafafa ;"> <td >' +(i+1) + '</td><td >' + data[i].endTime + '</td><td>' + data[i].guestCount + '</td><td>' + data[i].suestionStatisticsCount + '</td></tr>';
        }
            $('#dataBiao1').html(str)

    }).fail(function (data) {
        console.log(data);
    });
    //访客数据7==================================================================
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/chat/record/sta/search-uuid-count/type?type=week"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        timeList7 =  [];
        guestCountList7 =  [];
        suestionStatisticsCountList7 =  [];

        for (var j = 0; j < data.length; j++) {
            timeList7.push(data[j].beginTime);
            guestCountList7.push(data[j].guestCount);
            suestionStatisticsCountList7.push(data[j].suestionStatisticsCount);
        }
     //表数据
        var str = '<tr> <th style="min-width: 100px;" Name="Num">序号</th> <th style="min-width:240px;">时间</th> <th style="min-width: 320px" >访问人数</th> <th style="min-width: 120px;" >问答数量</th> </tr>';
        for (var i = 0; i < data.length; i++) {
            str += '<tr style="border-bottom:1px solid #fafafa ;"> <td >' +(i+1) + '</td><td >' + data[i].beginTime + '</td><td>' + data[i].guestCount + '</td><td>' + data[i].suestionStatisticsCount + '</td></tr>';
        }
        $('#dataBiao2').html(str)
    }).fail(function (data) {
        console.log(data);
    });
    //访客数据30==================================================================
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/chat/record/sta/search-uuid-count/type?type=month"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        timeList30 =  [];
        guestCountList30 =  [];
        suestionStatisticsCountList30 =  [];

        for (var j = 0; j < data.length; j++) {
            timeList30.push(data[j].beginTime);
            guestCountList30.push(data[j].guestCount);
            suestionStatisticsCountList30.push(data[j].suestionStatisticsCount);
        }
     //表数据
        var str = '<tr> <th style="min-width: 100px;" Name="Num">序号</th> <th style="min-width:240px;">时间</th> <th style="min-width: 320px" >访问人数</th> <th style="min-width: 120px;" >问答数量</th> </tr>';
        for (var i = 0; i < data.length; i++) {
            str += '<tr style="border-bottom:1px solid #fafafa ;"> <td >' +(i+1) + '</td><td >' + data[i].beginTime + '</td><td>' + data[i].guestCount + '</td><td>' + data[i].suestionStatisticsCount + '</td></tr>';
        }
        $('#dataBiao3').html(str)
    }).fail(function (data) {
        console.log(data);
    });
}
//时间段搜索“1”访客数据折线图
function searchT1(){

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/chat/record/sta/search-uuid-count/type?type="+"&beginTime="+ $('#startdate1').val() +"&endTime="+ $('#enddate1').val()
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
       var timeListT1 =  [];
       var guestCountListT1 =  [];
       var suestionStatisticsCountListT1 =  [];

        for (var j = 0; j < data.length; j++) {
            timeListT1.push(data[j].beginTime);
            guestCountListT1.push(data[j].guestCount);
            suestionStatisticsCountListT1.push(data[j].suestionStatisticsCount);
        }
        // 指定图表的配置项和数据

        var optionT1 = {
            backgroundColor: 'rgba(255,255,255,1)',
            legend: {
                data:['访客数据图']
            },
            toolbox: {
                show : true,
                feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                    magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                    restore : {show: true},//还原
                    saveAsImage : {show: true}//保存截图
                }
            },
            calculable : true,
            tooltip : {
                trigger: 'axis',
                formatter: "日期 : {b}<br/>访客 : {c}人"
            },
            xAxis :[
                {
                    type : 'category',
                    axisLine : {onZero: false},
                    axisLabel : {
                        formatter: '{value} '
                    },
                    boundaryGap : false,
                    data : timeListT1


                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value} 人'
                    }
                }
            ] ,
            series : [
                {
                    name:'访客数据图',
                    type:'line',
                    smooth:true,
                    itemStyle: {
                        normal: {
                            lineStyle: {
                                shadowColor : 'rgba(0,0,0,0.4)'
                            }
                        }
                    },
                    data: guestCountListT1
                }
            ]
        };
        var myChartT1 = echarts.init(document.getElementById('TU1'));
        myChartT1.setOption(optionT1);
        $('#Jday1').css('color','#000')
        $('#Jweek1').css('color','#000')
        $('#Jmonth1').css('color','#000')
    }).fail(function (data) {
        console.log(data);
    });
}
//时间段搜索“2”问答数据折线图
function searchT2(){

    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/chat/record/sta/search-uuid-count/type?type="+"&beginTime="+ $('#startdate2').val() +"&endTime="+ $('#enddate2').val()
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        var timeListT2 =  [];
        var guestCountListT2 =  [];
        var suestionStatisticsCountListT2 =  [];

        for (var j = 0; j < data.length; j++) {
            timeListT2.push(data[j].beginTime);
            guestCountListT2.push(data[j].guestCount);
            suestionStatisticsCountListT2.push(data[j].suestionStatisticsCount);
        }
        // 指定图表的配置项和数据

        var optionT2 = {
            backgroundColor: 'rgba(255,255,255,1)',
            legend: {
                data:['问答数据图']
            },
            toolbox: {
                show : true,
                feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                    magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                    restore : {show: true},//还原
                    saveAsImage : {show: true}//保存截图
                }
            },
            calculable : true,
            tooltip : {
                trigger: 'axis',
                formatter: "日期 : {b}<br/>问答 : {c}条"
            },
            xAxis :[
                {
                    type : 'category',
                    axisLine : {onZero: false},
                    axisLabel : {
                        formatter: '{value} '
                    },
                    boundaryGap : false,
                    data : timeListT2


                }
            ],
            yAxis : [
                {
                    type : 'value',
                    axisLabel : {
                        formatter: '{value} 人'
                    }
                }
            ] ,
            series : [
                {
                    name:'问答数据图',
                    type:'line',
                    smooth:true,
                    itemStyle: {
                        normal: {
                            lineStyle: {
                                shadowColor : 'rgba(0,0,0,0.4)'
                            }
                        }
                    },
                    data: suestionStatisticsCountListT2
                }
            ]
        };
        var myChartT2 = echarts.init(document.getElementById('TU2'));
        myChartT2.setOption(optionT2);
        $('#Jday2').css('color','#000')
        $('#Jweek2').css('color','#000')
        $('#Jmonth2').css('color','#000')
    }).fail(function (data) {
        console.log(data);
    });
}
//时间段搜索“3”数据表
function searchB3(){
    $('#Jday3').css('color','#000')
    $('#Jweek3').css('color','#000')
    $('#Jmonth3').css('color','#000')
    $('#dataBiao1').css('display','none')
    $('#dataBiao2').css('display','none')
    $('#dataBiao3').css('display','none')
    $('#dataBiao4').css('display','table')
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/chat/record/sta/search-uuid-count/type?type="+"&beginTime="+ $('#startdate3').val() +"&endTime="+ $('#enddate3').val()
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        //表数据
        var str = '<tr> <th style="min-width: 100px;" Name="Num">序号</th> <th style="min-width:240px;">时间</th> <th style="min-width: 320px" >访问人数</th> <th style="min-width: 120px;" >问答数量</th> </tr>';
        for (var i = 0; i < data.length; i++) {
            str += '<tr style="border-bottom:1px solid #fafafa ;"> <td >' +(i+1) + '</td><td >' + data[i].beginTime + '</td><td>' + data[i].guestCount + '</td><td>' + data[i].suestionStatisticsCount + '</td></tr>';
        }
        if(data.length=0){
            layer.msg("没有搜索到相应数据");
        }
        $('#dataBiao4').html(str)
    }).fail(function (data) {
        console.log(data);
    });
}
//数据表按键=======================================================
function dataBiao() {
    $('#talkData0').css('background', '#00c4c1')
    $('#visitData0').css('background', '#ccc')
    $('#dataBiao').css('display','block')
    $('#dataT').css('display','none')
}
//数据图按键============================================================
function dataT() {
    $('#visitData0').css('background', '#00c4c1')
    $('#talkData0').css('background', '#ccc')
    $('#dataT').css('display','block')
    $('#dataBiao').css('display','none')
}
//数据表切换============================================
function quesB24(){
    $('#Jday3').css('color','#00c4c1')
    $('#Jweek3').css('color','#000')
    $('#Jmonth3').css('color','#000')
    $('#dataBiao1').css('display','table')
    $('#dataBiao2').css('display','none')
    $('#dataBiao3').css('display','none')
    $('#dataBiao4').css('display','none')
}
function quesB7(){
    $('#Jday3').css('color','#000')
    $('#Jweek3').css('color','#00c4c1')
    $('#Jmonth3').css('color','#000')
    $('#dataBiao1').css('display','none')
    $('#dataBiao2').css('display','table')
    $('#dataBiao3').css('display','none')
    $('#dataBiao4').css('display','none')
}
function quesB30(){
    $('#Jday3').css('color','#000')
    $('#Jweek3').css('color','#000')
    $('#Jmonth3').css('color','#00c4c1')
    $('#dataBiao1').css('display','none')
    $('#dataBiao2').css('display','none')
    $('#dataBiao3').css('display','table')
    $('#dataBiao4').css('display','none')
}

//昨日访问数据折线图==========================================================
function visit24(){
    $('#Jday1').css('color','#00c4c1')
    $('#Jweek1').css('color','#000')
    $('#Jmonth1').css('color','#000')

    // 指定图表的配置项和数据

    var option1 = {
        backgroundColor: 'rgba(255,255,255,1)',
        legend: {
               data:['昨日访客数据图']
        },
        toolbox: {
            show : true,
            feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                restore : {show: true},//还原
                saveAsImage : {show: true}//保存截图
            }
        },
        calculable : true,
        tooltip : {
            trigger: 'axis',
            formatter: "日期 : {b}<br/>访客 : {c}人"
        },
        xAxis :[
            {
                type : 'category',
                axisLine : {onZero: false},
                axisLabel : {
                    formatter: '{value} '
                },
                boundaryGap : false,
                data : timeList24


            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 人'
                }
            }
        ] ,
        series : [
            {
                name:'昨日访客数据图',
                type:'line',
                smooth:true,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            shadowColor : 'rgba(0,0,0,0.4)'
                        }
                    }
                },
                data: guestCountList24
            }
        ]
    };
    var myChart1 = echarts.init(document.getElementById('TU1'));
    myChart1.setOption(option1);

}
//昨日问答数据折线图===========================================================
function ques24(){
    $('#Jday2').css('color','#00c4c1')
    $('#Jweek2').css('color','#000')
    $('#Jmonth2').css('color','#000')
    // 指定图表的配置项和数据

    var option2 = {
        backgroundColor: 'rgba(255,255,255,1)',
        legend: {
               data:['昨日问答数据图']
        },
        toolbox: {
            show : true,
            feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                restore : {show: true},//还原
                saveAsImage : {show: true}//保存截图
            }
        },
        calculable : true,
        tooltip : {
            trigger: 'axis',
            formatter: "日期 : {b}<br/>问答 : {c} 条"
        },
        xAxis :[
            {
                type : 'category',
                axisLine : {onZero: false},
                axisLabel : {
                    formatter: '{value} '
                },
                boundaryGap : false,
                data : timeList24
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 条'
                }
            }
        ] ,
        series : [
            {
                name:'昨日问答数据图',
                type:'line',
                smooth:true,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            shadowColor : 'rgba(0,0,0,0.4)'
                        }
                    }
                },
                data: suestionStatisticsCountList24
            }
        ]
    };
    var myChart2 = echarts.init(document.getElementById('TU2'));
    myChart2.setOption(option2);

}
//近一周访问数据折线图=======================================================
function visit7(){
    $('#Jday1').css('color','#000')
    $('#Jweek1').css('color','#00c4c1')
    $('#Jmonth1').css('color','#000')

    // 指定图表的配置项和数据

    var option1 = {
        backgroundColor: 'rgba(255,255,255,1)',
        legend: {
               data:['近一周访客数据图']
        },
        toolbox: {
            show : true,
            feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                restore : {show: true},//还原
                saveAsImage : {show: true}//保存截图
            }
        },
        calculable : true,
        tooltip : {
            trigger: 'axis',
            formatter: "日期 : {b}<br/>访客 : {c}人"
        },
        xAxis :[
            {
                type : 'category',
                axisLine : {onZero: false},
                axisLabel : {
                    formatter: '{value} '
                },
                boundaryGap : false,
                data : timeList7


            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 人'
                }
            }
        ] ,
        series : [
            {
                name:'近一周访客数据图',
                type:'line',
                smooth:true,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            shadowColor : 'rgba(0,0,0,0.4)'
                        }
                    }
                },
                data: guestCountList7
            }
        ]
    };
    var myChart1 = echarts.init(document.getElementById('TU1'));
    myChart1.setOption(option1);

}
//近一周问答数据折线图======================================================
function ques7(){
    $('#Jday2').css('color','#000')
    $('#Jweek2').css('color','#00c4c1')
    $('#Jmonth2').css('color','#000')
    // 指定图表的配置项和数据

    var option2 = {
        backgroundColor: 'rgba(255,255,255,1)',
        legend: {
               data:['近一周问答数据图']
        },
        toolbox: {
            show : true,
            feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                restore : {show: true},//还原
                saveAsImage : {show: true}//保存截图
            }
        },
        calculable : true,
        tooltip : {
            trigger: 'axis',
            formatter: "日期 : {b}<br/>问答 : {c} 条"
        },
        xAxis :[
            {
                type : 'category',
                axisLine : {onZero: false},
                axisLabel : {
                    formatter: '{value} '
                },
                boundaryGap : false,
                data : timeList7
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 条'
                }
            }
        ] ,
        series : [
            {
                name:'近一周问答数据图',
                type:'line',
                smooth:true,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            shadowColor : 'rgba(0,0,0,0.4)'
                        }
                    }
                },
                data: suestionStatisticsCountList7
            }
        ]
    };
    var myChart2 = echarts.init(document.getElementById('TU2'));
    myChart2.setOption(option2);

}
//近月访问数据折线图=======================================================
function visit30(){
    $('#Jday1').css('color','#000')
    $('#Jweek1').css('color','#000')
    $('#Jmonth1').css('color','#00c4c1')

    // 指定图表的配置项和数据

    var option1 = {
        backgroundColor: 'rgba(255,255,255,1)',
        legend: {
               data:['近一月访客数据图']
        },
        toolbox: {
            show : true,
            feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                restore : {show: true},//还原
                saveAsImage : {show: true}//保存截图
            }
        },
        calculable : true,
        tooltip : {
            trigger: 'axis',
            formatter: "日期 : {b}<br/>访客 : {c}人"
        },
        xAxis :[
            {
                type : 'category',
                axisLine : {onZero: false},
                axisLabel : {
                    formatter: '{value} '
                },
                boundaryGap : false,
                data : timeList30


            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 人'
                }
            }
        ] ,
        series : [
            {
                name:'近一月访客数据图',
                type:'line',
                smooth:true,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            shadowColor : 'rgba(0,0,0,0.4)'
                        }
                    }
                },
                data: guestCountList30
            }
        ]
    };
    var myChart1 = echarts.init(document.getElementById('TU1'));
    myChart1.setOption(option1);

}

//近月问答数据折线图==================================================================
function ques30(){
    $('#Jday2').css('color','#000')
    $('#Jweek2').css('color','#000')
    $('#Jmonth2').css('color','#00c4c1')
    // 指定图表的配置项和数据

    var option2 = {
        backgroundColor: 'rgba(255,255,255,1)',
        legend: {
               data:['近一月问答数据图']
        },
        toolbox: {
            show : true,
            feature : {
//                    mark : {show: true},//辅助编辑
//                 dataView : {show: true, readOnly: false},//数据视图
                magicType : {show: true, type: ['line', 'bar']},//折线图，柱形图切换
                restore : {show: true},//还原
                saveAsImage : {show: true}//保存截图
            }
        },
        calculable : true,
        tooltip : {
            trigger: 'axis',
            formatter: "日期 : {b}<br/>问答 : {c} 条"
        },
        xAxis :[
            {
                type : 'category',
                axisLine : {onZero: false},
                axisLabel : {
                    formatter: '{value} '
                },
                boundaryGap : false,
                data : timeList30
            }
        ],
        yAxis : [
            {
                type : 'value',
                axisLabel : {
                    formatter: '{value} 条'
                }
            }
        ] ,
        series : [
            {
                name:'近一月问答数据图',
                type:'line',
                smooth:true,
                itemStyle: {
                    normal: {
                        lineStyle: {
                            shadowColor : 'rgba(0,0,0,0.4)'
                        }
                    }
                },
                data: suestionStatisticsCountList30
            }
        ]
    };
    var myChart2 = echarts.init(document.getElementById('TU2'));
    myChart2.setOption(option2);

}