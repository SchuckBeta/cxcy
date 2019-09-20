<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/teacherGrace.css">
    <title>${frontTitle}</title>

</head>
<body>

<div class="main-content"  style="background-color: #f6f6f6">
    <div class="banner-sec" style="background-image:url('/img/banner-teacher.jpg');"></div>
    <div class="container">
        <div class="text-center">
            <div class="title-bar title-bar-show">
                <h3>导师风采</h3>
            </div>
        </div>
        <div class="title-bar title-bar-ts">
            <span class="triangle"></span>
            <!-- <h4>企业导师</h4> -->
            <h4>校内导师</h4>
        </div>
        <div class="slider-teacher slider-teacher-company">
            <div class="slider-tc-wrapper">
                <div class="stc-item">
                    <div class="row">
                        <div class="col-md-3 col-lg-2 col-md-push-9 col-lg-push-10">
                            <div class="pic-teacher-block">
                                <img class="img-responsive" src="${ctxImg}/tg-daiweifen.jpg" alt="戴伟芬">
                            </div>
                            <p class="name-teacher text-center"><span>戴伟芬</span></p>
                        </div>
                        <div class="col-md-9 col-lg-10 col-md-pull-3 col-lg-pull-2">
                            <ul class="info-list info-pill">
                                <li class="pill"><span>年龄：</span><span>性别：女</span></li>
                                <li>毕业学院：华中师范学院</li>
                                <li class="pill">学位：博士</li>
                                <li class="pill">职称：副教授</li>
                                <li class="pill">学位：博士，硕士</li>
                                <li class="pill">
                                    主要经历：教育部人文社科2011年度青年基金项目“基于专业取向的美国教师教育课程思想研究”（11YJC880015）
                                    　　全国教育科学“十二五”规划2011年度教育部重点“美国社区学院兼职教师的专业发展及启示——基于“双师型”教师培养的视角”（GJA114021）
                                    　　湖北省教育科学“十二五”规划2011年度专项资助重点“免费师范生在教师教育课程中的学习经验与成效研究”（2011B096）
                                    　　湖北省教育厅人文社科2011年度指导性项目“推进大学生健康发展——闲暇教育探讨”（405）
                                    　　中央高校基本科研业务费专项资金资助“当代美国教师教育课程思想的发展及其启示”（2010DG027）
                                    　　湖北省基础教育研究中心“启航”项目“我国免费师范生课程设置研究”（11QH16 0012）
                                    　　参与哥伦比亚大学教育学院Goodwin教授 《新教师入职培养项目》（获联邦政府资助975万美元）
                                    　　参与教育部[2007]重大研究项目“教育公平研究”子项目“国际教育公平比较研究”
                                </li>
                                <li class="pill">荣获奖项及证书：<br>
                                    1、论文《论美国教师教育课程的学术整合取向》获湖北省第七次优秀高等教育研究成果二等奖 <br>
                                    2、011年10月在“第一届全球教师教育峰会”被授予“田家炳-教师研究青年学者”称号 <br>
                                    3、新华社特约分析师，曾编写《新标准与评价：美国教师教育课程的改革》，《美国高质量中小学教师的培训》等约10篇美国教师教育前沿报告<br>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- <div class="title-bar title-bar-ts">
            <span class="triangle"></span>
            <h4>校园导师</h4>
        </div> -->
        <div class="slider-teacher slider-teacher-school">
            <div class="slider-tc-wrapper">
                <div class="stc-item">
                    <div class="row">
                        <div class="col-md-6">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <img class="img-responsive" src="${ctxImg}/tg-xiongchun.jpg" alt="熊淳">
                                    </div>
                                    <div class="ti-content">
                                        <p>国家社科基金青年项目“非洲基础教育援助策略的国际比较研究”（课题编号CDA110113）。
                                            　　湖北省教育科学“十二五”规划2011年度专向资助重点课题“日本义务教育学院布局调整政策研究：经验与启示”（课题编号2011A098）。
                                            　　2010年华中师范大学基本科研业务费专项资金项目 “非洲基础教育外援的国际比较研究”（课题编号2010DG025）。
                                            　　湖北省基础教育启航课题项目“非洲基础教育均衡发展的困境与脱困路径之研究”（课题编号11QH004）。
                                            　　华中师范大学混合式教学模式研究立项“教师专业发展”。
                                            　　华中师范大学2012年校级教学研究培育项目“高校免费师范生课堂教学中的教育情感研究”。</p>
                                    </div>
                                </div>
                                <div class="ti-side">
                                    <span class="name">熊淳</span>
                                    <div class="keywords">
                                        <p>主要研究方向：国际教育开发援助、教育现象学、教育情感、教师专业发展与日本教育问题</p>
                                        <p>关键字：<span>国际教育开发</span><span>教育现象学</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <img class="img-responsive" src="${ctxImg}/tg-gaowei.jpg" alt="高巍">
                                    </div>
                                    <div class="ti-content">
                                        <p>2005年毕业于华中师范大学教育学院，获得教育学学士学位；随后继续在华中师范大学教育学院攻读研究生学位，2007年，获得课程与教学论专业硕士学位，2008年至2009年，在美国德克萨斯大学奥斯汀分校（The university of Texas at Austin）教育学院访问学习，从事有效教学及教师评价标准研究；2010年，获得教育学博士学位。</p>
                                    </div>
                                </div>
                                <div class="ti-side">
                                    <span class="name">高巍</span>
                                    <div class="keywords">
                                        <p>主要研究方向：课程与教学的基本原理</p>
                                        <p>关键字：<span>课程与教学的基本原理</span><span>教育研究与实验</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <img class="img-responsive" src="${ctxImg}/tg-tangbin.jpg" alt="唐斌">
                                    </div>
                                    <div class="ti-content">
                                        <p>2008年华中师范大学教育学院，获教育学博士学位。副教授、华中师范大学高等教育改革与发展研究中心副主任。曾先后参编《教育财务与成本管理》、《走进义务教育新时代》等著作3部，发表论文10余篇，参与数项省部级课题研究，主持全国教育科学“十一五”规划国家青年基金课题一项。</p>
                                    </div>
                                </div>
                                <div class="ti-side">
                                    <span class="name">唐斌</span>
                                    <div class="keywords">
                                        <p>主要研究方向：教育经济学与教育财政学的教学</p>
                                        <p>关键字：<span>教育经济学</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <img class="img-responsive" src="${ctxImg}/tg-xiajiafa.jpg" alt="夏家发">
                                    </div>
                                    <div class="ti-content">
                                        <p>现任华中师范大学教育学院副教授，语文课程教学论硕士研究生导师。主要社会兼职有：中国教育学会小学语文专业委员会理事，中国教育学会教学论专业委员会理事，湖北省教育学会小学语文专业委员会副理事长，武汉市教育学会小学语文专业委员会顾问，教育部国家级小学语文骨干教师培训专家，湖北省级中小学语文骨干教师培训专家，湖北省农村教师素质提高工程培训专家，武汉市江岸区教育发展咨询专家，武汉市崇仁路小学、东方红小学等多所名校驻校专家。</p>
                                    </div>
                                </div>
                                <div class="ti-side">
                                    <span class="name">夏家发</span>
                                    <div class="keywords">
                                        <p>主要研究方向：文化产业研究</p>
                                        <p>关键字：<span>教学研究室语文教研</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <img class="img-responsive" src="${ctxImg}/tg-guoqingyang.jpg" alt="郭清扬">
                                    </div>
                                    <div class="ti-content">
                                        <p>2008年7月留校任教以来，主要从事教育经济学与教育财政学的教学和科研工作。近年来在《教育研究》、《中国社会科学文摘》、《教育发展研究》、《教育与经济》、《中国社会科学报》、《华中师范大学学报》（人文社科版）、《河北师范大学学报》（教育科学版）、《教育发展评论》等刊物上公开发表论文十余篇，其中数篇被《新华文摘》、《中国社会科学文摘》、《高等学院文科学术文摘》、人大报刊复印资料等杂志全文或部分转载。参编的著作包括：《教育经济学》（中国人民大学出版社2006年版）、《中国中西部地区农村中小学合理布局结构研究》（中国社会科学出版社2009年版）和《农村中小学布局调整与教学点建设》（人民教育出版社2011年版）等</p>
                                    </div>
                                </div>
                                <div class="ti-side">
                                    <span class="name">郭清扬</span>
                                    <div class="keywords">
                                        <p>主要研究方向：教育经济学</p>
                                        <p>关键字：<span>教育经济学与教育财政学</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6">
                            <div class="teacher-intro">
                                <div class="ti-main">
                                    <div class="pic-teacher-box">
                                        <i></i>
                                        <img class="img-responsive" src="${ctxImg}/tg-caiyingqi.jpg" alt="蔡迎旗">
                                    </div>
                                    <div class="ti-content">
                                        <p>
                                            1990年毕业于华中师范大学教育系学前教育专业，获教育学学士学位；1994年毕业于华中师范大学教育科学学院教育学原理专业，获教育学硕士学位；2005年毕业于北京师范大学教育学院学前教育专业，获教育学博士学位。2006年12月～2009年6月，在武汉大学国家社会保障研究中心（基地）从事公共管理专业社会保障方向的博士后研究工作，专攻儿童福利。2010年1月～2011年1月，于美国佐治亚大学（The University of Georgia）教育学院访学一年，主攻学前教育管理、政策与财政。</p>
                                    </div>
                                </div>
                                <div class="ti-side">
                                    <span class="name">蔡迎旗</span>
                                    <div class="keywords">
                                        <p>主要研究方向：教育经济、管理与政策、学前教育基本理论</p>
                                        <p>关键字：<span>教育经济</span></p>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
</body>
</html>
