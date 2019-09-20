<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8"/>
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" href="${ctxCommon}/common-css/common.css"/>
    <link rel="stylesheet" href="${ctxCommon}/common-css/bootstrap.min.css"/>
    <link rel="stylesheet" href="${ctxCss}/msStyle.css"/> <!--名师讲堂样式文件-->
    <link rel="stylesheet" href="${ctxOther}/icofont/iconfont.css">  <!--图标样式-->
    <link rel="stylesheet" href="${ctxOther}/pages/jquery.page.css"> <!-- 分页样式 -->
    <title>${frontTitle}</title>
</head>

<body>
<div class="container" style="margin-top: 40px;">
    <div class="containerinfo">
        <div class="navigation">
            <ul>
                <li><img src="${ctxImg}/bc_home.png" id="shouye"></li>
                <li>
                    <a href="${ctxFront}/">首页></a>
                </li>
                <li>名师讲堂</li>
            </ul>
        </div>
        <div class="clearfix"></div>
        <div class="margintop">
            <h3>课程视频</h3>
            <div class="navborder fl" style="width: 600px; height: 0;"></div>
            <div class="redcolor fl" style="width: 17px; height: 9px; margin-top: 38px;"></div>
        </div>
        <div style="clear: both;"></div>
        <div class="article">
            <div class="articleone">
                <a href="${ctxFront}/">
                    <img src="${ctxImg}/msjt_info.jpg">
                </a>
                <div class="articleoneinfo">
                    <ul>
                        <li>课程名称:计算机科学和Python编程导论</li>
                        <li>课程简介:本课程是两部分课程的上半部分:即 计算机科学与Python编程导论及计算思维和
                            数据科学导论。两部分课程旨在帮助以前从未接触过计算机科学或编程的人们学会计算思维、编写程序解决问题
                        </li>
                        <li>授课教师:常键</li>
                        <li>
                            <i class="iconfont">&#xe609;</i>发布于2005-08-16 &nbsp;<i class="iconfont">&#xe61c;</i> 5
                            &nbsp; <i class="iconfont">&#xe60b;</i> 2 &nbsp; <i class="iconfont">&#xe622;</i> 30
                            <span style="float: right;"><img src="${ctxImg}/upload.jpg"> 课程下载</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="articleone">
                <a href="#">
                    <img src="${ctxImg}/msjt_info01.jpg">
                </a>
                <div class="articleoneinfo">
                    <ul>
                        <li>课程名称:创 ●课十讲:下一只独角兽的摇篮课</li>
                        <li>课程简介:创青春《"创课"十讲》采访了十多位有代表性的创业者和创业导师,从《自我修炼》等十个主题与大家分享穿创业路上的经历 故事 思考与洞见，让创业不再孤独,让创业不再盲目!
                        </li>
                        <li>授课教师:陈小宇 &nbsp;副教授</li>
                        <li>
                            <i class="iconfont">&#xe609;</i>发布于2005-08-16 &nbsp;<i class="iconfont">&#xe61c;</i> 5
                            &nbsp; <i class="iconfont">&#xe60b;</i> 2 &nbsp; <i class="iconfont">&#xe622;</i> 30
                            <span style="float: right;"><img src="${ctxImg}/upload.jpg"> 课程下载</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="articleone">
                <a href="#">
                    <img src="${ctxImg}/msjt_info04.jpg">
                </a>
                <div class="articleoneinfo">
                    <ul>
                        <li>课程名称:创新创业心理学</li>
                        <li>课程简介:创新的灵感从何而来？为什么创新需要心理学?创业者在创业维艰的过程中，如何保持一颗积极 乐观 坚韧 自信坦然的心态？
                        </li>
                        <li>授课教师:沈显君&nbsp;副教授</li>
                        <li>
                            <i class="iconfont">&#xe609;</i>发布于2005-08-16 &nbsp;<i class="iconfont">&#xe61c;</i> 5
                            &nbsp; <i class="iconfont">&#xe60b;</i> 2 &nbsp; <i class="iconfont">&#xe622;</i> 30
                            <span style="float: right;"><img src="${ctxImg}/upload.jpg"> 课程下载</span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="articleone">
                <a href="#">
                    <img src="${ctxImg}/msjt_info05.jpg">
                </a>
                <div class="articleoneinfo">
                    <ul>
                        <li>课程名称:项目管理</li>
                        <li>课程简介:课程系统梳理了项目管理的历史脉络与发展情况,归纳了总结了成功项目管理的四个方面和八大要素
                        </li>
                        <li>授课教师:田元 &nbsp;优秀教师</li>
                        <li>
                            <i class="iconfont">&#xe609;</i>发布于2005-08-16 &nbsp;<i class="iconfont">&#xe61c;</i> 5
                            &nbsp; <i class="iconfont">&#xe60b;</i> 2 &nbsp; <i class="iconfont">&#xe622;</i> 30
                            <span style="float: right;"><img src="${ctxImg}/upload.jpg"> 课程下载</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="articleone">
                <a href="#">
                    <img src="${ctxImg}/msjt_info06.jpg">
                </a>
                <div class="articleoneinfo">
                    <ul>
                        <li>课程名称:人体科学:下一只独角兽的摇篮课</li>
                        <li>课程简介:人体科学从人体结构到生涯,阐述机体的功能机制及其所需所能;常见病因病理。作为通识课程,旨在让你了解自己,懂得善待自己与家人,健康生活。
                        </li>
                        <li>授课教师:陈小宇 副教授</li>
                        <li>
                            <i class="iconfont">&#xe609;</i>发布于2005-08-16 &nbsp;<i class="iconfont">&#xe61c;</i> 5
                            &nbsp; <i class="iconfont">&#xe60b;</i> 2 &nbsp; <i class="iconfont">&#xe622;</i> 30
                            <span style="float: right;"><img src="${ctxImg}/upload.jpg"> 课程下载</span>
                        </li>
                    </ul>
                </div>
            </div>
            <div class="articleone">
                <a href="#">
                    <img src="${ctxImg}/mxjt_info03.jpg">
                </a>
                <div class="articleoneinfo">
                    <ul>
                        <li>课程名称:微观经济学原理</li>
                        <li>课程简介:经济是人类的头等大事.人类发明了许多解决经济问题的社会机制。微观经济学通过分析市场机制对经济主体的激励和协调会让你更好地理解我们生活的世界
                        </li>
                        <li>授课教师:沈显君 副教授</li>
                        <li>
                            <i class="iconfont">&#xe609;</i>发布于2005-08-16 &nbsp;<i class="iconfont">&#xe61c;</i> 5
                            &nbsp; <i class="iconfont">&#xe60b;</i> 2 &nbsp; <i class="iconfont">&#xe622;</i> 30
                            <span style="float: right;"><img src="${ctxImg}/upload.jpg"> 课程下载</span>
                        </li>
                    </ul>
                </div>
            </div>

            <div class="change_page clearfix">
                <div id="page">

                </div>
            </div>
        </div>
    </div>
    <div class="rightinfo">
        <div class="search_input clearfix">
            <div class="input">
                <input type="text" placeholder='关键字输入...'/>
                <i class="iconfont ico_search icon-sousuo-sousuo"></i>
            </div>
        </div>
        <div class="kclass">
            <h4>课程分类</h4>
            <div class="border"></div>
        </div>
        <!--专业课程-->
        <div class="kclassinfo">
            <h5>专业课程分类</h5>
            <div class="fenlei">
                <input id="lessonType" type="hidden" value="计算机,物理">
                <ul>
                    <li class="all-type">
                        <a href="#">全部</a>
                    </li>
                    <li>
                        <a href="#">计算机</a>
                    </li>
                    <li>
                        <a href="#">经营</a>
                    </li>
                    <li>
                        <a href="#">物理</a>
                    </li>
                    <li>
                        <a href="#">化学</a>
                    </li>
                    <li>
                        <a href="#">生命科学</a>
                    </li>
                    <li>
                        <a href="#">数学</a>
                    </li>
                    <li>
                        <a href="#">英语</a>
                    </li>
                    <li>
                        <a href="#">艺术</a>
                    </li>
                    <li>
                        <a href="#">历史</a>
                    </li>
                    <li>
                        <a href="#">社会科学</a>
                    </li>
                    <li>
                        <a href="#">教育</a>
                    </li>
                    <li>
                        <a href="#">文化</a>
                    </li>
                    <li>
                        <a href="#">公共管理</a>
                    </li>
                    <li>
                        <a href="#">设计</a>
                    </li>
                </ul>
            </div>
        </div>
        <!--专业课程结束-->
        <!--课程类型分类-->
        <div class="kclassinfoone">
            <h5>课程类型分类</h5>
            <div class="fenleione">
                <ul>
                    <li>
                        <a href="#">全部</a>
                    </li>
                    <li>
                        <a href="#">基础课</a>
                    </li>
                    <li>
                        <a href="#">核心课</a>
                    </li>
                    <li>
                        <a href="#">实战课</a>
                    </li>
                </ul>
            </div>
        </div>
        <!--课程类型分类结束-->
        <!--状态分类-->
        <div class="kclassinfotwo">
            <h5>状态分类</h5>
            <div class="fenleitwo">
                <ul>
                    <li>
                        <a href="#">全部</a>
                    </li>
                    <li>
                        <a href="#">即将开课</a>
                    </li>
                    <li>
                        <a href="#">已开课</a>
                    </li>
                    <li>
                        <a href="#">已结课</a>
                    </li>
                </ul>
            </div>
        </div>
        <!--结束状态分类-->
        <!--名师-->
        <div class="teacherfamous">
            <h4>名师讲堂</h4>
            <div class="teacherfamousone clearfix">
                <div class="fl">
                    <img src="${ctxImg}/msteacher04.png">
                </div>
                <div class="fl">
                    <p>常建 教授</p>
                    <p>华中师范大学</p>
                </div>
            </div>

            <div class="teacherfamousone clearfix">
                <div class="fl">
                    <img src="${ctxImg}/msteacher03.png">
                </div>
                <div class="fl">
                    <p>陈小宇 副教授</p>
                    <p>华中师范大学</p>
                </div>
            </div>

            <div class="teacherfamousone clearfix">
                <div class="fl">
                    <img src="${ctxImg}/mstecher02.png">
                </div>
                <div class="fl">
                    <p>于雷 优秀讲师</p>
                    <p>华中师范大学</p>
                </div>
            </div>

            <div class="teacherfamousone clearfix">
                <div class="fl">
                    <img src="${ctxImg}/msteacher01.png">
                </div>
                <div class="fl">
                    <p>沈显军 副教授</p>
                    <p>华中师范大学</p>
                </div>
            </div>

            <div class="teacherfamousone clearfix">
                <div class="fl">
                    <img src="${ctxImg}/msteacher.png">
                </div>
                <div class="fl">
                    <p>田元 优秀讲师</p>
                    <p>华中师范大学</p>
                </div>
            </div>
        </div>
    </div>
    <!--结束名师-->
