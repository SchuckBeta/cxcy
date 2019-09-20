<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <link href="${ctxStatic}/cropper/cropper.min.css" rel="stylesheet"/>
    <script src="${ctxStatic}/cropper/cropper.min.js" type="text/javascript"></script>


</head>
<body>

<style>

    .cutArea {
        /*margin-top:100px;*/
        width: 300px;
        height: 300px;
        /*background: pink;*/
    }


</style>

<div style="width:1000px;height:600px;">

    <div style="position: absolute; top: 185px; left: 300px;">
        <input id="avatarSlect" type="file"
               style="position: absolute;float: left; z-index: 10;">
        <div class="cutArea" style="position: absolute;top:50px; z-index: 5;">
            <img id="image" src="">
        </div>
        <button id="upLoadImg" style="position:absolute;top:100px;left:500px;width:100px;">上传</button>
        <img id="avatarPreview" src="${ctxStatic}/images/sample.png" title="点击更换图片"
             style="position: absolute;top:150px;left:500px; z-index: 9; float: left;width:100px;height:100px;">
    </div>



</div>


<script>


    $(function () {

        $("#avatarSlect").change(function () {
            var obj = $("#avatarSlect")[0].files[0];
            var fr = new FileReader();
            fr.onload = function () {
                $("#image").attr('src', this.result);
                $('#image').cropper({
                    aspectRatio: 1,
                    crop: function (e) {
                    }
                });
                $("#avatar").val(this.result);
            };
            fr.readAsDataURL(obj);
        });

        $('#upLoadImg').on('click', function () {
            console.log('d');
            var formData = new FormData();
            formData.append('upfile', $("#avatarSlect")[0].files[0]);

            /*获取上传的图片对象*/

            var imgData = $('#image').cropper('getData');
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
                    $("#avatarPreview").attr('src', args.url);
                    /*预览图片*/
                    $("#avatar").val(args.url);
                    /*将服务端的图片url赋值给form表单的隐藏input标签*/
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
