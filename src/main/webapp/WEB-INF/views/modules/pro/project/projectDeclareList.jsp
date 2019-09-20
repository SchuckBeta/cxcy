<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/page-innovation">双创项目</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/project/projectDeclare/curProject">我的项目</a></el-breadcrumb-item>
        <el-breadcrumb-item>项目列表</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="mgb-20 text-right">
        <el-button size="mini" @click.stop="goToCurPro">当前项目</el-button>
        <el-button type="primary" size="mini">项目列表</el-button>
    </div>
    <div class="table-container">
        <el-table :data="projectList" size="mini" class="table">
            <el-table-column label="项目信息">
                <template slot-scope="scope">
                    <p>{{scope.row.number}}</p>
                    <p><a href="javascript:void(0);" @click.stop.prevent="goToView(scope.row)">{{scope.row.project_name}}</a>
                    </p>
                    <p>{{scope.row.proTypeStr}}</p>
                </template>
            </el-table-column>
            <el-table-column label="成员信息">
                <template slot-scope="scope">
                    <p style="margin-bottom: 0">{{scope.row.leader}}</p>
                    <p v-show="scope.row.snames && scope.row.snames.split('/').length > 1">{{scope.row.snames}}</p>
                    <p v-show="scope.row.tnames">{{scope.row.tnames}}</p>
                </template>
            </el-table-column>
            <el-table-column prop="proTypeStr" label="项目类别" align="center">
                <template slot-scope="scope">
                    {{scope.row.type}}
                </template>
            </el-table-column>
            <el-table-column prop="apply_time" label="申报时间" align="center">
                <template slot-scope="scope">
                    {{scope.row.apply_time}}
                </template>
            </el-table-column>
            <el-table-column prop="result" label="项目结果" align="center">
                <template slot-scope="scope">
                    {{scope.row.result}}
                </template>
            </el-table-column>
            <el-table-column label="证书" align="center">
                <template slot-scope="scope">
                    <span style="display: none">1</span>
                    <span v-if="scope.row.scis" v-for="(item, index) in scope.row.scis">
                        <a href="javascript:void(0)" :title="item.name" class="look-type over-flow-tooltip project-office-tooltip" :sciid="item.id" @click.stop.prevent="lookImg(item.id)"
                           style="color:#53BEFF;">{{item.name}}</a>
                        <template v-if="index!== scope.row.scis.length -1"><br/></template>
                    </span>
                </template>
            </el-table-column>
            <el-table-column label="项目状态" align="center">
                <template slot-scope="scope">
                    <template v-if="scope.row.status_code === '-999'">
                        <%--<a href="javascript: void(0);" @click.stop.prevent="goToFlowView(scope.row)">{{scope.row.status}}</a>--%>
                        <a v-if="scope.row.proc_ins_id" :href="ctxFront + '/actyw/actYwGnode/designView?groupId=' + scope.row.groupId + '&proInsId=' + scope.row.proc_ins_id"
                           target="_blank">{{scope.row.status}}</a>
                        <a v-else href="javascript: void(0);">{{scope.row.status}}</a>
                    </template>
                    <template v-else>
                        <%--<a v-if="scope.row.subStatus === '1'" href="javascript: void(0);"--%>
                           <%--@click.stop.prevent="goToFlowView(scope.row)">--%>
                            <%--<template v-if="scope.row.state =='1'">项目已结项</template>--%>
                            <%--<template v-else>--%>
                                <%--<project-status :id="scope.row.id" v-if="scope.row.proc_ins_id"></project-status>--%>
                            <%--</template>--%>
                        <%--</a>--%>
                        <span v-if="scope.row.subStatus === '1'">
                            <a v-if="scope.row.proc_ins_id" target="_blank"
                               :href="ctxFront + '/actyw/actYwGnode/designView?groupId=' + scope.row.groupId + '&proInsId=' + scope.row.proc_ins_id">
                                <template v-if="scope.row.state =='1'">项目已结项</template>
                                <template v-else>
                                    <project-status :id="scope.row.id" v-if="scope.row.proc_ins_id"></project-status>
                                </template>
                            </a>
                            <a v-else href="javascript: void(0);">
                                <template v-if="scope.row.state =='1'">项目已结项</template>
                                <template v-else>
                                    <project-status :id="scope.row.id" v-if="scope.row.proc_ins_id"></project-status>
                                </template>
                            </a>
                        </span>
                    </template>
                </template>
            </el-table-column>
            <el-table-column v-if="isStudent" label="操作" align="center">
                <template slot-scope="scope">
                    <div v-if="scope.row.declareId === loginCurUser.id" class="table-btns-action">
                        <el-button  :disabled="scope.row.status_code != '0'"
                                   type="text" size="mini" @click.stop="goToProjectForm(scope.row)">继续完善
                        </el-button>
                        <br/>
                        <el-button :disabled="scope.row.status_code == '0'"
                                type="text" size="mini" @click.stop="confirmWithdraw(scope.row)">撤回
                        </el-button>
                        <br>
                        <el-button  :disabled="scope.row.status_code != '0'"
                                   type="text" size="mini" @click.stop="confirmDelProject(scope.row)">删除
                        </el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
        <div class="text-right mgb-20">
            <el-pagination
                    size="small"
                    @size-change="handlePSizeChange"
                    background
                    @current-change="handlePCPChange"
                    :current-page.sync="searchListForm.pageNo"
                    :page-sizes="[5,10,20]"
                    :page-size="searchListForm.pageSize"
                    layout="total,prev, pager, next, sizes"
                    :total="pageCount">
            </el-pagination>
        </div>
    </div>
    <el-dialog title="证书预览" :visible.sync="dialogCertVisible" width="60%">
        <el-carousel indicator-position="outside" ref="elCarousel" :height="bannerHeight + 'px'" :autoplay="false"
                     trigger="click">
            <el-carousel-item v-if="bannerImg[currentLookImgId]" v-for="item in bannerImg[currentLookImgId]" :key="item.id">
                <div class="preview-cert-content">
                    <img :src="item.imgUrl" alt="截图" v-auto-img="bannerScale">
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
    });
    Vue.component('project-status', {
        template: '<span>待{{taskMap.taskName}}</span>',
        props: {
            id: String
        },
        data: function () {
            return {
                taskMap: {}
            }
        },
        methods: {
            getActByPromodelId: function () {
                var self = this;
                this.$axios.get('/project/getActByPromodelId?proModelId=' + this.id).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.taskMap = data.data || {}
                    }
                })
            }
        },
        created: function () {
            this.getActByPromodelId();
        }
    })

    new Vue({
        el: '#app',
        data: function () {
            return {
                projectList: [],
                searchListForm: {
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                dialogCertVisible: false,
                bannerHeight: 400,
                bannerImg: {},
                bannerScale: 1,
                currentLookImgId: ''
            }
        },
        methods: {
            lookImg: function (id) {
                var self = this;
                if(!this.bannerImg[id]){
                    this.bannerImg[id] = [];
                    this.$axios.get('/cert/getCertIns?certinsid='+id).then(function (response) {
                        var data = response.data;
                        if(data){
                            data.forEach(function (item) {
                                self.bannerImg[id].push({id: item.id, imgUrl: item.imgUrl})
                            })
                        }
                        self.dialogCertVisible = true;
                        self.imgCenter();
                    })
                }else {
                    self.dialogCertVisible = true;
                    this.imgCenter();
                }

                this.currentLookImgId = id;

            },
            imgCenter: function () {
                var self = this;
                var bannerWidth = 0;
                self.$nextTick(function () {
                    bannerWidth = self.$refs.elCarousel.$el.offsetWidth;
                    self.bannerHeight = bannerWidth * 0.55;
                    self.bannerScale = bannerWidth / self.bannerHeight;
                });
            },
            handlePSizeChange: function (value) {
                this.searchListForm.pageSize = value;
                this.getProjectList();
            },

            handlePCPChange: function () {
                this.getProjectList();
            },

            goToProjectForm: function (row) {
                var href = this.ctxFront+'/promodel/proModel/form?id=' + row.id;
                if (row.ftb == 0) {
                    href = this.ctxFront + '/project/projectDeclare/form?id=' + row.id;
                }
                location.href = href;
            },

            goToFlowView: function (row) {
                if (row.proc_ins_id) {
                    location.href = this.ctxFront + '/actyw/actYwGnode/designView?groupId=' + row.groupId + '&proInsId=' + row.proc_ins_id;
                }
            },

            goToView: function (row) {
                var href = this.ctxFront + '/promodel/proModel/viewForm?id=' + row.id;
                if (row.ftb == 0) {
                    href = this.ctxFront + '/project/projectDeclare/viewPro?id=' + row.id;
                }
                location.href = href;
            },

            confirmDelProject: function (row) {
                var self = this;
                this.$confirm("确认要删除该项目申报吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.delProject(row)
                }).catch(function () {

                })
            },

            delProject: function (row) {
                var self = this;
                var params = {
                    id: row.id,
                    ftb: row.ftb
                };
                this.$axios.get('/project/projectDeclare/delProject?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$message.success('删除成功');
                        self.getProjectList();
                    } else {
                        self.$message.error(data.msg)
                    }
                })
            },

            getProjectList: function () {
                var self = this;
                this.$axios.get('/project/getProjectDeclareList',{params: this.searchListForm}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        data = data.data;
                        self.projectList = data.list || [];
                        self.searchListForm.pageNo = data.pageNo || 1;
                        self.searchListForm.pageSize = data.pageSize || 10;
                        self.pageCount = data.count || 0;
                    } else {
                        self.$message.error(data.msg)
                    }
                })
            },
            goToCurPro: function () {
                location.href = this.ctxFront + '/project/projectDeclare/curProject'
            },

            confirmWithdraw: function (row) {
                var self = this;
                this.$confirm("确认要撤回项目申报吗？", '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                }).then(function () {
                    self.withdrawProject(row)
                }).catch(function () {

                })
            },

            withdrawProject: function (row) {
                var self = this;
                this.$axios.get('/promodel/proModel/revertAfterApplyed', {params: {id: row.id}}).then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        self.$message({
                            type: 'success',
                            message: data.msg
                        })
                        self.getProjectList();
                    }else {
                        self.$message({
                            type: 'error',
                            message: data.msg
                        })
                    }
                })
            }
        },
        created: function () {
            this.getProjectList();
        }
    })

</script>

</body>
</html>