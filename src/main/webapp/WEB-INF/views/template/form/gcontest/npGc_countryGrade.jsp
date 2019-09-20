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
            <el-button type="primary" size="mini" :disabled="disabled" @click.stop="openScoreForm">审核</el-button>
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
                            <e-col-item label="大赛简称：">{{proModel.shortName}}</e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="大赛类别：">{{proModel.proCategory | selectedFilter(proCategoryEntries, true)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="12">
                            <e-col-item label="参赛组别：">{{proModel.level | selectedFilter(gContestLevelEntries, true)}}
                            </e-col-item>
                        </el-col>
                        <el-col :span="24">
                            <e-col-item label="大赛简介：" class="white-space-pre-static word-break"><span v-html="proModel.introduction"></span></e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>

                <e-panel label="项目材料" v-if="applyFiles.length > 0">
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

                <e-panel :label="item.gnodeName || 'gnodeName'" v-if="reports.length > 0" v-for="item in reports"
                         :key="item.id">
                    <ul class="timeline">
                        <li class="work" v-for="file in item.files" :key="file.id">
                            <span class="contest-date">{{file.createDate | formatDateFilter('YYYY-MM-DD HH:mm')}}</span>
                            <img src="${ctxImages}/time-line.png" alt="">
                            <div class="relative">
                                <e-file-item :file="file" :show="false"></e-file-item>
                            </div>
                        </li>
                    </ul>
                    <el-row :gutter="20" label-width="150px" style="margin-left:53px;">
                        <el-col :span="20">
                            <e-col-item label="已取得阶段性成果：" class="white-space-pre-static word-break">
                                <span v-html="item.stageResult"></span>
                            </e-col-item>
                        </el-col>
                    </el-row>
                </e-panel>

            </el-tab-pane>
            <el-tab-pane label="团队成员" name="three">
                <e-panel label="团队成员">
                    <team-student-list v-if="!isImportedData" :team-student="dutyFirstTeamStu"
                                       :leader="leader"></team-student-list>
                    <div v-else class="table-container">
                        <table class="table table-bordered table-default table-hover text-center"
                               style="margin-bottom: 0">
                            <thead>
                            <tr>
                                <th>姓名</th>
                                <th>学号</th>
                                <th>手机号</th>
                                <th>专业</th>
                                <th>所在学校</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in gmembers">
                                <td>{{item.name}}</td>
                                <td>{{item.no}}</td>
                                <td>{{item.mobile}}</td>
                                <td>{{item.professional}}</td>
                                <td>{{item.school}}</td>
                            </tr>
                            <tr v-if="gmembers.length === 0">
                                <td colspan="4" class="empty">暂无数据</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </e-panel>
                <e-panel label="指导老师">
                    <team-teacher-list v-if="!isImportedData" :team-teacher="teamTeacher"></team-teacher-list>
                    <div v-else class="table-container">
                        <table class="table table-bordered table-default table-hover text-center"
                               style="margin-bottom: 0">
                            <thead>
                            <tr>
                                <th>姓名</th>
                                <th>工号</th>
                                <th>地区</th>
                                <th>高校</th>
                                <th>部门</th>
                                <th>职称（职务）</th>
                                <th>联系电话</th>
                                <th>E-mail</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in gteachers">
                                <td>{{item.name}}</td>
                                <td>{{item.no}}</td>
                                <td>{{item.area}}</td>
                                <td>{{item.school}}</td>
                                <td>{{item.subject}}</td>
                                <td>{{item.technicalTitle}}</td>
                                <td>{{item.mobile}}</td>
                                <td>{{item.email}}</td>
                            </tr>
                            <tr v-if="gteachers.length === 0">
                                <td colspan="8" class="empty">暂无数据</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </e-panel>
            </el-tab-pane>
            <el-tab-pane v-if="gbusinfos.length > 0" label="工商信息" name="gsxx">
                <e-panel label="工商信息">
                    <div class="table-container">
                        <table class="table table-bordered table-default table-hover text-center"
                               style="margin-bottom: 0">
                            <thead>
                            <tr>
                                <th>公司名称</th>
                                <th>注册所在地</th>
                                <th>社会统一号</th>
                                <th>法人代表</th>
                                <th>注册资金</th>
                                <th>注册时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr v-for="item in gbusinfos">
                                <td>{{item.cyname}}</td>
                                <td>{{item.area}}</td>
                                <td>{{item.no}}</td>
                                <td>{{item.name}}</td>
                                <td>{{item.money}}</td>
                                <td>{{item.regTime}}</td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                </e-panel>
            </el-tab-pane>

            <el-tab-pane v-if="auditRecordList.length > 0" label="审核记录" name="four">
                <e-panel label="审核记录">
                    <div class="table-container">
                        <table class="table table-bordered table-default table-hover text-center"
                               style="margin-bottom: 0">
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
                                <td v-if="!item.id" colspan="5" class="score-row" style="text-align:right;">
                                    {{item.auditName}}：{{item.result}}
                                </td>
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
    <el-dialog title="审核"
               width="520px"
               :visible.sync="dialogVisibleScore"
               :close-on-click-modal="false"
               :before-close="handleChangeScoreClose">
        <el-form :model="auditForm" ref="auditForm" :rules="auditFormRules" size="mini" :disabled="disabled"
                 label-width="120px" autocomplete="off">
            <el-form-item prop="result" label="获奖情况：">
                <el-select v-model="auditForm.result">
                    <el-option v-for="item in awardList" :key="item.value" :value="item.value" :label="item.label"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="grade" label="推荐：">
                <el-select v-model="auditForm.grade">
                    <el-option v-for="item in actYwStatusList" :key="item.status" :value="item.status"
                               :label="item.state"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="disabled" type="primary"
                       @click.stop="validateForm">确定
            </el-button>
        </div>
    </el-dialog>
</div>
</body>
<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('competition_net_type'))}');
            var gContestLevels = JSON.parse('${fns:toJson(fns: getDictList('gcontest_level'))}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var applyFiles = JSON.parse('${fns:toJson(sysAttachments)}') || [];
            var reports = JSON.parse(JSON.stringify(${fns:toJson(reports)})) || [];
            var leader = proModel.deuser;
            var teamStudent = JSON.parse('${fns: toJson(teamStu)}') || [];     //没有把成员的photo传给我
            var teamTeacher = JSON.parse('${fns: toJson(teamTea)}') || [];
            var auditRecordList = JSON.parse(JSON.stringify(${fns: toJson(actYwAuditInfos)})) || [];
            var awardList = JSON.parse('${fns:toJson(fns: getDictList('0000000162'))}');
            var proModelMdGc = JSON.parse(JSON.stringify(${fns: toJson(proModelMdGc)})) || {};
            var gbusinfos = proModelMdGc.gbusinfos || [];
            var gteachers = proModelMdGc.gteachers || [];
            var gmembers = proModelMdGc.gmembers || [];
            var actYwStatusList = JSON.parse(JSON.stringify(${fns: toJson(actYwStatusList)})) || [{
                        status: '998',
                        state: '通过'
                    }, {status: '999', state: '不通过'}];
            <%--var actYwStatusList = JSON.parse(JSON.stringify(${fns: toJson(actYwStatusList)})) || [{--%>
                        <%--status: '1',--%>
                        <%--state: '通过'--%>
                    <%--}, {status: '0', state: '不通过'}];--%>
            return {
                proModel: proModel,
                proCategories: proCategories,
                tabActiveName: 'first',
                leader: leader,
                colleges: professionals,
                applyFiles: applyFiles,
                reports: reports,
                teamStudent: teamStudent,
                dutyFirstTeamStu: [],
                teamTeacher: teamTeacher,
                gbusinfos: gbusinfos,
                gteachers: gteachers,
                gmembers: gmembers,
                proModelMdGc: proModelMdGc,
                auditRecordList: auditRecordList,
                gContestLevels: gContestLevels,
                awardList: awardList,
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
                    result: '',
                    grade: '',
                    awardType: '0000000162',
                    actionPath: '${actionPath}',
                    gnodeId: '${gnodeId}',
                },
                auditFormRules: {
                    result: [{required: true, message: '请选择获奖情况', trigger: 'change'}],
                    grade: [{required: true, message: '请选择推荐情况', trigger: 'change'}]
                },
                disabled: false,
                dialogVisibleScore: false
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
            },
            isImportedData: function () {
                return this.proModel.impdata === '1'
            },

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

            openScoreForm: function () {
                this.dialogVisibleScore = true;
            },

            handleChangeScoreClose: function () {
                this.dialogVisibleScore = false;
            },

            validateForm: function () {
                var self = this;
                this.$refs.auditForm.validate(function (valid) {
                    if (valid) {
                        self.submitScoreForm();
                    }
                })
            },
            getParams: function () {
                var auditForm = JSON.parse(JSON.stringify(this.auditForm));
                delete auditForm.act;
                var auditFormParams = Object.toURLSearchParams(auditForm).toString();
                var actParams = this.getActParams();
                var params = [auditFormParams];
                if (actParams) {
                    params.push(actParams)
                }
                return params.join('&');
            },


            getActParams: function () {
                var act = this.auditForm.act;
                var entry = {};
                for (var k in act) {
                    if (act.hasOwnProperty(k)) {
                        entry['act.' + k] = act[k];
                    }
                }
                return Object.toURLSearchParams(entry).toString()
            },

            submitScoreForm: function () {
                var self = this;
                this.disabled = true;
                var params = this.getParams();
                this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/saveProModelGateAudit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: params
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            var referrer = document.referrer;
                            location.href = referrer;
                        }).catch(function () {
                            location.reload();
                        });
                        self.dialogVisibleScore = false;
                        self.changeSideTodoNum('${proModel.actYwId}');
                        <%--window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');--%>
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        });
                        self.disabled = false;
                    }
                })
            }
        },

        created: function () {
            this.getDutyFirst();
            this.proModel.logoUrl = this.proModel.logo ? this.proModel.logo.url : '';
            this.assignFormData(this.auditForm, this.proModel)
        }
    })

</script>
</html>
