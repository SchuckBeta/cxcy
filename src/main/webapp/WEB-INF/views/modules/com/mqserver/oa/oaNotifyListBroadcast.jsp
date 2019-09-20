<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="utf-8"/>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm.type" label="类型" :options="oaNotifyTypes"
                         @change="getOaNotifyList"></e-condition>
            <e-condition type="radio" v-model="searchListForm.status" label="状态"
                         :options="oaNotifyStatus" @change="getOaNotifyList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button icon="el-icon-circle-plus" type="primary" size="mini" @click.stop.prevent="goToAdd">添加
                </el-button>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="标题"
                        size="mini"
                        name="title"
                        @keyup.enter.native="getOaNotifyList"
                        v-model="searchListForm.title"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search" @click="getOaNotifyList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container" v-loading="tableLoading">
        <el-table :data="oaNotifyList" size="small" class="table">
            <el-table-column label="标题">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.title" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.title}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="所在门户" align="center" v-if="currTenantId == '20'">
                <template slot-scope="scope">
                    {{scope.row.schOorPro | selectedFilter(schOorProsEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="类型" align="center">
                <template slot-scope="scope">
                    <span v-if="scope.row.sendType == '2'">站内信</span>
                    <span v-else>
                        {{scope.row.type | selectedFilter(oaNotifyTypeEntries)}}
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.status" active-value="1" inactive-value="0" size="mini"
                               @change="handleChangeNotifyStatus(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="发送人" align="center">
                <template slot-scope="scope">
                    {{scope.row.createUser ? scope.row.createUser.name : '-'}}
                </template>
            </el-table-column>
            <el-table-column label="发布时间" align="center">
                <template slot-scope="scope">
                    {{scope.row.updateDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   :disabled="scope.row.status == '1'"
                                   @click.stop.prevent="goToView(scope.row)">
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop.prevent="confirmDelNotify(scope.row.id)"></el-button>
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
            var oaNotifyStatus = JSON.parse('${fns: toJson(fns:getDictList('oa_notify_status'))}');
            var oaNotifyTypes = JSON.parse('${fns: toJson(fns: getDictList('oa_notify_type'))}');
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    type: '',
                    title: '',
                    status: ''
                },
                oaNotifyList: [],
                pageCount: 0,
                oaNotifyStatus: oaNotifyStatus,
                oaNotifyTypes: oaNotifyTypes,
                tableLoading: true,
                schOorPros: [
                    {label: '校级门户', value: '1'},
                    {label: '省级门户', value: '2'}
                ]
            }
        },
        computed: {
            oaNotifyStatusEntries: function () {
                return this.getEntries(this.oaNotifyStatus)
            },
            oaNotifyTypeEntries: function () {
                return this.getEntries(this.oaNotifyTypes)
            },
            schOorProsEntries: function () {
                return this.getEntries(this.schOorPros);
            }
        },
        methods: {

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getOaNotifyList();
            },

            handlePCPChange: function () {
                this.getOaNotifyList();
            },

            goToView: function (row) {
                if (row.sendType === '2') {
                    return location.href = this.ctx + '/oa/oaNotify/formBroadcast?id=' + row.id;
                }
                return location.href = this.ctx + '/oa/oaNotify/allNoticeForm?id=' + row.id;
            },

            goToAdd: function () {
                return location.href = this.ctx + '/oa/oaNotify/allNoticeForm';
            },

            handleChangeNotifyStatus: function (row) {
                var self = this;
                this.$axios.get('/oa/oaNotify/updatePublish', {
                    params: {
                        id: row.id,
                        status: row.status
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 0) {
                        row.status = row.status == '1' ? '0' : '1'
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    } else {
//                        self.$alert('发布成功', '提示', {
//                            type: 'success'
//                        })
                    }
                }).catch(function (error) {
                    row.status = row.status == '1' ? '0' : '1'
                })
            },

            confirmDelNotify: function (id) {
                var self = this;
                this.$confirm('删除这条消息将不会再显示？是否继续', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delNotify(id);
                }).catch(function () {

                })
            },

            delNotify: function (id) {
                var self = this;
                this.$axios.get('/oa/oaNotify/delOaNotify', {params: {id: id}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getOaNotifyList();
                        self.$alert('删除成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                }).catch(function (error) {
                })
            },

            getOaNotifyList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/oa/oaNotify/getOaNotifyList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data;
                        if (pageData) {
                            self.oaNotifyList = pageData.list || [];
                            self.pageCount = pageData.count;
                            self.searchListForm.pageSize = pageData.pageSize;
                            self.searchListForm.pageNo = pageData.pageNo;
                        }
                    }
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.tableLoading = false;
                })
            }
        },
        mounted: function () {
            this.getOaNotifyList();
        }
    })


</script>

</body>
</html>