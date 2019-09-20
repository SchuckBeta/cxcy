/**
 * Created by Administrator on 2018/6/14.
 */

'use strict';

Vue.mixin({
    data: function () {
        return {
            responseCodes: {
                '1001': 'Token失效',
                '1002': '参数有误',
                '1003': '内部错误'
            }
        }
    },
    methods: {
        getEntries: function (data, defaultProps) {
            var i = 0, entries = {};
            defaultProps = defaultProps || {value: 'value', label: 'label'};
            if (!data || data.length < 1) {
                return null;
            }
            while (i < data.length) {
                entries[data[i][defaultProps.value]] = data[i][defaultProps.label];
                i++
            }
            return entries;
        },

        checkUserLogin: function (data) {
            try {
                var isChecked = data.indexOf("id=\"imFrontLoginPage\"") > -1;
                if (isChecked) {
                    this.$confirm('未登录或登录超时。请重新登录，谢谢', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(function () {
                        location.reload();
                    })
                }
            } catch (e) {
                isChecked = false;
            }

            return isChecked;
        },


        addFtpHttp: function (url) {
            if (!url) {
                return ''
            }
            return this.ftpHttp+url;
        },

        getUserIsCompleted: function () {
            return this.$axios.get('/cms/ajaxCheckUser')
        },

        //检测项目初始， 用于自定义项目申报
        checkProProjectIsValid: function (url,params) {
            var self = this;
            return new Promise(function (resolve, reject) {
                self.$axios.get(url, {params: params}).then(function (response) {
                    var data = response.data;
                    if (data.ret === 1) {
                        resolve({status: 'success', message: data.msg});
                    }
                    if (data.ret === 0) {
                        reject({status: 'error', code: '0', message: data.msg})
                    }
                    if (data.ret === 2) {
                        //有按钮   返回首页，  进入项目列表
                        reject({status: 'error', code: '2', message: data.msg, btns: data.btns})
                    }
                }).catch(function (error) {
                    reject({status: 'error', code: '3', message: self.xhrErrorMsg, error: error});
                })
            })
        },


        checkResponseCode: function (code, msg, showMessage) {
            var self = this;
            if (typeof showMessage === 'undefined') {
                showMessage = true;
            }
            if (code in this.responseCodes && showMessage) {
                this.$alert(msg || this.responseCodes[code], '提示', {
                    confirmButtonText: '确定',
                    type: 'error',
                    showClose: code !== '1001'
                }).then(function () {
                    if (code === '1001') {
                        location.href = self.frontOrAdmin;
                    }
                });
                return false;
            } else {
                if (code == 1000) {
                    return true;
                }
            }
            return true;
        },

        isResponseSuccess: function (code) {
            return code == 1000
        },

        setSearchListFormPagination: function () {

        },

        treeToList: function (data, expandLevel) {
            data = JSON.parse(JSON.stringify(data));
            expandLevel = expandLevel || 0;
            function flatten(data) {
                return data.reduce(function (p1, p2) {
                    var children = p2.children || [];
                    var parentIds = p2.parentIds;
                    parentIds = parentIds.replace(/\,$/, '');
                    parentIds = parentIds.split(',');
                    parentIds = parentIds.slice(0);
                    Vue.set(p2, 'dots', parentIds.join('-'))
                    Vue.set(p2, 'isCollapsed', parentIds.length > expandLevel + 1);
                    Vue.set(p2, 'isExpand', parentIds.length <= expandLevel);
                    return p1.concat(p2, flatten(children))
                }, [])
            }

            return flatten(data);
        },

        listToTreeEntity: function (data) {
            var entity = {};
            var tree = [];
            for(var i = 0; i < data.length; i++){
                entity[data[i].id] = data[i];
            }
            for(var j = 0; j < data.length; j++){
                var item = data[j];
                var parentId = item.parentId;
                if(entity[parentId]){
                    if(!entity[parentId].children){
                        entity[parentId].children = [];
                    }
                    entity[parentId].children.push(item);
                }else {
                    tree.push(item);
                }
            }
            return {
                entity: entity,
                tree: tree
            }
        },
        //浅复制数据
        assignFormData: function (formData, data) {
            for (var k in formData) {
                if (formData.hasOwnProperty(k)) {
                    if (typeof data[k] === 'undefined') {
                        continue;
                    }
                    if ({}.toString.call(data[k]) === '[object Object]') {
                        this.assignFormData(formData[k], data[k])
                    } else {
                        formData[k] = data[k]
                    }
                }
            }
            return formData;
        },

        //单独处理每一个result集合的id数据

        //处理左侧数据
        changeSideTodoNum: function (actywId) {
            if(!actywId){
                return;
            }
            this.$axios.get('/sys/menu/totoReflash/'+actywId).then(function (response) {
                var data = response.data;
                var result = data.result;
                if(data.status){
                    if(result && {}.toString.call(result) === '[object Object]'){
                        window.parent.sideNavModule && window.parent.sideNavModule.changeMenuItemsTodoNum(actywId, result);
                    }else {
                        window.parent.sideNavModule &&  window.parent.sideNavModule.emptyMenuItemsTodoNum(actywId);
                    }
                }else {

                }
            })
        },

        //批量获取dictList, key & params
        getBatchDictList: function (dicts) {
            var self = this;
            for(var k in dicts){
                if(dicts.hasOwnProperty(k)){
                    var url = '/sys/dict/getDictList?type='+ k;
                    getDictList(url, k);
                }
            }
            function getDictList(url, k) {
                self.$axios.get(url).then(function (response) {
                    if(response.data && {}.toString.call(response.data) === '[object Array]'){
                        self[dicts[k]] = response.data || []
                    }
                })
            }
        },

        getEntity: function (list, props) {
            props = props || {label: 'label', value:"value"};
            list = list || [];
            var label = props['label'];
            var value = props['value'];
            var entity = {};
            for (var i = 0; i < list.length; i++){
                var item = list[i];
                entity[item[value]] = item;
            }
            return entity;
        }
    }
})
