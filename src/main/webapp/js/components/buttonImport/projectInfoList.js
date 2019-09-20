
'use strict'

Vue.component('project-info-cell',{
    template:'<div class="project-info-cell">'+
             '       <p class="over-flow-tooltip"><el-tooltip class="item" effect="dark" :content="row.pName" placement="top">\n'+
             '          <a :href="href" class="black-a" style="display: inline-block;max-width:100%;font-weight: bold;">{{row.pName}}</a>\n'+
             '       </el-tooltip></p>\n'+
             '       <p><i class="iconfont icon-dibiao2"></i><el-tooltip class="item" effect="dark" :content="row.officeName" placement="bottom"><span class="over-flow-tooltip" style="display: inline-block;max-width:90%;vertical-align: bottom">{{row.officeName}}</span></el-tooltip></p>\n'+
             '</div>',
    props:{
        row:Object
    },
    computed:{
        href:{
            get:function(){
                return this.frontOrAdmin + '/promodel/proModel/viewForm?id=' + this.row.id
            }
        }
    }
})