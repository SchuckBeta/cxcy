/**
 * Created by Administrator on 2018/9/4.
 */


'use strict';
Vue.component('text-scroller', {
    template: '<div><slot></slot></div>',
    componentName: 'TextScroller',
    props: {
        height: String,
        interval: {
            type: Number,
            default: 3000
        },
        initialIndex: {
            type: Number,
            default: 0
        }
    },
    watch: {
        activeIndex: function (val, oldVal) {
            this.resetItemPosition(oldVal);
            this.$emit('change', val, oldVal);
        },
        items: function (val) {
            if (val.length > 0) this.setActiveItem(this.initialIndex);
        }
    },
    data: function () {
        return {
            items: [],
            activeIndex: 0
        }
    },
    methods: {
        updateItems: function () {
            this.items = this.$children.filter(function (child) {
                return child.$options.name === 'TextScrollerItem'
            })
        },
        playSlides: function () {
            if (this.items.length === 1) {
                this.activeIndex = 0;
                return false;
            }
            if (this.activeIndex < this.items.length - 1) {
                this.activeIndex++;
            } else {
                this.activeIndex = 0;
            }
        },
        resetItemPosition: function () {
            var length = this.items.length;
            var activeIndex = this.activeIndex;
            this.items.forEach(function (item, index) {
                item.calculateTranslate(index, activeIndex, length)
            })
        },
        setActiveItem: function (index) {
            var oldIndex = this.activeIndex;
            if (index < 0) {
                this.activeIndex = length - 1;
            } else if (index >= length) {
                this.activeIndex = 0;
            } else {
                this.activeIndex = index;
            }
            if (oldIndex === this.activeIndex) {
                this.resetItemPosition(oldIndex);
            }
        },
        startTimer: function () {
            // if(this.items.length > 1){
            this.timer = setInterval(this.playSlides, this.interval)
            // }
        }
    },
    created: function () {
        this.setActiveItem(this.initialIndex)
    },
    mounted: function () {
        this.updateItems();
        this.$nextTick(function () {
            if (this.initialIndex < this.items.length && this.initialIndex >= 0) {
                this.activeIndex = this.initialIndex;
            }
            this.resetItemPosition();
            this.startTimer();
        })
    }
});

Vue.component('text-scroller-item', {
    template: '<div class="text-scroller-item"  :style="transform" :class="{\'is-animating\': animating, \'is-active\': active}"><slot></slot></div>',
    name: 'TextScrollerItem',
    data: function () {
        return {
            translate: 0,
            height: 32,
            active: false,
            animating: false
        }
    },
    computed: {
        transform: {
            get: function () {
                return {
                    msTransform: 'translateY(' + this.translate + 'px)',
                    webkitTransform: 'translateY(' + this.translate + 'px)',
                    transform: 'translateY(' + this.translate + 'px)'
                }
            }
        }
    },
    methods: {
        calculateTranslate: function (index, activeIndex, length) {
            // this.translate = (index - activeIndex) * this.height;
            if (activeIndex < length - 1) {
                this.translate = (index * this.height - activeIndex * this.height)
            } else if (activeIndex === length - 1) {
                if (index === 0) {
                    this.translate = this.height
                } else {
                    this.translate = (index * this.height - activeIndex * this.height)
                }
            } else if (activeIndex === length) {
                if (index === 0) {
                    this.translate = 0
                } else {
                    this.translate = (index * this.height)
                }
            } else {
                this.translate = (index * this.height - activeIndex * this.height)
            }
            if(length == 1){
                this.translate = 0
            }
            // console.log(index, activeIndex, length)
            this.setAnimating(index, activeIndex, length)
        },
        setAnimating: function (index, activeIndex, length) {
            if (activeIndex <= length - 1) {
                this.animating = index === activeIndex || index === activeIndex - 1
            } else {
                this.animating = index === activeIndex || index === 0
            }
            this.active = activeIndex == index;
        }
    },
    created: function () {
        this.$parent && this.$parent.updateItems();
    },
    mounted: function () {
        // this.$nextTick(function () {
        //     this.height = 32;
        // })
    },
    destroy: function () {
        this.$parent && this.$parent.updateItems();
    }
})

