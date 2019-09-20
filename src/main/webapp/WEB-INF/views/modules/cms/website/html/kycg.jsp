<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8" />
<!--公用重置样式文件-->
<link rel="stylesheet" type="text/css"
	href="${ctxCommon}/common-css/bootstrap.min.css" />
<meta name="decorator" content="site-decorator" />
<title>${frontTitle}</title>
<style type="text/css">
.midBox {
	width: 760px;
	margin: 0px auto;
	margin-top: 100px;
	margin-bottom: 500px;
}

.midBox .rangeBox {
	width: 100%;
}

.midBox .rangeBox .title {
	display: block;
	width: 167px;
	height: 64px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px 0px;
	margin: 0px auto;
	margin-bottom: 60px;
}

.midBox .rangeBox .content {
	width: 100%;
	position: relative;
	color: #656565;
	font-family: "microsoft yahei";
	font-size: 14px;
	line-height: 26px;
	margin-bottom: 140px;
}

.midBox .rangeBox .content small {
	display: block;
	width: 60px;
	height: 38px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px -381px;
	position: absolute;
	top: -10px;
	left: -70px;
}

.midBox .rangeBox .content big {
	display: block;
	width: 60px;
	height: 38px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: -60px -381px;
	position: absolute;
	top: 60px;
	right: -70px;
}

.midBox .rewardObjectBox {
	width: 100%;
	overflow: hidden;
}

.midBox .rewardObjectBox .title {
	display: block;
	width: 167px;
	height: 63px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px -64px;
	margin: 0px auto;
	margin-bottom: 44px;
}

.midBox .rewardObjectBox .content {
	width: 100%;
	overflow: hidden;
	font-family: "microsoft yahei";
	color: #656565;
	font-size: 14px;
	line-height: 26px;
	margin-bottom: 150px;
}

.midBox .rewardObjectBox .content h6 {
	text-align: center;
	margin-top: 30px;
}

.midBox .declareBox {
	width: 100%;
	overflow: hidden;
}

.midBox .declareBox .title {
	display: block;
	width: 167px;
	height: 63px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px -127px;
	margin: 0px auto;
	margin-bottom: 56px;
}

.midBox .declareBox .content {
	width: 509px;
	height: 238px;
	margin: 0 auto;
	box-shadow: 1px 1px 1px 2px #999;
	-webkit-box-shadow: 1px 1px 1px 2px #d3d3d3;
	margin-bottom: 140px;
	position: relative;
}

.midBox .declareBox .content small {
	display: block;
	width: 12px;
	height: 12px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: -120px -381px;
	position: absolute;
	left: 40px;
	top: 18px;
}

.midBox .declareBox .content big {
	display: block;
	width: 12px;
	height: 12px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: -120px -381px;
	position: absolute;
	right: 40px;
	top: 18px;
}

.midBox .declareBox .content p {
	width: 100%;
	overflow: hidden;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	font-family: "宋体";
	text-align: center;
	padding: 88px 20px;
	color: #656565;
	font-size: 16px;
	line-height: 26px;
}

.midBox .declareBox .content p strong {
	font-size: 20px;
	color: #e9442d;
	font-family: "microsoft yahei";
}

.midBox .producerBox {
	width: 100%;
	overflow: hidden;
}

.midBox .producerBox .title {
	display: block;
	width: 167px;
	height: 63px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px -190px;
	margin: 0px auto;
	margin-bottom: 66px;
}

.midBox .producerBox ul {
	width: 100%;
	overflow: hidden;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 0px 37px;
	margin-bottom: 140px;
}

.midBox .producerBox ul li {
	width: 100%;
	height: 68px;
	margin-bottom: 12px;
	background: #e7e7e7;
	position: relative;
	border-radius: 34px;
	-moz-border-radius: 34px;
	-webkit-border-radius: 34px;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding: 0px 30px;
}

.midBox .producerBox ul li span {
	position: absolute;
	bottom: -6px;
	left: 50%;
	display: block;
	width: 12px;
	height: 6px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: -120px -393px;
}

.midBox .producerBox ul li big {
	float: left;
	width: 100px;
	height: 68px;
	line-height: 68px;
	color: #e9442d;
	font-size: 16px;
}

.midBox .producerBox ul li table {
	height: 100%;
	font-size: 14px;
	color: #999;
}

.midBox .awardStandardBox {
	width: 100%;
	overflow: hidden;
}

.midBox .awardStandardBox .title {
	display: block;
	width: 167px;
	height: 63px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px -253px;
	margin: 0px auto;
	margin-bottom: 40px;
}

.midBox .awardStandardBox .smalltitle {
	width: 100%;
	overflow: hidden;
	margin-bottom: 60px;
	color: #999;
	font-size: 14px;
	text-align: center;
}

