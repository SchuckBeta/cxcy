/**
 * Created by Administrator on 2017/12/12.
 */

+function ($) {
    var uploader = Vue.component('uploader', {
        template: '<div><div class="accessories-container"  v-show="fileList.length>0"> ' +
        '<div class="accessories" :class="className"> ' +
        '<div class="accessory" v-for="(item, index) in fileList"> <template v-if="item.id">' +
        '<div class="accessory-info"> ' +
        '<a :href="item.url" @click="downFile(item, $event)">' +
        '<img :src="item.suffix | imageType">' +
        ' <span class="accessory-name">{{item.title || item.name}}</span></a>' +
        '<i @click="removeAccessory(item, index)" class="btn-delete-accessory"><img src="' + ctxImg + '/remove-accessory.png"></i>' +
        ' </div></template>' +
        '<template v-if="!item.id"><div class="accessory-info">' +
        '<a :href="item.url" @click="downFile(item,  $event)"><img :src="item.type | imageType">' +
        '<span class="accessory-name">{{item.title}}</span></a>' +
        '<i @click="removeAccessory(item, index)" class="btn-delete-accessory"><img src="' + ctxImg + '/remove-accessory.png"></i></div></template> ' +
        '<div class="accessory-layer text-center" style="display: none" v-show="item.loading"><img src="' + ctxImages + '/loading.gif"> </div> </div> </div> </div> ' +
        '<div  v-if="isUploadFile"  class="btn-accessory-box" style="height: 34px;"> ' +
        '<div v-upload-file class="upload-content" style="display: inline-block;">上传附件</div>' +
        '<span v-show="tip" class="gray-color" style="margin-left: 8px; line-height: 34px;display: inline-block;vertical-align: top">{{tip}}</span>' +
        '<label v-show="fileErrorShow" class="file-error error">{{error}}</label></div></div>',
        model: {
            prop: 'fileList',
            event: 'change'
        },
        props: {
            fileList: {
                type: Array,
                default: function () {
                    return []
                }
            },
            className: {
                type: String,
                default: ''
            },
            url: {
                prop: String,
                default: ''
            },
            formData: {
                type: String,
                default: function () {
                    return {}
                }
            },
            tip: {
                type: String,
                default: ''
            },
            error: {
                type: String,
                default: '必填信息'
            },
            fileErrorShow: {
                type: Boolean,
                default: false
            },
            isUploadFile: {
                type: Boolean,
                default: true
            }
        },
        data: function () {
            return {
                filesHide: [],
                uploader: ''
            }
        },
        filters: {
            imageType: function (type) {
                var extname;
                switch (type) {
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
                return '/img/filetype/' + extname + '.png';
            }
        },
        methods: {


            //下载附件
            downFile: function (item, $event) {
                $event.preventDefault();
                if(item.id){
                    location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + (item.url) + "&fileName=" + encodeURI(encodeURI((item.title || item.name)));
                }else{
                    location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + (item.ftpUrl) + "&fileName=" + encodeURI(encodeURI((item.title || item.name)));
                }
            },
            //删除附件
            removeAccessory: function (item, index) {
                var self = this;
                var uploader = this.uploader;
                dialogCyjd.createDialog(0, '是否删除附件' + (item.title || item.name), {
                    title: '删除附件消息',
                    buttons: [{
                        text: '确定',
                        class: 'btn btn-sm btn-primary',
                        click: function () {
                            var $this = $(this);
                            var xhr;

                            if (!item.id) {
                                if (item.fileId) {
                                    uploader.removeFile(item.fileId);
                                    self.fileList.splice(index, 1);
                                    $this.dialog("close");
                                } else {
                                    self.fileList.splice(index, 1);
                                    $this.dialog("close");
                                }
                                return false;
                            }
                            xhr = $.post('/f/attachment/sysAttachment/ajaxDelete/' + (item.id), {
                                ftpUrl: item.ftpUrl
                            });
                            xhr.success(function (data) {
                                if (data.status) {
                                    self.fileList.splice(index, 1);
                                    $this.dialog("close");
                                } else {
                                    dialogCyjd.createDialog(0, data.msg)
                                }
                            })
                            xhr.error(function (data) {
                                dialogCyjd.createDialog(0, '网络连接错误， 错误代码：' + data.status)
                            })
                        }
                    }, {
                        text: '取消',
                        class: 'btn btn-sm btn-default',
                        click: function () {
                            $(this).dialog('close')
                        }
                    }]
                })
            }
        },
        beforeMount: function () {
            var filesHide = this.filesHide;
            this.fileList.forEach(function (t, i) {
                filesHide.push({
                    status: false
                })
            })
        },
        mounted: function () {
            // console.log(this.fileList)
        }
    })
}(jQuery);