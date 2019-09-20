<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/gContestForm.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/webuploadfile.css">
</head>
<body>
<div id="dialog-message" title="信息">
    <p id="dialog-content"></p>
</div>
<div class="container g-contest-detail">
    <h4 class="main-title">第三届"互联网+"大学生创新创业大赛报名</h4>
    <div class="contest-content form-horizontal">
        <div class="tool-bar">
            <a class="btn-print" onClick="window.print()" href="javascript:void(0);">打印申报表</a>
            <div class="inner">
                <c:if test="${id!=null}">
                    <span>大赛编号：</span>
                    <i>123213123123123</i>
                </c:if>
                <span>填表日期:</span>
                <i>2017/09/10</i>
            </div>
        </div>
        <h4 class="contest-title">大赛报名</h4>
        <div class="contest-wrap">
            <div class="row">
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">申报人：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">学院：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">学号（毕业年份）：</label>
                        <div class="input-box input-inline">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">专业年级：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">联系电话：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">E-mail：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>大赛基本信息</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="row">
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">关联项目：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">国创项目：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">参赛项目名称：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">大赛类别：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">参赛组别：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
                        </div>
                    </div>
                </div>
                <div class="col-sm-6">
                    <div class="form-group">
                        <label class="control-label">融资情况：</label>
                        <div class="input-box">
                            <p class="form-control-static">123213213</p>
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
                    <label class="control-label">团队信息：</label>
                    <div class="input-box">
                        <p class="form-control-static">123213213</p>
                    </div>
                </div>
            </div>
            <div class="table-title">
                <span>学生团队</span>
                <span id="ratio" style="background-color: #fff;color: #df4526;"></span>
            </div>
            <table class="table table-bordered table-team studenttb">
                <thead>
                <tr id="studentTr">
                    <th>序号</th>
                    <th>姓名</th>
                    <th>学号</th>
                    <th>学院</th>
                    <th>专业</th>
                    <th>技术领域</th>
                    <th>联系电话</th>
                    <th>在读学位</th>
                    <th class='credit-ratio'>学分配比</th>
                </tr>
                </thead>
                <tbody>

                </tbody>
            </table>
            <div class="table-title">
                <span>指导教师</span>
            </div>
            <table class="table table-bordered table-team teachertb">
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

                </tbody>
            </table>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>项目介绍</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label">项目介绍：</label>
                <div class="input-box">
                    <p class="form-control-static">123213213</p>
                </div>
            </div>
            <div class="edit-bar edit-bar-sm clearfix">
                <div class="edit-bar-left">
                    <span>附     件</span>
                    <i class="line"></i>
                </div>
            </div>
            <div class="accessory-box">
                <div class="other">
                    <ul class="fileul">
                        <li class="file-item">
                            <div class="file-info file-list">
                                <a class="file" data-id="${item.id}"
                                   data-ftp-url="${item.url}" data-title="${item.name}"
                                   href="javascript:void(0);"> <img class="pic-icon" src="${ctxImg}/filetype/rar.png">资料库
                                </a>
                            </div>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="btn-tool-bar">
                <button type="button" class="btn btn-default" onclick="window.location.history.go(-1)">返回</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>