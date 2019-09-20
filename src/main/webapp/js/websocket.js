var webSocket;
$(function(){
	var userid=$("#userid_27218e9429b04f06bb54fee0a2948350").val();
	if(userid&&userid!=""){
		var curWwwPath=window.document.location.href;
		
		//获取主机地址之后的目录如：/Tmall/index.jsp
		var pathName=window.document.location.pathname;
		var pos=curWwwPath.indexOf(pathName);
		
		//获取主机地址，如： http://localhost:8080
		var localhostPath=curWwwPath.substring(0,pos);
		try {
			webSocket = new WebSocket('ws://'+localhostPath.replace("http://","")+'/websocket?userid='+userid);
			webSocket.onerror = function(event) {
			};
			//与WebSocket建立连接
			webSocket.onopen = function(event) {
			};
			//处理服务器返回的信息
			webSocket.onmessage = function(event) {
				notifyModule.updateItem(JSON.parse(event.data));
				getUnreadCountForHead();
			};
		}catch (e){
			console.warn('不支持webSocket,请更换浏览器版本或者使用谷歌浏览器')
		}
		
	}
});