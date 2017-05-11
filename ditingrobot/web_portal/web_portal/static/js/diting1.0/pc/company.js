function save_com_set() {
    if ($("#name").val()!="" && companyName0()) {
        var industry = $("#industry option:selected").val();
        var name = $("#name").val().replace(/（/g, "(").replace(/）/g,")");
        // var location = $("#location").val();
        // var productName = $("#productName").val();
        var headPortrait1 = $('#headPortrait1').val()
        var company = {
            name: name,
            // location: location,
            // productName: productName,
            headPortrait: headPortrait1,
            industry: industry
        };
        console.log(company)
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/company/save',
            data: JSON.stringify(company)
        }).done(function (data) {
            if (data.url == ''){
                layer.msg("账号信息保存成功", {icon: 6},function(){
                    parent.window.location.reload(false);
                })
            }else{
                window.location.href = data.url;
            }
        }).fail(function (data) {
            //console.log(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    } else {
        layer.msg("所有者名请填2~16位中文名称！", {icon: 5})
    }
}

function companyName0() {
    var r = /^[\u4E00-\u9FA5\(\)（）]{2,16}$/;
    var that = document.getElementById("name");
    if (r.test(that.value)) {
        that.style.color = "#000";
        return true;
    } else {
        that.style.color = "red";
        return false;
    }
}

function csh_Com_Set(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/get-company-info"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data)
        console.log(data.headPortrait)
        $("#name").val(data.name)
        // $("#location").val(data.location);
        // $("#productName").val(data.productName);
        if(data.headPortrait == null){
            $('#headImg').attr("src","/static/images/xiaodi.jpg")
            $('#headImg').attr("alt","/static/images/xiaodi.jpg")
            console.log(1)
        }else{
            $('#headImg').attr("src",data.headPortrait+'/thumbnail/100x300')
            $('#headImg').attr("alt",data.headPortrait+'/thumbnail/600x800')
            $('#headPortrait1').val(data.headPortrait)
            console.log(1)
          
          
        }
        if(data.industry == null){
            console.log(data.industry)
        }else {
            $("#industry").val(data.industry);
        }

    }).fail(function (data) {
        //	console.log(data);
    });

}
//pc端私信消息--------pc1.0
function cshPrivateMessage (){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var receive_name =encodeURI($("#receive_name").val())
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/tele_other/search?receive_name=" + receive_name+"&pageNo="+ 1
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data)
        Paging_PrivateMessage(data.total);
        var  strL = '<tr>  <th style="min-width:240px;" >对您私信的人</th> <th style="min-width: 120px"  >日期</th> <th style="min-width: 120px;">查看</th> <th style="min-width: 120px">删除</th></tr>';

        if(data.items.length==0){
            strL += '<tr> <td>暂时没有私内消息</td> <td>'+ new Date().toLocaleString() +'</td> <td></td> <td></td> </tr>'

        }else {
            for (var i = 0; i < data.items.length; i++) {
                var d = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());

                if(data.items[i].flag==0){
                    strL += '<tr> <td><a href="/robot-company/'+ data.items[i].forword_unique_id +'"  style="color: #00C4C2;">'+ data.items[i].forword_name +'</a></td> <td>'+ times +'</td> <td><a class="selfLetter_details" style="cursor: pointer;color: #169bd5;"   data-time="' + times + '"  data-biaoti="' + data.items[i].forword_name + '悄悄对您的机器人说："  data-zhengwen="' + data.items[i].message + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].flag + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/><span class="Biao2" style="display:inline-block;width: 10px; height:10px;border-radius: 50%;background: red;    position: relative;top: -12px;left: 8px;"></span></a></td><td><a class="deleteSelfLetter" data-id="'+ data.items[i].id +'" style=" cursor: pointer;"><img src="/static/images/diting1.0/pc/icon_delete.png" style="width: 30px;margin:auto;"/></a></td> </tr>'

                }else {
                    strL += '<tr> <td><a href="/robot-company/'+ data.items[i].forword_unique_id +'"  style="color: #00C4C2;">'+ data.items[i].forword_name +'</a></td> <td>'+ times +'</td> <td><a class="selfLetter_details" style="cursor: pointer;color: #169bd5;"   data-time="' + times + '"  data-biaoti="' + data.items[i].forword_name + '悄悄对您的机器人说："  data-zhengwen="' + data.items[i].message + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].flag + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/></a></td><td><a class="deleteSelfLetter" data-id="'+ data.items[i].id +'" style=" cursor: pointer;"><img src="/static/images/diting1.0/pc/icon_delete.png" style="width: 30px;margin:auto;"/></a></td> </tr>'

                }
            }


        }
        $('#station_L').html(strL)
        $('.selfLetter_details').click(function(){
            var biaotiTC = $(this).attr('data-biaoti');
            var timeTC = $(this).attr('data-time');
            var zhengwenTC = $(this).attr('data-zhengwen');
            var ids = $(this).attr('data-id');
            var biao2 = $(this).attr('data-biao2');
            stationDetails();
            var idss = {
                id: ids
            };
            if(biao2 == 0){
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/tele_other/update',
                    data: JSON.stringify(idss)
                }).done(function (data) {
                    var data = JSON.parse(data);
                    console.log(data)
                }).fail(function (data) {

                });
            }

            $('.biaotiTC').html(biaotiTC);
            $('.timeTC').html(timeTC);
            $('.zhengwenTC').html(zhengwenTC);

            $('#close_details').click(function(){
                window.location.reload(false);
            });

        });
        $('.deleteSelfLetter').click(function(){
            var idDel = $(this).attr("data-id");
            // var this1 = $(this);
            layer.confirm('您真的确定要删除了吗？', {
                btn: ['确定', '再考虑一下'] //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/tele_other/delete/'+ idDel
                    // data: JSON.stringify(idss)
                }).done(function (data) {
                    // var data = JSON.parse(data);
                    console.log(data)
                    layer.msg(data.message, {time:1000,icon: 6},function(){
                        window.location.reload(false);
                        // this1.parent().parent().remove();
                    });

                }).fail(function (data) {

                });
                
            }, function () {

            });
        });
    }).fail(function (data) {
        //	console.log(data);
    });

}

