<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>结项报告查看</title>
	<meta charset="UTF-8">
	<meta name="decorator" content="creative"/>
	<link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
	<%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css" />--%>
	<script type="text/javascript" src="${ctxCommon}/common-js/datepicker/My97DatePicker/WdatePicker.js"></script>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/projectForm.css" />
	<script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
	<%--<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>--%>
	<link rel="stylesheet" href="${ctxCss}/frontCyjd/frontBime.css">
	<script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
	<script type="text/javascript">

		var ratio = "";
		$(document).ready(function() {
			ratio = "${ratio}";
			if(ratio==""){
				$("#ratioSpan").remove();
				$('th.credit-ratio,td.credit-ratio').remove();
			}
		});
		function submit(){
			$("#inputForm").submit();
		}
		function reOrder(tbodyid){
			//重置序号
			$("#"+tbodyid+" tr").find("td:first").each(function (i, v) {
				$(this).html(i + 1);
				$(this).parent().find("input[type=hidden]").val(i + 1);  //重置排序号
			});
			var index=0;
			var rex="\\[(.+?)\\]";
			$("#"+tbodyid+" tr").each(function (i, v) {
				$(this).find("[name]").each(function (i2, v2) {
					var name=$(this).attr("name");
					var indx=name.match(rex)[1];
					$(this).attr("name",name.replace(indx,index));
				});
				index++;
			});
		}
	</script>
