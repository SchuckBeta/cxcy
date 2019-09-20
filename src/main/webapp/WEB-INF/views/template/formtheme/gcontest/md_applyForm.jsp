<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>

</head>

<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/html-sctzsj">双创大赛</a></el-breadcrumb-item>
        <el-breadcrumb-item>{{projectName}}</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{sysDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">填写申请信息</span>
            </div>
        </div>
        <el-form :model="proModelForm" ref="proModelForm" :rules="competitionRules" :disabled="disabled"
                 size="mini" label-width="230px">
            <div class="apply-form-body">
                <div class="user-info-list">
                    <el-row :gutter="15" label-width="120px">
                        <el-col :span="12">
                            <e-col-item align="right" label="申报人：">{{loginCurUser.name}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item align="right" label="联系电话：">{{loginCurUser.mobile}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item align="right" label="E-mail：">{{loginCurUser.email}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label-width="120px" prop="bankName" label="开户银行：">
                                <el-input v-model="proModelForm.bankName" maxlength="64" placeholder="限64位字符"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <el-form-item label-width="120px" prop="bankNumber" label="开户账号：">
                                <el-input v-model="proModelForm.bankNumber" maxlength="64"></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item align="right" label="创始人简介：">
                                <span class="white-space-pre" v-html="loginCurUser.introduction"></span>
                            </e-col-item>
                        </el-col>
                    </el-row>
                </div>
                <div class="apply-form-group" v-loading="loading">

                    <div class="apply-form-item-titlebar">
                        <span class="title">大赛信息<i class="el-icon-d-arrow-right"></i></span>
                    </div>
                    <el-form-item prop="gcTrack" label="参赛赛道：">
                        <el-radio-group v-model="proModelForm.gcTrack" @change="handleChangeGcTrack">
                            <el-radio v-for="item in tracks" :key="item.value" :label="item.value">{{item.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item prop="pName" label="项目名称：">
                        <el-input v-model="proModelForm.pName" placeholder="最多30个字符" maxlength="30"></el-input>
                    </el-form-item>
                    <el-form-item label="项目Logo：" prop="logoUrl" class="common-upload-one-img">
                        <div class="upload-box-border site-logo-left-size">
                            <div class="avatar-uploader">
                                <div tabindex="0" class="el-upload el-upload--text"
                                     @click.stop="openDialogLogo">
                                    <img :src="proModelForm.logoUrl | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                                </div>
                            </div>
                            <div v-show="proModelForm.logoUrl && !disabled" class="arrow-block-delete"
                                 @click.stop="proModelForm.logoUrl = ''">
                                <i class="el-icon-delete"></i>
                            </div>
                        </div>
                    </el-form-item>
                    <el-form-item prop="proCategory" label="大赛类别：" v-if="proCategoryMap.length > 0">
                        <el-select v-model="proModelForm.proCategory">
                            <el-option v-for="item in proCategoryMap" :key="item.value" :value="item.value"
                                       :label="item.label"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="level" label="参赛组别：">
                        <el-tooltip content="请选择参赛赛道" :disabled="!!proModelForm.gcTrack" popper-class="white"
                                    placement="bottom">
                            <el-select v-model="proModelForm.level" :disabled="!proModelForm.gcTrack">
                                <el-option v-for="item in gContestLevel" :key="item.value" :value="item.value"
                                           :label="item.label"></el-option>
                            </el-select>
                        </el-tooltip>
                    </el-form-item>
                    <el-form-item prop="belongsField" label="所属领域：">
                        <el-checkbox-group v-model="proModelForm.belongsField">
                            <el-checkbox v-for="item in technologyFields" :label="item.value" :key="item.value">
                                {{item.label}}
                            </el-checkbox>
                        </el-checkbox-group>
                    </el-form-item>

                    <div class="apply-form-item-titlebar">
                        <span class="title">项目介绍<i class="el-icon-d-arrow-right"></i></span>
                    </div>
                    <el-form-item prop="introduction" label="项目概述：">
                        <el-input type="textarea" v-model="proModelForm.introduction" :rows="5"
                                  placeholder="可拆分为产品描述、用户群体、项目愿景、竞争对手等方面详细描述，不少于100字，最多1024个字符"
                                  minlength="100" maxlength="1024"></el-input>
                    </el-form-item>

                    <el-form-item prop="achievementTf" label="高校科研成果转化：">
                        <el-radio-group v-model="proModelForm.achievementTf">
                            <el-radio v-for="item in yesOrNo" :key="item.value" :label="item.value">{{item.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item prop="achievementUser" label="创始人为科技成果的完成人或所有人：">
                        <el-radio-group v-model="proModelForm.achievementUser">
                            <el-radio v-for="item in yesOrNo" :key="item.value" :label="item.value">{{item.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item prop="coCreation" label="师生共创：">
                        <el-radio-group v-model="proModelForm.coCreation">
                            <el-radio v-for="item in yesOrNo" :key="item.value" :label="item.value">{{item.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <div class="apply-form-item-titlebar">
                        <span class="title">认证信息<i class="el-icon-d-arrow-right"></i></span>
                    </div>

                    <el-form-item prop="ideaStage" label="项目进展：">
                        <el-radio-group v-model="proModelForm.ideaStage">
                            <el-radio v-for="item in ideaStages" :key="item.value" :label="item.value">{{item.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>

                    <el-form-item prop="sysAttachmentList" label="附件：">
                        <e-pw-upload-file v-model="proModelForm.sysAttachmentList"
                                          action="/ftp/ueditorUpload/uploadPwTemp?folder=project" :upload-file-vars="{}"
                                          @change="handleChangeFiles"
                                          is-post
                                          @get-is-upload="getIsUpload"
                                          tip="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件<br>（上传参赛项目申报表单、商业计划书等参赛项目文件）"></e-pw-upload-file>
                    </el-form-item>

                    <div class="apply-form-item-titlebar">
                        <span class="title">团队信息<i class="el-icon-d-arrow-right"></i></span>
                    </div>

                    <el-form-item label="选择团队：" prop="teamId">
                        <el-select v-model="proModelForm.teamId">
                            <el-option v-for="item in teamList" :key="item.id" :value="item.id"
                                       :label="item.name"></el-option>
                        </el-select>
                        <template v-if="teamList.length === 0" style="display: none"><span
                                class="el-form-item-expository">暂无团队，请去<a
                                href="${ctxFront}/team/indexMyTeamList">创建团队</a>吧</span></template>
                    </el-form-item>
                    <update-member :declare-id="loginCurUser.id" :disabled="disabled" :team-id="proModelForm.teamId"
                                   :operate-btn-show="false" :addable="false"
                                   @change="handleChangeTeam"></update-member>


                    <el-form-item label-width="0" class="text-center">
                        <el-button type="primary" @click.stop="submitForm" v-if="isStudent"
                                   :disabled="disabled || isFileUploading">提交并保存
                        </el-button>
                        <el-button @click.stop="goBack">返回</el-button>
                    </el-form-item>


                </div>


            </div>
        </el-form>

    </div>
    <el-dialog title="上传大赛Logo"
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

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.commonProContestRules, Vue.contestRules],
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)})) || {};
            var proCategoryMap = JSON.parse(JSON.stringify(${fns:toJson(proCategoryMap)})) || [];
            var teamList = JSON.parse(JSON.stringify(${fns: toJson(teams)})) || [];
            var tracks = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            var sysAttachmentList = proModel.fileInfo || [];
            sysAttachmentList = sysAttachmentList.map(function (item, index) {
                item.uid += index;
                return item;
            });
            return {
                proModel: proModel,
                proCategoryMap: proCategoryMap,
                gContestLevel: [],
                trackLevels: {},
                teamList: teamList,
                projectName: '${projectName}',
                sysDate: '${sysdate}',
                loading: false,
                proModelForm: {
                    id: '',
                    declareId: '',
                    bankName: '',
                    bankNumber: '',
                    gcTrack: '',
                    actYwId: '',
                    pName: '',
                    proCategory: '',
                    logoUrl: '',
                    level: '',
                    teamId: '',
                    introduction: '',
                    belongsField: [],
                    achievementTf: '',
                    achievementUser: '',
                    coCreation: '',
                    ideaStage: '',
                    sysAttachmentList: sysAttachmentList
                },
                disabled: true,
                studentList: [],
                tracks: tracks,
                yesOrNo: [],
                ideaStages: [],
                technologyFields: [],
                fileKeysYs: {
                    fielSize: 'size',
                    fielTitle: 'name',
                    fielType: 'suffix',
                    fielFtpUrl: 'tempUrl'
                },
                dialogVisibleChangeLogo: false,
                logoPicFile: null,
                isUpdating: false,
                logoPic: '',
                fileUploadStatus: {}
            }
        },
        computed: {
            proNameParams: function () {
                var proModelForm = this.proModelForm;
                return {
                    id: proModelForm.id,
                    actYwId: proModelForm.actYwId,
                    pName: proModelForm.pName
                }
            },

            proNumberParams: function () {
                var proModelForm = this.proModelForm;
                return  {
                    id: proModelForm.id,
                    num: proModelForm.competitionNumber
                };
            },
            studentParamObj: function () {
                var studentList = this.studentList;
                var entry = {};
                studentList.forEach(function (item, index) {
                    entry['teamUserHistoryList[' + index + '].userId'] = item['userId'];
                    entry['proModel.teamUserHistoryList[' + index + '].userId'] = item['userId'];
                });
                return entry;
            },
            isFileUploading: function () {
                var uploading = false;
                for (var k in this.fileUploadStatus) {
                    if (this.fileUploadStatus.hasOwnProperty(k)) {
                        if (this.fileUploadStatus[k]) {
                            uploading = true;
                            break;
                        }
                    }
                }
                return uploading;
            }
        },
        methods: {
            goBack: function () {
                history.go(-1)
            },

            getIsUpload: function (status) {
                this.$set(this.fileUploadStatus, status.uid, status.status);
            },

            handleChangeFiles: function (file) {
                this.$refs.proModelForm.validateField('sysAttachmentList');
            },


            handleChangeGcTrack: function (val, isReShow) {
                var self = this;
                if (isReShow !== 'isReVal') {
                    this.proModelForm.level = '';
                }
                if (this.trackLevels[val]) {
                    this.gContestLevel = this.trackLevels[val];
                    return;
                }
                this.$axios.get('/sys/dict/getDictList', {params: {type: val}}).then(function (response) {
                    var data = response.data;
                    self.trackLevels[val] = data || [];
                    self.gContestLevel = self.trackLevels[val];
                })
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
                })
            },
            proProjectApplyAble: function () {
                var proModel = this.proModel;
                var self = this;
                if (!this.isStudent) {
                    return false;
                }
                if (proModel.id) {
                    this.disabled = false;
                    return false;
                }
                this.checkProProjectIsValid('/gcontest/gContest/onGcontestApply', {
                    actywId: proModel.actYwId,
                    pid: proModel.id
                }).then(function () {
                    self.disabled = false;
                }).catch(function (error) {
                    var msgBoxOptions = {
                        type: 'error',
                        title: '提示',
                        closeOnClickModal: false,
                        closeOnPressEscape: false,
                        showClose: false,
                        message: error.message
                    };
                    var btns = error.btns;
                    if (error.code === '0') {
                        msgBoxOptions['confirmButtonText'] = '确定'
                    } else if (error.code === '2') {
                        msgBoxOptions['confirmButtonText'] = btns[0].name;
                        if (btns.length === 2) {
                            msgBoxOptions['cancelButtonText'] = btns[1].name;
                            msgBoxOptions['showCancelButton'] = true
                        }
                    } else {
                        msgBoxOptions['confirmButtonText'] = '刷新'
                    }
                    self.$msgbox(msgBoxOptions).then(function () {
                        if (error.code === '0') {
                            location.href = self.ctxFront
                        } else if (error.code === '2') {
                            location.href = btns[0].url;
                        } else {
                            location.reload();
                        }
                    }).catch(function () {
                        location.href = btns[1].url
                    });
                })
            },

            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
            },
            submitForm: function () {
                var self = this;
                var proModelForm = this.proModelForm;
                var fileParams = this.getSysAttachmentListParams();
                var params = JSON.parse(JSON.stringify(proModelForm));
                Object.assign(params, this.studentParamObj);
                delete params.sysAttachmentList;
                params = Object.toURLSearchParams(params) + '&' + fileParams;
                this.$refs.proModelForm.validate(function (valid) {
                    if (valid) {
                        self.submitAjax(params);
                    }
                });
            },
            submitAjax: function (params) {
                var self = this;
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/submit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: params
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.$alert('提交成功', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: true,
                            showClose: false
                        }).then(function () {
                            location.href = self.ctxFront + '/gcontest/gContest/list'
                        }).catch(function () {
                            location.reload();
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            confirmButtonText: '确定'
                        }).then(function () {

                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                })

            },
            getSysAttachmentListParams: function () {
                var sysAttachmentList = this.proModelForm.sysAttachmentList || [];
                var fileKeysYs = this.fileKeysYs;
                var paramsPaths = [];
                sysAttachmentList.forEach(function (item) {
                    var fileStep = item.fileStep;
                    var entry = {};
                    for (var k in fileKeysYs) {
                        var paramsStr = '';
//                        if (fileStep) {
                        paramsStr = 'proModel.';
//                        }
                        if (fileKeysYs.hasOwnProperty(k)) {
                            paramsStr = paramsStr + 'attachMentEntity.' + k;
                            entry[paramsStr] = item[fileKeysYs[k]];
                        }
                    }
                    paramsPaths.push(Object.toURLSearchParams(entry))
                });
                return paramsPaths.join('&');
            },
            getDictLists: function () {
                var dicts = {
                    'yes_no': 'yesOrNo',
                    'technology_field': 'technologyFields',
                    '20190508111448100067': 'ideaStages'
                };
                this.getBatchDictList(dicts)

            },


        },
        created: function () {
            this.proProjectApplyAble();
            this.getDictLists();
            if (!this.proModel.belongsField) {
                this.proModel.belongsField = [];
            } else {
                this.proModel.belongsField = this.proModel.belongsField.split(',')
            }
            this.proModelForm.declareId = this.loginCurUser.id;
            this.assignFormData(this.proModelForm, this.proModel);
        },
        mounted: function () {
            if (this.proModelForm.gcTrack) {
                this.handleChangeGcTrack(this.proModelForm.gcTrack, 'isReVal')
            }
        }
    })
</script>


</body>
</html>