//分页器，页码切换执行--------------------pc1.0私信消息分页器
function Paging_PrivateMessage(strips) { //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var receive_name =encodeURI($("#receive_name").val())
    var x = $("#pageNo").val();
    $('#red').smartpaginator({
        totalrecords: strips,
        datacontainer: 'station_L',
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
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/tele_other/search?receive_name=" + receive_name+"&pageNo="+ $("#pageNo").val()
            }).done(function (data) {
                var data = JSON.parse(data);
                console.log(data)
                Paging_PrivateMessage(data.total);
                var  strL = '<tr>  <th style="min-width:240px;" >对您私信的人</th> <th style="min-width: 120px"  >日期</th> <th style="min-width: 120px;">查看</th> <th style="min-width: 120px">删除</th></tr>';

                if(data.items.length==0){
                    strL += '<tr> <td>暂时没有私内消息</td> <td>'+ new Date().toLocaleString() +'</td> <td></td> <td></td> </tr>'

                }else {
                    for (var i = 0; i < data.items.length; i++) {
                        var d = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                        var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());

                        if(data.items[i].flag==0){
                            strL += '<tr> <td><a href="/robot-company/'+ data.items[i].forword_unique_id +'"  style="color: #00C4C2;">'+ data.items[i].forword_name +'</a></td> <td>'+ times +'</td> <td><a class="selfLetter_details" style="cursor: pointer;color: #169bd5;"   data-time="' + times + '"  data-biaoti="' + data.items[i].forword_name + '悄悄对您的机器人说："  data-zhengwen="' + data.items[i].message + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].flag + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/><span class="Biao2" style="display:inline-block;width: 10px; height:10px;border-radius: 50%;background: red;    position: relative;top: -12px;left: 8px;"></span></a></td><td><a class="deleteSelfLetter" data-id="'+ data.items[i].id +'" style=" cursor: pointer;"><img src="/static/images/diting1.0/pc/icon_delete.png" style="width: 30px;margin:auto;"/></a></td> </tr>'

                        }else {
                            strL += '<tr> <td><a href="/robot-company/'+ data.items[i].forword_unique_id +'"  style="color: #00C4C2;">'+ data.items[i].forword_name +'</a></td> <td>'+ times +'</td> <td><a class="selfLetter_details" style="cursor: pointer;color: #169bd5;"   data-time="' + times + '"  data-biaoti="' + data.items[i].forword_name + '悄悄对您的机器人说："  data-zhengwen="' + data.items[i].message + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].flag + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/></a></td><td><a class="deleteSelfLetter" data-id="'+ data.items[i].id +'" style=" cursor: pointer;"><img src="/static/images/diting1.0/pc/icon_delete.png" style="width: 30px;margin:auto;"/></a></td> </tr>'

                        }
                    }


                }
                $('#station_L').html(strL)
                $('.selfLetter_details').click(function(){
                    var biaotiTC = $(this).attr('data-biaoti');
                    var timeTC = $(this).attr('data-time');
                    var zhengwenTC = $(this).attr('data-zhengwen');
                    var ids = $(this).attr('data-id');
                    var biao2 = $(this).attr('data-biao2');
                    stationDetails();
                    var idss = {
                        id: ids
                    };
                    if(biao2 == 0){
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: '/tele_other/update',
                            data: JSON.stringify(idss)
                        }).done(function (data) {
                            var data = JSON.parse(data);
                            console.log(data)
                        }).fail(function (data) {

                        });
                    }

                    $('.biaotiTC').html(biaotiTC);
                    $('.timeTC').html(timeTC);
                    $('.zhengwenTC').html(zhengwenTC);

                    $('#close_details').click(function(){
                        window.location.reload(false);
                    });

                });
                $('.deleteSelfLetter').click(function(){
                    var idDel = $(this).attr("data-id");
                    // var this1 = $(this)
                    layer.confirm('您真的确定要删除了吗？', {
                        btn: ['确定', '再考虑一下'] //按钮
                    }, function () {
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: '/tele_other/delete/'+ idDel
                            // data: JSON.stringify(idss)
                        }).done(function (data) {
                            // var data = JSON.parse(data);
                            console.log(data)
                            layer.msg(data.message, {time:1000,icon: 6},function(){
                                window.location.reload(false);
                                // this1.parent().parent().remove();
                            });
    
                        }).fail(function (data) {
    
                        });
                    }, function () {

                    });
                });
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}








