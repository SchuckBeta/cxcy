<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%-- <%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %> --%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <style>
        .controls-checkbox > span {
            min-height: 20px;
            padding-left: 10px;
            display: inline-block;
            margin-bottom: 0;
            vertical-align: middle;
        }

        .controls-checkbox > span:first-child {
            padding-left: 0;
        }
    </style>
</head>
<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li class="active">创业基地</li>
        <li><a href="${ctxFront}/pw/pwEnterRel/list">入驻查询</a></li>
        <li class="active">项目详情</li>
    </ol>
    <div class="row-apply">
        <%--<div class="topbar clearfix"><a href="javascript:void (0)" class="pull-right btn-print">打印申请表</a><span>填报日期：</span></div>--%>
        <c:if test="${not empty pwEnter.applicant}">
            <h4 class="titlebar">创业人基本信息</h4>
            <div class="form-horizontal form-horizontal-apply">
            	<div class="row row-user-info">
            		<div class="col-xs-4">
                        <label class="label-static">入驻编号：</label>
                        <p class="form-control-static">${pwEnter.no}</p>
                    </div>
            		<div class="col-xs-4">
                        <label class="label-static">申请时间：</label>
                        <p class="form-control-static"><fmt:formatDate value="${pwEnter.createDate}" pattern="yyyy-MM-dd"/></p>
                    </div>
                    <div class="col-xs-4">
                        <label class="label-static">负责人：</label>
                        <p class="form-control-static">${pwEnter.applicant.name}</p>
                    </div>
	            </div>
                <div class="row row-user-info">

                        <%--<div class="col-xs-4">--%>
                        <%--<label class="label-static">入驻期限：</label>--%>
                        <%--<p class="form-control-static primary-color">${pwEnter.term} 天</p>--%>
                        <%--</div>--%>
                    <div class="col-xs-4">
                        <label class="label-static">学院：</label>
                        <p class="form-control-static">${pwEnter.applicant.office.name}</p>
                    </div>
                    <div class="col-xs-4">
                        <label class="label-static">学号：</label>
                        <p class="form-control-static">${pwEnter.applicant.no}</p>
                    </div>
                            <div class="col-xs-4">
                                <label class="label-static">联系方式：</label>
                                <p class="form-control-static">${pwEnter.applicant.mobile}</p>
                            </div>
                </div>
                <div class="row row-user-info">
                    <div class="col-xs-4">
                        <label class="label-static">邮件：</label>
                        <p class="form-control-static">${pwEnter.applicant.email}</p>
                    </div>
                    <div class="col-xs-4">
                        <label class="label-static">证件类型：</label>
                        <p class="form-control-static">${fns:getDictLabel(pwEnter.applicant.idType, 'id_type', '')}</p>
                    </div>
                    <div class="col-xs-4">
                        <label class="label-static">证件号码：</label>
                        <p class="form-control-static">${pwEnter.applicant.idNumber}</p>
                    </div>
                </div>
                <%--<div class="row row-user-info">--%>

                <%--</div>--%>
            </div>
        </c:if>
    </div>
    <div class="row-apply">
        <h4 class="titlebar">入驻申请信息</h4>
        <c:if test="${not empty pwEnter.ecompany.pwCompany}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>申请入驻创业企业</span>
                    <i class="line"></i>
                    <a data-toggle="collapse" href="#companyDetail"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="companyDetail" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="form-horizontal form-enter-apply">
                        <div class="form-group">
                            <label class="control-label col-xs-2">企业名称：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.ecompany.pwCompany.name}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">联系方式：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.ecompany.pwCompany.mobile}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">企业地址：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.ecompany.pwCompany.address}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">公司执照号：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.ecompany.pwCompany.no}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">公司注册资金：</label>
                            <div class="col-xs-2">
                                <p class="form-control-static">${pwEnter.ecompany.pwCompany.regMoney} 万元</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">资金来源：</label>
                            <div class="col-xs-10">
                                <div class="form-control-static controls-checkbox">
                                    <form:checkboxes path="pwEnter.ecompany.pwCompany.regMtypes"
                                                     items="${fns:getDictList('pw_reg_mtype')}"
                                                     itemLabel="label" itemValue="value"
                                                     htmlEscape="false" disabled="true" class="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">企业法人：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.ecompany.pwCompany.regPerson}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">企业入驻说明：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.ecompany.remarks}</p>
                            </div>
                        </div>
                        <%--企业入驻说明：：${pwEnter.ecompany.remarks}--%>
                            <%--<div class="form-group">--%>
                            <%--<label class="control-label col-xs-2">地址：</label>--%>
                            <%--<div class="col-xs-8">--%>
                            <%--<p class="form-control-static">${pwEnter.ecompany.pwCompany.address}</p>--%>
                            <%--</div>--%>
                            <%--</div>--%>
                            <%--<div class="form-group"><label class="control-label col-xs-2"><i>*</i>附件：</label>--%>
                            <%--<div class="col-xs-8"></div>--%>
                            <%--</div>--%>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix hide">
                        <div class="edit-bar-left">
                            <span>附件</span>
                            <i class="line"></i>
                        </div>
                    </div>
                    <div class="accessories-container">
                        <div class="accessories">
                            <div class="accessory" id="companyFiles">
                                    <%--<div class="accessory-info">--%>
                                    <%--<a><img src="${ctxImg}/filetype/rar.png">--%>
                                    <%--<span class="accessory-name">fhjd.rar</span>--%>
                                    <%--</a><i class="btn-delete-accessory"><img src="${ctxImg}/remove-accessory.png"></i>--%>
                                    <%--</div>--%>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty pwEnter.eproject.project}">
            <input id="paramsProjectTeamId" type="hidden" value="${pwEnter.eproject.project.teamId}">
            <input id="paramsProjectProId" type="hidden" value="${pwEnter.eproject.project.id}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>申请入驻创业项目</span>
                    <i class="line"></i>
                    <a data-toggle="collapse" href="#projectDetail"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="projectDetail" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="form-horizontal form-enter-apply">
                        <div class="form-group">
                            <label class="control-label col-xs-2">项目名称：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.eproject.project.name}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">项目类型：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">
                                        ${fns:getDictLabel(pwEnter.eproject.project.proTyped, 'act_project_type', '')}
                                </p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">项目类别：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">
                                    <c:if test="${fn:contains(pwEnter.eproject.project.proType, '1,')}">
                                        ${fns:getDictLabel(pwEnter.eproject.project.type, 'project_style', '')}
                                    </c:if>
                                    <c:if test="${fn:contains(pwEnter.eproject.project.proType, '7,')}">
                                        ${fns:getDictLabel(pwEnter.eproject.project.type, 'competition_type', '')}
                                    </c:if>
                                </p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">项目編号：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.eproject.project.number}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">项目简介：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.eproject.project.introduction}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">项目入驻说明：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.eproject.remarks}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">学生团队：</label>
                            <div class="col-xs-10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                    <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>姓名</th>
                                        <th>学号</th>
                                        <th>学院</th>
                                        <th>专业</th>
                                        <th>联系电话</th>
                                    </tr>
                                    </thead>
                                    <tbody id="projectTableStudent">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">指导老师：</label>
                            <div class="col-xs-10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                    <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>姓名</th>
                                        <th>工号</th>
                                        <th>导师类型</th>
                                        <th>联系电话</th>
                                        <th>E-mail</th>
                                        <th>单位（学院或企业、机构）</th>
                                    </tr>
                                    </thead>
                                    <tbody id="projectTableTeacher">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                    <div class="edit-bar edit-bar-sm clearfix hide">
                        <div class="edit-bar-left">
                            <span>附件</span>
                            <i class="line"></i>
                        </div>
                    </div>
                    <div class="accessories">
                        <div class="accessory" id="projectFiles">
                                <%--<div class="accessory-info">--%>
                                <%--<a><img src="${ctxImg}/filetype/rar.png">--%>
                                <%--<span class="accessory-name">fhjd.rar</span>--%>
                                <%--</a><i class="btn-delete-accessory"><img src="${ctxImg}/remove-accessory.png"></i>--%>
                                <%--</div>--%>
                        </div>
                    </div>

                    <%--项目入驻说明：${pwEnter.eproject.remarks}--%>
                </div>
            </div>
        </c:if>
        <c:if test="${not empty pwEnter.eteam.team}">
            <input id="paramsTeamId" type="hidden" value="${pwEnter.eteam.team.id}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>申请入驻创业团队</span>
                    <i class="line"></i>
                    <a data-toggle="collapse" href="#teamDetail"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="teamDetail" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="form-horizontal form-enter-apply">
                        <div class="form-group">
                            <label class="control-label col-xs-2">团队名称：</label>
                            <div class="col-xs-6">
                                <p class="form-control-static">${pwEnter.eteam.team.name}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">团队入驻说明：</label>
                            <div class="col-xs-8">
                                <p class="form-control-static">${pwEnter.eteam.remarks}</p>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">学生团队：</label>
                            <div class="col-xs-10">
                                <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                    <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>姓名</th>
                                        <th>学号</th>
                                        <th>学院</th>
                                        <th>专业</th>
                                        <th>联系电话</th>
                                    </tr>
                                    </thead>
                                    <tbody id="teamTableStudent">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-xs-2">导师团队：</label>
                            <div class="col-xs-10">
                                <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
                                    <thead>
                                    <tr>
                                        <th>序号</th>
                                        <th>姓名</th>
                                        <th>工号</th>
                                        <th>导师类型</th>
                                        <th>联系电话</th>
                                        <th>E-mail</th>
                                        <th>单位（学院或企业、机构）</th>
                                    </tr>
                                    </thead>
                                    <tbody id="teamTableTeacher">

                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix hide">
                    <div class="edit-bar-left">
                        <span>附件</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="accessories">
                    <div class="accessory" id="teamFiles">
                            <%--<div class="accessory-info">--%>
                            <%--<a><img src="${ctxImg}/filetype/rar.png">--%>
                            <%--<span class="accessory-name">fhjd.rar</span>--%>
                            <%--</a><i class="btn-delete-accessory"><img src="${ctxImg}/remove-accessory.png"></i>--%>
                            <%--</div>--%>
                    </div>
                </div>
                        <%--团队入驻说明：：${pwEnter.eteam.remarks}--%>
            </div>
        </c:if>
        <c:if test="${(not empty pwEnter.eroom) && (not empty pwEnter.eroom.pwRoom)}">
            <input id="paramsRoomId" type="hidden" value="${pwEnter.eroom.pwRoom.id}">
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>分配房间信息</span> <i class="line"></i> <a data-toggle="collapse" href="#roomMoreInfo"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="roomMoreInfo" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="form-horizontal form-horizontal-apply">
                        <div class="row row-user-info">
                            <div class="col-xs-4">
                                <span class="label-static">房间名：</span>
                                <p class="form-control-static">${pwEnter.eroom.pwRoom.name}</p>
                            </div>
                            <c:if test="${not empty pwEnter.eroom.pwRoom.alias}">
	                            <div class="col-xs-4">
	                                <span class="label-static">别名：</span>
	                                <p class="form-control-static">${pwEnter.eroom.pwRoom.alias}</p>
	                            </div>
                            </c:if>
                            <div class="col-xs-4">
                                <span class="label-static">房间类型：</span>
                                <p class="form-control-static">${fns:getDictLabel(pwEnter.eroom.pwRoom.type, 'pw_room_type', '')}</p>
                            </div>
                        </div>
                        <div class="row row-user-info">
                            <div class="col-xs-4">
                                <span class="label-static">容纳人数：</span>
                                <p class="form-control-static">${pwEnter.eroom.pwRoom.num}</p>
                            </div>
                            <div class="col-xs-4">
                                <span class="label-static">负责人：</span>
                                <p class="form-control-static">${pwEnter.eroom.pwRoom.person}</p>
                            </div>
                            <div class="col-xs-4">
                                <span class="label-static">手机：</span>
                                <p class="form-control-static">${pwEnter.eroom.pwRoom.mobile}</p>
                            </div>
                        </div>
                        <div class="row row-user-infod">
                            <div class="col-xs-8">
                                <span class="label-static">楼/层：</span>
                                <p class="form-control-static">
                                    <c:if test="${(not empty pwEnter.eroom.pwRoom.pwSpace.parent) && (pwEnter.eroom.pwRoom.pwSpace.parent.id ne root)}">
                                        <c:if test="${(not empty pwEnter.eroom.pwRoom.pwSpace.parent.parent) && (pwEnter.eroom.pwRoom.pwSpace.parent.parent.id ne root)}">
                                            <c:if test="${(not empty pwEnter.eroom.pwRoom.pwSpace.parent.parent.parent) && (pwEnter.eroom.pwRoom.pwSpace.parent.parent.parent.id ne root)}">
                                                <c:if test="${(not empty pwEnter.eroom.pwRoom.pwSpace.parent.parent.parent.parent) && (pwEnter.eroom.pwRoom.pwSpace.parent.parent.parent.parent.id ne root)}">
                                                    ${pwEnter.eroom.pwRoom.pwSpace.parent.parent.parent.parent.name}/
                                                </c:if>${pwEnter.eroom.pwRoom.pwSpace.parent.parent.parent.name}/
                                            </c:if>${pwEnter.eroom.pwRoom.pwSpace.parent.parent.name}/
                                        </c:if>${pwEnter.eroom.pwRoom.pwSpace.parent.name}/
                                    </c:if>${pwEnter.eroom.pwRoom.pwSpace.name}
                                </p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>


        <c:if test="${not empty pwEnter.remarks}">
	        <div class="edit-bar edit-bar-sm clearfix">
	            <div class="edit-bar-left">
	                <span>意见和建议</span>
	                <i class="line"></i>
	                <a data-toggle="collapse" href="#ideaSug"><i
	                        class="icon-collaspe icon-double-angle-up"></i></a>
	            </div>
	        </div>
	        <div id="ideaSug" class="panel-body collapse in">
	            <div class="panel-inner">
	                <div class="form-horizontal form-enter-apply">
	                    <div class="form-group">
	                        <label class="control-label col-xs-2">审核结果：</label>
	                        <div class="col-xs-8">
	                            <p class="form-control-static">
	                                <c:if test="${pwEnter.status eq 2}">拒绝</c:if>
	                                <c:if test="${pwEnter.status ne 2}">通过</c:if>
	                            </p>
	                        </div>
	                    </div>
	                    <div class="form-group">
	                        <label class="control-label col-xs-2">意见和建议：</label>
	                        <div class="col-xs-8">
	                            <p class="form-control-static">${pwEnter.remarks}</p>
	                        </div>
	                    </div>
	                </div>
	            </div>
	        </div>
        </c:if>
    </div>


    <div class="form-actions-cyjd text-center" style="border-top: none">
        <a class="btn btn-default" href="${ctxFront}/pw/pwEnterRel/list">返回</a>
    </div>
