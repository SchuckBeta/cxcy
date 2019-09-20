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
    <div class="mgb-20 text-right">
        <el-button type="primary" icon="el-icon-circle-plus" size="mini" @click.stop="handleAddActYwStatus">添加
        </el-button>
    </div>
    <div class="table-container">
        <el-table :data="actYwStatusList" size="small" class="table">
            <el-table-column label="网关类别">
                <template slot-scope="scope">
                    {{scope.row.name}}
                </template>
            </el-table-column>
            <el-table-column label="范围" align="center">
                <template slot-scope="scope">
                    {{scope.row.alias}}
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center">
                <template slot-scope="scope">
                    {{scope.row.state}}
                </template>
            </el-table-column>
            <el-table-column label="备注" align="center">
                <template slot-scope="scope">
                    {{scope.row.remarks}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-tooltip class="item" content="编辑"
                                popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   @click.stop="handleEditActYwStatus(scope.row)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除"
                                popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop="confirmDelActYwStatus(scope.row)"></el-button>
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
            :visible.sync="dialogVisibleActYwStatus"
            :close-on-click-modal="false"
            width="520px"
            :before-close="handleCloseActYwStatus">
        <el-form :model="actYwStatusForm" ref="actYwStatusForm" :rules="actYwStatusRules" size="mini"
                 label-width="120px" :disabled="updating">
            <el-form-item prop="gtype" label="条件类型：">
                <el-select :disabled="!!actYwStatusForm.id" clearable v-model="actYwStatusForm.gtype" filterable
                           @change="handleChangeGtype"
                           placeholder="请选择">
                    <el-option
                            v-for="item in actYwSgtypeAllList"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
                <el-tooltip v-if="!actYwStatusForm.id" class="item" effect="dark" popper-class="white" content="添加判断类型"
                            placement="top">
                    <el-button icon="el-icon-plus" @click.stop.prevent="openDialogActYwSgtype"></el-button>
                </el-tooltip>
            </el-form-item>
            <el-form-item v-if="actYwStatusForm.regType == '2'" label="范围：" style="margin-bottom: 0">
                <el-col :span="8">
                    <el-form-item prop="startNum">
                        <el-input name="startNum" v-model="actYwStatusForm.startNum"></el-input>
                    </el-form-item>
                </el-col>
                <el-col :span="1" class="text-center">-</el-col>
                <el-col :span="8">
                    <el-form-item prop="endNum">
                        <el-input name="endNum" v-model="actYwStatusForm.endNum"></el-input>
                    </el-form-item>
                </el-col>
            </el-form-item>
            <el-form-item prop="sign" label="状态 ：">
                <el-radio-group v-model="actYwStatusForm.sign">
                    <el-radio label="1">是</el-radio>
                    <el-radio label="0">否</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item prop="state" label="状态 ：">
                <el-input name="state" v-model="actYwStatusForm.state" maxlength="64"></el-input>
            </el-form-item>
            <el-form-item prop="remarks" label="备注 ：">
                <el-input name="remarks" type="textarea" :rows="3" maxlength="125"
                          v-model="actYwStatusForm.remarks"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button size="mini" @click="handleCloseActYwStatus">取 消</el-button>
    <el-button type="primary" size="mini" @click="saveActYwStatus" :disabled="updating">确 定</el-button>
  </span>
    </el-dialog>

    <el-dialog
            title="添加判断类型"
            :visible.sync="dialogVisibleActYwSgtype"
            :close-on-click-modal="false"
            width="520px"
            :before-close="handleCloseActYwSgtype">
        <el-form :model="actYwSgtypeForm" ref="actYwSgtypeForm" :disabled="updating" :rules="actYwSgtypeRules"
                 size="mini" label-width="120px" style="height: 207px;">
            <el-form-item prop="regType" label="网关类型：" required>
                <el-select v-model="actYwSgtypeForm.regType"
                           placeholder="请选择">
                    <el-option
                            v-for="item in regTypes"
                            :key="item.id"
                            :label="item.name"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="name" label="状态类型：">
                <el-input name="name" v-model="actYwSgtypeForm.name"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click="handleCloseActYwSgtype">取 消</el-button>
            <el-button type="primary" size="mini" @click="saveActYwSgtype" :disabled="updating">确 定</el-button>
          </span>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var validateStartNum = function (rule, value, callback) {
                if (value) {
                    if (!/^[1-9]?[0-9]$/.test(value)) {
                        return callback(new Error("请填写0-99间的数字"))
                    }
                    var endNum = self.actYwStatusForm.endNum;
                    if (endNum) {
                        if (parseInt(value) >= parseInt(endNum)) {
                            return callback(new Error("请填写的数小于最大值"))
                        }
                    }
                }
                return callback();
            }

            var validateEndNum = function (rule, value, callback) {
                if (value) {
                    if (!/^[1-9][0-9]?[0-9]?$/.test(value)) {
                        return callback(new Error("请填写0-100间的数字"))
                    }
                    var startNum = self.actYwStatusForm.startNum;
                    if (startNum) {
                        if (parseInt(value) > 100) {
                            return callback(new Error("请填写小于100的值"))
                        }
                        if (parseInt(value) <= parseInt(startNum)) {
                            return callback(new Error("请填写的数大于最小值"))
                        }
                    }
                }
                return callback();
            }

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
                    pageNo: 1,
                    pageSize: 10
                },
                actYwStatusList: [],
                pageCount: 0,
                tableLoading: true,
                dialogVisibleActYwStatus: false,
                actYwStatusForm: {
                    id: '',
                    gtype: '',
                    state: '',
                    remarks: '',
                    startNum: '',
                    endNum: '',
                    alias: '',
                    sign: '',
                    regType: ''
                },
                actYwStatusRules: {
                    gtype: [
                        {required: true, message: '请选择条件类型', trigger: 'change'}
                    ],
                    state: [
                        {required: true, message: '请填写状态', trigger: 'blur'},
                        {max: 10, message: '请输入不大于10个字符', trigger: 'blur'}
                    ],
                    startNum: [
                        {required: true, message: '请填写最小值', trigger: 'blur'},
                        {validator: validateStartNum, trigger: 'blur'}
                    ],
                    endNum: [
                        {required: true, message: '请填写最大值', trigger: 'blur'},
                        {validator: validateEndNum, trigger: 'blur'}
                    ]
                },
                actYwSgtypeAllList: [],

                actYwSgtypeForm: {
                    regType: '',
                    name: '',
                    id: ''
                },
                actYwSgtypeRules: {
                    name: [
                        {required: true, message: '请输入状态类型', trigger: 'change'},
                        {validator: validateName, trigger: 'blur'}
                    ]
                },
                regTypes: [],
                dialogVisibleActYwSgtype: false,
                updating: false
            }
        },
        computed: {
            dialogTitle: function () {
                return this.actYwStatusForm.id ? '修改' : '添加'
            }
        },
        watch: {
            'actYwStatusForm.alias': function (value) {
                if (value) {
                    var nums = value.split('-');
                    this.actYwStatusForm.startNum = nums[0];
                    this.actYwStatusForm.endNum = nums[1];
                } else {
                    this.actYwStatusForm.startNum = '';
                    this.actYwStatusForm.endNum = '';
                }
            }
        },
        methods: {


            openDialogActYwSgtype: function () {
                this.dialogVisibleActYwSgtype = true;
            },

            handleCloseActYwSgtype: function () {
                this.$refs.actYwSgtypeForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleActYwSgtype = false;
                })
            },

            saveActYwSgtype: function () {
                var self = this;
                this.$refs.actYwSgtypeForm.validate(function (valid) {
                    if (valid) {
                        self.postActYwSgType();
                    }
                })
            },

            postActYwSgType: function () {
                var self = this;
                this.updating = true;
                this.$axios.post('/actyw/actYwSgtype/saveActYwSgtype', this.actYwSgtypeForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getActYwSgtypeAllList();
                        self.handleCloseActYwSgtype();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.updating = false;
                })
            },

            handleChangeGtype: function (value) {
                var self = this;
                var actYwSgtypeAllList = this.actYwSgtypeAllList;
                actYwSgtypeAllList.forEach(function (item) {
                    if (item.id === value) {
                        self.actYwStatusForm.regType = item.regType;
                    }
                })
            },

            setActYwStatusForm: function (row) {
                var actYwStatusForm = this.actYwStatusForm;
                for (var k in actYwStatusForm) {
                    if (actYwStatusForm.hasOwnProperty(k)) {
                        actYwStatusForm[k] = row[k] || '';
                    }
                }
            },


            handleAddActYwStatus: function () {
                this.dialogVisibleActYwStatus = true;
            },

            handleEditActYwStatus: function (row) {
                this.dialogVisibleActYwStatus = true;
                this.$nextTick(function () {
                    this.$refs.actYwStatusForm.resetFields();
                    this.setActYwStatusForm(row)
                })
            },

            handleCloseActYwStatus: function () {
                this.$refs.actYwStatusForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleActYwStatus = false;
                    this.actYwStatusForm.alias = '';
                    this.actYwStatusForm.regType = '';
                    this.actYwStatusForm.id = '';
                });
            },


            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getActYwStatusList();
            },

            handlePCPChange: function () {
                this.getActYwStatusList();
            },


            confirmDelActYwStatus: function (row) {
                var self = this;
                this.$confirm('确认删除这条判断条件吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delActYwStatus(row)
                }).catch(function () {

                })
            },

            getRegTypes: function (row) {
                var self = this;
                this.$axios.get('/actyw/actYwStatus/getRegTypes').then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        if (data.data) {
                            self.regTypes = JSON.parse(data.data)
                        }
                    }
                })
            },

            delActYwStatus: function (row) {
                var self = this;
                this.$axios.post('/actyw/actYwStatus/delActYwStatus', {id: row.id}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getActYwStatusList();
                        self.$alert('删除成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            saveActYwStatus: function () {
                var self = this;
                this.$refs.actYwStatusForm.validate(function (valid) {
                    if (valid) {
                        self.postActYwStatus();
                    }
                })
            },

            setAlias: function () {
                var actYwStatusForm = this.actYwStatusForm;
                if (actYwStatusForm.regType === '2') {
                    this.actYwStatusForm.alias = [actYwStatusForm.startNum, actYwStatusForm.endNum].join('-')
                }
            },

            postActYwStatus: function () {
                var self = this;
                this.setAlias();
                this.updating = true;
                this.$axios.post('/actyw/actYwStatus/saveActYwStatus', this.actYwStatusForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getActYwStatusList();
                        self.handleCloseActYwStatus();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.updating = false;
                }).catch(function (error) {
                    self.updating = false;
                })
            },

            getActYwSgtypeAllList: function () {
                var self = this;
                this.$axios.get('/actyw/actYwStatus/getActYwSgtypeAllList').then(function (response) {
                    var data = response.data || {};
                    self.actYwSgtypeAllList = data.data || [];
                })
            },

            getActYwStatusList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/actyw/actYwStatus/getActYwStatusList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status === 1) {
                        pageData = data.data;
                    }
                    self.actYwStatusList = pageData.list || [];
                    self.pageCount = pageData.count || 0;
                    self.searchListForm.pageSize = pageData.pageSize || 1;
                    self.searchListForm.pageNo = pageData.pageNo || 10;
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.tableLoading = false;
                })
            }

        },
        created: function () {
            this.getActYwStatusList();
            this.getActYwSgtypeAllList();
            this.getRegTypes();
        }
    })

</script>
</body>
</html>