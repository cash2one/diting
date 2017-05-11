$(function () {

    'use strict';

    var console = window.console || {
            log: function () {
            }
        };
    var $image = $('#image');
    var $download = $('#download');
    var $dataX = $('#dataX');
    var $dataY = $('#dataY');
    var $dataHeight = $('#dataHeight');
    var $dataWidth = $('#dataWidth');
    var $dataRotate = $('#dataRotate');
    var $dataScaleX = $('#dataScaleX');
    var $dataScaleY = $('#dataScaleY');
    var options = {
        aspectRatio: 1 ,
        background:false,
        modal:true,
        zoomable:false,
        minContainerHeight:200,
        minCanvasWidth:100,
        minCanvasHeight:100,
        autoCrop:true,
        preview: '.img-preview',
        crop: function (e) {

            $dataX.val(Math.round(e.x));
            $dataY.val(Math.round(e.y));
            $dataHeight.val(Math.round(e.height));
            $dataWidth.val(Math.round(e.width));
            $dataRotate.val(e.rotate);
            $dataScaleX.val(e.scaleX);
            $dataScaleY.val(e.scaleY);
        }
    };


    // Cropper
    $image.on({
         // 'build.cropper': function (e) {
         // console.log(e.type);
         // },
         // 'built.cropper': function (e) {
         // console.log(e.type);
         // },
         // 'cropstart.cropper': function (e) {
         // console.log(e.type, e.action);
         // },
         // 'cropmove.cropper': function (e) {
         // console.log(e.type, e.action);
         // },
         // 'cropend.cropper': function (e) {
         // console.log(e.type, e.action);
         // },
         'crop.cropper': function (e) {
             if(e.x<0){
                 e.x=0
             }
             if(e.y<0){
                 e.y=0
             }
         // console.log(e.type, e.x, e.y, e.width, e.height, e.rotate, e.scaleX, e.scaleY);
             $('#cropParameter').val('?imageMogr2/crop/!'+ Math.round(e.width) +'x'+ Math.round(e.height) +'a'+  Math.round(e.x) +'a'+  Math.round(e.y))
             // console.log($('#cropParameter').val())
             if($('#RobotHeader').val()!=""){
                 $('#headPortrait1').val($('#RobotHeader').val()+$('#cropParameter').val())
                 // console.log($('#headPortrait1').val())
             }
         }
         // 'zoom.cropper': function (e) {
         // console.log(e.type, e.ratio);
         // }
    }).cropper(options);
    // Import image
    var $inputImage = $('#inputImage');
    var URL = window.URL || window.webkitURL;
    var blobURL;
    if (URL) {
        // $('#inputImage').live('change',function(){ 
    $inputImage.change(function () {
        $inputImage.css("display","none")
        
            var files = this.files;
            var file;

            if (!$image.data('cropper')) {
                return;
            }

            if (files && files.length) {
                file = files[0];

                if (/^image\/\w+$/.test(file.type)) {
                   
                    uploadImage("inputImage",this)
                   
                    blobURL = URL.createObjectURL(file);
                    var fileL = setInterval(function (){
                        if($('#RobotHeader').val()!=""){
                            $image.one('built.cropper', function () {
                                // Revoke when load complete
                                URL.revokeObjectURL(blobURL);
                            }).cropper('reset').cropper('replace', blobURL);
                           
                            $('#inputImage').val('');
                            clearInterval(fileL);
                        }else {
                            $('.row img').attr("src","/static/images/loading.gif")
                            $('.row img').css("margin","auto")
                        }

                    },1000);

                } else {
                    // window.alert('Please choose an image file.');
                   layer.alert('请选择一张图片');
                    $('#inputImage').val("")
                    $inputImage.css("display","block")
                }
            }
        
        });
    } else {
        $inputImage.prop('disabled', true).parent().addClass('disabled');
    }

    /**保存图像**/
    // $('.saveRobotPhoto').click(function () {
       
           
        
        // $(".onesPhoto").hide();
        // $(".personInformation").show();
        // var result = $image.cropper("getCroppedCanvas");   //拿到剪裁后的数据
        // console.log(result.toDataURL())
     // console.log(URL.createObjectURL(dataURLtoBlob(result.toDataURL())));
     // console.log(URL.createObjectURL(dataURLtoBlob(result.toDataURL())).replace("blob:",""));
     //    var responseUrl = URL.createObjectURL(dataURLtoBlob(result.toDataURL())).replace("blob:", "")
     //    console.log(responseUrl);
//         getResponseUrl()
//     });
    

    // 获取裁剪后图片url
    function getResponseUrl() {
        var result = $image.cropper("getCroppedCanvas");   //拿到剪裁后的数据
        return URL.createObjectURL(dataURLtoBlob(result.toDataURL())).replace("blob:", "");
    }

    function dataURLtoBlob(dataurl) {
        var arr = dataurl.split(','), mime = arr[0].match(/:(.*?);/)[1],
            bstr = atob(arr[1]), n = bstr.length, u8arr = new Uint8Array(n);
        while (n--) {
            u8arr[n] = bstr.charCodeAt(n);
        }
        return new Blob([u8arr], {type: mime});
    }

});



// var imgLink1 = Qiniu.imageMogr2({
//     // 布尔值，是否根据原图EXIF信息自动旋正，便于后续处理，建议放在首位。
//     strip: true,   // 布尔值，是否去除图片中的元信息
//     thumbnail: '!750x450r',   // 缩放操作参数
// //	       crop: '!750x450a0a0',  // 裁剪操作参数
//     gravity: 'NorthWest',    // 裁剪锚点参数
//     quality: 100,  // 图片质量，取值范围1-100
// //	       rotate: 20,   // 旋转角度，取值范围1-360，缺省为不旋转。
// //	       format: 'png',// 新图的输出格式，取值范围：jpg，gif，png，webp等
// //	       blur:'3x5'    // 高斯模糊参数
// }, res.key);

// CompressImg:function(imgUrl, width, height) {
//     return imgUrl + (imgUrl.indexOf("?") > 1 ? '&' : '?') + 'imageView/1/w/' + width + '/h/' + height +'/q/100/format/png';
// }

