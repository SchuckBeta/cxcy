<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20"><edit-bar></edit-bar></div>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" size="mini" :disabled="casUserList.length < 1" @click.stop.prevent="confirmControlLogin">{{casBtnText}}</el-button>
            </div>
            <div class="search-input">
                <input type="text" style="display: none;">
                <el-input placeholder="请输入内容" v-model="searchText" size="mini" style="width: 400px;"
                          @keyup.enter.native="getSysCasUserList">
                    <el-select v-model="searchSelect" slot="prepend" placeholder="请选择" style="width: 100px;"
                               @change="handleChangeSearchSelect">
                        <el-option label="登录名" value="ruid"></el-option>
                        <el-option label="用户名" value="rcname"></el-option>
                    </el-select>
                    <el-button slot="append" icon="el-icon-search" @click.stop.prevent="getSysCasUserList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="casUserList" size="small" class="table">
            <el-table-column label="登录名">
                <template slot-scope="scope">
                    {{scope.row.ruid}}
                </template>
            </el-table-column>
            <el-table-column label="用户名" align="center">
                <template slot-scope="scope">
                    {{scope.row.rcname}}
                </template>
            </el-table-column>
            <el-table-column label="允许CAS登录" align="center">
                <template slot-scope="scope">
                    {{scope.row.enable ? '已开启' : '已禁用'}}
                </template>
            </el-table-column>
            <el-table-column label="登录次数" align="center">
                <template slot-scope="scope">
                    {{scope.row.time}}
                </template>
            </el-table-column>
            <el-table-column label="最后更新时间" align="center">
                <template slot-scope="scope">
                    {{scope.row.lastLoginDate | formatDateFilter('YYYY-MM-DD HH:mm')}}
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
                searchText: '',
                searchSelect: 'ruid',
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                tableLoading: true,
                selectedValues: ['ruid', 'rcname'],
                casUserList: []
            }
        },
        computed: {
            casBtnText: function () {
                return this.casDisabled ? '禁用单点登录' : '开启单点登录'
            },
            casDisabled: function () {
                if(this.casUserList.length > 0){
                    return this.casUserList[0].enable;
                }
                return false;
            }
        },
        methods: {
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getSysCasUserList();
            },

            handlePCPChange: function () {
                this.getSysCasUserList();
            },

            clearOtherSelectedText: function () {
                var self = this;
                this.selectedValues.forEach(function (item) {
                    self.searchListForm[item] = '';
                })
            },

            handleChangeSearchSelect: function (value) {
                this.clearOtherSelectedText();
                if (value) {
                    this.searchListForm[value] = this.searchText;
                }
            },
            getSysCasUserList: function () {
                var self = this;
                this.tableLoading = true;
                this.searchListForm[this.searchSelect] = this.searchText;
                this.$axios.get('/cas/getSysCasUserList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status == 1) {
                        pageData = data.data;
                    }
                    self.casUserList = pageData.list || [];
                    self.searchListForm.pageNo = pageData.pageNo || 1;
                    self.searchListForm.pageSize = pageData.pageSize || 10;
                    self.pageCount = pageData.count || 0;
                    self.tableLoading = false;
                })
            },

            confirmControlLogin: function () {
                var self = this;
                this.$confirm('确认'+this.casBtnText+'吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.postControlLogin();
                }).catch(function () {

                })
            },

            postControlLogin: function () {
                var self = this;
                this.$axios.get('/cas/ajaxUpdateALLEnable?enable='+!this.casDisabled).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        self.getSysCasUserList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '操作成功' : '操作失败'
                    })
                })
            }
        },
        created: function () {
            this.getSysCasUserList();
        }
    })

</script>
</body>
</html>