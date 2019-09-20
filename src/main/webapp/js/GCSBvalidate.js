jQuery.validator.addMethod("isMobile", function(value, element) {
    var length = value.length;  
    var regPhone = mobileRegExp;
    return this.optional(element) || ( length == 11 && regPhone.test( value ) );    
}, "请正确填写您的手机号码");  

var validate1=$("#form1").validate({
	onfocusout: function(element) { $(element).valid(); },
	onsubmit:false,
    rules: {
    	user_officename: "required",
    	user_no: "required",
    	user_professional: "required",
    	user_mobile: "required",
    	user_mobile: {
	    	required: true,
	        isMobile: true,
        },
        user_email: {
    	required: true,
        email: true,
        },
        'projectDeclare.name': "required",
        'projectDeclare.introduction': "required",
        'projectDeclare.innovation': "required",
        'projectDeclare.planContent': "required",
	    'projectDeclare.planStartDate':{
	    	required: true,
	        date: true,
	        },
	    'projectDeclare.planEndDate': {
	    	required: true,
	        date: true,
	        },
	    'projectDeclare.planStep': "required",
	    'projectDeclare.resultContent': "required"
    },
    messages: {
    	user_officename: "请完善学院名称",
    	user_no: "请完善学号",
    	user_professional: "请完善专业信息",
    	user_mobile: "请完善正确的手机号码",
    	user_email: "请完善正确的邮箱",
    	'projectDeclare.name': "必填信息",
        'projectDeclare.introduction': "必填信息",
        'projectDeclare.innovation': "必填信息",
        'projectDeclare.planContent': "必填信息",
        'projectDeclare.planStartDate': "必填信息",
        'projectDeclare.planEndDate': "必填信息",
        'projectDeclare.planStep': "必填信息",
        'projectDeclare.resultContent': "必填信息"
    },
    errorPlacement: function (error, element) {
        if ($(element).attr("type") == "checkbox" || $(element).attr("type") == "radio") {
            error.appendTo($(element).parent().parent().parent());
        } else {
            error.insertAfter(element);
        }
    }
});

function mycheck(){
	var targ=0;
	if(!validate1.form()){
		targ=1;
	}
	if(targ!=0){
		return false;
	}else{
		return true;
	}
}



