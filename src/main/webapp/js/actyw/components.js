/*api 文档说明 https://github.com/Coffcer/vue-bootstrap-modal*/


var MyModal = Vue.extend({
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
                        <button type="button" class="btn-node-primary" :class="okClass" @click="ok">{{okText}}</button>\
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
})


var AddParentNode = Vue.extend({
    props: {
      parentNodeData:{
        type:Array,
        default:function(){
          return {
              flowPath:{
                type:[String,Number],
                default:'',
              },
              nodeName:{
                type:[String,Number],
                default:''
              },
              nodeLevel:{
                type:Number,
                default:''
              },
              nodeDesc:{
                type:[String,Number],
                default:''
              }
            }
        }
      },
      editable:{
        type:Boolean,
        default:false
      }
    },
    template: '<div>\
              <div v-for="node in parentNodeData" id="parentNodeWrap{{$index}}" class="form-group-wrap">\
                <div class="form-group">\
                  <label for="flowPath{{$index}}" class="control-label w130"><i class="icon-require">*</i>所属工作流：</label>\
                  <div class="input-box">\
                    <input type="text" id="flowPath{{$index}}" class="form-control" v-model="node.flowPath">\
                  </div>\
                </div>\
                <div class="row">\
                  <div class="col-sm-7">\
                    <div class="form-group">\
                      <label for="nodeName{{$index}}" class="control-label w130"><i class="icon-require">*</i>节点名：</label>\
                      <div class="input-box">\
                        <input type="text" id="nodeName{{$index}}" class="form-control" v-model="node.nodeName" placeholder="" />\
                      </div>\
                    </div>\
                  </div>\
                  <div class="col-sm-5">\
                    <div class="form-group form-lable-w84">\
                      <label for="nodeLevel{{$index}}" class="control-label"><i class="icon-require">*</i>级别：</label>\
                      <div class="input-box">\
                        <input type="text" id="nodeLevel{{$index}}" class="form-control" v-model="node.nodeLevel" placeholder="" />\
                      </div>\
                    </div>\
                  </div>\
                </div>\
                <div class="form-group">\
                  <label for="nodeDesc{{$index}}" class="control-label w130">描述：</label>\
                  <div class="input-box">\
                    <textarea id="nodeDesc{{$index}}" rows="2" class="form-control" v-model="node.nodeDesc"></textarea>\
                  </div>\
                </div>\
              </div>\
              <div v-show="editable" id="parentBtn" class="text-right">\
                <button type="button" class="btn btn-node-hover" v-on:click="appendPNode()">添加</button>\
              </div>\
            </div>',
    methods: {
        appendPNode: function () {
            this.parentNodeData.push({
                flowPath: '',
                nodeName: '',
                nodeLevel: '',
                nodeDesc: ''
            });
        }
    },
    watch: {
        parentNodeData: {
            handler: function () {
                this.$dispatch('addNodesEvent', this.parentNodeData)
            },
            deep: true
        }
    }
});

var AddChildNode = Vue.extend({
    props: {
        childNodeData: {
            type: Array,
            default: function(){
              return {
                flowPathParent:{
                  type:[String,Number],
                  default:'',
                },
                nodeParentLevel:{
                  type:Number,
                  default:''
                },
                childNodeName:{
                  type:String,
                  default:''
                },
                nodeChildLevel:{
                  type:[String,Number],
                  default:'',
                },
                childNodeDesc:{
                  type:[String,Number],
                  default:''
                }
              }
            },
        },
        editable:{
          type:Boolean,
          default:false
        }
    },
    template: '<div><div  v-for="node in childNodeData" class="form-group-wrap">\
              <div class="row">\
                <div class="col-sm-7">\
                  <div class="form-group">\
                    <label for="flowPathParent{{index}}" class="control-label w130"><i class="icon-require">*</i>所属父节点：</label>\
                    <div class="input-box">\
                      <input id="flowPathParent{{index}}"  class="form-control" v-model="node.flowPathParent">\
                    </div>\
                  </div>\
                </div>\
                <div class="col-sm-5">\
                  <div class="form-group form-lable-w84">\
                    <label for="nodeParentLevel{{index}}" class="control-label"><i class="icon-require">*</i>级别：</label>\
                    <div class="input-box">\
                      <input type="text" id="nodeParentLevel{{index}}" class="form-control" v-model="node.nodeParentLevel" readonly placeholder="" />\
                    </div>\
                  </div>\
                </div>\
              </div>\
              <div class="row">\
                <div class="col-sm-7">\
                  <div class="form-group">\
                    <label for="childNodeName{{index}}" class="control-label w130"><i class="icon-require">*</i>子节点名：</label>\
                    <div class="input-box">\
                      <input type="text" id="childNodeName{{index}}" class="form-control" v-model="node.childNodeName" placeholder="" />\
                    </div>\
                  </div>\
                </div>\
                <div class="col-sm-5">\
                  <div class="form-group form-lable-w84">\
                    <label for="nodeChildLevel{{index}}" class="control-label"><i class="icon-require">*</i>级别：</label>\
                    <div class="input-box">\
                      <input type="text" id="nodeChildLevel{{index}}" class="form-control" v-model="node.nodeChildLevel" placeholder="" />\
                    </div>\
                  </div>\
                </div>\
              </div>\
              <div class="form-group">\
                <label for="childNodeDesc{{index}}" class="control-label w130">描述：</label>\
                <div class="input-box">\
                  <textarea id="childNodeDesc{{index}}" rows="2" v-model="node.childNodeDesc" class="form-control"></textarea>\
                </div>\
              </div>\
            </div>\
            <div v-show="editable" class="text-right">\
                <button type="button" class="btn btn-node-hover" @click="appendCNode">添加</button>\
              </div>\
            </div>',
            methods:{
              appendCNode:function(){
                this.childNodeData.push({
                    flowPathParent:'',
                    nodeParentLevel:'',
                    childNodeName:'',
                    nodeChildLevel:'',
                    childNodeDesc:''
                })
              }
            },
            watch:{
              childNodeData:{
                handler:function(){
                  this.$dispatch('addCNodesEvent', this.childNodeData)
                },
                deep:true
              }
            }

})


Vue.component('my-modal', MyModal);
Vue.component('add-parent-node', AddParentNode);
Vue.component('add-child-node', AddChildNode);