Vue.component('h-title_bar', {
    template: '<div class="h-title_bar"><div class="h-column-wrap h-title_line">' +
    '<a :href="titleData.href"><h3 class="h-title">{{titleData.title}}</h3><p class="h-title_en">{{titleData.ename}}</p></a></div></div>',
    props: {
        titleData: Object
    }
})

var HomeBanner = Vue.component('home-banner', {
    template: '<div class="home-module-banner" style="width: 100%" v-loading="bannerLoading"> ' +
    '<el-carousel :interval="5000" :height="height"  trigger="click" arrow="always"><el-carousel-item v-for="item in bannerList" :key="item">' +
    '<img :src="item | ftpHttpFilter(ftpHttp)"> ' +
    '</el-carousel-item></el-carousel>' +
    '</div>',
    name: 'HomeBanner',
    props: {
        data: {
            type: Object
        }
    },
    data: function () {
        return {
            bannerList: [],
            height: '450px',
            bannerLoading: true
        }
    },
    methods: {
        getBannerList: function () {
            var self = this;
            this.bannerLoading = true;
            this.$axios.get('/cms/index/bannerList').then(function (response) {
                var data = response.data;
                var bannerList = [];
                if (data.status === 1) {
                    bannerList = data.data.bannerList.split(',');
                    self.bannerList = bannerList;
                }
                self.bannerLoading = false;
            }).catch(function (error) {
                self.bannerLoading = false;
            })
        }
    },
    beforeMount: function () {

    },
    mounted: function () {
        this.getBannerList();
        var self = this;
        this.height = Math.floor((window.innerWidth - 20) / 1920 * 450) + 'px';
        window.addEventListener('resize', function () {
            self.height = Math.floor((window.innerWidth - 20) / 1920 * 450) + 'px'
        })
    }
});


var HomeAnnouncement = Vue.component('home-announcement', {
    template: '<div class="home-module-notice_list"><div class="container">' +
    '<div class="hmn-header"><i class="iconfont icon-yinlianglabashengyin"></i>通知公告</div>' +
    '<div class="hmn-content">' +
    '<text-scroller class="hmn-notices-wrap">' +
    '<text-scroller-item v-for="(item, index) in noticeList" :key="index" class="hmn-notice-item"> <a :href="ctxFront+\'/frontNotice/noticeView?id=\'+item.id">{{item.title}}</a></text-scroller-item>' +
    '</text-scroller></div><div class="hmn-more"><a :href="ctxFront+\'/frontNotice/noticeList\'">更多<i class="el-icon-d-arrow-right"></i></a></div>' +
    '</div></div>',
    name: 'HomeAnnouncement',
    props: {
        data: Object
    },
    data: function () {
        return {
            noticeList: []
        }
    },
    methods: {
        getNoticeList: function () {
            var self = this;
            this.$axios.get('/cms/index/noticeIndex').then(function (response) {
                var data = response.data;
                var noticeList = [];
                if (data.status === 1) {
                    noticeList = data.data.noticeList || [];
                    self.noticeList = noticeList;
                }
            })
        }
    },
    created: function () {
        this.getNoticeList();
    }
})


