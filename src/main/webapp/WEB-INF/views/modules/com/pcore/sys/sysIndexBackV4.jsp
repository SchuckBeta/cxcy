<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>${backgroundTitle}</title>
    <meta name="decorator" content="backPath_header"/>
    <link href="${ctxStatic}/jquery-jbox/2.3/Skins/Bootstrap/jbox.min.css" rel="stylesheet" />
    <script src="${ctxStatic}/jquery-jbox/2.3/jquery.jBox-2.3.min.js" type="text/javascript"></script>
    <style>
        html, body {
            height: 100%;
        }

        .center-content {
            position: absolute;
            top: 70px;
            bottom: 0;
            width: 100%;
            left: 0;
            overflow: hidden;
        }

        .view-panel {
            position: absolute;
            top: 0;
            right: 0;
            bottom: 0px;
            left: 200px;
        }

        .view-panel iframe {
            width: 100%;
            height: 100%;
            border: none;
            overflow: auto;
        }

        .view-copyright {
            position: absolute;
            right: 0;
            bottom: 0;
            left: 212px;
        }

        .oe-menu .submenu .total-unread-tag {
            float: right;
            display: block;
            min-width: 10px;
            padding: 3px 7px;
            margin-left: 8px;
            margin-right: 8px;
            line-height: 1;
            text-align: center;
            border-radius: 10px;
            font-size: 12px;
            font-style: normal;
            background-color: #fff;
            color: #e9432d;
            vertical-align: middle;
            text-indent: 0;
        }
    </style>
</head>
<body>
<div class="center-content">
    <a href="javascript: void (0);"></a>
    <div id="oeSideNav" class="side-nav">
        <div class="oe-page-title">
            <c:if test="${empty firstMenu.href}">
                <a href="javascript: void (0);">
                    <i class="icon-page"><img src="${ctxImg}/icon_sc.png"> </i>
                    <h3 class="title">${firstMenu.name}</h3>
                </a>
            </c:if>
            <c:if test="${not empty firstMenu.href}">
                <a href="${ctx }/${firstMenu.href}" target="mainFrame">
                    <img src="${ctxImg}/icon_sc.png">
                    <span>${firstMenu.name}</span>
                </a>
            </c:if>
        </div>
        <ul id="sub_list_wrap" role="menubar" class="oe-menu menu-vertical">
            <c:forEach items="${secondMenus}" var="menu2" varStatus="idx">
                <c:if test="${fns:checkChildMenu(menu2.id) }">
                    <li role="menuitem" aria-haspopup="true" class="submenu">
                        <div class="submenu-title">
                            <c:if test="${not empty menu2.href}">
                                <a data-link="${idx.index}-0" href="/a${menu2.href}"
                                   target="mainFrame">
                                    <i style="display: none" class="total-unread-tag"></i>
                                        ${menu2.name}
                                </a>
                            </c:if>
                            <c:if test="${empty menu2.href}">
                                <a data-link="${idx.index}"
                                   href="javascript:;">
                                    <i style="display: none" class="total-unread-tag"></i>
                                        ${menu2.name}
                                </a>
                            </c:if>
                            <i class="control-menu-transition icon-submenu-arrow el-icon-arrow-down"></i>
                        </div>
                        <div class="oe-menu-transition">
                            <ul role="menu" class="oe-menu oe-menu-inline">
                                <c:forEach items="${menu2.children}" var="menu3" varStatus="idx3">
                                    <li role="menuitem" class="oe-menu-item">
                                        <a href="/a${menu3.href}" data-link="${idx.index}-${idx3.index}"
                                                <c:if test="${fns: contains(menu3.href, 'taskAssign')}">
                                                    data-assign="assign"
                                                </c:if>
                                           target="mainFrame"><c:if
                                                test="${menu3.todoCount>0}">
                                            <i class="unread-tag">${menu3.todoCount}</i>
                                        </c:if>
                                            <c:if test="${menu3.todoCount <= 0}">
                                                <i style="display: none" class="unread-tag"></i>
                                            </c:if>${menu3.name}</a>
                                    </li>
                                </c:forEach>
                            </ul>
                        </div>
                    </li>
                </c:if>
            </c:forEach>
        </ul>
    </div>
    <div class="view-panel" id="iframe_wrap">
        <iframe id="mainFrame" name="mainFrame" frameborder="0" height="100%"></iframe>
    </div>
</div>

