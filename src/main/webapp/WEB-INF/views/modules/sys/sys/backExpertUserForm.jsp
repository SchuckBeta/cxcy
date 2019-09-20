<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            validate1 = $("#inputForm").validate({
                rules: {
                    loginName: {
                        remote: {
                            url: "/a/sys/user/checkLoginName",     //后台处理程序
                            type: "post",               //数据发送方式
                            dataType: "json",           //接受数据格式
                            data: {                     //要传递的数据
                                userid: "${user.id}",
                                loginName: function () {
                                    return $("#loginName").val();
                                }
                            }
                        }
                    },

                    no: {
                        numberLetter: true,
                        remote: {
                            url: "/a/sys/user/checkNo",     //后台处理程序
                            type: "post",               //数据发送方式
                            dataType: "json",           //接受数据格式
                            data: {                     //要传递的数据
                                userid: "${user.id}",
                                no: function () {
                                    return $("#no").val();
                                }
                            }
                        }
                    },

                    mobile: {
                        required: true,
                        phone_number: true,//自定义的规则
                        digits: true,
                        remote: "${ctx}/sys/user/checkMobile?id=" + $("#id").val()
                    },
                    professionalName: 'required'

                },
                messages: {
                    loginName: {
                        remote: "用户登录名已存在"
                    },
                    no: {
                        remote: "该工号/学号已被占用"
                    },
                    confirmNewPassword: {
                        equalTo: "输入与上面相同的密码"
                    },
                    mobile: {
                        required: "必填信息",
                        digits: "请输入正确的手机号码",
                        remote: "手机号已存在"
                    },
                    professionalName: '必填信息'
                },
                submitHandler: function (form) {
                    if ("${user.id}" == "${cuser.id}" && $("#loginName").val() != "${cuser.loginName}") {
                        confirmx("修改当前登录用户的登录名将会重新登录，确定修改？", function () {
                            loading('正在提交，请稍等...');
                            form.submit();
                        });
                    } else {
                        loading('正在提交，请稍等...');
                        form.submit();
                    }
                },
                errorContainer: "#messageBox",
                errorPlacement: function (error, element) {
                    $("#messageBox").text(
                            "输入有误，请先更正。");
                    if (element.is(":checkbox") || element.is(":radio") || element.parent().is(".input-append")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });
            displayInput();
            //添加自定义验证规则
            jQuery.validator.addMethod("phone_number", function (value, element) {
                var length = value.length;
                return this.optional(element) || (length == 11 && mobileRegExp.test(value));
            }, "手机号码格式错误");

            jQuery.validator.addMethod("numberLetter", function (value, element) {
                var length = value.length;
                return this.optional(element) || numberLetterExp.test(value);
            }, "只能输入数字和字母");
        });

        function displayInput() {
            var userType = $("#userType").val();
            if (userType == '2') {
                $("#teacherType").css("display", "block");
            } else {
                $("#teacherType").css("display", "none");
            }
        }
        function sub() {
            if(validate1.form()){
                $("#inputForm").find('.btn-submit').prop('disabled', true);
            }
            $("#inputForm").submit();
        }
        function clearOrg() {
            $("#professional").val("");
            $("#professionalName").val("");
            $("#officeId").val("");
            $("#orgStr").html("");
        }
    </script>

