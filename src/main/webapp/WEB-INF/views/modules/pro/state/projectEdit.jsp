<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>项目变更</title>
    <script type="text/javascript">
        $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
        ftpHttp="${ftpHttp}";
    </script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/gcProject/GC_check_new.css">
    <link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>
    <link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script type="text/javascript" src="${ctxCommon}/common-js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxCommon}/common-js/datepicker/My97DatePicker/WdatePicker.js"></script>
    <script type="text/javascript" src="${ctxJs}/gcProject/project_check.js"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/projectModify.css">
    <script type="text/javascript" src="${ctxJs}/gcProject/projectModify.js"></script>
    <script type="text/javascript" src="${ctxStatic}/common/initiate.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/frontCommon.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/common.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=1111">
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>

<body>

<style>

    .minus {
        display: inline-block;
        width: 19px;
        height: 19px;
        background: url(/img/minuse.png) no-repeat;
    }

    .minus:hover {
        background: url(/img/minus2.png) no-repeat;
        cursor: pointer;
    }

    .plus {
        display: inline-block;
        width: 19px;
        height: 19px;
        background: url(/img/plus.png) no-repeat;
    }

    .plus:hover {
        background: url(/img/plus2.png) no-repeat;
        cursor: pointer;
    }

</style>


<div id="dialog-message" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
<div class="prj-change-wrap">
    <div class="mybreadcrumbs"
         style="border-bottom: 3px solid #f4e6d4; margin-top: 11px; margin-bottom: 45px;">
        <span>项目查询 - 项目变更</span>
    </div>
    <form:form id="inputForm" modelAttribute="projectDeclare"
               action="${ctx}/state/saveEdit" method="post">
        <input type="hidden" id="id" name="id" value="${projectDeclare.id}"/>
        <input type="hidden" id="year" name="year" value="${projectDeclare.year}"/>
        <input type="hidden" id="modifyPros" name="modifyPros" value="1"/>
        <input type="hidden" id="xfAuthorize" value="${xfAuthorize}"/>

        <div class="container-fluid content-wrap">
            <!--项目基本信息 -->
            <div class="row">
                <c:set var="leader"
                       value="${fns:getUserById(projectDeclare.leader)}"/>
                <div class="form-horizontal">
                    <div class="form-group col-sm-4 col-md-4 col-lg-4">
                        <label class="col-xs-5 ">项目负责人：</label>
                        <p class="col-xs-7">
                                ${fns:getUserById(projectDeclare.leader).name}</p>
                    </div>
                    <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
                    <div class="form-group col-sm-4 col-md-4 col-lg-4">
                        <label class="col-xs-5">学院：</label>
                        <p class="col-xs-7" id="officeName">${leader.office.name}</p>
                    </div>
                </div>

                <div class="form-horizontal">
                    <div class="form-group col-sm-4 col-md-4 col-lg-4">
                        <label class="col-xs-5 ">学号：</label>
                        <p class="col-xs-7" id="no">${leader.no}</p>
                    </div>
                    <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
                    <div class="form-group col-sm-4 col-md-4 col-lg-4">
                        <label class="col-xs-5">专业年级：</label>
                        <p class="col-xs-7" id="zylj">${fns:getOffice(leader.professional).name}
                            <fmt:formatDate
                                    value="${student.enterDate}" pattern="yyyy"/>
                        </p>
                    </div>
                </div>

                <div class="form-horizontal">
                    <div class="form-group col-sm-4 col-md-4 col-lg-4">
                        <label class="col-xs-5 ">联系电话：</label>
                        <p class="col-xs-7" id="mobile">${leader.mobile}</p>
                    </div>
                    <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
                    <div class="form-group col-sm-4 col-md-4 col-lg-4">
                        <label class="col-xs-5">E-mail：</label>
                        <p class="col-xs-7" id="email">${leader.email}</p>
                    </div>
                </div>
            </div>

            <section class="row">
                <div class="prj_common_info">
                    <h3>项目基本信息</h3>
                    <span class="yw-line"></span> <a href="javascript:;"
                                                     id="commonToggle" data-flag="true"><span
                        class="icon-double-angle-up"></span></a>
                </div>
                <div class="toggle_wrap" id="commonToggle_wrap">
                    <div class="form-horizontal row my-form-horizontal" novalidate>

                        <div class="form-group col-sm-6 col-md-6 col-lg-6">
                            <label style="width: 127px;"><span class="star">*</span>项目名称：</label>
                            <span class="tips-wrap"> <input type="text" name="name"
                                                            id="name" value="${projectDeclare.name}"
                                                            class="my-textarea required"/>
									<p class="name-err-tips"></p>
								</span>
                        </div>
                        <div class="form-group col-sm-6 col-md-6 col-lg-6">
                            <label style="width: 127px;"><span class="star">*</span>项目编号：</label>
                            <span class="tips-wrap"> <input type="text" name="number"
                                                            id="number" value="${projectDeclare.number}"
                                                            class="my-textarea required"/>
									<p class="name-err-tips"></p>
								</span>
                        </div>
                    </div>

                    <div class="form-horizontal row my-form-horizontal" novalidate>
                        <div class="form-group col-sm-6 col-md-6 col-lg-6"
                             style="margin-bottom: 0px;">

                            <label style="width: 127px;"><span class="star">*</span>项目类别：</label>
                            <span> <form:select path="type" onchange="changeRatio();"
                                                style="width:150px;resize: none;border: 1px solid #ddd;">
                                <form:options items="${fns:getDictList('project_type')}"
                                              itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
								</span>
                        </div>
                        <div class="form-group col-sm-6 col-md-6 col-lg-6"
                             style="margin-bottom: 0px;">

                            <label style="width: 127px;"><span class="star">*</span>项目来源：</label>
                            <span> <form:select path="source"
                                                style="width:150px;resize: none;border: 1px solid #ddd;">
                                <form:options items="${fns:getDictList('project_source')}"
                                              itemLabel="label" itemValue="value" htmlEscape="false"/>
                            </form:select>
								</span>
                        </div>
                    </div>

                    <div class="form-horizontal row prj-plan my-form-horizontal"
                         novalidate>

                        <label><span class="star">*</span>项目拓展及传承规划：</label> <span
                            class="tips-wrap"> <form:checkboxes path="development"
                                                                items="${fns:getDictList('project_extend')}"
                                                                itemLabel="label"
                                                                itemValue="value" class="required"/> <input
                            type="hidden"
                            id="developmentStr" value="${projectDeclare.development}"/>
								<p class="name-err-tips"></p>
							</span>
                    </div>
                </div>
            </section>


            <section class="row">
                <div class="prj_common_info">
                    <h3>团队信息</h3>
                    <span class="yw-line"></span>
                    <a href="javascript:;" id="teamToggle" data-flag="true">
                        <span class="icon-double-angle-up"></span>
                    </a>
                </div>
                <div class="toggle_wrap" id="teamToggle_wrap">
                    <table class="table  table-hover table-condensed table-bordered studenttb">
                        <caption>
                            <strong style="font-weight: 500; font-size: 14px; margin-left: -15px; color: #656565;">
                                <span class="star">*</span>项目团队：
                            </strong>
                            <span>${team.name}</span><span class="star xfpb">学分配比规则：</span>
                            <span id="xfpbgz" class="star xfpb"></span>
                        </caption>
                        <caption style="font-size: 14px;">学生团队</caption>
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>学号</th>
                            <th>联系电话</th>
                            <th>学院</th>
                            <th>当前在研</th>
                            <th>职责</th>
                            <th class="xfpb">学分配比</th>
                            <th><input type="button" class="btn btn-primary" userType="1" value="新增"
                                       onclick="selectUser(this)"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${turStudents}" var="item" varStatus="status">
                            <tr userid="${item.userId}">
                                <input class="custindex" type="hidden" name="studentList[${status.index}].userId"
                                       value="${item.userId}">
                                <input class="custindex" type="hidden" name="studentList[${status.index}].userType"
                                       value="1">
                                <td>${item.name}</td>
                                <td>${item.no}</td>
                                <td>${item.mobile}</td>
                                <td>${item.org_name}</td>
                                <td>${item.curJoin}</td>
                                <td>
                                    <select class="zzsel custindex" name="studentList[${status.index}].userzz">
                                        <option value="0"
                                                <c:if test="${item.userId==projectDeclare.leader}">selected</c:if> >
                                            负责人
                                        </option>
                                        <option value="1"
                                                <c:if test="${item.userId!=projectDeclare.leader}">selected</c:if> >
                                            组成员
                                        </option>
                                    </select>
                                </td>
                                <td class="xfpb">
                                    <input type="text" class="custindex xfpbval" style="width: 30px;" maxlength="10"
                                           name="studentList[${status.index}].weightVal"
                                           value="${item.weightVal}">
                                </td>
                                <td><a class="minus"></a></td>
                            </tr>
                        </c:forEach>

                        </tbody>
                    </table>

                    <table class="table  table-hover table-condensed table-bordered teachertb">
                        <caption class="prj_tab_caption"
                                 style="font-size: 14px; color: #656565;">指导老师
                        </caption>
                        <thead>
                        <tr>
                            <th>姓名</th>
                            <th>工号</th>
                            <th>导师来源</th>
                            <th>职称（职务）</th>
                            <th>学历</th>
                            <th>联系电话</th>
                            <th>当前指导</th>
                            <th><input type="button" class="btn btn-primary" userType="2" value="新增"
                                       onclick="selectUser(this)"/></th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${turTeachers}" var="item" varStatus="status">
                            <tr userid="${item.userId}">
                                <input class="custindex" type="hidden" name="teacherList[${status.index}].userId"
                                       value="${item.userId}">
                                <input class="custindex" type="hidden" name="teacherList[${status.index}].userType" value="2">
                                <td>${item.name}</td>
                                <td>${item.no}</td>
                                <td>${item.teacherType}</td>
                                <td>${item.technical_title}</td>
                                <td>${fns:getDictLabel(item.education,'enducation_level','')}</td>
                                <td>${item.mobile}</td>
                                <td>${item.curJoin}</td>
                                <td><a class="minus"></a></td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>


            <section class="row">
                <div class="prj_common_info">
                    <h3>项目介绍</h3>
                    <span class="yw-line"></span> <a href="javascript:;"
                                                     id="projectToggle" data-flag="true"><span
                        class="icon-double-angle-up"></span></a>
                </div>

                <div class="toggle_wrap" id="projectToggle_wrap">
                    <div class="prj_introduce">
                        <h5 style="margin-left: -15px; color: #656565;">
                            <span class="star">*</span>项目简介：
                        </h5>
                        <span class="tips-wrap"> <textarea
                                class="prj_desc required" name="introduction" maxlength="2000">${projectDeclare.introduction}</textarea>
								<p class="name-err-tips"></p>
							</span>
                    </div>

                    <div class="prj_introduce">
                        <h5 style="margin-left: -15px; color: #656565;">
                            <span class="star">*</span>前期调研准备：
                        </h5>
                        <span class="tips-wrap"> <textarea
                                class="prj_desc required" name="innovation" maxlength="2000">${projectDeclare.innovation}</textarea>
								<p class="name-err-tips"></p>
							</span>
                    </div>

                    <div class="prj_introduce">
                        <h5 style="margin-left: -15px; color: #656565;">
                            <span class="star">*</span>项目预案：
                        </h5>
                        <table class="table  table-hover table-condensed table-bordered">
                            <thead>
                            <th width="35%">实施方案</th>
                            <th width="21%">时间安排</th>
                            <th width="35%">保障措施</th>
                            </thead>
                            <tbody>
                            <tr>
                                <td class="tips-wrap"><textarea id="planContent"
                                                                name="planContent"
                                                                class="my-textarea required" maxlength="2000">${projectDeclare.planContent}</textarea>
                                    <p class="tname-err-tips"></p></td>
                                <td class="tips-wrap"><input class="Wdate required"
                                                             name="planStartDate" id="planStartDate"
                                                             onClick="WdatePicker({maxDate:'#F{$dp.$D(\'planEndDate\')}'})"
                                                             value='<fmt:formatDate value="${projectDeclare.planStartDate}" pattern="yyyy-MM-dd"/>'/>
                                    至 <input class="Wdate required" name="planEndDate"
                                             id="planEndDate"
                                             onClick="WdatePicker({minDate:'#F{$dp.$D(\'planStartDate\')}'})"
                                             value='<fmt:formatDate value="${projectDeclare.planEndDate}" pattern="yyyy-MM-dd"/>'/>
                                    <span class="tname-err-tips start-time"></span> <span
                                            class="tname-err-tips end-time"></span></td>
                                <td class="tips-wrap"><textarea id="planStep"
                                                                name="planStep"
                                                                class="my-textarea required" maxlength="2000">${projectDeclare.planStep}</textarea>
                                    <p class="tname-err-tips"></p></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                    <div class="prj_introduce">
                        <h5 style="margin-left: -15px;">
                            <span class="star">*</span>任务分工：
                        </h5>
                        <table class="table  table-hover table-condensed table-bordered">
                            <thead>
                            <th>序号</th>
                            <th>工作任务</th>
                            <th>任务描述</th>
                            <th width="290">时间安排</th>
                            <th width="80">成本</th>
                            <th width="70">质量评价</th>
                            <th width="80">操作</th>
                            </thead>
                            <tbody id="tbody1">
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>

            <section class="row">
                <div class="prj_common_info">
                    <h3>预期成果</h3>
                    <span class="yw-line"></span> <a href="javascript:;"
                                                     id="resultToggle" data-flag="true"><span
                        class="icon-double-angle-up"></span></a>
                </div>
                <div class="toggle_wrap" id="resultToggle_wrap">
                    <form class="form-horizontal" novalidate>

                        <div class="form-group col-sm-12 col-md-12 col-lg-12 prj-plan"
                             style="padding-left: 0px;">
                            <label style="width: 100px; margin-left: -15px;"><span
                                    class="star" style="margin-right: 0px;">*</span>成果形式：</label> <span
                                class="tips-wrap"> <form:checkboxes path="resultType"
                                                                    items="${fns:getDictList('project_result_type')}"
                                                                    itemLabel="label" itemValue="value"
                                                                    class="required"/> <input
                                type="hidden" id="resultTypeStr"
                                value="${projectDeclare.resultType}"/> <span
                                class="name-err-tips"></span>
								</span>
                        </div>

                        <div class="form-group col-sm-12 col-md-12 col-lg-12"
                             style="padding-left: 0px;">
                            <label class="col-xs-1 tips-wrap"
                                   style="width: 100px; margin-left: -15px; padding-left: 0px;"
                                   class="required"><span class="star">*</span>成果说明：</label>
                            <textarea name="resultContent" maxlength="2000"
                                      class="prj_desc col-xs-10 required">${projectDeclare.resultContent}</textarea>
                            <span class="name-err-tips result-desc"></span>
                        </div>
                    </form>
                </div>
            </section>

            <section class="row">
                <div class="prj_common_info">
                    <h3>经费预算</h3>
                    <span class="yw-line"></span> <a href="javascript:;"
                                                     id="budgetToggle" data-flag="true"><span
                        class="icon-double-angle-up"></span></a>
                </div>
                <div class="budget" id="budgetToggle_wrap">
                    <textarea name="budget" maxlength="2000" class="change-budget">${projectDeclare.budget}</textarea>
                </div>
            </section>

            <section class="row">
                <div class="prj_common_info">
                    <h3>附件</h3>
                    <span class="yw-line"></span> <a href="javascript:;"
                                                     id="uploadFile" data-flag="true"><span
                        class="icon-double-angle-up"></span></a>
                </div>
                    <%--<sys:adminFileUpload fileitems="${fileListMap}" filepath="project"--%>
                    <%--btnid="upload"></sys:adminFileUpload>--%>

                <sys:frontFileUpload fileitems="${fileListMap}" filepath="project"></sys:frontFileUpload>

            </section>
            <section class="row">
                <div class="prj_common_info">
                    <h3>审核记录</h3><span class="yw-line"></span>
                    <a href="javascript:;" id="checkRecord" data-flag="true"><span class="icon-double-angle-up"></span></a>
                </div>
                <div style="padding:10px" class="toggle_wrap" id="checkRecord_wrap">
                    <table class="table table-hover table-bordered">
                        <c:if test="${fn:length(infos1)>0}" >
                            <thead class="prj_table_head">
                            <th colspan="4" style="text-align: center;">立项审核记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>审核人</th>
                            <th>项目评级</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <tbody>
                            <c:forEach items="${infos1}" var="info">
                                <tr>
                                    <td align="center">${fns:getUserById(info.createBy.id).name}</td>
                                    <td align="center">${fns:getDictLabel(info.grade, "project_degree", info.grade)}</td>
                                    <td>${info.suggest}</td>
                                    <td align="center">
                                        <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </c:if>

                        <c:if test="${fn:length(infos2)>0}" >
                            <thead class="th_align-center">
                            <th colspan="4" style="text-align: center;">中期检查评分记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>评分专家</th>
                            <th>评分</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <tbody>
                            <c:forEach items="${infos2}" var="info">
                                <tr>
                                    <td align="center">${fns:getUserById(info.createBy.id).name}</td>
                                    <td align="center">${info.score}</td>
                                    <td>${info.suggest}</td>
                                    <td align="center">
                                        <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach >

                            <tr>
                                <td class="prj_range" align="center">平均分</td>
                                <td colspan="3" class="result_score">${projectDeclare.midScore}</td>
                            </tr>

                            </tbody>
                        </c:if>

                        <c:if test="${fn:length(infos3)>0}" >
                            <thead class="th_align-center">
                            <th colspan="4" style="text-align: center;">中期检查结果记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>审核人</th>
                            <th>中期结果</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <c:forEach items="${infos3}" var="info">
                                <tr>
                                    <td align="center">${fns:getUserById(info.createBy.id).name}</td>
                                    <td align="center">${fns:getDictLabel(info.grade, "project_result", info.grade)}</td>
                                    <td>${info.suggest}</td>
                                    <td align="center">
                                        <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:if>

                        <c:if test="${fn:length(infos4)>0}" >
                            <thead class="prj_table_head">
                            <th colspan="4" style="text-align: center;">结项评分记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>评分专家</th>
                            <th>评分</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <tbody>
                            <c:forEach items="${infos4}" var="info">
                                <tr>
                                    <td align="center">${fns:getUserById(info.createBy.id).name}</td>
                                    <td align="center">${info.score}</td>
                                    <td>${info.suggest}</td>
                                    <td align="center">
                                        <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach >

                            <tr>
                                <td class="prj_range">平均分</td>
                                <td colspan="3" class="result_score">${projectDeclare.finalScore}</td>
                            </tr>

                            </tbody>
                        </c:if>

                        <c:if test="${fn:length(infos5)>0}" >
                            <thead class="prj_table_head">
                            <th colspan="4" style="text-align: center;">答辩分记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>审核人</th>
                            <th>评分</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <tbody>
                            <c:forEach items="${infos5}" var="info">
                                <tr>
                                    <td align="center">${fns:getUserById(info.createBy.id).name}</td>
                                    <td align="center">${info.score}</td>
                                    <td>${info.suggest}</td>
                                    <td align="center">
                                        <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach >
                            </tbody>
                        </c:if>

                        <c:if test="${fn:length(infos6)>0}" >
                            <thead class="prj_table_head">
                            <th colspan="4" style="text-align: center;">结果评定记录</th>
                            </thead>
                            <thead class="th_align-center">
                            <th>审核人</th>
                            <th>结果</th>
                            <th>建议及意见</th>
                            <th>审核时间</th>
                            </thead>
                            <tbody>
                            <c:forEach items="${infos6}" var="info">
                                <tr>
                                    <td align="center">${fns:getUserById(info.createBy.id).name}</td>
                                    <td align="center">${fns:getDictLabel(info.grade, "project_result", info.grade)}</td>
                                    <td>${info.suggest}</td>
                                    <td align="center">
                                        <fmt:formatDate value="${info.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                    </td>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </c:if>

                    </table>
                </div>
                <c:if test="${not empty changeGnodes}">
                <div class="form-horizontal row my-form-horizontal" novalidate>
                    <div class="form-group col-sm-6 col-md-6 col-lg-6"
                         style="margin-bottom: 0px;">

                        <label style="width: 127px;">变更流程到节点：</label>
                        <span> <form:select path="toGnodeId"
                                            style="width:150px;resize: none;border: 1px solid #ddd;">
                            <form:option value="" label="--请选择--"/>
                            <form:options items="${changeGnodes}" itemValue="value"
                                          itemLabel="name"
                                          htmlEscape="false"/>
                        </form:select>
								</span>
                    </div>
                </div>
                </c:if>
            </section>

            <section class="row">

                <div class="row" style="text-align: center; margin-bottom: 60px;">


                    <c:if test="${not empty proMidId}">
                        <a href="${ctx}/project/proMid/view?id=${proMidId}" target="_blank"
                           class="btn btn-sm btn-primary " role="btn">中期检查报告</a>
                    </c:if>
                    <c:if test="${not empty proCloseId}">
                        <a href="${ctx}/project/projectClose/view?id=${proCloseId}"
                           target="_blank" class="btn btn-sm btn-primary " role="btn">结项报告</a>
                    </c:if>
                    <a type="button" data-control="uploader" class="btn btn-sm btn-primary" role="btn"
                       onclick="submitData();" id="submitBtn">保存</a>
                    <a href="javascript:;"
                       class="btn btn-sm btn-primary" role="btn"
                       onclick="history.go(-1)">返回</a>
                </div>

            </section>
        </div>
    </form:form>
