

//-----------------------------------
addEvent(window, "load", function () {
    var oPhone = document.getElementById("phone");//手机号
    var oMessCode = document.getElementById("telCode");//短信验证码
    var oPwd = document.getElementById("pwd");//密码
    var oPwd2 = document.getElementById("pwd2");//确认密码
    // addEvent(oPhone, "blur", oPhonetest);
    addEvent(oPwd, "blur", pwdtest);
    addEvent(oPwd2, "blur", pwdtest2);
});

function oPhonetest() {
    var that = document.getElementById("phone");
    var re = /^1[5|3|4|7|8][0-9]{9}$/;
    if (re.test(that.value)) {
        that.style.color = "black";
        $('.phone_i').css("display","none");
        return true;
    } else {
        //layer.alert("请输入正确手机号");
        that.style.color = "red";
        $('.phone_i').css("display","block");
        return false;
    }
}

function pwdtest() {
    var that = document.getElementById("pwd");
    var re = /^\w{6,16}$/;
    // var re = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
    if (re.test(that.value)) {
        that.style.color = "black";
        $('.pwd_i').css("display","none");
        return true;
    } else {
        //layer.alert("密码不能少于6位哦");
        that.style.color = "red";
        $('.pwd_i').css("display","block");
        return false;
    }
}

function pwdtest2() {
    var oPwd = document.getElementById("pwd");//密码
    var that = document.getElementById("pwd2");//确认密码
    if (that.value == oPwd.value) {
        that.style.color = "black";
        $('.pwd2_i').css("display","none");
        return true;
    } else {
        //layer.alert("两次密码需要一致");
        that.style.color = "red";
        $('.pwd2_i').css("display","block");
        return false;
    }
}

function telcode() {
    var that = document.getElementById("telcode");
    if (that.value == "") {
        //layer.alert("请输入正确验证码");
        that.style.color = "red";
        return false;
    } else {
        that.style.color = "black";
        return true;
    }
}
//-----------------------------------
function addEvent(obj, type, fn) { //添加事件兼容
    if (obj.addEventListener) {
        obj.addEventListener(type, fn);
    } else if (obj.attachEvent) {
        obj.attachEvent('on' + type, fn);
    }
}

function removeEvent(obj, type, fn) { //移除事件兼容
    if (obj.removeEventListener) {
        obj.removeEventListener(type, fn);
    } else if (obj.detachEvent) {
        obj.detachEvent('on' + type, fn);
    }
}

function getTarget(evt) { //得到事件目标
    if (evt.target) {
        return evt.target;
    } else if (window.event.srcElement) {
        return window.event.srcElement;
    }
}

function getCode() {
    var arr = ['a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9']
    var str = ''
    while (str.length < 32) {
        var iNum = parseInt(Math.random() * 100)
        while (iNum > 35) {
            iNum = parseInt(Math.random() * 100)
        }
        str += arr[iNum]
    }
    return str;
}

//1组注册-----------------------------------
function save01() {
    var checkbox00 = document.getElementById('checkbox');
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();
    if (oPhonetest() && pwdtest() && pwdtest2() && telcode() && checkbox00.checked == true) {
    // if (oPhonetest() && checkbox00.checked == true && btnTxCode == txcode) {
        textCode0( $('.code_send0'));
        save()
    }
    else {
        layer.msg('您的注册账号、密码和短信验证都要正确填写,还有不要忘记阅读协议~~~', {icon: 5});
        // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});
    }
}

//2组注册-----------------------------------
function save02Z() {
    var checkbox00 = document.getElementById('checkbox');
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();
    if (oPhonetest() && pwdtest() && pwdtest2() && telcode() && checkbox00.checked == true) {
        // if (oPhonetest() && checkbox00.checked == true && btnTxCode == txcode) {
        textCode0( $('.code_send0'));
        save2Z()
    }
    else {
        layer.msg('您的注册账号、密码和短信验证都要正确填写,还有不要忘记阅读协议~~~', {icon: 5});
        // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});
    }
}


