<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@ include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <script type="text/javascript">
        $(document).ready(function () {
            itemChangeShow();
            $("#inputForm").validate({
                submitHandler: function (form) {
                    resetTip();
                    form.submit();
                },
                errorPlacement: function (error, element) {
                    if (element.is(":checkbox")) {
                        error.appendTo(element.parent().parent());
                    } else {
                        error.insertAfter(element);
                    }

                }
            });

        });
        function clearNextsVal(ob) {
            ob.parents('.control-group').nextAll().find("select").val("");
            $("#category").val("");
            $("#subdivision").val("");
        }
        function itemChange() {
            clearNextsVal($("#item"));
            itemChangeShow();
        }
        function itemChangeShow() {
            var item = $("#item").val();
            $(".skill").attr("style", "display:none");
            $(".project").attr("style", "display:none");
            $(".gcontest").attr("style", "display:none");
            $(".procId").attr("style", "display:none");
            if (item == "0000000128" || item == "0000000129") {//双创项目双创大赛
                if (item == "0000000128") {//双创项目
                    $(".project").attr("style", "display:");
                    pcChange($("#project_category"), null);
                }
                if (item == "0000000129") {//双创大赛
                    $(".gcontest").attr("style", "display:");
                    gcChange($("#gcontest_category"), null)
                }
            } else {
                $(".procId").attr("style", "display:");
                if (item == "0000000132") {//技能证书
                    $(".skill").attr("style", "display:");
                    $(".procId").attr("style", "display:");
                }
            }
        }
        function setV(ob, name) {
            $("#" + name).val($(ob).val());
        }
        function pcChange(ob, name) {
            if (name) {
                setV(ob, name);
                $("#project_subdivision").val("");
                $("#subdivision").val("");
            }
            $("#project_subdivision").find("option[value!='']").attr("style", "display:none");
            $.ajax({
                type: 'post',
                url: '/a/sco/scoAffirmConf/getSetData',
                dataType: "json",
                data: {
                    item: '1,',
                    category: $(ob).val()
                },
                success: function (data) {
                    if (data) {
                        $(".project_subdivision").attr("style", "display:");
                        $.each(data, function (i, v) {
                            $("#project_subdivision").find("option[value='" + v + "']").attr("style", "display:");
                        });
                    } else {
                        $(".project_subdivision").attr("style", "display:none");
                    }
                }
            });
        }
        function gcChange(ob, name) {
            if (name) {
                setV(ob, name);
                $("#gcontest_subdivision").val("");
                $("#subdivision").val("");
            }
            $("#gcontest_subdivision").find("option[value!='']").attr("style", "display:none");
            $.ajax({
                type: 'post',
                url: '/a/sco/scoAffirmConf/getSetData',
                dataType: "json",
                data: {
                    item: '7,',
                    category: $(ob).val()
                },
                success: function (data) {
                    if (data) {
                        $(".gcontest_subdivision").attr("style", "display:");
                        $.each(data, function (i, v) {
                            $("#gcontest_subdivision").find("option[value='" + v + "']").attr("style", "display:");
                        });
                    } else {
                        $(".gcontest_subdivision").attr("style", "display:none");
                    }

                }
            });
        }
    </script>

    <style>
        .control-group-inline {
            display: inline-block;
            vertical-align: top;
        }
    </style>

</head>
<body>
<div class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>编辑认定项目</span>
            <i class="line weight-line"></i>
        </div>
    </div>
    <sys:message content="${message}"/>
    <form:form class="form-horizontal" id="inputForm"
               modelAttribute="scoAffirmConf" action="save" method="post">
        <input type="hidden" id="id" name="id" value="${scoAffirmConf.id }"/>
        <input type="hidden" id="category" name="category" value="${scoAffirmConf.category }"/>
        <input type="hidden" id="subdivision" name="subdivision" value="${scoAffirmConf.subdivision }"/>
        <div class="control-group">
            <label class="control-label" for="type"><i>*</i>学分类型：</label>
            <div class="controls">
                <form:select path="type" class="form-control required">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('0000000118')}"
                                  itemValue="value" itemLabel="label" htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group">
            <label class="control-label" for="item"><i>*</i>学分项：</label>
            <div class="controls">
                <form:select path="item" class="form-control required" onchange="itemChange()">
                    <form:option value="" label="--请选择--"/>
                    <form:options items="${fns:getDictList('0000000119')}"
                                  itemValue="value" itemLabel="label" htmlEscape="false"/>
                </form:select>
            </div>
        </div>
        <div class="control-group skill" style="display:none">
            <div class="control-group-inline">
                <label class="control-label" for="skill_category"><i>*</i>技能证书类型：</label>
                <div class="controls">
                    <form:select path="skill_category" class="form-control required" onchange="setV(this,'category')">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getDictList('0000000120')}"
                                      itemValue="value" itemLabel="label" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="control-group project" style="display:none">
            <div class="control-group-inline">
                <label class="control-label" for="project_category"><i>*</i>项目类型：</label>
                <div class="controls">
                    <form:select path="project_category" class="form-control required"
                                 onchange="pcChange(this,'category')">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${project_style}"
                                      itemValue="value" itemLabel="label" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group-inline project_subdivision">
                <label class="control-label" for="project_subdivision"><i>*</i>项目类别：</label>
                <div class="controls">
                    <form:select path="project_subdivision" class="form-control required"
                                 onchange="setV(this,'subdivision')">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getDictList('project_type')}"
                                      itemValue="value" itemLabel="label" htmlEscape="false" style="display:none"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="control-group gcontest" style="display:none">
            <div class="control-group-inline">
                <label class="control-label" for="gcontest_category"><i>*</i>大赛类型：</label>
                <div class="controls">
                    <form:select path="gcontest_category" class="form-control required"
                                 onchange="gcChange(this,'category')">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${competition_type}"
                                      itemValue="value" itemLabel="label" htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
            <div class="control-group-inline gcontest_subdivision">
                <label class="control-label" for="gcontest_subdivision">大赛类别：</label>
                <div class="controls">
                    <form:select path="gcontest_subdivision" class="form-control" onchange="setV(this,'subdivision')">
                        <form:option value="" label="--全部--"/>
                        <form:options items="${fns:getDictList('competition_net_type')}"
                                      itemValue="value" itemLabel="label" htmlEscape="false" style="display:none"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="control-group procId" style="display:none">
            <div class="control-group-inline">
                <label class="control-label" for="procId"><i>*</i>学分认定流程：</label>
                <div class="controls">
                    <form:select path="procId" class="form-control required">
                        <form:option value="" label="--请选择--"/>
                        <form:options items="${fns:getActListData('120')}" itemLabel="group.name" itemValue="id"
                                      htmlEscape="false"/>
                    </form:select>
                </div>
            </div>
        </div>
        <div class="control-group">
            <div class="control-group-inline">
                <label class="control-label" for="remarks">备注</label>
                <div class="controls">
                    <textarea id="remarks" class="input-xxlarge" name="remarks" rows="5"
                              maxlength="255">${scoAffirmConf.remarks}</textarea>
                </div>
            </div>
        </div>
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">保存</button>
            <button type="button" class="btn btn-default"
                    onclick="javascript:location.href='${ctx}/sco/scoAffirmConf'">返回
            </button>
        </div>
    </form:form>
</div>
</body>
</html>