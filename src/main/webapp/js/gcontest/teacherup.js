$(function(){
	DSSB.initDSSB();

});
var DSSB = {
	plansIndex:0,
	initDSSB : function(){
		$("#upload").on('click', function() {  
		    $('#fileToUpload').click(); 
		    
		});  
		//选择文件之后执行上传  
		$('#fileToUpload').on('change', function() { 
			$.ajaxFileUpload({
				url:'/ftp/urlLoad/file',
				secureuri:false,
				fileElementId:'fileToUpload',//file标签的id
				dataType: 'json',//返回数据的类型
				data:{type:'rencaiku'
				},//一同上传的数据
				success: function (data, status) {
					//把图片替换
					var obj = jQuery.parseJSON(data); 
					var state=obj.state;
					if(state=="3"){
						alert(obj.msg);
					}else if(state=="2"){
						alert(obj.msg);
					}else{
						var html="<p>" +obj.fileName+"</p>"
						+"<input type='hidden'  name='arrUrl' value='"+obj.arrUrl+"'/>"
						+"<input type='hidden' name='arrName' value='"+obj.fileName+"'/>";
					 	//$("#fujian").append("<li><p>'+HTML+'</p></li>'");
						//UpdateLoade.change(html);
					 	
					 	var $btn=$("<button type='button' id='del'>删除</button>");
				       $('#fujian').append('<li><p>'+html+'</p></li>').children().last().append($btn);
				        
					}
				},
				error: function (data, status, e) {
					alert(e);
				}
			});
			
			
			
			$('body').delegate('#del','click',function(){
		        //var arrurl=$(this).parent().find(name='arrUrl');
				var remove=$(this).parent();
		        var arrurl=$(this).parent().find("input[name='arrUrl']").val();
		        alert(arrurl);
				$.ajax({ url: "/ftp/delload", 
					dataType: 'json',
					data:{fileName:arrurl},
					success: function(data){
						//var obj = jQuery.parseJSON(data); 
		    		   	var state=data.state;
						if(state=="2"){
							alert(obj.msg);
						}else {
							//remove.remove();
						}
		        	}
				});
		        $(this).parent().remove();
		       // location.href="/ftp/delload?fileName="+encodeURIComponent(arrurl);
		      
		   })
		})
			$('.del').click(function(){
				var arrurl = $("#url").val();
				$.ajax({
					url:"/a/project/rencaiku",
					dataType:'json',
					data:{fileName:arrurl},
					success:function(data){
						var state = data.state;
						if(state=="2"){
							alert(obj.msg);
						}else{
							alert(111);
						}
					}
				})
				$(this).parent().remove();
			})

	}
}

