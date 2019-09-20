<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script type="text/javascript" src="${ctxJs}/components/recommendation/recommendation.js"></script>

    <style>
        .main-left {
            /*border: 1px solid #f1f1f1;*/
            /*border-radius: 5px;*/
            border-right: 1px solid #f1f1f1;
            overflow: hidden;
            margin-right: 20px;
            margin-bottom: 20px;
            padding-top: 20px;
        }

        .aside-right {
            /*border: 1px solid #f1f1f1;*/
            /*border-radius: 5px;*/
            padding-top: 20px;
            overflow: hidden;
        }

        .operate-state {
            font-weight: bold;
            font-size: 14px;
            padding: 5px 0 5px 15px;
            border-top: 1px solid #f1f1f1;
            border-bottom: 1px solid #f1f1f1;
            margin-bottom: 20px;
        }

        .el-checkbox__label {
            font-size: 12px;
        }

        .item-bottom .el-form-item--mini.el-form-item {
            margin-bottom: 5px;
        }


    </style>

<body>



<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar f-title="优秀项目" second-name="修改"></edit-bar>
    </div>
    <el-form :model="baseContentForm" ref="baseContentForm" :rules="baseContentFormRules" size="mini"
             label-width="90px">

        <el-container>
            <el-main class="main-left">
                <el-row :gutter="10">
                    <el-col :xs="24" :lg="12">
                        <el-form-item prop="categories" label="归属栏目：">
                            <el-cascader :options="menuTree" :show-all-levels="false"
                                         :props="{label: 'name', children: 'children', value: 'id'}"
                                         v-model="baseContentForm.categories"></el-cascader>
                            <span v-if="contentTypeName" style="font-size: 12px; margin-left: 8px;" class="empty-color">内容内型：{{contentTypeName}}</span>
                        </el-form-item>
                    </el-col>
                    <el-col :xs="24" :lg="12">
                        <el-form-item prop="module" label="栏目模块：">
                            <%--<el-select v-model="baseContentForm.module" placeholder="请选择栏目模块">--%>
                            <%--<el-option v-for="item in cmsModuleList" :key="item.value" :label="item.label"--%>
                            <%--:value="item.value"></el-option>--%>
                            <%--</el-select>--%>
                            <div class="el-form-item-content_static">
                                {{baseContentForm.module | selectedFilter(cmsModuleEntries)}}
                            </div>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="项目名称：">
                    <div class="el-form-item-content_static">{{baseContentForm.projectName}}</div>
                </el-form-item>
                <%--<el-row :gutter="10">--%>
                    <%--<el-col :span="12">--%>
                        <%--<el-form-item prop="video" label="视频展示：">--%>
                            <%--<input type="hidden" v-model="baseContentForm.video">--%>

                        <%--</el-form-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="12">--%>
                        <%--<el-form-item prop="img" label="图片展示：">--%>
                            <%--<input type="hidden" v-model="baseContentForm.img">--%>
                            <%--<el-upload--%>
                                    <%--class="cover-uploader"--%>
                                    <%--action="${ctx}/ftp/ueditorUpload/cutImg?folder=temp/site&x=0&y=0&width=200&height=100"--%>
                                    <%--:show-file-list="false"--%>
                                    <%--:on-success="handleImgSuccess"--%>
                                    <%--accept="image/jpeg"--%>
                                    <%--name="upfile">--%>
                                <%--<img v-for="item in imgShows" :key="item.uid" :src="item.ftpUrl | ftpHttpFilter(ftpHttp)"--%>
                                     <%--class="cover-uploader_cover" style="border-radius: 6px;">--%>
                                <%--<i v-if="imgShows.length == 0" class="el-icon-plus cover-uploader-icon"></i>--%>
                            <%--</el-upload>--%>
                        <%--</el-form-item>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
                <%--<el-form-item prop="dynamicTags" label="关键词：">--%>
                    <%--<el-tag--%>
                            <%--:key="tag"--%>
                            <%--v-for="tag in baseContentForm.dynamicTags"--%>
                            <%--size="medium"--%>
                            <%--closable--%>
                            <%--:disable-transitions="false"--%>
                            <%--@close="handleTagClose(tag)">--%>
                        <%--{{tag}}--%>
                    <%--</el-tag>--%>
                    <%--<el-input style="width:100px;"--%>
                              <%--class="input-new-tag"--%>
                              <%--v-if="inputVisible"--%>
                              <%--v-model="inputValue"--%>
                              <%--ref="saveTagInput"--%>
                              <%--size="mini"--%>
                              <%--@keyup.enter.native="handleInputConfirm"--%>
                              <%--@blur="handleInputConfirm"--%>
                    <%-->--%>
                    <%--</el-input>--%>
                    <%--<el-button v-else class="button-new-tag" size="mini" @click="showInput">+ New Tag</el-button>--%>
                <%--</el-form-item>--%>
                <div class="base-content-separate-line"></div>
                <el-row :gutter="10">
                    <el-col :span="12">
                        <el-form-item prop="link" label="标题链接：">
                            <el-input v-model="baseContentForm.link"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="8">
                        <el-form-item prop="hasLink" label-width="0">
                            <el-checkbox v-model="baseContentForm.hasLink" :true-label="1" :false-label="0">添加</el-checkbox>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-form-item label="是否显示：">
                    <el-checkbox v-model="baseContentForm.hasDuty" :true-label="1" :false-label="0">负责人</el-checkbox>
                    <el-checkbox v-model="baseContentForm.hasTeacher" :true-label="1" :false-label="0">指导老师</el-checkbox>
                    <el-checkbox v-model="baseContentForm.hasComeFrom" :true-label="1" :false-label="0">项目来源</el-checkbox>
                </el-form-item>
                <el-form-item prop="relateRecommendIds" label="相关推荐：">
                    <input type="hidden" v-model="baseContentForm.relateRecommendIds">
                    <div class="table-container" v-show="recommendTableList.length > 0" style="margin-bottom:20px;">
                        <el-table :data="recommendTableList" size="mini" class="table" ref="recommendTableList"
                                  style="margin-bottom: 0">
                            </el-table-column>
                            <el-table-column label="项目名称" prop="name" align="center"></el-table-column>
                            <el-table-column label="所属栏目" prop="module" align="center"></el-table-column>
                            <el-table-column label="到期时间" prop="viewCount" align="center">
                            </el-table-column>
                            <el-table-column label="发布时间" prop="publishDate" align="center">
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
                    <el-button type="primary" @click.stop.prevent="relateRecommendVisible = true">添加相关</el-button>
                </el-form-item>
                <%--<el-form-item prop="files" label="附件：">--%>
                <%--<el-upload action="${ctx}/ftp/ueditorUpload/uploadTemp?folder=project"--%>
                <%--:show-file-list="true"--%>
                <%--:file-list="baseContentForm.files"--%>
                <%--name="upfile">--%>
                <%--<el-button size="mini" type="primary">点击上传</el-button>--%>
                <%--</el-upload>--%>
                <%--</el-form-item>--%>
                <el-form-item prop="content" label="项目展示：">
                    <script id="excellentProContent" name="content" type="text/plain" style="width:100%;height:600px">
                    {{baseContentForm.content}}
                    </script>
                </el-form-item>
            </el-main>


            <el-aside width="310px">
                <div class="aside-right">
                    <el-form-item prop="video" label="视频：" label-width="80px">
                        <input type="hidden" v-model="baseContentForm.video">
                        <div v-if="baseContentForm.video" class="video-preview" style="width: 200px; height: 100px;">
                            <video style="width: 100%; height: 100%" controls>
                                <source :src="baseContentForm.video | ftpHttpFilter(ftpHttp)">
                            </video>
                        </div>
                        <el-upload
                                class="upload-demo"
                                :action="frontOrAdmin+'/ftp/ueditorUpload/upload'"
                                :show-file-list="false"
                                name="upfile"
                                :on-success="uploadVideoSuccess"
                                :on-error="uploadVideoError"
                        <%--:on-preview="handlePreview"--%>
                        <%--:on-remove="handleRemove"--%>
                        <%--:before-remove="beforeRemove"--%>
                                accept="video/flv,video/swf,video/mkv,video/avi,video/mp4,video/rmvb,video/rm"
                        <%--:on-exceed="handleExceed"--%>
                                :file-list="videoList">
                            <el-button size="mini" type="primary">点击上传</el-button>
                            <div slot="tip" class="el-upload__tip">只能上传flv,swf,mkv,avi,rm,rmvb,mp4文件</div>
                        </el-upload>
                    </el-form-item>
                    <el-form-item prop="pic" label="封面：" label-width="80px">
                        <input type="hidden" name="pic" :value="baseContentForm.pic"/>
                        <div v-show="baseContentForm.pic" style="width: 200px; height: 125px; overflow: hidden">
                            <img class="img-responsive" :src="baseContentForm.pic | ftpHttpFilter(ftpHttp) | defaultProPic" style="width: 200px; height: 125px;">
                        </div>
                        <el-button type="primary" size="mini" @click.stop.prevent="handleChangeExlProPicOpen">更换封面
                        </el-button>

                    </el-form-item>
                    <el-form-item prop="publishDate" label="发布时间：" label-width="80px">
                        <el-date-picker v-model="baseContentForm.publishDate" type="date" value-format="yyyy-MM-dd"
                                        style="width:130px"></el-date-picker>
                    </el-form-item>
                    <el-form-item prop="sort" label="排序：" label-width="80px">
                        <el-input-number v-model="baseContentForm.sort" :min="1" style="width:120px;"></el-input-number>
                    </el-form-item>
                    <div class="item-bottom">
                        <p class="operate-state">状态</p>
                        <el-form-item prop="hasPublish" label-width="10px">
                            <el-checkbox v-model="baseContentForm.hasPublish" :true-label="1" :false-label="0">发布</el-checkbox>
                        </el-form-item>
                        <el-form-item label="有效期：" label-width="93px" v-show="baseContentForm.hasPublish == '1'">
                            <el-date-picker :disabled="baseContentForm.isForever == '1'" value-format="yyyy-MM-dd"
                                            v-model="validDate"
                                            type="daterange"
                                            align="right"
                                            unlink-panels
                                            range-separator="至"
                                            start-placeholder="开始日期"
                                            end-placeholder="结束日期"
                                            :picker-options="validDatePickerOptions"
                                            style="width:202px;">
                            </el-date-picker>
                        </el-form-item>
                        <el-form-item prop="isForever" label-width="34px" v-show="baseContentForm.hasPublish == '1'">
                            <el-checkbox v-model="baseContentForm.isForever" :true-label="1" :false-label="0"
                                         :disabled="validDate && validDate.length > 0">永久
                            </el-checkbox>
                        </el-form-item>

                        <el-form-item prop="hasTop" label-width="10px">
                            <el-checkbox v-model="baseContentForm.hasTop" :true-label="1" :false-label="0">置顶</el-checkbox>
                        </el-form-item>
                        <el-form-item prop="overDate" label="过期时间：" label-width="107px" v-show="baseContentForm.hasTop == '1'">
                            <el-date-picker style="width:130px;" value-format="yyyy-MM-dd"
                                            v-model="baseContentForm.overDate"
                                            type="date"
                                            placeholder="选择日期">
                            </el-date-picker>
                        </el-form-item>

                        <el-form-item prop="hasIndex" label-width="10px">
                            <el-checkbox v-model="baseContentForm.hasIndex" :true-label="1" :false-label="0">推荐首页</el-checkbox>
                        </el-form-item>
                        <%--<el-form-item prop="recommendWeight" label="首页排序：" label-width="107px">--%>
                        <%--<el-input-number v-model="baseContentForm.recommendWeight" :min="1"--%>
                        <%--style="width:120px;"></el-input-number>--%>
                        <%--</el-date-picker>--%>
                        </el-form-item>
                        <el-form-item prop="hasComment" label-width="10px">
                            <el-checkbox v-model="baseContentForm.hasComment" :true-label="1" :false-label="0">允许评论</el-checkbox>
                        </el-form-item>
                    </div>
                </div>
            </el-aside>
        </el-container>
        <el-form-item class="text-center">
            <el-button type="primary" @click.stop.prevent="saveForm('baseContentForm')">保存</el-button>
            <el-button type="primary">预览</el-button>
            <el-button>返回</el-button>
        </el-form-item>

    </el-form>


    <el-dialog title="相关推荐" :visible.sync="relateRecommendVisible"
               :before-close="handleCloseDialog" width="70%">
        <recommendation v-if="relateRecommendVisible" @recommend="recommend" @cancelrecommend="batchCancelRecommend"
                        :related-ids="baseContentForm.relateRecommendIds"></recommendation>
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
            <%--var cmsModuleList = JSON.parse('${fns: toJson(fns: getDictList('0000000274'))}');--%>
            var contentTypes = JSON.parse('${fns: toJson(fns: getDictList('0000000271'))}');
            return {
                publishStatuses: [{label: '发布', value: '2'}, {label: '未发布', value: '1'}],
                cmsModuleList:[],
                contentTypes: contentTypes,
                baseContentForm: {
                    id:'',
                    categoryId:'',
                    categories: [],
                    module: '',
                    type: '',
                    projectName: '结果改好了公开课',
//                    dynamicTags: ['好看', 'smart', 'ok'],
                    pic: '',
                    hasLink: 1,
                    link: '',
                    publishDate: '',
                    startDate: '',
                    endDate: '',
                    sort: '',
                    hasDuty: 1,
                    hasTeacher: 0,
                    hasComeFrom: 0,
                    isForever: 0,
                    relateRecommendIds: [],
                    hasPublish: 0,
                    hasTop: 0,
                    hasIndex: 0,
                    hasComment: 0,
                    overDate: '',
                    content: '',
                    img:'',
                    video:''
                },
                dialogVisibleProPic:false,
                uploadProPic:'',
                isUpdating: false,
                proPicFile:null,
                imgShows:[],
                inputVisible: false,
                inputValue: '',
                baseContentFormRules: {
                    categories: [{required: true, message: '请选择归属栏目', trigger: 'blur'}],
                    module: [{required: true, message: '请选择栏目模块', trigger: 'blur'}]
                },
                validDatePickerOptions: {
                    shortcuts: [{
                        text: '最近一周',
                        onClick: function (picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近一个月',
                        onClick: function (picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                            picker.$emit('pick', [start, end]);
                        }
                    }, {
                        text: '最近三个月',
                        onClick: function (picker) {
                            const end = new Date();
                            const start = new Date();
                            start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                            picker.$emit('pick', [start, end]);
                        }
                    }]
                },
                relateRecommendVisible: false,
//                categoriesList: [],
                categoryList:[],
                validDate: [],
                recommendTableList: [],
                menuList:[],
                menuTree: [],
                UEditor: null,
                videoList: []

            }
        },
        watch: {
            validDate: function (value) {
                value = value || [];
                this.baseContentForm.startDate = value[0];
                this.baseContentForm.endDate = value[1];
            },
            imgShows:function (value) {
                this.baseContentForm.img = value.map(function (item) {
                    return item.ftpUrl
                }).join(',');
            },
            recommendTableList: function (value) {
                this.baseContentForm.relateRecommendIds = value.map(function (item) {
                    return item.id
                });
            },
            videoList: function (value) {
                this.baseContentForm.video = value && value.length > 0 ? value[0].ftpUrl : '';
            }

        },
        computed: {
            publishStatusEntries: {
                get: function () {
                    return this.getEntries(this.publishStatuses)
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
            },
        },
        methods: {
            getCategoryList: function () {
                var self = this;
                this.$axios.get('/cms/category/cmsCategoryList').then(function (response) {
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
                    if(!parent){
                        break;
                    }
                    result.unshift(parentId);
                    parentId = parent.parentId;

                }
                return result
            },
            handleChangeCategory:function () {
                var len = this.baseContentForm.categories.length;
                this.baseContentForm.categoryId = this.baseContentForm.categories[len-1];
            },
            getDataList: function () {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: ''
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        Object.assign(self.baseContentForm, data.data);
                        self.setImgShowss(self.baseContentForm.img);
                        self.setVideoList(self.baseContentForm.video);
                    }
                }).catch(function () {
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                });
            },
            saveForm:function (formName) {
                var self = this;
                this.$refs[formName].validate(function (valid) {
                    if(valid){

                    }
                })
            },
            beforeBaseContentCoverUpload: function () {

            },
            setImgShowss: function (value) {
                if (!value) {
                    return;
                }
                this.imgShows.push({
                    ftpUrl: value
                });
            },
            setVideoList: function (value) {
                if (!value) {
                    return;
                }
                this.videoList.push({
                    ftpUrl: value
                });
            },
            handleImgSuccess:function (response, file, fileList) {
                var nfile = Object.assign(file, response);
                if (response.state === 'SUCCESS') {
                    this.imgShows = fileList.slice(-1);
                } else {
                    this.$message({
                        message: '上传失败',
                        type: 'error'
                    });
                }
            },
            uploadVideoSuccess: function (respsonse, file, fileList) {
                if (respsonse.state === 'SUCCESS') {
                    this.videoList.splice(0, 1, Object.assign(file, respsonse));
                }
                this.$message({
                    type:  respsonse.state === 'SUCCESS' ? 'success' : 'error',
                    message: respsonse.state === 'SUCCESS' ? '上传成功' : '上传失败'
                })
            },

            uploadVideoError: function (err, file, fileList) {
                this.$message({
                    type:  'error',
                    message: '上传失败'
                })
            },

            handleChangeProPicClose:function () {
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
//                        self.moveFile(data.ftpUrl);
                        self.isUpdating = false;
                        self.baseContentForm.pic = data.ftpUrl;
                        self.handleChangeProPicClose();
                    }
                }).catch(function (error) {

                })
            },


            getExlProPicFile: function (file) {
                this.proPicFile = file;
            },
            handleChangeExlProPicOpen: function () {
                var pic = this.baseContentForm.pic;
                this.dialogVisibleProPic = true;
                this.$nextTick(function (_) {
                    this.uploadProPic = this.addFtpHttp(pic) || '/img/video-default.jpg';
                })
            },


            handleTagClose: function (tag) {
                this.baseContentForm.dynamicTags.splice(this.baseContentForm.dynamicTags.indexOf(tag), 1);
            },
            handleInputConfirm: function () {
                var inputValue = this.inputValue;
                if (inputValue) {
                    this.baseContentForm.dynamicTags.push(inputValue);
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
            handleCloseDialog:function () {
                this.relateRecommendVisible = false;
            },
            recommend: function (obj) {
                this.relateRecommendVisible = false;
                var selections = obj.selections || [];
                if (this.recommendTableList.length > 0) {
                    for (var i = 0; i < selections.length; i++) {
                        if (this.baseContentForm.relateRecommendIds.indexOf(selections[i].id) == -1) {
                            this.recommendTableList.push(selections[i]);
                        }
                    }
                } else {
                    this.recommendTableList = selections;
                }
            },
            batchCancelRecommend: function (obj) {
                this.relateRecommendVisible = false;
                var ids = obj.ids;
                if (this.recommendTableList.length > 0) {
                    for (var i = 0; i < this.recommendTableList.length; i++) {
                        for (var j = 0; j < ids.length; j++) {
                            if (this.recommendTableList[i].id == ids[j]) {
                                this.recommendTableList.splice(i, 1);
                            }
                        }
                    }
                }
            },
            cancelRecommend: function (id) {
                for (var i = 0; i < this.recommendTableList.length; i++) {
                    if (this.recommendTableList[i].id == id) {
                        this.recommendTableList.splice(i, 1);
                        break;
                    }
                }
            },
            getCategoryModelList: function () {
                var self = this;
                this.$axios.get('/cms/cmsArticle/categoryModel?modelParam=hot').then(function (response) {
                    var cmsModuleList = response.data.data;
                    if (cmsModuleList) {
                        self.cmsModuleList = cmsModuleList || [];
                        self.baseContentForm.module = self.cmsModuleList[0].value;
                    }
                })
            }
        },
        created: function () {
            this.getDataList();
        },
        beforeMount: function () {
            this.getCategoryList();
        },
        mounted: function () {
            this.UEditor = UE.getEditor('excellentProContent')
            this.getCategoryModelList();
        }
    })

</script>

</body>
</html>