/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

var he_perc_valid = false;
var he_cgpa_valid = false;


// Start: ChangeId: 2024010901
function checkAppURL() {
    var thisURL = window.location.href;
    if (thisURL.endsWith("applicant.html")) {
        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
    }
}
window.onload = checkAppURL;

var stDate = new Date(document.getElementById("app_dob").value);
    

//alert(stDate);

$('.form_date').datetimepicker({
    format: 'yyyy-mm-dd',
    language: 'fr',
    weekStart: 1, //alert(arr1[0].products);
    todayBtn: 1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    //forceParse : 0,
    startDate: stDate,
    endDate: today
});

$('.form_date_future').datetimepicker({
    format: 'yyyy-mm-dd',
    language: 'fr',
    weekStart: 1, //alert(arr1[0].products);
    todayBtn: 1,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    //forceParse : 0,
    startDate: today,
    endDate: "2050-01-01"
});


/*
 * Validations for all text boxes (on keyup/keypress/keydown) Line 500 to 1095
 */

$(document).ready(

        function () {
            $('#zone_sel').select2({
                dropdownAutoWidth : true,
                placeholder : 'Select preferred exam locations', 
                width:'100%',
                maximumSelectionLength:3
            });
            // 		$("[type='date']").keypress(function(evt) {
            // //			alert("Key pressed");
            // 			evt.preventDefault();
            // 			return false;
            // 		});
            document.getElementById("li_name").innerHTML = "Welcome "
                    + userName;
            $('#cur_openings').show();
            $('#app_status').hide();
            $('#messageDiv').hide();
            $('#changePassword').hide();
            $('#changeMobile').hide();

            loadState();
            loadBoard("NA", "NA");
            loadUni();

            $("#back2Top").click(function (event) {
                event.preventDefault();
                $("html, body").animate({
                    scrollTop: 0
                }, "slow");
                return false;
            });

            $('#acceptconditions').modal({
                backdrop: 'static',
                keyboard: false
            });
            //		openTab('openings');

            $("#dobAppl").val(document.getElementById("app_dob").value);
            $("#dobAppl").prop("disabled", true);
            $("#email_inp").val(document.getElementById("email_id").value);
            $("#email_inp").prop("disabled", true);
            changeDateRanges();

            $("#Educational_Qualification-tab").click(function () {
                $("#Personal_Details").removeClass("active");
                $("#Educational_Qualification").addClass("active");
                $("#Experience").removeClass("active");
                $("#myTab .t2 a").css("background-color", "#777");
                $("#myTab .t1 a").css("background-color", "#2262c3");
                $("#myTab .t3 a").css("background-color", "#2262c3");
            });

            $("#Personal_Details-tab").click(function () {
                $("#Educational_Qualification").removeClass("active");
                $("#Personal_Details").addClass("active");
                $("#Experience").removeClass("active");
                $("#myTab .t1 a").css("background-color", "#777");
                $("#myTab .t2 a").css("background-color", "#2262c3");
                $("#myTab .t3 a").css("background-color", "#2262c3");
            });

            $("#Experience-tab").click(function () {
                $("#Personal_Details-tab").removeClass("active");
                $("#Educational_Qualification").removeClass("active");
                $("#Experience").addClass("active");
                $("#myTab .t3 a").css("background-color", "#777");
                $("#myTab .t1 a").css("background-color", "#2262c3");
                $("#myTab .t2 a").css("background-color", "#2262c3");
            });

            $("#next_personal").click(function () {
                $("#Personal_Details").removeClass("active");
                $("#Educational_Qualification").addClass("active");
                $("#Experience").removeClass("active");
                $("#myTab .t2 a").css("background-color", "#777");
                $("#myTab .t1 a").css("background-color", "#2262c3");
            });

            $("#back_education").click(function () {
                $("#Educational_Qualification").removeClass("active");
                $("#Personal_Details").addClass("active")
                $("#Experience").removeClass("active");
                ;
                $("#myTab .t1 a").css("background-color", "#777");
                $("#myTab .t2 a").css("background-color", "#2262c3");
            });

            $("#next_education").click(function () {
                $("#Personal_Details").removeClass("active");
                $("#Educational_Qualification").removeClass("active");
                $("#Experience").addClass("active");
                $("#myTab .t3 a").css("background-color", "#777");
                $("#myTab .t2 a").css("background-color", "#2262c3");
            });

            $("#back_experience").click(function () {
                $("#Personal_Details").removeClass("active");
                $("#Experience-tab").removeClass("active");
                $("#Educational_Qualification").addClass("active");
                $("#myTab .t2 a").css("background-color", "#777");
                $("#myTab .t3 a").css("background-color", "#2262c3");
            });

            var $regexname = /^([a-z A-Z]{1,60})$/; //  ChangeId: 2025052003 length changed from 30 to 60
            var $regexdblspace = /([ ]{2,})/; // ChangeId: 2025042303
            $('.first_name').on('keypress keydown keyup', function () {
                $(this).val($(this).val().toUpperCase()); // ChangeId: 2023112802 auto convert to Upper case
                if (!$(this).val().match($regexname) || $(this).val().match($regexdblspace)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_First_Name').removeClass('hidden');
                    $('.e_First_Name').show();
                } else {

                    // else, do not display message
                    $('.e_First_Name').addClass('hidden');
                }
                
                
                
            });
            
            
            // 		var $regexname1 = /^([a-z A-Z]{0,30})$/;
            // 		$('.middle_name').on('keypress keydown keyup', function() {
            // 			if (!$(this).val().match($regexname1)) {
            // 				$('.e_Middle_Name').removeClass('hidden');
            // 				$('.e_Middle_Name').show();
            // 			} else {
            // 				$('.e_Middle_Name').addClass('hidden');
            // 			}
            // 		});
            // 		var $regexname2 = /^([a-z A-Z]{0,30})$/;
            // 		$('.last_name').on('keypress keydown keyup', function() {
            // 			if (!$(this).val().match($regexname2)) {
            // 				$('.e_Last_Name').removeClass('hidden');
            // 				$('.e_Last_Name').show();
            // 			} else {
            // 				$('.e_Last_Name').addClass('hidden');
            // 			}
            // 		});

            var $regexfmname = /^([a-z A-Z]{0,50})$/;
            var $regexdblspace = /([ ]{2,})/; // ChangeId: 2025042303
            $('.father_name').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexfmname)|| $(this).val().match($regexdblspace)) { // ChangeId: 2025042303
                    $('.e_Father_Name').removeClass('hidden');
                    $('.e_Father_Name').show();
                } else {
                    $('.e_Father_Name').addClass('hidden');
                }
            });
            $('.mother_name').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexfmname)|| $(this).val().match($regexdblspace)) { // ChangeId: 2025042303
                    $('.e_Mother_Name').removeClass('hidden');
                    $('.e_Mother_Name').show();
                } else {
                    $('.e_Mother_Name').addClass('hidden');
                }
            });

            var $regexstr = /^([a-z A-Z]{3,30})$/; // ChangeId: 2023121503
            $('.pob').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexstr)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;

                    $('.e_Place_of_birth').removeClass('hidden');
                    $('.e_Place_of_birth').show();
                } else {
                    // else, do not display message

                    $('.e_Place_of_birth').addClass('hidden');
                }
            });
            $('.sport_name').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexstr)) {
                    // there is a mismatch, hence show the error message;

                    $('.e_Sports_name').removeClass('hidden');
                    $('.e_Sports_name').show();
                } else {
                    // else, do not display message

                    $('.e_Sports_name').addClass('hidden');
                }
            });

            var $regexhouse = /^([a-z A-Z 0-9,.\-\/]{1,50})$/;
            $('.house_no').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexhouse)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Address_House_no').removeClass('hidden');
                    $('.e_Address_House_no').show();
                } else {
                    $('.e_Address_House_no').addClass('hidden');
                }
            });
            $('.p_house_no').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexhouse)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Permanent_house_no').removeClass('hidden');
                    $('.e_Permanent_house_no').show();
                } else {
                    $('.e_Permanent_house_no').addClass('hidden');
                }
            });

            var $regexlocality = /^([a-z A-Z0-9,]{1,50})$/;
            $('.locality').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexlocality)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Address_Locality').removeClass('hidden');
                    $('.e_Address_Locality').show();
                } else {
                    // else, do not display message

                    $('.e_Address_Locality').addClass('hidden');
                }
            });
            $('.p_locality').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexlocality)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;

                    $('.e_Permanent_locality').removeClass('hidden');
                    $('.e_Permanent_locality').show();
                } else {
                    // else, do not display message

                    $('.e_Permanent_locality').addClass('hidden');
                }
            });

            var $regextown = /^([a-z A-Z]{1,30})$/;
            $('.town').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regextown)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Address_Town').removeClass('hidden');
                    $('.e_Address_Town').show();
                } else {
                    // else, do not display message

                    $('.e_Address_Town').addClass('hidden');
                }
            });
            $('.p_town').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regextown)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;

                    $('.e_Permanent_town').removeClass('hidden');
                    $('.e_Permanent_town').show();
                } else {
                    // else, do not display message

                    $('.e_Permanent_town').addClass('hidden');
                }
            });

            var $regexpincode = /^([1-9]{1}[0-9]{5})$/;
            $('.pincode').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexpincode)) {
                    // there is a mismatch, hence show the error message;

                    $('.e_Address_Pincode').removeClass('hidden');
                    $('.e_Address_Pincode').show();
                } else {
                    // else, do not display message

                    $('.e_Address_Pincode').addClass('hidden');
                }
            });
            $('.p_pincode').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexpincode)) {
                    // there is a mismatch, hence show the error message;

                    $('.e_Permanent_pincode').removeClass('hidden');
                    $('.e_Permanent_pincode').show();
                } else {
                    // else, do not display message

                    $('.e_Permanent_pincode').addClass('hidden');
                }
            });

            var $regexrs = /^([a-z A-Z]{1,50})$/;
            $('.railway_station').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexrs)) {
                    // there is a mismatch, hence show the error message;

                    $('.e_Railway_station').removeClass('hidden');
                    $('.e_Railway_station').show();
                } else {
                    // else, do not display message

                    $('.e_Railway_station').addClass('hidden');
                }
            });

            var $regexcontact = /^([6-9]{1}[0-9]{9})$/;
            $('.contact_no').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexcontact)) {
                    // there is a mismatch, hence show the error message;
                    $('.e_Mobile_no').removeClass('hidden');
                    $('.e_Mobile_no').show();
                } else {
                    // else, do not display message
                    $('.e_Mobile_no').addClass('hidden');
                }
            });
            // PPEG-HRD: Start AADHAAR
            var $regexaadhaar = /^([0-9]{12,12})$/;
            $('.aadhaar').on('keypress keydown keyup', function () {
                $(this).val($(this).val().replaceAll(" ","")); // ChangeId: 2023121605
                if (!$(this).val().match($regexaadhaar)) {
                    // there is a mismatch, hence show the error message;
                    $('.e_Aadhaar_no').removeClass('hidden');
                    $('.e_Aadhaar_no').show();
                } else {
                    // else, do not display message
                    $('.e_Aadhaar_no').addClass('hidden');
                }
            }); 
            // PPEG-HRD: End AADHAAR
            var $regexcontact1 = /^([0-9]{0,12})$/;
            $('.alt_contact').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexcontact1)) {
                    // there is a mismatch, hence show the error message;
                    $('.e_Alternate_contact').removeClass('hidden');
                    $('.e_Alternate_contact').show();
                } else {
                    // else, do not display message
                    $('.e_Alternate_contact').addClass('hidden');
                }
            });
            
            
            //Start: ChangeId:2025041601
            var $regexname = /^([a-z A-Z]{1,60})$/; //  ChangeId: 2025052003 length changed from 30 to 60
            var $regexdblspace = /([ ]{2,})/; // ChangeId: 2025042303
            $('.bank_acc_beneficiary').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexname)|| $(this).val().match($regexdblspace)) { // ChangeId: 2025042303
                    $('.e_bank_acc_beneficiary').removeClass('hidden');
                    $('.e_bank_acc_beneficiary').show();
                } else {
                    $('.e_bank_acc_beneficiary').addClass('hidden');
                }
            });
            var $regex_bank_acc_no = /^[0-9]{9,18}$/;
            $('.bank_acc_no_confirm').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regex_bank_acc_no)) {
                    $('.e_bank_acc_no_confirm').removeClass('hidden');
                    $('.e_bank_acc_no_confirm').show();
                } else {
                    $('.e_bank_acc_no_confirm').addClass('hidden');
                }
            }); 
            var $regex_bank_ifsc_code = /^[a-zA-Z]{4}[0]{1}[a-zA-Z0-9]{6}$/;
            $('.bank_ifsc_code_confirm').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regex_bank_ifsc_code)) {
                    $('.e_bank_ifsc_code_confirm').removeClass('hidden');
                    $('.e_bank_ifsc_code_confirm').show();
                } else {
                    $('.e_bank_ifsc_code_confirm').addClass('hidden');
                }
            });
            //End: ChangeId:2025041601
            
            // Start: ChangeId: 2023111001
            var $regex_bank_acc_no = /^[0-9]{9,18}$/;
            $('.bank_acc_no').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regex_bank_acc_no)) {
                    $('.e_bank_acc_no').removeClass('hidden');
                    $('.e_bank_acc_no').show();
                } else {
                    $('.e_bank_acc_no').addClass('hidden');
                }
            }); 
            var $regex_bank_ifsc_code = /^[a-zA-Z]{4}[0]{1}[a-zA-Z0-9]{6}$/;
            $('.bank_ifsc_code').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regex_bank_ifsc_code)) {
                    $('.e_bank_ifsc_code').removeClass('hidden');
                    $('.e_bank_ifsc_code').show();
                } else {
                    $('.e_bank_ifsc_code').addClass('hidden');
                }
            });
            // End: ChangeId: 2023111001
            var $regexboard = /^([a-z A-Z]{0,50})$/;
            $('.x_board').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)) {
                    // there is a mismatch, hence show the error message

                    $('.e_x_board').removeClass('hidden');
                    $('.e_x_board').show();
                } else {
                    // else, do not display message

                    $('.e_x_board').addClass('hidden');
                }
            });

            $('.xii_board').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)) {
                    // there is a mismatch, hence show the error message

                    $('.e_xii_board').removeClass('hidden');
                    $('.e_xii_board').show();
                } else {
                    // else, do not display message

                    $('.e_xii_board').addClass('hidden');
                }
            });

            $('.ug_university').on(
                    'keypress keydown keyup',
                    function () {
                        if (!$(this).val().match($regexboard)) {
                            // there is a mismatch, hence show the error message

                            $('.e_Higher_Education_university')
                                    .removeClass('hidden');
                            $('.e_Higher_Education_university').show();
                        } else {
                            // else, do not display message

                            $('.e_Higher_Education_university').addClass(
                                    'hidden');
                        }
                    });

            $('.x_school').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_X_school').removeClass('hidden');
                    $('.e_X_school').show();
                } else {
                    // else, do not display message

                    $('.e_X_school').addClass('hidden');
                }
            });

            $('.xii_school').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message
                    $('.e_XII_school').removeClass('hidden');
                    $('.e_XII_school').show();
                } else {
                    // else, do not display message

                    $('.e_XII_school').addClass('hidden');
                }
            });
            $('.iti_college').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_ITI_college').removeClass('hidden');
                    $('.e_ITI_college').show();
                } else {
                    // else, do not display message

                    $('.e_ITI_college').addClass('hidden');
                }
            });
            $('.dip_college').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_dip_college').removeClass('hidden');
                    $('.e_dip_college').show();
                } else {
                    // else, do not display message

                    $('.e_dip_college').addClass('hidden');
                }
            });

            $('.ug_college').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)) {
                    // there is a mismatch, hence show the error message

                    $('.e_Higher_Education_college').removeClass('hidden');
                    $('.e_Higher_Education_college').show();
                } else {
                    // else, do not display message
                    $('.e_Higher_Education_college').addClass('hidden');
                }
            });

            var $regexpercentage = /^[1-9]{1}[0-9]{0,2}(\.[0-9]{1,2})?$/;
            $('.x_per').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message

                    $('.e_X_cgpa_percentage').removeClass('hidden');
                    $('.e_X_cgpa_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_X_cgpa_percentage').addClass('hidden'); // ChangeId: 2023122601
                } else {
                    //else, do not display message

                    $('.e_X_cgpa_percentage').addClass('hidden');
                }
            });
            $('.xii_per').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message

                    $('.e_XII_cgpa_percentage').removeClass('hidden');
                    $('.e_XII_cgpa_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_XII_cgpa_percentage').addClass('hidden'); // ChangeId: 2023112302
                } else {
                    //else, do not display message

                    $('.e_XII_cgpa_percentage').addClass('hidden');
                }
            });
            $('.iti_per').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message
                    $('.e_ITI_cgpa_percentage').removeClass('hidden');
                    $('.e_ITI_cgpa_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_ITI_cgpa_percentage').addClass('hidden'); // ChangeId: 2023112302
                } else {
                    //else, do not display message
                    $('.e_ITI_cgpa_percentage').addClass('hidden');

                }
            });
            $('.dip_per').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message
                    $('.e_dip_cgpa_percentage').removeClass('hidden');
                    $('.e_dip_cgpa_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_dip_cgpa_percentage').addClass('hidden'); // ChangeId: 2023112302
                } else {
                    //else, do not display message
                    $('.e_dip_cgpa_percentage').addClass('hidden');

                }
            });
            $('.ug_per').on(
                    'keypress keydown keyup',
                    function () {
                        var val = parseFloat($(this).val());
                        // Start: ChangeId: 2023122702
                        var min = 10.0;
                        // Start: ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                        if(document.getElementById("post_name_applicant").value.search("Scientist")>-1){
                            if(minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"])
                            {
                                min = parseFloat(minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"]); // ChangeId: 2023121902
                            }
                        }
                        // End: ChangeId: 2024012005
                        // End: ChangeId: 2023122702

                        if ((!$(this).val().match($regexpercentage) || val < min || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10 ,2023121902 min
                            //there is a mismatch, hence show the error message
                            if ($(this).val() !== "") {
                                $('.e_Higher_Education_cgpa_percentage')
                                        .removeClass('hidden');
                                $('.e_Higher_Education_cgpa_percentage')
                                        .show();
                            } else {
                                $('.e_Higher_Education_cgpa_percentage')
                                        .addClass('hidden');
                            }
                        } else if ($(this).val().trim() === "") {
                            $(this).val("");
                            $('.e_Higher_Education_cgpa_percentage')
                                    .addClass('hidden');
                        } else {
                            //else, do not display message

                            $('.e_Higher_Education_cgpa_percentage')
                                    .addClass('hidden');
                        }
                    });

            $('.x_per1').on('keypress keydown keyup', function () {
                // Start: ChangeId: 2023122601
                if($("#x_cgpa_obt").val().trim() === ""){
                    if($('.e_X_cgpa').hasClass('hidden') === false)
                        $('.e_X_cgpa').addClass('hidden');

                    if($("#x_percentage").val().trim() === "" && $('.e_X_cgpa_percentage').hasClass('hidden') === false)
                        $('.e_X_cgpa_percentage').addClass('hidden');
                }
                // End: ChangeId: 2023122601
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message
                    $('.e_X_percent').removeClass('hidden');

                    $('.e_X_percent').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_X_percent').addClass('hidden'); // ChangeId: 2023122601
                } else {
                    //else, do not display message
                    $('.e_X_percent').addClass('hidden');

                }
            });
            $('.xii_per1').on('keypress keydown keyup', function () {
                // Start: ChangeId: 2023122601
                if($("#xii_cgpa_obt").val().trim() === ""){
                    if($('.e_XII_cgpa').hasClass('hidden') === false)
                        $('.e_XII_cgpa').addClass('hidden');

                    if($("#xii_percentage").val().trim() === "" && $('.e_XII_cgpa_percentage').hasClass('hidden') === false)
                        $('.e_XII_cgpa_percentage').addClass('hidden');
                }
                // End: ChangeId: 2023122601
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message
                    $('.e_XII_percentage').removeClass('hidden');
                    $('.e_XII_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_XII_percentage').addClass('hidden'); // ChangeId: 2023122601
                } else {
                    //else, do not display message
                    $('.e_XII_percentage').addClass('hidden');

                }
            });
            $('.iti_per1').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message
                    $('.e_ITI_percentage').removeClass('hidden');
                    $('.e_ITI_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                } else {
                    //else, do not display message
                    $('.e_ITI_percentage').addClass('hidden');

                }
            });
            $('.dip_per1').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexpercentage) || val < 10 || val > 100) && $(this).val().trim() !== "") { // ChangeId: 2023122102 < 10
                    //there is a mismatch, hence show the error message
                    $('.e_dip_percentage').removeClass('hidden');

                    $('.e_dip_percentage').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                } else {
                    //else, do not display message
                    $('.e_dip_percentage').addClass('hidden');

                }
            });
            $('.ug_per1').on(
                    'keypress keydown keyup',
                    function () {
                        // Start: ChangeId: 2023122601
                        if($("#ug_cgpa_obt").val().trim() === ""){
                            if($('.e_Higher_Education_cgpa').hasClass('hidden') === false)
                                $('.e_Higher_Education_cgpa').addClass('hidden');

                            if($("#ug_percentage").val().trim() === "" && $('.e_Higher_Education_cgpa_percentage').hasClass('hidden') === false)
                                $('.e_Higher_Education_cgpa_percentage').addClass('hidden');
                        }
                        // End: ChangeId: 2023122601
                        var val = parseFloat($(this).val());

                        // Start: ChangeId: 2023122702
                        var min = 10.0;
                        // Start: ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                        if(document.getElementById("post_name_applicant").value.search("Scientist")>-1){
                            if(minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"])
                                min = parseFloat(minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"]); // ChangeId: 2023121902
                            // End: ChangeId: 2023122702
                        }
                        // End: ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                        if ((!$(this).val().match($regexpercentage) ||  val > 100)  && $(this).val().trim() !== "") { // ChangeId: 2023121902 min value checked, 2025050708 min check removed
                            //there is a mismatch, hence show the error message
                            $('.e_Higher_Education_percentage')
                                    .removeClass('hidden');
                            
                            $('.e_Higher_Education_percentage').show();
                        } else if ($(this).val().trim() === "") {
                            $(this).val("");
                            $('.e_Higher_Education_percentage').addClass(
                                    'hidden'); // ChangeId: 2023122601
                        } else {
                            //else, do not display message
                            $('.e_Higher_Education_percentage').addClass(
                                    'hidden');
                        }
                    });

            var $regexspecify = /^([a-z A-Z]{0,30})$/; ///^([a-z A-Z]{1,50})$/;
            $('.xii_spc').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexspecify)) {
                    // there is a mismatch, hence show the error message
                    $('.e_XII_specialization').removeClass('hidden');
                    $('.e_XII_specialization').show();
                } else {
                    // else, do not display message
                    $('.e_XII_specialization').addClass('hidden');

                }
            });

            var $regexspecify_1 = /^([a-z A-Z]{0,30})$/; ///^([a-z A-Z]{1,50})$/;
            $('.ug_spc1').on(
                    'keypress keydown keyup',
                    function () {
                        if (!$(this).val().match($regexspecify_1)) {
                            // there is a mismatch, hence show the error message
                            $('.e_Higher_Education_specialization')
                                    .removeClass('hidden');
                            $('.e_Higher_Education_specialization').show();
                        } else {
                            // else, do not display message
                            $('.e_Higher_Education_specialization')
                                    .addClass('hidden');

                        }
                    });

            var $regexcgpa = /^[0-9]{1,2}(\.[0-9]{0,2})?$/;
            $('.x_cgpa').on('keypress keydown keyup', function () {
                // Start: ChangeId: 2023122601
                if($("#x_per").val().trim() === "" ){
                    if($('.e_X_percent').hasClass('hidden') === false)
                        $('.e_X_percent').addClass('hidden');
                }
                // End: ChangeId: 2023122601
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexcgpa) || val <= 0 || val > 10) && $(this).val().trim() !== "") {
                    //there is a mismatch, hence show the error message
                    $('.e_X_cgpa').removeClass('hidden');

                    $('.e_X_cgpa').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_X_cgpa').addClass('hidden'); // ChangeId: 2023122601
                } else {
                    //else, do not display message
                    $('.e_X_cgpa').addClass('hidden');

                }
            });

            $('.xii_cgpa').on('keypress keydown keyup', function () {
                // Start: ChangeId: 2023122601
                if($("#xii_per").val().trim() === "" ){
                    if($('.e_XII_percentage').hasClass('hidden') === false)
                        $('.e_XII_percentage').addClass('hidden');
                }
                // End: ChangeId: 2023122601
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexcgpa) || val <= 0 || val > 10) && $(this).val().trim() !== "") {
                    //there is a mismatch, hence show the error message
                    $('.e_XII_cgpa').removeClass('hidden');

                    $('.e_XII_cgpa').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_XII_cgpa').addClass('hidden'); // ChangeId: 2023122601
                } else {
                    //else, do not display message
                    $('.e_XII_cgpa').addClass('hidden');

                }
            });

            $('.iti_cgpa').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexcgpa) || val <= 0 || val > 10) && $(this).val().trim() !== "") {
                    //there is a mismatch, hence show the error message
                    $('.e_ITI_cgpa').removeClass('hidden');
                    $('.e_ITI_cgpa').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                } else {
                    //else, do not display message
                    $('.e_ITI_cgpa').addClass('hidden');

                }
            });

            $('.dip_cgpa').on('keypress keydown keyup', function () {
                var val = parseFloat($(this).val());
                if ((!$(this).val().match($regexcgpa) || val <= 0 || val > 10) && $(this).val().trim() !== "") {
                    //there is a mismatch, hence show the error message
                    $('.e_dip_cgpa').removeClass('hidden');
                    $('.e_dip_cgpa').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                } else {
                    //else, do not display message
                    $('.e_dip_cgpa').addClass('hidden');

                }
            });

            $('.ug_cgpa').on('keypress keydown keyup', function () {
                // Start: ChangeId: 2023122601
                if($("#ug_per").val().trim() === "" ){
                    if($('.e_Higher_Education_percentage').hasClass('hidden') === false)
                        $('.e_Higher_Education_percentage').addClass('hidden');
                }
                // End: ChangeId: 2023122601
                var val = parseFloat($(this).val());
                var cm = parseFloat($("#ug_cgpa_max").val());
                var min = 1.0;
                // Start: ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
                if(document.getElementById("post_name_applicant").value.search("Scientist")>-1){
                    if (cm > 9.5 && minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinCGPA"]) // ChangeId: 2023121902, 2023122702
                        min = parseFloat(minMarks[$('#ug_qualification2').val().trim()]["MinCGPA"]); // ChangeId: 2023121902
                }
                // End: ChangeId: 2024012005
                if ((!$(this).val().match($regexcgpa) ||  val > 10) && $(this).val().trim() !== "") { // ChangeId: 2023121902 min checked, 2025050708 min check removed
                    //there is a mismatch, hence show the error message
                    $('.e_Higher_Education_cgpa').removeClass('hidden');

                    $('.e_Higher_Education_cgpa').show();
                } else if ($(this).val().trim() === "") {
                    $(this).val("");
                    $('.e_Higher_Education_cgpa').addClass('hidden'); // ChangeId: 2023122601
                } else {
                    //else, do not display message
                    $('.e_Higher_Education_cgpa').addClass('hidden');
                }
            });

            var $regexdisc = /^([a-z A-Z.&]{0,50})$/;
            var $regexdisc1 = /^([a-z A-Z.&]{0,50})$/;
            $('.ug_discipline').on(
                    'keypress keydown keyup',
                    function () {
                        if (!$(this).val().match($regexdisc1)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                            // there is a mismatch, hence show the error message

                            $('.e_Higher_Education_Discipline')
                                    .removeClass('hidden');
                            $('.e_Higher_Education_Discipline').show();
                        } else {

                            // else, do not display message
                            $('.e_Higher_Education_Discipline').addClass(
                                    'hidden');
                        }
                    });

            var $regthesis = /^([a-z A-Z.]{0,100})$/;
            $('.ug_qual_other').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regthesis)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_Thesis').removeClass('hidden');
                    $('.e_Thesis').show();
                } else {

                    // else, do not display message
                    $('.e_Thesis').addClass('hidden');
                }
            });

            var $regnetoth = /^([a-z A-Z]{0,10})$/;
            $('.nes_sco').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regnetoth)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_net_other').removeClass('hidden');
                    $('.e_net_other').show();
                } else {

                    // else, do not display message
                    $('.e_net_other').addClass('hidden');
                }
            });

            $('.iti_spc').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexdisc)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_ITI_specialization').removeClass('hidden');
                    $('.e_ITI_specialization').show();
                } else {

                    // else, do not display message
                    $('.e_ITI_specialization').addClass('hidden');
                }
            });
            $('.dip_spc').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexdisc)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message

                    $('.e_dip_specialization').removeClass('hidden');
                    $('.e_dip_specialization').show();
                } else {

                    // else, do not display message
                    $('.e_dip_specialization').addClass('hidden');
                }
            });

            var $regexemp = /^([a-z A-Z 0-9,.\-\/\&']{0,50})$/;
            $('.emp_name').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexemp)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Employer_Name').removeClass('hidden');
                    $('.e_Employer_Name').show();
                } else {
                    $('.e_Employer_Name').addClass('hidden');
                }
            });
            $('.emp_address').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexemp)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Employer_Address').removeClass('hidden');
                    $('.e_Employer_Address').show();
                } else {
                    $('.e_Employer_Address').addClass('hidden');
                }
            });
            var $regdesig = /^([a-z A-Z]{0,50})$/;
            $('.emp_desig').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regdesig)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Designation').removeClass('hidden');
                    $('.e_Designation').show();
                } else {
                    $('.e_Designation').addClass('hidden');
                }
            });
            $('.emp_work').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regdesig)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Nature_of_work').removeClass('hidden');
                    $('.e_Nature_of_work').show();
                } else {
                    $('.e_Nature_of_work').addClass('hidden');
                }
            });
            // Start PPEG-HRD: PayDrawn
            var $regexamount = /^([0-9]{0,9})$/; // ChangeId: 2023121802 length 12 -> 9
            $('.emp_paydrawn').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexamount)) {
                    // there is a mismatch, hence show the error message;
                    $('.e_Pay_drawn').removeClass('hidden');
                    $('.e_Pay_drawn').show();
                } else {
                    $('.e_Pay_drawn').addClass('hidden');
                }
            });
            // End PPEG-HRD: PayDrawn

            // Start PPEG-HRD: ReasonForLeaving
            $('.emp_reason').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regdesig)|| $(this).val().match(/([ ]{2,})/)) { // ChangeId: 2025042303
                    // there is a mismatch, hence show the error message;
                    $('.e_Reason_for_leaving').removeClass('hidden');
                    $('.e_Reason_for_leaving').show();
                } else {
                    $('.e_Reason_for_leaving').addClass('hidden');
                }
            });
            // End PPEG-HRD: ReasonForLeaving
            //Nurse and doc here
            var $regexregno = /^([a-z A-Z0-9 \-\/]{1,20})$/; // ChangeId: 2024012002 '/' allowed
            $('.nurse_reg_no').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexregno)) {
                    // there is a mismatch, hence show the error message;
                    $('.e_nurse_reg_no').removeClass('hidden');
                    $('.e_nurse_reg_no').show();
                } else {
                    // else, do not display message

                    $('.e_nurse_reg_no').addClass('hidden');
                }
            });
            $('.doc_reg_no').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexregno)) {
                    // there is a mismatch, hence show the error message;
                    $('.e_doc_reg_no').removeClass('hidden');
                    $('.e_doc_reg_no').show();
                } else {
                    // else, do not display message

                    $('.e_doc_reg_no').addClass('hidden');
                }
            });
            $('.nurse_council').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)) {
                    // there is a mismatch, hence show the error message

                    $('.e_nurse_council').removeClass('hidden');
                    $('.e_nurse_council').show();
                } else {
                    // else, do not display message

                    $('.e_nurse_council').addClass('hidden');
                }
            });
            // Start: ChangeId:2023090402
            $('.doc_council').on('keypress keydown keyup', function () {
                if (!$(this).val().match($regexboard)) {
                    // there is a mismatch, hence show the error message

                    $('.e_doc_council').removeClass('hidden');
                    $('.e_doc_council').show();
                } else {
                    // else, do not display message

                    $('.e_doc_council').addClass('hidden');
                }
            });
            // End: ChangeId:2023090402
            //Nurse and doc ends here

            // Start: ChangeId: 2023112001
            var last_valid_selection = null;
            $('#zone_sel').change(function(event) {
              if ($(this).val().length > 3) {
                $(this).val(last_valid_selection);
                alert("Maximum 3 preferences are allowed.");
              } else {
                last_valid_selection = $(this).val();
              }
              var zonelist = $("#zone_sel").val();
              if(zonelist.length>0){
                  //$("#zone_selected").text(zonelist); // ChangeId: 2023120601
                  $("#zone_selected").html("<span style='color:brown'>Select 3 locations in the order of preference</span>");
              }
              else {
                  $("#zone_selected").html("<span style='color:red'>No exam centre selected</span>");
              }              
            });
            // End: ChangeId: 2023112001
            // 
            // Start: ChangeId: 2023121201
            $("select").on("select2:select", function (evt) {
                var element = evt.params.data.element;
                var $element = $(element);
                $element.detach();
                $(this).append($element);
                $(this).trigger("change");
                /*
                setTimeout(function(){
                    $( ".select2-selection__choice" ).each(function( index ) {
                        var txt = $( this ).text();
                        txt = txt.substring(1);
                        $(this).html("<span class='select2-selection__choice__remove' role='presentation'>×</span>Pref "+ index + ": " + txt);
                    });
                }, 100);*/

            });
            $("select").on("change", function () {
                setTimeout(function(){
                    $( ".select2-selection__choice" ).each(function( index ) {
                        var txt = $( this ).text();
                        txt = txt.substring(1);
                        txt = txt.replace(/Pref [0-9]: /, '');
                        $(this).html("<span class='select2-selection__choice__remove' role='presentation'>×</span>Pref "+ (index+1) + ": " + txt);
                    });
                }, 50);
            });
            // End: ChangeId: 2023121201
        });

