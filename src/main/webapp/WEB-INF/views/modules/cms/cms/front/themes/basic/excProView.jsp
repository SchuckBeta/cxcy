<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <script src="${ctxJs}/frontCyjd/noticeList.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/frontCyjd/commentList.js?version=${fns: getVevison()}"></script>
    <script src="${ctxJs}/frontCyjd/textareaComment.js?version=${fns: getVevison()}"></script>
</head>
<body>
<div id="app" class="pdt-60">
    <div class="container">
        <div aria-label="Breadcrumb" role="navigation" class="el-breadcrumb">
        <span class="el-breadcrumb__item">
            <span role="link" class="el-breadcrumb__inner">
                <a href="/f"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
            </span>
            <i class="el-breadcrumb__separator el-icon-arrow-right"></i>
        </span>
            <%-- <span class="el-breadcrumb__item">
             <span role="link" class="el-breadcrumb__inner">
                 <a href="#">优秀项目列表</a>
             </span>
             <i class="el-breadcrumb__separator el-icon-arrow-right"></i>
         </span>--%>
            <span class="el-breadcrumb__item">
            <span role="link" class="el-breadcrumb__inner">
                ${article.cmsCategory.name}
            </span>
            <i class="el-breadcrumb__separator el-icon-arrow-right"></i>
        </span>
        </div>
    </div>
    <div class="container">
        <div class="excpro-view-header">
            <h5 class="title">
                <c:if test="${article.isshowlink == 1}">
						<span><a href="http://${article.link}" target="_blank">
                                ${article.projectName}
                        </a></span>
                </c:if>
                <c:if test="${article.isshowlink != 1}">
						<span>${article.projectName}
                        </span>
                </c:if>
            </h5>
            <div class="source-date"><span>项目来源：${article.sourceName}</span><span>发表时间：<span
                    class="date"><fmt:formatDate value="${article.articlepulishDate}"
                                                 pattern="yyyy-MM-dd"/></span></span></div>
            <div class="excpro-info">
                <div class="excpro-pic">
                    <img src="${not empty article.thumbnail ? fns:ftpImgUrl(article.thumbnail) : '/img/video-default.jpg'}">
                </div>
                <div class="excpro-info-side">
                    <p class="excpro-office">学院：<span>${article.collgeName}</span></p>
                    <div class="teachers-item">指导老师：${article.tnames}</div>
                    <div class="actions-item">浏览量：<span class="action-num">${article.views}</span>点赞数：<span
                            class="action-num">${article.cmsArticleData.likes}</span>评论：<span
                            class="action-num">${commentCounts}</span></div>


                    <div class="excpro-tags-item">
                        关键字：
                        <c:forEach items="${keywordsList}" var="keyword">
                            <c:if test="${keyword != null && keyword != ''}">
                                <span class="el-tag el-tag--info el-tag--small">${keyword}</span>
                            </c:if>
                        </c:forEach>
                    </div>


                </div>
            </div>
        </div>
    </div>
    <c:if test="${article.cmsArticleData.content eq null || article.cmsArticleData.content == ''}">
        <div class="excpro-block">
            <div class="container">
                <div class="title">项目介绍</div>
                <div class="excpro-intro">${article.projectIntroduction}</div>
            </div>
        </div>

        <div class="excpro-block">
            <div class="container">
                <div class="title">团队成员</div>
                <div class="excpro-team-member">
                    <c:forEach items="${stuList}" var="stu">
                        <div class="excpro-member">
                            <div class="excpro-member-inner">
                                <div class="member-pic">
                                    <img src="${not empty tea.photo ? fns:ftpImgUrl(tea.photo) : '/img/u4110.png'}">
                                </div>
                                <div class="member-info-items">
                                    <div class="member-info-item">姓名：<span>${stu.name}</span></div>
                                    <div class="member-info-item">学院：<span>${stu.office}</span></div>
                                    <div class="member-info-item">专业：<span>${stu.professional}</span></div>
                                    <div class="member-info-item">技术领域：<span>${stu.domain}</span></div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>


        </div>
        <div class="excpro-block">
            <div class="container">
                <div class="title">指导老师</div>
                <div class="excpro-team-member">
                    <c:forEach items="${teaList}" var="tea">
                        <div class="excpro-member">
                            <div class="excpro-member-inner">
                                <div class="member-pic">
                                    <img src="${not empty tea.photo ? fns:ftpImgUrl(tea.photo) : '/img/u4110.png'}">
                                </div>
                                <div class="member-info-items">
                                    <div class="member-info-item">姓名：<span>${tea.name}</span></div>
                                    <div class="member-info-item">学院：<span>${tea.office}</span></div>
                                    <div class="member-info-item">专业：<span>${tea.professional}</span></div>
                                    <div class="member-info-item">技术领域：<span>${tea.domain}</span></div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${article.cmsArticleData.content != null}">
        <div class="excpro-article-content" style="padding: 60px 0">
            <div class="container">
                    ${article.cmsArticleData.content}
            </div>
        </div>
    </c:if>

    <div class="container">
        <div id="actionGood" class="action-good">
            <i class="iconfont icon-dianzan1"></i>
            <span style="display: block" class="text-center good-num">${article.cmsArticleData.likes}</span>
        </div>
        <div class="base-content-container">
            <div class="recommend-block">
                <h4 class="recommend-title"><span>相关推荐</span></h4>
                <c:if test="${cmsArticleAbout.size() > 0}">
                    <ul class="recommend-list">
                        <c:forEach items="${cmsArticleAbout}" var="articleAbout">
                            <li class="recommend-item">
                                <a href="${ctx}/getOneCmsArticle?id=${articleAbout.id}"><i
                                        class="recommend-circle"></i>${articleAbout.title}</a>
                                <span class="date"><fmt:formatDate value="${articleAbout.articlepulishDate}"
                                                                   pattern="yyyy-MM-dd"/></span>
                            </li>
                        </c:forEach>
                    </ul>
                </c:if>
                <c:if test="${cmsArticleAbout.size() < 1}">
                    <p class="empty empty-color">暂无推荐</p>
                </c:if>
            </div>
        </div>
    </div>
    <c:if test="${article.cmsCategory.allowComment eq 1 && article.cmsArticleData.allowComment eq 1 }">
        <div class="container">
            <div class="excellent-comment-box">
                <p style="font-size: 16px;">发表评论</p>
                <div id="textareaComment" class="textarea-comment">
                    <div class="textarea-font-family el-textarea el-input--mini">
                        <textarea rows="5" placeholder="最多500字" class="el-textarea__inner"
                                  style="min-height: 30px;"></textarea>
                    </div>
                    <div class="text-right" style="margin: 12px 0px;">
                        <button type="button" class="el-button el-button--default el-button--mini reset"><span>重置</span>
                        </button>
                        <button type="button"
                                class="el-button el-button--primary el-button--mini is-disabled btn-submit">
                            <span>发表评论</span>
                        </button>
                    </div>
                    <div class="un-login-text-area un-login-comment" style="display: none">
                        <span>我来说两句，请先</span><a href="${ctxFront}/toLogin">登录</a>
                    </div>
                </div>
                <div class="el-tabs el-tabs--top">
                    <div class="el-tabs__header is-top">
                        <div class="el-tabs__nav-wrap is-top">
                            <div class="el-tabs__nav-scroll">
                                <div role="tablist" class="el-tabs__nav" style="transform: translateX(0px);">
                                    <div class="el-tabs__active-bar is-top"
                                         style=" transform: translateX(0px);"></div>
                                    <div id="tab-first" aria-controls="pane-first" role="tab" aria-selected="true"
                                         tabindex="0"
                                         class="el-tabs__item is-top is-active">全部评论<span class="count"></span>
                                    </div>
                                    <div id="tab-second" aria-controls="pane-second" role="tab" tabindex="-1"
                                         class="el-tabs__item is-top">我的评论<span class="count"></span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="el-tabs__content">
                        <div role="tabpanel" id="pane-first" aria-labelledby="tab-first" class="el-tab-pane">

                        </div>
                        <div role="tabpanel" aria-hidden="true" id="pane-second" aria-labelledby="tab-second"
                             class="el-tab-pane" style="display: none;">

                        </div>
                    </div>
                </div>
                <div style="margin-top: 20px;" class="text-right mgb-60">
                    <div id="elPagination" class="el-pagination is-background" size="small">
            <span class="el-pagination__sizes"><div id="elSelectJq" class="el-select"><!---->
                    <div class="el-input el-input--suffix"><!---->
                        <input type="text" autocomplete="off" placeholder="请选择" readonly="readonly"
                               class="el-input__inner">
                        <!----><span class="el-input__suffix">
                                     <span class="el-input__suffix-inner">
                                          <i class="el-select__caret el-input__icon el-icon-arrow-up"></i><!---->
                                     </span><!---->
                             </span><!---->
                    </div>
                    <div class="el-select-dropdown el-popper el-select-dropdown-transition"
                         style="display: none; min-width: 110px;overflow: hidden">
                        <div class="el-scrollbar" style="">
                            <div class="el-select-dropdown__wrap el-scrollbar__wrap el-select-dropdown-inner">
                                <ul class="el-scrollbar__view el-select-dropdown__list"><!---->
                                    <li class="el-select-dropdown__item"><span>5条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>10条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>20条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>50条/页</span></li>
                                    <li class="el-select-dropdown__item"><span>100条/页</span></li></ul>
                            </div>
                            <div class="el-scrollbar__bar is-horizontal">
                                <div class="el-scrollbar__thumb" style="transform: translateX(0%);"></div>
                            </div>
                            <div class="el-scrollbar__bar is-vertical">
                                <div class="el-scrollbar__thumb" style="transform: translateY(0%);"></div>
                            </div>
                        </div>
                        <!----></div>
                </div></span>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <div id="alertBox" style="display: none">
        <div tabindex="-1" role="dialog" aria-modal="true" aria-label="提示" class="el-message-box__wrapper"
             style="z-index: 2003;">
            <div class="el-message-box">
                <div class="el-message-box__header">
                    <div class="el-message-box__title"><!----><span>提示</span></div><!----></div>
                <div class="el-message-box__content">
                    <div class="el-message-box__status el-icon-warning"></div>
                    <div class="el-message-box__message"><p>请先登录</p></div>
                    <div class="el-message-box__input" style="display: none;">
                        <div class="el-input"><!----><input type="text" autocomplete="off" placeholder=""
                                                            class="el-input__inner"><!----><!----><!----></div>
                        <div class="el-message-box__errormsg" style="visibility: hidden;"></div>
                    </div>
                </div>
                <div class="el-message-box__btns"><!---->
                    <button type="button" class="el-button el-button--default el-button--small el-button--primary "
                            onclick="location.href = '/f/toLogin'"><!---->
                        <!----><span>
          登录
        </span></button>
                </div>
            </div>
        </div>
        <div class="v-modal" tabindex="0" style="z-index: 2002;"></div>
    </div>
    <%--<div id="alertBoxSuccess" style="display: none">--%>
        <%--<div role="alert" class="el-message el-message--success" style="z-index: 2001;"><i--%>
                <%--class="el-message__icon el-icon-success"></i>--%>
            <%--<p class="el-message__content">发表成功,待审核</p><!----></div>--%>
    <%--</div>--%>
    <%--<div id="alertBoxError" style="display: none">--%>
        <%--<div role="alert" class="el-message el-message--error" style="z-index: 2001;"><i--%>
                <%--class="el-message__icon el-icon-error"></i>--%>
            <%--<p class="el-message__content"></p><!----></div>--%>
    <%--</div>--%>
