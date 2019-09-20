<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!-- 需要页面引入以下文件 -->
<%@ attribute name="tip1" type="java.lang.String" required="false"
              description="提示语一" %>
<%@ attribute name="tip2" type="java.lang.String" required="false"
              description="提示语一" %>
<%@ attribute name="className" type="java.lang.String" required="false"
              description="后台前台添加class名称前台accessories-h34, 后台accessories-h30" %>
<%@ attribute name="fileitems" type="java.util.List" required="false" description="文件列表" %>
<%@ attribute name="filepath" type="java.lang.String" required="false" description="文件目录" %>
<%@ attribute name="gnodeId" type="java.lang.String" required="false" description="流程节点" %>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否只读" %>
<%@ attribute name="fileTypeEnum" type="java.lang.String" required="false" description="附件所属分类" %>
<%@ attribute name="fileStepEnum" type="java.lang.String" required="false" description="附件所属子分类" %>
<%@ attribute name="fileInfoPrefix" type="java.lang.String" required="false" description="附件信息name属性值前缀" %>
<c:set var="idsuffix" value="${fns:getUuid()}"></c:set>
<div>
    <div id="${idsuffix}" class="accessories-container"
         style="display: <c:if test="${empty fileitems}">none</c:if>">
        <div class="accessories ${className}">
            <c:forEach items="${fileitems}" var="item" varStatus="status">
                <div class="accessory" data-file-id="${item.id}">
                    <div class="accessory-info">
                        <a class="accessory-file" title="<c:if test="${not empty item.viewUrl}">预览</c:if>${item.name}"
                           href="javascript: void(0);" data-id="${item.id}" data-view-url="${item.viewUrl}"
                           data-ftp-url="${item.url}" data-name="${item.name}" data-file-id="">
                            <img src="${ctxImg}/filetype/${item.imgType }.png">
                            <span class="accessory-name">${item.name}</span></a>
                        <i title="下载${item.name}" class="btn-downfile-newaccessory"></i>
                        <c:if test="${empty readonly or readonly==false}">
                            <i title="删除${item.name}" class="btn-delete-newaccessory"></i>
                        </c:if>
                    </div>
                    <div class="accessory-layer text-center" style="display: none;"><img
                            src="${ctxImages}/loading.gif"></div>
                    <input type='hidden' name='proModel.attachMentEntity.fielSize' value='${item.size}'/>
                    <input type='hidden' name='proModel.attachMentEntity.fielTitle' value='${item.name}'/>
                    <input type='hidden' name='proModel.attachMentEntity.fielType' value='${item.suffix}'/>
                    <input type='hidden' name='proModel.attachMentEntity.fielFtpUrl' value='${item.url}'/>
                </div>
            </c:forEach>
        </div>
    </div>
    <c:if test="${empty readonly or readonly==false}">
        <div class="btn-accessory-box <c:if test="${fn:contains(className, 30)}">btn-accessory-box-h30</c:if>
         <c:if test="${fn:contains(className, 34)}">btn-accessory-box-h34</c:if>">
            <div id="uploader${idsuffix}" class="upload-content"></div>
            <div class="uploader-tip">
                <c:if test="${empty tip1}">支持上传word、pdf、excel、txt、rar、ppt图片类型等文件，请到时间轴页面下载相关报告模板</c:if>
                    ${tip1}
            </div>
            <c:if test="${not empty tip2}">
                <div class="uploader-tip-other" style="display: block">${tip2}</div>
            </c:if>
        </div>
    </c:if>
</div>
<script>
    +function ($) {
        var uploaderFiles = JSON.parse('${fns: toJson(fileitems)}')
        var uploader = new Uploader(document.getElementById('uploader${idsuffix}'), {
            server: $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder=${filepath}',//上传地址
            inputValues: {
                fileStepEnum: '${fileStepEnum}',
                fileTypeEnum: '${fileTypeEnum}',
                gnodeId: '${gnodeId}',
                fileInfoPrefix: '${fileInfoPrefix}'
            },
            files: uploaderFiles != '' ? uploaderFiles : [],
            containerId: '#${idsuffix}',
            //上传之前
            beforeFileFn: function (file) {

            },
            //加入队列
            fileQueued: function (file) {
                $('button[data-control="uploader"]').prop('disabled', true);
            },
            //上传成功
            fileSuccess: function (file, respons) {

            },
            //上传完成
            fileCompleted: function (file) {
                $('button[data-control="uploader"]').prop('disabled', false);
            },
            //上传错误
            fileError: function (file, error) {

            },
            cancelFileFn: function () {
                $('button[data-control="uploader"]').prop('disabled', false);
            }
        })
    }(jQuery);
</script>