$(window).scroll(function () {
    var height = $(window).scrollTop();
    if (height > 100) {
        $('#back2Top').fadeIn();
    } else {
        $('#back2Top').fadeOut();
    }
});

function openTab(str) {
    if (str === 'openings') {
        createDynamicTable();
        $('#cur_openings').show();
        $('#app_status').hide();
        $('#messageDiv').hide();
        $('#changePassword').hide();
        $('#changeMobile').hide();
    } else if (str === 'status') {
        applicantStatusTable();
        $('#app_status').show();
        $('#cur_openings').hide();
        $('#messageDiv').hide();
        $('#changePassword').hide();
        $('#changeMobile').hide();
    } else if (str === 'cp') {
        $('#app_status').hide();
        $('#cur_openings').hide();
        $('#messageDiv').hide();
        $('#changePassword').show();
        $('#changeMobile').hide();
    } else if (str === 'cm') {
        $('#app_status').hide();
        $('#cur_openings').hide();
        $('#messageDiv').hide();
        $('#changePassword').hide();
        $('#changeMobile').show();
        $("#otp_um").prop("disabled", true);
        $("#reOtpBtn_um").prop("disabled", true);
        $("#sendOtpBtn_um").prop("disabled", false);
    }
}
$("#category_exservice_from")
        .change(
                function () {
                    var validFrom = document
                            .getElementById("category_exservice_from").value;
                    var validTill = document
                            .getElementById("category_exservice_to").value;
                    if (validTill !== "") {
                        if ((Date.parse(validFrom) >= Date.parse(validTill))) {
                            alert("The duration of service is incorrect. Please set again!!");
                            document
                                    .getElementById("category_exservice_from").value = "";
                        }
                    }
                });

