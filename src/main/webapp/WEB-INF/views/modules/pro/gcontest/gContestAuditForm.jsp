<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>学院专家大赛评分</title>
    <%--<meta name="decorator" content="default"/>--%>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <style>
        .require-star {
            color: red;
            margin-right: 4px;
        }
    </style>
    <script type="text/javascript">
        $(function () {
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


        $(document).ready(function () {
            var oneFloatReg = /^[0-9]+([.]{1}[0-9]{1,1})?$/; //一位正小数
            var validate = $("#inputForm").validate({
                rules: {
                    midScore: {
                        required: true,
                        number: true,
                        min: 0,
                        max: 100
                    }
                },
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    }
                    else {
                        error.insertAfter(element);
                    }
                },
                submitHandler: function (form) {
                    $("#inputForm").find('button[type="submit"]').prop('disabled', true);
                    form.submit();
                    return false;
                }

            });
            jQuery.validator.addMethod("oneFloat", function (value, element) {
                var length = value.length;
                return this.optional(element) || oneFloatReg.test(value);
            }, "只能输入一位正小数");
            jQuery.validator.addMethod("maxBefore", function (value, element) {
                var $max = $(element).parent().prev();
                var maxVal = Number($max.text());
                var curVal = Number(value);
                return this.optional(element) || maxVal >= curVal
            }, "不能大于评分标准");

            jQuery.validator.addMethod("maxLength3", function (value, element) {
                return this.optional(element) || value < 1000;
            }, "评分不超过三位数");

            var $totalScore = $('#totalScore');
            var $formScore = $('.form-score');

            $formScore.on('change', function () {
                var score = countTotalScore();
                /*     $totalScore.next().val(score);
                 $totalScore.text(score);*/
                $totalScore.val(score);
            });

            function countTotalScore() {
                var totalScore = 0;
                $formScore.each(function (i, item) {
                    var $item = $(item);
                    var val = $item.val();
                    var score = Number(val);
                    if (val == '' || !val || !$.isNumeric(val)) {
                        score = 0;
                    }
                    totalScore += score;
                });
                return totalScore;
            };

            $(document).on('show', '.panel-body', function () {
                var $panelTitle = $(this).prev();
                var $icon = $panelTitle.find('>a>i');
                $icon.addClass('icon-double-angle-up').removeClass('icon-double-angle-down')
            });
            $(document).on('hidden', '.panel-body', function () {
                var $panelTitle = $(this).prev();
                var $icon = $panelTitle.find('>a>i');
                $icon.removeClass('icon-double-angle-up').addClass('icon-double-angle-down')
            })
        });


    </script>
