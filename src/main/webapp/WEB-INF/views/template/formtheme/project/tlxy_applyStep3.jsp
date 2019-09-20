<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${fns:getConfig('frontTitle')}</title>
    <meta name="decorator" content="creative"/>

</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home"></i>首页</a></el-breadcrumb-item>
        <el-breadcrumb-item>双创项目</el-breadcrumb-item>
        <el-breadcrumb-item>${proProject.projectName}</el-breadcrumb-item>
    </el-breadcrumb>
    <row-step-apply>
        <step-item is-complete>第一步（填写项目基本信息）</step-item>
        <step-item is-complete>第二步（填写团队基本信息）</step-item>
        <step-item is-complete>第三步（提交项目申报附件）</step-item>
    </row-step-apply>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{applyDate}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">项目基本信息</span>
            </div>
        </div>
        <div class="apply-form-body">
            <%--<div class="user-info-list">--%>
                <%--<el-row :gutter="15" label-width="120px">--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="姓名：">{{loginCurUser.name}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="学号：">--%>
                            <%--{{loginCurUser.no}}--%>
                        <%--</e-col-item>--%>
                    <%--</el-col>--%>
                <%--</el-row>--%>
            <%--</div>--%>
            <el-form :model="proModelTlxyForm" ref="proModelTlxyForm" :rules="proModelTlxyRules" :disabled="disabled"
                     size="mini"
                     label-width="120px">
                <el-form-item label="下载项目申报模板：">
                    <e-file-item v-for="(item, index) in fileList" :key="item.value" :file="item"
                                 :down-url="item.downUrl"
                                 :show="false"></e-file-item>
                </el-form-item>
                <el-form-item prop="sysAttachmentList" label="附件：">
                    <e-pw-upload-file v-model="proModelTlxyForm.sysAttachmentList"
                                      action="/ftp/ueditorUpload/uploadPwTemp?folder=project" :upload-file-vars="{}"
                                      is-post
                                      tip="支持上传word、pdf、excel、txt、rar、ppt图片类型等文件<br>（上传项目申报表单、商业计划书等项目文件）"
                                      @change="handleChangeFiles"></e-pw-upload-file>
                </el-form-item>
                <el-form-item v-show="!isSubmited" label-width="0" class="text-center">
                    <el-button :disabled="isFileUploading" @click.sotp="validateProModelForm('prev')">上一步</el-button>
                    <el-button :disabled="isFileUploading" type="primary" @click.stop="validateProModelForm('save')">保存</el-button>
                    <el-button :disabled="isFileUploading" type="primary" @click.stop="validateProModelForm('submit')">提交</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</div>

<script>
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var proModelTlxy = JSON.parse(JSON.stringify(${fns: toJson(proModelTlxy)}));
            var proModel = proModelTlxy.proModel;
            var sysAttachmentList = proModel.fileInfo || [];
            sysAttachmentList = sysAttachmentList.map(function (item, index) {
                item.uid += index;
                return item;
            });
            return {
                proModelTlxy: proModelTlxy,
                proModelTlxyForm: {
                    sysAttachmentList: sysAttachmentList
                },
                proModelTlxyParams: {
                    id: proModelTlxy.id,
                    actYwId: proModel.actYwId,
                    proModel: {
                        declareId: '',
                        subTime: '',
                        proType: ''
                    },
                },
                proModelTlxyRules: {
                    sysAttachmentList: [
                        {required: true, message: '请上传附件', trigger: 'change'}
                    ]
                },
                disabled: false,
                fileKeysYs: {
                    fielSize: 'size',
                    fielTitle: 'name',
                    fielType: 'suffix',
                    fielFtpUrl: 'tempUrl'
                }
            }
        },
        computed: {
            isFileUploading: function(){
                return this.proModelTlxyForm.sysAttachmentList.length > 0 && this.proModelTlxyForm.sysAttachmentList.some(function (item) {
                            return !item.ftpUrl;
                        })
            },
            applyDate: function () {
                return moment(this.proModelTlxy.proModel.subTime).format('YYYY-MM-DD')
            },
            fileList: function () {
                return [
                    {
                        value: 'tlxy_cxxl',
                        name: '创新训练项目申请表',
                        suffix: 'doc',
                        downUrl: '/f/proproject/downTemplate?type=tlxy_cxxl'
                    },
                    {
                        value: 'tlxy_cyxl',
                        name: '创业训练项目申请表',
                        suffix: 'doc',
                        downUrl: '/f/proproject/downTemplate?type=tlxy_cyxl'
                    },
                    {
                        value: 'tlxy_cysj',
                        name: '创业实践项目申请表',
                        suffix: 'doc',
                        downUrl: '/f/proproject/downTemplate?type=tlxy_cysj'
                    }
                ]
            },
            isSubmited: function () {
                return this.proModelTlxy.proModel.subStatus === '1';
            }
        },
        methods: {
            getSysAttachmentListParams: function () {
                var sysAttachmentList = this.proModelTlxyForm.sysAttachmentList || [];
                var fileKeysYs = this.fileKeysYs;
                var paramsPaths = [];
                sysAttachmentList.forEach(function (item) {
                    var fileStep = item.fileStep;
                    var entry = {};
                    for (var k in fileKeysYs) {
                        var paramsStr = '';
//                        if (fileStep) {
                            paramsStr = 'proModel.';
//                        }
                        if (fileKeysYs.hasOwnProperty(k)) {
                            paramsStr = paramsStr + 'attachMentEntity.' + k;
                            entry[paramsStr] = item[fileKeysYs[k]];
                        }
                    }
                    paramsPaths.push(Object.toURLSearchParams(entry).toString())
                });
                var proModelTlxyParams = this.getParams();
                paramsPaths.push(Object.toURLSearchParams(proModelTlxyParams).toString());
                return paramsPaths.join('&');
            },

            getParams: function () {
                var proModelTlxyParams = this.proModelTlxyParams;
                var proModel = proModelTlxyParams.proModel;
                var proModelParams = {};
                for (var p in proModel) {
                    if (proModel.hasOwnProperty(p)) {
                        proModelParams['proModel.' + p] = proModel[p];
                    }
                }
                proModelParams.id = proModelTlxyParams.id;
                proModelParams.actYwId = proModelTlxyParams.actYwId;
                return proModelParams;
            },

            validateProModelForm: function (type) {
                var self = this;
                var proModelTlxyParams = this.proModelTlxyParams;
                var proModel = proModelTlxyParams.proModel;
                if(type === 'prev'){
                    self.saveProModel(type);
                    return;
                }
                this.$refs.proModelTlxyForm.validate(function (valid) {
                    if (valid) {
                        if (type === 'save' || type === 'prev') {
                            self.saveProModel(type)
                        } else {
                            self.confirmProModel();
                        }
                    } else {
                        if (type === 'prev') {
                            location.href = '/f/proModelTlxy/applyStep2?id=' + proModelTlxyParams.id + '&actYwId=' + proModel.actYwId;
                        }
                    }
                })
            },

            isChangeFile: function () {
                return this.proModelTlxyForm.sysAttachmentList.every(function (item) {
                    return !!item.fileStep;
                })
            },

            saveProModel: function (type) {
                var self = this;
                var paramsPathStr = this.getSysAttachmentListParams();
                var proModelTlxyParams = this.proModelTlxyParams;
                var proModel = proModelTlxyParams.proModel;
                this.disabled = true;
                if (this.isChangeFile && type === 'prev') {
                    location.href = '/f/proModelTlxy/applyStep2?id=' + proModelTlxyParams.id + '&actYwId=' + proModel.actYwId;
                    return false;
                }
                this.$axios({
                    method: 'POST',
                    url: '/proModelTlxy/ajaxSave3',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: paramsPathStr
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        if (type === 'prev') {
                            location.href = '/f/proModelTlxy/applyStep2?id=' + proModelTlxyParams.id + '&actYwId=' + proModel.actYwId;
                            return false;
                        }
                        self.$alert('保存成功，跟踪当前项目？', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: true,
                            showClose: false
                        }).then(function () {
                            location.href = '/f/project/projectDeclare/curProject'
                        }).catch(function () {
                            location.reload();
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            confirmButtonText: '确定'
                        }).then(function () {
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })

            },

            confirmProModel: function () {
                var self = this;
                this.$confirm('提交后不可修改，是否继续？', '提示', {
                    type: 'warning',
                    cancelButtonText: '取消',
                    confirmButtonText: '确定'
                }).then(function () {
                    self.submitProModel();
                }).catch(function () {

                })
            },

            submitProModel: function () {
                var self = this;
                var paramsPathStr = this.getSysAttachmentListParams();
                this.disabled = true;

                this.$axios({
                    method: 'POST',
                    url: '/proModelTlxy/submit',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: paramsPathStr
                }).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        self.proModelTlxy.proModel.subStatus = '1'
                        self.$alert('提交成功，跟踪当前项目', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            location.href = '/f/project/projectDeclare/curProject'
                        }).catch(function () {
                            location.reload();
                        })
                    } else {
                        self.$alert(data.msg, '提示', {
                            type: 'error',
                            confirmButtonText: '确定'
                        }).then(function () {
                        });
                        self.disabled = false;
                    }
                }).catch(function () {
                    self.disabled = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            },
            handleChangeFiles: function (file) {
                this.$refs.proModelTlxyForm.validateField('sysAttachmentList')
            }
        },
        created: function () {
            var subTime = this.proModelTlxy.proModel.subTime;
            subTime = moment(subTime).format('YYYY-MM-DD');
            this.proModelTlxy.proModel.subTime = subTime
            this.assignFormData(this.proModelTlxyParams, this.proModelTlxy)
        }
    })
</script>
</body>
</html>