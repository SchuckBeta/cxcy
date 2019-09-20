<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="decorator" content="cyjd-site-default"/>
    <title>${frontTitle}</title>
    <style type="text/css">




        .dsrdinfo h3 {
            text-align: center;
        }

        .dsrdinfo h6 {
            text-align: center;
            margin-top: 34px;
            color: #BCBCBC;
        }

        .more h3 {
            margin-top: 112px;
            border-bottom: 3px solid red;
            width: 100px;
            padding-bottom: 10px;
            float: left;
        }

        .newinfo {
            margin-top: 30px;
        }

        .newinfo ul li {
            padding-top: 20px;
        }

        .newinfo ul li span {
            margin-left: 195px;
        }


    </style>
</head>

<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li>
            <a href="${ctxFront}#hotTopics"><i class="icon-home"></i>首页</a>
        </li>
        <li class="active">大赛热点</li>
    </ol>
    <div class="dsrdinfo" style="margin: 0">
        <h3>${gcontestHots.title }</h3>
        <h6>发布时间:<fmt:formatDate value="${gcontestHots.releaseDate}" pattern="yyyy-MM-dd"/> 来源:${gcontestHots.source }
            浏览量:${gcontestHots.views }</h6>
    </div>
    <div style="clear: both;"></div>
    ${gcontestHots.content }
    <div class="more">
        <h3>相关推荐</h3>
    </div>

    <div class="clearfix"></div>
    <div class="newinfo">
        <ul>
            <c:forEach items="${more}" var="item">
                <li><a href="view?id=${item.id}">${item.title}</a><span><fmt:formatDate value="${item.release_date}"
                                                                                        pattern="yyyy-MM-dd"/></span>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>
</body>
</html>