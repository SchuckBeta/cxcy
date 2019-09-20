/**
 * Created by Administrator on 2019/1/3.
 */

'use strict';


Vue.component('credit-user-column', {
    template: '<div class="credit-user-column"><div class="cu-photo"><img :src="user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter" :alt="user.name"></div><div class="cu-info"><p class="cu-no">{{user.no}}</p>' +
    '<p class="cu-name">{{user.name}}</p>' +
    '<el-tooltip v-if="!cnName" :content="user.professional || user.officeId | getProfessionName(officeEntries)" popper-class="white" placement="right"><p class="cu-office break-ellipsis">{{user.professional || user.officeId | getProfessionName(officeEntries)}}</p></el-tooltip>' +
    '<el-tooltip v-else :disabled="!officeNamePro" :content="officeNamePro" popper-class="white" placement="right"><p class="cu-office break-ellipsis">{{officeNamePro}}</p></el-tooltip></div></div>',
    props: {
        user: {
            type: Object,
            required: true
        },
        cnName: Boolean
    },
    data: function () {
        return {}
    },
    computed: {
        officeNamePro: function () {
            if (this.user.officeName) {
                return this.user.officeName + (this.user.professional ? ('/' + this.user.professional) : '');
            }
            return ''
        },
        officeList: function () {
            var officeList = this.$root.officeList;
            if (!officeList) {
                console.error("机构获取异常");
                return [];
            }
            return officeList;
        },
        officeEntries: function () {
            var entries = {};
            var i = 0;
            while (i < this.officeList.length) {
                var item = this.officeList[i];
                entries[item.id] = item;
                i++;
            }
            return entries;
        }
    },
    methods: {}
})

Vue.component('credit-course-column', {
    template: '<div class="credit-course-column"><e-col-item label="名称："><el-tooltip :content="tooltipContent" popper-class="white" placement="right"><template v-if="applyId"><a :href="href" class="course-name break-ellipsis">{{course.name}}({{course.code}})</a></template><template v-else><span class="course-name break-ellipsis">{{course.name}}({{course.code}})</span></template></el-tooltip></e-col-item><e-col-item label="性质：">{{course.nature | selectedFilter(scoCourseNatureEntries)}}</e-col-item><e-col-item label="类型：">{{course.type | selectedFilter(scoCourseTypeEntries)}}</e-col-item></div>',
    props: {
        course: {
            type: Object,
            required: true
        },
        applyId: String
    },
    computed: {
        scoCourseTypeEntries: function () {
            return this.$root.scoCourseTypeEntries
        },
        scoCourseNatureEntries: function () {
            return this.$root.scoCourseNatureEntries
        },
        tooltipContent: function () {
            return this.course.name + '(' + this.course.code + ')';
        },
        href: function () {

            return '/a/scoapply/view?id=' + this.applyId
        }
    }
})

var CreditChangeFormSingle = Vue.component('credit-change-form-single', {
    template: '<div><el-form :model="creditForm"  ref="creditForm"  size="mini" :disabled="disabled" label-width="120px"><el-form-item prop="val" :rules="valRules" label="修改学分"><el-input v-model="creditForm.val" style="width: 100px;" maxlength="11"></el-input></el-form-item></el-form></div>',
    props: {
        disabled: Boolean,
        form: Object
    },
    data: function () {
        return {
            creditForm: {
                apply: {
                    id: ''
                },
                user: {
                    id: ''
                },
                val: ''
            }
        }
    },
    computed: {
        valRules: function () {
            return this.$root.valRules
        }
    },
    methods: {
        updateCreditForm: function () {
            this.assignFormData(this.creditForm, this.form);
        }
    },
    created: function () {
        // this.assignFormData(this.creditForm, this.form);
    }
});

var CreditChangeFormMany = Vue.component('credit-change-form-many', {
    template: '<div><el-form :model="creditForm" ref="creditForm" size="mini" inline-message :disabled="disabled" label-width="0">' +
    '<div class="table-container"><el-table :data="creditForm.scoRsumList" size="mini" class="table"><el-table-column width="160" prop="user.name" label="组成员"><template slot-scope="scope">{{scope.row.user.name}}</template></el-table-column>' +
    '<el-table-column label="学分"  prop="val" :render-header="creditRatioTableLabel"><template slot-scope="scope"><el-form-item :rules="valRules" style="margin-bottom: 0" :prop="\'scoRsumList.\'+scope.$index+\'.val\'" ><el-input style="width: 100px;" v-model="scope.row.val" maxlength="11"></el-input></el-form-item></template></el-table-column></el-table></div></el-form></div>',
    props: {
        disabled: Boolean,
        form: Object
    },
    data: function () {
        return {
            creditForm: {
                scoRsumList: [
                    {
                        apply: {
                            id: ''
                        },
                        user: {
                            id: '',
                            name: ''
                        },
                        val: ''
                    }
                ]
            }
        }
    },
    computed: {
        valRules: function () {
            return this.$root.valRules
        }
    },
    methods: {
        updateCreditForm: function () {
            this.assignFormData(this.creditForm, this.form);
        },
        //渲染表头
        creditRatioTableLabel: function (h) {
            return h('span', {
                'class': 'credit-ratio-label'
            }, '学分')
        },
    },
    created: function () {

    }
})