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
            <e-condition type="checkbox" v-model="searchListForm.proCategory" label="项目类别"
                         :options="proCategories"
                         name="proCategory" @change="searchCondition"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <template v-if="isAdmin =='1'">
                    <c:if test="${fns: getTenantIsOpen()}">
                        <el-tooltip v-if="!!provinceActYwId" class="item" effect="dark" popper-class="white"
                                    :disabled="projectListMultipleSelection.length > 0" content="请选择项目"
                                    placement="bottom">
                            <span><el-button size="mini" :disabled="projectListMultipleSelection.length == 0"
                                             type="primary" @click.stop="confirmSendProvince"
                                             icon="iconfont icon-wj-fswj"
                                             >推送到省级</el-button></span>
                        </el-tooltip>
                    </c:if>
                    <button-import :is-first-menu="true" href="/impdata/promodellist?"
                                   :actyw-id="searchListForm.actywId"
                                   :gnode-id="searchListForm.gnodeId" :is-query-menu="true"></button-import>

                    <button-export :spilt-prefs="spiltPrefs" label="项目"
                                   :spilt-posts="spiltPosts"
                                   :search-list-form="searchListForm"></button-export>

                    <el-button size="mini" type="primary"
                               @click.stop.prevent="goToCertAssign" icon="iconfont icon-guanlizhengshu">证书下发
                    </el-button>

                    <el-tooltip class="item" content="请选择需要发布的优秀项目" :disabled="projectListMultipleSelection.length > 0"
                                popper-class="white" placement="bottom">
                        <span>
                    <el-button size="mini" type="primary"
                               @click.stop.prevent="publishExPro"
                               icon="iconfont icon-icon-project"
                               :disabled="projectListMultipleSelection.length < 1">发布优秀项目
                    </el-button>
                            </span>
                        </el-tooltip>
                    <el-tooltip class="item" content="请选择需要删除的项目" :disabled="projectListMultipleSelection.length > 0"
                                popper-class="white" placement="bottom">
                        <span>
                    <el-button size="mini" type="primary"
                               @click.stop.prevent="deleteProject(multipleSelectedId.join(','))"
                               :disabled="projectListMultipleSelection.length < 1"><i class="iconfont icon-delete"></i>批量删除
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
                            <a :href="ctx + '/promodel/proModel/viewForm?id=' + scope.row.id"
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
            <el-table-column label="成员信息" align="left" min-width="150">
                <template slot-scope="scope">
                    <p>负责人：{{scope.row.deuser.name || '-'}}</p>
                    <p class="over-flow-tooltip">成员：
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.team.entName"
                                    placement="bottom">
                            <span class="team-member-tooltip">{{scope.row.team.entName || '-'}}</span></el-tooltip>
                    </p>
                    <p class="over-flow-tooltip">导师：
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.team.uName"
                                    placement="bottom"><span
                                class="team-member-tooltip">{{scope.row.team.uName || '-'}}</span></el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="项目类别" align="center" min-width="100">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="申报日期" align="center" prop="sub_time" min-width="110" sortable="sub_time">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>
            <el-table-column label="证书" align="center" min-width="85">
                <template slot-scope="scope">
                    <p v-for="item in scope.row.scis" class="over-flow-tooltip">
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="item.name"
                                    placement="bottom">
                            <span class="task-cert-preview"
                                  @click.stop.prevent="taskCertPreview(item.id)">{{item.name || '-'}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="审核结果" align="center" prop="final_result" min-width="110">
                <template slot-scope="scope">
                    {{scope.row.finalResult || '-'}}
                </template>
            </el-table-column>
            <el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <template v-if="scope.row.state == '1'">
                        <a class="black-a"
                           :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank">项目已结项</a>
                    </template>
                    <template v-else>
                        <a class="black-a"
                           :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank">{{scope.row.auditMap ? '待' + scope.row.auditMap.taskName : ''}}</a>
                    </template>
                </template>
            </el-table-column>
            <c:if test="${isAdmin eq 1}">
                <el-table-column label="操作" align="center" min-width="120">
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

    <el-dialog title="证书预览" :visible.sync="dialogCertVisible" width="60%">
        <el-carousel indicator-position="outside" ref="elCarousel" height="400px">
            <el-carousel-item v-for="item in previewCertImgSrc" :key="item.id">
                <div class="preview-cert-content">
                    <img :src="item.imgUrl" alt="证书" v-auto-img="bannerScale">
                </div>
            </el-carousel-item>
        </el-carousel>
    </el-dialog>

</div>

<script>

    'use strict';


    Vue.directive('auto-img', {
        componentUpdated: function (element, binding) {
            var ratio = binding.value;
            element.onload = function () {
                var imgScale = element.naturalWidth / element.naturalHeight;
                element.style.width = imgScale > ratio ? '100%' : 'auto';
                element.style.height = imgScale >= ratio ? 'auto' : '100%'
            }
        },
        unbind: function (element) {
            element.onload = null;
        }
    })


    new Vue({
        el: '#app',
        data: function () {
            var proCategories = JSON.parse('${fns: toJson(fns:getDictList("project_type"))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            var professionals = JSON.parse(JSON.stringify(${fns: toJson(fns: findColleges())})) || [];
            var spiltPrefs = JSON.parse('${fns:spiltPrefs()}');
            var spiltPosts = JSON.parse('${fns:spiltPosts()}');
            var pageList = JSON.parse(JSON.stringify(${fns: toJson(page.list)})) || [];
            var pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            return {
                proCategories: proCategories,
                adviceLevel: projectDegrees,
                colleges: professionals,
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
                    proCategory: [],
                    'deuser.office.id': []
                },
                loading: false,
                applyDate: [],
                multipleSelectedId: [],
                isAdmin: '${isAdmin}',
                dialogCertVisible: false,
                previewCertImgSrc: [],
                bannerScale: 1,
                provinceActYwId: ''
            }
        },
        computed: {
            projectLevelEntries: {
                get: function () {
                    return this.getEntries(this.adviceLevel)
                }
            },


            listLen: {
                get: function () {
                    return this.projectListMultipleSelection.length;
                }
            },
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
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

            confirmSendProvince: function () {
                var self = this;
                this.$confirm("确认要推送项目到省级吗？", '提示', {
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
                location.href = this.ctx + '/promodel/proModel/projectEdit?id=' + row.id + '&secondName=' + encodeURI("变更")
            },

            goToCertAssign: function () {
                location.href = this.ctx + '/certMake/list?actywId=' + this.searchListForm.actywId;
            },

            taskCertPreview: function (id) {
                var self = this;
                this.previewCertImgSrc = [];
                this.$axios({
                    method: 'POST',
                    url: '/cert/getCertIns?certinsid=' + id
                }).then(function (response) {
                    self.previewCertImgSrc = response.data;
                    self.certImgCenter();
                    self.dialogCertVisible = true;
                }).catch(function () {

                })

            },

            certImgCenter: function () {
                var self = this;
                var bannerWidth = 0;
                var bannerHeight = 400;
                self.$nextTick(function () {
                    bannerWidth = self.$refs.elCarousel.$el.offsetWidth;
                    self.bannerScale = bannerWidth / bannerHeight;
                });
            },


            publishExPro: function () {
                var self = this;
                this.$confirm('确定发布所选项目到门户网站？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.publishExProAxios();
                })
            },

            publishExProAxios: function () {
                var self = this;
                var ids = this.multipleSelectedId.join(',');
                this.$axios.get('/cms/cmsArticle/excellent/projectPublish', {params: {ids: ids}}).then(function (response) {
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
            }

        },
        created: function () {

        },
        mounted: function () {
            this.getProvinceId();
        }
    })

</script>
</body>
</html>