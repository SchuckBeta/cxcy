<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" class="container-fluid mgb-60" style="display: none">
    <div class="mgb-20">
        <edit-bar :second-name="secondName"></edit-bar>
    </div>
    <el-form :model="drForm" ref="drForm" :rules="drFormRules" :disabled="disabled" size="mini" label-width="120px"
             autocomplete="off" style="width: 520px;">
        <input type="text" style="display: none" :value="drForm.id">
        <el-form-item prop="no" label="设备号：">
            <el-input v-model="drForm.no" maxlength="64"></el-input>
        </el-form-item>
        <el-form-item prop="psw" label="设备密码：">
            <el-input v-model="drForm.psw" maxlength="64"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="设备名称：">
            <el-input v-model="drForm.name" maxlength="64"></el-input>
        </el-form-item>
        <el-form-item prop="delFlag" label="是否启用：" required>
            <el-switch v-model="drForm.delFlag" active-value="1" inactive-value="0"></el-switch>
        </el-form-item>
        <el-form-item prop="type" label="设备类型：">
            <el-select v-model="drForm.type">
                <el-option v-for="item in eTypeList" :key="item.label" :label="item.value"
                           :value="item.label"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="ip" label="IP地址：">
            <el-input v-model="drForm.ip" maxlength="64"></el-input>
        </el-form-item>
        <el-form-item prop="port" label="端口号：">
            <el-input v-model="drForm.port" maxlength="5"></el-input>
        </el-form-item>
        <el-form-item prop="drNoList" label="设备出口编号：">
            <el-select v-model="drForm.drNoList" multiple placeholder="请选择" style="width: 100%">
                <el-option
                        v-for="item in drKeyList"
                        :key="item.key"
                        :label="item.name"
                        :value="item.key">
                </el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="tindex" label="设备索引号：">
            <el-input v-model="drForm.tindex" maxlength="5"></el-input>
        </el-form-item>
        <el-form-item prop="tsize" label="设备每次取号：">
            <el-input v-model="drForm.tsize" maxlength="5"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop="validateDrForm">保存</el-button>
            <el-button @click.stop="goToback">返回</el-button>
        </el-form-item>
    </el-form>
</div>
<script type="text/javascript">
    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            var self = this;
            var eTypeList = JSON.parse(JSON.stringify(${fns: toJson(eTypeList)}));
            var drKeyList = JSON.parse(JSON.stringify(${fns: toJson(drKeyList)}));
            var regIp = /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/;
            drKeyList.forEach(function (item) {
                item.key = item.key.toString()
            });
            var validatePort = function (rules, value, callback) {
                if (value) {
                    var drForm = self.drForm;
                    var params = {
                        id: drForm.id,
                        no: drForm.no,
                        ip: drForm.ip,
                        port: value
                    }
                    return self.$axios.get('/dr/drEquipment/ajaxCheck?' + Object.toURLSearchParams(params)).then(function (response) {
                        var data = response.data;
                        if(data.ret == "1"){
                            return callback();
                        }
                        return callback(new Error(data.msg))
                    })
                }
                return callback()
            };
            var validateNumber = function (rules, value, callback) {
                if(value){
                    if(/^\d+$/.test(value)){
                        return callback()
                    }
                    return callback(new Error("请输入数字"))
                }
                return callback();
            };
            var validateIp = function (rules, value, callback) {
                if(value){
                    if(regIp.test(value)){
                        return callback();
                    }
                    return callback(new Error('ip地址不合格'));
                }
                return callback();
            };
            var validateDrBH = function (rules, value, callback) {
                if(value){
                    if(/^[0-9a-zA-Z]+\-?[0-9a-zA-Z]*$/.test(value)){
                        return callback();
                    }
                    return callback(new Error('请输入数字和英文'))
                }
                return callback();
            };

            return {
                drForm: {
                    id: '',
                    no: '',
                    psw: '',
                    name: '',
                    delFlag: '0',
                    type: '',
                    ip: '',
                    port: '',
                    drNoList: '',
                    tindex: '',
                    tsize: ''
                },
                drFormRules: {
                    no: [
                        {required: true, message: '请输入设备号', trigger: 'blur'},
                        {validator: validateDrBH, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入设备名称', trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择设备类型', trigger: 'change'}
                    ],
                    port: [
                        {required: true, message: '请输入端口号', trigger: 'blur'},
                        {validator: validateNumber, trigger: 'blur'},
                        {validator: validatePort, trigger: 'blur'}
                    ],
                    ip: [
                        {required: true, message: '请输入IP地址', trigger: 'change'},
                        {validator: validateIp, trigger: 'blur'}
                    ],
                    tindex: [
                        {validator: validateNumber, trigger: 'blur'}
                    ],
                    tsize: [
                        {validator: validateNumber, trigger: 'blur'}
                    ]
                },
                disabled: false,
                eTypeList: eTypeList,
                drKeyList: drKeyList
            }
        },
        computed: {
            secondName: function () {
                return this.drForm.id ? '修改' : '添加'
            }
        },
        methods: {
            validateDrForm: function () {
                var self = this;
                this.$refs.drForm.validate(function (valid) {
                    if (valid) {
                        self.saveDr()
                    }
                })
            },
            saveDr: function () {
                var self = this;
                this.disabled = true;
                this.$axios.post('/dr/drEquipment/saveDrEq', this.drForm).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            showClose: false,
                            type: 'success'
                        }).then(function () {
                            location.href = '/a/dr/drEquipment/list'
                        }).catch(function () {

                        })
                    } else {
                        self.$alert('保存失败', '提示', {
                            type: 'error'
                        })
                    }
                    self.disabled = false;
                })
            },
            goToback: function () {
                history.go(-1);
            }
        },
        created: function () {
            var drEquipment = JSON.parse(JSON.stringify(${fns: toJson(drEquipment)}));
            this.assignFormData(this.drForm, drEquipment);
        }
    })

</script>
</body>
</html>