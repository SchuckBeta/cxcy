<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .credit-standard-block {
            position: relative;
            padding-right: 100px;
            overflow: hidden;
        }

        .credit-standard-block .credit-standard-control {
            position: absolute;
            right: 0;
            top: 0;
        }

        .credit-standard-block .credit-standard-column {
            font-size: 0;
            vertical-align: top;
        }

        .credit-standard-block .credit-standard-cell {
            display: inline-block;
            max-width: 80px;
            vertical-align: top;
        }

        .credit-standard-block .credit-standard-zhi {
            margin: 0 8px;
            font-size: 14px;
        }

        .credit-standard-block .credit-standard-cell-oth {
            display: inline-block;
            font-size: 12px;
            height: 28px;
            line-height: 28px;
            margin-bottom: 18px;
            margin-left: 8px;
            margin-right: 8px;
        }

        .standard-table td {
            padding-top: 26px;
        }
    </style>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid mgb-60">
    <div class="mgb-20">
        <edit-bar second-name="课程学分认定标准"></edit-bar>
    </div>
    <div class="table-container">
        <el-form :model="standardForm" ref="standardForm" size="mini" :disabled="disabled">
            <table class="el-table el-table--fit el-table--enable-row-hover el-table--enable-row-transition el-table--mini e-table-tree standard-table el-table--border"
                   style="table-layout: fixed; margin-bottom: 20px;"
                   cellspacing="0" cellpadding="0">
                <thead>
                <tr>
                    <th width="38%">课时</th>
                    <th>成绩及学分标准</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(item, index) in standardForm.standard">
                    <td>
                        <div class="credit-standard-block">
                            <div class="credit-standard-column">
                                <div class="credit-standard-cell">
                                    <el-form-item :prop="'standard.'+index+'.couse.start'" :rules="courseRules">
                                        <el-input v-model="item.couse.start" maxlength="3"></el-input>
                                    </el-form-item>
                                </div>
                                <span class="credit-standard-zhi">-</span>
                                <div class="credit-standard-cell">
                                    <el-form-item :prop="'standard.'+index+'.couse.end'" :rules="courseEndRules">
                                        <el-input v-model="item.couse.end" maxlength="3"></el-input>
                                    </el-form-item>
                                </div>
                                <span class="credit-standard-cell-oth">课时</span>
                            </div>
                            <div class="credit-standard-control">
                                <el-form-item>
                                    <el-button icon="el-icon-plus" @click.stop="addStandard"></el-button>
                                    <el-button v-show="standardForm.standard.length > 1"
                                               icon="el-icon-minus"
                                               @click.stop="standardForm.standard.splice(index, 1)"></el-button>
                                </el-form-item>
                            </div>
                        </div>
                    </td>
                    <td>
                        <div class="credit-standard-block" v-for="(score, index2) in item.scores">
                            <div class="credit-standard-column">
                                <div class="credit-standard-cell">
                                    <el-form-item :prop="'standard.'+index+'.scores.'+ index2 + '.start'" maxlength="15"
                                                  :rules="scoreRules">
                                        <el-input v-model="score.start"></el-input>
                                    </el-form-item>
                                </div>
                                <span class="credit-standard-zhi">-</span>
                                <div class="credit-standard-cell">
                                    <el-form-item :prop="'standard.'+index+'.scores.'+ index2 + '.end'" maxlength="15"
                                                  :rules="scoreEndRules">
                                        <el-input v-model="score.end"></el-input>
                                    </el-form-item>
                                </div>
                                <span class="credit-standard-cell-oth">认定</span>
                                <div class="credit-standard-cell">
                                    <el-form-item :prop="'standard.'+index+'.scores.'+ index2 + '.score'" maxlength="15"
                                                  :rules="scoreRules">
                                        <el-input v-model="score.score"></el-input>
                                    </el-form-item>
                                </div>
                                <span class="credit-standard-cell-oth">学分</span>
                            </div>
                            <div class="credit-standard-control">
                                <el-form-item>
                                    <el-button icon="el-icon-plus" @click.stop="addScore(item.scores)"></el-button>
                                    <el-button v-show="item.scores.length > 1" icon="el-icon-minus"
                                               @click.stop="item.scores.splice(index2, 1)"></el-button>
                                </el-form-item>
                            </div>
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
            <el-form-item class="text-center">
                <el-button type="primary" @click.stop="validateForm">保存</el-button>
                <el-button @click.stop="goToBack">返回</el-button>
            </el-form-item>
        </el-form>
    </div>