$("#category_exservice_to")
        .change(
                function () {
                    var validFrom = document
                            .getElementById("category_exservice_from").value;
                    var validTill = document
                            .getElementById("category_exservice_to").value;
                    if ((Date.parse(validFrom) >= Date.parse(validTill))) {
                        alert("The duration of service is incorrect. Please set again!!");
                        document.getElementById("category_exservice_to").value = "";
                    }
                });

// 		$("#time_to")
// 				.change(
// 						function() {
// 							var validFrom = document
// 									.getElementById("time_from").value;
// 							var validTill = document.getElementById("time_to").value;
// 							if ((Date.parse(validFrom) >= Date.parse(validTill))) {
// 								alert("The duration of service is incorrect. Please set again!!");
// 								document.getElementById("time_to").value = "";
// 							}
// 						});



// 	function submitForm() {
// 		alert("Submitting");
// 		$("#personalData").submit();
// 	}

$("#category_exservice_to")
        .change(
                function () {
                    var validFrom = document
                            .getElementById("category_exservice_from").value;
                    var validTill = document
                            .getElementById("category_exservice_to").value;
                    if ((Date.parse(validFrom) >= Date.parse(validTill))) {
                        alert("The duration of service is incorrect. Please set again!!");
                        document.getElementById("category_exservice_to").value = "";
                    }
                });

