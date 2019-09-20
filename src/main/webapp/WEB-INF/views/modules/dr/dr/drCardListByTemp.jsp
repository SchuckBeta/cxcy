<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxJs}/cyjd/checkall.js"></script>
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
                	"ids": id,
                	"cardType": '0'
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


    		//initdis(dealdom, dealdisdom);
    		dealdisTimer = setInterval(function() {
    			$(dealdisdom).attr("disabled", false);
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
						var curDom = $("input[value=" + this.id + "][class='dealId']").parent();
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


    	function toImpPage() {
    		location.href = "${ctx}/impdata/impDrcardList";
        }


        $(function () {

            function isTempCardIsDisabled() {
                return !checkAllInstance.someChecked();
            }


            $('button[data-toggle="tempCard"]').prop('disabled', isTempCardIsDisabled());

            $('input[data-toggle="checkall"]').change(function () {
                $('button[data-toggle="tempCard"]').prop('disabled', isTempCardIsDisabled());
            })

            $('input[data-toggle="checkitem"]').change(function () {
                $('button[data-toggle="tempCard"]').prop('disabled', isTempCardIsDisabled());
            })


        })



        function toDrCardPlForm() {
            location.href = "${ctx}/dr/drCard/formPl?user.id=${user.id}&ids="+ checkAllInstance.getCheckedIds().join(',');
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
        <li><a href="${ctx}/dr/drCard/listByUser?cardType=1">门禁卡管理</a></li>
        <li class="active"><a href="${ctx}/dr/drCard/listByTemp?cardType=0">临时卡管理</a></li>
    </ul>
    <div class="tab-content-default">
        <form:form id="searchForm" modelAttribute="drCard" action="${ctx}/dr/drCard/listByTemp" method="post"
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
                    <%-- <div class="control-group">
                        <label class="control-label">特权</label>
                        <div class="controls">
                            <form:select path="privilege" class="input-medium">
                                <form:option value="" label="--请选择--"/>
                                <form:options items="${drAuths}" itemLabel="name" itemValue="key"
                                              htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">是否预警</label>
                        <div class="controls">
                            <form:select path="warnning" class="input-medium">
                                <form:option value="" label="--请选择--"/>
                                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                              htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">取消预警</label>
                        <div class="controls">
                            <form:select path="isCancel" class="input-medium">
                                <form:option value="" label="--请选择--"/>
                                <form:options items="${fns:getDictList('yes_no')}" itemLabel="label" itemValue="value"
                                              htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div> --%>
            </div>
            <div class="search-btn-box">
                <button type="submit" class="btn btn-primary">查询</button>
                <button id="impBtn" type="button" class="btn btn-primary" onclick="toImpPage()">导入</button>
                <a href="${ctx}/dr/drCard/form?cardType=0" class="btn btn-primary">开临时卡</a>
                <button type="button" disabled data-toggle="tempCard" class="btn btn-primary"  onclick="toDrCardPlForm()">批量授权</button>
            </div>

        </form:form>
        <sys:message content="${message}"/>
        <table id="contentTable"
               class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
            <thead>
            <tr>
                <th>
                	<input type="checkbox" data-toggle="checkall"  name="allCheck">
                </th>
                <th>卡号</th>
                <th>学号/工号</th>
                <th>姓名</th>
                <th>手机号</th>
                <th>有效期</th>
                <th>卡状态</th>
                <shiro:hasPermission name="dr:drCard:edit">
                    <th width="220">操作</th>
                </shiro:hasPermission>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${page.list}" var="drCard">
            	<c:if test="${empty drCard.cardType }">
            		<c:set var="curCardType" value="0" />
            	</c:if>
            	<c:if test="${not empty drCard.cardType }">
            		<c:set var="curCardType" value="${drCard.cardType}" />
            	</c:if>
                <tr>
                    <td>
                    	<c:if test="${(drCard.dealStatus ne '1') && (not empty drCard.no)}"><input type="checkbox" data-toggle="checkitem" value="${drCard.id}"></c:if>
                    </td>
                    <td><a href="${ctx}/dr/drCard/form?id=${drCard.id}&user.id=${drCard.user.id}&cardType=${curCardType}">
                        <c:if test="${empty drCard.no}">-</c:if>
                        <c:if test="${not empty drCard.no}">${drCard.no}</c:if>
                    </a>
                    </td>
                    <td>
                        ${drCard.tmpNo}
                    </td>
                    <td>${drCard.tmpName}</td>
                    <td>${drCard.tmpTel}</td>
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
                    <shiro:hasPermission name="dr:drCard:edit">
                        <td style="width: 20%; text-align: left;">
                            <c:if test="${empty drCard.status}">
								<button class="btn btn-primary btn-small disBtn"
								   onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&user.id=${drCard.user.id}&cardType=${curCardType}')"
								   type="button">开卡</button>
                            </c:if>
                            <c:if test="${(drCard.status eq '0')}">
                                <button class="btn btn-small btn-primary disBtn"
								   	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}')"
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
                                	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}')"
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
                                	onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&cardType=${curCardType}')"
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
                            <c:if test="${(drCard.status eq '10')}">
                            	<button class="btn btn-primary btn-small disBtn"
								   onclick="disableToHref(this, '${ctx}/dr/drCard/form?id=${drCard.id}&user.id=${drCard.user.id}&cardType=${curCardType}')"
								   type="button">开卡</button>
                                <button class="btn btn-default btn-small disBtn"
                                	onclick="disConfirmxToHref(this, '${ctx}/dr/drCard/delete?id=${drCard.id}&cardType=${curCardType}')"
                                   	type="button">删除</button>
                            </c:if>
                            <c:if test="${drCard.canTozs}">
                            	<button class="btn btn-primary btn-small disBtn"
								   onclick="disableToHref(this, '${ctx}/dr/drCard/ajaxStatusZs?id=${drCard.id}&cardType=1')"
								   type="button">转正式卡</button>
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