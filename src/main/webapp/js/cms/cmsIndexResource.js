$(function(){
	DSSB.initDSSB();

});
var prePath = $('#fileToUpload').val();  
var fileUp = false;  
 
var DSSB = {
	plansIndex:0,
	initDSSB : function(){
	//	DSSB.addEventToBtn();
	//	DSSB.printOut();
	//	DSSB.plansIndex=$(".task tbody tr").size()-1;
		$("#pId").on('change', function() { 	
			var options=$("#pId option:selected");
		    var opt=options.text(); 
		    $("#projectName").val(opt);
		});
		
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
			if (prePath == curPath) { 
				fileUp=false;
		    	showModalMessage(0, "不能上传相同文件名");
		        return false;  
		    } 
		    prePath = curPath;  
		    
			var ftpIds=$("input[name='ftpId']");
			var ftpId="";
			if(ftpIds.length>0){
				ftpId=ftpIds[0].value;
			}
			$.ajaxFileUpload({
				url:'/ftp/urlLoad/file',
				secureuri:false,
				fileElementId:'fileToUpload',//file标签的id
				dataType: 'json',//返回数据的类型
				data:{type:'cmsIndexResource',
					ftpId:ftpId,
					name:'txt'
				},//一同上传的数据
				success: function (data, status) {
					//把图片替换
					fileUp=false;
					var obj = jQuery.parseJSON(data); 
					var state=obj.state;
					if(state=="1"){
						//针对谷歌浏览器会记住上传文件名导致不触发onchange方法
						$("#fileToUpload").val("");
						var $btn="<span class='del' id='del');'>" +
						
								/*"<span class='del' onclick='DSSB.delFile(this,null,\"" + obj.arrUrl + "\");'>" +*/
								"<input type='hidden'  name='arrUrl' value='"+obj.arrUrl+"'/>" +
								"<input type='hidden'  name='ftpId' value='"+obj.ftpId+"'/>" +
								"<input type='hidden' name='arrName' value='"+obj.fileName+"'/></div>" +
								"<i class='icon-remove-sign'></i></span>";
						var extname = obj.fileName.split('.').pop().toLowerCase();
						switch(extname) {
							case "xls":
							case "xlsx":
								extname = "excel";
								break;
							case "doc":
							case "docx":
								extname = "word";
								break;
							case "ppt":
							case "pptx":
								extname = "ppt";
								break;
							// 我不太确定这个文件格式
//                    case "project":
							case "jpg":
							case "jpeg":
							case "gif":
							case "png":
							case "bmp":
								extname = "image";
								break;
							case "rar":
							case "zip":
							case "txt":
							case "project":
								// just break
								break;
							default:
								extname = "unknow";
						}
				       $('#file').append('<li><p><img src="' + ctxImg + '/filetype/' + extname + '.png"/>' + obj.fileName + $btn +'</p></li>');
					 //	var $btn=$("<button type='button' id='del'>删除</button>");
				       //$('#fujian').append('<li><p>'+html+'</p></li>').children().last().append($btn);
				       $("#url").val(obj.arrUrl);
					}else{
						showModalMessage(0, obj.msg);
					}
				},
				error: function (data, status, e) {
					fileUp=false;
					showModalMessage(0, e);
				}
			});
			
			
			$('body').delegate('#del','click',function(){
		        //var arrurl=$(this).parent().find(name='arrUrl');
				var remove=$(this).parent();
		        var arrurl=$(this).parent().find("input[name='arrUrl']").val();
		        //alert(arrurl);
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
					url:"/a/cms/cmsIndexResource/delload",
					dataType:'json',
					data:{fileName:arrurl},
					success:function(data){
						var state = data.state;
						if(state=="2"){
							alert(obj.msg);
						}else{
							alert("操作异常");
						}
					}
				})
				$(this).parent().remove();
			})

	}



}

