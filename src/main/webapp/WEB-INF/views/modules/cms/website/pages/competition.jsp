<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="cyjd-site-default"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/competition.css"/>
</head>

<body>
<!--顶部logo及导航，共用部分-->

<!--------banner图-------->
<div class="bannerBox">
    <%--<div class="container container-ct">--%>
    <%--<div class="edit-bar clearfix" style="width:1270px;margin:60px auto 0;">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<div class="mybreadcrumbs" style="margin:0 0 35px 9px;">--%>
                <%--<i class="icon-home" aria-hidden="true"></i>&nbsp;&nbsp;<a href="/f" style="color:#333;text-decoration: underline;">首页</a>&nbsp;&gt;&nbsp;双创大赛--%>
            <%--</div>--%>
        <%--</div>--%>
    <%--</div>--%>
        <ol class="breadcrumb" style="margin-top: 0;width:1270px;margin:60px auto 0;">
            <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
            <li class="active">双创大赛</li>
        </ol>
        <%--</div>--%>
    <%--<div class="banner">--%>
    <%--<img src="${ctxImg}/competition1.png"/>--%>
    <%--</div>--%>
    <%----%>
    <img class="decorationPic7" src="${ctxImg}/competition7.png"/>
    <!----------banner下面的二级导航-------->
    <div class="secondNav">
        <ul>
            <li class="current">第二届“互联网”+大赛</li>
            <li>2017年创青春大赛</li>
            <li>2017年挑战杯大赛</li>
            <li>十七届蓝杯大赛</li>
        </ul>
    </div>
</div>

