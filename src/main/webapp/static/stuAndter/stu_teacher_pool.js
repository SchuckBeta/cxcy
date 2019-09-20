$(function(){

    //未点击编辑按钮时导师基本信息是不可编辑的
    $('.tea-details input').attr('readonly',true);

    $('.team-textarea').attr('readonly','readonly');

    //导师详情展开收起
    $('#unfold').click(function(){
        var flag=$(this).attr('data-flag');
        if(flag=='true'){
           $('#private-info').hide();
           $(this).html('展开');
            $(this).children('span').attr('class','icon-double-angle-down')
           $(this).attr('data-flag',false);
        }else{
           $('#private-info').show();
           $(this).html('收起');
           $(this).children('span').attr('class','icon-double-angle-up')
           $(this).attr('data-flag',true);
        }
    });

    /**
     * [description 点击编辑按钮，导师基本信息可编辑]
     */
    $('#edit-tecacher').click(function(){
        $(this).hide();
        $(this).siblings('#save-tecacher').show();
        $('.tea-details input').addClass('isEdit').attr('readonly',false);
        $('.team-textarea').removeAttr('readonly');
        $('.team-textarea').addClass('isEdit');
    });


    /**
     * [description 点击编辑按钮，导师详细信息可编辑]
     */
    $('#private-desc').click(function(){
      $('.team-textarea').addClass('isEdit').removeAttr('readonly');
    })

})