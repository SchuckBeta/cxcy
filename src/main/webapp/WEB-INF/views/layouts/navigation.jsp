<%@ page contentType="text/html;charset=UTF-8" %>
<div class="edit-bar edit-bar-tag edit-bar_new clearfix">
    <div class="edit-bar-left">
        <span></span>
        <i class="line weight-line"></i>
    </div>
</div>

<script>
    $(function () {
        var $osSideNav = $(window.parent.document.getElementById('oeSideNav'));
        var $currentSenMenu = $osSideNav.find('.submenu-title.active')
        var $currentTMenu = $osSideNav.find('.oe-menu-item.active')
        var tagText;
        var menuName;
        var secondName=$("#secondName").val();

        if($currentTMenu.size() > 0){
            tagText = $currentTMenu.find('.unread-tag').text() || '';
            menuName = $currentTMenu.text();
        }else {
            tagText = $currentSenMenu.find('.unread-tag').text() || '';
            menuName = $currentSenMenu.text();
        }
        menuName = menuName.replace(tagText, '');
        if(secondName!=null && secondName!=""){
            menuName=menuName+"- "+secondName;
        }
        $('.edit-bar_new span').text(menuName);
//        var menuName= $(parent.frames["menu"].document).find("li.oe-menu-item").find("active").html();

//        $("#oeSideNav").find(".active").each(function(index){
//            alert(this.html());
//        })
        //alert(menuName);
        //var menuName=data.secondName;
//        $(".edit-bar_new span").html(menuName);
//        var href = window.location.pathname;
//        var param = window.location.search;
//        var url="${ctx}/sys/menu/navigation";
//        $.ajax({
//            type : "GET",
//            url : url,
//            dataType: "JSON",
//            data : {
//                "href" : href+param,
//            },
//            success: function (data) {
//                if(data.ret == "1"){
//
//                }
//
//            }
//         });
    })
</script>