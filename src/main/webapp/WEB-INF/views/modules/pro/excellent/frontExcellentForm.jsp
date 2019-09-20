<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <title>${frontTitle}</title>
    <%--<link rel="stylesheet" type="text/css" href="${ctxCommon}/common-css/bootstrap.min.css">--%>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/bootstrap/2.3.1/awesome/font-awesome.min.css">
    <link href="${ctxCss}/excellent/lessonProjectEditCommon.css" rel="stylesheet">
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/creatives.css">
    <%--<link rel="stylesheet" href="${ctxCss}/excellent/frontBime.css">--%>
    <link rel="stylesheet" href="${ctxCss}/frontCyjd/frontBime.css">
    <!-- 配置文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>
    <script src="${ctxOther}/jquery.form.min.js" type="text/javascript" charset="utf-8"></script>
    <style>
        .preview {
            height: auto;
        }

        .icon-home {
            font-size: 16px;
            margin-right: 2px;
        }

        .btn-tool {
            text-align: center;
            margin-top: 20px;
        }

        body {
            background: white;
        }
    </style>
</head>
<body>
<form:form id="projectEditForm" modelAttribute="es" action="save" method="post">
    <input type="hidden" name="id" value="${es.id }"/>
    <input type="hidden" name="foreignId" value="${es.foreignId }"/>
    <input type="hidden" name="type" value="${es.type }"/>
    <input type="hidden" name="subType" value="${es.subType }"/>
    <input type="hidden" name="managed" value="${es.managed }"/>
    <input type="hidden" id="isRelease" name="isRelease" value="${es.isRelease }"/>
    <div class="space-container preserve">
        <ol class="breadcrumb" style="margin-top: 0">
            <li><a href="${ctxFront}"><i class="icon-home"></i>首页</a></li>
            <li class="active">项目展示</li>
        </ol>
        <div class="preserve-detail">
            <h2>${projectInfo.name }</h2>
            <c:if test="${es.type=='0000000075' }">
                <p><span>项目来源：${fns:getDictLabel(es.subType, 'project_style', '')}</span><span>发布时间：<fmt:formatDate
                        value="${es.releaseDate}" pattern="yyyy-MM-dd"/>${projectInfo.createDate }</span></p>
            </c:if>
            <c:if test="${es.type=='0000000076' }">
                <p><span>项目来源：${fns:getDictLabel(es.subType, 'competition_type', '')}</span><span>发布时间：<fmt:formatDate
                        value="${es.releaseDate}" pattern="yyyy-MM-dd"/>${projectInfo.createDate }</span></p>
            </c:if>
            <div class="row preserve-appear">
                <div class="col-md-3">
                    <img id="coverImg_show"
                         src="${not empty es.coverImg ? fns:ftpImgUrl(es.coverImg) : '/images/cover-pic.png'}" alt="">
                    <input type="hidden" id="coverImg" name="coverImg" value="${es.coverImg}"/>
                </div>
                <div class="col-md-6">
                    <c:if test="${es.type=='0000000076' }">
                        <p>荣获奖项：优秀项目</p>
                    </c:if>
                    <p>项目负责人：${projectInfo.lname }</p>
                    <p>学院：${projectInfo.oname }</p>
                    <p>指导教师：
                        <c:forEach items="${projectTeacherInfo}" var="item">
                            <span>${item.uname }</span><span>${item.oname }</span><span>${item.post_title }</span> </c:forEach>
                    </p>
                    <p class="key-word">
                        <span>关键字：</span>
                        <input type="text" class="form-control form-keyword" id="formKeyword" autocomplete="off"
                               placeholder="输入关键字按回车键添加"/>
                    <p>
                        <c:forEach items="${es.keywords}" var="item">
	                    		<span class="keyword">
	                    			<input name="keywords" value="${item}" type="hidden"/>
		                    		<span>${item}</span>
		                    		<a class="delete-keyword" href="javascript:void(0);" onclick="delKey(this);">×</a>
	                    		</span>
                        </c:forEach>
                    </p>
                    </p>
                </div>
            </div>
            <p><span>浏览量：${es.views}</span><span>点赞数：${es.likes}</span><a id="upload" class="btn btn-primary btn-small">更新封面</a>
            </p>
            <p class="layout-template">
                <span>选择布局模板：</span>
                <select id="temp" class="form-control form-template" onchange="ontempchange()">
                    <option tempcontent="" value="" selected="selected">--选择布局模板--</option>
                    <c:forEach items="${fns:getExcTemplateList()}" var="item" varStatus="status">
                        <option tempcontent="${item.content}" value="${item.value}">${item.name}</option>
                    </c:forEach>
                </select>
            <div class="col-md-4 col-error">
            </div>
            </p>
        </div>

        <div class="edit-docu">
            <span><i class="icon-require">*</i>正文内容：</span>
            <div class="docu-word">
                <script id="container" name="content" type="text/plain" style="width:800px;height:600px">
        					${es.content}

                </script>
            </div>
        </div>
        <div class="btn-tool">
            <button type="button" class="btn btn-primary-oe" onclick="preview()">预览</button>
            <button id="btnSave" type="button" class="btn btn-primary-oe">保存</button>
            <c:if test="${fromPage=='gcontest'}">
                <button type="button" class="btn btn-primary-oe"
                        onclick="javascript:location.href='/f/gcontest/gContest/list'">返回
                </button>
            </c:if>
            <c:if test="${fromPage=='project'}">
                <button type="button" class="btn btn-primary-oe"
                        onclick="javascript:location.href='/f/project/projectDeclare/list'">返回
                </button>
            </c:if>
            <input type="hidden" id="fromPage" value="${fromPage }">
        </div>
    </div>

