/**
 * Created by Administrator on 2017/11/27.
 */


var numberLetterExp = /^[a-zA-Z0-9]+$/; // 数字和字母正则
var mobileRegExp = /^0?(13[0-9]|15[012356789]|18[0-9]|17[0-9])[0-9]{8}$/;

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == ""){
        url = window.location.search;
    }else{
        url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]); return null;
}

/*
 * url 目标url
 * arg 需要替换的参数名称
 * arg_val 替换后的参数的值
 * return url 参数替换后的url
 */
function changeURLArg(url,arg,arg_val){
    var pattern=arg+'=([^&]*)';
    var replaceText=arg+'='+arg_val;
    if(url.match(pattern)){
        var tmp='/('+ arg+'=)([^&]*)/gi';
        tmp=url.replace(eval(tmp),replaceText);
        return tmp;
    }else{
        if(url.match('[\?]')){
            return url+'&'+replaceText;
        }else{
            return url+'?'+replaceText;
        }
    }
    return url+'\n'+arg+'\n'+arg_val;
}

function toHref(href) {
	self.location.href = href;
}

function resetTip() {
	top.$.jBox.tip.mess = null;
}

// 关闭提示框
function closeTip() {
    top.$.jBox.closeTip();
}

//显示提示框
function showTip(mess, type, timeout, lazytime, closed) {
    resetTip();
    setTimeout(function () {
        top.$.jBox.tip(mess, (type == undefined || type == '' ? 'info' : type), {
            opacity: 0,
            timeout: timeout == undefined ? 2000 : timeout,
            closed: closed || function () {

            }
        });
    }, lazytime == undefined ? 500 : lazytime);
}

// 显示加载框
function loading(mess) {
    if (mess == undefined || mess == "") {
        mess = "正在提交，请稍等...";
    }
    resetTip();
    top.$.jBox.tip(mess, 'loading', {opacity: 0});
}

// 关闭提示框
function closeLoading() {
    // 恢复提示框显示
    resetTip();
    // 关闭提示框
    closeTip();
}

// 警告对话框
function alertx(mess, closed) {
    // var winH = $(window).height();
    // var ctop = (winH - 140) / 2;
    top.$.jBox.info(mess, '提示', {
        closed: function () {
            if (typeof closed == 'function') {
                closed();
            } else if (typeof closed == 'string') {
                location = closed;
            }
        }, persistent: true
    });
    top.$('.jbox-body .jbox-icon').css('top', '55px');
}

// 确认对话框
function confirmx(mess, href, closed) {
    var winH = $(window).height();
    var ctop = (winH - 140) / 2;
    top.$.jBox.confirm(mess, '提示', function (v, h, f) {
        resetTip();
        if (v == 'ok') {
            if (typeof href == 'function') {
                href();
            } else if (typeof href == 'string') {
                resetTip();
                location = href;
            }
        }
        if (v == 'cancel') {
            if (typeof closed == 'function') {
                closed();
            } else if (typeof closed == 'string') {
                resetTip(); //loading();
                location = closed;
            }
        }
    }, {
        persistent: true, top: ctop
    });
    top.$('.jbox-body .jbox-icon').css('top', '55px');
    return false;
}

// 提示输入对话框
function promptx(title, lable, href, closed) {
    top.$.jBox("<div class='form-search' style='padding:20px;text-align:center;'>" + lable + "：<input type='text' id='txt' name='txt'/></div>", {
        title: title, submit: function (v, h, f) {
            if (f.txt == '') {
                top.$.jBox.tip("请输入" + lable + "。", 'error');
                return false;
            }
            if (typeof href == 'function') {
                href();
            } else {
                resetTip(); //loading();
                location = href + encodeURIComponent(f.txt);
            }
        }, closed: function () {
            if (typeof closed == 'function') {
                closed();
            }
        }
    });
    return false;
}
//获取字典标签
function getDictLabelByBoolean(data, value, defaultValue){
    for (var i=0; i<data.length; i++){
        var row = data[i];
        if((row.value == true || row.value == 'true') && (value == true || value == 'true')){
            return row.label;
        }else if((row.value == false || row.value == 'false') && (value == false || value == 'false')){
            return row.label;
        }
    }
    return defaultValue;
}

(function (window, $) {
    $.fn.treeData = function () {
        var url = $(this).attr('data-url');
        if (!url) {
            return false;
        }
        var $this = $(this);
        var treeDataXhr = $.get(url);
        var iframeId = $this.attr('data-iframe');
        var iframeUrl = $this.attr('data-iframe-url');
        treeDataXhr.success(function (data) {
            var floorTree = $.fn.zTree.init($this, {
                view: {
                    showIcon: true
                },
                data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId", rootPId: '1'}},
                callback: {
                    onClick: function (event, treeId, treeNode, clickFlag) {
                        var rUrl = iframeUrl.replace('pwSpaceId', treeNode.id);
                        rUrl = rUrl.replace('pwSpaceType', treeNode.type || '')
                        // $(iframeId).find('.nav-tabs li:last-child').find('a').attr('href', ctx+'/pw/pwRoom/form?pwSpace.id='+treeNode.id+'&pwSpace.type='+treeNode.type)
                        $(iframeId).attr('src', rUrl)
                        //if(clickFlag && treeNode.level >= 1){
                        $(iframeId).attr('src', rUrl)
                        //}
                    }
                }
            }, data);
            floorTree.expandAll(true);
        })
    };


    $.fn.jConfirm = function () {
        var $this = $(this);
        var $body = $('body');
        $(this).on('click', function (e) {
            e.preventDefault();
            var href = $(this).attr('href');
            var msg = $(this).attr('data-msg');
            var id = $(this).attr('data-id');
            var confirmId = 'confirm' + id;
            var template = '<div id="' + confirmId + '" class="dialog-confirm hide" title="提示"><i class="icon-confirm"></i><span class="msg">' + msg + ' </span></div>';
            $(template).appendTo($body);
            $(template).dialog({
                closeText: "关闭",
                resizable: false,
                modal: true,
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-primary',
                    click: function () {
                        $(this).dialog('close');
                        $(this).dialog('destroy');
                        $('#' + confirmId).detach();
                        location = href;
                    }
                }, {
                    text: '取消',
                    'class': 'btn btn-default',
                    click: function () {
                        $(this).dialog('close');
                        $(this).dialog('destroy');
                        $('#' + confirmId).detach();
                    }
                }]
            })
        })


    }
    //confirm
    $('[data-toggle="confirm"]').jConfirm();


})(window, jQuery);

