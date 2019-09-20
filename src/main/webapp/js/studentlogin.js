

//登录方式切换

var studentLogin = {
	messageLogin:function(){
		$(".wholebox .content .mainbox .box ul li.message").click(function(){
			$(".wholebox .content .mainbox .box ul li").removeClass("current");
			$(this).addClass("current");
			$(".wholebox .content .mainbox .box .fillboxone").show();
			$(".wholebox .content .mainbox .box .fillboxtwo").hide();
		})
	},
	accountLogin:function(){
		$(".wholebox .content .mainbox .box ul li.accountpassword").click(function(){
			$(".wholebox .content .mainbox .box ul li").removeClass("current");
			$(this).addClass("current");
			$(".wholebox .content .mainbox .box .fillboxone").hide();
			$(".wholebox .content .mainbox .box .fillboxtwo").show();
		})
	}
}




jQuery.validator.addMethod("isMobile", function(value, element) {  
    var length = value.length;  
    var regPhone = /^1([3578]\d|4[57])\d{8}$/;  
    return this.optional(element) || ( length == 11 && regPhone.test( value ) );    
}, "请正确填写您的手机号码"); 


//暂时未匹配学号
jQuery.validator.addMethod("mobilemailxuehao", function(value, element) {  
    var length = value.length;  
    var regPhone = /^1([3578]\d|4[57])\d{8}$/; 
    var regMail = /^[\w-]+(\.[\w-]+)*@[\w-]+(\.[\w-]+)+$/
    return this.optional(element) || ( length == 11 && regPhone.test( value ) || regMail.test(value));    
}, "请输入您的手机"); 




$("#phonelogin").validate({
	onfocusout: function(element) { $(element).valid(); }, 
    rules: {
        dianhua: {
	    	required: true,
	        isMobile: true,
	        },
	        
	    yanzhengma:{
	    	required: true,
	    }
    },
    messages: {
        dianhua: "*请正确填写您的手机号码",
        yanzhengma: "*请正确填写验证码",
    }
});


$("#accountlogin").validate({
	onfocusout: function(element) { $(element).valid(); }, 
    rules: {
        phonemailxuehao: {
	    	required: true,
	        mobilemailxuehao: true,
	        },
	        
	    password: {
	    	required: true,
	        mobilemailxuehao: true,
	        },
	        
	    yanzhengma:{
	    	required: true,
	    },
    },
    messages: {
        phonemailxuehao: "*请输入您的手机",
        password:"密码不正确",
        yanzhengma: "*请正确填写验证码",
    }
});



























