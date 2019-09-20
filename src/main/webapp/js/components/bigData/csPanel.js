
'use strict';

Vue.component('cs-panel', {
    template:   '<div class="cs-panel">' +
                    '<div class="cs-panel-header">' +
                        '<span class="cs-ph-label">{{ label }}</span>' +
                        '<div v-if="$slots.header" class="cs-ph-header">' +
                            '<div class="cs-ph-wrapper"><slot name="header"></slot></div>' +
                        '</div>' +
                    '</div>' +
                    '<div class="cs-panel-body"><slot></slot></div>' +
                '</div>',
    props: {
        label: String
    }
});
