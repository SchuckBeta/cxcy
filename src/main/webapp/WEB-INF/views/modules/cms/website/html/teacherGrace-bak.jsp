<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<html>
<head>
<meta charset="UTF-8">
	<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/common.css"/>
<meta name="decorator" content="site-decorator" />
<title>${frontTitle}</title>
<style>
.banner {
	display: block;
	width: 100%;
}

.mid {
	width: 1240px;
	/*overflow: hidden;*/
	margin: 0 auto;
}

.itemname {
	width: 212px;
	height: 67px;
	margin: 0 auto;
	margin-top: 85px;
	margin-bottom: 65px;
	overflow: hidden;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	background-position: 0px 0px;
}

/*浼佷笟瀵煎笀閮ㄥ垎css鏍峰紡*/
.companyTeacher p {
	width: 100%;
	height: 36px;
	line-height: 36px;
	overflow: hidden;
	color: #de3b0a;
	font-size: 22px;
}

.companyTeacher p span {
	float: left;
	width: 15px;
	height: 16px;
	margin-top: 10px;
	margin-right: 10px;
	overflow: hidden;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	background-position: -213px -67px;
}

.companyTeacher .content {
	width: 100%;
	overflow: hidden;
	position: relative;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 0px 98px;
}

.companyTeacher .content span {
	display: block;
	width: 49px;
	height: 49px;
	overflow: hidden;
	position: absolute;
	top: 192px;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	cursor: pointer;
}

.companyTeacher .content span:first-child {
	left: 24px;
	background-position: 0px -67px;
}

.companyTeacher .content span:nth-child(2) {
	right: 24px;
	background-position: -49px -67px;
}

.companyTeacher .content .scrollbox {
	width: 100%;
	overflow: hidden;
}

.companyTeacher .content ul {
	width:; /*閫氳繃js鑾峰彇瀹藉害*/
	margin: 50px 0px;
	transition: all 0.5s ease-in;
	-webkit-transition: all 0.5s ease-in;
	-moz-transition: all 0.5s ease-in;
	-ms-transition: all 0.5s ease-in;
	-o-transition: all 0.5s ease-in;
}

.companyTeacher .content ul li {
	float: left;
	width: 346px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 10px 25px;
	margin-bottom: 130px;
	text-align: center;
}

.companyTeacher .content ul li .bigCircle {
	width: 296px;
	height: 296px;
	border-radius: 148px;
	-webkit-border-radius: 148px;
	box-shadow: 1px 1px 1px 1px #d2d0d0;
	-webkit-box-shadow: 1px 1px 1px 1px #d2d0d0;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 10px;
	margin-bottom: 50px;
}

.companyTeacher .content ul li .secondCircle {
	width: 100%;
	height: 100%;
	overflow: hidden;
	border-radius: 50%;
	-webkit-border-radius: 50%;
	position: relative;
}

.companyTeacher .content ul li .secondCircle img {
	width: 100%;
	height: 100%;
}

.companyTeacher .content ul li .secondCircle .sheild {
	position: absolute;
	left: 0px;
	top: 0px;
	width: 100%;
	height: 100%;
	border-radius: 50%;
	-webkit-border-radius: 50%;
	background: rgba(222, 59, 10, 0.6);
	text-align: center;
	display: none;
}

.companyTeacher .content ul li .secondCircle .sheild a {
	display: inline-block;
	width: 115px;
	height: 39px;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	background-position: -98px -67px;
	margin-top: 120px;
}

.companyTeacher .content ul li:hover .sheild {
	display: block;
}

.companyTeacher .content ul li big {
	display: block;
	color: #474747;
	font-size: 20px;
	line-height: 24px;
	letter-spacing: 2px;
}

.companyTeacher .content ul li .line {
	width: 70px;
	height: 1px;
	background: #474747;
	display: inline-block;
}

.companyTeacher .content ul li small {
	display: block;
	color: #de3b0a;
	font-size: 20px;
	line-height: 50px;
	letter-spacing: 8px;
}

/*鏍″洯瀵煎笀閮ㄥ垎css鏍峰紡*/
.schoolTeacher .title {
	width: 100%;
	height: 36px;
	line-height: 36px;
	overflow: hidden;
	color: #de3b0a;
	font-size: 22px;
}

