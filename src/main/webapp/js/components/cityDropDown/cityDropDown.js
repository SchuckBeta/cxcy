/**
 * Created by Administrator on 2018/6/14.
 */

;+function (Vue) {
    'use strict';
    var cityDropdown = Vue.component('city-dropdown', {
        template: '<div class="city-input-group" :class="{open: focusing && cityPaths.length > 0}">\n' +
        '        <input name="countryName" v-model="cityName" autocomplete="off" :disabled="disabled"\n' +
        '               type="text"\n' +
        '               v-blur\n' +
        '               @keydown.down="handlerDown"\n' +
        '               @keydown.up="handlerUp"\n' +
        '               @keydown.enter="handlerEnter"\n' +
        '               ref="countryName"\n' +
        '               :placeholder="placeholder"\n' +
        '               :class="className" @change="handlerChangeName" @focus="handlerFocus" @blur="handlerBlur">\n' +
        '        <el-input type="hidden" :value="countryId" :name="name" style="display: none;"></el-input>\n' +
        '        <div class="city-input-group-btn">\n' +
        '            <slot name="rightSelected"></slot>\n' +
        '        </div>\n' +
        '        <div class="city-dropdown" :class="{open: cityPaths.length > 0 && focusing}">\n' +
        '            <ul class="city-dropdown-ul">\n' +
        '                <li v-for="(cp, index) in cityPaths" :class="{active: index == selectedIndex}"><a\n' +
        '                        href="javascript:void(0);"\n' +
        '                        @click.stop="selectedCity(cp)">{{cp.paths.join(\',\')}}</a></li>\n' +
        '            </ul>\n' +
        '        </div>\n' +
        '    </div>',
        model: {
            prop: 'countryId',
            event: 'change'
        },
        props: {
            cityData: {},
            visible: Boolean,
            countryId: String,
            name: String,
            disabled: Boolean,
            className: [String, Object],
            placeholder: String
        },
        data: function () {
            return {
                cityPaths: [],
                cityName: '',
                focusing: false,
                selectedIndex: -1,
                cityIds: [],
                isSelected: false,
                clickInput: true
            }
        },
        watch: {
            countryId: function () {
                this.cityName = this.getCityName();
            },
            cityName: function (val) {
                if (this.focusing) {
                    this.clickInput = false;
                    this.cityIds = this.searchNames(val);
                    if (this.cityIds.length < 1) {
                        this.selectedIndex = -1;
                    }
                    this.getPath(this.cityIds);
                }
            },
            focusing: function (value) {
                if (this.clickInput) {
                    return
                }
                this.$emit('change', '');
                this.cityName = '';
            }
        },
        directives: {
            blur: {
                inserted: function (element, binding, vnode) {
                    $(document).on('click.city.input', function (event) {
                        event.stopPropagation();
                        if (event && event.type === 'click' && /input|textarea/i.test(event.target.tagName)) return;
                        if ($(event.target).parents('.city-dropdown').hasClass('open')) {
                            return
                        }
                        vnode.context.focusing = false;
                        vnode.context.selectedIndex = -1;
                    })
                }
            }
        },
        methods: {
            searchNames: function (value) {
                var regVal = new RegExp(value.replace('中国', ''));
                var cityData = this.cityData;
                var cityIds = [];
                if (!value) {
                    return [];
                }
                for (var k in cityData) {
                    var city, shortName;
                    if (!cityData.hasOwnProperty(k)) {
                        continue;
                    }
                    city = cityData[k];
                    shortName = city.shortName;
                    if (regVal.test(shortName)) {
                        cityIds.push(k);
                    }
                }
                return cityIds;
            },
            getPath: function (cityIds) {
                var cityPaths = [];
                if (cityIds.length < 1) {
                    this.cityPaths = [];
                    return;
                }
                if(!this.cityData[this.countryId]){
                    return;
                }
                for (var i = 0; i < cityIds.length; i++) {
                    var id = cityIds[i];
                    var city = this.cityData[id];
                    var parent = this.cityData[city.parentId];
                    var path = [];
                    path.push(city.name);
                    while (parent) {
                        var parentId = parent.parentId;
                        path.push(parent.name);
                        parent = this.cityData[parentId]
                    }
                    cityPaths.push({
                        id: id,
                        paths: path
                    })
                }
                this.cityPaths = cityPaths;
            },
            getCityName: function () {
                if(!this.countryId){
                    return '';
                }
                if(!this.cityData[this.countryId]){
                    return this.countryId;
                }
                return this.cityData[this.countryId].name
            },

            handlerFocus: function () {
                this.focusing = true;
                this.countryId && this.getPath([this.countryId]);
            },

            handlerChangeName: function () {

            },

            handlerDown: function () {
                this.selectedIndex++;
                if (this.selectedIndex >= this.cityIds.length) {
                    this.selectedIndex = 0
                }
            },
            handlerUp: function () {
                this.selectedIndex--;
                if (this.selectedIndex < 0) {
                    this.selectedIndex = this.cityIds.length - 1
                }
            },
            handlerEnter: function () {
                if (this.selectedIndex > -1) {
                    this.clickInput = true;
                    this.$emit('change', this.cityIds[this.selectedIndex]);
                    this.selectedIndex = -1;
                    this.$refs.countryName.blur();
                    this.focusing = false;
                    this.$nextTick(function () {
                        this.cityName = this.getCityName();
                    })

                }
            },
            selectedCity: function (cp) {
                this.clickInput = true;
                this.$emit('change', cp.id);
                this.selectedIndex = -1;
                this.$refs.countryName.blur();
                this.focusing = false;
                this.$nextTick(function () {
                    this.cityName = this.getCityName();
                })

            },
            handlerBlur: function () {
            }
        },
        beforeMount: function () {
            this.cityName = this.getCityName();
        },
        mounted: function () {

        }
    })
}(Vue)