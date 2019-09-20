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
        <edit-bar second-name="变更"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body" style="min-width: 1086px;">
            <applicant-info :pw-enter-id="pwEnterId" @change-photo="changePhoto" is-bg></applicant-info>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space">
        <div class="btn-audit-box">
            <el-button type="primary" size="mini" :disabled="disabled" @click.stop.prevent="saveForm">保存</el-button>
        </div>
        <enter-form-group ref="enterFormGroup" is-admin :pw-enter-id="pwApplyForm.id" :disabled="disabled"
                          :form-names="formNames">
            <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
                <el-tab-pane v-if="pwApplyForm.type == '2'" label="企业信息" name="firstPwEnterTab">
                    <tab-pane-content>
                        <e-panel label="企业信息">
                            <enterprise-form ref="enterpriseForm" is-admin :app-type="appType" style="max-width: 960px;"></enterprise-form>
                        </e-panel>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="团队信息" name="secondPwEnterTab">
                    <tab-pane-content>
                        <e-panel label="团队信息">
                            <link-team-form ref="LinkTeamForm" is-admin></link-team-form>
                        </e-panel>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="项目信息" name="thirdPwEnterTab">
                    <tab-pane-content>
                        <e-panel label="项目信息">
                            <link-project-form is-admin :status="pwApplyForm.status" ref="linkProjectForm"></link-project-form>
                        </e-panel>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="场地要求" name="fourPwEnterTab">
                    <tab-pane-content>
                        <e-panel label="场地要求">
                            <pw-site-form ref="pwSiteForm" is-admin style="max-width: 520px;"></pw-site-form>
                        </e-panel>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="入驻记录" name="fivePwEnterTab">
                    <tab-pane-content>
                        <pw-enter-record-list :pw-enter-id="pwApplyForm.id"></pw-enter-record-list>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane label="成果记录" name="sixPwEnterTab">
                    <tab-pane-content>
                        <pw-enter-result-list :pw-enter-id="pwEnterId"></pw-enter-result-list>
                    </tab-pane-content>
                </el-tab-pane>
                <el-tab-pane v-if="pwEnter.status != '0'" label="审核记录" name="sevenPwEnterTab">
                    <tab-pane-content>
                        <pw-enter-result :pw-enter="pwEnter"></pw-enter-result>
                    </tab-pane-content>
                </el-tab-pane>
            </el-tabs>
        </enter-form-group>
    </div>
</div>

<script type="text/javascript">
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwEnter = JSON.parse(JSON.stringify(${fns: toJson(pwEnter)}));
            var user = pwEnter.applicant || {};
            return {
                pwEnterId: pwEnter.id,
                type: pwEnter.type,
                pwEnter: pwEnter,
                tabActiveName: pwEnter.type === '2' ? 'firstPwEnterTab' : 'secondPwEnterTab',
                disabled: false,
                pwApplyForm: {
                    id: pwEnter.id,
                    type: pwEnter.type,
                    isTemp: pwEnter.isTemp,
                    declarePhoto: pwEnter.declarePhoto,
                    declareId: user.id,
                    applicant: {
                        id: user.id
                    },
                    appType: '2',
                    createDate: pwEnter.createDate,
                    status: pwEnter.status
                },
                enterType: pwEnter.type,
                appType: pwEnter.appType
            }
        },
        computed: {
            formNames: function () {
                var formNames = ['LinkTeamForm', 'LinkProjectForm', 'PwSiteForm'];
                if (this.hasEnterpriseForm) {
                    formNames.push('EnterpriseForm')
                }
                return formNames;
            },
            hasEnterpriseForm: function () {
                return this.pwApplyForm.type === '2'
            }
        },
        methods: {
            saveForm: function () {
                this.enterFormGroupPromise();
            },

            changePhoto: function (url) {
                this.pwApplyForm.declarePhoto = url
            },

            enterFormGroupPromise: function () {
                var self = this;
                this.$refs.enterFormGroup.saveEnterForm().then(function (data) {
                    var pwApplyForm = JSON.parse(JSON.stringify(self.pwApplyForm));
                    var postParams = {};
                    data.forEach(function (item) {
                        postParams = Object.assign(pwApplyForm, item);
                    })
                    if(pwApplyForm.teamId){
                        postParams.team = {id: pwApplyForm.teamId};
                    }
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
            isFileUploading: function (params) {
                return params.pwCompany.sysAttachmentList.length > 0 && params.pwCompany.sysAttachmentList.some(function (item) {
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
                };
                this.disabled = true;
                this.$axios.post('/pw/pwEnter/ajaxPwEnterChange', params).then(function (response) {
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
                            showCancelButton: true,
                            cancelButtonText: '返回列表',
                            showClose: false,
                            message: '保存成功'
                        }).then(function () {

                        }).catch(function () {
                            location.href = document.referrer;
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
            }
        },
        mounted: function () {
        }
    })
</script>
</body>
</html>