.schoolTeacher .title span {
	float: left;
	width: 15px;
	height: 16px;
	margin-top: 10px;
	margin-right: 10px;
	overflow: hidden;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	background-position: -213px -67px;
}

.schoolTeacher .content {
	width: 100%;
	overflow: hidden;
	position: relative;
}

.schoolTeacher .content .introBox {
	position: absolute;
	top: 80px;
	right: 0px;
	width: 711px;
	height: 277px;
	overflow: hidden;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 5px 80px 42px 20px;
	overflow: hidden;
}

.schoolTeacher .content .introBox big {
	font-size: 30px;
	color: #de3b0a;
	letter-spacing: 8px;
	line-height: 36px;
}

.schoolTeacher .content .introBox .line {
	width: 50px;
	height: 2px;
	background: #de3b0a;
	margin: 10px 0px;
}

.schoolTeacher .content .introBox p {
	font-size: 14px;
	text-indent: 2em;
	line-height: 24px;
}

.schoolTeacher .content .introBox .arrowBox {
	width: 100%;
	height: 42px;
	position: absolute;
	left: 20px;
	bottom: 0px;
}

.schoolTeacher .content .introBox .arrowBox span {
	float: left;
	width: 41px;
	height: 42px;
	overflow: hidden;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	background-position: -212px 0px;
	cursor: pointer;
}

.schoolTeacher .content .introBox .arrowBox a {
	float: left;
	width: 41px;
	height: 42px;
	overflow: hidden;
	background: url(/img/teacherGraceSmallIcons.png) no-repeat;
	background-position: -259px 0px;
	cursor: pointer;
}

.schoolTeacher .content ul {
	width: 8000px;
	overflow: hidden;
	padding: 80px 0px 100px 0px;
}

.schoolTeacher .content ul li {
	width: 130px;
	height: 130px;
	margin-top: 286px;
	padding: 6px;
	float: left;
	margin-right: 30px;
	box-shadow: 1px 1px 1px 1px #d2d0d0;
	-webkit-box-shadow: 1px 1px 1px 1px #d2d0d0;
	text-align: center;
	overflow: hidden;
}

.schoolTeacher .content ul li:first-child {
	margin-left: 10px;
}

.schoolTeacher .content ul li:nth-child(2) {
	width: 324px;
	height: 408px;
	margin-top: 0px;
	padding: 10px;
}

.schoolTeacher .content ul li img {
	width: 100%;
}

/*瑁呴グ鍥剧墖瀹氫綅鏍峰紡*/
.decorone {
	width: 66px;
	height: 48px;
	overflow: hidden;
	background: url(/img/teacherGraceSquarePic.png) no-repeat;
	background-position: 0px 0px;
	position: absolute;
	top: 350px;
	left: 15px;
}

.decortwo {
	width: 211px;
	height: 226px;
	overflow: hidden;
	background: url(/img/LINE.PNG) no-repeat;
	background-position: left top;
	background-size: 100% 100%;
	position: absolute;
	top: 400px;
	right: 0px;
	margin-right: -70px;
}

.decorthree {
	width: 500px;
	height: 500px;
	overflow: hidden;
	background: url(/img/LINE.PNG) no-repeat;
	background-position: 0px 0px;
	background-size: 100% 100%;
	position: absolute;
	top: 900px;
	left: 0px;
	margin-left: -300px;
}

.decorfour {
	width: 96px;
	height: 70px;
	overflow: hidden;
	background: url(/img/teacherGraceSquarePic.png) no-repeat;
	background-position: -66px 0px;
	position: absolute;
	top: 1000px;
	right: 20px;
}

