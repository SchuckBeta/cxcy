<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="creative"/>
    <script src="${ctxJs}/cityData/citydataNew.js"></script>
</head>
<body>

<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>创业基地</el-breadcrumb-item>
        <el-breadcrumb-item>{{applyLabel}}</el-breadcrumb-item>
    </el-breadcrumb>
    <h3 class="pw-enter-apply-title">大学生创业孵化示范基地{{applyLabel}}</h3>
    <row-step-apply>
        <step-item is-complete>第一步（完善基本信息）</step-item>
        <step-item :is-complete="isSecondStep">第二步（输入申报信息）</step-item>
    </row-step-apply>
    <div class="pw-enter-container">
        <div class="pw-apply-topbar">
            <span class="apply-date">申报日期：{{createdDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <div class="pw-apply-titlebar-wrap">
            <div class="pw-apply-titlebar">
                <span class="title">{{formTitle}}</span>
            </div>
        </div>
        <div v-show="!isSecondStep" class="pw-apply-form-body">
            <div class="pwa-user-card-photo">
                <div class="card-photo common-upload-one-img">
                    <div class="upload-box-border site-cover-size">
                        <el-upload
                                :disabled="disabled || (pwApplyForm.isTemp === '0' && appType === '2')"
                                class="avatar-uploader"
                                action="${ctxFront}/ftp/ueditorUpload/uploadTemp?folder=pwEnter"
                                :show-file-list="false"
                                :on-success="idPhotoSuccess"
                                :on-error="idPhotoError"
                                name="upfile"
                                accept="image/jpg, image/jpeg, image/png">
                            <img :src="pwApplyForm.declarePhoto | ftpHttpFilter(ftpHttp) | cardPhoto">
                        </el-upload>
                        <div class="arrow-block-delete" v-if="pwApplyForm.declarePhoto && !(pwApplyForm.isTemp === '0' && appType === '2')"><i class="el-icon-delete" @click.stop.prevent="pwApplyForm.declarePhoto = ''"></i></div>
                    </div>
                    <div class="img-tip">
                        请上传1寸登记照，变更申请时，将不可以修改登记照
                    </div>
                </div>
            </div>
            <div class="pwa-user-info-list">
                <el-row :gutter="15" label-width="120px">
                    <el-col :span="8">
                        <e-col-item align="right" label="姓名：">{{user.name}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="性别：">{{user.sex === '1' ? '男' : '女'}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="出生年月：">{{user.birthday | formatDateFilter('YYYY-MM')}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="证件类型：">{{user.idType | selectedFilter(idTypeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="证件号码：">{{user.idNumber}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学号：">{{user.no}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学院：">{{user.officeName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="专业：">{{user.professional |
                            selectedFilter(professionalEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="电子邮箱：">{{user.email}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="手机号：">{{user.mobile}}</e-col-item>
                    </el-col>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="户籍所在地：">{{user.residence}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <el-col :span="8">
                        <e-col-item align="right" label="国家/户籍：">{{user.country |
                                                                        userCountryName(cityIdKeyData)}}</e-col-item>
                    </el-col>

                    <el-col :span="24">
                        <e-col-item align="right" label="个人简介：" class="white-space-pre-static word-break"><span v-html="user.introduction"></span>
                        </e-col-item>
                    </el-col>
                </el-row>

                <el-form :model="pwApplyForm" ref="pwApplyForm" :disabled="disabled" :rules="pwApplyOneRules"
                         size="mini" label-width="120px">
                    <el-form-item prop="type" label="入驻类型：">
                        <el-radio-group v-model="pwApplyForm.type" :disabled="(pwApplyForm.isTemp === '0' && enterType === '2')">
                            <el-radio v-for="item in pwEnterTypes" :key="item.value" :label="item.value">
                                {{item.label}}
                            </el-radio>
                        </el-radio-group>
                    </el-form-item>
                    <el-form-item v-if="isStudent" label-width="0" class="text-center">
                        <el-button type="primary" :disabled="!pwApplyForm.type || disabled"
                                   @click.stop.prevent="goToStep">下一步
                        </el-button>
                    </el-form-item>
                </el-form>
            </div>

        </div>
        <template v-if="isRender">
            <enter-form-group ref="enterFormGroup" v-show="isSecondStep" :pw-enter-id="pwApplyForm.id" :disabled="disabled"
                              :form-names="formNames">
                <template v-if="hasEnterpriseForm">
                    <div class="enter-form-group-titlebar">
                        <span class="title">企业信息 <i class="el-icon-d-arrow-right"></i></span>
                    </div>
                    <enterprise-form ref="enterpriseForm" :app-type="appType" :enter-type="enterType"></enterprise-form>
                </template>
                <div class="enter-form-group-titlebar">
                    <span class="title">团队信息 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <link-team-form ref="linkTeamForm"></link-team-form>
                <div class="enter-form-group-titlebar">
                    <span class="title">项目信息 <i class="el-icon-d-arrow-right"></i></span>
                </div>
                <link-project-form ref="linkProjectForm" :status="pwApplyForm.status"></link-project-form>
                <el-dialog
                        title="入驻场地要求"
                        :visible.sync="dialogVisibleBase"
                        width="520px"
                        :close-on-click-modal="false"
                        :before-close="handleCloseBase">
                    <pw-site-form ref="pwSiteForm" :app-type="pwApplyForm.appType"></pw-site-form>
                    <span slot="footer" class="dialog-footer">
                            <el-button type="primary" size="mini" :disabled="disabled" @click.stop.prevent="validateBaseForm">确 定</el-button>
                     </span>
                </el-dialog>
                <div class="text-center enter-form-group-btns">
                    <el-button size="mini" :disabled="disabled"  @click.stop.prevent="goToFirstStep">上一步</el-button>
                    <el-button size="mini" v-if="(pwApplyForm.appType === '1')" :disabled="disabled" type="primary" @click.stop.prevent="saveForm">保存</el-button>
                    <el-button type="primary" :disabled="disabled" size="mini" @click.stop.prevent="submitForm">{{submitLabel}}</el-button></div>
            </enter-form-group>
        </template>
    </div>

</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwEnter = JSON.parse(JSON.stringify(${fns: toJson(pwEnter)})) || {};
            var user = pwEnter.applicant || {};
            var changeEnterType = '${changeEnterType}';
            var isSecondStep = false;
            var isRender = false;
            return {
                pwApplyForm: {
                    id: pwEnter.id,
                    type: pwEnter.type || '0',
                    isTemp: pwEnter.isTemp || '1',
                    declarePhoto: pwEnter.declarePhoto,
                    parentId: pwEnter.parentId,
                    declareId: user.id,
                    isShow: pwEnter.isShow || '1',
                    applicant: {
                        id: user.id
                    },
                    appType: pwEnter.appType || '1',
                    createDate: pwEnter.createDate,
                    status: pwEnter.status
                },
                pwApplyOneRules: {
                    type: [{required: true, message: '请选择入驻类型', trigger: 'change'}]
                },
                city: cityData,
                pwEnterTypes: [],
                postParams: {},
                user: user,
                idTypes: [],
                professionals: [],
                disabled: false,
                isSecondStep: isSecondStep,
                isRender: isRender,
                changeEnterType: changeEnterType,
                dialogVisibleBase: false,
                enterType: pwEnter.type,
                appType: pwEnter.appType,
                originIsTemp: pwEnter.isTemp || '1',
                sysDate: ''

            }
        },
        computed: {

            submitLabel: function () {
                return (this.pwApplyForm.id && this.appType === '2') ? '提交变更' : '提交'
            },
            applyLabel: function () {
                return (this.pwApplyForm.id && this.appType === '2') ? '变更申请' : '入驻申请'
            },

            idTypeEntries: function () {
                return this.getEntries(this.idTypes)
            },
            professionalEntries: function () {
                return this.getEntries(this.professionals, {value: 'id', label: 'name'})
            },
            formTitle: function () {
                return this.isSecondStep ? '填写入驻信息' : '创业人基本信息'
            },
            formNames: function () {
                var formNames = ['LinkTeamForm', 'LinkProjectForm'];
                if(this.hasEnterpriseForm){
                    formNames.push('EnterpriseForm')
                }
                return formNames;
            },
            hasEnterpriseForm: function () {
                return this.pwApplyForm.type === '2'
            },
            createdDate: function () {
                return this.pwApplyForm.createDate || this.sysDate;
            },
            cityIdKeyData: function () {
                var data = {};
                for (var i = 0; i < this.city.length; i++) {
                    var city = this.city[i];
                    data[city.id] = city;
                }
                return data;
            }
        },
        methods: {
            getSysDate: function () {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    self.sysDate = moment(response.data).format('YYYY-MM-DD');
                })
            },
            handleCloseBase: function () {
                this.dialogVisibleBase = false;
            },

            validateBaseForm: function () {
                var self = this;
                this.$refs.pwSiteForm.$refs.pwSiteForm.validate(function (valid) {
                    if(valid){
                        Object.assign(self.postParams, self.$refs.pwSiteForm.$data.pwSiteForm)
                        self.pwApplyForm.isTemp = '0';
                        self.submitPwApplyForm(self.postParams);
                    }
                })
            },



            goToStep: function () {
                this.isRender = true;
                this.isSecondStep = true;
            },

            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                })
            },

            idPhotoSuccess: function (response, file) {
                if (response.state === 'SUCCESS') {
                    this.pwApplyForm.declarePhoto = response.ftpUrl;
                    return
                }
                this.$message.error("登记照上传失败，请重新上传")
            },

            idPhotoError: function (error) {
                this.$message.error(this.xhrErrorMsg)
            },

            getIdTypes: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=id_type').then(function (response) {
                    self.idTypes = response.data || [];
                })
            },

            getProfessionals: function () {
                var self = this;
                this.$axios.get('/sys/office/getOrganizationList').then(function (response) {
                    var data = response.data || [];
                    self.professionals = data.filter(function (item) {
                        return item.grade === '3';
                    })
                })
            },

            goToFirstStep: function () {
                this.isSecondStep = false;
            },

            saveForm: function () {
                this.pwApplyForm.isTemp = '1';
                this.enterFormGroupPromise();
            },

            enterFormGroupPromise: function () {
                var self = this;
                this.$refs.enterFormGroup.noValidateForm().then(function (data) {
                    var pwApplyForm = JSON.parse(JSON.stringify(self.pwApplyForm));
                    var postParams = {};
                    data.forEach(function (item) {
                        postParams = Object.assign(pwApplyForm, item);
                    })
                    postParams.team = {id: pwApplyForm.teamId};
                    self.postParams = postParams;
                    self.submitPwApplyForm(postParams);
                }).catch(function (error) {
                    self.$alert(error.error, '提示', {
                        type: 'error'
                    }).catch(function () {

                    })
//                    console.log('error', error)
                })
            },

            submitForm: function () {
                var self = this;
                this.pwApplyForm.isTemp = '0';
                this.$refs.enterFormGroup.saveEnterForm().then(function (data) {
                    var pwApplyForm = JSON.parse(JSON.stringify(self.pwApplyForm));
                    var postParams = {};
                    data.forEach(function (item) {
                        postParams = Object.assign(pwApplyForm, item);
                    })
                    postParams.team = {id: pwApplyForm.teamId};
                    self.postParams = postParams;
                    self.dialogVisibleBase = true;
                }).catch(function (error) {
                    self.$alert(error.error, '提示', {
                        type: 'error'
                    }).catch(function () {

                    })
                })
            },
            isFileUploading: function (params) {
                return params.pwCompany && params.pwCompany.sysAttachmentList.length > 0 && params.pwCompany.sysAttachmentList.some(function (item) {
                            return !item.ftpUrl
                        }) || this.$refs.linkProjectForm.isFileUpdating
            },

            submitPwApplyForm: function (params) {
                var self = this;
                var isTemp =  this.pwApplyForm.isTemp;
                if(this.isFileUploading(params)){
                    this.$message({
                        type: 'warning',
                        message:'有文件正在上传，请稍等或者取消上传文件'
                    });
                    return false;
                }
                this.disabled = true;
                this.$axios.post('/pw/pwEnter/ajaxPwEnterApplySave', params).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pwApplyForm.id = data.data.id;
                        self.dialogVisibleBase = false;
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: self.applyLabel+(isTemp == '0' ? '提交' : '保存')+'成功'
                        }).then(function () {
                            location.href = '/f/pw/pwEnterRel/list';
                        }).catch(function () {
                        });
                    }else {
                        self.$alert(data.msg, "提示", {
                            type: 'warning'
                        });
                    }
                    self.disabled = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = false;
                })
            },
        },
        created: function () {
            var self = this;
            this.getIdTypes();
            this.getPwEnterTypes();
            this.getProfessionals();
            if(!this.pwApplyForm.createDate && this.isStudent){
                this.getSysDate();
                this.getUserIsCompleted().then(function (response) {
                    var data = response.data;
                    var loginCurUser = self.loginCurUser;
                    if(data.status !== 1){
                        self.disabled = true;
                        self.$msgbox({
                            type: 'error',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: data.msg
                        }).then(function () {
                            if (loginCurUser.userType === '1') {
                                location.href = '/f/sys/frontStudentExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';
                            } else {
                                location.href = '/f/sys/frontTeacherExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';
                            }
                        }).catch(function () {
                        });
                    }
                })
            }
        },
    })

</script>
</body>
</html>
