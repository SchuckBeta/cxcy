<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>学院专家大赛评分</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>


    <script type="text/javascript">
        $(function () {
            $('a[data-toggle="collapse"]').click(function () {
                $(this).find('i').toggleClass('icon-double-angle-up icon-double-angle-down')
            });

            // 根据附件的名字自动修改其图标
            $('.file-item').each(function () {
                var el = $(this);
                var name = $.trim(el.children('a').text());
                var extname = name.split('.').pop().toLowerCase();
                switch (extname) {
                    case "xls":
                    case "xlsx":
                        extname = "excel";
                        break;
                    case "doc":
                    case "docx":
                        extname = "word";
                        break;
                    case "ppt":
                    case "pptx":
                        extname = "ppt";
                        break;
                        // 我不太确定这个文件格式
//                    case "project":
                    case "jpg":
                    case "jpeg":
                    case "gif":
                    case "png":
                    case "bmp":
                        extname = "image";
                        break;
                    case "rar":
                    case "zip":
                    case "txt":
                    case "project":
                        // just break
                        break;
                    default:
                        extname = "unknow";
                }
                el.children('img').attr('src', "/img/filetype/" + extname + ".png");
            });
        });

    </script>
</head>
<body>
<div class="container-fluid container-fluid-audit">
    <%----%>

    <div class="edit-bar edit-bar-tag edit-bar_new clearfix">
        <div class="edit-bar-left">
            <span>
                <c:if test="${state=='1'}">
                    学院专家评分 - 查看
                </c:if>
                <c:if test="${state=='2'}">
                    学院秘书审核 - 查看
                </c:if>
                <c:if test="${state=='3'}">
                    学院专家评分 - 查看
                </c:if>
                <c:if test="${state=='4'}">
                    学院管理员审核 - 查看
                </c:if>
                <c:if test="${state=='5'}">
                    学院管理员路演 - 查看
                </c:if>
                <c:if test="${state=='6'}">
                    学院管理员评级 - 查看
                </c:if>
                <c:if test="${state=='7'}">
                    学院管理员评级 - 查看
                </c:if>
                <c:if test="${state=='8'}">
                    学院审核未通过 - 查看
                </c:if>
                <c:if test="${state=='9'}">
                    学院审核未通过 - 查看
                </c:if>
            </span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/saveAuditWangping"
               method="post" class="form-horizontal form-container">
        <form:hidden path="id"/>
        <form:hidden path="procInsId"/>
        <form:hidden path="auditState"/>
        <div class="row-fluid row-info-fluid">
            <div class="span6">
                <label class="item-label">
                    大赛编号：
                </label>
                <div class="items-box">
                        ${gContest.competitionNumber}
                </div>
            </div>
            <div class="span6">
                <label class="item-label">
                    填表日期：
                </label>
                <div class="items-box">
                    <fmt:formatDate value="${gContest.subTime}" pattern="yyyy-MM-dd"/>
                </div>
            </div>
        </div>
        <div class="row-fluid row-info-fluid">
            <div class="span6">
                <label class="item-label">
                    申报人：
                </label>
                <div class="items-box">
                        ${sse.name}
                </div>
            </div>
            <div class="span6">
                <label class="item-label">
                    学院：
                </label>
                <div class="items-box">
                    <c:if test="${sse.office!=null}">
                        ${sse.office.name}
                    </c:if>
                </div>
            </div>
        </div>
        <div class="row-fluid row-info-fluid">
            <div class="span6">
                <label class="item-label">
                    学号(毕业年份)：
                </label>
                <div class="items-box">
                        ${sse.no}
                </div>
            </div>
            <div class="span6">
                <label class="item-label">
                    专业年级：
                </label>
                <div class="items-box">
                        ${fns:getProfessional(sse.professional)}
                </div>
            </div>
        </div>
        <div class="row-fluid row-info-fluid">
            <div class="span6">
                <label class="item-label">
                    联系电话：
                </label>
                <div class="items-box">
                        ${sse.mobile}
                </div>
            </div>
            <div class="span6">
                <label class="item-label">
                    邮箱：
                </label>
                <div class="items-box">
                        ${sse.email}
                </div>
            </div>
        </div>
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>项目基本信息</span>
                <i class="line"></i>
            </div>
        </div>
        <div class="row-fluid row-info-fluid">
            <div class="span12">
                <span class="item-label">参赛项目名称：</span>
                <div class="items-box">
                        ${gContest.pName}
                </div>
            </div>
        </div>
        <c:if test='${gContest.pId!=null && gContest.pId!=""}'>
            <div class="row-fluid row-info-fluid">
                <div class="span12">
                    <span class="item-label">关联项目：</span>
                    <div class="items-box">
                            ${relationProject}
                    </div>
                </div>
            </div>
        </c:if>
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>团队信息</span>
                <i class="line"></i>
                <a data-toggle="collapse" href="#teamInfo"><i class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="teamInfo" class="panel-body collapse in">
            <div class="panel-inner">
                <p>项目团队：${team.name}</p>
                <div class="table-caption">学生团队</div>
                <table class="table table-bordered table-condensed table-theme-default table-hover table-nowrap table-center table-orange">
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
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${turStudents!=null&&turStudents.size() > 0}">
                    <c:forEach items="${turStudents}" var="item" varStatus="status">
                    <tr>
                        <td>${status.index+1}</td>
                        <td><c:out value="${item.name}"/></td>
                        <td><c:out value="${item.no}"/></td>
                        <td><c:out value="${item.org_name}"/></td>
                        <td><c:out value="${item.professional}"/></td>
                        <td>${item.domain}</td>
                        <td><c:out value="${item.mobile}"/></td>
                        <td><c:out value="${item.instudy}"/></td>
                    <tr>
                        </c:forEach>
                        </c:if>
                    </tbody>
                </table>
                <div class="table-caption">指导老师</div>
                <table class="table table-bordered table-condensed table-theme-default table-hover table-nowrap table-center table-orange">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>单位（学院或企业、机构）</th>
                        <th>职称（职务）</th>
                        <th>技术领域</th>
                        <th>联系电话</th>
                        <th>E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${turTeachers!=null&&turTeachers.size() > 0}">
                        <c:forEach items="${turTeachers}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.org_name}"/></td>
                                <td><c:out value="${item.technical_title}"/></td>
                                <td>${item.domain}</td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>项目介绍</span>
                <i class="line"></i>
                <a data-toggle="collapse" href="#projectIntroP"><i class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="projectIntroP" class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row-fluid row-info-fluid">
                    <div class="span12">
                        <span class="item-label">项目简介：</span>
                        <div class="items-box">
                                ${gContest.introduction}
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <c:if test="${sysAttachments!=null }">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>附&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;件</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body">
                <div class="panel-inner">
                    <c:forEach items="${sysAttachments}" var="sysAttachment">
                        <div class="doc file-item mgb15">
                            <img src="${ctxImg}/filetype/unknow.png"/>
                            <a href="javascript:void(0)"
                               onclick="downfile('${sysAttachment.url}','${sysAttachment.name}');return false">
                                    ${sysAttachment.name}</a>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty asdiList}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核标准</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body">
                <div class="panel-inner">
                    <table class="table table-bordered table-condensed table-hover table-nowrap table-center table-orange"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>
                            <th><i class="require-star">*</i>分值</th>
                            <c:if test="${not empty isScore }">
                                <th>评分</th>
                            </c:if>
                        </tr>
                        </thead>
                            <%--   添加评分标准--%>
                        <tbody>
                        <c:forEach items="${asdiList}" var="de">
                            <tr>
                                <td>${de.checkPoint }</td>
                                <td> ${de.checkElement }</td>
                                <td>${de.viewScore }</td>
                                <c:if test="${not empty isScore }">
                                    <td>${de.score }</td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <c:if test="${not empty isScore }">
                            <tr>
                                <td>总分</td>
                                <td colspan="3" id="totalScoreV">${auditStandard.totalScore}</td>
                            </tr>
                        </c:if>
                        </tfoot>
                    </table>
                </div>
            </div>
        </c:if>

        <c:if test="${state=='1'}">
            <c:if test="${infos!=null}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="panel-inner">
                        <table class="table table-bordered table-condensed table-hover table-nowrap table-center table-orange">
                            <thead>
                            <tr>
                                <th colspan="4">学院审核记录</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="m-table-b">
                                <th style="width: 8%">学院专家</th>
                                <th style="width: 7%">评分</th>
                                <th colspan="2">建议及意见</th>
                            </tr>
                            <c:forEach items="${infos}" var="info">
                                <c:if test="${info.createBy.id == loginUser.id}">
                                    <tr>
                                        <td>${fns:getUserById(info.createBy.id).name}</td>
                                        <td>${info.score}</td>
                                        <td colspan="2">${info.suggest}</td>
                                    </tr>
                                </c:if>
                            </c:forEach>
                            </tbody>

                        </table>
                    </div>
                </div>
            </c:if>
        </c:if>
        <c:if test="${state=='2'}">
            <c:if test="${infos!=null}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="panel-inner">
                        <table class="table table-bordered table-condensed table-hover table-nowrap table-center table-orange">
                            <thead>
                            <tr>
                                <th colspan="4">学院审核记录</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr class="m-table-b">
                                <th style="width: 8%">学院专家</th>
                                <th style="width: 7%">评分</th>
                                <th colspan="2">建议及意见</th>
                            </tr>
                            <c:forEach items="${infos}" var="info">
                            <tr>
                                <td>${fns:getUserById(info.createBy.id).name}</td>
                                <td>${info.score}</td>
                                <td colspan="2">${info.suggest}</td>
                            <tr>
                                </c:forEach>
                            </tbody>

                        </table>
                    </div>
                </div>

            </c:if>
        </c:if>
        <c:if test="${state=='3'}">
            <c:if test="${infos!=null}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="panel-inner">
                        <table class="table table-bordered table-condensed table-hover table-nowrap table-center table-orange">
                            <thead>
                            <tr>
                                <th colspan="4">学院审核记录</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${collegeExportinfos!=null && fn:length(collegeExportinfos) > 0}">
                                <tr class="m-table-b">
                                    <th style="width: 8%">学院专家</th>
                                    <th style="width: 7%">评分</th>
                                    <th colspan="2">建议及意见</th>
                                </tr>
                                <c:forEach items="${collegeExportinfos}" var="info">
                                    <tr>
                                        <td>${fns:getUserById(info.createBy.id).name}</td>
                                        <td>${info.score}</td>
                                        <td colspan="2">${info.suggest}</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <tr>
                                <th>学院评分</th>
                                <th class="col-md-2 col-lg-2">排名</th>
                                <th colspan="2" class="col-md-6 col-lg-6">建议及意见</th>
                            </tr>
                            <c:forEach items="${infos}" var="info">
                                <tr>
                                    <td>${info.score}</td>
                                    <td>${info.sort}</td>
                                    <td colspan="2">${info.suggest}</td>
                                </tr>
                            </c:forEach>
                            <tr>
                                <td class="m-table-d">学院网评得分</td>
                                <td colspan="3">${gContest.gScore}</td>
                            </tr>
                            <c:if test="${schoolExportinfos!=null && fn:length(schoolExportinfos) > 0}">
                                <thead>
                                <tr>
                                    <th colspan="4">学院审核记录</th>
                                </tr>
                                </thead>
                                <tr class="m-table-b">
                                    <th>学校专家</th>
                                    <th>评分</th>
                                    <th colspan="2">建议及意见</th>
                                </tr>

                                <c:forEach items="${schoolExportinfos}" var="info">
                                    <c:if test="${info.createBy.id == loginUser.id}">
                                        <tr>
                                            <td>${fns:getUserById(info.createBy.id).name}</td>
                                            <td>${info.score}</td>
                                            <td>${info.suggest}</td>
                                        </tr>
                                    </c:if>
                                </c:forEach>
                            </c:if>
                            </tbody>
                        </table>

                    </div>
                </div>
            </c:if>
        </c:if>
        <c:if test="${state=='4'}">
            <c:if test="${infos!=null}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <div class="panel-inner">
                        <table class="table table-bordered table-condensed  table-hover table-nowrap table-center table-orange">
                            <thead>
                            <tr>
                                <th colspan="4">学院审核记录</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:if test="${collegeExportinfos!=null && fn:length(collegeExportinfos) > 0}">
                                <tr class="m-table-b">
                                    <th style="width: 8%">学院专家</th>
                                    <th style="width: 7%">评分</th>
                                    <th colspan="2">建议及意见</th>
                                </tr>
                                <c:forEach items="${collegeExportinfos}" var="info">
                                    <tr>
                                        <td>${fns:getUserById(info.createBy.id).name}</td>
                                        <td>${info.score}</td>
                                        <td colspan="2">${info.suggest}</td>
                                    </tr>
                                </c:forEach>
                            </c:if>
                            <tr>
                                <td class="m-table-d">学院网评得分</td>
                                <td colspan="3">${collegeInfo.score}</td>
                            </tr>
                            <tr>
                                <td class="m-table-d">学院排名</td>
                                <td colspan="3">${collegeInfo.sort}</td>
                            </tr>
                            <thead>
                            <tr>
                                <th colspan="4">学院审核记录</th>
                            </tr>
                            </thead>
                            <tr class="m-table-b">
                                <th>学院专家</th>
                                <th>评分</th>
                                <th colspan="2">建议及意见</th>
                            </tr>
                            <c:forEach items="${infos}" var="info">

                                <tr>
                                    <td>${fns:getUserById(info.createBy.id).name}</td>
                                    <td>${info.score}</td>
                                    <td>${info.suggest}</td>
                                </tr>

                            </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>
        </c:if>
        <c:if test="${state=='5'}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核记录</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body">
                <div class="panel-inner">
                    <table class="table table-bordered table-condensed table-hover table-nowrap table-center table-orange">
                        <thead>
                        <tr>
                            <th colspan="4">学院审核记录</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${collegeExportinfos!=null && fn:length(collegeExportinfos) > 0}">
                            <tr class="m-table-b">
                                <th style="width: 8%">学院专家</th>
                                <th style="width: 7%">评分</th>
                                <th colspan="2">建议及意见</th>
                            </tr>
                            <c:forEach items="${collegeExportinfos}" var="info">
                                <tr>
                                    <td>${fns:getUserById(info.createBy.id).name}</td>
                                    <td>${info.score}</td>
                                    <td colspan="2">${info.suggest}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <tr>
                            <td class="m-table-d">学院网评得分</td>
                            <td colspan="3">${collegeInfo.score}</td>
                        </tr>
                        <tr>
                            <td class="m-table-d">学院排名</td>
                            <td colspan="3">${collegeInfo.sort}</td>
                        </tr>

                        <thead>
                        <tr>
                            <th colspan="4">学院审核记录</th>
                        </tr>
                        </thead>
                        <tr class="m-table-b">
                            <th>学校专家</th>
                            <th>评分</th>
                            <th colspan="2">建议及意见</th>
                        </tr>
                        <c:forEach items="${schoolExportinfos}" var="info">
                            <tr>
                                <td>${fns:getUserById(info.createBy.id).name}</td>
                                <td>${info.score}</td>
                                <td>${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td class="m-table-d">学校网评得分</td>
                            <td colspan="3">${gContest.gScore}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>
        <c:if test="${state=='6'}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核记录</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body">
                <div class="panel-inner">
                    <table class="table table-bordered table-condensed table-hover table-nowrap table-center table-orange">
                        <thead>
                        <tr>
                            <th colspan="4">学院审核记录</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${collegeExportinfos!=null && fn:length(collegeExportinfos) > 0}">
                            <tr class="m-table-b">
                                <th style="width: 8%">学院专家</th>
                                <th style="width: 7%">评分</th>
                                <th colspan="2">建议及意见</th>
                            </tr>
                            <c:forEach items="${collegeExportinfos}" var="info">
                                <tr>
                                    <td>${fns:getUserById(info.createBy.id).name}</td>
                                    <td>${info.score}</td>
                                    <td colspan="2">${info.suggest}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <tr>
                            <td class="m-table-d">学院网评得分</td>
                            <td colspan="3">${collegeInfo.score}</td>
                        </tr>
                        <tr>
                            <td class="m-table-d">学院排名</td>
                            <td colspan="3">${collegeInfo.sort}</td>
                        </tr>
                        <thead>
                        <tr>
                            <th colspan="4">学院审核记录</th>
                        </tr>
                        </thead>
                        <tr class="m-table-b">
                            <th>学校专家</th>
                            <th>评分</th>
                            <th colspan="2">建议及意见</th>
                        </tr>
                        <c:forEach items="${schoolExportinfos}" var="info">
                            <tr>
                                <td>${fns:getUserById(info.createBy.id).name}</td>
                                <td>${info.score}</td>
                                <td colspan="2">${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td class="m-table-d">学校网评得分</td>
                            <td colspan="3">${schoolinfo.score}</td>
                        </tr>
                        <tr>
                            <td class="m-table-d">路演得分</td>
                            <td colspan="3">${lyinfo.score}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>
        <c:if test="${state=='7'}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核记录</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body">
                <div class="panel-inner">
                    <table class="table table-bordered table-condensed  table-hover table-nowrap table-center  table-orange">
                        <thead>
                        <tr>
                            <th colspan="4">学院审核记录</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:if test="${collegeExportinfos!=null && fn:length(collegeExportinfos) > 0}">
                            <tr class="m-table-b">
                                <th style="width: 8%">学院专家</th>
                                <th style="width: 7%">评分</th>
                                <th colspan="2">建议及意见</th>
                            </tr>
                            <c:forEach items="${collegeExportinfos}" var="info">
                                <tr>
                                    <td>${fns:getUserById(info.createBy.id).name}</td>
                                    <td>${info.score}</td>
                                    <td colspan="2">${info.suggest}</td>
                                </tr>
                            </c:forEach>
                        </c:if>
                        <tr>
                            <td class="m-table-d">学院网评得分</td>
                            <td colspan="3">${collegeInfo.score}</td>
                        </tr>
                        <tr>
                            <td class="m-table-d">学院排名</td>
                            <td colspan="3">${collegeInfo.sort}</td>
                        </tr>
                        <thead>
                        <tr>
                            <th colspan="4">学院审核记录</th>
                        </tr>
                        </thead>
                        <tr class="m-table-b">
                            <th>学校专家</th>
                            <th>评分</th>
                            <th colspan="2">建议及意见</th>
                        </tr>
                        <c:forEach items="${schoolExportinfos}" var="info">
                            <tr>
                                <td>${fns:getUserById(info.createBy.id).name}</td>
                                <td>${info.score}</td>
                                <td colspan="2">${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        <tr>
                            <td class="m-table-d">学校网评得分</td>
                            <td colspan="3">${schoolinfo.score}</td>
                        </tr>
                        <tr>
                            <td class="m-table-d">路演得分</td>
                            <td colspan="3">${lyinfo.score}</td>
                        </tr>
                        <tr>
                            <td class="m-table-d">学院排名</td>
                            <td colspan="3">${pjinfos.sort}</td>
                        </tr>
                        <thead>
                        <tr>
                            <th colspan="4">校赛结果</th>
                        </tr>
                        </thead>
                        <tr class="m-table-b">
                            <th>校赛总得分</th>
                            <th>校赛排名</th>
                            <th>校赛结果</th>
                            <th>建议及意见</th>
                        </tr>
                        <tr>
                            <td>${pjinfos.score}</td>
                            <td>${pjinfos.sort}</td>
                            <td>
                                    ${fns:getDictLabel(pjinfos.grade, "competition_college_prise", "")}
                            </td>
                            <td>${pjinfos.suggest}</td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:if>

        <div class="text-center">
            <a class="btn btn-default" href="javascript:void (0);" onclick="history.go(-1)">返回</a>
        </div>
    </form:form>
</div>
</body>
</html>