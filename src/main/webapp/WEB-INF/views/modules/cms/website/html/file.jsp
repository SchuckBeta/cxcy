<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="cyjd-site-default"/>
    <title>${frontTitle}</title>
    <link href="${ctxStatic}/cropper/cropper.min.css" rel="stylesheet"/>
    <script src="${ctxStatic}/cropper/cropper.min.js" type="text/javascript"></script>


</head>
<body>

<style>

    .changeImg {
        width: 128px;
        height: 128px;
    }

    .cropper-area {
        width: 300px;
        height: 300px;
        margin-top: 10px;
    }

    .cropperImg{
        width: 300px;
        height: 300px;
    }


</style>

<div style="width:1000px;height:600px;">

    <img class="changeImg" src="${ctxImg}/u4110.png" data-toggle="modal" data-target="#modalAvatar">


</div>


<div id="modalAvatar" class="modal fade">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                <p style="margin-bottom: 0">更改图片</p>
            </div>
            <div class="modal-body">
                <input class="upImgFile" type="file" style="display: none" accept="image/jpeg,image/png">
                <button type="button" class="btn btn-primary upImg">上传图片</button>
                <div class="cropper-area">
                    <img class="cropperImg" src="${ctxImg}/u4110.png">
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">取消</button>
                <button type="button" class="btn btn-primary upLoadImg">上传</button>
            </div>
        </div>
    </div>
</div>


<script>


    $(function () {

        var $modalAvatar = $('#modalAvatar');
        var $changeImg = $('.changeImg');
        var $upImg = $('.upImg');
        var $upImgFile = $('.upImgFile');
        var $cropperImg = $('.cropperImg');
        var $upLoadImg = $('.upLoadImg');

        var flag = false;

        $upImg.on('click', function () {
            $upImgFile.trigger('click');
        });

        $upImgFile.change(function () {
            var obj = $upImgFile[0].files[0];
            var fr = new FileReader();
            fr.onload = function () {

                if (!flag) {
                    flag = true;
                    $cropperImg.attr('src', this.result);
                    $cropperImg.cropper({
                        aspectRatio: 1,
                        crop: function (e) {
                        }
                    });
                } else {
                    $cropperImg.cropper("reset", true).cropper('replace', this.result);
                }


            };
            fr.readAsDataURL(obj);
        });

//        $modalAvatar.on('hide.bs.modal',function(){
//            $cropperImg.cropper('destroy');
//        });

        $upLoadImg.on('click', function () {

            var formData = new FormData();
            formData.append('upfile', $upImgFile[0].files[0]);

            /*获取上传的图片对象*/

            var imgData = $cropperImg.cropper('getData');
            var x = parseInt(imgData.x);
            var y = parseInt(imgData.y);
            var width = parseInt(imgData.width);
            var height = parseInt(imgData.height);

            $.ajax({
                url: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=user' + '&x=' + x + '&y=' + y + '&width=' + width + '&height=' + height,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (args) {
                    console.log(args);
                    /*服务器端的图片地址*/
                    $changeImg.attr('src', args.url);
                    $modalAvatar.modal('hide');
//                    $cropperImg.cropper('destroy');
                }
            })

        });


    });


    //    function bindAvatar() {
    //        bindAvatar2();
    ////                bindAvatar1();
    //    }
    //    /*Ajax上传至后台并返回图片的url*/
    //    function bindAvatar1() {
    ////        $("#avatarSlect").change(function () {
    //////    				var csrf = $("input[name='csrfmiddlewaretoken']").val();
    ////            var formData = new FormData();
    //////    				formData.append("csrfmiddlewaretoken",csrf);
    ////            console.log($("#avatarSlect")[0].files[0])
    ////            formData.append('upfile', $("#avatarSlect")[0].files[0]);
    ////            /*获取上传的图片对象*/
    ////            $.ajax({
    ////                url: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=user&x=96&y=96&width=768&height=768',
    ////                type: 'POST',
    ////                data: formData,
    ////                contentType: false,
    ////                processData: false,
    ////                success: function (args) {
    ////                    console.log(args);
    ////                    /*服务器端的图片地址*/
    ////                    $("#avatarPreview").attr('src', args.url);
    ////                    /*预览图片*/
    ////                    $("#avatar").val(args.url);
    ////                    /*将服务端的图片url赋值给form表单的隐藏input标签*/
    ////                }
    ////            })
    ////        })
    //
    //        $('#image').cropper('getCroppedCanvas').toBlob(function (blob) {
    //            var formData = new FormData();
    //            formData.append('upfile', $("#avatarSlect")[0].files[0]);
    //            formData.append('croppedImage', blob);
    //
    //            $.ajax($frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=user' + '&x={{x}}&y={{y}}&width={{width}}&height={{height}}', {
    //                method: "POST",
    //                data: formData,
    //                processData: false,
    //                contentType: false,
    //                success: function (args) {
    //                    console.log('Upload success');
    //                    $("#avatarPreview").attr('src', args.url);
    //                },
    //                error: function () {
    //                    console.log('Upload error');
    //                }
    //            });
    //        });
    //    }
    //
    //
    //    /*window.FileReader本地预览*/
    //    function bindAvatar2() {
    //        console.log(2);
    //        $("#avatarSlect").change(function () {
    //            var obj = $("#avatarSlect")[0].files[0];
    //            var fr = new FileReader();
    //            fr.onload = function () {
    //                $("#image").attr('src', this.result);
    //                $('#image').cropper({
    //                    aspectRatio: 1,
    //                    crop: function (e) {
    //                        // Output the result data for cropping image.
    ////                        var cropX = e.x;
    ////                        var cropY = e.y;
    ////                        var cropWidth = e.width;
    ////                        var cropHeight = e.height;
    //                        console.log(e.x);
    //                        console.log(e.y);
    //                        console.log(e.width);
    //                        console.log(e.height);
    //                        console.log(e.rotate);
    //                        console.log(e.scaleX);
    //                        console.log(e.scaleY);
    //                    }
    //                });
    ////                console.log(this.result);
    ////                console.log($('#image').cropper('getCropBoxData').x);
    //                $("#avatar").val(this.result);
    //            };
    //            fr.readAsDataURL(obj);
    //        })
    //
    //
    //    }
    //    /*window.URL.createObjectURL本地预览*/
    //    function bindAvatar3() {
    //        console.log(3);
    //        $("#avatarSlect").change(function () {
    //            var obj = $("#avatarSlect")[0].files[0];
    //            var wuc = window.URL.createObjectURL(obj);
    //            $("#avatarPreview").attr('src', wuc);
    //            $("#avatar").val(wuc);
    //
    //        })
    //    }
</script>


</body>
</html>
