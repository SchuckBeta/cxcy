/**
 * Created by Administrator on 2019/4/19.
 */


//topbar 获取未读消息个数

function getUnReadMessage() {
    var $notification_count = $('#appHeader .notification_count');
    axios.get('/oa/oaNotify/getUnreadCount').then(function (data) {
        if(data && data.count){
            $notification_count.show().text('（'+data.count+'）');
        }else {
            $notification_count.hide();
        }
    })
}

$(function () {
    var isUserLogin_ed =window.loginCurUser && !!window.loginCurUser.id;
    if(isUserLogin_ed){
        getUnReadMessage()
    }
})