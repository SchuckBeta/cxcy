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
    <el-form :model="searchListForm" size="mini" autocomplete="off" ref="searchListForm">

        <div class="conditions" style="position: relative;">
            <%--<e-condition type="radio" v-model="searchListForm['proModel.deuser.office.id']" label="学院"--%>
                         <%--:options="collegeList"--%>
                         <%--:default-props="{label: 'name', value: 'id'}" @change="searchCondition"--%>
                         <%--name="proModel.deuser.office.id"></e-condition>--%>
            <e-condition type="radio" v-model="searchListForm['proModel.proCategory']" label="大赛类别"
                         :options="proCategories"
                         name="proModel.proCategory" @change="searchCondition"></e-condition>
            <%--<e-condition type="checkbox" v-model="searchListForm['proModel.finalStatus']" label="项目级别"--%>
            <%--:options="adviceLevel"--%>
            <%--name="proModel.finalStatus" @change="searchCondition"></e-condition>--%>


        </div>
        <div class="search-block_bar clearfix">
            <div class="search-btns">
                <%--<button-advice-level--%>
                <%--v-if="isAdmin != null && isAdmin =='1'"--%>
                <%--:multiple-selection="projectListMultipleSelection"--%>
                <%--@advice-level-submit="adviceLevelSubmit" :advice-level="adviceLevel"></button-advice-level>--%>


                <el-button  type="primary"
                            :disabled="projectListMultipleSelection.length < 1" size="mini"
                            @click.stop.prevent="openScoreForm">
                    <i class="iconfont icon-tongguo"></i>批量通过
                </el-button>
                <button-import :is-first-menu="isFirstMenu"
                               :actyw-id="searchListForm.actywId" href="/impdata/promodellist?"
                               :gnode-id="searchListForm.gnodeId"></button-import>
                <button-export :spilt-prefs="spiltPrefs" is-hsxm
                               :spilt-posts="spiltPosts" label="项目"
                               :search-list-form="searchListForm"></button-export>

                <button-export-file :menu-name="menuName" label="项目" is-hsxm
                                    :search-list-form="searchListForm"
                                    @export-file-start="exportFileStart"></button-export-file>
            </div>
            <div class="search-input">
                <%--<el-date-picker--%>
                <%--v-model="applyDate"--%>
                <%--type="daterange"--%>
                <%--size="mini"--%>
                <%--align="right"--%>
                <%--@change="searchCondition"--%>
                <%--:default-time="defaultTime"--%>
                <%--unlink-panels--%>
                <%--range-separator="至"--%>
                <%--start-placeholder="开始日期"--%>
                <%--end-placeholder="结束日期"--%>
                <%--value-format="yyyy-MM-dd HH:mm:ss"--%>
                <%--style="width: 270px;">--%>
                <%--</el-date-picker>--%>
                <input type="text" style="display: none">
                <el-input
                        placeholder="大赛名称/编号/负责人"
                        size="mini"
                        name="proModel.queryStr"
                        v-model="searchListForm['proModel.queryStr']"
                        @keyup.enter.native="searchCondition"
                        style="width: 300px;">
                    <el-button slot="append" icon="el-icon-search"
                               @click.stop.prevent="searchCondition"></el-button>
                </el-input>
            </div>
        </div>
    </el-form>
    <div class="selected-list_len-bar">选择<span class="selected-num">{{listLen}}</span>条数据</div>

    <div class="table-container" style="margin-bottom:40px;">
        <el-table size="mini" :data="projectListFlatten" class="table" ref="multipleTable" v-loading="loading"
                  @sort-change="handleTableSortChange"
                  @selection-change="handleTableSelectionChange">
            <el-table-column
                    type="selection"
                    width="60">
            </el-table-column>
            <el-table-column label="大赛信息" align="left" prop="competition_number" width="240"
                             sortable="competition_number">
                <template slot-scope="scope">
                    <span>{{scope.row.competitionNumber}}</span>
                    <p class="over-flow-tooltip">
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.pName"
                                    placement="bottom">
                            <a :href="frontOrAdmin + '/promodel/proModel/viewForm?id=' + scope.row.id"
                               class="black-a project-name-tooltip">{{scope.row.pName}}</a>
                        </el-tooltip>
                    </p>
                    <p  v-show="!!scope.row.officeName">
                        <i class="iconfont icon-dibiao2"></i>
                        <el-tooltip class="item" effect="dark" popper-class="white" :content="scope.row.officeName"
                                    placement="bottom">
                            <span class="over-flow-tooltip project-office-tooltip">{{scope.row.officeName}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息" align="left" :min-width="isScore != null && isScore == '1'?155:170">
                <template slot-scope="scope">
                    <p>负责人：{{scope.row.deuser.name}}</p>
                    <p class="over-flow-tooltip">成员：
                        <el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.team.entName"
                                    placement="bottom"><span
                                class="team-member-tooltip">{{scope.row.team.entName}}</span></el-tooltip>
                    </p>
                    <p  v-show="!!scope.row.team.uName" class="over-flow-tooltip">导师：
                        <el-tooltip class="item" popper-class="white" effect="dark" :content="scope.row.team.uName"
                                    placement="bottom"><span class="team-member-tooltip">{{scope.row.team.uName}}</span>
                        </el-tooltip>
                    </p>
                </template>
            </el-table-column>
            <el-table-column label="项目类别" align="center" min-width="85">
                <template slot-scope="scope">
                    {{scope.row.proCategory | selectedFilter(proCategoryEntries, true)}}
                </template>
            </el-table-column>
            <el-table-column label="申报日期" align="center" prop="sub_time" min-width="110" sortable="sub_time">
                <template slot-scope="scope">
                    {{scope.row.subTime | formatDateFilter('YYYY-MM-DD')}}
                </template>
            </el-table-column>

            <el-table-column v-if="isScore != null && isScore == '1'" label="评分" align="center" prop="g_score"
                             min-width="85" sortable="g_score">
                <template slot-scope="scope">
                    {{scope.row.gScore}}
                </template>
            </el-table-column>


            <el-table-column label="${menuName}结果" align="center" prop="final_result" min-width="110"
                             sortable="final_result">
                <template slot-scope="scope">
                    {{scope.row.finalResult}}
                </template>
            </el-table-column>
            <el-table-column label="获奖情况" align="center" prop="type" min-width="110" sortable="final_result">
                <template slot-scope="scope">
                    <award-result v-if="scope.row.type" :type="scope.row.type" :result="scope.row.result"></award-result>
                    <%--{{scope.row.finalResult}}--%>
                </template>
            </el-table-column>
            <%--<el-table-column label="年份" align="center" prop="final_result" min-width="110"--%>
                             <%--sortable="final_result">--%>
                <%--<template slot-scope="scope">--%>
                    <%--{{scope.row.year}}--%>
                <%--</template>--%>
            <%--</el-table-column>--%>
            <el-table-column label="审核状态" align="center" min-width="110">
                <template slot-scope="scope">
                    <a class="black-a"
                       :href="frontOrAdmin +'/actyw/actYwGnode/designView?groupId='+scope.row.actYw.groupId+'&proInsId='+scope.row.procInsId"
                       target="_blank">
                        <span v-if="scope.row.state == '1'">项目已结项</span>
                        <span v-else>待{{scope.row.auditMap.taskName}}</span>
                    </a>
                </template>
            </el-table-column>
            <el-table-column label="操作" align="center" min-width="60">
                <template slot-scope="scope">
                    <el-button v-if="scope.row.state == '1'" type="text" size="small" disabled>审核</el-button>
                    <div v-else style="display: inline-block">
                        <a v-if="scope.row.auditMap.status == 'todo'"
                           href="javascript: void(0);" @click.stop.prevent="goToAudit(scope.row)">审核</a>
                        <el-button v-else type="text" size="small" disabled>审核</el-button>
                    </div>
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

    <el-dialog title="审核"
               width="520px"
               :visible.sync="dialogVisibleScore"
               :close-on-click-modal="false"
               :before-close="handleChangeScoreClose">
        <el-form :model="auditForm" ref="auditForm" :rules="auditFormRules" size="mini" :disabled="disabled"
                 label-width="120px" autocomplete="off">
            <el-form-item prop="level" label="推荐：">
                <el-select v-model="auditForm.level">
                    <el-option v-for="item in actYwStatusList" :key="item.status" :value="item.status"
                               :label="item.state"></el-option>
                </el-select>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button size="mini" :disabled="disabled" type="primary"
                       @click.stop="validateForm">确定
            </el-button>
        </div>
    </el-dialog>

    <export-file-process ref="exportFileProcess" :visible.sync="EFPVisible"
                         :menu-name="menuName" :gnode-id="searchListForm.gnodeId"></export-file-process>
</div>


<script>

    'use strict';

    Vue.component('award-result', {
        template: '<span>{{result | selectedFilter(dictListEntries)}}</span>',
        props: {
            type: String,
            result: String
        },
        data: function () {
            return {
                dictList: []
            }
        },
        computed: {
            dictListEntries: function () {
                return this.getEntries(this.dictList);
            }
        },
        watch: {
            type: function () {
                this.getDictList();
            }
        },
        methods: {
            getDictList: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=' + this.type).then(function (response) {
                    var data = response.data;
                    self.dictList = data || []
                })
            }
        },
        created: function () {
            this.getDictList();
        }
    })

    new Vue({
        el: '#app',
        data: function () {
            var proCategories = JSON.parse('${fns:toJson(fns: getDictList('competition_net_type'))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList('gcontest_level'))}');


            var isFirstMenu = '${fns:isFirstMenu(actywId,gnodeId)}';
            var spiltPrefs = JSON.parse('${fns:spiltPrefs()}');
            var spiltPosts = JSON.parse('${fns:spiltPosts()}');
            var actYwStatusList = JSON.parse(JSON.stringify(${fns: toJson(actYwStatusList)})) || [{
                        status: '998',
                        state: '通过'
                    }, {status: '999', state: '不通过'}];

            return {
                proCategories: proCategories,
                projectDegrees: projectDegrees,
                offices: [],
                applyDate: [],
                projectList: [],
                isFirstMenu: isFirstMenu === 'true',
                messageInfo: '${message}',
                menuName: '${menuName}',
                isScore: '${isScore}',
                isGate: '${isGate}',
                isAdmin: '${isAdmin}',
                spiltPrefs: spiltPrefs,
                spiltPosts: spiltPosts,
                batchCheckIsPass: [
                    {label: '通过', value: '${actYwStatus.status}'}
                ],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10,
                    'proModel.queryStr': '',
                    orderBy: '',
                    orderByType: '',
//                    'proModel.beginDate': '',
//                    'proModel.endDate': '',
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    'proModel.deuser.office.id': '',
                    'proModel.proCategory': ''
                },
                pageCount: 0,
                projectListMultipleSelection: [],
                loading: false,
                adviceLevel: projectDegrees,
                multipleSelectedId: [],
                controlEditings: {},
                schoolAutoShow: '${autoShow}',
                defaultTime: ['00:00:00', '23:59:59'],
                EFPVisible: true,
                actYwStatusList: actYwStatusList,
                auditForm: {
                    level: ''
                },
                auditFormRules: {
                    level: [{required: true, message: '请选择推荐情况', trigger: 'change'}]
                },
                disabled: false,
                dialogVisibleScore: false
            }
        },
        computed: {

            projectListFlatten: {
                get: function () {
                    var projectList = this.projectList;
                    var list = [];
                    if (!projectList || projectList.length < 1) return [];
                    for (var i = 0; i < projectList.length; i++) {
                        var proModel = Object.assign({}, projectList[i].proModel);
                        proModel['gCompetitionNumber'] = projectList[i].gCompetitionNumber;
                        proModel['pCompetitionNumber'] = projectList[i].pCompetitionNumber;
                        var id = proModel.id;
                        proModel = Object.assign(proModel, proModel.vars)
                        proModel = Object.assign(proModel, proModel.team)
                        proModel = Object.assign(proModel, proModel.deuser)
                        proModel.id = id;
                        proModel.type = projectList[i].type;
                        proModel.result = projectList[i].result;
                        if (proModel.impdata === '1') {
                            if (projectList[i].gmembers && projectList[i].gmembers.length > 0) {
                                proModel.team.entName = projectList[i].gmembers.reduce(function (name, item, index) {
                                    if (index === 0) {
                                        return name + item.name;
                                    }
                                    return name + ',' + item.name;
                                }, '');
                            }
                            if (projectList[i].gteachers && projectList[i].gteachers.length > 0) {
                                proModel.team.uName = projectList[i].gteachers.reduce(function (name, item, index) {
                                    if (index === 0) {
                                        return name + item.name;
                                    }
                                    return name + ',' + item.name;
                                }, '')
                            }

                        }
                        list.push(proModel)
                    }
                    return list;
                }
            },



            projectLevelEntries: {
                get: function () {
                    return this.getEntries(this.adviceLevel)
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
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
            }
        },
        watch: {
            applyDate: function (value) {
                value = value || [];
                this.searchListForm['proModel.beginDate'] = value[0];
                this.searchListForm['proModel.endDate'] = value[1];
                this.searchCondition();
            }
        },
        methods: {
            openScoreForm: function () {
                this.dialogVisibleScore = true;
            },

            handleChangeScoreClose: function () {
                this.dialogVisibleScore = false;
            },

            validateForm: function () {
                var self = this;
                this.$refs.auditForm.validate(function (valid) {
                    if (valid) {
                        self.batchPass();
                    }
                })
            },
            exportFileStart: function () {
                this.EFPVisible = true;
                //主动去调用
                this.$refs.exportFileProcess.getExpInfo();
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
                var self = this;
                this.loading = true;
                var listXhr = this.$axios({
                    method: 'POST',
                    url: '/cms/ajax/listForm' + "?" + Object.toURLSearchParams(this.searchListForm)
                });
                listXhr.then(function (response) {
                    var page = response.data.page;
                    self.pageCount = page.count;
                    self.searchListForm.pageSize = page.pageSize;
                    self.projectList = page.list || [];
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },

            adviceLevelSubmit: function (obj) {
                var adviceLevelRadioVal = obj.adviceLevelRadioVal;
                var self = this;
                var levelXhr = this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/ajax/batchAuditLevel?ids=' + self.multipleSelectedId.join(',') + '&finalStatus=' + adviceLevelRadioVal,
                    timeout: 1000 * 60 * 30,
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    }
                });
                this.loading = true;
                levelXhr.then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        self.searchCondition();
                    }
                    self.loading = false;
                }).catch(function () {
                    self.$alert('操作异常', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                    self.loading = false;
                })
            },

            batchPass: function () {
                var self = this;
                this.loading = true;
                this.$axios({
                    method: 'POST',
                    url: '/promodel/proModel/ajax/batchAudit?ids=' + self.multipleSelectedId.join(',') + '&grade=' + this.auditForm.level + '&gnodeId=' + '${gnodeId}',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
                    }
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
//                        window.parent.sideNavModule.changeUnreadTag(self.searchListForm.actywId);
//                        window.parent.sideNavModule.changeStaticUnreadTag("/a/promodel/proModel/getTaskAssignCountToDo?actYwId=" + self.searchListForm.actywId, 2000);
                        self.searchCondition();
                        self.$message({
                            message: '批量审核成功',
                            type: 'success'
                        });
                        self.handleChangeScoreClose();
                        self.changeSideTodoNum(self.searchListForm.actywId);
                    } else {
                        self.$message({
                            message: '审核失败',
                            type: 'error'
                        });
                    }
                    self.loading = false;
                }).catch(function () {
                    self.$alert('操作异常', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    });
                    self.loading = false;
                })
            },


            goToAudit: function (row) {
                location.href = this.frontOrAdmin + '/act/task/auditform?gnodeId=' + row.auditMap.gnodeId + '&proModelId=' + row.auditMap.proModelId + '&pathUrl=${actionUrl}&taskName=' + encodeURI(row.auditMap.taskName);
            }
        },
        created: function () {
            if (this.messageInfo) {
                this.$alert(this.messageInfo, '提示', {
                    confirmButtonText: '确定',
                    type: this.messageInfo.indexOf('成功') > -1 ? 'success' : 'fail'
                });
                this.messageInfo = '';
//                window.parent.sideNavModule.changeUnreadTag(this.searchListForm.actywId);
//                window.parent.sideNavModule.changeStaticUnreadTag("/a/promodel/proModel/getTaskAssignCountToDo?actYwId=" + this.searchListForm.actywId);
            }

            this.changeSideTodoNum(this.searchListForm.actywId);
            this.searchCondition();


        }
    })


</script>

</body>
</html>