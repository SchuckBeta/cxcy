<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>创业孵化基地</title>
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

        .hatch-title{
            line-height: 2;
            font-size: 18px;
            margin-bottom: 15px;
        }
        .tab-floors{
            overflow: hidden;
        }
        .tab-floors>a{
            display: block;
            float: left;
            padding: 4px 20px;
            color: #333;
            text-decoration: none;
            border: 1px solid #ddd;
        }
        .tab-floors>a.active{
            color: #fff;
            border-color: #e9432d;
            background-color: #e9432d;
        }
        .space-statues{
            text-align: right;
        }
        .space-statues>a{
            display: inline-block;
            padding: 4px 16px;
            color: #333;
            text-decoration: none;
        }
        .space-statues .space-statues-empty{
            border: 1px solid #ddd;
        }
        .space-statues .space-statues-apply{
            background-color: #ddd;
        }
        .space-statues .space-statues-promise{
            background-color: rgba(181, 203, 144, 1);
        }
        .space-statues>a.space-statues-using{
            color: #fff;
            border-color: #e9432d;
            background-color: #e9432d;
        }
        .space-item{
            position: relative;
            float: left;
        }

        .space-item .layer{
            display: none;
            position: absolute;
            left: 0;
            bottom: 0;
            padding: 20px;
            background-color: rgba(226,226,226,.8);
            z-index: 100;
        }
        .space-item:hover>.layer{
            display: block;
        }
        .space-child-item:hover .layer{
            display: block;
        }
    </style>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
</head>
<body>
<div id="enterPersonManage" class="container-fluid container-fluid-oe" style="margin-bottom: 60px;">
    <h3 class="hatch-title text-center">众创空间平面图</h3>
    <div class="tab-floors">
        <a class="active" href="javascript:void (0)">一层</a>
        <a href="javascript:void (0)">二层</a>
        <a href="javascript:void (0)">三层</a>
    </div>
    <div class="tab-floor-content">
        <div class="tab-floor-panel">
            <div class="space-statues">
                <a href="javascript:void (0);" class="space-statues-empty">空闲</a>
                <a href="javascript:void (0);"  class="space-statues-apply">待审核</a>
                <a href="javascript:void (0);"  class="space-statues-promise">已预约</a>
                <a href="javascript:void (0);"  class="space-statues-using">正在使用中</a>
            </div>
            <div class="clearfix" style="max-width: 1100px;margin: 0 auto">
                <div class="space-item" style="width: 12.18%">
                    <img class="img-responsive" src="${ctxImages}/fhjd/sy-2.png">
                    <span style="position: absolute;left: 50%; margin-left: -18px;z-index: 100;top: 20%;">洗手间</span>
                    <span style="position: absolute;left: 50%; margin-left: -18px;z-index: 100;top: 70%;">洗手间</span>
                </div>
                <div class="space-item" style="width: .25%;min-height: 1px;"></div>
                <div class="space-item clearfix" style="width: 42.545%">
                    <div class="space-child-item" style="width: 50%;position: relative;float: left">
                        <img class="img-responsive" src="${ctxImages}/fhjd/sy-3-1.png">
                        <span style="position: absolute;left: 50%; margin-left: -18px;z-index: 100;top: 12%;">会议室</span>
                        <div class="layer">
                            <p>面积：50平方米</p>
                            <p>简介：会议区，是多个创业团队共享的会议室，配置有会议桌椅、投影仪幕布等会议常用工具。对于会议室的使用，各个创业团队也许提前在大学生创新创业平台软件上进行预约申请以避免造成不必要的使用上的冲突。同时，每间会议室均装配有先进的智能家居办公系统，可对房间的灯光，窗帘，空调等设备进行远程监控。 </p>
                        </div>
                    </div>
                    <div class="space-child-item" style="width: 50%;position: relative;float: left">
                        <img class="img-responsive" src="${ctxImages}/fhjd/sy-3-2.png">
                        <span style="position: absolute;left: 50%; margin-left: -18px;z-index: 100;top: 12%;">休闲洽谈区</span>
                        <div class="layer">
                            <p>面积：50平方米</p>
                            <p>简介：休闲洽谈区由可以提供餐饮服务的吧台以及数套具有文艺气息的桌椅组成，休闲洽谈区具备以下几点主要功能：做为精益创业坊的接待空间。休闲洽谈区工作人员为其提供必要的咨询支持；商务业务洽谈。轻松融洽的风格布局，使得休闲洽谈区非常适合作为非正式的商务或其他业务进行沟通洽谈的地方，与此同时，也适合产品经理和技术人员进行头脑风暴。</p>
                        </div>
                    </div>
                </div>
                <div class="space-item" style="width: .5%; min-height: 1px;"></div>
                <div class="space-item" style="width: 44.36%">
                    <img class="img-responsive" src="${ctxImages}/fhjd/sy-4.png">
                    <span style="position: absolute;left: 50%; margin-left: -18px;z-index: 100;top: 8%;">众创空间</span>
                    <div class="layer">
                        <p>面积：500平方米</p>
                        <p>简介：创客空间是一个具备实验和工作室等功能的可以供创客们共享资源和知识来完成实现自己想法的空间,装配有数张工作台以及一排设备柜，在设备柜内摆放着供创客们实验创新用的创新硬件及工具，同时创客空间前端配置有讲台和电子白板，也可供做为创新创业相关课程的教室使用。</p>
                    </div>
                </div>
            </div>
            <div style="padding-top: 1%"></div>
            <div class="clearfix" style="max-width: 1100px;margin: 0 auto">
                <div class="space-item" style="width: 55.5%">
                    <img class="img-responsive" src="${ctxImages}/fhjd/sy-5.png">
                    <span style="position: absolute;left: 50%; margin-left: -60px;z-index: 100;top: 12%;">项目实验区</span>
                    <div class="layer">
                        <p>面积：500平方米</p>
                        <p>简介：创客空间是一个具备实验和工作室等功能的可以供创客们共享资源和知识来完成实现自己想法的空间,装配有数张工作台以及一排设备柜，在设备柜内摆放着供创客们实验创新用的创新硬件及工具，同时创客空间前端配置有讲台和电子白板，也可供做为创新创业相关课程的教室使用。</p>
                    </div>
                </div>
                <div class="space-item" style="width: .5%;min-height: 1px;"></div>
                <div class="space-item" style="width: 44.36%">
                    <img class="img-responsive" src="${ctxImages}/fhjd/sy-6.png">
                    <span style="position: absolute;left: 64%;z-index: 100;top: 20%;">路演大厅</span>
                    <div class="layer">
                        <p>面积：500平方米</p>
                        <p>简介：商业路演区由演讲台和听众区组成，演讲台配备有完整的灯光音响设备，听众区座椅采用可移动的折叠椅方案可随时移动，采用移动座椅的目的是使得该区域除了具备路演功能外，还可以承接其他如公共课程、招聘会、派对等中小型公共活动。</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
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
                    siteUseStatusLabel: '已占用'
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