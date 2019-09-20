//学院专家打分变更
var jsonCollegeExportStr="[]";
var jsonarrayCollegeExport = eval('('+jsonCollegeExportStr+')');
var collegeExportTrue=true;

//学院秘书审核变更
var jsonCollegeStr="{}";
var jsonObjectCollege = eval('('+jsonCollegeStr+')');


//学院专家打分变更
var jsonSchoolExportStr="[]";
var jsonarraySchoolExport = eval('('+jsonSchoolExportStr+')');
var schoolExportTrue=true;

//学校秘书网评审核变更
var jsonSchoolStr="{}";
var jsonObjectSchool = eval('('+jsonSchoolStr+')');

//学校管理员路演审核变更
var jsonSchoollyStr="{}";
var jsonObjectSchoolly = eval('('+jsonSchoollyStr+')');
var schoollyTrue=true;

//学校管理员评级审核变更
var jsonSchoolendStr="{}";
var jsonObjectSchoolend = eval('('+jsonSchoolendStr+')');

var validate1=$("#changeForm").validate({

})
  
$(function() {
	//学院专家审核
	var state=$("#state").val();
	
	if(state=="1"){
		$("#schoolExportTitle").hide();
		$('tr[name="schoolExport"]').hide();
		$("#schoolTitle").hide();
		$("#schoolluyanTitle").hide();
		$("#schooldefen").hide();
		$("#schoolEndTitle").hide();
		$("#schoolEndAudit").hide();
	}else if(state=="2"){
		$("#schoolExportTitle").hide();
		$('tr[name="schoolExport"]').hide();
		$("#schoolTitle").hide();
		$("#schoolluyanTitle").hide();
		$("#schooldefen").hide();
		$("#schoolEndTitle").hide();
		$("#schoolEndAudit").hide();
	}else if(state=="3"){
		$("#schoolluyanTitle").hide();
		$("#schooldefen").hide();
		$("#schoolEndTitle").hide();
		$("#schoolEndAudit").hide();
	}else if(state=="4"){
		$("#schoolluyanTitle").hide();
		$("#schooldefen").hide();
		$("#schoolEndTitle").hide();
		$("#schoolEndAudit").hide();
	}else if(state=="5"){
		
	}else if(state=="6"){
		
	}else if(state=="7"){
	
	}else if(state=="8"){
		$("#schoolluyanTitle").hide();
		$("#schooldefen").hide();
		$("#schoolEndTitle").hide();
		$("#schoolEndAudit").hide();
	}else if(state=="9"){
		$("#schoolExportTitle").hide();
		$("#schoolTitle").hide();
		$('tr[name="schoolExport"]').hide();
		$("#schoolluyanTitle").hide();
		$("#schooldefen").hide();
		$("#schoolEndTitle").hide();
		$("#schoolEndAudit").hide();
	}
	
	
	$('input[name="collegeExportScoreSt"]').on('change', function() {
		var exportAudit=$(this).parent().parent().parent();
		var auditId=exportAudit.find('input[name="collegeExportAuditId"]').val();
		var auditScore=exportAudit.find('input[name="collegeExportScoreSt"]').val();
		var auditName=exportAudit.find('input[name="collegeExport"]').val();
		var auditSuggest=exportAudit.find('input[name="collegeExportSuggest"]').val();
		//var pattern=/^(\d{1,2}(\.\d{1})?|100)$/;
		var pattern=/^([0-9]{1,2}|100)$/;
		var bl = pattern.test(auditScore); 
		var el = exportAudit.find('.score-error-msg');
		if(bl){
			collegeExportTrue=true;
			el.hide();
		}else{
			collegeExportTrue=false;
			el.text("填写0-100之间分数").show();
			return;
		}
		if(jsonarrayCollegeExport.length>0){
		 	for ( var i = 0; i < jsonarrayCollegeExport.length; i++){
				if(jsonarrayCollegeExport[i].auditName==auditName){
					jsonarrayCollegeExport.splice(i, 1);
				}  
			}
		}
		var auditObject  = {
			         "auditId" : auditId,
					 "auditName" : auditName,
					 "auditSuggest" : auditSuggest,
			         "auditScore" : auditScore
			     }
		jsonarrayCollegeExport.push(auditObject);
		changeCollegeSocre();
	})
	
	$('input[name="collegeExportSuggest"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var auditId=exportAudit.find('input[name="collegeExportAuditId"]').val();
		var auditScore=exportAudit.find('input[name="collegeExportScoreSt"]').val();
		var auditName=exportAudit.find('input[name="collegeExport"]').val();
		var auditSuggest=exportAudit.find('input[name="collegeExportSuggest"]').val();
		//var pattern=/^(\d{1,2}(\.\d{1})?|100)$/;
		var pattern=/^([0-9]{1,2}|100)$/;
		var bl = pattern.test(auditScore); 
		var el = exportAudit.find('.score-error-msg');
		if(bl){
			el.hide();
		}else{
			el.text("填写0-100之间分数").show();
			return;
		}
		
		if(jsonarrayCollegeExport.length>0){
		 	for ( var i = 0; i < jsonarrayCollegeExport.length; i++){
				if(jsonarrayCollegeExport[i].auditName==auditName){
					jsonarrayCollegeExport.splice(i, 1);
				}  
		    }
		}
		var auditObject  = {
			         "auditId" : auditId,
					 "auditName" : auditName,
					 "auditSuggest" : auditSuggest,
			         "auditScore" : auditScore
			     }
		jsonarrayCollegeExport.push(auditObject);
	})
	
	
	//学院秘书审核
	$('select[name="collegeResult"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var collegeResult=exportAudit.find('select[name="collegeResult"]').val();
		var collegeSuggest=exportAudit.find('input[name="collegeSuggest"]').val();
		if(collegeResult=='1'){
			$("#schoolExportTitle").show();
			$('tr[name="schoolExport"]').show();
			$("#schoolTitle").show();
		}else if(collegeResult=='0'){
			$("#schoolExportTitle").hide();
			$('tr[name="schoolExport"]').hide();
			$("#schoolTitle").hide();
			
			$("#schoolluyanTitle").hide();
			$("#schooldefen").hide();
			
			$("#schoolEndTitle").hide();
			$("#schoolEndAudit").hide();
		}
		jsonObjectCollege["collegeResult"]=collegeResult;
		jsonObjectCollege["collegeSuggest"]=collegeSuggest;
	})
	
	//学院秘书审核
	$('input[name="collegeSuggest"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var collegeResult=exportAudit.find('select[name="collegeResult"]').val();
		var collegeSuggest=exportAudit.find('input[name="collegeSuggest"]').val();
		
		jsonObjectCollege["collegeResult"]=collegeResult;
		jsonObjectCollege["collegeSuggest"]=collegeSuggest;
	})
	
	
	//学校专家审核
	$('input[name="schoolExportScoreSt"]').on('change', function() {
		var exportAudit=$(this).parent().parent().parent();
		var auditId=exportAudit.find('input[name="schoolExportAuditId"]').val();
		var auditScore=exportAudit.find('input[name="schoolExportScoreSt"]').val();
		var auditName=exportAudit.find('input[name="schoolExport"]').val();
		var auditSuggest=exportAudit.find('input[name="schoolExportSuggest"]').val();
		//var pattern=/^(\d{1,2}(\.\d{1})?|100)$/;
		var pattern=/^([0-9]{1,2}|100)$/;
		var bl = pattern.test(auditScore); 
		var el = exportAudit.find('.score-error-msg');
		if(bl){
			schoolExportTrue=true;
			el.hide();
		}else{
			schoolExportTrue=false;
			el.text("填写0-100之间分数").show();
			return;
		}
		if(jsonarraySchoolExport.length>0){
		 	for ( var i = 0; i < jsonarraySchoolExport.length; i++){
				if(jsonarraySchoolExport[i].auditName==auditName){
					jsonarraySchoolExport.splice(i, 1);
				}  
		    }
		}
		var auditObject  = {
			         "auditId" : auditId,
					 "auditName" : auditName,
					 "auditSuggest" : auditSuggest,
			         "auditScore" : auditScore
			     }
		jsonarraySchoolExport.push(auditObject);
		changeSchoolSocre();
	})
	
	$('input[name="schoolExportSuggest"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var auditId=exportAudit.find('input[name="schoolExportAuditId"]').val();
		var auditScore=exportAudit.find('input[name="schoolExportScoreSt"]').val();
		var auditName=exportAudit.find('input[name="schoolExport"]').val();
		var auditSuggest=exportAudit.find('input[name="schoolExportSuggest"]').val();
		//var pattern=/^(\d{1,2}(\.\d{1})?|100)$/;
		var pattern=/^([0-9]{1,2}|100)$/;
		var bl = pattern.test(auditScore); 
		var el = exportAudit.find('.score-error-msg');
		if(bl){
			el.hide();
		}else{
			el.text("填写0-100之间分数").show();
			return;
		}
		if(jsonarraySchoolExport.length>0){
		 	for ( var i = 0; i < jsonarraySchoolExport.length; i++){
				if(jsonarraySchoolExport[i].auditName==auditName){
					jsonarraySchoolExport.splice(i, 1);
				}  
		    }
		}
		var auditObject  = {
			         "auditId" : auditId,
					 "auditName" : auditName,
					 "auditSuggest" : auditSuggest,
			         "auditScore" : auditScore
			     }
		jsonarraySchoolExport.push(auditObject);
	
	})
	
	//学校秘书网评审核
	$('select[name="schoolResult"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var schoolResult=exportAudit.find('select[name="schoolResult"]').val();
		var schoolSuggest=exportAudit.find('input[name="schoolSuggest"]').val();
		if(schoolResult=='1'){
			$("#schoolluyanTitle").show();
			$("#schooldefen").show();
			$("#schoolEndTitle").show();
			$("#schoolEndAudit").show();
		}else if(schoolResult=='0'){
			$("#schoolluyanTitle").hide();
			$("#schooldefen").hide();
			
			$("#schoolEndTitle").hide();
			$("#schoolEndAudit").hide();
		}
		jsonObjectSchool["schoolResult"]=schoolResult;
		jsonObjectSchool["schoolSuggest"]=schoolSuggest;
	})
	
	
	//学校秘书网评审核
	$('input[name="schoolSuggest"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var schoolResult=exportAudit.find('select[name="schoolResult"]').val();
		var schoolSuggest=exportAudit.find('input[name="schoolSuggest"]').val();
		
		jsonObjectSchool["schoolResult"]=schoolResult;
		jsonObjectSchool["schoolSuggest"]=schoolSuggest;
	})
	
	//学校秘书路演审核
	$('input[name="schoollyScore"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var schoollyScore=exportAudit.find('input[name="schoollyScore"]').val();
		var schoollySuggest=exportAudit.find('input[name="schoolluyanSug"]').val();
		//100内一位小数判断
		//var pattern=/^(\d{1,2}(\.\d{1})?|100)$/;
		var pattern=/^([0-9]{1,2}|100)$/;
		var bl = pattern.test(schoollyScore); 
		var el = exportAudit.find('.score-error-msg');
		if(bl){
			schoollyTrue=true;
			el.hide();
		}else{
			schoollyTrue=false;
			el.text("填写0-100之间分数").show();
			return;
		}
		jsonObjectSchoolly["schoollyScore"]=schoollyScore;
		jsonObjectSchoolly["schoollySuggest"]=schoollySuggest;
		changeSchoolSocre();
	})
	
	//学校秘书路演审核
	$('input[name="schoolluyanSug"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var schoollyScore=exportAudit.find('input[name="schoollyScore"]').val();
		var schoollySuggest=exportAudit.find('input[name="schoolluyanSug"]').val();
		//var pattern=/^(\d{1,2}(\.\d{1})?|100)$/;
		var pattern=/^([0-9]{1,2}|100)$/;
		var bl = pattern.test(schoollyScore); 
		var el = exportAudit.find('.score-error-msg');
		if(bl){
			//setErrorMsg();
			el.hide();
		}else{
			el.text("填写0-100之间分数").show();
			return;
		}
		jsonObjectSchoolly["schoollyScore"]=schoollyScore;
		jsonObjectSchoolly["schoollySuggest"]=schoollySuggest;
	})
	
	//学校秘书评级
	$('select[name="schoolendResult"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var schoolendResult=exportAudit.find('select[name="schoolendResult"]').val();
		var schoolendSuggest=exportAudit.find('input[name="schoolendSuggest"]').val();
		jsonObjectSchoolend["schoolendResult"]=schoolendResult;
		jsonObjectSchoolend["schoolendSuggest"]=schoolendSuggest;
	})
	
	//学校秘书评级
	$('input[name="schoolendSuggest"]').on('change', function() {
		var exportAudit=$(this).parent().parent();
		var schoolendResult=exportAudit.find('select[name="schoolendResult"]').val();
		var schoolendSuggest=exportAudit.find('input[name="schoolendSuggest"]').val();
		jsonObjectSchoolend["schoolendResult"]=schoolendResult;
		jsonObjectSchoolend["schoolendSuggest"]=schoolendSuggest;
	})
});
function changeCollegeSocre(){
	var collegeScore;
	var totalScore=0;
	var number=0;
	$('input[name="collegeExportScoreSt"]').each(function (index,element) {
		var numscore=$(element).val();
		if(numscore==""){
			numscore=0;
		}
		totalScore=totalScore+parseInt(numscore);
		number++;
	})
	collegeScore=totalScore/number;
	$("#collegeScore").val(collegeScore.toFixed(1));
	$("#collegeScore").text(collegeScore.toFixed(1));	
}

