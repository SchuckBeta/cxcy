<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script src="${ctxStatic}/vue/vue.min.js"></script>
    <script src="${ctxJs}/components/checkbox/checkboxGroup.js"></script>
    <script src="${ctxJs}/components/checkbox/checkbox.js?v=1"></script>

    <style>

        .e-checkbox_assign_users .e-checkbox-group .e-checkbox {
            margin-bottom: 5px;
            font-size: 14px;
        }
       .table-assigned  .e-checkbox_label{
            font-size: 14px;
        }
        .e-checkbox_assign_users .e-checkbox-group {
            padding-top: 5px;
        }
        .table-assigned th, .table-assigned td{
            border-left: 0;
        }
        .table-assigned th, .table-assigned td{
            border-top:1px dashed #ddd;
        }
        .table-assigned {
            border: 0;
        }
        .table-assigned>thead>tr>th{
            font-size: 14px;
            font-weight: normal;
        }
        .table-assigned>tbody>tr>td{
            font-size: 14px;
        }

        .form-actions{
            border-top:none;
        }

        #inputForm .allCheck {
            margin-left:0;
        }

        .e-checkbox, .e-radio{
            vertical-align: middle;
            margin:0;
        }

    </style>
</head>
<body>
<div id="actYwGassignUserForm" class="container-fluid">

    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="inputForm" modelAttribute="actYwGassign"
               action="${ctx}/actyw/actYwGassign/saveAssign"
               method="post"
               class="form-horizontal">
        <div class="control-group">
            <label class="control-label">指派节点：</label>
            <div class="controls">
                <p class="control-static">${actYwGnode.name}</p>
            </div>
        </div>
        <input type="hidden" name="promodelIds" value="${promodelIds}">
        <input type="hidden" name="gnodeId" value="${gnodeId}">
        <input type="hidden" name="ywId" value="${ywId}">
        <input type="hidden" name="secondName" id="secondName" value="${secondName}">
        <input type="hidden" name="userIds" id="userIds" v-model="checkedAssignUsers">
        <div class="control-group">
            <label class="control-label">选择指派专家：</label>
            <div class="controls">
                <table v-show="isLoad" class="assign table table-bordered table-assigned table-condensed"
                       style="min-width: 724px;display: none;">
                    <thead>
                    <tr>
                        <th class="text-left" width="220" style="border-left: none;padding:5px 5px 0">
                            <e-checkbox v-model="checkAll" :disabled="isGetting" custom-class="allCheck"
                                        @change="handleCheckAllTRChange">全部
                            </e-checkbox>
                        </th>
                        <th class="text-left" v-for="role in roles" style="padding:0px 5px 4px 10px"><div style="width: 90px; text-align: left;">{{role.name}}</div></th>
                    </tr>
                    </thead>
                    <tr v-for="college in colleges">
                        <td style="border-left: none;padding: 9px 5px 0;">
                            <span class="text-ellipsis"
                                  style="display: inline-block;width: 100px; vertical-align: middle">{{college.name}}</span>
                            <e-checkbox v-model="college.checkAll" :disabled="isGetting"
                                        @change="handleCheckAllChange(college)">全部
                            </e-checkbox>
                        </td>
                        <td class="e-checkbox_assign_users" v-for="userRole in college.userRoles" style="padding:5px 5px 1px">
                            <e-checkbox-group v-model="userRole.checkedAssignUsers" @change="checkboxChange(college)"
                                              :disabled="isGetting">
                                <e-checkbox v-for="assignUser in userRole.assignUsers" :label="assignUser.id"
                                            @change="getAuditUser"
                                            :key="assignUser.id">{{assignUser.name}}
                                </e-checkbox>
                            </e-checkbox-group>
                        </td>
                    </tr>
                </table>
            </div>


                <%--<div class="controls chosen-assign">--%>
                <%--<select data-placeholder="请选择" id="userChoose" name="userChoose" multiple="" class="chosen-select input-xlarge required"--%>
                <%--style="visibility: hidden"--%>
                <%--tabindex="-1">--%>
                <%--<c:forEach items="${userList}" var="user">--%>
                <%--<option value="${user.id}"--%>
                <%--&lt;%&ndash;<c:if test="${fns:contains(actYwGassign.userIds, user.id)}">selected</c:if>&ndash;%&gt;--%>
                <%-->${user.name}</option>--%>
                <%--</c:forEach>--%>
                <%--</select>--%>
                <%--</div>--%>
        </div>
        <div class="control-group">
            <div class="controls">
                <table class="table table-bordered table-condensed table-hover table-orange table-center table-GassignUser">
                    <thead>
                    <tr>
                        <td>审核人</td>
                        <td>待办数</td>
                        <td>指定数</td>
                        <td>学校/学院</td>
                    </tr>
                    </thead>
                    <tbody>
                    <tr v-show="!isGetting" v-for="auditUser in auditUsers" style="display: none">
                        <td>{{auditUser.name}}</td>
                        <td>{{auditUser.num}}</td>
                        <td>${promodelNum}</td>
                        <td>{{auditUser.officeName}}</td>
                    </tr>
                    <tr v-show="!isGetting && auditUsers.length < 1" style="display: none">
                        <td colspan="4"><span class="gray-color">没有数据</span></td>
                    </tr>
                    <tr v-show="isGetting" style="display: none">
                        <td colspan="4"><span class="gray-color">数据加载中...</span></td>
                    </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="form-actions text-center">
                <%--<shiro:hasPermission name="actyw:actYwGassign:edit">--%>
            <button type="button" id="submitBtn" class="btn btn-primary" :disabled="auditUsers == 0 || disabled" @click.stop.prevent="submitForm">指派</button>
                <%--</shiro:hasPermission>--%>
            <button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
        </div>
    </form:form>
