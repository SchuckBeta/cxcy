
'use strict'


Vue.component('button-advice-level',{
    template: '<div style="display: inline-block;"><el-button type="primary" :disabled="multipleSelection.length < 1" size="mini" @click.stop.prevent="adviceLevelVisible = true"><i class="iconfont icon-tuijianjibie"></i>推荐级别 </el-button>'+
        '<el-dialog title="推荐级别" :visible.sync="adviceLevelVisible" width="25%" top="25vh">\n'+
        '    <el-form :model="adviceLevelForm" ref="adviceLevelForm" style="text-align: center;">\n'+
        '          <el-radio-group v-model="adviceLevelForm.adviceLevelRadio">\n'+
        '                <el-radio v-for="level in adviceLevel" :label="level.value" :key="level.value">{{level.label}}</el-radio>\n'+
        '          </el-radio-group>\n'+
        '     </el-form>\n'+
        '     <div slot="footer" class="dialog-footer">\n'+
        '          <el-button size="mini" @click.stop.prevent="adviceLevelVisible = false">取 消</el-button>\n'+
        '          <el-button size="mini" type="primary" @click.stop.prevent="adviceLevelSubmit">确 定\n'+
        '          </el-button>\n'+
        '      </div>\n'+
        '</el-dialog></div>',
    props:{
        multipleSelection:{
            type:Array,
            default:function(){
                return []
            }
        },
        adviceLevel:Array
    },
    data:function(){
        return {
            adviceLevelVisible:false,
            adviceLevelForm: {
                adviceLevelRadio: '0000000264',
                adviceLevelRadioVal: ''
            }
        }
    },
    methods:{
        visible:function(){
            this.adviceLevelVisible = false;
        },
        adviceLevelSubmit:function(){
            this.$emit('advice-level-submit',{
                adviceLevelRadioVal:this.adviceLevelForm.adviceLevelRadio
            },this.visible());

        }
    }

})