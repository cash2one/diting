
//生成随机id
function getCode() {
    var arr = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    var str = '';
    while (str.length < 32) {
        var iNum = parseInt(Math.random() * 100)
        while (iNum > 35) {
            iNum = parseInt(Math.random() * 100)
        }
        str += arr[iNum]
    }
    $("#uuid").val(str);
    return str;
}
//存cookie
function setCookie(name, str, time) {
    var date = new Date();
    var date1 = date.setDate(date.getDate() + time);
    window.document.cookie = name + "=" + str + ";expires=" + date1 + ";"
}
//取得cookie
function getCookie(name) {
    var str = document.cookie.split(";")
    for (var i = 0; i < str.length; i++) {
        var str2 = str[i].split("=");
        if (str2[0] == name) {
            return str2[1];
        }
    }
    return str2[1];
}

//点击显示隐藏块
function clickDisnone(Across, disnone) {
    var oClick = document.getElementById(Across);
    var oDisnone = document.getElementById(disnone);
    oClick.onclick = function () {
        if (oDisnone.style.display == "none") {
            oDisnone.style.display = "block";
        } else {
            oDisnone.style.display = "none";
        }
    }
}
//动态改变页面宽度
function dynamicWidth() {
    document.documentElement.style.fontSize = document.documentElement.clientWidth / 12 + "px";
    window.addEventListener("resize", fn, false);
    function fn() {
        document.documentElement.style.fontSize = document.documentElement.clientWidth / 12 + "px";
    }
}
//与小谛的交流对话区
function talk() {
    getCode();
    var arrIcon = ['/static/images/theader.jpg', '/static/images/xiaodi.jpg'];
    var num = 0;     //控制头像改变
    var iNow = -1;    //用来累加改变左右浮动
    var icon = document.getElementById('user_face_icon').getElementsByTagName('img');
    var btn = document.getElementById('btn');
    var text = document.getElementById('text');
    var content = document.getElementById('content');
    var img = content.getElementsByTagName('img');
    var span = content.getElementsByTagName('span');
    //发送
    // var name = getCookie("uuid");
    var name = $("#uuid").val();

    function fasong() {
        var myDate = new Date();
        var Hour = myDate.getHours();
        var Min = myDate.getMinutes();
        Hour = Hour > 9 ? Hour : "0" + Hour;
        Min = Min > 9 ? Min : "0" + Min;
        if (text.value == '') {
            layer.msg('不能发送空消息哦！', {icon: 6})
        } else {
            content.innerHTML += '<li><img class="headImg " src="' + arrIcon[0] + '"><span>' + text.value + '<i>' + Hour + ':' + Min + '</i></span></li>';
            iNow++;
            img[iNow].className += 'imgright';
            span[iNow].className += 'spanright';
            var question = text.value;
            text.value = '';
            // 内容过多时,将滚动条放置到最底端
            content.scrollTop = content.scrollHeight;
            //------------
            var ques = {
                uuid: name,
                question: question
            };
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/chat/info',
                data: JSON.stringify(ques)
            }).done(function (data) {
                var answer = JSON.parse(data).answer;
                content.innerHTML += '<li><img src="' + arrIcon[1] + '"><span>' + answer + '<i>' + Hour + ':' + Min + '</i></span></li>';
                iNow++;
                img[iNow].className += 'imgleft';
                span[iNow].className += 'spanleft';
                content.scrollTop = content.scrollHeight;
            }).fail(function (data) {
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        }
    }

    //点击按钮发送
    btn.onclick = function () {
        fasong();
    };
    //回车键发送
    text.onkeydown = function (ev) {
        var oEvent = ev || event;
        if (oEvent.keyCode == 13 && oEvent.ctrlKey || oEvent.keyCode == 13) {
            fasong();
        }
    }
}
//划过返回顶部
function hover1(obj1, obj2) {
    obj1.hover(function () {
            obj2.addClass("none_1")
        },
        function () {
            obj2.removeClass("none_1")
        })
}


function timess(timestamp3) {
    var d = new Date(timestamp3);    //根据时间戳生成的时间对象
    var Hour = d.getHours() > 9 ? d.getHours() : "0" + d.getHours();
    var Min = d.getMinutes() > 9 ? d.getMinutes() : "0" + d.getMinutes();
    var Sec = d.getSeconds() > 9 ? d.getSeconds() : "0" + d.getSeconds();
    var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate()) + " " + (Hour) + ":" + (Min) + ":" + (Sec);
    return times
}


//上传图片
function uploadImageAdd() {

    $.ajaxFileUpload({
        type: 'post',
        url: '/upload/inputImage', //你处理上传文件的服务端
        secureuri: false, //是否需要安全协议，一般设置为false
        fileElementId: 'inputImage',//与页面处理代码中file相对应的ID值
        async: false,
        dataType: 'json', //返回数据类型:text，xml，json，html,scritp,jsonp五种
        success: function (data) {
            //聊天图片添加
            $("#img-yuLan").attr("src", data.url + "?imageMogr2/thumbnail/150x150")
            $('#img-yuLan').attr("alt",data.url)
            $('#addImgUrl').val(data.url)
            console.log($('#addImgUrl').val())
            // window.location.href = $('#RobotHeader').val()+$('#cropParameter').val();
//                if (element == 'input-logo') {
//                    $("#img-log").attr("src", data.url + "?date=" + new Date().getTime());
//                    $("#img-log").css("display", "block");
//                    $("#smallLogoUrl").val(data.url);
//                    ctx.company().smallLogoUrl = data.url;
//
//                    $("#input-logo").off("change");
//                    $("#input-logo").on("change", function () {
//                        toUpload(this);
//                    });
//                } else if (element == 'com-pic') {
//                    picManage.picUpload(data.url, element);
//                    ctx.company().imageUrls.push(data.url);
//
//                    $("#com-pic").off("change");
//                    $("#com-pic").on("change", function () {
//                        toUpload(this);
//                    });
//                } else {
//                    picManage.picUpload(data.url, element);
//                    ctx.product().productImageUrls.push(data.url);
//                    $("#product-pic").off("change");
//                    $("#product-pic").on("change", function () {
//                        toUpload(this);
//                    });
//                }
        },
        error: function (data, status, e)//服务器响应失败处理函数
        {
            console.error(e);
//                $("#com-pic,#product-pic").off("change");
//                $("#com-pic,#product-pic").on("change", function () {
//                    toUpload(this);
//                });
        }

    })


}

function answerImgBig(src){
    var str = "";
    str += '<div class="closeImfBig" style="position: fixed;z-index:999;background: rgba(0,0,0,0.7);bottom: 0px;top: 0px;left: 0px;right: 0px;display: block;">  <img  class="bigImg0" src="'+ src +'"></div>'
    $('#answerImgBigTC').html(str);

    $('.closeImfBig').click(function(){
        $('#answerImgBigTC').html("");
    })
}


