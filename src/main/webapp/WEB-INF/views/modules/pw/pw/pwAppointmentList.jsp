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

            <e-condition label="房间类型" type="radio" v-model="searchListForm['pwRoom.type']"
                         name="type" :options="pwRoomTypes" @change="getDataList">
            </e-condition>
            <e-condition label="审核状态" type="radio" v-model="searchListForm.status"
                         name="status" :options="pwAppointmentStatus" @change="getDataList">
            </e-condition>

        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-input">
                <span style="vertical-align: middle">容纳人数范围</span>
                <el-input name="capacityMin" size="mini" style="width:80px;" maxlength="6" type="number" v-model.number="searchListForm.capacityMin"
                           @change="getDataList">
                </el-input>
                <span style="color:#dcdfe6;">-</span>
                <el-input name="capacityMax" size="mini" style="width:80px;" maxlength="6" type="number" v-model.number="searchListForm.capacityMax"
                           @change="getDataList">
                </el-input>

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
                <el-input name="userName" size="mini" class="w300" v-model="searchListForm['user.name']"
                          placeholder="申请人" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading" @sort-change="handleTableSortChange">

            <el-table-column prop="u.name" label="申请人" align="left" min-width="100"  sortable="u.name">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.user.name || ''}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="预约日期" align="center" min-width="80">
                <template slot-scope="scope">
                    {{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="预约时间段" align="center" min-width="80">
                <template slot-scope="scope">
                    <span>{{scope.row.startDate | formatDateFilter('HH:mm')}}-{{scope.row.endDate | formatDateFilter('HH:mm')}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" align="center" min-width="100" sortable="status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(pwAppointmentStatusEntries)}}<span v-if="scope.row.status == '0' && scope.row.expired">(已过期)</span>
                </template>
            </el-table-column>
            <el-table-column prop="r.name" label="房间名称" align="center" sortable="r.name" min-width="100">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.pwRoom.name" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.pwRoom.name}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="r.type" label="房间类型" align="center" min-width="90" sortable="r.type">
                <template slot-scope="scope">
                    {{scope.row.pwRoom.type | selectedFilter(pwRoomTypesEntries)}}
                </template>
            </el-table-column>
            <el-table-column prop="subject" label="用途" align="center" min-width="100">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.subject" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.subject}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="pwRoom.num" label="容纳人数" align="center" min-width="70">
                <template slot-scope="scope">
                    <span v-if="scope.row.pwRoom.numtype == '1'">{{scope.row.pwRoom.num}}</span><span v-else>0</span>
                </template>
            </el-table-column>
            <el-table-column prop="personNum" label="参与人数" align="center" min-width="70">
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
            var pwRoomTypes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('pw_room_type'))}));
            var pwAppointmentStatus = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('pw_appointment_status'))}));
            return {
                pwRoomTypes:pwRoomTypes,
                pwAppointmentStatus:pwAppointmentStatus,
                pageCount: 0,
                message: '${message}',
                applyDate: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    'pwRoom.type':'',
                    status:'',
                    capacityMin:'',
                    capacityMax:'',
                    dateFrom:'',
                    dateTo:'',
                    timeFrom:'',
                    timeTo:'',
                    'user.name':''
                },
                loading: false,
                pageList: []
            }
        },
        computed: {
            pwRoomTypesEntries: function () {
                return this.getEntries(this.pwRoomTypes);
            },
            pwAppointmentStatusEntries:function () {
                return this.getEntries(this.pwAppointmentStatus);
            }
        },
        methods: {
            getDataList: function () {
                var self = this;

                if(this.searchListForm.capacityMin){
                    var flagMin = this.getReg(this.searchListForm.capacityMin);
                    if(!flagMin){
                        return false;
                    }
                }
                if(this.searchListForm.capacityMax){
                    var flagMax = this.getReg(this.searchListForm.capacityMax);
                    if(!flagMax){
                        return false;
                    }
                    if(parseInt(this.searchListForm.capacityMax) < parseInt(this.searchListForm.capacityMin)){
                        this.$message({
                            message: '最大值不能小于' + self.searchListForm.capacityMin + '，请修改后，再次查询！',
                            type: 'warning'
                        });
                        return false;
                    }
                }
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwAppointment/listpage?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
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
            getReg:function (value) {
                var reg = /^[1-9][0-9]*$/;
                if (!reg.test(value)) {
                    this.$message({
                        message: '范围必须为正整数，请修改后，再次查询！',
                        type: 'warning'
                    });
                    return false;
                }else if(value > 999999){
                    this.$message({
                        message: '范围不得大于999999，请修改后，再次查询！',
                        type: 'warning'
                    });
                    return false;
                }
                return true;
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