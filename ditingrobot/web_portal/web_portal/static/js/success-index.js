function saveRob() {
    if ($('#name').val() != '' && $('#shortDomainName').val() != '' && rob__name() && rob_shortDomainName()) {
        save();
    } else {
        layer.msg('飘红处请正确填写哦！', {icon: 5})
    }
}

function companyName00() {
    var r = /^[\u4E00-\u9FA5\(\)（）]{2,16}$/;
    var that = document.getElementById("companyName");
    if (r.test(that.value)) {
        that.style.color = "#000";
        return true;
    } else {
        that.style.color = "red";
        return false;
    }
}

function save() {
    var isDisable = $("#isDisable option:selected").val();
    var invokeEnable = $("#invokeEnable option:selected").val();
    var name = $("#name").val();
    var shortDomainName = $("#shortDomainName").val();
    if($('#profile').val()!=""){
        var profile = $('#profile').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var profile = $('#profile').val()
    }
    if($('#welcomes').val()!=""){
        var welcomes = $('#welcomes').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var welcomes = $('#welcomes').val()
    }
    if($('#answer1').val()!=""){
        var answer1 = $('#answer1').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer1 = $('#answer1').val()
    }
    if($('#answer1').val()!=""){
        var answer2 = $('#answer2').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer2 = $('#answer2').val()
    }
    if($('#answer3').val()!=""){
        var answer3 = $('#answer3').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer3 = $('#answer3').val()
    }
    if($('#answer4').val()!=""){
        var answer4 = $('#answer4').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer4 = $('#answer4').val()
    }
    if($('#answer5').val()!=""){
        var answer5 = $('#answer5').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer5 = $('#answer5').val()
    }
    if($('#answer6').val()!=""){
        var answer6 = $('#answer6').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer6 = $('#answer6').val()
    }
    if($('#answer7').val()!=""){
        var answer7 = $('#answer7').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer7 = $('#answer7').val()
    }
    if($('#answer8').val()!=""){
        var answer8 = $('#answer8').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer8 = $('#answer8').val()
    }
    if($('#answer9').val()!=""){
        var answer9 = $('#answer9').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer9 = $('#answer9').val()
    }
    if($('#answer10').val()!=""){
        var answer10 = $('#answer10').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'")
    }else {
        var answer10 = $('#answer10').val()
    }
  
    var robot = {
        name: name,
        shortDomainName: shortDomainName,
        welcomes: welcomes,
        profile: profile,
        invalidAnswer1: answer1,
        invalidAnswer2: answer2,
        invalidAnswer3: answer3,
        invalidAnswer4: answer4,
        invalidAnswer5: answer5,
        invalidAnswer6: answer6,
        invalidAnswer7: answer7,
        invalidAnswer8: answer8,
        invalidAnswer9: answer9,
        invalidAnswer10: answer10,
        enable: isDisable,
        invokeEnable: invokeEnable
    };
    $.ajax({
        type: "POST",
        contentType: "application/json",
        url: '/api/robot/save',
        data: JSON.stringify(robot)
    }).done(function (data) {
        document.getElementById("name").style.color = "#000";
        layer.msg(data.message, {time: 800, icon: 6}, function () {
            window.location.reload(true);
        })
    }).fail(function (data) {
        console.log(data);
        var message = JSON.parse(data.responseText).message;
        layer.msg(message, {icon: 5})
    });
}
function csh_suc_index() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/get-robot-info"
    }).done(function (data) {
        var data = JSON.parse(data);
        $("#name").val(data.name);
        $("#shortDomainName").val(data.shortDomainName);
        $("#shortDName").html(data.shortDomainName);
        if(data.profile!=""){
            $("#profile").val(data.profile.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#profile').val()
        }
        if(data.welcomes!=""){
            $("#welcomes").val(data.welcomes.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#welcomes').val()
        }
        if(data.invalidAnswer1!=""){
            $("#answer1").val(data.invalidAnswer1.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer1').val()
        }
        if(data.invalidAnswer2!=""){
            $("#answer2").val(data.invalidAnswer2.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer2').val()
        }
        if(data.invalidAnswer3!=""){
            $("#answer3").val(data.invalidAnswer3.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer3').val()
        }
        if(data.invalidAnswer4!=""){
            $("#answer4").val(data.invalidAnswer4.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer4').val()
        }
        if(data.invalidAnswer5!=""){
            $("#answer5").val(data.invalidAnswer5.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer5').val()
        }
        if(data.invalidAnswer6!=""){
            $("#answer6").val(data.invalidAnswer6.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer6').val()
        }
        if(data.invalidAnswer7!=""){
            $("#answer7").val(data.invalidAnswer7.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer7').val()
        }
        if(data.invalidAnswer8!=""){
            $("#answer8").val(data.invalidAnswer8.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer8').val()
        }
        if(data.invalidAnswer9!=""){
            $("#answer9").val(data.invalidAnswer9.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer9').val()
        }
        if(data.invalidAnswer10!=""){
            $("#answer10").val(data.invalidAnswer10.replace(/\&lt/g,"<").replace(/\&gt/g,">"));
        }else {
            $('#answer10').val()
        }
        if (data.enable) {
            $("#isDisable .option2").attr("selected", "selected");
        } else {
            $("#isDisable .option1").attr("selected", "selected");
        }
        if (data.invokeEnable) {
            $("#invokeEnable .option22").attr("selected", "selected");
        } else {
            $("#invokeEnable .option11").attr("selected", "selected");
        }
    }).fail(function (data) {
        console.log(data);
    });
}
function rob_shortDomainName() {
    var r = /^[a-zA-Z0-9]{3,30}$/;
    var that = document.getElementById("shortDomainName");
    if (r.test(that.value)) {
        that.style.color = "#000";
        $('#DomainN').css('display','none');
        return true;
    } else {
        that.style.color = "red";
        $('#DomainN').css('display','block');
        return false;
    }
}

function rob__name() {
    var re = /^[\u4E00-\u9FA5]{2,5}$/;
    var res = /^[a-zA-Z0-9]{4,12}$/;
    var ress = /^[\u4E00-\u9FA5a-zA-Z0-9]{4,5}$/;
    var that = document.getElementById("name");
    if (re.test(that.value)) {
        that.style.color = "#000";
        $('#RobotN').css('display','none');
        return true;
    } else if (res.test(that.value)) {
        that.style.color = "#000";
        $('#RobotN').css('display','none');
        return true;
    } else if (ress.test(that.value)) {
        that.style.color = "#000";
        $('#RobotN').css('display','none');
        return true;
    } else {
        that.style.color = "red";
        $('#RobotN').css('display','block');
        return false;
    }
}
//一个汉字占三个字符
//var inputNum = document.getElementById('name').value.replace(/[^\x00-\xff]/g,"***").length;
function three3(){
    var str = '<div style="padding:0 15px;width: 450px;height: 360px;background: #fff;position: fixed;top: 26%;left: 44%;overflow-x:hidden;overflow-y: auto;border: 1px solid #ccc;z-index: 100;cd"> <div style="height: 42px;font-weight:900;font-size: 18px;line-height: 42px;margin-right: -8px;">应用<a class="fb-close" id="fb_close_x" onclick="close_three3()">×</a></div><div id="three33" style="overflow-y: auto;height: 280px;"></div><div style="width: 85%;position: absolute;bottom:4px;font-size: 16px;"><input type="checkbox" name="allThree" id="allThree"/>全选<button id="Qbtn">提交</button></div></div>'
    $('#tanc').html(str);
    var strx ='';
     $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/apis/search-validity"
    }).done(function (data) {
        var data1 = JSON.parse(data);
        for (var i = 0; i < data1.length; i++) {
           strx += '<input class="threeButton threeButtonG" type="button" value="'+ data1[i].name +'" data-mn="0" data-id="'+ data1[i].id +'"/>';
            $('#three33').html(strx)
        }
         //获取已经选择的应用
         $.ajax({
             type: "GET",
             contentType: "application/json",
             url: "/api/ex-applications/search-checked"
         }).done(function (data) {
             var data2 = JSON.parse(data);
             var data3 =new Array()
             $(".threeButton").each(function () {
                 data3.push($(this).attr("data-id"));
             });
               var tab1 = arrayIntersection(data2,data3)
         }).fail(function (data) {
             console.log(data);
         });
            open_three3()
    }).fail(function (data) {
        console.log(data);
    });
}
function arrayIntersection(a, b) { //获取数组交集
    var ai=0, bi=0;
    var result = new Array();
    while( ai < a.length && bi < b.length )
    {
        if (a[ai] < b[bi] ){ ai++; }
        else if (a[ai] > b[bi] ){ bi++; }
        else  {
            result.push(a[ai]);
            $(".threeButton").eq(bi).removeClass("threeButtonG").addClass("threeButtonB").attr("data-mn","1");
            ai++;
            bi++;
        }
    }
    return result;
}

function close_three3(){
    $('#tanc').html("")
}
function open_three3(){ //弹出选择启用第三方后的操作
//   是否选中    
    $(".threeButton").click(function(){ 
        if($(this).attr("data-mn")=="0"){
            $(this).removeClass("threeButtonG").addClass("threeButtonB").attr("data-mn","1");
        }else  {
            $(this).removeClass("threeButtonB").addClass("threeButtonG").attr("data-mn","0");
        }
    });
//全选
    $("#allThree").click(function () {
        if (this.checked == true) {
            $(".threeButton").removeClass("threeButtonG").addClass("threeButtonB").attr("data-mn","1");
        } else {
            $(".threeButton").removeClass("threeButtonB").addClass("threeButtonG").attr("data-mn","0");
        }
    });
//    启用提交键
    $('#Qbtn').click(function(){
        var OpenList = new Array();
        $(".threeButtonB").each(function () {
            OpenList.push($(this).attr("data-id"));
        });
        var CloseList = new Array();
        $(".threeButton").each(function () {
            CloseList.push($(this).attr("data-id"));
        });
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/ex-applications/setting',
            data: JSON.stringify({'openIds': OpenList,'closedIds': CloseList})
        }).done(function (data) {
            layer.msg(data.message, {time:1000,icon: 6},function (){
                close_three3()
            })
        }).fail(function (data) {
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    })
}












