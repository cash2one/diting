
function save() {
    if($("#company_name").val() == "" &&
        $("#company_web").val() == ""  &&
        $("#company_site").val() == "" &&
        $("#company_bus").val() == "" &&
        $("#company_tel").val() == "" &&
        $("#company_work").val() == "" &&
        $("#company_wx").val() == "" &&
        $("#company_size").val() == "" &&
        $("#company_abstract").val() == "" &&
        $("#product_suggest").val() == "" &&
        $("#skill_well").val() == "" &&
        $("#now_industry").val() == "" &&
        $("#development").val() == "" &&
        $("#company_culture").val() == "" &&
        $("#work_time").val() == "" &&
        $("#company_leader").val() == ""&&
        $("#morMoney").val() == "" &&
        $("#hot_positions").val() == ""
    ){
        layer.msg("请您至少填写一项内容", {icon: 5})
    }else {
        var company_name = $("#company_name").val();
        var company_web = $("#company_web").val();
        var company_site = $("#company_site").val();
        var company_bus = $("#company_bus").val();
        var company_tel = $("#company_tel").val();
        var company_work = $("#company_work").val();
        var company_wx = $("#company_wx").val();
        var company_size = $("#company_size").val();
        var company_abstract = $("#company_abstract").val();
        var product_suggest = $("#product_suggest").val();
        var skill_well = $("#skill_well").val();
        var now_industry = $("#now_industry").val();
        var development = $("#development").val();
        var company_culture = $("#company_culture").val();
        var work_time = $("#work_time").val();
        var company_leader = $("#company_leader").val();

        var morMoney = $("#morMoney").val();
        var hot_positions = $("#hot_positions").val();
        <!--公司名称；公司网址；所在行业；融资状况？；公司规模；公司概况；热招职位？；工作时间；工作地点；-->
        var companys = {
            companyName: company_name, //公司名称
            companyUrl: company_web, //公司网址
            companyAddress: company_site, //公司地址；工作地点
            busLine: company_bus, //公交路线
            companyTel: company_tel, //公司电话
            companyBusiness: company_work, //公司业务
            companyWeChat: company_wx, //公司微信
            companySize: company_size, //公司规模
            companyProfile: company_abstract, //公司简介；概况
            productIntroduction: product_suggest, //产品介绍
            technologyAdvantage: skill_well, //技术优势
            industryWhere: now_industry, //所在行业
            developmentProspect: development, //发展前景
            corporateCulture: company_culture, //企业文化
            workShift: work_time, //上班时间；工作时间
            companyLeadership: company_leader, //公司领导
            financingSituation:morMoney,  //    融资情况
            hotPositions:hot_positions   //    热招职位
        };
        console.log(companys);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/knowledge/company/base_knowledge_info',
            data: JSON.stringify(companys)
        }).done(function (data) {
            console.log(data);
            layer.msg(data.message, {icon: 6})
        }).fail(function (data) {
            console.log(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    }
}
//加载公司名称和地址
function csh_name_place(){
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/get-company-info"
    }).done(function (data) {
        var data = JSON.parse(data);
        $("#company_name").val(data.name);
    }).fail(function (data) {
        //	console.log(data);
    });
}

//初始化
function csh_Base_T() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/find/base_info"
    }).done(function (data) {
        var data = JSON.parse(data);
        console.log(data);
        $("#company_name").val(data.companyName);
        $("#company_web").val(data.companyUrl);
        $("#company_site").val(data.companyAddress);
        $("#company_bus").val(data.busLine);
        $("#company_tel").val(data.companyTel);
        $("#company_work").val(data.companyBusiness);
        $("#company_wx").val(data.companyWeChat);
        $("#company_size").val(data.companySize);
        $("#company_abstract").val(data.companyProfile);
        
        //---------------------
        $("#product_suggest").val(data.productIntroduction);
        $("#skill_well").val(data.technologyAdvantage);
        $("#now_industry").val(data.industryWhere);
        $("#development").val(data.developmentProspect);
        $("#company_culture").val(data.corporateCulture);
        $("#work_time").val(data.workShift);
        $("#company_leader").val(data.companyLeadership);
        $("#morMoney").val(data.financingSituation);
        $("#hot_positions").val(data.hotPositions);
    //热招职位标签
    //     var strLiJob = '';
    //     for (var i = 0; i < data.recruitList.length; i++) {
    //         strLiJob +='<input class="hot_K" type="button" value="' + data.recruitList[i].name + '"   data-id="' + data.recruitList[i].id + '" data-name="' + data.recruitList[i].name + '" data-details="' + data.recruitList[i].details + '" data-salary="' + data.recruitList[i].salary + '" data-skillsRequired="' + data.recruitList[i].skillsRequired + '" data-workExperience="' + data.recruitList[i].workExperience + '" data-educationRequirements="' + data.recruitList[i].educationRequirements + '"/>';
    //         $('#allRecruitJob').html(strLiJob)
    //     }
    //     successChangeJob()
    }).fail(function (data) {
        console.log(data);

    });

}