//pc端系统消息初始化
function csh_sysStation(){

    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/mails/search_phone?pageNo="+ 1
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_sysStation(data.total);
console.log(data)
        var strS = '<tr>  <th style="min-width:240px;" >标题</th> <th style="min-width: 120px"  >日期</th> <th style="min-width: 120px;">查看</th> </tr>';

        if(data.items.length==0){
            strS += '<tr> <td>暂时没有站内消息</td> <td>'+ new Date().toLocaleString() +'</td> <td></td> <td></td> </tr>'
        }else {
            for (var i = 0; i < data.items.length; i++) {
                var d = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());

                if(data.items[i].mail_phone.length==0){
                    strS += '<tr> <td>'+ data.items[i].biaoti +'</td> <td>'+ times +'</td> <td><a class="station_message_details" style="cursor: pointer;text-align: center"   data-time="' + times + '"  data-biaoti="' + data.items[i].biaoti + '"  data-zhengwen="' + data.items[i].zhengwen + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].mail_phone.length + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/><span class="Biao2" style="display:inline-block;width: 10px; height:10px;border-radius: 50%;background: red;    position: relative;top: -12px;left: 8px;"></span></a></td> </tr>'
                }else {
                    strS += '<tr> <td>'+ data.items[i].biaoti +'</td> <td>'+ times +'</td> <td><a class="station_message_details" style="cursor: pointer;text-align: center"   data-time="' + times + '"  data-biaoti="' + data.items[i].biaoti + '"  data-zhengwen="' + data.items[i].zhengwen + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].mail_phone.length + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/></a></td>  </tr>'
                }
            }
        }
        
        $('#station_M').html(strS)
        $('.station_message_details').click(function(){
            var biaotiTC = $(this).attr('data-biaoti');
            var timeTC = $(this).attr('data-time');
            var zhengwenTC = $(this).attr('data-zhengwen');
            var ids = $(this).attr('data-id');
            var biao2 = $(this).attr('data-biao2');
            stationDetails();
            var idss = {
                ids: ids
            };
            if(biao2 == 0){
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/mails/save_phone',
                    data: JSON.stringify(idss)
                }).done(function (data) {

                }).fail(function (data) {

                });
            }
         
            $('.biaotiTC').html(biaotiTC);
            $('.timeTC').html(timeTC);
            $('.zhengwenTC').html(zhengwenTC);

            $('#close_details').click(function(){
                window.location.reload(false);
                $('#tanchuang').html("");
            });
        });
      
    }).fail(function (data) {
        //	console.log(data);
    });

}
//分页器，页码切换执行--------------------pc1.0系统消息分页器
function Paging_sysStation(strips) { //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var x = $("#pageNo").val();
    $('#red').smartpaginator({
        totalrecords: strips,
        datacontainer: 'station_M',
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
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/mails/search_phone?pageNo="+ $("#pageNo").val()
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_sysStation(data.total);
                console.log(data)
                var strS = '<tr>  <th style="min-width:240px;" >标题</th> <th style="min-width: 120px"  >日期</th> <th style="min-width: 120px;">查看</th> </tr>';

                if(data.items.length==0){
                    strS += '<tr> <td>暂时没有站内消息</td> <td>'+ new Date().toLocaleString() +'</td> <td></td> <td></td> </tr>'
                }else {
                    for (var i = 0; i < data.items.length; i++) {
                        var d = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                        var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());

                        if(data.items[i].mail_phone.length==0){
                            strS += '<tr> <td>'+ data.items[i].biaoti +'</td> <td>'+ times +'</td> <td><a class="station_message_details" style="cursor: pointer;text-align: center"   data-time="' + times + '"  data-biaoti="' + data.items[i].biaoti + '"  data-zhengwen="' + data.items[i].zhengwen + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].mail_phone.length + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/><span class="Biao2" style="display:inline-block;width: 10px; height:10px;border-radius: 50%;background: red;    position: relative;top: -12px;left: 8px;"></span></a></td> </tr>'
                        }else {
                            strS += '<tr> <td>'+ data.items[i].biaoti +'</td> <td>'+ times +'</td> <td><a class="station_message_details" style="cursor: pointer;text-align: center"   data-time="' + times + '"  data-biaoti="' + data.items[i].biaoti + '"  data-zhengwen="' + data.items[i].zhengwen + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].mail_phone.length + '"><img src="/static/images/diting1.0/pc/icon_CK.png" style="width: 30px;margin:auto;"/></a></td>  </tr>'
                        }
                    }
                }

                $('#station_M').html(strS)
                $('.station_message_details').click(function(){
                    var biaotiTC = $(this).attr('data-biaoti');
                    var timeTC = $(this).attr('data-time');
                    var zhengwenTC = $(this).attr('data-zhengwen');
                    var ids = $(this).attr('data-id');
                    var biao2 = $(this).attr('data-biao2');
                    stationDetails();
                    var idss = {
                        ids: ids
                    };
                    if(biao2 == 0){
                        $.ajax({
                            type: "POST",
                            contentType: "application/json",
                            url: '/mails/save_phone',
                            data: JSON.stringify(idss)
                        }).done(function (data) {

                        }).fail(function (data) {

                        });
                    }

                    $('.biaotiTC').html(biaotiTC);
                    $('.timeTC').html(timeTC);
                    $('.zhengwenTC').html(zhengwenTC);

                    $('#close_details').click(function(){
                        window.location.reload(false);
                        $('#tanchuang').html("");
                    });
                });
                
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

