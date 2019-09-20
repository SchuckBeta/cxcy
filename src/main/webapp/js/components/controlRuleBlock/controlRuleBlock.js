/**
 * Created by Administrator on 2018/5/17.
 */


+function ($, Vue) {
    var controlRuleBlock = Vue.component('control-rule-block',{
        template: '<div class="control-rule-block">\n' +
        '        <div class="control-rule-inner">\n' +
        '            <div class="cr-header">\n' +
        '                <slot name="checkbox"></slot>\n' +
        '                <span class="cr-title">{{title}}</span>\n' +
        '            </div>\n' +
        '            <div class="cr-body">\n' +
        '                <slot></slot>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>',
        props: {
            title: String
        }
    })

}(jQuery, Vue);