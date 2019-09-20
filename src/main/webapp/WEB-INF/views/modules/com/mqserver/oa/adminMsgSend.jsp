<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>


<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 renewal-back-manage">
    <edit-bar>我的消息</edit-bar>

    <el-form :model="searchListForm" ref="searchListForm" size="mini">

        <div class="list-type-tab" style="margin-top: 20px;">
            <div class="tab-cell" @click.stop.prevent="goRec">
                <span>接收消息</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell active" @click.stop.prevent="goSend">
                <span>发送消息</span>
                <div class="arrow-right"></div>
            </div>
        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-btns">
                <el-button size="mini" type="primary" :disabled="multipleSelectedId.length == 0" @click.stop.prevent="batchDelete(multipleSelectedId)">
                    <i class="iconfont icon-delete"></i>批量删除
                </el-button>
            </div>
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input name="title" size="mini" class="w300" v-model="searchListForm.title" maxlength="200"
                          placeholder="标题" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column prop="title" label="标题" align="center">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.title" popper-class="white" placement="right">
                        <span class="break-ellipsis underline-pointer"
                              @click.stop.prevent="showNotifyDetail(scope.row)">
                            <i v-if="scope.row.readFlag == '1'" class="iconfont icon-read"></i>
                            <i v-else class="iconfont icon-noread"></i>
                            {{scope.row.title}}
                        </span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="type" label="消息类型" align="center">
                <template slot-scope="scope">
                    {{scope.row.type | selectedFilter(msgTypesEntries)}}
                </template>
            </el-table-column>
            <%--<el-table-column prop="readFlag" label="消息状态" align="center">--%>
                <%--<template slot-scope="scope">--%>
                    <%--<span v-if="scope.row.readFlag == 1">已读</span>--%>
                    <%--<span v-else>未读</span>--%>
                <%--</template>--%>
            <%--</el-table-column>--%>
            <el-table-column label="发送人" align="center">
                <template slot-scope="scope">
                    {{scope.row.createUser ? scope.row.createUser.name : ''}}
                </template>
            </el-table-column>
            <el-table-column prop="effectiveDate" label="发布时间" align="center">
                <template slot-scope="scope">
                    {{scope.row.effectiveDate | formatDateFilter('YYYY-MM-DD hh:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini" @click.stop.prevent="batchDelete([{id:scope.row.id}])">删除
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
    <el-dialog
            title="通知消息"
            :visible.sync="dialogVisibleNotifyDetail"
            :close-on-click-modal="false"
            :show-close="false"
            width="520px"
            :before-close="handleCloseNotifyDetail">
        <div class="notify-dialog-detail">
            <p class="title">{{notifyDetail.title}}</p>
            <div class="publishDate">{{notifyDetail.publishDate}}</div>
            <%--<template v-if="notifyDetailHasAccept">--%>
                <div class="ndd-content" v-html="notifyDetail.content"></div>
            <%--</template>--%>
            <%--<template v-else>--%>
                <%--<div class="ndd-content">--%>
                    <%--<template v-if="notifyDetail.type == '7'">{{notifyDetail.content}}</template>--%>
                    <%--<template v-else>--%>
                        <%--{{notifyDetail.content}}--%>
                    <%--</template>--%>
                <%--</div>--%>
            <%--</template>--%>
        </div>
        <span slot="footer" class="dialog-footer">
        <%--<el-button size="mini" @click="dialogVisibleNotifyDetail = false">取 消</el-button>--%>
        <el-button size="mini" type="primary" @click="handleCloseNotifyDetail">确 定</el-button>
      </span>
    </el-dialog>


</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            return {
                msgTypes: [],
                pageCount: 0,
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    title: ''
                },
                loading: false,
                pageList: [],
                multipleSelection: [],
                multipleSelectedId: [],
                dialogVisibleNotifyDetail: false,
                notifyDetail: {}
            }
        },
        computed: {
            msgTypesEntries: function () {
                return this.getEntries(this.msgTypes);
            }
        },
        methods: {
            showNotifyDetail: function (row) {
                this.getNotifyView(row);
                this.dialogVisibleNotifyDetail = true;
            },
            handleCloseNotifyDetail: function () {
                this.getDataList();
                this.dialogVisibleNotifyDetail = false;
            },
            getNotifyView: function (row) {
                var id = row.id;
                var self = this;
                this.$axios.get('/oa/oaNotify/viewMsg?oaNotifyId=' + id).then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.notifyDetail = data;
                    } else {
                        self.$message({
                            type: 'error',
                            message: self.xhrErrorMsg
                        });
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    });
                })
            },
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/oa/oaNotify/getMsgSendList',
                    params: Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                });
            },
            batchDelete: function (arr) {
                var self = this;
                this.$confirm('确定删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'POST',
                        url: '/oa/oaNotify/deleteSendMsg',
                        data: arr
                    }).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })
            },

            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push({id: value[i].id});
                }
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            goRec: function () {
                window.location.href = this.frontOrAdmin + '/oa/oaNotify/msgRecList';
            },
            goSend: function () {
                window.location.href = this.frontOrAdmin + '/oa/oaNotify/msgSendList';
            },
            getDictListNotifyTypes: function () {
                var self = this;
                this.$axios.get('/oa/oaNotify/ajaxOaNotifyTypes?isAll=true').then(function (response) {
                    var data = response.data;
                    self.msgTypes = JSON.parse(data.data) || [];
                }).catch(function (error) {

                })
            }

        },
        created: function () {
            this.getDictListNotifyTypes();
            this.getDataList();
        }
    })
</script>


</body>
</html>