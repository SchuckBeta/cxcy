<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>
</head>
<body>
<div id="app" class="hide" :class="{show: pageLoad}">
    <%@include file="/WEB-INF/views/include/nprovince/topBar.jsp" %>
    <div class="app-aside" :class="{ 'app-aside-w216': isCollapsed }" @mouseleave="isCollapsed = false"
         @mouseenter="isCollapsed = true">
        <div class="menu-root-list">
            <div v-for="item in rootMenuList" :key="item.id" class="menu-root-item">
                <div :class="{'router-link-active': item.id === firstMenu.id}">
                    <span class="mri-icon-box">
                        <i class="mri-icon iconfont" :class="item.icon"></i>
                        <a :href="ctx+'/sys/menu/treePlus?parentId='+item.id">{{item.name}}</a>
                    </span>
                </div>
            </div>
        </div>
    </div>
    <div class="app-container">
        <div class="second-menu-container">
            <h3 class="menu-title">
                <a href="javascript:void(0);" class="router-link-active"><span>{{firstMenu.name}}</span></a>
            </h3>
            <div class="sub-menu-list">
                <div v-for="subMenu in secondMenus" :key="subMenu.id" class="sub-menu-item">
                    <div class="sub-menu-title" :class="{'router-link-active': curMenuId == subMenu.id }">
                        <i v-show="subMenu.children && subMenu.children.length > 0"
                           class="sub-menu-icon menu-icon"
                           :class="{'el-icon-caret-bottom': !subMenuShows[subMenu.id], 'el-icon-caret-top': subMenuShows[subMenu.id] }"
                           @click.stop="changeCollapse(subMenu)"></i>
                        <span class="menu-title-span" @click.stop="changeSubMenu(subMenu)">
                            <a href="javascript:void (0);">{{ subMenu.name }}</a>
                        </span>
                        <sub-badge :menu="subMenu"></sub-badge>
                    </div>
                    <el-collapse-transition>
                        <div v-show="subMenuShows[subMenu.id]" class="three-menu-children">
                            <div v-for="threeMenu in subMenu.children"
                                 :key="threeMenu.id"
                                 @click.stop="changeThreeMenu(threeMenu)"
                                 class="three-menu-item">
                                    <div class="threeMenu-menu-title-span" :class="{'router-link-active': curMenuId == threeMenu.id }">
                                        <a href="javascript:void (0);">{{ threeMenu.name }}</a>
                                        <el-badge v-if="threeMenu.todoCount && threeMenu.todoCount > 0" :value="threeMenu.todoCount" :max="99">
                                        </el-badge>
                                    </div>
                            </div>
                        </div>
                    </el-collapse-transition>
                </div>
            </div>
        </div>
        <div class="main-container">
            <iframe  id="mainFrame" v-show="!!iframeMenu.id" name="mainFrame" width="100%"  frameborder="0" height="100%"></iframe>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';
    Vue.component('sub-badge', {
        template: '<div v-show="todoCount > 0"><el-badge :value="todoCount" :max="99"></el-badge></div>',
        props: {
            menu: {}
        },
        computed: {
          todoCount: function () {
              var children = this.menu.children || [];
              var count = 0;
              children.forEach(function (item) {
                  if(item.todoCount && item.todoCount > 0){
                      count+=item.todoCount;
                  }
              });
              return count;
          }
        }
    });

    var LeftSideBarMenuApp = new Vue({
        el: '#app',
        data: function () {
            var menuList = JSON.parse(JSON.stringify(${fns: toJson(fns:getAllMenuList())})) || [];
            var firstMenu = JSON.parse(JSON.stringify(${fns: toJson(firstMenu)})) || {};
            var secondMenus = JSON.parse(JSON.stringify(${fns: toJson(secondMenus)})) || {};
            return {
                menuList: menuList,
                firstMenu: firstMenu,
                secondMenus: secondMenus,
                isCollapsed: false,
                subMenuShows: {},
                iframeMenu: {},
                secondMenuList: [],
                menuEntity: {}
            }
        },
        computed: {
            rootMenuList: function () {
                return this.menuList.filter(function (item) {
                    return item.parentId === '${fns:getRoot().id}' && item.isShow === '1'
                })
            },
            locationHash: function () {
                if(sessionStorage.getItem("subMenuId")){
                    return sessionStorage.getItem("subMenuId");
                }
                return false;
            },
            curMenuId: function () {
                return this.iframeMenu.id;
            },
        },
        methods: {


            getSecondMenuList: function () {
                var flatten = function (data) {
                    return data.reduce(function (p1, p2) {
                        var children = p2.children || [];
                        return p1.concat(p2, flatten(children));
                    }, []);
                }
                return flatten(this.secondMenus)
            },
            getMenuEntity: function () {
                var entity = {};
                for(var i = 0; i < this.secondMenuList.length; i++){
                    var menu = this.secondMenuList[i];
                    entity[menu.id] = menu;
                }
                return entity;
            },

            setMenuShow: function() {
                var smenu = this.iframeMenu;
                for(var i = 0; i < this.secondMenuList.length; i++){
                    var menu = this.secondMenuList[i];
                    var isCollapsed = menu.id === smenu.id;
                    if(isCollapsed && !menu.children){
                        var subMenu = this.getMenuById(menu.parentId);
                        this.$set(this.subMenuShows, subMenu.id, true);
                    }else {
                        this.$set(this.subMenuShows, menu.id, isCollapsed);
                    }
                }
            },

            getIframeMenu: function () {
                var href = this.secondMenus[0].href;
                if(!!href){
                    return this.secondMenus[0];
                }
                var threeMenus = this.secondMenus[0].children;
                return threeMenus[0];
            },

            getMenuById: function(id){
                var smenu;
                if(!id) return;
                for(var i = 0; i < this.secondMenus.length; i++){
                    var menu = this.secondMenus[i];
                    if(menu.id === id){
                        smenu = menu;
                        break;
                    }
                }
                return smenu;
            },

            setLocationHash: function () {
                var smenu = this.getMenuById(this.locationHash);
                if(smenu){
                    this.iframeMenu = smenu
                }else {
                    this.iframeMenu = this.getIframeMenu();
                }
                document.getElementById('mainFrame').setAttribute('src', this.ctx + this.iframeMenu.href)
            },

            changeSubMenu: function (item) {
                if(item.href){
                    this.iframeMenu = item;
                }else {
                    this.iframeMenu = item.children[0];
                }
                this.$set(this.subMenuShows, item.id, true);
            },

            changeThreeMenu: function (item) {
                this.iframeMenu = item;
                document.getElementById('mainFrame').setAttribute('src', this.ctx + this.iframeMenu.href)
            },

            changeCollapse: function(item) {
                this.$set(this.subMenuShows, item.id, !this.subMenuShows[item.id]);
            },

            getMenuBreadcrumb: function () {
                var curMenuId = this.curMenuId;
                var breadcrumb = [];
                for(var i = 0; i < this.secondMenuList.length; i++){
                    var menu = this.secondMenuList[i];
                    if(menu.id === curMenuId){
                        if(!menu.children){
                            if(this.menuEntity[menu.parentId]){
                                breadcrumb.push(this.menuEntity[menu.parentId]);
                            }
                            breadcrumb.push(menu);
                            break;
                        }
                    }
                }
                return breadcrumb;
            },
            
            updateTodoCount: function () {
                var menuEntity = this.menuEntity;
                this.$axios.get('/sys/menu/getMenuTodoNum/'+this.firstMenu.id).then(function (response) {
                    var data = response.data;
                    if(data.status === 1){
                        var menuList = data.data || [];
                        menuList.forEach(function (item) {
                            menuEntity[item.id].todoCount = item.todoCount;
                        })
                    }
                })
            },

        },
        created: function () {
            this.secondMenuList = this.getSecondMenuList();
            this.menuEntity = this.getMenuEntity();
            this.setLocationHash();
            this.setMenuShow();
        },
    })


</script>
</body>
</html>