</div>
<script>
    var ctxImagesComment = '${ctxImages}';
    $('#actionGood .iconfont').on('click', function (event) {
        event.stopPropagation();
        event.preventDefault();
        var goodNum = parseInt($(this).next().text()) || 0;
        var $_this = $(this);
        if ($(this).hasClass('good')) {
            return false;
        }
        axios.get('/cmsArticleLikes?contentId=${article.id}&likes=' + (goodNum + 1)).then(function (response) {
            var data = response.data;
            if (data.status == '1') {
                goodNum += 1;
                $_this.next().text(goodNum)
                $_this.addClass('good')
            } else {
                ELEMENT.Message({
                    type: 'error',
                    message: '点赞失败'
                })
//                $('#alertBoxError').show().find('.el-message__content').text('点赞失败')
//                setTimeout(function () {
//                    $('#alertBoxError').hide();
//                }, 1500)
            }

        }).catch(function (error) {
//            $('#alertBoxError').show().find('.el-message__content').text('点赞失败')
//            setTimeout(function () {
//                $('#alertBoxError').hide();
//            }, 1500)
        })
    })

    var elPagination = new ElPagination('#elPagination', {
        pageNo: 1,
        pageSize: 10,
        count: 0,
        pageChange: function (value) {
            commentList.setPageNo(value);
            commentList.getCommentList();
        }
    });

    var eLSelect = new ELSelect('#elSelectJq', {
        pageSize: 10,
        changePageSize: function (value) {
            elPagination.setPageSize(value)
            commentList.setPageSize(value);
            commentList.getCommentList();
        }
    })


    var commentList = new CommentList('#pane-first', {
        userId: '${fns:getUser().id}',
        ftpHttp: '${fns: ftpHttpUrl()}',
        cntId: '${article.id}',
        getCommentSuccess: function (option) {
            elPagination.setPageCount(option.pageCount)
            elPagination.setPageNo(option.pageNo);
            var $elTabsActive = $('.el-tabs__item.is-active');
            var width;
            $elTabsActive.find('.count').text('(' + option.pageCount + ')')
            width = $elTabsActive.width();
            $elTabsActive.attr('translate-x', 0)
            $('.el-tabs__active-bar').width(width)
        }
    });

    $('.el-tabs__nav').on('click', '.el-tabs__item', function (event) {
        event.stopPropagation();
        event.preventDefault();
        var $_self = $(this);
        if ($(this).hasClass('is-active')) {
            return false;
        }
        var controls = $(this).attr('aria-controls');
        $(this).addClass('is-active').siblings().removeClass('is-active');
        if (!$(this).attr('translate-x')) {
            var $elTabsItems = $('.el-tabs__nav .el-tabs__item');
            var itemIdx = $elTabsItems.index($(this));
            var $beforeItems = $elTabsItems.slice(0, itemIdx);
            var translateX = 0;
            $beforeItems.each(function (idx, item) {
                translateX += ($(item).innerWidth() + 20);
            })
            $(this).attr('translate-x', translateX + 'px')
        }
        $(this).parent().find('.el-tabs__active-bar').css({
            'transform': 'translateX(' + $(this).attr('translate-x') + ')',
            'width': $(this).width()
        })


        commentList = new CommentList('#' + controls, {
            userId: '${fns:getUser().id}',
            ftpHttp: '${fns: ftpHttpUrl()}',
            cntId: '${article.id}',
            type: controls.indexOf('first') > -1 ? '1' : '2',
            getCommentSuccess: function (option) {
                eLSelect.setPageSize(option.pageSize);
                elPagination.setPageSize(option.pageSize)
                elPagination.setPageCount(option.pageCount)
                elPagination.setPageNo(option.pageNo);
                $_self.find('.count').text('(' + option.pageCount + ')')
            }
        });
        $('#' + controls).show().siblings().hide().children().remove();
    })


    new TextareaComment('#textareaComment', {
        userId: '${fns:getUser().id}',
        isAudit: ${article.cmsCategory.isAudit},
        change: function (event, value) {
            var self = this;
            axios({
                method: 'POST',
                url: '/cms/cmsConmment/ajaxSave',
                data: {
                    category: {id: '${article.categoryId}'},
                    cnt: {id: '${article.id}'},
                    content: value
                }
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.reset();
//                    $('#alertBoxSuccess').show();
//                    setTimeout(function () {
//                        $('#alertBoxSuccess').hide();
//                    }, 1500)
                    ELEMENT.Message({
                        type: 'success',
                        message: self.options.isAudit == '1' ? '发表成功，待审核' : '发表成功'
                    })
                    if(self.options.isAudit != '1'){
                        commentList.getCommentList();
                    }
                } else {
                    ELEMENT.Message({
                        type: 'error',
                        message: data.msg
                    })
//                    $('#alertBoxError').show().find('.el-message__content').text(data.msg)
//                    setTimeout(function () {
//                        $('#alertBoxError').hide();
//                    }, 1500)
                }

            }).catch(function () {

            })
        }
    });


</script>
</body>
</html>
