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
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>企业管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <ul class="nav nav-tabs nav-tabs-default">
        <li class="active"><a href="${ctx}/pw/pwCompany/">入驻企业列表</a></li>
        <shiro:hasPermission name="pw:pwCompany:edit">
            <li><a href="${ctx}/pw/pwCompany/form">入驻企业添加</a></li>
        </shiro:hasPermission>
    </ul>
    <div class="tab-content-default">
        <form:form id="searchForm" modelAttribute="pwCompany" action="${ctx}/pw/pwCompany/" method="post"
                   class="form-horizontal clearfix form-search-block">
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
            <div class="col-control-group">
                <div class="control-group">
                    <label class="control-label">名称</label>
                    <div class="controls">
                        <form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">法人</label>
                    <div class="controls">
                        <form:input path="regPerson" htmlEscape="false" maxlength="255" class="input-medium"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">资金来源</label>
                    <div class="controls">
                        <form:select path="regMtype" class="input-medium">
                            <form:option value="" label="--请选择--"/>
                            <form:options items="${fns:getDictList('pw_reg_mtype')}" itemLabel="label" itemValue="value"
                                          htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">手机</label>
                    <div class="controls">
                        <form:input path="mobile" htmlEscape="false" maxlength="255" class="input-medium"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">电话</label>
                    <div class="controls">
                        <form:input path="phone" htmlEscape="false" maxlength="255" class="input-medium"/>
                    </div>
                </div>
            </div>
            <div class="search-btn-box">
                <input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/>
            </div>
        </form:form>
        <sys:message content="${message}"/>
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover table-center table-orange table-sort table-nowrap table-subscribe">
            <thead>
            <tr>
                <th>名称</th>
                <th>法人</th>
                <th>注册资金(万元)</th>
                <th>资金来源</th>
                <th>手机</th>
                <%--<th>电话</th>--%>
                <th>地址</th>
                <%--<th>备注</th>--%>
                <shiro:hasPermission name="pw:pwCompany:edit">
                    <th>操作</th>
                </shiro:hasPermission>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="pwCompany">
                <tr>
                    <td><a href="${ctx}/pw/pwCompany/form?id=${pwCompany.id}">
                            ${pwCompany.name}
                    </a></td>
                    <td>
                            ${pwCompany.regPerson}
                    </td>
                    <td>
                            ${pwCompany.regMoney}
                    </td>
                    <td>
                        <c:forEach var="regmt" items="${pwCompany.regMtypes }" varStatus="idx">
                            <c:if test="${idx.index eq 0 }">${fns:getDictLabel(regmt, 'pw_reg_mtype', '')}</c:if>
                            <c:if test="${idx.index ne 0 }"> / ${fns:getDictLabel(regmt, 'pw_reg_mtype', '')}</c:if>
                        </c:forEach>
                    </td>
                    <td>
                            ${pwCompany.mobile}
                    </td>
                        <%--<td>--%>
                        <%--${pwCompany.phone}--%>
                        <%--</td>--%>
                    <td>
                            ${pwCompany.address}
                    </td>

                        <%--<td>--%>
                        <%--${pwCompany.remarks}--%>
                        <%--</td>--%>
                    <shiro:hasPermission name="pw:pwCompany:edit">
                        <td style="width:12%">
                            <a class="btn btn-small btn-primary"
                               href="${ctx}/pw/pwCompany/form?id=${pwCompany.id}">修改</a>
                            <a class="btn btn-small btn-default" href="${ctx}/pw/pwCompany/delete?id=${pwCompany.id}"
                               onclick="return confirmx('确认要删除该入驻企业吗？', this.href)">删除</a>
                        </td>
                    </shiro:hasPermission>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    ${page.footer}
</div>
<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
</body>
</html>