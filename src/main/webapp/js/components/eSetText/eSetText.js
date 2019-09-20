/**
 * Created by Administrator on 2018/7/17.
 */


'use strict';


Vue.component('e-set-text', {
    template: '<div class="e-set-text-container"><div class="e-set-text" v-show="!editing" @dblclick.stop.prevent="dbClick"><span v-show="text">{{text}}</span><span v-show="!text" class="empty empty-color">双击设置编号</span></div><div class="input-set-content" v-show="editing"><el-input class="input-set-text" size="mini" v-model="selfText"></el-input><el-button type="default" class="btn-set" @click.stop.prevent="changeText"><i class="iconfont icon-tongguo"></i></el-button></div></div>',
    props: {
        text: String,
        editing: Boolean,
        row: Object,
        min: Number,
        max: Number
    },
    data: function () {
        return {
            selfText: ''
        }
    },
    watch:  {
      text: function (value) {
          this.selfText = value;
      }
    },
    methods: {
        dbClick: function () {
            this.$emit('update:editing', true)
        },
        changeText: function () {
            if(this.selfText && /^[a-zA-Z0-9]+$/.test(this.selfText)){
                var min = this.min || 0;
                var max = this.max || Number.MAX_VALUE;
                var len = this.selfText.length;
                if(len < min || len > max){
                    this.show$message({
                        status: false,
                        msg: '请输入'+min+'至'+max+'位字符'
                    })
                }else {
                    this.$emit('change', {
                        text: this.selfText,
                        row: this.row
                    });
                }

            }else {
                this.show$message({
                    status: false,
                    msg: '请输入数字或者字母'
                })
            }
        }
    },
    mounted: function () {
        this.selfText = this.text;
    }
})