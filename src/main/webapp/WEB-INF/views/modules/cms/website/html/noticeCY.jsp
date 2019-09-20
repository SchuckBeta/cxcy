<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<html>

<head>
    <meta charset="utf-8"/>
    <!--头部导航公用样式-->
    <!--公用重置样式文件-->
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <meta name="decorator" content="site-decorator"/>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/html/gxxmsb.css"/>
    <title>${frontTitle}</title>
</head>
<body>
<div class="container">
    <div>
        <h3 class="text-center" style="font-size: 24px; margin-top: 60px;letter-spacing: 3px;">
            关于组织申报2018年院级</h3>
        <h4 style="letter-spacing: 3px;text-align: center;    margin-top: 27px;">大学生创新创业项目的通知</h4>
        <h4 style="margin-top: 34px;margin-bottom: 30px; color: #D8D8D8; font-size: 14px;text-align: center">
            <span class="puber">作者：陈章惠</span><span class="bm">资讯来源：创新创业学院 </span><span class="pubtime">发布时间：2017-12-04 20:52:38</span><span
                class="fwl">点击数量:</span><span class="num" id="newsNum">310</span>
        </h4>
    </div>
    <div class="con" id="contentdisplay" style="padding:55px 30px 30px;border: 1px solid #ddd">
        <div class="xwcon" id="xwcontentdisplay"><p><b><span
                style="font-size: 10.5pt; font-weight: bold; line-height: 200%">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 院属各单位：</span></b>
        </p>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">为激发大学生的创造性思维，培养富有创新精神、勇于投身实践的创业人才，特设立广州铁路职业技术学院大学生创新创业项目(以下简称“创新创业项目”)。现将2018年院级大学生创新创业项目申报事项通知如下：</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><b><span
                    style="font-size: 10.5pt; font-weight: bold; line-height: 200%">一、项目类别</span></b></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">创新创业项目分创新训练、创业训练、创业实践三种类型。</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(一)创新训练项目：是学生个人或团队在教师指导下，自主完成创新性研究项目设计、研究报告撰写、研究条件准备等工作，重点考察调研实践活动和创新技术的理论研究成果。执行期限为一年。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(二)创业训练项目：是学生团队在教师指导下，提出一项具有市场前景的创新性产品或者服务，并通过扮演公司运营角色，开展编制商业计划书、参加企业实践、模拟企业运行、撰写创业策划书等实践，重点考察参与创业实践的活动过程。执行期限为一年。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(三)创业实践项目：是学生团队在教师指导下，提出一项具有市场应用前景的创新性产品或者服务，并通过成立公司开展创业实践，重点考察创业实践运营成果和效益。执行期限为两年。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><b><span
                    style="font-size: 10.5pt; font-weight: bold; line-height: 200%">二、项目申报条件与经费</span></b></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(一)创新训练项目</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">面向在校大一和大二年级的学生或团队申报。支持经费为1万元/项。</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(二)创业训练项目</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">面向全体在校学生申报，项目团队成员不少于3人。支持经费为1.5万元/项。</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(三)创业实践项目</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">面向全体在校学生申报，项目团队成员不少于5人。优先支持已注册公司的学生团队申报，项目负责人为企业法人代表。支持经费为2万元/项。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><b><span
                    style="font-size: 10.5pt; font-weight: bold; line-height: 200%">三、项目管理与验收</span></b></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(一)严格按照《广州铁路职业技术学院大学生创新创业项目管理办法(试行)》(附件4)执行；</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(二)除提交结题验收报告外，还应提交以下标志性成果材料：</span></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">1.创新训练项目：提交1份研究期间完成的、与项目内容相关的高水平研究报告和1篇以上研究论文(指导教师可作为第一作者)。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">2.创业训练项目：提交1份含项目设计、生产、销售、运营和风险投资等内容在内的创业策划书，同时提交1份以上与创业项目相关的专利或软件著作权证书。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">3.创业实践项目：提交1份创业项目的营业执照和1份以上与创业项目相关的专利或软件著作权证书，同时提交1套企业运行佐证材料(含场地租赁合同、场地建设过程、企业运行记录等)。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><b><span
                    style="font-size: 10.5pt; font-weight: bold; line-height: 200%">四、注意事项</span></b></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(一)每名学生只能申报主持或参加一个项目，未结题国家、省、市和院级创新创业项目的负责人，原则上不得申报新的项目。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(二)创新创业项目申请人须选择校内教师作为指导教师。指导教师要全程跟踪项目执行情况，指导学生完成研究训练内容。指导教师应具有中级以上职称或硕士以上学位，每年新增指导项目不得超过2项，每个项目的指导教师人数不超过2人。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(三)同等条件下，具备以下条件的项目优先立项：跨学科、跨院系或跨年级合作项目；项目申请人有创业培训经历(如：SYB、GYB和创业实训指导课等)的项目；项目申请人参加过创新创业比赛、创新创业训练营、学科竞赛、实践调研的项目。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">(四)</span><span style="font-size: 10.5pt">&nbsp;2018年度拟择优立项13项，其中</span><span
                    style="font-size: 10.5pt; line-height: 200%">创新训练项目2项，创业训练项目5项，创业实践项目6项。项目经费为市财政经费，将一次性拨付，须年度内使用完毕，不得下年度结转。</span>
            </div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><b><span
                    style="font-size: 10.5pt; font-weight: bold; line-height: 200%">五、材料报送</span></b></div>
            <div style="text-align: left; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt"><span
                    style="font-size: 10.5pt; line-height: 200%">申请人填写《广州铁路职业技术学院大学生创新创业项目申请书》(附件1-3)，指导教师签署意见并经各二级学院审核后，于12月15日16:00前将纸质材料一式一份送至创新创业学院，同时提交电子版（联系人：陈章惠，电话：86020533</span><span
                    style="font-size: 10.5pt; color: rgb(0,0,0); line-height: 200%"></a>）</span></div>

            <div style="text-align: right; margin: 3.75pt 0pt; line-height: 200%; text-indent: 21pt" align="right"><span
                    style="font-size: 10.5pt; line-height: 200%">创新创业学院</span></div>
            <div style="text-align: right; margin: 3.75pt 0pt 0pt; line-height: 200%; text-indent: 21pt" align="right">
                <span style="font-size: 10.5pt; line-height: 200%">2017年12月4日</span></div>
            <div style="margin: 0pt">&nbsp;</div>
        </div>
    </div>
</div>
<%--<script type="text/javascript" src="${ctxCommon}/common-js/jquery.min.js"></script>--%>
</body>

</html>