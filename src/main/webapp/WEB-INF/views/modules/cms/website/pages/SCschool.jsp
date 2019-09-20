<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<html>

<head>
<meta charset="utf-8" />
<!--公用重置样式文件-->
<link rel="stylesheet" type="text/css"
	href="${ctxCommon}/common-css/bootstrap.min.css" />
<meta name="decorator" content="site-decorator" />
<title>${frontTitle}</title>
<style type="text/css">
.introBox {
	width: 758px;
	overflow: hidden;
	margin: 0 auto;
	margin-top: 100px;
	text-align: center;
	padding: 0px 70px;
}

.introBox h1 {
	font-weight: normal;
	font-size: 42px;
	color: #4b4b4b;
}

.introBox .line {
	width: 100%;
	height: 1px;
	background: #9c9c9c;
	margin-top: 45px;
}

.introBox span {
	display: inline-block;
	width: 165px;
	font-size: 55px;
	color: #df4526;
	background: white;
	z-index: 1;
	margin-top: -40px;
}

.introBox p {
	width: 100%;
	overflow: hidden;
	color: #4b4b4b;
	line-height: 30px;
	font-size: 14px;
	margin-top: 50px;
}
</style>
</head>
<body>
	<div class="introBox">
		<h1>华中师范大学教务处创新创业教育办公室</h1>
		<div class="line"></div>
		<span>简介</span>
		<p>
			华中师范大学教务处创新创业教育办公室是我校大学生创新创业教育的管理与服务部门，负责创新创业教育校本课程体系建设、创新创业师资队伍建设、大学生创新创业实习实践基地建设、全国大学生创新创业训练计划项目、全国“互联网+”大学生创新创业大赛、大学生创新创业拔尖人才培养等工作。以面向未来的大学生创新创业精神、创新创业意识和创新创业能力培养为己任，全面推进教育创新校企合作育人，助力学院人才培养质量提升。
		</p>
	</div>
</body>
</html>
