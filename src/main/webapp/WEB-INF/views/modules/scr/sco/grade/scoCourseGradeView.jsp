<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="查看"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info-credit :user="user" :student="studentExpansion"></applicant-info-credit>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="申请认定学分" name="firstCreditTab">
                <tab-pane-content>
                    <credit-course-apply :apply="creditApply"></credit-course-apply>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="课程信息" name="secondCreditTab">
                <tab-pane-content>
                    <credit-course-info :course="scoCourse"></credit-course-info>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="审核记录" name="threeCreditTab">
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


<script>
    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var studentExpansion = JSON.parse(JSON.stringify(${fns: toJson(studentExpansion)}));
            var officeList = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            var scoCourse = JSON.parse(JSON.stringify(${fns: toJson(scoCourse)}));
            var scoApply = JSON.parse(JSON.stringify(${fns: toJson(scoApply)}));
            var applyUser = JSON.parse(JSON.stringify(${fns: toJson(applyUser)})) || {};

            return {
                studentExpansion: studentExpansion,
                creditApply: scoApply,
                officeList: officeList,
                scoCourse: scoCourse,
                applyUser: applyUser,
                auditStatues: [],
                tabActiveName: 'firstCreditTab',
            }
        },
        computed: {
            user: function () {
                var loginCurUser = this.applyUser;
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
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },

        },
        methods: {
            goToBack: function () {
                history.go(-1);
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
            this.getAuditStatues()
        }
    })
</script>

</body>
</html>