<%@ tag language="java" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<!-- 需要页面引入以下文件 -->
<!--<script src="${ctxOther}/jquery-ui-1.12.1/jquery-ui.js" type="text/javascript" charset="utf-8"></script>
<script src="${ctxJs}/common.js" type="text/javascript"></script>
<script src="${ctxCommon}/common-js/ajaxfileupload.js"></script>-->
<%@ attribute name="type" type="java.lang.String" required="true" description="附件业务类型"%>
<%@ attribute name="model" type="java.lang.String" required="true" description="附件展现形式(file、files、image、images、flash、flashs)"%>

<%@ attribute name="maxSize" type="java.lang.String" required="false" description="最大文件大小"%>
<%@ attribute name="imgWidth" type="java.lang.String" required="false" description="最佳(最大)图片文件宽度"%>
<%@ attribute name="imgHeight" type="java.lang.String" required="false" description="最佳(最大)图片文件高度"%>

<%@ attribute name="theme" type="java.lang.String" required="false" description="上传插件主题"%>
<%@ attribute name="cssStyle" type="java.lang.String" required="false" description="模板块样式"%>
<%@ attribute name="postfix" type="java.lang.String" required="false" description="页面有多个上传框时，使用后缀区分"%>


<%@ attribute name="isshow" type="java.lang.Boolean" required="false" description="显示文件"%>
<%@ attribute name="isshows" type="java.lang.Boolean" required="false" description="显示文件列表"%>

<%@ attribute name="seletorHids" type="java.lang.String" required="${((isshow eq true) || (isshows eq true))?true:false }" description="文件地址隐藏区域"%>
<%@ attribute name="seletorItem" type="java.lang.String" required="${(isshow eq true)?true:false }" description="文件显示选择器"%>
<%@ attribute name="seletorItems" type="java.lang.String" required="${(isshows eq true)?true:false }" description="列表显示选择器"%>
<%@ attribute name="fileitem" type="java.lang.String" required="false" description="初始化文件数据"%>
<%@ attribute name="fileitems" type="java.util.List" required="false" description="初始化文件列表数据"%>


<%@ attribute name="multiple" type="java.lang.Boolean" required="false" description="是否允许多选"%>
<%@ attribute name="filetype" type="java.lang.String" required="false" description="files、images、flash、thumb"%>


<!-- themeLayer 定义主题布局  -->
<!-- default   -->
<c:set var="themeLayer" value="${(empty isshows)? 'default' : theme}" ></c:set>
<!-- isshow 是否显示单选个文件 -->
<c:set var="isshow" value="${(empty isshow)? true : isshow}" ></c:set>
<!-- isshows 是否显示列表  -->
<c:set var="isshows" value="${(empty isshows)? false : isshows}" ></c:set>
<!-- maxSize 默认文件大小  -->
<c:set var="maxSize" value="${(empty maxSize)? false : '10485760'}" ></c:set>

<c:if test="${(empty theme) || (theme eq 'default')}">
	<c:set var="themeLayer" value="default" ></c:set>
	<!-- themeLayer 定义主题布局上传按钮样式  -->
	<c:set var="themeLayerBtn" value="uploadFiles-btn" ></c:set>
	<!-- themeLayer 定义主题布局按钮ID  -->
	<c:set var="themeLayerBtnId" value="upload" ></c:set>
	<!-- themeLayer 定义主题布局上传文件框ID  -->
	<c:set var="themeLayerFileID" value="fileToUpload" ></c:set>
	<!-- themeLayer 定义主题布局上传文件框ID  -->
	<c:set var="themeLayerCseletor" value="${cseletor}" ></c:set>
	<!-- multiple 是否多文件上传  -->
	<c:set var="multiple" value="false" ></c:set>

	<a href="javascript:;"  class="btn ${themeLayerBtn}" id="${themeLayerBtnId}${postfix}">上传附件</a>
	<input type="file" style="display: none" name="fileName" id="${themeLayerFileID}${postfix}" />

	<c:if test="${isshow}">
		<div id="fileitem-${themeLayer}${postfix}" style="display: none;">
			<c:if test="${model eq 'image' }">
				<img src="${(empty fileitem)?'${ctxImages}/upload.png':fileitem}" style="${(empty cssStyle)?'width:100%; height: 100%;':cssStyle}" />
			</c:if>

			<c:if test="${(model eq 'file')}">
				file
			</c:if>

			<c:if test="${model eq 'flash' }">
				flash
			</c:if>
		</div>
	</c:if>

	<c:if test="${isshows}">
		<div id="fileitems-${themeLayer}${postfix}" style="display: none;">
			<c:if test="${model eq 'images' }">
				<ul style="${cssStyle}">
					<c:forEach items="${fileitems}" var="item" varStatus="status">
						<li><p>
							<img src="${ctxImg}/filetype/${item.imgType}.png">
							<a  href="javascript:void(0)"  onclick="downfile('${item.arrUrl}','${item.arrName}');return false">${item.arrName}</a>
							<span class="del" onclick="delUrlFile(this,'${item.id}','${item.arrUrl}','delFile');"><i class='icon-remove-sign'></i></span>
						</p></li>
					</c:forEach>
				</ul>
			</c:if>
			<c:if test="${model eq 'files' }">
				files
			</c:if>
			<c:if test="${model eq 'flashs' }">
				flashs
			</c:if>
		</div>
	</c:if>
