<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>审核</title>
    <meta name="decorator" content="default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/state/titlebar.css">
    <style>
        body {
            font-size: 14px;
        }

        .panel-title {
            position: relative;
            margin-bottom: 20px;
        }

        .panel-title > span {
            position: relative;
            font-size: 14px;
            color: #000;
            font-weight: normal;
            padding-right: 10px;
            background-color: #fff;
            z-index: 10;
        }

        .panel-title .line {
            display: block;
            position: absolute;
            left: 0;
            right: 0;
            top: 10px;
            height: 1px;
            background-color: #f4e6d4;
        }

        .panel-handler {
            position: absolute;
            right: 5px;
            top: -4px;
            font-size: 16px;
            color: #e9432d;
            text-decoration: none;
        }

        .panel-handler:hover {
            color: #e9432d;
            text-decoration: none;
        }

        .accordion-heading, .table th {
            background: transparent;
        }

        .table thead tr {
            background-color: #f4e6d4;
        }

        .form-wrap {
            padding: 0 15px;
        }

        .panel-body {
            padding: 0 30px;
            margin-bottom: 30px;
        }

        .pf-item > strong {
            display: inline-block;
            width: 110px;
            text-align: right;
            vertical-align: middle;
        }

        .pf-item {
            font-size: 14px;
            line-height: 20px;
        }

        .table-identifying {
            display: inline-block;
            padding: 6px 12px;
            background-color: #eee;
            border-radius: 3px;
        }

        .controls-static {
            margin-bottom: 0;
            line-height: 26px;
        }

        .control-group {
            border-bottom: none;
        }

        .table-pf .require-star {
            color: red;
            margin-right: 5px;
        }

        .table-pf input {
            margin-bottom: 0;
        }

        .table {
            margin-bottom: 20px;
        }

        .score-error-msg {
            margin-bottom: 0;
        }

        .btn-primary-oe {
            background: #e9432d;
            color: #fff;
        }

        .btn-primary-oe:hover, .btn-primary-oe:focus {
            background: #e9432d;
            color: #fff;
        }
    </style>
</head>

