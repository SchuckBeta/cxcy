<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="创建课程"></edit-bar>
    </div>
    <el-form :model="scoCourseForm" ref="scoCourseForm" autocomplete="off" auto-complete="off" :rules="scoCourseRules"
             size="mini" label-width="120px" :disabled="scoCourseDisabled">
        <el-form-item prop="code" label="课程代码：">
            <el-input v-model="scoCourseForm.code" maxlength="64" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="课程名：">
            <el-input v-model="scoCourseForm.name" maxlength="125" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="type" label="课程类型：">
            <el-select v-model="scoCourseForm.type">
                <el-option v-for="item in scoCourseTypes" :key="item.value" :value="item.value"
                           :label="item.label"></el-option>
            </el-select>
        </el-form-item>
        <el-form-item prop="nature" label="课程性质：">
            <el-radio-group v-model="scoCourseForm.nature">
                <el-radio v-for="item in scoCourseNatures" :key="item.value" :label="item.value">{{item.label}}
                </el-radio>
            </el-radio-group>
        </el-form-item>
        <el-form-item prop="planScore" label="计划学分：">
            <el-input v-model="scoCourseForm.planScore" maxlength="6" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="planTime" label="计划课时：">
            <el-input v-model="scoCourseForm.planTime" maxlength="4" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="overScore" label="合格成绩：">
            <el-input v-model="scoCourseForm.overScore" maxlength="6" class="w300"></el-input>
        </el-form-item>
        <el-form-item prop="professionalList" label="面向专业科类：" style="max-width: 800px;">
            <el-checkbox-group v-model="scoCourseForm.professionalList" class="el-checkbox-group-mr">
                <el-checkbox v-for="item in professionalList" :key="item.value" :label="item.value">{{item.label}}
                </el-checkbox>
            </el-checkbox-group>
        </el-form-item>
        <el-form-item prop="remarks" label="备注：" style="max-width: 800px;">
            <el-input type="textarea" :rows="3" v-model="scoCourseForm.remarks" maxlength="255"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click.stop="validateScoCourse">保存</el-button>
            <el-button @click.stop="goToBack">返回</el-button>
        </el-form-item>
    </el-form>
</div>
<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.regExpMixin],
        data: function () {
            var self = this;
            var validateCode = function (rules, value, callback) {
                var scoCourseForm = self.scoCourseForm;
                if (value) {
                    if (!self.onlyNumLetter.test(value)) {
                        return callback(new Error("请输入字母或者数字"))
                    }
                    var params = {
                        id: scoCourseForm.id,
                        code: scoCourseForm.code
                    };
                    return self.$axios.post('/sco/scoCourse/checkCode', Object.toURLSearchParams(params)).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error("课程代码已经存在"))
                        }
                        return callback();
                    })
                }
                return callback();
            };
            var validateName = function (rules, value, callback) {
                var scoCourseForm = self.scoCourseForm;
                if (value) {
                    var params = {
                        id: scoCourseForm.id,
                        name: scoCourseForm.name
                    };
                    return self.$axios.post('/sco/scoCourse/checkName',Object.toURLSearchParams(params)).then(function (response) {
                        var data = response.data;
                        if (!data) {
                            return callback(new Error("课程名称已经存在"))
                        }
                        return callback();
                    })
                }
                return callback();
            };
            var validateScore = function (rules, value, callback) {
                if (typeof value !== 'undefined') {
                    if (!self.scoCourseScore.test(value)) {
                        return callback(new Error("请输入3位数正整数，可带一位小数"))
                    }
                    return callback();
                }
                return callback();
            };

            var validateTime = function (rules, value, callback) {
                if (typeof value !== 'undefined') {
                    if (!self.posInteger.test(value)) {
                        return callback(new Error("请输入3位数正整数"))
                    }
                    return callback();
                }
                return callback();
            };

            var scoCourse = JSON.parse(JSON.stringify(${fns: toJson(scoCourse)}));
            return {
                scoCourseForm: {
                    id: '',
                    code: '',
                    name: '',
                    type: '',
                    nature: '',
                    planScore: '',
                    planTime: '',
                    overScore: '',
                    professionalList: [],
                    remarks: ''
                },
                scoCourse: scoCourse,
                scoCourseRules: {
                    code: [
                        {required: true, message: '请输入课程代码', trigger: 'blur'},
                        {max: 64, message: '请输入小于64位字符', trigger: 'blur'},
                        {validator: validateCode, trigger: 'blur'}
                    ],
                    name: [
                        {required: true, message: '请输入课程名称', trigger: 'blur'},
                        {max: 125, message: '请输入小于125位字符', trigger: 'blur'},
                        {validator: validateName, trigger: 'blur'}
                    ],
                    type: [
                        {required: true, message: '请选择课程类型', trigger: 'change'}
                    ],
                    nature: [
                        {required: true, message: '请选择课程性质', trigger: 'change'}
                    ],
                    planScore: [
                        {required: true, message: '请输入计划学分', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    planTime: [
                        {required: true, message: '请输入计划课时', trigger: 'blur'},
                        {max: 3, message: '请输入3位正整数', trigger: 'blur'},
                        {validator: validateTime, trigger: 'blur'}
                    ],
                    overScore: [
                        {required: true, message: '请输入合格成绩', trigger: 'blur'},
                        {validator: validateScore, trigger: 'blur'}
                    ],
                    professionalList: [
                        {required: true, message: '请选择面向专业科类', trigger: 'change'},
                    ],
                    remarks: [
                        {max: 225, message: '请输入小于225位字符', trigger: 'blur'}
                    ]
                },
                scoCourseDisabled: false,
                scoCourseTypes: [],
                scoCourseNatures: [],
                professionalList: []
            }
        },
        methods: {
            getScoCourseTypes: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=0000000102').then(function (response) {
                    self.scoCourseTypes = response.data || [];
                })
            },
            getScoCourseNatures: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=0000000108').then(function (response) {
                    self.scoCourseNatures = response.data || [];
                })
            },
            getProfessionalList: function () {
                var self = this;
                this.$axios.get('/sys/dict/getDictList?type=0000000111').then(function (response) {
                    self.professionalList = response.data || [];
                })
            },
            validateScoCourse: function () {
                var self = this;
                this.$refs.scoCourseForm.validate(function (valid) {
                    if (valid) {
                        self.submitScoCourse()
                    }
                })
            },
            goToBack: function () {
                window.history.go(-1);
            },
            submitScoCourse: function () {
                var self = this;
                this.scoCourseDisabled = true;
                this.$axios.post('/sco/scoCourse/saveScoCourse', this.scoCourseForm).then(function (response) {
                    var data = response.data;
                    self.scoCourseDisabled = false;
                    if (data.status === 1) {
                        self.assignFormData(self.scoCourseForm, data.data);
                        self.$msgbox({
                            type: 'success',
                            title: '提示',
                            closeOnClickModal: false,
                            closeOnPressEscape: false,
                            confirmButtonText: '确定',
                            showClose: false,
                            message: "保存成功"
                        }).then(function () {
                            self.goToBack();
                        }).catch(function () {
                        });
                    } else {
                        self.$alert(data.msg, "提示", {
                            type: "error"
                        })
                    }
                }).catch(function () {
                    self.scoCourseDisabled = false;
                })
            }
        },
        created: function () {
            this.assignFormData(this.scoCourseForm, this.scoCourse);
            this.getScoCourseTypes();
            this.getScoCourseNatures();
            this.getProfessionalList();
        }
    })

</script>
</body>
</html>