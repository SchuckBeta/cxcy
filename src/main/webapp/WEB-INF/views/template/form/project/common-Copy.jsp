

'use strict';

Vue.component('button-common-imp-exp', {
    template: '<div>\n' +
    '    <el-button type="primary" size="mini" @click.stop.prevent="commonImpExp"><i class="iconfont icon-daochu"></i>导入</el-button>\n' +
    '\n' +
    '\n' +
    '    <el-dialog :title="iepTpl.name" :visible.sync="dialogVisible" top="2vh" width="900px" class="tem-step-dialog"\n' +
    '               :before-close="handleClose"\n' +
    '               :close-on-click-modal="false">\n' +
    '\n' +
    '        <div style="text-align: center;">\n' +
    '            <div class="tem-step common-step">\n' +
    '                <div v-if="iepTpl.level == \'10\'" class="tem-step-item" v-for="(item,index) in iepTpl.childrens"\n' +
    '                     :key="item.id">\n' +
    '                    <div class="tem-step-arrow" v-if="index != 0">\n' +
    '                        <i :class="{\'iconfont\':true,\'icon-iconset0438\':true,\'tem-step-arrow-color\':item.curr == 0}"></i>\n' +
    '                    </div>\n' +
    '                    <div class="tem-step-todo">\n' +
    '                        <div v-for="(child,index) in item.childrens">\n' +
    '                            <p class="one-step" v-if="index == \'0\'" :class="{\'one-step-background\':item.curr == 0}">\n' +
    '                                第{{item.step}}步：{{child.name}}{{item.name}}</p>\n' +
    '                            <button-import-contest v-if="child.operType == \'10\'" :child="child" :actyw-id="actywId"\n' +
    '                                                   :gnode-id="gnodeId" :level="iepTpl.level" :item="item"\n' +
    '                                                   :item-curr="item.curr"\n' +
    '                                                   @upload-success="getDataList"></button-import-contest>\n' +
    '                            <el-button size="mini" type="primary" v-if="child.operType == \'20\'"\n' +
    '                                       style="margin-bottom: 10px;"\n' +
    '                                       @click.stop.prevent="exportFile(child.id)">{{child.name}}{{item.name}}\n' +
    '                            </el-button>\n' +
    '                            <p v-if="child.operType == \'30\'" class="download-tem"\n' +
    '                               @click.stop.prevent="downloadFile(item.id)">\n' +
    '                                {{child.name}}</p>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '\n' +
    '            </div>\n' +
    '        </div>\n' +
    '\n' +
    '\n' +
    '        <div v-if="iepTpl.level == \'20\'" class="tem-one-item common-step">\n' +
    '            <div class="search-block_bar clearfix">\n' +
    '                <div class="search-btns">\n' +
    '                <span v-for="item in iepTpl.childrens" :key="item.id">\n' +
    '                    <el-button v-if="item.operType == \'30\'" size="mini" type="primary"\n' +
    '                               @click.stop.prevent="downloadFile(item.id)">{{item.name}}</el-button>\n' +
    '\n' +
    '                    <button-import-contest v-if="item.operType == \'10\'" :child="item" :actyw-id="actywId"\n' +
    '                                           :level="iepTpl.level"\n' +
    '                                           :gnode-id="gnodeId" @upload-success="getDataList"></button-import-contest>\n' +
    '\n' +
    '                    <el-button v-if="item.operType == \'20\'" size="mini" type="primary"\n' +
    '                               @click.stop.prevent="exportFile(item.id)">{{item.name}}\n' +
    '                    </el-button>\n' +
    '                </span>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '        </div>\n' +
    '\n' +
    '        <div class="tem-data">\n' +
    '            <div class="tem-data-title">\n' +
    '            <span>查看导入结果<i :class="{\'el-icon-caret-top\':isShowDataList,\'el-icon-caret-bottom\':!isShowDataList}"\n' +
    '                           @click.stop.prevent="isShowDataList = !isShowDataList"></i></span>\n' +
    '                <hr>\n' +
    '            </div>\n' +
    '            <div class="table-container" v-show="isShowDataList" style="box-shadow: none">\n' +
    '                <el-table :data="pageList" ref="pageList" class="table" size="mini" v-loading="loading" :span-method="arraySpanMethod"\n' +
    '                          max-height="263">\n' +
    '                    <el-table-column prop="filename" label="数据类型" align="left" min-width="100">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            <el-tooltip :content="scope.row.filename" popper-class="white"\n' +
    '                                        placement="right">\n' +
    '                                <span class="break-ellipsis">{{scope.row.filename}}</span>\n' +
    '                            </el-tooltip>\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="createDate" label="导入时间" align="center" min-width="150">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.createDate}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="total" label="数据总数" align="center" min-width="80">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            <span v-if="!scope.row.errmsg">{{scope.row.total}}</span>   \n' +
        '                        <span v-else style="color:#E9432D">\n' +
        '                            {{scope.row.errmsg}}\n' +
        '                        </span>\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="todoNum" label="导入成功数" align="center" min-width="100">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.success}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="fail" label="导入失败数" align="center" min-width="100">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.fail}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column prop="isComplete" label="状态" align="center" min-width="70">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            {{scope.row.isComplete | selectedFilter(isCompleteLabelEntries)}}\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                    <el-table-column label="操作" align="center" min-width="140">\n' +
    '                        <template slot-scope="scope">\n' +
    '                            <div class="table-btns-action">\n' +
    '                                <el-dropdown @command="handleCommand" trigger="click" size="small" v-if="!scope.row.errmsg && scope.row.isComplete == \'1\'">\n' +
    '                                    <span class="el-dropdown-link" style="color:#e9432d;cursor:pointer;">\n' +
    '                                        下载数据\n' +
    '                                    </span>\n' +
    '                                    <el-dropdown-menu slot="dropdown">\n' +
    '                                        <el-dropdown-item :command="scope.row.id + \'s\'">下载成功数据</el-dropdown-item>\n' +
    '                                        <el-dropdown-item :command="scope.row.id + \'e\'" v-if="scope.row.total != scope.row.success">下载错误数据</el-dropdown-item>\n' +
    '                                    </el-dropdown-menu>\n' +
    '                                </el-dropdown>\n' +
    '                                <el-button type="text" size="mini" @click.stop.prevent="delRecord(scope.row)">删除\n' +
    '                                </el-button>\n' +
    '                            </div>\n' +
    '                        </template>\n' +
    '                    </el-table-column>\n' +
    '                </el-table>\n' +
    '                <div class="text-right mgb-20" v-if="pageCount">\n' +
    '                    <el-pagination\n' +
    '                            size="small"\n' +
    '                            @size-change="handlePaginationSizeChange"\n' +
    '                            background\n' +
    '                            @current-change="handlePaginationPageChange"\n' +
    '                            :current-page.sync="searchListForm.pageNo"\n' +
    '                            :page-sizes="[5,10,20,50,100]"\n' +
    '                            :page-size="searchListForm.pageSize"\n' +
    '                            layout="total, prev, pager, next, sizes"\n' +
    '                            :total="pageCount">\n' +
    '                    </el-pagination>\n' +
    '                </div>\n' +
    '            </div>\n' +
    '\n' +
    '\n' +
    '        </div>\n' +
    '    </el-dialog></div>\n',
    props: {
        iepTplId:String,
        operTypeBtn:String
    },
    data: function () {
        return {
            iepTpl: {},
            pageList: [],
            loading: false,
            pageCount: 0,
            searchListForm: {
                pageNo: 1,
                pageSize: 5
            },
            isShowDataList: true,
            isCompleteLabel: [
                {label: '完成', value: '1'},
                {label: '处理中', value: '0'}
            ],
            originalPageList: [],
            timerInterval: null,
            dialogVisible:false
        }
    },
    computed: {
        actywId: function () {
            return this.$root.actywId;
        },
        gnodeId: function () {
            return this.$root.gnodeId;
        },
        isCompleteLabelEntries: function () {
            return this.getEntries(this.isCompleteLabel);
        }
    },
    methods: {
        arraySpanMethod:function (row) {
            if(row.row.errmsg){
                if(row.columnIndex === 2){
                    return [1,3];
                }else if(row.columnIndex === 3 || row.columnIndex === 4){
                    return [0,0];
                }
            }
        },
        commonImpExp: function () {
            this.dialogVisible = true;
            this.getImpExpData();
            this.getDataList();
        },
        handleClose:function () {
            this.dialogVisible = false;
            this.searchListForm.pageNo = 1;
            this.searchListForm.pageSize = 5;
            this.isShowDataList = true;
            this.timerInterval && clearInterval(this.timerInterval);
            this.$emit('imp-exp-success');
        },
        getImpExpData: function () {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/iep/iepTpl/ajaxIepTpl?id=' + self.iepTplId
            }).then(function (response) {
                var data = response.data;
                self.iepTpl = data || {};
                if (self.iepTpl.level == '10') {
                    self.iepTpl.childrens.forEach(function (item) {
                        item.childrens.sort(function (a, b) {
                            return parseInt(a.operType) - parseInt(b.operType);
                        });
                        item.childrens.forEach(function (child) {
                            child.isUploading = false;
                        })
                    });
                } else if (self.iepTpl.level == '20') {
                    self.iepTpl.childrens.forEach(function (item) {
                        item.isUploading = false;
                    });
                }
            }).catch(function () {

            })
        },
        exportFile: function (id) {
            // <%--location.href = '${ctx}/iep/ie/downEdata?id=' + id;--%>
        },
        downloadFile: function (id) {
            location.href = '/a/iep/ie/downTpl?id=' + id;
        },
        getImpInfoList: function () {
            var self = this;
            var pageList = self.pageList;
            var unCompleteList = pageList.filter(function (item) {
                return item.isComplete != '1';
            });
            var params = unCompleteList.map(function (item) {
                return {id: item.id};
            });

            if (unCompleteList.length == 0) {
                this.timerInterval && clearInterval(this.timerInterval);
                return false;
            }
            this.$axios({
                method: 'POST',
                url: '/iep/ie/ajaxIepInfo?iepId=' + self.iepTplId + '&operType=' + self.operTypeBtn,
                data: params
            }).then(function (response) {
                var data = response.data.datas || {};
                self.pageList.forEach(function (item) {
                    var impInfo = data[item.id];
                    if (impInfo) {
                        Object.assign(item, impInfo);
                        item.isComplete = impInfo.isComplete;
                        self.getImpExpData();
                    }
                });
            });
        },
        getDataList: function () {
            var self = this;
            this.loading = true;
            this.$axios({
                method: 'GET',
                url: '/iep/ie/ajaxIepInfos?id=' + self.iepTplId + '&operType=' + self.operTypeBtn
            }).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.originalPageList = data.datas ? JSON.parse(JSON.stringify(data.datas)) : [];
                    self.pageCount = data.datas ? data.datas.length : 0;
                    self.pageList = self.getCurrentPageList();
                }else {
                    self.$message({
                        message: data.status ? '导入成功' : data.msg || '导入失败',
                        type: data.status ? 'success' : 'error'
                    })
                }
                self.loading = false;
                self.requestUnComplete();
            }).catch(function () {
                self.loading = false;
            })
        },
        getCurrentPageList: function () {
            var pageMaxNo = Math.ceil(this.pageCount / this.searchListForm.pageSize) || 1;
            if (this.searchListForm.pageNo > pageMaxNo) {
                this.searchListForm.pageNo = pageMaxNo;
            }
            var list = [], startIndex = this.searchListForm.pageNo * this.searchListForm.pageSize;
            list = this.originalPageList.slice(startIndex - this.searchListForm.pageSize, startIndex);
            return list;
        },
        requestUnComplete: function () {
            var self = this;
            var unCompleteList = this.pageList.filter(function (item) {
                return item.isComplete != '1';
            });
            if (unCompleteList.length < 1) {
                return false;
            }
            this.timerInterval = setInterval(function () {
                self.getImpInfoList()
            }, 8000);
        },
        handleCommand:function (command) {
            var type = command.substr(command.length-1,1);
            var id = command.substring(0,command.length-1);
            if(type == 's'){
                // location.href = '/a/iep/ie/downSdata?id=' + id + '&operType=' + this.operTypeBtn;   //下载成功数据
            }else if(type == 'e'){
                location.href = '/a/iep/ie/downEdata?id=' + id + '&operType=' + this.operTypeBtn;
            }
        },
        delRecord: function (row) {
            var self = this;
            this.$confirm('确认删除吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(function () {
                self.delRecordRequest(row)
            }).catch(function () {

            })
        },
        delRecordRequest: function (row) {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/iep/ie/delRecord?id=' + row.id + '&operType=' + self.operTypeBtn + '&iepId=' + row.impTpye
            }).then(function (response) {
                var data = response.data;
                if (data.status) {
                    self.getDataList();
                }
                self.$message({
                    message: data.status ? '删除成功' : '删除失败',
                    type: data.status ? 'success' : 'error'
                })
            }).catch(function (error) {
                self.$message({
                    type: 'error',
                    message: self.xhrErrorMsg
                })
            })
        },
        handlePaginationSizeChange: function (value) {
            this.searchListForm.pageSize = value;
            this.timerInterval && clearInterval(this.timerInterval);
            this.pageList = this.getCurrentPageList();
            this.requestUnComplete();
        },
        handlePaginationPageChange: function (value) {
            this.searchListForm.pageNo = value;
            this.timerInterval && clearInterval(this.timerInterval);
            this.pageList = this.getCurrentPageList();
            this.requestUnComplete();
        }
    }
});




































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
    '                    <div class="table-container">\n' +
    '                        <el-table size="mini" :data="teacherList" class="table" ref="multipleTable" v-loading="loading">\n' +
    '                           <el-table-column label="姓名" align="center" width="210"><template slot-scope="scope"><div class="user-info-box"><span class="user-pic"><img :src="scope.row.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></span>{{scope.row.name}}</div></template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="职称" align="center"><template slot-scope="scope">{{scope.row.technical_title}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="导师类型" align="center"><template slot-scope="scope">{{scope.row.teacherType}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="任教学院" align="center"><template slot-scope="scope">{{scope.row.org_name}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="联系电话" align="center"><template slot-scope="scope">{{scope.row.mobile}}</template>\n' +
    '                            </el-table-column>\n' +
    '                            <el-table-column label="操作" width="64" align="center" v-if="operateBtnShow">\n' +
    '                                <template slot-scope="scope">\n' +
    '                                        <el-button :disabled="disabled" icon="el-icon-delete" size="mini"\n' +
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
        }
    },
    mixins: [Vue.collegesMixin],
    data: function () {
        return {
            colleges: [],
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
            collegesTreeOriginName: '',
            studentMax: -1,
            teacherMax: -1,
            userDuties: [
                {value: '0', label: '负责人'},
                {value: '1', label: '组成员'}
            ]
        }
    },
    computed: {

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
            var teamId = this.teamId;
            return this.teacherList.map(function (item) {
                return {
                    userType: item.user_type || item.userType || item.utype,
                    teamId: teamId,
                    userId: item.userId,
                }
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
                    self.studentList = stuList.map(function (item) {
                        item['userzz'] = item.userId === self.declareId ? '0' : '1';
                        return item;
                    });
                    self.teacherList = data.teaList || [];
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
            this.collegesTreeOriginName = this.getCollegesTreeOriginName(this.collegesTree[0].id);
            this.collegesTree[0].name = this.collegesTreeOriginName;
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
            this.collegesTree[0].name = '校园导师';
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
        getCollegesTreeOriginName: function (id) {
            var name;
            if (!this.collegesTreeOriginName) {
                var collegesTranscript = this.collegesTranscript;
                for (var i = 0; i < collegesTranscript.length; i++) {
                    if (collegesTranscript[i].id === id) {
                        name = collegesTranscript[i].name;
                        break;
                    }
                }
                return name;
            }
            return this.collegesTreeOriginName;
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
            teamMemberXhr = this.$axios({
                method: 'GET',
                url: '/sys/user/ajaxUserListTree',
                params: this.getUserListParams()
            });
            teamMemberXhr.then(function (response) {
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
                self.$message({
                    type: 'error',
                    message: error.response ? error.response.data : error
                })
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

        getColleges: function () {
            var self = this;
            this.$axios.get('/sys/office/getOrganizationList').then(function (response) {
                var data = response.data;
                self.colleges = data;
                if (self.colleges && self.colleges.length > 1) {
                    self.setCollegeEntries(self.colleges);
                    self.collegesTranscript = self.getCollegesTranscript();
                    // //若果是去掉学院，需要重新重新获取rootIds, 此时需要设置isDefaultCollegeRootId 未false
                    var rootIds = !self.isDefaultCollegeRootId ? self.setCollegeRooIds(self.colleges) : ['1'];
                    self.collegesTree = self.getCollegesTree(['1'], self.collegesProps);
                }
            })
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
        getSaveTeam: function () {
            var savedTeamUrl = this.savedTeamUrl;
            var self = this;
            this.$axios.get(savedTeamUrl).then(function (response) {
                var data = response.data || {};
                if (data.status === 1) {
                    data = data.data;
                    if(data){
                        var stuList = data.stus || [];
                        self.studentList = stuList.map(function (item) {
                            item['userzz'] = item.userId === self.declareId ? '0' : '1';
                            return item;
                        });
                        self.teacherList = data.teas || [];
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
        if(this.addable){
            this.getColleges();
            this.getTeacherTypes();
            this.getCurrentSate();
            this.getCurJoinProjects();
        }
    }
});