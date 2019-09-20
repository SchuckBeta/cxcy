<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>资产管理</title>
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

        .card-settings{
            position: relative;
            padding: 40px 60px;
            max-width: 700px;
            margin-bottom: 30px;
            margin-left:  112px;
            border: 1px solid #eee;
        }
        .card-settings .cs-list{
            list-style: none;
            margin: 0;
            padding: 0;
        }
        .card-settings .cs-list>li{
            line-height: 30px;
            margin-bottom: 20px;
        }
        .card-settings .cs-list>li input{
            margin: 0 5px;
            vertical-align: middle;
        }
        .card-settings .cs-title{
            position: absolute;
            left: 20px;
            top: -10px;
            font-weight: bold;
            color: #000;
            padding: 0 10px;
            background-color: #fff;
        }
        .btn-card-settings{
            padding-left: 112px;
        }
        .form-box{
            margin-top: -20px;
            padding-top: 20px;
            padding-bottom: 20px;
            border: solid #ddd;
            border-width: 0 1px;
            padding-right: 15px;
        }
        .form-box form{
            margin-bottom: 0;
        }
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>资产管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <ul class="nav nav-tabs">
        <li class="active"><a href="javascript:void (0)">资产管理</a></li>
        <li><a href="${ctx}/html-zclxgl">资产类型管理</a></li>
    </ul>
    <div class="form-box">
        <form>
            <div class="text-right">
                <a href="javascript: void(0);" class="btn btn-back-oe btn-primaryBack-oe">创建资产</a>
                <button type="button" class="btn btn-back-oe btn-primaryBack-oe">删除资产</button>
            </div>
        </form>
    </div>
    <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
        <thead>
        <tr>
            <th><input type="checkbox" v-model="allChecked" @change="checkedEnd"></th>
            <th>资产编号范围</th>
            <th>名称</th>
            <th>类型</th>
            <th>数量</th>
            <th>所属场地</th>
            <th>所在楼层</th>
            <th>损坏编号</th>
            <th>操作</th>
        </tr>
        </thead>
        <tbody v-if="list.length >0">
        <tr v-for="(item,index) in list">
            <td><input type="checkbox" v-model="checkedList[index].checked"
                       @change="changeCheck(index)"></td>
            <td>{{item.assetNumber}}</td>
            <td>{{item.assetName}}</td>
            <td>{{item.assetTypeLabel}}</td>
            <td>{{item.assetCount}}</td>
            <td>{{item.assetAdr}}</td>
            <td>{{item.assetFloor}}</td>
            <td>{{item.assetDirtyNo}}</td>
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
                    assetNumber: 'PC001-PC030',
                    assetName: '台式电脑',
                    assetTypeLabel: '电脑',
                    assetCount: '30台',
                    assetAdr: '众创空间',
                    assetFloor: '二楼',
                    assetDirtyNo: '-',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    assetNumber: 'PC031-PC040',
                    assetName: '台式电脑',
                    assetTypeLabel: '电脑',
                    assetCount: '10台',
                    assetAdr: '创新实验室001',
                    assetFloor: '三楼',
                    assetDirtyNo: '-',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    assetNumber: 'PC041-PC050',
                    assetName: '台式电脑',
                    assetTypeLabel: '电脑',
                    assetCount: '10台',
                    assetAdr: '创新实验室002',
                    assetFloor: '三楼',
                    assetDirtyNo: '-',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    assetNumber: 'VDI001',
                    assetName: '一体机',
                    assetTypeLabel: '电脑',
                    assetCount: '1台',
                    assetAdr: '路演大厅',
                    assetFloor: '三楼',
                    assetDirtyNo: '-',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    assetNumber: 'VDI002',
                    assetName: '一体机',
                    assetTypeLabel: '电脑',
                    assetCount: '1台',
                    assetAdr: '会议室001',
                    assetFloor: '二楼',
                    assetDirtyNo: '-',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    assetNumber: 'VDI002',
                    assetName: '一体机',
                    assetTypeLabel: '电脑',
                    assetCount: '1台',
                    assetAdr: '会议室002',
                    assetFloor: '三楼',
                    assetDirtyNo: '-',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
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