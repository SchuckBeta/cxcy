<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<%@ attribute name="btnname" type="java.lang.String" required="true" description="按钮名称"%>
<%@ attribute name="likestagid" type="java.lang.String" required="true" description="点赞数元素id"%>
<%@ attribute name="foreignid" type="java.lang.String" required="true" description="点赞信息foreign_id"%>
<%@ attribute name="disable" type="java.lang.Boolean" required="true" description="是否可用"%>
<c:set var="idsuffix" value="${fns:getUuid()}"></c:set>
<c:set var="curuserid" value="${fns:getUser()}"></c:set>
<c:if test="${not empty curuserid and curuserid!=foreignid}">
	<c:if test="${disable==true}">
		<%--<button id="${idsuffix}" type="button"  class="btn btn-primary-oe disabled">${btnname}</button>--%>
		<img class="btn-good-at disabled"  id="${idsuffix}" src="${ctxImages}/good-disabled.png">
	</c:if>
	<c:if test="${disable==false}">
		<%--<button id="${idsuffix}" type="button" onclick="saveLikeForUserInfo()" class="btn btn-primary-oe">${btnname}</button>--%>
		<img class="btn-good-at" id="${idsuffix}" onclick="saveLikeForUserInfo()" src="${ctxImages}/good-clickable.png">
	</c:if>
</c:if>
<script>
function saveLikeForUserInfo(){
	$("#${idsuffix}").removeAttr("onclick");
	$("#${idsuffix}").addClass("disabled");
	$("#${idsuffix}").attr('src','/images/good-disabled.png');
	var data= {
			foreignId:'${foreignid}'
	    };
	$.ajax({
        type: "POST",
        url: "/f/interactive/sysLikes/saveForUserInfo",
        data: JSON.stringify(data),
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (data) {
            if(data.ret==1){
            	var likes=$("#${likestagid}");
            	likes.html(parseInt(likes.html())+1);
            	showModalMessage(1, data.msg);
            }else{
            	showModalMessage(0, data.msg);
            }
        },
        error: function (msg) {
            showModalMessage(0, msg);
        }
    });
}
</script>