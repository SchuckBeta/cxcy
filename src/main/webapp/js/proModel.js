$(function(){
	//proModel.initDSSB();
	// fileDownUp("gcontest","delFile");
});

var proModel = {
	printOut:function(){
		$("#print").click(function(){
			document.body.innerHTML=document.getElementById('wholebox').innerHTML;
			window.print();
		})
	},

	checkIsToLogin:function(data){
		try{
			if(data.indexOf("id=\"imFrontLoginPage\"") != -1){
				return true;
			}
		}catch(e){
			return false;
		}
	},
	submit:function(obj){
		$("#competitionfm").attr("action","/f/promodel/proModel/submit");
		if(validate1.form()){
			$(obj).prop('disabled', true);
			$("#competitionfm").ajaxSubmit(function (data) {
				if(proModel.checkIsToLogin(data)){
					showModalMessage(0, "未登录或登录超时。请重新登录，谢谢！", {
						确定: function() {
							location.reload();
						}
					});
				}else{
					if(data.ret==1){
						showModalMessage(1, data.msg, [{
							'text': '确定',
							'click': function(){
								$(this).dialog('close');
								location.reload();
								$(obj).prop('disabled', false);
							}
						}]);
					}else{
						showModalMessage(0, data.msg);
						$(obj).prop('disabled', false);
					}
				}
			});
		}
	}
	
}

