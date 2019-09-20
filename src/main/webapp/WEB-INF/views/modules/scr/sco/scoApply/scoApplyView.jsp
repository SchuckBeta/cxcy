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
            <applicant-info-credit :user="user" :student="studentExpansion"></applicant-info-credit>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane v-if="creditApply.auditStatus !== '1'" label="申请认定学分" name="firstCreditTab">
                <tab-pane-content>
                    <credit-course-apply :apply="creditApply"></credit-course-apply>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="课程信息" name="secondCreditTab">
                <tab-pane-content>
                    <credit-course-info :course="scoCourse"></credit-course-info>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane v-if="creditApply.auditStatus !== '1'" label="审核记录" name="threeCreditTab">
                <tab-pane-content>
                    <credit-course-audit :id="creditApply.id"></credit-course-audit>
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
            var studentExpansion = JSON.parse(JSON.stringify(${fns: toJson(studentExpansion)}));
            var officeList = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            var scoCourse = JSON.parse(JSON.stringify(${fns: toJson(scoCourse)}));
            var scoApply = JSON.parse(JSON.stringify(${fns: toJson(scoApply)}));
            return {
                studentExpansion: studentExpansion,
                creditApply: scoApply,
                officeList: officeList,
                scoCourse: scoCourse,
                tabActiveName: scoApply.auditStatus !== '1' ? 'firstCreditTab' : 'secondCreditTab'
            }
        },
        computed: {
            user: function () {
                var loginCurUser = this.loginCurUser;
                if (this.officeEntity) {
                    loginCurUser.professional = this.officeEntity[loginCurUser.professional]
                }
                return loginCurUser;
            },
            officeEntity: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },


        },
        methods: {
            goToBack: function () {
                history.go(-1);
            },

        },
        created: function () {
        }
    })

</script>
</body>
</html>