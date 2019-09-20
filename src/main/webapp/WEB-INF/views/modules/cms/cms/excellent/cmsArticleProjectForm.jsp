<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <%--<script type="text/javascript" src="${ctxJs}/components/recommendation/recommendation.js"></script>--%>
    <script type="text/javascript" src="${ctxJs}/components/recommendation/recommendation-wqt.js"></script>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar f-title="优秀项目" second-name="修改"></edit-bar>
    </div>
    <el-form :model="baseContentForm" ref="baseContentForm" :rules="baseContentFormRules" size="mini" class="cms-content-project-form"
             label-width="90px">

        <el-container>
            <el-main class="main-left">
                <el-row :gutter="10">
                    <el-col :xs="24" :lg="12">
                        <el-form-item label="归属栏目：">
                            <div class="el-form-item-content_static">{{baseContentForm.categoryId |
                                selectedFilter(categoryEntries)}}
                            </div>
                            <%--<el-cascader :disabled="true" :options="menuTree" :show-all-levels="false"--%>
                            <%--:props="{label: 'name', children: 'children', value: 'id'}"--%>
                            <%--v-model="baseContentForm.categories"></el-cascader>--%>

                            <%--<el-select disabled v-model="baseContentForm.categoryId">--%>
                            <%--<el-option v-for="item in categoryList" :key="item.id" :value="item.id"--%>
                            <%--:label="item.name"></el-option>--%>
                            <%--</el-select>--%>
                            <span v-if="contentTypeName" style="font-size: 12px; margin-left: 8px;" class="empty-color">内容类型：{{contentTypeName}}</span>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :lg="12">
                        <el-form-item prop="module" label="模型：">
                            <div class="el-form-item-content_static">
                                {{baseContentForm.module | selectedFilter(cmsModuleEntries)}}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="文章标题：">
                    <div class="el-form-item-content_static">{{baseContentForm.title}}</div>
                </el-form-item>
                <el-form-item prop="keywords" label="关键词：">
                    <input type="hidden" v-model="baseContentForm.keywords">
                    <el-tag
                            :key="tag"
                            v-for="tag in dynamicTags"
                            size="medium"
                            closable
                            :disable-transitions="false"
                            @close="handleTagClose(tag)">
                        {{tag}}
                    </el-tag>
                    <el-input style="width:100px;"
                              class="input-new-tag"
                              v-if="inputVisible"
                              v-model="inputValue"
                              ref="saveTagInput"
                              size="mini"
                              @keyup.enter.native="handleInputConfirm"
                              @blur="handleInputConfirm"
                    >
                    </el-input>
                    <el-button v-else class="button-new-tag" size="mini" @click="showInput">+ 关键词</el-button>
                </el-form-item>
                <div class="base-content-separate-line"></div>
                <el-row :gutter="10">
                    <el-col :span="12">
                        <el-form-item prop="link" label="标题链接：">
                            <el-input v-model="baseContentForm.link"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item prop="isshowlink" label-width="0">
                            <el-checkbox v-model="baseContentForm.isshowlink" :true-label="'1'" :false-label="'0'">添加
                            </el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item prop="cmsArticleData.relation" label="相关推荐：">
                    <input type="hidden" v-model="baseContentForm.cmsArticleData.relation">
                    <div class="table-container" style="margin-bottom:20px;">
                        <el-table :data="relationList" size="mini" class="table" ref="relationList"
                                  style="margin-bottom: 0">
                            <el-table-column label="标题" prop="title" align="center">
                                <template slot-scope="scope">
                                    <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.title" placement="bottom">
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
                                        <el-button size="mini" type="text"
                                                   @click.stop.prevent="cancelRecommend(scope.row.id)">取消推荐
                                        </el-button>
                                    </div>
                                </template>
                            </el-table-column>
                        </el-table>
                    </div>
                    <el-button type="primary" @click.stop.prevent="relateRecommendVisible = true">添加相关推荐</el-button>
                </el-form-item>
                <el-form-item prop="cmsArticleData.content" label="项目展示：">
                    <script id="excellentProContent" name="content" type="text/plain" style="width:100%;height:600px">
                    {{baseContentForm.cmsArticleData.content}}

                    </script>
                </el-form-item>
            </el-main>


            <el-aside width="310px">
                <div class="aside-right">
                    <el-form-item prop="thumbnail" label="封面：" label-width="80px">
                        <div class="bsc-thumbnail-preview">
                            <img v-if="baseContentForm.thumbnail"
                                 :src="baseContentForm.thumbnail | ftpHttpFilter(ftpHttp) | defaultProPic"
                                 @click.stop.prevent="handleChangeExlProPicOpen">
                            <i v-else class="el-icon-plus"
                               @click.stop.prevent="handleChangeExlProPicOpen"></i>
                            <div v-if="baseContentForm.thumbnail" class="arrow-block-delete">
                                <i class="el-icon-delete" @click.sotp.prevent="baseContentForm.thumbnail = ''"></i>
                            </div>
                        </div>

                    </el-form-item>
                    <el-form-item prop="articlepulishDate" label="发表时间：" label-width="81px">
                        <el-date-picker v-model="baseContentForm.articlepulishDate" type="date" :picker-options="publishEpPickerOptions"
                                        value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择日期"
                                        style="width:130px"></el-date-picker>
                    </el-form-item>
                    <el-form-item prop="sort" label="排序：" label-width="80px">
                        <el-input-number v-model="baseContentForm.sort" :min="1" style="width:120px;"></el-input-number>
                    </el-form-item>
                    <div class="item-bottom">
                        <p class="operate-state">状态</p>
                        <el-form-item prop="publishStatus" label-width="10px">
                            <el-checkbox v-model="baseContentForm.publishStatus" :true-label="'1'" :false-label="'0'"
                                         @change="changePublishStatus">发布
                            </el-checkbox>
                        </el-form-item>
                        <el-form-item label="有效期：" prop="validDate" label-width="93px"
                                      v-show="baseContentForm.publishStatus == '1'">
                            <el-date-picker
                                    v-model="baseContentForm.validDate"
                                    type="daterange"
                                    align="right"
                                    unlink-panels
                                    range-separator="至"
                                    start-placeholder="开始日期"
                                    end-placeholder="结束日期"
                                    :picker-options="publishEpPickerOptions"
                                    @change="handleChangePublishEp"
                                    :clearable="false"
                                    style="width:202px;">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item prop="publishhforever" label-width="34px"
                                      v-show="baseContentForm.publishStatus == '1'">
                            <el-checkbox v-model="baseContentForm.publishhforever" :true-label="'1'" :false-label="'0'"
                                         @change="handleChangePbF">永久
                            </el-checkbox>
                        </el-form-item>

                        <el-form-item prop="top" label-width="10px">
                            <el-checkbox v-model="baseContentForm.top" :true-label="'1'" :false-label="'0'">置顶
                            </el-checkbox>
                        </el-form-item>
                        <el-form-item prop="deadline" label="过期时间：" label-width="107px"
                                      v-show="baseContentForm.top == '1'">
                            <el-date-picker style="width:130px;" value-format="yyyy-MM-dd HH:mm:ss"
                                            v-model="baseContentForm.deadline"
                                            :picker-options="publishEpPickerOptions"
                                            type="date"
                                            placeholder="选择日期">
                            </el-date-picker>
                        </el-form-item>

                        <el-form-item prop="posid" label-width="10px">
                            <el-checkbox v-model="baseContentForm.posid" :true-label="'1'" :false-label="'0'">推荐首页
                            </el-checkbox>
                        </el-form-item>
                        </el-form-item>
                        <el-form-item prop="cmsArticleData.allowComment" label-width="10px">
                            <el-checkbox v-model="baseContentForm.cmsArticleData.allowComment" :true-label="'1'"
                                         :false-label="'0'">允许评论
                            </el-checkbox>
                        </el-form-item>
                    </div>
                </div>
            </el-aside>
        </el-container>
        <el-form-item class="text-center">
            <el-button type="primary" @click.stop.prevent="saveForm('baseContentForm')">保存</el-button>
            <el-button @click.stop.prevent="goHistory">返回</el-button>
        </el-form-item>

    </el-form>


    <el-dialog title="相关推荐" :visible.sync="relateRecommendVisible"
               :before-close="handleCloseDialog" width="70%">
        <recommendation v-if="relateRecommendVisible" url="/cms/cmsArticle/excellent/projectList"
                        :related-ids="relatedIds"
                        @change="handleChangeRelatedIds"></recommendation>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" @click="relateRecommendVisible = false">取 消</el-button>
        </div>
    </el-dialog>


    <el-dialog title="上传封面"
               width="440px"
               :close-on-click-modal="false"
               :visible.sync="dialogVisibleProPic"
               :before-close="handleChangeProPicClose">
        <e-pic-file v-model="uploadProPic" :disabled="isUpdating" @get-file="getExlProPicFile"></e-pic-file>
        <cropper-pic :img-src="uploadProPic" :disabled="isUpdating" ref="cropperPic"
                     :copper-params="{aspectRatio: 297/187}"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeProPicClose">取消
            </el-button>
            <el-button size="mini" :disabled="!proPicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateProPic">上传
            </el-button>
        </div>
    </el-dialog>


