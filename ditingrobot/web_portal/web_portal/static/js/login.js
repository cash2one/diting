document.onkeydown = function (ev) {
    var oEvent = ev || event;
    if (oEvent.keyCode == 13 && oEvent.ctrlKey || oEvent.keyCode == 13) {
        save11();
    }
}
//平台登录
function save11() {
    console.log("点击了登录");
    var account = $("#phone").val();
    var password = $("#pwd").val();
    if (account == "" || account == null) {
        layer.msg('账号不能为空', {icon: 5})
    } else if (password == "" || password == null) {
        layer.msg("密码不能为空", {icon: 5});
    } else {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/login',
            data: JSON.stringify({'userName': account, 'password': password})
        }).done(function (data) {
            //var message=JSON.parse(data).message;
            window.location.href = "/home/index"
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5});
        });
    }
}

//移动端登录
function save_phone() {
    var account = $("#phone").val();
    var password = $("#pwd").val();
    if (account == "" || account == null) {
        layer.msg('账号不能为空', {icon: 5})
    } else if (password == "" || password == null) {
        layer.msg("密码不能为空", {icon: 5});
    } else {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/login',
            data: JSON.stringify({'userName': account, 'password': password})
        }).done(function (data) {
            //var message=JSON.parse(data).message;
            window.location.href = "/m/home"
            // window.location.href = "/home/index"

        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5});
        });
    }
}


//转角AI移动端登录
function cornerAi_save() {
    var account = $("#phone").val();
    var password = $("#pwd").val();
    if (account == "" || account == null) {
        layer.msg('账号不能为空', {icon: 5})
    } else if (password == "" || password == null) {
        layer.msg("密码不能为空", {icon: 5});
    } else {

        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/login',
            data: JSON.stringify({'userName': account, 'password': password})
        }).done(function (data) {
            //var message=JSON.parse(data).message;
            window.location.href = "/m/newYearRegister"
            // window.location.href = "/home/index"

        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5});
        });
    }
}

//绑定登录
function bindwechat() {
    console.log("点击了登录");
    var account = $("#phone_login").val();
    var password = $("#pwd_login").val();
    var openId = $("#open_id").val();
    var unionId = $("#union_id").val();
    if (account == "" || account == null) {
        layer.msg('账号不能为空', {icon: 5})
    } else if (password == "" || password == null) {
        layer.msg("密码不能为空", {icon: 5});
    } else {
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/wx/api/wechat_bind',
            data: JSON.stringify({'userName': account, 'password': password,'openId': openId,'unionId':unionId})
        }).done(function (data) {
            //var message=JSON.parse(data).message;
            window.location.href = "/home/index"
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5});
        });
    }
}

//语义词库登录

