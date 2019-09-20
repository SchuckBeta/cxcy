/**
 * Created by Administrator on 2018/6/19.
 */

'use strict';


Vue.component('mobile-form', {
    template: '<el-form :model="mobileForm" ref="mobileForm" size="mini" label-width="100px">\n' +
    '        <el-form-item v-show="!isAdd" label="当前手机号：">\n' +
    '            <el-input :value="oldMobile" name="oldMobile" :disabled="true" placeholder="请填写当前手机号码"></el-input>\n' +
    '        </el-form-item>\n' +
    '        <el-form-item :label="mobileLabel" prop="mobile" :rules="sendCodeAble ? mobileRule : []"\n' +
    '                      >\n' +
    '            <el-input v-model="mobileForm.mobile" @change="$emit(\'update:mobile\', mobileForm.mobile)">\n' +
    '                <template slot="prepend">+86</template>\n' +
    '            </el-input>\n' +
    '        </el-form-item>\n' +
    '        <el-form-item v-if="sendCodeAble && hasMvcode" label="验证码：" prop="yzm" :rules="yzmRules">\n' +
    '            <el-input name="yzm" v-model="mobileForm.yzm" :disabled="!mobileForm.mobile">\n' +
    '                <verification-code-btn slot="append" ref="verificationCode" :time-count.sync="timeCount"\n' +
    '                                       style="width: 92px;text-align: center;"\n' +
    '                                       :disabled="postCodeDisabled" @post-code="postCode"\n' +
    '                                       @time-end="postCodeDisabled = true"></verification-code-btn>\n' +
    '            </el-input>\n' +
    '        </el-form-item>\n' +
    '        <slot></slot>\n' +
    '    </el-form>',
    mixins: [Vue.verifyExpressionMixin],
    props: {
        oldMobile: String,
        isAdd: Boolean,
        sendCodeAble: {
            type: Boolean,
            default: true
        }
    },
    computed: {
        mobileLabel: function () {
            return '手机号：'
        },
        mobileRule: function () {
            var self = this;
            var validateRepeatMobile = function (rules, value, callback) {
                if (value) {
                    if (value === self.oldMobile) {
                        return callback(new Error("请不要输入已绑定的手机号"))
                    }
                    return callback();
                }
                return callback();
            };
            var validateMobileXhr = this.validateMobileXhr;
            var validateMobile = this.validateMobile;
            var rules = [
                {required: true, message: '请填写手机号', trigger: 'blur'},
                {validator: validateMobile, trigger: 'blur'},
                {validator: validateRepeatMobile, trigger: 'blur'}
            ];
            if (this.sendCodeAble) {
                rules.push({validator: validateMobileXhr, trigger: 'blur'})
            }
            return rules;
        }
    },
    data: function () {
        var validateMobile = this.validateMobile;
        var validateVerityCodeXhr = this.validateVerityCodeXhr;
        return {
            mobileForm: {
                mobile: '',
                oldMobile: '',
                yzm: ''
            },
            timeCount: 60,
            postCodeDisabled: false,
            oldMobileRule: [
                {required: true, message: '请填写手机号', trigger: 'blur'},
                {validator: validateMobile, trigger: 'blur'}
            ],
            yzmRules: [
                {required: true, message: '请填写验证码', trigger: 'blur'},
                {validator: validateVerityCodeXhr, trigger: 'blur'},
            ]
        }
    },
    methods: {
        postCode: function () {
            var self = this;
            this.$refs.mobileForm.validateField('mobile', function (errorMsg) {
                if (errorMsg) return;
                self.postCodeDisabled = true;
                self.$axios.post('/mobile/sendMobileValidateCode?mobileVcode=' + self.mobileForm.mobile).then(function (response) {
                    if (response.data) {
                        self.$refs.verificationCode.timeTick();
                        return;
                    }
                    self.postCodeDisabled = false;
                })
            })
        },

        mobileFormValidate: function () {
            var self = this;
            this.$refs.mobileForm.validate(function (valid) {
                if (valid) {
                    self.$emit('update-user-mobile', self.mobileForm)
                }
            })
        },

        clearMobileForm: function () {
            this.$refs.mobileForm.resetFields();
            this.$refs.verificationCode && this.$refs.verificationCode.clearTimer();
        }
    },
    mounted: function () {
    }
});
