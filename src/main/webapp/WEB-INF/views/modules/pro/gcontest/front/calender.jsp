<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${frontTitle}</title>
    <meta name="decorator" content="creative"/>
</head>
<body>

<div id="app" v-show="pageLoad" class="container page-container pdt-60" style="display: none">
    <el-breadcrumb separator-class="el-icon-arrow-right">
        <el-breadcrumb-item><a :href="frontOrAdmin"><i class="iconfont icon-ai-home" style="font-size: 12px;"></i>首页</a>
        </el-breadcrumb-item>
        <el-breadcrumb-item><a :href="frontOrAdmin + '/gcontest/gContest/list'">双创大赛</a></el-breadcrumb-item>
        <el-breadcrumb-item>我的大赛</el-breadcrumb-item>
        <el-breadcrumb-item>当前大赛</el-breadcrumb-item>
    </el-breadcrumb>
    <div class="text-right mgb-20">
        <el-button type="primary" size="mini">当前大赛</el-button>
        <el-button  size="mini" @click.stop.prevent="goToGcontestList">我的大赛列表</el-button>
    </div>
    <div class="gcontest-calendars">
        <div class="gcontest-calendar gcontest-calendar-header">
            <span class="title">{{gcontestCalendar.name}}</span>
            <span class="bianhao">项目编号：{{gcontestCalendar.code}}</span>
        </div>
        <div v-for="(item, index) in gcontestCalendars" :key="index" class="gcontest-calendar">
            <div class="gcontest-date">
                <div class="en-month">
                    {{item.Date | enMonth(monthArr)}}
                </div>
                <div class="day-level">
                    <div class="day">
                        {{item.Date | getDate}}
                    </div>
                    <div class="level">
                        {{item.SpeedOfProgress}}
                    </div>
                </div>
            </div>
            <div class="gcontest-content">
                <div v-if="item.type != 0" class="gcontest-info-bar">
                    <span>所在组别：{{item.group}}</span> <span>学院：{{item.School}}</span> <span>参赛项目组数：{{item.Number_of_entries}}</span>
                </div>
                <div class="gcontest-info-table">
                    <template v-if="item.type == '0'">
                        <div v-for="file in item.list" class="e-file-item e-file-item_mini">
                            <a href="javascript:void(0);" :title="file.link" target="_blank" @click.stop.prevent="openView(file)" class="e-file-item_name">
                                <img :src="file.link | getSuffix | fileSuffixFilter">
                               {{file.link}}
                            </a>
                            <a title="下载文件" href="javascript: void(0);" @click.stop.prevent="downFile(file)" class="e-file-item_downfile">
                                <i class="iconfont icon-custom-down"></i></a>
                        </div>
                    </template>
                    <template v-else-if="item.type == '1'">
                        <el-table :data="item.list" size="small" border>
                            <el-table-column label="学院评分">
                                <template slot-scope="scope">
                                    {{scope.row.College_score}}<span style="opacity: 0">1</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="建议及意见">
                                <template slot-scope="scope">
                                    {{scope.row.Proposal}}
                                </template>
                            </el-table-column>
                            <el-table-column label="学院排名">
                                <template slot-scope="scope">
                                    {{scope.row.ranking}}
                                </template>
                            </el-table-column>
                        </el-table>
                    </template>
                    <template v-else="item.type == '2'">
                        <gcontest-score :score-list="item.list"></gcontest-score>
                    </template>
                </div>
            </div>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';

    Vue.component('gcontest-score', {
        props: {
            scoreList: Array
        },
        template: '<el-table size="mini" border :data="scoreList" :span-method="objectSpanMethod">\n' +
        '                            <el-table-column label="评分内容">\n' +
        '                                <template slot-scope="scope">\n' +
        '                                    {{scope.row.Review_the_content}}\n<span style="opacity: 0">1</span>' +
        '                                </template>\n' +
        '                            </el-table-column>\n' +
        '                            <el-table-column label="得分">\n' +
        '                                <template slot-scope="scope">\n' +
        '                                    {{scope.row.getScore}}\n' +
        '                                </template>\n' +
        '                            </el-table-column>\n' +
        '                            <el-table-column label="建议及意见">\n' +
        '                                <template slot-scope="scope">\n' +
        '                                    {{scope.row.advice}}\n' +
        '                                </template>\n' +
        '                            </el-table-column>\n' +
        '                            <el-table-column label="当前排名">\n' +
        '                                <template slot-scope="scope">\n' +
        '                                    {{scope.row.Current_rank}}\n' +
        '                                </template>\n' +
        '                            </el-table-column>\n' +
        '                            <el-table-column label="荣获奖项">\n' +
        '                                <template slot-scope="scope">\n' +
        '                                    {{scope.row.Awards}}\n' +
        '                                </template>\n' +
        '                            </el-table-column>\n' +
        '                            <el-table-column label="荣获奖金">\n' +
        '                                <template slot-scope="scope">\n' +
        '                                    {{scope.row.bonus}}\n' +
        '                                </template>\n' +
        '                            </el-table-column>\n' +
        '                        </el-table>',

        methods: {
            objectSpanMethod: function (obj) {
                var columnIndex = obj.columnIndex;
                var length = this.scoreList.length;
                if (columnIndex === 4 || columnIndex === 5) {
                    return {
                        rowspan: length,
                        colspan: 1
                    };
                } else {
                    return {
                        rowspan: 1,
                        colspan: 1
                    }
                }
            }
        }
    });

    new Vue({
        el: '#app',
        data: function () {
            return {
                gcontestCalendar:{},
                gcontestCalendars: [],
                monthArr: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']
            }
        },

        filters: {
            enMonth: function (value, arr) {
                if (!value) return '';
                return arr[new Date(value).getMonth()];
            },
            getDate: function (value) {
                if (!value) return '';
                return new Date(value).getDate();
            },
            getSuffix: function (value) {
                var suffix;
                if(!value) return '';
                suffix = value.match(/\.(\w+)/)[0];
                if(suffix){
                    suffix = suffix.replace('.', '');
                    return suffix;
                }
                return '';
            },
            fileView: function (value, file) {
                if(/jpg|jpeg|pdf/i.test(value)){
                    return file.url;
                }
                return 'javascript:void(0);'
            }
        },

        methods: {

            goToGcontestList: function () {
              location.href =this.frontOrAdmin + '/gcontest/gContest/list'
            },

            openView: function (file) {
                var suffix, url;
                var name = file.link;
                if(!name) return false;
                suffix = name.match(/\.(\w+)/)[0];
                if(!suffix){
                    return false;
                }
                if(/jpg|jpeg|pdf/i.test(name)){
                    url = this.addFtpHttp(file.url);
                    window.open(url);
                    return false;
                }
                this.$message({type: 'error', message: '暂不支持预览，请下载查阅'});
            },

            getGcontestTimeIndexData: function () {
                var self = this;
                this.$axios.get('/gcontest/gContest/getGcontestTimeIndexData').then(function (response) {
                    var data = response.data;
                    var gcontestCalendars = [];
                    if (data) {
                        self.gcontestCalendar = data.project_code || {};
                        gcontestCalendars = data.table_first ? (data.table_first.list || []) : [];
                        self.gcontestCalendars = gcontestCalendars.sort(function (a, b) {
                            return parseInt(a.type) - parseInt(b.type)
                        })
                    }
                })
            },
            downFile: function (file) {
                location.href = this.frontOrAdmin + "/ftp/ueditorUpload/downFile?url=" + file.url + "&fileName=" + encodeURI(encodeURI(file.link));
            }

        },
        beforeMount: function () {
            this.getGcontestTimeIndexData();
        }
    })


</script>

</body>
</html>
