<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }


        function kaiKa111() {
            $.ajax({
                type: 'post',
                url: '/a/dr/drCard/ajaxCardPublish/1',
                dataType: 'json',
                data: {
                    "user.id": "1",
                    "no": "100",
                    "password": "FFF11",
                    "expiry": "2018-04-08 11:39:17",
                    "status": "1",
                    "openTimes": "1000",
                    "privilege": "0",
                    "holidayUse": "0",
                    "drNo": "0",
                    "warnning": "0",
                    "isCancel": "1"
                },
                success: function (data) {
                    if (data.status) {
                        console.info(data);
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>门禁卡</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="drCard" action="${ctx}/dr/drCard/" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">用户名</label>
                <div class="controls">
                    <form:input path="user.name" htmlEscape="false" maxlength="64" class="input-medium"/>
                    <%-- <sys:treeselect id="user" name="user.id" value="${drCard.user.id}" labelName="user.name"
                                    labelValue="${drCard.user.name}"
                                    title="用户" url="/sys/office/treeData?type=3" cssClass="input-small"
                                    allowClear="true"
                                    notAllowSelectParent="true"/> --%>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">卡号</label>
                <div class="controls">
                    <form:input path="no" htmlEscape="false" maxlength="64" class="input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">卡状态</label>
                <div class="controls">
                    <form:select path="status" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${drCstatuss}" itemLabel="name" itemValue="key"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">特权</label>
                <div class="controls">
                    <form:select path="privilege" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${drAuths}" itemLabel="name" itemValue="key"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">是否预警</label>
                <div class="controls">
                    <form:select path="warnning" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">取消预警</label>
                <div class="controls">
                    <form:select path="isCancel" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button type="submit" class="btn btn-primary">查询</button>
            <a href="${ctx}/dr/drCard/form" class="btn btn-primary" onclick="create();">创建用户</a>
        </div>

    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <th>用户</th>
            <th>门禁卡卡号</th>
            <th>有效期</th>
            <th>状态</th>
            <th>开门次数</th>
            <th>特权</th>
            <th>节假日限制</th>
            <th>是否预警</th>
            <th>取消预警</th>
            <shiro:hasPermission name="dr:drCard:edit">
                <th width="220">操作</th>
            </shiro:hasPermission>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="drCard">
            <tr>
                <td><a href="${ctx}/dr/drCard/form?id=${drCard.id}">
                        ${drCard.user.name}
                </a></td>
                <td>
                        ${drCard.no}
                </td>
                <td>
                    <fmt:formatDate value="${drCard.expiry}" pattern="yyyy-MM-dd HH:mm:ss"/>
                </td>
                <td>
                    <c:forEach var="item" items="${drCstatuss}">
                        <c:if test="${drCard.status eq item.key}">${item.name }</c:if>
                    </c:forEach>
                </td>
                <td>
                    <c:if test="${expireMin eq drCard.openTimes}">无</c:if>
                    <c:if test="${expireMax eq drCard.openTimes}">不限</c:if>
                    <c:if test="${(expireMin ne drCard.openTimes) && (expireMax ne drCard.openTimes)}">${drCard.openTimes}次</c:if>
                </td>
                <td>
                    <c:forEach var="item" items="${drAuths}">
                        <c:if test="${drCard.privilege eq item.key}">${item.name }</c:if>
                    </c:forEach>
                </td>
                <td>
                        ${fns:getDictLabel(drCard.holidayUse, 'true_false', '')}
                </td>
                <td>
                        ${fns:getDictLabel(drCard.warnning, 'yes_no', '-')}
                </td>
                <td>
                        ${fns:getDictLabel(drCard.isCancel, 'yes_no', '-')}
                </td>
                <shiro:hasPermission name="dr:drCard:edit">
                    <td>
                        <div style="margin-bottom: 5px;">
                            <a id="fk" class="btn btn-small btn-primary" href="javascript:void(0)"
                               onclick="kaiKa111()">发卡</a>
                            <a class="btn btn-small btn-primary"
                               href="${ctx}/dr/drCard/ajaxCardActivate?id=${drCard.id}">激活</a>
                            <a class="btn btn-small btn-primary"
                               href="${ctx}/dr/drCard/ajaxCardLoss?id=${drCard.id}">挂失</a>
                            <a class="btn btn-small btn-primary"
                               href="${ctx}/dr/drCard/ajaxCardBack?id=${drCard.id}">退卡</a>
                        </div>

                        <a class="btn btn-small btn-primary"
                           href="${ctx}/dr/drCard/ajaxCardRepublish?id=${drCard.id}">重新发卡</a>
                        <a class="btn btn-small btn-primary" href="${ctx}/dr/drCard/form?id=${drCard.id}">修改</a>
                        <a class="btn btn-default btn-small" href="${ctx}/dr/drCard/delete?id=${drCard.id}"
                           onclick="return confirmx('确认要删除该门禁卡吗？', this.href)">删除</a>
                    </td>
                </shiro:hasPermission>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>


</body>
</html>