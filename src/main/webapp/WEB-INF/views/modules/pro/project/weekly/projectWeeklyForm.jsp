<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>项目周报</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script>
        var  ctxImg = "${ctxImg}";
        var  ctxImages = "${ctxImages}";
    </script>
    <%--<link rel="stylesheet" type="text/css" href="${ctxCss}/projectForm1.css"/>--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <script src="${ctxStatic}/fullcalendar/moment.min.js"></script>

</head>
<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/page-innovation">双创项目</a></li>
        <li><a href="${ctxFront}/project/projectDeclare/curProject">我的项目</a></li>
        <li class="active">新建周报</li>
    </ol>
    <h3 class="text-center" style="margin-bottom: 1em; margin-top: 0">${projectDeclare.name}</h3>
    <form:form id="inputForm" modelAttribute="vo" action="submit"
               method="post" class="form-horizontal" enctype="multipart/form-data">
        <div class="weekly-wrap">
            <div class="row">
                <div class="col-xs-6">
                    项目编号：${projectDeclare.number}
                </div>
                <%--<div class="col-xs-6 text-right">--%>
                    <%--创建时间：<fmt:formatDate value="${projectDeclare.createDate}"/>--%>
                <%--</div>--%>
            </div>
            <h4 class="titlebar titlebar-custom">项目周报</h4>
            <div class="section-user">
                <div class="row">
                    <div class="col-xs-5 col-xs-offset-1 control-group-user">
                        <span class="control-span-user text-right">汇报人：</span>
                        <div class="controls-user">
                            <p class="controls-user-static">${cuser.name}</p>
                        </div>
                    </div>
                    <div class="col-xs-5 control-group-user">
                        <span class="control-span-user text-right">职责：</span>
                        <div class="controls-user">
                            <p class="controls-user-static">${duty}</p>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-5 col-xs-offset-1 control-group-user">
                        <span class="control-span-user text-right">项目组成员：</span>
                        <div class="controls-user">
                            <p class="controls-user-static">${teamList}</p>
                        </div>
                    </div>
                    <div class="col-xs-5 control-group-user">
                        <span class="control-span-user text-right">项目导师：</span>
                        <div class="controls-user">
                            <p class="controls-user-static">${teacher}</p>
                        </div>
                    </div>
                </div>
            </div>


            <input type="hidden" id="projectWeekly.projectId"
                   name="projectWeekly.projectId" value="${projectWeekly.projectId}">
            <input type="hidden" id="projectWeekly.id" name="projectWeekly.id"
                   value="${projectWeekly.id}">
            <%--<input type="hidden" id="projectWeekly.lastId"--%>
                   <%--name="projectWeekly.lastId" value="${projectWeekly.lastId}">--%>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>本阶段任务完成情况</span>
                    <i class="line"></i>
                </div>
                <div class="edit-bar-right">
                    <%--<p class="time-section gray-color">时间：<span>--%>
                        <%--<fmt:formatDate value="${vo.lastpw.startDate}" pattern="yyyy-MM-dd"/> 至--%>
                        <%--<fmt:formatDate value="${vo.lastpw.endDate}" pattern="yyyy-MM-dd"/></span>--%>
                    <%--</p>--%>
                    <c:if test="${user.userType==1}">
                        <div class="edit-time-section">
                            <div class="edit-time-label control-label" style="width:120px;margin-top:-8px;"><i>*</i>任务完成时间：</div>
                            <div class="edit-time-form">
                                <div class="input-group input-group-sm">
                                    <input type="text" class="form-control m97-date required"
                                           name="projectWeekly.startDate"
                                           value='<fmt:formatDate
                                                value="${vo.projectWeekly.startDate}" pattern="yyyy-MM-dd" />'>
                                    <span class="input-group-addon">至</span>
                                    <input type="text" class="form-control m97-date required"
                                           name="projectWeekly.endDate"
                                           value='<fmt:formatDate value="${vo.projectWeekly.endDate}" pattern="yyyy-MM-dd" />'>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${user.userType==2}">
                        <p class="time-section gray-color">任务完成时间：<span><fmt:formatDate
                               value="${vo.projectWeekly.startDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate
                               value="${vo.projectWeekly.endDate}" pattern="yyyy-MM-dd"/></span></p>
                    </c:if>
                </div>
            </div>
            <%--<div class="form-group">--%>
                <%--<label class="control-label col-xs-2">上周任务计划：</label>--%>
                <%--<div class="col-xs-10">--%>
                    <%--<p class="form-control-static">--%>
                        <%--<c:if test="${empty vo.lastpw.id}"><span class="gray-color">上周无任务计划</span></c:if>--%>
                            <%--${vo.lastpw.plan}--%>
                    <%--</p>--%>
                <%--</div>--%>
            <%--</div>--%>
            <div class="form-group">
                <label class="control-label col-xs-2">完成情况：</label>
                <div class="col-xs-10">
                    <c:if test="${user.userType==1}">
                        <c:if test="${not empty vo.projectWeekly.id}">
                            <form:textarea path="projectWeekly.achieved" rows="4" cssClass="form-control"></form:textarea>
                        </c:if>
                        <c:if test="${empty vo.projectWeekly.id}">
                            <form:textarea path="projectWeekly.achieved" rows="4" cssClass="form-control"></form:textarea>
                        </c:if>
                    </c:if>
                    <c:if test="${user.userType==2}">
                        <p class="form-control-static">${vo.projectWeekly.achieved}</p>
                    </c:if>
                </div>
            </div>
            <div class="form-group" style="margin-bottom: 30px;">
                <label class="control-label col-xs-2">存在问题：</label>
                <div class="col-xs-10">
                    <c:if test="${user.userType==1}">
                        <c:if test="${not empty vo.projectWeekly.id}">
                            <form:textarea path="projectWeekly.problem" rows="4" cssClass="form-control"></form:textarea>
                        </c:if>
                        <c:if test="${empty vo.projectWeekly.id}">
                            <form:textarea path="projectWeekly.problem" rows="4" cssClass="form-control"></form:textarea>
                        </c:if>
                    </c:if>
                    <c:if test="${user.userType==2}">
                        <p class="form-control-static">${vo.projectWeekly.problem}</p>
                    </c:if>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>下阶段任务计划</span>
                    <i class="line"></i>
                </div>
                <div class="edit-bar-right">
                    <c:if test="${user.userType==1}">


                        <div class="edit-time-section">
                            <div class="edit-time-label control-label" style="width:120px;margin-top:-8px;"><i>*</i>计划任务时间：</div>
                            <div class="edit-time-form">
                                <div class="input-group input-group-sm">
                                    <input type="text" class="form-control m97-date required"
                                           name="projectWeekly.startPlanDate"
                                           value='<fmt:formatDate
												value="${vo.projectWeekly.startPlanDate}" pattern="yyyy-MM-dd" />'>
                                    <span class="input-group-addon">至</span>
                                    <input type="text" class="form-control m97-date required"
                                           name="projectWeekly.endPlanDate"
                                           value='<fmt:formatDate value="${vo.projectWeekly.endPlanDate}" pattern="yyyy-MM-dd" />'>
                                </div>
                            </div>
                        </div>
                    </c:if>
                    <c:if test="${user.userType==2}">
                        <p class="time-section gray-color">计划任务时间：<span><fmt:formatDate
                                value="${vo.projectWeekly.startPlanDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate
                                value="${vo.projectWeekly.endPlanDate}" pattern="yyyy-MM-dd"/></span></p>
                    </c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>计划内容：</label>
                <div class="col-xs-10">
                    <c:if test="${user.userType==1}">
                        <form:textarea path="projectWeekly.plan" rows="4"
                                       cssClass="form-control required"></form:textarea>
                    </c:if>
                    <c:if test="${user.userType==2}">
                        <p class="form-control-static">${vo.projectWeekly.plan}</p>
                    </c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2">附件：</label>
                <div class="col-xs-10">
                    <c:if test="${user.userType==1}">
                        <sys:frontFileUpload className="accessories-h34" fileitems="${vo.fileInfo}"
                                             filepath="weekly"></sys:frontFileUpload>
                    </c:if>
                    <c:if test="${user.userType==2}">
                        <sys:frontFileUpload className="accessories-h34" fileitems="${vo.fileInfo}"
                                             filepath="weekly" readonly="true"></sys:frontFileUpload>
                        <c:if test="${empty vo.fileInfo}"><p class="form-control-static gray-color">无附件</p></c:if>
                    </c:if>
                </div>
            </div>
            <c:if test="${user.userType==1 && not empty vo.projectWeekly.id}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>导师意见及建议</span>
                        <i class="line"></i>
                    </div>
                    <div class="edit-bar-right">
                        <p class="time-section gray-color">时间：<span><fmt:formatDate
                                value="${vo.projectWeekly.suggestDate}" pattern="yyyy-MM-dd"/></span></p>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-xs-2">意见及建议：</label>
                    <div class="col-xs-10">
                        <p class="form-control-static">${vo.projectWeekly.suggest}</p>
                    </div>
                </div>
            </c:if>
            <c:if test="${user.userType==2}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>导师意见及建议</span>
                        <i class="line"></i>
                    </div>
                    <div class="edit-bar-right">
                        <p class="time-section gray-color">时间：<span><fmt:formatDate
                                value="${currentDate}" pattern="yyyy-MM-dd"/></span></p>
                    </div>
                </div>

                <div class="form-group">
                    <label class="control-label col-xs-2">意见及建议：</label>
                    <div class="col-xs-10">
                        <form:textarea path="projectWeekly.suggest" rows="3"
                                       cssClass="form-control" maxlength="255"></form:textarea>
                    </div>
                </div>
            </c:if>
        </div>
        <div class="text-center" style="margin-bottom: 30px;">
            <c:if test="${user.userType==2}">
                <button type="button" data-control="uploader" class="btn btn-primary btn-save">保存</button>
            </c:if>
            <c:if test="${user.userType==1}">
                <%--<a href="javascript:;" class="btn btn-primary" onclick="weekly.subBtn();">提交</a>--%>
                <button type="button" data-control="uploader" class="btn btn-primary btn-submit">提交</button>
            </c:if>
            <button type="button" data-control="uploader" class="btn btn-default" onclick="history.go(-1);">返回</button>
        </div>
    </form:form>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">

    +function ($) {
        var $inputForm = $('#inputForm');
        var $startDate = $('input[name="projectWeekly.startDate"]');
        var $endDate = $('input[name="projectWeekly.endDate"]');
        var $startPlanDate = $('input[name="projectWeekly.startPlanDate"]');
        var $endPlanDate = $('input[name="projectWeekly.endPlanDate"]');
        var $btnSubmit = $inputForm.find('button.btn-submit');
        var $btnSave = $inputForm.find('button.btn-save');
        var minDate = '${projectDeclare.applyTime}';
        var maxDate;
        var date = minDate ? new Date(minDate) : new Date();
        var curDay = date.getDay();

        /* date.setDate(date.getDate() - (curDay == 0 ? 7 : curDay) + 1); */

        minDate = moment(date).format('YYYY-MM-DD');
        /* maxDate = moment(date.setDate(date.getDate() + 6)).format('YYYY-MM-DD'); */

        var inputFormValidate = $inputForm.validate({
            errorPlacement: function (error, element) {
                var name = element.attr('name');
                if (name === 'projectWeekly.startDate' || name === 'projectWeekly.endDate' || name === 'projectWeekly.startPlanDate' || name === 'projectWeekly.endPlanDate') {
                    error.appendTo(element.parent().parent());
                } else {
                    error.appendTo(element.parent());
                }
            }
        });

        $btnSubmit.on('click', function () {
            $inputForm.attr('action', 'submit');
            submitAndSave('submit')
        })

        $btnSave.on('click', function () {
            $inputForm.attr('action', 'save');
            submitAndSave('save')
        })

        function submitAndSave(type) {
            if (type === 'submit') {
                if (!inputFormValidate.form()) {
                    return false;
                }
            }
            $btnSave.prop('disabled', true).text('保存中...');
            $btnSubmit.prop('disabled', true).text('提交中...');
            $inputForm.ajaxSubmit(function (data) {
                var isLoginOut = checkIsToLogin(data);
                var state = data.ret;
                if (isLoginOut) {
                    dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
                        dialogClass:  'dialog-cyjd-container none-close',
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-primary',
                            click: function () {
                                location.href = '${ctxFront}/toLogin'
                            }
                        }]
                    });
                    return false;
                }
                $btnSave.prop('disabled', false).text('保存');
                $btnSubmit.prop('disabled', false).text('提交');
                dialogCyjd.createDialog(state, data.msg, {
                    dialogClass:  'dialog-cyjd-container none-close',
                    buttons: [{
                        text: '确定',
                        'class': 'btn btn-primary',
                        click: function () {
                            if (state == 0) {
                                $(this).dialog('close');
                                return false;
                            }
                            location.href = '${ctxFront}/project/projectDeclare/curProject'
                        }
                    }]
                });
            });
        }

        $startDate.on('click', function () {
            var element = $(this)[0];
            WdatePicker({
                el: element,
//                minDate: minDate,
                maxDate: $endDate.val() || moment(new Date()).format('YYYY-MM-DD')
            })
        });

        $endDate.on('click', function () {
            var element = $(this)[0];
            WdatePicker({
                el: element,
//                minDate:$startDate.val() || minDate,
                maxDate: moment(new Date()).format('YYYY-MM-DD')
            })
        })

        $startPlanDate.on('click', function () {
            var element = $(this)[0];
            WdatePicker({
                el: element,
//                minDate: minDate,
                maxDate: $endPlanDate.val() || moment(new Date()).format('YYYY-MM-DD')
            })
        });

        $endPlanDate.on('click', function () {
            var element = $(this)[0];
            WdatePicker({
                el: element,
//                minDate:$startPlanDate.val() || minDate,
//                maxDate: moment(new Date()).format('YYYY-MM-DD')
            })
        })

        function checkIsToLogin(data) {
            try {
                if (data.indexOf("id=\"imFrontLoginPage\"") != -1) {
                    return true;
                }
            } catch (e) {
                return false;
            }
        }


    }(jQuery)

</script>
</body>
</html>