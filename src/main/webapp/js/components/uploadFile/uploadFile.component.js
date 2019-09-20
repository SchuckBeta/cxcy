/**
 * Created by Administrator on 2018/8/8.
 */


Vue.component('e-upload-file', {
    template: '<div> <div class="upload-file-list"> <div v-for="file in fileList" class="ufl-item"> ' +
    '<a class="ufl-item-bd" href="javascript:void(0);" @click.stop.prevent="goToViewUrl(file)"><span class="ufl-item-pic"><img :src="file.suffix || file.type | fileSuffixFilter"> </span>{{file.name}}</a> ' +
    '<el-button type="text" size="mini" @click.stop.prevent="downUploadFile(file)"><i class="btn-ufl-icon btn-down-icon"></i></el-button> ' +
    '<el-button type="text" size="mini" @click.stop.prevent="deleteUploadFile(file, fileList)"><i class="btn-ufl-icon btn-delete-icon"></i></el-button> ' +
    '<input v-if="!file.remotePath" v-for="item in nInputsHidden" type="hidden" :name="item"  :value="file[item]"> ' +
    '<div class="ufl-layer" v-if="(file.status != \'success\'&& file.uid) && !file.id"> <img :src="ctxImages + \'/loading.gif\'"> </div> </div> </div> ' +
    '<el-upload class="upload-demo" :disabled="disabled" :action="frontOrAdmin + action" ' +
    'name="upfile" :show-file-list = "false" :before-upload="handleBeforeUploadFile" :on-change="handleChangeUploadFile" :on-success="handleSuccessUploadFile"' +
    ':on-error="handleErrorUploadFile"' +
    ':on-progress="handleProgressUploadFile"' +
    ':file-list="fileList"> ' +
    '<el-button size="mini" type="primary" :disabled="uploadFileLoading">点击上传</el-button> ' +
    '<div slot="tip" class="el-upload__tip" v-html="tip"></div> ' +
    '</el-upload> </div>',
    model: {
        prop: 'fileList',
        event: 'change'
    },
    props: {
        fileList: Array,
        action: String,
        tip: {
            type: String,
            default: function () {
                return '请上传word、pdf、excel、txt、rar、ppt和图片类型文件'
            }
        },
        uploadFileVars: {
            type: Object,
            required: true
        },
        deleteFileUrl: {
            type: String,
            default: '/ftp/ueditorUpload/delFile'
        },
        disabled: Boolean
    },
    data: function () {
        return {
            uploadFileExtensions: 'gif,jpg,jpeg,bmp,png,xls,xlsx,doc,docx,ppt,pptx,txt,zip,rar,pdf',
            uploadFileLoading: false,
            inputsHidden: [['fielSize', 'size'], ['fielTitle', 'title'], ['fielType', 'type'], ['fielFtpUrl', 'ftpUrl'], ['fileStepEnum'], ['fileTypeEnum'], ['gnodeId']],
            // frontOrAdmin: '/f',
            // xhrErrorMsg: '请求失败'
        }
    },
    computed: {
        nInputsHidden: {
            get: function () {
                var arr = [];
                var self = this;
                this.inputsHidden.forEach(function (item) {
                    var key = 'attachMentEntity.' + item[0];
                    var fileInfoPrefix = self.uploadFileVars.fileInfoPrefix;
                    if (fileInfoPrefix) {
                        key = fileInfoPrefix + key;
                    }
                    arr.push(key);
                });
                return arr;
            }
        }
    },
    methods: {

        getFileSuffix: function (name) {
            var reg = /(?!\.)[a-zA-Z]+$/;
            if (reg.test(name)) {
                return name.match(reg)[0]
            }
            return false;
        },

        handleBeforeUploadFile: function (file) {
            var suffix, self = this;
            suffix = this.getFileSuffix(file.name);
            if (file.size === 0) {
                this.$message({
                    type: 'error',
                    message: '请不要上传0KB的空文件'
                });
                return false;
            }
            if (file.size > this.webMaxUpFileSize) {
                this.$message({
                    type: 'error',
                    message: '超过文件最大限制' + (this.webMaxUpFileSize / 1024 / 1024) + 'M，不允许添加'
                });
                return false;
            }
            if (this.uploadFileExtensions.indexOf(suffix) === -1) {
                this.$message({
                    type: 'error',
                    message: '请上传word、pdf、excel、txt、rar、ppt和图片类型文件'
                });
                return false;
            }
            if (this.hasSameFile(file)) {
                var sameFile = this.getSameFile(file);
                if (sameFile) {
                    return new Promise(function (resolve, reject) {
                        self.$confirm(file.name + '，与已上传的文件存在同名，继续上传会覆盖原文件， 是否继续？', '提示', {
                            confirmButtonText: '确定',
                            cancelButtonText: '取消',
                            type: 'warning'
                        }).then(function () {
                            resolve(file);
                        }).catch(function () {
                            self.removeUploadFile(file, self.fileList);
                            reject(file);
                        })
                    })
                } else {
                    this.$message({
                        type: 'error',
                        message: '已经上传过该文件了'
                    });
                    return false;
                }
            }
            return true;
        },


        hasSameFile: function (file) {
            var files = this.fileList;
            for (var i = 0; i < files.length; i++) {
                var fileItem = files[i];
                var type = fileItem.remotePath ? fileItem.suffix : fileItem.type;
                if (type === this.getFileSuffix(file.name) && fileItem.name === file.name) {
                    return true;
                }
            }
            return false;
        },

        getSameFile: function (file) {
            var files = this.fileList;
            for (var i = 0; i < files.length; i++) {
                var fileItem = files[i];
                var type = fileItem.remotePath ? fileItem.suffix : fileItem.type;
                if (type === this.getFileSuffix(file.name) && fileItem.name === file.name) {
                    return fileItem;
                }
            }
            return false;
        },

        handleChangeUploadFile: function (file, fileList) {
            if (file.status === 'ready') {
                this.$emit('change', fileList)
            }
        },

        downUploadFile: function (file) {
            console.log(this.frontOrAdmin + "/ftp/ueditorUpload/downFile?url=" + file.ftpUrl + "&fileName=" + encodeURI(encodeURI(file.name)))
            location.href = this.frontOrAdmin + "/ftp/ueditorUpload/downFile?url=" + file.ftpUrl + "&fileName=" + encodeURI(encodeURI(file.name));
        },


        handleProgressUploadFile: function (event, file, fileList) {
            this.uploadFileLoading = true;

        },

        goToViewUrl: function (file) {
            if (!file.viewUrl) {
                this.$message({
                    type: 'error',
                    message: '暂不支持预览，请下载查阅'
                })
                return;
            }
            var viewUrl = file.viewUrl.indexOf(this.ftpHttp) > -1 ? file.viewUrl : (this.ftpHttp + file.viewUrl.replace('/tool', ''))
            window.open(viewUrl)
        },

        handleSuccessUploadFile: function (response, file, fileList) {
            var raw = file.raw;
            var suffix = this.getFileSuffix(file.name);
            var self = this;
            var sameFile;
            if (response.state === 'SUCCESS') {
                this.$message({
                    type: 'success',
                    message: raw.name + ', 上传成功'
                });
                if (new RegExp(response.type, 'i').test('gif,jpg,jpeg,bmp,png,pdf')) {
                    Vue.set(file, 'viewUrl', response.ftpUrl)
                }

                this.nInputsHidden.forEach(function (item, i) {
                    var value = self.inputsHidden[i][1] ? response[self.inputsHidden[i][1]] : self.uploadFileVars[self.inputsHidden[i][0]];
                    Vue.set(file, item, value);
                })
                sameFile = this.getSameFile(file)
                if(sameFile){
                    this.removeUploadFile(sameFile, fileList)
                }
                file.tempFtpUrl = response.ftpUrl;
                file.tempUrl = response.url;
                file.suffix = response.type;
                this.updateUploadFile(Object.assign({}, file, response), fileList)

            } else {
                this.$message({
                    type: 'error',
                    message: raw.name + ', 上传失败'
                });
                this.removeUploadFile(file, fileList)
            }
            this.uploadFileLoading = false;
        },
        handleErrorUploadFile: function (err, file, fileList) {
            this.$message({
                type: 'error',
                message: self.xhrErrorMsg
            });
            this.removeUploadFile(file, fileList);
            this.uploadFileLoading = false;
        },

        updateUploadFile: function (file, fileList) {
            for (var i = 0; i < fileList.length; i++) {
                if (file.uid === fileList[i].uid) {
                    fileList.splice(i, 1, file);
                    break;
                }
            }
            this.$emit('change', fileList);

        },

        //移除不合格的文件
        removeUploadFile: function (file, fileList) {
            for (var i = 0; i < fileList.length; i++) {
                if (file.uid === fileList[i].uid) {
                    fileList.splice(i, 1);
                    break;
                }
            }
            this.$emit('change', fileList)
        },

        deleteUploadFile: function (file, fileList) {
            //刚上传的文件
            var self = this;
            if (!file.remotePath) {
                this.removeUploadFile(file, fileList)
            } else {
                this.$confirm('是否删除' + file.name + '？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.deleteUploadFileXhr(file, fileList)
                }).catch(function () {

                })
            }
        },
        deleteUploadFileXhr: function (file, fileList) {
            var self = this;
            this.$axios({
                method: 'post',
                url: this.deleteFileUrl,
                params: {
                    id: file.id,
                    url: file.ftpUrl
                }
            }).then(function (response) {
                self.$message({
                    type: 'success',
                    message: '删除成功'
                });
                self.removeUploadFile(file, fileList)
            }).catch(function (error) {
                self.$message({
                    type: 'error',
                    message: self.xhrErrorMsg
                })
            })
        }
    }

})