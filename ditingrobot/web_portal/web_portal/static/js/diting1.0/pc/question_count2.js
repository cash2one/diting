
function timess(timestamp3){
	var d = new Date(timestamp3 );    //根据时间戳生成的时间对象
	var Hour = d.getHours() > 9?d.getHours():"0"+d.getHours();
	var Min = d.getMinutes() > 9?d.getMinutes():"0"+d.getMinutes();
	var Sec = d.getSeconds() > 9?d.getSeconds():"0"+d.getSeconds();
	var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate()) + " " + (Hour) + ":" + (Min) + ":" + (Sec);
	return  times
}


//初始化用户管理界面（问答与统计）
function cshQuesCount() {
	$.ajaxSetup({cache: false})  //禁止ie浏览器读取缓存的ajax
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/company/chatlog/search-group?pageNo=" + 1
	}).done(function (data) {
		var data = JSON.parse(data);
		Paging_ques_count(data.total);
		var str = '<tr><th style="min-width: 100px;">序号</th> <th style="min-width: 160px;">用户名称</th> <th style="min-width: 185px;">用户类别</th> <th style="min-width: 130px;">对话总次数</th> <th style="min-width: 370px;">最近一次使用时间</th> <th style="min-width: 120px">查看记录</th> </tr>';
		for (var i = 0; i < data.items.length; i++) {
			var userClass = '';
			var times = timess(data.items[i].createdTime);

			if (data.items[i].extra4 == "1") {
				userClass = 'web机器人'
			} else {
				userClass = '微信机器人'
			}
			var loginUserName = '';
			if (data.items[i].loginUsername == undefined) {
				loginUserName = '游客'+(i + 1);
			} else {
				loginUserName = data.items[i].loginUsername;
			}
			str += '<tr><td>'+(i+1)+'</td> <td>'+loginUserName +'</td> <td>' + userClass + '</td> <td>' + data.items[i].count + '</td> <td>' + times + '</td> <td> <a  class="newsRecord" style="text-decoration:none;cursor: pointer;" href="/talk_message?uuid='+ data.items[i].uuid +'"><img src="/static/images/diting1.0/pc/icon_QA.png" style="width: 30px;margin:auto;background: #fff;"/></a> </td> </tr>';

		}

		if (data.items.length == 0) {
			var strxx = '<tr><th width="100">序号</th> <th width="160">用户名称</th> <th width="185">用户类别</th> <th width="130">对话总次数</th> <th width="317">最近一次使用时间</th> <th width="120">查看记录</th> </tr>';

			layer.msg("没有搜索到相应数据");
			$('#mt').html(strxx)

		} else {
			$('#mt').html(str)
		}

	}).fail(function (data) {
		console.info(data);

	});

}


//分页器，页码切换执行(用户管理页)
function Paging_ques_count(strips) { //参数是总计多少条
	$.ajaxSetup({cache: false})  //禁止ie浏览器读取缓存的ajax
	var x = $("#pageNo").val();
	$('#red').smartpaginator({

		totalrecords: strips,
		datacontainer: 'mt',
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
				url: "/api/company/chatlog/search-group?pageNo=" + $("#pageNo").val()
			}).done(function (data) {
				var data = JSON.parse(data);
				Paging_ques_count(data.total);
				var str = '<tr><th width="100">序号</th> <th width="160">用户名称</th> <th width="185">用户类别</th> <th width="130">对话总次数</th> <th width="317">最近一次使用时间</th> <th width="120">查看记录</th> </tr>';
				for (var i = 0; i < data.items.length; i++) {
					var userClass = '';
					var times = timess(data.items[i].createdTime);
					if (data.items[i].extra4 == "1") {
						userClass = 'web机器人'
					} else {
						userClass = '微信机器人'
					}
					var loginUserName = '';
					if (data.items[i].loginUsername == undefined) {
						loginUserName = '游客'+(i+1);
					} else {
						loginUserName = data.items[i].loginUsername;
					}
					str += '<tr><td>'+(i+1)+'</td> <td>' + loginUserName + '</td> <td>' + userClass + '</td> <td>' + data.items[i].count + '</td><td>' + times + '</td> <td> <a  class="newsRecord" style="text-decoration:none;cursor: pointer"  href="/talk_message?uuid='+ data.items[i].uuid +'"><img src="/static/images/diting1.0/pc/icon_QA.png" style="width: 30px;margin:auto;background: #fff;"/></a> </td>  </tr>';

					$('#mt').html(str)
				}


			}).fail(function (data) {
				console.info(data);

			});
		}
	});
}
//点击消息记录

function csh_talkMessage(){
	
	var newsrecordid =$('#messUuid').val();
	$('#thisuuid').val(newsrecordid);
	var str=' <div style="padding: 0.2rem;text-align: left;""> <input type="hidden" name="id" id="id" value=""> '
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/company/chatlog/searchpage?pageNo="+ 1 +"&uuid="+ newsrecordid
	}).done(function (data) {
		var data = JSON.parse(data);
		$("#pageNo").val(1);
		Paging_record(data.total);
		for (var i = 0; i < data.items.length; i++) {
			var times = timess(data.items[i].createdTime);
			str += '<p style="color: #00c4c2;font-size: 14px;margin: 5px 125px;">问：' + data.items[i].question + '&nbsp;&nbsp;&nbsp;&nbsp;' + times + '</p><p style="font-size: 14px;margin: 5px 125px;">答：' + data.items[i].answer + '</p>'

		}
		str +='</div>'
	
		$('#quesMessage').html(str)

	}).fail(function (data) {
		console.info(data);

	});

}


//分页器，页码切换执行（用户管理--消息记录窗口)
function Paging_record(strips){ //参数是总计多少条
	$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
	var x=$("#pageNo").val();
	$('#red').smartpaginator({

		totalrecords: strips,
		datacontainer: 'quesMessage',
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
		onchange:function(){
			//页面转换后知识内容加载
			var newsrecordid =$('#thisuuid').val();
			var str='<div ></div> <div style="padding: 0.2rem;text-align: left;"> <input type="hidden" name="id" id="id" value=""> '
			$.ajax({
				type: "GET",
				contentType: "application/json",
				url: "/api/company/chatlog/searchpage?pageNo="+ $("#pageNo").val() +"&uuid="+ newsrecordid
			}).done(function (data) {
				var data = JSON.parse(data);
				Paging_record(data.total);
				for (var i = 0; i < data.items.length; i++) {
					var times = timess(data.items[i].createdTime);
					str += '<p style="color: #00c4c2;font-size: 14px;margin: 5px 125px;">问：' + data.items[i].question + '&nbsp;&nbsp;&nbsp;&nbsp;' + times + '</p><p style="font-size: 14px;margin: 5px 125px;">答：' + data.items[i].answer + '</p>'

				}
				str +='</div>'
			
				$('#quesMessage').html(str)
			}).fail(function (data) {
				console.info(data);

			});
		}
	})
}


