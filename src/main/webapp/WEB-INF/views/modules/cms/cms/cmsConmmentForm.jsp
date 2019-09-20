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
    <edit-bar second-name="全部评论"></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
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
        </div>
    </el-form>


    <div class="table-container mgb-20 cms-comment-form" v-loading="loading">
        <div class="articles-comments-list">
            <div v-for="article in articleList" :key="article.id" class="article-comments">
                <div class="article-comments-header">
                    <el-row>
                        <el-col style="width:30px;">
                            <el-checkbox v-model="isAllChecked" :indeterminate="isIndeterminate"
                                         @change="handleChangeIsAllChecked"></el-checkbox>
                        </el-col>
                        <el-col class="checkbox-category-title" :span="8">
                            <span class="category-name">{{article.cmsCategory ? '['+article.cmsCategory.name+']': ''}}</span>
                            <el-tooltip class="item" effect="dark" popper-class="white" :content="article.title" placement="bottom">
                                <span class="over-flow-tooltip project-office-tooltip" style="height:50px;max-width:60%;">
                                    <a class="title" href="javascript: void(0);">
                                        {{article.title}}
                                    </a>
                                </span>
                            </el-tooltip>
                        </el-col>
                        <el-col :span="2">
                            <span style="color:#999;">文章ID：{{article.id}}</span>
                        </el-col>
                    </el-row>
                </div>
                <div class="article-comments-body control-article-comments">
                    <comment-item v-for="(comment, index) in articleComments[article.id]" :key="comment.id"
                                  :comment="comment" :article-id="article.id" :idx="index"
                                  @change="handleChangeComment"></comment-item>
                </div>
            </div>
            <div v-if="commentList.length < 1" class="text-center" style="padding: 60px 0">
                <span class="empty-color">暂无评论数据</span>
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

</div>


