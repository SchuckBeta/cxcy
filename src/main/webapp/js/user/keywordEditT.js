var $formKeyword = $('#formKeyword');
var $colKeywordBox = $('.col-keyword-box');
var $keywords = $('.keyword');
function delKey(ob){
	$(ob).parent().remove();
}
$(function(){
	var projectEdit = new ProjectEdit();
});	
function ProjectEdit() {
	this.init();
}
ProjectEdit.prototype.init = function () {

		var self = this;
		$formKeyword.on('keydown',function(e){
			var keywordHtml = '';
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
	return '<span class="keyword"><input name="keywords" value="'+keyword+'" type="hidden"/><span>'+keyword+'</span><a class="delete-keyword" href="javascript:void(0);" onclick="delKey(this);"></a></span>'
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


