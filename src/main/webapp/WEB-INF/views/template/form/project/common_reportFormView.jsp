<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>${fns:getFrontTitle()}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>


</head>

<body>


<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a :href="ctxFront"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item><a :href="ctxFront + '/page-innovation'">双创项目</a></el-breadcrumb-item>
        <el-breadcrumb-item><a :href="ctxFront + '/project/projectDeclare/curProject'">我的项目</a></el-breadcrumb-item>
        <el-breadcrumb-item>项目申报详情</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="panel panel-project mgb-20">
        <div class="panel-header panel-header-title"><span>{{proModel.pName}}</span></div>
        <div class="panel-body">
            <div class="project-pic"><img :src="proModel.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter" alt="" align="left" hspace="5" vspace="5"></div>
            <div class="project-info_list">
                <e-col-item label="项目编号：" align="right" style="margin-bottom: 10px;">
                    <span>{{proModel.competitionNumber}}</span>
                </e-col-item>
                <e-col-item label="项目类别：" align="right" style="margin-bottom: 10px;">{{proModel.proCategory |
                    selectedFilter(proCategoryEntries)}}
                </e-col-item>
                <e-col-item label="所属学院：" align="right" style="margin-bottom: 10px;">{{proModel.deuser.officeName}}
                </e-col-item>
                <e-col-item label="填报日期：" align="right" style="margin-bottom: 10px;">{{proModel.subTime |
                    formatDateFilter('YYYY-MM-DD')}}
                </e-col-item>
            </div>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>

    <div class="pro-contest-content panel-padding-space mgb-20">
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="负责人信息" name="first">
                <e-panel label="负责人信息">
                    <leader-info :leader="leader" :professional-entries="professionalEntries"
                                 :office-name="proModel.officeName"></leader-info>
                </e-panel>
            </el-tab-pane>
            <el-tab-pane label="项目信息" name="second">
                <e-panel label="项目基本信息">
                    <el-row :gutter="20" label-width="110px">
                        <el-col :span="24">
                            <e-col-item label="项目名称：">{{proModel.pName}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="项目简称：">{{proModel.shortName || ''}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="项目类别：">{{proModel.proCategory | selectedFilter(proCategoryEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="项目简介："><span class="white-space-pre" v-html="proModel.introduction"></span></e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>

                <e-panel label="项目材料" v-if="applyFiles.length > 0">
                    <ul class="timeline">
                        <li class="work" v-for="file in applyFiles" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="/images/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                </e-panel>
                <e-panel label="${gnode.name}" v-if="reportFiles.length > 0">
                    <ul class="timeline">
                        <li class="work" v-for="file in reportFiles" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="/images/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                    <e-col-item label="已取得阶段性成果：" label-width="150px" style="margin-left:53px;"><span class="white-space-pre">${proReport.stageResult}</span></e-col-item>
                </e-panel>
                <e-panel :label="item.gnodeName || 'gnodeName'" v-if="reports.length > 0" v-for="item in reports" :key="item.id">
                    <ul class="timeline">
                        <li class="work" v-for="file in item.files" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="/images/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                    <el-row :gutter="20" label-width="150px" style="margin-left:53px;">
                        <el-col :span="20">
                            <e-col-item label="已取得阶段性成果："><span class="white-space-pre" v-html="item.stageResult"></span></e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>

            </el-tab-pane>
            <el-tab-pane label="团队成员" name="three">
                <e-panel label="团队成员">
                    <team-student-list :team-student="dutyFirstTeamStu" :leader="leader"></team-student-list>
                </e-panel>
                <e-panel label="指导老师">
                    <team-teacher-list :team-teacher="teamTeacher"></team-teacher-list>
                </e-panel>
            </el-tab-pane>

            <el-tab-pane v-if="auditRecordList.length > 0" label="审核记录" name="four">
                <e-panel label="审核记录">
                    <div class="table-container">
                        <table class="table table-bordered table-default table-hover text-center" style="margin-bottom: 0">
                            <thead>
                            <tr>
                                <th>审核动作</th>
                                <th>审核时间</th>
                                <th>审核人</th>
                                <th>审核结果</th>
                                <th style="width:50%;">建议及意见</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in auditRecordList">
                                <td v-if="item.id">{{item.auditName}}</td>
                                <td v-if="item.id">{{item.createDate | formatDateFilter('YYYY-MM-DD')}}</td>
                                <td v-if="item.id">{{item.user ? item.user.name : ''}}</td>
                                <td v-if="item.id">{{item.result}}</td>
                                <td v-if="item.id" class="word-break">{{item.suggest}}</td>
                                <td v-if="!item.id" colspan="5" class="score-row" style="text-align:right;">{{item.auditName}}：{{item.result}}</td>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                </e-panel>

            </el-tab-pane>




        </el-tabs>
    </div>

    <div class="text-center">
        <el-button size="mini" @click.stop="goBack">返回</el-button>
    </div>



</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('project_type'))}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var applyFiles = JSON.parse(JSON.stringify(${fns:toJson(applyFiles)})) || [];
            var reports = JSON.parse(JSON.stringify(${fns:toJson(reports)})) || [];
            var leader = proModel.deuser;
            var teamStudent = JSON.parse(JSON.stringify(${fns: toJson(teamStu)})) || [];
            var teamTeacher = JSON.parse(JSON.stringify(${fns: toJson(teamTea)})) || [];
            var auditRecordList = JSON.parse(JSON.stringify(${fns: toJson(actYwAuditInfos)})) || [];
            var proReport = JSON.parse(JSON.stringify(${fns: toJson(proReport)})) || {};
            var reportFiles = proReport.files || [];
            return {
                proModel:proModel,
                proCategories:proCategories,
                tabActiveName: 'first',
                leader:leader,
                colleges: professionals,
                applyFiles:applyFiles,
                reports:reports,
                teamStudent: teamStudent,
                dutyFirstTeamStu:[],
                teamTeacher: teamTeacher,
                auditRecordList: auditRecordList,
                reportFiles: reportFiles,
            }
        },
        computed: {
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
            },
            professionalEntries: {
                get: function () {
                    return this.getEntries(this.colleges, {label: 'name', value: 'id'})
                }
            }

        },
        methods:{
            getDutyFirst:function () {
                for(var i = 0; i < this.teamStudent.length; i++){
                    if(this.leader.id == this.teamStudent[i].userId){
                        this.dutyFirstTeamStu.push(this.teamStudent[i]);
                    }
                }
                for(var j = 0; j < this.teamStudent.length; j++){
                    if(this.leader.id != this.teamStudent[j].userId){
                        this.dutyFirstTeamStu.push(this.teamStudent[j]);
                    }
                }
            },
            goBack: function () {
                location.href = this.ctxFront + '/project/projectDeclare/curProject'
            }

        },
        created:function () {
            this.getDutyFirst();
        }
    })

</script>

</body>

</html>
