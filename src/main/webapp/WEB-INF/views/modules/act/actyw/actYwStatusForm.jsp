<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <style>

    </style>
</head>
<body>
<div class="container-fluid">
    <%--<div class="edit-bar clearfix">--%>
        <%--<div class="edit-bar-left">--%>
            <%--<span>网关节点状态</span>--%>
            <%--<i class="line weight-line"></i>--%>
        <%--</div>--%>
    <%--</div>--%>
    <%@ include file="/WEB-INF/views/layouts/navigation.jsp" %>
    <form:form id="inputForm" modelAttribute="actYwStatus" action="${ctx}/actyw/actYwStatus/save" method="post"
               class="form-horizontal">
        <form:hidden path="id" value="${actYwStatus.id}"/>
        <input type="hidden" id="secondName" name="secondName"value="${secondName}"/>
        <sys:message content="${message}"/>
        <div class="control-group">
            <label class="control-label"><i>*</i>条件类型 ：</label>
            <div class="controls">
                <c:choose>
                    <c:when test="${actYwStatus.id!=null}">
                        <form:hidden path="gtype" value="${actYwStatus.gtype}"/>
                        <select class="required" disabled="true">

                                <%--<c:forEach items="${fns:getActywStatusList()}" var="item">--%>

                            <option value="${item.id}" data-regType="${item.regType}">${actYwStatus.name}</option>
                                <%--</c:forEach>--%>
                        </select>
                    </c:when>
                    <c:otherwise>
                        <div class="input-append">
                            <select name="gtype" id="gtype" class="required" onchange="addStatus()">
                                <option>请选择</option>
                                <c:forEach items="${fns:getActywStatusList()}" var="item">
                                    <option value="${item.id}" data-regType="${item.regType}">${item.name}</option>
                                </c:forEach>
                            </select>
                            <button class="btn btn-default" type="button" data-toggle="modal"
                                    data-target="#addGTypeModal">+
                            </button>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>

        <div class="control-group" id="aliasGroup"
                <c:if test="${actYwStatus.regType!=2}">
                    style="display:none"
                </c:if>
        >
            <label class="control-label">范围 ：</label>
            <div class="controls">
                <div>
                    <input id="startNum" name="startNum" type="text" value="${startNum}" maxlength="3"
                           class="input-mini required number digits firstNoZero"/> -
                    <input id="endNum" name="endNum" type="text" value="${endNum}" maxlength="3"
                           class="input-mini required number digits firstNoZero"/>
                    <form:input path="alias" type="hidden" htmlEscape="false" maxlength="10"/>
                </div>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label"><i>*</i>状态 ：</label>
            <div class="controls">
                <form:input path="state" htmlEscape="false" maxlength="10"
                            class="required"/>
            </div>
        </div>

        <div class="control-group">
            <label class="control-label">备注 ：</label>
            <div class="controls">
                <form:textarea path="remarks" htmlEscape="false" rows="3" maxlength="200" class="input-xxlarge "/>
            </div>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-default" onclick="history.go(-1)">返回</button>
        </div>
    </form:form>
</div>
<div id="addGTypeModal" class="modal hide" data-draggable="draggable"
<%-- data-draggable-option=${fns:toJson({"handle": ".modal-header"})} --%>
     tabindex="-1" role="dialog"
     aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
        <h3>添加网关类型</h3>
    </div>
    <div class="modal-body">
        <form:form id="actYwSgtypeSubmit" action="${ctx}/actyw/actYwSgtype/saveByStatus" method="post"
                   cssClass="form-horizontal">
            <%-- <div class="control-group">
                <label class="control-label"><i>*</i>网关类型：</label>
                <div class="controls">
                    <select name="regType" class="required">
                        <c:forEach items="${fns:getDictList('act_status_type')}" var="item">
                            <option value="${item.value}">${item.label}</option>
                        </c:forEach>
                    </select>
                </div>
            </div> --%>
            <div class="control-group">
                <label class="control-label"><i>*</i>网关类型：</label>
                <div class="controls">
                    <select name="regType" class="required">
                        <option value="">--请选择--</option>
                        <c:forEach var="curRegType" items="${regTypes}">
                            <option value="${curRegType.id}">${curRegType.name }</option>
                        </c:forEach>
                    </select>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label"><i>*</i>状态类型：</label>
                <div class="controls">
                    <input type="text" name="name" class="required"/>
                </div>
            </div>
        </form:form>
    </div>
    <div class="modal-footer">
        <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">关闭</button>
        <button type="button" class="btn btn-primary" onclick="saveSgtype()">提交</button>
    </div>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>

