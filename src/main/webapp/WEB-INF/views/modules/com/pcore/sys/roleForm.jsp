<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>

<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="roleForm.id ? '修改': '添加'"></edit-bar>
    </div>
    <el-form :model="roleForm" ref="roleForm" :rules="roleRules" size="mini" label-width="120px" auto-complete="off"
             :disabled="roleFormDisabled" style="width: 540px;">
        <el-form-item prop="name" label="角色名称：">
            <el-input name="name" v-model="roleForm.name" maxlength="24" placeholder="最大24位字符"></el-input>
        </el-form-item>
        <el-form-item prop="rtype" label="角色业务类型：">
            <el-select name="rtype" v-model="roleForm.rtype">
                <el-option v-for="item in rtypeList" :key="item.key" :label="item.remark"
                           :value="item.key"></el-option>
            </el-select>
        </el-form-item>
        <%--<el-form-item prop="enname" label="角色英文名称：">--%>
        <%--<el-col :span="11">--%>
        <%--<el-input name="enname" v-model="roleForm.enname"></el-input>--%>
        <%--</el-col>--%>
        <%--<el-col :span="13">--%>
        <%--<span class="el-form-item-expository">工作流用户组标识</span>--%>
        <%--</el-col>--%>
        <%--</el-form-item>--%>
        <el-form-item prop="roleGroup" label="角色范围：">
            <%--<el-col :span="11">--%>
            <%--<el-select name="roleGroup" v-model="roleForm.roleGroup">--%>
            <%--<el-option v-for="item in roleGroups" :key="item.value" :label="item.label"--%>
            <%--:value="item.value"></el-option>--%>
            <%--</el-select>--%>
            <%--</el-col>--%>
            <%--<el-col :span="13">--%>
            <%--<span class="el-form-item-expository">前台：可登录前台 后台：可登录后台</span>--%>
            <%--</el-col>--%>
            <%--<el-col :span="11">--%>
            <%--后台--%>
            <%--</el-col>--%>
            <%--<el-col :span="13">--%>
            <%--<span class="el-form-item-expository">后台：可登录后台</span>--%>
            <%--</el-col>--%>
            {{roleForm.roleGroup === '1' ? '前台' : '后台'}}
        </el-form-item>
        <el-form-item prop="sysData" label="是否系统数据：">
            <el-col :span="11">
                <el-popover
                        placement="right"
                        width="200"
                        trigger="hover">
                    <span class="el-form-item-expository">
                        “是”代表此数据只有超级管理员能进行修改，“否”则表示拥有角色修改人员的权限都能进行修改
                    </span>
                    <el-select slot="reference" :disabled="loginCurUser.auth != 1" name="sysData"
                               v-model="roleForm.sysData">
                        <el-option v-for="item in yesNo" :key="item.value" :label="item.label"
                                   :value="item.value"></el-option>
                    </el-select>
                </el-popover>
            </el-col>
        </el-form-item>
        <el-form-item prop="menuIds" label="角色授权：">
            <el-tree
                    :data="menuTree"
                    show-checkbox
                    ref="elMenuTree"
                    node-key="id"
                    :check-strictly="true"
                    :default-expanded-keys="expandedKeys"
                    :default-checked-keys="roleForm.menuIds"
                    @check="menuCheck"
                    :props="defaultProps">
            </el-tree>
        </el-form-item>
        <el-form-item prop="remarks" label="备注：">
            <el-input name="remarks" :rows="3" type="textarea" maxlength="200" v-model="roleForm.remarks"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="saveRole">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var bizTypes = JSON.parse('${fns: toJson(fns:getDictList('0000000153'))}');
            var yesNo = JSON.parse('${fns: toJson(fns:getDictList('yes_no'))}');
            var roleForm = JSON.parse(JSON.stringify(${fns: toJson(role)}));
            var menuList = JSON.parse(JSON.stringify(${fns: toJson(menuList)})) || [];
            var outRoleList = JSON.parse(JSON.stringify(${fns: toJson(outRoleList)}));
            var rtypeList = JSON.parse(JSON.stringify(${fns: toJson(rtypeList)})) || [];
            var self = this;
            var validateName = function (rules, value, callback) {
                if (value) {
                    if (/[@#\$%\^&\*\s]+/g.test(value)) {
                        return callback(new Error("请不要输入特殊符号"));
                    }
                    var params = {
                        id: roleForm.id,
                        name: value
                    };
                    return self.$axios.get('/sys/role/checkRoleName', {
                        params: params
                    }).then(function (response) {
                        if (response.data) {
                            return callback();
                        }
                        return callback(new Error("名称已经存在"))
                    })
                }
                return callback();
            }


            return {
                role: roleForm,
                roleForm: {
                    id: '',
                    name: '',
                    oldName: '',
                    bizType: '',
                    rtype: '',
                    roleGroup: '',
                    sysData: '0',
                    useable: '0',
                    dataScope: '',
                    menuIds: [],
                    officeIds: [],
                    remarks: ''
                },
                outRoleList: outRoleList,
                roleRules: {
                    name: [
                        {required: true, message: '请输入角色名称', trigger: 'blur'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    rtype: [
                        {required: true, message: '请选择角色业务类型', trigger: 'blur'}
                    ]
                },
                roleFormDisabled: true,
                isAdmin: ${admin},
                bizTypes: bizTypes,
                roleGroups: [{label: '前台', value: '1'}, {label: '后台', value: '2'}],
                yesNo: yesNo,
                dataScopes: [],
                menuList: menuList,
                menuTree: [],
                menuEntries: {},
                originMenuList: JSON.parse(JSON.stringify(menuList)),
                defaultProps: {
                    children: 'children',
                    label: 'name'
                },
                menuProps: {
                    id: 'id',
                    parentKey: 'parentId'
                },
                expandedKeys: [],
                rtypeList: rtypeList,
            }
        },
        methods: {
            saveRole: function () {
                var self = this;
                this.$refs.roleForm.validate(function (valid) {
                    if (valid) {
                        self.postRole();
                    }
                })
            },
            postRole: function () {
                var self = this;
                this.roleFormDisabled = true;
                this.$axios.post('/sys/role/saveRole', this.getRoleParams()).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.$msgbox({
                            message: "保存成功",
                            title: '提示',
                            confirmButtonText: '确定',
                            showClose: false,
                            closeOnClickModal: false,
                            type: 'success'
                        }).then(function () {
                            self.goToBack();
                        }).catch(function () {

                        })
                    } else {
                       self.$alert(data.msg, '提示', {
                           type: 'error'
                       })
                    }
                    self.roleFormDisabled = false;
                }).catch(function () {
                    self.roleFormDisabled = false;
                })
            },
            getRoleParams: function () {
                var roleForm = JSON.parse(JSON.stringify(this.roleForm));
                var checkedNodes = this.$refs.elMenuTree.getCheckedNodes();
                var checkedNodeIds = checkedNodes.map(function (item) {
                    return item.id;
                })
                roleForm.menuIds = [].concat(checkedNodeIds, this.outRoleList.map(function (item) {
                    return item.id;
                }));
                roleForm.menuIds = roleForm.menuIds.join(',');
                roleForm.officeIds = roleForm.officeIds.join(',');
                return roleForm;
            },

            menuCheck: function (data, checkedData) {
                var checkedKeys = checkedData.checkedKeys;
                var isChecked = checkedKeys.indexOf(data.id) > -1;
                var menuIds = checkedData.checkedKeys;
                var nData = JSON.parse(JSON.stringify(data));
                var childList = this.getFlattenData([nData]);
                var parentIds = data.parentIds;
                parentIds = parentIds.replace(/\,{1}$/, '');
                parentIds = parentIds.split(',');
                if (isChecked) {
                    childList = childList.map(function (item) {
                        return item.id;
                    });
                    childList = childList.concat(parentIds);
                    childList.forEach(function (item) {
                        if (menuIds.indexOf(item) === -1) {
                            menuIds.push(item)
                        }
                    });
                } else {
                    childList.forEach(function (item) {
                        if (menuIds.indexOf(item.id) > -1) {
                            menuIds.splice(menuIds.indexOf(item.id), 1)
                        }
                    });
                    var parentId = data.parentId;
                    var sameCheck = false;
                    while (parentId) {
                        var sameParents = this.originMenuList.filter(function (item) {
                            return item.parentId === parentId;
                        });
                        sameCheck = sameParents.some(function (item) {
                            return menuIds.indexOf(item.id) > -1;
                        });
                        if (!sameCheck) {
                            menuIds.splice(menuIds.indexOf(parentId), 1);
                            parentId = this.menuEntries[parentId].parentId;
                        }
                        if (sameCheck || parentId === '0') {
                            break;
                        }
                    }
                }
                this.$refs.elMenuTree.setCheckedNodes(this.originMenuList.filter(function (item) {
                    return menuIds.indexOf(item.id) > -1
                }));

            },

            getFlattenData: function (nData) {
                var self = this;
                return nData.reduce(function (p1, p2) {
                    var children = p2.children || [];
                    return p1.concat(p2, self.getFlattenData(children))
                }, [])
            },


            changeOfficeIds: function (data) {
                this.roleForm.officeIds = data;
            },

            goToBack: function () {
                location.href = this.ctx + '/sys/role'
            },
            getUserRoleAuth: function () {
                var self = this;
                this.disabled = true;
                this.$axios.post('/sys/role/getUserRoleAuth', {
                    id: this.loginCurUser.id
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        if (data.data.auth) {
                            Vue.set(self.loginCurUser, 'auth', parseInt(data.data.auth));
                        }
                        self.disabled = false;
                    }
                }).catch(function () {

                })
            },
            getMenuEntity: function () {
                return this.listToTreeEntity(this.menuList);
            }
        },
        created: function () {
            var menuEntity = this.getMenuEntity();
            var menuTree = menuEntity.tree || [];
            var secondChildren = [];
            var menuSecondIds = [];
            var menuRootIds = [];
            menuTree = menuTree.filter(function (item) {
                return item.parentId === '0'
            });
//            if(menuTree.length > 0){
//                secondChildren = menuTree[0].children || [];
//            }
//            secondChildren.forEach(function (item) {
//                menuSecondIds.push(item.id);
//            })
            menuTree.forEach(function (item) {
                menuRootIds.push(item.id);
            })
            this.menuEntries = menuEntity.entity || {};
            this.menuTree = menuTree;
            this.expandedKeys = [].concat(menuRootIds, menuSecondIds);

        },
        mounted: function () {
            this.getUserRoleAuth();
            this.role.menuIds = this.role.menuIds.split(',') || [];
            this.role.officeIds = this.role.officeIds.split(',') || [];
            this.assignFormData(this.roleForm, this.role);
            this.roleFormDisabled = false;
        }
    })

</script>

</body>
</html>