
   function knows_add(){//保存
        var question = $("#question").val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
        var answer = $("#answer").val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
        var addImgUrl = $('#addImgUrl').val();
        var actionOption = "action_" + $("#action_add").val();
        var add_scene = $("#add_scene").val()
        var knows = {
            question:question,
            actionOption:actionOption,
            answer:answer,
            img_url:addImgUrl,
            scene:add_scene
        };
        if(question !="" && answer !="" ){
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: '/api/knowledge/save',
                data: JSON.stringify(knows)
            }).done(function (data) {
                data =	JSON.parse(data)
                if(data.result=="添加成功"){
                    layer.msg(data.result, {time:1000,icon: 6},function(){
                        //关闭后的操作
                        parent.window.location.reload(false);
                    })
                }else {
                    layer.open({
                        type: 1,
                        shade: false,
                        title: false, //不显示标题
                        content:data.result, //捕获的元素，注意：最好该指定的元素要存放在body最外层，否则可能被其它的相对元素所影响
                        cancel: function(){
                        }
                    });
                }

            }).fail(function (data) {
                console.log(data);
                var message=JSON.parse(data.responseText).message;
                layer.msg(message, {icon: 5})
            });
        }else {
            layer.msg("问题和答案都是必填项哦！", {time:1000,icon: 5}, function () {
               
            })
        }
    }
   
function changeInput(tt,box,max){
    var now = tt.val().length
    var v = max - now
    box.val(v)
}
 