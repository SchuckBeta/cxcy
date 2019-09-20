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
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <div class="mgb-20 text-right">
        <el-button type="primary" size="mini" icon="el-icon-circle-plus" @click.stop="goAddMenu">添加菜单
        </el-button>
        <shiro:hasPermission name="sys:menu:edit">
            <el-button type="primary" size="mini" icon="el-icon-sort" :disabled="menuList.length < 1 || loading"
                       @click.stop="saveSorts">保存排序
            </el-button>
        </shiro:hasPermission>
    </div>
    <div v-loading="loading" class="table-container">
        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
               style="table-layout: fixed"
               cellspacing="0" cellpadding="0">
            <thead>
            <tr>
                <th width="380"><span class="e-table-tree-dot"></span>名称</th>
                <th width="100">排序</th>
                <th width="100" class="text-center">显示</th>
                <th>链接</th>
                <th width="180">权限标识</th>
                <shiro:hasPermission name="sys:menu:edit">
                    <th width="160">操作</th>
                </shiro:hasPermission>
            </tr>
            </thead>
            <tbody>
            <tr v-for="menu in menuList" :key="menu.id" v-show="!menu.isCollapsed">
                <td>
                <span class="e-table-tree-dot"
                      v-for="(dot, index) in menu.dots.split('-').length - 1"></span>
                    <i :class="elIconCaret(menu)" class="el-icon-caret-right"
                       @click.stop.prevent="handleExpandCell(menu)"></i>
                    <span class="e-checkbox__label_dr_card">{{menu.name}}</span>
                </td>
                <td>
                    <shiro:hasPermission name="sys:menu:edit">
                        <el-input size="mini" v-model.number="menu.sort" maxlength="9"></el-input>
                    </shiro:hasPermission>
                    <shiro:lacksPermission name="sys:menu:edit">
                        {{menu.sort}}
                    </shiro:lacksPermission>
                </td>
                <td class="text-center">
                    <el-switch v-model="menu.isShow" :active-value="'1'" :inactive-value="'0'"
                               @change="handleChangeIsShow(menu)"></el-switch>
                </td>
                <td>
                    <el-tooltip :content="menu.href" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{menu.href}}</span>
                    </el-tooltip>
                </td>
                <td>
                    <el-tooltip :content="menu.permission" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{menu.permission}}</span>
                    </el-tooltip>
                </td>
                <shiro:hasPermission name="sys:menu:edit">
                    <td>

                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       @click.stop="goChangeData(menu.id)">
                            </el-button>
                        </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop="singleDelete(menu.id)">
                            </el-button>
                        </el-tooltip>

                        <el-tooltip class="item" content="添加下级菜单" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" icon="el-icon-plus" type="text"
                                       @click.stop.prevent="goAddChild(menu.id)">
                            </el-button>
                        </el-tooltip>
                    </td>
                </shiro:hasPermission>
            </tr>
            </tbody>
        </table>
        <p v-if="menuList.length === 0" class="empty-color empty" style="padding: 20px 0">数据加载中</p>
    </div>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                menuList: [],
                loading: false,
                menuProps: {
                    parentKey: 'parentId',
                    id: 'id'
                },
            }
        },
        computed: {},
        methods: {
            elIconCaret: function (row) {
                return {
                    'is-leaf': !row.children || !row.children.length,
                    'expand-icon': row.isExpand
                }
            },
            //控制行的样式
            eTableCellStyle: function (row) {
                return {
                    'display': row.row.isCollapsed ? 'none' : ''

                }
            },

            //控制行的展开收起
            handleExpandCell: function (row) {
                var children = row.children;
                if (!children) {
                    return;
                }
                row.isExpand = !row.isExpand;
                if (row.isExpand) {
                    this.expandCellTrue(children, row.isExpand);
                    return
                }
                this.expandCellFalse(children, row.isExpand);
            },

            expandCellTrue: function (list, b) {
                for (var i = 0; i < list.length; i++) {
                    var item = list[i];
                    item.isCollapsed = !b;
                    item.isExpand = false;
                }
            },

            expandCellFalse: function (list, b) {
                var childrenIds = this.getPwSpaceChildrenIds(list);
                for (var i = 0; i < this.menuList.length; i++) {
                    var item = this.menuList[i];
                    if (childrenIds.indexOf(item.id) > -1) {
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            //获取所有子的ID
            getPwSpaceChildrenIds: function (list) {
                var ids = [];

                function getIds(list) {
                    if (!list) return ids;
                    for (var i = 0; i < list.length; i++) {
                        ids.push(list[i].id);
                        getIds(list[i].children);
                    }
                }

                getIds(list);
                return ids;
            },

            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/sys/menu/getMenuTree').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageList = data.data || [];
                        self.menuList = self.treeToList(pageList, 1);
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },
            getFlattenList: function (pageList) {
                var entity = this.listToTreeEntity(pageList);
                var tree = entity.tree;
                var parentIds = pageList[0].parentIds;
                tree = tree.filter(function (menu) {
                    return menu.parentId === parentIds[0]
                });
                return this.treeToList(tree, 1);
            },

            goAddMenu: function () {
                window.location.href = this.ctx + '/sys/menu/form';
            },

            goChangeData: function (id) {
                window.location.href = this.ctx + '/sys/menu/form?id=' + id;
            },

            goAddChild: function (id) {
                window.location.href = this.ctx + '/sys/menu/form?parent.id=' + id;
            },

            singleDelete: function (id) {
                var self = this;
                this.$confirm('要删除该菜单及所有子菜单项吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.singleDeleteRequest(id);
                })
            },

            singleDeleteRequest: function (id) {
                var self = this;
                this.$axios.post('/sys/menu/ajaxDeleteMenu', {id: id}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getDataList();
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

            saveSorts: function () {
                var self = this;
                var list = [];
                var menuList = this.menuList;
                for (var i = 0; i < menuList.length; i++) {
                    var menuItem = menuList[i];
                    if (!(/^\+?[0-9][0-9]*$/.test(menuItem.sort))) {
                        this.$message({
                            type: 'error',
                            message: '请输入排序数字'
                        });
                        return false;
                    }
                    list.push({id: menuItem.id, sort: parseInt(menuItem.sort)});
                }
                this.loading = true;
                this.$axios.post('/sys/menu/ajaxUpdateSort', {menuList: list}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getDataList();
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });

            },

            handleChangeIsShow: function (row) {
                var self = this;
                this.$axios.post('/sys/menu/ajaxChangeMenuIsShow', {
                    id: row.id,
                    isShow: row.isShow
                }).then(function (response) {
                    var data = response.data;
                    if (data.status !== 1) {
                        row.isShow = row.isShow == '1' ? '0' : '1';
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                }).catch(function (error) {
                    row.isShow = row.isShow == '1' ? '0' : '1';
                })
            },


        },
        mounted: function () {
            this.getDataList();
        }
    })

</script>

</body>
</html>