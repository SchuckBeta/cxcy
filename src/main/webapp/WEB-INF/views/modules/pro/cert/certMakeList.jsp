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
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" size="mini" autocomplete="off" ref="searchListForm">
        <div class="conditions">
            <e-condition v-if="certList.length > 0" type="radio" :is-show-all="false" v-model="searchListForm.certid"
                         label="证书"
                         :options="certList"
                         :default-props="{label: 'name', value: 'id'}"
            ></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-tooltip v-if="certList.length > 0" class="item" popper-class="white" effect="dark" content="请先选择证书"
                            :disabled="!!searchListForm.certid"
                            placement="top">
                    <span> <el-button :disabled="!searchListForm.certid" type="primary" size="mini"
                                      @click.stop="certMake">下发证书</el-button>
                    </span></el-tooltip>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="pageList" size="mini" class="table">
            <el-table-column label="证书" prop="certname"></el-table-column>
            <el-table-column label="开始下发时间" prop="createDate" align="center">
                <template slot-scope="scope">
                    {{scope.row.createDate | formatDateFilter('YYYY-MM-DD HH:mm:ss')}}
                </template>
            </el-table-column>
            <el-table-column label="总数" prop="total" align="center"></el-table-column>
            <el-table-column label="成功数" prop="success" align="center"></el-table-column>
            <el-table-column label="失败数" prop="fail" align="center"></el-table-column>
            <el-table-column label="状态" prop="isComplete" align="center">
                <template slot-scope="scope">
                    <span v-if="scope.row.isComplete == '0'">下发中</span>
                    <span v-else>
                        <template v-if="!scope.row.errmsg">下发完毕</template>
                         <template v-else>下发失败</template>
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="错误信息" prop="errmsg" align="center">
                <template slot-scope="scope">
                    {{scope.row.errmsg}}
                </template>
            </el-table-column>
            <shiro:hasPermission name="cert:sysCert:edit">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" :disabled="scope.row.isComplete != 1"
                                   @click.stop="confirmDel(scope.row)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </shiro:hasPermission>
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
                    layout="total,prev, pager, next, sizes"
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
            var certList = JSON.parse(JSON.stringify(${fns: toJson(certList)})) || [];
            var pageData = JSON.parse(JSON.stringify(${fns: toJson(page)}));
            var pageNo = pageData.pageNo || 1;
            var pageSize = pageData.pageSize || 10;
            var pageCount = pageData.count || 0;
            var pageList = pageData.list;
            var certObj = {};
            var nCertList = [];
            certList.forEach(function (item) {
                if(!certObj[item.id]){
                    certObj[item.id] = true;
                    nCertList.push(item);
                }
            });
            return {
                certList: nCertList || [],
                certId: '',
                searchListForm: {
                    certid: '',
                    actywId: '${actywId}',
                    pageNo: pageNo,
                    pageSize: pageSize,
                    referrer: '${referer}'
                },
                pageCount: pageCount,
                pageList: pageList,
                timeouts: {}
            }
        },
        computed: {
            certParams: function () {
                var searchListForm = this.searchListForm;
                return {
                    certid: searchListForm.certid,
                    actywId: searchListForm.actywId
                }
            }
        },
        methods: {
            certMake: function () {
                var self = this;
                this.$axios.get('/certMake/doCertMake?' + Object.toURLSearchParams(this.certParams)).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.searchListForm.pageNo = 1;
                        self.getCertMakeList();
                    }else {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            },
            getCertMakeList: function () {
                var self = this;
                this.$axios.get('/certMake/getCertMakeList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data || {};
                        var pageList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                        self.pageList = pageList;
                        self.pageList.forEach(function (item) {
                            if (item.isComplete == 0) {
                                if (!self.timeouts[item.id]) {
                                    self.timeouts[item.id] = setTimeout(function () {
                                        self.getInfoSub(item.id)
                                    }, 2000)
                                }
                            }
                        })
                    }
                })
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getCertMakeList();
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getCertMakeList();
            },

            getInfoSub: function (id) {
                var self = this;
                this.$axios.get('/certMake/getCertMakeInfo?id=' + id).then(function (response) {
                    var data = response.data;
                    if (data) {
                        self.getCertData(id, data);
                    } else {
                        if (self.timeouts[id]) {
                            clearTimeout(self.timeouts[id]);
                        }
                    }
                })
            },

            getCertData: function (id, data) {
                var i = 0;
                var pageList = this.pageList;
                while (i < pageList.length) {
                    if (id == pageList[i].id) {
                        Object.assign(pageList[i], data);
                        if (this.timeouts[id]) {
                            clearTimeout(this.timeouts[id]);
                        }
                        break;
                    }
                    i++
                }
            },

            confirmDel: function (row) {
                var self = this;
                this.$confirm("确认删除吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                }).then(function () {
                    self.delCert(row);
                }).catch(function () {

                })

            },

            delCert: function (row) {
                var self = this;
                var params = {
                    id: row.id,
                    referrer: '${encodereferrer}'
                }
                this.$axios.get('/certMake/delCert?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message({
                            type: 'success',
                            message: '删除成功'
                        })
                        self.getCertMakeList();
                    } else {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            }
        },
        created: function () {
            var self = this;
            this.pageList.forEach(function (item) {
                if (item.isComplete == 0) {
                    if (!self.timeouts[item.id]) {
                        self.timeouts[item.id] = setTimeout(function () {
                            self.getInfoSub(item.id)
                        }, 2000)
                    }
                }
            })
        }
    })

</script>
</body>
</html>