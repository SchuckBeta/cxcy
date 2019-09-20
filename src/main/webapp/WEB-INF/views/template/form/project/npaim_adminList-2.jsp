<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <%--<%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>--%>
<body>

<div id="app" class="hide" :class="{show: pageLoad}">
    <edit-bar></edit-bar>



    <div class="content-container">
        <ec-condition v-model="collapsed">
            <ec-condition-item label="地区"
                               :value="searchListForm.areaCode | getSelectedVal(areaEntity, {label:'name', value: 'id'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.areaCode">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in areas" :label="item.id" :key="item.id">{{item.name}}</ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="高校类型"
                               :value="searchListForm.schoolType | getSelectedVal(schoolTypeEntity, {label:'name', value: 'key'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.schoolType">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in schoolTypeList" :label="item.key" :key="item.key">{{item.name}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="项目类型" :value="searchListForm.proCategory | getSelectedVal(proCategoryEntity)">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.proCategory">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in proCategory" :label="item.value" :key="item.value">{{item.label}}
                        </ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
        </ec-condition>
        <div class="search-bar-container">
            <%--<div class="result-count">--%>
            <%--共选中<span>{{ multipleSelection.length }}</span>条结果--%>
            <%--</div>--%>
            <div class="search-input_content">
                <el-date-picker
                        v-model="timeRange"
                        type="daterange"
                        class="w300"
                        size="mini"
                        value-format="yyyy-MM-dd HH:mm:ss"
                        :default-time="['00:00:00','23:59:59']"
                        unlink-panels
                        @change="getPage"
                        range-separator="至"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期">
                </el-date-picker>
                <el-input
                        class="w300"
                        size="mini"
                        placeholder="请输入关键字"
                        v-model="searchListForm.queryStr">
                    <el-button slot="append" icon="el-icon-search"></el-button>
                </el-input>
            </div>
            <div class="search-buttons_content">
                <el-dropdown size="mini">
                    <el-button size="mini" type="primary">
                        导出<i class="el-icon-arrow-down el-icon--right"></i>
                    </el-button>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item>省级项目</el-dropdown-item>
                        <el-dropdown-item>推荐国家级</el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
                <el-button size="mini" class="mgl-10"  @click.stop="confirmChangeProject">
                    一键转换“互联网+”
                </el-button>
                <el-button size="mini" type="primary" @click.stop="goToCertAssign">
                    下发证书
                </el-button>
                <publish-ex-pro-btn :fids="[{id:'1'}]"></publish-ex-pro-btn>
                <%--<el-button size="mini" type="primary">--%>
                    <%--发布项目--%>
                <%--</el-button>--%>
                <el-button size="mini" type="primary">
                    删除
                </el-button>
            </div>
        </div>
    </div>
</div>
<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                collapsed: false,
                searchListForm: {
                    actywId: '${actywId}',
                    gnodeId: '${gnodeId}',
                    schoolType: '',
                    areaCode: "",
                    queryStr: '',
                    schoolName: '',
                    proCategory: '',
                    pageNo: 1,
                    pageSize: 10
                },
                timeRange: [],
                pageCount: 0,
                pageList: [],
                areas: [],
                schoolTypeList: [],
                proCategory: [],
                multipleSelection: [],
                proModelHsxmForm: {
                    sysAttachmentList: []
                },
            }
        },
        computed: {
            areaEntity: function () {
                return this.getEntity(this.areas, {label: 'name', value: 'id'})
            },
            schoolTypeEntity: function () {
                return this.getEntity(this.schoolTypeList, {label: 'name', value: 'key'})
            },
            proCategoryEntity: function () {
                return this.getEntity(this.proCategory)
            }
        },
        methods: {
            handleChangeFiles: function () {

            },
            handleChangeTeam: function () {

            },
            goToCertAssign: function () {
                location.href = this.ctx + '/certMake/list?actywId=' + this.searchListForm.actywId;
            },

            getSchoolCityList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolCityList').then(function (data) {
                    if (data.status === 1) {
                        self.areas = data.data || []
                    }
                })
            },

            getSchoolTypeList: function () {
                var self = this;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (data) {
                    if (data.status === 1) {
                        self.schoolTypeList = JSON.parse(data.data) || []
                    }
                })
            },

            getProCategory: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=project_type').then(function (data) {
                    self.proCategory = data || [];
                })
            },

            handleSelectionChange: function (val) {
                this.multipleSelection = val;
            },

            handlePaginationSizeChange: function () {

            },

            handlePaginationPageChange: function () {

            },

            getPage: function () {
                console.log(this.timeRange)
            }
        },
        created: function () {
            this.getSchoolCityList();
            this.getSchoolTypeList();
            this.getProCategory();
        }
    })


</script>
</body>
</html>