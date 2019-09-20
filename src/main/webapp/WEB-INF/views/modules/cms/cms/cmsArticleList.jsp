<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="utf-8"/>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm.publishStatus" label="发布状态"
                         :options="publishStatues" @change="getBaseContentList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" :disabled="multipleSelectionBSList.length < 1" size="mini"
                           @click.stop.prevent="updatePublishStatusBatch(1)">发布
                </el-button>
                <el-button type="primary" :disabled="multipleSelectionBSList.length < 1" size="mini"
                           @click.stop.prevent="updatePublishStatusBatch(0)">取消发布
                </el-button>
                <el-tooltip class="item" content="请选择需要置顶的文章" :disabled="multipleSelectionBSList.length > 0"
                            popper-class="white" placement="bottom">
                        <span>
                <el-button icon="el-icon-arrow-up" type="primary" :disabled="multipleSelectionBSList.length < 1" size="mini"
                           @click.stop.prevent="updateTopBatch">置顶
                </el-button>
                            </span>
                    </el-tooltip>

                <el-button icon="el-icon-circle-plus" type="primary" size="mini" @click.stop.prevent="goToAdd">添加
                </el-button>

                <el-tooltip class="item" content="请选择需要删除的文章" :disabled="multipleSelectionBSList.length > 0"
                            popper-class="white" placement="bottom">
                        <span>
                <el-button icon="el-icon-delete" type="primary" :disabled="multipleSelectionBSList.length < 1" size="mini"
                           @click.stop.prevent="confirmDelBatchArticle">删除
                </el-button>
                                        </span>
                </el-tooltip>

            </div>
            <div class="search-input">
                <el-input
                        placeholder="标题"
                        size="mini"
                        name="title"
                        v-model="searchListForm.title"
                        @change="getBaseContentList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
        </div>
    </div>
    <div class="table-container" v-loading="tableLoading">
        <el-table :data="baseContentList" size="small" class="table" @selection-change="handleSelectionBCList">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="标题" min-width="100">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.title" popper-class="white" placement="right">
                        <span class="break-ellipsis">
                            <span class="red" v-show="scope.row.top == 1">【置顶】</span>
                            {{scope.row.title}}
                        </span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="置顶" align="center" min-width="50">
                <template slot-scope="scope">
                    <el-switch v-model="scope.row.top" active-value="1" inactive-value="0" size="mini"
                               @change="handleChangeTop(scope.row)"></el-switch>
                </template>
            </el-table-column>
            <el-table-column label="栏目名称" align="center" min-width="70">
                <template slot-scope="scope">
                    {{scope.row.cmsCategory.name}}
                </template>
            </el-table-column>

            <el-table-column label="状态" align="center" min-width="50">
                <template slot-scope="scope">
                    {{scope.row.publishStatus | selectedFilter(publishStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="浏览量" align="center" min-width="60">
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
            <el-table-column label="操作" align="center" min-width="90">
                <template slot-scope="scope">
                        <%--<el-button type="text" size="mini" @click.stop.prevent="goToVisit(scope.row)"></el-button>--%>

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
                                           @click.stop.prevent="updatePublishStatusSingle(scope.row)">
                                </el-button>
                            </span>
                        </el-tooltip>
                        <el-tooltip v-show="scope.row.publishStatus == '1'" class="item" content="取消发布" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-quxiaofabu1"
                                           @click.stop.prevent="updatePublishStatusSingle(scope.row)">
                                </el-button>
                            </span>
                        </el-tooltip>

                        <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                           @click.stop.prevent="goToEdit(scope.row.id)"></el-button>
                            </span>
                        </el-tooltip>
                        <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                            <span>
                                <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                           @click.stop.prevent="confirmDelSingleArticle(scope.row.id)">

                                </el-button>
                            </span>
                        </el-tooltip>
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
            var cmsModuleList = JSON.parse('${fns: toJson(fns: getDictList('0000000274'))}');
            var publishStatues = JSON.parse('${fns: toJson(fns: getDictList('0000000279'))}');
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    title: '',
                    publishStartdate: '', //有效开始时间
                    publishEnddate: '', //有效结束时间
                    articlepulishStartDate: '', //发布有效期
                    articlepulishEndDate: '', //发布有效期 实例2018-09-10 10:00:00
                    publishStatus: '',
                },
                pageCount: 0,
                baseContentList: [],
                multipleSelectionBSList: [],
                tableLoading: false,
                cmsModuleList: cmsModuleList,
                publishStatues: publishStatues,
                dateTypes: [{label: '发布时间', value: 'articlepulishDate'}, {label: '有效期', value: 'publishStartdate'}],
                dateType: '',
                dateQuery: []
            }
        },

        computed: {
            cmsModuleEntries: {
                get: function () {
                    return this.getEntries(this.cmsModuleList)
                }
            },
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

            goToVisit: function (row) {
                window.open('/f/getOneCmsArticle?id=')
            },
            //改变时间
            handleChangeDateQuery: function (value) {
                var dateType = this.dateType;
                if (!value) {
                    this.searchListForm.publishStartdate = '';
                    this.searchListForm.publishEnddate = '';
                    this.searchListForm.articlepulishStartDate = '';
                    this.searchListForm.articlepulishEndDate = '';
                } else {
                    if (dateType === 'articlepulishDate') {
                        this.searchListForm.articlepulishStartDate = moment(value[0]).format('YYYY-MM-DD');
                        this.searchListForm.articlepulishEndDate = moment(value[1]).format('YYYY-MM-DD');
                    } else if (dateType === 'publishStartdate') {
                        this.searchListForm.publishStartdate = moment(value[0]).format('YYYY-MM-DD');
                        this.searchListForm.publishEnddate = moment(value[1]).format('YYYY-MM-DD');
                    }
                }
                this.getBaseContentList();
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getBaseContentList();
            },

            handlePCPChange: function () {
                this.getBaseContentList();
            },

            handleSelectionBCList: function (value) {
                this.multipleSelectionBSList = value
            },

            handleChangeDateType: function (value) {
                this.dateQuery = [];
                this.handleChangeDateQuery();
            },


            handleChangeTop: function (row) {
                this.updateTopSingle(row)
            },


            updatePublishStatusBatch: function (status) {
                var publishStatus = [], ids = [];
                this.multipleSelectionBSList.forEach(function (item) {
                    ids.push(item.id);
                    publishStatus.push(status)
                });
                this.updateCmsArticleTop({
                    ids: ids,
                    publishstatus: publishStatus,
                    tops: []
                })
            },


            updatePublishStatusSingle: function (row) {
                var params = {
                    ids: [row.id],
                    publishstatus: [row.publishStatus == '1' ? '0' : '1'],
                    tops: []
                };
                this.updateCmsArticleTop(params);
            },

            updateTopBatch: function () {
                var tops = [], ids = [];
                this.multipleSelectionBSList.forEach(function (item) {
                    ids.push(item.id);
                    tops.push(1)
                });
                this.updateCmsArticleTop({
                    ids: ids,
                    tops: tops,
                    publishstatus: []
                })
            },

            updateTopSingle: function (row) {
                var params = {
                    ids: [row.id],
                    tops: [row.top],
                    publishstatus: []
                };
                this.updateCmsArticleTop(params);
            },

            //置顶数据
            updateCmsArticleTop: function (params) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsArticle/updateCmsArticle?ids=' + params.ids.join(',') + '&tops=' + params.tops.join(',') + '&publishstatus=' + params.publishstatus.join(',')
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getBaseContentList()
                        self.$alert('操作成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                }).catch(function (error) {
                })
            },

            //批量
            confirmDelBatchArticle: function () {
                var self = this;
                var multipleSelectionBSList = this.multipleSelectionBSList;
                this.$confirm('此操作会删除选中文章，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var ids = multipleSelectionBSList.map(function (item) {
                        return item.id;
                    });
                    self.deleteCmsArticle(ids);
                }).catch(function () {

                })
            },

            //删除数据
            confirmDelSingleArticle: function (id) {
                var self = this;
                this.$confirm('此操作会删除这篇文章，是否继续？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.deleteCmsArticle([id]);
                }).catch(function () {

                })
            },

            deleteCmsArticle: function (params) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/cms/cmsArticle/deleteCmsArticle?ids=' + params.join(','),
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getBaseContentList()
                        self.$alert('操作成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                }).catch(function (error) {
                })
            },

            //获取表格
            getBaseContentList: function () {
                var self = this;
                this.tableLoading = true;
                this.$axios.get('/cms/cmsArticle/cmsArticleList', {
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data;
                        if (pageData) {
                            self.baseContentList = pageData.list || [];
                            self.pageCount = pageData.count;
                            self.searchListForm.pageSize = pageData.pageSize;
                            self.searchListForm.pageNo = pageData.pageNo;
                        }
                    }
                    self.tableLoading = false;
                }).catch(function (error) {
                    self.tableLoading = false;
                })
            },

            goToEdit: function (id) {
                location.href = this.ctx + '/cms/cmsArticle/form?id=' + id;
            },

            goToAdd: function () {
                location.href = this.ctx + '/cms/cmsArticle/form';
            }
        },
        mounted: function () {
            this.getBaseContentList();
        }
    })
</script>
</body>
</html>