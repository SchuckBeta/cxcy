/**
 * Created by Administrator on 2017/12/13.
 */


+function($){
    var fullCalendar = Vue.component('full-calendar', {
        template: ' <div id="calendar" v-calendar="calendarConfig"></div>',
        model: {
            prop: 'eventList',
            event: 'change'
        },
        props: {
            eventList: {
                type: Array,
                default: []
            },
            minTime: {
                type: String,
                default: '08:00',
            },
            maxTime: {
                type: String,
                default: '22: 10'
            }
        },
        data: function () {
            return {
                calendar: '',
                calendarConfig: {

                }
            }
        },
        computed: {

        },
        watch: {
          eventList: {
              deep: true,
              handler: function () {
                this.calendarConfig.event = this.eventList;
              }
          }  
        },

        methods: {

        },
        beforeMount: function () {
            this.calendarConfig.minTime = this.minTime;
            this.calendarConfig.maxTime = this.maxTime;
        },
        mounted: function(){

        }
    })
}(jQuery);