//4组注册-----------------------------------
function saveTwo() {
    var checkbox00 = document.getElementById('checkbox');
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();
    if (oPhonetest() && pwdtest() && pwdtest2() && telcode() && checkbox00.checked == true) {
        // if (oPhonetest() && checkbox00.checked == true && btnTxCode == txcode) {
        textCode0( $('.code_send0'));
        save0002()
    }
    else {
        layer.msg('您的注册账号、密码和短信验证都要正确填写,还有不要忘记阅读协议~~~', {icon: 5});
        // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});
    }
}

//4组红包注册-----------------------------------
function saveTwo_hongbao() {

    if (oPhonetest() && telcode()) {
        // if (oPhonetest() && checkbox00.checked == true && btnTxCode == txcode) {
        textCode0( $('.code_send0'));
        save0002_hongbao()
    }
    else {
        layer.msg('您的注册账号、密码和短信验证都要正确填写,还有不要忘记阅读协议~~~', {icon: 5});
        // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});
    }
}


//2组注册活动报名页面-----------------------------------
function saveTwo_resdit() {
    window.location.href = "/m/registerTwo";

    // var name = document.getElementById('name');
    // save0002_resdit()
    // if (oPhonetest() && name.value!="") {
    //     // if (oPhonetest() && checkbox00.checked == true && btnTxCode == txcode) {
    //     textCode0( $('.code_send0'));
    //     save0002_resdit()
    // }
    // else {
    //     layer.msg('您的姓名、手机号码都要正确填写~~~', {icon: 5});
    //     // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});
    // }
}

//2组跳转到注册活动报名页面-----------------------------------
function saveTwo_resdit_cc() {
    window.location.href = "http://www.ditingai.com/m/registerTwo";
}



//微信注册-----------------------------------
function wechat_register() {
    var checkbox00 = document.getElementById('checkbox');
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();

    if (oPhonetest() && pwdtest() && pwdtest2() && telcode() && checkbox00.checked == true) {
    // if (oPhonetest() && checkbox00.checked == true && btnTxCode == txcode) {
        textCode0( $('.code_send0'));
        wechat_save()
    }
    else {
        layer.msg('您的注册账号、密码和短信验证都要正确填写哦,还有不要忘记阅读协议~~~', {icon: 5});
        // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});

    }
}

//ASCII码编写的，，生成随机4位验证码。
function textCode0(obj) {
    var str1 = "";
    for (i = 0; i < 4; i++) {
        var iNum1 = parseInt(Math.random() * 123);
        while (!((0 <= iNum1 && iNum1 <= 9) || (65 <= iNum1 && iNum1 <= 90) || (97 <= iNum1 && iNum1 <= 123))) {
            iNum1 = parseInt(Math.random() * 123);
        }
        if (0 <= iNum1 && iNum1 <= 9) {
            str1 = str1 + iNum1;
        }
        else {
            str1 = str1 + String.fromCharCode(iNum1);
        }
        $(obj).val(str1)
    }
    return str1;
}
//注册处的4位随机验证码
function code_if() {
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();
    if (btnTxCode == txcode) {
        $('.txcode_i').css("display","none");
        onclickTimeOutR()
    } else {
        // layer.msg('请先正确填写验证码', {icon: 5})
        $('.txcode_i').css("display","block");
        $('.code_send0').val(textCode0())
    }
}

//注册时获取短信时的90s倒计时
var flag = false;
function onclickTimeOutR() {
    var phone = $("#phone").val();
    if (oPhonetest() == false) {
        // $("#phone").val('请正确填写手机号');
        return false;
    } else {
        if (flag) {
            return;
        }
        // 向后台发送处理数据
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/register/captchas/mobile/" + phone
        }).done(function (data) {
            layer.msg(data.message, {icon: 6}, function () {
                flag = true;
                var I = 90;
                var objTimeOut = setInterval(function () {
                    if (I == 0) {
                        //code="";
                        flag = false;
                        clearInterval(objTimeOut);
                        $('#btnCode').val('发送验证码');
                        $('#btnCode').attr("class", "_send");
                        $('.code_send0').val(textCode0())

                    } else {
                        $('#btnCode').attr("class", "_send_disable");
                        $('#btnCode').val(I + "秒");
                    }
                    I--;
                }, 1000);
            })
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    }
}

