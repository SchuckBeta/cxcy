<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8" />
<meta name="decorator" content="site-decorator" />
<link rel="stylesheet" href="${ctxCommon}/common-css/common.css" />
<link rel="stylesheet" href="${ctxCommon}/common-css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctxCss}/scStyle.css" />
<title>双创动态</title>
</head>

<body>
	<hr style="height: 5px; background-color: red;">
	<cms:pageDetailList />
<hr style="height: 5px; background-color: red;">
</body>
</html>