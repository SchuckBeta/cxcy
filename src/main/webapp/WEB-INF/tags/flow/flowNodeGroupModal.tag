<%@ tag language="java" pageEncoding="UTF-8" %>


<div class="modal-module" :data-show="controlModal">
    <my-modal title="修改流程节点" :show.sync="modalShow" @ok="saveFlowGroup" @cancel="cancel" ok-text="确定" cancel-text="取消"
              :large="true">
        <div class="modal-body" slot="body">
            <form class="form-horizontal">
                <div class="control-group"><label class="control-label">流程组名：</label>
                    <div class="controls"><input type="hidden" v-model="groupData.groupId"><span class="control-static">{{groupName}}</span>
                    </div>
                </div>
                <div class="control-group"><label class="control-label">流程跟节点：</label>
                    <div class="controls"><input type="hidden" v-model="groupData.parentId"><span
                            class="control-static">{{parentName}}</span></div>
                </div>
                <%--hideNextFunIds--%><%--{{prevNodes}}--%>
                <div v-show="hideNextFunIds" class="control-group"><label class="control-label">流程前置业务节点：</label>
                    <div class="controls"><select v-model="groupData.preFunId" class="form-control input-large">
                        <option value="0">-请选择-</option>
                        <option v-for="(item, index) in prevNodes" :value="item.id">{{item.name}}</option>
                    </select></div>
                </div>
                <div class="control-group"><label class="control-label"><span class="red"
                                                                              style="margin-right: 4px;">*</span>流程业务节点：</label>
                    <div class="controls"><input type="text" class="form-control input-large" name="name"
                                                 v-model="groupData.name" @change="hasNodeGroupName = !groupData.name">
                        <div v-show="hasNodeGroupName" class="red">请填写节点名称</div>
                    </div>
                </div>
                <div v-show="hideNextFunIds" class="control-group"><label class="control-label">流程后置业务节点：</label>
                    <div class="controls"><select v-model="groupData.nextFunId" class="form-control input-large">
                        <option :value="0">-请选择-</option>
                        <option v-if="nextNodes.length > 0" v-for="(item,idx) in nextNodes" :value="item.id"
                                :key="item.id">{{item.node.name}}
                        </option>
                    </select></div>
                </div>
                <div class="control-group"><label class="control-label"><span class="red"
                                                                              style="margin-right: 4px;">*</span>图标</label>
                    <div class="controls">
                        <div class="img-content" style="max-width: 100px; max-height: 100px;line-height: 1"><img
                                id="iconPic" :src="groupData.iconUrl" style="display: block;max-width: 100%"><%--<input type="text" style="display: none" name="file" v-model="groupData.iconUrl">--%>
                        </div>
                        <input type="button" id="uploadIcon" class="btn" style="" value="更新图标"/>
                        <div v-show="controlIconUrl" class="red">请上传图标</div>
                    </div>
                </div>
                <div class="control-group"><label class="control-label">显示：</label>
                    <div class="controls"><label><input type="radio" v-model="groupData.isShow" :value="1"
                                                        class="form-control">是</label><label><input type="radio"
                                                                                                    v-model="groupData.isShow"
                                                                                                    :value="0"
                                                                                                    class="form-control">否</label>
                    </div>
                </div>
                <div class="control-group"><label class="control-label">备注：</label>
                    <div class="controls"><textarea rows="3" v-model="groupData.remarks"
                                                    class="input-xlarge"></textarea></div>
                </div>
            </form>
        </div>
    </my-modal>
</div>


