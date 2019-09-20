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
        <el-breadcrumb-item>课程学分认定</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="mgb-20">
        <div class="conditions">
            <e-condition type="radio" label="课程性质" :options="scoCourseNatures"
                         v-model="searchListForm['scoCourse.nature']" @change="getScoApplyList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <el-date-picker
                        v-model="createDateRange"
                        type="daterange"
                        range-separator="至"
                        size="mini"
                        clearable
                        class="w300"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="defaultTime"
                        @change="handleChangeCreateDateRange"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
                <input type="text" style="display: none">
                <el-input v-model="searchListForm.keyword" size="mini" placeholder="请输入课程代码或者课程名称" class="w300"
                          @keyup.enter.native="getScoApplyList">
                    <el-button slot="append" icon="el-icon-search" @click.stop="getScoApplyList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container">
        <el-table :data="scoApplyList" size="mini" class="table">
            <el-table-column width="286" label="课程信息">
                <template slot-scope="scope">
                    <%--<a href="#">{{scope.row.scoCourse.name}}</a>--%>
                    <credit-course-column :course="scope.row.scoCourse"></credit-course-column>
                </template>
            </el-table-column>
            <el-table-column label="计划学分" align="center">
                <template slot-scope="scope">{{scope.row.scoCourse.planScore | hideZero}}</template>
            </el-table-column>
            <el-table-column label="计划课时" align="center">
                <template slot-scope="scope">{{scope.row.scoCourse.planTime | hideZero}}</template>
            </el-table-column>
            <el-table-column label="合格成绩" align="center">
                <template slot-scope="scope">{{scope.row.scoCourse.overScore | hideZero}}</template>
            </el-table-column>
            <el-table-column label="实际学时" align="center">
                <template slot-scope="scope">{{scope.row.realTime | hideZero}}</template>
            </el-table-column>
            <el-table-column label="实际成绩" align="center">
                <template slot-scope="scope">{{scope.row.realScore | hideZero}}</template>
            </el-table-column>
            <el-table-column label="认定学分" align="center">
                <template slot-scope="scope">{{scope.row.score | hideZero}}</template>
            </el-table-column>
            <el-table-column label="申请日期" align="center">
                <template slot-scope="scope">{{scope.row.applyDate | formatDateFilter('YYYY-MM-DD')}}</template>
            </el-table-column>
            <el-table-column label="审核状态" align="center">
                <template slot-scope="scope">
                    <template v-if="notApply.indexOf(scope.row.auditStatus) > -1">{{scope.row.auditStatus |
                        selectedFilter(auditStatueEntries)}}
                    </template>
                    <template v-else>-</template>
                </template>
            </el-table-column>
            <el-table-column v-if="isStudent" label="操作" align="center" width="120" v-if="isStudent">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button v-if="scope.row.auditStatus == '4'" type="text" size="mini"
                                   @click.stop="goToApply(scope.row)">重新申请
                        </el-button>
                        <el-button v-if="notApply.indexOf(scope.row.auditStatus) == -1" type="text" size="mini"
                                   @click.stop="goToApply(scope.row)">申请学分
                        </el-button>
                        <el-button type="text" size="mini" @click.stop="goToView(scope.row)">查看</el-button>
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
                scoApplyList: [],
                scoCourseTypes: [],
                scoCourseNatures: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    keyword: '',
                    beginDate: '',
                    endDate: '',
                    'scoCourse.nature': ''
                },
                pageCount: 10,
                notApply: ['3', '4', '2'],
                auditStatues: [],
                createDateRange: [],
                defaultTime: ['00:00:00', '23:59:59'],
            }
        },
        computed: {
            scoCourseTypeEntries: function () {
                return this.getEntries(this.scoCourseTypes)
            },
            scoCourseNatureEntries: function () {
                return this.getEntries(this.scoCourseNatures)
            },
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
        },
        methods: {
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScoApplyList();
            },

            handleChangeCreateDateRange: function (value) {
                if (value && value.length > 0) {
                    this.searchListForm.beginDate = value[0];
                    this.searchListForm.endDate = value[1];
                } else {
                    this.searchListForm.beginDate = '';
                    this.searchListForm.endDate = '';
                }
                this.getScoApplyList();
            },

            handlePaginationPageChange: function (value) {
                this.getScoApplyList();
            },
            getScoApplyList: function () {
                var self = this;
                this.$axios.get('/scoapply/getScoApplyList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        data = data.data || {};
                        self.scoApplyList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    }
                }).catch(function (error) {
                    self.scoApplyList = [];
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

            goToApply: function (row) {
                location.href = '/f/scoapply/scoApplyForm?id=' + row.id;
            },
            goToView: function (row) {
                location.href = '/f/scoapply/view?id=' + row.id;
            },

            getAuditStatues: function () {
                var self = this;
                this.$axios.get('/scr/ajaxFrontScoRstatuss?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },
        },
        created: function () {
            this.getScoApplyList();
            this.getScoCourseTypes();
            this.getScoCourseNatures();
            this.getAuditStatues();
        }
    })

</script>
</body>
</html>