$(document).ready(function() {
	getInfo();
	$("#ps").val($("#pageSize").val());
});
function goback(){
	location.href=$("#referrer").val();
}
function page(n, s) {
	$("#pageNo").val(n);
	$("#pageSize").val(s);
	$("#searchForm").submit();
	return false;
}
function getInfo(){
	$("[uncomplete]").each(function(){
		var id=$(this).attr("uncomplete");
		getInfoSub(id);
		eval("it"+id+"=setInterval(function(){getInfoSub(id);},2000);");
	});
}
function getInfoSub(id){
	$.ajax({
        type: "GET",
        url: "/a/certMake/getCertMakeInfo",
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
            		$("tr[uncomplete='"+id+"']").children('td').eq(5).html("下发完毕");
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
function certMake(ob){
	var actywId=$("#actywId").val();
	var certid=$("#certid").val();
	if(certid==""){
		alertx("请选择要下发的证书");
		return false;
	}
	var obfn=$(ob).attr("onclick");
	$(ob).attr("onclick","");
	$.ajax({
        type: "POST",
        url: "/a/certMake/doCertMake",
        data: {actywId:actywId,certid:certid},
        dataType: "json",
        success: function (data) {
        	if(data.ret=="1"){
        		page(1,10);
        	}else{
        		$(ob).attr("onclick",obfn);
        		alertx(data.msg);
        	}
        }
	});
}