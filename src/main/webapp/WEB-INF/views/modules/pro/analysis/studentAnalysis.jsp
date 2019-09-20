<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>学生自画像</title>
    <meta charset="UTF-8">
    <meta name="renderer" content="webkit">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/pagenation.css">
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/backtable.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/common/tablepage.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/stu.css">
    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script src="${ctxStatic}/common/mustache.min.js" type="text/javascript"></script>
    <script type="text/javascript">var ctx = '${ctx}', ctxStatic = '${ctxStatic}';</script>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());

            //增加学院下拉框change事件
            $("#collegeId").change(function () {
                var parentId = $(this).val();
                //根据当前学院id 更改
                $("#professionalSelect").empty();
                $("#professionalSelect").append('<option value="">所有专业</option>');
                $.ajax({
                    type: "post",
                    url: "/a/sys/office/findProfessionals",
                    data: {"parentId": parentId},
                    async: true,
                    success: function (data) {
                        $.each(data, function (i, val) {
                            if (val.id == "${studentExpansion.user.professional}") {
                                $("#professionalSelect").append('<option selected="selected" value="' + val.id + '">' + val.name + '</option>')
                            } else {
                                $("#professionalSelect").append('<option value="' + val.id + '">' + val.name + '</option>')
                            }

                        })
                    }
                });

            })
            $("#collegeId").trigger('change');
        });

        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
    <style>
        .row {
            margin-right: -15px !important;
            margin-left: -15px !important;
        }

        .information {
            margin-top: 15px;
            border-radius: 5px;
            border: 1px solid #ccc;
        }

        .user-info {
            padding: 15px 15px 0 15px;
        }

        .user-name {
            text-align: center;
            font-weight: bold;
            font-size: 16px;
            margin-top: 10px;
        }

        .img-box {
            min-height: 254px;
        }

        .pagination_num {
            margin: 30px 0 0 !important;
        }

        .stu-information {
            border-top: 1px solid #ccc;
        }

        .user-detail {
            margin: 0;
            padding: 25px 0;
            list-style: none;
        }

        .user-detail > li {
            line-height: 2;
            overflow: hidden;
        }

        .user-detail > li .item2 {
            float: right;
        }

        .user-detail > li b {
            color: #666;
        }

        .user-detail > li .item2:first-child {
            float: left;
        }

        .tec-area > b {
            float: left;
            display: inline-block;
        }

        .tec-area > span {
            display: inline-block;
            float: left;
            background-color: #e9422d;
            color: #fff;
        }

        .exp-list {
            padding: 25px 10px;
            min-height: 190px;
            background: url(/images/dian.jpg) repeat center;
        }

        .exp-item {
            margin-top: 20px;
            min-height: 56px;
            overflow: hidden;
        }

        .exp-item:first-child {
            margin-top: 0;
        }

        .control-b {
            float: left;
            width: 72px;
            color: #666;
            line-height: 2;
            font-size: 14px;
        }

        .exp-item .exp-ul {
            margin-left: 72px;
            margin-bottom: 0;
            overflow: hidden;
        }

        .stu-recommend {
            position: relative;
            padding-top: 4px;

        }

        .line {
            border-top: 1px solid #ccc;
        }

        .stu-recommend:after {
            position: absolute;
            content: '';
            left: 50%;
            top: -4px;
            margin-left: -4px;
            width: 8px;
            height: 8px;
            border-radius: 50%;
            background-color: #e9422d;
        }

        .stu-recommend dl {
            min-height: 146px;
            margin: 15px 0;
            line-height: 2;
        }

        .stu-recommend dt {
            font-size: 16px;
            padding-bottom: 15px;
            text-align: center;
            border-bottom: 1px solid #eee;
        }

        .stu-recommend dd {
            position: relative;
            padding-right: 32px;
            margin-top: 10px;
            line-height: 1.5;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        .stu-recommend dd em {
            font-style: normal;
        }

        .stu-recommend dd a {
            display: block;
            position: absolute;
            right: 0;
            top: 0;
            color: #e9422d;
        }

        .mybreadcrumbs {
            margin: 20px 1.5em;
            margin-left: 27px;
            border-bottom: 3px solid #f4e6d4;
        }

        .mybreadcrumbs span {
            position: relative;
            top: 9px;
            font-size: 16px;
            font-weight: bold;
            color: #e9432d;
            display: inline-block;
            background-color: #FFF;
            padding-right: 10px;
        }
        .form-group label{
            font-weight: normal;
        }
        .form-group{
            margin-right: 15px;
        }
    </style>
</head>
<body>
<div class="container" style="width:100%;padding: 20px 27px;">
    <div class="mybreadcrumbs" style="margin: 0px 0px 15px 0px;"><span>学生画像</span></div>
    <form:form id="searchForm" style="height:auto;border:0;" modelAttribute="studentExpansion"
               action="${ctx}/analysis/studentAnalysis/toPage"
               method="post" class="form-inline">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="form-group">
            <label for="collegeId">学院：</label>
            <form:select path="user.office.id" class="form-control" id="collegeId">
                <form:option value="" label="所有学院"/>
                <form:options items="${fns:findColleges()}" itemLabel="name" itemValue="id" htmlEscape="false"/>
            </form:select>
        </div>
        <div class="form-group">
            <label for="professionalSelect">专业：</label>
            <form:select path="user.professional" class="form-control"
                         id="professionalSelect">
                <form:option value="" label="所有专业"/>
            </form:select>
        </div>
        <div class="form-group">
            <label for="userName">关键词：</label>
            <form:input id="userName" path="user.name" type="text" class="form-control"/>
        </div>
        <input id="btnSubmit" class="btn btn-primary" style="background-color:#e9422d;border-color:#e9422d;float: right"  type="submit" value="查询"/>
            <%--<button class="btn">导出学生画像</button>--%>
    </form:form>
    <c:forEach items="${page.list}" var="studentExpansion">
        <div class="information">
            <div class="user-info">
                <div class="row"><!-- 图像+图标 开始 -->
                    <div class="col-md-3">
                        <div class="img-box">
                            <c:choose>
                                <c:when test="${studentExpansion.user.photo!=null && studentExpansion.user.photo!='' }">
                                    <img class="img-responsive" src="${fns:ftpImgUrl(studentExpansion.user.photo)}">
                                </c:when>
                                <c:otherwise>
                                    <img class="img-responsive" src="${ctxImg}/u4110.png">
                                </c:otherwise>
                            </c:choose>
                        </div>
                        <p class="user-name">${studentExpansion.user.name}</p>
                    </div>
                    <div class="col-md-9">
                        <img class="img-responsive" src="${ctxImages}/tubiao.png">
                    </div>
                </div>
                <div class="stu-information">
                    <div class="row">
                        <div class="col-md-3"><!-- 左侧信息 开始 -->
                            <ul class="user-detail">
                                <li>
                                    <div class="item2"><b>年龄：</b><c:if
                                            test="${studentExpansion.user.birthday!=null && studentExpansion.user.birthday!=''}">
                                        ${fns:getAge(studentExpansion.user.birthday)}
                                    </c:if></div>
                                    <div class="item2"><b>性别：</b><c:choose>
                                        <c:when test="${studentExpansion.user.sex!=null && studentExpansion.user.sex!='0'}">
                                            男
                                        </c:when>
                                        <c:otherwise>
                                            女
                                        </c:otherwise>
                                    </c:choose></div>
                                </li>
                                <li><b>学院：</b>${studentExpansion.user.office.name}</li>
                                <li><b>专业：</b>${fns:getOffice(studentExpansion.user.professional).name}</li>
                                <li class="tec-area">
                                    <b>现状：</b>${fns:getDictLabel(studentExpansion.currState, 'current_sate', '')}
                                    /
                                    <c:if test="${studentExpansion.graduation!=null && studentExpansion.graduation!=''}">
                                        <fmt:formatDate value="${studentExpansion.graduation}" pattern="yyyy"/>级
                                    </c:if>
                                    /
                                        ${fns:getDictLabel(studentExpansion.instudy, 'degree_type', '')}</li>
                                <li><b>技术领域：</b><c:if
                                        test="${studentExpansion.user.domainlt!=null && studentExpansion.user.domainlt!=''}">
                                    ${fns:getDomainlt(studentExpansion.user.domainlt,'span')}
                                </c:if></li>
                            </ul>
                        </div><!-- 左侧信息 结束 -->
                        <div class="col-md-9"><!-- 右侧信息 开始 -->
                            <div class="exp-list">
                                <div class="exp-item">
                                    <b class="control-b">项目经历：</b>
                                    <ul class="exp-ul">
                                        <c:if test="${fn:length(studentExpansion.projectList)>0}">
                                            <c:forEach items="${studentExpansion.projectList}" var="project">
                                                <li>
                                                    <fmt:formatDate value="${project.startDate}" pattern="yyyy"/>年
                                                    /${project.name}
                                                    <span class="pull-right">${project.level} ${project.result}</span>
                                                </li>

                                            </c:forEach>
                                        </c:if>
                                    </ul>
                                </div>
                                <div class="exp-item">
                                    <b class="control-b">大赛经历：</b>
                                    <ul class="exp-ul">
                                        <c:if test="${fn:length(studentExpansion.gContestList)>0}">
                                            <c:forEach items="${studentExpansion.gContestList}" var="gContest">
                                                <li>
                                                    <fmt:formatDate value="${gContest.createDate}" pattern="yyyy"/>年
                                                    /${gContest.type}/${gContest.pName}
                                                    <span class="pull-right">${gContest.award}</span></li>
                                            </c:forEach>
                                        </c:if>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row line">
                    <div class="col-md-4">
                        <div class="stu-recommend">
                            <span></span>
                            <dl>
                                <dt>双创课程推荐</dt>
                                <dd>互联网金融的基因在哪里？</dd>
                                <dd>互联网金融的基因在哪里？</dd>
                                <dd>互联网金融的基因在哪里？</dd>
                            </dl>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="stu-recommend">
                            <span></span>
                            <dl>
                                <dt>项目导师推荐</dt>
                                <dd><em>互联网金融的基因在哪里？互联网金融的基因在哪里？</em><a href="#">联系</a></dd>
                                <dd><em>互联网金融的基因在哪里？互联网金融的基因在哪里？</em><a href="#">联系</a></dd>
                                <dd><em>互联网金融的基因在哪里？互联网金融的基因在哪里？</em><a href="#">联系</a></dd>
                            </dl>
                        </div>
                    </div><!-- 推荐 1 結束 -->
                    <div class="col-md-4"> <!-- 推荐 1 开始 -->
                        <div class="stu-recommend">
                            <span></span>
                            <dl>
                                <dt>创业合作人推荐</dt>
                                <dd><em>互联网金融的基因在哪里？互联网金融的基因在哪里？</em><a href="#">联系</a></dd>
                                <dd><em>互联网金融的基因在哪里？互联网金融的基因在哪里？</em><a href="#">联系</a></dd>
                                <dd><em>互联网金融的基因在哪里？互联网金融的基因在哪里？</em><a href="#">联系</a></dd>
                            </dl>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>
    ${page.footer}
</div>
</body>

<script src="${ctxJs}/goback.js" type="text/javascript" charset="UTF-8"></script>
</html>