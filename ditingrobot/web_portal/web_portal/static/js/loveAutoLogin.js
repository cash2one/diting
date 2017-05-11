/**
 * Created by Administrator on 2017/4/12/012.
 */

function autoLogin() {
    var account = localStorage.getItem("htzhzhht");
    var password = localStorage.getItem("htmmmmht");
    if(account==""&&account==undefined&&account==null){
        console.log("该用户之前未登录")
    }else{
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/login',
            data: JSON.stringify({'userName': account, 'password': password}),
            success:function (data) {
                console.log("自动登录成功")
               localStorage.setItem("htzhzhht",account)
                localStorage.setItem("htmmmmht",password)
                 $("#isLogin").html("ok")
            },
            error:function (data) {
                console.log(data,"自动登录失败！")
            }
        });
    }
}