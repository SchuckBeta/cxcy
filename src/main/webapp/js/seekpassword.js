//密碼找回步驟123
var frontPath =$("#frontPath").html();
$("#procedure1btn").click(function(){
	if (validateone.form()) {
		var yzma = $("#valicodeyzm").val();
		/*alert($('[name="validateCode"]').val())*/
		var	phonemailxuehao = $("#phone").val();
		$("#pspan").html(phonemailxuehao);
		  $.ajax({
              type: "post",
              url: frontPath+"/f/confirm",
              data: {'phonemailxuehao':phonemailxuehao,'valiCode':yzma},
              success: function (result) {
                  if(result == "1") {
                	  $('.step1').hide();
              		  $(".step2").show();
              		  $(".step3").hide();
              		  $("#spanyzm").hide();
              		  $("#imgyzm").hide();
                  }else if(result =="0"){
                	  document.getElementById("error1").innerText="请输入正确的手机号码";
                  }else if(result == "2"){
                	  document.getElementById("error1").innerText="验证码错误!";
                  }else {
                	  document.getElementById("error1").innerText="验证码不能为空!";
                  }
              },
           
          });
		
	} else{
		return false;
	}
})





$("#procedure2btn").click(function(){
	
	if (validatetwo.form()) {
		var wyzm = $("#wyzm").val();
		var	phonemailxuehao = $("#phone").val();
		  $.ajax({
              type: "post",
              url: frontPath+"/f/safeConfirm",
              data: {'wordValicode':wyzm,"phone":phonemailxuehao},
              success: function (result) {
            	  if(result == "1") {
                	  $('.step1').hide();
              		  $(".step2").hide();
              		  $(".step3").show();
                  }else {
                	  document.getElementById("phoneerror").innerText="验证码错误!";
                	  document.getElementById("phoneyz").innerText="";
                  }
              }
          });
		
	} else{
		return false;
	}
})

$("#procedure3btn").click(function(){
	var phonenum = $("#phone").val();
	if (validatetwo.form()) {
		var	repassword = $("#confirm_password").val();
		var	password = $("#password").val();
		if(password == repassword) {
			document.getElementById("passerror").innerText="";
			$.ajax({
	             type: "post",
	             url: frontPath+"/f/resetPwd",
	             data: {'password':password,"repassword":repassword,"phoneNum":phonenum},
	             success: function (result) {
	           	  if(result == "1") {
	           		document.getElementById("passmessage").innerText="修改密码成功!前往登录页面进行登录";
	           		document.getElementById("passerror").innerText="";
	           		$("p").hide(); 
	             }else {
	            	 document.getElementById("passerror").innerText="修改密码失败";
	            	 document.getElementById("passerror").innerText="";
	             }
	             }
	         })
			
		}else {
			document.getElementById("passerror").innerText="两次密码不一致!";
		}
	} else{
		return false;
	}
})

/*$("#sendYzm").click(function(){
	var	phonemailxuehao = $("#phone").val(); 
	$.ajax({
          type: "post",
          url: frontPath+"/f/sendMessage",
          data: {'phone':phonemailxuehao},
          success: function (result) {
        	if(result = "1") {
        		 document.getElementById("phoneyz").innerText="发送成功!";
        		 document.getElementById("phoneerror").innerText="";
        	}
          }
      });
	
}); 
*/
	$("#sendYzm").click(
			function() {
				var second = 60;
				var disable = $("#sendYzm").attr("disabled");
				var	phonemailxuehao = $("#phone").val();
				if (disable == undefined || disable == null
						|| disable == "" || disable == false) {
					$.ajax({
						type: "post",
						url: frontPath+"/f/sendMessage",
						data: {'phone':phonemailxuehao},
						success : function(data) {
						 	if(data=="1"){
						 		 document.getElementById("phoneyz").innerText="发送成功!";
				        		 document.getElementById("phoneerror").innerText="";
						 		 timer = window.setInterval(function() {
									$("#sendYzm").attr("disabled", true);
									$("#sendYzm").text(
											'(' + (--second) + 's)' + '后重新发送');
									if (second == 0) {
										clearInterval(this.timer);
										$("#sendYzm").attr("disabled", false).text(
												'获取验证码');
										second = 60;
									}

								}, 1000);
		                	}else {
		                		document.getElementById("phoneyz").innerText="发送失败!";
				        		document.getElementById("phoneerror").innerText="";
		                	}
			            },
			            error: function (msg) {
			                alert("发送失败");
			            }

					});

					

				} else {
					return false;
				}

			});
	



//表單驗證

jQuery.validator.addMethod("isMobile", function(value, element) {  
    var length = value.length;  
    var regPhone = /^1([3578]\d|4[57])\d{8}$/;  
    return this.optional(element) || ( length == 11 && regPhone.test( value ) );    
}, "请正确填写您的手机号码"); 

jQuery.validator.addMethod("mobilemailxuehao", function(value, element) {  
    var length = value.length;  
    var regPhone = /^1([3578]\d|4[57])\d{8}$/; 
    var regMail = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
    return this.optional(element) || ( length == 11 && regPhone.test( value ) || regMail.test(value));    
}, "请输入您的手机");


var validateone=$("#fmone").validate({
	onfocusout: function(element) { $(element).valid(); }, 
    rules: {
    	
    	phonemailxuehao: {
	    	required: true,
	        mobilemailxuehao: true,
	        },
	    yanzhengma:{
	    	required: true,
	    },
    },
    messages: {
    	phonemailxuehao: "请输入您的手机",
        yanzhengma: "请正确填写验证码",
    }
});

var validatetwo=$("#fmtwo").validate({
	onfocusout: function(element) { $(element).valid(); }, 
    rules: {
	    yanzhengma:{
	    	required: true,
	    },
    },
    messages: {
        yanzhengma: "请正确填写验证码",
    }
});


var validatethree=$("#fmthree").validate({
	onfocusout: function(element) { $(element).valid(); }, 
    rules: {
	     password: {
	        required: true,
	        minlength: 6
	      },
	      confirm_password: {
	        required: true,
	        minlength: 6,
	        equalTo: "#password"
	      },
    },
    messages: {
        password: {
	        required: "请输入密码",
	        minlength: "请输入6~20位数字、字母"
	      },
	      confirm_password: {
	        required: "请确认密码",
	        minlength: "请输入6~20位数字、字母",
	        equalTo: "两次密码输入不一致"
	      },
    }
});

