<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<meta charset="UTF-8">
	<%--<meta name="decorator" content="default"/>--%>
	<%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/gctontestChange.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/checkform.css" >
	<%--<link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />--%>
	<%--<script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>--%>
	<%--<script src="${ctxStatic}/common/initiate.js" type="text/javascript"></script>--%>
	<style>
		.content .tg .m-valid{
			border: 1px solid #cbcbcb;
			text-indent: 5px;
			border-radius: 3px;
			width: 55px;;
		}
		.content .tg .m-table-two th{
			background-color: #f4e6d4;
		}
		.content .tg .m-table-hui, .content .tg .m-table-thhui th{
			background-color: #eee;
			font-family: 'Microsoft yahei';
			color: #636363;
			font-size: 14px;
			font-weight: bold;
		}
		.content .personalinfor{
			margin: 0px;;
		}
		.content .personalinfor li{
			width:495px;
		}
		.other table tr td,th{
			border-right: 1px solid #ddd;
		}
		.minus {
			display: inline-block;
			width: 19px;
			height: 19px;
			background: url(/img/minuse.png) no-repeat;
		}

		.minus:hover {
			background: url(/img/minus2.png) no-repeat;
			cursor: pointer;
		}
		[class^="icon-"], [class*=" icon-"]{
			margin-top:17px;
		}
	</style>
