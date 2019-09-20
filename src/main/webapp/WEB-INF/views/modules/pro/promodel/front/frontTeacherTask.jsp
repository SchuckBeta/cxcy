<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <style>
        .el-date-editor .el-range-separator {
            width: auto;
        }
    </style>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>导师审核</el-breadcrumb-item>
    </el-breadcrumb>
    <el-form :model="searchListForm" ref="searchListForm" size="mini" style="margin-top: 60px;">
        <%--<div class="conditions">--%>
            <%--<e-condition label="学院" type="radio" v-model="searchListForm['proModel.deuser.office.id']"--%>
                         <%--:default-props="defaultProps"--%>
                         <%--name="proModel.deuser.office.id" :options="collegeList" @change="getDataList"></e-condition>--%>
            <%--<e-condition label="项目类别" type="radio" v-model="searchListForm['proModel.proCategory']"--%>
                         <%--name="proModel.proCategory" :options="proCategories" @change="getDataList"></e-condition>--%>
            <%--<e-condition label="项目级别" type="radio" v-model="searchListForm['proModel.finalStatus']"--%>
                         <%--name="proModel.finalStatus" :options="projectDegrees" @change="getDataList"></e-condition>--%>
        <%--</div>--%>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <el-date-picker
                        v-model="applyDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        @change="getDataList"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="searchDefaultTime"
                        style="width: 270px;">
                </el-date-picker>
                <input type="text" style="display:none">
                <el-input name="queryStr" size="mini" class="w300"
                          v-model="searchListForm.queryStr"
                          placeholder="项目名称/编号/负责人/组成员/指导教师" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>


    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @sort-change="handleTableSortChange">
            <el-table-column label="项目信息" align="left" width="240" sortable="competitionNumber">
                <template slot-scope="scope">
                    <div>{{scope.row.competitionNumber}}</div>
                    <el-tooltip :content="scope.row.pName" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.pName}}</span>
                    </el-tooltip>
                    <div>
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip :content="scope.row.officeName" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.officeName}}</span>
                        </el-tooltip>
                    </div>
                </template>
            </el-table-column>
            <el-table-column label="团队成员" align="left" min-width="155">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>

            <el-table-column label="项目类别" align="center" min-width="85">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="申报日期" align="center" prop="subTime" min-width="110" sortable="subTime">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <%--<el-table-column label="项目级别" align="center" prop="finalStatus" min-width="85">--%>
                <%--<template slot-scope="scope">--%>
                    <%--{{scope.row.finalStatus | selectedFilter(projectDegreesEntries)}}--%>
                <%--</template>--%>
            <%--</el-table-column>--%>

            <el-table-column label="审核结果" align="center" prop="finalResult" min-width="110" sortable="finalResult">
                <template slot-scope="scope">
                    {{scope.row.finalResult}}
                </template>
            </el-table-column>
            <el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <a class="black-a"
                       :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                       target="_blank">
                        <span v-if="scope.row.state == '1'">项目已结项</span>
                        <span v-else>待{{scope.row.auditMap.taskName}}</span>
                    </a>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center" min-width="60">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button @click.stop.prevent="goToAudit(scope.row)" type="text" size="small"
                                   :disabled="scope.row.state == '1' || scope.row.auditMap.status != 'todo'">审核
                        </el-button>
                    </div>
                </template>
            </el-table-column>

        </el-table>
        <div class="text-right mgb-20" v-if="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total, prev, pager, next, sizes"
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
            var professionals = JSON.parse(JSON.stringify(${fns:getOfficeListJson()})) || [];
            var proCategories = JSON.parse(JSON.stringify(${fns:toJson(fns: getDictList('project_type'))}));
            var projectDegrees = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList(levelDict))}));
            <%--var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)}));--%>
//            console.log(pageList);
            return {
                professionals: professionals,
                proCategories: proCategories,
                projectDegrees: projectDegrees,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
//                    'proModel.deuser.office.id': '',
//                    'proModel.proCategory': '',
//                    'proModel.finalStatus': '',
                    beginDate: '',
                    endDate: '',
                    queryStr: ''
                },
                applyDate: [],
                searchDefaultTime: ['00:00:00', '23:59:59'],
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                multipleSelection: [],
                multipleSelectedId: [],
                pageCount: 0,
                loading: false,
                pageList: []
            }
        },
        computed: {
            collegeList: {
                get: function () {
                    return this.professionals.filter(function (item) {
                        return item.grade == '2';
                    })
                }
            },
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
            },
            projectDegreesEntries: {
                get: function () {
                    return this.getEntries(this.projectDegrees)
                }
            }
        },
        watch: {
            applyDate: function (value) {
                value = value || [];
                this.searchListForm.beginDate = value[0];
                this.searchListForm.endDate = value[1];
                this.getDataList();
            }
        },
        methods: {
            goToAudit: function (row) {
                location.href = this.frontOrAdmin + '/promodel/proModel/auditForm?gnodeId=' + row.auditMap.gnodeId + '&proModelId=' + row.auditMap.proModelId + '&taskName=' + encodeURI(row.auditMap.taskName);
            },
            getPwEnterTeamInfo: function (row) {
                var eteam = row.team || {};
                var applicant = row.deuser;
                return {
                    applicantName: applicant.name,
                    snames: eteam.entName || '',
                    tnames: eteam.uName || ''
                }
            },
            getDataList: function () {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/ajaxGetTeacherAuditTaskList?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if(data.data){
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                })
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            }
        },
        created: function () {
            this.getDataList();
        }
    });


</script>

</body>
</html>