</form:form>
<sys:frontCutImageCommon btnid="upload" column="coverImg" imgId="coverImg_show" filepath="excellentCoverImg"
                         className="modal-avatar"></sys:frontCutImageCommon>
<script src="${ctxJs}/excellent/frontLessonProjectEditCommon.js" type="text/javascript"></script>
<div id="dialog-message" title="信息" style="display: none;">
    <p id="dialog-content"></p>
</div>
<script>
    $(function () {
        var cropperPreviewFlag = false;
        var $previews = $('.preview-avatar');
        $(btnid).uploadAvatar({
            cropperOption: {
                aspectRatio: 16 / 9,
                crop: function (e) {
                    if (!cropperPreviewFlag) {
                        cropperPreviewFlag = true;
                        var $clone = $('img.cropper-hidden').clone().removeClass('cropper-hidden');
                        $clone.css({
                            display: 'block',
                            width: '100%',
                            minWidth: 0,
                            minHeight: 0,
                            maxWidth: 'none',
                            maxHeight: 'none'
                        });
                        $previews.html($clone);
                    }
                    var imageData = $(this).cropper('getImageData');
                    var previewAspectRatio = e.width / e.height;
                    $previews.each(function () {
                        var $preview = $(this);
                        var previewWidth = $preview.width();
                        var previewHeight = previewWidth / previewAspectRatio;
                        var imageScaledRatio = e.width / previewWidth;
                        $preview.height(previewHeight).find('img').css({
                            width: imageData.naturalWidth / imageScaledRatio,
                            height: imageData.naturalHeight / imageScaledRatio,
                            marginLeft: -e.x / imageScaledRatio,
                            marginTop: -e.y / imageScaledRatio
                        });
                    });
                }
            },
            success: function (data) {
                var $inputPhoto = $('input[name="' + column + '"]');
                $(imgId).attr('src', data.url);
                if ($inputPhoto.size() > 0) {
                    $inputPhoto.val(data.ftpUrl)
                } else {
                    $('<input name="' + column + '" type="hidden" value="' + data.ftpUrl + '">').insertAfter($(imgId));
                }
            }
        })
    })
</script>
</body>