+function ($, Vue) {
    var eCheckboxGroup = Vue.component('e-checkbox-group', {
        template: '<div class="e-checkbox-group" role="group" aria-label="checkbox-group"><slot></slot></div>',
        componentName: 'ECheckboxGroup',
        props: {
            disabled: Boolean,
            value: {}
        },
        watch: {
            value: function (value) {
            }
        },
    })
}(jQuery, Vue)