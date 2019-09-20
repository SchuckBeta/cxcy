<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- 需要页面引入以下文件 -->
<script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
<link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
<link rel="stylesheet" type="text/css" href="${ctxCss}/webuploadfile.css">
<%@ attribute name="btnid" type="java.lang.String" required="false" description="上传按钮"%>
<%@ attribute name="fileitems" type="java.util.List" required="true" description="文件列表"%>
<%@ attribute name="filepath" type="java.lang.String" required="false" description="文件目录"%>
<%@ attribute name="gnodeId" type="java.lang.String" required="false" description="流程节点"%>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否只读"%>
<%@ attribute name="fileTypeEnum" type="java.lang.String" required="false" description="附件所属分类"%>
<%@ attribute name="fileStepEnum" type="java.lang.String" required="false" description="附件所属子分类"%>
<%@ attribute name="fileInfoPrefix" type="java.lang.String" required="false" description="附件信息name属性值前缀"%>
<c:set var="idsuffix" value="${fns:getUuid()}"></c:set>
<div class="other" id="fujian_${idsuffix}">
	<ul id="file_${idsuffix}" class="fileul">
		<c:forEach items="${fileitems}" var="item" varStatus="status">
			<li class="file-item">
				<div class="file-list">
					<a onclick="downFile(this)" data-id="${item.id}"
						data-ftp-url="${item.url}" data-title="${item.name}"
						href="javascript:void(0);"> <img class="pic-icon"
						src="${ctxImg}/filetype/${item.imgType }.png">${item.name}
					</a>
					<c:if test="${empty readonly or readonly==false }">
						<i class="icon icon-remove-sign"></i>
					</c:if>
				</div>
			</li>
		</c:forEach>
	</ul>
</div>
<script>
var filepath="uploader";
var BASE_URL = '/static/webuploader';
var $loading;
if("${filepath}"){
	filepath="${filepath}";
}
$(function() {
	if(typeof(uploaderArr)=="undefined"){
		uploaderArr={};
	}
	var uploader;
	if('${btnid}'!=""){
		uploader = WebUploader.create({
			auto : true,
			swf : BASE_URL + '/Uploader.swf',
			server : $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder='+filepath, //文件上传地址 folder表示功能模块
			pick : '#${btnid}',
			fileVal : 'upfile'
		});
	}else{
		uploader = WebUploader.create({
			auto : true,
			swf : BASE_URL + '/Uploader.swf',
			server : $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder='+filepath, //文件上传地址 folder表示功能模块
			fileVal : 'upfile'
		});
	}
	uploaderArr['${idsuffix}']=uploader;
	uploaderArr['${idsuffix}'].on(
					'fileQueued',
					function(file) {
						var $li = $('<li id="' + file.id + '" class="file-item">'
								+ '<div class="file-list"><a href="javascript:void(0);"><img class="pic-icon" src="${ctxImg}/filetype/'
								+ switchImgType(file.ext)
								+ '.png">'
								+ file.name
								+ '</a><i class="icon icon-remove-sign"></i></div>'
								+ '<p class="loading"><img src="${ctxImages}/loading.gif"></p></li>');
						$loading = $li.find('p.loading');
						$('#file_${idsuffix}').append($li);
						$loading.css('display', 'block');
					});

	uploaderArr['${idsuffix}'].on(
					'uploadSuccess',
					function(file, response) {
						var $li = $('#' + file.id);
						var $a = $li.find('a');
						$a.attr({
							'data-original' : response.original,
							'data-size' : response.size,
							'data-title' : response.title,
							'data-type' : response.type,
							'data-ftp-url' : response.ftpUrl
						}).attr('onclick', 'downFile(this)');
						var $li = $('#' + file.id), $loading = $li
								.find('p.loading');
						$("#fujian_${idsuffix}")
								.append(
										"<span id=fileparam"+file.id+"></span>");
						$("#fileparam" + file.id)
								.append(
										"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.fielSize' value='"+response.size+"'/>");
						$("#fileparam" + file.id)
								.append(
										"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.fielTitle' value='"+response.title+"'/>");
						$("#fileparam" + file.id)
								.append(
										"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.fielType' value='"+response.type+"'/>");
						$("#fileparam" + file.id)
								.append(
										"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.fielFtpUrl' value='"+response.ftpUrl+"'/>");
						$("#fileparam" + file.id).append(
								"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.fileTypeEnum' value='${fileTypeEnum}'/>");
						$("#fileparam" + file.id).append(
								"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.fileStepEnum' value='${fileStepEnum}'/>");
						if("${filepath}"){
							$("#fileparam" + file.id).append(
								"<input type='hidden' name='${fileInfoPrefix}attachMentEntity.gnodeId' value='${gnodeId}'/>");
						}
						$loading.hide()
					});

	uploaderArr['${idsuffix}'].on('uploadError', function(file) {
		var $li = $('#' + file.id);
		$li.remove();
		$("#fileparam" + file.id).remove();
		showModalMessage(0, "上传失败(文件超过100M或者服务器异常)", {
			'确定' : function() {
				$(this).dialog("close");
			}
		});
	});
});

function switchImgType(imgType) {
	var extname;
	switch (imgType) {
	case "xls":
	case "xlsx":
		extname = "excel";
		break;
	case "doc":
	case "docx":
		extname = "word";
		break;
	case "ppt":
	case "pptx":
		extname = "ppt";
		break;
	case "jpg":
	case "jpeg":
	case "gif":
	case "png":
	case "bmp":
		extname = "image";
		break;
	case 'txt':
		extname = 'txt';
		break;
	case 'zip':
		extname = 'zip';
		break;
	case 'rar':
		extname = 'rar';
		break;
	default:
		extname = "unknow";
	}
	return extname;
}

$(document).on('click','#file_${idsuffix}>li>div>i.icon-remove-sign', function() {
	var btn = $(this);
	confirmx("确定删除?", function(){
		//向服务器发出请求，删除文件
		var url = btn.prev().attr("data-ftp-url");
		var id = btn.prev().attr("data-id");
		if (!id) {
			id = "";
		}
		$.ajax({
			type : 'post',
			url : $frontOrAdmin+'/ftp/ueditorUpload/delFile',
			data : {
				url : url,
				id : id
			},
			success : function(data) {
				$("#fileparam" + btn.parents('li').attr("id")).remove();
				btn.parents('li').detach();
				uploaderArr['${idsuffix}'].reset();
			}
		});
	});
});
function downFile(obj) {
	var url = $(obj).attr("data-ftp-url");
	var fileName = $(obj).attr("data-title");
	location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName="
			+ encodeURI(encodeURI(fileName));
}
</script>