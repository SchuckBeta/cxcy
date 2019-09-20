<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>审核</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>

<body>

<div class="container-fluid container-fluid-audit" style="margin-bottom: 60px;">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>${taskName}</span> <i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/promodelGateAudit"
               method="post" class="form-horizontal form-container">
        <form:hidden path="id"/>
        <form:hidden path="act.taskId"/>
        <form:hidden path="act.taskName"/>
        <form:hidden path="act.taskDefKey"/>
        <form:hidden path="act.procInsId"/>
        <form:hidden path="act.procDefId"/>

        <input type="hidden" name="actionPath" value="${actionPath}"/>
        <input type="hidden" name="gnodeId" value="${gnodeId}"/>
        <input type="hidden" name="secondName" id="secondName" value="${taskName}"/>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>申报人基本信息</span> <i class="line"></i> <a
                    data-toggle="collapse" href="#projectPeopleDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="projectPeopleDetail" class="panel-body collapse in">
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <div class="item-label">
                        作品编号：
                    </div>
                    <div class="items-box">
                        <p class="text-ellipsis">${proModel.competitionNumber}</p>
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">负责人：</span>
                    <div class="items-box">
                            ${proModel.deuser.name}
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">申报日期：</span>
                    <div class="items-box">
                        <fmt:formatDate value="${proModel.createDate}"></fmt:formatDate>
                    </div>
                </div>


            </div>
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">学号：</span>
                    <div class="items-box">
                        <p class="text-ellipsis"> ${proModel.deuser.no}</p>
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">性别：</span>
                    <div class="items-box">
                        <c:choose>
                            <c:when test="${proModel.deuser.sex == '1'}">男</c:when>
                            <c:when test="${proModel.deuser.sex == '0'}">女</c:when>
                        </c:choose>
                    </div>
                </div>
                    <%--<div class="span4">--%>
                    <%--<span class="item-label">学历：</span>--%>
                    <%--<div class="items-box">--%>
                    <%--${proModel.deuser.education}--%>
                    <%--</div>--%>
                    <%--</div>--%>

            </div>
            <div class="row-fluid row-info-fluid">
                    <%--<div class="span4">--%>
                    <%--<span class="item-label">学位：</span>--%>
                    <%--<div class="items-box">--%>
                    <%--${proModel.deuser.degree}--%>
                    <%--</div>--%>
                    <%--</div>--%>
                <div class="span4">
                    <span class="item-label">参赛学院：</span>
                    <div class="items-box">
                            ${proModel.deuser.office.name}
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">专业：</span>
                    <div class="items-box">
                            ${proModel.deuser.subject.name}
                    </div>
                </div>

            </div>
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">联系电话：</span>
                    <div class="items-box">
                            ${proModel.deuser.mobile}
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">Email：</span>
                    <div class="items-box">
                            ${proModel.deuser.email}
                    </div>
                </div>
            </div>
        </div>


        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>参赛作品信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                            href="#teamDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="teamDetail" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">作品名称：</span>
                        <div class="items-box">
                                ${proModel.pName}
                        </div>
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">所属领域：</span>
                        <div class="items-box">
                                ${proModelGzsmxx.regionName}
                        </div>
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">领域分组：</span>
                        <div class="items-box">
                                ${proModelGzsmxx.regionGroupName}
                        </div>
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">作品简介：</span>
                        <div class="items-box">
                                ${proModel.introduction}
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>团队信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                          href="#teamInfo"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="teamInfo" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">团队名称：</span>${team.name}
                    </div>
                </div>


                <div class="table-caption">学生团队</div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>手机号</th>
                        <th>所在学院</th>
                    </tr>
                    </thead>
                    <tbody id="teamTableStudent">
                    <c:forEach items="${teamStu}" var="student" varStatus="status">
                        <tr>
                            <td>${status.index +1}</td>
                            <td>${student.name}</td>
                            <td>${student.no}</td>
                            <td>${student.mobile}</td>
                            <td>${student.orgName}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <div class="table-caption">指导教师</div>
                <table
                        class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th>E-mail</th>
                    </tr>
                    </thead>
                    <tbody id="teamTableTeacher">
                    <c:forEach items="${teamTea}" var="teacher" varStatus="status">
                        <tr>
                            <td>${status.index +1}</td>
                            <td>${teacher.name}</td>
                            <td>${teacher.no}</td>
                            <td>${teacher.teacherType}</td>
                            <td>${fns:getDictLabel(item.technical_title,'postTitle_type','')}</td>
                            <td>${fns:getDictLabel(item.education,'enducation_level','')}</td>
                            <td>${teacher.mobile}</td>
                            <td>${teacher.email}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>


        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>附件</span> <i class="line"></i> <a data-toggle="collapse"
                                                        href="#teamDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>

        <div id="attDetail" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">附件：</span>
                        <div class="items-box">
                            <sys:frontFileUpload className="accessories-h30" fileitems="${sysAttachments}"
                                                 readonly="true"></sys:frontFileUpload>
                        </div>
                    </div>
                </div>
            </div>
        </div>


        <!-- 审核记录 -->
        <c:if test="${not empty actYwAuditInfos}">

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核记录</span> <i class="line"></i><a data-toggle="collapse" href="#actYwAuditInfos"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>

            <div id="actYwAuditInfos" class="panel-body collapse in">
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                    <thead>
                    <tr>
                        <th>审核动作</th>
                        <th>审核时间</th>
                        <th>审核人</th>
                        <th>审核结果</th>
                        <th style="width:45%">建议及意见</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${actYwAuditInfos}" var="item">
                        <c:choose>
                            <c:when test="${not empty item.id}">
                                <tr>
                                    <td>${item.auditName}</td>
                                    <td><fmt:formatDate value="${item.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>${item.user.name}</td>
                                    <td>${item.result}</td>
                                    <td>${item.suggest}</td>
                                </tr>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="5" style="color: red; text-align: right;font-weight: bold">${item.auditName}：${item.result}</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    </tbody>
                </table>
            </div>

        </c:if>


        <!-- 专家评分 -->
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>评分</span> <i class="line"></i> <a
                    data-toggle="collapse" href="#scoreDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="scoreDetail" class="panel-body collapse in">

            <div class="control-group">
                <label class="control-label"><i>*</i>评分：</label>
                <div class="controls">
                    <input name="gScore" class="input-medium maxFs required" type="text" minlength="0" maxlength="100"
                           placeholder="输入0-100之间的整数" style="vertical-align: top">
                </div>
            </div>
            <div class="control-group" style="margin-top:30px;">
                <label class="control-label"><i style="display:none;">*</i>审核建议或意见：</label>
                <div class="controls">
                        <textarea name="source" maxlength="100" rows="5" class="form-control input-xxlarge"
                                  placeholder="请给予您的意见和建议（最大长度100）"></textarea>
                </div>
            </div>
        </div>
        <input type="hidden" name="grade"/>
        <div class="form-actions-cyjd text-center" style="border-top: none">
            <button type="submit" class="btn btn-primary" style="margin-right: 10px;">提交</button>
            <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
        </div>
    </form:form>


</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

<script type="text/javascript">
    $(function () {
        var $inputForm = $('#inputForm');
        console.log('${proModel.actYwId}')
        $inputForm.validate({
            rules: {
                source: {
                    maxlength: 300
                }
            },
            messages: {
                source: {
                    maxlength: "最多输入{0}个字"
                }
            },
            submitHandler: function (form) {
                form.submit();
                window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
            }
        });

        jQuery.validator.addMethod("maxFs", function (value, element) {
            return this.optional(element) || (value >= 0 && value <= 100 && /^([0-9]{1,2}|100)$/.test(value.toString()));
        }, "填写0-100之间的整数");

    })

</script>

</body>

</html>
