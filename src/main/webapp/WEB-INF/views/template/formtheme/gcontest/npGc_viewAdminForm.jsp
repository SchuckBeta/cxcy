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
            <pro-view-contest :key="proModelId" v-if="viewLoading" :view-id="proModelId"></pro-view-contest>
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
                proModel: {},
                proModelId: '${proModelId}',
                gContestLevel: [],
                technologyFields: technologyFields,
                tracks: tracks,
                rendered: true,
                disabled: false,
                pageNo: 1,
                pageSize: 15,
                pageCount: 0,
                pageList: [],
                viewLoading: false,
                schoolList: [],
                schoolTypeList: []
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