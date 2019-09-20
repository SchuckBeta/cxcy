$(function(){

    //默认查看详情，个人信息不可编辑
    $('.private-info .row-line input').attr('readonly',true);

    $('.school-info input').attr('readonly',true);

    $('.tec-area').attr('readonly',true);

    //点击按钮后，才可以编辑个人信息和在校情况
    $('#edit-private').click(function(){
        $('.private-info .row-line input').addClass('isEdit');
        $('.private-info .row-line input').removeAttr('readonly');
    });

    $('#edit-school').click(function(){
        $('.school-info input').addClass('isEdit');
        $('.school-info input').removeAttr('readonly');
        $('.tec-area').removeAttr('readonly');
        $('.tec-area').addClass('isEdit')
    })
});