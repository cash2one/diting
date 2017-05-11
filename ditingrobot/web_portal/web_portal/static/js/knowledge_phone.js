
function timess(timestamp3){
	var d = new Date(timestamp3 );    //根据时间戳生成的时间对象
	var Hour = d.getHours() > 9?d.getHours():"0"+d.getHours();
	var Min = d.getMinutes() > 9?d.getMinutes():"0"+d.getMinutes();
	var Sec = d.getSeconds() > 9?d.getSeconds():"0"+d.getSeconds();
	var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate()) + " " + (Hour) + ":" + (Min) + ":" + (Sec);
	return  times
}



function phone_csh_KN(){
	$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
	//初始化知识库
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/company/knowledge/search-page?pageNo=" + 1
	}).done(function (data) {
		var data = JSON.parse(data);
		// console.log(data)
		PagingKnowledgePhone(data.total);
		var str = '';
		for (var i = 0; i < data.items.length; i++) {
			var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
			var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
			var actionOption = "";
			if(data.items[i].actionOption == null){
				actionOption = "action_0";
			}else {
				actionOption = data.items[i].actionOption;
			}
			str += '<li class="knowsLi1"> <ul class="edits" style="overflow-y: auto;width: 2.7rem;height: 1.7rem" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '" data-img_url="'+ data.items[i].img_url +'"> <li class="knowsLi11">问题:</li> <li class="knowsLi12">' + data.items[i].question + '</li> <li class="knowsLi11">答案:</li> <li class="knowsLi12">' + data.items[i].answer + '</li> </ul> <div><input type="button" title="删除" class="deletePhoneKnow" name="deletes" data-value="' + data.items[i].id + '"> <span class="knowledgeTime">'+times+'</span></div> </li>'

			$('#knowledgeBox').html(str)
		}
		if (data.items.length == 0) {
			var strxx = '您还没有建立知识库，请点击右下角添加您的第一条知识吧！';
			layer.msg("没有搜索到相应数据");
			$('#knowledgeBox').html(strxx)
		} else {
			$('#knowledgeBox').html(str)
		}
		success();
	}).fail(function (data) {
		console.log(data);
	});


}

//分页器，页码切换执行(移动端知识库)
function PagingKnowledgePhone(strips){ //参数是总计多少条
	$.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
	
	var x=$("#pageNo").val();
	$('#red').smartpaginator({

		totalrecords: strips,
		datacontainer: 'knowledgeBox',
		recordsperpage: 15,
		dataelement: 'li',
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
			var selectques = $('#selectques').val();
			var navsearchinput = encodeURI($('#nav-search-input').val())
			$.ajax({
				type: "GET",
				contentType: "application/json",
				url: "/api/company/knowledge/search-page?pageNo="  + $("#pageNo").val() + "&keywords=" + navsearchinput + "&type=" + selectques
			}).done(function (data) {
				var data = JSON.parse(data);
				console.log(data)
				PagingKnowledgePhone(data.total);
				var str = '';
				for (var i = 0; i < data.items.length; i++) {
					var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
					var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
					var actionOption = "";
					if(data.items[i].actionOption == null){
						actionOption = "action_0";
					}else {
						actionOption = data.items[i].actionOption;
					}
					str += '<li class="knowsLi1"> <ul class="edits" style="overflow-y: auto;width: 2.7rem;height: 1.7rem" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '" data-img_url="'+ data.items[i].img_url +'"> <li class="knowsLi11">问题:</li> <li class="knowsLi12">' + data.items[i].question + '</li> <li class="knowsLi11">答案:</li> <li class="knowsLi12">' + data.items[i].answer + '</li> </ul> <div><input type="button" title="删除" class="deletePhoneKnow" name="deletes" data-value="' + data.items[i].id + '"> <span class="knowledgeTime">'+times+'</span></div> </li>'

					$('#knowledgeBox').html(str)
				}
success();
			}).fail(function (data) {
				console.info(data);

			});
		}
	});
}

