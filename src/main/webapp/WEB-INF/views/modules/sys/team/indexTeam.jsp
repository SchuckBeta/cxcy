<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>


<div id="app" v-show="pageLoad" style="display: none;" class="page-container pdb-60 team-build">

    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>人才库</el-breadcrumb-item>
        <el-breadcrumb-item>团队建设</el-breadcrumb-item>
    </el-breadcrumb>
    <template v-if="isStudent">
        <div class="team-build-process">
            <div class="process-label">团建步骤：</div>
            <ul>
                <li>第一步：创建团队</li>
                <li>第二步：待审核通过</li>
                <li>第三步：发布/邀请组员</li>
            </ul>
        </div>
        <el-form ref="createTeam" method="post" size="mini"
                 label-width="130px" class="demo-ruleForm" :model="createTeam" :rules="rules" :disabled="formDisabled">
            <input id="id" name="id" type="hidden" :value="createTeam.id">
            <input id="state" name="state" type="hidden" :value="createTeam.state">
            <input id="number" name="number" type="hidden">
            <input id="proType" name="proType" type="hidden" value="${proType}">
            <div class="create-box" :class="{'box-border':isBorder}">
                <el-collapse-transition>
                    <div class="box-form" v-show="createBox">
                        <el-row :gutter="20">
                            <el-col :span="6">
                                <el-form-item label="团队名称" prop="name" label-width="72px">
                                    <el-input id="name" name="name" :old="oldList.name"
                                              v-model="createTeam.name"></el-input>
                                </el-form-item>

                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="所需组员人数(含项目负责人)" prop="memberNum" label-width="174px">
                                    <el-tooltip class="item" :disabled="!studentMax" effect="dark" popper-class="white"
                                                :content="'人数不能超过'+studentMax+'人'" placement="top">
                                        <el-input-number id="memberNum" name="memberNum" :min="1"
                                                         :max="parseInt(studentMax)"
                                                         v-model.number="createTeam.memberNum"></el-input-number>
                                    </el-tooltip>
                                </el-form-item>
                            </el-col>
                            <el-col :span="5">
                                <el-form-item label="所需校内导师数" prop="schoolTeacherNum">
                                    <el-tooltip class="item" :disabled="!teacherMax" effect="dark" popper-class="white"
                                                :content="'人数不能超过'+teacherMax+'人'" placement="top">
                                        <el-input-number id="schoolTeacherNum" name="schoolTeacherNum"
                                                         :min="parseInt(teacherMin)" :max="parseInt(teacherMax)"
                                                         v-model.number="createTeam.schoolTeacherNum"></el-input-number>
                                    </el-tooltip>
                                </el-form-item>
                            </el-col>
                            <el-col :span="6">
                                <el-form-item label="所需企业导师数" prop="enterpriseTeacherNum">
                                    <el-input-number id="enterpriseTeacherNum" name="enterpriseTeacherNum" :min="0"
                                                     v-model.number="createTeam.enterpriseTeacherNum"></el-input-number>
                                </el-form-item>
                            </el-col>
                        </el-row>
                        <el-row :gutter="20" class="member-top">
                            <el-col :span="12">
                                <el-form-item label="组员要求" prop="membership" label-width="72px">
                                    <el-input id="membership" name="membership" :old="oldList.membership"
                                              type="textarea"
                                              v-model="createTeam.membership" :rows="4"
                                              placeholder="200字以内"></el-input>
                                </el-form-item>
                            </el-col>
                            <el-col :span="12">
                                <el-form-item label="团队介绍" prop="summary">
                                    <el-input id="summary" name="summary" :old="oldList.summary"
                                              type="textarea"
                                              v-model="createTeam.summary" :rows="4"
                                              placeholder="500字以内"></el-input>
                                </el-form-item>
                            </el-col>
                        </el-row>
                    </div>
                </el-collapse-transition>


                <div class="text-right" v-show="userType == 1">
                    <el-button class="create-btn" v-show="createBox" type="primary" :disabled="formDisabled"
                               @click="submitCreateForm('createTeam')"
                               size="mini">
                        保存
                    </el-button>
                    <el-button @click.stop="resetCreateForm" size="mini" v-show="createBox">取消</el-button>
                </div>

            </div>

        </el-form>
    </template>
    <div class="clearfix">
        <div class="search-input">
            <el-input placeholder="请输入内容" v-model="searchListForm.searchText" :name="searchName" :id="searchName"
                      maxlength="128"
                      class="w300"
                      size="mini">
                <el-select v-model="searchListForm.selectOption" slot="prepend" placeholder="选择查询条件"
                           style="width: 120px;">
                    <el-option v-for="option in options" :label="option.label" :value="option.value"
                               :key="option.value"></el-option>
                </el-select>
                <el-button slot="append" icon="el-icon-search"
                           @click.stop.prevent="searchCondition"></el-button>
            </el-input>
            <el-button v-if="isStudent" type="primary" size="mini" :disabled="!isCanCreate" @click.stop="showForm">
                创建团队
            </el-button>
            <el-button class="dele-btn" type="primary" size="mini"
                       :disabled="multipleSelectedId==null || multipleSelectedId==''"
                       @click.stop.prevent="deleteTeam(multipleSelectedId)">删除团队
            </el-button>
        </div>
        <div class="conditions">
            <e-condition label="团队状态：" type="checkbox" :options="teamStates" v-model="searchListForm.stateSch"
                         name="stateSch"
                         @change="searchCondition"></e-condition>
        </div>
    </div>


    <div class="table-container">
        <el-table ref="multipleTable" :data="teamList" size="small" style="width: 100%;"
                  class="team-table"
                  @selection-change="handleSelectionChange">
            <el-table-column type="selection" width="60" :selectable="selectable">
            </el-table-column>

            <el-table-column label="团队信息" align="center" width="300">
                <template slot-scope="scope">
                    <p class="team-name">
                        <span v-show="curUserId != scope.row.sponsorId && scope.row.state == 3">信息审核中</span>
                        <span v-show="curUserId != scope.row.sponsorId && scope.row.state == 4">信息审核未通过</span>
                        <span v-show="curUserId == scope.row.sponsorId || (scope.row.state != 3 && scope.row.state != 4)"
                              class="team-teamName"
                              @click.stop.prevent="goName(scope.row.id)">{{scope.row.name}}</span>
                    </p>
                    <p>所属学院：{{scope.row.localCollege}}</p>
                    <p>创建日期：{{scope.row.createDate}}</p>
                </template>
            </el-table-column>

            <el-table-column prop="sponsor" label="团队负责人" align="center"></el-table-column>

            <el-table-column label="团队成员（已组建/共需）" align="center">
                <template slot-scope="scope">
                    <span :class="{red: scope.row.userCount !== scope.row.memberNum}">{{scope.row.userCount}}</span>/<span>{{scope.row.memberNum}}</span>
                    <p>{{scope.row.userName}}</p>
                </template>
            </el-table-column>

            <el-table-column label="团队导师（已组建/共需）" align="center">
                <template slot-scope="scope">
                    <span :class="{red: scope.row.schoolNum !== scope.row.schoolTeacherNum}">{{scope.row.schoolNum}}</span>/<span>{{scope.row.schoolTeacherNum}}</span>
                    <p>校内导师：{{scope.row.schName}}</p>
                    <span :class="{red: scope.row.enterpriseNum !==  scope.row.enterpriseTeacherNum}">{{scope.row.enterpriseNum}}</span>/<span>{{scope.row.enterpriseTeacherNum}}</span>
                    <p>企业导师：{{scope.row.entName}}</p>
                </template>
            </el-table-column>

            <el-table-column prop="state" label="团队状态" align="center">
                <template slot-scope="scope">
                    <span>{{teamStatesEntries[scope.row.state]}}</span>
                </template>
            </el-table-column>

            <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                    <div class="do-p" v-if="curUserId==scope.row.sponsorId && scope.row.state!=2">
                        <p>
                            <el-button type="text" size="small" :disabled="scope.row.state != 0"
                                       @click="releaseTeam(scope.row.id)">发布团队
                            </el-button>
                        </p>
                        <p>
                            <el-button type="text" size="small"
                                       :disabled="!(scope.row.state == 0 || !scope.row.checkIsHus)"
                                       @click="goInvite(scope.row.id)">邀请组员
                            </el-button>
                        </p>
                        <p>
                            <el-button type="text" size="small"
                                       :disabled="!(scope.row.state == 0 || scope.row.state == 1 || scope.row.state == 4)"
                                       @click="editTeam(scope.row)">编辑团队
                            </el-button>
                        </p>
                        <p>
                            <el-button type="text" size="small"
                                       :disabled="!(scope.row.state != 2 && scope.row.state != 3)"
                                       @click="toDisTeam(scope.row.id)">解散团队
                            </el-button>
                        </p>
                    </div>
                    <div class="do-p">
                        <p>
                            <el-button type="text" size="small" :disabled="!isCanCreate"
                                       v-show="!scope.row.checkJoinTUR && userType!=2 && scope.row.state==0"
                                       @click="checkIfLogin(scope.row.id)">加入团队
                            </el-button>
                        </p>
                        <p>
                            <el-button type="text" size="small" v-show="!scope.row.checkJoinTUR || scope.row.state == 2"
                                       @click="deleteTeam(scope.row.id)">删除
                            </el-button>
                        </p>
                    </div>
                </template>
            </el-table-column>

        </el-table>
        <div class="text-right mgb-20" v-if="teamList.length > 0">
            <el-pagination background
                           @size-change="handleSizeChange"
                           @current-change="handleCurrentChange"
                           :current-page="searchListForm.pageNo"
                           :page-sizes="[5,10,20,50,100]"
                           :page-size="searchListForm.pageSize"
                           layout="total, prev, pager, next, sizes"
                           :total="teamListLength">
            </el-pagination>
        </div>
    </div>


    <el-dialog title="团队发布" :visible.sync="dialogFormVisible" width="50%">
        <div class="team-publi">
            <div class="publi-list">
                <ul>
                    <li class="table-first" :class="{active: schoolOffice.id == currentProfessionalId}">
                        <span @click.stop.prevent="changeMajorTable(schoolOffice)">
                            <span @click.stop.prevent="majorCollapse" class="add">{{isAdd}}</span>
                            {{schoolOffice.name}}
                        </span>
                    </li>
                    <el-collapse-transition>
                        <div v-show="isMajorShow">
                            <li v-for="(professional,index) in collegesList"
                                class="dashed-line"
                                :class="{active:professional.id==currentProfessionalId}">
                                <span @click.stop.prevent="changeMajorTable(professional)">{{professional.name}}</span>
                            </li>
                        </div>
                    </el-collapse-transition>
                </ul>
            </div>
            <div class="publi-form">
                <el-table ref="multipleMajorTable" :data="clickedMajorList" size="small" style="width: 100%;"
                          @selection-change="handleSelectionMajor">
                    <el-table-column type="selection" width="60">
                    </el-table-column>

                    <el-table-column prop="name" label="专业" align="center">
                    </el-table-column>

                </el-table>
            </div>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-tooltip content="请选择专业" :disabled="isAble" popper-class="white" placement="bottom">
                <span><el-button size="mini" type="primary" :disabled="!isAble" @click="confirmRelease">{{releaseText}}
                </el-button></span>
            </el-tooltip>
        </div>
    </el-dialog>


