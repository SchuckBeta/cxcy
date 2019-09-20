<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>
<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <edit-bar-province></edit-bar-province>
    <div class="content-container">
        <div class="competition-tabs">
            <div class="el-row">
                <el-col :span="6" v-for="item in trackList" :key="item.value">
                    <div class="competition-tab" :class="{active: searchListForm.gcTrack === item.value}"><a
                            :href="item.value ? '#'+ item.value : 'javascript:void(0);'" @click.stop="handleChangeTrack(item)">{{item.label}}</a></div>
                </el-col>
            </div>
        </div>
        <ec-condition v-model="collapsed">
            <ec-condition-item label="地区"
                               :value="searchListForm.areaCode | getSelectedVal(areaEntity, {label:'name', value: 'id'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.areaCode">
                    <ec-radio class="ec-radio_all" label="" @change="getPage">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in areas" :label="item.id" :key="item.id" @change="getPage">
                            {{item.name}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="高校类型"
                               :value="searchListForm.schoolType | getSelectedVal(schoolTypeEntity, {label:'name', value: 'key'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.schoolType">
                    <ec-radio class="ec-radio_all" label="" @change="getPage">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in schoolTypeList" :label="item.key" :key="item.key" @change="getPage">
                            {{item.name}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="高校"
                               :value="searchListForm.schoolName | getSelectedVal(schoolEntity, {label:'schoolName', value: 'id'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.schoolName">
                    <ec-radio class="ec-radio_all" label="" @change="getPage">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in schoolList" :label="item.id" :key="item.id" @change="getPage">
                            {{item.schoolName}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="技术领域"
                               :value="searchListForm.belongsField | getSelectedVal(technologyFieldsEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.belongsField">
                    <ec-radio class="ec-radio_all" label="" @change="getPage">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in technologyFields" :label="item.value" :key="item.value"
                                  @change="getPage">
                            {{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item v-show="!!searchListForm.gcTrack" label="参赛组别"
                               :value="searchListForm.level | getSelectedVal(gContestLevelEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.level" @change="getPage">
                    <ec-radio class="ec-radio_all" label="" >全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in gContestLevel" :label="item.value" :key="item.value">
                            {{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item v-show="searchListForm.gcTrack !== '20190508110645100065'" label="大赛类别" :value="searchListForm.proCategory | getSelectedVal(proCategoryEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.proCategory"
                                @change="getPage">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in proCategory" :label="item.value" :key="item.value">{{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
        </ec-condition>
        <div class="search-bar-container">
            <div v-if="actYwStatusList.length > 0" class="result-count">
                共选中<span>{{ multipleSelection.length }}</span>条结果
            </div>
            <div class="search-input_content">
                <el-input
                        class="w300"
                        size="mini"
                        placeholder="项目名/编号/申报人"
                        @keyup.enter.native="getPage"
                        v-model="searchListForm.queryStr">
                    <el-button slot="append" icon="el-icon-search" @click.stop="getPage"></el-button>
                </el-input>
            </div>
            <c:if test="${isAdmin eq 1}">
            <div v-if="actYwStatusList.length > 0" class="search-buttons_content">
                <el-dropdown :disabled="disabled" size="small" @command="handleCommand">
                    <el-button :icon="disabled ? 'el-icon-loading' : ''" type="primary" size="mini">
                        批量录入结果<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item v-for="item in actYwStatusList" :command="item" :key="item.id">{{item.state}}</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>
            </c:if>
        </div>
        <div v-loading="loading" class="table-container">
            <el-table
                    :data="pageList"
                    @selection-change="handleSelectionChange"
                    @sort-change="handleSortChange"
                    size="mini"
                    class="mgb-20"
                    ref="multipleTable">
                <el-table-column v-show="actYwStatusList.length > 0" type="selection" width="50"
                                 align="center"></el-table-column>
                <el-table-column width="85" label="学校排名" prop="proModel.ranking" align="center"></el-table-column>
                <el-table-column label="项目信息" width="280">
                    <template slot-scope="scope">
                        <competition-info-column-gc
                                :key="scope.row.id"
                                :row="scope.row.proModel"
                                :to="ctx + '/promodel/proModel/provViewForm?id='+ scope.row.proModel.id+'&actYwId='+scope.row.actYwId">
                        </competition-info-column-gc>
                    </template>
                </el-table-column>
                <el-table-column label="团队成员" width="200">
                    <template slot-scope="scope">
                        <project-team-column :row="scope.row.proModel" :key="scope.row.id"></project-team-column>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="proCategory"
                        label="大赛类别"
                        align="center"
                        sortable="custom">
                    <template slot-scope="scope">
                        {{scope.row.proModel.proCategory | getSelectedVal(proCategoryEntity)}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        sortable="custom"
                        align="center"
                        label="申请日期">
                    <template slot-scope="scope">{{ scope.row.createDate | formatDate }}</template>
                </el-table-column>
                <template v-if="actYwStatusList.length > 0">
                    <el-table-column  prop="score" align="center" label="专家评分"
                                      sortable="custom">
                        <template slot-scope="scope">{{ scope.row.proModel.gScore }}</template>
                    </el-table-column>
                    <el-table-column align="center" label="项目评级">
                        <template slot-scope="scope">
                            <el-select v-if="scope.row.proModel.auditMap.status == 'todo'" placeholder="录入结果" size="mini" :disabled="disabled"
                                       v-model="scope.row.tempLevel" @change="handleChangeTempLevel(scope.row)">
                                <el-option v-for="item in actYwStatusList" :key="item.id" :label="item.state"
                                           :value="item.status"></el-option>
                            </el-select>
                        </template>
                    </el-table-column>
                </template>
                <el-table-column align="center" label="状态">
                    <template slot-scope="scope">
                        <a :href="ctx +'/actyw/actYwGnode/designView?groupId='+scope.row.proModel.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank">
                            <span v-if="scope.row.state == '1'">项目已结项</span><span v-else>待{{scope.row.proModel.auditMap.taskName}}</span>
                        </a>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="操作">
                    <template slot-scope="scope">
                        <el-tooltip v-if="scope.row.state != '1' &&  scope.row.proModel.auditMap.status == 'todo'" class="item"
                                    :content="scope.row.proModel.auditMap.taskName" popper-class="white" placement="bottom">
                                  <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-daishenhe"
                                           @click.stop="goToAudit(scope.row)"></el-button>
                                  </span>
                        </el-tooltip>
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
</div>
<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var tracks = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            var technologyFields = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("technology_field"))})) || [];
            var page = JSON.parse(JSON.stringify(${fns: toJson(page)})) || {};
            var pageCount = page.count || 0;
            var pageList = page.list || [];
            return {
                collapsed: true,
                actionPath: '${actionPath}',
                searchListForm: {
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    schoolType: '',
                    areaCode: "",
                    queryStr: '',
                    schoolName: '',
                    proCategory: '',
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    gcTrack: '',
                    belongsField: '',
                    level: '',
                    finalResult: '',
                },
                tracks: tracks,
                technologyFields: technologyFields,
                gContestLevel: [],
                trackLevels: {},
                timeRange: [],
                pageCount: pageCount,
                pageList: pageList,
                areas: [],
                schoolTypeList: [],
                proCategory: [],
                multipleSelection: [],
                loading: false,
                proLevel: '',
                actYwStatusList: [],
                schoolList: [],
                finalResults: [],
                disabled: false,
                gContestLevelAll: []
            }
        },
        computed: {
            trackList: function () {
                return [{
                    label: '全部',
                    value:''
                }].concat(this.tracks)
            },
            areaEntity: function () {
                return this.getEntity(this.areas, {label: 'name', value: 'id'})
            },
            schoolTypeEntity: function () {
                return this.getEntity(this.schoolTypeList, {label: 'name', value: 'key'})
            },
            proCategoryEntity: function () {
                return this.getEntity(this.proCategory)
            },
            schoolEntity: function () {
                return this.getEntity(this.schoolList, {label: 'schoolName', value: 'id'})
            },
            gContestLevelEntity: function () {
                return this.getEntity(this.gContestLevelAll)
            },

            technologyFieldsEntity: function () {
                return this.getEntity(this.technologyFields)
            },

            finalResultEntity: function () {
                return this.getEntity(this.finalResults)
            }
        },
        methods: {
            handleChangeTrack: function (item) {
                this.searchListForm.gcTrack = item.value;
                if(item.value === '20190508110645100065'){
                    this.searchListForm.proCategory =  '';
                }
                this.getGContestLevels();
                this.getPage();
            },

            setGcTrack: function () {
                var hash = location.hash;
                var tracks = this.tracks;
                var gcTrackItem = tracks[0];
                if (hash) {
                    hash = hash.replace('#', '');
                    gcTrackItem = this.getGcTrackItem(hash);
                }
                this.searchListForm.gcTrack = gcTrackItem.value;
                this.getGContestLevels();
//                this.getPage();
            },

            getGcTrackItem: function (value) {
                var i = 0;
                var tracks = this.tracks;
                while (i < tracks.length) {
                    var gcTrackItem = tracks[i];
                    if (gcTrackItem.value === value) {
                        return gcTrackItem;
                    }
                    i++;
                }
                return '';
            },

            getGContestLevels: function () {
                var self = this;
                var val = this.searchListForm.gcTrack;
                this.searchListForm.level = '';
                if (this.trackLevels[val]) {
                    this.gContestLevel = this.trackLevels[val];
                    return;
                }
                this.$axios.get('/sys/dict/getDictList', {params: {type: val}}).then(function (response) {
                    var data = response.data;
                    self.trackLevels[val] = data || [];
                    self.gContestLevel = self.trackLevels[val];
                    self.gContestLevelAll = self.gContestLevelAll.concat(data)
                })
            },
            getPage: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/cms/ajax/listForm', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.page) {
                        var page = data.page || {};
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        self.pageList = page.list || [];
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
            },
            getSchoolList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolList = data.data || []
                    }
                })
            },
            getSchoolCityList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolCityList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.areas = data.data || []
                    }
                })
            },

            getSchoolTypeList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolTypeList = JSON.parse(data.data) || []
                    }
                })
            },

            getProCategory: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=competition_net_type').then(function (response) {
                    self.proCategory = response.data || [];
                })
            },

            handleSelectionChange: function (val) {
                this.multipleSelection = val;
            },

            handlePaginationSizeChange: function (val) {
                this.searchListForm.pageSize = val;
                this.getPage();
            },

            handlePaginationPageChange: function () {
                this.getPage();
            },

            goToAudit: function (row) {
                location.href = this.ctx + '/promodel/proModel/provAuditForm?actYwId=' + row.actYwId + '&gnodeId=' + row.proModel.auditMap.gnodeId + '&proModelId=' + row.proModel.auditMap.proModelId + '&pathUrl=${actionUrl}&taskName=' + encodeURI(row.proModel.auditMap.taskName);
            },

            handleSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getPage();
            },


            getActYwStatusListByGnodeId: function () {
                var self = this;
                this.$axios.get('/promodel/proModel/getActYwStatusListByGnodeId', {params: {gnodeId: this.searchListForm.gnodeId}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.actYwStatusList = data.data || [];
                    }
//                    self.getPage();
                })
            },

            handleCommand: function (item) {
                this.proLevel = item.status;
                this.setProLevel();
            },

            handleChangeTempLevel: function (row) {
                var params = {
                    id: row.modelId,
                    gnodeId: row.proModel.auditMap.gnodeId,
                    grade: row.tempLevel,
                    source: '',
                    actionPath: this.actionPath,
                };
                this.setProLevel(params, row);
            },

            handleChangeProLevel: function () {
                this.setProLevel();
            },

            setProLevel: function (params, row) {
                var self = this;
                var ids = this.multipleSelection.map(function (item) {
                    return item.modelId;
                });
                if(ids.length === 0 && !params.id){
                    this.$alert('请选择需要录入结果的项目', '提示', {
                        type: 'warning'
                    })
                    return false;
                }
                var defaultParams = {
                    id: ids.join(','),
                    gnodeId: this.searchListForm.gnodeId,
                    actionPath: this.actionPath,
                    grade: this.proLevel,
                    source: '',
                };
                this.disabled = true;
                params = params || defaultParams;
                this.$axios.get('/promodel/proModel/saveProvProModelGateAudit', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.getPage();
                        self.proLevel = '';
                        self.$alert('项目评级成功', '提示', {
                            type: 'success'
                        })
                        window.parent.LeftSideBarMenuApp.updateTodoCount()
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.multipleSelection = [];
                    if (row) {
                        row.tempLevel = '';
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                })
            }
        },
        created: function () {
//            this.getPage();
//            this.setGcTrack();
            this.getSchoolCityList();
            this.getSchoolTypeList();
            this.getProCategory();
            this.getActYwStatusListByGnodeId();
            this.getSchoolList();
        }
    })


</script>
</body>
</html>