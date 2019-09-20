$(function () {
    var $accessoryListPdf = $('#accessoryListPdf');
    //下载申报资料
   $accessoryListPdf.on('click', 'a', function (e) {
       e.preventDefault();
       var url = $(this).attr("data-ftp-url");
       var fileName = $(this).attr("data-title");
       location.href = $frontOrAdmin+"/ftp/ueditorUpload/downFile?url=" + url + "&fileName=" + encodeURI(encodeURI(fileName));
   });
});