function changeSchoolSocre(){
	var collegeScore;
	var totalScore=0;
	var number=0;
	$('input[name="schoolExportScoreSt"]').each(function (index,element) {
		var numscore=$(element).val();
		
		if(numscore==""){
			numscore=0;
		}
		totalScore=totalScore+parseInt(numscore);
		number++;
	})
	collegeScore=totalScore/number;
	var luyanScore=$('#schoollyScore').val();
	if(luyanScore==""){
		luyanScore=0;
	}
	var totalScoreEnd=parseInt(collegeScore)+parseInt(luyanScore);
	var schoolScore=totalScoreEnd/2;
	$("#schoolScore").val(schoolScore.toFixed(1));
	$("#schoolScore").text(schoolScore.toFixed(1));
}


function changeForm(){
	var haveFzr = false;
	$(".zzsel").each(function (i, v) {
		if ($(v).val() == "0") {
			haveFzr = true;
			return;
		}
	});
	if (!haveFzr) {
		alertx("请选择负责人");
		return;
	}
	var jsonStr="{}";
	var jsonarray= eval('('+jsonStr+')');
	
	if(jsonarrayCollegeExport.length>0){
		jsonarray["collegeExport"]=jsonarrayCollegeExport;
	}
	if(!jQuery.isEmptyObject(jsonObjectCollege)){
		jsonarray["college"]=jsonObjectCollege;
	}
	if(jsonarraySchoolExport.length>0){
		jsonarray["schoolExport"]=jsonarraySchoolExport;
	}
	if(!jQuery.isEmptyObject(jsonObjectSchool)){
		jsonarray["school"]=jsonObjectSchool;
	}
	if(!jQuery.isEmptyObject(jsonObjectSchoolly)){
		jsonarray["schoolly"]=jsonObjectSchoolly;
	}
	if(!jQuery.isEmptyObject(jsonObjectSchoolend)){
		jsonarray["schoolend"]=jsonObjectSchoolend;
	}
	$("#changeData").val(JSON.stringify(jsonarray));
	if(collegeExportTrue && schoolExportTrue && schoollyTrue){
		if(validate1.form()) {
			$("#changeForm").ajaxSubmit(function (data) {
				showTip(data.msg, data.ret == 1 ? 'success' : 'warning');
			})
		}
		
		/*$("#changeForm").submit();*/
	}
}

