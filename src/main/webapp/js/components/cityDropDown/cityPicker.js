/**
 * Created by Administrator on 2018/6/14.
 */

;+function (Vue) {
    'use strict';
    var cityPicker = Vue.component('city-picker', {
        template: '<div class="city-picker">\n' +
        '        <div class="city-picker_title"></div>\n' +
        '        <div class="city-picker-content">\n' +
        '            <ul class="city-picker-tabs">\n' +
        '                <li v-for="tab in tabs" class="city-picker-tab_item" :class="{active: tabActiveKey === tab.key}">\n' +
        '                    <a href="javascript:void(0);" @click.stop="showTabContent(tab, $event)">{{tab.name}}</a>\n' +
        '                </li>\n' +
        '            </ul>\n' +
        '            <div ref="cityPickerTabContents" class="city-picker-tab_contents">\n' +
        '                <div v-for="(tabContent, key) in tabContents" class="pp-tab_content"\n' +
        '                     :class="{show: tabActiveKey === key}">\n' +
        '                    <ul class="city-list">\n' +
        '                        <li class="city-item" v-for="(city, index) in tabContent">\n' +
        '                            <div class="city-name-triangle">\n' +
        '                                <a class="city-name" href="javascript:void (0);" :class="{active: city.id === cityId}"\n' +
        '                                   @click.stop="selected(city,key, $event)">{{key === \'allProvinces\' ? city.name :\n' +
        '                                    city.shortName}}\n' +
        '                                </a>\n' +
        '                                <span class="triangle" v-show="capitalShow && capitalId === city.id"\n' +
        '                                      style="display: none"></span>\n' +
        '                            </div>\n' +
        '                            <div v-show="capitalShow && capitalId === city.id" class="city-item-areas"\n' +
        '                                 :style="{width: capitalStyle.width, marginLeft: capitalStyle.marginLeft}">\n' +
        '                                <div class="city-item-box">\n' +
        '                                    <a v-for="area in capitalCities[city.id]" class="city-name"\n' +
        '                                       href="javascript:void (0);"\n' +
        '                                       :class="{active: area.id === cityId}"\n' +
        '                                       @click.stop="handleChangeArea(area, $event)">{{area.name}}</a>\n' +
        '                                </div>\n' +
        '                            </div>\n' +
        '                        </li>\n' +
        '                    </ul>\n' +
        '                </div>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>',
        computed: {
            //省
            provinces: function () {
                return this.cityData.filter(function (t) {
                    return t.parentId === '100000'
                })
            },
            //城市
            cities: function () {
                var cities = [];
                var provinces = this.provinces;
                var cityData = this.cityData;
                for (var i = 0; i < provinces.length; i++) {
                    for (var c = 0; c < cityData.length; c++) {
                        if (cityData[c].parentId === provinces[i].id) {
                            cities.push(cityData[c])
                        }
                    }
                }
                return cities;
            },

            rowTabs: function () {
                var rowTabs = {};
                var cities = this.cities;
                for (var p = 0; p < cities.length; p++) {
                    var city = cities[p];
                    if (!rowTabs[city.letter]) {
                        rowTabs[city.letter] = []
                    }
                    if (!city) {
                        continue;
                    }
                    rowTabs[city.letter].push(city);
                }
                return rowTabs;
            },

            tabs: function () {
                if (this.hasAllProvinces) {
                    this.dTabs.push({
                        key: 'allProvinces',
                        name: '所有省份(含港澳台)'
                    })
                }
                if (this.hasOutCountry) {
                    this.dTabs.push({
                        key: 'outCountry',
                        name: '国外'
                    })
                }
                this.dTabs.unshift({
                    key: 'hotCities',
                    name: '热门城市'
                });
                return this.dTabs;
            },

            hotCitiesTabContent: function () {
                var hotCities = [];
                for (var h = 0; h < this.hotCities.length; h++) {
                    var hotCity = this.hotCities[h];
                    for (var c = 0; c < this.cities.length; c++) {
                        var city = this.cities[c];
                        if (city.name.indexOf(hotCity) > -1) {
                            hotCities.push(city);
                            city.hasArea = true;
                            break;
                        }
                    }
                }
                return hotCities;
            },

            outCountryTabContent: function () {
                return this.cityData.filter(function (t) {
                    return t.id === '0000000'
                })
            },
        },
        model: {
            prop: 'cityId',
            event: 'change'
        },
        props: {
            cityId: String,
            cityData: {},
            hotCities: {
                type: Array,
                default: function () {
                    return ['北京', '上海', '广州', '深圳', '武汉', '西安', '杭州', '南京', '成都', '重庆', '东莞', '大连', '沈阳', '苏州', '昆明', '长沙', '合肥', '宁波', '郑州', '天津', '青岛', '济南', '哈尔滨', '长春', '福州']
                }
            },
            whole: {
                type: Boolean,
                default: true
            },
            hasAllProvinces: {
                type: Boolean,
                default: true
            },
            hasOutCountry: {
                type: Boolean,
                default: true
            },
            tabActiveKey: {
                type: String,
                default: 'hotCities'
            }
        },
        data: function () {
            return {
                dTabs: [{key: 'ABC', name: 'ABC'},
                    {key: 'DEFG', name: 'DEFG'},
                    {key: 'HI', name: 'HI'},
                    {key: 'JK', name: 'JK'},
                    {key: 'LMN', name: 'LMN'},
                    {key: 'OPQR', name: 'OPQR'},
                    {key: 'STU', name: 'STU'},
                    {key: 'VWX', name: 'VWX'},
                    {key: 'YZ', name: 'YZ'}],
                capitalCities: {},
                capitalShow: false,
                capitalId: '',
                tabContents: {},
                capitalStyle: {
                    width: '',
                    marginLeft: ''
                },
                selectedId: '',
                newCityData: []
            }
        },
        watch: {
            tabActiveKey: function () {
                this.capitalShow = false;
            }
        },
        methods: {
            setDefaultTabContent: function () {
                this.tabContents['hotCities'] = this.hotCitiesTabContent;
            },

            showTabContent: function (tab) {
                var tabKey = tab.key;
                if (!this.tabContents[tab.key]) {
                    if (tab.key === 'allProvinces') {
                        this.tabContents[tab.key] = this.provinces;
                    } else if (tab.key === 'outCountry') {
                        this.tabContents[tab.key] = this.outCountryTabContent;
                    } else {
                        for (var k = 0; k < tabKey.length; k++) {
                            if (!this.tabContents[tabKey]) {
                                this.tabContents[tabKey] = [];
                            }
                            if (this.rowTabs[tabKey[k]]) {
                                this.tabContents[tabKey] = this.tabContents[tabKey].concat(this.rowTabs[tabKey[k]]);
                            }
                        }
                    }
                }

                this.$emit('update:tabActiveKey', tab.key)


            },
            //生成所需要的数据json;
            generateCityData: function (tabKey) {
                if (tabKey === 'YZ') {
                    var tabJson = [];
                    for (var ke in this.tabContents) {
                        if (ke === 'hotCities' || ke === 'outCountry') {
                            continue;
                        }
                        if (!this.tabContents.hasOwnProperty(ke)) {
                            continue;
                        }
                        tabJson = tabJson.concat(this.tabContents[ke])
                    }
                    for (var kId in this.capitalCities) {
                        if (!this.capitalCities.hasOwnProperty(kId)) {
                            continue;
                        }
                        tabJson = tabJson.concat(this.capitalCities[kId])
                    }
                    console.log(JSON.stringify(tabJson))
                }
            },

            selected: function (city, key, ev) {
                var cityPickerTabContents = this.$refs.cityPickerTabContents;
                var cityListWidth;
                this.capitalShow = city.hasArea || false;
                this.capitalId = city.id;
                if (!city.hasArea) {
                    this.$emit('change', city.id);
                    this.$emit('selected', city);
                    return false;
                }
                cityListWidth = $(cityPickerTabContents).width();
                this.capitalStyle.width = cityListWidth + 'px';
                this.capitalStyle.marginLeft = (-$(ev.target).parents('li').position().left) + 'px';
                if (!this.capitalCities[city.id]) {
                    this.capitalCities[city.id] = [];
                    for (var i = 0; i < this.cityData.length; i++) {
                        if (this.cityData[i].parentId === city.id) {
                            this.capitalCities[city.id].push(this.cityData[i])
                        }
                    }
                }

            },

            handleChangeArea: function (area) {
                this.$emit('change', area.id);
                this.$emit('selected', area);
                this.capitalShow = false;
            }
        },
        created: function () {
            this.setDefaultTabContent();
        }
    })
}(Vue)