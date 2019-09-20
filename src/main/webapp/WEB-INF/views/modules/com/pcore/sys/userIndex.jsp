<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <!--前台创业基地-->
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/jquery-ui.css?v=1"/>
    <%--<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>--%>
    <!--头部导航公用样式-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/commonCyjd.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=q1111">
    <!--focus样式表-->


    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>

    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <!--文本溢出-->
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <script src="${ctxJs}/frontCyjd/frontCommon.js?v=21" type="text/javascript"></script>

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

        .dialog-tip {
            min-width: 334px;
            min-height: 210px;
        }

        .ui-dialog .ui-dialog-buttonpane button {
            margin-bottom: 15px;
        }

        .ui-dialog .ui-dialog-buttonpane .ui-dialog-buttonset {
            float: none;
        }

        .dialog-message-wqt .ui-dialog-buttonset {
            margin: 10px auto;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            changeName();
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

        function getIds(invType) {
            var zTree = $.fn.zTree.getZTreeObj("ztree");
            var nodes = zTree.getChangeCheckedNodes(true);
            var officeIds = '';
            /* if(nodes.length==0){
             alert("请选择部门！！");
             return false;
             } */
            for (var i = 0; i < nodes.length; i++) {
                var halfCheck = nodes[i].getCheckStatus();
                if (!halfCheck.half) {
                    var idt = nodes[i].id;
                    // idt=idt.replace(" ","");
                    if (idt != null && idt != undefined) {
                        idt = $.trim(idt);
                    }
                    if (idt == null || idt == "" || idt.length == 0) {
                    } else {
                        // alert(nodes[i].id.length);
                        officeIds += nodes[i].id + ',';
                    }

                }
            }

            officeIds = officeIds.substring(0, officeIds.lastIndexOf(","));
            //alert(officeIds);

            var idarray = document.getElementById("officeContent").contentWindow
                    .getValue();
            var userIds = '';
            if (idarray != null && idarray != undefined && idarray.length > 0) {
                $.each(idarray, function (index, value) {
                    //alert(index+"..."+value);
                    if (value != null && value != undefined && value != "") {
                        userIds += value + ',';
                    }
                });
                userIds = userIds.substring(0, userIds.lastIndexOf(","));
                //alert(userIds);
            }

            var teamId = $("#teamId").html();
            var opType = $("#opType").html();
            var userType = $("#userType").html();
            var typename = "";
            if (userType == 1) {
                typename = "学生";
            }
            if (userType == 2) {
                typename = "导师";
            }
            var url = "";
            if (opType == 1) {
                url = "${ctxFront}/team/teamUserRelation/batInTeamOffice";
            } else if (opType == 2) {
                if (invType == 2) {
                    url = "${ctxFront}/team/teamUserRelation/pullIn";
                } else if (invType == 3) {
                    url = "${ctxFront}/team/teamUserRelation/toInvite";
                }
            }

            if (userIds == null || userIds == "" || userIds == undefined) {
                if (userType == 1) {
                    var stuNum = idarray.length;
                    if (stuNum > 2) {
                        dialogCyjd.createDialog(0, "最多邀请6个学生！");
                    }
                    dialogCyjd.createDialog(0, "请至少选择一个学生！");
                } else if (userType == 2) {
                    var teaNum = idarray.length;
                    if (teaNum > 2) {
                        dialogCyjd.createDialog(0, "最多邀请2个导师！");
                    }
                    dialogCyjd.createDialog(0, "请至少选择一个导师！");
                } else {

                    dialogCyjd.createDialog(0, "请至少选择一个导师或一个学生！");
                }
                return;
            }

            if (teamId == null || teamId == "") {
                dialogCyjd.createDialog(0, "请求参数异常");
                return;
            }

            $.ajax({
                type: "post",
                url: url,
                data: {
                    'offices': officeIds,
                    'userIds': userIds,
                    'teamId': teamId,
                    'userType': userType
                },
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        if (opType == 1) {
                            dialogCyjd.createDialog(1, "发布成功！");
                        } else if (opType == 2 && invType == 2) {
                            dialogCyjd.createDialog(1, "成功添加" + data.res + "个" + typename, {
                                buttons: [{
                                    text: '确定',
                                    'class': 'btn btn-primary',
                                    click: function () {
//                                        location.href = location.href;
                                        window.parent.location.reload();
                                    }
                                }]
                            });
                        } else if (opType == 2 && invType == 3) {
                            dialogCyjd.createDialog(1, "成功邀请" + data.res + "个" + typename, {
                                buttons: [{
                                    text: '确定',
                                    'class': 'btn btn-primary',
                                    click: function () {
                                        $(this).dialog('close')
//                                        location.href = location.href;
                                        window.parent.location.reload();
                                    }
                                }]
                            });
                        }


                    } else {
                        dialogCyjd.createDialog(0, !data.msg ? "操作失败" : data.msg);
                    }
                },
                error: function () {
                    dialogCyjd.createDialog(0, "系统异常!");
                }
            });

        }

        function getStus() {
            var userType = $("#userType").html();
            var userName = $("#username").val();
            var currState = $("#currState").val();
            var arrs = new Array();
            $("input[name='curJoinStr']:checkbox").each(function () {
                if ($(this).attr("checked")) {
                    arrs.push($(this).val());
                }
            });

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
                $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?userType=" + userType + "&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
            } else {
                if (findGrade == '1') {
                    $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?userType=" + userType + "&teacherType=1&userName=" + userName + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                } else if (findGrade == '2') {
                    $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?office.id=" + id + "&userType=" + userType + "&grade=2&teacherType=1&userName=" + userName + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                } else if (findGrade == '3') {
                    $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?professionId=" + id + "&userType=" + userType + "&grade=3&teacherType=1&userName=" + userName + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                } else {
                    $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?userType=" + userType + "&userName=" + userName + "&teacherType=" + teacherType + "&currState=" + currState + "&curJoinStr=" + arrs.join(","));
                }
            }

            return false;
        }


        function searchEntTeacher() {
            $("#teacherType").html("2");
            $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?userType=2" + "&teacherType=2");
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
            }
        }
    </script>
