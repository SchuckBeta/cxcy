<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<%@ page errorPage="/WEB-INF/views/error/formMiss.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <meta charset="UTF-8">
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

<body>

<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <shiro:hasPermission name="pw:pwCategory:edit">
            <edit-bar :second-name="saveForm.id ? '修改': '添加'" href="/pw/pwCategory/list"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="pw:pwCategory:edit">
            <edit-bar second-name="查看" href="/pw/pwCategory/list"></edit-bar>
        </shiro:lacksPermission>
    </div>


    <el-form size="mini" :model="saveForm" :rules="saveFormRules" ref="saveForm" :disabled="formDisabled"
             label-width="120px">

        <el-form-item prop="parent.id" label="父类别：" v-if="saveForm.parent.id != '1'">
            <el-select name="parentId" size="mini" v-model="saveForm.parent.id" placeholder="请选择查询条件" class="w300">
                <el-option
                        v-for="item in selectOptions"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                </el-option>
            </el-select>
        </el-form-item>

        <el-form-item prop="name" label="名称：">
            <el-input name="name" v-model="saveForm.name" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="remarks" label="备注：">
            <el-input type="textarea" name="remarks" :rows="5" v-model="saveForm.remarks" maxlength="200" style="width:500px;"></el-input>
        </el-form-item>

        <el-form-item label="编号规则" class="item-bottom-border"></el-form-item>

        <el-form-item prop="pwFassetsnoRule.prefix" label="前缀：">
            <el-input name="pwFassetsnoRulPrefix" v-model="saveForm.pwFassetsnoRule.prefix" class="w300" maxlength="24"></el-input>
        </el-form-item>

        <el-form-item prop="pwFassetsnoRule.startNumber" label="开始编号：" v-if="saveForm.parent.id != '1'">
            <el-input name="pwFassetsnoRulStartNumber" v-model="saveForm.pwFassetsnoRule.startNumber" class="w300" @change="handleChangeStartNumber"></el-input>
        </el-form-item>

        <el-form-item prop="pwFassetsnoRule.numberLen" label="编号位数：" v-if="saveForm.parent.id != '1'">
            <el-input name="pwFassetsnoRulNumberLen" v-model="saveForm.pwFassetsnoRule.numberLen" class="w300"></el-input>
            <span style="color:#999;margin-left: 10px;">表示数字最小的位数，不足位数的前面补0</span>
        </el-form-item>

        <el-form-item label="示例：" v-if="saveForm.parent.id != '1'" style="color:#999;">
            {{rule}}
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="pw:pwCategory:edit">
                <el-button type="primary" :disabled="formDisabled"
                           @click.stop.prevent="save('saveForm')">保存
                </el-button>
            </shiro:hasPermission>
        </el-form-item>

    </el-form>



</div>

