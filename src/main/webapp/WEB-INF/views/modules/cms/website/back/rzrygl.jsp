<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
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
        .c-date-status{
            float: left;
        }

        .form-top-search .c-date-controls{
            margin-left: 155px;
        }
        .form-top-search .c-date-status select.input-medium{
            width: 140px;
        }

        .form-top-search .condition-item-date{
            width: 500px;
            margin-bottom: 20px;
            overflow: hidden;
        }
        .form-top-search .c-date-controls span{
            margin: 0 8px;
        }
        .form-top-search .c-date-controls .input-medium{
            width: 143px;
        }
        .btn-search-box{
            margin-bottom: 20px;
        }
        .table thead th{
            vertical-align: middle;
        }
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>入驻人员管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-top-search">
        <div class="search-form-wrap form-inline">
            <input type="text" class="input-medium" placeholder="姓名/团队名称/企业职务" v-modal="searchText">
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">查询</button>
        </div>
        <div class="condition-row form-horizontal">
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label" style="width: 72px;">入驻人状态</label>
                    <div class="controls" style="margin-left: 86px">
                        <select class="input-medium" v-model="auditStatus">
                            <option value="">-请选择-</option>
                            <option v-for="item in auditStatusList" :value="item.id">{{item.label}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">学院</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-请选择-</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">专业</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-请选择-</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">入驻类型</label>
                    <div class="controls">
                        <select class="input-medium" v-model="entryType">
                            <option value="">-入驻类型-</option>
                            <option :value="3">入驻项目</option>
                            <option :value="2">入驻团队</option>
                            <option :value="1">入驻企业</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">入驻状态</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-请选择-</option>
                            <option v-for="item in enterStatus" :value="item.id">{{item.label}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">卡状态</label>
                    <div class="controls" style="height: 34px;overflow: hidden;">
                        <select class="input-medium">
                            <option value="">-请选择-</option>
                            <option v-for="item in cardStatues" :value="item.id">{{item.label}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item condition-item-date">
                <div class="c-date-status">
                    <select class="input-medium">
                        <option>-请选择查询条件-</option>
                        <option>入驻日期</option>
                        <option>入驻有效期</option>
                        <option>入学年份</option>
                        <option>退出日期</option>
                    </select>
                </div>
                <div class="c-date-controls">
                    <input type="text" class="input-medium Wdate" value="2017-11-13" onClick="WdatePicker()"><span>至</span><input type="text" class="input-medium Wdate" value="2018-11-13" onClick="WdatePicker()">
                </div>
            </div>
        </div>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>卡号</th>
            <th>姓名</th>
            <th>团队名称</th>
            <th>团队职责</th>
            <th>入驻日期</th>
            <th>入驻有效期</th>
            <th>退出日期</th>
            <th>入驻状态</th>
            <th>卡状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody v-if="list.length >0">
        <tr v-for="(item,index) in list">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>{{item.number}}</td>
            <td>{{item.name}}</td>
            <td>{{item.teamName}}</td>
            <td>{{item.teamDuty}}</td>
            <td>{{item.teamDuty}}</td>
            <td>{{item.teamDuty}}</td>
            <td>{{item.teamDuty}}</td>
            <td>{{item.enterTypeLabel}}</td>
            <td>{{item.cardTypeLabel}}</td>
            <td>
                <button type="button" v-if="item.cardStatusId == 2" class="btn btn-back-oe btn-primaryBack-oe btn-small">注销卡号</button>
                <button type="button" v-if="item.cardStatusId == 1" class="btn btn-back-oe btn-primaryBack-oe btn-small">分配卡号</button>
                <button type="button" v-if="item.cardStatusId == 1" class="btn btn-back-oe btn-default btn-small">取消入驻</button>
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
                    "number": "",
                    "name": "汪书仪",
                    "teamName": "Yehops之光团队",
                    "teamDuty": "组成员",
                    "enterTypeLabel": "已入驻",
                    "cardTypeLabel": "未分配",
                    "entryType": 3,
                    "schoolName": "机械与电子学院",
                    "auditStatus": 1,
                    'auditStatusLabel': '待审核',
                    "entryTypeLabel": '入驻项目',
                    "cardStatusId": 1
                }, {
                    "number": "001",
                    "name": "万梦昕",
                    "entryType": 3,
                    "teamName": "Yehops之光团队",
                    "teamDuty": "团队负责人",
                    "enterTypeLabel": "已入驻",
                    "cardTypeLabel": "已分配",
                    "schoolName": "机械与电子学院",
                    "auditStatus": 1,
                    'auditStatusLabel': '待审核',
                    "entryTypeLabel": '入驻项目',
                    "cardStatusId": 2
                }, {
                    "number": "002",
                    "name": "常钧涵",
                    "teamName": "Yehops之光团队",
                    "teamDuty": "组成员",
                    "enterTypeLabel": "已入驻",
                    "cardTypeLabel": "已分配",
                    "schoolName": "电气工程系",
                    "entryType": 2,
                    "auditStatus": 0,
                    'auditStatusLabel': '不通过',
                    "entryTypeLabel": '入驻团队',
                    "cardStatusId": 2
                }, {
                    "number": "003",
                    "name": "杨楠楠",
                    "teamName": "Yehops之光团队",
                    "teamDuty": "组成员",
                    "enterTypeLabel": "已入驻",
                    "cardTypeLabel": "已分配",
                    "entryType": 2,
                    "schoolName": "信息工程系",
                    "auditStatus": 3,
                    'auditStatusLabel': '通过',
                    "entryTypeLabel": '入驻企业',
                    "cardStatusId": 2
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
                cardStatues: [
                    {label: '未分配', id: 1},
                    {label: '已激活', id: 2},
                    {label: '已过期', id: 3},
                    {label: '已注销', id: 4}
                ],
                searchText: '',
                entryType: '',
                name: '',
                auditStatus: '',
                number: '',
                allChecked: false,
                checkedList: [],
                enterStatus: [
                    {label: '已入驻', id: 1},
                    {label: '即将到期', id: 2},
                    {label: '预警中', id: 3},
                    {label: '已到期', id: 4},
                    {label: '已退出', id: 5}
                ],
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