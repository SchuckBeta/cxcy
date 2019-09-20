<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcyjd.jsp" %>
    <%@include file="/WEB-INF/views/include/treetable.jsp" %>
    <script type="text/javascript" src="${ctxStatic}/vue/vue.min.js"></script>
    <script type="text/javascript" src="${ctxJs}/dr/selectMultiple.component.js?v=11111"></script>
    <style type="text/css">

        .table-devices_door {
            margin: 0;
        }

        .table-devices {
            min-width: 850px;
        }

        .table-devices .form-control-edit_name {
            margin: 0;
            height: 26px;
            line-height: 20px;
            width: 164px;
            box-sizing: border-box;
            padding: 2px 10px;
            font-size: 12px;
        }

        .table-devices label.error {
            text-align: left;
            width: 164px;
            display: inline-block;
        }

        .table-devices_door > tbody > tr > td:first-child {
            border-left: 0;
        }

        .table-devices .device-selected {
            width: 180px;
            margin: 0 auto;
        }

        .table-devices .table tr:hover > td {
            background-color: #fff;
        }

        .table-devices .device-selected input {
            display: block;
            font-size: 12px;
            width: 100%;
            height: 26px;
            margin: 0;
            padding: 3px 5px;
            line-height: 20px;
            -webkit-box-sizing: border-box;
            box-sizing: border-box;
        }

        .select-multiple .smu-list li a.disabled {
            background-color: #ddd;
            color: #666;
            cursor: default;
        }

        .table-devices .select-multiple .smu-list {
            max-height: 244px;
            overflow-x: hidden;
            overflow-y: auto;
        }

        .table-devices .selected-list {
            display: none;
        }

        .table-devices .selectedMultiple .smu-header {
            padding: 0;
            border: none;
        }

        .table-devices .devices-door-row {
            white-space: nowrap;
            overflow: hidden;
        }

        .table-devices .devices-door-row + div {
            margin-top: 8px;
        }

        .table-devices .devices-door-row .name {
            display: block;
            float: left;
            width: 82px;
            padding-top: 5px;
            height: 20px;
            line-height: 20px;
        }

        .table-devices .devices-door-row .door-group {
            vertical-align: middle;
            margin-left: 8px;
            margin-right: 100px;
            white-space: nowrap;
            overflow: hidden;

        }

        .table-devices .devices-door-row .radio, .table-devices .devices-door-row .checkbox {
            font-size: 12px;
            width: 80px;
            overflow: hidden;
        }

        .table-devices .btn-group {
            float: right;
        }

        .table-devices .door-group .checkbox-box {
            position: relative;
            float: left;
            padding-right: 24px;
            margin-right: 5px;
        }

        .table-devices .door-group .change-door_name {
            display: none;
            position: absolute;
            right: 0;
            top: 0;
            padding-top: 5px;
            height: 20px;
        }

        .table-devices .door-group .checkbox-box:hover .change-door_name {
            display: block;
        }
    </style>
