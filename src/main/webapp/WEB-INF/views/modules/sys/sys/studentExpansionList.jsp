<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 user-share">
    <edit-bar></edit-bar>
    <div class="mgb-20">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm['user.office.id']" label="学院"
                         :options="collegeList"
                         :default-props="{label: 'name', value: 'id'}" @change="getStudentList"
                         name="proModel.deuser.office.id"></e-condition>
            <e-condition type="radio" label="学生现状" :options="currentStates" name="currState"
                         v-model="searchListForm['currState']" @change="getStudentList"></e-condition>
            <e-condition type="radio" label="学历" :options="educationLevels" name="user.education"
                         v-model="searchListForm['user.education']" @change="getStudentList"></e-condition>
            <e-condition type="radio" label="学位" :options="degreeTypes" name="user.degree"
                         v-model="searchListForm['user.degree']" @change="getStudentList"></e-condition>
            <e-condition type="radio" label="当前在研" :options="curJoins" name="curJoinStr"
                         v-model="searchListForm['curJoinStr']" @change="getStudentList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" size="mini" icon="el-icon-circle-plus" @click.stop="goToAddStudent">添加
                </el-button>
                <el-tooltip class="item" content="请选择需要删除的学生" :disabled="multipleSelectionStudent.length > 0"
                            popper-class="white" placement="bottom">
                    <span>
                <el-button type="primary" icon="el-icon-delete" :disabled="multipleSelectionStudent.length === 0"
                           size="mini"
                           @click.stop="confirmDelStudents">批量删除
                </el-button>
                        </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <input type="text" style="display: none">
                <el-input size="mini" name="myFind" v-model="searchListForm.myFind" placeholder="关键字"
                          class="w300" @keyup.enter.native="getStudentList">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getStudentList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div v-loading="tableLoading" class="table-container">
        <el-table :data="studentList" size="small" class="table" @selection-change="handleSelectionStudents"
                  @sort-change="handleSortStudent">
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column prop="u.no" align="left" width="250" label="学生信息" sortable="u.no">
                <template slot-scope="scope">
                    <div class="user-element">
                        <div class="user-img">
                            <img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                        </div>
                        <div class="user-detail">
                            <div class="user-name">
                                <el-tooltip :content="scope.row.user.name || '-'" popper-class="white"
                                            placement="right">
                                    <span class="break-ellipsis">
                                        <img src="${ctxImages}/user-name.png" alt="">
                                        <a :href="frontOrAdmin + '/sys/studentExpansion/view?id=' + scope.row.id"
                                           class="underline-pointer">{{scope.row.user.name || '-'}}</a>
                                    </span>
                                </el-tooltip>
                            </div>
                            <div class="user-tag">
                                <el-tooltip :content="scope.row.user.no || '-'" popper-class="white" placement="right">
                                    <span class="break-ellipsis">
                                        <img src="${ctxImages}/user-no.png" alt="">
                                        {{scope.row.user.no || '-'}}
                                    </span>
                                </el-tooltip>
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
            <el-table-column label="联系方式" prop="u.mobile" align="center" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.user.mobile}}
                </template>
            </el-table-column>
            <el-table-column label="性别" align="center" min-width="60">
                <template slot-scope="scope">
                    {{scope.row.user.sex | selectedFilter(sexEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="当前在研" align="center" min-width="90">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.curJoin" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.curJoin}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="现状" align="center" min-width="70">
                <template slot-scope="scope">
                    {{scope.row.currState | selectedFilter(currentStateEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="学历" align="center" min-width="70">
                <template slot-scope="scope">
                    {{scope.row.user.education | selectedFilter(educationEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="学位" align="center" min-width="70">
                <template slot-scope="scope">
                    {{scope.row.user.degree | selectedFilter(degreeEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="毕业时间" align="center" min-width="90">
                <template slot-scope="scope">
                    {{scope.row.graduationStr}}
                </template>
            </el-table-column>
            <el-table-column label="休学时间" align="center" min-width="90">
                <template slot-scope="scope">
                    {{scope.row.temporaryDateStr}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="90">
                <template slot-scope="scope">
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   @click.stop="changeStudent(scope.row.id)">
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop="confirmDelStu(scope.row.id)">

                        </el-button>
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
            var curJoins = JSON.parse(JSON.stringify(${fns: toJson(fns:getPublishDictList())}));

            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    'orderBy': '',
                    'orderByType': '',
                    'user.office.id': '',
                    'user.education': '',
                    'user.degree': '',
                    'curJoinStr': '',
                    'currState': '',
                    'myFind': ''
                },
                officeList: [],
                currentStates: [],
                isUnderStudy: [],
                educationLevels: [],
                degreeTypes: [],
                curJoins: curJoins,
                sexes: [],
                multipleSelectionStudent: [],
                studentList: [],
                pageCount: 0,
                tableLoading: true,
            }
        },
        computed: {
            collegeList: {
                get: function () {
                    return this.officeList.filter(function (item) {
                        return item.grade === '2';
                    });
                }
            },
            currentStateEntries: function () {
                return this.getEntries(this.currentStates)
            },
            educationEntries: function () {
                return this.getEntries(this.educationLevels)
            },
            degreeEntries: function () {
                return this.getEntries(this.degreeTypes)
            },
            sexEntries: function () {
                return this.getEntries(this.sexes)
            },
            collegeEntries: function () {
                var officeList = this.officeList;
                var i = 0;
                var entity = {};
                while (i < officeList.length) {
                    var item = officeList[i];
                    entity[item.id] = item;
                    i++;
                }
                return entity;
            }
        },
        methods: {


            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getStudentList();
            },

            handlePCPChange: function () {
                this.getStudentList();
            },
            //全选学生
            handleSelectionStudents: function (value) {
                this.multipleSelectionStudent = value
            },

            //排序学生
            handleSortStudent: function (obj) {
                this.searchListForm.orderBy = obj.prop;
                this.searchListForm.orderByType = obj.order ? (obj.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getStudentList();
            },

            changeStudent: function (id) {
                location.href = this.ctx + '/sys/studentExpansion/form?id=' + id;
            },

            goToAddStudent: function () {
                location.href = this.ctx + '/sys/studentExpansion/form';
            },

            //确认批量删除学生
            confirmDelStudents: function () {
                var self = this;
                this.$confirm('此操作将删除被选中的学生，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delStues()
                }).catch(function () {

                })
            },

            delStues: function () {
                var self = this;
                var ids = this.multipleSelectionStudent.map(function (item) {
                    return item.id;
                })
                this.$axios.get('/sys/studentExpansion/deleteBatch?ids=' + ids.join(',')).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getStudentList();
                        self.$alert('操作成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            confirmDelStu: function (id) {
                var self = this;
                this.$confirm('此操作将删除该学生，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delStu(id)
                }).catch(function () {

                })
            },

            delStu: function (id) {
                var self = this;
                this.$axios.get('/sys/studentExpansion/deleteStu?id=' + id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getStudentList();
                        self.$alert('操作成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            //获取学生列表
            getStudentList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/sys/studentExpansion/getStudentList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data;
                        if (pageData) {
                            self.studentList = pageData.list || [];
                            self.pageCount = pageData.count;
                            self.searchListForm.pageSize = pageData.pageSize || 10;
                            self.searchListForm.pageNo = pageData.pageNo || 1;
                        }
                    }
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.tableLoading = false;
                })
            },

            getDictLists: function () {
                var dicts = {
                    'current_sate': 'currentStates',
                    'yes_no': 'isUnderStudy',
                    'enducation_level': 'educationLevels',
                    'degree_type': 'degreeTypes',
                    'sex': 'sexes'
                };
                this.getBatchDictList(dicts)
            },

            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.officeList = data.data || []
                    }
                })
            }
        },
        created: function () {
            this.getDictLists();
        },

        mounted: function () {
            this.getStudentList();
            this.getOfficeList();
        }
    })

</script>
</body>
</html>