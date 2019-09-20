;(function(name,definition){
	var hasDefine = typeof define === 'function';
	var hasExports = typeof module !== 'undefined' && module.exports;
	if(hasDefine){
		define(definition)
	}else if(hasExports){
		module.exports = definition()
	}else{
		window[name] = definition();
	}
}('node',function(){
	var node = new Vue({
        el: "#app",
        // components: {MyModal,AddParentNode,AddChildNode},
        // components:{myComponent},
        data: function () {
            return {
                nodes: [{
                    name: '立项审核',
                    level: '01',
                    underWork: '国创项目申报审批流程',
                    description: '',
                    childrenNodes: [{
                        name: '待学院教学秘书审核',
                        level: '0102',
                        description: '自定义立项审核、中期检查、项目结项审核流程',
                    }, {
                        name: '待学校管理员审核',
                        level: '0102',
                        description: '自定义非国创项目申报，针对泛创业项目申报、审核流程的管理和配置',
                    }]
                }, {
                    name: '中期检查',
                    level: '02',
                    underWork: '国创项目申报审批流程',
                    description: '自定义互联网+大赛报名及审核流程，可根据学校的审批架构灵活配置',
                    childrenNodes: [{
                        name: '待院级专家评分',
                        level: '0201',
                        description: '',
                    }, {
                        name: '待院级教学秘书审核',
                        level: '0202',
                        description: '',
                    }, {
                        name: '待校级专家评分',
                        level: '0203',
                        description: '',
                    },{
                        name: '待校级管理员审核',
                        level: '0204',
                        description: '自定义科研成果的申报及审核流程，可以根据学校的科研成果审批架构灵活配置',
                    }]
                }, {
                    name: '结项审核',
                    level: '03',
                    underWork: '国创项目申报审批流程',
                    description: '',
                    childrenNodes: [{
                        name: '待院级专家评分',
                        level: '0301',
                        description: '对C级和B级项目的结项材料进行网评',
                    }, {
                        name: '待院级教学秘书审核',
                        level: '0302',
                        description: '录入C级和B级项目的答辩分数及结果',
                    }, {
                        name: '待校级专家评分',
                        level: '0303',
                        description: '对A级和A+级项目的结项材料进行网评',
                    },{
                        name: '待校级管理员审核',
                        level: '0304',
                        description: '录入A级和A+级项目的答辩分数及结果',
                    }]
                }],
                createNodeShow:false,
                parentNodeData:[{
                    flowPath:'',
                    nodeName:'',
                    nodeLevel:'',
                    nodeDesc:''
                }],
                childNodeData:[{
                    flowPathParent:'',
                    nodeParentLevel:'',
                    childNodeName:'',
                    nodeChildLevel:'',
                    childNodeDesc:''
                }],
                CNtitle:'',
                tableAddCNodeShow:false,
                tableEditPNodeShow:false,
                editableCn:false,
                deleteItemShow:false,
                deletText:'确认删除这条数据吗？'
            }
        },
        events:{
            addNodesEvent:function(nodeData){
                this.parentNodes = nodeData;
            },
            addCNodesEvent:function(nodeData){
                this.childNodeData = nodeData;
            }
        },
        methods: {
            createNodeModal:function(){
                this.createNodeShow = true;
            },

            createNodeOk:function(){
                this.clearNodeData();
            },
            createNodeCancel:function(){
                this.clearNodeData();
            },

            CNAddOk:function(){

            },
            CNCancel:function(){
                this.clearNodeData();
            },
            openCNModal:function(index,row){
                this.tableAddCNodeShow = true;
                this.editableCn = true;
                this.CNtitle = '添加子节点'
            },
            openPNModal:function(index,row){
                this.tableEditPNodeShow = true;
                this.parentNodeData[0]['flowPath'] =  this.nodes[index]['underWork'];
                this.parentNodeData[0]['nodeName'] = row['name'];
                this.parentNodeData[0]['nodeLevel'] = row['level'];
                this.parentNodeData[0]['nodeDesc']= row['description'];
            },
            PNAddOk:function(){

            },
            PNCancel:function(){
                this.clearNodeData();
            },
            openEditCNModal:function(index,row,node){
                this.tableAddCNodeShow = true;
                this.editableCn = false;
                this.childNodeData[0]['flowPathParent'] = node['name'];
                this.childNodeData[0]['nodeParentLevel'] = node['level'];
                this.childNodeData[0]['childNodeName'] = row['name'];
                this.childNodeData[0]['nodeChildLevel'] = row['level'];
                this.childNodeData[0]['childNodeDesc'] = row['description'];
                this.CNtitle = '编辑'

            },
            clearNodeData:function(){
                this.parentNodeData = [{
                    flowPath:'',
                    nodeName:'',
                    nodeLevel:'',
                    nodeDesc:''
                }]
                this.childNodeData = [{
                    flowPathParent:'',
                    nodeParentLevel:'',
                    childNodeName:'',
                    nodeChildLevel:'',
                    childNodeDesc:''
                }]
            },
            deleteRow:function(index,node){
                this.deleteItemShow = true;
            },
            deleteOk:function(){

            },
            deletCancel:function(){

            }
        }
    })
	return node;
}))