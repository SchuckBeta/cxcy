<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="utf-8"/>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.config.js"></script>
    <!-- 编辑器源码文件 -->
    <!-- <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script> -->
    <script type="text/javascript" src="${ctxStatic}/ueditor/ueditor.all.js"></script>

</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar :second-name="notifyForm.id ? '修改' : '添加'"></edit-bar>
    </div>
    <%--<el-form :model="notifyForm" ref="notifyForm" :rules="notifyRules" :disabled="notifyFormDisabled" method="post" --%>
             <%--:action="frontOrAdmin+'/oa/oaNotify/saveAllNotice'" autocomplete="off" size="mini" label-width="120px">--%>
    <el-form :model="notifyForm" ref="notifyForm" :rules="notifyRules" :disabled="notifyFormDisabled" method="post"
              autocomplete="off" size="mini" label-width="120px">
        <input type="hidden" name="id" :value="notifyForm.id"/>
        <input type="hidden" name="sendType" value="1"/>
        <el-form-item prop="schOorPro" label="发布的门户：" v-if="currTenantId == '20'">
            <input type="hidden" name="type" :value="notifyForm.schOorPro">
            <el-radio-group v-model="notifyForm.schOorPro">
                <el-radio v-for="(item, i) in schOorPros" :label="item.value" :key="i">{{item.label}}</el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item prop="type" label="类型：" v-if="notifyForm.schOorPro != '1'">
            <input type="hidden" name="type" :value="notifyForm.type">
            <el-select v-model="notifyForm.type" class="w300">
                <el-option v-for="item in oaNotifyTypes" :key="item.value" :value="item.value"
                           :label="item.label"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="title" label="标题：">
            <el-input name="title" v-model="notifyForm.title" class="w300"></el-input>
        </el-form-item>
        <el-form-item  prop="source" label="来源：">
            <el-input name="source" v-model="notifyForm.source" class="w300"></el-input>
        </el-form-item>
        <el-form-item v-if="notifyForm.type && notifyForm.type != '3'" prop="keywords" label="关键词：">
            <el-tag
                    :key="tag"
                    v-for="tag in keywords"
                    size="medium"
                    closable
                    type="info"
                    :disable-transitions="false"
                    @close="handleTagClose(tag)">
                <input type="hidden" name="keywords" :value="tag">
                {{tag}}
            </el-tag>
            <el-input style="width:100px;"
                      class="input-new-tag"
                      v-if="inputVisible"
                      v-model="inputValue"
                      ref="saveTagInput"
                      size="mini"
                      @keyup.enter.native="handleInputConfirm"
                      @blur="handleInputConfirm"
            >
            </el-input>
            <el-button v-else class="button-new-tag" size="mini" @click="showInput">+ 关键词</el-button>
        </el-form-item>
        <el-form-item prop="content" label="内容：">
            <script id="notifyContent" name="content" type="text/plain" style="width:90%;height:400px">
                    {{notifyForm.content}}

            </script>
        </el-form-item>
        <el-form-item prop="status" label="状态：" required>
            <el-switch v-model="notifyForm.status" active-value="1" inactive-value="0" name="status"></el-switch>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop.prevent="validateNotifyForm">保存</el-button>
            <el-button @click.stop.prevent="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var notifyForm = JSON.parse(JSON.stringify(${fns: toJson(oaNotify)}));
            var oaNotifyStatus = JSON.parse('${fns: toJson(fns:getDictList('oa_notify_status'))}');
            var oaNotifyTypes = JSON.parse('${fns: toJson(fns: getDictList('oa_notify_type'))}');

            return {
                notifyForm: {
                    id: notifyForm.id,
                    sendType: '1',
                    type: notifyForm.type,
                    title: notifyForm.title,
                    source: notifyForm.source,
                    keywords: notifyForm.keywords || [],
                    content: notifyForm.content,
                    status: notifyForm.status || '0',
                    otype: '0',
                    schOorPro: notifyForm.schOorPro || ''
                },
                keywords: notifyForm.keywords || [],
                oaNotifyStatus: oaNotifyStatus,
                oaNotifyTypes: oaNotifyTypes,
                notifyRules: {
                    schOorPro: [
                        {required: true, message: '请选择发布的门户', trigger: 'change'}
                    ],
                    title: [
                        {required: true, message: '请填入标题', trigger: 'blur'},
                        {max: 125, message: '请输入不大于125位字符', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择通知类型', trigger: 'change'}
                    ],
                    content: [
                        {required: true, message: '请写入内容', trigger: 'change'}
                    ]
                },
                UEditor: null,
                inputVisible: false,
                inputValue: '',
                notifyFormDisabled: false,
                schOorPros: [
                    {label: '校级门户', value: '1'},
                    {label: '省级门户', value: '2'}
                ]
            }
        },
        watch: {
            'notifyForm.schOorPro': function (value) {
                if(value == '1') {
                    this.notifyForm.type = '9';
                }else {
                    this.notifyForm.type = '';
                }
            }
        },
        methods: {

            validateNotifyForm: function () {
                var self = this;
                this.notifyForm.content = this.UEditor.getContent();
                this.$refs.notifyForm.validate(function (valid) {
                    if(valid){
                        self.notifyForm.keywords = self.keywords;
                        self.notifyFormDisabled = true;
                        self.$axios.post('/oa/oaNotify/saveNotify', self.notifyForm).then(function (response) {
                            var data = response.data;
                            if(data.status == 1){
                                self.$msgbox({
                                    type: 'success',
                                    title: '提示',
                                    message: '保存成功',
                                    confirmButtonText: '确定'
                                }).then(function () {
                                    location.href = self.frontOrAdmin + '/oa/oaNotify/broadcastList'
                                }).catch(function () {

                                })
                            }else {
                                self.$message({
                                    type: 'error',
                                    message: '保存失败，请重试'
                                })
                            }
                            self.notifyFormDisabled = false;
                        }).catch(function (error) {
                            self.notifyFormDisabled = false;
                        })
                    }
                })

            },

            handleTagClose: function (tag) {
                this.keywords.splice(this.keywords.indexOf(tag), 1);
            },
            handleInputConfirm: function () {
                var inputValue = this.inputValue;
                if (inputValue) {
                    this.keywords.push(inputValue);
                }
                this.inputVisible = false;
                this.inputValue = '';
            },
            showInput: function () {
                this.inputVisible = true;
                this.$nextTick(function () {
                    this.$refs.saveTagInput.$refs.input.focus();
                });
            },
            configUeditor: function () {
                var frontOrAdmin = this.frontOrAdmin;
                UE.Editor.prototype._bkGetActionUrl = UE.Editor.prototype.getActionUrl;
                UE.Editor.prototype.getActionUrl = function (action) {
                    if (action == 'uploadimage' || action == 'uploadscrawl' || action == 'uploadvideo'
                            || action == 'uploadfile' || action == 'catchimage' || action == 'listimage' || action == 'listfile') {
                        return frontOrAdmin + '/ftp/ueditorUpload/uploadTempFormal?folder=cmsArticle';
                    } else {
                        return this._bkGetActionUrl.call(this, action);
                    }
                }
            },
            goToBack: function () {
                return window.history.go(-1);
            }
        },
        mounted: function () {
            this.UEditor = UE.getEditor('notifyContent');
            this.configUeditor();
        }
    })

</script>
</body>
</html>