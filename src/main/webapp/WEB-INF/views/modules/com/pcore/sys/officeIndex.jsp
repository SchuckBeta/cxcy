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
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
            <e-condition type="radio" v-model="collegeId" :label="collegeLabel"
                         :options="collegeList"
                         :default-props="{label: 'name', value: 'id'}" @change="handleChangeCollege"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <el-input
                    placeholder="关键字"
                    size="mini"
                    v-model="queryStr"
                    @keyup.enter.native="searchColleges"
                    style="width: 300px;">
                <el-button slot="append" icon="el-icon-search"
                           @click.stop.prevent="searchColleges"></el-button>
            </el-input>
        </div>
    </div>
    <div v-loading="dataLoading" class="table-container">
        <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree"
               cellspacing="0" cellpadding="0">
            <thead>
            <tr>
                <th width="25%"><span class="e-table-tree-dot"></span><i v-show="flattenColleges.length > 0"
                                                                         :class="{'el-icon-remove-outline':isAllCollasped,'el-icon-circle-plus-outline':!isAllCollasped}"
                                                                         style="margin-right: 6px;color:#e9432d;cursor:pointer;"
                                                                         @click.stop="toggleCollasped"></i>名称
                </th>
                <th width="25%" class="text-center" style="text-align: center">机构编码</th>
                <th width="25%" class="text-center" style="text-align: center">机构类型</th>
                <th width="25%" class="text-center" style="text-align: center">操作</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(item, index) in flattenColleges" :key="item.id" v-show="!item.isCollapsed">
                <td><span class="e-table-tree-dot"
                          v-for="(dot, index) in item.dots.split('-').length"></span>
                    <i :class="elIconCaret(item)" class="el-icon-caret-right"
                       @click.stop.prevent="handleExpandCell(item)"></i>
                    <span class="e-checkbox__label_dr_card"><keyword-font :word="queryStrStatic"
                                                                          :text="item.name"></keyword-font></span></td>
                <td class="text-center">
                    {{item.code}}
                </td>
                <td class="text-center">
                    {{item.type | selectedFilter(sysOfficeTypeEntries)}}
                </td>
                <td class="text-center">
                    <el-tooltip class="item" content="修改"
                                popper-class="white" placement="bottom">
                        <el-button :disabled="urvo.office ? urvo.office.indexOf(item.id) > -1 : false" title="编辑" class="btn-icon-font" type="text" icon="el-icon-edit"
                                   @click.stop="goToEditForm(item.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                        <el-button :disabled="item.parentId == 0 || (urvo.office ? urvo.office.indexOf(item.id) > -1 : false)" class="btn-icon-font" type="text" icon="el-icon-delete"
                                   @click.stop="confirmColumn(item.id)"></el-button>
                    </el-tooltip>
                    <el-tooltip class="item" content="添加下级" popper-class="white" placement="bottom">
                        <el-button class="btn-icon-font" icon="el-icon-plus" type="text" :disabled="item.grade >= 3"
                                   @click.stop="goToAddChildren(item)"></el-button>
                    </el-tooltip>
                </td>
            </tr>
            <tr v-if="flattenColleges.length === 0">
                <td colspan="5" class="text-center"><span class="empty">暂无数据</span></td>
            </tr>
            </tbody>
        </table>
    </div>
</div>