function successChangeJob(){
    //点存在的标签弹出修改该标签内容弹窗
    $(".hot_K").click(function(){
        
        if($(this).attr('data-name')!=""){
            var oldName = $(this).attr('data-name').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        }else {
            var oldName = $(this).attr('data-name')
        }
        if($(this).attr('data-details')!=""){
            var oldDetails = $(this).attr('data-details').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        }else {
            var oldDetails = $(this).attr('data-details')
        }
        if($(this).attr('data-salary')!=""){
            var oldSalary = $(this).attr('data-salary').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        }else {
            var oldSalary = $(this).attr('data-salary')
        }
        if($(this).attr('data-skillsRequired')!=""){
            var oldSkillsRequired = $(this).attr('data-skillsRequired').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        }else {
            var oldSkillsRequired = $(this).attr('data-skillsRequired')
        }
        if($(this).attr('data-workExperience')!=""){
            var oldWorkExperience = $(this).attr('data-workExperience').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        }else {
            var oldWorkExperience = $(this).attr('data-workExperience')
        }
        if($(this).attr('data-educationRequirements')!=""){
            var oldEducationRequirements= $(this).attr('data-educationRequirements').replace(/\&lt/g,"<").replace(/\&gt/g,">");
        }else {
            var oldEducationRequirements= $(this).attr('data-educationRequirements')
        }

        var jobId = $(this).attr('data-id')
        change_jobTC();
        $('#job_name').val(oldName)
        $('#job_details').val(oldDetails)
        $('#job_salary').val(oldSalary)
        $('#job_skillsRequired').val(oldSkillsRequired)
        $('#job_Experience').val(oldWorkExperience)
        $('#job_education').val(oldEducationRequirements)
        $('#jobId').val(jobId)

        $('#close_details').click(function(){
            $('#tanchuang').html("");
        });

        $('#job_change').click(function(){
           
            if($('#job_name').val()!=""){
                var name = $('#job_name').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            }else {
                var name = $('#job_name').val()
            }
            if($('#job_details').val()!=""){
                var details = $('#job_details').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            }else {
                var details = $('#job_details').val()
            }
            if($('#job_salary').val()!=""){
                var salary = $('#job_salary').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            }else {
                var salary = $('#job_salary').val()
            }
            if($('#job_skillsRequired').val()!=""){
                var skillsRequired = $('#job_skillsRequired').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            }else {
                var skillsRequired = $('#job_skillsRequired').val()
            }
            if($('#job_Experience').val()!=""){
                var workExperience = $('#job_Experience').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            }else {
                var workExperience = $('#job_Experience').val();
            }
            if($('#job_education').val()!=""){
                var educationRequirements = $('#job_education').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
            }else {
                var educationRequirements = $('#job_education').val()
            }
            var username = $('#regname').val();
            var job = {
                name: name,
                details:details,
                salary: salary,
                skillsRequired: skillsRequired,
                workExperience: workExperience,
                educationRequirements:educationRequirements,
                username:username,
                id:jobId
            };
            if ($('#job_name').val()!="") {
                //保存把数据传到后台
                console.log(job)
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/api/recruit/update',
                    data: JSON.stringify(job)
                }).done(function (data) {
                    layer.msg(data.message, {time:500,icon: 6},function(){
                        $('#tanchuang').html("");
                        window.location.reload(false);
                    })
                }).fail(function (data) {
                    console.log(data);
                    var message = JSON.parse(data.responseText).message;
                    layer.msg(message, {icon: 5})
                });
            } else {
                layer.msg("招聘！至少要写上招聘职位哦！", {icon: 5})
            }
        });
        //删除这条数据
        $("#job_delete").click(function () {
            var jId = $('#jobId').val();
            console.log(jId)
            layer.confirm('您真的确定要删除了吗？', {
                btn: ['确定', '再考虑一下'] //按钮
            }, function () {
                $.ajax({
                    type: "POST",
                    contentType: "application/json",
                    url: '/api/recruit/delete/' + jId

                }).done(function (data) {
                    layer.msg(data.message, {icon: 6})
                    $('#tanchuang').html("");
                    parent.history.go(0);//取消键刷新页面
                }).fail(function (data) {
                    console.log(data);
                    var message = JSON.parse(data.responseText).message;
                    layer.msg(message, {icon: 5})
                });

            }, function () {

            });
        });
    })
}





