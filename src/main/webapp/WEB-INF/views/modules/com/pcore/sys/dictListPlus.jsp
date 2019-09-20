<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

    <style>
        .dict-type-list{
            padding: 0;
            margin: 0;
            overflow: hidden;
        }
        .dict-type-list .dict-type-item{
            position: relative;
            padding-right: 60px;
        }
        .dict-type-list .dict-type-item>a{
            display: block;
            color: #333;
            line-height: 24px;
            text-overflow: ellipsis;
            white-space: nowrap;
            overflow: hidden;
        }
        .dict-type-list .dict-type-item .dict-type-actions{
            display: none;
            position: absolute;
            right: 0;
            top: 0;
            font-size: 0;
            width: 60px;
            height: 24px;
            line-height: 24px;
            overflow: hidden;
        }
        .dict-type-list .dict-type-actions>a{
            display: inline-block;
            width: 50%;
            font-size: 12px;
            text-align: center;
            vertical-align: top;
        }
        .dict-type-list .dict-type-item:hover>a{
            color: #E9432D;
        }
        .dict-type-list .dict-type-item:hover .dict-type-actions{
            display: block;
        }
        .dict-type-list .dict-type-item.active>a{
            color: #E9432D;
        }
        .dict-type-list .dict-type-item.active .dict-type-actions{
            display: block;
        }
        .dict-types-body{
            overflow: auto;
        }

        .scroll-bar-style::-webkit-scrollbar{
            width: 10px;
            background-color: #fff;
        }

        .scroll-bar-style::-webkit-scrollbar-track{
            /*-webkit-box-shadow: inset 0 0 6px rgba(0,0,0,0.3);*/
        }

        .scroll-bar-style::-webkit-scrollbar-thumb {
            border-radius: 10px;
            background-color: hsla(220, 4%, 58%, .3);
            /*outline: 1px solid slategrey;*/
        }

    </style>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <el-container :style="{height: containerHeight + 'px'}">
        <el-aside width="310px">
            <div class="mgb-20">
                <el-input size="mini" :placeholder="selectDictName || '请输入字典类型'" v-model="dictType" style="width: 240px"
                          @change="handleChangeDictType">
                    <i slot="suffix" class="el-input__icon el-icon-search"></i>
                </el-input>
                <el-button type="primary" size="mini" :disabled="dictTypesComputed.length > 0"
                           @click.stop.prevent="handleAddDictType">添加
                </el-button>
            </div>
            <div class="dict-types-body scroll-bar-style" :style="{height: (containerHeight - 50) + 'px'}">
                <ul class="dict-type-list">
                    <li v-for="item in dictTypesComputed" :key="item.id" class="dict-type-item" :class="{active: item.id === searchListForm.typeid}">
                        <a href="javascript: void(0);" @click.stop.prevent="handleSelectDictType(item)">
                            {{item.label}}
                        </a>
                        <div class="dict-type-actions">
                            <a href="javascript: void(0);" title="编辑" @click.stop.prevent="handleEditDictType(item)"><i
                                    class="el-icon-edit"></i></a>
                            <a href="javascript: void(0);" title="删除" @click.stop.prevent="handleDelDictType(item)"><i
                                    class="el-icon-delete"></i></a>
                        </div>
                    </li>
                </ul>
                <div v-show="dictTypesComputed.length == 0" class="empty-color empty text-center">
                    数据不存在，可点击添加按钮添加
                </div>
            </div>
        </el-aside>
        <el-main class="scroll-bar-style">
            <div class="text-right mgb-20">
                <el-input size="mini" placeholder="关键字搜索" v-model="searchListForm.name" style="width: 240px">
                    <i slot="suffix" class="el-input__icon el-icon-search"></i>
                </el-input>
                <el-button type="primary" size="mini" @click.stop.prevent="handleAddDict">添加</el-button>
            </div>
            <div class="table-container" v-loading="dictListLoading">
                <el-table :data="dictList" size="small" class="table">
                    <el-table-column label="字典名称">
                        <template slot-scope="scope">
                            {{scope.row.label}}
                        </template>
                    </el-table-column>
                    <el-table-column label="字典类型" align="center">
                        <template slot-scope="scope">
                            {{scope.row.type_name}}
                        </template>
                    </el-table-column>
                    <el-table-column label="字典排序" align="center">
                        <template slot-scope="scope">
                            {{scope.row.sort}}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center">
                        <template slot-scope="scope">
                            <div class="table-btns-action">
                                <el-button type="text" size="mini" @click.stop.prevent="handleEditDict(scope.row)">编辑
                                </el-button>
                                <el-button type="text" size="mini" @click.stop.prevent="handleDelDict(scope.row)">删除
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
        </el-main>
    </el-container>
    <el-dialog
            :title="isAddDict ? '添加字典' : '修改字典'"
            :visible.sync="dialogVisibleDict"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseDict">
        <el-form :model="dictForm" ref="dictForm" :rules="dictRules" size="mini" label-width="100px">
            <el-form-item prop="typeid" label="字典类型：">
                <el-select v-model="dictForm.typeid" name="typeid" :disabled="!isAddDict" filterable
                           placeholder="请输入字典名称" style="width: 100%">
                    <el-option
                            v-for="item in dictTypes"
                            :key="item.id"
                            :label="item.label"
                            :value="item.id">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="name" label="字典名称：">
                <el-input v-model="dictForm.name" name="name"></el-input>
            </el-form-item>
            <el-form-item prop="sort" label="排序：">
                <el-input-number v-model="dictForm.sort" :min="0" :max="100000"></el-input-number>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisibleDict = false" size="mini">取 消</el-button>
    <el-button type="primary" @click="saveDict" size="mini">确 定</el-button>
  </span>
    </el-dialog>
    <el-dialog
            :title="isAddDictType ? '添加字典类型' : '修改字典类型'"
            :visible.sync="dialogVisibleDictType"
            width="520px"
            :close-on-click-modal="false"
            :before-close="handleCloseDictType">
        <el-form :model="dictTypeForm" ref="dictTypeForm" :rules="dictTypeRules" size="mini" label-width="120px">
            <el-form-item prop="name" label="字典类型名称：">
                <el-input v-model="dictTypeForm.name" name="name"></el-input>
            </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
    <el-button @click="dialogVisibleDictType = false" size="mini">取 消</el-button>
    <el-button type="primary" @click="saveDictType" size="mini">确 定</el-button>
  </span>
    </el-dialog>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                dictType: '',
                dictTypes: [],
                dictList: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    typeid: '',
                    name: ''
                },
                pageCount: 0,
                selectDictName: '',

                dictListLoading: true,

                dialogVisibleDict: false,
                isAddDict: false,
                dictForm: {
                    name: '',
                    sort: '',
                    id: '',
                    typeid: ''
                },
                dictRules: {
                    typeid: [{required: true, message: '请选择字典类型', trigger: 'change'}],
                    name: [
                        {required: true, message: '请输入字典名称', trigger: 'blur'},
                        {max: 24, message: '请输入1-24位间字符', trigger: 'blur'}
                    ]
                },

                dialogVisibleDictType: false,
                isAddDictType: false,
                dictTypeForm: {
                    id: '',
                    name: ''
                },
                dictTypeRules: {
                    name: [
                        {required: true, message: '请输入字典类型名称', trigger: 'blur'},
                        {max: 64, message: '请输入1-64位间字符', trigger: 'blur'}
                    ]
                },

                containerHeight: 0,
                timeout: null
            }
        },
        computed: {
            dictTypesComputed: function () {
                var dictType = this.dictType;
                if (!dictType) return this.dictTypes;
                return this.dictTypes.filter(function (item) {
                    return item.label.indexOf(dictType) > -1;
                })
            }
        },
        watch: {
            'searchListForm.name': function (value) {
                var self = this;
                clearTimeout(this.timeout);
                this.dictListLoading = true;
                this.timeout = setTimeout(function () {
                    self.searchListForm.pageNo = 1;
                    self.getDictList();
                }, 2000 * Math.random());
            }
        },
        methods: {


            handleEditDict: function (row) {
                this.isAddDict = false;
                this.dialogVisibleDict = true;
                this.$nextTick(function () {
                    this.$refs.dictForm.resetFields();
                    this.setDictValues(row);
                })
            },

            handleAddDict: function () {
                this.isAddDict = true;
                this.dialogVisibleDict = true;
                this.$nextTick(function () {
                    this.$refs.dictForm.resetFields();
                    if (this.searchListForm.typeid) {
                        this.dictForm.typeid = this.searchListForm.typeid
                    }
                })
            },

            handleDelDict: function (row) {
                var self = this;
                this.$confirm('此操作将删除这条字典，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delDict(row);
                }).catch(function () {

                })
            },

            delDict: function (row) {
                var self = this;
                this.$axios.get('/sys/dict/delDictType?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.getDictList();
                    }
                    self.$message({
                        type: data.ret == '1' ? 'success' : 'error',
                        message: data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            handleCloseDict: function () {
                this.$refs.dictForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleDict = false;
                })
            },

            saveDict: function () {
                var self = this;
                var urlPath = this.isAddDict ? 'addDict' : 'edtDict';
                this.$axios.get('/sys/dict/' + urlPath + '?' + Object.toURLSearchParams(this.dictForm)).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.getDictList();
                        self.dialogVisibleDict = false;
                    }
                    self.$message({
                        type: data.ret == '1' ? 'success' : 'error',
                        message: data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            handleCloseDictType: function () {
                this.$refs.dictTypeForm.resetFields();
                this.$nextTick(function () {
                    this.dialogVisibleDictType = false;
                })
            },

            handleEditDictType: function (row) {
                this.isAddDictType = false;
                this.dialogVisibleDictType = true;
                this.$nextTick(function () {
                    this.$refs.dictTypeForm.resetFields();
                    this.dictTypeForm.name = row.label;
                    this.dictTypeForm.id = row.id;
                })
            },

            handleAddDictType: function () {
                this.isAddDictType = true;
                this.dictTypeForm.id = '';
                this.dictTypeForm.name = this.dictType;
                this.saveDictType();
            },

            saveDictType: function () {
                var self = this;
                var urlPath = this.isAddDictType ? 'addDictType' : 'edtDictType';
                this.$axios.get('/sys/dict/' + urlPath + '?' + Object.toURLSearchParams(this.dictTypeForm)).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.getDictTypes();
                        self.dialogVisibleDictType = false;
                    }
                    self.$message({
                        type: data.ret == '1' ? 'success' : 'error',
                        message: data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },


            handleDelDictType: function (row) {
                var self = this;
                this.$confirm('此操作将删除这条字典类型，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delDictType(row);
                }).catch(function () {

                })
            },

            delDictType: function (item) {
                var self = this;
                this.$axios.get('/sys/dict/delDictType?id=' + item.id).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.getDictTypes();
                    }
                    self.$message({
                        type: data.ret == '1' ? 'success' : 'error',
                        message: data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            setDictValues: function (row) {
                var dictForm = this.dictForm;
                dictForm.name = row.label;
                dictForm.id = row.id;
                dictForm.sort = row.sort;
                dictForm.typeid = row.parent_id;
            },

            handleChangeDictType: function (value) {
                if (!value && this.searchListForm.typeid) {
                    this.searchListForm.typeid = '';
                    this.dictForm.typeid = '';
                    this.selectDictName = '';
                    this.searchListForm.pageSize = 10;
                    this.searchListForm.pageNo = 1;
                    this.getDictList();
                }
            },

            getDictTypes: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictTypeListPlus').then(function (response) {
                    self.dictTypes = response.data || [];
                })
            },
            getDictList: function () {
                var self = this;
                this.dictListLoading = true;
                this.$axios.get('/sys/dict/getDictPagePlus?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.dictList = data.list;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.pageCount = data.count;
                    } else {
                        self.$message({
                            type: 'error',
                            message: self.xhrErrorMsg
                        })
                    }
                    self.dictListLoading = false;
                }).catch(function () {
                    self.dictListLoading = false;
                })
            },
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDictList();
            },

            handlePCPChange: function () {
                this.getDictList();
            },

            handleSelectDictType: function (item) {
                this.searchListForm.typeid = item.id;
                this.searchListForm.pageSize = 10;
                this.searchListForm.pageNo = 1;
                this.getDictList();

                //dictForm typeid;
//                this.dictForm.typeid = item.id;
                this.selectDictName = item.label;
            },
            
            getContainerHeight: function () {
                return (window.innerHeight - 40 - 20 - 15);
            }
        },
        beforeMount: function () {
            this.containerHeight = this.getContainerHeight();
            this.getDictTypes();
            this.getDictList();
        }
    })

</script>
</body>
</html>