<script type="text/javascript">

    'use strict';


    Vue.component('keyword-font', {
        props: {
            word: String,
            text: String
        },
        render: function (createElement) {
            var text = this.text;
            if (this.word) {
                text = this.text.replace(new RegExp(this.word, 'g'), '<span style="font-weight: bold;color: #e9432d">' + this.word + '</span>');
            }
            return createElement('span', {
                domProps: {
                    innerHTML: text
                }
            });
        }
    });

    new Vue({
        el: '#app',
        data: function () {
            return {
                colleges: [],
                isAllCollasped: false,
                flattenColleges: [],
                officeTree: [],
                dataLoading: false,
                sysOfficeTypes: [],
                collegeId: '',
                originFlattenColleges: [],
                queryStr: '',
                type: 'tree',
                queryStrStatic: '',
                collegeResult: []
            }
        },
        computed: {
            sysOfficeTypeEntries: function () {
                return this.getEntries(this.sysOfficeTypes)
            },
            collegeList: function () {
                return this.colleges.filter(function (item) {
                    return item.grade == '2'
                })
            },
            collegeEntries: function () {
                var entries = {};
                for (var i = 0; i < this.colleges.length; i++) {
                    var item = this.colleges[i];
                    entries[item.id] = item;
                }
                return entries;
            },
            collegeLabel: function () {
                if (this.isSchoolPlatform) {
                    return '学院'
                }
                return '机构'
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

            toggleCollasped: function () {
                this.isAllCollasped = !this.isAllCollasped;
                this.flattenColleges = this.treeToList(this.officeTree, this.isAllCollasped ? 0 : 10);

            },

            goToEditForm: function (id) {
                location.href = this.ctx + '/sys/office/form?id=' + id;
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
                    if (data.status === 1) {
                        self.getOfficeList();
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

            getOfficeList: function () {
                var self = this;
                this.dataLoading = true;
                this.$axios.get('/sys/office/getOfficeTree').then(function (response) {
                    var data = response.data || [];
                    self.officeTree = data;
                    self.flattenColleges = self.treeToList(data, 1);
                    self.colleges = self.flattenColleges;
                    self.dataLoading = false;
                })
            },


            goToAddChildren: function (row) {
                location.href = this.ctx + '/sys/office/form?parent.id=' + row.id;
            },

            handleChangeCollege: function (value) {
                var flattenColleges = [];
                var nEntityColleges = [];
                this.type = 'tree';
                this.queryStr = '';
                this.queryStrStatic = '';
                if (value) {
                    this.colleges.forEach(function (item) {
                        if (item.parentId === value) {
                            flattenColleges.push(item);
                            nEntityColleges.push(item);
                            item.isCollapsed = false;
                        }
                        if (item.id === value) {
                            flattenColleges.unshift(item);
                            item.isCollapsed = false;
                        }
                    });
                    if (flattenColleges) {
                        flattenColleges[0].isExpand = true;
                        flattenColleges[0].isCollapsed = false;
                        flattenColleges[0].children = nEntityColleges;
                    }
                    this.flattenColleges = flattenColleges;
                } else {
                    this.getOfficeList();
                }
            },

            searchColleges: function () {
                if (!this.queryStr) {
                    this.getOfficeList();
                    this.queryStrStatic = '';
                    return false;
                }
                this.setCollegeExpand(this.queryStr);
                this.queryStrStatic = this.queryStr

            },

            setCollegeExpand: function (keyword) {
                var collegeEntries = this.collegeEntries;
                var self = this;
                this.flattenColleges.forEach(function (item) {
                    if (item.name.indexOf(keyword) > -1) {
                        var parentCollege = collegeEntries[item.parentId];
                        if (parentCollege && !parentCollege.isExpand) {
                            self.setParentCollegeExpand(parentCollege)
                        }
                    }
                })
            },


            setParentCollegeExpand: function (parentCollege) {
                var parent = parentCollege;
                var collegeEntries = this.collegeEntries;
                while (parent) {
                    if (parent.isExpand) {
                        break;
                    }
                    parent.isExpand = true;
                    this.expandCellTrue(parent.children, parent.isExpand);
                    parent = collegeEntries[parent.parentId];
                }
            },


            //忽略
            getCollegeResult: function () {
                var nColleges = [];
                var queryStr = this.queryStr;
                var self = this;
                this.flattenColleges.forEach(function (item) {
                    if (item.name.indexOf(queryStr) > -1) {
                        if (!item.pathName) {
                            item.pathName = self.getPathName(item);
                        }
                        nColleges.push(item);
                    }
                })
                return nColleges
            },

            //忽略
            getPathName: function (item) {
                var parentId = item.id;
                var names = [];
                while (parentId) {
                    var parentItem = this.collegeEntries[parentId];
                    if (!parentItem) {
                        break;
                    }
                    names.unshift(parentItem.name);
                    parentId = parentItem.parentId;
                }
                return names.join('/')
            },

            getDictList: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=sys_office_type').then(function (response) {
                    var data = response.data;
                    self.sysOfficeTypes = data.data || []
                })
            }


        },

        mounted: function () {
            this.getOfficeList();
            this.getDictList();

        }
    })

</script>
</body>
</html>