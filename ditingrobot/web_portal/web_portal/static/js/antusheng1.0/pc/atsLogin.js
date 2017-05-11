function oPhonetest() {                          //手机号检测
    var that = document.getElementById("phone");
    var re = /^1[5|3|4|7|8][0-9]{9}$/;
    if (re.test(that.value)) {
        that.style.color = "#ccc";
        $('#phone_ts').css("display","none");
        $("#phone").css("border","1px solid #ccc")
        return true;
    } else {
        that.style.color = "red";
        $('#phone_ts').css("display","block");
        $("#phone").css("border","1px solid red")
        return false;
    }
}
function pwdtest() {                              //密码检测
    var that = document.getElementById("pwd");
    var re = /^\w{8,16}$/;
    // var re = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$/;
    if (re.test(that.value)) {
        that.style.color = "#ccc";
        $('#pwd_ts').css("display","none");
        $("#pwd").css("border","1px solid #ccc")
        return true;
    } else {
        that.style.color = "red";
        $('#pwd_ts').css("display","block");
        $("#pwd").css("border","1px solid red")
        return false;
    }
}
function pwdtest2() {         //密码一致检测
    var oPwd = document.getElementById("pwd");//密码
    var that = document.getElementById("pwd2");//确认密码
    if (that.value == oPwd.value) {
        that.style.color = "#ccc";
        $('#pwd2_ts').css("display","none");
        $("#pwd2").css("border","1px solid #ccc")
        return true;
    } else {
        //layer.alert("两次密码需要一致");
        that.style.color = "red";
        $('#pwd2_ts').css("display","block");
        $("#pwd2").css("border","1px solid red")
        return false;
    }
}
function telcode() {          //验证码检测
    var that = document.getElementById("telcode");
    if (that.value == "") {
        //layer.alert("请输入正确验证码");
        that.style.color = "red";
        return false;
    } else {
        that.style.color = "#ccc";
        return true;
    }
}
//ASCII码编写的，，生成随机4位验证码。
function textCode0() {
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
        $("#btnTxCode").val(str1)
    }
    return str1;
}

function code_if() {                      //获取验证码
    var btnTxCode = $('#btnTxCode').val().toLowerCase();   //校验码值
    var txcode = $('#txcode').val().toLowerCase();           //验证码值
    if (btnTxCode == txcode) {
        $('#txcode_ts').css("display","none");
        $("#txcode").css("border","1px solid #ccc")
        onclickTimeOutR()
    } else {
        $('#txcode_ts').css("display","block");
        $("#txcode").css("border","1px solid red")
        textCode0()
    }
}
function code_if_change() {
    onclickTimeOut()
}
var flag = true;                   //忘记密码90倒计时
function onclickTimeOut() {
    //console.log("获取验证码")
    var phone = $("#phone").val();
    if (oPhonetest() == false) {
        return false;
    } else {
        if (flag==true) {
            flag=false;
            console.log("已经获取")
           $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/reset/captchas/mobile/" + phone, // 目标地址
                data: {
                //"code" : code
                }
             }).done(function (data) {
                alert(data.message)
                //flag = true;
                var I = 90;
                var objTimeOut = setInterval(function () {
                    if (I == 0) {
                        flag = true;
                        clearInterval(objTimeOut);
                        $('#btnCode').val("发送验证码");
                    } else {
                        $('#btnCode').html(I + "秒");
                    }
                    I--;
                }, 1000);
             }).fail(function (data) {
                var message = JSON.parse(data.responseText).message;
                alert(message)
               flag=true;
            });
        }else {

        }

    }
}
var flag = true;                    //注册获取90s倒计时
function onclickTimeOutR() {
    console.log("获取验证码")
    var phone = $("#phone").val();
    if (oPhonetest() == false) {
        return false;
    } else {
        if (flag==true) {
            flag=false
            console.log("已经获取")
                $.ajax({
                type: "GET",
                contentType: "application/json",
                 url: "/api/register/captchas/mobile/" + phone
            }).done(function (data) {
                alert("获取验证码成功！")
                var I = 90;
                var objTimeOut = setInterval(function () {
                    if (I == 0) {
                        //code="";
                        flag = true;
                        clearInterval(objTimeOut);
                        $('#btnCode').val('发送验证码');
                        textCode0();

                    } else {
                        //$('#btnCode').attr("class", "_send_disable");
                        $('#btnCode').html(I + "秒");
                    }
                    I--;
                }, 1000);

            }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            alert(message)
            });
        }else{

        }
    }
}

//安图声登录
function atsLogin() {
    var account = $("#phone").val();
    var password = $("#pwd").val();
    if (account == "" || account == null) {
        alert("账户不能为空")
    } else if (password == "" || password == null) {
        alert("密码不能为空")
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/login',                                 //安图声登录接口
            data: JSON.stringify({'userName': account, 'password': password})
        }).done(function (data) {
            console.log("登录成功")
            window.location.href = "/home/index"

        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            alert(message)
        });
    }
}
function register2() {
    var account = {}
    account.userName = $("#phone").val();
    account.password = $("#pwd").val();
    account.mobile = $("#phone").val();
    account.verifyCode = $("#telcode").val();
    //account.invitationCode = $("#InvitationR").val();
    account.source = 2
    console.log(account)
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/accounts/register/mobile',
        data: JSON.stringify(account)
    }).done(function (data) {
        alert("注册成功！")
            window.location.href = "/atsLogin"
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        //console.log(message);
        alert(message)
    });
}

//安图声注册
function atsRegister() {
    var btnTxCode = $('#btnTxCode').val().toLowerCase();
    var txcode = $('#txcode').val().toLowerCase();  //转为小写
    if (oPhonetest() && pwdtest() && pwdtest2() && telcode()) {
        //textCode0( $('.code_send0'));
        register2()
    }
    else {
        //layer.msg('您的注册账号、密码和短信验证都要正确填写,还有不要忘记阅读协议~~~', {icon: 5});
        // layer.msg('请您正确填写验证码,还有不要忘记阅读协议~~~', {icon: 5});
    }
}

//安图声密码找回
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
        alert(data.message)
        //layer.msg(data.message, {icon: 6}, function () {
            window.location.href = "/atsLogin";
        //})
    }).fail(function (data) {
        var message = JSON.parse(data.responseText).message;
        //layer.msg(message, {icon: 5})
        alert(message)
    });
}
//密码找回输入检测
function atsForget() {
    if (oPhonetest() && pwdtest() && pwdtest2() && telcode()) {
        saveFind()
    }
    else {
        //alert("信息填写有误！")
    }
}