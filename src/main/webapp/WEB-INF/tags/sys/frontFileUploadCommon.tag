<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!-- 需要页面引入以下文件 -->
<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
<link rel="stylesheet" type="text/css" href="${ctxCss}/webuploadfile.css">
<script type="text/javascript" src="${ctxJs}/sco/upload-accessoryStep.js"></script>

<%@ attribute name="btnid" type="java.lang.String" required="false" description="上传按钮" %>
<%@ attribute name="fileitems" type="java.util.List" required="true" description="文件列表" %>
<%@ attribute name="filepath" type="java.lang.String" required="false" description="文件目录" %>
<%@ attribute name="gnodeId" type="java.lang.String" required="false" description="流程节点" %>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否只读" %>
<c:set var="idsuffix" value="${fns:getUuid()}"></c:set>
<div class="other" id="fujian_${idsuffix}">
    <ul id="file_${idsuffix}" class="fileul">
        <c:forEach items="${fileitems}" var="item" varStatus="status">
            <li class="file-item">
                <div class="file-info">
                    <a class="file" data-id="${item.id}"
                       data-ftp-url="${item.url}" data-title="${item.name}"
                       href="javascript:void(0);"> <img class="pic-icon"
                                                        src="${ctxImg}/filetype/${item.imgType }.png">${item.name}
                    </a>
                    <c:if test="${empty readonly or readonly==false }">
                        <i class="icon icon-remove-sign"></i>
                    </c:if>
                    <c:if test="${not empty item.viewUrl }">
                    	<a href="${item.viewUrl }" target="_blank">预览</a>
                    </c:if>
                </div>
            </li>
        </c:forEach>
    </ul>
</div>
<script>
    $(function () {
        var containerId = '#file_${idsuffix}';
        var $btnPick = $('#${btnid}');
        var filePath = "${filepath}";
        var filePathName = filePath || 'uploader';
        var gnodeId = "${gnodeId}";

        $btnPick.uploadAccessory({
            isPreView: false,
            serverUrl: $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder=' + filePathName,
            pick: '#${btnid}',
            acceptErrorMsg: '文件是否损坏',
            isFormData: true,
            containerImgId: containerId,
            containerOtherId: containerId,
            isImagePreview: false,
            accept: {}
        });
        $btnPick.on('uploadSuccess', function (e) {
            var file = e.file;
            var id = file.id;
            var $li = $('#' + id);
            var response = e.response;
            var inputHtml = tplInputs(response, id);
            $li.append(inputHtml);
        });

        function tplInputs(response, id) {
            var input = '';
            if (response) {
                input += '<input type="hidden" name="attachMentEntity.fielSize" value=' + response.size + '>' +
                        '<input type="hidden" name="attachMentEntity.fielTitle" value=' + encodeURI(encodeURI(response.title)) + '>' +
                        '<input type="hidden" name="attachMentEntity.fielType" value=' + response.type + '>' +
                        '<input type="hidden" name="attachMentEntity.fielFtpUrl" value=' + response.ftpUrl + '>';
                if (filePath) {
                    input += '<input type="hidden" name="attachMentEntity.gnodeId" value=' + gnodeId + '>';
                }
                return '<span class="fileparamspan" id=fileparam' + id + '>' + input + '</span>';
            }
            return input;
        }

        $(containerId).on('click', 'a.file', function (e) {
            var url = $(this).attr("data-ftp-url");
            var fileName = $(this).attr("data-title");
            location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName="
                    + encodeURI(encodeURI(fileName));
        });
    });

</script>