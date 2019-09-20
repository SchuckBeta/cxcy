<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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

    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}/page-innovation">双创项目</a></li>
        <li class="active">申报</li>
    </ol>
    <div class="row-step-cyjd mgb40">
        <div class="step-indicator">
            <a class="step completed">第一步（填写个人基本信息）</a>
            <a class="step">第二步（填写项目基本信息）</a>
            <a class="step">第三步（提交项目申报附件）</a>
        </div>
    </div>


    <div class="topbar-block">
        <div class="row row-top-bar">
            <div class="col-xs-3">
                <p class="text-center topbar-item">项目编号：${proModel.competitionNumber}</p>
            </div>
            <div class="col-xs-3">
                <p class="text-center topbar-item">填报日期：<fmt:formatDate value='${proModel.createDate}'
                                                                        pattern='yyyy-MM-dd'/></p>
            </div>
            <div class="col-xs-3">
                <p class="text-center topbar-item"> 申报人：${leader.name}</p>
            </div>
            <div class="col-xs-3">
                <p class="text-center topbar-item"> 学号：${leader.no}</p>
            </div>
        </div>
    </div>


    <div class="row-apply">
        <h4 class="titlebar">${proModelType}申报</h4>
        <form:form id="form1" modelAttribute="proModel" action="#" method="post" class="form-horizontal"
                   enctype="multipart/form-data">
            <input type='hidden' id="usertype" value="${cuser.userType}"/>
            <form:hidden path="id"/>
            <%--<form:hidden path="actYwId"/>--%>
            <input type="hidden" name="actYwId" id="actYwId" value="${actYw.id}"/>
            <form:hidden path="year"/>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">项目负责人：</label>
                    <p class="form-control-static">${cuser.name}</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">性别：</label>
                    <p class="form-control-static">${fns:getDictLabel(cuser.sex,'sex','')}</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">所属学院：</label>
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


        </form:form>

        <div class="form-actions-cyjd text-center">
            <button type="button" class="btn btn-primary btn-save" onclick="saveStep1(this);">下一步</button>
        </div>
    </div>
</div>


<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">
    $(function () {
        onProjectApplyInit($("[id='actYwId']").val(), $("[id='id']").val(), null);
    });
    function saveStep1(obj) {
        var onclickFn = $(obj).attr("onclick");
        $(obj).removeAttr("onclick");
        $("#form1").attr("action", "/f/proproject/saveStep1");
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
                    top.location = "/f/proproject/applyStep2?actywId=" + $("#actYwId").val() + ($("#id").val() == "" ? "" : "&id=" + $("#id").val());
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
</script>
</body>
</html>