var HomeNotice = Vue.component('home-notice', {
    name: 'HomeNotice',
    template: '<div class="home-module-container"><div class="container"><h-title_bar :title-data="titleData"></h-title_bar>' +
    '<div class="h_notification-block"><a :href="ctxFront + \'/page-SCtognzhi#homeSSDT\'"><span class="hnb-title" :class="className">{{notiyNames.homeSSDT}}</span></a><div class="hnb-inner">' +
    '<div class="hnb-pic"><img :src="msgImg | ftpHttpFilter(ftpHttp)"></div>' +
    '<ul class="hnb-notices"><li v-for="item in notifys.ss"><a :href="ctxFront+\'/oa/oaNotify/viewDynamic?id=\'+item.id">{{item.title}}</a><span class="date">{{item.createDate | formatDateFilter(\'YYYY-MM-DD\')}}</span></li></ul><div class="text-right more"><a :href="ctxFront + \'/page-SCtognzhi#homeSSDT\'">更多<i class="el-icon-d-arrow-right"></i></a></div></div></div>' +
    '<div class="h_notification-banner"><el-carousel trigger="click" height="255px"><el-carousel-item v-for="item in dynamiceImages" :key="item"><img :src="item | ftpHttpFilter(ftpHttp)"></el-carousel-item></el-carousel></div>' +
    '<div class="h_notification-dynamics">' +
    '<div class="hn-dynamic"><h4 class="title">{{notiyNames.homeSCDT}}<a class="more" :href=" ctxFront + \'/page-SCtognzhi#homeSCDT\'">更多<i class="el-icon-d-arrow-right"></i></a></h4>' +
    '<ul class="hnd-notices"><li v-for="item in notifys.sc" :key="item.id"><a :href="ctxFront+\'/oa/oaNotify/viewDynamic?id=\'+item.id">{{item.title}}</a><span class="date">{{item.createDate | formatDateFilter(\'YYYY-MM-DD\')}}</span></li></ul></div>' +
    '<div class="hn-dynamic"><h4 class="title">{{notiyNames.homeSCTZ}}<a class="more" :href=" ctxFront + \'/page-SCtognzhi#homeSCTZ\'">更多<i class="el-icon-d-arrow-right"></i></a></h4>' +
    '<ul class="hnd-notices"><li v-for="item in notifys.tz"><a :href="ctxFront+\'/oa/oaNotify/viewDynamic?id=\'+item.id">{{item.title}}</a><span class="date">{{item.createDate | formatDateFilter(\'YYYY-MM-DD\')}}</span></li></ul></div>' +
    '</div>' +
    '</div></div>',
    props: {
        componentData: {
            type: Object,
            require: true
        },
        idx: Number
    },
    computed: {
        titleData: function () {
            return {
                href: this.ctxFront + '/page-SCtognzhi',
                title: this.componentData.modelname,
                ename: this.componentData.ename
            }

        },
        className: function () {
            return {
                'home-bg-white': this.idx % 2 == 0,
                'home-bg-gray': this.idx % 2 == 1
            }
        }
    },
    data: function () {
        return {
            notifys: {
                'sc': [],
                'tz': [],
                'ss': []
            },
            dynamiceImages: [],
            notiyNames: {
                'homeSCDT': '',
                'homeSCTZ': '',
                'homeSSDT': ''
            },
            msgImg: ''
        }
    },
    methods: {
        getOaNotifys: function () {
            var self = this;
            this.$axios.get('/frontNotice/getOaNotifys').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.notifys = data.data;
                }
            }).catch(function (error) {
                console.log(error)
            })
        },
        getDynamiceImages: function () {
            var self = this;
            this.$axios.get('/cms/index/indexDynamicImg').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.dynamiceImages = data.data.dynamicImg.split(',');
                    self.msgImg = data.data.msgImg;
                }
            }).catch(function (error) {
                console.log(error)
            })
        },
        getIndexInLayout: function () {
            var self = this;
            this.$axios.get('/cms/index/indexInLayout').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    var notiyNames = data.data;
                    notiyNames.forEach(function (item) {
                        self.notiyNames[item.modelename] = item.modelname;
                    })
                }
            }).catch(function (error) {
                console.log(error)
            })
        }
    },
    created: function () {
        this.getOaNotifys();
        this.getDynamiceImages();
        this.getIndexInLayout();
    }
});


//第二导师

