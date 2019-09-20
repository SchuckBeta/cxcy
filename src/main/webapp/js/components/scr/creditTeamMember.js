'use strict';

Vue.component('credit-team-member', {
    template: '<div class="update-member-box">\n' +
    '                <div v-if="addable" class="text-right mgb-20"><span style="margin-right: 20px; color: red" v-if=scoRatios[scoRatioLenString]>学分配比：{{scoRatios[scoRatioLenString]}}</span><el-tooltip class="item" popper-class="white"  effect="dark" :disabled="tooltipStuDisabled"  :content="upStuMaxText" placement="top"><el-button class="btn-add" :disabled="disabled" type="primary" size="mini"' +
    '                              @click.stop.prevent="inviteStudent"><i class="el-icon-circle-plus el-icon--left"></i>添加组员\n' +
    '                           </el-button></el-tooltip></div><e-col-item :label-width="colItemLabelWidth" :class="colItemClass" :required="labelRequired" :label="colItemLabel">\n' +
    '                           <div v-if="isAudit" class="text-right" style="margin-top: -20px; "><span style="color: red" v-if=scoRatios[scoRatioLenString]>学分配比：{{scoRatios[scoRatioLenString]}}</span></div><div class="table-container">\n' +
    '                        <el-table size="mini" :data="studentList" class="table" ref="multipleTable" v-loading="loading">\n' +
    '                            <el-table-column label="姓名"  align="center" :width="nameColumnWidth"><template slot-scope="scope"><div v-if="isAudit">{{scope.row.user.name}}</div><div v-else class="user-info-box"><span class="user-pic"><img :src="scope.row.user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></span>{{scope.row.user.name}}</div></template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="所属学院/专业" align="center">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                    <div>{{scope.row.user.office || scope.row.user.officeName}}<br/>{{scope.row.user.professional}}</div>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column v-if="!isAudit" prop="user.mobile" label="联系电话" align="center"><template slot-scope="scope">{{scope.row.user.mobile}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column v-if="!isAudit" prop="user.email" label="电子邮箱" align="center"><template slot-scope="scope">{{scope.row.user.email}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="团队职责" align="center">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                    <span v-if="scope.row.user.userId == declareId">负责人</span>\n' +
    '                                    <span v-else>组成员</span>' +
    '                                </template>\n' +
    '                            </el-table-column>' +
    '                            <el-table-column v-if="!(studentList.length === 1 && !isAudit)" prop="rate" label="学分配比" align="center">' +
    '                               <template slot-scope="scope">' +
    '                                       <div class="el-form-item-credit" :class="{\'is-error\': scope.row.rateError}"><el-input class="w64" size="mini" :disabled="disabled || studentList.length === 1" maxlength="3" v-model="scope.row.rate" @change="handleChangeScoreRatio(scope.row)"></el-input></div>' +
    '                               </template>' +
    '                           </el-table-column>' +
    '                               <el-table-column v-if="hasaffirmscore" prop="aFScore" label="认定学分" align="center">' +
    '                               <template slot-scope="scope"><div class="el-form-item-credit" :class="{\'is-error\': scope.row.aFScoreError}">' +
    '                           <el-input class="w64" size="mini" :disabled="disabled" v-model="scope.row.aFScore" maxlength="14" @change="handleChangeAFScore(scope.row)"></el-input></div></template>' +
    '                           </el-table-column>\n' +
    '                            <el-table-column label="操作" width="64" align="center" v-if="addable">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                        <el-button icon="el-icon-delete" size="mini" :disabled="(scope.row.user.userId == declareId) || disabled"\n' +
    '                                           @click.stop.prevent="deleteStudent(scope.$index)"></el-button>\n' +
    '                                </template>\n' +
    '                            </el-table-column>\n' +
    '                        </el-table>\n' +
    '                    </div>\n' +
    '                       </e-col-item>\n' +
    '        <el-dialog v-if="!isAudit" :title="dialogTitle" :visible.sync="dialogVisible" top="5vh" :close-on-click-modal="false" width="1100px" :before-close="handleClose">\n' +
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
    '                                   :disabled="userListStudentMultipleTable.length < 1"\n' +
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
    '                        <el-tree :data="collegesTree" :props="defaultProps"\n' +
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
        memberList: Array,
        addable: Boolean,
        disabled: Boolean,
        declareId: String,
        scoRatios: {
            type: Object
        },
        keepNpoint: {
            type: String,
            default: '0'
        },
        isRound: Boolean, //是否处理四舍五入
        ratioMax: Number,
        totalScore: [String, Number],
        isAudit: Boolean, //审核， 默认申报
        hasaffirmscore: Boolean //认定学分
    },
    data: function () {
        return {
            currentStates: [],
            curJoinProjects: [],
            studentList: [],
            defaultProps: {
                label: 'name',
                children: 'children'
            },
            userListStudent: [],
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
            userDuties: [
                {value: '0', label: '负责人'},
                {value: '1', label: '组成员'}
            ],
            scoreReg: /^(([1-9]{1}\d{0,10}|0)(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,7})\d{0,3}$/,
        }
    },
    computed: {
        studentMax: function () {
            return this.ratioMax;
        },
        collegesTree: {
            get: function () {
                return this.$root.collegesTree;
            }
        },
        scoRatioLenString: function () {
            return this.studentList.length.toString();
        },
        labelRequired: function () {
            return this.isAudit
        },
        colItemLabel: function () {
            return this.isAudit ? '组员准予认定学分：' : (this.addable ? '认定组成员：' : '')
        },
        colItemClass: function () {
            return {
                "is-credit-audit": this.isAudit
            }
        },
        colItemLabelWidth: function () {
            return this.isAudit ? '0' : '110px'
        },
        nameColumnWidth: function () {
            return this.isAudit ? '' : '210'
        },

        addStuAble: function () {
            if (this.tooltipStuDisabled) {
                return false;
            }
            return this.studentList.length >= this.studentMax;
        },


        tooltipStuDisabled: function () {
            return (this.studentMax === '' || this.studentMax === -1);
        },

        upStuMaxText: function () {
            return '项目组成员不得超过' + this.studentMax + '人';
        },

        searchInputPlaceholder: {
            get: function () {
                var text;
                text = this.userListParams.userType === '1' ? '学号' : '教工号';
                return '姓名 / 技术领域 / ' + text;
            }
        },

        studentListIds: function () {
            return this.studentList.map(function (item) {
                return item.user.userId
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

        keepB: function () {
            if(this.keepNpoint == 0){
                return 1;
            }else if(this.keepNpoint == 1){
                return 10;
            }else if(this.keepNpoint == 2){
                return 100;
            }else {
                return 1000;
            }
        }

    },
    watch: {
        // totalScore: function (value) {
        //     if(typeof value != 'undefined' && value != ''){
        //         var ratiosTotal = this.getRatiosTotal();
        //         var ratios = ratiosTotal.ratios;
        //         var total = ratiosTotal.total;
        //         if(total){
        //             var totalScore = this.totalScore;
        //             var keepNpoint = this.keepNpoint;
        //             this.studentList.forEach(function (item, index) {
        //                 var aFScore = ratios[index]/total*totalScore;
        //                 item.aFScore = aFScore.toFixed(parseInt(keepNpoint))
        //             })
        //         }
        //     }
        // }
    },
    methods: {

        handleChangeScoreRatio: function (row) {
            var rate = row.rate;
            if (typeof rate !== 'undefined' && rate !== '') {
                var isError = !(/^[1-9]{1}\d*$/.test(rate));
                if (isError) {
                    this.$message.error('请输入最多三位数正整数')
                }
                Vue.set(row, 'rateError', isError);
                if (!isError) {
                    Vue.set(row, 'aFScoreError', false);
                    this.setAFScore();
                } else {
                    this.$emit('change', this.studentList);
                }
                return false;
            }
            Vue.set(row, 'rateError', true);
        },

        setAFScore: function () {
            var ratiosTotal = this.getRatiosTotal();
            var ratios = ratiosTotal.ratios;
            var total = ratiosTotal.total;
            if (total > 0) {
                var totalScore = this.totalScore;
                var keepNpoint = parseInt(this.keepNpoint);
                var isRound = this.isRound;
                var keepB = this.keepB;
                if(this.studentList.length === 1){
                    this.studentList.forEach(function (item) {
                        item.aFScore = totalScore;
                    });
                }else {
                    this.studentList.forEach(function (item, index) {
                        var aFScore = ratios[index] / total * totalScore;
                        Vue.set(item, 'aFScoreError', false);
                        var score = aFScore.toString();
                        if (isRound) {
                            score = score.replace(/^\d+\.?(\d*)/, function ($1, $2) {
                                var len = $2.length;
                                var num$1 = parseInt($1);
                                if (keepNpoint === 0) {
                                    return num$1;
                                }
                                if (len === keepNpoint) {
                                    return $1;
                                }
                                if (keepNpoint > len) {
                                    var arr = [];
                                    var i = 0;
                                    var leftPoint = keepNpoint - len;
                                    while (i < leftPoint) {
                                        arr.push('0');
                                        i++
                                    }
                                    return num$1 + '.' + $2 + arr.join('');
                                }
                                return num$1 + '.' + $2.substring(0, keepNpoint);

                            })
                        } else {
                            aFScore = aFScore.toString();
                            var afIndex = aFScore.indexOf('.');
                            if(afIndex === -1){
                                score = parseFloat(aFScore).toFixed(keepNpoint);
                            }else {
                                var nAfScore = aFScore.substring(afIndex+1);
                                if(nAfScore.length > keepNpoint){
                                    aFScore = aFScore.substring(0, afIndex + 1 + 1 + keepNpoint);
                                    aFScore = parseFloat(aFScore);

                                    score = Math.round(aFScore * keepB)/keepB;
                                }
                            }

                        }
                        item.aFScore = parseFloat(score).toFixed(keepNpoint);
                    })
                }

            }
            this.$emit('change', this.studentList);
        },

        getRatiosTotal: function () {
            var i = 0;
            var total = 0;
            var ratios = [];
            var studentList = this.studentList;
            while (i < studentList.length) {
                var rate = studentList[i].rate;
                if (typeof rate !== 'undefined' && rate !== '') {
                    total += parseInt(rate);
                    ratios.push(parseInt(rate));
                } else {
                    total = 0;
                    break;
                }
                i++
            }
            return {
                total: total,
                ratios: ratios
            };
        },


        handleChangeAFScore: function (row) {
            var aFScore = row.aFScore;
            if (typeof aFScore !== 'undefined' && aFScore !== '') {
                if (/^\d+$/.test(aFScore)) {
                    Vue.set(row, 'aFScoreError', false)
                } else {
                    if (/^\d+/.test(aFScore)) {
                        var reg = '\\.\\d\{1\,' + this.keepNpoint + '\}\$';
                        if (new RegExp(reg).test(aFScore)) {
                            Vue.set(row, 'aFScoreError', false)
                        } else {
                            this.$message.error('请输入最多' + this.keepNpoint + '位小数的正整数');
                            Vue.set(row, 'aFScoreError', true);
                        }
                    } else {
                        this.$message.error('请输入最多' + this.keepNpoint + '位小数的正整数');
                        Vue.set(row, 'aFScoreError', true);
                    }
                }
            } else {
                this.$message.error('请输入最多' + this.keepNpoint + '位小数的正整数');
                Vue.set(row, 'aFScoreError', true);
            }
            this.$emit('change', this.studentList);
        },

        inviteStudent: function () {
            var self = this;
            if (this.studentList.length >= this.studentMax && this.studentMax !== -1) {
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
            this.getUserList();
            this.dialogVisible = true;
        },

        sendInvite: function () {
            var userType = this.userListParams.userType;
            if (userType === '1') {
                this.sendInviteStudents();
            }
        },

        sendInviteStudents: function () {
            var userType = this.userListParams.userType;
            var self = this;
            var len, tableLength, selectedLength;
            len = this.studentList.length + this.userListStudentMultipleTable.length;
            if (len > this.studentMax && this.studentMax !== -1) {
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
                        return {
                            id: '',
                            user: item,
                            rate: ''
                        }
                    })
                    self.studentList = self.studentList.concat(student);
                    self.$emit('change', self.studentList);
                    self.handleClose();
                    return false;
                }
                self.$message.error(data.msg)
            });

        },


        deleteStudent: function ($index) {
            this.studentList.splice($index, 1);
            this.$emit('change', this.studentList)
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

        handleClose: function () {
            this.$refs.userListStudentMultipleTable.clearSelection();
            this.dialogVisible = false;
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

        handleSelectionStudentChange: function (val) {
            this.userListStudentMultipleTable = val;
        },


        selectableStudent: function (row) {
            return this.studentListIds.indexOf(row.id) === -1;
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

        // getCreditRule: function () {
        //     var self = this;
        //     this.$axios.get('/scr/scoRset/list').then(function (response) {
        //         var data = response.data;
        //         if (data.status === 1) {
        //             data = data.data || [];
        //             if (data.length > 0) {
        //                 self.keepNpoint = data[0].keepNpoint;
        //             }
        //         }
        //     }).catch(function () {
        //
        //     })
        // },
    },
    created: function () {
        if (!this.isAudit) {
            this.getCurrentSate();
            this.getCurJoinProjects();
        }
        var studentList = [].concat(this.memberList || []);
        for(var i = 0; i < studentList.length; i ++){
            if(studentList[i].user.userId == this.declareId){
                studentList.unshift(studentList[i]);
                studentList.splice(i + 1,1);
                break;
            }
        }
        this.studentList = studentList;

    }
});