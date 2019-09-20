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
    <div class="clearfix mgb-20">
    <el-input
            placeholder="判断类型名称"
            size="mini"
            name="name"
            v-model="searchListForm.name"
            @keyup.enter.native="getActYwSgtypeList"
            class="w300">
        <el-button slot="append" icon="el-icon-search" @click.stop.prevent="getActYwSgtypeList"></el-button>
    </el-input>
    </div>
    <div class="table-container">
        <el-table v-loading="tableLoading" :data="actYwSgtypeList" size="mini" class="table">
            <el-table-column prop="name" label="名称"></el-table-column>
            <el-table-column label="操作" align="center" width="140">
                <template slot-scope="scope">
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   @click.stop.prevent="handleEditActYwSgtype(scope.row)">
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop.prevent="handleDelActYwSgtype(scope.row)">
                        </el-button>
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
    <el-dialog
            :title="dialogTitle + '判断条件'"
            :visible.sync="dialogVisibleActYwSgType"
            :close-on-click-modal="false"
            width="520px"
            :before-close="handleCloseActYwSgType">
        <el-form :model="actYwSgtypeForm" ref="actYwSgtypeForm" :rules="actYwSgtypeRules" size="mini"
                 :disabled="updating"
                 label-width="120px">
            <el-form-item prop="name" label="名称：">
                <el-input v-model="actYwSgtypeForm.name"></el-input>
            </el-form-item>
            <input type="text" style="display: none">
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button type="primary" size="mini" @click="saveActYwSgType" :disabled="updating">确 定</el-button>
  </span>
    </el-dialog>
</div>


<script type="text/javascript">


    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var validateName = function (rule, value, callback) {
                var actYwSgtypeForm = self.actYwSgtypeForm;
                if (!value) return callback();
                if ((/[@#\$%\^&\*\s]+/g.test(value))) {
                    return callback(new Error('请不要输入特殊符号'));
                }
                return self.$axios.post('/actyw/actYwSgtype/checkActYwSgtypeName', actYwSgtypeForm).then(function (response) {
                    if (response.data) {
                        return callback()
                    }
                    return callback(new Error('判断类型名称已存在'));
                }).catch(function (error) {
                    return callback(new Error('网络连接失败'));
                })
            }
            return {
                searchListForm: {
                    name: '',
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                actYwSgtypeList: [],
                dialogVisibleActYwSgType: false,
                actYwSgtypeForm: {
                    id: '',
                    name: ''
                },
                actYwSgtypeRules: {
                    name: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {max: 60, message: '请输入不大于60位字符', trigger: 'blur'},
                        {validator: validateName, trigger: 'blur'}
                    ]
                },
                tableLoading: true,
                updating: false
            }
        },
        computed: {
            dialogTitle: function () {
                return this.actYwSgtypeForm.id ? '修改' : '添加'
            }
        },
        methods: {

            handleEditActYwSgtype: function (row) {
                this.dialogVisibleActYwSgType = true;
                this.$nextTick(function () {
                    Object.assign(this.actYwSgtypeForm, row);
                })
            },

            handleCloseActYwSgType: function () {
                this.$refs.actYwSgtypeForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleActYwSgType = false;
                });
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getActYwSgtypeList();
            },

            handlePCPChange: function () {
                this.getActYwSgtypeList();
            },

            saveActYwSgType: function () {
                var self = this;
                this.$refs.actYwSgtypeForm.validate(function (valid) {
                    if (valid) {
                        self.postActYwSgType();
                    }
                })
            },

            handleDelActYwSgtype: function (row) {
                var self = this;

                this.$confirm('确认删除这条判断类型吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.updating = true;
                    self.$axios.post('/actyw/actYwSgtype/delActYwSgtype', {id: row.id}).then(function (response) {
                        var data = response.data;
                        if (data.status === 1) {
                            self.getActYwSgtypeList();
                            self.$alert('保存成功', '提示', {
                                type: 'success'
                            })
                        }else {
                            self.$alert(data.msg, '提示', {
                                type: 'error'
                            })
                        }
                        self.updating = false;
                    })
                }).catch(function () {
                    self.updating = false;
                })


            },

            postActYwSgType: function () {
                var self = this;
                this.updating = true;
                this.$axios.post('/actyw/actYwSgtype/saveActYwSgtype', this.actYwSgtypeForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getActYwSgtypeList();
                        self.handleCloseActYwSgType();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    }else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.updating = false;
                }).catch(function () {
                    self.updating = false;
                })
            },

            getActYwSgtypeList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/actyw/actYwSgtype/getActYwSgtypeList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status === 1) {
                        pageData = data.data;
                    }
                    self.actYwSgtypeList = pageData.list || [];
                    self.pageCount = pageData.count || 0;
                    self.searchListForm.pageSize = pageData.pageSize || 1;
                    self.searchListForm.pageNo = pageData.pageNo || 10;
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.tableLoading = false;
                })
            }
        },
        mounted: function () {
            this.getActYwSgtypeList();
        }
    })

</script>

</body>
</html>