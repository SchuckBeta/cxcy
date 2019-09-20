<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/gctontestChange.css"/>
	<link rel="stylesheet" type="text/css" href="${ctxCss}/checkform.css" >
	<meta name="decorator" content="default"/>
	<title></title>

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
	</style>
</head>
<body>

	<form:form id="changeForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/changeFrom" method="post" >
		<div class="wholebox" id="wholebox">

			<p class="title">大赛变更表</p>
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
						<!-- <input id="one" name="guochuang" type="radio"  value="1"  disabled="true"  />
						<label class="linehead" for="one">无关联</label>
						<input id="two" name="guochuang" type="radio"  value="2"  disabled="true"  />
						<label class="linehead" for="two">国创项目</label> -->
					<p>
						<label class="linehead">
							<span>*</span>参赛项目名称：
						</label>
						<input type="text" name="pName"  class='required'value="${gContest.pName}"/>
					</p>

					<div class="selecbox">
						<div class="label-wrap" style="width:440px;">
							<label class="linehead">
								<span>*</span>项目类型：
							</label><form:select path="type" class="input-xlarge">
								<form:option value="" label="请选择"/>
								<form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
						<div class="label-wrap">
							<label class="linehead">
								<span>*</span>参赛组别：
							</label><form:select path="level" class="input-xlarge required">
								<form:option value="" label="请选择"/>
								<form:options items="${fns:getDictList('gcontest_level')}" itemLabel="label" itemValue="value" htmlEscape="false"/>
							</form:select>
						</div>
						<div class="label-wrap">
							<label class="linehead">
								<span>*</span>融资情况：
							</label><form:select path="financingStat" class="input-xlarge required">
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

					<p>
						<label><span>*</span>团队信息：</label>
						<form:select  required="required" onchange="DSSB.findTeamPerson();"  path="teamId" class="input-medium required">
						<form:option value="" label="--请选择--"/>
						<form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
						</form:select>
					</p>

					<p class="studentteam">
						<span>学生团队</span>
					</p>
					<table class="studenttb">
						<thead>
							<tr>
								<th width="50px">序号</th>
								<th width="70px">姓名</th>
								<th width="110px">学号</th>
								<th width="150px">学院</th>
								<th width="140px">专业</th>
								<th>技术领域</th>
								<th width="105px">联系电话</th>
								<th width="90px">在读学位</th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${gContestVo.teamStudent!=null&&gContestVo.teamStudent.size() > 0}">
							<c:forEach items="${gContestVo.teamStudent}" var="item" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td><c:out value="${item.name}" /></td>
								<td><c:out value="${item.no}" /></td>
								<td><c:out value="${item.org_name}" /></td>
								<td><c:out value="${item.professional}"/></td>
								<td><c:out value="${item.domain}" /></td>
								<td><c:out value="${item.mobile}" /></td>
								<td><c:out value="${item.instudy}" /></td>
							<tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>
					<p class="guideteacher">
						<span>指导教师</span>
					</p>
					<table class="teachertb">
						<thead>
							<tr>
								<th width="50px">序号</th>
								<th width="70px">姓名</th>
								<th >单位（学院或企业、机构）</th>
								<th width="150px">职称（职务）</th>
								<th width="140px">技术领域</th>
								<th width="110px">联系电话</th>
								<th width="170px">E-mail</th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${gContestVo.teamTeacher!=null&&gContestVo.teamTeacher.size() > 0}">
							<c:forEach items="${gContestVo.teamTeacher}" var="item" varStatus="status">
							<tr>
								<td>${status.index+1}</td>
								<td><c:out value="${item.name}" /></td>
								<td><c:out value="${item.org_name}" /></td>
								<td><c:out value="${item.technical_title}" /></td>
								<td><c:out value="${item.domain}" /></td>
								<td><c:out value="${item.mobile}" /></td>
								<td><c:out value="${item.email}" /></td>
							</tr>
							</c:forEach>
						</c:if>
						</tbody>
					</table>

				</div>

				<div class="itemintro">
					<div class="biaoti">
						<span class="char4">项目介绍</span>
					</div>
					<p>
						<label><span>*</span>项目介绍：</label>
					</p>

					<textarea class="introarea" name="introduction">${gContest.introduction}</textarea>
				</div>

				<div class="other" id="fujian">
			 		<div class="biaoti" >
						<span class="char2" style="width:70px;">附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件</span>
						<a class="upload"id="upload">上传附件</a>
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
</body>
</html>