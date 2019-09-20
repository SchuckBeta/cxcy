<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${fns:getConfig('frontTitle')}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>
    <link rel="stylesheet" href="${ctxStatic}/fullcalendar/fullcalendar.min.css">
    <link rel="stylesheet" href="${ctxStatic}/fullcalendar/scheduler.min.css">
    <script src="${ctxStatic}/fullcalendar/fullcalendar.min.js"></script>
    <script src="${ctxStatic}/fullcalendar/scheduler.min.js"></script>
    <script src="${ctxStatic}/fullcalendar/locale/zh-cn.js"></script>

</head>
<body>
<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none; position: static">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a href="${ctxFront}"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item>创业基地</el-breadcrumb-item>
        <el-breadcrumb-item>预约申请</el-breadcrumb-item>
    </el-breadcrumb>
    <el-form :model="searchListForm" :rules="searchListFormRules" ref="searchListForm" size="mini"
             :show-message="false">
        <div class="conditions">
            <e-condition type="checkbox" v-model="searchListForm.roomTypes" label="房间类型" :options="pwRoomTypes"
                         @change="handleChangeRoomType"
                         name="roomTypes"></e-condition>
            <e-condition type="checkbox" v-model="searchListForm.status" label="房间状态" :options="pwAppointmentStatuses"
                         @change="handleChangeStatus"
                         name="status"></e-condition>
            <e-condition type="radio" v-model="searchListForm.buildingId" label="楼栋" :options="buildingList"
                         @change="handleChangeBuilding"
                         name="build" :default-props="{label: 'name', value: 'id'}"></e-condition>
            <e-condition type="radio" v-model="searchListForm.floorId" label="楼层" :options="floorList"
                         @change="handleChangeFloor"
                         name="build" :default-props="{label: 'name', value: 'id'}"></e-condition>
            <%--<e-condition type="radio" v-model="searchListForm.roomIds" label="房间" name="room" :options="roomList"--%>
            <%--@change="handleChangeRoom"--%>
            <%--:default-props="{label: 'name', value: 'id'}"></e-condition>--%>
        </div>
        <div class="search-block_bar clearfix">
            <div class="search-input" style="width: 300px;">
                <el-form-item style="margin-bottom: 0">
                    <el-col :span="9">
                        <el-form-item prop="roomNumMin" style="margin-bottom: 0">
                            <el-input v-model="searchListForm.roomNumMin" placeholder="最小容纳人数"></el-input>
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <span class="divide-zhi">-</span>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item prop="roomNumMax" style="margin-bottom: 0">
                            <el-input v-model="searchListForm.roomNumMax" placeholder="最大容纳人数">
                                <el-button slot="append" icon="el-icon-search"
                                           @click.stop.prevent="searchWeek"></el-button>
                            </el-input>
                        </el-form-item>
                    </el-col>
                </el-form-item>
            </div>
        </div>
    </el-form>
    <div class="table-container" style="padding: 15px">
        <div v-loading="loadingCalendar">
            <div v-if="fullCalendarLoad && isUserStu" v-full-calendar-day="fullCalendarOption"></div>
            <template v-else>
                <p class="empty empty-color pdt-20" style="margin-bottom: 0">导师不能预约房间</p>
            </template>
        </div>
    </div>

    <el-dialog
            :title="appointmentDialogTitle"
            :visible.sync="appointmentDialogVisible"
            :close-on-click-modal="false"
            width="540px"
            :before-close="handleCloseAppointmentDialog">
        <el-form :model="appointmentForm" ref="appointmentForm" :rules="appointmentRules" size="mini" label-width="96px"
                 :disabled="appointmentDisabled">
            <el-form-item v-if="!isAdmin" label="申请人：">
                <p class="el-form-item-content_static">{{user.name}}</p>
            </el-form-item>
            <el-form-item label="房间：">
                <p class="el-form-item-content_static">{{appointmentForm.roomName}}</p>
            </el-form-item>
            <el-form-item label="可容纳人数：">
                <p class="el-form-item-content_static">{{appointmentForm.capacity}}人</p>
            </el-form-item>
            <el-form-item label="预约日期：">
                <p class="el-form-item-content_static">{{appointmentForm.day}}</p>
            </el-form-item>
            <el-form-item label="预约时间段：">
                <p class="el-form-item-content_static">
                    {{appointmentForm.startTimeStr}}至{{appointmentForm.endTimeStr}}</p>
            </el-form-item>
            <el-form-item v-if="!isAdmin" prop="appointmentstyle" label="预约形式：">
                <el-select v-model="appointmentForm.appointmentstyle">
                    <el-option value="1" label="个人"></el-option>
                    <el-option value="2" label="团队"></el-option>
                    <el-option value="3" label="企业"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item v-if="!isAdmin" prop="personNum" label="参与人数：">
                <el-input-number v-model="appointmentForm.personNum" :min="1" :max="100000"></el-input-number>
            </el-form-item>
            <el-form-item prop="subject" label="用途：">
                <el-input type="textarea" v-model="appointmentForm.subject" :rows="3"
                          placeholder="请输入1-50之间字符"></el-input>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="handleCloseAppointmentDialog" size="mini">取 消</el-button>
            <el-button :disabled="appointmentDisabled" type="primary" @click="submitAppointmentForm" size="mini">确 定
            </el-button>
        </div>
    </el-dialog>
    <div v-show="tooltipShow" class="tooltip bottom" role="tooltip" :style="toolStyle"
         style="opacity: 0; display: none">
        <div class="tooltip-arrow"></div>
        <div class="tooltip-inner">
            <span style="display: block;white-space: pre" v-html="toolTitle"></span>
        </div>
    </div>
