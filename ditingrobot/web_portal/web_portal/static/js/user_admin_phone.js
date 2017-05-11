
function timess(timestamp3){
	var d = new Date(timestamp3 );    //根据时间戳生成的时间对象
	var Hour = d.getHours() > 9?d.getHours():"0"+d.getHours();
	var Min = d.getMinutes() > 9?d.getMinutes():"0"+d.getMinutes();
	var Sec = d.getSeconds() > 9?d.getSeconds():"0"+d.getSeconds();
	var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate()) + " " + (Hour) + ":" + (Min) + ":" + (Sec);
	return  times
}




//初始化用户管理界面（问答与统计）
function csh_user_cou(){
	$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/company/chatlog/search-group?pageNo=" + 1
	}).done(function (data) {
		var data = JSON.parse(data);
		console.log(data)
		Paging_ques_count(data.total);
		var str = '<tr> <th>用户姓名</th> <th>用户类别</th> <th>对话次数</th> <th >记录</th> </tr>';
		for (var i = 0; i < data.items.length; i++) {
			var userClass ='';
			var times = timess(data.items[i].createdTime);

			if(data.items[i].extra4=="1"){
				userClass = 'web'
			}else{
				userClass = '微信'
			}

			str += '<tr> <td>游客 '+(i+1)+'</td> <td>'+userClass+'</td> <td>'+ data.items[i].count +'</td> <td> <a  class="newsRecord" style="text-decoration:none;cursor: pointer"  href="/m/user_message_phone?uuid='+ data.items[i].uuid +'">消息记录</a> </td>  </tr>';

		}

		if (data.items.length == 0) {
			var strxx = '<tr> <th>用户姓名</th> <th>用户类别</th> <th>对话次数</th> <th >记录</th> </tr>';

			layer.msg("没有搜索到相应数据");
			$('#userAdminPhone').html(strxx)
			
		} else {
			$('#userAdminPhone').html(str)
		}

	}).fail(function (data) {
		console.info(data);

	});

}


//分页器，页码切换执行(用户管理页)
function Paging_ques_count(strips){ //参数是总计多少条
	$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
	var x=$("#pageNo").val();
	$('#red').smartpaginator({

		totalrecords: strips,
		datacontainer: 'userAdminPhone',
		recordsperpage: 15,
		dataelement: 'tr',
		length: 1,
		next: '>',
		prev: '<',
		first: '<<',
		last: '>>',
		theme: 'red',
		initval:x,
		controlsalways: true,
		onchange:function(){
			//页面转换后知识内容加载
			$.ajax({
				type: "GET",
				contentType: "application/json",
				url: "/api/company/chatlog/search-group?pageNo=" + $("#pageNo").val()
			}).done(function (data) {
				var data = JSON.parse(data);
				Paging_ques_count(data.total);
				var str = '<tr> <th >姓名</th> <th>类别</th> <th>对话次数</th> <th >记录</th></tr>';
				for (var i = 0; i < data.items.length; i++) {
					var userClass ='';
					var times = timess(data.items[i].createdTime);
					if(data.items[i].extra4=="1"){
						userClass = 'web'
					}else{
						userClass = '微信'
					}

					str += '<tr> <td>游客 '+((($("#pageNo").val()-1)*15)+(i+1))+'</td> <td>'+ userClass +'</td> <td>'+ data.items[i].count +'</td> <td> <a  class="newsRecord" style="text-decoration:none;cursor: pointer"   href="/m/user_message_phone?uuid='+ data.items[i].uuid +'">消息记录</a> </td> </tr>';
					
					$('#userAdminPhone').html(str)
				}


			}).fail(function (data) {
				console.info(data);

			});
		}
	});
}


//点击消息记录

function csh_userMessage(){
	
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
			str +='<p style="padding: 0.1rem;">提问：</p><p style="color: #555;padding: 0.1rem;border: 1px solid #ed5a5a;border-radius: 0.15rem;margin-left: 0.3rem;font-size: 0.24rem;">'+ data.items[i].question +'<br/><span style="text-align: right;display: block;">'+ times +'</span></p><p style="padding: 0.1rem;">回答：</p><p style="color: #666;padding: 0.1rem;border: 1px solid #08bec2;border-radius: 0.15rem;margin-left: 0.3rem;font-size: 0.24rem;">'+ data.items[i].answer +'</p>'

		}
		str +='</div>'
	
		$('#userMessagePhone').html(str)

	}).fail(function (data) {
		console.info(data);

	});

}
function closenews(){
	window.location.reload(false);
}


//分页器，页码切换执行（用户管理--消息记录窗口)
function Paging_record(strips){ //参数是总计多少条
	$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
	var x=$("#pageNo").val();
	$('#red').smartpaginator({

		totalrecords: strips,
		datacontainer: 'userMessagePhone',
		recordsperpage: 15,
		dataelement: 'tr',
		length: 1,
		next: '>',
		prev: '<',
		first: '<<',
		last: '>>',
		theme: 'red',
		initval:x,
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
					str +='<p style="padding: 0.1rem;">提问：</p><p style=" color: #555;padding: 0.1rem;border: 1px solid #ed5a5a;border-radius: 0.15rem;margin-left: 0.3rem;font-size: 0.24rem;">'+ data.items[i].question +'<br><span style="text-align: right;display: block;">'+ times +'</span></p><p style="padding: 0.1rem;">回答：</p><p style="color: #666;padding: 0.1rem;border: 1px solid #08bec2;border-radius: 0.15rem;margin-left: 0.3rem;font-size: 0.24rem;">'+ data.items[i].answer +'</p>'

				}
				str +='</div>'
			
				$('#userMessagePhone').html(str)
			}).fail(function (data) {
				console.info(data);

			});
		}
	})
}


