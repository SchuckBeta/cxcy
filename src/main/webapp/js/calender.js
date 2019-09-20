$(function () {
    $.ajax({
        type: "GET",
        url: "getGcontestTimeIndexData",
        data: "",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            console.log(data, 898);
            common.changeData(data);
        },
        error: function (msg) {
            // alert(msg);
        }
    });
});

/**
 * 日历表公用函数
 */
var common = {};
//头部标题替换
common.titleReplace = function (data) {
    if (!data.project_code.name) {
        $('#form-head-title').html("");
        $('#prj-num').html("");
    } else {
        $('#form-head-title').html(data.project_code.name);
        $('#prj-num').html(data.project_code.code);
    }
}


//列表划分三类
common.changeData = function (data) {
    if (!data.table_first) {
        return;
    }
    var arr = data.table_first.list;
    var formTypeArr = [], colleageTypeArr = [], competionTypeArr = [];
    if (!arr) {
        return
    }
    arr.forEach(function (item) {
        var oType = parseInt(item.type);
        switch (oType) {
            case 0 :
                formTypeArr.push(item);
                break;
            case 1 :
                colleageTypeArr.push(item);
                break;
            case 2 :
                competionTypeArr.push(item);
                break;
            default:
                break;
        }
    });
    common.formType(formTypeArr);
    common.colleageType(colleageTypeArr);
    common.competionType(competionTypeArr);
    common.titleReplace(data);
}
//type :0-->提交表单类型的数据处理
common.formType = function (formTypeArr) {
    var htmlStr = [], str = '';
    var fileList = formTypeArr[0].list;

    fileList.forEach(function (item) {
        var s = item.link;
        var index = s.lastIndexOf('.');
        var Txtype = s.substr(index + 1, s.length), icon_type = '';
        switch (Txtype) {
            case 'docx' :
                icon_type = 'word';
                break;
            case 'doc'  :
                icon_type = 'word';
                break;
            case 'pptx' :
                icon_type = 'ppt';
                break;
            case 'ppt'  :
                icon_type = 'ppt';
                break;
            case 'xlsx' :
                icon_type = 'excel';
                break;
            case 'xls'  :
                icon_type = 'excel';
                break;
            case 'txt'    :
                icon_type = 'txt';
                break;
            case 'rar'    :
                icon_type = 'rar';
                break;
            case 'zip'    :
                icon_type = 'ZIP';
                break;
            case 'project'    :
                icon_type = 'project';
                break;
            default     :
                icon_type = 'image';
                break;
        }
        str += '<p class="txt-list"><img src="' + ctxImages + '/' + icon_type + '.png">';//<a href="'+item.url+'">'+item.link+'</a></p>'
        str += '<a  href="javascript:void(0)"  onclick="downfile(\'' + item.url + '\',\'' + item.link + '\');return false">' + item.link + '</a></p>';


    });
    var t = common.formater(formTypeArr[0]['Date']);
    htmlStr = [
        '<section class="row list-wrap">',
        '<div class="col-sm-4 col-md-3 col-lg-3 month-label month-label-one" style="max-width:123px;width:13.5%;">',
        '<div class="en-month">' + t.mon + '</div>',
        '<div class="mon-wrap">',
        '<div class="date"><strong>' + t.day + '</strong></div>',
        '<div class="events">提交报名表</div>',
        '</div>',
        '</div>',
        '<div class="col-sm-8 col-md-9 col-lg-9 evt-list evt-list-one" style="min-height:120px;width:84%;">',
        '<div class="evt-list-span evt-list-span-one">',
        '<span class="left-arrow"></span>',
        '<span class="right-arrow">',
        '</span><span class="top-arrow">',
        '</span><span class="bottom-arrow"></span>',
        '</div>' + str + '</div>',
        '</section>'
    ].join('');

    $('#calender-list').append(htmlStr);

    ////设置左侧每个月份标签高度
    $('.month-label-one').height($('.evt-list-one').height() - 20);

    //设置当前hover的红色框的宽，高度
    $('.evt-list-span-one').height($('.evt-list-one').height() + 18);
    $('.evt-list-span-one').width($('.evt-list-one').width() + 32);


    //设置小三角top高度位置
    $('.evt-list-span-one .bottom-arrow').css({
        top: $('.evt-list-one').height() + 12
    })
}

