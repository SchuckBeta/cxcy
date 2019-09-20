<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title></title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <style type="text/css">
        .table .team-name{
            width: 160px;
            min-width: 160px;
            max-height: 40px;
            margin: 0 auto;
            text-align: center;
            display: -webkit-box;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            line-height: 20px;
            overflow: hidden;
        }
        .table .team-number{
            width: 100px;
            max-width: 174px;
            height: 20px;
            margin: 0 auto;
            text-align: center;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        @media (min-width: 1200px) {
            .table .team-name{
                width:auto;
            }
        }
        @media (min-width: 1100px) {
            .table .team-number{
                width: 174px;
            }
        }
    </style>
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
        function expData() {
            $.get('/a/proprojectmd/checkExpAll?actywId=' + $("#actywId").val(), {}, function (data) {
                if (data.ret == "0") {
                    confirmx("没有需要导出的数据，继续导出？", function () {
                        location.href = "/a/proprojectmd/expAll?actywId=" + $("#actywId").val();
                    });
                } else {
                    location.href = "/a/proprojectmd/expAll?actywId=" + $("#actywId").val();
                }
            });
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
            confirmx("确定发布所选项目到门户网站？", function () {
                $.ajax({
                    type: 'post',
                    url: '/a/excellent/resall',
                    dataType: "json",
                    data: {
                        fids: temarr.join(",")
                    },
                    success: function (data) {
                        if (data) {
                            alertx(data.msg);
                        }
                    }
                });
            });
        }
        function subckchange(ob) {
            if (!$(ob).attr("checked")) {
                $("#selectAllbtn").attr("checked", false);
            }
            /* if($("input[name='subck']:checked").length==0){
             $("#resallbtn").removeAttr("onclick");
             }else{
             $("#resallbtn").attr("onclick","resall()");
             } */
        }
    </script>
</head>
<body>
<div class="container-fluid container-fluid-oe">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>${menuName}</span>
            <i class="line weight-line"></i>
        </div>
    </div>
</div>
<div class="content_panel">
    <form:form id="searchForm" modelAttribute="proModel" action="${ctx}/cms/form/queryMenuList" method="post"
               class="form-inline form-content-box">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
    </form:form>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover  table-sort table-theme-default table-center">
        <thead>
        <tr>
            <th><input type="checkbox" id="selectAllbtn" onclick="selectAll(this)"></th>
            <th>项目编号</th>
            <th>参赛项目名称</th>
            <th style="white-space: nowrap">申报人</th>
            <th style="white-space: nowrap">项目类型</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proModelMd">
            <tr>
                <td><input type="checkbox" name="subck" onclick="subckchange(this)" value="${proModelMd.proModel.id}"></td>
                <td><p class="team-number">${proModelMd.proModel.competitionNumber}</p></td>
                <td><p class="team-name">
                    <a href="${ctx}/promodel/proModel/viewForm?id=${proModelMd.proModel.id}">${proModelMd.proModel.pName}</a>
                </p></td>
                <td>${fns:getUserById(proModelMd.proModel.declareId).name}</td>
                <td>
                        ${fns:getDictLabel(proModelMd.proModel.proCategory, "project_type", "")}
                </td>
                <td>

                        <c:choose>
                            <c:when test="${proModelMd.closeState!=null }">
                                <c:if test="${proModelMd.closeState=='0' }">
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}&grade=1" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                    结项审核不通过
                                </a>
                                </c:if>
                                <c:if test="${proModelMd.closeState=='1' }">
                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                    结项审核通过
                                </a>
                                </c:if>
                            </c:when>
                            <c:otherwise>
                                <c:choose>
                                    <c:when test="${proModelMd.midState!=null }">
                                        <c:if test="${proModelMd.midState=='0' }">
                                            <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}&grade=1" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                中期检查不通过
                                            </a>
                                            </c:if>
                                            <c:if test="${proModelMd.midState=='1' }">
                                            <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                中期检查通过
                                            </a>
                                        </c:if>
                                    </c:when>
                                    <c:otherwise>
                                        <c:choose>
                                            <c:when test="${proModelMd.setState!=null }">
                                                <c:if test="${proModelMd.setState=='0' }">
                                                    <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}&grade=1" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                        立项审核不通过
                                                    </a>
                                                    </c:if>
                                                    <c:if test="${proModelMd.setState=='1' }">
                                                    <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                        立项审核通过
                                                    </a>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${ proModelMd.proModel.procInsId}" class="check_btn btn-pray btn-lx-primary" target="_blank">
                                                    ${pj:getProModelAuditNameById(proModelMd.proModel.procInsId)}
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:otherwise>
                                </c:choose>
                            </c:otherwise>
                        </c:choose>
                </td>
                <td style="white-space: nowrap">
                    <a href="${ctx}/promodel/proModel/viewForm?id=${proModelMd.proModel.id}">查看</a>
                   	<a href="${ctx}/proprojectmd/proModelMd/toModifyPage?proModelId=${proModelMd.proModel.id}">变更</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
</body>
</html>