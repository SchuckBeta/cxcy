/**
 * Created by qingtengwang on 2017/7/7.
 */

(function ($) {
    $(function () {
        var $accessoryList = $('#accessoryList');
        var $accessoryListPdf = $('#accessoryListPdf');
        var BASE_URL = '/static/webuploader';
        var $loading;
        var thumbnailWidth = 100;
        var thumbnailHeight = 100;
        var uploader = WebUploader.create({
            auto: true,
            swf: BASE_URL + '/Uploader.swf',
            server: $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder=scoapply', //文件上传地址 folder表示功能模块
            pick: '#btnUpload',
            fileVal: 'upfile',
            accept: {
                title: '图片或者PDF',
                extensions: 'gif,jpg,jpeg,bmp,png,pdf',
                mimeTypes: 'image/*,application/pdf'
            }
        });


        uploader.on('fileQueued', function (file) {
            var $li;
            if (file.ext == 'pdf') {
                $li = $('<li id="' + file.id + '" class="file-item">' +
                    '<div class="file-info"><a href="javascript:void(0);"><img class="pic-icon" src="' + ctxImg + '/filetype/' + switchImgType(file.ext) + '.png">' + file.name + '</a><i class="icon icon-remove-sign"></i></div>' +
                    '<p class="loading"><img src="' + ctxImages + '/loading.gif"></p><p class="error"></p></li>');
                $accessoryListPdf.append($li);
            } else {
                $li = $('<li id="' + file.id + '" class="file-item file-item-img">' +
                    '<div class="file-info"><a href="javascript:void(0);"><p class="file-name">' + file.name + '</p></a><i class="icon icon-remove-sign"></i></div>' +
                    '<p class="loading"><img src="' + ctxImages + '/loading.gif"></p><p class="error"></p></li>');
                $accessoryList.append($li);

                uploader.makeThumb(file, function (error, src) {
                    if (error) {
                        return;
                    }
                    var img = $('<img class="img-responsive lightbox" src="' + src + '">');
                    var $fileName = $li.find('.file-name');
                    $(img).insertBefore($fileName);
                }, thumbnailWidth, thumbnailHeight);
            }
            $loading = $li.find('p.loading');
            $loading.css('display', 'block');
        });


        uploader.on('uploadSuccess', function (file, response) {
            var $li = $('#' + file.id);
            var $a = $li.find('a');
            $a.attr({
                'data-original': response.original,
                'data-size': response.size,
                'data-title': response.title,
                'data-type': response.type,
                'data-ftp-url': response.ftpUrl,
                'data-url': response.url,
                'href': response.url
            });
            $loading = $li.find('p.loading');
            $loading.hide();
            if(typeof baguetteBox == 'undefined'){

            }else{
                baguetteBox.run('.tz-gallery', {
                    filter: /.+\.(gif|jpe?g|png|webp|bmp)/i
                });
            }

        });

        uploader.on('uploadError', function (file) {
            var $li = $('#' + file.id);
            showModalMessage(0, "上传失败(文件超过100M或者服务器异常)", {
                '确定': function () {
                    $(this).dialog("close");
                    $li.remove();
                }
            });
        });

        function switchImgType(imgType) {
            var extname;
            switch (imgType) {
                case "xls":
                case "xlsx":
                    extname = "excel";
                    break;
                case "doc":
                case "docx":
                    extname = "word";
                    break;
                case "ppt":
                case "pptx":
                    extname = "ppt";
                    break;
                case "jpg":
                case "jpeg":
                case "gif":
                case "png":
                case "bmp":
                    extname = "image";
                    break;
                case 'txt':
                    extname = 'txt';
                    break;
                case 'zip':
                    extname = 'zip';
                    break;
                case 'rar':
                    extname = 'rar';
                    break;
                default:
                    extname = "unknow";
            }
            return extname;
        }

        $(document).on('click', 'i.icon-remove-sign', function () {
            $(this).parents('li').detach();
            //向服务器发出请求，删除文件
            var url = $(this).prev().attr("data-ftp-url");

            $.ajax({
                type: 'post',
                url: $frontOrAdmin+'/ftp/ueditorUpload/delFile',
                data: {url: url},
                success: function (data) {
                    console.log("删除成功");
                    uploader.reset();
                }
            });

        })

    })


})(jQuery);