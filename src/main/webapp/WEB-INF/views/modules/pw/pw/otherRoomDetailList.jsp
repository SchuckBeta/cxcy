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


<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">

            <e-condition label="学院" type="radio" v-model="searchListForm.localCollege" :default-props="defaultProps"
                         name="localCollege" :options="collegeList" @change="getDataList">
            </e-condition>

        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-input">
                <el-select size="mini" v-model="searchListForm.condition" placeholder="请选择查询条件"
                           @change="handleChangeCondition" style="width:135px;">
                    <el-option
                            v-for="item in conditions"
                            :key="item.id"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker :disabled="!searchListForm.condition"
                                v-model="applyDate"
                                type="datetimerange"
                                size="mini"
                                align="right"
                                @change="handleChangeDate"
                                unlink-panels
                                range-separator="至"
                                start-placeholder="开始时间"
                                end-placeholder="结束时间"
                                value-format="yyyy-MM-dd HH:mm"
                                format="yyyy-MM-dd HH:mm"
                                style="width:280px;">
                </el-date-picker>
                <input type="text" style="display:none">
                <el-input name="queryStr" size="mini" class="w300" v-model="searchListForm.queryStr"
                          placeholder="姓名/学号/卡号" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                   @sort-change="handleTableSortChange">
            <el-table-column label="学生信息" align="left">
                <template slot-scope="scope">
                    <div>
                        <el-tooltip :content="scope.row.no" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.no}}</span>
                        </el-tooltip>
                    </div>
                    <div>
                        <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.name}}</span>
                        </el-tooltip>
                    </div>
                    <div>
                        <el-tooltip :content="scope.row.academy + '/' + scope.row.major" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.academy}}/{{scope.row.major}}</span>
                        </el-tooltip>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="mobile" label="联系方式" align="center">
            </el-table-column>
            <el-table-column prop="cardId" label="卡号" align="center">
            </el-table-column>
            <el-table-column prop="enterDate" label="进入时间" align="center" sortable="enterDate">
                <template slot-scope="scope">
                    {{scope.row.enterDate}} {{scope.row.enterTime}}
                </template>
            </el-table-column>
            <el-table-column prop="exitDate" label="退出时间" align="center" sortable="exitDate">
                <template slot-scope="scope">
                    {{scope.row.exitDate}} {{scope.row.exitTime}}
                </template>
            </el-table-column>
            <el-table-column prop="state" label="状态" align="center" sortable="state">
                <template slot-scope="scope">
                    <span>{{scope.row.state | selectedFilter(statesEntries)}}</span>
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
            var professionals = JSON.parse('${fns:getOfficeListJson()}') || [];
            return {
                professionals: professionals,
                states:[
                    {
                        id:'111',
                        label:'已退出',
                        value:'1'
                    },
                    {
                        id:'222',
                        label:'已进入',
                        value:'2'
                    }
                ],
                conditions: [
                    {
                        id: '111',
                        label: '进入日期范围',
                        value: '1'
                    },
                    {
                        id: '222',
                        label: '退出日期范围',
                        value: '2'
                    }
                ],
                pageCount: 0,
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    localCollege: '',
                    condition:'',
                    enterDate: '',
                    exitDate: '',
                    enterTime:'',
                    exitTime:'',
                    queryStr: ''
                },
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                loading: false,
                applyDate: [],

                pageList: [
                    {
                        id: '111',
                        no: '1450340007',
                        name: '程小凤',
                        mobile: '15071485960',
                        academy: '工商管理学院',
                        major:'电子商务专业',
                        cardId: '620035552123520',
                        enterDate:'2017-01-26',
                        enterTime:'10:10',
                        exitDate:'2017-01-26',
                        exitTime:'12:30',
                        state:'1'
                    },
                    {
                        id: '222',
                        no: '1450340007',
                        name: '程小凤',
                        mobile: '15071485960',
                        academy: '工商管理学院',
                        major:'电子商务专业',
                        cardId: '620035552123520',
                        enterDate:'2017-01-26',
                        enterTime:'10:10',
                        exitDate:'2017-01-26',
                        exitTime:'12:30',
                        state:'2'
                    },
                    {
                        id: '333',
                        no: '1450340007',
                        name: '程小凤',
                        mobile: '15071485960',
                        academy: '工商管理学院',
                        major:'电子商务专业',
                        cardId: '620035552123520',
                        enterDate:'2017-01-26',
                        enterTime:'10:10',
                        exitDate:'2017-01-26',
                        exitTime:'12:30',
                        state:'1'
                    }
                ]
            }
        },
        computed: {
            collegeList: {
                get: function () {
                    return this.professionals.filter(function (item) {
                        return item.grade == '2';
                    })
                }
            },
            statesEntries: function () {
                return this.getEntries(this.states);
            }
        },
        methods: {
            getDataList: function () {
//                var self = this;
//                this.loading = true;
//                this.$axios({
//                    method: 'POST',
//                    url: '/pw/ajaxListXQRZ?' + Object.toURLSearchParams(this.searchListForm)
//                }).then(function (response) {
//                    var data = response.data;
//                    if (data.status == '1') {
//                        self.pageCount = data.data.count;
//                        self.searchListForm.pageSize = data.data.pageSize;
//                        self.pageList = data.data.list || [];
//                    }
//                    self.loading = false;
//                }).catch(function () {
//                    self.loading = false;
//                    self.$message({
//                        message: '请求失败',
//                        type: 'error'
//                    })
//                });
            },
            handleChangeCondition: function () {
                if (this.searchListForm.endDate) {
                    this.getDataList();
                }
            },
            handleChangeDate: function (value) {
                value = value || [];
                this.searchListForm.enterDate = moment(value[0]).format('YYYY-MM-DD');
                this.searchListForm.exitDate = moment(value[1]).format('YYYY-MM-DD');
                this.searchListForm.enterTime = moment(value[0]).format('HH:mm:ss');
                this.searchListForm.exitTime = moment(value[1]).format('HH:mm:ss');
//                this.getDataList();
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc' > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
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
//            this.getDataList();
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