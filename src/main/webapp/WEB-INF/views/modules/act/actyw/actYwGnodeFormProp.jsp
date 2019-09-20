<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>流程</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-horizontal">
        <div class="control-group">
            <label class="control-label">自定义流程：</label>
            <div class="controls">
                <p class="control-static"> ${actYwGnode.group.name}</p>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">父级节点：</label>
            <div class="controls">
                <p class="control-static"> ${actYwGnode.parent.name}</p>
            </div>
        </div>

        <c:if test="${not empty preNodeNames}">
            <div class="control-group">
                <label class="control-label">流程前置节点：</label>
                <div class="controls">
                    <p class="control-static">${preNodeNames}</p>
                </div>
            </div>
        </c:if>

        <div class="control-group">
            <label class="control-label">流程业务节点：</label>
            <div class="controls">
                <p class="control-static">${actYwGnode.name}</p>
            </div>
        </div>

        <c:if test="${not empty nextNodeNames}">
            <div class="control-group">
                <label class="control-label">流程后置节点：</label>
                <div class="controls">
                    <p class="control-static">${nextNodeNames}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty actYwGnode.gformNames}">
            <div class="control-group">
                <label class="control-label">表单：</label>
                <div class="controls">
                    <p class="control-static">${actYwGnode.gformNames}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty actYwGnode.groleNames}">
            <div class="control-group">
                <label class="control-label">角色：</label>
                <div class="controls">
                    <p class="control-static">${actYwGnode.groleNames}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty actYwGnode.gstatusNames}">
            <div class="control-group">
                <label class="control-label">状态：</label>
                <div class="controls">
                    <p class="control-static">${actYwGnode.gstatusNames}</p>
                </div>
            </div>
        </c:if>

        <c:if test="${ not empty actYwGnode.iconUrl}">
            <div class="control-group">
                <label class="control-label">图标：</label>
                <div class="controls">
                    <div class="act_Yw-icon">
                        <img src="${fns:ftpImgUrl(actYwGnode.iconUrl)}"/>
                    </div>
                </div>
            </div>

        </c:if>
        <div class="control-group">
            <label class="control-label">显示：</label>
            <div class="controls">
                <p class="control-static"><c:if test="${isShow}">是</c:if>
                <c:if test="${not isShow}">否</c:if></p>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label">备注：</label>
            <div class="controls">
                <p class="control-static">${actYwGnode.remarks}</p>
            </div>
        </div>
        <div class="form-actions">
            <button type="button" onclick="history.go(-1)" class="btn btn-default">返回</button>
        </div>
    </div>
</div>
</body>
</html>