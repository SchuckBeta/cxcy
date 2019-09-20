<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/sco/frontCreditModule.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/md_applyForm.css">
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
    <style>
        .apply-content {
            border: 1px solid #eee;
        }

        .apply-date {
            padding: 10px 15px;
            overflow: hidden;
        }

        .btn-print {
            color: #333;
            text-decoration: none;
        }

        .btn-print:hover {
            color: #df4526;
        }

        .apply-title {
            line-height: 30px;
            padding: 5px 15px;
            margin-top: 0;
            margin-bottom: 15px;
            color: #333;
            font-size: 16px;
            font-weight: 700;
            background-color: #f4e6d4;
            text-align: left;
        }

        .control-label-apply {
            line-height: 30px;
            font-weight: normal;
            text-align: right;
            width: 140px;
            float: left;
            margin: 0;
        }

        .control-label-apply > i {
            font-style: normal;
            color: red;
            margin-right: 4px;
        }

        .input-box-apply {
            line-height: 30px;
            margin-left: 155px;
        }

        .input-box-apply .checkbox-inline input, .input-box-apply .radio-inline input {
            margin-top: 8px;
        }

        .intro-wrap {
            padding: 0 15px;
        }

        .row-intro {
            margin-bottom: 15px;
        }

        .step-one-btns {
            margin-bottom: 30px;
        }

        .alert-status {
            color: red;
        }

        .tabs-apply-enter {
            list-style: none;
            padding: 0;
            margin: 0;
            border-bottom: 1px solid #ccc;
        }

        .tabs-apply-enter > li {
            float: left;
        }

        .tabs-apply-enter > li > a {
            display: block;
            position: relative;
            padding: 6px 20px;
            color: #333;
            text-decoration: none;
        }

        .tabs-apply-enter > li.active > a {
            color: #e9442d;
        }

        .tabs-apply-enter > li.active > a:after {
            content: '';
            position: absolute;
            left: 0;
            right: 0;
            bottom: -1px;
            border-top: 2px solid #e9442d;
        }

        .w175 {
            width: 175px;
        }

        .input-ac-box {
            margin-left: 190px;
        }

        .form-gk .control-label {
            float: left;
        }

        .form-gk {
            padding: 0 15px;
        }

        .form-gk .control-label > i {
            font-style: normal;
            color: red;
            margin-right: 4px;
        }

        .tab-ac-content {
            padding: 30px 0 0 0;
        }

        .step-two-btns {
            margin-bottom: 30px;
        }

        .apply-intro-panel {
            display: none;
        }

        .apply-intro-panel.show {
            display: block;
        }

        .step-two-btns, .step-one-btns {
            display: none;
        }

        .step-two-btns.show {
            display: block;
        }

        .step-one-btns.show {
            display: block;
        }

        .tab-ac-panel {
            display: none;
        }

        .tab-ac-panel.active {
            display: block;
        }

        .table-bordered > thead > tr > td, .table-bordered > thead > tr > th {
            text-align: center;
        }
    </style>
</head>
<body>


