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
    <ol class="breadcrumb">
        <li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>
        <li><a href="${ctxFront}//page-innovation">双创项目</a></li>
        <li class="active">申报</li>
    </ol>
    <div class="row-step-cyjd mgb40">
        <div class="step-indicator">
            <a class="step completed">第一步（填写个人基本信息）</a>
            <a class="step completed">第二步（填写项目基本信息）</a>
            <a class="step">第三步（提交项目申报附件）</a>
        </div>
    </div>
    <div class="row-apply">
        <h4 class="titlebar">桂子山创业梦想秀</h4>
        <form:form id="form1" modelAttribute="proModel" action="#" method="post" class="form-horizontal"
                   enctype="multipart/form-data">
            <div class="row row-user-info">
                <div class="col-xs-6">
                    <label class="label-static">填报日期：</label>
                    <p class="form-control-static">2018/5/11</p>
                </div>
                <div class="col-xs-6">
                    <label class="label-static">大赛编号：</label>
                    <p class="form-control-static"></p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>参赛作品信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>作品名称：</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>所属领域：</label>
                <div class="col-xs-10">
                    <label class="radio-inline">
                        <input type="radio">产品
                    </label>
                    <label class="radio-inline">
                        <input type="radio">服务
                    </label>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>领域分组：</label>
                <div class="col-xs-10">
                    <select class="form-control">
                        <option value="">-请选择分组-</option>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>作品简介：</label>
                <div class="col-xs-10">
                    <textarea class="form-control" rows="5"></textarea>
                </div>
            </div>
        </form:form>

        <div class="form-actions-cyjd text-center" style="border: none">
            <button type="button" class="btn btn-primary btn-save" onclick="saveStep1(this);">下一步</button>
        </div>
    </div>
</div>


<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">

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