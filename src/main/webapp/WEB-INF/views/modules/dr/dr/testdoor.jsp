<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
</head>
<body>
<br>
<div class="form-horizontal">
    <div class="control-group">
        <label class="control-label">ip：</label>
        <div class="controls">
            <input type="text" id="eqIp" >
        </div>
        <label class="control-label">port：</label>
        <div class="controls">
           <input type="text" id="eqPort" >
        </div>
        <label class="control-label">编号：</label>
        <div class="controls">
           <input type="text" id="eqNo" >
        </div>
        <label class="control-label">密码：</label>
        <div class="controls">
           <input type="text" id="eqPass" >
        </div>
        <a class="btn" href="javascript:connetEq();"><i class="icon-file"></i>设备连接测试</a>
        <a class="btn" href="javascript:getRecond();"><i class="icon-file"></i>得到连接信息</a>
    </div>

    <div class="control-group">
        <label class="control-label">卡号：</label>
        <div class="controls">
            <input type="text" id="cardNo" >
        </div>
        <label class="control-label">密码：</label>
        <div class="controls">
           <input type="text" id="cardPass" >
        </div>
        <label class="control-label">开门次数：</label>
        <div class="controls">
           <input type="text" id="openNum" >
        </div>
        <label class="control-label">开卡状态：</label>
        <div class="controls">
           <input type="text" id="openState" >（0:正常 1:挂失）
        </div>
        <label class="control-label">开门权限：</label>
        <div class="controls">
           <input type="text" id="openDoor" >
        </div>
        <a class="btn" href="javascript:openCard();"><i class="icon-file"></i>开卡测试</a>
        <a class="btn" href="javascript:getOpenCardRecond();"><i class="icon-file"></i>得到开卡信息</a>
    </div>

    <div class="control-group">
        <label class="control-label">卡号：</label>
        <div class="controls">
            <input type="text" id="readCardNo" >
        </div>
        <a class="btn" href="javascript:readCardNo();"><i class="icon-file"></i>读取记录</a>
        <a class="btn" href="javascript:getReadCardNoRecond();"><i class="icon-file"></i>得到读取记录</a>
    </div>
    <div class="control-group">
        <label class="control-label">每次读取长度：</label>
        <div class="controls">
            <input type="text" id="readNum" >
        </div>
        <a class="btn" href="javascript:readNewRecord();"><i class="icon-file"></i>读最新记录</a>
        <a class="btn" href="javascript:getReadNewRecord();"><i class="icon-file"></i>得到读取记录</a>
    </div>


    <div class="control-group">

        <label class="control-label">数据显示区域</label>
        <textarea  style="width: 500px; height: 200px"  rows="5" cols="5"  id="message1" ></textarea>

    </div>
    <button onclick="clearTextarea()"type="button">清空文本域</button>
    <button onclick="clearRedis()"type="button">清空缓存</button>
</div>

<script type="text/javascript">
    function clearTextarea(){
        $("#message1").val("");
    }

    function clearRedis(){
        var url =	"${ctx}/doorBase/clearRedis";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
                if(data.msg!=null){
                    var msgContent=$("#message1").val()+"\n"+data.msg;
                    $("#message1").val(msgContent);
                }
            }//end-callback
        });//end-ajax
    }

    function getRecond(){
        var url =	"${ctx}/doorBase/getRecond";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "no":$("#eqNo").val()
            },
            contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
                if(data.msg!=null){
                    var msgContent=$("#message1").val()+"\n"+data.msg;
                    $("#message1").val(msgContent);
                }

            }//end-callback
        });//end-ajax
    }
    function getOpenCardRecond(){
        var url =	"${ctx}/doorBase/getOpenCardRecond";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "cardNo":$("#cardNo").val()
            },
             contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
                if(data.msg!=null){
                    var msgContent=$("#message1").val()+"\n"+data.msg;
                    $("#message1").val(msgContent);
                }

            }//end-callback
        });//end-ajax
    }
    function getReadCardNoRecond(){
        var url =	"${ctx}/doorBase/getReadCardNoRecond";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "readCardNo":$("#readCardNo").val()
            },
             contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
                if(data.msg!=null){
                    var msgContent=$("#message1").val()+"\n"+data.msg;
                    $("#message1").val(msgContent);
                }

            }//end-callback
        });//end-ajax
    }
    function getReadNewRecord(){
        var url =	"${ctx}/doorBase/getReadNewRecord";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
             contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
                if(data.msg!=null){
                    var msgContent=$("#message1").val()+"\n"+data.msg;
                    $("#message1").val(msgContent);
                }

            }//end-callback
        });//end-ajax
    }


    function connetEq(){
        var url =	"${ctx}/doorBase/connetEq";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "ip":$("#eqIp").val(),
                "port":$("#eqPort").val(),
                "no":$("#eqNo").val(),
                "psw":$("#eqPass").val()
            },
            contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }
    function openCard(){
        var url =	"${ctx}/doorBase/openCard";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "cardNo":$("#cardNo").val(),
                "cardPass":$("#cardPass").val(),
                "openNum":$("#openNum").val(),
                "openState":$("#openState").val(),
                "openDoor":$("#openDoor").val()
            },
            contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }

    function readCardNo(){
        var url =	"${ctx}/doorBase/readCardNo";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "readCardNo":$("#readCardNo").val()
            },
            contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }

    function uploadCard(){
        var url =	"${ctx}/doorBase/uploadCard";
        console.info($("#inputForm").serialize());
        $.ajax({
            type: "POST",
            //dataType: "JSON",
            cache: false,
            url: url,
            data:$("#inputForm").serialize(),
            success: function (data) {
                console.info(data);
            }//end-callback
        });//end-ajax
    }

    function readNewRecord(){
        var url = "${ctx}/doorBase/readNewRecord";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            data: {
                "readNum":$("#readNum").val()
            },
            contentType: "application/json; charset=utf-8",
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }

    function getNewRecords(){
        var url =	"${ctx}/doorBase/getNewRecords";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }

    function getRecordsByIndex(){
        var url =	"${ctx}/doorBase/getRecordsByIndex";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }

    function deleteCard(){
        var url =	"${ctx}/doorBase/deleteCard";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }

    function add(){
        var url =	"${ctx}/doorBase/getCard";
        $.ajax({
            type: "GET",
            async: false,
            dataType: "JSON",
            cache: false,
            url: url,
            success: function (data) {
            }//end-callback
        });//end-ajax
    }
</script>

</body>
</html>

