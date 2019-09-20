/**
 * Created by Administrator on 2018/6/14.
 */



;+function (window) {
    'use strict';
    window.globalUtils = {

        lowerCaseCamel: function (str, isMultiple) {
            //noinspection JSValidateTypes
            if(typeof str == undefined){
                 throw '参数为空';
            }
            return str.replace(new RegExp('[A-Z]+', isMultiple ? 'g' : ''), function ($1) {
                return $1.toLowerCase()
            })
        },
        getQueryString: function (name) {
            var reg = new RegExp('(^|&)' + name + '=([^&]*)(&|$)', 'i');
            var r = window.location.search.substr(1).match(reg);
            if (r != null) {
                return decodeURI(r[2]);
            }
            return null;
        },



    }
}(window);