<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <title>${frontTitle}</title>

</head>
<body>

<style>
    .auditorWidth{
        width:20%;
    }

    .projectWidth {
        width: 10%;
    }

    .suggestionWidth {
        width: 56%;
    }

    .timeWidth {
        width: 18%;
    }

</style>


<div class="container container-ct">
    <input type="hidden" id="pageType" value="edit">
    <div class="row-apply">
        <div class="row row-top-bar">
            <c:set var="sse" value="${fns:getUserById(proModel.declareId)}"/>
            <div class="col-xs-4">
                <p class="text-center topbar-item">项目编号：${proModel.competitionNumber}</p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item">填报日期：<fmt:formatDate value='${proModel.createDate}' pattern='yyyy-MM-dd'/></p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item"> 申报人：${sse.name}</p>
            </div>
        </div>
        <h4 class="titlebar">项目申报表</h4>
        <div class="contest-wrap form-horizontal form-horizontal-apply">
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">项目负责人：</label>
                    <p class="form-control-static">${sse.name}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学号：</label>
                    <p class="form-control-static">${sse.no}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">性别：</label>
                    <p class="form-control-static">${fns:getDictLabel(sse.sex, 'sex', '')}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学院：</label>
                    <p class="form-control-static">${sse.office.name}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">专业年级：</label>
                    <p class="form-control-static">${fns:getProfessional(sse.professional)}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">联系电话：</label>
                    <p class="form-control-static">${sse.mobile}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">E-mail：</label>
                    <p class="form-control-static">${sse.email}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">身份证号：</label>
                    <p class="form-control-static">${sse.idNumber}</p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>项目基本信息</span>
                    <i class="line"></i>
                    <a data-toggle="collapse" href="#applicatioinDetail"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div class="form-horizontal form-horizontal-apply" style="margin-bottom:40px;">
                <div id="applicatioinDetail" class="panel-body collapse in">
                    <div class="row row-user-info">
                        <div class="col-xs-6">
                            <label class="label-static">项目名称：</label>
                            <p class="form-control-static" style="word-break: break-all">${proModel.pName}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">项目类别：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModel.proCategory, "project_type","" )}</p>
                        </div>
                    </div>
                    <div class="row row-user-info">
                        <div class="col-xs-11">
                            <label class="label-static">项目简介：</label>
                            <p class="form-control-static" style="word-break: break-all">${proModel.introduction}</p>
                        </div>
                    </div>
                    <div class="row row-user-info">
                        <div class="col-xs-4">
                            <label class="label-static">项目logo：</label>
                            <p class="form-control-static"><img style="width:40px;height: 40px;border-radius:50%;"
                                                                id="logoImg" paramurl="${proModel.logo.url }"
                                                                class="backimg"
                                                                src="${empty proModel.logo.url ? '/images/upload-default-image1.png':fns:ftpImgUrl(proModel.logo.url)}">
                            </p>
                        </div>
                    </div>
                </div>


                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                        <a data-toggle="collapse" href="#projectDetail"><i
                                class="icon-collaspe icon-double-angle-up"></i></a>
                    </div>
                </div>
                <div class="table-condition form-horizontal form-horizontal-apply">
                    <div id="projectDetail" class="panel-body collapse in">
                        <div class="row row-user-info">
                            <div class="col-xs-8">
                                <label class="label-static">团队名称：</label>
                                <p class="form-control-static">${team.name}</p>
                            </div>
                        </div>
                        <div class="table-title">
                            <span>学生团队</span>
                            <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
                        </div>
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                            <thead>
                            <tr id="studentTr">
                                <th>序号</th>
                                <th>姓名</th>
                                <th>工号</th>
                                <th>学院</th>
                                <th>专业</th>
                                <th>手机号</th>
                                <th>现状</th>
                                <th>当前在研</th>
                                <th>技术领域</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                                <c:forEach items="${teamStu}" var="item" varStatus="status">
                                    <tr>
                                        <td>${status.index+1 }</td>
                                        <td>${item.name }</td>
                                        <td>${item.no }</td>
                                        <td>${item.org_name }</td>
                                        <td>${item.professional}</td>
                                        <td>
                                                ${item.mobile }
                                        </td>
                                        <td>${fns:getDictLabel(item.currState, 'current_sate', '')}</td>
                                        <td>${item.curJoin }</td>
                                        <td>${item.domain }</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                        <div class="table-title">
                            <span>指导教师</span>
                        </div>
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>姓名</th>
                                <th>工号</th>
                                <th>单位(学院或企业、机构)</th>
                                <th>导师来源</th>
                                <th>当前指导</th>
                                <th>技术领域</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                                <c:forEach items="${teamTea}" var="item" varStatus="status">
                                    <tr>
                                        <td>${status.index+1 }</td>
                                        <td>${item.name }</td>
                                        <td>${item.no }</td>
                                        <td>${item.org_name }</td>
                                        <td>${item.teacherType}</td>
                                        <td>${item.curJoin }</td>
                                        <td>${item.domain }</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目申报资料</span>
                        <i class="line"></i>
                        <a data-toggle="collapse" href="#elseDetail"><i
                                class="icon-collaspe icon-double-angle-up"></i></a>
                    </div>
                </div>
                <div id="elseDetail" class="panel-body collapse in">
                    <div class="form-group" style="margin-left: 2px;">
                        <label class="control-label label-static">申报资料：</label>
                        <div class="form-control-static" style="padding: 0;width:80%;">
                            <sys:frontFileUpload className="accessories-h34" fileitems="${sysAttachments}"
                                                 readonly="true"></sys:frontFileUpload>
                        </div>
                    </div>
                </div>


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
                                <div class="row-fluid row-info-fluid" style="width:90%;margin-left:60px;margin-bottom:0;">
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
                                <div class="row-fluid row-info-fluid" style="width:90%;margin-left:60px;margin-bottom:0;">
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
                                                        <c:if test="${average2 < 80}">不</c:if>
                                                        同意立项
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
                                <div class="row-fluid row-info-fluid" style="width:90%;margin-left:60px;margin-bottom:0;">
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
                                <div class="row-fluid row-info-fluid" style="width:90%;margin-left:60px;margin-bottom:0;">
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
                                <div class="row-fluid row-info-fluid" style="width:90%;margin-left:60px;margin-bottom:0;">
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
                                <div class="row-fluid row-info-fluid" style="width:90%;margin-left:60px;margin-bottom:0;">
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
            </div>
        </div>
    </div>


    <div class="form-actions-cyjd text-center" style="border-top: none">
        <a class="btn btn-default" href="${ctxFront}/project/projectDeclare/list">返回</a>
    </div>

</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

</body>
</html>