.decorfive {
	width: 66px;
	height: 82px;
	overflow: hidden;
	background: url(/img/teacherGraceSquarePic.png) no-repeat;
	background-position: 0px -48px;
	position: absolute;
	top: 1380px;
	left: 10px;
}
</style>
</head>
<body>

	<!--企业导师部分-->
	<div class="companyTeacher mid" style="margin-top:50px">
		<p><a id="qyds" name="qyds" style="display: none;"></a>
			<span id="qyds" name="qyds"></span>企业导师
		</p>
		<div class="content">
			<span id="left"></span> <span id="right"></span>
			<div class="scrollbox" id="scrollbox">
				<ul>
					<li>
						<div class="bigCircle">
							<div class="secondCircle">
								<img src="${ctxImg}/ceo2.png" />
								<div class="sheild">
									<a href="#"></a>
								</div>
							</div>
						</div> <big>领英中国总裁</big>
						<div class="line"></div> <small>沈博阳</small>

					</li>
					<li>
						<div class="bigCircle">
							<div class="secondCircle">
								<img src="${ctxImg}/ceo.png" alt="" />
								<div class="sheild">
									<a href="#"></a>
								</div>
							</div>
						</div> <big>噢易云董事长</big>
						<div class="line"></div> <small>杨军</small>

					</li>
					<li>
						<div class="bigCircle">
							<div class="secondCircle">
								<img src="${ctxImg}/partner.png" />
								<div class="sheild">
									<a href="#"></a>
								</div>
							</div>
						</div> <big>赛富亚洲首席合伙人</big>
						<div class="line"></div> <small>阎焱</small>

					</li>
					<li>
						<div class="bigCircle">
							<div class="secondCircle">
								<img src="${ctxImg}/ceo.png" alt="" />
								<div class="sheild">
									<a href="#"></a>
								</div>
							</div>
						</div> <big>噢易云董事长</big>
						<div class="line"></div> <small>杨军</small>

					</li>
					<li>
						<div class="bigCircle">
							<div class="secondCircle">
								<img src="${ctxImg}/partner.png" />
								<div class="sheild">
									<a href="#"></a>
								</div>
							</div>
						</div> <big>赛富亚洲首席合伙人</big>
						<div class="line"></div> <small>阎焱</small>

					</li>
				</ul>
			</div>
		</div>
	</div>

	<!--校园导师部分-->
	<div class="schoolTeacher mid">
		<p class="title">
			<span id="xyds" name="xyds" ></span>校园导师
		</p>
		<div class="content">
			<div class="introBox">
				<big>贺慧</big>
				<div class="line"></div>
				<p>学士，教授。1983年毕业于北京大学物理系，获学士学位。现任长沙理工大学大学物电学院副院长，长沙理工大学空间等离子体物理研究所副所长。
				</p>
				<p>从教以来先后讲授《EDA技术基础》、《电子线路CAD》、《数学物理方法》等课程。获校级教学成果二等奖一项，指导本科生参加大学生电子设计竞赛多次获得全国一等奖。</p>
				<div class="arrowBox">
					<span id="moveLeft"></span> <a id="moveRight"></a>
				</div>
			</div>
			<ul id="inforScroll">
				<li class="one"><img src="${ctxImg}/1.jpg" /></li>
				<li class="two"><img src="${ctxImg}/2.png" /></li>
				<li class="three"><img src="${ctxImg}/3.jpg" /></li>
				<li class="four"><img src="${ctxImg}/4.jpg" /></li>
				<li class="five"><img src="${ctxImg}/5.jpg" /></li>
				<li class="six"><img src="${ctxImg}/6.jpg" /></li>
				<li class="five"><img src="${ctxImg}/5.jpg" /></li>
				<li class="six"><img src="${ctxImg}/6.jpg" /></li>
			</ul>
		</div>
	</div>

	<!--以下是装饰图片非主要内容-->
	<div class="decorone"></div>
	<div class="decortwo"></div>
	<div class="decorthree"></div>
	<div class="decorfour"></div>
	<div class="decorfive"></div>
</body>


<!--js：1.11.3-->
<script src="${ctxCommon}/common-js/jquery.min.js" type="text/javascript"
	charset="utf-8"></script>
<script type="text/javascript">
	$(function() {
		var initCompanyTeacher = function(){
			companyTeacher.leftClick();
			companyTeacher.rightClick();
		}();
		var initSchoolTeacher = function(){
			schoolTeacher.moveLeftClick();
			schoolTeacher.moveRightClick();
		}();
	});
	var scrollBoxUl = document.getElementById("scrollbox").getElementsByTagName("ul")[0];
	var scrollBoxLi = scrollBoxUl.getElementsByTagName("li");
//		设置ul的宽度
	scrollBoxUl.style.width = scrollBoxLi.length*346+"px";
//		向左移动的次数
	var n = 0;
	var companyTeacher = {
		leftClick:function(){
			$("#left").click(function(){
				if (n>=(scrollBoxLi.length-3)){
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
		}
	}
</script>
</html>
