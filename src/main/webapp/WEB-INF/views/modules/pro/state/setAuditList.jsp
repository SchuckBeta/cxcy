<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <script type="text/javascript">
    $frontOrAdmin="${frontOrAdmin_2ed8fe6ddd034cffa47a41e42a694948}";
    </script>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        var validate;
        $(document).ready(function () {

            //左侧菜单加入数字
            $('#sub_list_wrap', window.parent.document).find('li a').each(function () {
                var hrefReg = $(this).attr('href');
                var count = "${page.todoCount}";
                if (hrefReg.indexOf("setAuditList") != -1) {
                    if (count > 0) {
                        if (count > 99) {
                            //count = "99+";
                        }
                        if ($(this).find('i').size() > 0) {
                            $(this).find('i').text(count);
                        } else {
                            $(this).append('<i class="unread-tag" style="box-sizing: border-box">' + count + '</i>');
                        }
                    } else {
                        $(this).find('i').detach();
                    }
                    window.parent.sideNavModule.changeTotalUnreadTag($(this).parents(".submenu")[0]);
                    return false;
                }
            });

            $("#ps").val($("#pageSize").val());
            validate = $("#addForm").validate({
                messages: {
                    level: {
                        required: "请选择"
                    }
                },
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function claim(taskId) {
            $.get('${ctx}/act/task/claim', {taskId: taskId}, function (data) {
                location.reload();
            });
        }
    </script>
</head>
<body>

<div class="container-fluid">
    <%--<div class="edit-bar edit-bar-tag clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>立项审核</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="projectDeclare" action="${ctx}/state/setAuditList" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">项目编号</label>
                <div class="controls">
                    <form:input type="text" path="number" cssClass="form-control input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目名称</label>
                <div class="controls">
                    <form:input type="text" path="name" cssClass="form-control input-medium"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目类别</label>
                <div class="controls">
                    <form:select path="type" cssClass="form-control input-medium">
                        <form:option value="" label="所有项目类别"/>
                        <form:options items="${fns:getDictList('project_type')}" itemLabel="label"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目级别</label>
                <div class="controls">
                    <form:select path="level" cssClass="form-control input-medium">
                        <form:option value="" label="请选择"/>
                        <form:options items="${fns:getDictList('project_degree')}" itemLabel="label"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group" style="margin-right: 150px;">
                <label class="control-label">负责人</label>
                <div class="controls">
                    <form:input type="text" path="leader" cssClass="form-control input-medium"/>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button id="btnSubmit" type="submit" class="btn btn-primary">查询</button>
            <button id="btnSubmit1" type="button" class="btn btn-primary" data-toggle="modal"
                    data-target="#myModal">批量审核
            </button>
            <%--<button type="button" class="btn btn-primary" data-toggle="modal"--%>
                  <%--data-target="#myModal">查询</button>--%>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th width="32"><input type="checkbox" id="check_all" data-flag="false"></th>
            <th width="151">项目编号</th>
            <th width="25%">项目名称</th>
            <th>项目类型</th>
            <th>负责人</th>
            <th>组人数</th>
            <th>指导老师</th>
            <th>项目级别</th>
            <th>年份</th>
            <th>项目审核状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proDeclare">
            <tr>
                <td class="checkone">
                    <c:if test="${proDeclare.state=='todo'}">
                        <input type="checkbox" value="${proDeclare.id}:${proDeclare.act.task.id}" name="boxTd">
                    </c:if>
                </td>
                <td>${proDeclare.number}</td>
                <td>
                    <a href="${ctx}/state/infoView?id=${proDeclare.id}">${proDeclare.name}</a>
                </td>
                <td>
                       ${fns:getDictLabel(proDeclare.type, "project_type", "")}
                </td>
                <td>
                       ${proDeclare.leaderString}
                </td>
                <td>
                       ${proDeclare.snumber}
                </td>
                <td>
                       ${proDeclare.tnames}
                </td>
                <td>${fns:getDictLabel(proDeclare.level, "project_degree", "")}</td>
                <td>
                       ${proDeclare.year} <%--${fns:getDictLabel(act.vars.map.level, "project_degree", "")}--%>
                </td>
                <td>
                    <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                    <c:if test="${proDeclare.state=='todo'||proDeclare.state=='claim'}">

                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                ${pj:getAuditStatus(projectDeclare.status_code)}
                        </a>
                    </c:if>
                    <c:if test="${proDeclare.state=='finish'}">
                        ${pj:getAuditStatus(projectDeclare.status_code)}
                    </c:if>
                </td>
                <td>
                    <c:if test="${proDeclare.state=='claim'}">
                        <a class="btn btn-primary btn-small" href="javascript:void(0)"
                           onclick="claim('${proDeclare.act.task.id}');">签收</a>
                    </c:if>
                    <c:if test="${proDeclare.state=='todo'}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/act/task/form?taskId=${proDeclare.act.task.id}&taskName=${fns:urlEncode(proDeclare.act.task.name)}&taskDefKey=${proDeclare.act.task.taskDefinitionKey}&procInsId=${proDeclare.act.task.processInstanceId}&procDefId=${proDeclare.act.task.processDefinitionId}&status=${proDeclare.state}">审核</a>
                    </c:if>
                    <c:if test="${proDeclare.state=='finish'}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/state/infoView?id=${proDeclare.id}&taskDefinitionKey=${proDeclare.act.histTask.taskDefinitionKey}">查看</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>

<!-- Button to trigger modal -->

<!-- Modal -->
<div id="myModal" data-backdrop="static"  class="modal hide" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">批量审核</h3>
    </div>
    <div class="modal-body" id="selectArea">
        <form class="form-horizontal" id="addForm" name="addForm" method="post" novalidate>
            <div class="control-group">
                <label class="control-label" style="width: 175px;">需要将所选项目批量审核为</label>
                <div class="controls" style="margin-left: 190px;">
                    <select class="form-control" id="leveldialog" name="leveldialog" required>
                        <option value="">--请选择--</option>
                        <option value="2">提交给校级评审</option>
                        <option value="4">C</option>
                        <option value="5">不合格</option>
                    </select>
                </div>
            </div>
        </form>
        <div class="buffer_gif" style="text-align:center;padding:20px 0px;display:none;" id="bufferImg">
            <img src="${ctxImg}/jbox-loading1.gif" alt="缓冲图片">
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn btn-primary" aria-hidden="true" id="confirmBtn"
                onclick="doBatch('/a/projectBatch/collegeSetBatch');">确定
        </button>
        <button class="btn btn-default" data-dismiss="modal">取消</button>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script src="${ctxJs}/gcProject/auditList.js?v=1"></script>

</body>
</html>