</head>
<body>
<div id="linkDevice" class="container-fluid">
    <div class="edit-bar clearfix">
        <div class="edit-bar-left">
            <span>门禁设备配置</span> <i class="line weight-line"></i>
        </div>
    </div>
    <div class="text-right mgb-20">
        <button type="button" class="btn btn-primary" :disabled="isSaving" @click="saveDeviceDoor">保存</button>
        <a href="${ctx}/dr/drEquipment/list" class="btn btn-default">返回</a>
    </div>
    <table v-tree-table style="display: none"
           class="table table-bordered table-condensed table-hover table-orange table-nowrap table-center table-devices">
        <thead>
        <tr>
            <th class="text-left" width="240">基地结构</th>
            <th>已关联设备</th>
            <th width="186">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(build, index) in buildings" :id="build.id" :pId="build.parentId == '1' ? 0 : build.parentId">
            <td class="text-left">{{build.name}}</td>
            <td class="text-left " style="padding: 0;white-space: nowrap">
                <table class="table table-nowrap table-condensed table-center table-devices_door">
                    <tbody>
                    <tr v-for="(device, index2) in build.devices">
                        <template v-if="index2 == 0">
                            <td width="120" :rowspan="build.devices.length">
                                {{device.name}}
                            </td>
                        </template>
                        <td>
                            <edit-name :device="device" :editing="device.edit"
                                       @remove-door="removeDoor(build.id, device)"></edit-name>
                        </td>
                        <%--<td width="120">--%>
                        <%--<div class="btn-group">--%>
                        <%--<button type="button" class="btn btn-small btn-primary"--%>
                        <%--@click.stop="openEdit(build, device, index2, $event)">编辑--%>
                        <%--</button>--%>
                        <%--<button type="button" class="btn btn-small btn-primary" @click.stop="saveDeviceDoor(device, $event)">保存</button>--%>
                        <%--<button type="button" class="btn btn-small btn-default"--%>
                        <%--@click.stop="removeDoor(build.id, device, $event)">移除--%>
                        <%--</button>--%>
                        <%--</div>--%>
                        <%--</td>--%>
                        <%--<template v-if="index2 == 0">--%>
                        <%--<td width="64" :rowspan="build.devices.length">--%>

                        <%--</td>--%>
                        <%--</template>--%>
                    </tr>
                    </tbody>
                </table>
                <%--<div class="devices-door-row" v-for="(device, index2) in build.devices">--%>
                <%--<span class="name">{{device.name}}-{{device.doorName}}</span>--%>
                <%--<div class="btn-group">--%>
                <%--<button type="button" class="btn btn-small btn-primary">保存</button>--%>
                <%--<button type="button" class="btn btn-small btn-default">删除</button>--%>
                <%--</div>--%>
                <%--<div class="door-group">--%>
                <%--<div class="checkbox-box"  v-for="door in device.doors">--%>
                <%--<label class="checkbox inline">--%>
                <%--<input type="checkbox" :name="'door'+index+index2" :value="door.id" v-model="build.doors">{{door.name}}--%>
                <%--</label>--%>
                <%--<a class="change-door_name" href="javascript:void(0);" @click.stop="">改名</a>--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--</div>--%>
            </td>
            <td>
                <div v-if="build.type > 2 || typeof build.type === 'undefined'" class="device-selected">
                    <select-multiple :list="deviceList" :selected-list="build.devices" placeholder="请点击选择设备端口"
                                     :list-length="10000"
                                     less-list="没有更多设备端口了"
                                     @select="selectedDevices(build.id)"></select-multiple>
                </div>
            </td>
        </tr>
        </tbody>
    </table>
</div>
<div id="dialogCyjd" class="dialog-cyjd"></div>
<script type="text/x-template" id="edit-name-template">
    <div>
        <span>{{device.doorNo || device.doorName}} </span>-
        <input type="text" placeholder="请输入别名" autocomplete="off"
               name="editName" ref="editName"
               class="form-control-edit_name"
               v-model="editName" @blur="changeDoorName"
               @input="validateName" @focus="focusDoorName"
        >
        <a href="javascript:void(0);" @click.stop="removeDoor"><img src="${ctxImg}/btn-hover-delete-file.png"> </a>
        <div class="text-center">
            <label v-if="hasErr" class="error">{{err}}</label>
        </div>
    </div>
