<%@ tag language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!-- 需要页面引入以下文件 -->
<%@ attribute name="logo" type="java.lang.String" required="false" description="文件列表" %>
<%@ attribute name="readonly" type="java.lang.Boolean" required="false" description="是否只读" %>
<%@ attribute name="url" type="java.lang.String" required="false" description="地址" %>
<%@ attribute name="inputName" type="java.lang.String" required="false" description="input 上传名字" %>
<%@ attribute name="filepath" type="java.lang.String" required="false" description="input 上传路径" %>
<%@ attribute name="dir" type="java.lang.String" required="false" description="上传文件夹" %>
<%@ attribute name="fileVal" type="java.lang.String" required="false" description="上传文件夹" %>
<%@ attribute name="logoId" type="java.lang.String" required="false" description="上传文件夹" %>

<c:set var="idsuffix" value="${fns:getUuid()}"></c:set>
<div id="uploaderLogo${idsuffix}" class="uploader-logo-container">
    <c:if test="${empty readonly or readonly==false}">
        <div class="uploader-logo">
            <img class="uploader-logo_pic"
                 src="${empty logo ? '/images/upload-default-image1.png':fns:ftpImgUrl(logo)}">
            <a class="btn-delete-logo" href="javascript: void(0);"><img src="${ctxImg}/btn-hover-delete-file.png"> </a>
            <div class="uploader-logo-layer">
                <a class="btn-uploader-logo" href="javascript: void (0);">${empty logo ?'上传图片' : '更换图片'}</a>
            </div>
            <input type="hidden" name="${inputName}">
        </div>
        <div id="modal${idsuffix}" class="modal model-uploder-logo hide">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close btn-cancel" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <span>更换项目logo</span>
                    </div>
                    <div class="modal-body">
                        <button type="button"
                                class="btn btn-primary btn-sm btn-small handle-change-pic">${empty logo ? '上传图片' : '更换图片'}</button>
                        <input type="file" style="display: none" accept="image/jpeg,image/png">
                        <div class="uploader-preview-cropper">
                            <img class="cropper-img"
                                 src="${empty logo ? '/images/upload-default-image1.png':fns:ftpImgUrl(logo)}">
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-default btn-sm btn-small btn-cancel" data-dismiss="modal"
                                aria-hidden="true">取消
                        </button>
                        <button type="button" class="btn btn-primary btn-sm btn-small submit-pic">保存</button>
                    </div>
                </div>
            </div>
        </div>
    </c:if>
    <c:if test="${readonly}">
        <div class="project-logo-box">
            <img src="${empty logo ? '/images/upload-default-image1.png':fns:ftpImgUrl(logo)}">
        </div>
    </c:if>
