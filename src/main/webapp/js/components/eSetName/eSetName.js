/**
 * Created by Administrator on 2018/7/17.
 */


'use strict';


Vue.component('e-set-name', {
    template: '<div class="e-set-text-container"><div class="e-set-text" v-show="!editing" @dblclick.stop.prevent="dbClick"><span v-show="text">{{text}}</span><span v-show="!text" class="empty empty-color">双击设置名称</span></div><div class="input-set-content" v-show="editing"><el-input class="input-set-text" size="mini" v-model="selfText"></el-input><el-button type="default" class="btn-set" @click.stop.prevent="changeText"><i class="iconfont icon-tongguo"></i></el-button></div></div>',
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

            var len = this.selfText.split('').length;
            var max = this.max || Number.MAX_VALUE;
            if (!this.selfText) {
                this.$message({
                    message: '名称不能为空！',
                    type: 'warning'
                });
                return false;
            }else if(/[@#\$%\^&\*\s]+/g.test(this.selfText)){
                this.$message({
                    message: '请不要输入特殊符号！',
                    type: 'warning'
                });
                return false;
            }else if(len > max){
                this.$message({
                    message: '请输入不大于' + max + '位字符的名称！',
                    type: 'warning'
                });
                return false;
            }


            this.$emit('change', {
                text: this.selfText,
                row: this.row
            });

        }
    },
    mounted: function () {
        this.selfText = this.text;
    }
});