<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
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
            <el-button type="primary" size="mini" :disabled="disabled || isFileUploading" @click.stop="validateAllForm">保存</el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="项目信息" name="firstCreditTab">
                <tab-pane-content>
                    <e-panel label="项目信息">
                        <el-form :model="proModelForm" ref="proModelForm" :rules="proModelRules" :disabled="disabled"
                                 size="mini" label-width="120px">
                            <el-form-item label="申报日期：">
                                {{proModel.createDate | formatDateFilter('YYYY-MM-DD')}}
                            </el-form-item>
                            <el-form-item label="项目编号：" prop="competitionNumber">
                                <el-input v-model="proModelForm.competitionNumber" maxlength="64"
                                          class="w300"></el-input>
                            </el-form-item>
                            <el-form-item label="项目名称：" prop="pName">
                                <el-input v-model="proModelForm.pName" placeholder="最多128个字符"
                                          maxlength="128"></el-input>
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
                            <el-form-item prop="introduction" label="项目简介：">
                                <el-input type="textarea" v-model="proModelForm.introduction" :rows="3"
                                          placeholder="最多2000个字符"
                                          maxlength="2000"></el-input>
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
            <el-tab-pane v-if="reports.length > 0" label="项目报告" name="secondCreditTab">
                <tab-pane-content>
                    <el-form :model="reportsForm" ref="reportsForm" :rules="reportsRules" size="mini"
                             :disabled="disabled" label-width="120px">
                        <e-panel v-for="(item, index) in reportsForm.reports" :key="item.gnodeId">
                            <act-yw-gnode-label slot="label"
                                                :url="'/promodel/proModel/getActYwGnode/'+item.gnodeId"></act-yw-gnode-label>
                            <el-form-item :prop="'reports.'+ index + '.files'" :rules="reportsRulesFile" label="附件：">
                                <e-pw-upload-file v-model="reportsForm.reports[index].files"
                                                  action="/ftp/ueditorUpload/uploadPwTemp?folder=project"
                                                  :upload-file-vars="{}"
                                                  is-post
                                                  tip="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件，请到时间轴页面下载相关报告模板"
                                                  @change="handleChangeReportFiles('reports.'+ index + '.files')"></e-pw-upload-file>
                            </el-form-item>
                        </e-panel>
                    </el-form>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="审核记录" name="threeCreditTab">
                <tab-pane-content>
                    <promodel-audit-record :pro-model-id="proModel.id"></promodel-audit-record>
                </tab-pane-content>
            </el-tab-pane>
        </el-tabs>
    </div>
    <div class="text-center">
        <el-button size="mini" @click.stop="goToBack">返回</el-button>
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
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            var sysAttachmentList = JSON.parse(JSON.stringify(${fns: toJson(applyFiles)})) || [];
            var savedTeamUrl = '/promodel/proModel/getProChangedTeam/' + proModel.id + '?teamId=' + proModel.teamId;
            var actYwAuditInfos = JSON.parse(JSON.stringify(${fns: toJson(actYwAuditInfos)})) || [];
            var reports = JSON.parse(JSON.stringify(${fns: toJson(reports)})) || [];
            var changeGnodes = JSON.parse(JSON.stringify(${fns: toJson(changeGnodes)})) || [];

            reports.forEach(function (report) {
                report.files.forEach(function (item, index) {
                    item.uid += index;
                    return item;
                })
            });

            sysAttachmentList = sysAttachmentList.map(function (item, index) {
                item.uid += index;
                return item;
            });
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
            var validateNumber = function (rules, value, callback) {
                if (value) {
                    var proModelForm = self.proModelForm;
                    var params = {
                        id: proModelForm.id,
                        num: proModelForm.competitionNumber,
                        actYwId: proModelForm.actYwId
                    };
                    if(!(/^[a-zA-Z0-9]+$/.test(value))){
                        return callback(new Error('请输入英文或者数字'))
                    }
                    return self.$axios.get('/promodel/proModel/checkNumberByActYwId',{params: params}).then(function (response) {
                        var data = response.data;
                        if (data) {
                            return callback();
                        }
                        return callback(new Error('项目编号已经存在'))
                    }).catch(function () {
                        return callback(new Error(self.xhrErrorMsg))
                    })
                }
                return callback();
            };
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
                    competitionNumber: '',
                    pName: '',
                    shortName: '',
                    proCategory: '',
                    introduction: '',
                    logoUrl: '',
                    teamId: '',
                    sysAttachmentList: sysAttachmentList
                },
                changeGnodes: changeGnodes,

                savedTeamUrl: savedTeamUrl,
                reports: reports,
                actYwAuditInfos: actYwAuditInfos,
                reportsForm: {
                    reports: reports
                },
                reportsRules: {},
                reportsRulesFile: [
                    {required: true, message: '请上传附件', trigger: 'change'}
                ],
                proModelRules: {
                    competitionNumber: [
                        {required: true, message: '请输入项目编号', trigger: 'blur'},
                        {validator: validateNumber, trigger: 'blur'}
                    ],
                    pName: [
                        {required: true, message: '请输入项目名称', trigger: 'blur'},
                        {validator: validatePNameAndSt, trigger: 'blur'}
                    ],
                    proCategory: [
                        {required: true, message: '请选择项目类别', trigger: 'change'}
                    ],
                    sysAttachmentList: [
                        {required: true, message: '请上传附件', trigger: 'change'}
                    ]
                },
                disabled: false,
                applyUser: proModel.deuser,
                officeList: JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())})),
                tabActiveName: 'firstCreditTab',

                dialogVisibleChangeLogo: false,
                logoPicFile: null,
                isUpdating: false,
                logoPic: '',

                dialogVisibleTeamChange: false,

                projectStyles: [],
                teamUserChangeList: [],
                studentList: [],
                teacherList: [],
                fileKeysYs: {
                    fielSize: 'size',
                    fielTitle: 'name',
                    fielType: 'suffix',
                    fielFtpUrl: 'tempUrl',
                    gnodeId: 'gnodeId'
                }
            }
        },
        computed: {
            isFileUploading: function(){
                return this.proModelForm.sysAttachmentList.length > 0 && this.proModelForm.sysAttachmentList.some(function (item) {
                            return !item.ftpUrl;
                        })
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
            goToBack: function () {
                history.go(-1);
            },
            handleChangeReportFiles: function (path) {
                this.$refs.reportsForm.validateField(path)
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

            validateReportsForm: function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    self.$refs.reportsForm.validate(function (valid, object) {
                        if (valid) {
                            resolve()
                        } else {
                            reject({
                                formName: '项目报告',
                                error: object
                            })
                        }
                    })
                })
            },


            validateAllForm: function () {
                var self = this;
                var reportsForm = this.$refs.reportsForm;
                var proModelForm = this.$refs.proModelForm;
                var promiseAll = [];
                reportsForm && promiseAll.push(this.validateReportsForm());
                proModelForm && promiseAll.push(this.validateProModelForm());
                Promise.all(promiseAll).then(function () {
                    self.submitProModelForm();
                }).catch(function (error) {
                    self.$alert('请检查' + error.formName + '表单是否合格', '提示');
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
                            location.href = "/a/cms/form/queryMenuList/?actywId=${proModel.actYwId}";
                        }).catch(function () {
                            location.reload();
                        })
                        window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                        self.disabled = false;
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
                var reportParams = this.getReportParams();
                var params = [proModelParams, sysAttachmentListParams, reportParams];
                if (actParams) {
                    params.push(actParams)
                }
                return params.join('&');
            },

            getReportParams: function () {
                var reports = this.reportsForm.reports || [];
                var fileKeysYs = this.fileKeysYs;
                var paramsPaths = [];
                reports.forEach(function (report) {
                    report.files.forEach(function (item) {
                        var fileStep = item.fileStep;
                        var createDate = item.createDate;
                        var entry = {};
                        for (var k in fileKeysYs) {
                            var paramsStr = '';
//                            if (fileStep || createDate) {
                                paramsStr = 'proModel.';
//                            }
                            if (fileKeysYs.hasOwnProperty(k)) {
                                paramsStr = paramsStr + 'attachMentEntity.' + k;
                                if (k === 'gnodeId') {
                                    entry[paramsStr] = report[k];
                                } else {
                                    entry[paramsStr] = item[fileKeysYs[k]];
                                }
                            }
                        }
                        paramsPaths.push(Object.toURLSearchParams(entry).toString())
                    });
                })
                return paramsPaths.join('&');
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
                this.$axios.get('/team/teamUserChange/ajax/getTeamUserChangeList?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.ret == 1) {
                        self.teamUserChangeList = data.list || []
                    } else {
                        self.teamUserChangeList = [];
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg)
                })
            },

            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
                this.teacherList = data.teacherList;
            },


            getProjectStyles: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_type').then(function (response) {
                    var data = response.data;
                    self.projectStyles = data || [];
                })
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
            }
        },
        created: function () {
            this.assignFormData(this.proModelForm, this.proModel);
            this.proModelForm.logoUrl = this.proModel.logo ? this.proModel.logo.url : '';
            this.getProjectStyles();
        }
    })

</script>

</body>
</html>