</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>用户管理</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <input type="hidden" id="userSelectPage" value="1"/>
    <form:form id="inputForm" modelAttribute="user"
               action="${ctx}/sys/expert/save" method="post" class="form-horizontal" autocomplete="off">
        <form:hidden path="id"/>
        <sys:message content="${message}"/>
        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>账号信息</span>
                <i class="line"></i>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="oldLoginName"><i>*</i>登录名:</label>
            <div class="controls">
                <input id="oldLoginName" name="oldLoginName" type="hidden" value="${user.loginName}">
                <form:input path="loginName" htmlEscape="false" maxlength="50" class="required userName"/>
            </div>
        </div>

        <%--<div class="control-group">--%>
            <%--<label class="control-label" for="newPassword"><c:if test="${empty user.id}">--%>
                <%--<i>*</i>--%>
            <%--</c:if>登陆密码:</label>--%>
            <%--<div class="controls">--%>
                <%--<input id="newPassword" name="newPassword" type="password" value=""--%>
                       <%--maxlength="50" minlength="3"--%>
                       <%--class="${empty user.id?'required':''}"/>--%>
                <%--<c:if test="${not empty user.id}">--%>
                    <%--<span class="help-inline gray-color">若不修改密码，请留空。</span>--%>
                <%--</c:if>--%>
            <%--</div>--%>
        <%--</div>--%>
        <%--<div class="control-group">--%>
            <%--<label class="control-label" for="confirmNewPassword"><c:if test="${empty user.id}">--%>
                <%--<i>*</i>--%>
            <%--</c:if>确认密码:</label>--%>
            <%--<div class="controls">--%>
                <%--<input id="confirmNewPassword" name="confirmNewPassword"--%>
                       <%--type="password" value="" maxlength="50" minlength="3"--%>
                       <%--equalTo="#newPassword" class="${empty user.id?'required':''}"/>--%>
            <%--</div>--%>
        <%--</div>--%>

        <%--<div class="control-group">--%>
            <%--<label class="control-label" for="userType">用户类型:</label>--%>
            <%--<div class="controls">--%>
                <%--<form:select path="userType" onchange="javascript:displayInput();clearOrg();">--%>
                    <%--&lt;%&ndash; <form:option value="" label="请选择" /> &ndash;%&gt;--%>
                    <%--<form:options items="${fns:getDictList('sys_user_type')}"--%>
                                  <%--itemLabel="label" itemValue="value" htmlEscape="false"/>--%>
                <%--</form:select>--%>
            <%--</div>--%>
        <%--</div>--%>
        <div class="control-group" id="teacherType">
            <label class="control-label">导师来源:</label>
            <div class="controls">
                <form:select path="teacherType" onchange="clearOrg()">
                    <%-- <form:option value="" label="请选择" /> --%>
                    <form:options items="${fns:getDictList('master_type')}"
                                  itemLabel="label" itemValue="value" htmlEscape="false"/>
                </form:select>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="roleIdList"><i>*</i>用户角色:</label>
            <div class="controls controls-checkbox">
                <form:checkboxes path="roleIdList" items="${allRoles}"
                                 itemLabel="name" itemValue="id" htmlEscape="false"
                                 class="required"/>
            </div>
        </div>
        <br/>

        <div class="edit-bar edit-bar-sm clearfix">
            <div class="edit-bar-left">
                <span>基本信息</span>
                <i class="line"></i>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="name"><i>*</i>姓名:</label>
            <div class="controls">
                <form:input path="name" htmlEscape="false" maxlength="50"
                            class="required"/>

            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="no"><i>*</i>工号/学号:</label>
            <div class="controls">
                <form:input path="no" htmlEscape="false" maxlength="50" class="required "/>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="mobile"><i>*</i>手机号:</label>
            <div class="controls">
                <form:input path="mobile" htmlEscape="false" maxlength="100" class="required"/>
            </div>
        </div>

        <input type="hidden" id="officeId" name="office.id" value="${user.office.id}"/>
        <div class="control-group">
            <label class="control-label" for="professional"><i>*</i>所属:</label>
            <div class="controls">
                <sys:treeselect id="professional" name="professional"
                                value="${user.professional}" labelName="professionalName"
                                labelValue="${fns:getOffice(user.professional).name}" title=""
                                cssStyle="width: 175px;"
                                url="/sys/office/treeData"/>
                <label id="orgStr" style="display:none"></label>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label" for="domainIdList">擅长技术领域:</label>
            <div class="controls controls-checkbox">
                <form:checkboxes path="domainIdList" items="${allDomains}"
                                 itemLabel="label" itemValue="value" htmlEscape="false"/>
                <!-- <span class="help-inline"><font color="red">*</font> </span> -->
            </div>
        </div>

        <div class="control-group">
            <label class="control-label" for="email">邮箱:</label>
            <div class="controls">
                <form:input path="email" htmlEscape="false" maxlength="100"
                            class="email"/>
            </div>
        </div>


        <c:if test="${not empty user.id}">
            <div class="control-group">
                <label class="control-label">创建时间:</label>
                <div class="controls">
                    <p class="control-static"><fmt:formatDate
                            value="${user.createDate}" type="both" dateStyle="full"/></p>
                </div>
            </div>
            <%--<div class="control-group">--%>
            <%--<label class="control-label">最后登陆:</label>--%>
            <%--<div class="controls">--%>
            <%--<label class="lbl">IP:--%>
            <%--${user.loginIp}&nbsp;&nbsp;&nbsp;&nbsp;时间：<fmt:formatDate--%>
            <%--value="${user.loginDate}" type="both" dateStyle="full" />--%>
            <%--</label>--%>
            <%--</div>--%>
            <%--</div>--%>
        </c:if>
        <div class="form-actions">
            <shiro:hasPermission name="sys:user:edit">
                <button type="button" class="btn btn-primary btn-submit" onclick="sub();">保存</button>
            </shiro:hasPermission>
            <a class="btn btn-default" href="${ctx}/sys/expert/list">返回</a>
        </div>
    </form:form>
</div>

</body>
</html>