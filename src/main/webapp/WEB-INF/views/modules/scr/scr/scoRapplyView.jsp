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
        <edit-bar second-name="创新创业学分详情"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info-credit :user="user" :loading="loading" :student="studentExpansion"></applicant-info-credit>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
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
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                scrId: '${scrId}',
                creditType: '${creditType}',
                SKILLCREDITID: '${SKILLCREDITID}',
                user: {},
                studentExpansion: {},
                creditApply: {},
                loading: false,
                auditStatues: [],
                tabActiveName: 'firstCreditTab',
                scoRuleList: [],
            }
        },
        computed: {
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
            scoRuleListEntries: function () {
                return this.getEntries(this.scoRuleList, {
                    label: 'name',
                    value: 'id'
                })
            }
        },
        methods: {


            goToBack: function () {
                history.go(-1);
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


            getScrRuleList: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/getScoRuleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRuleList = data.data || [];
                    }
                })
            }
        },
        created: function () {
            this.getScoDetail();
            this.getAuditStatues();
            this.getScrRuleList();
        }
    })

</script>
</body>
</html>