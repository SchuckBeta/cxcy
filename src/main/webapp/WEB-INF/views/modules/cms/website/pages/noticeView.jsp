<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>

<body>
<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/frontNotice/noticeList">通知公告</a></el-breadcrumb-item>
        <el-breadcrumb-item>详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="base-content-container">
        <h3 class="bc-title">
            ${oaNotify.title}
        </h3>
        <div class="bc-sources">
            <span>发布时间：<fmt:formatDate value="${oaNotify.updateDate}" pattern="yyyy.MM.dd"/></span>
            <span>来源：${oaNotify.source}</span>
            <span>浏览量：${oaNotify.views}</span>
        </div>

        <div class="bc-content" v-html="oaNotify.content" style="line-height: 1">
            <!--正文-->
            <%--${content}--%>
            <%--<c:out value=" ${ content } "  escapeXml="false" />--%>
        </div>
    </div>
</div>

<script type="text/javascript">


    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var oaNotify = JSON.parse(JSON.stringify(${fns: toJson(oaNotify)}));
            return {
                oaNotify: oaNotify || {}
            }
        },
        methods: {

        },
        created: function () {
        }
    })

</script>

</body>

</html>