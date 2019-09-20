(function(name,definition){
	var hasDefine = typeof define === 'function';
	var hasExports = typeof module !== 'undefined' && module.exports;
	if(hasDefine){
		define(definition)
	}else if(hasExports){
		module.exports = definition()
	}else{
		this[name] = definition()
	}
})('exhibition',function(){
	var exhibition;
	exhibition = new Vue({
		el: "#exhibition",
		// components: {MyModal},
		data: function () {
			return {
				nodes: [{
					parent:{
						name: ''
					},
					role: {
						name: ''
					}
				}],
				createModal: {
					show: false
				},
				hasSubNode: false
			}
		},
		filters: {
			cutText: function (val) {
				var valStr;
				if(val){
					valStr = val.toString();
					valStr = valStr.substring(valStr.indexOf('/')+1);
					return valStr
				}
			}
		},
		methods: {
			createNode: function () {
				this.createModal.show = true;
			},
			createModalOk: function () {

			},
			createModalCancel: function () {

			},
			edit: function () {

			},
			save: function () {

			},
			getNodeList: function () {

			},
			line: function(isAngle,css,direction){
				var directionClass = '';
				var span = '';
				if(isAngle){
					directionClass +=' '+ direction + '-angle';
				}
				span += ('<span class="line'+ directionClass +'" style="left:'+ css.left +';top:'+ css.top +';"></span>');
				return span;
			},
			span: function(left,top,width,height){
				return '<span style="display: block;position: absolute;left: '+left+'px;top:'+top+'px;width:'+width+'px;height: '+height+'px;background-color: #797979"></span>'
			},
			setAutoWidth: function () {
				var $flowWorks = $('#flowWork');
				var $exhibition = $('#exhibition');
				var exhibitionWidth = $exhibition.width();
				var $fwItem = $flowWorks.find('.fw-item');
				var fwItemWidth = $fwItem.eq(0).width();
				var maxNum = Math.floor(exhibitionWidth/(fwItemWidth+88));
				var width = fwItemWidth * maxNum + 88 * (maxNum - 1) + 20;
				var $spanLine;
				var self = this;
				$flowWorks.width(width);
				if($fwItem.size() < 1){
					$flowWorks.find('>span').remove();
					return false;
				}
				$fwItem.each(function(i,item){
					// var $item = $(item);
					var ys = i%maxNum;
					var ysm;
					if(ys > 0){
						ysm = ys - 1
					}else{
						ysm = 0;
					}

					var rowYs = Math.ceil(i/maxNum);
					var halfHeight = (165/2);
					var top = rowYs * (165+88) - halfHeight - 88 + 15;
					if(i%maxNum == 0){
						$flowWorks.append($(self.span((maxNum*fwItemWidth+(maxNum-1)*88+15),top,20,2)));

						$flowWorks.append($(self.span((maxNum*fwItemWidth+(maxNum-1)*88+15+20),top,2,110)));

						$flowWorks.append($(self.span(102+15,top+108,(maxNum*fwItemWidth+(maxNum-1)*88+20-102),2)));

						if(i != 0){
							$flowWorks.append($(self.span(102+15,top+108,2,53)).addClass('line-bottom-angle'))
						}

					}else{
						var left = (ys)*(fwItemWidth)+(ysm*88) + 15;
						$spanLine = self.line(true,{
							left: left + 'px',
							top: top + 'px'
						},'right');
						$flowWorks.append($spanLine)
					}
				})
			}
		},
		watch: {
			nodes: {
				handler: function (list) {
					this.hasSubNode = list.every(function (item) {
						return item.parentId == '1';
					});
					this.$nextTick(function () {
						this.setAutoWidth();
					})
				},
				deep: true
			}
		},
		compiled: function () {
			this.setAutoWidth();
		}
	});
	return exhibition
});
