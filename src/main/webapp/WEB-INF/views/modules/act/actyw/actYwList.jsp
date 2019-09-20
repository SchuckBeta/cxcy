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
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm.isPreRelease" label="预发布"
                         :options="yesNoes" @change="getActYwList"></e-condition>
            <e-condition type="radio" v-model="searchListForm.isDeploy" label="发布状态"
                         :options="yesNoes" @change="getActYwList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button icon="el-icon-circle-plus" type="primary" size="mini" @click.stop.prevent="goToAdd">添加
                </el-button>
            </div>
            <div class="search-input">
                <el-input
                        size="mini"
                        name="inputValue"
                        v-model="inputValue"
                        @keyup.enter.native="getActYwList"
                        style="width: 400px;"
                >
                    <el-select v-model="selectName" slot="prepend" placeholder="请选择" style="width: 100px">
                        <el-option label="项目名称" value="proProject.projectName"></el-option>
                        <el-option label="流程名称" value="group.name"></el-option>
                    </el-select>
                    <el-button slot="append" icon="el-icon-search" @click.stop.prevent="getActYwList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container">
        <el-table :data="actYwList" size="small" class="table" border :span-method="objectSpanMethod">
            <el-table-column label="项目名称/类型">
                <template slot-scope="scope">
                    {{scope.row.group.flowType  ? scope.row.group.name : ''}}/
                    {{scope.row.proProject  ? scope.row.proProject.projectName : ''}}
                </template>
            </el-table-column>
            <el-table-column label="工作流程" align="center">
                <template slot-scope="scope">
                    <a :href="ctx + '/actyw/actYwGnode/' + scope.row.group.id + '/view'" target="_blank">
                        {{scope.row.group.name}}
                    </a>-{{scope.row.isPush}}-
                </template>
            </el-table-column>
            <el-table-column label="编号规则" align="center">
                <template slot-scope="scope">
                	<template v-if="scope.row.isNrule == 1">
	                    <a :href="ctx + '/sys/sysNumberRule?appType='+scope.row.id">
	                        {{scope.row.numberRuleName}}
	                    </a>
	                </template>
	                <template v-if="!(scope.row.isNrule == 1)">默认</template>
                </template>
            </el-table-column>
            <el-table-column label="编号规则示例" align="center">
                <template slot-scope="scope">
                    {{scope.row.numberRuleText}}
                </template>
            </el-table-column>

            <el-table-column label="发布类型" align="center">
                <template slot-scope="scope">
                        {{scope.row.isPreRelease ? '预发布' : '正式发布'}}
                </template>
            </el-table-column>
            <%--<el-table-column label="发布/生效" align="center">--%>
                <%--<template slot-scope="scope">--%>
                    <%--{{scope.row.isDeploy | selectedFilter(publishEntries)}}/--%>
                    <%--{{(scope.row.isCurr == 1)  ? '是' : '否'}}--%>
                <%--</template>--%>
            <%--</el-table-column>--%>
            <el-table-column label="年份" align="center">
                <template slot-scope="scope">
                    {{scope.row.year}}
                </template>
            </el-table-column>
            <el-table-column label="项目有效期" align="center" width="180">
                <template slot-scope="scope">
                    <template v-if="!!scope.row.startDate">
                        {{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}至{{scope.row.endDate |
                        formatDateFilter('YYYY-MM-DD')}}
                    </template>
                </template>
            </el-table-column>
            <el-table-column label="修改时间" align="center">
                <template slot-scope="scope">
                    <a :href="ctx + '/actyw/actYw/formGtime?id='+scope.row.id+'&yearId='+scope.row.yearId">修改时间</a>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                    	<c:if test="${aconfig.hasUpProp }">
                        	<el-button type="text" size="mini" v-if="ywIds.indexOf(scope.row.id) == -1" @click.stop.prevent="handleEditProp(scope.row)">修改属性</el-button>
						</c:if>
                    	<c:if test="${!aconfig.hasUpProp }">
                        	<el-button type="text" size="mini" v-if="ywIds.indexOf(scope.row.id) == -1" @click.stop.prevent="handleEditProp(scope.row)">查看属性</el-button>
						</c:if>
                        <template v-if="scope.row.isDeploy">
                            <el-button v-if="scope.row.isPreRelease" type="text" size="mini"
                                       @click.stop.prevent="confirmReleaseActYw(scope.row)">正式发布
                            </el-button>
                            <el-button type="text" size="mini" v-if="ywIds.indexOf(scope.row.id) == -1" @click.stop.prevent="confirmUnReleaseActYw(scope.row)">取消发布</el-button>
                        </template>
                        <template v-else>
                            <el-button type="text" size="mini" @click.stop.prevent="confirmAjaxDeployActYw(scope.row)">预发布</el-button>
                            <el-button type="text" size="mini" @click.stop.prevent="confirmDelActYw(scope.row)">删除</el-button>
                        </template>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    isPreRelease: '',
                    isDeploy: '',
                    'group.flowType': '${aconfig.flowType}'
                },
                pageCount: 0,
                yesNoes: [],
                publishes: [],
                selectName: 'proProject.projectName',
                inputValue: '',
                actYwList: [],
                tableLoading: true,
                actYw: {
                    group: {}
                },
                rowspanArr: [0, 1, 2, 3, 4, 5, 9],
                ywgId: '',
                ywpId: ''
            }
        },
        computed: {
            publishEntries: function () {
                return this.getEntries(this.publishes)
            },
            ywIds: function () {
                return [this.ywgId, this.ywpId]
            }
        },
        watch: {
            selectName: function (value) {
                this.searchListForm[value] = this.inputValue;
            }
        },
        methods: {

            goToAdd: function () {
                location.href = this.ctx + '/actyw/actYw/form?group.flowType=' + this.actYw.group.flowType
            },

            getActYwList: function () {
                var self = this;
                this.searchListForm['group.name'] = '';
                this.searchListForm['proProject.projectName'] = '';
                this.searchListForm[this.selectName] = this.inputValue;
                this.tableLoading = true;
                this.$axios.get('/actyw/actYw/getActYwList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    var pageData = {};
                    if (data.status == '1') {
                        pageData = data.data;
                    }
                    self.actYwList = self.changeActYwList(pageData.list || []);
                    self.pageCount = pageData.count || 0;
                    self.searchListForm.pageSize = pageData.pageSize || 1;
                    self.searchListForm.pageNo = pageData.pageNo || 10;
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.tableLoading = false;
                })
            },

            changeActYwList: function (list) {
                var nList = [];
                var self = this;
                list = list || [];
                list.forEach(function (item, index) {
                    if(item.years && item.years.length > 0){
                        if (item.years.length > 1) {
                            var yearLen = item.years.length;
                            var nItem = list.slice(index, index + 1)[0];
                            nItem = JSON.parse(JSON.stringify(nItem));
                            nItem.rowspan = yearLen;
                            nList.push(self.extendYears(nItem, item.years[0]));
                            item.rowspan = 0;
                            for (var i = 1; i < yearLen; i++) {
                                nList.push(self.extendYears(item, item.years[i]))
                            }
                        } else {
                            nList.push(self.extendYears(item, item.years[0]));
                        }
                    }
                })
                return nList;
            },

            extendYears: function (item, year) {
                var nItem = JSON.parse(JSON.stringify(item))
                nItem.endDate = year.endDate;
                nItem.startDate = year.startDate;
                nItem.year = year.year;
                nItem.yearId = year.id;
                return nItem;

            },

            getActYw: function () {
                var self = this;
                this.$axios.get('/actyw/actYw/getActYw?group.flowType=' + this.searchListForm['group.flowType']).then(function (response) {
                    var data = response.data;
                    self.actYw = data.data.actYw;
                    self.ywgId = data.data.ywgId;
                    self.ywpId = data.data.ywpId;
                }).catch(function (error) {

                })
            },

            objectSpanMethod: function (spanObj) {
                var row = spanObj.row;
                var column = spanObj.column;
                var rowIndex = spanObj.rowIndex;
                var columnIndex = spanObj.columnIndex;
                if (row.rowspan != undefined) {
                    if (this.rowspanArr.indexOf(columnIndex) > -1) {
                        if (row.rowspan > 0) {
                            return {
                                rowspan: row.rowspan,
                                colspan: 1
                            }
                        } else {
                            return {
                                rowspan: 0,
                                colspan: 0
                            }
                        }

                    }
                }
            },


            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getActYwList();
            },

            handlePCPChange: function () {
                this.getActYwList();
            },

            handleEditProp: function (row) {
                location.href = this.ctx + '/actyw/actYw/formProp?id=' + row.id
            },

            confirmReleaseActYw: function (row) {
                var self = this;
                this.$confirm('确认要正式发布' + row.proProject.projectName + '吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.releaseActYw(row)
                }).catch(function () {

                })
            },

            releaseActYw: function (row) {
                var self = this;
                var loading  = this.$loading({
                    lock: true,
                    text: '发布中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)',
                    customClass: 'gray-green-loading'
                });
                this.$axios.post('/actyw/actYw/ajaxPreReleaseJson', {
                    id: row.id,
                    isPreRelease: false
                }).then(function (response) {
                    var data = response.data;
                    loading.close()
                    if (data.status == 1) {
                        self.getActYwList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '正式发布成功' : data.msg
                    })
                }).catch(function () {
                    loading.close()
                })
            },

            confirmUnReleaseActYw: function (row) {
                var self = this;
                this.$confirm('确认要取消发布' + row.proProject.projectName + '吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.unReleaseActYw(row)
                }).catch(function () {

                })
            },

            unReleaseActYw: function (row) {
                var self = this;
                var loading  = this.$loading({
                    lock: true,
                    text: '取消发布中...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)',
                    customClass: 'gray-green-loading'
                });
                this.$axios.post('/actyw/actYw/ajaxDeployJson', {
                    id: row.id,
                    isDeploy: false
                }).then(function (response) {
                    var data = response.data;
                    loading.close()
                    if (data.status == 1) {
                        self.getActYwList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '取消发布成功' : data.msg
                    })
                }).catch(function () {
                    loading.close()
                })
            },

            confirmAjaxDeployActYw: function (row) {
                var self = this;
                this.$confirm('确认要预发布' + row.proProject.projectName + '吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.ajaxDeployActYw(row)
                }).catch(function () {

                })
            },

            ajaxDeployActYw: function (row) {
                var self = this;
                var loading  = this.$loading({
                    lock: true,
                    text: '预发布...',
                    spinner: 'el-icon-loading',
                    background: 'rgba(0, 0, 0, 0)',
                    customClass: 'gray-green-loading'
                });
                this.$axios.post('/actyw/actYw/ajaxDeployJson', {
                    id: row.id,
                    isDeploy: true,
                    isPreRelease: true,
                    isUpdateYw: true
                }).then(function (response) {
                    var data = response.data;
                    loading.close()
                    if (data.status == 1) {
                        self.getActYwList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '预发布成功' : data.msg
                    })
                }).catch(function () {
                    loading.close()
                })
            },

            confirmDelActYw: function (row) {
                var self = this;
                this.$confirm('确认要删除' + row.proProject.projectName + '吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delActYw(row)
                }).catch(function () {

                })
            },
            confirmAjaxPushTpl: function (row) {
                var self = this;
                this.$confirm('确认要推送' + row.proProject.projectName + '吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.ajaxPushTpl(row)
                }).catch(function () {

                })
            },
            ajaxPushTpl: function (row) {
                var self = this;
                this.$axios.post('/actyw/actYw/ajaxPushTpl?id='+row.id).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.getActYwList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '推送成功' : data.msg
                    })
                })
            },
            confirmAjaxIsCurr: function (row) {
                var self = this;
                this.$confirm('设置当前生效，其它同类型的流程将失效，确认要设置当前' + row.proProject.projectName + '生效吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.ajaxIsCurr(row)
                }).catch(function () {

                })
            },
            delActYw: function (row) {
                var self = this;
                this.$axios.post('/actyw/actYw/delActYw', row).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.getActYwList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '删除成功' : data.msg
                    })
                })
            },
            ajaxIsCurr: function (row) {
                var self = this;
                this.$axios.post('/actyw/actYw/ajaxIsCurr?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.getActYwList();
                    }
                    self.$message({
                        type: data.status == '1' ? 'success' : 'error',
                        message: data.status == '1' ? '删除成功' : data.msg
                    })
                })
            },

            getDictLists: function () {
                var dicts = {
                    'yes_no': 'yesNoes',
                    'true_false': 'publishes',
                };
                this.getBatchDictList(dicts)

            },

        },
        mounted: function () {
            this.getActYwList();
            this.getActYw();
            this.getDictLists();
        }

    })

</script>

</body>
</html>