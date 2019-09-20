
'use strict';

Vue.component('button-import-contest',{
    template:'\n' +
    '        <el-upload style="display: inline-block"\n' +
    '                   name="fileName"\n' +
    '                   accept=".xls,.xlsx,.zip"\n' +
    '                   :on-success="uploadSuccess"\n' +
    '                   :on-error="uploadError"\n' +
    '                   :show-file-list="false"\n' +
    '                   :disabled="isUploading || itemCurr == 0"\n' +
    '                   :before-upload="beforeUpload"\n' +
    '                   :action="frontOrAdmin + \'/iep/ie/uploadTpl?iepId=\'+ child.id +\'&actywId=\' + actywId + \'&gnodeId=\' + gnodeId">\n' +
    '            <div v-if="level == \'10\'" class="upload-img">' +
    '               <img class="circle-img" :src="circleSrc" alt="">' +

    '               <img v-if="item.step == \'1\'" :class="{\'flower-img\':true,\'hover-img\':isHoverImg}" :src="ctxImages +\'/white-flower.png\'" alt="">'+
    '               <img v-if="item.step == \'2\'" :class="{\'flower-img\':true,\'hover-img\':isHoverImg}" :src="ctxImages +\'/white-form.png\'" alt="">'+
    '               <img v-if="item.step == \'3\'" :class="{\'flower-img\':true,\'hover-img\':isHoverImg}" :src="ctxImages +\'/white-file.png\'" alt="">'+

    '               <img class="arrow-img" :src="arrowSrc" alt="">' +

    '               <div @mouseover="enterUploadBox" @mouseout="leaveUploadBox"></div>'+
    '            </div>'+
    '            <el-button v-if="level == \'20\'" :disabled="isUploading" size="mini" type="primary">\n' +
    '                <span v-if="isUploading">导入中...</span>\n' +
    '                <span v-else>{{child.name}}</span>\n' +
    '            </el-button>\n' +
    '        </el-upload>\n',
    props:{
        child:Object,
        actywId:String,
        gnodeId:String,
        level:Number,
        item:{
            type:Object,
            default:function () {
                return {}
            }
        },
        itemCurr:{
            type:Number,
            default:function () {
                return 1;
            }
        }
    },
    data:function () {
        return {
            isUploading:this.child.isUploading,
            circleSrc: this.itemCurr == 1 ? '/images/solid-rim.png' : '/images/solid-rim-white.png',
            arrowSrc: this.itemCurr == 1 ? '/images/upload-arrow.png' : '/images/upload-arrow-white.png',
            isHoverImg:false
        }
    },
    watch:{
        itemCurr:function (value) {
            this.circleSrc = value == 1 ? '/images/solid-rim.png' : '/images/solid-rim-white.png';
            this.arrowSrc = value == 1 ? '/images/upload-arrow.png' : '/images/upload-arrow-white.png';
        }
    },
    methods:{
        enterUploadBox:function () {
            if(this.itemCurr == 1){
                this.circleSrc = '/images/solid-rim-shadow.png';
                this.arrowSrc = '/images/upload-arrow-shadow.png';
                this.isHoverImg = true;
            }
        },
        leaveUploadBox:function () {
            if(this.itemCurr == 1){
                this.circleSrc = '/images/solid-rim.png';
                this.arrowSrc = '/images/upload-arrow.png';
                this.isHoverImg = false;
            }
        },
        uploadSuccess:function (response) {
            this.isUploading = false;
            if (response.status) {
                this.$emit('upload-success');
            }else {
                this.$message({
                    type: 'error',
                    message: response.msg
                })
            }
            // this.$message({
            //     type: response.status ? 'success' : 'error',
            //     message: response.msg
            // })
        },
        uploadError:function () {
            this.isUploading = false;
            this.$message({
                type: 'error',
                message: this.xhrErrorMsg
            })
        },
        beforeUpload:function () {
            this.isUploading = true;
            return true;
        }
    },
    create:function () {

    },
    mounted:function () {

    }
});