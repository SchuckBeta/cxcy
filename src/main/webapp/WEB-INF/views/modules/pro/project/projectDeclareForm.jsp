<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <title>${frontTitle}</title>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form_new.css"/>
    <link rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <style>

        .ui-dialog .ui-dialog-titlebar-close{
            border: none;
        }
        .agree-actions{
            text-align: center;
        }

    </style>
</head>
<body>
<div class="container container-ct">
    <%--<div class="edit-bar clearfix" style="margin-top:0;">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<div class="mybreadcrumbs" style="margin:0 0 20px 9px;">--%>
                <%--<i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;--%>
                <%--<a href="/f" style="color:#333;text-decoration: underline;">首页</a>&nbsp;&gt;&nbsp;--%>
                <%--<a href="${ctxFront}/page-innovation" style="color:#333;text-decoration: underline;">双创项目</a>&nbsp;&gt;&nbsp;--%>
                <%--项目申报--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/page-innovation">双创项目</a></li>
        <li class="active">项目申报</li>
    </ol>
    <input type="hidden" id="pageType" value="edit">
    <h4 class="main-title">大学生创新创业训练计划项目申报</h4>
    <form:form id="form1" modelAttribute="projectDeclareVo" action="save" method="post" class="form-horizontal"
               enctype="multipart/form-data">
        <input type='hidden' id="usertype" value="${user.userType}"/>
        <form:hidden path="projectDeclare.id" value="${projectDeclare.id}"/>
        <form:hidden path="projectDeclare.year" value="${projectDeclare.year}"/>
        <form:hidden path="projectDeclare.number" value="${projectDeclare.number}"/>
        <form:hidden path="projectDeclare.actywId" value="${projectDeclare.actywId}"/>
        <form:hidden path="projectDeclare.templateId" value="${projectDeclareVo.projectAnnounce.id}"/>
        <div class="contest-content">
            <div class="tool-bar">
                <a class="btn-print" onClick="window.print()" href="javascript:void(0);">打印申报表</a>
                <div class="inner">
                    <c:if test="${projectDeclare.id!=null}">
                        <span>项目编号：</span>
                        <i>${projectDeclare.number}</i>
                    </c:if>
                    <span>填表日期:</span>
                    <i>${sysdate}</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>${creater.name}</i>
                    <c:if test="${projectDeclare.id==null}">
                        <a href="javascript:void(0)">${projectDeclareVo.projectAnnounce.name}</a>
                    </c:if>
                </div>
            </div>
            <h4 class="contest-title">项目申报表</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">项目负责人：</label>
                            <div class="input-box">
                                <p class="form-control-static"> ${leader.name}</p>
                                <input type="hidden" id="user_name" value="${leader.name}"/>
                                <form:hidden path="projectDeclare.leader" value="${leader.id}"/>
                                <form:hidden path="projectDeclare.createBy" value="${creater.id}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static"> ${leader.office.name}</p>
                                <input type="hidden" name="user_officename" id="user_officename" class="form-control"
                                       value="${leader.office.name}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">学号/毕业年份：</label>
                            <div class="input-box">

                                <p class="form-control-static">${leader.no}
                                    <c:if test="${not empty studentExpansion.graduation}">
                                        /<fmt:formatDate value='${studentExpansion.graduation}' pattern='yyyy'/>
                                    </c:if>
                                    <c:if test="${empty studentExpansion.graduation}">
                                        <span class="gray-color">/暂无</span>
                                    </c:if>
                                </p>
                                <input type="hidden" id="user_no" name="user_no" value="${leader.no}"/>

                                <%--<c:if test="${studentExpansion.graduation!=null}">--%>
                                    <%--<input type="text" name="user_no" class="form-control"--%>
                                           <%--value="${leader.no}/<fmt:formatDate value='${studentExpansion.graduation}' pattern='yyyy'/>"--%>
                                           <%--readonly/>--%>
                                <%--</c:if>--%>
                                <%--<c:if test="${studentExpansion.graduation==null}">--%>
                                    <%--<input type="text" name="user_no" class="form-control"--%>
                                           <%--value="${leader.no}" readonly/>--%>
                                <%--</c:if>--%>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">专业年级：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getProfessional(leader.professional)}</p>
                                <input type="hidden" name="user_professional"
                                       value="${fns:getProfessional(leader.professional)}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">${leader.mobile}</p>
                                <input type="hidden" name="user_mobile" value="${leader.mobile}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">E-mail：</label>
                            <div class="input-box">
                                <p class="form-control-static">${leader.email}</p>
                                <input type="hidden" class="form-control" name="user_email"/>
                                <input type="hidden" name="graduation" id="graduation"
                                       value="<fmt:formatDate value='${studentExpansion.graduation}' pattern='yyyy-MM-dd'/>"/>
                                <input type="hidden" name="graduated" id="graduated"
                                       value="${studentExpansion.graduated}"/>
                                    <%--<input type="hidden" name="currState" id="currState"--%>
                                    <%--value="${studentExpansion.currState}"/>--%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目基本信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label for="projectDeclareName" class="control-label"><i
                                    class="icon-require">*</i>项目名称：</label>
                            <div class="input-box">
                                <input type="text" maxlength="128" class="form-control required" id="projectDeclareName"
                                       name="projectDeclare.name" value="${projectDeclare.name}"/>
                            </div>
                        </div>
                    </div>
                    <%--<div class="col-xs-12">--%>
                        <%--<div class="form-group">--%>
                            <%--<label class="control-label"><i--%>
                                    <%--class="icon-require">*</i>项目名称：</label>--%>
                            <%--<div class="input-box">--%>
                                <%--<form:input path="shortName" class="form-control required"  maxlength="32"></form:input>--%>
                            <%--</div>--%>
                        <%--</div>--%>
                    <%--</div>--%>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label for="projectDeclare.type" class="control-label"><i
                                    class="icon-require">*</i>项目类别：</label>
                            <div class="input-box">
                                <form:select required="required" path="projectDeclare.type"
                                             class="form-control" onchange="GCSB.findTeamPerson();">
                                    <form:option value="" label="--请选择--"/>
                                    <form:options items="${project_type}" itemValue="value" itemLabel="label"
                                                  htmlEscape="false"/></form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label for="projectDeclare.source" class="control-label"><i class="icon-require">*</i>项目来源：</label>
                            <div class="input-box">
                                <form:select required="required" path="projectDeclare.source"
                                             class="form-control">
                                    <form:option value="" label="--请选择--"/>
                                    <form:options items="${project_source}" itemValue="value" itemLabel="label"
                                                  htmlEscape="false"/></form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="form-group form-span-checkbox">
                            <label class="control-label"><i class="icon-require">*</i>项目拓展及传承：</label>
                            <div id="mycheckbox" class="input-box">
                                <div class="checkbox-span">
                                    <form:checkboxes class="required" path="projectDeclare.development" items="${project_extend}"
                                                     itemValue="value" itemLabel="label" htmlEscape="false"/>
                                    <form:hidden id="developmentStr" path="" value="${projectDeclare.development}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="table-condition">
                    <div class="form-group">
                        <label for="projectDeclare.teamId" class="control-label"><i
                                class="icon-require">*</i>团队信息：</label>
                        <div class="input-box" style="max-width: 394px;">
                            <form:select required="required" onchange="GCSB.findTeamPerson();" path="projectDeclare.teamId" class="input-medium form-control">
                                <form:option value="" label="--请选择--"/>
                                <form:options items="${teams}" itemValue="id" itemLabel="name"
                                              htmlEscape="false"/></form:select>
                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team studenttb">
                    <thead>
                    <tr id="studentTr">
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>学院</th>
                        <th>专业</th>
                        <th>技术领域</th>
                        <th>联系电话</th>
                        <th>在读学位</th>
                        <c:if test="${fns:checkMenuByNum(5)}">
                        <th width="50" class='credit-ratio'>学分配比</th>
                        </c:if>
                    </tr>
                    </thead>
                    <c:if test="${projectDeclareVo.teamStudent!=null&&projectDeclareVo.teamStudent.size() > 0}">
                        <c:forEach items="${projectDeclareVo.teamStudent}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}
                                <input type="hidden" name="teamUserRelationList[${status.index}].userId"
                                           value="${item.userId}"></td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                                <td><c:out value="${item.professional}"/></td>
                                <td><c:out value="${item.domain}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.instudy}"/></td>
                                <c:if test="${fns:checkMenuByNum(5)}">
                                <td class="credit-ratio">
                                    <input class="form-control input-sm "
                                           name="teamUserRelationList[${status.index}].weightVal"
                                           value="${item.weightVal}">
                                </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                    </c:if>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team teachertb">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>单位（学院或企业、机构）</th>
                        <th>职称（职务）</th>
                        <th>技术领域</th>
                        <th>联系电话</th>
                        <th>E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${projectDeclareVo.teamTeacher!=null&&projectDeclareVo.teamTeacher.size() > 0}">
                        <c:forEach items="${projectDeclareVo.teamTeacher}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.teacherType}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                                <td><c:out value="${item.technical_title}"/></td>
                                <td><c:out value="${item.domain}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目介绍</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label"><i
                            class="icon-require">*</i>项目介绍：</label>
                    <div class="input-box">
                        <textarea class="form-control" rows="3"
                                  name="projectDeclare.introduction"
                                  maxlength="2000">${projectDeclare.introduction}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label  class="control-label"><i
                            class="icon-require">*</i>前期调研准备：</label>
                    <div class="input-box">
                        <textarea class="form-control" rows="3"
                                  name="projectDeclare.innovation"
                                  maxlength="2000">${projectDeclare.innovation}</textarea>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label"><i class="icon-require">*</i>项目预案：</label>
                    <div class="input-box">
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team yuantb">
                            <thead>
                            <tr>
                                <th>实施预案</th>
                                <th width="300">时间安排</th>
                                <th>保障措施</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>
                            <textarea name="projectDeclare.planContent" class="form-control" rows="3"
                                      maxlength="2000">${projectDeclare.planContent}</textarea>
                                </td>
                                <td style="vertical-align: middle">
                                    <div class="time-input-inline">
                                        <input id="plan-start-date" class="Wdate form-control" type="text"
                                               name="projectDeclare.planStartDate" style="width: 100px;"
                                               value='<fmt:formatDate value="${projectDeclare.planStartDate}" pattern="yyyy-MM-dd"/>'
                                               onfocus="WdatePicker(getStartDatepicker())"/>
                                        <span>至</span>
                                        <input id="plan-end-date" class="Wdate form-control" type="text"
                                               name="projectDeclare.planEndDate" style="width: 100px;"
                                               value='<fmt:formatDate value="${projectDeclare.planEndDate}" pattern="yyyy-MM-dd"/>'
                                               onfocus="WdatePicker(getEndDatepicker())"/>
                                    </div>
                                </td>
                                <td>
                            <textarea name="projectDeclare.planStep" class="form-control" rows="3"
                                      maxlength="2000">${projectDeclare.planStep}</textarea>
                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label"><i class="icon-require">*</i>任务分工：</label>
                    <div class="input-box">
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team task">
                            <thead>
                            <tr>
                                <th width="48">序号</th>
                                <th width="130">工作任务</th>
                                <th width="130">任务描述</th>
                                <th width="300">时间安排</th>
                                <th width="100">成本（元）</th>
                                <th>质量评价</th>
                                <th width="65px">操作</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${projectDeclareVo.plans==null||projectDeclareVo.plans.size() ==0}">
                                <tr>
                                    <td>
                                        1
                                    </td>
                                    <td>
                                        <textarea name="plans[0].content" maxlength="2000" required rows="3"
                                                  class="form-control"></textarea>
                                    </td>
                                    <td>
                                        <textarea name="plans[0].description" maxlength="2000" required rows="3"
                                                  class="form-control"></textarea>
                                    </td>
                                    <td style="vertical-align: middle">
                                        <div class="time-input-inline">
                                            <input required id="plan-start-date-0" class="Wdate form-control"
                                                   style="width: 100px;"
                                                   type="text"
                                                   name="plans[0].startDate"
                                                   onClick="WdatePicker(getStartDatepicker(0))"/>
                                            <span>至</span>
                                            <input required id="plan-end-date-0" class="Wdate form-control" type="text"
                                                   style="width: 100px;"
                                                   name="plans[0].endDate"
                                                   onClick="WdatePicker(getEndDatepicker(0))"/>
                                        </div>
                                    </td>
                                    <td style="vertical-align: middle">
                                        <input type="text" class="number required form-control" maxlength="20"
                                               name="plans[0].cost"/>
                                    </td>
                                    <td>
                                        <textarea required name="plans[0].quality" class="form-control" rows="3"
                                                  maxlength="2000"></textarea>
                                    </td>
                                    <td>
                                        <a class="minus"></a>
                                        <a class="plus"></a>
                                    </td>
                                </tr>
                            </c:if>
                            <c:if test="${projectDeclareVo.plans!=null&&projectDeclareVo.plans.size() >0}">
                                <c:forEach items="${projectDeclareVo.plans}" var="item" varStatus="status">
                                    <tr>
                                        <td>
                                                ${status.index+1}
                                        </td>
                                        <td>
                                            <textarea required maxlength="2000" class="form-control" rows="3"
                                                      name="plans[${status.index}].content">${projectDeclareVo.plans[status.index].content }</textarea>
                                        </td>
                                        <td>
                                            <textarea required maxlength="2000" class="form-control" rows="3"
                                                      name="plans[${status.index}].description">${projectDeclareVo.plans[status.index].description }</textarea>
                                        </td>
                                        <td style="vertical-align: middle">
                                            <div class="time-input-inline">
                                                <input required id="plan-start-date-${status.index}"
                                                       class="Wdate form-control"
                                                       type="text"
                                                       style="width: 100px;"
                                                       value='<fmt:formatDate value="${projectDeclareVo.plans[status.index].startDate }" pattern="yyyy-MM-dd"/>'
                                                       name="plans[${status.index}].startDate"
                                                       onClick="WdatePicker(getStartDatepicker(${status.index}))"/>
                                                <span>至</span>
                                                <input required id="plan-end-date-${status.index}"
                                                       class="Wdate form-control" type="text"
                                                       style="width: 100px;"
                                                       value='<fmt:formatDate value="${projectDeclareVo.plans[status.index].endDate }" pattern="yyyy-MM-dd"/>'
                                                       name="plans[${status.index}].endDate"
                                                       onClick="WdatePicker(getEndDatepicker(${status.index}))"/>
                                            </div>
                                        </td>
                                        <td style="vertical-align: middle">
                                            <input type="text" class="number required form-control" maxlength="20"
                                                   name="plans[${status.index}].cost"
                                                   value="${projectDeclareVo.plans[status.index].cost }"/>
                                        </td>
                                        <td>
                                            <textarea required maxlength="2000"
                                                      name="plans[${status.index}].quality" rows="3"
                                                      class="form-control">${projectDeclareVo.plans[status.index].quality }</textarea>
                                        </td>
                                        <td>
                                            <a class="minus"></a>
                                            <a class="plus"></a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>预期成果</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="form-group form-span-checkbox">
                    <label class="control-label"><i class="icon-require">*</i>成果形式：</label>
                    <div id="exceptresult" class="input-box">
                        <div class="checkbox-span">
                            <form:checkboxes class="required" path="projectDeclare.resultType" items="${resultTypeList}"
                                             itemValue="value"
                                             itemLabel="label" htmlEscape="false"/>
                            <form:hidden id="resultTypeStr" path="" value="${projectDeclare.resultType}"/>

                        </div>
                    </div>
                </div>
                <div class="form-group">
                    <label class="control-label"><i
                            class="icon-require">*</i>成果说明：</label>
                    <div class="input-box">
                        <textarea class="form-control" rows="3"
                                  name="projectDeclare.resultContent"
                                  maxlength="2000">${projectDeclare.resultContent}</textarea>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>经费预算</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="form-group">
                    <textarea placeholder="简要描述在项目各个阶段产生的费用项目及明细，如：硬件采购、耗材费、差旅费等" maxlength="2000" rows="3"
                              class="form-control"
                              name="projectDeclare.budget">${projectDeclare.budget}</textarea>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>附件</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div style="padding: 0 20px;">
                    <sys:frontFileUpload fileitems="${projectDeclareVo.fileInfo}" className="accessories-h34" filepath="project" tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件"></sys:frontFileUpload>
                </div>
                <%--<sys:frontFileUploadCommon fileitems="${projectDeclareVo.fileInfo}" filepath="project"--%>
                                           <%--btnid="btnUpload"></sys:frontFileUploadCommon>--%>
                <div class="btngroup">
                    <a class="btn btn-default" href="javascript:history.go(-1);">返回</a>
                    <button type="button" data-control="uploader" class="btn btn-primary btn-save">保存</button>
                    <button type="submit" data-control="uploader"  class="btn btn-primary btn-submit">提交并保存</button>
                </div>
            </div>
        </div>
    </form:form>
