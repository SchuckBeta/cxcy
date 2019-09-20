/**
 * Created by Administrator on 2018/6/14.
 */


;+function (Vue) {
    Vue.mixin({
        methods: {
            getEntries: function (data, defaultProps) {
                var i = 0, entries = {};
                defaultProps = defaultProps || {value: 'value', label: 'label'};
                if(!data || data.length < 1){
                    return null;
                }
                while (i < data.length){
                    entries[data[i][defaultProps.value]] = data[i][defaultProps.label];
                    i++
                }
                return entries;
            }
        }
    })
}(Vue)