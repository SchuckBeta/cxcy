/**
 * Created by Administrator on 2017/8/25.
 */
var mobileRegExp = /^0?(13[0-9]|15[012356789]|18[0-9]|17[0-9])[0-9]{8}$/;
var emailRegExp = /^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\.[a-zA-Z0-9_-]+)+$/;
var numberLetterExp =  /^[a-zA-Z0-9]+$/ ; // 数字和字母正则
var IDCardExp = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;  //身份证校验


function showModalMessage(ret, msg, buttons){
    var html=null;
    //ret 3种状态，0表示操作失败，1表示操作成功，2表示操作出现警告
    switch(parseInt(ret)){
        case 0 : html=createFailMessage(msg);
            break;
        case 1 : html=createOkMessage(msg);
            break;
        case 2 : html=warningMessage(msg);
            break;
        default: break;

    }
    $( "#dialog-content" ).html(html);
    $( "#dialog-message" ).dialog({
        modal: true,
        buttons: buttons || {
            确定: function() {
                $( this ).dialog( "close" );
            }
        }
    });
}
//正确的情况
function createOkMessage(msg){
    return '<i class="icon-ok-msg"></i><span>' + msg + '</span>';
}
//失败的情况
function createFailMessage(msg){
    return '<i class="icon-fail-msg"></i><span>' + msg + '</span>';
}
//警告的状态
function warningMessage(msg){
    return '<i class="icon-warning-msg"></i><span>' + msg + '</span>';
}
//回到视野中央
function showCenter(){
    $(window).scrollTop($('.table').offset().top);
}

/**
 * 访问FTP资源文件路径
 **/
function ftpHttpUrlByIp(ftpIp, curUrl){
    if((ftpIp == null) || (ftpIp == "")){
        console.info("Waring: ftpIp is required!");
        return "";
    }
    curUrl = curUrl.replace("/tool", "");
    return ftpIp+curUrl;
}

$(function () {
    //分页去除无用class
    $('.pagination_num').removeClass('row');


    //通用分页函数调用


});





