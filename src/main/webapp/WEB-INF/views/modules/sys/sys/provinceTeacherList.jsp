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
            <ec-condition-item label="行业" :value="searchListForm.industry">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.industry" @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in industries" :label="item.label" :key="item.id">{{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="领域" :value="searchListForm['user.domain'] | getSelectedVal(domainsEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm['user.domain']"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in domains" :label="item.value" :key="item.id">{{item.label}}</ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="职务"
                               :value="searchListForm.postTitle">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.postTitle"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in postTitles" :label="item.label" :key="item.id">{{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="服务意向"
                               :value="searchListForm.serviceIntention | getSelectedVal(masterHelpsEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.serviceIntention"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in masterHelps" :label="item.value" :key="item.value">{{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="推荐机构" :value="searchListForm.recommendedUnits">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.recommendedUnits"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in recommendedUnitsDict" :label="item.label" :key="item.id">
                            {{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
        </ec-condition>
        <div class="search-bar-container">
            <div class="result-count">共选中<span>{{ multipleSelection.length }}</span>条结果</div>
            <div class="search-input_content">
                <el-input class="w300" size="mini" placeholder="请输入关键字搜索" @keyup.enter.native="getPage"
                          v-model="searchListForm.queryStr">
                    <el-button slot="append" icon="el-icon-search" @click.stop="getPage"></el-button>
                </el-input>
            </div>
            <div class="search-buttons_content">
                <el-button size="mini" type="primary" @click.stop.prevent="add" icon="el-icon-circle-plus">添加
                </el-button>
                <el-button size="mini" @click.stop.prevent="batchDelete" icon="el-icon-delete">删除</el-button>
            </div>
        </div>
        <div v-loading="loading" class="table-container">
            <el-table :data="pageList" @selection-change="handleSelectionChange" @sort-change="handleSortChange"
                      size="mini" class="mgb-20" ref="multipleTable">
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column label="导师信息" min-width="300">
                    <template slot-scope="scope">
                        <div class="teacher-as-info-cell">
                            <div class="user-img">
                                <img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                            </div>
                            <div class="user-detail">
                                <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                                    <a :href="ctx + '/sys/backTeacherExpansion/provinceTeacherView?user.id=' + scope.row.user.id">
                                        {{scope.row.user.name | textEllipsis(12)}}
                                    </a>
                                </el-tooltip>
                                <span>{{scope.row.user.sex | getSelectedVal(sexesEntity)}}</span>
                                <span v-if="!!scope.row.workUnit"><br></span>
                                <el-tooltip :content="scope.row.workUnit" popper-class="white" placement="right">
                                    <span class="break-ellipsis">{{scope.row.workUnit}}</span>
                                </el-tooltip>
                            </div>
                        </div>
                    </template>
                </el-table-column>
                <el-table-column prop="a.post_title" label="职务" sortable="a.post_title" min-width="90">
                    <template slot-scope="scope">
                        {{scope.row.postTitle}}
                    </template>
                </el-table-column>
                <el-table-column prop="a.industry" label="行业" sortable="a.industry" min-width="110">
                    <template slot-scope="scope">
                        {{scope.row.industry}}
                    </template>
                </el-table-column>
                <el-table-column prop="u.domain" label="擅长领域" sortable="u.domain" min-width="150">
                    <template slot-scope="scope">
                        {{scope.row.user.domainlt}}
                    </template>
                </el-table-column>
                <el-table-column prop="a.service_intention" label="服务意向" sortable="a.service_intention" min-width="130">
                    <template slot-scope="scope">
                        {{scope.row.serviceIntention | getCheckboxValue(masterHelpsEntity)}}
                    </template>
                </el-table-column>
                <el-table-column prop="recommendedUnits" label="推荐机构" min-width="90">
                </el-table-column>
                <el-table-column align="center" label="操作" min-width="100">
                    <template slot-scope="scope">
                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                               <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                       @click.stop="edit(scope.row.user.id)"></el-button>
                                   </span>
                        </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <span>
                            <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop="singleDelete(scope.row.user.id)"></el-button>
                                </span>
                        </el-tooltip>
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
</div>
<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                collapsed: true,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    industry: '',
                    'user.domain': '',
                    postTitle: '',
                    serviceIntention: '',
                    recommendedUnits: '',
                    queryStr: ''
                },
                loading: false,
                pageCount: 0,
                pageList: [],
                multipleSelection: [],
                multipleSelectedId: [],
                industries: [],
                domains: [],
                postTitles: [],
                masterHelps: [],
                recommendedUnitsDict: [],
                sexes: [],
            }
        },
        computed: {
            sexesEntity: function () {
                return this.getEntity(this.sexes);
            },
            masterHelpsEntity: function () {
                return this.getEntity(this.masterHelps);
            },
            domainsEntity: function () {
                return this.getEntity(this.domains);
            }
        },
        methods: {
            add: function () {
                location.href = this.ctx + '/sys/backTeacherExpansion/provinceTeacherForm';
            },

            edit: function (id) {
                location.href = this.ctx + '/sys/backTeacherExpansion/provinceTeacherForm?user.id=' + id;
            },

            batchDelete: function () {
                if (this.multipleSelectedId.length == 0) {
                    this.$message({
                        message: '请选择要删除的数据',
                        type: 'warning'
                    });
                    return false;
                }
                var self = this;
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios.get('/sys/backTeacherExpansion/deleteBatchByUser?ids=' + self.multipleSelectedId.join(',') + '&type=1').then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getPage();
                        }
                        self.$message({
                            message: data.status == '1' ? data.msg || '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })
            },

            singleDelete: function (id) {
                var self = this;
                this.$confirm('确认删除?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios.get('/sys/backTeacherExpansion/deleteBatchByUser?ids=' + id + '&type=1').then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getPage();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                    })
                }).catch(function () {

                })
            },

            getPage: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/sys/backTeacherExpansion/provinceTeacherList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.data) {
                        var page = data.data || {};
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        self.pageList = page.list || [];
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
            },

            handleSelectionChange: function (val) {
                this.multipleSelection = val;
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].user.id);
                }
            },

            handlePaginationSizeChange: function (val) {
                this.searchListForm.pageSize = val;
                this.getPage()
            },

            handlePaginationPageChange: function () {
                this.getPage()
            },

            handleSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getPage();
            },
            getDictLists: function () {
                var dicts = {
                    'sex': 'sexes',
                    'master_help': 'masterHelps',
                    'technology_field': 'domains',
                    '20190506144023100046': 'postTitles',
                    '20190506142903100035': 'industries',
                    '20190507144620100060': 'recommendedUnitsDict'
                };
                this.getBatchDictList(dicts)
            },
        },
        mounted: function () {
            this.getPage();
            this.getDictLists();
        }
    })


</script>

</body>
</html>