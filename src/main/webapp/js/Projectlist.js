$(document).ready(function(){
    $('.btn').on('click',function(){
        if($('.auditRecord').is(":visible"))return;
        $('.auditRecord').css({
            'margin-top':-($('.auditRecord').height()/2)
        }).show();
    });
    $('.return').on('click',function(){
       $('.auditRecord').hide(); 
    })
});
function delPro(id,ftb){
	showModalMessage(0, '确认要删除该项目申报吗？',{
		确定: function() {
			top.location = "/f/project/projectDeclare/delete?id="+id+"&ftb="+ftb;
		},
		取消: function() {
			$( this ).dialog( "close" );
		}
	});
}