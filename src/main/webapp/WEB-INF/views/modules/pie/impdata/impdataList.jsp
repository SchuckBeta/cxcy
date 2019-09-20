<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
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
        <el-dropdown size="small" split-button type="primary" @command="handleCommandDown">
            下载导入模板
            <el-dropdown-menu slot="dropdown">
                <el-dropdown-item v-for="item in downTemps" :command="item" :key="item.value">{{item.label}}
                </el-dropdown-item>
            </el-dropdown-menu>
        </el-dropdown>
        <el-upload
                style="display: inline-block"
                name="fileName"
                accept=".xls,.xlsx"
                :on-success="uploadSuccess"
                :on-error="uploadError"
                :show-file-list="false"
                :disabled="isUploading"
                :before-upload="beforeUpload"
                action="${ctx}/impdata/importData">
            <el-button :disabled="isUploading" icon="el-icon-upload" size="small" type="primary">{{isUploading ?
                '导入中...' : '导入模板'}}
            </el-button>
        </el-upload>
    </div>
    <div class="table-container">
        <el-table :data="impdataList" size="mini" class="table">
            <el-table-column label="导入数据类型">
                <template slot-scope="scope">
                    {{scope.row.imp_tpye | selectedFilter(downTempsEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="开始导入时间" align="center">
                <template slot-scope="scope">
                    {{scope.row.create_date}}
                </template>
            </el-table-column>
            <el-table-column label="数据总数" align="center">
                <template slot-scope="scope">
                    {{scope.row.total}}
                </template>
            </el-table-column>
            <el-table-column label="导入成功数" align="center">
                <template slot-scope="scope">
                    {{scope.row.success}}
                </template>
            </el-table-column>
            <el-table-column label="导入失败数" align="center">
                <template slot-scope="scope">
                    {{scope.row.fail}}
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center">
                <template slot-scope="scope">
                    {{scope.row.is_complete == '1' ? '导入完毕' : '导入中'}}
                </template>
            </el-table-column>
            <shiro:hasPermission name="sys:user:import">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-tooltip class="item" content="下载错误数据"
                                    popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-download"
                                       :disabled="scope.row.is_complete != '1'"
                                       @click.stop="downImpData(scope.row)">
                            </el-button>
                        </el-tooltip>

                        <el-tooltip class="item" content="删除"
                                    popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       :disabled="scope.row.is_complete != '1'"
                                       @click.stop="confirmDelImpdata(scope.row)">
                            </el-button>
                        </el-tooltip>
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
            return {
                isUploading: false,
                downTemps: [{label: '学生信息模板', value: '1'}, {label: '导师信息模板', value: '2'}, {
                    label: '后台用户信息模板',
                    value: '3'
                },
                    {label: '机构信息模板', value: '4'},
                    {label: '全国学生信息模板', value: '13'}
                    /* ,
                     {label: '项目信息模板', value: '5'},
                     {label: '互联网+大赛信息模板', value: '10'} */
                ],
                tableLoading: true,
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1
                },
                impdataList: [],
                pageCount: 0,
                timerInterval: null,
                isFirst: true
            }
        },
        computed: {
            downTempsEntries: function () {
                var entries = {};
                this.downTemps.forEach(function (item) {
                    entries[item.value] = item.label.replace('模板', '');
                })
                return entries;
            }
        },
        methods: {
            confirmDelImpdata: function (row) {
                var self = this;
                this.$confirm('确认删除这条导入模板数据吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delImpdata(row)
                }).catch(function () {

                })
            },

            delImpdata: function (row) {
                location.href = this.ctx + '/impdata/delete?id=' + row.id
            },

            downImpData: function (row) {
                location.href = (this.ctx + '/impdata/expData?id=' + row.id + '&type=' + row.imp_tpye);
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.timerInterval && clearInterval(this.timerInterval);
                this.getImpdataList();
            },

            handlePCPChange: function () {
                this.timerInterval && clearInterval(this.timerInterval);
                this.getImpdataList();
            },

            getImpInfoList: function () {
                var self = this;
                var impdataList = self.impdataList;
                var unCompleteList = impdataList.filter(function (item) {
                    return item.is_complete != '1';
                });
                var params = unCompleteList.map(function (item) {
                    return item.id;
                });
                if (unCompleteList.length == 0) {
                    this.timerInterval && clearInterval(this.timerInterval);
                    return false;
                }
                this.$axios.post('/impdata/getImpInfoList', params).then(function (response) {
                    var data = response.data.data || {};
                    self.impdataList.forEach(function (item) {
                        var impInfo = data[item.id];
                        if (impInfo) {
                            Object.assign(item, impInfo);
                            item.is_complete = impInfo.isComplete;
                        }
                    })
                });

            },

            getImpdataList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/impdata/getImpdataList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status === 1) {
                        pageData = data.data || {};
                    }
                    self.impdataList = pageData.list || [];
                    self.pageCount = pageData.count || 0;
                    self.searchListForm.pageSize = pageData.pageSize || 1;
                    self.searchListForm.pageNo = pageData.pageNo || 10;
                    self.tableLoading = false;
                    var unCompleteList = self.impdataList.filter(function (item) {
                        return item.is_complete != '1';
                    });
                    if (unCompleteList.length < 1) {
                        return false;
                    }
                    self.timerInterval = setInterval(function () {
                        self.getImpInfoList()
                    }, 2000);

                }).catch(function (error) {
                    self.tableLoading = false;
                })
            },

            handleCommandDown: function (item) {
                location.href = "/a/impdata/downTemplate?type=" + item.value;
            },
            uploadSuccess: function (response) {
                this.isUploading = false;
                if (response.ret == '1') {
                    this.getImpdataList();
                }
                this.$message({
                    type: response.ret === '1' ? 'success' : 'error',
                    message: response.msg
                })
            },
            uploadError: function (error) {
                this.isUploading = false;
                this.$message({
                    type: 'error',
                    message: this.xhrErrorMsg
                })
            },
            beforeUpload: function (file) {
//                var downTempLabels = this.downTemps.map(function (item) {
//                    return item.label.replace('模板', '导入');
//                });
//                var fileName = file.name.replace(/(.*\/)*([^.]+).*/ig, "$2");
//                if (downTempLabels.indexOf(fileName) === -1) {
//                    this.$message({
//                        type: 'error',
//                        message: '模板名称有误，或者模板错误，请使用正确模板'
//                    });
//                    return false;
//                }
                this.isUploading = true;
                return true;
            }
        },
        created: function () {
            this.getImpdataList();
        }
    })

</script>
</body>
</html>