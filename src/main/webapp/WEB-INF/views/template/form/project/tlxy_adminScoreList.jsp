<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid" style="margin-bottom:40px;">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" size="mini" autocomplete="off" ref="searchListForm">
        <input type="hidden" name="pageNo" :value="searchListForm.pageNo"/>
        <input type="hidden" name="pageSize" :value="searchListForm.pageSize"/>
        <input type="hidden" name="orderBy" :value="searchListForm.orderBy"/>
        <input type="hidden" name="orderByType" :value="searchListForm.orderByType"/>
        <input type="hidden" name="beginDate" :value="searchListForm['proModel.beginDate']"/>
        <input type="hidden" name="endDate" :value="searchListForm['proModel.endDate']"/>
        <input type="hidden" name="actywId" :value="searchListForm.actywId"/>
        <input type="hidden" name="gnodeId" :value="searchListForm.gnodeId"/>

        <div class="conditions" style="position: relative;">
            <e-condition type="checkbox" v-model="searchListForm['proModel.deuser.office.id']" label="学院"
                         :options="collegeList"
                         :default-props="{label: 'name', value: 'id'}" @change="searchCondition"
                         name="proModel.deuser.office.id"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm['proModel.proCategory']" label="项目类别"
                         :options="proCategories"
                         name="proModel.proCategory" @change="searchCondition"></e-condition>
            <%--<e-condition type="checkbox" v-model="searchListForm['proModel.finalStatus']" label="项目级别"--%>
                         <%--:options="adviceLevel"--%>
                         <%--name="proModel.finalStatus" @change="searchCondition"></e-condition>--%>


        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <%--<button-advice-level--%>
                        <%--v-if="isAdmin != null && isAdmin =='1'"--%>
                        <%--:multiple-selection="projectListMultipleSelection"--%>
                                     <%--@advice-level-submit="adviceLevelSubmit" :advice-level="adviceLevel"></button-advice-level>--%>

                <%--<button-batch-check--%>
                        <%--v-if="isAdmin != null && isAdmin =='1'"--%>
                        <%--:multiple-selection="projectListMultipleSelection" :min="0" :max="100"--%>
                                        <%--@batch-check-submit="batchCheckSubmit"></button-batch-check>--%>
                <shiro:hasPermission name="sys:user:import">
                <button-import v-if="isAdmin != null && isAdmin =='1'" :is-first-menu="isFirstMenu" :actyw-id="searchListForm.actywId" href="/impdata/promodellist?"
                               :gnode-id="searchListForm.gnodeId"></button-import>
                </shiro:hasPermission>

                <button-export v-if="isAdmin != null && isAdmin =='1'" :spilt-prefs="spiltPrefs" :spilt-posts="spiltPosts" label="项目"
                               :search-list-form="searchListForm"></button-export>

                <button-export-file  v-if="isAdmin != null && isAdmin =='1'" :menu-name="menuName" label="项目" :search-list-form="searchListForm" @export-file-start="exportFileStart"></button-export-file>
            </div>
            <div class="search-input">
                <el-date-picker
                        v-model="applyDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        @change="searchCondition"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd"
                        style="width: 270px;">
                </el-date-picker>
                <el-input
                        placeholder="项目名称/编号/负责人/组成员/指导教师"
                        size="mini"
                        name="proModel.queryStr"
                        v-model="searchListForm['proModel.queryStr']"
                        style="width: 300px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchCondition"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="selected-list_len-bar">选择<span class="selected-num">{{listLen}}</span>条数据</div>

    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="projectListFlatten" class="table" ref="multipleTable" v-loading="loading"
                  @sort-change="handleTableSortChange"
                  @selection-change="handleTableSelectionChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="项目信息" align="left" prop="competitionNumber" width="240" sortable="competitionNumber">
                <template slot-scope="scope">
                    <span v-if="scope.row.finalStatus == '0000000264'">{{scope.row.competitionNumber}}</span>
                    <e-set-text v-else-if="scope.row.finalStatus == '0000000265'" :editing.sync="controlEditings[scope.row.id]" :min="5" :max="24"
                                :text="scope.row.pCompetitionNumber" :row="scope.row" @change="handleChangeText"></e-set-text>
                    <e-set-text v-else-if="scope.row.finalStatus == '0000000266'" :editing.sync="controlEditings[scope.row.id]" :min="5" :max="24"
                                :text="scope.row.gCompetitionNumber" :row="scope.row" @change="handleChangeText"></e-set-text>
                    <p class="over-flow-tooltip">
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.pName" placement="bottom">
                          <a :href="frontOrAdmin + '/promodel/proModel/viewForm?id=' + scope.row.id" class="black-a project-name-tooltip">{{scope.row.pName}}</a>
                        </el-tooltip>
                    </p>
                    <p>
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.officeName" placement="bottom">
                            <span class="over-flow-tooltip project-office-tooltip">{{scope.row.officeName}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" min-width="155">
                <template slot-scope="scope">
                    <p>负责人：{{scope.row.deuser.name}}</p>
                    <p class="over-flow-tooltip">成员：<el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.team.entName" placement="bottom"><span class="team-member-tooltip">{{scope.row.team.entName}}</span></el-tooltip></p>
                    <p class="over-flow-tooltip">导师：<el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.team.uName" placement="bottom"><span class="team-member-tooltip">{{scope.row.team.uName}}</span></el-tooltip></p>
                </template>
            </el-table-column>
            <el-table-column label="项目类别" align="center" min-width="85">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="申报日期" align="center" prop="subTime" min-width="110" sortable="subTime">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <%--<el-table-column label="项目级别" align="center" prop="finalStatus" min-width="85">--%>
                <%--<template slot-scope="scope">--%>
                    <%--{{scope.row.finalStatus | selectedFilter(projectLevelEntries)}}--%>
                <%--</template>--%>
            <%--</el-table-column>--%>

            <el-table-column v-if="isScore != null && isScore == '1'" label="评分" align="center" prop="gScore" min-width="85" sortable="gScore">
                <template slot-scope="scope">
                    {{scope.row.gScore}}
                </template>
            </el-table-column>

            <el-table-column label="是否委派" align="center" prop="delegateState" min-width="100">
                <template slot-scope="scope">
                    <span v-if="scope.row.delegateState">{{scope.row.delegateState | selectedFilter(delegateStatesEntries)}}</span>
                </template>
            </el-table-column>

            <el-table-column label="审核结果" align="center" prop="finalResult" min-width="110" sortable="finalResult">
                <template slot-scope="scope">
                    {{scope.row.finalResult}}
                </template>
            </el-table-column>
            <el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <a class="black-a" :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                       target="_blank">
                        <span v-if="scope.row.state == '1'">项目已结项</span>
                        <span v-else>待{{scope.row.auditMap.taskName}}</span>
                    </a>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="60">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.state == '1'" type="text" size="small" disabled>审核</el-button>
                    <div v-else style="display: inline-block">
                        <a v-if="scope.row.auditMap.status == 'todo'"
                           href="javascript: void(0);" @click.stop.prevent="goToAudit(scope.row)">审核</a>
                        <el-button v-else type="text" size="small" disabled>审核</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20" v-show="pageCount">
            <el-pagination
                    size="small"
                    @size-change="handlePaginationSizeChange"
                    background
                    @current-change="handlePaginationPageChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20,50,100]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>

    <export-file-process v-if="isAdmin != null && isAdmin =='1'" ref="exportFileProcess" :visible.sync="EFPVisible" :menu-name="menuName" :gnode-id="searchListForm.gnodeId"></export-file-process>
</div>


<script>

    'use strict';


    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('project_type'))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)}));
