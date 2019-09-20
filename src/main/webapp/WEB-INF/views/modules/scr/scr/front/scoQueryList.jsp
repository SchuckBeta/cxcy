<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>学分认定</el-breadcrumb-item>
        <el-breadcrumb-item>学分查询</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="table-container">
        <el-table :data="scoApplyList" size="mini" class="table" @sort-change="handleSortScoQueryList">
            <el-table-column prop="className" label="学分类型" align="center" sortable="className"></el-table-column>
            <el-table-column prop="creditName" label="学分类别" align="center" sortable="creditName">
                <template slot-scope="scope">{{scope.row.creditName}}</template>
            </el-table-column>
            <el-table-column prop="name" label="认定内容及标准" width="210" sortable="name">
                <template slot-scope="scope">
                    <el-tooltip v-if="scope.row.typeName" class="item" popper-class="white" effect="dark" :content="scope.row.typeName"
                                placement="right"><span class="break-ellipsis">{{scope.row.typeName}}</span></el-tooltip>
                    <div>
                        {{scope.row.name}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="credit" label="认定学分" align="center" sortable="credit">
                <template slot-scope="scope">
                    {{scope.row.credit}}
                </template>
            </el-table-column>
            <el-table-column prop="applyDate" label="申请日期" align="center" sortable="applyDate">
                <template slot-scope="scope">
                    {{scope.row.applyDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column prop="projectName" label="名称" align="center">
                <template slot-scope="scope">
                    {{scope.row.projectName}}
                </template>
            </el-table-column>
            <el-table-column prop="m.status" label="状态" align="center" sortable="m.status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(auditStatueEntries)}}
                </template>
            </el-table-column>
            <el-table-column v-if="isStudent" label="操作" align="center">
                <template slot-scope="scope">
                    <div v-if="scope.row.creditType === '2'" class="table-btns-action">
                        <template v-if="scope.row.status === '4' && loginCurUser.id === scope.row.userId">
                            <el-button type="text" size="mini" @click.stop="goToForm(scope.row)">重新提交</el-button>
                            <br>
                        </template>
                        <el-button type="text" size="mini" @click.stop="goToView(scope.row)">查看</el-button>
                        <el-button v-if="loginCurUser.id === scope.row.userId && scope.row.status === '4'" type="text" size="mini"
                                   @click.stop="confirmDelScoApply(scope.row)">删除
                        </el-button>
                    </div>
                    <div v-else class="table-btns-action">
                        <template v-if="scope.row.status == '4' && loginCurUser.id === scope.row.userId">
                            <el-button type="text" size="mini" @click.stop="goToForm(scope.row)">重新提交</el-button>
                            <br>
                        </template>
                        <el-button type="text" size="mini" @click.stop="goToView(scope.row)">查看</el-button>
                        <el-button v-if="loginCurUser.id === scope.row.userId && scope.row.status == '4'" type="text" size="mini"
                                   @click.stop="confirmDelScoApply(scope.row)">删除
                        </el-button>
                    </div>
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

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                scoApplyList: [],
                scoRuleList: [],
                auditStatues: [{key: 1, name: '审核通过'}],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    'orderBy': '',
                    'orderByType': '',
                },
                pageCount: 0
            }
        },
        computed: {
            scoRuleListEntries: function () {
                var scoRuleList = this.scoRuleList;
                var i = 0;
                var entries = {};
                while (i < scoRuleList.length) {
                    var item = scoRuleList[i];
                    entries[item.id] = item.name;
                    i++;
                }
                return entries;
            },
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            }
        },
        methods: {
            confirmDelScoApply: function (row) {
                var self = this;
                this.$confirm('确认删除这条学分申请记录吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delScoApply(row)
                }).catch(function () {

                })
            },

            delScoApply: function (row) {
                var self = this;
                this.$axios.get('/scr/ajaxDeleteScoRapply?id=' + row.id+'&creditType='+ row.creditType).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getScoQueryList();
                        self.$message.success('删除成功');
                    } else {
                        self.$message.error(data.msg);
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            goToForm: function (row) {
                if(row.creditType === '1'){
                    location.href = '/f/scr/toFrontScrApplyForm?id=' + row.id + '&creditType=' + row.creditType;
                }else {
                    location.href = '/f/scoapply/scoApplyForm?id=' + row.id + '&creditType=' + row.creditType;
                }

            },

            goToView: function (row) {
//                location.href = '/f/scr/toFrontScrApplyForm?id=' + row.id+'&creditType='+row.creditType;
                if(row.creditType === '1'){
                    location.href = '/f/scr/toFrontScoDetailForm?id=' + row.id + '&creditType=' + row.creditType;
                }else {
                    location.href = '/f/scoapply/view?id=' + row.id + '&creditType=' + row.creditType;
                }

            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScoQueryList();
            },

            handlePCPChange: function () {
                this.getScoQueryList();
            },
            //排序学生
            handleSortScoQueryList: function (obj) {
                this.searchListForm.orderBy = obj.prop;
                this.searchListForm.orderByType = obj.order ? (obj.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getScoQueryList();
            },

            getScoRuleList: function () {
                var self = this;
                this.$axios.get('/scr/fronScoRuleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRuleList = data.data || [];
                    }
                })
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

            getScoQueryList: function () {
                var self = this;
                this.$axios.get('/scr/ajaxFindScoList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.scoApplyList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    }
                })
            }
        },
        created: function () {
            this.getScoQueryList();
//            this.getScoRuleList();
            this.getAuditStatues();
        }
    })

</script>
</body>
</html>