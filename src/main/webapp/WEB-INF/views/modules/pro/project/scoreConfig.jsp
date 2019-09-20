<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <script type="text/javascript" src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js"></script>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet"/>
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <title>${frontTitle}</title>
    <style type="text/css">
        .container {
            padding-top: 100px;
        }

        .table-theme-default caption {
            padding: 8px;
            line-height: 20px;
            font-size: 16px;
            font-weight: 700;
            color: #333;
            background-color: #f4e6d4;
        }

        .table-theme-default thead th {
            font-size: 14px;
            font-weight: normal;
        }

        .table-theme-default {
            table-layout: fixed;
        }

        .table-theme-default .form-control {
            max-width: 120px;
            float: left;
        }
    </style>

    <script>

        $(function () {
            var ratio = "${ratio}";
            var ratioArr = ratio.split(':');
            ratioArr = ratioArr.sort(function (a, b) {
                return b - a;
            });
            ratio=ratioArr.join(':');
            var maxValue = Math.max.apply(null, ratioArr);
            var minValue = Math.min.apply(null, ratioArr);


            var validate = $('#creditForm').validate({
                debug: true,
                submitHandler: function (form) {
                    var inputValues = [];
                    $('.form-control').each(function (i, input) {
                        inputValues.push(parseInt($(input).val()));
                    });
                    inputValues = inputValues.sort(function (a, b) {
                        return b - a;
                    });
                    if (ratio == inputValues.join(':')) {
                        var formData = $("#creditForm").serialize();
                        console.log(formData);
                        $.ajax({
                            type:'post',
                            url:'/f/project/projectDeclare/saveScoreConfig',
                            data: formData,
                            success:function(data){
                                if(data){
                                    dialogCyjd.createDialog(1, "保存学分配比成功", {
                                        dialogClass: 'dialog-cyjd-container none-close',
                                        buttons: [{
                                            text: "确定",
                                            'class': 'btn btn-sm btn-primary',
                                            click: function () {
                                            	$(this).dialog('close')
                                            	top.location = "/f/project/projectDeclare/list";
                                            }
                                        }]
                                    });
                                }
                            }
                        });
                    } else {
                        dialogCyjd.createDialog(0, "学分配置错误", {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: "确定",
                                'class': 'btn btn-sm btn-primary',
                                click: function () {
                                	$(this).dialog('close')
                                }
                            }]
                        });
                    }
                }
            });

            jQuery.validator.addMethod("integer", function (value, element) {
                return this.optional(element) || (/^[0-9]*[1-9][0-9]*$/.test(value));
            }, $.validator.format("请输入整数"));

            jQuery.validator.addMethod("rangValue", function (value, element) {
                return this.optional(element) || (value >= minValue && value <= maxValue);
            }, $.validator.format('请输入' + minValue + '-' + maxValue + '间数字'));

            $('#content').css('minHeight',$(window).height() - $('.header').height() - $('.footerBox').height())
        })

    </script>

</head>
<body>
<div class="container">
    <form id="creditForm" action="${ctxFront}/project/projectDeclare/saveScoreConfig">
        <table id="creditSetting" class="table table-hover table-bordered table-theme-default">
            <caption>配比规则：${ratio}</caption>
            <thead>
            <tr>
                <th style="width: 150px;">项目组成员</th>
                <th>配比</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${studentList}" var="teamUserHistory" varStatus="status">
                <tr>
                    <td>${teamUserHistory.name}</td>
                    <td><input name="teamUserHistoryList[${status.index}].weightVal"
                               class="form-control input-sm integer rangValue"
                               value="${fns:deleteZero(teamUserHistory.weightVal)}" required/>
                        <input type="hidden" name="teamUserHistoryList[${status.index}].id"
                               value="${teamUserHistory.id}"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
        <div class="text-center">
            <c:if test="${isSubmit}">
                <button type="submit" class="btn btn-primary-oe" >保存</button>
            </c:if>
            <button type="button" class="btn btn-default" onclick="history.back(-1);">返回</button>
        </div>
    </form>
</div>
</body>
</html>
