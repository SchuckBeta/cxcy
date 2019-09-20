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
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
            <e-condition type="radio" label="所属学院" :options="colleges" :default-props="officeProps"
                         v-model="searchListForm['user.office.id']" @change="getScrApplyList"></e-condition>
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
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="defaultTime"
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
        <el-table :data="cpScrApplyList" size="mini" class="table" @sort-change="handleSortScoApplyList">
            <el-table-column :prop="orss['user.no']" width="286" label="申请人信息" :sortable="orss['user.no'] ? 'custom' : false">
                <template slot-scope="scope">
                    <credit-user-column :user="scope.row.user"></credit-user-column>
                </template>
            </el-table-column>
            <%--<el-table-column prop="top.id" label="学分类型" sortable="top.id" align="center">--%>
            <%--<template slot-scope="scope">--%>
            <%--{{scope.row.top ? scope.row.top.name : ''}}--%>
            <%--</template>--%>
            <%--</el-table-column>--%>
            <el-table-column :prop="orss['rule.name']" label="学分类别" :sortable="orss['rule.name'] ? 'custom' : false" align="center">
                <template slot-scope="scope">
                    {{scope.row.rule.name}}
                </template>
            </el-table-column>
            <el-table-column :prop="orss['rdetail.name']" label="认定内容及标准" :sortable="orss['rdetail.name'] ? 'custom' : false">
                <template slot-scope="scope">
                    <el-tooltip v-if="scope.row.lrule.contentNames" class="item" popper-class="white" effect="dark"
                                :content="scope.row.lrule.contentNames"
                                placement="right"><span
                            class="break-ellipsis">{{scope.row.lrule.contentNames}}</span></el-tooltip>
                    <div>
                        {{scope.row.rdetail.name}}
                    </div>
                </template>
            </el-table-column>
            <el-table-column :prop="orss['applyDate']" label="申请日期" :sortable="orss['applyDate'] ? 'custom' : false" align="center">
                <template slot-scope="scope">
                    {{scope.row.applyDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column :prop="orss['status']" label="状态" align="center" :sortable="orss['status'] ? 'custom' : false">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(auditStatueEntries)}}
                </template>
            </el-table-column>
            <shiro:hasPermission name="sys:user:view">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <template v-if="scope.row.status == '2'">
                                <el-button size="mini" type="text"
                                           @click.stop="goToAudit(scope.row)">审核
                                </el-button>
                            </template>
                            <el-button type="text" size="mini" @click.stop="goToView(scope.row)">查看</el-button>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>
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
            var orss = JSON.parse(JSON.stringify(${fns: toJson(orss)}));
            return {
                searchListForm: {
                    'user.office.id': '',
                    'rule.id': '',
                    pageSize: 10,
                    pageNo: 1,
                    createDate: '',
                    createQDate: '',
                    keys: ''
                },
                defaultTime: ['00:00:00','23:59:59'],
                orss: orss,
                pageCount: 0,
                createDateRange: [],
                officeList: [],
                creditTypes: [],
                scoRuleList: [],
//                scoRuleDetailList: [],
                auditStatues: [],
                scrApplyList: [],
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
            cpScrApplyList: function () {
                var scoRuleListEntryNames = this.scoRuleListEntryNames;
                return this.scrApplyList.map(function (item) {
                    var lrule = item.lrule || {};
                    var parentIds = lrule.parentIds;
                    if (parentIds) {
                        parentIds += lrule.id;
                        parentIds = parentIds.split(',');
                        parentIds = parentIds.slice(3);
                        var contentNames = '';
                        var names = [];
                        parentIds.forEach(function (id) {
                            if (scoRuleListEntryNames && scoRuleListEntryNames[id]) {
                                contentNames += scoRuleListEntryNames[id] + ' ';
                                names.push(scoRuleListEntryNames[id])
                            }
                        })
                    }
                    lrule.contentNames = contentNames;
                    lrule.names = names;
                    item.lrule = lrule;
                    return item;
                });
            }
        },
        methods: {

            handleChangeCreateDateRange: function (value) {
                if (value && value.length > 0) {
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
                        self.scrApplyList = data.list || [];
//						console.log(self.scrApplyList)
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

            goToAudit: function (row) {
                location.href = this.frontOrAdmin + '/scr/scoRapply/form?scrId=' + row.id + '&creditType=1';
            },

            goToView: function (row) {
                location.href = this.frontOrAdmin + '/scr/scoRapply/view?scrId=' + row.id + '&creditType=1';
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
                this.$axios.get('/scr/scoRapply/ajaxScoRstatuss?isAll=true').then(function (response) {
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
            this.getAuditStatues();
        }
    })

</script>
</body>
</html>