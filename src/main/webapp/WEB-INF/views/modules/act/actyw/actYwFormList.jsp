<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>

    <div class="clearfix">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm.sgtype" label="审核类型" :options="regTypes"
                         :default-props="{label: 'name', value: 'id'}" @change="getDataList"></e-condition>
            <e-condition type="radio" v-model="searchListForm.type" label="表单类型" :options="formTypes"
                         :default-props="{label: 'name', value: 'key'}" @change="getDataList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button size="mini" icon="el-icon-circle-plus" type="primary" @click.stop="addData">添加
                </el-button>
            </div>
            <div class="search-input">
                <input type="text" style="display: none">
                <el-input
                        placeholder="请输入表单名称/归属组"
                        size="mini"
                        name="queryStr"
                        v-model="searchListForm.queryStr"
                        @keyup.enter.native="getDataList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>
    </div>

    <div class="table-container">
        <el-table :data="tableList" v-loading="loading" size="mini" class="table" ref="tableList">
            <el-table-column prop="name" align="center" label="表单名称"></el-table-column>
            <el-table-column prop="theme" label="归属组">
                <template slot-scope="scope">
                    {{scope.row.theme | selectedFilter(formThemeEntries)}}
                </template>
            </el-table-column>
            <el-table-column align="center" label="表单类型">
                <template slot-scope="scope">
                    {{scope.row.type | selectedFilter(formTypeEntries)}}
                </template>
            </el-table-column>
            <el-table-column align="center" label="审核类型">
                <template slot-scope="scope">
                    {{scope.row.sgtype | selectedFilter(regTypeEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="path" align="center" label="文件路径">
            </el-table-column>
            <el-table-column align="center" label="对应列表路径">
                <template slot-scope="scope">
                    {{scope.row.listId | selectedFilter(formListEntries)}}
                </template>
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       @click.stop="changeLineData(scope.row)">
                            </el-button>
                        </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop="deleteLineData(scope.row.id)">
                            </el-button>
                        </el-tooltip>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20" v-show="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total, prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>


    <el-dialog :title="dialogTitle + '流程关联表单'" :visible.sync="dialogVisible" :close-on-click-modal="false"
               :before-close="handleClose" width="560px">
        <el-form size="mini" :model="dialogForm" :rules="dialogFormRules" ref="dialogForm" label-width="140px"
                 :disabled="dialogFormDisabled" :close-on-click-modal="false">
            <el-form-item prop="name" label="名称：">
                <el-input v-model="dialogForm.name" style="width:193px;"></el-input>
            </el-form-item>
            <el-form-item prop="theme" label="归属组：">
                <el-select v-model="dialogForm.theme" clearable placeholder="请选择归属组">
                    <el-option v-for="item in formThemes" :key="item.id" :label="item.name" :value="parseInt(item.id)">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="type" label="表单类型：">
                <el-select v-model="dialogForm.type" filterable placeholder="可输入查询选择表单类型">
                    <el-option v-for="item in formTypes" :key="item.key" :label="item.name" :value="item.key">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="sgtype" label="审核类型：">
                <el-select v-model="dialogForm.sgtype" placeholder="请选择审核类型">
                    <el-option v-for="item in regTypes" :key="item.id" :label="item.name" :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="path" label="文件路径：">
                <el-select :disabled="!dialogForm.theme" v-model="dialogForm.path" placeholder="请先选择归属组">
                    <el-option-group
                            v-for="group in formListsTheme"
                            :key="group.filename"
                            :label="group.filename">
                        <el-option
                                v-for="item in group.child"
                                :key="item.path"
                                :label="item.filename"
                                :value="formRoot+group.filename+'/' + item.filename.replace('.jsp', '')">
                        </el-option>
                    </el-option-group>
                </el-select>
            </el-form-item>
            <el-form-item prop="hasListId" label="是否有对应列表：">
                <el-switch v-model="dialogForm.hasListId" active-value="1" inactive-value="0"></el-switch>
            </el-form-item>
            <el-form-item v-if="dialogForm.hasListId === '1'" prop="listId" label="列表文件：">
                <el-select v-model="dialogForm.listId" placeholder="请选择列表文件">
                    <el-option v-for="item in formLists" :key="item.listId" :label="item.name" :value="item.listId">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="remarks" label="备注：">
                <el-input type="textarea" :rows="3" v-model="dialogForm.remarks" class="w300"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="dialogFormDisabled" @click.stop.prevent="handleClose">取 消</el-button>
            <el-button size="mini" :disabled="dialogFormDisabled" type="primary"
                       @click.stop.prevent="saveDialog('dialogForm')">确 定
            </el-button>
        </div>
    </el-dialog>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var regTypes = JSON.parse('${regTypes}');
            var formLists = JSON.parse('${fns:toJson(formLists)}');
            var filelist = JSON.parse('${fns: toJson(filelist)}');
            var formRoot = '${formRoot}';

            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    queryStr: '',
                    type: '',
                    sgtype: ''
                },
                pageCount: 0,
                loading: false,
                tableList: [],
                flowTypes: [],
                formTypes: [],
                formStyleTypes: [],
                formClientTypes: [],
                formThemes: [],
                regTypes: regTypes, //审核类型，
                formLists: formLists,//列表文件
                filelist: filelist,
                dialogVisible: false,
                dialogForm: {
                    theme: '',
                    name: '',
                    type: '',
                    sgtype: '',
                    path: '',
                    listId: '',
                    remarks: '',
                    hasListId: '1'
                },
                formRoot: formRoot,

                isAdd: false,

                dialogFormDisabled: false,

            }
        },
        computed: {
            dialogTitle: {
                get: function () {
                    return this.isAdd ? '添加' : '修改'
                }
            },
            themeKey: function () {
                var theme = this.dialogForm.theme;
                var formThemes = this.formThemes;
                if (!theme) return '';
                for (var i = 0; i < formThemes.length; i++) {
                    if (formThemes[i].id == theme) {
                        return formThemes[i].key
                    }
                }

                return '';
            },
            formListsTheme: function () {
                var themeKey = '';
                var theme = this.dialogForm.theme;
                var formThemes = this.formThemes;
                var filelist = JSON.parse(JSON.stringify(this.filelist));
                for (var i = 0; i < formThemes.length; i++) {
                    if (formThemes[i].id == theme) {
                        themeKey = formThemes[i].key;
                        break;
                    }
                }

                if (!themeKey) return this.filelist;
                return filelist.filter(function (item) {
                    var child = [];
                    if (item.child) {
                        child = item.child.filter(function (item2) {
                            return item2.filename.indexOf(themeKey) > -1;
                        });
                        item.child = child;
                        return child.length > 0;
                    }
                    return false;
                })
            },

            dialogFormRules: {
                get: function () {
                    return {
                        name: [
                            {required: true, message: '请输入名称', trigger: 'blur'}
                        ],
                        theme: [
                            {required: true, message: '请选择归属组', trigger: 'change'}
                        ],
                        type: [
                            {required: true, message: '请选择表单类型', trigger: 'change'}
                        ],
                        sgtype: [
                            {required: true, message: '请选择审核类型', trigger: 'change'}
                        ],
                        path: [
                            {required: true, message: '请填写文件路径', trigger: 'blur'},
                            {max: 258, message: '请输入不大于258位字符', trigger: 'blur'}
                        ],
                        listId: [
                            {required: this.dialogForm.hasListId === '1', message: '请选择对应列表', trigger: 'change'}
                        ],
                        remarks: [
                            {max: 64, message: '请输入不大于64位字符', trigger: 'blur'}
                        ]
                    }
                }
            },

            flowTypeEntries: function () {
                return this.getEntries(this.flowTypes, {label: 'name', value: 'key'})
            },

            formTypeEntries: function () {
                return this.getEntries(this.formTypes, {label: 'name', value: 'key'})
            },

            formStyleTypeEntries: function () {
                return this.getEntries(this.formStyleTypes, {label: 'name', value: 'key'})
            },

            formClientTypeEntries: function () {
                return this.getEntries(this.formClientTypes, {label: 'name', value: 'key'})
            },

            formThemeEntries: function () {
                return this.getEntries(this.formThemes, {label: 'name', value: 'id'})
            },
            formListEntries: function () {
                return this.getEntries(this.formLists, {label: 'name', value: 'id'})
            },

            regTypeEntries: function () {
                return this.getEntries(this.regTypes, {label: 'name', value: 'id'})
            }
        },
        methods: {


            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/actyw/actYwForm/ajaxList', {
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.tableList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                });
            },


            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            addData: function () {
                this.dialogVisible = true;
                this.isAdd = true;
            },

            changeLineData: function (row) {
                this.dialogVisible = true;
                this.isAdd = false;
                this.dialogForm = Object.assign(this.dialogForm, row);
            },
            saveDialog: function (formName) {
                var self = this;
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                });
            },

            saveAjax: function () {
                this.loading = true;
                var self = this;
                this.dialogFormDisabled = true;
                this.$axios.post('/actyw/actYwForm/ajaxSave', this.dialogForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.dialogVisible = false;
                        self.$refs.dialogForm.resetFields();
                        self.getDataList();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        });
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'success'
                        })
                    }
                    self.loading = false;
                    self.dialogFormDisabled = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.dialogFormDisabled = false;
                });
            },

            handleClose: function () {
                this.dialogVisible = false;
                this.$refs.dialogForm.resetFields();
            },

            axiosRequest: function (url, showMsg) {
                var self = this;
                this.loading = true;
                this.$axios.get(url).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getDataList();
                        if (showMsg) {
                            self.$alert('操作成功', '提示', {
                                type: 'success'
                            });
                        }
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'success'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },

            deleteLineData: function (id) {
                var self = this;
                this.$confirm('确认要删除该流程表单吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var url = '/actyw/actYwForm/ajaxDelete?id=' + id;
                    self.axiosRequest(url, true);
                }).catch(function () {

                })
            },


            //获取流程类型
            getFlowTypes: function () {
                var self = this;
                this.$axios.get('/actyw/gnode/ajaxFlowTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.flowTypes = JSON.parse(data.datas);
                    }
                })
            },


            //获取表单类型
            getFormTypes: function () {
                var self = this;
                this.$axios.get('/actyw/gnode/ajaxFormTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.formTypes = JSON.parse(data.datas);
                    }
                })
            },

            //获取表单样式类型
            getFormStyleTypes: function () {
                var self = this;
                this.$axios.get('/actyw/gnode/ajaxFormStyleTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.formStyleTypes = JSON.parse(data.datas);
                    }
                })
            },

            //获取表单客户端类型
            getFormClientTypes: function () {
                var self = this;
                this.$axios.get('/actyw/gnode/ajaxFormClientTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.formClientTypes = JSON.parse(data.datas);
                    }
                })
            },

            //获取归属组
            getFormThemes: function () {
                var self = this;
                this.$axios.get('/actyw/gnode/ajaxFormThemes?isAll=true').then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.formThemes = JSON.parse(data.datas);
                    }
                })
            },

        },

        created: function () {
            this.getDataList();
            this.getFormTypes();
            this.getFormStyleTypes();
            this.getFormClientTypes();
            this.getFormThemes();
        }
    })

</script>

</body>
</html>