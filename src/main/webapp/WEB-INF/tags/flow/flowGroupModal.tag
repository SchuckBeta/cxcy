<%@ tag language="java" pageEncoding="UTF-8" %>


<script type="text/x-template" id="flowGroupModalTemplate">
    <div class="modal-module" :data-show="controlModal">
        <my-modal :title="title" :show.sync="modalShow" @ok="saveFlowGroup" @cancel="cancel"
                  :save-disabled="saveDisabled" ok-text="确定" cancel-text="取消" :large="true">
            <div class="modal-body" slot="body">
                <form class="form-horizontal">
                    <div class="control-group">
                        <label class="control-label">流程组名：</label>
                        <div class="controls">
                            <input type="hidden" v-model="groupData.groupId"/>
                            <span class="control-static">{{groupName}}</span>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">流程跟节点：</label>
                        <div class="controls">
                            <input type="hidden" v-model="groupData.parentId"/>
                            <span class="control-static">{{parentName}}</span>
                        </div>
                    </div>
                    <div v-show="hidePreFunIds" class="control-group">
                        <label class="control-label">流程前置业务节点：</label>
                        <div class="controls">
                            <select v-model="groupData.preFunId" class="form-control input-large">
                                <option value="0">-请选择-</option>
                                <option v-for="(item, index) in prevNodes" :value="item.id"
                                        v-option-bind="{label: item.name}"></option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label"><span class="red"
                                                           style="margin-right: 4px;">*</span>流程业务节点：</label>
                        <div class="controls">
                            <input type="text" class="form-control input-large" name="name" v-model="groupData.name"
                                   @change="hasNodeGroupName = !groupData.name"/>
                            <div v-show="hasNodeGroupName" class="red">
                                请填写节点名称
                            </div>
                        </div>
                    </div>
                    <div v-show="hideNextFunIds" class="control-group">
                        <label class="control-label">流程后置业务节点：</label>
                        <div class="controls">
                            <select v-model="groupData.nextFunId" class="form-control input-large">
                                <option :value="0">-请选择-</option>
                                <option v-for="(item,idx) in nextNodes" :value="item.id" :key="item.id"
                                        v-option-bind="{label: item.name}"></option>
                            </select>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label"><span class="red" style="margin-right: 4px;">*</span>图标</label>
                        <div class="controls">
                            <div class="img-content" style="max-width: 100px; max-height: 100px;line-height: 1">
                                <img id="iconPic" ref="iconPic" :src="groupData.iconImgUrl"
                                     style="display: block;max-width: 100%"/>
                                <%--<input type="text" style="display: none" name="file" v-model="groupData.iconUrl" />--%>
                            </div>
                            <input type="button" @click="choosePic = true" class="btn" style="" value="更新图标"/>
                            <div v-show="controlIconUrl" class="red">请上传图标</div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">显示：</label>
                        <div class="controls">
                            <label><input type="radio" v-model="groupData.isShow" :value="1"
                                          class="form-control"/>是</label>
                            <label><input type="radio" v-model="groupData.isShow" :value="0"
                                          class="form-control"/>否</label>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label">备注：</label>
                        <div class="controls">
                            <textarea rows="3" v-model="groupData.remarks" class="input-xlarge"></textarea>
                        </div>
                    </div>
                </form>
            </div>
        </my-modal>
        <my-modal title="选择图片" :show.sync="choosePic" @ok="saveFlowGroup" @cancel="choosePic = false" :has-save="false"
                  ok-text="确定" cancel-text="取消" :large="true">
            <div class="modal-body" slot="body">
                <ul class="nav nav-tabs" id="myTab">
                    <li :class="{active: defaultImgTab}"><a href="javascript: void (0)" @click="defaultImgTab = true">默认图片</a>
                    </li>
                    <li :class="{active: !defaultImgTab}"><a href="javascript: void (0)"
                                                             @click="defaultImgTab = !defaultImgTab">本地图片</a></li>
                </ul>
                <div class="tab-content">
                    <div class="tab-pane" :class="{active: defaultImgTab}">
                        <ul class="default-pic-list clearfix">
                            <li class="pic-item" v-for="img in iconList">
                                <a @click="chooseDefaultImg(img.url, $event)" href="javascript: void (0);">
                                    <img :src="img.ftpUrl">
                                </a>
                            </li>
                        </ul>
                    </div>
                    <div class="tab-pane" :class="{active: !defaultImgTab}">
                        <div class="upload-content"  v-upload-file="{src: groupData.iconImgUrl, cropOption: cropOption, url: severUrl,fileVal: fileVal}">上传本地图标</div>
                    </div>
                </div>
            </div>
        </my-modal>
    </div>
