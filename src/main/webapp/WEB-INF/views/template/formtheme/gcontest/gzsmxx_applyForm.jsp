<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>--%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>

<div class="container container-ct">


    <%--<ol class="breadcrumb">--%>
        <%--<li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>--%>
        <%--<li><a href="${ctxFront}//page-innovation">双创大赛</a></li>--%>
        <%--<li class="active">申报</li>--%>
    <%--</ol>--%>
    <div class="edit-bar clearfix" style="margin-top:0;">
        <div class="edit-bar-left">
            <div class="mybreadcrumbs" style="margin:0 0 20px 9px;">
                <i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;<a href="/f" style="color:#333;text-decoration: underline;">首页</a>&nbsp;&gt;&nbsp;双创大赛&nbsp;&gt;&nbsp;申报
            </div>
        </div>
    </div>
    <div class="row-step-cyjd mgb40">
        <div class="step-indicator">
            <a class="step completed">第一步（团队信息）</a>
            <a class="step">第二步（项目信息）</a>
            <a class="step">第三步（提交参赛表单）</a>
        </div>
    </div>

    <div class="row-apply">
        <h4 class="titlebar">桂子山创业梦想秀</h4>
        <form:form id="form1" modelAttribute="proModelGzsmxx" action="#" method="post" class="form-horizontal"
                   enctype="multipart/form-data">
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">填报日期：</label>
                    <p class="form-control-static">${sysdate}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">大赛编号：</label>
                    <p class="form-control-static">${proModelGzsmxx.proModel.competitionNumber}</p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>申报人基本信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">项目负责人：</label>
                    <p class="form-control-static">${cuser.name}</p>
                    <input type="hidden" name="proModel.declareId" id="declareId" value="${cuser.id}"/>
                    <input type="hidden" name="proModel.actYwId" id="actYwId" value="${actYw.id}"/>
                    <input type="hidden" name="proModel.id" id="proModelId" value="${proModelGzsmxx.proModel.id}"/>
                    <input type="hidden" name="proModel.proCategory" id="proCategory" value="7"/>
                    <input type="hidden" name="id" id="id" value="${proModelGzsmxx.id}"/>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">学号：</label>
                    <p class="form-control-static">${cuser.no}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">参赛学院：</label>
                    <p class="form-control-static">${cuser.office.name}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">专业：</label>
                    <p class="form-control-static">${fns:getProfessional(cuser.professional)}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">联系电话：</label>
                    <p class="form-control-static">${cuser.mobile}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">Email：</label>
                    <p class="form-control-static">${cuser.email}</p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>团队信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2" style="width: 190px;"><i>*</i>选择团队：</label>
                <div class="col-xs-6">
                    <form:select onchange="findTeamPerson()"
                                 path="proModel.teamId"
                                 class="form-control has-disabled required proModelTeamId">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${teams}" itemValue="id" itemLabel="name" htmlEscape="false"/>
                    </form:select>
                </div>
                <div class="col-xs-4">
                    <p class="form-control-static gray-color"><c:if test="${teams.size() <= 0}">没有可用团队请</c:if><a href="${ctxFront}//team/indexMyTeamList">创建团队</a></p>
                </div>
            </div>
            <div class="table-title">
                <span>学生团队</span>
            </div>
            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover studenttb">
                <thead>
                <tr id="studentTr">
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
                            <input type="hidden" name="proModel.teamUserHistoryList[${status.index}].userId"
                                   value="${item.userId}">
                        </tr>
                    </c:forEach>
                </c:if>

                </tbody>
            </table>
            <div class="table-title">
                <span>导师</span>
            </div>
            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover teachertb">
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
        </form:form>

        <div class="form-actions-cyjd text-center" style="border: none">
            <button type="button" class="btn btn-primary btn-save" onclick="saveStep1(this);">下一步</button>
        </div>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

<script type="text/javascript">
    $(function () {
        onGcontestApplyInit($("[id='actYwId']").val(), $("[id='id']").val(), null);
    });

    var validate1 = $("#form1").validate({
        errorPlacement: function (error, element) {
            error.insertAfter(element);
        }
    });

    function saveStep1(obj) {
        var onclickFn = $(obj).attr("onclick");
        $(obj).removeAttr("onclick");
        if (validate1.form()) {
            $("#form1").attr("action", "/f/proModelGzsmxx/ajaxSave");
            $(obj).prop('disabled', true);
            $("#form1").ajaxSubmit(function (data) {
                if (checkIsToLogin(data)) {
                    dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
                        dialogClass: 'dialog-cyjd-container',
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-sm btn-primary',
                            click: function () {
                                $(this).dialog('close');
                                top.location = top.location;
                            }
                        }]
                    });
                } else {
                    if (data.ret == 1) {
                        top.location = "/f/proModelGzsmxx/applyStep2?actywId=" + $("#actYwId").val() + "&id=" + data.id;
                    } else {
                        $(obj).attr("onclick", onclickFn);
                        dialogCyjd.createDialog(data.ret, data.msg, {
                            dialogClass: 'dialog-cyjd-container',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-sm btn-primary',
                                click: function () {
                                    $(this).dialog('close');
                                }
                            }]
                        });
                    }
                }
                $(obj).prop('disabled', false);
            });
        }
        $(obj).attr("onclick", onclickFn);
    }
</script>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<%--<script src="${ctxJs}/gzsmxx/mdApplyBak.js?v=11" type="text/javascript" charset="utf-8"></script>--%>
<script src="${ctxJs}/gzsmxx/findteam.js" type="text/javascript" charset="utf-8"></script>
</body>
</html>
