/**
 * 解决浏览器回退和前进左边菜单树焦点不正确的问题
 * 在所有的二级菜单引入此js即可
 * <script src="${ctxJs}/goback.js" type="text/javascript"></script>
 */
$(document).ready(function(){

    var url = location.href;
    var $parent = $(window.parent.document);
    if(typeof $parent == "undefined" || $parent == null || $parent == ""){
        return;
    }
    var $listWrap = $parent.find('#list_wrap');
    if(typeof $listWrap == "undefined" || $listWrap == null || $listWrap == ""){
        return;
    }
    var $menuList = $parent.find('#list_wrap .level_three');
    $menuList.each(function (i, item) {
        var href = $(item).find('a').attr('href');
        if(url.indexOf(href) > -1){
            $listWrap.find('li').removeClass('current').find('a').removeClass('active');
            $(item).addClass('current');
            $(item).parents('.level_two').find('>a').addClass('active');
            return false;
        }
    })
});