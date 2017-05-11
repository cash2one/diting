function csh_b() {
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    //初始化知识库
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + 1 + "&companyId=" + $('#companyid0').val()
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="180"  Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th width="350"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw1</th> <th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw2</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw3</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw4</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw5</th><th width="80"  Name="Scene" EditType="TextBox" style="text-align: center;">场景</th> <th width="80"  Name="actionOption" EditType="TextBox" style="text-align: center;">动作</th><th width="100"  Name="ChangeTime" style="text-align: center;">修改时间</th> <th width="80"  Name="nums" style="text-align: center;">调用次数</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d1 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            str += '<tr style="border-bottom:1px solid #ccc ;"><td width="50"  style="text-align: center;">' + n + '</td><td width="180">' + data.items[i].question + '</td><td width="350" style="max-height: 43px;display: block;">' + data.items[i].answer + '</td><td width="50" style="text-align: center;">' + data.items[i].kw1 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw2 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw3 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw4 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw5 + '</td><td width="80" style="text-align: center;">' + data.items[i].scene + '</td><td width="80" style="text-align: center;">' + data.items[i].actionOption + '</td><td width="100" style="text-align: center;">' + times + '</td><td width="80" style="text-align: center;">' + data.items[i].frequency + '</td></tr>';
        }
        if (data.items.length == 0) {
            var strxx = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="180"  Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th width="350"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw1</th> <th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw2</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw3</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw4</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw5</th><th width="80"  Name="Scene" EditType="TextBox" style="text-align: center;">场景</th> <th width="80"  Name="actionOption" EditType="TextBox" style="text-align: center;">动作</th> <th width="100"  Name="ChangeTime" style="text-align: center;">修改时间</th> <th width="80"  Name="nums" style="text-align: center;">调用次数</th> </tr>';
            layer.msg("没有搜索到相应数据");
            $('#tabProduct').html(strxx);
        } else {
            $('#tabProduct').html(str);
        }
    }).fail(function (data) {
        console.log(data);
    });
}

//回车键搜索功能
function enterChangessB(ev){
    var oEvent = ev || event;
    if (oEvent.keyCode == 13 && oEvent.ctrlKey || oEvent.keyCode == 13) {
        changess()
    }
}

//知识库学习页面的搜索功能
function changess() {
    var order = $('#order').val();
    var select = $('#select').val();
    var selectques = $('#selectques').val();
    var navsearchinput = $('#nav-search-input').val();
    $("#pageNo").val(1)
    $.ajax({
        type: "GET",
        contentType: "application/json",
        url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + $('#companyid0').val() + "&keywords=" + navsearchinput + "&queryState=" + select + "&queryCriteria=" + order + "&type=" + selectques
    }).done(function (data) {
        var data = JSON.parse(data);
        Paging_device(data.total);
        var str = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="180"  Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th width="350"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw1</th> <th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw2</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw3</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw4</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw5</th><th width="80"  Name="Scene" EditType="TextBox" style="text-align: center;">场景</th><th width="80"  Name="actionOption" EditType="TextBox" style="text-align: center;">动作</th>  <th width="100"  Name="ChangeTime" style="text-align: center;">修改时间</th> <th width="80"  Name="nums" style="text-align: center;">调用次数</th> </tr>';
        for (var i = 0; i < data.items.length; i++) {
            var d1 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
            var times = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
            var n = (i + 1);
            str += '<tr style="border-bottom:1px solid #ccc ;"><td width="50"  style="text-align: center;">' + n + '</td><td width="180">' + data.items[i].question + '</td><td width="350" style="max-height: 43px;display: block;">' + data.items[i].answer + '</td><td width="50" style="text-align: center;">' + data.items[i].kw1 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw2 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw3 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw4 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw5 + '</td><td width="80" style="text-align: center;">' + data.items[i].scene + '</td><td width="80" style="text-align: center;">' + data.items[i].actionOption + '</td><td width="100" style="text-align: center;">' + times + '</td><td width="80" style="text-align: center;">' + data.items[i].frequency + '</td></tr>';
        }
        if (data.items.length == 0) {
            var strxx = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="180"  Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th width="350"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw1</th> <th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw2</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw3</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw4</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw5</th><th width="80"  Name="Scene" EditType="TextBox" style="text-align: center;">场景</th> <th width="80"  Name="actionOption" EditType="TextBox" style="text-align: center;">动作</th> <th width="100"  Name="ChangeTime" style="text-align: center;">修改时间</th> <th width="80"  Name="nums" style="text-align: center;">调用次数</th> </tr>';
            layer.msg("没有搜索到相应数据");
            $('#tabProduct').html(strxx);
        } else {
            $('#tabProduct').html(str);
        }
    }).fail(function (data) {
        console.log(data);
    });
}

//分页器，页码切换执行
function Paging_device(strips) { //参数是总计多少条
    $.ajaxSetup({cache:false})  //禁止ie浏览器读取缓存的ajax
    var x = $("#pageNo").val();
    var com = $('#companyid0').val()
    $('#red').smartpaginator({
        totalrecords: strips,
        datacontainer: 'tabProduct',
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
            var order = $('#order').val();
            var select = $('#select').val();
            var selectques = $('#selectques').val();
            var navsearchinput = $('#nav-search-input').val();
            $.ajax({
                type: "GET",
                contentType: "application/json",
                url: "/api/company/knowledge/search-page?pageNo=" + $("#pageNo").val() + "&companyId=" + com + "&keywords=" + navsearchinput + "&queryState=" + select + "&queryCriteria=" + order + "&type=" + selectques
            }).done(function (data) {
                var data = JSON.parse(data);
                Paging_device(data.total);
                var str = '<tr>  <th width="50" Name="Num" style="text-align: center;">序号</th> <th width="180"  Name="Question" EditType="TextBox" maxlength="60">问题（长度≤60）</th> <th width="350"  Name="Answer" EditType="TextBox" maxlength="240">答案（长度≤240）</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw1</th> <th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw2</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw3</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw4</th><th width="50"  Name="Keyword" EditType="TextBox" style="text-align: center;">kw5</th><th width="80"  Name="Scene" EditType="TextBox" style="text-align: center;">场景</th><th width="80"  Name="actionOption" EditType="TextBox" style="text-align: center;">动作</th>  <th width="100"  Name="ChangeTime" style="text-align: center;">修改时间</th> <th width="80"  Name="nums" style="text-align: center;">调用次数</th> </tr>';
                for (var i = 0; i < data.items.length; i++) {
                    var d1 = new Date(data.items[i].updatedTime);    //根据时间戳生成的时间对象
                    var times = (d1.getFullYear()) + "/" + (d1.getMonth() + 1) + "/" + (d1.getDate());
                    var n = ((($("#pageNo").val() - 1) * 15) + (i + 1));
                    str += '<tr style="border-bottom:1px solid #ccc ;"><td width="50"  style="text-align: center;">' + n + '</td><td width="180">' + data.items[i].question + '</td><td width="350" style="max-height: 43px;display: block;">' + data.items[i].answer + '</td><td width="50" style="text-align: center;">' + data.items[i].kw1 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw2 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw3 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw4 + '</td><td width="50" style="text-align: center;">' + data.items[i].kw5 + '</td><td width="80" style="text-align: center;">' + data.items[i].scene + '</td><td width="80" style="text-align: center;">' + data.items[i].actionOption + '</td><td width="100" style="text-align: center;">' + times + '</td><td width="80" style="text-align: center;">' + data.items[i].frequency + '</td></tr>';
                    $('#tabProduct').html(str);
                }
            }).fail(function (data) {
                console.log(data);
            });
        }
    });
}

