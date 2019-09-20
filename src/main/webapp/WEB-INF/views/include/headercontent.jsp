<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!--------顶部------->
<div class="top">
    <ul>
        <li><a href="${ctx}/logout"><i class="icon-signout"></i> 退出</a></li>
        <li><a href="${ctx}/sysMenuIndex"><i class="icon-reply-all"></i> 返回首页 </a></li>
        <li><a href="#"><i class="icon-user"></i> ${fns:getUser().name}</a></li>
        <!-- <li><span></span><a href="#">提醒通知</a></li> -->
    </ul>
</div>
<div id="dialog-message" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/notify.css?v=1">
<div id="notifyModule" class="footer-notify-container" style="display: block;">
    <div class="notify-wrap">
        <div class="notify-header clearfix">
            <span>消息内容<span class="no-readbar">（<span class="no-read-num"></span>条）</span></span>
            <a href="javascript:void(0);" class="notify-close">
                <img src="${ctxImages}/notice-close-black.png">
            </a>
            <div class="open-scale">

                <a href="javascript:void(0);" class="open"><img src="${ctxImages}/scale-up-black.png"></a>
                <a href="javascript:void(0);" class="scale"><img src="${ctxImages}/scale-down-black.png"></a>
            </div>
            <!-- <p class="no-read-tip"><span class="no-read-num"></span>条未读消息</p> -->
        </div>
        <div class="notify-body">
            <div class="carousel carousel-notify-generic">
                <div class="carousel-inner" role="listbox"></div>
            </div>
        </div>
        <div class="notify-footer">
            <div class="notify-btns text-center" style="display: none;">
                <a class="btn-prev disabled" href="javascript:void (0);">上一条</a>
                <a class="btn-next" href="javascript:void (0);">下一条</a>
            </div>
            <div class="notify-know text-center" style="">
                <a class="btn-know" href="javascript:void (0);">我知道了</a>
            </div>
        </div>
    </div>