.midBox .awardStandardBox .content {
	width: 100%;
	overflow: hidden;
	position: relative;
	margin-bottom: 60px;
}

.midBox .awardStandardBox .content img {
	float: left;
	position: absolute;
	left: 0px;
	top: 15px;
}

.midBox .awardStandardBox .content .right {
	width: 100%;
	overflow: hidden;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding-left: 130px;
}

.midBox .awardStandardBox .content .right ul {
	width: 100%;
	overflow: hidden;
	margin-bottom: 60px;
}

.midBox .awardStandardBox .content .right ul li {
	width: 100%;
	overflow: hidden;
	margin-bottom: 7px;
}

.midBox .awardStandardBox .content .right ul li span {
	float: left;
	width: 19px;
	height: 19px;
	overflow: hidden;
	background: #8e8e8e;
	border-radius: 10px;
	-webkit-border-radius: 10px;
	-moz-border-radius: 10px;
	text-align: center;
	line-height: 19px;
	color: white;
	margin-right: 6px;
	font-size: 12px;
}

.midBox .awardStandardBox .content .right ul li p {
	float: left;
	width: 600px;
	overflow: hidden;
	font-size: 12px;
	color: #5d5d5d;
	line-height: 20px;
}

.midBox .rewardMethod {
	width: 100%;
	overflow: hidden;
}

.midBox .rewardMethod .title {
	display: block;
	width: 167px;
	height: 65px;
	overflow: hidden;
	background: url(/img/achievement1.png) no-repeat;
	background-position: 0px -316px;
	margin: 0px auto;
	margin-bottom: 64px;
}

.midBox .rewardMethod .content {
	width: 399px;
	height: 458px;
	overflow: hidden;
	margin: 0px auto;
	background: url(/img/achievement4.png) no-repeat center center;
	text-align: center;
	box-sizing: border-box;
	-moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	padding-top: 150px;
	padding-left: 50px;
	padding-right: 50px;
	color: #666;
	line-height: 30px;
}

.decorationpic5 {
	position: absolute;
	top: 100px;
	left: 0px;
	z-index:-99;
}

.decorationpic6 {
	position: absolute;
	top: 581px;
	right: 0px;
}

.decorationpic7 {
	position: absolute;
	top: 1406px;
	left: 0px;
}

.decorationpic8 {
	position: absolute;
	top: 2247px;
	right: 0px;
}

.decorationpic9 {
	position: absolute;
	top: 3822px;
	left: 0px;
}

