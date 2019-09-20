<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>


<div id="app" v-show="pageLoad" style="display: none;" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>我要留言</el-breadcrumb-item>
    </el-breadcrumb>


    <div class="front-leave-msg">
        <el-form :model="leaveMsgForm" ref="leaveMsgForm" :rules="leaveMsgFormRules" size="mini" :disabled="disabled"
                 label-width="100px">
            <el-form-item prop="type" label="留言分类：">
                <el-select v-model="leaveMsgForm.type" placeholder="请选留言类型">
                    <el-option v-for="item in msgTypes" :key="item.value" :label="item.label"
                               :value="item.value"></el-option>
                </el-select>
            </el-form-item>

            <el-form-item prop="content" label="留言内容：">
                <el-input type="textarea" :rows="4" v-model="leaveMsgForm.content" maxlength="200"></el-input>
            </el-form-item>
            <el-form-item prop="files" label="上传截图：">
                <input type="hidden" v-model="leaveMsgForm.files">
                <e-pw-upload-file v-model="leaveMsgForm.files"
                                  action="/ftp/ueditorUpload/uploadTemp?folder=project"
                                  :upload-file-vars="{fileStepEnum: '4000', fileTypeEnum: '4000'}"></e-pw-upload-file>

            </el-form-item>
            <el-row :gutter="20">

                <el-col :span="8">
                    <el-form-item prop="phone" label="联系电话：">
                        <el-input v-model="leaveMsgForm.phone" class="w300"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item prop="qq" label="QQ：">
                        <el-input v-model="leaveMsgForm.qq" class="w300"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="8">
                    <el-form-item prop="email" label="邮箱：">
                        <el-input v-model="leaveMsgForm.email" class="w300"></el-input>
                    </el-form-item>
                </el-col>
            </el-row>


            <el-form-item>
                <el-button type="primary" :disabled="submitIsDisable"
                           @click.stop.prevent="saveForm('leaveMsgForm')">提交
                </el-button>
            </el-form-item>

        </el-form>


        <el-tabs v-model="activeCommentName" class="excellent-comment-box">
            <el-tab-pane label="我的留言" name="first">

                <div class="excellent-comment" v-for="item in msgList" :key="item.id" v-if="msgList.length > 0">
                    <div class="excellent-comment-head">
                        <img :src="item.createUser.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                        <a href="javascript:void (0);" target="_blank" class="excellent-comment-name">{{item.createUser.name}}</a>
                        <span class="excellent-comment-date">{{item.createDate}}</span>
                    </div>
                    <div class="excellent-comment-content">{{item.content}}</div>
                    <div class="excellent-comment-reply-item" v-if="item.reContent">
                        <img src="${ctxImages}/test/admin.jpg" alt="">
                        <span>{{item.reUser && item.reUser.name ? item.reUser.name : ''}} 回复：</span>
                        <span>{{item.reContent ? item.reContent : ''}}</span>
                        <div class="reply-meta">
                            <span>{{item.reDate ? item.reDate : ''}}</span>
                        </div>
                    </div>

                </div>
                <div class="no-content" v-if="msgList.length == 0">
                    暂无留言，快去留言吧！
                </div>


            </el-tab-pane>
        </el-tabs>

        <div class="text-right mgb-20" v-if="pageCount" style="margin-top:20px;">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total, prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>


    </div>


</div>

