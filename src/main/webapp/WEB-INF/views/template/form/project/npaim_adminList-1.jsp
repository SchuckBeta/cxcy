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
    <div class="page-content-container">


        <div class="todo-audit-project-list">
            <div class="todo-au-title">
                <span>项目列表</span>
            </div>
            <div class="todo-audit-project-wrapper">
                <e-scroller :update-data="updateData" top :rendered="rendered">
                    <todo-project-item
                            :to="ctx + '/promodel/proModel/viewForm?id='+ item.id"
                            :item="item.proModel"
                            :class="{'router-link-active': viewId === item.id}"
                            v-for="item in todoProjectListMany"
                            :key="item.id"
                    ></todo-project-item>
                </e-scroller>
            </div>
        </div>
        <div class="project-info-view-page">
            <div class="project-view-container">
                <pro-view-header :project="project" :pro-model="project.proModel">
                    <grade-dialog
                            ref="gradeDialog"
                            :disabled="disabled"
                    ></grade-dialog>
                    <audit-dialog
                            ref="auditDialog"
                            :disabled="disabled"
                            :options="[]"
                    ></audit-dialog>
                </pro-view-header>
                <div class="pro-vm-body">
                    <div class="pro-vm-border"></div>
                    <div class="pro-vmb-wrap">
                        <el-tabs v-model="activeName" size="mini">
                            <el-tab-pane label="项目信息" name="first">
                                <project-base-info-pane
                                        :project="project"
                                        :apply-files="applyFiles"
                                ></project-base-info-pane>
                            </el-tab-pane>
                            <el-tab-pane label="团队信息" name="second">
                                <pro-team-pane :project="project.proModel">
                                    <team-student-list slot="studentList" :team-student="studentList"
                                                       :leader="proModel.deuser || {}"></team-student-list>
                                    <team-teacher-list slot="teacherList"
                                                       :team-teacher="teacherList"></team-teacher-list>
                                </pro-team-pane>
                            </el-tab-pane>
                            <el-tab-pane label="审核记录" name="fourth">
                                <pro-audit-list-pane
                                        :audit-list="auditList"
                                ></pro-audit-list-pane>
                            </el-tab-pane>
                        </el-tabs>
                    </div>
                </div>
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
                activeName: 'first',
                viewId: '1fc938ff2e4c48119a7ebfd9d85e4df0',
                disabled: false,
                todoProjectList: [{
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
                count: 30,
                rendered: true,
                applyFiles: [{
                    "id": "b3734f3a642b49288a8482b37ee6e18b",
                    "isNewRecord": false,
                    "createDate": "2019-04-23 10:38:02",
                    "updateDate": "2019-04-23 10:38:02",
                    "type": "S11",
                    "fileStep": "S1102",
                    "uid": "5b8ec0d0e57044749961cebcb5baeacb",
                    "url": "/tool/oseasy/project/2019-04-23/dfcaa1996ef44b20ac0dbe07afd3c963.jpg",
                    "name": "timg (1).jpg",
                    "userName": "苏子",
                    "size": "67256",
                    "suffix": "jpg",
                    "imgType": "image",
                    "viewUrl": "http://192.168.0.130:82/oseasy/project/2019-04-23/dfcaa1996ef44b20ac0dbe07afd3c963.jpg",
                    "ftpUrl": "http://192.168.0.130:82/oseasy/project/2019-04-23/dfcaa1996ef44b20ac0dbe07afd3c963.jpg",
                    "tempUrl": "/tool/oseasy/project/2019-04-23/dfcaa1996ef44b20ac0dbe07afd3c963.jpg",
                    "fileName": "dfcaa1996ef44b20ac0dbe07afd3c963.jpg",
                    "remotePath": "/tool/oseasy/project/2019-04-23/"
                }],
                auditList:[{"id":"ecad5dddb5a94cc8a82dd755ed09f08c","isNewRecord":false,"createDate":"2019-04-24 10:03:29","updateDate":"2019-04-24 10:03:29","promodelId":"bc430d90e433416487220a40a6355bf7","auditId":"de073c956715434980ecfe166228b6e1","auditName":"学院立项审核","gnodeId":"56597fbfdbd142e297432065d65b813c","suggest":"111","score":0.0,"grade":"5","user":{"id":"de073c956715434980ecfe166228b6e1","isNewRecord":false,"name":"学院秘书","loginFlag":"1","age":"","admin":false,"sysAdmin":false,"roleNames":""},"result":"C级","complete":false},{"id":"6a3f0c645e7c4fb99975141631971a3f","isNewRecord":false,"createDate":"2019-04-24 11:12:52","updateDate":"2019-04-24 11:12:52","promodelId":"bc430d90e433416487220a40a6355bf7","auditId":"51cad4e2200f4b2aadb323919d0429ff","auditName":"学院中期评分","gnodeId":"b8d7807e93e14649bf00742becdb6c6b","suggest":"1000","score":10.0,"user":{"id":"51cad4e2200f4b2aadb323919d0429ff","isNewRecord":false,"name":"学院专家","loginFlag":"1","age":"","admin":false,"sysAdmin":false,"roleNames":""},"result":"10","gnodeVesion":"ae51e5ca49d646f0ade885e38e405936","complete":false},{"isNewRecord":true,"auditName":"学院中期评分","score":0.0,"result":"10.0","complete":false}],
                auditOptions: [],
            }
        },
        computed: {
            project: function () {
                return {
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
                    "planList": [{
                        "id": "901116a7550e4f79b08aaf54fab42b8e",
                        "isNewRecord": false,
                        "createDate": "2019-04-23 10:37:24",
                        "projectId": "5b8ec0d0e57044749961cebcb5baeacb",
                        "content": "双创项目",
                        "description": "双创项目",
                        "startDate": "2019-04-17",
                        "endDate": "2019-05-13",
                        "cost": "12",
                        "quality": "双创项目",
                        "sort": "0"
                    }],
                    "officeName": "其他"
                }
            },
            proModel: function () {
                return this.project.proModel || {};
            },
            studentList: function () {
                return [{
                    "no": "444",
                    "currState": "1",
                    "mobile": "13666666666",
                    "instudy": "硕士",
                    "office": "信息工程系",
                    "userId": "925c752394c04d9e94c19b069434a93b",
                    "professional": "计算机应用技术",
                    "curJoin": "大创项目,铜陵学院_苏瑞林,铜陵学院项目01",
                    "user_type": "1",
                    "domain": "智慧城市,VR/AR",
                    "name": "李国斌",
                    "id": "0b3327c2de304989aa5b6222247b2632",
                    "org_name": "信息工程系",
                    "email": "6515155@qq.com"
                }, {
                    "no": "565656",
                    "currState": "1",
                    "mobile": "13279363404",
                    "instudy": "学士",
                    "office": "广州铁路职业技术学院",
                    "userId": "95a89ce72fb4464bb6b480c6826c3142",
                    "professional": "教育技术中心",
                    "curJoin": "大创项目,铜陵学院_苏瑞林,铜陵学院项目01",
                    "user_type": "1",
                    "domain": "大数据,移动互联网",
                    "name": "海洋",
                    "id": "814a2be7460246789803d4a469df06ad",
                    "org_name": "广州铁路职业技术学院",
                    "email": "614561515@qq.com"
                }, {
                    "no": "20111703101",
                    "currState": "1",
                    "mobile": "13054044009",
                    "instudy": "博士",
                    "office": "其他",
                    "userId": "2d14e9f88d924cbba7147dff743f5332",
                    "professional": "其他",
                    "curJoin": "互联网+大赛,大创项目,学生科研项目,铜陵学院_苏瑞林,铜陵学院项目01",
                    "user_type": "1",
                    "domain": "大数据",
                    "name": "苏子",
                    "id": "94552e9848e240bd8b5ea0966b5ec035",
                    "org_name": "其他",
                    "email": "88888888@qq.com"
                }]
            },
            teacherList: function () {
                return [{
                    "no": "6201532",
                    "education": "2",
                    "mobile": "13825102536",
                    "photo": "/tool/oseasy/user/2017-10-11/f5183c8d839d441b98094cf9c200f1bd.png",
                    "userId": "8657db21b0074578a98b0e1efcf3a0ce",
                    "teacherType": "校内导师",
                    "curJoin": "大创项目,铜陵学院项目01",
                    "user_type": "2",
                    "technical_title": "教授",
                    "ttv": "1",
                    "name": "何鑫成",
                    "postTitle": "",
                    "org_name": "其他",
                    "email": "26021626@qq.com"
                }, {
                    "no": "157820",
                    "education": "3",
                    "mobile": "13542512012",
                    "photo": "/tool/oseasy/user/2017-10-11/cb5919b02ad0412b86fcbb67bfd75143.png",
                    "userId": "68a3e07613e64593b14764249cbd55bd",
                    "teacherType": "企业导师",
                    "curJoin": "大创项目,铜陵学院项目01",
                    "user_type": "2",
                    "technical_title": "高级工程师",
                    "ttv": "2",
                    "name": "李铁民",
                    "postTitle": "",
                    "org_name": "",
                    "email": "2260156@qq.com"
                }, {
                    "no": "268316",
                    "education": "3",
                    "mobile": "13878745879",
                    "photo": "/tool/oseasy/user/2017-10-11/21cfb2c0049c49f4ba127d08a60b1ac4.png",
                    "userId": "ee76c90aa72c44a68526a9adce2a6062",
                    "teacherType": "校内导师",
                    "curJoin": "互联网+大赛,大创项目,铜陵学院_苏瑞林,铜陵学院项目01",
                    "user_type": "2",
                    "technical_title": "副教授",
                    "ttv": "1",
                    "name": "郝北成",
                    "postTitle": "",
                    "org_name": "",
                    "email": "5784521@qq.com"
                }, {
                    "no": "200415821077",
                    "education": "3",
                    "mobile": "18109106655",
                    "photo": "",
                    "userId": "6730f318591b457587dcb3cd01ed2d79",
                    "teacherType": "企业导师",
                    "curJoin": "大创项目,铜陵学院项目01",
                    "user_type": "2",
                    "technical_title": "名誉教授",
                    "ttv": "2",
                    "name": "王小何",
                    "postTitle": "",
                    "org_name": "其他",
                    "email": "85921602@qq.com"
                }, {
                    "no": "200473411444",
                    "education": "2",
                    "mobile": "18142993932",
                    "userId": "4aaf9c90486e43cd99477c4ea1678161",
                    "teacherType": "企业导师",
                    "curJoin": "大创项目,学生科研项目,铜陵学院项目01",
                    "user_type": "2",
                    "technical_title": "",
                    "ttv": "2",
                    "name": "李晓鸿",
                    "postTitle": "",
                    "org_name": "其他",
                    "email": ""
                }]
            },
            todoProjectListMany: function () {
                var list = [];
                var project = this.todoProjectList[0];
                var i = 0;
                while (i < this.count) {
                    list.push(Object.assign(project, {id: project.id + i}));
                    i++
                }
                return list.concat(this.todoProjectList[0])
            }
        },
        methods: {
            updateData: function () {
                var self = this;
                self.rendered = false;
                if (!this.rendered) {
                    self.$axios.get('/sys/dict/getDictList?type=project_type').then(function (data) {
                        setTimeout(function () {
                            self.count += 10;
                            self.rendered = true;
                        }, 2000)
                    })
                }
            },
            
            getAuditOptions: function () {
                
            }
        }
    })


</script>
</body>
</html>