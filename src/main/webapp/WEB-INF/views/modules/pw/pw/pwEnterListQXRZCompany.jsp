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
            <div class="tab-cell active" @click.stop.prevent="goCompany">
                <span>企业退孵</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell" @click.stop.prevent="goTeam">
                <span>团队退孵</span>
                <div class="arrow-right"></div>
            </div>
            <div class="tab-cell" @click.stop.prevent="goApply">
                <span>审核退孵申请</span>
                <div class="arrow-right"></div>
                <div class="bubble-num" v-if="applyRecordNum"><span>{{applyRecordNum}}</span></div>
            </div>
        </div>

        <div class="search-block_bar clearfix mgt-20">
            <div class="search-btns">
                <el-button :disabled="multipleSelectedId == 0" type="primary" size="mini"
                           @click.stop.prevent="batchBack(multipleSelection)">批量退孵
                </el-button>
                <el-button :disabled="multipleSelectedId == 0" size="mini"
                           @click.stop.prevent="batchDelete(multipleSelection)">批量删除
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
                          placeholder="企业名称/负责人/组成员/导师" @keyup.enter.native="getDataList">
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
            <el-table-column label="企业名称" align="left" min-width="130">
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
                    <span>{{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}<br></span>
                    <template v-if="scope.row.endDate">
                        <span>至<br></span>
                        <span>{{scope.row.endDate | formatDateFilter('YYYY-MM-DD')}}</span>
                    </template>
                </template>
            </el-table-column>
            <el-table-column prop="exitDate" label="退孵日期" align="center" sortable="exitDate" min-width="100">
                <template slot-scope="scope">
                    <div>{{scope.row.exitDate | formatDateFilter('YYYY-MM-DD')}}</div>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="入驻状态" align="center" sortable="status" min-width="110">
                <template slot-scope="scope">
                    <span :class="{red:scope.row.status == '30'}">{{scope.row.status | selectedFilter(enterStatesEntries)}}</span>
                </template>
            </el-table-column>

            <shiro:hasPermission name="pw:pwEnter:edit">
                <el-table-column label="操作" align="center" min-width="90">
                    <template slot-scope="scope">
                        <div class="table-btns-action">
                            <el-button size="mini" type="text" @click.stop.prevent="batchBack([scope.row])" v-if="scope.row.status != '60'">退孵
                            </el-button>
                            <el-button size="mini" type="text" @click.stop.prevent="batchDelete([scope.row])" v-if="scope.row.status == 60">删除
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

</div>


<script>
    new Vue({
        el: '#app',
        data: function () {
            var professionals = JSON.parse(JSON.stringify(${fns:getOfficeListJson()})) || [];
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
                        label: '退孵日期',
                        value: '3'
                    }
                ],
                pageCount: 0,
                message: '${message}',
                condition: '',
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
                    exitDate: '',
                    exitQDate: '',
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
                pwEnterTypes: [],
                applyRecordNum: 0,
                searchDefaultTime: ['00:00:00','23:59:59'],

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
            enterStatesEntries: function () {
                return this.getEntries(this.enterStates, {value: 'key', label: 'name'});
            },
            pwEnterTypeEntries: function () {
                return this.getEntries(this.pwEnterTypes)
            },
            officeEntries: function () {
                return this.getEntries(this.professionals, {label: 'name', value: 'id'})
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/pw/pwEnter/ajaxListQXRZ?type=2&' + Object.toURLSearchParams(this.searchListForm)
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
            setDateSearch: function () {
                this.searchListForm.startDate = '';
                this.searchListForm.startQDate = '';
                this.searchListForm.endDate = '';
                this.searchListForm.endQDate = '';
                this.searchListForm.exitDate = '';
                this.searchListForm.exitQDate = '';
                if (this.condition == '1') {
                    this.searchListForm.startDate = this.applyDate[0];
                    this.searchListForm.startQDate = this.applyDate[1];
                } else if (this.condition == '2') {
                    this.searchListForm.endDate = this.applyDate[0];
                    this.searchListForm.endQDate = this.applyDate[1];
                } else if (this.condition == '3') {
                    this.searchListForm.exitDate = this.applyDate[0];
                    this.searchListForm.exitQDate = this.applyDate[1];
                }
            },
            handleChangeCondition: function () {
                if (this.applyDate.length == 0) {
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
            batchBack: function (arr) {
                var self = this;
                var canBackIds = [],canBackNames = [],backAlreadyName = [],flag = true,tip = '';
                arr.forEach(function (item) {
                    if(item.status == '60'){
                        backAlreadyName.push(item.ecompany.pwCompany.name);
                        flag = false;
                    }else{
                        canBackNames.push(item.ecompany.pwCompany.name);
                        canBackIds.push(item.id);
                    }
                });
                tip = '退孵后的企业将退出基地，是否继续？';
                if(!flag){
                    tip = backAlreadyName.join('、') + '已退孵！' + (canBackNames.length > 0 ? canBackNames.join('、') + '将会退孵，退孵后将退出基地，是否继续？' : '请重新选择！');
                }
                this.$confirm(tip,'提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(function () {
                    if(canBackIds.length == 0){
                        return false;
                    }
                    self.$axios({method:'GET', url:'/pw/pwEnter/ajaxExits?ids=' + canBackIds.join(',')}).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message:data.status == '1' ? '退孵成功' : data.msg || '退孵失败',
                            type: data.status == '1' ? 'success' :'error'
                        });
                    }).catch(function (error) {
                        self.$message({
                            message: self.xhrErrorMsg,
                            type: 'error'
                        })
                    })
                }).catch(function () {

                })

            },
            batchDelete:function (arr) {
                var self = this;
                var backAlreadyIds = [],canBackNames = [],backAlreadyName = [],flag = true,tip = '';
                arr.forEach(function (item) {
                    if(item.status == '60'){
                        backAlreadyName.push(item.ecompany.pwCompany.name);
                        backAlreadyIds.push(item.id);
                    }else{
                        canBackNames.push(item.ecompany.pwCompany.name);
                        flag = false;
                    }
                });
                tip = '数据删除后无法恢复，确定删除？';
                if(!flag){
                    tip = canBackNames.join('、') + '未退孵，不能删除！' + (backAlreadyName.length > 0 ? backAlreadyName.join('、') + '将会删除，数据删除后无法恢复，确定删除？' : '请重新选择！');
                }
                this.$confirm(tip,'提示',{
                    confirmButtonText:'确定',
                    cancelButtonText:'取消',
                    type:'warning'
                }).then(function () {
                    if(backAlreadyIds.length == 0){
                        return false;
                    }
                    self.$axios({method:'POST', url:'/pw/pwEnter/ajaxDeletePL?ids=' + backAlreadyIds.join(',')}).then(function (response) {
                        var data = response.data;
                        if (data.status == '1') {
                            self.getDataList();
                        }
                        self.$message({
                            message:data.status == '1' ? '删除成功' : data.msg || '删除失败',
                            type: data.status == '1' ? 'success' :'error'
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
            },
            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                }
            },
            goCompany:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwEnter/listQXRZCompany'
            },
            goTeam:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwEnter/listQXRZTeam'
            },
            goApply:function () {
                window.location.href = this.frontOrAdmin + '/pw/pwEnter/listQXRZ'
            },
            getEnterStates: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxPwEnterStatus?type=30').then(function (response) {
                    var data = response.data;
                    self.enterStates = JSON.parse(data.data) || [];
                });
            },
            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                })
            },
            getApplyRecordNum: function () {
                var self = this;
                this.$axios.get('/pw/pwApplyRecord/ajaxCountByType?type=40').then(function (response) {
                    var data = response.data;
                    self.applyRecordNum = data.data || 0;
                })
            }

        },
        created: function () {
            this.getApplyRecordNum();
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