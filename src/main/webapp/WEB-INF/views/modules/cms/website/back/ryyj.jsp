<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>人员预警</title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <style>
        button {
            width: auto;
            height: auto;
        }

        .table thead tr th {
            white-space: nowrap;
        }

        .btn-default {
            color: #333;
            background-color: #fff;
            border-color: #ccc
        }

        .btn-default.focus, .btn-default:focus {
            color: #333;
            background-color: #e6e6e6;
            border-color: #8c8c8c
        }

        .btn-default:hover {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad
        }

        .btn-default.active, .btn-default:active, .open > .dropdown-toggle.btn-default {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad
        }

        .c-date-status {
            float: left;
        }

        .form-top-search .c-date-controls {
            margin-left: 155px;
        }

        .form-top-search .c-date-status select.input-medium {
            width: 140px;
        }

        .form-top-search .condition-item-date {
            width: 500px;
            margin-bottom: 20px;
            overflow: hidden;
        }

        .form-top-search .c-date-controls span {
            margin: 0 8px;
        }

        .form-top-search .c-date-controls .input-medium {
            width: 143px;
        }

        .btn-search-box {
            margin-bottom: 20px;
        }

        .table thead th {
            vertical-align: middle;
        }

        .form-top-search .condition-login-date {
            width: 440px;
        }

        .form-top-search .condition-login-date span {
            margin: 0 8px;
        }

        .table .team-name {
            display: inline-block;
            max-width: 270px;
        }

        .table .site-intro {
            display: inline-block;
            max-width: 300px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            vertical-align: middle;
        }

        .card-settings {
            position: relative;
            padding: 40px 60px;
            max-width: 700px;
            margin-bottom: 30px;
            margin-left: 112px;
            border: 1px solid #eee;
        }

        .card-settings .cs-list {
            list-style: none;
            margin: 0;
            padding: 0;
        }

        .card-settings .cs-list > li {
            line-height: 30px;
            margin-bottom: 20px;
        }

        .card-settings .cs-list > li input {
            margin: 0 5px;
            vertical-align: middle;
        }

        .card-settings .cs-title {
            position: absolute;
            left: 20px;
            top: -10px;
            font-weight: bold;
            color: #000;
            padding: 0 10px;
            background-color: #fff;
        }

        .btn-card-settings {
            padding-left: 112px;
        }

        .form-box {
            margin-top: -20px;
            padding-top: 20px;
            padding-bottom: 20px;
            border: solid #ddd;
            border-width: 0 1px;
            padding-right: 15px;
        }

        .form-box form {
            margin-bottom: 0;
        }
        .status-alert{
            color: red;
        }
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>人员预警</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-top-search">
        <div class="search-form-wrap form-inline">
            <input type="text" class="input-medium" placeholder="姓名/项目名称/团队名称" v-modal="searchText">
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">查询</button>
        </div>
        <div class="condition-row form-horizontal">
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">学号/卡号</label>
                    <div class="controls">
                        <input type="text" class="input-medium">
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">姓名</label>
                    <div class="controls">
                        <input type="text" class="input-medium">
                    </div>
                </div>
            </div>
            <div class="condition-item condition-login-date">
                <div class="control-group">
                    <label class="control-label">最近登录</label>
                    <div class="controls">
                        <input type="text" class="input-medium Wdate" value="2017-11-13"
                               onClick="WdatePicker()"><span>至</span><input type="text" class="input-medium Wdate"
                                                                            value="2018-11-13" onClick="WdatePicker()">
                    </div>
                </div>
            </div>
        </div>
        <div class="btn-search-box text-right">
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">批量注销</button>
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">批量删除</button>
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">取消预警</button>
        </div>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>卡号</th>
            <th>学号</th>
            <th>姓名</th>
            <th>所属项目</th>
            <th>所属团队</th>
            <th>最近一次登录时间</th>
            <th>连续未登<br>入天数</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody v-if="list.length >0">
        <tr v-for="(item,index) in list">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>{{item.cardNumber}}</td>
            <td>{{item.stuNumber}}</td>
            <td>{{item.name}}</td>
            <td><span class="team-name">{{item.proName}}</span></td>
            <td>{{item.teamName}}</td>
            <td>{{item.lastTime}}</td>
            <td><span :class="{'status-alert': item.unLoginDayCount < 10}">{{item.unLoginDayCount}}</span></td>
            <td><span :class="{'status-alert': item.warningStatusId != 2}">{{item.warningStatusLabel}}</span></td>
            <td>
                <button v-if="item.warningStatusId == 1" type="button" class="btn btn-back-oe btn-primaryBack-oe btn-small">注销</button>
                <button v-if="item.warningStatusId == 3" type="button" class="btn btn-back-oe btn-primaryBack-oe btn-small">续期</button>
                <button v-if="item.warningStatusId != 2" type="button" class="btn btn-back-oe btn-default btn-small">取消预警</button>
                <button v-if="item.warningStatusId == 2" type="button" class="btn btn-back-oe btn-default btn-small">删除</button>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<script>

    var enterApply = new Vue({
        el: '#enterPersonManage',
        data: function () {
            return {
                list: [{
                    cardNumber: '0001',
                    stuNumber: '1409111044',
                    name: '李思缔',
                    proName: '去有机质前后土壤高光谱分析/功能化荧光分子修饰的四苯乙烯衍生物构建及性能研究',
                    teamName: '光之翼',
                    lastTime: '2017.06.16  10:01:59',
                    unLoginDayCount: '7',
                    warningStatusLabel: '待注销',
                    warningStatusId: '1'
                }, {
                    cardNumber: '0002',
                    stuNumber: '1409111012',
                    name: '刘家胜',
                    proName: '去有机质前后土壤高光谱分析',
                    teamName: '',
                    lastTime: '2017.06.16  10:01:59',
                    unLoginDayCount: '7',
                    warningStatusLabel: '待注销',
                    warningStatusId: '1'
                }, {
                    cardNumber: '0005',
                    stuNumber: '1409111035',
                    name: '郭力',
                    proName: '去有机质前后土壤高光谱分析',
                    teamName: '',
                    lastTime: '2017.06.15  10:01:59',
                    unLoginDayCount: '8',
                    warningStatusLabel: '待注销',
                    warningStatusId: '1'
                }, {
                    cardNumber: '0015',
                    stuNumber: '1309131069',
                    name: '彭宇',
                    proName: '基于百度地图的武汉老地名查询系统的开发',
                    teamName: '',
                    lastTime: '2017.06.15  09:01:36',
                    unLoginDayCount: '0',
                    warningStatusLabel: '剩余7天有效期',
                    warningStatusId: '3'
                }, {
                    cardNumber: '0123',
                    stuNumber: '1409131020',
                    name: '谢世远',
                    proName: '基于百度地图的武汉老地名查询系统的开发',
                    teamName: '',
                    lastTime: '2017.06.15  09:01:36',
                    unLoginDayCount: '10',
                    warningStatusLabel: '已注销',
                    warningStatusId: '2'
                }, {
                    cardNumber: '0002',
                    stuNumber: '1409111025',
                    name: '刘家胜',
                    proName: '去有机质前后土壤高光谱分析',
                    teamName: '',
                    lastTime: '2017.06.15  09:01:36',
                    unLoginDayCount: '15',
                    warningStatusLabel: '已注销',
                    warningStatusId: '2'
                }, {
                    cardNumber: '0005',
                    stuNumber: '1309111126',
                    name: '郭力',
                    proName: '去有机质前后土壤高光谱分析',
                    teamName: '',
                    lastTime: '2017.06.15  09:01:36',
                    unLoginDayCount: '30',
                    warningStatusLabel: '已注销',
                    warningStatusId: '2'
                }],
                auditStatusList: [
                    {label: '待审核', id: 1},
                    {label: '已审核', id: 2},
                    {label: '通过', id: 3},
                    {label: '不通过', id: 0}
                ],
                entryTypes: [
                    {label: '入驻项目', id: 3},
                    {label: '入驻团队', id: 2},
                    {label: '入驻企业', id: 1}
                ],
                enterStatus: [
                    {label: '已入驻', id: 1},
                    {label: '即将到期', id: 2},
                    {label: '预警中', id: 3},
                    {label: '已到期', id: 4},
                    {label: '已退出', id: 5}
                ],
                cardStatus: [
                    {label: '未分配', id: 1},
                    {label: '已激活', id: 2},
                    {label: '已过期', id: 3},
                    {label: '已注销', id: 4}
                ],
                warningStatuses: [
                    {label: '待注销', id: 1},
                    {label: '已注销', id: 2},
                    {label: '剩余7天有效期', id: 3}
                ],
                searchText: '',
                entryType: '',
                name: '',
                auditStatus: '',
                number: '',
                allChecked: false,
                checkedList: []
            }
        },
        computed: {

//            allChecked: function () {
//                return this.checkedList.every(function (item) {
//                    return item.checked
//                })
//            }
        },
        watch: {
            allChecked: function (val) {
                this.checkedList.forEach(function (item) {
                    if (val) {
                        item.checked = true;
                    }
                })
            }
        },
        methods: {
            changeCheck: function (index) {
                this.allChecked = this.checkedList.every(function (item) {
                    return item.checked
                })
            },
            checkedEnd: function () {
                if (!this.allChecked) {
                    this.checkedList.forEach(function (item) {
                        item.checked = false;
                    })
                }
            },
            setCheckedList: function () {
                var list = this.checkedList;
                this.list.forEach(function (item) {
                    list.push({
                        checked: false
                    });
                });
            }
        },
        beforeMount: function () {
            this.setCheckedList()
            this.changeCheck()
        },
        mounted: function () {

        }
    })

</script>
</body>
</html>