<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>授权</title>
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/bootstrap/2.3.1/css_${not empty cookie.theme.value ? cookie.theme.value : 'cerulean'}/bootstrap.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/bootstrap/2.3.1/js/bootstrap.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css" type="text/css" rel="stylesheet" />
	<link href="${ctxStatic}/jquery-select2/3.4/select2.min.css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-select2/3.4/select2.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />
	<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
	<script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
	<link href="${ctxStatic}/common/initiate.css" type="text/css" rel="stylesheet" />
	<script src="${ctxStatic}/common/initiate.js" type="text/javascript"></script>
	<script type="text/javascript">var ctx = '${ctx}', ctxStatic='${ctxStatic}';</script>
	<!--公用重置样式文件-->
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>
	<!--头部样式文件公共部分-->
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header2.css"/>
	<!--创新创业云服务平台管理一级页面样式表-->
	<link rel="stylesheet" type="text/css" href="${ctxCss}/managePlatFormLeverOne.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>
	<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxJs}/common.js" type="text/javascript"></script>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/common.css" />
	<link rel="stylesheet" type="text/css" href="${ctxCss}/accredit.css">
	<link rel="stylesheet" type="text/css" href="${ctxCss}/about.css">
	<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
	<script src="${ctxJs}/accredit.js" type="text/javascript"></script>
	<style type="text/css">
		.top {
			height: 70px;
		}
		.card{
			position: relative;
			padding: 40px 30px;
			border: 1px solid #ddd;
			max-width: 500px;
			margin-bottom: 40px;
		}
		.card .card-title{
			display: block;
			position: absolute;
			left: 20px;
			top:  -10px;
			line-height: 20px;
			height: 20px;
			padding: 0 10px;
			font-size: 14px;
			font-weight: bold;
			color: #333;
			background-color: #fff;
		}

		.card .link{
			color: rgb(0,0,230);
			text-decoration: underline;
		}

		.card .license-file{
			color: #e04527;
		}
		.edit-bar {
			margin-bottom: 15px;
		}

		.edit-bar-left {
			position: relative;
		}

		.edit-bar-left > span {
			position: relative;
			padding: 0 15px 0 0;
			color: #e9432d;
			font-size: 16px;
			font-weight: 700;
			line-height: 2.5;
			background: #fff;
			z-index: 1;
		}

		.edit-bar-left .line {
			display: block;
			position: absolute;
			left: 0;
			top: 50%;
			right: 0;
			border-top: 1px solid #f4e6d4;
		}
		.edit-bar .edit-bar-left .weight-line{
			border-top: 3px solid #f4e6d4;
		}
		.edit-bar-sm .edit-bar-left > span {
			color: #000;
			font-size: 16px;
			font-weight: normal;
		}
		.edit-bar-tag .edit-bar-left>span{
		}
		.edit-bar .unread-tag{
			display: inline-block;
			min-width: 10px;
			padding: 3px 7px;
			margin-left: 8px;
			line-height: 1;
			text-align: center;
			border-radius: 10px;
			font-size: 12px;
			font-weight: normal;
			font-style: normal;
			background-color: #e9432d;
			color: #fff;
			vertical-align: middle;
			margin-top: -3px;
		}
	</style>
</head>
<body>
<div class="top">
	<ul>
	</ul>
</div>
<div id="dialog-message" title="信息" style="display: none;">
	<p id="dialog-content"></p>
</div>
<div class="container-fluid">
	<div class="content-wrap">
		<div class="edit-bar clearfix">
			<div class="edit-bar-left">
				<span>产品激活</span>
				<i class="line weight-line"></i>
			</div>
		</div>
		<div class="card">
			<span class="card-title">激活步骤</span>
			<p>
				1、首先<a class="link" href="${ctx}/authorize/donwLoadMachineInfo">导出硬件码文件</a>，并妥善保存（当前服务器数量：${count}）
			</p>
			<p>
				2、将硬件ID文件提供给软件提供商或者访问<a  target="_blank" class="link" href="${getLicenseUrl}">官方授权申请网址</a>，申请授权文件。
			</p>
			<p>
				3、点击下面按钮上传<span class="license-file">license.key</span>
				<section class="fj-uploadFile">
					<a href="javascript:;"  class="btn uploadFiles-btn" id="upload">授权</a>
					<input type="file"  style="display: none" id="fileToUpload" name="fileName" accept=".acvt" />
				</section>
			</p>
		</div>
		<%--<div class="footerBox" style="height: 40px; line-height:40px; text-align:center;  background-color: rgb(255, 255, 255); position: fixed; bottom: 0px; width: 100%; border-top: 1px solid #ddd;">--%>
		<%--<p class="copyright" style="margin:0px 10px;">--%>
		<%--<img src="${ctxImages}/net.png"><span>公司名称：武汉噢易云计算股份有限公司</span>--%>
		<%--<img src="${ctxImages}/net.png"><span>官方网址：www.os-easy.com</span>--%>
		<%--<img src="${ctxImages}/net.png"><span>官方网址400服务电话：4001-027-580</span>--%>
		<%--</p>--%>
		<%--</div>--%>
	</div>
</div>
</body>
</html>