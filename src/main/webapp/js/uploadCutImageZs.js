/**
 * Created by Administrator on 2017/9/11.
 */
$.fn.uploadAvatar = function (option) {
    var options = $.extend(true, {}, $.fn.uploadAvatar.default, option);
    var $this = $(this);
    var $modal = $(options.modal);
    var $modalAvatarArea = $modal.find('.cut-avatar-area');
    var cropperOption = options.cropperOption;
    var $avatarFile = $modal.find('.avatar-file');
    var braceReg = /\{\{(.)+?\}\}/g;
    var url = options.url;
    var mValueResults = url.match(braceReg);
    var $btnModalUpload = $modal.find('.btn-modal-upload');
    var fileVal = options.fileVal;
    var oMyForm;
    var minCropBoxWidth = cropperOption.minCropBoxWidth;
    var minCropBoxHeight = cropperOption.minCropBoxHeight;
    var $btnUploadImage = $modal.find('.btn-upload-image');

    var uploadFile;
    var oFReader;


    $btnUploadImage.on('click', function () {
        $avatarFile.trigger('click')
    });

    $this.on('click.show.modal', function (e) {
        e.stopPropagation();
        var beforeEvent = $.Event('beforeModalShow',{
            relatedTarget: e.target
        });
        $this.trigger('beforeModalShow');
        if(beforeEvent.isDefaultPrevented()){
            return false
        }

        $modal.modal('show');
//            $modalAvatarArea.find('>img').cropper('destroy');
    });


    $avatarFile.on('change', function () {
        var aFiles = this.files;
        var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;
        oFReader = new FileReader();
        if (!rFilter.test(aFiles[0].type)) {
            alert('上传图片格式不正确');
            return false;
        }
        if (window.FileReader) {
            oFReader.onload = function (oFREvent) {
                var oPreviewImg = new Image();
                var $modalAvatarAreaImg = $modalAvatarArea.find('img');
                oPreviewImg.src = oFREvent.target.result;
                if ($modalAvatarAreaImg.size() > 0) {
                    $modalAvatarAreaImg.cropper('replace', oFREvent.target.result);
                } else {
                    $modalAvatarArea.append($(oPreviewImg));
                    setModalAvatarPic.call(oPreviewImg);
                }
                $(options.previewAvatar).find('>img').show().attr('src', oFREvent.target.result);
            };
            oFReader.readAsDataURL(aFiles[0]);
        }
        if (navigator.appName === "Microsoft Internet Explorer") {
            $modalAvatarArea[0].filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = $(this).value;
        }
        uploadFile = aFiles[0];
    });

    $btnModalUpload.on('click', function () {
        var $previewImg = $modalAvatarArea.find('>img');
        var imgData = $previewImg.cropper('getData');
        var rUrl;
        var oReq = new XMLHttpRequest();
        var $btn = $(this);
        if (!$avatarFile.val()) {
            alert('请先上传图片');
            return false;
        }

        if (imgData.x < 0) {
            imgData.x = 0
        }
        if (imgData.y < 0) {
            imgData.y = 0
        }
        if (imgData.width < minCropBoxWidth) {
            alert('上传图片宽度低于' + minCropBoxWidth + 'px');
            return false
        }
        if (imgData.height < minCropBoxHeight) {
            alert('上传图片高度低于' + minCropBoxHeight + 'px');
            return false
        }

        $(this).addClass('load').prop('disabled', true);
        oMyForm = new FormData();
        for (var i = 0; i < mValueResults.length; i++) {
            var key = mValueResults[i].replace('{{', '');
            key = key.replace('}}', '');
            rUrl = url.replace(mValueResults[i], Math.floor(imgData[key]));
            url = rUrl;
        }
        // Crop
        var croppedCanvas = $previewImg.cropper('getCroppedCanvas');
        // Round
        var roundedCanvas = getRoundedCanvas(croppedCanvas);
        var successEvent = $.Event('uploadSuccess');
        url = options.url;
        oMyForm.append(fileVal, uploadFile);
        oReq.open('post', rUrl);
        oReq.onload = function (oEvent) {
            if (oReq.status == 200) {
                options.success.call(null, JSON.parse(oReq.responseText));
                $modal.trigger(successEvent, [JSON.parse(oReq.responseText), roundedCanvas.toDataURL(), imgData]);
                $modal.modal('hide');
            } else {
                options.error.call(null, oReq.responseText)
            }
            $btn.prop('disabled', false);
        };
        oReq.send(oMyForm);
    });


    
    $modal.on('hide.bs.modal', function () {
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
    function getRoundedCanvas(sourceCanvas) {
        var canvas = document.createElement('canvas');
        var context = canvas.getContext('2d');
        var width = sourceCanvas.width;
        var height = sourceCanvas.height;
        canvas.width = width;
        canvas.height = height;
        context.drawImage(sourceCanvas, 0, 0, width, height);
        return canvas;
    }

};


$.fn.uploadAvatar.default = {
    modal: '#modalAvatar',
    url: '',
    avatarPic: '#image',
    cropperOption: {
        // aspectRatio: 1,
        viewMode: 1,
        zoomOnWheel: false,
        crop: function (e) {
        }
    },
    //canvas
    fileVal: 'upfile',
    previewAvatar: '.preview-avatar',
    error: function () {
    },
    success: function () {
    }
};