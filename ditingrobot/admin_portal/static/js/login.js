document.onkeydown = function(ev)
{
	var oEvent = ev||event;
	if(oEvent.keyCode == 13&&oEvent.ctrlKey||oEvent.keyCode == 13 )
	{
		save11();
	}
}



function save11() {
	 console.log("点击了登录");
	var account = $("#phone").val();

	var password = $("#pwd").val();

	if (account == "" || account == null) {
		layer.alert('账号不能为空', {icon: 5})

	} else if (password == "" || password == null) {
		layer.alert("密码不能为空",{icon: 5});

	} else {

		$.ajax({
			type: "POST",
			contentType: "application/json",
			url: '/api/login',
			data: JSON.stringify({'userName': account, 'password': password})
		}).done(function (data) {
			window.location.href = "/knowledge/a"
		}).fail(function (data) {
			var message=JSON.parse(data.responseText).message;
			layer.msg(message, {icon: 5});
		});
	}
}
