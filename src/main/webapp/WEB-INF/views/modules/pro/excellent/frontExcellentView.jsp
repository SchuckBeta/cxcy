<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
    <script src="${ctxJs}/frontCyjd/noticeList.js"></script>
    <script src="${ctxJs}/frontCyjd/commentList.js"></script>
    <script src="${ctxJs}/frontCyjd/textareaComment.js"></script>
</head>
<body>
<div class="container">
    <div class="excellent-comment-box">
        <p style="font-size: 16px;">发表评论</p>
        <div id="textareaComment" class="textarea-comment">
            <div class="textarea-font-family el-textarea el-input--mini">
                <textarea rows="5" placeholder="最多500字" class="el-textarea__inner" style="min-height: 30px;"></textarea>
            </div>
            <div class="text-right" style="margin: 12px 0px;">
                <button type="button" class="el-button el-button--default el-button--mini reset"><span>重置</span>
                </button>
                <button type="button" class="el-button el-button--primary el-button--mini is-disabled btn-submit"><span>发表评论</span>
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
                            <div id="tab-first" aria-controls="pane-first" role="tab" aria-selected="true" tabindex="0"
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
<div id="alertBoxSuccess" style="display: none">
    <div role="alert" class="el-message el-message--success" style="z-index: 2001;"><i
            class="el-message__icon el-icon-success"></i>
        <p class="el-message__content">发表成功,待审核</p><!----></div>
</div>
<div id="alertBoxError" style="display: none">
    <div role="alert" class="el-message el-message--error" style="z-index: 2001;"><i
            class="el-message__icon el-icon-error"></i>
        <p class="el-message__content"></p><!----></div>
</div>


<script>


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
        getCommentSuccess: function (option) {
            elPagination.setPageCount(option.pageCount)
            elPagination.setPageNo(option.pageNo);
            var $elTabsActive = $('.el-tabs__item.is-active');
            var width;
            $elTabsActive.find('.count').text('('+option.pageCount+')')
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
        if(!$(this).attr('translate-x')){
            var $elTabsItems = $('.el-tabs__nav .el-tabs__item');
            var itemIdx = $elTabsItems.index($(this));
            var $beforeItems = $elTabsItems.slice(0, itemIdx);
            var translateX = 0;
            $beforeItems.each(function (idx, item) {
                translateX  += ($(item).innerWidth() + 20);
            })
            $(this).attr('translate-x',  translateX + 'px')
        }
        $(this).parent().find('.el-tabs__active-bar').css({
            'transform': 'translateX('+$(this).attr('translate-x')+')',
            'width': $(this).width()
        })



        commentList = new CommentList('#' + controls, {
            userId: '${fns:getUser().id}',
            ftpHttp: '${fns: ftpHttpUrl()}',
            type: controls.indexOf('first') > -1 ? '1' : '2',
            getCommentSuccess: function (option) {
                eLSelect.setPageSize(option.pageSize);
                elPagination.setPageSize(option.pageSize)
                elPagination.setPageCount(option.pageCount)
                elPagination.setPageNo(option.pageNo);
                $_self.find('.count').text('('+option.pageCount+')')
            }
        });
        $('#' + controls).show().siblings().hide().children().remove();
    })


    new TextareaComment('#textareaComment', {
        userId: '${fns:getUser().id}',
        change: function (event, value) {
            var self = this;
            axios({
                method: 'POST',
                url: '/cms/cmsConmment/ajaxSave',
                data: {
                    category: {id: '10031'},
                    cnt: {id: '10001'},
                    content: value
                }
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.reset();
                    $('#alertBoxSuccess').show();
                    setTimeout(function () {
                        $('#alertBoxSuccess').hide();
                    }, 1500)
                } else {
                    $('#alertBoxError').show().find('.el-message__content').text(data.msg)
                    setTimeout(function () {
                        $('#alertBoxError').hide();
                    }, 1500)
                }

            }).catch(function () {

            })
        }
    });


</script>


</body>
</html>