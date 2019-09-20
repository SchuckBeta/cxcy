<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${frontTitle}</title>

    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/frontCyjd/jquery-ui.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxOther}/jquery-ui-1.12.1/jquery-ui.css"/>
    <!--头部导航公用样式-->
    <link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/header.css"/>

    <link rel="stylesheet" type="text/css" href="${ctxCss}/index.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/common.css"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/frontCyjd/frontBime.css?v=q1111">
    <!--focus样式表-->

    <script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
    <script src="${ctxStatic}/jquery-validation/1.11.0/jquery.validate.min.js" type="text/javascript"></script>

    <script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
    <script src="${ctxCommon}/common-js/bootstrap.min.js" type="text/javascript"></script>
    <!--用于轮播的js-->
    <!--文本溢出-->
    <script src="${ctxJs}/common.js" type="text/javascript"></script>
    <script src="${ctxJs}/frontCyjd/frontCommon.js?v=21" type="text/javascript"></script>


    <%@include file="/WEB-INF/views/include/treeview.jsp" %>

    <style type="text/css">

        .accordion-group {
            width: 300px;
        }

        .accordion-heading {
            height: 34px;
            line-height: 34px;
            padding: 0 15px;
            background-color: #f4e6d4;
        }

        .ztree {
            margin-bottom: 0;
            padding-right: 15px;
            height: 390px;
            overflow-x: hidden;
            overflow-y: auto;
        }

        #openClose .close, #openClose {
            height: 425px;
        }

        #right {
            float: none;
            height: 425px;
            margin-left: 310px;
            overflow: hidden;
        }

        #left, #openClose {
            float: left;
        }

        #openClose {
            width: 6px;
            margin: 0 1px;
            cursor: pointer;
        }

        #openClose, #openClose.close {
            background: #efefef url("${ctxStatic}/images/openclose.png") no-repeat -29px center;
        }

        #openClose.close {
            background-position: 1px center;
            opacity: 0.5;
            filter: alpha(opacity=50)
        }
    </style>
    <script type="text/javascript">


        function getIds(invType, btnObj) {
            var zTree = $.fn.zTree.getZTreeObj("ztree");
            var nodes = zTree.getChangeCheckedNodes(true);

            /* if(nodes.length==0){
             alert("请选择部门！！");
             return false;
             } */
            var trLen = $('#officeContent').contents().find('#contentTable tbody tr').size();

            var officeIds = currentId;
            for (var i = 0; i < nodes.length; i++) {
                var halfCheck = nodes[i].getCheckStatus();
                if (!halfCheck.half) {
                    var idt = nodes[i].id;
                    // idt=idt.replace(" ","");
                    if (idt != null && idt != undefined) {
                        idt = $.trim(idt);
                    }
                    if (idt == null || idt == "" || idt.length == 0) {
                    } else if (nodes[i].grade == '2') {
                        // alert(nodes[i].id.length);
                        officeIds += nodes[i].id + ',';
                    }
                }
            }


            if (officeIds.indexOf(",") > 0) {
                officeIds = officeIds.substring(0, officeIds.lastIndexOf(","));
            }
            var idarray = document.getElementById("officeContent").contentWindow.getValue();
            var userIds = '';
            if (idarray != null && idarray != undefined && idarray.length > 0) {
                $.each(idarray, function (index, value) {
                    //alert(index+"..."+value);
                    if (value != null && value != undefined && value != "") {
                        userIds += value + ',';
                    }
                });
                userIds = userIds.substring(0, userIds.lastIndexOf(","));
            }

            var teamId = $("#teamId").html();
            var opType = $("#opType").html();
            var userType = $("#userType").html();
            var typename = "";
            if (userType == 1) {
                typename = "学生";
            }
            if (userType == 2) {
                typename = "导师";
            }
            var url = "";
            if (opType == 1) {
                url = "${ctxFront}/team/teamUserRelation/batInTeamOffice";
            } else if (opType == 2) {
                if (invType == 2) {
                    url = "${ctxFront}/team/teamUserRelation/pullIn";
                } else if (invType == 3) {
                    url = "${ctxFront}/team/teamUserRelation/toInvite";
                }
            }


            if (!officeIds) {
                dialogCyjd.createDialog(0, "请至少选择学院");
                return;
            }
            if (trLen > 0) {
                if (userIds == null || userIds == "" || userIds == undefined) {
                    dialogCyjd.createDialog(0, "请至少选择一个专业");
                    return;
                }
            }


            if (teamId == null || teamId == "") {
                dialogCyjd.createDialog(0, "请求参数异常");
                return;
            }
            $(btnObj).prop('disabled', true).text(invType == 1 ? '发布中...' : '邀请中...')
            $.ajax({
                type: "post",
                url: url,
                data: {
                    'offices': officeIds,
                    'userIds': userIds,
                    'teamId': teamId,
                    'userType': userType
                },
                dataType: "json",
                success: function (data) {
                    if (data.success) {
                        if (opType == 1) {
                            dialogCyjd.createDialog(1, "发布成功！", {
                                buttons: [{
                                    text: '确定',
                                    'class': 'btn btn-sm btn-primary',
                                    click: function () {
                                        $('#myModal .close', parent.document).click()
                                        $(this).dialog('close');
                                    }
                                }]
                            });
                        } else if (opType == 2 && invType == 2) {
                            dialogCyjd.createDialog(1, "成功添加" + data.res + "个" + typename);
                        } else if (opType == 2 && invType == 3) {
                            dialogCyjd.createDialog(1, "成功邀请" + data.res + "个" + typename);
                        }
                        $(btnObj).prop('disabled', false).text(invType == 1 ? '确定发布' : '发送邀请')
                    }

                },
                error: function () {
                    dialogCyjd.createDialog(0, "系统异常!");
                    $(btnObj).prop('disabled', false).text(invType == 1 ? '确定发布' : '发送邀请')
                }
            });


        }

        function page(n, s, userType, ctxFront) {
            $("#pageNo").val(n);
            $("#pageSize").val(s);
            var professional = $("#professional").val();
            var domain = $("domain").val();
            //$("#searchForm").attr("action","${ctxFront}/sys/office/professionList");
            //$("#searchForm").submit();
            document.getElementById("officeContent").src = ctxFront + "/sys/office/professionList?userType=" + userType + "&professional=" + professional + "&domain=" + domain;
            return false;
        }
    </script>
