<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>测试管理</title>
	<meta name="decorator" content="default"/>
	<%@include file="/WEB-INF/views/include/supcan.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			su = supcan(AF, "Test", {
				ready: function(){ 
					// 加载数据
					page();
				},
				event: function(Event, p1, p2, p3, p4){
					if(Event == 'DblClicked'){
						edit();
					}
				}
			});
		});
		
		// 新建
		function add(){
			alertx("新建数据");
			location = "${ctx}/test/test/form";
		}
		
		// 修改
		function edit(){
			var id = su.getCellText("id");
			if (id != ""){
				location = "${ctx}/test/test/form?id=" + id;
				alertx("修改数据：id=" + id);
			}else{
				alertx("请选择一行数据！");
			}
		}
		
		//删除
		function dele(){
			var ids = su.getCellText("id", true);
			if (ids != ""){
				$.get("${ctx}/test/test/delete?id=" + ids, function(data){
					if (data == "true"){
						showTip("删除“" + su.getCellText("name", true) + "”成功。");
						page();
					}else{
						showTip("删除“" + su.getCellText("name", true) + "”失败！");
					}
				});
			}else{
				alertx("请选择一行或多行数据！");
			}
		}
		
		// 翻页
		function page(pageNo, pageSize){
			if (pageNo != ""){
				$("#pageNo").val(pageNo);
			}
			if (pageSize != ""){
				$("#pageSize").val(pageSize);
			}
			su.loadByForm("#searchForm", "#page");
        	return false;
        }
	</script>
</head>
<body>
<form:form id="inputForm" modelAttribute="seq" action="${ctx}/door/addSeq" method="post" class="form-horizontal">
	<form:hidden path="id"/>
	<sys:message content="${message}"/>
	<div class="control-group">
		<label class="control-label">名称:</label>
		<div class="controls">
			<form:input path="seqName" htmlEscape="false" maxlength="200" class="required"/>
		</div>
	</div>
	<div class="control-group">
			<label class="control-label">开始数值:</label>
			<div class="controls">
				<form:input path="startNum" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>
		<div class="control-group">
			<label class="control-label">每次递增数值:</label>
			<div class="controls">
				<form:input path="updateNum" htmlEscape="false" maxlength="200" class="required"/>
			</div>
		</div>


	<div class="form-actions">
		<shiro:hasPermission name="test:test:edit"><input id="btnSubmit" class="btn btn-primary" type="submit" value="保 存"/>&nbsp;</shiro:hasPermission>
		<input id="btnCancel" class="btn" type="button" value="返 回" onclick="history.go(-1)"/>
	</div>
</form:form>
<table id="contentTable"
          class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
       <thead>
       <tr>
           <th><input type="checkbox" id="check_all" data-flag="false"></th>
           <th width="25%">序列名称</th>
           <th style="display:none">序列编号</th>
           <th>序列递增</th>
       </tr>
       </thead>
       <tbody>
       <c:forEach items="${page.list}" var="seq">
           <tr>
               <td class="checkone"><input type="checkbox" /></td>
               <td>${seq.name} </td>
               <td>${seq.number}</td>
               <td>${seq.sponsor}</td>
           </tr>
       </c:forEach>
       </tbody>
   </table>
</body>
</html>
