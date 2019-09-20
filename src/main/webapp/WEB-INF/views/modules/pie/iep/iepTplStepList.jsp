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

<div id="app" v-show="pageLoad" style="display: none" class="tem-step-dialog">
    <div style="text-align: center;">
        <div class="tem-step common-step">
            <div v-if="iepTpl.level == '10'" class="tem-step-item" v-for="(item,index) in iepTpl.childrens"
                 :key="item.id">
                <div class="tem-step-arrow" v-if="index != 0">
                    <i :class="{'iconfont':true,'icon-iconset0438':true,'tem-step-arrow-color':item.curr == 0}"></i>
                </div>
                <div class="tem-step-todo">
                    <div v-for="(child,index) in item.childrens">
                        <p class="one-step" v-if="index == '0'" :class="{'one-step-background':item.curr == 0}">第{{item.step}}步：{{child.name}}{{item.name}}</p>
                        <button-import-contest v-if="child.operType == '10'" :child="child" :actyw-id="actywId"
                                               :gnode-id="gnodeId" :level="iepTpl.level" :item="item" :item-curr="item.curr"
                                               @upload-success="importSuccess"></button-import-contest>
                        <el-button size="mini" type="primary" v-if="child.operType == '20'" style="margin-bottom: 10px;"
                                   @click.stop.prevent="exportFile(child.id)">{{child.name}}{{item.name}}
                        </el-button>
                        <p v-if="child.operType == '30'" class="download-tem"
                           @click.stop.prevent="downloadFile(item.id)">
                            {{child.name}}</p>
                    </div>
                </div>
            </div>

        </div>
    </div>


    <div v-if="iepTpl.level == '20'" class="tem-one-item common-step">
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <span v-for="item in iepTpl.childrens" :key="item.id">
                    <el-button v-if="item.operType == '30'" size="mini" type="primary"
                               @click.stop.prevent="downloadFile(item.id)">{{item.name}}</el-button>

                    <button-import-contest v-if="item.operType == '10'" :child="item" :actyw-id="actywId" :level="iepTpl.level"
                                           :gnode-id="gnodeId" @upload-success="getDataList"></button-import-contest>

                    <el-button v-if="item.operType == '20'" size="mini" type="primary"
                               @click.stop.prevent="exportFile(item.id)">{{item.name}}
                    </el-button>
                </span>
            </div>
        </div>
    </div>

    <div class="tem-data">
        <div class="tem-data-title">
            <span>查看导入结果<i :class="{'el-icon-caret-top':isShowDataList,'el-icon-caret-bottom':!isShowDataList}"
                           @click.stop.prevent="isShowDataList = !isShowDataList"></i></span>
            <hr>
        </div>
        <div class="table-container" v-show="isShowDataList" style="box-shadow: none">
            <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                      max-height="263">
                <el-table-column prop="filename" label="数据类型" align="left" min-width="100">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.filename" popper-class="white"
                                    placement="right">
                            <span class="break-ellipsis">{{scope.row.filename}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column prop="createDate" label="导入时间" align="center" min-width="150">
                    <template slot-scope="scope">
                        {{scope.row.createDate}}
                    </template>
                </el-table-column>
                <el-table-column prop="total" label="数据总数" align="center" min-width="80">
                    <template slot-scope="scope">
                        {{scope.row.total}}
                    </template>
                </el-table-column>
                <el-table-column prop="todoNum" label="导入成功数" align="center" min-width="100">
                    <template slot-scope="scope">
                        {{scope.row.success}}
                    </template>
                </el-table-column>
                <el-table-column prop="fail" label="导入失败数" align="center" min-width="100">
                    <template slot-scope="scope">
                        {{scope.row.fail}}
                    </template>
                </el-table-column>
                <el-table-column prop="isComplete" label="状态" align="center" min-width="70">
                    <template slot-scope="scope">
                        {{scope.row.isComplete | selectedFilter(isCompleteLabelEntries)}}
                    </template>
                </el-table-column>
                <el-table-column label="操作" align="center" min-width="140">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-tooltip :content="scope.row.errmsg" popper-class="white" v-if="scope.row.errmsg"
                                        placement="left">
                                <el-button type="text" size="mini" @click.stop.prevent="downEdata(scope.row.id)">下载错误数据
                                </el-button>
                            </el-tooltip>
                            <el-button v-else type="text" size="mini" @click.stop.prevent="downEdata(scope.row.id)">下载错误数据
                            </el-button>
                            <el-button type="text" size="mini" @click.stop.prevent="delRecord(scope.row)">删除
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


</div>

<script type="text/javascript">


    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var iepTpl = JSON.parse(JSON.stringify(${fns:toJson(iepTpl)}));
            if (iepTpl.level == '10') {
                iepTpl.childrens.forEach(function (item) {
                    item.childrens.sort(function (a, b) {
                        return parseInt(a.operType) - parseInt(b.operType);
                    });
                    item.childrens.forEach(function (child) {
                        child.isUploading = false;
                    })
                });
            } else if (iepTpl.level == '20') {
                iepTpl.childrens.forEach(function (item) {
                    item.isUploading = false;
                });
            }
            return {
                actywId: '${actywId}',
                gnodeId: '${gnodeId}',
                operTypeBtn: '${operType}',
                iepTpl: iepTpl,
                pageList: [],
                loading: false,
                pageCount: 0,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 5
                },
                isShowDataList: true,
                isCompleteLabel: [
                    {label: '完成', value: '1'},
                    {label: '处理中', value: '0'}
                ],
                originalPageList: [],
                heightArr: [],
                timerInterval: null
            }
        },
        computed: {
            isCompleteLabelEntries: function () {
                return this.getEntries(this.isCompleteLabel);
            }
        },
        watch: {
            isShowDataList: function () {
                var bodyHeight = document.body.offsetHeight + 20;
                var stepHeight;
                var heights = [], height;
                $('.common-step').each(function () {
                    heights.push($(this).height() + 70);
                });
                stepHeight = Math.max.apply(null, heights);
                if (this.heightArr.length == 0) {
                    height = stepHeight;
                } else if (this.heightArr.length == 1) {
                    height = this.heightArr[0];
                } else if (this.heightArr.length >= 2) {
                    this.heightArr.shift();
                    height = this.heightArr[0];
                }
                window.parent.document.getElementById("modalCommonExpBody").style.height = height + 'px';
                this.heightArr.push(bodyHeight);
            }
        },
        methods: {
            importSuccess:function () {
                this.getDataList();
            },
            getImpExpData:function () {
                var self = this;
                this.$axios({
                    method:'POST',
                    url:'/iep/iepTpl/ajaxIepTpl?id=' + self.iepTpl.id
                }).then(function (response) {
                    var data = response.data;
                    self.iepTpl = data || {};
                    if (self.iepTpl.level == '10') {
                        self.iepTpl.childrens.forEach(function (item) {
                            item.childrens.sort(function (a, b) {
                                return parseInt(a.operType) - parseInt(b.operType);
                            });
                            item.childrens.forEach(function (child) {
                                child.isUploading = false;
                            })
                        });
                    } else if (self.iepTpl.level == '20') {
                        self.iepTpl.childrens.forEach(function (item) {
                            item.isUploading = false;
                        });
                    }
                }).catch(function () {

                })
            },
            exportFile: function (id) {
                <%--location.href = '${ctx}/iep/ie/downEdata?id=' + id;--%>
            },
            downloadFile: function (id) {
                location.href = '${ctx}/iep/ie/downTpl?id=' + id;
            },
            getImpInfoList:function () {
                var self = this;
                var pageList = self.pageList;
                var unCompleteList = pageList.filter(function (item) {
                    return item.isComplete != '1';
                });
                var params = unCompleteList.map(function (item) {
                    return {id:item.id};
                });

                if(unCompleteList.length == 0){
                    this.timerInterval && clearInterval(this.timerInterval);
                    return false;
                }
                this.$axios({
                    method:'POST',
                    url:'/iep/ie/ajaxIepInfo?iepId=' + self.iepTpl.id + '&operType=' + self.operTypeBtn,
                    data:params
                }).then(function (response) {
                    var data = response.data.datas || {};
                    self.pageList.forEach(function (item) {
                        var impInfo = data[item.id];
                        if(impInfo){
                            Object.assign(item, impInfo);
                            item.isComplete = impInfo.isComplete;
                            self.getImpExpData();
                        }
                    });
                });
            },
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'POST',
                    url: '/iep/ie/ajaxIepInfos?id=' + self.iepTpl.id + '&operType=' + self.operTypeBtn
                }).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.originalPageList = data.datas ? JSON.parse(JSON.stringify(data.datas)) : [];
                        self.pageCount = data.datas ? data.datas.length : 0;
                        self.pageList = self.getCurrentPageList();
                        self.dialogAutoHeight();
                    }
                    self.loading = false;
                    self.requestUnComplete();
                }).catch(function () {
                    self.loading = false;
                })
            },
            getCurrentPageList: function () {
                var pageMaxNo = Math.ceil(this.pageCount/this.searchListForm.pageSize) || 1;
                if(this.searchListForm.pageNo > pageMaxNo){
                    this.searchListForm.pageNo = pageMaxNo;
                }
                var list = [], startIndex = this.searchListForm.pageNo * this.searchListForm.pageSize;
                list = this.originalPageList.slice(startIndex - this.searchListForm.pageSize, startIndex);
                return list;
            },
            dialogAutoHeight: function () {
                this.$nextTick(function () {
                    var dialogBody = document.body.offsetHeight + 20;
                    window.parent.document.getElementById("modalCommonExpBody").style.height = dialogBody + 'px';
                });
            },
            requestUnComplete:function () {
                var self = this;
                var unCompleteList = this.pageList.filter(function (item) {
                    return item.isComplete != '1';
                });
                if (unCompleteList.length < 1) {
                    return false;
                }
                this.timerInterval = setInterval(function () {
                    self.getImpInfoList()
                }, 5000);
            },
            downEdata: function (id) {
                location.href = '${ctx}/iep/ie/downEdata?id=' + id+ '&operType=' + this.operTypeBtn;
            },
            delRecord: function (row) {
                var self = this;
                this.$confirm('确认删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delRecordRequest(row)
                }).catch(function () {

                })
            },
            delRecordRequest: function (row) {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/iep/ie/delRecord?id=' + row.id + '&operType=' + self.operTypeBtn + '&iepId=' + row.impTpye
                }).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.getDataList();
                    }
                    self.$message({
                        message: data.status ? '删除成功' : '删除失败',
                        type: data.status ? 'success' : 'error'
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.timerInterval && clearInterval(this.timerInterval);
                this.pageList = this.getCurrentPageList();
                this.dialogAutoHeight();
                this.requestUnComplete();
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.timerInterval && clearInterval(this.timerInterval);
                this.pageList = this.getCurrentPageList();
                this.dialogAutoHeight();
                this.requestUnComplete();
            }

        },
        mounted: function () {
            this.getDataList();
        }
    })

</script>
</body>
</html>