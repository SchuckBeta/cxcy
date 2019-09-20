Vue.pwApplyRules = {
    data: function () {
        return {
            numberReg: /^\d+$/,
            specificSymbol: /[@#\$%\^&\*\s]+/g
        }
    },
    methods: {
        validateEnterpriseName: function (rule, value, callback) {
            if (value) {
                if (this.specificSymbol.test(value)) {
                    return callback(new Error("请不要输入特殊符号"))
                }
                var pwCompany = this.enterpriseForm;
                var params = {
                    id: this.pwEnterId,
                    pwCompany: {
                        id: pwCompany.id,
                        name: pwCompany.name,
                    }
                };
                return this.$axios.post('/pw/pwEnter/checkPwEnterCompanyName', params).then(function (response) {
                    if (response.data) {
                        return callback();
                    }
                    return callback(new Error("企业名称已经存在"));
                }).catch(function (error) {
                    return callback(new Error("请求失败"));
                })
            }
            return callback();
        },
        validateEnterpriseNo: function (rule, value, callback) {
            if (value) {
                if (this.specificSymbol.test(value)) {
                    return callback(new Error("请不要输入特殊符号"))
                }
                var pwCompany = this.enterpriseForm;
                var params = {
                    id: this.pwEnterId,
                    pwCompany: {
                        id: pwCompany.id,
                        no: pwCompany.no
                    }
                };
                return this.$axios.post('/pw/pwEnter/checkPwEnterCompanyNo', params).then(function (response) {
                    if (response.data) {
                        return callback();
                    }
                    return callback(new Error("工商注册号已经存在"));
                }).catch(function (error) {
                    return callback(new Error("请求失败"));
                })
            }
            return callback();
        },
        validateRegMoney: function (rule, value, callback) {
            if (value) {
                if (/^(([1-9]{1}\d{0,5}|0)(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,5})\d{0,2}$/.test(value)) {
                    if(value === '0.0' || value === '0.00'){
                        return callback(new Error("注册资金不能为0"))
                    }
                    return callback();
                }
                return callback(new Error("请输入小数位不超过2位的正数注册资金"))
            }

        },
    }
}