<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@ include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        var numberReg = /^[2-9]|[1-9][0-9]+$/;
        var numReg = /^[0-9]*[1-9][0-9]*$/;
        jQuery.validator.addMethod("ckNumber", function (value, element) {
            return this.optional(element) || numberReg.test(value);
        }, "请填写2以上的整数");
        jQuery.validator.addMethod("ckRatio1", function (value, element) {
            if (!this.optional(element)) {
                var rs = value.split(":");
                for (var i = 0; i < rs.length; i++) {
                    if (!numReg.test(rs[i])) {
                        return false;
                    }
                }
            }
            return true;
        }, "格式不对,请以冒号分隔正整数,n:n:n");
        jQuery.validator.addMethod("ckRatio2", function (value, element) {
            if (!this.optional(element) && numberReg.test($("#number").val())) {
                var rs = value.split(":");
                if (rs.length != parseInt($("#number").val())) {
                    return false;
                }
            }
            return true;
        }, function (params, element) {
            return "请填写" + $("#number").val() + "个比例";
        });
        $(document).ready(function () {
            $("#inputForm").validate({
                submitHandler: function (form) {
                    resetTip();
                    form.submit();
                },
                rules: {
                    "number": {
                    	ckNumber:true,
                        remote: {
                            async: true,
                            url: "/a/sco/scoAllotRatio/checkNumber",     //后台处理程序
                            type: "post",               //数据发送方式
                            data: {                     //要传递的数据
                                id: function () {
                                    return $("#id").val();
                                },
                                confid: function () {
                                    return $("#affirmConfId").val();
                                }
                            }
                        }
                    },
                    "ratio": {
                        ckRatio1: true,
                        ckRatio2: true
                    }
                },
                messages: {
                    "number": {
                        remote: "已经存在"
                    }
                },
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox")) {
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
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>创建学分配比</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form class="form-horizontal" id="inputForm" autocomplete="off"
               modelAttribute="scoAllotRatio" action="save" method="post">
        <input type="hidden" id="id" name="id" value="${scoAllotRatio.id }"/>
        <input type="hidden" id="secondName" name="secondName"value="${secondName}"/>
        <input type="hidden" id="affirmConfId" name="affirmConfId" value="${scoAllotRatio.affirmConfId }"/>
        <div class="control-group">
            <label class="control-label"><i>*</i>组人数：</label>
            <div class="controls">
                <form:input path="number" cssClass="required digits"
                            placeholder="最小人数为2人"></form:input>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label"><i></i>学分分配比例：</label>
            <div class="controls">
                <form:input path="ratio" placeholder="n:n:n" class="required"></form:input>
            </div>
        </div>
        <div class="control-group">
            <div class="control-group-inline">
                <label class="control-label">备注：</label>
                <div class="controls">
                    <textarea name="remarks" maxlength="255" class="input-xxlarge"
                              rows="5">${scoAllotRatio.remarks }</textarea>
                </div>
            </div>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-default"
                    onclick="javascript:location.href='${ctx}/sco/scoAllotRatio?confId=${scoAllotRatio.affirmConfId }&secondName=学分配比'">
                返回
            </button>
        </div>
    </form:form>
</div>
</body>
</html>