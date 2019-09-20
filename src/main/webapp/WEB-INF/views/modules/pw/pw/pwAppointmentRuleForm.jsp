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
        <edit-bar></edit-bar>
    </div>
    <el-form :model="pwAppointmentRuleForm" :rules="pwAppointmentRuleRules" action="${ctx}/pw/pwAppointmentRule/save" :disabled="isSaving"
             method="POST" size="mini" ref="pwAppointmentRuleForm">
        <input type="hidden" name="id" :value="pwAppointmentRuleForm.id">
        <control-rule-block title="选择预约审核模式" class="control-rule_appointment-rule">
            <el-form-item prop="isAuto">
                <el-radio-group v-model="pwAppointmentRuleForm.isAuto">
                    <el-radio-button name="isAuto" label="0">手动审核</el-radio-button>
                    <el-radio-button name="isAuto" label="1">自动审核</el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item prop="autoTime" v-if="pwAppointmentRuleForm.isAuto == '1'">
                <el-col :span="4">
                    <el-input name="autoTime" v-model="pwAppointmentRuleForm.autoTime"></el-input>
                </el-col>
                <el-col :span="8">
                    <span class="el-form-item-expository">分钟未审核，系统自动审核通过</span>
                </el-col>
            </el-form-item>
        </control-rule-block>
        <control-rule-block title="预约设置" class="control-rule_appointment-rule">
            <el-form-item prop="afterDays" label="允许预约" label-width="110px">
                <el-col :span="4">
                    <el-input name="afterDays" v-model="pwAppointmentRuleForm.afterDays"></el-input>
                </el-col>
                <el-col :span="8">
                    <span class="el-form-item-expository">天以内的房间</span>
                </el-col>
            </el-form-item>
            <el-form-item prop="isAppDayList" label="开放预约周次" label-width="110px">
                <el-checkbox-group v-model="pwAppointmentRuleForm.isAppDayList">
                    <el-checkbox-button name="isAppDayList" v-for="week in weekList" :key="week.value"
                                        :label="week.value">{{week.label}}
                    </el-checkbox-button>
                </el-checkbox-group>
            </el-form-item>
            <el-form-item prop="isTimeAuto" label="开放预约时间段" label-width="110px">
                <el-radio-group v-model="pwAppointmentRuleForm.isTimeAuto">
                    <el-radio-button name="isTimeAuto" label="1">不限</el-radio-button>
                    <el-radio-button name="isTimeAuto" label="0">自定义</el-radio-button>
                </el-radio-group>
            </el-form-item>
            <el-form-item v-if="pwAppointmentRuleForm.isTimeAuto == '0'" label-width="110px">
                <el-col :span="6">
                    <el-form-item prop="beginTime">
                        <el-time-select
                                style="width: 100%;"
                                placeholder="起始时间"
                                v-model="pwAppointmentRuleForm.beginTime"
                                :picker-options="{
                          start: '00:00',
                          step: '00:30',
                          end: '23:30'
                        }">
                        </el-time-select>
                    </el-form-item>
                </el-col>
                <el-col :span="1">
                    <span class="divide-zhi">至</span>
                </el-col>
                <el-col :span="6">
                    <el-form-item prop="endTime">
                        <el-time-select
                                placeholder="结束时间"
                                style="width: 100%;"
                                v-model="pwAppointmentRuleForm.endTime"
                                :picker-options="{
                      start: '00:00',
                      step: '00:30',
                      end: '23:30',
                      minTime: pwAppointmentRuleForm.beginTime
                    }">
                        </el-time-select>
                        <input type="hidden" name="beginTime" :value="pwAppointmentRuleForm.beginTime">
                        <input type="hidden" name="endTime" :value="pwAppointmentRuleForm.endTime">
                    </el-form-item>
                </el-col>
            </el-form-item>
        </control-rule-block>
        <el-form-item label-width="135px">
            <el-button type="primary" @click.stop.prevent="savePwAppointmentRuleForm">保存</el-button>
        </el-form-item>
    </el-form>
</div>

<script>
    'use strict';

    var validatorAutoTime = function (rule, value, callback) {
        if(value){
            if(/^[1-9][0-9]{0,1}$/.test(value)){
                callback()
            }else {
                return callback(new Error('请输入1-99间的数字'));
            }
        }
        callback()
    }

    new Vue({
        el: '#app',
        data: function () {
            var pwAppointmentRule = JSON.parse('${fns: toJson(pwAppointmentRule)}');
            var weekList = JSON.parse('${fns: toJson(weekList)}');

            return {
                pwAppointmentRuleForm: {
                    id: pwAppointmentRule.id,
                    isAuto: pwAppointmentRule.isAuto || '0',
                    autoTime: pwAppointmentRule.autoTime,
                    isAppDayList: pwAppointmentRule.isAppDayList || [],
                    isTimeAuto: pwAppointmentRule.isTimeAuto || '0',
                    beginTime: pwAppointmentRule.beginTime,
                    endTime: pwAppointmentRule.endTime,
                    afterDays: pwAppointmentRule.afterDays
                },
                weekList: weekList,
                isSaving: false
            }
        },
        computed: {
            pwAppointmentRuleRules: {
                get: function () {
                    var pwAppointmentRuleForm = this.pwAppointmentRuleForm;
                    return {
                        isAuto: [
                            {required: true, message: '请选择预约审核模式', trigger: 'change'}
                        ],
                        autoTime: [
                            {required: pwAppointmentRuleForm.isAuto == '1', message: '请输入自动审核时间', trigger: 'blur'},
                            {validator: validatorAutoTime,  trigger: 'blur'}
                        ],
                        afterDays: [
                            {required: true, message: '请输入预约天数', trigger: 'blur'},
                            {validator: validatorAutoTime, trigger: 'blur'}
                        ],
                        isAppDayList: [
                            {required: true, message: '请选择开放预约周次', trigger: 'change'}
                        ],
                        isTimeAuto: [
                            {required: true, message: '请选择开放预约时间段', trigger: 'change'}
                        ],
                        beginTime: [
                            {required: pwAppointmentRuleForm.isTimeAuto == '0', message: '请输入开始时间', trigger: 'change'}
                        ],
                        endTime: [
                            {required: pwAppointmentRuleForm.isTimeAuto == '0', message: '请输入结束时间', trigger: 'change'}
                        ]
                    }
                }
            }
        },
        methods: {
            savePwAppointmentRuleForm: function () {
                var $el = this.$refs.pwAppointmentRuleForm.$el;
                var self = this;
                this.$refs.pwAppointmentRuleForm.validate(function (valid) {
                    if (valid) {
                        self.isSaving = true;
                        $el.submit();
                    }
                })
            }
        },
        created: function () {
            var message = '${message}';
            if(message){
                this.show$message({
                    status: message.indexOf('成功') > -1,
                    msg: message
                })
            }
            message = '';
        }
    })

</script>

</body>
</html>