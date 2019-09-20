<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户管理</title>
    <%@include file="/WEB-INF/views/include/backCommon.jsp" %>
    <script src="https://cdn.bootcss.com/vue/2.4.4/vue.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/actYwDesign/vue.min.js"></script>
    <link rel="stylesheet" type="text/css" href="${ctxStatic}/cropper/cropper.min.css">
    <script type="text/javascript" src="${ctxStatic}/cropper/cropper.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/uploadCutImage.js"></script>
    <style>
        .container-fluid {
            padding-right: 0;
        }

        .cert-elements {
            float: right;
            width: 335px;
            height: 100vh;
            overflow: hidden;
        }

        .cert-content {
            border-right: 1px solid #eee;
            margin-right: 350px;
            height: 100vh;
        }

        .cert-elements .cert-title {
            height: 34px;
        }

        .cert-elements .cert-title .btn {
            width: auto;
            height: auto;
        }

        .cert-elements .cert-ele-content {
            height: calc(100% - 60px);
        }

        .cert-elements .cert-ele-list {
            height: 100%;
            padding-right: 15px;
            padding-top: 30px;
            padding-bottom: 30px;
            overflow: auto;
            box-sizing: border-box;
        }

        .cert-elements .ele-thumbnail {
            position: relative;
            width: 100px;
            height: 72px;
            cursor: pointer;
            overflow: hidden;
        }

        .cert-elements .cert-ele-item {
            position: relative;
            font-size: 0;
            margin-bottom: 15px;
            min-height: 72px;
            padding: 4px;
            border: 1px solid #ddd;
        }

        .cert-elements .cert-ele-item:last-child {
            margin-bottom: 40px;
        }

        .cert-elements .cert-ele-item.cert-ele-item-select {
            border-color: #e9442d;
        }

        .cert-elements .cert-ele-item.cert-ele-item-select .btn-del-ele {
            display: block;
        }

        .cert-elements .operations {
            display: inline-block;
            width: 183px;
            margin-left: 10px;
            padding-top: 10px;
            vertical-align: top;
        }

        .cert-elements .ele-pic-box {
            display: inline-block;
            position: relative;
            vertical-align: top;
        }

        .cert-elements .operation {
            height: 20px;
            margin-bottom: 10px;
        }

        .cert-elements .operation:before, .cert-elements .operation:after {
            content: '';
            display: table;
        }

        .cert-elements .operation:after {
            clear: both;
        }

        .cert-elements .operations .operation-inner {
            margin-left: 60px;
            padding: 0 20px 0 10px;
        }

        .cert-elements .operations .operation-label {
            float: left;
            width: 60px;
            font-size: 12px;
            text-align: right;
        }

        .cert-content .edit-pic-title {
            height: 34px;
            line-height: 34px;
            text-align: center;
            margin: 0;
        }

        .cert-content .cert-edit-area {
            padding: 15px 15px 15px 0;
            height: calc(100% - 100px);
            box-sizing: border-box;
        }

        .cert-elements .operation-inner .slider {
            margin-top: 7px;
            height: 6px;
            border-radius: 5px;
        }

        .cert-elements .operation-inner .slider-handler {
            width: 14px;
            height: 14px;
            margin-top: -4px;
            border-radius: 50%;
        }

        .cert-elements .slider .slider-val {
            color: #000;
        }

        .cert-elements .btn-del-ele {
            display: none;
            position: absolute;
            right: 0;
            top: 0;
            padding: 0 8px;
            font-size: 14px;
            line-height: 20px;
            font-weight: normal;
            font-style: normal;
            color: #fff;
            background-color: #e9442d;
            border: dashed #fff;
            text-decoration: none;
            border-width: 0 0 1px 1px;
        }

        .cert-elements .choose {
            position: absolute;
            bottom: 0;
            right: 0;
            width: 18px;
            height: 18px;
            background: rgba(255, 255, 255, .5) url('/images/choosed-gary.png') no-repeat center;
            background-size: cover;
            cursor: pointer;
        }

        .cert-elements .choose.shown {
            background-image: url('/images/choosed.png');
        }

        .cert-elements .ele-thumbnail:hover .ele-upload-box {
            display: block;
        }

        .cert-elements .ele-upload-box {
            display: none;
            position: absolute;
            left: 0;
            top: 0;
            width: 100%;
            height: 100%;
            line-height: 72px;
            background-color: rgba(0, 0, 0, .5);
            text-align: center;
            z-index: 100;
        }

        .cert-elements .btn-upload-pic {
            width: auto;
            height: auto;
            font-size: 14px;
            color: #fff;
        }

        .cert-elements .ele-text {
            height: 100%;
            width: 100%;
            box-sizing: border-box;
        }

        .cert-elements .cert-ele-item-font .operation-font {
            height: 30px;
        }

        .cert-elements .cert-ele-item-font .operation-font .operation-label {
            line-height: 28px;
        }

        .cert-ele-item .cert-ele-item-font select {
            display: block;
            width: 100%;
            height: auto;
            margin-bottom: 0;
        }

        .cert-elements .cert-ele-item-font .ele-thumbnail {
            height: 90px;
        }

        .cert-elements .cert-ele-item-font .btn-submit-text {
            display: block;
            width: 100%;
            height: 30px;
            margin-top: 8px;
        }

        .btn-upload-image {
            width: auto;
            height: auto;
        }
    </style>
    <style type="text/css">

        .slider {
            position: relative;
            display: block;
            padding: 0 5px;
            height: 10px;
            background-color: #ddd;
            box-sizing: border-box;
        }

        .slider.disabled {
            opacity: .6;
            cursor: not-allowed;
        }

        .slider .slider-handler {
            position: absolute;
            left: 0;
            top: 0;
            width: 10px;
            height: 16px;
            margin-left: -5px;
            margin-top: -3px;
            background-color: #f4e6d4;
        }

        .slider .slider-handler-hover .slider-tooltip {
            display: block;
        }

        .slider .slider-tooltip {
            position: absolute;
            z-index: 1070;
            display: none;
            width: 38px;
            height: 24px;
            font-size: 12px;
            bottom: 20px;
            margin-left: -14px;
            font-style: normal;
            font-weight: 400;
            line-height: 1.42857143;
            text-decoration: none;
        }

        .slider .slider-val {
            position: absolute;
            width: 22px;
            padding: 3px 8px 4px;
            color: #fff;
            text-align: center;
            background-color: #f4e6d4;
            border-radius: 4px;
        }

        .slider .arrow {
            position: absolute;
            width: 0;
            height: 0;
            border-color: transparent;
            border-style: solid;
            top: 100%;
            left: 50%;
            margin-left: -5px;
            border-width: 5px 5px 0;
            border-top-color: #f4e6d4;
        }
    </style>
    <style type="text/css">
        .certificate-container {
            position: relative;
        }

        .certificate-bg {
            background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABAAAAAQAQMAAAAlPW0iAAAAA3NCSVQICAjb4U/gAAAABlBMVEXMzMz////TjRV2AAAACXBIWXMAAArrAAAK6wGCiw1aAAAAHHRFWHRTb2Z0d2FyZQBBZG9iZSBGaXJld29ya3MgQ1M26LyyjAAAABFJREFUCJlj+M/AgBVhF/0PAH6/D/HkDxOGAAAAAElFTkSuQmCC') repeat;
        }

        .certificate-modal {
            position: absolute;
            left: 0;
            right: 0;
            top: 0;
            bottom: 0;
            cursor: crosshair;
            background: rgba(0, 0, 0, .5);
        }

        .certificate-crop-box {
            position: absolute;
            left: 0;
            top: 0;
        }

        .certificate-view-box {
            display: block;
            height: 100%;
            overflow: hidden;
            width: 100%;
        }

        .certificate-view-box img {
            display: block;
        }

        .certificate-face {
            display: block;
            position: absolute;
            left: 0;
            top: 0;
            right: 0;
            bottom: 0;
            background-color: rgba(255, 255, 255, .1);
            cursor: move;
        }

        .certificate-line, .certificate-point {
            display: block;
            height: 100%;
            opacity: .1;
            position: absolute;
            width: 100%;
        }

        .certificate-line {
            background-color: #39f;
        }

        .certificate-crop-box .certificate-line {
            display: none;
        }

        .certificate-line.line-e {
            cursor: e-resize;
            right: -3px;
            top: 0;
            width: 5px;
        }

        .certificate-line.line-n {
            cursor: n-resize;
            height: 5px;
            left: 0;
            top: -3px;
        }

        .certificate-line.line-w {
            cursor: w-resize;
            left: -3px;
            top: 0;
            width: 5px;
        }

        .certificate-line.line-s {
            bottom: -3px;
            cursor: s-resize;
            height: 5px;
            left: 0;
        }

        .certificate-point {
            background-color: #39f;
            height: 5px;
            opacity: .75;
            width: 5px;
        }

        .certificate-point.point-e {
            cursor: e-resize;
            margin-top: -3px;
            right: -3px;
            top: 50%;
        }

        .certificate-point.point-n {
            cursor: n-resize;
            left: 50%;
            margin-left: -3px;
            top: -3px;
        }

        .certificate-point.point-w {
            cursor: w-resize;
            left: -3px;
            margin-top: -3px;
            top: 50%;
        }

        .certificate-point.point-s {
            bottom: -3px;
            cursor: s-resize;
            left: 50%;
            margin-left: -3px;
        }

        .certificate-point.point-ne {
            cursor: ne-resize;
            right: -3px;
            top: -3px;
        }

        .certificate-point.point-nw {
            cursor: nw-resize;
            left: -3px;
            top: -3px;
        }

        .certificate-point.point-sw {
            bottom: -3px;
            cursor: sw-resize;
            left: -3px;
        }

        .certificate-point.point-se {
            height: 5px;
            opacity: .75;
            width: 5px;
            bottom: -3px;
            cursor: se-resize;
            right: -3px;
        }

        .certificate-point.point-se:before {
            background-color: #39f;
            bottom: -50%;
            content: ' ';
            display: block;
            height: 200%;
            opacity: 0;
            position: absolute;
            right: -50%;
            width: 200%;
        }

        .line-e.ui-draggable-dragging {
            visibility: hidden;
        }

        .cert-content .cert-pic {
            display: block;
            margin: 0 auto;
        }

        .certificate-line .dragged-handler {
            display: block;
            width: 100%;
            height: 100%;
        }

        .cert-content .cert-pic-box {
            position: relative;
            margin: 0 auto;
            z-index: 1000;
        }

        .cert-content .certificate-crop-box.certificate-crop-box-selected {
            z-index: 1100;
        }

        .cert-content .certificate-crop-box.certificate-crop-box-selected .certificate-line {
            display: block;
        }

        .cert-content .certificate-crop-box.certificate-crop-box-selected .certificate-view-box {
            outline-color: rgba(51, 153, 255, 0.75);
            outline: 1px solid #39f;
        }

        .btn-modal-upload {
            width: auto;
            height: auto;
        }


    </style>
