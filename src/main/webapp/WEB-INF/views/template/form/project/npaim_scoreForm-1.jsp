<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>

<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <edit-bar-province second-name="查看"></edit-bar-province>
    <div class="page-content-container">
        <div class="todo-audit-project-list">
            <div class="todo-au-title">
                <span>所有待评分项目</span>
            </div>
            <div class="todo-audit-project-wrapper">
                <e-scroller :update-data="updateData" top :rendered="rendered">
                    <todo-project-item
                            :to="ctx + '/promodel/proModel/provAuditForm?gnodeId=' + item.proModel.auditMap.gnodeId + '&proModelId=' + item.proModel.auditMap.proModelId + '&pathUrl=${actionUrl}'"
                            :item="item.proModel"
                            :class="{'router-link-active': proModel.id === item.proModel.id}"
                            v-for="item in pageList"
                            :key="item.id"
                    ></todo-project-item>
                </e-scroller>
            </div>
        </div>
        <div class="project-info-view-page">
            <div class="project-view-container">
                <pro-view-header :project="provinceProModel" :pro-model="provinceProModel.proModel">
                    <%--<grade-dialog--%>
                    <%--ref="gradeDialog"--%>
                    <%--:disabled="disabled"--%>
                    <%--@submit-form="submitGrade"--%>
                    <%--></grade-dialog>--%>
                    <audit-dialog
                            ref="auditDialog"
                            :disabled="disabled"
                            :options="[{state:'打回',status:'999'}]"
                            @submit-form="submitGrade"
                    ></audit-dialog>
                </pro-view-header>
                <div class="pro-vm-body">
                    <div class="pro-vm-border"></div>
                    <div class="pro-vmb-wrap">
                        <el-tabs v-model="tabActiveName" size="mini">
                            <el-tab-pane label="项目信息" name="first">
                                <div class="pro-vmb-tab-wrap">
                                    <div class="pro-tab-cate-title"><span>详细介绍</span></div>
                                    <e-col-con label-width="110px">
                                        <e-col-item label="项目名称：" class="word-break">{{proModel.pName}}</e-col-item>
                                        <e-col-item label="项目来源：">{{proModel.source |
                                            selectedFilter(sourceEntries)}}
                                        </e-col-item>
                                        <e-col-item label="项目类别：">{{proModel.proCategory |
                                            selectedFilter(proCategoryEntries)}}
                                        </e-col-item>
                                        <e-col-item label="项目拓展及传承：">1、项目能与其他大型比赛、活动对接 2、可在低年级同学中传承 3、结项后能继续开展
                                        </e-col-item>
                                        <e-col-item label="项目简介："><span class="white-space-pre"
                                                                        v-html="proModel.introduction"></span>
                                        </e-col-item>
                                        <e-col-item v-if="proModel.innovation" label="前期调研准备："><span
                                                class="white-space-pre" v-html="proModel.innovation"></span>
                                        </e-col-item>
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
                                                    <td class="word-break">{{proModel.planContent}}</td>
                                                    <td>
                                                        {{proModel.planStartDate |
                                                        formatDateFilter('YYYY-MM-DD')}}至{{proModel.planEndDate
                                                        | formatDateFilter('YYYY-MM-DD')}}
                                                    </td>
                                                    <td class="word-break">{{proModel.planStep}}</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </e-col-item>
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
                                                <tr v-if="proModel.planList"
                                                    v-for="(item,index) in proModel.planList" :key="item.id">
                                                    <td>{{index + 1}}</td>
                                                    <td class="word-break">{{item.content}}</td>
                                                    <td class="word-break">{{item.description}}</td>
                                                    <td>
                                                        {{item.startDate |
                                                        formatDateFilter('YYYY-MM-DD')}}至{{item.endDate |
                                                        formatDateFilter('YYYY-MM-DD')}}
                                                    </td>
                                                    <td>{{item.cost}}</td>
                                                    <td class="word-break">{{item.quality}}</td>
                                                </tr>
                                                <tr v-if="!proModel.planList">
                                                    <td colspan="6" class="empty-color">没有任务分工</td>
                                                </tr>
                                                </tbody>
                                            </table>
                                        </e-col-item>
                                    </e-col-con>
                                    <div class="pro-tab-cate-title"><span>预期成果</span></div>
                                    <e-col-con label-width="110px">
                                        <e-col-item label="成果形式：">{{proModel.resultType |
                                            checkboxFilter(projectResultEntries)}}
                                        </e-col-item>
                                        <e-col-item label="成果说明："><span class="white-space-pre"
                                                                        v-html="proModel.resultContent"></span>
                                        </e-col-item>
                                    </e-col-con>
                                    <div class="pro-tab-cate-title"><span>预期成果</span></div>
                                    <e-col-con label-width="110px">
                                        <e-col-item label="经费预算明细："><span class="white-space-pre"
                                                                          v-html="proModel.budget"></span>
                                        </e-col-item>
                                    </e-col-con>
                                    <div class="pro-tab-cate-title"><span>项目材料</span></div>
                                    <e-col-con label-width="110px">
                                        <ul class="timeline">
                                            <li class="work" v-for="file in projectHsxmApplyFiles" :key="file.id">
                                                <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                                                <img src="${ctxImages}/time-line.png" alt="">
                                                <div class="relative">
                                                    <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                                                </div>
                                            </li>
                                        </ul>
                                    </e-col-con>
                                    <template v-if="projectHsxmReports.length > 0" >
                                        <div v-for="item in projectHsxmReports" :key="item.id">
                                            <div class="pro-tab-cate-title"><span>{{item.gnodeName}}</span></div>
                                            <ul class="timeline">
                                                <li class="work" v-for="file in item.files" :key="file.id">
                                                    <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                                                    <img src="${ctxImages}/time-line.png" alt="">
                                                    <div class="relative">
                                                        <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                                                    </div>
                                                </li>
                                            </ul>
                                            <e-col-con label-width="110px">
                                                <e-col-item label="已取得阶段性成果："><span class="white-space-pre"
                                                                                    v-html="item.stageResult"></span>
                                                </e-col-item>
                                            </e-col-con>
                                        </div>
                                    </template>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane label="团队信息" name="second">
                                <div class="pro-vmb-tab-wrap">
                                    <div class="pro-tab-cate-title"><span>团队成员</span></div>
                                    <div>
                                        <team-student-list :team-student="dutyFirstTeamStu"
                                                           :leader="leader"></team-student-list>
                                    </div>
                                    <div class="pro-tab-cate-title"><span>指导老师</span></div>
                                    <div>
                                        <team-teacher-list :team-teacher="teamTeacher"></team-teacher-list>
                                    </div>
                                </div>
                            </el-tab-pane>
                            <el-tab-pane v-if="auditRecordList.length > 0" label="审核记录" name="four">
                                <div class="pro-vmb-tab-wrap">
                                    <div class="pro-tab-cate-title"><span>审核记录</span></div>
                                    <div class="table-container">
                                        <table class="el-table table table-center" style="margin-bottom: 0">
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
                                                <template v-if="item.id">
                                                    <td>{{item.auditName}}</td>
                                                    <td>{{item.updateDate | formatDateFilter('YYYY-MM-DD')}}</td>
                                                    <td>{{item.user.name}}</td>
                                                    <td>{{item.result}}</td>
                                                    <td class="word-break">{{item.suggest}}</td>
                                                </template>
                                                <template v-else>
                                                    <td colspan="5" class="score-row" style="text-align:right;">
                                                        {{item.auditName}}：{{item.result}}
                                                    </td>
                                                </template>
                                            </tr>
                                            </tbody>
                                        </table>

                                    </div>
                                </div>
                            </el-tab-pane>
                        </el-tabs>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <pro-view-gc :view-id="proModel.id"></pro-view-gc>
