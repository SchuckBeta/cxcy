<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .team-detail-info_admin {
            width: 90%;
            margin: 0 auto;
        }

        .team-detail-info_admin .e-col-item {
            margin-bottom: 8px;
        }
    </style>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="团队详情"></edit-bar>
    </div>
    <div class="panel panel-project mgb-20">
        <div class="panel-header panel-header-title"><span>${teamDetails.name }</span></div>
        <div class="panel-body">
            <div class="team-detail-info_admin">
                <el-row :gutter="20" label-width="84px">
                    <el-col :span="12">
                        <e-col-item label="团队负责人：" align="right">${teamDetails.sponsor }</e-col-item>
                    </el-col>
                    <el-col :span="12">
                        <e-col-item label="所属学院：" align="right">${teamDetails.localCollege }</e-col-item>
                    </el-col>
                    <%--<el-col :span="8">--%>
                    <%--<e-col-item label="项目组人数：" align="right">${teamDetails.memberNum }人</e-col-item>--%>
                    <%--</el-col>--%>
                    <el-col :span="12">
                        <e-col-item label="校内导师：" align="right">${teamDetails.schoolTeacherNum }人
                        </e-col-item>
                    </el-col>
                    <el-col :span="12">
                        <e-col-item label="企业导师：" align="right">${teamDetails.enterpriseTeacherNum }人
                        </e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="团队介绍：" align="right"
                                    class="white-space-pre-static word-break">${teamDetails.summary }</e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="组员要求：" align="right"
                                    class="white-space-pre-static word-break">${teamDetails.membership }</e-col-item>
                    </el-col>
                </el-row>
            </div>
        </div>
        <div class="pro-category-placeholder"></div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName" >
            <el-tab-pane label="团队成员" name="teamMember">
                <div class="panel">
                    <div class="panel-header">团队成员</div>
                    <div class="panel-body">
                        <el-row :gutter="20" class="team-list" label-width="76px"
                                v-show="dutyFirstTeamStu.length > 0">
                            <el-col v-for="info in dutyFirstTeamStu" :key="info.id" :span="12">
                                <div class="user-pic">
                                    <img :src="info.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""
                                         align="left" hspace="5" vspace="5">
                                </div>
                                <div class="user-detail">
                                    <e-col-item label="姓名：" align="right">
                                        {{info.uName}}
                                        <el-tag v-show="teamDetails.leaderid == info.userId" type="info"
                                                size="mini">
                                            项目负责人
                                        </el-tag>
                                    </e-col-item>
                                    <e-col-item label="学号：" align="right">{{info.no}}
                                        <el-tag v-show="info.currState" type="info" size="mini">
                                            {{getCurStateLabel(info.currState)}}
                                        </el-tag>
                                    </e-col-item>
                                    <e-col-item label="学院/专业：" align="right">
                                        {{info.officeId}}/{{info.professional | collegeFilter(collegeEntries)}}
                                    </e-col-item>
                                    <%--<e-col-item label="当前在研：" align="right">--%>
                                    <%--{{info.curJoin}}--%>
                                    <%--</e-col-item>--%>
                                    <e-col-item label="联系方式：" align="right">{{info.mobile}}
                                    </e-col-item>
                                    <e-col-item label="技术领域：" align="right">{{info.domainlt}}
                                    </e-col-item>
                                </div>
                            </el-col>
                        </el-row>
                        <div v-show="!dutyFirstTeamStu || dutyFirstTeamStu.length == 0" class="empty-color text-center">
                            目前没有学生加入到团队中，快去邀请吧...
                        </div>
                    </div>


                </div>
                <div class="panel">
                    <div class="panel-header">指导老师</div>
                    <div class="panel-body">
                        <el-row :gutter="20" class="team-list-teacher team-list" label-width="76px"
                                v-show="teamTeacherInfo.length > 0">
                            <el-col v-for="tInfo in teamTeacherInfo" :key="tInfo.id" :span="12">
                                <div class="user-pic">
                                    <img :src="tInfo.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""
                                         align="left" hspace="5" vspace="5">
                                </div>
                                <div class="user-detail">
                                    <e-col-item label="姓名：" align="right">{{tInfo.uName}}
                                        <el-tag v-show="tInfo.teacherType" type="info" size="mini">
                                            {{getTeacherSourceLabel(tInfo.teacherType)}}
                                        </el-tag>
                                    </e-col-item>
                                    <e-col-item label="工号：" align="right">{{tInfo.no}}</e-col-item>
                                    <e-col-item label="学院/专业：" align="right">
                                        {{tInfo.officeId}}
                                    </e-col-item>
                                    <e-col-item label="当前指导：" align="right">{{tInfo.curJoin}}</e-col-item>
                                    <e-col-item label="技术领域：" align="right">{{tInfo.domainlt}}</e-col-item>
                                </div>
                            </el-col>
                        </el-row>
                        <div v-show="!teamTeacherInfo || teamTeacherInfo.length == 0" class="empty-color text-center">
                            暂无导师
                        </div>
                    </div>
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>
    <div class="mgb-20 text-center">
        <el-button type="primary" :disabled="passDisabled" size="mini" @click.stop.prevent="passTeam">通过</el-button>
        <el-button type="primary" :disabled="passDisabled" size="mini" @click.stop.prevent="unPassTeam">不通过</el-button>
        <el-button size="mini" :disabled="passDisabled" @click.stop.prevent="goToBack">返回</el-button>
    </div>
