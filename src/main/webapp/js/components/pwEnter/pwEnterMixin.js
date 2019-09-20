
Vue.pwEnterMixin = {
    data: function () {
        return {
            officeList: [],
            pwEnterTypes: [],
            cityData: cityData,
            regMtypes: [],
        }
    },
    computed: {
        officeEntries: function () {
            return this.getEntries(this.officeList, {
                label: 'name',
                value: 'id'
            })
        },

        pwEnterTypeEntries: function () {
            return this.getEntries(this.pwEnterTypes)
        },

        cityEntries: function () {
            return this.getEntries(this.cityData, {
                label: 'shortName',
                value: 'id'
            })
        },

        regMTypeEntries: function () {
            return this.getEntries(this.regMtypes)
        }
    },
    methods: {
        getOfficeList: function () {
            var self = this;
            this.$axios.get('/sys/office/getOrganizationList').then(function (response) {
                self.officeList = response.data || [];
            })
        },

        getPwEnterTypes: function () {
            var self = this;
            this.$axios.get('/pw/pwEnter/getPwEnterTypes').then(function (response) {
                var data = response.data;
               self.pwEnterTypes = data || [];
            })
        },

        getRegMtypes: function () {
            var self = this;
            this.$axios.get('/sys/dict/getDictList?type=pw_reg_mtype').then(function (response) {
                self.regMtypes = response.data || [];
            })
        },
    },
    beforeMount: function () {
        this.getOfficeList();
        this.getPwEnterTypes();
        this.getRegMtypes();
    }
}