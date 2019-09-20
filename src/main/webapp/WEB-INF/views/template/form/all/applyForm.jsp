<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
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
<div class="container">
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
                                <input type="hidden" name="declareId" id="declareId" value="${proModel.declareId}"/>
                                <input type="hidden" name="actYwId" id="actYwId" value="${proModel.actYwId}"/>
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
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="type" class="control-label"><i class="icon-require">*</i>项目类型：</label>
                            <div class="input-box">
                                <form:select id="type" path="type" required="required" class="form-control">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label for="type" class="control-label"><i class="icon-require">*</i>参赛组别：</label>
                            <div class="input-box">
                                <form:select path="level" required="required" class="form-control">
                                    <form:option value="" label="请选择"/>
                                    <form:options items="${fns:getDictList('gcontest_level')}" itemLabel="label"
                                                  itemValue="value" htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
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

                <div class="btn-tool-bar">
                    <a href="javascript:void(0)" class="btn btn-default" onClick="history.back(-1)">返回</a>
                    <a href="javascript:void(0)"  class="btn btn-primary-oe" onclick="proModel.submit();">提交并保存</a>
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
<script src="${ctxOther}/datepicker/My97DatePicker/WdatePicker.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/common.js" type="text/javascript"></script>
<script src="${ctxJs}/proModel.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
<script src="${ctxJs}/fileUpLoad.js"></script>
</body>
</html>