+function ($, Vue) {
    var roomElementGroup = Vue.component('room-element-group', {
        template: '<div class="group" :class="[{groupOpened: visibleIndex === index}]">\n' +
        '        <h4 class="group-label"><i class="iconfont iconfont-open" @click.stop="handleChangeIndex"></i><i class="iconfont iconfont-close" @click.stop="handleChangeIndex"></i>{{title}}\n' +
        '        </h4>\n' +
        '        <div class="shape-elements" v-show="visibleIndex === index">\n' +
        '            <div class="room-element" v-if="namesMap[element.name]" v-for="element in roomElements" :key="element.id"\n' +
        '                 :ref="element.id"\n' +
        '                 draggable="false"\n' +
        '                 @mousedown.stop="handleMousedown(element, $event)">\n' +
        '                <div class="room-element_pic" draggable="false">\n' +
        '                    <img :src="element.base64Code" draggable="false">\n' +
        '                </div>\n' +
        '                <span class="room-element_name" draggable="false">{{namesMap[element.name]}}</span>\n' +
        '            </div>\n' +
        '        </div>\n' +
        '    </div>',
        props: {
            title: String,
            roomElements: [Object, Array],
            visibleIndex: {
                type: Number,
                default: 0
            },
            index: {
                type: Number,
            },
        },
        data: function () {
            return {
                open: true,
                namesMap: {
                    basin: '盆栽',
                    chair: '椅子',
                    circleTable: '会议桌',
                    door: '单门',
                    workTable: '办公桌',
                    window2: '窗户',
                    rectEightTable: '会议桌',
                    fourMeetingTable: '会议桌',
                    fourSofa: '四人沙发',
                    // treefloor: '地板',
                    threeSofa: '三人沙发',
                    twoSofa: '双人沙发',
                    oneSofa: '沙发',
                    rectMeetingTable: '会议桌',
                    meetingTableBig: '会议桌',
                    'window': '窗户',
                    workTable2: '办公桌',
                    doubeldoor: '双门'
                }
            }
        },
        methods: {
            handleChangeIndex: function () {
                this.$emit('change-index', {
                    visibleIndex: this.visibleIndex,
                    index: this.index
                })
            },
            handleMousedown: function (element, ev) {
                var self = this;
                var startX = ev.clientX,
                    startY = ev.clientY,
                    $target = $(this.$refs[element.id]),
                    offset = $target.offset(),
                    img = $target.find('img')[0],
                    imgWidth = img.naturalWidth,
                    imgHeight = img.naturalHeight,
                    name = this.namesMap[element.id];
                var startData = {
                    startX: startX,
                    startY: startY,
                    offset: offset,
                    name: name,
                    element: element
                };
                var moveData;

                this.$emit('mousedown', startData);

                $(document).on('mousemove', function (e) {
                    e.stopPropagation();
                    var x = e.clientX, y = e.clientY;
                    moveData = {dx: x - startX, dy: y - startY, offset: offset, element: element, name: name};
                    self.$emit('mousemove', moveData);
                });

                $(document).on('mouseup', function (e) {
                    e.stopPropagation();
                    $(document).off('mousemove');
                    $(document).off('mouseup');
                    self.$emit('mouseup', {
                        element: element,
                        name: name,
                        imgWidth: imgWidth,
                        imgHeight: imgHeight,
                        e: e
                    });
                })

            }
        }
    });
}(jQuery, Vue)