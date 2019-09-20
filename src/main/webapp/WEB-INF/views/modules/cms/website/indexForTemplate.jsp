<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/modules/cms/cms/front/include/taglib.jsp" %>
<!DOCTYPE html>
<html>

<head>
    <title>${frontTitle}</title>
    <meta charset="UTF-8">
    <meta name="decorator" content="creative"/>

</head>


<body>

<div id="app">
    <component v-for="(component, index) in componentList" :is="component.modelename" :key="component.sort" :idx="index" :component-data="component" :style="getStyle(component.modelename)"
               :class="getClassName(index)"></component>
</div>

<script type="text/javascript">

    'use strict'

    new Vue({
        el: '#app',
        components: {HomeBanner: HomeBanner, HomeAnnouncement: HomeAnnouncement, HomeNotice: HomeNotice, HomeTeacher: HomeTeacher, HomeProject: HomeProject, HomeCourse: HomeCourse, HomeGcontest: HomeGcontest},
        data: function () {
            var technical = JSON.parse(JSON.stringify(${fns: toJson(fns: getDictList("postTitle_type"))})) || [];
            return {
                siteId: '',
                componentList: [],
                technical: technical
            }
        },
        computed: {
            expertJobNamesEntries: function () {
                return this.getEntries(this.technical);
            }
        },
        methods: {
            getHomeLayout: function () {
                var self = this;
                this.$axios.get('/cms/index/indexLayout').then(function (response) {
                    var data = response.data;
                    var checkResponseCode = self.checkResponseCode(data.code, data.msg);
                    var layouts = [];
                    if (checkResponseCode) {
                        layouts = data.data;
                        layouts = layouts.sort(function (item1, item2) {
                            return parseInt(item1.sort) - parseInt(item2.sort);
                        });
                        self.componentList = layouts;
                    }
                }).catch(function (error) {
                    self.$message({
                        type: 'error',
                        message: error.response.data
                    })
                })
            },

            getClassName: function (idx) {
                if (idx < 1) {return ''}
                return {
                    'home-bg-white': idx % 2 == 0,
                    'home-bg-gray': idx % 2 == 1
                }
            },

            getStyle: function (name) {
                return {
                    'marginBottom': name === 'homeCourse' ? '-40px' : 0
                }
            }
        },
        created: function () {
            this.getHomeLayout();
        }
    })
</script>

</body>
</html>