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
        <div class="conditions" style="position: relative;">
            <e-condition type="checkbox" v-model="searchListForm['deuser.office.id']" label="学院"
                         :options="colleges"
                         :default-props="{label: 'name', value: 'id'}" @change="searchCondition"
                         name="deuser.office.id"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm.proCategory" label="大赛类别"
                         :options="proCategories"
                         name="proCategory" @change="searchCondition"></e-condition>
            <e-condition type="radio" v-model="searchListForm.gcTrack" label="参赛赛道"
                         :options="tracks"
                         name="gcTrack" @change="handleChangeGcTrack"></e-condition>
            <e-condition v-show="!!searchListForm.gcTrack" type="radio" v-model="searchListForm.level" label="参赛组别"
                         :options="gContestLevelList"
                         name="level" @change="searchCondition"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">

                <shiro:hasPermission name="sys:user:import">
                    <button-import v-if="isAdmin =='1'" :is-first-menu="isFirstMenu"
                                   :actyw-id="searchListForm.actywId" href="/impdata/promodelgcontestlist?"
                                   :gnode-id="searchListForm.gnodeId"></button-import>
                </shiro:hasPermission>

                <c:if test="${fns: getTenantIsOpen()}">
                    <el-tooltip v-if="!!provinceActYwId" class="item" effect="dark" popper-class="white"
                                :disabled="projectListMultipleSelection.length > 0" content="请选择项目"
                                placement="bottom">
                            <span><el-button size="mini" :disabled="projectListMultipleSelection.length == 0"
                                             icon="iconfont icon-wj-fswj"
                                             type="primary" @click.stop="confirmSendProvince">推送到省级</el-button></span>
                    </el-tooltip>
                </c:if>

                <button-export :spilt-prefs="spiltPrefs" :spilt-posts="spiltPosts" label="大赛"
                               :search-list-form="searchListForm"></button-export>

                <button-export-file :menu-name="menuName" label="大赛" :search-list-form="searchListForm"
                                    @export-file-start="exportFileStart"></button-export-file>
            </div>
            <div class="search-input">
                <el-date-picker
                        v-model="applyDate"
                        type="daterange"
                        size="mini"
                        align="right"
                        @change="searchCondition"
                        :default-time="defaultTime"
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
                    <project-info-column-gc-school
                            :key="scope.row.id"
                            :row="scope.row"
                            :to="ctx + '/promodel/proModel/viewForm?id='+ scope.row.id">
                    </project-info-column-gc-school>
                </template>
            </el-table-column>
            <el-table-column label="团队成员" align="left">
                <template slot-scope="scope">
                    <project-team-column-school :row="scope.row" :key="scope.row.id"></project-team-column-school>
                </template>
            </el-table-column>
            <el-table-column label="参赛组别" align="center" min-width="70">
                <template slot-scope="scope">
                    {{scope.row.level | selectedFilter(gContestLevelEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="大赛类别" align="center" min-width="85">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries)}}
                </template>
            </el-table-column>


            <el-table-column label="参赛赛道" align="center" min-width="70">
                <template slot-scope="scope">
                    {{scope.row.gcTrack | selectedFilter(gcTrackEntries)}}
                </template>
            </el-table-column>
            <%--<el-table-column label="审核结果" align="center" prop="final_result" min-width="110" sortable="final_result">
                <template slot-scope="scope">
                    {{scope.row.finalResult || '-'}}
                </template>
            </el-table-column>--%>

            <el-table-column label="状态" align="center"  min-width="85">
                <template slot-scope="scope">
                    <span>{{scope.row.isSend === '1' ? '已': '待'}}</span>推荐省赛
                </template>
            </el-table-column>

            <el-table-column label="申报日期" align="center" prop="sub_time" min-width="85" sortable="sub_time">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>

            <el-table-column label="排名" align="center" prop="ranking" min-width="80">
                <template slot-scope="scope">
                    <el-input size="mini" v-model="scope.row.tempRanking" :disabled="!!scope.row.ranking" maxlength="64"
                              @blur="rankBlur(scope.row)">
                        <el-button v-if="!!scope.row.ranking" slot="append" @click.stop="scope.row.ranking = ''" icon="el-icon-edit"></el-button>
                    </el-input>
                </template>
            </el-table-column>


            <%--<el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <a class="black-a" :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                       target="_blank">
                        <span v-if="scope.row.state == '1'">项目已结项</span>
                        <span v-else>{{scope.row.auditMap ? '待' + scope.row.auditMap.taskName : ''}}</span>
                    </a>
                </template>
            </el-table-column>--%>
            <%-- <el-table-column label="操作" align="center" min-width="60">
                 <template slot-scope="scope">
                     <el-button v-if="scope.row.state == '1'" type="text" size="small" disabled>审核</el-button>
                     <div v-else style="display: inline-block">
                         <a v-if="scope.row.auditMap.status == 'todo'"
                            href="javascript: void(0);" @click.stop.prevent="goToAudit(scope.row)">审核</a>
                         <el-button v-else type="text" size="small" disabled>审核</el-button>
                     </div>
                 </template>
             </el-table-column>--%>
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

    <export-file-process v-if="isAdmin =='1'" ref="exportFileProcess" :visible.sync="EFPVisible"
                         :menu-name="menuName" :gnode-id="searchListForm.gnodeId"></export-file-process>
