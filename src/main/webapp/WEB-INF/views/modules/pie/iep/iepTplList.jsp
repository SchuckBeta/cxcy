<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>模板导入导出管理</title>
	<!-- <meta name="decorator" content="default"/> -->
	<%@include file="/WEB-INF/views/include/backtable.jsp" %>
	<%@include file="/WEB-INF/views/include/treetable.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			var tpl = $("#treeTableTpl").html().replace(/(\/\/\<!\-\-)|(\/\/\-\->)/g,"");
			var data = ${fns:toJson(list)}, ids = [], rootIds = [];
			for (var i=0; i<data.length; i++){
				ids.push(data[i].id);
			}
			ids = ',' + ids.join(',') + ',';
			for (var i=0; i<data.length; i++){
				if (ids.indexOf(','+data[i].parentId+',') == -1){
					if ((','+rootIds.join(',')+',').indexOf(','+data[i].parentId+',') == -1){
						rootIds.push(data[i].parentId);
					}
				}
			}
			for (var i=0; i<rootIds.length; i++){
				addRow("#treeTableList", tpl, data, rootIds[i], true);
			}
			$("#treeTable").treeTable({expandLevel : 2});
		});
		var tplTypes = JSON.parse('${tplTypes}');
		var tplOtypes = JSON.parse('${tplOtypes}');
		var tplFtypes = JSON.parse('${tplFtypes}');
		var tplLevels = JSON.parse('${tplLevels}');
		function addRow(list, tpl, data, pid, root){
			var type = '';
			var operType = '';
			var ftype = '';
			var level = '';
			for (var i=0; i<data.length; i++){
				var row = data[i];
				if ((${fns:jsGetVal('row.parentId')}) == pid){
					var hasFgf = "-";
					var hasFile = "-";
					if(row.level == '30'){
						hasFgf = getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, row.hasFgf);
						hasFile = getDictLabel(${fns:toJson(fns:getDictList('yes_no'))}, row.hasFile);
					}
					if(row.type){
						for (var j = 0; j < tplTypes.length; j++) {
							if(row.type == tplTypes[j].key){
								type = tplTypes[j].name;
								break;
							}
						}
					}

					if(row.operType){
						for (var j = 0; j < tplOtypes.length; j++) {
							if(row.operType == tplOtypes[j].key){
								operType = tplOtypes[j].name;
								break;
							}
						}
					}

					if(row.ftype){
						for (var j = 0; j < tplFtypes.length; j++) {
							if(row.ftype == tplFtypes[j].key){
								ftype = tplFtypes[j].name;
								break;
							}
						}
					}

					if(row.level){
						for (var j = 0; j < tplLevels.length; j++) {
							if(row.level == tplLevels[j].key){
								level = tplLevels[j].name;
								break;
							}
						}
					}

					$(list).append(Mustache.render(tpl, {
						dict: {
							type: type,
							operType: operType,
							ftype: ftype,
							hasFile:hasFile,
							hasFgf:hasFgf,
							level:level,
						blank123:0}, pid: (root?0:pid), row: row
					}));
					addRow(list, tpl, data, row.id);
				}
			}
		}
	</script>
