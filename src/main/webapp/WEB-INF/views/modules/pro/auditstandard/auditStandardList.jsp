<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="text-right mgb-20">
        <el-button type="primary" size="mini" @click.stop.prevent="goToLinkPro">关联项目</el-button>
        <el-button type="primary" size="mini" @click.stop.prevent="goToAddStandard">添加评审标准</el-button>
    </div>
    <div class="table-container">
        <el-table :data="auditStandardList" size="small" class="table">
            <el-table-column label="评审标准" prop="name"></el-table-column>
            <el-table-column label="评审标准说明" align="center">
                <template slot-scope="scope">
                    {{scope.row.remarks}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini" @click.stop.prevent="handleEditAuditStandard(scope.row)">修改
                        </el-button>
                        <el-button type="text" size="mini" @click.stop.prevent="confirmDelAuditStandard(scope.row)">删除
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
                    pageNo: 1,
                    pageSize: 10
                },
                auditStandardList: [],
                pageCount: 0,
                tableLoading: true
            }
        },
        methods: {
            goToLinkPro: function () {
                location.href = this.frontOrAdmin + '/auditstandard/auditStandard/listFlow'
            },
            goToAddStandard: function () {
                location.href = this.frontOrAdmin + '/auditstandard/auditStandard/form'
            },

            handleEditAuditStandard: function (row) {
                location.href = this.frontOrAdmin + '/auditstandard/auditStandard/form?id='+row.id;
            },

            confirmDelAuditStandard: function (row) {
                var self = this;
                this.$confirm('确认删除这条评审标准吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delAuditStandard(row)
                }).catch(function () {

                })
            },

            delAuditStandard: function (row) {
                var self = this;
                this.$axios.post('/auditstandard/auditStandard/delAuditStandard', {id: row.id}).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getAuditStandardList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '删除成功' : data.msg
                    })
                })
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getAuditStandardList();
            },

            handlePCPChange: function () {
                this.getAuditStandardList();
            },

            getAuditStandardList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/auditstandard/auditStandard/getAuditStandardList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status == '1') {
                        pageData = data.data;
                    }
                    self.auditStandardList = pageData.list || [];
                    self.pageCount = pageData.count || 0;
                    self.searchListForm.pageSize = pageData.pageSize || 1;
                    self.searchListForm.pageNo = pageData.pageNo || 10;
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                    self.tableLoading = false;
                })
            }
        },
        created: function () {
            this.getAuditStandardList();
        }
    })

</script>

</body>
</html>