</div>
<script>

    'use strict';

    new Vue({
        el: '#app',
        mixins: [Vue.creditDetailMixin],
        data: function () {
            var standardMap = JSON.parse(JSON.stringify(${fns: toJson(map)}));
            var validateScore = this.validateScoreCouldZero;
            var self = this;
            var validateCourseTime = function (rules, value, callback) {
                if (value) {
                    if (/^[1-9]{1}\d{0,2}$|^0$/.test(value)) {
                        return callback();
                    }
                    return callback(new Error('输入正整数'))
                }
                return callback();
            };

            var validateCourseTimeEnd = function (rules, value, callback) {
                if (value) {
                    var num = rules.fullField.match(/\d+/);
                    var standard = self.standardForm.standard;
                    var start = standard[num].couse.start;
                    if (parseFloat(start) > parseFloat(value)) {
                        return callback(new Error('小于开始课时'))
                    }
                    return callback();
                }
                return callback();
            };

            var validateScoreEndRules = function (rules, value, callback) {
                if (value) {
                    var num = rules.fullField.match(/\d+/g);
                    var standard = self.standardForm.standard;
                    var start = standard[num[0]].scores[num[1]].start;
                    if (parseFloat(start) > parseFloat(value)) {
                        return callback(new Error('小于开始学分'))
                    }
                    return callback();
                }
                return callback();
            };
            return {
                standardForm: {
                    standard: []
                },
                standardMap: standardMap,
                disabled: false,
                scoreRules: [
                    {required: true, message: '请输入学分', trigger: 'blur'},
                    {validator: validateScore, trigger: 'blur'}
                ],
                scoreEndRules: [
                    {required: true, message: '请输入学分', trigger: 'blur'},
                    {validator: validateScore, trigger: 'blur'},
                    {validator: validateScoreEndRules, trigger: 'blur'}
                ],
                courseRules: [
                    {required: true, message: '请输入课时', trigger: 'blur'},
                    {validator: validateCourseTime, trigger: 'blur'}
                ],
                courseEndRules: [
                    {required: true, message: '请输入课时', trigger: 'blur'},
                    {validator: validateCourseTime, trigger: 'blur'},
                    {validator: validateCourseTimeEnd, trigger: 'blur'},
                ]
            }
        },

        methods: {
            getStandardForm: function () {
                var standardMap = JSON.parse(JSON.stringify(this.standardMap));
                var standardForm = this.standardForm;
                for (var k in standardMap) {
                    if (standardMap.hasOwnProperty(k)) {
                        standardForm.standard.push(standardMap[k])
                    }
                }
                return standardForm;
            },
            addStandard: function () {
                this.standardForm.standard.push({
                    couse: {
                        start: '',
                        end: ''
                    },
                    scores: [
                        {
                            start: '',
                            end: '',
                            score: ''
                        }
                    ]
                })
            },
            addScore: function (row) {
                row.push({
                    start: '',
                    end: '',
                    score: ''
                })
            },

            getParams: function () {
                var standard = this.standardForm.standard;
                return standard.map(function (item) {
                    var scores = [];
                    var sta = {
                        couse: {
                            start: item.couse.start,
                            end: item.couse.end
                        },
                    }
                    scores = item.scores.map(function (score) {
                        return {
                            start: score.start,
                            end: score.end,
                            score: score.score
                        }
                    })
                    sta['scores'] = scores;
                    return sta;
                })
            },

            validateForm: function () {
                var self = this;
                this.$refs.standardForm.validate(function (valid) {
                    if (valid) {
                        self.submitForm();
                    }
                })
            },

            submitForm: function () {
                var self = this;
                var dataJson = this.getParams();
                dataJson = JSON.stringify(dataJson);
                this.disabled = true;
                this.$axios({
                    method: 'POST',
                    url: '/sco/scoAffirmCriterionCouse/saveScoAffirmCriterionCouse',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded'
                    },
                    data: Object.toURLSearchParams({
                        foreignId: '${foreignId}',
                        fromPage: '${scoAffirmCriterionCouse.fromPage}',
                        dataJson: dataJson
                    }).toString()
                }).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        self.$alert('保存成功', '提示', {
                            type: 'success',
                            cancelButtonText: '取消',
                            confirmButtonText: '确定',
                            showCancelButton: false,
                            showClose: false
                        }).then(function () {
                            location.href = '/a/sco/scoCourse/list'
                        })
                    } else {
                        self.$message.error(data.msg);
                        self.disabled = false;
                    }
                })
            },
            goToBack: function () {
                history.go(-1);
            }
        },
        created: function () {
            this.getStandardForm();
        }
    })

</script>
</body>
</html>