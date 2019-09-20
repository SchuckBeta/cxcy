/**
 * Created by Administrator on 2017/12/11.
 */

// 获取URL地址参数
function getQueryString(name, url) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    if (!url || url == "") {
        url = window.location.search;
    } else {
        url = url.substring(url.indexOf("?"));
    }
    r = url.substr(1).match(reg)
    if (r != null) return unescape(r[2]);
    return null;
}

/*
 * url 目标url
 * arg 需要替换的参数名称
 * arg_val 替换后的参数的值
 * return url 参数替换后的url
 */
function changeURLArg(url, arg, arg_val) {
    var pattern = arg + '=([^&]*)';
    var replaceText = arg + '=' + arg_val;
    if (url.match(pattern)) {
        var tmp = '/(' + arg + '=)([^&]*)/gi';
        tmp = url.replace(eval(tmp), replaceText);
        return tmp;
    } else {
        if (url.match('[\?]')) {
            return url + '&' + replaceText;
        } else {
            return url + '?' + replaceText;
        }
    }
    return url + '\n' + arg + '\n' + arg_val;
}


var dialogCyjd = {
    successIcon: function (msg) {
        return '<div class="dialog-info-box"><i class="icon-ok-msg"></i><span>' + msg + '</span></div>';
    },
    errorIcon: function (msg) {
        return '<div class="dialog-info-box"><i class="icon-fail-msg"></i><span>' + msg + '</span></div>';
    },
    warningIcon: function (msg) {
        return '<div class="dialog-info-box"><i class="icon-warning-msg"></i><span>' + msg + '</span></div>';
    },
    dialogDefaultOption: {
        id: '#dialogCyjd',
        modal: true,
        resizable: false,
        width: 'auto',
        height: 'auto',
        title: '提示',
        closeText: '',
        dialogClass: 'dialog-cyjd-container',
        buttons: [{
            text: '确定',
            class: 'btn btn-sm btn-primary',
            click: function () {
                $(this).dialog("close");
            }
        }]
    },
    createDialog: function (res, msg, dialogOption) {
        var html = '';
        var eleId;
        var options;
        dialogOption = dialogOption || {};
        options = $.extend({}, this.dialogDefaultOption, dialogOption);
        eleId = options.id;
        switch (res * 1) {
            case 0 :
                html = this.errorIcon(msg);
                break;
            case 1 :
                html = this.successIcon(msg);
                break;
            case 2 :
                html = this.warningIcon(msg);
                break;
        }
        $(eleId).html(html);
        $(eleId).dialog(options);
    }
}

$(function () {
    var winHeight = $(window).height();
    var $content = $('#content');
    var footerBoxHeight = $('.footer').innerHeight();
    var headerHeight = $('.header').height();
    $content.css('minHeight', winHeight - footerBoxHeight - headerHeight);
    $.fn.dealSeparator = function () {
        var $this = $(this);
        if ($this.size() < 1) return;
        $this.each(function (i, item) {
            var text = $(item).text();
            text = $.trim(text);
            if (/\/$/.test(text)) {
                $(item).text(text.replace(/\/$/, ''));
            }
        })
    };

    $.fn.dealPeopleNum = function () {
        var $this = $(this);
        if ($this.size() < 1) {
            return;
        }
        $(this).each(function (i, item) {
            var texts = $(item).text();
            if (texts) {
                var num = texts.split('/');
                $(item).text(num.length)
            }
        })
    };


    $.fn.dealPeopleNum = function () {
        var $this = $(this);
        if ($this.size() < 1) {
            return;
        }
        $(this).each(function (i, item) {
            var texts = $(item).text();
            if (texts) {
                var num = texts.split('/');
                $(item).text(num.length)
            }
        })
    };

    $.fn.textareaLineFeed = function () {
        if($(this).size() < 1){
            return false;
        }
        $(this).each(function () {
            var text = $(this).html();
            if(text){
                text = text.replace(/\n/g, '<br/>');
            }
            $(this).html(text)
        })
    }

    $('[data-toggle="pNum"]').dealPeopleNum();
    $('.deal-separator').dealSeparator();
    $('[data-line-feed="textarea"]').textareaLineFeed()
})


function loginOuted() {
    dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
        buttons: [{
            'text': '确定',
            'class': 'btn btn-sm btn-small btn-primary',
            'click': function () {
                $(this).dialog('close')
            }
        }]
    })
}


try {
    jQuery.validator.addMethod("positiveNumber", function (value, element) {
        return this.optional(element) || value > 0;
    }, "请填写大于0的数字");
}catch (e){

}
