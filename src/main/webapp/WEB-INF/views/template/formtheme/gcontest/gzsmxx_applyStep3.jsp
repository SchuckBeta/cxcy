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
    <div class="edit-bar clearfix" style="margin-top:0;">
        <div class="edit-bar-left">
            <div class="mybreadcrumbs" style="margin:0 0 20px 9px;">
                <i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;<a href="/f" style="color:#333;text-decoration: underline;">首页</a>&nbsp;&gt;&nbsp;双创大赛&nbsp;&gt;&nbsp;申报
            </div>
        </div>
    </div>

    <div class="main-wrap">
        <%--<ol class="breadcrumb">--%>
            <%--<li><a href="${ctxFront}/"><i class="icon-home"></i>首页</a></li>--%>
            <%--<li><a href="${ctxFront}//page-innovation">双创大赛</a></li>--%>
            <%--<li class="active">申报</li>--%>
        <%--</ol>--%>
        <div class="row-step-cyjd mgb40">
            <div class="step-indicator">
                <a class="step">第一步（团队信息）</a>
                <a class="step">第二步（项目信息）</a>
                <a class="step completed">第三步（提交参赛表单）</a>
            </div>
        </div>

        <div class="row-apply">
            <h4 class="titlebar">${proModelType}申报</h4>
            <form:form id="form1" modelAttribute="proModelGzsmxx" action="#" method="post" class="form-horizontal"
                       style="margin-left:60px" enctype="multipart/form-data">
                <input type="hidden" name="proModel.declareId" id="declareId" value="${cuser.id}"/>
                <input type="hidden" name="id" id="id" value="${proModelGzsmxx.id}"/>
                <input type="hidden" name="actYwId" id="actYwId" value="${proModelGzsmxx.proModel.actYwId}"/>
                <div class="row row-user-info">
                    <div class="col-xs-6">
                        <label class="label-static">填报日期：</label>
                        <p class="form-control-static">${sysdate}</p>
                    </div>
                    <div class="col-xs-6">
                        <label class="label-static">大赛编号：</label>
                        <p class="form-control-static">${proModelGzsmxx.proModel.competitionNumber}</p>
                    </div>
                </div>

                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>附件信息</span>
                        <i class="line"></i>
                    </div>
                </div>

                <div class="form-group" style="position:relative;left:25px;margin-top:30px;">
                    <label class="control-label col-xs-2" style="width: 170px;">表单模板：</label>
                    <div class="col-xs-10">
                        <div class="accessories-container">
                            <div class="accessories accessories-h34 accessories-inline">
                                <div class="accessory">
                                    <div class="accessory-info">
                                        <a href="javascript: void(0);">
                                            <img src="${ctxImg}/filetype/image.png"> <span
                                                class="accessory-name">报名表.docx</span></a>
                                        <a href="${ctxFront}/promodel/proModel/downTemplate?type=gzsmxx-bm"><i title="报名表.docx" class="btn-downfile-newaccessory"></i></a>
                                    </div>
                                </div>
                            </div>
                            <div class="accessories accessories-h34 accessories-inline">
                                <div class="accessory">
                                    <div class="accessory-info">
                                        <a href="javascript: void(0);">
                                            <img src="${ctxImg}/filetype/image.png"> <span
                                                class="accessory-name">作品申报书.docx</span></a>
                                        <a href="${ctxFront}/promodel/proModel/downTemplate?type=gzsmxx-sb"><i title="作品申报书.docx" class="btn-downfile-newaccessory"></i></a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>


                <div class="form-horizontal form-enter-apply" style="margin-left:-1px;">
                    <div class="form-group">
                        <label class="control-label col-xs-2"><i>*</i>附件：</label>
                        <div class="col-xs-10">
                            <c:if test="${not empty proModelGzsmxx.proModel.procInsId}">
                                <sys:frontFileUpload className="accessories-h34" readonly="true" fileitems="${proModelGzsmxx.proModel.fileInfo}" filepath="project" tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>
                            </c:if>
                            <c:if test="${empty proModelGzsmxx.proModel.procInsId}">
                                <sys:frontFileUpload className="accessories-h34"  fileitems="${proModelGzsmxx.proModel.fileInfo}" filepath="project" tip1="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>
                            </c:if>
                                <%--<sys:frontFileUpload className="accessories-h34" fileitems="${proModel.fileInfo}" filepath="projectgt" tip2="（上传项目申报表单、商业计划书等项目文件）"></sys:frontFileUpload>--%>
                        </div>
                    </div>
                </div>
            </form:form>

            <div class="form-actions-cyjd text-center">
                <a id="prevbtn" class="btn btn-primary" data-control="uploader" href="javascript:void(0)" onclick="prevStep()" style="margin-right: 10px;">上一步</a>
                <button id="savebtn" type="button" data-control="uploader" class="btn btn-primary btn-save" onclick="saveStep3(this)"
                        style="margin-right: 10px;">保存
                </button>
                <button id="subbtn" data-control="uploader" type="button" class="btn btn-primary" onclick="submitStep3(this)">提交</button>
            </div>

        </div>


    </div>

</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>


<script>

    function saveStep3(obj) {
        var onclickFn = $(obj).attr("onclick");
        $(obj).removeAttr("onclick");
        $("#form1").attr("action", "/f/proModelGzsmxx/ajaxSave3");
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
                    dialogCyjd.createDialog(data.ret,  '保存成功，跟踪当前大赛？', {
                    	dialogClass: 'dialog-cyjd-container none-close',
                        buttons: [{
                            text: '确定',
                            'class': 'btn btn-sm btn-primary',
                            click: function () {
                                $(this).dialog('close');
                                top.location = "/f/gcontest/gContest/list";
                            }
                        },{
                            text: '取消',
                            'class': 'btn btn-sm btn-primary',
                            click: function () {
                                $(this).dialog('close');
                                top.location = top.location;
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
                            $("#form1").attr("action", "/f/proModelGzsmxx/submit");
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
                                        dialogCyjd.createDialog(data.ret,  '保存成功，跟踪当前大赛？', {
                                            dialogClass: 'dialog-cyjd-container none-close',
                                            buttons: [{
                                                text: '确定',
                                                'class': 'btn btn-sm btn-primary',
                                                click: function () {
                                                    $(this).dialog('close');
                                                    top.location = "/f/gcontest/gContest/list";
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
           return oldfiles.length > 0
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
        top.location="${ctxFront}/proModelGzsmxx/applyStep2?id="+id;
    }
    function saveStep3UnCheck(){
        var preFn=$("#prevbtn").attr("onclick");
        $("#prevbtn").removeAttr("onclick");
        var saveFn=$("#savebtn").attr("onclick");
        $("#savebtn").removeAttr("onclick");
        var subFn=$("#subbtn").attr("onclick");
        $("#subbtn").removeAttr("onclick");
        $("#form1").attr("action","/f/proproject/saveStep3");
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