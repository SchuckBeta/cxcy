<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
</head>
<body>

<div class="container container-ct">

    <div class="main-wrap">
        <div class="mybreadcrumbs">
            <%--双创项目&nbsp;&gt;&nbsp;${proModelType}申报--%>
            <%--<i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;双创项目&nbsp;&gt;&nbsp;${proModelType}申报--%>

            <%--<ol class="breadcrumb">--%>
                <%--<i class="icon-home" aria-hidden="true"></i>--%>
              <%--<li><a href="#">首页</a></li>--%>
              <%--<li><a href="#">双创项目</a></li>--%>
              <%--<li class="active">申报</li>--%>
            <%--</ol>--%>

            <ol class="breadcrumb">
                <li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>
                <li><a href="${ctxFront}//page-innovation">双创项目</a></li>
                <li class="active">申报</li>
            </ol>
        </div>
        <div class="row-step-cyjd" style="height: 30px; margin-bottom: 15px;">
            <div class="step-indicator" style="margin-right: -20px;">
                <a class="step completed">第一步（填写个人基本信息）</a>
                <a class="step">第二步（填写项目基本信息）</a>
                <a class="step">第三步（提交项目申报附件）</a>
            </div>
        </div>


        <div class="row-apply" style="margin-top:40px;">
            <div class="form-horizontal">
                <div class="row row-user-info" style="margin-bottom:0;">
                    <div  class="col-xs-3">
                        <label class="application-one">项目编号：</label>
                        <p class="application-p">${proModel.competitionNumber}</p>
                    </div>
                    <div  class="col-xs-3">
                        <label class="application-one">填报日期：</label>
                        <p class="application-p"><fmt:formatDate value='${proModel.createDate}' pattern='yyyy-MM-dd'/></p>
                    </div>
                    <div  class="col-xs-3">
                        <label class="application-one">申报人：</label>
                        <p class="application-p">${leader.name}</p>
                    </div>
                    <div  class="col-xs-3">
                        <label class="application-one">学号：</label>
                        <p class="application-p">${leader.no}</p>
                    </div>
                </div>
            </div>
        </div>




        <div class="row-apply">
            <h4 class="titlebar">${proModelType}申报</h4>
            <form:form id="form1" modelAttribute="proModel" action="#" method="post" class="form-horizontal" style="margin-left:60px"
               	enctype="multipart/form-data">
               	<input type='hidden' id="usertype" value="${cuser.userType}"/>
        		<form:hidden path="id"/>
        		<form:hidden path="actYwId"/>
        		<form:hidden path="year"/>
                <div class="row row-user-info">
                    <div class="col-xs-6">
                        <label class="label-static">项目负责人：</label>
                        <p class="form-control-static">${leader.name}</p>
                    </div>
                    <div class="col-xs-6">
                        <label class="label-static">性别：</label>
                        <p class="form-control-static">${fns:getDictLabel(leader.sex,'sex','')}</p>
                    </div>
                </div>
                <div class="row row-user-info">
                    <div class="col-xs-6">
                        <label class="label-static">所属学院：</label>
                        <p class="form-control-static">${leader.office.name}</p>
                    </div>
                    <div class="col-xs-6">
                        <label class="label-static">专业：</label>
                        <p class="form-control-static">${fns:getProfessional(leader.professional)}</p>
                    </div>
                </div>
                <div class="row row-user-info">
                    <div class="col-xs-6">
                        <label class="label-static">联系电话：</label>
                        <p class="form-control-static">${leader.mobile}</p>
                    </div>
                    <div class="col-xs-6">
                        <label class="label-static">Email：</label>
                        <p class="form-control-static">${leader.email}</p>
                    </div>
                </div>
                
           
	            
	        </form:form>

	        <div class="form-actions-cyjd text-center">
	            <button type="button" class="btn btn-primary btn-save" onclick="saveStep1(this);">下一步</button>
	        </div>
	        
        </div>




    </div>

</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/javascript">
$(function(){
	onProjectApplyInit($("[id='actYwId']").val(),$("[id='id']").val(),null);
});
function saveStep1(obj){
	var onclickFn=$(obj).attr("onclick");
	$(obj).removeAttr("onclick");
	$("#form1").attr("action","/f/proprojectgt/saveStep1");
	$(obj).prop('disabled', true);
	$("#form1").ajaxSubmit(function (data) {
		if(checkIsToLogin(data)){
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
		}else{
			if(data.ret==1){
				top.location="${ctxFront}/proprojectgt/applyStep2?actywId="+$("#actYwId").val()+($("#id").val()==""?"":"&id="+$("#id").val());
			}else{
				$(obj).attr("onclick",onclickFn);
				dialogCyjd.createDialog(data.ret, data.msg, {
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
		$(obj).prop('disabled', false);
	});
}
</script>
</body>
</html>