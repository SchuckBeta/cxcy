<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="creative"/>
</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>双创项目</el-breadcrumb-item>
        <el-breadcrumb-item>${proProject.projectName}</el-breadcrumb-item>
    </el-breadcrumb>
    <row-step-apply>
        <step-item is-complete>第一步（完善个人基本信息）</step-item>
        <step-item is-complete>第二步（填写项目基本信息）</step-item>
        <step-item>第三步（提交项目申报附件）</step-item>
    </row-step-apply>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{applyDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">${proModelType}申报</span>
            </div>
        </div>
        <div class="apply-form-body">
            <%--<div class="user-info-list">--%>
                <%--<el-row :gutter="15" label-width="120px">--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="姓名：">{{loginCurUser.name}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="学号：">--%>
                            <%--{{loginCurUser.no}}--%>
                        <%--</e-col-item>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
            <%--</div>--%>
            <el-form :model="proModelForm" ref="proModelForm" :rules="proModelRules" :disabled="disabled" size="mini"
                     label-width="120px">
                <el-form-item label="项目名称：" prop="pName">
                    <el-input v-model="proModelForm.pName" placeholder="最多128个字符" maxlength="128"></el-input>
                </el-form-item>
                <el-form-item label="项目简称：" prop="shortName">
                    <el-input v-model="proModelForm.shortName" class="w300" placeholder="最多15个字符"
                              maxlength="15"></el-input>
                </el-form-item>
                <el-form-item label="项目类别：" prop="proCategory">
                    <el-select v-model="proModelForm.proCategory">
                        <el-option v-for="item in projectStyles" :key="item.value" :value="item.value"
                                   :label="item.label"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="选择团队：" prop="teamId">
                    <el-tooltip content="请先选择项目类别" :disabled="!!proModelForm.proCategory" popper-class="white"
                                placement="right">
                        <el-select :disabled="!proModelForm.proCategory" v-model="proModelForm.teamId">
                            <el-option v-for="item in teamList" :key="item.id" :value="item.id"
                                       :label="item.name"></el-option>
                        </el-select>
                    </el-tooltip>
                        <template v-if="teamList.length === 0" style="display: none"><span
                                class="el-form-item-expository">暂无团队，请去<a
                                href="${ctxFront}/team/indexMyTeamList">创建团队</a>吧</span></template>
                </el-form-item>
                <update-member :declare-id="loginCurUser.id" :disabled="disabled" :team-id="proModelForm.teamId"
                               :operate-btn-show="false" :addable="false"
                               @change="handleChangeTeam"></update-member>
                <el-form-item label="项目Logo：" prop="logoUrl" class="common-upload-one-img">
                    <div class="upload-box-border site-logo-left-size">
                        <div class="avatar-uploader">
                            <div tabindex="0" class="el-upload el-upload--text" @click.stop="openDialogLogo">
                                <img :src="proModelForm.logoUrl | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                            </div>
                        </div>
                        <div v-show="proModelForm.logoUrl && !disabled" class="arrow-block-delete"
                             @click.stop="proModelForm.logoUrl = ''">
                            <i class="el-icon-delete"></i>
                        </div>
                    </div>
                </el-form-item>
                <el-form-item prop="introduction" label="项目简介：">
                    <el-input type="textarea" v-model="proModelForm.introduction" :rows="3" placeholder="最多2000个字符"
                              maxlength="2000"></el-input>
                </el-form-item>
                <el-form-item v-show="!isSubmited" label-width="0" class="text-center">
                    <el-button @click.stop="validateProModelForm('prev')">上一步</el-button>
                    <el-button type="primary" @click.stop="validateProModelForm">下一步</el-button>
                    <el-button type="primary" @click.stop="validateProModelForm('save')">保存</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
    <el-dialog title="上传项目Logo"
               width="440px"
               :visible.sync="dialogVisibleChangeLogo"
               :close-on-click-modal="false"
               :before-close="handleChangeLogoPicClose">
        <e-pic-file v-model="logoPic" :disabled="isUpdating" @get-file="getLogoPicFile"></e-pic-file>
        <cropper-pic :img-src="logoPic" default-img="${ctxImages}/default-pic.png" :disabled="isUpdating"
                     ref="cropperPic"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeLogoPicClose">取消</el-button>
            <el-button size="mini" :disabled="!logoPicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateUserPic">上传
            </el-button>
        </div>
    </el-dialog>
</div>


<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            var teamList = JSON.parse(JSON.stringify(${fns: toJson(teams)}));
            var self = this;
            var validateCheckSpSt = function (rules, value, callback) {
                return self.$axios.get('/sys/checkNotSpSteel?str=' + encodeURI(value))
            };

            var validatePName = function (rules, value, callback) {
                var proModelForm = self.proModelForm;
                var params = {
                    id: proModelForm.id,
                    actYwId: proModelForm.actYwId,
                    pName: proModelForm.pName
                };
                return self.$axios.get('/promodel/proModel/checkProName', {params: params})
            };

            var validatePNameAndSt = function (rules, value, callback) {
                return self.$axios.all([validateCheckSpSt(rules, value, callback), validatePName(rules, value, callback)])
                        .then(self.$axios.spread(function (spStrReq, nameReq) {
                            if (spStrReq.data.data && nameReq.data) {
                                return callback();
                            }
                            if (!spStrReq.data.data) {
                                return callback(new Error(spStrReq.data.msg));
                            }
                            return callback(new Error("项目名称已存在"));
                            // Both requests are now complete
                        }));
            };
            return {
                proModel: proModel,
                proModelForm: {
                    id: '',
                    actYwId: '',
                    year: '',
                    pName: '',
                    shortName: '',
                    proCategory: '',
                    teamId: '',
                    logoUrl: '',
                    introduction: ''
                },
                proModelFormOrigin: {},
                studentList: [],
                teacherList: [],
                proModelRules: {
                    pName: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'},
                        {validator: validatePNameAndSt, trigger: 'blur'},
                    ],
                    proCategory: [
                        {required: true, message: '请选择项目类别', trigger: 'change'}
                    ],
                    teamId: [
                        {required: true, message: '请选择团队', trigger: 'change'}
                    ]
                },
                disabled: false,
                projectStyles: [],
                teamList: teamList,

                dialogVisibleChangeLogo: false,
                logoPicFile: null,
                isUpdating: false,
                logoPic: ''
            }
        },
        computed: {
            applyDate: function () {
                return this.proModel.createDate
            },
            studentParamObj: function () {
                var studentList = this.studentList;
                var entry = {};
                studentList.forEach(function (item, index) {
                    entry['studentList[' + index + '].userId'] = item['userId']
                });
                return entry;
            },
            teacherParamObj: function () {
                var teacherList = this.teacherList;
                var entry = {};
                teacherList.forEach(function (item, index) {
                    entry['teacherList[' + index + '].userId'] = item['userId']
                });
                return entry;
            },
            isSubmited: function () {
                return this.proModel.subStatus === '1';
            }
        },
        methods: {
            validateProModelForm: function (type) {
                var self = this;
                var proModelForm = this.proModelForm;
                if(type === 'prev'){
                    self.saveProModel(type);
                    return;
                }
                this.$refs.proModelForm.validate(function (valid) {
                    if (valid) {
                        if (type === 'prev' || type === 'save') {
                            self.saveProModel(type);
                        } else {
                            if (self.isChangeProjectVal() && self.proModelForm.id) {
                                location.href = '/f/proproject/applyStep3?id=' + proModelForm.id + '&actYwId=' + proModelForm.actYwId;
                            } else {
                                self.nextProModel();
                            }
                        }
                    }
                })
            },

            saveProModel: function (type) {
                var self = this;
                var proModelForm = this.proModelForm;
                var params = JSON.parse(JSON.stringify(this.proModelForm));
                var url = '/proproject/saveStep2Uncheck';

                if (type === 'prev') {
                    if (this.isChangeProjectVal() && proModelForm.id) {
                        location.href = '/f/proproject/applyStep1?id=' + proModelForm.id + '&actYwId=' + proModelForm.actYwId;
                        return false;
                    }
                }
                var nParams = Object.assign(params, this.studentParamObj, this.teacherParamObj);
                if(nParams.logoUrl.indexOf('/temp/projectlogo') === -1){
                    delete nParams.logoUrl;
                }
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: url,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: Object.toURLSearchParams(nParams)
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        if (type === 'save') {
                            self.$alert(data.msg, '提示', {
                                type: 'success',
                                cancelButtonText: '取消',
                                confirmButtonText: '下一步',
                                showCancelButton: true,
                                showClose: false
                            }).then(function () {
                                location.href = '/f/proproject/applyStep3?id=' + data.id + '&actYwId=' + params.actYwId;
                            }).catch(function () {
                                location.href = '/f/proproject/applyStep2?id=' + data.id + '&actYwId=' + params.actYwId;
                            });
                        } else {
                            location.href = '/f/proproject/applyStep1?id=' + data.id + '&actYwId=' + params.actYwId;
                        }
                        self.proModelForm.id = data.id;
                        self.proModelFormOrigin = JSON.parse(JSON.stringify(self.proModelForm));
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            cancelButtonText: '确定'
                        }).then(function () {
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            nextProModel: function () {
                var self = this;
                var params = JSON.parse(JSON.stringify(this.proModelForm));
                var url = '/proproject/saveStep2';
                Object.assign(params, this.studentParamObj, this.teacherParamObj);
                this.disabled = true;
                this.$axios.post(url, Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        location.href = '/f/proproject/applyStep3?id=' + data.id + '&actYwId=' + params.actYwId;
                        self.proModelForm.id = data.id;
                        self.proModelFormOrigin = JSON.parse(JSON.stringify(self.proModelForm));
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            cancelButtonText: '确定'
                        }).then(function () {
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            isChangeProjectVal: function () {
                var proModelFormOrigin = this.proModelFormOrigin;
                var proModelForm = this.proModelForm;
                var isNotChange = true;
                for (var k in  proModelFormOrigin) {
                    if (proModelFormOrigin.hasOwnProperty(k)) {
                        if (proModelFormOrigin[k] !== proModelForm[k]) {
                            isNotChange = false;
                            break;
                        }
                    }
                }
                return isNotChange;
            },

            getProjectStyles: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_type').then(function (response) {
                    var data = response.data;
                    self.projectStyles = data || [];
                })
            },
            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
                this.teacherList = data.teacherList;
            },

            handleChangeLogoPicClose: function () {
                this.logoPicFile = null;
                this.logoPic = '';
                this.dialogVisibleChangeLogo = false;
            },

            openDialogLogo: function () {
                var url = this.proModelForm.logoUrl;
                if (this.disabled) return false;
                url = this.addFtpHttp(url);
                this.logoPic = url;
                this.dialogVisibleChangeLogo = true;

            },

            getLogoPicFile: function (file) {
                this.logoPicFile = file;
            },

            updateUserPic: function () {
                var data = this.$refs.cropperPic.getData();
                var self = this;
                var formData = new FormData();
                if (data.x < 0 || data.y < 0) {
                    this.$message.error('超出边界，请缩小裁剪框，点击上传');
                    return false;
                }
                this.isUpdating = true;
                formData.append('upfile', this.logoPicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/cutImgToTempDir?folder=projectlogo&x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
                        self.proModelForm.logoUrl = data.ftpUrl;
                        self.handleChangeLogoPicClose();
                        self.logoPic = '';
                        self.logoPicFile = null;
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.isUpdating = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            }
        },
        created: function () {
            this.assignFormData(this.proModelForm, this.proModel);
            this.proModelForm.logoUrl = this.proModel.logo ? this.proModel.logo.url : '';
            this.proModelFormOrigin = JSON.parse(JSON.stringify(this.proModelForm));
            this.getProjectStyles();
        }
    })

</script>

</body>
</html>