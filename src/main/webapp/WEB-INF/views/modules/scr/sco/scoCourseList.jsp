<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="text-right mgb-20">
        <el-button type="primary" size="mini" @click.stop="goToAdd">创建课程</el-button>
    </div>
    <div class="table-container">
        <el-table :data="scoCourseList" size="mini" class="table">
            <el-table-column width="286" label="课程信息">
                <template slot-scope="scope">
                    <%--<a href="#">{{scope.row.scoCourse.name}}</a>--%>
                    <credit-course-column :course="scope.row"></credit-course-column>
                </template>
            </el-table-column>
            <el-table-column label="计划学分" align="center">
                <template slot-scope="scope">{{scope.row.planScore | hideZero}}</template>
            </el-table-column>
            <el-table-column label="计划课时" align="center">
                <template slot-scope="scope">{{scope.row.planTime | hideZero}}</template>
            </el-table-column>
            <el-table-column label="合格成绩" align="center">
                <template slot-scope="scope">{{scope.row.overScore | hideZero}}</template>
            </el-table-column>
            <shiro:hasPermission name="scocourse:scoCourse:view">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button type="text" size="mini" @click.stop="goToBZ(scope.row)">设置认定标准
                            </el-button>
                            <el-button type="text" size="mini" @click.stop="goToEditScoCourse(scope.row)">编辑
                            </el-button>
                            <el-button type="text" size="mini" @click.stop="confirmDelScoCourse(scope.row)">删除
                            </el-button>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>
        </el-table>
        <div class="text-right mgb-20">
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

<script>

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                scoCourseList: [],
                scoCourseTypes: [],
                scoCourseNatures: [],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0
            }
        },
        computed: {
            scoCourseTypeEntries: function () {
                return this.getEntries(this.scoCourseTypes)
            },
            scoCourseNatureEntries: function () {
                return this.getEntries(this.scoCourseNatures)
            }
        },
        methods: {
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScoCourseList();
            },

            handlePaginationPageChange: function (value) {
                this.getScoCourseList();
            },
            getScoCourseList: function () {
                var self = this;
                this.$axios.get('/sco/scoCourse/getScoCourseList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.scoCourseList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                })
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
            goToBZ: function (row) {
                location.href = this.frontOrAdmin + '/sco/scoAffirmCriterionCouse/form?foreign_id=' + row.id;
            },
            goToEditScoCourse: function (row) {
                location.href = this.frontOrAdmin + '/sco/scoCourse/form?id=' + row.id;
            },
            goToAdd: function () {
                location.href = this.frontOrAdmin + '/sco/scoCourse/form';
            },
            confirmDelScoCourse: function (row) {
                var self = this;
                this.$confirm('确认要删除该条课程学分标准吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).catch(function () {

                }).then(function () {
                    self.delScoCourse(row);
                })
            },
            delScoCourse: function (row) {
                var self = this;
                this.$axios.get('/sco/scoCourse/delScoCourse?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getScoCourseList();
                        self.$message.success('删除成功')
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg)
                })
            }
        },
        created: function () {
            this.getScoCourseList();
            this.getScoCourseTypes();
            this.getScoCourseNatures();
        }
    })

</script>
</body>
</html>