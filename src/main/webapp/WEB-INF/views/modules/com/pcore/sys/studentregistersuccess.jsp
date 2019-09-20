<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<html>

<head>
<meta charset="utf-8" />
<meta name="decorator" content="site-decorator" />
<title>${frontTitle}</title>
<link rel="stylesheet" type="text/css"
	href="${ctxCommon}/common-css/bootstrap.min.css" />
<style type="text/css">
	.now{
	width:600px;
	height:40px;
		margin:100px auto;
		font-size: 30px;

	}
</style>
</head>

<body>
	<div class="now"><span>该页面正在建设中。。。。。。</span></div>
	<script src="${ctxCommon}/common-js/jquery.min.js" type="text/javascript"
		charset="utf-8"></script>
	<script type="text/javascript" src="${ctxCommon}/common-js/bootstrap.min.js"></script>
	<!--用于轮播的js-->
	<script type="text/javascript"
		src="${ctxCommon}/common-js/jquery.SuperSlide.2.1.1.js"></script>
	<!--文本溢出-->
	<script src="${ctxCommon}/common-js/clamp.js" type="text/javascript"
		charset="utf-8"></script>
	<!--本页面相关js部分-->
	<script src="${ctxJs}/index.js" type="text/javascript" charset="utf-8"></script>
	<!--页面js初始化-->
	<script type="text/javascript">
		$(function() {
			//			banner轮播初始化js
			var initBannerBox = function() {
				bannerBox.focusScroll();
			}();
			//			视频播放初始化js
			var initHotspotBox = function() {
				hotspotBox.videoPlay();
				hotspotBox.focusScroll();
			}();

			var initTeacherGraceBox = function() {
				teacherGraceBox.focusScroll();
			}();
			var user = $("#userId").val();
			if(user!=null && user!=""){
				$("#myCarousel").show();
				$.ajax({
					url : "a/oa/oaNotify/loginList",
					type : "POST",
					success : function(msg) {
						if (msg.length > 0) {
							msg.forEach(function(it) {
								var item = $('<div class="item">' + it + '</div>');
								$('#wrap').append(item);
							})
						}
						$('#wrap div').eq(0).addClass('active');
					},
					error : function() {
					}

				});
			}

			// 初始化轮播
			$("#myCarousel").carousel({
				interval : 2500
			});//每页切换时间间隔

		});
	</script>
</body>
</html>