</head>
<body>
<div id="cert" class="container-fluid">
    <div class="cert-elements">
        <div class="cert-title">
            <button type="button" class="btn">添加元素</button>
        </div>
        <div class="cert-ele-content">
            <div class="cert-ele-list">
                <div class="cert-ele-item">
                    <div class="ele-pic-box">
                        <div class="ele-thumbnail">
                            <img class="img-responsive" :src="src.resource">
                            <div class="ele-upload-box"
                                 v-upload-file="{data: {url: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=sysCertificate/10000&x={{x}}&y={{y}}&width={{width}}&height={{height}}',fileVal: 'upfile', resource: src.resource}, name: 'src',cropOption:cropOption}">
                            </div>
                        </div>
                    </div>
                    <a class="choose" href="javascript: void (0);" :class="{shown: src.isShow}"></a>
                </div>
                <div class="cert-ele-item"
                     :class="{'cert-ele-item-select': certEleType === 'water'}"
                     @click="selectCertEle(water, 'water')">
                    <div class="ele-pic-box">
                        <div class="ele-thumbnail">
                            <img class="img-responsive" :src="water.resource">
                            <div class="ele-upload-box"
                                 v-upload-file="{data: {url: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=sysCertificate/10000&x={{x}}&y={{y}}&width={{width}}&height={{height}}',fileVal: 'upfile', resource: water.resource}, name: 'water',cropOption:cropOption}">
                            </div>
                        </div>
                    </div>
                    <div class="operations">
                        <div class="operation">
                            <div class="operation-label">透明度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :step="10" :value="water.opacity"
                                        :disabled="!water.isShow"></slider>
                            </div>
                        </div>
                        <div class="operation">
                            <div class="operation-label">角度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :step="1" :max="360"
                                        :value="water.rate"></slider>
                            </div>
                        </div>
                    </div>
                    <%--<a class="btn-del-ele" href="javascript:void (0)">&times;</a>--%>
                    <a class="choose" :class="{shown: water.isShow}"></a>
                </div>
                <div class="cert-ele-item" :class="{'cert-ele-item-select': certEleType === 'backIcon'}"
                     @click="selectCertEle(backIcon, 'backIcon')">
                    <div class="ele-pic-box">
                        <div class="ele-thumbnail">
                            <img class="img-responsive" :src="backIcon.resource">
                            <div class="ele-upload-box"
                                 v-upload-file="{data: {url: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=sysCertificate/10000&x={{x}}&y={{y}}&width={{width}}&height={{height}}',fileVal: 'upfile', resource: backIcon.resource}, name: 'backIcon',cropOption:cropOption}">
                            </div>
                        </div>
                    </div>
                    <div class="operations">
                        <div class="operation">
                            <div class="operation-label">透明度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete"
                                        :step="10"
                                        :value="backIcon.opacity"
                                        :disabled="!backIcon.isShow"></slider>
                            </div>
                        </div>
                        <div class="operation">
                            <div class="operation-label">角度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :step="1" :max="360"
                                        :value="backIcon.rate"
                                        :disabled="!backIcon.isShow"></slider>
                            </div>
                        </div>
                    </div>
                    <%--<a class="btn-del-ele" href="javascript:void (0)">&times;</a>--%>
                    <a class="choose" :title="backIcon.isShow ? '隐藏背景图片': '显示背景图片'" :class="{shown: backIcon.isShow}"
                       @click="chooseShow(backIcon, ['backIcon'], $event)"></a>
                </div>
                <div class="cert-ele-item cert-ele-item-font"
                     :class="{'cert-ele-item-select': certEleType === 'backText'}"
                     @click="selectCertEle(backText, 'backText')">
                    <div class="ele-pic-box">
                        <div class="ele-thumbnail">
                            <textarea class="ele-text" :disabled="!backText.isShow"
                                      v-model="backText.resource">背景文本</textarea>
                        </div>
                    </div>
                    <div class="operations">
                        <div class="operation">
                            <div class="operation-label">透明度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :disabled="!backText.isShow" :step="10"
                                        :value="backText.opacity" v-model="backText.opacity"></slider>
                            </div>
                        </div>
                        <div class="operation">
                            <div class="operation-label">角度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete"
                                        :disabled="!backText.isShow"
                                        :step="1" :max="360"
                                        v-model="backText.rate"
                                        :value="backText.rate"></slider>
                            </div>
                        </div>
                        <div class="operation operation-font">
                            <div class="operation-label">字体：</div>
                            <div class="operation-inner">
                                <select class="input-small" :disabled="!backText.isShow">
                                    <option>微软雅黑</option>
                                </select>
                            </div>
                        </div>
                        <div class="operation">
                            <div class="operation-label">字体大小：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :disabled="!backText.isShow"
                                        v-model="backText.fontSize" :min="12" :step="2" :max="48"
                                        :value="backText.fontSize"></slider>
                            </div>
                        </div>
                    </div>
                    <%--<a class="btn-del-ele" href="javascript:void (0)">&times;</a>--%>
                    <a class="choose" :title="backText.isShow ? '隐藏背景文本': '显示背景文本'" :class="{shown: backText.isShow}"
                       @click="chooseShow(backText, ['backText'], $event)"></a>
                </div>
                <div class="cert-ele-item cert-ele-item-font"
                     :class="{'cert-ele-item-select': certEleType === 'certText' && certTextCur == index}"
                     v-for="(certText, index) in param"
                     @click="selectCertEle(certText, 'certText', index)">
                    <div class="ele-pic-box">
                        <div class="ele-thumbnail">
                            <textarea class="ele-text" :disabled="!certText.isShow"
                                      v-model="certText.resource"></textarea>
                        </div>
                    </div>
                    <div class="operations">
                        <div class="operation">
                            <div class="operation-label">透明度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :step="10" :disabled="!certText.isShow"
                                        v-model="certText.opacity"
                                        :value="certText.opacity"></slider>
                            </div>
                        </div>
                        <div class="operation">
                            <div class="operation-label">角度：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :step="1" :max="360"
                                        :disabled="!certText.isShow" :value="certText.rate"
                                        v-model="certText.rate"></slider>
                            </div>
                        </div>
                        <div class="operation operation-font">
                            <div class="operation-label">字体：</div>
                            <div class="operation-inner">
                                <select class="input-small" :disabled="!certText.isShow">
                                    <option>微软雅黑</option>
                                </select>
                            </div>
                        </div>
                        <div class="operation">
                            <div class="operation-label">字体大小：</div>
                            <div class="operation-inner">
                                <slider @slide-complete="slideComplete" :min="12" :step="2" :max="48"
                                        :value="certText.fontSize"
                                        v-model="certText.fontSize"
                                        :disabled="!certText.isShow"></slider>
                            </div>
                        </div>
                    </div>
                    <a class="btn-del-ele" href="javascript:void (0)">&times;</a>
                    <a class="choose" :title="certText.isShow ? '隐藏文字': '显示文字'" :class="{shown: certText.isShow}"
                       @click="chooseShow(certText, ['certText', index], $event)"></a>
                </div>
            </div>
        </div>
    </div>
    <div class="cert-content">
        <h4 class="edit-pic-title" @click="changePic">证书名称</h4>
        <div class="cert-edit-area">
            <div class="certificate-container certificate-bg">
                <div class="cert-pic-box">
                    <img class="cert-pic" v-auto-pic="{ratio: '', parentEleCls: '.cert-edit-area'}"
                         :src="src.resource">

                    <cert-ele box-type="pic" :data="water"
                              :is-selected="certEleType === 'water'"
                              cert-ele-type="water"
                              @choose-crop-box="chooseCropBox"
                              :ratio="ratio"></cert-ele>

                    <cert-ele box-type="pic" :data="backIcon"
                              :is-selected="certEleType === 'backIcon'"
                              cert-ele-type="backIcon"
                              @choose-crop-box="chooseCropBox"
                              :ratio="ratio"></cert-ele>

                    <cert-ele box-type="font" :data="backText"
                              :is-selected="certEleType === 'backText'"
                              cert-ele-type="backText"
                              @choose-crop-box="chooseCropBox"
                              :ratio="ratio"></cert-ele>

                    <cert-ele v-for="(certText, index) in param" box-type="font" :data="certText"
                              :is-selected="certEleType == 'certText' && certTextCur == index"
                              cert-ele-type="certText"
                              :cert-text-cur="index"
                              :ratio="ratio"
                              @choose-crop-box="chooseCropBox"></cert-ele>
                </div>
                <div class="certificate-modal"></div>

            </div>
        </div>
    </div>
    <div class="cert-btns">
        <button type="button" class="btn">预览</button>
        <button type="button" class="btn">保存</button>
    </div>
