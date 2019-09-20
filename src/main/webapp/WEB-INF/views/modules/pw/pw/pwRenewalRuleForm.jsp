<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>

<style>
    .el-form-item__error{
        left:128px;
    }
</style>

<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar></edit-bar>
    </div>
    <el-form :model="form" :rules="formRules" action="${ctx}/pw/pwRenewalRule/save" :disabled="isSaving"
             method="POST" size="mini" ref="form">
        <input id="id" name="id" type="hidden" :value="form.id">


        <control-rule-block title="续期预警设置" class="control-rule_appointment-rule">
            <input type="hidden" id="isWarm" name="isWarm" :value="form.isWarm">
            <el-form-item prop="warmTime">
                <el-checkbox v-model="form.isWarm" true-label="1" false-label="0"></el-checkbox>
                距离入驻有效期还剩
                <el-input type="number" style="width:80px;" name="warmTime" v-model.number="form.warmTime" size="mini"></el-input>
                天，预警入驻即将到期
            </el-form-item>
        </control-rule-block>

        <control-rule-block title="退孵设置（勾选后，入驻到期系统自动处理退孵，不勾选管理员手动处理退孵）" class="control-rule_appointment-rule">
            <input type="hidden" id="isHatback" name="isHatback" :value="form.isHatback">
            <el-form-item prop="isHatback">
                <el-checkbox v-model="form.isHatback" true-label="1" false-label="0"></el-checkbox>
                入驻到期后自动退孵
            </el-form-item>
        </control-rule-block>


        <el-form-item label-width="27px">
            <el-button type="primary" :disabled="isSaving" size="mini"
                       @click.stop.prevent="saveForm('form')">保存
            </el-button>
        </el-form-item>

    </el-form>
</div>

<script>
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwRenewalRule = JSON.parse(JSON.stringify(${fns:toJson(pwRenewalRule)}));
            return {
                form: {
                    id: pwRenewalRule.id || '',
                    isWarm: pwRenewalRule.isWarm || '',
                    isHatback: pwRenewalRule.isHatback || '',
                    warmTime: pwRenewalRule.warmTime || ''
                },
                isSaving: false
            }
        },
        computed: {
            formRules: {
                get: function () {
                    var validateWarmTime = function (rule, value, callback) {
                        var len = value.toString().split('').length;
                        if(value < 0){
                            callback(new Error('请输入正数'));
                        }else if (value && len > 4) {
                            callback(new Error('最大4位数'))
                        } else {
                            callback();
                        }
                    };
                    return {
                        warmTime:[
                            {required: true, message: '必填', trigger: 'blur'},
                            {validator: validateWarmTime, trigger: 'blur'}
                        ]
                    }
                }
            }
        },
        methods: {
            saveForm: function (formName) {
                var self = this;
                this.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.isSaving = true;
                        self.$refs.form.$el.submit();
                    }
                })
            }
        },
        created: function () {
            var message = '${message}';
            if (message) {
                this.$message({
                    type: message.indexOf('成功') > -1 ? 'success' : 'error',
                    message: message
                })
            }
            message = '';
        }
    })

</script>

</body>
</html>