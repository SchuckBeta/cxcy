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
        <el-breadcrumb-item><a href="/f"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>人才库</el-breadcrumb-item>
        <el-breadcrumb-item>导师库</el-breadcrumb-item>
    </el-breadcrumb>

    <el-form :model="searchListForm" ref="searchListForm" size="mini">
        <div class="conditions">
            <e-condition label="所属学院" type="radio" v-model="searchListForm['user.office.id']" :default-props="defaultProps"
                         name="user.office.id" :options="collegeList" @change="getDataList"></e-condition>
            <e-condition label="导师来源" type="radio" v-model="searchListForm.teachertype"
                         name="teachertype" :options="teacherTypes" @change="getDataList"></e-condition>
            <e-condition label="当前指导" type="radio" v-model="searchListForm.curJoinStr"
                         name="curJoinStr" :options="curJoinProjects" @change="getDataList"></e-condition>
            <%--<e-condition label="职称" type="radio" v-model="searchListForm.curTechnicalTitle"--%>
                         <%--name="curTechnicalTitle" :options="curTechnicalTitles" @change="getDataList"></e-condition>--%>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input">
                <input type="text" style="display:none">
                <el-input  v-model="searchListForm.myFind" name="myFind" size="mini" placeholder="关键字" class="w300" @keyup.enter.native="getDataList">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>

    </el-form>


    <div class="table-container">
        <el-table :data="pageList" size="mini" class="table" v-loading="loading"  @sort-change="handleSortChange">
            <el-table-column label="姓名" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.name" popper-class="white" placement="right">
                        <span class="break-ellipsis underline-pointer" @click.stop.prevent="goUserDetail(scope.row.id)">{{scope.row.user.name}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="当前指导" prop="curJoin" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.curJoin" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.curJoin}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="导师来源" prop="teachertype" align="left">
                <template slot-scope="scope">
                    {{scope.row.teachertype | selectedFilter(teacherTypeEntries)}}
                </template>
            </el-table-column>
            <el-table-column label="职称" align="left">
                <template slot-scope="scope">
                    {{scope.row.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}
                </template>
            </el-table-column>
            <el-table-column label="所属学院" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.officeName" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.user.officeName}}</span>
                    </el-tooltip>
                </template>
            </el-table-column>
            <el-table-column label="指导项目最高奖项" align="left">
                <template slot-scope="scope">
                    {{scope.row.topPrise}}
                </template>
            </el-table-column>
            <el-table-column label="技术领域" align="left">
                <template slot-scope="scope">
                    <el-tooltip :content="scope.row.user.domainlt" popper-class="white" placement="right">
                        <span class="break-ellipsis">{{scope.row.user.domainlt}}</span>
                    </el-tooltip>
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
                <el-table-column label="校内导师（已组建/共需）" align="center">
                    <template slot-scope="scope">
                        <span :class="{red: scope.row.schoolNum != scope.row.schoolTeacherNum}">{{scope.row.schoolNum}}</span>/{{scope.row.schoolTeacherNum}}<br>{{scope.row.schName}}
                    </template>
                </el-table-column>
                <el-table-column label="企业导师（已组建/共需）" align="center">
                    <template slot-scope="scope">
                        <span :class="{red: scope.row.enterpriseNum != scope.row.enterpriseTeacherNum}">{{scope.row.enterpriseNum}}</span>/{{scope.row.enterpriseTeacherNum}}<br>{{scope.row.entName}}
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
            var professionals = JSON.parse(JSON.stringify(${fns:getOfficeListJson()})) || [];
            var teacherTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:getDictList('master_type'))}));
            var curJoinProjects = JSON.parse(JSON.stringify(${fns:toJson(fns: getPublishDictList())})) || [];
            var technical = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("postTitle_type"))})) || [];
            return {
                pageList: [],
                pageCount:0,
                searchListForm:{
                    pageNo: 1,
                    pageSize: 10,
                    orderBy: '',
                    orderByType: '',
                    'user.office.id': '',
                    teachertype: '',
                    curJoinStr: '',
                    curTechnicalTitle: '',
                    myFind: ''
                },
                defaultProps:{
                    label:'name',
                    value:'id'
                },
                professionals: professionals,
                teacherTypes: teacherTypes,
                curJoinProjects: curJoinProjects,
                expertJobNames: technical,
//                curTechnicalTitles:[{label:'教授',value:'1'},{label:'培训师',value:'2'},{label:'导师',value:'3'}],
                loading: false,
                canInvite:true,
                curTechnicalTitle:'${curTechnicalTitle}',
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
                    return this.professionals.filter(function (item) {
                        return item.grade == '2';
                    })
                }
            },
            teacherTypeEntries: function () {
                return this.getEntries(this.teacherTypes)
            },
            expertJobNamesEntries:function () {
                return this.getEntries(this.expertJobNames);
            }
        },
        methods:{
            getDataList:function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'GET',
                    url: '/sys/frontTeacherExpansion/ajaxFrontTeacherList?' + Object.toURLSearchParams(this.searchListForm)
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
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
                    method: 'POST',
                    url: '/sys/frontTeacherExpansion/toInvite',
                    params:{
                        userIds:self.userId,
                        teamId:self.multipleSelectedId.join(','),
                        userType:'2'
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
                window.location.href = '${ctxFront}/sys/frontTeacherExpansion/view?id=' + id + '&isNewJSP=1';
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