// 		$("#time_to")
// 				.change(
// 						function() {
// 							var validFrom = document
// 									.getElementById("time_from").value;
// 							var validTill = document.getElementById("time_to").value;
// 							if ((Date.parse(validFrom) >= Date.parse(validTill))) {
// 								alert("The duration of service is incorrect. Please set again!!");
// 								document.getElementById("time_to").value = "";
// 							}
// 						});

function CheckAddress(that) {
    // 		alert(that.state.value + ", " + that.district.value );
    if ((that.state.value === "") || (that.state.value === "State")) {
        alert("Please select the state and district");
        that.sameaddressconfirmation.checked = false;
        return;
    }
    if (that.district.value === "Select District") {
        alert("Please select the district");
        that.sameaddressconfirmation.checked = false;
        return;
    }

    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_Address_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        alert("Please rectify the error in Present Address ("
                + name.replace("e_", "") + ")");
        that.sameaddressconfirmation.checked = false;
        return false;
    }

    //alert("2");
    var div = document.getElementById("permanent_address");

    if (that.sameaddressconfirmation.checked === true) {
        //alert($("#state").val());
        document.getElementById("p_state").selectedIndex = document.getElementById("state").selectedIndex;
        $("#p_state").val($("#state").val());
        loadPDistrictOptions($("#district").val());  // ChangeId: 2023110901 district value passed to set in p_district instead of NA
        that.p_house_no.value = that.house_no.value;
        that.p_locality.value = that.locality.value;
        that.p_town.value = that.town.value;
        that.p_pincode.value = that.pincode.value;
        $("#permanent_address_1 *").prop('disabled', true);
        $("#permanent_address_2 *").prop('disabled', true);
    } else {
        document.getElementById("p_house_no").required = true;
        document.getElementById("p_locality").required = true;
        document.getElementById("p_state").required = true;
        document.getElementById("p_district").required = true;
        document.getElementById("p_town").required = true;
        document.getElementById("p_pincode").required = true;


        /* ChangeId: 2023110901
        that.p_state.value = "";
        that.p_district.value = "Select District";
        that.p_house_no.value = "";
        that.p_locality.value = "";
        that.p_town.value = "";
        that.p_pincode.value = "";
        */
        $("#permanent_address_1 *").prop('disabled', false);
        $("#permanent_address_2 *").prop('disabled', false);
    }

}

