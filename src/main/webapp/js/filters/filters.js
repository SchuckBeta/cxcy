/**
 * Created by Administrator on 2018/6/14.
 */


'use strict';



    //应用于默认项目图像
    Vue.filter('proDefaultImg', function (value) {
        if (!value) return '/images/default-pic.png';
        return value;
    })
    //应用于默认项目图像
    Vue.filter('userDefaultImg', function (value) {
        if (!value) return '/images/default-user-pic.png';
        return value;
    })
    //应用于时间
    Vue.filter('formatDate', function (value, format) {
        if(value){
            return moment(value).format(format || 'YYYY-MM-DD');
        }
        return '';
    })

    //应用于时间
    Vue.filter('getSelectedVal', function (val, entity, props) {
        props = props || { label: 'label', value: 'value' }
        var label = props['label'];
        var value = props['value'];
        if(val && entity[val]){
            return entity[val][label];
        }
        return '';
    })

    //应用于多选
    Vue.filter('getCheckboxValue', function (value, entity) {
        if(!value || !entity) return;
        var labels = [];
        if({}.toString.call(value) === '[object String]'){
            value = value.split(',')
        }
        value.forEach(function (item) {
            if(entity[item]){
                labels.push(entity[item].label)
            }
        });
        return labels.join('；')
    })


    //应用于人才库
    Vue.filter('getUserCollege', function (user, entity) {
        var professional = user.professional;
        var officeId = user.officeId;
        var names = [];
        if(officeId !== professional){
            if(!!officeId){
                entity[officeId] && names.push(entity[officeId].name);
            }
        }
        if(!!professional){
            entity[professional] && names.push(entity[professional].name);
        }
       return names.join('/')
    })

    Vue.filter('hideZero', function (value) {
        if(typeof value !== 'undefined'){
            value = parseFloat(value);
            return value;
        }
        return '-';
    })

    Vue.filter('cardPhoto', function (value) {
        if(!value){
            return '/img/u4110X185X228.png';
        }
        return value;
    })

    Vue.filter('dateAddDays', function (value, date, days) {
        if (!value || days == '-1' || date == '' || typeof date == 'undefined') {
            return value;
        }
        var nDate = new Date(moment(date).format());
        var nDay = nDate.getDate() + 1;
        nDay += parseInt(days);
        nDate.setDate(nDay);
        nDate = moment(nDate).format('YYYY-MM-DD');
        return value + ' （' + nDate + '）';
    })

    Vue.filter("hideNo", function (value, hasJoin) {
        if (value && typeof hasJoin !== 'undefined') {
            if (!hasJoin) {
                return value.replace(value.substr(1, value.length - 2), '****')
            }
        }
        return value;
    })

    Vue.filter("hideMobile", function (value, hasJoin) {
        if (value && typeof hasJoin !== 'undefined') {
            if (!hasJoin) {
                return value.replace(/\d{3}(\d{4})/, function ($1, $2) {
                    return $1.replace($2, '****')
                })
            }
        }
        return value;
    })
    // Vue.filter('textLineFeedFilter', function (value) {
    //     if(!value) return '';
    //     return value
    // })
    Vue.filter("getRoomNames", function (value, entries, key, roomName, num) {
        var names = [];
        var parent = entries[value];
        key = key || 'pId';
        while (parent) {
            if (parent[key] === '1') {
                names.unshift(parent.name);
                break;
            }
            names.unshift(parent.name);
            parent = entries[parent[key]];
        }
        if (roomName) {
            names.push(roomName)
        }
        names = names.slice(1);
        names = names.join('/');
        if (typeof num !== 'undefined') {
            return names + '(' + num + ')';
        }
        return names;
    })

    Vue.filter('userCountryName', function (value, cityIdKeyData) {
        if (!value) {
            return '';
        }
        if (cityIdKeyData[value]) {
            return cityIdKeyData[value].shortName
        }
        return value
    });

    //用户详情页地址跳转
    Vue.filter('goToUserPage', function (value) {
        var userType = value.user_type || value.userType;
        if (userType === '1') return '/f/sys/frontStudentExpansion/form?id=' + (value.st_id || value.id);
        return '/f/sys/frontTeacherExpansion/view?id=' + (value.te_id || value.id);
    })

    //单个profession查找上级
    Vue.filter('getProfessionName', function (value, entries) {
        if (!value || !entries) return '';
        var profession = entries[value];
        if(!profession){
            return '';
        }
        var office = entries[profession.parentId];
        var text = '';
        if (office && profession.parentId !== '1') {
            text += office.name + '/';
        }
        text += profession.name;
        return text;
    })

    //用户导师详情
    Vue.filter('userRoleName', function (value, userId) {
        if (!value) return '';
        if (userId === value.leaderId) {
            return '项目负责人'
        } else {
            if (value.userType === '1') {
                return '组成员'
            }
        }
        return '导师'
    })

    //用户导师详情 时间周期
    Vue.filter('userProContestDRange', function (value) {
        var startDate, endDate;
        if (!value) return '';
        if (value.startDate) {
            startDate = moment(value.startDate).format('YYYY-MM-DD');
        }
        if (value.endDate) {
            endDate = moment(value.endDate).format('YYYY-MM-DD');
        }
        if (startDate) {
            return startDate + '至' + endDate;
        }
        return ''
    })

    Vue.filter('textEllipsis', function (value, size) {
        if (!value) return '';
        size = size || 30;
        value = value.toString();
        if (value.length <= size) {
            return value;
        }
        return value.substring(0, size - 2) + '...';
    })

    Vue.filter("textMiddleEllipsis", function (value, count) {
        if (!value) return '';
        count = count || 20;
        value = value.toString();
        if(value.length < count ){
            return value;
        }
        return value.substring(0, count - 7) + '...' + value.substring(value.length - 7);
    })

    //默认项目大赛图片
    Vue.filter('defaultProPic', function (value) {
        if (!value) return '/img/video-default.jpg';
        return value;
    })


    Vue.filter('ftpHttpFilter', function (value, ftpHttp) {
        if (!value) return '';
        return ftpHttp+value;
    })

    Vue.filter('studentPicFilter', function (value) {
        if (!value) return '/img/u4110.png';
        return value;
    })

    Vue.filter('proGConLogo', function (value) {
        if (!value) return '';
        if(value.url){
            return value.url;
        }
        return value;
    })

    Vue.filter('proGConPicFilter', function (value) {
        if (!value) return '/images/default-pic.png';
        return value;
    })

    Vue.filter('selectedFilter', function (value, entries, isShowOrigin) {
        if (typeof value === 'undefined' || !entries) {
            return '-'
        };
        if(entries[value]){
            return entries[value];
        }
        if(isShowOrigin){
            return value
        }
        return '';
    });

    Vue.filter('collegeFilter', function (value, entries) {
        if (!value) {
            return ''
        };
        if (!entries[value]) {
            return ''
        }
        return entries[value].name;
    });

    Vue.filter('cascaderCollegeFilter', function (value, entries) {
        var names = [], len;
        if (!value) return '';
        if (value.length < 1) {
            return '';
        }
        var i = value.length - 1;
        while (i > -1){
            var college = entries[value[i]];
            if(value.length === 2){
                if(value[i] === '1'){
                    break;
                }
            }
            if(college){
                names.unshift(college.name);
            }
            i--;
        }
        return names.join('/')
    })

    Vue.filter('checkboxFilter', function (value, entries) {
        var arr = [];
        if (!value || value.length < 1 || !entries) {
            return '';
        }
        if ({}.toString.call(value) !== '[object Array]') {
            value = value.split(',')
        }
        value.forEach(function (item) {
            if(entries[item]){
                arr.push(entries[item]);
            }
        });
        return arr.join('；')
    });

    Vue.filter('filterPwRoomAddress', function (value, entries) {
        var parentId = value.parentId || value.pId;
        var address = [value.name];
        while (parentId) {
            var parent = entries[parentId];
            if (!parent || parent.type == '0') break;
            address.unshift(parent.name);
            parentId = parent.parentId || parent.pId;
            if (value.floorNum) {
                address.push(value.floorNum + '层');
            }
        }
        return address.join('/');
    })

    Vue.filter('filterPwEnterType', function (value, entries) {
        var types = ['eteam', 'eproject', 'ecompany'];
        var i = 0;
        var enterTypes = [];
        while (i < types.length) {
            if (value[types[i]]) {
                enterTypes.push(entries[value[types[i]].type]);
            }
            i++;
        }
        return enterTypes.join('-')
    })

    Vue.filter('formatDateFilter', function (value, pattern) {
        if (!value) return '-';
        return moment(value).format(pattern)
    })

    Vue.filter('fileSuffixFilter', function (ext) {
        var extname;
        switch (ext) {
            case "xls":
            case "xlsx":
                extname = "excel";
                break;
            case "doc":
            case "docx":
                extname = "word";
                break;
            case "ppt":
            case "pptx":
                extname = "ppt";
                break;
            case "jpg":
            case "jpeg":
            case "gif":
            case "png":
            case "bmp":
                extname = "image";
                break;
            case 'txt':
                extname = 'txt';
                break;
            case 'zip':
                extname = 'zip';
                break;
            case 'rar':
                extname = 'rar';
                break;
            default:
                extname = "unknow";
        }
        return '/img/filetype/' + extname + '.png'
    })

