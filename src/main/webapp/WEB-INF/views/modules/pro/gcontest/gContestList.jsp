<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>我的大赛</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="mgb-20 text-right">
        <el-button size="mini" @click.stop="goToList">大赛列表</el-button>
    </div>
    <div class="table-container">
        <el-table :data="projectList" size="mini" class="table">
            <el-table-column label="项目信息">
                <template slot-scope="scope">
                    <p>{{scope.row.competitionNumber}}</p>
                    <p><a :href="ctxFront+'/promodel/proModel/viewForm?id='+scope.row.id">{{scope.row.pName}}</a>
                    </p>
                    <p>{{scope.row.proTypeStr}}</p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息">
                <template slot-scope="scope">
                    <p style="margin-bottom: 0">{{scope.row.declareName}}</p>
                    <p v-show="scope.row.snames && scope.row.snames.split('/').length > 1">{{scope.row.snames}}</p>
                    <p v-show="scope.row.tnames">{{scope.row.tnames}}</p>
                </template>
            </el-table-column>
            <el-table-column prop="gcTrack" label="赛道" align="center">
                <template slot-scope="scope">
                    {{scope.row.gcTrack | selectedFilter(trackEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="level" label="参赛组别" align="center">
                <template slot-scope="scope">
                    {{scope.row.level | selectedFilter(gContestLevelEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="type" label="大赛" align="center">
                <template slot-scope="scope">
                    {{scope.row.type | selectedFilter(competitionEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="createDate" label="申报时间" align="center">
                <template slot-scope="scope">
                    {{scope.row.createDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="项目状态" align="center">
                <template slot-scope="scope">
                    <template v-if="scope.row.status_code === '-999'">
                        <%--<a href="javascript: void(0);" @click.stop.prevent="goToFlowView(scope.row)">{{scope.row.status}}</a>--%>
                        <a v-if="scope.row.proc_ins_id" :href="ctxFront + '/actyw/actYwGnode/designView?groupId=' + scope.row.groupId + '&proInsId=' + scope.row.proc_ins_id"
                           target="_blank">{{scope.row.status}}</a>
                        <a v-else href="javascript: void(0);">{{scope.row.status}}</a>
                    </template>
                    <template v-else>
                        <%--<a v-if="scope.row.subStatus === '1'" href="javascript: void(0);"--%>
                           <%--@click.stop.prevent="goToFlowView(scope.row)">--%>
                            <%--<template v-if="scope.row.state =='1'">项目已结项</template>--%>
                            <%--<template v-else>--%>
                                <%--<project-status :id="scope.row.id" v-if="scope.row.proc_ins_id"></project-status>--%>
                            <%--</template>--%>
                        <%--</a>--%>
                        <span v-if="scope.row.subStatus === '1'">
                            <a v-if="scope.row.proc_ins_id" target="_blank"
                               :href="ctxFront + '/actyw/actYwGnode/designView?groupId=' + scope.row.groupId + '&proInsId=' + scope.row.proc_ins_id">
                                <template v-if="scope.row.state =='1'">项目已结项</template>
                                <template v-else>
                                    <project-status :id="scope.row.id" v-if="scope.row.proc_ins_id"></project-status>
                                </template>
                            </a>
                            <a v-else href="javascript: void(0);">
                                <template v-if="scope.row.state =='1'">项目已结项</template>
                                <template v-else>
                                    <project-status :id="scope.row.id" v-if="scope.row.proc_ins_id"></project-status>
                                </template>
                            </a>
                        </span>
                    </template>
                </template>
            </el-table-column>
            <el-table-column v-if="isStudent" label="操作" align="center">
                <template slot-scope="scope">
                    <div v-if="scope.row.declareId === loginCurUser.id" class="table-btns-action">
                       <%-- <el-button :disabled="scope.row.status_code == '0'"
                                type="text" size="mini" @click.stop="confirmWithdraw(scope.row)">撤回
                        </el-button>--%>


                        <el-button  :disabled="scope.row.subStatus == '1'"
                                    type="text" size="mini" @click.stop="goToProjectForm(scope.row)">继续完善
                        </el-button>
                        <br/>
                        <el-button :disabled="scope.row.subStatus == '0'"
                                   type="text" size="mini" @click.stop="confirmWithdraw(scope.row)">撤回
                        </el-button>
                        <br>
                        <el-button  :disabled="scope.row.subStatus == '1'"
                                    type="text" size="mini" @click.stop="confirmDelProject(scope.row)">删除
                        </el-button>
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
                    :page-sizes="[5,10,20]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>

</div>

<script>
    'use strict';

    Vue.component('project-status', {
        template: '<span>待{{taskMap.taskName}}</span>',
        props: {
            id: String
        },
        data: function () {
            return {
                taskMap: {}
            }
        },
        methods: {
            getActByPromodelId: function () {
                var self = this;
                this.$axios.get('/project/getActByPromodelId?proModelId=' + this.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.taskMap = data.data || {}
                    }
                })
            }
        },
        created: function () {
            this.getActByPromodelId();
        }
    })

    new Vue({
        el: '#app',
        data: function () {
            var competitionTypes = JSON.parse('${fns: toJson(fns: getDictList('competition_net_type'))}');
            var tracks =  JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            return {
                projectList: [],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                competitionTypes: competitionTypes,
                tracks: tracks,
                gContestLevel: []
            }
        },
        computed: {
            competitionEntries: function () {
                return this.getEntries(this.competitionTypes)
            },
            trackEntries: function () {
                return this.getEntries(this.tracks)
            },
            gContestLevelEntries: function () {
                return this.getEntries(this.gContestLevel)
            }
        },
        methods: {
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getProjectList();
            },

            handlePCPChange: function () {
                this.getProjectList();
            },

            goToList: function () {
                location.href = this.ctxFront + '/gcontest/gContest/';
            },

            goToProjectForm: function (row) {
                var href = this.ctxFront+'/promodel/proModel/form?id=' + row.id;
                if (row.ftb == 0) {
                    href = this.ctxFront + '/project/projectDeclare/form?id=' + row.id;
                }
                location.href = href;
            },

            goToFlowView: function (row) {
                if (row.proc_ins_id) {
                    location.href = this.ctxFront + '/actyw/actYwGnode/designView?groupId=' + row.groupId + '&proInsId=' + row.proc_ins_id;
                }
            },

            confirmDelProject: function (row) {
                var self = this;
                this.$confirm("确认要删除该项目申报吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delProject(row)
                }).catch(function () {

                })
            },

            delProject: function (row) {
                var self = this;
                var params = {
                    id: row.id,
                    ftb: row.ftb
                };
                this.$axios.get('/project/projectDeclare/delProject?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('删除成功');
                        self.getProjectList();
                    } else {
                        self.$message.error(data.msg)
                    }
                })
            },

            getProjectList: function () {
                var self = this;
                this.$axios.get('/gcontest/gContest/getGContestList',{params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data;
                        self.projectList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    }
                })
            },


            confirmWithdraw: function (row) {
                var self = this;
                this.$confirm("确认要撤回大赛申报吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.withdrawProject(row)
                }).catch(function () {

                })
            },

            withdrawProject: function (row) {
                var self = this;
                this.$axios.get('/promodel/proModel/revertAfterApplyed', {params: {id: row.id}}).then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        self.$message({
                            type: 'success',
                            message: data.msg
                        })
                        self.getProjectList();
                    }else {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            },

            getDictListLevels: function () {
                var self = this;
                this.tracks.forEach(function (item) {
                    self.$axios.get('/sys/dict/getDictList?type='+item.value).then(function (response) {
                        var data = response.data || [];
                        self.gContestLevel = self.gContestLevel.concat(data);
                    })
                })
            }
        },
        created: function () {
            this.getProjectList();
        },
        mounted: function () {
            this.getDictListLevels();
        }
    })

</script>

</body>
</html>