$('#upload_rc').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        // 								alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_rc').val("");
            document.getElementById("rc_size").innerHTML = "";
        } else {
            // 									alert("File uploaded!!");
        }
    },
    'onStart': function () {
        // 									alert("Upload started");
    },
});

$('#upload_ews').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        // 								alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_ews').val("");
            document.getElementById("ews_size").innerHTML = "";
        } else {
            // 									alert("File uploaded!!");
        }
    },
    'onStart': function () {
        // 									alert("Upload started");
    },
});

$('#upload_disability').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        // 								alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_disability').val("");
            document.getElementById("disability_size").innerHTML = "";
        } else {
            // 									alert("File uploaded!!");
        }
    },
    'onStart': function () {
        // 									alert("Upload started");
    },
});

$('#upload_serviceman').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        // 								alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_serviceman').val("");
            document.getElementById("serviceman_size").innerHTML = "";
        } else {
            // 									alert("File uploaded!!");
        }
    },
    'onStart': function () {
        // 									alert("Upload started");
    },
});

//'input[type="file"]').ajaxfileupload({
$('#upload_photograph').ajaxfileupload({
    'action': 'UploadFileServlet',
    'validate_size': true, // ChangeId: 2023112201
    'valid_size': 40, // 40KB // ChangeId: 2023112201
    'validate_dimension': true, // ChangeId: 2023112201
    'valid_width': 110, //px  // ChangeId: 2023112201
    'valid_height': 140, //px // ChangeId: 2023112201
    'params':{'unit': 'KB'}, // ChangeId: 2023112201
    'onComplete': function (response) {
        // 								alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_photograph').val("");
            document.getElementById("PreviewPhoto").style.display = "none";
            document.getElementById("photograph_size").innerHTML = "";
        } else {
            // 									alert("File uploaded!!");
        }
    },
    'onStart': function () {
        // 									alert("Upload started");
    },
    // 			params : {
    // 				extra : a1,
    // 				extra1 : p1
    // 			}
});

