<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>场地类型管理</title>
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
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>场地类型管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-top-search">
        <div class="search-form-wrap form-inline">
            <a href="javascript:void (0);" class="btn btn-back-oe btn-primaryBack-oe btn-search">创建场地类型</a>
        </div>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>编号</th>
            <th>场地类型</th>
            <th>场地数量</th>
            <th>场地简介</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item,index) in list">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>{{item.number}}</td>
            <td>{{item.name}}</td>
            <td>{{item.siteNumber}}</td>
            <td><span class="site-intro">{{item.siteIntro}}</span></td>
            <td>
                <button type="button" class="btn btn-back-oe btn-primaryBack-oe btn-small">编辑</button>
                <button type="button" class="btn btn-back-oe btn-default btn-small">删除</button>
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
                    number: 'C001',
                    name: '会议室001',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    number: 'C002',
                    name: '会议室001',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用',
                    siteIntro: '',
                    siteNumber: 2
                }, {
                    number: 'C003',
                    name: '众创空间',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用',
                    siteIntro: '',
                    siteNumber: 2
                }, {
                    number: 'C004',
                    name: '项目实验区',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用',
                    siteIntro: '',
                    siteNumber: 2
                }, {
                    number: '0005',
                    name: '路演大厅',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用',
                    siteIntro: '',
                    siteNumber: 1
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