</head>
<body>
<div id="dialog-message" class="dialog-tip" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<div id="opType" style="display: none;">${opType}</div>
<div id="userType" style="display: none;">${userType}</div>
<div id="teacherType" style="display: none;">${teacherType}</div>
<div id="findGrade" style="display: none;"></div>
<div id="findId" style="display: none;"></div>


<div class="searchbar">
    <form:form id="form" modelAttribute="user" action="#" method="post" class="form-inline clearfix">
        <div class="pull-left clearfix" style="width: 950px;">
            <c:if test="${userType=='1'}">
                <div class="form-group form-group-sm">
                    <label class="control-label">现状：</label>
                    <form:select path="currState"
                                 class="input-xlarge required form-control">
                        <form:option value="" label="-请选择-"/>
                        <form:options items="${fns:getDictList('current_sate')}"
                                      itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
                <div class="form-group form-group-sm form-group-checkboxes" style="padding-top: 4px;">
                    <label class="control-label">当前在研：</label>
                    <form:checkboxes path="curJoinStr" items="${fns:getPublishDictList()}"
                                     itemValue="value"
                                     itemLabel="label" htmlEscape="false"/>
                </div>
            </c:if>
            <c:if test="${userType=='2'}">
                <div class="form-group form-group-sm form-group-checkboxes" style="padding-top: 4px;">
                    <label class="control-label">当前指导：</label>
                    <form:checkboxes path="curJoinStr" items="${fns:getPublishDictList()}"
                                     itemValue="value"
                                     itemLabel="label" htmlEscape="false"/>
                </div>
            </c:if>
        </div>
        <div class="pull-right text-right" style="width: 100%;margin-top:10px;">
            <div class="form-group form-group-sm">
                <input class="form-control" type="text" id="username" placeholder="姓名/技术领域">
            </div>
            <div class="form-group form-group-sm">
                <button type="button" class="btn btn-primary-oe btn-sm" id="qrybtn">查询</button>
                <c:if test="${opType==1}">
                    <button type="button" class="btn btn-primary-oe btn-sm" onclick="getIds(1)">确定发布</button>
                </c:if>
                <c:if test="${opType==2}">
                    <button type="button" class="btn btn-primary-oe btn-sm" onclick="getIds(3)">发送邀请</button>
                </c:if>
                    <%--<c:if test="${userType=='2'}">--%>
                    <%--<button type="button" class="btn btn-primary-oe btn-sm" id="btnAddTeacher">添加导师</button>--%>
                    <%--</c:if>--%>
            </div>
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
        <iframe id="officeContent" src="${ctxFront}/sys/user/userListTree?userType=${userType}&allTeacher=1"
                width="100%" height="100%" frameborder="0"></iframe>
    </div>
