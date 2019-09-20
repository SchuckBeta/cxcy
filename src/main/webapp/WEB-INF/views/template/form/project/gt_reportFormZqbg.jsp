<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp"%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <%--<meta name="decorator" content="site-decorator"/>--%>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/sco/frontCreditModule.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=15">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script src="${ctxJs}/frontCyjd/uploaderFile.js"></script>
    <title>${frontTitle}</title>
    <style>
        .contest-content .input-box > .form-control-static {
            padding: 7px 0;
        }

        textarea{
            resize: none;
        }

        button {
            width: auto;
            height: auto;
        }

        .table-pro-work > tbody > tr > td {
            line-height: 30px;
        }

        .table-theme-default > thead > tr {
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

        a.step:hover {
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

        .step-row {
            width: 565px;
            height: 30px;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 20px;
        }

        .result-textarea-box {
            margin-bottom: 20px;
        }

        #btnUpload.disabled{
            cursor: not-allowed;
            filter: alpha(opacity = 65);
            -webkit-box-shadow: none;
            box-shadow: none;
            opacity: .65;
        }
        #btnUpload.disabled .webuploader-pick{
            cursor: not-allowed;
        }

        .btn-downfile-newaccessory{
            background-image: url(/img/btn-downfile.png);
            display: inline-block;
            width: 16px;
            height: 16px;
            border: none;
            overflow: hidden;
            cursor: pointer;
            vertical-align: middle;
        }

        .file-info:hover .btn-downfile-newaccessory{
            background-image: url(/img/btn-hover-downfile.png);
        }

    </style>
</head>
<body>


<div class="container project-view-contanier">

    <h4 class="main-title">大学生创新创业训练计划中期报告</h4>
    <form:form id="competitionfm" modelAttribute="proModel" action="${ctxFront}/proprojectgt/midSubmit?gnodeId=${gnodeId}" method="post" class="form-horizontal"
                   enctype="multipart/form-data">

        <div class="contest-content">
            <div class="tool-bar" style="margin-bottom:-2px;">
                <div class="inner">
                    <%--<span>项目编号：${proModel.competitionNumber}</span>--%>
                    <%--<span style="margin-left: 15px;">填报日期:</span>--%>
                    <%--<i>${sysdate}</i>--%>
                    <%--<span style="margin-left: 15px">申请人:</span>--%>
                    <%--<i>${sse.name}</i>--%>
                    <div class="col-xs-4">
                        <p class="text-center topbar-item">项目编号：${proModel.competitionNumber}</p>
                    </div>
                    <div class="col-xs-4">
                        <p class="text-center topbar-item">填报日期：${sysdate}</p>
                    </div>
                    <div class="col-xs-4">
                        <p class="text-center topbar-item"> 申请人：${sse.name}</p>
                    </div>
                </div>
            </div>
            <h4 class="contest-title">项目中期报告</h4>
            <div class="contest-wrap">
                <div class="row">
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label">项目负责人：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.name}</p>
                                <form:hidden path="id"/>
        						<form:hidden path="actYwId"/>
                                <input type="hidden" name="gnodeId" id="gnodeId" value="${gnodeId}"/>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label">学院：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.office.name}</p>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-4">
                        <div class="form-group">
                            <label class="control-label">联系电话：</label>
                            <div class="input-box">
                                <p class="form-control-static">${sse.mobile}</p>
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
                            <label class="control-label">项目名称：</label>
                            <div class="input-box">
                                <p class="form-control-static">${proModel.pName}</p>
                            </div>
                        </div>
                    </div>
                   <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">项目类别：</label>
                            <div class="input-box">
                                <p class="form-control-static">${fns:getDictLabel(proModel.proCategory, "project_type","" )}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label">项目简介：</label>
                            <div class="input-box">
                                <p class="form-control-static">${proModel.introduction}</p>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-6">
                        <div class="form-group">
                            <label class="control-label">项目logo：</label>
                            <div class="input-box">
                                <p class="form-control-static"><img style="width:40px;height: 40px;border-radius:50%;" id="logoImg" paramurl="${proModel.logo.url }" class="backimg" src="${empty proModel.logo.url ? '/images/upload-default-image1.png':fns:ftpImgUrl(proModel.logo.url)}"></p>
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
                        <div class="input-box" style="max-width: 394px;">
                            <p class="form-control-static">${team.name}</p>
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
	                    <th>工号</th>
	                    <th>学院</th>
	                    <th>专业</th>
	                    <th>手机号</th>
	                    <th>现状</th>
	                    <th>当前在研</th>
	                    <th>技术领域</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamStu!=null&&teamStu.size() > 0}">
                        <c:forEach items="${teamStu}" var="item" varStatus="status">
                            <tr>
				                        <td>${status.index+1 }</td>
				                        <td>${item.name }</td>
				                        <td>${item.no }</td>
				                        <td>${item.org_name }</td>
				                        <td>${item.professional}</td>
				                        <td>
				                        	${item.mobile }
				                        </td>
				                        <td>${fns:getDictLabel(item.currState, 'current_sate', '')}</td>
				                        <td>${item.curJoin }</td>
				                        <td>${item.domain }</td>
				                    </tr>
                        </c:forEach>
                    </c:if>
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
	                    <th>单位(学院或企业、机构)</th>
	                    <th>导师来源</th>
	                    <th>当前指导</th>
	                    <th>技术领域</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:if test="${teamTea!=null&&teamTea.size() > 0}">
                        <c:forEach items="${teamTea}" var="item" varStatus="status">
                            <tr>
                                <tr>
				                        <td>${status.index+1 }</td>
				                        <td>${item.name }</td>
				                        <td>${item.no }</td>
				                        <td>${item.org_name }</td>
				                        <td>${item.teacherType}</td>
				                        <td>${item.curJoin }</td>
				                        <td>${item.domain }</td>
				                    </tr>
                            </tr>
                        </c:forEach>
                      </c:if>
                    </tbody>
                </table>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>已取得阶段性成果</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="result-textarea-box">
                    <textarea placeholder="最多2000字" name="proMidSubmit.stageResult" maxLength="2000" class="form-control" rows="5"></textarea>
                </div>
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目申报资料</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label">申报资料：</label>
                            <div class="input-box">
                            	<sys:frontFileUpload className="accessories-h34" fileitems="${sysAttachments}" readonly="true"></sys:frontFileUpload>
                            </div>
                        </div>
                    </div>
                </div>
				<div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>中期检查资料</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label">下载中期检查申请表：</label>
                            <div class="input-box">
                                <ul id="downFileUl" class="file-list">
                                    <li class="file-item">
                                        <div class="file-info">
                                            <img src="${ctxImg}/filetype/word.png" style="margin-left:-8px;">
                                            <a href="javascript:void(0)" style="margin-left:6px;">大学生创新创业项目中期检查表</a>
                                            <i title="大学生创新创业项目中期检查表.doc" class="btn-downfile-newaccessory" style="margin-left:10px;"></i>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>上传中期检查资料：</label>
                            <div class="input-box" id="midFilesDiv" style="margin-left:150px;">
                            	<sys:frontFileUpload className="accessories-h34" fileitems="${emptFiels}" filepath="projectgt" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="btngroup">
                    <button type="button" class="btn btn-primary-oe" onclick="midSubmit(this)">提交</button>
                    <button type="button" onclick="history.go(-1)" class="btn btn-default-oe">返回</button>
                </div>
            </div>
        </div>
    </form:form>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
