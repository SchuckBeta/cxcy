<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<html>

	<head>
		<meta charset="utf-8" />
		<link href="${ctxCss}/video-js.css" rel="stylesheet">
		<!--头部导航公用样式-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css" />
		<!--公用重置样式文件-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="${ctxCss}/html/dsrdStyle.css" />
		<meta name="decorator" content="site-decorator"/>
		<title>${frontTitle}</title>
	</head>

	<body>
		<div class="container" style="margin-top: 40px;">
			<!-- <div class="scinfo">
				<div class="scinfonavigation">
					<ul>
						<li><img src="${ctxImg}/bc_home.png" style="margin-top:-3px"></li>
						<li>
							<a href="${ctxFront}/">首页></a>
						</li>
						<li>大赛热点</li>
					</ul>
				</div>
			</div> -->
			<div class="dsrdinfo">
				<h3>“翱翔系列小卫星”夺冠第二届中国“互联网+”大学生创新创业大赛</h3>
				<h6>发布时间:2016-12-16   来源:全国大学生创业服务端     浏览量:150</h6>
				<h5 style="color:#666">“翱翔系列小卫星”项目以530分获得全国冠军！2016年10月14日晚,经过2小时的精彩对决,第二届中国"互联网+"
				大学生创新创业全国总决赛在华中科技大学光谷体育诞生:</h5>
				<div class="m">
					<!--播放器-->
					<video style="margin:auto; " id="my-video" class="video-js" controls="controls" preload="auto" width="514" height="360" poster="MY_VIDEO_POSTER.jpg" data-setup="{}">
						<source src="${ctxImg}/test.mp4" type="video/ogg" autostart="false" loop="false">
						<p class="vjs-no-js">
							当前浏览器不支持 video直接播放，请使用高版本的浏览器查看。
							<a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
						</p>
					</video>
				</div>
			</div>
			<div style="clear: both;"></div>
			<div class="gj_info">
				<p>冠军项目是什么样的高科技？</p>
				<p>目前以立方星为代表的低成本商业航天正成为世界航天市场的创新创业热点。获得冠军的“翱翔系列微小卫星”项目,由西北工业大学在校研究生组成的创业团队创立,面向微小微型系统平台解决方案与服务,作为对高成本尚业卫星的替代</p>
				<p>西北工业大学“翱翔系列微小卫星”项目团队成员代表、指导教师接受现场采访</p>
				<p>6月25日 游戏呗工业大学团队制的第一颗微小卫星"翱翔之星",作为世界上首颗12U立方星,捂载"长征七号"新型运载火箭在海南文昌发射成功。2015年底,"翱翔"系列还将有两颗卫星发射</p>
				<p>翱翔系列微小卫星’项目以530分获得全国总冠军！2016年10月14日晚，经过2个小时的精彩对决，第二届中国“互联网+”大学</p>
				<p>该项目团队定制了立方星总体设计,系统集成和总装测试的研制规范,并提出基于"互联网+航天"的商业模式,提供面向团体和个人用户的低成本,模块化的功能定制卫星平台和定制化,个性化的空间信息五福等</p>
				<div class="dsimg">
					<img src="${ctxImg}/dsrd_02.jpg">
				</div>
				<p style="text-align: center; margin-top: -100px;margin-left:-30px">西北工业大学“翱翔系列微小卫星”项目团队成员代表、指导教师接受现场采访</p>
			</div>

			<div class="more">
				<h3>相关推荐</h3>
			</div>
			<div class="clearfix"></div>
			<div class="newinfo">
				<ul>
					<!--<li>
							<div class="newone fl">
								李克强对首届中国"互联网+"大学生创新创业大赛做出重要批示
							</div>
							<div class="newtwo fr">
								2016年1月12日
							</div>
						</li>-->
					<li><img src="${ctxImg}/dsrd.jpg"> </li>
					<li><img src="${ctxImg}/dsrd01.jpg"></li>
					<li><img src="${ctxImg}/dsrdo2.jpg"></li>
					<li><img src="${ctxImg}/dsrd03.jpg"></li>
				</ul>
			</div>
		</div>
		</div>

		<script src="http://vjs.zencdn.net/5.18.4/video.min.js"></script>
		<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>
		<script type="text/javascript" src="${ctxJs}/scroll.js"></script>
		<script>
			var myPlayer = videojs('my-video');
			videojs("my-video").ready(function() {
				var myPlayer = this;
				myPlayer.play();
			});
		</script>
	</body>

</html>