</div>




<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/messages_zh.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/GCSBvalidate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxOther}/datepicker/My97DatePicker/WdatePicker.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/GCSB_new.js?v=1" type="text/javascript" charset="utf-8"></script>
<script>
    $(function () {
        var $form = $('#form1');
        function loginOutState() {
            dialogCyjd.createDialog(0, '未登录或登录超时。请重新登录，谢谢！', {
                dialogClass: 'dialog-cyjd-container none-close',
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-primary',
                    'click': function () {
                        location.href = 'list'
                    }
                }]
            })
        }

        $form.on('click', 'button.btn-save', function (e) {
            var ajaxXhr;
            var $this = $(this);
            var creditArr = [];
            var hasEmptyRatio;
            var $creditFormControls = $('.credit-ratio input.form-control');
            //学分配比的校验
            if($creditFormControls.size() > 0){
                $creditFormControls.each(function(i,item){
                    creditArr.push($(item).val());
                });
                hasEmptyRatio = creditArr.every(function (item) {
                    return item != '';
                });
                if(!hasEmptyRatio){
                    dialogCyjd.createDialog(0, "请完成未填写的学分配比！",{
                        buttons: [{
                            'text': '确定',
                            'class':'btn btn-primary',
                            'click': function () {
                                $( this ).dialog( "close" );
                                $("input[name='teamUserRelationList[0].weightVal']" ).focus();
                            }
                        }]
                    });
                    return false;
                }
            }

            if(!checkRatio()){
                dialogCyjd.createDialog(0, "学分配比不符合规则！",{
                    buttons: [{
                        'text': '确定',
                        'class':'btn btn-primary',
                        'click': function () {
                            $( this ).dialog( "close" );
                            $("input[name='teamUserRelationList[0].weightVal']" ).focus();
                        }
                    }]
                });
                return false;
            }
            $(this).prop('disabled', true).text('保存中...');
            $form.attr('action', 'save');
            ajaxXhr = $.post('save', $form.serialize());
            ajaxXhr.success(function (data) {
                if (GCSB.checkIsToLogin(data)) {
                    $this.prop('disabled', false).text('保存');
                    loginOutState();
                    return false;
                }
                if (data.ret == 1) {
                    $("[id='projectDeclare.id']").val(data.id);
                    dialogCyjd.createDialog(1, '保存成功，跟踪当前项目？', {
                        dialogClass: 'dialog-cyjd-container none-close',
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-sm btn-primary',
                            'click': function () {
                                top.location = "curProject";
                            }
                        }, {
                            text: '返回',
                            'class': 'btn btn-sm btn-default',
                            'click': function () {
                                top.location = "list";
                            }
                        }]
                    })
                } else {
                    dialogCyjd.createDialog(0, data.msg)
                }
                $this.prop('disabled', false).text('再次保存');
            });
            ajaxXhr.error(function (err) {
                dialogCyjd.createDialog(0, '网络连接失败，请重试')
                $this.prop('disabled', false).text('重新保存')
            })
        });

        $form.on('click', 'button.btn-submit', function (e) {
            var xhrPost;
            var $this = $(this);
            if (!validate1.form()) {
                validate1.focusInvalid();
                return false;
            }
            //学分配比的校验
            if(!checkRatio()){
                dialogCyjd.createDialog(0, "学分配比不符合规则！",{
                    buttons: [{
                        'text': '确定',
                        'class':'btn btn-primary',
                        'click': function () {
                            $( this ).dialog( "close" );
                            $("input[name='teamUserRelationList[0].weightVal']" ).focus();
                        }
                    }]
                });
                return false;
            }
            $form.attr('action', 'submit');
            showAgreeMessage(function (agree) {
                if (!agree) {
                    return false;
                }
                $form.attr('action', 'submit');
                $this.prop('disabled', true).text('提交中...');
                xhrPost = $.post('submit', $form.serialize());
                xhrPost.success(function (data) {
                    if (GCSB.checkIsToLogin(data)) {
                        $this.prop('disabled', false).text('保存并提交');
                        loginOutState();
                        return false;
                    }
                    if (data.ret == 1) {
                        $this.text('已提交');
                        $('button.btn-save').prop('disabled', true).text('已保存');
                        $('#btnUpload').remove();
                        dialogCyjd.createDialog(1, '保存成功，跟踪当前项目？', {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-sm btn-primary',
                                'click': function () {
                                    top.location = "curProject";
                                }
                            }, {
                                text: '返回',
                                'class': 'btn btn-sm btn-default',
                                'click': function () {
                                    top.location = "list";
                                }
                            }]
                        })
                    } else {
                        $this.prop('disabled', false).text('保存并提交');
                        dialogCyjd.createDialog(0, '保存失败，请重试')
                    }
                });
                xhrPost.error(function (err) {
                    dialogCyjd.createDialog(0, '网络连接失败，请重试')
                    $this.prop('disabled', false).text('提交并保存')
                })
            });
            return false;
        })
    })
</script>
</body>
</html>
