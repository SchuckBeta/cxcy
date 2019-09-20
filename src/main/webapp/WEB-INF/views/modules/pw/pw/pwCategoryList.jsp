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

    <div class="clearfix mgt-20">
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <shiro:hasPermission name="pw:pwCategory:edit">
                    <el-button type="primary" size="mini" @click.stop.prevent="goAddPCategory">添加父类别</el-button>
                </shiro:hasPermission>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="资产类别"
                        size="mini"
                        v-model="queryStr"
                        @keyup.enter.native="searchColleges"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchColleges"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container">
        <el-table v-loading="dataLoading" :data="flattenColleges" size="mini" :cell-style="eTableCellStyle"
                  class="e-table-tree">
            <el-table-column label="资产类别">
                <template slot-scope="scope">
                    <span class="e-table-tree-dot" v-if="index>0" v-for="(dot, index) in scope.row.dots.split('-').length"></span>
                    <i :class="elIconCaret(scope.row)" class="el-icon-caret-right" @click.stop.prevent="handleExpandCell(scope.row)"></i>
                    <span :class="{'e-checkbox__label_dr_card':true,'underline-pointer':scope.row.id !== '1'}"
                          @click.stop.prevent="goInfo(scope.row.id)"><keyword-font :word="queryStrStatic" :text="scope.row.name"></keyword-font></span>
                </template>
            </el-table-column>

            <el-table-column label="编号规则示例" align="center">
                <template slot-scope="scope">
                    {{getRule(scope.row)}}
                </template>
            </el-table-column>

            <shiro:hasPermission name="pw:pwCategory:edit">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <a v-if="scope.row.parentIds.split(',').length < 4" :href="'${ctx}/pw/pwCategory/form?parent.id='+ scope.row.id">添加子类别</a>
                            <a v-if="scope.row.id != '1'" :href="'${ctx}/pw/pwCategory/form?id='+ scope.row.id">修改</a>
                            <a v-if="scope.row.id != '1'" href="javascript:void(0)" @click.stop.prevent="singleDelete(scope.row.id)">删除</a>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>
        </el-table>
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
            if(this.word){
                text  = this.text.replace(new RegExp(this.word, 'g'), '<span style="font-weight: bold;color: #e9432d">'+this.word+'</span>');
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
        mixins: [Vue.collegesMixin],
        data: function () {
            return {
                colleges: [],
                flattenColleges: [],
                dataLoading: false,
                originFlattenColleges: [],
                formMessage: '${message}',
                queryStr: '',
                queryStrStatic: '',
                collegeResult: []
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


            getOfficeList: function () {
                var self = this;
                this.dataLoading = true;
                this.$axios.get('/pw/pwCategory/listpage').then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        self.colleges = data.data || [];
                        self.setFlattenColleges([].concat(self.colleges));
                    }
                    self.dataLoading = false;
                });
            },
            setFlattenColleges: function (colleges) {
                this.collegeEntries = {};
                this.setCollegeEntries(colleges);
                this.setCollegeRooIds(colleges);
                this.collegesTree = this.getCollegesTree(this.collegeRooIds, this.collegesProps);
                this.flattenColleges = this.getFlattenColleges(1);
                this.originFlattenColleges = JSON.parse(JSON.stringify(this.flattenColleges));
            },

            searchColleges: function () {
                var reg = new RegExp(this.queryStr);
                this.flattenColleges = this.getFlattenColleges(1);
                if(this.queryStr){
                    this.flattenColleges = this.flattenColleges.filter(function (item) {
                        return reg.test(item.name);
                    });
                }
//                if(!this.queryStr){
//                    this.flattenColleges = this.getFlattenColleges(1);
//                    this.queryStrStatic = '';
//                    return false;
//                }
//                this.setCollegeExpand(this.queryStr);
//                this.queryStrStatic = this.queryStr
            },

            setCollegeExpand: function (keyword) {
                var collegeEntries = this.collegeEntries;
                var self = this;
                this.flattenColleges.forEach(function (item) {
                    if (item.name.indexOf(keyword) > -1) {
                        var parentCollege = collegeEntries[item.parentId];
                        if(!parentCollege.isExpand){
                            self.setParentCollegeExpand(parentCollege)
                        }
                    }
                })
            },


            setParentCollegeExpand: function (parentCollege) {
                var parent = parentCollege;
                var collegeEntries = this.collegeEntries;
                while (parent){
                    if(parent.isExpand){
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
                });
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


            goAddPCategory:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwCategory/form?parent.id=1';
            },
            goInfo:function (id) {
                if(id == '1'){
                    return false;
                }
                window.location.href = this.frontOrAdmin + '/pw/pwCategory/details?id=' + id;
            },
            getRule:function (row) {
                if (row.parentIds.split(",").length >= 4) {
                    var parentPrefix = row.parentPrefix;
                    var prefix = row.pwFassetsnoRule.prefix;
                    var startNumber = row.pwFassetsnoRule.startNumber;
                    var numberLen = row.pwFassetsnoRule.numberLen;
                    if (parentPrefix && startNumber && numberLen) {
                        return parentPrefix + prefix + this.prefixZero(parseInt(startNumber), parseInt(numberLen));
                    }
                }
                return '';
            },
            prefixZero:function (num, length) {
                return (Array(length).join('0') + num).slice(-length);
            },
            singleDelete:function (id) {
                var self = this;
                this.$confirm('确认要删除该资产类别及所有子资产类别吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method:'GET',
                        url:'/pw/pwCategory/delete',
                        params:{
                            id:id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getOfficeList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type:'error'
                        })
                    })
                })
            }


        },

        created: function () {
            this.getOfficeList();
            if (this.formMessage) {
                this.$message({
                    type: this.formMessage.indexOf('成功') > -1 ? 'success' : 'error',
                    message: this.formMessage
                });
                this.formMessage = '';
            }

        }
    })

</script>
</body>
</html>