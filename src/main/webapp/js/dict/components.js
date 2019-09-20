/*api 文档说明 https://github.com/Coffcer/vue-bootstrap-modal*/


var MyModal = Vue.component('my-modal', {
    // template:'<div>123456</div>'
    template: '<div v-show="show" :transition="transition">\
					<div class="modal" @click.self="clickMask">\
							<div class="modal-dialog" :class="modalClass" v-el:dialog>\
								<div class="modal-content">\
									<div class="modal-header">\
										<button type="button" class="close" @click="cancel"><span>&times;</span></button>\
										<slot name="header">\
											<h4 class="modal-title">{{title}}</h4>\
										</slot>\
									</div>\
									<div class="modal-body">\
										<slot name="body"></slot>\
									</div>\
									<div class="modal-footer">\
										<slot name="footer">\
				                            <button type="button" :class="cancelClass" @click="cancel">{{cancelText}}</button>\
				                            <button type="button" :class="okClass" @click="ok">{{okText}}</button>\
				                        </slot>\
									</div>\
								</div>\
							</div>\
						</div>\
						<div class="modal-backdrop in"></div>\
					</div>\
				</div>',
    props: {
        show: {
            type: Boolean,
            twoWay: true,
            default: false
        },
        small: {
            type: Boolean,
            default: false
        },
        large: {
            type: Boolean,
            default: false
        },
        full: {
            type: Boolean,
            default: false
        },
        title: {
            type: String,
            defalut: 'Modal'
        },
        force: {
            type: Boolean,
            default: false
        },
        okText: {
            type: String,
            default: 'OK'
        },
        // 自定义组件transition
        transition: {
            type: String,
            default: 'modal'
        },
        cancelText: {
            type: String,
            default: 'Cancel'
        },
        // 确认按钮className
        okClass: {
            type: String,
            default: 'btn blue'
        },
        // 取消按钮className
        cancelClass: {
            type: String,
            default: 'btn red btn-outline'
        },
        // 点击确定时关闭Modal
        // 默认为false，由父组件控制prop.show来关闭
        closeWhenOK: {
            type: Boolean,
            default: false
        }
    },
    data: function () {
        return {
            duration: null
        };
    },
    computed: {
        modalClass: function () {
            return {
                'modal-lg': this.large,
                'modal-sm': this.small,
                'modal-full': this.full
            }
        }
    },
    created: function () {
        if (this.show) {
            document.body.className += ' modal-open';
        }
    },
    beforeDestroy: function () {
        document.body.className = document.body.className.replace(/\s?modal-open/, '');
    },
    watch: {
        show: function (value) {
            // 在显示时去掉body滚动条，防止出现双滚动条
            if (value) {
                document.body.className += ' modal-open';
            }
            // 在modal动画结束后再加上body滚动条
            else {
                if (!this.duration) {
                    this.duration = window.getComputedStyle(this.$el)['transition-duration'].replace('s', '') * 1000;
                }
                window.setTimeout(function () {
                    document.body.className = document.body.className.replace(/\s?modal-open/, '');
                }, this.duration || 0);
            }
        }
    },
    methods: {
        ok: function () {
            this.$emit('ok');
            if (this.closeWhenOK) {
                this.show = false;
            }
        },
        cancel: function () {
            this.$emit('cancel');
            this.show = false;
        },
        // 点击遮罩层
        clickMask: function () {
            if (!this.force) {
                this.cancel();
            }
        }
    }
});


