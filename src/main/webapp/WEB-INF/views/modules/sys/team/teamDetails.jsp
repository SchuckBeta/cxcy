<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>

</head>
<body>
<div id="myTeam" v-show="pageLoad" style="display: none" class="page-container">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item>人才库</el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/team/indexMyTeamList">团队建设</a></el-breadcrumb-item>
        <el-breadcrumb-item>团队信息</el-breadcrumb-item>
    </el-breadcrumb>


    <c:if test="${not empty notifyError}">
        <div class="empty" style="padding: 60px 0">${notifyError}</div>
    </c:if>
    <c:if test="${empty notifyError}">
        <input type="hidden" id="teamId" value="${teamDetails.id }">

        <h4 class="team-details-title">
            <c:if test="${notSponsor&&teamDetails.state==3}"><span>信息审核中</span></c:if>
            <c:if test="${notSponsor&&teamDetails.state==4}"><span class="danger-color">信息审核未通过</span></c:if>
            <c:if test="${not notSponsor||(teamDetails.state!=3&&teamDetails.state!=4)}"><span>${teamDetails.name }</span></c:if>
        </h4>
        <div class="panel">
            <div class="panel-header">团队信息</div>
            <div class="panel-body">
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
                        <e-col-item label="团队介绍：" align="right" ><span class="white-space-pre"><c:if
                                test="${not notSponsor||(teamDetails.state!=3&&teamDetails.state!=4)}">${teamDetails.summary }</c:if><c:if
                                test="${notSponsor&&teamDetails.state==3}">信息审核中</c:if><c:if
                                test="${notSponsor&&teamDetails.state==4}">信息审核未通过</c:if></span></e-col-item>
                    </el-col>
                    <el-col :span="24">
                        <e-col-item label="组员要求：" align="right"><span class="white-space-pre"><c:if
                                test="${not notSponsor||(teamDetails.state!=3&&teamDetails.state!=4)}">${teamDetails.membership }</c:if><c:if
                                test="${notSponsor&&teamDetails.state==3}">信息审核中</c:if><c:if
                                test="${notSponsor&&teamDetails.state==4}">信息审核未通过</c:if></span></e-col-item>
                    </el-col>
                </el-row>
                <div class="text-center">
                    <template v-if="notifyForm=='notify' && notifyType=='6'">
                        <el-button type="primary" :disabled="notifyDisabled" size="mini"
                                   @click.stop.prevent="acceptInviation">接受
                        </el-button>
                        <el-button size="mini" :disabled="notifyDisabled" @click.stop.prevent="refuseInviation">拒绝
                        </el-button>
                    </template>
                    <template v-else-if="notifyForm=='notify' && notifyType=='7'">
                        <el-button type="primary" :disabled="notifyDisabled" size="mini"
                                   @click.stop.prevent="applyJoin">申请加入
                        </el-button>
                    </template>
                </div>
            </div>
        </div>
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
                                    <%--{{info.no | hideNo(hasJoin)}}--%>
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
                                    <%--{{info.mobile | hideMobile(hasJoin)}}--%>
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
        <div class="panel mgb-20">
            <div class="panel-header">指导老师</div>
            <div class="panel-body">
                <el-row :gutter="20" class="team-list-teacher team-list team-teacher-list" label-width="76px"
                        v-show="teamTeacherInfo.length > 0">
                    <el-col v-for="tInfo in teamTeacherInfo" :key="tInfo.id" :span="12">
                        <div class="first-teacher-label" v-if="firstTeacherId && firstTeacherId == tInfo.userId"><span>第一导师</span></div>
                        <div class="user-pic" style="border-radius: 0">
                            <img :src="tInfo.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt=""
                                 align="left" hspace="5" vspace="5">
                        </div>
                        <div class="user-detail">
                            <e-col-item label="姓名：" align="right">{{tInfo.uName}}
                                <el-tag v-show="tInfo.teacherType" type="info" size="mini">
                                    {{getTeacherSourceLabel(tInfo.teacherType)}}
                                </el-tag>
                            </e-col-item>
                            <e-col-item label="工号：" align="right">{{tInfo.no}}
                                <%--<el-tag v-if="team.firstTeacher && team.firstTeacher == tInfo.userId" type="info" size="mini">--%>
                                    <%--第一导师--%>
                                <%--</el-tag>--%>
                                    <%--{{tInfo.no | hideNo(hasJoin)}}--%>
                            </e-col-item>
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
        <div class="mgb-20 text-center">
            <el-button size="mini" @click.stop.prevent="goToBack">返回</el-button>
        </div>
    </c:if>
