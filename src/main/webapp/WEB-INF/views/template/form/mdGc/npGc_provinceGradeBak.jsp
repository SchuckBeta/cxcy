<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>

<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <edit-bar-province second-name="查看"></edit-bar-province>
    <div class="page-content-container">
        <div class="todo-audit-project-list">
            <div class="todo-au-title">
                <span>所有待评分项目</span>
            </div>
            <div class="todo-audit-project-wrapper">
                <e-scroller :update-data="updateData" top :rendered="rendered">
                    <todo-project-item
                            @change-project="handleChangeProject"
                            :item="item.proModel"
                            :class="{'router-link-active': proModelId === item.proModel.id}"
                            v-for="item in pageList"
                            :key="item.id"
                    ></todo-project-item>
                </e-scroller>
            </div>
        </div>
        <div class="project-info-view-page">
            <pro-view-gc :key="proModelId" v-if="viewLoading" :view-id="proModelId">
                <audit-dialog
                        ref="auditDialog"
                        :disabled="disabled"
                        :options="actYwStatusList"
                        @submit-form="submitGrade"
                ></audit-dialog>
            </pro-view-gc>
        </div>
    </div>
</div>
<script>

    'use strict';
    new Vue({
        el: '#app',
        data: function () {
            var gnodeId = '${gnodeId}';
            var actionPath = '${actionPath}';
            var proCategories = JSON.parse('${fns: toJson(fns:getDictList("project_type"))}');
            var sources = JSON.parse('${fns: toJson(fns:getDictList("project_source"))}');
            var projectResultType = JSON.parse('${fns: toJson(fns:getDictList("project_result_type"))}');
            var projectDegrees = JSON.parse('${fns: toJson(fns:getDictList(levelDict))}');
            return {
                gnodeId: gnodeId,
                actywId: '${actYwId}',
                actionPath: actionPath,
                sources: sources,
                proCategories: proCategories,
                projectResultType: projectResultType,
                projectDegrees: projectDegrees,
                proModel: {},
                proModelId: '${proModelId}',
                rendered: true,
                disabled: false,
                pageNo: 1,
                pageSize: 15,
                pageCount: 0,
                pageList: [],
                viewLoading: false,
                isFirst:'${isFirst}',
                actYwStatusList: []
            }
        },
        computed: {
            proCategoryEntries: {
                get: function () {
                    return this.getEntries(this.proCategories)
                }
            },
            sourceEntries: {
                get: function () {
                    return this.getEntries(this.sources)
                }
            },
            projectResultEntries: {
                get: function () {
                    return this.getEntries(this.projectResultType)
                }
            },
            projectLevelEntries: {
                get: function () {
                    return this.getEntries(this.projectDegrees)
                }
            }
        },
        methods: {
            updateData: function () {
                var self = this;
                self.rendered = false;
                if (!this.rendered) {
                    self.pageNo+=1;
                    self.pageSize *= self.pageNo;
                    this.getPageList().then(function (response) {
                        var data = response.data;
                        if (data.page) {
                            var page = data.page || {};
                            self.pageCount = page.count;
                            self.pageSize = page.pageSize;
                            self.pageList  = page.list || [];
                        }
                        self.loading = false;
                        self.rendered = true;
                    });
                }
            },
            getActYwStatusListByGnodeId: function () {
                var self = this;
                var pushBack = {
                    state:'打回',
                    status:'999'
                };
                this.$axios.get('/promodel/proModel/getActYwStatusListByGnodeId', {params: {gnodeId: this.gnodeId}}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.actYwStatusList = data.data || [];
                        if(self.isFirst == '1'){
                            self.actYwStatusList.push(pushBack);
                        }
                    }
                })
            },
            getPageList: function () {
                var params = {
                    pageNo: 1,
                    pageSize: this.pageSize,
                    actywId: this.actywId,
                    gnodeId: '${gnodeId}'
                };
                return this.$axios.get('/cms/ajax/listForm', {params: params})
            },

            setPageList: function () {
                var self = this;
                this.getPageList().then(function (response) {
                    var data = response.data;
                    if (data.page) {
                        var page = data.page || {};
                        self.pageCount = page.count;
                        self.pageNo = page.pageNo;
                        self.pageList = page.list || [];
                    }
                    self.viewLoading = true;
                })
            },

            handleChangeProject: function (item) {
                this.proModelId = item.id;
                this.proModel = item;
            },

            getActParams: function () {
                var act = this.proModel.act;
                var entry = {};
                for (var k in act) {
                    if (act.hasOwnProperty(k)) {
                        entry['act.' + k] = act[k];
                    }
                }
                return entry
            },

            submitGrade: function (form) {
                var self = this;
                var oParams = {
                    id: this.proModelId,
                    gnodeId: this.gnodeId,
                    actionPath: this.actionPath,
                };
                this.disabled = true;
                var params = Object.assign({}, this.getActParams(), oParams, form);
                this.$axios.post('/promodel/proModel/saveProvProModelGateAudit', Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                        }).catch(function () {

                        });
                        self.setPageList();
                        self.$refs.auditDialog.handleClose();
                    } else {
                        self.$alert(data.msg, '提示',{
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                })
            },
        },
        created: function () {
            this.setPageList();
            this.getActYwStatusListByGnodeId();
        }
    })


</script>
</body>
</html>