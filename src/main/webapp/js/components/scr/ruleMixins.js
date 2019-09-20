Vue.creditRatioMixin = {
    data: function () {
        return {
            cRatioValReg: /^([1-9]{1}\:){1,10}[1-9]$/ //学分配置规则
        }
    },
    methods: {
        validateRatioValReg: function (rules, value, callback) {
            var regStr = "\^\(\(\[1-9\]\{1\}\\d\{0,2\}\)\{1\}\\:\)\{n\}\[1-9\]\{1\}\\d\{0,2\}\$"; // toString /^(([1-9]{1}\d*){1}\:){1}[1-9]$/
            var fullField = rules.fullField;
            var index = fullField.match(/\d+(?=\.)/)[0];
            var num = this.creditRatioForm.scoRulePbs[index].num;
            var regObj;
            regStr = regStr.replace('n', parseInt(num) - 1);
            regObj = new RegExp(regStr);
            if (value) {
                if (value.indexOf('：') > -1) {
                    return callback(new Error('请输入英文冒号'))
                }
                if (regObj.test(value)) {
                    return callback();
                }
                return callback(new Error('请输入类似n:n:n的规则，n>0'))
            }
            return callback();
        }
    }
}

Vue.creditDetailMixin = {
    data: function () {
        return {
            scoreReg: /^(([1-9]{1}\d{0,10}|0)(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,7})\d{0,3}$/,
        }
    },
    computed: {
        validateScoreRules: function () {
            var validateScore = this.validateScore;
            return [
                {required: true, message: '请输入分值', trigger: 'blur'},
                {validator: validateScore, trigger: 'blur'}
            ]
        }
    },
    methods: {
        validateTypeName: function (rules, value, callback) {
            var self = this;
            if (value) {
                var creditTypeForm = this.creditTypeForm;
                var params = {
                    id: creditTypeForm.id,
                    name: creditTypeForm.name,
                    type: creditTypeForm.type
                }
                return this.$axios.get('/scr/scoRule/ajaxValiScrRuleName?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        return callback();
                    }
                    return callback(new Error(data.msg))
                }).catch(function () {
                    return callback(new Error(self.xhrErrorMsg))
                })
            }
            return callback();
        },

        validateRuleDetailName: function (rules, value, callback) {
            var self = this;
            if (value) {
                var creditDetailForm = this.creditDetailForm;
                var params = {
                    id: creditDetailForm.id,
                    name: creditDetailForm.name,
                    'rule.id': creditDetailForm.rule.id
                };
                return this.$axios.get('/scr/scoRuleDetail/ajaxValiScrRuleDetailName?' + Object.toURLSearchParams(params)).then(function (response) {
                    var data = response.data;
                    if (data.status === 1) {
                        return callback();
                    }
                    return callback(new Error(data.msg))
                }).catch(function () {
                    return callback(new Error(self.xhrErrorMsg))
                })
            }
            return callback();
        },

        validateScore: function (rules, value, callback) {
            if (typeof value !== 'undefined' && value !== '') {
                var valStr = value;
                if (typeof value === 'number') {
                    valStr = value.toString();
                }
                if (this.scoreReg.test(valStr)) {
                    if (parseFloat(value) > 0) {
                        return callback();
                    }
                    return callback(new Error('不能为0'));
                }
                return callback(new Error('最大可输入11位正整数可带三位小数'))
            }
            return callback();
        },

        validateScoreCouldZero: function (rules, value, callback) {
            if (typeof value !== 'undefined' && value !== '') {
                var valStr = value;
                if (typeof value === 'number') {
                    valStr = value.toString();
                }
                if (this.scoreReg.test(valStr)) {
                    return callback();
                }
                return callback(new Error('最大可输入11位正整数可带三位小数'))
            }
            return callback();
        },

        validateLowScoMax: function (rules, value, callback) {
            var creditDetailForm = this.creditDetailForm;
            if (typeof value !== 'undefined' && value !== '') {
                var lowSco = parseFloat(creditDetailForm.lowSco) || 0;
                if (lowSco >= parseFloat(value)) {
                    return callback(new Error("分值需大于" + lowSco))
                }
                return callback();
            }
            return callback();
        },

        validateLowScoMaxType: function (rules, value, callback) {
            var creditTypeForm = this.creditTypeForm;
            if (typeof value !== 'undefined' && value !== '') {
                var lowSco = parseFloat(creditTypeForm.scoRuleDetailMould.lowSco) || 0;
                if (lowSco >= parseFloat(value)) {
                    return callback(new Error("分值需大于" + lowSco))
                }
                return callback();
            }
            return callback();
        },

        validateSnumVal: function (rules, value, callback) {
            var creditRuleForm = this.creditRuleForm;
            if (typeof value !== 'undefined' && value !== '') {
                var snumMin = parseFloat(creditRuleForm.snumMin) || 0;
                if (snumMin >= parseFloat(value)) {
                    return callback(new Error("分值需大于" + snumMin))
                }
                return callback();
            }
            return callback();
        }
    }
}


Vue.creditApplyMixin = {
    methods: {
        validteRdetailIsSta: function (rules, value, callback) {
            var creditApplyForm = this.creditApplyForm;
            var parentIdArr = creditApplyForm.rdetail.parentIdArr;
            var self = this;
            if (parentIdArr.length > 0) {
                var user = creditApplyForm.user;
                var id = parentIdArr[parentIdArr.length - 1];
                var rdetail = this.getRdetail(id);
                if (rdetail) {
                    var params = {
                        user: user,
                        apply: {
                            id: creditApplyForm.id,
                            rdetail: {
                                id: rdetail.id,
                                maxOrSum: rdetail.maxOrSum,
                                isSpecial: rdetail.isSpecial
                            },
                            name: creditApplyForm.name
                        },
                        scoRapplyCert: {
                            id: creditApplyForm.scoRapplyCert.id
                        }
                    };
                    return this.$axios.post('/scr/ajaxValidScoRapply', params).then(function (response) {
                        var data = response.data;
                        if (data.status === 1) {
                            return callback();
                        }
                        return callback(new Error(data.msg))
                    }).catch(function () {
                        return callback(new Error(self.xhrErrorMsg));
                    })
                }
                return callback(new Error('请选择认定内容及标准'))
            }
            return callback();
        }
    }
}