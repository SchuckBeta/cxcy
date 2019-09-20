<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%--<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>--%>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar :second-name="secondName"></edit-bar>
    </div>
    <div class="panel panel-pw-enter mgb-20">
        <div class="panel-body">
            <applicant-info-pro-contest :apply-user="applyUser"></applicant-info-pro-contest>
            <div class="pro-category-placeholder"></div>
        </div>
    </div>
    <div class="pro-contest-content panel-padding-space mgb-20">
        <div class="btn-audit-box">
            <el-button type="primary" size="mini" :disabled="disabled || isFileUploading" @click.stop="validateAllForm">保存</el-button>
        </div>
        <el-tabs class="pro-contest-tabs" v-model="tabActiveName">
            <el-tab-pane label="项目信息" name="firstCreditTab">
                <tab-pane-content>
                    <e-panel label="项目信息">
                        <el-form :model="proModelHsxmForm" ref="proModelHsxmForm" :rules="proModelHsxmRules"
                                 :disabled="disabled" autocomplete="off"
                                 size="mini" label-width="120px">
                            <el-form-item prop="proModel.subTime" label="申报日期：" inline-message>
                                <el-date-picker
                                        v-model="proModelHsxmForm.proModel.subTime"
                                        type="date"
                                        value-format="yyyy-MM-dd"
                                        placeholder="选择日期">
                                </el-date-picker>
                            </el-form-item>
                            <el-form-item label="项目编号：" prop="proModel.competitionNumber">
                                <el-input v-model="proModelHsxmForm.proModel.competitionNumber" maxlength="64"
                                          class="w300"></el-input>
                            </el-form-item>
                            <el-form-item label="项目名称：" prop="proModel.pName">
                                <el-input v-model="proModelHsxmForm.proModel.pName" maxlength="128"
                                          placeholder="最多128个字符"></el-input>
                            </el-form-item>
                            <el-form-item label="项目类别：" prop="proModel.proCategory">
                                <el-select v-model="proModelHsxmForm.proModel.proCategory">
                                    <el-option v-for="item in projectStyles" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="项目来源：" prop="source">
                                <el-select v-model="proModelHsxmForm.source" clearable>
                                    <el-option v-for="item in projectSources" :key="item.value" :value="item.value"
                                               :label="item.label"></el-option>
                                </el-select>
                            </el-form-item>
                            <el-form-item label="项目拓展及传承：">
                                1、项目能与其他大型比赛、活动对接 2、可在低年级同学中传承 3、结项后能继续开展
                            </el-form-item>
                            <el-form-item label="项目Logo：" prop="proModel.logoUrl" class="common-upload-one-img">
                                <div class="upload-box-border site-logo-left-size">
                                    <div class="avatar-uploader">
                                        <div tabindex="0" class="el-upload el-upload--text"
                                             @click.stop="openDialogLogo">
                                            <img :src="proModelHsxmForm.proModel.logoUrl | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                                        </div>
                                    </div>
                                    <div v-show="proModelHsxmForm.proModel.logoUrl && !disabled"
                                         class="arrow-block-delete"
                                         @click.stop="proModelHsxmForm.proModel.logoUrl = ''">
                                        <i class="el-icon-delete"></i>
                                    </div>
                                </div>
                            </el-form-item>
                            <el-form-item prop="sysAttachmentList" label="附件：">
                                <e-pw-upload-file v-model="proModelHsxmForm.sysAttachmentList"
                                                  action="/ftp/ueditorUpload/uploadPwTemp?folder=project" :upload-file-vars="{}"
                                                  is-post
                                                  tip="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件<br>（上传项目申报表单、商业计划书等项目文件）"
                                                  @change="handleChangeFiles"></e-pw-upload-file>
                            </el-form-item>
                            <el-form-item prop="proModel.introduction" label="项目简介：">
                                <el-input type="textarea" v-model="proModelHsxmForm.proModel.introduction" :rows="3"
                                          placeholder="1、请简要描述项目的立项背景、项目的主要内容及实施目标； 2、项目的特色或创新点。(最多2000个字符)"
                                          maxlength="2000"></el-input>
                            </el-form-item>
                            <div style="margin: 0 -30px">
                                <div class="enter-form-group-titlebar">
                                    <span class="title">项目介绍 <i class="el-icon-d-arrow-right"></i></span>
                                </div>
                            </div>
                            <el-form-item prop="innovation" label="前期调研准备：">
                                <el-input type="textarea" v-model="proModelHsxmForm.innovation" :rows="3"
                                          maxlength="2000"></el-input>
                            </el-form-item>
                            <el-form-item label="项目预案：" required>
                                <div class="table-container">
                                    <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                                           cellspacing="0" cellpadding="0">
                                        <thead>
                                        <tr>
                                            <th>实施预案</th>
                                            <th width="300">时间安排</th>
                                            <th>保障措施</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr>
                                            <td>
                                                <el-form-item prop="planContent" label-width="0"
                                                              :rules="requiredTextRules">
                                                    <el-input type="textarea" v-model="proModelHsxmForm.planContent"
                                                              :rows="3"
                                                              maxlength="2000"></el-input>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item prop="planDateRange" label-width="0"
                                                              :rules="requiredDateRules">
                                                    <el-date-picker
                                                            v-model="proModelHsxmForm.planDateRange"
                                                            type="daterange"
                                                            unlink-panels
                                                            :default-time="defaultTime"
                                                            format="yyyy-MM-dd HH:mm:ss"
                                                            value-format="yyyy-MM-dd HH:mm:ss"
                                                            @change="handleChangePlanDateRange"
                                                            range-separator="至"
                                                            start-placeholder="开始日期"
                                                            end-placeholder="结束日期">
                                                    </el-date-picker>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item prop="planStep" label-width="0"
                                                              :rules="requiredTextRules">
                                                    <el-input type="textarea" v-model="proModelHsxmForm.planStep"
                                                              :rows="3"
                                                              maxlength="2000"></el-input>
                                                </el-form-item>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </el-form-item>
                            <el-form-item label="任务分工：" required>
                                <div class="table-container">
                                    <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
                                           style="table-layout: fixed"
                                           cellspacing="0" cellpadding="0">
                                        <thead>
                                        <tr>
                                            <th>工作任务</th>
                                            <th>任务描述</th>
                                            <th width="346">时间安排</th>
                                            <th width="80">成本</th>
                                            <th>质量评价</th>
                                            <th width="50">操作</th>
                                        </tr>
                                        </thead>
                                        <tbody>
                                        <tr v-for="(item, index) in proModelHsxmForm.planList">
                                            <td>
                                                <el-form-item :prop="'planList.'+index+'.content'" label-width="0"
                                                              :rules="requiredTextRules">
                                                    <el-input type="textarea" v-model="item.content" :rows="3"
                                                              maxlength="2000"></el-input>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item :prop="'planList.'+index+'.description'" label-width="0"
                                                              :rules="requiredTextRules">
                                                    <el-input type="textarea" v-model="item.description" :rows="3"
                                                              maxlength="2000"></el-input>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item :prop="'planList.'+index+'.dateRange'"
                                                              :rules="requiredDateRules">
                                                    <el-date-picker
                                                            v-model="item.dateRange"
                                                            type="daterange"
                                                            unlink-panels
                                                            :default-time="defaultTime"
                                                            @change="handleChangeDateRange(index)"
                                                            range-separator="至"
                                                            start-placeholder="开始日期"
                                                            style="width: 330px"
                                                            format="yyyy-MM-dd HH:mm:ss"
                                                            value-format="yyyy-MM-dd HH:mm:ss"
                                                            end-placeholder="结束日期">
                                                    </el-date-picker>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item :prop="'planList.'+index+'.cost'" label-width="0"
                                                              :rules="requiredCostRules">
                                                    <el-input v-model="item.cost" maxlength="20"></el-input>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item :prop="'planList.'+index+'.quality'" label-width="0"
                                                              :rules="requiredTextRules">
                                                    <el-input type="textarea" v-model="item.quality" :rows="3"
                                                              maxlength="2000"></el-input>
                                                </el-form-item>
                                            </td>
                                            <td>
                                                <el-form-item>
                                                    <el-button style="margin-bottom: 5px;" icon="el-icon-plus"
                                                               @click.stop="addPlanList"></el-button>
                                                    <br>
                                                    <el-button v-show="proModelHsxmForm.planList.length > 1"
                                                               icon="el-icon-minus"
                                                               @click.stop="proModelHsxmForm.planList.splice(index, 1)"></el-button>
                                                </el-form-item>
                                            </td>
                                        </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </el-form-item>
                            <div style="margin: 0 -30px">
                                <div class="enter-form-group-titlebar">
                                    <span class="title">预期成果 <i class="el-icon-d-arrow-right"></i></span>
                                </div>
                            </div>
                            <el-form-item prop="resultType" label="成果形式：">
                                <el-checkbox-group v-model="proModelHsxmForm.resultType">
                                    <el-checkbox v-for="item in projectResultTypes" :label="item.value"
                                                 :key="item.value">
                                        {{item.label}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                            <el-form-item prop="resultContent" label="成果说明：">
                                <el-input type="textarea" v-model="proModelHsxmForm.resultContent" :rows="3"
                                          maxlength="2000"></el-input>
                            </el-form-item>
                            <div style="margin: 0 -30px">
                                <div class="enter-form-group-titlebar">
                                    <span class="title">经费预算明细 <i class="el-icon-d-arrow-right"></i></span>
                                </div>
                            </div>
                            <el-form-item prop="budget" label="经费预算明细：">
                                <el-input type="textarea" v-model="proModelHsxmForm.budget" :rows="3"
                                          placeholder="简要描述在项目各个阶段产生的费用项目及明细，如：硬件采购、耗材费、差旅费等"
                                          maxlength="2000"></el-input>
                            </el-form-item>
                            <div style="margin: 0 -30px">
                                <div class="enter-form-group-titlebar">
                                    <span class="title">团队信息 <i class="el-icon-d-arrow-right"></i></span>
                                </div>
                            </div>
                            <el-form-item label="团队名称：" required>{{proModelHsxm.proModel.team.name}}
                                <el-button type="text" @click.stop="openTeamChange">成员变更记录</el-button>
                            </el-form-item>
                            <update-member :declare-id="applyUser.id" :disabled="disabled"
                                           :team-id="proModelHsxm.proModel.teamId" is-admin
                                           :saved-team-url="savedTeamUrl"
                                           @change="handleChangeTeam"></update-member>
                            <el-form-item v-if="changeGnodes.length > 0" label="变更流程到节点：" prop="toGnodeId">
                                <el-select v-model="proModelHsxmForm.proModel.toGnodeId" clearable filterable>
                                    <el-option v-for="item in changeGnodes" :label="item.auditName"
                                               :value="item.gnodeId"
                                               :key="item.gnodeId"></el-option>
                                </el-select>
                            </el-form-item>
                        </el-form>
                    </e-panel>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane v-if="reports.length > 0" label="项目报告" name="secondCreditTab">
                <tab-pane-content>
                    <el-form :model="reportsForm" ref="reportsForm" :rules="reportsRules" size="mini"
                             :disabled="disabled" label-width="120px">
                        <e-panel v-for="(item, index) in reportsForm.reports" :key="item.gnodeId">
                            <act-yw-gnode-label slot="label"
                                                :url="'/promodel/proModel/getActYwGnode/'+item.gnodeId"></act-yw-gnode-label>
                            <el-form-item :prop="'reports.'+ index + '.files'" :rules="reportsRulesFile" label="附件：">
                                <e-pw-upload-file v-model="reportsForm.reports[index].files"
                                                  action="/ftp/ueditorUpload/uploadPwTemp?folder=project"
                                                  :upload-file-vars="{}"
                                                  is-post
                                                  tip="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件，请到时间轴页面下载相关报告模板"
                                                  @change="handleChangeReportFiles('reports.'+ index + '.files')"></e-pw-upload-file>
                            </el-form-item>
                        </e-panel>
                    </el-form>
                </tab-pane-content>
            </el-tab-pane>
            <el-tab-pane label="审核记录" name="threeCreditTab">
                <tab-pane-content>
                    <promodel-audit-record :pro-model-id="proModelHsxm.proModel.id"></promodel-audit-record>
                </tab-pane-content>
            </el-tab-pane>
        </el-tabs>
    </div>
    <el-dialog title="上传项目Logo"
               width="440px"
               :visible.sync="dialogVisibleChangeLogo"
               :close-on-click-modal="false"
               :before-close="handleChangeLogoPicClose">
        <e-pic-file v-model="logoPic" :disabled="isUpdating" @get-file="getLogoPicFile"></e-pic-file>
        <cropper-pic :img-src="logoPic" default-img="${ctxImages}/default-pic.png" :disabled="isUpdating"
                     ref="cropperPic"></cropper-pic>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="isUpdating" @click.stop.prevent="handleChangeLogoPicClose">取消</el-button>
            <el-button size="mini" :disabled="!logoPicFile || isUpdating" type="primary"
                       @click.stop.prevent="updateUserPic">上传
            </el-button>
        </div>
    </el-dialog>
    <el-dialog
            title="成员变更记录"
            :visible.sync="dialogVisibleTeamChange"
            width="600px"
            :close-on-click-modal="false"
            :before-close="handleCloseTeamChange">
        <div class="table_user-list-container" style="height: auto">
            <div class="table_user_wrapper">
                <div class="table-container">
                    <el-table :data="teamUserChangeList" size="mini" class="table" empty-text="无变更记录">
                        <el-table-column label="变更成员" prop="name" width="100"></el-table-column>
                        <el-table-column label="变更内容" prop="content"></el-table-column>
                        <el-table-column label="变更时间" prop="date" width="160"></el-table-column>
                    </el-table>
                </div>
            </div>
        </div>
    </el-dialog>
    <div class="text-center">
        <el-button size="mini" @click.stop="goToBack">返回</el-button>
    </div>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var proModelHsxm = JSON.parse(JSON.stringify(${fns: toJson(proModelHsxm)}));
            var proModel = proModelHsxm.proModel;
            var savedTeamUrl = '/promodel/proModel/getProChangedTeam/' + proModel.id + '?teamId=' + proModel.teamId;
            var changeGnodes = JSON.parse(JSON.stringify(${fns: toJson(changeGnodes)})) || [];
            var reports = JSON.parse(JSON.stringify(${fns: toJson(reports)})) || [];
            var projectHsxmApplyFiles = JSON.parse('${fns:toJson(applyFiles)}') || [];
            reports.forEach(function (report) {
                report.files.forEach(function (item, index) {
                    item.uid += index;
                    return item;
                })
            });


            var validateNumber = function (rules, value, callback) {
                if (value) {
                    if (/^(([1-9]{1}\d*)|0)\.?\d{0,3}$/.test(value)) {
                        return callback();
                    }
                    return callback(new Error('请输入数字'))
                }
                return callback();
            };

            var validateCheckSpSt = function (rules, value, callback) {
                return self.$axios.get('/sys/checkNotSpSteel?str='+encodeURI(value))
            }
            var validatePName = function (rules, value, callback) {
                var proModelHsxmForm = self.proModelHsxmForm;
                var proModelForm = proModelHsxmForm.proModel;
                var params = {
                    id: proModelForm.id,
                    actYwId: proModelForm.actYwId,
                    pName: proModelForm.pName
                };
                return self.$axios.get('/promodel/proModel/checkProName', {params: params})
            }
            var validatePNameAndSt = function (rules, value, callback) {
                return self.$axios.all([validateCheckSpSt(rules, value, callback), validatePName(rules, value, callback)])
                        .then(self.$axios.spread(function (spStrReq, nameReq) {
                            if(spStrReq.data.data && nameReq.data){
                                return callback();
                            }
                            if(!spStrReq.data.data){
                                return callback(new Error(spStrReq.data.msg));
                            }
                            return callback(new Error("项目名称已存在"));
                            // Both requests are now complete
                        }));
            }
            var validateCompetitionNumber = function (rules, value, callback) {
                if (value) {
                    var proModelForm = self.proModelHsxmForm.proModel;
                    var params = {
                        id: proModelForm.id,
                        num: proModelForm.competitionNumber,
                        actYwId: proModelForm.proModel.actYwId
                    };
                    if(!(/^[a-zA-Z0-9]+$/.test(value))){
                        return callback(new Error('请输入英文或者数字'))
                    }
                    return self.$axios.get('/promodel/proModel/checkNumberByActYwId',{params: params}).then(function (response) {
                        var data = response.data;
                        if (data) {
                            return callback();
                        }
                        return callback(new Error('项目编号已经存在'))
                    }).catch(function () {
                        return callback(new Error(self.xhrErrorMsg))
                    })
                }
                return callback();
            };

            return {
                secondName: '${secondName}',
                proModelHsxm: proModelHsxm,
                proModelHsxmForm: {
                    id: '',
                    source: '',
                    innovation: '',
                    planContent: '',
                    planStartDate: '',
                    planEndDate: '',
                    planStep: '',
                    planDateRange: [], //时间安排
                    planList: [
                        {content: '', description: '', startDate: '', endDate: '', dateRange: [], cost: '', quality: ''}
                    ],
                    sysAttachmentList: projectHsxmApplyFiles,
                    resultType: [],
                    resultContent: '',
                    budget: '',
                    proModel: {
                        id: '',
                        year: '',
                        actYwId: '',
                        act: {
                            taskId: '',
                            taskName: '',
                            taskDefKey: '',
                            procInsId: '',
                            procDefId: ''
                        },
                        actionPath: '${actionPath}',
                        gnodeId: '${gnodeId}',
                        subTime: '',
                        competitionNumber: '',
                        pName: '',
                        proCategory: '',
                        logoUrl: '',
                        introduction: '',
                        teamId: '',
                        toGnodeId: ''
                    }
                },
                proModelHsxmRules: {
                    resultType: [
                        {required: true, message: "请选择成果形式", trigger: 'change'}
                    ],
                    resultContent: [
                        {required: true, message: "请输入成果说明", trigger: 'blur'}
                    ],
                    budget: [
                        {required: true, message: "请输入经费预算明细", trigger: 'blur'}
                    ],
                    proModel: {
                        subTime: [
                            {required: true, message: "请选择申报日期", trigger: 'change'}
                        ],
                        pName: [
                            {required: true, message: "请输入项目名称", trigger: 'blur'},
                            {validator: validatePNameAndSt, trigger: 'blur'},
                        ],
                        competitionNumber: [
                            {required: true, message: "请输入项目名称", trigger: 'blur'},
                            {validator: validateCompetitionNumber, trigger: 'blur'}
                        ],
                        proCategory: [
                            {required: true, message: "请选择项目类别", trigger: 'change'}
                        ],
                        introduction: [
                            {required: true, message: "请输入项目简介", trigger: 'blur'}
                        ]
                    },
                    sysAttachmentList: [
                        {required: true, message: '请上传附件', trigger: 'change'}
                    ]
                },
                requiredTextRules: [
                    {required: true, message: "请输入文字", trigger: 'blur'}
                ],
                requiredDateRules: [
                    {required: true, message: "请选择时间", trigger: 'blur'}
                ],
                requiredCostRules: [
                    {required: true, message: "请输入数字", trigger: 'blur'},
                    {validator: validateNumber, trigger: 'blur'}
                ],
                officeList: JSON.parse('${fns: toJson(fns: getOfficeList())}'),
                disabled: false,
                applyUser: proModel.deuser,
                savedTeamUrl: savedTeamUrl,
                tabActiveName: 'firstCreditTab',

                reports: reports,
                reportsForm: {
                    reports: reports
                },
                reportsRules: {},
                reportsRulesFile: [
                    {required: true, message: '请上传附件', trigger: 'change'}
                ],

                projectStyles: [],
                projectSources: [],
                projectResultTypes: [],

                dialogVisibleChangeLogo: false,
                logoPicFile: null,
                isUpdating: false,
                logoPic: '',

                defaultTime: ['00:00:00', '23:59:59'],

                studentList: [],
                teacherList: [],

                dialogVisibleTeamChange: false,
                teamUserChangeList: [],
                changeGnodes: changeGnodes,

                fileKeysYs: {
                    fielSize: 'size',
                    fielTitle: 'name',
                    fielType: 'suffix',
                    fielFtpUrl: 'tempUrl',
                    gnodeId: 'gnodeId'
                }
            }

        },
        computed: {
            isFileUploading: function(){
                return this.proModelHsxmForm.sysAttachmentList.length > 0 && this.proModelHsxmForm.sysAttachmentList.some(function (item) {
                            return !item.ftpUrl;
                        })
            },
            officeEntries: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },
            applyDate: function () {
                return this.proModelHsxm.createDate
            },
            defaultPlanL: function () {
                return {
                    content: '',
                    description: '',
                    startDate: '',
                    endDate: '',
                    dateRange: [],
                    cost: '',
                    quality: ''
                }
            },
            studentParamObj: function () {
                var studentList = this.studentList;
                var entry = {};
                studentList.forEach(function (item, index) {
                    entry['studentList[' + index + '].userId'] = item['userId'];
                    entry['studentList[' + index + '].userType'] = item['userType'];
                    entry['studentList[' + index + '].userzz'] = item['userzz'];
                });
                return entry;
            },
            teacherParamObj: function () {
                var teacherList = this.teacherList;
                var entry = {};
                teacherList.forEach(function (item, index) {
                    entry['teacherList[' + index + '].userId'] = item['userId'];
                    entry['teacherList[' + index + '].userType'] = item['userType'];
                });
                return entry;
            },
        },
        methods: {
            handleChangeFiles: function (file) {
                this.$refs.proModelHsxmForm.validateField('sysAttachmentList')
            },
            validateProModelForm: function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    self.$refs.proModelHsxmForm.validate(function (valid, object) {
                        if (valid) {
                            resolve()
                        } else {
                            reject({
                                formName: '项目信息',
                                error: object
                            })
                        }
                    })
                })
            },

            validateReportsForm: function () {
                var self = this;
                return new Promise(function (resolve, reject) {
                    self.$refs.reportsForm.validate(function (valid, object) {
                        if (valid) {
                            resolve()
                        } else {
                            reject({
                                formName: '项目报告',
                                error: object
                            })
                        }
                    })
                })
            },

            validateAllForm: function () {
                var self = this;
                var reportsForm = this.$refs.reportsForm;
                var proModelHsxmForm = this.$refs.proModelHsxmForm;
                var promiseAll = [];
                reportsForm && promiseAll.push(this.validateReportsForm());
                proModelHsxmForm && promiseAll.push(this.validateProModelForm());
                Promise.all(promiseAll).then(function () {
                    self.submitProModelForm();
                }).catch(function (error) {
                    self.$alert('请检查' + error.formName + '表单是否合格', '提示',{
                        type: 'error'
                    });
                })
            },


            submitProModelForm: function () {
                var self = this;
                var params = this.getParams();
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/saveProjectEdit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: params
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.$alert(data.msg, '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            location.href = self.ctx+ "/cms/form/queryMenuList/?actywId=${proModel.actYwId}";
                        }).catch(function () {
                            location.reload();
                        })
                        window.parent.sideNavModule.changeUnreadTag('${proModel.actYwId}');
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                        self.disabled = false;
                    }
                }).catch(function () {

                })
            },

            getSysAttachmentListParams: function () {
                var sysAttachmentList = this.proModelHsxmForm.sysAttachmentList || [];
                var fileKeysYs = this.fileKeysYs;
                var paramsPaths = [];
                sysAttachmentList.forEach(function (item) {
                    var fileStep = item.fileStep;
                    var entry = {};
                    for (var k in fileKeysYs) {
                        var paramsStr = '';
//                        if (fileStep) {
                            paramsStr = 'proModel.';
//                        }
                        if (fileKeysYs.hasOwnProperty(k)) {
                            paramsStr = paramsStr + 'attachMentEntity.' + k;
                            entry[paramsStr] = item[fileKeysYs[k]];
                        }
                    }
                    paramsPaths.push(Object.toURLSearchParams(entry).toString())
                });
                return paramsPaths.join('&');
            },

            getParams: function () {
                var proModelHsxmForm = JSON.parse(JSON.stringify(this.proModelHsxmForm));
                var resultType = proModelHsxmForm.resultType;
                var resultTypeParams = [];
                var planListParams = this.getPlanListParams(proModelHsxmForm.planList);
                resultType.forEach(function (item) {
                    resultTypeParams.push('resultType=' + item);
                });
                resultTypeParams = resultTypeParams.join('&');

                var proModelHsxmParams = this.getProModelHsxmParams();
                var proModelParams = this.getProModelParams();
                var actParams = this.getActParams();
                var reportParams = this.getReportParams();
                var stuTeaListParams = this.getStuTeaListParams();
                var sysAttachmentListParams = this.getSysAttachmentListParams();
                var params = [proModelHsxmParams, planListParams, resultTypeParams, proModelParams,stuTeaListParams, reportParams, sysAttachmentListParams];

                if (actParams) {
                    params.push(actParams)
                }
                return params.join('&');
            },

            getStuTeaListParams: function () {
              return  Object.toURLSearchParams(Object.assign(this.studentParamObj, this.teacherParamObj)).toString();
            },

            getProModelParams: function () {
                var proModelHsxmForm = JSON.parse(JSON.stringify(this.proModelHsxmForm));
                var proModelForm = proModelHsxmForm.proModel;
                var proModelParams = {};
                delete proModelForm.act;

                for (var p in proModelForm) {
                    if (proModelForm.hasOwnProperty(p)) {
                        if (p === 'logoUrl') {
                            if (proModelForm[p].indexOf('/temp/projectlogo') === -1) {
                                continue;
                            }
                        }
                        proModelParams[p] = proModelForm[p];
                    }
                }
                return Object.toURLSearchParams(proModelParams).toString();
            },

            getPlanListParams: function (planList) {
                var planListEntry = {};
                planList.forEach(function (item, index) {
                    for (var k in item) {
                        if (item.hasOwnProperty(k)) {
                            if (k === 'dateRange') {
                                continue;
                            }
                            planListEntry['planList[' + index + '].' + k] = item[k];
                        }
                    }
                });
                return Object.toURLSearchParams(planListEntry);
            },


            getProModelHsxmParams: function () {
                var proModelHsxmForm = JSON.parse(JSON.stringify(this.proModelHsxmForm));
                delete proModelHsxmForm.id;
                delete proModelHsxmForm.proModel;
                delete proModelHsxmForm.planList;
                delete proModelHsxmForm.resultType;
                delete proModelHsxmForm.planDateRange;
                delete proModelHsxmForm.sysAttachmentList;
                return Object.toURLSearchParams(proModelHsxmForm).toString();
            },

            getReportParams: function () {
                var reports = this.reportsForm.reports || [];
                var fileKeysYs = this.fileKeysYs;
                var paramsPaths = [];
                reports.forEach(function (report) {
                    report.files.forEach(function (item) {
                        var fileStep = item.fileStep;
                        var createDate = item.createDate;
                        var entry = {};
                        for (var k in fileKeysYs) {
                            var paramsStr = '';
//                            if (fileStep || createDate) {
                                paramsStr = 'proModel.';
//                            }
                            if (fileKeysYs.hasOwnProperty(k)) {
                                paramsStr = paramsStr + 'attachMentEntity.' + k;
                                if (k === 'gnodeId') {
                                    entry[paramsStr] = report[k];
                                } else {
                                    entry[paramsStr] = item[fileKeysYs[k]];
                                }
                            }
                        }
                        paramsPaths.push(Object.toURLSearchParams(entry).toString())
                    });
                })
                return paramsPaths.join('&');
            },

            getActParams: function () {
                var act = this.proModelHsxmForm.proModel.act;
                var entry = {};
                for (var k in act) {
                    if (act.hasOwnProperty(k)) {
                        entry['proModel.act.' + k] = act[k];
                    }
                }
                return Object.toURLSearchParams(entry).toString()
            },


            goToBack: function () {
                history.go(-1);
            },
            handleChangeReportFiles: function (path) {
                this.$refs.reportsForm.validateField(path)
            },

            handleCloseTeamChange: function () {
                this.dialogVisibleTeamChange = false;
            },

            openTeamChange: function () {
                this.dialogVisibleTeamChange = true;
                this.getTeamUserChangeList();
            },

            getTeamUserChangeList: function () {
                var self = this;
                var params = {
                    proModelId: this.proModelHsxmForm.id,
                    teamId: this.proModelHsxmForm.proModel.teamId
                };
                this.$axios.get('/team/teamUserChange/ajax/getTeamUserChangeList', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.ret == 1) {
                        self.teamUserChangeList = data.list || []
                    } else {
                        self.teamUserChangeList = [];
                        self.$message.error(data.msg)
                    }
                })
            },

            handleChangeTeam: function (data) {
                this.studentList = data.studentList;
                this.teacherList = data.teacherList;
            },

            handleChangeLogoPicClose: function () {
                this.logoPicFile = null;
                this.logoPic = '';
                this.dialogVisibleChangeLogo = false;
            },

            openDialogLogo: function () {
                var url = this.proModelHsxmForm.proModel.logoUrl;
                if (this.disabled) return false;
                url = this.addFtpHttp(url);
                this.logoPic = url;
                this.dialogVisibleChangeLogo = true;

            },

            getLogoPicFile: function (file) {
                this.logoPicFile = file;
            },

            updateUserPic: function () {
                var data = this.$refs.cropperPic.getData();
                var self = this;
                var formData = new FormData();
                if (data.x < 0 || data.y < 0) {
                    this.$message.error('超出边界，请缩小裁剪框，点击上传');
                    return false;
                }
                this.isUpdating = true;
                formData.append('upfile', this.logoPicFile);
                self.$axios({
                    method: 'POST',
                    url: '/ftp/ueditorUpload/cutImgToTempDir?folder=projectlogo&x=' + parseInt(data.x) + '&y=' + parseInt(data.y) + '&width=' + parseInt(data.width) + '&height=' + parseInt(data.height),
                    data: formData
                }).then(function (response) {
                    var data = response.data;
                    if (data.state === 'SUCCESS') {
                        self.proModelHsxmForm.proModel.logoUrl = data.ftpUrl;
                        self.handleChangeLogoPicClose();
                        self.logoPic = '';
                        self.logoPicFile = null;
                    } else {
                        self.$message.error(data.msg);
                    }
                    self.isUpdating = false;
                })
            },
            addPlanList: function () {
                this.proModelHsxmForm.planList.push(JSON.parse(JSON.stringify(this.defaultPlanL)))
            },

            handleChangePlanDateRange: function (value) {
                if (value && value.length > 0) {
                    this.proModelHsxmForm.planStartDate = value[0];
                    this.proModelHsxmForm.planEndDate = value[1];
                } else {
                    this.proModelHsxmForm.planStartDate = '';
                    this.proModelHsxmForm.planEndDate = '';
                }
            },

            handleChangeDateRange: function (index) {
                var proModelHsxmForm = this.proModelHsxmForm;
                var planList = proModelHsxmForm.planList;
                var dateRange = planList[index].dateRange;
                if (dateRange && dateRange.length > 0) {
                    planList[index].startDate = dateRange[0];
                    planList[index].endDate = dateRange[1]
                } else {
                    planList[index].startDate = '';
                    planList[index].endDate = ''
                }
            },

            setProModelHsxmForm: function () {
                var proModelHsxm = this.proModelHsxm;
                var planStartDate = proModelHsxm.planStartDate;
                var planEndDate = proModelHsxm.planEndDate;
                var planList = proModelHsxm.planList || [];
                planList = planList.map(function (item) {
                    var dateRange = [];
                    if (item.startDate) {
                        dateRange.push(moment(item.startDate).format('YYYY-MM-DD HH:mm:ss'));
                        dateRange.push((moment(item.endDate).format('YYYY-MM-DD')) + ' 23:59:59');
                    }
                    return {
                        content: item.content,
                        description: item.description,
                        startDate: moment(item.startDate).format('YYYY-MM-DD HH:mm:ss'),
                        endDate:(moment(item.endDate).format('YYYY-MM-DD')) + ' 23:59:59',
                        dateRange: dateRange,
                        cost: item.cost,
                        quality: item.quality
                    }
                });
                proModelHsxm.planList = planList.length > 0 ? planList : this.defaultPlanL;
                if (planStartDate) {
                    proModelHsxm.planStartDate = moment(planStartDate).format('YYYY-MM-DD HH:mm:ss');
                    proModelHsxm.planEndDate = moment(planEndDate).format('YYYY-MM-DD HH:mm:ss');
                    proModelHsxm.planDateRange = [proModelHsxm.planStartDate, proModelHsxm.planEndDate]
                }
                proModelHsxm.resultType = proModelHsxm.resultType ? proModelHsxm.resultType.split(',') : [];
                proModelHsxm.proModel.logoUrl = proModelHsxm.proModel.logo ? proModelHsxm.proModel.logo.tempUrl : '${logo.url}';
                proModelHsxm.proModel.subTime = proModelHsxm.proModel.subTime ? moment(proModelHsxm.proModel.subTime).format('YYYY-MM-DD') : '';
                this.assignFormData(this.proModelHsxmForm, proModelHsxm);
            },
            getProjectStyles: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_type').then(function (response) {
                    var data = response.data;
                    self.projectStyles = data || [];
                })
            },
            getProjectSources: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_source').then(function (response) {
                    var data = response.data;
                    self.projectSources = data || [];
                })
            },

            getProjectResultTypes: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_result_type').then(function (response) {
                    var data = response.data;
                    self.projectResultTypes = data || [];
                })
            },
        },
        created: function () {
            this.getProjectStyles();
            this.getProjectSources();
            this.getProjectResultTypes();
            this.setProModelHsxmForm();

        }
    })


</script>
</body>
</html>
