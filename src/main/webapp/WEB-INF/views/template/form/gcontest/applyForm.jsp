<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">

    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="${ctxCss}/GCSB.css"/>--%>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/gContestForm.css?v=1">
    <%--<link rel="stylesheet" type="text/css" href="${ctxCss}/competitionRegistration.css"/>--%>
    <%--<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>--%>
    <%--<link rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">--%>
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <style>
        .competition-title {
            width: 100%;
            height: 40px;
            line-height: 40px;
            color: #656565;
            background-color: #f4e6d4;
            font-size: 16px;
            font-weight: bold;
            box-sizing: border-box;
            -moz-box-sizing: border-box;
            -webkit-box-sizing: border-box;
            padding-left: 10px;
        }
    </style>
</head>
<body>
<div class="container container-fluid-oe">
    <input type="hidden" id="pageType" value="edit">
    <h2 class="text-center" style="margin-top: 0; margin-bottom: 30px;">${projectName}申请表</h2>
    <form:form id="competitionfm" class="form-horizontal" modelAttribute="proModel" action="${ctxFront}/promodel/proModel/submit"
               method="post" enctype="multipart/form-data">
        <input type="hidden" name="proMark" value="${proModel.proMark}"/>
        <div class="contest-content">
            <div class="tool-bar">
                <a class="btn-print" onClick="window.print()" href="javascript:void(0);">打印申报表</a>
                <div class="inner">
                    <c:if test="${id!=null}">
                        <span>大赛编号：</span>
                        <i>${competitionNumber}</i>
                    </c:if>
                    <input type="hidden" name="competitionNumber" value="${competitionNumber}"/>
                    <span>填表日期:</span>
                    <i>${sysdate}</i>
                </div>
            </div>
            <h4 class="contest-title">大赛报名</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="shenbaoren" class="control-label"><i class="icon-require">*</i>申报人：</label>
                            <div class="input-box">
                                <input type="text" id="shenbaoren" class="form-control" name="shenbaoren" readonly
                                       value="${sse.name}">
                                <input type="hidden" name="declareId" id="declareId" value="${sse.id}"/>
                                <input type="hidden" name="actYwId" id="actYwId" value="${actYw.id}"/>
                                <input type="hidden" name="id" id="id" value="${proModel.id}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="company" class="control-label"><i class="icon-require">*</i>学院：</label>
                            <div class="input-box">
                                <input type="text" id="company" class="form-control" readonly
                                       value="${sse.office.name}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="zynj" class="control-label"><i class="icon-require">*</i>专业年级：</label>
                            <div class="input-box">
                                <input type="text" id="zynj" name="zynj" class="form-control" readonly
                                       value="${fns:getProfessional(sse.professional)}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="mobile" class="control-label"><i class="icon-require">*</i>联系电话：</label>
                            <div class="input-box">
                                <input type="text" id="mobile" class="form-control" readonly value="${sse.mobile}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="email" class="control-label"><i class="icon-require">*</i>E-mail：</label>
                            <div class="input-box">
                                <input type="text" id="email" class="form-control" readonly value="${sse.email}"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>大赛基本信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="projectName" class="control-label"><i class="icon-require">*</i>参赛项目名称：</label>
                            <div class="input-box">
                                <input type="text" name="pName" id="projectName" class="form-control" maxlength='30'
                                       value="${gContest.pName}"/>
                            </div>
                        </div>
                    </div>
                    <c:if test="${not empty proCategoryMap}">
                        <div class="col-md-6">
                            <div class="form-group">
                                    <%--<label for="type" class="control-label"><i class="icon-require">*</i>项目类型：</label>
                                    <div class="input-box">
                                        <form:select id="type" path="type" required="required" class="form-control">
                                            <form:option value="" label="请选择"/>
                                            <form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label"
                                                          itemValue="value" htmlEscape="false"/>
                                        </form:select>
                                    </div>--%>
                                <flow:flowTypeSelect name="大赛类别" divClass="input-box" type="proCategory"
                                                     typeList="${proCategoryMap}"></flow:flowTypeSelect>
                            </div>
                        </div>
                    </c:if>
                </div>
                <div class="row">
                    <c:if test="${not empty prolevelMap}">
                        <div class="col-md-6">
                            <div class="form-group">
                                <flow:flowTypeSelect name="参赛级别" divClass="input-box" type="level"
                                                     typeList="${prolevelMap}"></flow:flowTypeSelect>
                            </div>
                        </div>
                    </c:if>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="financingStat" class="control-label"><i class="icon-require">*</i>融资情况：</label>
                            <div class="input-box">
                                <form:select id="financingStat" path="financingStat" required="required"
                                             class="form-control">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('financing_stat')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
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
                        <label class="control-label"><i class="icon-require">*</i>团队信息：</label>
                        <div class="input-box" style="max-width: 394px;">
                            <form:select required="required" onchange="findTeamPerson()" path="teamId"
                                         class="form-control"><form:option value="" label="--请选择--"/>
                                <form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
                </div>
                <table class="table table-bordered table-team studenttb">
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
                        <%--<th class='credit-ratio'>学分配比</th>--%>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${gContestVo.teamStudent!=null&&gContestVo.teamStudent.size() > 0}">
                        <c:forEach items="${gContestVo.teamStudent}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                                <td><c:out value="${item.professional}"/></td>
                                <td><c:out value="${item.domain}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.instudy}"/></td>
                                <input type="hidden" name="teamUserHistoryList[${status.index}].id"
                                                                          value="${item.id}">
                                <%--<td class="credit-ratio">--%>

                                    <%--<input class="form-control input-sm "--%>
                                           <%--name="teamUserHistoryList[${status.index}].weightVal"--%>
                                           <%--value="${item.weightVal}">--%>
                                <%--</td>--%>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-team teachertb">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>单位（学院或企业、机构）</th>
                        <th>职称（职务）</th>
                        <th>技术领域</th>
                        <th>联系电话</th>
                        <th>E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${gContestVo.teamTeacher!=null&&gContestVo.teamTeacher.size() > 0}">
                        <c:forEach items="${gContestVo.teamTeacher}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
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
                    <label for="introduction" class="control-label"><i class="icon-require">*</i>项目介绍：</label>
                    <div class="input-box">
                        <textarea id="introduction" class="introarea form-control" rows="5" name="introduction"
                                  maxlength='1024'>${gContest.introduction}</textarea>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>附     件</span>
                        <i class="line"></i>
                    </div>
                </div>
                <c:if test="${gnodeId!=null && gnodeId!=''}">
                    <input type="hidden" name="gnodeId" value="${gnodeId}"/>
                </c:if>
                <div id="fujian" class="accessory-box">
                    <sys:frontFileUploadCommon fileitems="${sysAttachments}"
                                               filepath="promodel"
                                               btnid="btnUpload" readonly="true"></sys:frontFileUploadCommon>
                </div>

                <div class="btn-tool-bar">
                    <a href="javascript:void(0)" class="btn btn-default" onClick="history.back(-1)">返回</a>
                    <a href="javascript:void(0)" class="btn btn-primary-oe" onclick="proModel.submit(this);">提交并保存</a>
                    <div class="btn-upload-file" id="btnUpload" style="vertical-align: top">上传附件</div>
                </div>
            </div>
        </div>
    </form:form>
</div>
<div id="dialog-message" title="信息">
    <p id="dialog-content">
    </p>
</div>

<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/messages_zh.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/DSSBvalidate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/findteam.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxOther}/datepicker/My97DatePicker/WdatePicker.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/common.js" type="text/javascript"></script>
<script src="${ctxJs}/proModel.js" type="text/javascript" charset="utf-8"></script>
<%--<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>--%>
<%--<script src="${ctxJs}/fileUpLoad.js"></script>--%>
<script>
    $(function(){
        findTeamPerson();
        onGcontestApplyInit($("[id='actYwId']").val(),$("[id='id']").val(),checkOnInit);
    });
    function checkOnInit(){
    	//TODO
    }
</script>
</body>
</html>