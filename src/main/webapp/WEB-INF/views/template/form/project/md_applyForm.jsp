<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/md_applyForm.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <style>
        .form-group .error a{
            color: red;
        }
    </style>
</head>
<body>


<div class="container container-ct">
    <input type="hidden" id="pageType" value="edit">
    <input type="hidden" id="hasDisabled" value="${isSubmit!=null && isSubmit==1 ? '1' : 0}">
    <div class="step-row">
        <div class="step-indicator">
            <a class="step completed" href="javascript:void (0);">第一步（填写基本信息）</a>
            <a class="step" href="javascript:void (0);">第二步（建设团队）</a>
            <a class="step" href="javascript:void (0);">第三步（提交项目申报表）</a>
        </div>
    </div>
    <input type="hidden" name="showStepNumber" id="showStepNumber" value="${showStepNumber}"/>
    <form:form id="competitionfm" modelAttribute="proModelMd" action="${ctxFront}/proprojectmd/proModelMd/ajaxSave" method="post" class="form-horizontal"
               enctype="multipart/form-data">

        <div class="contest-content">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：${proModel.competitionNumber}</span>
                    <span style="margin-left: 15px;">填表日期:</span>
                    <i>${sysdate}</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>${sse.name}</i>

                </div>
            </div>
            <h4 class="contest-title">项目申报表</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">项目负责人：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.name}</p>
                                <input type="hidden" name="id" id="proModelMdId" value="${proModelMd.id}"/>
                                <input type="hidden" name="proModel.declareId" id="declareId" value="${sse.id}"/>
                                <%--<input type="hidden" name="modelId" id="modelId" value="${proModelMd.modelId}"/>--%>
                                <input type="hidden" name="proModel.id" id="proModelId" value="${proModelMd.modelId}"/>
                                <input type="hidden" name="proModel.actYwId" id="proModel.actYwId" value="${actYw.id}"/>
                                <input type="hidden" name="proModel.type" id="proModel.type" value="${proProject.type}"/>
                                <input type="hidden" name="proModel.proType" id="proModel.proType" value="${proProject.proType}"/>
								<form:hidden path="proModel.year"/>
                                <input type="hidden" name="college" id="college" value="${sse.office.name}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">学号：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.no}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">性别：</label>
                            <div class="input-box">
                                <p class="form-control-static">
                                       ${fns:getDictLabel(sse.sex, 'sex', '')}
                                    </p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">民族：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.national}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.office.name}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">专业年级：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getProfessional(sse.professional)}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.mobile}</p>
                                <input type="hidden" id="mobile" value="${sse.mobile}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">E-mail：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.email}</p>
                                <input type="hidden" id="email" value="${sse.email}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">身份证号：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.idNumber}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">QQ：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.qq}</p>
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
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>项目名称：</label>
                            <div class="input-box">
                                <form:input path="proModel.pName" maxlength="128" class="form-control has-disabled required" value=""/>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>项目简称：</label>
                            <div class="input-box">
                                <form:input path="proModel.aliasName" maxlength="128" class="form-control has-disabled" value=""/>
                            </div>
                        </div>
                    </div>

                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>所属学科：</label>
                            <div class="input-box">
                                    <%--是否需要用treeselect--%>
                                <form:select path="subject" required="required" class="form-control has-disabled">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('0000000111')}" itemLabel="label"
                                                     itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <%--<label class="control-label"><i class="icon-require">*</i>项目类别：</label>
                            <div class="input-box">
                                <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>
                            </div>--%>
                            <flow:flowTypeSelect name="项目类别" divClass="input-box proModelProCateGoryBox" formClass="has-disabled" type="proModel.proCategory" typeList="${proCategoryMap}"></flow:flowTypeSelect>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>申报级别：</label>
                            <div class="input-box">
                                <form:select path="appLevel" required="required" class="form-control has-disabled">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('0000000196')}" itemLabel="label"
                                                    itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label">申请金额：</label>
                            <div class="input-box">
                                <form:input path="appAmount" type="text"  maxlength="20" class="form-control number positiveNum has-disabled" value=""/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>项目来源：</label>
                            <div class="input-box">
                              <%--  <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>--%>
                            <form:select path="proSource" required="required"  class="form-control has-disabled">
                                <form:option value="" label="请选择"/>
                                <form:options items="${fns:getDictList('project_soure_type')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row source-row hide">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>来源项目名称：</label>
                            <div class="input-box">
                                <form:input path="sourceProjectName" maxlength="256" class="form-control has-disabled" value=""/>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>来源项目类别：</label>
                            <div class="input-box">
                                <form:select path="sourceProjectType"   class="form-control has-disabled">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('0000000138')}" itemLabel="label"
                                                 itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                </div>
              <%--  <div class="row">
                    <div class="col-xs-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>是否申请入孵：</label>
                            <div class="input-box">
                                <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                --%>
                <div class="btngroup">
                    <button type="button" id="btnStepOne" class="btn btn-primary go-next">下一步</button>
                    <button type="button" id="saveStepOne" class="hide">保存</button>
                    <button id="goBackStepOne" type="button" class="btn btn-default">返回</button>
                </div>
            </div>
        </div>
    <%--</form:form>
    <form:form id="competitionfmSecond" modelAttribute="proModelMd" action="${ctxFront}/proprojectmd/proModelMd/save" method="post" class="form-horizontal"
                enctype="multipart/form-data">--%>
        <div class="contest-content">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：${proModel.competitionNumber}</span>
                    <span style="margin-left: 15px;">填表日期:</span>
                    <i>${sysdate}</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>${sse.name}</i>
                </div>
            </div>
            <h4 class="contest-title">项目申报表</h4>

            <div class="contest-wrap">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="table-condition">
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>团队信息：</label>
                        <div class="input-box">
                                <%--<select class="form-control required">
                                    <option>-请选择-</option>
                                </select>--%>

                            <form:select  cssStyle="display: inline-block;width: 394px;" onchange="findTeamPerson()" path="proModel.teamId"
                                         class="form-control has-disabled proModelTeamId">

                                <form:option value="" label="--请选择--"/>
                                <form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
                            </form:select>


                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span class="ratio"></span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover studenttb">
                    <thead>
                    <tr id="studentTr">
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>手机号</th>
                        <th>所在学院</th>
                            <%--  <th>项目分工</th>--%>
                    </tr>
                    </thead>
                    <tbody>
                        <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                            <c:forEach items="${teamStu}" var="item" varStatus="status">
                                <tr>
                                    <td>${status.index+1}</td>
                                    <td><c:out value="${item.name}"/></td>
                                    <td><c:out value="${item.no}"/></td>
                                    <td><c:out value="${item.mobile}"/></td>
                                    <td><c:out value="${item.org_name}"/></td>
                                    <input type="hidden" name="proModel.teamUserHistoryList[${status.index}].userId"
                                                                               value="${item.userId}">
                                </tr>
                            </c:forEach>
                        </c:if>
                    </tbody>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover teachertb">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th width="175">E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                        <c:forEach items="${teamTea}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.teacherType}"/></td>
                                <td><c:out value="${item.technical_title}"/></td>
                                <td><c:out value="${fns:getDictLabel(item.education,'enducation_level','')}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                          </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div class="btngroup">
                    <button type="button" class="btn btn-primary go-prev">上一步</button>
                    <button type="button" class="btn btn-primary go-next">下一步</button>
               <%--     <button type="button" id="saveStepTwo"  class="btn btn-primary-oe save-next">保存</button>--%>
                    <%--<button type="button" class="btn btn-default-oe">返回</button>--%>
                </div>

            </div>
        </div>

        <div class="contest-content hide">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：${proModel.competitionNumber}</span>
                    <span style="margin-left: 15px;">填表日期:</span>
                    <i>${sysdate}</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>${sse.name}</i>
                </div>
            </div>
            <h4 class="contest-title">项目申报表</h4>

            <div class="contest-wrap">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目申报资料</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label class="control-label">下载项目申请表：</label>
                            <div class="input-box">
                                <ul id="downFileUl" class="file-list">
                                    <li class="file-item">
                                        <div class="file-info">
                                            <img src="${ctxImg}/filetype/word.png">
                                            <a href="javascript:void(0);" data-url=""
                                               data-original="" data-size=""
                                               data-title=""
                                               data-type=""
                                               data-ftp-url=""></a>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-xs-12">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>上传申报资料：</label>
                            <div id="accessories" class="input-box">
                                <sys:frontFileUpload  fileitems="${sysAttachments}" fileInfoPrefix="proModel." className="accessories-h34" filepath="scoapply" tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件"></sys:frontFileUpload>
                            </div>
                            <div class="input-box">

                                <%--<ul id="accessoryListPdf" class="file-list">--%>
                                    <%--<c:forEach items="${sysAttachments}" var="sysAttachment">--%>
                                    <%--<li class="file-item">--%>
                                        <%--<div class="file-info">--%>
                                            <%--<img src="${ctxImg}/filetype/word.png">--%>
                                            <%--<a href="javascript:void(0);" data-url="${sysAttachment.url}"--%>
                                               <%--data-original="" data-size="" data-id="${sysAttachment.id}"--%>
                                               <%--data-title="${sysAttachment.name}"--%>
                                               <%--data-type=""--%>
                                               <%--data-ftp-url="${sysAttachment.url}">${sysAttachment.name}</a>--%>
                                            <%--<c:if test="${isSubmit==null || isSubmit!=1}"><i class="icon icon-remove-sign"></i> </c:if>--%>
                                        <%--</div>--%>
                                    <%--</li>--%>
                                    <%--</c:forEach>--%>
                                <%--</ul>--%>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="btngroup">
                    <input type="hidden" id="isSubmit"value="${isSubmit}">
                    <div id="btnUpload"
                         <c:if test="${isSubmit!=null && isSubmit==1}">disabled </c:if>
                         class="btn btn-primary has-disabled">上传申报资料</div>
                    <button type="button" class="btn btn-primary go-prev">上一步</button>
                    <button  id="saveApplyForm" data-control="uploader" type="button" class="btn btn-primary"
                            <c:if test="${isSubmit!=null && isSubmit==1}">disabled </c:if>
                        >保存</button>
                    <button id="submitApplyForm" data-control="uploader" type="button"
                            <c:if test="${isSubmit!=null && isSubmit==1}">disabled </c:if>
                            class="btn btn-primary">提交</button>
                </div>
            </div>
        </div>
        <div id="fujianpp" style="display:none">
        <c:forEach items="${sysAttachments}" var="sysAttachment">
        	<input type='hidden' name='proModel.attachMentEntity.fielSize' value='${sysAttachment.size}'/>
        	<input type='hidden' name='proModel.attachMentEntity.fielTitle' value='${sysAttachment.name}'/>
        	<input type='hidden' name='proModel.attachMentEntity.fielType' value='${sysAttachment.suffix}'/>
        	<input type='hidden' name='proModel.attachMentEntity.fielFtpUrl' value='${sysAttachment.url}'/>
        	</c:forEach>
        </div>
    </form:form>

</div>
<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
<script>
   window.wtypes = '${wtypes}';
   window.wprefix = '${wprefix}';
</script>
<script src="${ctxJs}/md/mdApplyBak.js?v=11" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/md/findteam.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>

</body>
</html>
