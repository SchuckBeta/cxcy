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

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60 user-share">
    <edit-bar second-name="查看专家任务"></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">

            <e-condition label="所属学院" type="radio" v-model="searchListForm.officeId" :default-props="{label:'name',value:'id'}"
                         name="officeId" :options="collegeList" @change="getDataList">
            </e-condition>

        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-btns">
                <el-button type="primary" size="mini" @click.stop.prevent="informExpert">通知专家
                </el-button>
            </div>
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input @keyup.enter.native="getDataList"
                          name="queryStr"
                          placeholder="专家姓名/工号"
                          v-model="searchListForm.queryStr"
                          size="mini"
                          class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @sort-change="handleTableSortChange" @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="50">
            </el-table-column>
            <el-table-column prop="u.no" label="专家信息" align="left" min-width="150" sortable="u.no">
                <template slot-scope="scope">
                    <div class="user-element">
                        <div class="user-img">
                            <img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" alt="">
                        </div>
                        <div class="user-detail">
                            <div class="user-name">
                                <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                                    <span class="break-ellipsis">
                                        <img src="${ctxImages}/user-name.png" alt="">
                                        {{scope.row.user.name}}
                                    </span>
                                </el-tooltip>
                            </div>
                            <div class="user-tag">
                                <el-tooltip :content="scope.row.user.no" popper-class="white" placement="right">
                                    <span class="break-ellipsis">
                                        <img src="${ctxImages}/user-no.png" alt="">
                                        {{scope.row.user.no}}
                                    </span>
                                </el-tooltip>
                            </div>
                            <div class="user-phone">
                                <el-tooltip :content="scope.row.user.officeName" popper-class="white"
                                            placement="right">
                                    <span class="break-ellipsis">
                                        <i class="iconfont icon-dibiao2"
                                           style="width:20px;vertical-align: bottom;display: inline-block;text-align: center;"></i>
                                        {{scope.row.user.officeName}}
                                    </span>
                                </el-tooltip>
                            </div>
                        </div>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="reNum" label="分配的项目数" align="center" min-width="150">
                <template slot-scope="scope">
                    <expert-col-re-num :row="scope.row" :actyw-id="searchListForm.actywId" :gnode-id="searchListForm.gnodeId" v-if="!scope.row.reNum" @change="handleChangeRow"></expert-col-re-num>
                    <span v-else>{{scope.row.reNum}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="hasNum" label="已审核项目数" align="center" min-width="150">
            </el-table-column>
            <el-table-column prop="todoNum" label="待审核项目数" align="center" min-width="150">
                <template slot-scope="scope">
                    <span :class="{red:scope.row.todoNum > 0}">{{scope.row.todoNum}}</span>
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
    new Vue({
        el: '#app',
        data: function () {
            var professionals = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            var actYwEtAssignRule = JSON.parse(JSON.stringify(${fns:toJson(actYwEtAssignRule)}));
            return {
                professionals: professionals,
                pageCount: 0,
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    officeId: '',
                    queryStr: '',
                    actywId: actYwEtAssignRule.actywId || '',
                    gnodeId: actYwEtAssignRule.gnodeId || ''
                },
                loading: false,
                pageList: [],
                multipleSelectedId:[]
            }
        },
        computed: {
            collegeList: {
                get: function () {
                    return this.professionals.filter(function (item) {
                        return item.grade == '2';
                    })
                }
            }
        },
        methods: {
            handleChangeRow:function (scope) {
                this.assignFormData(scope.row,scope.data);
                scope.row.reNum = (parseInt(scope.data.hasNum) + parseInt(scope.data.todoNum)).toString();
            },
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.post('/actyw/actywAssignRule/ajaxGetExpertAssginNumList',Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        var list = data.data.list || [];
                        self.pageList = list.map(function (item) {
                            item.todoNum = '';
                            item.reNum = '';
                            item.hasNum = '';
                            return item;
                        })
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    })
                });
            },

            informExpert: function () {
                var self = this;
                if (this.multipleSelectedId.length == 0) {
                    this.$message({
                        message: '请选择需要通知的专家！',
                        type: 'warning'
                    });
                    return false;
                }
                this.$confirm('确认通知?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({
                        method: 'GET',
                        url: '/sys/backTeacherExpansion/sendMsgToExpert',
                        params: {
                            ids: self.multipleSelectedId.join(',')
                        }
                    }).then(function (response) {
                        var data = response.data;
                        self.$message({
                            message: data.status == '1' ? '通知成功' : data.msg || '通知失败',
                            type: data.status == '1' ? 'success' : 'error'
                        })
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'warning'
                        });
                    })
                }).catch(function () {
                    
                })
            },

            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            handleSelectionChange: function (val) {
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].user.id);
                }
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
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })
</script>


</body>
</html>