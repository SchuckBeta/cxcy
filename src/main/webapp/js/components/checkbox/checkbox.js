+function ($, Vue) {
    var eCheckbox = Vue.component('e-checkbox', {
        template: '<div class="e-checkbox-inline_block"><label class="e-checkbox" role="checkbox" :class="[customClass, {\'is-checked\': isChecked }]">\n' +
        '        <span class="e-checkbox_input"><input\n' +
        '                type="checkbox"\n' +
        '                :name="name"\n' +
        '                :disabled="isDisabled"\n' +
        '                :value="label"\n' +
        '                aria-hidden="true"\n' +
        '                v-model="model"\n' +
        '                class="e-checkbox_original"\n' +
        '                @change="handleChange"\n' +
        '                @focus="focus=true"\n' +
        '                @blue="focus=false"></span>\n' +
        '        <span class="e-checkbox_label" v-if="$slots.default || label">\n' +
        '           <slot></slot>\n' +
        '            <template v-if="!$slots.default">{{label}}</template>\n' +
        '        </span>\n' +
        '    </label></div>',
        computed: {
            model: {
                get: function () {
                    return this.isGroup ? this.store : this.value !== undefined ? this.value : this.selfModel;
                },
                set: function (value) {
                    if (this.isGroup) {
                        this._checkboxGroup.$emit('input', value)
                    } else {
                        this.$emit('input', value);
                        this.selfModel = value;
                    }
                }
            },
            isGroup: function () {
                var $parent = this.$parent;
                while ($parent) {
                    if ($parent.$options.componentName !== 'ECheckboxGroup') {
                        $parent = $parent.$parent;
                    } else {
                        this._checkboxGroup = $parent;
                        return true;
                    }
                }
                return false;
            },
            store: function () {
                return this._checkboxGroup ? this._checkboxGroup.value : this.value;
            },
            isDisabled: function () {
                return this.isGroup ? (this._checkboxGroup.disabled || this.disabled) : this.disabled
            },
            isChecked: function () {
                if (Array.isArray(this.model)) {
                    return this.model.indexOf(this.label) > -1;
                } else if ({}.toString.call(this.model) === '[object Boolean]') {
                    return this.model;
                } else if (this.model !== null && this.model !== undefined) {
                    return this.model = true;
                }
            },
        },
        props: {
            value: {},
            label: {},
            name: String,
            checked: Boolean,
            disabled: Boolean,
            customClass: String
        },
        data: function () {
            return {
                focus: false,
                selfModel: false
            }
        },
        methods: {
            addToStore: function () {
                if (Array.isArray(this.model) && this.model.indexOf(this.label) === -1) {
                    this.model.push(this.label);
                } else {
                    this.model = true;
                }
            },

            removeStore: function () {
                if (Array.isArray(this.model) && this.model.indexOf(this.label) > -1) {
                    this.model.splice(this.model.indexOf(this.label), 1);
                } else {
                    this.model = false;
                }
            },

            handleChange: function (ev) {
                var value;
                if (ev.target.checked) {
                    value = this.label === undefined ? true : this.label;
                    this.addToStore();
                } else {
                    value = this.label === undefined ? false : this.label;
                    this.removeStore();
                }
                this.$emit('change', value, ev, ev.target.checked);
                this.$nextTick(function () {
                    if (this.isGroup) {
                        this._checkboxGroup.$emit('change', [this._checkboxGroup.value])
                        this._checkboxGroup.$emit('old-change', value);
                    }
                })
            }
        },
        created: function () {
            this.checked && this.addToStore();
        },
        mounted: function () {
        }
    })
}(jQuery, Vue)