<script>

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var pwCategory = JSON.parse(JSON.stringify(${fns:toJson(pwCategory)})) || [];
            var pwFassetsnoRule = pwCategory.pwFassetsnoRule || {};
            var pCategoryTypes = JSON.parse(JSON.stringify(${fns:toJson(fns:findChildrenCategorys('1'))}));
            return {
				saveForm:{
                    id: pwCategory.id || '',
                    parent: {
                        id:pwCategory.parentId || ''
                    },
                    name: pwCategory.name || '',
                    remarks: pwCategory.remarks || '',
                    pwFassetsnoRule:{
                        id:pwFassetsnoRule.id || '',
                        prefix:pwFassetsnoRule.prefix || '',
                        startNumber:pwFassetsnoRule.startNumber || '',
                        numberLen:pwFassetsnoRule.numberLen || ''
                    }
                },
                selectOptions:pCategoryTypes,
                formDisabled:false,
                parentPrefix:pwCategory.parentPrefix,
                message:'${message}'
            }
        },
        computed:{
            numberLength:{
                get:function () {
                    return this.saveForm.pwFassetsnoRule.startNumber.length;
                }  
            },
            saveFormRules:{
                get:function () {
                    var self = this;
                    var nameReg = /['"\s“”‘’]+/;
                    var positiveReg = /^[1-9][0-9]*$/;
                    var validateName = function (rule, value, callback) {
                        if (nameReg.test(value)) {
                            callback(new Error('名称存在空格或者引号'))
                        } else {
                            callback();
                        }
                    };
                    var validateStartNumber = function (rule, value, callback) {
                        var len = value.toString().length;
                        if (!positiveReg.test(value)) {
                            callback(new Error('请输入正整数'));
                        }else if((len > 8)){
                            callback(new Error('开始编号最大8位数'))
                        }else{
                            callback();
                        }
                    };
                    var validateNumberLen = function (rule, value, callback) {
                        var len = value.toString().length;
                        if (!positiveReg.test(value)) {
                            callback(new Error('请输入正整数'));
                        }else if((len > 2)){
                            callback(new Error('编号位数最大2位数'))
                        }else if(value < self.numberLength){
                            callback(new Error('编号位数必须大于开始编号的长度'));
                        } else{
                            callback();
                        }
                    };
                    return {
                        parent:{
                            id: [
                                {required: true, message: '请选择父类别', trigger: 'change'}
                            ]
                        },
                        name: [
                            {required: true, message: '请输入名称', trigger: 'change'},
                            {min: 1, max: 30, message: '长度在 1 到 30 个字符', trigger: 'change'},
                            {validator: validateName, trigger: 'change'}
                        ],
                        pwFassetsnoRule:{
                            prefix: [
                                {required: true, message: '请输入前缀', trigger: 'change'}
                            ],
                            startNumber: [
                                {required: true, message: '请输入开始编号', trigger: 'change'},
                                {validator: validateStartNumber, trigger: 'change'}
                            ],
                            numberLen: [
                                {required: true, message: '请输入编号位数', trigger: 'change'},
                                {validator: validateNumberLen, trigger: 'change'}
                            ]
                        }
                    }
                }
            },
            rule:{
                get:function () {
                    var prefix = this.saveForm.pwFassetsnoRule.prefix;
                    var startNumber = this.saveForm.pwFassetsnoRule.startNumber;
                    var numberLen = this.saveForm.pwFassetsnoRule.numberLen;
                    if (prefix && startNumber && numberLen) {
                        return this.parentPrefix + prefix + this.prefixZero(parseInt(startNumber), parseInt(numberLen));
                    }
                    return ''
                }
            }
        },
        methods: {
            handleChangeStartNumber:function () {
                if(this.saveForm.pwFassetsnoRule.numberLen){
                    this.$refs.saveForm.validateField('pwFassetsnoRule.numberLen');
                }
            },
            save:function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                    	self.saveAjax();
                    }
                })
            },
			saveAjax:function () {
                var self = this;
                this.formDisabled = true;
                this.$axios({
                    method:'POST',
                    url:'/pw/pwCategory/save',
                    data:self.saveForm
                }).then(function (response) {
                    var data = response.data;
                    if(data.status == '1'){
                        window.location.href = self.frontOrAdmin + '/pw/pwCategory/list';
                    }
                    self.formDisabled = false;
                    self.$message({
                        message: data.status == '1' ? '保存成功' : data.msg || '保存失败',
                        type: data.status == '1' ? 'success' : 'error'
                    })
                }).catch(function (error) {
                    self.formDisabled = false;
                    self.$message({
                        message: self.xhrErrorMsg,
                        type:'error'
                    })
                });
			},
            prefixZero:function (num, length) {
                return (Array(length).join('0') + num).slice(-length);
            }
        },
        created: function () {
            if (this.message) {
                this.$message({
                    message: this.message,
                    type: this.message.indexOf('成功') > -1 ? 'success' : 'warning'
                });
                this.message = '';
            }
        }
    })

</script>

</body>
</html>