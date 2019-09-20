<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@ include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <%--<link rel="stylesheet" type="text/css" href="${ctxCss}/credit-module-cri.css">--%>

    <script type="text/javascript">
        var scoreReg = /^(\d{1,2}(\.\d{0,1})?|100|100.0)$/;
        jQuery.validator.addMethod("ckScore", function (value, element) {
            return this.optional(element) || scoreReg.test(value);
        }, "0到100最多一位小数");
        $(document).ready(function () {
            $.ajax({
                type: "GET",
                url: "getScores",
                data: "confId=" + $("#affirmConfId").val(),
                dataType: "json",
                success: function (data) {
                    if (data) {
                        $.each(data, function (i, v) {
                            $("input[name='" + v.category + "" + v.result + "']").val(v.score);
                        });
                    }
                }
            });
            $("#inputForm").validate({
                submitHandler: function (form) {
                    var dataJson = [];
                    $("#dataTb").find("input").each(function (i, v) {
                        var data = {};
                        data.category = $(this).attr("category");
                        data.result = $(this).attr("result");
                        data.score = $(this).val();
                        dataJson.push(data);
                    });
                    $("#dataJson").val(JSON.stringify(dataJson));
                    resetTip()
                    form.submit();
                },
                errorPlacement: function (error, element) {
                    error.appendTo(element.parent());
                }
            });

        });
    </script>
</head>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>${titleName}认定标准</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="inputForm"
               modelAttribute="scoAffirmCriterion" action="save" method="post" autocomplete="off">
        <input type="hidden" id="affirmConfId" name="affirmConfId" value="${confId}"/>
        <input type="hidden" id="dataJson" name="dataJson"/>
        <input type="hidden" id="secondName" name="secondName"value="${secondName}"/>
        <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap">
            <thead>
            <tr>
                <th>${categoryName }</th>
                <th colspan="${fn:length(resultList)}">结果及认定学分标准</th>
            </tr>
            </thead>
            <tbody id="dataTb">
            <c:forEach items="${categoryList}" var="category">
                <tr>
                    <td>${category.label}</td>
                    <c:forEach items="${resultList}" var="result">
                        <td style="white-space: nowrap">
                                ${result.label}<input name="${category.value }${result.value}"
                                                      class="required ckScore input-mini"
                                                      style="margin: 0 8px; text-align: center"
                                                      category="${category.value }" result="${result.value }"
                                                      type="text">学分
                        </td>
                    </c:forEach>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="text-center">
            <button type="submit" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-default" onclick="javascript:location.href='${ctx}/sco/scoAffirmConf'">
                返回
            </button>
        </div>
    </form:form>
</div>
</body>
</html>