<script>

    var LeftSideBarMenuApp = {
                getMenuBreadcrumb: function () {
                    return [
                        {name: '节点', href:''},
                        {name: '节点2', href:''}
                    ]
                }
            }

    ;
    +function ($) {

        function MenuCollapse(element, option) {
            this.$element = $(element);
            this.options = $.extend({}, MenuCollapse.DEFAULTS, (option || {}));
            this.$submenus = this.$element.find('>.submenu'); //第一级submenu
            this.$controlMenuTransition = this.$submenus.find('.control-menu-transition');
            this.$mainFrame = $('#mainFrame');
            this.init();
        }

        MenuCollapse.DEFAULTS = {
            paddingLeft: 40,
            duration: 0.3,
            indexes: '0-0',
            currentHref: '${href}',
        }


        MenuCollapse.prototype.init = function () {
            this.controlMenuTransitionEvent();
            this.addActive();
            this.initialPadding();
            this.initialIsOpened();
        };


        MenuCollapse.prototype.initialPadding = function () {
            var options = this.options;
            var $submenus = this.$submenus;
            var paddingLeft = options.paddingLeft;
            var $submenuTitles = $submenus.find('>.submenu-title');
            var $menuitems = $submenus.find('.oe-menu-item');
            this.setPaddingLeft($submenuTitles, paddingLeft); //一级
            this.setPaddingLeft($menuitems.children(), (paddingLeft + 14)) //二级
        };

        MenuCollapse.prototype.initialIsOpened = function () {
            var options = this.options;
            var indexes = this.getIndexes();
            var duration = options.duration;
            var self = this;
            var $oeMenuTransition, $oeMenuItem, mainFrameHref;
            if (!indexes) {
                this.$mainFrame.attr('src', options.currentHref);
                return false;
            }
            $oeMenuTransition = this.$submenus.eq(indexes[0]).find('.oe-menu-transition');
            $oeMenuItem = $oeMenuTransition.find('.oe-menu-item a').eq(indexes[1]);
            mainFrameHref = this.$submenus.find('.submenu-title').eq(indexes[0]).addClass('active').find('a').attr('href');
            if (/javascript/.test(mainFrameHref)) {
                mainFrameHref = $oeMenuItem.attr('href');
                this.$mainFrame.attr('src', mainFrameHref);
                $oeMenuItem.trigger('click');
            } else {
                this.$mainFrame.attr('src', mainFrameHref)
            }
            this.setIsOpened($oeMenuTransition, $oeMenuTransition.children().height());
            setTimeout(function () {
                self.setDuration($oeMenuTransition, duration);
            })
        };

        MenuCollapse.prototype.getIndexes = function () {
            var options = this.options;
            var currentHref = options.currentHref;
            var $element = this.$element;
            var $link;
            if (!currentHref) {
                return options.indexes.split('-');
            }
            $link = $element.find('a[href="' + currentHref + '"]');
            if ($link.size() > 0) {
                return $link.attr('data-link').split('-')
            }
            return false;
        }


        MenuCollapse.prototype.setIsOpened = function (element, height) {
            element.height(height);
            element.parent().addClass('isOpened')
            element.parent().find('.control-menu-transition').removeClass('el-icon-arrow-down').addClass('el-icon-arrow-up');
        };

        MenuCollapse.prototype.setDuration = function (element, duration) {
            duration += 's';
            element.css({
                'webkitTransition': duration,
                'transition': duration
            })
        }

        MenuCollapse.prototype.controlMenuTransitionEvent = function () {
            var $controlMenuTransition = this.$controlMenuTransition;
            var duration = this.options.duration || 0.3;
            var self = this;

            $controlMenuTransition.on('click.control', function (e, param1) {
                e.stopPropagation();
                e.preventDefault();
                var $target = $(this), $submenuTitle = $target.parent(), $oeMenuTransition = $submenuTitle.next(),
                        $oeMenu = $oeMenuTransition.children(), height = $oeMenu.height();
                var $curSubmenu = $submenuTitle.parent();
                var $submenuSiblings = $curSubmenu.siblings('.isOpened');

                if ($curSubmenu.hasClass('isOpened') && !param1) {
                    self.setDuration($oeMenuTransition, duration);
                    $oeMenuTransition.height(0);
                    $target.addClass('el-icon-arrow-down').removeClass('el-icon-arrow-up');
                    $curSubmenu.removeClass('isOpened');
                    return false;
                }
                self.setDuration($oeMenuTransition, duration);
                $oeMenuTransition.height(height);
                $target.removeClass('el-icon-arrow-down').addClass('el-icon-arrow-up');
                $curSubmenu.addClass('isOpened');
                $submenuSiblings.removeClass('isOpened').find('.oe-menu-transition').height(0).end().find('.control-menu-transition').addClass('el-icon-arrow-down').removeClass('el-icon-arrow-up');


            })
        }

        MenuCollapse.prototype.onTransition = function () {
            var $oeMenuTransition = this.$element.find('.oe-menu-transition');
            var transiitonname = this.getTransitionName();
            $oeMenuTransition.on(transiitonname, function (e) {

            })
        }


        MenuCollapse.prototype.getTransitionName = function () {
            return 'webkitTransitionend' in window ? 'webkitTransition' : 'transitionend'
        }


        MenuCollapse.prototype.setPaddingLeft = function (elements, paddingLeft) {
            elements.css({
                'paddingLeft': paddingLeft + 'px'
            })
        }

        MenuCollapse.prototype.addActive = function () {
            var $element = this.$element;
            var $submenuTitles = $element.find('.submenu-title');
            var $menuItemLis = $element.find('.oe-menu-item');
            var self = this;
            this.$element.on('click.menu', 'a', function (e) {
                e.stopPropagation();
                var $parent = $(this).parent();
                var $target = $(this);
                var $controlMenuTransition = $target.next();
                if ($parent.hasClass('submenu-title')) {
                    $submenuTitles.removeClass('active');
                    $parent.addClass('active');
                    if (!(/javascript/.test($target.attr('href')))) {
                        $menuItemLis.removeClass('active');
                    }
                    $controlMenuTransition.trigger('click.control', ['open']);
                } else {
                    $menuItemLis.removeClass('active');
                    $parent.addClass('active');
                    $parent.parents('.submenu').find('.submenu-title').addClass('active').end().siblings().find('.submenu-title').removeClass('active');
                }
                sessionStorage.setItem('navIndex', $(this).attr('data-link'));
            })
        }

        var menuCollapse = new MenuCollapse('#sub_list_wrap', {
            indexes: (sessionStorage.getItem('navIndex') || '0-0')
        })
    }(jQuery)
