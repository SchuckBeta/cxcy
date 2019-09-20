<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>入驻审核</title>
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

        .status-alert {
            color: red;
        }
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterApply" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>入驻审核</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-top-search">
        <div class="search-form-wrap form-inline">
            <input type="text" class="input-medium" placeholder="项目名/团队名/企业名" v-modal="searchText">
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">查询</button>
        </div>
        <div class="condition-row form-horizontal">
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">院校</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-所有院校-</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">专业</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-专业-</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">审核状态</label>
                    <div class="controls">
                        <select class="input-medium" v-model="auditStatus">
                            <option value="">-请选择-</option>
                            <option v-for="item in auditStatusList" :value="item.id">{{item.label}}</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">入驻编号</label>
                    <div class="controls">
                        <input type="text" class="input-medium" v-model="number">
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">申请人</label>
                    <div class="controls" style="height: 34px;overflow: hidden;">
                        <input type="text" class="input-medium" v-model="name">
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
            <div class="condition-item condition-login-date">
                <div class="control-group">
                    <label class="control-label">申请日期</label>
                    <div class="controls">
                        <input type="text" class="input-medium Wdate" value="2017-11-13"
                               onClick="WdatePicker()"><span>至</span><input type="text" class="input-medium Wdate"
                                                                            value="2018-11-13" onClick="WdatePicker()">
                    </div>
                </div>
            </div>
        </div>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>入驻编号</th>
            <th>申请人</th>
            <th>性别</th>
            <th>入驻类型</th>
            <th>院校</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody v-if="list.length >0">
        <tr v-for="(item, index) in list" :key="item.number">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>{{item.number}}</td>
            <td>{{item.name}}</td>
            <td>{{item.sex}}</td>
            <td>{{item.entryTypeLabel}}</td>
            <td>{{item.schoolName}}</td>
            <td>
                <a href="#">{{item.auditStatusLabel}}</a>
            </td>
            <td>
                <a v-if="item.auditStatus == 1" href="${ctx}/html-rzshAction" class="btn btn-back-oe btn-primaryBack-oe btn-small">审核</a>
                <a v-if="item.auditStatus != 1" href="#" class="btn btn-back-oe btn-primaryBack-oe btn-small">查看</a>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<script>

    var enterApply = new Vue({
        el: '#enterApply',
        data: function () {
            return {
                list: [{
                    "number": 20171113001,
                    "name": "李思缔",
                    "sex": '男',
                    "entryType": 3,
                    "schoolName": "机械与电子学院",
                    "auditStatus": 1,
                    'auditStatusLabel': '待审核',
                    "entryTypeLabel": '入驻项目'
                }, {
                    "number": 20171113002,
                    "name": "刘瑞轩",
                    "sex": '男',
                    "entryType": 2,
                    "schoolName": "电气工程系",
                    "auditStatus": 0,
                    'auditStatusLabel': '不通过',
                    "entryTypeLabel": '入驻团队'
                }, {
                    "number": 20171113003,
                    "name": "万梦昕",
                    "sex": '女',
                    "entryType": 1,
                    "schoolName": "信息工程系",
                    "auditStatus": 3,
                    'auditStatusLabel': '通过',
                    "entryTypeLabel": '入驻企业'
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
                searchText: '',
                entryType: '',
                name: '',
                auditStatus: '',
                number: '',
                allChecked: false,
                checkedList: []
            }
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