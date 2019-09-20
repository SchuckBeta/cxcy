/**
 * Created by Administrator on 2018/7/24.
 */
'use strict';

Vue.component('col-project-node', {
    template: '<div></div>\n',
    props: {
        row: Object
    },
    methods: {
        getProjectNode: function () {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/actyw/actywAssignRule/ajaxProvOther',
                data:{
                    actywId:this.row.actywId,
                    gnodeId:this.row.gnodeId
                }
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.$emit('change', {
                        data: data.data || {},
                        row: self.row
                    });
                }
            }).catch(function (error) {
                self.$message({
                    message: self.xhrErrorMsg,
                    type: 'error'
                })
            });
        }
    },
    created: function () {
        this.getProjectNode();
    }

});



Vue.component('expert-col-current-assign', {
    template: '<div></div>\n',
    props: {
        row:Object,
        actywId:String,
        gnodeId:String
    },
    methods: {
        getProjectNode: function () {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/actyw/actywAssignRule/ajaxProvGetManuallyExpertToDoNum',
                data:{
                    actywId:this.actywId,
                    gnodeId:this.gnodeId,
                    userId:this.row.user.id
                }
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.$emit('change',{
                        data:data.data,
                        row:self.row
                    });
                }
            }).catch(function (error) {
                self.$message({
                    message: self.xhrErrorMsg,
                    type: 'error'
                })
            });
        }
    },
    created: function () {
        this.getProjectNode();
    }

});


Vue.component('expert-col-re-num', {
    template: '<div></div>\n',
    props: {
        row:Object,
        actywId:String,
        gnodeId:String
    },
    methods: {
        getProjectNode: function () {
            var self = this;
            this.$axios({
                method: 'POST',
                url: '/actyw/actywAssignRule/ajaxProvGetExpertAssginNum',
                data:{
                    actywId:this.actywId,
                    gnodeId:this.gnodeId,
                    userId:this.row.user.id
                }
            }).then(function (response) {
                var data = response.data;
                if (data.status == '1') {
                    self.$emit('change',{
                        data:data.data || {},
                        row:self.row
                    });
                }
            }).catch(function (error) {
                self.$message({
                    message: self.xhrErrorMsg,
                    type: 'error'
                })
            });
        }
    },
    created: function () {
        this.getProjectNode();
    }

});