//type :1-->院级类型的的列表数据处理
common.colleageType = function (colleageTypeArr) {
    var htmlStr = [], str = '';
    if (!colleageTypeArr.length) {
        return false;
    }
    var tableList = colleageTypeArr[0].list;
    var t = common.formater(colleageTypeArr[0]['Date']);

    tableList.forEach(function (it) {
        str += [
            '<tr>',
            '<td>' + (it.College_score || '<span style="opacity: 0">1</span>') + '</td>',
            '<td>' + it.Proposal + '</td>',
            '<td>' + it.ranking + '</td>',
            '</tr>'
        ].join('');
    });

    htmlStr = [
        '<section class="row list-wrap">',
        '<div class="col-sm-4 col-md-3 col-lg-3 month-label month-label-two" style="max-width:123px;width:13.5%;">',
        '<div class="en-month">' + t.mon + '</div>',
        '<div class="mon-wrap">',
        '<div class="date"><strong>' + t.day + '</strong></div>',
        '<div class="events">' + colleageTypeArr[0].SpeedOfProgress + '</div>',
        '</div>',
        '</div>',
        '<div class="col-sm-8 col-md-9 col-lg-9 evt-list evt-list-two" style="width:84%;">',
        '<div class="evt-list-span evt-list-span-two">',
        '<span class="left-arrow"></span>',
        '<span class="right-arrow">',
        '</span><span class="top-arrow">',
        '</span><span class="bottom-arrow"></span>',
        '</div>',
        '<table class="table table-hover table-striped table-bordered">',
        '<caption class="prj-title-desc">',
        '<p>所在组别：<span>' + colleageTypeArr[0].group + '</span></p>',
        '<p>学院：<span>' + colleageTypeArr[0].School + '</span></p>',
        '<p>参赛项目组数：<span>' + colleageTypeArr[0].Number_of_entries + '</span></p>',
        '</caption>',
        '<thead>',
        '<th>学院评分</th>',
        '<th>建议及意见</th>',
        '<th>学院排名</th>',
        '</thead>',
        '<tbody>' + str + '</tbody>',
        '</table>',
        '</div>',
        '</section>'
    ].join('');
    $('#calender-list').append(htmlStr);

    //设置左侧每个月份标签高度
    $('.month-label-two').height($('.evt-list-two').height() - 20);

    console.log($('.evt-list-two').width(), '类型1')

    //设置当前hover的红色框的宽，高度
    $('.evt-list-span-two').height($('.evt-list-two').height() + 18);
    $('.evt-list-span-two').width($('.evt-list-two').width() + 32);

    //设置小三角top高度位置
    $('.evt-list-span-two .bottom-arrow').css({
        top: $('.evt-list-two').height() + 12
    })
}

//type :2-->大赛类型的列表数据处理
common.competionType = function (competionTypeArr) {
    var htmlStr = [];
    if (!competionTypeArr.length) {
        return false;
    }
    competionTypeArr.forEach(function (item) {
        var str = [];
        t = common.formater(item['Date']);
        item.list.forEach(function (it, index, arr) {
            str += [
                '<tr>',
                '<td>' + (it.Review_the_content || '<span style="opacity: 0">1</span>') + '</td>',
                '<td>' + it.getScore + '</td>',
                '<td>' + it.advice + '</td>',
                '<td>' + it.Current_rank + '</td>',
                index === 0 ? '<td rowspan="' + arr.length + '" >' + item.Awards + '</td>' : "",
                index === 0 ? '<td rowspan="' + arr.length + '" >' + item.bonus + '</td>' : "",
                '</tr>'
            ].join('');
        });
        var htmlStr = [
            '<section class="row list-wrap">',
            '<div class="col-sm-4 col-md-3 col-lg-3 month-label month-label-three" style="max-width:123px;width:13.5%;">',
            '<div class="en-month">' + t.mon + '</div>',
            '<div class="mon-wrap">',
            '<div class="date"><strong>' + t.day + '</strong></div>',
            '<div class="events">' + item.SpeedOfProgress + '</div>',
            '</div>',
            '</div>',
            '<div class="col-sm-8 col-md-9 col-lg-9 evt-list evt-list-three" style="width:84%;">',
            '<div class="evt-list-span evt-list-span-three">',
            '<span class="left-arrow"></span>',
            '<span class="right-arrow">',
            '</span><span class="top-arrow">',
            '</span><span class="bottom-arrow"></span>',
            '</div>',
            '<table class="table table-hover table-striped table-bordered">',
            '<caption class="prj-title-desc">',
            '<p>所在组别：<span>' + item.group + '</span></p>',
            '<p>学院：<span>' + item.School + '</span></p>',
            '<p>参赛项目组数：<span>' + item.Number_of_entries + '</span></p>',
            '</caption>',
            '<thead>',
            '<th>评分内容</th>',
            '<th>得分</th>',
            '<th>建议及意见</th>',
            '<th>当前排名</th>',
            '<th>荣获奖项</th>',
            '<th>荣获奖金</th>',
            '</thead>',
            '<tbody>' + str + '</tbody>',
            '</table>',
            '</div>',
            '</section>'
        ].join('');
        $('#calender-list').append(htmlStr);

        //设置左侧每个月份标签高度
        $('.month-label-three').each(function () {
            $(this).height($(this).siblings('.evt-list-three').height() - 20);
        })

        //设置当前hover的红色框的宽，高度
        $('.evt-list-span-three').each(function () {
            $(this).height($(this).parent('.evt-list-three').height() + 18)
            $(this).width($(this).parent('.evt-list-three').width() + 32)
        })

        //设置小三角top高度位置
        $('.evt-list-span-three ').each(function () {
            $(this).children('.bottom-arrow').css({
                top: $(this).height() - 6
            })
        })
    })
}

//时间格式处理函数
common.formater = function (date) {
    var o = {};
    var d = new Date(date)
    var monthArr = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    o.mon = monthArr[d.getMonth()].substring(0, 3);
    o.day = d.getDate();
    return o;
}
function downfile(filename) {
    location.href = "/ftp/download?fileName=" + encodeURIComponent(filename);
}
function downfile(url, fileName) {
    location.href = "/ftp/loadUrl?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
}