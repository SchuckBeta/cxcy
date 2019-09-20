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
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="mgb-20">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm.theme" label="表单组"
                         :options="flowThemes" :default-props="flowThemeProps"
                         @change="getActYwGroupList"></e-condition>
            <e-condition type="radio" v-model="searchListForm.flowType" label="流程类型"
                         :options="flowTypes" :default-props="flowTypeProps" @change="getActYwGroupList"></e-condition>
            <e-condition type="radio" v-model="searchListForm.status" label="发布状态"
                         :options="yesNoes" @change="getActYwGroupList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button size="mini" type="primary" icon="el-icon-circle-plus" @click.stop="goToAdd">添加</el-button>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="流程名称"
                        size="mini"
                        name="name"
                        v-model="searchListForm.name"
                        @keyup.enter.native="getActYwGroupList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search" @click.stop="getActYwGroupList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container">
        <el-table v-loading="tableLoading" :data="actYwGroupList" size="mini" class="table"
                  @sort-change="handleSortChange">
            <el-table-column label="流程名称" sortable="name" prop="name" width="260">
                <template slot-scope="scope">
                    <a :href="ctx + '/actyw/actYwGnode/designView?groupId=' + scope.row.id" target="_blank">
                        {{scope.row.name}}
                    </a>
                    <br>
                    <p v-if="!!scope.row.relationId">关联流程：
                        <a :href="ctx + '/actyw/actYwGnode/designView?groupId=' + scope.row.relationId" target="_blank">
                            {{scope.row.relationName}}
                        </a>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="表单组" sortable="theme" prop="theme" align="center">
                <template slot-scope="scope">
                    {{scope.row.theme | selectedFilter(flowThemeEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="流程/表单类型" sortable="flowType" prop="flow_type" align="center">
                <template slot-scope="scope">
                    {{scope.row.flowType | selectedFilter(flowTypeEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="发布" sortable="status" prop="status" align="center">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(yesNoEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="临时数据" sortable="temp" prop="temp" align="center">
                <template slot-scope="scope">
                    {{scope.row.temp | selectedFilter(tempDictEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="是否推送数据" sortable="temp" prop="temp" align="center">
                <template slot-scope="scope">
                    {{scope.row.isPush | selectedFilter(tempDictEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="更新时间" prop="update_date" sortable="update_date" align="center">
                <template slot-scope="scope">
                    {{scope.row.updateDate | formatDateFilter('YYYY-MM-DD HH:mm')}}
                </template>
            </el-table-column>
            <el-table-column v-if="showActrel" label="状态" align="center">
                <template slot-scope="scope">
                    <a href="javascript:void(0)" @click.stop="showProGu(scope.row)" v-if="scope.row.step">
                        <span v-if="scope.row.step == '7'">已完成</span>
                        <span v-else>
                            待{{actYwStepEntity[scope.row.step]}}
                        </span>
                    </a>
                </template>
            </el-table-column>
            <shiro:hasPermission name="actyw:actYwGroup:edit">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button type="text" size="mini" @click.stop.prevent="handleEditActYwGroup(scope.row)">
                                修改
                            </el-button>
                            <template v-if="scope.row.status == 0">
                                <el-button class="btn-hand-animation" type="text" size="mini"
                                           @click.stop.prevent="handleDesignActYwGroup(scope.row)"><i
                                        v-if="scope.row.step === '1'" class="iconfont iconfont-ani icon-dianji"></i>设计
                                </el-button>
                                <el-button class="btn-hand-animation" type="text" size="mini"
                                           @click.stop.prevent="confirmReleaseActYwGroup(scope.row)"><i
                                        v-if="scope.row.step === '2'" class="iconfont iconfont-ani icon-dianji"></i>发布
                                </el-button>
                                <el-button v-if="(scope.row.ywsize != 1) && (!scope.row.isPush)" type="text" size="mini"
                                           @click.stop.prevent="confirmDelActYwGroup(scope.row)">删除
                                </el-button>
                            </template>
                            <template v-else>
                                <el-button class="btn-hand-animation" v-if="showActrel && !scope.row.relationId" type="text"
                                           size="mini"
                                           @click.stop="showLinkFlowForm(scope.row)"><i v-if="scope.row.step === '3'"
                                                                                        class="iconfont iconfont-ani icon-dianji"></i>关联流程
                                </el-button>
                                <el-button type="text" size="mini"
                                           @click.stop.prevent="confirmUnReleaseActYwGroup(scope.row)">取消发布
                                </el-button>
                            </template>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>
        </el-table>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
    <shiro:hasPermission name="actyw:actYwGroup:edit">
        <div class="fixed-act-tip">
            <a href="javascript:void(0);" @click.stop="showGuice">自定义流程指南</a>
        </div>
        <el-dialog
                title="关联流程"
                :visible.sync="dialogVisibleLinkFlow"
                :close-on-click-modal="false"
                width="520px"
                :before-close="handleCloseLinkFlow">
            <el-form :model="linkFlowForm" ref="linkFlowForm" :disabled="linkFlowDisabled" size="mini"
                     label-width="120px"
                     autocomplete="off">
                <el-form-item label="当前流程：">{{linkFlowObject.name }}</el-form-item>
                <el-form-item v-show="linkFlowList.length > 0" label="关联下级流程：" :rules="linkModelRules">
                    <el-select v-if="!isLinkedFlow" v-model="linkFlowForm.modelGroupId">
                        <el-option v-for="item in linkFlowList" :label="item.name" :value="item.id"
                                   :key="item.id"></el-option>
                    </el-select>
                    <template v-else>{{linkedFlowObj.relationName}}</template>
                </el-form-item>
                <el-form-item v-if="linkFlowList.length == 0" label="提示：">
                    <span class="empty">无可关联的流程，请切换到运营平台，新建并发布，下发流程模板</span>
                </el-form-item>
            </el-form>
            <div v-if="!isLinkedFlow && linkFlowList.length > 0" slot="footer" class="dialog-footer">
                <el-button :disabled="linkFlowDisabled" size="mini" type="primary" @click.stop="validateLinkFlowForm">确定
                </el-button>
            </div>
        </el-dialog>

        <el-dialog
                title="流程"
                :visible.sync="dvAddFlow"
                :close-on-click-modal="false"
                width="520px"
                :before-close="handleCloseAddFlow">
            <el-form :model="actYwGroupForm" :disabled="disabled" auto-complete="off" autocomplete="off"
                     ref="actYwGroupForm" :rules="actYwGroupRules"
                     size="mini" label-width="120px">
                <el-form-item prop="name" label="流程名称：">
                    <el-input name="name" v-model="actYwGroupForm.name" maxlength="50" placeholder="限50个字符"></el-input>
                </el-form-item>
                <template v-if="actYwGroupForm.status !== '1'">
                    <el-form-item prop="theme" label="表单组：">
                        <el-select v-model="actYwGroupForm.theme" placeholder="请选择" clearable filterable
                                   @change="handleChangeTheme">
                            <el-option v-for="item in flowThemes" :key="item.idx" :value="item.idx"
                                       :label="item.name"></el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="flowType" label="流程类型：">
                        {{actYwGroupForm.flowType | selectedFilter(flowTypeEntries)}}
                    </el-form-item>
                </template>
                <el-form-item prop="remarks" label="备注：">
                    <el-input name="remarks" type="textarea" :rows="4" v-model="actYwGroupForm.remarks" maxlength="200"
                              placeholder="限200个字符"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button :disabled="disabled" type="primary" size="mini" @click.stop="validateActYwGroupForm">保存
                </el-button>
            </div>
        </el-dialog>
        <el-dialog
                v-if="!showActrel"
                title="温馨提示"
                :visible.sync="dialogVisibleAutoDefinedFlow"
                :close-on-click-modal="false"
                width="703px"
                :before-close="handleCloseAutoDefinedFlow">
            <div class="flow-reminder-content">
                <ul class="time-horizontal">
                    <div>
                        <div class="flow-li3"><label>自定义流程</label></div>
                        <div class="flow-li3"><label>自定义项目或大赛</label></div>
                    </div>
                    <li><b class="step-line">1</b><span>添加流程</span></li>
                    <li><b class="step-line">2</b><span>设计流程</span></li>
                    <li><b class="step-line">3</b><span>发布流程</span></li>
                    <li><b class="step-line">4</b><span>添加项目</span></li>
                    <li><b class="step-line">5</b><span>设置编号</span></li>
                    <li><b>6</b><span>发布项目</span></li>
                </ul>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-checkbox v-model="isRemind">不再提醒</el-checkbox>
            </div>
        </el-dialog>

        <el-dialog
                v-if="showActrel"
                title="操作引导"
                :visible.sync="dvGuidance"
                :close-on-click-modal="false"
                width="760px"
                :before-close="handleCloseDvGuidance">
            <div class="flow-guidance-step">
                <div v-for="(item, index) in actYwGroupSteps" class="fgs-item"
                     :class="{'completed': item.status === '1'}" :style="{'zIndex':   actYwGroupSteps.length - index}">
                    <div class="fgs-item-label">第{{item.upNum}}步</div>
                    <div class="fgs-item-name">
                        <a :href="item.href" @click.stop="handleChangeStep(index)">{{item.name}}</a>
                    </div>
                </div>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button size="mini" @click.stop="handleCloseDvGuidance">关闭</el-button>
            </div>
        </el-dialog>
    </shiro:hasPermission>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                searchListForm: {
                    theme: '',
                    flowType: '',
                    type: '',
                    status: '',
                    name: '',
                    orderBy: '',
                    orderByType: '',
                    pageNo: 1,
                    pageSize: 10
                },

                flowThemes: [],
                flowTypes: [],

                actYwGroupList: [],
                pageCount: 0,

                isTempDictList: [],
                yesNoes: [],
                actProjectTypes: [],
                flowThemeProps: {
                    label: 'name',
                    value: 'idx'
                },
                flowTypeProps: {
                    label: 'sname',
                    value: 'key'
                },
                tableLoading: true,
                isRemind: false,
                dialogVisibleAutoDefinedFlow: false,

                dialogVisibleLinkFlow: false,
                linkFlowDisabled: false,
                linkFlowForm: {
                    provGroupId: '',
                    modelGroupId: ''
                },
                linkFlowList: [],
                linkModelRules: [{required: true, message: '请选择关联流程', trigger: 'change'}],
                isLinkedFlow: false,
                linkedFlowObj: {},


                dvAddFlow: false,
                actYwGroupForm: {
                    id: '',
                    name: '',
                    theme: '',
                    flowType: '',
                    remarks: '',
                    status: ''
                },
                disabled: false,

                actYwGroupRules: {
                    name: [
                        {required: true, message: '请输入流程名称', trigger: 'blur'},
                    ],
                    theme: [
                        {required: true, message: '请选择表单组', trigger: 'change'},
                    ]
                },

                dvGuidance: false,

                actYwGroupSteps: [
                    {name: '创建流程', status: '0', upNum: '一', id: '', href: 'javascript:void(0);'},
                    {name: '设计流程', status: '0', upNum: '二', id: '', href: 'javascript:void(0);'},
                    {name: '发布流程', status: '0', upNum: '三', id: '', href: 'javascript:void(0);'},
                    {name: '关联流程', status: '0', upNum: '四', id: '', href: 'javascript:void(0);'},
                    {name: '创建项目', status: '0', upNum: '五', id: '', href: 'javascript:void(0);'},
                    {name: '设置项目编号', status: '0', upNum: '六', id: '', href: 'javascript:void(0);'},
                    {name: '选择发布的学校', status: '0', upNum: '七', id: '', href: 'javascript:void(0);'}
                ],
                actYwStepEntity: {
                    '0': '创建流程',
                    '1': '设计流程',
                    '2': '发布流程',
                    '3': '关联流程',
                    '4': '创建项目',
                    '5': '设置项目编号',
                    '6': '选择发布的学校'
                },
                isFlowComplete: false,
            }
        },

        computed: {
            tempDictEntries: function () {
                return this.getEntries(this.isTempDictList)
            },
            yesNoEntries: function () {
                return this.getEntries(this.yesNoes)
            },
            actProjectTypeEntries: function () {
                return this.getEntries(this.actProjectTypes)
            },
            flowThemeEntries: function () {
                return this.getEntries(this.flowThemes, this.flowThemeProps)
            },
            flowTypeEntries: function () {
                return this.getEntries(this.flowTypes, this.flowTypeProps)
            },

            linkFlowObject: function () {
                var provGroupId = this.linkFlowForm.provGroupId;
                if (!provGroupId) {
                    return {}
                }
                var i = 0, actYwGroupList = this.actYwGroupList;
                while (i < actYwGroupList.length) {
                    var actYw = actYwGroupList[i];
                    if (actYw.id === provGroupId) {
                        return actYw;
                    }
                    i++;
                }
                return {}
            }
        },
        methods: {

            showProGu: function (row) {
                var self = this;
                var step = row.step ? row.step : '5';
                step = parseInt(step);
                this.isFlowComplete = step === 7;
                this.actYwGroupSteps.forEach(function (item, index) {
                    if (step === 7) {
                        item.status = '1'
                    } else {
                        item.status = index === step ? '1' : '0';
                        item.row = row;
                    }
                });
                this.dvGuidance = true;
            },

            handleChangeStep: function (index) {
                var groupStep = this.getGroupStepsByStatus();
                var row = {}, step;
                if (this.isFlowComplete) {
                    return;
                }
                if (groupStep) {
                    row = groupStep.row;
                }
                step = row.step || '0';
                if(step !== index.toString()){
                    return false;
                }
                var href;
                switch (step) {
                    case '0':
                        this.dvAddFlow = true;
                        this.handleCloseDvGuidance();
                        break;
                    case '1':
                        location.href = '${ctx}/actyw/actYwGnode/designNew?group.id=' + row.id + '&groupId=' + row.id;
                        break;
                    case '2':
                        this.dvGuidance = false;
                        this.confirmReleaseActYwGroup(row);
                        break;
                    case '3':
                        this.dvGuidance = false;
                        this.showLinkFlowForm(row);
                        break;
                    case '4':
                        href = '${ctx}/actyw/actYw/list?group.flowType=' + row.flowType;
                        this.changeLinkFn(href);
                        location.href = '${ctx}/actyw/actYw/form?group.flowType=' + row.flowType + '&groupId=' + row.id;
                        break;
                    case '5':
                        href = '${ctx}/sys/sysNumberRule';
                        this.changeLinkFn(href);
                        location.href = href;
                        break;
                    case '6':
                        href = '${ctx}/actyw/actYw/list?group.flowType=' + row.flowType;
                        this.changeLinkFn(href);
                        location.href = href
                        break;
                }
            },

            changeLinkFn: function (href) {
                if (window.parent) {
                    window.parent.sideNavModule && window.parent.sideNavModule.changeLink(href)
                }
            },

            getGroupStepsByStatus: function () {
                var actYwGroupSteps = this.actYwGroupSteps;
                var groupStep;
                for (var i = 0; i < actYwGroupSteps.length; i++) {
                    var item = actYwGroupSteps[i];
                    if (item.status === '1') {
                        groupStep = item;
                        break;
                    }
                }
                return groupStep;
            },


            showGuice: function () {
                if (this.showActrel) {
                    this.dvGuidance = true;
                } else {
                    this.dialogVisibleAutoDefinedFlow = true;
                }
            },

            handleCloseDvGuidance: function () {
                this.dvGuidance = false;
                this.actYwGroupSteps.forEach(function (item) {
                    item.id = '';
                    item.status = '0'
                    delete item.row;
                })
            },

            getActYwStep: function (row) {
                var self = this;
                this.$axios.get('/actyw/actYwStep/getActYwStepByGroupId', {params: {groupId: row.id}}).then(function (reponse) {
                    var data = reponse.data;
                    if (data.status === 1) {
                        console.log(data)
                    }
                })
            },

            handleCloseAddFlow: function () {
                this.actYwGroupForm.id = '';
                this.actYwGroupForm.flowType = '';
                this.actYwGroupForm.theme = '';
                this.actYwGroupForm.status = '';
                this.$refs.actYwGroupForm.resetFields();
                this.dvAddFlow = false;
            },

            goToAdd: function () {
                this.dvAddFlow = true;
            },

            handleEditActYwGroup: function (row) {
                this.dvAddFlow = true;
                this.$nextTick(function () {
                    this.assignFormData(this.actYwGroupForm, row);
                })
            },

            validateActYwGroupForm: function () {
                var self = this;
                this.$refs.actYwGroupForm.validate(function (valid) {
                    if (valid) {
                        self.submitActYwGroupForm();
                    }
                })
            },

            submitActYwGroupForm: function () {
                var self = this;
                this.disabled = true;
                this.$axios({
                    method: 'GET',
                    url: '/actyw/actYwGroup/ajaxSave',
                    params: this.actYwGroupForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.handleCloseAddFlow();
                        self.getActYwGroupList();
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            showConfirmButton: self.actYwGroupForm.status != '1',
                            showCancelButton: false,
                            confirmButtonText: '设计流程图',
                            message: data.msg || '自定义流程创建成功'
                        }).then(function () {
                            location.href = '${ctx}/actyw/actYwGnode/designNew?group.id=' + data.datas + '&groupId=' + data.datas;
                        }).catch(function () {

                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                })
            },

            handleChangeTheme: function (value) {
                var theme = this.getThemeById(value);
                if (theme) {
                    this.actYwGroupForm.flowType = theme.ftype;
                }

                if (!this.actYwGroupForm.id) {
                    return;
                }
                this.$alert("确认修改表单组属性吗？若修改，请更新设计页的关联表单，否则流程发布后无法正常审核", "提示", {
                    type: 'warning'
                })
            },

            getThemeById: function (id) {
                for (var i = 0; i < this.flowThemes.length; i++) {
                    if (this.flowThemes[i].idx === id) {
                        return this.flowThemes[i]
                    }
                }
                return false;
            },

            handleCloseLinkFlow: function () {
                this.dialogVisibleLinkFlow = false;
            },

            showLinkFlowForm: function (row) {
                this.linkFlowForm.provGroupId = row.id;
                this.linkFlowForm.modelGroupId = '';
                this.dialogVisibleLinkFlow = true;
                this.isLinkedFlow = !!row.relationId;
                this.linkedFlowObj = row;
                if (!this.isLinkedFlow) {
                    this.getLinkFlowList();
                }
            },

            getLinkFlowList: function () {
                var self = this;
                var types = this.linkFlowObject.types;
                var type = types.length > 0 ? types[0] : '';
                type += ',';
                this.$axios.get('/actyw/actYw/getActywByModel', {params: {proType: type}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.linkFlowList = data.data || [];
                    }
                })
            },

            validateLinkFlowForm: function () {
                var self = this;
                this.$refs.linkFlowForm.validate(function (valid) {
                    if (valid) {
                        self.saveRelProvSchool();
                    }
                })
            },

            saveRelProvSchool: function () {
                var self = this;
                this.linkFlowDisabled = true;
                this.$axios.get('/actyw/actYwGroup/relProvSchool', {params: this.linkFlowForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.linkFlowList = data.data || [];
                        self.$alert('关联流程成功', '提示', {
                            type: 'success'
                        })
                        self.getActYwGroupList();
                        self.dialogVisibleLinkFlow = false;
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.linkFlowDisabled = false;
                }).catch(function () {
                    self.linkFlowDisabled = false;
                })
            },


            handleCloseAutoDefinedFlow: function () {
                this.dialogVisibleAutoDefinedFlow = false;
                if (this.isRemind) {
                    $.cookie('isShowStepModal', 'noMore', {
                        expires: 100,
                        path: '/'
                    })
                } else {
                    $.removeCookie('isShowStepModal')
                }
            },


            handleDesignActYwGroup: function (row) {
                location.href = this.ctx + '/actyw/actYwGnode/designNew?group.id=' + row.id + '&groupId=' + row.id
            },

            confirmReleaseActYwGroup: function (row) {
                var self = this;
                this.$confirm('流程发布后，将会允许被应用到项目。流程一旦被使用，有项目申报，就无法再修改流程，确认发布此流程吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.releaseActYwGroup(row)
                }).catch(function () {

                })
            },

            releaseActYwGroup: function (row) {
                var self = this;
                this.$axios.get('/actyw/actYwGroup/ajaxDeployJson?id=' + row.id + '&status=1&isUpdateYw=true').then(function (response) {
                    var data = response.data;
                    if (data.status != '1') {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                        return false;
                    }
                    self.releaseActYwGroupSuccess(data.msg, row);
                })
            },

            releaseActYwGroupSuccess: function (msg, row) {
                var self = this;
                this.$confirm((msg || '流程发布成功，') + '请去自定义项目或者自定义大赛页面，添加项目', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'success'
                }).then(function () {
                    if (window.parent.sideNavModule) {
                        window.parent.sideNavModule.changeLink('${ctx}/actyw/actYw/list?group.flowType=' + row.flowType);
                    }
                    location.href = '${ctx}/actyw/actYw/form?group.flowType=' + row.flowType + '&groupId=' + row.id;
                }).catch(function () {
                    self.getActYwGroupList();
                })
            },

            confirmUnReleaseActYwGroup: function (row) {
                var self = this;
                this.$confirm('确认要取消发布方案' + row.name + '吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.unReleaseActYwGroup(row)
                }).catch(function () {

                })
            },

            unReleaseActYwGroup: function (row) {
                var self = this;
                this.$axios.get('/actyw/actYwGroup/ajaxDeployJson?id=' + row.id + '&status=0&isUpdateYw=true').then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getActYwGroupList();
                        self.$alert('取消发布成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },


            confirmDelActYwGroup: function (row) {
                var self = this;
                this.$confirm('确认要删除该自定义流程吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delActYwGroup(row)
                }).catch(function () {

                })
            },


            delActYwGroup: function (row) {
                var self = this;
                this.$axios.post('/actyw/actYwGroup/delActYwGroup', {
                    id: row.id
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getActYwGroupList();
                        self.$alert('删除成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },


            handleSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getActYwGroupList()
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getActYwGroupList()
            },

            handlePCPChange: function () {
                this.getActYwGroupList()
            },

            getActYwGroupList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/actyw/actYwGroup/getActYwGroupList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status == 1) {
                        pageData = data.data;
                        self.actYwGroupList = pageData.list || [];
                        self.pageCount = pageData.count || 0;
                        self.searchListForm.pageNo = pageData.pageNo || 1;
                        self.searchListForm.pageSize = pageData.pageSize || 10;
                    }
                    self.tableLoading = false;
                }).catch(function () {
                    self.tableLoading = false;
                })
            },

            getFlowThemes: function () {
                var self = this;
                this.$axios.get('/actyw/actYwGroup/getFlowThemes').then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.flowThemes = data.data;
                    }
                })
            },

            getFlowTypes: function () {
                var self = this;
                this.$axios.get('/actyw/actYwGroup/getFlowTypes').then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.flowTypes = JSON.parse(data.data);
                    }
                })
            },

            getDictLists: function () {
                var dicts = {
                    'true_false': 'isTempDictList',
                    'yes_no': 'yesNoes',
                    'act_project_type': 'actProjectTypes'
                };
                this.getBatchDictList(dicts)
            },

            setGud: function () {
                var isShowStepModal = $.cookie('isShowStepModal');
                var dialogVisibleAutoDefinedFlow = !(isShowStepModal);
                if (this.showActrel) {
                    this.dvGuidance = dialogVisibleAutoDefinedFlow;
                } else {
                    this.dialogVisibleAutoDefinedFlow = dialogVisibleAutoDefinedFlow;
                }
                this.isRemind = !!isShowStepModal
            }
        },
        mounted: function () {
            this.getActYwGroupList();
            this.getFlowThemes();
            this.getFlowTypes();
            this.getDictLists();
        }
    })

</script>

</body>
</html>