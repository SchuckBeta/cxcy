<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="utf-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="site-decorator"/>
    <script src="https://unpkg.com/vue"></script>
    <script src="https://unpkg.com/vue-router@2.0.0/dist/vue-router.js"></script>
    <style>
        .time-line{
            position: relative;
            width: 600px;
            padding-top: 40px;
            padding-bottom: 40px;
            margin: 0 auto;
        }
        .time-line::before{
            content: '';
            position: absolute;
            top: 0;
            left: 50%;
            height: 100%;
            width: 4px;
            background: #d7e4ed;
            margin-left: -2px;
        }

        .time-line .time-item{
            position: relative;
        }
        .time-line .time-item .time-inner{
            position: relative;
            float: right;
            width: 230px;
            margin-bottom: 40px;
            margin-left: 20px;
            border: 1px solid #c3c3c3;
        }
        .time-line .time-item .title{
            position: relative;
            padding: 0 8px;
            background-color: #c3c3c3;
        }
        .time-line .time-item .title .arrow{
            position: absolute;
            left: -7px;
            top: 8px;
            border-top: 6px solid transparent;
            border-bottom: 6px solid transparent;
            border-right: 6px solid #c3c3c3;
        }
        .time-line .time-item.active .title .arrow{
            border-right: 6px solid #e9422d;
        }
        .time-line .time-item.active .time-inner{
            border: 1px solid #e9422d;
        }

        .time-line .time-item.active .title{
            color: #fff;
            background-color: #e9422d;
        }
        .time-line .time-quantum{
            position: absolute;
            left: 50%;
            top: 0;
            width: 62px;
            height: 62px;
            border-radius: 50%;
            margin-left: -31px;
            font-size: 12px;
            border: 1px solid #c3c3c3;
            text-align: center;
            background-color: #fff;
            vertical-align: middle;
        }
        .time-line .time-quantum .cell{
            display: table-cell;
            height: 60px;
            width: 1%;
            text-align: center;
            vertical-align: middle;
        }

        .time-line .time-box{
            padding: 5px 8px;
        }


    </style>
</head>
<body>
<div id="appTime">
    <div class="container" style="width: 1270px">
        <div>
            <div v-for="tab in tabList">
                <router-link :to="{path: tab.id, query: {type: tab.type, actYwId: tab.actYwId}}">{{tab.name}}
                </router-link>
            </div>
        </div>
        <router-view></router-view>
    </div>
</div>

<script type="text/x-template" id="projectTimeLine">
    <div class="time-line-container">
        <h4>{{proname}}</h4>
        <div class="clearfix">
            <div class="pull-right">
                <router-link :to="{path: projectId, query: {type: $route.query.type, actYwId: $route.query.actYwId}}"
                             class="btn">当前项目
                </router-link>
                <a href=""></a>
            </div>
            <div class="project-info">
                <span>项目申报时间：{{apply_time}}</span><span>项目编号：{{number}}</span><span>项目负责人：{{leader}}</span>
            </div>
        </div>
        <div class="time-line" v-if="hasCorrectTime">
            <div class="time-item clearfix" v-for="item in milestones" :class="{active: item.current}">
                <div class="time-quantum">
                    <div class="cell">
                        <span>{{item.title}}</span>
                    </div>
                </div>
                <div class="time-inner">
                    <div class="title"><span class="arrow"></span>{{item.title}}</div>
                    <div class="time-box">
                        <p>开始时间：{{item.start_date}}</p>
                        <p>结束时间：{{item.start_date}}</p>
                        <div v-if="item.btn_option" class="time-btns text-center">
                            <a :href="item.btn_option.url" class="btn btn-primary-oe btn-sm">{{item.btn_option.name}}</a>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="!hasCorrectTime">

        </div>
    </div>
</script>


<script>


    var TimeLine = Vue.component('time-line', {
        template: '#projectTimeLine',
        data: function () {
            return {
                timeList: [],
                proname: '',
                pid: '',
                number: '',
                apply_time: '',
                leader: '',
                hasCorrectTime: false,
                milestones: [],
                dates: []
            }
        },
        computed: {
            projectId: function () {
                return this.$route.params.id;
            }
        },
        filters: {
            format: function (time) {
                if(!time){
                    return '';
                }
                var timeStr = time.toString();
                return timeStr.substring(0, 4) + '-' + timeStr.substring(4, 6) + '-' + timeStr.substring(6, 8);
            }
        },
        watch: {
            // 如果路由有变化，会再次执行该方法
            '$route': 'getTimeList'
        },
        methods: {
            getTimeList: function () {
                var postData = {
                    pptype: this.$route.query.type,
                    actywId: this.$route.query.actYwId,
                    ppid: this.projectId
                };
                var xhr = $.post('/f/project/projectDeclare/getTimeIndexData', postData);
                var self = this;
                var everyTime = false;
                xhr.success(function (data) {
                    if (data) {
                        self.timeList = data.contents;
                        self.leader = data.leader;
                        self.number = data.number;
                        self.pid = data.pid;
                        self.apply_time = data.apply_time;
                        self.proname = data.proname;
                        everyTime = self.timeList.every(function (item) {
                            return item.start_date && item.end_date
                        });
                        self.hasCorrectTime = everyTime;
                        self.milestones = self.timeList.filter(function (item) {
                            return item.type === 'milestone'
                        });
                        self.dates = self.timeList.filter(function (item) {
                            return item.dates === 'date'
                        })
                    }
                })
            }
        },
        beforeMount: function () {
            this.getTimeList()
        },
        mounted: function () {

        }
    });

    var router = new VueRouter({
        routes: [
//            {path: '', name: 'timeLine', redirect: '/719e5f3a8ec3491797b42a698f36f89d'},
            // 动态路径参数 以冒号开头
            {path: '/:id', name: 'timeLine', component: TimeLine}
        ]
    });

    var app = new Vue({
        router: router,
        data: function () {
            return {
                tabList: [
//                    {
//                        id: 'dfd5fc7e850247048a66f14dc6a5be39',
//                        actYwId: '04ba36e74ca649d1902b18311280e443',
//                        type: '0000000196',
//                        name: '民大项目'
//                    },
                    {
                        id: '227f6f786a5e4bf6a7343fa9d94d874a',
                        actYwId: '2d7850ca88324274b0da3c18cb292f96',
                        type: '1',
                        name: '大创项目'
                    }, {
                        id: '81a6263754854b809fcf0dba0abd9637',
                        actYwId: '3a43a67e1c884919a6fadab4fb56e56d',
                        type: '0000000103',
                        name: '小创项目'
                    }
                ]
            }
        }
    });

    app.$mount('#appTime')

</script>
</body>
</html>