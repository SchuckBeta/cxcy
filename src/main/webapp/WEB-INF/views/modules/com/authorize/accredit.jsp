<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>授权${backgroundTitle}</title>
	<%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60">
	<div class="sys-top-header" :style="style"></div>
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
					:before-upload="beforeUpload"
					:disabled="uploading"
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
				fileList: [],
				uploading: false
			}
		},
		computed: {
			backgroundImage: function () {
				if(this.isSchoolPlatform){
					return '/images/managetoplogo_01.png'
				}else if(this.isCenterPlatform){
					return '/images/managetoplogo_yunying.jpg'
				}else {
					return '/images/province-top-bar-bg.png'
				}
			},
			style: function () {
				var style =  {
					width: 'auto',
					position: 'relative',
					margin: '0 -20px 20px -20px',
					backgroundImage: 'url('+ this.backgroundImage +')'
				};
				if(this.isProvincePlatform){
					style.backgroundColor = 'transparent';
				}
				return style
			}
		},
		methods:{

			beforeUpload: function(){
				this.uploading = true;
				return true;
			},

			uploadLicenseSuccess: function (response, file, fileList) {
				var data = response;
				if(data.ret){
					if(data.ret === '1'){
						this.$alert(data.msg, '提示', {
							type: 'success',
							showClose: false,
							confirmButtonText: '首页'
						}).then(function () {
							location.href = '${ctx}';
						})
					}else {
						this.$alert(data.msg, '提示', {
							type: 'error',
						})
						this.uploading = false;
					}
				}

			},
			uploadLicenseError: function (response, file, fileList) {
				this.$alert(response.msg, '提示', {
					type: 'error',
				})
				this.uploading = false;
			}
		}
	})

</script>
</body>
</html>