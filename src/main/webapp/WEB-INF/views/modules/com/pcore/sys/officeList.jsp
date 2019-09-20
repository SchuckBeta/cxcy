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
    <%--<div class="mgb-20">--%>
    <%--<edit-bar></edit-bar>--%>
    <%--</div>--%>
    <div class="table-container">
        <el-table v-loading="dataLoading" :data="flattenColleges" size="mini" :cell-style="eTableCellStyle"
                  class="e-table-tree">
            <el-table-column label="名称">
                <template slot-scope="scope">
                            <span class="e-table-tree-dot" v-if="index>0"
                                  v-for="(dot, index) in scope.row.dots.split('-').length"></span>
                    <i :class="elIconCaret(scope.row)" class="el-icon-caret-right"
                       @click.stop.prevent="handleExpandCell(scope.row)"></i>
                    <span class="e-checkbox__label_dr_card">{{scope.row.name}}</span>
                </template>
            </el-table-column>

            <el-table-column label="机构编码" align="center">
                <template slot-scope="scope">
                    {{scope.row.code}}
                </template>
            </el-table-column>
            <el-table-column label="机构类型" align="center">
                <template slot-scope="scope">
                    {{scope.row.type | selectedFilter(sysOfficeTypeEntries)}}
                </template>
            </el-table-column>


            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button size="mini" type="text"
                                   @click.stop.prevent="goToEditForm(scope.row.id)">修改
                        </el-button>
                        <el-button :disabled="scope.row.parentId == 0" size="mini" type="text"
                                   @click.stop.prevent="confirmColumn(scope.row.id)">删除
                        </el-button>
                        <el-button size="mini" type="text" @click.stop.prevent="goToAddChildren(scope.row)"
                                   :disabled="scope.row.grade >= 3">
                            添加下级栏目
                        </el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
    </div>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            <%--var colleges = JSON.parse('${fns: toJson(fns: getOfficeList())}') || [];--%>
            var sysOfficeTypes = JSON.parse('${fns: toJson(fns: getDictList("sys_office_type"))}')
            return {
                colleges: [],
                flattenColleges: [],
                dataLoading: false,
                sysOfficeTypes: sysOfficeTypes
            }
        },
        computed: {
            sysOfficeTypeEntries: function () {
                return this.getEntries(this.sysOfficeTypes)
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
                var childrenIds = this.getCollegeChildrenIds(list);
                for (var i = 0; i < this.flattenColleges.length; i++) {
                    var item = this.flattenColleges[i];
                    if (childrenIds.indexOf(item.id) > -1) {
                        item.isCollapsed = !b;
                        item.isExpand = false;
                    }
                }
            },

            //获取所有子的ID
            getCollegeChildrenIds: function (list) {
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
            goToEditForm: function (id) {
                location.href = this.frontOrAdmin + '/sys/office/form?id=' + id;
            },
            confirmColumn: function (id) {
                var self = this;
                this.$confirm("要删除该机构及所有子机构项吗？", "提示", {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delOffice(id);
                }).catch(function () {

                })
            },

            delOffice: function (id) {
                var self = this;
                this.$axios.post('/sys/office/delOffice', {id: id}).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getOfficeList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '删除成功' : data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            getOfficeList: function () {
                var self = this;
                this.dataLoading = true;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    if (checkResponseCode) {
                        data = data.data;
                        if (data) {
                            self.colleges = data || [];
                            self.setFlattenColleges([].concat(self.colleges))
                        }
                    }
                    self.dataLoading = false;
                })
            },
            setFlattenColleges: function (colleges) {
                this.setCollegeEntries(colleges);
                this.collegesTree = this.getCollegesTree(['1'], this.collegesProps);
                this.flattenColleges = this.getFlattenColleges()
            },

            goToAddChildren: function (row) {
                location.href = this.frontOrAdmin + '/sys/office/form?parent.id=' + row.id;
            }
        },

        created: function () {
            this.getOfficeList();
        }
    })

</script>
</body>
</html>