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
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="mgb-20 text-right">
        <el-button size="mini" type="primary" icon="el-icon-circle-plus" @click.stop="goRoleAdd">角色添加</el-button>
    </div>
    <div class="table-container">
        <el-table size="mini" :data="cpPageList" class="table" v-loading="loading"
                  :cell-style="filterRow">
            <el-table-column prop="name" label="角色名称">
            </el-table-column>
            <el-table-column v-show="loginCurUser.auth" label="操作" align="center" width="200">
                <template slot-scope="scope">
                    <el-tooltip class="item" content="修改"
                                popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   :disabled="disabled || (urvo.role ? urvo.role.indexOf(scope.row.id) > -1 : false)"
                                   @click.stop="goChange(scope.row.id)">
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除"
                                popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   :disabled="disabled || scope.row.rinit == '1' || (urvo.role ? urvo.role.indexOf(scope.row.id) > -1 : false)"
                                   @click.stop="singleDelete(scope.row.id)"></el-button>
                    </el-tooltip>
                </template>
            </el-table-column>
        </el-table>
    </div>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                pageList: [],
                bizTypes: [],
                dataScopes: [],
                loading: false,
                admin: false,
                disabled: false,
                roleAuth: []
            }
        },
        computed: {
            bizTypesEntries: {
                get: function () {
                    return this.getEntries(this.bizTypes);
                }
            },
            dataScopesEntries: {
                get: function () {
                    return this.getEntries(this.dataScopes);
                }
            },

            cpPageList: function () {
                var pageList = this.pageList.sort(function (item1, item2) {
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
                return pageList.reverse();
            }
        },
        methods: {


            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/sys/role/getRoleList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pageList = data.data || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
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
                            Vue.set(self.loginCurUser, 'auth', parseInt(data.data.auth))
                        }
                        self.disabled = false;
                    }
                }).catch(function () {

                })
            },

            getRoleAuth: function () {
                var self = this;
                this.$axios.get('/sys/role/getRoleAuth').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.roleAuth = data.data || []
                    }
                })
            },

            checkUserIsAdmin: function () {
                var self = this;
                this.$axios({method: 'GET', url: '/sys/user/checkUserIsAdmin'}).then(function (response) {
                    self.admin = response.data;
                })
            },

            filterRow: function (row) {
                if (!(this.admin || (row.row.id != '1' && row.row.id != '10'))) {
                    return {
                        'display': 'none'
                    };
                }
            },

            goRoleAdd: function () {
                window.location.href = this.ctx + '/sys/role/form';
            },


            goChange: function (id) {
                window.location.href = this.ctx + '/sys/role/form?id=' + id;
            },

            singleDelete: function (id) {
                var self = this;
                this.$confirm('确认要删除该角色吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.deleteRole(id);
                })
            },

            deleteRole: function (id) {
                var self = this;
                this.$axios.post('/sys/role/ajaxDelete', {id: id}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getDataList();
                        self.$alert('删除成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert('删除成功', '提示', {
                            type: 'error'
                        })
                    }
                })
            },
            getDictLists: function () {
                var dicts = {
                    '0000000153': 'bizTypes',
                    'sys_data_scope': 'dataScopes',
                };
                this.getBatchDictList(dicts)
            },
        },
        created: function () {
            this.checkUserIsAdmin();
            this.getDataList();
            this.getUserRoleAuth();
        }
    })

</script>
</body>
</html>