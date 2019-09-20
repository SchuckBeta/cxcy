<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${cmsDeclareNotify.title }</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <script type="text/javascript">
        var validate;
        UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
        UE.Editor.prototype.getActionUrl = function (action) {
            if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo' || action == 'uploadfile') {
                return $frontOrAdmin+'/ftp/ueditorUpload/uploadTempFormal?folder=declarenotify';
            } else {
                return this._bkGetActionUrl.call(this, action);
            }
        }
        $(document).ready(function () {
            var ue = UE.getEditor('content');
            validate = $("#inputForm").validate({
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }

                }
            });

        });
        function save(obj) {
            if (validate.form()) {
                if (UE.getEditor('content').getContent() == "") {
                    top.$.jBox.tip('请填写内容', 'warning');
                } else {
                    loading('正在提交，请稍等...');
                    var onclickFn = $(obj).attr("onclick");
                    $(obj).removeAttr("onclick");
                    $(obj).prop('disabled', true);
                    $("#inputForm").ajaxSubmit(function (data) {
                        closeLoading();
                        if (data.ret == 1) {
                            alertx(data.msg, function () {
                                back();
                            });
                        } else {
                            $(obj).attr("onclick", onclickFn);
                            alertx(data.msg);
                        }
                        $(obj).prop('disabled', false);
                    });
                }

            }
        }
        function back() {
            location.href = document.referrer;
        }
        function preview() {
            $("#inputForm").attr("action", $("#front_url").val() + "/cmsDeclareNotify/preView");
            $("#inputForm").attr("target", "_blank");
            $("#inputForm").submit();
            $("#inputForm").attr("action", "/a/cms/cmsDeclareNotify/save");
            $("#inputForm").removeAttr("target");
        }
    </script>


</head>
<body>
<input type="hidden" id="front_url" value="${front_url}"/>

<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>申报通知</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form id="inputForm" modelAttribute="cmsDeclareNotify" action="${ctx}/cms/cmsDeclareNotify/save"
               method="post" class="form-horizontal">
        <form:hidden path="id"/>
        <form:hidden path="categoryId"/>

        <div class="control-group">

            <label class="control-label"><i>*</i>通知分类：
            </label>
            <div class="controls">
                <form:select path="type" class="required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('0000000259')}" itemValue="value" itemLabel="label"
                                  htmlEscape="false"/></form:select>
            </div>
        </div>
        <div class="control-group">

            <label class="control-label"><i>*</i>标题：
            </label>
            <div class="controls">
                <form:input path="title" htmlEscape="false" maxlength="200" class="input-xlarge required"/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><i>*</i>内容：
            </label>
            <div class="controls">
                <form:textarea path="content" htmlEscape="false" rows="6" maxlength="2000" class="required"
                               style="display: block; width: 100%; max-width: 1200px; min-width: 750px;"/>
            </div>

        </div>
        <div class="control-group">
            <label class="control-label">是否发布：</label>
            <div class="controls">
                <form:select path="isRelease"
                             class="form-control">
                    <form:options items="${fns:getDictList('yes_no')}" itemValue="value" itemLabel="label"
                                  htmlEscape="false"/></form:select>
            </div>
        </div>
        <div class="form-actions">
            <button class="btn btn-primary" type="button" onclick="preview()">预览</button>
            <button class="btn btn-primary" type="button" onclick="save(this)">保存</button>
            <button class="btn btn-default" type="button" onclick="back()">返回</button>
        </div>
    </form:form>
</div>


</body>
</html>