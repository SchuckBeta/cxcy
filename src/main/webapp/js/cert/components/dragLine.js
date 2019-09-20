/**
 * Created by Administrator on 2017/11/10.
 */

var DragLine = Vue.component('drag-line', {
    template: '<span class="certificate-line" :class="className"><i class="dragged-handler" v-dragged.prevent="draggedLine"></i></span>',
    props: {
        direction: {
            type: String,
            default: ''
        },
        boxCls: {
            type: String,
            default: '.certificate-crop-box'
        },
        max: {
            type: [String, Number],
            default: ''
        },
        min: {
            type: [String, Number],
            default: ''
        },
        ratio: {
            type: Number,
            default: 1
        },
        containment: {
            type: String,
            default: '.cert-pic-box'
        }
    },
    data: function () {
        return {
            firstDragEleHeight: '',
            firstDragEleWidth: '',
            firstDragELeTop: '',
            firstDragEleLeft: '',
            boxEle: '',
            certPicBox: '',
            handler: '',
            boxWidth: '',
            boxLeft: '',
            boxTop: '',
            boxHeight: ''
        }
    },
    computed: {
        className: function () {
            switch (this.direction) {
                case 'east':
                    return 'line-e';
                case 'north':
                    return 'line-n';
                case 'west':
                    return 'line-w';
                default:
                    return 'line-s';
            }
        },
        minVal: function () {
            return this.min * this.ratio;
        },
        maxVal: function () {
            return this.max * this.ratio;
        }
    },
    methods: {
        draggedLine: function (params) {
            var el = params.el;
            var $boxEle = $(this.boxEle).size() < 1 ? $('.cert-pic-box') : $(this.boxEle);
            var $certPicBox = $(this.certPicBox) < 1 ? $(this.$el).parents(this.boxCls) : $(this.certPicBox);
            var left;
            var distanceWidth, distanceHeight;
            var boxWidth;
            var boxHeight;
            var top;
            var boxEleTop;
            var boxLeft;
            if (params.first) {
                this.certPicBox = $('.cert-pic-box');
                this.boxEle = $(this.$el).parents(this.boxCls);
                this.firstDragEleHeight = parseInt($boxEle.css('height'));
                this.firstDragEleWidth = parseInt($boxEle.css('width'));
                this.firstDragELeTop = parseInt($boxEle.css('top'));
                this.firstDragEleLeft = parseInt($boxEle.css('left'));
                this.dragged = true;
                return
            }

            if (params.last) {
                this.boxWidth = $boxEle.width();
                this.boxHeight = $boxEle.height();
                this.boxTop = (parseInt($boxEle.css('top')));
                this.boxLeft = (parseInt($boxEle.css('left')));
                this.$emit('drag-complete', [this.boxWidth / this.ratio, this.boxHeight / this.ratio, this.boxTop / this.ratio, this.boxLeft / this.ratio]);
                this.dragged = false;
                return
            }

            var nRatio = this.firstDragEleHeight / this.firstDragEleWidth;
            if (this.direction === 'east') {
                boxWidth = parseInt($boxEle.css('width'));
                left = boxWidth;
                distanceWidth = (left + params.deltaX * this.ratio);
                distanceHeight = distanceWidth * nRatio;
                boxEletop = this.firstDragELeTop - (distanceHeight - this.firstDragEleHeight) * this.ratio / 2;
                boxLeft = left;
                if (distanceWidth < this.minVal) {
                    return false;
                }
                if (distanceWidth > this.maxVal) {
                    return false;
                }
                if (top < 0) {
                    return false;
                }

                if (top + distanceHeight > $certPicBox.height()) {
                    return false;
                }
                if (left + distanceWidth > $certPicBox.width()) {
                    return false
                }
                el.style.left = (distanceWidth) + 'px';
                $boxEle.width(distanceWidth);
                $boxEle.css('top', top).find('.certificate-view-box>img').css('top', top)

            }

            if (this.direction === 'north') {
                boxHeight = parseInt($boxEle.css('height'));
                top = boxHeight;
                distanceHeight = (top + (-params.deltaY) * this.ratio);
                distanceWidth = distanceHeight / nRatio;
                boxLeft = left = this.firstDragEleLeft - (distanceWidth - this.firstDragEleWidth) * this.ratio / 2;
                boxEleTop = this.firstDragELeTop - (distanceHeight - this.firstDragEleHeight) * this.ratio;


                if (distanceWidth < this.minVal) {
                    return false;
                }
                if (distanceWidth > this.maxVal) {
                    return false;
                }

                if (boxEleTop < 0) {
                    return false
                }
                if (left < 0) {
                    return false
                }
                if (left + distanceWidth > $certPicBox.width()) {
                    return false
                }

                el.style.top = (distanceHeight) + 'px';
                $boxEle.width(distanceWidth);

                $boxEle.css({
                    'left': left,
                    'top': boxEleTop
                }).find('.certificate-view-box>img').css({
                    'left': left,
                    'top': boxEleTop
                });
            }


            if (this.direction === 'west') {
                boxWidth = parseInt($boxEle.css('width'));
                left = boxWidth;
                distanceWidth = (left + (-params.deltaX) * this.ratio);
                distanceHeight = distanceWidth * nRatio;
                boxEleTop = top = this.firstDragELeTop - (distanceHeight - this.firstDragEleHeight) * this.ratio / 2;
                boxLeft = this.firstDragEleLeft - (distanceWidth - this.firstDragEleWidth) * this.ratio;

                if (distanceWidth < this.minVal) {
                    return false;
                }
                if (distanceWidth > this.maxVal) {
                    return false;
                }

                if (top < 0) {
                    return false
                }

                if (boxLeft < 0) {
                    return false;
                }

                if (top + distanceHeight > $certPicBox.height()) {
                    return false
                }

                el.style.left = (distanceWidth) + 'px';
                $boxEle.width(distanceWidth);
                $boxEle.css({
                    'left': boxLeft,
                    'top': top
                }).find('.certificate-view-box>img').css({
                    'left': boxLeft,
                    'top': top
                });
            }

            if (this.direction === 'south') {
                boxHeight = parseInt($boxEle.css('height'));
                top = boxHeight;
                distanceHeight = (top + (params.deltaY) * this.ratio);
                distanceWidth = distanceHeight / nRatio;
                boxLeft = left = this.firstDragEleLeft - (distanceWidth - this.firstDragEleWidth) * this.ratio / 2;
                boxEleTop = this.firstDragELeTop - (distanceHeight - this.firstDragEleHeight) * this.ratio;


                if (distanceWidth < this.minVal) {
                    return false;
                }
                if (distanceWidth > this.maxVal) {
                    return false;
                }


                if (left < 0) {
                    return false
                }
                if (left + distanceWidth > $certPicBox.width()) {
                    return false
                }

                if (boxEleTop + distanceHeight > $certPicBox.height()) {
                    return false;
                }

                el.style.top = (distanceHeight) + 'px';
                $boxEle.width(distanceWidth);

                $boxEle.css({
                    'left': left
                }).find('.certificate-view-box>img').css({
                    'left': left
                });
            }


        }
    },
    mounted: function () {
        this.certPicBox = $('.cert-pic-box');
        this.boxEle = $(this.$el).parents(this.boxCls)
    }
});