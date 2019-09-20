<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/creatives.css">
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/frontBime.css">
    <link href="${ctxStatic}/cropper/cropper.min.css" rel="stylesheet"/>
    <script src="${ctxStatic}/cropper/cropper.min.js" type="text/javascript"></script>

</head>
<body>

<style>
    .cropper-area {
        width: 300px;
        height: 300px;
        margin-top: 10px;
    }

    .cropperImg {
        width: 300px;
        height: 300px;
    }
</style>
<div class="container preserve">
    <ol class="breadcrumb" style="background: none">
        <li><a href="#"><i class="icon-home"></i>我的空间</a></li>
        <li><a href="">基于互联网的教学反馈系统设计与实现</a></li>
    </ol>
    <div class="preserve-detail">
        <h2>腕带识别系统</h2>
        <p><span>项目来源：国创计划</span><span>发布时间：2016-12-10</span></p>
        <div class="row preserve-appear">
            <div class="col-md-3">
                <input class="hide-img" type="hidden" value="">
                <img class="set-pic" src="${ctxImages}/upload-default-image1.png" alt="">
            </div>
            <div class="col-md-6">
                <p>荣获奖项：优秀项目</p>
                <p>项目负责人：李思缔</p>
                <p>学院：计算机学院</p>
                <p>指导教师：唐立军 &nbsp;&nbsp;教授</p>
                <p class="key-word"><span>关键字：</span>
                    <input type="text" class="form-control" value="手势识别、可穿戴设备">
                </p>
            </div>
        </div>
        <p><span>浏览量：140</span><span>点赞数：20</span><a href="#" class="btn btn-primary btn-small changeImg"
                                                     data-toggle="modal" data-target="#preserveModel">更新封面</a></p>
        <p class="layout-template">
            <select class="form-control">
                <option value="1">模板一</option>
                <option value="2">模板二</option>
            </select>
            <span>选择布局模板：</span>
        </p>
    </div>

    <div class="edit-docu">
        <span>正文内容：</span>
        <div class="docu-word"></div>
    </div>

    <div class="video-show">
        <span>视频展示：</span>
        <div class="video-load">
            <div class="video-three">
                <div class="video-play">
                    <span class="video-top">已置顶</span>
                    <span class="video-pass">通过</span>
                    <img src="${ctxImages}/circle-hollow.png" alt="">
                    <img src="${ctxImages}/video-play.png" alt="">
                </div>
                <div class="video-detail">
                    <p>去有机质前后土壤高光谱分析</p>
                    <p>视频类型：项目展示</p>
                    <a href="#" class="btn blue-small">大数据</a>
                    <a href="#" class="btn blue-small">人工智能</a>
                    <p class="video-time">创建时间：2017-11-17</p>
                    <p><span>浏览量：50</span><span>点赞量：43</span></p>
                </div>
                <div class="video-upload">
                    <img src="${ctxImages}/circle-hollow.png" alt="">
                    <img src="${ctxImages}/video-upload.png" alt="">
                </div>
            </div>
            <div class="video-delete">
                <select class="form-control top-select">
                    <option value="1">置顶</option>
                    <option value="2">取消置顶</option>
                </select>
                <a href="#" class="btn btn-small btn-primary">删除</a>
            </div>
        </div>
    </div>

</div>


<div id="preserveModel" class="modal fade">
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
                    <img class="cropperImg" src="${ctxImages}/upload-default-image1.png">
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

        var $preserveModel = $('#preserveModel');
        var $setPic = $('.set-pic');
        var $upImg = $('.upImg');
        var $upImgFile = $('.upImgFile');
        var $cropperImg = $('.cropperImg');
        var $upLoadImg = $('.upLoadImg');
        var $hideImg = $('.hide-img');
        var changeImg = $('.changeImg')

        var flag = false;

        changeImg.on('click', function () {
            if (flag) {
                $cropperImg.cropper('replace', $setPic.attr('src'));
            } else {
                $cropperImg.attr('src', $setPic.attr('src'));
            }
        });
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
                        aspectRatio: 271/185,
                        viewMode: 1,
                        crop: function (e) {
                        }
                    });
                } else {
                    $cropperImg.cropper("reset", true).cropper('replace', this.result);
                }
            };
            fr.readAsDataURL(obj);
        });


        $upLoadImg.on('click', function () {

            var formData = new FormData();
            formData.append('upfile', $upImgFile[0].files[0]);
            var inputName = "logoUrl";
            /*获取上传的图片对象*/
            var filepath = 'user'
            var imgData = $cropperImg.cropper('getData');
            var x = parseInt(imgData.x);
            var y = parseInt(imgData.y);
            var width = parseInt(imgData.width);
            var height = parseInt(imgData.height);
            $hideImg.attr('name', inputName);

            $.ajax({
                url: $frontOrAdmin + '/ftp/ueditorUpload/cutImgToTempDir?folder=' + filepath + '&x=' + x + '&y=' + y + '&width=' + width + '&height=' + height,
                type: 'POST',
                data: formData,
                contentType: false,
                processData: false,
                success: function (args) {
                    console.log(args);
                    /*服务器端的图片地址*/
                    $setPic.attr('src', args.url);
                    $hideImg.val(args.ftpUrl);
                    $preserveModel.modal('hide');
                }
            })

        });


    });

</script>


</body>
</html>