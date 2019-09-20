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
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <edit-bar></edit-bar>
    <div class="clearfix">

        <div class="conditions">
            <e-condition label="地区" type="radio" v-model="searchListForm.schoolCity" :default-props="defaultCityProps"
                         name="schoolCity" :options="schoolCities" @change="getDataList">
            </e-condition>
            <e-condition label="类型" type="radio" v-model="searchListForm.schoolType" :default-props="defaultTypeProps"
                         name="schoolType" :options="schoolTypes" @change="getDataList">
            </e-condition>
        </div>

        <div class="search-block_bar clearfix">
            <div class="search-btns">

                <el-button icon="el-icon-circle-plus" size="mini" type="primary" @click.stop.prevent="addTenant">
                    添加
                </el-button>
                <el-tooltip class="item" content="请选择需要删除的租户" :disabled="multipleSelection.length > 0"
                            popper-class="white" placement="bottom">
                        <span>
                            <el-button icon="el-icon-delete" size="mini" type="primary"
                                       :disabled="multipleSelection.length == 0"
                                       @click.stop.prevent="batchDelete">批量删除</el-button>
                        </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="请输入关键字搜索"
                        size="mini"
                        name="keys"
                        v-model="searchListForm.keys"
                        @keyup.enter.native="getDataList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>

            </div>
        </div>
    </div>
    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="pageList" class="table" v-loading="loading"
                  @sort-change="handleTableSortChange" @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column prop="a.school_code" label="代码" align="center" sortable="a.school_code">
                <template slot-scope="scope">
                    {{scope.row.schoolCode}}
                </template>
            </el-table-column>
            <el-table-column prop="schoolName" label="名称" align="center">
            </el-table-column>
            <el-table-column prop="schoolCity" label="所属地区" align="center">
                <template slot-scope="scope">
                    {{scope.row.schoolCity | selectedFilter(schoolCitiesEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="a.school_type" label="类型" align="center" sortable="a.school_type">
                <template slot-scope="scope">
                    {{scope.row.schoolType | selectedFilter(schoolTypesEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="domainName" label="域名" align="center">
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-tooltip v-if="scope.row.status != '1'" class="item" content="开户" popper-class="white"
                                placement="bottom">
                             <el-button class="btn-icon-font" type="text" icon="el-icon-plus"
                                        :disabled="urvo.tenant ? urvo.tenant.indexOf(scope.row.id) > -1 : false"
                                        @click.stop.prevent="openTenant(scope.row.id)">
                            </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       :disabled="urvo.tenant ? urvo.tenant.indexOf(scope.row.id) > -1 : false"
                                       @click.stop.prevent="editTenant(scope.row)">
                            </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop.prevent="singleDelete(scope.row.id)"
                                   :disabled="scope.row.status == '1' || (urvo.tenantDel ? urvo.tenantDel.indexOf(scope.row.id) > -1 : false)">
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
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>

    <el-dialog :title="action" :visible.sync="dialogVisible" :close-on-click-modal="false"
               :before-close="handleClose" width="470px">
        <el-form :model="dialogForm" :rules="dialogFormRules" ref="dialogForm"
                 label-width="100px" size="mini" :disabled="disabled">
            <template v-if="(currentType == '3' && currentTpl == '0') || (currentType == '' && currentTpl == '')">
                <el-form-item prop="schoolCity" label="所属省市：">
                    <span>湖北省</span>
                    <el-select v-model="dialogForm.schoolCity" placeholder="-请选择-" size="mini"
                               style="width: 250px;margin-left: 10px;">
                        <el-option v-for="item in schoolCities" :key="item.id" :label="item.name"
                                   :value="item.code"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="schoolType" label="类型：">
                    <el-select v-model="dialogForm.schoolType" placeholder="-请选择-" size="mini" class="w300">
                        <el-option v-for="item in schoolTypes" :key="item.id" :label="item.name"
                                   :value="item.key"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item prop="schoolCode" label="代码：">
                    <el-input v-model="dialogForm.schoolCode" maxlength="64" class="w300"></el-input>
                </el-form-item>
            </template>
            <el-form-item prop="schoolName" label="名称：">
                <el-input v-model="dialogForm.schoolName" maxlength="64" class="w300"></el-input>
            </el-form-item>
            <el-form-item prop="domainName" label="域名：">
                <el-input v-model="dialogForm.domainName" maxlength="64" class="w300"></el-input>
            </el-form-item>

        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" @click="handleClose">取 消</el-button>
            <el-button size="mini" type="primary" @click.stop.prevent="saveDialog">确 定</el-button>
        </div>
    </el-dialog>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                pageList: [],
                pageCount: 0,
                loading: false,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    schoolCity: '',
                    schoolType: '',
                    keys: ''
                },
                multipleSelection: [],
                multipleSelectedId: [],
                schoolCities: [],
                schoolTypes: [],
                defaultCityProps: {
                    label: 'name',
                    value: 'code'
                },
                defaultTypeProps: {
                    label: 'name',
                    value: 'key'
                },
                action: '添加',
                dialogVisible: false,
                dialogForm: {
                    id: '',
                    schoolProvince: '420000',
                    schoolCity: '',
                    schoolType: '',
                    schoolCode: '',
                    schoolName: '',
                    domainName: ''
                },
                currentType: '',
                currentTpl: '',
                disabled: false
            }
        },
        computed: {
            schoolCitiesEntries: {
                get: function () {
                    return this.getEntries(this.schoolCities, {label: 'name', value: 'code'});
                }
            },
            schoolTypesEntries: {
                get: function () {
                    return this.getEntries(this.schoolTypes, {label: 'name', value: 'key'});
                }
            },
            dialogFormRules: function () {
                var self = this;
                var numReg = /^[0-9]+$/;
                var fieldTip = {
                    schoolCode: '代码重复',
                    schoolName: '名称重复',
                    domainName: '域名重复'
                };
                var validateRepeat = function (rule, value, callback) {
                    if (value) {
                        var p = {};
                        if (rule.field == 'schoolCode' && !numReg.test(value)) {
                            return callback(new Error("请输入数字"))
                        }
                        p.id = self.dialogForm.id;
                        p[rule.field] = value;
                        return self.$axios.get('/sys/tenant/validTenant', {
                            params: p
                        }).then(function (response) {
                            if (response.data.status == '1') {
                                return callback();
                            }
                            return callback(new Error(fieldTip[rule.field]));
                        }).catch(function () {
                            return callback(new Error("请求失败"))
                        })
                    }
                    return callback();
                };
                return {
                    schoolCity: {required: true, message: '请选择城市', trigger: 'change'},
                    schoolType: {required: true, message: '请选择类型', trigger: 'change'},
                    schoolCode: [
                        {required: true, message: '请输入代码', trigger: 'blur'},
                        {validator: validateRepeat, trigger: 'blur'}
                    ],
                    schoolName: [
                        {required: true, message: '请输入名称', trigger: 'blur'},
                        {validator: validateRepeat, trigger: 'blur'}
                    ],
                    domainName: [
                        {required: true, message: '请输入域名', trigger: 'blur'},
                        {validator: validateRepeat, trigger: 'blur'}
                    ]
                }
            },
            multipleCanDeleteId: function () {
                var self = this;
                return this.multipleSelection.filter(function (item) {
                    return item.status != '1' && (self.urvo.tenantDel ? self.urvo.tenantDel.indexOf(item.id) == -1 : true);
                }).map(function (value) {
                    return value.id;
                });
            },
            multipleCanDeleteNames: function () {
                var self = this;
                return this.multipleSelection.filter(function (item) {
                    return self.multipleCanDeleteId.indexOf(item.id) > -1;
                }).map(function (value) {
                    return value.schoolName;
                });
            },
            multipleCanNotDeleteNames: function () {
                var self = this;
                return this.multipleSelection.filter(function (item) {
                    return self.multipleCanDeleteId.indexOf(item.id) == -1;
                }).map(function (value) {
                    return value.schoolName;
                });
            }
        },
        methods: {
            addTenant: function () {
                this.dialogVisible = true;
                this.action = '添加';
            },
            editTenant: function (row) {
                this.dialogVisible = true;
                this.action = '编辑';
                this.dialogForm.id = '';
                this.$nextTick(function () {
                    this.dialogForm = Object.assign({}, row);
                    this.currentType = row.type;
                    this.currentTpl = row.isTpl;
                });
            },
            saveDialog: function () {
                var self = this;
                this.$refs.dialogForm.validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                });
            },
            saveAjax: function () {
                var self = this;
                this.disabled = true;
                this.$axios.post('/sys/tenant/ajaxSaveTenant', self.dialogForm).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.handleClose();
                        self.getDataList();
                    }
                    self.$message({
                        message: data.status == '1' ? '保存成功' : '保存失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                    self.disabled = false;
                }).catch(function (error) {
                    self.disabled = false;
                    self.$message({
                        message: error.response.data || '操作失败',
                        type: 'error'
                    })
                });
            },
            handleClose: function () {
                this.$refs.dialogForm.resetFields();
                this.dialogForm.id = '';
                this.dialogForm.schoolCode = '';
                this.currentType = '';
                this.currentTpl = '';
                this.$nextTick(function () {
                    this.dialogVisible = false;
                });
            },
            openTenant: function (id) {
                var self = this;
                this.$confirm('确认开户?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var loading = self.$loading({
                        lock: true,
                        text: '开户中...',
                        spinner: 'el-icon-loading',
                        background: 'rgba(0, 0, 0, 0)',
                        customClass: 'gray-green-loading'
                    });
                    self.$axios({
                        method: 'GET',
                        url: '/sys/tenant/ajaxOpen?id=' + id,
                        timeout: 1000 * 60 * 30
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                            window.parent.tenantApp.getData();
                        }
                        loading.close();
                        self.$message({
                            message: data.status == '1' ? '开户成功' : '开户失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                        loading.close();
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })
            },
            batchDelete: function () {
                var self = this, d = [], tip = '', canNames = this.multipleCanDeleteNames, notNames = this.multipleCanNotDeleteNames;
                if (this.multipleSelectedId.length == 0) {
                    this.$message({
                        message: '请选择要删除的数据',
                        type: 'warning'
                    });
                    return false;
                }
                if (canNames.length == 0) {
                    tip = '所选的租户均不能删除，请重新选择';
                } else {
                    tip = notNames.length > 0 ? notNames.join('、') + '不能删除！' + canNames.join('、') + '将会删除，是否继续？' : '确定删除？';
                }
                this.multipleCanDeleteId.forEach(function (v) {
                    d.push({id: v});
                });
                this.$confirm(tip, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    if (d.length == 0) {
                        return false;
                    }
                    self.$axios.post('/sys/tenant/ajaxDeleteTenant', d).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? data.msg || '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })
            },
            singleDelete: function (id) {
                var self = this;
                var d = [{id: id}];
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios.post('/sys/tenant/ajaxDeleteTenant', d).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })
            },
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/sys/tenant/tenantList?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败！',
                        type: 'error'
                    })
                })
            },
            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push({id: value[i].id});
                }
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            getSchoolCity: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolCityList').then(function (response) {
                    var data = response.data.data || [];
                    self.schoolCities = data || [];
                }).catch(function (error) {
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                })
            },
            getSchoolType: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (response) {
                    var data = response.data;
                    self.schoolTypes = JSON.parse(data.data) || [];
                }).catch(function (error) {
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                })
            }

        },
        created: function () {
            this.getDataList();
            this.getSchoolCity();
            this.getSchoolType();
        }
    })

</script>
</body>
</html>