<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <edit-bar></edit-bar>
    <div class="clearfix">
        <div class="conditions">
            <e-condition label="所属学院" type="radio" v-model="searchListForm.localCollege" :default-props="defaultProps"
                         name="localCollege" :options="collegeList" @change="getDataList">
            </e-condition>
            <e-condition label="团队状态" type="radio" v-model="searchListForm.stateSch"
                         name="stateSch" :options="teamStates" @change="getDataList">
            </e-condition>
            <e-condition label="在研项目" type="radio" v-model="searchListForm.inResearch"
                         name="inResearch" :options="inResearchs" @change="getDataList">
            </e-condition>
        </div>

        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <shiro:hasPermission name="team:audit:edit">
                    <el-button type="primary" size="mini" icon="iconfont icon-daishenhe" @click.stop.prevent="autoCheck" v-if="teamCheckOnOff == '1'">
                        设置为自动审核
                    </el-button>
                    <el-button type="primary" size="mini" icon="iconfont icon-daishenhe" @click.stop.prevent="unAutoCheck"
                               v-if="teamCheckOnOff != '1'">设置为手动审核
                    </el-button>
                </shiro:hasPermission>
                <el-tooltip class="item" content="请选择需要删除的团队" :disabled="multipleSelectedId.length > 0"
                            popper-class="white" placement="bottom">
                    <span>
                <el-button type="primary" icon="el-icon-delete" size="mini" :disabled="multipleSelectedId.length == 0"
                           @click.stop.prevent="batchDelete">批量删除</el-button>
                        </span>
                </el-tooltip>
            </div>
            <div class="search-input">
                <el-input name="nameSch" size="mini" class="w300" v-model="searchListForm.nameSch" placeholder="关键字"
                          @keyup.enter.native="getDataList">
                    <el-button slot="append" class="el-icon-search" @click.stop.prevent="getDataList"></el-button>
                </el-input>
            </div>
        </div>
    </div>

    <div class="table-container">
        <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading"
                  @selection-change="handleSelectionChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="团队名称" prop="name" min-width="130">
                <template slot-scope="scope">
                    <p>
                        <el-tooltip :content="scope.row.name" popper-class="white" placement="right">
                            <span class="underline-pointer break-ellipsis"
                                  @click.stop.prevent="goTeamDetail(scope.row.id)">{{scope.row.name}}</span>
                        </el-tooltip>
                    </p>
                    <p>{{scope.row.createDate || '-'}}</p>
                    <p>
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip :content="scope.row.localCollege" popper-class="white" placement="right">
                            <span class="break-ellipsis max-wid-80-per">{{scope.row.localCollege}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="团队负责人" align="center" prop="sponsor" min-width="90">
            </el-table-column>
            <el-table-column label="组员（已组建/共需）" align="center" min-width="150">
                <template slot-scope="scope">
                    <span :class="{red: scope.row.userCount != scope.row.memberNum}">{{scope.row.userCount}}</span>/{{scope.row.memberNum}}<br>{{scope.row.userName}}
                </template>
            </el-table-column>
            <el-table-column label="校内导师（已组建/共需）" align="center" min-width="150">
                <template slot-scope="scope">
                    <span :class="{red: scope.row.schoolNum != scope.row.schoolTeacherNum}">{{scope.row.schoolNum}}</span>/{{scope.row.schoolTeacherNum}}<br>{{scope.row.schName}}
                </template>
            </el-table-column>
            <el-table-column label="企业导师（已组建/共需）" align="center" min-width="150">
                <template slot-scope="scope">
                    <span :class="{red: scope.row.enterpriseNum != scope.row.enterpriseTeacherNum}">{{scope.row.enterpriseNum }}</span>/{{scope.row.enterpriseTeacherNum}}<br>{{scope.row.entName}}
                </template>
            </el-table-column>
            <el-table-column label="团队状态" align="center" min-width="100">
                <template slot-scope="scope">
                    <span v-if="scope.row.state == '1'">建设完毕</span>
                    <span v-if="scope.row.state == '0'">建设中</span>
                    <span v-if="scope.row.state == '2'">解散</span>
                    <span v-if="scope.row.state == '3'">待审核</span>
                    <span v-if="scope.row.state == '4'">未通过</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="100">
                <template slot-scope="scope">
                    <shiro:hasPermission name="team:audit:edit">
                        <el-button class="btn-icon-font" type="text" @click.stop="teamCheck(scope.row.id)"
                                   v-if="scope.row.state == '3'">审核
                        </el-button>
                    </shiro:hasPermission>
                    <el-tooltip v-if="scope.row.state != '2'" class="item" content="解散" popper-class="white"
                                placement="bottom">
                        <el-button class="btn-icon-font" icon="el-icon-remove-outline" type="text"
                                   @click.stop="disTeam(scope.row.id)">
                        </el-button>
                    </el-tooltip>
                    <el-tooltip v-if="scope.row.state == '2'" class="item" content="删除" popper-class="white"
                                placement="bottom">
                        <el-button class="btn-icon-font" icon="el-icon-delete" type="text" @click.stop="singleDelete(scope.row.id)">
                        </el-button>
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


<script>
    new Vue({
        el: '#app',
        data: function () {
            var professionals = JSON.parse('${fns:getOfficeListJson()}') || [];
            var teamStates = JSON.parse('${fns:toJson(fns:getDictList('teamstate_flag'))}');
            var inResearchs = JSON.parse('${fns:toJson(fns:getDictList('yes_no'))}');
            return {
                professionals: professionals,
                teamStates: teamStates,
                inResearchs: inResearchs,
                pageList: [],
                pageCount: 0,
                teamCheckOnOff: '${teamCheckOnOff}',
                message: '${message}',
                searchListForm: {
                    pageSize: 10,
                    pageNo: 1,
                    localCollege: '',
                    stateSch: '',
                    inResearch: '',
                    beginValidDate: '',
                    endValidDate: '',
                    nameSch: ''
                },
                defaultProps: {
                    label: 'name',
                    value: 'id'
                },
                loading: false,
                multipleSelection: [],
                multipleSelectedId: [],
                applyDate: []
            }
        },
        computed: {
            collegeList: {
                get: function () {
                    return this.professionals.filter(function (item) {
                        return item.grade == '2';
                    })
                }
            }
        },
        watch: {
            applyDate: function (value) {
                value = value || [];
                this.searchListForm.beginValidDate = value[0];
                this.searchListForm.endValidDate = value[1];
                this.getDataList();
            }
        },
        methods: {
            getDataList: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/team/getTeamList', {params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.pageCount = data.data.count;
                        self.searchListForm.pageSize = data.data.pageSize;
                        self.pageList = data.data.list || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },
            autoCheck: function () {
                var self = this;
                this.$confirm('当前待审核的团队将会审核通过，确定要设置为自动审核？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.loading = true;
                    self.$axios({method: 'POST', url: '/team/autoCheck'}).then(function (response) {
                        var data = response.data;
                        if (data.ret == '1') {
                            self.teamCheckOnOff = data.teamCheckOnOff;
                            self.getDataList();
                        }
                        self.$message({
                            message: data.ret == '1' ? '设置成功' : data.msg || '设置失败',
                            type: data.ret == '1' ? 'success' : 'error'
                        });
                        self.loading = false;
                    }).catch(function () {
                        self.loading = false;
                    })
                })
            },
            unAutoCheck: function () {
                var self = this;
                this.loading = true;
                this.$axios({method: 'POST', url: '/team/unAutoCheck'}).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.teamCheckOnOff = data.teamCheckOnOff;
                        self.getDataList();
                    }
                    self.$message({
                        message: data.ret == '1' ? '设置成功' : data.msg || '设置失败',
                        type: data.ret == '1' ? 'success' : 'error'
                    });
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                })
            },
            batchDelete: function () {
                var self = this;
                this.$confirm('确认要删除所选团队吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var path = {method: 'POST', url: '/team/delTeams', data: self.multipleSelectedId};
                    self.axiosRequest(path, true, '批量删除');
                })
            },
            goTeamDetail: function (id) {
                window.location.href = '${ctx}/team/findByTeamId?id=' + id;
            },
            teamCheck: function (id) {
                window.location.href = '${ctx}/team/toTeamAudit?id=' + id;
            },
            disTeam: function (id) {
                var self = this;
                this.$confirm('确认要解散该团队吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var path = {method: 'POST', url: '/team/dissolveTeam', data: {id: id}};
                    self.axiosRequest(path, true, '解散');
                })
            },
            singleDelete: function (id) {
                var self = this;
                this.$confirm('确认要删除该团队吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var path = {method: 'POST', url: '/team/delTeams', data: [{id: id}]};
                    self.axiosRequest(path, true, '删除');
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
            handleSelectionChange: function (value) {
                this.multipleSelection = value;
                this.multipleSelectedId = [];
                for (var i = 0; i < value.length; i++) {
                    this.multipleSelectedId.push({id: value[i].id});
                }
            },
            axiosRequest: function (path, showMsg, msg) {
                var self = this;
                this.loading = true;
                this.$axios(path).then(function (response) {
                    var data = response.data;
                    if (data.status == '1' || data.ret == '1') {
                        self.getDataList();
                        if (showMsg) {
                            self.$message({
                                message: data.msg || msg + '成功',
                                type: 'success'
                            });
                        }
                    } else {
                        self.$message({
                            message: data.msg || msg + '失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                    self.$message({
                        message: '请求失败',
                        type: 'error'
                    })
                })
            }
        },
        created: function () {
            this.getDataList();
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('完成') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
            window.parent.sideNavModule.changeStaticUnreadTag("/a/team/getTeamCountToAudit");
        }
    })
</script>


</body>
</html>