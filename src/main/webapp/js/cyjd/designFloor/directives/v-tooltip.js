/**
 * Created by Administrator on 2017/12/27.
 */


Vue.directive('tooltip',{
    inserted: function (element, binding, vnode) {
        $(element).tooltip({
            container: 'body'
        })

        $(element).hover(function () {
            $(this).tooltip('show')
        }, function () {
            // $(this).tooltip('hide')
        })

    }
})