</div>
</div>
<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>
<script src="${ctxOther}/pages/jquery.page.js"></script>
<script>
    $("#page").Page({
        totalPages: 10, //分页总数
        liNums: 7, //分页的数字按钮数(建议取奇数)
        activeClass: 'activP', //active 类样式定义
        callBack: function (page) {
            //console.log(page)
        }
    });

    $.fn.chooseTag = function (option) {
        var config = $.extend({}, option);
        var $target = $(config.target);
        var $els = $(config.el);
        var val = $target.val();
        var tags = val.split(',');
        var $allCheck = $(config.allCheck);
        var allCheckClass = config.allCheck.substring(1);

        $.each(tags, function (i, tag) {
            var text;
            var $notActiveTags;
            if (tag == '全部') {
                $els.addClass('active')
            } else {
                $notActiveTags = $els.not('.active');
                $notActiveTags.each(function (i, li) {
                    var $li = $(li);
                    text = $.trim($li.text());
                    if (tag === text) {
                        $li.addClass('active');
                    }
                })
            }
        });

        $allCheck.find('a').on('click', function () {
            if ($(this).parent().hasClass('active')) {
                $els.removeClass('active')
            } else {
                $els.addClass('active')
            }
        });

        $(document).on('click', config.el + '>a', function (e) {
            e.preventDefault();
            var $li = $(this).parent();
            if (!$li.hasClass(allCheckClass)) {
                $(this).parent().toggleClass('active');
                if ($els.not('.active').hasClass(allCheckClass) && $els.not('.active').size() == 1) {
                    $els.not('.active').addClass('active')
                } else {
                    $els.parent().find('.' + allCheckClass).removeClass('active')
                }
               var arr = setTargetVal($els.parent().find('.active'),false);
                $target.val(arr.join(','));
            }
        });

        function setTargetVal(els,isAll){
            var arr = [];
            els.each(function(i,el){
                arr.push($.trim($(el).text()))
            });
            return arr;
        }
    };

    $(document).chooseTag({
        el: '.fenlei li',
        target: '#lessonType',
        allCheck: '.all-type'
    })

</script>

</body>

</html>