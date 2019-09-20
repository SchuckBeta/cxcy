var toggle={
    show:function(){
        $('.Show').on('click',function(){
            $(this).parent().parent().next().show(400);
        })
    },
    hide:function(){
        $('.Hide').on('click',function(){
           $(this).parent().parent().next().hide(400);
        })
    }
}