$(function () {
    var $btnUpload = $('#btnUpload');
    var $fujianContent = $("#fujianpp");
    var $accessoryListPdf = $('#accessoryListPdf');

    var $downFileUl = $('#downFileUl');
    var wtypes = window.wtypes;

    $btnUpload.uploadAccessory({
        fileNumLimit: 1,
        hasFiles: false,
        isImagePreview: false,
        multiple: false
    });

//文件上传相关


    $btnUpload.on('uploadSuccess', function (e) {
        var uploader = e.uploader;
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielSize' value='" + e.response.size + "'/>");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielTitle' value='" + e.response.title + "'/>");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielType' value='" + e.response.type + "'/>");
        $fujianContent.append("<input type='hidden' name='proModel.attachMentEntity.fielFtpUrl' value='" + e.response.ftpUrl + "'/>");
        uploader.reset();
    });

//删除之前
    $btnUpload.on('deleteFileBefore', function (e) {
        $accessoryListPdf.find('li').hide();
    });

    //删除完成
    $btnUpload.on('deleteFileComplete', function () {
        // showModalMessage(0, '资料删除成功');
        $fujianContent.empty();
    });
    //下载申报资料
   $accessoryListPdf.on('click', 'a', function (e) {
       e.preventDefault();
       var url = $(this).attr("data-ftp-url");
       var fileName = $(this).attr("data-title");
       location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
   });
    //设置下载文档地址
    function setFileDocUrl() {
        var name, href, type;
        var wprefix = window.wprefix;
        var id;
        $.each(JSON.parse(wtypes), function (i, item) {
            var tpl = item.tpl;
            if ((item.type.key == $('#proCate').val()) && (item.tpl.key == "2")) {
                name = item.name;
                type = item.key;
                return false;
            }
        });
        href = '/f/proprojectmd/proModelMd/ajaxWord?proId=' + $('#proId').val() + '&type=' + type;
        $downFileUl.find('a').attr('href', href).text(wprefix + name);
    }

    setFileDocUrl()

});



function checkIsToLogin(data) {
     return typeof data == 'string' && data.indexOf("id=\"imFrontLoginPage\"") > -1;
 }
function dialogTimeout() {
    showModalMessage(0, '未登录或登录超时。请重新登录，谢谢！', [{
        text: '确定',
        click: function () {
            location.reload();
        }
    }]);
}

function closeSubmit(obj){
    $(obj).prop('disabled', true);
    $("#competitionfm").ajaxSubmit(function (data) {
        isLogin = checkIsToLogin(data);
        if (isLogin) {
            dialogTimeout();
        } else {
            showModalMessage(data.ret, data.msg);
        }
        $(obj).prop('disabled', false);
    })
}
