//表单验证

jQuery.validator.addMethod("isMobile", function(value, element) {  
    var length = value.length;  
    var regPhone = /^1([3578]\d|4[57])\d{8}$/;  
    return this.optional(element) || ( length == 11 && regPhone.test( value ) );    
}, "请正确填写您的手机号码");

var validate1=$("#competitionfm").validate({
	onfocusout: function(element) { $(element).valid(); }, 
	onsubmit:false,
    rules: {
    	pName:"required",
    	introduction:"required",
    },
    
    messages: {
    	pName: "请填写参赛项目名称",
    	introduction: "请填写项目介绍",
    },
    
});














