<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>


<div id="app" v-show="pageLoad" class="container-fluid mgb-60 pro-task" style="display: none">
    <edit-bar second-name="分配专家审核任务"></edit-bar>
    <el-form :model="assignRuleForm" :disabled="disabled" size="mini" autocomplete="off" ref="assignRuleForm" class="mgt-20"
             :rules="assignRuleFormRules" :show-message="false">
        <el-form-item label="专家任务" label-width="120px">
            {{proName}}（{{gNodeName}}）
        </el-form-item>
        <el-form-item label="分配项目规则" label-width="120px">
            <el-form-item prop="isAssignNum" style="margin-bottom: 0;">
                <el-radio-group v-model="assignRuleForm.isAssignNum" @change="changeIsAssignNum">
                    <el-radio label="1">每个项目分配给
                        <el-input v-model="assignRuleForm.assignNum" :disabled="assignNumDisable" style="width:80px;"
                                  @change="changeAssignNum"></el-input>
                        名专家审核
                    </el-radio>
                    <el-radio label="0">不限（<span class="red">所有专家审核所有项目</span>）</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form-item>
        <el-form-item label="分配任务方式" label-width="120px">
            <el-form-item prop="auditType" style="margin-bottom: 0;">
                <el-radio-group v-model="assignRuleForm.auditType" @change="changeAuditType">
                    <el-radio label="0">自动分配</el-radio>
                    <el-radio label="1">手动分配</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form-item>
        <el-form-item v-if="!showActrel" label="选择专家范围" label-width="120px" v-if="assignRuleForm.auditType == '0'">
            <el-form-item prop="auditRole" style="margin-bottom: 0;">
                <el-radio-group v-model="assignRuleForm.auditRole" class="multi-row">
                    <el-radio class="audit-role" v-for="item in expertFrom" :key="item.id" :label="item.value">
                        {{item.label}}
                    </el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form-item>
        <el-form-item label="选择分配规则" label-width="120px" v-if="assignRuleForm.auditType == '0'">
            <el-form-item prop="isAuditMaxNum" style="margin-bottom: 0;">
                <el-radio-group v-model="assignRuleForm.isAuditMaxNum" @change="changeIsAuditMaxNum">
                    <el-radio label="1">同一组专家不能审核相同的
                        <el-input v-model="assignRuleForm.auditMaxNum" :disabled="auditMaxNumDisable"
                                  style="width:80px;" @change="changeAuditMaxNum"></el-input>
                        个项目
                    </el-radio>
                    <el-radio label="0">不限</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form-item>


        <div class="conditions college-left" v-if="assignRuleForm.auditType == '1'">
            <e-condition label="所属学院" type="radio" v-model="searchListForm.officeId" :default-props="defaultProps"
                         name="officeId" :options="collegeList" @change="getDataList">
            </e-condition>
        </div>


        <div class="search-block_bar clearfix">
            <div class="search-btns">

                <el-button size="mini" type="primary" @click.stop.prevent="batchAssign"
                           v-if="assignRuleForm.auditType == '0'">分配
                </el-button>
                <el-tooltip v-show="assignRuleForm.auditType == '1'"
                            content="请选择要分配的项目"
                            :disabled="multipleSelection.length > 0"
                            popper-class="white"
                            placement="bottom">
                    <span>
                        <el-button size="mini"
                                   :disabled="multipleSelection.length === 0" type="primary"
                                   @click.stop="batchChooseExpert">选择专家
                             </el-button>
                        </span>
                </el-tooltip>
                <el-button size="mini" @click.stop="batchClearAssign">清除分配</el-button>

            </div>
            <div class="search-input">
                <el-form-item prop="isAuto" v-if="assignRuleForm.auditType == '0'"
                              style="margin-bottom: 0;display: inline-block;">
                    <el-checkbox v-model="assignRuleForm.isAuto" true-label="1" false-label="0" @change="changeIsAuto">
                        开启持续自动分配专家（<span
                            class="red">此功能与自动分配功能配合使用，开启后，一旦执行分配，此审核阶段未分配专家的项目可自动分配专家，此功能不能与手动分配同时执行</span>）
                    </el-checkbox>
                </el-form-item>

            </div>
        </div>
    </el-form>
    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="pageList" class="table" v-loading="loading"
                  @sort-change="handleTableSortChange" @selection-change="handleSelectionChange">
            <el-table-column v-if="assignRuleForm.auditType == '1'"
                             type="selection"
                             width="50">
            </el-table-column>
            <el-table-column prop="competition_number" label="项目信息" align="left" width="250"
                             sortable="competition_number">
                <template slot-scope="scope">
                    <div>
                        <el-tooltip :content="scope.row.competitionNumber" popper-class="white" placement="right"><span
                                class="break-ellipsis">{{scope.row.competitionNumber}}</span></el-tooltip>
                    </div>
                    <div>
                        <el-tooltip :content="scope.row.pName" popper-class="white" placement="right"><span
                                class="break-ellipsis">{{scope.row.pName}}</span></el-tooltip>
                    </div>
                    <div>
                        <el-tooltip :content="scope.row.officeName" popper-class="white" placement="right"><span
                                class="break-ellipsis">{{scope.row.officeName}}</span></el-tooltip>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" min-width="130">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>
            <el-table-column prop="proCategory" label="类别" align="center" min-width="100" sortable="proCategory">
                <template slot-scope="scope">
                    <span v-if="scope.row.proType == '1,'">{{scope.row.proCategory | selectedFilter(projectTypesEntries)}}</span>
                    <span v-if="scope.row.proType == '7,'">{{scope.row.proCategory | selectedFilter(competitionTypesEntries)}}</span>
                </template>
            </el-table-column>
            <el-table-column label="分配状态" align="center" min-width="150">
                <template slot-scope="scope">
                    <span v-if="scope.row.taskAssigns">已分配</span>
                    <span v-else>未分配</span>
                </template>
            </el-table-column>
            <el-table-column label="分配的专家" align="center" min-width="150">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.taskAssigns" popper-class="white" placement="right"><span
                            class="break-ellipsis">{{scope.row.taskAssigns}}</span></el-tooltip>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" min-width="150">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini"
                                   v-if="assignRuleForm.auditType == '1' && !scope.row.taskAssigns"
                                   @click.stop.prevent="singleChooseExpert(scope.row)">选择专家
                        </el-button>
                        <el-button type="text" size="mini"
                                   v-if="assignRuleForm.auditType == '1' && scope.row.taskAssigns"
                                   @click.stop.prevent="singleChangeExpert(scope.row)">更换专家
                        </el-button>

                        <el-button type="text" size="mini" :disabled="!scope.row.taskAssigns"
                                   @click.stop.prevent="singleClearAssign(scope.row)">清除分配
                        </el-button>
                    </div>
                </template>
            </el-table-column>

        </el-table>
        <div class="text-right mgb-20" v-show="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>

    <el-dialog title="选择专家" :visible.sync="dialogVisible" top="1vh" width="900px" :before-close="handleClose"
               class="user-share"
               :close-on-click-modal="false">
        <el-form :model="searchListDialogForm" size="mini" autocomplete="off" ref="searchListDialogForm">

            <div class="conditions">
                <e-condition label="所属学院" type="radio" v-model="searchListDialogForm.officeId"
                             :default-props="defaultProps"
                             name="officeId" :options="collegeList" @change="getExpertList">
                </e-condition>
                <e-condition label="专家来源" type="radio" v-model="searchListDialogForm.expertType"
                             name="expertType" :options="expertFrom.slice(1)" @change="getExpertList">
                </e-condition>
            </div>

            <div class="search-block_bar clearfix">
                <div class="search-btns">
                    <el-tooltip content="请选择专家" :disabled="multipleSelectedIdDialog.length > 0" popper-class="white"
                                placement="bottom">
                        <span>
                            <el-button size="mini" type="primary"
                                       :disabled="dialogFormDisabled || multipleSelectedIdDialog.length === 0" icon="iconfont icon-jiancharenwufenpei"
                                       @click.stop="saveDialog">分配
                            </el-button>
                        </span>
                    </el-tooltip>
                </div>
                <div class="search-input">
                    <input type="text" style="display: none">
                    <el-input
                            placeholder="工号/专家姓名"
                            size="mini"
                            name="queryStr"
                            v-model="searchListDialogForm.queryStr"
                            @keyup.enter.native="getExpertList"
                            class="w300">
                        <el-button slot="append" icon="el-icon-search"
                                   @click.stop.prevent="getExpertList"></el-button>
                    </el-input>
                </div>
            </div>
        </el-form>
        <div class="table-container">
            <el-table size="mini" :data="expertList" class="table" v-loading="expertLoading" ref="expertList"
                      max-height="466"
                      @sort-change="handleTableSortChangeDialog" @select="handleSelectionChangeDialog"
                      @select-all="handleSelectionAllDialog">
                <el-table-column
                        type="selection"
                        width="50">
                </el-table-column>
                <el-table-column prop="userNo" label="专家信息" align="left" width="200" sortable="userNo">
                    <template slot-scope="scope">
                        <div class="user-element">
                            <div class="user-img">
                                <img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                            </div>
                            <div class="user-detail">
                                <div class="user-name">
                                    <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                                        <span class="break-ellipsis">
                                            <img src="${ctxImages}/user-name.png" alt="">
                                            {{scope.row.user.name}}
                                        </span>
                                    </el-tooltip>
                                </div>
                                <div class="user-tag">
                                    <el-tooltip :content="scope.row.user.no" popper-class="white" placement="right">
                                        <span class="break-ellipsis">
                                            <img src="${ctxImages}/user-no.png" alt="">
                                            {{scope.row.user.no}}
                                        </span>
                                    </el-tooltip>
                                </div>
                                <div class="user-phone">
                                    <el-tooltip :content="scope.row.user.officeName" popper-class="white"
                                                placement="right">
                                        <span class="break-ellipsis">
                                            <i class="iconfont icon-dibiao2"
                                               style="width:20px;vertical-align: bottom;display: inline-block;text-align: center;"></i>
                                            {{scope.row.user.officeName}}
                                        </span>
                                    </el-tooltip>
                                </div>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="todoNum" label="待审核项目数" align="center" min-width="120">
                    <template slot-scope="scope">
                        <expert-col-current-assign :row="scope.row" :actyw-id="assignRuleForm.actywId"
                                                   :gnode-id="assignRuleForm.gnodeId"
                                                   v-if="!scope.row.todoNum"
                                                   @change="handleChangeRow"></expert-col-current-assign>
                        <span v-else>{{scope.row.todoNum}}</span>
                    </template>
                </el-table-column>
                <%--<el-table-column prop="teachertype" label="专家来源" align="center" min-width="100" sortable="teachertype">--%>
                <%--<template slot-scope="scope">--%>
                <%--{{scope.row.teachertype | selectedFilter(expertSourcesEntries)}}--%>
                <%--</template>--%>
                <%--</el-table-column>--%>
                <el-table-column prop="expertType" label="专家来源" align="center" min-width="100">
                    <template slot-scope="scope">
                        {{scope.row.expertType | checkboxFilter(originalExpertFromEntries)}}
                    </template>
                </el-table-column>
            </el-table>
            <div class="text-right mgb-20" v-show="pageCountDialog">
                <div class="warm-tip">温馨提示：可分页选择</div>
                <el-pagination style="display: inline-block"
                               size="small"
                               @size-change="handlePaginationSizeChangeDialog"
                               background
                               @current-change="handlePaginationPageChangeDialog"
                               :current-page.sync="searchListDialogForm.pageNo"
                               :page-sizes="[5,10,20,50,100]"
                               :page-size="searchListDialogForm.pageSize"
                               layout="total,prev, pager, next, sizes"
                               :total="pageCountDialog">
                </el-pagination>
            </div>
        </div>
        <span slot="footer" class="dialog-footer">
            <%--<el-button size="mini" @click.stop.prevent="handleClose">取消</el-button>--%>
        </span>
    </el-dialog>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var proName = '${proName}';
            var gNodeName = '${gnodeName}';
            var actYwEtAssignRule = JSON.parse('${fns:toJson(actYwEtAssignRule)}');
            var isAssignNum = '1';
            var isAuditMaxNum = '1';
            var assignNumDisable = false;
            var auditMaxNumDisable = false;

            if (actYwEtAssignRule.auditUserNum == '-1') {
                isAssignNum = '0';
                assignNumDisable = true;
            }
            if (actYwEtAssignRule.auditMax == '-1') {
                isAuditMaxNum = '0';
                auditMaxNumDisable = true;
            }
            return {
                officeList: [],
                proName: proName,
                gNodeName: gNodeName,
                projectTypes: [],
                competitionTypes: [],
                expertSources: [],
                expertSourceForm: [],
                assignRuleForm: {
                    id: actYwEtAssignRule.id || '',
                    actywId: actYwEtAssignRule.actywId || '',
                    gnodeId: actYwEtAssignRule.gnodeId || '',
                    auditUserNum: actYwEtAssignRule.auditUserNum || '3',    //3
                    auditType: actYwEtAssignRule.auditType || '0',        //0
                    auditRole: actYwEtAssignRule.auditRole || '0',        //0
                    auditMax: actYwEtAssignRule.auditMax || '5',        //5
                    isAuto: actYwEtAssignRule.isAuto || '0',          //0

                    isAssignNum: isAssignNum,     //1
                    assignNum: actYwEtAssignRule.auditUserNum != '-1' ? actYwEtAssignRule.auditUserNum || '3' : '',       //3
                    isAuditMaxNum: isAuditMaxNum,   //1
                    auditMaxNum: actYwEtAssignRule.auditMax != '-1' ? actYwEtAssignRule.auditMax || '5' : ''      //5
                },
                originalIsAuto: actYwEtAssignRule.isAuto || '0',
                assignNumDisable: assignNumDisable,
                auditMaxNumDisable: auditMaxNumDisable,
                pageList: [],
                pageCount: 0,
                disabled: true,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    id: actYwEtAssignRule.id || '',
                    actywId: actYwEtAssignRule.actywId || '',
                    gnodeId: actYwEtAssignRule.gnodeId || '',
                    officeId: ''
                },
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                loading: false,
                expertLoading: false,
                message: '${message}',
                multipleSelection: [],
                multipleSelectedId: [],
                singleSelectedId: '',
                singleAlreadyChooseName: [],

