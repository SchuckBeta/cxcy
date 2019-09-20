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

            $('#sub_list_wrap', window.parent.document).find('li a').each(function () {
                var hrefReg = $(this).attr('href');

                if (hrefReg.indexOf("collegeExportScore") != -1) {
                    var count = "${pj:collegeExportCount()}";
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
                } else if (hrefReg.indexOf("schoolActAuditList") != -1) {
                    var count = "${pj:schoolActAuditList()}";
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
                } else if (hrefReg.indexOf("schoolEndAuditList") != -1) {
                    var count = "${pj:schoolEndAuditList()}";
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
                /*	top.$.jBox.tip('签收完成');*/
                location.reload();
            });
        }
        function undoCount() {
            var undoCount = $("#contentTable").find(".countNum").size();

            if (undoCount > 0) {
                $("#undoCount").html(undoCount);
                $("#undoCount").show();
            } else {
                $("#undoCount").hide();
            }
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar edit-bar-tag clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>${menuName}</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="searchForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/schoolActAuditList"
               method="post" class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
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
        </div>
        <div class="search-btn-box">
            <button class="btn btn-primary" type="submit">查询</button>
        </div>
    </form:form>
    <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-sort">
        <thead>
        <tr>
            <th width="151">大赛编号</th>
            <th width="25%">参赛项目名称</th>
            <th>学院</th>
            <th>申报人</th>
            <th>组人数</th>
            <th>组别</th>
            <th>大赛类别</th>
            <%--<th>融资情况</th>--%>
            <th>指导老师</th>
            <c:if test="${state!=null && state=='1'}">
                <th>学院专家评分</th>

            </c:if>
            <c:if test="${state!=null && state=='2'}">
                <th>学院专家评分</th>
                <th>学院排名</th>
            </c:if>
            <c:if test="${state!=null && state=='3'}">
                <th>学院得分</th>
                <th>学院排名</th>
            </c:if>
            <c:if test="${state!=null && state=='4'}">
                <th>学院专家评分</th>
            </c:if>
            <c:if test="${state!=null && state=='5'}">
                <th>学院排名</th>
                <th>学院排名</th>
                <th>学院网评得分</th>
            </c:if>
            <c:if test="${state!=null && state=='6'}">
                <th>网评得分</th>
                <th>路演得分</th>
                <th>评定结果</th>
            </c:if>
            <c:if test="${state!=null && state=='7'}">
                <th>网评得分</th>
                <th>路演得分</th>
                <th>评定结果</th>
            </c:if>
            <th>状态</th>
            <th width="110">操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="gContest">
            <tr>
                <td>${gContest.competitionNumber}</td>
                <td>
                    <a href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${gContest.auditCode}">
                       ${gContest.pName}</a>
                </td>
                <td>
                    ${gContest.collegeName}
                </td>
                <td>
                        ${gContest.declareName}
                </td>
                <td>
                        ${pj:getTeamNum(gContest.snames)}
                </td>
                <td>
                        ${fns:getDictLabel(gContest.level, "gcontest_level", "")}
                </td>
                <td>
                        ${fns:getDictLabel(gContest.type, "competition_net_type", "")}
                </td>
                    <%--<td>--%>
                    <%--${fns:getDictLabel(gContest.financingStat, "financing_stat", "")}--%>
                    <%--</td>--%>
                <td>
                        ${gContest.tnames}
                </td>
                <c:if test="${state!=null && state=='1'}">
                    <td>
                            ${gContest.gScore}
                    </td>

                </c:if>
                <c:if test="${state!=null && state=='2'}">
                    <td>
                            ${gContest.gScore}
                    </td>
                    <td>
                        <c:if test="${gContest.sort!=null && gContest.sort!=''}">
                            ${gContest.sort}
                        </c:if>
                    </td>
                </c:if>
                <c:if test="${state!=null && state=='3'}">
                    <td>
                            ${gContest.gScore}
                    </td>
                    <td>
                        <c:if test="${gContest.sort!=null && gContest.sort!=''}">
                            ${gContest.sort}
                        </c:if>
                    </td>
                </c:if>
                <c:if test="${state!=null && state=='4'}">
                    <td>
                            ${gContest.gScore}
                    </td>
                </c:if>
                <c:if test="${state!=null && state=='5'}">
                    <td>
                            ${gContest.collegeSort}
                    </td>
                    <td>
                            ${gContest.schoolSort}
                    </td>
                    <td>
                            ${gContest.gScore}
                    </td>
                </c:if>
                <c:if test="${state!=null && state=='6'}">
                    <td>
                            ${gContest.schoolScore}
                    </td>
                    <td>
                            ${gContest.schoolluyanScore}
                    </td>
                    <td>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='0'}">
                            不合格
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='1'}">
                            合格
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='2'}">
                            三等奖
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='3'}">
                            二等奖
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='4'}">
                            一等奖
                        </c:if>
                    </td>
                </c:if>
                <c:if test="${state!=null && state=='7'}">
                    <td>
                            ${gContest.schoolScore}
                    </td>
                    <td>
                            ${gContest.schoolluyanScore}
                    </td>
                    <td>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='0'}">
                            不合格
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='1'}">
                            合格
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='2'}">
                            三等奖
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='3'}">
                            二等奖
                        </c:if>
                        <c:if test="${gContest.schoolendResult!=null && gContest.schoolendResult=='4'}">
                            一等奖
                        </c:if>
                    </td>
                </c:if>
                <td>
                    <c:choose>
                        <c:when test="${gContest.auditCode!=null && gContest.auditCode == state  && gContest.isHave!=null && gContest.isHave!='1' && gContest.auditCode!='0' && gContest.auditCode!='7' && gContest.auditCode!='8'&& gContest.auditCode!='9'}">
                            <%-- <a target="_blank" class="countNum" href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}">${gContest.auditState}</a>--%>
                            <a href="${ctx}/actyw/actYwGnode/designView/${gContest.auditCode}?groupId=${gContest.groupId}"
                               class="countNum" target="_blank">${gContest.auditState}</a>
                        </c:when>
                        <c:otherwise>
                            ${gContest.auditState}
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:if test="${state!=null && state=='5'}">
                        <c:choose>
                            <c:when test="${gContest.auditCode=='5'}">
                                <c:choose>
                                    <c:when test="${gContest.assignee!=null&& gContest.assignee!=''&& gContest.assignee== userId}">
                                        <a class="btn btn-primary btn-small"
                                           href="${ctx}/gcontest/gContest/auditView?gcontestId=${gContest.id}"/>
                                        审核</a>
                                    </c:when>
                                    <c:when test="${gContest.assignee!=null&& gContest.assignee!=''&& gContest.assignee != userId}">
                                        已被其他人签收
                                        <a
                                           href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${gContest.auditCode}"/>
                                        查看
                                        </a>
                                    </c:when>
                                    <c:otherwise>
                                        <a class="btn btn-primary btn-small"
                                           href="javascript:void(0)"
                                           onclick="claim('${gContest.taskId}');">签收</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                <a class="btn btn-primary btn-small"
                                   href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${gContest.auditCode}"/>
                                查看
                                </a>
                            </c:otherwise>
                        </c:choose>
                    </c:if>
                        <%--<a class="btn btn-back-oe btn-primaryBack-oe btn-small"
                        href="${ctx}/gcontest/gContest/auditedView?gcontestId=${gContest.id}&state=${gContest.auditCode}"/>
                        查看
                        </a>--%>
                        <%-- <a class="btn btn-back-oe btn-primaryBack-oe btn-small" target="_blank"
                         href="${ctx}/act/task/processMap?procDefId=${gContest.taskDef}&proInstId=${gContest.taskIn}">查看</a> --%>
                </td>
            </tr>

        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
</body>
</html>