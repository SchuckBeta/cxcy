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
        .apply-container{
            padding: 0 15px;
            border: 1px solid #ddd;
        }
        .apply-title{
            font-size: 14px;
            line-height: 2;
            margin: 0 -15px;
            padding: 0 15px; ;
            position: relative;
            background-color: #f4e6d4;
        }
        .apply-label{
            display: inline-block;
            width: 100px;
            text-align: right;
        }
        .row-fluid-apply{
            max-width: 1100px;
            margin: 0 auto;
        }
        .apply-label i{
            font-style: normal;
            color: red;
            margin-right: 4px;
        }
        .apply-intro-item{
            /*margin-bottom: 15px;*/
        }
        .row-fluid-apply{
            padding-top: 15px;

        }
        .pdb15{
            padding-bottom: 15px;
        }
        .apply-panel{
            padding: 15px 0;
        }
        .control-static{
            margin-bottom: 0;
            line-height: 30px;
        }
        .control-label i{
            font-style: normal;
            color: red;
            margin-right: 4px;
        }
        .down-files{
            list-style: none;
            padding: 0;
            margin: 0;
        }
        .down-files>li{
            line-height: 30px;
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
    <div class="apply-container" style="margin-bottom: 60px;">
        <h4 class="apply-title"><span>申请人基本信息</span></h4>
        <div class="row-fluid row-fluid-apply">
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>姓名：</label>李思缔
                </div>
            </div>
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>性别：</label>男
                </div>
            </div>
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>政治面貌：</label>党员
                </div>
            </div>
        </div>
        <div class="row-fluid row-fluid-apply">
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>现状：</label>在校
                </div>
            </div>
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>学号：</label>20152421
                </div>
            </div>
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>专业：</label>通信
                </div>
            </div>
        </div>
        <div class="row-fluid row-fluid-apply pdb15">
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>户籍所在地：</label>广东省
                </div>
            </div>
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>手机：</label>13654896545
                </div>
            </div>
            <div class="span4">
                <div class="apply-intro-item">
                    <label class="apply-label"><i>*</i>电子邮箱：</label>124785@qq.com
                </div>
            </div>
        </div>
        <h4 class="apply-title"><span>团队基本信息</span></h4>
        <div class="apply-panel">
            <table class="table table-hover table-bordered table-condensed table-center table-theme-default">
                <thead>
                <tr>
                    <th>姓名</th>
                    <th>性别</th>
                    <th>年龄</th>
                    <th>所在/毕业院校</th>
                    <th>最高学历</th>
                    <th>所学专业</th>
                    <th>拟任职务</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>李思缔</td>
                    <td>男</td>
                    <td>23</td>
                    <td>机械与电子学院</td>
                    <td>本科在读</td>
                    <td>通信</td>
                    <td>团队负责人</td>
                </tr>
                <tr>
                    <td>刘家胜</td>
                    <td>男</td>
                    <td>23</td>
                    <td>机械与电子学院</td>
                    <td>本科在读</td>
                    <td>通信</td>
                    <td>组成员</td>
                </tr>
                <tr>
                    <td>郭力</td>
                    <td>男</td>
                    <td>22</td>
                    <td>机械与电子学院</td>
                    <td>本科在读</td>
                    <td>通信</td>
                    <td>组成员</td>
                </tr>
                </tbody>
            </table>
        </div>
        <h4 class="apply-title"><span>申请入驻项目</span></h4>
        <div class="apply-panel">
            <div class="form-horizontal">
                <div class="control-group">
                    <label class="control-label"><i>*</i>项目名称：</label>
                    <div class="controls">
                        <p class="control-static">网络信贷平台监管的立法构建研究</p>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><i>*</i>创业项目简介：</label>
                    <div class="controls">
                        <p class="control-static">今年国家的宏观经济走向不好，证券市场的收益高，但如果没有足够的知识和内幕行情很容易赔得血本无归。这时网络信贷开始悄然兴起，逐渐形成了继股票市场外第二个民间投资平台。在其中起着非常重要的作用的是P2P平台。但由于我国的制度并不完善，导致今年出现600多家P2P平台相继倒闭，投资人的钱被打水漂的状况。所以，我们选取这个题目来作为我们科研的主要研究方向，希望通过我们的进一步研究调查，为今后的P2P平台规范构建做出贡献。</p>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label"><i>*</i>附件：</label>
                    <div class="controls">
                        <ul class="down-files">
                            <li><a href="javascript:void (0);">网络信贷平台监管的立法构建研究-项目计划书.docx</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
        <h4 class="apply-title"><span>审核</span></h4>
        <div class="apply-panel">
            <div class="form-horizontal">
                <div class="control-group">
                    <label class="control-label">审核</label>
                    <div class="controls">
                        <select v-model="applyStatus">
                            <option value="">-请审核-</option>
                            <option v-for="item in applyStatues" :value="item.id">{{item.label}}</option>
                        </select>
                    </div>
                </div>
                <div v-if="!applyStatus" class="control-group">
                    <label class="control-label"><i>*</i>审核意见</label>
                    <div class="controls">
                        <textarea style="min-width: 500px;" rows="5"></textarea>
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <button type="button" class="btn btn-back-oe btn-primaryBack-oe" onclick="history.go(-1)">保存</button>
                        <button type="button" class="btn btn-back-oe btn-default" onclick="history.go(-1)">返回</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
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
                applyStatues: [{
                    label: '通过',
                    id: 1
                },{
                    label: '不予通过',
                    id: 0
                }],
                applyStatus: '',
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