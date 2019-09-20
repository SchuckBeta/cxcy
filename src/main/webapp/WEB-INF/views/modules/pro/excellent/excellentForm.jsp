<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <!-- 配置文件 -->
    <!-- <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script> -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <!-- <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script> -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
</head>

<body>
<div id="app" class="container-fluid mgb-60" v-show="pageLoad" style="display: none">
    <div class="mgb-20">
        <edit-bar second-name="展示"></edit-bar>
    </div>
    <el-form :model="excellentProForm" :rules="excellentProRules" size="mini" ref="excellentProForm" label-width="120px"
             class="excellent-pro-form" modelAttribute="es" save="save" :action="actionUrl" :target="formTarget"
             method="post">
        <input type="hidden" name="id" :value="excellentProForm.id"/>
        <input type="hidden" name="foreignId" :value="excellentProForm.foreignId"/>
        <input type="hidden" name="type" :value="excellentProForm.type"/>
        <input type="hidden" name="subType" :value="excellentProForm.subType"/>
        <input type="hidden" name="managed" :value="excellentProForm.managed"/>
        <input type="hidden" name="isRelease" value="1"/>
        <div class="exl-pro-form-header">
            <h3 class="title">{{projectInfo.name}}</h3>
            <el-row :gutter="20" class="exl-pro-row" label-width="120px">
                <el-col :span="8">
                    <div class="excellent-pro-pic">
                        <input type="hidden" name="coverImg" :value="excellentProForm.coverImg"/>
                        <img :src="excellentProForm.coverImg | ftpHttpFilter(ftpHttp) | defaultProPic">
                        <div class="exl-btn-upload-box">
                            <el-button type="primary" size="mini" @click.stop.prevent="handleChangeExlProPicOpen">更换封面
                            </el-button>
                        </div>
                    </div>
                </el-col>
                <el-col :span="16">
                    <e-col-item label="所属学院：">{{projectInfo.oname}}</e-col-item>
                    <e-col-item label="指导老师：">
                        <template v-for="teacher in projectTeacherList">
                            <div class="pro-teacher-item" v-if="teacher"><span>{{teacher.uname}}</span><span>{{teacher.oname}}</span><span>{{teacher.post_title}}</span>
                            </div>
                        </template>
                    </e-col-item>
                    <e-col-item label="项目来源：">{{excellentProForm.subType | selectedFilter(projectSourceEntries)}}
                    </e-col-item>
                    <e-col-item label="关键词：">
                        <el-tag
                                :key="keyword"
                                v-for="(keyword, index) in excellentProForm.keywords"
                                closable
                                size="medium"
                                type="info"
                                :disable-transitions="false"
                                @close="handleDeleteKeyword(index)">
                            <input type="hidden" name="keywords" :value="keyword">
                            {{keyword}}
                        </el-tag>
                        <div class="input-new-keyword-box" v-if="inputNewKeywordVisible">
                            <el-form-item size="mini" prop="keyword" label-width="0">
                                <el-input
                                        class="input-new-keyword"
                                        v-model="excellentProForm.keyword"
                                        ref="saveKeywordInput"
                                        @keyup.enter.native="handleInputConfirm"
                                        @blur="handleInputConfirm"
                                >
                                </el-input>
                            </el-form-item>
                        </div>
                        <el-button v-else class="button-new-tag" size="mini" @click="showKeywordInput">+ 新关键字
                        </el-button>
                    </e-col-item>
                </el-col>
            </el-row>
        </div>

        <div class="exl-pro--content">
            <el-form-item label="页面模板：" prop="excTemp">
                <input type="hidden" name="temp" :value="excellentProForm.excTemp">
                <el-select v-model="excellentProForm.excTemp" clearable @change="handleChangeExcTemp"
                           placeholder="--选择布局模板--">
                    <el-option
                            v-for="item in excTemplateList"
                            :key="item.value"
                            :label="item.name"
                            :value="item.value">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="页面内容：" prop="content">
                <script id="excellentProContent" name="content" type="text/plain" style="width:800px;height:600px">
                    {{excellentProForm.content}}






                </script>
            </el-form-item>
            <el-form-item label="页面内容：">
                <el-radio-group v-model="excellentProForm.isTop">
                    <el-radio v-for="yesNo in yesNoList" name="isTop" :key="yesNo.value" :label="yesNo.value">
                        {{yesNo.label}}
                    </el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="是否置顶：">
                <el-radio-group v-model="excellentProForm.isComment">
                    <el-radio v-for="yesNo in yesNoList" name="isComment" :key="yesNo.value" :label="yesNo.value">
                        {{yesNo.label}}
                    </el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item>
                <el-button type="primary" :disabled="isPublish" @click.stop.prevent="goToPreview">预览</el-button>
                <el-button type="primary" :disabled="isPublish" @click.stop.prevent="publishExcPro">发布</el-button>
                <el-button type="default" :disabled="isPublish" @click.stop.prevent="goToBack">返回</el-button>
            </el-form-item>
        </div>
    </el-form>

    <el-dialog title="上传图像"
               width="440px"
               :visible.sync="dialogVisibleExlProPic"
               :before-close="handleChangeExlProPicClose">
        <e-pic-file v-model="uploadExlProPic" :disabled="isUpdating" @get-file="getExlProPicFile"></e-pic-file>
        <cropper-pic :img-src="uploadExlProPic" :disabled="isUpdating" ref="cropperPic"
                     :copper-params="{aspectRatio: ''}"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeExlProPicClose">取消
            </el-button>
            <el-button size="mini" :disabled="!exlProPicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateExlProPic">上传
            </el-button>
        </div>
    </el-dialog>

