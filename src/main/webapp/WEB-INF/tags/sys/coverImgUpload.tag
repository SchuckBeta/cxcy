<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- 需要页面引入以下文件 -->
<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
<link rel="stylesheet" type="text/css"
	href="${ctxCss}/upload/frontCreditModule.css">
<%@ attribute name="btnid" type="java.lang.String" required="true"
	description="上传按钮"%>
<%@ attribute name="filepath" type="java.lang.String" required="true"
	description="文件目录"%>
<%@ attribute name="coverimgshow" type="java.lang.String" required="true"
	description="需要显示图片的元素id"%>
<c:set var="idsuffix" value="${fns:getUuid()}"></c:set>
<span id="fileparam" style="display:none"></span>
<script>
var filepath="uploader";
var BASE_URL = '/static/webuploader';
var $loading;
var uploader;
if("${filepath}"){
	filepath="${filepath}";
}
$(function() {
	if('${btnid}'!=""){
		uploader = WebUploader.create({
			auto : true,
			swf : BASE_URL + '/Uploader.swf',
			server : $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder='+filepath, //文件上传地址 folder表示功能模块
			pick : '#${btnid}',
			fileVal : 'upfile',
			accept: {// 只允许选择图片文件格式
		         title: 'Images',
		         extensions: 'gif,jpg,bmp,png,x-png,jpeg',
		         mimeTypes: 'image/*'
		         }
		});
	}else{
		uploader = WebUploader.create({
			auto : true,
			swf : BASE_URL + '/Uploader.swf',
			server : $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder='+filepath, //文件上传地址 folder表示功能模块
			fileVal : 'upfile'
		});
	}
	uploader
			.on(
					'fileQueued',
					function(file) {
						$loading = $("#${btnid}").next('span.loading');
						if(!$loading||$loading.length==0){
							$("#${btnid}").after('<span class="loading"><img src="${ctxImages}/loading.gif"></span>');
							$loading = $("#${btnid}").next('span.loading');
						}
						$loading.css('display', '');
					});

	uploader
			.on(
					'uploadSuccess',
					function(file, response) {
						$("#fileparam").html("");
						$("#${coverimgshow}").attr("src",response.url);
						$("#fileparam")
								.append(
										"<input type='hidden' name='attachMentEntity.fielSize' value='"+response.size+"'/>");
						$("#fileparam")
								.append(
										"<input type='hidden' name='attachMentEntity.fielTitle' value='"+response.title+"'/>");
						$("#fileparam")
								.append(
										"<input type='hidden' name='attachMentEntity.fielType' value='"+response.type+"'/>");
						$("#fileparam")
								.append(
										"<input type='hidden' name='attachMentEntity.fielFtpUrl' value='"+response.ftpUrl+"'/>");
						$loading.hide();
					});

	uploader.on('uploadError', function(file) {
		$loading.hide();
		showModalMessage(0, "上传失败(文件超过100M或者服务器异常)", {
			'确定' : function() {
				$(this).dialog("close");
			}
		});
	});
});
</script>