/**
 * Created by Administrator on 2017/12/14.
 */


+function ($) {
    Vue.directive('floor', {
        inserted: function (element, binding, vnode) {
            var url = vnode.context.url;
            var xhr = $.get(url);
            var id = vnode.context.roomId;
            xhr.success(function (data) {
                var selectNode;
                if (!data.length) {
                    return
                }
                data.map(function (t) {
                    if (t.type <= 3) {
                        t.open = true
                    }
                    if (t.id === id) {
                        selectNode = t;
                    }
                });
                var floorTree = $.fn.zTree.init($(element), {
                    view: {
                        showIcon: true
                    },
                    data: {simpleData: {enable: true, idKey: "id", pIdKey: "pId", rootPId: '1'}},
                    callback: {
                        onClick: function (event, treeId, treeNode, clickFlag) {
                            if (treeNode.type === 'room') {
                                vnode.context.tip = false;
                                vnode.context.roomId = treeNode.id;
                                vnode.context.roomName = treeNode.name;
                                console.log(treeNode)
                            }
                        },
                        onDblClick: function (event, treeId, treeNode, clickFlag) {
                            var nodes = floorTree.getSelectedNodes();
                            if (treeNode.type === 'room') {
                                console.log(treeNode)
                                vnode.context.roomId = treeNode.id;
                                vnode.context.roomName = treeNode.name;
                            } else {
                                vnode.context.tip = true;
                            }
                        }
                    }
                }, data);

                if (selectNode) {
                    var node = floorTree.getNodeByParam('id', selectNode.id);
                    floorTree.selectNode(node, false);
                }
            });
        },
        unbind: function () {

        }
    })
}(jQuery);

+(function ($) {
    var floorTree = Vue.component('floor-tree', {
        template: '<div v-show="show" class="floor-tree-container" style="display: none"><div class="modal">' +
        '<div class="modal-dialog modal-sm"><div class="modal-content">' +
        '<div class="modal-header">' +
        '<button type="button" class="close" @click="cancel"><span aria-hidden="true">×</span></button> ' +
        '<h4 class="modal-title">选择房间</h4></div>' +
        '<div class="modal-body"><div v-show="tip" class="alert alert-danger">请选择房间</div><div v-if="show" class="ztree" v-floor></div></div>' +
        '<div class="modal-footer"><button type="button" @click="save" class="btn btn-save btn-sm btn-primary">确定</button> <button type="button" @click="clear" class="btn btn-sm btn-primary">清空</button>' +
        '<button type="button" class="btn btn-sm btn-default" @click="cancel">取消</button></div></div></div></div>' +
        '</div></div>',
        props: {
            show: {
                type: Boolean,
                default: false
            },
            roomId: {
                type: String,
                default: ''
            },
            roomName: {
                type: String,
                default: ''
            },
            url: {
                type: String,
                default: ''
            }
        },
        data: function () {
            return {
                tip: false
            }
        },
        watch: {
            show: function (val) {
                console.log(val)
            }
        },
        methods: {
            save: function () {
                this.$emit('save', [this.roomId, this.roomName])
            },
            clear: function () {
                this.$emit('clear');
                this.show = false;
            },
            cancel: function () {
                this.$emit('cancel')
            }
        },
        mounted: function () {

        }
    })

})(jQuery);