</head>
<body>
<form action="${ctxFront}/project/projectClose/save" id="inputForm" >
	<div class="top-title">
		<h3>${projectDeclare.name}</h3>
		<h4>结项报告</h4>
		<div class="top-bread">
			<div class="top-prj-num"><span>项目编号:</span>${projectDeclare.number}</div>
			<div class="top-prj-num"><span>创建时间:</span><fmt:formatDate value="${projectDeclare.createDate}" /></div>
			<a href="javascript:;" class="btn btn-sm btn-primary" onclick="history.go(-1);">返回</a>
		</div>
	</div>
	<div class="outer-wrap">
		<div class="container-fluid">
			<input type="hidden" name="projectId" value="${projectDeclare.id}" />
			<input type="hidden" name="id" value="${projectClose.id}" />
			<c:set var="leader" value="${fns:getUserById(projectDeclare.leader)}" />
			<div class="row content-wrap">
				<section class="row">
					<div class="form-horizontal" novalidate>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5 ">项目负责人：</label>
							<p class="col-xs-7">
								${leader.name}
							</p>
						</div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5">学院：</label>
							<p  class="col-xs-7">${leader.office.name}</p>
						</div>
					</div>

					<div class="form-horizontal" novalidate>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5 ">学号：</label>
							<p class="col-xs-7">
								${leader.no}
							</p>
						</div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5">专业年级：</label>
							<p  class="col-xs-7">${fns:getOffice(leader.professional).name}<fmt:formatDate value="${student.enterDate}" pattern="yyyy"/></p>
						</div>
					</div>

					<div class="form-horizontal" novalidate>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5">联系电话：</label>
							<p  class="col-xs-7">${leader.mobile}</p>
						</div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5 ">E-mail：</label>
							<p class="col-xs-7">
								${leader.email}
							</p>
						</div>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info"><h4>项目基本信息</h4><span class="yw-line"></span></div>
					<div class="form-horizontal" novalidate>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5 ">项目类别：</label>
							<p class="col-xs-7">
								${fns:getDictLabel(projectDeclare.type, "project_type", "")}
							</p>
						</div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5 ">项目级别：</label>
							<p class="col-xs-7">
								${fns:getDictLabel(projectDeclare.level, "project_degree", "")}
							</p>
						</div>
						<div class="form-group col-sm-4 col-md-4 col-lg-4">
							<label class="col-xs-5">项目经费：</label>
							<p  class="col-xs-7">${fns:getDictLabel(projectDeclare.level, "project_degree_fund", "")}元</p>
						</div>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info"><h4>团队信息</h4><span class="yw-line"></span></div>
					<div class="table-wrap">
						<div>
							<span><strong style="color:red;">*</strong>项目团队：${team.name}</span>
						</div>
						<table class="table  table-hover table-bordered">
							<caption>学生团队</caption>
							<thead>
							<th>序号</th>
							<th>姓名</th>
							<th>学号</th>
							<th>学院</th>
							<th>专业</th>
							<th>技术领域</th>
							<th>联系电话</th>
							<th>在读学位</th>
							<c:if test="${fns:checkMenuByNum(5)}">
								<th class='credit-ratio'>学分配比</th>
							</c:if>
							</thead>
							<tbody>
							<c:forEach items="${turStudents}" var="tur" varStatus="status">
								<tr>
									<td>${status.count}</td>
										<td>${tur.name}</td>
										<td>${tur.no}</td>
										<td>${tur.org_name}</td>
										<td>${tur.professional} </td>
										<td>
												${fns:getDictLabels(tur.domain, "technology_field", tur.domain)}
										</td>
										<td>${tur.mobile}</td>
										<td>
												${tur.instudy}
										</td><c:if test="${fns:checkMenuByNum(5)}">
										<td class="credit-ratio">
												${tur.weightVal}
										</td>
									</c:if>
								</tr>
							</c:forEach>
							</tbody>
						</table>

						<table class="table  table-hover table-bordered">
							<caption>指导老师</caption>
							<thead>
							<th>序号</th>
							<th>姓名</th>
							<th>单位（学院或企业，机构）</th>
							<th>职称（职务）</th>
							<th>技术领域</th>
							<th>联系电话</th>
							<th>E-mail</th>
							</thead>
							<tbody>
							<c:forEach items="${turTeachers}" var="tur2" varStatus="status">
								<tr>
									<td>${status.count}</td>
									<td>${tur2.name}</td>
									<td>${tur2.office.name}</td>
									<td>${tur2.technical_title}</td>
									<td>
										${tur2.domain}
									</td>
									<td>${tur2.mobile}</td>
									<td>${tur2.email}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info"><h4>计划工作任务</h4><span class="yw-line"></span></div>
					<div class="table-wrap">
						<table class="table  table-hover table-bordered">
							<thead>
							<th>序号</th>
							<th>计划时间段</th>
							<th>计划工作任务</th>
							</thead>
							<tbody>
							<c:forEach items="${plans}" var="plan" varStatus="status">
								<tr>
									<td>${status.count}</td>
									<td><fmt:formatDate value="${plan.startDate}" pattern="yyyy-MM-dd"/>  至 <fmt:formatDate value="${plan.endDate}" pattern="yyyy-MM-dd"/></td>
									<td>${plan.content}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info"><h4>任务目标完成情况</h4><span class="yw-line"></span></div>
					<div class="table-wrap">
						<table class="table  table-hover table-bordered">
							<caption style="width:116px;">组成员完成情况</caption>
							<thead>
							<th>项目组成员</th>
							<th>项目分工</th>
							<th>完成情况</th>
							</thead>
							<tbody>
								<c:forEach items="${proSituationList}" var="proSituation" varStatus="status">
									<tr>
										<input type="hidden"  name="proSituationList[${status.index}].user.id" value="${proSituation.user.id}" />
										<td>${proSituation.user.name}</td>
										<td class="td_input">${proSituation.division}</td>
										<td class="td_input">${proSituation.situation}</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>

						<table class="table  table-hover table-bordered">
							<caption style="width:116px;">当前项目进度</caption>
							<thead>
							<th>序号</th>
							<th>实际完成时间</th>
							<th>完成情况</th>
							<th>阶段性成果</th>
							</thead>
							<tbody id="prj_process_body">
							<script type="text/template" id="tpl">
								<tr id="progeressTr">
									<td>
										{{idx}}
									</td>
									<td>
										{{row.startDate}}
										<span>至</span>
										{{row.endDate}}
									</td>
									<td class="td_input">
										{{row.situation}}
									</td>
									<td class="td_input">
										{{row.result}}
									</td>
								</tr>
							</script>
							<script type="text/javascript">
								var rowIdx = 1;
								var idIdx=1;
								var tpl = $("#tpl").html();
								var delBtn=true;
								function addRow(tbodyId, tpl,row){
									if(rowIdx==1){
										delBtn=false;
									}else{
										delBtn=true;
									}
									$(tbodyId).append(Mustache.render(tpl, {
										idx: rowIdx,nameIdx:rowIdx-1,delBtn:delBtn,row:row,idIdx:idIdx
									}));
									rowIdx++;
									idIdx++;
								}
								function delRow(obj){
									$(obj).parent().parent().remove();
									rowIdx--;
									reOrder("prj_process_body");
								}


								var data = ${fns:toJson(proProgressList)};
								if(data.length>0){
									for (var i=0; i<data.length; i++){
										addRow('#prj_process_body',tpl,data[i]);
									}
								}else{
									addRow('#prj_process_body',tpl,'');
								}

							</script>
							</tbody>
						</table>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info"><h4>经费使用情况</h4><span class="yw-line"></span></div>
					<div class="table-wrap">
						<table class="table  table-hover table-bordered">
							<thead>
							<th>序号</th>
							<th>使用经费项目</th>
							<th>使用金额(元)</th>
							</thead>
							<tbody id="tbody2">
							<script type="text/template" id="tpl2">
								<tr>
									<input type="hidden" name="projectCloseFundList[{{nameIdx}}].sort" value="{{idx}}" />
									<td>{{idx}}</td>
									<td class="td_input">{{row.place}}</td>
									<td class="td_input">{{row.count}}</td>
								</tr>
							</script>
							<script type="text/javascript">
								var rowIdx2 = 1;
								var tpl2 = $("#tpl2").html();
								var delBtn2=true;
								function addRow2(tbodyId, tpl,row){
									if(rowIdx2==1){
										delBtn2=false;
									}else{
										delBtn2=true;
									}
									$(tbodyId).append(Mustache.render(tpl2, {
										idx: rowIdx2,nameIdx:rowIdx2-1,delBtn:delBtn2,row:row
									}));
									rowIdx2++;
								}
								function delRow2(obj){
									$(obj).parent().parent().remove();
									rowIdx2--;
									reOrder("tbody2");
								}
								var data2 = ${fns:toJson(projectCloseFundList)};
								if(data2.length>0){
									for (var i=0; i<data2.length; i++){
										addRow2('#tbody2',tpl2,data2[i]);
									}
								}else{
									addRow2('#tbody2',tpl2,'');
								}

							</script>
							</tbody>
						</table>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info"><h4>成果描述</h4><span class="yw-line"></span></div>
					<div class="table-wrap">
						<table class="table  table-hover table-bordered">
							<thead>
							<th>序号</th>
							<th>项目成果</th>
							<th>奖励情况描述</th>
							</thead>
							<tbody id="tbody3">
							<script type="text/template" id="tpl3">
								<tr>
									<input type="hidden" name="projectCloseResultList[{{nameIdx}}].sort" value="{{idx}}" />
									<td>{{idx}}</td>
									<td class="td_input">{{row.result}}</td>
									<td class="td_input">{{row.reward}}</td>
								</tr>
							</script>
							<script type="text/javascript">
								var rowIdx3 = 1;
								var tpl3 = $("#tpl3").html();
								var delBtn3=true;
								function addRow3(tbodyId, tpl,row){
									if(rowIdx3==1){
										delBtn3=false;
									}else{
										delBtn3=true;
									}
									$(tbodyId).append(Mustache.render(tpl3, {
										idx: rowIdx3,nameIdx:rowIdx3-1,delBtn:delBtn3,row:row
									}));
									rowIdx3++;
								}
								function delRow3(obj){
									$(obj).parent().parent().remove();
									rowIdx3--;
									reOrder("tbody3");
								}

								var data3 = ${fns:toJson(projectCloseResultList)};
								if(data3.length>0){
									for (var i=0; i<data3.length; i++){
										addRow3('#tbody3',tpl3,data3[i]);
									}
								}else{
									addRow3('#tbody3',tpl3,'');
								}

							</script>
							</tbody>
						</table>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info" style="height: 40px;line-height: 40px;">
						<h4 class="sub-file">附件 </h4><span class="yw-line"></span>
					</div>
					<div class="textarea-wrap">
						<sys:frontFileUpload fileitems="${fileListMap}"  className="accessories-h34" filepath="proCloseForm" readonly="true"></sys:frontFileUpload>
					</div>
				</section>

				<section class="row">
					<div class="prj_common_info" style="height: 40px;line-height: 40px;">
						<h4 class="sub-file" style="margin-top: 25px;">导师意见及建议</h4><span class="yw-line yw-line-fj"></span>
					<span href="javascript:;" class="upload-file" style="background: none;color:#656565 !important;">
						<strong>
							时间：
							<c:choose>
								<c:when test="${not empty projectClose.suggestDate}">
									<fmt:formatDate value="${projectClose.suggestDate}" pattern="yyyy-MM-dd"/>
								</c:when>
								<c:otherwise>${fns:getDate('yyyy-MM-dd')}</c:otherwise>
							</c:choose>
						</strong>
					</span>
					</div>
					<textarea name="suggest" class="col-xs-12 col-md-12 col-sm-12 col-lg-12 my-textarea" readonly>${projectClose.suggest}</textarea>
				</section>

				<div class="footer-btn-wrap">
					<%--<a href="javascript:;" class="btn btn-sm btn-primary btn-save" onclick="submit()">保存</a>--%>
					<%--<a href="javascript:;" class="btn btn-sm btn-primary" onclick="submit()">提交</a>--%>
				</div>
			</div>

		</div>
	</div>
</form>

</body>
</html>