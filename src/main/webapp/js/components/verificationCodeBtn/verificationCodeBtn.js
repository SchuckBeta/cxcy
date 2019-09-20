/**
 * Created by Administrator on 2018/6/20.
 */


    'use strict';

    var verificationCodeBtn = Vue.component('verification-code-btn', {
        template: '<div> <el-button :type="type" :disabled="isDisabled" @click.stop.prevent="postCode">{{ showText }} </el-button> </div>',
        props: {
            type: {
                type: String,
                default: 'text'
            },
            disabled: Boolean,
            text: {
                type: String,
                default: '获取验证码'
            },
            timeCount: {
                required: true,
                type: Number,
                default: 60
            }
        },
        data: function () {

            return {
                isTimeTick: false,
                timer: null,
                originCount: 0
            }
        },
        computed: {
            showText: {
                get: function () {
                    return this.isTimeTick ? (this.timeCount + 's后重新获取验证码') : this.text;
                }
            },
            isDisabled: function () {
                // return 'undefined' != typeof this.disabled ? this.disabled : this.isTimeTick;
                return this.isTimeTick;
            }
        },
        watch: {
            timeCount: function (value) {
                if(value <= 0){
                    this.clearTimer();
                }
            }
        },
        methods: {
            postCode: function () {
                this.$emit('post-code')
            },

            timeTick: function () {
                var self = this;
                self.isTimeTick = true;
                // self.timeCount = 60;
                this.timer = setTimeout(function () {
                    var timeCount = self.timeCount;
                    if (self.timeCount <= 0) {
                        self.isTimeTick = false;
                    } else {
                        self.timeTick()
                    }
                    if (!self.isTimeTick) {
                        self.clearTimer();
                    }
                    timeCount--;
                    self.$emit('update:timeCount', timeCount);
                }, 1000)
            },
            clearTimer: function () {
                this.timer && clearTimeout(this.timer);
                this.isTimeTick = false;
                this.$emit('update:timeCount', this.originCount);
                this.$emit('time-end')
            }
        },
        created: function () {
            this.originCount = this.timeCount;
        },
        destroyed: function () {
           this.clearTimer();
        }
    });