//站内消息查看详情
function stationDetails(){
    var str0 ='';
    str0 += '<div class="update_c" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;"> <div class="stationMessageMoreStyle" ><div id="close_details" style="height: 27px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;cursor: pointer;"></div><p style="text-align: center;font-size: 16px;margin: 20px 0;" class="biaotiTC"></p><p class="timeTC"></p> <p class="zhengwenTC" style=" text-align: left;text-indent:2em;font-size: 12px;line-height: 24px;color:#999;"></p>  </div> </div>'
    $('#tanchuang').html(str0);
    
}

//移动端站内消息加载
function csh_Station_phone(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/mails/search_phone"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data)
        var strS='';
        if(data.items.length==0){
            strS += '<li> <p id="newTime">'+ new Date().toLocaleString() +'</p> <p>暂时没有站内消息。</p> </li>'
        }else {
            for (var i = 0; i < data.items.length; i++) {
                var d = new Date(data.items[i].createdTime);    //根据时间戳生成的时间对象
                var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());

                if(data.items[i].mail_phone.length==0){
                    strS += '<li  class="station_message_details" data-time="' + times + '"  data-biaoti="' + data.items[i].biaoti + '"  data-zhengwen="' + data.items[i].zhengwen + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].mail_phone.length + '"> <p  style="color: #888;">'+ times +'</p> <p style="margin: 0 5% 0% 13%;">'+ data.items[i].biaoti +'<a class="station_message_details" style="cursor: pointer;float: right;color: orange;"  ><span class="Biao2" style="display:inline-block;width: 6px; height:6px;border-radius: 50%;background: red;"></span>>></a></p> </li>';
                }else {
                    strS += '<li  class="station_message_details" data-time="' + times + '"  data-biaoti="' + data.items[i].biaoti + '"  data-zhengwen="' + data.items[i].zhengwen + '" data-id="' + data.items[i].id + '" data-biao2="' + data.items[i].mail_phone.length + '"> <p  style="color: #888;">'+ times +'</p> <p style="margin: 0 5% 0% 13%;">'+ data.items[i].biaoti +'<a class="station_message_details" style="float: right;color: orange;" >>></a></p> </li>';

                }
            }


        }
        $('#station_M').html(strS)
        $('.station_message_details').click(function(){
            var biaotiTC = $(this).attr('data-biaoti');
            var timeTC = $(this).attr('data-time');
            var zhengwenTC = $(this).attr('data-zhengwen');
            var ids = $(this).attr('data-id');
            var biao2 = $(this).attr('data-biao2');
            stationDetails();
            var idss = {
                ids: ids
            };
            if(biao2 == 0){
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/mails/save_phone',
                    data: JSON.stringify(idss)
                }).done(function (data) {

                }).fail(function (data) {

                });
            }

            $('.biaotiTC').html(biaotiTC);
            $('.timeTC').html(timeTC);
            $('.zhengwenTC').html(zhengwenTC);

            $('#close_details').click(function(){
                window.location.reload(false);
                $('#tanchuang').html("");
            });
        });
    }).fail(function (data) {
        //	console.log(data);
    });

}