</head>
<body>

	<form:form id="changeForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/changeFrom" method="post" >
		<div class="wholebox" id="wholebox">

			<%--<p class="title">大赛变更表</p>--%>
			<div class="edit-bar edit-bar-tag edit-bar_new clearfix">
				<div class="edit-bar-left">
					<span>参赛项目查询 - 大赛变更</span>
					<i class="line weight-line"></i>
				</div>
			</div>
			<div class="content" style="padding:50px 125px;">

				<ul class="personalinfor">
					<li>
						<label style="width:182px"><span>*</span>申报人：</label>

						<input type="text" name="shenbaoren" readonly="readonly" value="${sse.name}" >
						<form:hidden path="declareId"/>
						<form:hidden path="id"/>
				        <form:hidden path="procInsId"/>
				        <form:hidden path="auditState"/>
					</li>
					<li>
						<label><span>*</span>学院：</label>
						<input path="" type="text" name="company" readonly="readonly"id="company" value="${sse.company.name}"/>
					</li>
					<li>
						<label style="width:182px"><span>*</span>学号（或者毕业年份）：</label>
						<input type="text" name="xuehao" id="zhuanye" readonly="readonly" value="${sse.no}" />
						<input type="text" name="nianfen" id="nianfen" readonly="readonly" value="${sse.graduation}"/>
						<small>年</small>
					</li>
					<li>
						<label><span>*</span>专业年级：</label>
						<input type="text"  readonly="readonly"
						 value="${fns:getProfessional(sse.professional)}"/>
					</li>
					<li>
						<label style="width:182px"><span>*</span>联系电话：</label>
						<input type="text" name="mobile" id="mobile" value="${sse.mobile}" readonly="readonly"/>
					</li>
					<li>
						<label><span>*</span>E-mail：</label>
						<input type="text" name="email" id="email" value="${sse.email}" readonly="readonly"/>
					</li>
				</ul>

				<div class="iteminfor">
					<div class="biaoti">
						<span>大赛基本信息</span>
					</div>
					<c:if test="${relationProject!=null}">
					<p id="myradiobox">
						<label class="linehead">
							<span>*</span>关联项目：
						</label>
							<input type="text" name="relationProject"  value="${relationProject}"  readonly="readonly" />
					</p>
					</c:if>
					<p>
						<label class="linehead">
							<span>*</span>参赛项目名称：
						</label>
						<input type="text" name="pName"  class='required'value="${gContest.pName}"/>
					</p>
					<div class="selecbox">
						<div class="label-wrap" style="width:440px;">
							<label class="linehead">
								<span>*</span>大赛类别：
							</label><form:select path="type" class="input-xlarge required" cssStyle="margin-left: 0">
								<form:option value="" label="请选择"/>
								<form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
						<div class="label-wrap">
							<label class="linehead">
								<span>*</span>参赛组别：
							</label><form:select path="level" class="input-xlarge required" cssStyle="margin-left: 0">
								<form:option value="" label="请选择"/>
								<form:options items="${fns:getDictList('gcontest_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
					</div>
					<div class="selecbox">
						<div class="label-wrap">
							<label class="linehead">
								<span>*</span>融资情况：
							</label><form:select path="financingStat" class="input-xlarge required" cssStyle="margin-left: 0">
							<form:option value="" label="请选择"/>
							<form:options items="${fns:getDictList('financing_stat')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
						</form:select>
						</div>
					</div>
				</div>


				<div class="teaminfor">
					<div class="biaoti">
						<span class="char4">团队信息</span>
					</div>
					<div class="panel-inner">
						<div class="row-fluid row-info-fluid">
							<div class="span6">
								<span class="item-label">团队名称：</span>${team.name}
							</div>
						</div>
						<div class="table-caption">学生团队</div>
						<table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover studenttb">
							<thead>
							<tr>
								<th>姓名</th>
								<th>学号</th>
								<th>联系电话</th>
								<th>学院</th>
								<th>当前在研</th>
								<th>职责</th>
								<th><input type="button" class="btn btn-primary btn-small" userType="1" value="新增"
										   onclick="selectUser(this)"/></th>
							</tr>
							</thead>
							<tbody id="teamTableStudent">
							<c:forEach items="${gContestVo.teamStudent}" var="item" varStatus="status">
								<tr userid="${item.userId}">
									<input class="custindex" type="hidden" name="studentList[${status.index}].userId"
										   value="${item.userId}">
									<input class="custindex" type="hidden" name="studentList[${status.index}].usertype"
										   value="1">
									<td>${item.name}</td>
									<td>${item.no}</td>
									<td>${item.mobile}</td>
									<td>${item.orgName}</td>
									<td>${item.curJoin}</td>
									<td>
										<select class="zzsel custindex" name="studentList[${status.index}].userzz">
											<option value="0"
													<c:if test="${item.userId==gContest.declareId}">selected</c:if> >
												负责人
											</option>
											<option value="1"
													<c:if test="${item.userId!=gContest.declareId}">selected</c:if> >
												组成员
											</option>
										</select>
									</td>
									<td><a class="minus"></a></td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
						<div class="table-caption">指导教师</div>
						<table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap teachertb">
							<thead>
							<tr>
								<th>姓名</th>
								<th>工号</th>
								<th>导师来源</th>
								<th>职称（职务）</th>
								<th>学历</th>
								<th>联系电话</th>
								<th>当前指导</th>
								<th><input type="button" class="btn btn-primary btn-small" userType="2" value="新增"
										   onclick="selectUser(this)"/></th>
							</tr>
							</thead>
							<tbody id="teamTableTeacher">
							<c:forEach items="${gContestVo.teamTeacher}" var="item" varStatus="status">
								<tr userid="${item.userId}">
									<input class="custindex" type="hidden" name="teacherList[${status.index}].userId"
										   value="${item.userId}">
									<input class="custindex" type="hidden" name="teacherList[${status.index}].userType" value="2">
									<td>${item.name}</td>
									<td>${item.no}</td>
									<td>${item.teacherType}</td>
									<td>${item.technicalTitle}</td>
									<td>${fns:getDictLabel(item.education,'enducation_level','')}</td>
									<td>${item.mobile}</td>
									<td>${item.curJoin}</td>
									<td><a class="minus"></a></td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>

				<div class="itemintro">
					<div class="biaoti">
						<span class="char4">项目介绍</span>
					</div>
					<p>
						<label><span>*</span>项目介绍：</label>
					</p>

					<textarea class="introarea  required" name="introduction">${gContest.introduction}</textarea>
				</div>

				<div class="other" id="fujian">
			 		<div class="biaoti" >
						<span class="char2" style="width:70px;">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件</span>
						<a class="upload" id="upload">上传附件</a>
						<input type="file"  style="display: none" id="fileToUpload" name="fileName"/>
					</div>
					<ul id="file">
						<c:forEach items="${sysAttachments}" var="item" varStatus="status">
							<li>
							<p>

							<img src="${ctxImg}/filetype/${item.imgType}.png">
								<a  href="javascript:void(0)"  onclick="downfile('${item.arrUrl}','${item.arrName}');return false">
							${item.arrName}</a>
							<span class="del" onclick="delUrlFile(this,'${item.id}','${item.arrUrl}','delFile');">
							<i class='icon-remove-sign'></i>
							</span>
							</p>

							</li>
						</c:forEach>
					</ul>
					<!-- <p>123456.txt</p>
					<p>项目明细对应表.txt</p> -->
				</div>
		<c:choose>
			<c:when test="${isImport!=null && isImport ==1}">

			</c:when>
			<c:otherwise>
				<%--新添加表格--%>
				<div class="itemintro">
					<div class="biaoti">
						<span class="char4">审核记录</span>
					</div>
				</div>
				<div class="other">
				<input type="hidden" name="changeData" id="changeData"/>
				<input type="hidden" name="state" id="state" value="${state}"/>
			<%-- 	<c:if test="${state=='1'}"> --%>
					<table class="tg" style="width: 100%;"  cellpadding="0" cellspacing="0" style="border: 1px solid #ccc;">
						<tr>
							<th colspan="2">评分</th>
							<th>排名</th>
							<th colspan="2">建议及意见</th>
						</tr>
						<tr class="m-table-thhui">
							<th colspan="2">学院评分</th>
							<th>学院排名</th>
							<th colspan="2">学院建议及意见</th>
						</tr>
						<c:forEach items="${infocolleges}" var="info" varStatus="state">
							<tr>
								<c:if test="${state.count eq 1}">
									<td rowspan="${fn:length(infocolleges)}" class="m-table-hui" style="width: 70px;">专家评分</td>
								</c:if>
								<td style="padding: 0px;" style="width: 100px;">
									<table class="tg" style="width: 100%;" cellpadding="0" cellspacing="0" style="border: 1px solid #ccc;">
										<tr>
											<c:choose>
												<c:when test="${info.id!=null}">
													<input type="hidden" name="collegeExportAuditId" value="${info.id}"/>
												</c:when>
												<c:otherwise>
													<input type="hidden" name="collegeExportAuditId" value=""/>
												</c:otherwise>
											</c:choose>
											<input type="hidden" name="collegeExport" value="${info.createBy.id}"/>

											<td style="width:150px;">${fns:getUserById(info.createBy.id).name}</td>
											<td style="text-align:left;">
											<c:choose>
												<c:when test="${info.score!=null && info.score!='0.0'}">
													<input style="float:left;margin-right:10px;" type="text" name="collegeExportScoreSt"class="m-valid"
													value="<fmt:formatNumber type='number' value='${info.score}' maxFractionDigits='0'/>" />
													<p style="display:none;" class="score-error-msg"></p>
												</c:when>
												<c:otherwise>
													<input style="float:left;margin-right:10px;" type="text" name="collegeExportScoreSt"class="m-valid"value="">
													<p style="display:none;" class="score-error-msg"></p>
												</c:otherwise>
											</c:choose>
											</td>
										</tr>
									</table>
								</td>
								<c:if test="${state.count eq 1}">
									<td rowspan="${fn:length(infocolleges)+2}">
										<c:choose>
											<c:when test="${collegeSecinfo!=null}">
												第${collegeSecinfo.sort}名
											</c:when>
											<c:otherwise>
												&nbsp;
											</c:otherwise>
										</c:choose>
									</td>
								</c:if>
								<td colspan="2">
								<input type="text" name="collegeExportSuggest"class="m-valid" style="width:90%;"value="${info.suggest}"/>
								</td>
							</tr>
						</c:forEach>
						<tr>
							<td class="m-table-hui">得分</td>
							<td id="collegeScore">
								<c:choose>
									<c:when test="${collegeSecinfo!=null}">
										${collegeSecinfo.score}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td colspan="2">

							</td>
						</tr>
						<tr>
							<td class="m-table-hui">学院审核结果</td>
							<td>
								<select name="collegeResult" class="input-xlarge">
									<c:choose>
										<c:when test="${collegeSecinfo!=null}">
											<c:if test="${collegeSecinfo.grade ==1}">
												<option value="0" >不合格</option>
												<option value="1" selected="selected">合格</option>
											</c:if>
											<c:if test="${collegeSecinfo.grade ==0}">
												<option value="0" selected="selected">不合格</option>
												<option value="1">合格</option>
											</c:if>
										</c:when>
										<c:otherwise>   
											<option value="">请选择</option>
										 	<option value="0">不合格</option>
											<option value="1">合格</option>
										</c:otherwise>
									</c:choose>
								</select>
							</td>
							<td colspan="2">
								<c:choose>
									<c:when test="${collegeSecinfo!=null}">
										<input type="text" name="collegeSuggest" style="width:90%;" class="m-valid"value="${collegeSecinfo.suggest}">
									</c:when>
									<c:otherwise>
										<input type="text" name="collegeSuggest" style="width:90%;" class="m-valid"value="">
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<%--第一部分 结束--%>
						<tr class="m-table-thhui" id="schoolExportTitle">
							<th colspan="2">学院评分</th>
							<th>学院排名</th>
							<th colspan="2">学院建议及意见</th>
						</tr>
						<c:forEach items="${schoolinfos}" var="info" varStatus="state">
							<tr name="schoolExport">
								<c:if test="${state.count eq 1}">
									<td rowspan="${fn:length(schoolinfos)}" class="m-table-hui" style="width: 70px;">专家评分</td>
								</c:if>
								<td style="padding: 0px;" style="width: 100px;">
									<table class="tg" style="width: 100%;"  cellpadding="0" cellspacing="0" style="border: 1px solid #ccc;">
										<tr>
											<c:choose>
												<c:when test="${info.id!=null}">
													<input type="hidden" name="schoolExportAuditId" value="${info.id}"/>
												</c:when>
												<c:otherwise>
													<input type="hidden" name="schoolExportAuditId" value=""/>
												</c:otherwise>
											</c:choose>
											<input type="hidden" name="schoolExport" value="${info.createBy.id}"/>
											<td style="width:150px;">${fns:getUserById(info.createBy.id).name}</td>
											<td style="text-align:left;">
											<c:choose>
												<c:when test="${info.score!=null && info.score!='0.0'}">
													<input style="float:left;margin-right:10px;" type="text" name="schoolExportScoreSt"class="m-valid"
													value="<fmt:formatNumber type='number' value='${info.score}' maxFractionDigits='0'/>" />

												</c:when>
												<c:otherwise>
													<input style="float:left;margin-right:10px;" type="text" name="schoolExportScoreSt"class="m-valid" />
												</c:otherwise>

											</c:choose>
											<p style="display:none;" class="score-error-msg"></p>
											</td>
										</tr>
									</table>
								</td>
								<c:if test="${state.count eq 1}">
									<td rowspan="${fn:length(schoolinfos)+3}">
										<c:choose>
											<c:when test="${schoolEndinfo!=null}">
												第${schoolEndinfo.sort}名
											</c:when>
											<c:otherwise>
												&nbsp;
											</c:otherwise>
										</c:choose>
									</td>
								</c:if>
								<td colspan="2">
								<c:choose>
									<c:when test="${info.suggest!=null}">
										<input type="text" name="schoolExportSuggest" style="width:90%;" class="m-valid" value="${info.suggest}"/>
									</c:when>
									<c:otherwise>
										<input type="text" name="schoolExportSuggest" style="width:90%;" class="m-valid" value=""/>
									</c:otherwise>
								</c:choose>
								</td>
							</tr>
						</c:forEach>
						<tr id="schoolTitle">
							<td class="m-table-hui">学院审核结果</td>
							<td>
								<select name="schoolResult" class="input-xlarge">
								<c:choose>
									<c:when test="${schoolSecinfo!=null}">
										<c:if test="${schoolSecinfo.grade ==1}">
											<option value="0" >不合格</option>
											<option value="1" selected="selected">合格</option>
										</c:if>
										<c:if test="${schoolSecinfo.grade ==0}">
											<option value="0" selected="selected">不合格</option>
											<option value="1">合格</option>
										</c:if>
									</c:when>
									<c:otherwise>   
										<option value="">请选择</option>
									 	<option value="0">不合格</option>
										<option value="1">合格</option>
									</c:otherwise>
								</c:choose>
								</select>
							</td>
							<td colspan="2">
								<c:choose>
									<c:when test="${schoolSecinfo!=null}">
										<input type="text" name="schoolSuggest" style="width:90%;" class="m-valid"value="${schoolSecinfo.suggest}">
									</c:when>
									<c:otherwise>
										<input type="text" name="schoolSuggest" style="width:90%;" class="m-valid"value="">
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr id="schoolluyanTitle">
							<td class="m-table-hui">路演得分</td>
							<td style="text-align:left;">
								<c:choose>
									<c:when test="${schoolLyinfo!=null}">
										<input style="float:left;margin-right:10px;" id="schoollyScore" type="text" name="schoollyScore"class="m-valid"
										value="<fmt:formatNumber type='number' value='${schoolLyinfo.score}' maxFractionDigits='0'/>" />
										<p style="display:none;" class="score-error-msg"></p>
									</c:when>
									<c:otherwise>
										<input style="float:left;margin-right:10px;" id="schoollyScore" type="text" name="schoollyScore"class="m-valid">
										<p style="display:none;" class="score-error-msg"></p>
									</c:otherwise>
								</c:choose>
							</td>
							<td colspan="2">
								<c:choose>
									<c:when test="${schoolLyinfo!=null}">
										<input type="text" style="width:90%;" class="m-valid" name="schoolluyanSug"value="${schoolLyinfo.suggest}">
									</c:when>
									<c:otherwise>
										<input type="text" style="width:90%;" class="m-valid" name="schoolluyanSug">
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr id="schooldefen">
							<td class="m-table-hui">得分</td>
							<td id="schoolScore">
								<c:choose>
									<c:when test="${schoolEndinfo!=null}">
										${schoolEndinfo.score}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td colspan="2">&nbsp;</td>
						</tr>
						<%--第二部分 结束--%>
						<tr class="m-table-thhui" id="schoolEndTitle">
							<th colspan="2">校赛结果</th>
							<th>荣获奖励</th>
							<th colspan="2">建议及意见</th>
						</tr>
						<tr id="schoolEndAudit">
							<td colspan="2">
								<c:choose>
									<c:when test="${schoolEndinfo!=null}">
										<form:select path="schoolendResult" name="schoolEndResult" class="input-xlarge">
											<form:option value="" label="请选择"/>
											<form:options items="${fns:getDictList('competition_college_prise')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
										</form:select>
									</c:when>
									<c:otherwise>   
										<select name="schoolendResult" class="input-xlarge">
											<option value="">请选择</option>
										 	<option value="0">不合格</option>
											<option value="1">合格</option>
											<option value="5">优秀</option>
											<option value="2">三等奖</option>
											<option value="3">二等奖</option>
											<option value="4">一等奖</option>
										</select>
									</c:otherwise>
								</c:choose>

							</td>
							<td>
								<c:choose>
									<c:when test="${gContestAward!=null}">
										${gContestAward.money}
									</c:when>
									<c:otherwise>
										&nbsp;
									</c:otherwise>
								</c:choose>
							</td>
							<td colspan="2"><input type="text" name="schoolendSuggest" style="width:90%;" class="m-valid" value="${schoolEndinfo.suggest}"/>
							</td>
						</tr>
					</table>
				<%--  </c:if> --%>
			</c:otherwise>
		</c:choose>

				<div class="btngroup">
					<a onclick="changeForm();">保存</a>
					<a onClick="history.back(-1)">返回</a>
				</div>
			</div>
		</div>
	</form:form>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/messages_zh.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/gcontestChange.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/fileUpLoad.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/DSSB.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>
