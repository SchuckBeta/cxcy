<%@ page contentType="text/html;charset=UTF-8" %>
<%@ include file="/WEB-INF/views/include/taglib.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>${backgroundTitle}</title>
    <%@include file="/WEB-INF/views/include/backcreative.jsp" %>
    <style>
        .pw-canvas-image {
            display: inline-block;
            vertical-align: middle;
            margin: 0 auto;
            max-width: 100%;
        }
    </style>
</head>
<body>
<div id="app" v-show="pageLoad" style="display: none" class="container-fluid">
    <edit-bar>基地全局图预览</edit-bar>
    <div class="conditions">
        <e-condition label="基地" type="radio" :options="baseList" v-model="baseId" :is-show-all="false"
                     @change="handleChangeBase"
                     :default-props="{label: 'name', value: 'id'}"></e-condition>

        <e-condition label="楼栋" :options="buildList">
            <e-radio-group class="e-radio-spaces" v-model="buildId" @change="handleChangeBuild">
                <e-radio v-for="build in buildList" name="buildId" :class="{'is-siblings': build.isSiblings}"
                         :label="build.id" :key="build.id">{{build.name}}
                </e-radio>
            </e-radio-group>
        </e-condition>

        <e-condition label="楼层" type="radio" :options="floorList" :is-show-all="false" v-model="floorId"
                     @change="handleChangeFloor"
                     :default-props="{label: 'name', value: 'id'}"></e-condition>
    </div>
    <div v-loading="loading" class="table-container text-center mgb-60" style="padding: 10px; min-height: 300px;">
        <div :style="designerStyle" style="margin: 0 auto;background-repeat: no-repeat;background-position: center;">
            <img class="pw-canvas-image" :src="canvasImage | ftpHttpFilter(ftpHttp) | canvasImageDefault"/>
        </div>
    </div>
</div>

<script type="text/javascript">

    'use strict';

    new Vue({
        el: '#app',
        data: function () {
            return {
                baseId: '',
                buildId: '',
                floorId: '',
                spaceList: [],
                canvasImage: '',
                isDefaultBuildId: false,
                loading: false,
                designerStyle: {
                    width: '',
                    height: '',
                    backgroundColor: '',
                    backgroundImage: '',
                }
            }
        },
        computed: {
            baseList: {
                get: function () {
                    return this.spaceList.filter(function (item) {
                        return item.type === '2'
                    })
                }
            },

            buildList: {
                get: function () {
                    var buildList = [];
                    this.spaceList.forEach(function (item) {
                        if (item.type === '3') {
                            Vue.set(item, 'isSiblings', true);
                            buildList.push(item);
                        }
                    });
                    return buildList;
                }
            },

            floorList: function () {
                var self = this;
                if (!this.buildId) return [];
                return this.spaceList.filter(function (item) {
                    return item.pId === self.buildId;
                })
            },

        },
        filters: {
            canvasImageDefault: function (value) {
                if (!value) {
                    return '/images/floorViewDefault2.jpg'
                }
                return value;
            }
        },
        watch: {
            baseList: function () {
                if (!this.isDefaultBuildId) {
                    this.isDefaultBuildId = true;
                    this.baseId = this.baseList[0] ? this.baseList[0].id : '';
                    this.getPwCanvas(this.baseId)
                }
            }
        },
        methods: {
            getSpaceList: function () {
                var self = this;
                return this.$axios.get('/pw/pwSpace/treeData').then(function (response) {
                    self.spaceList = response.data || [];
                })
            },
            handleChangeBuild: function (value) {
                var buildId = this.buildId;
                if (!buildId) {
                    this.floorId = '';
                    this.buildList.forEach(function (item) {
                        Vue.set(item, 'isSiblings', true);
                    });
                    this.getPwCanvas(value);
                    return false;
                }
                var build = this.buildList.filter(function (item) {
                    return item.id === buildId;
                });

                this.baseId = build[0].pId;
                var baseId = this.baseId;
                this.buildList.forEach(function (item) {
                    Vue.set(item, 'isSiblings', !baseId ? true : item.pId === baseId);
                });
                this.floorId = '';
                this.getPwCanvas(value);
            },

            handleChangeBase: function (value) {
                var buildList = this.buildList;
                buildList.forEach(function (item) {
                    Vue.set(item, 'isSiblings', !value ? true : item.pId === value);
                });
                this.buildId = '';
                this.floorId = '';
                this.getPwCanvas(value);
            },

            handleChangeFloor: function (value) {
                this.getPwCanvas(value);
            },

            getPwCanvas: function (id) {
                var self = this;
                this.loading = true;
                this.$axios.get('/pw/pwDesignerCanvas/parentAndChildren?id=' + id).then(function (response) {
                    var data = response.data;
                    if (data) {
                        var pwCanvas = data.self;
                        var pwDesignerCanvas = pwCanvas.pwDesignerCanvas;
                        self.canvasImage = pwCanvas.imageUrl;
                        if (pwDesignerCanvas) {
                            self.canvasImage = pwDesignerCanvas.picUrl;
                            self.designerStyle = {
                                width: pwDesignerCanvas.width + 'px',
                                height: pwDesignerCanvas.height + 'px',
                                backgroundColor: pwDesignerCanvas.backgroundColor,
                                backgroundImage: ''
                            };
                            if (pwDesignerCanvas.backgroundImage) {
                                self.designerStyle.backgroundImage = 'url(' + self.ftpHttp + pwDesignerCanvas.backgroundImage + ')'
                            }
                        }else {
                            self.designerStyle = {
                                width: '',
                                height: '',
                                backgroundColor: '',
                                backgroundImage: ''
                            }
                        }
                    } else {
                        self.canvasImage = '';
                    }
                    self.loading = false;
                }).catch(function (error) {
                    self.loading = false;
                    self.$message.error(self.xhrErrorMsg)
                })
            }
        },
        created: function () {
            this.getSpaceList();
        }

    })

</script>
</body>
</html>