<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>


</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
            <e-condition label="留言类型" type="radio" v-model="searchListForm.type"
                         :options="msgTypes" @change="getArticleInCommentList">
            </e-condition>
            <e-condition label="审核状态" type="radio" v-model="searchListForm.auditstatus"
                         :options="checkStates" @change="getArticleInCommentList">
            </e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button size="mini" type="primary" :disabled="multipleSelectionId.length == 0"
                           @click.stop.prevent="batchPass">审核通过
                </el-button>
                <el-button size="mini" type="primary" :disabled="multipleSelectionId.length == 0"
                           @click.stop.prevent="batchNoPass">审核不通过
                </el-button>
                <el-button size="mini" type="primary" :disabled="multipleSelectionId.length == 0"
                           @click.stop.prevent="batchDelete">批量删除
                </el-button>
            </div>
            <div class="search-input">
                <el-checkbox v-model="isAllChecked" :indeterminate="isIndeterminate" @change="handleChangeIsAllChecked" v-if="msgList.length > 0"
                             style="margin: 3px 16px 0 25px;">全选</el-checkbox>
            </div>
        </div>
    </div>

    <div class="table-container mgb-20 cms-comment-guest-list" v-loading="loading">
        <div class="articles-comments-list">
            <div v-for="(msg,index) in msgList" :key="msg.id" class="article-comments">
                <div class="article-comments-header">
                    <el-row>
                        <el-col class="checkbox-category-title" :span="8">
                            <el-checkbox v-model="msg.isChecked" @change="handleChangeIsChecked"></el-checkbox>
                            <span class="category-name">留言类型:</span>
                            <a class="title" href="javascript: void(0);">
                                【{{msg.type | selectedFilter(msgTypesEntries)}}】
                            </a>
                        </el-col>
                        <el-col :span="12">
                            <span style="color:#999;">留言ID：{{msg.id}}</span>
                        </el-col>
                        <el-col :span="4" class="msg-header-edit">
                            <a href="javascript:void(0)" v-if="msg.filess && msg.filess.length > 0" @click.stop.prevent="lookImg(msg.filess)">查看截图</a>
                        </el-col>
                    </el-row>
                </div>
                <div class="article-comments-body control-article-comments">
                    <comment-item :comment="msg" :idx="index"
                                  @change="handleChangeComment"></comment-item>
                </div>
            </div>
            <div v-if="msgList.length < 1" class="text-center" style="padding: 60px 0">
                <span class="empty-color">暂无留言数据</span>
            </div>
        </div>
    </div>

    <div class="text-right mgb-20" v-if="pageCount">
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

    <el-dialog title="查看截图" :visible.sync="dialogVisible" width="60%">
        <el-carousel indicator-position="outside" ref="elCarousel" :height="bannerHeight + 'px'">
            <el-carousel-item v-for="item in bannerImg" :key="item.id">
                <div class="preview-cert-content">
                    <img :src="item.imgUrl" alt="截图" v-auto-img="bannerScale">
                </div>
            </el-carousel-item>
        </el-carousel>
    </el-dialog>


</div>


