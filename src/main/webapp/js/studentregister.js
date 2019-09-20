//表单验证


jQuery.validator.addMethod("isMobile", function(value, element) {  
    var length = value.length;  
    var regPhone = /^1([3578]\d|4[57])\d{8}$/;  
    return this.optional(element) || ( length == 11 && regPhone.test( value ) );    
}, "请正确填写您的手机号码"); 



$("#registerfm").validate({
	onfocusout: function(element) { $(element).valid(); }, 
    rules: {
        dianhua: {
	    	required: true,
	        isMobile: true,
	        },
	        
	    yanzhengma:{
	    	required: true,
	    },
	     password: {
	        required: true,
	        minlength: 5
	      },
	      confirm_password: {
	        required: true,
	        minlength: 5,
	        equalTo: "#password"
	      },
    },
    messages: {
        dianhua: "*请正确填写您的手机号码",
        yanzhengma: "*请正确填写验证码",
          password: {
	        required: "请输入密码",
	        minlength: "密码长度不能小于 5 个字母"
	      },
	      confirm_password: {
	        required: "请输入密码",
	        minlength: "密码长度不能小于 5 个字母",
	        equalTo: "两次密码输入不一致"
	      },
    }
});