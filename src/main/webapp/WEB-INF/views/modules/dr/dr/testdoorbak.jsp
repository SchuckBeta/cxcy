<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
	<title>测试门禁</title>
	<meta name="decorator" content="default"/>
	<script src="${ctxStatic}/jquery/jquery-1.8.3.min.js" type="text/javascript"></script>
</head>
<body>
    <div class="btn-toolbar breadcrumb">
        <br class="btn-group">

        </br></br></br></br>

        <input type="text" id="getCardCardNo" >
        <input type="text" id="getCardEeqId" >
        <a class="btn" href="javascript:add();"><i class="icon-file"></i>读取卡的信息</a>
        </br>
        <a class="btn" href="javascript:deleteCard();"><i class="icon-file"></i> 删除卡</a></br>
        <a class="btn" href="javascript:getRecordsByIndex();"><i class="icon-file"></i>从指定索引位置开始读取打卡记录</a></br>
      <a class="btn" href="javascript:getNewRecords();"><i class="icon-file"></i>读取新打卡记录</a></br></br>
        <a class="btn" href="javascript:disposeDrCardRecord();"><i class="icon-file"></i>出入记录</a></br></br>

        <div class="form-actions">
       <form id="inputForm" method="post" class="form-horizontal">
            <div class="control-group">
                <label class="control-label">名称:</label><input name="no" htmlEscape="false" maxlength="200" class="required"/>
            </div>
           <input  onclick="uploadCard()" class="btn btn-primary" type="button" value="开卡"/>&nbsp;
        </form>

       <%-- <div class="control-group">
            <label class="control-label">数据显示区域</label>
            <textarea  style="width: 500px; height: 200px"  rows="5" cols="5"  id="message1" >消息显示区域</textarea>
        </div>--%>

        </div>
    </div>
	</div>
	<script type="text/javascript">
        function uploadCard(){
            var url =	"${ctx}/door/uploadCard";
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


        function getNewRecords(){
            var url =	"${ctx}/door/getNewRecords";
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
            var url =	"${ctx}/door/getRecordsByIndex";
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

        function disposeDrCardRecord(){
            var url =	"${ctx}/door/disposeDrCardRecord";
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
            var url =	"${ctx}/door/deleteCard";
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
            var url =	"${ctx}/door/getCard";
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

