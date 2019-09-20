$(function () {
    var $btnUpload = $('#btnUpload');
    var $goNextBtns = $('.go-next');
    var $goPrevBtns = $('.go-prev');
    var $saveNext = $('.save-next');
    var $showStepNumber = $('#showStepNumber');
    var showStepNumber = 0;
    var flag = false;
    var $proSource = $('#proSource');
    var $sourceRow = $('.source-row');
    var $comForm = $("#competitionfm");
    var $downFileUl = $('#downFileUl');
    var wtypes = window.wtypes;
    var $proModelMdId = $("#proModelMdId");
    var $proModelId = $('#proModelId');
    var $proCategory = $(".proModelProCateGoryBox select");
    var $accessoryListPdf = $('#accessoryListPdf');
    var $contestContents = $('.contest-content');
    var $hasDisabled = $('#hasDisabled');
    var $hasDisableds = $('.has-disabled');
    var $fujianContent = $("#fujianpp");
    var $stepIndicator = $('.step-indicator');
    var $steps = $stepIndicator.find('.step');
    var $saveApplyForm = $('#saveApplyForm');
    var $submitApplyForm = $('#submitApplyForm');
    var isDownFileHref = false;
    var nextStep = false;
    var $proModelTeamId = $('.proModelTeamId');
    var currentStepIndex = '';
    var validate1 = $comForm.validate();
    var $goBackStepOne = $('#goBackStepOne');
    var $saveStepOne = $('#saveStepOne');
    var $saveStepTwo = $('#saveStepTwo');
    var $btnStepOne = $('#btnStepOne');
    var submitFlag = false;
    var isSubmit = false;
    //初始化是或否能填写
    // var checkType = $('#isSubmit').val();
    // if (checkType == "0") {
    //     checkOnInit();
    // }


    // $proModelTeamId.change()
    if ($hasDisabled.val() == '1') {
        $proModelTeamId.change()
        $hasDisableds.prop('disabled', true);
        // $proModelTeamId.change()
    }

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

    //第一步保存
    $saveStepOne.on('click', function (e, params, $dialogObj) {
        var referrer = document.referrer;
        $saveStepOne.prop('disabled', true);
        $comForm.attr('action', '/f/proprojectmd/proModelMd/ajaxSave');
        $comForm.ajaxSubmit(function (data) {
            isLogin = checkIsToLogin(data);
            if (isLogin) {
                dialogTimeout();
            } else {
                downFileHref(data);
                showModalMessage(data.ret, data.msg);
            }
            $saveStepOne.prop('disabled', false);
            if ($dialogObj) {
                $dialogObj.dialog('close');
                if (referrer.indexOf('login')) {
                    location.href = '/f';
                } else {
                    history.go(-1);
                }
            }
        })
    });

    //返回
    $goBackStepOne.on('click', function (e) {
        //如果提交了直接返回
        var referrer = document.referrer;
        if (isSubmit || $hasDisabled.val() == '1') {
            if (referrer.indexOf('login')) {
                location.href = '/f';
            } else {
                history.go(-1);
            }
            return false;
        }
        showModalMessage(0, '是否保存已填写的好的表单？', [{
            text: '确定',
            click: function (e) {
                $saveStepOne.trigger('click', ['goBack', $(this)])
            }
        }, {
            text: '取消',
            click: function (e) {
                $(this).dialog('close');
                if (referrer.indexOf('login')) {
                    location.href = '/f';
                } else {
                    history.go(-1);
                }
            }
        }])
    });

    //第二部保存
    $saveStepTwo.on('click', function (e) {
        $saveStepTwo.prop('disabled', true);
        $comForm.attr('action', '/f/proprojectmd/proModelMd/ajaxSave');
        $comForm.ajaxSubmit(function (data) {
            isLogin = checkIsToLogin(data);
            if (isLogin) {
                dialogTimeout();
            } else {
                downFileHref(data);
                showModalMessage(data.ret, data.msg);
            }
            $saveStepTwo.prop('disabled', false);
        })
    });

    // showStep(showStepNumber);

    $steps.eq(showStepNumber).trigger('click');

    //设置downFile
    downFileHref();


    $btnStepOne.on('click', function(){
        var isLogin;
        if(checkType == '1'){
            showStep(1);
        }else{
            if(!validate1.form()){
                return false
            }
            $comForm.attr('action', '/f/proprojectmd/proModelMd/ajaxSave');
            $comForm.ajaxSubmit(function (data) {
                isLogin = checkIsToLogin(data);
                if (isLogin) {
                    dialogTimeout();
                } else {
                    if(data.ret==1){
                        downFileHref(data);
                        addTeamRules()
                        showStep(1);
                        // checkTeam();
                    }
                    if(data.ret==0){
                        showModalMessage(data.ret, data.msg);
                    }
                }
                $saveStepOne.prop('disabled', false);
                // if(data.ret==1){
                // 	$steps.eq(1).trigger('click');
                // }

            })
        }


    })

    $stepIndicator.on('click', 'a.step', function (e) {
        var index, $this;
        e.preventDefault();
        $this = $(this);
        index = $steps.index($this);
        // if (index != 0 && !$this.prev().hasClass('completed')) {
        //     return false;
        // }
        // showStep(index);
        if(checkType == '1'){
            showStep(index);
        }else{
            if (validate1.form()) {
                $showStepNumber.val(index);
                showStep(index);
                downFileHref()
            }
        }

    });

    $steps.eq(showStepNumber).trigger('click');

    $(document).on('click', '.go-prev', function (e) {
        var $this, index, $contestContent;
        e.preventDefault();
        $this = $(this);
        $contestContent = $this.parents('.contest-content');
        index = $contestContents.index($contestContent);
        index--;
        if (index == 0 || index == 1) {
            removeTeamRules();
        }
        $steps.eq(index).trigger('click');
    });

    $(document).on('click', '.go-next', function (e) {
        var $this, index, $contestContent;
        e.preventDefault();
        $this = $(this);
        $contestContent = $this.parents('.contest-content');
        index = $contestContents.index($contestContent);

        if(index == 0){
        	return false;
        }

        index++;
        currentStepIndex = index;
        //index = 0为第一个页面 index = 1为第二个页面


        $steps.eq(index).trigger('click');

        setTimeout(function () {
            if (currentStepIndex == 1) {
                checkTeam();
            }
        }, 0);


        if (index <= 2) {
            nextStep = true;
            if ($hasDisabled.val() != '1') {
                if (!isSubmit) {
                    if (validate1.form()) {
                        $(this).prop('disabled', true);
                        $saveApplyForm.trigger('click', ['stepSave', $this]);
                    } else {
                        $(this).prop('disabled', false);
                    }
                }
            }
        } else {
            nextStep = false;
        }
    });

    //下载申报资料
    $accessoryListPdf.on('click', 'a', function (e) {
        e.preventDefault();
        var url = $(this).attr("data-ftp-url");
        var fileName = $(this).attr("data-title");
        location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
    });


    $saveApplyForm.on('click', function (e, params, $goNext) {
        var isLogin;
        var $this = $(this);
        var isTrigger = (params && params == 'stepSave');
        if (isTrigger) {
            $comForm.attr('action', '/f/proprojectmd/proModelMd/ajaxSave');
        } else {

            // if ($accessoryListPdf.find('a').size() < 1) {
            //     showModalMessage(0, '请上传申报资料');
            //     return false;
            // }
            $comForm.attr('action', '/f/proprojectmd/proModelMd/save');
            $(this).prop('disabled', true);
        }


        $comForm.ajaxSubmit(function (data) {
            isLogin = checkIsToLogin(data);
            if (isLogin) {
                dialogTimeout();
            } else {
                // $("input[name='proModel.attachMentEntity.fielFtpUrl']").val(data.fileUrl);
                downFileHref(data);
                if (!isTrigger) {
                    showModalMessage(data.ret, data.msg);
                    $("input[name='proModel.attachMentEntity.fielFtpUrl']").val(data.fileUrl);
                    var tema = $($("#accessoryListPdf").find("a")[0]);
                    tema.attr("data-url", data.fileHttpUrl);
                    tema.attr("data-ftp-url", data.fileUrl);
                    tema.attr("data-id", data.fileId);
                }
            }
            if (currentStepIndex == 1 && isTrigger) {
                addTeamRules()
            }
            $this.prop('disabled', false);
            $goNext && $goNext.prop('disabled', false);
        })
    });

    $submitApplyForm.on('click', function (e) {
        if (validate1.form()) {
            if ($accessoryListPdf.find('a').size() < 1) {
                showModalMessage(0, '请上传申报资料');
                return false;
            }
            dialogConfirmSave();
        }
    });

    function showStep(number) {
        $contestContents.eq(number).removeClass('hide').siblings().addClass('hide');
        $steps.eq(number).addClass('completed').nextAll().removeClass('completed');
        $steps.eq(number).prev().addClass('completed');
        // $steps.eq(showStepNumber).next().removeClass('completed');
        // $steps.eq(showStepNumber).prev().addClass('completed');
        //初始化上传申报

        createUploader();
    }

    function createUploader() {
        if ($hasDisabled.val() == '1') {
            return false;
        }
        if (!flag && !$btnUpload.parents('.contest-content').hasClass('hide')) {
            flag = true;
            $btnUpload.uploadAccessory({
                hasFiles: false,
                isImagePreview: false,
                multiple: false
            });
        }
    }

    function downFileHref(data) {
        var id;
        var promodelId;
        var corrId;
        if (data) {
            id = data.id;
            promodelId = data.proModelId;
            $proModelMdId.val(id);
            $proModelId.val(promodelId);
        }
        corrId = id || $proModelMdId.val();
        setDownFileHref(corrId);
    }

    function setDownFileHref(id) {
        var name, href, type;
        var wprefix = window.wprefix;
        $.each(JSON.parse(wtypes), function (i, item) {
            var tpl = item.tpl;
            if ((item.type.key == $proCategory.val()) && (item.tpl.key == "1")) {
                name = item.name;
                type = item.key;
                return false;
            }
        });
        href = '/f/proprojectmd/proModelMd/ajaxWord?proId=' + id + '&type=' + type;
        $downFileUl.find('a').attr('href', href).text(wprefix + name);
        isDownFileHref = true;
    }

    function checkIsToLogin(data) {
        return typeof data == 'string' && data.indexOf("id=\"imFrontLoginPage\"") > -1;
    }

    function dialogTimeout() {
        showModalMessage(0, '未登录或登录超时。请重新登录，谢谢！', [{
            text: '確定',
            click: function () {
                location.reload();
            }
        }]);
    }

    function dialogConfirmSave() {
        $saveApplyForm.prop('disabled', true);
        $submitApplyForm.prop('disabled', true);
        $btnUpload.attr('disabled', '');
        $saveStepOne.prop('disabled', true);
        $saveStepTwo.prop('disabled', true);
        $('.icon-remove-sign').hide();
        showModalMessage(0, '提交后无法编辑', [{
            text: '确定',
            click: function () {
                if (!submitFlag) {
                    submitFlag = true;
                    $comForm.attr('action', '/f/proprojectmd/proModelMd/submit');
                    $comForm.ajaxSubmit(function (data) {
                        if (checkIsToLogin(data)) {
                            dialogTimeout();
                        } else {

                            $('.icon-remove-sign').detach();
                            if (data.ret == 1) {
                                isSubmit = true;
                                $saveApplyForm.prop('disabled', true);
                                $submitApplyForm.prop('disabled', true);
                                $hasDisableds.prop('disabled', true).find('input[name="file"]').prop('disabled', true);
                                $btnUpload.attr('disabled', '');
                                $saveStepOne.prop('disabled', true);
                            }else{
                                $saveApplyForm.prop('disabled', false);
                                $submitApplyForm.prop('disabled', false);
                                $hasDisableds.prop('disabled', false).find('input[name="file"]').prop('disabled', false);
                                $btnUpload.removeAttr('disabled');
                                $saveStepOne.prop('disabled', false);
                            }
                            showModalMessage(data.ret, data.msg);
                        }

                        submitFlag = false;
                    })
                }
            }
        }, {
            text: '取消',
            click: function () {
                isSubmit = false;
                $saveApplyForm.prop('disabled', false);
                $submitApplyForm.prop('disabled', false);
                // $hasDisableds.prop('disabled', true).find('input[name="file"]').prop('disabled', true);
                $btnUpload.removeAttr('disabled');
                $saveStepOne.prop('disabled', false);
                $saveStepTwo.prop('disabled', false);
                $('.icon-remove-sign').show();
                $(this).dialog('close');
            }
        }])
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

    function checkTeam() {
        if (isSubmit || $hasDisabled.val() == '1') {
            return false;
        }
        if ($("select[id='proModel.teamId']").find("option").length <= 1) {
            showModalMessage(0, "无可用团队信息，立即建设团队？", {
                确定: function () {
                    top.location = "/f/team/indexMyTeamList";
                },
                // 取消: function () {
                //     $(this).dialog("close");
                // }
            });
            return;
        }
    }


    function checkOnInit() {
        var user_officename = $.trim($("#college").val());
        var user_mobile = $.trim($("#mobile").val());
        var user_email = $.trim($("#email").val());
        if (!user_officename || !user_mobile || !user_email) {
            showModalMessage(0, "个人信息未完善，立即完善个人信息？", {
                确定: function () {
                    top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                },
                // 取消: function () {
                //     $(this).dialog("close");
                // }
            });
            return;
        }
        if (!mobileRegExp.test(user_mobile)) {
            showModalMessage(0, "个人信息未完善，立即完善个人信息？", {
                确定: function () {
                    top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                },
                // 取消: function () {
                //     $(this).dialog("close");
                // }
            });
            return;
        }
        if (!emailRegExp.test(user_email)) {
            showModalMessage(0, "个人信息未完善，立即完善个人信息？", {
                确定: function () {
                    top.location = "/f/sys/frontStudentExpansion/findUserInfoById?custRedict=1&isEdit=1";
                },
                // 取消: function () {
                //     $(this).dialog("close");
                // }
            });
            return;
        }

    }

    // 正整数
    jQuery.validator.addMethod("positiveNum", function (value, element) {
        value = parseInt(value);
        return this.optional(element) || value >= 0;
    }, '金额为正整数');
})

