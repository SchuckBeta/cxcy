<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .flow-reminder-content{
            width: 720px;
        }
        .flow-reminder-content .time-horizontal{
            max-width: 704px;
        }
        .flow-reminder-content .flow-li3:first-child{
            width: 195px;
        }
        .flow-reminder-content .flow-li3:last-child{
            width: 295px;
        }
        .flow-reminder-content .time-horizontal li{
            width: 14%;
        }
        .flow-reminder-content .flow-li3:last-child:after{
            left: 135px;
        }
    </style>

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
                <el-button size="mini" icon="el-icon-sort" :disabled="tableList.length < 1" type="primary" @click.stop.prevent="saveSorts">保存排序
                </el-button>
                <el-button size="mini" type="primary" :disabled="multipleSelectedId.length == 0"
                           @click.stop.prevent="batchPublish">发布
                </el-button>
                <el-button size="mini" type="primary" :disabled="multipleSelectedId.length == 0"
                           @click.stop.prevent="batchCancelPublish">取消发布
                </el-button>
                <el-tooltip class="item" content="请选择需要删除的项目文章" :disabled="multipleSelectedId.length > 0"
                            popper-class="white" placement="bottom">
                        <span>
                <el-button size="mini" type="primary" icon="el-icon-delete" :disabled="multipleSelectedId.length == 0"
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
                    <input type="text" style="display: none">
                    <el-input size="mini" name="sort" v-model="scope.row.sort" style="width:60px;"></el-input>
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" min-width="50">
                <template slot-scope="scope">
                    {{scope.row.publishStatus | selectedFilter(publishStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="浏览量" prop="views" align="center" min-width="60">
                <template slot-scope="scope">
                    {{scope.row.views || '-'}}
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

    <div class="fixed-act-tip">
        <a href="javascript:void(0);" @click.stop.prevent="dialogVisibleAutoDefinedFlow=true">项目文章管理操作指南</a>
    </div>
    <el-dialog
            title="温馨提示"
            :visible.sync="dialogVisibleAutoDefinedFlow"
            :close-on-click-modal="false"
            width="765px"
            :before-close="handleCloseAutoDefinedFlow">
        <div class="flow-reminder-content">
            <ul class="time-horizontal">
                <div>
                    <div class="flow-li3"><label>项目管理系统</label></div>
                    <div class="flow-li3"><label>网站内容管理系统</label></div>
                </div>
                <li><b class="step-line">1</b><span>项目查询</span></li>
                <li><b class="step-line">2</b><span>选择项目</span></li>
                <li><b class="step-line">3</b><span>发布优秀项目</span></li>
                <li><b class="step-line">4</b><span>项目文章管理</span></li>
                <li><b class="step-line">5</b><span>修改文章</span></li>
                <li><b class="step-line">6</b><span>访问</span></li>
                <li><b>7</b><span>发布</span></li>
            </ul>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-checkbox v-model="isRemind">不再提醒</el-checkbox>
        </div>
    </el-dialog>

</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var publishStatues = JSON.parse('${fns: toJson(fns: getDictList('0000000279'))}');
            var isShowStepModal = $.cookie('isShowStepModalProjectArticle');
            var dialogVisibleAutoDefinedFlow = !(isShowStepModal);
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
                loading: false,
                dialogVisibleAutoDefinedFlow:dialogVisibleAutoDefinedFlow,
                isRemind:!!isShowStepModal
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

            handleCloseAutoDefinedFlow:function () {
                this.dialogVisibleAutoDefinedFlow = false;
                if(this.isRemind){
                    $.cookie('isShowStepModalProjectArticle','noMore',{
                        expires:100
                    });
                }else{
                    $.removeCookie('isShowStepModalProjectArticle');
                }
            },

            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/cms/cmsArticle/excellent/projectList',{
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
                    if (data.status === 1) {
                        self.getDataList();
                        if (showMsg) {
                            self.$alert('操作成功', '提示', {
                                type:'success'
                            })
                        }
                    } else {
                        self.$alert(data.msg, '提示', {
                            type:'error'
                        })
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
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

            saveSorts: function () {
                var self = this;
                var reg =  /^\+?[1-9][0-9]*$/;
                var flag = true;
                var list = this.tableList.map(function (item) {
                    if(!reg.test(item.sort) && item.sort){
                        self.$message({
                            message:'排序值必须为正整数，请修改后，再次保存！',
                            type:'warning'
                        });
                        flag = false;
                        return;
                    }
                    return {id:item.id,sort:item.sort || undefined};
                });
                if(!flag){
                    return false;
                }
                this.loading = true;
                this.$axios.post('/cms/cmsArticle/excellent/updateSortsArticle', list).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getDataList();
                        self.$alert('保存成功', '提示', {
                            type:'success'
                        })
                    }else {
                        self.$alert(data.msg, '提示', {
                            type:'error'
                        })
                    }
                    self.loading = false;
                }).catch(function (error) {
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
                window.location.href = '${ctx}/cms/cmsArticle/excellent/projectForm?id=' + id;
            }
        },
        mounted: function () {
            this.getDataList();
        }
    });
</script>

</body>
</html>