$(function () {
    var $downFileUl = $('#downFileUl');
    var wtypes = window.wtypes;


    //设置下载文档地址
    function setFileDocUrl() {
        var proId = $('#proId').val();
        var proCate = $('#proCate').val();
        var proNtype = $('#proNtype').val();

        var name, href, type;
        var wprefix = window.wprefix;
        var id;
        try {
            getWtparam().success(function (data) {
                $.each(JSON.parse(data.wtypes), function (i, item) {
                    var tpl = item.tpl;
                    if ((item.type.key == proCate) && (item.tpl.key == proNtype)) {
                        name = item.name;
                        type = item.key;
                        return false;
                    }
                });
                href = '/f/proprojectmd/proModelMd/ajaxWord?proId=' + proId + '&type=' + type;
                $downFileUl.find('a').attr('href', href).find('.accessory-name').text(data.wprefix + name);
            })

        } catch (e) {
            console.log(e)
        }

    }

    function getWtparam() {
        return $.ajax({
            type: 'GET',
            url: '/f/proprojectmd/proModelMd/ajaxWtparam',
        })
    }


    setFileDocUrl()

});


function checkIsToLogin(data) {
    return typeof data == 'string' && data.indexOf("id=\"imFrontLoginPage\"") > -1;
}
function dialogTimeout() {
    dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
        buttons: [{
            text: '确定',
            'class': 'btn btn-primary',
            click: function () {
                location.reload();
            }
        }]
    });
}

//检查是否有附件
function hasFj() {
    var $accessoryListPdf = $('#accessories');
    return $accessoryListPdf.find('.accessory-file').size() > 0;
}
function noFj() {
    dialogCyjd.createDialog(0, '请上传资料')
}

function midSubmit(obj) {
    var fj = hasFj();
    if (fj) {
        dialogCyjd.createDialog(0, '提交后不可修改，是否继续？', {
            buttons: [{
                text: '继续',
                'class': 'btn btn-primary',
                click: function () {
                    $(this).dialog('close');
                    $(obj).prop('disabled', true);
                    $("#competitionfm").ajaxSubmit(function (data) {
                        isLogin = checkIsToLogin(data);
                        if (isLogin) {
                            $(obj).prop('disabled', false);
                            dialogTimeout();
                        } else {
                            if (data.ret) {
                                $('.accessories').find('.btn-delete-newaccessory').detach();
                            }
                            dialogCyjd.createDialog(data.ret, data.msg, {
                                buttons: [{
                                    'text': '确定',
                                    'class': 'btn btn-primary',
                                    click: function () {
                                        location.href = '/f/project/projectDeclare/list'
                                    }
                                }]
                            });
                        }
                    })
                }
            }, {
                text: '取消',
                'class': 'btn btn-default',
                click: function () {
                    $(this).dialog('close');
                }
            }]
        })
    } else {
        noFj();
    }
}

function closeSubmit(obj) {
    var fj = hasFj();
    if (fj) {
        dialogCyjd.createDialog(0, '提交后不可修改，是否继续？', {
            buttons: [{
                text: '继续',
                'class': 'btn btn-primary',
                click: function () {
                    $(this).dialog('close');
                    $(obj).prop('disabled', true);
                    $("#competitionfm").ajaxSubmit(function (data) {
                        isLogin = checkIsToLogin(data);
                        if (isLogin) {
                            $(obj).prop('disabled', false);
                            dialogTimeout();
                        } else {
                            if (data.ret) {
                                $('.accessories').find('.btn-delete-newaccessory').detach();
                            }
                            dialogCyjd.createDialog(data.ret, data.msg, {
                                buttons: [{
                                    'text': '确定',
                                    'class': 'btn btn-primary',
                                    click: function () {
                                        location.href = '/f/project/projectDeclare/list'
                                    }
                                }]
                            });
                        }
                    })
                }
            }, {
                text: '取消',
                'class': 'btn btn-default',
                click: function () {
                    $(this).dialog('close');
                }
            }]
        })
    } else {
        noFj();
    }
}