</div>
<script>

    'use strict';
    new Vue({
        el: '#app',
        data: function () {
            var gnodeId = '${gnodeId}';
            var actionPath = '${actionPath}';
            var proCategories = JSON.parse('${fns: toJson(fns:getDictList("project_type"))}');
            var sources = JSON.parse('${fns: toJson(fns:getDictList("project_source"))}');
            var projectResultType = JSON.parse('${fns: toJson(fns:getDictList("project_result_type"))}');
            var projectHsxmApplyFiles = JSON.parse('${fns:toJson(applyFiles)}') || [];
            var projectHsxmReports = JSON.parse(JSON.stringify(${fns:toJson(reports)})) || [];
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)})) || {};
            var teamStudent = JSON.parse('${fns: toJson(teamStu)}') || [];
            var teamTeacher = JSON.parse('${fns: toJson(teamTea)}') || [];
            var auditRecordList = JSON.parse(JSON.stringify(${fns: toJson(actYwAuditInfos)})) || [];
            var leader;
            var provinceProModel = JSON.parse(JSON.stringify(${fns: toJson(provinceProModel)})) || {};
            leader = proModel.deuser;
            return {
                gnodeId: gnodeId,
                actywId: provinceProModel.actYwId,
                actionPath: actionPath,
                sources: sources,
                proCategories: proCategories,
                projectResultType: projectResultType,
                projectHsxmApplyFiles: projectHsxmApplyFiles,
                projectHsxmReports: projectHsxmReports,
                proModel: proModel,
                projectDegrees: projectDegrees,
                provinceProModel: provinceProModel,
                teamStudent: teamStudent,
                dutyFirstTeamStu: [],
                teamTeacher: teamTeacher,
                leader: leader,
                auditRecordList: auditRecordList,
                tabActiveName: 'first',
                rendered: true,
                disabled: false,
                pageNo: 0,
                pageSize: 15,
                pageCount: 0,
                pageList: []
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
            },
            updateData: function () {
                var self = this;
                self.rendered = false;
                if (!this.rendered) {
                    this.pageNo+=1;
                    var params = {
                        pageNo: this.pageNo,
                        pageSize: this.pageSize,
                        actywId: this.actywId,
                        gnodeId: '${gnodeId}',
                    };
                    this.$axios.get('/cms/ajax/listForm',{params: params}).then(function (response) {
                        var data = response.data;
                        if(data.page){
                            var page = data.page || {};
                            self.pageCount = page.count;
                            self.pageNo = page.pageNo;
                            self.pageSize = page.pageSize;
                            self.pageList = page.list || [];
                        }
                        self.loading = false;
                        self.rendered = true;
                    });
                }
            },

            getActParams: function () {
                var act = this.proModel.act;
                var entry = {};
                for (var k in act) {
                    if (act.hasOwnProperty(k)) {
                        entry['act.' + k] = act[k];
                    }
                }
                return entry
            },

            submitGrade: function (form) {
                var self = this;
                var oParams = {
                    gnodeId: this.gnodeId,
                    actionPath: this.actionPath,
                }
                var params = Object.assign({}, this.getActParams(),oParams, form);
                this.$axios.post('/promodel/proModel/saveProModelGateAudit', Object.toURLSearchParams(params)).then(function (response) {
                    console.log(response)
                })
            },
            getProvViewForm: function () {
                var self = this;
                this.$axios.post('/promodel/proModel/ajaxProvViewForm', Object.toURLSearchParams({id: this.provinceProModel.proModel.id})).then(function (response) {
                    console.log(response)
                })
            }

        },
        created: function () {
            this.getDutyFirst();
            this.updateData();
            this.getProvViewForm();
        }
    })


</script>
</body>
</html>