<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
</head>

<body>
<div class="container-fluid container-fluid-audit">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>${taskName}</span> <i class="line weight-line"></i>
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
            <input name="actionPath" type="hidden" value="${actionPath}"/>
            <input name="gnodeId" type="hidden" value="${gnodeId}"/>
            <%--<div class="panel-body">
                <div class="row-fluid">
                    <div class="span6">
                        <p class="pf-item"><strong>申报人：</strong>${fns:getUserById(proModel.declareId).name}</p>
                    </div>
                </div>
            </div>--%>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>申报人基本信息</span> <i class="line"></i> <a
                        data-toggle="collapse" href="#projectPeopleDetail"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="projectPeopleDetail" class="panel-body collapse in">
                <div class="row-fluid row-info-fluid">
                    <div class="span4">
                        <div class="item-label">
                            作品编号：
                        </div>
                        <div class="items-box">
                            <p class="text-ellipsis">${proModel.competitionNumber}</p>
                        </div>
                    </div>
                    <div class="span4">
                        <span class="item-label">负责人：</span>
                        <div class="items-box">
                                ${proModel.deuser.name}
                        </div>
                    </div>
                    <div class="span4">
                        <span class="item-label">申报日期：</span>
                        <div class="items-box">
                            <fmt:formatDate value="${proModel.createDate}"></fmt:formatDate>
                        </div>
                    </div>


                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span4">
                        <span class="item-label">学号：</span>
                        <div class="items-box">
                            <p class="text-ellipsis"> ${proModel.deuser.no}</p>
                        </div>
                    </div>
                    <div class="span4">
                        <span class="item-label">性别：</span>
                        <div class="items-box">
                            <c:choose>
                                <c:when test="${proModel.deuser.sex == '1'}">男</c:when>
                                <c:when test="${proModel.deuser.sex == '0'}">女</c:when>
                            </c:choose>
                        </div>
                    </div>
                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span4">
                        <span class="item-label">参赛学院：</span>
                        <div class="items-box">
                                ${proModel.deuser.office.name}
                        </div>
                    </div>
                    <div class="span4">
                        <span class="item-label">专业：</span>
                        <div class="items-box">
                                ${proModel.deuser.subject.name}
                        </div>
                    </div>

                </div>
                <div class="row-fluid row-info-fluid">
                    <div class="span4">
                        <span class="item-label">联系电话：</span>
                        <div class="items-box">
                                ${proModel.deuser.mobile}
                        </div>
                    </div>
                    <div class="span4">
                        <span class="item-label">Email：</span>
                        <div class="items-box">
                                ${proModel.deuser.email}
                        </div>
                    </div>
                </div>
            </div>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>参赛作品信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                                href="#teamDetail"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="teamDetail" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="row-fluid row-info-fluid">
                        <div class="span11">
                            <span class="item-label">作品名称：</span>
                            <div class="items-box">
                                    ${proModel.pName}
                            </div>
                        </div>
                    </div>

                    <div class="row-fluid row-info-fluid">
                        <div class="span11">
                            <span class="item-label">所属领域：</span>
                            <div class="items-box">
                                    ${proModelGzsmxx.regionName}
                            </div>
                        </div>
                    </div>

                    <div class="row-fluid row-info-fluid">
                        <div class="span11">
                            <span class="item-label">领域分组：</span>
                            <div class="items-box">
                                    ${proModelGzsmxx.regionGroupName}
                            </div>
                        </div>
                    </div>

                    <div class="row-fluid row-info-fluid">
                        <div class="span11">
                            <span class="item-label">作品简介：</span>
                            <div class="items-box">
                                    ${proModel.introduction}
                            </div>
                        </div>
                    </div>
                </div>
            </div>


            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>团队信息</span> <i class="line"></i> <a data-toggle="collapse"
                                                              href="#teamInfo"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
            <div id="teamInfo" class="panel-body collapse in">
                <div class="panel-inner">
                    <div class="row-fluid row-info-fluid">
                        <div class="span6">
                            <span class="item-label">团队名称：</span>${team.name}
                        </div>
                    </div>


                    <div class="table-caption">学生团队</div>
                    <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>姓名</th>
                            <th>学号</th>
                            <th>手机号</th>
                            <th>所在学院</th>
                        </tr>
                        </thead>
                        <tbody id="teamTableStudent">
                        <c:forEach items="${teamStu}" var="student" varStatus="status">
                            <tr>
                                <td>${status.index +1}</td>
                                <td>${student.name}</td>
                                <td>${student.no}</td>
                                <td>${student.mobile}</td>
                                <td>${student.org_name}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                    <div class="table-caption">指导教师</div>
                    <table
                            class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
                        <thead>
                        <tr>
                            <th>序号</th>
                            <th>姓名</th>
                            <th>工号</th>
                            <th>导师来源</th>
                            <th>职称（职务）</th>
                            <th>学历</th>
                            <th>联系电话</th>
                            <th>E-mail</th>
                        </tr>
                        </thead>
                        <tbody id="teamTableTeacher">
                        <c:forEach items="${teamTea}" var="teacher" varStatus="status">
                            <tr>
                                <td>${status.index +1}</td>
                                <td>${teacher.name}</td>
                                <td>${teacher.no}</td>
                                <td>${teacher.teacherType}</td>
                                <td>${teacher.postTitle}</td>
                                <td>${fns:getDictLabel(teacher.education,'enducation_level','')}</td>
                                <td>${teacher.mobile}</td>
                                <td>${teacher.email}</td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>

                </div>
            </div>


            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>附件</span> <i class="line"></i> <a data-toggle="collapse"
                                                            href="#teamDetail"><i
                        class="icon-collaspe icon-double-angle-up"></i></a>
                </div>
            </div>
			<c:if test="${fn:length(sysAttachments) > 0}">
	            <div id="attDetail" class="panel-body collapse in">
	                <div class="panel-inner">
	                    <div class="row-fluid row-info-fluid">
	                        <div class="span11">
	                            <span class="item-label">附件：</span>
	                            <div class="items-box">
	                                <sys:frontFileUpload className="accessories-h30" fileitems="${sysAttachments}"
	                                                     readonly="true"></sys:frontFileUpload>
	                            </div>
	                        </div>
	                    </div>
	                </div>
	            </div>
            </c:if>


            <!-- 审核记录 -->
            <c:if test="${not empty actYwAuditInfos}">

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>审核记录</span> <i class="line"></i><a data-toggle="collapse" href="#actYwAuditInfos"><i
                            class="icon-collaspe icon-double-angle-up"></i></a>
                    </div>
                </div>

                <div id="actYwAuditInfos" class="panel-body collapse in">
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
                                        <td><fmt:formatDate value="${item.updateDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
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

            </c:if>

            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>审核建议及意见</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="panel-body">
                <div class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label">审核：</label>
                        <div class="controls">
                            <form:select path="grade" class="input-large required">
                                <form:option value="" label="请选择"/>
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
                            </form:select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">建议及意见：</label>
                        <div class="controls">
                                <textarea name="source" maxlength="300" rows="5"
                                          class="input-xxlarge"
                                          placeholder="请给予您的意见和建议"></textarea>
                        </div>
                    </div>
                </div>
            </div>
            <div class="text-center">
                <button type="submit" class="btn btn-primary">提交</button>
                <button type="button" class="btn btn-default"  onclick="history.go(-1)">返回</button>
            </div>
        </form:form>
    </div>
</div>

<script type="text/javascript">
    $(function () {
        var $inputForm = $('#inputForm');
        var $grade = $('select[name="grade"]');
        var $source = $('textarea[name="source"]');


//        $grade.on('change', function (e) {
//            var val = $(this).val();
//            if (val == 0) {
//                $source.addClass('required')
//                $source.parents('.controls').prev().find('i').removeClass('hide');
//                return false;
//            }
//            $source.removeClass('required');
//            $source.parents('.controls').prev().find('i').addClass('hide');
//            return false;
//        })

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
            submitHandler: function (form) {
                $inputForm.find('button[type="submit"]').prop('disabled', true);
                form.submit();
                window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
            }
        });

        jQuery.validator.addMethod("maxFs", function (value, element) {
            return this.optional(element) || (value >= 0 && value <= 100 && /^([0-9]{1,2}|100)$/.test(value.toString()));
        }, "填写0-100之间的整数");

    })

</script>
</body>

</html>
