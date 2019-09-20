<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 user-share">
    <edit-bar></edit-bar>
    <div class="mgb-20">
        <div class="conditions">
            <e-condition type="radio" label="导师来源" :options="masterTypes" name="teachertype"
                         v-model="searchListForm['teachertype']" @change="searchTeacherList"></e-condition>
            <e-condition type="radio" label="学位" :options="degreeTypes" name="user.degree"
                         v-model="searchListForm['user.degree']" @change="searchTeacherList"></e-condition>
            <e-condition label="职称" type="radio" v-model="searchListForm.technicalTitle"
                         name="technicalTitle" :options="expertJobNames" @change="searchTeacherList">
            </e-condition>
            <e-condition type="checkbox" label="服务意向" :options="masterHelps" name="serviceIntentionIds"
                         v-model="searchListForm['serviceIntentionIds']" @change="searchTeacherList"></e-condition>
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
                                <el-radio v-for="item in masterTypesRadio" :key="item.value" :label="item.value">
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

                    <el-button slot="reference" size="mini" type="primary" icon="iconfont icon-zhuanhuan">批量转换身份
                    </el-button>
                </el-popover>

                <el-button type="primary" size="mini" icon="el-icon-circle-plus" @click.stop.prevent="goToAddTeacher">添加
                </el-button>
                <el-tooltip class="item" content="请选择需要删除的学生" :disabled="multipleSelectionTeachers.length > 0"
                            popper-class="white" placement="bottom">
                    <span>
                <el-button type="primary" size="mini" icon="el-icon-delete" :disabled="multipleSelectionTeachers == 0"
                           @click.stop="confirmDelTeachers">批量删除
                </el-button>
                        </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <el-input size="mini" name="keyWords" v-model="searchListForm.keyWords" placeholder="职工号/姓名/职务"
                          @keyup.enter.native="getTeacherList" class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getTeacherList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div v-loading="teacherListLoading" class="table-container">
        <el-table :data="teacherList" size="small" class="table" @selection-change="handleSelectionTeachers"
                  @sort-change="handleSortTeacher">
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column prop="u.no" label="导师信息" align="left" width="320" sortable="u.no">
                <template slot-scope="scope">
                    <div class="user-element">
                        <div class="user-img">
                            <img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                        </div>
                        <div class="user-detail">
                            <div class="user-name">
                                <img src="${ctxImages}/user-name.png" alt="">
                                <a :href="frontOrAdmin + '/sys/backTeacherExpansion/teacherView?id=' + scope.row.id"
                                   class="underline-pointer">{{scope.row.user.name || '-'}}</a>
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
                            <i class="iconfont icon-dibiao2"
                               style="width:20px;vertical-align: bottom;display: inline-block;text-align: center;"></i>
                            {{scope.row.user | getUserCollege(collegeEntries)}}
                            </span>
                                </el-tooltip>
                            </div>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="导师来源" align="center">
                <template slot-scope="scope">
                    {{scope.row.teachertype | selectedFilter(masterTypeEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="当前指导" align="center">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.curJoin" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.curJoin}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="服务意向" align="center">
                <template slot-scope="scope">
                    {{scope.row.serviceIntentionStr}}
                </template>
            </el-table-column>
            <el-table-column label="职务" align="center">
                <template slot-scope="scope">
                    {{scope.row.postTitle}}
                </template>
            </el-table-column>
            <el-table-column label="职称" align="center">
                <template slot-scope="scope">
                    {{scope.row.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
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
                                    <el-radio v-for="item in masterTypesRadio" :key="item.value"
                                              :label="item.value">
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
                                       @click.stop="goToEditTeacher(scope.row)">
                            </el-button>
                        </span>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop="confirmDelTeacher(scope.row)">
                            </el-button>
                        </span>
                    </el-tooltip>
                </template>
            </el-table-column>
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
                    layout="total, prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
</div>


<script type="text/javascript">
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                searchListForm: {
                    teachertype: '',
                    'user.education': '',
                    'user.degree': '',
                    'workUnitType': '',
                    'serviceIntentionIds': [],
                    'curJoinStr': [],
                    'keyWords': '',
                    'pageSize': 10,
                    'pageNo': 1,
                    'orderBy': '',
                    'orderByType': '',
                    'technicalTitle': ''
                },
                teacherList: [],
                pageCount: 0,
                multipleSelectionTeachers: [],
                multipleSelectedId: [],
                masterTypes: [],
                enducationLevels: [],
                degreeTypes: [],
                masterHelps: [],
                expertJobNames: [],
                expertSources: [],
                officeList: [],
                messageTip: '${message}',
                teacherListLoading: false,


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
            masterTypesRadio: function () {
                var masterTypes = this.masterTypes;
                return [{label: '无', value: ''}].concat(masterTypes)
            },

            expertTypes: function () {
                var names = this.hideRoleNames;
                return this.expertSources.filter(function (item) {
                    return names.indexOf(item.id) > -1
                })
            },
            masterTypeEntries: {
                get: function () {
                    return this.getEntries(this.masterTypes);
                }
            },
            enducationLevelEntires: {
                get: function () {
                    return this.getEntries(this.enducationLevels);
                }
            },
            expertJobNamesEntries: function () {
                return this.getEntries(this.expertJobNames);
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
                            self.getTeacherList();
                        }
                        self.batchPopoverVisible = false;
                        self.$message({
                            message: data.status == '1' ? data.msg || '转换身份成功' : data.msg || '转换身份失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.batchPopoverVisible = false;
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'warning'
                        });
                    })
                }).catch(function () {
                    row.popoverVisible = false;
                });
            },
            showBatchChangeRole: function () {
                if (this.multipleSelectedId.length == 0) {
                    this.batchPopoverVisible = false;
                    this.$message({
                        message: '请选择导师！',
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
                        url: '/sys/backTeacherExpansion/changeExpertOrTea',
                        data: self.roleForm
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getTeacherList();
                        }
                        row.popoverVisible = false;
                        self.$message({
                            message: data.status == '1' ? '转换身份成功' : data.msg || '转换身份失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        row.popoverVisible = false;
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'warning'
                        });
                    })
                }).catch(function () {
                    row.popoverVisible = false;
                });
            },

            handleSelectionTeachers: function (value) {
                this.multipleSelectionTeachers = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].user.id);
                }
            },
            handleSortTeacher: function (row) {
                var order = null;
                if (row.order) {
                    order = row.order.indexOf('asc') > -1 ? 'asc' : 'desc';
                }
                this.searchListForm.orderByType = order;
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.pageNo = 1;
                this.getTeacherList();
            },

            searchTeacherList: function () {
                this.searchListForm.pageNo = 1;
                this.getTeacherList();
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getTeacherList();
            },
            handlePCPChange: function () {
                this.getTeacherList();
            },

            goToEditTeacher: function (row) {
                location.href = this.ctx + '/sys/backTeacherExpansion/form?user.id=' + row.user.id;
            },

            goToAddTeacher: function () {
                location.href = this.ctx + '/sys/backTeacherExpansion/form?operateType=1';
            },


            confirmDelTeachers: function () {
                var self = this, canDeleteName = [], canDeleteIds = [], cannotDeleteName = [], flag = true, tip = '';
                this.multipleSelectionTeachers.forEach(function (item) {
                    if (item.curJoin && item.curJoin.split(',').length > 0) {
                        cannotDeleteName.push(item.user.name);
                        flag = false;
                    } else {
                        canDeleteName.push(item.user.name);
                        canDeleteIds.push(item.user.id);
                    }
                });
                tip = '此操作将永久删除导师, 是否继续?';
                if (!flag) {
                    tip = cannotDeleteName.join('、') + '当前有指导任务，不能删除！' + (canDeleteName.length > 0 ? canDeleteName.join('、') + '将会删除，是否继续？' : '请重新选择！');
                }
                this.$confirm(tip, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    if (canDeleteIds.length == 0) {
                        return false;
                    }
                    self.delBatchTeachers(canDeleteIds);
                }).catch(function () {

                })
            },
            delBatchTeachers: function (ids) {
                var self = this;
                this.$axios.get('/sys/backTeacherExpansion/deleteBatchByUser?ids=' + ids.join(',') + '&type=0').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        self.$message({
                            type: 'success',
                            message: data.msg || '删除导师信息成功'
                        });
                        self.getTeacherList();
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    })
                })
            },

            confirmDelTeacher: function (row) {
                var self = this;
                if (row.curJoin && row.curJoin.split(',').length > 0) {
                    this.$message({
                        message: '该导师当前有指导任务，不能删除',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('此操作将删除这位导师，是否继续', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delTeacher(row.user.id);
                }).catch(function () {
                })
            },

            delTeacher: function (id) {
                var self = this;
                this.$axios.get('/sys/backTeacherExpansion/deleteBatchByUser?ids=' + id + '&type=0').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        self.$message({
                            type: 'success',
                            message: data.msg || '删除导师信息成功'
                        });
                        self.getTeacherList();
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    })
                })
            },
            getTeacherList: function () {
                var self = this;
                this.teacherListLoading = true;
                this.$axios('/sys/backTeacherExpansion/getTeacherList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    var pageData;
                    if (data.status === 1) {
                        data = data.data;
                        pageData = data.page;
                        self.pageCount = pageData.count;
                        var list = pageData.list || [];
                        list.forEach(function (item) {
                            item.popoverVisible = false;
                        });
                        self.teacherList = list;
                    }
                    self.teacherListLoading = false;
                }).catch(function (error) {
                    self.teacherListLoading = false;
                })
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
                    'expert_source': 'expertSources',
                    'enducation_level': 'enducationLevels',
                    'degree_type': 'degreeTypes',
                    'master_help': 'masterHelps'
                };
                this.getBatchDictList(dicts)
            }

        },

        mounted: function () {
            this.getTeacherList();
            this.getDictLists();
            this.getRoleList();
            this.getOfficeList();

            if (this.messageTip) {
                this.$msgbox({
                    title: '提示',
                    message: this.messageTip,
                    type: this.messageTip.indexOf('成功') > -1 ? 'success' : 'error'
                });
                this.messageTip = '';
            }

        }
    })


</script>

</body>
</html>