</div>
<script type="text/javascript">
    $(function () {


        $('a[data-toggle="collapse"]').click(function () {
            $(this).find('i').toggleClass('icon-double-angle-up icon-double-angle-down')
        });

        var tableList = function () {
            var teamId = $("#paramsTeamId").val(),
                    paramsProjectTeamId = $("#paramsProjectTeamId").val(),
                    paramsProjectProId = $("#paramsProjectProId").val();

            if ((teamId != undefined)) {
                 $.ajax({
                    type: 'GET',
                    url: '${ctxFront}/team/ajaxTeamStudent?teamid=' + teamId,
                    dataType: "json",
                    success: function (data) {
                        if (data && data.datas) {
                            var resp = data.datas, tableStudList = [];
                            resp.map(function (it, idx) {
                                tableStudList.push('<tr>' +
                                        '<td>' + (++idx) + '</td>' +
                                        '<td>' + it.name + '</td>' +
                                        '<td>' + it.no + '</td>' +
                                        '<td>' + it.orgName + '</td>' +
                                        '<td>' + it.professional + '</td>' +
                                        '<td>' + it.mobile + '</td>' +
                                        '</tr>');
                            });
                            $("#teamTableStudent").append(tableStudList.join(","));
                        } else {
                            $("#teamTableStudent").append('<tr>' +
                                    '<td colspan="6">没有数据</td>' +
                                    '</tr>');
                        }
                        ;
                        //返回如果是undefined就不显示
                        $("table td").map(function (idx, it) {
                            if (it.innerHTML === "undefined") {
                                it.innerHTML = "";
                            }
                            ;
                        });
                    }
                });
            }

            if ((teamId != undefined)) {
                $.ajax({
                    type: 'GET',
                    url: '${ctxFront}/team/ajaxTeamTeacher?teamid=' + teamId,
                    dataType: "json",
                    success: function (data) {
                        if (data && data.datas) {
                            var resp = data.datas, tableTeaList = [];
                            resp.map(function (it, idx) {
                                tableTeaList.push('<tr>' +
                                        '<td>' + (++idx) + '</td>' +
                                        '<td>' + it.name + '</td>' +
                                        '<td>' + it.no + '</td>' +
                                        '<td>' + it.teacherType + '</td>' +
                                        '<td>' + it.mobile + '</td>' +
                                        '<td>' + it.email + '</td>' +
                                        '<td>' + it.orgName + '</td>' +
                                        '</tr>');
                            });
                            $("#teamTableTeacher").append(tableTeaList.join(","));
                        } else {
                            $("#teamTableTeacher").append('<tr>' +
                                    '<td colspan="8">没有数据</td>' +
                                    '</tr>');
                        }
                        ;
                        //返回如果是undefined就不显示
                        $("table td").map(function (idx, it) {
                            if (it.innerHTML === "undefined") {
                                it.innerHTML = "";
                            }
                            ;
                        });
                    }
                });
            }

            if ((paramsProjectTeamId != undefined) || (paramsProjectProId != undefined)) {
                $.ajax({
                    type: 'GET',
                    url: '${ctxFront}/team/ajaxTeamStudent?teamid=' + paramsProjectTeamId + "&proId=" + paramsProjectProId,
                    dataType: "json",
                    success: function (data) {
                        if (data && data.datas) {
                            var resp = data.datas, tableStudList = [];
                            resp.map(function (it, idx) {
                                tableStudList.push('<tr>' +
                                        '<td>' + (++idx) + '</td>' +
                                        '<td>' + it.name + '</td>' +
                                        '<td>' + it.no + '</td>' +
                                        '<td>' + it.orgName + '</td>' +
                                        '<td>' + it.professional + '</td>' +
                                        '<td>' + it.mobile + '</td>' +
                                        '</tr>');
                            });
                            $("#projectTableStudent").append(tableStudList.join(","));
                        } else {
                            $("#projectTableStudent").append('<tr>' +
                                    '<td colspan="6">没有数据</td>' +
                                    '</tr>');
                        }
                        ;
                        //返回如果是undefined就不显示
                        $("table td").map(function (idx, it) {
                            if (it.innerHTML === "undefined") {
                                it.innerHTML = "";
                            }
                            ;
                        });
                    }
                });
            }

            if ((paramsProjectTeamId != undefined) || (paramsProjectProId != undefined)) {
                $.ajax({
                    type: 'GET',
                    url: '${ctxFront}/team/ajaxTeamTeacher?teamid=' + paramsProjectTeamId + "&proId=" + paramsProjectProId,
                    dataType: "json",
                    success: function (data) {
                        if (data && data.datas) {
                            var resp = data.datas, tableTeaList = [];
                            resp.map(function (it, idx) {
                                tableTeaList.push('<tr>' +
                                        '<td>' + (++idx) + '</td>' +
                                        '<td>' + it.name + '</td>' +
                                        '<td>' + it.no + '</td>' +
                                        '<td>' + it.teacherType + '</td>' +
                                        '<td>' + it.mobile + '</td>' +
                                        '<td>' + it.email + '</td>' +
                                        '<td>' + it.orgName + '</td>' +
                                        '</tr>');
                            });
                            $("#projectTableTeacher").append(tableTeaList.join(","));
                        } else {
                            $("#projectTableTeacher").append('<tr>' +
                                    '<td colspan="8">没有数据</td>' +
                                    '</tr>');
                        }
                        ;
                        //返回如果是undefined就不显示
                        $("table td").map(function (idx, it) {
                            if (it.innerHTML === "undefined") {
                                it.innerHTML = "";
                            }
                            ;
                        });
                    }
                });
            }
        };
        tableList();
    })
    $(function () {
        var S_ENTER_COMPANY = [];
        var S_ENTER_PROJECT = [];
        var S_ENTER_TEAM = [];
        var $teamFiles = $('#teamFiles');
        var $projectFiles = $('#projectFiles')
        var $companyFiles = $('#companyFiles')
        getFiles()
        function getFiles() {
            var xhr = $.get('${ctxFront}/attachment/sysAttachment/ajaxFiles/${pwEnter.id}?type=30000')
            xhr.success(function (data) {
                if (data.status && data.datas) {
                    var temp1 = '';
                    var temp2 = '';
                    var temp3 = '';
                    S_ENTER_COMPANY.length = 0;
                    S_ENTER_PROJECT.length = 0;
                    S_ENTER_TEAM.length = 0;
                    data.datas.forEach(function (t) {
                        if (t.fileStep === 'S_ENTER_COMPANY') {
                            S_ENTER_COMPANY.push(t)
                        }
                        if (t.fileStep === 'S_ENTER_PROJECT') {
                            S_ENTER_PROJECT.push(t)
                        }
                        if (t.fileStep === 'S_ENTER_TEAM') {
                            S_ENTER_TEAM.push(t)
                        }
                    });
                    S_ENTER_COMPANY.forEach(function (t) {
                        temp1 += template(t)
                    });
                    S_ENTER_PROJECT.forEach(function (t) {
                        temp2 += template(t)
                    });
                    S_ENTER_TEAM.forEach(function (t) {
                        temp3 += template(t)
                    })
                    if (temp3) {
                        $teamFiles.append(temp3)
                        $teamFiles.parents('.accessories').prev().removeClass('hide')
                    }
                    if (temp2) {
                        $projectFiles.append(temp2)
                        $projectFiles.parents('.accessories').prev().removeClass('hide')
                    }
                    if ($companyFiles) {
                        $companyFiles.append(temp1)
                        $companyFiles.parents('.accessories').prev().removeClass('hide')
                    }
                }
            })
        }

        function template(t) {
            var temp = '';
            temp += '<div class="accessory-info"> <a data-ftp-url="' + t.ftpUrl + '" href="' + t.url + '">' +
                    '<img src="' + getImgType(t.suffix) + '"> <span class="accessory-name">' + t.name + '</span>' +
                    ' </a> </div>';
            return temp
        }

        function getImgType(type) {
            var extname;
            switch (type) {
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
                case "jpg":
                case "jpeg":
                case "gif":
                case "png":
                case "bmp":
                    extname = "image";
                    break;
                case 'txt':
                    extname = 'txt';
                    break;
                case 'zip':
                    extname = 'zip';
                    break;
                case 'rar':
                    extname = 'rar';
                    break;
                default:
                    extname = "unknow";
            }
            return '/img/filetype/' + extname + '.png';
        }

        $(document).on('click', '.accessory-info a', function (e) {
            e.preventDefault();
            var url = $(this).attr('href')
            var name = $(this).find('.accessory-name').text();
            var ftpUrl = $(this).attr('data-ftp-url');
            location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName=" + encodeURI(encodeURI(name));
        })
    })
</script>
</body>
</html>