</div>

<script>

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var projectStyles = JSON.parse('${fns:toJson(fns: getDictList('project_style'))}');
            var competitionTypes = JSON.parse('${fns: toJson(fns: getDictList('competition_type'))}');
            var excTemplateList = JSON.parse(JSON.stringify(${fns: toJson(fns:getExcTemplateList())}));
            var excellentProVo = ${fns: toJson(es)};
            var front_url = JSON.parse('${fns: toJson(front_url)}');
            <%--var excellentPro = JSON.parse(JSON.stringify(${fns:toJson(excellentPro)}));--%>
//            var excellentProVo = excellentPro.excellentProVo;
            var projectInfo = ${fns: toJson(projectInfo)};
            var projectTeacherInfo = JSON.parse('${fns: toJson(projectTeacherInfo)}');
            var yesNoList = JSON.parse('${fns:toJson(fns:getDictList('yes_no'))}');
            var self = this;
            var validateKeywordSame = function (rule, value, callback) {
                var keywords = self.excellentProForm.keywords;
                if (value) {
                    for (var i = 0; i < keywords.length; i++) {
                        var keyword = keywords[i];
                        if (value === keyword) {
                            return callback(new Error('请输入不存在的关键词'))
                        }
                    }
                    return callback();
                }
                return callback()
            };

            return {
                excellentProForm: {
                    id: excellentProVo.id,
                    foreignId: excellentProVo.foreignId,
                    type: excellentProVo.type,
                    subType: excellentProVo.subType,
                    managed: excellentProVo.managed,
                    keywords: excellentProVo.keywords || [],
                    excTemp: '',
                    content: excellentProVo.content,
                    isTop: excellentProVo.isTop,
                    isComment: excellentProVo.isComment,
                    keyword: '',
                    coverImg: excellentProVo.coverImg
                },
                excellentProRules: {
                    keyword: [
                        {validator: validateKeywordSame}
                    ],
                    excTemp: [{required: false, message: '请选择布局模板', trigger: 'change'}]
                },
                projectInfo: projectInfo || {},
                projectTeacherList: projectTeacherInfo || [],
                projectStyles: projectStyles,
                competitionTypes: competitionTypes,
                inputNewKeywordVisible: false,
                excTemplateList: excTemplateList,
                UEditor: {},
                yesNoList: yesNoList,
                front_url: front_url,
                isPreview: false,
                dialogVisibleExlProPic: false,
                isUpdating: false,
                exlProPicFile: null,
                uploadExlProPic: '',
                isPublish: false
            }
        },
        computed: {
            isExcellentPro: {
                get: function () {
                    return this.excellentProForm.type === '0000000075';
                }
            },
            projectSources: {
                get: function () {
                    return this.isExcellentPro ? this.projectStyles : this.competitionTypes;
                }
            },
            projectSourceEntries: {
                get: function () {
                    return this.getEntries(this.projectSources)
                }
            },
            actionUrl: {
                get: function () {
                    return this.isPreview ? (this.front_url + '/frontExcellentPreview') : 'save'
                }
            },
            formTarget: {
                get: function () {
                    return this.isPreview ? '_target' : '_self';
                }
            }
        },
        methods: {

            handleChangeExlProPicClose: function () {
                this.exlProPicFile = null;
                this.dialogVisibleExlProPic = false;
            },

            updateExlProPic: function () {
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
                formData.append('upfile', this.exlProPicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/uploadTemp?folder=excellentCoverImg&x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
//                        self.moveFile(data.ftpUrl);
                        self.isUpdating = false;
                        self.excellentProForm.coverImg = data.ftpUrl;
                        self.handleChangeExlProPicClose();
                    }
                }).catch(function (error) {

                })
            },

            getExlProPicFile: function (file) {
                this.exlProPicFile = file;
            },

            handleChangeExlProPicOpen: function () {
                var coverImg = this.excellentProForm.coverImg;
                this.dialogVisibleExlProPic = true;
                this.$nextTick(function (_) {
                    this.uploadExlProPic = this.addFtpHttp(coverImg) || '/img/video-default.jpg';
                })
            },

            handleDeleteKeyword: function (index) {
                this.excellentProForm.keywords.splice(index, 1)
            },
            handleInputConfirm: function () {
                var self = this;
                this.$refs.excellentProForm.validateField('keyword', function (error) {
                    if (!error && self.excellentProForm.keyword != "") {
                        self.excellentProForm.keywords.push(self.excellentProForm.keyword);
                        self.excellentProForm.keyword = ''
                    }
                    if (!error) {
                        self.inputNewKeywordVisible = false;
                    }
                })
            },
            showKeywordInput: function () {
                this.inputNewKeywordVisible = true;
                this.$nextTick(function (_) {
                    this.$refs.saveKeywordInput.$refs.input.focus();
                });
            },

            handleChangeExcTemp: function (value) {
                var excTemplateList = this.excTemplateList;
                var content;
                for (var i = 0; i < excTemplateList.length; i++) {
                    var item = excTemplateList[i];
                    if (item.value === value) {
                        content = item.content;
                    }
                }
                if (!content) {
                    content = '';
                }
                this.UEditor.setContent(content);
            },

            goToBack: function () {
                return history.go(-1);
            },
            goToPreview: function () {
                this.isPreview = true;
                this.isPublish = true;
                this.$nextTick(function () {
                    this.$refs.excellentProForm.$el.submit();
                    this.isPublish = false;
                })
            },
            publishExcPro: function () {
                var self = this;
                var $elForm = $(this.$refs.excellentProForm.$el);
                this.isPreview = false;
                this.$refs.excellentProForm.validate(function (valid) {
                    if (valid) {
                        self.isPublish = true;
                        $elForm.ajaxSubmit(function (data) {
                            if (!self.checkUserLogin(data)) {
                                if (data.ret == '1') {
                                    self.excellentProForm.id = data.id;
                                }
                                self.show$message({
                                    status: data.ret == '1',
                                    msg: data.msg
                                })
                                self.isPublish = false;
                            }
                        });
                    }
                });
            }
        },
        mounted: function () {
            this.UEditor = UE.getEditor('excellentProContent')
            UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
	        UE.Editor.prototype.getActionUrl = function (action) {
	            if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo'
	                    || action == 'uploadfile' || action == 'catchimage' || action == 'listimage' || action == 'listfile') {
	                return $frontOrAdmin+'/ftp/ueditorUpload/uploadTempFormal?folder=oaNotice';
	            } else {
	                return this._bkGetActionUrl.call(this, action);
	            }
	        }
        }
    })

</script>

</body>