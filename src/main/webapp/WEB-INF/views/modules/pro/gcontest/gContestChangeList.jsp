<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script>
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
        function selectAll(ob) {
            if ($(ob).attr("checked")) {
                $("input[name='subck']:checkbox").attr("checked", true);
            } else {
                $("input[name='subck']:checkbox").attr("checked", false);
            }
        }
        function resall() {
            var temarr = [];
            $("input[name='subck']:checked").each(function (i, v) {
                temarr.push($(v).val());
            });
            if (temarr.length == 0) {
                alertx("请选择要发布的项目");
                return;
            }
            confirmx("确定发布所选项目？", function () {
                $.ajax({
                    type: 'post',
//                    url: '/a/excellent/resall',
                    url: '/a/cms/cmsArticle/excellent/gcontestPublish',
                    dataType: "json",
                    data: {
//                        fids: temarr.join(",")
                        ids: temarr.join(",")
                    },
                    success: function (data) {
                        alertx(data.status == '1' ? '发布成功' : '发布失败');
                    }
                });
            });
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar edit-bar-tag clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>参赛项目查询</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/gContestChangeList"
               method="post" class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">大赛类别</label>
                <div class="controls">
                    <form:select path="type" class="form-control input-medium">
                        <form:option value="" label="所有大赛类别"/>
                        <form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label"
                                      itemValue="value"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">参赛项目名称</label>
                <div class="controls">
                    <form:input type="text" cssClass="form-control input-medium" path="pName"/>
                </div>
            </div>
            <div class="control-group" >
                <label class="control-label">年份</label>
                <div class="controls">
                    <form:input path="year" class="Wdate form-control input-medium"  readonly="true" onclick="WdatePicker({dateFmt:'yyyy', isShowClear:true});"></form:input>
                    <span>年</span>
                </div>
            </div>
        </div>
        <div class="search-btn-box">
            <button class="btn btn-primary" type="submit">查询</button>
            <button class="btn btn-primary" onclick="resall()" type="button">一键发布优秀项目</button>
        </div>
    </form:form>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th><input type="checkbox" onclick="selectAll(this)"></th>
            <th width="151" data-name="t.competition_number">
                <a class="btn-sort" href="javascript:void(0)"><span>大赛编号</span><i class="icon-sort"></i></a>
            </th>
            <th width="25%" data-name="t.p_name">
                <a class="btn-sort" href="javascript:void(0)"><span>项目名称</span><i class="icon-sort"></i></a>
            </th>
            <th data-name="f.name">
                <a class="btn-sort" href="javascript:void(0)"><span>学院</span><i class="icon-sort"></i></a>
            </th>
            <th>申报人</th>
            <th>组人数</th>
            <th>指导老师</th>
            <th>年份</th>
            <th>网评分</th>
            <th>路演分</th>
            <th>评级</th>
            <th>状态</th>
            <th width="160">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="gContest">
            <tr>
                <td><input type="checkbox" name="subck" value="${gContest.id}"></td>
                <td>${gContest.competitionNumber}</td>
                <td>
                    <a href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${gContest.auditCode}">${gContest.pName}</a>
                </td>
                <td>${gContest.collegeName}</td>
                <td>${gContest.declareName}</td>
                <td>${pj:getTeamNum(gContest.snames)}</td> <!--组人数-->
                <td><c:if test="${empty gContest.tnames}">-</c:if><c:if
                        test="${not empty gContest.tnames}">${gContest.tnames}</c:if></td>
                <td>${gContest.year}</td>
                <td>${gContest.schoolExportScore}</td>
                <td>${gContest.schoolluyanScore}</td>
                <td>
                    <c:if test="${gContest.auditCode =='7'}">
                        ${fns:getDictLabel(gContest.schoolendResult, "competition_college_prise", "")}
                    </c:if>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${gContest.auditCode!=null && gContest.auditCode!='7' && gContest.auditCode!='8'&& gContest.auditCode!='9'}">
                            <%--<a href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}" target="_blank">${gContest.auditState}</a>--%>
                            <a href="${ctx}/actyw/actYwGnode/designView/${gContest.auditCode}?groupId=${gContest.groupId}"
                               class="countNum" target="_blank">${gContest.auditState}</a>
                        </c:when>
                        <c:otherwise>
                            ${gContest.auditState}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${college == null}">
                        <a class="btn btn-primary btn-small"
                           href="${ctx}/gcontest/gContest/changeGcontest?gcontestId=${gContest.id}&state=${gContest.auditCode}"/>
                        变更
                        </a>
                    </c:if>
                    <%--<shiro:hasPermission name="excellent:gcontestShow:edit">--%>
                        <%--<a class="btn btn-primary btn-small"--%>
                           <%--href="${ctx}/excellent/gcontestShowForm?gcontestId=${gContest.id}">--%>
                            <%--展示--%>
                        <%--</a>--%>
                    <%--</shiro:hasPermission>--%>

                    <c:if test="${isAdmin == '1'}">
                    <a class="btn btn-small btn-default" href="${ctx}/gcontest/gContest/gcontestDelete?id=${gContest.id}"
                          onclick="return confirmx('会删除项目相关信息,确认要删除吗？', this.href)">删除</a>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
</body>
</html>