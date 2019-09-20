$("#upload").on('click', function() {;
    $('#fileToUpload').click();
});

var prePath = $('#fileToUpload').val();
var fileUp = false;

$('#fileToUpload').on('change', function() {

    if(fileUp){
        return false;
    }
    fileUp=true;
    var curPath = $('#fileToUpload').val();
    if (prePath == curPath) {
        fileUp=false;
        alert("不能上传相同文件名");
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
        data:{type:'proMidForm',ftpId:ftpId},//一同上传的数据
        success: function (data, status) {
            //把图片替换
            var obj = jQuery.parseJSON(data);
            var state=obj.state;
            if(state=="3"){
                fileUp=false;
                alert("上传文件名相同");
            }else if(state=="2"){
                fileUp=false;
                alert("上传失败");
            }else{
                //针对谷歌浏览器会记住上传文件名导致不触发onchange方法
                $("#fileToUpload").val("");
                fileUp=false;

                var $btn="<span class='del' onclick='delFile(this,null,\""+obj.arrUrl+"\");'>" +
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
            }
        },
        error: function (data, status, e) {
            fileUp=false;
            alert(e);
        }
    });
});
function delFile(ob,id,url){
    $.ajax({
        type: "GET",
        url: "/f/project/projectDeclare/delFile",
        data: "id="+id+"&arrUrl="+encodeURIComponent(url),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if(data.ret==1){
                prePath="";
                $(ob).parent().remove();
            }
        },
        error: function (msg) {
            alert(msg);
        }
    });
}

function downFile(url,fileName){
    location.href="/ftp/loadUrl?url="+url+"&fileName="+encodeURI(encodeURI(fileName));
}