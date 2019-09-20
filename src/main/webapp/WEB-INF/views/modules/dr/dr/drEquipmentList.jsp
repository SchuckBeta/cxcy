<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>${backgroundTitle}</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp"%>

	<script type="text/javascript">


        $(document).ready(function() {
            $("#ps").val($("#pageSize").val());
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }

        function ajaxCheckConnect(dom, id) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}

            closeTip();
            var _this = $(this);
            $.ajax({
                type : 'post',
                url : '/a/dr/drEquipment/ajaxCheckConnect',
                dataType : 'json',
                data : {
                    "id" : id
                },
                success : function(data) {
                    if (data.status) {
                        location.reload();
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        $(function() {
            $("#btn_open_door").on("click", function (e) {
                $.ajax({
                    type: 'get',
                    url: '/a/dr/drEquipment/openEntranceGuards',
                    dataType: 'json',
                    success: function (data) {
                        if (data.status) {
                            location.reload();
                        }
                    }
                });
            });

            $("#btn_close_door").on("click", function (e) {
                $.ajax({
                    type: 'get',
                    url: '/a/dr/drEquipment/closeEntranceGuards',
                    dataType: 'json',
                    success: function (data) {
                        if (data.status) {
                            location.reload();
                        }
                    }
                });
            });
        });

        var dealTimer;
        var dealdisTimer;
        $(function() {
            var dealdom = ".dealId";
            var dealdisdom = ".disBtn";
            dealTimer = setInterval(function() {
                dealing(dealdom, dealdisdom, '${ctx}/dr/drEquipment/ajaxDealing');
            }, '${drDealTimerMax}')

            $(dealdom).each(function () {
            	$(this).parent().parent().find(dealdisdom).prop("disabled", true);
            });
        });

        function ajaxDealReset(id) {
        	if ((id == "") || (id == null)) {
                return;
            }

        	$.ajax({
                type : 'POST',
                url : '${ctx}/dr/drEquipment/ajaxDealReset',
                data : {
                    ids : id
                },
                success : function(res) {
					$("#" + id).html("重置成功");
                }
            });
        }
        function disableToHref(dom, href) {
        	var istrue = $(dom).attr("disabled");
        	if(istrue){
        		return;
        	}
        	self.location.href = href;
        }
        function disConfirmxToHref(dom, href, msg) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}
    		return confirmx(msg, href);
        }

        function dealing(doms, btnDoms, url) {
            if (($(doms).length <= 0) || (!url)) {
                clearInterval(dealTimer);
    			$(btnDoms).prop("disabled", false);
                return;
            }
            var ids = "";
            var i = 0;
            $(doms).each(function() {
                if (i == 0) {
                    ids += $(this).val();
                } else {
                    ids += ",";
                    ids += $(this).val();
                }
                i++;
            });

            if (ids == "") {
                return;
            }

            $.ajax({
                type : 'POST',
                url : url,
                data : {
                    ids : ids
                },
                success : function(res) {
                    $(res.datas).each(function() {
						var curDom = $("input[value=" + this.id + "]").parent();
                        if (this.dealStatus == '0') {
                            curDom.html("正常");
                        	curDom.parent().find(btnDoms).prop("disabled", false);
                        } else if (this.dealStatus == '1') {
                        	curDom.parent().find(btnDoms).prop("disabled", true);
                        } else if (this.dealStatus == '2') {
                            curDom.html("失败");
                        	curDom.parent().find(btnDoms).prop("disabled", false);
                        }
                    });
                }
            });
        }
	</script>
</head>
<body>
<div class="container-fluid">
	<%--<div class="edit-bar clearfix">--%>
	<%--<div class="edit-bar-left">--%>
	<%--<span>门禁设备</span> <i class="line weight-line"></i>--%>
	<%--</div>--%>
	<%--</div>--%>
	<%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
	<ul class="nav nav-tabs nav-tabs-default">
		<li class="active"><a href="${ctx}/dr/drEquipment/list">设备管理</a></li>
		<li><a href="${ctx}/dr/drEquipment/listByRoom">设备门管理</a></li>
	</ul>
	<div class="tab-content-default">
		<form:form id="searchForm" modelAttribute="drEquipment"
				   action="${ctx}/dr/drEquipment/" method="post"
				   class="form-horizontal clearfix form-search-block">
			<input id="pageNo" name="pageNo" type="hidden"
				   value="${page.pageNo}" />
			<input id="pageSize" name="pageSize" type="hidden"
				   value="${page.pageSize}" />
			<div class="col-control-group">
				<div class="control-group">
					<label class="control-label">设备号</label>
					<div class="controls">
						<form:input path="no" htmlEscape="false" maxlength="64"
									class="input-medium" />
					</div>
				</div>
				<%-- <div class="control-group">
					<label class="control-label">queryStr</label>
					<div class="controls">
						<form:input path="queryStr" htmlEscape="false" maxlength="64"
									class="input-medium" />
					</div>
				</div> --%>
				<div class="control-group">
					<label class="control-label">设备名称</label>
					<div class="controls">
						<form:input path="name" htmlEscape="false" maxlength="64"
									class="input-medium" />
					</div>
				</div>
				<div class="control-group">
					<label class="control-label">IP地址</label>
					<div class="controls">
						<form:input path="ip" htmlEscape="false" maxlength="64"
									class="input-medium" />
					</div>
				</div>
				<%-- <div class="control-group">
					<label class="control-label">状态</label>
					<div class="controls">
						<form:select path="dealStatus" class="input-medium">
							<form:option value="" label="--请选择--" />
							<form:options items="${drCdstatuss}" itemLabel="name"
										  itemValue="key" htmlEscape="false" />
						</form:select>
					</div>
				</div> --%>
			</div>
			<div class="search-btn-box">
				<button type="submit" class="btn btn-primary">查询</button>
				<shiro:hasPermission name="dr:drEquipment:edit">
					<a href="${ctx}/dr/drEquipment/form?secondName=添加" class="btn btn-primary">添加</a>
				</shiro:hasPermission>
				<button class="btn btn-primary disBtn" type="button" id="btn_open_door">一键开门</button>
				<button class="btn btn-primary disBtn" type="button" id="btn_close_door">一键关门</button>
			</div>
		</form:form>
		<sys:message content="${message}" />
		<table id="contentTable"
			   class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
			<thead>
			<tr>
				<th>设备号</th>
				<th>设备名称</th>
				<th>IP地址</th>
				<th>端口号</th>
				<th>设备出口编号</th>
				<th>处理状态</th>
				<th>禁用状态</th>
				<shiro:hasPermission name="dr:drEquipment:edit">
					<th width="300">操作</th>
				</shiro:hasPermission>
			</tr>
			</thead>
			<tbody>
			<c:forEach items="${page.list}" var="drEquipment">
				<tr>
					<td><a
							href="${ctx}/dr/drEquipment/form?id=${drEquipment.id}">
							${drEquipment.no} </a></td>
					<td>${drEquipment.name}</td>
					<td>${drEquipment.ip}</td>
					<td>${drEquipment.port}</td>
					<td><c:forEach items="${drEquipment.drNoList}" var="item">
						<c:forEach items="${drKeyList}" var="itemKey">
							<c:if test="${item == itemKey.key}">
								${itemKey.name}
							</c:if>
						</c:forEach>
					</c:forEach></td>
					<td id="${drEquipment.id }">
						<c:forEach var="item" items="${drCdstatuss}">
							<c:if test="${drEquipment.dealStatus eq item.key}">${item.name }</c:if>
						</c:forEach>
						<c:if test="${drEquipment.dealStatus eq '1'}">
	                        <img src="${ctxImages}/loading-row.gif" width="20px" />
							<input type="hidden" class="dealId" value="${drEquipment.id }" />
						</c:if>
					</td>
					<td>
						<c:if test="${drEquipment.delFlag eq '1'}">
							已禁用
						</c:if>
						<c:if test="${drEquipment.delFlag eq '0'}">
							使用中
						</c:if>
					</td>
					<shiro:hasPermission name="dr:drEquipment:edit">
						<td>
							<button class="btn btn-small btn-primary btn-small disBtn"
							   onclick="ajaxCheckConnect(this, '${drEquipment.id}')"
							   type="button">测试连接</button>
							<button class="btn btn-small btn-primary disBtn"
							   onclick="disableToHref(this, '${ctx}/dr/drEquipment/formSet?id=${drEquipment.id}')"
							   type="button">配置</button>

							<button class="btn btn-small btn-primary disBtn"
							   onclick="disableToHref(this, '${ctx}/dr/drEquipment/form?id=${drEquipment.id}')"
							   type="button">修改</button>
						   	<c:if test="${drEquipment.delFlag eq '1'}">
								<button class="btn btn-small btn-default disBtn"
									onclick="disConfirmxToHref(this, '${ctx}/dr/drEquipment/delete?id=${drEquipment.id}&delFlags=0', '确认要使用门禁设备吗？')"
									type="button">使用</button>
								<button class="btn btn-small btn-default disBtn"
								onclick="disConfirmxToHref(this, '${ctx}/dr/drEquipment/delete?id=${drEquipment.id}', '确认要删除门禁设备吗？')"
								type="button">删除</button>
							</c:if>
							<c:if test="${drEquipment.delFlag eq '0'}">
								<button class="btn btn-small btn-default disBtn"
									onclick="disConfirmxToHref(this, '${ctx}/dr/drEquipment/delete?id=${drEquipment.id}&delFlags=1', '确认要禁用门禁设备吗？')"
									type="button">禁用</button>
							</c:if>
							<c:if test="${drEquipment.dealStatus eq '1'}">
		                        <button class="btn btn-small btn-primary"
								   onclick="ajaxDealReset('${drEquipment.id}')"
								   type="button">重置</button>
							</c:if>
						</td>
					</shiro:hasPermission>
				</tr>
			</c:forEach>
			</tbody>
		</table>
	</div>
	${page.footer}
</div>


</body>
</html>