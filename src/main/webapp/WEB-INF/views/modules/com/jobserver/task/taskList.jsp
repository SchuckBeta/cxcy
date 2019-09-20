<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>任务配置</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
</head>
<style>
    .table input,.table select{
        margin-bottom: 0;
    }
</style>
<body class="bgray">

    <div class="container-fluid">
        <div class="edit-bar clearfix">
            <div class="edit-bar-left">
                <span>任务配置</span>
                <i class="line weight-line"></i>
            </div>
        </div>
        <form id="addForm" method="post">
        <table class="table table-bordered table-condensed table-hover table-center table-orange table-nowrap" >
            <thead>
            <tr>
                <td>id</td>
                <td>name</td>
                <td>group</td>
                <td>状态</td>
                <td>cron表达式</td>
                <td>描述</td>
                <td>同步否</td>
                <td>类路径</td>
                <td>spring id</td>
                <td>方法名</td>
                <td>操作</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="job" items="${taskList}">
                <tr>
                    <td>${job.jobId }</td>
                    <td>${job.jobName }</td>
                    <td>${job.jobGroup }</td>
                    <td>${job.jobStatus }
                        <c:choose>
                        <c:when test="${job.jobStatus=='1' }">
                            <a href="javascript:;"
                               onclick="changeJobStatus('${job.jobId}','stop')">停止</a>&nbsp;
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:;"
                               onclick="changeJobStatus('${job.jobId}','start')">开启</a>&nbsp;
                        </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${job.cronExpression }</td>
                    <td>${job.description }</td>
                    <td>${job.isConcurrent }</td>
                    <td>${job.beanClass }</td>
                    <td>${job.springId }</td>
                    <td>${job.methodName }</td>
                    <td><a href="javascript:;" onclick="updateCron('${job.jobId}')">更新cron</a> |
                        <a href="javascript:;" onclick="delCron('${job.jobId}')">删除</a>
                       <%-- <a href="javascript:;" onclick="runAJobNow('${job.jobId}')">立即运行</a>--%>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td>n</td>
                <td><input class="input-mini" type="text" name="jobName" id="jobName"/></td>
                <td><input class="input-mini" type="text" name="jobGroup" id="jobGroup"/></td>
                <td>0<input type="hidden" name="jobStatus" value="0"/></td>
                <td><input class="input-mini" type="text" name="cronExpression"
                           id="cronExpression"/></td>
                <td><input class="input-mini" type="text" name="description" id="description"/></td>
                <td><select style="width:50px;" name="isConcurrent" id="isConcurrent">
                    <option value="1">1</option>
                    <option value="0">0</option>
                </select></td>
                <td><input class="input-small" type="text" name="beanClass" id="beanClass"/></td>
                <td><input class="input-mini" type="text" name="springId" id="springId"/></td>
                <td><input class="input-mini" type="text" name="methodName" id="methodName"/></td>
                <td><input type="button" class="btn btn-primary input-mini" onclick="add()" value="保存"/></td>
            </tr>
            </tbody>
        </table>
        </form>
    </div>
<script>
    function validateAdd() {
        if ($.trim($('#jobName').val()) == '') {
            alert('name不能为空！');
            $('#jobName').focus();
            return false;
        }
        if ($.trim($('#jobGroup').val()) == '') {
            alert('group不能为空！');
            $('#jobGroup').focus();
            return false;
        }
        if ($.trim($('#cronExpression').val()) == '') {
            alert('cron表达式不能为空！');
            $('#cronExpression').focus();
            return false;
        }
        if ($.trim($('#beanClass').val()) == '' && $.trim($('#springId').val()) == '') {
            $('#beanClass').focus();
            alert('类路径和spring id至少填写一个');
            return false;
        }
        if ($.trim($('#methodName').val()) == '') {
            $('#methodName').focus();
            alert('方法名不能为空！');
            return false;
        }
        return true;
    }
    function add() {
        if (validateAdd()) {
            showWaitMsg();
            $.ajax({
                type: "POST",
                async: false,
                dataType: "JSON",
                cache: false,
                url: "task/add",
                data: $("#addForm").serialize(),//对表单进行序列化
                success: function (data) {
                    hideWaitMsg();
                    if (data.flag) {
                        location.reload();//从服务端重新载入  reload(true)从缓存载入
                    } else {
                        alert(data.msg);
                    }
                }//end-callback
            });//end-ajax
        }
    }
    function changeJobStatus(jobId, cmd) {
        showWaitMsg();
        $.ajax({
            type: "POST",
            async: false,
            dataType: "JSON",
            cache: false,
            url: "task/changeJobStatus",
            data: {
                jobId: jobId,
                cmd: cmd
            },
            success: function (data) {
                hideWaitMsg();
                if (data.flag) {
                    location.reload();
                } else {
                    alert(data.msg);
                }

            }//end-callback
        });//end-ajax
    }
    function updateCron(jobId) {
        var cron = prompt("输入cron表达式！", "")
        if (cron) {
            showWaitMsg();
            $.ajax({
                type: "POST",
                async: false,
                dataType: "JSON",
                cache: false,
                url: "task/updateCron",
                data: {
                    jobId: jobId,
                    cron: cron
                },
                success: function (data) {
                    hideWaitMsg();
                    if (data.flag) {
                        location.reload();
                    } else {
                        alert(data.msg);
                    }

                }//end-callback
            });//end-ajax
        }

    }

    function delCron(jobId) {
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            url: "task/delCron",
            data: {
                jobId: jobId
            },
            success: function (data) {
                hideWaitMsg();
                if (data.flag) {
                    location.reload();
                } else {
                    alert(data.msg);
                }
            }//end-callback
        });//end-ajax
    }



    function showWaitMsg(msg) {
        if (msg) {

        } else {
            msg = '正在处理，请稍候...';
        }
        var panelContainer = $("body");
        $("<div id='msg-background' class='datagrid-mask' style=\"display:block;z-index:10006;\"></div>").appendTo(panelContainer);
        var msgDiv = $("<div id='msg-board' class='datagrid-mask-msg' style=\"display:block;z-index:10007;left:50%\"></div>").html(msg).appendTo(
                panelContainer);
        msgDiv.css("marginLeft", -msgDiv.outerWidth() / 2);
    }
    function hideWaitMsg() {
        $('.datagrid-mask').remove();
        $('.datagrid-mask-msg').remove();
    }
</script>
</body>
</html>