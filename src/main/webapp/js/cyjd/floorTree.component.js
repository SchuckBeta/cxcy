/**
 * Created by Administrator on 2017/12/11.
 */

+function ($) {
    var floorTree = Vue.component('floor-tree', {
        template: '<div> <input type="text" name="pwRoom.id" v-model="roomId" class="input-large hide" >' +
        ' <input type="text" name="pwRoom.name" :class="className" :disabled="disabled" v-model="roomName" class="input-large required" @click="getTreeData($event)" placeholder="-请选择-" readonly> </div>',
        props: {
            roomId: {
                type: String,
                default: ''
            },
            roomName: {
                type: String,
                default: ''
            },
            extId: {
                type: String,
                default: ''
            },
            url: {
                type: String,
                default: ''
            },
            module: {
                type: String,
                default: ''
            },
            checked: {
                type: [String, Boolean],
                default: ''
            },
            disabled: {
                type: Boolean,
                default: false
            },
            className: {
                type: String,
                default: ''
            },
            ctx: {
                type: String,
                default: ''
            }
        },
        methods: {
            getTreeData: function ($event) {
                var self = this;
                var ctx =this.ctx;
                top.$.jBox.open('iframe:' + this.ctx + '/tag/treeselect?url=' + encodeURIComponent(this.url) + '&module=' + this.module + '&checked=' + this.checked + '&extId=' + this.extId + '&isAll=true',
                    "选择房间", 320, 420, {
                        ajaxData: {selectIds: self.roomId},
                        buttons: {"确定": "ok", "清除": 'clear', "关闭": true},
                        submit: function (v, h, f) {
                            if (v == "ok") {
                                var tree = h.find("iframe")[0].contentWindow.tree;//h.find("iframe").contents();
                                var ids = [], names = [], nodes = [];
                                nodes = tree.getSelectedNodes();
                                var isChecked = true;
                                $.each(nodes, function (i, node) {
                                    if (node.type !== 'room') {
                                        isChecked = false;
                                        top.$.jBox.tip("请选择房间");
                                        return false;
                                    } else {
                                        isChecked = true;
                                        ids.push(node.id);
                                        names.push(node.name);
                                    }
                                });

                                if (!isChecked) {
                                    return false;
                                }
                                self.$emit('room-info', [ids.join(",").replace(/u_/ig, ""), names.join(",")])
                                $($event.target).parent().find('label.error').hide()
                            } else if (v === 'clear') {
                                self.roomId = '';
                                self.roomName = '';
                                $($event.target).parent().find('label.error').show()
                            }

                        },
                        loaded: function (h) {
                            $(".jbox-content", top.document).css("overflow-y", "hidden");
                        }
                    });
            }
        }
    })

}(jQuery);