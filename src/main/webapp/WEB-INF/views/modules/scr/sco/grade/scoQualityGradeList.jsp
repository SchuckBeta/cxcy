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
        <edit-bar></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info-credit :user="user" :loading="loading" :student="studentExpansion"></applicant-info-credit>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <div class="btn-audit-box">
            <el-button type="primary" size="mini" @click.stop="openAuditDialog">审核</el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="申请认定学分" name="firstCreditTab">
                <tab-pane-content>
                    <credit-apply-tab-view :credit-apply="creditApply" :loading="loading"></credit-apply-tab-view>
                </tab-pane-content>
            </el-tab-pane>

            <el-tab-pane v-if="creditApply.status !== '2'" label="审核记录" name="secondCreditTab">
                <tab-pane-content>
                    <credit-audit-list :credit-id="scrId" :type="creditType"></credit-audit-list>
                </tab-pane-content>
            </el-tab-pane>
        </el-tabs>
    </div>
    <div class="text-center">
        <el-button size="mini" @click.stop="goToBack">返回</el-button>
    </div>

    <el-dialog
            title="审核"
            :visible.sync="dialogVisibleAudit"
            :width="dialogWidth"
            :close-on-click-modal="false"
            :before-close="handleCloseAudit">
        <el-form :model="creditAuditForm" ref="creditAuditForm" :rules="creditAuditRules" :disabled="disabled"
                 size="mini" label-width="130px">
            <el-form-item label="准予认定学分分值：" prop="score">
                <el-input v-model="creditAuditForm.score" maxlength="11" style="width: 200px;" @change="handleChangeScore">
                    <span slot="append">分</span>
                </el-input>
                <span class="el-form-item-expository">请填写准予认定学分分值，然后分配组员认定学分</span>
            </el-form-item>
            <template v-if="isNotSkill">
                <credit-team-member :declare-id="user.id" ref="CreditTeamMember"
                                    :total-score="creditAuditForm.score"
                                    hasaffirmscore is-audit
                                    :sco-ratios="scoRatios"
                                    :disabled="creditTeamMemberDisabled"
                                    @change="handleChangeMemberList"
                                    :member-list="memberList"></credit-team-member>
                <el-form-item label="相同项目：" prop="projectId">
                    <el-select v-model="creditAuditForm.projectId" placeholder="请选择"></el-select>
                    <span class="el-form-item-expository">同一个项目记最高分值</span>
                </el-form-item>
            </template>
            <el-form-item label="审核：" style="margin-bottom: 0">
                <el-col :span="10">
                    <el-form-item label-width="0" prop="atype">
                        <el-select v-model="creditAuditForm.atype">
                            <el-option v-for="item in auditStatues" :label="item.name" :value="item.key" :key="item.key"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col  v-if="isNotSkill" :span="12">
                    <el-form-item label-width="0" prop="isHalf">
                        <el-checkbox v-model="creditAuditForm.isHalf" true-label="1" false-label="0">未在毕业前完成（学分减半）
                        </el-checkbox>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item prop="remarks" label="建议及意见：" :rules="creditAuditForm.atype === '4' ? remarksRules : []">
                <el-input type="textarea" maxlength="2000" :rows="3" v-model="creditAuditForm.remarks"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click.stop.prevent="validatePwEnterAuditForm" :disabled="disabled"
               size="mini">确 定</el-button>
  </span>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                scrId: 'f23d2c73032247e9829d4d5a9c2583a4',
                creditType: '1',
                user: {},
                studentExpansion: {},
                creditApply: {},
                loading: false,
                auditStatues: [],
                tabActiveName: 'firstCreditTab',

                dialogVisibleAudit: false,
                disabled: false,
                creditAuditForm: {
                    score: '',
                    atype: '',
                    isSame: '',
                    isHalf: '',
                    remarks: ''
                },
                creditAuditRules: {
                    score: [
                        {required: true, message: '请输入认定学分分值', trigger: 'blur'}
                    ],
                    atype: [
                        {required: true, message: '请选择审核结果', trigger: 'change'}
                    ]
                },
                remarksRules: [
                    {required: true, message: '请填写不通过理由', trigger: 'blur'}
                ],
                memberList: [],
                scoRulePbs: []
            }
        },
        computed: {
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
            isNotSkill: function () {
                return this.creditType !== '2'
            },
            dialogWidth: function () {
                return this.creditType === '1' ? '720px' : '520px';
            },
            scoRatios: function () {
                var entries = {};
                var i = 0;
                var scoRulePbs = this.scoRulePbs;
                while (i < scoRulePbs.length) {
                    entries[scoRulePbs[i].num] = scoRulePbs[i].val;
                    i++
                }
                return entries
            },
            creditTeamMemberDisabled: function () {
                return typeof this.creditAuditForm.score == 'undefined' || this.creditAuditForm.score == '' || this.disabled;
            }
        },
        methods: {
            handleCloseAudit: function () {
                this.dialogVisibleAudit = false;
            },

            openAuditDialog: function () {
                this.dialogVisibleAudit = true;
            },

            validatePwEnterAuditForm: function () {

            },

            goToBack: function () {
                history.go(-1);
            },

            handleChangeMemberList: function (memberList) {

            },

            //获取学分配比
            getScoPbList: function () {
                var self = this;
                this.$axios.get('/scr/scoRulePb/ajaxScoPbList?rule.id=13dfc36f685142a5897eb77364c270a2').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRulePbs = data.data;
                    }
                }).catch(function () {

                })
            },

            handleChangeScore: function (value) {
                this.$refs.CreditTeamMember.setAFScore();
            },

            getScoDetail: function () {
                var self = this;
                var params = {
                    id: this.scrId,
                    creditType: this.creditType
                };
                this.loading = true;
                this.$axios.post('/scr/scoRapply/ajaxFindScoDetail', params).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.user = data.user;
                        self.studentExpansion = data.studentExpansion;
                        self.creditApply = data;
                        var rdetail = data.rdetail;
                        var rule = rdetail.rule;
                        var parentIds = rule.parentIds + rule.id;
                        parentIds += ',' + rdetail.id;
                        parentIds = parentIds.split(',');
                        var scoRapplyCert = data.scoRapplyCert;
                        var scoRapplyMemberList = data.scoRapplyMemberList;
                        if (scoRapplyCert) {
                            var scoRapplyCertParentIds = scoRapplyCert.parentIds + ',' + scoRapplyCert.id;
                            scoRapplyCertParentIds = scoRapplyCertParentIds.split(',');
                        }
                        if (scoRapplyMemberList) {
                            var memberList = scoRapplyMemberList.map(function (item) {
                                item.user.userId = item.user.id;
                                item.user['userzz'] = item.userId === self.declareId ? '0' : '1';
                                item.user["userType"] = '1';
                                item.aFScore = '';
                                return item;
                            });
                            self.memberList = JSON.parse(JSON.stringify(memberList));
                        }
                        self.creditApplyDate = data.applyDate;
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                    self.loading = false;
                })
            },

            getAuditStatues: function () {
                var self = this;
                this.$axios.get('/scr/scoRapply/ajaxScoRstatuss?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },
        },
        created: function () {
            this.getScoDetail();
            this.getAuditStatues();
            this.getScoPbList();
        }
    })

</script>
</body>
</html>