</c:if>


<c:if test="${themeLayer eq 'default'}">
	<script type="text/javascript">
		$(function(){
			//显示单文件
			var isshow = '${isshow}';
			//显示多文件
			var isshows = '${isshows}';
			//最大文件大小
			var maxSize = '${maxSize}';
			//最佳(最大)图片文件宽度
			var imgWidth = '${imgWidth}';
			//最佳(最大)图片文件高度
			var imgHeight = '${imgHeight}';
			var type = '${type}';
			var multiple = '${multiple}';
			//上传按钮ID
			var themeLayerBtnId = '${themeLayerBtnId}${postfix}';
			var domBtn = $("#"+themeLayerBtnId);

			//上传file文本框ID
			var themeLayerFileID = '${themeLayerFileID}${postfix}';
			var domFile = $("#"+themeLayerFileID);

			//上传file地址隐藏域选择器
			var themeLayerSeletorHids = "${seletorHids}";
			var domFileHid = $(themeLayerSeletorHids);

			//初始化数据模板
			var themeLayerFileTplitem = 'fileitem-${themeLayer}${postfix}';
			var domShowTplitem = $("#"+themeLayerFileTplitem);
			//初始化数据容器选择器
			var themeLayerSeletoritem = '${seletorItem}';
			var domShowitem = $(themeLayerSeletoritem);

			//初始化数据列表模板
			var themeLayerFileTplitems = 'fileitems-${themeLayer}${postfix}';
			var domShowTplitems = $("#"+themeLayerFileTplitems);
			//初始化数据列表容器模板选择器
			var themeLayerSeletoritems = '${seletorItems}';
			var domShowitems = $(themeLayerSeletoritems);


			if(isshow == 'true'){
				if(domShowitem == null || domShowitem == undefined){
					console.info("isshow=true时domShowitem必填");
				}
				fileDownUp(domBtn, domFile, type, domShowitem, false, "delFile");
			}
			if(isshows == 'true'){
				if(domShowitems == null || domShowitems == undefined){
					console.info("isshows=true时domShowitems必填");
				}
				fileDownUp(domBtn, domFile, type, domShowitems, true, "delFile");
			}


			var prePath = $(domFile).val();
			var fileUp = false;

			/**
			 * 初始化上传组件-模板
			 * pslector包含的容器
			 * isMulShows显示模式
			 */
			function initShowItem(pslector, isMulShows){
				if(isMulShows){
					$(pslector).html($(domShowTplitems).html());
				}else{
					$(pslector).html($(domShowTplitem).html());
				}
				return $(pslector);
			}
			/**
			 * 更新组件显示
			 * pslector包含的容器
			 * hidslector回调存放地址的隐藏域
			 * datas显示渲染数据
			 */


			function updateShowItem(pslector, hidslector, datas){
				$(pslector).find("img").attr("src", ftpHttpUrlByIp("${fns:getConfig('ftp.httpUrl')}", datas.arrUrl));
				$(pslector).find("img").attr("title", datas.fileName);
				$(hidslector).val(datas.arrUrl);
				return $(pslector);
			}

			/**
			 * 初始化上传组件
			 * dbtn触发上传事件按钮
			 * dFile上传文本框
			 * type 附件业务类型
			 * pslector 显示父容器
			 * ismultiple 是否多文件显示
			 * delFile 删除文件操作的函数名
			 */
			function fileDownUp(dbtn, dFile, type, pslector, isMulShows, delFile){
				var showItems = initShowItem(pslector, isMulShows);

				$(dbtn).on('click', function() {
				    $(dFile).click();
				});

				$(dFile).on('change', function() {
					showModalMessage(0, "文件大小超过不能超过10M");
					var f = document.getElementById(dFile.attr("id")).files;
					if(f[0].size> maxSize){
					//if(f[0].size>10485760){
						 showModalMessage(0, "文件大小超过不能超过10M");
						 return;
					}
				    if(fileUp){
				        return false;
				    }
				    fileUp=true;
				    var curPath = $(dFile).val();
				    if (prePath == curPath) {
				        fileUp=false;
				        showModalMessage(0, "不能上传相同文件名");
				        return false;
				    }
				    var mb = myBrowser();
				    if ("IE" == mb) {
				    	curPath=curPath.substring(curPath.lastIndexOf('\\')+1);
				    }
				    var arrNames=$("input[name='arrName']");
				    if(arrNames.length>0){
				    	for(var i=0;i<arrNames.length;i++){
				    		 if (arrNames[i].value == curPath) {
				    			 fileUp=false;
				    			 showModalMessage(0, "不能上传相同文件名");
				    			 return false;
				    		 }
			    		}
				    }
				    prePath = curPath;
				    var ftpIds=$("input[name='ftpId']");
				    var ftpId="";
				    if(ftpIds.length>0){
				        ftpId=ftpIds[0].value;
				    }
				    $.ajaxFileUpload({
				        url:'${ctx}/ftp/urlLoad/file',
				        secureuri:false,
				        fileElementId:$(dFile).attr("id"),//file标签的id
				        dataType: 'json',//返回数据的类型
				        data:{type:type,ftpId:ftpId,maxSize:maxSize,imgWidth:imgWidth,imgHeight:imgHeight},//一同上传的数据
				        success: function (data, status) {
				            //把图片替换
				        	fileUp=false;
				            var obj = jQuery.parseJSON(data);
				            var state=obj.state;
				            if(state=="1"){
				                //针对谷歌浏览器会记住上传文件名导致不触发onchange方法
				                $(dFile).val("");
				                /*var $btn="<span class='del' onclick='"+delFile+"(this,null,\""+obj.arrUrl+"\");'>" +
				                    "<input type='hidden'  name='arrUrl' value='"+obj.arrUrl+"'/>" +
				                    "<input type='hidden'  name='ftpId' value='"+obj.ftpId+"'/>" +
				                    "<input type='hidden' name='arrName' value='"+obj.fileName+"'/>" +
				                    "<i class='icon-remove-sign'></i>" +
				                    "</span>";
				                var extname = obj.fileName.split('.').pop().toLowerCase();
				                switch(extname) {
				                    case "xls":
				                    case "xlsx":
				                        extname = "excel";
				                        break;
				                    case "doc":
				                    case "docx":
				                        extname = "word";
				                        break;
				                    case "ppt":
				                    case "pptx":
				                        extname = "ppt";
				                        break;
				                    // 我不太确定这个文件格式
				                    //case "project":
				                    case "jpg":
				                    case "jpeg":
				                    case "gif":
				                    case "png":
				                    case "bmp":
				                        extname = "image";
				                        break;
				                    case "rar":
				                    case "zip":
				                    case "txt":
				                    case "project":
				                        // just break
				                        break;
				                    default:
				                        extname = "unknow";
				                } */

				            	updateShowItem(pslector, domFileHid, obj);
				            }else{
				            	showModalMessage(0, obj.msg);
				            }
				        },
				        error: function (data, status, e) {
				            fileUp=false;
				            alert(e);
				        }
				    });
				});

			}

			function delFile(ob,id,url){
			    $.ajax({
			        type: "GET",
			        url: "${ctxFront}/delload",
			        data: "id="+id+"&fileName="+encodeURIComponent(url),
			        contentType: "application/json; charset=utf-8",
			        dataType: "json",
			        success: function (data) {
			            if(data.state==1){
			            	prePath="";
			                $(ob).parent().remove();
			            }else{
			            	showModalMessage(0, data.msg);
			            }
			        },
			        error: function (msg) {
			            alert(msg);
			        }
			    });
			}

			function delBackFile(ob,id,url){
			    $.ajax({
			        type: "GET",
			        url: "${ctx}/delload",
			        data: "id="+id+"&fileName="+encodeURIComponent(url),
			        contentType: "application/json; charset=utf-8",
			        dataType: "json",
			        success: function (data) {
			            if(data.state==1){
			            	prePath="";
			                $(ob).parent().remove();
			            }else{
			            	showModalMessage(0, data.msg);
			            }
			        },
			        error: function (msg) {
			            alert(msg);
			        }
			    });
			}

			function delUrlFile(ob,id,url,delFileUrl){
			    $.ajax({
			        type: "GET",
			        url: delFileUrl,
			        data: "id="+id+"&arrUrl="+encodeURIComponent(url),
			        contentType: "application/json; charset=utf-8",
			        dataType: "json",
			        success: function (data) {
			            if(data.ret==1){
			            	prePath="";
			                $(ob).parent().remove();
			            }else{
			            	showModalMessage(0, data.msg);
			            }
			        },
			        error: function (msg) {
			            alert(msg);
			        }
			    });
			}
			function downfile(url,fileName){
			    location.href="${ctxFront}/loadUrl?url="+url+"&fileName="+encodeURI(encodeURI(fileName));
			}
		});
	</script>
</c:if>
