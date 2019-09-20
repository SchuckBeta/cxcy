

var left = {
	itemsChange: function(){
		$(".down .left ul").find("li").mouseover(function(){
			$(".down .left ul li").removeClass("current");
			$(this).addClass("current");
		})
	},
}






