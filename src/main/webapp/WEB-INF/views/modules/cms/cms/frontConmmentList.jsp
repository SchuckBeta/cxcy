<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>


</head>
<body>
<style>

    .articles-comments-list .article-comments-header{
        padding: 0 24px;
        background-color: #f2f2f2;
        height: 50px;
        line-height: 50px;
    }

</style>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>

    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
            <e-condition label="栏目" type="radio" v-model="searchListForm.categoryId" :default-props="{label:'name',value:'id'}"
                          :options="categoryList" @change="getArticleInCommentList">
            </e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button size="mini" type="primary" :disabled="multipleSelectionId.length == 0" @click.stop.prevent="batchPass">审核通过
                </el-button>
                <el-button size="mini" type="primary" :disabled="multipleSelectionId.length == 0" @click.stop.prevent="batchNoPass">审核不通过
                </el-button>
                <el-button size="mini" type="primary" :disabled="multipleSelectionId.length == 0" @click.stop.prevent="batchDelete">批量删除
                </el-button>
            </div>
            <div class="search-input">
                <el-checkbox v-model="isAllChecked" :indeterminate="isIndeterminate" @change="handleChangeIsAllChecked" style="margin: 3px 16px 0 25px;"></el-checkbox>
                <el-input
                        placeholder="请输入文章标题"
                        size="mini"
                        v-model="searchListForm.title"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search" @click.stop.prevent="getArticleInCommentList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>

    <div class="table-container mgb-20" v-loading="loading">
        <div class="articles-comments-list">
            <div v-for="article in articleList" :key="article.id" class="article-comments">
                <div class="article-comments-header">
                    <el-row>
                        <el-col class="checkbox-category-title" :span="8">
                            <el-checkbox v-model="article.isChecked" @change="handleChangeIsChecked"></el-checkbox>
                            <span class="category-name">{{article.cmsCategory ? '['+article.cmsCategory.name+']': ''}}</span>
                            <a class="title" href="javascript: void(0);">
                                {{article.title}}
                            </a>
                        </el-col>
                        <el-col :span="2">
                            <span style="color:#999;">文章ID：{{article.id}}</span>
                        </el-col>
                        <el-col :span="2">
                            <el-button size="mini" @click.stop.prevent="goAllComments(article.id)">查看本文全部评论</el-button>
                        </el-col>
                    </el-row>
                </div>
                <div class="article-comments-body control-article-comments">
                    <comment-item v-for="(comment, index) in articleComments[article.id]" :key="comment.id"
                                  :comment="comment" :article-id="article.id" :idx="index"
                                  @change="handleChangeComment"></comment-item>
                </div>
                <div v-if="!articleComments[article.id]" class="text-center" style="padding: 30px 0">
                    <span class="empty-color" >暂无推荐的评论数据</span>
                </div>
            </div>
            <div v-if="articleList.length < 1" class="text-center" style="padding: 60px 0">
                <span class="empty-color" >暂无评论数据</span>
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
$.ajax({
    type : "POST",
    url : "/f/cms/cmsConmment/ajaxList",
    dataType : "json",
    data:{},
    contentType : "application/json;charset=utf-8",
    success:function(msg) {
        console.info("-----" + msg);
    },
    complete:function(msg) {
        console.info(msg);
    }
});

$.ajax({
    type : "POST",
    url : "/f/cms/cmsConmment/ajaxListByCuser",
    dataType : "json",
    data:{},
    contentType : "application/json;charset=utf-8",
    success:function(msg) {
        console.info("-----" + msg);
    },
    complete:function(msg) {
        console.info(msg);
    }
});


$.ajax({
    type : "POST",
    url : "/f/cms/cmsConmment/ajaxUpdateLikes",
    dataType : "json",
    data:JSON.stringify({
    	"id":"10001",
    	"likes":"1"
    }),
    contentType : "application/json;charset=utf-8",
    success:function(msg) {
        console.info("-----" + msg);
    },
    complete:function(msg) {
        console.info(msg);
    }
});

