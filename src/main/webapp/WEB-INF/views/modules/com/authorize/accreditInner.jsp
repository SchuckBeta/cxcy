<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>${backgroundTitle}</title>
	<%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60">
	<div class="mgb-20">
		<edit-bar></edit-bar>
	</div>
	<control-rule-block title="产品激活">
		<e-col-item label="1、" label-width="32px">首先<a class="link" href="${ctx}/authorize/donwLoadMachineInfo">导出硬件码文件</a>，并妥善保存（当前服务器数量：${count}）</e-col-item>
		<e-col-item label="2、" label-width="32px">将硬件ID文件提供给软件提供商或者访问<a  target="_blank" class="link" href="${getLicenseUrl}">官方授权申请网址</a>，申请授权文件。</e-col-item>
		<e-col-item label="3、" label-width="32px" style="margin-bottom: 0">点击下面按钮上传</e-col-item>
		<e-col-item label-width="32px">
			<el-upload
					action="${ctx}/authorize/uploadLicense"
					:show-file-list="false"
					:on-success="uploadLicenseSuccess"
					:on-error="uploadLicenseError"
					accept=".acvt"
					name="fileName"
					:file-list="fileList">
				<el-button size="mini" type="primary">授权</el-button>
			</el-upload>
		</e-col-item>
	</control-rule-block>
</div>

<script>

	'use strict';

	new Vue({
		el: '#app',
		data: function () {
			return {
				fileList: []
			}
		},
		methods:{
			uploadLicenseSuccess: function (response, file, fileList) {
				this.$message({
					type:response.ret == '1' ?  'success' : 'error',
					message:  response.msg
				})
			},
			uploadLicenseError: function (response, file, fileList) {
				this.$message({
					type: 'error',
					message:  response.msg
				})
			}
		}
	})

</script>
</body>
</html>