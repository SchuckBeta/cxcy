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
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName" @tab-click="handleClickTab">
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
            <el-tab-pane label="项目经历" name="projectExp">
                <div class="user_detail-inner user_detail-inner-experience">
                    <el-row :gutter="10">
                        <el-col :span="12" v-for="project in projectList" :key="project.id">
                            <div class="experience-card">
                                <div class="experience-card-header">
                                    <h4 class="experience-card-title">{{project.name}}</h4>
                                </div>
                                <div class="experience-card-body">
                                    <div class="exp-pic">
                                        <a href="javascript: void(0);"><img :src="project.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"></a>
                                    </div>
                                    <div class="exp-info">
                                        <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(project) }}</e-col-item>
                                        <e-col-item label="项目级别：" label-width="72px">{{project.level}}</e-col-item>
                                        <e-col-item label="项目结果：" label-width="72px">{{project.result}}</e-col-item>
                                        <e-col-item label="项目周期：" label-width="72px">{{getProjectRange(project)}}
                                        </e-col-item>
                                    </div>
                                    <div class="exp-intro">
                                        <e-col-item label="项目简介：" label-width="72px">
                                            {{project.introduction}}
                                        </e-col-item>
                                    </div>
                                </div>
                                <div class="experience-card-footer">
                                    <div class="text-right">
                                        <el-tag v-show="project.proName" type="info" size="mini">{{project.proName}}
                                        </el-tag>
                                        <el-tag v-show="project.year" type="info" size="mini">{{project.year}}</el-tag>
                                    </div>
                                </div>
                            </div>
                        </el-col>
                        <div v-show="!projectList.length" class="text-center">
                            <div class="user_experience-title none empty-color">
                                <span>暂无项目经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>
            </el-tab-pane>
            <el-tab-pane label="大赛经历" name="gcontestExp">
                <div class="user_detail-inner user_detail-inner-experience">
                    <el-row :gutter="10">
                        <el-col :span="12" v-for="contest in contestList" :key="contest.id">
                            <div class="experience-card">
                                <div class="experience-card-header">
                                    <h4 class="experience-card-title">{{contest.pName}}</h4>
                                </div>
                                <div class="experience-card-body">
                                    <div class="exp-pic">
                                        <a href="javascript: void(0);"><img :src="contest.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"></a>
                                    </div>
                                    <div class="exp-info">
                                        <e-col-item label="担任角色：" label-width="72px">{{ getRoleName(contest) }}</e-col-item>
                                        <e-col-item label="大赛级别：" label-width="72px">{{contest.level}}</e-col-item>
                                        <e-col-item label="大赛获奖：" label-width="72px">{{contest.award}}</e-col-item>
                                        <e-col-item label="大赛周期：" label-width="72px">{{getProjectRange(contest)}}
                                        </e-col-item>
                                    </div>
                                    <div class="exp-intro">
                                        <e-col-item label="大赛简介：" label-width="72px">
                                            {{contest.introduction}}
                                        </e-col-item>
                                    </div>
                                </div>
                                <div class="experience-card-footer">
                                    <div class="text-right">
                                        <el-tag v-show="contest.type" type="info" size="mini">{{contest.type}}</el-tag>
                                        <el-tag v-show="contest.year" type="info" size="mini">{{contest.year}}</el-tag>
                                    </div>
                                </div>
                            </div>
                        </el-col>
                        <div v-show="contestList.length < 1" class="text-center">
                            <div class="user_experience-title none empty-color">
                                <span>暂无大赛经历</span>
                            </div>
                        </div>
                    </el-row>
                </div>
            </el-tab-pane>
        </el-tabs>
    </div>
    <div class="mgb-20 text-center">
        <el-button size="mini" @click.stop.prevent="goToBack">返回</el-button>
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
                projectList: [],
                contestList: []
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

            handleClickTab: function () {
                if(this.tabActiveName == 'projectExp' && this.projectList.length < 1){
                    this.getProjectListByTeamId();
                }else if(this.tabActiveName == 'gcontestExp' && this.contestList.length < 1){
                    this.getGgcontestListByTeamId();
                }
            },
            getRoleName: function (project) {
                if (this.leaderId === project.leaderId) {
                    return '项目负责人'
                } else {
                    if (project.userType === '1') {
                        return '组成员'
                    }
                }
                return '导师'
            },
            getProjectRange: function (project) {
                var startDate, endDate;
                if (project.startDate) {
                    startDate = moment(project.startDate).format('YYYY-MM-DD');
                }
                if (project.endDate) {
                    endDate = moment(project.endDate).format('YYYY-MM-DD');
                }
                if (startDate) {
                    return startDate + '至' + endDate;
                }
                return ''
            },

            getProjectListByTeamId: function () {
                var self = this;
                this.$axios.post('/team/getProjectListByTeamId', {id: this.teamDetails.id}).then(function (response) {
                    var data = response.data;
                    self.projectList = data.data || [];
                })
            },
            getGgcontestListByTeamId: function () {
                var self = this;
                this.$axios.post('/team/getGgcontestListByTeamId', {id: this.teamDetails.id}).then(function (response) {
                    var data = response.data;
                    self.contestList = data.data || [];
                })
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