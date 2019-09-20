<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css"/>
    <title>${frontTitle}</title>
    <style>
        .contest-content .input-box > .form-control-static {
            padding: 7px 0;
        }
        button{
            width: auto;
            height: auto;
        }

        .table-pro-work>tbody>tr>td{
            line-height: 30px;
        }

        .table-theme-default>thead>tr{
            background-color: #f4e6d4;
        }

        .btn-default-oe {
            color: #333;
            background-color: #fff;
            border-color: #ccc
        }

        .btn-default-oe.focus, .btn-default-oe:focus {
            color: #333;
            background-color: #e6e6e6;
            border-color: #8c8c8c
        }

        .btn-default-oe:hover {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad
        }

        .step-indicator {
            margin-bottom: 20px;
            line-height: 30px;
        }

        a.step {
            display: block;
            float: left;
            font-weight: bold;
            background: #f7f7f7;
            padding-right: 10px;
            height: 30px;
            width: 240px;
            text-align: center;
            line-height: 32px;
            margin-right: 33px;
            position: relative;
            text-decoration: none;
            color: #4c4b4a;
            cursor: default;
        }
        .step:before {
            content: "";
            display: block;
            width: 0;
            height: 0;
            position: absolute;
            top: 0;
            left: -30px;
            border: 15px solid transparent;
            border-color: #f7f7f7;
            border-left-color: transparent;
        }
        .step:after {
            content: "";
            display: block;
            width: 0;
            height: 0;
            position: absolute;
            top: 0;
            right: -30px;
            border: 15px solid transparent;
            border-left-color: #f7f7f7;
        }
        .step:first-of-type {
            border-radius: 2px 0 0 2px;
            padding-left: 15px;
        }
        .step:first-of-type:before {
            display: none;
        }
        .step:last-of-type {
            border-radius: 0 2px 2px 0;
            margin-right: 25px;
            padding-right: 15px;
        }
        .step:last-of-type:after {
            display: none;
        }
        a.step:hover{
            text-decoration: none;
        }
        .step.completed {
            background: #ffdacf;
            color: #de3b0a;
            cursor: pointer;
        }
        .step.completed:before {
            border-color: #ffdacf;
            border-left-color: transparent;
        }
        .step.completed:after {
            border-left-color: #ffdacf;
        }
        .step.completed:hover {
            background: #ffdacf;
            border-color: #ffdacf;
            color: #de3b0a;
            text-decoration: none;
        }
        .step.completed:hover:before {
            border-color: #ffdacf;
            border-left-color: transparent;
        }
        .step.completed:hover:after {
            border-left-color: #ffdacf;
        }

        .step-row{
            width: 565px;
            height: 30px;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 20px;
        }


    </style>
</head>
<body>


<div class="container project-view-contanier">
    <input type="hidden" id="pageType" value="edit">
    <div class="step-row">
        <div class="step-indicator">
            <a class="step" href="#">第一步</a>
            <a class="step" href="#">第二步</a>
        </div>
    </div>
    <form:form modelAttribute="" action="save" method="post" class="form-horizontal"
               enctype="multipart/form-data">
        <%--<h4 class="main-title">大学生创新创业训练计划项目申报</h4>--%>
        <div class="contest-content">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：2017/09/17</span>
                    <span style="margin-left: 15px;">填表日期:</span>
                    <i>2017/9/17</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>王清腾</i>
                </div>
            </div>
            <h4 class="contest-title">项目申报表</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">项目负责人：</label>
                            <div class="input-box">
                                <p class="form-control-static">王清腾</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">学号：</label>
                            <div class="input-box">
                                <p class="form-control-static">2017/09/17</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">性别：</label>
                            <div class="input-box">
                                <p class="form-control-static">男</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">民族：</label>
                            <div class="input-box">
                                <p class="form-control-static">汉</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static">噢易创新创业云平台</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">专业年级：</label>
                            <div class="input-box">
                                <p class="form-control-static">幼儿园</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">2323232323</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">E-mail：</label>
                            <div class="input-box">
                                <p class="form-control-static">738826716@qq.com</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">身份证号：</label>
                            <div class="input-box">
                                <p class="form-control-static">4211811991XXXXXXXXXXXXX</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">QQ：</label>
                            <div class="input-box">
                                <p class="form-control-static">738826716</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目基本信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>项目名称：</label>
                            <div class="input-box">
                                <input type="text" class="form-control required" value=""/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>所属学科：</label>
                            <div class="input-box">
                                <%--是否需要用treeselect--%>
                                <input type="text" class="form-control" value=""/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>项目类别：</label>
                            <div class="input-box">
                                <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>申报级别：</label>
                            <div class="input-box">
                                <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>申请金额：</label>
                            <div class="input-box">
                                <input type="text" class="form-control required number">
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>项目来源：</label>
                            <div class="input-box">
                                <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">来源项目名称：</label>
                            <div class="input-box">
                                <select class="form-control">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">来源项目类别：</label>
                            <div class="input-box">
                                <select class="form-control">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>是否申请入孵：</label>
                            <div class="input-box">
                                <select class="form-control required">
                                    <option>-请选择-</option>
                                </select>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>团队信息</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="table-condition">
                    <div class="form-group">
                        <label class="control-label"><i class="icon-require">*</i>团队信息：</label>
                        <div class="input-box" style="max-width: 394px;">
                              <select class="form-control required">
                                  <option>-请选择-</option>
                              </select>
                        </div>
                    </div>
                </div>
                <div class="table-title">
                    <span>学生团队</span>
                    <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
                </div>
                <table class="table table-bordered table-pro-work table-condensed table-theme-default">
                    <thead>
                    <tr id="studentTr">
                        <th>序号</th>
                        <th>姓名</th>
                        <th>学号</th>
                        <th>手机号</th>
                        <th>所在学院</th>
                        <th>项目分工</th>
                    </tr>
                    </thead>
                    <tbody>
                    <tr>
                        <td>121345</td>
                        <td>王清腾王清腾·王清腾</td>
                        <td>134566879</td>
                        <td>18696128279</td>
                        <td>计算机软件工程</td>
                        <td><input type="text" class="form-control input-sm required"></td>
                    </tr>
                    </tbody>
                </table>
                <div class="table-title">
                    <span>指导教师</span>
                </div>
                <table class="table table-bordered table-condensed table-theme-default">
                    <thead>
                    <tr>
                        <th>序号</th>
                        <th>姓名</th>
                        <th>工号</th>
                        <th>导师来源</th>
                        <th>职称（职务）</th>
                        <th>学历</th>
                        <th>联系电话</th>
                        <th width="175">E-mail</th>
                    </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>738826716@qq.com</td>
                        </tr>
                    </tbody>
                </table>
                <div class="btngroup">
                    <button type="button" class="btn btn-primary-oe">下一步</button>
                    <button type="button" class="btn btn-primary-oe">保存</button>
                    <button type="button" class="btn btn-default-oe">返回</button>
                </div>
            </div>
        </div>
    </form:form>
</div>

</body>
</html>
