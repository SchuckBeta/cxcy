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
        $(document).ready(function () {
            //左侧菜单加入数字
            $('#sub_list_wrap', window.parent.document).find('li a').each(function () {
                var hrefReg = $(this).attr('href');
                var count = "${page.todoCount}";
                if (hrefReg.indexOf("middleRatingList") != -1) {
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
            <%--<span>中期检查</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="projectDeclare" action="${ctx}/state/middleRatingList" method="post"
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
            <button id="btnSubmit" class="btn btn-primary" type="submit">查询</button>
            <button class="btn btn-primary" type="button" data-toggle="modal" data-target="#middleAuditModal">批量审核
            </button>
        </div>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th width="151">项目编号</th>
            <th width="25%">项目名称</th>
            <th>项目类别</th>
            <th>负责人</th>
            <th>组人数</th>
            <th>指导老师</th>
            <th>项目级别</th>
            <th>中期评分</th>
            <th>年份</th>
            <th>项目审核状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proDeclare">
            <tr>
                <td>
                        ${proDeclare.status == "other"? act.projectDeclare.number :proDeclare.number} <%--${act.vars.map.number}--%>
                </td>
                <td>
                    <a href="${ctx}/state/infoView?id=${proDeclare.id}&taskDefinitionKey=middleScore">
                    ${proDeclare.state == "other"? proDeclare.name :proDeclare.name} </a>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.typeString :fns:getDictLabel(proDeclare.type, "project_type", "")} <%--${fns:getDictLabel(act.vars.map.type, "project_type", "")}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.leaderString :proDeclare.leaderString} <%--${act.vars.map.leader}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"?  pj:getTeamNum(proDeclare.snames) : proDeclare.snumber} <%--${act.vars.map.teamList}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"? proDeclare.tnames :proDeclare.tnames} <%--${act.vars.map.teacher}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"?proDeclare.levelString :fns:getDictLabel(proDeclare.level, "project_degree", "")} <%--${fns:getDictLabel(act.vars.map.level, "project_degree", "")}--%>
                </td>
                <td>
                        ${proDeclare.state == "other"? "" :proDeclare.midScore} <%--${act.vars.map.midScore}--%>
                </td>

                <td>
                        ${proDeclare.state == "other"? proDeclare.year : proDeclare.year} <%--${fns:getDictLabel(act.vars.map.level, "project_degree", "")}--%>
                </td>
                <td>

                    <c:if test="${proDeclare.state=='todo'||proDeclare.state=='claim'}">
                        <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                        <%-- <a href="${ctx}/act/task/processMap?procDefId=${act.task.processDefinitionId}&proInstId=${act.task.processInstanceId}" target="_blank">待${act.taskName}</a> --%>
                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                ${pj:getAuditStatus(projectDeclare.status_code)}
                        </a>
                    </c:if>
                    <c:if test="${proDeclare.state=='finish'}">
                        <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                        ${pj:getAuditStatus(projectDeclare.status_code)}
                    </c:if>
                    <c:if test="${proDeclare.status=='other'}">
                        <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                        <%-- <a href="${ctx}/act/task/processMap2?proInsId=${act.projectDeclare.procInsId}" target="_blank">待${act.taskName}</a> --%>
                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                ${pj:getAuditStatus(projectDeclare.status_code)}
                        </a>
                    </c:if>
                    <c:if test="${proDeclare.state=='middleScore'}">
                        <c:set var="projectDeclare" value="${pj:getProjectDeclareListVoById(proDeclare.id)}"/>
                        <%-- <a href="${ctx}/act/task/processMap2?proInsId=${act.histTask.processInstanceId}" target="_blank">${act.taskName}</a> --%>
                        <a href="${ctx}/actyw/actYwGnode/designView/${projectDeclare.status_code}?groupId=${projectDeclare.groupId}"
                           target="_blank">
                                ${pj:getAuditStatus(projectDeclare.status_code)}
                        </a>
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
                           href="${ctx}/state/infoView?id=${proDeclare.id}&taskDefinitionKey=${act.histTask.taskDefinitionKey}">查看</a>
                    </c:if>
                    <c:if test="${proDeclare.state=='other'}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/state/projectDetail?id=${proDeclare.id}&editFlag=1">
                            审核
                        </a>
                    </c:if>
                    <c:if test="${proDeclare.state =='middleScore'}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/state/infoView?id=${proDeclare.id}&taskDefinitionKey=middleScore">
                            查看
                        </a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
<!-- Modal -->


<div id="middleAuditModal" class="modal hide" data-backdrop="static">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>批量审核</h3>
    </div>
    <div class="modal-body">
        <form class="form-horizontal" id="addForm" name="addForm" method="post" autocomplete="off">
            <div class="control-group">
                <label class="control-label">批量将高于等于</label>
                <div class="controls">
                    <input type="text" class="input-mini number required" id="midScore"
                           name="midScore"/>
                    <span>分，定义为合格</span>
                </div>
            </div>
        </form>
        <p class="input-numbers" style="padding-left: 60px;">项目数共<span style="color: red;">-</span>个，被选为合格的有<span
                style="color: red">-</span>个，被选为不合格的有<span style="color: red">-</span>个</p>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-primary btn-audit">确定</button>
        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
    </div>
</div>
<script>

    $(function () {
        var $middleAuditModal = $('#middleAuditModal');
        var $midScore = $('input[name="midScore"]');
        var $inputNumbers = $middleAuditModal.find('.input-numbers');
        var $btnAudit = $middleAuditModal.find('.btn-audit');
        var passIds = [];
        var passTaskIds = [];
        var failedIds = [];
        var failedTaskIds = [];
        var $addForm = $('#addForm');
        $midScore.on('change.getPro', function (e) {
            var val = $(this).val();
            $.ajax({
                type: 'POST',
                url: '${ctx}/projectBatch/getNoByScore',
                data: {
                    score: val
                },
                success: function (data) {
                    var passes = data.pass;
                    var fails = data.failed;
                    var passLen = data.pass.length;
                    var failedLen = data.failed.length;
                    inputProNum($inputNumbers.find('span'), passLen, failedLen)
                    emptyIds()
                    pushIds(passes, fails)
                }
            })
        });
        $addForm.validate({
            rules: {
                midScore: {
                    min: 0,
                    max: 100
                }
            },
            errorPlacement: function (error, element) {
                if (element.attr('name') == 'midScore') {
                    error.appendTo(element.parent())
                }
            },
            submitHandler: function (form) {
                $btnAudit.attr("disabled", true).text('审核中...');
                $.ajax({
                    type: 'POST',
                    url: '${ctx}/projectBatch/middleRatingBatch',
                    data: {
                        passIds: passIds.join(','),
                        passTaskIds: passTaskIds.join(','),
                        failedIds: failedIds.join(','),
                        failedTaskIds: failedTaskIds.join(',')
                    },
                    success: function (data) {
                        $btnAudit.attr("disabled", false).text('确定');
                        $middleAuditModal.modal('hide')
                        window.location.reload();
                    },
                    error: function () {
                        $btnAudit.attr("disabled", false).text('确定');
                    }
                })
                return false;
            }
        });
        $btnAudit.on('click', function () {
            $addForm.submit();
        })

        function inputProNum(elements, passLen, failedLen) {
            elements.each(function (i, span) {
                switch (i) {
                    case 0:
                        $(this).text(passLen + failedLen);
                        break;
                    case 1:
                        $(this).text(passLen);
                        break;
                    default:
                        $(this).text(failedLen);

                }
            })
        }


        function pushIds(passes, fails) {
            $.each(passes, function (i, item) {
                passIds.push(item.bussinessId);
                passTaskIds.push(item.taskId)
            });
            $.each(fails, function (i, item) {
                failedIds.push(item.bussinessId);
                failedTaskIds.push(item.taskId);
            })
        }

        function emptyIds() {
            passIds.length = 0;
            passTaskIds.length = 0;
            failedIds.length = 0;
            failedTaskIds.length = 0;
        }
    });

</script>
</body>
</html>