</div>

<div id="addTeacherByStu" title="添加导师" style="display: none">
    <div class="container container-add-teacher">
        <form id="formAddTeacher" class="form-horizontal form-add-teacher">
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>姓名：</label>
                <div class="col-xs-6">
                    <input type="text" name="name" class="required form-control input-sm">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>导师来源：</label>
                <div class="col-xs-6">
                    <label class="radio-inline">
                        <input type="radio" name="type" checked class="required" value="1">校内导师
                    </label>
                    <label class="radio-inline">
                        <input type="radio" name="type" class="required" value="2">企业导师
                    </label>
                </div>
            </div>
            <div class="form-group form-group-type">
                <label class="control-label col-xs-2"><i>*</i>工号：</label>
                <div class="col-xs-6">
                    <input type="text" name="no" minlength="3" maxlength="16" class="form-control input-sm">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>手机：</label>
                <div class="col-xs-6">
                    <input type="text" name="mobile" class="form-control isMobileNumber input-sm">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2">学院：</label>
                <div class="col-xs-6">
                    <select name="office" class="form-control input-sm">
                        <option value="">-请选择-</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2">专业：</label>
                <div class="col-xs-6">
                    <select name="profes" class="form-control input-sm">
                        <option value="">-请选择-</option>
                    </select>
                </div>
            </div>
            <div class="form-group form-group-msg">
                <label class="control-label col-xs-2"></label>
                <div class="col-xs-6">
                    <p class="form-control-static primary-color"></p>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"></label>
                <div class="col-xs-6">
                    <button type="submit" class="btn btn-sm btn-primary-oe btn-submit">提交</button>
                    <button type="button" class="btn btn-sm btn-default-oe btn-cancel">取消</button>
                </div>
            </div>
        </form>
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
        var officeXhr = $.getJSON('${ctxFront}/sys/office/treeData');
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
                                if (data.ret == '1') {
                                    timerID && clearTimeout(timerID)
                                    timerID = setTimeout(function () {
                                        addTeacherDialog.dialog('close')
                                    }, 1000)
                                    $btnSubmit.prop('disabled', false);
                                } else {
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
            },
            onCheck: function (event, treeId, treeNode) {
                var curTree = $.fn.zTree.getZTreeObj(treeId);
                var nodes = curTree.getCheckedNodes(true);
                var id = treeNode.id == '0' ? '' : treeNode.id;
                $('#officeContent').attr("src", "${ctxFront}/sys/user/userListTree?office.id=" + id + "&office.name=" + treeNode.name);
            }
        }/*,
         check : {
         enable : true
         }*/
    };

    function refreshTree() {
        $.getJSON("${ctxFront}/sys/office/treeData", function (data) {
            $.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
            if (${userType==2}) {
                var treeGen = $("#ztree_1_span");
                treeGen.text("校内导师");
            }
        });
    }

    window.onload = function () {
        $('.container-details').css('height', function () {
            return 545 - $('.searchbar').height() - 40
        })
    }
</script>
</body>
</html>