</div>
<script>

    $(function () {


        var actYwGassignUserForm = new Vue({
            el: '#actYwGassignUserForm',
            data: function () {
                return {
                    isLoad: false,
                    isGetting: false,
                    auditUsers: [],
                    checkedAssignUsers: [],
                    checkAll: false,
                    userList: JSON.parse(JSON.stringify(${fns: toJson(userList)})),
                    roles: [],
                    colleges: [],
                    disabled: false
                }
            },
            methods: {

                submitForm: function () {
                    this.disabled = true;
                    $('#inputForm').submit();
                },

                handleCheckAllTRChange: function (value) {
                    var colleges = this.colleges;
                    colleges.forEach(function (item) {
                        item.checkAll = value;
                        var userRoles = item.userRoles;
                        for (var i = 0; i < userRoles.length; i++) {
                            if (!item.checkAll) {
                                userRoles[i].checkedAssignUsers = [];
                            } else {
                                userRoles[i].checkedAssignUsers = [];
                                for (var a = 0; a < userRoles[i].assignUsers.length; a++) {
                                    userRoles[i].checkedAssignUsers.push(userRoles[i].assignUsers[a].id);
                                }
                            }
                        }
                    })
                    this.getAuditUser();
                },
                handleCheckAllChange: function (college) {
                    var userRoles = college.userRoles;
                    for (var i = 0; i < userRoles.length; i++) {
                        if (!college.checkAll) {
                            userRoles[i].checkedAssignUsers = [];
                        } else {
                            userRoles[i].checkedAssignUsers = [];
                            for (var a = 0; a < userRoles[i].assignUsers.length; a++) {
                                userRoles[i].checkedAssignUsers.push(userRoles[i].assignUsers[a].id);
                            }
                        }
                    }
                    this.checkAll = this.colleges.every(function (item) {
                        return item.checkAll;
                    })
                    this.getAuditUser();
                },
                checkboxChange: function (college) {
                    var userRoles = college.userRoles;
                    var allCount = 0;
                    var count = 0;
                    for (var i = 0; i < userRoles.length; i++) {
                        count += userRoles[i].checkedAssignUsers.length;
                        allCount += userRoles[i].assignUsers.length;
                    }
                    college.checkAll = count === allCount;
                    this.checkAll = this.colleges.every(function (item) {
                        return item.checkAll;
                    })
                },
                initCheckAll: function () {
                    var colleges = this.colleges;
                    for (var i = 0; i < colleges.length; i++) {
                        var college = colleges[i];
                        this.checkboxChange(college);
                    }
                },
                getAuditUser: function () {
                    var colleges = this.colleges;
                    var users = [];
                    for (var i = 0; i < colleges.length; i++) {
                        var college = colleges[i];
                        var userRoles = college.userRoles;
                        for (var u = 0; u < userRoles.length; u++) {
//                            console.log(userRoles[u].checkedAssignUsers)
                            users = users.concat(userRoles[u].checkedAssignUsers)

                        }
                    }

                    this.ajaxGetToDoNumByUser(users);
                },
                ajaxGetToDoNumByUser: function (val) {
                    var self = this;
                    this.checkedAssignUsers = val.join(',');
                    if (val.length < 1) {
                        this.auditUsers = [];
                        return false;
                    }
                    this.isGetting = true;
                    $.ajax({
                        url: '/a/actyw/actYwGassign/ajaxGetToDoNumByUser',
                        data: {
                            userIds: val.join(','),
                            gnodeId: '${gnodeId}'
                        },
                        dataType: 'json',
                        success: function (data) {
                            self.auditUsers = data || [];
                            self.isGetting = false;
                        },
                        error: function (error) {
                            self.isGetting = false;
                        }
                    })
                },

                setRoles: function () {
                    var roles = {};
                    for (var i = 0; i < this.userList.length; i++) {
                        var user = this.userList[i];
                        if (!roles[user.roleId]) {
                            roles[user.roleId] = true;
                            this.roles.push({
                                id: user.roleId,
                                name: user.roleName
                            })
                        }
                    }
                },

                getColleges: function () {
                    var colleges = {};
                    var collegesArr = [];
                    for (var i = 0; i < this.userList.length; i++) {
                        var user = this.userList[i];
                        var collegeId = user.collegeId;
                        if (!collegeId) {
                            user.collegeId = 'other';
                        }
                        if (!colleges[user.collegeId]) {
                            colleges[user.collegeId] = true;
                            collegesArr.push({
                                id: user.collegeId,
                                name: (user.collegeName || '其它')
                            })
                        }
                    }
                    return collegesArr;
                },

                setColleges: function () {
                    var roles = this.roles;
                    var collegesArr = this.getColleges();
                    var rolePerson = this.getRolesPerson();
                    for (var i = 0; i < roles.length; i++) {
                        var role = roles[i];
                        for (var c = 0; c < collegesArr.length; c++) {
                            var college = collegesArr[c];
                            if (typeof college.checkAll == 'undefined') {
                                college.checkAll = false;
                            }
                            if (!college.userRoles) {
                                college.userRoles = [];
                            }
                            college.userRoles.push({
                                checkedAssignUsers: [],
                                assignUsers: rolePerson[role.id + (college.id || 'other')]
                            })
                        }
                    }
                    this.colleges = collegesArr;
                },

                getRolesPerson: function () {
                    var rolePerson = {};
                    var roles = this.roles;
                    var userList = this.userList;
                    for (var i = 0; i < roles.length; i++) {
                        var role = roles[i];
                        for (var u = 0; u < userList.length; u++) {
                            var user = userList[u];
                            if(!rolePerson[role.id + (user.collegeId || 'other')]){
                                rolePerson[role.id + (user.collegeId || 'other')] = []
                            }
                            if (role.id === user.roleId) {
                                rolePerson[role.id + (user.collegeId || 'other')].push({
                                    id: user.id,
                                    name: user.name
                                })
                            }
                        }
                    }
                    return rolePerson;
                }
            },
            created: function () {
                this.setRoles();
                this.setColleges();

            },
            mounted: function () {
                this.initCheckAll();
//                this.getAuditUser();
                this.isLoad = true;
            }
        });
    })

</script>
</body>
</html>