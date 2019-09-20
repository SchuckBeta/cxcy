<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@ include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
            if ($("#flow").val()) {
                resetTip();
                $.ajax({
                    type: 'get',
                    url: '${ctx}/actyw/actYwGnode/treeDataByYwId?level=1&ywId=' + $("#flow").val(),
                    success: function (data) {
                        if (data) {
                            $.each(data, function (i, v) {
                                if ($("#nodeid").val() == v.id) {
                                    $("#node").append('<option selected value="' + v.id + '" >' + v.name + '</option>');
                                } else {
                                    $("#node").append('<option value="' + v.id + '" >' + v.name + '</option>');
                                }
                            });
                        }
                    }
                });
            }
            $("#form-search-bar").validate({
                submitHandler: function (form) {
                    if (!$("#name").val()) {
                        alertx("请选择评审标准");
                        return;
                    }
                    if (!$("#flow").val()) {
                        alertx("请选择关联项目");
                        return;
                    }
                    if (!$("#node").val()) {
                        alertx("请选择关联审核节点");
                        return;
                    }
                    resetTip();
                    form.submit();
                }
            });

        });
        function page(n, s) {
            location.href = "${ctx}/auditstandard/auditStandard?pageNo=" + n + "&pageSize=" + s;
        }
        function flowChange() {
            resetTip();
            $("#node").find("option:gt(0)").remove();
            if ($("#flow").val()) {
                $.ajax({
                    type: 'get',
                    url: '${ctx}/actyw/actYwGnode/treeDataByYwId?level=1&ywId=' + $("#flow").val(),
                    success: function (data) {
                        if (data) {
                            $.each(data, function (i, v) {
                                $("#node").append('<option value="' + v.id + '" >' + v.name + '</option>');
                            });
                        }
                    }
                });
            }
        }
        function confirmxChange(rid, ob) {
            resetTip();
            confirmx("确认设置？", function () {
                onChildChange(rid, ob);
            }, function () {
                if ($(ob).attr("checked")) {
                    $(ob).removeAttr("checked");
                } else {
                    $(ob).attr("checked", true);
                }
            });
        }
        function onChildChange(rid, ob) {
            var str = "";
            var obs = $(ob).parent().parent().find("input[type='checkbox']:checked");
            if (obs) {
                var arr = new Array(obs.length);
                $.each(obs, function (i, v) {
                    arr.push($(v).val());
                });
                str = arr.join(",");
            }
            resetTip();
            $.ajax({
                type: 'post',
                url: '${ctx}/auditstandard/auditStandard/saveChild',
                datatype: "json",
                data: {relationId: rid, isEscoreNodes: str},
                success: function (data) {
                    var st = "success";
                    if (data.ret == "0") {
                        st = "error";
                    }
                    top.$.jBox.tip(data.msg, st, {persistent: true, opacity: 0});
                }
            });
        }


    </script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>关联项目</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <sys:message content="${message}"/>
    <form:form id="form-search-bar" modelAttribute="vo" action="${ctx}/auditstandard/auditStandard/saveDetail"
               class="form-horizontal clearfix form-search-block">
        <input id="nodeid" name="nodeid" type="hidden" value="${vo.node}"/>
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">评审标准</label>
                <div class="controls">
                    <form:select path="name" class="input-medium">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getAuditStandardList()}" itemLabel="name" itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">关联项目</label>
                <div class="controls">
                    <form:select path="flow" class="input-medium" onchange="flowChange()">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getActListData('')}" itemLabel="proProject.projectName"
                                      itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">关联审核节点</label>
                <div class="controls">
                    <select id="node" name="node" class="input-medium">
                        <option value="">--请选择--</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="search-btn-box">
            <button type="submit" class="btn btn-primary">添加</button>
            <button type="button" class="btn btn-default"
                    onclick="javascript:location.href='${ctx}/auditstandard/auditStandard/list'">返回
            </button>
        </div>
    </form:form>
    <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
        <thead>
        <tr>
            <th>评审标准</th>
            <th>关联项目</th>
            <th>关联审核节点</th>
            <th>是否需要评分</th>
            <th width="80">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="sta">
            <tr>
                <td>${sta.name }</td>
                <td>${sta.flowName }</td>
                <td>${sta.nodeName }</td>
                <td>
                    <c:forEach items="${sta.childs}" var="child">
                        <label for="${child.id }">
                            <input onclick="confirmxChange('${sta.relationId}',this)" id="${child.id }" value="${child.id }" <c:if test="${child.sel=='1' }">checked</c:if> type="checkbox">
                                ${child.name }</label>
                    </c:forEach>
                </td>
                <td>
                    <button type="button" class="btn btn-default btn-small"
                            onclick="javascript:return confirmx('确认要删除吗？', function(){location.href='${ctx}/auditstandard/auditStandard/deleteDetail?id=${sta.id}&flow=${sta.flow}&node=${sta.node}'})">
                        删除
                    </button>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
</body>
</html>