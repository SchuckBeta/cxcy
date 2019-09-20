<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<form>
		    <h4>头像</h4><img src="${userInfo.photo }"></br>
			<label class="control-label">姓名：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.name }" maxlength="64"/>
		   <label class="control-label">性别：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.sex==0?'男':'女' }" maxlength="64"/>
		
			<label class="control-label">出生年月：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${userInfo.birthday }" type='date'/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
			
			
			<label class="control-label">证件类型：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.idType }" maxlength="64"/>
		
			<label class="control-label">国家地区：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.area }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
		
		<label class="control-label">名族：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.national }" maxlength="64"/>
		
			<label class="control-label">证件号：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.idNo }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
		
		
		<label class="control-label">联系地址：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="" maxlength="64"/>
		
			<label class="control-label">政治面貌：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.political }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
		
		
		
		<label class="control-label">电子邮箱：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.email }" maxlength="64"/>
		
			<label class="control-label">手机号：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.mobile }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
	
		
			
			<h3>在校情况</h3>
		<label class="control-label">院系名称：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.officeId }" maxlength="64"/>
		
			<label class="control-label">专业班级：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.tClass }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		
		
		<label class="control-label">学号/毕业年份：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.no}" maxlength="64"/><br/>
		
			<label class="control-label">在读学位：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.degree }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		
		<label class="control-label">学历：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.education }" maxlength="64"/>
		
			<label class="control-label">学位：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.degree }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
		
		<label class="control-label">毕业时间：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="<fmt:formatDate value="${userInfo.graduation }" type='date'/>" maxlength="64"/>
		
			<label class="control-label">休学时间：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${userInfo.temporaryDate }" type='date'/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		
		<label class="control-label">技术领域：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.domain }" maxlength="64"/>
			
		 <%-- <h3>项目经历</h3>
		     <span>项目类型:${userInfo.type }</span></br>
			<label class="control-label">项目周期：</label>
		   <fmt:formatDate value="${userInfo.beginDate }" type='date'/> 
		   <fmt:formatDate value="${userInfo.finalEndDate }" type='date'/></br>
		
			<label class="control-label">项目名称：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="<fmt:formatDate value="${userInfo.pname }" type='date'/>"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
			
			
			<label class="control-label">担任角色：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.sponsor }" maxlength="64"/><br/>
		
			<label class="control-label">项目评级：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.level }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/>
		
		<label class="control-label">项目结果：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.finalResult }" maxlength="64"/>	
			
			
			
			
	  <h3>大赛经历</h3>
		<label class="control-label">大赛类型：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="" maxlength="64"/>
		
			<label class="control-label">参赛时间：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value=""
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
		
		
		<label class="control-label">参赛项目名称：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.contestName}" maxlength="64"/>
		
			<label class="control-label">担任角色：</label>
				<input name="validDateStart" type="text" readonly="readonly" maxlength="20" class="input-medium Wdate "
					value="${userInfo.sponsor }"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss',isShowClear:false});"/><br/>
		
		<label class="control-label">获奖情况：</label>
		   <input id="name" name="name" class="input-xlarge " type="text" value="${userInfo.rewardSituation }" maxlength="64"/>
		
			 --%>
	</form>
</body>
</html>