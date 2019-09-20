//--------  公共函数

$(function () {
    getCurData();
});

var timesStatus = '';

function getCurData() {
    var ca = $("#datatab>li.active>a");
    $.ajax({
        type: "POST",
        url: "getTimeIndexData",
        data: {pptype: ca.attr("data-pptype"), ppid: ca.attr("data-ppid"), actywId: ca.attr("data-actywId")},
        dataType: "json",
        success: function (data) {
            if (data) {
                $("#proname").html(data.proname || "");
                $("#apply_time").html("项目申报时间：" + (data.apply_time || ""));
                $("#number").html("项目编号：" + (data.number || ""));
                $("#leader").html("项目负责人：" + (data.leader || ""));
                if (data.pid) {
                    if (ca.attr("data-pptype") == "1") {
                        $("#weekBtn").attr("href", "/f/project/weekly/createWeekly?projectId=" + data.pid);
                    } else {
                        $("#weekBtn").attr("href", "/f/project/weekly/createWeeklyPlus?projectId=" + data.pid);
                    }
                }
                var nodes = mergeNodes(file2Node(data.files), content2Node(data.contents));
                var timeaxis = $('.timeaxis');
                var tableHtml = [];
                var timeStatusB = nodes.every(function (item) {
                    return item.content.start_date && item.content.end_date
                });
                var timeStatusC = nodes.some(function (item) {
                    return item.content.start_date || item.content.end_date
                });


                if (timeStatusB) {
                    timesStatus = timeStatusB ? 'success' : '';
                } else {
                    timesStatus = timeStatusC ? 'some' : 'fail';
                }
                if (timesStatus === 'success') {
                    generateAxisData(data);
                } else {
                    if (timesStatus === 'some') {
                        nodes.forEach(function (item) {
                            var someContent = item.content;
                            tableHtml.push('<tr><td>' + someContent.title + '</td><td>' + (someContent.start_date || '-') + '</td><td>' + (someContent.end_date || '-') + '</td></tr>')
                        });
                        timeaxis.html('<table class="table text-center table-borderd"><thead><tr><td>流程</td><td>开始时间</td><td>结束时间</td></tr></thead><tbody>' + tableHtml.join(' ') + '</tbody><tfoot><tr><td colspan="3">请设置审核时间</td></tr></tfoot></table>')

                    } else {
                        timeaxis.html($('<p class="text-center" style="margin: 60px 0;color: #666; border:solid #ddd;border-width: 1px 0;line-height: 30px; ">请设置审核时间</p>'))
                    }
                }

            } else {
                $("#proname").html("");
                $("#apply_time").html("项目申报时间：");
                $("#number").html("项目编号：");
                $("#leader").html("项目负责人：");
                $(".timeaxis").html("");
            }
        },
        error: function (msg) {
            alert(msg);
        }
    });
}
/**
 * 生成时间轴浮动元素
 * @param data.files - 文件信息
 * @param data.contents - 任务信息和里程碑信息的糅合版信息
 * 每个节点都没有时间， 提示请设置审核时间， 不显示时间抽
 * 存在节点时间，                      不显示时间抽
 *
 */
