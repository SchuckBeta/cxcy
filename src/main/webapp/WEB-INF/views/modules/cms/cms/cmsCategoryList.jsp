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
    <div class="clearfix mgb-20 text-right">
        <el-button icon="el-icon-circle-plus" type="primary" size="mini" @click.stop.prevent="goToMenuForm">添加
        </el-button>
        <el-button icon="el-icon-sort" type="primary" size="mini" :disabled="flattenMenuList.length < 1"
                   @click.stop.prevent="postCmsCategorySorts">保存排序
        </el-button>

    </div>
    <div v-loading="dataLoading" class="table-container">

        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
               cellspacing="0" cellpadding="0">
            <thead>
            <tr>
                <th><span class="e-table-tree-dot"></span><i v-show="flattenMenuList.length > 0"
                                                             :class="{'el-icon-remove-outline':isAllCollasped,'el-icon-circle-plus-outline':!isAllCollasped}"
                                                             style="margin-right: 6px;color:#e9432d;cursor:pointer;"
                                                             @click.stop="toggleCollasped"></i>名称
                </th>
                <th width="100" class="text-center" style="text-align: center">排序</th>
                <th width="150" class="text-center" style="text-align: center">显示</th>
                <th class="text-center" style="text-align: center">展现方式</th>
                <th width="10%" class="text-center" style="text-align: center">栏目模型</th>
                <th class="text-center" style="text-align: center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in flattenMenuList" v-show="!item.isCollapsed">
                <td>
                   <span class="e-table-tree-dot"
                         v-for="(dot, index) in item.dots.split('-').length"></span>
                    <i :class="elIconCaret(item)" class="el-icon-caret-right"
                       @click.stop.prevent="handleExpandCell(item)"></i>
                    <span class="e-checkbox__label_dr_card">{{item.name}}</span>
                </td>
                <td class="text-center">
                    <el-input size="mini" v-model.number="item.sort"></el-input>
                </td>
                <td class="text-center">
                    <el-switch v-model="item.isShow" :active-value="1" :inactive-value="0"
                               @change="handleChangeColumnShow(item)"
                    ></el-switch>
                </td>
                <td class="text-center">
                    {{item.showModes | selectedFilter(cmsShowModeEntries)}}
                </td>
                <td class="text-center">
                    {{item.module | selectedFilter(cmsModuleEntries)}}
                </td>
                <td class="text-center">
                    <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                   :disabled="publishCategories.indexOf(item.publishCategory) > -1"
                                   @click.stop.prevent="goToEditForm(item.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                   :disabled="item.isSys == 1"
                                   @click.stop.prevent="confirmColumn(item.id)">
                        </el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="添加下级" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" type="text" icon="el-icon-plus"
                                   @click.stop.prevent="goToAddChildren(item)"
                                   :disabled="item.dots.split('-').length >= 3"></el-button>
                    </el-tooltip>
                </td>
            </tr>
            </tbody>
        </table>
        <p v-if="dataLoading" class="text-center empty" style="padding: 30px 0">加载中...</p>
    </div>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {

            return {
                menuList: [],
                menuTree: [],
                cmsModuleList: [],
                cmsShowModeList: [],
                flattenMenuList: [],
                dataLoading: false,
                isAllCollasped: false,
                publishCategories: ['0000000285', '0000000175']
            }
        },
        computed: {
            cmsModuleEntries: {
                get: function () {
                    return this.getEntries(this.cmsModuleList)
                }
            },
            cmsShowModeEntries: {
                get: function () {
                    return this.getEntries(this.cmsShowModeList)
                }
            }
        },
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
                for (var i = 0; i < this.flattenMenuList.length; i++) {
                    var item = this.flattenMenuList[i];
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

            goToMenuForm: function () {
                location.href = this.ctx + '/cms/category/form'
            },

            goToEditForm: function (id) {
                location.href = this.ctx + '/cms/category/form?id=' + id;
            },

            goToAddChildren: function (row) {
                location.href = this.ctx + '/cms/category/form?parentId=' + row.id;
            },

            goTohref: function (row) {
                location.href = this.ctx + row.href;
            },

            handleChangeColumnShow: function (row) {
                var self = this;
                this.$axios.get('/cms/category/updateShow', {
                    params: {
                        id: row.id,
                        isShow: row.isShow
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.status !== 1) {
                        row.isShow = row.isShow == 1 ? 0 : 1;
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    } else {
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                    }
                }).catch(function (error) {
                    row.isShow = row.isShow == 1 ? 0 : 1;
                })
            },

            getCmsCategoryList: function () {
                var self = this;
                this.dataLoading = true;
                this.$axios.get('/cms/category/cmsCategoryList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data;
                        if (data) {
                            self.menuList = data.list || [];
                            self.menuTree = self.listToTree(self.menuList.slice(0));
                            self.flattenMenuList = self.treeToList(self.menuTree, self.isAllCollasped ? 1 : 10)
                        }
                    }
                    self.dataLoading = false;
                }).catch(function (error) {
                    self.dataLoading = false;
                })
            },

            toggleCollasped: function () {
                this.isAllCollasped = !this.isAllCollasped;
                this.flattenMenuList = this.treeToList(this.menuTree, this.isAllCollasped ? 1 : 10);

            },

            postCmsCategorySorts: function () {
                var self = this;
                var ids = [], sorts = [];
                var flattenMenuList = this.flattenMenuList;

                for (var i = 0; i < flattenMenuList.length; i++) {
                    var menuItem = flattenMenuList[i];
                    if (!(/^\+?[1-9][0-9]*$/.test(menuItem.sort))) {
                        this.$message({
                            type: 'error',
                            message: '请输入排序数字'
                        });
                        return false;
                    }
                    ids.push(menuItem.id);
                    sorts.push(menuItem.sort);
                }


                this.dataLoading = true;
                this.$axios.get('/cms/category/ajaxUpdateSort?ids=' + ids.join(',') + '&sorts=' + sorts.join(',')).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success'
                        })
                        self.getCmsCategoryList();
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.dataLoading = false;
                }).catch(function (error) {
                    self.dataLoading = false;
                })
            },

            confirmColumn: function (id) {
                var self = this;
                this.$confirm('此操作将会删除这个栏目和它的子栏目，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.deleteCmsCategory(id);
                }).catch(function () {

                })
            },

            deleteCmsCategory: function (id) {
                var self = this;
                this.$axios.get('/cms/category/deleteCmsCategory', {params: {id: id}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getCmsCategoryList();
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


            listToTree: function (menuList) {
                var entity = {};
                var tree = [];
                var rootIds = [];
                for (var i = 0; i < menuList.length; i++) {
                    entity[menuList[i].id] = menuList[i];
                }
                for (var m = 0; m < menuList.length; m++) {
                    var rMenu = menuList[m];
                    var rParentId = rMenu.parentId;
                    var rParent = entity[rParentId];
                    var wParentIds = [rParentId];
                    while (rParent) {
                        var wParentId = rParent.parentId;
                        wParentIds.unshift(wParentId);
                        if (!entity[wParentId]) {
                            rootIds.indexOf(wParentId) === -1 && rootIds.push(wParentId);
                            break;
                        } else {
                            rParent = entity[wParentId];
                        }
                    }
                    wParentIds.unshift("0");
                    rMenu.parentIds = wParentIds.join(',') + ',';
                }
                var rootMenus = menuList.filter(function (item) {
                    return rootIds.indexOf(item.parentId) > -1;
                })

                rootIds = rootMenus.map(function (item) {
                    return item.id;
                })

                for (var j = 0; j < menuList.length; j++) {
                    var menu = menuList[j];
                    var id = menu.id;
                    var parentId = menu.parentId;
                    if (rootIds.indexOf(id) === -1) {
                        if (!entity[parentId].children) {
                            entity[parentId].children = [];
                        }
                        entity[parentId].children.push(menu);
                    } else {
                        tree.push(entity[id]);
                    }
                }
                return tree;
            },
            getDictLists: function () {
                var dicts = {
                    '0000000274': 'cmsModuleList',
                    'cms_show_modes': 'cmsShowModeList',
                };
                this.getBatchDictList(dicts)

            },
        },
        mounted: function () {
            this.getCmsCategoryList();
            this.getDictLists();
        }
    })

</script>

</body>
</html>