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
        <el-breadcrumb-item>学分认定</el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/scr/toFindScoForm">学分查询</a></el-breadcrumb-item>
        <el-breadcrumb-item>查看详情</el-breadcrumb-item>
    </el-breadcrumb>
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
                    <credit-audit-list :credit-id="scrId" :type="creditType"></credit-audit-list>
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
                scrId: '${id}',
                creditType: '${creditType}',
                user: {},
                studentExpansion: {},
                creditApply: {},
                loading: false,
                auditStatues: [],
                scoRuleList: [],
                tabActiveName: 'firstCreditTab'
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
                this.$axios.get('/scr/ajaxFindScoDetail?'+ Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data;
                        self.user = data.user;
                        self.studentExpansion = data.studentExpansion;
                        self.creditApply = data;
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
                this.$axios.get('/scr/ajaxFrontScoRstatuss?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },
            getScrRuleList: function () {
                var self = this;
                this.$axios.get('/scr/fronScoRuleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRuleList = data.data || [];
                    }
                })
            }
        },
        created: function () {
            this.getScoDetail();
            this.getScrRuleList();
            this.getAuditStatues();
        }
    })

</script>
</body>
</html>