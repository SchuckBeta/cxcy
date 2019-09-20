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

<div id="app" class="container-fluid mgb-60 user-share" v-show="pageLoad" style="display: none;">
    <edit-bar></edit-bar>

    <el-form :model="searchListForm" size="mini" autocomplete="off" ref="searchListForm">
        <div class="conditions">
            <e-condition :label="collegeLabel" type="radio" :options="academyList" v-model="searchListForm['office.id']"
                         name="'office.id"
                         :default-props="defaultProps" @change="getDataList"></e-condition>
            <e-condition label="用户角色" type="radio" :options="roleListOptions" v-model="searchListForm.roleId"
                         name="roleId"
                         :default-props="defaultProps" @change="getDataList"></e-condition>
        </div>

        <div class="search-block_bar clearfix">
            <shiro:hasPermission name="sys:user:edit">
                <div class="search-btns">
                    <el-button type="primary" size="mini" @click.stop.prevent="createUser">
                        <i class="el-icon-circle-plus el-icon--left"></i>创建用户
                    </el-button>
                    <el-button type="primary" :disabled="multipleSelectedId.length == 0" size="mini"
                               @click.stop.prevent="batchDelete(multipleSelectedId)">
                        <i class="iconfont icon-delete"></i>批量删除
                    </el-button>
                </div>
            </shiro:hasPermission>
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input @keyup.enter.native="getDataList"
                          name="queryStr"
                          placeholder="登录名/姓名/学号/手机号/工号/技术领域"
                          v-model="searchListForm.queryStr"
                          size="mini"
                          class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="multipleTable" class="table" v-loading="loading"
                  @selection-change="handleSelectionChange" size="mini">
            <el-table-column
                    type="selection" :selectable="selectable"
                    width="60">
            </el-table-column>
            <el-table-column width="320" label="用户信息">
                <template slot-scope="scope">
                    <div class="user-element">
                        <div class="user-img">
                            <img :src="scope.row.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                        </div>
                        <div class="user-detail">
                            <div class="user-name">
                                <img src="${ctxImages}/user-name.png" alt="">
                                {{scope.row.name || '-'}}
                            </div>
                            <div class="user-tag">
                                <img src="${ctxImages}/user-no.png" alt="">
                                {{scope.row.no || '-'}}
                            </div>
                            <div class="user-phone">
                                <img src="${ctxImages}/user-phone.png" alt="">
                                {{scope.row.mobile || '-'}}
                            </div>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="用户角色" align="center">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.roleNames" popper-class="white" placement="right"><span
                            class="break-ellipsis">{{scope.row.roleNames}}</span></el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="登录名" align="center">
                <template slot-scope="scope">
                    {{scope.row.loginName || '-'}}
                </template>
            </el-table-column>
            <el-table-column :label="collegeLabel" align="center">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row | getUserCollege(collegeEntries)" popper-class="white"
                                placement="right">
                            <span class="break-ellipsis">
                                {{scope.row | getUserCollege(collegeEntries)}}
                            </span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="技术领域" align="center">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.domainlt" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.domainlt}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <shiro:hasPermission name="sys:user:edit">
                <el-table-column label="操作" align="center" width="160">
                    <template slot-scope="scope">
                        <el-tooltip
                                v-show="((parseInt(scope.row.auth) || 40) > parseInt(loginCurUser.auth)) || scope.row.id == loginCurUser.id"
                                class="item" content="重置密码"
                                popper-class="white" placement="bottom">
                            <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-setting" @click.stop.prevent="resetPwd(scope.row.id)"></el-button>
                                </span>
                        </el-tooltip>
                        <el-tooltip
                                v-show="((parseInt(scope.row.auth) || 40) > parseInt(loginCurUser.auth)) || scope.row.id == loginCurUser.id"
                                class="item" content="修改"
                                popper-class="white" placement="bottom">
                             <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit" @click.stop="amendUser(scope.row)"></el-button>
                                 </span>
                        </el-tooltip>
                        <el-tooltip v-show="((parseInt(scope.row.auth) || 40) > parseInt(loginCurUser.auth))"
                                    class="item" content="删除"
                                    popper-class="white" placement="bottom">
                            <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete" @click.stop="deleteData(scope.row.id)"></el-button>
                                </span>
                        </el-tooltip>
                    </template>
                </el-table-column>
            </shiro:hasPermission>

        </el-table>
        <div class="text-right mgb-20" v-if="pageCount">
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
    <shiro:hasPermission name="sys:user:edit">
        <el-dialog :title="dialogAction + '用户'" top="5vh" :visible.sync="dialogCreateVisible"
                   :before-close="handleClose"
                   :close-on-click-modal="false"
                   class="dialog-form-condition" width="60%">
            <el-form ref="createUserForm" :model="createUserForm" label-width="120px" :disabled="updating"
                     method="post" size="mini" class="demo-ruleForm" auto-complete="off">
                <div class="gray-box">
                    <p class="gray-box-title"><span>必填信息</span> <span v-show="!!createUserForm.id">创建时间：{{createUserForm.createDate}}</span>
                    </p>
                    <div class="gray-box-content">
                        <el-form-item label="登录名：" prop="loginName" :rules="loginNameRules" style="width:50%;">
                            <el-input name="loginName" maxlength="64" v-model="createUserForm.loginName"></el-input>
                        </el-form-item>
                        <el-form-item label="姓名：" :rules="userNameRules" prop="name" style="width:50%;">
                            <el-input name="name" maxlength="64" v-model="createUserForm.name"></el-input>
                        </el-form-item>
                        <el-form-item label="学号/工号：" prop="no" :rules="userAsStuNoRules" style="width:50%;">
                            <el-input name="no" maxlength="64" v-model="createUserForm.no"></el-input>
                        </el-form-item>
                        <el-form-item label="用户角色：" prop="roleIdList" :rules="roleRules">
                            <el-checkbox-group v-model="createUserForm.roleIdList" name="roleIdList"
                                               @change="checkRoles">
                                <el-checkbox v-for="role in cpRoleList" :label="role.id" :key="role.id"
                                             :disabled="parseInt(role.auth) <= parseInt(loginCurUser.auth)">
                                    {{role.name}}
                                </el-checkbox>
                            </el-checkbox-group>
                        </el-form-item>
                        <el-form-item label="导师来源：" prop="teacherType" v-if="teacherTypeShow">
                            <el-radio-group v-model="createUserForm.teacherType" id="teacherType" name="teacherType">
                                <el-radio v-for="type in teacherTypes" :label="type.value" :key="type.id">{{type.label}}
                                </el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="所属机构：" prop="cascaderList" :rules="stuProfessionalRules">
                            <el-cascader :options="collegesTree" clearable
                                         style="width: 60%" filterable placeholder="请选择所属机构（可搜索）"
                                         :props="cascaderProps"
                                         change-on-select
                                         v-model="createUserForm.cascaderList">
                            </el-cascader>
                        </el-form-item>
                    </div>
                </div>
                <div class="gray-box">
                    <p>选填信息</p>
                    <div class="gray-box-content">
                        <el-form-item label="擅长技术领域：" prop="domainIdList">
                            <el-select v-model="createUserForm.domainIdList" name="domainIdList" multiple
                                       style="width: 90%"
                                       placeholder="请选择擅长的技术领域">
                                <el-option
                                        v-for="item in allDomains"
                                        :key="item.id"
                                        :label="item.label"
                                        :value="item.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                        <el-form-item label="手机号：" prop="mobile" :rules="mobileRules" style="width:50%;">
                            <el-input id="mobile" name="mobile" v-model="createUserForm.mobile"></el-input>
                        </el-form-item>
                        <el-form-item label="电子邮箱：" prop="email" :rules="emailRules" style="width:50%;">
                            <el-input id="email" name="email" v-model="createUserForm.email"></el-input>
                        </el-form-item>
                    </div>
                </div>
            </el-form>

            <div slot="footer" class="dialog-footer" style="text-align: center">
                <el-button size="mini" @click.stop.prevent="handleClose">取 消</el-button>
                <el-button size="mini" type="primary" :disabled="updating"
                           @click.stop.prevent="submitCreateUser('createUserForm')">保 存
                </el-button>
            </div>
        </el-dialog>
    </shiro:hasPermission>