</div>
<div id="dialogCyjdFooter" class="dialog-cyjd"></div>
<script type="text/javascript">
    (function (name, definition) {
        var hasDefine = typeof define === 'function';
        var hasExports = typeof module !== 'undefined' && module.exports;
        if (hasDefine) {
            define(definition)
        } else if (hasExports) {
            module.exports = definition()
        } else {
            window[name] = definition()
        }
    })('notifyModule', function () {
        function NotifyModule() {

            this.user = localStorage.getItem('userId');
            this.list = null;
            this.notifyModule = $('#notifyModule');
            this.carouselInner = this.notifyModule.find('.carousel-inner');
            this.notifyFooter = this.notifyModule.find('.notify-footer');
            this.btnScale = this.notifyModule.find('.scale');
            this.btnOpen = this.notifyModule.find('.open');
            this.btnClose = this.notifyModule.find('.notify-close');
            this.unRead = this.notifyModule.find('.no-read-num');
            this.btnView = this.notifyModule.find('.btn-view');
            this.btnAccept = this.notifyModule.find('.btn-accept');
            this.btnRefuse = this.notifyModule.find('.btn-refuse');
            this.carouselNotify = this.notifyModule.find('.carousel-notify-generic');
            this.btnPrev = this.notifyModule.find('.btn-prev');
            this.btnNext = this.notifyModule.find('.btn-next');
            this.notifyKnow = this.notifyModule.find('.notify-know')
            this.btnKnow = this.notifyModule.find('.notify-know').find('.btn-know')
            this.notifyBtns = this.notifyModule.find('.notify-btns')
            this.unReadNum = 0;


            this.btnScale.on('click', $.proxy(this.toggleOpenScale, this));
            this.btnOpen.on('click', $.proxy(this.toggleOpenScale, this));
            this.btnClose.on('click', $.proxy(this.closeNotify, this));
            this.notifyModule.on('click', '.btn-view', $.proxy(this.openView, this));
            this.notifyModule.on('click', '.btn-accept', $.proxy(this.accept, this));
            this.notifyModule.on('click', '.btn-refuse', $.proxy(this.refuse, this));
            this.notifyModule.on('click', '.btn-prev', $.proxy(this.prev, this));
            this.notifyModule.on('click', '.btn-next', $.proxy(this.next, this));
            this.notifyModule.on('click', '.btn-know', $.proxy(this.iRead, this));


            this.init();
        }

        NotifyModule.prototype.init = function () {
            var user = this.user;
            var self = this;
            var html;
            if ($("#notifyShow_27218e9429b04f06bb54fee0a2948350").val()) {
                resetNotifyShow();
                sessionStorage.notifyShow = null;
            }
            var isNotifyShow = sessionStorage.notifyShow;
            if (isNotifyShow == "ok") {
                this.destroy();
            } else {
                this.getUnReadOaNotify().then(function () {
                    var xhr;
                    if (self.list.length > 0 && self.list != null) {
                        if (self.list.length < 2) {
                            self.notifyBtns.hide();
                            self.notifyKnow.show();
                        } else {
                            self.notifyBtns.show();
                            self.notifyKnow.hide();
                        }
                        html = self.tplListHtml(self.list);
                        self.carouselInner.empty().append($(html));
                        self.unReadNum = self.list.length;
                        self.unRead.text(self.unReadNum);
                        self.carouselNotify.carousel({
                            interval: false
                        });
                        self.notifyModule.show();
//                            xhr = $.post('/f/closeButton', {send_id: self.list[0].notifyId});
//                            xhr.success(function (res) {
//                                if (res == 1) {
//                                    self.carouselNotify.find('.item').addClass('read')
//                                }
//                            });
//                            xhr.error(function (err) {
//                                console.log(err)
//                            })
                    } else {
                        self.destroy();
                    }
                });
                this.carouselNotify.on('slid.bs.carousel', $.proxy(this.carouselSlide, this));
            }
//                else {
//                    self.notifyModule.hide();
//                }
//            }
        };

        NotifyModule.prototype.updateItem = function (data) {
            sessionStorage.notifyShow = null;
            var itemHtml = [];
            var $items = this.carouselInner.children();
            var content = data.content;
            var btns = data.btns;


            itemHtml.push('<div data-notify-id="' + data.notifyId + '" class="item text-center"><div class="cell"><div class="msg-box">' + content + '</div>');
            if (btns && btns.length > 0) {
                itemHtml.push('<div class="text-center" style="font-size: 0"><div class="btn-group">');
                $.each(btns, function (i, item) {
                    itemHtml.push('<button type="button" type="button" class="btn btn-sm" onClick="' + item.click + '">' + item.name + '</button>');
                });
                itemHtml.push('</div></div>')
            }
            itemHtml.push('</div></div>');


            if ($items.size() > 0) {
                $(itemHtml.join('')).insertBefore($items.eq(0));
            } else {
                this.carouselInner.append(itemHtml.join('\n'));
            }
            this.carouselInner.children().eq(0).addClass('active').siblings().removeClass('active');
            this.notifyModule.show();
            if (this.carouselInner.children().size() < 2) {
                this.notifyBtns.hide();
                this.notifyKnow.show();
            } else {
                this.notifyBtns.show();
                this.notifyKnow.hide();
            }
            var $nItems = this.carouselInner.children();
            var $active = this.carouselInner.find('.active');
            if ($nItems.index($active) == 0) {
                this.btnPrev.addClass('disabled')
                this.btnNext.removeClass('disabled')
            } else if ($nItems.index($active) == $nItems.size() - 1) {
                this.btnPrev.removeClass('disabled')
                this.btnNext.addClass('disabled')
            } else {
                this.btnPrev.removeClass('disabled')
                this.btnNext.removeClass('disabled')
            }


            if (this.carouselInner.children().size() > 1) {
                this.notifyFooter.show();
                this.carouselNotify.carousel({
                    interval: false
                });
            }


        };

        /*返回promise object*/
        NotifyModule.prototype.getUnReadOaNotify = function () {
            var xhr = $.get('/a/oa/oaNotify/unFootReadOaNotify?' + Math.random());
            var self = this;
            xhr.success(function (res) {
                if (res) {
                    self.list = res.list || [];
                    self.inOnOff = res.inOnOff;
                }
            });
            return xhr;
        };

        NotifyModule.prototype.tplListHtml = function (list) {
            var html = '';
            var strTeamName = '';
            $.each(list, function (i, item) {
                var type = item.type;
                var data = {};
                var infoTpl = '';
                if (type == 5) {
                    data['userName'] = item.sentName;
                    data['teamName'] = item.teamName;
                    data['notifyType'] = '申请加入';
                    data['projectName'] = data['teamName'];
                    data.accept = true;
                    data.refuse = true;

                    infoTpl += '<span class="user-name">' + (data.userName || '') + '</span> 申请加入' +
                            '<span class="nt-project-name">' + (data.projectName || '') + '</span> 项目团队';

                } else if (type == 6) {
                    data['teamName'] = item.teamName;
                    data['userName'] = item.sentName;
                    data['notifyType'] = '邀请你加入';
                    data['projectName'] = data['teamName'];
                    data.view = true;
                    data.accept = true;
                    data.refuse = true;
                    infoTpl += '<span class="user-name">' + (data.userName || '') + '</span> 邀请你加入' +
                            '<span class="nt-project-name">' + (data.projectName || '') + '</span>项目团队 ';
                } else if (type == 7) {
                    data.view = true;
                    infoTpl += '<span class="nt-project-name">' + (item.teamName || '') + '</span>项目团队已发布,欢迎加入我们';
                } else {
                    data['userName'] = item.sentName;
                    data['notifyType'] = '';
                    data['projectName'] = item.teamName;
                    infoTpl += item.content;
                }


                html += '<div class="item text-center ' + (i === 0 ? 'active' : '') + '" data-notify-id="' + item.notifyId + '" data-team-id="' + item.teamId + '"><div> ' +
                        '<div class="msg-box"> ' + infoTpl +
                        '</div> <div class="text-center" style="font-size: 0;">' +
                        ' <div class="btn-group" data-notify-id="' + item.notifyId + '" data-team-id="' + item.teamId + '">';
                if ((type == 5 || type == 6) && self.inOnOff === '1') {
                    html += ' <button type="button" class="btn btn-accept btn-sm" style="display: ' + (!data.accept ? 'none' : 'inline-block') + '">接受</button>' +
                            ' <button type="button" class="btn btn-refuse btn-sm" style="display: ' + (!data.refuse ? 'none' : 'inline-block') + '">拒绝</button>';
                }
                html += ' <button type="button" class="btn btn-view btn-sm" style="display: ' + (!data.view ? 'none' : 'inline-block') + '">查看详情</button>' +
                        '</div> </div></div> </div>';
            });
            return html;
        };

        NotifyModule.prototype.toggleOpenScale = function (e) {
            var self = this;
            this.notifyModule.toggleClass('notify-scale');
            return false;
        };

        NotifyModule.prototype.closeNotify = function (e) {
            sessionStorage.notifyShow = 'ok';
            this.destroy();
            return false
        };

        NotifyModule.prototype.openView = function (e) {
            var $target = $(e.target);
            var teamId = $target.parent().data('teamId');
            var notifyId = $target.parent().data('notifyId');
            var xhr = $.post('/f/closeButton', {send_id: notifyId});
            $target.prop('disabled', true);
            xhr.success(function (res) {
                changeUnreadTrForNotify(notifyId);
                if (res == 1) {
                    window.location.href = "/f/team/findByTeamId?from=notify&id=" + teamId + "&notifyId=" + notifyId;
                }
                $target.prop('disabled', false);
            });
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })

        };

        NotifyModule.prototype.readTip = function (id) {
            var xhr = $.post('/f/closeButton', {send_id: id});
            var self = this;
            xhr.success(function (res) {
                if (res == 1) {
//                    self.init();
//                    changeUnreadTrForNotify(id);
                }
            })
        }

        NotifyModule.prototype.iRead = function () {
            var self = this;
            var $active = this.carouselInner.find('.active');
            this.readedTip($active);
            this.closeNotify();
        }

        NotifyModule.prototype.accept = function (e) {
            var $target = $(e.target);
            var notifyId = $target.parent().data('notifyId');
            var xhr = $.post('/f/team/acceptInviationByNotify', {send_id: notifyId});
            var self = this;
            $target.prop('disabled', true);
            xhr.success(function (data) {
                changeUnreadTrForNotify(notifyId);
                if (data.ret != 1) {
                    dialogCyjd.createDialog(0, data.msg, {
                        id: '#dialogCyjdFooter', buttons: [{
                            'text': '确定 ',
                            class: 'btn btn-primary-oe btn-sm',
                            click: function () {
                                $(this).dialog('close')
                            }
                        }]
                    })
                }
                self.init();
                $target.prop('disabled', false);
            })
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })
        };
        NotifyModule.prototype.refuse = function (e) {
            var $target = $(e.target);
            var notifyId = $target.parent().data('notifyId');
            var xhr = $.post('/f/team/refuseInviationByNotify', {send_id: notifyId});
            var self = this;
            $target.prop('disabled', true);
            xhr.success(function (data) {
                changeUnreadTrForNotify(notifyId);
                if (data.ret != 1) {
                    dialogCyjd.createDialog(0, data.msg, {
                        id: '#dialogCyjdFooter', buttons: [{
                            'text': '确定 ',
                            class: 'btn btn-primary-oe btn-sm',
                            click: function () {
                                $(this).dialog('close')
                            }
                        }]
                    })
                }
                self.init();
                $target.prop('disabled', false);
            })
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })
        };

        NotifyModule.prototype.carouselSlide = function (e) {
            var $items = this.carouselInner.children();
            var $active = this.carouselInner.find('.active');
            if ($items.index($active) == 0) {
                this.btnPrev.addClass('disabled')
                this.btnNext.removeClass('disabled')
            } else if ($items.index($active) == $items.size() - 1) {
                this.btnPrev.removeClass('disabled')
                this.btnNext.addClass('disabled')
                if (!$active.hasClass('read')) {
                    this.readedTip($active)
                }
            } else {
                this.btnPrev.removeClass('disabled')
                this.btnNext.removeClass('disabled')
            }
            if ($active.hasClass('read')) {
                return false
            }
            this.readedTip($active.prev())
        };

        NotifyModule.prototype.readedTip = function (ele) {
            var self = this;
            var notifyId = ele.data('notifyId');
            var xhr = $.post('/f/closeButton', {send_id: notifyId});
            xhr.success(function (res) {
                if (res == 1) {
                    ele.addClass('read');
                    changeUnreadTrForNotify(notifyId);
                    self.unReadNum = self.carouselInner.children().size() - self.carouselInner.find('.read').size()
                    self.unRead.text(self.unReadNum);
                }
            });
            xhr.error(function (err) {
                console.log(err);
            })
        }

        NotifyModule.prototype.prev = function () {
            if (this.btnPrev.hasClass('disabled')) {
                return false;
            }
            this.carouselNotify.carousel('prev');

        }

        NotifyModule.prototype.next = function () {

            if (this.btnNext.hasClass('disabled')) {
                return false;
            }
            this.carouselNotify.carousel('next');
        }

        NotifyModule.prototype.destroy = function () {
//            this.btnScale.off();
//            this.btnOpen.off();
//            this.btnScale.off();
//            this.btnView.off();
            this.notifyModule.hide();
        };
        /* 自定义按钮调用事件 **************************/
        NotifyModule.prototype.acceptP = function (target, notifyId) {
            var $target = $(target);
            var xhr = $.post('/f/team/acceptInviationByNotify', {send_id: notifyId});
            var self = this;
            $target.prop('disabled', true);
            xhr.success(function (data) {
                changeUnreadTrForNotify(notifyId);
                if (data.ret != 1) {
                    dialogCyjd.createDialog(0, data.msg, {
                        id: '#dialogCyjdFooter', buttons: [{
                            'text': '确定 ',
                            class: 'btn btn-primary-oe btn-sm',
                            click: function () {
                                $(this).dialog('close')
                            }
                        }]
                    })

                }
                self.init();
                $target.prop('disabled', false);
            })
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })
        };
        NotifyModule.prototype.refuseP = function (target, notifyId) {
            var $target = $(target);
            var xhr = $.post('/f/team/refuseInviationByNotify', {send_id: notifyId});
            var self = this;
            $target.prop('disabled', true);
            xhr.success(function (data) {
                changeUnreadTrForNotify(notifyId);
                if (data.ret != 1) {
                    dialogCyjd.createDialog(0, data.msg, {
                        id: '#dialogCyjdFooter', buttons: [{
                            'text': '确定 ',
                            class: 'btn btn-primary-oe btn-sm',
                            click: function () {
                                $(this).dialog('close')
                            }
                        }]
                    })

                }
                self.init();
                $target.prop('disabled', false);
            })
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })
        };

        NotifyModule.prototype.enterApply = function (target, id) {
            var $target = $(target);
            var xhr = $.post('/f/team/refuseInviationByNotify', {send_id: notifyId});
            var self = this;
            $target.prop('disabled', true);
            xhr.success(function (data) {
                changeUnreadTrForNotify(notifyId);
                if (data.ret != 1) {
                    dialogCyjd.createDialog(0, data.msg, {
                        id: '#dialogCyjdFooter', buttons: [{
                            'text': '确定 ',
                            class: 'btn btn-primary-oe btn-sm',
                            click: function () {
                                $(this).dialog('close')
                            }
                        }]
                    })

                }
                self.init();
                $target.prop('disabled', false);
            })
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })
        };

        NotifyModule.prototype.openViewP = function (target, teamId, notifyId) {
            var $target = $(target);
            var xhr = $.post('/f/closeButton', {send_id: notifyId});
            $target.prop('disabled', true);
            xhr.success(function (res) {
                changeUnreadTrForNotify(notifyId);
                if (res == 1) {
                    window.location.href = "/f/team/findByTeamId?from=notify&id=" + teamId + "&notifyId=" + notifyId;
                }
                $target.prop('disabled', false);
            });
            xhr.error(function (err) {
                console.log(err);
                $target.prop('disabled', false);
            })

        };
        /* 自定义按钮调用事件 **************************/
        return new NotifyModule()
    });
    function resetNotifyShow() {
        $.ajax({
            type: "GET",
            url: "/f/resetNotifyShow"
        });
    }
    //修改我的消息列表状态
    function changeUnreadTrForNotify(nid) {
        getUnreadCountForHead();
        $.ajax({
            type: 'post',
            url: '/f/oa/oaNotify/getReadFlag',
            data: {
                oaNotifyId: nid
            },
            success: function (data) {
                if (data == "1") {
                    var ntr = $("tr[key_5f820f675bfe4ffbb2b18f49ae12cbfe='" + nid + "']");
                    if (ntr && ntr.length > 0) {
                        ntr.children('td').eq(5).html("已读");
                        ntr.removeClass("unreadTr");
                    }
                }
            }
        });
    }
</script>