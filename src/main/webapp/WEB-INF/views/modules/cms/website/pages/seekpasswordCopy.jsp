<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<!--公用重置样式文件-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css" />
		<!--头部导航公用样式-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css" />
		<!--找回密码样式表-->
		<link rel="stylesheet" type="text/css" href="${ctxCss}/seekpassword.css"/>
		<meta name="decorator" content="site-decorator"/>
		<title>${frontTitle}</title>
	</head>
	<body>

		<div class="itemname">
			<div class="mid" style="font-size: 16px;">找回密码</div>
		</div>


		<div class="step1">

			<form id="fmone">
				<img class="procedure" src="${ctxImg}/seekpassprocedure_03.png" alt="" />
				<p><span>请填写您需要找回的账号</span></p>
				<font size="4" color="red"><span id="error1"></span></font>
				<p><input id="phone" class="account" type="text" name="phonemailxuehao" placeholder="请您输入手机号" /></p>
				<p><input id="valicodeyzm" class="fillyanzhengma" name="yanzhengma" type="text" placeholder="请输入验证码" /><small>	<span id="spanyzm"><img src="${ctxFront}/validateCode/createValidateCode" onclick="$('.validateCodeRefresh').click();" class="mid validateCode" style="width:102px;height:40px"></span>
		</small><a id="imgyzm"  class="validateCodeRefresh" onclick="$('.validateCode').attr('src','${ctxFront}/validateCode/createValidateCode?'+new Date().getTime());">换一张</a></p>
				<p><button type="button" id="procedure1btn">下一步</button></p>

			</form>
		</div>




		<div class="step2">
			<form id="fmtwo">
				<img class="procedure" src="${ctxImg}/seekpassprocedure2_03.png" alt="" />
				<p><small>为了你的账号安全，请完成身份验证</small></p>
				<p><big>手机号码验证</big></p>
				<p><strong>手机:<span id="pspan"></span></strong></p>
				<font size="4" color="red"><span id="phoneerror"></span></font>
				<font size="4" color="green"><span id="phoneyz"></span></font>
				<p><big class="wordyanzhengma" >验证码：</big></p>
				<p><input type="text" name="yanzhengma" id="wyzm" placeholder="短信验证码" /><a id="sendYzm" size="5" >获取验证码</a></p>
				<p><button type="button" id="procedure2btn">下一步</button></p>
			</form>
		</div>

		<div class="step3">
			<form id="fmthree">
				<img class="procedure" src="${ctxImg}/seekpassprocedure3_03.png" alt="" />
				<p><span></span><small>为了你的账号安全，请完成身份验证</small></p>
				<font size="4" color="red"><span id="passerror"></span></font>
				<font size="4" color="green"><span id="passmessage"></span></font>
				<p><span>新密码</span><input type="password" id="password" name="password" placeholder="" /></p>
				<p><span>确认新密码</span><input type="password" id="confirm_password" name="confirm_password" placeholder="" /></p>
				<p><span></span><button id="procedure3btn" type="button">确认</button></p>
			</form>
		</div>


		<div id="frontPath" hidden="true">${pageContext.request.contextPath}</div>

	<script src="${ctxCommon}/common-js/jquery.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxCommon}/common-js/messages_zh.min.js" type="text/javascript" charset="utf-8"></script>
	<script src="${ctxJs}/seekpassword.js" type="text/javascript" charset="utf-8"></script>
	</body>
</html>
