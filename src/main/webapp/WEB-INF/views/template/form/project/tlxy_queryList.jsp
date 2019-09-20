<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" v-show="pageLoad" class="container-fluid" style="display: none">
    <edit-bar></edit-bar>
    <div class="clearfix">

        <div class="conditions">
            <e-condition type="checkbox" v-model="searchListForm['proModel.deuser.office.id']" label="学院"
                         :options="colleges"
                         :default-props="{label: 'name', value: 'id'}" @change="searchCondition"
                         name="proModel.deuser.office.id"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm['proModel.proCategory']" label="项目类别"
                         :options="proCategories"
                         name="proModel.proCategory" @change="searchCondition"></e-condition>
            <%--<e-condition type="checkbox" v-model="searchListForm['proModel.finalStatus']" label="项目级别"--%>
                         <%--:options="adviceLevel"--%>
                         <%--name="proModel.finalStatus" @change="searchCondition"></e-condition>--%>
        </div>

        <div class="search-block_bar clearfix">
            <div class="search-btns">

                    <button-import v-if="isAdmin != null && isAdmin =='1'" :is-first-menu="true" :actyw-id="searchListForm.actywId" href="/impdata/promodellist?"
                                   :gnode-id="searchListForm.gnodeId" :is-query-menu="true"></button-import>

                <button-export  v-if="isAdmin != null && isAdmin =='1'" :spilt-prefs="spiltPrefs" :spilt-posts="spiltPosts" label="项目"
                               :search-list-form="searchListForm"></button-export>

                    <el-button v-if="isAdmin != null && isAdmin =='1'" size="mini" type="primary" @click.stop.prevent="goToCertAssign"
                               :disabled="projectListMultipleSelection.length < 1"><i
                            class="iconfont icon-guanlizhengshu"></i>证书下发
                    </el-button>


                    <export-ex-pro v-if="isAdmin != null && isAdmin =='1'" :fids="projectListMultipleSelection" project-type="project"
                                   :disabled="projectListMultipleSelection.length < 1"></export-ex-pro>


                    <el-button v-if="isAdmin != null && isAdmin =='1'" size="mini" type="primary" @click.stop.prevent="deleteProject(multipleSelectedId.join(','))"
                               :disabled="projectListMultipleSelection.length < 1"><i class="iconfont icon-delete"></i>批量删除
                    </el-button>


            </div>
            <div class="search-input">
                <el-date-picker
                        v-model="applyDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        @change="searchCondition"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd"
                        style="width: 270px;">
                </el-date-picker>
                <el-input
                        placeholder="项目名称/编号/负责人/组成员/指导教师"
                        size="mini"
                        name="proModel.queryStr"
                        v-model="searchListForm['proModel.queryStr']"
                        style="width: 300px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchCondition"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="selected-list_len-bar">选择<span class="selected-num">{{listLen}}</span>条数据</div>
    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="projectListFlatten" class="table" ref="multipleTable" v-loading="loading"
                  @sort-change="handleTableSortChange"
                  @selection-change="handleTableSelectionChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="项目信息" align="left"  width="240" >
                <template slot-scope="scope">
                    <span v-if="scope.row.finalStatus == '0000000264'">{{scope.row.competitionNumber}}</span>
                    <e-set-text v-else-if="scope.row.finalStatus == '0000000265'"
                                :editing.sync="controlEditings[scope.row.id]" :min="5" :max="24"
                                :text="scope.row.pCompetitionNumber" :row="scope.row"
                                @change="handleChangeText"></e-set-text>
                    <e-set-text v-else-if="scope.row.finalStatus == '0000000266'"
                                :editing.sync="controlEditings[scope.row.id]" :min="5" :max="24"
                                :text="scope.row.gCompetitionNumber" :row="scope.row"
                                @change="handleChangeText"></e-set-text>
                    <p class="over-flow-tooltip">
                        <el-tooltip class="item" effect="dark" :content="scope.row.pName" popper-class="white" placement="bottom">
                            <a :href="frontOrAdmin + '/promodel/proModel/viewForm?id=' + scope.row.id"
                               class="black-a project-name-tooltip">{{scope.row.pName}}</a>
                        </el-tooltip>
                    </p>
                    <p>
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip class="item" effect="dark" :content="scope.row.officeName" popper-class="white" placement="bottom">
                            <span class="over-flow-tooltip project-office-tooltip">{{scope.row.officeName}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" min-width="150">
                <template slot-scope="scope">
                    <p>负责人：{{scope.row.deuser.name}}</p>
                    <p class="over-flow-tooltip">成员：
                        <el-tooltip class="item" effect="dark" :content="scope.row.team.entName" popper-class="white" placement="bottom">
                            <span class="team-member-tooltip">{{scope.row.team.entName}}</span></el-tooltip>
                    </p>
                    <p class="over-flow-tooltip">导师：
                        <el-tooltip class="item" effect="dark" :content="scope.row.team.uName" popper-class="white" placement="bottom"><span
                                class="team-member-tooltip">{{scope.row.team.uName}}</span></el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="项目类别" align="center" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="申报日期" align="center" min-width="110">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <%--<el-table-column label="项目级别" align="center" prop="finalStatus" min-width="100">--%>
                <%--<template slot-scope="scope">--%>
                    <%--{{scope.row.finalStatus | selectedFilter(projectLevelEntries)}}--%>
                <%--</template>--%>
            <%--</el-table-column>--%>
            <el-table-column label="审核结果" align="center"  min-width="110" >
                <template slot-scope="scope">
                    {{scope.row.finalResult}}
                </template>
            </el-table-column>
            <el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <template v-if="scope.row.state == '1'">
                        <a class="black-a"
                           :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank">项目已结项</a>
                    </template>
                    <template v-else>
                        <a class="black-a"
                           :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank">待{{scope.row.auditMap.taskName}}</a>
                    </template>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" min-width="120">
                <template slot-scope="scope">
                    <shiro:hasPermission name="promodel:promodel:modify">
                        <div class="table-btns-action">
                            <el-button type="text" size="mini" @click.stop.prevent="goToProcessTrack(scope.row)">进度跟踪
                            </el-button>
                            <el-button type="text" size="mini" style="margin-left: 0"
                                       @click.stop.prevent="goToChangeCharge(scope.row)">变更
                            </el-button>
                        </div>
                    </shiro:hasPermission>
                    <div class="table-btns-action">
                        <button-audit-record :row="scope.row"></button-audit-record>
                        <shiro:hasPermission name="promodel:promodel:modify">
                            <el-button type="text" size="mini" @click.stop.prevent="deleteProject(scope.row.id)">删除
                            </el-button>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="promodel:promodel:modify">
                            <el-button type="text" v-if="scope.row.state != '1'" size="mini" @click.stop.prevent="endProject(scope.row)">结束
                            </el-button>
                        </shiro:hasPermission>
                    </div>
                    <%--<shiro:hasPermission name="excellent:projectShow:edit">--%>
                        <%--<el-button type="text" size="mini" @click.stop.prevent="gotoProjectShowForm(scope.row.id)">展示--%>
                        <%--</el-button>--%>
                    <%--</shiro:hasPermission>--%>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
</div>

<script>

    'use strict';
    new Vue({
        el: '#app',
        data: function () {
            var proCategories = JSON.parse('${fns: toJson(fns:getDictList("project_type"))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var professionals = JSON.parse(JSON.stringify(${fns: toJson(fns: findColleges())})) || [];
            var spiltPrefs = JSON.parse('${fns:spiltPrefs()}');
            var spiltPosts = JSON.parse('${fns:spiltPosts()}');
            var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)})) || [];
            var pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            return {
                proCategories: proCategories,
                adviceLevel: projectDegrees,
                colleges: professionals,
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                projectList: pageList,
                projectListMultipleSelection: [],
                controlEditings: {},
                pageCount: pageCount,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    'proModel.beginDate': '',
                    'proModel.endDate': '',
                    'proModel.queryStr': '',
                    'proModel.finalStatus': [],
                    'proModel.proCategory': [],
                    'proModel.deuser.office.id': []
                },
                loading: false,
                applyDate: [],
                multipleSelectedId: [],
                isAdmin: '${isAdmin}'
            }
        },
        computed: {

            projectListFlatten: {
                get: function () {
                    var projectList = this.projectList;
                    var list = [];
                    if (!projectList || projectList.length < 1) return [];
                    for (var i = 0; i < projectList.length; i++) {
                        var proModel = Object.assign({}, projectList[i].proModel);
                        proModel['gCompetitionNumber'] = projectList[i].gCompetitionNumber;
                        proModel['pCompetitionNumber'] = projectList[i].pCompetitionNumber;
                        var id = proModel.id;
                        proModel = Object.assign(proModel, proModel.vars)
                        proModel = Object.assign(proModel, proModel.team)
                        proModel = Object.assign(proModel, proModel.deuser)
                        proModel.id = id;
                        list.push(proModel)
                    }
                    return list;
                }
            },


            projectLevelEntries: {
                get: function () {
                    return this.getEntries(this.adviceLevel)
                }
            },

            listLen: {
                get: function () {
                    return this.projectListMultipleSelection.length;
                }
            },
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
            }
        },
        watch: {
            applyDate: function (value) {
                value = value || [];
                this.searchListForm['proModel.beginDate'] = value[0];
                this.searchListForm['proModel.endDate'] = value[1];
                this.searchCondition();
            }
        },
        methods: {
            endProject:function (row) {
                var self = this;
                this.$confirm('确认结束该项目整个流程吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'GET',
                        url: '/promodel/proModel/finishProject',
                        params:{
                            proModelId:row.id
                        }
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.searchCondition();
                        }
                        self.$message({
                            message: data.status == '1' ? '结束成功' : data.msg || '结束失败',
                            type: data.status == '1' ? 'success' : data.msg || 'error'
                        });
                    }).catch(function () {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'warning'
                        });
                    })
                }).catch(function () {
                    
                })
            },
            gotoProjectShowForm: function (id) {
                location.href = this.frontOrAdmin + '/excellent/projectShowForm?projectId=' + id;
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.searchCondition();
            },

            handleTableSelectionChange: function (val) {
                this.projectListMultipleSelection = val;
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].id);
                }
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.searchCondition();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.searchCondition();
            },

            searchCondition: function () {
                this.getQueryMenuList();
            },

            handleChangeText: function (obj) {
                var self = this;
                var text = obj.text;
                var row = obj.row;
                var projectList = this.projectList;
                var proModelI = {};

                for (var i = 0; i < projectList.length; i++) {
                    if (projectList[i].proModel.id === row.id) {
                        proModelI = projectList[i].proModel;
                        break;
                    }
                }

                if (row.finalStatus == '0000000265' && text == row.pCompetitionNumber) {
                    self.controlEditings[proModelI.id] = false;
                } else if (row.finalStatus == '0000000266' && text == row.gCompetitionNumber) {
                    self.controlEditings[proModelI.id] = false;
                } else {
                    var numXhr = self.$axios({
                        method: 'POST',
                        url: '/workflow.tlxy/proModelTlxy/ajax/ajaxSaveNum?proModelId=' + row.id + '&num=' + text + '&type=' + row.finalStatus
                    });
                    numXhr.then(function (response) {
                        var data = response.data;
                        if (data.ret == '1') {
                            Vue.set(proModelI, 'competitionNumber', text);
                            self.controlEditings[proModelI.id] = false;
                            self.searchCondition();
                        } else if (data.ret == '0') {
                            self.$alert('该项目级别的编号已存在', '提示', {
                                confirmButtonText: '确定',
                                type: 'warning'
                            });
                        }
                    }).catch(function () {
                        self.$alert('操作异常', '提示', {
                            confirmButtonText: '确定',
                            type: 'warning'
                        });
                    })
                }

            },
            goToProcessTrack: function (row) {
                location.href = this.frontOrAdmin + '/promodel/proModel/process?id=' + row.id
            },

            goToChangeCharge: function (row) {
                location.href = this.frontOrAdmin + '/promodel/proModel/projectEdit?id=' + row.id + '&secondName='+encodeURI("变更")
            },

            goToCertAssign: function () {
                location.href = this.frontOrAdmin + '/certMake/list?actywId=' + this.searchListForm.actywId;
            },

            deleteProject: function (ids) {
                var self = this;
                this.$confirm('此操作将永久删除文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: '/promodel/proModel/ajaxPromodelDelete?ids=' + ids
                    }).then(function (response) {
                        var data = response.data;
                        if (data.ret == '1') {
                            self.searchCondition();
                            self.$message({
                                message: '删除成功',
                                type: 'success'
                            });
                        }
                    }).catch(function () {
                        self.$alert('请求失败', '提示', {
                            confirmButtonText: '确定',
                            type: 'warning'
                        });
                    })
                })
            },

            getQueryMenuList: function () {
                var self = this;
                this.getControlEditings();
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/cms/ajax/ajaxQueryMenuList?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var page = response.data.page;
                    self.pageCount = page.count;
                    self.searchListForm.pageSize = page.pageSize;
                    self.projectList = page.list || [];
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
            },

            getControlEditings: function () {
                var projectList = this.projectList;
                for (var i = 0; i < projectList.length; i++) {
                    Vue.set(this.controlEditings, projectList[i].proModel.id, false);
                }
            }
        },
        created: function () {
            this.getControlEditings();
//            this.getQueryMenuList();
//            console.log(this.searchListForm);
        }
    })

</script>
</body>
</html>