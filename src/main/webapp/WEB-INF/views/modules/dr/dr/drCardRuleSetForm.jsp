<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>

</head>
<body>

<div id="app" class="container-fluid container-fluid_bg" v-show="pageLoad" style="display: none">
   <div class="mgb-20">
       <edit-bar></edit-bar>
   </div>
    <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" method="POST" class="mgb-20"
                 :disabled="ruleFormDisabled"
                 action="${ctx}/dr/drCardRule/save">
            <input type="hidden" name="id" :value="drCardRuleId">
            <input type="hidden" name="isEnter" :value="ruleForm.isEnter == 1 ? 1 : 0">
            <input type="hidden" name="isOut" :value="ruleForm.isOut == 1 ? 1 : 0">
            <control-rule-block>
                <el-checkbox slot="checkbox"  v-model="ruleForm.isEnter" label="1">未进入基地预警
                </el-checkbox>
                <div class="control-rule-input">
                    <span>连续</span>
                    <el-form-item prop="enterTime" size="mini">
                        <el-input v-model.number="ruleForm.enterTime" name="enterTime" placeholder="输入天数"
                                  style="width: 80px;"></el-input>
                    </el-form-item>
                    <span>天未进入基地，系统预警</span>
                </div>
            </control-rule-block>
            <control-rule-block>
                <el-checkbox slot="checkbox" v-model="ruleForm.isOut" label="1">未出基地预警</el-checkbox>
                <div class="control-rule-input">
                    <span>已进入基地，截止当天</span>
                    <el-form-item required style="width: 144px;">
                        <input type="hidden" name="beginTime" :value="ruleForm.beginTime">
                        <%--<el-col :span="10" style="width: 148px;">--%>
                            <%--<el-form-item prop="beginTime" size="mini">--%>
                                <%--<el-time-select class="control-rule-time_select"--%>
                                                <%--placeholder="起始时间"--%>
                                                <%--name="beginTime"--%>
                                                <%--v-model="ruleForm.beginTime"--%>
                                                <%--:picker-options="{--%>
                                    <%--start: '00:00',--%>
                                    <%--step: '00:30',--%>
                                    <%--end: '23:59'--%>
                                    <%--}">--%>
                                <%--</el-time-select>--%>
                            <%--</el-form-item>--%>
                        <%--</el-col>--%>
                        <%--<el-col class="line" :span="4" style="width: 20px; text-align: center;line-height: 28px;">至--%>
                        <%--</el-col>--%>
                        <el-col :span="10" style="width: 148px;">
                            <el-form-item prop="endTime" size="mini">
                                <el-time-select class="control-rule-time_select"
                                                placeholder="结束时间"
                                                name="endTime"
                                                v-model="ruleForm.endTime"
                                                :picker-options="{
                                    start: '00:00',
                                    step: '00:30',
                                    end: '23:59',
                                    minTime: ruleForm.beginTime
                                    }">
                                </el-time-select>
                            </el-form-item>
                        </el-col>
                    </el-form-item>
                    <span>未出基地预警</span>
                </div>

            </control-rule-block>
            <div class="text-center" style="width: 700px;">
                <el-button type="primary" size="mini" @click="submitForm('ruleForm')">保 存</el-button>
            </div>
        </el-form>

    <div class="mgb-20">
        <edit-bar>自定义不预警时间段</edit-bar>
    </div>
        <el-form :model="preWarningTimeForm" ref="preWarningTimeForm" label-width="60px">
            <div class="dynamic-warning-time_line" v-for="(drCreGtime, index) in preWarningTimeForm.drCreGtimes">
                <el-form-item required size="mini" label="日期" :key="drCreGtime.key"
                              :rules="[{required: true, message: '请选择日期范围'}]"
                              :prop="'drCreGtimes.'+index+'.rangeDate'">
                    <el-date-picker
                            v-model="drCreGtime.rangeDate"
                            type="daterange"
                            range-separator="至"
                            start-placeholder="开始日期"
                            end-placeholder="结束日期"
                            value-format="yyyy-MM-dd"
                            :default-time="['00:00:00', '23:59:59']">
                    </el-date-picker>
                    <el-button @click="addDrCreGtime('preWarningTimeForm')" type="primary" style="vertical-align: top">
                        添加
                    </el-button>
                </el-form-item>
            </div>
        </el-form>
        <div class="table-container">
            <el-table
                    class="table"
                    :data="drCardreGtimeData"
                    size="small"
                    style="width: 100%">
                <el-table-column
                        type="index"
                        label="序号"
                        align="center"
                        width="100">
                    <template slot-scope="scope">
                        {{(scope.$index + 1) * 10}}
                    </template>
                </el-table-column>
                <el-table-column
                        prop="beginDate"
                        align="center"
                        label="开始时间">
                </el-table-column>
                <el-table-column
                        prop="endDate"
                        align="center"
                        label="结束时间">
                </el-table-column>
                <el-table-column
                        label="操作">
                    <template slot-scope="scope">
                        <el-button type="text" :disabled="isSubmitDr || scope.$index === 0" size="mini"
                                   @click.stop.prevent="upDrCardItem(scope.row, scope.$index)">上移
                        </el-button>
                        <el-button type="text" :disabled="isSubmitDr || scope.$index === drCardreGtimeData.length - 1"
                                   size="mini" @click.stop.prevent="downDrCardItem(scope.row, scope.$index)">下移
                        </el-button>
                        <el-button type="text" size="mini" :disabled="isSubmitDr"
                                   @click.stop.prevent="handleRemoveDrCardGtime(scope.row,scope.$index)">删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div class="mgb-20" style="padding-left: 15px;">
                <el-button size="mini" type="primary" :disabled="isSubmitDr  || drCardreGtimeData.length < 1"
                           @click.stop.prevent="submitDrCardreGtime">保存
                </el-button>
            </div>
    </div>