</div>

<script>

    'use strict';

    new Vue({
        mixins: [Vue.menuTreeMixin],
        el: '#app',
        data: function () {
            var cmsArticleForm =${fns: toJson(cmsArticle)}, cmsArticleData, proModel = ${fns: toJson(proModelMap)}, relationList = ${fns: toJson(relationList)} || [];
            var contentTypes = JSON.parse('${fns: toJson(fns: getDictList('0000000271'))}');
            cmsArticleForm = Object.assign({}, cmsArticleForm);
            cmsArticleData = cmsArticleForm.cmsArticleData || {};
            var relatedIds = [];
            relatedIds = cmsArticleData.relation ? cmsArticleData.relation.split(',') : [];
            return {
                publishStatuses: [{label: '发布', value: '2'}, {label: '未发布', value: '1'}],
                cmsModuleList: [],
                baseContentForm: {
                    id: cmsArticleForm.id || '',
                    prId: cmsArticleForm.prId || '',
                    categoryId: '',
                    categories: [],
                    module: cmsArticleForm.module || '',
                    title: cmsArticleForm.title || '',
                    keywords: cmsArticleForm.keywords || '',
                    link: cmsArticleForm.link || '',
                    isshowlink: cmsArticleForm.isshowlink || '0',
                    cmsArticleData: {
                        id: cmsArticleData.id || '',
                        allowComment: cmsArticleData.allowComment || '0',
                        content: cmsArticleData.content || '',
                        copyfrom: cmsArticleData.copyfrom || '',
                        isshowcopyfrom: cmsArticleData.isshowcopyfrom || '0',
                        relation: cmsArticleData.relation || ''
                    },
                    thumbnail: cmsArticleForm.thumbnail || '',
                    articlepulishDate: cmsArticleForm.articlepulishDate || moment('${systemDate}').format('YYYY-MM-DD HH:mm:ss'),
                    sort: cmsArticleForm.sort || '1',
                    publishStatus: cmsArticleForm.publishStatus || '1',
                    validDate: [],
                    publishStartdate: cmsArticleForm.publishStartdate || '',
                    publishEnddate: cmsArticleForm.publishEnddate || '',
                    publishhforever: cmsArticleForm.publishhforever || '0',
                    top: cmsArticleForm.top || '0',
                    deadline: cmsArticleForm.deadline || '',
                    posid: cmsArticleForm.posid || '0'
                },
                contentTypes: contentTypes,
                dialogVisibleProPic: false,
                uploadProPic: '',
                isUpdating: false,
                proPicFile: null,
                inputVisible: false,
                inputValue: '',
                relateRecommendVisible: false,
                categoryList: [],
                dynamicTags: cmsArticleForm.keywords ? cmsArticleForm.keywords.split(',') : [],
                recommendTableList: [],
                relatedIds: relatedIds,
                menuList: [],
                menuTree: [],
                UEditor: null,
                category: null,
                relationList: relationList,
                publishEpPickerOptions: {
                    disabledDate: function(time) {
                        return time.getTime() < Date.now() - 24 * 60 * 60 * 1000;
                    }
                }
            }
        },
        watch: {
            'baseContentForm.validDate': function (value) {
                if (!value || value.length < 1 || !value[0] || !value[1]) {
                    this.baseContentForm.publishStartdate = '';
                    this.baseContentForm.publishEnddate = '';
                    return false;
                }
                this.baseContentForm.publishStartdate = moment(value[0]).format('YYYY-MM-DD HH:mm:ss');
                this.baseContentForm.publishEnddate = moment(value[1]).format('YYYY-MM-DD HH:mm:ss');
            },
            'baseContentForm.top': function () {
                this.baseContentForm.deadline = '';
            },
            relationList: function (value) {
                this.baseContentForm.cmsArticleData.relation = value.map(function (item) {
                    return item.id
                }).join(',');
                this.relatedIds = value.map(function (item) {
                    return item.id
                });
            },
            'baseContentForm.categories': function (value) {
                if (value && value.length > 0) {
                    this.baseContentForm.categoryId = value[value.length - 1];
                    this.category = this.getCategory(); //重新获取categroy对象
                }
            },
            dynamicTags: function (value) {
                this.baseContentForm.keywords = value.join(',');
            }
        },
        computed: {
            categoryEntries: function () {
                return this.getEntries(this.categoryList, {label: 'name', value: 'id'})
            },
            publishStatusEntries: {
                get: function () {
                    return this.getEntries(this.publishStatuses)
                }
            },
            baseContentFormRules: {
                get: function () {
                    var baseContentForm = this.baseContentForm;
                    var top = baseContentForm.top;
                    var isPublishRequired = baseContentForm.publishStatus == '1' && baseContentForm.publishhforever == '0' && baseContentForm.validDate.length == '0';

                    return {
//                        categories: [{required: true, message: '请选择归属栏目', trigger: 'change'}],
//                        module: [{required: true, message: '请选择栏目模块', trigger: 'change'}],
                        cmsArticleData: {
                            content: [
                                {required: true, message: '请输入内容', trigger: 'change'}
                            ]
                        },
                        link: [
                            {required: baseContentForm.isshowlink == '1', message: '请输入标题链接', trigger: 'blur'},
                            {max: 2000, message: '请输入大不于2000位字符', trigger: 'blur'}
                        ],
                        articlepulishDate: [
                            {required: true, message: '请选择文章发表时间', trigger: 'change'}
                        ],
                        validDate: [
                            {required: isPublishRequired, message: '请选择发布有效期或者永久', trigger: 'change'}
                        ],
                        publishhforever: [
                            {required: isPublishRequired, message: '请选择发布有效期或者永久', trigger: 'change'}
                        ],
                        deadline: [
                            {required: top == '1', message: '请选择过期时间', trigger: 'change'}
                        ]
                    }

                }
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
            }
        },
        methods: {
            getCategory: function () {
                if (!this.baseContentForm.categoryId) {
                    return null;
                }
                return this.menuEntries[this.baseContentForm.categoryId.toString()]
            },

            getCategoryList: function () {
                var self = this;
                this.$axios.get('/cms/category/cmsCategoryList?publishCategory=0000000286').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        data = data.data;
                        if (data) {
                            self.categoryList = data.list || [];
                            self.baseContentForm.categoryId = data.list[0].id;
//                            self.menuList = list.filter(function (item) {
//                                return item.parentIds.split(',').length < 3
//                            });
//                            self.setFlattenMenuList([].concat(self.menuList))
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
            saveForm: function (formName) {
                var self = this;
                this.baseContentForm.cmsArticleData.content = this.UEditor.getContent();
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        var baseContentForm = JSON.parse(JSON.stringify(self.baseContentForm));
                        delete baseContentForm.categories;
                        delete baseContentForm.validDate;
                        baseContentForm.sort = baseContentForm.sort.toString();
                        self.$axios({
                            method: 'POST',
                            url: '/cms/cmsArticle/excellent/projectSave',
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
                                    location.href = self.frontOrAdmin + '/cms/excellent/projectList'
                                }).catch(function () {

                                })
                            } else {
                                self.$message({
                                    message: !isResponseSuccess ? '保存失败' : '保存成功',
                                    type: !isResponseSuccess ? 'error' : 'success'
                                });
                            }
                        }).catch(function () {
                            self.$message({
                                message: '请求失败',
                                type: 'error'
                            })
                        })
                    }
                })
            },
            handleChangeRelatedIds: function (data) {
                var self = this;
                this.relateRecommendVisible = false;
                this.relatedIds = data.relatedIds;
                this.baseContentForm.cmsArticleData.relation = this.relatedIds.join(',');
                if (this.relatedIds.length == 0) {
                    this.relationList = [];
                    return false;
                }
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsArticle/getRelationList?ids=' + this.baseContentForm.cmsArticleData.relation
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.relationList = data.data;
                    } else {
                        self.$message({
                            message: '推荐失败',
                            type: 'error'
                        })
                    }
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                });
            },
            changePublishStatus: function () {
                this.baseContentForm.validDate = [];
                this.baseContentForm.publishStartdate = '';
                this.baseContentForm.publishEnddate = '';
                this.baseContentForm.publishhforever = '0';
            },
            handleChangePublishEp: function (value) {
                this.baseContentForm.publishhforever = '0';
            },

            handleChangePbF: function () {
                this.baseContentForm.validDate = [];
            },

            handleChangeProPicClose: function () {
                this.proPicFile = null;
                this.dialogVisibleProPic = false;
            },
            updateProPic: function () {
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
                formData.append('upfile', this.proPicFile);
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
                    self.handleChangeProPicClose();
                }).catch(function (error) {
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                })
            },


            getExlProPicFile: function (file) {
                this.proPicFile = file;
            },
            handleChangeExlProPicOpen: function () {
                var thumbnail = this.baseContentForm.thumbnail;
                this.dialogVisibleProPic = true;
                this.$nextTick(function (_) {
                    this.uploadProPic = this.addFtpHttp(thumbnail) || '/img/video-default.jpg';
                })
            },


            handleTagClose: function (tag) {
                this.dynamicTags.splice(this.dynamicTags.indexOf(tag), 1);
            },
            handleInputConfirm: function () {
                var inputValue = this.inputValue;
                if (inputValue) {
                    this.dynamicTags.push(inputValue);
                }
                this.inputVisible = false;
                this.inputValue = '';
            },
            showInput: function () {
                this.inputVisible = true;
                this.$nextTick(function () {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },
            handleCloseDialog: function () {
                this.relateRecommendVisible = false;
            },
            cancelRecommend: function (id) {
                for (var i = 0; i < this.relationList.length; i++) {
                    if (this.relationList[i].id == id) {
                        this.relationList.splice(i, 1);
                        break;
                    }
                }
            },
            getCategoryModelList: function () {
                var self = this;
                this.$axios.get('/cms/cmsArticle/categoryModel?modelParam=project').then(function (response) {
                    var cmsModuleList = response.data.data;
                    if (cmsModuleList) {
                        self.cmsModuleList = cmsModuleList || [];
                        self.baseContentForm.module = self.cmsModuleList[0].value;
                    }
                })
            },
            configUeditor: function () {
                var frontOrAdmin = this.frontOrAdmin;
                UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
                UE.Editor.prototype.getActionUrl = function (action) {
                    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo'
                            || action == 'uploadfile' || action == 'catchimage' || action == 'listimage' || action == 'listfile') {
                        return frontOrAdmin + '/ftp/ueditorUpload/uploadTempFormal?folder=cmsArticle';
                    } else {
                        return this._bkGetActionUrl.call(this, action);
                    }
                }
            },
            goHistory:function () {
                window.history.go(-1);
            }
        },
        created: function () {
            if (this.baseContentForm.publishStartdate) {
                this.baseContentForm.validDate = [this.baseContentForm.publishStartdate, this.baseContentForm.publishEnddate];
            }
        },
        beforeMount: function () {
            this.getCategoryList();
        },
        mounted: function () {
            this.UEditor = UE.getEditor('excellentProContent');
            this.getCategoryModelList();
            this.configUeditor()
        }
    })

</script>

</body>
</html>