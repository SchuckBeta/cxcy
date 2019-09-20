<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <el-form size="mini">
        <input type="hidden" name="referrer" :value="searchListForm.referrer">
        <input type="hidden" name="isTrue" :value="searchListForm.isTrue">
        <input type="hidden" name="gnodeId" :value="searchListForm.gnodeId">
        <input type="hidden" name="actywId" :value="searchListForm.actywId">
        <input name="pageNo" type="hidden" :value="searchListForm.pageNo"/>
        <input name="pageSize" type="hidden" :value="searchListForm.pageSize"/>
        <input type="file" ref="proCT" accept=".xls,.xlsx,.zip" name="fileName" multiple="multiple"
               style="display: none" @change="uploadFile">
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button size="mini" type="primary" @click.stop.prevent="downTemplate">下载模板</el-button>
                <el-button size="mini" type="primary" :disabled="isUploading" @click.stop.prevent="uploadProCT">
                    {{isUploading ? '上传文件中...':'上传文件'}}
                </el-button>
                <el-button size="mini" type="default" :disabled="isUploading" @click.stop.prevent="goBack">返回
                </el-button>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="exportFileList" size="mini" class="table" v-loading="loading">
            <el-table-column label="文件名称" prop="filename" align="center"></el-table-column>
            <el-table-column label="导入时间" prop="createDate" align="center"></el-table-column>
            <el-table-column label="数据总数" prop="total" align="center"></el-table-column>
            <el-table-column label="导入成功数" prop="success" align="center"></el-table-column>
            <el-table-column label="导入失败数" prop="fail" align="center"></el-table-column>
            <el-table-column label="状态" prop="isComplete" align="center">
                <template slot-scope="scope">
                    <span  style="color: #E9432D" v-show="scope.row.isComplete == '0'">导入中...</span>
                    <span v-show="scope.row.isComplete == '1'">导入完成</span>
                    <span v-show="scope.row.isComplete == '1' && scope.row.errmsg" >导入失败</span>
                </template>
            </el-table-column>
            <el-table-column label="错误信息" prop="errmsg" align="center">
                <template slot-scope="scope">
                    {{scope.row.errmsg || '-'}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <el-button :disabled="scope.row.isComplete !== '1'" size="mini" type="text"
                               @click.stop.prevent="downloadErrorData(scope)">下载错误数据
                    </el-button>
                    <el-button size="mini" type="text" @click.stop.prevent="deleteProCt(scope.row)">删除</el-button>
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
                    layout="prev, pager, next, sizes"
                    :total="total">
            </el-pagination>
        </div>
    </div>
</div>


<script>
    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            return {
                total: 0,
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    isTrue: '',
                    referrer: '${referrer}'
                },
                exportFileList: [],
                isUploading: false,
                loading: true,
                impDataTimers: {}
            }
        },
        methods: {

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getImpProModelList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getImpProModelList();
            },

            deleteProCt: function (row) {
                var self = this;
                this.$axios.delete('/impdata/deleteDrcardProCt?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.getImpProModelList();
                    }
                    self.show$message({
                        status: data.status,
                        msg: data.msg
                    })
                }).catch(function (error) {
                    self.show$message({
                        status: false,
                        msg: error.response.data
                    })
                })
            },

            goBack: function () {
                location.href = this.searchListForm.referrer;
            },

            downloadErrorData: function (scope) {
                location.href = this.frontOrAdmin + '/impdata/expDrcardData?id=' + scope.row.id;
            },

            downTemplate: function () {
                location.href = this.frontOrAdmin + '/impdata/downDrcardTemplate?1=1&' + Object.toURLSearchParams({
                            isTrue: this.searchListForm.isTrue
                        });
            },

            uploadProCT: function () {
                this.$refs.proCT.click();
            },

            uploadFile: function () {
                var files = this.$refs.proCT.files;
                var formData = new FormData();
                var self = this;
                //不存在文件
                if (!files || files.length < 1) {
                    this.$refs.proCT.files = null;
                    return false;
                }
                this.isUploading = true;
                for (var i = 0; i < files.length; i++) {
                    formData.append('fileName', files[i])
                }

                this.$axios({
                    method: 'POST',
                    url: '/impdata/importDrcardData',
                    params: {
                        actywId: this.searchListForm.actywId,
                        gnodeId: this.searchListForm.gnodeId,
                        hasFile: files.length > 0 ? '1' : '0'
                    },
                    data: formData,
                    onUploadProgress: function (progressEvent) {
                        // Do whatever you want with the native progress event
                    }
                }).then(function (response) {
                    var data = response.data;
                    self.$refs.proCT.files = null;
                    if (data.ret > 0) {
                        self.getImpProModelList();
                    }
                    self.show$message({
                        status: data.ret > 0,
                        msg: data.msg
                    })
                    self.isUploading = false;
                }).catch(function (error) {
                    self.show$message({
                        status: false,
                        msg: error.response.data
                    })
                    self.isUploading = false;
                })

            },

            getImpProModelList: function () {
                var self = this;
                self.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/impdata/getImpDrcardList',
                    params: this.searchListForm
                }).then(function (response) {
                    var data = response.data;
                    var datas;
                    console.info("----------------1");
                    console.info(data);
                    if (data.status) {
                        datas = data.datas;
                        self.setSearchListForm(datas);
                        self.setExportFileList(datas);
                        self.impInfoTimer();
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
            },

            impInfoTimer: function () {
//                this.$axios.get('/impdata/getImpInfo?id='+ scope.)
                var exportFileList = this.exportFileList;
                var self = this;
                for (var i = 0; i < exportFileList.length; i++) {
                    var item = exportFileList[i];
                    var isComplete = item.isComplete != '1';
                    if (isComplete) {
                        this.impDataTimers[item.id] = (function (item) {
                            return setInterval(function () {
                                self.getImpInfo(item)
                            }, 2000)
                        })(item)
                    }
                }
            },

            getImpInfo: function (item) {
                var self = this;
                return this.$axios.get('/impdata/getImpInfo?id=' + item.id).then(function (response) {
                    var data = response.data;
                    if (data.isComplete == '1') {
                        clearInterval(self.impDataTimers[item.id])
                        delete self.impDataTimers[item.id]
                    }
                    Object.assign(item, data)
                }).catch(function () {
                    clearInterval(self.impDataTimers[item.id])
                    delete self.impDataTimers[item.id]
                })

            },

            setSearchListForm: function (data) {
                var page = data.page;
                this.searchListForm.pageNo = page.pageNo;
                this.searchListForm.pageSize = page.pageSize;
                this.searchListForm.referrer = data.referrer;
                this.total = page.count;
            },

            setExportFileList: function (data) {
                this.exportFileList = data.page.list || [];
            }
        },
        created: function () {
            this.getImpProModelList();
        }
    })

</script>

</body>
</html>