<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <c:set var="type" value="7"></c:set>
    <script type="text/javascript">
        $(document).ready(function () {
            getExpInfo();
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
                /*	top.$.jBox.tip('签收完成');*/
                location.reload();
            });
        }
        function expData() {
            $(".expbtn").removeAttr("onclick", "expData()");
            $(".expbtn").attr("disabled", true);
            $.get('/a/proprojectmd/expPlus?actywId=${actywId}&gnodeId=${gnodeId}', {}, function (data) {
            //$.get('/a/proprojectmd/expPlus?type=${type}&actywId=${actywId}&gnodeId=${gnodeId}', {}, function (data) {
                if (data.ret == "1") {
                    $("#expTr").children('td').eq(1).html("[项目数]");
                    /* $("#expTr").children('td').eq(2).html("[文件]"); */
                    $("#expTr").children('td').eq(2).html("[开始时间]");
                    $("#expTr").children('td').eq(3).html("[状态]");
                    $("#expTr").children('td').eq(4).html("");
                    getExpInfo();
                } else {
                    alertx(data.msg);
                    $(".expbtn").attr("onclick", "expData()");
                    $(".expbtn").removeAttr("disabled");
                }
            });
        }
        function impData() {
            location.href = "/a/impdata/mdlist?type=${type}&actywId=${actywId}&gnodeId=${gnodeId}";
        }

        var isFirst = true;
        var exportTimer = null;
        function getExpInfo() {
            $.ajax({
                type: "GET",
                url: "/a/proprojectmd/getExpMidInfo",
                dataType: "json",
                success: function (data) {
                    if (data) {
                    	$("#expTable").attr("style", "display:");
                        if(data.total){
                            $("#expTr").children('td').eq(1).html("[项目数]" + data.total);
                    	}
                    	/* if(data.success){
                        	$("#expTr").children('td').eq(2).html("[文件]" + data.success);
                    	} */
                    	if(data.startDate){
                        	$("#expTr").children('td').eq(2).html("[开始时间]" + data.startDate);
                    	}
                        if (data.isComplete == '1' && data.sa) {
                            $("#expTr").children('td').eq(3).html("[状态]可下载");
                        }
                        if (data.isComplete == '1' && !data.sa) {
                            $("#expTr").children('td').eq(3).html("[状态]导出失败");
                        }
                        if (data.isComplete == '0') {
                            $("#expTr").children('td').eq(3).html("[状态]文件准备中...");
                        }
                        if (data.isComplete == '1' && data.sa) {
                            $("#expTr").children('td').eq(4).html("<a href='"+$frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + data.sa.url + "'>下载文件</a>");
                        }
                        if (data.isComplete == '1') {
                            $(".expbtn").attr("onclick", "expData()");
                            $(".expbtn").removeAttr("disabled");
                        } else {
                            //eval("it" + data.id + "=setInterval(function(){getExpInfoSub(data.id);},2000);");
                            if ((data.id != undefined)) {
                      			exportTimer = setInterval(function(){getExpInfoSub(data.id);},5000);
                        	}
                        }
                    } else {
                    	if(!isFirst){location.reload();}
                        $(".expbtn").attr("onclick", "expData()");
                        $(".expbtn").removeAttr("disabled");
                    }
                    isFirst = false;
                }
            });
        }
        function getExpInfoSub(id) {
            $.ajax({
                type: "GET",
                url: "/a/proprojectmd/getExpInfo",
                data: "eid=" + id,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    if (data) {
                    	$("#expTable").attr("style", "display:");
                        if(data.total){
                            $("#expTr").children('td').eq(1).html("[项目数]" + data.total);
                    	}
                    	/* if(data.success){
                        	$("#expTr").children('td').eq(2).html("[文件]" + data.success);
                    	} */
                    	if(data.startDate){
                        	$("#expTr").children('td').eq(2).html("[开始时间]" + data.startDate);
                    	}
                        if (data.isComplete == '1' && data.sa) {
                            $("#expTr").children('td').eq(3).html("[状态]可下载");
                        }
                        if (data.isComplete == '1' && !data.sa) {
                            $("#expTr").children('td').eq(3).html("[状态]导出失败");
                        }
                        if (data.isComplete == '0') {
                            $("#expTr").children('td').eq(3).html("[状态]文件准备中...");
                        }
                        if (data.isComplete == '1' && data.sa) {
                            $("#expTr").children('td').eq(4).html("<a href='"+$frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + data.sa.url + "'>下载文件</a>");
                        }
                        if (data.isComplete == '1') {
                            $(".expbtn").attr("onclick", "expData()");
                            $(".expbtn").removeAttr("disabled");
                            clearInterval(exportTimer);
                        }
                    } else {
                        $(".expbtn").attr("onclick", "expData()");
                        $(".expbtn").removeAttr("disabled");
                        clearInterval(exportTimer);
                    }
                },
                error: function (msg) {
                    $(".expbtn").attr("onclick", "expData()");
                    $(".expbtn").removeAttr("disabled");
                    clearInterval(exportTimer);
                }
            });
        }
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
            <span>中期审核</span>
            <i class="line weight-line"></i>
        </div>
    </div>

    <form:form id="searchForm" modelAttribute="act" action="${actionUrl}" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input id="gnodeId" name="gnodeId" type="hidden" value="${gnodeId}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>
        <shiro:hasPermission name="sys:user:import">
            <div class="text-right mgb-20">
                <button class="btn btn-primary expbtn" disabled onclick="expData()" type="button">导出</button>
                <button class="btn  btn-primary" onclick="impData()" type="button">导入</button>
            </div>
            <div class="search-form-wrap">
                <table id="expTable"
                       class="table table-bordered table-condensed table-hover  table-sort table-theme-default table-center"
                       style="display:none">
                    <tbody>
                    <tr id="expTr">
                        <td>
                            民大项目中期.zip
                        </td>
                        <td>
                            [项目数]
                        </td>
                        <!-- <td>
                            [文件]
                        </td> -->
                        <td>
                            [开始时间]
                        </td>
                        <td>
                            [状态]
                        </td>
                        <td>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </shiro:hasPermission>
    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-sort table-nowrap">
        <thead>
        <tr>
            <th width="120">项目编号</th>
            <th width="28%">参赛项目名称</th>
            <th>申报人</th>
            <th>申报类别</th>
            <th>申报级别</th>
            <!-- <th>组成员</th>
            <th>指导老师</th> -->

            <th>所属学院</th>
            <th>年份</th>
            <th>状态</th>
            <%--<th>操作</th>--%>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proModelMd">
            <%-- <c:set var="proModelMd" value="${fns:getProModelMdById(act.vars.map.id)}" />--%>
            <tr>
                <td>
                        ${proModelMd.proModel.competitionNumber}
                </td>
                <td><a href="${ctx}/promodel/proModel/viewForm?id=${proModelMd.proModel.id}">${proModelMd.proModel.pName}</a></td>
                <td>${fns:getUserById(proModelMd.proModel.declareId).name}</td>
                <td>${fns:getDictLabel(proModelMd.proModel.proCategory, "project_type", "")}</td>
                <td>${fns:getDictLabel(proModelMd.appLevel, "0000000196", "")}</td>
                <td>${fns:getUserById(proModelMd.proModel.declareId).office.name}</td>
                <td>${proModelMd.proModel.year}</td>
                <td>
                    <c:choose>
                        <c:when test="${proModelMd.midState!=null &&proModelMd.midState=='1'}">
                            通过
                        </c:when>
                        <c:when test="${proModelMd.midState!=null &&proModelMd.midState=='0'}">
                            不通过
                        </c:when>
                        <c:otherwise>
                            <a href="${ctx}/actyw/actYwGnode/designView?groupId=${auditGonde.group.id}&proInsId=${proModelMd.proModel.procInsId}" target="_blank">待审核</a>
                        </c:otherwise>
                    </c:choose>
                </td>
                <%--<td>--%>
                    <%--<a class="btn btn-back-oe btn-primaryBack-oe btn-small"--%>
                       <%-->查看</a>--%>
                <%--</td>--%>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    ${page.footer}
</div>
</body>
</html>