</div>
<script type="text/javascript">

    +function ($) {


        function UploaderLogo(element, option) {
            this.$element = $(element);
            this.options = $.extend(true, {}, UploaderLogo.DEFAULT, option);
            this.$modal = $('#modal' + this.options.id);
            this.$cropperImg = this.$modal.find('.cropper-img');
            this.$uploaderLogoPic = this.$element.find('.uploader-logo_pic');
            this.$btnUploaderLogo = this.$element.find('.btn-uploader-logo');
            this.$handleChangePic = this.$modal.find('.handle-change-pic');
            this.$inputFile = this.$modal.find('input[type="file"]');
            this.$submitPic = this.$modal.find('.submit-pic');
            this.$btnCancel = this.$modal.find('.btn-cancel');
            this.$btnUploaderLogo.on('click', $.proxy(this.showModal, this));
            this.$fileVal = this.$element.find('input[name="' + this.options.inputName + '"]');
            this.$deleteLogo = this.$element.find('.btn-delete-logo');

            this.$handleChangePic.on('click', $.proxy(this.openFile, this));

            this.$inputFile.on('change', $.proxy(this.changeFile, this));

            this.$submitPic.on('click', $.proxy(this.uploadLogo, this));

            this.$btnCancel.on('click', $.proxy(this.cancel, this));

            this.$deleteLogo.on('click', $.proxy(this.deleteLogo, this));

            this.init()
        }

        UploaderLogo.DEFAULT = {
            defaultUrl: '/images/upload-default-image1.png'
        };

        UploaderLogo.prototype.init = function () {
            this.cropper();
        }

        UploaderLogo.prototype.showModal = function (e) {
            e.stopPropagation();
            e.preventDefault();
            this.$modal.modal('show');
            this.$cropperImg.cropper('replace', this.$uploaderLogoPic.attr('src'));
        }

        UploaderLogo.prototype.cropper = function () {
            this.$cropperImg.cropper({
                aspectRatio: 1,
                viewMode: 1,
                crop: function (e) {
                }
            });
        }

        UploaderLogo.prototype.openFile = function (e) {
            e.stopPropagation();
            e.preventDefault();
            this.$inputFile.trigger('click')
        }

        UploaderLogo.prototype.changeFile = function (e) {
            e.stopPropagation();
            e.preventDefault();
            var files = e.target.files;
            var fileReader, file;
            var $cropperImg;
            if (files && files.length > 0) {
                file = files[0];
                fileReader = new FileReader();
                $cropperImg = this.$cropperImg;
                fileReader.onload = function (event) {
                    $cropperImg.cropper("reset", true).cropper('replace', event.target.result);
                    e.target.files = null;
                };
                fileReader.readAsDataURL(file)
            } else {
                e.target.files = null;
            }
        };

        UploaderLogo.prototype.uploadLogo = function (e) {
            e.stopPropagation();
            e.preventDefault();
            var url = this.options.url;
            var cropperData = this.$cropperImg.cropper('getData');
            var formData = new FormData();
            var $input = this.$inputFile;
            var files = $input[0].files;
            var $uploaderLogoPic = this.$uploaderLogoPic;
            var $modal = this.$modal;
            var $fileVal = this.$fileVal;
            var file;
            var $submitPic = this.$submitPic;
            var $handleChangePic = this.$handleChangePic;
            var $btnCancel = this.$btnCancel
            if (files && files.length > 0) {
                file = files[0];
                formData.append(this.options.fileVal, file);
                for (var k in cropperData) {
                    if (cropperData.hasOwnProperty(k)) {
                        url = url.replace(/\{\{(\w+)\}\}/, function ($1, $2) {
                            if (typeof cropperData[$2] != 'undefined') {
                                return parseInt(cropperData[$2])
                            }
                        })
                    }
                }
                $submitPic.prop('disabled', true);
                $handleChangePic.prop('disabled', true);
                $btnCancel.prop('disabled', true);
                $.ajax({
                    type: 'POST',
                    url: url,
                    data: formData,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if (data.state.toLowerCase() === 'success') {
                            $fileVal.val(data.ftpUrl);
                            $uploaderLogoPic.attr('src', data.url);
                            $modal.modal('hide');
                            $input[0].files = null;
                        }
                        $submitPic.prop('disabled', false);
                        $handleChangePic.prop('disabled', false);
                        $btnCancel.prop('disabled', false)
                    },
                    error: function (error) {
                        $submitPic.prop('disabled', false);
                        $handleChangePic.prop('disabled', false);
                        $btnCancel.prop('disabled', false)
                    }
                })
            }
        }

        UploaderLogo.prototype.cancel = function () {
            var $input = this.$inputFile;
            $input[0].files = null;
        };

        UploaderLogo.prototype.deleteLogo = function (e) {
            e.stopPropagation();
            e.preventDefault();
            var options = this.options;
            var $fileVal = this.$fileVal;
            var $input = this.$inputFile;
            var deleteUrl = this.options.deleteUrl;
            var $uploaderLogoPic = this.$uploaderLogoPic;
            var url = $fileVal.val() || options.logoUrl;
            var defaultUrl = this.options.defaultUrl;

            if (url == null || url == "" || url == this.options.defaultUrl) {
                return;
            }
            if(/\/temp\/\w+/.test(url)){
                $uploaderLogoPic.attr('src', defaultUrl);
                $fileVal.val('');
                $input[0].files = null;
                return false;
            }

            dialogCyjd.createDialog(0, "确定删除?", {
                dialogClass: 'dialog-cyjd-container none-close',
                buttons: [{
                    text: '确定',
                    'class': 'btn btn-sm btn-primary',
                    click: function () {
                        //向服务器发出请求，删除文件
                        $.ajax({
                            type: 'post',
                            url: deleteUrl,
                            data: {
                                id: options.logoId,
                                url: url,
                            },
                            success: function (data) {
                                if(data){
                                    $uploaderLogoPic.attr('src', defaultUrl);
                                    $fileVal.val('');
                                    $input[0].files = null;
                                }else {
                                    alertx('删除失败')
                                }
                            }
                        });
                        $(this).dialog('close');
                    }
                }, {
                    text: '取消',
                    'class': 'btn btn-sm btn-default',
                    click: function () {
                        $(this).dialog('close');
                    }
                }]
            });
        }


        var uploaderLogo = new UploaderLogo(document.getElementById('uploaderLogo${idsuffix}'), {
            id: '${idsuffix}',
            inputName: '${inputName}',
            url: $frontOrAdmin + '/ftp/ueditorUpload/${dir}?folder=${filepath}&x={{x}}&y={{y}}&width={{width}}&height={{height}}',
            fileVal: '${empty fileVal ? 'upfile': fileVal}',
            deleteUrl: $frontOrAdmin+'/ftp/ueditorUpload/delFile',
            logoId: '${logoId}',
            logoUrl: '${logo}'
        })
    }(jQuery)

</script>