function generateAxisData(data) {
    var nodeDirection = 1;
    var prevType;
    var htmls = ['<div class="axis"></div>'];
    var nodes = mergeNodes(file2Node(data.files), content2Node(data.contents));
    nodes.forEach(function (o) {
        switch (o.nodeType) {
            // 独立的文件节点
            case 'file':
                if (prevType !== 'file') {
                    nodeDirection = nodeDirection * -1;
                }
                htmls.push(createFileNode(o, nodeDirection));
                break;
            // 独立的内容节点，包括：里程碑、任务，如果后续需要扩展，这里最好分开
            case 'date':
                nodeDirection = nodeDirection * -1;
                htmls.push(createContentNode(o, nodeDirection));
                break;
            case 'milestone':
                htmls.push(createMilestone(o));
                break;
            // 混合的内容节点，有内容和文件，假如文件和里程碑是同一天，这个会比较尴尬
            case 'mixin':
                // 混合节点的方向根据现有的节点方向自动调整
                htmls.push(createMixinNode(o, nodeDirection, prevType));
                break;
            // case 'date':
            // 	htmls.push(createPanel(o, panelDirection));
            // 	panelDirection = panelDirection * -1;
            // 	break;
            // case 'milestone':
            // 	htmls.push(createMilestone(o));
            // 	break;
            // case 'empty':
            // 	htmls.push(createEmpty(o));
            // 	break;
        }
        prevType = o.nodeType;
    });
    var timeaxis = $('.timeaxis');
    timeaxis.html(htmls.join(''));
    // 鼠标移入移出小圆圈的时候显示额外的信息
    // 显示信息的时候，需要先找到小圆圈对应的数据信息

    // timeaxis.find('div.file').hover(function(){
    // 	$(this).addClass('file_over');
    // }, function(){
    // 	var a = $(this).children('a:visible');
    // 	a.length && a.hide();
    // 	$(this).removeClass('file_over');
    // }).children('span').hover(function(){
    // 	$(this).addClass('span_over').siblings('a').show();
    // }, function(){
    // 	$(this).removeClass('span_over')
    // });
    timeaxis.find('.file-group').hover(function () {
        var nodetime = $(this).siblings(".axis-node-time");
        nodetime.removeData("hideOnMouseleave");
        clearTimeout(nodetime.data("hideTimer"));
        nodetime.removeData("hideTimer");
    }, function () {
        $(this).css('visibility', 'hidden').siblings(".axis-node-time").removeClass("show-arrow");
    }).siblings('.axis-node-time').hover(function () {
        var me = $(this).addClass('show-arrow');
        me.siblings('.file-group').css('visibility', 'visible');
        me.data("hideOnMouseleave", true);
    }, function () {
        var me = $(this);
        if (me.data("hideOnMouseleave") === true) {
            me.data("hideTimer", setTimeout(function () {
                me.siblings(".file-group").css("visibility", "hidden");
                me.removeClass("show-arrow");
            }, 50));
            me.removeData("hideOnMouseleave");
        }
    });
}

/**
 * 将文件列表按照日期分组后转成节点 map，格式如下：
 *   {
 *      'YYYY-mm-dd': {
 *          nodeType: 'file',
 *          files: []
 *      },
 *      ...
 *   }
 * @param {Object} files
 */
function file2Node(files) {
    var map = {}, d;
    files.forEach(function (o) {
        var d = o.create_date;
        if (!map[d]) {
            map[d] = [];
        }
        map[d].push(o);
    });
    for (d in map) {
        map[d] = {date: d, nodeType: 'file', files: map[d]};
    }
    return map;
}

function content2Node(contents) {
    var map = {};
    contents.forEach(function (content) {
        // 如果这里的日期有重复，数据将不完整
        map[content.start_date] = {
            nodeType: content.type,
            content: content,
            date: content.start_date
        }
    });
    return map;
}

function mergeNodes(filemap, contentmap) {
    var arr = [], k;
    for (k in contentmap) {
        if (contentmap[k].nodeType === "milestone") {
            arr.push(contentmap[k]);
        }
    }
    arr.forEach(function (node) {
        delete contentmap[node.date];
    });
    // TODO: 使用 for  ...  in 比较 low
    for (k in filemap) {
        if (k in contentmap) {
            arr.push({
                nodeType: 'mixin',
                date: k,
                file: filemap[k],
                content: contentmap[k]
            });
        } else {
            arr.push(filemap[k]);
        }
    }
    for (k in contentmap) {
        if (!(k in filemap)) {
            arr.push(contentmap[k]);
        }
    }
    arr.sort(function (node1, node2) {
        var v1 = node1.date;
        var v2 = node2.date;
        if (v1 === v2) {
            return 0;
        }
        return v1 < v2 ? -1 : 1;
    });
    return arr;
}

