<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css?v=11"/>
    <link rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <style>
        .accessory-info a{
            color: #e9432d;
        }
    </style>
</head>
<body>
<div class="container project-view-contanier">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/page-innovation">双创项目</a></li>
        <li><a href="${ctxFront}/project/projectDeclare/curProject">我的项目</a></li>
        <li class="active">项目申报详情</li>
    </ol>
    <form:form id="form1" modelAttribute="projectDeclareVo" action="save" method="post" class="form-horizontal"
               enctype="multipart/form-data">
        <div class="form-horizontal">
            <div class="contest-content">
                <div class="tool-bar">
                    <a class="btn-print" onClick="window.print()"
                       href="javascript:void(0);">打印申报表</a>
                    <div class="inner">
                        <c:if test="${projectDeclare.id!=null}">
                            <span>项目编号：</span>
                            <i>${projectDeclare.number}</i>
                        </c:if>
                        <span>填表日期:</span> <i>${sysdate}</i> <span
                            style="margin-left: 15px">申请人:</span> <i>${creater.name}</i>
                        <c:if test="${projectDeclare.id==null}">
                            <a href="javascript:void(0)">${projectDeclareVo.projectAnnounce.name}</a>
                        </c:if>
                    </div>
                </div>
                <h4 class="contest-title">项目申报表</h4>
                <div class="contest-wrap">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"><i class="icon-require">*</i>项目负责人：</label>
                                <div class="input-box">
                                    <p>${leader.name}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"><i class="icon-require">*</i>学院：</label>
                                <div class="input-box">
                                    <p>${leader.office.name}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"><i class="icon-require">*</i>学号/毕业年份：</label>
                                <div class="input-box">
                                    <c:if test="${studentExpansion.graduation!=null}">
                                        <p>${leader.no}</p>
                                    </c:if>
                                    <c:if test="${studentExpansion.graduation==null}">
                                        <p>${leader.no}</p>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"><i class="icon-require">*</i>专业年级：</label>
                                <div class="input-box">
                                    <p>${fns:getProfessional(leader.professional)}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"><i class="icon-require">*</i>联系电话：</label>
                                <div class="input-box">
                                    <p>${leader.mobile}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"><i class="icon-require">*</i>E-mail：</label>
                                <div class="input-box">
                                    <p>${leader.email}</p>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>项目基本信息</span> <i class="line"></i>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-8">
                            <div class="form-group">
                                <label class="control-label"> <i class="icon-require">*</i>项目名称：
                                </label>
                                <div class="input-box">
                                    <p style="height:auto">${projectDeclare.name}</p>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"> <i class="icon-require">*</i>项目类别：
                                </label>
                                <div class="input-box">
                                    <form:select required="required" disabled="true"
                                                 path="projectDeclare.type" class="form-control">
                                        <form:option value="" label="--请选择--"/>
                                        <form:options items="${project_type}" itemValue="value"
                                                      itemLabel="label" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="form-group">
                                <label class="control-label"> <i class="icon-require">*</i>项目来源：
                                </label>
                                <div class="input-box">
                                    <form:select required="required" disabled="true"
                                                 path="projectDeclare.source" class="form-control">
                                        <form:option value="" label="--请选择--"/>
                                        <form:options items="${project_source}" itemValue="value"
                                                      itemLabel="label" htmlEscape="false"/>
                                    </form:select>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-12">
                            <div class="form-group form-span-checkbox">
                                <label class="control-label"><i class="icon-require">*</i>项目拓展及传承：</label>
                                <div id="mycheckbox" class="input-box">
                                    <div class="checkbox-span">
                                        <form:checkboxes disabled="true"
                                                         path="projectDeclare.development" items="${project_extend}"
                                                         itemValue="value" itemLabel="label" htmlEscape="false"/>
                                    </div>
                                    <form:hidden id="developmentStr" path="" value="${projectDeclare.development}"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>团队信息</span> <i class="line"></i>
                        </div>
                    </div>
                    <div class="table-condition">
                        <div class="form-group">
                            <label class="control-label"> <i class="icon-require">*</i>团队信息：
                            </label>
                            <div class="input-box" style="max-width: 394px;">
                                <form:select required="required" disabled="true"
                                             path="projectDeclare.teamId" class="input-medium form-control">
                                    <form:option value="" label="--请选择--"/>
                                    <form:options items="${teams}" itemValue="id" itemLabel="name"
                                                  htmlEscape="false"/>
                                </form:select>
                            </div>
                        </div>
                    </div>
                    <div class="table-title">
                        <span>学生团队</span>
                    </div>
                    <table class="table table-bordered table-team studenttb">
                        <thead>
                        <tr>
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
                        </tr>
                        </thead>
                        <c:if
                                test="${projectDeclareVo.teamStudent!=null&&projectDeclareVo.teamStudent.size() > 0}">
                            <c:forEach items="${projectDeclareVo.teamStudent}" var="item"
                                       varStatus="status">
                                <tr>
                                    <td>${status.index+1}</td>
                                    <td><c:out value="${item.name}"/></td>
                                    <td><c:out value="${item.no}"/></td>
                                    <td><c:out value="${item.org_name}"/></td>
                                    <td><c:out value="${item.professional}"/></td>
                                    <td><c:out value="${item.domain}"/></td>
                                    <td><c:out value="${item.mobile}"/></td>
                                    <td><c:out value="${item.instudy}"/></td>
                                    <c:if test="${fns:checkMenuByNum(5)}">
                                        <td class="credit-ratio">${item.weightVal}</td>
                                    </c:if>
                                </tr>
                            </c:forEach>
                        </c:if>
                    </table>
                    <div class="table-title">
                        <span>指导教师</span>
                    </div>
                    <table class="table table-bordered table-team teachertb">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>姓名</th>
                            <th>工号</th>
                            <th>导师来源</th>
                            <th>单位（学院或企业、机构）</th>
                            <th>职称（职务）</th>
                            <th>技术领域</th>
                            <th>联系电话</th>
                            <th>E-mail</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if
                                test="${projectDeclareVo.teamTeacher!=null&&projectDeclareVo.teamTeacher.size() > 0}">
                            <c:forEach items="${projectDeclareVo.teamTeacher}" var="item"
                                       varStatus="status">
                                <tr>
                                    <td>${status.index+1}</td>
                                    <td><c:out value="${item.name}"/></td>
                                    <td><c:out value="${item.no}"/></td>
                                    <td><c:out value="${item.teacherType}"/></td>
                                    <td><c:out value="${item.org_name}"/></td>
                                    <td><c:out value="${item.technical_title}"/></td>
                                    <td><c:out value="${item.domain}"/></td>
                                    <td><c:out value="${item.mobile}"/></td>
                                    <td><c:out value="${item.email}"/></td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        </tbody>
                    </table>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>项目介绍</span> <i class="line"></i>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>项目介绍：</label>
                        <div class="input-box">
                            <p style="height: auto" data-line-feed="textarea">${projectDeclare.introduction}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>前期调研准备：</label>
                        <div class="input-box">
                            <p style="height: auto" data-line-feed="textarea">${projectDeclare.innovation}</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>项目预案：</label>
                        <div class="input-box">
                            <table class="table table-bordered table-team yuantb">
                                <thead>
                                <tr>
                                    <th>实施预案</th>
                                    <th width="300">时间安排</th>
                                    <th>保障措施</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td data-line-feed="textarea" style="text-align: left">${projectDeclare.planContent}</td>
                                    <td style="vertical-align: middle"><fmt:formatDate
                                            value="${projectDeclare.planStartDate }"
                                            pattern="yyyy-MM-dd"/>至<fmt:formatDate
                                            value="${projectDeclare.planEndDate }" pattern="yyyy-MM-dd"/>
                                    </td>
                                    <td data-line-feed="textarea" style="text-align: left">${projectDeclare.planStep}</td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>任务分工：</label>
                        <div class="input-box">
                            <table class="table table-bordered table-team task">
                                <thead>
                                <tr>
                                    <th width="48">序号</th>
                                    <th>工作任务</th>
                                    <th>任务描述</th>
                                    <th>时间安排</th>
                                    <th>成本（元）</th>
                                    <th>质量评价</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:if test="${projectDeclareVo.plans!=null&&projectDeclareVo.plans.size() >0}">
                                    <c:forEach items="${projectDeclareVo.plans}" var="item"
                                               varStatus="status">
                                        <tr>
                                            <td>${status.index+1}</td>
                                            <td data-line-feed="textarea" style="text-align: left">${projectDeclareVo.plans[status.index].content }</td>
                                            <td data-line-feed="textarea" style="text-align: left">${projectDeclareVo.plans[status.index].description }</td>
                                            <td style="vertical-align: middle">
                                                <div class="time-input-inline">
                                                    <fmt:formatDate
                                                            value="${projectDeclareVo.plans[status.index].startDate }"
                                                            pattern="yyyy-MM-dd"/>
                                                    至
                                                    <fmt:formatDate
                                                            value="${projectDeclareVo.plans[status.index].endDate }"
                                                            pattern="yyyy-MM-dd"/>
                                                </div>
                                            </td>
                                            <td style="vertical-align: middle">${projectDeclareVo.plans[status.index].cost }</td>
                                            <td data-line-feed="textarea" style="text-align: left">${projectDeclareVo.plans[status.index].quality }</td>
                                        </tr>
                                    </c:forEach>
                                </c:if>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>预期成果</span> <i class="line"></i>
                        </div>
                    </div>
                    <div class="form-group form-span-checkbox">
                        <label class="control-label"><i class="icon-require">*</i>成果形式：</label>
                        <div class="input-box">
                            <div class="checkbox-span">
                                <form:checkboxes disabled="true"
                                                 path="projectDeclare.resultType" items="${resultTypeList}"
                                                 itemValue="value" itemLabel="label" htmlEscape="false"/>
                            </div>
                            <form:hidden id="resultTypeStr" path="" value="${projectDeclare.resultType}"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label"> <i class="icon-require">*</i>成果说明：
                        </label>
                        <div class="input-box">
                            <p style="height: auto;" data-line-feed="textarea">${projectDeclare.resultContent}</p>
                        </div>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>经费预算</span> <i class="line"></i>
                        </div>
                    </div>
                    <div class="form-group">
                        <p style="padding-left: 65px" data-line-feed="textarea">${projectDeclare.budget}</p>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>附件</span> <i class="line"></i>
                        </div>
                    </div>
                    <div style="padding: 0 20px;">
                        <sys:frontFileUpload fileitems="${projectDeclareVo.fileInfo}" className="accessories-h34" readonly="true"></sys:frontFileUpload>
                    </div>
                        <c:if
                                test="${projectDeclare.status!=null&&projectDeclare.status!='0'&&(not empty projectDeclareVo.auditInfo.level_d||not empty projectDeclareVo.auditInfo.mid_d||not empty projectDeclareVo.auditInfo.final_d)}">
                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>审核记录</span> <i class="line"></i>
                        </div>
                    </div>
                    <div class="form-group">
                            <table class="table table-bordered table-audit">
                                <thead>
                                <th colspan="3" class="colspan">评审内容</th>
                                <th>评审时间</th>
                                </thead>
                                <tbody>
                                <tr>
                                    <td rowspan="2" width="80">立项审核</td>
                                    <td width="120">评级结果</td>
                                    <td>建议及意见</td>
                                    <td rowspan="2" width="150">${projectDeclareVo.auditInfo.level_d }</td>
                                </tr>
                                <tr>
                                    <td><span
                                            style="display: inline-block; min-height: 20px; line-height: 20px;">${projectDeclareVo.auditInfo.level }</span>
                                    </td>
                                    <td><span
                                            style="display: inline-block; min-height: 20px; line-height: 20px;">${projectDeclareVo.auditInfo.level_s }</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td rowspan="2" width="80">中期审核</td>
                                    <td  width="120">评级结果</td>
                                    <td>建议及意见</td>
                                    <td rowspan="2">${projectDeclareVo.auditInfo.mid_d }</td>
                                </tr>
                                <tr>
                                    <td><span
                                            style="display: inline-block; min-height: 20px; line-height: 20px;">${projectDeclareVo.auditInfo.mid_result }</span>
                                    </td>
                                    <td><span
                                            style="display: inline-block; min-height: 20px; line-height: 20px;">${projectDeclareVo.auditInfo.mid_s }</span>
                                    </td>
                                </tr>
                                <tr>
                                    <td rowspan="2" width="80">结项审核</td>
                                    <td  width="120">评级结果</td>
                                    <td>建议及意见</td>
                                    <td rowspan="2">${projectDeclareVo.auditInfo.final_d }</td>
                                </tr>
                                <tr>
                                    <td><span
                                            style="display: inline-block; min-height: 20px; line-height: 20px;">${projectDeclareVo.auditInfo.final_result }</span>
                                    </td>
                                    <td><span
                                            style="display: inline-block; min-height: 20px; line-height: 20px;">${projectDeclareVo.auditInfo.final_s }</span>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </c:if>
                    </div>
                    <div class="btngroup">
                        <a class="btn btn-default" href="javascript:history.go(-1);">返回</a>
                    </div>
                </div>
            </div>
        </div>
    </form:form>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script src="${ctxJs}/GCSB.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
