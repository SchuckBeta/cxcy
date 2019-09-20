UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
UE.Editor.prototype.getActionUrl = function(action) {
    if (action == 'uploadimage' || action == 'uploadscrawl' ||action == 'uploadvideo' || action=='uploadfile') {
    	return $frontOrAdmin+'/ftp/ueditorUpload/uploadTemp?folder=excellentContent';
    }  else {
        return this._bkGetActionUrl.call(this, action);
    }
}
var ue;
var $projectEditForm = $('#projectEditForm');
var $btnSave = $('#btnSave');
var $formKeyword = $('#formKeyword');
var $colKeywordBox = $('.col-keyword-box');
var $keywords = $('.keyword');
function delKey(ob){
	$(ob).parent().remove();
}
$(function(){
    	ue = UE.getEditor('container');
    	

        var projectEdit = new ProjectEdit();
        

});	
    function ProjectEdit() {
		this.init();
	}
    ProjectEdit.prototype.init = function () {
		var validate = this.validate();
		var self = this;
		
		$btnSave.on('click', function () {
			var saveBtn=$(this);
			var isValidate = validate.form();
			if (isValidate) {
				var onclickFn=saveBtn.attr("onclick");
				saveBtn.removeAttr("onclick");
				$("#projectEditForm").attr("action","save");
				$("#projectEditForm").removeAttr("target");
				if($("#isRelease").val()=="1"){
					showModalMessage(0, "该项目已被发布为优秀项目，修改将需要重新审核，是否确定修改？", {
						确定:function() {
							$("#projectEditForm").ajaxSubmit(function (data) {
								if(checkIsToLogin(data)){
									showModalMessage(0, "未登录或登录超时。请重新登录，谢谢！", {
										确定:function() {
											top.location = top.location;
										}
									});
								}else{
									saveBtn.attr("onclick",onclickFn);
									if(data.ret==1){
										$("[name='id']").val(data.id);
										showModalMessage(1, data.msg, {
											确定:function() {
												if($("#fromPage").val()=="gcontest"){
													top.location = "/f/gcontest/gContest/list";
												}
												if($("#fromPage").val()=="project"){
													top.location = "/f/project/projectDeclare/list";
												}
											},
											取消: function() {
												$( this ).dialog( "close" );
											}
										});
									}else{
										showModalMessage(0, data.msg);
									}
								}
							});
						},
						取消: function() {
							$( this ).dialog( "close" );
						}
					});
				}else{
					$("#projectEditForm").ajaxSubmit(function (data) {
						if(checkIsToLogin(data)){
							showModalMessage(0, "未登录或登录超时。请重新登录，谢谢！", {
								确定:function() {
									top.location = top.location;
								}
							});
						}else{
							saveBtn.attr("onclick",onclickFn);
							if(data.ret==1){
								$("[name='id']").val(data.id);
								showModalMessage(1, data.msg, {
									确定:function() {
										if($("#fromPage").val()=="gcontest"){
											top.location = "/f/gcontest/gContest/list";
										}
										if($("#fromPage").val()=="project"){
											top.location = "/f/project/projectDeclare/list";
										}
									},
									取消: function() {
										$( this ).dialog( "close" );
									}
								});
							}else{
								showModalMessage(0, data.msg);
							}
						}
					});
				}
			}
		})
		
		$formKeyword.on('keydown',function(e){
			var keywordHtml = '';
			var xhr;
			var keyword = $(this).val();
			var $next = $(this).parent().next('.col-error')
			if(e.keyCode == 13){
				if(self.hasSameKeyword(keyword)){
					if($next.children().size() < 1){
						$next.append('<label class="error">请不要重复添加</label>');
					}else{
						$next.find('label').text('请不要重复添加').show();
					}
					return false;
				}
				keywordHtml += self.tmpKeyWord(keyword);
				$colKeywordBox.append(keywordHtml);
				
				self.oldKeyWord = keyword;
				$next.empty();
				return false;
			}
		})
	
}

ProjectEdit.prototype.validate = function () {
	return $projectEditForm.validate({
		errorPlacement: function (error, element) {
			if (element.hasClass('form-template') || element.hasClass('form-keyword')) {
				var $target = element.parent().next('.col-error');
				error.appendTo($target);
			} else {
				error.appendTo(element.parent())
			}
		}
	})
}

ProjectEdit.prototype.tmpKeyWord = function(keyword){
	return '<span class="keyword"><input name="keywords" value="'+keyword+'" type="hidden"/><span>'+keyword+'</span><a class="delete-keyword" href="javascript:void(0);" onclick="delKey(this);">&times;</a></span>'
}

ProjectEdit.prototype.hasSameKeyword = function(keyword){
	var hasSameKeyword = false;
	$keywords = $('.keyword');
	$keywords.each(function(i,item){
		var val = $(item).find('span').text();
		if(val == keyword){
			hasSameKeyword = true
			return false;
		}
	})
	
	return hasSameKeyword;
}
function checkIsToLogin(data){
	try{
		if(data.indexOf("id=\"imFrontLoginPage\"") != -1){
			return true;
		}
	}catch(e){
		return false;
	}
}
function ontempchange(){
	ue.setContent($("#temp option:checked").attr("tempcontent"));
}
function preview(){
	$("#projectEditForm").attr("action","../frontExcellentPreview");
	$("#projectEditForm").attr("target","_blank");
	$("#projectEditForm").submit();
}