</div>

<script type="text/javascript">
    'use strict';
    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {

            var teamInfo = JSON.parse(JSON.stringify(${fns:toJson(teamInfo)}));

            var teamDetails = JSON.parse(JSON.stringify(${fns:toJson(teamDetails)}));

            var teamTeacherInfo = JSON.parse(JSON.stringify(${fns:toJson(teamTeacherInfo)}));

            var colleges = JSON.parse('${fns: getOfficeListJson()}') || [];
            var currentStates = JSON.parse('${fns: getDictListJson("current_sate")}') || [];
            var teacherTypes = JSON.parse('${fns: getDictListJson('master_type')}') || [];

            return {
                teamInfo: teamInfo || [],
                dutyFirstTeamStu: [],
                teamDetails: teamDetails,
                teamTeacherInfo: teamTeacherInfo,
                colleges: colleges,
                currentStates: currentStates,
                teacherTypes: teacherTypes,
                notifyType: '${notifyType}',
                notifyForm: '${from}',
                notifyId: '${notifyId}',
                notifyDisabled: false,
                tabActiveName: 'teamMember',
                passDisabled: false
            }
        },
        computed: {
            currentStateEntries: function () {
                var entries = {};
                this.currentStates.forEach(function (item) {
                    entries[item.value] = item.label;
                });
                return entries;
            },
            teacherTypeEntries: function () {
                var entries = {};
                this.teacherTypes.forEach(function (item) {
                    entries[item.value] = item.label;
                });
                return entries;
            },
            leaderId: function () {
                return this.teamInfo[0].sponsor;
            }
        },
        methods: {
            getCurStateLabel: function (state) {
                if (!state) return '';
                return this.currentStateEntries[state];
            },
            getTeacherSourceLabel: function (value) {
                if (!value) return '';
                return this.teacherTypeEntries[value];
            },
            getDutyFirst: function () {
                for (var i = 0; i < this.teamInfo.length; i++) {
                    if (this.teamDetails.leaderid == this.teamInfo[i].userId) {
                        this.dutyFirstTeamStu.push(this.teamInfo[i]);
                    }
                }
                for (var j = 0; j < this.teamInfo.length; j++) {
                    if (this.teamDetails.leaderid != this.teamInfo[j].userId) {
                        this.dutyFirstTeamStu.push(this.teamInfo[j]);
                    }
                }
            },

            postTeamXhr: function (state) {
                var self = this;
                this.passDisabled = true;
                this.$axios.get('/team/checkTeam?'+ Object.toURLSearchParams({
                    teamId: '${teamDetails.teamId}',
                    res: state
                })).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.$confirm(data.msg, '提示', {
                            confirmButtonText: '确定',
                            type: 'success'
                        }).then(function () {
                            window.parent.sideNavModule.changeStaticUnreadTag("/a/team/getTeamCountToAudit");
                            window.location.href = document.referrer;
                        });
                        return false;
                    }
                    self.$message({
                        type: 'error',
                        message: data.msg
                    })
                    self.passDisabled = false;
                }).catch(function () {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            passTeam: function () {
                this.postTeamXhr(1)
            },

            unPassTeam: function () {
                this.postTeamXhr(0)
            },


            goToBack: function () {
                window.history.go(-1);
            }
        },
        created: function () {
            this.getDutyFirst();
//            this.ajaxGetUserProjectById();
//            this.ajaxGetUserGContestById();
        }
    })
</script>


</body>
</html>