$('#upload_signature')
        .ajaxfileupload(
                {
                    'action': 'UploadFileServlet',
                    'valid_size': 20, // 20KB // ChangeId: 2023112201
                    'params':{'unit': 'KB'}, // ChangeId: 2023112201
                    'validate_dimension': true, // ChangeId: 2023112201
                    'valid_width': 200, //px  // ChangeId: 2023112201
                    'valid_height': 50, //px // ChangeId: 2023112201
                    'onComplete': function (response) {
                        //alert("Upload completed");
                        //$('#upload').hide();
                        //    alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                        if (response.status === false) {
                            alert(response.message);
                            $('#upload_signature').val("");
                            document.getElementById("PreviewSignature").style.display = "none";
                            document.getElementById("signature_size").innerHTML = "";
                        } else {
                            //alert("File uploaded!!");
                        }
                    },
                    'onStart': function () {
                        //alert("Upload started");
                        //$('#upload').show();
                    }
                });
//Start PPEG-HRD MedicalRegCertificate 
$('#upload_doc_reg_certificate')
.ajaxfileupload(
        {
            'action': 'UploadFileServlet',
            'onComplete': function (response) {
                //alert("Upload completed");
                //$('#upload').hide();
                //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                if (response.status === false) {
                    alert(response.message);
                    $('#upload_doc_reg_certificate').val("");
                    document
                            .getElementById("doc_reg_certificate_size").innerHTML = "";
                } else {
                    //alert("File uploaded!!");
                }
            },
            'onStart': function () {
                //alert("Upload started");
                //$('#upload').show();
            }
        });
