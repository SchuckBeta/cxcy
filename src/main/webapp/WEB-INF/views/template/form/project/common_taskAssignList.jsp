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

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid" style="margin-bottom:40px;">
    <edit-bar></edit-bar>
    <el-form :model="searchListForm" ref="searchListForm" size="mini" autocomplete="off" ref="searchListForm">
        <input type="hidden" name="pageNo" :value="searchListForm.pageNo"/>
        <input type="hidden" name="pageSize" :value="searchListForm.pageSize"/>
        <input type="hidden" name="orderBy" :value="searchListForm.orderBy"/>
        <input type="hidden" name="orderByType" :value="searchListForm.orderByType"/>
        <input type="hidden" name="beginDate" :value="searchListForm.beginDate"/>
        <input type="hidden" name="endDate" :value="searchListForm.endDate"/>
        <input type="hidden" name="actywId" :value="searchListForm.actywId"/>
        <input type="hidden" name="gnodeId" :value="searchListForm.aGnodeId"/>

        <div class="conditions" style="position: relative;">
            <e-condition type="checkbox" v-model="searchListForm['deuser.office.id']" label="学院"
                         :options="collegeList"
                         :default-props="{label: 'name', value: 'id'}" @change="searchCondition"
                         name="deuser.office.id"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm.proCategory" label="项目类别"
                         :options="proCategories"
                         name="proCategory" @change="searchCondition"></e-condition>
            <e-condition type="radio" v-model="searchListForm.hasAssigns" label="指派状态"
                         :options="assignState"
                         name="hasAssigns" @change="searchCondition"></e-condition>
            <e-condition  :is-show-all="false" type="radio" v-model="searchListForm.gnodeId" label="指派节点"
                          :options="assignNodes" :default-props="{label:'name',value:'id'}"
                          name="gnodeId" @change="searchCondition"></e-condition>


        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <el-button type="primary" :disabled="projectListMultipleSelection.length < 1" size="mini" @click.stop.prevent="toTaskAssign">指派</el-button>
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
    </el-form>

    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="projectList" class="table" ref="multipleTable" v-loading="loading"
                  @sort-change="handleTableSortChange"
                  @selection-change="handleTableSelectionChange">
            <el-table-column
                    type="selection"
                    width="45">
            </el-table-column>
            <el-table-column label="项目信息" align="left" prop="competition_number" width="240" sortable="competition_number">
                <template slot-scope="scope">
                    <span>{{scope.row.competitionNumber || '-'}}</span>
                    <p class="over-flow-tooltip">
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.pName" placement="bottom">
                          <a :href="frontOrAdmin + '/promodel/proModel/viewForm?id=' + scope.row.id" class="black-a project-name-tooltip">{{scope.row.pName || '-'}}</a>
                        </el-tooltip>
                    </p>
                    <p>
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.officeName" placement="bottom">
                            <span class="over-flow-tooltip project-office-tooltip">{{scope.row.officeName || '-'}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" min-width="140">
                <template slot-scope="scope">
                    <p>负责人：{{scope.row.deuser.name || '-'}}</p>
                    <p class="over-flow-tooltip">成员：<el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.team.entName" placement="bottom"><span class="team-member-tooltip">{{scope.row.team.entName || '-'}}</span></el-tooltip></p>
                    <p class="over-flow-tooltip">导师：<el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.team.uName" placement="bottom"><span class="team-member-tooltip">{{scope.row.team.uName || '-'}}</span></el-tooltip></p>
                </template>
            </el-table-column>
            <el-table-column label="项目类别" align="center" min-width="85">
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
                    <el-tooltip class="item" effect="dark" popper-class="white" :content="item.name" placement="bottom">
                        <span class="task-cert-preview" @click.stop.prevent="taskCertPreview(item.id)">{{item.name}}</span>
                    </el-tooltip>
                    </p>
                </template>
            </el-table-column>

            <el-table-column label="指派状态" align="center" min-width="85">
                <template slot-scope="scope">
                    <span v-if="scope.row.taskAssigns">已指派</span>
                    <span v-else>未指派</span>
                </template>
            </el-table-column>

            <el-table-column label="指派人员" align="center" prop="taskAssigns" min-width="85">
                <template slot-scope="scope">
                    <p class="over-flow-tooltip"><el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.taskAssigns" placement="bottom"><span class="task-assign-tooltip">{{scope.row.taskAssigns || '-'}}</span></el-tooltip></p>
                </template>
            </el-table-column>

            <el-table-column label="审核状态" align="center" min-width="120">
                <template slot-scope="scope">
                    <template v-if="scope.row.state == '1' && !scope.row.procInsId">
                        <span>项目已结项</span>
                    </template>
                    <template v-else>
                        <a v-if="scope.row.state == '1'" :href="frontOrAdmin+'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                           target="_blank" class="black-a">项目已结项</a>
                        <a v-else :href="frontOrAdmin + '/actyw/actYwGnode/designView?groupId=' + scope.row.actYw.groupId + '&proInsId=' + scope.row.procInsId"
                           target="_blank" class="black-a">
                            {{scope.row.auditMap ? '待' + scope.row.auditMap.taskName : ''}}
                        </a>
                    </template>
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


    Vue.directive('auto-img',{
        componentUpdated: function (element, binding) {
            var ratio = binding.value;
            element.onload =  function () {
                var imgScale = element.naturalWidth/element.naturalHeight;
                element.style.width = imgScale > ratio ? '100%' : 'auto';
                element.style.height =imgScale >= ratio ? 'auto' : '100%'
            }
        },
        unbind: function (element) {
            element.onload = null;
        }
    });


    new Vue({
        el: '#app',
        mixins: [Vue.collegesMixin],
        data: function () {
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('project_type'))}');
            var professionals = JSON.parse('${fns: getOfficeListJson()}') || [];
            var assignNodes = JSON.parse('${fns: toJson(fns:getAssignNodes(actywId))}') || [];
            var firstAssignNode = assignNodes[0].id;
            var pageList = ${fns: toJson(page.list)};
            var isFirstMenu = '${fns:isFirstMenu(actywId,gnodeId)}';

            return {
                proCategories: proCategories,
                colleges: professionals,
                assignNodes:assignNodes,
                offices: [],
                applyDate: [],
                projectList: pageList,
                isFirstMenu: isFirstMenu === 'true',
                messageInfo: '${message}',
                menuName: '${menuName}',
                isScore:'${isScore}',
                isGate:'${isGate}',
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    actywId: '${actywId}',
                    aGnodeId: '${gnodeId}',
                    queryStr: '',
                    beginDate: '',
                    endDate: '',
                    proCategory: [],
                    gnodeId: firstAssignNode,
                    hasAssigns:[],
                    'deuser.office.id': []
                },
                pageCount: 0,
                projectListMultipleSelection: [],
                loading: false,
                multipleSelectedId: [],
                schoolAutoShow:'${autoShow}',
                assignState:[
                    {label:'未指派',value:'0'},
                    {label:'已指派',value:'1'}
                ],
                dialogCertVisible:false,
                previewCertImgSrc:[],
                bannerScale: 1
            }
        },
        computed: {

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
                this.searchListForm.beginDate = value[0];
                this.searchListForm.endDate = value[1];
                this.searchCondition();
            }
        },
        methods: {
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
                this.loading = true;

                var listXhr = this.$axios({
                    method: 'POST',
                    url: '/cms/form/ajaxTaskAssignList' + "?" + Object.toURLSearchParams(this.searchListForm)
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

            toTaskAssign:function(){
                if(!this.searchListForm.gnodeId){
                    this.$alert('请选择要指派的节点', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                    return;
                }
                location.href='/a/actyw/actYwGassign/toGassignView?promodelIds=' + this.multipleSelectedId.join(',') + '&gnodeId=' + this.searchListForm.gnodeId + '&actywId=' + this.searchListForm.actywId;
            },

            taskCertPreview:function(id){
                var self = this;
                this.previewCertImgSrc = [];
                this.$axios({
                    method:'POST',
                    url:'/cert/getCertIns?certinsid=' + id
                }).then(function(response){
                    self.previewCertImgSrc = response.data;
                    self.certImgCenter();
                    self.dialogCertVisible = true;
                }).catch(function(){

                })

            },

            certImgCenter:function(){
                var self = this;
                var bannerWidth = 0;
                var bannerHeight = 400;
                self.$nextTick(function () {
                    bannerWidth = self.$refs.elCarousel.$el.offsetWidth;
                    self.bannerScale = bannerWidth / bannerHeight;
                });
            }

        },
        created: function () {
            if (this.messageInfo) {
                this.$alert(this.messageInfo, '提示', {
                    type: this.messageInfo.indexOf('完成') > -1 ? 'success' : 'error',
                    confirmButtonText: '确定'
                });
                this.messageInfo = '';
            }

            this.pageCount = JSON.parse('${fns: toJson(page.count)}') || 0;
            window.parent.sideNavModule.changeStaticUnreadTag("/a/promodel/proModel/getTaskAssignCountToDo?actYwId=${actywId}");
        }
    })


</script>

</body>
</html>