//1组注册----------------------------------
function save() {
    var account = {}
    account.userName = $("#phone").val();
    account.password = $("#pwd").val();
    account.mobile = $("#phone").val();
    account.verifyCode = $("#telcode").val();
    account.invitationCode = $("#InvitationR").val();
    account.source = 1
    console.log(account)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {icon: 6}, function () {
            window.location.href = "/login";
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        console.log(message);
        layer.msg(message, {icon: 5})
    });
}

//2组注册----------------------------------
function save2Z() {
    var account = {}
    account.userName = $("#phone").val();
    account.password = $("#pwd").val();
    account.mobile = $("#phone").val();
    account.verifyCode = $("#telcode").val();
    account.invitationCode = $("#InvitationR").val();
    account.source = 2
    console.log(account)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {icon: 6}, function () {
            window.location.href = "/login";
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        console.log(message);
        layer.msg(message, {icon: 5})
    });
}

//4组注册----------------------------------
function save0002() {
    var account = {}
    account.userName = $("#phone").val();
    account.password = $("#pwd").val();
    account.mobile = $("#phone").val();
    account.verifyCode = $("#telcode").val();
    account.invitationCode = $("#InvitationR").val();
    account.source = 4
    console.log(account)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {icon: 6}, function () {
            // window.location.href = "http://www.hdb.com/party/ysrob.html";
            window.location.href = "/login";
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        console.log(message);
        layer.msg(message, {icon: 5})
    });
}



//4组注册----------------------------------
function save0002_hongbao() {
    var account = {}
    account.userName = $("#phone").val();
    account.password = "123456";
    account.mobile = $("#phone").val();
    account.verifyCode = $("#telcode").val();
    account.invitationCode = $("#InvitationR").val();
    account.source = 4
    console.log(account)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {icon: 6}, function () {
            window.location.href = "https://fb.fanqier.cn/f/9b8ilk";
            // window.location.href = "/login";
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        console.log(message);
        layer.msg(message, {icon: 5})
    });
}

//2组活动报名----------------------------------
function save0002_resdit() {
    window.location.href = "/m/register_phoneTwo_resdit";

    // var account = {}
    // account.userName = $("#phone").val();
    // account.name = $("#name").val();
    // console.log(account)
    // $.ajax({
    //     type: "POST",
    //     contentType: "application/json",
    //     url: '/api/accounts/register/mobile',
    //     data: JSON.stringify(account)
    // }).done(function (data) {
    //     layer.msg(data.message, {icon: 6}, function () {
    //         window.location.href = "/m/register_phoneTwo_resdit";
    //     })
    // }).fail(function (data) {
    //     var message = JSON.parse(data.responseText).message;
    //     console.log(message);
    //     layer.msg(message, {icon: 5})
    // });
}



//拜年机器人部分
//所有者正则
function companyNameYear() {
    var r = /^[\u4E00-\u9FA5\(\)]{2,16}$/;
    var that = document.getElementById("companyName");
    if (r.test(that.value)) {
        that.style.color = "black";
        return true;
    } else {
        that.style.color = "red";
        return false;
    }
}
//拜年手机号
function oPhonetestY() {
    var that = document.getElementById("phone");
    var re = /^1[5|3|4|7|8][0-9]{9}$/;
    if (re.test(that.value)) {
        that.style.color = "black";
        $('.phone_i').css("display","none");
        return true;
    } else {
        //layer.alert("请输入正确手机号");
        that.style.color = "red";
        $('.phone_i').css("display","block");
        return false;
    }
}
//机器人名正则
function newYearRobotName() {
    var re = /^[\u4E00-\u9FA5]{2,5}$/;
    var res = /^[a-zA-Z0-9]{4,12}$/;
    var ress = /^[\u4E00-\u9FA5a-zA-Z0-9]{2,5}$/;
    var that = document.getElementById("name");
    if (re.test(that.value)) {
        that.style.color = "black";
        return true;
    } else if (res.test(that.value)) {
        that.style.color = "black";
        return true;
    } else if (ress.test(that.value)) {
        that.style.color = "black";
        return true;
    } else {
        that.style.color = "red";
        return false;
    }
}



function zidingyi(){
    $('#zidingyi').attr('checked',"checked")
    $('#zidingyi').val($('#welcomes').val())
}

