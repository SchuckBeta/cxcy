<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%--<%@include file="/WEB-INF/views/include/backCommon.jsp" %>--%>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/backTable/sort.js"></script><!--排序js-->
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
        function expProject(){
        	var appName=$("[id='deuser.name']").val();
        	var pName=$("[id='pName']").val();
        	var competitionNumber=$("[id='competitionNumber']").val();
        
        	if(appName){
        		appName=encodeURI(encodeURI(appName));
        	}
        	if(pName){
        		pName=encodeURI(encodeURI(pName));
        	}
        	if(competitionNumber){
        		competitionNumber=encodeURI(encodeURI(competitionNumber));
        	}
        	var proCategory=$("[id='proCategory']").val();
        	
        	var setResultsArrs = new Array();
            $("input[name='setResults']:checkbox").each(function () {
                if ($(this).attr("checked")) {
                	setResultsArrs.push($(this).val());
                }
            });
            var setResults=setResultsArrs.join(",");
            
            var finalResultsArrs = new Array();
            $("input[name='finalResults']:checkbox").each(function () {
                if ($(this).attr("checked")) {
                	finalResultsArrs.push($(this).val());
                }
            });
            var finalResults=finalResultsArrs.join(",");
            var param="&appName="+appName+"&pName="+pName+"&competitionNumber="+competitionNumber
            +"&proCategory="+proCategory+"&setResults="+setResults+"&finalResults="+finalResults;
        	location.href="${ctx}/proprojectgt/expProject?actywId=${actywId}"+param;
        }
    </script>
    <style>
        .table > tbody > tr > th, .table > tbody > tr > td{
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>${menuName}</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="searchForm" modelAttribute="proModel" action="${ctx}/cms/form/queryMenuList" method="post"
               class="form-horizontal clearfix form-search-block">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <input type="hidden" id="orderBy" name="orderBy" value="${page.orderBy}"/>
        <input type="hidden" id="orderByType" name="orderByType" value="${page.orderByType}"/>
        <input id="actywId" name="actywId" type="hidden" value="${actywId}"/>


        <div class="col-control-group">
            <div class="control-group">
                <label class="control-label">申报人</label>
                <div class="controls">
                    <form:input class="input-medium" path="deuser.name" htmlEscape="false" maxlength="100"
                                autocomplete="off"
                                placeholder="申报人名称模糊搜索"/>
                </div>
            </div>
            <div class="control-group" >
                <label class="control-label">项目名称</label>
                <div class="controls">
                    <form:input class="input-medium" path="pName" htmlEscape="false" maxlength="100" autocomplete="off"
                                placeholder="项目名称模糊搜索"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">项目编号</label>
                <div class="controls">
                    <form:input class="input-medium" path="competitionNumber" htmlEscape="false" maxlength="100"
                                autocomplete="off"/>
                </div>
            </div>
            <div class="control-group" style="margin-right: 120px;">
                <label class="control-label">项目类型</label>
                <div class="controls">
                    <form:select path="proCategory" class="input-medium" cssStyle="width: 164px;">
                        <form:option value="" label="---请选择---"/>
                        <%--<form:options items="${fns:getDictList('project_type')}" itemLabel="name" itemValue="id"--%>
                        <%--htmlEscape="false"/>--%>
                        <form:options items="${fns:getProCategoryByActywId(proModel.actYwId)}" itemValue="value"
                                      itemLabel="label"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>


        <div class="col-control-group">

            <div class="control-group" style="margin-left:14px;">
                <label class="control-label">立项结果</label>
                <div class="controls controls-checkbox">
                    <form:checkboxes path="setResults" cssClass="checkbox-item" items="${fns:getDictList('0000000151')}" itemLabel="label" itemValue="value" htmlEscape="false" />
                </div>
            </div>
            <div class="control-group" style="margin-left:29px;">
                <label class="control-label">项目结果</label>
                <div class="controls">

                    <c:forEach items="${fns:getDictList('project_result')}" var="result">
                        <c:if test="${!fns:contains(result.label, '延期')}">
                        <label class="checkbox inline">
                            <input type="checkbox" name="finalResults" <c:if test="${fns:contains(proModel.finalResults, result.value)}">checked="checked"</c:if> value="${result.value}">
                            ${result.label}
                        </label>
                        </c:if>

                    </c:forEach>
                    <%--<form:checkboxes path="finalResults" cssClass="checkbox-item" items="${fns:getDictList('project_result')}" itemLabel="label" itemValue="value" htmlEscape="false" />--%>
                </div>
            </div>
        </div>


        <div class="search-btn-box">
            <button id="searchBtn" type="submit" class="btn btn-primary">查询</button>
            <button type="button" class="btn  btn-primary" onclick="expProject();">导出</button>
        </div>

    </form:form>
    <table id="contentTable"
           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-font14">
        <thead>
        <tr>
            <th data-name="a.competition_number"><a class="btn-sort" href="javascript:void(0);">项目编号<i
                    class="icon-sort"></i></a></th>
            <th data-name="a.p_name" style="width: 50%"><a class="btn-sort" href="javascript:void(0);">项目名称<i
                    class="icon-sort"></i></a></th>
            <th data-name="u.name"><a class="btn-sort" href="javascript:void(0);">申报人<i
                    class="icon-sort"></i></a></th>
            <th>项目类型</th>
            <th data-name="a.set_result"><a class="btn-sort" href="javascript:void(0);">立项结果<i
                    class="icon-sort"></i></a></th>
            <th data-name="a.final_result"><a class="btn-sort" href="javascript:void(0);">项目结果<i
                    class="icon-sort"></i></a></th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="proModel">
            <tr>
                <td>${proModel.competitionNumber}</td>
                <td style="word-break: break-all">
                    <a style="word-break: break-all" href="${ctx}/promodel/proModel/viewForm?id=${proModel.id}">${proModel.pName}</a>
                </td>
                <td>${fns:getUserById(proModel.declareId).name}</td>
                <td>
                        ${fns:getDictLabel(proModel.proCategory, "project_type", "")}
                </td>
                <td>
                        <c:if test="${proModel.setResult == '0'}">不通过</c:if>
                        <c:if test="${proModel.setResult == '1'}">通过</c:if>
                </td>
                <td>
                        ${fns:getDictLabel(proModel.finalResult, 'project_result', '')}
                </td>
                <td>

                    <c:set var="name" value="${pj:getProModelAuditNameById(proModel.procInsId)}" />
                    <a href="${ctx}/actyw/actYwGnode/designView?groupId=${groupId}&proInsId=${proModel.procInsId}&grade=${proModel.state}"
                       class="check_btn btn-pray btn-lx-primary" target="_blank">
                        <c:choose>
                            <c:when test="${not empty name}">
                                <c:choose>
                                    <c:when test="${proModel.state == '1'}">
                                        ${name}不通过
                                    </c:when>
                                    <c:otherwise>
                                        待${name}
                                    </c:otherwise>
                                </c:choose>
                            </c:when>
                            <c:otherwise>
                                已结项
                            </c:otherwise>
                        </c:choose>
                    </a>
                </td>
                <td>
                    <c:if test="${isAdmin == '1'}">
                        <a class="btn btn-small" href="${ctx}/promodel/proModel/promodelDelete?id=${proModel.id}"
                        onclick="return confirmx('会删除项目相关信息,确认要删除吗？', this.href)">删除</a>
                    </c:if>
                    <a class="btn btn-small btn-primary" href="${ctx}/promodel/proModel/viewForm?id=${proModel.id}">查看</a>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
<div class="content_panel">


</div>

</body>
</html>