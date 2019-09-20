<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>图片裁剪测试</title>
    <link rel="stylesheet" href="${ctxStatic}/bootstrap/2.3.1/css_default/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cropper/cropper.min.css">
    <script src="${ctxStatic}/jquery/jquery.min.js"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js"></script>
    <script type="text/javascript" src="${ctxStatic}/cropper/cropper.min.js"></script>
    <style>
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

        .modal-avatar {
            width: 700px;
            margin-left: -350px;
        }

        .modal .avatar-content {
            overflow: hidden;
        }

        .modal .avatar-area {
            float: left;
            /*width: 400px;*/
        }
        .preview-avatar-lg{
            width:150px;
        }
        .preview-avatar-md{
            width: 100px;
        }
        .preview-avatar-sm{
            width:50px;
        }
        .preview-avatars{
            float: right;
        }
        .preview-avatar{
            margin-top:8px;
            overflow: hidden;
        }
        .cut-avatar-area{
            max-width: 500px;
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
<div id="modalAvatar" class="modal fade hide modal-avatar">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <p style="margin-bottom: 0">更改图片</p>
    </div>
    <div class="modal-body">
        <div class="avatar-content">
            <div class="avatar-area">
                <input id="avatarFile" type="file">
                <div id="cutAvatarArea" class="cut-avatar-area">
                </div>
            </div>
            <div class="preview-avatars">
                <div class="preview-avatar preview-avatar-lg"></div>
                <div class="preview-avatar preview-avatar-md"></div>
                <div class="preview-avatar preview-avatar-sm"></div>
            </div>
        </div>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal" aria-hidden="true">取消</button>
        <button id="btnModalUpload" class="btn btn-primary">上传</button>
    </div>
</div>
<script>


    $.fn.uploadAvatar = function (option) {
        var options = $.extend(true,{}, $.fn.uploadAvatar.default, option);
        var $this = $(this);
        var $modal = $(options.modal);
        var $modalAvatarArea = $(options.cutAvatarArea);
        var cropperOption = options.cropperOption;
        var $avatarFile = $(options.avatarFile);
        var braceReg = /\{\{(.)+?\}\}/g;
        var url = options.url;
        var mValueResults = url.match(braceReg);
        var $btnModalUpload = $('#btnModalUpload');
        var fileVal = options.fileVal;
        var oMyForm;
        var minCropBoxWidth = cropperOption.minCropBoxWidth;
        var minCropBoxHeight = cropperOption.minCropBoxHeight;


        var uploadFile;
        var oFReader;


        $this.on('click.show.modal', function (e) {
            e.stopPropagation();
            $modal.modal('show');
//            $modalAvatarArea.find('>img').cropper('destroy');
        });


        $avatarFile.on('change', function () {
            var aFiles = this.files;
            oFReader = null;
            oFReader = new FileReader();

            if(window.FileReader){
                oFReader.onload = function(oFREvent){
                    var oPreviewImg = new Image();
                    var $modalAvatarAreaImg = $modalAvatarArea.find('img');
                    oPreviewImg.src = oFREvent.target.result;
                    if($modalAvatarAreaImg.size() > 0){
                        $modalAvatarAreaImg.cropper('replace',oFREvent.target.result);
                    }else{
                        $modalAvatarArea.append($(oPreviewImg));
                        setModalAvatarPic.call(oPreviewImg);
                    }
                    $(options.previewAvatar).find('>img').show().attr('src',oFREvent.target.result);
                };
                oFReader.readAsDataURL(aFiles[0]);
            }
            if (navigator.appName === "Microsoft Internet Explorer") {
                $modalAvatarArea[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = $(this).value;
            }
            uploadFile = aFiles[0];
        });

        $btnModalUpload.on('click',function(){
            var $previewImg = $modalAvatarArea.find('>img');
            var imgData = $previewImg.cropper('getData');
            var rUrl;
            var oReq = new XMLHttpRequest();
            var $btn = $(this);
            if(!$avatarFile.val()){
                alert('请先上传图片');
                return false;
            }
            if(imgData.x < 0){
                imgData = 0
            }
            if(imgData.y < 0){
                imgData.y = 0
            }

            $(this).addClass('load').prop('disabled');
            oMyForm = new FormData();



            for(var i=0;i<mValueResults.length;i++){
                var key = mValueResults[i].replace('{{','');
                key = key.replace('}}','');
                rUrl = url.replace(mValueResults[i],Math.floor(imgData[key]));
                url = rUrl;
            }
            url = options.url;
            oMyForm.append(fileVal, uploadFile);
            oReq.open('post', rUrl);
            oReq.onload = function(oEvent) {
                if (oReq.status == 200) {
                    options.success.call(null,JSON.parse(oReq.responseText));
                    $modal.modal('hide');
                } else {
                    options.error.call(null,oReq.responseText)
                }
                $btn.prop('disabled',false);
            };
            oReq.send(oMyForm);
        });



         $modal.on('hide',function(){
             var $previewImg = $modalAvatarArea.find('>img');
             $previewImg.cropper('destroy');
             $previewImg.detach();
             oMyForm = null;
             $avatarFile.val('');
             $(options.previewAvatar).height('auto').find('img').hide();
         });


        function setModalAvatarPic() {
            $(this).cropper(cropperOption);
        }


    };



    $.fn.uploadAvatar.default = {
        modal: '#modalAvatar',
//        imgData: {
//            x: 0,
//            y: 0,
//            width: 200,
//            height: 200
//        },
        url: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=user&x={{x}}&y={{y}}&width={{width}}&height={{height}}',

        avatarPic: '#avatarPic',
        avatarFile: '#avatarFile',
        cropperOption: {
            aspectRatio: 1,
            minCropBoxWidth: 200,
            minCropBoxHeight: 200,
            zoomOnWheel: false,
            crop: function(e){
            }
        },
        //canvas
        cutAvatarArea: '#cutAvatarArea',
        btnModalUpload: '#btnModalUpload',
        fileVal: 'upfile',
        previewAvatar: '.preview-avatar',
        error: function(){},
        success: function(){}
    };

    $(function () {
        var cropperPreviewFlag = false;
        var $previews = $('.preview-avatar');
        $('#btnUploadAvatar').uploadAvatar({
            cropperOption: {

                crop: function(e){
                    if(!cropperPreviewFlag){
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
            success: function(data){
                var $inputPhoto = $('input[name="photo"]');
                $('#avatarPic').attr('src',data.url);
                if($inputPhoto.size() > 0){
                    $inputPhoto.val(data.ftpUrl)
                }else{
                    $('<input name="photo" type="hidden" value="'+data.ftpUrl+'">').insertAfter($("#avatarPic"));
                }
            }
        })
    })

</script>
</body>
</html>