$.ajax({
    type : "POST",
    url : "/f/cms/cmsConmment/ajaxUpdateLikes",
    dataType : "json",
    data:JSON.stringify({
    	"id":"10000",
    	"likes":"-1"
    }),
    contentType : "application/json;charset=utf-8",
    success:function(msg) {
        console.info("-----" + msg);
    },
    complete:function(msg) {
        console.info(msg);
    }
});

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
        '                                        <span v-if="comment.user">评论者：{{comment.user.name}}</span>\n' +
        '                                        <span>评论时间：{{comment.createDate}}</span>\n' +
        '                                    </div>\n' +
        '                                </el-col>\n' +
        '                                <el-col :span="12" class="text-right">\n' +
        '                                           <el-button size="mini" @click.stop.prevent="changeAudit">{{comment.auditstatus== \'1\'? \'审核不通过\' : \'审核通过\'}}</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeShowComment">修改评论</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeShowReply">{{comment.reply == \'\' || comment.reply == null ? \'回复\' : \'修改回复\'}}</el-button>' +
        '                                   <el-button size="mini" @click.stop.prevent="changeRecommended">{{comment.isRecommend == \'1\'? \'取消推荐\' : \'推荐\'}}</el-button>' +
        '                                    <el-button size="mini" @click.stop.prevent="confirmDelComment">删除</el-button>\n' +
        '                                </el-col>\n' +
        '                            </el-row>\n' +
        '                        </div>\n' +
        '                    </div>',
        props: {
            comment: Object,
            articleId: String,
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
                commentJson.reply = comment.text;
                commentJson.inputType = 'reply';
                this.updateComment(commentJson);
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
                this.$axios.post('/cms/cmsConmment/ajaxDelete?id=' + this.comment.id).then(function (response) {
                    self.$emit('change', {
                        articleId: self.articleId,
                        comment: null,
                        idx: self.idx
                    });
                })
            },


            updateComment: function (comment) {
                var self = this;
                this.$axios.post('/cms/cmsConmment/ajaxSave', comment).then(function (response) {
                    var nComment = Object.assign(comment, response.data.data || {});
                    if(comment.showComment && comment.inputType === 'comment'){
                        nComment.showComment = false;
                    }
                    if(comment.showReply && comment.inputType === 'reply'){
                        nComment.showReply = false;
                    }
                    self.$emit('change', {
                        articleId: self.articleId,
                        comment: nComment,
                        idx: self.idx
                    });
                })
            }

        },
        created: function () {

        }

    })


    new Vue({
        el: '#app',
        data: function(){

            return {
                articleList: [],
                commentList: [],
                commentEntries: {},
                articleComments: {},
                multipleSelectionId: [],
                multipleBatchPassList: [],
                multipleBatchNoPassList: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    categoryId:'',
                    title: ''
                },
                pageCount: 0,
                categoryList:[],
                isAllChecked: false,
                isIndeterminate:false,
                loading:false
            }
        },
        methods: {
            handleChangeComment: function (data) {
                var articleId = data.articleId;
                var comment = data.comment;
                var idx = data.idx;
                if (comment) {
                    this.articleComments[articleId].splice(idx, 1, comment)
                } else {
                    this.articleComments[articleId].splice(idx, 1)
                }
            },

            getArticleInCommentList: function () {
                var self = this;
                this.loading = true;
                this.$axios.post('/cms/cmsArticle/articleInCommentList', Object.toURLSearchParams(self.searchListForm)).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        var pageData = data.data;
                        if (pageData) {
                            var ids = [];
                            self.articleList = pageData.list || [];
                            self.articleList.forEach(function (item) {
                                Vue.set(item, 'isChecked', false);
                                self.isAllChecked = false;
                            });
                            self.pageCount = pageData.count;
                            self.searchListForm.pageSize = pageData.pageSize;
                            self.searchListForm.pageNo = pageData.pageNo;
                            ids = self.articleList.map(function (item) {
                                return item.id;
                            });
                            if (ids.length > 0) {
                                self.getCommentList(ids.join(','))
                            }
                        }
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },


            getCommentList: function (ids) {
                var self = this;
                this.$axios.get('/cms/cmsConmment/ajaxList?pageSize=1000').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        var pageData = data.data;
                        if (pageData) {
                            self.commentList = pageData.list || [];
                            self.commentList = self.commentList.filter(function (item) {
                                return item.isRecommend == '1';
                            });
                            self.commentEntries = self.getCommentEntries();
                            self.articleComments = self.getArticleComments();
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

            getCmsCategoryList:function () {
                var self = this;
                this.$axios.get('/cms/category/cmsCategoryList').then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        var list = data.data.list;
                        for(var i = 0; i < list.length; i++){
                            if(list[i].parentId == '1'){
                                self.categoryList.push({name:list[i].name,id:list[i].id})
                            }
                        }
                    }
                }).catch(function (error) {
                    self.$message({
                        message: error.response.data,
                        type: 'error'
                    });
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

            getMultipleData:function (item) {
                this.multipleSelectionId.push(item.id);
                this.multipleBatchPassList.push({cnt:{id:item.id},auditstatus:'1'});
                this.multipleBatchNoPassList.push({cnt:{id:item.id},auditstatus:'2'});
            },
            handleChangeIsChecked: function () {
                var self = this;
                this.multipleSelectionId = [];
                this.multipleBatchPassList = [];
                this.multipleBatchNoPassList = [];
                this.articleList.forEach(function (item) {
                    if(item.isChecked){
                        self.getMultipleData(item);
                    }
                });
                var len = this.multipleSelectionId.length;
                this.isAllChecked = len == this.articleList.length;
                this.isIndeterminate = len > 0 && len < this.articleList.length;
            },
            handleChangeIsAllChecked:function () {
                var self = this;
                this.isIndeterminate = false;
                if(this.isAllChecked){
                    this.articleList.forEach(function (item) {
                        item.isChecked = true;
                        self.getMultipleData(item);
                    })
                }else{
                    this.multipleSelectionId = [];
                    this.multipleBatchPassList = [];
                    this.multipleBatchNoPassList = [];
                    this.articleList.forEach(function (item) {
                        item.isChecked = false;
                    })
                }
            },
            batchPass:function () {
                var url = '/cms/cmsConmment/ajaxUpdatePLAuditByArticle';
                var data = {
                    datas: this.multipleBatchPassList
                };
                this.axiosRequest(url,true,data);
            },
            batchNoPass:function () {
                var url = '/cms/cmsConmment/ajaxUpdatePLAuditByArticle';
                var data = {
                    datas: this.multipleBatchNoPassList
                };
                this.axiosRequest(url,true,data);
            },
            batchDelete:function () {
                var self = this;
                this.$confirm('是否确定删除','提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(function () {
                    var url = '/cms/cmsConmment/ajaxDeletePLByArticle';
                    var data = {
                        datas: self.multipleSelectionId
                    };
                    self.axiosRequest(url,true,data);
                }).catch(function () {

                })
            },
            goAllComments:function (id) {
                window.location.href = '${ctx}/cms/cmsConmment/form?cnt.id=' + id;
            },
            axiosRequest:function (url,showMsg,data) {
                var self = this;
                this.loading = true;
                this.$axios({
                    method:'POST',
                    url:url,
                    data:data
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        self.getArticleInCommentList();
                        self.isAllChecked = false;
                        self.isIndeterminate = false;
                        if(showMsg){
                            self.$message({
                                message:'操作成功',
                                type:'success'
                            })
                        }
                    }else{
                        self.$message({
                            message:'操作失败',
                            type:'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.$message({
                        message:'请求失败',
                        type:'error'
                    })
                    self.loading = false;
                })
            }
        },

        beforeMount: function () {
            this.getCmsCategoryList();
            this.getArticleInCommentList();
        }
    })

</script>
</body>
</html>