<div id="app" class="container project-view-contanier">
    <div class="step-row" style="width: 540px;">
        <div class="step-indicator" style=" margin-right: -20px;">
            <a class="step" :class="{completed: stepOne}" href="javascript:void (0);">第一步（完善基本信息）</a>
            <a class="step" :class="{completed: !panelShow}" href="javascript:void (0);">第二步（输入申报信息）</a>
        </div>
    </div>
    <div class="apply-content">
        <div class="apply-date">
            <a class="pull-right btn-print" href="javascript:void (0)">打印申请表</a>
            <span>填报日期：</span>
        </div>
        <div class="apply-print-container">
            <div class="apply-intro-panel" :class="{show: panelShow}">
                <h4 class="apply-title">第一步：创业人基本信息</h4>
                <div class="intro-wrap">
                    <div class="row row-intro">
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>姓名：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control">
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>性别：</label>
                            <div class="input-box-apply">
                                <label class="radio-inline">
                                    <input type="radio" name="sex">男
                                </label>
                                <label class="radio-inline">
                                    <input type="radio" name="sex">女
                                </label>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>出生年月：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control Wdate">
                            </div>
                        </div>
                    </div>
                    <div class="row row-intro">
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>证件类型：</label>
                            <div class="input-box-apply">
                                <select class="form-control">
                                    <option>-请选择-</option>
                                    <option>身份证</option>
                                    <option>护照</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>证件号码：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control">
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>政治面貌：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control Wdate">
                            </div>
                        </div>
                    </div>
                    <div class="row row-intro">
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>学号/毕业年份：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control">
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>学院：</label>
                            <div class="input-box-apply">
                                <select class="form-control">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>专业：</label>
                            <div class="input-box-apply">
                                <select class="form-control">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="row row-intro">
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>户籍所在地：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control">
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>手机：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control">
                            </div>
                        </div>
                        <div class="col-xs-4">
                            <label class="control-label-apply"><i>*</i>电子邮箱：</label>
                            <div class="input-box-apply">
                                <input type="text" class="form-control">
                            </div>
                        </div>
                    </div>
                    <div class="row row-intro">
                        <div class="col-xs-12">
                            <label class="control-label-apply"><i>*</i>创业人简介：</label>
                            <div class="input-box-apply">
                                <textarea class="form-control" rows="5"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="apply-intro-panel" :class="{show: !panelShow}">
                <h4 class="apply-title">第二步：填写入驻申请信息<span class="alert-status">（选择入驻类别，并填写对应的申请信息内容，支持多选）</span></h4>
                <div class="intro-wrap">
                    <ul class="tabs-apply-enter clearfix">
                        <li v-for="(item,index) in tabsApplyEnter" :class="{active: twoCurrentIndex === index}"><a
                                href="javascript:void(0);" @click="twoCurrentIndex = index">{{item.name}}</a></li>
                    </ul>
                    <div class="tab-ac-content">
                        <div class="tab-ac-panel" :class="{active: twoCurrentIndex == 0}">
                            <form class="form-horizontal form-gk">
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>关联项目：</label>
                                    <div class="input-ac-box">
                                        <select class="form-control" v-model="linkPro">
                                            <option value="">-请选择-</option>
                                            <option value="1">大创项目</option>
                                            <option value="2">其它项目</option>
                                        </select>
                                    </div>
                                </div>
                                <div v-show="linkPro" class="form-group">
                                    <label class="control-label w175"><i>*</i>选择项目：</label>
                                    <div class="input-ac-box">
                                        <select class="form-control">
                                            <option>-请选择-</option>
                                            <option>去有机质前后土壤高光谱分析</option>
                                            <option>基于百度地图的武汉老地名查询系统的开发</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>项目名称：</label>
                                    <div class="input-ac-box">
                                        <input type="text" class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>创业项目简介：</label>
                                    <div class="input-ac-box">
                                        <textarea class="form-control" rows="5" placeholder="简单描述创业项目的思路及想法"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>附件：</label>
                                    <div class="input-ac-box">
                                        <div class="fj"></div>
                                        <button type="button" class="btn btn-primary-oe ">上传附件</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="tab-ac-panel" :class="{active: twoCurrentIndex == 1}">
                            <form class="form-horizontal form-gk">
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>团队名称：</label>
                                    <div class="input-ac-box">
                                        <input type="text" class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label"
                                           style="float: none;margin-bottom: 15px;padding-left: 30px;"><i>*</i>创业团队主要管理人员情况：</label>
                                    <div class="input-ac-box" style="margin-left: 0;padding: 0 30px;">
                                        <table class="table table-bordered table-condensed table-theme-default">
                                            <thead>
                                            <tr>
                                                <th>姓名</th>
                                                <th>性别</th>
                                                <th>年龄</th>
                                                <th>所在/毕业院校</th>
                                                <th>最高学历</th>
                                                <th>所学专业</th>
                                                <th>拟任职务</th>
                                                <%--<th>操作</th>--%>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td><input type="text" class="form-control input-sm"
                                                           style="width: 120px;"></td>
                                                <td><select class="form-control input-sm" style="width: 60px">
                                                    <option>男</option>
                                                    <option>女</option>
                                                </select></td>
                                                <td><input type="text" class="form-control input-sm"
                                                           style="width: 40px;"></td>
                                                <td><select class="form-control input-sm" style="width: 150px">
                                                    <option>-请选择-</option>
                                                </select></td>
                                                <td><select class="form-control input-sm" style="width: 150px">
                                                    <option>-请选择-</option>
                                                </select></td>
                                                <td><select class="form-control input-sm" style="width: 120px">
                                                    <option>-请选择-</option>
                                                </select></td>
                                                <td><input type="text" class="form-control input-sm"
                                                           style="width: 300px;"></td>
                                                <%--<td></td>--%>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>附件：</label>
                                    <div class="input-ac-box">
                                        <div class="fj"></div>
                                        <button type="button" class="btn btn-primary-oe ">上传附件</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                        <div class="tab-ac-panel" :class="{active: twoCurrentIndex == 2}">
                            <form class="form-horizontal form-gk">
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>企业（团队）名称：</label>
                                    <div class="input-ac-box">
                                        <input type="text" class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>公司主营业务：</label>
                                    <div class="input-ac-box">
                                        <textarea class="form-control" rows="5"></textarea>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>注册资金：</label>
                                    <div class="input-ac-box">
                                        <input type="text" class="form-control">
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>资金来源：</label>
                                    <div class="input-ac-box">
                                        <label class="checkbox-inline">
                                            <input type="checkbox">自筹
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox">借入
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox">创业基金
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox">基地参股
                                        </label>
                                        <label class="checkbox-inline">
                                            <input type="checkbox">其他
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label w175"><i>*</i>附件：</label>
                                    <div class="input-ac-box">
                                        <div class="fj"></div>
                                        <button type="button" class="btn btn-primary-oe ">上传附件</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="text-center step-one-btns" :class="{show: panelShow}">
            <button type="button" disabled class="btn btn-primary-oe">上一步</button>
            <button type="button" @click="panelShow = !panelShow" class="btn btn-primary-oe">下一步</button>
            <button type="button" class="btn btn-primary-oe">保存</button>
        </div>
        <div class="text-center step-two-btns" :class="{show: !panelShow}">
            <button type="button" @click="panelShow = true" class="btn btn-primary-oe">上一步</button>
            <button type="button" class="btn btn-primary-oe">提交</button>
            <button type="button" class="btn btn-primary-oe">保存</button>
        </div>
    </div>

</div>


<script>
    var ApplyForm = new Vue({
        el: '#app',
        data: function () {
            return {
                stepOne: true,
                stepTwo: false,
                panelShow: true,
                twoCurrentIndex: 0,
                linkPro: '',
                tabStatus: [true, false, false],
                tabsApplyEnter: [{
                    name: '申请入驻创业项目'
                }, {
                    name: '申请入驻创业团队'
                }, {
                    name: '申请入驻创业企业'
                }]
            }
        }
    })
</script>

</body>
</html>
