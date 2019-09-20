$(function(){
    
    $('.person-info-details input').addClass("noInputBorder").attr('readonly',true);
    $('.person-info-details select').addClass("noSelectBorder").attr('disabled','disabled');

    //点击按钮后，才可以编辑个人信息和在校情况
    $('#edit-private').click(function(){
        $('.person-info-details input').removeClass("noInputBorder");
        $('.person-info-details  select').removeClass("noSelectBorder");
        $('.person-info-details  input').removeAttr('readonly');
        $('.person-info-details  select').removeAttr('disabled');
        $('.person-info-details input,.person-info-details select').addClass('isEdit');
        $("#officeName").attr('readonly',true);
        //时间控件可点击
        $(".isTime").click(function () {
            WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});
        })
    });

    $('#edit-school').click(function(){
    	editZxqk();
    });

    /**
     * 修改在校情况
     * @returns
     */
    function editZxqk(){
    	 $('.school-info input').addClass('isEdit');
         $('.school-info input:not(.only)').removeAttr('readonly');
         $('.tec-area').removeAttr('readonly');
         $('.tec-area').addClass('isEdit');
         $('.school-info select').removeAttr('disabled');
    }

});