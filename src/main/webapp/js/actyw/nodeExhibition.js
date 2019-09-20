;(function(window,$){
	$(function(){
		console.log(window['exhibition'])

		var $flowWorks = $('.flow-work');
		var winWidth = $(window).width();
		var $fwItem = $flowWorks.find('.fw-item').eq(0);
		var singleWidth = $fwItem.width() + parseInt($fwItem.css('margin-right'));
		var minNum = Math.floor(winWidth/singleWidth);
		$flowWorks.each(function(){
			var $flowLevelOnes = $(this).find('.fw-level-one');
			$flowLevelOnes.each(function(){
				if($(this).next().hasClass('fw-level-two')){
					var oneChildren = $(this).children();
					var num;
					var width;
					var size = oneChildren.size();
					if(oneChildren.size() > minNum){
						num = minNum
					}else{
						num = oneChildren.size()
					}
					var width = oneChildren.width() * num;
					$(this).parents('.flow-work').width(width+88);
					$(this).width(width+ 88*(num-1))
				}
			})
		});
	}) 
})(window,jQuery)