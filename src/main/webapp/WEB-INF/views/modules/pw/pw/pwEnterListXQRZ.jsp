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
            <div class="tab-cell" @click.stop.prevent="goTeam">
                <span>团队续期</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell active" @click.stop.prevent="goApply">
                <span>审核续期申请</span>
                <div class="arrow-right"></div>
                <div class="bubble-num" v-if="applyRecordNum"><span>{{applyRecordNum}}</span></div>
            </div>
        </div>

        <div class="search-block_bar clearfix mgt-20">
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
                          placeholder="企业/团队名称/负责人/组成员/导师" @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading" @sort-change="handleTableSortChange">

            <el-table-column label="企业/团队名称" align="left" min-width="130">
                <template slot-scope="scope">
                    <table-thing-info :row="getPwEnterInfo(scope.row)"></table-thing-info>
                </template>
            </el-table-column>
            <el-table-column label="团队成员" align="left" min-width="130">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>
            <el-table-column prop="startDate" label="入驻有效期" align="center" sortable="startDate" min-width="150">
                <template slot-scope="scope">
                    <span>{{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}} 至 {{scope.row.endDate | formatDateFilter('YYYY-MM-DD')}}</span>
                </template>
            </el-table-column>
            <el-table-column prop="termTo" label="申请续期到" align="center" min-width="80">
                <template slot-scope="scope">
                    <span>{{scope.row.termTo | formatDateFilter('YYYY-MM-DD')}}</span>
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
            <el-table-column label="状态" align="center" min-width="60">
                <template slot-scope="scope">
                    <span v-if="scope.row.applyRecord">{{scope.row.applyRecord.status | selectedFilter(checkStatesEntries)}}</span>
                </template>
            </el-table-column>

            <shiro:hasPermission name="pw:pwEnter:edit">
                <el-table-column label="操作" align="center" min-width="60">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button size="mini" type="text" @click.stop.prevent="checkApply(scope.row)">审核
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


    <el-dialog title="续期审核" :visible.sync="dialogVisible" :close-on-click-modal="isClose"
               :before-close="handleCloseDialog" width="600px">
        <el-form size="mini" :model="dialogForm" :rules="dialogFormRules" ref="dialogForm" :disabled="formDisabled"
                 label-width="120px" class="user-share">
            <div class="gray-box">
                <p class="gray-box-title"><span>申请信息</span></p>
                <div class="gray-box-content">
                    <el-row :gutter="20" label-width="120px">
                        <el-col :span="22">
                            <e-col-item label="申请续期到：">{{listRow.termTo | formatDateFilter('YYYY-MM-DD')}}<span class="el-form-item-expository">包含当天</span></e-col-item>
                        </el-col>
                    </el-row>
                </div>
            </div>
            <div class="gray-box">
                <p class="gray-box-title"><span>审核</span></p>
                <div class="gray-box-content">
                    <el-form-item prop="atype" label="审核：">
                        <el-select size="mini" v-model="dialogForm.atype" placeholder="请选择" style="width: 170px;">
                            <el-option
                                    v-for="item in resultTypes"
                                    :key="item.id"
                                    :label="item.label"
                                    :value="item.value">
                            </el-option>
                        </el-select>
                    </el-form-item>
                    <el-form-item prop="remarks" label="建议及意见：">
                        <el-input type="textarea" :rows="3" v-model="dialogForm.remarks" maxlength="200"
                                  style="width:300px;"></el-input>
                    </el-form-item>
                </div>
            </div>
        </el-form>
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
                checkStates:[],
                conditions: [
                    {
                        label: '入驻日期',
                        value: '1'
                    },
                    {
                        label: '到期日期',
                        value: '2'
                    }
                ],
                resultTypes: [
                    {
                        id: '222',
                        label: '通过',
                        value: '1'
                    },
                    {
                        id: '111',
                        label: '不通过',
                        value: '0'
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
                    keys: ''
                },
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                loading: false,
                applyDate: [],
                dialogForm: {
                    id: '',
                    type: '30',
                    atype: '',
                    remarks: ''
                },
                listRow: {},
                dialogAction: '',
                dialogVisible: false,
                isClose: false,
                formDisabled: false,

                pageList: [],
                pwEnterTypes: [],
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
            checkStatesEntries: function () {
                return this.getEntries(this.checkStates);
            },
            pwEnterTypeEntries: function () {
                return this.getEntries(this.pwEnterTypes)
            },
            officeEntries: function () {
                return this.getEntries(this.professionals, {label: 'name', value: 'id'})
            },
            dialogFormRules: {
                get: function () {
                    return {
                        atype: [
                            {required: true, message: '请选择审核结果', trigger: 'change'}
                        ],
                        remarks: [
                            {required: this.dialogForm.atype == '0', message: '请提出建议及意见', trigger: 'change'}
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
                    url: '/pw/pwEnter/ajaxListXQRZRecord?isCopy=1&' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        var list = [];
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        list = data.data.list || [];
                        list.forEach(function (item) {
                            if(item.applyRecord){
                                item.termTo = moment(item.endDate).add(item.applyRecord.term,'days');
                            }
                        });
                        self.pageList = list;
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
                if(this.condition == '1'){
                    this.searchListForm.startDate = this.applyDate[0];
                    this.searchListForm.startQDate = this.applyDate[1];
                }else if(this.condition == '2'){
                    this.searchListForm.endDate = this.applyDate[0];
                    this.searchListForm.endQDate = this.applyDate[1];
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
            getCheckStates:function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterAuditList').then(function (response) {
                    self.checkStates = response.data || [];
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
                    label: label,
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
            handleCloseDialog: function () {
                this.dialogVisible = false;
                this.$refs.dialogForm.resetFields();
                this.dialogForm.id = '';
                this.dialogForm.type = '30';
            },
            checkApply: function (row) {
                this.listRow = row;
                this.dialogForm.id = row.id;
                this.dialogVisible = true;
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
                this.loading = true;
                this.formDisabled = true;
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwEnter/ajaxAuditRecored',
                    params: this.dialogForm
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.getDataList();
                        self.handleCloseDialog();
                        self.getApplyRecordNum();
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwEnter/listXQRZCompany",  self.applyRecordNum);
                    }
                    self.loading = false;
                    self.formDisabled = false;
                    self.$message({
                        message: data.status == '1' ? '审核成功' : '审核失败',
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
            this.getEnterStates();
            this.getCheckStates();
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