</div>

<script>
    +function ($, Vue) {
        var app = new Vue({
            el: '#app',
            mixins: [paginationMixin],
            data: function () {
                var isEnter = '${drCardRule.isEnter}';
                var isOut = '${drCardRule.isOut}';
                isEnter = isEnter === '1';
                isOut = isOut === '1';
                return {
                    ruleForm: {
                        isEnter: isEnter,
                        enterTime: parseInt('${drCardRule.enterTime}')||0,
                        isOut: isOut,
                        beginTime: '${drCardRule.beginTime}',
                        endTime: '${drCardRule.endTime}'
                    },
                    rules: {
                        enterTime: [{
                            validator: function (rule, value, callback) {
                                if (!value) {
                                    return callback(new Error('请输入天数'));
                                }
                                if (!(/^\+?[1-9][0-9]*$/.test(value))) {
                                    callback(new Error('请输入数字值'));
                                } else {
                                    if (value > 99 || value < 0) {
                                        callback(new Error('必须在1-99天'));
                                    } else {
                                        callback();
                                    }
                                }
                            }, trigger: 'blur'
                        }],
                        beginTime: [{required: true, message: '请选择开始时间', trigger: 'change'}],
                        endTime: [{required: true, message: '请选择结束时间', trigger: 'change'}]
                    },
                    preWarningTimeForm: {
                        drCreGtimes: [{
                            beginDate: '',
                            endDate: '',
                            rangeDate: [],
                            sort: '',
                            status: '0',
                            value: '',
                            id: '',
                            isTimeLimit: '',
                        }]
                    },
                    drCardRuleId: '${drCardRule.id}',
                    drCardreGtimeData: [],
                    isSubmitDr: false,
                    groupId: '${uuid}',
                    uuids: [],
                    ruleFormDisabled: false
                }
            },
            watch: {
                'uuids': {
                    deep: true,
                    handler: function (value) {
                        if (value.length < 10) {
                            this.getUUIds(10);
                        }
                    }
                }
            },
            methods: {
                submitForm: function (formName) {
                    var self = this;
                    this.$refs[formName].validate(function (valid) {
                        if (valid) {
                            self.ruleFormDisabled = true;
                            self.$refs[formName].$el.submit();
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    })
                },
                addDrCreGtime: function (formName) {
                    var self = this;
                    this.$refs[formName].validate(function (valid) {
                        if (valid) {
                            var drCreGtimes = self.preWarningTimeForm.drCreGtimes[0];
                            var rangeDate = drCreGtimes.rangeDate;
                            self.drCardreGtimeData.push({
                                id: self.uuids.splice(0, 1)[0],
                                group: {id: self.groupId},
                                beginDate: rangeDate[0] + " 00:00:00",
                                endDate: rangeDate[1] + " 23:59:59",
                                status: '1'
                            });
                            self.$refs[formName].resetFields();
                        } else {
                            console.log('error submit!!');
                            return false;
                        }
                    })
                },
                getDrCreGtimes: function () {
                	var self = this;
                    var drCreGtimesXhr = this.$axios.post('/dr/drCardreGtime/ajaxGtimesByGid/' + this.groupId);
                    drCreGtimesXhr.then(function (response) {
                    	var data = response.data
                       	if(data.status){
                       	 self.drCardreGtimeData =(data.datas ? data.datas.drCreGtimes : []) || []
                       	}
                    })
                    drCreGtimesXhr.catch(function (error) {

                    })

                },

                handleRemoveDrCardGtime: function (row, index) {
                    var id = row.id;
                    var removeXhr, self = this;
                    if (!id) {
                        this.drCardreGtimeData.splice(index, 1);
                        return false;
                    }
                    removeXhr = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardreGtime/ajaxDelete',
                        params: {ids: id},
                        headers: {
                            'Content-Type': 'application/json;charset=utf-8'
                        }
                    });
                    removeXhr.then(function (response) {
                        var data = response.data;
                        if(data.status){
                        	self.drCardreGtimeData.splice(index, 1)
                        }
                        self.show$message(data);

                    });
                    removeXhr.catch(function (error) {
                        self.show$message({}, error.response.data);
                    })
                },
                submitDrCardreGtime: function () {
                    var submitDrCardXhr, self = this;
                    this.drCardreGtimeData.forEach(function (item, index) {
                        item.sort = (index + 1) * 10
                    });

                    this.isSubmitDr = true;
                    submitDrCardXhr = this.$axios({
                        method: 'POST',
                        url: '/dr/drCardreGtime/ajaxSavePl',
                        headers: {
                            'Content-Type': 'application/json;charset=UTF-8'
                        },
                        data: JSON.stringify({
                            id: this.groupId,
                            drCreGtimes: this.drCardreGtimeData
                        })
                    });
                    submitDrCardXhr.then(function (response) {
                        var data = response.data;
                        self.show$message(data);
                        self.isSubmitDr = false;
                    });

                    submitDrCardXhr.catch(function (error) {
                        self.isSubmitDr = false;
                        self.show$message({}, error.response.data);
                    });

                },
                upDrCardItem: function (row, index) {
                    var item = this.drCardreGtimeData.splice(index, 1);
                    this.drCardreGtimeData.splice(index - 1, 0, item[0]);
                },
                downDrCardItem: function (row, index) {
                    var item = this.drCardreGtimeData.splice(index, 1);
                    this.drCardreGtimeData.splice(index + 1, 0, item[0]);
                },
                getUUIds: function (num) {
                    var self = this;
                    return this.$axios.get('/sys/uuids/' + num).then(function (respons) {
                        var data = respons.data;
                        if (data.status) {
                            self.uuids = self.uuids.concat(JSON.parse(data.id));
                        }
                    });
                }
            },
            created: function () {
                this.getDrCreGtimes();
                this.getUUIds(10);
            },
            mounted: function () {
                if(this.formSubmitMessage){
                    this.show$message({status: this.formSubmitMessage.indexOf('成功') > -1 , msg: this.formSubmitMessage})
                }
            }
        })
    }(jQuery, Vue);
</script>

</body>
</html>