//            pageList = pageList.replace(/\n/g, '\\\\n').replace(/\r/g, '\\\\r');
//            pageList = JSON.parse(pageList);

            var isFirstMenu = '${fns:isFirstMenu(actywId,gnodeId)}';
            var spiltPrefs = JSON.parse('${fns:spiltPrefs()}');
            var spiltPosts = JSON.parse('${fns:spiltPosts()}');

            return {
                proCategories: proCategories,
                projectDegrees: projectDegrees,
                colleges: professionals,
                offices: [],
                applyDate: [],
                projectList: pageList,
                isFirstMenu: isFirstMenu === 'true',
                messageInfo: '${message}',
                menuName: '${menuName}',
                isScore:'${isScore}',
                isAdmin:'${isAdmin}',
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                batchCheckIsPass: [
                    {label: '通过', value: '${actYwStatus.status}'}
                ],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    'proModel.queryStr': '',
                    orderBy: '',
                    orderByType: '',
                    'proModel.beginDate': '',
                    'proModel.endDate': '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    'proModel.deuser.office.id': [],
                    'proModel.finalStatus': [],
                    'proModel.proCategory': []
                },
                pageCount: 0,
                projectListMultipleSelection: [],
                loading: false,
                adviceLevel: projectDegrees,
                multipleSelectedId: [],
                controlEditings: {},
                schoolAutoShow:'${autoShow}',
                EFPVisible: true,
                delegateStates:[
                    {label:'已委派',value:'1'},
                    {label:'未委派',value:'0'}
                ]
            }
        },
        computed: {

            delegateStatesEntries:function () {
                return this.getEntries(this.delegateStates);
            },
            projectListFlatten: {
                get: function () {
                    var projectList = this.projectList;
                    var list = [];
                    if (!projectList || projectList.length < 1) return [];
                    for (var i = 0; i < projectList.length; i++) {
                        var proModel = Object.assign({}, projectList[i].proModel);
                        proModel['gCompetitionNumber'] = projectList[i].gCompetitionNumber;
                        proModel['pCompetitionNumber'] = projectList[i].pCompetitionNumber;
                        var id = proModel.id;
                        proModel = Object.assign(proModel, proModel.vars)
                        proModel = Object.assign(proModel, proModel.team)
                        proModel = Object.assign(proModel, proModel.deuser)
                        proModel.id = id;
                        list.push(proModel)
                    }
                    return list;
                }
            },

            officeList: {
                get: function () {
                    return this.getFlattenColleges();
                }
            },

            collegeList: {
                get: function () {
                    return this.officeList.filter(function (item) {
                        return item.grade === '2';
                    });
                }
            },

            projectLevelEntries: {
                get: function () {
                    return this.getEntries(this.adviceLevel)
                }
            },

            actionUrl: {
                get: function () {
                    return location.pathname.replace(this.frontOrAdmin, '');
                }
            },

            listLen: {
                get: function () {
                    return this.projectListMultipleSelection.length;
                }
            },
            proCategoryEntries:{
                get:function(){
                    return this.getEntries(this.proCategories)
                }
            }
        },
        watch: {
            applyDate: function (value) {
                value = value || [];
                this.searchListForm['proModel.beginDate'] = value[0];
                this.searchListForm['proModel.endDate'] = value[1];
                this.searchCondition();
            }
        },
        methods: {
            exportFileStart: function () {
                this.EFPVisible = true;
                //主动去调用
                this.$refs.exportFileProcess.getExpInfo();
            },
            handleTableSortChange: function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? ( row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.searchCondition();
            },
            handleTableSelectionChange: function (val) {
                this.projectListMultipleSelection = val;
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].id);
                }
            },

            handleChangeText: function (obj) {
                var self = this;
                var text = obj.text;
                var row = obj.row;
                var projectList = this.projectList;
                var proModelI = {};

                for (var i = 0; i < projectList.length; i++) {
                    if (projectList[i].proModel.id === row.id) {
                        proModelI = projectList[i].proModel;
                        break;
                    }
                }

                if(row.finalStatus == '0000000265' && text == row.pCompetitionNumber){
                    self.controlEditings[proModelI.id] = false;
                }else if(row.finalStatus == '0000000266' && text == row.gCompetitionNumber){
                    self.controlEditings[proModelI.id] = false;
                }else{
                    var numXhr = self.$axios({
                        method:'POST',
                        url:'/workflow.tlxy/proModelTlxy/ajax/ajaxSaveNum?proModelId=' + row.id + '&num=' + text + '&type=' + row.finalStatus
                    });
                    numXhr.then(function(response){
                        var data = response.data;
                        if(data.ret == '1'){
                            Vue.set(proModelI, 'competitionNumber', text);
                            self.controlEditings[proModelI.id] = false;
                            self.searchCondition();
                        }else if(data.ret == '0'){
                            self.$alert('该项目级别的编号已存在', '提示', {
                                confirmButtonText: '确定',
                                type: 'warning'
                            });
                        }
                    }).catch(function(){

                    })
                }
            },

            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.searchCondition();
            },
            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.searchCondition();
            },

            searchCondition: function () {
                var self = this;
                this.getControlEditings();
                this.loading = true;
                var listXhr = this.$axios({
                    method: 'POST',
                    url: '/cms/ajax/listForm' + "?" + Object.toURLSearchParams(this.searchListForm)
                });
                listXhr.then(function (response) {
                    var page = response.data.page;
                    self.pageCount = page.count;
                    self.projectList = page.list || [];
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },

            adviceLevelSubmit:function(obj){
                var adviceLevelRadioVal = obj.adviceLevelRadioVal;
                var self = this;
                var levelXhr = this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/ajax/batchAuditLevel?ids=' + self.multipleSelectedId.join(',') + '&finalStatus=' + adviceLevelRadioVal
                });
                levelXhr.then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.searchCondition();
                    }
                }).catch(function () {
                    self.$alert('操作异常', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                })
            },
            batchCheckSubmit:function(obj,fn){
                var self = this;
                var checkScore = obj.score;
                var checkXhr = self.$axios({
                    method:'POST',
                    url:'/promodel/proModel/ajax/batchScoreAudit?ids=' + self.multipleSelectedId.join(',') + '&score=' + checkScore + '&gnodeId=' + '${gnodeId}',
                });
                checkXhr.then(function(response){
                    var data = response.data;
                    if(data.ret == '1'){
                        window.parent.sideNavModule.changeUnreadTag(self.searchListForm.actywId);
                        fn;
                        self.searchCondition();
                    }else{
                        self.$alert(data.msg, '提示', {
                            confirmButtonText: '确定',
                            type: 'warning'
                        });
                    }
                }).catch(function(){

                })
            },

            getControlEditings: function () {
                var projectList = this.projectList;
                for (var i = 0; i < projectList.length; i++) {
                    Vue.set(this.controlEditings, projectList[i].proModel.id, false);
                }
            },

            goToAudit: function (row) {
                location.href = this.frontOrAdmin+'/act/task/auditform?gnodeId='+row.auditMap.gnodeId+'&proModelId='+row.auditMap.proModelId+'&pathUrl=${actionUrl}&taskName='+encodeURI(row.auditMap.taskName);
            }
        },
        created: function () {
            if (this.messageInfo) {
                this.$alert(this.messageInfo, '提示', {
                    confirmButtonText: '确定',
                    type: this.messageInfo.indexOf('成功') > -1 ? 'success' : 'fail',
                });
                this.messageInfo = '';
            }

            this.pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            this.getControlEditings();
            window.parent.sideNavModule.changeUnreadTag(this.searchListForm.actywId);
        }
    })


</script>

</body>
</html>