//End PPEG-HRD MedicalRegCertificate 
//Start PPEG-HRD NurseRegCertificate 
$('#upload_nurse_reg_certificate')
.ajaxfileupload(
        {
            'action': 'UploadFileServlet',
            'onComplete': function (response) {
                //alert("Upload completed");
                //$('#upload').hide();
                //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                if (response.status === false) {
                    alert(response.message);
                    $('#upload_nurse_reg_certificate').val("");
                    document
                            .getElementById("nurse_reg_certificate_size").innerHTML = "";
                } else {
                    //alert("File uploaded!!");
                }
            },
            'onStart': function () {
                //alert("Upload started");
                //$('#upload').show();
            }
        });
//End PPEG-HRD NurseRegCertificate
$('#upload_x_degree_certificate')
        .ajaxfileupload(
                {
                    'action': 'UploadFileServlet',
                    'valid_size': 3, // 3MB // ChangeId: 2023121803
                    'params':{'unit': 'MB'}, // ChangeId: 2023121803
                    'onComplete': function (response) {
                        //alert("Upload completed");
                        //$('#upload').hide();
                        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                        if (response.status === false) {
                            alert(response.message);
                            $('#upload_x_degree_certificate').val("");
                            document
                                    .getElementById("x_degree_certificate_size").innerHTML = "";
                        } else {

                        }
                    },
                    'onStart': function () {
                        //alert("Upload started");
                        //$('#upload').show();
                    }
                });

