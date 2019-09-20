var prePath = $('#fileToUpload').val();  
var fileUp = false; 
$(function(){
		$("#upload").on('click', function() {
			$('#fileToUpload').click();
		}); 
		//选择文件之后执行上传  
		$('#fileToUpload').on('change', function() {
			if(fileUp){
				return false;  
			}
			fileUp=true;
			var curPath = $('#fileToUpload').val();  
		    prePath = curPath;  
		    
			$.ajaxFileUpload({
                    url:'/a/authorize/uploadLicense',
                    secureuri:false,
                    fileElementId:'fileToUpload',//file标签的id
                    dataType: 'json',//返回数据的类型
                    data:{},//一同上传的数据
                    success: function (data, status) {
                        //把图片替换
                        var obj = jQuery.parseJSON(data); 
                        if(obj.ret=="1"){
                            fileUp=false;
                            $("#exp").html(obj.exp);
                            showModalMessage(1, obj.msg,{
                                首页: function() {
                                	$( this ).dialog( "close" );
                                    location = "/a";
                                },
                                返回: function() {
                                    $( this ).dialog( "close" );
                                }
                            });
                        }else{
                            $("#fileToUpload").val("");
                            fileUp=false;
                            showModalMessage(0, obj.msg);
                        }
                    },
				error: function (data, status, e) {
					fileUp=false;
					showModalMessage(0, e);
				}
			});
		});
});