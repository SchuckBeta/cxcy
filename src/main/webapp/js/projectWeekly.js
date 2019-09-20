$(function() {
	weekly.init();
});

var weekly = {
	init : function() {

	},
	checkIsToLogin : function(data) {
		try {
			if (data.indexOf("id=\"imFrontLoginPage\"") != -1) {
				return true;
			}
		} catch (e) {
			return false;
		}
	},
	save : function() {
		$("#inputForm").attr("action", "save");
		$("#inputForm")
				.ajaxSubmit(
						function(data) {
							if (weekly.checkIsToLogin(data)) {
								$("#dialog-content").html('未登录或登录超时。请重新登录，谢谢！');
								$("#dialog-message").dialog({
									modal : true,
									buttons : {
										确定 : function() {
											top.location = top.location;
										}
									}
								});
							} else {
								if (data.ret == 1) {
									$("#dialog-content").html(data.msg);
									$("#dialog-message")
											.dialog(
													{
														modal : true,
														buttons : {
															确定 : function() {
																top.location = "/f/project/projectDeclare/curProject";
															}
														}
													});
								} else {
									$("#dialog-content").html(data.msg);
									$("#dialog-message").dialog({
										modal : true,
										buttons : {
											确定 : function() {
												$(this).dialog("close");
											}
										}
									});
								}
							}
						});
	},
	subBtn : function() {
		if (weekly.mycheck()) {
			$("#inputForm").attr("action", "submit");
			$("#inputForm")
					.ajaxSubmit(
							function(data) {
								if (weekly.checkIsToLogin(data)) {
									showModalMessage(0, '未登录或登录超时。请重新登录，谢谢！', {
										确定 : function() {
											top.location = top.location;
										}
									});
								} else {
									if (data.ret == 1) {
										showModalMessage(
												1,
												data.msg,
												{
													确定 : function() {
														top.location = "/f/project/projectDeclare/curProject";
													}
												});
									} else {
										showModalMessage(0, data.msg);
									}
								}
							});
		} else {
			return false;
		}
	},
	mycheck : function() {
		var targ = 0;
		// if(!validate1.form()){
		// targ=1;
		// }
		if (!$("[name='projectWeekly.startDate']").val()) {
			showModalMessage(0, "请填写任务开始时间")
			return false;
		}
		if (!$("[name='projectWeekly.endDate']").val()) {
			showModalMessage(0, "请填写任务结束时间");
			return false;
		}
		if (!$("[name='projectWeekly.plan']").val()) {
			showModalMessage(0, "请填写本周任务计划");
			return false;
		}
		if (targ != 0) {
			return false;
		} else {
			return true;
		}
	}
}
