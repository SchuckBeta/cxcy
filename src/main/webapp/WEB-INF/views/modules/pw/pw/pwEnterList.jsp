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
                         name="officeId" :default-props="officeProps" @change="getPwEnterList"></e-condition>
            <e-condition type="radio" v-model="searchListForm.type" label="类型" :options="pwEnterTypes"
                         name="type" @change="getPwEnterList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <el-date-picker
                        v-model="pwEnterApplyDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        @change="handleChangeApplyDate"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="searchDefaultTime"
                        style="width: 270px;">
                </el-date-picker>
                <el-input
                        placeholder="企业或团队名称/负责人/组成员/导师"
                        size="mini"
                        name="keys"
                        v-model="searchListForm.keys"
                        @keyup.enter.native="getPwEnterList"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getPwEnterList"></el-button>
                </el-input>
                <input type="text" style="display: none">
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="pwEnterList" size="mini" class="table" @sort-change="sortChange">
            <el-table-column label="入驻信息" prop="type" sortable="type">
                <template slot-scope="scope">
                    <table-thing-info :row="getPwEnterInfo(scope.row)"></table-thing-info>
                </template>
            </el-table-column>
            <el-table-column label="团队成员">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>
            <el-table-column label="申请日期" prop="startDate" align="center" sortable="startDate">
                <template slot-scope="scope">
                    {{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="状态"  prop="status" align="center" sortable="status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(pwEnterStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button :disabled="scope.row.status === '1'" type="text" size="mini"
                                   @click.stop.prevent="goToAudit(scope.row)">审核
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
            var pwEnterStatues = JSON.parse('${fns: toJson(fns:getDictList('pw_enter_status'))}');
            return {
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    type: '',
                    orderBy: '',
                    orderByType: '',
                    startDate: '',
                    startQDate: '',
                    keys: '',
                    'applicant.office.id': ''
                },
                pwEnterApplyDate: [],
                pageCount: 0,
                pwEnterTypes: [],
                pwEnterStatues: pwEnterStatues,
                offices: offices,
                pwEnterList: [],
                loading: false,

                officeProps: {
                    label: 'name',
                    value: 'id'
                },
                searchDefaultTime: ['00:00:00','23:59:59']
            }
        },
        computed: {
            pwEnterTypeEntries: function () {
                return this.getEntries(this.pwEnterTypes)
            },
            pwEnterStatusEntries: function () {
                return this.getEntries(this.pwEnterStatues)
            },
            colleges: function () {
                return this.offices.filter(function (item) {
                    return item.grade === '2'
                })
            },
            officeEntries: function () {
                return this.getEntries(this.offices, {label: 'name', value: 'id'})
            }
        },
        methods: {

            sortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getPwEnterList()
            },

            handleChangeApplyDate: function (value) {
                var startDate, startQDate;
                var hasValue = value && value.length > 0;
                startDate = hasValue ? value[0] : '';
                startQDate = hasValue ? value[1] : '';
                this.searchListForm.startDate = startDate;
                this.searchListForm.startQDate = startQDate;
                this.getPwEnterList();
            },

            goToAudit: function (row) {
                location.href = this.frontOrAdmin + '/pw/pwEnter/form?id=' + row.id + '&type=' + row.type;
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getPwEnterList();
            },

            handlePCPChange: function () {
                this.getPwEnterList();
            },
            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                }).catch(function () {

                })
            },
            getPwEnterList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/pw/pwEnter/ajaxList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data || {};
                        self.pwEnterList = pageData.list || [];
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
                var pwEnterTypeEntries = this.pwEnterTypeEntries;
                if(!pwEnterTypeEntries){
                    return {}
                }
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
                    teamName: eteam.team.name,
                }
            }

        },
        created: function () {
            this.getPwEnterList();
            this.getPwEnterTypes();
        }
    })

</script>

</body>
</html>