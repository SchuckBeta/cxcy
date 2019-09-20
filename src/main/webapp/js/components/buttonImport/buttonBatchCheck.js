
'use strict'

Vue.component('button-batch-check',{
    template:'<div style="display: inline-block;"><el-button type="primary" :disabled="multipleSelection.length < 1" size="mini" @click.stop.prevent="batchCheckVisible = true"><i class="iconfont icon-shenhetongguo1"></i>批量审核 </el-button>'+
        '<el-dialog title="批量审核" :visible.sync="batchCheckVisible" width="25%" top="25vh">\n'+
        '    <el-form :model="batchCheckForm" ref="batchCheckForm" style="text-align: center;">\n'+
        '         大于\n'+
        '        <el-input type="number" v-model="batchCheckForm.score" auto-complete="off" size="mini"\n'+
        '                  style="width:70px"></el-input>\n'+
        '        分 通过\n'+
        '    </el-form>\n'+
        '    <div slot="footer" class="dialog-footer">\n'+
        '        <el-button size="mini" @click.stop.prevent="batchCheckVisible = false">取 消</el-button>\n'+
        '        <el-button size="mini" type="primary" @click.stop.prevent="batchCheckSubmit">确 定\n'+
        '        </el-button>\n'+
        '    </div>\n'+
        '</el-dialog></div>',
    props:{
        multipleSelection:{
            type:Array,
            default:function(){
                return []
            }
        },
        min:Number,
        max:Number

    },
    data:function(){
        return {
            batchCheckVisible:false,
            batchCheckForm: {
                range: '0',
                score: 60,
                isPass: '0'
            }
        }
    },
    methods:{
        visible:function(){
            this.batchCheckVisible = false;
        },
        batchCheckSubmit: function () {
            var checkScore = this.batchCheckForm.score;
            if(checkScore){
                var min = this.min || 0;
                var max = this.max || Number.MAX_VALUE;
                if(checkScore < min || checkScore > max){
                    this.show$message({
                        status: false,
                        msg: '请输入'+min+'到'+max+'的数字'
                    })
                }else {
                    this.$emit('batch-check-submit',{
                        score:checkScore
                    },this.visible());
                }
            }else{
                this.show$message({
                    status: false,
                    msg: '请输入分数'
                })
            }

        }
    }

})