function success() { //知识库返回值成功获取--后执行的函数
	// 编辑弹窗<juning>
	$(".edits").click(function () {
		var knowledgeId = $(this).attr('data-value');
		var question = $(this).attr('data-question').replace(/\&lt/g,"<").replace(/\&gt/g,">");
		var answer = $(this).attr('data-answer').replace(/\&lt/g,"<").replace(/\&gt/g,">");
		var scene = $(this).attr('data-scene');
		var addImgUrl = $(this).attr('data-img_url');
		var actionOption = $(this).attr('data-actionOption').replace("action_","");
		var str=''
		str +='<div id="editsDiv" style="background: url(/static/images/phone/niupiBJ.png) repeat ;height: 11rem;position: absolute;"> <header class="header1"> <a href="javascript:void(0)" style="display: block;position: absolute; left: 0.1rem;top: 0.15rem;font-size: 0.22rem;padding: 0.1rem 0.05rem;color: #fff;z-index:11;"><div style="width: 0.4rem;height: 0.5rem; background: #08bec2;" ></div></a> 编辑问题 </header> <ul class="phone_robotSet_ul"> <li style="padding-left: 0.46rem;"> 问题：<input type="text" style="border-bottom: 1px solid #ccc;margin-bottom: 0.4rem;" name="question" required="required" id="question"  placeholder="问题最多60位"  maxlength="60" oninput="changeInputQuestion()"/> <div style="width: 1rem;float: right; position: relative;bottom: -0.5rem;right: -0.1rem;color: #ccc;"><input type="text" readonly="readonly" id="quesLength" value="60" style="width: 0.48rem;text-align: right;color: #ccc;font-size: 0.22rem;font-weight: 100;"/><span style="font-weight: 100;">/60</span></div> </li> <li style="padding-left: 0.46rem;"> 答案：<input type="text" style="border-bottom: 1px solid #ccc;margin-bottom: 0.5rem;" placeholder="答案最多240位" name="answer" required="required" id="answer"  maxlength="240" oninput="changeInputAnswer()"/> <div style="width: 1rem;float: right; position: relative;bottom: -0.5rem;right: -0.1rem;color: #ccc;"><input type="text" readonly="readonly" id="answerLength"  value="240" style="width: 0.4rem;text-align: right;color: #ccc;font-size: 0.22rem;font-weight: 100;"/><span style="font-weight: 100;">/240</span></div> </li> <li style="padding-left: 0.46rem;"> <div style=" width: 0.7rem;float: left;">图片<br>答案:</div> <form action="/upload/inputImage" name="userForm" id="userForm" method="post" enctype="multipart/form-data"> <div style="width: 5rem;float: left;"> <input type="file" accept="image/gif,image/jpeg,image/png" id="inputImage" name="file" multiple style="width: 3.4rem;"/> <input  type="button" value="确定" onclick="uploadImageAdd()"/> <div style="width: 1.6rem;height: 0.96rem;margin: 0.02rem;"> <img id="img-yuLan" src="/static/images/yuLan.jpg" alt="/static/images/yuLan.jpg" style="max-width: 1.6rem;max-height: 0.96rem;" onclick="answerImgBig(this.alt)"> <div style="display: none;"><input id="addImgUrl" type="text" /></div> </div> </div> <div style="clear: both;"></div> </form> </li> <li style="padding-left: 0.46rem;"> 场景：<input type="text" name="scene" id="add_scene" required="required" maxlength="20" value="" title="场景" style="border-bottom: 1px solid #ccc;margin-bottom: 0.2rem;" /> <p style="margin-bottom: 0.15rem;font-weight: 100;"> <span style="color: orange;">提示：</span>对于一个问题的多种不同表达，不用重复添加。例如：“你们公司在哪里”“告诉我你们在哪里”“你们在哪”“你们在什么地方”这些只用添加任意一个就行。</p> </li> </ul> <div style="text-align: center;"> <input class="phone_saveRob btn2" type="button" value="保存" style="width: 1.4rem"> <input class="phone_saveRob btn3" type="button" value="取消" style="width: 1.4rem;margin-left: 1.6rem;"> </div> </div>'
		$('#editsTC').html(str)
		$('#question').val(question);
		$('#answer').val(answer);
		$('#addImgUrl').val(addImgUrl);
		if($(this).attr('data-img_url') != "null"){
			$('#img-yuLan').attr("src",addImgUrl+'?imageMogr2/thumbnail/150x150')
			$('#img-yuLan').attr("alt",addImgUrl)
		}else {
			$('#img-yuLan').attr("src","/static/images/yuLan.jpg")
			$('#img-yuLan').attr("alt","/static/images/yuLan.jpg")
		}
		if($(this).attr('data-actionOption') == "null"){
			$("#action_change").val("0")
		}else {
			$("#action_change").val(actionOption);
		}
		$('#editscene').val(scene);
		$('.btn2').click(function () { //保存
			var newquestion = $('#question').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
			var newanswer = $('#answer').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
			var newscene = $('#scene').val();
			var actionOption = "action_"+$("#action_change").val();
			var changeImgUrl =  $('#addImgUrl').val();
			var editknows = {
				question: newquestion,
				actionOption:actionOption,
				answer: newanswer,
				scene: newscene,
				img_url:changeImgUrl,
				id: knowledgeId
			};
			// console.log(editknows)
			if ($('#editquestion').val() != "" && $('#editanswer').val() != "" && actionOption !="" && $("#action_change").val() != "") {
				//保存把数据传到后台
				// $("#action_change").val().indexOf("action_")==0
				$.ajax({
					type: "POST",
					contentType: "application/json",
					url: '/api/knowledge/update',
					data: JSON.stringify(editknows)
				}).done(function (data) {
					layer.msg(data.message, {time:500,icon: 6},function(){
						closeX()
						changeQuestion()
					})
				}).fail(function (data) {
					console.log(data);
					var message = JSON.parse(data.responseText).message;
					layer.msg(message, {icon: 5})
				});
			} else {
				layer.msg("问题和答案还有动作是必填项哦！", {icon: 5})
			}

		});
		$('.btn3').click(function () {//取消
			closeX()
		});
		function closeX(){
			$('#editsTC').html("")
		}
	});
	//删除这条数据
	$(".deletePhoneKnow").click(function () {
		var knowledgeId = $(this).attr('data-value');
		layer.confirm('您真的确定要删除了吗？', {
			btn: ['确定', '再考虑一下'] //按钮
		}, function () {
			$.ajax({
				type: "POST",
				contentType: "application/json",
				url: '/api/knowledge/delete/' + knowledgeId

			}).done(function (data) {
				layer.msg(data.message, {icon: 6})
				changeQuestion()
				// parent.history.go(0);//取消键刷新页面
			}).fail(function (data) {
				console.log(data);
				var message = JSON.parse(data.responseText).message;
				layer.msg(message, {icon: 5})
			});

		}, function () {

		});
	});
}


