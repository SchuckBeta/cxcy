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
                <e-col-item label="大赛编号：" align="right" style="margin-bottom: 10px;">
                    <span>{{proModel.competitionNumber}}</span>
                </e-col-item>
                <e-col-item label="大赛类型：" align="right" style="margin-bottom: 10px;">{{proModel.proCategory |
                    selectedFilter(proCategoryEntries, true)}}
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
    <div class="pro-contest-content panel-padding-space  mgb-20">
        <div class="btn-audit-box">
            <el-button type="primary" size="mini" :disabled="disabled" @click.stop="openAuditForm">审核</el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="负责人信息" name="first">
                <e-panel label="负责人信息">
                    <leader-info :leader="leader" :professional-entries="professionalEntries"
                                 :office-name="proModel.officeName"></leader-info>
                </e-panel>
            </el-tab-pane>
            <el-tab-pane label="大赛信息" name="second">
                <e-panel label="大赛基本信息">
                    <el-row :gutter="20" label-width="110px">
                        <el-col :span="24">
                            <e-col-item label="大赛名称：">{{proModel.pName}}</e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="大赛简称：">{{proModel.shortName || ''}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="大赛类别：">{{proModel.proCategory | selectedFilter(proCategoryEntries,
                                true)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="参赛组别：">{{proModel.level | selectedFilter(gContestLevelEntries, true)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="大赛简介：" class="white-space-pre-static word-break">
                                <span v-html="proModel.introduction"></span>
                            </e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>
                <e-panel label="大赛材料" v-if="applyFiles.length > 0">
                    <ul class="timeline">
                        <li class="work" v-for="file in applyFiles" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="${ctxImages}/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" size="mini" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                </e-panel>
            </el-tab-pane>
            <el-tab-pane label="团队成员" name="three">
                <e-panel label="团队成员">
                    <team-student-list :team-student="dutyFirstTeamStu"
                                       :leader="leader"></team-student-list>
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
        <el-button size="mini" @click.stop="goToBack">返回</el-button>
    </div>
    <el-dialog title="评分"
               width="520px"
               :visible.sync="dialogVisibleAudit"
               :close-on-click-modal="false"
               :before-close="handleChangeAuditFormClose">
        <promodel-audit-form ref="promodelAuditForm" is-score :pro-model="proModel" :options="[]"  :disabled="disabled" @submit-before="disabled = true" @submit-success="dialogVisibleAudit = false" @submit-complete="disabled=false"></promodel-audit-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="disabled" type="primary"
                       @click.stop="submitAuditForm">确定
            </el-button>
        </div>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('competition_net_type'))}');
            var gContestLevels = JSON.parse('${fns:toJson(fns: getDictList('gcontest_level'))}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var applyFiles = JSON.parse(JSON.stringify(${fns:toJson(applyFiles)})) || [];
            var leader = proModel.deuser;
            var teamStudent = JSON.parse(JSON.stringify(${fns: toJson(teamStu)})) || [];     //没有把成员的photo传给我
            var teamTeacher = JSON.parse(JSON.stringify(${fns: toJson(teamTea)})) || [];
            var auditRecordList = JSON.parse(JSON.stringify(${fns: toJson(actYwAuditInfos)})) || [];
            var actYwStatusList = JSON.parse(JSON.stringify(${fns: toJson(actYwStatusList)})) || [{
                        status: '1',
                        state: '通过'
                    }, {status: '0', state: '不通过'}];

            var validateGScore = function (rules, value, callback) {
                if(value){
                    if((/^[1-9]{1}\d?$|100/).test(value)){
                        return callback();
                    }
                    return callback(new Error('填写0-100之间的整数'));
                }
                return callback();
            }

            return {
                proModel: proModel,
                proCategories: proCategories,
                tabActiveName: 'first',
                leader: leader,
                colleges: professionals,
                applyFiles: applyFiles,
                teamStudent: teamStudent,
                dutyFirstTeamStu: [],
                teamTeacher: teamTeacher,
                gContestLevels: gContestLevels,
                auditRecordList: auditRecordList,
                actYwStatusList: actYwStatusList,
                auditForm: {
                    id: '',
                    act: {
                        taskId: '',
                        taskName: '',
                        taskDefKey: '',
                        procInsId: '',
                        procDefId: ''
                    },
                    source: '',
                    gScore: '',
                    actionPath: '${actionPath}',
                    gnodeId: '${gnodeId}',
                },
                auditFormRules: {
                    gScore: [{required: true, message: '请填写评分', trigger: 'blur'},{validator: validateGScore, trigger: 'blur'}]
                },
                disabled: false,
                dialogVisibleScore: false,
                actionPath:'${actionPath}',
                gnodeId: '${gnodeId}',
                dialogVisibleAudit: false,
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
            },
            gContestLevelEntries: function () {
                return this.getEntries(this.gContestLevels)
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

            goToBack: function () {
                history.go(-1)
            },

            openAuditForm: function () {
                this.dialogVisibleAudit = true;
            },

            handleChangeAuditFormClose: function () {
                this.dialogVisibleAudit = false;
            },
            submitAuditForm: function () {
                this.$refs.promodelAuditForm.submitAuditForm();
            }
        },

        created: function () {
            this.getDutyFirst();
            this.proModel.logoUrl = this.proModel.logo ? this.proModel.logo.url : '';
            this.assignFormData(this.auditForm, this.proModel)
        }
    })
</script>
</body>

</html>
