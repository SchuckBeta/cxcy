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


<div id="app" v-show="pageLoad" class="container-fluid mgb-60 user-share" style="display: none">
    <edit-bar></edit-bar>
    <div class="mgb-20">
        <div class="conditions">
            <e-condition label="所属学院" type="radio" v-model="searchListForm.officeId" :default-props="defaultProps"
                         name="officeId" :options="collegeList" @change="getDataList">
            </e-condition>
            <e-condition label="专家来源" type="radio" v-model="searchListForm.expertType"
                         name="expertType" :options="expertSources" @change="getDataList">
            </e-condition>
            <e-condition label="职称" type="radio" v-model="searchListForm.technicalTitle"
                         name="technicalTitle" :options="expertJobNames" @change="getDataList">
            </e-condition>
            <e-condition label="评审任务" type="radio" v-model="searchListForm.curJoinStr"
                         name="curJoinStr" :options="curJoinProjects" @change="getDataList">
            </e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-popover
                        placement="bottom"
                        width="340"
                        v-model="batchPopoverVisible"
                        @show="showBatchChangeRole">
                    <el-form size="mini" :model="batchRoleForm" ref="batchRoleForm"
                             label-width="60px">
                        <el-form-item prop="expertTypeList" label="专家：">
                            <el-checkbox-group v-model="batchRoleForm.expertTypeList" class="multi-row">
                                <el-checkbox v-for="item in expertSources" :key="item.value" :label="item.value">
                                    {{item.label}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                        <el-form-item prop="teachertype" label="导师：">
                            <el-radio-group v-model="batchRoleForm.teachertype" class="multi-row">
                                <el-radio v-for="item in masterTypes" :key="item.value" :label="item.value">
                                    {{item.label}}
                                </el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <div class="text-center" style="margin-top:16px;">
                            <el-button size="mini"
                                       @click.stop.prevent="batchPopoverVisible = false">取消
                            </el-button>
                            <el-button type="primary" size="mini" @click.stop.prevent="batchChangeRole">确定
                            </el-button>
                        </div>
                    </el-form>
                    <el-button slot="reference" size="mini" icon="iconfont icon-zhuanhuan" type="primary">批量转换身份</el-button>
                </el-popover>

                <el-button size="mini" type="primary" icon="el-icon-circle-plus" @click.stop.prevent="goAdd">添加专家
                </el-button>
                <el-tooltip class="item" content="请选择需要删除的专家" :disabled="multipleSelection.length > 0"
                            popper-class="white" placement="bottom">
                    <span>
                <el-button size="mini" icon="el-icon-delete" type="primary" :disabled="multipleSelection.length == 0"
                           @click.stop.prevent="batchDelete">批量删除
                </el-button>
                        </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="专家姓名/工号"
                        size="mini"
                        name="queryStr"
                        v-model="searchListForm.queryStr"
                        @keyup.enter.native="getDataList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>

            </div>
        </div>
    </div>
    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="pageList" class="table" v-loading="loading"
                  @sort-change="handleTableSortChange" @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>

            <el-table-column prop="u.no" label="专家信息" align="left" width="320" sortable="u.no">
                <template slot-scope="scope">
                    <div class="user-element">
                        <div class="user-img">
                            <img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                        </div>
                        <div class="user-detail">
                            <div class="user-name">
                                <img src="${ctxImages}/user-name.png" alt="">
                                <a :href="frontOrAdmin + '/sys/backTeacherExpansion/expertView?user.id=' + scope.row.user.id"
                                   class="underline-pointer">{{scope.row.user.name ||
                                    '-'}}</a>
                            </div>
                            <div class="user-tag">
                                <img src="${ctxImages}/user-no.png" alt="">
                                {{scope.row.user.no || '-'}}
                            </div>
                            <div class="user-phone">
                                <el-tooltip :content="scope.row.user | getUserCollege(collegeEntries)"
                                            popper-class="white"
                                            placement="right">
                                        <span class="break-ellipsis">
                                            <i class="iconfont icon-dibiao2"></i>
                                            {{scope.row.user | getUserCollege(collegeEntries)}}
                                        </span>
                                </el-tooltip>
                            </div>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="proNameList" label="当前评审任务" align="center" min-width="100">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.proNameList ? scope.row.proNameList.join(',') : ''"
                                popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.proNameList ? scope.row.proNameList.join(',') : ''}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="专家来源" align="center" min-width="95">
                <template slot-scope="scope">
                    {{scope.row.expertType | checkboxFilter(expertSourcesEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="职称" align="center" min-width="85">
                <template slot-scope="scope">
                    {{scope.row.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}
                </template>
            </el-table-column>
            <%--<shiro:hasPermission name="sys:expert:edit">--%>
            <el-table-column label="操作" align="center" min-width="150">
                <template slot-scope="scope">

                    <el-popover
                            placement="bottom"
                            width="340"
                            v-model="scope.row.popoverVisible"
                            @show="handleShow(scope.row)">

                        <el-form size="mini" :model="roleForm" ref="roleForm"
                                 label-width="60px">
                            <el-form-item prop="expertTypeList" label="专家：">
                                <el-checkbox-group v-model="roleForm.expertTypeList" class="multi-row">
                                    <el-checkbox v-for="item in expertSources" :key="item.value"
                                                 :label="item.value">
                                        {{item.label}}
                                    </el-checkbox>
                                </el-checkbox-group>
                            </el-form-item>
                            <el-form-item prop="teachertype" label="导师：">
                                <el-radio-group v-model="roleForm.teachertype" class="multi-row">
                                    <el-radio v-for="item in masterTypes" :key="item.value" :label="item.value">
                                        {{item.label}}
                                    </el-radio>
                                </el-radio-group>
                            </el-form-item>

                            <div class="text-center" style="margin-top:16px;">
                                <el-button size="mini"
                                           @click.stop.prevent="scope.row.popoverVisible = false">取消
                                </el-button>
                                <el-button type="primary" size="mini"
                                           @click.stop.prevent="saveChangeRole(scope.row)">确定
                                </el-button>
                            </div>

                        </el-form>

                        <span slot="reference">
                        <el-tooltip class="item" content="转换身份" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="iconfont icon-zhuanhuan">

                            </el-button>
                                       </el-tooltip></span>
                    </el-popover>
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                        <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       @click.stop.prevent="goEdit(scope.row)">
                            </el-button>
                        </span>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop.prevent="singleDelete(scope.row)">
                            </el-button>
                        </span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <%--</shiro:hasPermission>--%>
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


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var curJoinProjects = JSON.parse(JSON.stringify(${fns:toJson(fns: getPublishDictList())})) || [];

            return {
                pageList: [],
                pageCount: 0,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    officeId: '',
                    expertType: '',
                    teachertype: '',
                    technicalTitle: '',
                    curJoinStr: '',
                    queryStr: ''
                },
                masterTypes: [],
                expertSources: [],
                expertJobNames: [],
                officeList: [],
                curJoinProjects: curJoinProjects,
                loading: false,
                message: '${message}',
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                multipleSelection: [],
                multipleSelectedId: [],


                hideRoleNames: ['2494442643c24193bc1aa480eddcf43f', 'ecee0da215d04186bdeea0373bf8eeea', 'ef8b7924557747e2ac71fe5b52771c08'],
                roleList: [],
                roleForm: {
                    user: {
                        id: ''
                    },
                    roleType: '',
                    expertTypeList: [],
                    teachertype: ''
                },

                batchPopoverVisible: false,
                batchRoleForm: {
                    userIdList: [],
                    roleType: '',
                    expertTypeList: [],
                    teachertype: ''
                }

            }
        },
        computed: {
            expertTypes: function () {
                var names = this.hideRoleNames;
                return this.roleList.filter(function (item) {
                    return names.indexOf(item.id) > -1
                })
            },

            collegeList: function () {
                return this.officeList.filter(function (item) {
                    return item.grade == '2';
                })
            },

            collegeEntries: function () {
                var i = 0;
                var entity = {};
                while (i < this.officeList.length) {
                    var item = this.officeList[i];
                    entity[item.id] = item;
                    i++
                }
                return entity;
            },

            expertSourcesEntries: function () {
                return this.getEntries(this.expertSources);
            },

            expertJobNamesEntries: function () {
                return this.getEntries(this.expertJobNames);
            }
        },
        methods: {
            batchChangeRole: function () {
                var self = this, tip = '确定转换身份？', newRoleTypes = [];
                var expertLength = this.batchRoleForm.expertTypeList.length;
                if (expertLength == 0 && this.batchRoleForm.teachertype == '') {
                    this.$message({
                        message: '至少要有一个身份！请重新选择！',
                        type: 'warning'
                    });
                    return false;
                }
                if (expertLength > 0) {
                    newRoleTypes.push('1');
                }
                if (this.batchRoleForm.teachertype != '') {
                    newRoleTypes.push('0');
                }
                this.batchRoleForm.roleType = newRoleTypes.join(',');
                this.batchRoleForm.userIdList = this.multipleSelectedId;
                this.$confirm(tip, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: '/sys/backTeacherExpansion/PlChangeExpertOrTea',
                        data: self.batchRoleForm
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.batchPopoverVisible = false;
                        self.$message({
                            message: data.status == '1' ? data.msg || '转换身份成功' : data.msg || '转换身份失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.batchPopoverVisible = false;
                    })
                }).catch(function () {
                    row.popoverVisible = false;
                });
            },
            showBatchChangeRole: function () {
                if (this.multipleSelectedId.length == 0) {
                    this.batchPopoverVisible = false;
                    this.$message({
                        message: '请选择专家！',
                        type: 'warning'
                    });
                    return false;
                }
                this.batchRoleForm = {
                    userIdList: [],
                    roleType: '',
                    expertTypeList: [],
                    teachertype: ''
                }
            },

            getRoleList: function () {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/sys/role/getRoleList'
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.roleList = data.data || [];
                    }
                }).catch(function () {

                })
            },
            handleShow: function (row) {
                this.roleForm = {
                    user: {
                        id: row.user.id || ''
                    },
                    roleType: row.roleType || '',
                    expertTypeList: row.expertTypeList || [],
                    teachertype: row.teachertype || ''
                }
            },
            saveChangeRole: function (row) {
                var self = this, tip = '确定转换身份？', newRoleTypes = [];
                var expertLength = this.roleForm.expertTypeList.length;
                if (expertLength == 0 && this.roleForm.teachertype == '') {
                    this.$message({
                        message: row.user.name + '至少要有一个身份！请重新选择！',
                        type: 'warning'
                    });
                    return false;
                }
                if (expertLength > 0) {
                    newRoleTypes.push('1');
                }
                if (this.roleForm.teachertype != '') {
                    newRoleTypes.push('0');
                }
                this.roleForm.roleType = newRoleTypes.join(',');

                this.$confirm(tip, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: 'sys/backTeacherExpansion/changeExpertOrTea',
                        data: self.roleForm
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        row.popoverVisible = false;
                        self.$message({
                            message: data.status == '1' ? '转换身份成功' : data.msg || '转换身份失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        row.popoverVisible = false;
                    })
                }).catch(function () {
                    row.popoverVisible = false;
                });
            },
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/sys/expert/getExpertList', {params: this.searchListForm}).then(function (response) {
                    var page = response.data.data;
                    if (response.data.status == '1') {
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        var list = page.list || [];
                        list.forEach(function (item) {
                            item.popoverVisible = false;
                        });
                        self.pageList = list;
                    }
                    self.loading = false;
                })
            },
            goAdd: function () {
                window.location.href = this.ctx + '/sys/backTeacherExpansion/expertForm';
            },
            goEdit: function (row) {
                window.location.href = this.ctx + '/sys/backTeacherExpansion/expertForm?user.id=' + row.user.id;
            },

            batchDelete: function () {
                var self = this, canDeleteName = [], canDeleteIds = [], cannotDeleteName = [], flag = true, tip = '';
                if (this.multipleSelectedId.length == 0) {
                    this.$message({
                        message: '请选择需要删除的专家！',
                        type: 'warning'
                    });
                    return false;
                }
                this.multipleSelection.forEach(function (item) {
                    if (item.proNameList && item.proNameList.length > 0) {
                        cannotDeleteName.push(item.user.name);
                        flag = false;
                    } else {
                        canDeleteName.push(item.user.name);
                        canDeleteIds.push(item.user.id);
                    }
                });
                tip = '此操作将永久删除专家, 是否继续?';
                if (!flag) {
                    tip = cannotDeleteName.join('、') + '当前有评审任务，不能删除！' + (canDeleteName.length > 0 ? canDeleteName.join('、') + '将会删除，是否继续？' : '请重新选择！');
                }
                this.$confirm(tip, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    if (canDeleteIds.length == 0) {
                        return false;
                    }
                    var path = {
                        method: 'GET',
                        url: '/sys/backTeacherExpansion/deleteBatchByUser',
                        params: {
                            ids: canDeleteIds.join(','),
                            type: '1'
                        }
                    };
                    self.axiosRequest(path, true, '批量删除');
                }).catch(function () {

                })
            },

            singleDelete: function (row) {
                var self = this;
                if (row.proNameList && row.proNameList.length > 0) {
                    this.$message({
                        message: '该专家当前有评审任务，不能删除',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('此操作将永久删除专家, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
//                    var path = {method: 'POST', url: '/sys/expert/delExpert', data: {id: row.user.id}};
                    var path = {
                        method: 'GET',
                        url: '/sys/backTeacherExpansion/deleteBatchByUser',
                        params: {
                            ids: row.user.id,
                            type: '1'
                        }
                    };
                    self.axiosRequest(path, true, '删除');
                }).catch(function () {

                })
            },
            axiosRequest: function (path, showMsg, msg) {
                var self = this;
                this.loading = true;
                this.$axios(path).then(function (response) {
                    var data = response.data;
                    if (data.status == '1' || data.ret == '1') {
                        self.getDataList();
                        if (showMsg) {
                            self.$message({
                                message: msg + '成功',
                                type: 'success'
                            });
                        }
                    } else {
                        self.$message({
                            message: data.msg || msg + '失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                })
            },
            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].user.id);
                }
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
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

            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.officeList = data.data || [];
                    }
                })
            },

            getDictLists: function () {
                var dicts = {
                    'master_type': 'masterTypes',
                    'postTitle_type': 'expertJobNames',
                    'expert_source': 'expertSources'
                };
                this.getBatchDictList(dicts)
            }
        },
        created: function () {
            this.getRoleList();
            this.getDataList();

            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        },
        mounted: function () {
            this.getDictLists();
            this.getOfficeList();
        }
    })

</script>
</body>
</html>