</script>
<script type="text/javascript">

    ;
    +function ($) {
        var sideNavModule = {
            sideNav: $('#sub_list_wrap'),
            mainFrame: $('#mainFrame'),
            unreadTimer: null,
            currentHref: '${href}',
            time: 2000,


            changeUnreadTag: function (params) {
                var sideNav = this.sideNav;
                var self = this;
                var levelThrees = sideNav.find('.submenu-title.active').parent().find('.oe-menu-item');
                if (this.unreadTimer) {
                    clearTimeout(this.unreadTimer);
                }
                this.unreadTimer = setTimeout(function () {
                    $.ajax({
                        url: '${ctx}/sys/menu/totoReflash/' + params,
                        type: 'GET',
                        dataType: 'json',
                        success: function (data) {
                            var result = data.result;
                            if (!result) {
                                sideNav.find('.active').find('i.unread-tag').hide().text('').end().siblings().find('a:not([data-assign="assign"])').find('i.unread-tag').hide().text('')
                                sideNavModule.changeTotalUnreadTag(levelThrees.parents(".submenu")[0]);
                                return false;
                            }

                            var untoNums = {};
                            levelThrees.each(function (index, item) {
                                var $a = $(this).find('>a');
                                var href = $a.attr('href');
                                untoNums[index.toString()] = {
                                    isShow: false,
                                    index: index
                                };
                                if (href.indexOf('taskAssign') > -1) {
                                    untoNums[index.toString()] = {
                                        isShow: true,
                                        index: index
                                    };
                                }
                                for (var k in result) {
                                    if (!result.hasOwnProperty(k)) {
                                        continue;
                                    }
                                    if (new RegExp(k, 'i').test(href)) {
                                        var value = result[k];
                                        $a.find('i.unread-tag')[(value > 0 ? 'show' : 'hide')]().text(value);
                                        untoNums[index.toString()].isShow = true;
                                    }
                                }
                            });

                            for (var nk in untoNums) {
                                if (!untoNums[nk].isShow) {
                                    levelThrees.eq(untoNums[nk].index).find('>a i.unread-tag').hide().text(0)
                                }
                            }
                            sideNavModule.changeTotalUnreadTag(levelThrees.parents(".submenu")[0]);

                        },
                        error: function () {
                        }
                    })
                }, this.time)
            },
            changeStaticUnreadTag: function (dataurl, time) {
                setTimeout(function () {
                    var aTag = $('#sub_list_wrap').find('.oe-menu-item.active').parents('.submenu').find('li.active>a');
                    var $assign = $('#sub_list_wrap').find('.oe-menu-item.active').parents('.submenu').find('a[data-assign="assign"]');
                    var $selector = $assign.size() > 0 ? $assign : aTag;
                    $.ajax({
                        url: dataurl,
                        type: 'GET',
                        dataType: 'text',
                        success: function (data) {
                            $selector.find('i.unread-tag')[(data > 0 ? 'show' : 'hide')]().text(data);
                            sideNavModule.changeTotalUnreadTag($selector.parents(".submenu")[0]);
                        }
                    })
                }, time || 0);
            },
            changeTotalUnreadTag: function (submenuOb) {
                var temtotal = 0;
                $(submenuOb).find("i.unread-tag").each(function (i, v) {
                    if ($(v).attr("style") == "display: none") {

                    } else {
                        temtotal = temtotal + (parseInt($(v).html()) || 0);
                    }
                });
                if (temtotal == 0 || !temtotal) {
                    $(submenuOb).find("i.total-unread-tag").attr("style", "display: none");
                } else {
                    $(submenuOb).find("i.total-unread-tag").removeAttr("style");
                    $(submenuOb).find("i.total-unread-tag").html(temtotal);
                }

            },

            changeStaticUnreadTagTargetUrl: function (dataurl, tagurl, time) {
                setTimeout(function () {
                    $.ajax({
                        url: dataurl,
                        type: 'GET',
                        dataType: 'text',
                        success: function (data) {
                            var $aHref = $("a[href='" + tagurl + "']");
                            $aHref.find('i.unread-tag')[(data > 0 ? 'show' : 'hide')]().text(data);
                            var $selector = $aHref.parents('.oe-menu-item');
                            sideNavModule.changeTotalUnreadTag($selector.parents(".submenu")[0]);
                        }
                    });
                }, time || 0)
            },

            changeLink: function (href) {
                var sideNav = this.sideNav;
                var $allAs = sideNav.find('a');
                $allAs.parent().removeClass('active').parent().prev().removeClass('active');
                $allAs.each(function (i, item) {
                    var $item = $(item);
                    var link = $item.attr('href');
                    if (href == link) {
                        $item.parent().addClass('active').parent().prev().addClass('active');
                        return false;
                    }
                });
            },
            changeTodoNum: function (url, num) {
                $("a[href='" + url + "']").find('i.unread-tag')[(num > 0 ? 'show' : 'hide')]().text(num);
                sideNavModule.changeTotalUnreadTag($("a[href='" + url + "']").parents(".submenu")[0]);
            },
            topReload: function (url) {
//                location.href = url;
                window.location.reload();
            },

            //处理多个节点todo数;
            changeMenuItemsTodoNum: function (actywId, results) {
                var sideNav = this.sideNav;
                var $oeMenuTitle = sideNav.find('.submenu-title.active');
                var $oeMenuItems = $oeMenuTitle.parent().find('.oe-menu-item');
                var outResultsTodoTags = [];
                var totalTodoNum = 0;
                $oeMenuItems.each(function (index, item) {
                    var $oeMenuItemLink = $(item).find('>a');
                    var href = $oeMenuItemLink.attr('href');
                    var $oeMenuItemTodoTag = $oeMenuItemLink.find('.unread-tag');
                    var gnodeId = href.match(/gnodeId=(\w+)(?=\&)?/);
                    var hActywId = href.match(/actywId=(\w+)(?=\&)?/);
                    var todoNum;
                    if (gnodeId && hActywId) {
                        gnodeId = gnodeId[1];
                        hActywId = hActywId[1];
                        todoNum = results[gnodeId];
                        if (todoNum && hActywId === actywId) {
                            todoNum = todoNum > 100 ? '99+' : todoNum;
                            $oeMenuItemTodoTag.show().text(todoNum);
                            totalTodoNum += todoNum;
                        } else {
                            outResultsTodoTags.push($oeMenuItemTodoTag);
                        }
                    }
                });
                $.each(outResultsTodoTags, function (index, item) {
                    $(item).hide().text('0');
                });
                totalTodoNum = totalTodoNum > 100 ? '99+' : totalTodoNum;
                $oeMenuTitle.find('.total-unread-tag').show().text(totalTodoNum);
            },


            emptyMenuItemsTodoNum: function (actywId) {
                var sideNav = this.sideNav;
                var $oeMenuTitle = sideNav.find('.submenu-title.active');
                var $oeMenuItems = $oeMenuTitle.parent().find('.oe-menu-item');
                $oeMenuItems.each(function (index, item) {
                    var $oeMenuItemLink = $(item).find('>a');
                    var href = $oeMenuItemLink.attr('href');
                    var $oeMenuItemTodoTag = $oeMenuItemLink.find('.unread-tag');
                    var gnodeId = href.match(/gnodeId=(\w+)(?=\&)?/);
                    var hActywId = href.match(/actywId=(\w+)(?=\&)?/);
                    if (gnodeId && hActywId) {
                        hActywId = hActywId[1];
                        if (hActywId === actywId) {
                            $oeMenuItemTodoTag.hide().text(0);
                        }
                    }
                });
                $oeMenuTitle.find('.total-unread-tag').hide().text(0);
            }
        };
        window['sideNavModule'] = sideNavModule;


    }(jQuery)

    $(function () {
        $(".submenu").each(function (i, v) {
            sideNavModule.changeTotalUnreadTag($(v));
        });
    });
</script>
</body>
</html>
