<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>
<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <edit-bar-province></edit-bar-province>
    <div class="content-container">
        <ec-condition v-model="collapsed">
            <ec-condition-item label="地区"
                               :value="searchListForm.areaCode | getSelectedVal(areaEntity, {label:'name', value: 'id'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.areaCode" @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in areas" :label="item.id" :key="item.id">{{item.name}}</ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="高校类型"
                               :value="searchListForm.schoolType | getSelectedVal(schoolTypeEntity, {label:'name', value: 'key'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.schoolType"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in schoolTypeList" :label="item.key" :key="item.key">{{item.name}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="高校"
                               :value="searchListForm.schoolName | getSelectedVal(schoolEntity, {label:'schoolName', value: 'id'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.schoolName">
                    <ec-radio class="ec-radio_all" label="" @change="getPage">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in schoolList" :label="item.id" :key="item.id" @change="getPage">
                            {{item.schoolName}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="项目类型" :value="searchListForm.proCategory | getSelectedVal(proCategoryEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.proCategory"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in proCategory" :label="item.value" :key="item.value">{{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
        </ec-condition>
        <div class="search-bar-container">
            <div class="result-count">
                共选中<span>{{ multipleSelection.length }}</span>条结果
            </div>
            <div class="search-input_content">
                <el-date-picker
                        v-model="timeRange"
                        type="daterange"
                        class="w300"
                        size="mini"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="['00:00:00','23:59:59']"
                        unlink-panels
                        @change="handleChangeTimeRange"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
                <el-input
                        class="w300"
                        size="mini"
                        placeholder="项目名/编号/申报人"
                        @keyup.enter.native="getPage"
                        v-model="searchListForm.queryStr">
                    <el-button slot="append" icon="el-icon-search" @click.stop="getPage"></el-button>
                </el-input>
            </div>
            <c:if test="${isAdmin eq 1}">
                <div class="search-buttons_content">
                    <span>
                    <el-button size="mini" class="mgl-10" @click.stop="confirmChangeProject">
                        一键转换“互联网+”
                    </el-button>
                        </span>
                    <span>
                    <el-button size="mini" type="primary" @click.stop="goToCertAssign"
                               icon="iconfont icon-guanlizhengshu">
                        下发证书
                    </el-button>
                        </span>
                    <el-tooltip class="item" content="请选择需要发布的优秀项目" :disabled="multipleSelection.length > 0"
                                popper-class="white" placement="bottom">
                        <span>
                <el-button size="mini" type="primary"
                           @click.stop.prevent="publishExPro"
                           :disabled="multipleSelection.length < 1" icon="iconfont icon-icon-project">发布优秀项目
                </el-button>

                            </span>
                    </el-tooltip>
                </div>
            </c:if>
        </div>
        <div v-loading="loading" class="table-container">
            <el-table
                    :data="pageList"
                    @selection-change="handleSelectionChange"
                    @sort-change="handleSortChange"
                    size="mini"
                    class="mgb-20"
                    ref="multipleTable">
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column label="项目信息" width="360">
                    <template slot-scope="scope">
                        <project-info-column-gc
                                :key="scope.row.id"
                                :row="scope.row"
                                :to="ctx + '/promodel/proModel/provViewForm?id='+ scope.row.proModel.id+'&actYwId='+scope.row.actYwId">
                        </project-info-column-gc>
                    </template>
                </el-table-column>
                <el-table-column label="团队成员" width="230">
                    <template slot-scope="scope">
                        <project-team-column :row="scope.row.proModel" :key="scope.row.id"></project-team-column>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="proCategory"
                        label="项目类别"
                        align="center"
                        sortable="custom">
                    <template slot-scope="scope">
                        {{scope.row.proModel.proCategory | getSelectedVal(proCategoryEntity)}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        sortable="custom"
                        align="center"
                        label="申请日期">
                    <template slot-scope="scope">{{ scope.row.createDate | formatDate }}</template>
                </el-table-column>
                <%--<el-table-column align="center" label="项目级别">--%>
                <%--<template slot-scope="scope">{{ scope.row.proModel.level }}</template>--%>
                <%--</el-table-column>--%>
                <el-table-column prop="score" align="center" label="专家评分" sortable="custom">
                    <template slot-scope="scope">{{ scope.row.proModel.level }}</template>
                </el-table-column>
                <el-table-column align="center" label="项目最新状态">
                    <template slot-scope="scope">{{ scope.row.proModel.level }}</template>
                </el-table-column>
                <el-table-column align="center" label="状态">
                    <template slot-scope="scope">
                        <a :href="ctx +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank">
                            <span v-if="scope.row.state == '1'">项目已结项</span><span v-else>待{{scope.row.proModel.auditMap.taskName}}</span>
                        </a>
                    </template>
                </el-table-column>
                <%--<shiro:hasPermission name="promodel:promodel:modify">--%>
                <%--<el-table-column align="center" label="操作">--%>
                <%--<template slot-scope="scope">--%>
                <%--<div class="table-btns-action">--%>
                <%--<el-button type="text" size="mini" @click.stop="goToChangeCharge(scope.row)">变更</el-button>--%>
                <%--</div>--%>
                <%--</template>--%>
                <%--</el-table-column>--%>
                <%--</shiro:hasPermission>--%>
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
</div>
<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var page = JSON.parse(JSON.stringify(${fns: toJson(page)})) || {};
            var pageCount = page.count || 0;
            var pageList = page.list || [];
            return {
                collapsed: true,
                searchListForm: {
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    schoolType: '',
                    areaCode: "",
                    queryStr: '',
                    schoolName: '',
                    proCategory: '',
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    beginDate: '',
                    endDate: ''
                },
                timeRange: [],
                loading: false,
                pageCount: pageCount,
                pageList: pageList,
                areas: [],
                schoolTypeList: [],
                proCategory: [],
                multipleSelection: [],
                schoolList: []
            }
        },
        computed: {
            areaEntity: function () {
                return this.getEntity(this.areas, {label: 'name', value: 'id'})
            },
            schoolTypeEntity: function () {
                return this.getEntity(this.schoolTypeList, {label: 'name', value: 'key'})
            },
            proCategoryEntity: function () {
                return this.getEntity(this.proCategory)
            },
            schoolEntity: function () {
                return this.getEntity(this.schoolList, {label: 'schoolName', value: 'id'})
            }
        },
        methods: {
            publishExPro: function () {
                var self = this;
                this.$confirm('确定发布所选项目到门户网站？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.publishExProAxios();
                })
            },

            publishExProAxios: function () {
                var self = this;
                var multipleSelectedIds = this.multipleSelection.map(function (item) {
                    return item.id;
                })
                var ids = multipleSelectedIds.join(',');
                this.$axios.get('/cms/cmsArticle/excellent/projectPublish', {params: {ids: ids}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('发布成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            handleChangeTimeRange: function (value) {
                if (!value) {
                    this.searchListForm.beginDate = '';
                    this.searchListForm.endDate = '';
                } else {
                    this.searchListForm.beginDate = value[0];
                    this.searchListForm.endDate = value[1];
                }
                this.getPage();
            },


            goToCertAssign: function () {
                location.href = this.ctx + '/certMake/list?actywId=' + this.searchListForm.actywId;
            },

            getSchoolCityList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolCityList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.areas = data.data || []
                    }
                })
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

            getSchoolTypeList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolTypeList = JSON.parse(data.data) || []
                    }
                })
            },

            handleSelectionChange: function (val) {
                this.multipleSelection = val;
            },

            handlePaginationSizeChange: function (val) {
                this.searchListForm.pageSize = val;
                this.getPage()
            },

            handlePaginationPageChange: function () {
                this.getPage()
            },
            goToChangeCharge: function (row) {
                location.href = this.ctx + '/promodel/proModel/projectEdit?id=' + row.id + '&secondName=' + encodeURI("变更")
            },

            getPage: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/cms/ajax/ajaxQueryMenuList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.page) {
                        var page = data.page || {};
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        self.pageList = page.list || [];
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
            },

            handleSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getPage();
            },

            getDictLists: function () {
                var dicts = {
                    'project_type': 'proCategory',
                };
                this.getBatchDictList(dicts)
            },
        },
        mounted: function () {
            this.getSchoolCityList();
            this.getSchoolTypeList();
            this.getSchoolList();
            this.getDictLists();
        }
    })


</script>
</body>
</html>