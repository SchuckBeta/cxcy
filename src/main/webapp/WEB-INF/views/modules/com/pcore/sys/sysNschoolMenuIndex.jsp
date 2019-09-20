<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <meta name="decorator" content="backPath_header"/>
    <script src="${ctxStatic}/snap/snap.svg-min.js"></script>
    <script src="${ctxJs}/components/flowDesignComponents/school-confirm-flow.js?version=${fns: getVevison()}"></script>
    <style>
        .v-modal {
          opacity: .5;
        }
        .sys-menu-container .fixed-right-box {
            max-height: 500px;
            overflow-y: auto;
        }
        .sys-menu-container .fixed-right-box::-webkit-scrollbar {
            height: 5px;
            width: 5px;
        }
        .sys-menu-container .fixed-right-box::-webkit-scrollbar-track {
            background: white;
        }
        .sys-menu-container .fixed-right-box::-webkit-scrollbar-thumb {
            background: #DEDFE0;
            border-radius: 5px;
        }
    </style>
</head>
<body>

<c:if test="${msg!=null}">
    ${msg}
</c:if>
<div id="app" v-show="pageLoad" style="display: none" class="sys-menu-container container">
    <div class="row sys-menu-row">
        <c:forEach items="${fns:getAllMenuList()}" var="menu" varStatus="idxStatus">
            <c:if test="${menu.parent.id eq fns:getRoot().id && menu.isShow eq '1'}">
                <div class="span3">
                    <div class="menu-item <c:if test="${not fns:checkMenu(menu.id)}">menu-item-qx</c:if> menu ${not empty firstMenu && firstMenu ? ' active' : ''}">
                        <c:if test="${empty menu.href}">
                            <c:if test="${not fns: contains(menu.name, '大赛')}"> <span class="topic-unread"></span></c:if>
                            <a data-id="${menu.id}" href="${ctx}/sys/menu/treePlus?parentId=${menu.id}">
                                <img class="menu-pic" src="${fns:ftpImgUrl(menu.imgUrl)}" alt=""/>
                                <span class="name">${menu.name}</span>
                            </a>
                            <p class="desc">${menu.remarks}</p>
                        </c:if>
                        <c:if test="${not empty menu.href}">
                            <c:if test="${not fns: contains(menu.name, '大赛')}"> <span class="topic-unread"></span></c:if>
                            <a data-id="${menu.id}" href="${ctx}/sys/menu/treePlus?parentId=${menu.id}">
                                <img class="menu-pic" src="${fns:ftpImgUrl(menu.imgUrl)}" alt=""/>
                                <span class="name">${menu.name}</span>
                            </a>
                            <p class="desc">${menu.remarks}</p>
                        </c:if>
                        <div class="layer-jurisdiction">
                            <span class="text">请开通权限访问</span>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>


    <%--校平台后台首页确认弹框--%>

    <div class="fixed-right-box">
        <div class="fixed-act-tip" v-for="item in messageList" :key="item.id">
            <a href="javascript:void(0);" @click.stop.prevent="openStep(item)">{{item.provinceActywName}}项目确认</a>
        </div>
    </div>

    <el-dialog :title="'省级平台发布申报“'+curMsg.provinceActywName+'”项目'" :visible.sync="dialogConfirmVisible"
               width="850px" :close-on-click-modal="false" :show-close="false">
        <p class="tenant-tip">*请收到该项目发布通知的学校确认以下信息，确认无误后，点击【确定】按钮；若有任何不符的，请到相应的页面进行调整</p>
        <row-step-apply style="width:auto;">
            <step-item :is-complete="currentStep >= 1">第一步（项目申报周期确认）</step-item>
            <step-item :is-complete="currentStep >= 2">第二步（项目附件表单模板确认）</step-item>
            <step-item :is-complete="currentStep >= 3">第三步（项目审核流程确认）</step-item>
            <step-item :is-complete="currentStep >= 4">第四步（审核人员的确认）</step-item>
        </row-step-apply>
        <div class="mgb-10" style="font-size: 12px;">
            <div v-show="currentStep == 1">如需调整，请进入<span class="red">基础数据管理系统 → 双创管理 → {{actYwGroup.flowType === '1' ? '双创大赛管理' : '双创项目管理'}} → 修改时间页面</span>进行修改</div>
            <div v-show="currentStep == 3">如需调整，请进入<span class="red">基础数据管理系统 → 流程管理 → 自定义流程 → 设计页面</span>进行修改</div>
        </div>

        <div class="school-confirm-box">

            <div class="step-content" v-loading="loading">
                <el-table v-show="currentStep == 1" size="mini" :data="firstStepList" class="table" :max-height="244"
                          border style="border-radius: 4px;">
                    <el-table-column prop="gnodeName" label="项目周期内容" align="center">
                    </el-table-column>
                    <el-table-column label="周期" align="center">
                        <template slot-scope="scope">
                            {{scope.row.beginDate | formatDateFilter('YYYY-MM-DD')}} 至 {{scope.row.endDate | formatDateFilter('YYYY-MM-DD')}}
                        </template>
                    </el-table-column>
                </el-table>

                <e-col-item v-show="currentStep == 2" label="项目申报表单模板：">
                    <el-table size="mini" :data="secondStepData.projectApplyList" class="table" :max-height="224" border
                              style="border-radius: 4px;">
                        <el-table-column label="表单名称" align="center">
                            <template slot-scope="scope">
                                项目申报表单
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="表单模板" align="center">
                        </el-table-column>
                    </el-table>
                </e-col-item>
                <e-col-item v-show="currentStep == 2" label="学生提交表单模板：">
                    <el-table size="mini" :data="secondStepData.studentSubmitList" class="table" :max-height="224"
                              border style="border-radius: 4px;">
                        <el-table-column label="表单名称" align="center">
                            <template slot-scope="scope">
                                学生提交表单
                            </template>
                        </el-table-column>
                        <el-table-column prop="name" label="表单模板" align="center">
                        </el-table-column>
                    </el-table>
                </e-col-item>

                <div v-show="currentStep == 3">
                    <school-confirm-flow v-if="currentStep == 3" :act-yw-group="actYwGroup"></school-confirm-flow>
                </div>
                <div v-show="currentStep == 4">
                    <%--<p class="tenant-tip">*请检查相应的审核节点中是否有对应的人员，若没有，请直接点击【进入页面修改】按钮，自行创建</p>--%>
                    <el-table v-show="currentStep == 4" size="mini" :data="fourthStepList" class="table" :max-height="224"
                              border style="border-radius: 4px;">
                        <el-table-column prop="name" label="审核项" align="center">
                        </el-table-column>
                        <%--<el-table-column prop="groleNames" label="审核角色" align="center">--%>
                        <%--</el-table-column>--%>
                    </el-table>
                </div>


            </div>

            <div class="text-right mgt-20" style="padding: 0 20px;">
                <el-button size="mini" v-show="currentStep != 1" @click.stop.prevent="preStep" :disabled="loading">上一条</el-button>
                <el-button size="mini" type="primary" v-show="currentStep != 4" @click.stop.prevent="nextStep" :disabled="loading">下一条
                </el-button>
            </div>

        </div>

        <div slot="footer" class="dialog-footer text-center">
            <el-button size="mini" @click="handleClose">稍后确认</el-button>
            <el-button size="mini" type="primary" @click.stop.prevent="saveDialog" :disabled="loading || disabled">确认无误</el-button>
        </div>
    </el-dialog>


