<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>${fns:getFrontTitle()}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>


</head>

<body>

<script>
    $(function(){

        var pageList = '${fns: toJson(proModel)}';
        pageList = pageList.replace(/\n/g, '\\\\n').replace(/\r/g, '\\\\r');
        pageList = JSON.parse(pageList);
        console.log(pageList);

        var proModelTlxy = '${fns: toJson(proModelTlxy)}';
        proModelTlxy = proModelTlxy.replace(/\n/g, '\\\\n').replace(/\r/g, '\\\\r');
        proModelTlxy = JSON.parse(proModelTlxy);
        console.log(proModelTlxy,'proModelTlxy');
    })
</script>

<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>1首页</a></li>
        <li><a href="${ctxFront}/page-innovation">双创项目</a></li>
        <li><a href="${ctxFront}/project/projectDeclare/curProject">我的项目</a></li>
        <li class="active">项目申报详情</li>
    </ol>
    <div class="row-apply">
        <div class="row row-top-bar">
            <c:set var="sse" value="${fns:getUserById(proModel.declareId)}"/>
            <div class="col-xs-4">
                <p class="text-center topbar-item">项目编号：
                    <c:choose>
                        <c:when test="${proModelTlxy.proModel.finalStatus == '0000000264'}">
                            ${proModelTlxy.proModel.competitionNumber}
                        </c:when>
                        <c:otherwise>
                                <c:choose>
                                    <c:when test="${proModelTlxy.proModel.finalStatus == '0000000265'}">
                                        ${proModelTlxy.gCompetitionNumber}
                                    </c:when>
                                    <c:otherwise>${proModelTlxy.pCompetitionNumber}</c:otherwise>
                                </c:choose>
                        </c:otherwise>
                    </c:choose>
                </p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item">填报日期：<fmt:formatDate value='${proModel.subTime}'
                                                                        pattern='yyyy-MM-dd'/></p>
            </div>
            <div class="col-xs-4">
                <p class="text-center topbar-item"> 申报人：${sse.name}</p>
            </div>
        </div>
        <h4 class="titlebar">项目申报表</h4>
        <div class="contest-wrap form-horizontal form-horizontal-apply">
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">项目负责人：</label>
                    <p class="form-control-static">${sse.name}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学号：</label>
                    <p class="form-control-static">${sse.no}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">性别：</label>
                    <p class="form-control-static">${fns:getDictLabel(sse.sex, 'sex', '')}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学院：</label>
                    <p class="form-control-static">${sse.office.name}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">专业年级：</label>
                    <p class="form-control-static">${fns:getProfessional(sse.professional)}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">联系电话：</label>
                    <p class="form-control-static">${sse.mobile}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">E-mail：</label>
                    <p class="form-control-static">${sse.email}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">身份证号：</label>
                    <p class="form-control-static">${sse.idNumber}</p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>项目基本信息</span>
                    <i class="line"></i>
                    <a data-toggle="collapse" href="#applicatioinDetail"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div class="form-horizontal form-horizontal-apply mgb40">
                <div id="applicatioinDetail" class="panel-body collapse in">
                    <div class="row row-user-info">
                        <div class="col-xs-6">
                            <label class="label-static">项目名称：</label>
                            <p class="form-control-static" style="word-break: break-all">${proModel.pName}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">项目来源：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModelTlxy.source,'project_source','')}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">项目类别：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModel.proCategory, "project_type","" )}</p>
                        </div>
                        <div class="col-xs-6">
                            <label class="label-static">项目级别：</label>
                            <p class="form-control-static">${fns:getDictLabel(proModel.finalStatus,levelDict,"" )}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目简介：</label>
                            <p class="form-control-static">${proModel.introduction}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目logo：</label>
                            <div class="form-control-static">
                                <div class="project-logo-box">
                                    <img class="img-responsive" src="${empty proModel.logo.url ? '/images/default-pic.png':fns:ftpImgUrl(proModel.logo.url)}">
                                </div>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目拓展及传承：</label>
                            <p class="form-control-static">
                                <span>1、项目能与其他大型比赛、活动对接 </span>
                                <span>2、可在低年级同学中传承 </span>
                                <span>3、结项后能继续开展 </span>
                            </p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">前期调研准备：</label>
                            <p class="form-control-static">${proModelTlxy.innovation}</p>
                        </div>
                        <div class="col-xs-11">
                            <label class="label-static">项目预案：</label>
                            <div class="form-control-static">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                    <thead>
                                    <tr>
                                        <th>实施预案</th>
                                        <th width="250">时间安排</th>
                                        <th>保障措施</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td data-line-feed="textarea"
                                            style="text-align: left;">${proModelTlxy.planContent}</td>
                                        <td style="vertical-align: middle"><fmt:formatDate
                                                value="${proModelTlxy.planStartDate }"
                                                pattern="yyyy-MM-dd"/>至<fmt:formatDate
                                                value="${proModelTlxy.planEndDate }"
                                                pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td data-line-feed="textarea"
                                            style="text-align: left;">${proModelTlxy.planStep}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="col-xs-11">
                            <label class="label-static">任务分工：</label>
                            <div class="form-control-static">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                    <thead>
                                    <tr>
                                        <th width="48">序号</th>
                                        <th>工作任务</th>
                                        <th>任务描述</th>
                                        <th>时间安排</th>
                                        <th>成本</th>
                                        <th>质量评价</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <c:if test="${proModelTlxy.planList!=null&&proModelTlxy.planList.size() >0}">
                                        <c:forEach items="${proModelTlxy.planList}" var="item"
                                                   varStatus="status">
                                            <tr>
                                                <td>${status.index+1}</td>
                                                <td data-line-feed="textarea"
                                                    style="text-align: left;">${proModelTlxy.planList[status.index].content }</td>
                                                <td data-line-feed="textarea"
                                                    style="text-align: left;">${proModelTlxy.planList[status.index].description }</td>
                                                <td style="vertical-align: middle">
                                                    <div class="time-input-inline">
                                                        <fmt:formatDate
                                                                value="${proModelTlxy.planList[status.index].startDate }"
                                                                pattern="yyyy-MM-dd"/>
                                                        至
                                                        <fmt:formatDate
                                                                value="${proModelTlxy.planList[status.index].endDate }"
                                                                pattern="yyyy-MM-dd"/>
                                                    </div>
                                                </td>
                                                <td style="vertical-align: middle">${proModelTlxy.planList[status.index].cost }</td>
                                                <td data-line-feed="textarea"
                                                    style="text-align: left;">${proModelTlxy.planList[status.index].quality }</td>
                                            </tr>
                                        </c:forEach>
                                    </c:if>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">成果形式：</label>
                            <p class="form-control-static">${fns:getDictLabels(proModelTlxy.resultType,'project_result_type','')}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">成果说明：</label>
                            <p class="form-control-static">${proModelTlxy.resultContent}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">项目经费预算：</label>
                            <p class="form-control-static">${proModelTlxy.budgetDollar}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">经费预算明细：</label>
                            <p class="form-control-static">${proModelTlxy.budget}</p>
                        </div>
                        <div class="col-xs-12">
                            <label class="label-static">附件：</label>
                            <div class="form-control-static">
                                <sys:frontFileUpload className="accessories-h30" fileitems="${applyFiles}"
                                                     readonly="true"></sys:frontFileUpload>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                        <a data-toggle="collapse" href="#projectDetail"><i
                                class="icon-collaspe icon-double-angle-up"></i></a>
                    </div>
                </div>
                <div class="table-condition form-horizontal form-horizontal-apply">
                    <div id="projectDetail" class="panel-body collapse in">
                        <div class="row row-user-info">
                            <div class="col-xs-8">
                                <label class="label-static">团队名称：</label>
                                <p class="form-control-static">${team.name}</p>
                            </div>
                        </div>
                        <div class="table-title">
                            <span>学生团队</span>
                            <span class="ratio" id="ratio"></span>
                        </div>
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                            <thead>
                            <tr id="studentTr">
                                <th>序号</th>
                                <th>姓名</th>
                                <th>工号</th>
                                <th>学院</th>
                                <th>专业</th>
                                <th>手机号</th>
                                <th>现状</th>
                                <th>当前在研</th>
                                <th>技术领域</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                                <c:forEach items="${teamStu}" var="item" varStatus="status">
                                    <tr>
                                        <td>${status.index+1 }</td>
                                        <td>${item.name }</td>
                                        <td>${item.no }</td>
                                        <td>${item.org_name }</td>
                                        <td>${item.professional}</td>
                                        <td>
                                                ${item.mobile }
                                        </td>
                                        <td>${fns:getDictLabel(item.currState, 'current_sate', '')}</td>
                                        <td>${item.curJoin }</td>
                                        <td>${item.domain }</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                        <div class="table-title">
                            <span>指导教师</span>
                        </div>
                        <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                            <thead>
                            <tr>
                                <th>序号</th>
                                <th>姓名</th>
                                <th>工号</th>
                                <th>单位(学院或企业、机构)</th>
                                <th>导师来源</th>
                                <th>当前指导</th>
                                <th>技术领域</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                                <c:forEach items="${teamTea}" var="item" varStatus="status">
                                    <tr>
                                        <td>${status.index+1 }</td>
                                        <td>${item.name }</td>
                                        <td>${item.no }</td>
                                        <td>${item.org_name }</td>
                                        <td>${item.teacherType}</td>
                                        <td>${item.curJoin }</td>
                                        <td>${item.domain }</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>


                <!-- 学生提交的各种报告-->
                <c:if test="${not empty reports}">
                    <c:forEach items="${reports}" var="item" varStatus="status">
                        <c:set var="actywGnode" value="${fns:getActYwGnode(item.gnodeId)}"/>
                        <div class="edit-bar edit-bar-sm clearfix">
                            <div class="edit-bar-left">
                                <span>${actywGnode.name}</span> <i class="line"></i>
                                <a data-toggle="collapse" href="#files${status.index}">
                                    <i class="icon-collaspe icon-double-angle-up"></i>
                                </a>
                            </div>
                        </div>

                        <div id="files${status.index}" class="panel-body collapse in">
                            <div class="panel-inner">
                                <div class="form-group">
                                    <label class="control-label label-static">附件：</label>
                                    <div class="form-control-static">
                                        <sys:frontFileUpload className="accessories-h30" fileitems="${item.files}"
                                                             readonly="true"></sys:frontFileUpload>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label label-static">已取得阶段性成果：</label>
                                    <p class="form-control-static">
                                            ${item.stageResult}
                                    </p>
                                </div>

                            </div>
                        </div>
                    </c:forEach>
                </c:if>


                <!--   审核记录  -->
                <c:if test="${not empty actYwAuditInfos}">

                    <div class="edit-bar edit-bar-sm clearfix">
                        <div class="edit-bar-left">
                            <span>审核记录</span> <i class="line"></i><a data-toggle="collapse" href="#actYwAuditInfos"><i
                                class="icon-collaspe icon-double-angle-up"></i></a>
                        </div>
                    </div>

                    <div id="actYwAuditInfos" class="panel-body collapse in">
                        <div class="panel-inner">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>审核动作</th>
                                    <th>审核时间</th>
                                    <th>审核人</th>
                                    <th>审核结果</th>
                                    <th style="width:45%">建议及意见</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach items="${actYwAuditInfos}" var="item">
                                    <c:choose>
                                        <c:when test="${not empty item.id}">
                                            <tr>
                                                <td>${item.auditName}</td>
                                                <td><fmt:formatDate value="${item.createDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                                <td>${item.user.name}</td>
                                                <td>${item.result}</td>
                                                <td>${item.suggest}</td>
                                            </tr>
                                        </c:when>
                                        <c:otherwise>
                                            <tr>
                                                <td colspan="5" style="color: red; text-align: right;font-weight: bold">${item.auditName}：${item.result}</td>
                                            </tr>
                                        </c:otherwise>
                                    </c:choose>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>

                    </div>

                </c:if>
            </div>
        </div>
        <div class="text-center mgb20">
            <a class="btn btn-default" href="javascript:history.go(-1)">返回</a>
        </div>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>


</body>


</html>
