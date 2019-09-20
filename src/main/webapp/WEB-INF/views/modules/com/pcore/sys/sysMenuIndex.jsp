<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <meta name="decorator" content="backPath_header"/>
    <style>
        .v-modal {
          opacity: .5;
        }
    </style>
</head>
<body>

<c:if test="${msg!=null}">
    ${msg}
</c:if>
<div id="app" v-show="pageLoad" style="display: none" class="sys-menu-container container">
    <div class="row sys-menu-row" v-if="isShowMenu">
        <c:forEach items="${fns:getAllMenuList()}" var="menu" varStatus="idxStatus">
            <c:if test="${menu.parent.id eq fns:getRoot().id && menu.isShow eq '1'}">
                <div class="span3">
                    <div class="menu-item <c:if test="${not fns:checkMenu(menu.id)}">menu-item-qx</c:if> menu ${not empty firstMenu && firstMenu ? ' active' : ''}">
                        <c:if test="${empty menu.href}">
                            <span class="topic-unread"></span>
                            <a data-id="${menu.id}" href="${ctx}/sys/menu/treePlus?parentId=${menu.id}">
                                <img class="menu-pic" src="${fns:ftpImgUrl(menu.imgUrl)}" alt=""/>
                                <span class="name">${menu.name}</span>
                            </a>
                            <p class="desc">${menu.remarks}</p>
                        </c:if>
                        <c:if test="${not empty menu.href}">
                            <span class="topic-unread"></span>
                            <a data-id="${menu.id}" href="${ctx}/sys/menu/treePlus?parentId=${menu.id}">
                                <img class="menu-pic" src="${fns:ftpImgUrl(menu.imgUrl)}" alt=""/>
                                <span class="name">${menu.name}</span>
                            </a>
                            <p class="desc">${menu.remarks}</p>
                        </c:if>
                        <div class="layer-jurisdiction">
                            <span class="text">请开通权限访问</span>
                        </div>
                    </div>
                </div>
            </c:if>
        </c:forEach>
    </div>


    <el-dialog
            title="选择运营平台"
            :visible.sync="dialogVisible"
            width="60%" :close-on-click-modal="false" :show-close="false" :before-close="handleClose">
        <p class="tenant-tip">*选中后双击进入运营平台</p>
        <div class="school-box" v-loading="tenantDialogLoading">
            <div class="school-province">
                <label>省级平台</label>
                <span @dblclick="chooseProvince"  @click="clickType" :class="{red: clickName == provinceName}">{{provinceName}}</span>
            </div>
            <div class="school-item">
                <label>校级平台</label>
                <div class="school-detail">
                    <div class="type-name">
                        <div v-for="item in schoolTypes" :key="item.key">
                            <span :class="{active:item.key == activeType}"
                                  @click="changeType(item)">{{item.name}}</span>
                        </div>
                    </div>
                    <ul>
                        <li v-for="item in schools" :key="item.id">
                            <span @dblclick="chooseType(item)" :class="{red: item.schoolName == clickName}" @click="clickType(item)">{{item.schoolName}}</span>
                        </li>
                    </ul>
                </div>
            </div>
        </div>
    </el-dialog>


</div>

<%@ include file="/WEB-INF/views/layouts/website/copyright.jsp" %>
<script>
    'use strict';

   var tenantDialog = new Vue({
        el: '#app',
        data: function () {
            return {
                clickName: '',
                dialogVisible: false,
                schoolTypes: [],
                activeType: '1',
                allSchools: [],
                provinceName: '',
                provinceId: '',
                tenantDialogLoading: false,
                isShowMenu: false
            }
        },
        computed: {
            schools: {
                get: function () {
                    var self = this;
                    return this.allSchools.filter(function (v) {
                        return v.schoolType == self.activeType;
                    });
                }
            }
        },
        methods: {
            clickType: function (item) {
                if(item.schoolName){
                    this.clickName = item.schoolName;
                    return false;
                }
                this.clickName = this.provinceName;
            },
            getSchoolType: function () {
                var self = this;
                this.tenantDialogLoading = true;
                this.$axios.get('/sys/tenant/schoolTypeList').then(function (response) {
                    var data = response.data;
                    self.schoolTypes = JSON.parse(data.data) || [];
                    self.tenantDialogLoading = false;
                }).catch(function () {
                    self.tenantDialogLoading = false;
                });
            },
            changeType: function (item) {
                this.activeType = item.key;
            },
            chooseType: function (item) {
                var self = this;
                this.$axios.get('/sys/tenant/ajaxChange?id=' + item.id).then(function (response) {
                    var data = response.data;
                    if(data.status == 1){
                        self.dialogVisible = false;
                        location.href = '${ctx}/sysMenuIndex';
                    }else {
                        self.$message({
                            message: data.msg,
                            type: 'error'
                        })
                    }
                })
            },
            chooseProvince: function () {
                var self = this;
                this.$axios.get('/sys/tenant/ajaxChange?id=' + this.provinceId).then(function (response) {
                    var data = response.data;
                    if(data.status == 1){
                        self.dialogVisible = false;
                        location.href = '${ctx}/sysMenuIndex';
                    }else {
                        self.$message({
                            message: data.msg,
                            type: 'error'
                        })
                    }
                })
            },
            handleClose: function () {
                this.dialogVisible = false;
                this.isShowMenu = true;
            }

        },
        created: function () {
            this.dialogVisible = this.showTenantPop;
            this.isShowMenu = !this.showTenantPop;
            this.getSchoolType();
        }

   });

    $('.sys-menu-container .menu-item').each(function (i, item) {
        var $item = $(item);
        getUnDealThing($item, $item.find('a').attr('data-id'))
    })

    function getUnDealThing(element, id) {
        $.ajax({
            type: 'GET',
            url: '${ctx}/sys/menu/ajaxMenuCount',
            data: {id: id},
            success: function (data) {
                if (data.status) {
                    var total = data.datas;
                    element.find('.topic-unread').css('display', 'block').text(total > 999 ? (total + '+') : total)
                }
            }
        })
    }
</script>

</body>
</html>