$('#upload_x_formula').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_x_formula').val("");
            document.getElementById("x_formula_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_xii_degree_certificate')
        .ajaxfileupload(
                {
                    'action': 'UploadFileServlet',
                    'valid_size': 3, // 3MB // ChangeId: 2023121803
                    'params':{'unit': 'MB'}, // ChangeId: 2023121803
                    'onComplete': function (response) {
                        //alert("Upload completed");
                        //$('#upload').hide();
                        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                        if (response.status === false) {
                            alert(response.message);
                            $('#upload_xii_degree_certificate').val("");
                            document
                                    .getElementById("xii_degree_certificate_size").innerHTML = "";
                        } else {
                            //alert("File uploaded!!");
                        }
                    },
                    'onStart': function () {
                        //alert("Upload started");
                        //$('#upload').show();
                    }
                });

$('#xii_formula').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#xii_formula').val("");
            document.getElementById("xii_formula_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_iti_degree_certificate')
        .ajaxfileupload(
                {
                    'action': 'UploadFileServlet',
                    'valid_size': 3, // 3MB // ChangeId: 2023121803
                    'params':{'unit': 'MB'}, // ChangeId: 2023121803
                    'onComplete': function (response) {
                        //alert("Upload completed");
                        //$('#upload').hide();
                        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                        if (response.status === false) {
                            alert(response.message);
                            $('#upload_iti_degree_certificate').val("");
                            document
                                    .getElementById("iti_degree_certificate_size").innerHTML = "";
                        } else {
                            //alert("File uploaded!!");
                        }
                    },
                    'onStart': function () {
                        //alert("Upload started");
                        //$('#upload').show();
                    }
                });

$('#iti_formula').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#iti_formula').val("");
            document.getElementById("iti_formula_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_dip_degree_certificate')
        .ajaxfileupload(
                {
                    'action': 'UploadFileServlet',
                    'valid_size': 3, // 3MB // ChangeId: 2023121803
                    'params':{'unit': 'MB'}, // ChangeId: 2023121803
                    'onComplete': function (response) {
                        //alert("Upload completed");
                        //$('#upload').hide();
                        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                        if (response.status === false) {
                            alert(response.message);
                            $('#upload_dip_degree_certificate').val("");
                            document
                                    .getElementById("dip_degree_certificate_size").innerHTML = "";
                        } else {
                            //alert("File uploaded!!");
                        }
                    },
                    'onStart': function () {
                        //alert("Upload started");
                        //$('#upload').show();
                    }
                });

$('#dip_formula').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#dip_formula').val("");
            document.getElementById("dip_formula_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_ug_formula').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_ug_formula').val("");
            document.getElementById("ug_formula_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_ug_marksheet').ajaxfileupload({
    'action': 'UploadFileServlet',
    'valid_size': 16, // 16MB // ChangeId: 2025041702
    'params':{'unit': 'MB'}, // ChangeId: 2025041702
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_ug_marksheet').val("");
            document.getElementById("ug_marksheet_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_ug_degree_certificate')
        .ajaxfileupload(
                {
                    'action': 'UploadFileServlet',
                    'valid_size': 3, // 3MB // ChangeId: 2023121803
                    'params':{'unit': 'MB'}, // ChangeId: 2023121803
                    'onComplete': function (response) {
                        //alert("Upload completed");
                        //$('#upload').hide();
                        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
                        if (response.status === false) {
                            alert(response.message);
                            $('#upload_ug_degree_certificate').val("");
                            document
                                    .getElementById("ug_degree_certificate_size").innerHTML = "";
                        } else {
                            //alert("File uploaded!!");
                        }
                    },
                    'onStart': function () {
                        //alert("Upload started");
                        //$('#upload').show();
                    }
                });

$('#upload_ug_abstract').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        //alert("Upload completed");
        //$('#upload').hide();
        //	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
        if (response.status === false) {
            alert(response.message);
            $('#upload_ug_abstract').val("");
            document.getElementById("ug_abstract_size").innerHTML = "";
        } else {
            //alert("File uploaded!!");
        }
    },
    'onStart': function () {
        //alert("Upload started");
        //$('#upload').show();
    }
});

$('#upload_exp_certificate').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        if (response.status === false) {
            alert(response.message);
            $('#upload_exp_certificate').val("");
            document.getElementById("exp_certificate_size").innerHTML = "";
        } else {
        }
    },
    'onStart': function () {
    }
});

$('#nes_ct').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        if (response.status === false) {
            alert(response.message);
            $('#nes_ct').val("");
        } else {
        }
    },
    'onStart': function () {
    }
});

// 	$('.nes_ct').ajaxfileupload({
// 		'action' : 'UploadFileServlet',
// 		'onComplete' : function(response) {
// 			//alert("Upload completed");
// 			//$('#upload').hide();
// 			//	alert("res : " + response.status);// + ", Status : " + response.status + ", " + response.message);
// 			if (response.status === false) {
// 				alert(response.message);
// 				$('.nes_ct').val("");
// 			} else {
// 				//alert("File uploaded!!");
// 			}
// 		},
// 		'onStart' : function() {
// 			//alert("Upload started");
// 			//$('#upload').show();
// 		}
// 	});

$("[type='number']").keypress(function (evt) {
    evt.preventDefault();
});

// End: ChangeId: 2024010901

//Start: ChangeId:2025041601
$('#bank_acc_doc').ajaxfileupload({
    'action': 'UploadFileServlet',
    'onComplete': function (response) {
        if (response.status === false) {
            alert(response.message);
            $('#bank_acc_doc').val("");
            document.getElementById("bank_acc_doc_size").innerHTML = "";
        } else {

        }
    },
    'onStart': function () {
    }
});
//End: ChangeId:2025041601

// Start: ChangeId: 2025042305
function applyPost(ele){
    $('.e_bank_acc_beneficiary').addClass('hidden');
    $('#bank_acc_beneficiary').val("");
    idx = ele.id.split("-")[1];
    ele = document.getElementById('postrow-'+idx);
    showFields(idx, ele);
    document.getElementById('applyBtn').click();
}
function payPost(ele){
    idx = ele.id.split("-")[1];
    ele = document.getElementById('postrow-'+idx);
    showFields(idx, ele);
    document.getElementById('homeMakePaymentBtn').click();
}
function submitPost(ele){
    // Start: 2025051301
    $('.e_bank_acc_beneficiary').addClass('hidden');
    $('#bank_acc_beneficiary').val("");
    // End: 2025051301
    idx = ele.id.split("-")[1];
    ele = document.getElementById('postrow-'+idx);
    showFields(idx, ele);
    document.getElementById('homeSubmitBtn').click();
}
function continuePost(ele){
    idx = ele.id.split("-")[1];
    ele = document.getElementById('postrow-'+idx);
    showFields(idx, ele);
    document.getElementById('applyBtn').click();
}
function awaitingPost(ele){
    console.log("awaiting post called");
    idx = ele.id.split("-")[1];
    ele = document.getElementById('postrow-'+idx);
    showFields(idx, ele);
    document.getElementById('homeMakePaymentBtn').click();
}
// End: ChangeId: 2025042305

