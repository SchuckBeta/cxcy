/**
 * Created by Administrator on 2017/12/12.
 */


+function ($) {

    Vue.directive('upload-file', {
        inserted: function (element, binding, vnode) {
            var ctxStatic = ctxStatic;
            var uploadDefault = {
                fileVal: 'upfile',
                pick: {
                    id: element,
                    multiple: false
                },
                method: 'POST',
                auto: true,
                server: vnode.context.url,
                swfUrl: ctxStatic + '/webuploader/Uploader.swf',
                fileNumLimit: '',
                acceptErrorMsg: '',
                accept: {
                    extensions: 'gif,jpg,jpeg,bmp,png,xls,xlsx,doc,docx,ppt,pptx,txt,zip,rar,pdf' //可上传表格，文档，ppt pdf， txt, 图片
                },
                // formData: vnode.context.formData,
                fileSizeLimit: typeof webMaxUpFileSize != 'undefined' ? webMaxUpFileSize : 50 * 1024 * 1024,    // 100 M 验证文件总大小是否超出限制, 超出则不允许加入队列
                fileSingleSizeLimit: typeof webMaxUpFileSize != 'undefined' ? webMaxUpFileSize : 50 * 1024 * 1024,    // 50 M 验证单个文件大小是否超出限制, 超出则不允许加入队列,
            };
            var options = $.extend({}, binding.value, uploadDefault);
            var uploader;
            vnode.context.uploader = WebUploader.create(options);
            uploader = vnode.context.uploader;
            uploader.on('beforeFileQueued', function (file) {

            });

            uploader.on('fileQueued', function (file) {
                vnode.context.$emit('change-saveing', true)
                vnode.context.fileList.push({
                    type: file.ext,
                    title: file.name,
                    loading: true
                });
                // vnode.context.uploader.formData.fileName = file.name
                $(element).parent().prev().find('.accessory-layer').removeClass('hide')
            })

            uploader.on('uploadSuccess', function (file, response) {
                var lastFileIndex;
                if (response.status) {
                    dialogCyjd.createDialog(1, '上传附件' + file.name + '成功', {
                        title: '上传成功'
                    });
                    lastFileIndex = vnode.context.fileList.length - 1;
                    vnode.context.fileList[lastFileIndex].loading = false;
                    vnode.context.fileList[lastFileIndex].fileId = file.id;
                    vnode.context.fileList[lastFileIndex].suffix = response.datas.type;
                    $.extend(vnode.context.fileList[lastFileIndex], response.datas);
                } else {
                    dialogCyjd.createDialog(0, response.msg, {
                        title: '上传失败'
                    })
                    vnode.context.fileList.splice(vnode.context.fileList.length - 1, 1)
                    $(element).parent().prev().find('.accessory-layer').addClass('hide')
                    uploader.removeFile(file.fileId)
                }
                vnode.context.$emit('change-saveing', false)

            })

            uploader.on('uploadError', function (file, code) {
                var lastFileIndex = vnode.context.fileList.length - 1;
                vnode.context.fileList.splice(lastFileIndex, 1);
                dialogCyjd.createDialog(0, '上传附件' + file.name + '失败，错误代码：' + code.status, {
                    title: '上传失败'
                })
                $(element).parent().prev().find('.accessory-layer').addClass('hide')
                uploader.removeFile(file.fileId)
                vnode.context.$emit('change-saveing', false)
            });

            uploader.on('error', function (state) {
                var errorMsg;
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
                dialogCyjd.createDialog(0, '错误消息：' + errorMsg, {
                    title: '上传失败'
                })
            });
        },
        unbind: function (element, binding, vnode) {
            vnode.context.uploader.destroy();
        }
    })

}(jQuery);