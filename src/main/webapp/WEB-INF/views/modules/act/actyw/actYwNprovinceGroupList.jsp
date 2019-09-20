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
    <div class="clearfix">
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
                <el-button size="mini" icon="el-icon-circle-plus" type="primary" @click.stop.prevent="goToAdd">添加
                </el-button>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="流程名称"
                        size="mini"
                        name="name"
                        v-model="searchListForm.name"
                        @keyup.enter.native="getActYwGroupList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search" @click.stop.prevent="getActYwGroupList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container">
        <el-table v-loading="tableLoading" :data="actYwGroupList" size="mini" class="table"
                  @sort-change="handleSortChange">
            <el-table-column label="流程名称" sortable="name" prop="name">
                <template slot-scope="scope">
                    <a :href="ctx + '/actyw/actYwGnode/'+scope.row.id+'/view'" target="_blank">
                        {{scope.row.name}}
                    </a>
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
            <el-table-column label="关联项目类型" prop="types" align="center">
                <template slot-scope="scope">
                    {{scope.row.types | checkboxFilter(actProjectTypeEntries)}}
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
            <shiro:hasPermission name="actyw:actYwGroup:edit">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button type="text" size="mini" @click.stop.prevent="handleEditActYwGroup(scope.row)">
                                修改
                            </el-button>
                            <template v-if="scope.row.status == 0">
                                <el-button type="text" size="mini"
                                           @click.stop.prevent="handleDesignActYwGroup(scope.row)">设计
                                </el-button>
                                <el-button type="text" size="mini"
                                           @click.stop.prevent="confirmReleaseActYwGroup(scope.row)">发布
                                </el-button>
                                <el-button  v-if="(scope.row.ywsize != 1) && (!scope.row.isPush)" type="text" size="mini"
                                           @click.stop.prevent="confirmDelActYwGroup(scope.row)">删除
                                </el-button>
                            </template>
                            <template v-else>
                                <el-button type="text" size="mini" @click.stop="handleEditActYwGroup(scope.row)">关联流程
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
    <div class="fixed-act-tip">
        <a href="javascript:void(0);" @click.stop.prevent="dialogVisibleAutoDefinedFlow=true">自定义流程指南</a>
    </div>
    <el-dialog
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
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var isTempDictList = JSON.parse('${fns: toJson(fns: getDictList('true_false'))}');
            var yesNoes = JSON.parse('${fns: toJson(fns: getDictList('yes_no'))}');
            var actProjectTypes = JSON.parse('${fns: toJson(fns: getDictList('act_project_type'))}');
            var isShowStepModal = $.cookie('isShowStepModal');
            var dialogVisibleAutoDefinedFlow = !(isShowStepModal);

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

                isTempDictList: isTempDictList,
                yesNoes: yesNoes,
                actProjectTypes: actProjectTypes,
                flowThemeProps: {
                    label: 'name',
                    value: 'idx'
                },
                flowTypeProps: {
                    label: 'sname',
                    value: 'key'
                },
                tableLoading: true,
                isRemind: !!isShowStepModal,
                dialogVisibleAutoDefinedFlow: dialogVisibleAutoDefinedFlow,
                actYwGroupMessage: '${message}'
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
            }
        },

        methods: {


            handleCloseAutoDefinedFlow: function () {
                this.dialogVisibleAutoDefinedFlow = false;
                if (this.isRemind) {
                    $.cookie('isShowStepModal', 'noMore', {
                        expires: 100
                    })
                } else {
                    $.removeCookie('isShowStepModal')
                }
            },

            handleEditActYwGroup: function (row) {
                location.href = this.ctx + '/actyw/actYwGroup/form?id=' + row.id;
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
                this.$axios.post('/actyw/actYwGroup/ajaxDeployJson', {
                    id: row.id,
                    status: '1',
                    isUpdateYw: true
                }).then(function (response) {
                    var data = response.data;
                    if (data.status != '1') {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        });
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
                    window.parent.sideNavModule.changeLink('${ctx}/actyw/actYw/list?group.flowType=' + row.flowType);
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
                this.$axios.post('/actyw/actYwGroup/ajaxDeployJson', {
                    id: row.id,
                    status: 0,
                    isUpdateYw: true
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getActYwGroupList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '取消发布成功' : data.msg
                    })
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
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '删除成功' : data.msg
                    })
                })
            },

            goToAdd: function () {
                location.href = this.ctx + '/actyw/actYwGroup/form'
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
                this.$axios({
                    method: 'GET',
                    url: '/actyw/actYwGroup/getActYwGroupList',
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status == 1) {
                        pageData = data.data;
                    } else {
                        self.$message({
                            type: 'error',
                            message: self.xhrErrorMsg
                        })
                    }
                    self.actYwGroupList = pageData.list || [];
                    self.pageCount = pageData.count || 0;
                    self.searchListForm.pageNo = pageData.pageNo || 1;
                    self.searchListForm.pageSize = pageData.pageSize || 10;
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
            }
        },
        created: function () {
            this.getActYwGroupList();
            this.getFlowThemes();
            this.getFlowTypes();
        },
        mounted: function () {
            if (this.actYwGroupMessage) {
                this.$msgbox({
                    title: '提示',
                    message: this.actYwGroupMessage,
                    type: this.actYwGroupMessage.indexOf('成功') > -1 ? 'success' : 'error'
                });
                this.actYwGroupMessage = '';
            }
        }
    })

</script>

</body>
</html>