<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <!-- <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script> -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" src="${ctxJs}/components/recommendation/recommendation-wqt.js"></script>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar f-title="优秀项目" :second-name="baseContentForm.id ? '修改' : '添加'"></edit-bar>
    </div>
    <el-form :model="baseContentForm" ref="baseContentForm" :rules="baseContentFormRules" size="mini" class="cms-content-project-form"
             :disabled="baseContentDisabled"
             label-width="120px">

        <el-container>
            <el-main class="main-left">
                <el-row :gutter="10">
                    <el-col :span="24">
                        <el-form-item prop="categories" label="归属栏目：">
                            <el-cascader :options="menuTree" change-on-select
                                         :props="{label: 'name', children: 'children', value: 'id'}"
                                         v-model="baseContentForm.categories"></el-cascader>
                            <span v-if="contentTypeName" style="font-size: 12px; margin-left: 8px;" class="empty-color">内容内型：{{contentTypeName}}</span>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :lg="8">
                        <el-form-item prop="module" label="模型：">
                            <el-select v-model="baseContentForm.module" placeholder="请选择模型"
                                       @change="handleChangeModule">
                                <el-option v-for="item in cmsModuleListArticle" :key="item.value" :label="item.label"
                                           :value="item.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col v-if="isShowLink" :sm="16" :md="16"  :lg="10">
                        <el-form-item prop="link" label="标题链接：">
                            <el-input v-model="baseContentForm.link">
                                <span slot="prepend">http://</span>
                            </el-input>
                        </el-form-item>
                    </el-col>
                    <el-col v-if="isShowLink" :sm="6" :md="6" :lg="6">
                        <el-form-item prop="isshowlink" label-width="0">
                            <el-checkbox v-model="baseContentForm.isshowlink" true-label="1" false-label="0"
                                         style="vertical-align: middle">添加超链接
                            </el-checkbox>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :lg="12">
                        <el-form-item prop="title" label="标题：">
                            <el-input v-model="baseContentForm.title"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :lg="6">
                        <el-form-item prop="writer" label="作者：">
                            <el-input v-model="baseContentForm.writer"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :lg="6">
                        <el-form-item prop="cmsArticleData.copyfrom" label="来源：">
                            <el-input v-model="baseContentForm.cmsArticleData.copyfrom"></el-input>
                        </el-form-item>
                    </el-col>


                    <el-col :span="12">
                        <el-form-item prop="articlepulishDate" label="发表时间：">
                            <el-date-picker
                                    v-model="baseContentForm.articlepulishDate"
                                    type="date"
                                    format="yyyy-MM-dd"
                                    :picker-options="publishEpPickerOptions"
                                    value-format="yyyy-MM-dd HH:mm:ss"
                                    placeholder="选择日期">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="24">
                        <el-form-item prop="description" label="摘要：">
                            <el-input type="textarea" :rows="3" v-model="baseContentForm.description"></el-input>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item prop="contentActions" label="是否显示：">
                    <el-checkbox v-model="baseContentForm.isshowwriter" true-label="1" false-label="0"
                                 @change="handleChangeShowWT(baseContentForm.isshowwriter, 0)">作者
                    </el-checkbox>
                    <el-checkbox v-model="baseContentForm.cmsArticleData.isshowcopyfrom" true-label="1" false-label="0"
                                 @change="handleChangeShowCF(baseContentForm.cmsArticleData.isshowcopyfrom, 1)">
                        文章来源
                    </el-checkbox>
                    <el-checkbox v-model="baseContentForm.isshowpublishdate" true-label="1" false-label="0"
                                 @change="handleChangeShowPD(baseContentForm.isshowpublishdate, 2)">发表时间
                    </el-checkbox>
                    <el-checkbox :disabled="isDescriptionDisabled" v-model="baseContentForm.isshowdescription"
                                 @change="handleChangeShowDS(baseContentForm.isshowdescription, 3)"
                                 true-label="1" false-label="0">摘要
                    </el-checkbox>
                </el-form-item>

                <el-form-item prop="hasComment" label="相关推荐：">
                    <el-button type="primary" @click.stop.prevent="relateRecommendVisible=true">添加相关推荐</el-button>
                </el-form-item>
                <el-form-item>
                    <div class="table-container">
                        <el-table :data="recommendList" size="small">
                            <el-table-column label="标题" align="center">
                                <template slot-scope="scope">
                                    <el-tooltip class="item" effect="dark" :content="scope.row.title" placement="bottom">
                                        <span class="over-flow-tooltip project-office-tooltip">{{scope.row.title | textEllipsis}}</span>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                            <el-table-column label="栏目名称" align="center">
                                <template slot-scope="scope">
                                    <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.cmsCategory ? scope.row.cmsCategory.name : '-'" placement="bottom">
                                        <span class="over-flow-tooltip project-office-tooltip">{{scope.row.cmsCategory ? scope.row.cmsCategory.name : '-'}}</span>
                                    </el-tooltip>
                                </template>
                            </el-table-column>
                            <%--<el-table-column label="排序" align="center">--%>
                            <%--<template slot-scope="scope">--%>
                            <%--<el-switch size="mini"></el-switch>--%>
                            <%--</template>--%>
                            <%--</el-table-column>--%>
                            <el-table-column label="状态" align="center">
                                <template slot-scope="scope">
                                    {{scope.row.publishStatus == '1' ? '发布' : '未发布'}}
                                </template>
                            </el-table-column>
                            <el-table-column label="发布时间" align="center">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.publishStatus == '1'">{{scope.row.publishStartdate | formatDateFilter('YYYY-MM-DD')}}</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="发布到期" align="center">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.publishStatus == '1' && scope.row.publishhforever == '0'">{{scope.row.publishEnddate | formatDateFilter('YYYY-MM-DD')}}</span>
                                    <span v-if="scope.row.publishStatus == '1' && scope.row.publishhforever == '1'">永久</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="操作" align="center">
                                <template slot-scope="scope">
                                    <div class="table-btns-action">
                                        <el-button type="text" size="mini"
                                                   @click.stop.prevent="cancelRem(scope.row.id)">取消推荐
                                        </el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                </el-form-item>

                <el-form-item label="内容：" prop="cmsArticleData.content">
                    <script id="excellentProContent" name="content" type="text/plain" style="width:100%;height:400px">
                    {{baseContentForm.cmsArticleData.content}}









                    </script>
                </el-form-item>

                <%--<el-form-item prop="files" label="附件：">--%>
                <%--<el-upload action="${ctx}/ftp/ueditorUpload/uploadTemp?folder=project"--%>
                <%--:show-file-list="true"--%>
                <%--:file-list="baseContentForm.files"--%>
                <%--name="upfile">--%>
                <%--<el-button size="mini" type="primary">点击上传</el-button>--%>
                <%--</el-upload>--%>
                <%--</el-form-item>--%>
            </el-main>


            <el-aside width="310px">
                <div class="aside-right">
                    <el-form-item v-if="contentTypeName.indexOf('图') > -1" prop="thumbnail" label="封面：" label-width="80px">
                        <div class="bsc-thumbnail-preview">
                            <img v-if="baseContentForm.thumbnail"
                                 :src="baseContentForm.thumbnail | ftpHttpFilter(ftpHttp) | defaultProPic"
                                 @click.stop.prevent="handleChangeExlProPicOpen">
                            <i v-else class="el-icon-plus"
                               @click.stop.prevent="handleChangeExlProPicOpen"></i>
                        </div>
                    </el-form-item>
                    <el-form-item prop="sort" label="排序：" label-width="80px">
                        <el-input-number v-model="baseContentForm.sort" :min="1" style="width:120px;"></el-input-number>
                    </el-form-item>
                    <div class="item-bottom">
                        <p class="operate-state">状态</p>
                        <el-form-item prop="publishStatus" label-width="10px">
                            <el-checkbox v-model="baseContentForm.publishStatus" @change="handleChangePublishStatus"
                                         true-label="1" false-label="0">发布
                            </el-checkbox>
                        </el-form-item>
                        <template v-if="baseContentForm.publishStatus == 1">
                            <el-form-item prop="publishExpires" label="有效期：" label-width="93px">
                                <el-date-picker
                                        v-model="baseContentForm.publishExpires"
                                        type="daterange"
                                        align="right"
                                        unlink-panels
                                        range-separator="至"
                                        :picker-options="publishEpPickerOptions"
                                        start-placeholder="开始日期"
                                        end-placeholder="结束日期"
                                        @change="handleChangePublishEp"
                                        style="width:202px;">
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item prop="publishhforever" label-width="34px">
                                <el-checkbox v-model="baseContentForm.publishhforever" true-label="1" false-label="0"
                                             @change="handleChangePbF">
                                    永久
                                </el-checkbox>
                            </el-form-item>
                        </template>
                        <el-form-item prop="top" label-width="10px">
                            <el-checkbox v-model="baseContentForm.top" @change="handleChangeTop" true-label="1"
                                         false-label="0">置顶
                            </el-checkbox>
                        </el-form-item>
                        <template v-if="baseContentForm.top == 1">
                            <el-form-item prop="deadline" label="过期时间：" label-width="107px">
                                <el-date-picker v-model="baseContentForm.deadline" type="date" format="yyyy-MM-dd"
                                                placeholder="选择日期" value-format="yyyy-MM-dd HH:mm:ss"
                                                :picker-options="publishEpPickerOptions"
                                                style="width:202px;">
                                </el-date-picker>
                            </el-form-item>
                        </template>
                        <el-form-item prop="posid" label-width="10px">
                            <el-checkbox v-model="baseContentForm.posid" true-label="1" false-label="0">推荐首页
                            </el-checkbox>
                        </el-form-item>
                        <el-form-item prop="cmsArticleData.allowComment" label-width="10px">
                            <el-checkbox v-model="baseContentForm.cmsArticleData.allowComment" true-label="1"
                                         false-label="0">允许评论
                            </el-checkbox>
                        </el-form-item>
                    </div>
                </div>
            </el-aside>
        </el-container>
        <el-form-item class="text-center">
            <el-button type="primary" @click.stop.prevent="saveCmsArticle">保存</el-button>
            <%--<el-button type="primary" @click.stop.prevent="goToNewView">预览</el-button>--%>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>

    </el-form>


    <el-dialog title="相关推荐" :visible.sync="relateRecommendVisible" :close-on-click-modal="false"
               :before-close="handleCloseDialog" width="70%" style="min-width: 794px;">
        <recommendation v-if="relateRecommendVisible" url="/cms/cmsArticle/cmsArticleList" :related-ids="relatedIds"
                        @change="handleChangeRelatedIds"></recommendation>
    </el-dialog>


    <el-dialog title="上传封面"
               width="440px"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleArticlePic"
               :before-close="handleChangeArticlePicClose">
        <e-pic-file v-model="uploadArticleProPic" :disabled="isUpdating" @get-file="getExlProPicFile"></e-pic-file>
        <cropper-pic :img-src="uploadArticleProPic" :disabled="isUpdating" ref="cropperPic"
                     :copper-params="{aspectRatio: 297/187}"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeArticlePicClose">取消
            </el-button>
            <el-button size="mini" :disabled="!articlePicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateArticlePic">上传
            </el-button>
        </div>
    </el-dialog>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.menuTreeMixin],
        data: function () {
            <%--var cmsModuleList = JSON.parse('${fns: toJson(fns: getDictList('0000000274'))}');--%>
            var contentTypes = JSON.parse('${fns: toJson(fns: getDictList('0000000271'))}');
            var cmsArticleForm =${fns: toJson(cmsArticle)}, cmsArticleData;
            var relatedIds = [];
            cmsArticleForm = Object.assign({}, cmsArticleForm);
            cmsArticleData = cmsArticleForm.cmsArticleData || {};
            relatedIds = cmsArticleData.relation ? cmsArticleData.relation.split(',') : [];

            return {

                baseContentForm: {
                    id: cmsArticleForm.id || '',
                    categoryId: cmsArticleForm.categoryId,
                    categories: [], //栏目模块
                    module: cmsArticleForm.module || '0000000278',
                    title: cmsArticleForm.title,
                    link: cmsArticleForm.link,
                    isshowlink: cmsArticleForm.isshowlink || '',
                    writer: cmsArticleForm.writer || '',
                    isshowwriter: cmsArticleForm.isshowwriter || '1',
                    articlepulishDate: cmsArticleForm.articlepulishDate || moment('${systemDate}').format('YYYY-MM-DD HH:mm:ss'),
                    isshowpublishdate: cmsArticleForm.isshowpublishdate || '1',
                    isshowdescription: cmsArticleForm.isshowdescription || '1',
                    description: cmsArticleForm.description || '',
                    thumbnail: cmsArticleForm.thumbnail || '',
                    sort: cmsArticleForm.sort || '1',
                    publishStatus: cmsArticleForm.publishStatus || '1',
                    publishhforever: cmsArticleForm.publishhforever || '1',
                    top: cmsArticleForm.top || '0',
                    deadline: cmsArticleForm.deadline || '',//过期时间
                    posid: cmsArticleForm.posid || '1',
                    publishStartdate: cmsArticleForm.publishStartdate || '',
                    publishEnddate: cmsArticleForm.publishEnddate || '',
                    cmsArticleData: {
                        id: cmsArticleData.id || '',
                        allowComment: cmsArticleData.allowComment || '0',
                        content: cmsArticleData.content || '',
                        copyfrom: cmsArticleData.copyfrom || '',
                        isshowcopyfrom: cmsArticleData.isshowcopyfrom || '1',
                        relation: cmsArticleData.relation || ''
                    },
                    publishExpires: [],//有效期,
                    contentActions: []
                },
                oldTile:cmsArticleForm.title || '',

                baseContentDisabled: false,

                category: null,

                cmsModuleList: [],
                contentTypes: contentTypes,
                menuList: [],
                menuTree: [],
                UEditor: null, //editor
                relateRecommendVisible: false,


                isUpdating: false,
                uploadArticleProPic: '',
                dialogVisibleArticlePic: false,
                articlePicFile: null,

                relatedIds: relatedIds,
                recommendList: [],
                publishEpPickerOptions: {
                    disabledDate: function(time) {
                        return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
                    }
                }

            }
        },

        computed: {

            cmsModuleListArticle: function () {
                return this.cmsModuleList.filter(function (item) {
                    return item.label.indexOf('文章') > -1 || item.label.indexOf('链接') > -1;
                })
            },

            contentTypeName: function () {
                if (!this.category) {
                    return ''
                }
                return this.contentTypeEntries[this.category.contenttype];
            },

            contentTypeEntries: function () {
                return this.getEntries(this.contentTypes)
            },
            cmsModuleEntries: function () {
                return this.getEntries(this.cmsModuleList)
            },
            isShowLink: function () {
                if (!this.baseContentForm.module || !this.cmsModuleEntries) return false;
                return this.cmsModuleEntries[this.baseContentForm.module].indexOf('链接') > -1;
            },
            isDescriptionDisabled: function () {
                if (!this.contentTypeName) {
                    return false
                }
                return this.contentTypeName.indexOf('图') > -1;
            },


            baseContentFormRules: {
                get: function () {
                    var baseContentForm = this.baseContentForm;
                    var isshowcopyfrom = baseContentForm.cmsArticleData.isshowcopyfrom;
                    var isshowwriter = baseContentForm.isshowwriter;
                    var isshowdescription = baseContentForm.isshowdescription;
                    var isshowpublishdate = baseContentForm.isshowpublishdate;
                    var top = baseContentForm.top;
                    var isPublishRequired = baseContentForm.publishStatus == '1' && baseContentForm.publishhforever == '0' && baseContentForm.publishExpires.length == 0;
                    var isDescriptionDisabled = this.isDescriptionDisabled;
                    return {
                        categories: [{required: true, message: '请选择归属栏目', trigger: 'change'}],
                        module: [{required: true, message: '请选择栏目模块', trigger: 'change'}],
                        title: [
                            {required: true, message: '请输入标题', trigger: 'change'},
                            {max: 125, message: '请输入不大于125位字符', trigger: 'change'},
                            {validator: this.validatorTitle, trigger: 'blur'}
                        ],
                        writer: [
//                            {required: isshowwriter == '1', message: '请输入作者', trigger: 'blur'},
                            {max: 24, message: '请输入不大于24位字符', trigger: 'change'}
                        ],
                        cmsArticleData: {
                            copyfrom: [
//                                {required: isshowcopyfrom == '1', message: '请输入来源', trigger: 'blur'},
                                {max: 24, message: '请输入不大于24位字符', trigger: 'change'}
                            ],
                            content: [
                                {required: true, message: '请输入内容', trigger: 'change'}
                            ]
                        },
                        link: [
                            {required: baseContentForm.isshowlink == '1', message: '请输入标题链接', trigger: 'change'},
                            {max: 2000, message: '请输入不大于2000位字符', trigger: 'change'}
                        ],
                        description: [
//                            {required: isshowdescription == '1', message: '请输入摘要', trigger: 'blur'},
                            {max: 200, message: '请输入不大于200位字符', trigger: 'change'}
                        ],
//                        articlepulishDate: [
//                            {required: isshowpublishdate == '1', message: '请选择文章发表时间', trigger: 'change'}
//                        ],
                        contentActions: [
                            {required: true},
                            {
                                validator: this.validatorContentActions, trigger: 'change'
                            }
                        ],
                        publishExpires: [
                            {required: isPublishRequired, message: '请选择发布有效期或者永久', trigger: 'change'}
                        ],
                        publishhforever: [
                            {required: isPublishRequired, message: '请选择发布有效期或者永久', trigger: 'change'}
                        ],
                        deadline: [
                            {required: top == '1', message: '请选择过期时间', trigger: 'change'}
                        ],
                        thumbnail: [
                            {required: isDescriptionDisabled, message: '请上传封面', trigger: 'change'}
                        ]
                    }
                }
            }

        },
        watch: {
            'baseContentForm.categories': function (value) {
                if (value && value.length > 0) {
                    this.baseContentForm.categoryId = value[value.length - 1];
                    this.category = this.getCategory(); //重新获取categroy对象
                }
            },
            'isDescriptionDisabled': function (value) {
                if (value) {
                    this.baseContentForm.isshowdescription = '1'
                }
            },
            'baseContentForm.publishExpires': function (value) {
                if (!value && value.length < 1) {
                    this.baseContentForm.publishStartdate = '';
                    this.baseContentForm.publishEnddate = '';
                    return
                }
                this.baseContentForm.publishStartdate = moment(value[0]).format('YYYY-MM-DD HH:mm:ss');
                this.baseContentForm.publishEnddate = moment(value[1]).format('YYYY-MM-DD HH:mm:ss');
            },
            contentTypeName: function (value) {
                if (value && value.indexOf('图') === -1) {
                    this.baseContentForm.thumbnail = '';
                }
            }
        },

        methods: {
            goToBack: function () {
                window.history.go(-1);
            },

            handleChangePublishStatus: function (value) {
                if (value === '0') {
                    this.baseContentForm.publishhforever = '0';
                    this.baseContentForm.publishExpires = [];
                }
            },

            handleChangeTop: function (value) {
                if (value == '0') {
                    this.baseContentForm.deadline = '';
                }
            },

            handleChangeRelatedIds: function (data) {
                this.relatedIds = data.relatedIds;
                this.baseContentForm.cmsArticleData.relation = this.relatedIds.join(',');
                this.getRecommendList(this.baseContentForm.cmsArticleData.relation)
                if (data.type === 'batch') {
                    this.relateRecommendVisible = false;
                }
            },

            //取消推荐
            cancelRem: function (id) {
                this.relatedIds.splice(this.relatedIds.indexOf(id), 1);
                this.baseContentForm.cmsArticleData.relation = this.relatedIds.join(',');
                this.getRecommendList(this.baseContentForm.cmsArticleData.relation)
            },

            //图片上传开始

            handleChangeArticlePicClose: function () {
                this.articlePicFile = null;
                this.dialogVisibleArticlePic = false;
            },

            updateArticlePic: function () {
                var data = this.$refs.cropperPic.getData();
                var self = this;
                var formData = new FormData();

                if (data.x < 0 || data.y < 0) {
                    this.show$message({
                        status: false,
                        msg: '超出边界，请缩小裁剪框，点击上传'
                    });
                    return false;
                }

                this.isUpdating = true;
                formData.append('upfile', this.articlePicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/cutImgToTempDir?x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
                        self.baseContentForm.thumbnail = data.ftpUrl;
                    } else {
                        self.$message({
                            message: data.msg,
                            type: 'error'
                        })
                    }
                    self.isUpdating = false;
                    self.handleChangeArticlePicClose();

                }).catch(function (error) {
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                })
            },

            getExlProPicFile: function (file) {
                this.articlePicFile = file;
            },

            handleChangeExlProPicOpen: function () {
                var coverImg = this.baseContentForm.thumbnail;
                this.dialogVisibleArticlePic = true;
                this.$nextTick(function (_) {
                    this.uploadArticleProPic = this.addFtpHttp(coverImg) || '/img/video-default.jpg';
                })
            },


            validatorTitle:function (rule, value, callback) {
                var self = this;
                if(this.oldTile == value){
                    return callback();
                }
                var params = {
                    cmsCategory: {
                        id: self.baseContentForm.categoryId
                    },
                    title: value
                };
                this.$axios.post('cms/cmsArticle/validateArticleName',params).then(function (response) {
                    var data = response.data;
                    if(data.code == '1005'){
                        return callback(new Error('该栏目下此标题已存在'));
                    }
                    return callback();
                }).catch(function (error) {
                    return callback();
                });
            },

            //图片上传结束

            validatorContentActions: function (rule, value, callback) {
                var hasShowOne = value.some(function (item) {
                    return item == '1';
                });
                if (!hasShowOne) {
                    return callback(new Error('请选择需要显示的块'))
                }
                return callback()
            },

            handleChangePublishEp: function (value) {
                this.baseContentForm.publishhforever = '0'
            },

            handleChangePbF: function () {
                this.baseContentForm.publishExpires = []
            },

            handleChangeModule: function (value) {
                if (!this.isShowLink) {
                    this.baseContentForm.link = '';
                    this.baseContentForm.isshowlink = '';
                }
            },

            handleChangeShowWT: function (value, index) {
                this.setContentActions(value, index)
            },
            handleChangeShowCF: function (value, index) {
                this.setContentActions(value, index)
            },
            handleChangeShowPD: function (value, index) {
                this.setContentActions(value, index)
            },
            handleChangeShowDS: function (value, index) {
                this.setContentActions(value, index)
            },

            setContentActions: function (value, index) {
                this.baseContentForm.contentActions.splice(index, 1, value);
                this.$refs.baseContentForm.validateField('contentActions')
            },

            getCategory: function () {
                if (!this.baseContentForm.categoryId) {
                    return null;
                }
                return this.menuEntries[this.baseContentForm.categoryId.toString()]
            },

            getCategoryList: function () {
                var self = this;
                this.$axios.get('/cms/category/cmsCategoryList?module=0000000275').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        data = data.data;
                        if (data) {
                            var list = data.list || [];
                            self.menuList = list;
                            self.setFlattenMenuList([].concat(self.menuList))
                        }
                    }
                })
            },

            setFlattenMenuList: function (menuList) {
                this.setMenuEntries(menuList);
                var rootIds = this.setMenuRootIds(menuList);
                this.menuTree = this.getMenuTreeTree(rootIds, this.menuProps, menuList);
                this.baseContentForm.categories = this.getCategoryLocalParentId();
            },

            getCategoryLocalParentId: function () {
                var parentId = this.baseContentForm.categoryId;
                var result = [];
                if (!parentId) {
                    return result;
                }
                while (parentId) {
                    var parent = this.menuEntries[parentId];
                    if (!parent) {
                        break;
                    }
                    result.unshift(parentId);
                    parentId = parent.parentId;
                }
                return result
            },


            saveCmsArticle: function () {
                var self = this;
                this.baseContentForm.cmsArticleData.content = this.UEditor.getContent();
                this.$refs.baseContentForm.validate(function (valid) {
                    if (valid) {

                        self.postCmsArticle();
                    }
                })

            },

            postCmsArticle: function () {
                var self = this;
                this.baseContentDisabled = true;
                var baseContentForm = JSON.parse(JSON.stringify(this.baseContentForm));
                delete baseContentForm.categories;
                delete baseContentForm.contentActions;
                delete baseContentForm.publishExpires;
                baseContentForm.sort = baseContentForm.sort.toString();
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsArticle/editSaveCmsArticle',
                    data: baseContentForm
                }).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    var isResponseSuccess = self.isResponseSuccess(data.code);
                    if (checkResponseCode) {
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: '保存成功'
                        }).then(function () {
                            location.href = self.frontOrAdmin + '/cms/cmsArticle'
                        }).catch(function () {

                        })
                    } else {
                        self.$message({
                            message: !isResponseSuccess ? data.msg : '操作成功',
                            type: !isResponseSuccess ? 'error' : 'success'
                        });
                    }

                    self.baseContentDisabled = false;
                }).catch(function (error) {
                    self.$message({
                        message: error.response.data,
                        type: 'error'
                    })
                    self.baseContentDisabled = false;
                })


            },


            handleCloseDialog: function () {
                this.relateRecommendVisible = false;
            },


            getRecommendList: function (relation) {
                if (!relation) return this.recommendList = [];
                var self = this;
                this.$axios.get('/cms/cmsArticle/cmsArticleList?ids=' + relation).then(function (response) {
                    var pageData = response.data.data;
                    if (pageData) {
                        self.recommendList = pageData.list || [];
                    }

                })
            },

            getCategoryModelList: function () {
                var self = this;
                this.$axios.get('/cms/cmsArticle/categoryModel?modelParam=all').then(function (response) {
                    var cmsModuleList = response.data.data;
                    if (cmsModuleList) {
                        self.cmsModuleList = cmsModuleList || [];
                    }

                })
            },

            configUeditor: function () {
                var frontOrAdmin = this.frontOrAdmin;
                UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
                UE.Editor.prototype.getActionUrl = function (action) {
                    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo'
                            || action == 'uploadfile' || action == 'catchimage' || action == 'listimage' || action == 'listfile') {
                        return frontOrAdmin+'/ftp/ueditorUpload/uploadTempFormal?folder=cmsArticle';
                    } else {
                        return this._bkGetActionUrl.call(this, action);
                    }
                }
            }

        },
        beforeMount: function () {
            this.getCategoryList();
            this.getRecommendList(this.baseContentForm.cmsArticleData.relation)
        },

        mounted: function () {
            var baseContentForm = this.baseContentForm;
            this.UEditor = UE.getEditor('excellentProContent');
            this.baseContentForm.contentActions = [baseContentForm.isshowwriter, baseContentForm.cmsArticleData.isshowcopyfrom, baseContentForm.isshowpublishdate, baseContentForm.isshowdescription];
            if (baseContentForm.publishEnddate) {
                this.baseContentForm.publishExpires = [baseContentForm.publishStartdate, baseContentForm.publishEnddate]
            }
            this.getCategoryModelList();
            this.configUeditor()



        }
    })

</script>

</body>
</html>