+function ($) {
    $.fn.colorPicker = function (option) {
        var options = $.extend({}, $.fn.colorPicker.default, option);
        var colors = options.colors;
        var lis = '';
        var $this = $(this);
        var html = '';
        var $showColorI = $this.next().find('i');
        var $controls = $this.parent();
        var $colorsDrop, $colorShowBlock;
        var timerId = null;
        var val = $this.val();
        var $parent = $this.parent();
        $.each(colors, function (i, color) {
            lis += '<li><span style="background-color: ' + color + '"></span></li>';
        });
        html += ' <div class="colors-drop" tabindex="1"><div class="color-block-show"></div><ul class="color-list">' + lis + '</ul></div>';

        if (val) {
            $showColorI.css('background-color', '#' + val)
        }

        $this.on('click', function () {
            if ($(this).parent().find('.colors-drop').size() < 1) {
                $colorsDrop = $controls.append(html).find('.colors-drop');
                $colorShowBlock = $colorsDrop.find('.color-block-show');
                $colorsDrop.on('blur', function () {
                    $colorsDrop.hide()
                })
                if (val) {
                    $colorShowBlock.css('background-color', '#' + val)
                }
            } else {
                $colorsDrop.show()
            }
            $colorsDrop.focus();
        })


        // $this.on('blur', function () {
        //     timerId = setTimeout(function () {
        //         $colorsDrop.hide()
        //     }, 100)
        // })

        $controls.on('click', '.colors-drop li', function () {
            var color = $(this).find('span').css('background-color');
            var $lis = $(this).parent().find('li');
            var index = $lis.index($(this));

            timerId && clearTimeout(timerId);

            $showColorI.css('background-color', color);
            $controls.find('.colors-drop').hide();
            $this.val(colors[index].substring(1))
        });

        $controls.on('mouseenter', '.colors-drop li', function () {
            var color = $(this).find('span').css('background-color');
            $colorShowBlock.css('background-color', color)
        })

    }

    $.fn.colorPicker.default = {
        colors: ['#000000', '#e9432d', '#f46e65', '#3dbd7d', '#49a9ee', '#f7629e', '#f78e3d', '#948aec', '#ffce3d', '#3db8c1', '#d9d9d9', '#d75000', '#f56a00']
    }

    $(function () {
        $('.color-block').colorPicker({})
    })
}(jQuery)

$(function () {



    //modal 框可移动

   var modalDraggable = $('.modal[data-draggable="draggable"]');
    modalDraggable.draggable((modalDraggable.data('draggableOption') || {}));



    //房间管理
    var $handlerBar = $('.layout-handler-bar');
    $handlerBar.on('click', function (e) {
        $(this).toggleClass('bar-close');
        $(this).prev().toggle()
    });

    $('#floorTree').treeData();


})





$(function () {
    var winHref = location.href;
    var $listWrap;
    var pathName = location.pathname;
    //顶级窗体
    var topWin = (function () {
        var p = window.parent;
        while (p != p.window.parent) {
            p = p.window.parent;
        }
        return p;
    })();
    $listWrap = $(top.document).find('#list_wrap');
    if(/index$/.test(winHref)){
        $listWrap.find('li').removeClass('current');
        $listWrap.find('.level_two').eq(0).find('>a').addClass('active');
        return;
    }

    $listWrap.find('li').each(function (i, li) {
        var $li = $(li);
        var href = $li.find('a').attr('href');
        if(!(/javascript/.test(href))){
            if(href === pathName){
                $listWrap.find('li').removeClass('current');
                $listWrap.find('.level_two').find('>a').removeClass('active');
                $li.addClass('current');
                if($li.hasClass('level_three')){
                    $li.parents('.level_two').find('>a').addClass('active')
                }
                return false;
            }
        }
    })
})


jQuery.validator.addMethod("phone_number", function (value, element) {
    var length = value.length;
    return this.optional(element) || (length == 11 && (/^0?(13[0-9]|15[012356789]|18[0-9]|17[0-9])[0-9]{8}$/).test(value));
}, "手机号码格式错误");

jQuery.validator.addMethod("letterNumber", function (value, element) {
    return this.optional(element) || ((/^[0-9A-Za-z]+$/).test(value));
}, "输入字母或者数字");

jQuery.validator.addMethod("ip", function (value, element) {
    return this.optional(element) || ((/^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/).test(value));
}, "输入正确的IP地址");

//下载ftp文件
function downfile(url, fileName) {
    location.href = "/ftp/loadUrl?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
}

//获取字典标签
function getDictLabel(data, value, defaultValue){
    for (var i=0; i<data.length; i++){
        var row = data[i];
        if (row.value == value){
            return row.label;
        }
    }
    return defaultValue;
}