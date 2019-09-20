<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>审核</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
</head>

<body>

<div class="container-fluid container-fluid-audit" style="margin-bottom: 60px;">
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
                <span>基本信息</span> <i class="line"></i>
                <a data-toggle="collapse" href="#projectPeopleDetail">
                    <i class="icon-collaspe icon-double-angle-up"></i>
                </a>
            </div>
        </div>
        <div id="projectPeopleDetail" class="panel-body collapse in">
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <div class="item-label">
                        项目编号：
                    </div>
                    <div class="items-box">
                        <p class="text-ellipsis">${proModel.competitionNumber}</p>
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">项目负责人：</span>
                    <div class="items-box">
                            ${proModel.deuser.name}
                    </div>
                </div>
                <div class="span4">
                    <span class="item-label">填报日期：</span>
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

            </div>
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">所属学院：</span>
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
                <span>团队信息</span> <i class="line"></i>
                <a data-toggle="collapse" href="#teamInfo">
                    <i class="icon-collaspe icon-double-angle-up"></i>
                </a>
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
                            <td>${student.org_name}</td>
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
                <span>参赛信息</span> <i class="line"></i>
                <a data-toggle="collapse" href="#teamDetail">
                    <i class="icon-collaspe icon-double-angle-up"></i>
                </a>
            </div>
        </div>
        <div id="teamDetail" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">参赛类别：</span>
                        <div class="items-box">
                                ${fns:getDictLabel(proModel.proCategory, "competition_net_type", "")}
                        </div>
                    </div>
                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">参赛组别：</span>
                        <div class="items-box">
                                ${fns:getDictLabel(proModel.level, "gcontest_level", "")}
                        </div>
                    </div>
                </div>
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
                        <span class="item-label">项目简介：</span>
                        <div class="items-box">
                                ${proModel.introduction}
                        </div>
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">附件：</span>
                        <div class="items-box">
                                <%--<sys:frontFileUpload fileitems="${sysAttachments}" filepath="project"></sys:frontFileUpload>--%>
                            <sys:frontFileUpload className="accessories-h30" fileitems="${sysAttachments}"
                                                 readonly="true"></sys:frontFileUpload>
                        </div>
                    </div>
                </div>

            </div>
        </div>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>
                    ${proModel.act.taskName}
                </span> <i class="line"></i>
                <a data-toggle="collapse" href="#examDetail">
                    <i class="icon-collaspe icon-double-angle-up"></i>
                </a>
            </div>
        </div>
        <div id="examDetail" class="panel-body collapse in">
            <div class="control-group">
                <label class="control-label">获奖情况：</label>
                <div class="controls">
                    <input type="hidden" name="awardType" value="0000000154">
                    <form:select path="result"  class="form-control has-disabled">
                        <form:option value="" label="请选择"/>
                        <form:options items="${fns:getDictList('0000000154')}" itemLabel="label"
                                        itemValue="value" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">推荐：</label>
                <div class="controls">
                    <form:select path="grade" id="sec-idea" class="form-control input-large accpet">
                        <form:option value="" label="-请选择-"/>
                        <c:choose>
                            <c:when test="${not empty actYwStatusList}">
                                <c:forEach items="${actYwStatusList}" var="item">
                                    <form:option value="${item.status}">${item.state}</form:option>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <form:option value="1">通过</form:option>
                                <form:option value="0">不通过</form:option>
                            </c:otherwise>
                        </c:choose>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="form-actions-cyjd text-center" style="border-top: none">
            <button type="submit" class="btn btn-primary" id="btnSumbit" style="margin-right: 10px;">确定</button>
            <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
        </div>
    </form:form>


</div>

<div id="dialogCyjd" class="dialog-cyjd"></div>
<script>
    $(function () {
        var $inputForm = $('#inputForm');
        var formValidate = $inputForm.validate({
            submitHandler: function (form) {
                $inputForm.find('button[type="submit"]').prop('disabled', true);
                form.submit();
                window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
            }
        });
    })

</script>


</body>

</html>
