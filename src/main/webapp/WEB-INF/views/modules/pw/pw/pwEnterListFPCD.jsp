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
                         name="officeId" :default-props="officeProps" @change="getPwEnterListFPCD"></e-condition>
            <e-condition type="radio" v-model="searchListForm.status" label="入驻状态" :options="pwEnterStatues"
                         name="status" @change="getPwEnterListFPCD" :default-props="roomStatusProps"></e-condition>
            <e-condition type="radio" v-model="searchListForm['restatus']" label="房间分配" :options="roomStatus"
                         name="restatus" @change="getPwEnterListFPCD" :default-props="roomStatusProps"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <el-select size="mini" v-model="condition" placeholder="请选择查询条件" clearable
                           @change="handleChangeCondition"
                           style="width:135px;">
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
                        :default-time="searchDefaultTime"
                        style="width: 270px;">
                </el-date-picker>
                <el-input
                        placeholder="企业或团队名称/负责人/组成员/导师"
                        size="mini"
                        name="keys"
                        v-model="searchListForm.keys"
                        @keyup.enter.native="getPwEnterListFPCD"
                        class="w300">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getPwEnterListFPCD"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="pwEnterListFPCD" size="mini" class="table" @sort-change="sortChange">
            <el-table-column label="入驻信息"  prop="type" sortable="type">
                <template slot-scope="scope">
                    <table-thing-info :row="getPwEnterInfo(scope.row)"></table-thing-info>
                </template>
            </el-table-column>
            <el-table-column label="团队成员">
                <template slot-scope="scope">
                    <table-team-member :row="getPwEnterTeamInfo(scope.row)"></table-team-member>
                </template>
            </el-table-column>
            <el-table-column label="入驻说明" align="center">
                <template slot-scope="scope">
                    <template v-if="hasApplyRecordType(scope.row)">
                        {{scope.row.applyRecord.type | selectedFilter(pwEnterRemarkEntries)}}
                    </template>
                    <template v-else>{{scope.row.applyRecord.bgremarks}}</template>
                </template>
            </el-table-column>
            <el-table-column label="所需工位" align="center">
                <template slot-scope="scope">
                    <template v-if="scope.row.expectWorkNum">
                        {{scope.row.expectWorkNum}}位
                    </template>
                    <template v-else>否</template>
                </template>
            </el-table-column>
            <el-table-column label="入驻场地（工位数）" align="center">
                <template slot-scope="scope">
                    <template v-if="scope.row.erooms">
                        <el-tooltip v-for="room in scope.row.erooms" :key="room.id"
                                    :content="room.pwRoom.pwSpace.id | getRoomNames(baseTreeEntries, 'pId', room.pwRoom.name, room.num)"
                                    popper-class="white" placement="right">
                            <span class="break-ellipsis">
                            {{room.pwRoom.pwSpace.id | getRoomNames(baseTreeEntries, 'pId', room.pwRoom.name, room.num)}}
                                <br>
                            </span>
                        </el-tooltip>
                    </template>
                    <template v-else>-</template>
                </template>
            </el-table-column>
            <el-table-column label=" 入驻有效期" align="center">
                <template slot-scope="scope">
                    <span>{{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}<br></span>
                    <span>至<br></span>
                    <span>{{scope.row.endDate | formatDateFilter('YYYY-MM-DD')}}</span>
                </template>
            </el-table-column>
            <el-table-column label="入驻状态" align="center" prop="status" sortable="status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(pwEnterStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="房间分配" align="center" prop="restatus"  sortable="restatus">
                <template slot-scope="scope">
                    {{scope.row.restatus | selectedFilter(roomStatuEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" size="mini"
                                   @click.stop.prevent="assignRoom(scope.row)">{{scope.row.restatus === '20' ? '重新分配' : '分配'}}
                        </el-button>
                        <el-button :disabled="scope.row.status === '1'" type="text" size="mini"
                                   @click.stop.prevent="ignoreRoom(scope.row)">忽略
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
                    type: '',
                    orderBy: '',
                    orderByType: '',
                    startDate: '',
                    startQDate: '',
                    endDate: '',
                    endQDate: '',
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
                pwEnterListFPCD: [],
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
                conditions: [{label: "入驻日期", value: "1"}, {label: "到期日期", value: "2"}],
                condition: '',
                baseTreeEntries: {},
                pwEnterRemarks: [],
                searchDefaultTime: ['00:00:00','23:59:59']
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
            },
            pwEnterRemarkEntries: function () {
                return this.getEntries(this.pwEnterRemarks, this.roomStatusProps)
            }
        },
        methods: {
            hasApplyRecordType: function (row) {
                var applyRecord = row.applyRecord;
                var type = applyRecord.type;
                if(!type) return false;
                return this.pwEnterRemarkEntries && !!this.pwEnterRemarkEntries[type];
            },
            sortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getPwEnterListFPCD()
            },
            handleChangeCondition: function (value) {
                this.emptyAllDate();
                this.pwEnterApplyDate = [];
                if (!value) {
                    this.getPwEnterListFPCD();
                }
            },

            emptyAllDate: function () {
                this.emptyStartDate();
                this.emptyEndDate();
            },

            emptyStartDate: function () {
                this.searchListForm.startDate = '';
                this.searchListForm.startQDate = '';
            },

            emptyEndDate: function () {
                this.searchListForm.endDate = '';
                this.searchListForm.endQDate = '';
            },

            handleChangeApplyDate: function (value) {
                var startDate, startQDate, endDate, endQDate;
                var hasValue = value && value.length > 0;
                if (this.condition === '1') {
                    startDate = hasValue ? value[0] : '';
                    startQDate = hasValue ? value[1] : '';
                    this.searchListForm.startDate = startDate;
                    this.searchListForm.startQDate = startQDate;
                    this.emptyEndDate();
                } else if (this.condition === '2') {
                    endDate = hasValue ? value[0] : '';
                    endQDate = hasValue ? value[1] : '';
                    this.searchListForm.endDate = endDate;
                    this.searchListForm.endQDate = endQDate;
                    this.emptyStartDate();
                }

                this.getPwEnterListFPCD();
            },


            assignRoom: function (row) {
                location.href = this.frontOrAdmin + '/pw/pwEnterRoom/assignRoomForm?pwEnter.id=' + row.id + '&type=' + row.type;
            },

            ignoreRoom: function (row) {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxShow?' + Object.toURLSearchParams({
                            id: row.id,
                            isShow: '0'
                        })).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        window.parent.sideNavModule.changeStaticUnreadTag("/a/pw/pwEnter/getCountToFPCD");
                        self.$message.success("忽略该条数据成功")
                        self.getPwEnterListFPCD();
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg);
                })
            },

            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getPwEnterListFPCD();
            },

            handlePCPChange: function () {
                this.getPwEnterListFPCD();
            },
            getPwEnterTypes: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                }).catch(function () {

                })
            },

            getPwEnterBgremarks: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxPwEnterRemarks').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pwEnterRemarks = JSON.parse(data.data);
                    }
                })
            },

            getPwEnterListFPCD: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/pw/pwEnter/ajaxListFPCD?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var pageData = data.data || {};
                        self.pwEnterListFPCD = pageData.list || [];
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
                this.$axios.get('/pw/pwEnter/ajaxPwEnterStatus?type=10').then(function (response) {
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
            this.getPwEnterListFPCD();
            this.getPwEnterTypes();
            this.getRoomStatus();
            this.getPwEnterStatus();
            this.getSpaceList();
            this.getPwEnterBgremarks();
        }
    })

</script>

</body>
</html>