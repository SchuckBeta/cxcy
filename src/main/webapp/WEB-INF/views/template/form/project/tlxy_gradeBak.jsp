<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="审核"></edit-bar>
    </div>
    <div class="panel panel-project mgb-20">
        <div class="panel-header panel-header-title"><span>{{proModel.pName}}</span></div>
        <div class="panel-body">
            <div class="project-pic"><img :src="proModel.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter"
                                          alt="" align="left" hspace="5" vspace="5"></div>
            <div class="project-info_list">
                <e-col-item label="项目编号：" align="right" style="margin-bottom: 10px;">
                    <span v-if="proModelTlxy.proModel.finalStatus == '0000000264'">{{proModel.competitionNumber}}</span>
                    <span v-else-if="proModelTlxy.proModel.finalStatus == '0000000265'">{{proModelTlxy.pCompetitionNumber}}</span>
                    <span v-else-if="proModelTlxy.proModel.finalStatus == '0000000266'">{{proModelTlxy.gCompetitionNumber}}</span>
                </e-col-item>
                <e-col-item label="项目类别：" align="right" style="margin-bottom: 10px;">{{proModel.proCategory |
                    selectedFilter(proCategoryEntries)}}
                </e-col-item>
                <e-col-item label="所属学院：" align="right" style="margin-bottom: 10px;">{{proModel.officeName}}
                </e-col-item>
                <e-col-item label="填报日期：" align="right" style="margin-bottom: 10px;">{{proModel.subTime |
                    formatDateFilter('YYYY-MM-DD')}}
                </e-col-item>
            </div>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space">
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
                            <e-col-item label="项目名称：" class="word-break">{{proModel.pName}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="项目来源：">{{proModelTlxy.source | selectedFilter(sourceEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="项目类别：">{{proModel.proCategory | selectedFilter(proCategoryEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="项目级别：">
                                {{proModelTlxy.proModel.finalStatus | selectedFilter(projectLevelEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="项目拓展及传承：">1、项目能与其他大型比赛、活动对接 2、可在低年级同学中传承 3、结项后能继续开展</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="项目简介：" class="white-space-pre-static word-break">{{proModel.introduction}}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="前期调研准备：" class="white-space-pre-static word-break">{{proModelTlxy.innovation}}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="项目预案：">
                                <table class="table table-bordered table-default table-hover text-center">
                                    <thead>
                                    <tr>
                                        <th width="40%">实施预案</th>
                                        <th width="20%">时间安排</th>
                                        <th width="40%">保障措施</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td class="word-break">{{proModelTlxy.planContent}}</td>
                                        <td>
                                            {{proModelTlxy.planStartDate |
                                            formatDateFilter('YYYY-MM-DD')}}至{{proModelTlxy.planEndDate |
                                            formatDateFilter('YYYY-MM-DD')}}
                                        </td>
                                        <td class="word-break">{{proModelTlxy.planStep}}</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="任务分工：">
                                <table class="table table-bordered table-default table-hover text-center">
                                    <thead>
                                    <tr>
                                        <th width="32">序号</th>
                                        <th>工作任务</th>
                                        <th style="min-width: 78px">任务描述</th>
                                        <th width="164">时间安排</th>
                                        <th width="78">成本（元）</th>
                                        <th style="min-width: 78px">质量评价</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr v-if="proModelTlxy.planList" v-for="(item,index) in proModelTlxy.planList"
                                        :key="item.id">
                                        <td>{{index + 1}}</td>
                                        <td class="word-break">{{item.content}}</td>
                                        <td class="word-break">{{item.description}}</td>
                                        <td>
                                            {{item.startDate | formatDateFilter('YYYY-MM-DD')}}至{{item.endDate |
                                            formatDateFilter('YYYY-MM-DD')}}
                                        </td>
                                        <td>{{item.cost}}</td>
                                        <td class="word-break">{{item.quality}}</td>
                                    </tr>
                                    <tr v-if="!proModelTlxy.planList">
                                        <td colspan="6" class="empty-color">没有任务分工</td>
                                    </tr>
                                    </tbody>
                                </table>
                            </e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>
                <e-panel label="预期成果">
                    <el-row :gutter="20" label-width="110px">
                        <el-col :span="24">
                            <e-col-item label="成果形式：">{{proModelTlxy.resultType |
                                checkboxFilter(projectResultEntries)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="成果说明：" class="white-space-pre-static word-break">{{proModelTlxy.resultContent}}</e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>

                <e-panel label="经费预算">
                    <el-row :gutter="20" label-width="110px">
                        <el-col :span="24">
                            <e-col-item label="项目经费预算：">{{proModelTlxy.budgetDollar}}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="经费预算明细：" class="white-space-pre-static word-break">{{proModelTlxy.budget}}</e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>
                <e-panel label="项目材料" v-if="projectTlxyApplyFiles.length > 0">
                    <ul class="timeline">
                        <li class="work" v-for="file in projectTlxyApplyFiles" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="${ctxImages}/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                </e-panel>
                <e-panel :label="item.gnodeName" v-if="projectTlxyReports.length > 0" v-for="item in projectTlxyReports"
                         :key="item.id">
                    <ul class="timeline">
                        <li class="work" v-for="file in item.files" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="${ctxImages}/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                    <el-row :gutter="20" label-width="150px" style="margin-left:53px;">
                        <el-col :span="20">
                            <e-col-item label="已取得阶段性成果：" class="white-space-pre-static word-break">{{item.stageResult || ''}}</e-col-item>
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
            <el-tab-pane :label="taskName" name="four">
                <e-panel :label="taskName">
                    <audit-form :options="actYwStatusList" :task-name="taskName"
                                :action="frontOrAdmin + '/promodel/proModel/promodelGateAudit'"
                                @submit-complete="submitComplete">
                        <input type="hidden" name="id" :value="proModel.id">
                        <input type="hidden" name="gnodeId" :value="gnodeId"/>
                        <input type="hidden" name="actionPath" :value="actionPath"/>
                        <%--<input type="hidden" name="act.taskId" :value="proModel.act.taskId">--%>
                        <%--<input type="hidden" name="act.taskName" :value="proModel.act.taskName">--%>
                        <%--<input type="hidden" name="act.taskDefKey" :value="proModel.act.taskDefKey">--%>
                        <%--<input type="hidden" name="act.procInsId" :value="proModel.act.procInsId">--%>
                        <%--<input type="hidden" name="act.procDefId" :value="proModel.act.procDefId">--%>
                    </audit-form>
                </e-panel>
            </el-tab-pane>
        </el-tabs>
    </div>
</div>


</body>


<script>


    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var gnodeId = '${gnodeId}';
            var actionPath = '${actionPath}';
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('project_type'))}');
            var sources = JSON.parse('${fns: toJson(fns:getDictList("project_source"))}');
            var projectResultType = JSON.parse('${fns: toJson(fns:getDictList("project_result_type"))}');
            var projectTlxyApplyFiles = JSON.parse('${fns:toJson(applyFiles)}') || [];
            var projectTlxyReports = ${fns:toJson(reports)} || [];
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var proModel = ${fns: toJson(proModel)} || {};
            var teamStudent = JSON.parse('${fns: toJson(teamStu)}');
            var teamTeacher = JSON.parse('${fns: toJson(teamTea)}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var actYwStatusList = ${fns: toJson(actYwStatusList)} || [];
            var leader;
            var proModelTlxy = ${fns: toJson(proModelTlxy)} || {};


            leader = proModel.deuser;
            return {
                gnodeId: gnodeId,
                actionPath: actionPath,
                sources: sources,
                proCategories: proCategories,
                projectDegrees: projectDegrees,
                projectResultType: projectResultType,
                projectTlxyApplyFiles: projectTlxyApplyFiles,
                projectTlxyReports: projectTlxyReports,
                proModel: proModel,
                proModelTlxy: proModelTlxy,
                colleges: professionals,
                teamStudent: teamStudent,
                dutyFirstTeamStu: [],
                teamTeacher: teamTeacher,
                leader: leader,
                actYwStatusList: actYwStatusList,
                tabActiveName: 'four',
                isFirst:'${isFirst}',
                taskName: '${taskName}'
            }
        },
        computed: {
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
            },
            sourceEntries: {
                get: function () {
                    return this.getEntries(this.sources)
                }
            },
            professionalEntries: {
                get: function () {
                    return this.getEntries(this.colleges, {label: 'name', value: 'id'})
                }
            },
            projectResultEntries: {
                get: function () {
                    return this.getEntries(this.projectResultType)
                }
            },
            projectLevelEntries: {
                get: function () {
                    return this.getEntries(this.projectDegrees)
                }
            }
        },
        methods: {
            submitComplete: function () {
                var self = this;
//                window.parent.sideNavModule.changeUnreadTag(this.proModel.actYwId);
            },
            getDutyFirst: function () {
                for (var i = 0; i < this.teamStudent.length; i++) {
                    if (this.leader.id == this.teamStudent[i].userId) {
                        this.dutyFirstTeamStu.push(this.teamStudent[i]);
                    }
                }
                for (var j = 0; j < this.teamStudent.length; j++) {
                    if (this.leader.id != this.teamStudent[j].userId) {
                        this.dutyFirstTeamStu.push(this.teamStudent[j]);
                    }
                }
            }
        },
        created: function () {
            this.getDutyFirst();
            var pushBack = {
                state:'打回',
                status:'999'
            };
            if(this.isFirst == '1'){
                this.actYwStatusList.push(pushBack);
            }
        }
    })

</script>

</html>
