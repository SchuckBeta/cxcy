<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>


<div id="app" v-show="pageLoad" class="container-fluid" style="display: none">
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
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
                <template v-if="isAdmin =='1'">
                    <c:if test="${fns: getTenantIsOpen()}">
                        <el-tooltip v-if="!!provinceActYwId" class="item" effect="dark" popper-class="white"
                                    :disabled="projectListMultipleSelection.length > 0" content="请选择项目"
                                    placement="bottom">
                            <span><el-button size="mini" :disabled="projectListMultipleSelection.length == 0"
                                             icon="iconfont icon-wj-fswj"
                                             type="primary" @click.stop="confirmSendProvince">推送到省级</el-button></span>
                        </el-tooltip>
                    </c:if>

                    <button-import :is-first-menu="true" href="/impdata/promodelgcontestlist?"
                                   :actyw-id="searchListForm.actywId"
                                   :gnode-id="searchListForm.gnodeId" :is-query-menu="true"></button-import>

                    <button-export :spilt-prefs="spiltPrefs" label="大赛"
                                   :spilt-posts="spiltPosts"
                                   :search-list-form="searchListForm"></button-export>

                    <el-tooltip class="item" content="请选择需要发布的优秀项目" :disabled="projectListMultipleSelection.length > 0"
                                popper-class="white" placement="bottom">
                        <span>
                            <el-button size="mini" type="primary"
                                       @click.stop="publishExPro"
                                       :disabled="projectListMultipleSelection.length == 0"
                                       icon="iconfont icon-icon-project">发布优秀项目
                            </el-button>
                        </span>
                    </el-tooltip>
                    <el-tooltip class="item" content="请选择需要删除的项目" :disabled="projectListMultipleSelection.length > 0"
                                popper-class="white" placement="bottom">
                        <span>
                            <el-button size="mini" type="primary"
                                       icon="el-icon-delete"
                                       @click.stop="deleteProject(multipleSelectedId.join(','))"
                                       :disabled="projectListMultipleSelection.length === 0">批量删除
                        </el-button>
                        </span>
                    </el-tooltip>
                </template>

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
                    <project-info-column-gc-school
                            :key="scope.row.id"
                            :row="scope.row"
                            :to="ctx + '/promodel/proModel/viewForm?id='+ scope.row.id">
                    </project-info-column-gc-school>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" min-width="150">
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
            <el-table-column label="申报日期" align="center" prop="sub_time" min-width="85" sortable="sub_time">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="状态" align="center" prop="isSend" min-width="85">
                <template slot-scope="scope">
                    <span>{{scope.row.isSend === '1' ? '已': '待'}}</span>推荐省赛
                </template>
            </el-table-column>
            <el-table-column label="排名" align="center" min-width="50">
                <template slot-scope="scope">
                    {{scope.row.ranking || '-'}}
                </template>
            </el-table-column>
            <c:if test="${isAdmin eq '1'}">
                <el-table-column label="操作" align="center">
                    <template slot-scope="scope">
                        <shiro:hasPermission name="promodel:promodel:modify">

                            <el-tooltip class="item" content="进度跟踪" popper-class="white" placement="bottom">
                                  <span>
                                <el-button class="btn-icon-font" type="text" icon="iconfont icon-renwutiaodugenzong"
                                           @click.stop="goToProcessTrack(scope.row)">
                                </el-button>
                                      </span>
                            </el-tooltip>
                            <el-tooltip class="item" content="编辑" popper-class="white" placement="bottom">
                                <span>
                                <el-button class="btn-icon-font" type="text" icon="el-icon-edit"
                                           @click.stop="goToChangeCharge(scope.row)">
                                </el-button>
                                    </span>
                            </el-tooltip>
                        </shiro:hasPermission>
                        <br>
                        <button-audit-record :row="scope.row"></button-audit-record>
                        <shiro:hasPermission name="promodel:promodel:modify">
                            <el-tooltip class="item" content="删除" popper-class="white" placement="bottom">
                                 <span>
                                <el-button class="btn-icon-font" type="text" icon="el-icon-delete"
                                           @click.stop="deleteProject(scope.row.id)">
                                </el-button>
                                     </span>
                            </el-tooltip>
                        </shiro:hasPermission>
                    </template>
                </el-table-column>
            </c:if>
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


</div>

<script>

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var spiltPrefs = JSON.parse(JSON.stringify(${fns:spiltPrefs()}));
            var spiltPosts = JSON.parse(JSON.stringify(${fns:spiltPosts()}));
            var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)}));
            var pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            var tracks = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            var firstTrack = tracks.length > 0 ? tracks[0].value : '';
            return {
                proCategories: [],
                gContestLevel: [],
                officeList: [],
                technologyFields: [],
                schoolTypeList: [],
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                projectList: pageList,
                projectListMultipleSelection: [],
                pageCount: pageCount,
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    beginDate: '',
                    endDate: '',
                    queryStr: '',
                    gcTrack: firstTrack,
                    level: '',
                    proCategory: [],
                    'deuser.office.id': []
                },
                loading: false,
                applyDate: [],
                multipleSelectedId: [],
                isAdmin: '${isAdmin}',
                tracks: tracks,
                provinceActYwId: ''
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
                    return item.type === gcTrack
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
                if (!value) {
                    this.searchListForm.level = ''
                }
                this.getQueryMenuList();
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
                        self.getQueryMenuList();
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
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
                this.getQueryMenuList();
            },

            goToProcessTrack: function (row) {
                location.href = this.ctx + '/promodel/proModel/process?id=' + row.id
            },

            goToChangeCharge: function (row) {
                location.href = this.ctx + '/promodel/proModel/gcontestEdit?id=' + row.id + '&secondName=' + encodeURI("变更")
            },


            publishExPro: function () {
                var self = this;
                this.$confirm('确定发布所选项目到门户网站？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.publishExProAxios()
                })
            },

            publishExProAxios: function () {
                var self = this;
                var ids = this.multipleSelectedId.join(',');
                this.$axios.get('/cms/cmsArticle/excellent/gcontestPublish', {params: {ids: ids}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('发布成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            deleteProject: function (ids) {
                var self = this;
                this.$confirm('此操作将永久删除文件, 是否继续?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.deleteProjectAxios(ids);
                })
            },

            deleteProjectAxios: function (ids) {
                var self = this;
                this.$axios.get('/promodel/proModel/ajaxPromodelDelete', {params: {ids: ids}}).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.searchCondition();
                        self.$alert('删除成功', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                })
            },

            getQueryMenuList: function () {
                var self = this;
                var searchListForm = JSON.parse(JSON.stringify(this.searchListForm));
                this.loading = true;
                searchListForm.proCategory = searchListForm.proCategory.join(',');
                searchListForm['deuser.office.id'] = searchListForm['deuser.office.id'].join(',');
                this.$axios.get('/cms/ajax/ajaxQueryMenuList', {params: searchListForm}).then(function (response) {
                    var data = response.data;
                    var page;
                    if (data.page) {
                        page = data.page;
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        self.projectList = page.list || [];
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                })
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
                    if (data.status === 1) {
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
        mounted: function () {
            this.getDictListLevels();
            this.getProvinceId();
            this.getDictLists();
            this.getOfficeList();
            this.getSchoolTypeList();
        }
    })

</script>
</body>
</html>