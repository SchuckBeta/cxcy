<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>场地管理</title>
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
        .primary-color{
            color: #e9432d;
        }
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>场地管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <div class="form-top-search">
        <div class="search-form-wrap form-inline">
            <a href="javascript:void (0);" class="btn btn-back-oe btn-primaryBack-oe btn-search">创建场地</a>
        </div>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>编号</th>
            <th>名称</th>
            <th>座位数</th>
            <th>楼层</th>
            <th>场地类型</th>
            <th>场地面积(平方米)</th>
            <th>状态</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item,index) in list">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>CD00{{index+1}}</td>
            <td>{{item.name}}</td>
            <td>{{item.seatNumber}}</td>
            <td>{{item.floorNumber}}</td>
            <td>{{item.siteTypeLabel}}</td>
            <td>{{item.siteArea}}</td>
            <td><span :class="{'primary-color': item.siteUseStatusId}">{{item.siteUseStatusLabel}}</span></td>
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
                    number: 'CD001',
                    name: '会议室001',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用'
                }, {
                    number: 'CD002',
                    name: '会议室001',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '空闲'
                }, {
                    number: 'CD003',
                    name: '商务洽谈区001',
                    seatNumber: '10',
                    floorNumber: '二楼',
                    siteTypeLabel: '会客室',
                    siteArea: '200',
                    siteUseStatusLabel: '空闲'
                }, {
                    number: 'CD004',
                    name: '商务洽谈区001',
                    seatNumber: '10',
                    floorNumber: '三楼',
                    siteTypeLabel: '会客室',
                    siteArea: '200',
                    siteUseStatusLabel: '已占用'
                }, {
                    number: 'CD005',
                    name: '创客空间',
                    seatNumber: '30',
                    floorNumber: '二楼',
                    siteTypeLabel: '众创空间',
                    siteArea: '500',
                    siteUseStatusLabel: '占用部分'
                }, {
                    number: 'CD006',
                    name: '创业区',
                    seatNumber: '30',
                    floorNumber: '三楼',
                    siteTypeLabel: '众创空间',
                    siteArea: '500',
                    siteUseStatusLabel: '占用部分'
                }, {
                    number: 'CD007',
                    name: '创新实验室001',
                    seatNumber: '30',
                    floorNumber: '二楼',
                    siteTypeLabel: '项目实验区',
                    siteArea: '500',
                    siteUseStatusLabel: '已满员',
                    siteUseStatusId: 1
                }, {
                    number: 'CD008',
                    name: '创新实验室002',
                    seatNumber: '30',
                    floorNumber: '二楼',
                    siteTypeLabel: '项目实验区',
                    siteArea: '500',
                    siteUseStatusLabel: '已满员',
                    siteUseStatusId: 1
                }, {
                    number: 'CD008',
                    name: '路演室',
                    seatNumber: '300',
                    floorNumber: '三楼',
                    siteTypeLabel: '路演大厅',
                    siteArea: '500',
                    siteUseStatusLabel: '空闲'
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