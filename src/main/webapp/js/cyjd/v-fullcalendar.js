/**
 * Created by Administrator on 2017/12/13.
 */

+function ($) {

    Vue.directive('calendar', {
        inserted: function (element, binding, vnode) {
            var defaultOption = {
                defaultView: 'agendaWeek',
                weekends: true,
                slotDuration: '00:30:00',
                slotLabelFormat: 'HH:mm',
                minTime: '08:00',
                maxTime: '22: 10',
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'agendaWeek'
                },
                allDaySlot: false,
                navLinks: true, // can click day/week names to navigate views
                editable: true,
                height: 'auto',
                selectable: true,
                selectConstraint: 'businessHours',
                schedulerLicenseKey: 'GPL-My-Project-Is-Open-Source',
                // events: []
            };
            var options = $.extend({}, binding.value, defaultOption);
            console.log(options)
            vnode.context.calendar = $('#'+element.id).fullCalendar(options);
        }
    })

}(jQuery)