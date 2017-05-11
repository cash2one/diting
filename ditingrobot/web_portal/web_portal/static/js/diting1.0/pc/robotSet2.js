function saveRob() {
    if ($('#name').val() != '' && $('#companyName').val() != ''&& $('#shortDomainName').val() != '' && rob__name() && rob_shortDomainName() && companyName00() ) {
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
    var isDisable = $('#isDisable').val();
    var companyName = $('#companyName').val();
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
    var industry = $("#industry option:selected").val();
    var robot = {
        name: name,
        companyName: companyName,
        shortDomainName: shortDomainName,
        industry: industry,
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
        enable: isDisable
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
        console.log(data);
        $("#name").val(data.name);
        $("#shortDomainName").val(data.shortDomainName);
        $("#shortDName").html(data.shortDomainName);
        $('#companyName').val(data.companyName);
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
        $('#isDisable').val(data.enable)
        if (data.enable) {
            $("input[type='radio'][name='isDisable']").parent().removeClass("transRadio")
            //给自己对应的label
            $("#isDisableTrue").parent().addClass("transRadio");
        } else {
            $("input[type='radio'][name='isDisable']").parent().removeClass("transRadio")
            //给自己对应的label
            $("#isDisableFalse").parent().addClass("transRadio");
        }
        if(data.industry == null){
            console.log(data.industry)
        }else {
            $("#industry").val(data.industry);
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












