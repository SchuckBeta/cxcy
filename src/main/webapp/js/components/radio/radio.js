+function ($, Vue) {
    var eRadio = Vue.component('e-radio', {
        template: '<div class="e-radio-inline_block"><label class="e-radio" role="radio" :class="[customClass, {\'is-checked\': model===label}, {\'e-check-gray\': isGray}]">\n' +
        '        <span class="e-radio_input">\n' +
        '            <input type="radio"\n' +
        '                   :name="name"\n' +
        '                   :disabled="isDisabled"\n' +
        '                   :value="label"\n' +
        '                   class="e-radio_original"\n' +
        '                   v-model="model"\n' +
        '                   @change="handleChange"\n' +
        '                   @focus="focus=true"\n' +
        '                   @blue="focus=false"\n' +
        '            >\n' +
        '        </span>\n' +
        '        <span class="e-radio_label" v-if="$slots.default || label">\n' +
        '            <slot></slot>\n' +
        '            <template v-if="!$slots.default">{{label}}</template>\n' +
        '        </span>\n' +
        '    </label></div>',
        computed: {
            model: {
                get: function () {
                    return this.isGroup ? this._radioGroup.value : this.value;
                },
                set: function (value) {
                    if (this.isGroup) {
                        this._radioGroup.$emit('input', value)
                    } else {
                        this.$emit('input', value);
                    }
                }
            },
            isGroup: function () {
                var $parent = this.$parent;
                while ($parent) {
                    if ($parent.$options.componentName !== 'ERadioGroup') {
                        $parent = $parent.$parent;
                    } else {
                        this._radioGroup = $parent;
                        return true;
                    }
                }
                return false;
            },
            isDisabled: function () {
                return this.isGroup ? (this._radioGroup.disabled || this.disabled) : this.disabled;
            }
        },
        props: {
            value: {},
            label: {},
            customClass: String,
            isGray: Boolean,
            disabled: Boolean,
            name: String,
            checked: String
        },
        data: function () {
            return {
                focus: false
            }
        },
        methods: {
            handleChange: function () {
                this.$nextTick(function () {
                    this.$emit('change', this.model);
                    this.isGroup && this._radioGroup.$emit('change', this.model)
                })
            }
        }
    })
}(jQuery, Vue)