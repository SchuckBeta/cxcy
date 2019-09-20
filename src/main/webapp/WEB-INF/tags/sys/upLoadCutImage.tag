<%@ tag language="java" pageEncoding="UTF-8" %>
<%--<link rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css">--%>
<style type="text/css">
    .modal-avatar {
        /*width: 700px;*/
        /*margin-left: -350px;*/
    }

    .modal-avatar .avatar-content {
        /*overflow: hidden;*/
    }

    .modal-avatar .avatar-area {
        float: left;
    }

    .modal-avatar .preview-avatar-lg {
        width: 150px;
    }

    .modal-avatar .preview-avatar-md {
        width: 100px;
    }

    .modal-avatar .preview-avatar-sm {
        width: 50px;
    }

    .modal-avatar .preview-avatars {
        float: right;
    }

    .modal-avatar .preview-avatar {
        margin-top: 8px;
        overflow: hidden;
    }

    .modal-avatar .cut-avatar-area {
        max-width: 400px;
        max-height: 400px;
    }

    .modal-avatar .modal-footer .btn {
        width: auto;
        height: auto;
        background-image: none;
    }

    .modal-avatar .modal-footer .btn-default:hover, .modal-avatar .modal-footer .btn-default:focus {
        border: 1px solid #ccc;
    }

    .modal-avatar .btn-primary-oe {
        color: #fff;
        width: auto;
        height: auto;
        background-color: #e9442d;
        background-image: none;
    }

    .modal-avatar .btn-primary-oe:hover, .modal-avatar .btn-primary-oe:focus {
        color: #fff;
        border: 1px solid #e9442d;
        background-color: #e9442d;
    }
</style>
<%--<script src="${ctxStatic}/jquery/jquery.min.js"></script>
<script src="${ctxCommon}/common-js/bootstrap.min.js"></script>--%>


<%@ attribute name="width" type="java.lang.String" required="true" description="图片最小宽度" %>
<%@ attribute name="height" type="java.lang.String" required="true" description="图片最小高度" %>
<%@ attribute name="btnid" type="java.lang.String" required="true" description="上传按钮Id" %>
<%@ attribute name="column" type="java.lang.String" required="true" description="图片地址的字段值" %>
<%@ attribute name="imgId" type="java.lang.String" required="true" description="图片ID值" %>
<%@ attribute name="filepath" type="java.lang.String" required="true" description="文件目录" %>
<%@ attribute name="className" type="java.lang.String" required="true" description="modal样式" %>
<%@ attribute name="modalId" type="java.lang.String" required="false" description="modal样式" %>
<%@ attribute name="aspectRatio" type="java.lang.String" required="false" description="modal样式" %>

<!--弹层部分-->
<div id="${modalId}" class="modal fade ${className}">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <p style="margin-bottom: 0">更改图片</p>
            </div>
            <div class="modal-body">
                <div class="avatar-content clearfix">
                    <div class="avatar-area">
                        <div style="margin-bottom: 10px;">
                            <input class="avatar-file" type="file" style="display: none">
                            <button type="button" class="btn-upload-image btn btn-primary-oe">上传图片</button>
                        </div>
                        <div class="cut-avatar-area"></div>
                    </div>
                    <div class="preview-avatars" style="padding-top: 36px;display: none">
                        <div class="preview-avatar preview-avatar-lg"></div>
                        <div class="preview-avatar preview-avatar-md"></div>
                        <div class="preview-avatar preview-avatar-sm"></div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
                <button class="btn btn-primary-oe btn-modal-upload">上传</button>
            </div>
        </div>
    </div>
</div>


<script>
    $(function () {
        var imgId = "#${imgId}";
        var width = "${width}";
        var height = "${height}";
        var btnid = "#${btnid}";
        var column = "${column}";
        var modalId = "#${modalId}";
        var uploadUrl = $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder='
                + '${filepath}'
                + '&x={{x}}&y={{y}}&width={{width}}&height={{height}}';
        var cropperPreviewFlag = false;
        var aspectRatio = "${aspectRatio}";
        var $previews = $('.preview-avatar');

        $(btnid).uploadAvatar({
            modal: modalId,
            url: uploadUrl,
            avatarPic: imgId,
            cropperOption: {
                aspectRatio:aspectRatio,
                minCropBoxWidth: width,
                minCropBoxHeight: height,
                crop: function (e) {
                    if (!cropperPreviewFlag) {
                        cropperPreviewFlag = true;
                        var $clone = $('img.cropper-hidden').clone().removeClass('cropper-hidden');
                        $clone.css({
                            display: 'block',
                            width: '100%',
                            minWidth: 0,
                            minHeight: 0,
                            maxWidth: 'none',
                            maxHeight: 'none'
                        });
                        $previews.html($clone);
                    }
                    var imageData = $(this).cropper('getImageData');
                    var previewAspectRatio = e.width / e.height;
                    $previews.each(function () {
                        var $preview = $(this);
                        var previewWidth = $preview.width();
                        var previewHeight = previewWidth / previewAspectRatio;
                        var imageScaledRatio = e.width / previewWidth;
                        $preview.height(previewHeight).find('img').css({
                            width: imageData.naturalWidth / imageScaledRatio,
                            height: imageData.naturalHeight / imageScaledRatio,
                            marginLeft: -e.x / imageScaledRatio,
                            marginTop: -e.y / imageScaledRatio
                        });
                    });
                }
            },
            success: function (data) {
                var $inputPhoto = $('input[name="' + column + '"]');
                $(imgId).attr('src', data.url);
                if ($inputPhoto.size() > 0) {
                    $inputPhoto.val(data.ftpUrl)
                } else {
                    $('<input name="' + column + '" type="hidden" value="' + data.ftpUrl + '">').insertAfter($(imgId));
                }
            }
        })
    })

</script>