</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var validateName = function (rule, value, callback) {
                if (value) {
                    var params = {
                        "name": value,
                        "teamId": self.createTeam.id
                    }
                    return self.$axios.get('/sys/user/ifTeamNameExist', {params: params}).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            callback(new Error('团队名称已经存在'));
                        }
                        callback();
                    }).catch(function () {
                        callback(new Error(self.xhrErrorMsg));
                    })
                }
                return callback();
            };
            var validateNum = function (rule, value, callback) {
                if (value > 99) {
                    return callback(new Error('人数最多2位数'));
                } else {
                    callback();
                }
            };

            return {
                userType: '${userType}',
                createBox: false,
                messageInfo: '${message}',
                curUserId: '${curUserId}',
                opType: '${opType}',
                teamId: '',
                isBorder: false,

                searchListForm: {
                    searchText: '',
                    stateSch: [],
                    selectOption: 'nameSch',
                    pageNo: 1,
                    pageSize: 5
                },

                multipleSelection: [],
                multipleSelectedId: [],
                currentStateIndex: '',
                dialogFormVisible: false,
                isActive: false,
                currentProfessionalId: '',
                clickedMajorList: [],
                oneLevelMajorList: [],
                multipleMajor: [],
                multipleMajorId: [],
                multipleMajorTable: {},
                currentPage: 1,
                teamListLength: 0,
                eachLength: 5,
                createTeam: {
                    id: '',
                    state: '',
                    name: '',
                    memberNum: '',
                    schoolTeacherNum: '',
                    enterpriseTeacherNum: '',
                    membership: '',
                    summary: '',
                    proType: '${proType}'
                },
                teamCheckOnOff: '${teamCheckOnOff}',

                rules: {
                    name: [
                        {required: true, message: '请输入团队名称', trigger: 'blur'},
                        {min: 1, max: 64, message: '请输入1-64个字符', trigger: 'blur'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    memberNum: [
                        {required: true, message: '请输入所属组员人数', trigger: 'blur'},
                        {type: 'number', message: '请输入数字', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],
                    schoolTeacherNum: [
                        {required: true, message: '请输入所需校内导师数', trigger: 'blur'},
                        {type: 'number', message: '请输入数字', trigger: 'blur'},
                        {validator: validateNum, trigger: 'change'}
                    ],
                    membership: [
                        {required: true, message: '请输入组员要求', trigger: 'blur'},
                        {min: 1, max: 200, message: '请输入1-200个字符', trigger: 'blur'}
                    ],
                    summary: [
                        {required: true, message: '请输入团队介绍', trigger: 'blur'},
                        {min: 1, max: 500, message: '请输入1-500个字符', trigger: 'blur'}
                    ]
                },
                teamStates: [
                    {label: '建设中', value: '0'},
                    {label: '建设完毕', value: '1'},
                    {label: '解散', value: '2'},
                    {label: '待审核', value: '3'},
                    {label: '未通过', value: '4'}
                ],
                teamStatesEntries: {
                    '0': '建设中',
                    '1': '建设完毕',
                    '2': '解散',
                    '3': '待审核',
                    '4': '未通过'
                },
                teamState: [],
                searchText: '',
                selectOption: '',
                teamList: [],
                options: [
                    {label: '团队名称', value: 'nameSch'},
                    {label: '团队负责人', value: 'creator'}
                ],
                option: [],
                searchName: '',
                editAble: false,
                oldList: {
                    name: '${team.name}',
                    membership: '${team.membership}',
                    summary: '${team.summary}'
                },
                isMajorShow: true,
                isAdd: '+',
                searchTeamList: '',
                isCanCreate: false,
                releaseText: '确认发布',
                teacherMin: '${teacherMin}',
                teacherMax: '${teacherMax}',
                studentMax: '${studentMax}',
                formDisabled: false,

                officeList: [],
                officeEntity: {},
                officeTree: [],
            }
        },
        computed: {

            schoolOffice: function () {
                if (this.officeTree.length > 0) {
                    return this.officeTree[0]
                }
                return {};
            },
            collegesList: function () {
                if (this.schoolOffice.children) {
                    return this.schoolOffice.children;
                }
                return [];
            },

            isAble: function () {
                return this.multipleMajorId.length > 0;
            }

        },
        methods: {
            majorCollapse: function () {
                this.isMajorShow = !this.isMajorShow;
                this.isAdd = !this.isMajorShow ? '-' : '+'
            },

            changeMajorTable: function (obj) {
                var clickedMajorList = [];
                var parentId = obj.parentId;
                var collegesList = [];
                if (!this.officeEntity[parentId]) {
                    //第一级
                    collegesList = this.collegesList;
                } else {
                    collegesList.push(obj)
                }
                collegesList.forEach(function (item) {
                    clickedMajorList = clickedMajorList.concat(item.children || [])
                });
                this.currentProfessionalId = obj.id;
                this.clickedMajorList = clickedMajorList;
            },

            releaseTeam: function (id) {
                this.dialogFormVisible = true;
                this.multipleMajorId = [];
                this.clickedMajorList = [];
                this.changeMajorTable(this.schoolOffice);
                this.teamId = id;
            },

            handleSelectionMajor: function (val) {
                this.multipleMajorId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleMajorId.push(val[i].id);
                }
            },

            confirmRelease: function () {
                var self = this;
                var params = {
                    offices: this.currentProfessionalId,
                    userIds: this.multipleMajorId.join(','),
                    teamId: this.teamId
                };
                this.releaseText = '发布中...';
                this.$axios.get('/team/teamUserRelation/batInTeamUser', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.success) {
                        self.dialogFormVisible = false;
                        self.releaseText = '确认发布';
                        self.$alert('发布成功!', '提示', {
                            type: 'success'
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                }).catch(function () {
                    self.releaseText = '确认发布';
                })
            },

            handleSizeChange: function (val) {
                this.searchListForm.pageSize = val;
                this.searchCondition();
            },

            handleCurrentChange: function (val) {
                this.searchListForm.pageNo = val;
                this.searchCondition();
            },


            showForm: function () {
                var self = this;
                this.editAble = false;
                this.$axios.get('/team/checkTeamCreateCdn').then(function (response) {
                    var data = response.data;
                    if (data.ret == 1) {
                        self.createBox = true;
                        self.isBorder = true;
                        self.$nextTick(function () {
                            self.$refs.createTeam.resetFields();
                            self.createTeam.id = '';
                            self.createTeam.state = '';
                        });
                    } else if (!self.editAble) {
                        if (data.ret != 2) {
                            self.$alert(data.msg, '提示', {
                                confirmButtonText: '确定',
                                type: 'warning'
                            });
                        }
                    }
                });
                return false;
            },

            selectable: function (row) {
                return row.state == 2 || !row.checkJoinTUR;
            },


            searchCondition: function () {
                var self = this;
                var params = {
                    stateSch: this.searchListForm.stateSch.join(','),
                    pageNo: this.searchListForm.pageNo,
                    pageSize: this.searchListForm.pageSize
                };
                params[this.searchListForm.selectOption] = this.searchListForm.searchText;
                this.$axios.get('/team/ajaxTeamList', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.status) {
                        self.teamList = data.datas.list || [];
                        self.teamListLength = data.datas.total;
                    }
                })
            },

            checkIfLogin: function (id) {
                var self = this;
                this.$axios.get('/team/applyJoin', {
                    params: {
                        teamId: id
                    }
                }).then(function (response) {
                    var data = response.data;
                    self.$alert(data, '提示', {
                        confirmButtonText: '确定',
                        type: data.indexOf('成功') > -1 ? 'success' : 'error'
                    });
                })
            },

            deleteTeam: function (id) {
                var self = this;
                this.$confirm('确认删除吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    var deleteXhr = self.$axios({
                        method: 'POST',
                        url: 'team/hiddenDelete?teamIds=' + id
                    });
                    deleteXhr.then(function (response) {
                        var data = response.data;
                        if (data.code == 0) {
                            self.searchCondition();
                        }
                    }).catch(function () {
                        self.$alert('操作异常', '提示', {
                            confirmButtonText: '确定',
                            type: 'warning'
                        });
                    })
                });
                return false;
            },

            toDisTeam: function (id) {
                var self = this;
                this.$confirm('确认要解散该团队吗？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    customClass: 'message-center'
                }).then(function () {
                    var deleXhr = self.$axios({
                        method: 'POST',
                        url: '/team/disTeam?id=' + id
                    });
                    deleXhr.then(function (response) {
                        var data = response.data;
                        if (data.code == 0) {
                            self.searchCondition();
                        } else {
                            self.$alert(data.msg, '提示', {
                                confirmButtonText: '确定',
                                type: 'error',
                                size: 'mini'
                            });
                        }
                    }).catch(function () {
                        self.$alert('该团队正在进行项目、大赛或已入驻基地，不能解散', '提示', {
                            confirmButtonText: '确定',
                            type: 'error',
                            size: 'mini'
                        });
                    })
                });
            },

            editTeam: function (row) {
                var self = this;
                self.editAble = true;
                var teamXhr = this.$axios.get('/team/findById',{
                    params: {
                        "id": row.id
                    }
                });
                this.isBorder = true;
                this.createBox = true;

                teamXhr.then(function (response) {
                    var data = response.data;
                    $.each(data, function (k, v) {
                        $.each(self.oldList, function (m) {
                            if (k == m) {
                                self.oldList[m] = v;
                            }
                        })
                    });
                    self.$nextTick(function () {
                        self.createTeam = {
                            id: row.id,
                            state: row.state,
                            name: data.name,
                            memberNum: data.memberNum,
                            schoolTeacherNum: data.schoolTeacherNum,
                            enterpriseTeacherNum: data.enterpriseTeacherNum,
                            membership: data.membership,
                            summary: data.summary
                        }
                    })
                }).catch(function () {
                });
                return false;
            },

            goInvite: function (id) {
                window.location.href = '${ctxFront}/team/teambuild?id=' + id;
            },

            goName: function (id) {
                window.location.href = '${ctxFront}/team/findByTeamId?id=' + id;
            },



            submitCreateForm: function (formName) {
                var self = this;
                if (this.createBox == false) {
                    this.$refs[formName].resetFields();
                } else {
                    self.$refs[formName].validate(function (valid) {
                        if (valid) {
                            var ck = "0";
                            if (self.createTeam.state != "4" && self.createTeam.id && self.teamCheckOnOff == "1") {
                                $.each(self.oldList, function (k, v) {
                                    if (self.createTeam[k] != v) {
                                        ck = "1";
                                    }
                                })
                            }
                            if (ck == '1') {
                                self.$confirm('修改团队名称、组员要求、团队介绍需要审核，确定保存？', '提示', {
                                    confirmButtonText: '确定',
                                    cancelButtonText: '取消',
                                    type: 'warning'
                                }).then(function () {
                                    self.saveAjax();
                                });
                            } else {
                                self.saveAjax();
                            }
                        }
                    })
                }
                this.createBox = true;
                this.isBorder = true;
            },

            saveAjax: function () {
                var self = this;
                this.formDisabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/team/ajaxIndexSave?proType=' + self.createTeam.proType,
                    data: self.createTeam
                }).then(function (response) {
                    var data = response.data;
                    if (data.status == '1') {
                        self.searchCondition();
                        self.createBox = false;
                        self.isBorder = false;
                    }
                    self.formDisabled = false;
                    self.$message({
                        message: data.status == '1' ? data.data.message || '保存成功' : data.msg || '保存失败',
                        type: data.status == '1' ? 'success' : 'error'
                    })
                }).catch(function () {
                    self.formDisabled = false;
                });
            },

            resetCreateForm: function () {
                this.$refs.createTeam.resetFields();
                this.createTeam.id = '';
                this.createTeam.state = '';
                this.$nextTick(function () {
                    this.createBox = false;
                    this.isBorder = false;
                })
            },

            handleSelectionChange: function (val) {
                this.multipleSelectedId = [];
                for (var i = 0; i < val.length; i++) {
                    this.multipleSelectedId.push(val[i].id);
                }
            },

            getOfficeList: function () {
                var self = this;
                this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        var officeList = data.data || [];
                        var treeEntity = self.listToTreeEntity(officeList);
                        self.officeTree = JSON.parse(JSON.stringify(treeEntity.tree));
                        self.officeEntity = treeEntity.entity;
                        self.officeList = officeList;
                    }
                })
            },

            validatePersonComplete: function () {
                var self = this;
                this.$axios.get('/team/checkTeamCreateCdn').then(function (response) {
                    var data = response.data;
                    if (data.ret !== 1) {
                        self.isCanCreate = false;
                        if(self.isStudent){
                            self.$alert(data.msg, '提示', {
                                type: 'warning',
                                showClose: false
                            }).then(function () {
                                if (data.ret === 2) {
                                    location.href = '${ctxFront}/sys/frontStudentExpansion/findUserInfoById?id=' + self.loginCurUser.id + '&isEdit=1';
                                }
                            })
                        }
                    } else if (data.ret === 1) {
                        self.isCanCreate = true;
                    }
                })
            }
        },

        created: function () {
            this.validatePersonComplete();
        },
        mounted: function () {
            if(this.isStudent){
                this.getOfficeList();
            }
            this.searchCondition();
        },


    })
</script>


</body>
</html>