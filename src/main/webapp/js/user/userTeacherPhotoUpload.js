$(function(){
	DSSB.initDSSB();

});
var prePath = $('#fileToUpload').val();  
var fileUp = false;  
var DSSB = {
	plansIndex:0,
	initDSSB : function(){
		$("#upload").on('click', function() {
	    $('#fileToUpload').click();
	});

	$('#fileToUpload').on('change', function() {
	    if(fileUp){
	        return false;
	    }
	    fileUp=true;
	    var curPath = $('#fileToUpload').val();
	    var imgtype = (curPath
				.substr(curPath
						.lastIndexOf(".")))
				.toLowerCase();
		if (imgtype != ".jpg"
				&& imgtype != ".gif"
				&& imgtype != ".bmp"
				&& imgtype != ".png") {
			//alert("您上传图片的类型不符合(jpg|jpeg|gif|png)！");
			showModalMessage(0, "您上传图片的类型不符合(jpg|bmp|gif|png)！");
			return false;
		}
	    if (prePath == curPath) {
	        fileUp=false;
	        showModalMessage(0, "不能上传相同文件名");
	        return false;
	    }
	    var mb = myBrowser();
	    if ("IE" == mb) {
	    	curPath=curPath.substring(curPath.lastIndexOf('\\')+1);
	    }
	    var arrNames=$("input[name='arrName']");
	    if(arrNames.length>0){
	    	for(var i=0;i<arrNames.length;i++){
	    		 if (arrNames[i].value == curPath) {
	    			 fileUp=false;
	    			 showModalMessage(0, "不能上传相同文件名");
	    			 return false;
	    		 }
    		}
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
				data:{type:'teacherExpansion',
					ftpId:ftpId,
					name:'txt'
				},//一同上传的数据
				success: function (data, status) {
					//把图片替换
					fileUp=false;
					var obj = jQuery.parseJSON(data); 
					var state=obj.state;
					$.ajax({
						url:"/ftp/uploadPhoto",
						data:{"arrUrl":obj.arrUrl,
								"id": $("#userid").val()
						}
					});
					if(state=="1"){
						$("#fileToUpload").val("");
						var $btn="<span class='del' id='del');'>" +
						"<input type='hidden'  name='arrUrl' value='"+obj.arrUrl+"'/>" +
						"<input type='hidden'  name='ftpId' value='"+obj.ftpId+"'/>" +
						"<input type='hidden' name='arrName' value='"+obj.fileName+"'/></div></span>"; 
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
						var path="/ftp/download?fileName="+encodeURIComponent(obj.arrUrl);
						$("#fileId").attr('src',path);
						 //	var $btn=$("<button type='button' id='del'>删除</button>");
					       //$('#fujian').append('<li><p>'+html+'</p></li>').children().last().append($btn);
				        $("#files").append($btn);
					}else{
						alert(obj.msg);
					}
				},
				error: function (data, status, e) {
					fileUp=false;
					alert(e);
				}
			});
			
			
			
			$('body').delegate('#del','click',function(){
		        //var arrurl=$(this).parent().find(name='arrUrl');
				var remove=$(this).parent();
		        var arrurl=$(this).parent().find("input[name='arrUrl']").val();
				$.ajax({ url: "/ftp/delload", 
					dataType: 'json',
					data:{fileName:arrurl},
					success: function(data){
						//var obj = jQuery.parseJSON(data); 
						prePath='';
		    		   	var state=data.state;
						if(state=="2"){
							
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
					url:"/a/project/projectAnnounce/delload",
					dataType:'json',
					data:{fileName:arrurl},
					success:function(data){
						var state = data.state;
						if(state=="2"){
							
						}else{
							
						}
					}
				})
				$(this).parent().remove();
			})

	}
}

