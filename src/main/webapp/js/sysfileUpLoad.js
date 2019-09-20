var prePath = $('#fileToUpload').val();
var fileUp = false;

function fileDownUp(type,delFile){
	$("#upload").on('click', function() {
	    $('#fileToUpload').click();
	});

	$('#fileToUpload').on('change', function() {
		
		var f = document.getElementById("fileToUpload").files;  
		if(f[0].size>10485760){
			 showModalMessage(0, "文件大小超过不能超过10M");
			 return;
		}
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
	        //url:'/ftp/urlLoad/file',
	       
	        url:'/ftp/urlLoad/file',
	        secureuri:false,
	        fileElementId:'fileToUpload',//file标签的id
	        dataType: 'json',//返回数据的类型
	        data:{type:type,ftpId:ftpId},//一同上传的数据
	        success: function (data, status) {
	            //把图片替换
	        	
	        	fileUp=false;
	            var obj = jQuery.parseJSON(data);
	            var state=obj.state;
	            if(state=="1"){
	                //针对谷歌浏览器会记住上传文件名导致不触发onchange方法
	                $("#fileToUpload").val("");
	                var $btn="<span class='del' onclick='"+delFile+"(this,null,\""+obj.arrUrl+"\");'>" +
	                    "<input type='hidden'  name='arrUrl' value='"+obj.arrUrl+"'/>" +
	                    "<input type='hidden'  name='ftpId' value='"+obj.ftpId+"'/>" +
	                    "<input type='hidden' name='arrName' value='"+obj.fileName+"'/>" +
	                    "<i class='icon-remove-sign'></i>" +
	                    "</span>";
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
	           
	                $('#file').append('<li><p><img src="' + ctxImg + '/filetype/' + extname + '.png"/>' + obj.fileName + $btn +  '</p></li>');  
	            }else{
	            	showModalMessage(0, obj.msg);
	            }
	        },
	        error: function (data, status, e) {
	            fileUp=false;
	            alert(e);
	        }
	    });
	});
	
}

function delFile(ob,id,url){
    $.ajax({
        type: "GET",
        url: "/ftp/delload",
        data: "id="+id+"&fileName="+encodeURIComponent(url),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if(data.state==1){
            	prePath="";
                $(ob).parent().remove();
            }else{
            	showModalMessage(0, data.msg);
            }
        },
        error: function (msg) {
            alert(msg);
        }
    });
}

function delBackFile(ob,id,url){
    $.ajax({
        type: "GET",
        url: "/ftp/delload",
        data: "id="+id+"&fileName="+encodeURIComponent(url),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if(data.state==1){
            	prePath="";
                $(ob).parent().remove();
            }else{
            	showModalMessage(0, data.msg);
            }
        },
        error: function (msg) {
            alert(msg);
        }
    });
}

function delUrlFile(ob,id,url,delFileUrl){
    $.ajax({
        type: "GET",
        url: delFileUrl,
        data: "id="+id+"&arrUrl="+encodeURIComponent(url),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if(data.ret==1){
            	prePath="";
                $(ob).parent().remove();
            }else{
            	showModalMessage(0, data.msg);
            }
        },
        error: function (msg) {
            alert(msg);
        }
    });
}
function downfile(url,fileName){
    location.href="/ftp/loadUrl?url="+url+"&fileName="+encodeURI(encodeURI(fileName));
}
function myBrowser(){
	var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
	var isOpera = userAgent.indexOf("Opera") > -1;
    if (isOpera) {
        return "Opera"
    }; //判断是否Opera浏览器
    if (userAgent.indexOf("Firefox") > -1) {
        return "FF";
    } //判断是否Firefox浏览器
    if (userAgent.indexOf("Chrome") > -1){
    	return "Chrome";
    }
    if (userAgent.indexOf("Safari") > -1) {
        return "Safari";
    } //判断是否Safari浏览器
    if (userAgent.indexOf("compatible") > -1 && userAgent.indexOf("MSIE") > -1 && !isOpera) {
        return "IE";
    }//判断是否IE浏览器
    if((userAgent.indexOf("Gecko")>0 && userAgent.indexOf("rv:11")>0)){
    	return "IE";
    }//判断是否IE浏览器
    ; //判断是否IE浏览器
}

