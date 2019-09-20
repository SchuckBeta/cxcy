/**
 * Created by Administrator on 2019/1/4.
 */


Vue.regExpMixin = {
    data: function () {
        return {
            posInteger: /^[1-9]{1}\d*$/,      //正整数
            onlyNumLetter: /^[a-zA-Z0-9]+$/, //只能输入字母和数字一个或者多个
            scoCourseScore: /^(([1-9]{1}\d{0,2})(\.{1}(?=\d+))|[1-9]{1}[0-9]{0,1})\d{0,1}$/, //课程学分认定标准 计划学分，合格成绩 3位整数，无小数或者一位小数
        }
    }
}