<script>
    var flowGroupModal = Vue.component('flow-group-modal', {
        template: '<div class="modal-module" :data-show="controlModal"><my-modal title="修改流程节点" :show.sync="modalShow" @ok="saveFlowGroup" @cancel="cancel"              ok-text="确定" cancel-text="取消" :large="true"><div class="modal-body" slot="body"><form class="form-horizontal"><div class="control-group"><label class="control-label">流程组名：</label><div class="controls"><input type="hidden" v-model="groupData.groupId"><span class="control-static">{{groupName}}</span></div></div><div class="control-group"><label class="control-label">流程跟节点：</label><div class="controls"><input type="hidden" v-model="groupData.parentId"><span class="control-static">{{parentName}}</span></div></div><%--hideNextFunIds--%><%--{{prevNodes}}--%><div v-show="hideNextFunIds" class="control-group"><label class="control-label">流程前置业务节点：</label><div class="controls"><select v-model="groupData.preFunId" class="form-control input-large"><option value="0">-请选择-</option><option v-for="(item, index) in prevNodes" :value="item.id" >{{item.name}}</option></select></div></div><div class="control-group"><label class="control-label"><span class="red"                                                       style="margin-right: 4px;">*</span>流程业务节点：</label><div class="controls"><input type="text" class="form-control input-large"  name="name"                               v-model="groupData.name" @change="hasNodeGroupName = !groupData.name"><div v-show="hasNodeGroupName" class="red">请填写节点名称</div></div></div><div v-show="hideNextFunIds" class="control-group"><label class="control-label">流程后置业务节点：</label><div class="controls"><select v-model="groupData.nextFunId" class="form-control input-large"><option :value="0">-请选择-</option><option v-if="nextNodes.length > 0" v-for="(item,idx) in nextNodes" :value="item.id" :key="item.id">{{item.node.name}}</option></select></div></div><div class="control-group"><label class="control-label"><span class="red" style="margin-right: 4px;">*</span>图标</label><div class="controls"><div class="img-content" style="max-width: 100px; max-height: 100px;line-height: 1"><img id="iconPic" :src="groupData.iconUrl" style="display: block;max-width: 100%"><%--<input type="text" style="display: none" name="file" v-model="groupData.iconUrl">--%></div><input type="button" id="uploadIcon" class="btn" style="" value="更新图标"/><div v-show="controlIconUrl" class="red">请上传图标</div></div></div><div class="control-group"><label class="control-label">显示：</label><div class="controls"><label><input type="radio" v-model="groupData.isShow" :value="1" class="form-control">是</label><label><input type="radio" v-model="groupData.isShow" :value="0" class="form-control">否</label></div></div><div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" v-model="groupData.remarks" class="input-xlarge"></textarea></div></div></form></div></my-modal></div>',
        components: {},
        props: {
            controlModal: {
                type: Boolean,
                default: false
            },
            controlIconUrl: {
                type: Boolean,
                default: false
            },
            nextNodes: {
                type: Array,
                default: function () {
                    return []
                }
            },
            prevNodes: {
                type: Array,
                default: function () {
                    return [];
                }
            },
            groupName: {
                type: String,
                default: ''
            },
            parentName: {
                type: String,
                default: ''
            },
            groupData: {
                type: Object,
                twoWay: true,
                default: function () {
                    return {
                        id: '',
                        preFunId: '',
                        preFunGnode: {
                            node: {
                                id: ''
                            }
                        },
                        name: '',
                        nextFunId: '',
                        nextFunGnode: {
                            id: '',
                            node: {
                                id: ''
                            }
                        },
                        isShow: 1,
                        role: '',
                        remarks: '',
                        hasGateway: false,
                        parent: {
                            id: ''
                        },
                        nodeId: '',
                        hasGroup: false,
                        posLux: '',
                        posLuy: '',
                        width: '',
                        height: '',
                        iconUrl: ''
                    }
                }
            }
        },
        data: function () {
            return {
                hidePreFunIds: false,
                hideNextFunIds: false,
                hasNodeGroupName: false
            }
        },
        computed: {
            modalShow: function () {
                return this.controlModal;
            }
        },
        watch: {
            controlModal: function (value) {
                this.modalShow = value;
            },
//            'groupData.iconUrl': function (value) {
//                console.log(value)
//                this.isUploadIconUrl = !value
//            }
        },
        methods: {
            saveFlowGroup: function () {
                if (!this.groupData.name) {
                    this.hasNodeGroupName = true;
                    return false;
                }
                this.$emit('save-flow-group');
            },

            cancel: function () {
                this.$emit('cancel');
            }
        },
        beforeMount: function () {

        },
        mounted: function () {

        }
    });
</script>