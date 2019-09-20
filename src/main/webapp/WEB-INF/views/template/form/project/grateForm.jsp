<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html>
<head>
    <title>评级审核</title>
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
               审核
			</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-wrap">
        <form:form id="inputForm" modelAttribute="proModel" action="${ctx}/promodel/proModel/promodelGateAudit"
                   method="post">
            <form:hidden path="id"/>
            <form:hidden path="act.taskId"/>
            <form:hidden path="act.taskName"/>
            <form:hidden path="act.taskDefKey"/>
            <form:hidden path="act.procInsId"/>
            <form:hidden path="act.procDefId"/>
            <input name="actionPath" type="hidden"value="${actionPath}"/>
            <input name="gnodeId" type="hidden"value="${gnodeId}"/>
			<div class="panel-body">
                <div class="row-fluid">
                    <div class="span6">
                        <p class="pf-item"><strong>申报人：</strong>${fns:getUserById(proModel.declareId).name}</p>
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
                                   <td><c:out value="${item.technical_title}"/></td>
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
                <span>审核建议及意见</span>
                <i class="line"></i>
            </div>
            <div class="panel-body">
                <div class="form-horizontal">
                    <div class="control-group">
                      <%-- 添加内容 --%>
                        <%--<c:if test="${proProject!=null &&proProject.id!=null}">
                            <label class="control-label">评级：</label>
                            <div class="controls">

                                <form:select path="finalStatus" class="form-control input-large required">
                                    ${fns:getGrateSelect(proProject.id)}
                                </form:select>
                             </div>
                        </c:if>--%>
                       <label class="control-label">审核：</label>
                        <div class="controls">
							<form:select path="grade" class="form-control input-large required">
                                <c:choose>
                                    <c:when test="${not empty actYwStatusList}">
                                        <c:forEach items="${actYwStatusList}" var="item">
                                            <form:option value="${item.status}">${item.state}</form:option>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <form:option value="1">通过</form:option>
                                        <form:option value="0">不通过</form:option>
                                    </c:otherwise>
                                </c:choose>
								<%--<form:option value="" label="请选择"/>--%>
								<%--<form:option value="0" label="不合格"/>--%>
								<%--<form:option value="1" label="合格"/>--%>
							</form:select>
                        </div>

                    </div>
                    <div class="control-group">
                        <div class="control-group">
                            <label class="control-label">建议及意见：</label>
                            <div class="controls">
                                <textarea name="source" maxlength="300" rows="5" class="form-control input-xxlarge  required"
                                          placeholder="请给予您的意见和建议"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="text-center mar_bottom">
                <button type="submit" id="btnSubmit" class="btn btn-primary-oe">提交</button>
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