function createFileNode(node, direction) {
    var buf = [];
    var files = node.files;
    var iconre = /\.xlsx?/i;
    if (files.length === 1) {
        buf.push('<div class="file-node onlyone ');
    } else {
        buf.push('<div class="file-node ');
    }
    buf.push(getDirectionClass(direction));
    buf.push('">');
    buf.push('<ul class="file-group">');
    files.forEach(function (file) {
        var iconCls = iconre.test(file.name) ? "icon-xls" : "icon-doc";
        buf.push('<li class="file"><a class="' + iconCls + '" href="' + file.url + '">' + file.name + '</a></li>');
    });
    buf.push('</ul>');
    buf.push('<span class="axis-node-time">' + node.date.substring(5) + '</span>');
    buf.push('<span class="axis-node-icon"></span>');
    buf.push('</div>');
    return buf.join('');
}

function createContentNode(node, direction) {
    var content = node.content;
    if (content.type === "milestone") {
        return createMilestone(content);
    }
    return '<div class="axis-panel ' + getDirectionClass(direction) + '">' +
        '  <div class="axis-panel-header">' + content.desc + '</div>' +
        '  <div class="axis-panel-body">' + content.intro + '</div>' +
        '  <div class="axis-panel-arrow"></div>' +
        '  <div class="axis-panel-time-label">' + content.start_date.substring(5) + '</div>' +
        '  <div class="axis-panel-dot"><div class="axis-panel-dot-inner"></div></div>' +
        '</div>';
}

function createMixinNode(node, direction, prevType) {
    var htmls = ['<div class="mixin-node">'];
    if (prevType === "file") {
        htmls.push(createFileNode(node.file, direction));
        htmls.push(createContentNode(node.content, direction * -1));
    } else {
        htmls.push(createContentNode(node.content, direction * -1));
        htmls.push(createFileNode(node.file, direction));
    }
    htmls.push('</div>');
    return htmls.join('');
}

function getDirectionClass(direction) {
    return direction === 1 ? "left-node" : "right-node";
}

function createPanel(o, direction) {
    var oHeight = 0;
    var cls = direction === 1 ? "axis-panel-right" : "axis-panel-left";
    var fileinfo = [];
    if (o.files && o.files.length) {
        fileinfo.push('<div class="axis-fileinfo">');
        fileinfo.push(groupFile(o.files, o.start_date));
        fileinfo.push('</div>');
        oHeight = o.files.length * 25;
    }

    // 使用 YYYY-mm-dd 这种格式，不格式为中文
    // var time_label=(new Date(o.date).getMonth()+1)+"月"+new Date(o.date).getDate()+"日";
    return '<div class="axis-panel ' + cls + '" style="margin-bottom:' + oHeight + '">' +
        '  <div class="axis-panel-header">' + o.desc + '</div>' +
        '  <div class="axis-panel-body">' + o.intro + '</div>' +
        '  <div class="axis-panel-arrow"></div>' +
        '  <div class="axis-panel-time-label">' + o.start_date.substring(5) + '</div>' +
        '  <div class="axis-panel-dot"><div class="axis-panel-dot-inner"></div></div>' +
        fileinfo.join('') +
        '</div>';
}

function groupFile(files, start_date) {
    var buf = [];
    var cache = [];
    var prevDate;
    var iconre = /\.xlsx?/i;

    files.forEach(function (file) {
        file.iconCls = iconre.test(file.name) ? "icon-xls" : "icon-doc";
        if (prevDate) {
            if (file.create_date === prevDate) {
                cache.push(file);
            } else {
                if (cache.length === 1) {
                    buf.push(cache[0]);
                } else {
                    buf.push(cache);
                }
                cache = [file];
            }
        } else {
            cache.push(file);
        }
        prevDate = file.create_date;
    });
    if (cache.length === 1) {
        buf.push(cache[0]);
    } else {
        buf.push(cache);
    }
    buf = buf.map(function (file) {
        var htmls = [];
        if (typeof file.slice === "function") {
            htmls.push('<div class="file-group"><ul>')
            file.forEach(function (f) {
                htmls.push('<li class="file"><a class="' + f.iconCls + '" href="' + f.url + '">' + f.name + '</a></li>');
            });
            htmls.push('</ul><span>' + file[0].create_date.substring(5) + '</span></div>');
        } else {
            htmls.push('<div class="file"><a class="' + file.iconCls + '" href="' + file.url + '">' + file.name + '</a><span>' + file.create_date.substring(5) + '</span></div>');
        }
        return htmls.join('');
    });
    if (files[0].create_date !== start_date) {
        buf.shift('<br/>');
    }
    return buf.join('');
}

