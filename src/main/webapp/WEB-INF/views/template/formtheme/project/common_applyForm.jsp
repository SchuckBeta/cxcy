<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
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
        <step-item is-complete>第一步（完善个人基本信息）</step-item>
        <step-item>第二步（填写项目基本信息）</step-item>
        <step-item>第三步（提交项目申报附件）</step-item>
    </row-step-apply>
    <div class="apply-form-container">
        <div class="apply-form-topbar">
            <span class="apply-date">申报日期：{{applyDate | formatDateFilter('YYYY-MM-DD')}}</span>
        </div>
        <div class="apply-form-titlebar-wrap">
            <div class="apply-form-titlebar">
                <span class="title">个人基本信息</span>
            </div>
        </div>
        <div class="apply-form-body">
            <div class="user-info-list">
                <el-row :gutter="15" label-width="120px">
                    <%--<el-col :span="8">--%>
                        <%--<e-col-item align="right" label="项目编号：">{{proModel.competitionNumber}}</e-col-item>--%>
                    <%--</el-col>--%>
                    <el-col :span="8">
                        <e-col-item align="right" label="姓名：">{{loginCurUser.name}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学院：">{{loginCurUser.officeName}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="专业班级：">
                            {{loginCurUser.professional | selectedFilter(officeEntries)}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="学号：">
                            {{loginCurUser.no}}
                        </e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="手机号：">{{loginCurUser.mobile}}</e-col-item>
                    </el-col>
                    <el-col :span="8">
                        <e-col-item align="right" label="电子邮箱：">{{loginCurUser.email}}
                        </e-col-item>
                    </el-col>
                </el-row>
            </div>
        </div>
        <el-form v-if="isStudent" :disabled="disabled" size="mini">
            <%--<input type='hidden' name="usertype" :value="loginCurUser.userType"/>--%>
            <%--<input type='hidden' name="id" :value="proModel.id"/>--%>
            <%--<input type='hidden' name="actYwId" :value="proModel.actYwId"/>--%>
            <%--<input type='hidden' name="year" :value="proModel.year"/>--%>
            <el-form-item v-show="!isSubmited" class="text-center">
                <el-button type="primary" size="mini" @click.stop="saveApplyForm">下一步</el-button>
            </el-form-item>
        </el-form>
    </div>
</div>

<script type="text/javascript">

    'use strict';


    new Vue({
        el: '#app',
        data: function () {
            var proModel = JSON.parse(JSON.stringify(${fns: toJson(proModel)}));
            return {
                proModel: proModel,
                officeList: JSON.parse('${fns: toJson(fns: getOfficeList())}'),
                disabled: true
            }
        },
        computed: {
            officeEntries: function () {
                return this.getEntries(this.officeList, {
                    label: 'name',
                    value: 'id'
                })
            },
            applyDate: function () {
                return this.proModel.createDate
            },
            step2Href: function () {
                var proModel = this.proModel;
                var params = {
                    actywId: proModel.actYwId,
                    id: proModel.id
                };
                return this.frontOrAdmin + '/proproject/applyStep2?' + Object.toURLSearchParams(params);
            },
            isSubmited: function () {
                return this.proModel.subStatus === '1';
            }
        },
        methods: {
            proProjectApplyAble: function () {
                var proModel = this.proModel;
                var self = this;
                if (!this.isStudent) {
                    return false;
                }
                if(proModel.id){
                    this.disabled = false;
                    return false;
                }
                this.checkProProjectIsValid('/project/projectDeclare/onProjectApply',{
                    actywId: proModel.actYwId,
                    pid: proModel.id
                }).then(function () {
                    self.disabled = false;
                }).catch(function (error) {
                    var msgBoxOptions = {
                        type: 'error',
                        title: '提示',
                        closeOnClickModal: false,
                        closeOnPressEscape: false,
                        showClose: false,
                        message: error.message
                    };
                    var btns = error.btns;
                    if (error.code === '0') {
                        msgBoxOptions['confirmButtonText'] = '确定'
                    } else if (error.code === '2') {
                        msgBoxOptions['confirmButtonText'] = btns[0].name;
                        if(btns.length === 2){
                            msgBoxOptions['cancelButtonText'] = btns[1].name;
                            msgBoxOptions['showCancelButton'] = true
                        }
                    } else {
                        msgBoxOptions['confirmButtonText'] = '刷新'
                    }
                    self.$msgbox(msgBoxOptions).then(function () {
                        if (error.code === '0') {
                            location.href = '/f'
                        } else if (error.code === '2') {
                            location.href = btns[0].url;
                        } else {
                            location.reload();
                        }
                    }).catch(function () {
                        location.href = btns[1].url
                    });
                })
            },

            saveApplyForm: function () {
                var self = this;
                var proModel = this.proModel;
                var params = {
                    userType: this.loginCurUser.userType,
                    id: proModel.id,
                    actYwId: proModel.actYwId,
                    year: proModel.year
                };
                this.disabled = true;
                if(proModel.id){
                    location.href = this.step2Href;
                    return false;
                }
                this.$axios.get('/proproject/saveStep1', {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.ret == 1) {
                        location.href = self.step2Href;
                    }else {
                        self.$alert(data.msg, '提示', {
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                }).catch(function () {
                    self.disabled = false;
                })
            }
        },
        created: function () {
            this.proProjectApplyAble();
        }
    })


</script>
</body>
</html>