</div>
<script src="${ctxJs}/cert/directives/v-dragged.js"></script>
<script src="${ctxJs}/cert/directives/v-autoPic.js"></script>
<script src="${ctxJs}/cert/directives/v-ratio.js"></script>
<script src="${ctxJs}/cert/directives/v-generateText.js"></script>
<script src="${ctxJs}/cert/directives/v-upload-file.js"></script>
<script src="${ctxJs}/cert/components/dragLine.js"></script>
<script src="${ctxJs}/cert/components/slider.js"></script>
<script src="${ctxJs}/cert/components/certEle.js"></script>

<script>
    var Cert = new Vue({
        el: '#cert',
        data: function () {
            return {
                ratio: '',
                certEleType: '',
                certTextCur: '',
                src: {
                    width: 100,
                    height: 100,
                    opacity: 100,
                    rate: 0,
                    xlt: 0,
                    ylt: 0,
                    hasLoop: false,
                    isShow: true,
                    resource: '/images/cert-default.png'
                },

                cropOption: {
                    viewMode: 1,
                    minCropBoxWidth: 100,
                    minCropBoxHeight: 100,
                    zoomOnWheel: false,
                    zoomable: false
                },
                water: {
                    width: 100,
                    height: 100,
                    opacity: 100,
                    rate: 0,
                    xlt: 0,
                    ylt: 0,
                    hasLoop: false,
                    isShow: true,
                    resource: '/images/cert-default.png'
                },
                backIcon: {
                    width: 100,
                    height: 100,
                    opacity: 100,
                    rate: 1,
                    xlt: 0,
                    ylt: 0,
                    hasLoop: false,
                    isShow: false,
                    resource: '/images/cert-default.png'
                },
                backText: {
                    width: 100,
                    height: 100,
                    opacity: 100,
                    rate: 0,
                    xlt: 0,
                    ylt: 0,
                    fontSize: 36,
                    font: 'Microsoft YaHei',
                    color: '#333',
                    hasLoop: false,
                    isShow: false,
                    resource: '王清腾'
                },
                param: [{
                    width: 100,
                    height: 100,
                    opacity: 100,
                    rate: 0,
                    xlt: 0,
                    ylt: 0,
                    fontSize: 36,
                    font: 'Microsoft YaHei',
                    color: '#333',
                    isShow: false,
                    resource: '123456'
                }],

                mainSrc: '/images/cert-default.png'
            }
        },
        computed: {},
        methods: {
            slideComplete: function () {
            },
            dragLineComplete: function (params) {
                console.log(params)
            },
            changePic: function () {
                this.src = '/img/video-default.jpg'
            },

            chooseCropBox: function (params) {
                this.certEleType = params[0];
                this.certTextCur = params[1]
            },

            /*拿到数据生成元素数组*/
            generateCertPics: function () {

            },


            /*
             选中可操作的证书元素
             参数当前对象
             */
            selectCertEle: function (certEle, certELeType, index) {
                this.certEleType = certELeType;
                this.certTextCur = index > -1 ? index : '';
                console.log(this.certEleType == 'certText' && this.certTextCur == index)
            },

            chooseShow: function (certEle, params, event) {
                event.stopPropagation();
                this.certEleType = params[0];
                this.certTextCur = params[1] || '';
                certEle.isShow = !certEle.isShow;
            },

            //生成文字
            changeCertText: function (certEle) {
                var text = certEle.resource;
            },


            //删除证书元素
            delCertEle: function () {

            }


        },
        beforeMount: function () {

        },
        mounted: function () {

        }
    })
</script>
</body>
</html>