//知识库学习页面的搜索功能
function changeQuestion() {
	var selectques = $('#selectques').val();
	var navsearchinput = encodeURI($('#nav-search-input').val())
	$("#pageNo").val(1)
	//初始化知识库
	$.ajax({
		type: "GET",
		contentType: "application/json",
		url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val()  + "&keywords=" + navsearchinput + "&type=" + selectques
	}).done(function (data) {
		var data = JSON.parse(data);
		// console.log(data)
		PagingKnowledgePhone(data.total);
		var str = '';
		for (var i = 0; i < data.items.length; i++) {
			var d = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
			var times = (d.getFullYear()) + "/" + (d.getMonth() + 1) + "/" + (d.getDate());
			var actionOption = "";
			if(data.items[i].actionOption == null){
				actionOption = "action_0";
			}else {
				actionOption = data.items[i].actionOption;
			}
			str += '<li class="knowsLi1"> <ul class="edits" style="overflow-y: auto;width: 2.7rem;height: 1.7rem" data-question="' + data.items[i].question + '" data-actionOption="' + data.items[i].actionOption + '" data-answer="' + data.items[i].answer + '" data-scene="' + data.items[i].scene + '" data-value="' + data.items[i].id + '" data-img_url="'+ data.items[i].img_url +'"> <li class="knowsLi11">问题:</li> <li class="knowsLi12">' + data.items[i].question + '</li> <li class="knowsLi11">答案:</li> <li class="knowsLi12">' + data.items[i].answer + '</li> </ul> <div><input type="button" title="删除" class="deletePhoneKnow" name="deletes" data-value="' + data.items[i].id + '"> <span class="knowledgeTime">'+times+'</span></div> </li>'

			$('#knowledgeBox').html(str)
		}
		if (data.items.length == 0) {
			var strxx = '您还没有建立知识库，请点击右下角添加您的第一条知识吧！';
			layer.msg("没有搜索到相应数据");
			$('#knowledgeBox').html(strxx)
		} else {
			$('#knowledgeBox').html(str)
		}
		success();
	}).fail(function (data) {
		console.log(data);
	});
}

function changeInputAnswer(){
	var now = $('#answer').val().length
	var v = 240 - now
	$('#answerLength').val(v)
}
function changeInputQuestion(){
	var now = $('#question').val().length
	var v = 60 - now
	$('#quesLength').val(v)
}