<body>
<div class="container-fluid">
    <div class="edit-bar edit-bar-tag clearfix" style="margin-top: 30px">
        <div class="edit-bar-left">
			<span>
               查看
			</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-wrap">
        <form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/promodelAudit"
                   method="post">
            <form:hidden path="id"/>
            <form:hidden path="act.taskId"/>
            <form:hidden path="act.taskName"/>
            <form:hidden path="act.taskDefKey"/>
            <form:hidden path="act.procInsId"/>
            <form:hidden path="act.procDefId"/>
            <input name="actionPath" type="hidden"value="${actionPath}"/>
            <input name="gnodeId" type="hidden"value="${gnodeId}"/>
			<%--<div class="panel-body">
                <div class="row-fluid">
                    <div class="span6">
                        <p class="pf-item"><strong>申报人：</strong>${fns:getUserById(proModel.declareId).name}</p>
                    </div>
                </div>
            </div>--%>

            <div id="proPF" class="panel-body collapse in">
                          <div class="row-fluid">
                              <div class="span6">
                                  <p class="pf-item"><strong>项目编号：</strong>${proModel.competitionNumber}</p>
                                  <p class="pf-item"><strong>申报人：</strong>${fns:getUserById(proModel.declareId).name}</p>
                                  <p class="pf-item"><strong>学号(毕业年份)：</strong>${fns:getUserById(proModel.declareId).no}</p>
                                  <p class="pf-item"><strong>联系电话：</strong>${fns:getUserById(proModel.declareId).mobile}</p>
                              </div>
                              <div class="span6">
                                  <p class="pf-item"><strong>填表日期：</strong><fmt:formatDate value="${proModel.subTime}"
                                                                                           pattern="yyyy-MM-dd"/></p>
                                  <p class="pf-item"><strong>学院：</strong>
                                      <c:if test="${fns:getUserById(proModel.declareId).office!=null}">
                                          ${fns:getUserById(proModel.declareId).office.name}
                                      </c:if>
                                  </p>
                                  <p class="pf-item"><strong>专业年级：</strong>
                                          ${fns:getProfessional(fns:getUserById(proModel.declareId).professional)}
                                  </p>
                                  <p class="pf-item"><strong>E-mail：</strong>${fns:getUserById(proModel.declareId).email}</p>
                              </div>
                          </div>
                      </div>


            <div class="panel-title">
                <span>项目基本信息</span>
                <i class="line"></i>
            </div>
            <div id="panelBaseInfo" class="panel-body collapse in">
                <div class="row-fluid">
                    <div class="span6">
                        <p class="pf-item"><strong>参赛项目名称：</strong>${proModel.pName}</p>
                    </div>
                    <div class="span6">
                        <p class="pf-item">
                            <strong>项目类型：</strong> ${fns:getDictLabel(proModel.proCategory, "project_type", "")}</p>
                        <p class="pf-item">
                            <strong>项目来源：</strong> ${fns:getDictLabel(proModel.projectSource, "project_source", "")}</p>
                    </div>
                </div>
            </div>

            <div id="panelTeamInfo" class="panel-body collapse in">
                <p><strong>项目团队：</strong>${team.name}</p>
                <div class="table-identifying">学生团队</div>
                <table class="table table-bordered table-hover table-condensed" style="margin-bottom: 20px;">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>手机号</th>
                        <th>所在学院</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                    <c:forEach items="${teamStu}" var="item" varStatus="status">
                        <tr>
                           <td>${status.index+1}</td>
                           <td><c:out value="${item.name}"/></td>
                           <td><c:out value="${item.no}"/></td>
                           <td><c:out value="${item.mobile}"/></td>
                           <td><c:out value="${item.org_name}"/></td>
                       </tr>
                    </c:forEach>
                    </c:if>
                    </tbody>
                </table>
                <div class="table-identifying">老师团队</div>
                <table class="table table-bordered table-hover table-condensed">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th width="175">E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                        <c:forEach items="${teamTea}" var="item" varStatus="status">
                            <tr>
                                <td>${status.index+1}</td>
                                <td><c:out value="${item.name}"/></td>
                                <td><c:out value="${item.no}"/></td>
                                <td><c:out value="${item.teacherType}"/></td>
                                <td><c:out value="${fns:getDictLabel(item.technical_title,'postTitle_type','')}"/></td>
                                <td><c:out value="${fns:getDictLabel(item.education,'enducation_level','')}"/></td>
                                <td><c:out value="${item.mobile}"/></td>
                                <td><c:out value="${item.email}"/></td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    </tbody>
                </table>
            </div>
            <div class="panel-title">
                <span>项目介绍</span>
                <i class="line"></i>
                <a class="panel-handler" href="javascript:void (0);" data-toggle="collapse" data-target="#projectIntro"><i
                        class="icon-double-angle-up"></i></a>
            </div>
            <div id="projectIntro" class="panel-body panel-body-pro-intro collapse in">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label" style="width: auto">项目简介：</label>
                        <div class="controls" style="margin-left: 68px;">
                            <p class="controls-static">${proModel.introduction}</p>
                        </div>
                    </div>
                </div>
            </div>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                   <span>附     件</span>
                   <i class="line"></i>
                </div>
            </div>
            <div id="fujian" class="accessory-box">
                <sys:frontFileUpload fileitems="${sysAttachments}" filepath="project" ></sys:frontFileUpload>
            </div>

            <div class="text-center mar_bottom">
                <button class="btn" type="button" onclick="history.go(-1)">返回</button>
            </div>
        </form:form>
    </div>
</div>

<script type="text/javascript">
    $(function(){
        var $inputForm = $('#inputForm');
        $inputForm.validate({
            rules: {
                source: {
                    maxlength: 300
                }
            },
            messages: {
                source: {
                    maxlength: "最多输入{0}个字"
                }
            },
            submitHandler: function(form){
                form.submit();
            }
        });

        jQuery.validator.addMethod("maxFs", function (value, element) {
            return this.optional(element) || (value >= 0 && value <= 100 && /^([0-9]{1,2}|100)$/.test(value.toString()));
        }, "填写0-100之间的整数");

    })

</script>
</body>

</html>