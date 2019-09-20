<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>审核</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>

<body>

<style>
    .auditorWidth {
        width: 15%;
    }

    .projectWidth {
        width: 10%;
    }

    .suggestionWidth {
        width: 60%;
    }

    .timeWidth {
        width: 15%;
    }

</style>

<div class="container-fluid container-fluid-audit" style="margin-bottom: 60px;">

    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>项目信息</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/promodelAudit"
               method="post" class="form-horizontal form-container">

        <%--<div class="edit-bar edit-bar-sm clearfix">--%>
        <%--<div class="span4">--%>
        <%--<span class="item-label">填报日期：</span><fmt:formatDate value="${proModel.createDate}"></fmt:formatDate>--%>
        <%--</div>--%>
        <%--<div class="span4">--%>
        <%--<span class="item-label">项目编号：</span>${proModel.competitionNumber}--%>
        <%--</div>--%>
        <%--</div>--%>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>基本信息</span> <i class="line"></i> <a
                    data-toggle="collapse" href="#projectPeopleDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="projectPeopleDetail" class="panel-body collapse in">
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">项目编号：</span>${proModel.competitionNumber}
                </div>
                <div class="span4">
                    <span class="item-label">项目负责人：</span>${proModel.deuser.name}
                </div>
                <div class="span4">
                    <span class="item-label">填报日期：</span><fmt:formatDate
                        value="${proModel.createDate}"></fmt:formatDate>
                </div>

            </div>
            <div class="row-fluid row-info-fluid">
                <div class="span4">
                    <span class="item-label">学号：</span>${proModel.deuser.no}
                </div>
                <div class="span4">
                    <span class="item-label">性别：</span>
                    <c:choose>
                        <c:when test="${proModel.deuser.sex == '1'}">男</c:when>
                        <c:when test="${proModel.deuser.sex == '0'}">女</c:when>
                    </c:choose>
                </div>
                <div class="span4">
                    <span class="item-label">所属学院：</span>${proModel.deuser.office.name}
                </div>

            </div>
            <div class="row-fluid row-info-fluid">


                <div class="span4">
                    <span class="item-label">专业：</span>${proModel.deuser.subject.name}
                </div>
                <div class="span4">
                    <span class="item-label">联系电话：</span>${proModel.deuser.mobile}
                </div>
                <div class="span4">
                    <span class="item-label">Email：</span>${proModel.deuser.email}
                </div>
            </div>
        </div>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>项目基本信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                          href="#teamDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="teamDetail" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span6">
                        <span class="item-label">项目类别：</span>${fns:getDictLabel(proModel.proCategory, "project_type", "")}
                    </div>
                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">项目名称：</span>
                        <div style="margin:-20px 0 0 140px;word-break: break-all">
                                ${proModel.pName}
                        </div>
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span11">
                        <span class="item-label">项目简介：</span>
                        <div style="margin:-20px 0 0 140px;word-break: break-all">
                                ${proModel.introduction}
                        </div>
                    </div>
                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span6" style="margin-top:5px;">
                        <span class="item-label">项目logo：</span>
                            <img style="width:40px;height:40px;border-radius:50%;margin-left:-5px;" class="look-tu"
                                 src="${empty proModel.logo.url ? '/images/upload-default-image1.png':fns:ftpImgUrl(proModel.logo.url)}">
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span10" style="margin-top:10px;">
                        <span class="item-label">附件：</span>
                        <div class="controls" style="margin:-25px 0 0 135px;">
                                <%--<sys:frontFileUpload fileitems="${applyFiles}" filepath="project"></sys:frontFileUpload>--%>
                            <sys:frontFileUpload className="accessories-h30" fileitems="${applyFiles}"
                                                 readonly="true"></sys:frontFileUpload>
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
                            <td>${teacher.postTitle}</td>
                            <td>${fns:getDictLabel(teacher.education,'enducation_level','')}</td>
                            <td>${teacher.mobile}</td>
                            <td>${teacher.email}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>

            </div>
        </div>




        <c:if test="${not empty midFiles}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>中期报告</span> <i class="line"></i><a data-toggle="collapse" href="#midFiles"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>

            <div id="midFiles" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="row-fluid row-info-fluid">
                        <div class="span10" style="margin-top:10px;">
                            <span class="item-label">附件：</span>
                            <div class="controls" style="margin:-25px 0 0 135px;">
                                <sys:frontFileUpload className="accessories-h30" fileitems="${midFiles}"
                                                     readonly="true"></sys:frontFileUpload>
                            </div>
                        </div>
                    </div>

                    <c:if test="${not empty proMidSubmit.stageResult}">
                        <div class="row-fluid row-info-fluid">
                            <div class="span11" style="margin-top:10px;margin-left: -30px;">
                                <span class="item-label" style="width:150px;">已取得阶段性成果：</span>
                                <div style="margin:-20px 0 0 170px;word-break: break-all">
                                    <label>${proMidSubmit.stageResult}</label>
                                </div>
                            </div>
                        </div>
                    </c:if>


                </div>
            </div>



        </c:if>


        <c:if test="${not empty closeFiles}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>结项报告</span> <i class="line"></i><a data-toggle="collapse" href="#closeFiles"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>

            <div id="closeFiles" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="row-fluid row-info-fluid">
                        <div class="span10" style="margin-top:10px;">
                            <span class="item-label">附件：</span>
                            <div class="controls" style="margin:-25px 0 0 135px;">
                                    <%--<sys:frontFileUpload fileitems="${closeFiles}" filepath="project"></sys:frontFileUpload>--%>
                                <sys:frontFileUpload className="accessories-h30" fileitems="${closeFiles}"
                                                     readonly="true"></sys:frontFileUpload>

                            </div>
                        </div>
                    </div>

                    <c:if test="${not empty proCloseSubmit.stageResult}">
                        <div class="row-fluid row-info-fluid">
                            <div class="span11" style="margin-top:10px;margin-left: -30px;">
                                <span class="item-label" style="width:150px;">已取得阶段性成果：</span>
                                <div style="margin:-20px 0 0 170px;word-break: break-all">
                                    <label>${proCloseSubmit.stageResult}</label>
                                </div>
                            </div>
                        </div>
                    </c:if>


                </div>
            </div>


        </c:if>


        <c:if test="${not empty list1 or not empty list2 or not empty list3 or not empty list4 or not empty list5 or not empty list6}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核记录</span> <i class="line"></i> <a
                        data-toggle="collapse" href="#resultDetail"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
        </c:if>
        <div id="resultDetail">
            <c:if test="${not empty list1}">
                <div class="panel-body collapse in" style="margin-bottom:0;">
                    <div class="panel-inner" style="margin-bottom:0;">
                        <div class="row-fluid row-info-fluid" style="margin-left:90px;margin-bottom:0;">
                            <div class="span10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover"
                                       style="margin-bottom:0;border-radius:0;">
                                    <thead>
                                    <tr>
                                        <th colspan="4" style="text-align: center">立项审核</th>
                                    </tr>
                                    <tr>
                                        <th class="auditorWidth">审核人</th>
                                        <th class="projectWidth">项目初审</th>
                                        <th class="suggestionWidth">建议及意见</th>
                                        <th class="timeWidth">审核时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list1}" var="item">
                                        <tr>
                                            <td>${item.user.name}</td>
                                            <td>
                                                <c:if test="${item.grade == '1'}">通过</c:if>
                                                <c:if test="${item.grade == '0'}">不通过</c:if>
                                            </td>
                                            <td style="word-break: break-all">${item.suggest}</td>
                                            <td><fmt:formatDate value="${item.updateDate}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                        <c:if test="${item.grade == '0'}">
                                            <tr>
                                                <td style="color: #DF4526;font-weight: bold;">立项结果</td>
                                                <td colspan="3"
                                                    style="color: #DF4526;text-align: right;font-weight: bold;">不同意立项
                                                </td>
                                            </tr>
                                        </c:if>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty list2}">
                <div class="panel-body collapse in" style="margin-bottom:0;">
                    <div class="panel-inner" style="margin-bottom:0;">
                        <div class="row-fluid row-info-fluid" style="margin-left:90px;margin-bottom:0;">
                            <div class="span10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover"
                                       style="margin-bottom:0;border-top:none;border-radius:0;">
                                    <thead>
                                    <tr>
                                        <th colspan="4" style="text-align: center">立项评分</th>
                                    </tr>
                                    <tr>
                                        <th class="auditorWidth">评分专家</th>
                                        <th class="projectWidth">评分</th>
                                        <th class="suggestionWidth">建议及意见</th>
                                        <th class="timeWidth">评分时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list2}" var="item">
                                        <tr>
                                            <td>${item.user.name}</td>
                                            <td>
                                                    ${item.score}
                                            </td>
                                            <td style="word-break: break-all">${item.suggest}</td>
                                            <td><fmt:formatDate value="${item.updateDate}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${not empty average2}">
                                        <tr>
                                            <td style="color: #DF4526;font-weight: bold;">平均分</td>
                                            <td colspan="3"
                                                style="color: #DF4526;text-align: right;font-weight: bold;">${average2}</td>
                                        </tr>
                                        <tr>
                                            <td style="color: #DF4526;font-weight: bold;">立项结果</td>
                                            <td colspan="3" style="color: #DF4526;text-align: right;font-weight: bold;">
                                                <c:if test="${average2 < 80}">不</c:if>同意立项
                                            </td>
                                        </tr>
                                    </c:if>
                                    </tbody>

                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty list3}">
                <div class="panel-body collapse in" style="margin-bottom:0;">
                    <div class="panel-inner" style="margin-bottom:0;">
                        <div class="row-fluid row-info-fluid" style="margin-left:90px;margin-bottom:0;">
                            <div class="span10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover"
                                       style="margin-bottom:0;border-top:none;border-radius:0;">
                                    <thead>
                                    <tr>
                                        <th colspan="4" style="text-align: center">中期评分</th>
                                    </tr>
                                    <tr>
                                        <th class="auditorWidth">评分专家</th>
                                        <th class="projectWidth">评分</th>
                                        <th class="suggestionWidth">建议及意见</th>
                                        <th class="timeWidth">评分时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list3}" var="item">
                                        <tr>
                                            <td>${item.user.name}</td>
                                            <td>
                                                    ${item.score}
                                            </td>
                                            <td style="word-break: break-all">${item.suggest}</td>
                                            <td><fmt:formatDate value="${item.updateDate}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${not empty average3}">
                                        <tr>
                                            <td style="color: #DF4526;font-weight: bold;">平均分</td>
                                            <td colspan="3"
                                                style="color: #DF4526;text-align: right;font-weight: bold;">${average3}</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty list4}">
                <div class="panel-body collapse in" style="margin-bottom:0;">
                    <div class="panel-inner" style="margin-bottom:0;">
                        <div class="row-fluid row-info-fluid" style="margin-left:90px;margin-bottom:0;">
                            <div class="span10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover"
                                       style="margin-bottom:0;border-top:none;border-radius:0;">
                                    <thead>
                                    <tr>
                                        <th colspan="4" style="text-align: center">中期审核</th>
                                    </tr>
                                    <tr>
                                        <th class="auditorWidth">审核人</th>
                                        <th class="projectWidth">中期审核</th>
                                        <th class="suggestionWidth">建议及意见</th>
                                        <th class="timeWidth">审核时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list4}" var="item">
                                        <tr>
                                            <td>${item.user.name}</td>
                                            <td>
                                                <c:if test="${item.grade == '1'}">通过</c:if>
                                                <c:if test="${item.grade == '0'}">不通过</c:if>
                                            </td>
                                            <td style="word-break: break-all">${item.suggest}</td>
                                            <td><fmt:formatDate value="${item.updateDate}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                    </c:forEach>
                                        <%--<tr>--%>
                                        <%--<td style="color: #DF4526;font-weight: bold;">中期审核结果</td>--%>
                                        <%--<td colspan="3" style="color: #DF4526;text-align: right;font-weight: bold;">--%>
                                        <%--<c:if test="${item.grade == '1'}">通过</c:if>--%>
                                        <%--<c:if test="${item.grade == '0'}">不通过</c:if>--%>
                                        <%--</td>--%>
                                        <%--</tr>--%>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty list5}">
                <div class="panel-body collapse in" style="margin-bottom:0;">
                    <div class="panel-inner" style="margin-bottom:0;">
                        <div class="row-fluid row-info-fluid" style="margin-left:90px;margin-bottom:0;">
                            <div class="span10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover"
                                       style="margin-bottom:0;border-top:none;border-radius:0;">
                                    <thead>
                                    <tr>
                                        <th colspan="4" style="text-align: center">结项评分</th>
                                    </tr>
                                    <tr>
                                        <th class="auditorWidth">评分专家</th>
                                        <th class="projectWidth">评分</th>
                                        <th class="suggestionWidth">建议及意见</th>
                                        <th class="timeWidth">评分时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list5}" var="item">
                                        <tr>
                                            <td>${item.user.name}</td>
                                            <td>
                                                    ${item.score}
                                            </td>
                                            <td style="word-break: break-all">${item.suggest}</td>
                                            <td><fmt:formatDate value="${item.updateDate}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                    </c:forEach>
                                    <c:if test="${not empty average5}">
                                        <tr>
                                            <td style="color: #DF4526;font-weight: bold;">平均分</td>
                                            <td colspan="3"
                                                style="color: #DF4526;text-align: right;font-weight: bold;">${average5}</td>
                                        </tr>
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
            <c:if test="${not empty list6}">
                <div class="panel-body collapse in" style="margin-bottom:0;">
                    <div class="panel-inner" style="margin-bottom:0;">
                        <div class="row-fluid row-info-fluid" style="margin-left:90px;margin-bottom:0;">
                            <div class="span10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover"
                                       style="margin-bottom:0;border-top:none;border-radius:0;">
                                    <thead>
                                    <tr>
                                        <th colspan="4" style="text-align: center">结项审核</th>
                                    </tr>
                                    <tr>
                                        <th class="auditorWidth">审核人</th>
                                        <th class="projectWidth">结项审核</th>
                                        <th class="suggestionWidth">建议及意见</th>
                                        <th class="timeWidth">审核时间</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:forEach items="${list6}" var="item">
                                        <tr>
                                            <td>${item.user.name}</td>
                                            <td>
                                                    ${fns:getDictLabel(item.grade, 'project_result', '')}
                                            </td>
                                            <td style="word-break: break-all">${item.suggest}</td>
                                            <td><fmt:formatDate value="${item.updateDate}"
                                                                pattern="yyyy-MM-dd HH:mm"/></td>
                                        </tr>
                                    </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>
        </div>
    </form:form>


    <div class="form-actions-cyjd text-center" style="border-top: none">
        <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
    </div>


</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
</body>


</html>
