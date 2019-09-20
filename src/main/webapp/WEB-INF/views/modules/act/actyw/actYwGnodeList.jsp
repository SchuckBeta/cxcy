<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g, "");
            var data = ${fns:toJson(list)}, ids = [], rootIds = [];
            for (var i = 0; i < data.length; i++) {
                ids.push(data[i].id);
            }
            ids = ',' + ids.join(',') + ',';
            for (var i = 0; i < data.length; i++) {
                if (ids.indexOf(',' + data[i].parentId + ',') == -1) {
                    if ((',' + rootIds.join(',') + ',').indexOf(',' + data[i].parentId + ',') == -1) {
                        rootIds.push(data[i].parentId);
                    }
                }
            }
            for (var i = 0; i < rootIds.length; i++) {
                addRow("#treeTableList", tpl, data, rootIds[i], true);
            }
            $("#treeTable").treeTable({expandLevel: 5});
        });
        function addRow(list, tpl, data, pid, root) {
            for (var i = 0; i < data.length; i++) {
                var row = data[i];

                if ((${fns:jsGetVal('row.parentId')}) == pid) {
                    var isFormVal = getDictLabelByBoolean(${fns:toJson(fns:getDictList('true_false'))}, row.isForm);
                    var formNameVal = (row.form != null) ? row.form.name : "";
                    $(list).append(Mustache.render(tpl, {
                        dict: {
                            isShow: ((row.isShow || (row.isShow == 'true')) ? "显示" : "隐藏"),
                            isForm: isFormVal,
                            formName: ((row.isForm || (row.isForm == 'true')) ? isFormVal + " " + formNameVal : isFormVal),
                            roleName: ((row.flowGroup != null) ? "是 " + row.role.name : "否"),
                            isLevel0: ((row.node.level == '0') ? true : false),
                            isLevel1: ((row.node.level == '1') ? true : false),
                            isLevel2: ((row.node.level == '2') ? true : false),
                            blank123: 0
                        }, pid: (root ? 0 : pid), row: row
                    }));
                    addRow(list, tpl, data, row.id);
                }
            }
        }
        function updateSort() {
            loading('正在提交，请稍等...');
            $("#listForm").attr("action", "${ctx}/actyw/actYwGnode/updateSort?groupId=" + $("#searchForm").find("select[name='groupId']").val());
            $("#listForm").submit();
        }


    </script>
</head>
<body>

<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>流程列表</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="actYwGnode" action="${ctx}/actyw/actYwGnode/" method="post"
               class="form-horizontal clearfix form-search-block">
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">自定义流程</label>
                <div class="controls">
                    <form:select path="group.id" class="input-medium">
                        <%--<form:option value="" label="--请选择--"/>--%>
                        <c:forEach var="actYwGroup" items="${actYwGroups }">
                            <form:option value="${actYwGroup.id}" label="${actYwGroup.name}"/>
                        </c:forEach>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">显示业务</label>
                <div class="controls">
                    <select name="isYw" class="input-medium">
                        <c:if test="${isYw}">
                            <option value="0">否</option>
                            <option value="1" selected="selected">是</option>
                        </c:if>
                        <c:if test="${!isYw}">
                            <option value="0" selected="selected">否</option>
                            <option value="1">是</option>
                        </c:if>
                    </select>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button type="submit" class="btn btn-primary">查询流程</button>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="treeTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <!-- <th>自定义流程</th> -->
            <th>流程节点</th>
            <!-- <td>{{row.isShow}}</td> -->
            <!-- <th>排序</th> -->
            <%-- <td style="text-align:center;">
                <shiro:hasPermission name="sys:menu:edit">
                    <input type="hidden" name="ids" value="{{row.id}}"/>
                    <input name="sorts" type="text" value="{{row.sort}}" style="width:50px;margin:0;padding:0;text-align:center;">
                </shiro:hasPermission><shiro:lacksPermission name="sys:menu:edit">
                    {{row.sort}}
                </shiro:lacksPermission>
            </td> --%>
            <th>表单</th>
            <th>角色</th>
            <th>显示</th>
            <th>最后更新时间</th>
            <th>备注</th>
            <%--<shiro:hasPermission name="actyw:actYwGnode:edit"><th>操作</th></shiro:hasPermission>--%>
        </tr>
        </thead>
        <tbody id="treeTableList"></tbody>
    </table>
</div>
<script type="text/template" id="treeTableTpl">
    <tr id="{{row.id}}" pId="{{pid}}">
        <td style="text-align:left;"><a href="${ctx}/actyw/actYwGnode/view?id={{row.id}}">
            {{row.name}}
        </a>
        </td>
        <td>
            {{row.gformNames}}
        </td>
        <td>
            {{row.groleNames}}
        </td>
        <td>{{dict.isShow}}</td>
        <td>
            {{row.updateDate}}
        </td>
        <td>
            {{row.remarks}}
        </td>
        <%--<shiro:hasPermission name="actyw:actYwGnode:edit"><td>--%>
        <%--&lt;%&ndash;{{#dict.isLevel1}}&ndash;%&gt;--%>
        <%--&lt;%&ndash;<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwGnode/form?parent.id={{row.id}}&parentId={{row.id}}&groupId={{row.group.id}}&group.id={{row.group.id}}">添加下级流程</a>&ndash;%&gt;--%>
        <%--&lt;%&ndash;{{/dict.isLevel1}}&ndash;%&gt;--%>
        <%--<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/actyw/actYwGnode/formProp?id={{row.id}}&groupId={{row.group.id}}&group.id={{row.group.id}}">修改</a>--%>
        <%--&lt;%&ndash;<a class="check_btn btn-pray btn-lx-primary" data-node="delete" data-url="${ctx}/actyw/gnode/delete/{{row.id}}" href="javascript: void (0);" >删除</a>&ndash;%&gt;--%>
        <%--</td></shiro:hasPermission>--%>
    </tr>
</script>
</body>
</html>