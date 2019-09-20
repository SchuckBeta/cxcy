<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>基地预警设置</title>
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
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>基地预警设置</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <form>
        <div class="card-settings">
            <p class="cs-title">人员预警设置</p>
            <div class="cs-body">
                <ul class="cs-list">
                    <li><input type="checkbox" checked>连续<input type="text" value="7" class="input-mini">天未登录众创空间预警</li>
                    <li><input type="checkbox">距离账号剩余有效期<input type="text" class="input-mini">天预警</li>
                    <li><input type="checkbox" checked>从账号激活，截止到当前<input type="text" value="7" class="input-mini">天，活跃度低于<input type="text" value="5" class="input-mini">预警</li>
                </ul>
            </div>
        </div>
        <div class="card-settings">
            <p class="cs-title">项目预警设置</p>
            <div class="cs-body">
                <ul class="cs-list">
                    <li><input type="checkbox" checked>连续<input type="text" value="30" class="input-mini">天未更新项目预警</li>
                    <li><input type="checkbox">从账号激活，截止到当前<input type="text" class="input-mini">天，活跃度低于<input type="text" class="input-mini">预警</li>
                    <li><input type="checkbox">从账号激活，截止到当前<input type="text" class="input-mini">天，项目进度低于<input type="text" class="input-mini">%预警</li>
                    <li><input type="checkbox">从账号激活，截止到当前<input type="text" class="input-mini">天，项目更新次数少于<input type="text" class="input-mini">次预警</li>
                </ul>
            </div>
        </div>
        <div class="btn-card-settings">
            <button class="btn btn-back-oe btn-primaryBack-oe" type="button">保存</button>
            <button class="btn btn-back-oe btn-default" type="button">取消</button>
        </div>
    </form>
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
                    siteUseStatusLabel: '已占用',
                    siteIntro: '会议区，是多个创业团队共享的会议室',
                    siteNumber: 2
                }, {
                    number: 'CD002',
                    name: '会议室001',
                    seatNumber: '50',
                    floorNumber: '二楼',
                    siteTypeLabel: '会议室',
                    siteArea: '300',
                    siteUseStatusLabel: '已占用',
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