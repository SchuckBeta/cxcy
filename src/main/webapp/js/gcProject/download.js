/**
 * Created by zhangzheng on 2017/3/28.
 */
function downloadFile(id,flag){
    var url;
    $.ajax({
        type: "post",
        url: "/a/projectBase/getDownloadUrl",
        data: {"id":id,"flag":flag},
        async : false,
        success: function (data) {
            url=data;
        }
    });
    location.href="' + ctx + '/download?fileName="+encodeURIComponent(url);
}