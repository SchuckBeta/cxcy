/**
 * Created by Administrator on 2018/7/24.
 */
'use strict';


Vue.component('score-form', {
    template: '<el-form :model="scoreForm" ref="scoreForm" :action="action" method="POST" size="mini" :rules="scoreFormRules" label-width="150px">' +
    '<el-form-item :label="labelText" prop="gScore"><el-input name="gScore" type="number" style="width:200px;" v-model.number="scoreForm.gScore" placeholder="输入整数"></el-input></el-form-item>' +
    '<el-form-item label="审核建议或意见：" prop="source"><el-input name="source" type="textarea" class="textarea-font-family" v-model="scoreForm.source" :rows="4" placeholder="请给予您的意见和建议（最大长度100）"></el-input></el-form-item>' +
    '<el-form-item><el-button type="primary" :disabled="isSubmit" @click.stop.prevent="submitScoreForm">提交</el-button><el-button type="default" @click.stop.prevent="goHistory">返回</el-button><input type="text" style="display: none"/></el-form-item><slot></slot></el-form>',
    props: {
        label: String,
        action: String
    },
    data: function () {
        return {
            scoreForm: {
                gScore: '',
                source: ''
            },
            isSubmit: false,
        }
    },
    computed: {
        labelText: {
          get: function () {
              return this.label + '：'
          }
        },
        scoreFormRules: {
            get: function () {
                var self = this;
                var label = this.label;
                var validateGScore = function (rule,value,callback) {
                    if(value){
                        if(self.label == '评分'){
                            if(value >= 0 && value <= 100 && /^\d{1,}(?!\.)$/g.test(value)){
                                callback();
                            }else{
                                callback(new Error('请输入0-100间的整分数'));
                            }
                        }else if(self.label.indexOf('资金分配') > -1 || self.label.indexOf('经费') > -1){
                            if(value >= 0 && value <= 10000000000 && /^\d{1,}(?!\.)$/g.test(value)){
                                callback();
                            }else{
                                callback(new Error('请输入0-10000000000的整数金额'));
                            }
                        }
                    }
                    callback();
                };
                return {
                    gScore: [
                        {required: true, message: '请输入'+label, trigger: 'blur'},
                        {validator:validateGScore,trigger:'blur'}
                    ],
                    source: [
                        { max: 100, message: '请输入不大于100位字符' , trigger: 'blur'}
                    ]
                }
            }
        }
    },
    methods: {
        goHistory:function(){
            window.history.go(-1)
        },
        submitScoreForm: function () {
            var scoreForm = this.$refs.scoreForm;
            var self = this;
            scoreForm.validate(function (valid) {
                if(valid){
                    self.isSubmit = true;
                    scoreForm.$el.submit();
                    self.$emit('submit-complete', self.scoreForm)
                }
            })
        }
    }
});/**
 * Created by lqz on 2018/8/6.
 */
