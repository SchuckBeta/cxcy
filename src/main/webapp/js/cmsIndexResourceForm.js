UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == 'uploadimage' || action == 'uploadscrawl' ||action == 'uploadvideo' || action=='uploadfile') {
        return '/ftp/urlLoad/upLoadForUeditor?type=cmsResource';
    }  else {
        return this._bkGetActionUrl.call(this, action);
    }
}

var prePath = $('#fileToUpload').val();
var fileUp = false;

function fileDownUp(type){
	$("#upload").on('click', function() {
	    $('#fileToUpload').click();
	});

	$('#fileToUpload').on('change', function() {
		
	    if(fileUp){
	        return false;
	    }
	    fileUp=true;
	    var curPath = $('#fileToUpload').val();
	    if (prePath == curPath) {
	        fileUp=false;
	        showModalMessage(0, "不能上传相同文件名");
	        return false;
	    }
	    var mb = myBrowser();
	    if ("IE" == mb) {
	    	curPath=curPath.substring(curPath.lastIndexOf('\\')+1);
	    }
	    prePath = curPath;
	    var ftpIds=$("input[name='ftpId']");
	    var ftpId="";
	    if(ftpIds.length>0){
	        ftpId=ftpIds[0].value;
	    }
	    $.ajaxFileUpload({
	        url:'/ftp/urlLoad/urlRenzhengLoad',
	        secureuri:false,
	        fileElementId:'fileToUpload',//file标签的id
	        dataType: 'json',//返回数据的类型
	        data:{type:type,ftpId:ftpId	
	        },//一同上传的数据
	        success: function (data, status) {
	            //把图片替换
	        	fileUp=false;
	            var obj = jQuery.parseJSON(data);
	            var state=obj.state;
	            if(state=="1"){
	                //针对谷歌浏览器会记住上传文件名导致不触发onchange方法
	                $("#fileToUpload").val("");
	                $("#image-upload-input").val(obj.arrUrl);
	                $("#image-upload-input-ftp").val(obj.httpUrl);
	            }else{
	            	showModalMessage(0, obj.msg);
	            }
	        },
	        error: function (data, status, e) {
	            fileUp=false;
	            showModalMessage(0, e);
	        }
	    });
	});
	
}
$(document).ready(function() {
	var ue = UE.getEditor('container');
	fileDownUp("cmsResFile");
	ontplUrlchange();
				$("#inputForm")
						.validate(
								{
									submitHandler : function(form) {
										loading('正在提交，请稍等...');
										form.submit();
									},
									errorContainer : "#messageBox",
									errorPlacement : function(error, element) {
										$("#messageBox").text("输入有误，请先更正。");
										if (element.is(":checkbox")
												|| element.is(":radio")
												|| element.parent().is(
														".input-append")) {
											error.appendTo(element.parent()
													.parent());
										} else {
											error.insertAfter(element);
										}
									}
	});
});
function onResModelChange(){
	var resModel=$("#resModel").val();
	if(resModel=="0"){
		$(".htmlmodel").attr("style","display:none");
		$(".templemodel").attr("style","display:none");
		$(".parammodel").attr("style","display:");
	}
	if(resModel=="1"){
		$(".parammodel").attr("style","display:none");
		$(".htmlmodel").attr("style","display:none");
		$(".templemodel").attr("style","display:");
	}
	if(resModel=="2"){
		$(".parammodel").attr("style","display:none");
		$(".templemodel").attr("style","display:none");
		$(".htmlmodel").attr("style","display:");
	}
	if(!resModel){
		$(".parammodel").attr("style","display:none");
		$(".templemodel").attr("style","display:none");
		$(".htmlmodel").attr("style","display:none");
	}
}
function ontplUrlchange(){
	$("#templeParam").html($("#tplUrl option:checked").attr("jsonparam"));
}