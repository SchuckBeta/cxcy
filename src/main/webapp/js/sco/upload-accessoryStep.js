/**
 * Created by qingtengwang on 2017/7/7.
 */

;(function ($, WebUploader) {

    $.fn.uploadAccessory = function (option) {
        var options = $.extend({}, $.fn.uploadAccessory.default, option);
        var auto = options.auto;
        var serve = options.serverUrl;
        var fileVal = options.fileVal;
        var accept = options.accept;
        var swf = options.swf;
        var $containerOther = $(options.containerOtherId);
        var $containerImg = $(options.containerImgId);
        var thumbnailWidth = options.thumbnailWidth;
        var thumbnailHeight = options.thumbnailHeight;
        var imageExt = options.imageExt;
        var isPreView = options.isPreView;
        var btnPickId = options.btnPickId;
        var $btnPick = $(btnPickId);
        var $loading;
        var hasBaguetteBox = typeof baguetteBox != 'undefined';
        var errorMsg;
        var fileNumLimit = options.fileNumLimit;
        var fileSizeLimit = options.fileSizeLimit;
        var fileSingleSizeLimit = options.fileSingleSizeLimit;
        var inputKeys = options.inputKeys;
        var isImagePreview = options.isImagePreview;
        var multiple = options.multiple;
        var isAcceptUpload = false;
        var uploader = WebUploader.create({
            auto: auto,
            swf: swf,
            server: serve,
            fileVal: fileVal,
            pick: {
                id: btnPickId,
                multiple: multiple
            },
            accept: accept,
            fileNumLimit: fileNumLimit,
            fileSizeLimit: fileSizeLimit,
            fileSingleSizeLimit: fileSingleSizeLimit
        });


        uploader.on('beforeFileQueued', function (file) {
            var pattern = new RegExp("[~'!@#￥$%^&*+=:]") //` ~ ! @ # $ % ^ + & * \\ / ? | : . < > {} () [] \" ";
            isAcceptUpload = true;
            if(pattern.test(file.name)){
                // uploader.reset();
                uploader.removeFile(file);
                isAcceptUpload = false;
                showModalMessage(0, '上传文件名不能包含特殊字符');
            }
        });

        uploader.on('fileQueued', function (file) {
            var $li;

            var $beforeFileQueued = $.Event('beforeFileQueued',{ uploader:uploader, file: file });

            $btnPick.trigger($beforeFileQueued);
            if(!isAcceptUpload){
                return false;
            }
            if (new RegExp(file.ext).test(imageExt) && isImagePreview) {
                $li = $('<li id="' + file.id + '" class="file-item file-item-img">' +
                    '<div class="file-info"><a class="file" href="javascript:void(0);"><p class="file-name">' + file.name + '</p></a><i class="icon icon-remove-sign"></i></div>' +
                    '<p class="loading"><img src="' + ctxImages + '/loading.gif"></p><p class="error"></p></li>');
                $containerImg.append($li);
                uploader.makeThumb(file, function (error, src) {
                    if (error) {
                        return;
                    }
                    var img = $('<img class="img-responsive lightbox" src="' + src + '">');
                    var $fileName = $li.find('.file-name');
                    $(img).insertBefore($fileName);
                }, thumbnailWidth, thumbnailHeight);

            } else {
                $li = $('<li id="' + file.id + '" class="file-item">' +
                    '<div class="file-info"><a class="file" href="javascript:void(0);"><img class="pic-icon" src="' + ctxImg + '/filetype/' + switchImgType(file.ext) + '.png">' + file.name + '</a><i class="icon icon-remove-sign"></i></div>' +
                    '<p class="loading"><img src="' + ctxImages + '/loading.gif"></p><p class="error"></p></li>');
                $containerOther.append($li);
            }
            $loading = $li.find('p.loading');
            $loading.css('display', 'block');
        });

        uploader.on('uploadSuccess', function (file, response) {
            var $li = $('#' + file.id);
            var $a = $li.find('a');
            var $successEvent = $.Event('uploadSuccess', {uploader: uploader, file: file, response: response});
            $a.attr({
                'data-original': response.original,
                'data-size': response.size,
                'data-title': response.title,
                'data-type': response.type,
                'data-ftp-url': response.ftpUrl,
                'data-url': response.url
            });
            if(isImagePreview){
                $a.attr({
                    'href': response.url
                });
            }
            $loading = $li.find('p.loading');
            $loading.hide();
            if (isPreView && hasBaguetteBox) {
                baguetteBox.run('.tz-gallery', {
                    filter: /.+\.(gif|jpe?g|png|webp|bmp)/i
                });
            }

            $btnPick.trigger($successEvent);
        });

        uploader.on('uploadError', function (file, reason) {
            var $li = $('#' + file.id);
            var $uploadErrorEvent = $.Event('uploadError', {uploader: uploader, file: file});
            try {
                showModalMessage(0, '上传失败(错误代码'+reason+')', {
                    '确定': function () {
                        $(this).dialog("close");
                        $li.remove();
                    }
                });
            } catch (e) {
                console.log(e)
            }
            $btnPick.trigger($uploadErrorEvent)
        });


        uploader.on('error', function (state) {
            switch (state) {
                case 'Q_TYPE_DENIED':
                    errorMsg = options.acceptErrorMsg;
                    break;
                case 'Q_EXCEED_NUM_LIMIT':
                    errorMsg = '添加的数量超过' + fileNumLimit + '个';
                    break;
                case 'Q_EXCEED_SIZE_LIMIT':
                    errorMsg = '文件过大';
                    break;
                case 'F_DUPLICATE':
                    errorMsg = '请勿重复上传文件';
                    break;
                default:
                    errorMsg = '未知错误';
                    break;
            }
            try {
                showModalMessage(0, errorMsg);
            } catch (e) {
                console.info('require jquery dialog')
            }
        });

        $(document).on('click', 'i.icon-remove-sign', function () {

            //向服务器发出请求，删除文件
            var url = $(this).prev().attr("data-ftp-url");
            var id = $(this).prev().attr("data-id") || '';
            var $deleteFileComplete = $.Event('deleteFileComplete');
            var $deleteFileBefore = $.Event('deleteFileBefore');
            var $deleteFileFail = $.Event('deleteFileFail');
            var $this = $(this);
            var xhr;

            showModalMessage(0, '确定删除这个附件吗？', [{
                text: '确定',
                click: function () {
                    $btnPick.trigger($deleteFileBefore);
                    xhr = $.post($frontOrAdmin+'/ftp/ueditorUpload/delFile', { id: id, url: url });
                    xhr.success(function (data) {
                        if(data){
                            $btnPick.trigger($deleteFileComplete);
                            $("#fujianpp").html("");
                            uploader.reset();
                            $this.parents('li').detach();
                        }else{
                            $btnPick.trigger($deleteFileFail);
                        }
                    });
                    xhr.error(function (error) {
                       // showModalMessage()
                    });
                    $(this).dialog("close");
                }
            },{
                text: '取消',
                click: function () {
                    $(this).dialog("close");
                }
            }]);
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

    };


    $.fn.uploadAccessory.default = {
        thumbnailWidth: 100,
        thumbnailHeight: 100,
        accept: {
            title: '上传word或者zip类型文件',
            extensions: 'docx,doc,zip,rar',
            mimeTypes: ''
        },
        acceptErrorMsg: '请上传word或者zip类型文件',
        fileVal: 'upfile',
        swfUrl: '/static/webuploader/Uploader.swf',
        containerImgId: '#accessoryList',
        containerOtherId: '#accessoryListPdf',
        auto: true,
        serverUrl: $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder=scoapply',
        btnPickId: '#btnUpload',
        isPreView: false,
        imageExt: 'gif,jpg,jpeg,bmp,png',
        fileNumLimit: '',
        fileSizeLimit: 100 * 1024 * 1024,    // 100 M
        fileSingleSizeLimit: 50 * 1024 * 1024,    // 50 M,
        hasFiles: true,
        inputKeys: 'url,name,size,suffix',
        inputName: 'attachmentList',
        isFormData: false,
        isImagePreview: true,
        multiple: true
    };

})(jQuery, WebUploader);