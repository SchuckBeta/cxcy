<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>场地分配</title>
    <meta name="decorator" content="cyjd-site-default"/>

</head>
<body>
<div class="container container-ct">
    <ol class="breadcrumb" style="margin-top: 0">
        <li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>
        <li class="active">房间名</li>
    </ol>
    <div class="row-apply mgb15">
        <h4 class="titlebar">房间信息</h4>
        <div class="panel-body collapse in">
            <div class="panel-inner">
                <div class="row row-info-fluid" style="height: auto">
                    <div class="col-xs-6">
                        <p><span class="item-label">名称：</span><i></i>王清腾</p>
                        <p><span class="item-label">房间类型：</span>会议室</p>
                        <p><span class="item-label">容纳人数：</span>123人</p>
                        <p><span class="item-label">负责人：</span>123人</p>
                    </div>
                    <div class="col-xs-6">
                        <p><span class="item-label">别名：</span>王清腾</p>
                        <p><span class="item-label">允许多团队入驻：</span>是</p>
                        <p><span class="item-label">基地/楼栋/楼层：</span>基地/楼栋/楼层</p>
                        <p><span class="item-label">手机：</span>18696128279</p>
                    </div>
                </div>
            </div>
        </div>
        <h4 class="titlebar">房间分配信息</h4>
        <div class="panel-body collapse in">
            <div class="panel-inner">
                <table id="assignedTable"
                       class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-assigned">
                    <thead>
                    <tr>
                        <th>入驻编号</th>
                        <th>名称（团队/企业/项目）</th>
                        <th>负责人</th>
                    </tr>
                    </thead>
                    <tbody id="assignedTbody">
                    <tr data-index="0">
                        <td>SNPER20171220110508235</td>
                        <td><span class="team-name">VVC团队 / - / -</span></td>
                        <td>张志恒</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
    <div class="text-center">
        <button type="button" class="btn btn-default">返回</button>
    </div>
</div>

</body>
</html>