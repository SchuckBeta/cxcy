//-------------公司导师轮播部分的js代码-------------
//-------------公司导师轮播部分的js代码-------------
//-------------公司导师轮播部分的js代码-------------
var scrollBoxUl = document.getElementById("scrollbox").getElementsByTagName("ul")[0];
var scrollBoxLi = scrollBoxUl.getElementsByTagName("li");
//	设置ul的宽度
scrollBoxUl.style.width = scrollBoxLi.length*346+"px";
//	向左移动的次数
var n = 0;

var companyTeacher = {
	
	leftClick:function(){
		$("#left").click(function(){
			if (n>=(scrollBoxLi.length-3)) {
				n=scrollBoxLi.length-3;
				scrollBoxUl.style.marginLeft=(-346)*n+"px";
			} else{
				scrollBoxUl.style.marginLeft=(-346)*(n+1)+"px";
				n=n+1;
			}
		});
	},
	rightClick:function(){
		$("#right").click(function(){
			if (n<=0) {
				n=0;
				scrollBoxUl.style.marginLeft=(-346)*n+"px";
			} else{
				scrollBoxUl.style.marginLeft=(-346)*(n-1)+"px";
				n=n-1;
			}
		});
	},
	
}

//-------------校园导师轮播部分的js代码-------------
//-------------校园导师轮播部分的js代码-------------
//-------------校园导师轮播部分的js代码-------------

var inforScroll = document.getElementById("inforScroll");
var inforScrollLi = inforScroll.getElementsByTagName("li");

var schoolTeacher = {
	
	moveLeftClick:function(){
		$("#moveLeft").click(function(){
			inforScroll.appendChild(inforScrollLi[0]);
		});
	},
	moveRightClick:function(){
		$("#moveRight").click(function(){
			inforScroll.insertBefore(inforScrollLi[inforScrollLi.length-1],inforScrollLi[0])
		});
	},
	
}

