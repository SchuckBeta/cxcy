/**
 * Created by Administrator on 2018/1/24.
 */

+function ($, WebUploader) {
    function Uploader(element, option) {
        this.element = element;
        this.options = $.extend(true, Uploader.DEFAULT, {
            pick: {
                id: element
            }
        }, (option || {}));

        this.uploader = element ? WebUploader.create(this.options) : null;
        this.$fileContainer = null;
        this.init();
    }

    Uploader.DEFAULT = {
        auto: false, //自动上传
        swf: '/static/webuploader/Uploader.swf',
        server: $frontOrAdmin + '/ftp/ueditorUpload/uploadTemp?folder=uploader', //上传地址
        chunked: false, //分片上传
        method: 'POST',
        deleteUrl: $frontOrAdmin + '/ftp/ueditorUpload/delFile',
        pick: {
            label: '上传附件',
            multiple: false //开启一次上传多个文件
        },
        accept: {
            extensions: 'gif,jpg,jpeg,bmp,png,xls,xlsx,doc,docx,ppt,pptx,txt,zip,rar,pdf' //可上传表格，文档，ppt pdf， txt, 图片
        },
        fileVal: 'upfile',
        template: '<div data-file-id="{{fileId}}" class="accessory"><div class="accessory-info">' +
        '<a class="accessory-file" title="{{name}}" href="javascript: void(0);" data-view-url="{{viewUrl}}" data-url="{{url}}" data-ftp-url="{{ftpUrl}}" data-name="{{name}}" data-file-id="{{fileId}}"><img src="' + ctxImg + '/filetype/{{imgType}}.png"> ' +
        '<span class="accessory-name">{{name}}</span></a> ' +
        '<i title="下载{{name}}" class="btn-downfile-newaccessory"></i>' +
        '<i title="删除{{name}}" class="btn-delete-newaccessory"></i></div>' +
        '<div class="accessory-layer text-center" style="display:none"><img src="' + ctxImages + '/loading.gif"><div file-id="{{fileId}}" title="取消上传" class="pull-right btn-cancel-upload"><img src="' + ctxImg + '/btn-hover-delete-file.png"> </div></div></div>',
//            fileNumLimit: 100, //验证文件总数量, 超出则不允许加入队列
        fileSizeLimit: typeof webMaxUpFileSize != 'undefined' ? webMaxUpFileSize : 50 * 1024 * 1024,    // 100 M 验证文件总大小是否超出限制, 超出则不允许加入队列
        fileSingleSizeLimit: typeof webMaxUpFileSize != 'undefined' ? webMaxUpFileSize : 50 * 1024 * 1024,    // 50 M 验证单个文件大小是否超出限制, 超出则不允许加入队列,
        inputValues: {
            fileStepEnum: '',
            fileTypeEnum: '',
            gnodeId: '',
            fileInfoPrefix: ''
        },
        inputs: '<div class="fileparamspan"><input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.fielSize" value="{{size}}"/>' +
        '<input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.fielTitle" value="{{title}}"/>' +
        '<input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.fielType" value="{{type}}"/>' +
        '<input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.fielFtpUrl" value="{{ftpUrl}}"/>' +
        '<input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.fileStepEnum" value="{{fileStepEnum}}"/>' +
        '<input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.fileTypeEnum" value="{{fileTypeEnum}}"/>' +
        '<input type="hidden" name="{{fileInfoPrefix}}attachMentEntity.gnodeId" value="{{gnodeId}}"/></div>',
        beforeFileFn: function () {
        },
        fileQueued: function () {

        },
        fileSuccess: function () {

        },
        fileCompleted: function () {

        },
        fileError: function () {

        },
        cancelFileFn: function () {

        }
    };

    Uploader.prototype.init = function () {
        this.getFileContainer();
        if (!this.uploader) {
            if (!this.options.containerId) {
                console.error('初始化元素不存在， 必须指定文件包裹容器ID');
                return false;
            }
        }
        this.deleteFile();
        this.viewFile();
        this.downFile();
        if (this.uploader) {
            this.beforeFileQueued();
            this.fileQueued();
            this.uploadSuccess();
            this.uploadComplete();
            this.uploadError();
            this.fileDequeued();
            this.cancelFile();
            this.error();
            this.reset();
        }

    };

    Uploader.prototype.beforeFileQueued = function () {
        var beforeFileFn = this.options.beforeFileFn;
        var self = this;
        var $element = $(this.element);
        this.uploader.on('beforeFileQueued', function (file) {
            var isDisabled = $element.hasClass('disabled');
            if (isDisabled) {
                return false;
            }
            if (file.size == 0) {
                dialogCyjd.createDialog(0, file.name + '内容为空');
                return false;
            }

            beforeFileFn && beforeFileFn.call(self, file);
        })

    };

    Uploader.prototype.hasSameFile = function (file) {
        var files = this.options.files;
        if (file.length < 1) {
            return false
        }
        ;
        for (var i = 0; i < files.length; i++) {
            var fileItem = files[i];
            var type = fileItem.remotePath ? fileItem.suffix : fileItem.type;
            if (type === file.ext && fileItem.name === file.name) {
                return fileItem;
            }
        }
        return false;
    }

    Uploader.prototype.getSameFile = function (file) {
        var files = this.options.files;
        if (file.length < 1) {
            return false
        }
        ;
        for (var i = 0; i < files.length; i++) {
            var fileItem = files[i];
            var type = fileItem.remotePath ? fileItem.suffix : fileItem.type;
            if (type === file.ext && fileItem.name === file.name) {
                return fileItem;
            }
        }
        return false;
    }

    Uploader.prototype.getFileContainer = function () {
        var containerId = this.options.containerId;
        var $accessoriesContainer = this.element ? $(this.element).parents('.btn-accessory-box').prev() : null;
        this.$fileContainer = containerId ? $(containerId) : $accessoriesContainer;
    };

    Uploader.prototype.fileQueued = function () {
        var template = this.options.template;
        var self = this;
        var $li;
        var $fileContainer = this.$fileContainer;
        var fileQueued = this.options.fileQueued;
        this.uploader.on('fileQueued', function (file) {
            var sameFile;
            var li = template.replace(/\{\{(\w+)\}\}/g, function ($1, $2) {
                switch ($2) {
                    case 'fileId':
                        return file['id'];
                    case 'url':
                    case 'viewUrl':
                    case 'ftpUrl':
                        return $1;
                    case 'imgType':
                        return self.switchImgType(file['ext']);
                    default:
                        return file[$2]
                }
            });
            $li = $(li);
            $li.find('.accessory-layer').show();
            $fileContainer.children().append($li);
            self.uploader.stop();
            sameFile = self.hasSameFile(file)
            if (sameFile) {
                dialogCyjd.createDialog(0, file.name + '与已上传的文件存在同名，继续上传会覆盖原文件， 是否继续？', {
                    buttons: [{
                        'text': '确定',
                        'class': 'btn btn-sm btn-small btn-primary',
                        'click': function () {
                            $(this).dialog('close');
                            var $fileLi = $('.accessory[data-file-id="' + sameFile.id + '"]');
                            self.deleteSameFile(sameFile.id, sameFile.url, '', $fileLi).then(function () {
                                self.uploader.upload();
                            })
                        }
                    }, {
                        'text': '取消',
                        'class': 'btn btn-sm btn-small btn-default',
                        'click': function () {
                            self.uploader.removeFile(file)
                            $li.remove();
                            $(this).dialog('close')

                        }
                    }]
                });
            } else {
                self.uploader.upload();
            }

            if ($fileContainer.is(':hidden')) {
                $fileContainer.show();
            }
            fileQueued && fileQueued.call(self, file);
        })
    };

    Uploader.prototype.uploadSuccess = function () {
        var $fileLi, $layer, $accessoryFile, $btnDeleteFile;
        var inputs = this.options.inputs;
        var inputValues = this.options.inputValues;
        var inputTemp;
        var uploader = this.uploader;
        var fileSuccess = this.options.fileSuccess;
        var self = this;
        this.uploader.on('uploadSuccess', function (file, respons) {
            var viewUrl = '';
            $fileLi = $('.accessory[data-file-id="' + file.id + '"]');
            $layer = $fileLi.find('.accessory-layer');
            if (respons.state === 'FAIL') {
                uploader.removeFile(file, true);
                $layer.remove();
                $fileLi.remove();
                dialogCyjd.createDialog(0, '上传失败');
                return false;
            }


            $accessoryFile = $fileLi.find('.accessory-file');
            $accessoryFile.attr('data-ftp-url', respons.ftpUrl);
            $accessoryFile.attr('data-url', respons.url);
            $accessoryFile.attr('data-id', respons.id);
            if (new RegExp(respons.type, 'i').test('gif,jpg,jpeg,bmp,png,pdf')) {
                viewUrl += ftpHttp + respons.ftpUrl.replace('/tool', '')
            }
            $accessoryFile.attr('data-view-url', viewUrl);
            if (viewUrl) {
                $accessoryFile.attr('title', '预览' + respons.title)
            }
            $btnDeleteFile = $fileLi.find('.btn-delete-accessory');
            $layer.remove();
            inputTemp = inputs.replace(/\{\{(\w+)\}\}/g, function ($1, $2) {
                switch ($2) {
                    case 'fileStepEnum':
                    case 'fileTypeEnum':
                    case 'gnodeId':
                    case 'fileInfoPrefix':
                        return inputValues[$2];
                    default:
                        return respons[$2]
                }
            });
            $fileLi.append(inputTemp);
            fileSuccess && fileSuccess.call(self, file, respons);
            self.options.files.push(respons);
            var sameFile = self.getSameFile(file)
            if (sameFile) {
                $('a[data-ftp-url="' + sameFile.url + '"]').parents('.accessory').remove()
            }
            // dialogCyjd.createDialog(1, '上传成功');
        })
    };

    Uploader.prototype.uploadComplete = function () {
        var fileCompleted = this.options.fileCompleted;
        var self = this;
        this.uploader.on('uploadComplete', function (file) {
            fileCompleted && fileCompleted.call(self, file)
        })
    };

    //上传错误
    Uploader.prototype.uploadError = function () {
        var uploader = this.uploader;
        var fileError = this.options.fileError;
        var self = this;
        this.uploader.on('uploadError', function (file, error) {
            uploader.removeFile(file, true);
            fileError && fileError.call(self, file, error);
            dialogCyjd.createDialog(0, '网络连接失败');
        })
    };

    Uploader.prototype.cancelFile = function () {
        var uploader = this.uploader;
        var cancelFileFn = this.options.cancelFileFn;
        var self = this;
        var containerId = this.options.containerId;
        var $container = containerId ? $(containerId) : null;
        if ($container.size() < 1) {
            return false;
        }
        $container.on('click', '.btn-cancel-upload', function (e) {
            e.stopPropagation();
            var $target = $(this);
            var fileId = $target.attr('file-id');

            uploader.cancelFile(fileId);
            $target.parents('.accessory').remove();
            $container.find('.accessory').size() < 1 && $container.hide();
            cancelFileFn && cancelFileFn.call(self, fileId);
        })
    }

    Uploader.prototype.fileDequeued = function () {
        this.uploader.on('fileDequeued', function (file) {

        })
    };
    //基本类型错误
    Uploader.prototype.error = function () {
        var fileSizeLimit = this.options.fileSizeLimit;
        this.uploader.on('error', function (type) {
            var msg;
            switch (type) {
                case 'Q_TYPE_DENIED':
                    msg = '请上传word、pdf、excel、txt、rar、ppt和图片类型文件';
                    break;
                case 'Q_EXCEED_NUM_LIMIT':
                    msg = '超过文件最大限制' + (Math.floor(fileSizeLimit / 1024 / 1024)) + 'M，不允许添加';
                    break;
                case 'Q_EXCEED_SIZE_LIMIT':
                    msg = '文件超过' + (Math.floor(fileSizeLimit / 1024 / 1024)) + 'M，添加失败';
                    break;
                case 'F_DUPLICATE':
                    msg = '已经上传过该文件了';
                    break;
                default:
                    msg = '错误类型' + type;
            }
            dialogCyjd.createDialog(0, msg);
        })
    };

    Uploader.prototype.reset = function () {
        this.uploader.on('reset', function (file) {

        })
    };

    Uploader.prototype.deleteFile = function () {
        var uploader = this.uploader;
        var self = this;
        var $fileContainer = this.$fileContainer;
        $fileContainer.on('click.delete', 'i.btn-delete-newaccessory', function (e) {
            e.stopPropagation();
            e.preventDefault();
            var $ele = $(this);
            var $accessoryFile = $ele.parent().find('.accessory-file');
            var fileId = $accessoryFile.attr('data-file-id');
            var $fileLi = $(this).parents('.accessory');
            var id = $accessoryFile.attr('data-id');
            var name = $accessoryFile.attr('data-name');
            var ftpUrl = $accessoryFile.attr('data-ftp-url');
            if (fileId) {
                uploader.removeFile(fileId, true);
                $fileLi.remove();
                self.removeOptionFile(fileId);
            } else {
                self.deleteSavedFile(id, ftpUrl, name, $fileLi)
            }
            $fileContainer.find('.accessory').size() < 1 && $fileContainer.hide();
        });
    };

    Uploader.prototype.removeOptionFile = function (fileId) {
        var files = this.options.files;
        for (var i = 0; i < files.length; i++) {
            var fileItem = files[i];
            var type = fileItem.remotePath ? fileItem.suffix : fileItem.type;
            if (fileId === fileItem.id) {
                files.splice(i, 1)
            }
        }
    }

    Uploader.prototype.removeFile = function () {

    };

    Uploader.prototype.deleteSameFile = function (id, ftpUrl, name, $fileLi) {
        var $fileContainer = this.$fileContainer;
        var deleteUrl = this.options.deleteUrl;
        var self = this;
        return  $.ajax({
            url: deleteUrl,
            type: 'POST',
            data: {id: id, url: ftpUrl},
            success: function (data) {
                $fileLi.remove();
                $fileContainer.find('.accessory').size() < 1 && $fileContainer.hide();
                self.removeOptionFile(id);
            },
            error: function () {
            }
        })
    };

    Uploader.prototype.deleteSavedFile = function (id, ftpUrl, name, $fileLi) {
        var deleteUrl = this.options.deleteUrl;
        var $fileContainer = this.$fileContainer;
        var self = this;
        dialogCyjd.createDialog(0, '是否删除' + name + '？', {
            title: '删除附件',
            buttons: [{
                text: '删除',
                'class': 'btn btn-primary btn-sm btn-small btn-delete-file',
                'click': function () {
                    var $btnDeleteFile = $(this).parents('.dialog-cyjd-container').find('.btn-delete-file');
                    var $this = $(this);
                    $btnDeleteFile.prop('disabled', true);
                    $.ajax({
                        url: deleteUrl,
                        type: 'POST',
                        data: {id: id, url: ftpUrl},
                        success: function (data) {
                            if(data){
                                $fileLi.remove();
                                $fileContainer.find('.accessory').size() < 1 && $fileContainer.hide();

                                self.removeOptionFile(id);

                                $this.dialog('close');
                            }

                        },
                        error: function () {
                            $btnDeleteFile.prop('disabled', false);
                            $btnDeleteFile.text('重新删除')
                        }
                    })
                }
            }, {
                text: '取消',
                'class': 'btn btn-default btn-sm btn-small',
                'click': function () {
                    $(this).dialog('close');
                }
            }]
        });
    };

    Uploader.prototype.viewFile = function () {
        var $fileContainer = this.$fileContainer;
        $fileContainer.on('click.delete', 'a.accessory-file', function (e) {
            e.stopPropagation();
            e.preventDefault();
            var $ele = $(this);
            var viewUrl = $ele.attr('data-view-url');
            if (viewUrl) {
                window.open(viewUrl);
            } else {
                dialogCyjd.createDialog(0, '暂不支持预览，请下载查阅')
            }
        });
    };

    Uploader.prototype.downFile = function () {
        var $fileContainer = this.$fileContainer;
        $fileContainer.on('click.downfile', 'i.btn-downfile-newaccessory', function (e) {
            e.stopPropagation();
            e.preventDefault();
            var $ele = $(this);
            var $accessoryFile = $ele.parent().find('.accessory-file');
            var id = $accessoryFile.attr('data-id');
            var fileName = $accessoryFile.attr('data-name');
            var ftpUrl = $accessoryFile.attr('data-ftp-url');
            location.href = $frontOrAdmin + "/ftp/ueditorUpload/downFile?url=" + ftpUrl + "&fileName="
                + encodeURI(encodeURI(fileName));
        });
    };

    Uploader.prototype.switchImgType = function (ext) {
        var extname;
        switch (ext) {
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
    };

    window['Uploader'] = Uploader;
}(jQuery, ((typeof WebUploader !== 'undefined') ? WebUploader : {}));