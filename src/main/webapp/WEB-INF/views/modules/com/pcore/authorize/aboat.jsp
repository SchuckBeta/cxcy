<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
    <control-rule-block title="版本信息">
        <e-col-item label="产品名称：" label-width="90px">噢易创新创业教育云平台（简称“开创啦”）</e-col-item>
        <e-col-item label="版本号：" label-width="90px">${version}</e-col-item>
        <e-col-item label="产品状态：" label-width="90px"><c:if test="${valid=='1'}">已激活</c:if><c:if
                test="${valid=='0'}">未激活</c:if><c:if test="${valid=='2'}">已过期</c:if></e-col-item>
        <e-col-item label="产品有效期：" label-width="90px"><c:if
                test="${valid=='1'||valid=='2'}">${fns: substringBeforeLast(exp, '00:00:00')}</c:if></e-col-item>
    </control-rule-block>
    <control-rule-block title="警告">
        本产品受版权及国际公约保护，任何单位和个人，在没有经过版权所有者的书面认可下，不得以任何手段复制、改编、引用本文件，版权所有者对该软件的侵权行为诉诸法律的权利。
    </control-rule-block>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app'
    })

</script>
</body>
</html>