<script type="text/javascript">
function midSubmit(obj){

    if (ckeckFiles()) {
    	dialogCyjd.createDialog(0, '提交后不可修改，是否继续？', {
            dialogClass: 'dialog-cyjd-container none-close',
            buttons: [{
                text: '继续',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                	$(this).dialog('close');
    	            $(obj).prop('disabled', true);
    	            $(obj).prev().addClass('disabled').find('input[type="file"]').prop('disabled', true)
    	            $("#competitionfm").ajaxSubmit(function (data) {
    	                 isLogin = checkIsToLogin(data);
    	                 if (isLogin) {
    	                     $(obj).prop('disabled', false);
    	                     $(obj).prev().removeClass('disabled').find('input[type="file"]').prop('disabled', false)
    	                     dialogTimeout();
    	                 } else {
    	                     if(data.ret=='1'){
   	                         	$('.icon-remove-sign').detach();
    	                        dialogCyjd.createDialog(1, data.msg, {
    	                             dialogClass: 'dialog-cyjd-container none-close',
    	                             buttons: [{
    	                                 text: '确定',
    	                                 'class': 'btn btn-sm btn-primary',
    	                                 click: function () {
    	                                     $(this).dialog('close');
    	                                     //top.location = "/f/project/projectDeclare/list";
                                             top.location = "/f/project/projectDeclare/curProject";
    	                                 }
    	                             }]
    	                         });
    	                     }else{
    	                    	 $(obj).prop('disabled', false);
    	                    	 $(obj).prev().removeClass('disabled').find('input[type="file"]').prop('disabled', false);
    	                    	 dialogCyjd.createDialog(0, data.msg, {
    	                             dialogClass: 'dialog-cyjd-container none-close',
    	                             buttons: [{
    	                                 text: '确定',
    	                                 'class': 'btn btn-sm btn-primary',
    	                                 click: function () {
    	                                     $(this).dialog('close');
    	                                 }
    	                             }]
    	                         });
    	                     }
    	                 }
    	            });
                }
            },{
                text: '取消',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close');
                }
            }]
        });
    } else {
    	dialogCyjd.createDialog(0, "请上传附件", {
            dialogClass: 'dialog-cyjd-container none-close',
            buttons: [{
                text: '确定',
                'class': 'btn btn-sm btn-primary',
                click: function () {
                    $(this).dialog('close');
                }
            }]
        });
    }
}
function ckeckFiles(){
	var newfiles=$("#midFilesDiv").find("input[name='attachMentEntity.fielFtpUrl']");
	if(newfiles.length>0){
		return true;
	}else{
		var oldfiles=$("#midFilesDiv").find("a[class='accessory-file']");
		if(oldfiles.length>0){
    		return true;
    	}else{
    		return false;
    	}
	}
}
function checkIsToLogin(data) {
    return typeof data == 'string' && data.indexOf("id=\"imFrontLoginPage\"") > -1;
}
function dialogTimeout() {
	dialogCyjd.createDialog(0, "未登录或登录超时。请重新登录，谢谢！", {
        dialogClass: 'dialog-cyjd-container none-close',
        buttons: [{
            text: '确定',
            'class': 'btn btn-sm btn-primary',
            click: function () {
                $(this).dialog('close');
                top.location = top.location;
            }
        }]
    });
}


$(function(){
    $('.file-info .btn-downfile-newaccessory').click(function(){
        location.href = "/f/proprojectgt/downTemplate?type=mid";
    });
})


</script>
</body>
</html>