//新增热招职位弹窗
function add_jobTC(){
    var str0 ='';
    str0 += ' <div style=" width: 300px;height: 490px;background: #fff;position: fixed;top: 18%;left: 54%;border-radius: 10px;padding: 10px;z-index: 100;border: 1px solid #ccc;"><div id="close_details" style="height: 20px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;cursor: pointer;"></div><p style="text-align: left;font-size: 16px;color: #169bd5;">职位名称：</p><p><input id="job_name" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p> <br/><p style="text-align: left;font-size: 16px;color: #169bd5;">职位详情：</p><p style=" text-align: left;text-indent:0em;font-size: 12px;line-height: 24px;color:#999;"><textarea id="job_details" placeholder="职位详情简述" required="required" style="background: url(/static/images/linebg.gif) repeat;text-indent: 0em;line-height: 20px;width:279px;height: 82px;border:none;overflow-y:auto;font-size: 13px; outline: none;margin-left: 5px;resize:none" rows="5" cols="37" maxlength="240"> </textarea></p> <br/><p style="text-align: left;font-size: 16px;color: #169bd5;">薪资待遇：</p><p><input id="job_salary" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p><br/> <p style="text-align: left;font-size: 16px;color: #169bd5;">技能要求：</p><p><input id="job_skillsRequired" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p> <br/><p style="text-align: left;font-size: 16px;color: #169bd5;">工作经历：</p><p><input id="job_Experience" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p><br/> <p style="text-align: left;font-size: 16px;color: #169bd5;">学历要求：</p><p><input id="job_education" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p> <br/> <input id="job_create" style="float: right;padding: 3px 20px;" type="button" value="保存"/> </div> '
    $('#tanchuang').html(str0);

}
//修改热招职位弹窗
function change_jobTC(){
    var str01 ='';
    str01 += ' <div style=" width: 300px;height: 490px;background: #fff;position: fixed;top: 18%;left: 54%;border-radius: 10px;padding: 10px;z-index: 100;border: 1px solid #ccc;"><div id="close_details" style="height: 20px;width: 16px;float: right;background: url(/static/images/icono0.png) no-repeat left bottom;cursor: pointer;"></div><p style="text-align: left;font-size: 16px;color: #169bd5;">职位名称：</p><p><input id="job_name" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p> <br/><p style="text-align: left;font-size: 16px;color: #169bd5;">职位详情：</p><p style=" text-align: left;text-indent:0em;font-size: 12px;line-height: 24px;color:#999;"><textarea id="job_details" placeholder="职位详情简述" required="required" style="background: url(/static/images/linebg.gif) repeat;text-indent: 0em;line-height: 20px;width:279px;height: 82px;border:none;overflow-y:auto;font-size: 13px; outline: none;margin-left: 5px;resize:none" rows="5" cols="37" maxlength="240"> </textarea></p> <br/><p style="text-align: left;font-size: 16px;color: #169bd5;">薪资待遇：</p><p><input id="job_salary" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p><br/> <p style="text-align: left;font-size: 16px;color: #169bd5;">技能要求：</p><p><input id="job_skillsRequired" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p> <br/><p style="text-align: left;font-size: 16px;color: #169bd5;">工作经历：</p><p><input id="job_Experience" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p><br/> <p style="text-align: left;font-size: 16px;color: #169bd5;">学历要求：</p><p><input id="job_education" type="text" value="" style="border:none;border-bottom: 1px solid #ccc;width: 100%;height: 23px;"/></p> <br/><input id="job_delete" style="float: left;padding: 3px 20px;" type="button" value="删除"/> <input id="job_change" style="float: right;padding: 3px 20px;" type="button" value="保存"/>     <div style="display: none"><input id="jobId" type="text" value=""></div>  </div> '
    $('#tanchuang').html(str01);
}

//新增热招职位标签
function job_create(){
   
    if($('#job_name').val()!=""){
        var name = $('#job_name').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    }else {
        var name = $('#job_name').val()
    }
    if($('#job_details').val()!=""){
        var details = $('#job_details').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    }else {
        var details = $('#job_details').val()
    }
    if($('#job_salary').val()!=""){
        var salary = $('#job_salary').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    }else {
        var salary = $('#job_salary').val()
    }
    if($('#job_skillsRequired').val()!=""){
        var skillsRequired = $('#job_skillsRequired').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    }else {
        var skillsRequired = $('#job_skillsRequired').val()
    }
    if($('#job_Experience').val()!=""){
        var workExperience = $('#job_Experience').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    }else {
        var workExperience = $('#job_Experience').val();
    }
    if($('#job_education').val()!=""){
        var educationRequirements = $('#job_education').val().replace(/\</g, "&lt").replace(/\>/g,"&gt").replace(/\"/g,"'");
    }else {
        var educationRequirements = $('#job_education').val()
    }
    var username = $('#regname').val();

    var job = {
        name: name,
        details:details,
        salary: salary,
        skillsRequired: skillsRequired,
        workExperience: workExperience,
        educationRequirements:educationRequirements,
        username:username
    };
    if ($('#job_name').val()!="") {
        //保存把数据传到后台
console.log(job);
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: '/api/recruit/create',
            data: JSON.stringify(job)
        }).done(function (data) {
            layer.msg(data.message, {time:500,icon: 6},function(){
                $('#tanchuang').html("");
                window.location.reload(false);
            })
        }).fail(function (data) {
            console.log(data);
            var message = JSON.parse(data.responseText).message;
            layer.msg(message, {icon: 5})
        });
    } else {
        layer.msg("招聘！至少要写上招聘职位哦！", {icon: 5})
    }
}


//修改热招职位标签内容
function job_change(){

}











