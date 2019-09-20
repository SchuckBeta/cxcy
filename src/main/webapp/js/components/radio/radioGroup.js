+function ($, Vue) {
    var eRadioGroup = Vue.component('e-radio-group', {
        template: '<div class="e-radio-group" role="group" aria-label="radio-group"><slot></slot></div>',
        componentName: 'ERadioGroup',
        props: {
            disabled: Boolean,
            value: {}
        },
        watch: {
            value: function (value) {
            }
        }
    })
}(jQuery, Vue)