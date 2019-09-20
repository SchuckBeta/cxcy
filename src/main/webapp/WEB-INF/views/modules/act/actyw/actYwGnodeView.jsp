<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/snap/snap.svg-min.js"></script>
    <style>

        .flow-center {
            position: relative;
            overflow: hidden;
            margin: 40px auto;
        }

        .flow-content {
            position: absolute;
        }
    </style>
</head>
<body>

<%--<div class="top-header">--%>
    <%--<ul>--%>
        <%--<li class="remove-nav-index logout"><a href="${ctx}/logout"><i class="icon-signout"></i> 退出</a></li>--%>
        <%--<li class="remove-nav-index"><a href="${ctx}/sysMenuIndex"><i class="icon-reply-all"></i> 返回首页 </a></li>--%>
        <%--<li class="remove-nav-index"><a href="${ctx}/sys/menu/treePlusByLtype?lver=2&ltype=55&href=/sys/user/modifyPwd"><i--%>
                <%--class="icon-user"></i> ${fns:getUser().name}</a></li>--%>
        <%--<li class="remove-nav-index"><a href="${ctx}/sys/menu/treePlusByLtype?lver=2&ltype=55&href=/oa/oaNotify/msgRecList"><i class="icon-bell"></i>消息(<i style="font-style: normal" id="84bcd363e9b24e25b8aaad7bed3d1dcf_unread">0</i>)</a></li>--%>
        <%--<!-- <li><span></span><a href="#">提醒通知</a></li> -->--%>
    <%--</ul>--%>
<%--</div>--%>

<div id="container-width" class="container container-ct">

    <c:if test="${not empty group.uiHtml}">
        <div class="flow-center">
            <div class="flow-content">
                <svg id="svg" version="1.1" width="100%" height="100%" xmlns="http://www.w3.org/2000/svg"
                     xmlns:xlink="http://www.w3.org/1999/xlink">
                    <g id="g">
                            ${group.uiHtml}
                    </g>
                </svg>
            </div>
        </div>
    </c:if>
    <c:if test="${empty group.uiHtml}">
        <p class="gray-color text-center" style="margin-top: 50px;">没有流程预览图</p>
    </c:if>


</div>

<script>
    +function($){
        function Flow(){
            this.flowContent = $('.flow-content');
            this.svg = Snap("#svg");
            this.init();
        }

        //init里面放立即调用内容
        Flow.prototype.init = function () {
            if(this.svg){
                this.appearCenter();
            }
        }


        Flow.prototype.appearCenter = function () {
            //让整个svg居中
            var gBox = this.svg.select('g[id="g"]').getBBox();
            var gWidth = gBox.width;
            var gHeight = gBox.height;
            $('#container-width').css('width',gWidth + 4);

            var gLeft = gBox.x;
            var gTop = gBox.y;

            $('.flow-center').css('width', gWidth + 5).css('height', gHeight + 5);
            $('.flow-content').css('width', gWidth + 5 + gLeft).css('height', gHeight + 5 + gTop).css('left', -gLeft + 1).css('top', -gTop + 2);

        }

        var flow = new Flow();

    }(jQuery);
</script>










</body>
</html>