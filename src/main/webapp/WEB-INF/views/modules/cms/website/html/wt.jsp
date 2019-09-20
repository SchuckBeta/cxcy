<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>入驻申请</title>
    <meta name="decorator" content="cyjd-site-default"/>
</head>
<body>
<div class="container container-ct">
    <div class="row-apply">
        <div class="topbar clearfix"><a href="javascript:void (0)" class="pull-right btn-print">打印申请表</a>
            <span>填报日期：</span></div>
        <h4 class="titlebar">创业人基本信息</h4>
        <div class="form-horizontal form-horizontal-apply">
            <div class="row row-user-info">
                <div class="col-xs-4">
                    <label class="label-static">申请人：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
                <div class="col-xs-4">
                    <label class="label-static">入驻期限：</label>
                    <p class="form-control-static primary-color">王清腾</p>
                </div>
                <div class="col-xs-4">
                    <label class="label-static">学院：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-4">
                    <label class="label-static">学号：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
                <div class="col-xs-4">
                    <label class="label-static">联系方式：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
                <div class="col-xs-4">
                    <label class="label-static">邮件：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
            </div>
            <div class="row row-user-info">
                <div class="col-xs-4">
                    <label class="label-static">证件类型：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
                <div class="col-xs-8">
                    <label class="label-static">证件号码：</label>
                    <p class="form-control-static">王清腾</p>
                </div>
            </div>
        </div>
    </div>
    <div class="row-apply">
        <h4 class="titlebar">入驻申请信息</h4>
        <div class="form-horizontal form-horizontal-tabs">
            <div class="form-group">
                <span class="checkbox-inline active">
                    <a href="javascript: void (0);" class="tab-item">申请入驻创业企业</a>
                </span>
            </div>
         </div>
        <div class="panel-body">
            <div class="panel-inner">
                <div class="form-horizontal form-enter-apply">
                    <div class="form-group">
                        <label class="control-label col-xs-2">企业名称：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static">企业名称</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">联系方式：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static">联系方式</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">企业地址：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static">企业地址</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">公司执照号：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static">公司执照号</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">公司注册资金：</label>
                        <div class="col-xs-2">
                            <p class="form-control-static">公司注册资金</p>
                        </div>
                    </div>
                    <div class="form-group"><label class="control-label col-xs-2">资金来源：</label>
                        <div class="col-xs-10">
                            <p class="form-control-static">自筹；借入</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">企业法人：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static">企业法人</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">地址：</label>
                        <div class="col-xs-8">
                            <p class="form-control-static">地址</p>
                        </div>
                    </div>
                    <div class="form-group"><label class="control-label col-xs-2"><i>*</i>附件：</label>
                        <div class="col-xs-8"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-horizontal form-horizontal-tabs">
            <div class="form-group">
                <span class="checkbox-inline">
                    <a href="javascript: void (0);" class="tab-item">申请入驻创业团队</a>
                </span>
            </div>
        </div>
        <div class="panel-body">
            <div class="panel-inner">
                <div class="form-horizontal form-enter-apply">
                    <div class="form-group">
                        <label class="control-label col-xs-2">选择团队：</label>
                        <div class="col-xs-6">
                            <p class="form-control-static">选择团队</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">学生团队：</label>
                        <div class="col-xs-10">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>姓名</th>
                                    <th>学号</th>
                                    <th>学院</th>
                                    <th>专业</th>
                                    <th>技术领域</th>
                                    <th>联系电话</th>
                                    <th>在读学位</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>王清腾</td>
                                    <td>134564654</td>
                                    <td>测试学院</td>
                                    <td>测试专业</td>
                                    <td>技术淋雨</td>
                                    <td>手机</td>
                                    <td>学位</td>
                                </tr> <!----></tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group">
                        <label class="control-label col-xs-2">导师团队：</label>
                        <div class="col-xs-10">
                            <table class="table table-bordered table-condensed table-orange table-center table-nowrap table-hover">
                                <thead>
                                <tr>
                                    <th>序号</th>
                                    <th>姓名</th>
                                    <th>单位（学院或企业、机构）</th>
                                    <th>职称（职务）</th>
                                    <th>技术领域</th>
                                    <th>联系电话</th>
                                    <th>E-mail</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>1</td>
                                    <td>王清腾</td>
                                    <td>测试学院</td>
                                    <td>职称</td>
                                    <td>技术淋雨</td>
                                    <td>手机</td>
                                    <td>邮箱</td>
                                </tr> <!----></tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-horizontal form-horizontal-tabs">
            <div class="form-group">
                <span class="checkbox-inline">
                    <a href="javascript: void (0);" class="tab-item">申请入驻创业项目</a>
                </span>
            </div>
        </div>
        <div class="panel-body">
            <div class="panel-inner">

            </div>
        </div>
    </div>
</div>


</body>
</html>