<script type="text/javascript">

    +function ($) {

        function ActYwStatus(element, options) {
            this.$element = $(element);
            this.options = options;
            this.$form = this.$element.find('form');
            this.$btnSave = this.$element.find('.modal-footer .btn-primary');
            this.$regType = this.$form.find('select[name="regType"]');
            this.$gType = this.$form.find('input[name="gtype"]');
            this.$name = this.$form.find('input[name="name"]');
            this.gTypeFormValid = null;
            this.init();
        }


        ActYwStatus.prototype.init = function () {
            this.gTypeFormValid = this.gTypeValidate();
            this.gTypeModalHideBefore();
            this.saveGType();
        };

        ActYwStatus.prototype.gTypeValidate = function () {
            return this.$form.validate();
        };

        //添加GType 请求
        ActYwStatus.prototype.addGTypeXhr = function () {
            var url = this.options.url;
            var $gType = this.$gType;
            var $regType = this.$regType;
            var $name = this.$name;
            return $.ajax({
                type: 'POST',
                url: url,
                data: {
                    name: $name.val(),
                    regType: $regType.val(),
                    gtype: $gType.val()
                }
            })
        };

        //保存
        ActYwStatus.prototype.saveGType = function () {
            var gTypeFormValid = this.gTypeFormValid;
            var $btnSave = this.$btnSave;
            var self = this;
            var $element = this.$element;
            var addGTypeXhr;
            this.$btnSave.on('click', function () {
                if (gTypeFormValid.form()) {
                    $btnSave.prop('disabled', true).text('保存中...');
                    addGTypeXhr = self.addGTypeXhr();
                    $element.modal('hide');
                    addGTypeXhr.success(function (data) {
                        dialogCyjd.createDialog(data.ret, data.msg);
                        window.location.reload();
                    });

                    addGTypeXhr.error(function (error) {
                        dialogCyjd.createDialog(0, '网络连接失败，错误代码' + error.status);
                    });

                    addGTypeXhr.done(function () {
                        $btnSave.prop('disabled', false).text('保存');
                    })
                }
            })
        };

        //关闭前清空表单
        ActYwStatus.prototype.gTypeModalHideBefore = function () {
            var gTypeFormValid = this.gTypeFormValid;
            var $gTypeForm = this.$form;
            this.$element.on('hide', function () {
                $gTypeForm[0].reset();
                gTypeFormValid.resetForm();
            })
        };

        $(function () {
            var actYwStatus = new ActYwStatus('#addGTypeModal', {
                url: '${ctx}/actyw/actYwSgtype/ajaxSave'
            })
        })


    }(jQuery);


    $(document).ready(function () {
        $(document).ready(function () {
            var $id = $("#id");
            var $gType = $("#gtype");
            var $startNum = $('#startNum');
            var $endNum = $('#endNum');
            var $alias = $('#alias');
            var $regType = $('#regType');
            var $aliasGroup = $('#aliasGroup');
            var $inputForm = $("#inputForm");
            var inputValidate = $inputForm.validate({
                submitHandler: function (form) {
                    $(form).find('button[type="submit"]').prop('disabled', true);
                    $alias.val([$startNum.val(), $endNum.val()].join('-'));
                    form.submit();
                },
                rules: {
                    'state': {
                        remote: {
                            url: '${ctx}/actyw/actYwStatus/checkState',
                            type: "post",
                            data: {
                                id: function () {
                                    return $id.val()
                                },
                                gtype: function () {
                                    return $gType.val()
                                }
                            }
                        }
                    },
                    'startNum': {
                        min: 0,
                        max: function () {
                            var val = $inputForm.find('input[name="endNum"]').val()
                            return val ? parseInt(val) - 1 : 99
                        }
                    },
                    'endNum': {
                        min: function () {
                            var val = $inputForm.find('input[name="startNum"]').val()
                            return val ? parseInt(val) + 1 : 1
                        },
                        max: 100
                    }
                },
                messages: {
                    'state': {
                        remote: '节点状态重复'
                    },
                    'startNum': {
                        min: '最小值为0',
                        max: function () {
                            return '不能超过最大值' + ($(element).find('input[name="endNum"]').val() || '99')
                        }
                    },
                    'endNum': {
                        min: function () {
                            return '不能小于最小值' + ($(element).find('input[name="startNum"]').val() || '1')
                        },
                        max: '最大值为100'
                    }
                },
                errorPlacement: function (error, element) {
                    if ((/startNum|endNum/).test(element.attr('name'))) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }
                }
            });


            $gType.on('change', function () {
                var val = $(this).val()
                var regType = $(this).find('option:selected').attr('data-regType')
                if (regType != 2) {
                    $aliasGroup.hide()
                    $startNum.removeClass('required');
                    $endNum.removeClass('required');
                } else {
                    $aliasGroup.show();
                    $startNum.addClass('required');
                    $endNum.addClass('required');
                }

            })

            jQuery.validator.addMethod("firstNoZero", function (value, element) {
                return this.optional(element) || !(/^0\d+/.test(value));
            }, "请输入数字");
        });


    });

    function addStatus() {

    }

    function saveStatus() {
        $("#inputForm").submit();
    }
    function saveSgtype() {
        // $("#actYwSgtypeSubmit").submit();
    }

</script>
</body>
</html>