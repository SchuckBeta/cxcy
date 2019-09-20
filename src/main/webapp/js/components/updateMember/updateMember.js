'use strict';


Vue.component('update-member', {
    template: '\n' +
    '    <div class="update-member-box">\n' +
    '                <div v-if="addable" class="text-right mgb-20"><el-tooltip class="item" popper-class="white"  effect="dark" :disabled="tooltipStuDisabled"  :content="upStuMaxText" placement="top"><el-button class="btn-add" :disabled="disabled" type="primary" size="mini"' +
    '                              @click.stop.prevent="inviteStudent"><i class="el-icon-circle-plus el-icon--left"></i>添加组员\n' +
    '                           </el-button></el-tooltip></div><e-col-item label-width="110px" label="团队组成员：">\n' +
    '                    <div class="table-container">\n' +
    '                        <el-table size="mini" :data="studentList" class="table" ref="multipleTable" v-loading="loading">\n' +
    '                            <el-table-column label="姓名"  align="center" width="210"><template slot-scope="scope"><div class="user-info-box"><span class="user-pic"><img :src="scope.row.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></span>{{scope.row.name}}</div></template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="所属学院/专业" align="center">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                    <div class="break-ellipsis">{{scope.row.org_name || scope.row.office}}<span v-if="scope.row.professional">/</span>{{scope.row.professional}}</div>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column prop="mobile" label="联系电话" align="center"><template slot-scope="scope">{{scope.row.mobile}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column prop="email" label="电子邮箱" align="center"><template slot-scope="scope">{{scope.row.email}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="团队职责" align="center">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                    <template v-if="!isAdmin"><span v-if="scope.row.userId == declareId">负责人</span>\n' +
    '                                    <span v-else>组成员</span></template>' +
    '                                   <template v-else><el-tooltip class="item" popper-class="white" :disabled="scope.row.userzz !==\'0\'"  effect="dark" content="请先更换其他组成员为负责人" placement="top">' +
    '                                   <el-select size="mini" :disabled="scope.row.userzz===\'0\'" v-model="scope.row.userzz" @change="handleChangeChargeMen(scope.row)">' +
    '                                       <el-option v-for="item in userDuties" :key="item.value" :value="item.value" :label="item.label"></el-option></el-select></el-tooltip></template>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="操作" width="64" align="center" v-if="operateBtnShow">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                        <el-button icon="el-icon-delete" size="mini" :disabled="(scope.row.userzz == \'0\') || disabled"\n' +
    '                                           @click.stop.prevent="deleteStudent(scope.$index)"></el-button>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                        </el-table>\n' +
    '                    </div>\n' +
    '                       </e-col-item>\n' +
    '                <div v-if="addable" class="text-right mgb-20"><el-tooltip class="item" popper-class="white"  effect="dark" :disabled="tooltipTeaDisabled" :content="upTeaMaxTex" placement="top">' +
    '                           <el-button class="btn-add" :disabled="disabled" type="primary" size="mini"' +
    '                              @click.stop.prevent="inviteTeacher"><i class="el-icon-circle-plus el-icon--left"></i>添加导师\n' +
    '                           </el-button></el-tooltip></div><e-col-item label-width="110px" label="指导教师：">\n' +
    '                    <div class="table-container update-member-box-table">\n' +
    '                        <el-table size="mini" :data="teacherList" class="table" ref="multipleTable" v-loading="loading">\n' +
    '                           <el-table-column label="姓名" align="center" min-width="140"><template slot-scope="scope"><div class="first-teacher-label" v-if="firstTeacherIdView == scope.row.userId"><span>第一导师</span></div><div class="user-info-box"><el-tooltip :content="scope.row.name" popper-class="white" placement="right"><span class="break-ellipsis"><span class="user-pic"><img :src="scope.row.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></span>{{scope.row.name}}</span></el-tooltip></div></template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="职称" align="center" min-width="80"><template slot-scope="scope">{{scope.row.technicalTitle | selectedFilter(technicalEntries, true)}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="导师类型" align="center" min-width="80"><template slot-scope="scope">{{scope.row.teacherType}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="任教学院" align="center" min-width="110"><template slot-scope="scope">{{scope.row.org_name}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="联系电话" align="center" min-width="100"><template slot-scope="scope">{{scope.row.mobile}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="导师职责" align="center" min-width="120" v-if="firstTeacherId">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                    <template v-if="!isAdmin"><span v-if="scope.row.userId == firstTeacherId">第一导师</span>\n' +
    '                                    <span v-else>成员</span></template>' +
    '                                   <template v-else><el-tooltip class="item" popper-class="white" :disabled="scope.row.teacherzz !==\'0\'"  effect="dark" content="请先更换其他导师为第一导师" placement="top">' +
    '                                   <el-select size="mini" :disabled="scope.row.teacherzz===\'0\'" v-model="scope.row.teacherzz" @change="handleChangeChargeTeacher(scope.row)">' +
    '                                       <el-option v-for="item in teacherDuties" :key="item.value" :value="item.value" :label="item.label"></el-option></el-select></el-tooltip></template>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="操作" min-width="60" align="center" v-if="operateBtnShow">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                        <el-button :disabled="(scope.row.teacherzz && scope.row.teacherzz == \'0\') || disabled" icon="el-icon-delete" size="mini"\n' +
    '                                           @click.stop.prevent="deleteTeacher(scope.$index)"></el-button>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                        </el-table>\n' +
    '                    </div>\n' +
    '                </e-col-item>\n' +
    '        <el-dialog v-if="addable" :title="dialogTitle" :visible.sync="dialogVisible" top="5vh" :close-on-click-modal="false" width="1100px" :before-close="handleClose">\n' +
    '            <el-form size="mini" autocomplete="off" class="user-list-params-from">\n' +
    '                <div class="conditions">\n' +
    '                    <e-condition name="currState" type="radio" v-model="userListParams.currState" label="现状"\n' +
    '                                 :options="currentStates"\n' +
    '                                 v-show="userListParams.userType === \'1\'" @change="getUserList"></e-condition>\n' +
    '                    <e-condition type="checkbox" :label="curJoinLabel" :options="curJoinProjects"\n' +
    '                                 v-model="userListParams.curJoinStr" name="curJoinStr" @change="getUserList"></e-condition>\n' +
    '                </div>\n' +
    '                <div class="search-block_bar clearfix">\n' +
    '                    <div class="search-btns">\n' +
    '                        <el-button type="primary"\n' +
    '                                   :disabled="userListTeacherMultipleTable.length < 1 && userListStudentMultipleTable.length < 1"\n' +
    '                                   size="mini" @click.stop.prevent="sendInvite">添加\n' +
    '                        </el-button>\n' +
    '                    </div>\n' +
    '                    <div class="search-input">\n' +
    '                        <el-input name="userName"\n' +
    '                                  :placeholder="searchInputPlaceholder"\n' +
    '                                  v-model="userListParams.userName"\n' +
    '                                  size="mini"\n' +
    '                                  style="width: 270px;">\n' +
    '                            <el-button type="button" slot="append" icon="el-icon-search"\n' +
    '                                       @click.stop.prevent="getUserList"></el-button>\n' +
    '                        </el-input>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '            </el-form>\n' +
    '            <el-row>\n' +
    '                <el-col v-show="!isFullUserList" :span="5">\n' +
    '                    <div class="college_selected_tree">\n' +
    '                        <div v-show="userListParams.userType === \'2\'" class="user-teacher_qy">\n' +
    '                            <a class="el-tree-node__label" href="javascript: void(0);"\n' +
    '                               @click.stop.prevent="getCompanyTeacherList">企业导师</a>\n' +
    '                        </div>\n' +
    '                        <el-tree :data="officeTree" :props="defaultProps"\n' +
    '                                 :highlight-current="highlightTree"\n' +
    '                                 default-expand-all\n' +
    '                                 :expand-on-click-node="false"\n' +
    '                                 @node-click="handleNodeClick"></el-tree>\n' +
    '                    </div>\n' +
    '                </el-col>\n' +
    '                <el-col :span="isFullUserList?24:19">\n' +
    '                    <div class="control-table_user-width" @click.stop.prevent="isFullUserList = !isFullUserList">\n' +
    '                        <i v-show="!isFullUserList" class="iconfont icon-triangle-arrow-l"></i>\n' +
    '                        <i v-show="isFullUserList" class="iconfont icon-triangle-arrow-r"></i>\n' +
    '                    </div>\n' +
    '                    <div class="table_user-list-container">\n' +
    '                        <div v-loading="dataLoading"\n' +
    '                             class="table_user_wrapper">\n' +
    '                            <el-table v-show="userListParams.userType === \'1\'" :data="userListStudent"\n' +
    '                                      ref="userListStudentMultipleTable"\n' +
    '                                      class="mgb-20"\n' +
    '                                      @selection-change="handleSelectionStudentChange" size="mini">\n' +
    '                                <el-table-column\n' +
    '                                        type="selection"\n' +
    '                                        :selectable="selectableStudent"\n' +
    '                                        width="60">\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="姓名">\n' +
    '                                    <template slot-scope="scope">{{scope.row.name}}</template>\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="学号">\n' +
    '                                    <template slot-scope="scope">{{scope.row.no}}</template>\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="现状" width="80">\n' +
    '                                    <template slot-scope="scope">{{scope.row.currStateStr}}</template>\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="技术领域" width="250">\n' +
    '                                    <template slot-scope="scope">' +
    '                                       <el-tooltip :content="scope.row.domainlt" popper-class="white" placement="right">\n' +
    '                                           <span class="break-ellipsis">{{scope.row.domainlt}}</span>\n' +
    '                                       </el-tooltip>\n' +
    '                                    </template>\n' +
    '                                </el-table-column>\n' +
    '                            </el-table>\n' +
    '                            <el-table v-show="userListParams.userType === \'2\'" :data="userListTeacher"\n' +
    '                                      ref="userListTeacherMultipleTable"\n' +
    '                                      class="mgb-20"\n' +
    '                                      @selection-change="handleSelectionTeacherChange" size="mini">\n' +
    '                                <el-table-column\n' +
    '                                        type="selection"\n' +
    '                                        :selectable="selectableTeacher"\n' +
    '                                        width="60">\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="姓名">\n' +
    '                                    <template slot-scope="scope">{{scope.row.name}}</template>\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="工号">\n' +
    '                                    <template slot-scope="scope">{{scope.row.no}}</template>\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="导师来源" width="100">\n' +
    '                                    <template slot-scope="scope">{{scope.row.teacherType |\n' +
    '                                        selectedFilter(teacherTypeEntries)}}\n' +
    '                                    </template>\n' +
    '                                </el-table-column>\n' +
    '                                <el-table-column align="center" label="技术领域" width="220">\n' +
    '                                    <template slot-scope="scope">' +
    '                                       <el-tooltip :content="scope.row.domainlt" popper-class="white" placement="right">\n' +
    '                                           <span class="break-ellipsis">{{scope.row.domainlt}}</span>' +
    '                                       </el-tooltip>\n' +
    '                                    </template>\n' +
    '                                </el-table-column>\n' +
    '                            </el-table>\n' +
    '                        </div>\n' +
    '                        <div class="text-right">\n' +
    '                            <el-pagination\n' +
    '                                    size="small"\n' +
    '                                    @size-change="handlePaginationSizeChange"\n' +
    '                                    background\n' +
    '                                    @current-change="handlePaginationPageChange"\n' +
    '                                    :current-page.sync="userListParams.pageNo"\n' +
    '                                    :page-sizes="[5,10,20,50,100]"\n' +
    '                                    :page-size="userListParams.pageSize"\n' +
    '                                    layout="prev, pager, next, sizes"\n' +
    '                                    :total="userListParams.total">\n' +
    '                            </el-pagination>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </el-col>\n' +
    '            </el-row>\n' +
    '        </el-dialog>\n' +
    '    </div>',
    props: {
        teamId: {               //团队ID，请求团队成员
            type: String
        },
        disabled: Boolean,
        isAdmin: Boolean,
        operateBtnShow: {       //控制table操作列的显示与影藏
            type: Boolean,
            default: true
        },
        declareId: {
            type: String
        },
        savedTeamUrl: {
            type: [String, Boolean]
        },
        addable:{
            type: Boolean,
            default: true
        },
        firstTeacherId:{
            type: String,
            default: ''
        },
        firstTeacherIdView:{
            type: String,
            default: ''
        }
    },
    data: function () {
        return {
            technical:[],
            officeList: [],
            officeTree: [],
            officeEntity: {},
            currentStates: [],
            teacherTypes: [],
            curJoinProjects: [],
            studentList: [],
            teacherList: [],
            defaultProps: {
                label: 'name',
                children: 'children'
            },
            userListStudent: [],
            userListTeacher: [],
            userListTeacherMultipleTable: [],
            userListStudentMultipleTable: [],
            userListParams: {
                userName: '',
                userType: '',
                curJoinStr: [],
                currState: '',
                pageSize: 10,
                total: 0,
                pageNo: 1
            },
            loading: false,
            dataLoading: false,
            dialogTitle: '',
            dialogVisible: false,
            isFullUserList: false,
            highlightTree: false,
            paginationDisabled: true,
            officeTreeOriginName: '',
            studentMax: -1,
            teacherMax: -1,
            userDuties: [
                {value: '0', label: '负责人'},
                {value: '1', label: '组成员'}
            ],
            teacherDuties: [
                {value: '0', label: '第一导师'},
                {value: '1', label: '成员'}
            ]
        }
    },
    computed: {

        technicalEntries:function () {
            return this.getEntries(this.technical);
        },
        addStuAble: function () {
            if (this.tooltipStuDisabled) {
                return false;
            }
            return this.studentList.length >= this.studentMax;
        },

        addTeaAble: function () {
            if (this.tooltipStuDisabled) {
                return false;
            }
            return this.teacherList.length >= this.teacherMax;
        },

        tooltipTeaDisabled: function () {
            return (this.teacherMax == '' || this.teacherMax === -1);
        },

        tooltipStuDisabled: function () {
            return (this.studentMax == '' || this.studentMax === -1);
        },

        upStuMaxText: function () {
            return '项目组成员不得超过' + this.studentMax + '人';
        },

        upTeaMaxTex: function () {
            return '指导导师不得超过' + this.teacherMax + '人';
        },

        teacherTypeEntries: function () {
            return this.getEntries(this.teacherTypes);
        },
        searchInputPlaceholder: {
            get: function () {
                var text;
                text = this.userListParams.userType === '1' ? '学号' : '教工号';
                return '姓名 / 技术领域 / ' + text;
            }
        },
        teacherListIds: function () {
            return this.teacherList.map(function (item) {
                return item.userId;
            });
        },
        studentListIds: function () {
            return this.studentList.map(function (item) {
                return item.userId
            });
        },
        curJoinLabel: function () {
            var userType = this.userListParams.userType;
            switch (userType) {
                case "1":
                    return "当前在研";
                    break;
                case "2":
                    return "当前指导";
                    break;
            }
        },
        teamHistoryStu: function () {
            var teamId = this.teamId;
            var declareId = this.declareId;
            return this.studentList.map(function (item) {
                return {
                    userType: item.user_type || item.userType || item.utype,
                    teamId: teamId,
                    userzz: item.userzz,
                    name: item.name,
                    userId: item.userId
                }
            })
        },
        teamHistoryTea: function () {
            var self = this;
            var teamId = this.teamId;
            var obj = {};
            return this.teacherList.map(function (item) {
                obj = {
                    userType: item.user_type || item.userType || item.utype,
                    teamId: teamId,
                    userId: item.userId
                };
                if(self.firstTeacherId){
                    obj['teacherzz'] = item.teacherzz;
                }
                return obj;
            })
        }
    },
    watch: {
        teamId: function (value) {
            this.getMember();
        }
    },
    methods: {
        selectableStudent: function (row) {
            return this.studentListIds.indexOf(row.id) === -1;
        },
        selectableTeacher: function (row) {
            return this.teacherListIds.indexOf(row.id) === -1;
        },
        getMember: function () {
            var self = this;
            this.$axios.get('/team/findTeamPerson?teamId=' + self.teamId).then(function (response) {
                var data = response.data || {};
                if (data.status === 1) {
                    data = data.data;
                    var stuList = data.stuList || [];
                    var teaList = data.teaList || [];
                    self.studentList = stuList.map(function (item) {
                        item['userzz'] = item.userId === self.declareId ? '0' : '1';
                        return item;
                    });
                    self.teacherList = teaList.map(function (item) {
                        if(self.firstTeacherId){
                            item['teacherzz'] = item.userId == self.firstTeacherId ? '0' : '1';
                        }
                        return item;
                    });
                }

                self.$emit('change', {
                    studentListIds: self.studentListIds,
                    teacherListIds: self.teacherListIds,
                    studentList: self.teamHistoryStu,
                    teacherList: self.teamHistoryTea
                });
            })
        },
        handleSelectionStudentChange: function (val) {
            this.userListStudentMultipleTable = val;
        },
        handleSelectionTeacherChange: function (val) {
            this.userListTeacherMultipleTable = val;
        },
        //重置pagination
        resetPagination: function () {
            this.userListParams.pageNo = 1;
            this.userListParams.pageSize = 10;
        },

        //重置officeId
        resetCollegeId: function () {
            this.userListParams.professionId = '';
            this.userListParams['office.id'] = '';
        },
        resetHighlightTree: function () {
            this.highlightTree = false;
        },
        //重置查找用户参数
        resetUserListParams: function () {
            this.userListParams.curJoinStr = [];
            this.userListParams.currState = '';
            this.userListParams.userName = '';
        },
        inviteStudent: function () {
            var self = this;
            if (this.studentList.length >= this.studentMax && this.studentMax != -1) {
                this.$message({
                    message: '项目组成员不得超过' + self.studentMax + '人',
                    type: 'warning'
                });
                return false;
            }
            this.dialogTitle = '邀请组员';
            this.userListParams.userType = '1';
            this.userListParams.teacherType = '';
            this.resetPagination();
            this.resetCollegeId();
            this.resetHighlightTree();
            this.resetUserListParams();
            this.officeTree[0].name = this.officeTreeOriginName;
            this.getUserList();
            this.dialogVisible = true;
        },
        inviteTeacher: function () {
            this.dialogTitle = '邀请导师';
            this.userListParams.userType = '2';
            this.userListParams.teacherType = '';
            this.resetPagination();
            this.resetCollegeId();
            this.resetHighlightTree();
            this.resetUserListParams();
            this.officeTree[0].name = '校园导师';
            this.getUserList();
            this.dialogVisible = true;
        },
        sendInvite: function () {
            var userType = this.userListParams.userType;
            var self = this;
            var len, tableLength, selectedLength;
            if (userType == '1') {
                this.sendInviteStudents();
            } else if (userType == '2') {
                this.sendInviteTeachers();
            }
        },

        sendInviteStudents: function () {
            var userType = this.userListParams.userType;
            var self = this;
            var len, tableLength, selectedLength;
            len = this.studentList.length + this.userListStudentMultipleTable.length;
            if (len > this.studentMax  && this.studentMax != -1) {
                this.$message({
                    message: '项目组成员不得超过' + self.studentMax + '人',
                    type: 'warning'
                });
                return false;
            }
            var ids = this.userListStudentMultipleTable.map(function (item) {
                return item.id
            })
            this.$axios.post('/team/getUserStudentTeam', ids).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var student = data.data || [];
                    student = student.map(function (item) {
                        item['userzz'] = item.userId === self.declareId ? '0' : '1';
                        item["userType"] = item.userType;
                        return item;
                    })
                    self.studentList = self.studentList.concat(student);
                    self.$emit('change', {
                        studentListIds: self.studentListIds,
                        teacherListIds: self.teacherListIds,
                        studentList: self.teamHistoryStu,
                        teacherList: self.teamHistoryTea
                    });
                    self.handleClose();
                    return false;
                }
                self.$message.error(data.msg)
            });

        },

        sendInviteTeachers: function () {
            var userType = this.userListParams.userType;
            var self = this;
            var tableLength;
            tableLength = this.teacherList.filter(function (item) {
                return item.teacherType == '校园导师';
            }).length;
            if (tableLength >= this.teacherMax  && this.teacherMax != -1) {
                this.$message({
                    message: '指导导师不得超过' + self.teacherMax + '人',
                    type: 'warning'
                });
                return false;
            }
            var ids = this.userListTeacherMultipleTable.map(function (item) {
                return item.id
            })
            this.$axios.post('/team/getUserTeacherTeam', ids).then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var teacher = data.data || [];
                    teacher = teacher.map(function (item) {
                        if(self.firstTeacherId){
                            item['teacherzz'] = item.userId == self.firstTeacherId ? '0' : '1';
                        }
                        item["userType"] = item.userType;
                        return item;
                    })
                    self.teacherList = self.teacherList.concat(teacher);
                    self.$emit('change', {
                        studentListIds: self.studentListIds,
                        teacherListIds: self.teacherListIds,
                        studentList: self.teamHistoryStu,
                        teacherList: self.teamHistoryTea
                    });
                    self.handleClose();
                    return false;
                }
                self.$message.error(data.msg)
            });

        },

        deleteStudent: function ($index) {
            this.studentList.splice($index, 1);
            this.$emit('change', {
                studentListIds: this.studentListIds,
                teacherListIds: this.teacherListIds,
                studentList: this.teamHistoryStu,
                teacherList: this.teamHistoryTea
            });
        },
        deleteTeacher: function ($index) {
            this.teacherList.splice($index, 1);
            this.$emit('change', {
                studentListIds: this.studentListIds,
                teacherListIds: this.teacherListIds,
                studentList: this.teamHistoryStu,
                teacherList: this.teamHistoryTea
            });
        },


        handleClose: function () {
            this.$refs.userListTeacherMultipleTable.clearSelection();
            this.$refs.userListStudentMultipleTable.clearSelection();
            this.dialogVisible = false;
        },

        //查找用户
        handleNodeClick: function (data) {
            var grade = data.grade;
            this.highlightTree = true;
            switch (grade) {
                case "2":
                    this.userListParams['office.id'] = data.id;
                    this.userListParams.professionId = '';
                    break;
                case "3":
                case "4":
                    this.userListParams['office.id'] = '';
                    this.userListParams.professionId = data.id;
                    break;
                default:
                    this.userListParams['office.id'] = '';
                    this.userListParams.professionId = '';
            }

            this.userListParams.grade = (data.grade === '2' || data.grade === '3') ? data.grade : '';
            this.userListParams.teacherType = '1';
            this.userListParams.pageNo = 1;
            this.getUserList();
        },
        //获取查找用户参数
        getUserListParams: function () {
            var userListParamsCopy = JSON.parse(JSON.stringify(this.userListParams));
            userListParamsCopy.curJoinStr = userListParamsCopy.curJoinStr.join(',');
            return userListParamsCopy;
        },
        getUserList: function () {
            var self = this;
            var teamMemberXhr;
            this.paginationDisabled = true;
            this.dataLoading = true;
            this.$axios.get('/sys/user/ajaxUserListTree',{params: this.getUserListParams()}).then(function (response) {
                var data = response.data;
                if (data.status) {
                    data = data.datas;
                    self[self.userListParams.userType === '1' ? 'userListStudent' : 'userListTeacher'] = data.list;
                    self.userListParams.pageSize = data.pageSize;
                    self.userListParams.total = data.total;
                    self.userListParams.pageNo = data.pageNo;
                    self.paginationDisabled = false;
                }
                self.dataLoading = false;
            }).catch(function (error) {
                self.dataLoading = false;
            })
        },

        getCompanyTeacherList: function () {
            this.userListParams.teacherType = '2';
            this.userListParams['office.id'] = '';
            this.userListParams.professionId = '';
            this.getUserList();
        },
        handlePaginationSizeChange: function (value) {
            this.userListParams.pageSize = value;
            this.userListParams.pageNo = 1;
            this.getUserList()
        },


        handlePaginationPageChange: function (value) {
            this.userListParams.pageNo = value;
            this.getUserList()
        },

        getOfficeList: function () {
            var self = this;
            var rootOfficeList = this.$root.officeList;
            if(rootOfficeList && rootOfficeList.length > 0){
                this.setOfficeTool(rootOfficeList);
                return false;
            }

            this.$axios.get('/sys/office/getOfficeList').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var officeList = data.data || [];
                    self.setOfficeTool(officeList)
                }
            });
        },

        setOfficeTool: function (officeList) {
            var treeEntity = this.listToTreeEntity(officeList);
            if(officeList.length > 0){
                this.officeTree = JSON.parse(JSON.stringify(treeEntity.tree));
                this.officeTreeOriginName = treeEntity.tree[0].name;
                this.officeEntity = treeEntity.entity;
                this.officeList = officeList;
            }
        },

        handleChangeChargeMen: function (row) {
            var studentList = this.studentList;
            for (var i = 0; i < studentList.length; i++) {
                if (studentList[i].userzz === row.userzz && studentList[i].id !== row.id) {
                    studentList[i].userzz = '1';
                    break;
                }
            }
            this.$emit('change', {
                studentListIds: this.studentListIds,
                teacherListIds: this.teacherListIds,
                studentList: this.teamHistoryStu,
                teacherList: this.teamHistoryTea
            });
        },
        handleChangeChargeTeacher: function (row) {
            var teacherList = this.teacherList;
            for (var i = 0; i < teacherList.length; i++) {
                if (teacherList[i].teacherzz === row.teacherzz && teacherList[i].id !== row.id) {
                    teacherList[i].teacherzz = '1';
                    break;
                }
            }
            this.$emit('change', {
                studentListIds: this.studentListIds,
                teacherListIds: this.teacherListIds,
                studentList: this.teamHistoryStu,
                teacherList: this.teamHistoryTea
            });
        },

        getTeacherTypes: function () {
            var self = this;
            this.$axios.get('/sys/dict/getDictList?type=master_type').then(function (response) {
                self.teacherTypes = response.data || [];
            })
        },

        getCurrentSate: function () {
            var self = this;
            this.$axios.get('/sys/dict/getDictList?type=current_sate').then(function (response) {
                self.currentStates = response.data || [];
            })
        },
        getCurJoinProjects: function () {
            var self = this;
            this.$axios.get('/sys/dict/getCurJoinProjects').then(function (response) {
                self.curJoinProjects = response.data || [];
            })
        },
        getTechnicals: function () {
            var self = this;
            this.$axios.get('/sys/dict/getDictList?type=postTitle_type').then(function (response) {
                var data = response.data;
                self.technical = data || [];
            })
        },
        getSaveTeam: function () {
            var savedTeamUrl = this.savedTeamUrl;
            var self = this;
            this.$axios.get(savedTeamUrl).then(function (response) {
                var data = response.data || {};
                if (data.status === 1) {
                    data = data.data;
                    if(data){
                        var stuList = data.stus || [];
                        var teaList = data.teas || [];
                        self.studentList = stuList.map(function (item) {
                            item['userzz'] = item.userId === self.declareId ? '0' : '1';
                            return item;
                        });
                        self.teacherList = teaList.map(function (item) {
                            if(self.firstTeacherId){
                                item['teacherzz'] = item.userId == self.firstTeacherId ? '0' : '1';
                            }
                            return item;
                        });
                    }
                }
                self.$emit('change', {
                    studentListIds: self.studentListIds,
                    teacherListIds: self.teacherListIds,
                    studentList: self.teamHistoryStu,
                    teacherList: self.teamHistoryTea
                });
            })
        }
    },
    created: function () {
        if (this.savedTeamUrl) {
            this.getSaveTeam();
        } else if (this.teamId) {
            this.getMember();
        }
        this.getTechnicals();
        if(this.addable){
            this.getOfficeList();
            this.getTeacherTypes();
            this.getCurrentSate();
            this.getCurJoinProjects();
        }
    }
});