</div>
<script type="text/template" id="tpl">
    <tr>
        <td>
            {{idx}}
        </td>
        <td class="td_input tips-wrap">
            <textarea name="planList[{{nameIdx}}].content"
                      maxlength="2000"
                      class="my-textarea required planListTextarea">{{row.content}}</textarea>
            <span class="tname-err-tips"></span>
        </td>
        <td class="td_input tips-wrap">
            <textarea name="planList[{{nameIdx}}].description" maxlength="2000" class="my-textarea required planListTextarea">{{row.description}}</textarea>
            <span class="tname-err-tips"></span>
        </td>

        <td class="tips-wrap">
            <input id="startDate[{{idIdx}}]" name="planList[{{nameIdx}}].startDate" class="Wdate required planListStart"
                   type="text"
                   onClick="WdatePicker({maxDate:'#F{$dp.$D(\'endDate[{{idIdx}}]\')}'} )"
                   value="{{row.startDate}}"/>
            <span>至</span>
            <input id="endDate[{{idIdx}}]" name="planList[{{nameIdx}}].endDate" class="Wdate required planListEnd"
                   type="text"
                   onClick="WdatePicker({minDate:'#F{$dp.$D(\'startDate[{{idIdx}}]\')}'}  )"
                   value="{{row.endDate}}"/>
            <span class="start-time tstart-time"></span>
            <span class="end-time tend-time"></span>
        </td>
        <td class="td_input tips-wrap">
            <input style="width:65px;" name="planList[{{nameIdx}}].cost"
                   class="my-textarea required planListInput number" value="{{row.cost}}"/>
            <span class="tname-err-tips" style="left: 13px;"></span>
        </td>
        <td class="td_input tips-wrap">
            <input style="width:65px;" name="planList[{{nameIdx}}].quality" class="my-textarea required planListInput"
                   value="{{row.quality}}"/>
            <span class="tname-err-tips"></span>
        </td>


        <td class="opt_btn" style="width:120px">
            <button type="button" onclick="addRow('#tbody1',tpl,'');"
               class="btn btn-sm btn-sm-reset btn-add"><span class="icon-plus"></span></button>
            {{#delBtn}}<button type="button"  onclick="delRow(this);"
                          class="btn btn-sm btn-sm-reset btn-delete"><span class="icon-minus "></span></button>{{/delBtn}}
        </td>
    </tr>
</script>
<script type="text/template" id="tplInfo2">
    <tr>
        <input type="hidden" name="midAuditList[{{idx}}].createBy.id" value="{{row.userId}}"/>
        <input type="hidden" name="midAuditList[{{idx}}].createDate" value="{{row.createDate}}"/>
        <input type="hidden" name="midAuditList[{{idx}}].auditStep" value="2"/>
        <td align="center">
            {{row.userName}}
        </td>
        <td align="center" class="tips-wrap">
            <input name="midAuditList[{{idx}}].score" value="{{row.score}}" class="midScore required number infoMsg "
                   onblur="delMidScore();"/>
            <span class="name-err-tips prj-record-tips"></span>
        </td>
        <td>
            <textarea name="midAuditList[{{idx}}].suggest" class="my-textarea"
                      maxlength="2000">{{row.suggest}}</textarea>
        </td>
        <td align="center">
            {{row.createDate}}
        </td>
    </tr>
</script>
<script type="text/template" id="tplInfo4">
    <tr>
        <input type="hidden" name="closeAuditList[{{idx}}].createBy.id" value="{{row.userId}}"/>
        <input type="hidden" name="closeAuditList[{{idx}}].createDate" value="{{row.createDate}}"/>
        <input type="hidden" name="closeAuditList[{{idx}}].auditStep" value="4"/>
        <td align="center">
            {{row.userName}}
        </td>
        <td align="center" class="tips-wrap">
            <input name="closeAuditList[{{idx}}].score" value="{{row.score}}"
                   class="closeScore required number infoMsg " onblur="delCloseScore();"/>
            <span class="name-err-tips prj-record-tips"></span>
        </td>
        <td>
            <textarea name="closeAuditList[{{idx}}].suggest" class="my-textarea"
                      maxlength="2000">{{row.suggest}}</textarea>
        </td>
        <td align="center">
            {{row.createDate}}
        </td>
    </tr>
</script>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">
    $(document).ready(function () {
        changeRatio();
        var students = ${fns:toJson(studentList)};
        //增加下拉框change事件
        $("#leader").change(function () {
            //遍历students
            var thisId = $(this).val();
            $.each(students, function (i, val) {
                if (val.id == thisId) {
                    setValue(val);
                }
            })

        })

        $.each($("#developmentStr").val().split(","), function (i, item) {
            $("input[name='development'][ value='" + item + "']").attr("checked", true);
        });
        $.each($("#resultTypeStr").val().split(","), function (i, item) {
            $("input[name='resultType'][ value='" + item + "']").attr("checked", true);
        });
    });

    function setValue(val) {
        $("#officeName").text(val.officeName);
        $("#no").text(val.no);
        if (val.professional && val.enterDate) {
            $("#zylj").text(val.professional + val.enterDate);
        }
        if (val.professional && (typeof val.enterDate == "undefined")) {
            $("#zylj").text(val.professional)
        }
        if ((typeof val.professional == "undefined") && val.enterDate) {
            $("#zylj").text(val.enterDate)
        }
        if ((typeof val.professional == "undefined") && (typeof val.enterDate == "undefined")) {
            $("#zylj").text("");
        }
        $("#mobile").text(val.mobile);
        $("#email").text(val.email);
    }

    var validate;
    $(document).ready(function () {
        validate = $("#inputForm").validate({
            ignore: ".ignore",
            rules: {
                "number": {
                    remote: {
                        url: "/a/project/projectDeclare/checkNumber",     //后台处理程序
                        type: "post",               //数据发送方式
                        dataType: "json",           //接受数据格式
                        data: {                     //要传递的数据
                            id: "${projectDeclare.id}",
                            num: function () {
                                return $("input[name='number']").val();
                            }
                        }
                    }
                }
            },
            messages: {
                "number": {
                    remote: "项目编号已存在"
                }
            },
            errorPlacement: function (error, element) {
                if (element.attr("name") == "name") {
                    $(element).siblings(".name-err-tips").html(error);
                }
                else if (element.attr("name") == "development") {
                    $(element).parent().siblings(".name-err-tips").html(error);
                }
                else if (element.attr("name") == "introduction") {
                    $(element).siblings(".name-err-tips").html(error);
                }
                else if (element.attr("name") == "innovation") {  //前期调研准备
                    $(element).siblings(".name-err-tips").html(error);
                }
                else if (element.attr("name") == "planContent") {  //实施方案
                    $(element).siblings(".tname-err-tips").html(error);
                }
                else if (element.attr("name") == "planStartDate") {  //时间安排 开始时间
                    $(element).siblings(".start-time").html(error);
                }
                else if (element.attr("name") == "planEndDate") {  //时间安排 结束时间
                    $(element).siblings(".end-time").html(error);
                }
                else if (element.attr("name") == "planStep") {  //保障措施
                    $(element).siblings(".tname-err-tips").html(error);
                }
                else if (element.attr("class").indexOf("planListTextarea") >= 0) {
                    $(element).siblings('.tname-err-tips').html(error)
                }
                else if (element.attr("class").indexOf("planListStart") >= 0) {
                    $(element).siblings('.start-time').html(error)
                }
                else if (element.attr("class").indexOf("planListEnd") >= 0) {
                    $(element).siblings('.end-time').html(error)
                }
                else if (element.attr("class").indexOf("planListInput") >= 0) {
                    $(element).siblings('.tname-err-tips').html(error)
                }
                else if (element.attr("name") == "resultType") {  //成果形式
                    $(element).parent().siblings(".name-err-tips").html(error);
                }
                else if (element.attr("name") == "resultContent") {  //成果说明
                    $(element).siblings(".name-err-tips").html(error);
                }
                else if (element.attr("class").indexOf("infoMsg") >= 0) {
                    $(element).siblings(".prj-record-tips").html(error);
                }
                else {
                    error.insertAfter(element);
                }
            }
        });
    });


    function submitData() {
        //隐藏审核信息不校验
        setIgnore();
        if (!checkRatio()) {
            alertx("学分配比不符合规则");
            return;
        }
        var haveFzr = false;
        $(".zzsel").each(function (i, v) {
            if ($(v).val() == "0") {
                haveFzr = true;
                return;
            }
        });
        if (!haveFzr) {
            alertx("请选择负责人");
            return;
        }
        if (validate.form()) {
            $("#submitBtn").removeAttr("onclick");
            $("#submitBtn").addClass("disabled");

            $("#inputForm").ajaxSubmit(function (data) {
                if (data.ret == "0") {
                    $("#submitBtn").attr("onclick", "submit();");
                    $("#submitBtn").removeClass("disabled");
                    alertx(data.msg);
                } else {
                    alertx(data.msg, function () {
                        //history.go(-1);
                        window.location.href="${ctx}/state/allList";
                    });
                }
            });
        }
    }

    function setIgnore() {
        $("#checkRecord_wrap select").each(function () {
            if ($(this).parent().parent().is(":hidden") || $(this).parent().parent().parent().is(":hidden")) {
                $(this).addClass("ignore");
                $("#modifyPros").val("0");
            } else {
                $(this).removeClass("ignore");
                $("#modifyPros").val("1");
            }
        })
        $("#checkRecord_wrap input").each(function () {
            if ($(this).parent().parent().is(":hidden") || $(this).parent().parent().parent().is(":hidden")) {
                $(this).addClass("ignore");
                $("#modifyPros").val("0");
            } else {
                $(this).removeClass("ignore");
                $("#modifyPros").val("1");
            }
        })
    }

    var rowIdx = 1;
    var idIdx = 1;
    var tpl = $("#tpl").html();
    var delBtn = true;
    function addRow(tbodyId, tpl, row) {
        if (rowIdx == 1) {
            delBtn = false;
        } else {
            delBtn = true;
        }
        $(tbodyId).append(Mustache.render(tpl, {
            idx: rowIdx, nameIdx: rowIdx - 1, delBtn: delBtn, row: row, idIdx: idIdx
        }));
        rowIdx++;
        idIdx++;
    }
    function delRow(obj) {
        $(obj).parent().parent().remove();
        rowIdx--;
        reOrder();
    }
    function reOrder() {
        //重置序号
        $("#tbody1  tr").find("td:first").each(function (i, v) {
            $(this).html(i + 1);
        });
        var index = 0;
        var rex = "\\[(.+?)\\]";
        $("#tbody1 tr").each(function (i, v) {
            $(this).find("[name]").each(function (i2, v2) {
                var name = $(this).attr("name");
                var indx = name.match(rex)[1];
                ;
                $(this).attr("name", name.replace(indx, index));
            });
            index++;
        });
    }

    var data = ${fns:toJson(plans)};
    if (data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            addRow('#tbody1', tpl, data[i]);
        }
    } else {
        addRow('#tbody1', tpl, '');
    }

    $(document).ready(function () {
        $("#info01Grade").val("${info01.grade}");  //学院立项评审下拉框初始化值
        $("#info02Grade").val("${info02.grade}");  //学校立项评审下拉框初始化值
        $("#info3Grade").val("${info3.grade}"); //中期评级下拉框初始化
        changeSchool();
        initInfo02();
        //设置立项评审下拉框change事件，值为2显示学校立项评级，其他隐藏
        $("#info01Grade").change(function () {

            var type = 1;
            if ($(this).val() == 4) {  //C级
                changeMidInfo(type);
                changeMidCheck(type);
                changeCloseInfo(type);
                changeReply(type);
                changeAssess(type);
            }
            if ($(this).val() == 6) {   //提交给校级评审
                if ($("#info02Grade").val() == 1 || $("#info02Grade").val() == 2) {
                    type = 2;
                    changeMidCheck(type);
                    changeReply(type);
                    changeAssess(type);
                }
            }
            changeSchool();
        })

        $("#info02Grade").change(function () {   //学校立项下拉框change事件 ,选择A+、A时 中期检查评分为校级 其他为院级
            var type = 1;
            if ($(this).val() == 1 || $(this).val() == 2) {
                type = 2;
            }
            if ($(this).val() != "") {
                changeMidInfo(type);
                changeMidCheck(type);
                changeCloseInfo(type);
                changeReply(type);
                changeAssess(type);
            }

        })

        $("#info3Grade").change(function () {  //中期评级下拉框change事件
            if ($(this).val() == 2 || $(this).val() == "") { //不合格
                $("#info3Grade").parent().parent().parent().nextAll().hide();
            } else {
                $("#info3Grade").parent().parent().parent().nextAll().show();
            }
        })
        $("#info6Grade").val("${info6.grade}"); //结果评定下拉框初始化
    });


    function initInfo02() {
        var type = 1;
        if ($("#info02Grade").val() == 1 || $("#info02Grade").val() == 2) {
            type = 2;
        }
        if ($("#info02Grade").val() != "") {
            changeMidInfo(type);
            changeMidCheck(type);
            changeCloseInfo(type);
            changeReply(type);
            changeAssess(type);
        }
    }


    function changeSchool() {

        var grade01 = $("#info01Grade").val();
        if (grade01 == 6) {  //校级
            $("#info01Grade").parent().parent().next("tr").show();
            $("#info01Grade").parent().parent().parent().nextAll().show();
            changeMidNext();
        } else if (grade01 == 4 || grade01 == "") {  //C 请选择
            $("#info01Grade").parent().parent().next("tr").hide();
            $("#info01Grade").parent().parent().parent().nextAll().show();
            changeMidNext();
        } else {  // 不合格 隐藏其他的
            $("#info01Grade").parent().parent().next("tr").hide();
            $("#info01Grade").parent().parent().parent().nextAll().hide();
        }
    }

    //根据中期检查结果记录 （合格、不合格）来显示隐藏后面的节点
    function changeMidNext() {  //0 合格 2 不合格
        console.log("info3Grade", $("#info3Grade").val())
        if ($("#info3Grade").val() == 2 || $("#info3Grade").val() == "") { //不合格
            $("#info3Grade").parent().parent().parent().nextAll().hide();
        } else {
            $("#info3Grade").parent().parent().parent().nextAll().show();
        }
    }

    //改变中期评分的人
    function changeMidInfo(type) { //type=1代表学院 2代表学校
        $("#tbodyInfo2").html(""); //先清空
        var data = ${fns:toJson(info11)};
        if (type == 2) {
            data = ${fns:toJson(info12)};
        }
        midIdx = 0;
        if (data.length > 0) {
            for (var i = 0; i < data.length; i++) {
                addRow2('#tbodyInfo2', tplInfo2, data[i]);
            }
        }
        delMidScore();
    }

    //改变结项评分的人
    function changeCloseInfo(type) {  //type=1代表学院 2代表学校
        $("#tbodyInfo4").html(""); //先清空
        var data = ${fns:toJson(info41)};
        if (type == 2) {
            data = ${fns:toJson(info42)};
        }
        closeIdx = 0;
        if (data.length > 0) {
            for (var i = 0; i < data.length; i++) {
                addRow4('#tbodyInfo4', tplInfo4, data[i]);
            }
        }
        delCloseScore();
    }

    var collegeSec =${fns:toJson(collegeSec)};
    var schoolSec =${fns:toJson(schoolSec)};
    //改变中期检查的人
    function changeMidCheck(type) { //type=1代表学院 2代表学校
        if (type == 2) {
            $("#info3Mid").val(schoolSec.id);
            $("#info3Name").text(schoolSec.name);
        } else {
            $("#info3Mid").val(collegeSec.id);
            $("#info3Name").text(collegeSec.name);
        }
    }

    //改变答辩分记录的人
    function changeReply(type) { //type=1代表学院 2代表学校
        if (type == 2) {
            $("#info5Reply").val(schoolSec.id);
            $("#info5Name").text(schoolSec.name);
        } else {
            $("#info5Reply").val(collegeSec.id);
            $("#info5Name").text(collegeSec.name);
        }
    }

    //改变结果评定的人
    function changeAssess(type) {
        if (type == 2) {
            $("#info6Assess").val(schoolSec.id);
            $("#info6Name").text(schoolSec.name);
        } else {
            $("#info6Assess").val(collegeSec.id);
            $("#info6Name").text(collegeSec.name);
        }
    }

    //处理中期平均分
    function delMidScore() {
        var midScore;
        var totalScore = 0;
        var number = 0;
        $(".midScore").each(function (index, element) {
            totalScore = totalScore + parseInt($(element).val());
            number++;
        })
        midScore = totalScore / number;
        $("#midScore").val(midScore.toFixed(1));
        $("#midScoreTd").text(midScore.toFixed(1));
    }
    //处理结项平均分
    function delCloseScore() {
        var closeScore;
        var totalScore = 0;
        var number = 0;
        $(".closeScore").each(function (index, element) {
            totalScore = totalScore + parseInt($(element).val());
            number++;
        })
        closeScore = totalScore / number;
        $("#finalScore").val(closeScore.toFixed(1));
        $("#finalScoreTd").text(closeScore.toFixed(1));
    }

    var midIdx = 0;
    var tplInfo2 = $("#tplInfo2").html();
    function addRow2(tbodyId, tplInfo2, row) {
        $(tbodyId).append(Mustache.render(tplInfo2, {
            row: row, idx: midIdx
        }));
        midIdx++;

    }
    var grade = "${projectDeclare.level}";
    var data = ${fns:toJson(info11)};
    if (grade == "1" || grade == "2") {
        data = ${fns:toJson(info12)};
    }

    if (data.length > 0) {
        for (var i = 0; i < data.length; i++) {
            addRow2('#tbodyInfo2', tplInfo2, data[i]);
        }
    }

    var closeIdx = 0;
    var tplInfo4 = $("#tplInfo4").html();
    function addRow4(tbodyId, tplInfo4, row) {
        $(tbodyId).append(Mustache.render(tplInfo4, {
            row: row, idx: closeIdx
        }));
        closeIdx++;
    }
    var grade4 = "${projectDeclare.level}";
    var data4 = ${fns:toJson(info41)};
    if (grade4 == "1" || grade4 == "2") {
        data4 = ${fns:toJson(info42)};
    }
    if (data4.length > 0) {
        for (var i = 0; i < data4.length; i++) {
            addRow4('#tbodyInfo4', tplInfo4, data4[i]);
        }
    }

    /* 团队变更 */
    $(document).on('click', '.minus', function (e) {
        var tbd = $(this).parents("tbody");
        var tb = $(this).parents("table");
        $(this).parents("tr").remove();
        resetNameIndex(tbd);
        if (tb.hasClass("studenttb")) {
            $(".xfpbval").val("");
        }
        changeRatio();
    });
    $(document).on('change', '.zzsel', function (e) {
        if ($(this).val() == "0") {
            $(".zzsel").val("1");
            $(this).val("0");
        }
    });
    function saveModify() {
        var haveFzr = false;
        $(".zzsel").each(function (i, v) {
            if ($(v).val() == "0") {
                haveFzr = true;
                return;
            }
        });
        if (!haveFzr) {
            alertx("请选择负责人");
            return;
        }
        if (validate1.form()) {
            $("#saveBtn").removeAttr("onclick");
            $("#saveBtn").addClass("disabled");
            $("#inputForm").ajaxSubmit(function (data) {
                if (data.ret == "0") {
                    $("#saveBtn").attr("onclick", "saveModify();");
                    $("#saveBtn").removeClass("disabled");
                    alertx(data.msg);
                } else {
                    alertx(data.msg, function () {
                        history.go(-1);
                    });
                }
            });
        }
    }
    function selectUser(ob) {
        var c_btn = $(ob);
        var idsarr = [];
        c_btn.parents("table").find("tbody>tr").each(function (i, v) {
            idsarr.push($(v).attr("userid"));
        });
        var ids = idsarr.join(",");
        var userType = c_btn.attr("userType");
        var ititle = "选择学生";
        if (userType == '2') {
            ititle = "选择导师";
        }
        top.$.jBox.open('iframe:' + '/a/backUserSelect?ids=' + ids + '&userType=' + userType, ititle, 1100, 540, {
            buttons: {"确定": "ok", "关闭": true}, submit: function (v, h, f) {
                var temarr = [];
                $("input[name='boxTd']:checked", $(h.find("iframe")[0].contentDocument).find("iframe")[0].contentDocument)
                    .each(function (i, v) {
                        temarr.push($(v).val());
                    });
                if (v == "ok") {
                    addSelectUser(userType, temarr.join(","));
                    return true;
                }
            },
            loaded: function (h) {
                $(".jbox-content", top.document).css("overflow-y", "hidden");
            }
        });
    }
    function addSelectUser(usertype, ids) {
        if (ids != "") {
            if ("1" == usertype) {
                $.ajax({
                    type: "POST",
                    url: "/a/selectUser/getStudentInfo",
                    data: {ids: ids},
                    success: function (data) {
                        if (data) {
                            var datahtml = "";
                            var tpl = $("#tpl_st").html();
                            $.each(data, function (i, v) {
                                datahtml = datahtml + Mustache.render(tpl, {
                                    userId: v.userId,
                                    name: v.name,
                                    no: v.no,
                                    mobile: v.mobile,
                                    office: v.office,
                                    curJoin: v.curJoin
                                });
                            });
                            $(".studenttb>tbody").append(datahtml);
                            resetNameIndex($(".studenttb>tbody"));
                            $(".xfpbval").val("");
                            changeRatio();
                        }
                    }
                });
            } else {
                $.ajax({
                    type: "POST",
                    url: "/a/selectUser/getTeaInfo",
                    data: {ids: ids},
                    success: function (data) {
                        if (data) {
                            var datahtml = "";
                            var tpl = $("#tpl_tea").html();
                            $.each(data, function (i, v) {
                                datahtml = datahtml + Mustache.render(tpl, {
                                    userId: v.userId,
                                    name: v.name,
                                    no: v.no,
                                    teacherType: v.teacherType,
                                    postTitle: v.postTitle,
                                    education: v.education,
                                    mobile: v.mobile,
                                    curJoin: v.curJoin
                                });
                            });
                            $(".teachertb>tbody").append(datahtml);
                            resetNameIndex($(".teachertb>tbody"));
                        }
                    }
                });
            }
        }
    }
    function resetNameIndex(tbodyOb) {
        var indexNum = 0;
        var rex = "\\[(.+?)\\]";
        $(tbodyOb).find("tr").each(function (i, v) {
            $(v).find(".custindex").each(function (ti, tv) {
                var name = $(tv).attr("name");
                var indx = name.match(rex)[1];
                $(tv).attr("name", name.replace(indx, indexNum));
            });
            indexNum++;
        });
    }
    //根据项目类型和团队学生人数查找配比规则
    function changeRatio() {
        if ($("#xfAuthorize").val() == "true") {
            var type = $("#type").val();
            var snumber = $(".studenttb>tbody>tr").length;
            $.ajax({
                type: 'post',
                url: '/a/project/projectDeclare/findRatio',
                data: {
                    type: type,
                    snumber: snumber
                },
                success: function (data) {
                    if (data != "") {
                        $(".xfpb").attr("style", "display:;");
                        $("#xfpbgz").text(data);
                    } else {
                        $(".xfpb").attr("style", "display:none;");
                        $(".xfpbval").val("");
                    }
                }
            });
        }
    }
    //学分配比校验
    function checkRatio() {
        var ratio = $("#xfpbgz").text();
        var result = true;
        if (ratio != "") {
            var creditArr = [];
            $('.xfpbval').each(function (i, item) {
                creditArr.push($(item).val());
            });

            creditArr.sort(function (a, b) {
                return b - a;
            });
            var ratioArr = ratio.split(':').sort(function (a, b) {
                return b - a;
            });

            if (creditArr.join(':') !== ratioArr.join(':')) {
                result = false;
            }
        }
        return result;
    }

</script>
<script type="text/template" id="tpl_st">
    <tr userid="{{userId}}">
        <input type="hidden" class="custindex" name="studentList[custindex].userId" value="{{userId}}">
        <input type="hidden" class="custindex" name="studentList[custindex].userType" value="1">
        <td>{{name}}</td>
        <td>{{no}}</td>
        <td>{{mobile}}</td>
        <td>{{office}}</td>
        <td>{{curJoin}}</td>
        <td>
            <select class="zzsel custindex" name="studentList[custindex].userzz">
                <option value="0">负责人
                </option>
                <option value="1" selected>组成员
                </option>
            </select>
        </td>
        <td class="xfpb">
            <input type="text" class="custindex xfpbval" style="width: 30px;" maxlength="10"
                   name="studentList[custindex].weightVal"
                   value="${item.weightVal}">
        </td>
        <td>
            <a class="minus"></a>
        </td>
    </tr>
</script>
<script type="text/template" id="tpl_tea">
    <tr userid="{{userId}}">
        <input type="hidden" class="custindex" name="teacherList[custindex].userId" value="{{userId}}">
        <input type="hidden" class="custindex" name="teacherList[custindex].userType" value="2">
        <td>{{name}}</td>
        <td>{{no}}</td>
        <td>{{teacherType}}</td>
        <td>{{postTitle}}</td>
        <td>{{education}}</td>
        <td>{{mobile}}</td>
        <td>{{curJoin}}</td>
        <td>
            <a class="minus"></a>
        </td>
    </tr>
</script>
</body>

</html>
