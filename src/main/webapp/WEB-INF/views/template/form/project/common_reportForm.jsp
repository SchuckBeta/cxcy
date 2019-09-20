<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>${frontTitle}</title>
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
        <el-breadcrumb-item>{{gnodeName}}</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="panel panel-project mgb-20">
        <div class="panel-header panel-header-title"><span>{{proModel.pName}}</span></div>
        <div class="panel-body">
            <div class="project-pic">
                <img :src="proModel.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proGConPicFilter">
            </div>
            <div class="project-info_list">
                <e-col-item label="项目编号：" align="right" style="margin-bottom: 10px;">
                    <span>{{proModel.competitionNumber}}</span>
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
                            <e-col-item label="项目简介：" class="white-space-pre-static word-break"><span
                                    v-html="proModel.introduction"></span></e-col-item>
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

                <e-panel :label="item.gnodeName || 'gnodeName'" v-if="reports.length > 0" v-for="item in reports"
                         :key="item.id">
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
                            <e-col-item label="已取得阶段性成果：" class="white-space-pre-static word-break"><span
                                    v-html="item.stageResult"></span></e-col-item>
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
            <el-tab-pane :label="gnodeName" name="four">
                <e-panel :label="gnodeName">
                    <el-form :model="reportForm" :rules="reportFormRules" ref="reportForm" size="mini" :disabled="reportFormDisabled"
                             :action="frontOrAdmin + '/proproject/reportSubmit'"
                             method="post"
                             enctype="multipart/form-data"
                             label-width="160px">
                        <input type="hidden" name="gnodeId" value="${gnodeId}"/>
                        <input type="hidden" name="proModelId" value="${proModel.id}"/>
                        <input type="hidden" name="id" value="${proReport.id}"/>
                        <el-form-item prop="stageResult" label="已取得阶段性成果：">
                            <el-input v-model="reportForm.stageResult" name="stageResult" type="textarea" placeholder="请输入已取得的阶段性成果，最多2000字" rows="5" maxlength="2001"></el-input>
                        </el-form-item>
                        <el-form-item prop="uploaderFiles" label="上传报告：">
                            <e-upload-file v-model="reportForm.uploaderFiles"
                                           action="/ftp/ueditorUpload/uploadTemp?folder=project"  :upload-file-vars="uploadFileVars"></e-upload-file>
                        </el-form-item>
                        <el-form-item>
                            <el-button v-if="!hideSubmit" :disabled="isFileUploading" type="primary" @click.stop.prevent="submitTask">{{gnodeName}}</el-button>
                            <el-button :disabled="isFileUploading" type="default" @click.stop.prevent="goToBack">返回</el-button>
                        </el-form-item>
                    </el-form>
                </e-panel>
            </el-tab-pane>
        </el-tabs>
    </div>
</div>


<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns:toJson(proModel)})) || {};
            var professionals = JSON.parse(JSON.stringify(${fns: getOfficeListJson()})) || [];
            var teamStudent = JSON.parse(JSON.stringify(${fns: toJson(teamStu)})) || [];
            var teamTeacher = JSON.parse(JSON.stringify(${fns: toJson(teamTea)})) || [];
            var applyFiles = JSON.parse(JSON.stringify(${fns:toJson(applyFiles)})) || [];
            var reports = JSON.parse(JSON.stringify(${fns:toJson(reports)})) || [];
            var proReport = JSON.parse(JSON.stringify(${fns: toJson(proReport)} ))|| {};
            var proCategories = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('project_type'))}));
            var leader = proModel.deuser;
            return {
                gnodeName: '${gnode.name}',
                proCategories: proCategories,
                proModel: proModel,
                colleges: professionals,
                tabActiveName: 'first',
                leader:leader,
                reports: reports,
                applyFiles: applyFiles,
                teamStudent: teamStudent,
                dutyFirstTeamStu:[],
                teamTeacher: teamTeacher,
                reportForm: {
                    stageResult: proReport.stageResult,
                    uploaderFiles:  proReport.files || [],
                },
                reportFormDisabled: false,
                uploadFileVars: {
                    fileStepEnum: '${fileStepEnum}',
                    fileTypeEnum: '${fileTypeEnum}',
                    gnodeId: '${gnodeId}',
                    fileInfoPrefix: '${fileInfoPrefix}'
                },
                reportFormRules: {
                    stageResult: [
                        {required: true, message: '请输入已取得的阶段性成果', trigger: 'blur'},
                        {max: 2000, message: '请输入1-2000位字符', trigger: 'blur'}
                    ],
                    uploaderFiles: [{required: true, message: '请上传附件', trigger: 'change'}]
                },
                hideSubmit:false
            }
        },
        computed: {
            isFileUploading: function(){
                return this.reportForm.uploaderFiles.length > 0 && this.reportForm.uploaderFiles.some(function (item) {
                            return !item.ftpUrl;
                        })
            },
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
        },
        methods:{
            submitTask: function () {
                var self = this;
                this.$refs.reportForm.validate(function (valid) {
                    if (!valid) {
                        return;
                    }
                    self.confirmSubmit();
                })
            },

            confirmSubmit: function () {
                var self = this;
                this.$confirm('提交后不可修改，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.reportFormDisabled = true;
                    self.ajaxSubmitTask();
                }).catch(function () {

                })
            },
            ajaxSubmitTask: function () {
                var $form = $(this.$refs.reportForm.$el);
                var self = this;
                $form.ajaxSubmit(function (data) {
                    self.reportFormDisabled = false;
                    if (!self.checkUserLogin(data)) {
                        self.hideSubmit = true;
                        self.$alert(data.msg, '提示', {
                            confirmButtonText: '确定',
                            type: data.ret === 1 ? 'success' : 'error',
                            showClose: data.ret !== 1
                        }).then(function () {
                            if (data.ret === 1) {
                                location.href = self.frontOrAdmin + '/project/projectDeclare/curProject'
                            }
                        })
                    }
                })
            },
            goToBack: function () {
                return history.go(-1);
            },
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
            }
        },
        created:function () {
            this.getDutyFirst();
        }
    })

</script>
</body>
</html>