</script>


<script>
    var flowGroupModal = Vue.component('flow-group-modal', {
        template: '#flowGroupModalTemplate',
        components: {},
        props: {
            iconList: {
                type: Array,
                default: function () {
                    return []
                }
            },
            title: {
                type: String,
                default: ''
            },
            controlModal: {
                type: Boolean,
                default: false
            },
            controlIconUrl: {
                type: Boolean,
                default: false
            },
            nextNodes: {
                type: Array,
                default: function () {
                    return []
                }
            },
            prevNodes: {
                type: Array,
                default: function () {
                    return [];
                }
            },
            groupName: {
                type: String,
                default: ''
            },
            parentName: {
                type: String,
                default: ''
            },
            saveDisabled: {
                type: Boolean,
                default: false
            },
            groupData: {
                type: Object,
                twoWay: true,
                default: function () {
                    return {
                        id: '',
                        preFunId: '',
                        preFunGnode: {
                            node: {
                                id: ''
                            }
                        },
                        name: '',
                        nextFunId: '',
                        nextFunGnode: {
                            id: '',
                            node: {
                                id: ''
                            }
                        },
                        isShow: 1,
                        role: '',
                        remarks: '',
                        hasGateway: false,
                        parent: {
                            id: ''
                        },
                        nodeId: '',
                        hasGroup: false,
                        posLux: '',
                        posLuy: '',
                        width: '',
                        height: '',
                        iconUrl: '',
                        iconImgUrl: ''
                    }
                }
            }
        },
        data: function () {
            return {
                hidePreFunIds: false,
                hideNextFunIds: false,
                hasNodeGroupName: false,
                choosePic: false,
                defaultImgTab: true,
                severUrl: $frontOrAdmin+'/ftp/ueditorUpload/cutImg?folder=ueditor&x={{x}}&y={{y}}&width={{width}}&height={{height}}',
                fileVal: 'upfile',
                cropOption: {
                    aspectRatio: 1,
                    viewMode: 1,
                    minCropBoxWidth: 100,
                    minCropBoxHeight: 100,
                    zoomOnWheel: false
                }
            }
        },
        directives: {
            optionBind: {
                bind: function (el, binding) {
                    el.innerHTML = binding.value.label
                }
            },
            uploadFile: {
                twoWay: true,
                bind: function (el, binding, vnode) {
                    var self = this;
                    var cropOption = binding.value.cropOption;
                    var src = binding.value.src;
                    var url = binding.value.url;
                    var fileVal = binding.value.fileVal;
                    var html = '';
                    var $el = $(el);
                    var $btnUpload;
                    var $img;
                    var $fileInput;
                    var uploadFile;
                    var $btnUploaderHandler;
                    var braceReg = /\{\{(.)+?\}\}/g;
                    var mValueResults = url.match(braceReg);
                    var rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;
                    var rUrl;
                    var copyUrl;
                    html += '<button type="button" class="btn btn-upload-handler">上传本地图标</button><button type="button" disabled class="btn btn-upload">保存</button><div class="upload-img-box"><input type="file" style="display: none;"><div class="img-box"><img crossOrigin="Anonymous" style="display: none;" src=""></div></div>';
                    el.innerHTML = html;
                    $img = $el.find('img');
                    $btnUpload = $el.find('.btn-upload');
                    $fileInput = $el.find('input[type="file"]');
                    $btnUploaderHandler = $el.find('.btn-upload-handler');

                    if (src) {
                        $img.attr('src', src).show().cropper(cropOption);
                    }

                    $fileInput.on('change', function (e) {
                        var oFReader = new FileReader();
                        var aFiles = e.target.files;
                        if(!aFiles){
                            return false;
                        }
                        if (!rFilter.test(aFiles[0].type)) {
                            alert('上传图片格式不正确');
                            return false;
                        }
                        if (window.FileReader) {
                            oFReader.onload = function (oFREvent) {
                                var oPreviewImg = new Image();
                                oPreviewImg.src = oFREvent.target.result;
                                if($img.attr('src')){
                                    $img.cropper('replace', oFREvent.target.result);
                                }else {
                                    $img.attr('src', oFREvent.target.result);
                                    $img.cropper(cropOption);
                                }
                            };
                            oFReader.readAsDataURL(aFiles[0]);
                        }
                        if (navigator.appName === "Microsoft Internet Explorer") {
                            $img.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = e.target.value;
                        }
                        uploadFile = aFiles[0];
                        $btnUpload.prop('disabled', false)
                    });
//
                    $btnUploaderHandler.on('click', function () {
                        $fileInput.trigger('click')
                    });


                    $btnUpload.on('click', function () {
                        var imgData,oReq;
                        var oMyForm;
                        imgData = $img.cropper('getData');
                        oReq = new XMLHttpRequest();
                        oMyForm = new FormData();
                        copyUrl = url;
                        for (var i = 0; i < mValueResults.length; i++) {
                            var key = mValueResults[i].replace('{{', '');
                            key = key.replace('}}', '');
                            rUrl = copyUrl.replace(mValueResults[i], Math.floor(imgData[key]));
                            copyUrl = rUrl;
                        }
                        $btnUpload.prop('disabled', true).text('保存中...');
                        oMyForm.append(fileVal, uploadFile);
                        oReq.open('post', rUrl);
                        oReq.onload = function (oEvent) {
                            if (oReq.status == 200) {
                                vnode.context.groupData.iconImgUrl = JSON.parse(oReq.responseText).url;
                                vnode.context.groupData.iconUrl = JSON.parse(oReq.responseText).ftpUrl;
                                vnode.context.choosePic = false;
                            } else {
                                alertx('上传失败')
                            }
                            $btnUpload.prop('disabled', false).text('保存');
                        };
                        oReq.send(oMyForm);
                    })
                },
                update: function (el, binding) {}
            }
        },
        computed: {
            modalShow: function () {
                return this.controlModal;
            },
            httpFtp: function () {
                var ftpPath = $('#ftpPath').val();
                return ftpPath.substring(0, ftpPath.indexOf('/oseasy/'));
            }
        },
        watch: {
            controlModal: function (value) {
                this.modalShow = value;
            },
            choosePic: function (value) {
                if (value) {
                    this.defaultImgTab = value
                }
            }
        },
        methods: {
            saveFlowGroup: function () {
                if (!this.groupData.name) {
                    this.hasNodeGroupName = true;
                    return false;
                }
                this.$emit('save-flow-group');
            },

            cancel: function () {
                this.$emit('cancel');
            },
            chooseDefaultImg: function (src, event) {
                var $iconPic = $(this.$refs.iconPic);
                var $iconPicNext = $iconPic.next();
                var input = '<input name="file" type="hidden" value="/tool' + src + '">';
                this.groupData.iconImgUrl = $(event.target).attr('src');
                this.groupData.iconUrl = src;
//                console.log(this.groupData.iconImgUrl,'---',$(event.target).attr('src'));
                if ($iconPicNext.size() > 0) {
                    $iconPicNext.val(src);
                } else {
                    $(input).insertAfter($iconPic);
                }
                this.choosePic = false;
                return false;
            }
        },
        beforeMount: function () {

        },
        mounted: function () {

        }
    });
</script>