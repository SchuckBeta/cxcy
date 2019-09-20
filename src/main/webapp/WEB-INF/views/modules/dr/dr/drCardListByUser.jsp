<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <style>
        .red{
            color: red;
            font-weight:bold;
            background-color: #eee;
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

        function ajaxCardActivateIds(dom, id) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}

            closeTip();
            $.ajax({
                type: 'post',
                url: '/a/dr/drCard/ajaxCardActivateIds',
                dataType: 'json',
                data: {
                    "ids": id
                },
                success: function (data) {
                    if (data.status) {
//                       showTip(data.msg, 'success', 1000, 0, function () {
                        location.reload();
//                       })
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        function ajaxCardLossIds(dom, id) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}

            $.ajax({
                type: 'post',
                url: '/a/dr/drCard/ajaxCardLossIds',
                dataType: 'json',
                data: {
                    "ids": id
                },
                success: function (data) {
                    if (data.status) {
//                        showTip(data.msg, 'success', 1000, 0, function () {
                        location.reload();
//                        })
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        function ajaxCardBackListIds(dom, id) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}

            $.ajax({
                type: 'post',
                url: '/a/dr/drCard/ajaxCardBackListIds',
                dataType: 'json',
                data: {
                    "ids": id
                },
                success: function (data) {
                    if (data.status) {
//                        showTip(data.msg, 'success', 1000, 0, function () {
                        location.reload();
//                        })
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        function ajaxCardBackIds(dom, id) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}

            $.ajax({
                type: 'post',
                url: '/a/dr/drCard/ajaxCardBackIds',
                dataType: 'json',
                data: {
                    "ids": id
                },
                success: function (data) {
                    if (data.status) {
//                        showTip(data.msg, 'success', 1000, 0, function () {
                        location.reload();
//                        })
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        function ajaxCardRepublishIds(dom, id) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}

            $.ajax({
                type: 'post',
                url: '/a/dr/drCard/ajaxCardRepublishIds',
                dataType: 'json',
                data: {
                    "ids": id
                },
                success: function (data) {
                    if (data.status) {
//                        showTip(data.msg, 'success', 1000, 0, function () {
                        location.reload();
//                        })
                    } else {
                        alertx(data.msg);
                    }
                }
            });
        }

        var dealTimer;
        var dealdisTimer;
    	$(function() {
    		var dealdom = ".dealId";
    		var dealdisdom = ".disBtn";
    		dealTimer = setInterval(function() {
    			dealing(dealdom, dealdisdom, '${ctx}/dr/drCard/ajaxDealing');
    		}, '${drDealTimerMax}')

    		dealdisTimer = setInterval(function() {
    			$(dealdisdom).attr("disabled", false);
    		}, '${drDealBtnTimerMax}')

    		console.info($(dealdom));
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
                url : '${ctx}/dr/drCard/ajaxDealReset',
                data : {
                    id : id
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
        function disConfirmxToHref(dom, href) {
        	var istrue = $(dom).prop("disabled");
        	if(istrue){
        		return;
        	}
    		return confirmx('确认要删除该门禁卡吗？', href);
        }

    	function dealing(doms, btnDoms, url) {
    		if (($(doms).length <= 0) || (!url)) {
    			clearInterval(dealTimer);
    			$(btnDoms).attr("disabled", false);
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
    						curDom.html(curDom.html().replace("处理中", "正常"));
    						curDom.parent().find(".disBtn").prop("disabled", false);
    					} else if (this.dealStatus == '1') {
    						curDom.parent().find(".disBtn").prop("disabled", true);
    					} else if (this.dealStatus == '2') {
    						curDom.html(curDom.html().replace("处理中", "失败"));
    						curDom.find("img").remove();
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
            <%--<span>门禁卡</span> <i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <ul class="nav nav-tabs nav-tabs-default">
        <li class="active"><a href="${ctx}/dr/drCard/listByUser?cardType=1">门禁卡管理</a></li>
        <li><a href="${ctx}/dr/drCard/listByTemp?cardType=0">临时卡管理</a></li>
    </ul>
    <div class="tab-content-default">
        <form:form id="searchForm" modelAttribute="drCard" action="${ctx}/dr/drCard/listByUser" method="post"
                   class="form-horizontal clearfix form-search-block">
            <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
            <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>

            <div class="col-control-group">
                <%-- <div class="control-group">
                    <label class="control-label">日期</label>
                    <div class="controls">

                        <input type="text" readonly
                               class="input-medium Wdate"
                               value="<fmt:formatDate value="${drCard.expiry}" pattern="yyyy-MM-dd"/>"
                               onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false})"/>
                    </div>
                </div> --%>
                <div class="control-group">
                    <label class="control-label">所属学院</label>
                    <div class="controls">
                        <form:select path="user.office.name" htmlEscape="false" maxlength="255" class="input-medium"
                                     id="collegeId">
                            <form:option value="" label="所有学院"/>
                            <form:options items="${fns:findColleges()}" itemLabel="name" itemValue="name"
                                          htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
                <%-- <div class="control-group">
                    <label class="control-label">入驻类型</label>
                    <div class="controls">
                            <form:select path="peInfo.etype" class="input-medium">
	                            <form:option value="" label="--请选择--"/>
	                            <form:options items="${pwEtypes}" itemLabel="name" itemValue="key"
	                                          htmlEscape="false"/>
	                        </form:select>
                    </div>
                </div> --%>
                <div class="control-group">
                    <label class="control-label">卡状态</label>
                    <div class="controls">
                        <form:select path="status" class="input-medium">
                            <form:option value="" label="--请选择--"/>
                            <form:options items="${drCstatuss}" itemLabel="name" itemValue="key"
                                          htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
                <%-- <div class="control-group">
                    <label class="control-label">学号/工号</label>
                    <div class="controls">
                        <form:input path="tempNo" htmlEscape="false" maxlength="64" class="input-medium"/>
                    </div>
                </div> --%>
                <div class="control-group">
                    <label class="control-label">卡号</label>
                    <div class="controls">
                        <form:input path="no" htmlEscape="false" maxlength="64" class="input-medium"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">姓名</label>
                    <div class="controls">
                        <form:input path="user.name" htmlEscape="false" maxlength="64" class="input-medium"/>
                            <%-- <sys:treeselect id="user" name="user.id" value="${drCard.user.id}" labelName="user.name"
                                            labelValue="${drCard.user.name}"
                                            title="用户" url="/sys/office/treeData?type=3" cssClass="input-small"
                                            allowClear="true"
                                            notAllowSelectParent="true"/> --%>
                    </div>
                </div>
            </div>
            <div class="search-btn-box">
                <button type="submit" class="btn btn-primary">查询</button>
            </div>

        </form:form>
        <sys:message content="${message}"/>
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
            <thead>
            <tr>
                <th>卡号</th>
                <th>学号/工号</th>
                <th>姓名</th>
                <th>入驻类型</th>
                <th>所属学院</th>
                <!-- <th>入驻日期</th> -->
                <th>入驻有效期</th>
                <!-- <th>入驻状态</th> -->
                <th>有效期</th>
                <th>卡状态</th>
                <%--<th>门禁卡卡号</th>--%>

                <!-- <th>开门次数</th>
                <th>特权</th>
                <th>节假日限制</th>
                <th>是否预警</th>
                <th>取消预警</th> -->
                <shiro:hasPermission name="dr:drCard:edit">
                    <th width="220">操作</th>
                </shiro:hasPermission>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="drCard">
            	<c:if test="${empty drCard.cardType }">
            		<c:set var="curCardType" value="1" />
            	</c:if>
            	<c:if test="${not empty drCard.cardType }">
            		<c:set var="curCardType" value="${drCard.cardType}"/>
            	</c:if>
                <tr>
                    <td><a href="${ctx}/dr/drCard/form?id=${drCard.id}&user.id=${drCard.user.id}&cardType=${curCardType}">
                        <c:if test="${empty drCard.no}">-</c:if>
                        <c:if test="${not empty drCard.no}">${drCard.no}</c:if>
                    </a>
                    </td>
                    <td>
                    	${drCard.user.no}
                    </td>
                    <td>${drCard.user.name}</td>
                    <td>
                        <c:if test="${empty drCard.peInfo.enterTypeStr}">-</c:if>
                        <c:if test="${not empty drCard.peInfo.enterTypeStr}">${drCard.peInfo.enterTypeStr}</c:if>
                    </td>
                    <td>
                        <c:if test="${empty drCard.user.office.name}">-</c:if>
                        <c:if test="${not empty drCard.user.office.name}">${drCard.user.office.name}</c:if>
                    </td>
                    <!-- <td>
                    	待完善
                    </td> -->
                    <td>
                        <c:if test="${empty drCard.peInfo.enterEndTime}">-</c:if>
                        <c:if test="${not empty drCard.peInfo.enterTypeStr}">${drCard.peInfo.enterEndTime}</c:if>
                    </td>
                    <td class="<c:if test="${drCard.isExpiry eq '0'}">red</c:if>">
                    	<fmt:formatDate value="${drCard.expiry}" pattern="yyyy-MM-dd HH:mm:ss"/>
                    </td>
                    <td id="${drCard.id}">
                        <c:if test="${(empty drCard.status) || (empty drCard.dealStatus)}">-</c:if>
                        <c:if test="${(not empty drCard.status) && (not empty drCard.dealStatus)}">
                            <c:if test="${drCard.dealStatus eq '0'}">
                                <c:forEach var="item" items="${drCstatuss}">
                                    <c:if test="${drCard.status eq item.key}">${item.sname }</c:if>
                                </c:forEach>
                            </c:if>
                            <c:if test="${drCard.dealStatus ne '0'}">
                                <c:forEach var="item" items="${drCstatuss}">
                                    <c:if test="${drCard.status eq item.key}">${item.name }</c:if>
                                </c:forEach>
                                <c:forEach var="item" items="${drCdstatuss}">
                                    <c:if test="${drCard.dealStatus eq item.key}">${item.name }</c:if>
                                </c:forEach>
                            </c:if>
                        </c:if>
                        <c:if test="${drCard.dealStatus eq '1'}">
	                        <img src="${ctxImages}/loading-row.gif" width="20px" />
							<input type="hidden" class="dealId" value="${drCard.id }" />
						</c:if>
                    </td>


                        <%-- <td>
                             <c:if test="${expireMin eq drCard.openTimes}">无</c:if>
                             <c:if test="${expireMax eq drCard.openTimes}">不限</c:if>
                             <c:if test="${(expireMin ne drCard.openTimes) && (expireMax ne drCard.openTimes)}">${drCard.openTimes}次</c:if>
                         </td>
                         <td>
                             <c:forEach var="item" items="${drAuths}">
                                 <c:if test="${drCard.privilege eq item.key}">${item.name }</c:if>
                             </c:forEach>
                         </td>
                         <td>
                                 ${fns:getDictLabel(drCard.holidayUse, 'true_false', '')}
                         </td>
                         <td>
                                 ${fns:getDictLabel(drCard.warnning, 'yes_no', '-')}
                         </td>
                         <td>
                                 ${fns:getDictLabel(drCard.isCancel, 'yes_no', '-')}
                         </td> --%>
                    <shiro:hasPermission name="dr:drCard:edit">
                        <td style="width: 20%; text-align: left;">
                            <c:if test="${empty drCard.status}">
								<button class="btn btn-primary btn-small disBtn"
								   onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&user.id=${drCard.user.id}&cardType=${curCardType}&secondName=开卡')"
								   type="button">开卡</button>
                            </c:if>
                            <c:if test="${(drCard.status eq '0')}">
                                <button class="btn btn-small btn-primary disBtn"
								   	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}&secondName=修改')"
                                   	type="button">修改</button>
                                <button class="btn btn-small btn-primary disBtn"
                                   	onclick="ajaxCardLossIds(this, '${drCard.id}')"
                                   	type="button">挂失</button>

                                <button class="btn btn-small btn-primary disBtn"
                                	onclick="ajaxCardBackIds(this, '${drCard.id}')"
                                   	type="button">退卡</button>
                                <%-- <button class="btn btn-small btn-primary disBtn"
                                	onclick="ajaxCardBackListIds(this, '${drCard.id}')"
                                   	type="button">黑名单</button> --%>
                            </c:if>
                            <c:if test="${(drCard.status eq '1')}">
                                <button class="btn btn-small btn-primary disBtn"
                                	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}&secondName=修改')"
                                   	type="button">修改</button>
                                <button class="btn btn-small btn-primary disBtn"
                                   	onclick="ajaxCardActivateIds(this, '${drCard.id}')"
                                   	type="button">激活</button>
                                <button class="btn btn-small btn-primary disBtn"
                                	onclick="ajaxCardBackIds(this, '${drCard.id}')"
                                   	type="button">退卡</button>
                                <%-- <button class="btn btn-small btn-primary disBtn"
                                	onclick="ajaxCardBackListIds(this, '${drCard.id}')"
                                	type="button">黑名单</button> --%>
                            </c:if>
                                <%-- <c:if test="${(drCard.status eq '2') && (drCard.dealStatus ne '1')}"> --%>
                            <c:if test="${(drCard.status eq '2')}">
                                <button class="btn btn-small btn-primary disBtn"
                                	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}&secondName=修改')"
                                   	type="button">修改</button>
                                <button class="btn btn-small btn-primary disBtn"
                                   	onclick="ajaxCardActivateIds(this, '${drCard.id}')"
                                   	type="button">激活</button>
                                <button class="btn btn-small btn-primary disBtn"
                                	onclick="ajaxCardBackIds(this, '${drCard.id}')"
                                   	type="button">退卡</button>
                            </c:if>
                            <c:if test="${(drCard.status eq '9')}">
                            	<%-- <button class="btn btn-small btn-primary disBtn"
                                	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}')"
                                   	type="button">修改</button> --%>
                                <button class="btn btn-small btn-primary disBtn"
                                   	onclick="ajaxCardRepublishIds(this, '${drCard.id}')"
                                   	type="button">重新发卡</button>
                                <button class="btn btn-default btn-small disBtn"
                                	onclick="disConfirmxToHref(this, '${ctx}/dr/drCard/delete?id=${drCard.id}&cardType=${curCardType}')"
                                   	type="button">删除</button>
                            </c:if>
	                        <c:if test="${drCard.dealStatus eq '1'}">
                            	<button class="btn btn-small btn-primary"
                                	onclick="ajaxDealReset('${drCard.id}')"
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