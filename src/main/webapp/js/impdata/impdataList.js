$(document).ready(function() {
	getImpInfo();
	$("#ps").val($("#pageSize").val());
	$("#import").on('click', function() {
	    $('#fileToUpload').click();
	});

	$('#fileToUpload').on('change', function() {
	    $.ajaxFileUpload({
	        url:'/a/impdata/importData',
	        secureuri:false,
	        fileElementId:'fileToUpload',//file标签的id
	        dataType: 'json',//返回数据的类型
	        data:{},//一同上传的数据
	        success: function (data, status) {
	            var obj = jQuery.parseJSON(data);
	            if(obj.ret=="1"){
	            	page(1,10);
	            }else{
	            	alertx(obj.msg);
	            }
	        },
	        error: function (data, status, e) {
	        }
	    });
	});
});
function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}
function downTemplate(){
	if(!$("#tempType").val()){
		alertx("请选择模板类型");
	}else{
		location.href="' + ctx + '/impdata/downTemplate?type="+$("#tempType").val();
	}
}
function getImpInfo(){
	$("[uncomplete]").each(function(){
		var id=$(this).attr("uncomplete");
		getImpInfoSub(id);
		eval("it"+id+"=setInterval(function(){getImpInfoSub(id);},2000);");
	});
}
function getImpInfoSub(id){
	$.ajax({
        type: "GET",
        url: "/a/impdata/getImpInfo",
        data: "id="+id,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if(data){
            	$("tr[uncomplete='"+id+"']").children('td').eq(3).html(data.success);
            	$("tr[uncomplete='"+id+"']").children('td').eq(4).html(data.fail);
            	if(data.isComplete=='1'){
            		clearInterval(eval("it"+id));
            		$("tr[uncomplete='"+id+"']").children('td').eq(2).html(data.total);
            		$("tr[uncomplete='"+id+"']").children('td').eq(5).html("导入完毕");
            		$("tr[uncomplete='"+id+"']").find(".isComplete").attr("style","display:");
            	}
            }else{
            	clearInterval(eval("it"+id));
            }
        },
        error: function (msg) {
        	clearInterval(eval("it"+id));
        }
    });
}