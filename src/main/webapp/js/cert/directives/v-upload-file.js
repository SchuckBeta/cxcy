/**
 * Created by Administrator on 2017/11/13.
 */

Vue.directive('upload-file', {
    inserted: function (el, binding, vnode) {
        var data = binding.value.data;
        var name = binding.value.name;
        var cropOption = binding.value.cropOption;
        var html = '';
        var url = data.url;
        var fileVal = data.fileVal;
        var resource = data.resource;
        var braceReg = /\{\{(.)+?\}\}/g;
        var mValueResults = url.match(braceReg);
        var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;
        var rUrl;
        var copyUrl;
        var uploadFile;
        var $modal;
        var modal = '<div id="' + name + '" class="modal fade hide"> ' +
            '<div class="modal-dialog modal-lg"> <div class="modal-content"> <div class="modal-header"> ' +
            '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> <p style="margin-bottom: 0">更改图片</p> </div> ' +
            '<div class="modal-body" style="max-height: 450px;"> <div class="avatar-content"> <div class="avatar-area"> <div style="margin-bottom: 10px;"> ' +
            '<input type="file"  name="' + name + '" style="display: none"> ' +
            '<button type="button" class="btn-upload-image btn btn-primary-oe">上传图片</button> </div> <div class="cut-avatar-area"><img class="upload-pic" src="' + resource + '"> </div> </div> </div> </div>' +
            ' <div class="modal-footer"> <button class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>' +
            ' <button class="btn btn-primary-oe btn-modal-upload">上传</button> </div> </div> </div> </div>';

        html += '<a href="javascript:void (0);" class="btn-upload-pic">更换图片</a><input type="hidden" name="' + name + '" style="display: none;">';


        el.innerHTML = html;
        $('body').append(modal);
        $modal = $('#' + name);

        var $btnUploadPic = $(el).find('.btn-upload-pic');


        var $btnUploadFile = $modal.find('.btn-upload-image');
        var $img = $modal.find('.upload-pic');
        var $file = $modal.find('input[type="file"]');
        var $btnModalUpload = $modal.find('.btn-modal-upload');
        var $cropper;


        $file.on('change', function (e) {
            var aFiles = e.target.files;
            var oFReader = new FileReader();
            if (!aFiles) {
                return false;
            }
            if (!rFilter.test(aFiles[0].type)) {
                alert('上传图片格式不正确');
                return false;
            }

            if (window.FileReader) {
                oFReader.onload = function (oFREvent) {
                    var oPreviewImg = new Image();
                    oPreviewImg.src = oFREvent.target.result;
                    if ($img.attr('src')) {
                        $img.cropper('replace', oFREvent.target.result);
                    } else {
                        $img.attr('src', oFREvent.target.result);
                        if(!$cropper){
                            $img.cropper(cropOption);
                        }
                    }
                };
                oFReader.readAsDataURL(aFiles[0]);
            }
            if (navigator.appName === "Microsoft Internet Explorer") {
                $img.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = e.target.value;
            }

            uploadFile = aFiles[0];
        });

        $modal.on('shown', function () {
            if (resource && !$cropper) {
                $cropper = $img.cropper(cropOption);
            }
        })

        $btnUploadPic.on('click', function () {
            if (!$btnUploadPic.hasClass('disabled')) {
                $modal.modal('show');
            }
        });

        $btnUploadFile.on('click', function (e) {
            $file.trigger('click')
        });

        $btnModalUpload.on('click', function () {
            var imgData, oReq;
            var oMyForm;
            imgData = $img.cropper('getData');
            oReq = new XMLHttpRequest();
            oMyForm = new FormData();
            copyUrl = url;
            for (var i = 0; i < mValueResults.length; i++) {
                var key = mValueResults[i].replace('{{', '');
                key = key.replace('}}', '');
                rUrl = copyUrl.replace(mValueResults[i], Math.floor(imgData[key]));
                copyUrl = rUrl;
            }
            $btnModalUpload.prop('disabled', true).text('保存中...');
            oMyForm.append(fileVal, uploadFile);
            $img.cropper('disable');
            oReq.open('post', rUrl);
            oReq.onload = function (oEvent) {
                if (oReq.status == 200) {
                    vnode.context[name].resource = JSON.parse(oReq.responseText).url;
                    $modal.modal('hide');
                } else {
                    alertx('上传失败')
                }
                $img.cropper('enable');
                $btnModalUpload.prop('disabled', false).text('保存');
            };
            oReq.send(oMyForm);
        })
    }
});