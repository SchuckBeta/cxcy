<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>配置图标</title>
    <%@include file="/WEB-INF/views/include/backtable.jsp" %>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cropper/cropper.min.css">
    <script type="text/javascript" src="${ctxStatic}/cropper/cropper.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/uploadCutImageIcon.js"></script>
    <style>
        .form-search .ul-form li {
            margin-bottom: 15px;
        }

        .form-search .ul-form li.btns {
            float: right;
        }
        .form-search input,select{
            height: 30px;
            max-width: 150px;
        }
        #searchForm{
            height: auto;
        }
        .pic-box{
            margin-left: 23px;
            overflow: hidden;
        }
        .upload-icon{
            float: left;
        }
        .preview-icon{
            float: left;
            width: 30px;
        }
        .form-search .ul-form li{
            margin-bottom: 0;
        }

    </style>
    <script type="text/javascript">
        $(document).ready(function () {
            $("#ps").val($("#pageSize").val());
        });
        function page(n, s) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            $("#searchForm").submit();
            return false;
        }
    </script>
</head>
<body>
<div class="mybreadcrumbs">
    <span>图标配置</span>
</div>
<div class="content_panel">
    <ul class="nav nav-tabs">
        <li class="active"><a href="${ctx}/attachment/sysAttachment/icons/gnode">流程图标</a></li>
    </ul>
    <form:form id="searchForm" modelAttribute="sysAttachment" action="${ctx}/attachment/sysAttachment/icons/gnode"
               method="post" class="breadcrumb form-search">
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <ul class="ul-form">
            <li class="btns pull-right"><input id="btnSubmit" class="btn btn-primary" type="submit" value="查询"/></li>
            <li class="btns" style="padding-top: 3px;">
                <div class="pic-box">
                    <div class="preview-icon">
                        <img id="iconPic" src="" style="display: block;max-width: 100%" />
                    </div>
                    <button id="filePicker" type="button" class="upload-icon btn btn-primary" disabled  style="margin-left: 10px;">上传图标</button>
                </div>
            </li>
            <li><label>图标类型：</label>
                <select name="fstep" class="input-xlarge">
                    <option value="">--请选择--</option>
                    <c:forEach var="fsp" items="${fileSteps }">
                        <option value="${fsp.value}">${fsp.name}</option>
                    </c:forEach>
                </select>
                <span style="color: #666;line-height: 30px;">选择图标类型，才可以上传图标哦</span>
            </li>
            <li class="clearfix"></li>
        </ul>

    </form:form>
    <sys:message content="${message}"/>
    <table id="contentTable" class="table table-bordered table-condensed">
        <thead>
        <tr>
            <th>图片</th>
            <th>类型</th>
            <th>类别</th>
            <th>名称</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${page.list}" var="sysAttachment">
            <tr>
                <td><img alt="${sysAttachment.name }" src="${fns:ftpImgUrl(sysAttachment.url)}" width="50px;"></td>
                <td>${sysAttachment.type }</td>
                <td>${sysAttachment.fileStep }</td>
                <td>${sysAttachment.name }</td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
    ${page.footer}
</div>
<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
<%--<sys:ajaxUpLoadCutImage width="100" height="100" btnid="filePicker" imgId="iconPic" column="upfile" filepath="ueditor"--%>
                    <%--className="modal-avatar hide" modalId="modalAvatar1"></sys:ajaxUpLoadCutImage>--%>


<script>
    $(function () {
        var $filePicker = $('#filePicker');

        $('select[name="fstep"]').on('change', function () {
            if($(this).val()){
                $filePicker.prop('disabled', false)
            }else {
                $filePicker.prop('disabled', true)
            }
        }).change();

        $('#modalAvatar1').on('uploadSuccess', function () {
            location.reload();
        })
    })
</script>

</body>
</html>