<script type="text/javascript">

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
    })

    Vue.component('comment-item', {
        template: ' <div class="control-article-comment">\n' +
        '                        <div class="cac-body">\n' +
        '                            <div class="el-row">\n' +
        '                                <el-row>\n' +
        '                                    <el-col style="width:30px;margin-top:3px;">\n' +
        '                                    	 <el-checkbox v-model="comment.isChecked" @change="handleChangeIsChecked(comment)"></el-checkbox>\n' +
        '                                    </el-col>\n' +
        '                                    <el-col :span="18">\n' +
        '                                        <div class="comment-content">\n' +
        '                                            <span class="content-tip">评论内容</span>\n' +
        '                                            <div v-show="!comment.showComment" class="content-textarea">\n' +
        '                                                <span class="recommend" v-show="comment.isRecommend == \'1\'">【推荐】</span>{{comment.content}}\n' +
        '                                            </div>\n' +
        '                                            <control-comment-textarea :visible.sync="comment.showComment" :key="comment.id" :comment-id="comment.id" :text="comment.content" @change="changeCommentText"></control-comment-textarea>\n' +
        '                                        </div>\n' +
        '                                        <div v-show="comment.showReply || comment.reply" class="replay-content">\n' +
        '                                            <span class="content-tip">回复内容</span>\n' +
        '                                            <div v-show="!comment.showReply" class="content-textarea">\n' +
        '                                                {{comment.reply}}\n' +
        '                                            </div>\n' +
        '                                            <control-comment-textarea :visible.sync="comment.showReply" :key="comment.id" placehoder="回复下什么？？？" :comment-id="comment.id" :text="comment.reply" @change="changeReplyText"></control-comment-textarea>\n' +
        '                                        </div>\n' +
        '                                    </el-col>\n' +
        '                                    <el-col :span="6" class="text-right" style="margin-left:-30px;">\n' +
        '                                        审核状态：<span class="audit-status"\n' +
        '                                                   :class="{ \'audit-pass\': comment.auditstatus == \'1\' }">{{comment.auditstatus == \'0\' ? \'待审核\' : (comment.auditstatus == \'1\' ? \'审核通过\' : \'审核不通过\')}}</span>\n' +
        '                                    </el-col>\n' +
        '                                </el-row>\n' +
        '                            </div>\n' +
        '                        </div>\n' +
        '                        <div class="cac-footer">\n' +
        '                            <el-row>\n' +
        '                                <el-col :span="12" style="margin-left:12px;">\n' +
        '                                    <div class="commenter-bar">\n' +
        '                                        <span v-if="comment.user">评论者：{{comment.user.name}}</span>\n' +
        '                                        <span>评论时间：{{comment.createDate}}</span>\n' +
        '                                    </div>\n' +
        '                                </el-col>\n' +
        '                                <el-col :span="12" class="text-right" style="margin-left:-12px;">\n' +
        '                                           <el-button size="mini" @click.stop.prevent="changeAudit">{{comment.auditstatus== \'1\'? \'审核不通过\' : \'审核通过\'}}</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeShowComment">修改评论</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeShowReply">{{comment.reply == \'\' || comment.reply == null ? \'回复\' : \'修改回复\'}}</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeRecommended">{{comment.isRecommend == \'1\'? \'取消推荐\' : \'推荐\'}}</el-button>' +
        '                                    <el-button size="mini" @click.stop.prevent="confirmDelComment">删除</el-button>\n' +
        '                                </el-col>\n' +
        '                            </el-row>\n' +
        '                        </div>\n' +
        '                    </div>\n',
        props: {
            comment: Object,
            articleId: String,
            idx: Number
        },
        methods: {
            changeCommentText: function (comment) {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                var url = '/cms/cmsConmment/ajaxUpdateContent';
                var params = {
                    id: this.comment.id,
                    content: comment.text
                };
                commentJson.content = comment.text;
                commentJson.inputType = 'comment';
                this.updateComment(commentJson, url, params);
            },

            changeReplyText: function (comment) {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                var url = '/cms/cmsConmment/ajaxUpdateReply';
                var params = {
                    id: this.comment.id,
                    reply: comment.text
                };
                commentJson.reply = comment.text;
                commentJson.inputType = 'reply';
                this.updateComment(commentJson, url, params);
            },

            changeAudit: function () {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                var url = '/cms/cmsConmment/ajaxUpdatePLAudit';
                commentJson.auditstatus = commentJson.auditstatus === '1' ? '2' : '1';
                var params = {
                    datas: [{
                        id: this.comment.id,
                        auditstatus: commentJson.auditstatus
                    }]
                };
                this.updateComment(commentJson, url, params);
            },

            changeShowComment: function () {
                Vue.set(this.comment, 'showComment', true)
            },

            changeShowReply: function () {
                Vue.set(this.comment, 'showReply', true)
            },

            changeRecommended: function () {
                var commentJson = JSON.parse(JSON.stringify(this.comment));
                var url = '/cms/cmsConmment/ajaxUpdateRecommend';
                commentJson.isRecommend = commentJson.isRecommend === '1' ? '0' : '1';
                var params = {
                    id: this.comment.id,
                    isRecommend: commentJson.isRecommend
                };
                this.updateComment(commentJson, url, params);
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
                this.$axios.post('/cms/cmsConmment/ajaxDelete?id=' + this.comment.id).then(function (response) {
                    self.$emit('change', {
                        articleId: self.articleId,
                        comment: null,
                        idx: self.idx
                    });
                })
            },

            updateComment: function (comment, url, params) {
                var self = this;
                this.$axios.post(url, params).then(function (response) {
//                    var isCheck = comment.isChecked;
                    Object.assign(comment, response.data.data[0] || {});
                    if (comment.showComment && comment.inputType === 'comment') {
                        comment.showComment = false;
                    }
                    if (comment.showReply && comment.inputType === 'reply') {
                        comment.showReply = false;
                    }
//                    comment.isChecked = isCheck;
                    self.$emit('change', {
                        articleId: self.articleId,
                        comment: comment,
                        idx: self.idx,
                    });
                })
            },
            handleChangeIsChecked: function (row) {
                this.$emit('change', {
                    articleId: null,
                    comment: null,
                    idx: null,
                    selection: row
                });
            }

        },
        created: function () {

        }

    });


    new Vue({
        el: '#app',
        data: function () {

            return {
                articleList: [],
                commentList: [],
                commentEntries: {},
                articleComments: {},
                multipleSelectionId: [],
                multipleBatchPassList: [],
                multipleBatchNoPassList: [],
                searchListForm: {
                    'cnt.id': '${cmsConmment.cnt.id}',
                    pageSize: 10,
                    pageNo: 1,
                    auditstatus: ''
                },
                pageCount: 0,
                checkStates: [{label: '待审核', value: '0'}, {label: '审核通过', value: '1'}, {label: '审核不通过', value: '2'}],
                isAllChecked: false,
                loading: false,
                isIndeterminate: false
            }
        },
        methods: {
            handleChangeComment: function (data) {
                var self = this;
                var articleId = data.articleId;
                var comment = data.comment;
                var idx = data.idx;
                var selection = data.selection;

                if (articleId) {
                    if (comment) {
//                        .splice(idx, 1, comment);
                        this.assignFormData(this.articleComments[articleId][idx], comment)
                    } else {
                        this.articleComments[articleId].splice(idx, 1)
                    }
                }
                if (selection) {
                    this.multipleSelectionId = [];
                    this.multipleBatchPassList = [];
                    this.multipleBatchNoPassList = [];
                    this.commentList.forEach(function (item) {
                        if (item.isChecked) {
                            self.getMultipleData(item);
                        }
                    });
                }
                var len = this.multipleSelectionId.length;
                this.isAllChecked = len == this.commentList.length;
                this.isIndeterminate = len > 0 && len < this.commentList.length;
            },
            getMultipleData: function (item) {
                this.multipleSelectionId.push(item.id);
                this.multipleBatchPassList.push({id: item.id, auditstatus: '1'});
                this.multipleBatchNoPassList.push({id: item.id, auditstatus: '2'});
            },

            getArticleInCommentList: function () {
                var self = this;
                this.loading = true;
                this.$axios.post('/cms/cmsArticle/getOneCmsArticle?id=${cmsConmment.cnt.id}').then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        var ids = [];
                        self.articleList = [data.data] || [];
                        ids = self.articleList.map(function (item) {
                            return item.id;
                        });
                        if (ids.length > 0) {
                            self.getCommentList(ids.join(','))
                        }
                        self.loading = false;
                    }
                })
            },

            getCommentList: function (ids) {
                var self = this;
                this.$axios.get('/cms/cmsConmment/ajaxList?' + Object.toURLSearchParams(self.searchListForm)).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        var pageData = data.data;
                        if (pageData) {
                            self.commentList = pageData.list || [];
                            self.commentList.forEach(function (item) {
                                Vue.set(item, 'isChecked', false);
                                self.isAllChecked = false;
                            });
                            self.commentEntries = self.getCommentEntries();
                            self.articleComments = self.getArticleComments();
                            self.pageCount = pageData.count;
                            self.searchListForm.pageSize = pageData.pageSize || 10;
                            self.searchListForm.pageNo = pageData.pageNo || 1;
                        }
                    }
                })
            },


            getCommentEntries: function () {
                var entries = {};
                this.commentList.forEach(function (item) {
                    if (!entries[item.cnt.id]) {
                        entries[item.cnt.id] = [];
                    }
                    entries[item.cnt.id].push(item);
                });
                return entries;
            },

            getArticleComments: function () {
                var articleComments = {};
                var commentEntries = this.commentEntries;
                this.articleList.forEach(function (item) {
                    articleComments[item.id] = commentEntries[item.id]
                });
                return articleComments
            },

            handleChangeIsAllChecked: function () {
                var self = this;
                this.isIndeterminate = false;
                if (this.isAllChecked) {
                    this.commentList.forEach(function (item) {
                        item.isChecked = true;
                        self.getMultipleData(item);
                    })
                } else {
                    this.multipleSelectionId = [];
                    this.multipleBatchPassList = [];
                    this.multipleBatchNoPassList = [];
                    this.commentList.forEach(function (item) {
                        item.isChecked = false;
                    })
                }
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getArticleInCommentList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getArticleInCommentList();
            },
            batchPass: function () {
                var url = '/cms/cmsConmment/ajaxUpdatePLAudit';
                var data = {
                    datas: this.multipleBatchPassList
                };
                this.axiosRequest(url, true, data);
            },
            batchNoPass: function () {
                var url = '/cms/cmsConmment/ajaxUpdatePLAudit';
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
                    var url = '/cms/cmsConmment/ajaxDeletePL';
                    var data = {
                        datas: self.multipleSelectionId
                    };
                    self.axiosRequest(url, true, data);
                }).catch(function () {

                })
            },
            goHistory: function () {
                window.history.go(-1);
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