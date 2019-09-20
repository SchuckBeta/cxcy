<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>
<head>
	<title>学分规则详情</title>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
	<script type="text/javascript">
		$(document).ready(function() {
			//$("#name").focus();
			$("#inputForm").validate({
				submitHandler: function(form){
					loading('正在提交，请稍等...');
					form.submit();
				},
				errorContainer: "#messageBox",
				errorPlacement: function(error, element) {
					$("#messageBox").text("输入有误，请先更正。");
					if (element.is(":checkbox")||element.is(":radio")||element.parent().is(".input-append")){
						error.appendTo(element.parent().parent());
					} else {
						error.insertAfter(element);
					}
				}
			});
		});
	</script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>学分规则详情</span> <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="scoRuleDetail" action="${ctx}/scr/scoRuleDetail/save" method="post" class="form-horizontal">
			<form:hidden path="id"/>
			<sys:message content="${message}"/>
			<div class="control-group">
				<label class="control-label">	<i>*</i>规则编号：</label>
				<div class="controls">
					<form:input path="rid" htmlEscape="false" maxlength="64" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>名称：</label>
				<div class="controls">
					<form:input path="name" htmlEscape="false" maxlength="100" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	分值：</label>
				<div class="controls">
					<form:input path="score" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	最小分值:自定义分值不能低于该值：</label>
				<div class="controls">
					<form:input path="scMin" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	最大分值：自定义分值不能超过该值：</label>
				<div class="controls">
					<form:input path="scMax" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>权重,标准有互斥情况下，根据权重取分值:默认0：</label>
				<div class="controls">
					<form:input path="level" htmlEscape="false" maxlength="255" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否有证书：默认是：0、否；1、是：</label>
				<div class="controls">
					<form:input path="isCert" htmlEscape="false" maxlength="11" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否限制学分最值（最大、最小）：默认是：0、否；1、是：</label>
				<div class="controls">
					<form:input path="isLimitm" htmlEscape="false" maxlength="11" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否需要折半：默认是：0、否；1、是：</label>
				<div class="controls">
					<form:input path="isHalf" htmlEscape="false" maxlength="11" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	折半备注说明：需要折半时必填,便于对最终生成的分数不正确情况作原因说明：</label>
				<div class="controls">
					<form:input path="halfRemarks" htmlEscape="false" maxlength="1000" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>是否为参与类型：0：否 1：是，有次数上限(取join_max）；2、是：无次数上限（可无限累加）：</label>
				<div class="controls">
					<form:input path="joinType" htmlEscape="false" maxlength="1" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	<i>*</i>累加最大值：is_join=1时生效：</label>
				<div class="controls">
					<form:input path="joinMax" htmlEscape="false" maxlength="11" class=" required"/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	区间(60,}&gt;60 [50,70]50 									&lt;/td&gt;									&lt;td nowrap&gt;										&lt;input type=：</label>
				<div class="controls">
					<form:input path="condition" htmlEscape="false" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	区间名称：</label>
				<div class="controls">
					<form:input path="condName" htmlEscape="false" maxlength="255" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	条件校验类型：默认0：1、字典值(使用字典key）;2、区间值(）：</label>
				<div class="controls">
					<form:input path="condType" htmlEscape="false" maxlength="11" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	是否对执行条件校验：默认否：0、否；1、是：</label>
				<div class="controls">
					<form:input path="isCondcheck" htmlEscape="false" maxlength="11" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	认定单位、部门、认定对象所属机构：</label>
				<div class="controls">
					<form:input path="unit" htmlEscape="false" maxlength="1000" class=" "/>

				</div>
			</div>
			<div class="control-group">
				<label class="control-label">	备注：</label>
				<div class="controls">
					<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="1000" class="input-xxlarge "/>

				</div>
			</div>
			<div class="form-actions">
				<shiro:hasPermission name="scr:scoRuleDetail:edit"><button class="btn btn-primary" type="submit">保存</button></shiro:hasPermission>
				<button class="btn btn-default" type="button" onclick="history.go(-1)">返回</button>
			</div>
		</form:form>
</div>
</body>
</html>