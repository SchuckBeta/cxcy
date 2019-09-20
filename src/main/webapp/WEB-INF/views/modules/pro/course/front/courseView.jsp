<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <meta name="decorator" content="creative"/>
    <title>${frontTitle}</title>
    <script src="${ctxJs}/frontCyjd/noticeList.js"></script>
    <script src="${ctxJs}/frontCyjd/commentList.js"></script>
    <script src="${ctxJs}/frontCyjd/textareaComment.js"></script>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none;" class="container page-container mgb-60">
    <el-breadcrumb class="mgb-20" separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item><a href="${ctxFront}/course/frontCourseList">${cmsCourse.modelname}</a></el-breadcrumb-item>
        <el-breadcrumb-item>课程详情</el-breadcrumb-item>
    </el-breadcrumb>

    <div class="course-content">
        <h3 class="course-title">
            ${course.name}
        </h3>
        <div class="course-info-bar">
            <span>课程专业：<i v-if="course.categoryList" v-for="(item, index) in course.categoryList">{{item.label}}<template
                    v-if="index != course.categoryList.length - 1">、</template></i></span>
            <span>课程类型：{{course.type | selectedFilter(courseTypeEntries)}}</span>
            <span>课程状态：{{course.status | selectedFilter(courseStatusEntries)}}</span>
            <span>发布时间：{{course.publishDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <el-row class="course-details mgb-20" :gutter="30">
            <el-col :span="10">
                <div class="course-video">
                    <video v-if="course.video" :poster="course.coverImg | ftpHttpFilter(ftpHttp)"
                           style="width: 100%; height: 100%" controls>
                        <source :src="course.video | ftpHttpFilter(ftpHttp)">
                    </video>
                    <img v-else :src="course.coverImg | ftpHttpFilter(ftpHttp)">
                </div>
            </el-col>
            <el-col :span="14">
                <div class="course-descirption">
                    <e-col-item label="课程描述：" label-width="74px" class="white-space-pre-static word-break"><span v-html="course.description"></span>
                    </e-col-item>
                </div>
                <div class="course-teachers">
                    <e-col-item label="课程导师：" label-width="74px">
                        <span v-if="course.teacherList" class="teacher-item" v-for="item in course.teacherList">
                            {{item.teacherName}}<span class="college-name"
                                                      v-if="item.collegeName">{{item.collegeName}}</span>
                        </span>
                        <span v-else class="empty-color">暂无课程导师</span>
                    </e-col-item>
                </div>
                <div class="course-actions">
                    <el-dropdown v-if="course.attachmentList" trigger="click" size="mini" @command="downCourse">
                        <el-button type="primary" size="mini">
                            下载课件<i class="el-icon-arrow-down el-icon--right"></i>
                        </el-button>
                        <el-dropdown-menu slot="dropdown">
                            <el-dropdown-item v-for="item in course.attachmentList" :command="item"
                                              :key="item.id">
                                {{item.name | textEllipsis(20)}}
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                    <span v-else class="empty-color">暂无课件</span>
                    <span class="course-downloads" v-if="course.attachmentList">下载数（{{course.downloads}}）</span>
                    <div class="course-action-good" :class="{liked: checkLiked}">
                        <i class="iconfont icon-dianzan1" @click.stop.prevent="handleChangeLikes"></i>
                        <span class="course-good-num">（{{course.likes}}）</span>
                    </div>
                </div>
            </el-col>
        </el-row>
    </div>

    <el-tabs v-model="activeTabName" class="mgb-20 course-tabs">
        <el-tab-pane label="课程介绍" name="courseFirst">
            <div class="course-tab-content white-space-pre">${course.courseSummary}</div>
        </el-tab-pane>
        <el-tab-pane label="导师介绍" name="courseSecond">
            <div class="course-tab-content white-space-pre">${course.teacherSummary}</div>
        </el-tab-pane>
    </el-tabs>
    <div class="user_detail-title-handler">
        <div class="ud-row-title"><span class="name">推荐课程</span></div>
    </div>
    <el-carousel class="carouse-course" indicator-position="none" height="166px" :interval="5000" arrow="always">
        <el-carousel-item v-for="(itemCourseList, index) in rcCourseListSix" :key="index">
            <div class="carouse-course-inner">
                <el-row :gutter="20">
                    <el-col :span="4" v-for="item in itemCourseList" :key="item.id">
                        <div class="course-item">
                            <div class="course-item-pic">
                                <a :href="frontOrAdmin + '/course/view?id=' + item.id">
                                    <img :src="item.coverImg | ftpHttpFilter(ftpHttp) | proGConPicFilter">
                                </a>
                            </div>
                            <h5 class="course-item-title">
                                <a :href="frontOrAdmin + '/course/view?id=' + item.id" :title="item.name">
                                    {{item.name}}
                                </a>
                            </h5>
                        </div>
                    </el-col>
                </el-row>
            </div>
        </el-carousel-item>
    </el-carousel>

    <c:if test="${article.cmsCategory.allowComment eq 1 && article.cmsArticleData.allowComment eq 1 }">
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
                <div class="el-tabs el-tabs--top el-tabs-comment">
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


</div>
</div>


<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var recommendCourseList = JSON.parse(JSON.stringify(${fns: toJson(courseList)}));
            var course = JSON.parse(JSON.stringify(${fns: toJson(course)}));
            var userId = '${fns:getUser().id}';
            var courseStatus = JSON.parse('${fns: toJson(fns: getDictList('0000000082'))}');
            var courseTypes = JSON.parse('${fns: toJson(fns: getDictList('0000000078'))}');

            return {
                activeTabName: 'courseFirst',
                recommendCourseList: recommendCourseList,
                course: course,
                userId: userId,
                courseStatus: courseStatus,
                courseTypes: courseTypes,
                uuid: '',
                checkLiked: false
            }
        },
        computed: {
            rcCourseListSix: function () {
                var spliceIndex = 6;
                var nList = [];
                var recommendCourseList = this.recommendCourseList;
                for (var i = 0; i < Math.ceil(recommendCourseList.length / 6); i++) {
                    var item = recommendCourseList.slice(i * spliceIndex, (i + 1) * spliceIndex);
                    nList.push(item);
                }
                return nList;
            },
            courseStatusEntries: function () {
                return this.getEntries(this.courseStatus)
            },
            courseTypeEntries: function () {
                return this.getEntries(this.courseTypes)
            }
        },
        methods: {
            downCourse: function (value) {
                var self = this;
                if (!this.userId) {
                    this.$alert('请登录后下载课件，是否登录？', '提示', {
                        confirmButtonText: '确定',
                        type: 'warning'
                    }).then(function () {
                        location.href = self.frontOrAdmin + '/login'
                    }).catch(function () {

                    });
                    return;
                }
                location.href = this.frontOrAdmin + '/course/downLoad?id=' + this.course.id + '&url=' + value.url + '&fileName=' + value.name;
                var downloads = parseInt(this.course.downloads) || 0;
                downloads += 1;
                this.course.downloads = downloads;
            },

            handleChangeLikes: function () {
                var self = this;
                if (!this.userId) {
                    this.$message({
                        type: 'error',
                        message: '请先登录，然后点赞'
                    });
                    return;
                }
                if(this.checkLiked){
                    return false;
                }
                var likes = parseInt(this.course.likes) || 0;
                this.$axios.post('/interactive/sysLikes/save', {
                    foreignId: this.course.id,
                    foreignType: '2',
                    token: this.uuid
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret == '1') {
                        likes += 1;
                        self.course.likes = likes;
                        self.checkLiked = true;
                    }
                    self.$message({
                        type: data.ret == '1' ? 'success' : 'error',
                        message: data.msg
                    })
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: self.xhrErrorMsg
                    })
                })
            },

            getCheckLiked: function () {
                var self = this;
                this.$axios.get('/interactive/sysComment/checkLiked?' + Object.toURLSearchParams({
                            foreignId: this.course.id,
                            foreignType: '2',
                            token: this.uuid
                        })).then(function (response) {
                    if (response.data) {
                        self.checkLiked = response.data.isExistsLike == '1';
                    }
                })
            },

            getUUid: function () {
                var self = this;
                this.$axios.get('/interactive/sysLikes/uuid').then(function (response) {
                    self.uuid = response.data;
                    self.getCheckLiked();
                })
            }
        },
        created: function () {
            this.getUUid();

        }
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
            var $elTabsActive = $('.el-tabs-comment .el-tabs__item.is-active');
            var width;
            $elTabsActive.find('.count').text('('+option.pageCount+')')
            width = $elTabsActive.width();
            $elTabsActive.attr('translate-x', 0)
            $('.el-tabs__active-bar').width(width)
        }
    });

    $('.el-tabs-comment').on('click', '.el-tabs__item', function (event) {
        event.stopPropagation();
        event.preventDefault();
        var $_self = $(this);
        if ($(this).hasClass('is-active')) {
            return false;
        }
        var controls = $(this).attr('aria-controls');
        $(this).addClass('is-active').siblings().removeClass('is-active');
        if(!$(this).attr('translate-x')){
            var $elTabsItems = $('.el-tabs-comment .el-tabs__nav .el-tabs__item');
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
            cntId: '${article.id}',
            type: controls.indexOf('first') > -1 ? '1' : '2',
            getCommentSuccess: function (option) {
                console.log(option)
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
                    category: {id: '${article.categoryId}'},
                    cnt: {id: '${article.id}'},
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