</div>


<script>

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)})) || [];
            var isFirstMenu = '${fns:isFirstMenu(actywId,gnodeId)}';
            var spiltPrefs = JSON.parse(JSON.stringify(${fns:spiltPrefs()}));
            var spiltPosts = JSON.parse(JSON.stringify(${fns:spiltPosts()}));
            var tracks = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            var firstTrack = tracks.length > 0 ? tracks[0].value : '';
            pageList.forEach(function (item) {
                item.tempRanking = item.ranking
            })
            return {
                proCategories: [],
                gContestLevel: [],
                officeList: [],
                technologyFields: [],
                schoolTypeList: [],
                applyDate: [],
                projectList: pageList,
                isFirstMenu: isFirstMenu === 'true',
                menuName: '${menuName}',
                isScore: '${isScore}',
                isGate: '${isGate}',
                isAdmin: '${isAdmin}',
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    queryStr: '',
                    beginDate: '',
                    endDate: '',
                    gcTrack: firstTrack,
                    level: '',
                    proCategory: [],
                    'deuser.office.id': []
                },
                pageCount: 0,
                projectListMultipleSelection: [],
                loading: false,
                multipleSelectedId: [],
                EFPVisible: true,
                defaultTime: ['00:00:00', '23:59:59'],
                tracks: tracks,
                provinceActYwId: '',
            }
        },
        computed: {
            colleges: function () {
              return this.officeList.filter(function (item) {
                  return item.grade === '2';
              })
            },

            technologyFieldsEntity: function () {
                return this.getEntries(this.technologyFields)
            },

            schoolTypeEntity: function () {
                return this.getEntries(this.schoolTypeList, {label: 'name', value: 'key'})
            },

            listLen: function () {
                return this.projectListMultipleSelection.length;

            },
            proCategoryEntries: function () {
                return this.getEntries(this.proCategories)

            },
            gContestLevelEntries: function () {
                return this.getEntries(this.gContestLevel)

            },
            gcTrackEntries: function () {
                return this.getEntries(this.tracks)
            },
            gContestLevelList: function () {
                var gcTrack = this.searchListForm.gcTrack;
                return this.gContestLevel.filter(function (item) {
                    return  item.type === gcTrack
                })
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
            handleChangeGcTrack: function (value) {
                if(!value){
                    this.searchListForm.level = ''
                }
                this.searchCondition();
            },
            confirmSendProvince: function () {
                var self = this;
                this.$confirm("确认要推送项目吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.sendProvince()
                }).catch(function () {

                })
            },

            getProvinceId: function () {
                var self = this;
                this.$axios.get('/promodel/proModel/getProvinceActYwId', {params: {actywId: this.searchListForm.actywId}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.provinceActYwId = data.data;
                    }
                })
            },

            sendProvince: function () {
                var self = this;
                var projectList = this.projectListMultipleSelection.map(function (item) {
                    return {
                        id: item.id,
                        isSend: item.isSend,
                        subTime: item.subTime
                    };
                });
                this.$axios.post('/promodel/proModel/sendProToProvince/' + this.provinceActYwId, projectList).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('项目推送成功', '提示', {
                            type: 'success'
                        })
                        self.searchCondition();
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            rankBlur: function (row) {
                var ranking = row.tempRanking;
                if (ranking) {
                    if (/^[1-9]{1}\d*$/.test(ranking)) {
                        this.updateRank(row)
                    } else {
                        this.$message({
                            type: 'error',
                            duration: 1000,
                            message: '请输入正整数'
                        })
                    }
                }
            },

            updateRank: function (row) {
                var self = this;
                this.loading = true;

                this.$axios.post('/promodel/proModel/updateByProId', {
                    id: row.id,
                    ranking: row.tempRanking
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.searchCondition();
                        self.changeSideTodoNum(self.searchListForm.actywId);
                    } else {
                        self.$message({
                            type: 'error',
                            duration: 1000,
                            message: data.msg
                        });
                        self.loading = false;
                    }
                })
            },

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
                        var projectList = page.list || [];
                        projectList.forEach(function (item) {
                            item.tempRanking = item.ranking
                        });
                        self.projectList = projectList;
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },
            goToAudit: function (row) {
                location.href = this.ctx + '/act/task/auditform?gnodeId=' + row.auditMap.gnodeId + '&proModelId=' + row.auditMap.proModelId + '&pathUrl=${actionUrl}&taskName=' + encodeURI(row.auditMap.taskName);
            },
            getDictListLevels: function () {
                var self = this;
                this.tracks.forEach(function (item) {
                    self.$axios.get('/sys/dict/getDictList?type=' + item.value).then(function (response) {
                        var data = response.data || [];
                        self.gContestLevel = self.gContestLevel.concat(data);
                    })
                })
            },
            getDictLists: function () {
                var dicts = {
                    'competition_net_type': 'proCategories',
                    'technology_field': 'technologyFields'
                };
                this.getBatchDictList(dicts)
            },

            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        self.officeList = data.data || []
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
        },
        created: function () {
            this.pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            this.changeSideTodoNum(this.searchListForm.actywId);
        },
        mounted: function () {
            this.getDictListLevels();
            this.getDictLists();
            this.getOfficeList();
            this.getSchoolTypeList();
            this.getProvinceId();
        }
    })


</script>

</body>
</html>