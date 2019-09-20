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
<div class="container-fluid container-fluid-audit" style="margin-bottom: 60px;">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>项目审核</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/promodelAudit"
               method="post" class="form-horizontal form-container">
        <form:hidden path="id"/>
        <form:hidden path="act.taskId"/>
        <form:hidden path="act.taskName"/>
        <form:hidden path="act.taskDefKey"/>
        <form:hidden path="act.procInsId"/>
        <form:hidden path="act.procDefId"/>

        <input type="hidden" name="actionPath" value="${actionPath}"/>
        <input type="hidden" name="gnodeId" value="${gnodeId}"/>

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
                    <span class="item-label">填报日期：</span><fmt:formatDate value="${proModel.createDate}"></fmt:formatDate>
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
                        <img style="width:40px;height:40px;border-radius:50%;margin-left:-5px;" class="look-tu" src="${empty proModel.logo.url ? '/images/upload-default-image1.png':fns:ftpImgUrl(proModel.logo.url)}">
                    </div>
                </div>

                <div class="row-fluid row-info-fluid">
                    <div class="span10" style="margin-top:10px;">
                        <span class="item-label">附件：</span>
                        <div class="controls" style="margin:-25px 0 0 135px;">
                            <%--<sys:frontFileUpload fileitems="${sysAttachments}" filepath="project"></sys:frontFileUpload>--%>
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


        <!-- 后台创新创业领导初审 -->
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>
                    <%--<c:choose>--%>
                        <%--<c:when test="${proModel.act.taskDefKey =='sid-5afbd9c928004668b8ef68900c854297'}">--%>
                            <%--立项初审--%>
                        <%--</c:when>--%>
                        <%--<c:when test="${proModel.act.taskDefKey =='sid-862d4e4699964e8cae9bfb05a07fa368'}">--%>
                            <%--中期审核--%>
                        <%--</c:when>--%>
                        <%--<c:when test="${proModel.act.taskDefKey =='sid-fb536f4cf17e490d8f963a1a94258845'}">--%>
                            <%--结项审核--%>
                        <%--</c:when>--%>
                    <%--</c:choose>--%>
                    ${proModel.act.taskName}
                </span> <i class="line"></i> <a
                    data-toggle="collapse" href="#examDetail"><i
                    class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="examDetail" class="panel-body collapse in">
            <div class="control-group">
                <label class="control-label"><i>*</i>审核：</label>
                <div class="controls">
                    <form:select path="grade" id="sec-idea" class="form-control input-large required accpet">
                        <form:option value="" label="-请选择-"/>
                        <form:option value="1">优秀</form:option>
                        <form:option value="6">良好</form:option>
                        <form:option value="0">合格</form:option>
                        <form:option value="2">不合格</form:option>
                    </form:select>
                </div>
            </div>
            <div class="control-group" style="margin-top:30px;">
                <label class="control-label"><i style="display:none;">*</i>审核建议或意见：</label>
                <div class="controls" style="word-break: break-all">
                        <textarea style="word-break: break-all" id="auditRemarks" name="source" maxlength="200" rows="5" class="form-control input-xxlarge"
                                  placeholder="请给予您的意见和建议（最大长度200）"></textarea>
                </div>
            </div>
        </div>
        <div class="form-actions-cyjd text-center" style="border-top: none">
            <button type="submit" class="btn btn-primary" id="btnSumbit" style="margin-right: 10px;">提交</button>
            <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
        </div>
    </form:form>


</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>


<script>
    $(function () {
        var formValidate = $('#inputForm').validate();
    })
</script>


</body>

</html>