//微信支付充值点数
function payTransWX(){
    layer.msg('正在生成二维码');
    var body0 = $("#body0").val();
    var order_price0 = $("#order_price0").val();
    var order_dian0 = $("#order_dian0").val();
    var pay ={
        body:body0,
        order_price:order_price0
    };
    console.log(pay)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: 'api/wechat/pay',
        data: JSON.stringify(pay)
    }).done(function (data) {
        console.log(data);
        layer.open({
            type: 1,
            title:"微信扫码充值点数",
            skin: 'layui-layer', //样式类名
            closeBtn: 1, //不显示关闭按钮
            anim: 2,
            shadeClose: true, //开启遮罩关闭
            content: '<img src="'+data+'" style="margin: auto ;width: 290px;"/>'
        });
    }).fail(function (data) {
        console.log(data);

    });
}
//交易管理nav点数充值按键
function point0Trans() {
    $('#payNav0').css("display","block");
    $('#payOrderNav0').css("display","none");
    $('#robotTransfer0').css("display","none");
    $('#point0').css("background","#00c4c2");
    $('#order0').css("background","#82dedc");
    $('#transfer0').css("background","#82dedc");
}
//交易管理nav订单记录按键
function order0Trans() {
    $('#payNav0').css("display","none");
    $('#payOrderNav0').css("display","block");
    $('#robotTransfer0').css("display","none");
    $('#point0').css("background","#82dedc");
    $('#order0').css("background","#00c4c2");
    $('#transfer0').css("background","#82dedc");
    
    
    
    
}
//交易管理nav转让平行人按键
function transfer0Trans() {
    $('#payNav0').css("display","none");
    $('#payOrderNav0').css("display","none");
    $('#robotTransfer0').css("display","block");
    $('#point0').css("background","#82dedc");
    $('#order0').css("background","#82dedc");
    $('#transfer0').css("background","#00c4c2");
    
    
    
    
}