function saveNewYear(url){
    if (oPhonetestY() && companyNameYear() && newYearRobotName() && $("#phone").val()!="" && $("#name").val()!="" && $("#companyName").val()!="" && $("#telcode").val()!="") {
        NewYear(url)
    }
    else {
        layer.msg('您有信息没有填完，或者变色处填错了哦~~~', {icon: 5});
    }
}




//拜年机器人注册
function NewYear(url){
    var account = {
        userName:$("#phone").val(),
        robotName:$('#name').val(),
        newYearGreetings:"hello",
        mobile: $("#phone").val(),
        realName:$('#companyName').val(),
        verifyCode:$("#telcode").val(),
        password:$("#password").val(),
        headImgUrl:url,
        sex:sex0,
        source:3
    }
   console.log(account)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {time:800,icon: 6}, function () {
            window.location.href = "/m/cornerAI_login"
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        console.log(message);
        layer.msg(message, {icon: 5})
    });
}

//微信注册----------------------------------
function wechat_save() {
    var account = {}
    account.userName = $("#phone").val();
    account.password = $("#pwd").val();
    account.mobile = $("#phone").val();
    account.verifyCode = $("#telcode").val();
    account.invitationCode = $("#InvitationR").val();
    account.openId = $("#open_id").val();
    account.unionId = $("#union_id").val();
    account.source = 1
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {icon: 6}, function () {
            window.location.href = "/login";
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        console.log(message);
        layer.msg(message, {icon: 5})
    });
}

//密码找回--------------------------------
function save02() {
    if (oPhonetest() && pwdtest() && pwdtest2() && telcode()) {
        saveFind()
    }
    else {
       // layer.msg('您的注册账号、密码和短信验证都要正确填写哦~~~', {icon: 5});
        tishi();
    }
}


//密码找回
function saveFind() {
    var account = {}
    account.mobile = $("#phone").val();
    account.newPassword = $("#pwd").val();
    account.verifyCode = $("#telcode").val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/password/reset',
        data: JSON.stringify(account)
    }).done(function (data) {
            alert("找回密码成功!")
            window.location.href = "/m/cornerAI_login";

    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
       layer.msg(message, {icon: 5})
        //tishi();
    });
}

//登录成功之后的密码改变重置--------------------------------
function save03() {
    if (pwdtest() && pwdtest2() && telcode()) {
        saveChange()
    }
    else {
        layer.msg('您的密码和短信验证都要正确填写哦~~~', {icon: 5});
    }
}

//密码重置
function saveChange() {
    var account = {}
    account.mobile = $("#phone").val();
    account.newPassword = $("#pwd").val();
    account.verifyCode = $("#telcode").val();
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/password/change',
        data: JSON.stringify(account)
    }).done(function (data) {
        layer.msg(data.message, {time:500,icon: 6}, function () {
            window.location.href = "/login";
        })
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        layer.msg(message, {icon: 5})
    });
}

//更改密码处的4位随机验证码
function code_if_change() {
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();
    if (btnTxCode == txcode) {
        $('.txcode_i').css("display","none");
        onclickTimeOut()
    } else {
        // layer.msg('请先正确填写验证码', {icon: 5})
        $('.txcode_i').css("display","block");
        $('.code_send0').val(textCode0())
    }
}

//更改密码获取短信时的90s倒计时
var flag = false;
function onclickTimeOut() {
    var phone = $("#phone").val();
    if (oPhonetest() == false) {
        // $("#phone").val('请填写手机号');
        return false;
    } else {
        if (flag) {
            return;
        }
        // 向后台发送处理数据
        $.ajax({
            type: "GET",
            contentType: "application/json",
            url: "/api/reset/captchas/mobile/" + phone, // 目标地址
            data: {
                //"code" : code
            }
        }).done(function (data) {
            layer.msg(data.message, {time: 500, icon: 6}, function () {
                flag = true;
                var I = 90;
                var objTimeOut = setInterval(function () {
                    if (I == 0) {
                        //code="";
                        flag = false;
                        clearInterval(objTimeOut);
                        $('#btnCode').val("发送验证码");
                        $('#btnCode').attr("class", "_send");
                        $('.code_send0').val(textCode0())
                    } else {
                        $('#btnCode').attr("class", "_send_disable");
                        $('#btnCode').val(I + "秒");
                    }
                    I--;
                }, 1000);
            })
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    }
}
