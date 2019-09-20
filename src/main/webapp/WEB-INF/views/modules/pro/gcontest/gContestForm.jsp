<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/gContestForm_new.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/frontCyjd/uploaderFile.js"></script>

</head>
<body>

<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0;">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/html-sctzsj">双创大赛</a></li>
        <li class="active">"互联网+"校赛报名</li>
    </ol>
    <input type="hidden" id="pageType" value="edit">
    <h2 class="main-title">第四届"互联网+"大学生创新创业大赛报名</h2>A
    <form:form id="competitionfm" class="form-horizontal" modelAttribute="gContest" action="${ctxFront}/gcontest/gContest/save"
               method="post" enctype="multipart/form-data">
        <form:hidden path="year" value="${gContest.year}"/>
        <form:hidden path="id" value="${gContest.id}"/>
        <%--<form:hidden path="announceId" value="${gContestAnnounce.id}"/>--%>
        <form:hidden path="actywId" value="${gContest.actywId}"/>
        <div class="contest-content">
            <div class="tool-bar">
                <a class="btn-print" onClick="window.print()" href="javascript:void(0);">打印申报表</a>
                <div class="inner">
                    <c:if test="${id!=null}">
                        <span>大赛编号：</span>
                        <i>${competitionNumber}</i>
                    </c:if>
                    <input type="hidden" name="competitionNumber" value="${competitionNumber}"/>
                    <span>填表日期:</span>
                    <i>${sysdate}</i>
                </div>
            </div>
            <h4 class="contest-title">大赛报名</h4>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label class="control-label">申报人：</label>
                        <div class="input-box">
                            <p class="form-control-static">${sse.name}</p>
                            <input type="hidden" id="shenbaoren" name="shenbaoren" value="${sse.name}">
                            <input type="hidden" name="declareId" id="declareId" value="${gContest.declareId}"/>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label class="control-label">学院：</label>
                        <div class="input-box">
                            <p class="form-control-static">${sse.office.name}</p>
                            <input type="hidden" id="company" value="${sse.office.name}">
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label class="control-label">学号（毕业年份）：</label>
                        <div class="input-box">
                            <p class="form-control-static">${sse.no}
                                <c:if test="${not empty studentExpansion.graduation}">
                                    （<fmt:formatDate value='${studentExpansion.graduation}' pattern='yyyy'/>）
                                </c:if>
                                <c:if test="${empty studentExpansion.graduation}">
                                    <span class="gray-color">（暂无）</span>
                                </c:if>
                            </p>
                            <input type="hidden" id="zhuanye" value="${sse.no}"/>
                            <input type="hidden" id="nianfen" value="${studentExpansion.graduation}"/>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label class="control-label">专业年级：</label>
                        <div class="input-box">
                            <p class="form-control-static">${fns:getProfessional(sse.professional)}</p>
                            <input type="hidden" id="zynj" name="zynj"
                                   value="${fns:getProfessional(sse.professional)}">
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label class="control-label">联系电话：</label>
                        <div class="input-box">
                            <p class="form-control-static">${sse.mobile}</p>
                            <input type="hidden" id="mobile" name="mobile" value="${sse.mobile}">
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label class="control-label">E-mail：</label>
                        <div class="input-box">
                            <p class="form-control-static">${sse.email}</p>
                            <input type="hidden" id="email" name="email" value="${sse.email}">
                        </div>
                    </div>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>大赛基本信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-12">
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>关联项目：</label>
                        <div class="input-box">
                            <label class="radio-inline">
                                <input id="one" name="guochuang" type="radio" value="1" checked/> 无关联
                            </label>
                            <label class="radio-inline">
                                <input id="two" name="guochuang" type="radio" value="2"/> 国创项目
                            </label>
                            <form:select path="pId" class="form-control select-inline"
                                         cssStyle="visibility: hidden">
                                <form:option value="">-请选择-</form:option>
                                <%--<form:options items="${projects}" itemValue="id" itemLabel="name"--%>
                                <%--htmlEscape="false"></form:options>--%>
                                <c:forEach items="${projects}" var="item">
                                    <form:option value="${item.id}">${fn:substring(item.name, 0, 30)}</form:option>
                                </c:forEach>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="projectName" class="control-label"><i class="icon-require">*</i>参赛项目名称：</label>
                        <div class="input-box">
                            <input type="text" name="pName" id="projectName" class="form-control" maxlength='30'
                                   value="${gContest.pName}"/>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="type" class="control-label"><i class="icon-require">*</i>大赛类别：</label>
                        <div class="input-box">
                            <form:select id="type" path="type" required="required" class="form-control">
                                <form:option value="" label="请选择"/>
                                <form:options items="${fns:getDictList('competition_net_type')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="type" class="control-label"><i class="icon-require">*</i>参赛组别：</label>
                        <div class="input-box">
                            <form:select path="level" required="required" class="form-control">
                                <form:option value="" label="请选择"/>
                                <form:options items="${fns:getDictList('gcontest_level')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                </div>
                <div class="col-xs-6">
                    <div class="form-group">
                        <label for="financingStat" class="control-label"><i class="icon-require">*</i>融资情况：</label>
                        <div class="input-box">
                            <form:select id="financingStat" path="financingStat" required="required"
                                         class="form-control">
                                <form:option value="" label="请选择"/>
                                <form:options items="${fns:getDictList('financing_stat')}" itemLabel="label"
                                              itemValue="value" htmlEscape="false"/>
                            </form:select>
                        </div>
                    </div>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>团队信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="table-condition">
                <div class="form-group">
                    <label class="control-label"><i class="icon-require">*</i>团队信息：</label>
                    <div class="input-box" style="max-width: 394px;">
                        <form:select required="required" onchange="DSSB.findTeamPerson();" path="teamId"
                                     class="form-control"><form:option value="" label="--请选择--"/>
                            <form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
                        </form:select>
                    </div>
                </div>
            </div>
            <div class="table-title">
                <span>学生团队</span>
                <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
            </div>
            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team studenttb">
                <thead>
                <tr id="studentTr">
                    <th>序号</th>
                    <th>姓名</th>
                    <th>学号</th>
                    <th>学院</th>
                    <th>专业</th>
                    <th>技术领域</th>
                    <th>联系电话</th>
                    <th>在读学位</th>
                    <c:if test="${fns:checkMenuByNum(5)}">
                        <th width="50" class='credit-ratio'>学分配比</th>
                    </c:if>
                </tr>
                </thead>
                <tbody>
                <c:if test="${gContestVo.teamStudent!=null&&gContestVo.teamStudent.size() > 0}">
                    <c:forEach items="${gContestVo.teamStudent}" var="item" varStatus="status">
                        <tr>
                            <td>${status.index+1}
                                <input type="hidden" name="teamUserHistoryList[${status.index}].userId"
                                       value="${item.userId}">
                            </td>
                            <td><c:out value="${item.name}"/></td>
                            <td><c:out value="${item.no}"/></td>
                            <td><c:out value="${item.org_name}"/></td>
                            <td><c:out value="${item.professional}"/></td>
                            <td><c:out value="${item.domain}"/></td>
                            <td><c:out value="${item.mobile}"/></td>
                            <td><c:out value="${item.instudy}"/></td>
                            <c:if test="${fns:checkMenuByNum(5)}">
                                <td class="credit-ratio">
                                    <input class="form-control input-sm "
                                           name="TeamUserHistoryList[${status.index}].weightVal"
                                           value="${item.weightVal}">
                                </td>
                            </c:if>

                        </tr>
                    </c:forEach>
                </c:if>
                </tbody>
            </table>
            <div class="table-title">
                <span>指导教师</span>
            </div>
            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover table-team teachertb">
                <thead>
                <tr>
                    <th>序号</th>
                    <th>姓名</th>
                    <th>工号</th>
                    <th>单位（学院或企业、机构）</th>
                    <th>职称（职务）</th>
                    <th>技术领域</th>
                    <th>联系电话</th>
                    <th>E-mail</th>
                </tr>
                </thead>
                <tbody>
                <c:if test="${gContestVo.teamTeacher!=null&&gContestVo.teamTeacher.size() > 0}">
                    <c:forEach items="${gContestVo.teamTeacher}" var="item" varStatus="status">
                        <tr>
                            <td>${status.index+1}</td>
                            <td><c:out value="${item.name}"/></td>
                            <td><c:out value="${item.no}"/></td>
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
                    <span>项目介绍</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="form-group">
                <label for="introduction" class="control-label"><i class="icon-require">*</i>项目介绍：</label>
                <div class="input-box">
                        <textarea id="introduction" class="introarea form-control" rows="5" name="introduction"
                                  maxlength='1024'>${gContest.introduction}</textarea>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>附     件</span>
                    <i class="line"></i>
                </div>
            </div>
            <div style="padding: 0 20px;">
                <sys:frontFileUpload fileitems="${sysAttachments}" filepath="gcontest" tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件"
                                     className="accessories-h34"></sys:frontFileUpload>
            </div>
            <div class="text-center mgb20">
                <button type="button" data-control="uploader" class="btn btn-default" onclick="history.back(-1);">返回</button>
                <button type="button" data-control="uploader" class="btn btn-primary" onclick="DSSB.save();">保存</button>
                <button type="button" data-control="uploader" class="btn btn-primary" id="btnSubmit" onclick="DSSB.submit();">提交并保存</button>
            </div>
        </div>
    </form:form>
</div>


<script type="text/javascript">

    $(function () {
        var initDSSB = function () {
            DSSB.tabletrminus();
            DSSB.tabletrplus();
        }();
    })
</script>


<%--<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>--%>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/jquery.validate.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxCommon}/common-js/messages_zh.min.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/DSSBvalidate.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/DSSB_new.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>