<script type="text/javascript">
	/* 团队变更 */


	$(function () {
		var student = JSON.parse('${gContestVo.teamStudent}');
		console.log('student',student);
	});


	$(document).on('click', '.minus', function (e) {
		var tbd = $(this).parents("tbody");
		var tb = $(this).parents("table");
		$(this).parents("tr").remove();
		resetNameIndex(tbd);
		if (tb.hasClass("studenttb")) {
			$(".xfpbval").val("");
		}
	});

	$(document).on('change', '.zzsel', function (e) {
		if ($(this).val() == "0") {
			$(".zzsel").val("1");
			$(this).val("0");
		}
	});
	function saveModify() {
		var haveFzr = false;
		$(".zzsel").each(function (i, v) {
			if ($(v).val() == "0") {
				haveFzr = true;
				return;
			}
		});
		if (!haveFzr) {
			alertx("请选择负责人");
			return;
		}
		if (validate1.form()) {
			$("#saveBtn").removeAttr("onclick");
			$("#saveBtn").addClass("disabled");
			$("#inputForm").ajaxSubmit(function (data) {
				if (data.ret == "0") {
					$("#saveBtn").attr("onclick", "saveModify();");
					$("#saveBtn").removeClass("disabled");
					alertx(data.msg);
				} else {
					alertx(data.msg, function () {
						history.go(-1);
					});
				}
			});
		}
	}
	function selectUser(ob) {
		var c_btn = $(ob);
		var idsarr = [];
		c_btn.parents("table").find("tbody>tr").each(function (i, v) {
			idsarr.push($(v).attr("userid"));
		});
		var ids = idsarr.join(",");
		var userType = c_btn.attr("userType");
		var ititle = "选择学生";
		if (userType == '2') {
			ititle = "选择导师";
		}
		top.$.jBox.open('iframe:' + '/a/backUserSelect?ids=' + ids + '&userType=' + userType, ititle, 1100, 540, {
			buttons: {"确定": "ok", "关闭": true}, submit: function (v, h, f) {
				var temarr = [];
				$("input[name='boxTd']:checked", $(h.find("iframe")[0].contentDocument).find("iframe")[0].contentDocument)
						.each(function (i, v) {
							temarr.push($(v).val());
						});
				if (v == "ok") {
					addSelectUser(userType, temarr.join(","));
					return true;
				}
			},
			loaded: function (h) {
				$(".jbox-content", top.document).css("overflow-y", "hidden");
			}
		});
	}
	function addSelectUser(usertype, ids) {
		if (ids != "") {
			if ("1" == usertype) {
				$.ajax({
					type: "POST",
					url: "/a/selectUser/getStudentInfo",
					data: {ids: ids},
					success: function (data) {
						if (data) {
							var datahtml = "";
							var tpl = $("#tpl_st").html();
							$.each(data, function (i, v) {
								datahtml = datahtml + Mustache.render(tpl, {
											userId: v.userId,
											name: v.name,
											no: v.no,
											mobile: v.mobile,
											office: v.office,
											curJoin: v.curJoin
										});
							});
							$(".studenttb>tbody").append(datahtml);
							resetNameIndex($(".studenttb>tbody"));
						}
					}
				});
			} else {
				$.ajax({
					type: "POST",
					url: "/a/selectUser/getTeaInfo",
					data: {ids: ids},
					success: function (data) {
						if (data) {
							var datahtml = "";
							var tpl = $("#tpl_tea").html();
							$.each(data, function (i, v) {
								datahtml = datahtml + Mustache.render(tpl, {
											userId: v.userId,
											name: v.name,
											no: v.no,
											teacherType: v.teacherType,
											postTitle: v.postTitle,
											education: v.education,
											mobile: v.mobile,
											curJoin: v.curJoin
										});
							});
							$(".teachertb>tbody").append(datahtml);
							resetNameIndex($(".teachertb>tbody"));
						}
					}
				});
			}
		}
	}
	function resetNameIndex(tbodyOb) {
		var indexNum = 0;
		var rex = "\\[(.+?)\\]";
		$(tbodyOb).find("tr").each(function (i, v) {
			$(v).find(".custindex").each(function (ti, tv) {
				var name = $(tv).attr("name");
				var indx = name.match(rex)[1];
				$(tv).attr("name", name.replace(indx, indexNum));
			});
			indexNum++;
		});
	}
	function submitData() {
		var haveFzr = false;
		$(".zzsel").each(function (i, v) {
			if ($(v).val() == "0") {
				haveFzr = true;
				return;
			}
		});
		if (!haveFzr) {
			alertx("请选择负责人");
			return;
		}
		if (validate.form()) {
			$("#submitBtn").removeAttr("onclick");
			$("#submitBtn").addClass("disabled");

			$("#inputForm").ajaxSubmit(function (data) {
				if (data.ret == "0") {
					$("#submitBtn").attr("onclick", "submitData();");
					$("#submitBtn").removeClass("disabled");
					alertx(data.msg);
				} else {
					alertx(data.msg, function () {
						window.location.href = "/a/cms/form/queryMenuList/?actywId=${proModel.actYwId}";
					});
				}
			});
		}
	}
