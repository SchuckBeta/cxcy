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
                <span>项目列表</span>
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
            <pro-view-gc :key="proModelId" v-if="viewLoading" :view-id="proModelId"></pro-view-gc>
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

           return {
               gnodeId: gnodeId,
               actywId: '${actYwId}',
               actionPath: actionPath,
               proCategories: [],
               proModel: {},
               proModelId: '${proModelId}',
               rendered: true,
               disabled: false,
               pageNo: 1,
               pageSize: 15,
               pageCount: 0,
               pageList: [],
               viewLoading: false,
               schoolTypeList: [],
               schoolList: []
           }
        },
        computed: {
            proCategoryEntries:  function () {
                return this.getEntries(this.proCategories)
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
                };
                return this.$axios.get('/cms/ajax/ajaxQueryMenuList', {params: params})
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

            getDictLists: function () {
                var dicts = {
                    'project_type': 'proCategories',
                    'technology_field': 'technologyFields'
                };
                this.getBatchDictList(dicts)
            },
        },
        created: function () {
            this.setPageList();
        },
        mounted: function () {
            this.getSchoolList();
            this.getSchoolTypeList();
            this.getDictLists();
        }
    })


</script>
</body>
</html>