/*
 // jQuery Ajax File Uploader
 //
 // @author: Jordan Feldstein <jfeldstein.com>
 //
 //  - Ajaxifies an individual <input type="file">
 //  - Files are sandboxed. Doesn't matter how many, or where they are, 
 on the page.
 //  - Allows for extra parameters to be included with the file
 //  - onStart callback can cancel the upload by returning false
 */


(function ($) {
    $.fn.ajaxfileupload = function (options) {
        var settings = {
            params: {
                unit:'MB' // ChangeId: 2023112201
            },
            action: '',
            onStart: function () {
                console.log('starting upload');
                console.log(this);
            },
            onComplete: function (response) {
                console.log('got response: ');
                console.log(response);
                console.log(this);
            },
            onCancel: function () {
                console.log('cancelling: ');
                console.log(this);
            },
            validate_extensions: true,
            valid_extensions_doc: ['pdf', 'jpg', 'jpeg'],
            valid_extensions_img: ['jpg', 'jpeg'],
            validate_size: true,
            valid_size: 2, // Default max size in MB,
            validate_dimension: false, // ChangeId: 2023112201
            valid_width: null, // ChangeId: 2023112201
            valid_height: null, // ChangeId: 2023112201
            submit_button: null
        };

        var uploading_file = false;

        if (options) {
            $.extend(settings, options);
        }


        // 'this' is a jQuery collection of one or more (hopefully)
        // file elements, but doesn't check for this yet
        return this.each(function () {
            var $element = $(this);

            // Skip elements that are already setup. May replace this
            // with uninit() later, to allow updating that settings
            if ($element.data('ajaxUploader-setup') === true)
                return;

            $element.change(function ()
            {
                // since a new image was selected, reset the marker
                uploading_file = false;

                // only update the file from here if we haven't assigned a
                // submit button
                if (settings.submit_button === null)
                {
                    upload_file();
                }
            });

            if (settings.submit_button === null)
            {
                // do nothing
            } else
            {
                settings.submit_button.click(function ()
                {
                    // only attempt to upload file if we're not uploading
                    if (!uploading_file)
                    {
                        upload_file();
                    }
                });
            }

            var upload_file = function ()
            {
                if ($element.val() === '')
                    return settings.onCancel.apply($element, [settings.params]);

                var fsize = $("#" + $element.attr('id'))[0].files[0].size;
                
                // Start: ChangeId: 2023112201
                if(settings.params.unit && settings.params.unit.toLowerCase() === 'mb'){
                    fsize = fsize / (1024 * 1024); // get size in MB
                }
                else {
                    fsize = fsize / 1024; // get size in KB
                }
                // End: ChangeId: 2023112201
                    
                


                var ft = document.getElementById("filetype").value;
                var valid_extensions = settings.valid_extensions_doc;
                if ((ft === "photograph") || (ft === "signature")) {
                    valid_extensions = settings.valid_extensions_img;
                }

                var sizeMB = Math.round(fsize * 10000) / 10000;
                if (ft.indexOf("exp_") == 0) {
                    document.getElementById("exp_certificate_size").innerHTML = "File Size: " + sizeMB + " MB";
                } else if (ft.indexOf("formula_") == 0) {
                    document.getElementById("ug_formula_size").innerHTML = "File Size: " + sizeMB + " MB";
                } else if (ft.indexOf("marksheet_") == 0) {
                    document.getElementById("ug_marksheet_size").innerHTML = "File Size: " + sizeMB + " MB";
                } else if (ft.indexOf("degree_certificate_") == 0) {
                    document.getElementById("ug_degree_certificate_size").innerHTML = "File Size: " + sizeMB + " MB";
                } else if (ft.indexOf("abstract_") == 0) {
                    document.getElementById("ug_abstract_size").innerHTML = "File Size: " + sizeMB + " MB";
//             } else if (ft.indexOf("nes")==0) {
//            	 //do nothing
                } else {
                    document.getElementById(ft + "_size").innerHTML = "File Size: " + sizeMB +" "+ settings.params.unit; //ChangeId: 2023112201
                }

                var fileName = $element.val().split("\\").pop();
                var $regFileName = /^([a-zA-Z0-9_\-.]{0,100})$/;

                if (!fileName.match($regFileName)) {
//                    console.log("Special characters are not allowed. Please check the file name");
//                    console.log("fileName : " + fileName + " NOT matches");
                    settings.onComplete.apply($element, [{status: false, message: 'Special characters are not allowed. Please check the file name.'}, settings.params]);
                    return false;
                } else {
//                    console.log("fileName : " + fileName + " matches");
                }


                // make sure extension is valid
                var ext = $element.val().split('.').pop().toLowerCase();
// if(true === settings.validate_extensions && $.inArray(ext,
// settings.valid_extensions) === -1) {
// // Pass back to the user
// settings.onComplete.apply($element, [{status: false, message: 'The selected
// file type is invalid. File must be ' + settings.valid_extensions.join(', ') +
// '.'}, settings.params]);
// } else if (true === settings.validate_extensions &&
// fsize>settings.valid_size) {
// //Pass back to the user
// settings.onComplete.apply($element, [{status: false, message: 'The file size
// should not be greater than ' + settings.valid_size + ' MB.'},
// settings.params]);
                if (true === settings.validate_extensions && $.inArray(ext, valid_extensions) === -1) {
                    // Pass back to the user
                    settings.onComplete.apply($element, [{status: false, message: 'The selected file type is invalid. File must be ' + valid_extensions.join(', ') + '.'}, settings.params]);

                } else if (true === settings.validate_extensions && fsize > settings.valid_size) {
                    // Pass back to the user
                    settings.onComplete.apply($element, [{status: false, message: 'The file size should not be greater than ' + settings.valid_size + settings.params.unit+'.'}, settings.params]);
                }
                // Start: ChangeId: 2023112201
                else if (true === settings.validate_dimension 
                           && settings.valid_width 
                           && settings.valid_height) {
                    var reader = new FileReader();
                    //Read the contents of Image File.
                    reader.readAsDataURL($("#" + $element.attr('id'))[0].files[0]);
                    reader.onload = function (e) {

                      //Initiate the JavaScript Image object.
                      var image = new Image();

                      //Set the Base64 string return from FileReader as source.
                      image.src = e.target.result;

                      //Validate the File Height and Width.
                      image.onload = function () {
                        var height = this.height;
                        var width = this.width;
                        if (height !== settings.valid_height || width !== settings.valid_width) {
                          //alert("Image dimension must be "+settings.valid_width+"x"+settings.valid_height);
                          // Pass back to the user
                            settings.onComplete.apply($element, [{status: false, message: "Image dimension must be "+settings.valid_width+"x"+settings.valid_height+" (width x height)."}, settings.params]);
                        }
                        // Start: ChangeId: 2023112804
                        else {
                            uploading_file = true;

                            // Creates the form, extra inputs and iframe used to
                            // submit / upload the file
                            wrapElement($element);

                            // Call user-supplied (or default) onStart(), setting
                            // it's this context to the file DOM element
                            var ret = settings.onStart.apply($element, [settings.params]);

                            // let onStart have the option to cancel the upload
                            if (ret !== false)
                            {
                                $element.parent('form').submit(function (e) {
                                    e.stopPropagation();
                                }).submit();
                            }
                        }
                        // End: ChangeId: 2023112804
                      };
                    };   
                }
                // End: ChangeId: 2023112201
                else {
                    uploading_file = true;

                    // Creates the form, extra inputs and iframe used to
                    // submit / upload the file
                    wrapElement($element);

                    // Call user-supplied (or default) onStart(), setting
                    // it's this context to the file DOM element
                    var ret = settings.onStart.apply($element, [settings.params]);

                    // let onStart have the option to cancel the upload
                    if (ret !== false)
                    {
                        $element.parent('form').submit(function (e) {
                            e.stopPropagation();
                        }).submit();
                    }
                }
            };

            // Mark this element as setup
            $element.data('ajaxUploader-setup', true);

            /*
             * // Internal handler that tries to parse the response // and clean
             * up after ourselves.
             */
            var handleResponse = function (loadedFrame, element) {
                var response, responseStr = loadedFrame.contentWindow.document.body.innerHTML;
                try {
                    // response = $.parseJSON($.trim(responseStr));
                    response = JSON.parse(responseStr);
                } catch (e) {
                    response = responseStr;
                }

                // Tear-down the wrapper form
                element.siblings().remove();
                element.unwrap();

                uploading_file = false;

                // Pass back to the user
                settings.onComplete.apply(element, [response, settings.params]);
            };

            /*
             * // Wraps element in a <form> tag, and inserts hidden inputs for
             * each // key:value pair in settings.params so they can be sent
             * along with // the upload. Then, creates an iframe that the whole
             * thing is // uploaded through.
             */
            var wrapElement = function (element) {
                // Create an iframe to submit through, using a semi-unique ID
                var frame_id = 'ajaxUploader-iframe-' + Math.round(new Date().getTime() / 1000)
                $('body').after('<iframe width="0" height="0" style="display:none;" name="' + frame_id + '" id="' + frame_id + '"/>');
                $('#' + frame_id).on('load', function () {
                    handleResponse(this, element);
                });

                // Wrap it in a form
                element.wrap(function () {
                    return '<form action="' + settings.action + '" method="POST" enctype="multipart/form-data" target="' + frame_id + '" />'
                })
                        .before(function () {
                            //alert("456567");
                            var html = '<input type="hidden" name="adv" value="' + document.getElementById("main_advt_no").value + '" />';
                            html += '<input type="hidden" name="post" value="' + document.getElementById("main_post_no").value + '" />';
                            html += '<input type="hidden" name="email" value="' + document.getElementById("email_id").value + '" />';
                            html += '<input type="hidden" name="dir" value="' + document.getElementById("UPLOAD_DIR").value + '" />';
                            html += '<input type="hidden" name="filetype" value="' + document.getElementById("filetype").value + '" />';
                            return html;
                        });
            }
        });
    }
})(jQuery)