<script type="text/javascript">

    'use strict';

    Vue.component('control-comment-textarea', {
        template: '<div v-if="visible" class="edit-comment-textarea">\n' +
        '                   <el-input type="textarea" v-model="textCopy" :rows="3" resize="none" :placeholder="placeholder"></el-input><input type="hidden">\n' +
        '                    <div class="edit-control-btns">\n' +
        '                      <el-button type="primary" size="mini" @click.stop.prevent="submitTextarea">提交</el-button>\n' +
        '                       <el-button size="mini" @click.stop.prevent="cancel">取消</el-button>\n' +
        '                      </div>\n' +
        '                 </div>',
        props: {
            visible: Boolean,
            text: String,
            placeholder: String,
            commentId: String,
            validator: {
                type: Function,
                default: function () {
                    return true
                }
            }
        },
        data: function () {
            return {
                textCopy: ''
            }
        },
        watch: {
            visible: function (value) {
                if (value) {
                    this.textCopy = this.text;
                }
            }
        },
        methods: {
            submitTextarea: function () {
                if (this.validator(this.textCopy)) {
                    this.$emit('change', {
                        text: this.textCopy,
                        id: this.commentId
                    })
                }
            },
            cancel: function () {
                this.$emit('update:visible', false)
            }
        }
    });

    Vue.component('comment-item', {
        template: ' <div class="control-article-comment">\n' +
        '                        <div class="cac-body">\n' +
        '                            <div class="el-row">\n' +
        '                                <el-row>\n' +
        '                                    <el-col :span="18">\n' +
        '                                        <div class="comment-content">\n' +
        '                                            <span class="content-tip">留言内容</span>\n' +
        '                                            <div v-show="!comment.showComment" class="content-textarea">\n' +
        '                                                <span class="recommend" v-show="comment.isRecommend == \'1\'">【推荐】</span>{{comment.content}}\n' +
        '                                            </div>\n' +
        '                                            <control-comment-textarea :visible.sync="comment.showComment" :key="comment.id" :comment-id="comment.id" :text="comment.content" @change="changeCommentText"></control-comment-textarea>\n' +
        '                                        </div>\n' +
        '                                        <div v-show="comment.showReply || comment.reContent" class="replay-content">\n' +
        '                                            <span class="content-tip">回复内容</span>\n' +
        '                                            <div v-show="!comment.showReply" class="content-textarea">\n' +
        '                                                {{comment.reContent}}\n' +
        '                                            </div>\n' +
        '                                            <control-comment-textarea :visible.sync="comment.showReply" :key="comment.id" placehoder="回复下什么？？？" :comment-id="comment.id" :text="comment.reContent" @change="changeReplyText"></control-comment-textarea>\n' +
        '                                        </div>\n' +
        '                                    </el-col>\n' +
        '                                    <el-col :span="6" class="text-right">\n' +
        '                                        审核状态：<span class="audit-status"\n' +
        '                                                   :class="{ \'audit-pass\': comment.auditstatus == \'1\' }">{{comment.auditstatus == \'0\' ? \'待审核\' : (comment.auditstatus == \'1\' ? \'审核通过\' : \'审核不通过\')}}</span>\n' +
        '                                    </el-col>\n' +
        '                                </el-row>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                        <div class="cac-footer">\n' +
        '                            <el-row>\n' +
        '                                <el-col :span="12">\n' +
        '                                    <div class="commenter-bar">\n' +
        '                                        <span style="display:inline-block;width:140px;">联系电话：{{comment.phone || \'-\'}}</span>\n' +
        '                                        <span style="display:inline-block;width:140px;">QQ：{{comment.qq || \'-\'}}</span>\n' +
        '                                        <span style="display:inline-block;width:190px;">邮箱：{{comment.email || \'-\'}}</span>\n' +
        '                                    </div>\n' +
        '                                </el-col>\n' +
        '                                <el-col :span="12" class="text-right">\n' +
        '                                           <el-button size="mini" @click.stop.prevent="changeAudit">{{comment.auditstatus== \'1\'? \'审核不通过\' : \'审核通过\'}}</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeShowComment">修改留言</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeShowReply">{{comment.reContent == \'\' || comment.reContent == null ? \'回复\' : \'修改回复\'}}</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeRecommended">{{comment.isRecommend == \'1\'? \'取消推荐\' : \'推荐\'}}</el-button>' +
        '                                    <el-button size="mini" @click.stop.prevent="confirmDelComment">删除</el-button>\n' +
        '                                </el-col>\n' +
        '                            </el-row>\n' +
        '                        </div>\n' +
        '                    </div>',
        props: {
            comment: Object,
            idx: Number
        },
        methods: {
            changeCommentText: function (comment) {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                commentJson.content = comment.text;
                commentJson.inputType = 'comment';
                this.updateComment(commentJson);
            },

            changeReplyText: function (comment) {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                var params = {
                    id: this.comment.id,
                    reContent: comment.text
                };
                commentJson.reContent = comment.text;
                commentJson.inputType = 'reply';
                this.updateRec(commentJson, params);
            },

            changeAudit: function () {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                commentJson.auditstatus = commentJson.auditstatus === '1' ? '2' : '1';
                this.updateComment(commentJson);
            },

            changeShowComment: function () {
                Vue.set(this.comment, 'showComment', true)
            },

            changeShowReply: function () {
                Vue.set(this.comment, 'showReply', true)
            },

            changeRecommended: function () {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                commentJson.isRecommend = commentJson.isRecommend === '1' ? '0' : '1';
                this.updateComment(commentJson);
            },

            confirmDelComment: function (id) {
                var self = this;
                this.$confirm('此操作将删除这条评论，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delComment(id)
                }).catch(function () {

                })
            },

            delComment: function () {
                var self = this;
                this.$axios.post('/cms/cmsGuestbook/ajaxDelete?id=' + this.comment.id).then(function (response) {
                    self.$emit('change', {
                        comment: null,
                        idx: self.idx
                    });
                })
            },

            updateRec: function (comment, params) {
                var self = this;
                this.$axios.post('/cms/cmsGuestbook/ajaxUpdateRec', params).then(function (response) {
                    var nComment = Object.assign(comment, response.data.data || {});
                    if (comment.showComment && comment.inputType === 'comment') {
                        nComment.showComment = false;
                    }
                    if (comment.showReply && comment.inputType === 'reply') {
                        nComment.showReply = false;
                    }
                    self.$emit('change', {
                        comment: nComment,
                        idx: self.idx
                    });
                })
            },

            updateComment: function (comment) {
                var self = this;
                this.$axios.post('/cms/cmsGuestbook/ajaxSave', comment).then(function (response) {
                    var nComment = Object.assign(comment, response.data.data || {});
                    if (comment.showComment && comment.inputType === 'comment') {
                        nComment.showComment = false;
                    }
                    if (comment.showReply && comment.inputType === 'reply') {
                        nComment.showReply = false;
                    }
                    self.$emit('change', {
                        comment: nComment,
                        idx: self.idx
                    });
                })
            }

        },
        created: function () {

        }

    });


    Vue.directive('auto-img', {
        componentUpdated: function (element, binding) {
            var ratio = binding.value;
            element.onload = function () {
                var imgScale = element.naturalWidth / element.naturalHeight;
                element.style.width = imgScale > ratio ? '100%' : 'auto';
                element.style.height = imgScale >= ratio ? 'auto' : '100%'
            }
        },
        unbind: function (element) {
            element.onload = null;
        }
    });


    new Vue({
        el: '#app',
        data: function () {

            return {
                msgList: [],
                multipleSelectionId: [],
                multipleBatchPassList: [],
                multipleBatchNoPassList: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    type: '',
                    auditstatus: ''
                },
                pageCount: 0,
                isAllChecked: false,
                isIndeterminate: false,
                loading: false,
                msgTypes: [{label: '咨询', value: '0'}, {label: '问题反馈', value: '1'}],
                checkStates: [{id: 1, label: '待审核', value: '0'}, {id: 2, label: '审核通过', value: '1'}, {id: 3, label: '审核不通过', value: '2'}],
                dialogVisible: false,
                bannerHeight: 400,
                bannerImg: [],
                bannerScale: 1
            }
        },
        computed: {
            msgTypesEntries: {
                get: function () {
                    return this.getEntries(this.msgTypes)
                }
            }
        },
        methods: {
            lookImg: function (files) {
                var self = this;
                this.bannerImg = [];
                if(!files || files.length == 0){
                    return false;
                }
                files.forEach(function (item) {
                    self.bannerImg.push({id:item.id,imgUrl:item.ftpUrl});
                });
                this.imgCenter();
                this.dialogVisible = true;

            },
            imgCenter: function () {
                var self = this;
                var bannerWidth = 0;
                self.$nextTick(function () {
                    bannerWidth = self.$refs.elCarousel.$el.offsetWidth;
                    self.bannerHeight = bannerWidth * 0.55;
                    self.bannerScale = bannerWidth / self.bannerHeight;
                });
            },
            handleChangeComment: function (data) {
                var comment = data.comment;
                var idx = data.idx;
                this.getArticleInCommentList();
            },

            getArticleInCommentList: function () {
                var self = this;
                this.loading = true;
                this.$axios.post('/cms/cmsGuestbook/ajaxList',Object.toURLSearchParams(self.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.searchListForm.pageNo = data.data.pageNo;
                        self.msgList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },


            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getArticleInCommentList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getArticleInCommentList();
            },

            getMultipleData: function (item) {
                this.multipleSelectionId.push(item.id);
                this.multipleBatchPassList.push({id: item.id, auditstatus: '1'});
                this.multipleBatchNoPassList.push({id: item.id, auditstatus: '2'});
            },
            handleChangeIsChecked: function () {
                var self = this;
                this.multipleSelectionId = [];
                this.multipleBatchPassList = [];
                this.multipleBatchNoPassList = [];
                this.msgList.forEach(function (item) {
                    if (item.isChecked) {
                        self.getMultipleData(item);
                    }
                });
                var len = this.multipleSelectionId.length;
                this.isAllChecked = len == this.msgList.length;
                this.isIndeterminate = len > 0 && len < this.msgList.length;
            },
            handleChangeIsAllChecked: function () {
                var self = this;
                this.isIndeterminate = false;
                if (this.isAllChecked) {
                    this.msgList.forEach(function (item) {
                        item.isChecked = true;
                        self.getMultipleData(item);
                    })
                } else {
                    this.multipleSelectionId = [];
                    this.multipleBatchPassList = [];
                    this.multipleBatchNoPassList = [];
                    this.msgList.forEach(function (item) {
                        item.isChecked = false;
                    })
                }
            },
            batchPass: function () {
                var url = '/cms/cmsGuestbook/ajaxUpdatePLAudit';
                var data = {
                    datas: this.multipleBatchPassList
                };
                this.axiosRequest(url, true, data);
            },
            batchNoPass: function () {
                var url = '/cms/cmsGuestbook/ajaxUpdatePLAudit';
                var data = {
                    datas: this.multipleBatchNoPassList
                };
                this.axiosRequest(url, true, data);
            },
            batchDelete: function () {
                var self = this;
                this.$confirm('是否确定删除', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var url = '/cms/cmsGuestbook/ajaxDeletePL';
                    var data = {
                        datas: self.multipleSelectionId
                    };
                    self.axiosRequest(url, true, data);
                }).catch(function () {

                })
            },
            axiosRequest: function (url, showMsg, data) {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'POST',
                    url: url,
                    data: data
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getArticleInCommentList();
                        self.isAllChecked = false;
                        self.isIndeterminate = false;
                        if (showMsg) {
                            self.$message({
                                message: '操作成功',
                                type: 'success'
                            })
                        }
                    } else {
                        self.$message({
                            message: '操作失败',
                            type: 'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    });
                    self.loading = false;
                })
            }
        },

        beforeMount: function () {
            this.getArticleInCommentList();
        }
    })

</script>
</body>
</html>