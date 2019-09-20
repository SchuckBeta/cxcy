/**
 * Created by qingtengwang on 2017/6/29.
 */

(function () {
    $(function () {
        
        var BASE_URL = '/static/webuploader';
        var $list = $('#uploadList');
        var thumbnailWidth = 100; //默认宽高
        var thumbnailHeight = 100;//默认宽高
        var $coverImg = $('#iconUrl');
// 初始化Web Uploader
        var uploader = WebUploader.create({

            // 选完文件后，是否自动上传。
            auto: true,

            // swf文件路径
            swf: BASE_URL + '/Uploader.swf',

            // 文件接收服务端。
            server: $frontOrAdmin+'/ftp/ueditorUpload/uploadImg?floder=actyw&imgWidth=100&imgHeight=100',

            // 选择文件的按钮。可选。
            // 内部根据当前运行是创建，可能是input元素，也可能是flash.
            pick: '#filePicker',
            fileVal: 'upfile',

            // 只允许选择图片文件。
            accept: {
                title: 'Images',
                extensions: 'gif,jpg,jpeg,bmp,png',
                mimeTypes: 'image/*'
            }
        });

// 当有文件添加进来的时候
        uploader.on('fileQueued', function (file) {
            // var $li = $(
            //         '<li id="' + file.id + '" class="file-item thumbnail">' +
            //         '<img>' +
            //         '</li>'
            //     ),
            //     $img = $li.find('img');
            //
            //
            // // $list为容器jQuery实例 重新添加
            // $list.empty().append($li);
            // $coverImg.val('');//置空隐藏字段
            // 创建缩略图
            // 如果为非图片文件，可以不用调用此方法。
            // thumbnailWidth x thumbnailHeight 为 100 x 100
            // $img.attr('src', file.name);
            // console.log(file);

            // uploader.makeThumb(file, function (error, src) {
            //     if (error) {
            //         $img.replaceWith('<span>不能预览</span>');
            //         return;
            //     }
            //     // console.log(src)
            //     // $img.attr('src', src);
            // });
        });

// 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress span');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<p class="progress"><span></span></p>')
                    .appendTo($li)
                    .find('span');
            }

            $percent.css('width', percentage * 100 + '%');
        });

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on('uploadSuccess', function (file, response) {
            if(response.state == 'SUCCESS'){
                var $li = $(
                        '<li id="' + file.id + '" class="file-item thumbnail">' +
                        '<img>' +
                        '</li>'
                    );


                // $list为容器jQuery实例 重新添加
                $list.empty().append($li);
                $('#' + file.id).addClass('upload-state-done').find('img').attr('src',response.url);
                $coverImg.val(response.ftpUrl);//置空隐藏字段
            }else{
                alertx(response.msg);
                uploader.removeFile(file);
            }
        });

// 文件上传失败，显示上传出错。
        uploader.on('uploadError', function (file) {
            var $li = $('#' + file.id),
                $error = $li.find('div.error');

            // 避免重复创建
            if (!$error.length) {
                $error = $('<div class="error"></div>').appendTo($li);
            }

            $error.text('上传失败');
            $coverImg.val('');//置空隐藏字段
        });

// 完成上传完了，成功或者失败，先删除进度条。
        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').remove();
        });
    });



    function webUploader(option){
        var config = $.extend({},webUploader.DEFAULT,option);
        var uploader = WebUploader.create(config);
        // 文件上传过程中创建进度条实时显示。
        uploader.on('uploadProgress', function (file, percentage) {
            var $li = $('#' + file.id),
                $percent = $li.find('.progress span');

            // 避免重复创建
            if (!$percent.length) {
                $percent = $('<p class="progress"><span></span></p>')
                    .appendTo($li)
                    .find('span');
            }

            $percent.css('width', percentage * 100 + '%');
        });

// 文件上传成功，给item添加成功class, 用样式标记上传成功。
        uploader.on('uploadSuccess', function (file, response) {
            if(response.state == 'SUCCESS'){
                var $li = $(
                    '<li id="' + file.id + '" class="file-item thumbnail">' +
                    '<img>' +
                    '</li>'
                );
                // $list为容器jQuery实例 重新添加
                $list.empty().append($li);
                $('#' + file.id).addClass('upload-state-done').find('img').attr('src',response.url);
                $coverImg.val(response.ftpUrl);//置空隐藏字段
            }else{
                alertx(response.msg);
                uploader.removeFile(file);
            }
        });

// 文件上传失败，显示上传出错。
        uploader.on('uploadError', function (file) {
            var $li = $('#' + file.id),
                $error = $li.find('div.error');

            // 避免重复创建
            if (!$error.length) {
                $error = $('<div class="error"></div>').appendTo($li);
            }

            $error.text('上传失败');
            $coverImg.val('');//置空隐藏字段
        });

// 完成上传完了，成功或者失败，先删除进度条。
        uploader.on('uploadComplete', function (file) {
            $('#' + file.id).find('.progress').remove();
        });
    }

    webUploader.DEFAULT = {
        // 选完文件后，是否自动上传。
        auto: true,

        // swf文件路径
        swf: '/static/webuploader/Uploader.swf',

        // 文件接收服务端。
        server: $frontOrAdmin+'/ftp/ueditorUpload/uploadImg?floder=actyw&imgWidth=100&imgHeight=100',

        // 选择文件的按钮。可选。
        // 内部根据当前运行是创建，可能是input元素，也可能是flash.
        pick: '#filePicker',
        fileVal: 'upfile',

        // 只允许选择图片文件。
        accept: {
            title: 'Images',
            extensions: 'gif,jpg,jpeg,bmp,png',
            mimeTypes: 'image/*'
        }
    }

})();


