/**
 * Created by Administrator on 2018/7/24.
 */
'use strict';


Vue.component('audit-form', {
    template: '<el-form :model="auditForm" ref="auditForm" :action="action" method="POST" size="mini" :rules="auditFormRules" label-width="150px">' +
    '<el-form-item :label="taskName" prop="grade"><el-select placeholader="-请选择-" clearable v-model="auditForm.grade">' +
    '<el-option v-for="option in sOptions"  :key="option[defaultProp.value]" :label="option[defaultProp.label]" :value="option[defaultProp.value]"></el-option></el-select></el-form-item>' +
    '<el-form-item label="审核建议或意见：" prop="source"><el-input name="source" class="textarea-font-family" type="textarea" v-model="auditForm.source" :rows="4" placeholder="请给予您的意见和建议（最大长度100）"></el-input></el-form-item>' +
    '<el-form-item><input type="hidden" name="grade" :value="auditForm.grade"><el-button type="primary" :disabled="isSubmit" @click.stop.prevent="submitAuditForm">提交</el-button><el-button type="default" @click.stop.prevent="goHistory">返回</el-button></el-form-item><slot></slot></el-form>',
    props: {
        options: {
            type: Array,
        },
        action: String,
        defaultProp: {
            type: Object,
            default: function () {
                return {
                    label: 'state',
                    value: 'status'
                }
            }
        },
        taskName: {
            required: true,
            type: String
        }
    },
    data: function () {
        return {
            defaultOptions: [
                {state: '通过', status: '0'},
                {state: '不通过', status: '1'}
            ],
            auditForm: {
                grade: '',
                source: ''
            },
            isSubmit: false
        }
    },
    computed: {
        taskNameLabel: {
          get: function () {
              return this.taskName + '：'
          }
        },
        sOptions: {
            get: function () {
                return (this.options && this.options.length > 0) ? this.options : this.defaultOptions;
            }
        },
        auditFormRules: {
            get: function () {
                var state = '';
                if(this.auditForm.grade){
                    state = this.sOptionsEntires[this.auditForm.grade];
                }
                return {
                    grade: [
                        {required: true, message: '请选择'+this.taskName+'选项', trigger: 'change'}
                    ],
                    source: [
                        {required: state === '不通过' || state === '打回' || state.indexOf('退回修改') > -1, message: '请填写您的意见或建议', trigger: 'blur'},
                        { max: 100, message: '请输入不大于100位字符' , trigger: 'blur'}
                    ]
                }
            }
        },
        sOptionsEntires: {
            get: function () {
                return this.getEntries(this.sOptions, {value: 'status', label: 'state'})
            }
        }
    },
    methods: {
        goHistory:function(){
            window.history.go(-1)
        },
        submitAuditForm: function () {
            var auditForm = this.$refs.auditForm;
            var self = this;
            auditForm.validate(function (valid) {
                if(valid){
                    self.isSubmit = true;
                    auditForm.$el.submit();
                    self.$emit('submit-complete', self.auditForm)
                }
            })
        }
    }
});