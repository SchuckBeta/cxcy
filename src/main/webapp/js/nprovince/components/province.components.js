/**
 * Created by Administrator on 2019/4/25.
 */


'use strict';


Vue.component('pro-view-header', {
    template: '<div class="pro-vw-header">\n' +
    '        <div class="pro-pic">\n' +
    '            <img :src="proModel.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proDefaultImg"  />\n' +
    '        </div>\n' +
    '        <div class="pro-info-row">\n' +
    '            <h4 class="name">{{ proModel.pName }}</h4>\n' +
    '            <p class="type-apply_date">\n' +
    '            <span class="type">{{ proModel.proCategory | selectedFilter(proCategoryEntries)}}</span\n' +
    '            ><span class="apply_date">{{ proModel.createDate | formatDate}}</span>\n' +
    '            </p>\n' +
    '            <p class="office">\n' +
    '                <i class="pro-icon iconfont icon-xuexiao"></i>{{ user.officeName\n' +
    '                }}<span class="officeType">（{{ proModel.schoolType }}）</span>\n' +
    '            </p>\n' +
    '            <p class="area">\n' +
    '                <i class="pro-icon iconfont icon-dingwei"></i>{{ user.areaCode }}\n' +
    '            </p>\n' +
    '        </div>\n' +
    '        <slot></slot>\n' +
    '        <div class="pro-vm-pl-h"></div>\n' +
    '    </div>',
    props: {
        project: {},
        proModel: {},
        user: {}
    },
    computed: {
        proCategoryEntries: function () {
            return this.$root.proCategoryEntries || {};
        }
    }
})


Vue.component('todo-project-item', {
    template: '<div class="todo-project-item">\n' +
    '    <a href="javascript:void(0);" @click.stop="$emit(\'change-project\', item)">\n' +
    '      <div class="todo-project-item-box">\n' +
    '        <div class="pro-pic">\n' +
    '          <img :src="item.logo | proGConLogo | ftpHttpFilter(ftpHttp) | proDefaultImg" />\n' +
    '        </div>\n' +
    '        <div class="pro-todo-info">\n' +
    '          <p class="name">{{ item.pName }}</p>\n' +
    '          <p class="type-apply_date">\n' +
    '            <span class="type">{{ item.proCategory | selectedFilter(proCategoryEntries) }}</span>\n' +
    '            <span class="apply-date">{{ item.createDate | formatDate }}</span>\n' +
    '          </p>\n' +
    '        </div>\n' +
    '      </div>\n' +
    '    </a>\n' +
    '  </div>',
    props: {
        item: {},
        to: [String, Object]
    },
    computed: {
        proCategoryEntries: function () {
            return this.$root.proCategoryEntries
        }
    }
})


Vue.component('edit-bar-province', {
    template: '<div class="edit-bar edit-bar-tag clearfix edit-bar_new edit-bar-province">\n' +
    '    <div class="edit-bar-left">\n' +
    '      <div class="edit-bar-path-content" v-if="$slots.default">\n' +
    '        <slot></slot>\n' +
    '      </div>\n' +
    '      <template v-if="!$slots.default">\n' +
    '        <div class="edit-bar-path-content">\n' +
    '          <el-breadcrumb separator-class="el-icon-arrow-right">\n' +
    '            <el-breadcrumb-item\n' +
    '              v-for="(item, index) in breadcrumb"\n' +
    '              :key="index"\n' +
    '              ><a :href="ctx+item.href">{{ item.name }}</a>\n' +
    '            </el-breadcrumb-item>\n' +
    '          </el-breadcrumb>\n' +
    '        </div>\n' +
    '        <el-button\n' +
    '        v-if="breadcrumb.length > 1"\n' +
    '        class="btn-eb-back"\n' +
    '        size="mini"\n' +
    '        @click.stop.prevent="historyGoBack"\n' +
    '        >返回\n' +
    '        </el-button>\n' +
    '      </template>\n' +
    '      <i class="line weight-line"></i>\n' +
    '    </div>\n' +
    '  </div>',
    props: {
        label: String,
        secondName: String,
        href: String
    },
    data: function () {
        return {
            title: "",
            breadcrumb: []
        };
    },
    computed: {},
    methods: {
        historyGoBack: function () {
            if (!this.href) {
                window.history.go(-1);
                return false;
            }
            window.location.href = this.ctx ? (this.ctx + this.href) : this.ctx;
        },

        getMenuBreadcrumb: function () {
            var breadcrumb = [];
            if (window.parent) {
                breadcrumb = window.parent.LeftSideBarMenuApp.getMenuBreadcrumb();
            }
            if (this.secondName) {
                breadcrumb.push({
                    name: this.secondName,
                    href: ''
                })
            }
            return breadcrumb.slice(1)
        }
    },
    mounted: function () {
        this.breadcrumb = this.getMenuBreadcrumb();
    }
})


