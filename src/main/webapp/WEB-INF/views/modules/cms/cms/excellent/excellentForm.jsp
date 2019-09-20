<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title></title>
    	<script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <meta charset="UTF-8">
    <%@ include file="/WEB-INF/views/include/backtable.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link href="${ctxCss}/excellent/lessonProjectEditCommon.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/state/titlebar.css">
    <!-- 配置文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>

</head>

<body>
<input type="hidden" id="front_url" value="${front_url}"/>
<div class="container-fluid lp-edit-container">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>展示</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="projectEditForm" modelAttribute="es" save="${ctx}/excellent/save" method="post">
        <input type="hidden" name="id" value="${es.id }"/>
        <input type="hidden" name="foreignId" value="${es.foreignId }"/>
        <input type="hidden" name="type" value="${es.type }"/>
        <input type="hidden" name="subType" value="${es.subType }"/>
        <input type="hidden" name="managed" value="${es.managed }"/>
        <input type="hidden" name="isRelease" value="1"/>
        <div class="title-box">
            <h2 class="title">${projectInfo.name }</h2>
           <c:if test="${es.type=='0000000075' }">
    		<span>项目来源:${fns:getDictLabel(es.subType, 'project_style', '')}</span>
    	</c:if>
        <c:if test="${es.type=='0000000076' }">
    		<span>项目来源:${fns:getDictLabel(es.subType, 'competition_type', '')}</span>
    	</c:if>
        </div>
        <div class="row form-row"
             style="margin-left: -15px!important;margin-right: -15px!important;margin-bottom: 15px!important;">
            <div class="col-sm-6" style="max-width: 635px">
                <div class="preview preview-default">
                    <img id="coverImg_show"
                         src="${not empty es.coverImg ? fns:ftpImgUrl(es.coverImg) : '/img/video-default.jpg'}">
                </div>
                <input type="hidden" id="coverImg" name="coverImg" value="${es.coverImg}"/>
                <div class="btns-upload">
                    <a id="upload" type="button" class="btn btn-primary-oe btn-sm">上传封面</a>
                    <sys:coverImgUpload filepath="excellentCoverImg" coverimgshow="coverImg_show" btnid="upload"></sys:coverImgUpload>
                </div>
            </div>
            <div class="col-sm-6">
                <div class="row row-project-info" style="margin:0 -15px 15px !important;">
                    <div class="col-md-2 col-w92">学院：</div>
                    <div class="col-md-9">
                            ${projectInfo.oname }
                    </div>
                </div>
                <div class="row row-project-info" style="margin:0 -15px 15px !important;">
                    <div class="col-md-2 col-w92">指导老师：</div>
                    <div class="col-md-6">
                        <ul class="teachers">
                            <c:forEach items="${projectTeacherInfo}" var="item">
                                <li>
                                    <span>${item.uname }</span><span>${item.oname }</span><span>${item.post_title }</span>
                                </li>
                            </c:forEach>
                        </ul>
                    </div>
                </div>
                <div class="form-horizontal form-keyword" style="margin:0 0 8px;">
                    <div class="form-group">
                        <div class="col-md-2 col-w92 control-label">关键词：</div>
                        <div class="col-md-5">
                            <input type="text" class="form-control form-keyword" id="formKeyword" autocomplete="off" style="height: 30px;"
                                   placeholder="输入关键字按回车键添加">
                        </div>
                        <div class="col-md-3 col-error">
                        </div>
                    </div>
                </div>
                <div class="row row-keyword" style="margin:0 -15px 15px !important;">
                    <div class="col-md-2 col-w92"></div>
                    <div class="col-md-9 col-offset-md-2 col-offset-w92 col-keyword-box">
                        <c:forEach items="${es.keywords}" var="item">
                    		<span class="keyword">
                    			<input name="keywords" value="${item}" type="hidden"/>
	                    		<span>${item}</span>
	                    		<a class="delete-keyword" href="javascript:void(0);" onclick="delKey(this);">×</a>
                    		</span>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-horizontal form-bottom">
            <div class="form-group">
                <label class="control-label col-md-2">页面模板：</label>
                <div class="col-md-4">
                    <select id="temp" class="form-control form-template" onchange="ontempchange()">
                        <option tempcontent="" value="" selected="selected">--选择布局模板--</option>
                        <c:forEach items="${fns:getExcTemplateList()}" var="item" varStatus="status">
                            <option tempcontent="${item.content}" value="${item.value}">${item.name}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-4 col-error">

                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-md-2"><i class="icon-require">*</i>页面内容：</label>
                <div class="col-md-8">
                    <script id="container" name="content" type="text/plain" style="width:800px;height:600px">
        					${es.content}
                    </script>
                </div>
            </div>
        </div>
        <div class="form-inline form-inline-publish">
            <div class="form-group">
                <label class="control-label">是否置顶：</label>
                <form:select path="isTop"
                             class="form-control">
                    <form:options items="${fns:getDictList('yes_no')}" itemValue="value" itemLabel="label"
                                  htmlEscape="false"/></form:select>
            </div>
            <div class="form-group">
                <label class="control-label">可否评论：</label>
                <form:select path="isComment"
                             class="form-control">
                    <form:options items="${fns:getDictList('yes_no')}" itemValue="value" itemLabel="label"
                                  htmlEscape="false"/></form:select>
            </div>
        </div>
        <div class="row" style="margin:0 -15px 15px !important;">
            <div class="col-md-8 text-center">
                <button type="button" class="btn btn-primary-oe" onclick="preview()">预览</button>
                <button id="btnSave" type="button" class="btn btn-primary-oe">发布</button>
                <button type="button" class="btn btn-primary-oe" onclick="history.go(-1)">返回</button>
            </div>
        </div>
    </form:form>
</div>

<script src="${ctxJs}/excellent/lessonProjectEditCommon.js" type="text/javascript"></script>

</body>