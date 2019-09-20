$(function(){
	// var oHeight=$('.list_wrap li').eq(0).height();
	// var len=$('.list_wrap').children().length;
	// var oScreen=document.documentElement.clientHeight||document.body.clientHeight;
	//设置一级容器高度
	// $('.list_wrap').height(oScreen-93);
	// var subHeight=$('.sub_list_wrap li').eq(0).height();
	// var subLen=$('.sub_list_wrap').children().length;

	//二级容器高度
	// $('.sub_list_wrap').height(subHeight*subLen);

	//三级点击切换事件
	$('.grand_sub_wrap li').on('click',function(){
		$(this).addClass('current').siblings().removeClass('current');

		$(this).parents('.level_two').siblings().find('li').removeClass('current');
	});

	//二级切换事件
	$('.level_two').on('click',function(){
		$(this).children('a').addClass('active').parent().siblings().children('a').removeClass('active');
		$(this).siblings().find('li').removeClass('current');
		/*$(this).children('.grand_sub_wrap').find('li').eq(0).addClass('current');
		console.log($(this).children('.grand_sub_wrap').find('li').eq(0));*/
	});
})