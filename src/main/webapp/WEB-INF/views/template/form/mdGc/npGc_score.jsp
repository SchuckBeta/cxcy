<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>

<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <edit-bar-province second-name="评分"></edit-bar-province>
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
                            v-for="item in pageListAble"
                            :key="item.id"
                    ></todo-project-item>
                </e-scroller>
            </div>
        </div>
        <div class="project-info-view-page">
            <pro-view-contest :key="proModelId" v-if="viewLoading" :view-id="proModelId">
                <grade-dialog
                        ref="gradeDialog"
                        :disabled="disabled"
                        @submit-form="submitGrade"
                ></grade-dialog>
            </pro-view-contest>
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
            var proCategories = JSON.parse('${fns: toJson(fns:getDictList("competition_net_type"))}');
            var technologyFields = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("technology_field"))})) || [];
            var tracks =  JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("20190508110518100063"))})) || [];
            return {
                gnodeId: gnodeId,
                actywId: '${actYwId}',
                actionPath: actionPath,
                proCategories: proCategories,
                gContestLevel: [],
                technologyFields: technologyFields,
                proModel: {},
                proModelId: '${proModelId}',
                rendered: true,
                disabled: false,
                pageNo: 1,
                pageSize: 15,
                pageCount: 0,
                pageList: [],
                viewLoading: false,
                tracks: tracks
            }
        },
        computed: {
            proCategoryEntries: function(){
                return this.getEntries(this.proCategories)
            },
            trackEntries: function () {
                return this.getEntries(this.tracks)
            },
            proLevelEntries:  function () {
                return this.getEntries(this.gContestLevel)
            },
            probelongsFieldsEntries: function () {
                return this.getEntries(this.technologyFields)
            },
            schoolTypeEntity: function () {
                return this.getEntries(this.schoolTypeList, {label: 'name', value: 'key'})
            },
            schoolEntity: function () {
                return this.getEntries(this.schoolList, {label: 'schoolName', value: 'id'})
            },
            pageListAble: function () {
                var list = this.pageList;
                return list.filter(function (item) {
                    return item.state != '1' && item.proModel.auditMap.status == 'todo'
                })
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

            getPageList: function () {
                var params = {
                    pageNo: 1,
                    pageSize: this.pageSize,
                    actywId: this.actywId,
                    gnodeId: '${gnodeId}'
                };
                return this.$axios.get('/cms/ajax/listForm', {params: params})
            },

            setPageList: function (isAudit) {
                var self = this;
                this.getPageList().then(function (response) {
                    var data = response.data;
                    if (data.page) {
                        var page = data.page || {};
                        self.pageCount = page.count;
                        self.pageNo = page.pageNo;
                        self.pageList = page.list || [];
                        if(isAudit === 'audited'){
                            if(self.pageListAble.length === 0){
                                history.go(-1);
                            }else {
                                self.handleChangeProject(self.pageListAble[0].proModel);
                            }
                            self.disabled = false;
                        }
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
                this.$axios.get('/promodel/proModel/saveProvProModelGateAudit', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            self.setPageList('audited');
                        }).catch(function () {

                        });
                        self.$refs.gradeDialog.handleClose();
                        window.parent.LeftSideBarMenuApp.updateTodoCount()
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                        self.disabled = false;
                    }
                })
            },
            getDictListLevels: function () {
                var self = this;
                this.tracks.forEach(function (item) {
                    self.$axios.get('/sys/dict/getDictList?type='+item.value).then(function (response) {
                        var data = response.data || [];
                        self.gContestLevel = self.gContestLevel.concat(data);
                    })
                })
            },
            getSchoolList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolList = data.data || []
                    }
                })
            },


            getSchoolTypeList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.schoolTypeList = JSON.parse(data.data) || []
                    }
                })
            },
        },
        created: function () {
            this.setPageList();
        },
        mounted: function () {
            this.getDictListLevels();
            this.getSchoolList();
            this.getSchoolTypeList();
        }
    })


</script>
</body>
</html>