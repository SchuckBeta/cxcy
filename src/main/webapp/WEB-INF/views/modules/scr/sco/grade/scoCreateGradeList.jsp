<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <edit-bar>学分审核</edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
            <e-condition type="radio" label="所属学院" :options="colleges" :default-props="officeProps"
                         v-model="searchListForm['user.office.id']" @change="getScrApplyList"></e-condition>
            <e-condition type="radio" label="学分类型" :options="creditTypes"
                         v-model="searchListForm.creditType" @change="getScrApplyList"></e-condition>
            <e-condition type="radio" label="学分类别" :options="scrRuleCategories" :default-props="officeProps"
                         v-model="searchListForm['rule.id']" @change="getScrApplyList"></e-condition>
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
                        value-format="yyyy-HH-MM"
                        @change="handleChangeCreateDateRange"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
                <input type="text" style="display: none">
                <el-input size="mini" name="keyWords" v-model="searchListForm.keys" placeholder="学号/姓名/认定内容（级别/标准）/专业"
                          @keyup.enter.native="getScrApplyList" class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getScrApplyList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="scrApplyList" size="mini" class="table" @sort-change="handleSortScoApplyList">
            <el-table-column prop="user.no" width="286" label="学生信息" sortable="user.no">
                <template slot-scope="scope">
                    <credit-user-column :user="scope.row.user"></credit-user-column>
                </template>
            </el-table-column>
            <el-table-column prop="creditType" label="学分类型" sortable="creditType" align="center">
                <template slot-scope="scope">
                    {{scope.row.creditType}}
                </template>
            </el-table-column>
            <el-table-column prop="rule.id" label="学分类别" sortable="rule.id" align="center">
                <template slot-scope="scope">
                    {{scope.row.rule.id | selectedFilter(scoRuleListEntryNames)}}
                </template>
            </el-table-column>
            <el-table-column prop="rdetail.id" label="认定内容及标准" sortable="rdetail.id">
                <template slot-scope="scope">
                    <div style="min-width: 170px;">
                        <span v-if="index >= 2" v-for="(item, index) in scope.row.rdetail.rule.parentIds.split(',')">{{item | selectedFilter(scoRuleListEntryNames)}}  </span>
                        <div>
                            {{scope.row.rdetail.name}}
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="applyDate" label="申请日期" sortable="applyDate" align="center">
                <template slot-scope="scope">
                    {{scope.row.applyDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" align="center">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(auditStatueEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <template v-if="scope.row.status !== '2'">
                            <el-button v-if="scope.row.status === '1'" size="mini" type="text"
                                       @click.stop="goToAudit(scope.row)">审核
                            </el-button>
                            <el-button v-else type="text" size="mini" @click.stop="goToView(scope.row)">查看详情</el-button>
                        </template>
                        <el-button v-else size="mini" type="text" @click.stop="confirmDelScrApply(scope.row)">删除
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
                searchListForm: {
                    'user.office.id': '',
                    creditType: '',
                    'rule.id': '',
                    pageSize: 10,
                    pageNo: 1,
                    createDate: '',
                    createQDate: '',
                    keys: ''
                },
                pageCount: 0,
                createDateRange: [],
                officeList: [],
                creditTypes: [],
                scoRuleList: [],
//                scoRuleDetailList: [],
                auditStatues: [{key: 1, name: '审核通过'}],
                scrApplyList: [
                    {
                        id: '123311111',
                        rule: {
                            id: '63f825a2cbe844db84f78e2a90e94782',
                        },
                        rdetail: {
                            id: '62b09c40fc024631bd4badc967cc8647',
                            name: '一类学术期刊',
                            rule: {
                                id: '76631360bd5c433fb63a52915d6bebc5',
                                parentIds: '0,1,63f825a2cbe844db84f78e2a90e94782,6a1aaf14e74f4b0c88504ace21674988,'
                            }
                        },
                        apply: {
                            id: '',
                            applyDate: '2019-01-08 12:00:00',
                            user: {
                                id: '7d045f4d47304826babfa73614b5e18e'
                            },
                            certDate: '',
                        },
                        val: '222',
                        status: '1',
                        applyDate: '2018-12-07 15:27:30',
                        creditType: '创新创业学分',
                        user: {
                            admin: false,
                            age: "",
                            isNewRecord: true,
                            loginFlag: "1",
                            name: "王清腾",
                            no: "123456",
                            photo: "/tool/oseasy/user/2019-01-03/45a4fd92d2504e799e5effb6b88dd72a.jpg",
                            professional: "b1f6aa86365b486882e3166bd817ee99",
                            roleNames: "",
                            sysAdmin: false
                        }
                    }
                ],
                officeProps: {
                    label: 'name',
                    value: 'id'
                }
            }
        },
        computed: {
            colleges: function () {
                return this.officeList.filter(function (item) {
                    return item.grade === '2';
                })
            },
            scrRuleCategories: function () {
                return this.scoRuleList.filter(function (item) {
                    return item.type === '2';
                })
            },
            scoRuleListEntryNames: function () {
                return this.getEntries(this.scoRuleList, this.officeProps)
            },
            auditStatueEntries: function () {
                return this.getEntries(this.auditStatues, {
                    label: 'name',
                    value: 'key'
                })
            },
            scoRuleListEntries: function () {
                var entries = {};
                var list = [].concat(this.scoRuleList, []);
                var i = 0;
                while (i < list.length) {
                    var item = list[i];
                    entries[item.id] = item;
                    i++;
                }
                return entries;
            },
        },
        methods: {

            handleChangeCreateDateRange: function (value) {
                if (value.length > 0) {
                    this.searchListForm.createDate = value[0];
                    this.searchListForm.createQDate = value[1];
                } else {
                    this.searchListForm.createDate = '';
                    this.searchListForm.createQDate = '';
                }
                this.getScrApplyList();
            },

            getScrApplyList: function () {
                var self = this;
                this.$axios.get('/scr/scoRapply/ajaxList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        self.scoRuleList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    }
                }).catch(function () {

                })
            },

            handleSortScoApplyList: function (obj) {
                this.searchListForm.orderBy = obj.prop;
                this.searchListForm.orderByType = obj.order ? (obj.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getScrApplyList();
            },

            confirmDelScrApply: function (row) {
                var self = this;
                this.$confirm("确认删除这条申请记录吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delScrApply(row);
                }).catch(function () {

                })
            },

            delScrApply: function (row) {
                var self = this;
                this.$axios.get('/scr/sss/del?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getScrApplyList();
                        self.$message.error('删除成功');
                    }
                }).catch(function () {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            goToAudit: function () {

            },

            goToView: function () {

            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getScrApplyList();
            },

            handlePCPChange: function () {
                this.getScrApplyList();
            },

            getScrRuleList: function () {
                var self = this;
                this.$axios.get('/scr/scoRule/getScoRuleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.scoRuleList = data.data || [];
                    }
                })
            },

//            getScoRuleDetailList: function () {
//                var self = this;
//                this.$axios.get('/scr/scoRule/getScoRuleDetailList').then(function (response) {
//                    var data = response.data;
//                    if (data.status === 1) {
//                        self.scoRuleDetailList = data.data || [];
//                    }
//                })
//            },


            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.officeList = data.data || []
                    }
                })
            },
            getAuditStatues: function () {
                var self = this;
                this.$axios.get('/scr/scoRapply/ajaxScoRptypestatuss').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.auditStatues = JSON.parse(data.data) || [];
                    }
                })
            },
        },
        created: function () {
            this.getScrApplyList();
            this.getOfficeList();
            this.getScrRuleList();
        }
    })

</script>
</body>
</html>