$(function(){
    //收起展开触发器
    var btnList=["commonToggle","standardToggle","teamToggle","projectToggle","resultToggle","budgetToggle","uploadFile","checkRecord","lastCheckToggle"];
    btnList.forEach(function(item){
        $('#'+item).click(function(evt){
            evt.stopPropagation();
            // var btn = $(this);
            // var body = btn.closest('.row').children('.toggle_wrap');
            // if(body.is(':visible')) {
            //     btn.removeClass()
            // }
            var flag=$(this).attr('data-flag');
            if(flag=='true'){
                $('#'+item+'_'+'wrap').hide();
                $(this).attr('data-flag',false);
                $(this).children('span').attr('class','icon-double-angle-down');
            }else{
                $('#'+item+'_'+'wrap').show();
                $(this).attr('data-flag',true);
                $(this).children('span').attr('class','icon-double-angle-up');
            }
        })
    })
})