//tab切换
$.fn.stepTab = function (option) {
    var $this = $(this);

    var options = $.extend({}, $.fn.stepTab.default, option);
    var stepIndex = options.stepIndex;
    var $steps = $this.find('.step');
    var isSubmit = options.isSubmit;
    // var $stepContent = $this.find('.step-indicator');
    var $contestContent = $('.contest-content');


    showTab(stepIndex);

    function showTab(index, previousStep, init) {

        var $showStep = $steps.eq(index);
        var showBeforeEvent = $.Event('showBefore', {
            relatedTarget: $showStep[0]
        });
        if (index < 0) {
            return
        }
        //提交后将submit 设置为1
        if (!isSubmit) {
            isSubmit = $this.attr('data-submit') == '1';
            // console.log(isSubmit)
        }

        if ($showStep.hasClass('active')) {
            return;
        }


        if (!isSubmit) {
            if (previousStep == 'nextStep') {
                $showStep.trigger(showBeforeEvent);
            }
        }


        if (showBeforeEvent.isDefaultPrevented()) {
            return;
        }


        $showStep.addClass('completed active').nextAll().removeClass('completed');
        $showStep.siblings().removeClass('active');
        $showStep.prev().addClass('completed');
        if (index < 0) {
            $contestContent.eq(index).removeClass('hide')
        } else {
            $contestContent.eq(index).removeClass('hide').siblings().addClass('hide');
        }
        if (index == 2) {
            $contestContent.css('height', '')
        }


        var showAfterEvent = $.Event('showAfter', {
            relatedTarget: $showStep[0]
        });

        setTimeout(function () {
            if (!isSubmit) {
                if (previousStep == 'success' || previousStep == 'nextStep') {
                    $showStep.trigger(showAfterEvent);
                }
            }
        }, 0)
    }

    $(document).on('click.step', 'a.step', function (e, previousStep) {
        e.preventDefault();
        var index = $steps.index($(this));
        var currentActiveIndex = $steps.index($('a.active'));
        if (Math.abs(currentActiveIndex - index) > 1) {
            return false;
        }
        if (previousStep) {
            showTab(index, previousStep);
        }
    })
};

$.fn.stepTab.default = {
    stepIndex: 0,
    isSubmit: false
};


