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
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>入驻企业管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-top-search">
        <div class="condition-row form-horizontal">
        	<div class="condition-item search-form-wrap">
	            <input type="text" class="input-medium" placeholder="企业名称/企业简介/企业成员" v-modal="searchText">
	            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">查询</button>
	        </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">所属学院</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-请选择-</option>
                        </select>
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">企业法人</label>
                    <div class="controls">
                        <input type="text" class="input-medium">
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">所属专业</label>
                    <div class="controls">
                        <select class="input-medium">
                            <option value="">-请选择-</option>
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
                    <label class="control-label">企业编号</label>
                    <div class="controls">
                        <input type="text" class="input-medium">
                    </div>
                </div>
            </div>
            <div class="condition-item">
                <div class="control-group">
                    <label class="control-label">使用场地</label>
                    <div class="controls">
                        <input type="text" class="input-medium">
                    </div>
                </div>
            </div>
			<div class="condition-item condition-item-date">
                <div class="c-date-status">
                    <select class="input-medium">
                        <option>-请选择查询条件-</option>
                        <option>注册日期</option>
                        <option>入驻日期</option>
                        <option>入驻有效期</option>
                        <option>退出日期</option>
                    </select>
                </div>
                <div class="c-date-controls">
                    <input type="text" class="input-medium Wdate" value="2017-11-13"
                           onClick="WdatePicker()"><span>至</span><input type="text" class="input-medium Wdate"
                                                                        value="2018-11-13" onClick="WdatePicker()">
                </div>
            </div>
        </div>
        <div class="btn-search-box text-right">
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">取消入驻</button>
            <button class="btn btn-back-oe btn-primaryBack-oe btn-search" type="button">清除记录</button>
        </div>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>企业编号</th>
            <th>企业名称</th>
            <th>企业法人</th>
            <th>人数</th>
            <th>注册资金<br>(万元)</th>
            <th>使用场地</th>
            <th>注册时间</th>
            <th>入驻日期</th>
            <th>入驻有效期</th>
            <th>退出日期</th>
            <th>入驻状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody v-if="list.length >0">
        <tr v-for="(item,index) in list">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>{{item.id}}</td>
            <td>{{item.projectName}}</td>
            <td>{{item.name}}</td>
            <td>{{item.number}}</td>
            <td>{{item.money}}</td>
            <td>{{item.address}}</td>
            <td>{{item.enterRegDate}}</td>
            <td>{{item.enterStartDate}}</td>
            <td>{{item.enterValidDate}}</td>
            <td>{{item.enterQuiteDate}}</td>
            <td>{{item.enterTypeLabel}}</td>
            <td>
                <button type="button"
                        class="btn btn-back-oe btn-primaryBack-oe btn-small">查看详情
                </button>
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
                    "id": "20172015001",
                    "projectName": "粤宝利集团",
                    "name": "万梦昕",
                    "number": 50,
                    "money": "100",
                    "address": "创业孵化基地二楼创客空1001室",
                    "enterRegDate": "2017-11-10",
                    "enterStartDate": "2017-11-13",
                    "enterValidDate": "2020-11-13",
                    "enterQuiteDate": "",
                    "enterTypeLabel": "已入驻",
                    "enterStatusId": "1",
                    "cardTypeLabel": "未分配",
                    "entryType": 3,
                    "schoolName": "机械与电子学院",
                    "auditStatus": 1,
                    'auditStatusLabel': '待审核',
                    "entryTypeLabel": '入驻项目'
                }, {
                    "id": "20172015012",
                    "projectName": "万众金融责任有限公司",
                    "name": "刘宽",
                    "number": 100,
                    "money": "300",
                    "address": "创业孵化基地二楼创客空3001室",
                    "enterRegDate": "2017-11-11",
                    "enterStartDate": "2017-11-12",
                    "enterValidDate": "2019-11-12",
                    "enterQuiteDate": "",
                    "enterTypeLabel": "已入驻",
                    "enterStatusId": "1",
                    "cardTypeLabel": "未分配",
                    "entryType": 3,
                    "schoolName": "机械与电子学院",
                    "auditStatus": 1,
                    'auditStatusLabel': '待审核',
                    "entryTypeLabel": '入驻项目'
                }, {
                    "id": "20172015025",
                    "projectName": "粤垦集团",
                    "name": "张樊",
                    "number": 300,
                    "money": "900",
                    "address": "创业孵化基地二楼创客空1010室",
                    "enterRegDate": "2017-01-10",
                    "enterStartDate": "2017-01-10",
                    "enterValidDate": "2018-01-10",
                    "enterQuiteDate": "",
                    "enterTypeLabel": "即将到期",
                    "enterStatusId": "2",
                    "cardTypeLabel": "未分配",
                    "entryType": 3,
                    "schoolName": "机械与电子学院",
                    "auditStatus": 1,
                    'auditStatusLabel': '待审核',
                    "entryTypeLabel": '入驻项目'
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