function createMilestone(o) {
    o = o.content;
    var st_tiem, en_time, startDate, endDate;


    if (o.start_date) {
        startDate = new Date(o.start_date);
        st_tiem = (startDate.getMonth() + 1) + '月' + startDate.getDate() + '日'
    }
    if (o.end_date) {
        endDate = new Date(o.end_date);
        en_time = (endDate.getMonth() + 1) + '月' + endDate.getDate() + '日'
    }
    var HTMLstr = '<div class="axis-milestone' + (o.current ? ' axis-milestone-active' : ' ') + '">' +
        '<div class="axis-milestone-bgImg"></div>' +
        '<div class="axis-milestone-date" style="font-size: 14px;"><div style="margin-bottom: 6px;">' + (startDate ? startDate.getFullYear() : "") + '年</div>' + st_tiem + '至' + en_time + '</div>' +
        '<div class="axis-milestone-title">' + o.title + '</div>';
    var approvalTimeStr = '';
    var approvalDescStr = '';
    if (o.approvalTime) {
        approvalTimeStr += '<div class="approval-time">' + (o.approvalTime) + '</div>'
    }
    if (o.approvalDesc) {
        approvalDescStr += '<div class="approval-desc">' + (o.approvalDesc) + '</div>'
    }
    if (o.btn_option) {
        if (o.btn_option.form_exist) {
            return HTMLstr +
                '<div class="desc-label">' +
                '<span  style="font-size: 14px;">' + st_tiem + '至' + en_time + '</span><br/><span>' + o.title + '</span>' +
                '</div>' +
                '<div class="axis-milestone-href">' +
                '<a href="' + o.btn_option.form_exist_exist_url + '"><span class="icon-plus-sign"></span>' + o.btn_option.form_exist_exist_url + '</a>' +
                '</div>' + approvalTimeStr + approvalDescStr +
                '</div>';

        } else {
            return HTMLstr +
                '<div class="desc-label"><span style="font-size: 14px;">' + st_tiem + '至' + en_time + '</span><br/><span>' + o.title + '</span></div>' +
                '<div class="axis-milestone-btn"><a href="' + o.btn_option.url + '"><span class="icon-plus-sign"></span>' + o.btn_option.name + '</a>' +
                '</div>' + approvalTimeStr + approvalDescStr +
                '</div>';
        }

    } else {
        return HTMLstr + '<div class="desc-label"><span style="font-size: 14px;">' + st_tiem + '至' + en_time + '</span><br/><span>' + o.title + '</span>' +
            '</div>' + approvalTimeStr + approvalDescStr +
            '</div>';

    }
}
function canSumitClose(projectId, url) {
    $.ajax({
        type: "GET",
        url: "canSumitClose",
        data: "projectId=" + projectId,
        dataType: "text",
        success: function (data) {
            if (data == "false") {
                showModalMessage(0, "未创建学分配比，请先完成该信息", {
                    创建学分配比: function () {
                        top.location = "scoreConfig?projectId=" + projectId;
                    },
                    取消: function () {
                        $(this).dialog("close");
                    }
                });
            } else {
                top.location = url + "?projectId=" + projectId;
            }
        },
        error: function (msg) {
            showModalMessage(0, msg);
        }
    });
}
function tabChange(ob) {
    $(ob).parent().parent().find("li.active").removeClass("active");
    $(ob).parent().addClass("active");
    getCurData();
}


