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
        <shiro:hasPermission name="pw:pwSpace:edit">
            <edit-bar :second-name="campusForm.id ? '校区修改': '校区添加'" href="/pw/pwSpace/list"></edit-bar>
        </shiro:hasPermission>
        <shiro:lacksPermission name="pw:pwSpace:edit">
            <edit-bar second-name="校区查看" href="/pw/pwSpace/list"></edit-bar>
        </shiro:lacksPermission>
    </div>


    <el-form size="mini" :model="campusForm" :rules="campusFormRules" ref="campusForm" :disabled="formDisabled"
             label-width="120px" action="${ctx}/pw/pwSpace/save" method="POST">
        <input type="hidden" id="id" name="id" :value="campusForm.id">
        <input type="hidden" id="parent.id" name="parent.id" :value="campusForm.parentId">
        <input type="hidden" id="type" name="type" value="1">
        <input type="hidden" id="secondName" name="secondName" :value="secondName">

        <el-form-item label="所属学院：" v-if="school">
            {{school}}
        </el-form-item>

        <el-form-item prop="name" label="校区名称：">
            <el-input name="name" v-model="campusForm.name" class="w300"></el-input>
        </el-form-item>

        <el-form-item prop="remarks" label="备注：">
            <el-input type="textarea" name="remarks" :rows="5" v-model="campusForm.remarks" maxlength="200" style="width:500px;"></el-input>
        </el-form-item>


        <el-form-item>
            <shiro:hasPermission name="pw:pwSpace:edit">
                <el-button type="primary" :disabled="formDisabled"
                           @click.stop.prevent="saveForm('campusForm')">保存
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
            var pwSpace = JSON.parse(JSON.stringify(${fns:toJson(pwSpace)})) || [];

            var nameReg = /['"\s“”‘’]+/;
            var validateName = function (rule, value, callback) {
                if (nameReg.test(value)) {
                    callback(new Error('校区名称存在空格或者引号'))
                } else {
                    callback();
                }
            };

            return {
                campusForm:{
                    id: pwSpace.id || '',
                    parentId:pwSpace.parentId || '',
                    name: pwSpace.name || '',
                    remarks: pwSpace.remarks || ''
                },
                school:'${school}',
                secondName:'${secondName}',
                formDisabled:false,
                message:'${message}',
                campusFormRules:{
                    name: [
                        {required: true, message: '请输入校区名称', trigger: 'change'},
                        {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'change'},
                        {validator: validateName, trigger: 'change'}
                    ]
                }
            }
        },
        methods: {
            saveForm:function (formName) {
                var self = this;
                self.$refs[formName].validate(function (valid) {
                    if (valid) {
                        self.formDisabled = true;
                        self.$refs.campusForm.$el.submit();
                    }
                })
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