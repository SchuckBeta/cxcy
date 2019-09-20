/*
*
* */


+function ($) {

    function SvgPaper(id, option) {
        this.svgEle = Snap(id);
        this.options = $.extend(true, SvgPaper.DEFAULT, (option || {}));
        this.overallGroup = this.geneOverallGroup();
        this.matrix = new Snap.Matrix();
        this.init()
    }

    SvgPaper.DEFAULT = {};

    SvgPaper.prototype.init = function () {
        var overallBBox = this.overallGroup.getBBox();
        this.createFlow();
        this.svgEle.toJSON();
        var maxTranslateXY = this.getMinTXMaxTY();


        console.log(maxTranslateXY)
        // if(maxTranslateXY){
        //     var scale =1;
        //     this.overallGroup.attr({
        //         'transform': 'matrix('+scale+',0,0,'+scale+','+((maxTranslateXY['width']-maxTranslateXY['width']*scale)/2+(maxTranslateXY['minTranslateX']-maxTranslateXY['minTranslateX']*scale))+','+0+')'
        //     })
        // }
        this.setGroupScale(this.overallGroup);

    };


    //生成控制所有放大缩小的组
    SvgPaper.prototype.geneOverallGroup = function () {
        var g = this.svgEle.group();
        g.addClass('overall-scale');
        return g;
    };

    /*
    @params scale default '1,1'
    * */
    SvgPaper.prototype.setGroupScale = function (group, scale) {
        var scaleValue = (scale || '1');
        var matrix = new Snap.Matrix();
        matrix.scale(2,2,145,340)
        group.transform(matrix.toTransformString());

    };

    /*
    * @params rotate [angle, centerX, centerY]*/
    SvgPaper.prototype.setGroupRotate = function (group, rotate) {
        if (rotate) {
            group.attr({
                'transform': 'rotate(' + rotate + ')'
            })
        } else {
            group.attr({
                'transform': 'matrix(1,0,0,1,0,0)'
            })
        }
    }

    SvgPaper.prototype.setGroupTranslate = function (group, translate) {
        var translateValue = translate || '0,0';
        group.attr({
            'transform': 'translate(' + translateValue + ')'
        })
    }


    //获取所有translateX,translateY, Math.max值
    //返回数组[translateX, translateY] or false
    SvgPaper.prototype.getMinTXMaxTY = function () {
        var translateGroups = this.svgEle.selectAll('.v-translate');
        var translateXs = [], translateYs = [], minTranslateX, maxTranslateY,width,height;
        if (translateGroups.length < 1) {
            return false;
        }
        translateGroups.forEach(function (t) {
            var BBox = t.getBBox();
            translateXs.push(BBox.x);
            translateYs.push(BBox.y);
        });
        minTranslateX = Math.min.apply(null, translateXs);
        maxTranslateY = Math.max.apply(null, translateYs);
        $.each(translateXs, function (i, x) {
            if(x === minTranslateX){
                width = translateGroups[i].getBBox().width;
                return false;
            }
        })


        return {
            minTranslateX: minTranslateX,
            maxTranslateY: maxTranslateY,
            width: width
        }
    }


    //构造假数据流程测试
    SvgPaper.prototype.createFlow = function () {
        var translateGroup, scaleGroup, rotateGroup, rect;
        for (var i = 0; i < 10; i++) {
            translateGroup = this.svgEle.group().addClass('v-translate');
            scaleGroup = this.svgEle.group().addClass('v-scale');
            rotateGroup = this.svgEle.group().addClass('v-rotate');

            rect = this.svgEle['rect'](0, 0, 1*100+100, 50);
            rect.attr({
                fill: 'transparent',
                style: 'stroke: #333'
            });
            this.setGroupTranslate(translateGroup, [(i*10+100), (i * 50 + (i > 0 ? (i - 1) * 20 + 20 : 0))].join(','))
            this.setGroupScale(scaleGroup)
            this.setGroupRotate(rotateGroup)
            rotateGroup.add(rect);
            scaleGroup.add(rotateGroup);
            translateGroup.attr('model-id','122323').add(scaleGroup);
            this.overallGroup.add(translateGroup);
        }
    };


    window['SvgPaper'] = SvgPaper;

}(jQuery);