</head>
<body>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<div id="teamId" style="display: none;">${teamId}</div>
<div id="opType" style="display: none;">${opType}</div>
<div id="userType" style="display: none;">${userType}</div>
<div class="text-right mgb15">
    <c:if test="${opType==1}">
        <button type="button" class="btn btn-primary btn-sm" onclick="getIds(1, this)">确定发布</button>
    </c:if>
    <c:if test="${opType==2}">
        <button type="button" class="btn btn-primary btn-sm" onclick="getIds(3, this)">发送邀请</button>
    </c:if>
</div>
<div id="teamId" style="display: none">${teamId}</div>
<%--<sys:message content="${message}"/>--%>
<div style="padding: 0 15px;">
    <div id="content" class="row">
        <div id="left" class="accordion-group">
            <div class="accordion-heading">
                <a class="accordion-toggle" href="javascript:void(0);" onclick="refreshTree();"> 列表
                </a>
            </div>
            <div id="ztree" class="ztree"></div>

        </div>
        <div id="openClose" class="close">&nbsp;</div>
        <div id="right">
            <iframe id="officeContent"
                    src="${ctxFront}/sys/office/professionList"
                    width="100%" height="100%" frameborder="0"></iframe>
        </div>
    </div>
</div>
<script type="text/javascript">


    var currentId = "";
    var setting = {
        data: {
            simpleData: {
                enable: true,
                idKey: "id",
                pIdKey: "pId",
                rootPId: '0'
            }
        },
        callback: {
            onClick: function (event, treeId, treeNode) {
                var id = treeNode.id == '0' ? '' : treeNode.id;
                if (treeNode.grade == '2') {
                    $('#officeContent').attr(
                            "src",
                            "${ctxFront}/sys/office/professionList?officeIds=" + id);
                } else if (treeNode.grade == '1') {
                    $('#officeContent').attr(
                            "src",
                            "${ctxFront}/sys/office/professionList?officeIds=" + "");
                }
                currentId = id;

            },
            onCheck: function (event, treeId, treeNode) {
                //var nodes =$.fn.zTree.getSelectedNodes();
                //	alert(treeNode.id);
                //	alert(treeNode.name);
                var officeIds = "";
                var curTree = $.fn.zTree.getZTreeObj(treeId);
                var nodes = curTree.getCheckedNodes(true);
                $.each(nodes, function () {
                    // alert("kk:"+this.id+"  "+this.grade);
                    if (this.grade == '2') {
                        // console.info("--------------------------------------->>"+this.name);
                        officeIds += this.id + ",";
                    }
                });
                officeIds = officeIds.substring(0, officeIds.lastIndexOf(","));
                // alert(professionIds);
                //alert(treeNode.checked);
                //var id = treeNode.id == '0' ? '' : treeNode.id;
                $('#officeContent').attr(
                        "src",
                        "${ctxFront}/sys/office/professionList?officeIds=" + officeIds);
            }
        }/*,
         check : {
         enable : true
         }*/
    };

    function refreshTree() {
        $.getJSON("${ctxFront}/sys/office/treeData?grade=2", function (data) {
            $.fn.zTree.init($("#ztree"), setting, data).expandAll(true);
        });

    }
    refreshTree();
</script>
</body>
</html>