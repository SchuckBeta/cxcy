<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>用户管理</title>
    <!--前台site-decorator-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/common.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/pagination.css">


    <!--无用css-->
    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>--%>

    <!--focus样式表-->
    <!--头部导航公用样式-->

    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>

    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <script src="${ctxCommon}/common-js/jquery.SuperSlide.2.1.1.js" type="text/javascript"></script>
    <!--文本溢出-->
    <script src="${ctxStatic}/common/initiate.js" type="text/javascript"></script>
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <%@include file="/WEB-INF/views/include/treeview.jsp" %>

    <style type="text/css">
        html, body {
            height: 100%;
            overflow: hidden;
        }

        .control-label {
            font-weight: normal;
        }

        span.checkbox-inline label {
            margin: 0;
            font-weight: normal;
        }

        .container-details {
            overflow: hidden;
        }

        .container-details .accordion-group {
            float: left;
            width: 280px;
        }

        .container-details .close-handler {
            float: left;
            width: 8px;
            border: solid #ddd;
            border-width: 0 1px;
            background: #efefef url(${ctxStatic}/images/openclose.png) no-repeat -29px center;
            opacity: 0.5;
            filter: alpha(opacity=50);
            cursor: pointer;
        }

        .container-details .close-handler.close {
            background-position: 1px center;
        }

        .table-user-content {
            margin-left: 288px;
        }

        .searchbar {
            padding: 20px 0;
            border-top: 1px solid #ddd;
        }

        .full-table .accordion-group {
            display: none;
        }

        .full-table .table-user-content {
            margin-left: 8px;
        }

        .container-details {
            height: calc(100% - 71px);
        }

        .container-details .accordion-group {
            height: 100%;
            overflow-y: auto;
            overflow-x: hidden
        }

        .container-details .close-handler {
            height: 100%;
        }

        .container-details .table-user-content {
            height: 100%;
        }

        .accordion-heading a {
            display: block;
            position: relative;
            text-decoration: none;
            line-height: 20px;
            padding: 4px 15px;
            color: #333;
            background-color: #f4e6d4;
            overflow: hidden;
        }

        .qiyedaoshi {
            font-size: 0;
            line-height: 18px;
            height: 18px;
            padding: 2px;
            margin-top: 10px;
            overflow: hidden;
        }

        .m-button {
            line-height: 0;
            margin: 0;
            width: 16px;
            height: 16px;
            display: inline-block;
            vertical-align: middle;
            border: 0 none;
            cursor: pointer;
            outline: 0;
            background: transparent url(${ctxStatic}/jquery-ztree/3.5.12/css/zTreeStyle/img/zTreeStandard.png) no-repeat scroll -92px -54px;
        }

        .ico_open {
            margin-right: 2px;
            background-position: -110px -16px;
            vertical-align: top;
        }

        .qiyedaoshi span, .qiyedaoshi a {
            vertical-align: middle;
        }

        .qiyedaoshi a {
            font-size: 12px;
        }

        .ui-widget.ui-widget-content {
            width: auto !important;
            height: auto !important;
        }

        .form-add-teacher .control-label {
            width: 110px;
        }

        .form-add-teacher .control-label i {
            font-style: normal;
            color: red;
            margin-right: 4px;
        }

        .form-add-teacher .col-xs-6 {
            width: 300px;
        }

        .container-add-teacher {
            width: 420px;
        }

        .radio-inline + .error {
            display: block;
        }

        .primary-color {
            color: #e9432d;
        }

        .form-group-msg {
            display: none;
        }

        .ui-dialog .ui-dialog-titlebar-close {
            border: none;
            background: url("/img/gb1.png") no-repeat;
            background-position: 3px;
        }

        .form-group-msg .form-control-static {
            line-height: 20px;
            margin: 0;
            text-align: left;
        }

        .dialog-tip{
            min-width: 334px;
            min-height: 210px;
        }
        .ui-dialog .ui-dialog-buttonpane button{
            margin-bottom: 15px;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $('#openClose').on('click', function () {
                $(this).parent().toggleClass('full-table');
                $(this).toggleClass('close')
            });
            $('#qrybtn').on('click', function (e) {
                getStus();
                return false;
            });

            $('.form-group-checkboxes>span').addClass('checkbox-inline')
        })


        function getStus() {
            var userType = $("#userType").html();
            var findGrade = $('#findGrade').html();
            var id = $('#findId').html();
            var teacherType = $('#teacherType').html();
            var userName = $("#username").val();
            var currState = $("#currState").val();
            var arrs = new Array();
            $("input[name='curJoinStr']:checkbox").each(function () {
                if ($(this).attr("checked")) {
                    arrs.push($(this).val());
                }
            });
            if (teacherType == '2') {
                $('#officeContent').attr("src", "${ctx}/sys/user/backUserListTree?ids=${ids}&userType=" + userType + "&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
            } else {
                if (findGrade == '1') {
                    $('#officeContent').attr("src", "${ctx}/sys/user/backUserListTree?ids=${ids}&userType=" + userType + "&teacherType=1&userName=" + userName + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                } else if (findGrade == '2') {
                    $('#officeContent').attr("src", "${ctx}/sys/user/backUserListTree?ids=${ids}&office.id=" + id + "&userType=" + userType + "&grade=2&teacherType=1&userName=" + userName + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                } else if (findGrade == '3') {
                    $('#officeContent').attr("src", "${ctx}/sys/user/backUserListTree?ids=${ids}&professionId=" + id + "&userType=" + userType + "&grade=3&teacherType=1&userName=" + userName + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                } else {
                    $('#officeContent').attr("src", "${ctx}/sys/user/backUserListTree?ids=${ids}&userType=" + userType + "&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                }
            }

            return false;
        }


        function searchEntTeacher() {
            $("#teacherType").html("2");
            getStus();
            return false;
        }
        function changeName() {
            if (${userType==''}) {
                $("#searchEntTeacher").css("display", "none");
            }
            if (${userType==1}) {
                $("#searchEntTeacher").css("display", "none");
            }
            if (${userType==2}) {
                $("#searchEntTeacher").css("display", "block");
                var treeGen = $("#ztree_1_span");
                treeGen.text("校内导师");
            }
        }
    </script>
</head>
<body onload="changeName()">
<div id="dialog-message" class="dialog-tip" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
<div id="opType" style="display: none;">${opType}</div>
<div id="userType" style="display: none;">${userType}</div>
<div id="teacherType" style="display: none;">${teacherType}</div>
<div id="findGrade" style="display: none;"></div>
<div id="findId" style="display: none;"></div>
<div id="ids" style="display: none;">${ids}</div>
<div class="text-right searchbar">
    <form:form id="form" modelAttribute="user" action="#" method="post" class="form-inline">
        <c:if test="${userType=='1'}">
            <div class="form-group form-group-sm">
                <label class="control-label">现状：</label>
                <form:select path="currState"
                             class="input-xlarge form-control">
                    <form:option value="" label="-请选择-"/>
                    <form:options items="${fns:getDictList('current_sate')}"
                                  itemLabel="label" itemValue="value"
                                  htmlEscape="false"/>
                </form:select>
            </div>
            <%--<div class="form-group form-group-sm form-group-checkboxes">--%>
                <%--<label class="control-label">当前在研：</label>--%>
                <%--<form:checkboxes path="curJoinStr" items="${fns:getPublishDictList()}"--%>
                                 <%--itemValue="value"--%>
                                 <%--itemLabel="label" htmlEscape="false"/>--%>
            <%--</div>--%>
        </c:if>
        <c:if test="${userType=='2'}">
            <div class="form-group form-group-sm form-group-checkboxes">
                <label class="control-label">当前指导：</label>
                <form:checkboxes path="curJoinStr" items="${fns:getPublishDictList()}"
                                 itemValue="value"
                                 itemLabel="label" htmlEscape="false"/>
            </div>
        </c:if>

        <div class="form-group form-group-sm">
            <c:if test="${userType=='1'}">
                <input class="form-control" type="text" id="username" placeholder="姓名/技术领域/学号">
            </c:if>
            <c:if test="${userType=='2'}">
                <input class="form-control" type="text" id="username" placeholder="姓名/技术领域/教工号">
            </c:if>
        </div>
        <%--<div class="form-group form-group-sm">--%>
            <%--<input class="form-control" type="text" id="username" placeholder="姓名/技术领域/">--%>
        <%--</div>--%>
        <div class="form-group form-group-sm">
            <button type="button" class="btn btn-primary-oe btn-sm" id="qrybtn">查询</button>
        </div>

    </form:form>
</div>
<div id="teamId" style="display: none">${teamId}</div>
<div id="content" class="container-details">
    <div id="left" class="accordion-group">
        <div class="accordion-heading">
            <a class="accordion-toggle"> 列表<i class="pull-right" onclick="refreshTree();"></i></a>
        </div>
        <div id="searchEntTeacher" class="qiyedaoshi">
            <span class="m-button" style="width: 18px;height: 18px"></span>
            <span class="m-button ico_open"></span>
            <a onclick="return searchEntTeacher();" style="color:#333">企业导师</a>
        </div>
        <div id="ztree" class="ztree"></div>
    </div>
    <div id="openClose" class="close-handler close">&nbsp;</div>
    <div id="right" class="table-user-content">
        <iframe id="officeContent" src="${ctx}/sys/user/backUserListTree?ids=${ids}&userType=${userType}"
                width="100%" height="100%" frameborder="0"></iframe>
    </div>
</div>

<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
    $(function () {
        refreshTree();


        var $formAddTeacher = $('#formAddTeacher');
        var $office = $('select[name="office"]');
        var $profes = $('select[name="profes"]');
        var $teacherTypes = $('input[name="type"]');
        var officeXhr = $.getJSON('${ctx}/sys/office/treeDataForUserSel');
        var $formGroupTypes = $('.form-group-type');
        var $no = $('input[name="no"]');
        var $mobile = $('input[name="mobile"]');
        var officeData;
        var officeOption = '';
        var $btnAddTeacher = $('#btnAddTeacher');
        var $btnCancel = $('.btn-cancel');
        var addTeacherDialog;
        var formAddTeacher;
        var $formGroupMsg = $('.form-group-msg');
        var $btnSubmit = $('.btn-submit');
        var timerID = null;


        $formAddTeacher.find('input').on('focus', function (e) {
            $formGroupMsg.hide();
        })

        $btnAddTeacher.on('click', function () {
            if (!addTeacherDialog) {
                addTeacherDialog = $('#addTeacherByStu').dialog({
                    width: '600',
                    height: 'auto',
                    minHeight: '400',
                    close: function () {
                        $formGroupMsg.hide();
                        formAddTeacher.resetForm();
                        $formAddTeacher[0].reset();
                        $('input[name="type"]:checked').trigger('change');
                        $btnSubmit.prop('disabled', false);
                    }
                });
                formAddTeacher = $formAddTeacher.validate({
                    rules: {
                        mobile: {
                            required: true
                        },
                        no: {
                            required: true,
                            isWordNum: true
                        }
                    },
                    submitHandler: function (form) {
                        if (form) {
                            var formData = $(form).serialize();
                            var xhr = $.post('/f/sys/frontTeacherExpansion/addTeacherByStu', formData);
                            $btnSubmit.prop('disabled', true);
                            xhr.success(function (data) {
                                $formGroupMsg.show().find('.form-control-static').text(data.msg);
                                if(data.ret == '1'){
                                    timerID && clearTimeout(timerID)
                                    timerID = setTimeout(function () {
                                        addTeacherDialog.dialog('close')
                                    }, 1000)
                                    $btnSubmit.prop('disabled', false);
                                }else {
                                    $btnSubmit.prop('disabled', false);
                                }

//                                $btnSubmit.prop('disabled', false);
                            })
                            xhr.error(function (data) {
                                $formGroupMsg.show().find('.form-control-static').text(data.msg)
                                $btnSubmit.prop('disabled', false);
                            })
                            return false;
                        }
                    },
                    errorPlacement: function (error, element) {
                        if (element.attr('type') === 'radio') {
                            error.appendTo(element.parent().parent())
                        } else {
                            error.insertAfter(element)
                        }
                    }
                });
            } else {
                addTeacherDialog.dialog('open')
            }
        });

        officeXhr.success(function (data) {
            officeData = data.filter(function (t) {
                return t.pId == '1'
            })
            officeData.forEach(function (t) {
                officeOption += '<option value="' + t.id + '">' + t.name + '</option>';
            })
            $office.append(officeOption);
        })

        $office.change(function (e) {
            var id = $(this).val()
            var xhr = $.post('/f/sys/office/findProfessionals', {parentId: id});
            var profesOption = '';
            $profes.find('option').not(':first').detach();
            xhr.success(function (data) {
                data.forEach(function (t) {
                    profesOption += '<option value="' + t.id + '">' + t.name + '</option>';
                })
                $profes.append(profesOption)
            })
        })

        $teacherTypes.on('change', function () {
            var val = $(this).val();
            var $no = $('input[name="no"]');
            var $mobile = $('input[name="mobile"]');
            if (val == '1') {
                //校园导师
                $formGroupTypes.find('.control-label i').show();
                $no.rules('add', {required: true});
                $mobile.rules('add', {required: true})
            }
            if (val == '2') {
                $no.rules('remove');
                $formGroupTypes.find('.control-label i').hide();
            }
        })

        $btnCancel.on('click', function () {
            addTeacherDialog.dialog('close')
        })


        // 手机号码验证
        jQuery.validator.addMethod("isMobileNumber", function (value, element) {
            var length = value.length;
            var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
            return this.optional(element) || (length == 11 && mobile.test(value));
        }, "请正确填写您的手机号码");

        jQuery.validator.addMethod('isWordNum', function (value, element) {
            return this.optional(element) || (/^[0-9a-zA-Z]*$/g).test(value)
        }, '请输入字母或者数字')
    });

    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPId: '0'
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                var userType = $("#userType").html();
                var id = treeNode.id == '0' ? '' : treeNode.id;
                var treeNodegrade = treeNode.grade;
                $('#findGrade').html(treeNodegrade);
                $('#findId').html(id);
                $('#teacherType').html("1");
                getStus();
            }
        }
    };

    function refreshTree() {
        $.getJSON("${ctx}/sys/office/treeDataForUserSel", function (data) {
            $.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
        });
    }
</script>
</body>
</html>