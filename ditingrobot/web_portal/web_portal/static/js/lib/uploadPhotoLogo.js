$(function () {

    'use strict';

    var console = window.console || {
            log: function () {
            }
        };
    var $image = $('#image');
    var $download = $('#download');
    // var $dataX = $('#dataX');
    // var $dataY = $('#dataY');
    // var $dataHeight = $('#dataHeight');
    // var $dataWidth = $('#dataWidth');
    // var $dataRotate = $('#dataRotate');
    // var $dataScaleX = $('#dataScaleX');
    // var $dataScaleY = $('#dataScaleY');
    var options = {
        aspectRatio: 242 / 63,
        preview: '.img-preview',
        crop: function (e) {
            // $dataX.val(Math.round(e.x));
            // $dataY.val(Math.round(e.y));
            // $dataHeight.val(Math.round(e.height));
            // $dataWidth.val(Math.round(e.width));
            // $dataRotate.val(e.rotate);
            // $dataScaleX.val(e.scaleX);
            // $dataScaleY.val(e.scaleY);
        }
    };


    // Cropper
    $image.on({
        /*'build.cropper': function (e) {
         console.log(e.type);
         },
         'built.cropper': function (e) {
         console.log(e.type);
         },
         'cropstart.cropper': function (e) {
         console.log(e.type, e.action);
         },
         'cropmove.cropper': function (e) {
         console.log(e.type, e.action);
         },
         'cropend.cropper': function (e) {
         console.log(e.type, e.action);
         },
         'crop.cropper': function (e) {
         console.log(e.type, e.x, e.y, e.width, e.height, e.rotate, e.scaleX, e.scaleY);
         },
         'zoom.cropper': function (e) {
         console.log(e.type, e.ratio);
         }*/
    }).cropper(options);


    // Import image
    var $inputImage = $('#inputImage');
    var URL = window.URL || window.webkitURL;
    var blobURL;

    if (URL) {
        $inputImage.change(function () {
            var files = this.files;
            var file;

            if (!$image.data('cropper')) {
                return;
            }

            if (files && files.length) {
                file = files[0];

                if (/^image\/\w+$/.test(file.type)) {
                    blobURL = URL.createObjectURL(file);
                    $image.one('built.cropper', function () {
                        // Revoke when load complete
                        URL.revokeObjectURL(blobURL);
                    }).cropper('reset').cropper('replace', blobURL);
                    $inputImage.val('');
                } else {
                    window.alert('Please choose an image file.');
                }
            }
        });
    } else {
        $inputImage.prop('disabled', true).parent().addClass('disabled');
    }

    /**保存图像**/
    $('.savePhoto').click(function () {
        // $(".onesPhoto").hide();
        // $(".personInformation").show();
        var result = $image.cropper("getCroppedCanvas");   //拿到剪裁后的数据
//      console.log(URL.createObjectURL(dataURLtoBlob(result.toDataURL())));
//      console.log(URL.createObjectURL(dataURLtoBlob(result.toDataURL())).replace("blob:",""));  
        var responseUrl = URL.createObjectURL(dataURLtoBlob(result.toDataURL())).replace("blob:", "")
        // console.log(responseUrl); 
        
    });

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