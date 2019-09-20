/**
 * Created by Administrator on 2018/9/18.
 */
'use strict';

Vue.component('e-student-profile-member', {
    template: '<div class="e-user-profile_member">' +
    '<div class="user-pic"><img :src="user.avatar || user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></div>' +
    '<div class="user-intro"><div class="user-name">{{name}}</div><div class="user-profession">{{profession}}</div><div class="user-mobile">{{user.mobile}}</div></div></div>',
    props: {
        user: Object,
        type: String,
        name: String,
        profession: String
    }
})

Vue.component('e-teacher-profile-member', {
    template: '<div class="e-user-profile_member">' +
    '<div class="user-pic"><img :src="user.avatar || user.photo | ftpHttpFilter(ftpHttp) | studentPicFilter"></div>' +
    '<div class="user-intro"><div class="user-name">{{name}}</div><div class="user-college">{{college}}</div><div class="user-source">{{source}}</div></div></div>',
    props: {
        user: Object,
        type: String,
        college: String,
        name: String,
        source: String
    }
})