var MyPagination = Vue.component('my-pagination', {
    props: {
        pageSize: {
            type: Number,
            default: 10
        },
        total: Number,
        pageCount: Number,
        currentPage: {
            type: Number,
            default: 1
        },
        pageSizes: {
            type: Array,
            default: function () {
                return [10, 20, 30];
            }
        }
    },
    template: '<div class="pagination-bar">\
                    <div class="num-page" style="display: none">\
                        <p>当前第{{currentPage}}页  总记录{{total}}条</p>\
                        <div class="select-page">\
                            <select v-model="pageSize" class="form-control" @change="selectChange(pageSize)">\
                                <option v-for="value in pageSizes" :value="value">{{value}}</option>\
                            </select>\
                        </div>\
                    </div>\
                    <ul class="pagination-list">\
                        <li class="prev">\
                            <a href="javascript:void(0);" :class="{disabled: currentPage === 1}" v-bind:disabled="currentPage === 1"  @click="prev">上一页</a>\
                        </li>\
                        <li :class="{ active: currentPage === 1 }" v-if="pageCount > 0" class="number">\
                            <a href="javascript:void(0)" @click="handleChange(1)">1</a>\
                        </li>\
                        <li v-if="showPrevMore"><span>...</span></li>\
                        <li v-for="pager in pagers" :class="{ active: currentPage === pager }" class="number">\
                            <a href="javascript:void(0)" @click="handleChange(pager)">{{ pager }}</a>\
                         </li>\
                         <li v-if="showNextMore"><span>...</span></li>\
                         <li :class="{ active: currentPage === pageCount }" class="number" v-if="pageCount > 1">\
                            <a href="javascript:void(0)" @click="handleChange(pageCount)">{{pageCount}}</a>\
                        </li>\
                        <li class="next">\
                            <a href="javascript:void(0);" :class="{disabled: currentPage === pageCount}"  @click="next">下一页</a>\
                        </li>\
                    </ul>\
                </div>',
    data: function () {
        return {
            showPrevMore: false,
            showNextMore: false
        }
    },
    computed: {
        pageCount: function () {
            if (typeof this.total === 'number') {
                return Math.ceil(this.total / this.pageSize);
            } else if (typeof this.pageCount === 'number') {
                return this.pageCount;
            }
            return null;
        },
        pagers: function () {
            var pagerCount = 7;
            var currentPage = this.currentPage;
            var pageCount = this.pageCount;
            var showPrevMore = false;
            var showNextMore = false;
            if (pageCount > pagerCount) {
                if (currentPage > pagerCount - 2) {
                    showPrevMore = true;
                }
                if (currentPage < pageCount - 2) {
                    showNextMore = true;
                }
            }
            var array = [];
            if (showPrevMore && !showNextMore) {
                const startPage = pageCount - (pagerCount - 2);
                for (var i = startPage; i < pageCount; i++) {
                    array.push(i);
                }
            } else if (!showPrevMore && showNextMore) {
                for (var i = 2; i < pagerCount; i++) {
                    array.push(i);
                }
            } else if (showPrevMore && showNextMore) {
                const offset = Math.floor(pagerCount / 2) - 1;
                for (var i = currentPage - offset; i <= currentPage + offset; i++) {
                    array.push(i);
                }
            } else {
                for (var i = 2; i < pageCount; i++) {
                    array.push(i);
                }
            }
            this.showPrevMore = showPrevMore;
            this.showNextMore = showNextMore;
            return array;
        }
    },
    watch: {
        pageSize: function (val) {
            console.log(val)
        }
    },
    methods: {
        prev: function () {
            if (this.currentPage === 1) {
                return
            }
            this.currentPage--;
            this.handleChange(this.currentPage);
        },
        next: function () {
            if (this.currentPage === this.pageCount) {
                return
            }
            this.currentPage++;
            this.handleChange(this.currentPage);
        },
        handleChange: function (pager) {
            this.$emit('handle-change', pager);
            this.currentPage = pager
        },
        selectChange: function (val) {
            this.currentPage = 1;
            this.$emit('select-change', val, this.currentPage);
        }
    }
});

// Vue.component('my-pagination',MyPagination);

var Alert = Vue.component('b-alert', {
    template: '<div v-if="localShow" :class="classObject" role="alert" aria-live="polite" aria-atomic="true">\
                <button type="button" class="close"  data-dismiss="alert" aria-label="dismissLabel" v-if="dismissible" @click.stop.prevent="dismiss">\
                    <span aria-hidden="true">&times;</span></button>\
                <slot></slot>\
            </div>',
    data: function () {
        return {
            countDownTimerId: null,
            dismissed: false
        };
    },
    created: function () {
        if (this.state) {
            console.warn('<b-alert> "state" property is deprecated, please use "variant" property instead.');
        }
    },
    computed: {
        classObject: function () {
            return ['alert', this.alertVariant, this.dismissible ? 'alert-dismissible' : ''];
        },
        alertVariant: function () {
            const variant = this.state || this.variant || 'info';
            return 'alert-' + variant;
        },
        localShow: function () {
            return !this.dismissed && (this.countDownTimerId || this.show);
        }
    },
    props: {
        variant: {
            type: String,
            default: 'info'
        },
        state: {
            type: String,
            default: null
        },
        dismissible: {
            type: Boolean,
            default: false
        },
        dismissLabel: {
            type: String,
            default: 'Close'
        },
        show: {
            type: [Boolean, Number],
            default: false
        }
    },
    watch: {
        show: function () {
            this.showChanged();
        }
    },
    mounted: function () {
        this.showChanged();
    },
    methods: {
        dismiss: function () {
            this.dismissed = true;
            this.$emit('dismissed');
            this.clearCounter();
        },
        clearCounter: function () {
            if (this.countDownTimerId) {
                clearInterval(this.countDownTimerId);
            }
        },
        showChanged: function () {
            var self = this;
            // Reset dismiss status
            this.dismissed = false;
            // No timer for boolean values
            if (this.show === true || this.show === false || this.show === null || this.show === 0) {
                return;
            }
            var dismissCountDown = this.show;
            this.$emit('dismiss-count-down', dismissCountDown);
            // Start counter
            this.clearCounter();
            this.countDownTimerId = setInterval(function () {
                if (dismissCountDown < 2) {
                    return self.dismiss();
                }
                dismissCountDown--;
                self.$emit('dismiss-count-down', dismissCountDown);
            }, 1000);
        }
    }
})