Vue.component('teacher-side', {
    template: '<div class="top-teachers-slide">\n' +
    '                <div style="overflow: hidden">\n' +
    '                    <div class="top-teachers-slide-wrapper" :style="{transform: \'translateX(-\'+teacherActiveIndex*880+\'px)\'}">\n' +
    '                        <div v-for="(item, index) in teachers" :key="index" class="tts-teachers">\n' +
    '                            <div v-for="teacher in item" class="teachers">\n' +
    '                                <a class="teacher-avatar" :href="ctxFront+\'/sys/frontTeacherExpansion/view?id=\'+teacher.id">\n' +
    '                                    <img :src="teacher.user.photo  | ftpHttpFilter(ftpHttp) | studentPicFilter">\n' +
    '                                </a>\n' +
    '                                <div class="teacher-name-title">\n' +
    '                                    <p class="name">{{teacher.user.name}}</p>\n' +
    '                                    <div class="title">\n' +
    '                                        <el-tag type="info" size="mini" v-if="teacher.technicalTitle">{{teacher.technicalTitle | selectedFilter(expertJobNamesEntries, true)}}</el-tag>\n' +
    '                                    </div>\n' +
    '                                </div>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </div>\n' +
    '                </div>\n' +
    '                <button type="button" class="top-teachers-arrow top-teachers-arrow-left"\n' +
    '                        @click.stop.prevent="handleChangePrevActiveIndex2">\n' +
    '                    <i class="el-icon-arrow-left"></i>\n' +
    '                </button>\n' +
    '                <button type="button" class="top-teachers-arrow top-teachers-arrow-right"\n' +
    '                        @click.stop.prevent="handleChangeNextActiveIndex2">\n' +
    '                    <i class="el-icon-arrow-right"></i>\n' +
    '                </button>\n' +
    '            </div>',
    props: {
        teachers: Array
    },
    data: function () {
        return {
            teacherActiveIndex: 0
        }
    },
    computed: {
        expertJobNamesEntries: function () {
            return this.$root.expertJobNamesEntries;
        }
    },
    methods: {
        handleChangePrevActiveIndex2: function () {
            this.teacherActiveIndex -= 1;
            if (this.teacherActiveIndex < 0) {
                this.teacherActiveIndex = 0
            }

        },
        handleChangeNextActiveIndex2: function () {
            this.teacherActiveIndex += 1;
            if (this.teacherActiveIndex > this.teachers.length - 1) {
                this.teacherActiveIndex = this.teachers.length - 1
            }
        }
    }
})

var HomeTeacher = Vue.component('home-teacher', {
    name: 'HomeTeacher',
    template: '<div class="home-module-container"><div class="container"><h-title_bar :title-data="titleData"></h-title_bar>' +
    '<div v-loading="teacherLoading"><div class="top-teachers-carousel"><div class="top-teachers-carousel-wrapper"><transition-group name="top-teacher-fade" tag="div">' +
    '<div v-show="activeIndex === index" v-for="(item, index) in topTeachers" :key="item.id" class="ttc-item" :class="{active: activeIndex === index}">' +
    '<div class="teacher-avatar"><a :href="ctxFront+\'/sys/frontTeacherExpansion/view?id=\'+item.id"><img :src="item.user.photo  | ftpHttpFilter(ftpHttp) | studentPicFilter"></a></div>' +
    '<div class="teacher-name-title"><span class="name">{{item.user.name}}</span><span class="title">{{item.postTitle}}</span></div><div class="teacher-tags text-right">' +
    '<el-tag v-if="item.keywords" v-for="(tag, index) in item.keywords" :key="index" type="info" size="small"> {{tag}}</el-tag></div><p class="teacher-intro">{{item.mainExp}}</p>' +
    '</div></transition-group></div><div class="top-teachers-indicators">' +
    '<button v-for="(item, index) in topTeachers" type="button" class="top-teachers-indicator"  :class="{active: activeIndex === index}"  @click.stop.prevent="handleChangeActiveIndex(index)"></button>' +
    '</div></div>' +
    '<teacher-side :teachers="sTeachers"></teacher-side></div></div></div>',
    props: {
        componentData: {
            type: Object,
            require: true
        }
    },
    data: function () {
        return {
            topTeachers: [],
            topTeacherTwo: [],
            activeIndex: 0,
            teacherLoading: true,
            timer:null
        }
    },
    computed: {
        titleData: function () {
            return {
                href: this.ctxFront+'/pageTeacher',
                title: this.componentData.modelname,
                ename: this.componentData.ename
            }
        },
        sTeachers: function () {
            var result = [];
            for (var x = 0; x < Math.ceil(this.topTeacherTwo.length / 5); x++) {
                var start = x * 5;
                var end = start + 5;
                result.push(this.topTeacherTwo.slice(start, end));
            }
            return result;
        }
    },
    methods: {
        handleChangeActiveIndex: function (index) {
            this.activeIndex = index;
            this.timeTick();
        },

        getTeacherData: function () {
            var self = this;
            this.$axios.get('/cms/index/teacherList?firstNum=3&siteNum=10').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.topTeachers = data.data.firstTeacherList || [];
                    self.topTeacherTwo = data.data.siteTeacherList || [];
                }
                self.teacherLoading = false;
                self.timeTick();
            }).catch(function (error) {
                self.teacherLoading = false;
            })
        },
        timeTick:function () {
            var self = this;
            self.timer && clearInterval(self.timer);
            var timeCount = self.topTeachers.length;
            self.timer = setInterval(function () {
                if(self.activeIndex >= timeCount-1){
                    self.activeIndex = 0;
                }else{
                    self.activeIndex++;
                }
            },3000);
        }
    },
    created: function () {
        this.getTeacherData();
    }
});


