<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="creative"/>
</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>学分认定</el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/scoapply/scoApplyList">课程学分认定</a></el-breadcrumb-item>
        <el-breadcrumb-item>添加认定课程</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="mgb-20">
        <el-input v-model="searchListForm.keyword" size="mini" placeholder="请输入课程代码或者课程名称" class="w300">
            <el-button slot="append" icon="el-icon-search"></el-button>
        </el-input>
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
            <el-table-column label="操作" align="center" width="120" v-if="isStudent">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" :disabled="scope.row.addabel !== '1'" size="mini"
                                   @click.stop="addCourse(scope.row)">添加
                        </el-button>
                        <el-button type="text" size="mini" @click.stop="confirmCancelCourse">取消</el-button>
                    </div>
                </template>
            </el-table-column>
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
<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                scoCourseList: [],
                scoCourseTypes: [],
                scoCourseNatures: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    keyword: ''
                },
                pageCount: 10
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
                this.$axios.get('/scoapply/findListByNameOrCode').then(function (response) {
                    var data = response.data;
                    if (data && data.length > 0) {
                        self.scoCourseList = data.map(function (item) {
                            item.addabel = '1';
                            return item;
                        })
                    }
                }).catch(function (error) {
                    self.scoCourseList = [];
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
            addCourse: function (row) {
                var self = this;
                this.$axios.post('/scoapply/saveAdd?courseId=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.success) {
                        row.addabel = '0'
                        self.$message.success(data.msg)
                    } else {
                        self.$message.error(data.msg);
                        row.addabel = data.msg.indexOf('已经被添加') > -1 ? '0' : '1'
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg)
                })
            },
            confirmCancelCourse: function () {

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