var MyModal = Vue.component('my-modal', {
    // template:'<div>123456</div>'
    template: '<transition :name="transition">\
          <div v-show="show" style="display: none"  class="modal" @click.self="clickMask">\
              <div class="modal-dialog" :class="modalClass">\
                <div class="modal-content">\
                  <div v-if="type" class="modal-header">\
                    <button type="button" class="close" @click="cancel"><span>&times;</span></button>\
                    <slot name="header">\
                      <h4 class="modal-title">{{title}}</h4>\
                    </slot>\
                  </div>\
                <slot name="body"></slot>\
                  <div v-if="type" class="modal-footer">\
                    <slot name="footer">\
                        <button type="button" :class="cancelClass"  @click="cancel">{{cancelText}}</button>\
                        <button type="button" class="btn-node-primary" v-show="hasSave" :disabled="saveDisabled" :class="okClass" @click="ok">{{okText}}</button>\
                    </slot>\
                  </div>\
                </div>\
              </div>\
            </div>\
            <div class="modal-backdrop in"></div>\
          </div>\
        </transition>',
    props: {
        show: {
            type: Boolean,
            twoWay: true,
            default: false
        },
        hasSave: {
          type: Boolean,
            default: true
        },
        value: {
            type: Boolean,
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
            default: '确定'
        },
        // 自定义组件transition
        transition: {
            type: String,
            default: 'modal'
        },
        cancelText: {
            type: String,
            default: '取消'
        },
        // 确认按钮className
        okClass: {
            type: String,
            default: 'btn'
        },
        // 取消按钮className
        cancelClass: {
            type: String,
            default: 'btn btn-outline'
        },
        // 点击确定时关闭Modal
        // 默认为false，由父组件控制prop.show来关闭
        closeWhenOK: {
            type: Boolean,
            default: false
        },
        saveDisabled: {
            type: Boolean,
            default: false
        },
        modalType: {
            type: String,
            default: 'modal'
        },
        autoClose: {
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
        },
        type: function(){
          return this.modalType === 'modal';
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
            if (this.closeWhenOK) {
                this.show = false;
            }

        },
        // 点击遮罩层
        clickMask: function () {
            if (!this.force) {
                this.cancel();
            }
        }
    }
});