//优秀项目大赛item

Vue.component('excellent-pro-item', {
    template: '<div class="excellent-pro-block">\n' +
    '                        <div class="exc-pro-pic">\n' +
    '                            <a :href="excData.id | hrefFilter(ctxFront)">\n' +
    '                                <img :src="excData.thumbnail | ftpHttpFilter(ftpHttp) | proGConPicFilter">\n' +
    '                            </a>\n' +
    '                        </div>\n' +
    '                        <h4 class="exc-pro-title"><a :href="excData.id | hrefFilter(ctxFront)">{{excData.title | textEllipsis(12)}}</a></h4>\n' +
    '                        <div class="exc-pro-members">\n' +
    '                            <div class="exc-pro-member">\n' +
    '                                负责人：{{excData.leaderName}}\n' +
    '                            </div>\n' +
    '                            <div class="exc-pro-member">\n' +
    '                                指导老师：{{excData.tnames}}\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                        <p v-show="false" class="exc-pro-description">{{excData.description}}</p>\n' +
    '                        <div class="exc-pro-action-source">\n' +
    '                            <div class="exc-pro-action">\n' +
    '                                <span><i class="iconfont icon-yanjing1"></i>{{excData.views}}</span>\n' +
    '                                <span><i class="iconfont icon-dianzan1"></i>{{ excData.cmsArticleData ? excData.cmsArticleData.likes : 0}}</span>\n' +
    '                            </div>\n' +
    '                            <div class="exc-source">\n' +
    '                                来源：{{excData.sourceName}}\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </div>',
    props: {
        excData: Object
    },
    filters: {
        hrefFilter: function(value, ctxFront){
            return ctxFront + '/getOneCmsArticle?id='+value;
        }
    }
});

//优秀项目
var HomeProject = Vue.component('home-project', {
    template: '<div class="home-module-container"><div class="container"><h-title_bar :title-data="titleData"></h-title_bar>' +
    '<div v-if="!!titleData.name" class="h-pro-gc-tab-nav">' +
    '<el-button v-for="(value, key, index) in excellentData" :key="key" :type="excellentActiveIndex == index ? \'primary\' : \'\'" size="medium" ' +
    '@click.stop.prevent="excellentActiveIndex=index"> {{ key == \'projectList\' ? \'优秀双创项目\' : \'大赛获奖作品\' }}</el-button></div>' +
    '<div v-loading="excLoading"><div class="h-pro-gc-tabs">' +
    '<div v-for="(value, key, index) in excellentData" class="h-pro-gc-tab-content" :class="{active: excellentActiveIndex == index}"> ' +
    '<el-row :gutter="30"><el-col :span="6" :key="item.id" v-if="colIndex <= 3" v-for="(item, colIndex) in value"><excellent-pro-item :exc-data="item"></excellent-pro-item></el-col></el-row></div>' +
    '</div></div></div></div>',
    name: 'HomeProject',
    props: {
        componentData: {
            type: Object,
            require: true
        }
    },
    data: function () {
        return {
            excellentData: {},
            excLoading: true,
            excellentActiveIndex: 0,
        }
    },
    computed: {
        titleData: function () {
            return {
                href: this.ctxFront+'/pageProject',
                title: this.componentData.modelname,
                ename: this.componentData.ename,
                name: this.componentData.modelname
            }
        },
    },
    methods: {
        getExcellentData: function () {
            var self = this;
            this.$axios.get('/cms/index/projectList').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    data = data.data || {};
                    self.excellentData =  {
                        projectList: data.projectList || [],
                        gcontestList: data.gcontestList || []
                    }
                }
                self.excLoading = false;
            })
        }
    },
    created: function () {
        this.getExcellentData();
    }
})


//名师讲堂item

