$(document).ready(function(){
    $('#first_check').on('change',function(){
        $('.check').prop('checked',this.checked?true:false);
    })
});