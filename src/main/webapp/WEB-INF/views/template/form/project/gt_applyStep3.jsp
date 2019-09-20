<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/webuploader/webuploader.css">
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript"  src="${ctxJs}/frontCyjd/uploaderFile.js?v=101"></script>
</head>


<body>


<div class="container container-ct">

    <div class="main-wrap">
        <ol class="breadcrumb">
            <li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>
            <li><a href="${ctxFront}//page-innovation">双创项目</a></li>
            <li class="active">申报</li>
        </ol>
        <div class="row-step-cyjd" style="height: 30px; margin-bottom: 15px;">
            <div class="step-indicator" style="margin-right: -20px;">
                <a class="step">第一步（填写个人基本信息）</a>
                <a class="step">第二步（填写项目基本信息）</a>
                <a class="step completed">第三步（提交项目申报附件）</a>
            </div>
        </div>


        <div class="row-apply" style="margin-top:40px;">

            <div class="form-horizontal">
                <div class="row row-user-info" style="margin-bottom:0;">
                    <div class="col-xs-3">
                        <label class="application-one">项目编号：</label>
                        <p class="application-p">${proModel.competitionNumber}</p>
                    </div>
                    <div class="col-xs-3">
                        <label class="application-one">填报日期：</label>
                        <p class="application-p"><fmt:formatDate value='${proModel.createDate}'
                                                                 pattern='yyyy-MM-dd'/></p>
                    </div>
                    <div class="col-xs-3">
                        <label class="application-one">申报人：</label>
                        <p class="application-p">${leader.name}</p>
                    </div>
                    <div class="col-xs-3">
                        <label class="application-one">学号：</label>
                        <p class="application-p">${leader.no}</p>
                    </div>
                </div>
            </div>

        </div>


        <div class="row-apply">
            <h4 class="titlebar">${proModelType}申报</h4>
            <form:form id="form1" modelAttribute="proModel" action="#" method="post" class="form-horizontal"
                       style="margin-left:60px"
                       enctype="multipart/form-data">
                <form:hidden path="id"/>
                <form:hidden path="actYwId"/>
                <form:hidden path="year"/>
                <div class="form-group" style="position:relative;left:25px;margin-top:30px;">
                    <label class="control-label col-xs-1" style="width: 170px;">下载项目申报模板：</label>
                    <div id="projectInfo" class="panel-body collapse in" style="margin-left:180px;">
                        <div class="accessories">
                            <div class="accessory" id="projectFiles" style="overflow: hidden;">
                                <div class="accessory-info one-line" style="margin:5px 20px 20px 0;">
                                    <a href="${ctxFront}/proprojectgt/downTemplate?type=${proModel.proCategory}"><img
                                            src="${ctxImages}/word.png">
                                        <span class="accessory-name">${fns:getDictLabel(proModel.proCategory, 'project_type', '')}项目申请表</span>
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
                <div class="form-horizontal form-enter-apply" style="margin-left:-1px;">
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>附件：</label>
                        <div class="col-xs-10">
							<c:if test="${not empty proModel.procInsId}">
								<sys:frontFileUpload className="accessories-h34" readonly="true" fileitems="${proModel.fileInfo}" filepath="projectgt" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>
							</c:if>
							<c:if test="${empty proModel.procInsId}">
								<sys:frontFileUpload className="accessories-h34"  fileitems="${proModel.fileInfo}" filepath="projectgt" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>
							</c:if>

                            <%--<sys:frontFileUpload className="accessories-h34" fileitems="${proModel.fileInfo}" filepath="projectgt" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>--%>
                        </div>
                    </div>
                </div>
            </form:form>

            <div class="form-actions-cyjd text-center">
                <a id="prevbtn" class="btn btn-primary" href="javascript:void(0)" onclick="prevStep()" style="margin-right: 10px;">上一步</a>
                <button id="savebtn" data-control="uploader" type="button" class="btn btn-primary btn-save" onclick="saveStep3(this)"
                        style="margin-right: 10px;">保存
                </button>
                <button id="subbtn" type="button" data-control="uploader" class="btn btn-primary" onclick="submitStep3(this)">提交</button>
            </div>

        </div>


    </div>

</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>