//             手动分配   选择专家  弹框
                dialogVisible: false,
                dialogFormDisabled: false,
                searchListDialogForm: {
                    pageNo: 1,
                    pageSize: 5,
                    orderBy: '',
                    orderByType: '',
                    queryStr: '',
                    officeId: '',
                    expertType: ''
                },
                originalExpertList: [],
                expertList: [],
                pageCountDialog: 0,
                multipleSelectionDialog: [],
                multipleSelectedIdDialog: []

            }
        },
        computed: {
            collegeList: function () {
                return this.officeList.filter(function (item) {
                    return item.grade == '2';
                })
            },
            expertFrom: function () {
                return [{label: '所有专家', value: '0', id: '111'}].concat(this.expertSourceForm)
            },
            projectTypesEntries: function () {
                return this.getEntries(this.projectTypes)
            },
            competitionTypesEntries: function () {
                return this.getEntries(this.competitionTypes)
            },
            expertSourcesEntries: function () {
                return this.getEntries(this.expertSources);
            },
            originalExpertFromEntries: function () {
                return this.getEntries(this.expertFrom);
            },
            assignRuleFormRules: {
                get: function () {

                    return {
                        isAssignNum: [
                            {required: true, message: '分配项目规则必选', trigger: 'change'}
                        ],
                        auditType: [
                            {required: true, message: '分配任务方式必选', trigger: 'change'}
                        ],
                        auditRole: [
                            {required: true, message: '选择专家范围必选', trigger: 'change'}
                        ],
                        isAuditMaxNum: [
                            {required: true, message: '选择分配规则必选', trigger: 'change'}
                        ]
                    }
                }
            }
        },
        methods: {
            handleChangeRow: function (scope) {
                scope.row.todoNum = scope.data;
            },
            getReg: function (value) {
                var reg = /^[1-9][0-9]*$/;
                if (!reg.test(value)) {
                    return '必须为正整数';
                } else if (value > 999999) {
                    return '不得超过6位数';
                }
                return '';
            },
            changeIsAssignNum: function (value) {
                this.assignNumDisable = false;
                this.assignRuleForm.auditUserNum = '';
                this.assignRuleForm.assignNum = '';
                if (value == '0') {
                    this.assignRuleForm.auditUserNum = '-1';
                    this.assignNumDisable = true;
                }
            },
            changeAssignNum: function (value) {
                this.assignRuleForm.auditUserNum = value;
            },
            changeIsAuditMaxNum: function (value) {
                this.auditMaxNumDisable = false;
                this.assignRuleForm.auditMax = '';
                this.assignRuleForm.auditMaxNum = '';
                if (value == '0') {
                    this.assignRuleForm.auditMax = '-1';
                    this.auditMaxNumDisable = true;
                }
            },
            changeAuditMaxNum: function (value) {
                this.assignRuleForm.auditMax = value;
            },
            changeAuditType: function () {
                if (this.searchListForm.officeId == '') {
                    return false;
                }
                this.searchListForm.officeId = '';
                this.getDataList();
            },
            getDataList: function () {
                var self = this;
                var url = this.showActrel ? '/actyw/actywAssignRule/ajaxProvGetProAssginList' : '/actyw/actywAssignRule/ajaxGetProAssginList';
                this.loading = true;
                this.$axios.get(url,{params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.pageCount = data.count;
                        self.searchListForm.pageSize = data.pageSize;
                        self.pageList = data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },
            getPwEnterTeamInfo: function (row) {
                return {
                    applicantName: row.deuser.name,
                    snames: row.team.entName,
                    tnames: row.team.uName
                }
            },
            getValidateFormMsg: function () {
                var tip = [];
                this.$refs.assignRuleForm.validate(function (valid, obj) {
                    if (!valid) {
                        for (var key in obj) {
                            if (obj.hasOwnProperty(key)) {
                                tip.push(obj[key][0].message);
                            }
                        }
                    }
                });
                return tip;
            },
            getValidateFirstInputMsg: function () {
                var tip = [];
                if (this.assignRuleForm.isAssignNum == '1') {
                    if (this.assignRuleForm.auditUserNum == '') {
                        tip.push('专家审核人数必填');
                    } else {
                        var msg = this.getReg(this.assignRuleForm.auditUserNum);
                        if (msg != '') {
                            tip.push('专家审核人数' + msg);
                        }
                    }
                }
                return tip;
            },
            getValidateSecondInputMsg: function () {
                var tip = [];
                if (this.assignRuleForm.isAuditMaxNum == '1') {
                    if (this.assignRuleForm.auditMax == '') {
                        tip.push('不能审核相同项目数必填');
                    } else {
                        var msg = this.getReg(this.assignRuleForm.auditMax);
                        if (msg != '') {
                            tip.push('不能审核相同项目数' + msg);
                        } else if (this.assignRuleForm.auditMax < 2) {
                            tip.push('不能审核相同项目数必须大于1');
                        }
                    }
                }
                return tip;
            },
            getAutoValidate: function () {
                var errorTip = [], inputErrorTip = [], validateTip = [];
                errorTip = this.getValidateFormMsg();
                inputErrorTip = inputErrorTip.concat(this.getValidateFirstInputMsg(), this.getValidateSecondInputMsg());
                validateTip = errorTip.concat(inputErrorTip);
                if (validateTip.length > 0) {
                    this.$confirm(validateTip.join('、'), '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {

                    }).catch(function () {

                    });
                    return false;
                }
                return true;
            },

            batchAssign: function () {
                var self = this, tip = '', sendData = {};

                if (this.pageCount == 0 && this.assignRuleForm.isAuto == '0') {
                    this.$message({
                        message: '没有可分配的项目，建议与“开启持续自动分配专家”配合使用！',
                        type: 'warning'
                    });
                    return false;
                }

                if (!this.getAutoValidate()) {
                    return false;
                }

                this.$confirm('所有项目将统一按照以上规则分配专家，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.assignTaskAxios();
                }).catch(function () {

                })
            },

            assignTaskAxios: function () {
                var self = this;
                var loading = this.$loading({
                    lock: true,
                    text: '分配中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)',
                    customClass: 'gray-green-loading'
                });
                var assignRuleForm = JSON.parse(JSON.stringify(this.assignRuleForm));
                var url = this.showActrel ? '/actyw/actywAssignRule/ajaxProvAssginTask' : '/actyw/actywAssignRule/ajaxAssginTask'
                assignRuleForm.proIdList = this.multipleSelectedId;
                this.$axios.post(url, assignRuleForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.assignRuleForm.id = data.id;
                        self.getDataList();
                        self.$alert('分配专家成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    loading.close();
                }).catch(function (error) {
                    loading.close();
                })
            },

            changeIsAuto: function () {
                var self = this, sendData = {};
                if (!this.getAutoValidate()) {
                    this.assignRuleForm.isAuto = this.originalIsAuto;
                    return false;
                }
                var assignRuleForm = JSON.parse(JSON.stringify(this.assignRuleForm))
                assignRuleForm.proIdList = this.multipleSelectedId;

                this.$axios.post('/actyw/actywAssignRule/ajaxSaveAuto', assignRuleForm).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.assignRuleForm.id = data.data.id;
                    }
                })
            },
            getHandValidate: function () {
                var errorTip = [], inputErrorTip = [], validateTip = [];
                if (this.pageCount == 0 && this.assignRuleForm.auditType == '1') {
                    this.$message({
                        message: '没有可分配的项目，无法手动分配专家，建议自动分配！',
                        type: 'warning'
                    });
                    return false;
                }
                errorTip = this.getValidateFormMsg();
                inputErrorTip = inputErrorTip.concat(this.getValidateFirstInputMsg());
                validateTip = errorTip.concat(inputErrorTip);
                if (validateTip.length > 0) {
                    this.$confirm(validateTip.join('、'), '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {

                    }).catch(function () {

                    });
                    return false;
                }
                return true;
            },
            batchChooseExpert: function () {
                if (!this.getHandValidate()) {
                    return false;
                }
                if (this.searchListForm.officeId) {
                    this.searchListDialogForm.officeId = this.searchListForm.officeId;
                }
                this.dialogVisible = true;
                this.getExpertList();
            },
            singleChooseExpert: function (row) {
                if (!this.getHandValidate()) {
                    return false;
                }
                this.singleSelectedId = this.showActrel ? row.provId : row.id;
                this.singleAlreadyChooseName = row.taskAssigns ? row.taskAssigns.split('、') : [];
                if (this.searchListForm.officeId) {
                    this.searchListDialogForm.officeId = this.searchListForm.officeId;
                }
                this.dialogVisible = true;
                this.getExpertList();
            },
            singleChangeExpert: function (row) {
                if (!this.getHandValidate()) {
                    return false;
                }
                this.singleSelectedId = this.showActrel ? row.provId : row.id;
                this.singleAlreadyChooseName = row.taskAssigns ? row.taskAssigns.split('、') : [];
                if (this.searchListForm.officeId) {
                    this.searchListDialogForm.officeId = this.searchListForm.officeId;
                }
                this.dialogVisible = true;
                this.getExpertList();
            },
            handleClose: function () {
                this.$refs.expertList.clearSort();
                this.dialogVisible = false;
                this.singleSelectedId = '';
                this.singleAlreadyChooseName = [];
                this.multipleSelectedIdDialog = [];
                this.multipleSelectionDialog = [];
                this.searchListDialogForm = {
                    pageNo: 1,
                    pageSize: 5,
                    orderBy: '',
                    orderByType: '',
                    queryStr: '',
                    officeId: '',
                    expertType: ''
                }
            },
            saveDialog: function () {
                var self = this, ids = [this.singleSelectedId], sendData = {};
                if (this.multipleSelectedIdDialog.length != this.assignRuleForm.auditUserNum && this.assignRuleForm.auditUserNum != '-1') {
                    this.$alert('根据设定的分配项目规则，每个项目分配给' + this.assignRuleForm.auditUserNum + '名专家审核，当前所选人数不相等，请重新选择专家！', '提示', {
                        type: 'warning'
                    });
                    return false;
                }
                this.manuallyAssginAxios();

            },

            manuallyAssginAxios: function () {
                var self = this;
                var loading = this.$loading({
                    lock: true,
                    text: '分配中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)',
                    customClass: 'gray-green-loading'
                });
                var singleSelectedId = this.singleSelectedId;
                var proListIds = this.multipleSelection.map(function (item) {
                    return self.showActrel ? item.provId : item.id;
                })
                var url = this.showActrel ? '/actyw/actywAssignRule/ajaxGetProvManuallyAssgin' : '/actyw/actywAssignRule/ajaxGetManuallyAssgin';
                var assignRuleForm = JSON.parse(JSON.stringify(this.assignRuleForm));
                assignRuleForm.userIdList = this.multipleSelectedIdDialog;
                assignRuleForm.proIdList = singleSelectedId ? [singleSelectedId] : proListIds;
                this.$axios.post(url, assignRuleForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.assignRuleForm.id = data.id;
                        self.getDataList();
                        self.handleClose();
                        self.$alert('分配专家成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    loading.close();
                }).catch(function (error) {
                    loading.close();
                })
            },


            getExpertList: function () {
                var self = this;
                this.expertLoading = true;
                var params = {
                    queryStr: this.searchListDialogForm.queryStr,
                    officeId: this.searchListDialogForm.officeId,
                    expertType: this.searchListDialogForm.expertType
                };
                this.$axios({
                    method: 'POST',
                    url: '/actyw/actywAssignRule/ajaxGetManuallyList',
                    params: params
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        var list = data.data || [];
                        self.pageCountDialog = data.data ? data.data.length : 0;
                        if (list.length > 0) {
                            list.forEach(function (item) {
                                item.todoNum = '';
                                item.userNo = item.user.no;
                                if (self.singleAlreadyChooseName.indexOf(item.user.name) > -1 && self.multipleSelectedIdDialog.indexOf(item.user.id) == -1) {
                                    self.multipleSelectedIdDialog.push(item.user.id);
                                }
                            });
                        }
                        self.originalExpertList = list || [];
                        self.sortList();
                    }
                    self.expertLoading = false;
                }).catch(function (error) {
                    self.expertLoading = false;
                })
            },
            getCurrentPageList: function () {
                var list = [], startIndex = this.searchListDialogForm.pageNo * this.searchListDialogForm.pageSize;
                list = this.originalExpertList.slice(startIndex - this.searchListDialogForm.pageSize, startIndex);
                return list;
            },
            showCheck: function () {
                var self = this;
                self.$nextTick(function () {
                    self.originalExpertList.forEach(function (item) {
                        if (self.multipleSelectedIdDialog.indexOf(item.user.id) > -1) {
                            self.$refs.expertList.toggleRowSelection(item);
                        }
                    })
                });
            },
            handleSelectionChangeDialog: function (value) {
                this.multipleSelectionDialog = value;
                this.multipleSelectedIdDialog = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedIdDialog.push(value[i].user.id);
                }
            },
            handleSelectionAllDialog: function (value) {
                this.multipleSelectionDialog = value;
                this.multipleSelectedIdDialog = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedIdDialog.push(value[i].user.id);
                }
            },
            handleTableSortChangeDialog: function (row) {
                this.searchListDialogForm.orderBy = row.prop;
                this.searchListDialogForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.sortList();
            },
            sortList: function () {
                if (this.searchListDialogForm.orderByType == 'asc') {
                    this.originalExpertList = this.compareArray(this.originalExpertList, this.searchListDialogForm.orderBy, true);
                } else {
                    this.originalExpertList = this.compareArray(this.originalExpertList, this.searchListDialogForm.orderBy, false);
                }
                this.expertList = this.getCurrentPageList();
                this.showCheck();
            },
            handlePaginationSizeChangeDialog: function (value) {
                this.searchListDialogForm.pageSize = value;
                this.expertList = this.getCurrentPageList();
                this.showCheck();
            },
            handlePaginationPageChangeDialog: function (value) {
                this.searchListDialogForm.pageNo = value;
                this.expertList = this.getCurrentPageList();
                this.showCheck();
            },
            compareArray: function (arr, property, sequence) {
                var self = this;
                for (var j = 0; j < arr.length - 1; j += 1) {
                    for (var i = 0; i < arr.length - 1 - j; i += 1) {
                        var before = self.compareString(arr[i][property], arr[i + 1][property]);
                        if (before === 1) {
                            var temp = arr[i];
                            arr[i] = arr[i + 1];
                            arr[i + 1] = temp;
                        }
                    }
                }
                if (sequence) {
                    arr.reverse();
                }
                return arr;
            },
            compareString: function (str1, str2) {
                var self = this;
                var str1Length = str1 ? str1.length : 0;
                var str2Length = str2 ? str2.length : 0;
                var StringLength = Math.min(str1Length, str2Length);
                var StringCode = '';
                var code = 0;
                for (var i = 0; i < StringLength; i += 1) {
                    StringCode = self.compareCharacter(str1[i], str2[i]);
                    if (StringCode === -1) {
                        code = -1;
                        break;
                    } else if (StringCode === 1) {
                        code = 1;
                        break;
                    } else {
                        code = 0;
                    }
                }
                if (code === 0 && str1Length !== str2Length) {
                    code = str1Length - str2Length <= -1 ? 1 : -1;
                }
                return code;
            },
            compareCharacter: function (Letter1, Letter2) {
                var Letter1TypeNum = this.DetermineType(Letter1);
                var Letter2TypeNum = this.DetermineType(Letter2);
                var before = '';
                var after = '';
                var code = '';
                if (Letter1TypeNum !== Letter2TypeNum) {
                    code = Letter1TypeNum < Letter2TypeNum;
                    return code ? 1 : -1
                } else {
                    if (Letter1TypeNum === 0) {
                        return code = 0;
                    }

                    if (Letter1TypeNum === 1) {
                        before = Letter1;
                        after = Letter2;
                    }
                    if (Letter1TypeNum === 2) {
                        before = Letter1.toLocaleLowerCase().charCodeAt(0);
                        after = Letter2.toLocaleLowerCase().charCodeAt(0);
                    }
                    if (Letter1TypeNum === 3) {
                        before = this.getFirstLetter(Letter1).toLocaleLowerCase().charCodeAt(0);
                        after = this.getFirstLetter(Letter2).toLocaleLowerCase().charCodeAt(0);
                    }
                }
                if (before > after) {
                    code = -1;
                } else if (before === after) {
                    code = 0;
                } else {
                    code = 1;
                }

                return code;
            },
            DetermineType: function (Letter) {
                var isNum = !isNaN(Letter);
                var isLetter = /[_a-zA-Z]/.test(Letter);
                var isChinese = !/[^\u4E00-\u9FA5]/.test(Letter);
                var typeNum = '';
                if (isNum) {
                    typeNum = 1;
                } else if (isLetter) {
                    typeNum = 2;
                } else if (isChinese) {
                    typeNum = 3;
                } else {
                    typeNum = 0;
                }
                return typeNum;
            },
            getFirstLetter: function (str) {
                var pinyin_dict_firstletter = {};
                pinyin_dict_firstletter.all = "YDYQSXMWZSSXJBYMGCCZQPSSQBYCDSCDQLDYLYBSSJGYZZJJFKCCLZDHWDWZJLJPFYYNWJJTMYHZWZHFLZPPQHGSCYYYNJQYXXGJHHSDSJNKKTMOMLCRXYPSNQSECCQZGGLLYJLMYZZSECYKYYHQWJSSGGYXYZYJWWKDJHYCHMYXJTLXJYQBYXZLDWRDJRWYSRLDZJPCBZJJBRCFTLECZSTZFXXZHTRQHYBDLYCZSSYMMRFMYQZPWWJJYFCRWFDFZQPYDDWYXKYJAWJFFXYPSFTZYHHYZYSWCJYXSCLCXXWZZXNBGNNXBXLZSZSBSGPYSYZDHMDZBQBZCWDZZYYTZHBTSYYBZGNTNXQYWQSKBPHHLXGYBFMJEBJHHGQTJCYSXSTKZHLYCKGLYSMZXYALMELDCCXGZYRJXSDLTYZCQKCNNJWHJTZZCQLJSTSTBNXBTYXCEQXGKWJYFLZQLYHYXSPSFXLMPBYSXXXYDJCZYLLLSJXFHJXPJBTFFYABYXBHZZBJYZLWLCZGGBTSSMDTJZXPTHYQTGLJSCQFZKJZJQNLZWLSLHDZBWJNCJZYZSQQYCQYRZCJJWYBRTWPYFTWEXCSKDZCTBZHYZZYYJXZCFFZZMJYXXSDZZOTTBZLQWFCKSZSXFYRLNYJMBDTHJXSQQCCSBXYYTSYFBXDZTGBCNSLCYZZPSAZYZZSCJCSHZQYDXLBPJLLMQXTYDZXSQJTZPXLCGLQTZWJBHCTSYJSFXYEJJTLBGXSXJMYJQQPFZASYJNTYDJXKJCDJSZCBARTDCLYJQMWNQNCLLLKBYBZZSYHQQLTWLCCXTXLLZNTYLNEWYZYXCZXXGRKRMTCNDNJTSYYSSDQDGHSDBJGHRWRQLYBGLXHLGTGXBQJDZPYJSJYJCTMRNYMGRZJCZGJMZMGXMPRYXKJNYMSGMZJYMKMFXMLDTGFBHCJHKYLPFMDXLQJJSMTQGZSJLQDLDGJYCALCMZCSDJLLNXDJFFFFJCZFMZFFPFKHKGDPSXKTACJDHHZDDCRRCFQYJKQCCWJDXHWJLYLLZGCFCQDSMLZPBJJPLSBCJGGDCKKDEZSQCCKJGCGKDJTJDLZYCXKLQSCGJCLTFPCQCZGWPJDQYZJJBYJHSJDZWGFSJGZKQCCZLLPSPKJGQJHZZLJPLGJGJJTHJJYJZCZMLZLYQBGJWMLJKXZDZNJQSYZMLJLLJKYWXMKJLHSKJGBMCLYYMKXJQLBMLLKMDXXKWYXYSLMLPSJQQJQXYXFJTJDXMXXLLCXQBSYJBGWYMBGGBCYXPJYGPEPFGDJGBHBNSQJYZJKJKHXQFGQZKFHYGKHDKLLSDJQXPQYKYBNQSXQNSZSWHBSXWHXWBZZXDMNSJBSBKBBZKLYLXGWXDRWYQZMYWSJQLCJXXJXKJEQXSCYETLZHLYYYSDZPAQYZCMTLSHTZCFYZYXYLJSDCJQAGYSLCQLYYYSHMRQQKLDXZSCSSSYDYCJYSFSJBFRSSZQSBXXPXJYSDRCKGJLGDKZJZBDKTCSYQPYHSTCLDJDHMXMCGXYZHJDDTMHLTXZXYLYMOHYJCLTYFBQQXPFBDFHHTKSQHZYYWCNXXCRWHOWGYJLEGWDQCWGFJYCSNTMYTOLBYGWQWESJPWNMLRYDZSZTXYQPZGCWXHNGPYXSHMYQJXZTDPPBFYHZHTJYFDZWKGKZBLDNTSXHQEEGZZYLZMMZYJZGXZXKHKSTXNXXWYLYAPSTHXDWHZYMPXAGKYDXBHNHXKDPJNMYHYLPMGOCSLNZHKXXLPZZLBMLSFBHHGYGYYGGBHSCYAQTYWLXTZQCEZYDQDQMMHTKLLSZHLSJZWFYHQSWSCWLQAZYNYTLSXTHAZNKZZSZZLAXXZWWCTGQQTDDYZTCCHYQZFLXPSLZYGPZSZNGLNDQTBDLXGTCTAJDKYWNSYZLJHHZZCWNYYZYWMHYCHHYXHJKZWSXHZYXLYSKQYSPSLYZWMYPPKBYGLKZHTYXAXQSYSHXASMCHKDSCRSWJPWXSGZJLWWSCHSJHSQNHCSEGNDAQTBAALZZMSSTDQJCJKTSCJAXPLGGXHHGXXZCXPDMMHLDGTYBYSJMXHMRCPXXJZCKZXSHMLQXXTTHXWZFKHCCZDYTCJYXQHLXDHYPJQXYLSYYDZOZJNYXQEZYSQYAYXWYPDGXDDXSPPYZNDLTWRHXYDXZZJHTCXMCZLHPYYYYMHZLLHNXMYLLLMDCPPXHMXDKYCYRDLTXJCHHZZXZLCCLYLNZSHZJZZLNNRLWHYQSNJHXYNTTTKYJPYCHHYEGKCTTWLGQRLGGTGTYGYHPYHYLQYQGCWYQKPYYYTTTTLHYHLLTYTTSPLKYZXGZWGPYDSSZZDQXSKCQNMJJZZBXYQMJRTFFBTKHZKBXLJJKDXJTLBWFZPPTKQTZTGPDGNTPJYFALQMKGXBDCLZFHZCLLLLADPMXDJHLCCLGYHDZFGYDDGCYYFGYDXKSSEBDHYKDKDKHNAXXYBPBYYHXZQGAFFQYJXDMLJCSQZLLPCHBSXGJYNDYBYQSPZWJLZKSDDTACTBXZDYZYPJZQSJNKKTKNJDJGYYPGTLFYQKASDNTCYHBLWDZHBBYDWJRYGKZYHEYYFJMSDTYFZJJHGCXPLXHLDWXXJKYTCYKSSSMTWCTTQZLPBSZDZWZXGZAGYKTYWXLHLSPBCLLOQMMZSSLCMBJCSZZKYDCZJGQQDSMCYTZQQLWZQZXSSFPTTFQMDDZDSHDTDWFHTDYZJYQJQKYPBDJYYXTLJHDRQXXXHAYDHRJLKLYTWHLLRLLRCXYLBWSRSZZSYMKZZHHKYHXKSMDSYDYCJPBZBSQLFCXXXNXKXWYWSDZYQOGGQMMYHCDZTTFJYYBGSTTTYBYKJDHKYXBELHTYPJQNFXFDYKZHQKZBYJTZBXHFDXKDASWTAWAJLDYJSFHBLDNNTNQJTJNCHXFJSRFWHZFMDRYJYJWZPDJKZYJYMPCYZNYNXFBYTFYFWYGDBNZZZDNYTXZEMMQBSQEHXFZMBMFLZZSRXYMJGSXWZJSPRYDJSJGXHJJGLJJYNZZJXHGXKYMLPYYYCXYTWQZSWHWLYRJLPXSLSXMFSWWKLCTNXNYNPSJSZHDZEPTXMYYWXYYSYWLXJQZQXZDCLEEELMCPJPCLWBXSQHFWWTFFJTNQJHJQDXHWLBYZNFJLALKYYJLDXHHYCSTYYWNRJYXYWTRMDRQHWQCMFJDYZMHMYYXJWMYZQZXTLMRSPWWCHAQBXYGZYPXYYRRCLMPYMGKSJSZYSRMYJSNXTPLNBAPPYPYLXYYZKYNLDZYJZCZNNLMZHHARQMPGWQTZMXXMLLHGDZXYHXKYXYCJMFFYYHJFSBSSQLXXNDYCANNMTCJCYPRRNYTYQNYYMBMSXNDLYLYSLJRLXYSXQMLLYZLZJJJKYZZCSFBZXXMSTBJGNXYZHLXNMCWSCYZYFZLXBRNNNYLBNRTGZQYSATSWRYHYJZMZDHZGZDWYBSSCSKXSYHYTXXGCQGXZZSHYXJSCRHMKKBXCZJYJYMKQHZJFNBHMQHYSNJNZYBKNQMCLGQHWLZNZSWXKHLJHYYBQLBFCDSXDLDSPFZPSKJYZWZXZDDXJSMMEGJSCSSMGCLXXKYYYLNYPWWWGYDKZJGGGZGGSYCKNJWNJPCXBJJTQTJWDSSPJXZXNZXUMELPXFSXTLLXCLJXJJLJZXCTPSWXLYDHLYQRWHSYCSQYYBYAYWJJJQFWQCQQCJQGXALDBZZYJGKGXPLTZYFXJLTPADKYQHPMATLCPDCKBMTXYBHKLENXDLEEGQDYMSAWHZMLJTWYGXLYQZLJEEYYBQQFFNLYXRDSCTGJGXYYNKLLYQKCCTLHJLQMKKZGCYYGLLLJDZGYDHZWXPYSJBZKDZGYZZHYWYFQYTYZSZYEZZLYMHJJHTSMQWYZLKYYWZCSRKQYTLTDXWCTYJKLWSQZWBDCQYNCJSRSZJLKCDCDTLZZZACQQZZDDXYPLXZBQJYLZLLLQDDZQJYJYJZYXNYYYNYJXKXDAZWYRDLJYYYRJLXLLDYXJCYWYWNQCCLDDNYYYNYCKCZHXXCCLGZQJGKWPPCQQJYSBZZXYJSQPXJPZBSBDSFNSFPZXHDWZTDWPPTFLZZBZDMYYPQJRSDZSQZSQXBDGCPZSWDWCSQZGMDHZXMWWFYBPDGPHTMJTHZSMMBGZMBZJCFZWFZBBZMQCFMBDMCJXLGPNJBBXGYHYYJGPTZGZMQBQTCGYXJXLWZKYDPDYMGCFTPFXYZTZXDZXTGKMTYBBCLBJASKYTSSQYYMSZXFJEWLXLLSZBQJJJAKLYLXLYCCTSXMCWFKKKBSXLLLLJYXTYLTJYYTDPJHNHNNKBYQNFQYYZBYYESSESSGDYHFHWTCJBSDZZTFDMXHCNJZYMQWSRYJDZJQPDQBBSTJGGFBKJBXTGQHNGWJXJGDLLTHZHHYYYYYYSXWTYYYCCBDBPYPZYCCZYJPZYWCBDLFWZCWJDXXHYHLHWZZXJTCZLCDPXUJCZZZLYXJJTXPHFXWPYWXZPTDZZBDZCYHJHMLXBQXSBYLRDTGJRRCTTTHYTCZWMXFYTWWZCWJWXJYWCSKYBZSCCTZQNHXNWXXKHKFHTSWOCCJYBCMPZZYKBNNZPBZHHZDLSYDDYTYFJPXYNGFXBYQXCBHXCPSXTYZDMKYSNXSXLHKMZXLYHDHKWHXXSSKQYHHCJYXGLHZXCSNHEKDTGZXQYPKDHEXTYKCNYMYYYPKQYYYKXZLTHJQTBYQHXBMYHSQCKWWYLLHCYYLNNEQXQWMCFBDCCMLJGGXDQKTLXKGNQCDGZJWYJJLYHHQTTTNWCHMXCXWHWSZJYDJCCDBQCDGDNYXZTHCQRXCBHZTQCBXWGQWYYBXHMBYMYQTYEXMQKYAQYRGYZSLFYKKQHYSSQYSHJGJCNXKZYCXSBXYXHYYLSTYCXQTHYSMGSCPMMGCCCCCMTZTASMGQZJHKLOSQYLSWTMXSYQKDZLJQQYPLSYCZTCQQPBBQJZCLPKHQZYYXXDTDDTSJCXFFLLCHQXMJLWCJCXTSPYCXNDTJSHJWXDQQJSKXYAMYLSJHMLALYKXCYYDMNMDQMXMCZNNCYBZKKYFLMCHCMLHXRCJJHSYLNMTJZGZGYWJXSRXCWJGJQHQZDQJDCJJZKJKGDZQGJJYJYLXZXXCDQHHHEYTMHLFSBDJSYYSHFYSTCZQLPBDRFRZTZYKYWHSZYQKWDQZRKMSYNBCRXQBJYFAZPZZEDZCJYWBCJWHYJBQSZYWRYSZPTDKZPFPBNZTKLQYHBBZPNPPTYZZYBQNYDCPJMMCYCQMCYFZZDCMNLFPBPLNGQJTBTTNJZPZBBZNJKLJQYLNBZQHKSJZNGGQSZZKYXSHPZSNBCGZKDDZQANZHJKDRTLZLSWJLJZLYWTJNDJZJHXYAYNCBGTZCSSQMNJPJYTYSWXZFKWJQTKHTZPLBHSNJZSYZBWZZZZLSYLSBJHDWWQPSLMMFBJDWAQYZTCJTBNNWZXQXCDSLQGDSDPDZHJTQQPSWLYYJZLGYXYZLCTCBJTKTYCZJTQKBSJLGMGZDMCSGPYNJZYQYYKNXRPWSZXMTNCSZZYXYBYHYZAXYWQCJTLLCKJJTJHGDXDXYQYZZBYWDLWQCGLZGJGQRQZCZSSBCRPCSKYDZNXJSQGXSSJMYDNSTZTPBDLTKZWXQWQTZEXNQCZGWEZKSSBYBRTSSSLCCGBPSZQSZLCCGLLLZXHZQTHCZMQGYZQZNMCOCSZJMMZSQPJYGQLJYJPPLDXRGZYXCCSXHSHGTZNLZWZKJCXTCFCJXLBMQBCZZWPQDNHXLJCTHYZLGYLNLSZZPCXDSCQQHJQKSXZPBAJYEMSMJTZDXLCJYRYYNWJBNGZZTMJXLTBSLYRZPYLSSCNXPHLLHYLLQQZQLXYMRSYCXZLMMCZLTZSDWTJJLLNZGGQXPFSKYGYGHBFZPDKMWGHCXMSGDXJMCJZDYCABXJDLNBCDQYGSKYDQTXDJJYXMSZQAZDZFSLQXYJSJZYLBTXXWXQQZBJZUFBBLYLWDSLJHXJYZJWTDJCZFQZQZZDZSXZZQLZCDZFJHYSPYMPQZMLPPLFFXJJNZZYLSJEYQZFPFZKSYWJJJHRDJZZXTXXGLGHYDXCSKYSWMMZCWYBAZBJKSHFHJCXMHFQHYXXYZFTSJYZFXYXPZLCHMZMBXHZZSXYFYMNCWDABAZLXKTCSHHXKXJJZJSTHYGXSXYYHHHJWXKZXSSBZZWHHHCWTZZZPJXSNXQQJGZYZYWLLCWXZFXXYXYHXMKYYSWSQMNLNAYCYSPMJKHWCQHYLAJJMZXHMMCNZHBHXCLXTJPLTXYJHDYYLTTXFSZHYXXSJBJYAYRSMXYPLCKDUYHLXRLNLLSTYZYYQYGYHHSCCSMZCTZQXKYQFPYYRPFFLKQUNTSZLLZMWWTCQQYZWTLLMLMPWMBZSSTZRBPDDTLQJJBXZCSRZQQYGWCSXFWZLXCCRSZDZMCYGGDZQSGTJSWLJMYMMZYHFBJDGYXCCPSHXNZCSBSJYJGJMPPWAFFYFNXHYZXZYLREMZGZCYZSSZDLLJCSQFNXZKPTXZGXJJGFMYYYSNBTYLBNLHPFZDCYFBMGQRRSSSZXYSGTZRNYDZZCDGPJAFJFZKNZBLCZSZPSGCYCJSZLMLRSZBZZLDLSLLYSXSQZQLYXZLSKKBRXBRBZCYCXZZZEEYFGKLZLYYHGZSGZLFJHGTGWKRAAJYZKZQTSSHJJXDCYZUYJLZYRZDQQHGJZXSSZBYKJPBFRTJXLLFQWJHYLQTYMBLPZDXTZYGBDHZZRBGXHWNJTJXLKSCFSMWLSDQYSJTXKZSCFWJLBXFTZLLJZLLQBLSQMQQCGCZFPBPHZCZJLPYYGGDTGWDCFCZQYYYQYSSCLXZSKLZZZGFFCQNWGLHQYZJJCZLQZZYJPJZZBPDCCMHJGXDQDGDLZQMFGPSYTSDYFWWDJZJYSXYYCZCYHZWPBYKXRYLYBHKJKSFXTZJMMCKHLLTNYYMSYXYZPYJQYCSYCWMTJJKQYRHLLQXPSGTLYYCLJSCPXJYZFNMLRGJJTYZBXYZMSJYJHHFZQMSYXRSZCWTLRTQZSSTKXGQKGSPTGCZNJSJCQCXHMXGGZTQYDJKZDLBZSXJLHYQGGGTHQSZPYHJHHGYYGKGGCWJZZYLCZLXQSFTGZSLLLMLJSKCTBLLZZSZMMNYTPZSXQHJCJYQXYZXZQZCPSHKZZYSXCDFGMWQRLLQXRFZTLYSTCTMJCXJJXHJNXTNRZTZFQYHQGLLGCXSZSJDJLJCYDSJTLNYXHSZXCGJZYQPYLFHDJSBPCCZHJJJQZJQDYBSSLLCMYTTMQTBHJQNNYGKYRQYQMZGCJKPDCGMYZHQLLSLLCLMHOLZGDYYFZSLJCQZLYLZQJESHNYLLJXGJXLYSYYYXNBZLJSSZCQQCJYLLZLTJYLLZLLBNYLGQCHXYYXOXCXQKYJXXXYKLXSXXYQXCYKQXQCSGYXXYQXYGYTQOHXHXPYXXXULCYEYCHZZCBWQBBWJQZSCSZSSLZYLKDESJZWMYMCYTSDSXXSCJPQQSQYLYYZYCMDJDZYWCBTJSYDJKCYDDJLBDJJSODZYSYXQQYXDHHGQQYQHDYXWGMMMAJDYBBBPPBCMUUPLJZSMTXERXJMHQNUTPJDCBSSMSSSTKJTSSMMTRCPLZSZMLQDSDMJMQPNQDXCFYNBFSDQXYXHYAYKQYDDLQYYYSSZBYDSLNTFQTZQPZMCHDHCZCWFDXTMYQSPHQYYXSRGJCWTJTZZQMGWJJTJHTQJBBHWZPXXHYQFXXQYWYYHYSCDYDHHQMNMTMWCPBSZPPZZGLMZFOLLCFWHMMSJZTTDHZZYFFYTZZGZYSKYJXQYJZQBHMBZZLYGHGFMSHPZFZSNCLPBQSNJXZSLXXFPMTYJYGBXLLDLXPZJYZJYHHZCYWHJYLSJEXFSZZYWXKZJLUYDTMLYMQJPWXYHXSKTQJEZRPXXZHHMHWQPWQLYJJQJJZSZCPHJLCHHNXJLQWZJHBMZYXBDHHYPZLHLHLGFWLCHYYTLHJXCJMSCPXSTKPNHQXSRTYXXTESYJCTLSSLSTDLLLWWYHDHRJZSFGXTSYCZYNYHTDHWJSLHTZDQDJZXXQHGYLTZPHCSQFCLNJTCLZPFSTPDYNYLGMJLLYCQHYSSHCHYLHQYQTMZYPBYWRFQYKQSYSLZDQJMPXYYSSRHZJNYWTQDFZBWWTWWRXCWHGYHXMKMYYYQMSMZHNGCEPMLQQMTCWCTMMPXJPJJHFXYYZSXZHTYBMSTSYJTTQQQYYLHYNPYQZLCYZHZWSMYLKFJXLWGXYPJYTYSYXYMZCKTTWLKSMZSYLMPWLZWXWQZSSAQSYXYRHSSNTSRAPXCPWCMGDXHXZDZYFJHGZTTSBJHGYZSZYSMYCLLLXBTYXHBBZJKSSDMALXHYCFYGMQYPJYCQXJLLLJGSLZGQLYCJCCZOTYXMTMTTLLWTGPXYMZMKLPSZZZXHKQYSXCTYJZYHXSHYXZKXLZWPSQPYHJWPJPWXQQYLXSDHMRSLZZYZWTTCYXYSZZSHBSCCSTPLWSSCJCHNLCGCHSSPHYLHFHHXJSXYLLNYLSZDHZXYLSXLWZYKCLDYAXZCMDDYSPJTQJZLNWQPSSSWCTSTSZLBLNXSMNYYMJQBQHRZWTYYDCHQLXKPZWBGQYBKFCMZWPZLLYYLSZYDWHXPSBCMLJBSCGBHXLQHYRLJXYSWXWXZSLDFHLSLYNJLZYFLYJYCDRJLFSYZFSLLCQYQFGJYHYXZLYLMSTDJCYHBZLLNWLXXYGYYHSMGDHXXHHLZZJZXCZZZCYQZFNGWPYLCPKPYYPMCLQKDGXZGGWQBDXZZKZFBXXLZXJTPJPTTBYTSZZDWSLCHZHSLTYXHQLHYXXXYYZYSWTXZKHLXZXZPYHGCHKCFSYHUTJRLXFJXPTZTWHPLYXFCRHXSHXKYXXYHZQDXQWULHYHMJTBFLKHTXCWHJFWJCFPQRYQXCYYYQYGRPYWSGSUNGWCHKZDXYFLXXHJJBYZWTSXXNCYJJYMSWZJQRMHXZWFQSYLZJZGBHYNSLBGTTCSYBYXXWXYHXYYXNSQYXMQYWRGYQLXBBZLJSYLPSYTJZYHYZAWLRORJMKSCZJXXXYXCHDYXRYXXJDTSQFXLYLTSFFYXLMTYJMJUYYYXLTZCSXQZQHZXLYYXZHDNBRXXXJCTYHLBRLMBRLLAXKYLLLJLYXXLYCRYLCJTGJCMTLZLLCYZZPZPCYAWHJJFYBDYYZSMPCKZDQYQPBPCJPDCYZMDPBCYYDYCNNPLMTMLRMFMMGWYZBSJGYGSMZQQQZTXMKQWGXLLPJGZBQCDJJJFPKJKCXBLJMSWMDTQJXLDLPPBXCWRCQFBFQJCZAHZGMYKPHYYHZYKNDKZMBPJYXPXYHLFPNYYGXJDBKXNXHJMZJXSTRSTLDXSKZYSYBZXJLXYSLBZYSLHXJPFXPQNBYLLJQKYGZMCYZZYMCCSLCLHZFWFWYXZMWSXTYNXJHPYYMCYSPMHYSMYDYSHQYZCHMJJMZCAAGCFJBBHPLYZYLXXSDJGXDHKXXTXXNBHRMLYJSLTXMRHNLXQJXYZLLYSWQGDLBJHDCGJYQYCMHWFMJYBMBYJYJWYMDPWHXQLDYGPDFXXBCGJSPCKRSSYZJMSLBZZJFLJJJLGXZGYXYXLSZQYXBEXYXHGCXBPLDYHWETTWWCJMBTXCHXYQXLLXFLYXLLJLSSFWDPZSMYJCLMWYTCZPCHQEKCQBWLCQYDPLQPPQZQFJQDJHYMMCXTXDRMJWRHXCJZYLQXDYYNHYYHRSLSRSYWWZJYMTLTLLGTQCJZYABTCKZCJYCCQLJZQXALMZYHYWLWDXZXQDLLQSHGPJFJLJHJABCQZDJGTKHSSTCYJLPSWZLXZXRWGLDLZRLZXTGSLLLLZLYXXWGDZYGBDPHZPBRLWSXQBPFDWOFMWHLYPCBJCCLDMBZPBZZLCYQXLDOMZBLZWPDWYYGDSTTHCSQSCCRSSSYSLFYBFNTYJSZDFNDPDHDZZMBBLSLCMYFFGTJJQWFTMTPJWFNLBZCMMJTGBDZLQLPYFHYYMJYLSDCHDZJWJCCTLJCLDTLJJCPDDSQDSSZYBNDBJLGGJZXSXNLYCYBJXQYCBYLZCFZPPGKCXZDZFZTJJFJSJXZBNZYJQTTYJYHTYCZHYMDJXTTMPXSPLZCDWSLSHXYPZGTFMLCJTYCBPMGDKWYCYZCDSZZYHFLYCTYGWHKJYYLSJCXGYWJCBLLCSNDDBTZBSCLYZCZZSSQDLLMQYYHFSLQLLXFTYHABXGWNYWYYPLLSDLDLLBJCYXJZMLHLJDXYYQYTDLLLBUGBFDFBBQJZZMDPJHGCLGMJJPGAEHHBWCQXAXHHHZCHXYPHJAXHLPHJPGPZJQCQZGJJZZUZDMQYYBZZPHYHYBWHAZYJHYKFGDPFQSDLZMLJXKXGALXZDAGLMDGXMWZQYXXDXXPFDMMSSYMPFMDMMKXKSYZYSHDZKXSYSMMZZZMSYDNZZCZXFPLSTMZDNMXCKJMZTYYMZMZZMSXHHDCZJEMXXKLJSTLWLSQLYJZLLZJSSDPPMHNLZJCZYHMXXHGZCJMDHXTKGRMXFWMCGMWKDTKSXQMMMFZZYDKMSCLCMPCGMHSPXQPZDSSLCXKYXTWLWJYAHZJGZQMCSNXYYMMPMLKJXMHLMLQMXCTKZMJQYSZJSYSZHSYJZJCDAJZYBSDQJZGWZQQXFKDMSDJLFWEHKZQKJPEYPZYSZCDWYJFFMZZYLTTDZZEFMZLBNPPLPLPEPSZALLTYLKCKQZKGENQLWAGYXYDPXLHSXQQWQCQXQCLHYXXMLYCCWLYMQYSKGCHLCJNSZKPYZKCQZQLJPDMDZHLASXLBYDWQLWDNBQCRYDDZTJYBKBWSZDXDTNPJDTCTQDFXQQMGNXECLTTBKPWSLCTYQLPWYZZKLPYGZCQQPLLKCCYLPQMZCZQCLJSLQZDJXLDDHPZQDLJJXZQDXYZQKZLJCYQDYJPPYPQYKJYRMPCBYMCXKLLZLLFQPYLLLMBSGLCYSSLRSYSQTMXYXZQZFDZUYSYZTFFMZZSMZQHZSSCCMLYXWTPZGXZJGZGSJSGKDDHTQGGZLLBJDZLCBCHYXYZHZFYWXYZYMSDBZZYJGTSMTFXQYXQSTDGSLNXDLRYZZLRYYLXQHTXSRTZNGZXBNQQZFMYKMZJBZYMKBPNLYZPBLMCNQYZZZSJZHJCTZKHYZZJRDYZHNPXGLFZTLKGJTCTSSYLLGZRZBBQZZKLPKLCZYSSUYXBJFPNJZZXCDWXZYJXZZDJJKGGRSRJKMSMZJLSJYWQSKYHQJSXPJZZZLSNSHRNYPZTWCHKLPSRZLZXYJQXQKYSJYCZTLQZYBBYBWZPQDWWYZCYTJCJXCKCWDKKZXSGKDZXWWYYJQYYTCYTDLLXWKCZKKLCCLZCQQDZLQLCSFQCHQHSFSMQZZLNBJJZBSJHTSZDYSJQJPDLZCDCWJKJZZLPYCGMZWDJJBSJQZSYZYHHXJPBJYDSSXDZNCGLQMBTSFSBPDZDLZNFGFJGFSMPXJQLMBLGQCYYXBQKDJJQYRFKZTJDHCZKLBSDZCFJTPLLJGXHYXZCSSZZXSTJYGKGCKGYOQXJPLZPBPGTGYJZGHZQZZLBJLSQFZGKQQJZGYCZBZQTLDXRJXBSXXPZXHYZYCLWDXJJHXMFDZPFZHQHQMQGKSLYHTYCGFRZGNQXCLPDLBZCSCZQLLJBLHBZCYPZZPPDYMZZSGYHCKCPZJGSLJLNSCDSLDLXBMSTLDDFJMKDJDHZLZXLSZQPQPGJLLYBDSZGQLBZLSLKYYHZTTNTJYQTZZPSZQZTLLJTYYLLQLLQYZQLBDZLSLYYZYMDFSZSNHLXZNCZQZPBWSKRFBSYZMTHBLGJPMCZZLSTLXSHTCSYZLZBLFEQHLXFLCJLYLJQCBZLZJHHSSTBRMHXZHJZCLXFNBGXGTQJCZTMSFZKJMSSNXLJKBHSJXNTNLZDNTLMSJXGZJYJCZXYJYJWRWWQNZTNFJSZPZSHZJFYRDJSFSZJZBJFZQZZHZLXFYSBZQLZSGYFTZDCSZXZJBQMSZKJRHYJZCKMJKHCHGTXKXQGLXPXFXTRTYLXJXHDTSJXHJZJXZWZLCQSBTXWXGXTXXHXFTSDKFJHZYJFJXRZSDLLLTQSQQZQWZXSYQTWGWBZCGZLLYZBCLMQQTZHZXZXLJFRMYZFLXYSQXXJKXRMQDZDMMYYBSQBHGZMWFWXGMXLZPYYTGZYCCDXYZXYWGSYJYZNBHPZJSQSYXSXRTFYZGRHZTXSZZTHCBFCLSYXZLZQMZLMPLMXZJXSFLBYZMYQHXJSXRXSQZZZSSLYFRCZJRCRXHHZXQYDYHXSJJHZCXZBTYNSYSXJBQLPXZQPYMLXZKYXLXCJLCYSXXZZLXDLLLJJYHZXGYJWKJRWYHCPSGNRZLFZWFZZNSXGXFLZSXZZZBFCSYJDBRJKRDHHGXJLJJTGXJXXSTJTJXLYXQFCSGSWMSBCTLQZZWLZZKXJMLTMJYHSDDBXGZHDLBMYJFRZFSGCLYJBPMLYSMSXLSZJQQHJZFXGFQFQBPXZGYYQXGZTCQWYLTLGWSGWHRLFSFGZJMGMGBGTJFSYZZGZYZAFLSSPMLPFLCWBJZCLJJMZLPJJLYMQDMYYYFBGYGYZMLYZDXQYXRQQQHSYYYQXYLJTYXFSFSLLGNQCYHYCWFHCCCFXPYLYPLLZYXXXXXKQHHXSHJZCFZSCZJXCPZWHHHHHAPYLQALPQAFYHXDYLUKMZQGGGDDESRNNZLTZGCHYPPYSQJJHCLLJTOLNJPZLJLHYMHEYDYDSQYCDDHGZUNDZCLZYZLLZNTNYZGSLHSLPJJBDGWXPCDUTJCKLKCLWKLLCASSTKZZDNQNTTLYYZSSYSSZZRYLJQKCQDHHCRXRZYDGRGCWCGZQFFFPPJFZYNAKRGYWYQPQXXFKJTSZZXSWZDDFBBXTBGTZKZNPZZPZXZPJSZBMQHKCYXYLDKLJNYPKYGHGDZJXXEAHPNZKZTZCMXCXMMJXNKSZQNMNLWBWWXJKYHCPSTMCSQTZJYXTPCTPDTNNPGLLLZSJLSPBLPLQHDTNJNLYYRSZFFJFQWDPHZDWMRZCCLODAXNSSNYZRESTYJWJYJDBCFXNMWTTBYLWSTSZGYBLJPXGLBOCLHPCBJLTMXZLJYLZXCLTPNCLCKXTPZJSWCYXSFYSZDKNTLBYJCYJLLSTGQCBXRYZXBXKLYLHZLQZLNZCXWJZLJZJNCJHXMNZZGJZZXTZJXYCYYCXXJYYXJJXSSSJSTSSTTPPGQTCSXWZDCSYFPTFBFHFBBLZJCLZZDBXGCXLQPXKFZFLSYLTUWBMQJHSZBMDDBCYSCCLDXYCDDQLYJJWMQLLCSGLJJSYFPYYCCYLTJANTJJPWYCMMGQYYSXDXQMZHSZXPFTWWZQSWQRFKJLZJQQYFBRXJHHFWJJZYQAZMYFRHCYYBYQWLPEXCCZSTYRLTTDMQLYKMBBGMYYJPRKZNPBSXYXBHYZDJDNGHPMFSGMWFZMFQMMBCMZZCJJLCNUXYQLMLRYGQZCYXZLWJGCJCGGMCJNFYZZJHYCPRRCMTZQZXHFQGTJXCCJEAQCRJYHPLQLSZDJRBCQHQDYRHYLYXJSYMHZYDWLDFRYHBPYDTSSCNWBXGLPZMLZZTQSSCPJMXXYCSJYTYCGHYCJWYRXXLFEMWJNMKLLSWTXHYYYNCMMCWJDQDJZGLLJWJRKHPZGGFLCCSCZMCBLTBHBQJXQDSPDJZZGKGLFQYWBZYZJLTSTDHQHCTCBCHFLQMPWDSHYYTQWCNZZJTLBYMBPDYYYXSQKXWYYFLXXNCWCXYPMAELYKKJMZZZBRXYYQJFLJPFHHHYTZZXSGQQMHSPGDZQWBWPJHZJDYSCQWZKTXXSQLZYYMYSDZGRXCKKUJLWPYSYSCSYZLRMLQSYLJXBCXTLWDQZPCYCYKPPPNSXFYZJJRCEMHSZMSXLXGLRWGCSTLRSXBZGBZGZTCPLUJLSLYLYMTXMTZPALZXPXJTJWTCYYZLBLXBZLQMYLXPGHDSLSSDMXMBDZZSXWHAMLCZCPJMCNHJYSNSYGCHSKQMZZQDLLKABLWJXSFMOCDXJRRLYQZKJMYBYQLYHETFJZFRFKSRYXFJTWDSXXSYSQJYSLYXWJHSNLXYYXHBHAWHHJZXWMYLJCSSLKYDZTXBZSYFDXGXZJKHSXXYBSSXDPYNZWRPTQZCZENYGCXQFJYKJBZMLJCMQQXUOXSLYXXLYLLJDZBTYMHPFSTTQQWLHOKYBLZZALZXQLHZWRRQHLSTMYPYXJJXMQSJFNBXYXYJXXYQYLTHYLQYFMLKLJTMLLHSZWKZHLJMLHLJKLJSTLQXYLMBHHLNLZXQJHXCFXXLHYHJJGBYZZKBXSCQDJQDSUJZYYHZHHMGSXCSYMXFEBCQWWRBPYYJQTYZCYQYQQZYHMWFFHGZFRJFCDPXNTQYZPDYKHJLFRZXPPXZDBBGZQSTLGDGYLCQMLCHHMFYWLZYXKJLYPQHSYWMQQGQZMLZJNSQXJQSYJYCBEHSXFSZPXZWFLLBCYYJDYTDTHWZSFJMQQYJLMQXXLLDTTKHHYBFPWTYYSQQWNQWLGWDEBZWCMYGCULKJXTMXMYJSXHYBRWFYMWFRXYQMXYSZTZZTFYKMLDHQDXWYYNLCRYJBLPSXCXYWLSPRRJWXHQYPHTYDNXHHMMYWYTZCSQMTSSCCDALWZTCPQPYJLLQZYJSWXMZZMMYLMXCLMXCZMXMZSQTZPPQQBLPGXQZHFLJJHYTJSRXWZXSCCDLXTYJDCQJXSLQYCLZXLZZXMXQRJMHRHZJBHMFLJLMLCLQNLDXZLLLPYPSYJYSXCQQDCMQJZZXHNPNXZMEKMXHYKYQLXSXTXJYYHWDCWDZHQYYBGYBCYSCFGPSJNZDYZZJZXRZRQJJYMCANYRJTLDPPYZBSTJKXXZYPFDWFGZZRPYMTNGXZQBYXNBUFNQKRJQZMJEGRZGYCLKXZDSKKNSXKCLJSPJYYZLQQJYBZSSQLLLKJXTBKTYLCCDDBLSPPFYLGYDTZJYQGGKQTTFZXBDKTYYHYBBFYTYYBCLPDYTGDHRYRNJSPTCSNYJQHKLLLZSLYDXXWBCJQSPXBPJZJCJDZFFXXBRMLAZHCSNDLBJDSZBLPRZTSWSBXBCLLXXLZDJZSJPYLYXXYFTFFFBHJJXGBYXJPMMMPSSJZJMTLYZJXSWXTYLEDQPJMYGQZJGDJLQJWJQLLSJGJGYGMSCLJJXDTYGJQJQJCJZCJGDZZSXQGSJGGCXHQXSNQLZZBXHSGZXCXYLJXYXYYDFQQJHJFXDHCTXJYRXYSQTJXYEFYYSSYYJXNCYZXFXMSYSZXYYSCHSHXZZZGZZZGFJDLTYLNPZGYJYZYYQZPBXQBDZTZCZYXXYHHSQXSHDHGQHJHGYWSZTMZMLHYXGEBTYLZKQWYTJZRCLEKYSTDBCYKQQSAYXCJXWWGSBHJYZYDHCSJKQCXSWXFLTYNYZPZCCZJQTZWJQDZZZQZLJJXLSBHPYXXPSXSHHEZTXFPTLQYZZXHYTXNCFZYYHXGNXMYWXTZSJPTHHGYMXMXQZXTSBCZYJYXXTYYZYPCQLMMSZMJZZLLZXGXZAAJZYXJMZXWDXZSXZDZXLEYJJZQBHZWZZZQTZPSXZTDSXJJJZNYAZPHXYYSRNQDTHZHYYKYJHDZXZLSWCLYBZYECWCYCRYLCXNHZYDZYDYJDFRJJHTRSQTXYXJRJHOJYNXELXSFSFJZGHPZSXZSZDZCQZBYYKLSGSJHCZSHDGQGXYZGXCHXZJWYQWGYHKSSEQZZNDZFKWYSSTCLZSTSYMCDHJXXYWEYXCZAYDMPXMDSXYBSQMJMZJMTZQLPJYQZCGQHXJHHLXXHLHDLDJQCLDWBSXFZZYYSCHTYTYYBHECXHYKGJPXHHYZJFXHWHBDZFYZBCAPNPGNYDMSXHMMMMAMYNBYJTMPXYYMCTHJBZYFCGTYHWPHFTWZZEZSBZEGPFMTSKFTYCMHFLLHGPZJXZJGZJYXZSBBQSCZZLZCCSTPGXMJSFTCCZJZDJXCYBZLFCJSYZFGSZLYBCWZZBYZDZYPSWYJZXZBDSYUXLZZBZFYGCZXBZHZFTPBGZGEJBSTGKDMFHYZZJHZLLZZGJQZLSFDJSSCBZGPDLFZFZSZYZYZSYGCXSNXXCHCZXTZZLJFZGQSQYXZJQDCCZTQCDXZJYQJQCHXZTDLGSCXZSYQJQTZWLQDQZTQCHQQJZYEZZZPBWKDJFCJPZTYPQYQTTYNLMBDKTJZPQZQZZFPZSBNJLGYJDXJDZZKZGQKXDLPZJTCJDQBXDJQJSTCKNXBXZMSLYJCQMTJQWWCJQNJNLLLHJCWQTBZQYDZCZPZZDZYDDCYZZZCCJTTJFZDPRRTZTJDCQTQZDTJNPLZBCLLCTZSXKJZQZPZLBZRBTJDCXFCZDBCCJJLTQQPLDCGZDBBZJCQDCJWYNLLZYZCCDWLLXWZLXRXNTQQCZXKQLSGDFQTDDGLRLAJJTKUYMKQLLTZYTDYYCZGJWYXDXFRSKSTQTENQMRKQZHHQKDLDAZFKYPBGGPZREBZZYKZZSPEGJXGYKQZZZSLYSYYYZWFQZYLZZLZHWCHKYPQGNPGBLPLRRJYXCCSYYHSFZFYBZYYTGZXYLXCZWXXZJZBLFFLGSKHYJZEYJHLPLLLLCZGXDRZELRHGKLZZYHZLYQSZZJZQLJZFLNBHGWLCZCFJYSPYXZLZLXGCCPZBLLCYBBBBUBBCBPCRNNZCZYRBFSRLDCGQYYQXYGMQZWTZYTYJXYFWTEHZZJYWLCCNTZYJJZDEDPZDZTSYQJHDYMBJNYJZLXTSSTPHNDJXXBYXQTZQDDTJTDYYTGWSCSZQFLSHLGLBCZPHDLYZJYCKWTYTYLBNYTSDSYCCTYSZYYEBHEXHQDTWNYGYCLXTSZYSTQMYGZAZCCSZZDSLZCLZRQXYYELJSBYMXSXZTEMBBLLYYLLYTDQYSHYMRQWKFKBFXNXSBYCHXBWJYHTQBPBSBWDZYLKGZSKYHXQZJXHXJXGNLJKZLYYCDXLFYFGHLJGJYBXQLYBXQPQGZTZPLNCYPXDJYQYDYMRBESJYYHKXXSTMXRCZZYWXYQYBMCLLYZHQYZWQXDBXBZWZMSLPDMYSKFMZKLZCYQYCZLQXFZZYDQZPZYGYJYZMZXDZFYFYTTQTZHGSPCZMLCCYTZXJCYTJMKSLPZHYSNZLLYTPZCTZZCKTXDHXXTQCYFKSMQCCYYAZHTJPCYLZLYJBJXTPNYLJYYNRXSYLMMNXJSMYBCSYSYLZYLXJJQYLDZLPQBFZZBLFNDXQKCZFYWHGQMRDSXYCYTXNQQJZYYPFZXDYZFPRXEJDGYQBXRCNFYYQPGHYJDYZXGRHTKYLNWDZNTSMPKLBTHBPYSZBZTJZSZZJTYYXZPHSSZZBZCZPTQFZMYFLYPYBBJQXZMXXDJMTSYSKKBJZXHJCKLPSMKYJZCXTMLJYXRZZQSLXXQPYZXMKYXXXJCLJPRMYYGADYSKQLSNDHYZKQXZYZTCGHZTLMLWZYBWSYCTBHJHJFCWZTXWYTKZLXQSHLYJZJXTMPLPYCGLTBZZTLZJCYJGDTCLKLPLLQPJMZPAPXYZLKKTKDZCZZBNZDYDYQZJYJGMCTXLTGXSZLMLHBGLKFWNWZHDXUHLFMKYSLGXDTWWFRJEJZTZHYDXYKSHWFZCQSHKTMQQHTZHYMJDJSKHXZJZBZZXYMPAGQMSTPXLSKLZYNWRTSQLSZBPSPSGZWYHTLKSSSWHZZLYYTNXJGMJSZSUFWNLSOZTXGXLSAMMLBWLDSZYLAKQCQCTMYCFJBSLXCLZZCLXXKSBZQCLHJPSQPLSXXCKSLNHPSFQQYTXYJZLQLDXZQJZDYYDJNZPTUZDSKJFSLJHYLZSQZLBTXYDGTQFDBYAZXDZHZJNHHQBYKNXJJQCZMLLJZKSPLDYCLBBLXKLELXJLBQYCXJXGCNLCQPLZLZYJTZLJGYZDZPLTQCSXFDMNYCXGBTJDCZNBGBQYQJWGKFHTNPYQZQGBKPBBYZMTJDYTBLSQMPSXTBNPDXKLEMYYCJYNZCTLDYKZZXDDXHQSHDGMZSJYCCTAYRZLPYLTLKXSLZCGGEXCLFXLKJRTLQJAQZNCMBYDKKCXGLCZJZXJHPTDJJMZQYKQSECQZDSHHADMLZFMMZBGNTJNNLGBYJBRBTMLBYJDZXLCJLPLDLPCQDHLXZLYCBLCXZZJADJLNZMMSSSMYBHBSQKBHRSXXJMXSDZNZPXLGBRHWGGFCXGMSKLLTSJYYCQLTSKYWYYHYWXBXQYWPYWYKQLSQPTNTKHQCWDQKTWPXXHCPTHTWUMSSYHBWCRWXHJMKMZNGWTMLKFGHKJYLSYYCXWHYECLQHKQHTTQKHFZLDXQWYZYYDESBPKYRZPJFYYZJCEQDZZDLATZBBFJLLCXDLMJSSXEGYGSJQXCWBXSSZPDYZCXDNYXPPZYDLYJCZPLTXLSXYZYRXCYYYDYLWWNZSAHJSYQYHGYWWAXTJZDAXYSRLTDPSSYYFNEJDXYZHLXLLLZQZSJNYQYQQXYJGHZGZCYJCHZLYCDSHWSHJZYJXCLLNXZJJYYXNFXMWFPYLCYLLABWDDHWDXJMCXZTZPMLQZHSFHZYNZTLLDYWLSLXHYMMYLMBWWKYXYADTXYLLDJPYBPWUXJMWMLLSAFDLLYFLBHHHBQQLTZJCQJLDJTFFKMMMBYTHYGDCQRDDWRQJXNBYSNWZDBYYTBJHPYBYTTJXAAHGQDQTMYSTQXKBTZPKJLZRBEQQSSMJJBDJOTGTBXPGBKTLHQXJJJCTHXQDWJLWRFWQGWSHCKRYSWGFTGYGBXSDWDWRFHWYTJJXXXJYZYSLPYYYPAYXHYDQKXSHXYXGSKQHYWFDDDPPLCJLQQEEWXKSYYKDYPLTJTHKJLTCYYHHJTTPLTZZCDLTHQKZXQYSTEEYWYYZYXXYYSTTJKLLPZMCYHQGXYHSRMBXPLLNQYDQHXSXXWGDQBSHYLLPJJJTHYJKYPPTHYYKTYEZYENMDSHLCRPQFDGFXZPSFTLJXXJBSWYYSKSFLXLPPLBBBLBSFXFYZBSJSSYLPBBFFFFSSCJDSTZSXZRYYSYFFSYZYZBJTBCTSBSDHRTJJBYTCXYJEYLXCBNEBJDSYXYKGSJZBXBYTFZWGENYHHTHZHHXFWGCSTBGXKLSXYWMTMBYXJSTZSCDYQRCYTWXZFHMYMCXLZNSDJTTTXRYCFYJSBSDYERXJLJXBBDEYNJGHXGCKGSCYMBLXJMSZNSKGXFBNBPTHFJAAFXYXFPXMYPQDTZCXZZPXRSYWZDLYBBKTYQPQJPZYPZJZNJPZJLZZFYSBTTSLMPTZRTDXQSJEHBZYLZDHLJSQMLHTXTJECXSLZZSPKTLZKQQYFSYGYWPCPQFHQHYTQXZKRSGTTSQCZLPTXCDYYZXSQZSLXLZMYCPCQBZYXHBSXLZDLTCDXTYLZJYYZPZYZLTXJSJXHLPMYTXCQRBLZSSFJZZTNJYTXMYJHLHPPLCYXQJQQKZZSCPZKSWALQSBLCCZJSXGWWWYGYKTJBBZTDKHXHKGTGPBKQYSLPXPJCKBMLLXDZSTBKLGGQKQLSBKKTFXRMDKBFTPZFRTBBRFERQGXYJPZSSTLBZTPSZQZSJDHLJQLZBPMSMMSXLQQNHKNBLRDDNXXDHDDJCYYGYLXGZLXSYGMQQGKHBPMXYXLYTQWLWGCPBMQXCYZYDRJBHTDJYHQSHTMJSBYPLWHLZFFNYPMHXXHPLTBQPFBJWQDBYGPNZTPFZJGSDDTQSHZEAWZZYLLTYYBWJKXXGHLFKXDJTMSZSQYNZGGSWQSPHTLSSKMCLZXYSZQZXNCJDQGZDLFNYKLJCJLLZLMZZNHYDSSHTHZZLZZBBHQZWWYCRZHLYQQJBEYFXXXWHSRXWQHWPSLMSSKZTTYGYQQWRSLALHMJTQJSMXQBJJZJXZYZKXBYQXBJXSHZTSFJLXMXZXFGHKZSZGGYLCLSARJYHSLLLMZXELGLXYDJYTLFBHBPNLYZFBBHPTGJKWETZHKJJXZXXGLLJLSTGSHJJYQLQZFKCGNNDJSSZFDBCTWWSEQFHQJBSAQTGYPQLBXBMMYWXGSLZHGLZGQYFLZBYFZJFRYSFMBYZHQGFWZSYFYJJPHZBYYZFFWODGRLMFTWLBZGYCQXCDJYGZYYYYTYTYDWEGAZYHXJLZYYHLRMGRXXZCLHNELJJTJTPWJYBJJBXJJTJTEEKHWSLJPLPSFYZPQQBDLQJJTYYQLYZKDKSQJYYQZLDQTGJQYZJSUCMRYQTHTEJMFCTYHYPKMHYZWJDQFHYYXWSHCTXRLJHQXHCCYYYJLTKTTYTMXGTCJTZAYYOCZLYLBSZYWJYTSJYHBYSHFJLYGJXXTMZYYLTXXYPZLXYJZYZYYPNHMYMDYYLBLHLSYYQQLLNJJYMSOYQBZGDLYXYLCQYXTSZEGXHZGLHWBLJHEYXTWQMAKBPQCGYSHHEGQCMWYYWLJYJHYYZLLJJYLHZYHMGSLJLJXCJJYCLYCJPCPZJZJMMYLCQLNQLJQJSXYJMLSZLJQLYCMMHCFMMFPQQMFYLQMCFFQMMMMHMZNFHHJGTTHHKHSLNCHHYQDXTMMQDCYZYXYQMYQYLTDCYYYZAZZCYMZYDLZFFFMMYCQZWZZMABTBYZTDMNZZGGDFTYPCGQYTTSSFFWFDTZQSSYSTWXJHXYTSXXYLBYQHWWKXHZXWZNNZZJZJJQJCCCHYYXBZXZCYZTLLCQXYNJYCYYCYNZZQYYYEWYCZDCJYCCHYJLBTZYYCQWMPWPYMLGKDLDLGKQQBGYCHJXY";
                var dict = {};
                if (!str || /^ +$/g.test(str)) return '';
                dict.firstletter = pinyin_dict_firstletter;
                if (dict.firstletter) {
                    var result = [];
                    for (var i = 0; i < str.length; i++) {
                        var unicode = str.charCodeAt(i);
                        var ch = str.charAt(i);
                        if (unicode >= 19968 && unicode <= 40869) {
                            ch = dict.firstletter.all.charAt(unicode - 19968);
                        }
                        result.push(ch);
                    }
                    return result.join('');
                }

            },


            batchClearAssign: function () {
                var self = this, canDeleteName = [], canDeleteIds = [], cannotDeleteName = [], flag = true, tip = '';
                if (this.multipleSelection.length == 0 && this.assignRuleForm.auditType == '1') {
                    this.$message({
                        message: '请选择要清除分配的项目',
                        type: 'warning'
                    });
                    return false;
                }
                if (this.assignRuleForm.auditType == '0') {
                    canDeleteIds = [];
                } else if (this.assignRuleForm.auditType == '1') {
                    this.multipleSelection.forEach(function (item) {
                        if (item.taskAssigns && item.taskAssigns != '') {
                            canDeleteName.push(item.pName);
                            if(this.showActrel){
                                canDeleteIds.push(item.provId);
                            }else {
                                canDeleteIds.push(item.id);
                            }

                        } else {
                            cannotDeleteName.push(item.pName);
                            flag = false;
                        }
                    });
                }

                tip = '确定清除分配?';
                if (!flag) {
                    tip = cannotDeleteName.join('、') + '当前不是已分配状态，不需清除！' + (canDeleteName.length > 0 ? canDeleteName.join('、') + '将会清除分配的专家，是否继续？' : '请重新选择！');
                }
                this.$confirm(tip, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.clearAssignAxios(canDeleteIds)
                }).catch(function () {

                })
            },

            clearAssignAxios: function (idList) {
                var self = this;
                if (idList.length == 0 && this.assignRuleForm.auditType == '1') {
                    return false;
                }
                var loading = this.$loading({
                    lock: true,
                    text: '清除分配中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)',
                    customClass: 'gray-green-loading'
                });
                var postData = {
                    actywId: this.assignRuleForm.actywId,
                    gnodeId: this.assignRuleForm.gnodeId,
                    proIdList: idList
                };
                var url = this.showActrel ? '/actyw/actywAssignRule/delProvProTask' : '/actyw/actywAssignRule/delProTask';
                this.$axios.post(url, postData).then(function (response) {
                    var data = response.data;
                    if (data.status ===  1) {
                        self.getDataList();
                        self.$alert('清除分配成功', '提示', {type:'success'})
                    }else {
                        self.$alert(data.msg, '提示', {type:'error'})
                    }
                    loading.close();

                }).catch(function (error) {
                    loading.close();
                })
            },

            singleClearAssign: function (row) {
                var self = this;
                this.$confirm('确定清除分配？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var ids = [];
                    if(self.showActrel){
                        ids.push(row.provId);
                    }else {
                        ids.push(row.id)
                    }
                    self.clearAssignAxios(ids)
                }).catch(function () {

                })
            },


            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                }
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },

            getDictLists: function () {
                var dicts = {
                    'project_type': 'projectTypes',
                    'competition_net_type': 'competitionTypes',
                    'expert_type': 'expertSources',
                    'expert_source': 'expertSourceForm',
                };
                this.getBatchDictList(dicts)

            },
            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.officeList = data.data || [];
                    }
                })
            },

        },
        mounted: function () {
            this.getDataList();
            this.getDictLists();
            this.getOfficeList();
            this.disabled = false;
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })

</script>
</body>
</html>