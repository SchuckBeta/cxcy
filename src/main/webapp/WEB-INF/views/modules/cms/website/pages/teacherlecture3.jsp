<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8" />
		<link href="${ctxCss}/video-js.css" rel="stylesheet">
		<!--公用重置样式文件-->
		<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css" />
		<link rel="stylesheet" type="text/css" href="${ctxCss}/msjtStyle.css" />
		<meta name="decorator" content="site-decorator"/>
		<title>${frontTitle}</title>
	</head>
<body>
		<div class="container" style="margin-top: 40px;">
			<div class="scinfo">
				<!-- <div class="scinfonavigation">
					<ul>
						<li><img src="${ctxImg}/bc_home.png"></li>
						<li>首页></li>
						<li>名师讲堂></li>
						<li>计算机科学和Python编程导论</li>
					</ul>
				</div> -->
			</div>
			<div style="clear: both;"></div>
			<div class="computerinfo">
				<h3>计算机科学和Python编程导论</h3>
				<p class="computer_info">发布时间 2017-1-2 专业课程分类:计算机</p>
				<div class="m">
					<!--播放器-->
					<video id="my-video" class="video-js" controls preload="auto" width="640" height="264" poster="MY_VIDEO_POSTER.jpg" data-setup="{}">
						<source src="${ctxImg}/temple.mp4" type="video/ogg">
						<p class="vjs-no-js">
							当前浏览器不支持 video直接播放，请使用高版本的浏览器查看。
							<a href="http://videojs.com/html5-video-support/" target="_blank">supports HTML5 video</a>
						</p>
					</video>
				</div>
				<div class="video_info">
					<h4>课程描述</h4>
					<p class="class_info">本课程将介绍把计算机科学作为工具解决现实世界中的分析问题</p>
					<p class="teach_info"><span>授课教师 </span> 常建 教授</p>
					<button id="Upload">下载课件</button> <span id="size">下载数(30)</span>
				</div>
			</div>
			<div style="clear: both;"></div>
			<!--选项卡-->
			<div class="tab">
				<div class="tab_title">
					<ul>
						<li id="main">菜单列表</li>
						<li id="add">菜单添加</li>
					</ul>
				</div>
				<div style="clear: both;"></div>
				<div class="tab_info">
					本课程是两部分课程的上半部分：即 计算机科学和 Python 编程导论及计算思维和数据科学导论。两部分课程旨在帮助以前从未接触
				</div>
				<div class="tab_infoone">
					本课程是两部分课程的上半部分：即 计算机科学和 Python 编程导论及计算思维和数据科学导论。两部分课程旨在帮助以前从未接触
				</div>
			</div>
			<!--无缝轮播-->
			<div class="recommendclass">
				<h4>推荐课程</h4>
			</div>
			<div id="featureContainer">
				<div id="feature">
					<div id="block">
						<div id="botton-scroll">
							<ul class="featureUL">
								<li class="featureBox">
									<div class="box">

										<img alt="Paracletos" src="${ctxImg}/msjt01.jpg" width="240" height="150">

									</div>
									<!-- /box -->
								</li>
								<li class="featureBox">
									<div class="box">

										<img alt="Natural Touch Soap" src="${ctxImg}/msjt01.jpg" width="240" height="150">

									</div>
									<!-- /box -->
								</li>
								<li class="featureBox">
									<div class="box">

										<img alt="LRTK" src="${ctxImg}/msjt01.jpg" width="240" height="150">

									</div>
									<!-- /box -->
								</li>
								<li class="featureBox">
									<div class="box">

										<img alt="Natalie Reid" src="${ctxImg}/msjt01.jpg" width="240" height="150">

									</div>
									<!-- /box -->
								</li>
								<li class="featureBox">
									<div class="box">

										<img alt="酷站代码" src="${ctxImg}/msjt01.jpg" width="240" height="150">

									</div>
									<!-- /box -->
								</li>
								<li class="featureBox">
									<div class="box">
										<img alt="Catherine Sherwood" src="${ctxImg}/msjt01.jpg" width="240" height="150">
									</div>
									<!-- /box -->
								</li>
							</ul>
						</div>
						<!-- /botton-scroll -->
					</div>
					<!-- /block -->
					<a class="prev" href="javascript:void();">Previous</a>
					<a class="next" href="javascript:void();">Next</a>
				</div>
			</div>

			<div class="comment">
				<span>发表评论</span>
				<div class="style_info"></div>
				<div style="clear: both;"></div>
				<textarea id="discuss"></textarea>
				<input type="button" value="发表评论" id="publish" />
				<ul class="review">
					<li>全部评论(30)</li>
					<li>我的评论(1)</li>
				</ul>
			</div>
			<div style="clear: both;"></div>
			<div class="commentcontent">
				<div class="commentimg">
					<img src="${ctxImg}/ceo.png">
				</div>
				<div class="commentright">
					<p>彭宇 <span>1小时</span></p>
					<p>适合从来没有接触过计算机科学成果的人们，学会计算机会议编写程序科研</p>
					<p>(30) 回复(0)</a>
					</p>
				</div>
			</div>

			<div class="commentcontent">
				<div class="commentimg">
					<img src="${ctxImg}/ceo.png">
				</div>
				<div class="commentright">
					<p>彭宇 <span>1小时</span></p>
					<p>适合从来没有接触过计算机科学成果的人们，学会计算机会议编写程序科研</p>
					<p>(30) 回复(0)</a>
					</p>
				</div>
			</div>

			<div class="commentcontent">
				<div class="commentimg">
					<img src="${ctxImg}/ceo.png">
				</div>
				<div class="commentright">
					<p>彭宇 <span>1小时</span></p>
					<p>适合从来没有接触过计算机科学成果的人们，学会计算机会议编写程序科研</p>
					<p>(30) 回复(0)</a>
					</p>
				</div>
			</div>

			<div class="packup">
				<a href="javascript:void(0)">收起/展示所有评论</a>
			</div>

			<div class="commentcontent commentcontent_info">
				<div class="commentimg">
					<img src="${ctxImg}/ceo.png">
				</div>
				<div class="commentright">
					<p>彭宇 <span>1小时</span></p>
					<p>适合从来没有接触过计算机科学成果的人们，学会计算机会议编写程序科研</p>
					<p>(30) 回复(0)</a>
					</p>
				</div>
			</div>

			<div class="commentcontent commentcontent_info">
				<div class="commentimg">
					<img src="${ctxImg}/ceo.png">
				</div>
				<div class="commentright">
					<p>彭宇 <span>1小时</span></p>
					<p>适合从来没有接触过计算机科学成果的人们，学会计算机会议编写程序科研</p>
					<p>(30) 回复(0)</a>
					</p>
				</div>
			</div>
		</div>
		<script src="http://vjs.zencdn.net/5.18.4/video.min.js"></script>
		<script type="text/javascript" src="${ctxJs}/scroll.js"></script>
		<script>
			var myPlayer = videojs('my-video');
			videojs("my-video").ready(function() {
				var myPlayer = this;
				myPlayer.play();
			});

			$(function() {
				$("#main").click(function() {
					$(".tab_info").show();
					$(".tab_infoone").hide();
					$("#main").css("background-color","#EA442E");
					$("#add").css("background-color","#D3D3D3");
				})
				$("#add").click(function() {
					$(".tab_infoone").show();
					$(".tab_info").hide();
					$("#add").css("background-color","#EA442E");
					$("#main").css("background-color","#D3D3D3");
				})
				$(".packup a").click(function(){
					$(".packup a").hide();
					$(".commentcontent_info").show();
				})
			})
		</script>
	</body>
</html>
