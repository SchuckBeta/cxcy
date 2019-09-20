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
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
        function ajaxCheckConnect(id) {
            closeTip();
            $.ajax({
                type: 'post',
                url: '/a/dr/drEquipment/ajaxCheckConnect',
                dataType: 'json',
                data: {
                    "id": id
                },
                success: function (data) {
                    if (data.status) {
                        location.reload();
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        $(function() {
            $("#btn_open_close_door").on("click", function (e) {
                $.ajax({
                    type: 'get',
                    url: '/a/dr/drEquipment/openOrCloseEntranceGuards',
                    dataType: 'json',
                    success: function (data) {
                        if (data.status) {
                            alertx(data.msg);
                        }
                    }
                });
            });

            $("#contentTable").on("click", '.disBtn', function () {

                var _a = $(this);
                var epmentid = $(this).data("epmentid");
                var doorno = $(this).data("doorno");
                $.ajax({
                    type: 'get',
                    url: '/a/dr/drEquipmentRspace/openEntranceGuardDoor',
                    dataType: 'json',
					data:{epmentid:epmentid, doorNo:doorno},
                    success: function (data) {
                        if (data.status) {
                            location.reload();
                        }
                    }
                });
            });

            $("#contentTable").on("click", '.closeDoor', function () {

                var _a = $(this);
                var epmentid = $(this).data("epmentid");
                var doorno = $(this).data("doorno");
                console.log($(this), arguments, $(this).data("epmentid"), $(this).data("doorNo"));
                $.ajax({
                    type: 'get',
                    url: '/a/dr/drEquipmentRspace/closeEntranceGuardDoor',
                    dataType: 'json',
                    data:{epmentid:epmentid, doorNo:doorno},
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
    			dealing(dealdom, dealdisdom, '${ctx}/dr/drEquipmentRspace/ajaxDealing');
    		}, '${drDealTimerMax}')

    		dealdisTimer = setInterval(function() {
    			$(dealdisdom).prop("disabled", false);
    		}, '${drDealBtnTimerMax}')

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
                url : '${ctx}/dr/drEquipmentRspace/ajaxDealReset',
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
                        	curDom.parent().find(".disBtn").prop("disabled", false);
    					} else if (this.dealStatus == '1') {
                        	curDom.parent().find(".disBtn").prop("disabled", true);
    					} else if (this.dealStatus == '2') {
    						curDom.html("失败");
                        	curDom.parent().find(".disBtn").prop("disabled", false);
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
        <li><a href="${ctx}/dr/drEquipment/list">设备管理</a></li>
        <li class="active"><a href="${ctx}/dr/drEquipment/listByRoom">设备门管理</a></li>
    </ul>
    <div class="tab-content-default">
	    <form:form id="searchForm" modelAttribute="drEquipmentRspace" action="${ctx}/dr/drEquipment/listByRoom" method="post"
	               class="form-horizontal clearfix form-search-block">
	        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
	        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
	        <div class="col-control-group">
	            <div class="control-group">
	                <label class="control-label">房间/场地</label>
	                <div class="controls">
	                    <form:input path="rsname" htmlEscape="false" maxlength="64" class="input-medium"/>
	                </div>
	            </div>
	            <div class="control-group">
	                <label class="control-label">设备名称</label>
	                <div class="controls">
	                    <form:input path="epment.name" htmlEscape="false" maxlength="64" class="input-medium"/>
	                </div>
	            </div>
	            <%-- <div class="control-group">
					<label class="control-label">queryStr</label>
					<div class="controls">
						<form:input path="queryStr" htmlEscape="false" maxlength="64"
									class="input-medium" />
					</div>
				</div> --%>
	            <%-- <div class="control-group">
	                <label class="control-label">状态</label>
	                <div class="controls">
	                	<form:select path="dealStatus" class="input-medium">
	                        <form:option value="" label="--请选择--"/>
	                        <form:options items="${drCdstatuss}" itemLabel="name" itemValue="key"
	                                      htmlEscape="false"/>
	                    </form:select>
	                </div>
	            </div> --%>
	        </div>
	        <div class="search-btn-box">
	           	<!-- <button class="btn btn-primary disBtn" type="button" id="btn_open_close_door">一键开门/关门</button> -->
	            <button type="submit" class="btn btn-primary">查询</button>
	            <%-- <shiro:hasPermission name="dr:drEquipment:edit">
	                <a href="${ctx}/dr/drEquipment/form" class="btn btn-primary">添加</a>
	            </shiro:hasPermission> --%>

	        </div>
	    </form:form>
	    <sys:message content="${message}"/>
	    <table id="contentTable"
	           class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
	        <thead>
	        <tr>
	            <th>场地</th>
	            <th>房间/场地</th>
	            <th>类型</th>
	            <th>设备名称/编号/端口</th>
	            <th>门</th>
	            <th>处理状态</th>
	            <shiro:hasPermission name="dr:drEquipment:edit">
	                <th width="260">操作</th>
	            </shiro:hasPermission>
	        </tr>
	        </thead>
	        <tbody>
       		<c:set var="rws" value="0"></c:set>
	        <c:forEach items="${page.list}" var="drEquipmentRspace">
		        <c:forEach items="${page.list}" var="drErspace">
		        	<c:if test="${(drEquipmentRspace.rspace) eq (drErspace.rspace)}">
		       		<c:set var="rws" value="0"></c:set>
		        	</c:if>
		        </c:forEach>
	            <tr>
	                <td>
	                	${drEquipmentRspace.pnames}
	                </td>
	                <td>
	                	${drEquipmentRspace.rsname}
	                </td>
	                <td>
	                	${drEquipmentRspace.rstype.name}
	                </td>
	                <td title="${drEquipmentRspace.epment.no}">${drEquipmentRspace.epment.name}/端口${drEquipmentRspace.drNo}</td>
	                <td>
	                	<c:forEach items="${drKeys}" var="itemKey">
                            <c:if test="${drEquipmentRspace.drNo == itemKey.key}">
                                ${itemKey.name}
                            </c:if>
                        </c:forEach>
	                </td>
	                <td id="${drEquipmentRspace.id }">
	                	<c:forEach var="item" items="${drCdstatuss}">
	                        <c:if test="${drEquipmentRspace.dealStatus eq item.key}">${item.name }</c:if>
	                    </c:forEach>
	                    <c:if test="${drEquipmentRspace.dealStatus eq '1'}">
	                        <img src="${ctxImages}/loading-row.gif" width="20px" />
							<input type="hidden" class="dealId" value="${drEquipmentRspace.id }" />
						</c:if>
	                </td>
	                <shiro:hasPermission name="dr:drEquipment:edit">
	                    <td>
	                    	 <button class="btn btn-small btn-primary btn-small disBtn" data-doorno="${drEquipmentRspace.drNo}" data-epmentid="${drEquipmentRspace.epment.id}"
	                           type="button">开门</button>

							 <button class="btn btn-small btn-primary btn-small disBtn closeDoor" data-doorno="${drEquipmentRspace.drNo}" data-epmentid="${drEquipmentRspace.epment.id}"
							   	type="button">关门</button>
	                        <c:if test="${drEquipmentRspace.dealStatus eq '1'}">
	                    	 	<button class="btn btn-small btn-primary"
							   		onclick="ajaxDealReset('${drEquipmentRspace.id}')"
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