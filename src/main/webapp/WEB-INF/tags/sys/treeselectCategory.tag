<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ attribute name="id" type="java.lang.String" required="true" description="编号" %>
<%@ attribute name="name" type="java.lang.String" required="true" description="隐藏域名称（ID）" %>
<%@ attribute name="value" type="java.lang.String" required="true" description="隐藏域值（ID）" %>
<%@ attribute name="labelName" type="java.lang.String" required="true" description="输入框名称（Name）" %>
<%@ attribute name="labelValue" type="java.lang.String" required="true" description="输入框值（Name）" %>
<%@ attribute name="title" type="java.lang.String" required="true" description="选择框标题" %>
<%@ attribute name="url" type="java.lang.String" required="true" description="树结构数据地址" %>
<%@ attribute name="checked" type="java.lang.Boolean" required="false"
              description="是否显示复选框，如果不需要返回父节点，请设置notAllowSelectParent为true" %>
<%@ attribute name="extId" type="java.lang.String" required="false" description="排除掉的编号（不能选择的编号）" %>
<%@ attribute name="isAll" type="java.lang.Boolean" required="false"
              description="是否列出全部数据，设置true则不进行数据权限过滤（目前仅对Office有效）" %>
<%@ attribute name="notAllowSelectRoot" type="java.lang.Boolean" required="false" description="不允许选择根节点" %>
<%@ attribute name="notAllowSelectParent" type="java.lang.Boolean" required="false" description="不允许选择父节点" %>
<%@ attribute name="module" type="java.lang.String" required="false" description="过滤栏目模型（只显示指定模型，仅针对CMS的Category树）" %>
<%@ attribute name="selectScopeModule" type="java.lang.Boolean" required="false"
              description="选择范围内的模型（控制不能选择公共模型，不能选择本栏目外的模型）（仅针对CMS的Category树）" %>
<%@ attribute name="allowClear" type="java.lang.Boolean" required="false" description="是否允许清除" %>
<%@ attribute name="allowInput" type="java.lang.Boolean" required="false" description="文本框可填写" %>
<%@ attribute name="cssClass" type="java.lang.String" required="false" description="css样式" %>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="css样式" %>
<%@ attribute name="smallBtn" type="java.lang.Boolean" required="false" description="缩小按钮显示" %>
<%@ attribute name="hideBtn" type="java.lang.Boolean" required="false" description="是否显示按钮" %>
<%@ attribute name="disabled" type="java.lang.String" required="false" description="是否限制选择，如果限制，设置为disabled" %>
<%@ attribute name="dataMsgRequired" type="java.lang.String" required="false" description="" %>
<%@ attribute name="selectLv" type="java.lang.String" required="false" description="选择的层级" %>
<div class="input-append">
    <input id="${id}" name="${name}" class="${cssClass}" type="hidden" value="${value}"/>
    <input id="${id}Name" name="${labelName}" ${allowInput?'':'readonly="readonly"'} type="text" value="${labelValue}"
           data-msg-required="${dataMsgRequired}"
           class="${cssClass}" placeholder="-请选择-" style="width:75%;${cssStyle}"/><a id="${id}Button" href="javascript:"
                                                                                     class="btn add-on ${disabled} ${hideBtn ? 'hide' : ''}"
                                                                                     style="${smallBtn?'padding:4px 2px;text-decoration:none;':''}">
    &nbsp;<i class="icon-search" style="color:#333;"></i>&nbsp;</a>&nbsp;&nbsp;
</div>
<script type="text/javascript">



    $("#${id}Button, #${id}Name").click(function () {
        <%--$("#${id}Button").click(function(){--%>
        // 是否限制选择，如果限制，设置为disabled
        if ($("#${id}Button").hasClass("disabled")) {
            return true;
        }
        // 正常打开
        top.$.jBox.open("iframe:${ctx}/tag/treeselect?url=" + encodeURIComponent("${url}") + "&module=${module}&checked=${checked}&extId=${extId}&isAll=${isAll}",
                "选择${title}", 320, 420, {
                    ajaxData: {selectIds: $("#${id}").val()},
                    buttons: {"确定": "ok", ${allowClear?"\"清除\":\"clear\", ":""}"关闭": true},
                    submit: function (v, h, f) {
                        if (v == "ok") {
                            var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
                            var ids = [], names = [], nodes = [];
                            if ("${checked}" == "true") {
                                nodes = tree.getCheckedNodes(true);
                            } else {
                                nodes = tree.getSelectedNodes();
                            }

                            var isChecked = true;
                            var $controlGroupNumberRule = $('.control-group-number-rule');

                            $.each(nodes, function (i, node) {
                                isChecked = true;
                                if(node.pId == '1'){
                                    $controlGroupNumberRule.show().addClass('required')
                                }else {
                                    $controlGroupNumberRule.hide().removeClass('required');
                                    $controlGroupNumberRule.find("input").val('');
                                }
                                ids.push(node.id);
                                names.push(node.name);
                                $("#parentPrefix").val(node.prefix);
                            })

                            if (!isChecked) {
                                return false;
                            }
                            $("#${id}").val(ids.join(",").replace(/u_/ig, ""));
                            $("#${id}Name").val(names.join(","));
                            $("#${id}").parent().find('label.error').hide();


                        }//<c:if test="${allowClear}">
                        else if (v == "clear") {
                            $("#${id}").val("");
                            $("#${id}Name").val("");
                        }//</c:if>
                        if (typeof ${id}TreeselectCallBack == 'function') {
                            ${id}TreeselectCallBack(v, h, f);
                        }
                    },
                    loaded: function (h) {
                        $(".jbox-content", top.document).css("overflow-y", "hidden");
                    }
                });
    });
</script>