function showTreaty(){//卖平行人协议
    layer.confirm(
        '<h4>转让说明</h4><p>卖方每24小时内对每个平行人最多可以估价3次无间隔限制，为确保转让期间的平行人知识库属实，在估价确认后，未达成转让或撤销估价之前，卖方无法进行修改、导入知识库操作。请留下常用联系方式，以免错过交易。购买者信息会显示在购买列表，以购买者填写的未注册新手机号为判定标准，作为最终选择转让完成的账号，请仔细核对。务必保留双方对话及付款记录截图，以免发生纠纷。买方购买平行人交易完毕会有24小时锁定时间，期间不影响使用，但不能执行修改、导入知识库操作。卖方可先完成交易，再让买方付款。如买方未付款卖方可提供证据向本站申诉，核实后予以找回。</p> <h4>免责声明</h4> <p>如因不可抗力或其他无法控制的原因造成网站销售系统崩溃或无法正常使用，从而导致网上交易无法完成或丢失有关的信息、记录等。或因个人失误导致的任何损失，本站将不承担责任。但是我们将会合理的竭尽所能协助处理善后事宜，并努力使平行人减少数据损失。</p> <p>本站仅提供给用户机器人使用平台及转让平台，机器人的知识库数据一致性由当前拥有者保障，转让前后机器人相关事宜由双方自行协商解决。与本站无关。双方应协商好相关事宜再进行机器人转让，本站对转让的机器人不承担任何责任，当前平行人转让属限免阶段。</p> <h4>客户监督</h4> <p>我们坚持通过不懈努力，为平行人提供最佳服务，我们在给平行人提供服务的全程中接受客户的监督。</p> <h4>争议处理</h4> <p>如果平行人客户与网站之间发生任何争议，可依据当时双方所认定的协议或相关法律来解决</p> <p>同意后即属认同并接受上述条款。</p> ',
        {
            skin: 'layui-layer-Treaty',btn: ['同意'], title:"服务条款"//按钮
        }, function () {
          
            $("#Treaty").prop("checked",true)
         
            layer.msg('勾选服务协议了哦！');
            
           
        });
}
function buyTreaty() {//买平行人协议
    layer.confirm(
        '<h4>转让说明</h4> <p>买方欲购买平行人，每个平行人交易期间买方每天只能报价一次。请确认新手机号未注册过本网站，否则将影响交易。请留下常用联系方式（仅卖方可见），避免错过交易。同时请记录好卖家联系方式，如未记录信息，请进入站内消息查看。务必保留双方对话及付款记录截图，以免发生纠纷。为确保转让期间的平行人知识库属实，卖方估价后在未完成转让或者撤销估价期间，卖方不得修改、导入知识库。买方购买平行人交易完毕会有24小时锁定时间，期间不影响使用，但不能执行修改、导入、导出知识库。卖方可先完成交易，再让买方付款。如买方未付款卖方可提供证据向本站申诉，核实后予以找回。</p><h4>免责声明</h4> <p>如因不可抗力或其他无法控制的原因造成网站销售系统崩溃或无法正常使用，从而导致网上交易无法完成或丢失有关的信息、记录等。或因个人失误导致的任何损失，本站将不承担责任。但是我们将会合理的竭尽所能协助处理善后事宜，并努力使平行人减少数据损失。</p><p>本站仅提供给用户机器人使用平台及转让平台，机器人的知识库数据一致性由当前拥有者保障，转让前后机器人相关事宜由双方自行协商解决。与本站无关。双方应协商好相关事宜再进行机器人转让，本站对转让的机器人不承担任何责任，当前平行人转让属限免阶段 <h4>客户监督</h4> <p>我们坚持通过不懈努力，为平行人提供最佳服务，我们在给平行人提供服务的全程中接受客户的监督。</p><h4>争议处理</h4> <p>如果平行人客户与网站之间发生任何争议，可依据当时双方所认定的协议或相关法律来解决 </p><p>同意后即属认同并接受上述条款。</p>',
        {
            skin: 'layui-layer-Treaty',btn: ['同意'], title:"服务条款"//按钮
        }, function () {
            $('#Treaty').attr("checked",true)
            layer.msg('勾选服务条款了哦！');
           
        });
}
