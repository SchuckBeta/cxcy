<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<style>
    .prj-plan span {
        margin-right: 20px;
    }

    .prj-plan span input[type="checkbox"] {
        position: relative;
        top: 2px;
        margin-right: 5px;
    }
    .col-xs-5{
        text-align: right;
    }

</style>
<form:form id="searchForm" modelAttribute="projectDeclare" action="" method="post">
    <script type="text/javascript">
        $(document).ready(function () {
            $.each($("#developmentStr").val().split(","), function (i, item) {
                $("input[name='development'][ value='" + item + "']").attr("checked", true);
            });
            $.each($("#resultTypeStr").val().split(","), function (i, item) {
                $("input[name='resultType'][ value='" + item + "']").attr("checked", true);
            });
        });
        function downFile(url, fileName) {
            location.href = "/a/loadUrl?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
        }
    </script>
    <c:set var="leader" value="${fns:getUserById(projectDeclare.leader)}"/>
    <div class="row">
        <div class="form-horizontal" name="addhardwareForm" method="post" novalidate>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5 ">项目编号：</label>
                <p class="col-xs-7">
                        ${projectDeclare.number}
                </p>
            </div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5">填表日期：</label>
                <p class="col-xs-7">
                    <fmt:formatDate value="${projectDeclare.createDate}" pattern="yyyy-MM-dd"/>
                </p>
            </div>
        </div>

        <div class="form-horizontal" name="addhardwareForm" method="post" novalidate>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5 ">项目负责人：</label>
                <p class="col-xs-7">
                        ${leader.name}
                </p>
            </div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5">学院：</label>
                <p class="col-xs-7">${leader.office.name}</p>
            </div>
        </div>

        <div class="form-horizontal" name="addhardwareForm" method="post" novalidate>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5 ">学号：</label>
                <p class="col-xs-7">
                        ${leader.no}
                </p>
            </div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5">专业年级：</label>
                <p class="col-xs-7">${fns:getOffice(leader.professional).name}<fmt:formatDate
                        value="${student.enterDate}" pattern="yyyy"/></p>
            </div>
        </div>

        <div class="form-horizontal" name="addhardwareForm" method="post" novalidate>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5 ">联系电话：</label>
                <p class="col-xs-7">
                        ${leader.mobile}
                </p>
            </div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4"></div>
            <div class="form-group col-sm-4 col-md-4 col-lg-4">
                <label class="col-xs-5">E-mail：</label>
                <p class="col-xs-7">${leader.email}</p>
            </div>
        </div>
    </div>

    <section class="row">
        <div class="prj_common_info">
            <h3>项目基本信息</h3><span class="yw-line"></span>
            <a href="javascript:;" id="commonToggle" data-flag="true"><span class="icon-double-angle-up"></span></a>
        </div>
        <div class="toggle_wrap" id="commonToggle_wrap">
            <div class="form-horizontal row" novalidate>
                <div>
                    <label style="width:140px;text-align: right">项目名称：</label>
                    <span style="display: block;margin: -26px 0 20px 144px;width:80%;">${projectDeclare.name}</span>
                </div>
            </div>

            <div class="form-horizontal row" novalidate>
                <div class="form-group col-sm-6 col-md-6 col-lg-6">
                    <label style="width:140px;text-align: right">项目类别：</label>
                    <span>${fns:getDictLabel(projectDeclare.type, "project_type", "")}</span>
                </div>

                <div class="form-group col-sm-6 col-md-6 col-lg-6">
                    <label style="width:140px;text-align: right">项目来源：</label>
                    <span>${fns:getDictLabel(projectDeclare.source, "project_source", "")}</span>
                </div>

            </div>

            <div class="form-horizontal row prj-plan" novalidate>
                <label>项目拓展及传承规划：</label>
                <form:checkboxes disabled="true" path="development" items="${fns:getDictList('project_extend')}"
                                 itemLabel="label" itemValue="value"/>
                <form:hidden id="developmentStr" path="development" value="${projectDeclare.development}"/>
            </div>
        </div>
    </section>

    <section class="row">
        <div class="prj_common_info">
            <h3>团队信息</h3><span class="yw-line"></span>
            <a href="javascript:;" id="teamToggle" data-flag="true"><span class="icon-double-angle-up"></span></a>
        </div>
        <div class="toggle_wrap" id="teamToggle_wrap">
            <table class="table  table-hover table-condensed table-bordered">
                <caption><strong>项目团队：</strong><span>${team.name}</span></caption>
                <caption>学生团队</caption>
                <thead>
                <tr>
                    <th width="60">序号</th>
                    <th width="100">姓名</th>
                    <th width="120">学号</th>
                    <th width="100">学院</th>
                    <th width="120">专业</th>
                    <th width="140">技术领域</th>
                    <th width="100">联系电话</th>
                    <th width="100">在读学位</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${turStudents}" var="tur" varStatus="status">
                    <tr>
                        <td>${status.count}</td>
                        <td>${tur.name}</td>
                        <td>${tur.no}</td>
                        <td>${tur.org_name}</td>
                        <td>${tur.professional}</td>
                        <td>${tur.domain}</td>
                        <td>${tur.mobile}</td>
                        <td>
                                ${tur.instudy}
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

            <table class="table  table-hover table-condensed table-bordered">
                <caption class="prj_tab_caption">指导老师</caption>
                <thead>
                <tr>
                    <th width="60">序号</th>
                    <th width="100">姓名</th>
                    <th width="120">单位（学院或企业、机构）</th>
                    <th width="120">职称（职务）</th>
                    <th width="120">技术领域</th>
                    <th width="100">联系电话</th>
                    <th width="60">E-mail</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${turTeachers}" var="tur2" varStatus="status">
                    <tr>
                        <td>${status.count}</td>
                        <td>${tur2.name}</td>
                        <td>${tur2.office.name}</td>
                        <td>${tur2.technical_title}</td>
                        <td>${tur2.domain}</td>
                        <td>${tur2.mobile}</td>
                        <td>${tur2.email}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </section>

    <section class="row">
        <div class="prj_common_info">
            <h3>项目介绍</h3><span class="yw-line"></span>
            <a href="javascript:;" id="projectToggle" data-flag="true"><span class="icon-double-angle-up"></span></a>
        </div>

        <div class="toggle_wrap" id="projectToggle_wrap">
            <div class="prj_introduce">
                <h5>项目简介：</h5>
                <c:if test="${fn:length(projectDeclare.introduction) > 0}">
                    <p class="prj_desc" style="padding: 8px 15px;">${projectDeclare.introduction}</p>
                </c:if>
            </div>

            <div class="prj_introduce">
                <h5>前期调研准备：</h5>
                <c:if test="${fn:length(projectDeclare.introduction) > 0}">
                    <p class="prj_desc" style="padding: 8px 15px;">
                            ${projectDeclare.innovation}
                    </p>
                </c:if>
            </div>

            <div class="prj_introduce">
                <h5>项目预案：</h5>
                <table class="table  table-hover table-condensed table-bordered">
                    <thead>
                    <th width="40%">实施方案</th>
                    <th width="20%">时间安排</th>
                    <th width="40%">保障措施</th>
                    </thead>
                    <tbody>
                    <tr>
                        <td>${projectDeclare.planContent}</td>
                        <td><fmt:formatDate value="${projectDeclare.planStartDate}" pattern="yyyy-MM-dd"/> 至
                            <fmt:formatDate value="${projectDeclare.planEndDate}" pattern="yyyy-MM-dd"/></td>
                        <td>${projectDeclare.planStep}</td>
                    </tr>
                    </tbody>
                </table>
            </div>

            <div class="prj_introduce">
                <h5>任务分工：</h5>
                <table class="table table-hover table-condensed table-bordered">
                    <thead>
                    <th width="10%">序号</th>
                    <th width="20%">工作任务</th>
                    <th width="20%">任务描述</th>
                    <th width="20%">时间安排</th>
                    <th width="20%">成本</th>
                    <th width="10%">质量评价</th>
                    </thead>
                    <tbody>
                    <c:forEach items="${plans}" var="plan" varStatus="status">
                        <tr>
                            <td>${status.count}</td>
                            <td>${plan.content}</td>
                            <td>${plan.description}</td>
                            <td><fmt:formatDate value="${plan.startDate}" pattern="yyyy-MM-dd"/> 至 <fmt:formatDate
                                    value="${plan.endDate}" pattern="yyyy-MM-dd"/></td>
                            <td>${plan.cost}</td>
                            <td>${plan.quality}</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </div>
        </div>
    </section>

    <section class="row">
        <div class="prj_common_info">
            <h3>预期成果</h3><span class="yw-line"></span>
            <a href="javascript:;" id="resultToggle" data-flag="true"><span class="icon-double-angle-up"></span></a>
        </div>
        <div class="toggle_wrap" id="resultToggle_wrap">
            <form class="form-horizontal" novalidate>

                <div class="form-group col-sm-12 col-md-12 col-lg-12 prj-plan">
                        <%--<label class="col-xs-1 " style="width:100px;">成果形式：</label>--%>

                        <%--<div class="form-group col-sm-10 col-md-10 col-lg-10  prj_check_span">--%>
                        <%--<form:checkboxes disabled="true" path="resultType" items="${fns:getDictList('project_result_type')}" itemLabel="label" itemValue="value"  />--%>
                        <%--<form:hidden id="resultTypeStr"  path="resultType" value="${projectDeclare.resultType}"/>--%>
                        <%--</div>--%>
                    <label style="width:100px;">成果形式：</label>
                    <span><form:checkboxes disabled="true" path="resultType"
                                           items="${fns:getDictList('project_result_type')}" itemLabel="label"
                                           itemValue="value"/>
						<form:hidden id="resultTypeStr" path="resultType" value="${projectDeclare.resultType}"/>
					</span>
                </div>

                <div class="form-group col-sm-12 col-md-12 col-lg-12">
                    <label class="col-xs-1 " style="width:100px;padding-left: 0">成果说明：</label>
                    <c:if test="${fn:length(projectDeclare.resultContent) > 0}">
                        <p class="prj_desc col-xs-10" style="height:auto">
                                ${projectDeclare.resultContent}
                        </p>
                    </c:if>
                </div>
            </form>
        </div>
    </section>

    <section class="row">
        <div class="prj_common_info">
            <h3>经费预算</h3><span class="yw-line"></span>
            <a href="javascript:;" id="budgetToggle" data-flag="true"><span class="icon-double-angle-up"></span></a>
        </div>
        <div class="budget" id="budgetToggle_wrap">
            <p >${projectDeclare.budget}</p>
        </div>
    </section>

    <section class="row">
        <div class="prj_common_info">
            <h3>附件</h3><span class="yw-line"></span>
            <a href="javascript:;" id="uploadFile" data-flag="true"><span class="icon-double-angle-up"></span></a>
        </div>
        <div class="toggle_wrap" id="uploadFile_wrap">

            <div class="accessories">
                <c:forEach items="${fileInfo}" var="item" varStatus="status">
                    <div class="accessory">
                        <div class="accessory-info">
                            <a class="accessory-file"
                               title="<c:if test="${not empty item.viewUrl}">预览</c:if>${item.name}"
                               href="javascript: void(0);" data-id="${item.id}" data-view-url="${item.viewUrl}"
                               data-ftp-url="${item.arrUrl}" data-name="${item.name}" data-file-id="">
                                <img src="${ctxImg}/filetype/${fns:getSuffixValue(item.suffix)}.png">
                                <span class="accessory-name">${item.arrName}</span></a>
                            <i title="下载${item.arrName}" class="btn-downfile-newaccessory"></i>
                        </div>
                    </div>
                </c:forEach>
            </div>
                <%--<sys:frontFileUpload fileitems="${fileInfo}" filepath="proCloseForm"  readonly="true"></sys:frontFileUpload>--%>
                <%--<section>--%>
                <%--<ul class="prj_file_list">--%>
                <%--<c:forEach items="${fileInfo}" var="item" varStatus="status">--%>
                <%--<li>--%>
                <%--<img src="${ctxImg}/filetype/${fns:getSuffixValue(item.suffix)}.png"/>--%>
                <%--<a href="javascript:void(0)" onclick="downFile('${item.arrUrl}','${item.arrName}')">${item.arrName}</a>--%>
                <%--</li>--%>
                <%--</c:forEach>--%>
                <%--</ul>--%>
                <%--</section>--%>
        </div>
    </section>

</form:form>

<script>
    +function ($) {
        var uploader = new Uploader(null, {
            containerId: '#uploadFile_wrap'
        })
    }(jQuery);
</script>