</div>

<%@ include file="/WEB-INF/views/layouts/website/copyright.jsp" %>
<script>
    'use strict';

    var tenantDialog = new Vue({
        el: '#app',
        data: function () {
            return {
                //校平台后台首页确认弹框
                currentStep: 1,
                dialogConfirmVisible: false,
                messageList: [],
                firstStepList: [],
                fourthStepList: [],
                curMsg: {},
                actYwGroup: {},
                sysList: [],
                loading: false,
                disabled: false
            }
        },
        computed: {
            secondStepData: function () {
                var arr = this.sysList, obj = {};
                obj.projectApplyList = arr.filter(function (item) {
                    return item.type == 'S18';
                });
                obj.studentSubmitList = arr.filter(function (item) {
                    return item.type == 'S19';
                });
                return obj;
            }
        },
        methods: {
            //校平台后台首页确认弹框
            nextStep: function () {
                this.currentStep += 1;
            },
            preStep: function () {
                this.currentStep -= 1;
            },
            handleClose: function () {
                this.dialogConfirmVisible = false;
                this.curMsg = {};
                this.actYwGroup = {};
                this.currentStep = 1;
            },
            saveDialog: function () {
                var self = this;
                this.dialogConfirmVisible = false;
                this.disabled = true;
                this.$axios.post('/actyw/actYw/updateProvPush', {
                    id: self.curMsg.id,
                    provinceActywId: self.curMsg.provinceActywId,
                    schoolTenantId: self.curMsg.schoolTenantId,
                    step: '4'
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == 1) {
                        self.handleClose();
                        self.getMessageList();
                        var h = self.$createElement;
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            showCancelButton: false,
                            showClose: false,
                            message: h('p', null, [
                                        h('span', null, '项目已创建成功，请进入'),
                                        h('span', { style: 'color: red' }, '基础数据管理系统 → 系统数据管理 → 编号管理页面'),
                                        h('span', null, '【添加项目编号规则】')
                                      ])
                        }).then(function () {

                        });
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                });
            },
            openStep: function (item) {
                this.dialogConfirmVisible = true;
                this.curMsg = item;
                this.getStep();
            },
            getStep: function () {
                var self = this;
                this.loading = true;
                this.$axios.get('/actyw/actYw/getStep', {params: {id: self.curMsg.id}}).then(function (response) {
                    var data = response.data;
                    if(data.status == 1) {
                        //给每步赋值
                        self.firstStepList = data.data.actYwGtimeList || [];
                        self.sysList = data.data.sysList || [];
                        data.data.actYwGroup.uiJson = JSON.parse(data.data.actYwGroup.uiJson);
                        self.actYwGroup = data.data.actYwGroup || {};
                        self.fourthStepList = data.data.gnodeList || [];
                    }
                    self.loading = false;
                }).catch(function () {
                    self.loading = false;
                });
            },
            getMessageList: function () {
                var self = this;
                this.$axios.post('/actyw/actYw/stepList').then(function (response) {
                    var data = response.data;
                    if(data.status == 1) {
                        self.messageList = data.data || [];
                    }
                });
            }
        },
        created: function () {
            this.getMessageList();
        }

    });

    $('.sys-menu-container .menu-item').each(function (i, item) {
        var $item = $(item);
        getUnDealThing($item, $item.find('a').attr('data-id'))
    })

    function getUnDealThing(element, id) {
        $.ajax({
            type: 'GET',
            url: '${ctx}/sys/menu/ajaxMenuCount',
            data: {id: id},
            success: function (data) {
                if (data.status) {
                    var total = data.datas;
                    element.find('.topic-unread').css('display', 'block').text(total > 999 ? (total + '+') : total)
                }
            }
        })
    }
</script>

</body>
</html>
