(function (name, definition) {
    var hasDefine = typeof define === 'function';
    var hasExports = typeof module !== 'undefined' && module.exports;
    if (hasDefine) {
        define(definition)
    } else if (hasExports) {
        module.exports = definition()
    } else {
        this[name] = definition()
    }
})('baseData', function () {
    var baseData = new Vue({
        el: '#baseData',
        components:{MyModal},
        data: function () {
            return {
                projectTypes: [{
                    name: 'A级（国家级）',
                    level: '项目申报级别',
                    levelId:1,
                    sortNumber: 0
                },{
                	name:'B级（校级）',
                	level:'项目申报级别',
                    levelId:1,
                	sortNumber: 0
                },{
                	name:'C级（院级）',
                	level:'项目申报级别',
                    levelId:1,
                	sortNumber: 0
                },{
                	name:'D级',
                	level:'项目申报级别',
                    levelId:1,
                	sortNumber: 0
                },{
                	name:'国创项目',
                	level:'项目类型',
                    levelId:6,
                	sortNumber: 1
                }],
                ptTypes:[{
                	label:'项目申报级别',
                	value:1
                },{
                	label:'项目类型',
                	value:2
                },{
                	label:'大赛类型',
                	value:3
                }],
                modalAddEdit:{
                    title:'',
                    show:false
                },
                modalDelete:{
                    title:'',
                    show:false
                },
                modalAdd:{
                    show:false
                },
                ptTypeVal:0,
                metaKey:'',
                ptTypeName:'',
                menuCollaspe:true

            }
        },
        methods: {
            editPtType: function (currIndex, row) {
            	this.modalAddEdit.title = '编辑';
            	this.modalAddEdit.show = true;
                this.ptTypeVal = row.levelId;
                this.ptTypeName = row.name;
            },
            deletePtType: function (currIndex, row) {
            	this.modalDelete.title="删除";
            	this.modalDelete.show = true;
            },
            modalAddEditOk:function(){

            },
            modalDeleteOk:function(){

            },
            modalAddEditCancel:function(){

            },
            modalDeleteCancel:function(){

            },
            modalAddOk:function(){

            },
            modalAddCancel:function(){

            },
            formSearch:function(){
                var metaKey = this.metaKey
            },
            formCreate:function(){
                this.modalAddEdit.title = '创建类别';
                this.modalAddEdit.show = true;
                this.ptTypeVal = 0;
                this.ptTypeName = ''
            },
            formAdd:function(){
                this.modalAdd.show = true;
            },
            offset:function(el){
                var totalLeft  = null;
                var totalTop = null;
                var parentEl = el.offsetParent;
                totalTop += el.offsetTop;
                totalLeft += el.offsetLeft;
                while(parentEl){
                    if(navigator.userAgent.indexOf("MSIE 8.0") === -1){
                        totalLeft += parentEl.clientLeft;
                        totalTop += parentEl.clientTop;
                    }
                    totalLeft+= parentEl.clientLeft;
                    totalTop += parentEl.clientTop;
                    parentEl = el.offsetParent;
                }
                return {left:totalLeft,top:totalTop}
            },
            
        },
        transitions:{
            collapse:{
                enter :function(el){
                    var nav = el.querySelector('.bd-nav');
                    var height = nav.offsetHeight;
                    el.style.height = height+'px';
                },
                leave :function(el){
                    var nav = el.querySelector('.bd-nav');
                    el.style.height = '';
                }
            }
        },
        ready: function () {
           var $bdNav = $('.bd-nav');
           var bdNavOffsetTop = $bdNav.offset().top;
           var winHeight = $(window).height();
           $bdNav.css('max-height',winHeight-bdNavOffsetTop)

        }
    })
    return baseData;
})