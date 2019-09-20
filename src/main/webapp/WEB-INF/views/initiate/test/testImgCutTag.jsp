<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

<head>
    <title>图片裁剪测试</title>
    <script src="${ctxStatic}/jquery/jquery.min.js"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js"></script>
    <link rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css">
    <style type="text/css">
        .avatar-profile {
            width: 200px;
            margin: 0 auto;
            padding: 5px 10px;
            border: 1px solid #ddd;
        }

        .avatar-box {
            height: 200px;
            margin-bottom: 10px;
            overflow: hidden;
        }

        .avatar-pic {
            display: block;
            width: auto;
            max-width: 100%;
        }

        .mgb-10 {
            margin-bottom: 10px;
        }

        .avatar-profile .user-intro-item {
            margin-bottom: 6px;
            overflow: hidden;
        }

        .avatar-profile .user-intro-item > i, .avatar-profile .user-intro-item > span {
            display: block;
        }

        .avatar-profile .user-intro-item > i {
            float: left;
            width: 88px;
            font-style: normal;
            font-size: 12px;
        }

        .avatar-profile .user-intro-item > span {
            margin-left: 88px;
            font-size: 12px;
        }

    </style>
</head>
<body>

<div class="avatar-profile">
    <div class="avatar-box">
        <img id="avatarPic" class="avatar-pic"  src="">
    </div>
    <div class="mgb-10 text-center">
        <button type="button" id="btnUploadAvatar" class="btn btn-primary btn-small">更改图像</button>
    </div>
</div>
<sys:testCut width="200" height="200" btnid="btnUploadAvatar" imgId="avatarPic" column="photo"  filepath="user"  className="modal-avatar"></sys:testCut>


</body>
</html>
