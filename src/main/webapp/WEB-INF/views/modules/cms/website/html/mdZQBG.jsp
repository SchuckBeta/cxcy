<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <link rel="stylesheet" href="${ctxStatic}/chosen/chosen.min.css">
    <script type="text/javascript" src="${ctxStatic}/chosen/chosen.jquery.min.js"></script>
    <script type="text/javascript">
        $(document).ready(function () {
            //$("#name").focus();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    loading('正在提交，请稍等...');
                    form.submit();
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text("输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
        });
    </script>
</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>业务指派表</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form:form class="form-horizontal">
        <%--<form:hidden path="id"/>--%>
        <%--<sys:message content="${message}"/>--%>
        <div class="control-group">
            <label class="control-label"><i>*</i>项目流程编号：</label>
            <div class="controls">
                    <%--<form:input path="ywId" htmlEscape="false" maxlength="64" class="required"/>--%>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">流程节点编号：</label>
            <div class="controls">
                    <%--<form:input path="gnodeId" htmlEscape="false" maxlength="64" class=""/>--%>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><i>*</i>项目id：</label>
            <div class="controls">
                    <%--<form:input path="promodeId" htmlEscape="false" maxlength="64" class="required"/>--%>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">指派人：</label>
            <div class="controls">
                    <%--<form:input path="assignUserId" htmlEscape="false" maxlength="64" class=""/>--%>
                <input type="text">
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">被指派人：</label>
            <div class="controls chosen-assign">
                    <%--<form:input path="revUserId" htmlEscape="false" maxlength="64" class=""/>--%>
                <select data-placeholder="王青铜" multiple="" class="chosen-select" style="visibility: hidden"
                        tabindex="-1">
                    <option value=""></option>
                    <option>American Black Bear</option>
                    <option>Asiatic Black Bear</option>
                    <option>Brown Bear</option>
                    <option>Giant Panda</option>
                    <option>Sloth Bear</option>
                    <option>Sun Bear</option>
                    <option>Polar Bear</option>
                    <option>Spectacled Bear</option>
                    <option value="1234" selected>王清腾</option>
                    <option>陈浩</option>
                    <option>张耀</option>
                </select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">备注：</label>
            <div class="controls">
                    <%--<form:textarea path="remarks" htmlEscape="false" rows="4" maxlength="200" class="input-xxlarge "/>--%>
            </div>
        </div>
        <div class="form-actions">
                <%--<shiro:hasPermission name="actyw:actYwGassign:edit">--%>
            <button type="submit" class="btn btn-primary">保存</button>
                <%--</shiro:hasPermission>--%>
            <button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
        </div>
    </form:form>
</div>
<script>

    $(function () {
        $('.chosen-select').chosen().change(function () {
            console.log($(this).val())
        });

    })

</script>
</body>
</html>