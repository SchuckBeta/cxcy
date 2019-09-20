<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <meta name="decorator" content="site-decorator"/>
    <link rel="stylesheet" type="text/css" href="${ctxCss}/sco/frontCreditModule.css">
    <link rel="stylesheet" type="text/css" href="${ctxCss}/project/form.css"/>
    <script type="text/javascript" src="${ctxStatic}/webuploader/webuploader.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/sco/upload-accessoryStep.js"></script>
    <title>${frontTitle}</title>
    <style>
        .contest-content .input-box > .form-control-static {
            padding: 7px 0;
        }
        button{
            width: auto;
            height: auto;
        }

        .table-pro-work>tbody>tr>td{
            line-height: 30px;
        }

        .table-theme-default>thead>tr{
            background-color: #f4e6d4;
        }

        .btn-default-oe {
            color: #333;
            background-color: #fff;
            border-color: #ccc
        }

        .btn-default-oe.focus, .btn-default-oe:focus {
            color: #333;
            background-color: #e6e6e6;
            border-color: #8c8c8c
        }

        .btn-default-oe:hover {
            color: #333;
            background-color: #e6e6e6;
            border-color: #adadad
        }

        .step-indicator {
            margin-bottom: 20px;
            line-height: 30px;
        }

        a.step {
            display: block;
            float: left;
            font-weight: bold;
            background: #f7f7f7;
            padding-right: 10px;
            height: 30px;
            width: 240px;
            text-align: center;
            line-height: 32px;
            margin-right: 33px;
            position: relative;
            text-decoration: none;
            color: #4c4b4a;
            cursor: default;
        }
        .step:before {
            content: "";
            display: block;
            width: 0;
            height: 0;
            position: absolute;
            top: 0;
            left: -30px;
            border: 15px solid transparent;
            border-color: #f7f7f7;
            border-left-color: transparent;
        }
        .step:after {
            content: "";
            display: block;
            width: 0;
            height: 0;
            position: absolute;
            top: 0;
            right: -30px;
            border: 15px solid transparent;
            border-left-color: #f7f7f7;
        }
        .step:first-of-type {
            border-radius: 2px 0 0 2px;
            padding-left: 15px;
        }
        .step:first-of-type:before {
            display: none;
        }
        .step:last-of-type {
            border-radius: 0 2px 2px 0;
            margin-right: 25px;
            padding-right: 15px;
        }
        .step:last-of-type:after {
            display: none;
        }
        a.step:hover{
            text-decoration: none;
        }
        .step.completed {
            background: #ffdacf;
            color: #de3b0a;
            cursor: pointer;
        }
        .step.completed:before {
            border-color: #ffdacf;
            border-left-color: transparent;
        }
        .step.completed:after {
            border-left-color: #ffdacf;
        }
        .step.completed:hover {
            background: #ffdacf;
            border-color: #ffdacf;
            color: #de3b0a;
            text-decoration: none;
        }
        .step.completed:hover:before {
            border-color: #ffdacf;
            border-left-color: transparent;
        }
        .step.completed:hover:after {
            border-left-color: #ffdacf;
        }

        .step-row{
            width: 565px;
            height: 30px;
            margin-left: auto;
            margin-right: auto;
            margin-bottom: 20px;
        }

    </style>
</head>
<body>


<div class="container project-view-contanier">
    <input type="hidden" id="pageType" value="edit">
    <div class="step-row">
        <div class="step-indicator">
            <a class="step completed" href="#">第一步</a>
            <a class="step completed" href="#">第二步</a>
        </div>
    </div>
    <%--<h4 class="main-title">大学生创新创业训练计划项目申报</h4>--%>
    <form:form modelAttribute="" action="save" method="post" class="form-horizontal"
               enctype="multipart/form-data">
        <div class="contest-content">
            <div class="tool-bar">
                <div class="inner">
                    <span>项目编号：2017/09/17</span>
                    <span style="margin-left: 15px;">填表日期:</span>
                    <i>2017/9/17</i>
                    <span style="margin-left: 15px">申请人:</span>
                    <i>王清腾</i>
                </div>
            </div>
            <h4 class="contest-title">项目申报表</h4>

            <div class="contest-wrap">
                <div class="edit-bar edit-bar-sm clearfix">
                    <div class="edit-bar-left">
                        <span>项目申报资料</span>
                        <i class="line"></i>
                    </div>
                </div>
                <div class="row">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label">下载项目申请表：</label>
                            <div class="input-box">
                                <ul class="file-list">
                                    <li class="file-item">
                                        <div class="file-info">
                                            <img src="${ctxImg}/filetype/word.png">
                                            <a href="javascript:void(0);" data-url=""
                                               data-original="${attachment.name}" data-size=""
                                               data-title=""
                                               data-type=""
                                               data-ftp-url="">资料名</a>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="control-label"><i class="icon-require">*</i>上传申报资料：</label>
                            <div class="input-box">
                                <ul id="accessoryListPdf" class="file-list">
                                    <li class="file-item">
                                        <div class="file-info">
                                            <img src="${ctxImg}/filetype/word.png">
                                            <a href="javascript:void(0);" data-url=""
                                               data-original="${attachment.name}" data-size=""
                                               data-title=""
                                               data-type=""
                                               data-ftp-url="">资料名</a>
                                                <i class="icon icon-remove-sign"></i>
                                        </div>
                                    </li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="btngroup">
                    <button id="btnUpload" type="button" class="btn btn-primary-oe">上传申报资料</button>
                    <button type="button" class="btn btn-primary-oe">下一步</button>
                    <button type="button" class="btn btn-primary-oe">保存</button>
                    <button type="button" class="btn btn-primary-oe">提交</button>
                    <button type="button" class="btn btn-default-oe">返回</button>
                </div>
            </div>
        </div>
    </form:form>
</div>

<script>

    $(function () {
        var $btnUpload = $('#btnUpload');
        $btnUpload.uploadAccessory({
            fileNumLimit: 1,
            hasFiles: false,
            isImagePreview: false
        });

        $btnUpload.on('beforeFileQueued', function (e) {
            var $accessoryListPdf = $('#accessoryListPdf');
            var $li = $accessoryListPdf.find('li');
            $li.size() > 0 && $li.detach();
        });

        $btnUpload.on('uploadSuccess', function (e) {
            var uploader = e.uploader;
            uploader.reset();
        })
    })


</script>

</body>
</html>
