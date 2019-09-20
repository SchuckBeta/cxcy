<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>

    <!-- 配置文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
    <script src="${ctxJs}/cmsIndexResourceForm.js" type="text/javascript"></script>

</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>内容管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <ul class="nav nav-tabs">
        <li><a href="${ctx}/cms/cmsIndexResource/">资源列表</a></li>
        <li class="active"><a
                href="${ctx}/cms/cmsIndexResource/form?id=${cmsIndexResource.id}">资源<shiro:hasPermission
                name="cms:cmsIndexResource:edit">${not empty cmsIndexResource.id?'修改':'添加'}</shiro:hasPermission>
            <shiro:lacksPermission name="cms:cmsIndexResource:edit">查看</shiro:lacksPermission></a></li>
    </ul>
    <form:form id="inputForm" modelAttribute="cmsIndexResource"
               action="${ctx}/cms/cmsIndexResource/save" method="post"
               class="form-horizontal">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <c:if test="${not empty cmsIndexResource.cmsIndexRegion.category.name}">
            <div class="control-group">
                <label class="control-label"><i>*</i>站点栏目：</label>
                <div class="controls">
                    <p class="control-static">${cmsIndexResource.cmsIndexRegion.category.name}</p>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty cmsIndexResource.id}">
            <div class="control-group htmlmodel" style="display: ${cmsIndexResource.resModel=='2' ? '' : 'none'}">
                <label class="control-label">页面链接：</label>
                <div class="controls">
                    <form:input path="" value="${ctxFront}/staticPage-${cmsIndexResource.id}" readonly="true" htmlEscape="false"
                                maxlength="64"
                                class="required"/>
                </div>
            </div>
        </c:if>
        <div class="control-group">
            <label class="control-label"><i>*</i>模式：</label>
            <div class="controls">
                <form:select path="resModel" class="required" onchange="onResModelChange()">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('region_model')}"
                                  itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><i>*</i>资源名称：</label>
            <div class="controls">
                <form:input path="resName" htmlEscape="false" maxlength="64"
                            class="required"/>
            </div>
        </div>
        <div class="control-group htmlmodel" style="display: ${((cmsIndexResource.resModel=='0') || (cmsIndexResource.resModel=='1') || (cmsIndexResource.resModel=='2')) ? '' : 'none'}">
            <label class="control-label"><i>*</i>页面Title：</label>
            <div class="controls">
                <form:input path="title" htmlEscape="false" maxlength="64"
                            class="required"/>
            </div>
        </div>
        <div class="control-group parammodel templemodel"
             style="display: ${cmsIndexResource.resModel=='2' ? 'none' : ''}">
            <label class="control-label"><i>*</i>栏目区域：</label>
            <div class="controls">
                <sys:treeselect id="cmsIndexRegion" name="cmsIndexRegion.id"
                                value="${cmsIndexResource.cmsIndexRegion.id}"
                                labelName="cmsIndexResource.cmsIndexRegion.regionName"
                                labelValue="${cmsIndexResource.cmsIndexRegion.regionName}"
                                title="栏目" url="/cms/cmsIndexRegion/treeData"
                                extId="${cmsIndexRegion.id}" cssClass="required"
                                cssStyle="width: 173px"
                                allowClear="false" notAllowSelectParent="true"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><i>*</i>状态：</label>
            <div class="controls">
                <form:select path="resState" class="required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('resstate_flag')}"
                                  itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <%-- <div class="control-group">
            <label class="control-label">排序：</label>
            <div class="controls">
                <form:input path="resSort" htmlEscape="false" maxlength="64"
                    class="required" />
            </div>
        </div> --%>

        <!-- 参数模式 -->
        <div class="parammodel" style="display: ${cmsIndexResource.resModel=='0' ? '' : 'none'}">
            <div class="control-group">
                <label class="control-label">按钮1名称：</label>
                <div class="controls">
                    <form:input path="botton1Name" htmlEscape="false" maxlength="64"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">资源地址1：</label>
                <div class="controls">
                    <div id="image-resUrl1"
                         style="width: 237px; height: 76px; background-color: #eee;"></div>
                    <form:input path="resUrl1" htmlEscape="false" maxlength="255"
                                class=""/>
                    <sys:fileUpload type="cmsIndexResource" postfix="1"
                                    seletorItem="#image-resUrl1" seletorHids="input[name=\'resUrl1\']"
                                    model="image"
                                    fileitem="${fns:ftpImgUrl(cmsIndexResource.resUrl1)}"
                                    isshow="true"/>
                    <span class="help-inline">建议上传资源大小请按照对应显示区域文件尺寸上传</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">跳转地址1：</label>
                <div class="controls">
                    <form:input path="jumpUrl1" htmlEscape="false" maxlength="255"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">按钮2名称：</label>
                <div class="controls">
                    <form:input path="botton2Name" htmlEscape="false" maxlength="64"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">资源地址2：</label>
                <div class="controls">
                    <div id="image-resUrl2"
                         style="width: 237px; height: 76px; background-color: #eee;"></div>
                    <form:input path="resUrl2" htmlEscape="false" maxlength="255"
                                class=""/>
                    <sys:fileUpload type="cmsIndexResource" postfix="2"
                                    seletorItem="#image-resUrl2" seletorHids="input[name=\'resUrl2\']"
                                    model="image"
                                    fileitem="${fns:ftpImgUrl(cmsIndexResource.resUrl2)}"
                                    isshow="true"/>
                    <span class="help-inline">建议上传资源大小请按照对应显示区域文件尺寸上传</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">跳转地址2：</label>
                <div class="controls">
                    <form:input path="jumpUrl2" htmlEscape="false" maxlength="255"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">按钮3名称：</label>
                <div class="controls">
                    <form:input path="botton3Name" htmlEscape="false" maxlength="64"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">资源地址3：</label>
                <div class="controls">
                    <div id="image-resUrl3"
                         style="width: 237px; height: 76px; background-color: #eee;"></div>
                    <form:input path="resUrl3" htmlEscape="false" maxlength="255"
                                class=""/>
                    <sys:fileUpload type="cmsIndexResource" postfix="3"
                                    seletorItem="#image-resUrl3" seletorHids="input[name=\'resUrl3\']"
                                    model="image"
                                    fileitem="${fns:ftpImgUrl(cmsIndexResource.resUrl3)}"
                                    isshow="true"/>
                    <span class="help-inline">建议上传资源大小请按照对应显示区域文件尺寸上传</span>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">跳转地址3：</label>
                <div class="controls">
                    <form:input path="jumpUrl3" htmlEscape="false" maxlength="255"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">预留字段1：</label>
                <div class="controls">
                    <form:input path="reserve1" htmlEscape="false" maxlength="255"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">预留字段2：</label>
                <div class="controls">
                    <form:input path="reserve2" htmlEscape="false" maxlength="255"
                                class=""/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">预留字段3：</label>
                <div class="controls">
                    <form:input path="reserve3" htmlEscape="false" maxlength="255"
                                class=""/>
                </div>
            </div>
        </div>
        <!-- 参数模式 -->
        <!-- 模板模式 -->
        <div class="control-group templemodel" style="display: ${cmsIndexResource.resModel=='1' ? '' : 'none'}">
            <label class="control-label"><i>*</i>模板：</label>
            <div class="controls">
                <select id="tplUrl" name="tplUrl" class="required" onchange="ontplUrlchange()">
                    <option jsonparam="" value="" selected="selected">--请选择--</option>
                    <c:forEach items="${fns:getTemplateList()}" var="item" varStatus="status">
                        <option jsonparam="${item.jsonparam}" value="${item.value}"
                                <c:if test="${cmsIndexResource.tplUrl==item.value}">selected="selected"</c:if> >${item.name}</option>
                    </c:forEach>
                </select>
            </div>
        </div>
        <div class="control-group templemodel" style="display: ${cmsIndexResource.resModel=='1' ? '' : 'none'}">
            <label class="control-label">参数示例：</label>
            <div class="controls">
                <form:textarea path="" id="templeParam" readonly="true" htmlEscape="false" rows="4"
                               style="width: 1006px; height: 327px;"/>
            </div>
        </div>
        <div class="control-group templemodel" style="display: ${cmsIndexResource.resModel=='1' ? '' : 'none'}">
            <label class="control-label">文件上传：</label>
            <div class="controls">
                <input type="file" style="display: none" id="fileToUpload" name="fileName"/>
                <form:input path="" id="image-upload-input"
                            style="width: 520px;display: ${cmsIndexResource.resModel=='1' ? '' : 'none'}"
                            readonly="true" htmlEscape="false" maxlength="255"
                            class="templemodel"/>
                <form:input path="" id="image-upload-input-ftp"
                            style="width: 520px;display: ${cmsIndexResource.resModel=='2' ? '' : 'none'}"
                            readonly="true" htmlEscape="false" maxlength="255"
                            class="htmlmodel"/>
                <a href="javascript:;" class="btn uploadFiles-btn" id="upload">上传文件</a>
                <span class="help-inline">上传文件后复制文件链接到下面，文件大小不超过100M</span>
            </div>
        </div>
        <div class="control-group templemodel" style="display: ${cmsIndexResource.resModel=='1' ? '' : 'none'}">
            <label class="control-label"><span class="help-inline"><font
                    color="red">*&nbsp;</font> </span>参数：</label>
            <div class="controls">
                <form:textarea path="tplJson" htmlEscape="false" rows="4" style="width: 1006px; height: 327px;"/>
            </div>
        </div>
        <!-- 模板模式 -->
        <!-- html模式 -->
        <div class="control-group htmlmodel" style="display: ${cmsIndexResource.resModel=='2' ? '' : 'none'}">
            <label class="control-label"><i>*</i>页面内容:</label>
            <div class="controls">
                <script id="container" name="content" type="text/plain" style="width:800px;height:600px">
        				${cmsIndexResource.content}




                </script>
            </div>
        </div>
        <!-- html模式 -->
        <div class="form-actions">
            <shiro:hasPermission name="cms:cmsIndexResource:edit">
                <button class="btn btn-primary" type="submit">保存</button>
            </shiro:hasPermission>
            <button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
        </div>
    </form:form>
</div>

</body>
</html>