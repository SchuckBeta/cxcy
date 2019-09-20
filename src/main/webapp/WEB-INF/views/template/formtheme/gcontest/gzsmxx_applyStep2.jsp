<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <link href="${ctxStatic}/cropper/cropper.min.css" rel="stylesheet"/>
    <script src="${ctxStatic}/cropper/cropper.min.js" type="text/javascript"></script>
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
            <a class="step">第一步（团队信息）</a>
            <a class="step completed">第二步（项目信息）</a>
            <a class="step">第三步（提交参赛表单）</a>
        </div>
    </div>
    <div class="row-apply">
        <h4 class="titlebar">桂子山创业梦想秀</h4>
        <form:form id="form1" modelAttribute="proModelGzsmxx" action="#" method="post" class="form-horizontal"
                   enctype="multipart/form-data">
            <input type="hidden" name="proModel.declareId" id="declareId" value="${cuser.id}"/>
            <input type="hidden" name="id" id="id" value="${proModelGzsmxx.id}"/>
            <input type="hidden" name="actYwId" id="actYwId" value="${proModelGzsmxx.proModel.actYwId}"/>
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
                    <span>参赛作品信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>作品名称：</label>
                <div class="col-xs-10">
                    <input type="text" class="form-control required fill" name="proModel.pName" maxlength="30" placeholder="最多30个字符" value="${proModelGzsmxx.proModel.pName}" data-value="${proModelGzsmxx.proModel.pName}">
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>所属领域：</label>
                <div class="col-xs-10">
                    <c:if test="${dicts!=null&&dicts.size() > 0}">
                        <c:forEach items="${dicts}" var="dict" varStatus="status">
                            <label class="radio-inline">
                                <input type="radio" name="region" data-value="" value="${dict.value}" data-value="${proModelGzsmxx.region}" <c:if test="${dict.value eq proModelGzsmxx.region}">checked</c:if>>${dict.label}
                            </label>
                        </c:forEach>
                    </c:if>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>领域分组：</label>
                <div class="col-xs-10">
                    <select class="form-control required fill" id="regionGroup" name="regionGroup" data-value="${proModelGzsmxx.regionGroup}">
                        <c:if test="${dicts!=null && dicts.size() > 0}">
                            <c:forEach items="${regionGroupList}" var="regionGroup" varStatus="status">
                                <option value="${regionGroup.value}" <c:if test="${regionGroup.value eq proModelGzsmxx.regionGroup}"> selected</c:if>>${regionGroup.label}</option>
                            </c:forEach>
                        </c:if>
                        <c:if test="${dicts!=null && dicts.size() <= 0}">
                            <option value="">-请选择分组-</option>
                        </c:if>
                    </select>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-2"><i>*</i>作品简介：</label>
                <div class="col-xs-10">
                    <textarea class="form-control required fill" rows="5" name="proModel.introduction" maxlength="2000" placeholder="最多2000个字符" data-value="${proModelGzsmxx.proModel.introduction}">${proModelGzsmxx.proModel.introduction}</textarea>
                </div>
            </div>
        </form:form>

        <div class="form-actions-cyjd text-center" style="border: none">
            <a id="prevbtn" class="btn btn-primary" href="javascript:void(0)" onclick="prevStep()"
               style="margin-right: 10px;">上一步</a>
            <a id="savebtn" class="btn btn-primary" href="javascript:void(0)" onclick="saveStep2UnCheck()"
               style="margin-right: 10px;">保存</a>
            <button id="nextbtn" type="button" class="btn btn-primary btn-save" onclick="saveStep2(this);">下一步</button>
        </div>
    </div>
</div>


<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">

    var validate1 = $("#form1").validate({
        errorPlacement: function (error, element) {
            error.insertAfter(element);
        }
    });

    $(function(){

        $("input[name='region']").on("click", function() {
            $("#regionGroup").empty();
            var value = $(this).val();
            $.ajax({
                type: "GET",
                url: "/f/proModelGzsmxx/getRegionGroup",
                data: "value=" + value,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function (data) {
                    if (data.datas) {
                        $.each(data.datas, function (i, v) {
                            console.log(i, v);
                            $("#regionGroup").append('<option value="'+data.datas[i].value+'">'+data.datas[i].label+'</option>')
                        });
                    }
                },
                error: function (msg) {
                    dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
                        dialogClass: 'dialog-cyjd-container none-close',
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-sm btn-primary',
                            click: function () {
                                $(this).dialog('close');
                                top.location = top.location;
                            }
                        }]
                    });
                }
            });
        });
    });

    function toPrevStep(id) {
        top.location = "/f/proModelGzsmxx/applyStep1?actywId=" + $("#actYwId").val() + (id == "" ? "" : "&id=" + id);
    }

    function prevStep() {
        if (checkIsFill()) {
            saveStep2UnCheck("1");
        } else {
            var id = $("#id").val();
            toPrevStep(id);
        }
    }

    function saveStep2(obj) {
        if (validate1.form()) {
            var onclickFn = $(obj).attr("onclick");
            $(obj).removeAttr("onclick");
            $("#form1").attr("action", "/f/proModelGzsmxx/ajaxSave2");
            $(obj).prop('disabled', true);
            $("#form1").ajaxSubmit(function (data) {
                if (checkIsToLogin(data)) {
                    dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
                        dialogClass: 'dialog-cyjd-container none-close',
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
                        top.location = "/f/proModelGzsmxx/applyStep3?id=" + data.id;
                    } else {
                        $(obj).attr("onclick", onclickFn);
                        dialogCyjd.createDialog(0, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
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
    }

    function saveStep2UnCheck(type) {
        var preFn = $("#prevbtn").attr("onclick");
        $("#prevbtn").removeAttr("onclick");
        var saveFn = $("#savebtn").attr("onclick");
        $("#savebtn").removeAttr("onclick");
        var nextFn = $("#nextbtn").attr("onclick");
        $("#nextbtn").removeAttr("onclick");
        $("#form1").attr("action", "/f/proModelGzsmxx/ajaxUncheckSave2");
        $("#form1").ajaxSubmit(function (data) {
            if (checkIsToLogin(data)) {
                dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
                    dialogClass: 'dialog-cyjd-container none-close',
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
                $("#prevbtn").attr("onclick", preFn);
                $("#savebtn").attr("onclick", saveFn);
                $("#nextbtn").attr("onclick", nextFn);
                if (data.ret == 1) {
                    if ($("#id").val() == "") {
                        $("#id").val(data.id);
                    }
                    $(".fill").each(function (i, v) {
                        $(v).data("value", $(v).val());
                    });
                    if (type == "1") {
                        toPrevStep(data.id);
                    } else {
                        dialogCyjd.createDialog(1, data.msg, {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-sm btn-primary',
                                click: function () {
                                    $(this).dialog('close');
                                }
                            }]
                        });
                    }

                } else {
                    dialogCyjd.createDialog(0, data.msg, {
                        dialogClass: 'dialog-cyjd-container none-close',
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
        });
    }

    function checkIsFill() {
        var tag = false;
        $(".fill").each(function (i, v) {
            console.log($(v).val(), $(v).data("value"));
            if ($(v).val() != "" && $(v).data("value") != $(v).val()) {
                tag = true;
                return false;
            }
        });
        return tag;
    }
</script>
</body>
</html>