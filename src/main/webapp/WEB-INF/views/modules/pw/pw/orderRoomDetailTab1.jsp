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
    <edit-bar second-name="预约详情" href="/pw/pwRoom/orderRoomList"></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">

            <e-condition label="学院" type="radio" v-model="searchListForm['office.id']" :default-props="defaultProps"
                         name="officeId" :options="collegeList" @change="getDataList">
            </e-condition>

        </div>

        <div class="list-type-tab">
            <div class="tab-cell active" @click.stop.prevent="goCurrent">
                <span>当前预约记录</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell" @click.stop.prevent="goHistory">
                <span>历史预约记录</span>
                <div class="arrow-right"></div>
            </div>
        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-input">
                <el-date-picker
                                v-model="applyDate"
                                type="datetimerange"
                                size="mini"
                                align="right"
                                @change="handleChangeDate"
                                unlink-panels
                                range-separator="至"
                                start-placeholder="开始预约时间"
                                end-placeholder="结束预约时间"
                                value-format="yyyy-MM-dd HH:mm"
                                format="yyyy-MM-dd HH:mm"
                                :default-time="['08:00:00', '17:00:00']"
                                style="width:280px;">
                </el-date-picker>
                <input type="text" style="display:none">
                <el-input name="keys" size="mini" class="w300" v-model="searchListForm.keys"
                          placeholder="学号/姓名/手机号/专业/团队/企业/用途" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                   @sort-change="handleTableSortChange">

            <el-table-column label="预约人" align="left">
                <template slot-scope="scope">
                    <div>
                        <el-tooltip :content="scope.row.user.no" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.user.no || ''}}</span>
                        </el-tooltip>
                    </div>
                    <div>
                        <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.user.name || ''}}</span>
                        </el-tooltip>
                    </div>
                    <div>
                        <el-tooltip :content="scope.row.office.name + '/' + (scope.row.user.professional && officeEntries[scope.row.user.professional] ? officeEntries[scope.row.user.professional] : '-')" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.office.name}}/{{scope.row.user.professional | selectedFilter(officeEntries)}}</span>
                        </el-tooltip>
                    </div>
                </template>
            </el-table-column>
            <el-table-column prop="user.mobile" label="联系方式" align="center">
            </el-table-column>
            <el-table-column prop="appointmentstyle" label="预约形式" align="center" sortable="appointmentstyle">
                <template slot-scope="scope">
                    {{scope.row.appointmentstyle | selectedFilter(appointmentStylesEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="预约时段" align="center" sortable="startDate">
                <template slot-scope="scope">
                    <div>{{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}</div>
                    <div>{{scope.row.startDate | formatDateFilter('HH:mm')}}-{{scope.row.endDate | formatDateFilter('HH:mm')}}</div>
                </template>
            </el-table-column>
            <el-table-column prop="subject" label="用途" align="center">
            </el-table-column>
            <el-table-column prop="querystatus" label="状态" align="center" sortable="querystatus">
                <template slot-scope="scope">
                    <span :class="{red:scope.row.querystatus == '11'}">{{scope.row.querystatus | selectedFilter(roomStatesEntries)}}</span>
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
            var professionals = JSON.parse(JSON.stringify(${fns:getOfficeListJson()})) || [];
            var rid = '${rid}';
            return {
                professionals: professionals,
                rid:rid,
                roomStates: [],
                appointmentStyles:[
                    {
                        label:'个人',
                        value:'1'
                    },
                    {
                        label:'团队',
                        value:'2'
                    },
                    {
                        label:'企业',
                        value:'3'
                    }
                ],
                pageCount: 0,
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    rid:rid || '',
                    queryType:'1',
                    'office.id': '',
                    dateFrom:'',
                    dateTo:'',
                    timeFrom:'',
                    timeTo:'',
                    keys: ''
                },
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                loading: false,
                applyDate: [],

                pageList: []
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
            officeEntries: function () {
                return this.getEntries(this.professionals, {label: 'name', value: 'id'})
            },
            statesEntries: function () {
                return this.getEntries(this.states);
            },
            appointmentStylesEntries:function () {
                return this.getEntries(this.appointmentStyles);
            },
            roomStatesEntries: function () {
                return this.getEntries(this.roomStates,{label:'statusname',value:'status'});
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwRoom/roomOrderDetailList',
                    params:Object.toURLSearchParams(this.searchListForm)
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
            handleChangeDate: function (value) {
                value = value || [];
                if(value.length == 0){
                    this.searchListForm.dateFrom = '';
                    this.searchListForm.dateTo = '';
                    this.searchListForm.timeFrom = '';
                    this.searchListForm.timeTo = '';
                }else{
                    this.searchListForm.dateFrom = moment(value[0]).format('YYYY-MM-DD');
                    this.searchListForm.dateTo = moment(value[1]).format('YYYY-MM-DD');
                    this.searchListForm.timeFrom = moment(value[0]).format('HH:mm');
                    this.searchListForm.timeTo = moment(value[1]).format('HH:mm');
                }
                this.getDataList();
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
            goCurrent: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwRoom/orderRoomDetailTab1?rid=' + this.rid;
            },
            goHistory: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwRoom/orderRoomDetailTab2?rid=' + this.rid;
            },
            getRoomStates:function () {
                var self = this;
                this.$axios.get('/pw/pwRoom/roomUseStatusList').then(function (response) {
                    var data = response.data;
                    self.roomStates = data.data || [];
                })
            }

        },
        created: function () {
            this.getRoomStates();
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