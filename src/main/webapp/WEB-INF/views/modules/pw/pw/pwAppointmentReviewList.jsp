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
                         name="status" :options="appointmentStates" @change="getDataList">
            </e-condition>

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
                <el-input name="userName" size="mini" class="w300" v-model="searchListForm['user.name']"
                          placeholder="申请人" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @sort-change="handleTableSortChange">

            <el-table-column prop="u.name" label="申请人" align="left" min-width="100" sortable="u.name">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.user.name || ''}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="预约日期" align="center" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="预约时间段" align="center" min-width="100">
                <template slot-scope="scope">
                    <span>{{scope.row.startDate | formatDateFilter('HH:mm')}}-{{scope.row.endDate | formatDateFilter('HH:mm')}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" align="center" min-width="80" sortable="status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(pwAppointmentStatusEntries)}}
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
            <el-table-column label="操作" align="center" min-width="80">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button size="mini" type="text" v-if="scope.row.status == '0'"
                                   @click.stop.prevent="checkApply(scope.row.id)">审核
                        </el-button>
                        <el-button size="mini" type="text" v-if="scope.row.status == '1'"
                                   @click.stop.prevent="cancelOrder(scope.row.id)">取消预约
                        </el-button>
                        <el-button size="mini" type="text" v-if="scope.row.status == '4'"
                                   @click.stop.prevent="cancelLock(scope.row.id)">取消锁定
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


    <el-dialog title="预约详情" :visible.sync="dialogVisible" :close-on-click-modal="isClose"
               :before-close="handleCloseDialog" width="600px">

        <el-row :gutter="20" label-width="100px">
            <el-col :span="12">
                <e-col-item label="申请人："><span class="break-ellipsis">{{details.user ? details.user.name : ''}}</span>
                </e-col-item>
            </el-col>
            <el-col :span="12">
                <e-col-item label="房间："><span
                        class="break-ellipsis">{{details.pwRoom ? details.pwRoom.name : ''}}</span></e-col-item>
            </el-col>
        </el-row>
        <el-row :gutter="20" label-width="100px">
            <el-col :span="12">
                <e-col-item label="可容纳人数：">{{details.pwRoom ? details.pwRoom.num : ''}}</e-col-item>
            </el-col>
            <el-col :span="12">
                <e-col-item label="预约日期：">{{details.startDate | formatDateFilter('YYYY-MM-DD')}}</e-col-item>
            </el-col>
        </el-row>
        <el-row :gutter="20" label-width="100px">
            <el-col :span="12">
                <e-col-item label="预约时间段：">{{details.startDate | formatDateFilter('HH:mm')}}-{{details.endDate |
                    formatDateFilter('HH:mm')}}
                </e-col-item>
            </el-col>
            <el-col :span="12">
                <e-col-item label="预约形式：">{{details.appointmentstyle | selectedFilter(appointmentstylesEntries)}}
                </e-col-item>
            </el-col>
        </el-row>
        <el-row :gutter="20" label-width="100px">
            <el-col>
                <e-col-item label="参与人数：">{{details.personNum}}</e-col-item>
            </el-col>
        </el-row>

        <el-row :gutter="20" label-width="100px">
            <el-col>
                <e-col-item label="用途：" class="white-space-pre-static word-break"><span v-html="details.subject"></span></e-col-item>
            </el-col>
        </el-row>

        <div slot="footer" class="dialog-footer">
            <el-button size="mini" type="primary" :disabled="formDisabled" @click.stop.prevent="isPassDialog(1)">审核通过
            </el-button>
            <el-button size="mini" type="primary" :disabled="formDisabled" @click.stop.prevent="isPassDialog(0)">审核不通过
            </el-button>
            <el-button size="mini" @click="handleCloseDialog">取消</el-button>
        </div>
    </el-dialog>

</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            var pwRoomTypes = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('pw_room_type'))}));
            var pwAppointmentStatus = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList('pw_appointment_status'))}));
            return {
                pwRoomTypes: pwRoomTypes,
                pwAppointmentStatus: pwAppointmentStatus,
                appointmentStates: [
                    {
                        label: '待审核',
                        value: '0'
                    },
                    {
                        label: '通过',
                        value: '1'
                    },
                    {
                        label: '已锁定',
                        value: '4'
                    }
                ],
                appointmentstyles: [
                    {
                        label: '个人',
                        value: '1'
                    },
                    {
                        label: '团队',
                        value: '2'
                    },
                    {
                        label: '企业',
                        value: '3'
                    }
                ],
                pageCount: 0,
                message: '${message}',
                applyDate: [],
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    'pwRoom.type': '',
                    status: '',
                    dateFrom: '',
                    dateTo: '',
                    timeFrom: '',
                    timeTo: '',
                    'user.name': ''
                },
                loading: false,
                details: {},
                dialogVisible: false,
                isClose: false,
                formDisabled: false,
                pageList: []
            }
        },
        computed: {
            pwRoomTypesEntries: function () {
                return this.getEntries(this.pwRoomTypes);
            },
            pwAppointmentStatusEntries: function () {
                return this.getEntries(this.pwAppointmentStatus);
            },
            appointmentstylesEntries: function () {
                return this.getEntries(this.appointmentstyles);
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwAppointment/reviewList?' + Object.toURLSearchParams(this.searchListForm)
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
            handleChangeDate: function (value) {
                value = value || [];
                if (value.length == 0) {
                    this.searchListForm.dateFrom = '';
                    this.searchListForm.dateTo = '';
                    this.searchListForm.timeFrom = '';
                    this.searchListForm.timeTo = '';
                } else {
                    this.searchListForm.dateFrom = moment(value[0]).format('YYYY-MM-DD');
                    this.searchListForm.dateTo = moment(value[1]).format('YYYY-MM-DD');
                    this.searchListForm.timeFrom = moment(value[0]).format('HH:mm');
                    this.searchListForm.timeTo = moment(value[1]).format('HH:mm');
                }
                this.getDataList();
            },

            handleCloseDialog: function () {
                this.dialogVisible = false;
            },
            checkApply: function (id) {
                var self = this;
                this.$axios.get('/pw/pwAppointment/details?id=' + id).then(function (response) {
                    var data = response.data;
                    self.details = Object.assign({}, data);
                    self.dialogVisible = true;
                })
            },
            isPassDialog: function (val) {
                this.loading = true;
                this.formDisabled = true;
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwAppointment/manualAudit/' + self.details.id + '/' + val
                }).then(function (response) {
                    var data = response.data;
                    if (data.success == true) {
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwAppointment/getCountToAudit");
                        self.getDataList();
                        self.handleCloseDialog();
                    }
                    self.loading = false;
                    self.formDisabled = false;
                    self.$message({
                        message: data.success == true ? '审核成功' : data.msg || '审核失败',
                        type: data.success == true ? 'success' : 'error'
                    });
                }).catch(function (error) {
                    self.loading = false;
                    self.formDisabled = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type: 'error'
                    });
                });
            },

            cancelOrder: function (id) {
                var self = this;
                this.$confirm('确认要取消该预约吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({method: 'GET', url: '/pw/pwAppointment/cancel?id=' + id}).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '消该预约成功' : data.msg || '消该预约失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                });
            },
            cancelLock: function (id) {
                var self = this;
                this.$confirm('确认要取消锁定吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.$axios({method: 'POST', url: '/pw/pwAppointment/cancel?id=' + id}).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message: data.status == '1' ? '取消锁定成功' : data.msg || '取消锁定失败',
                            type: data.status == '1' ? 'success' : 'error'
                        });
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
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
            }
        },
        created: function () {
            window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwAppointment/getCountToAudit");
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