</head>
<body>
	<div class="mybreadcrumbs">
		<span>模板导入导出</span>
	</div>
	<div class="content_panel">
		<ul class="nav nav-tabs">
			<li class="active"><a href="${ctx}/iep/iepTpl/">模板导入导出列表</a></li>
			<shiro:hasPermission name="iep:iepTpl:edit"><li><a href="${ctx}/iep/iepTpl/form">模板导入导出添加</a></li></shiro:hasPermission>
		</ul>
		<form:form id="searchForm" modelAttribute="iepTpl" action="${ctx}/iep/iepTpl/" method="post" class="breadcrumb form-search">
			<ul class="ul-form">
				<li><label>名称：</label>
					<form:input path="name" htmlEscape="false" maxlength="255" class="input-medium"/>
				</li>
				<li><label>模板类型：</label>
					<form:select id="type" path="type" class="required">
	                    <form:option value="" label="--请选择--"/>
	                    <c:forEach var="cur" items="${tplTypes}">
	                        <c:if test="${iepTpl.type eq cur.key}">
	                            <option value="${cur.key}" selected="selected">${cur.name }</option>
	                        </c:if>
	                        <c:if test="${iepTpl.type ne cur.key}">
	                            <option value="${cur.key}">${cur.name }</option>
	                        </c:if>
	                    </c:forEach>
	                </form:select>
				</li>
				<%-- <li><label>级别：</label>
					<form:select id="level" path="level" class="required">
	                    <form:option value="" label="--请选择--"/>
	                    <c:forEach var="cur" items="${tplLevels}">
	                        <c:if test="${iepTpl.level eq cur.key}">
	                            <option value="${cur.key}" selected="selected">${cur.name }</option>
	                        </c:if>
	                        <c:if test="${iepTpl.level ne cur.key}">
	                            <option value="${cur.key}">${cur.name }</option>
	                        </c:if>
	                    </c:forEach>
	                </form:select>
				</li> --%>
				<li><label>操作类型：</label>
					<form:select id="operType" path="operType" class="required">
	                    <form:option value="" label="--请选择--"/>
	                    <c:forEach var="cur" items="${tplOtypes}">
	                        <c:if test="${iepTpl.operType eq cur.key}">
	                            <option value="${cur.key}" selected="selected">${cur.name }</option>
	                        </c:if>
	                        <c:if test="${iepTpl.operType ne cur.key}">
	                            <option value="${cur.key}">${cur.name }</option>
	                        </c:if>
	                    </c:forEach>
	                </form:select>
				</li>
				<li><label>文件类型：</label>
					<form:select id="ftype" path="ftype" class="required">
	                    <form:option value="" label="--请选择--"/>
	                    <c:forEach var="cur" items="${tplFtypes}">
	                        <c:if test="${iepTpl.ftype eq cur.key}">
	                            <option value="${cur.key}" selected="selected">${cur.name }</option>
	                        </c:if>
	                        <c:if test="${iepTpl.ftype ne cur.key}">
	                            <option value="${cur.key}">${cur.name }</option>
	                        </c:if>
	                    </c:forEach>
	                </form:select>
				</li>
				<li class="btns"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
				<li class="clearfix"></li>
			</ul>
		</form:form>
		<sys:message content="${message}"/>
		<table id="treeTable" class="table table-striped table-bordered table-condensed">
			<thead>
				<tr>
					<th>名称</th>
					<th>类型</th>
					<th>级别</th>
					<th>步骤</th>
					<th>操作</th>
					<th>文件</th>
					<th>路径</th>
					<th>错误路径</th>
					<th>处理附件</th>
					<th>处理分隔</th>
					<shiro:hasPermission name="iep:iepTpl:edit"><th>操作</th></shiro:hasPermission>
				</tr>
			</thead>
			<tbody id="treeTableList"></tbody>
		</table>
	</div>
	<div id="dialog-message" title="信息">
		<p id="dialog-content"></p>
	</div>
	<script type="text/template" id="treeTableTpl">
		<tr id="{{row.id}}" pId="{{pid}}">
			<td><a href="${ctx}/iep/iepTpl/form?id={{row.id}}">
				{{row.name}}
			</a></td>
			<td>
				{{dict.type}}
			</td>
			<td>
				{{dict.level}}
			</td>
			<td>
				{{row.step}}
			</td>
			<td>
				{{dict.operType}}
			</td>
			<td>
				{{dict.ftype}}
			</td>
			<td>
				{{dict.path}}
			</td>
			<td>
				{{dict.epath}}
			</td>
			<td>
				{{dict.hasFile}}
			</td>
			<td>
				{{dict.hasFgf}}
			</td>
			<shiro:hasPermission name="iep:iepTpl:edit"><td>
   				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/iep/iepTpl/form?id={{row.id}}">修改</a>
				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/iep/iepTpl/delete?id={{row.id}}" onclick="return confirmx('确认要删除该模板导入导出及所有子模板导入导出吗？', this.href)">删除</a>
				<a class="check_btn btn-pray btn-lx-primary" href="${ctx}/iep/iepTpl/form?parent.id={{row.id}}">添加下级</a>
			</td></shiro:hasPermission>
		</tr>
	</script>
</body>
</html>