Vue.component('top-teacher-item', {
    template: '<div class="h-course-block">\n' +
    '                        <div class="h-course-pic">\n' +
    '                            <a :href="ctxFront+\'/course/view?id=\'+course.id">\n' +
    '                                <img :src="course.coverImg | ftpHttpFilter(ftpHttp)">\n' +
    '                            </a>\n' +
    '                        </div>\n' +
    '                        <div class="h-course-content">\n' +
    '                            <h4 class="title">\n' +
    '                                <a :href="ctxFront+\'/course/view?id=\'+course.id">{{course.name}}</a>\n' +
    '                            </h4>\n' +
    '                            <div class="h-course-teacher">\n' +
    '                                <span>课程讲师：{{course.teasNames}}</span>\n' +
    '                                <span>讲师职称：{{course.teasTitles}}</span>\n' +
    '                            </div>\n' +
    '                            <p class="description">\n' +
    '                                {{course.description}}\n' +
    '                            </p>\n' +
    '                            <div class="actions">\n' +
    '                                <span><i class="iconfont icon-yanjing1"></i>{{course.views}}</span>\n' +
    '                                <span><i class="iconfont icon-dianzan1"></i>{{course.likes}}</span>\n' +
    '                            </div>\n' +
    '                        </div>\n' +
    '                    </div>',
    props: {
        course: Object
    }
});

var HomeCourse = Vue.component('home-course', {
    template: '<div class="home-module-container"><div class="container"><h-title_bar :title-data="titleData"></h-title_bar><div v-loading="courseLoading"><el-row>' +
    '<el-col v-if="index<=3" v-for="(item, index) in courseList" :span="12" :key="item.id">' +
    '<top-teacher-item :course="item"></top-teacher-item></el-col></el-row></div></div></div>',
    props: {
        componentData: {
            type: Object,
            require: true
        }
    },
    data: function () {
        return {
            courseList: [],
            courseLoading: true
        }
    },
    computed: {
        titleData: function () {
            return {
                href: this.ctxFront+'/course/frontCourseList',
                title: this.componentData.modelname,
                ename: this.componentData.ename
            }
        },
    },
    methods: {
        getCourseList: function () {
            var self = this;
            this.$axios.get('/cms/index/courseList').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.courseList = data.data.courseList || [];
                }
                self.courseLoading = false;
            })
        }
    },
    created: function () {
        this.getCourseList();
    }
});


var HomeGcontest = Vue.component('home-gcontest', {
    template: '<div class="home-module-container"><div class="container"><h-title_bar :title-data="titleData"></h-title_bar><div v-loading="hotGconLoading" class="container container-hot-contest">' +
    '<div class="h-video-block"><video v-if="videoUrl" style="width: 100%; height: 100%" controls>您的浏览器不支持 video 标签。请更换谷歌浏览器获取更好的体验<source :src="videoUrl | ftpHttpFilter(ftpHttp)"></video></div><div class="h-hc-content" style="width: 460px;">' +
    '<el-carousel :interval="5000" height="260px"  trigger="click" arrow="always">' +
    '<el-carousel-item v-for="item in imageList" :key="item"><img :src="item | ftpHttpFilter(ftpHttp)"></el-carousel-item></el-carousel></div></div></div></div>',
    name: 'HomeGcontest',
    props: {
        componentData: {
            type: Object,
            require: true
        }
    },
    data: function () {
        return {
            videoUrl: '',
            imageList: [],
            urlId: '',
            hotGconLoading: true
        }
    },
    computed: {
        titleData: function () {
            return {
                href: this.ctxFront+'/getOneCmsArticle?id='+this.urlId+'&modelname='+decodeURI(this.componentData.modelname),
                title: this.componentData.modelname,
                ename: this.componentData.ename
            }
        },
    },
    methods: {
        getData: function () {
            var self = this;
            this.$axios.get('/cms/index/gcontestShow').then(function (response) {
                var data = response.data;
                if (data.status === 1) {
                    self.videoUrl = data.data.video;
                    self.imageList = data.data.Images.split(',');
                    self.urlId = data.data.id;
                }
                self.hotGconLoading = false;
            }).catch(function (error) {
                self.hotGconLoading = false;
            })
        }
    },
    created: function () {
        this.getData();
    }
})