</script>
<script type="text/javascript">
    +function ($, Vue) {

        var doorRelationList = '${fns:toJson(doorRelationList)}' || '[]';
        var pwSpacelist = '${fns: toJson(pwSpacelist)}' || '[]';
        var deviceList = '${fns:toJson(doorList)}' || '[]';
        var editName = Vue.component('edit-name', {
            template: '#edit-name-template',
            model: {
                prop: 'device',
                event: 'change'
            },
            props: {
                device: {
                    type: Object,
                    default: function () {
                        return {}
                    }
                }
            },
            data: function () {
                return {
                    hasErr: false,
                    err: '',
                    editName: ''
                }
            },
            methods: {
                validateEditName: function (name) {
                    if (!name) {
                        return {
                            valid: true,
                        }
                    } else if (name.length > 30) {
                        return {
                            valid: false,
                            maxlength: '名称字数不能超过30位',
                            key: 'maxlength',
                        }
                    } else if (name.length < 2) {
                        return {
                            valid: false,
                            minlength: '名称字数不要小于两位数',
                            key: 'minlength',
                        }
                    }
                    return {
                        valid: true
                    };
                },
                validateName: function () {
                    var validate = this.validateEditName(this.editName);
                    if (!validate.valid) {
                        this.hasErr = true;
                        this.err = validate[validate['key']]
                        this.device.valid = false;
                        return false;
                    }
                    this.hasErr = false;
                    this.device.valid = true;
                    return true;
                },
                focusDoorName: function () {
                    this.validateName();
                },
                changeDoorName: function () {
                    if (!this.validateName()) {
                        return false;
                    }
                    this.device.doorOtherName = this.editName;
                },
                removeDoor: function () {
                    this.$emit('remove-door')
                }
            },
            mounted: function () {
                this.editName = this.device.doorOtherName;
                this.device.valid = true;
            }

        });


        var linkDevice = new Vue({
            el: '#linkDevice',
            data: function () {
                return {
                    isSaving: false,
                    deviceList: JSON.parse(deviceList),
                    doorRelationList: JSON.parse(doorRelationList),
                    buildList: [],
                    buildings: JSON.parse(pwSpacelist)
                }
            },
            computed: {},
            directives: {
                treeTable: {
                    inserted: function (element, binding, vnode) {
                        $(element).treeTable({expandLevel: 10}).show();
                    }
                }
            },
            methods: {
                sortBuildings: function () {
//                    this.linkTree();
                    this.formatTreeList('1');
                    this.buildings = this.buildList;
                },

                linkTree: function () {
                    var buildings = this.buildings;
                    var doorRelationList = this.doorRelationList;

                    for (var i = 0; i < buildings.length; i++) {
                        var build = buildings[i];
                        if (build.type > 2 || typeof build.type === 'undefined') {
                            Vue.set(build, 'devices', []);
                        }
                        for (var d = 0; d < doorRelationList.length; d++) {
                            var item = doorRelationList[d];
                            var id = item.roomId;
                            if (id === build.id) {
                                buildings[i].devices.push(item);
                            }
                        }
                    }
                },

                linkDeviceList: function () {
                    var doorRelationList = this.doorRelationList;
                    var devices = this.deviceList;
                    for (var i = 0; i < doorRelationList.length; i++) {
                        var doorRelation = doorRelationList[i];
                        for (var d = 0; d < devices.length; d++) {
                            var device = devices[d];
                            if (device.id === doorRelation.drEquipmentId && device.doorPort === doorRelation.doorPort) {
                                device.disabled = true;
                                doorRelation.valid = true;
                                doorRelation.name = device.name;
                                doorRelation.disabled = true;
                                doorRelation.id = device.id;
                            }
                        }
                    }
                },

                formatTreeList: function (rootId) {
                    var list = this.buildings;

                    for (var i = 0; i < list.length; i++) {
                        var item = list[i];
                        if (item.parentId === rootId) {
                            this.buildList.push(item);
                            this.formatTreeList(item.id);
                        }
                    }
                },

                selectedDevices: function (id) {
                },


                openEdit: function (build, device, idx, $event) {
                    for (var i = 0; i < build.devices.length; i++) {
                        Vue.set(build.devices[i], 'edit', (device.id == build.devices[i].id && device.doorPort === build.devices[i].doorPort))
                    }
                },


                delDoorDevice: function (buildId, device) {
                    var build = this.getBuild(buildId);
                    var devices = build.devices;
                    var deviceID = device.id;
                    var deviceDoorPort = device.doorPort;


                    for (var i = 0; i < devices.length; i++) {
                        var deviceItem = devices[i];
                        if (deviceItem.id === deviceID && deviceDoorPort === deviceItem.doorPort) {
                            this.getDevice(deviceID, deviceDoorPort).disabled = false;
                            devices.splice(i, 1);
                            break;
                        }
                    }
                },

                //删除设备
                removeDoor: function (buildId, device, $event) {
                    var roomId = device.roomId;
                    var self = this;
                    if (!roomId) {
                        this.delDoorDevice(buildId, device);
                        return;
                    }
                    closeTip();
                    showTip('删除中...', 'info')
                    $.ajax({
                        type: 'POST',
                        url: '/a/dr/drEquipmentRspace/ajaxDeleDoorRelation ',
                        data: {
                            id: roomId
                        },
                        success: function (data) {
                            closeTip();
                            if (data.ret == '1') {
                                self.delDoorDevice(buildId, device);
                                showTip(data.msg || '删除成功', 'success')
                                return
                            }
                            showTip(data.msg || '删除失败', 'fail')
                        },
                        error: function (error) {
                            closeTip();
                            showTip('网络连接失败，请重试', 'fail')
                        }
                    })
                },


                getBuild: function (id) {
                    var item;
                    for (var i = 0; i < this.buildings.length; i++) {
                        if (this.buildings[i].id === id) {
                            item = this.buildings[i];
                            break;
                        }
                    }
                    return item;
                },


                getDevice: function (id, doorPort) {
                    var item;
                    for (var i = 0; i < this.deviceList.length; i++) {
                        if (this.deviceList[i].id === id && this.deviceList[i].doorPort === doorPort) {
                            item = this.deviceList[i];
                            break;
                        }
                    }
                    return item;
                },

                //获取发送数据
                getPostData: function () {
                    var buildings = this.buildings;
                    var list = [];
                    var valid = true;
                    for (var i = 0; i < buildings.length; i++) {
                        var build = buildings[i];
                        var devices = build.devices;
                        if (!devices || devices.length < 1) {
                            continue;
                        }
                        if (!valid) {
                            break;
                        }
                        for (var d = 0; d < devices.length; d++) {
                            if (devices[d].disabled) {
                                if (!devices[d].valid) {
                                    valid = false;
                                    break;
                                }
                                list.push({
                                    drEquipmentId: devices[d].id,
                                    rspaceId: build.id,
                                    type: build.type ? '1' : '2',
                                    doorName: devices[d].doorName,
                                    doorOtherName: devices[d].doorOtherName,
                                    doorPort: devices[d].doorPort,
                                    doorNo: devices[d].no
                                })
                            }
                        }
                    }
                    return {
                        valid: valid,
                        list: list
                    };
                },

                //保存数据
                saveDeviceDoor: function () {
                    var postData = this.getPostData();
                    var self = this;
                    if (!postData.valid) {
                        dialogCyjd.createDialog(1, '关联设备别名不合格，请检查')
                        return false;
                    }
                    this.isSaving = true;
                    $.ajax({
                        type: 'POST',
                        url: '/a/dr/drEquipmentRspace/ajaxSaveDoorRelation',
                        data: JSON.stringify({deviceList: postData.list}),
                        contentType: 'application/json;charset=utf-8',
                        success: function (data) {
                            closeTip();
                            self.isSaving = false;
                            if (data.ret == 1) {
                                dialogCyjd.createDialog(1, data.msg || '保存成功', {
                                    buttons: [{
                                        text: '确定',
                                        'class': 'btn btn-primary',
                                        'click': function () {
                                            $(this).dialog('close')
                                        }
                                    }, {
                                        text: '返回',
                                        'class': 'btn btn-default',
                                        'click': function () {
                                            $(this).dialog('close')
                                            location.href = '/a/dr/drEquipment/list'
                                        }
                                    }]
                                })
//                                location.reload();
                                return false;
                            }
                            dialogCyjd.createDialog(0, data.msg || '保存失败', {
                                buttons: [{
                                    text: '重新保存',
                                    'class': 'btn btn-primary',
                                    'click': function () {
                                        $(this).dialog('close')
                                        self.saveDeviceDoor();
                                    }
                                }, {
                                    text: '取消',
                                    'class': 'btn btn-default',
                                    'click': function () {
                                        $(this).dialog('close')
                                    }
                                }]
                            })
                        },
                        error: function (error) {
                            dialogCyjd.createDialog(0, '网络连接失败', {
                                buttons: [{
                                    text: '重新保存',
                                    'class': 'btn btn-primary',
                                    'click': function () {
                                        $(this).dialog('close')
                                        self.saveDeviceDoor();
                                    }
                                }, {
                                    text: '取消',
                                    'class': 'btn btn-default',
                                    'click': function () {
                                        $(this).dialog('close')
                                    }
                                }]
                            })
                            self.isSaving = false;
                        }
                    })
                }
            },
            beforeMount: function () {
                this.linkDeviceList();
                this.linkTree();
                this.sortBuildings();

            },
            mounted: function () {
            }
        })
    }(jQuery, Vue)


</script>
</body>
</html>