</head>
<body>
<div class="container-fluid container-fluid-audit">
    <div class="edit-bar edit-bar-tag clearfix">
        <div class="edit-bar-left">
			<span>
                <c:if test="${gContest.auditState=='1'}">
                    参赛项目网评 - 学院专家评分
                </c:if>
                <c:if test="${gContest.auditState=='2'}">
                    参赛项目网评 - 学院秘书审核
                </c:if>
                <c:if test="${gContest.auditState=='3'}">
                    参赛项目网评 - 学院专家评分
                </c:if>
                <c:if test="${gContest.auditState=='4'}">
                    参赛项目网评 - 学校管理员审核
                </c:if>
                <c:if test="${gContest.auditState=='5'}">
                    参赛项目路演 - 学校管理员路演
                </c:if>
                <c:if test="${gContest.auditState=='6'}">
                    参赛项目评级 - 学校管理员评级
                </c:if>
			</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="gContest" action="${ctx}/gcontest/gContest/saveAuditWangping"
               cssClass="form-horizontal form-container"
               method="post">
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
        <div id="panelBaseInfo" class="panel-body collapse in">
            <div class="row-fluid row-info-fluid">
                <div class="span6">
                    <span class="item-label">参赛项目名称：</span>
                    <div class="items-box">
                            ${gContest.pName}
                    </div>
                </div>
                <c:if test='${gContest.pId!=null && gContest.pId!=""}'>
                    <div class="span6">
                        <span class="item-label">关联项目：</span>
                        <div class="items-box">
                                ${relationProject}
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>团队信息</span>
                <i class="line"></i>
                <a data-toggle="collapse" href="#panelTeamInfo"><i class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>


        <div id="panelTeamInfo" class="panel-body collapse in">
            <div class="panel-inner">
                <p>项目团队：${team.name}</p>
                <div class="table-caption">学生团队</div>
                <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange"
                       style="margin-bottom: 20px;">
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
                <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange">
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
                <a data-toggle="collapse" href="#projectIntro"><i class="icon-collaspe icon-double-angle-up"></i></a>
            </div>
        </div>
        <div id="projectIntro" class="panel-body collapse in">
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
                <div class="fjs">
                    <c:forEach items="${sysAttachments}" var="item">
                        <div class="doc file-item mgb15">
                                <%--<img src="${ctxImg}/filetype/unknow.png"/>
                                <a href="javascript:void(0)"
                                   onclick="downfile('${sysAttachment.url}','${sysAttachment.name}');return false">
                                        ${sysAttachment.name}</a>--%>

                            <img src="${ctxImg}/filetype/${item.imgType}.png">
                            <a href="javascript:void(0)"
                               onclick="downfile('${item.arrUrl}','${item.arrName}');return false">
                                    ${item.arrName}</a>


                        </div>
                    </c:forEach>
                </div>
            </div>
        </c:if>
        <c:if test="${gContest.auditState=='1'}">

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>专家评分</span>
                    <i class="line"></i>
                </div>
            </div>
            <c:if test="${asList!=null&&asList.size() > 0}">
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-pf table-orange table-center"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th width="270"><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>
                            <th width="150"><i class="require-star">*</i>分值</th>
                            <c:if test="${not empty isScore }">
                                <th width="150">评分</th>
                            </c:if>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${asList}" var="de" varStatus="idx">
                            <tr>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkPoint }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkElement }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.viewScore }
                                    </div>
                                </td>
                                <c:if test="${not empty isScore }">
                                    <td>
                                        <input type="text" name="scoreList${idx.index}"
                                               class="form-score oneFloat maxBefore required input-mini">
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="panel-body panel-form">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label"><i>*</i>评分：</label>
                        <div class="controls">
                            <input class="form-control input-mini required maxLength3" name="gScore"
                                    <c:if test="${not empty isScore }">
                                        readonly="readonly"
                                    </c:if>
                                   id="totalScore" type="text"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                    <textarea rows="3" name="comment" maxlength="300"
                                              class="form-control input-xxlarge"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${gContest.auditState=='2'}">
            <c:if test="${infos!=null}">

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange">
                        <thead>
                        <tr>
                            <th>学院专家</th>
                            <th>评分</th>
                            <th>建议及意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${infos}" var="info">
                            <tr>
                                <td>${fns:getUserById(info.createBy.id).name}</td>
                                <td>${info.score}</td>
                                <td>${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <thead>
                        <tr>
                            <th>学院专家评分</th>
                            <th colspan="2">排名</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${sortCollegeList}" var="info">
                                <tr>
                                    <td>${info.score}</td>
                                    <td colspan="2">${info.sort}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核</span>
                    <i class="line"></i>
                </div>
            </div>

            <c:if test="${not empty asList }">
                <div class="panel-body">
                    <table class="table table-bordered table-condensed table-pf table-nowrap table-center table-orange"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th width="270"><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>

                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${asList}" var="de" varStatus="idx">
                            <tr>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkPoint }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkElement }
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="panel-body panel-form">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label"><i>*</i>结果审核：</label>
                        <div class="controls">
                            <form:select path="grade" class="input-large required">
                                <form:option value="" label="请选择"/>
                                <form:option value="0" label="不合格"/>
                                <form:option value="1" label="合格"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                    <textarea rows="3" name="comment" maxlength="300"
                                              class="form-control input-xxlarge"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${gContest.auditState=='3'}">
            <c:if test="${infos!=null}">

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange">
                        <caption style="padding: 4px 5px;background-color: #f4e6d4">学院审核记录</caption>
                        <thead>
                        <tr>
                            <th>学院评分</th>
                            <th>排名</th>
                            <th colspan="2">建议及意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${infos}" var="info">
                            <tr>
                                <td>${info.score}</td>
                                <td>${info.sort}</td>
                                <td colspan="2">${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                        <tfoot>
                        <tr>
                            <td>学院网评得分</td>
                            <td colspan="3">${gContest.gScore}</td>
                        </tr>
                        </tfoot>
                    </table>
                </div>
            </c:if>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>评分</span>
                    <i class="line"></i>
                </div>
            </div>
            <c:if test="${asList!=null&&asList.size() > 0}">
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-pf table-nowrap table-center table-orange"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th width="270"><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>
                            <th width="150"><i class="require-star">*</i>分值</th>
                            <c:if test="${not empty isScore }">
                                <th width="150">评分</th>
                            </c:if>
                        </tr>
                        </thead>
                            <%--   添加评分标准--%>
                        <tbody>
                        <c:forEach items="${asList}" var="de" varStatus="idx">
                            <tr>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkPoint }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkElement }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.viewScore }
                                    </div>
                                </td>
                                <c:if test="${not empty isScore }">
                                    <td>
                                        <input type="text" name="scoreList${idx.index}"
                                               class="form-score oneFloat maxBefore required input-mini">
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="panel-body panel-form">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label"><i>*</i>评&nbsp;&nbsp;&nbsp;&nbsp;分：</label>
                        <div class="controls">
                            <input class="form-control input-mini required maxLength3"
                                    <c:if test="${not empty isScore }">
                                        readonly="readonly"
                                    </c:if>
                                   name="gScore" type="text" id="totalScore"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                    <textarea rows="3" name="comment" maxlength="300"
                                              class="form-control input-xxlarge"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

        </c:if>
        <c:if test="${gContest.auditState=='4'}">
            <c:if test="${infos!=null}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>

                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange">
                        <caption style="padding: 4px 5px;background-color: #f4e6d4">学校审核记录</caption>
                        <thead>
                        <tr>
                            <th>学校专家</th>
                            <th>评分</th>
                            <th>建议及意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${infos}" var="info">
                            <tr>
                                <td>${fns:getUserById(info.createBy.id).name}</td>
                                <td>${info.score}</td>
                                <td>${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        </tbody>

                        <thead>
                        <tr>
                            <th>学校专家评分</th>
                            <th colspan="2">排名</th>
                        </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${sortSchoolList}" var="info">
                                <tr>
                                    <td>${info.score}</td>
                                    <td colspan="2">${info.sort}</td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                </div>
            </c:if>
            <c:if test="${asList!=null&&asList.size() > 0}">
                <%--  <h3 class="char2"><span>审核标准</span></h3>--%>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-pf table-nowrap table-center table-orange"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th width="270"><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>

                        </tr>
                        </thead>
                            <%--   添加评分标准--%>
                        <tbody>
                        <c:forEach items="${asList}" var="de" varStatus="idx">
                            <tr>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkPoint }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkElement }
                                    </div>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="panel-body panel-form">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label"><i>*</i>结果审核：</label>
                        <div class="controls">
                            <form:select path="grade" class="form-control input-large required">
                                <form:option value="" label="请选择"/>
                                <form:option value="0" label="不合格"/>
                                <form:option value="1" label="合格"/>
                            </form:select>
                            <p class="score-error-msg"></p>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                    <textarea rows="3" name="comment" maxlength="300"
                                              class="form-control input-xxlarge"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${gContest.auditState=='5'}">
            <c:if test="${infos!=null}">

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange">
                        <thead>
                        <tr>
                            <th>审核记录</th>
                            <th>评分</th>
                            <th>排名</th>
                            <th>建议及意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${infos}" var="info">
                            <tr>
                                <td>${info.auditName}</td>
                                <td>${info.score}</td>
                                <td>${info.sort}</td>
                                <td>${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>

            <c:if test="${asList!=null&&asList.size() > 0}">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-pf table-nowrap table-center table-orange"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th width="270"><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>
                            <th width="150"><i class="require-star">*</i>分值</th>
                            <c:if test="${not empty isScore }">
                                <th width="150">评分</th>
                            </c:if>
                        </tr>
                        </thead>
                            <%--   添加评分标准--%>
                        <tbody>
                        <c:forEach items="${asList}" var="de" varStatus="idx">
                            <tr>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkPoint }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkElement }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.viewScore }
                                    </div>
                                </td>
                                <c:if test="${not empty isScore }">
                                    <td>
                                        <input type="text" name="scoreList${idx.index}"
                                               class="form-score oneFloat maxBefore required input-mini">
                                    </td>
                                </c:if>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:if>
            <div class="panel-body panel-form">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label"><i>*</i> 评&nbsp;&nbsp;&nbsp;&nbsp;分：</label>
                        <div class="controls">
                            <input class="form-control input-mini required maxLength3" name="gScore"
                                    <c:if test="${not empty isScore }">
                                        readonly="readonly"
                                    </c:if>
                                   id="totalScore" type="text"/>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                    <textarea rows="3" name="comment" maxlength="300"
                                              class="form-control input-xxlarge"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <c:if test="${gContest.auditState=='6'}">
            <c:if test="${infos!=null}">

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="panel-body">
                    <table class="table table-bordered table-hover table-condensed table-nowrap table-center table-orange">
                        <thead>
                        <tr>
                            <th>审核记录</th>
                            <th>评分</th>
                            <th>排名</th>
                            <th>建议及意见</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${infos}" var="info">
                            <tr>
                                <td>${info.auditName}</td>
                                <td>${info.score}</td>
                                <td>${info.sort}</td>
                                <td>${info.suggest}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>

            </c:if>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>评级</span>
                    <i class="line"></i>
                </div>
            </div>
            <c:if test="${asList!=null&&asList.size() > 0}">
                <div class="panel-body">

                        <%--  <h3 class="char2"><span>审核标准</span></h3>--%>
                    <table class="table table-bordered table-hover table-condensed table-pf table-nowrap table-center table-orange"
                           id="tableFormReview">
                        <thead>
                        <tr>
                            <th width="270"><i class="require-star">*</i>检查要点</th>
                            <th><i class="require-star">*</i>审核元素</th>
                            <%--<th width="150"><i class="require-star">*</i>分值</th>--%>
                            <%--<c:if test="${not empty isScore }">--%>
                                <%--<th width="150">评分</th>--%>
                            <%--</c:if>--%>
                        </tr>
                        </thead>
                            <%--   添加评分标准--%>
                        <tbody>
                        <c:forEach items="${asList}" var="de" varStatus="idx">
                            <tr>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkPoint }
                                    </div>
                                </td>
                                <td>
                                    <div class="form-control-box">
                                            ${de.checkElement }
                                    </div>
                                </td>
                                <%--<td>--%>
                                    <%--<div class="form-control-box">--%>
                                            <%--${de.viewScore }--%>
                                    <%--</div>--%>
                                <%--</td>--%>
                                <%--<c:if test="${not empty isScore }">--%>
                                    <%--<td>--%>
                                        <%--<input type="text" name="scoreList${idx.index}"--%>
                                               <%--class="form-score oneFloat maxBefore input-mini required">--%>
                                    <%--</td>--%>
                                <%--</c:if>--%>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>


                </div>
            </c:if>
            <div class="panel-body panel-form">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label"><i>*</i>结果评定：</label>
                        <div class="controls">
                            <form:select path="grade" class="form-control input-large required">
                                <form:option value="" label="请选择"/>
                                <form:option value="0" label="不合格"/>
                                <form:option value="1" label="合格"/>
                                <form:option value="5" label="优秀"/>
                                <form:option value="2" label="三等奖"/>
                                <form:option value="3" label="二等奖"/>
                                <form:option value="4" label="一等奖"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                    <textarea rows="3" name="comment" maxlength="300"
                                              class="form-control input-xxlarge"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </c:if>
        <div class="text-center mar_bottom">
            <button type="submit" class="btn btn-primary">提交</button>
            <a href="${ctx}/gcontest/gContest/collegeExportScore" class="btn btn-default">返回</a>
        </div>
    </form:form>
</div>
</body>
</html>