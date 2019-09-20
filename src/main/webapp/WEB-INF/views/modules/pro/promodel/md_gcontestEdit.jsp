<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>

<body>


<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar :second-name="secondName"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info-pro-contest :apply-user="applyUser"></applicant-info-pro-contest>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <div class="btn-audit-box">
            <el-button type="primary" size="mini" :disabled="disabled || isFileUploading" @click.stop="validateAllForm">
                保存
            </el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="大赛信息" name="firstCreditTab">
                <tab-pane-content>
                    <e-panel label="大赛信息">
                        <el-form :model="proModelForm" ref="proModelForm" :rules="competitionRules" :disabled="disabled"
                                 autocomplete="off"
                                 size="mini" label-width="120px">
                            <el-form-item label="申报日期：">
                                {{proModel.createDate | formatDateFilter('YYYY-MM-DD')}}
                            </el-form-item>
                            <el-form-item label-width="120px" prop="bankName" label="开户银行：">
                                <el-input v-model="proModelForm.bankName" maxlength="64"
                                          placeholder="限64位字符"></el-input>
                            </el-form-item>
                            <el-form-item label-width="120px" prop="bankNumber" label="开户账号：">
                                <el-input v-model="proModelForm.bankNumber" maxlength="64"></el-input>
                            </el-form-item>
                            <el-form-item label="参赛编号：" prop="competitionNumber">
                                <el-input v-model="proModelForm.competitionNumber" maxlength="64"
                                          class="w300"></el-input>
                            </el-form-item>
                            <el-form-item prop="gcTrack" label="参赛赛道：">
                                <el-radio-group v-model="proModelForm.gcTrack" @change="handleChangeGcTrack">
                                    <el-radio v-for="item in tracks" :key="item.value" :label="item.value">
                                        {{item.label}}
                                    </el-radio>
                                </el-radio-group>
                            </el-form-item>
                            <el-form-item label="参赛项目名称：" prop="pName">
                                <el-input v-model="proModelForm.pName" placeholder="最多128个字符"
                                          maxlength="128"></el-input>
                            </el-form-item>
                            <el-form-item label="参赛类别：" prop="proCategory">
                                <el-select v-model="proModelForm.proCategory">
                                    <el-option v-for="item in competitionTypes" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item prop="level" label="参赛组别：">
                                <el-tooltip content="请选择参赛赛道" :disabled="!!proModelForm.gcTrack" popper-class="white"
                                            placement="bottom">
                                    <el-select v-model="proModelForm.level" :disabled="!proModelForm.gcTrack">
                                        <el-option v-for="item in gContestLevels" :key="item.value" :value="item.value"
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
                            <el-form-item prop="introduction" label="项目概述：">
                                <el-input type="textarea" v-model="proModelForm.introduction" :rows="3"
                                          placeholder="可拆分为产品描述、用户群体、项目愿景、竞争对手等方面详细描述，不少于100字，最多1024个字符"
                                          minlength="100" maxlength="1024"></el-input>
                            </el-form-item>
                            <el-form-item prop="achievementTf" label="高校科研成果转化：">
                                <el-radio-group v-model="proModelForm.achievementTf">
                                    <el-radio v-for="item in yesOrNo" :key="item.value" :label="item.value">
                                        {{item.label}}
                                    </el-radio>
                                </el-radio-group>
                            </el-form-item>

                            <el-form-item prop="achievementUser" label="创始人为科技成果的完成人或所有人：">
                                <el-radio-group v-model="proModelForm.achievementUser">
                                    <el-radio v-for="item in yesOrNo" :key="item.value" :label="item.value">
                                        {{item.label}}
                                    </el-radio>
                                </el-radio-group>
                            </el-form-item>

                            <el-form-item prop="coCreation" label="师生共创：">
                                <el-radio-group v-model="proModelForm.coCreation">
                                    <el-radio v-for="item in yesOrNo" :key="item.value" :label="item.value">
                                        {{item.label}}
                                    </el-radio>
                                </el-radio-group>
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
                            <el-form-item prop="sysAttachmentList" label="附件：">
                                <e-pw-upload-file v-model="proModelForm.sysAttachmentList"
                                                  action="/ftp/ueditorUpload/uploadPwTemp?folder=project"
                                                  :upload-file-vars="{}"
                                                  is-post
                                                  tip="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件，请到时间轴页面下载相关报告模板"
                                                  @change="handleChangeFiles"></e-pw-upload-file>
                            </el-form-item>
                            <el-form-item label="团队名称：" required>
                                ${team.name}
                                <el-button type="text" @click.stop="openTeamChange">成员变更记录</el-button>
                            </el-form-item>
                            <update-member :declare-id="applyUser.id" :disabled="disabled"
                                           :team-id="proModelForm.teamId" is-admin :saved-team-url="savedTeamUrl"
                                           @change="handleChangeTeam"></update-member>
                            <el-form-item v-if="changeGnodes.length > 0" label="变更流程到节点：" prop="toGnodeId">
                                <el-select v-model="proModelForm.toGnodeId" clearable filterable>
                                    <el-option v-for="item in changeGnodes" :label="item.auditName"
                                               :value="item.gnodeId"
                                               :key="item.gnodeId"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </e-panel>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="审核记录" name="threeCreditTab">
                <tab-pane-content>
                    <promodel-audit-record :pro-model-id="proModel.id"></promodel-audit-record>
                </tab-pane-content>
            </el-tab-pane>
        </el-tabs>
    </div>

    <el-dialog
            title="成员变更记录"
            :visible.sync="dialogVisibleTeamChange"
            width="600px"
            :close-on-click-modal="false"
            :before-close="handleCloseTeamChange">
        <div class="table_user-list-container" style="height: auto">
            <div class="table_user_wrapper">
                <div class="table-container">
                    <el-table :data="teamUserChangeList" size="mini" class="table" empty-text="无变更记录">
                        <el-table-column label="变更成员" prop="name" width="100"></el-table-column>
                        <el-table-column label="变更内容" prop="content"></el-table-column>
                        <el-table-column label="变更时间" prop="date" width="160"></el-table-column>
                    </el-table>
                </div>
            </div>
        </div>
    </el-dialog>

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
            var self = this;
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            var sysAttachmentList = JSON.parse(JSON.stringify(${fns: toJson(applyFiles)})) || [];
            var tracks = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            var savedTeamUrl = '/promodel/proModel/getProChangedTeam/' + proModel.id + '?teamId=' + proModel.teamId;
            var changeGnodes = JSON.parse(JSON.stringify(${fns: toJson(changeGnodes)})) || [];


            sysAttachmentList = sysAttachmentList.map(function (item, index) {
                item.uid += index;
                return item;
            });


            return {
                secondName: '${secondName}',
                proModel: proModel,
                proModelForm: {
                    id: '',
                    year: '',
                    actYwId: '',
                    act: {
                        taskId: '',
                        taskName: '',
                        taskDefKey: '',
                        procInsId: '',
                        procDefId: ''
                    },
                    actionPath: '${actionPath}',
                    gnodeId: '${gnodeId}',
                    bankName: '',
                    bankNumber: '',
                    competitionNumber: '',
                    pName: '',
                    proCategory: '',
                    introduction: '',
                    logoUrl: '',
                    gcTrack: '',
                    level: '',
                    teamId: '',
                    belongsField: [],
                    achievementTf: '',
                    achievementUser: '',
                    coCreation: '',
                    ideaStage: '',
                    toGnodeId: '',
                    sysAttachmentList: sysAttachmentList
                },
                disabled: false,
                savedTeamUrl: savedTeamUrl,


                changeGnodes: changeGnodes,

                competitionTypes: [],
                trackLevels: {},
                tracks: tracks,
                yesOrNo: [],
                ideaStages: [],
                technologyFields: [],
                gContestLevels: [],

                studentList: [],
                teacherList: [],
                teamUserChangeList: [],


                dialogVisibleChangeLogo: false,
                logoPicFile: null,
                isUpdating: false,
                logoPic: '',

                dialogVisibleTeamChange: false,


                applyUser: proModel.deuser,
                officeList: [],
                tabActiveName: 'firstCreditTab',
                fileUploadStatus: {},

                fileKeysYs: {
                    fielSize: 'size',
                    fielTitle: 'name',
                    fielType: 'suffix',
                    fielFtpUrl: 'tempUrl',
                }
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
                return {
                    id: proModelForm.id,
                    num: proModelForm.competitionNumber,
                    actYwId: proModelForm.actYwId
                };
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
            },
            applyDate: function () {
                return this.proModel.createDate
            },

            officeEntries: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },
            sexClassName: function () {
                return {
                    'icon-xingbie-nan': this.applyUser.sex === '1',
                    'icon-xingbie-nv': this.applyUser.sex !== '1'
                }
            },
            studentParamObj: function () {
                var studentList = this.studentList;
                var entry = {};
                studentList.forEach(function (item, index) {
                    entry['studentList[' + index + '].userId'] = item['userId'];
                    entry['studentList[' + index + '].userType'] = item['userType'];
                    entry['studentList[' + index + '].userzz'] = item['userzz'];
                });
                return entry;
            },
            teacherParamObj: function () {
                var teacherList = this.teacherList;
                var entry = {};
                teacherList.forEach(function (item, index) {
                    entry['teacherList[' + index + '].userId'] = item['userId'];
                    entry['teacherList[' + index + '].userType'] = item['userType'];
                });
                return entry;
            },
        },
        methods: {
            getIsUpload: function (status) {
                this.$set(this.fileUploadStatus, status.uid, status.status);
            },

            handleChangeGcTrack: function (val, isReShow) {
                var self = this;
                if (isReShow !== 'isReVal') {
                    this.proModelForm.level = '';
                }
                if (this.trackLevels[val]) {
                    this.gContestLevels = this.trackLevels[val] || [];
                    return;
                }
                this.$axios.get('/sys/dict/getDictList', {params: {type: val}}).then(function (response) {
                    var data = response.data;
                    self.trackLevels[val] = data || [];
                    self.gContestLevels = data || [];
                })
            },


            validateProModelForm: function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    self.$refs.proModelForm.validate(function (valid, object) {
                        if (valid) {
                            resolve()
                        } else {
                            reject({
                                formName: '项目信息',
                                error: object
                            })
                        }
                    })
                })
            },


            validateAllForm: function () {
                var self = this;
                var proModelForm = this.$refs.proModelForm;
                var promiseAll = [];
                proModelForm && promiseAll.push(this.validateProModelForm());
                Promise.all(promiseAll).then(function () {
                    self.submitProModelForm();
                }).catch(function (error) {
                    self.$alert('请检查' + error.formName + '表单是否合格', '提示', {
                        type: 'error'
                    });
                })
            },


            submitProModelForm: function () {
                var self = this;
                var params = this.getParams();
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/saveProjectEdit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: params
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.$alert(data.msg, '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            location.href = self.ctx + "/cms/form/queryMenuList/?actywId=${proModel.actYwId}";
                        }).catch(function () {
                            location.reload();
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                        self.disabled = false;
                        window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
                    }
                }).catch(function () {

                })
            },

            getParams: function () {
                var proModelForm = JSON.parse(JSON.stringify(this.proModelForm));
                delete proModelForm.sysAttachmentList;
                delete proModelForm.act;
                if (proModelForm.logoUrl.indexOf('/temp/projectlogo') === -1) {
                    delete proModelForm.logoUrl;
                }
                Object.assign(proModelForm, this.studentParamObj, this.teacherParamObj);
                var proModelParams = Object.toURLSearchParams(proModelForm).toString();
                var sysAttachmentListParams = this.getSysAttachmentListParams();
                var actParams = this.getActParams();
                var params = [proModelParams, sysAttachmentListParams];
                if (actParams) {
                    params.push(actParams)
                }
                return params.join('&');
            },

            getActParams: function () {
                var act = this.proModelForm.act;
                var entry = {};
                for (var k in act) {
                    if (act.hasOwnProperty(k)) {
                        entry['act.' + k] = act[k];
                    }
                }
                return Object.toURLSearchParams(entry).toString()
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
                    paramsPaths.push(Object.toURLSearchParams(entry).toString())
                });
                return paramsPaths.join('&');
            },


            handleChangeFiles: function (file) {
                this.$refs.proModelForm.validateField('sysAttachmentList')
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
            },


            handleCloseTeamChange: function () {
                this.dialogVisibleTeamChange = false;
            },

            openTeamChange: function () {
                this.dialogVisibleTeamChange = true;
                this.getTeamUserChangeList();
            },

            getTeamUserChangeList: function () {
                var self = this;
                var params = {
                    proModelId: this.proModelForm.id,
                    teamId: this.proModelForm.teamId
                };
                this.$axios.get('/team/teamUserChange/ajax/getTeamUserChangeList', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.ret == 1) {
                        self.teamUserChangeList = data.list || []
                    } else {
                        self.teamUserChangeList = [];
                        self.$message.error(data.msg)
                    }
                })
            },


            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
                this.teacherList = data.teacherList;
            },

            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.officeList = data.data || [];
                    }
                })
            },

            getDictLists: function () {
                var dicts = {
                    'yes_no': 'yesOrNo',
                    'technology_field': 'technologyFields',
                    '20190508111448100067': 'ideaStages',
                    'competition_net_type': 'competitionTypes'
                };
                this.getBatchDictList(dicts)

            },

        },

        mounted: function () {
            var proModel = this.proModel
            proModel.belongsField = proModel.belongsField.split(',');
            proModel.logoUrl = proModel.logo ? this.proModel.logo.url : '';
            this.assignFormData(this.proModelForm, proModel);
            this.getOfficeList();
            this.getDictLists();
            this.handleChangeGcTrack(this.proModelForm.gcTrack, 'isReVal')
        }
    })

</script>

</body>
</html>
