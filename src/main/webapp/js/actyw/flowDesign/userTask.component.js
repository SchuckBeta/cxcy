+function ($, Vue) {
    var userTask = Vue.component('user-task', {
        template: '<div class="group" :class="{groupOpened: open}">' +
        '<h4 class="group-label"><i class="iconfont iconfont-open" @click="open = true">&#xe63b;</i>' +
        '<i class="iconfont iconfont-close" @click="open = false">&#xe63e;</i><span>编辑{{userTask.name}}节点</span>' +
        '<a href="javascript:void(0);"><i class="iconfont iconfont-delete">&#xe625;</i></a></h4>' +
        '<form class="form-horizontal">' +
        '<div class="control-group"><label class="control-label"><i>*</i>节点名称：</label><div class="controls">' +
        '<input type="text" autocomplete="off" name="name" v-model="userTask.name" @change="changeNodeName"></div></div>' +
        '<div class="control-group"><label class="control-label">节点类型：</label><div class="controls"><p class="control-static">{{userTask.typeName}}</p></div></div>' +
        '<div class="control-group">' +
        '<label class="control-label"><i>*</i>角色：</label>' +
        '<div class="controls"> <slot name="roles"></slot></div></div>' +
        '<div class="control-group" v-show="!selectedStudent"><label class="control-label" @mouseenter="parseLabel(\'userTaskType\',$event)" @mouseleave="leaveLabel">任务类型：</label><div class="controls"><select v-model="userTask.taskType"><option v-for="type in taskTypes" :value="type.key">{{type.remark}}</option></select></div></div>' +
        '<div class="control-group" v-show="false"><label class="control-label" @mouseenter="parseLabel(\'userTaskListForm\',$event)" @mouseleave="leaveLabel"><i>*</i>列表表单：</label><div class="controls">' +
        '<slot name="listForm"></slot></div></div>' +
        '<div class="control-group" v-show="!selectedStudent"><label class="control-label" @mouseenter="parseLabel(\'userTaskRegType\',$event)" @mouseleave="leaveLabel"><i>*</i>{{!selectedStudent ? \'审核类型\': \'选择表单\'}}：</label><div class="controls"><select v-model="userTask.regType" @change="filterRegType"><option value="">-请选择-</option><option v-for="(item, index) in regTypes" :key="item.id" :value="item.id">{{item.name}}</option></select></div></div>' +
        '<div class="control-group" v-show="userTask.regType == 2">' +
        '<label class="control-label">指派：</label>' +
        '<div class="controls"><label class="radio inline"><input type="radio" name="assign" v-model="userTask.isAssign" value="1">是</label><label class="radio inline"><input type="radio" name="assign" v-model="userTask.isAssign" value="0">否</label></div></div>' +
        '<div class="control-group"><label class="control-label" @mouseenter="parseLabel(\'userTaskForm\',$event)" @mouseleave="leaveLabel"><i>*</i>审核表单：</label><div class="controls">' +
        '<slot name="form"></slot></div></div>' +
        '<div class="control-group" v-show="visiableShow"><label class="control-label">显示：</label>' +
        '<div class="controls"><label class="radio inline"><input type="radio" name="isShow" v-model="userTask.isShow" value="1">是</label><label class="radio inline"><input type="radio" name="isShow" v-model="userTask.isShow" value="0">否</label></div></div>' +
        '<div class="control-group"><label class="control-label">图标：</label><div class="controls controls-iconbox">' +
        '<img title="更换图标" @click="openUploadModal($event)" class="node-icon" :src="userTask.iconUrl | ftpHttp"></div></div>' +
        '<div class="control-group" v-if="hasCondition"><label class="control-label"><i>*</i>条件：</label><div class="controls"><slot name="condition"></slot></div></div>' +
        '<div v-if="hasUsers" class="control-group"><label class="control-label">用户：</label>' +
        '<div class="controls"> <slot name="users"></slot></div></div>' +
        '<div class="control-group"><label class="control-label">备注：</label><div class="controls"><textarea rows="3" placeholder="最多33个字" v-model="userTask.remarks"></textarea></div></div>' +
        '</form></div>',
        model: {
            prop: 'userTask',
            event: 'change'
        },
        props: {
            userTask: {
                type: Object,
                default: {
                    roleIds: []
                }
            },
            visiableShow: {
                type: Boolean,
                default: false
            },
            studentId: {
                type: String,
                default: ''
            },
            isStudent: {
                type: Boolean,
                default: false
            },
            forms: {
                type: Array,
                default: function () {
                    return []
                }
            },
            regTypes: {
                type: Array,
                default: function () {
                    return []
                }
            },
            hasUsers: {
                type: Boolean,
                default: true
            },
            roles: {
                type: Array,
                default: function () {
                    return []
                }
            },
            listForm: {
                type: Array,
                default: true
            },
            users: {
                type: Array,
                default: function () {
                    return []
                }
            },
            taskTypes: {
                type: Array,
                default: function () {
                    return []
                }
            },
            hasCondition: {
                type: Boolean,
                default: false
            },
            icon: {
                type: String,
                default: '/images/upload-default-image100X100.png'
            }
        },
        data: function () {
            return {
                open: true
            }
        },
        filters: {
            ftpHttp: function (val) {
                return val ? (ftpHttp + val.replace('/tool', '')) : '/images/upload-default-image100X100.png';
            }
        },
        watch: {
            'userTask.name': function (value) {
                this.$emit('change-node-name', value)
            }
        },

        computed: {
          selectedStudent: function(){
              var roleIds = this.userTask.roleIds;
              if(!roleIds){
                  return false;
              }
              for(var i = 0; i < roleIds.length; i++){
                  if(this.studentId === roleIds[i].id){
                      return true;
                  }
              }
              return false;
          }
        },
        methods: {
            changeNodeName: function () {

            },
            openUploadModal: function () {
                var nodeKey = this.userTask.nodeKey;
                this.$emit('open-modal', nodeKey.replace(/^\w{1}/i, function ($1) {
                    return $1.toLowerCase()
                }))
            },
            filterRegType: function () {
                this.$emit('filter-reg-type', this.userTask.regType)
            },
            parseLabel: function (type, $event) {
                this.$emit('parse-label', {type: type, $target: $($event.target)})
            },
            leaveLabel: function () {
                this.$emit('leave-label')
            }
        },
        mounted: function () {

        }
    })

}(jQuery, Vue);