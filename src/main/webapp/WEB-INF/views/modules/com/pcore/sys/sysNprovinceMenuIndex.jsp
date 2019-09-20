<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/nprovince/header.jsp" %>
</head>
<body>
<div id="app" class="hide" :class="{show: pageLoad}">
    <%@include file="/WEB-INF/views/include/nprovince/topBar.jsp" %>
    <div class="sys-menu-bg">
        <div class="sys-menu-opacity">
            <div class="sys-menu-root">
                <div class="sys-menu-root_content">
                    <div class="sys-menu-row">
                        <div class="sys-menu-col" v-for="item in rootMenuList" :key="item.id">
                            <el-badge :value="item.todoCount" type="primary" :max="90"
                                      :hidden="!item.todoCount || item.todoCount == 0" class="item">
                                <div class="sys-menu-root_item">
                                    <a :href="ctx+'/sys/menu/treePlus?parentId='+item.id">
                                        <div class="menu-pic">
                                            <img :src="item.imgUrl | ftpHttpFilter(ftpHttp)"/>
                                        </div>
                                        <p class="name text-center"><span class="name-box">{{item.name}}</span></p>
                                        <%--<div class="description">--%>
                                        <%--{{item.remarks}}--%>
                                        <%--</div>--%>
                                    </a>
                                </div>
                            </el-badge>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var menuList = JSON.parse(JSON.stringify(${fns: toJson(fns:getAllMenuList())})) || [];
            var rootMenuList = menuList.filter(function (item) {
                item.todoCount = 0;
                return item.parentId === '${fns:getRoot().id}' && item.isShow === '1'
            });
            return {
                rootMenuList: rootMenuList,
//                rootMenuList: rootMenuList.slice(0, 10),
                area: '',
                collapsed: false
            }
        },
        methods: {
            getMenuCount: function () {
                var menuCountXhr = [];
                var i = 0, rootMenuList = this.rootMenuList;
                while (i < rootMenuList.length) {
                    menuCountXhr.push(this.$axios.get('/sys/menu/ajaxMenuCount', {params: {id: rootMenuList[i].id}}));
                    i++;
                }
                this.$axios.all(menuCountXhr).then(this.$axios.spread(function () {
                    for (var j = 0; j < arguments.length; j++) {
                        var req = arguments[j].data;
                        if (!req.status) {
                            continue;
                        }
                        rootMenuList[j].todoCount = req.datas
                    }
                }));
            }
        },
        created: function () {
            this.getMenuCount();
        }
    })

</script>
</body>
</html>
