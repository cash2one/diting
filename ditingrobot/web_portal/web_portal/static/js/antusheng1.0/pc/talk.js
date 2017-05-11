//与小谛的交流对话区
function talks() {
    getCode();
    var arrIcon = ['/static/images/phone/manHeader1.png', '/static/images/phone/womanHeader2.png'];
    if($('#headPortrait').val()!="" && $('#headPortrait').val()!="None"){
        arrIcon[1]=$('#headPortrait').val()+'/thumbnail/100x100';
    }

    var iNow = -1;    //用来累加改变左右浮动
    var btn = document.getElementById('btn');
    var text = document.getElementById('text');
    var content = document.getElementById('content');
    var img = content.getElementsByClassName('headImg ');
    var span = content.getElementsByTagName('span');
    var myDate = new Date();
    var Hour = myDate.getHours();
    var Min = myDate.getMinutes();
    Hour = Hour > 9 ? Hour : "0" + Hour;
    Min = Min > 9 ? Min : "0" + Min;
    if ($("#welcomes").val() == "") {
        $("#welcomes").val("我很懒！没有写动态啦！")
    }
    // content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>'+ $("#welcomes").val() + '<i>' + Hour + ':' + Min + '</i></span></li>';
    // iNow++;
    // img[iNow].className += 'imgleft';
    // span[iNow].className += 'spanleft';
    $("#roboName0").html($("#name").val());
    
    // console.log($("#uuid").val());
    //发送
    // var uuid = getCookie("uuid");
    function talkSend() {
        clearInterval(selfTalk0);
        var myDate = new Date();
        var Hour = myDate.getHours();
        var Min = myDate.getMinutes();
        Hour = Hour > 9 ? Hour : "0" + Hour;
        Min = Min > 9 ? Min : "0" + Min;
        if (text.value == '') {
            layer.msg('不能发送空消息哦！', {icon: 6})
        } else {
            content.innerHTML += '<li><img class="headImg " src="' + arrIcon[0] + '"><span><b style="color:#333;">我：</b>'+ text.value.replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'") + '<i>' + Hour + ':' + Min + '</i></span></li>';
            iNow++;
            img[iNow].className += 'imgright';
            span[iNow].className += 'spanright';
            var uuid = $("#uuid").val();
            //console.log(uuid)
            var question = text.value.replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            text.value = '';
            // 内容过多时,将滚动条放置到最底端
            content.scrollTop = content.scrollHeight;
            //------------
            var ques = {
                uuid: uuid,
                question: question,
                robotName: $("#name").val(),
                unique_id: $("#unique_id").val(),
                answer1: $("#answer1").val(),
                answer2: $("#answer2").val(),
                answer3: $("#answer3").val(),
                answer4: $("#answer4").val(),
                answer5: $("#answer5").val(),
                enable: $("#enable").val()
            };
            console.log(ques)
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/chat/info',
                data: JSON.stringify(ques)
            }).done(function (data) {
                var data = JSON.parse(data);
                // console.log(data)
                if (data.mp3Url!=null && data.mp3Url!="null"){
                    content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<br/><audio controls="controls" src="'+ data.mp3Url +'">您的浏览器不支持audio音乐播放。</audio><i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                }else if(data.skip_url!=null && data.skip_url!="null"){
                    window.location.href = data.skip_url
                }else if(data.img_url!=null && data.img_url!="null"){
                    content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<br/><img class="answerImg" src="'+ data.img_url +'?imageMogr2/thumbnail/300x300" alt="'+ data.img_url + '"  onclick="answerImgBig(this.alt)"/><i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                }else if(data.changeBooks!=null && data.changeBooks!="null"){
                    var strBook = '';
                    for(var i=0;i<data.changeBooks.length;i++ ){
                        strBook +='<div class="changeBook1" ><img class="answerImg"  src="'+ data.changeBooks[i].picture +'?imageMogr2/thumbnail/300x300" alt="'+  data.changeBooks[i].picture + '"/></div><div class="changeBook2" ><h5>'+  data.changeBooks[i].name + '</h5><h6>作者：'+  data.changeBooks[i].author + '</h6><h6 >分享者：'+  data.changeBooks[i].share + '</h6><h6 >价格：'+  data.changeBooks[i].price + '</h6></div><h6>简介：'+  data.changeBooks[i].synopsis + '</h6><br/>'
                    }
                    content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<br/><br/><div >'+ strBook +'</div><i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                } else {
                    content.innerHTML += '<li><img  class="headImg "  src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                }
               
                content.scrollTop = content.scrollHeight;
            }).fail(function (data) {
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        }
    }
    //点击按钮发送
    btn.onclick = function () {
            talkSend();
    };
    $(".talkS").click(function(){
        var text = $(this).children("span").html();
        $("#text").val(text);
        talkSend();
    })

    //回车键发送
    text.onkeydown = function (ev) {
        var oEvent = ev || event;
        if (oEvent.keyCode == 13 && oEvent.ctrlKey || oEvent.keyCode == 13) {
            if($("#loginInfos").val()=="None"){
                $("#tanchuang").css("display","block")
            }else{
                talkSend();
            }
        }
    }
    //随机自动说话
    var ci1 = 0;
    var selfTalk0 = setInterval(function () {
            var question = getPlaceholder()
            var myDate = new Date();
            var Hour = myDate.getHours();
            var Min = myDate.getMinutes();
            Hour = Hour > 9 ? Hour : "0" + Hour;
            Min = Min > 9 ? Min : "0" + Min;
           
        
            content.innerHTML += '<li><img class="headImg " src="' + arrIcon[0] + '"><span><b style="color:#333;">我：</b>'+ question + '<i>' + Hour + ':' + Min + '</i></span></li>';
            iNow++;
            img[iNow].className += 'imgright';
            span[iNow].className += 'spanright';
            var uuid = $("#uuid").val();
            // 内容过多时,将滚动条放置到最底端
            content.scrollTop = content.scrollHeight;
            //------------
            var ques = {
                uuid: uuid,
                question: question,
                robotName: $("#name").val(),
                unique_id: $("#unique_id").val(),
                answer1: $("#answer1").val(),
                answer2: $("#answer2").val(),
                answer3: $("#answer3").val(),
                answer4: $("#answer4").val(),
                answer5: $("#answer5").val(),
                enable: $("#enable").val()
            };
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/chat/info',
                data: JSON.stringify(ques)
            }).done(function (data) {
                var data = JSON.parse(data);
                if (data.mp3Url!=null && data.mp3Url!="null"){
                    content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<br/><audio controls="controls" src="'+ data.mp3Url +'">您的浏览器不支持audio音乐播放。</audio><i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                }else if(data.skip_url!=null && data.skip_url!="null"){
                    window.location.href = data.skip_url
                }else if(data.img_url!=null && data.img_url!="null"){
                    content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<br/><img class="answerImg" src="'+ data.img_url +'?imageMogr2/thumbnail/300x300" alt="'+ data.img_url + '"  onclick="answerImgBig(this.alt)"/><i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                }else if(data.changeBooks!=null && data.changeBooks!="null"){
                    var strBook = '';
                    for(var i=0;i<data.changeBooks.length;i++ ){
                        strBook +='<div class="changeBook1" ><img class="answerImg"  src="'+ data.changeBooks[i].picture +'?imageMogr2/thumbnail/300x300" alt="'+  data.changeBooks[i].picture + '"/></div><div class="changeBook2" ><h5>'+  data.changeBooks[i].name + '</h5><h6>作者：'+  data.changeBooks[i].author + '</h6><h6 >分享者：'+  data.changeBooks[i].share + '</h6><h6 >价格：'+  data.changeBooks[i].price + '</h6></div><h6>简介：'+  data.changeBooks[i].synopsis + '</h6><br/>'
                    }
                    content.innerHTML += '<li><img class="headImg " src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<br/><br/><div >'+ strBook +'</div><i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';
                } else {
                    content.innerHTML += '<li><img  class="headImg "  src="' + arrIcon[1] + '"><span><b style="color:#333;">'+ $("#name").val() +'：</b>' + data.answer + '<i>' + Hour + ':' + Min + '</i></span></li>';
                    iNow++;
                    img[iNow].className += 'imgleft';
                    span[iNow].className += 'spanleft';


                }

                content.scrollTop = content.scrollHeight;
                
            }).fail(function (data) {
                var message = JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        ci1++
        if (ci1 >= 3){
            clearInterval(selfTalk0);
        }
        },4000);
}




