<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm['office.id']" label="学院"
                         :options="colleges" :default-props="{label: 'name', value: 'id'}"
                         @change="getScoCourseGradeList"></e-condition>
            <e-condition type="radio" v-model="searchListForm['scoCourse.type']" label="课程类型" :options="scoCourseTypes"
                         name="type" @change="getScoCourseGradeList"></e-condition>
            <e-condition type="radio" v-model="searchListForm['scoApply.auditStatus']" label="审核状态"
                         :options="cpAuditStatues" :default-props="{label: 'name', value: 'key'}" @change="getScoCourseGradeList"
                         name="status"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-tooltip content="请勾选需要审核课程" :disabled="multipleSelection.length > 0" popper-class="white"
                            placement="top"><span><el-button :disabled="multipleSelection.length === 0" type="primary"
                                                             size="mini"
                                                             @click.stop="auditCourse">批量通过</el-button></span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <input type="text" style="display: none">
                <el-input v-model="searchListForm.keyWord" size="mini" @keyup.enter.native="getScoCourseGradeList"
                          class="w300" placeholder="学号/课程代码/课程名称">
                    <el-button slot="append" icon="el-icon-search" @click.stop="getScoCourseGradeList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="scoCourseGradeList" size="mini" class="table" @selection-change="handleSelectionChange"
                  ref="multipleTable">
            <el-table-column
                    type="selection" :selectable="selectable"
                    width="60">
            </el-table-column>
            <el-table-column prop="user" width="286" label="申请人信息">
                <template slot-scope="scope">
                    <credit-user-column :user="scope.row.user"></credit-user-column>
                </template>
            </el-table-column>
            <el-table-column prop="scoCourse" width="286" label="课程信息">
                <template slot-scope="scope">
                    <credit-course-column :course="scope.row.scoCourse"></credit-course-column>
                </template>
            </el-table-column>
            <%--<el-table-column prop="scoCourse.nature" label="课程性质" align="center">--%>
            <%--<template slot-scope="scope">--%>
            <%--{{scope.row.scoCourse.nature | selectedFilter(scoCourseNatureEntries)}}--%>
            <%--</template>--%>
            <%--</el-table-column>--%>
            <%--<el-table-column prop="scoCourse.type" label="课程类型" align="center">--%>
            <%--<template slot-scope="scope">--%>
            <%--{{scope.row.scoCourse.type | selectedFilter(scoCourseTypeEntries)}}--%>
            <%--</template>--%>
            <%--</el-table-column>--%>
            <el-table-column prop="scoApply.realTime" label="完成课时" align="center">
                <template slot-scope="scope">
                    {{scope.row.scoApply.realTime | hideZero}}
                </template>
            </el-table-column>
            <el-table-column prop="scoApply.realScore" label="成绩" align="center">
                <template slot-scope="scope">
                    {{scope.row.scoApply.realScore | hideZero}}
                </template>
            </el-table-column>
            <el-table-column prop="scoCourse.planScore" label="计划学分" align="center">
                <template slot-scope="scope">
                    {{scope.row.scoCourse.planScore | hideZero}}
                </template>
            </el-table-column>
            <el-table-column prop="scoCourse.score" label="认定学分" align="center">
                <template slot-scope="scope">
                    {{scope.row.scoApply.score | hideZero}}
                </template>
            </el-table-column>
            <el-table-column prop="scoApply.auditStatus" label="审核状态" align="center">
                <template slot-scope="scope">
                    {{scope.row.scoApply.auditStatus | selectedFilter(auditStatueEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini" @click.stop="goToAudit(scope.row)" v-if="scope.row.scoApply.auditStatus == '2'">审核
                        </el-button>
                        <el-button type="text" size="mini" @click.stop="goToView(scope.row)">查看</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20" v-if="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
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
            var officeList = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            return {
                officeList: officeList,
                scoCourseTypes: [],
                scoCourseNatures: [],
                auditStatues: [],
                searchListForm: {
                    'office.id': '',
                    'scoCourse.type': '',
                    'scoApply.auditStatus': '',
                    keyWord: '',
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                scoCourseGradeList: [],
                multipleSelection: [],
            }
        },
        computed: {
            scoCourseTypeEntries: function () {
                return this.getEntries(this.scoCourseTypes)
            },
            scoCourseNatureEntries: function () {
                return this.getEntries(this.scoCourseNatures)
            },
            colleges: function () {
                return this.officeList.filter(function (item) {
                    return item.grade === '2';
                })
            },
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
            cpAuditStatues: function () {
                return this.auditStatues.filter(function (item) {
                    return item.key !== '1';
                })
            }
        },
        methods: {
            auditCourse: function () {
                var self = this;
                var ids = this.multipleSelection.map(function (item) {
                    return item.scoApply.id;
                });
                this.$axios.post('/scoapply/ajaxAuditForm?ids='+ ids.join(',')).then(function (response) {
                    var data = response.data;
                    if(data.ret === 1){
                        self.$alert('审核通过', '提示', {
                            type: 'success'
                        });
                        self.getScoCourseGradeList();
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/scoapply/getCountToAudit");
                    }else {
                        self.$message.error(data.msg);
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScoCourseGradeList();
            },

            handlePaginationPageChange: function (value) {
                this.getScoCourseGradeList();
            },

            handleSelectionChange: function (val) {
                this.multipleSelection = val;
            },
            getScoCourseTypes: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=0000000102').then(function (response) {
                    self.scoCourseTypes = response.data || [];
                })
            },
            getScoCourseNatures: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=0000000108').then(function (response) {
                    self.scoCourseNatures = response.data || [];
                })
            },
            getScoCourseGradeList: function () {
                var self = this;
                this.$axios.get('/sco/scoreGrade/getScoCourseGradeList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        var scoCourseGradeList = data.list || [];
                        scoCourseGradeList = scoCourseGradeList.map(function (item) {
                            item.user.officeId = item.office.id;
                            item.selectable = item.scoApply.auditStatus === '2';
                            return item;
                        });
                        self.scoCourseGradeList = scoCourseGradeList;
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg)
                })
            },
            selectable: function (row) {
                return row.selectable
            },

            goToView: function (row) {
                location.href = '/a/scoapply/view?id=' + row.scoApply.id;
            },

            goToAudit: function (row) {
                location.href = '/a/scoapply/auditView?id=' + row.scoApply.id;
            },
            getAuditStatues: function () {
                var self = this;
                this.$axios.get('/scr/scoRapply/ajaxScoRstatuss?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },
        },
        created: function () {
            this.getScoCourseGradeList();
            this.getScoCourseTypes();
            this.getScoCourseNatures();
            this.getAuditStatues();
            window.parent.sideNavModule.changeStaticUnreadTag("/a/scoapply/getCountToAudit");
        }
    })

</script>
</body>
</html>