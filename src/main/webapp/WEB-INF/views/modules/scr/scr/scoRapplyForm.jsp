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
        <edit-bar second-name="创新创业学分审核"></edit-bar>
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

            <el-tab-pane label="审核记录" name="secondCreditTab">
                <tab-pane-content>
                    <credit-audit-list is-admin :credit-id="scrId" :type="creditType"></credit-audit-list>
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
                <el-input v-model="creditAuditForm.score" maxlength="15" style="width: 200px;"
                          @change="handleChangeScore">
                    <span slot="append">分</span>
                </el-input>
                <span v-if="!isSkill && ptype === '2'" class="el-form-item-expository">请填写准予认定学分分值，然后分配组员认定学分</span>
            </el-form-item>
            <el-form-item v-if="scoRdetail.isCondcheck === '1'" label="实际分值：">{{creditApply.condVal}}分<span
                    v-if="scoRdetail.isCondcheck === '1'" class="el-form-item-expository"><span class="red">{{scoRdetail.condType | selectedFilter(scoRuleTypesEntries)}}{{scoRdetail.condition}}分给予认定学分{{scoRdetail.score}}分</span></span>
            </el-form-item>
            <template v-if="!isSkill && ptype === '2'">
                <credit-team-member v-show="memberList.length > 1" :declare-id="user.id" ref="CreditTeamMember"
                                    :total-score="creditAuditForm.score"
                                    hasaffirmscore is-audit
                                    :keep-npoint="keepNpoint"
                                    :sco-ratios="scoRatios"
                                    :is-round="isRound"
                                    :disabled="creditTeamMemberDisabled"
                                    @change="handleChangeMemberList"
                                    :member-list="memberList"></credit-team-member>
            </template>
            <el-form-item v-show="calculateRules.isLowSco === '1'" label="计算分值规则：" required>
                <span class="red">{{calculateText}}</span>
            </el-form-item>
            <el-form-item v-if="!isSkill" label="相同项目：" prop="appIds">
                <el-select v-model="creditAuditForm.appIds" filterable placeholder="请选择" @change="handleChangeAppId">
                    <el-option label="无" value="-1"></el-option>
                    <el-option v-for="item in myPros" :label="item.name" :value="item.id" :key="item.id"></el-option>
                </el-select>
                <span class="el-form-item-expository">同一个项目记最高分值</span>
            </el-form-item>
            <div v-show="creditAuditForm.appIds && creditAuditForm.appIds != '-1'">
                <el-form-item label="证明材料：">
                    <e-file-item v-if="sysAttachmentList" v-for="item in sysAttachmentList" :key="item.uid" :file="item"
                                 :show="false"></e-file-item>
                </el-form-item>
                <el-form-item label="标准：">
                    {{myScoApply.rdetail ? myScoApply.rdetail.name : ''}}
                </el-form-item>
            </div>
            <el-form-item label="审核：" prop="atype" style="margin-bottom: 0">
                <el-col :span="10">
                    <el-form-item label-width="0" prop="atype">
                        <el-select v-model="creditAuditForm.atype">
                            <el-option v-for="item in auditStatues" :label="item.remark" :value="item.key"
                                       :key="item.key"></el-option>
                        </el-select>
                    </el-form-item>
                </el-col>
                <el-col v-if="!isSkill" :span="12">
                    <el-form-item label-width="0" prop="isHalf">
                        <el-checkbox v-model="creditAuditForm.isHalf" true-label="1" false-label="0"
                                     @change="handleChangeIsHalf">未在毕业前完成（学分减半）
                        </el-checkbox>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item label="建议及意见：" prop="remarks" :rules="creditAuditForm.atype == '0' ? remarksRules : []">
                <el-input type="textarea" maxlength="2000" :rows="3" v-model="creditAuditForm.remarks"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" @click.stop.prevent="validatePwEnterAuditForm" :disabled="disabled"
               size="mini">提 交</el-button>
            <div class="credit-audit-tip">
                <p>认定标准及备注：</p>
                <p class="white-space-pre">{{creditApply.rdetail ? creditApply.rdetail.remarks : ''}}</p>
            </div>
  </span>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.creditDetailMixin],
        data: function () {
            var validateScore = this.validateScore;
            return {
                scrId: '${scrId}',
                creditType: '${creditType}',
                SKILLCREDITID: '${SKILLCREDITID}',
                user: {},
                scoRdetail: {},
                studentExpansion: {},
                creditApply: {},
                loading: false,
                auditStatues: [],
                tabActiveName: 'firstCreditTab',

                dialogVisibleAudit: false,
                disabled: false,
                creditAuditForm: {
                    id: '${scrId}',
                    score: '',
                    atype: '',
                    appIds: '-1',
                    ptype: '',
                    isHalf: '',
                    remarks: ''
                },
                creditAuditRules: {
                    score: [
                        {required: true, message: '请输入认定学分分值', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    atype: [
                        {required: true, message: '请选择审核结果', trigger: 'change'}
                    ],
                    appIds: [
                        {required: true, message: '请选择相同项目', trigger: 'change'}
                    ]
                },
                remarksRules: [
                    {required: true, message: '请填写不通过理由', trigger: 'blur'}
                ],
                memberList: [],
                scoRulePbs: [],
                scoRuleList: [],
                ptype: '',
                myPros: [],
                isFirst: true,
                keepNpoint: '0',
                isRound: false,
                globalCalculateRules: {
                    isLowSco: '',
                    isSnum: '',
                    snumMin: '',
                    snumVal: '',
                    lowSco: '',
                    lowScoMax: '',
                    isSnumlimit: '',
                    snumlimit: ''
                },
                sysAttachmentList: [],
                totalMessageError: []
            }
        },
        computed: {
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
            isSkill: function () {
                var id = this.creditApply.rule ? this.creditApply.rule.id : '';
                return this.SKILLCREDITID === id;
            },
            dialogWidth: function () {
                return !this.isSkill ? '720px' : '520px';
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
            },

            scoRuleListEntries: function () {
                return this.getEntries(this.scoRuleList, {
                    label: 'name',
                    value: 'id'
                })
            },
            scoRule: function () {
                var lruleId = this.creditApply.lrule ? this.creditApply.lrule.id : '';
                var i = 0;
                while (i < this.scoRuleList.length) {
                    var item = this.scoRuleList[i];
                    if (item.id === lruleId) {
                        return item;
                    }
                    i++;
                }
                return {};
            },

            myScoApply: function () {
                var appIds = this.creditAuditForm.appIds;
                var i = 0;
                while (i < this.myPros.length) {
                    if (this.myPros[i].id === appIds) {
                        return this.myPros[i]
                    }
                    i++;
                }
                return {};
            },

            calculateRules: function () {
                var rdetail = this.creditApply.rdetail || {};
                var globalCalculateRules = this.globalCalculateRules;
                if (rdetail.isLowSco === '1') {
                    return {
                        isLowSco: rdetail.isLowSco,
                        lowSco: parseFloat(rdetail.lowSco),
                        lowScoMax: parseFloat(rdetail.lowScoMax)
                    }
                }
                if (globalCalculateRules.isSnum === '1') {
                    return {
                        isLowSco: globalCalculateRules.isSnum,
                        lowSco: parseFloat(globalCalculateRules.snumMin),
                        lowScoMax: parseFloat(globalCalculateRules.snumVal)
                    }
                }
                return {};
            },

            calculateText: function () {
                var calculateRules = this.calculateRules;
                return "认定的学分低于" + calculateRules.lowSco + '分，按照' + calculateRules.lowScoMax + '分计算'
            },

            scoRuleTypesEntries: function () {
                return this.getEntries(this.regTypes, {
                    label: 'rulename',
                    value: 'key'
                })
            },
        },
        methods: {

            handleChangeAppId: function (appId) {
                var self = this;
                this.$axios.get('/scr/scoRapply/ajaxFiles?id=' + appId).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.sysAttachmentList = data.data || [];
                    } else {
                        self.sysAttachmentList = [];
                    }
                })
            },

            handleChangeIsHalf: function (value) {
                var snum = 1;
                if (value === '1') {
                    snum = 0.5
                } else {
                    snum = 2;
                }
                if (this.creditAuditForm.score != '') {
                    this.creditAuditForm.score = this.creditAuditForm.score * snum;
                    this.$nextTick(function () {
                        if (this.ptype === '2') {
                            this.$refs.CreditTeamMember.setAFScore();
                        }
                    })
                }
            },

            handleCloseAudit: function () {
                this.dialogVisibleAudit = false;
            },

            openAuditDialog: function () {
                this.dialogVisibleAudit = true;
                this.$nextTick(function () {
                    if (this.isFirst && !this.isSkill && this.ptype === '2') {
                        this.isFirst = false;
                        this.$refs.CreditTeamMember.setAFScore();
                    }
                })
            },

            validatePwEnterAuditForm: function () {
                var self = this;
                this.$refs.creditAuditForm.validate(function (valid) {
                    if (valid) {
                        if (!self.isSkill && self.ptype === '2') {
                            self.validateScoreTotal();
//                            self.validateScoreOther();
                        } else {
                            self.validateScoreOther();
                        }

                    }
                })
            },


            validateScoreOther: function () {
                var self = this;
                var user = this.user;
                var globalCalculateRules = this.globalCalculateRules;
                var creditApply = this.creditApply;
                var rdetail = creditApply.rdetail;
                var hasRDetailError = false;
                var hasGlobalCRError = false;
                var creditAuditForm = this.creditAuditForm;
                this.totalMessageError = [];
                var score = this.creditAuditForm.score;
                var calculateRules = this.calculateRules;
                if(this.creditAuditForm.atype === '1'){
                    if (!self.isSkill && self.ptype === '2') {
                        self.setMemberListLowScoMax();
                    }else {
                        if (calculateRules.isLowSco === '1' && parseFloat(score) != 0 && parseFloat(score) < calculateRules.lowSco) {
                            this.creditAuditForm.score = calculateRules.lowScoMax;
                        }
                    }
                }
                self.submitCreditAuditForm();
//                if (this.creditAuditForm.atype === '0') {
//                    this.submitCreditAuditForm();
//                    return false;
//                } else {
//                    this.disabled = true;
//                    this.$axios.get('/scr/scoRapply/ajaxScoSum?id=' + this.scrId).then(function (response) {
//                        var data = response.data;
//                        var entity = {};
//                        if (data.status === 1) {
//                            data = data.data || [];
//                            data.forEach(function (item) {
//                                entity[item.userId] = item;
//                            });
//                            var rDetailVal = entity[user.id].rDetailVal;
//                            var sumVal = entity[user.id].sumVal;
//                            if (rdetail.isJoin === '1') {
//                                var rDetailTotal = rDetailVal + parseFloat(creditAuditForm.score);
//                                if (rDetailTotal > parseFloat(rdetail.joinMax)) {
//                                    hasRDetailError = true;
//                                    self.generateErrorText({user: user}, 'rDetailVal', rDetailTotal - parseFloat(rdetail.joinMax), rDetailVal)
//                                }
//                            }
//                            if (globalCalculateRules.isSnumlimit === '1') {
//                                var sumValTotal = sumVal + creditAuditForm.score;
//                                if (sumValTotal > parseFloat(globalCalculateRules.snumlimit)) {
//                                    hasGlobalCRError = true;
//                                    self.generateErrorText({user: user}, 'snumlimit', sumValTotal - parseFloat(globalCalculateRules.snumlimit), sumVal)
//                                }
//                            }
//                            if (self.totalMessageError.length === 0) {
//                                self.submitCreditAuditForm();
//                            } else {
//                                if (rdetail.isJoin === '1') {
//                                    self.totalMessageError.unshift('<p class="red">计算分值规则：累计分值累计不超过' + rdetail.joinMax + '分</p>');
//                                }
//                                if (globalCalculateRules.isSnumlimit === '1') {
//                                    self.totalMessageError.unshift('<p class="red">认定学分规则：认定总分不得超过' + globalCalculateRules.snumlimit + '分</p>');
//                                }
//                                self.totalMessageError.unshift('<div>');
//                                self.totalMessageError.push('</div>');
//                                var sumErrorTextHtml = self.totalMessageError.join('');
//                                self.$alert(sumErrorTextHtml, '提示', {
//                                    dangerouslyUseHTMLString: true,
//                                    closeOnClickModal: false,
//                                    showClose: false,
//                                    type: 'error'
//                                }).then(function () {
//                                })
//                            }
//                        }
//
//                        self.disabled = false;
//                    }).catch(function () {
//                        self.disabled = false;
//                    })
            },

            validateScoreTotal: function () {
                var memberList = this.memberList;
                var self = this;
                var isValid = memberList.every(function (item) {
                    return !item.rateError && !item.aFScoreError;
                });
                var globalCalculateRules = this.globalCalculateRules;
                var creditApply = this.creditApply;
                var rdetail = creditApply.rdetail;
                var hasRDetailError = false;
                var hasGlobalCRError = false;
                this.totalMessageError = [];
                if (isValid) {
                    var score = this.creditAuditForm.score;
                    var calculateRules = this.calculateRules;
                    if(this.creditAuditForm.atype === '1'){
                        if (!self.isSkill && self.ptype === '2') {
                            self.setMemberListLowScoMax();
                        }else {
                            if (calculateRules.isLowSco === '1' && parseFloat(score) != 0 && parseFloat(score) < calculateRules.lowSco) {
                                this.creditAuditForm.score = calculateRules.lowScoMax;
                            }
                        }
                    }
                    self.submitCreditAuditForm();
//                    if (this.creditAuditForm.atype === '0') {
//                    } else {
//                        self.setMemberListLowScoMax();
//                    }
//                    self.submitCreditAuditForm();
//                    return false;
//                    this.disabled = true;
//                    this.$axios.get('/scr/scoRapply/ajaxScoSum?id=' + this.scrId).then(function (response) {
//                        var data = response.data;
//                        var entity = {};
//                        if (data.status === 1) {
//                            data = data.data || [];
//                            data.forEach(function (item) {
//                                entity[item.userId] = item;
//                            });
//                            var memberListLowScoMax = self.getMemberListLowScoMax();
//                            var i = 0;
//                            while (i < memberListLowScoMax.length) {
//                                var memberItem = memberListLowScoMax[i];
//                                var rDetailVal = entity[memberItem.user.id].rDetailVal;
//                                var sumVal = entity[memberItem.user.id].sumVal;
//                                if (rdetail.isJoin === '1') {
//                                    var rDetailTotal = rDetailVal + memberItem.aFScore;
//                                    if (rDetailTotal > parseFloat(rdetail.joinMax)) {
//                                        hasRDetailError = true;
//                                        self.generateErrorText(memberItem, 'rDetailVal', rDetailTotal - parseFloat(rdetail.joinMax), rDetailVal)
//                                    }
//                                }
//                                if (globalCalculateRules.isSnumlimit === '1') {
//                                    var sumValTotal = sumVal + memberItem.aFScore;
//                                    if (sumValTotal > parseFloat(globalCalculateRules.snumlimit)) {
//                                        hasGlobalCRError = true;
//                                        self.generateErrorText(memberItem, 'snumlimit', sumValTotal - parseFloat(globalCalculateRules.snumlimit), sumVal)
//                                    }
//                                }
//                                i++;
//                            }
//                            if (self.totalMessageError.length === 0) {
//                                self.setMemberListLowScoMax();
//                                self.submitCreditAuditForm();
//                            } else {
//                                if (rdetail.isJoin === '1') {
//                                    self.totalMessageError.unshift('<p class="red">计算分值规则：累计分值累计不超过' + rdetail.joinMax + '分</p>');
//                                }
//                                if (globalCalculateRules.isSnumlimit === '1') {
//                                    self.totalMessageError.unshift('<p class="red">认定学分规则：认定总分不得超过' + globalCalculateRules.snumlimit + '分</p>');
//                                }
//                                self.totalMessageError.unshift('<div>');
//                                self.totalMessageError.push('</div>');
//                                var sumErrorTextHtml = self.totalMessageError.join('');
//                                self.$alert(sumErrorTextHtml, '提示', {
//                                    dangerouslyUseHTMLString: true,
//                                    closeOnClickModal: false,
//                                    showClose: false,
//                                    type: 'error'
//                                }).then(function () {
//                                    self.creditAuditForm.atype = '0';
//                                    sumErrorTextHtml = sumErrorTextHtml.replace(/\<\/?(p|div).*?\>/gi, '\n');
//                                    sumErrorTextHtml = sumErrorTextHtml.replace(/\<\/?.*?\>/gi, '');
//                                    sumErrorTextHtml = sumErrorTextHtml.replace(/(^\n*)|(\n*$)/g, '');
//                                    sumErrorTextHtml = sumErrorTextHtml.replace(/\n(?=\n)/g, '');
//                                    self.creditAuditForm.remarks = sumErrorTextHtml;
//                                    console.log(self.creditAuditForm.remarks)
//                                })
//                            }
//                        }
//                        self.disabled = false;
//                    }).catch(function (error) {
//                        self.disabled = false;
//                        self.$message.error(self.xhrErrorMsg)
//                    });
//                    this.setMemberListLowScoMax();
//                    total = memberList.reduce(function (score, item) {
//                        return score + parseFloat(item.aFScore);
//                    }, 0);
//                    if (Math.abs(total - parseInt(this.creditAuditForm.score)) > 1) {
//                        this.$confirm('组员准予认定学分的总和不符，是否继续提交？', '提示', {
//                            confirmButtonText: '确定',
//                            cancelButtonText: '取消',
//                            type: 'warning'
//                        }).then(function () {
//                            self.submitCreditAuditForm();
//                        }).catch(function () {
//
//                        })
//                    } else {
//                        self.submitCreditAuditForm();
//                    }
                } else {
                    this.$message.error('学分配比或认定学分的值填写错误，请检查红色边框的值')
                }
            },


            generateErrorText: function (memberItem, type, upVal, score) {
                upVal = upVal.toFixed('3');
                if (type === 'rDetailVal') {
                    this.totalMessageError.push('<p>' + memberItem.user.name + '累计已获得标准分' + score + '，累计此次分值将超过最大标准分<span class="red">' + upVal + '</span>分</p>')
                } else {
                    this.totalMessageError.push('<p>' + memberItem.user.name + '累计已获得认定学分' + score + '，累计此次认定分值将超过最大认定总分<span class="red">' + upVal + '</span>分</p>')
                }
            },

            getMemberListLowScoMax: function () {
                var calculateRules = this.calculateRules;
                return this.memberList.map(function (item) {
                    if (parseFloat(item.aFScore) != 0 && parseFloat(item.aFScore) < calculateRules.lowSco) {
                        item.aFScore = calculateRules.lowScoMax;
                    }
                    item.aFScore = parseFloat(item.aFScore);
                    return item;
                })
            },


            setMemberListLowScoMax: function () {
                var calculateRules = this.calculateRules;
                this.memberList.forEach(function (item) {
                    if (calculateRules.isLowSco === '1' && parseFloat(item.aFScore) != 0 && parseFloat(item.aFScore) < calculateRules.lowSco) {
                        item.aFScore = calculateRules.lowScoMax;
                    }
                })
                if(this.memberList.length === 1){
                    if (calculateRules.isLowSco === '1' && parseFloat(this.creditAuditForm.score) != 0 && parseFloat(this.creditAuditForm.score) < calculateRules.lowSco) {
                        this.creditAuditForm.score = calculateRules.lowScoMax;
                    }
                }
            },

            isConformCalculateRules: function () {
                var memberList = this.memberList;
                var calculateRules = this.calculateRules;
                if (!calculateRules.isLowSco) {
                    return true;
                }
                return memberList.every(function (item) {
                    return parseFloat(item.aFScore) >= calculateRules.lowSco;
                })
            },

            getParams: function () {
                var creditAuditForm = JSON.parse(JSON.stringify(this.creditAuditForm));
                if (this.isSkill) {
                    return {
                        id: creditAuditForm.id,
                        score: creditAuditForm.score,
                        atype: creditAuditForm.atype,
                        ptype: creditAuditForm.ptype,
                        remarks: creditAuditForm.remarks
                    };
                } else {
                    creditAuditForm.appIds = creditAuditForm.appIds == '-1' ? '' : creditAuditForm.appIds;
                    if (this.ptype === '2') {
                        creditAuditForm.members = this.memberList.map(function (item) {
                            return {
                                uid: item.user.id,
                                rate: parseInt(item.rate),
                                score: item.aFScore
                            }
                        })
                    }
                }
                return creditAuditForm;
            },

            submitCreditAuditForm: function () {
                var self = this;
                this.disabled = true;
                this.$axios.post('/scr/scoRapply/ajaxAudit', this.getParams()).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.dialogVisibleAudit = false;
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: '保存成功'
                        }).then(function () {
                            location.href = '/a/scr/scoRapply/list';
                        }).catch(function () {
                        });
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/scr/scoRapplyList");
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                    self.disabled = false;
                })
            },

            goToBack: function () {
                history.go(-1);
            },

            handleChangeMemberList: function (memberList) {
                this.memberList = memberList;
            },


            //获取学分配比
            getScoPbList: function (id) {
                var self = this;
                this.$axios.get('/scr/scoRulePb/ajaxScoPbList?rule.id=' + id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRulePbs = data.data || [{num: 1, val: 1}];
                    }
                }).catch(function () {

                })
            },

            handleChangeScore: function (value) {
                var self = this;
                if (!this.isSkill && this.ptype === '2') {
                    this.$refs.creditAuditForm.validateField('score', function (errors) {
                        if (!errors) {
                            self.$refs.CreditTeamMember.setAFScore();
                        }
                    })

                }
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
                        self.scoRdetail = rdetail;
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
                        self.ptype = data.lrule.ptype;
                        self.creditAuditForm.ptype = self.ptype;
                        self.creditAuditForm.score = rdetail.score;
                        if (self.ptype === '2') {
                            self.getScoPbList(data.lrule.id);
                        }
                        self.getMyPros();
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
                this.$axios.get('/sys/passNots').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },


            getScrRuleList: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/getScoRuleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRuleList = data.data || [];
                    }
                })
            },

            getMyPros: function () {
                var self = this;
                this.$axios.get('/scr/scoRapply/ajaxProjects?id=' + this.scrId).then(function (response) {
                    var data = response.data;
                    console.info(data);
                    if (data.status === 1) {
                        self.myPros = data.data || [];
                    }
                })
            },
            getCreditRule: function () {
                var self = this;
                this.$axios.get('/scr/scoRset/list').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || [];
                        if (data.length > 0) {
                            self.keepNpoint = data[0].keepNpoint;
                            self.isRound = data[0].isRprd === '0';
                            self.assignFormData(self.globalCalculateRules, data[0]);
                        }
                    }
                }).catch(function () {

                })
            },
            //正则范围类型接口
            getRegTypes: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/ajaxRegTypes?isAll=false').then(function (response) {
                    var data = response.data;
                    data = JSON.parse(data.data) || [];
                    self.regTypes = data;
                })
            },
        },
        created: function () {
            this.getScoDetail();
            this.getAuditStatues();
            this.getScrRuleList();
            this.getCreditRule();
            this.getRegTypes();
        }
    })

</script>
</body>
</html>