.decorationpic10 {
	position: absolute;
	top: 4110px;
	right: 0px;
}
</style>
</head>
<body>
	<div class="midBox">
		<!-------成果范围-------->
		<div class="rangeBox">
			<span class="title"></span>
			<div class="content">
				<p>·大学生优秀科研成果形式主要为大学生在校期间获得的具有创新性或社会影响的科学技术成果、发明创造、专利技术、软科学成果、学术论文和文学作品等。
				</p>
				<p>·大学生毕业论文(设计)、美术类作品、表演类作品、“挑战杯”获奖成果及已获得省教育厅组织的学科竞赛、专业比赛奖励的成果不属于奖励范畴。</p>
				<small></small> <big></big>
			</div>
		</div>


		<!-------奖励对象------>
		<div class="rewardObjectBox">
			<span class="title"></span>
			<div class="content">
				<p>·当年在校的普通全日制本、专科学生个人独立或合作完成的科研成果经学院推荐可申请湖北省大学生优秀科研成果奖。</p>
				<p>·学生参与教师科研的成果不属于奖励范畴。</p>
				<h6>
					<img src="${ctxImg}/achievement2.png" alt="" />
				</h6>
			</div>
		</div>



		<!--------申报名额------------>
		<div class="declareBox">
			<span class="title"></span>
			<div class="content">
				<small></small> <big></big>
				<p>
					“211”工程学院申报数控制在本校上学年度全日制普通本专科在校生数的<strong>千分之二</strong>，其它高校申报数控制在<strong>千分之一</strong>。超过申报限额的院校不予受理。
				</p>
			</div>
		</div>


		<!---------工作程序--------->
		<div class="producerBox">
			<span class="title"></span>
			<ul>
				<li><span></span> <big>1.学院推荐</big>
					<table>
						<tr>
							<td>学院在校内评审奖励的基础上，按限定的名额向我厅择优推荐。各校在上报前必须对拟推荐成果在校内进行公示，公示期五天，无异议后方能上报。</td>
						</tr>
					</table></li>
				<li><span></span> <big>2.资格审查</big>
					<table>
						<tr>
							<td>省教育厅根据本《办法》的规定对各校申报成果进行资格审查。资格审查未通过的成果不提交评审。</td>
						</tr>
					</table></li>
				<li><span></span> <big>3.专家评审</big>
					<table>
						<tr>
							<td>省教育厅聘请有关专家组成湖北省大学生优秀科研成果评审委员会，负责评审工作。评审标准按《湖北省大学生优秀科研成果评审标准》（修订）的规定执行。</td>
						</tr>
					</table></li>
				<li><span></span> <big>4.成果公示</big>
					<table>
						<tr>
							<td>拟授奖的成果对外公示十天，接收异议。</td>
						</tr>
					</table></li>
				<li><span></span> <big>5.异议处理</big>
					<table>
						<tr>
							<td>对拟授奖成果提出异议，必须采用书面形式，写明成果名称和异议内容。</td>
						</tr>
					</table></li>
				<li><big>6.批准公布</big>
					<table>
						<tr>
							<td>经公示无异议，或异议裁定同意的成果由省教育厅行文公布并颁发获奖证书。</td>
						</tr>
					</table></li>
			</ul>
		</div>



		<!------------------评奖标准---------------------------->
		<div class="awardStandardBox">
			<span class="title"></span>
			<p class="smalltitle">大学生优秀科研成果奖设一、二、三等三个奖励等级，特别突出的可授予特等奖</p>
			<div class="content">
				<img src="${ctxImg}/achievement3.png" />
				<div class="right">
					<ul>
						<li><span>1</span>
							<p>经省级以上部门鉴定具有国内先进水平或自主开发有知识产权的科学技术成果。</p></li>
						<li><span>2</span>
							<p>向社会转让、出售，被生产部门应用并产生较大经济效益的技术成果；被社会有关部门采用并产生较大经济效益或
								社会效益的软科学研究成果。</p></li>
						<li><span>3</span>
							<p>参加并在国际专业学术会议上交流或在国家权威学术刊物(核心期刊或学科专业领域公认的权威性学术期刊)上发表的，理论上有一定创新或发展的学术论文。</p>
						</li>
						<li><span>4</span>
							<p>主管部门确认授予并运用于生产实际，产生一定经济效益的技术专利成果。</p></li>
						<li><span>5</span>
							<p>公开出版且产生较好影响的中长篇文学作品(包括小说、电影、电视剧本、报告文学等)或个人文学作品集。</p></li>
					</ul>
					<ul>
						<li><span>1</span>
							<p>经有关业务部门鉴定或奖励并有较高水平的科学技术成果。</p></li>
						<li><span>2</span>
							<p>被有关部门采用、有—定经济效益的技术成果或有一定成效的软科学研究成果。</p></li>
						<li><span>3</span>
							<p>在全国性专业学术会议上宣读或在省级以上公开出版的学术刊物上发表的高水平学术论文。</p></li>
						<li><span>4</span>
							<p>主管部门确认授予并有一定创新性和实用性的技术专利成果。</p></li>
						<li><span>5</span>
							<p>公开发表的中长篇文学作品或公开发表且产生较大影响的短篇文学作品。</p></li>
					</ul>
					<ul>
						<li><span>1</span>
							<p>有一定水平和价值的科学技术成果。</p></li>
						<li><span>2</span>
							<p>在专业学术会议上交流的或在公开出版的刊物(不包括增刊、论文集)上发表的有一定影响的学术论文。</p></li>
						<li><span>3</span>
							<p>在省级以上刊物(不包括增刊、论文集)公开发表的短篇文学作品。</p></li>
						<li><span>4</span>
							<p>产生一定社会影响的社会调查报告。</p></li>
						<li><span>5</span>
							<p>其他有关可视为评定对象和有一定价值的科研成果。</p></li>
					</ul>
				</div>
			</div>
		</div>


		<!------------------奖励办法---------------------->
		<div class="rewardMethod">
			<span class="title"></span>
			<div class="content">
				<p>由学院发文予以表彰。</p>
				<p>对特别优秀的科研成果,学院将推荐参加湖北省大学生优秀科研成果奖的评选。</p>
				<p>其他奖励按各学院制定的奖励方案执行。</p>
			</div>
		</div>
	</div>


	<!-------------周边装饰图----------------->
	<img class="decorationpic5" src="${ctxImg}/achievement5.png" />
	<img class="decorationpic6" src="${ctxImg}/achievement6.png" />
	<img class="decorationpic7" src="${ctxImg}/achievement7.png" />
	<img class="decorationpic8" src="${ctxImg}/achievement8.png" />
	<img class="decorationpic9" src="${ctxImg}/achievement9.png" />
	<img class="decorationpic10" src="${ctxImg}/achievement10.png" />
	<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>
</body>
</html>