<script>

        'use strict';


        new Vue({
            el: '#app',
            mixins: [Vue.leaveMsgMixin],
            data: function () {

                return {
                    leaveMsgForm: {
                        name: '',
                        phone: '',
                        email: '',
                        qq: '',
                        msgImg: '',
                        type: '',
                        content: '',
                        files: []
                    },
                    searchListForm: {
                        pageSize: 10,
                        pageNo: 1
                    },
                    pageCount: 0,
                    msgList: [],
                    msgImages: [],
                    msgTypes: [{label: '咨询', value: '0'}, {label: '问题反馈', value: '1'}],
                    submitIsDisable: false,
                    activeCommentName: 'first',
                    dialogImageUrl: '',
                    dialogVisible: false,
                    disabled: false,
                }
            },
            watch: {
                msgImages: function (value) {
                    this.leaveMsgForm.msgImg = value.map(function (item) {
                        return item.ftpUrl
                    });
                }
            },
            methods: {
                getDataList: function () {
                    var self = this;
                    var searchListForm = JSON.parse(JSON.stringify(this.searchListForm));
                    searchListForm.auditstatus = '1';
                    searchListForm['createBy.id'] = this.loginCurUser.id;
                    this.$axios.get('/cms/cmsGuestbook/ajaxList', {
                        params: searchListForm
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status === 1) {
                            self.pageCount = data.data.count;
                            self.searchListForm.pageSize = data.data.pageSize;
                            self.searchListForm.pageNo = data.data.pageNo;
                            self.msgList = data.data.list || [];
                            self.msgImages = [];
                        }
                    })
                },
                saveForm: function (formName) {
                    var self = this;
                    this.$refs[formName].validate(function (valid) {
                        if (valid) {
                            self.submitForm();
                        }
                    });
                },

                submitForm: function () {
                    var self = this;
                    var leaveMsgForm = JSON.parse(JSON.stringify(this.leaveMsgForm));
                    this.disabled = true;
                    this.$axios.post('/cms/cmsGuestbook/ajaxSave', this.getPostData(leaveMsgForm)).then(function (response) {
                        var data = response.data;
                        if (data.status === 1) {
                            self.getDataList();
                            self.$refs.leaveMsgForm.resetFields();
                            self.$alert('提交成功', '提示', {
                                type: 'success'
                            })
                        } else {
                            self.$alert(data.msg, '提示', {
                                type: 'error'
                            })
                        }
                        self.disabled = false;
                    }).catch(function () {
                        self.disabled = false;
                    })
                },


                getPostData: function (data) {
                    var files = data.files;
                    var prefix = 'attachMentEntity';
                    files = files.map(function (item) {
                        return {
                            'fielFtpUrl': item.tempFtpUrl || item.ftpUrl,
                            'fielSize': item.size,
                            'fielTitle': item.name || item.title,
                            'fielType': item.type || item.suffix,
                            'fileStepEnum': '4000',
                            'fileTypeEnum': '4000'
                        }
                    })
                    console.log(files, data.files)
                    data.files = files;
                    return data;
                },

                handlePaginationSizeChange: function (value) {
                    this.searchListForm.pageSize = value;
                    this.getDataList();
                },

                handlePaginationPageChange: function (value) {
                    this.searchListForm.pageNo = value;
                    this.getDataList();
                },
                fileMsgImgSuccess: function (response, file, fileList) {
                    var nfile = Object.assign(file, response);
                    if (response.state === 'SUCCESS') {
                        this.msgImages.push(nfile);
                    } else {
                        this.$message({
                            message: '上传失败',
                            type: 'error'
                        });
                    }
                },

                fileMsgImgError: function (err, file, fileList) {
                    if (err.state == 'error') {
                        this.$message({
                            message: '上传失败',
                            type: 'error'
                        });
                    }
                },
                handlePictureCardPreview: function (file) {
                    this.dialogImageUrl = file.url;
                    this.dialogVisible = true;
                },
                handleRemove: function (file, fileList) {
                    this.msgImages = fileList;
                }
            },
            created: function () {
                var self = this;
                this.getDataList();
                this.getUserIsCompleted().then(function (response) {
                    var data = response.data;
                    var loginCurUser = self.loginCurUser;
                    if (data.status !== 1) {
                        self.$msgbox({
                            type: 'error',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: data.msg
                        }).then(function () {
                            if (loginCurUser.userType === '1') {
                                location.href = self.ctxFront + '/sys/frontStudentExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';
                            } else {
                                location.href = self.ctxFront + '/sys/frontTeacherExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';
                            }
                        }).catch(function () {
                        });
                    }
                })
            }
        });


</script>


</body>
</html>