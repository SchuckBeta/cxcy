<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" size="mini" @click.stop.prevent="goToAdd"><i
                        class="iconfont icon-tianjia"></i>添加
                </el-button>
            </div>
            <div class="search-input">
                <input type="text" style="display: none">
                <el-input
                        placeholder="标题"
                        size="mini"
                        name="title"
                        v-model="searchListForm.title"
                        @change="getNotifyList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container" v-loading="tableLoading">
        <el-table :data="notifyList" size="small" class="table">
            <el-table-column label="标题" align="center">
                <template slot-scope="scope" align="center">
                    {{scope.row.title | textEllipsis}}
                </template>
            </el-table-column>
            <el-table-column label="通知类型" align="center">
                <template slot-scope="scope" align="center">
                    {{scope.row.type | selectedFilter(notifyTypeEntries)}}{{scope.row.isRelease}}
                </template>
            </el-table-column>
            <el-table-column label="发布" align="center">
                <template slot-scope="scope" align="center">
                    <el-switch v-model="scope.row.isRelease" size="mini" active-value="1" inactive-value="0" @change="setRelease(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="发布时间" align="center">
                <template slot-scope="scope" align="center">
                    {{scope.row.releaseDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope" align="center">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini" @click.stop.prevent="goToEdit(scope.row.id)">修改</el-button>
                        <el-button type="text" size="mini" @click.stop.prevent="confirmDelNotify(scope.row.id)">删除</el-button>
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
            var notifyTypes = JSON.parse('${fns: toJson(fns: getDictList('0000000259'))}');
            var isReleases = JSON.parse('${fns: toJson(fns: getDictList('yes_no'))}');
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    title: ''
                },
                pageCount: 0,
                notifyList: [],
                notifyTypes: notifyTypes,
                tableLoading: false
            }
        },
        computed: {
          notifyTypeEntries: {
              get: function () {
                  return this.getEntries(this.notifyTypes)
              }
          }
        },
        methods: {
            handlePSizeChange: function () {

            },

            handlePCPChange: function () {

            },

            setRelease:  function (row) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsDeclareNotify/setRelease',
                    params: {
                        id: row.id,
//                        isRelease: row.isRelease
                    }
                }).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    var isResponseSuccess = self.isResponseSuccess(data.code);
                    if (checkResponseCode) {
                        self.getBaseContentList()
                    }
                    self.$message({
                        message: !isResponseSuccess ? data.msg : '操作成功',
                        type: !isResponseSuccess ? 'error' : 'success'
                    })
                }).catch(function (error) {
                    self.$message({
                        message: error.response.data,
                        type: 'error'
                    })
                })
            },

            confirmDelNotify: function (id) {
                var self = this;
                this.$confirm('此操作会删除选中的通知公告，是否继续？', '提示', {
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
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsDeclareNotify/delNotify?id='+id
                }).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    var isResponseSuccess = self.isResponseSuccess(data.code);
                    if (checkResponseCode) {
                        self.getBaseContentList()
                    }
                    self.$message({
                        message: !isResponseSuccess ? data.msg : '操作成功',
                        type: !isResponseSuccess ? 'error' : 'success'
                    })
                }).catch(function (error) {
                    self.$message({
                        message: error.response.data,
                        type: 'error'
                    })
                })
            },

            getNotifyList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios({
                    method: 'GET',
                    url: '/cms/cmsDeclareNotify/getNotifyList',
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    var pageData;
                    if (checkResponseCode) {
                        pageData = data.data;
                        self.notifyList = pageData.list || [];
                        self.pageCount = pageData.count;
                        self.searchListForm.pageNo = pageData.pageNo;
                        self.searchListForm.pageSize = pageData.pageSize;
                    }
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    });
                    self.tableLoading = false;
                })
            },

            goToAdd: function () {
                location.href = this.frontOrAdmin + '/cms/cmsDeclareNotify/form';
            },
            goToEdit: function (id) {
                location.href = this.frontOrAdmin + '/cms/cmsDeclareNotify/form?id='+ id;
            }
        },
        beforeMount: function () {
            this.getNotifyList();
        }
    })

</script>
</body>
</html>