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
        <edit-bar second-name="审核"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body" style="min-width: 1086px;">
            <applicant-info :pw-enter-id="pwEnterId"></applicant-info>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space">
        <div class="btn-audit-box">
            <el-button type="primary" size="mini" @click.stop.prevent="dialogVisibleAudit=true">审核</el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane v-if="type == '2'" label="企业信息" name="firstPwEnterTab">
                <tab-pane-content>
                    <pw-enter-company :pw-enter-id="pwEnterId"></pw-enter-company>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="团队信息" name="secondPwEnterTab">
                <tab-pane-content>
                    <pw-enter-team :pw-enter-id="pwEnterId"></pw-enter-team>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="项目信息" name="thirdPwEnterTab">
                <tab-pane-content>
                    <pw-enter-project-view :pw-enter-id="pwEnterId"></pw-enter-project-view>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="场地要求" name="fourPwEnterTab">
                <tab-pane-content>
                    <pw-enter-site :pw-enter-id="pwEnterId"></pw-enter-site>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="入驻记录" name="fivePwEnterTab">
                <tab-pane-content>
                    <pw-enter-record-list :pw-enter-id="pwEnterId"></pw-enter-record-list>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="成果记录" name="sixPwEnterTab">
                <tab-pane-content>
                    <pw-enter-result-list :pw-enter-id="pwEnterId"></pw-enter-result-list>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="审核记录" name="sevenPwEnterTab">
                <tab-pane-content>
                    <pw-enter-result :pw-enter="pwEnter"></pw-enter-result>
                </tab-pane-content>
            </el-tab-pane>
        </el-tabs>
    </div>
    <el-dialog
            title="审核"
            :visible.sync="dialogVisibleAudit"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseAudit">
        <el-form :model="pwEnterAuditForm" ref="pwEnterAuditForm" :rules="pwEnterAuditRules" size="mini"
                 :disabled="disabled"
                 label-width="120px">
            <el-form-item label="审核：" prop="isPass">
                <el-select v-model="pwEnterAuditForm.isPass">
                    <el-option value="1" label="通过"></el-option>
                    <el-option value="2" label="不通过"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item label="备注：" prop="remarks">
               <el-input type="textarea" :rows="3" v-model="pwEnterAuditForm.remarks"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click.stop.prevent="validatePwEnterAuditForm" :disabled="disabled" size="mini">确 定</el-button>
  </span>
    </el-dialog>
</div>

<script type="text/javascript">
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwEnter = JSON.parse(JSON.stringify(${fns: toJson(pwEnter)})) || {};
            return {
                pwEnterId: pwEnter.id,
                type: pwEnter.type,
                tabActiveName: pwEnter.type === '2' ? 'firstPwEnterTab' : 'secondPwEnterTab',
                dialogVisibleAudit: false,
                pwEnter: pwEnter,
                pwEnterAuditForm: {
                    id:  pwEnter.id,
                    isPass: '',
                    term: '',
                    hasTerm: '',
                    remarks: '',
                    termDate: ''
                },
                sysDate: '',
                yearOptions: [],
                disabled: false
            }
        },
        computed: {
            sysDateAfter: function () {
                var date = new Date();
                if (this.sysDate) {
                    date = new Date(this.sysDate);
                    date.setDate(date.getDate() + 1);
                }
                return moment(date).format('YYYY-MM-DD');
            },
            pwEnterAuditRules: function () {
                var pwEnterAuditForm = this.pwEnterAuditForm;
                var isRequired = pwEnterAuditForm.isPass === '2';
                return {
                    isPass: [
                        {required: true, message: '请选择审核', trigger: 'change'}
                    ],
                    hasTerm: [
                        {required: !isRequired, message: '请选择入驻有效期', trigger: 'change'}
                    ],
                    termDate: [
                        {required: pwEnterAuditForm.hasTerm === '-1', message: '请选择自定义时间', trigger: 'change'}
                    ],
                    remarks: [
                        {required: isRequired, message: '请填写不通过建议及意见', trigger: 'blur'},
                        {max: 200, message: '请输入不大于200字建议及意见', trigger: 'blur'}
                    ]
                }
            },
//            pickerOptions: {
//                disabledDate: function (time) {
//                    return time.getTime() < (Date.now() + 24 * 3600 * 1000);
//                }
//            },
        },
        methods: {
            handleCloseAudit: function () {
                this.dialogVisibleAudit = false;
            },

            handleChangeAType: function (value) {
                if (value === '0') {
                    this.pwEnterAuditForm.hasTerm = '';
                }
            },

            handleChangeHasTerm: function (value) {
                if (value !== '-1') {
                    this.pwEnterAuditForm.termDate = '';
                }
            },

            getSysDate: function () {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    self.sysDate = moment(response.data).format('YYYY-MM-DD');
//                    var date1 = new Date(self.sysDate);
//                    var date2 = new Date(self.sysDate);
//                    date1.setMonth(date1.getMonth() + 6);
//                    date2.setMonth(date2.getMonth() + 12);
//                    var halfYear = moment(date1).format('YYYY-MM-DD');
//                    var oneYear = moment(date2).format('YYYY-MM-DD');
//                    self.yearOptions = [
//                        {label: ('半年（' + halfYear + '）'), value: halfYear},
//                        {label: ('一年（' + oneYear + '）'), value: oneYear},
//                        {label: '自定义', value: '-1'}
//                    ]
                })
            },

            getYearOptions: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxDtypeTerms?isAll=false').then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        self.yearOptions = JSON.parse(data.data) || [];
                        self.yearOptions.push({remarks: '自定义', num: '-1'})
                    }
                })
            },

            getAuditParams: function () {
                var pwEnterAuditForm = this.pwEnterAuditForm;
                return {
                    id: pwEnterAuditForm.id,
                    isPass: pwEnterAuditForm.isPass,
                    remarks: pwEnterAuditForm.remarks
                }
            },

            validatePwEnterAuditForm: function () {
                var self = this;
                this.$refs.pwEnterAuditForm.validate(function (valid) {
                    if (valid) {
                        self.submitAudit();
                    }
                })
            },

            submitAudit: function () {
                var self = this;
                var params = this.getAuditParams();
                this.disabled = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwEnter/ajaxAuditBGSH',
                    params: params
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.dialogVisibleAudit = false;
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwEnter/ajaxCountToAudit");
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: '审核完成'
                        }).then(function () {
                            location.href = '/a/pw/pwEnter/listBGSH';
                        }).catch(function () {
                        });
                    } else {
                        self.$alert(data.msg, "提示", {
                            type: 'warning'
                        });
                    }
                    self.disabled = false;
                }).catch(function (error) {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg);
                })
            }
        },
        mounted: function () {
//            this.getSysDate();
//            this.getYearOptions();
        }
    })
</script>
</body>
</html>