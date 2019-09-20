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
        </div>
        <div class="search-block_bar clearfix">
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
        <el-table :data="scoCourseGradeList" size="mini" class="table"
                  ref="multipleTable">
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
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini" @click.stop="goToView(scope.row)">查看</el-button>
                        <el-button type="text" size="mini" @click.stop="confirmDel(scope.row)">删除</el-button>
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
                searchListForm: {
                    'office.id': '',
                    'scoCourse.type': '',
                    'scoApply.auditStatus': '3',
                    keyWord: '',
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                scoCourseGradeList: [],
                multipleSelection: [],
                delAbles: ['4']
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
            }
        },
        methods: {
            goToView: function (row) {
                location.href = '/a/scoapply/view?id=' + row.scoApply.id;
            },

            confirmDel: function (row) {
                var self = this;
                this.$confirm('是否删除该条课程学分记录', '提示', {
                    type: 'warning',
                    confirmButtonText: '确定',
                    cancelButtonText: '取消'
                }).then(function () {
                    self.delCourse(row);
                }).catch(function () {

                })
            },

            delCourse: function (row) {
                var self = this;
                this.$axios.get('/scoapply/ajaxDeleteScoRapply?id=' + row.scoApply.id+'&creditType=2').then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        self.$message({
                            type: 'success',
                            message: '删除成功'
                        });
                        self.getScoCourseGradeList();
                    }else {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScoCourseGradeList();
            },

            handlePaginationPageChange: function (value) {
                this.getScoCourseGradeList();
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
            }
        },
        created: function () {
            this.getScoCourseGradeList();
            this.getScoCourseTypes();
            this.getScoCourseNatures();
        }
    })

</script>
</body>
</html>