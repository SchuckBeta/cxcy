
var bannerBox = {
	focusScroll: function() {
		// jQuery(".slideBox").slide({ titCell:".hd ul",  mainCell: ".bd ul", effect: "leftLoop", autoPlay: true, autoPage:"<li></li>"});
		jQuery(".slideBox").slide({ titCell:".hd ul",  mainCell: ".bd ul", effect: "leftLoop", autoPlay: true, interTime: 4000, autoPage:"<li></li>"});
	}
}

var hotspotBox = {
	media:document.getElementById("media"),
	videoPlay:function(){
		$(".start").click(function() {
			$(this).hide();
			hotspotBox.media.play();
		})
		hotspotBox.media.onended = function(){
			$(".start").show();
		}
	},
	focusScroll: function() {
		jQuery(".slideBox2").slide({ mainCell: ".bd ul", effect: "leftLoop", autoPlay: true });
	},
}

var teacherGraceBox = {
	focusScroll: function() {
		jQuery(".slideBox3").slide({ mainCell: ".bd ul", effect: "leftLoop", autoPlay: true, interTime: 4000});
	},
}

//var module = document.getElementById("spillout");
//$clamp(module, {clamp: 3});
//
//var module2 = document.getElementById("spillout2");
//$clamp(module2, {clamp: 3});
//
//var module3 = document.getElementById("spillout3");
//$clamp(module3, {clamp: 3});
//
//var intro = document.getElementById("intro");
//$clamp(intro, {clamp: 2});
//
//var intro2 = document.getElementById("intro2");
//$clamp(intro2, {clamp: 2});
//
//var intro3 = document.getElementById("intro3");
//$clamp(intro3, {clamp: 2});
//
//var intro4 = document.getElementById("intro4");
//$clamp(intro4, {clamp: 2});



//dom显示隐藏
function domIsShow(dom, isShow){
	if(isShow){
		$(dom).show();
	}else{
		$(dom).hide();

	}
}

//视频播放器
function initVBox(vid){
	var domVBoxP = $(".leftVideoBox p");
	var domVBoxStart = $(".leftVideoBox .start");

	//视频插件Hover显示隐藏文本
	$(vid).hover(function(){
		domIsShow(domVBoxP, false);
	},function(){
		domIsShow(domVBoxP, true);
	});

	//播放按钮Hover显示隐藏文本
	$(domVBoxStart).hover(function(){
		domIsShow(domVBoxP, false);
	},function(){
		domIsShow(domVBoxP, true);
	});

	vid.addEventListener('play', function() {
		domIsShow(domVBoxP, false);
	})
	vid.addEventListener('pause', function() {
		domIsShow(domVBoxP, false);
	})
	vid.addEventListener('ended', function() {
		vid.controls=false;
		domIsShow(domVBoxP, true);
	})
}

$(function() {
	//视频播放器初始化
	var vid=document.getElementById("media");
	if(vid){
		initVBox(vid);
	}

	//跑马灯及尾部弹窗
	//			banner轮播初始化js
	var initBannerBox = function() {
		var $slideBox = $('#slideBox');
		if($slideBox.find('.bd li').size() > 1){
			console.log($slideBox.find('.bd li').size());
			bannerBox.focusScroll();
		}else{
			$slideBox.find('hd,a.prev,a.next').detach();
		}

	}();
	//			视频播放初始化js
	if(vid){
		var initHotspotBox = function() {
			hotspotBox.videoPlay();
			//取消滚动
			// hotspotBox.focusScroll();
		}();
	}

	var initTeacherGraceBox = function() {
		teacherGraceBox.focusScroll();
	}();

	$.ajax({
		url : "/f/frontNotice/frontList",
		type : "GET",
		contentType: "application/json; charset=utf-8",
		dataType: "json",
		success : function(data) {
			//data.forEach(function(it) {
			if(data) {
				$.each(data, function (i, v) {
					var titleName = v.titleName;
					var noticeId = v.id;
					var ahref="' + ctxFront + '/frontNotice/noticeView?id="+noticeId;
					var item = $('<div class="item">'
								+'<a href='+ahref+'>'
								+titleName
								+'</a></div>');
					$('#wrap').append(item);
				})
			}
			$('#wrap div').eq(0).addClass('active');
		},
		error : function() {
		}
	});
	/* 跳转到通知详情 */
	// $("#wrap").click(function(){
	// 	window.location.href="f/page-notice";
	// });
	  // 初始化轮播
	$("#myCarousel").carousel({
		interval : 7000
	});//每页切 换时间间隔

	getUserId();
	//缓存用户ID
	function getUserId(){
		var user = $("#userId").val();
		if(!user){
			return;
		}
		localStorage.setItem("userId",user);
	}
});







$(function(){
	//双创动态、双创通知、省市动态新闻列表
	var news_data = {
		// 双创动态新闻列表
		dynamic : [ {
			// 新闻标题
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			// 新闻链接
			url : '/f/page-scdt',
			// 新闻日期
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		} ],
		// 双创通知新闻列表
		notifications : [ {
			// 新闻标题
			title : '关于组织申报2017年度大学生创新创业',
			// 新闻链接
			url : '/f/page-scdt',
			// 新闻日期
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		} ],
		// 省市动态新闻列表
		others : [ {
			// 新闻标题
			title : '第二届浙江省“互联网+”大学生创新创业“大赛',
			// 新闻链接
			url : '/f/page-scdt',
			// 新闻日期
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		}, {
			title : '李克强对首届中国“互联网+”大学生创新创业“大赛',
			url : '/f/page-scdt',
			date : '2016-2-23'
		} ]
	};

	// 双创新闻列表
	var newsList = $('.news-list');
	if((newsList != null) && (newsList.length > 0)){
		$.each($(newsList), function(){
			var list = $(this);
			var data = news_data[list.attr("data-key")];

			if((data != null) && (data.length > 0)){
				var buf = [];
				$.each(data, function(i, o){
					buf.push('<li><span class="date">' + o.date + '</span><a href="' + o.url + '" class="link" title="' + o.title + '">' + o.title + '</a></li>');
				});
				list.html(buf.join(''));
			}
		});
	}
});

function tmpl(str, data) {
	return str.replace(/\{(.*?)\}/g, function(_, key){
		return data[key];
	});
}