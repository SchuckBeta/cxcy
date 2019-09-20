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
                <el-button type="primary" size="mini" icon="el-icon-circle-plus" @click.stop.prevent="goToAdd">添加
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
        <el-table :data="actYwList" size="small" class="table" border :span-method="objectSpanMethod"
                  style="margin-left: -1px;margin-right: -1px">
            <el-table-column label="项目名称/类型">
                <template slot-scope="scope">
                    {{scope.row.group.flowType ? scope.row.group.name : ''}}/
                    {{scope.row.proProject ? scope.row.proProject.projectName : ''}}
                </template>
            </el-table-column>
            <el-table-column label="工作流程" align="center">
                <template slot-scope="scope">
                    <a :href="ctx + '/actyw/actYwGnode/' + scope.row.group.id + '/view'" target="_blank">
                        {{scope.row.group.name}}
                    </a>
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
            <!-- <el-table-column label="当前生效" align="center">
                <template slot-scope="scope">
                	{{(scope.row.isCurr == 1)  ? '是' : '否'}}
                </template>
            </el-table-column> -->
            <el-table-column label="发布类型" align="center">
                <template slot-scope="scope">
                    <!-- <template v-if="scope.row.isDeploy"> -->
                    {{scope.row.isPreRelease ? '预发布' : '正式发布'}}
                    <!-- </template> -->
                </template>
            </el-table-column>
            <%--<el-table-column label="发布/生效" align="center">--%>
                <%--<template slot-scope="scope">--%>
                    <%--{{scope.row.isDeploy | selectedFilter(publishEntries)}}/--%>
                    <%--{{(scope.row.isCurr == 1) ? '是' : '否'}}--%>
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
                    <a :href="frontOrAdmin + '/actyw/actYw/formGtime?id='+scope.row.id+'&yearId='+scope.row.yearId">修改时间</a>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <c:if test="${aconfig.hasUpProp }">
                            <el-button type="text" size="mini" v-if="ywIds.indexOf(scope.row.id) == -1"
                                       @click.stop.prevent="handleEditProp(scope.row)">修改属性
                            </el-button>
                        </c:if>
                        <c:if test="${!aconfig.hasUpProp }">
                            <el-button type="text" size="mini" v-if="ywIds.indexOf(scope.row.id) == -1"
                                       @click.stop.prevent="handleEditProp(scope.row)">查看属性
                            </el-button>
                        </c:if>
                        <!-- <template v-if="scope.row.isCurr != 1">
                            <el-button type="text" size="mini" @click.stop.prevent="confirmAjaxIsCurr(scope.row)">生效</el-button>
                        </template> -->
                        <template v-if="scope.row.isDeploy">
                            <el-button v-if="scope.row.isPreRelease" type="text" size="mini"
                                       @click.stop.prevent="confirmReleaseActYw(scope.row)">正式发布
                            </el-button>
                            <el-button type="text" size="mini" v-if="ywIds.indexOf(scope.row.id) == -1"
                                       @click.stop.prevent="confirmUnReleaseActYw(scope.row)">取消发布
                            </el-button>
                            <el-button v-if="showActrel" type="text" size="mini"
                                       @click.stop="showDialogSchool(scope.row)">发布到学校
                            </el-button>
                        </template>
                        <template v-else>
                            <el-button type="text" size="mini" @click.stop.prevent="confirmAjaxDeployActYw(scope.row)">
                                预发布
                            </el-button>
                            <el-button type="text" size="mini" @click.stop.prevent="confirmDelActYw(scope.row)">删除
                            </el-button>
                        </template>
                        <%--<el-button type="text" size="mini" @click.stop.prevent="confirmAjaxPushTpl(scope.row)">推送--%>
                        <%--</el-button>--%>
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

    <el-dialog
            v-if="showActrel"
            title="选择发布的学校"
            :visible.sync="dialogVisibleSchool"
            width="580px"
            :close-on-click-modal="false"
            :before-close="handleCloseSchool">
        <el-tabs v-model="activeSchoolType">
            <el-tab-pane v-for="item in schoolTypeList" :key="item.key"  :label="item.name" :name="item.key">
                <el-checkbox-group v-if="schoolListTabpane[item.key] && schoolListTabpane[item.key].length > 0" class="checkbox-group-school" v-model="checkList">
                    <el-checkbox v-for="schoolItem in schoolListTabpane[item.key]" :key="schoolItem.id" :disabled="actYwPscrelIds.indexOf(schoolItem.tenantId) > -1" :label="schoolItem.tenantId">{{schoolItem.schoolName}}</el-checkbox>
                </el-checkbox-group>
                <p v-else class="empty">暂无数据</p>
            </el-tab-pane>
        </el-tabs>
        <span slot="footer" class="dialog-footer">
              <el-tooltip class="item" effect="dark" popper-class="white" :disabled="checkList.length > 0"
                          content="请选择需要发布的学校" placement="bottom">
                  <span>
                    <el-button type="primary" :disabled="checkList.length === 0 || isPublishing" :loading="isPublishing" size="mini"
                       @click.stop="publishToSchool">发布</el-button>
                      </span>
              </el-tooltip>
        </span>
    </el-dialog>
