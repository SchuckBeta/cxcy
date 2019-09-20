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
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">

            <e-condition label="学院" type="radio" v-model="searchListForm['applicant.office.id']" :default-props="defaultProps"
                         name="officeId" :options="collegeList" @change="getDataList">
            </e-condition>
            <e-condition label="入驻状态" type="radio" v-model="searchListForm.status" :default-props="{label:'name',value:'key'}"
                         name="status" :options="enterStates" @change="getDataList">
            </e-condition>

        </div>

        <div class="list-type-tab">
            <div class="tab-cell" @click.stop.prevent="goCompany">
                <span>企业续期</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell active" @click.stop.prevent="goTeam">
                <span>团队续期</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell" @click.stop.prevent="goApply">
                <span>审核续期申请</span>
                <div class="arrow-right"></div>
                <div class="bubble-num" v-if="applyRecordNum"><span>{{applyRecordNum}}</span></div>
            </div>
        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-btns">
                <el-button :disabled="multipleSelectedId == 0" type="primary" size="mini"
                           @click.stop.prevent="batchRenewal">批量续期
                </el-button>
            </div>
            <div class="search-input">
                <el-select size="mini" v-model="condition" placeholder="请选择查询条件"
                           @change="handleChangeCondition" style="width:135px;">
                    <el-option
                            v-for="item in conditions"
                            :key="item.value"
                            :label="item.label"
                            :value="item.value">
                    </el-option>
                </el-select>
                <el-date-picker :disabled="!condition"
                                v-model="applyDate"
                                type="daterange"
                                size="mini"
                                align="right"
                                @change="handleChangeDate"
                                unlink-panels
                                range-separator="至"
                                start-placeholder="开始日期"
                                end-placeholder="结束日期"
                                value-format="yyyy-MM-dd HH:mm:ss"
                                :default-time="searchDefaultTime"
                                style="width: 270px;">
                </el-date-picker>
                <input type="text" style="display:none">
                <el-input name="keys" size="mini" class="w300" v-model="searchListForm.keys"
                          placeholder="团队名称/负责人/组成员/导师" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @selection-change="handleSelectionChange" @sort-change="handleTableSortChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="团队名称" align="left" min-width="130">
                <template slot-scope="scope">
                    <table-thing-info :row="getPwEnterInfo(scope.row)"></table-thing-info>
                </template>
            </el-table-column>
            <el-table-column label="团队成员" align="left" min-width="130">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="入驻有效期" align="center" sortable="startDate" min-width="130">
                <template slot-scope="scope">
                    <span>{{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}} 至 {{scope.row.endDate | formatDateFilter('YYYY-MM-DD')}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="reDate" label="最近一次续期日期（续期次数）" align="center" sortable="reDate"
                             width="150">
                <template slot-scope="scope">
                    <div>{{scope.row.reDate | formatDateFilter('YYYY-MM-DD')}}</div>
                    <div>{{scope.row.termNum ? scope.row.termNum + '次' : '-' }}</div>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="入驻状态" align="center" sortable="status" min-width="100">
                <template slot-scope="scope">
                    <span :class="{red:scope.row.status == '30'}">{{scope.row.status | selectedFilter(enterStatesEntries)}}</span>
                </template>
            </el-table-column>

            <shiro:hasPermission name="pw:pwEnter:edit">
                <el-table-column label="操作" align="center" min-width="100">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button size="mini" type="text" @click.stop.prevent="singleRenewal(scope.row)">续期
                            </el-button>
                        </div>
                    </template>
                </el-table-column>
            </shiro:hasPermission>

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


    <el-dialog :title="dialogAction + '续期'" :visible.sync="dialogVisible" :close-on-click-modal="isClose"
               :before-close="handleCloseDialog" width="400px">
        <el-form size="mini" :model="dialogForm" :rules="dialogFormRules" ref="dialogForm" :disabled="formDisabled"
                 label-width="120px">
            <el-form-item label="到期时间：">
                {{dialogForm.endDate | formatDateFilter('YYYY-MM-DD')}}<span class="el-form-item-expository">包含当天</span>
            </el-form-item>
            <el-form-item prop="termDate" label="请选择续期：">
                <el-select size="mini" v-model="dialogForm.termDate" placeholder="请选择续期" style="width: 170px;" @change="handleChangeTermDate">
                    <el-option
                            v-for="item in yearOptions"
                            :key="item.num"
                            :label="item.remarks | dateAddDays(dialogForm.endDate, item.num)"
                            :value="item.num">
                    </el-option>
                </el-select>
            </el-form-item>
            <el-form-item prop="termDefinedDate" v-if="dialogForm.termDate == '-1'">
                <el-date-picker style="width: 170px;"
                                v-model="dialogForm.termDefinedDate"
                                type="date"
                                :default-value="sysDateAfter"
                                :picker-options="pickerOptions"
                                placeholder="选择日期">
                </el-date-picker>
            </el-form-item>
        </el-form>
        <div class="renewal-dialog-tip">温馨提示：团队续期后，与团队关联的项目及企业一并续期</div>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" @click="handleCloseDialog">取消</el-button>
            <el-button size="mini" type="primary" @click.stop.prevent="saveDialog('dialogForm')">确定</el-button>
        </div>
    </el-dialog>

</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            var professionals = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            return {
                professionals: professionals,
                enterStates: [],
                conditions: [
                    {
                        label: '入驻日期',
                        value: '1'
                    },
                    {
                        label: '到期日期',
                        value: '2'
                    },
                    {
                        label: '续期日期',
                        value: '3'
                    }
                ],
                pageCount: 0,
                message: '${message}',
                condition:'',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    orderBy: '',
                    orderByType: '',
                    'applicant.office.id': '',
                    status: '',
                    startDate: '',
                    startQDate: '',
                    endDate: '',
                    endQDate: '',
                    reDate: '',
                    reQDate: '',
                    keys: ''
                },
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                loading: false,
                multipleSelection: [],
                multipleSelectedId: [],
                applyDate: [],
                dialogForm: {
                    ids: '',
                    termDate: '',
                    termDefinedDate: '',
                    term:'',
                    endDate:''
                },
                dialogAction: '',
                dialogVisible: false,
                isClose: false,
                formDisabled: false,

                pageList: [],
                pwEnterTypes: [],
                sysDate: '',
                yearOptions: [],
                applyRecordNum:0,
                searchDefaultTime: ['00:00:00','23:59:59']
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
            enterStatesEntries: function () {
                return this.getEntries(this.enterStates,{value: 'key', label: 'name'});
            },
            pwEnterTypeEntries: function () {
                return this.getEntries(this.pwEnterTypes)
            },
            officeEntries: function () {
                return this.getEntries(this.professionals, {label: 'name', value: 'id'})
            },
            sysDateAfter: function () {
                var date = new Date();
                if(this.dialogForm.endDate){
                    return moment(this.dialogForm.endDate).format('YYYY-MM-DD');
                }
                if (this.sysDate) {
                    date = new Date(this.sysDate);
                    date.setDate(date.getDate() + 1);
                }
                return moment(date).format('YYYY-MM-DD');
            },
            pickerOptions: {
                get:function () {
                    var self = this;
                    return {
                        disabledDate: function (time) {
                            return time.getTime() < new Date(self.dialogForm.endDate);
                        }
                    };
                }
            },
            dialogFormRules: {
                get: function () {
                    return {
                        termDate: [
                            {required: true, message: '请选择续期', trigger: 'change'}
                        ],
                        termDefinedDate: [
                            {required: true, message: '请选择自定义时间', trigger: 'change'}
                        ]
                    }
                }
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwEnter/ajaxListXQRZ?type=0&' + Object.toURLSearchParams(this.searchListForm)
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
            setDateSearch:function () {
                this.searchListForm.startDate = '';
                this.searchListForm.startQDate = '';
                this.searchListForm.endDate = '';
                this.searchListForm.endQDate = '';
                this.searchListForm.reDate = '';
                this.searchListForm.reQDate = '';
                if(this.condition == '1'){
                    this.searchListForm.startDate = this.applyDate[0];
                    this.searchListForm.startQDate = this.applyDate[1];
                }else if(this.condition == '2'){
                    this.searchListForm.endDate = this.applyDate[0];
                    this.searchListForm.endQDate = this.applyDate[1];
                }else if(this.condition == '3'){
                    this.searchListForm.reDate = this.applyDate[0];
                    this.searchListForm.reQDate = this.applyDate[1];
                }
            },
            handleChangeCondition: function () {
                if(this.applyDate.length == 0){
                    return false;
                }
                this.setDateSearch();
                this.getDataList();
            },
            handleChangeDate: function (value) {
                this.applyDate = value || [];
                this.setDateSearch();
                this.getDataList();
            },

            getEnterStates:function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxPwEnterStatus?type=20').then(function (response) {
                    var data = response.data;
                    self.enterStates = JSON.parse(data.data) || [];
                });
            },
            getPwEnterInfo: function (row) {
                var type = row.type;
                var name = row.eteam ? row.eteam.team.name : '';
                var label = this.pwEnterTypeEntries[row.type];
                var applicant = row.applicant;
                if (type == '2') {
                    name = row.ecompany ? row.ecompany.pwCompany.name : '';
                }
                return {
                    name: name,
                    href: this.frontOrAdmin + '/pw/pwEnter/view?id=' + row.id,
                    officePro: applicant.officeName + '/' + (applicant.professional && this.officeEntries[applicant.professional] ? this.officeEntries[applicant.professional] : '-')
                }
            },
            getPwEnterTeamInfo: function (row) {
                var eteam = row.eteam || {};
                var applicant = row.applicant;
                return {
                    applicantName: applicant.name,
                    snames: eteam.snames || '',
                    tnames: eteam.tnames || ''
                }
            },
            handleChangeTermDate: function (value) {
                if (value !== '-1') {
                    this.dialogForm.termDefinedDate = '';
                }
            },
            getSysDate: function () {
                var self = this;
                this.$axios.get('/sys/sysCurDateYmdHms').then(function (response) {
                    self.sysDate = moment(response.data).format('YYYY-MM-DD');
                    self.getYearOptions();
                })
            },
            getYearOptions: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxDtypeTerms?isAll=false').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.yearOptions = JSON.parse(data.data) || [];
                        self.yearOptions.push({remarks: '自定义', num: '-1'});
                    }
                })
            },

            handleCloseDialog: function () {
                this.dialogVisible = false;
                this.$refs.dialogForm.resetFields();
                this.dialogForm.ids = '';
                this.dialogForm.term = '';
                this.dialogForm.endDate = '';
            },
            batchRenewal: function () {
                var len = this.yearOptions.length;
                if (this.yearOptions[len-1].num == '-1') {
                    this.yearOptions.splice(len - 1, 1);
                }
                this.dialogVisible = true;
                this.dialogAction = '批量';
                this.dialogForm.ids = this.multipleSelectedId.join(',');
            },
            singleRenewal: function (row) {
                var len = this.yearOptions.length;
                if (this.yearOptions[len-1].num != '-1') {
                    this.yearOptions.push({remarks: '自定义', num: '-1'});
                }
                this.dialogVisible = true;
                this.dialogAction = '';
                this.dialogForm.ids = row.id;
                this.dialogForm.endDate = row.endDate;
            },
            getParams:function () {
                var params,date;
                var termDate = this.dialogForm.termDate;
                var termDefinedDate = this.dialogForm.termDefinedDate;
                params = {
                    ids:this.dialogForm.ids,
                    term:''
                };
                params['term'] = termDate;
                if(termDate == '-1'){
                    date = termDefinedDate;
                    var endDate = this.dialogForm.endDate || this.sysDate;
                    params['term'] = Math.ceil((new Date(date).getTime() - new Date(endDate).getTime()) / 24 / 3600 / 1000).toString();
                }
                return params;
            },
            saveDialog: function (formName) {
                var self = this;
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.saveAjax();
                    }
                })
            },
            saveAjax: function () {
                var params = this.getParams();
                this.loading = true;
                this.formDisabled = true;
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/pw/pwEnter/ajaxFormXqs',
                    params: Object.toURLSearchParams(params)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                        self.handleCloseDialog();
                    }
                    self.loading = false;
                    self.formDisabled = false;
                    self.$message({
                        message: data.status == '1' ? '续期成功' : data.msg || '续期失败',
                        type: data.status == '1' ? 'success' : 'error'
                    });
                }).catch(function () {
                    self.loading = false;
                    self.formDisabled = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    });
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
            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                }
            },
            goCompany: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwEnter/listXQRZCompany'
            },
            goTeam: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwEnter/listXQRZTeam'
            },
            goApply: function () {
                window.location.href = this.frontOrAdmin + '/pw/pwEnter/listXQRZ'
            },

            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                })
            },
            getApplyRecordNum:function () {
                var self = this;
                this.$axios.get('/pw/pwApplyRecord/ajaxCountByType?type=30').then(function (response) {
                    var data = response.data;
                    self.applyRecordNum = data.data || 0;
                })
            }

        },
        created: function () {
            this.getApplyRecordNum();
            this.getSysDate();
            this.getEnterStates();
            this.getPwEnterTypes();
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