<script>

    function saveStep3(obj) {
            var onclickFn = $(obj).attr("onclick");
            $(obj).removeAttr("onclick");
            $("#form1").attr("action", "/f/proprojectgt/saveStep3");
            $(obj).prop('disabled', true);
            $("#form1").ajaxSubmit(function (data) {
                if (checkIsToLogin(data)) {
                	dialogCyjd.createDialog(data.ret, "未登录或登录超时。请重新登录，谢谢！", {
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
                } else {
                    if (data.ret == 1) {
                        $(".fileparamspan").html("");
                        $(obj).attr("onclick", onclickFn);
                        dialogCyjd.createDialog(data.ret, '保存成功，跟踪当前项目？', {
                            dialogClass: 'dialog-cyjd-container none-close',
                            buttons: [{
                                text: '确定',
                                'class': 'btn btn-sm btn-primary',
                                click: function () {
                                    $(this).dialog('close');
                                    top.location = "/f/project/projectDeclare/curProject";
                                }
                            }]
                        });
                    } else {
                        dialogCyjd.createDialog(data.ret, data.msg);
                    }
                }
                $(obj).prop('disabled', false);
            });
    }
    function submitStep3(obj) {
        if (ckeckFiles()) {

        	dialogCyjd.createDialog(0, '提交后不可修改，是否继续？', {
                dialogClass: 'dialog-cyjd-container',
                buttons: [
                    {
	                    text: '继续',
	                    'class': 'btn btn-sm btn-primary',
	                    click: function () {
	                        $(this).dialog('close');

	                        var onclickFn = $(obj).attr("onclick");
	                        $(obj).removeAttr("onclick");
	                        $("#form1").attr("action", "/f/proprojectgt/submitStep3");
	                        $(obj).prop('disabled', true);
	                        $("#form1").ajaxSubmit(function (data) {
	                            if (checkIsToLogin(data)) {
	                                dialogCyjd.createDialog(data.ret, "未登录或登录超时。请重新登录，谢谢！", {
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
	                            } else {
	                                if (data.ret == 1) {
	                                    $(".fileparamspan").html("");
	                                    dialogCyjd.createDialog(data.ret, '保存成功，跟踪当前项目？', {
	                                        dialogClass: 'dialog-cyjd-container',
	                                        buttons: [{
	                                            text: '确定',
	                                            'class': 'btn btn-sm btn-primary',
	                                            click: function () {
	                                                $(this).dialog('close');
	                                                top.location = "${ctxFront}/project/projectDeclare/curProject";
	                                            }
	                                        }]
	                                    });

	                                } else {
	                                    $(obj).attr("onclick", onclickFn);
	                                    dialogCyjd.createDialog(data.ret, data.msg);
	                                }
	                            }
	                            $(obj).prop('disabled', false);
	                        });
	                    }
                	},
                	{
	                    text: '取消',
	                    'class': 'btn btn-sm btn-primary',
	                    click: function () {
	                        $(this).dialog('close');
	                    }
                	}
                ]
            });
        } else {
            dialogCyjd.createDialog(0, "请上传附件");
        }
    }
    function ckeckFiles() {
        var newfiles = $("input[name='attachMentEntity.fielFtpUrl']");
        if (newfiles.length > 0) {
            return true;
        } else {
            var oldfiles = $("a[class='accessory-file']");
            if (oldfiles.length > 0) {
                return true;
            } else {
                return false;
            }
        }
    }
    function prevStep(){
    	var newfiles = $("input[name='attachMentEntity.fielFtpUrl']");
        if (newfiles.length > 0) {
    		saveStep3UnCheck();
    	}else{
    		var id=$("#id").val();
    		toPrevStep(id);
    	}
    }
    function toPrevStep(id){
    	top.location="${ctxFront}/proprojectgt/applyStep2?id="+id;
    }
    function saveStep3UnCheck(){
    	var preFn=$("#prevbtn").attr("onclick");
    	$("#prevbtn").removeAttr("onclick");
    	var saveFn=$("#savebtn").attr("onclick");
    	$("#savebtn").removeAttr("onclick");
    	var subFn=$("#subbtn").attr("onclick");
    	$("#subbtn").removeAttr("onclick");
    	$("#form1").attr("action","/f/proprojectgt/saveStep3");
    	$("#form1").ajaxSubmit(function (data) {
    		if(checkIsToLogin(data)){
    			 dialogCyjd.createDialog(data.ret, "未登录或登录超时。请重新登录，谢谢！", {
                     dialogClass: 'dialog-cyjd-container',
                     buttons: [{
                         text: '确定',
                         'class': 'btn btn-sm btn-primary',
                         click: function () {
                             $(this).dialog('close');
                             top.location = top.location;
                         }
                     }]
                 });
    		}else{
    			if(data.ret==1){
   					toPrevStep($("#id").val());
    			}else{
	    			$("#prevbtn").attr("onclick",preFn);
					$("#savebtn").attr("onclick",saveFn);
					$("#subbtn").attr("onclick",subFn);
    				dialogCyjd.createDialog(data.ret, data.msg, {
                        dialogClass: 'dialog-cyjd-container',
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
    		}
    	});
    }
</script>
</body>
</html>