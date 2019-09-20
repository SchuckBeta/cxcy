<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">

    <edit-bar></edit-bar>

    <div class="clearfix">
        <div class="conditions">
            <e-condition label="状态" type="radio" v-model="searchListForm.publishStatus"
                         name="publishStatus" :options="publishStatues" @change="getDataList">
            </e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-tooltip class="item" content="请选择大赛热点排序" :disabled="multipleSelectedId.length > 0"
                            popper-class="white" placement="bottom">
                    <span>
                <el-button icon="el-icon-sort" size="mini" :disabled="tableList.length == 0" type="primary"
                           @click.stop.prevent="saveSorts">
                    保存排序
                </el-button>
                        </span>
                </el-tooltip>
                <el-button icon="el-icon-circle-plus" size="mini" type="primary" @click.stop.prevent="goAdd">添加
                </el-button>
                <el-tooltip class="item" content="请选择需要删除的大赛热点" :disabled="multipleSelectedId.length > 0"
                            popper-class="white" placement="bottom">
                    <span>
                <el-button icon="el-icon-delete" size="mini" type="primary" :disabled="multipleSelectedId.length == 0"
                           @click.stop.prevent="batchDelete">批量删除
                </el-button>
                        </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <el-input
                        placeholder="标题"
                        size="mini"
                        name="queryStr"
                        v-model="searchListForm.queryStr"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container">
        <el-table :data="tableList" size="mini" class="table" ref="tableList" v-loading="loading"
                  @selection-change="handleChangeSelection" @sort-change="handleTableSortChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="标题" prop="title" min-width="100">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.title" popper-class="white" placement="right">
                        <span class="break-ellipsis">
                            <span class="red" v-show="scope.row.posid == 1">【推荐首页】</span>
                            {{scope.row.title}}
                        </span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="置顶" align="center" min-width="50">
                <template slot-scope="scope">
                    <span><el-switch v-model="scope.row.top" active-value="1" inactive-value="0"
                                     @change="toTop(scope.row)"></el-switch></span>
                </template>
            </el-table-column>
            <el-table-column label="排序" align="center" min-width="70">
                <template slot-scope="scope">
                    <el-input size="mini" v-model.number="scope.row.sort" style="width:52px;"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" min-width="50">
                <template slot-scope="scope">
                    {{scope.row.publishStatus | selectedFilter(publishStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="浏览量" prop="views" align="center" min-width="60">
                <template slot-scope="scope">
                    {{scope.row.views}}
                </template>
            </el-table-column>
            <el-table-column label="发布时间" prop="publishStartdate" align="center" min-width="60">
                <template slot-scope="scope">
                    <span v-if="scope.row.publishStatus == '1'">{{scope.row.publishStartdate | formatDateFilter('YYYY-MM-DD')}}</span>
                </template>
            </el-table-column>
            <el-table-column label="发布到期" prop="publishEnddate" align="center" min-width="60">
                <template slot-scope="scope">
                    <span v-if="scope.row.publishStatus == '1' && scope.row.publishhforever == '0'">{{scope.row.publishEnddate | formatDateFilter('YYYY-MM-DD')}}</span>
                    <span v-if="scope.row.publishStatus == '1' && scope.row.publishhforever == '1'">永久</span>
                </template>
            </el-table-column>
            <%--width="220"--%>
            <el-table-column label="操作" align="center" min-width="90">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-tooltip class="item" content="访问" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-dianji"
                                           @click.stop.prevent="visit(scope.row.id)">
                                </el-button>
                            </span>
                        </el-tooltip>
                        <el-tooltip v-show="scope.row.publishStatus == '0'" class="item" content="发布" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-icon-project"
                                           @click.stop.prevent="publishProject(scope.row)">
                                </el-button>
                            </span>
                        </el-tooltip>
                        <el-tooltip v-show="scope.row.publishStatus == '1'" class="item" content="取消发布" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-quxiaofabu1"
                                           @click.stop.prevent="cancelPublishProject(scope.row)">
                                </el-button>
                            </span>
                        </el-tooltip>
                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                           @click.stop.prevent="goChange(scope.row.id)">
                                </el-button>
                            </span>
                            </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                       @click.stop.prevent="deleteLineData(scope.row.id)">
                                </el-button>
                            </span>
                        </el-tooltip>
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
            var publishStatues = JSON.parse('${fns: toJson(fns: getDictList('0000000279'))}');
            return {
                publishStatues: publishStatues,
                searchDateList: [{label: '发布时间', value: '1'}, {label: '到期时间', value: '2'}],
                searchListForm: {
                    publishStatus: '',
                    queryStr: '',
                    pageNo: 1,
                    pageSize: 10,
                    dateType: '',
                    startDate: '',
                    endDate: '',
                    orderBy: '',
                    orderByType: ''
                },
                pageCount: 0,
                publishDate: [],
                multipleSelection: [],
                multipleSelectedId: [],
                multipleSelectedPublishStatus: [],
                tableList: [],
                loading: false
            }
        },
        watch: {
            publishDate: function (value) {
                value = value || [];
                this.searchListForm.startDate = value[0];
                this.searchListForm.endDate = value[1];
                this.getDataList();
            }
        },
        computed: {
            publishStatusEntries: {
                get: function () {
                    return this.getEntries(this.publishStatues)
                }
            }
        },
        methods: {
            visit: function (id) {
                var url = this.showActrel ? '/tf/categoryToArticle/' : '/f/getOneCmsArticle?id=';
                window.open('http://' + this.domain + url + id);
            },

            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/cms/cmsArticle/homeGcontestList', {
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.tableList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },
            handleTableSortChange: function (column, prop, order) {
                this.searchListForm['orderBy'] = order;
                this.searchListForm['orderByType'] = prop;
                this.getDataList();
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            handleChangeSelection: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                this.multipleSelectedPublishStatus = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                    this.multipleSelectedPublishStatus.push(value[i].publishStatus);
                }
            },
            dateTypeChange: function () {
                if (this.searchListForm.dateType && this.publishDate.length > 0) {
//                this.getDataList();
                }
            },
            datePickerChange: function () {
//                this.getDataList();
            },
            axiosRequest: function (url, showMsg, obj) {
                var self = this, path;
                this.loading = true;
                if (obj) {
                    path = {
                        method: 'POST',
                        url: url,
                        data: obj
                    }
                } else {
                    path = {
                        method: 'POST',
                        url: url
                    }
                }
                this.$axios(path).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                        if (showMsg) {
                            self.$message({
                                message: '操作成功',
                                type: 'success'
                            });
                        }
                    } else {
                        self.$message({
                            message: '操作失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            },
            batchPublish: function () {
                var self = this;
                var url = '/cms/cmsArticle/excellent/publishStatus';
                var data = {
                    ids: self.multipleSelectedId.join(','),
                    publishStatus: '1'
                };
                this.$confirm('确定发布吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.axiosRequest(url, true, data);
                }).catch(function () {

                });

            },
            batchCancelPublish: function () {
                var self = this;
                var url = '/cms/cmsArticle/excellent/publishStatus';
                var data = {
                    ids: self.multipleSelectedId.join(','),
                    publishStatus: '0'
                };
                this.$confirm('确定取消发布吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.axiosRequest(url, true, data);
                }).catch(function () {

                });
            },
            batchDelete: function () {
                var self = this;
                var url = '/cms/cmsArticle/excellent/projectDel?ids=' + self.multipleSelectedId.join(',');
                this.$confirm('确定删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.axiosRequest(url, true);
                }).catch(function () {

                });
            },
            saveSort: function (row) {
                var url = '/cms/cmsArticle/homeGcontestSave';
                var data = {
                    id: row.id,
                    sort: row.sort
                };
                this.axiosRequest(url, false, data);
            },

            saveSorts: function () {
                var self = this;
                var reg = /^\+?[1-9][0-9]*$/;
                var flag = true;
                var list = this.tableList.map(function (item) {
                    if (!reg.test(item.sort) && item.sort) {
                        self.$message({
                            message: '排序值必须为正整数，请修改后，再次保存！',
                            type: 'warning'
                        });
                        flag = false;
                        return;
                    }
                    return {id: item.id, sort: item.sort || undefined};
                });
                if (!flag) {
                    return false;
                }
                this.loading = true;
                this.$axios.post('/cms/cmsArticle/excellent/updateSortsArticle', list).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                    }
                    self.$message({
                        message: data.status == '1' ? '操作成功' : '操作失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                    self.loading = false;
                }).catch(function (error) {
                    self.$message({
                        message: '操作失败',
                        type: 'error'
                    });
                })
            },

            toTop: function (row) {
                var url = '/cms/cmsArticle/editTopArticle';
                var data = {
                    id: row.id,
                    top: row.top
                };
                this.axiosRequest(url, false, data);
            },
            publishProject: function (row) {
                var url = '/cms/cmsArticle/excellent/publishStatus';
                var data = {
                    ids: row.id,
                    publishStatus: '1'
                };
                this.axiosRequest(url, true, data);
            },
            cancelPublishProject: function (row) {
                var url = '/cms/cmsArticle/excellent/publishStatus';
                var data = {
                    ids: row.id,
                    publishStatus: '0'
                };
                this.axiosRequest(url, true, data);
            },
            deleteLineData: function (id) {
                var self = this;
                var url = '/cms/cmsArticle/excellent/projectDel?ids=' + id;
                this.$confirm('确认删除该条数据吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.axiosRequest(url, true);
                }).catch(function () {

                })
            },
            goChange: function (id) {
                window.location.href = '${ctx}/cms/cmsArticle/homeGcontestForm?id=' + id;
            },
            goAdd: function () {
                window.location.href = '${ctx}/cms/cmsArticle/homeGcontestForm';
            }
        },
        created: function () {
            this.getDataList();
        }
    })

</script>

</body>
</html>