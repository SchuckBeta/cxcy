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
        data:{type:'gcsb',ftpId:ftpId},//一同上传的数据
        success: function (data, status) {
            //把图片替换
            var obj = jQuery.parseJSON(data);
            var state=obj.state;
            fileUp=false;
           if(state=="1"){
                //针对谷歌浏览器会记住上传文件名导致不触发onchange方法
                $("#fileToUpload").val("");
                fileUp=false;

                var $btn="<span class='del' onclick='delFile(this);'>" +
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
                $('#file').append('<p><img src="' + ctxImg + '/filetype/' + extname + '.png" />'
                                    + '<a href="javascript:void(0)" onclick="downFile(\''+obj.arrUrl+'\')"> '+obj.fileName+'</a>'
                                + $btn +  '</p>');
            }else{
               alert(obj.msg);
            }
        },
        error: function (data, status, e) {
            fileUp=false;
            alert(e);
        }
    });
});
function delFile(ob){
      $(ob).parent().remove();
}

function downFile(url,fileName){
    location.href="' + ctx + '/loadUrl?url="+url+"&fileName="+encodeURI(encodeURI(fileName));
}