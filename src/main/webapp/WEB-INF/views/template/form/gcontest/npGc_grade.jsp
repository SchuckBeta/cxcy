<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>

</head>

<body>


<div id="dialogCyjd" class="dialog-cyjd"></div>
<script>
    $(function () {
        var $inputForm = $('#inputForm');
        var formValidate = $inputForm.validate({
            submitHandler: function (form) {
                $inputForm.find('button[type="submit"]').prop('disabled', true);
                form.submit();
                window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
            }
        });
    })

</script>


</body>

</html>
