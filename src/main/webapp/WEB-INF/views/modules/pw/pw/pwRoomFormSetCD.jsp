<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/vue/vue.min.js"></script>
</head>
<body>
<div id="assetDoc" class="container-fluid">
    <sys:message content="${message}"/>
    <form:form id="searchForm" class="form-horizontal form-search-block" modelAttribute="pwRoom"
               action="${ctx}/pw/pwRoom/save" method="post">
        <form:hidden id="roomId" path="id"/>
        <input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}"/>
        <input id="pageSize" name="pageSize" type="hidden" value="${page.pageSize}"/>
        <div class="control-group">
            <label class="control-label">名称：</label>
            <div class="controls">
                <p class="control-static">${pwRoom.name}</p>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">人数：</label>
            <div class="controls">
                <p class="control-static">${pwRoom.num }人</p>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">负责人：</label>
            <div class="controls">
                <p class="control-static">${pwRoom.person }</p>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">电话：</label>
            <div class="controls">
                <p class="control-static">${pwRoom.mobile}</p>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">编号/名称：</label>
            <div v-show="isLoad" class="controls" tabindex="-1" style="outline: none;display: none">
                <input type="text" placeholder="输入团队名字" v-model="numOrName" @focus="numFocus($event)" @blur="numBlur">
                <table v-show="focusShow"
                       class="table-condensed table-hover table-center table-orange table-nowrap table-assign table-bordered table">
                    <thead>
                    <tr>
                        <td>入驻编号</td>
                        <td>团队</td>
                        <td>企业</td>
                        <td>项目</td>
                        <td>负责人</td>
                        <td>入驻有效期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-if="index <= 10" v-for="(item, index) in spaceListFilter" :key="item.id">
                        <td>{{item.name}}</td>
                        <td>{{item.data.eteam ? item.data.eteam.team.name : '-'}}</td>
                        <td>{{item.data.ecompany ? item.data.ecompany.pwCompany.name : '-'}}</td>
                        <td>{{item.data.eproject ? item.data.eproject.project.name : '-'}}</td>
                        <td>{{item.data.applicant ? item.data.applicant.name : '无负责人'}}</td>
                        <td>{{item.data.endDate | formatDate}}</td>
                        <td>
                            <button type="button" class="btn btn-primary btn-small" @click="assign(item, $event)">分配
                            </button>
                        </td>
                    </tr>
                    <tr v-if="!spaceListFilter.length">
                        <td colspan="7">没有分配信息，请重新搜索</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label">已入驻列表：</label>
            <div class="controls"  v-show="isLoad" style="display: none">
                <table id="assignedTable"
                       class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap table-assigned">
                    <thead>
                    <tr>
                        <td>入驻编号</td>
                        <td>团队</td>
                        <td>企业</td>
                        <td>项目</td>
                        <td>负责人</td>
                        <td>入驻有效期</td>
                        <td>操作</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-for="(item, index) in enterList">
                        <td>{{item.name}}</td>
                        <td>{{item.data.eteam ? item.data.eteam.team.name : '-'}}</td>
                        <td>{{item.data.ecompany ? item.data.ecompany.pwCompany.name : '-'}}</td>
                        <td>{{item.data.eproject ? item.data.eproject.project.name : '-'}}</td>
                        <td>{{item.data.applicant ? item.data.applicant.name : '无负责人'}}</td>
                        <td>{{item.data.endDate | formatDate}}</td>
                        <td>
                            <button type="button" class="btn btn-default btn-small" @click="cancelAssign(item, $event)">
                                取消分配
                            </button>
                        </td>
                    </tr>
                    <tr v-if="!enterList.length">
                        <td colspan="7">没有已入驻的信息</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>
        <div class="form-actions">
                <%--<button type="submit" class="btn btn-primary">保存</button>--%>
            <button type="button" onclick="history.go(-1)" class="btn btn-default">返回</button>
        </div>
    </form:form>
</div>

<script>

    +function ($) {
        var assetDoc = new Vue({
            el: '#assetDoc',
            data: function () {
                return {
                    enterList: [],
                    spaceList: [],
                    spaceListFilter: [],
                    roomId: '',
                    numOrName: '',
                    focusShow: false,
                    isAssignTable: false,
                    focusTimer: null,
                    isLoad: false
                }
            },
            watch: {
                numOrName: function (value) {
                    var reg = new RegExp(value, 'i');
                    this.spaceListFilter = this.spaceList.filter(function (t) {
                        var eTeam = t.data.eteam;
                        var eCompany = t.data.ecompany;
                        var eProject = t.data.eproject;
                        return reg.test(t.name) || (eTeam && reg.test(eTeam.team.name)) || (eCompany && reg.test(eCompany.pwCompany.name)) || (eProject && reg.test(eProject.project.name))
                    })
                }
            },
            filters: {
                formatDate: function (val) {
                    return val ? val.replace(' 00:00:00', '') : '-';
                }
            },
            methods: {
                getSpaceList: function () {
                    var self = this;
                    var xhr = $.get('${ctx}/pw/pwEnter/treeData?status=1&isAll=1');
                    xhr.success(function (data) {
                        if (data && data.length) {
                            self.spaceList = data;
                            self.spaceListFilter = self.spaceList;
                        }
                    })
                },
                getEnterList: function () {
                    var self = this;
                    var xhr = $.post('${ctx}/pw/pwEnter/treeData?status=6,3,4,7&rids=' + this.roomId);
                    xhr.success(function (data) {
                        self.enterList = data;
                        self.isLoad = true;
                    })
                },
                assign: function (item, $event) {
                    var self = this;
                    var xhr = $.post('${ctx}/pw/pwEnterRoom/ajaxPwEnterRoom/' + this.roomId + '?eid=' + item.id + '&isEnter=true');
                    xhr.success(function (data) {
                        if (data.status) {
                            self.getEnterList();
                            self.getSpaceList();
                            self.focusShow = false;
                        }
                        showTip(data.msg);
                    })
                },
                cancelAssign: function (item, $event) {
                    var self = this;
                    var xhr = $.post('${ctx}/pw/pwEnterRoom/ajaxPwEnterRoom/' + this.roomId + '?eid=' + item.id + '&isEnter=false');
                    xhr.success(function (data) {
                        if(data.status){
                            self.getEnterList();
                            self.getSpaceList();
                        }
                        showTip(data.msg);
                    })
                },
                numFocus: function ($event) {
                    this.focusShow = true;
                },

                numBlur: function () {
                    var self = this;
                    this.focusTimer && clearTimeout(this.focusTimer);
                    this.focusTimer = setTimeout(function () {
                        self.focusShow = false;
                        self.numOrName = '';
                    }, 200)
                }
            },
            beforeMount: function () {
                this.getSpaceList();
            },
            mounted: function () {
                this.roomId = $('#roomId').val();
                this.getEnterList();

            }
        })
    }(jQuery);


</script>
</body>
</html>