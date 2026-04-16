/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* ChangeId: ChangeId: 2024010801 File Newly added*/
var minDate = today.replace(YEAR, (YEAR - max_age));
var maxDate = today.replace(YEAR, (YEAR - min_age));

$('.form_date').datetimepicker({
    format: 'yyyy-mm-dd', //'dd/mm/yyyy'
    language: 'fr',
    weekStart: 1, //alert(arr1[0].products);
    todayBtn: 0,
    autoclose: 1,
    todayHighlight: 1,
    startView: 2,
    minView: 2,
    //forceParse : 0,
    startDate: minDate,
    endDate: maxDate
});

function matchPasswords() {
    var p1 = $("#password_field").val();
    var p2 = $("#pass_repeat").val();

    if (p1 !== "" && p2 !== "" && p1 !== p2) {
        $("#passMatch").text("Passwords do not match").css("color", "red").show();
        $("#passMatch").removeClass("hidden");
    } else {
        $("#passMatch").empty().hide();
        $("#passMatch").addClass("hidden");
    }
}

//                $("#pass_repeat").focusout(function(){
//                    if ($("#password_field").val().trim() !== $("#pass_repeat").val().trim()) {
//                        $("#passMatch").removeClass("hidden");
//                    } else {
//                        $("#passMatch").addClass("hidden");                        
//                    }
//                    console.log( $("#password_field").val());
//                    console.log( $("#pass_repeat").val());
//                    console.log($("#password_field").val().trim() === $("#pass_repeat").val().trim());
//                });
//		
$(document)
        .ready(
                function () {
                    //$("#captcha_reload").click();

                    //getInitToken();

                    $("#applicationDiv").hide();
                    $("#mainDiv").show();
                    $("#div_show").show();
                    $("#div_signup").hide();
                    $("#div_forgot").hide();
                    $("#password").val("");
                    $("#otp_l").val("");

                    init_login_div();
                    init_signup_div();
                    init_forgot_div();
                    alert("Please ensure the browser version is as follows: Firefox 109+, Chrome 100+, Edge 88+, Opera 64+. If not, please update the browser else some features might not work properly.");
                });

function init_login_div() {
    $("#sendOtpBtn_l").prop("disabled", false);
    $("#reOtpBtn_l").prop("disabled", true);
    $("#otp_l").prop("disabled", true);
    $("#otp_l").val("");
}

function init_signup_div() {
    //alert("resetting");
// 			var minDate = today.replace(YEAR, (YEAR - max_age));
// 			var maxDate = today.replace(YEAR, (YEAR - min_age));
// 			document.getElementById("dob").min = minDate;
// 			document.getElementById("dob").max = maxDate;

    //div_forgot
    $("#name").val("");
    $("#dob").val("");
    $("#email_signup").val("");
    $("#password_field").val("");
    $("#pass_repeat").val("");
    $("#mobile").val("");
    $("#otp_s").val("");
    $("#sendOtpBtn_s").prop("disabled", false);
    $("#reOtpBtn_s").prop("disabled", true);
    $("#passMatch").hide();
}

function init_forgot_div() {
    //alert("resetting");

    //div_forgot
    $("#email_field").val("");
    $("#email_field").prop("disabled", false);
    $("#sendOtpBtn").prop("disabled", false);
    $("#reOtpBtn").prop("disabled", true);
    $("#otp").prop("disabled", true);
    $("#password_forgot").prop("disabled", true);
    $("#pass_for_repeat").prop("disabled", true);
    $("#upPass").prop("disabled", true);

}
$(document).on('keypress', function (e) {
    if (e.which == 13) {
        //alert($("#div_login").is(":visible"));
        if ($("#div_login").is(":visible")) {
            login();
        } else if ($("#div_signup").is(":visible")) {
            signup();
        } else if ($("#div_forgot").is(":visible")) {
            updatePass();
        }
        return false;
    }
});

var IDLE_TIMEOUT = 1200; //seconds
var _idleSecondsCounter = 0;
document.onclick = function () {
    _idleSecondsCounter = 0;
};
document.onmousemove = function () {
    _idleSecondsCounter = 0;
};
document.onmousedown = function () {
    _idleSecondsCounter = 0;
};
document.onkeypress = function () {
    _idleSecondsCounter = 0;
};

var myInterval = window.setInterval(CheckIdleTime, 1000);

function CheckIdleTime() {
    if ($("#email_id").val() !== "") {
        _idleSecondsCounter = _idleSecondsCounter + 1;
        if (_idleSecondsCounter > IDLE_TIMEOUT) {
            alert("Dear Applicant, this page has been inactive for a while. Please login again.");
            document.location.href = "index.html";
        }
    }
}

$('body').bind('copy paste', function (e) {
    e.preventDefault();
    return false;
});
