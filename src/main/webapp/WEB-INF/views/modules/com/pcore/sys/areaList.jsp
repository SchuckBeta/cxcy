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

    <shiro:hasPermission name="sys:area:edit">
        <div class="text-right mgb-20">
            <el-button type="primary" icon="el-icon-circle-plus" size="mini" @click.stop.prevent="goAddArea">添加区域
            </el-button>
        </div>
    </shiro:hasPermission>

    <div class="table-container">
        <table class="el-table table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
               v-loading="loading">
            <thead>
            <tr>
                <th style="width:20%;">区域名称</th>
                <th style="width:15%;">区域编码</th>
                <th style="width:15%;">区域类型</th>
                <th>备注</th>
                <shiro:hasPermission name="sys:area:edit">
                    <th style="width:20%;">操作</th>
                </shiro:hasPermission>
            </tr>
            </thead>
            <tbody>
            <tr v-for="item in flattenMenuList" :key="item.id" v-if="!item.isCollapsed">
                <td>
                    <span class="e-table-tree-dot" v-if="index>0" v-for="(dot, index) in parseInt(item.level)"></span>
                    <i class="el-icon-caret-right" :class="elIconCaret(item)"
                       @click.stop.prevent="handleExpandCell(item)"></i>
                    <span class="e-checkbox__label_dr_card">{{item.name}}</span>
                </td>
                <td>{{item.code}}</td>
                <td>{{item.type | selectedFilter(areaTypesEntries)}}</td>
                <td>
                    <el-tooltip :content="item.remarks" popper-class="white" placement="right">
                        <span class="break-ellipsis" style="max-width:250px;">{{item.remarks}}</span>
                    </el-tooltip>

                </td>

                <shiro:hasPermission name="sys:area:edit">
                    <td>
                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       @click.stop.prevent="goChangeData(item.id)">
                            </el-button>
                        </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop.prevent="singleDelete(item.id)">
                            </el-button>
                        </el-tooltip>
                        <el-tooltip class="item" content="添加下级区域" popper-class="white" placement="bottom">
                            <el-button class="btn-icon-font" icon="el-icon-plus" type="text"
                                       @click.stop.prevent="goAddChild(item)"
                                       :disabled="item.type == '4'">
                            </el-button>
                        </el-tooltip>
                    </td>
                </shiro:hasPermission>
            </tr>
            </tbody>
        </table>


    </div>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var areaTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('sys_area_type'))})) || [];
            return {
                areaTypes: areaTypes,
                pageList: [],
                flattenMenuList: [],
                loading: false,
                message: '${message}',
                isDisabled: true
            }
        },
        computed: {
            areaTypesEntries: function () {
                return this.getEntries(this.areaTypes);
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

            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/sys/area/listpage'
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageList = data.data || [];
                        self.flattenMenuList = self.handleFlatten(self.pageList, 0);

                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
            },
            handleFlatten: function (data, expandLevel) {
                function flatten(data) {
                    return data.reduce(function (p1, p2) {
                        var children = p2.children || [];
                        Vue.set(p2, 'isCollapsed', parseInt(p2.level) > expandLevel + 1);
                        Vue.set(p2, 'isExpand', parseInt(p2.level) <= expandLevel);
                        return p1.concat(p2, flatten(children))
                    }, [])
                }

                return flatten(data);
            },

            goAddArea: function () {
                window.location.href = this.frontOrAdmin + '/sys/area/form';
            },

            goChangeData: function (id) {
                window.location.href = this.frontOrAdmin + '/sys/area/form?id=' + id;
            },

            goAddChild: function (row) {
                window.location.href = this.frontOrAdmin + '/sys/area/form?parent.id=' + row.id + '&parentIds=' + row.parentIds;
            },

            singleDelete: function (id) {
                var self = this;
                this.$confirm('要删除该区域及所有子区域项吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.singleDeleteRequest(id);
                }).catch(function () {

                })
            },

            singleDeleteRequest: function (id) {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/sys/area/delete',
                    params: {id: id}
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                    }
                    self.loading = false;
                    self.$message({
                        message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                }).catch(function (error) {
                    self.loading = false;
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    });
                })
            }
        },
        created: function () {
            this.getDataList();
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('完成') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })

</script>

</body>
</html>