</div>


<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var yesNoes = JSON.parse('${fns: toJson(fns:getDictList('yes_no'))}');
            var publishes = JSON.parse('${fns: toJson(fns: getDictList('true_false'))}');
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    isPreRelease: '',
                    isDeploy: '',
                    'group.flowType': '${aconfig.flowType}'
                },
                pageCount: 0,
                yesNoes: yesNoes,
                publishes: publishes,
                selectName: 'proProject.projectName',
                inputValue: '',
                actYwList: [],
                tableLoading: true,
                actYw: {
                    group: {}
                },
                rowspanArr: [0, 1, 2, 3, 4, 5, 9],
                ywgId: '',
                ywpId: '',
                dialogVisibleSchool: false,
                activeSchoolType: '',
                checkList: [],
                publishId: '',
                schoolList: [],
                schoolTypeList: [],
                actYwPscrelList: [],
                isPublishing: false
            }
        },
        computed: {
            publishEntries: function () {
                return this.getEntries(this.publishes)
            },
            ywIds: function () {
                return [this.ywgId, this.ywpId]
            },

            actYwPscrelIds: function () {
                return this.actYwPscrelList.map(function (item) {
                    return item.schoolTenantId;
                });
            },

            schoolListTabpane: function () {
                var schoolTypeList = this.schoolTypeList;
                var schoolList = this.schoolList;
                var entity = {};
                if(schoolTypeList.length === 0 || schoolList.length === 0){
                    return entity;
                }
                for(var i = 0; i < schoolTypeList.length; i++){
                    if(!entity[schoolTypeList[i].key]){
                        entity[schoolTypeList[i].key] = [];
                    }
                }
                for(var j = 0; j < schoolList.length; j++){
                    var item = schoolList[j];
                    var schoolType = item.schoolType;
                    if(entity[schoolType]){
                        entity[schoolType].push(item);
                    }
                }
                return entity;
            }
        },
        watch: {
            selectName: function (value) {
                this.searchListForm[value] = this.inputValue;
            }
        },
        methods: {

            showDialogSchool: function (row) {
                this.publishId = row.id;
                this.getSchoolByActYw(row)
            },

            handleCloseSchool: function () {
                this.publishId = '';
                this.dialogVisibleSchool = false;
                this.checkList = [];
            },

            getSchoolList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolList = data.data || []
                    }
                })
            },

            getSchoolByActYw: function (row) {
                var self = this;
                this.$axios.post('/actyw/actYw/getSchoolByActYw', {id: row.id}).then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        self.actYwPscrelList = data.data || [];
                        self.dialogVisibleSchool = true;
                    }else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            getSchoolTypeList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolTypeList = JSON.parse(data.data) || [];
                        if(self.schoolTypeList.length > 0){
                            self.activeSchoolType = self.schoolTypeList[0].key
                        }
                    }
                })
            },

            publishToSchool: function () {
                var self = this;
                this.isPublishing = true;
                this.$axios.get('/actyw/actYw/relationModelAndSchool', {
                    params: {
                        id: this.publishId,
                        schools: this.checkList.join(',')
                    }
                }).then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
//                        self.handleCloseSchool();
                        self.ajaxPushTpl({id: self.publishId});
                    }else {
                        self.$alert(data.msg,'提示', {
                            type:'error'
                        })
                        self.isPublishing = false;
                    }
                }).catch(function () {
                    self.isPublishing = false;
                })
            },

            goToAdd: function () {
                location.href = this.frontOrAdmin + '/actyw/actYw/form?group.flowType=' + this.actYw.group.flowType
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
                    if (item.years && item.years.length > 0) {
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
                location.href = this.frontOrAdmin + '/actyw/actYw/formProp?id=' + row.id
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
                this.$axios.post('/actyw/actYw/ajaxPushTpl?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status == 1) {
                        self.$alert('发布到学校成功','提示', {
                            type:'success'
                        })
                        self.getActYwList();
                        self.handleCloseSchool();
                    }else {
                        self.$alert(data.msg,'提示', {
                            type:'error'
                        })
                    }
                    self.isPublishing = false;
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

        },
        created: function () {
            this.getActYwList();
            this.getActYw();
        },
        mounted: function () {
          if(this.showActrel){
             this.getSchoolList();
             this.getSchoolTypeList();
          }
        }

    })

</script>

</body>
</html>