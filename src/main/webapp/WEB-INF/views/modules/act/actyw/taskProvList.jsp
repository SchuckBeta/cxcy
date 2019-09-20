<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative-province.jsp" %>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="mgb-60">
    <edit-bar-province></edit-bar-province>
    <div class="content-container">
        <el-form :model="searchListForm" ref="searchListForm" size="mini">
            <div class="conditions">

                <e-condition label="项目类型" type="radio" v-model="searchListForm.actywId" :default-props="proTypeDefaultProps"
                             name="actywId" :options="projectTypes" @change="changeProType">
                </e-condition>
                <e-condition label="审核节点" type="radio" v-model="searchListForm.gnodeId" v-show="checkTasks.length > 0"
                             :default-props="checkTasksDefaultProps"
                             name="gnodeId" :options="checkTasks" @change="getDataList">
                </e-condition>

            </div>

        </el-form>
        <div class="table-container">
            <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading">
                <el-table-column prop="actywName" label="项目类型" align="left" min-width="150">
                </el-table-column>
                <el-table-column prop="gnodeName" label="审核节点" align="center" min-width="150">
                </el-table-column>
                <el-table-column prop="todoNum" label="待分配项目数" align="center" min-width="120">
                    <template slot-scope="scope">
                        <col-project-node v-if="!scope.row.todoNum" :row="scope.row" @change="handleChangeRow"></col-project-node>
                        <span :class="{red:scope.row.todoNum != 0}" v-else>{{scope.row.todoNum}}</span>
                    </template>
                </el-table-column>
                <el-table-column prop="toauditNum" label="待审核项目数" align="center" min-width="120">
                </el-table-column>
                <el-table-column prop="hasNum" label="已分配项目数" align="center" min-width="120">
                </el-table-column>
                <el-table-column prop="expertNum" label="每个项目分配的专家数" align="center" min-width="160">
                    <template slot-scope="scope">
                        <span v-if="scope.row.expertNum != '-1'">{{scope.row.expertNum || '3'}}</span>
                        <span v-else>不限</span>
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" min-width="170">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <a :href="frontOrAdmin + '/actyw/actywAssignRule/proProvTaskList?actywId=' + scope.row.actywId + '&gnodeId=' + scope.row.gnodeId">分配任务</a>
                            <a :href="frontOrAdmin + '/actyw/actywAssignRule/expertProvTaskList?actywId=' + scope.row.actywId + '&gnodeId=' + scope.row.gnodeId" v-if="scope.row.toauditNum > 0">查看任务</a>
                            <el-button type="text" v-if="scope.row.toauditNum > 0" size="mini" @click.stop.prevent="clearTask(scope.row)">清除任务
                            </el-button>
                        </div>
                    </template>
                </el-table-column>
            </el-table>
            <div class="text-right mgb-20" v-if="pageCount">
                <el-pagination
                        size="small"
                        @size-change="handlePaginationSizeChange"
                        background
                        @current-change="handlePaginationPageChange"
                        :current-page.sync="searchListForm.pageNo"
                        :page-sizes="[5,10,20]"
                        :page-size="searchListForm.pageSize"
                        layout="total, prev, pager, next, sizes"
                        :total="pageCount">
                </el-pagination>
            </div>
        </div>
    </div>
</div>


<script>

    new Vue({
        el: '#app',
        data: function () {
            var proType = '${proType}';
            return {
                proType:proType,
                projectTypes: [],
                checkTasks: [],
                proTypeNode: {},
                pageCount: 0,
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    actywId: '',
                    gnodeId: ''
                },
                loading: false,
                pageList: [],
                proTypeDefaultProps: {
                    label: 'actywName',
                    value: 'actywId'
                },
                checkTasksDefaultProps: {
                    label: 'gnodeName',
                    value: 'gnodeId'
                }
            }
        },
        computed: {},
        methods: {
            handleChangeRow: function (scope) {
                this.assignFormData(scope.row, scope.data);
            },

            changeProType: function () {
                this.searchListForm.gnodeId = '';
                this.checkTasks = [];
                if(this.searchListForm.actywId){
                    this.checkTasks = this.proTypeNode[this.searchListForm.actywId];
                }
                this.getDataList();
            },
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'POST',
                    url: '/actyw/actywAssignRule/ajaxProvGetAssginList?' + Object.toURLSearchParams(this.searchListForm) + '&proType=' + this.proType
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        var list = data.data.list || [];
                        self.pageList = list.map(function (item) {
                            item.todoNum = '';
                            item.toauditNum = '';
                            item.hasNum = '';
                            return item;
                        })
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                });
            },
            clearTask: function (row) {
                var self = this;
                this.$confirm('确认清除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var loading = self.$loading({
                        lock: true,
                        text: '清除任务中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0)',
                        customClass:'gray-green-loading'
                    });
                    self.$axios({
                        method: 'POST',
                        url: '/actyw/actywAssignRule/delProvProTask',
                        data: {
                            actywId: row.actywId,
                            gnodeId: row.gnodeId
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        loading.close();
                        self.$message({
                            message: data.status == '1' ? '清除成功' : data.msg || '清除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        loading.close();
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'warning'
                        });
                    })
                }).catch(function () {
                    
                })
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            getProTypeNode: function () {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/actyw/actywAssignRule/ajaxGetQuery',
                    params:{
                        proType:this.proType
                    }
                }).then(function (response) {
                    var data = response.data;
                    self.proTypeNode = data.data.proTypeNode || {};
                    self.projectTypes = JSON.parse(JSON.stringify(data.data.projectTypes)) || [];

                })
            }
        },
        created: function () {
            this.getProTypeNode();
            this.getDataList();
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })
</script>

</body>
</html>