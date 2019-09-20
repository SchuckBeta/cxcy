<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini"
             :show-message="false">
        <input type="text" style="display: none">
        <div class="conditions">
            <e-condition type="radio" v-model="searchListForm['applicant.office.id']" label="学院" :options="colleges"
                         name="officeId" :default-props="officeProps" @change="getPwEnterListBGSH"></e-condition>
            <e-condition type="radio" v-model="searchListForm.status" label="入驻状态" :options="pwEnterStatues"
                         name="status" @change="getPwEnterListBGSH" :default-props="roomStatusProps"></e-condition>
            <e-condition type="radio" v-model="searchListForm['restatus']" label="房间分配" :options="roomStatus"
                         name="restatus" @change="getPwEnterListBGSH" :default-props="roomStatusProps"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <el-select size="mini" v-model="condition" placeholder="请选择查询条件" clearable @change="handleChangeCondition"
                       style="width:135px; vertical-align: top">
                <el-option
                        v-for="item in conditions"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                </el-option>
            </el-select>
            <el-date-picker
                    v-model="pwEnterApplyDate"
                    :disabled="!condition"
                    type="daterange"
                    size="mini"
                    align="right"
                    @change="handleChangeApplyDate"
                    unlink-panels
                    range-separator="至"
                    start-placeholder="开始日期"
                    end-placeholder="结束日期"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    style="width: 270px; vertical-align: top">
            </el-date-picker>
            <el-input
                    placeholder="企业或团队名称/负责人/组成员/导师/场地"
                    size="mini"
                    name="keys"
                    v-model="searchListForm.keys"
                    keyup.enter.native="getPwEnterListBGSH"
                    class="w300" style="vertical-align: top">
                <el-button slot="append" icon="el-icon-search"
                           @click.stop.prevent="getPwEnterListBGSH"></el-button>
            </el-input>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="pwEnterListBGSH" size="mini" class="table">
            <el-table-column label="入驻信息">
                <template slot-scope="scope">
                    <table-thing-info :row="getPwEnterInfo(scope.row)"></table-thing-info>
                </template>
            </el-table-column>
            <el-table-column label="团队成员">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>
            <el-table-column label="项目数" align="center">
                <template slot-scope="scope">
                    {{scope.row.eprojects ? scope.row.eprojects.length : '0'}}
                </template>
            </el-table-column>
            <el-table-column label="入驻场地" align="center">
                <template slot-scope="scope">
                    <template v-if="scope.row.erooms">
                            <span v-for="room in scope.row.erooms">
                            {{room.pwRoom.pwSpace.id | getRoomNames(baseTreeEntries)}}/{{room.pwRoom.name}}
                                <br>
                        </span>
                    </template>
                    <template v-else>-</template>
                </template>
            </el-table-column>
            <el-table-column label="退孵日期" align="center">
                <template slot-scope="scope">
                    {{scope.row.exitDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label=" 入驻有效期" align="center">
                <template slot-scope="scope">
                    {{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}至{{scope.row.endDate |
                    formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="入驻状态" align="center">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(pwEnterStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="房间分配" align="center">
                <template slot-scope="scope">
                    {{scope.row.restatus | selectedFilter(roomStatuEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini"
                                   @click.stop.prevent="goToChange(scope.row)">变更
                        </el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var offices = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())}));
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    type: '${type}',
                    orderBy: '',
                    orderByType: '',
                    startDate: '',
                    startQDate: '',
                    endDate: '',
                    endQDate: '',
                    exitDate: '',
                    exitQDate: '',
                    keys: '',
                    status: '',
                    'restatus': '',
                    'applicant.office.id': ''
                },
                pwEnterApplyDate: [],
                pageCount: 0,
                pwEnterTypes: [],
                pwEnterStatues: [],
                offices: offices,
                pwEnterListBGSH: [],
                loading: false,
                officeProps: {
                    label: 'name',
                    value: 'id'
                },
                roomStatus: [],
                roomStatusProps: {
                    label: 'name',
                    value: 'key'
                },
                conditions: [{label: "入驻日期", value: "1"}, {label: "到期日期", value: "2"},{label: "退孵日期", value: "3"}],
                condition: '',
                baseTreeEntries: {}
            }
        },
        computed: {
            pwEnterTypeEntries: function () {
                return this.getEntries(this.pwEnterTypes)
            },
            pwEnterStatusEntries: function () {
                return this.getEntries(this.pwEnterStatues, this.roomStatusProps)
            },
            colleges: function () {
                return this.offices.filter(function (item) {
                    return item.grade === '2'
                })
            },
            officeEntries: function () {
                return this.getEntries(this.offices, {label: 'name', value: 'id'})
            },
            roomStatuEntries: function () {
                return this.getEntries(this.roomStatus, this.roomStatusProps)
            }
        },
        methods: {


            handleChangeCondition: function (value) {
                this.emptyAllDate();
                this.pwEnterApplyDate = [];
                if (!value) {
                    this.getPwEnterListBGSH();
                }
            },

            emptyAllDate: function () {
                this.emptyStartDate();
                this.emptyEndDate();
                this.emptyTFDate();
            },

            emptyStartDate: function () {
                this.searchListForm.startDate = '';
                this.searchListForm.startQDate = '';
            },

            emptyEndDate: function () {
                this.searchListForm.endDate = '';
                this.searchListForm.endQDate = '';
            },

            emptyTFDate: function () {
                this.searchListForm.exitDate = '';
                this.searchListForm.exitQDate = '';
            },

            handleChangeApplyDate: function (value) {
                var hasValue = value && value.length > 0;
                var date1 = hasValue ? value[0] : '';
                var date2 = hasValue ? value[1] : '';
                if (this.condition === '1') {
                    this.searchListForm.startDate = date1;
                    this.searchListForm.startQDate = date2;
                    this.emptyEndDate();
                    this.emptyTFDate();
                } else if (this.condition === '2') {
                    this.searchListForm.endDate = date1;
                    this.searchListForm.endQDate = date2;
                    this.emptyStartDate();
                    this.emptyTFDate();
                }else {
                    this.searchListForm.exitDate = date1;
                    this.searchListForm.exitQDate = date2;
                    this.emptyStartDate();
                    this.emptyEndDate();
                }

                this.getPwEnterListBGSH();
            },


            goToChange: function (row) {
                location.href = this.frontOrAdmin + '/pw/pwEnter/formBgsh?id=' + row.id + '&appType=2';
            },


            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getPwEnterListBGSH();
            },

            handlePCPChange: function () {
                this.getPwEnterListBGSH();
            },
            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                }).catch(function () {

                })
            },
            getPwEnterListBGSH: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/pw/pwEnter/ajaxCompanyList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data || {};
                        self.pwEnterListBGSH = pageData.list || [];
                        self.searchListForm.pageNo = pageData.pageNo || 1;
                        self.searchListForm.pageSize = pageData.pageSize || 10;
                        self.pageCount = pageData.count;
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.loading = true;
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                    self.loading = true;
                })
            },
            getPwEnterInfo: function (row) {
                var type = row.type;
                var name = row.eteam.team.name;
                var label = this.pwEnterTypeEntries[row.type];
                var applicant = row.applicant;
                if (type == '2') {
                    name = row.ecompany.pwCompany.name;
                }
                return {
                    label: label,
                    name: name,
                    href: this.frontOrAdmin + '/pw/pwEnter/view?id=' + row.id,
                    officePro: applicant.officeName + (applicant.professional ? ('/' + this.officeEntries[applicant.professional]) : '')
                }
            },
            getPwEnterTeamInfo: function (row) {
                var eteam = row.eteam;
                var applicant = row.applicant;
                return {
                    applicantName: applicant.name,
                    snames: eteam.snames,
                    tnames: eteam.tnames,
                    teamName: eteam.team.name
                }
            },
            getRoomStatus: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxPwEroomStatus').then(function (response) {
                    var data = response.data;
                    self.roomStatus = JSON.parse(data.data) || []
                }).catch(function (error) {

                })
            },
            getPwEnterStatus: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxPwEnterStatus').then(function (response) {
                    var data = response.data;
                    self.pwEnterStatues = JSON.parse(data.data) || []
                }).catch(function (error) {

                })
            },
            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data;
                    var entries = {};
                    self.spaceList.forEach(function (item) {
                        entries[item.id] = item;
                    })
                    self.baseTreeEntries = entries;
                })
            },

        },
        created: function () {
            this.getPwEnterListBGSH();
            this.getPwEnterTypes();
            this.getRoomStatus();
            this.getPwEnterStatus();
            this.getSpaceList();
        }
    })

</script>

</body>
</html>