</div>

<script type="text/javascript">

    +function () {


        'use strict';

        Vue.directive('full-calendar-day', {
            inserted: function (element, binding, vnode) {
                var option = binding.value;
                var frontOrAdmin = option.frontOrAdmin;
                var eventList = option.eventList;
                var appRule = option.appRule;
                var appDayListIndex = appRule.isAppDayList.indexOf('7');
                if(appDayListIndex > -1){
                    appRule.isAppDayList.splice(appDayListIndex, 1, '0')
                }
                var isAppDayList =  appRule.isAppDayList;
                vnode.context.fullCalendar = $(element).fullCalendar({
                    defaultDate: option.now,
                    defaultView: 'timelineDay',
                    views: {
                        timelineThreeDays: {
                            type: 'timeline',
                            duration: {days: 3}
                        }
                    },
                    resourceLabelText: '房间名',
                    resources: option.resources,
                    aspectRatio: 4,
                    slotDuration: '00:30:00',
                    slotLabelFormat: 'HH:mm',
                    minTime: appRule.isTimeAuto == '1' ? '00:00:00' : appRule.beginTime,
                    maxTime: appRule.isTimeAuto == '1' ? '24:00:00' : appRule.endTime,
                    schedulerLicenseKey: 'GPL-My-Project-Is-Open-Source',
                    header: {
                        left: 'prev,next tButton',
                        center: 'title',
                        right: 'mButton,wButton,timelineDay'
                    },
                    customButtons: {
                        mButton: {
                            text: '月',
                            click: function () {
                                location.href = frontOrAdmin + '/pw/pwAppointment/viewMonth'
                            }
                        },
                        wButton: {
                            text: '周',
                            click: function () {
                                location.href = frontOrAdmin + '/pw/pwAppointment/viewWeek'
                            }
                        },
                        tButton: {
                            text: '今天',
                            click: function () {
                                $(element).fullCalendar('gotoDate', option.now)
                            }
                        }
                    },
                    allDaySlot: false,
                    navLinks: false, // can click day/week names to navigate views
                    height: 'auto',
                    selectable: true,
                    businessHours: {
                        dow: isAppDayList,
                        start: appRule.isTimeAuto == '1' ? '00:00:00' : appRule.beginTime,
                        end: appRule.isTimeAuto == '1' ? '24:00:00' : appRule.endTime
                    }, // display business hours锁定工作日
                    selectConstraint: 'businessHours',
                    events: eventList,
                    select: option.select,
                    selectOverlap: function (event) {
                        return event.rendering === '#ff9f89';
                    },
                    eventRender: option.eventRender,
                    eventClick: option.eventClick
                })
            }
        });

        var App = new Vue({
            el: '#app',
            data: function () {
                var pwRoomTypes = JSON.parse('${fns:toJson(fns:getDictList('pw_room_type'))}');
                var pwAppointmentStatuses = JSON.parse('${fns: toJson(fns:getDictList('pw_appointment_status'))}');
                var appList = JSON.parse(JSON.stringify(${fns:toJson(list)})) || [];
                var appRule = JSON.parse(JSON.stringify(${fns:toJson(appRule)})) || {};
                var validatorNumber = function (rule, value, callback) {
                    if (value) {
                        if (!(/^\d{1,}$/.test(value))) {
                            return callback(new Error('请输入数字'))
                        } else {
                            if (value > 99999) {
                                return callback(new Error('请输入小于100000数字'))
                            }
                            callback()
                        }
                    }
                    callback()
                };


                return {
                    appList: appList,
                    appRule: appRule,
                    user: JSON.parse(JSON.stringify(${fns:toJson(fns:getUser())})),
                    isAdmin: ${isAdmin},
                    isUserComplete: true,
                    now: '${now}',
                    pwRoomTypes: pwRoomTypes,
                    pwAppointmentStatuses: pwAppointmentStatuses,
                    rooms: JSON.parse(JSON.stringify(${fns:toJson(rooms)})),
                    appointmentForm: {
                        startDate: '',
                        endDate: '',
                        roomName: '',
                        roomId: '',
                        capacity: '',
                        subject: '',
                        personNum: '',
                        remarks: '',
                        day: '',
                        startTimeStr: '',
                        endTimeStr: '',
                        appointmentstyle: ''
                    },
                    appointmentRules: {
                        subject: [
                            {required: true, message: '请输入用途', trigger: 'blur'},
                            {max: 50, message: '请输入1-50之间字符', trigger: 'blur'}
                        ],
                        personNum: [{required: true, message: '请输入参与人数', trigger: 'change'}],
                        appointmentstyle: [
                            {required: true, message: '请选择预约形式', trigger: 'change'}
                        ]
                    },
                    appointmentDialogVisible: false,
                    appointmentDisabled: false,
                    loadingCalendar: false,
                    pwSpaceList: [],
                    fullCalendar: {},
                    fullCalendarOption: {},
                    fullCalendarLoad: false,
                    searchListForm: {
                        roomTypes: [],
                        buildingId: '',
                        floorId: '',
                        roomNumMin: '',
                        roomNumMax: '',
                        status: [],
                        roomIds: ''
                    },
                    searchListFormRules: {
                        roomNumMin: [
                            {validator: validatorNumber, trigger: 'blur'}
                        ],
                        roomNumMax: [
                            {validator: validatorNumber, trigger: 'blur'}
                        ]
                    },
                    tooltipShow: false,
                    toolStyle: {},
                    toolTitle: ''
                }
            },
            computed: {
                isUserStu: function () {
                    return this.loginCurUser.userType === '1'
                },
                buildingList: {
                    get: function () {
                        return this.pwSpaceList.filter(function (item) {
                            return item.type == '3';
                        })
                    }
                },
                floorList: {
                    get: function () {
                        var buildingId = this.searchListForm.buildingId;
                        return this.pwSpaceList.filter(function (item) {
                            return item.pId === buildingId;
                        })
                    }
                },
                roomList: {
                    get: function () {
                        var floorId = this.searchListForm.floorId;
                        if (!floorId) {
                            return this.rooms;
                        }
                        return this.rooms.filter(function (item) {
                            return item.pwSpace.id === floorId;
                        })
                    }
                },
                roomPath: {
                    get: function () {
                        var roomId = this.searchListForm.roomIds;
                        if (!roomId) {
                            return '全部场地'
                        }
                        for (var i = 0; i < this.rooms.length; i++) {
                            var roomItem = this.rooms[i];
                            if (roomId === roomItem.id) {
                                return roomItem.path;
                            }
                        }
                    }
                },
                postParams: {
                    get: function () {
                        var params = {};
                        var searchListForm = this.searchListForm;
                        for (var k in searchListForm) {
                            if (searchListForm.hasOwnProperty(k)) {
                                if (typeof searchListForm[k] == 'undefined') {
                                    continue;
                                }
                                if ({}.toString.call(searchListForm[k]) === '[object Array]') {
                                    if (searchListForm[k].length > 0) {
                                        params[k] = searchListForm[k].join(',')
                                    }
                                } else {
                                    params[k] = searchListForm[k]
                                }
                            }
                        }
                        return params
                    }
                },
                selectedRoom: {
                    get: function () {
                        var roomId = this.searchListForm.roomIds;
                        for (var i = 0; i < this.rooms.length; i++) {
                            if (roomId === this.rooms[i].id) {
                                return this.rooms[i];
                            }
                        }
                        return false;
                    }
                },
                appointmentDialogTitle: {
                    get: function () {
                        return this.isAdmin ? '锁定房间' : '预约房间';
                    }
                }
            },

            watch: {
                appointmentDialogVisible: function (value) {
                    var selectedRoom = this.selectedRoom;
                    if (!value) {
                        return
                    }
                    var startDate = this.appointmentForm.startDate;
                    var endDate = this.appointmentForm.endDate;
                    var day = moment(startDate).format('YYYY-MM-DD');
                    var startTimeStr = moment(startDate).format('HH:mm');
                    var endTimeStr = moment(endDate).format('HH:mm');
                    this.appointmentForm.roomName = selectedRoom.name;
                    this.appointmentForm.roomId = selectedRoom.id;
                    this.appointmentForm.capacity = selectedRoom.num;
                    this.appointmentForm.day = day;
                    this.appointmentForm.startTimeStr = startTimeStr;
                    this.appointmentForm.endTimeStr = endTimeStr;
                    this.appointmentForm['pwRoom.id'] = selectedRoom.id;
                }
            },

            methods: {

                searchWeek: function () {
                    var self = this;
                    this.loadingCalendar = true;
                    this.$axios({
                        url: '/pw/pwAppointment/weekSearch',
                        method: 'GET',
                        timeout: 5 * 1000,
                        params: this.postParams
                    }).then(function (response) {
                        var data = response.data;
                        self.changeCalendarResource(data.rooms);
                        self.changeCalendarEventList(data.list);
                        self.loadingCalendar = false;
                    }).catch(function () {
                        self.loadingCalendar = false;
                    })
                },

                getFullCalendarOption: function () {
                    var self = this;
                    var eventList = this.getEventList();
                    var resources = this.rooms.map(function (item) {
                        return {
                            eventColor: '#' + item.color,
                            title: item.path,
                            id: item.id
                        }
                    });
                    return {
                        now: moment(this.now).format('YYYY-MM-DD'),
                        frontOrAdmin: this.frontOrAdmin,
                        resources: resources,
                        appRule: this.appRule,
                        eventRender: function (event, element) {
                            if (event.rendering !== 'background') {
                                element.append('<div class="fc-close" style="position: absolute;right: 0;top: 0; cursor: pointer; z-index: 100"><i class="el-icon el-icon-close el-icon-fc-close"></i></div>')
                            }
                        },
                        select: function (start, end, jsEvent, view, resource) {
                            var dateNowTime = new Date().getTime();
                            var startTime, startDate, endDate;
                            startDate = start.format('YYYY-MM-DD HH:mm:ss');
                            endDate = end.format('YYYY-MM-DD HH:mm:ss');
                            startTime = new Date(startDate).getTime();
                            if (dateNowTime - startTime > 0) {
                                self.$alert('请预约未来时间', '提示', {
                                    type: 'error'
                                });
                                return;
                            }
                            self.searchListForm.roomIds = resource.id;
                            self.appointmentForm.startDate = startDate;
                            self.appointmentForm.endDate = endDate;
                            self.appointmentDialogVisible = true;
                        },
                        eventClick: function (date, event, jsEvent, view) {
                            var id = date.id;
                            if (event.target.className.indexOf('el-icon-fc-close') == -1) return;
                            self.$confirm('是否取消此预约', '提示', {
                                confirmButtonText: '确定',
                                cancelButtonText: '取消',
                                type: 'warning'
                            }).then(function () {
                                self.cancelAppointment(id);
                            }).catch(function () {

                            })
                        },
                        eventList: eventList
                    }
                },

                cancelAppointment: function (id) {
                    var self = this;
                    this.$axios.post('/pw/pwAppointment/asyCancel?id=' + id).then(function (response) {
                        var data = response.data;
                        var msg = '取消' + (data.success ? '成功' : '失败');
                        if (self.checkUserLogin(data)) {
                            return;
                        }
                        if (data.success) {
                            self.fullCalendar.fullCalendar('removeEvents', data.id);
                        }
                        self.show$message({
                            status: data.success,
                            msg: data.success ? msg : data.msg
                        });

                    }).catch(function (error) {
                        self.show$message({
                            status: false,
                            msg: error.response.data
                        });
                    })
                },

                getEventList: function (list) {
                    var nList = list || this.appList;
                    var self = this;
                    return nList.map(function (item) {
                        return self.assignFullCalendarEvent(item);
                    });
                },

                assignFullCalendarEvent: function (event) {
                    var isUser = event.user.id === this.user.id;
                    var nEvent = {
                        id: event.id,
                        start: event.startDate,
                        end: event.endDate,
                        className: 'test',
                        resourceId: event.pwRoom.id
                    };

                    if (isUser) {
                        nEvent.color = '#' + event.color;
                        nEvent.title = '用途：' + event.subject + '\n预约人：' + event.user.name + '';
                    } else {
                        nEvent.overlap = false;
                        nEvent.rendering = 'background';
                        nEvent.color = '#d7d7d7'
                    }
                    return nEvent;
                },

                handleCloseAppointmentDialog: function () {
                    this.$refs.appointmentForm.resetFields();
                    this.$nextTick(function () {
                        this.appointmentDialogVisible = false;
                    })
                },

                submitAppointmentForm: function () {
                    var self = this;
                    this.$refs.appointmentForm.validate(function (valid) {
                        if (valid) {
                            self.postAppointmentXhr();
                        }
                    })
                },
                postAppointmentXhr: function () {
                    var self = this;
                    this.appointmentDisabled = true;
                    this.$axios({
                        method: 'POST',
                        url: '/pw/pwAppointment/asySave',
                        params: this.appointmentForm
                    }).then(function (response) {
                        var data = response.data;
                        var msg = self.appointmentDialogTitle + (data.success ? '申请已提交' : '申请失败');
                        if (self.checkUserLogin(data)) {
                            self.appointmentDialogVisible = false;
                            return;
                        }
                        if (data.success) {
                            self.renderEvent(data.id);
                            self.handleCloseAppointmentDialog();
                        }
                        self.show$message({
                            status: data.success,
                            msg: data.success ? msg : data.msg
                        });
                        self.appointmentDisabled = false;
                    }).catch(function (error) {
                        self.appointmentDisabled = false;
                        self.show$message({
                            status: false,
                            msg: error.response.data
                        });
                    })
                },

                renderEvent: function (id) {
                    var eventData = {
                        id: id,
                        title: '用途：' + this.appointmentForm.subject + '\n预约人：' + this.user.name + '',
                        start: this.appointmentForm.startDate,
                        end: this.appointmentForm.endDate,
                        color: '#' + this.selectedRoom.color,
                        resourceId: this.selectedRoom.id
                    };
                    this.fullCalendar.fullCalendar('renderEvent', eventData, true);
                },


                handleChangeFloor: function () {
                    this.searchListForm.roomIds = '';
                    this.searchWeek();
                },

                handleChangeRoom: function (room) {
                    this.searchWeek();
                },


                handleChangeBuilding: function () {
                    this.searchListForm.floorId = '';
                    this.searchListForm.roomIds = '';
                    this.searchWeek();
                },

                handleChangeRoomType: function () {
                    this.searchWeek();
                },

                handleChangeStatus: function () {
                    this.searchWeek();
                },

                changeCalendarEventList: function (list) {
                    var eventList = this.getEventList(list);
                    this.fullCalendar.fullCalendar('removeEvents');
                    this.fullCalendar.fullCalendar('addEventSource', eventList)
                },

                changeCalendarResource: function (rooms) {
                    var self = this;
                    var resources = rooms.map(function (item) {
                        return {
                            eventColor: '#' + item.color,
                            title: item.path,
                            id: item.id
                        }
                    });
                    this.rooms.forEach(function (item) {
                        self.fullCalendar.fullCalendar('removeResource', item.id);
                    });
                    resources.forEach(function (item) {
                        self.fullCalendar.fullCalendar('addResource', item)
                    });
                    self.rooms = rooms;
                },

                getSpaceList: function () {
                    var self = this;
                    return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                        self.pwSpaceList = response.data;
                    })
                }


            },
            beforeMount: function () {
                var self = this;
                this.getSpaceList();
                var loginCurUser = self.loginCurUser;
                if(this.isUserStu){
                    this.getUserIsCompleted().then(function (response) {
                        var data = response.data;
                        if (data.status !== 1) {
                            self.disabled = true;
                            self.$msgbox({
                                type: 'error',
                                title: '提示',
                                closeOnClickModal: false,
                                closeOnPressEscape: false,
                                confirmButtonText: '确定',
                                showClose: false,
                                message: data.msg
                            }).then(function () {
                                if (loginCurUser.userType === '1') {
                                    location.href = '/f/sys/frontStudentExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';
                                } else {
                                    location.href = '/f/sys/frontTeacherExpansion/findUserInfoById?userId=' + loginCurUser.id + '&custRedict=1&isEdit=1';
                                }
                            }).catch(function () {
                            });
                        }
                    })
                }
            },
            created: function () {
                this.fullCalendarOption = this.getFullCalendarOption();
                this.fullCalendarLoad = true;
            },

            mounted: function () {
            }
        })

        $(document).on('mouseenter', '.fc-timeline-event .fc-content', function () {
            var time = $(this).find('.fc-time').text()
            var title = $(this).find('.fc-title').html()
            var width = $(this).width()
            var height = $(this).height();
            var left = $(this).offset().left;
            var top = $(this).offset().top;
            var toolWidth;
            App.$data.tooltipShow = true;
            App.$data.toolTitle = title;
            setTimeout(function () {
                toolWidth = $('.tooltip .tooltip-inner').width()
                App.$data.toolStyle = {
                    left: (left - (toolWidth - width) / 2) + 'px',
                    top: (top + height) + 'px',
                    opacity: 1
                }
            }, 0)
        }).on('mouseleave', '.fc-timeline-event .fc-content', function (e) {
            if ($(e.target).hasClass('tooltip') || $(e.target).parents('.tooltip').size() > 0) {
                return false;
            }
            App.$data.toolStyle.opacity = 0;
            App.$data.tooltipShow = false;
        })
    }();
</script>

</body>
</html>