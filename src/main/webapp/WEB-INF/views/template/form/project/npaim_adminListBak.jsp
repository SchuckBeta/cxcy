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
            <ec-condition-item label="地区" :value="searchListForm.areaCode | getSelectedVal(areaEntity, {label:'name', value: 'id'})">
                <ec-radio-group class="ec-radio-group_ec_condition" v-model="searchListForm.areaCode">
                    <ec-radio class="ec-radio_all" label="">全部</ec-radio>
                    <div class="ec-radio-list-content">
                        <ec-radio v-for="item in areas" :label="item.id" :key="item.id">{{item.name}}</ec-radio>
                    </div>
                </ec-radio-group>
            </ec-condition-item>
            <ec-condition-item label="高校类型" :value="searchListForm.schoolType | getSelectedVal(schoolTypeEntity, {label:'name', value: 'key'})">
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
            <el-input
                    class="w300"
                    size="mini"
                    maxlength="64"
                    placeholder="请输入关键字"
                    v-model="searchListForm.queryStr">
                <el-button size="mini" slot="append" icon="el-icon-search"></el-button>
            </el-input>
        </div>
        <div class="table-container">
            <el-table
                    :data="pageList"
                    @selection-change="handleSelectionChange"
                    size="mini"
                    class="mgb-20"
                    ref="multipleTable">
                <el-table-column type="selection" width="50" align="center"></el-table-column>
                <el-table-column label="项目信息" width="380">
                    <template slot-scope="scope">
                        <project-info-column
                                :key="scope.row.id"
                                :row="scope.row.proModel"
                                :to="ctx + '/promodel/proModel/viewForm?id='+ scope.row.id">
                        </project-info-column>
                    </template>
                </el-table-column>
                <el-table-column label="团队成员" width="260">
                    <template slot-scope="scope">
                        <project-team-column :row="scope.row.proModel" :key="scope.row.id"></project-team-column>
                    </template>
                </el-table-column>
                <el-table-column
                        prop="proCategory"
                        label="项目类别"
                        align="center"
                        sortable="custom"
                        >
                    <template slot-scope="scope">
                        {{scope.row.proModel.proCategory | getSelectedVal(proCategoryEntity)}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="createDate"
                        sortable="custom"
                        align="center"
                        label="申请日期">
                    <template slot-scope="scope">{{ scope.row.createDate | formatDate }}</template>
                </el-table-column>
                <%--<el-table-column align="center" label="项目级别" width="90">--%>
                    <%--<template slot-scope="scope">{{ scope.row.level }}</template>--%>
                <%--</el-table-column>--%>
                <el-table-column align="center" label="状态">
                    <template slot-scope="scope">
                        <a :href="ctx +'/actyw/actYwGnode/designView?groupId='+scope.row.proModel.actYw.groupId+'&proInsId='+scope.row.proModel.procInsId" target="_blank">
                            <span v-if="scope.row.proModel.state == '1'">项目已结项</span><span v-else>待{{scope.row.proModel.auditMap.taskName}}</span>
                        </a>
                    </template>
                </el-table-column>
                <el-table-column align="center" label="操作">
                    <template slot-scope="scope">
                        <el-button size="mini" type="primary" @click.stop="goToAudit(scope.row)">评分</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="text-right mgb-20" v-if="pageCount">
                <el-pagination
                        size="small"
                        @size-change="handlePaginationSizeChange"
                        background
                        @current-change="handlePaginationPageChange"
                        :current-page.sync="searchListForm.pageNo"
                        :page-sizes="[5,10,20,50,100]"
                        :page-size="searchListForm.pageSize"
                        layout="total, prev, pager, next, sizes"
                        :total="pageCount">
                </el-pagination>
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
                    actywId:'${actywId}',
                    gnodeId: '${gnodeId}',
                    schoolType: '',
                    areaCode: "",
                    queryStr: '',
                    schoolName:'',
                    proCategory: '',
                    pageNo: 1,
                    pageSize: 10
                },
                pageCount: 0,
                pageList: [],
                areas: [],
                schoolTypeList: [],
                proCategory: [],
                multipleSelection: []
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
            goToAudit: function (row) {
                location.href = this.ctx+'/act/task/auditform?gnodeId='+row.proModel.auditMap.gnodeId+'&proModelId='+row.proModel.auditMap.proModelId+'&pathUrl=${actionUrl}&taskName='+encodeURI(row.proModel.auditMap.taskName);
            },

            getPage: function () {
                var self = this;
                this.$axios({
                    method: 'POST',
                    url: '/cms/ajax/listForm',
                    params: this.searchListForm
                }).then(function (data) {
                    var page = {"page": {
                        "pageNo": 1,
                        "pageSize": 10,
                        "count": 2,
                        "todoCount": 0,
                        "list": [{
                            "id": "1fc938ff2e4c48119a7ebfd9d85e4df0",
                            "isNewRecord": false,
                            "createDate": "2019-04-23 10:37:24",
                            "updateDate": "2019-04-23 10:37:52",
                            "modelId": "5b8ec0d0e57044749961cebcb5baeacb",
                            "proModel": {
                                "id": "5b8ec0d0e57044749961cebcb5baeacb",
                                "isNewRecord": false,
                                "createDate": "2019-04-23 10:37:23",
                                "updateDate": "2019-04-23 10:38:03",
                                "actYwId": "7e9dd5ec40e54d1692c68c1a3d0136f4",
                                "pName": "双创项目双创项目双创项目双创项目双创项目",
                                "declareId": "2d14e9f88d924cbba7147dff743f5332",
                                "deuser": {
                                    "id": "2d14e9f88d924cbba7147dff743f5332",
                                    "isNewRecord": false,
                                    "remarks": "",
                                    "updateDate": "2019-03-08 16:32:15",
                                    "officeId": "049168153f064a8896aeeaa97c10d651",
                                    "loginName": "srl",
                                    "no": "20111703101",
                                    "name": "苏子",
                                    "email": "88888888@qq.com",
                                    "mobile": "13054044009",
                                    "userType": "1",
                                    "loginIp": "127.0.0.1",
                                    "loginDate": "2019-04-17 09:46:47",
                                    "loginFlag": "1",
                                    "oldLoginIp": "127.0.0.1",
                                    "oldLoginDate": "2019-04-17 09:46:47",
                                    "degree": "1",
                                    "education": "2",
                                    "sex": "1",
                                    "birthday": 936115200000,
                                    "professional": "5e3bff085a0d4dfab174df37e2a6c82a",
                                    "subject": {
                                        "id": "5e3bff085a0d4dfab174df37e2a6c82a",
                                        "isNewRecord": false,
                                        "name": "其他",
                                        "sort": 30,
                                        "type": "2",
                                        "parentId": "0"
                                    },
                                    "idType": "1",
                                    "national": "少数民族",
                                    "political": "中共党员",
                                    "idNumber": "313664199401212088",
                                    "qq": "88888888",
                                    "age": "20",
                                    "admin": false,
                                    "officeName": "其他",
                                    "roleNames": "",
                                    "companyId": "1",
                                    "sysAdmin": false
                                },
                                "type": "2",
                                "proCategory": "1",
                                "introduction": "双创项目",
                                "teamId": "741ea25f8ad74db0a15e1a3a10be8a7a",
                                "team": {
                                    "isNewRecord": true,
                                    "name": "aaa",
                                    "enterpriseTeacherNum": 0,
                                    "schoolTeacherNum": 0,
                                    "memberNum": 3,
                                    "userCount": 0,
                                    "enterpriseNum": 0,
                                    "schoolNum": 0,
                                    "uName": "何鑫成,郝北成,王小何,李晓鸿,李铁民",
                                    "entName": "李国斌,海洋",
                                    "isSelf": false,
                                    "number": "20190311205545100025",
                                    "checkJoin": false,
                                    "checkJoinTUR": false,
                                    "checkIsHus": true
                                },
                                "procInsId": "ba13dce917e149aca26e18093747b0ae",
                                "competitionNumber": "DC20180423001",
                                "subTime": 1555987082000,
                                "actYw": {
                                    "id": "7e9dd5ec40e54d1692c68c1a3d0136f4",
                                    "isNewRecord": false,
                                    "relId": "1666a226556e4d65810f2b51d3a3426b",
                                    "groupId": "30e4148fd9894142ba8cd2ff64334823",
                                    "flowId": "F_SNPT181100044_SNFW181000104:1:0a4969a9d8c34811ad2d14b32906e91f",
                                    "deploymentId": "99b26322bfd54a3e85eae9f882cc1cad",
                                    "isDeploy": true,
                                    "showTime": "1",
                                    "keyType": "hsxm_"
                                },
                                "auditMap": {
                                    "proModelId": "5b8ec0d0e57044749961cebcb5baeacb",
                                    "gnodeId": "56597fbfdbd142e297432065d65b813c",
                                    "taskName": "学院立项审核"
                                },
                                "year": "2018",
                                "pname": "双创项目双创项目双创项目双创项目双创项目",
                                "officeName": "其他",
                                "iid": "5b8ec0d0e57044749961cebcb5baeacb",
                                "vars": {
                                    "proCategory": "1",
                                    "finalStatus": "",
                                    "level": "",
                                    "projectSource": "",
                                    "type": "2",
                                    "number": "DC20180423001",
                                    "actYwId": "7e9dd5ec40e54d1692c68c1a3d0136f4",
                                    "financingStat": "",
                                    "teamId": "741ea25f8ad74db0a15e1a3a10be8a7a",
                                    "declareId": "2d14e9f88d924cbba7147dff743f5332",
                                    "dofficeName": "其他",
                                    "name": "双创项目双创项目双创项目双创项目双创项目",
                                    "id": "5b8ec0d0e57044749961cebcb5baeacb"
                                },
                                "iaurl": {
                                    "base": "/cmss",
                                    "start": "/cmss/start",
                                    "welcome": "/auditstandard/index",
                                    "view": "/cmss/vform",
                                    "lurl": "/cmss/lform/",
                                    "lajax": "/cmss/lform/ajaxList",
                                    "ldel": "/cmss/lform/ajaxDelpl",
                                    "aurl": "/cmss/aform",
                                    "asave": "/cmss/asave",
                                    "qurl": "/cmss/qform",
                                    "qajax": "/cmss/qform/ajaxList",
                                    "gzurl": "/cmss/act/task/processMap"
                                }
                            },
                            "source": "1",
                            "resultType": "5,4",
                            "innovation": "双创项目",
                            "resultContent": "双创项目",
                            "budget": "双创项目",
                            "planStartDate": 1556035200000,
                            "planEndDate": 1556726399000,
                            "planContent": "双创项目",
                            "planStep": "双创项目",
                            "officeName": "其他"
                        }, {
                            "id": "8e49e954d72e4dfabcc9a38444adad67",
                            "isNewRecord": false,
                            "createDate": "2019-04-16 14:40:46",
                            "updateDate": "2019-04-16 14:41:13",
                            "modelId": "82c0f79ae6384974b959fec9fd91c8d2",
                            "proModel": {
                                "id": "82c0f79ae6384974b959fec9fd91c8d2",
                                "isNewRecord": false,
                                "createDate": "2019-04-16 14:40:46",
                                "updateDate": "2019-04-16 14:43:12",
                                "actYwId": "7e9dd5ec40e54d1692c68c1a3d0136f4",
                                "pName": "测试文件上传取消",
                                "declareId": "2d14e9f88d924cbba7147dff743f5332",
                                "deuser": {
                                    "id": "2d14e9f88d924cbba7147dff743f5332",
                                    "isNewRecord": false,
                                    "remarks": "",
                                    "updateDate": "2019-03-08 16:32:15",
                                    "officeId": "049168153f064a8896aeeaa97c10d651",
                                    "loginName": "srl",
                                    "no": "20111703101",
                                    "name": "苏子",
                                    "email": "88888888@qq.com",
                                    "mobile": "13054044009",
                                    "userType": "1",
                                    "loginIp": "127.0.0.1",
                                    "loginDate": "2019-04-17 09:46:47",
                                    "loginFlag": "1",
                                    "oldLoginIp": "127.0.0.1",
                                    "oldLoginDate": "2019-04-17 09:46:47",
                                    "degree": "1",
                                    "education": "2",
                                    "sex": "1",
                                    "birthday": 936115200000,
                                    "professional": "5e3bff085a0d4dfab174df37e2a6c82a",
                                    "subject": {
                                        "id": "5e3bff085a0d4dfab174df37e2a6c82a",
                                        "isNewRecord": false,
                                        "name": "其他",
                                        "sort": 30,
                                        "type": "2",
                                        "parentId": "0"
                                    },
                                    "idType": "1",
                                    "national": "少数民族",
                                    "political": "中共党员",
                                    "idNumber": "313664199401212088",
                                    "qq": "88888888",
                                    "age": "20",
                                    "admin": false,
                                    "officeName": "其他",
                                    "roleNames": "",
                                    "companyId": "1",
                                    "sysAdmin": false
                                },
                                "type": "2",
                                "proCategory": "1",
                                "introduction": "测试文件上传取消",
                                "teamId": "a83c7163b8ea472090cef0196dfa07e8",
                                "team": {
                                    "isNewRecord": true,
                                    "name": "充分色温的服务充分色温的服务充分色温的服务充分色温的服务",
                                    "enterpriseTeacherNum": 0,
                                    "schoolTeacherNum": 0,
                                    "memberNum": 1,
                                    "userCount": 0,
                                    "enterpriseNum": 0,
                                    "schoolNum": 0,
                                    "isSelf": false,
                                    "number": "20190308174139100008",
                                    "checkJoin": false,
                                    "checkJoinTUR": false,
                                    "checkIsHus": true
                                },
                                "procInsId": "dc46809ad43041ec82e4343747d82b77",
                                "competitionNumber": "DC20180416001",
                                "subTime": 1555396991000,
                                "actYw": {
                                    "id": "7e9dd5ec40e54d1692c68c1a3d0136f4",
                                    "isNewRecord": false,
                                    "relId": "1666a226556e4d65810f2b51d3a3426b",
                                    "groupId": "30e4148fd9894142ba8cd2ff64334823",
                                    "flowId": "F_SNPT181100044_SNFW181000104:1:0a4969a9d8c34811ad2d14b32906e91f",
                                    "deploymentId": "99b26322bfd54a3e85eae9f882cc1cad",
                                    "isDeploy": true,
                                    "showTime": "1",
                                    "keyType": "hsxm_"
                                },
                                "auditMap": {
                                    "proModelId": "82c0f79ae6384974b959fec9fd91c8d2",
                                    "gnodeId": "56597fbfdbd142e297432065d65b813c",
                                    "taskName": "学院立项审核"
                                },
                                "year": "2018",
                                "pname": "测试文件上传取消",
                                "officeName": "其他",
                                "iid": "82c0f79ae6384974b959fec9fd91c8d2",
                                "vars": {
                                    "proCategory": "1",
                                    "finalStatus": "",
                                    "level": "",
                                    "projectSource": "",
                                    "type": "2",
                                    "number": "DC20180416001",
                                    "actYwId": "7e9dd5ec40e54d1692c68c1a3d0136f4",
                                    "financingStat": "",
                                    "teamId": "a83c7163b8ea472090cef0196dfa07e8",
                                    "declareId": "2d14e9f88d924cbba7147dff743f5332",
                                    "dofficeName": "其他",
                                    "name": "测试文件上传取消",
                                    "id": "82c0f79ae6384974b959fec9fd91c8d2"
                                },
                                "iaurl": {
                                    "base": "/cmss",
                                    "start": "/cmss/start",
                                    "welcome": "/auditstandard/index",
                                    "view": "/cmss/vform",
                                    "lurl": "/cmss/lform/",
                                    "lajax": "/cmss/lform/ajaxList",
                                    "ldel": "/cmss/lform/ajaxDelpl",
                                    "aurl": "/cmss/aform",
                                    "asave": "/cmss/asave",
                                    "qurl": "/cmss/qform",
                                    "qajax": "/cmss/qform/ajaxList",
                                    "gzurl": "/cmss/act/task/processMap"
                                }
                            },
                            "source": "1",
                            "resultType": "1,2,3,4",
                            "innovation": "测试文件上传取消",
                            "resultContent": "测试文件上传取消",
                            "budget": "测试文件上传取消",
                            "planStartDate": 1555344000000,
                            "planEndDate": 1558281600000,
                            "planContent": "测试文件上传取消",
                            "planStep": "1",
                            "officeName": "其他"
                        }],
                        "courseFooter": "<div class=\"row pagination_num\"><ul class=\"pagination_list\"><li class=\"disabled head_footer\"><a href=\"javascript:\">« 上一页</a></li><li class=\"current\"><a href=\"javascript:\">1</a></li>\n<li class=\"disabled head_footer\"><a href=\"javascript:\">下一页 »</a></li>\n</ul></div>",
                        "html": "<ul>\n<li class=\"disabled\"><a href=\"javascript:\">« 上一页</a></li>\n<li class=\"active\"><a href=\"javascript:\">1</a></li>\n<li class=\"disabled\"><a href=\"javascript:\">下一页 »</a></li>\n<li class=\"disabled controls\"><a href=\"javascript:\">当前 <input type=\"text\" value=\"1\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if (c==13)page(this.value,10,'');\" onclick=\"this.select();\"/> / <input type=\"text\" value=\"10\" onkeypress=\"var e=window.event||event;var c=e.keyCode||e.which;if (c==13)page(1,this.value,'');\" onclick=\"this.select();\"/> 页，共 2 条</a></li>\n</ul>\n<div style=\"clear:both;\"></div>",
                        "footer": "<div class=\"row pagination_num\"><ul class=\"pagination_list\"><li class=\"disabled head_footer\"><a href=\"javascript:\">« 上一页</a></li><li class=\"current\"><a href=\"javascript:\">1</a></li>\n<li class=\"disabled head_footer\"><a href=\"javascript:\">下一页 »</a></li>\n</ul><div class=\"num_page\"><p class=\"page_tips\">当前第1页  总记录2条  </p><select id=\"ps\"  class=\"per_page_count\"  onchange=\"page(1,this.value)\" ><option value=\"5\">5</option><option value=\"10\">10</option><option value=\"20\">20</option><option value=\"50\">50</option><option value=\"100\">100</option></select></div></div>",
                        "firstResult": 0,
                        "maxResults": 10
                    },}
                    if(data.page){
//                        var page = data.page;
                        page = page.page;
                        self.pageCount = page.count;
                        self.searchListForm.pageSize = page.pageSize;
                        self.pageList = page.list || [];
                        console.log(self.pageList)
                    }else {
                        self.pageList = [];
                    }
                    self.loading = false;
                })
            }
        },
        created: function () {
            this.getPage();
            this.getSchoolCityList();
            this.getSchoolTypeList();
            this.getProCategory();
        }
    })


</script>
</body>
</html>