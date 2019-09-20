<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="clearfix">

        <div class="conditions">
            <e-condition type="checkbox" v-model="searchListForm['deuser.office.id']" label="学院"
                         :options="colleges"
                         :default-props="{label: 'name', value: 'id'}" @change="searchCondition"
                         name="proModel.deuser.office.id"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm.proCategory" label="项目类别"
                         :options="proCategories"
                         name="proCategory" @change="searchCondition"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">

                <shiro:hasPermission name="sys:user:import">
                    <button-import v-if="isAdmin =='1'" :is-first-menu="isFirstMenu" :actyw-id="searchListForm.actywId"
                                   href="/impdata/promodellist?"
                                   :gnode-id="searchListForm.gnodeId"></button-import>
                </shiro:hasPermission>

                <button-export :spilt-prefs="spiltPrefs" :spilt-posts="spiltPosts" label="项目"
                               :search-list-form="searchListForm"></button-export>

                <button-export-file :menu-name="menuName" label="项目" :search-list-form="searchListForm"
                                    @export-file-start="exportFileStart"></button-export-file>
            </div>
            <div class="search-input">
                <input type="text" style="display: none">
                <el-date-picker
                        v-model="applyDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        :default-time="defaultTime"
                        @change="searchCondition"
                        unlink-panels
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        style="width: 270px;">
                </el-date-picker>
                <el-input
                        placeholder="项目名称/编号/负责人/组成员/导师"
                        size="mini"
                        name="queryStr"
                        @keyup.enter.native="searchCondition"
                        v-model="searchListForm.queryStr"
                        style="width: 300px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchCondition"></el-button>
                </el-input>
            </div>
        </div>
    </div>

    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="projectList" class="table" ref="multipleTable" v-loading="loading"
                  @sort-change="handleTableSortChange"
                  @selection-change="handleTableSelectionChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="项目信息" align="left" prop="competition_number" width="240"
                             sortable="competition_number">
                <template slot-scope="scope">
                    <span>{{scope.row.competitionNumber || '-'}}</span>
                    <p class="over-flow-tooltip">
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.pName"
                                    placement="bottom">
                            <a :href="frontOrAdmin + '/promodel/proModel/viewForm?id=' + scope.row.id"
                               class="black-a project-name-tooltip">{{scope.row.pName || '-'}}</a>
                        </el-tooltip>
                    </p>
                    <p>
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.officeName"
                                    placement="bottom">
                            <span class="over-flow-tooltip project-office-tooltip">{{scope.row.officeName || '-'}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" :min-width="isScore != null && isScore == '1'?155:170">
                <template slot-scope="scope">
                    <p>负责人：{{scope.row.deuser.name || '-'}}</p>
                    <p v-if="!!scope.row.team" class="over-flow-tooltip">成员：
                        <el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.team.entName"
                                    placement="bottom"><span class="team-member-tooltip">{{scope.row.team.entName || '-'}}</span>
                        </el-tooltip>
                    </p>
                    <p v-if="!!scope.row.team" class="over-flow-tooltip">导师：
                        <el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.team.uName"
                                    placement="bottom"><span
                                class="team-member-tooltip">{{scope.row.team.uName || '-'}}</span></el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="项目类别" align="center" min-width="85">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="申报日期" align="center" prop="sub_time" min-width="110" sortable="subTime">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>


            <el-table-column label="审核结果" align="center" prop="final_result" min-width="110" sortable="final_result">
                <template slot-scope="scope">
                    {{scope.row.finalResult || '-'}}
                </template>
            </el-table-column>
            <el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <a class="black-a"
                       :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                       target="_blank">
                        <span v-if="scope.row.state == '1'">项目已结项</span>
                        <span v-else>{{scope.row.auditMap ? '待' + scope.row.auditMap.taskName : ''}}</span>
                    </a>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="60">
                <template slot-scope="scope">
                    <el-tooltip v-if="scope.row.state != '1' &&  scope.row.auditMap.status == 'todo'" class="item"
                                :content="scope.row.auditMap.taskName" popper-class="white" placement="bottom">
                                  <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-daishenhe"
                                           @click.stop="goToAudit(scope.row)"></el-button>
                                  </span>
                    </el-tooltip>
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

    <export-file-process v-if="isAdmin =='1'" ref="exportFileProcess" :visible.sync="EFPVisible" :menu-name="menuName"
                         :gnode-id="searchListForm.gnodeId"></export-file-process>
</div>


<script>

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('project_type'))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var professionals = JSON.parse(JSON.stringify(${fns: toJson(fns: findColleges())})) || [];
            var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)})) || [];
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
                isScore: '${isScore}',
                isGate: '${isGate}',
                isAdmin: '${isAdmin}',
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                batchCheckIsPass: [
                    {label: '通过', value: '${actYwStatus.status}'}
                ],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    queryStr: '',
                    orderBy: '',
                    orderByType: '',
                    beginDate: '',
                    endDate: '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    proCategory: [],
                    'deuser.office.id': []
                },
                pageCount: 0,
                projectListMultipleSelection: [],
                loading: false,
                multipleSelectedId: [],
                schoolAutoShow: '${autoShow}',
                EFPVisible: true,
                defaultTime: ['00:00:00', '23:59:59']
            }
        },
        computed: {
            proCategoryEntries: function () {
                return this.getEntries(this.proCategories)
            }
        },
        watch: {
            applyDate: function (value) {
                value = value || [];
                this.searchListForm.beginDate = value[0];
                this.searchListForm.endDate = value[1];
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
                var searchListForm = JSON.parse(JSON.stringify(this.searchListForm));
                this.loading = true;
                searchListForm.proCategory = searchListForm.proCategory.join(',');
                searchListForm['deuser.office.id'] = searchListForm['deuser.office.id'].join(',');
                this.$axios.get('/cms/ajax/listForm', {params: searchListForm}).then(function (response) {
                    var data = response.data;
                    var page;
                    if (data.page) {
                        page = data.page;
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        self.projectList = page.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },
            goToAudit: function (row) {
                location.href = this.ctx + '/act/task/auditform?gnodeId=' + row.auditMap.gnodeId + '&proModelId=' + row.auditMap.proModelId + '&pathUrl=${actionUrl}&taskName=' + encodeURI(row.auditMap.taskName);
            }

        },
        created: function () {
            if (this.messageInfo) {
                this.$alert(this.messageInfo, '提示', {
                    confirmButtonText: '确定',
                    type: this.messageInfo.indexOf('成功') > -1 ? 'success' : 'fail'
                });
                this.messageInfo = '';
            }

            this.pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            this.changeSideTodoNum(this.searchListForm.actywId);

        }
    })


</script>

</body>
</html>