</div>


<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.verifyRules],
        data: function () {
            return {
                pageList: [],
                pageCount: 0,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    queryStr: '',
                    'office.id': '',
                    roleId: ''
                },
                colleges: [],
                collegesTree: [],
                collegeEntries: {},
                officeEntity: {},
                roleList: [],
                teacherTypes: [],
                expertFrom: [],
                allDomains: [],
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                cascaderProps: {
                    label: 'name',
                    value: 'id',
                    childern: 'children'
                },
                loading: false,
                dialogAction: '',
                dialogCreateVisible: false,
                stuTea: false,
                createUserForm: {
                    id: '',
                    loginName: '',
                    name: '',
                    no: '',
                    mobile: '',
                    email: '',
                    teacherType: '1',
                    professional: '',
                    officeId: '',
                    roleIdList: [],
                    domainIdList: [],
                    cascaderList: [],
                    createDate: ''
                },
                multipleSelection: [],
                multipleSelectedId: [],
                updating: false,
                hideRoleNames: ['13757518f4da45ecaa32a3b582e8396a'],
                roleAuth: [],
                roleRules: [{required: true, message: '请选择角色', trigger: 'change'}]
            }
        },
        computed: {
            collegeLabel: function () {
                if(this.currTenantId === '10'){
                    return '学院'
                }
                if (!this.showActrel) {
                    return '学院/专业'
                }
                return '机构'
            },
            userId: function () {
                return this.createUserForm.id;
            },

            loginName: function () {
                return this.createUserForm.loginName;
            },

            userAsStuNoRules: function () {
                var userNoRules = this.userNoRules;
                var rules = [{required: true, message: '请输入学号/工号', trigger: 'blur'}]
                return rules.concat(userNoRules);
            },

            stuProfessionalRules: function () {
                var professionRules = this.professionRules;
                var rules = [{required: true, message: '请选择所属机构', trigger: 'change'}];
                if (this.roleTypes.length === 1 && this.roleTypes.indexOf('1') > -1) {
                    return rules.concat(professionRules);
                }
                return rules;
            },

            rolesJs: function () {
                var entity = {};
                for (var i = 0; i < this.roleList.length; i++) {
                    var item = this.roleList[i];
                    entity[item.id] = item.bizType;
                }
                return entity;
            },

            academyList: function () {
                var collegesTree = this.collegesTree;
                var list = [];
                collegesTree.forEach(function (item) {
                    if (item.children && item.children.length > 0) {
                        list = list.concat(item.children);
//                        list = list.concat(item);
                    }
                });
                return list;
            },

            roleAuthEntries: function () {
                return this.getEntries(this.roleAuth, {label: 'value', value: 'id'});
            },

            roleListOptions: function () {
                var self = this;
                this.roleList.forEach(function (item) {
                    item.auth = (self.roleAuthEntries ? self.roleAuthEntries[item.id] : Number.MAX_VALUE) || 40;
                });
                return this.roleList.sort(function (item1, item2) {
                    var auth1 = parseInt(item1.auth);
                    var auth2 = parseInt(item2.auth);
                    if (auth1 < auth2) {
                        return 1
                    } else if (auth1 > auth2) {
                        return -1
                    } else {
                        return 0
                    }
                })

            },

            cpRoleList: function () {
                var self = this, names = this.hideRoleNames;
                return this.roleList.filter(function (item) {
                    return names.indexOf(item.id) === -1
                })
            },

            cpRoleNames: function () {
                var names = this.hideRoleNames;
                return (this.roleList.filter(function (item) {
                    return names.indexOf(item.id) === -1
                })).map(function (item) {
                    return item.name;
                })
            },

            roleTypes: function () {
                var rolesJs = this.rolesJs;
                return this.createUserForm.roleIdList.map(function (item) {
                    return rolesJs[item];
                })
            },

            teacherTypeShow: function () {
                return this.roleTypes.indexOf('2') > -1;
            }
        },
        methods: {
            getDictLists: function () {
                var dicts = {
                    'technology_field': 'allDomains',
                    'master_type': 'teacherTypes',
                    'expert_source': 'expertFrom'
                };
                this.getBatchDictList(dicts)

            },

            getRoleList: function () {
                var self = this;
                this.$axios.get('/sys/role/getRoleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.roleList = data.data || [];
                    }
                })
            },

            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var colleges = data.data || [];
                        var treeEntity = self.listToTreeEntity(colleges);
                        self.colleges = colleges;
                        self.collegesTree = JSON.parse(JSON.stringify(treeEntity.tree));
                        self.collegeEntries = treeEntity.entity;
                        self.officeEntity = treeEntity.entity;
                    }
                })
            },

            checkRoles: function (value) {
                var roleTypes = this.roleTypes;
                if (roleTypes.indexOf('1') > -1 && roleTypes.indexOf('2') > -1) {
                    this.$message({
                        message: '不能同时选择学生和导师角色',
                        type: 'warning',
                        duration: 1000
                    });
                    this.createUserForm.roleIdList.splice(1, value.length - 1)
                }
            },


            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/sys/user/getUserList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },


            createUser: function () {
                this.dialogAction = '创建';
                this.dialogCreateVisible = true;
            },

            setUserForm: function (userData) {
                var parentIds;
                var professional = userData.professional || userData.officeId;
                if (professional && this.officeEntity[professional]) {
                    parentIds = this.officeEntity[professional].parentIds;
                }
                if (parentIds) {
                    parentIds = parentIds.split(',');
                    parentIds = parentIds.slice(1, parentIds.length - 1);
                    parentIds.push(professional);
                    userData.cascaderList = parentIds
                }
                userData.roleIdList = this.getRoleId(userData.roleNames);
                this.assignFormData(this.createUserForm, userData);
            },

            amendUser: function (row) {
                this.dialogAction = '修改';
                this.dialogCreateVisible = true;
                var rowCopy = JSON.parse(JSON.stringify(row));
                this.$nextTick(function () {
                    this.setUserForm(rowCopy);
                })
            },

            submitCreateUser: function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                })
            },

            saveAjax: function () {
                var self = this;
                this.loading = true;
                this.updating = true;
                var createUserForm = JSON.parse(JSON.stringify(self.createUserForm));
                var cascaderList = createUserForm.cascaderList;
                if (!this.teacherTypeShow) {
                    delete createUserForm.teacherType;
                }
                if (cascaderList.length > 2) {
                    createUserForm.professional = cascaderList[2];
                    createUserForm.officeId = cascaderList[1];
                } else {
                    createUserForm.professional = '';
                    createUserForm.officeId = cascaderList[cascaderList.length - 1];
                }
                createUserForm.domainIdList = createUserForm.domainIdList.join(',');
                createUserForm.roleIdList = createUserForm.roleIdList.join(',');
                delete createUserForm.cascaderList;
                this.$axios({
                    method: 'GET',
                    url: '/sys/user/ajaxSaveUser',
                    params: createUserForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                        self.handleClose();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.loading = false;
                    self.updating = false;
                }).catch(function () {
                    self.loading = false;
                    self.updating = false;
                })
            },

            handleClose: function () {
                this.createUserForm.id = '';
                this.createUserForm.professional = '';
                this.createUserForm.officeId = '';
                this.$refs.createUserForm.resetFields();
                this.dialogCreateVisible = false;
            },

            getRoleId: function (roleNames) {
                var arr = roleNames.split(',');
                var lists = this.roleList;
                var ids = [];
                arr.forEach(function (value) {
                    lists.forEach(function (item) {
                        if (item.name == value) {
                            ids.push(item.id);
                        }
                    })
                });
                return ids;
            },


            resetPwd: function (id) {
                var self = this;
                this.$confirm('确认要重置密码吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: '/sys/user/resetPassword',
                        data: {
                            id: id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        self.$message({
                            message: data.status == '1' ? '重置密码成功！密码已重置为：123456' : '重置密码失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    })
                }).catch(function () {

                })
            },
            deleteData: function (id) {
                var self = this;
                this.$confirm('确认删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: '/sys/user/delUser',
                        data: {
                            id: id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });

                        self.$axios({
                            method: 'GET',
                            url: '/cas/ajaxDeleteBy/' + id
                        }).then(function (response) {
                            var data = response.data;
                        })
                    }).catch(function () {
                        self.$message({
                            message: '请求失败',
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })

            },

            batchDelete: function (ids) {
                var self = this;
                this.$confirm('确认删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: '/sys/user/ajaxDelUser',
                        data: {
                            ids: ids
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    })
                }).catch(function () {

                })
            },

            handleSelectionChange: function (val) {
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].id);
                }
            },

            selectable: function (row) {
                return row.admin == false;
            },

            getUserRoleAuth: function () {
                var self = this;
                this.$axios.post('/sys/role/getUserRoleAuth', {id: this.loginCurUser.id}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        if (data.data.auth) {
                            Vue.set(self.loginCurUser, 'auth', parseInt(data.data.auth));
                        }
                    }
                })
            },

            getRoleAuth: function () {
                var self = this;
                this.$axios.get('/sys/role/getRoleAuth').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.roleAuth = data.data || [];
                    }
                })
            }
        },
        mounted: function () {
            this.getDataList();
            this.getDictLists();
            this.getRoleList();
            this.getOfficeList();
            this.getRoleAuth();
            this.getUserRoleAuth();
        }
    })

</script>


</body>

</html>