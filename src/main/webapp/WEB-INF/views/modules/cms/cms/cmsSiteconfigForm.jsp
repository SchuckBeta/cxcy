<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .site-province-size{
            width: 283px;
            height: 150px;
        }

    </style>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>

    <el-form :model="cmsSiteconfig" :rules="webSiteConfigRules" ref="cmsSiteconfig" size="mini" class="site-config"
             label-width="130px">

        <div class="table-container mgb-20">
            <div class="articles-comments-list">

                <div class="article-comments">
                    <div class="article-comments-header">
                        <span class="font-bold">站点主题</span>
                    </div>
                    <div class="article-comments-body control-article-comments">
                        <div class="control-article-comment">
                            <div class="cac-body">
                                <el-form-item label="站点名称：">
                                    <p class="el-form-item-content_static">{{cmsSiteconfig.siteName}}</p>
                                </el-form-item>

                                <el-form-item prop="theme" label="主题：">

                                    <el-radio-group v-model="cmsSiteconfig.theme">
                                        <el-radio v-for="(item,index) in themeList" :key="index" :label="item.value" :disabled="item.value != '1'" style="position:relative;">
                                            <span>{{item.label}}</span>
                                            <div class="theme-mark">
                                                <div class="theme-color" :style="{background:item.color}"></div>
                                                <span class="theme-close" v-if="item.value != '1'">
                                                    暂未开放
                                                </span>
                                            </div>
                                        </el-radio>
                                    </el-radio-group>

                                </el-form-item>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="article-comments" v-if="cmsSiteconfig.isProvince == '1'">
                    <div class="article-comments-header">
                         <span class="font-bold"> 省厅设置</span>
                    </div>
                    <div class="article-comments-body control-article-comments">
                        <div class="control-article-comment">
                            <div class="cac-body">
                                <el-form-item prop="headText" label="头部标题：">
                                    <el-input v-model="cmsSiteconfig.headText" class="w300"></el-input>
                                </el-form-item>
                                <el-form-item prop="backgroundUrl" label="背景图片：" class="common-upload-one-img">
                                    <input type="hidden" v-model="cmsSiteconfig.backgroundUrl">

                                    <div class="upload-box-border site-province-size">
                                        <el-upload
                                                class="avatar-uploader"
                                                action="${ctx}/ftp/ueditorUpload/uploadTemp"
                                                :show-file-list="false"
                                                :on-success="fileBackgroundImgsSuccess"
                                                :on-error="fileLogoError"
                                                name="upfile"
                                                accept="image/jpg, image/jpeg, image/png">
                                            <img v-for="item in backgroundImgs" :key="item.uid"
                                                 :src="item.ftpUrl | ftpHttpFilter(ftpHttp)">
                                            <i v-if="backgroundImgs.length == 0"
                                               class="el-icon-plus avatar-uploader-icon"></i>
                                        </el-upload>
                                    </div>
                                    <div class="img-tip">
                                        仅支持上传jpg/png文件，建议图片大小：1920*1020（像素）
                                    </div>
                                </el-form-item>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="article-comments" v-if="cmsSiteconfig.isProvince != '1'">
                    <div class="article-comments-header">
                        <span class="font-bold"> logo设置</span>
                    </div>
                    <div class="article-comments-body control-article-comments">
                        <div class="control-article-comment">
                            <div class="cac-body">
                                <el-row :gutter="20">
                                    <el-col :span="12">
                                        <el-form-item prop="logoLeft" label="logo（左）：" class="common-upload-one-img">
                                            <input type="hidden" v-model="cmsSiteconfig.logoLeft">

                                            <div class="upload-box-border site-logo-left-size">
                                                <el-upload
                                                        class="avatar-uploader"
                                                        action="${ctx}/ftp/ueditorUpload/uploadTemp"
                                                        :show-file-list="false"
                                                        :on-success="fileLogoLeftSuccess"
                                                        :on-error="fileLogoError"
                                                        name="upfile"
                                                        accept="image/jpg, image/jpeg, image/png">
                                                    <img v-for="item in fileLogoLeft" :key="item.uid"
                                                         :src="item.ftpUrl | ftpHttpFilter(ftpHttp)">
                                                    <i v-if="fileLogoLeft.length == 0"
                                                       class="el-icon-plus avatar-uploader-icon"></i>
                                                </el-upload>
                                                <div class="arrow-block-delete" v-if="fileLogoLeft.length > 0">
                                                    <i class="el-icon-delete" @click.sotp.prevent="fileLogoLeft = []"></i>
                                                </div>
                                            </div>
                                            <div class="img-tip">
                                                仅支持上传jpg/png文件，建议logo大小：76*76（像素）
                                            </div>


                                        </el-form-item>
                                    </el-col>
                                    <el-col :span="12">
                                        <el-form-item prop="logoRight" label="logo（右）：" class="common-upload-one-img">
                                            <input type="hidden" v-model="cmsSiteconfig.logoRight">

                                            <div class="upload-box-border site-logo-right-size">
                                                <el-upload
                                                        class="avatar-uploader"
                                                        action="${ctx}/ftp/ueditorUpload/uploadTemp"
                                                        :show-file-list="false"
                                                        :on-success="fileLogoRightSuccess"
                                                        :on-error="fileLogoError"
                                                        name="upfile"
                                                        accept="image/jpg, image/jpeg, image/png">
                                                    <img v-for="item in fileLogoRight" :key="item.uid"
                                                         :src="item.ftpUrl | ftpHttpFilter(ftpHttp)">
                                                    <i v-if="fileLogoRight.length == 0"
                                                       class="el-icon-plus avatar-uploader-icon"></i>
                                                </el-upload>
                                                <div class="arrow-block-delete" v-if="fileLogoRight.length > 0">
                                                    <i class="el-icon-delete" @click.sotp.prevent="deleteLogoRight"></i>
                                                </div>
                                            </div>
                                            <div class="img-tip">
                                                仅支持上传jpg/png文件，建议logo大小：159*76（像素），与产品默认logo切换显示
                                            </div>

                                        </el-form-item>
                                    </el-col>
                                </el-row>
                                <el-form-item label="logo预览：">
                                    <div class="el-form-content_static">
                                        <img v-show="fileLogoLeft.length > 0" v-for="item in fileLogoLeft"
                                             :key="item.uid"
                                             :src="item.ftpUrl | ftpHttpFilter(ftpHttp)">
                                        <div class="logo-height">
                                            <transition-group name="logo-fade">
                                                <img v-for="(item,index) in fileLogoRightPreview" :key="item.uid"
                                                     v-show="currentIndex == index" :src="item.url">
                                            </transition-group>
                                        </div>
                                    </div>
                                </el-form-item>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="article-comments">
                    <div class="article-comments-header">
                         <span class="font-bold"> banner设置</span>   <span class="site-config-header-tip">仅支持上传jpg/png（可拖拽调整顺序），建议banner大小：1920*450（像素）</span>
                    </div>
                    <div class="article-comments-body control-article-comments">
                        <div class="control-article-comment">
                            <div class="cac-body">
                                <el-form-item prop="bannerimage" label="Banner：">
                                    <input type="hidden" v-model="cmsSiteconfig.bannerimage">

                                    <div class="el-upload-list el-upload-list--picture-card common-upload-many-img">
                                        <draggable v-model="bannerImages"
                                                   class="draggable-tag"
                                                   @start="dragBanner=true"
                                                   @end="dragBanner=false">
                                            <div tabindex="0" class="el-upload-list__item is-success site-banner-size"
                                                v-for="item in bannerImages" :key="item.uid">
                                                <img :src="item.ftpUrl | ftpHttpFilter(ftpHttp)"
                                                     alt="" class="el-upload-list__item-thumbnail">
                                                <label class="arrow-block-delete">
                                                    <i class="el-icon-delete" @click.sotp.prevent="deleteImg(item,bannerImages)"></i>
                                                </label>
                                            </div>
                                        </draggable>
                                        <el-upload
                                                class="avatar-uploader site-banner-size"
                                                action="${ctx}/ftp/ueditorUpload/uploadTemp"
                                                :show-file-list="false"
                                                :file-list="bannerImages"
                                                name="upfile"
                                                :before-upload="fileLogoBannerBeforeUpload"
                                                :on-success="fileLogoBannerSuccess"
                                                :on-error="fileLogoError"
                                                accept="image/jpg, image/jpeg, image/png">
                                            <i class="el-icon-plus avatar-uploader-icon"></i>
                                        </el-upload>
                                    </div>

                                </el-form-item>


                            </div>
                        </div>
                    </div>
                </div>


                <div class="article-comments">
                    <div class="article-comments-header">
                         <span class="font-bold"> 动态通知设置</span>
                    </div>
                    <div class="article-comments-body control-article-comments">
                        <div class="control-article-comment">
                            <div class="cac-body">
                                <el-form-item prop="dynamicImg" label="轮播图片：">
                                    <input type="hidden" v-model="cmsSiteconfig.dynamicImg">


                                    <div class="el-upload-list el-upload-list--picture-card common-upload-many-img">
                                        <div tabindex="0" class="el-upload-list__item is-success site-dynamic-size"
                                            v-for="item in dynamicImages" :key="item.uid">
                                            <img :src="item.ftpUrl | ftpHttpFilter(ftpHttp)"
                                                 alt="" class="el-upload-list__item-thumbnail">
                                            <label class="arrow-block-delete">
                                                <i class="el-icon-delete" @click.sotp.prevent="handleRemoveDynamicImg(item,dynamicImages)"></i>
                                            </label>
                                        </div>
                                        <el-upload
                                                class="avatar-uploader site-dynamic-size"
                                                action="${ctx}/ftp/ueditorUpload/uploadTemp"
                                                :show-file-list="false"
                                                name="upfile"
                                                :on-success="fileDynamicSuccess"
                                                :on-error="fileLogoError"
                                                :before-upload="handleBeforeUploadDyn"
                                                accept="image/jpg, image/jpeg, image/png">
                                            <i class="el-icon-plus avatar-uploader-icon"></i>
                                        </el-upload>
                                        <div class="el-upload__tip" slot="tip">
                                            仅支持上传jpg/png文件，建议图片大小：395*255（像素）
                                        </div>
                                    </div>

                                </el-form-item>

                                <el-form-item prop="msgImg" label="封面：" class="common-upload-one-img">
                                    <input type="hidden" v-model="cmsSiteconfig.msgImg">

                                    <div class="upload-box-border site-cover-size">
                                        <el-upload
                                                class="avatar-uploader"
                                                action="${ctx}/ftp/ueditorUpload/uploadTemp"
                                                :show-file-list="false"
                                                :on-success="fileMsgSuccess"
                                                :on-error="fileLogoError"
                                                name="upfile"
                                                accept="image/jpg, image/jpeg, image/png">
                                            <img v-for="item in msgImages" :key="item.uid"
                                                 :src="item.ftpUrl | ftpHttpFilter(ftpHttp)">
                                            <i v-if="msgImages.length == 0"
                                               class="el-icon-plus avatar-uploader-icon"></i>
                                        </el-upload>
                                    </div>
                                    <div class="img-tip">
                                        仅支持上传jpg/png文件，建议图片大小：340*125（像素）
                                    </div>


                                </el-form-item>
                            </div>
                        </div>
                    </div>
                </div>


            </div>
        </div>


        <el-form-item class="text-center">
            <el-button type="primary" :disabled="submitIsDisable"
                       @click.stop.prevent="saveWebSiteConfig('cmsSiteconfig')">保存
            </el-button>
        </el-form-item>
        </el-form-item>
    </el-form>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                cmsSiteconfig: {
                    id: '',
                    siteId: '',
                    siteName: '',
                    theme: '1',
                    logoLeft: '',
                    logoRight: '',
                    bannerimage: '',
                    dynamicImg: '',
                    msgImg: '',
                    isProvince: '',
                    headText: '',
                    backgroundUrl: ''
                },
                cmsSiteconfigList: {},
                themeList: [{label: '噢易红', value: '1', color:'#E9432D'}, {label: '天空蓝', value: '2', color:'#58ADED'}, {label: '森林绿',value: '3', color:'#3C7236'}],
                fileLogoLeft: [],
                fileLogoRight: [],
                bannerImages: [],
                dynamicImages: [],
                msgImages: [],
                backgroundImgs: [],
                fileLogoRightPreview: [],
                dragBanner: false,
                dragDynamic: false,
                webSiteConfigRules: {
                    theme: [
                        {required: true, message: '请选择主题', trigger: 'blur'}
                    ],
                    headText: [
                        {required: true, message: '请输入头部标题', trigger: 'blur'},
                        {max: 20, message: '请输入不大于20字的标题', trigger: 'blur'}
                    ],
                    backgroundUrl: [
                        {required: true, message: '请上传背景图片', trigger: 'blur'}
                    ],
                    bannerimage: [
                        {required: true, message: '请上传banner', trigger: 'blur'}
                    ],
                    dynamicImg: [
                        {required: true, message: '请上传轮播图片', trigger: 'blur'}
                    ],
                    msgImg: [
                        {required: true, message: '请上传封面', trigger: 'blur'}
                    ]
                },
                currentIndex: 0,
                timer: null,
                submitIsDisable: false
            }
        },


        watch: {
            fileLogoLeft: function (value) {
                this.cmsSiteconfig.logoLeft = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            fileLogoRight: function (value) {
                this.cmsSiteconfig.logoRight = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            bannerImages: function (value) {
                this.cmsSiteconfig.bannerimage = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            dynamicImages: function (value) {
                this.cmsSiteconfig.dynamicImg = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            msgImages: function (value) {
                this.cmsSiteconfig.msgImg = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            backgroundImgs: function (value) {
                this.cmsSiteconfig.backgroundUrl = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            fileLogoRightPreview: function (value) {
                if (value.length > 1) {
                    this.startTimer();
                }
            }

        },

        methods: {

            searchCondition: function () {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/cms/cmsSiteconfig/siteConfigForm'
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        Object.assign(self.cmsSiteconfigList, data.data);
                        var list = self.cmsSiteconfigList;
                        for (var key in list) {
                            if (Object.prototype.toString.call(list[key]) == '[object Array]') {
                                var picList = list[key];
                                for (var i = 0; i < picList.length; i++) {
                                    for (var k in picList[i]) {
                                        self.cmsSiteconfig[k] = picList[i][k];
                                    }
                                }
                            } else {
                                self.cmsSiteconfig[key] = list[key];
                            }
                        }
                        self.setFileLogoLeft(self.cmsSiteconfig.logoLeft);
                        self.setFileLogoRight(self.cmsSiteconfig.logoRight);
                        self.setBannerImages(self.cmsSiteconfig.bannerimage);
                        self.setDynamicImages(self.cmsSiteconfig.dynamicImg);
                        self.setMsgImages(self.cmsSiteconfig.msgImg);
                        self.setBackgroundImages(self.cmsSiteconfig.backgroundUrl);
                        self.fileLogoRightPreview = self.getFileLogoRightPreview();
                    }
                })
            },

            setFileLogoLeft: function (value) {
                if (!value) {
                    return;
                }
                this.fileLogoLeft.push({
                    ftpUrl: value
                });
            },

            setFileLogoRight: function (value) {
                if (!value) {
                    return;
                }
                this.fileLogoRight.push({
                    ftpUrl: value
                });
            },

            setBannerImages: function (value) {
                if (!value) {
                    return;
                }
                var arr = value.split(',');
                for (var i = 0; i < arr.length; i++) {
                    this.bannerImages.push({ftpUrl: arr[i]});
                }
            },
            setDynamicImages: function (value) {
                if (!value) {
                    return;
                }
                var arr = value.split(',');
                for (var i = 0; i < arr.length; i++) {
                    this.dynamicImages.push({ftpUrl: arr[i], url: this.addFtpHttp(arr[i])});
                }
            },
            setMsgImages: function (value) {
                if (!value) {
                    return;
                }
                this.msgImages.push({
                    ftpUrl: value
                });
            },
            setBackgroundImages: function (value) {
                if (!value) {
                    return;
                }
                this.backgroundImgs.push({
                    ftpUrl: value
                });
            },

            fileLogoError: function (err, file, fileList) {
                if (err.state == 'error') {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            fileLogoLeftSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.fileLogoLeft = fileList.slice(-1);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            fileLogoRightSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.fileLogoRight = fileList.slice(-1);
                    this.currentIndex = 0;
                    this.fileLogoRightPreview = this.getFileLogoRightPreview(response.ftpUrl);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },
            fileLogoBannerBeforeUpload: function (file) {
                if(this.bannerImages.length >= 10){
                    this.$message({
                        message:'最多上传10张图片！',
                        type:'warning'
                    });
                    return false;
                }
            },
            deleteImg: function (file, fileList) {
                var fileIndex;
                if(this.bannerImages.length === 1){
                    this.$message({
                        message:'最少需要1张Banner图！',
                        type:'warning'
                    });
                    return false;
                }
                fileIndex = this.getFileListIndex(file, fileList);
                fileList.splice(fileIndex, 1);
            },

            fileLogoBannerSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.bannerImages.push(nfile);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            handleBeforeUploadDyn: function () {
                if(this.dynamicImages.length === 5){
                    this.$message({
                        message:'最多上传5张图片！',
                        type:'warning'
                    });
                    return false;
                }
            },

            fileDynamicSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.dynamicImages.push(nfile);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },
            fileMsgSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.msgImages = fileList.slice(-1);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },
            fileBackgroundImgsSuccess: function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.backgroundImgs = fileList.slice(-1);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },

            deleteLogoRight:function () {
                this.fileLogoRight = [];
                this.currentIndex = 0;
                this.cmsSiteconfig.logoRight = '';
                this.fileLogoRightPreview = this.getFileLogoRightPreview('');
            },

            handleRemoveDynamicImg: function (file, fileList) {
                var fileIndex;
                if(this.dynamicImages.length === 1){
                    this.$message({
                        message:'最少需要1张轮播图！',
                        type:'warning'
                    });
                    return false;
                }
                fileIndex = this.getFileListIndex(file, fileList);
                fileList.splice(fileIndex, 1);
            },


            getFileListIndex: function (file, fileList) {
                for (var i = 0; i < fileList.length; i++) {
                    if (fileList[i].uid) {
                        if (file.uid === fileList[i].uid) {
                            return i;
                        }
                    } else {
                        if (file.ftpUrl === fileList[i].ftpUrl) {
                            return i;
                        }
                    }
                }
                return false;
            },

            saveWebSiteConfig: function (formName) {
                var self = this;
                this.submitIsDisable = true;
                var list = self.cmsSiteconfig;
                var types = ['logoLeft','logoRight','bannerimage','dynamicImg','msgImg','backgroundUrl'];
                this.cmsSiteconfigList = {};
                this.cmsSiteconfigList.picList = [];
                for(var key in list){
                    if(types.indexOf(key) > -1){
                        self.cmsSiteconfigList.picList.push({type:key,url:list[key]});
                    }else{
                        self.cmsSiteconfigList[key] = list[key];
                    }
                }
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.$axios({
                            method: 'POST',
                            url: '/cms/cmsSiteconfig/siteConfigSave',
                            data: self.cmsSiteconfigList
                        }).then(function (response) {
                            var data = response.data;
                            self.submitIsDisable = false;
                            self.$message({
                                message: data.ret == '1' ? '保存成功' : '保存失败',
                                type: data.ret == '1' ? 'success' : 'error'
                            });
                        }).catch(function () {
                            self.submitIsDisable = false;
                        })
                    } else {
                        self.submitIsDisable = false;
                    }
                });
            },

            getFileLogoRightPreview: function (ftpUrl) {
                var logoRight = this.cmsSiteconfig.logoRight;
                var logos = [{
                    uid: new Date().getTime(),
                    url: '/images/s-brandx161.png'
                }];
                var url = ftpUrl || logoRight;
                if (url) {
                    logos.push({
                        uid: new Date().getTime(),
                        url: this.addFtpHttp(url)
                    })
                }
                return logos;
            },

            startTimer: function () {
                var self = this;
                this.timer && clearInterval(this.timer);
                this.timer = setInterval(function () {
                    var len = self.fileLogoRightPreview.length;
                    if (self.currentIndex < len - 1) {
                        self.currentIndex += 1;
                    } else {
                        self.currentIndex = 0;
                    }
                }, 3000)
            }

        },
        created: function () {
            this.searchCondition();

        }
    })

</script>



</body>
</html>