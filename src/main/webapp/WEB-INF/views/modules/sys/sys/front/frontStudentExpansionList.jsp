<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>

</head>
<body>

<div id="app" v-show="pageLoad" style="display: none" class="container page-container pdt-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>人才库</el-breadcrumb-item>
        <el-breadcrumb-item>学生库</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="clearfix">
        <div class="conditions">
            <e-condition label="所属学院" type="radio" v-model="searchListForm['user.office.id']" :default-props="defaultProps"
                         name="user.office.id" :options="collegeList" @change="getDataList"></e-condition>
            <e-condition label="大赛经历" type="radio" v-model="searchListForm.contestExperience"
                         name="contestExperience" :options="contestExperience" @change="getDataList"></e-condition>
            <e-condition label="项目经历" type="radio" v-model="searchListForm.projectExperience"
                         name="projectExperience" :options="contestExperience" @change="getDataList"></e-condition>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input  v-model="searchListForm.keyWord" name="keyWord" size="mini" placeholder="关键字" class="w300" @keyup.enter.native="getDataList">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </div>


    <div class="table-container">
        <el-table :data="pageList" size="mini" class="table" v-loading="loading"  @sort-change="handleSortChange">
            <el-table-column label="姓名" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                        <span class="break-ellipsis underline-pointer" @click.stop.prevent="goUserDetail(scope.row.id)">{{scope.row.user.name}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="性别" prop="u.sex" sortable="u.sex" align="left">
                <template slot-scope="scope">
                    {{scope.row.user.sex | selectedFilter(sexEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="现状" prop="currState" sortable="currState" align="left">
                <template slot-scope="scope">
                    {{scope.row.currState | selectedFilter(currentStateEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="所属学院" prop="u.officeName" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.officeName" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.user.officeName}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="专业班级" prop="u.professional" sortable="u.professional" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.professional | collegeFilter(collegeEntries) + ' ' + (scope.row.tClass || '')" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.user.professional | collegeFilter(collegeEntries)}} {{scope.row.tClass}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="学历" prop="u.education" sortable="u.education" align="left">
                <template slot-scope="scope">
                    {{scope.row.user.education | selectedFilter(educationEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="学位" prop="u.degree" sortable="u.degree" align="left">
                <template slot-scope="scope">
                    {{scope.row.user.degree | selectedFilter(degreeEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="荣获最高奖项" align="left">
                <template slot-scope="scope">
                    {{scope.row.topPrise}}
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="table-btns-action">
                        <el-button type="text" :disabled="!canInvite || !scope.row.canInvite || teams.length == 0" size="mini" @click.stop.prevent="inviteTeam(scope.row)">邀请</el-button>
                    </div>
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

    <el-dialog title="邀请" :visible.sync="dialogVisible" width="60%" :close-on-click-modal="isClose">
        <div class="table-container">
            <el-table :data="teams" size="mini" class="table" @selection-change="handleSelectionChange" style="margin-bottom:0;">
                <el-table-column type="selection" width="60" :selectable="selectable"></el-table-column>
                <el-table-column prop="number" label="团队编号" align="center">
                </el-table-column>
                <el-table-column prop="name" label="团队名称" align="center">
                    <template slot-scope="scope">
                        <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                            <span class="break-ellipsis">{{scope.row.name}}</span>
                        </el-tooltip>
                    </template>
                </el-table-column>
                <el-table-column label="组员（已组建/共需）" align="center">
                    <template slot-scope="scope">
                        <span :class="{red: scope.row.userCount != scope.row.memberNum}">{{scope.row.userCount}}</span>/{{scope.row.memberNum}}<br>{{scope.row.userName}}
                    </template>
                </el-table-column>
            </el-table>
        </div>
        <span slot="footer" class="dialog-footer">
            <el-button size="mini" @click.stop.prevent="dialogVisible = false">取 消</el-button>
            <el-button type="primary" size="mini" :disabled="multipleSelectedId.length == 0" @click.stop.prevent="saveInvite">确 定</el-button>
        </span>
    </el-dialog>



</div>

<script>

    'use strict';

    new Vue({
        el:'#app',
        data:function () {
            var colleges = JSON.parse(JSON.stringify(${fns: toJson(fns: getOfficeList())})) || [];
            var contestExperience = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('yes_no'))}));
            var sexes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('sex'))}));
            var currentStates = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('current_sate'))}));
            var educationLevels = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('enducation_level'))}));
            var degreeTypes = JSON.parse(JSON.stringify(${fns: toJson(fns:getDictList('degree_type'))}));
            return {
                pageList: [],
                pageCount:0,
                searchListForm:{
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    'user.office.id': '',
                    contestExperience: '',
                    projectExperience: '',
                    award: '',
                    keyWord: ''
                },
                defaultProps:{
                    label:'name',
                    value:'id'
                },
                colleges: colleges,
                contestExperience: contestExperience,
                sexes:sexes,
                currentStates: currentStates,
                educationLevels: educationLevels,
                degreeTypes: degreeTypes,
                loading: false,
                canInvite:true,
                teams:[],
                multipleSelection: [],
                multipleSelectedId: [],
                userId:'',
                canInviteTeamIds:[],
                dialogVisible:false,
                isClose:false
            }
        },
        computed:{
            collegeList:{
                get:function () {
                    return this.colleges.filter(function (item) {
                        return item.grade == '2';
                    })
                }
            },
            collegeEntries: function () {
                var entity = {};
              for(var i = 0; i < this.colleges.length; i++){
                  var item = this.colleges[i];
                  entity[item.id] = item;
              }
              return entity;
            },
            sexEntries: function () {
                return this.getEntries(this.sexes)
            },
            currentStateEntries: function () {
                return this.getEntries(this.currentStates)
            },
            educationEntries: function () {
                return this.getEntries(this.educationLevels)
            },
            degreeEntries: function () {
                return this.getEntries(this.degreeTypes)
            }
        },
        methods:{
            getDataList:function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/sys/frontStudentExpansion/ajaxFrontStudentList?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        self.pageCount = data.data.page.count;
                        self.searchListForm.pageSize = data.data.page.pageSize;
                        self.pageList = data.data.page.list || [];
                        self.teams = data.data.teams || [];
                        self.canInvite = data.data.canInvite;
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败！',
                        type: 'error'
                    })
                })
            },
            selectable: function (row) {
                var arr = this.canInviteTeamIds.split(',');
                if(arr.indexOf(row.id) > -1){
                    return true;
                }
            },
            inviteTeam:function (row) {
                this.teams = Object.assign([],this.teams);
                this.userId = row.user.id;
                this.canInviteTeamIds = row.canInviteTeamIds;
                this.dialogVisible = true;
            },
            saveInvite:function () {
                var self = this;
                this.$axios({
                    method: 'GET',
                    url: '/sys/frontStudentExpansion/toInvite',
                    params:{
                        userIds:self.userId,
                        teamId:self.multipleSelectedId.join(','),
                        userType:'1'
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.dialogVisible = false;
                        self.getDataList();
                    }
                    self.$message({
                        message: data.ret == '1' ? '邀请成功' : data.msg || '邀请失败',
                        type:  data.ret == '1' ? 'success' : 'error'
                    })
                }).catch(function () {
                    self.$message({
                        message: '请求失败！',
                        type: 'error'
                    })
                })
            },
            handlePaginationSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getDataList();
            },

            handlePaginationPageChange: function (value) {
                this.searchListForm.pageNo = value;
                this.getDataList();
            },
            handleSortChange:function (row) {
                this.searchListForm.orderBy = row.prop;
                this.searchListForm.orderByType = row.order ? (row.order.indexOf('asc') > -1 ? 'asc' : 'desc') : '';
                this.getDataList();
            },
            goUserDetail:function (id) {
                window.location.href = '${ctxFront}/sys/frontStudentExpansion/form?id=' + id;
            },
            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push(value[i].id);
                }
            }
        },
        created:function () {
            this.getDataList();
        }
    })


</script>
</body>
</html>