<div class="grayBgBox" style="background-color: #fff;">
    <div class="midBox" style="background-color: #fff;padding-top: 0;">

        <!---------互联网大赛时间轴--------->
        <img class="procedureMap" src="${ctxImg}/competition2_03.png" alt=""/>
        <!--------大赛详情表单部分------------->
        <div class="tableBox">
            <table>
                <tr>
                    <td colspan="6">“互联网+”大学生创新创业大赛</td>
                </tr>
                <tr>
                    <td>
                        <p class="tal">报名</p>
                        <p class="tar">(开始时间)</p>
                    </td>
                    <td>
                        <p class="tal">报名</p>
                        <p class="tar">(结束时间)</p>
                    </td>
                    <td>推荐专业</td>
                    <td>参赛对象</td>
                    <td>参赛形式</td>
                    <td>地区</td>
                </tr>
                <tr>
                    <td width="162">2016年3月起</td>
                    <td width="194">不得晚于2016年8月31日</td>
                    <td width="189">不限</td>
                    <td width="262">普通高等学院在校生（可为本专科生、研究生、不含在职生），或毕业5年以内的毕业生，各组别不同，详见正文</td>
                    <td width="280">团队</td>
                    <td>全国</td>
                </tr>
                <tr>
                    <td>报名费</td>
                    <td colspan="3">组织机构</td>
                    <td>参赛方法</td>
                    <td>举办次数</td>
                </tr>
                <tr>
                    <td>不详（官网无说明）</td>
                    <td colspan="3">
                        <p>
                            1、主办单位：教育部、中央网络安全和信息化领导小组办公室、国家发展和改革委员会、工业和信息化部、人力资源和社会保障部、国家知识产权局、中国科学院、中国工程院、共青团中央和湖北省人民政府</p>
                        <p>2、承办单位：华中科技大学</p>
                        <p>3、大赛组委会：教育部部长袁贵仁和湖北省省长王国生担任主任，有关部门负责人作为成员</p>
                        <p>4、协办单位：中国高校创新创业教育联盟、中国高校创新创业投资联盟、中国教育电视台、光明校园传媒</p>
                        <p>5、其它说明：各省（区、市）可根据实际成立相应的机构，负责本地初赛和复赛的组织实施、项目评审和推荐等工作。</p>
                    </td>
                    <td>官网报名或大赛APP报名或大赛微信公众号报名</td>
                    <td>第2届</td>
                </tr>
                <tr>
                    <td>竞赛类别</td>
                    <td>大赛官方网址</td>
                    <td colspan="4">大赛奖励</td>
                </tr>
                <tr>
                    <td>A级（国家级）</td>
                    <td>cy.ncss.org.cn</td>
                    <td colspan="4">
                        <p>1、大赛设30个金奖、90个银奖、480个铜奖。设最佳创意奖、最具商业价值奖、最佳带动就业奖、最具人气奖各1个。获奖项目颁发获奖证书，提供投融资对接、落地孵化等服务</p>
                        <p>2、设高校集体奖20个、省市优秀组织奖10个和优秀创新创业导师若干名，颁发获奖证书及奖牌</p>
                    </td>
                </tr>
            </table>
        </div>
        <img class="shadowPic" src="${ctxImg}/competition4_06.png"/>

        <!--------大赛简介------------->
        <div class="birefBox">
            <p class="title">
                <span></span>
                大赛简介
            </p>
            <p class="content">
                为贯彻落实《国务院办公厅关于深化高等学院创新创业教育改革的实施意见》（国办发〔2015〕36号），进一步激发高校学生创新创业热情，展示高校创新创业教育成果，搭建大学生创新创业项目与社会投资对接平台，定于2016年3月至10月举办第二届中国“互联网+”大学生创新创业大赛。</p>
            <p class="content">
                第二届中国“互联网+”大学生创新创业大赛旨在深化高等教育综合改革，激发大学生的创造力，培养造就“大众创业、万众创新”的生力军；推动赛事成果转化和产学研用紧密结合，促进“互联网+”新业态形成，服务经济提质增效升级；以创新引领创业、创业带动就业，推动高校毕业生更高质量创业就业，重在把大赛作为深化创新创业教育改革的重要抓手，引导各地各高校主动服务创新驱动发展战略，积极开展教学改革探索，把创新创业教育融入人才培养，切实提高高校学生的创新精神、创业意识和创新创业能力。</p>
            <img src="${ctxImg}/competition5.png"/>
        </div>

        <!----------------参赛组别参与对象说明------------>
        <div class="discriptionBox">
            <p class="title">
                <span></span>
                参赛组别参与对象说明
            </p>
            <ul>
                <li>
                    <div class="cell">
                        <div class="mark">
                            <span>创意组</span>
                        </div>
                        <p>参赛项目具有较好的创意和较为成型的产品原型或服务模式，但尚未完成工商登记注册。参赛申报人须为团队负责人，须为普通高等学院在校生（可为本专科生、研究生，不含在职生）</p>
                    </div>
                </li>
                <li>
                    <div class="cell">
                        <div class="mark">
                            <span>初创组</span>
                        </div>
                        <p>
                            参赛项目工商登记注册未满3年（2013年3月1日后注册），且获机构或个人股权投资不超过1轮次。参赛申报人须为企业法人代表，须为普通高等学院在校生（可为本专科生、研究生，不含在职生），或毕业5年以内的毕业生（2011年6月10日之后毕业）</p>
                    </div>
                </li>
                <li>
                    <div class="cell">
                        <div class="mark">
                            <span>成长组</span>
                        </div>
                        <p>
                            参赛项目工商登记注册3年以上（2013年3月1日前注册）；或工商登记注册未满3年（2013年3月1日后注册），且获机构或个人股权投资2轮次以上（含2轮次）。参赛申报人须为企业法人代表，须为普通高等学院在校生（可为本专科生、研究生，不含在职生），或毕业5年以内的毕业生（2011年6月10日之后毕业）
                        </p>
                    </div>
                </li>
            </ul>
        </div>

        <!----------------参赛项目类型------------>
        <div class="itemTypeBox">
            <p class="title">
                <span></span>
                参赛项目类型
            </p>
            <ul>
                <li><span>1</span><a>“互联网+”现代农业，包括农林牧渔等</a></li>
                <li><span>2</span><a>“互联网+”制造业，包括智能硬件、先进制造、工业自动化、生物医药、节能环保、新材料、军工等</a></li>
                <li><span>3</span><a>“互联网+”信息技术服务，包括工具软件、社交网络、媒体门户、数字娱乐、企业服务等</a></li>
                <li><span>4</span><a>“互联网+”商务服务，包括电子商务、消费生活、金融、旅游户外、房产家居、高效物流等</a></li>
                <li><span>5</span><a>“互联网+”公共服务，包括教育文化、医疗健康、交通、人力资源服务等</a></li>
                <li><span>6</span><a>“互联网+”公益创业，以社会价值为导向的非盈利性创业</a></li>
            </ul>
        </div>

        <!----------------参赛项目要求------------>
        <div class="itemRequestBox">
            <p class="title">
                <span></span>
                参赛项目要求
            </p>
            <ul>
                <li>1、参赛项目须真实、健康、合法，无任何不良信息</li>
                <li>2、参赛项目不得侵犯他人知识产权</li>
                <li>3、所涉及的发明创造、专利技术、资源等必须拥有清晰合法的知识产权或物权</li>
                <li>4、抄袭、盗用、提供虚假材料或违反相关法律法规一经发现即刻丧失参赛相关权利并自负一切法律责任</li>
                <li>5、参赛项目涉及他人知识产权的，报名时需提交完整的具有法律效力的所有人书面授权许可书、专利证书等</li>
                <li>6、已完成工商登记注册的创业项目，报名时需提交单位概况、法定代表人情况、股权结构、组织机构代码复印件等相关证明材料</li>
            </ul>
        </div>

    </div>
</div>


<img class="decorationPic8" src="${ctxImg}/competition8.png"/>
<img class="decorationPic9" src="${ctxImg}/competition9.png"/>
<img class="decorationPic10" src="${ctxImg}/competition10.png"/>
<img class="decorationPic11" src="${ctxImg}/competition11.png"/>


</body>

</html>