</script>
<script type="text/template" id="tpl_st">
	<tr userid="{{userId}}">
		<input type="hidden" class="custindex" name="studentList[custindex].userId" value="{{userId}}">
		<input type="hidden" class="custindex" name="studentList[custindex].userType" value="1">
		<td>{{name}}</td>
		<td>{{no}}</td>
		<td>{{mobile}}</td>
		<td>{{office}}</td>
		<td>{{curJoin}}</td>
		<td>
			<select class="zzsel custindex" name="studentList[custindex].userzz">
				<option value="0">负责人
				</option>
				<option value="1" selected>组成员
				</option>
			</select>
		</td>
		<td>
			<a class="minus"></a>
		</td>
	</tr>
</script>
<script type="text/template" id="tpl_tea">
	<tr userid="{{userId}}">
		<input type="hidden" class="custindex" name="teacherList[custindex].userId" value="{{userId}}">
		<input type="hidden" class="custindex" name="teacherList[custindex].userType" value="2">
		<td>{{name}}</td>
		<td>{{no}}</td>
		<td>{{teacherType}}</td>
		<td>{{postTitle}}</td>
		<td>{{education}}</td>
		<td>{{mobile}}</td>
		<td>{{curJoin}}</td>
		<td>
			<a class="minus"></a>
		</td>
	</tr>
</script>

</body>
</html>