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
            <e-condition type="radio" v-model="searchListForm['assignStatus']" label="房间分配" :options="roomStatus"
                         name="restatus" @change="handleChangeReStatus" :default-props="roomStatusProps"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" size="mini" :disabled="disabled" @click.stop.prevent="exportTeamFiles">
                    {{disabled? '导出中...' : '导出'}}
                </el-button>
                <el-button type="primary" size="mini" :disabled="disabled" @click.stop.prevent="confirmDelList">批量删除</el-button>
            </div>
            <div class="search-input">
                <el-select size="mini" v-model="condition" placeholder="请选择查询条件" clearable
                           @change="handleChangeCondition"
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
                        :default-time="searchDefaultTime"
                        style="width: 270px; vertical-align: top">
                </el-date-picker>
                <el-input
                        placeholder="企业或团队名称/负责人/组成员/导师"
                        size="mini"
                        name="keys"
                        v-model="searchListForm.keys"
                        @keyup.enter.native="getPwEnterListBGSH"
                        class="w300" style="vertical-align: top">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getPwEnterListBGSH"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="table-container">
        <el-table :data="pwEnterListBGSH" size="mini" class="table" @sort-change="sortChange"  @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    :selectable="selectable"
                    width="60">
            </el-table-column>
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
            <el-table-column label="退孵日期" align="center" prop="exitDate" sortable="exitDate">
                <template slot-scope="scope">
                    {{scope.row.exitDate | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label=" 入驻有效期" align="center" prop="endDate" sortable="endDate">
                    <template slot-scope="scope">
                        <span>{{scope.row.startDate | formatDateFilter('YYYY-MM-DD')}}<br></span>
                        <template v-if="scope.row.endDate">
                            <span>至<br></span>
                            <span>{{scope.row.endDate | formatDateFilter('YYYY-MM-DD')}}</span>
                        </template>
                </template>
            </el-table-column>
            <el-table-column label="入驻状态" align="center" prop="status" sortable="status">
                <template slot-scope="scope">
                    {{scope.row.status | selectedFilter(pwEnterStatusEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="房间分配" align="center" prop="restatus" sortable="restatus">
                <template slot-scope="scope">
                    <template v-if="scope.row.isShow==='0' && !scope.row.erooms">
                        未分配
                    </template>
                    <template v-else>
                        {{scope.row.restatus | selectedFilter(roomStatuEntries)}}
                    </template>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button v-if="disabledStatus.indexOf(scope.row.status) == -1" type="text" size="mini"
                                   @click.stop.prevent="goToChange(scope.row)">变更
                        </el-button>
                        <el-button v-else type="text" size="mini"
                                   @click.stop.prevent="confirmDelPwEnterApply(scope.row)">删除
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
                    type: '0',
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
                    assignStatus: '',
                    'restatus': '',
                    hasYfp: '',
                    isShow: '',
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
                conditions: [{label: "入驻日期", value: "1"}, {label: "到期日期", value: "2"}, {label: "退孵日期", value: "3"}],
                condition: '',
                baseTreeEntries: {},
                disabled: false,
                disabledStatus: ['0','20','60'],
                multipleSelection: [],
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
            }
        },
        methods: {
            handleChangeReStatus: function (value) {
                if (value !== 'tempNotAssign') {
                    this.searchListForm.restatus = value;
                    this.searchListForm.hasYfp = '';
                    this.searchListForm.isShow = '';
                } else {
                    this.searchListForm.restatus = '';
                    this.searchListForm.hasYfp = '0';
                    this.searchListForm.isShow = '0';
                }
                this.getPwEnterListBGSH()
            },

            handleSelectionChange: function (value) {
                this.multipleSelection = value;
            },

            selectable: function (row) {
                return this.disabledStatus.indexOf(row.status) > -1
            },

            confirmDelList: function () {
                var self = this;
                this.$confirm("确认删除选中的入驻记录吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delPwEnterApplyList();
                }).catch(function () {

                })
            },
            delPwEnterApplyList: function () {
                var self = this;
                var ids = this.multipleSelection.map(function (item) {
                    return item.id;
                });
                this.disabled = true;
                this.$axios.post('/pw/pwEnter/ajaxDeletePL?ids=' + ids.join(',')).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getPwEnterListBGSH();
                        self.$message.success("删除成功")
                    } else {
                        self.$message.error(data.msg)
                    }
                    self.disabled = false;
                }).catch(function (error) {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            },
            sortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getPwEnterListBGSH()
            },
            exportTeamFiles: function () {
                var self = this;
                location.href = '/a/pw/pwEnter/ajaxTeamExp?'+ Object.toURLSearchParams(this.searchListForm)
            },

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
                } else {
                    this.searchListForm.exitDate = date1;
                    this.searchListForm.exitQDate = date2;
                    this.emptyStartDate();
                    this.emptyEndDate();
                }

                this.getPwEnterListBGSH();
            },


            goToChange: function (row) {
                location.href = this.frontOrAdmin + '/pw/pwEnter/formBg?id=' + row.id + '&appType=2';
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
                this.$axios.get('/pw/pwEnter/getPwEnterTypes?type=90').then(function (response) {
                    var data = response.data;
                    self.pwEnterTypes = data || [];
                }).catch(function () {

                })
            },
            getPwEnterListBGSH: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/pw/pwEnter/ajaxTeamList?' + Object.toURLSearchParams(this.searchListForm)).then(function (response) {
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
                var applicant = row.applicant;
                if (type == '2') {
                    name = row.ecompany.pwCompany.name;
                }
                return {
//                    label: label,
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
                    self.roomStatus = JSON.parse(data.data) || [];
                    self.roomStatus.push({
                        key: 'tempNotAssign',
                        name: '未分配'
                    })
                }).catch(function (error) {

                })
            },
            getPwEnterStatus: function () {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxPwEnterStatus').then(function (response) {
                    var data = response.data;
                    self.pwEnterStatues = JSON.parse(data.data) || [];
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
            confirmDelPwEnterApply: function (row) {
                var self = this;
                this.$confirm("确认删除这条入驻记录吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delPwEnterApply(row);
                }).catch(function () {

                })
            },

            //删除入驻记录
            delPwEnterApply: function (row) {
                var self = this;
                this.$axios.get('/pw/pwEnter/ajaxDelPwEnterApply?id=' + row.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getPwEnterListBGSH();
                        self.$message.success("删除成功")
                    } else {
                        self.$message.error(data.msg)
                    }
                }).catch(function (error) {
                    self.$message.error(self.xhrErrorMsg)
                })
            }
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