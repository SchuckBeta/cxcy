/**
 * Created by Administrator on 2018/7/20.
 */



'use strict';

Vue.component('e-panel', {
    template: '<div class="panel"><template v-if="$slots.label || label"><div class="panel-header panel-header-title"><slot name="label"></slot><span v-if="label"></span></div></template><div class="panel-body"><slot></slot></div></div> ',
    props: {
        label: String
    }
})