</div>

<script>
    +function (Vue) {
        var myTeam = new Vue({
            el: '#myTeam',
            mixins: [Vue.collegesMixin],
            data: function () {

                var teamInfo = JSON.parse(JSON.stringify(${fns:toJson(teamInfo)})) || {};

                var teamDetails = JSON.parse(JSON.stringify(${fns:toJson(teamDetails)})) || {};
                var team = JSON.parse(JSON.stringify(${fns:toJson(team)})) || {};

                var teamTeacherInfo = JSON.parse(JSON.stringify(${fns:toJson(teamTeacherInfo)})) || {};

                var colleges = JSON.parse('${fns: getOfficeListJson()}') || [];
                var currentStates = JSON.parse('${fns: getDictListJson("current_sate")}') || [];
                var teacherTypes = JSON.parse('${fns: getDictListJson('master_type')}') || [];
                for(var i = 0; i < teamTeacherInfo.length; i ++){
                    if(teamTeacherInfo[i].userId == team.firstTeacher){
                        teamTeacherInfo.unshift(teamTeacherInfo[i]);
                        teamTeacherInfo.splice(i + 1,1);
                        break;
                    }
                }
                return {
                    team:team,
                    teamInfo: teamInfo,
                    dutyFirstTeamStu: [],
                    teamDetails: teamDetails,
                    teamTeacherInfo: teamTeacherInfo,
                    colleges: colleges,
                    currentStates: currentStates,
                    teacherTypes: teacherTypes,
                    notifyType: '${notifyType}',
                    notifyForm: '${from}',
                    notifyId: '${notifyId}',
                    hasJoin: ${hasJoin},
                    notifyDisabled: false,
                    firstTeacherId:team.firstTeacher
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
                acceptInviation: function () {
                    var self = this;
                    this.notifyDisabled = true
                    this.$axios({
                        method: "post",
                        url: "/team/acceptInviationByNotify?send_id=" + this.notifyId
                    }).then(function (response) {
                        var data = response.data;
                        if (data.ret !== '1') {
                            self.notifyDisabled = false
                        }
                        self.$message({
                            type: data.ret == '1' ? 'success' : 'error',
                            message: data.msg
                        })
                    }).catch(function (error) {
                        self.$message({
                            type: 'error',
                            message: self.xhrErrorMsg
                        })
                    });
                },
                refuseInviation: function () {
                    var self = this;
                    this.notifyDisabled = true
                    this.$axios({
                        method: "post",
                        url: "/team/refuseInviationByNotify?send_id=" + this.notifyId
                    }).then(function (response) {
                        var data = response.data;
                        if (data.ret !== '1') {
                            self.notifyDisabled = false
                        }
                        self.$message({
                            type: data.ret == '1' ? 'success' : 'error',
                            message: data.msg
                        })
                    }).catch(function (error) {
                        self.$message({
                            type: 'error',
                            message: self.xhrErrorMsg
                        })
                    });
                },
                applyJoin: function () {
                    var self = this;
                    this.notifyDisabled = true
                    this.$axios({
                        method: "post",
                        url: "/team/applyJoin?teamId=" + this.teamDetails.id
                    }).then(function (response) {
                        var data = response.data;
                        if (data.indexOf('成功') == -1) {
                            self.notifyDisabled = false
                        }
                        self.$message({
                            type: data.indexOf('成功') > -1 ? 'success' : 'error',
                            message: data
                        })
                    }).catch(function (error) {
                        self.$message({
                            type: 'error',
                            message: self.xhrErrorMsg
                        })
                    });
                },
                goToBack: function () {
                    window.location.href = this.frontOrAdmin + '/team/indexMyTeamList';
                }
            },
            created: function () {
                this.getDutyFirst();
            }
        })
    }(Vue);
</script>


</body>
</html>