$(function () {
    var $stepIndicator = $('.step-indicator');
    var $steps = $stepIndicator.find('.step');
    var $goPrevious = $('.go-prev');
    var $goNextSteps = $('.go-next');

    //步
    var $stepOne = $steps.eq(0);
    var $stepTwo = $steps.eq(1);
    var $stepThree = $steps.eq(2);

    var $btnUpload = $('#btnUpload');
    var uploaderFlag = false;
    var checkType = $('#isSubmit').val() == '1';

    var $comForm = $("#competitionfm");

    //初始进入的
    var $showStepNumber = $('#showStepNumber');
    var showStepNumber = $showStepNumber.val();

    var $sourceRow = $('.source-row');
    var $proModelTeamId = $('.proModelTeamId');
    var comFormValidate = $comForm.validate();

    var $downFileUl = $('#downFileUl');
    var wtypes = window.wtypes;
    var $proCategory = $(".proModelProCateGoryBox select");


    var $saveApplyForm = $('#saveApplyForm');

    var $accessoryListPdf = $('#accessories');
    var $hasDisableds = $('.has-disabled');

    var $fujianContent = $("#fujianpp");
    var $submitApplyForm = $('#submitApplyForm');
    var $proSource = $('#proSource');
    var submitFlag = false;
    var $goBackStepOne = $('#goBackStepOne');
    var $proModelGzxmxxId = $("#proModelGzxmxxId");
    var $proModelId = $('#proModelId');
    var lHref = location.href;
    var lStep = getQueryString('step', lHref)
    //初始化是或否能填写
    //第一步    下一步
    // var $btnStepOne = $('#btnStepOne');


    //创建tab


    $stepIndicator.stepTab({
        stepIndex: (lStep || -1),
        isSubmit: checkType
    });

    if (lStep) {
        $proModelTeamId.change();
        setFileDocUrl();
    }

    // if (checkType) {
    //     $proModelTeamId.change();
    //     setFileDocUrl();
    //     $hasDisableds.prop('disabled', true);
    // } else {
    //     onGcontestApplyInit($("[id='actYwId']").val(), $("[id='proModelId']").val(), checkOnInit);
    // }


    //是否有项目来源 并开始调用一次
    $proSource.on('change', function (e) {
        var value = $(this).val();
        if (value == 'B' || value == 'C') {
            $sourceRow.removeClass('hide');
            addRules()
        } else {
            $sourceRow.addClass('hide');
            removeRules();
        }
    }).change();

    $goPrevious.on('click', function (e) {
        var index = $steps.index($('a.step.active'));
        //上一步不触发事件
        index--;
        $steps.eq(index).trigger('click', ['prevStep'])
    });

    $goBackStepOne.on('click', function (e) {
        var referrer = document.referrer;
        if (submitFlag || checkType) {
            if (referrer.indexOf('login')) {
                location.href = '/f';
            } else {
                history.go(-1);
            }
            return false;
        }
        dialogCyjd.createDialog(0, '是否保存已填写的好的表单？', {
            buttons: [{
                text: '确定',
                'class': 'btn btn-primary',
                click: function (e) {
                    // saveBefore(true);
                    $(this).dialog('close');
                    saveStepForm(function success() {
                        if (referrer.indexOf('login')) {
                            location.href = '/f';
                        } else {
                            history.go(-1);
                        }
                    }, function reject() {
                    })
                }
            }, {
                text: '取消',
                'class': 'btn btn-default',
                click: function (e) {
                    $(this).dialog('close');
                    if (referrer.indexOf('login')) {
                        location.href = '/f';
                    } else {
                        history.go(-1);
                    }
                }
            }]
        })
    });

    //下一步
    $goNextSteps.on('click', function (e) {
        var index = $steps.index($('a.step.active'));
        index++;
        $steps.eq(index).trigger('click.step', ['nextStep'])
    });


    //第二步出现前
    $stepTwo.on('showBefore', function (e) {
        //设置为已提交不会触发showBefore showAfter事件
        // var hasTeam = hasTeam();
        // $("[id='proModel.teamId']").hide();
        var $proModelTeamEle = $("[id='proModel.teamId']");
        var $errorEle = $proModelTeamEle.parent().find('span.error');
        // $errorEle.hide().find('a').text('').attr('href', '');
        if (!checkType) {
            if (!submitFlag) {
                if (comFormValidate.form()) {
                    if ($errorEle.find('a').attr('href')) {
                        $steps.eq(1).trigger('click', ['success']);
                        $errorEle.hide().find('a').text('').attr('href', '');
                        $proModelTeamId.val('');
                    } else {
                        // saveBefore(true);
                        saveStepForm(function success(data) {
                            setFileDocUrl(data);
                            console.log('first step success');
                            $steps.eq(1).trigger('click', ['success']);
                            if (hasTeam() && $('.studenttb tbody tr').size() < 1) {
                                $proModelTeamId.change();
                            }
                        }, function error(error) {

                        }, '1');
                    }
                }
                return false;
            }

        }
    });

    //第二部出现后
    $stepTwo.on('showAfter', function (e) {
        //出现后添加规则
        addTeamRules();
        checkTeam();

    });

    //第三部
    $stepThree.on('showBefore', function (e) {
        if (!comFormValidate.form()) {
            return false;
        }
        var type = $("select[id='proModel.proCategory']").val();
        var subtype = $("[id='proModel.type']").val();
        var studentSize = $('.studenttb tbody tr').size();
        var $proModelTeamEle = $("[id='proModel.teamId']");
        var $errorEle = $proModelTeamEle.parent().find('span.error');
        var proModelTeamVal = $proModelTeamEle.val();
        var $error;
        if ($errorEle.find('a').attr('href')) {
            return false;
        }


        /*  if ((type == "1" || type == "2") && (studentSize > 5 || studentSize < 1)) {
         $error = $('<span for="proModel.teamId" class="error"><a href="' + ctxFront + '/team/indexMyTeamList?opType=2&id='+proModelTeamVal+'&proType='+type+'&type='+subtype+'">创新创业训练学生团队为1-5人</a></span>');
         if($errorEle.size() > 0){
         $errorEle.show().find('a').attr('href', '/f/team/indexMyTeamList?opType=2&id='+proModelTeamVal+'&proType='+type+'&type='+subtype).text('创新创业训练学生团队为1-5人')
         }else{
         $proModelTeamEle.parent().append($error)
         }
         return false;
         }
         if (type == "3" && (studentSize < 1 || studentSize > 7)) {
         $error = $('<span for="proModel.teamId" class="error"><a href="' + ctxFront + '/team/indexMyTeamList?opType=2&id='+proModelTeamVal+'&proType='+type+'&type='+subtype+'">创业实践学生团队为1-7人</a></span>')
         if($errorEle.size() > 0){
         $errorEle.show().find('a').attr('href', '/f/team/indexMyTeamList?opType=2&id='+proModelTeamVal+'&proType='+type+'&type='+subtype).text('创业实践学生团队为1-7人')
         }else{
         $proModelTeamEle.parent().append($error)
         }
         return false;
         }
         */
        if (!checkType) {
            if (!submitFlag) {
                // saveBefore(true);
                saveStepForm(function success(data) {
                    console.log('second step success');
                    setFileDocUrl(data)
                    $steps.eq(2).trigger('click', ['success'])
                }, function error(error) {

                }, '2');
                return false;
            }
        }

    });

    $stepThree.on('showAfter', function (e) {
        //初始化上传
        // createUploader()
    });

    $saveApplyForm.on('click', function (e) {

        saveApplyForm(function success(data) {
            // $("input[name='proModel.attachMentEntity.fielFtpUrl']").val(data.fileUrl);
            // var tema = $($("#accessoryListPdf").find("a")[0]);
            // tema.attr("data-url", data.fileHttpUrl);
            // tema.attr("data-ftp-url", data.fileUrl);
            // tema.attr("data-id", data.fileId);
            // dialogCyjd.createDialog(data.ret, data.msg);
            dialogCyjd.createDialog(data.ret, data.msg, {
                dialogClass: 'dialog-cyjd-container none-close',
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        location.href = '/f/project/projectDeclare/list'
                    }
                }]
            });
            // console.log('three step success');
        }, function reject(error) {
        })
    });

    //提交
    $submitApplyForm.on('click', function (e) {
        var fj = hasFj();
        if (fj) {
            submitForm();
        } else {
            noFj()
        }
    });

    function saveBefore(isAll) {
        $hasDisableds.prop('disabled', true);
        $btnUpload.find('input[name="file"]').prop('disabled', true);
        $btnUpload.attr('disabled', '');
        if (isAll) {
            $goNextSteps.prop('disabled', true);
            $goPrevious.prop('disabled', true);
        }
        $saveApplyForm.prop('disabled', true);
        $submitApplyForm.prop('disabled', true);
    }

    function saveAfter(isAll) {
        $hasDisableds.prop('disabled', false);
        $btnUpload.find('input[name="file"]').prop('disabled', false);
        $btnUpload.removeAttr('disabled');
        if (isAll) {
            $goNextSteps.prop('disabled', false);
            $goPrevious.prop('disabled', false);
        }
        $saveApplyForm.prop('disabled', false);
        $submitApplyForm.prop('disabled', false);
    }

    //存储下一步表单
    function saveStepForm(resolve, reject, step) {
        var isLogin;
        var queryString = $comForm.formSerialize();
        var xhr;
        if (step == "2") {
            xhr = $.post('/f/proprojectgzsmxx/proModelGzxmxx/ajaxSave2', queryString);
        } else {
            xhr = $.post('/f/proprojectgzsmxx/proModelGzxmxx/ajaxSave', queryString);
        }
        xhr.success(function (data) {
            isLogin = checkIsToLogin(data);
            if (isLogin) {
                dialogTimeout();
            } else {
                if (data.ret == 1) {
                    resolve && resolve(data);
                } else if (data.ret == 0) {
                    reject && reject(data);
                    // dialogCyjd.createDialog(data.ret, data.msg);
                }
            }
            saveAfter(true);
        });
        xhr.error(function (error) {
            reject && reject(error);
            saveAfter(true);
        });
        return xhr;
    }

    //保存apply
    function saveApplyForm(resolve, reject) {
        var isLogin;
        var queryString = $comForm.serialize();
        var xhr;
        saveBefore(true);
        xhr = $.post('/f/proprojectgzsmxx/proModelGzxmxx/save', queryString);
        xhr.success(function (data) {
            isLogin = checkIsToLogin(data);
            if (isLogin) {
                dialogTimeout();
            } else {
                if (data.ret == 1) {
                    resolve && resolve(data);
                } else if (data.ret == 0) {
                    reject && reject(data);
                    // dialogCyjd.createDialog(data.ret, data.msg);
                }
            }
            saveAfter(true);
        });
        xhr.error(function (error) {
            saveAfter(true);
            reject && reject(error);

        });
        return xhr;
    }

    //保存apply
    function submitApplyForm(resolve, reject) {
        var isLogin;
        var queryString = $comForm.serialize();
        saveBefore(true);
        var xhr = $.post('/f/proprojectgzsmxx/proModelGzxmxx/submit', queryString);
        xhr.success(function (data) {
            isLogin = checkIsToLogin(data);
            if (isLogin) {
                dialogTimeout();
            } else {
                if (data.ret == 1) {
                    resolve && resolve(data);
                } else if (data.ret == 0) {
                    reject && reject(data);
                    // saveAfter(false);
                }
            }
        });
        xhr.error(function (error) {
            reject && reject(error);

        });
        return xhr;
    }

    //提交函数
    function submitForm() {
        dialogCyjd.createDialog(0, '提交后不可修改，是否继续？', {
            buttons: [{
                text: '继续',
                'class': 'btn btn-primary',
                click: function () {
                    submitFlag = false;
                    if (!submitFlag) {
                        // saveBefore(true);
                        $goNextSteps.prop('disabled', false);
                        $goPrevious.prop('disabled', false);
                        $(this).dialog('close');
                        submitApplyForm(function success(data) {
                            $('.icon-remove-sign').detach();
                            dialogCyjd.createDialog(data.ret, data.msg, {
                                dialogClass: 'dialog-cyjd-container none-close',
                                buttons: [{
                                    text: '跟踪当前项目',
                                    'class': 'btn btn-sm btn-primary',
                                    'click': function () {
                                        top.location = '/f/project/projectDeclare/curProject#/'+data.proProjectId+'?type='+data.pptype +'&actYwId='+data.actywId +'&projectId='+data.projectId ;
                                    }
                                }, {
                                    text: '返回',
                                    'class': 'btn btn-sm btn-default',
                                    'click': function () {
                                        top.location = "/f/project/projectDeclare/list";
                                    }
                                }]
                            });
                            submitFlag = true;
                            $goNextSteps.prop('disabled', false);
                            $goPrevious.prop('disabled', false);
                        }, function reject() {
                            saveAfter(true);
                            submitFlag = false;
                        })
                        // $goNextSteps.prop('disabled', false);
                        // $goPrevious.prop('disabled', false);
                        // $(this).dialog('close');
                        // submitFlag = true;
                        // console.log(submitFlag)
                    }

                }
            }, {
                text: '取消',
                'class': 'btn btn-default',
                click: function () {
                    $(this).dialog('close');
                    saveAfter(true);
                }
            }]
        })
    }

    //设置下载文档地址
    function setFileDocUrl(data) {
        var name, href, type;
        var wprefix = window.wprefix;
        var id;


        try {
            getWtparam().success(function (data) {
                $.each(JSON.parse(data.wtypes), function (i, item) {
                    var tpl = item.tpl;
                    if ((item.type.key == $proCategory.val()) && (item.tpl.key == "1")) {
                        name = item.name;
                        type = item.key;
                        return false;
                    }
                });
                href = '/f/proprojectgzsmxx/proModelGzxmxx/ajaxWord?proId=' + id + '&type=' + type;
                $downFileUl.find('a').attr('href', href).find('.accessory-name').text(data.wprefix + name);
            })
        } catch (e) {
            console.log(e)
        }
        if (data) {
            id = data.id;
            $proModelGzxmxxId.val(data.proModelId);
            $proModelId.val(data.proModelId);
        } else {
            id = $proModelGzxmxxId.val();
        }

    }

    function getWtparam() {
        return $.ajax({
            type: 'GET',
            url: '/f/proprojectgzsmxx/proModelGzxmxx/ajaxWtparam',
        })
    }

    function checkIsToLogin(data) {
        return typeof data == 'string' && data.indexOf("id=\"imFrontLoginPage\"") > -1;
    }

    function dialogTimeout() {
        dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
            buttons: [{
                text: '确定',
                'class': 'btn btn-primary',
                click: function () {
                    $(this).dialog('close');
                    saveAfter(true);
                    location.reload();
                }
            }]
        });
    }

    //初始化一次
    // if (showStepNumber == '2') {
    //     createUploader();
    // }

    //创建uploader
    function createUploader() {
        if (checkType) {
            return false;
        }
        if (!uploaderFlag && !$btnUpload.parents('.contest-content').hasClass('hide')) {
            uploaderFlag = true;
            $btnUpload.uploadAccessory({
                hasFiles: false,
                isImagePreview: false,
                multiple: false
            });
        }
    }

    function addRules() {
        $sourceRow.find('input').rules('add', {required: true});
        $sourceRow.find('select').rules('add', {required: true})
    }

    function removeRules() {
        $sourceRow.find('input').rules('remove');
        $sourceRow.find('select').rules('remove');
    }

    function addTeamRules() {
        $proModelTeamId.rules('add', {required: true})
    }

    function removeTeamRules() {
        // $('input[name="proModel.teamId"]').rules('remove');
        $proModelTeamId.rules('remove')
    }


    function hasTeam() {
        return $proModelTeamId.find("option").length > 1;
    }

    //检查是否有团队
    function checkTeam() {
        if (!hasTeam()) {
            var type = $("select[id='proModel.proCategory']").val();
            var studentSize = $('.studenttb tbody tr').size();
            var $proModelTeamEle = $("[id='proModel.teamId']");
            var $errorEle = $proModelTeamEle.parent().find('span.error');
            var proModelTeamVal = $proModelTeamEle.val();
            var $error;
            $error = $('<span for="proModel.teamId" class="error"><a href="' + ctxFront + '/team/indexMyTeamList?proType=' + type + '">没有团队,请去创建团队</a></span>')
            if ($errorEle.size() > 0) {
                $errorEle.show().find('a').text('没有团队,请去创建团队')
            } else {
                $proModelTeamEle.parent().append($error)
            }
        }
    }

    //检查是否有附件
    function hasFj() {
        return $accessoryListPdf.find('.accessory-file').size() > 0;
    }

    //没有附件
    function noFj() {
        dialogCyjd.createDialog(0, '请上传申报资料')
    }


    //文件上传相关
    $btnUpload.on('beforeFileQueued', function (e) {
        var $accessoryListPdf = $('#accessoryListPdf');
        var $li = $accessoryListPdf.find('li');
        $li.size() > 0 && $li.detach();
        console.log(e.file)
    });

    $btnUpload.on('uploadSuccess', function (e) {
        var uploader = e.uploader;
        $fujianContent.html("");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielSize' value='" + e.response.size + "'/>");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielTitle' value='" + e.response.title + "'/>");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielType' value='" + e.response.type + "'/>");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielFtpUrl' value='" + e.response.ftpUrl + "'/>");
        uploader.reset();
    });

    //删除之前
    $btnUpload.on('deleteFileBefore', function (e) {
        $accessoryListPdf.find('li').hide();
    });

    //删除完成
    $btnUpload.on('deleteFileComplete', function () {
        // showModalMessage(0, '资料删除成功');
        $fujianContent.empty();
    });

    //删除完成
    $btnUpload.on('deleteFileFail', function () {
        showModalMessage(0, '资料删除失败');
        $accessoryListPdf.find('li').show();
    });


    function checkOnInit() {
        var user_officename = $.trim($("#college").val());
        var user_mobile = $.trim($("#mobile").val());
        var user_email = $.trim($("#email").val());
        if (!user_officename || !user_mobile || !user_email) {
            dialogCyjd.createDialog(0, "个人信息未完善，立即完善个人信息？", {
                buttons: [{
                    'text': '确定',
                    'class': 'btn btn-primary',
                    click: function () {
                        top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                    },
                    // 取消: function () {
                    //     $(this).dialog("close");
                    // }
                }]
            });
            return;
        }
        if (!mobileRegExp.test(user_mobile)) {
            dialogCyjd.createDialog(0, "个人信息未完善，立即完善个人信息？", {
                buttons: [{
                    'text': '确定',
                    'class': 'btn btn-primary',
                    click: function () {
                        top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                    },
                    // 取消: function () {
                    //     $(this).dialog("close");
                    // }
                }]
            });
            return;
        }
        if (!emailRegExp.test(user_email)) {
            dialogCyjd.createDialog(0, "个人信息未完善，立即完善个人信息？", {
                buttons: [{
                    'text': '确定',
                    'class': 'btn btn-primary',
                    click: function () {
                        top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                    },
                    // 取消: function () {
                    //     $(this).dialog("close");
                    // }
                }]
            });
            return;
        }

    }

    // 正整数
    jQuery.validator.addMethod("positiveNum", function (value, element) {
        value = parseInt(value);
        return this.optional(element) || value >= 0;
    }, '金额为正整数');

    //下载申报资料
    $accessoryListPdf.on('click', 'a', function (e) {
        e.preventDefault();
        var url = $(this).attr("data-ftp-url");
        var fileName = $(this).attr("data-title");
        location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
    });

});

