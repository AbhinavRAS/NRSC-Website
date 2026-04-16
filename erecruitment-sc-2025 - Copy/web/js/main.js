var UPLOAD_DIR = "";
var email = "";
var userName = "";
// var YEAR = 2019;
var today = getDateStr(new Date());
var YEAR = new Date(today).getFullYear();
var min_age = 18;
var max_age = 60;

(function ($) {
    "use strict";

    /*
     * ================================================================== [
     * Validate ]
     */
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit', function () {
        var check = true;

        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                check = false;
            }
        }

        return check;
    });

    $('.validate-form .input100').each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function validate(input) {
        if ($(input).attr('type') == 'email'
                || $(input).attr('name') == 'email') {
            if ($(input)
                    .val()
                    .trim()
                    .match(
                            /^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        } else {
            if ($(input).val().trim() == '') {
                return false;
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }

})(jQuery);

var otpCount_login = 0;

function resendOTP_login() {
    $("#reOtpBtn_l").prop("disabled", true);
    if (otpCount_login > 3) {
        alert("Maximum attempts to resend OTP exceeded. Please refresh the page to try again.")
        return;
    }
    $(document.body).css({
        'cursor': 'wait'
    });

    var article = new Object();
    article.action = "resendotp";
    article.email = encodeURIComponent($("#email").val());
    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($("#email")
                            .val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in Sending OTP. Please check again or contact website administrator");
                    $("#reOtpBtn_l").prop("disabled", false);
                    $(document.body).css({
                        'cursor': 'default'
                    });
                },
                success: function (data) {
                    $(document.body).css({
                        'cursor': 'default'
                    });

                    $("#reOtpBtn_l").prop("disabled", false);
                    var actionStr = data.Results[0];
                    if (actionStr === "error") {
                        var messageStr = data.Results[1];
                        alert(messageStr);
                        $("#reOtpBtn_l").prop("disabled", true);
                        $("#sendOtpBtn_l").prop("disabled", false);
                    } else if (actionStr === "success") {
                        otpCount_login++;
                        alert("OTP sent again to the registered email ID and mobile number");
                    }
                }
            });
}

function sendOTP_login() {
    // 123456
    // Start: ChangeId: 2023120605
    if(!validateCaptcha()){
        alert("Please enter a valid captcha!");
        return false;
    }
    //document.getElementById("captchaTextBox").value = "";
    // End: ChangeId: 2023120605
    otpCount_forgot = 0;
    var flag = checkemail($("#email").val(), "Email");
    // alert(flag);
    if (flag) {
        if ($("#password").val() === "") {
            alert("Please enter the password");
            flag = false;
        }
    } else {
        return;
    }
    if (!flag) {
        return;
    }
    $(document.body).css({
        'cursor': 'wait'
    });

    // alert("ahead");
    $("#sendOtpBtn_l").prop("disabled", true);
    var article = new Object();
    article.action = "sendotp";
    article.email = encodeURIComponent($("#email").val());
    article.password = encodeURIComponent($("#password").val());
    // alert("Calling 1");
    var token = $("#csrfToken").val();
    // alert("Calling2");
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    // alert("Calling3");
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($("#email")
                            .val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in SENDING OTP. Please check again or contact website administrator");
                    $("#sendOtpBtn_l").prop("disabled", false);
                    $(document.body).css({
                        'cursor': 'default'
                    });

                },
                success: function (data) {
                    $(document.body).css({
                        'cursor': 'default'
                    });
                    var actionStr = data.Results[0];
                    if (actionStr === "error") {
                        alert(data.Results[1]);
                        $("#otp_l").prop("disabled", true);
                        $("#reOtpBtn_l").prop("disabled", true);
                        $("#sendOtpBtn_l").prop("disabled", false);
                    } else if (actionStr === "success") {
                        otpCount_login = 1;
                        // ChangeId: 2024021201, alert modified for OTP to mibile
                        //alert("OTP sent to the registered email ID and/or mobile number. The OTP is valid for 10 minutes.");
                        alert("OTP sent to the registered mobile number. The OTP is valid for 10 minutes.");
                        $("#sendOtpBtn_l").prop("disabled", true);
                        $("#reOtpBtn_l").prop("disabled", false);
                        $("#otp_l").prop("disabled", false);
                    }
                }
            });
}

function logout() {
    userName = "";
    $("email_id").val("");
    displayDiv('login', '');
    localStorage.removeItem("csrf-token"); // ChangeId: 2023120502
    localStorage.removeItem("vtoken"); // ChangeId: 2023120502
    createCaptcha(); // ChangeId: 2023122104
    $("#captchaTextBox").val(""); // ChangeId: 2023122104
}

var lock = false;

function login() {
    
    localStorage.removeItem("csrf-token"); // ChangeId: 2023120502
    localStorage.removeItem("vtoken"); // ChangeId: 2023120502
    if (lock) {
        alert("Too many failed login attempts. Please refresh the page to continue");
        return false;
    }
    // console.log("Entered login method");
    // validateForm();
    // alert("login method called");
    var flag = checkemail($("#email").val(), "Email");
    if (flag) {
        if ($("#password").val() === "") {
            alert("Please enter the password");
            flag = false;
        }
    } else {
        return;
    }
    if (flag) { //OTP disabled for testing - enabled
        if ($("#otp_l").prop("disabled")) {
            alert("Please click on 'Send OTP' to receive an OTP on your email id and mobile number");
            flag = false;
        } else {
            flag = checkOTP($("#otp_l").val(),
                    "valid OTP sent to your email id and mobile number");
        }
    } else {
        return;
    }

    if (!flag) {
        return;
    }
    var token = $("#csrfToken").val();
    $("#loadMeLogin").modal("show"); // ChangeId: 2024021301
    var article = new Object();
    article.email = encodeURIComponent($("#email").val());
    article.password = encodeURIComponent($("#password").val());
    article.otp = encodeURIComponent($("#otp_l").val());  
    //article.otp = "12345678"; //OTP disabled for testing
    article.action = "login";
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    //alert("Email : " + $("#email").val());
    var emailId = $("#email").val();
    $
            .ajax({
                url: "/eRecruitment_NRSC/LoginServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($("#email")
                            .val()));
                },
                mimeType: 'application/json',
                error: function (data, status, er) {
                    $("#loadMeLogin").modal("hide"); // ChangeId: 2024021301
                    $("#password").val("");
                    // ChangeId: 2024021301
                    setTimeout(function() {
                        alert("Problem in Logging in. Please check again or contact website administrator");
                    }, 1000);
                    
                },
                success: function (data) {
                    $("#loadMeLogin").modal("hide"); // ChangeId: 2024021301
                    $("#password").val("");
                    var actionStr = data.Results[0];
                    var messageStr = data.Results[1];
                    if (actionStr === "error") {
                        // ChangeId: 2024021301
                        setTimeout(function() {
                            alert(messageStr);
                        }, 1000);
                        
                        if (messageStr.indexOf("lock") > -1) {
                            lock = true;
                        }
                    } else if (actionStr === "duplicate_session") {
                        // ChangeId: 2024021301
                        setTimeout(function() {
                            alert("Duplicate session identified, please logout the previous session or wait for 30 minutes");
                        }, 1000);
                        
                    } else if (actionStr === "success") {
                        today = getDateStr(new Date());
                        YEAR = new Date(today).getFullYear();
                        // alert("YEAR : " + YEAR);
                        if (messageStr === "applicant") {
                            var message = data.Results[2];
                            UPLOAD_DIR = data.Results[3];
                            userName = message;
                            displayDiv("applicant", message);
                            email = data.Results[4];
                            document.getElementById("UPLOAD_DIR").value = UPLOAD_DIR;
                            document.getElementById("email_id").value = email;
                            document.getElementById("app_dob").value = data.Results[5];
                            // alert(data.Results[6]);
                            localStorage.setItem("csrf-token", data.Results[6]);
                            document.getElementById("app_contact").value = data.Results[7];
                            localStorage.setItem("vtoken", data.Results[8]);
                            // alert("Dob : " + data.Results[5]);
                        } else if (messageStr === "admin") {
                            var message = data.Results[2];
                            userName = message;
                            displayDiv("admin", message);
                            UPLOAD_DIR = data.Results[3];
                            email = data.Results[4];
                            document.getElementById("UPLOAD_DIR").value = UPLOAD_DIR;
                            document.getElementById("email_id").value = email;
                            // alert(data.Results[6]);
                            localStorage.setItem("csrf-token", data.Results[6]);
                            localStorage.setItem("vtoken", data.Results[8]);
                        } else {
                            alert("Unknown message : " + messageStr
                                    + ". Please contact website administrator");
                        }
                    }
                }
            });
}

function getInitToken() {
    // $(csrfTokenrf").val();
    var article = new Object();
    article.action = "getToken";
    article.vtoken = localStorage.getItem("vtoken");
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent("user"));
                },
                url: "/eRecruitment_NRSC/LoginServlet",
                async: false,
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in Logging in. Please check again or contact website administrator");
                },
                success: function (data) {
                    if (data.Results[0] === "success") {
                        localStorage.setItem("csrfToken", data.Results[1]);
                        $("#csrfToken").val(data.Results[1]);
                        $("#CSRFToken").val(data.Results[1]);
                        $("#csrf_token").val(data.Results[1]);
                        // alert("got token");
                    }
                }
            });
}

function alphanumeric(inputtxt, name) {
    var letters = /^([a-zA-Z0-9]{8})$/;
    if (inputtxt.match(letters)) {
        return true;
    }
    alert('Please enter valid ' + name + ' sent to your email id');
    return false;
}

function alphabets(inputtxt, name) {
    if (inputtxt === "") {
        alert("Please enter the " + name);
        return false;
    }
    var letters = /^([a-z A-Z]{1,30})$/;
    if (inputtxt.match(letters)) {
        return true;
    }
    alert('Please enter only letters for ' + name);
    return false;
}

function checkdate(inputtxt, name) {
    if (inputtxt === "") {
        // alert("Please enter a valid value for " + name);
        alert(name + " is either empty or incorrect. Please check");
        return false;
    }
    var nowTime = new Date().getTime();
    var temp = new Date(inputtxt).getTime();
    if (nowTime < temp) {
        alert(name + " cannot be greater than current date.");
        return false;
    }
    return true;
}

function checkemail(inputtxt, name) {
    if (inputtxt === "") {
        alert("Please enter the " + name);
        return false;
    }
    var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
    if (inputtxt.match(mailformat)) {
        return true;
    }
    alert('Please enter correct email id');
    return false;
    // if (/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/.test(inputtxt)) {
}

function checkpass(inputtxt, name) {
    if (inputtxt === "") {
        alert("Please enter the " + name);
        return false;
    }
    // Regular Expressions.
    var regex = new Array();
    regex.push("[A-Z]"); // Uppercase Alphabet.
    regex.push("[a-z]"); // Lowercase Alphabet.
    regex.push("[0-9]"); // Digit.
    regex.push("[$@!%*#&]"); // Special Character.

    var passed = 0;
    // Validate for each Regular Expression.
    for (var i = 0; i < regex.length; i++) {
        if (new RegExp(regex[i]).test(inputtxt)) {
            passed++;
        }
    }

    // Validate for length of Password.
    if (inputtxt.length >= 8) {
        passed++;
    }
    if (passed === 5) {
        return true;
    }
    alert('Password to include a lower case letter, an upper case letter, a number and a special character ($@$!%*#&) and the length to be greater than 8 characters');
    return false;
}

function checkrepeatpass(inputtxt, name, prevpass) {
    if (inputtxt === "") {
        alert("Please enter the " + name);
        return false;
    }
    if (inputtxt === prevpass) {
        return true;
    }
    alert('Re-typed password does not match');
    return false;
}

function checkmobile(inputtxt, name) {
    if (inputtxt === "") {
        alert("Please enter the " + name);
        return false;
    }
    var mailformat = /^([6-9]{1}[0-9]{9})$/;
    if (inputtxt.match(mailformat)) {
        return true;
    }
    alert('Please enter correct mobile number');
    return false;
}

function checkOTP(inputtxt, name) {
    if (inputtxt === "") {
        alert("Please enter the " + name);
        return false;
    }
    var mailformat = /^([0-9]{8})$/;
    if (inputtxt.match(mailformat)) {
        return true;
    }
    alert('Please enter the valid OTP sent to your email id and mobile number');
    return false;
}

function signup() {
    var flag = alphabets($("#name").val(), "Name");
    if (flag) {
        flag = checkdate($("#dob").val(), "Date of birth");
    } else {
        return;
    }
    if (flag) {
        flag = checkmobile($("#mobile").val(), "Mobile Number");
    } else {
        return;
    }
    if (flag) {
        flag = checkemail($("#email_signup").val(), "Email");
    } else {
        return;
    }
    if (flag) {
        flag = checkpass($("#password_field").val(), "Password");
    } else {
        return;
    }
    if (flag) {
        flag = checkrepeatpass($("#pass_repeat").val(), " Password again", $(
                "#password_field").val());
    } else {
        return;
    }
    if (flag) {
        if ($("#otp_s").prop("disabled")) {
            alert("Please click on 'Send OTP' beside mobile number to receive an OTP on your email id and mobile number");
            flag = false;
        } else {
            flag = checkOTP($("#otp_s").val(),
                    "OTP sent to your email id and mobile number");
        }
    } else {
        return;
    }
    if (!flag) {
        return;
    }
    // alert("Going ahead");
    var article = new Object();
    article.action = "signup";
    article.name = encodeURIComponent($("#name").val());
    article.dob = $("#dob").val();
    article.email = encodeURIComponent($("#email_signup").val());
    article.password = encodeURIComponent($("#password_field").val());
    article.passrepeat = encodeURIComponent($("#pass_repeat").val());
    article.mobile = encodeURIComponent($("#mobile").val());
    article.otp = encodeURIComponent($("#otp_s").val());
    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($(
                            "#email_signup").val()));
                },
                url: "/eRecruitment_NRSC/register",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    $("#password_field").val("");
                    $("#pass_repeat").val("");
                    alert("Problem in Sign up. Please check again or contact website administrator");
                },
                success: function (data) {
                    $("#password_field").val("");
                    $("#pass_repeat").val("");
                    var actionStr = data.Results[0];

                    if (actionStr === "error") {
                        var messageStr = data.Results[1];
                        alert(messageStr);
                    } else if (actionStr === "success") {
                        alert("Sign up successful. Please activate your account through the link sent to the registered email.");
                        displayDiv('login', '');
                    }
                }
            });
}
var otpCount_signup = 0;

function resendOTP_signup() {
    $("#reOtpBtn_s").prop("disabled", true);
    if (otpCount_signup > 3) {
        alert("The OTP has been sent 3 times. Please wait or refresh the page after some time.")
        return;
    }

    var article = new Object();
    article.action = "resendotp";
    article.email = encodeURIComponent($("#email_signup").val());
    article.mobile = encodeURIComponent($("#mobile").val());
    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($(
                            "#email_signup").val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in Sending OTP. Please check again or contact website administrator");
                    $("#reOtpBtn_s").prop("disabled", false);
                },
                success: function (data) {
                    $("#reOtpBtn_s").prop("disabled", false);
                    var actionStr = data.Results[0];

                    if (actionStr === "error") {
                        var messageStr = data.Results[1];
                        alert(messageStr);
                        if (messageStr.indexOf("OTP Expired") > -1) {
                            $("#reOtpBtn_s").prop("disabled", true);
                            $("#sendOtpBtn_s").prop("disabled", false);
                        }
                    } else if (actionStr === "success") {
                        otpCount_signup = otpCount_signup + 1;
                        alert("OTP sent again to the registered email ID and mobile number");
                    }
                }
            });
}

function resetOTP_signup() {
    $("#sendOtpBtn_s").prop("disabled", false);
//	$("#sendOtpBtn_s").effect( "highlight", {color:"#669966"}, 3000 );
    $("#reOtpBtn_s").prop("disabled", true);
    $("#otp_s").prop("disabled", true);
    $("#otp_s").val("");
}

function sendOTP_signup() {
    otpCount_signup = 0;
    var flag = checkemail($("#email_signup").val(), "Email");
    // alert(flag);
    if (!flag) {
        return;
    }
    flag = checkmobile($("#mobile").val(), "Mobile Number");
    // alert(flag);
    if (!flag) {
        return;
    }
    // alert("ahead");
    $("#sendOtpBtn_s").prop("disabled", true);
    var article = new Object();
    article.action = "sendotp_signup";
    article.email = encodeURIComponent($("#email_signup").val());
    article.mobile = encodeURIComponent($("#mobile").val());
    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($("#email_signup").val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in SENDING OTP. Please check again or contact website administrator");
                    $("#sendOtpBtn_s").prop("disabled", false);
                },
                success: function (data) {
                    var actionStr = data.Results[0];
                    if (actionStr === "error") {
                        alert(data.Results[1]);
                        $("#sendOtpBtn_s").prop("disabled", false);
                        $("#otp_s").prop("disabled", true);
                        // $("#password_field").prop("disabled", true);
                        // $("#pass_repeat").prop("disabled", true);
                        // $("#upPass").prop("disabled", true);
                        // $("#reOtpBtn").prop("disabled", true);
                        //
                        // $("#email_field").prop("disabled", false);

                    } else if (actionStr === "success") {
                        otpCount_signup = 1;
                        alert("OTP sent to this email ID and mobile number");
                        // $("#email_signup").prop("disabled", true);
                        $("#reOtpBtn_s").prop("disabled", false);
                        $("#otp_s").prop("disabled", false);
                        // $("#password_field").prop("disabled", false);
                        // $("#pass_repeat").prop("disabled", false);
                        // $("#upPass").prop("disabled", false);
                    }
                }
            });
}

var otpCount_forgot = 0;

function resendOTP_forgot() {
    $("#reOtpBtn").prop("disabled", true);
    if (otpCount_forgot > 3) {

        alert("The OTP has been sent 3 times. Please wait for some time or check back later.")
        return;
    }

    var article = new Object();
    article.action = "resendotp";
    article.email = encodeURIComponent($("#email_field").val());
    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($(
                            "#email_field").val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in Sending OTP. Please check again or contact website administrator");
                    $("#reOtpBtn").prop("disabled", false);
                },
                success: function (data) {
                    $("#reOtpBtn").prop("disabled", false);
                    var actionStr = data.Results[0];
                    var messageStr = data.Results[1];
                    if (actionStr === "error") {
                        alert(messageStr);
                        if (messageStr.indexOf("OTP Expired") > -1) {
                            $("#reOtpBtn").prop("disabled", true);
                            $("#sendOtpBtn").prop("disabled", false);
                        }
                    } else if (actionStr === "success") {
                        otpCount_forgot++;
                        alert("OTP sent again to the registered email ID and mobile number");
                    }
                }
            });
}

function sendOTP_forgot() {

    otpCount_forgot = 0;
    var flag = checkemail($("#email_field").val(), "Email");
    // alert(flag);
    if (!flag) {
        return;
    }
    // alert("ahead");
    $("#sendOtpBtn").prop("disabled", true);
    var article = new Object();
    article.action = "sendotp_forgot";
    article.email = encodeURIComponent($("#email_field").val());
    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($(
                            "#email_field").val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in SENDING OTP. Please check again or contact website administrator");
                    $("#sendOtpBtn").prop("disabled", false);
                },
                success: function (data) {
                    var actionStr = data.Results[0];

                    if (actionStr === "error") {
                        var messageStr = data.Results[1];
                        alert(messageStr);
                        $("#otp").prop("disabled", true);
                        $("#password_forgot").prop("disabled", true);
                        $("#pass_for_repeat").prop("disabled", true);
                        $("#upPass").prop("disabled", true);
                        $("#reOtpBtn").prop("disabled", true);
                        $("#email_field").prop("disabled", false);
                        $("#sendOtpBtn").prop("disabled", false);
                    } else if (actionStr === "success") {
                        otpCount_forgot = 1;
                        alert("OTP sent to the registered email ID");
                        $("#email_field").prop("disabled", true);
                        $("#reOtpBtn").prop("disabled", false);
                        $("#otp").prop("disabled", false);
                        $("#password_forgot").prop("disabled", false);
                        $("#pass_for_repeat").prop("disabled", false);
                        $("#upPass").prop("disabled", false);
                    }
                }
            });
}

function ping() {
    alert("123");
}

function updatePass() {
    var flag = checkemail($("#email_field").val(), "Email");
    if (flag) {
        flag = alphanumeric($("#otp").val(), "OTP");
    } else {
        return;
    }
    if (flag) {
        flag = checkpass($("#password_forgot").val(), "Password");
    } else {
        return;
    }
    if (flag) {
        flag = checkrepeatpass($("#pass_for_repeat").val(), " Password again",
                $("#password_forgot").val());
    } else {
        return;
    }
    if (!flag) {
        return;
    }

    var article = new Object();
    article.action = "updatePasswd";
    article.email = encodeURIComponent($("#email_field").val());
    article.otp = encodeURIComponent($("#otp").val());
    article.password = encodeURIComponent($("#password_forgot").val());
    article.passrepeat = encodeURIComponent($("#pass_for_repeat").val());

    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent($(
                            "#email_field").val()));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    $("#password_forgot").val("");
                    $("#pass_for_repeat").val("");
                    alert("Problem in update password. Please check again or contact website administrator");
                },
                success: function (data) {
                    $("#password_forgot").val("");
                    $("#pass_for_repeat").val("");
                    var actionStr = data.Results[0];
                    if (actionStr === "error") {
                        alert(data.Results[1]);
                    } else if (actionStr === "success") {
                        alert("Password updated succesfully. Please enter your email and password to login.");
                        displayDiv('login', '');
                        localStorage.removeItem("csrf-token"); // ChangeId: 2023120603
                        localStorage.removeItem("vtoken"); // ChangeId: 2023120603
                    }
                }
            });
}

function changePass() {
    var flag = checkpass($("#old_password").val(), "Old Password");
    if (flag) {
        flag = checkpass($("#new_password").val(), "New Password");
    } else {
        return;
    }
    if (flag) {
        flag = checkrepeatpass($("#confirm_password").val(),
                "Confirm Password", $("#new_password").val());
    } else {
        return;
    }
    if (!flag) {
        return;
    }
    
    // Start: ChangeId: 2023120604
    if($("#old_password").val().trim() === $("#new_password").val().trim() ){
        alert("Your new password cannot be same as previous password!")
        return;
    }
    // End: ChangeId: 2023120604

    // alert("Email : " + email);
    var article = new Object();
    article.action = "changePasswd";
    article.email = email;
    article.oldPassword = $("#old_password").val();
    article.password = $("#new_password").val();
    article.passrepeat = $("#confirm_password").val();

    var token = $("#csrfToken").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('Csrf-Token', token);
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/PasswordServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in change password. Please check again or contact website administrator");
                },
                success: function (data) {
                    var actionStr = data.Results[0];
                    if (actionStr === "error") {
                        alert(data.Results[1]);
                    } else if (actionStr === "success") {
                        alert("Password has been updated succesfully.");
                        $("#old_password").val("");
                        $("#new_password").val("");
                        $("#confirm_password").val("");
                        localStorage.removeItem("csrf-token"); // ChangeId: 2023120603
                        localStorage.removeItem("vtoken"); // ChangeId: 2023120603
                    }
                }
            });
}

function getDateStr(tempDate) {
    var tempMonth = (tempDate.getMonth() + 1);
    if (tempMonth < 10) {
        tempMonth = "0" + tempMonth;
    }
    var tempDay = tempDate.getDate();
    if (tempDay < 10) {
        tempDay = "0" + tempDay;
    }
    var d = tempDate.getFullYear() + '-' + tempMonth + '-' + tempDay;
    return d;
}
/* ChangeId: 2023120605 commented
function generateCaptcha() {
    var alpha = new Array('A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
            'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
            'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
            'x', 'y', 'z');
    var i;
    for (i = 0; i < 4; i++) {
        var a = alpha[Math.floor(Math.random() * alpha.length)];
        var b = alpha[Math.floor(Math.random() * alpha.length)];
        var c = alpha[Math.floor(Math.random() * alpha.length)];
        var d = alpha[Math.floor(Math.random() * alpha.length)];
    }
    var code = a + '' + b + '' + '' + c + '' + d;
    document.getElementById("mainCaptcha").value = code
}
function CheckValidCaptcha() {
    var string1 = removeSpaces(document.getElementById('mainCaptcha').value);
    var string2 = removeSpaces(document.getElementById('txtInput').value);
    if (string1 == string2) {
        document.getElementById('success').innerHTML = "Form is validated Successfully";
        // alert("Form is validated Successfully");
        return true;
    } else {
        document.getElementById('error').innerHTML = "Please enter a valid captcha.";
        // alert("Please enter a valid captcha.");
        return false;

    }
}
*/
// Start: ChangeId: 2023120605
var code;
function createCaptcha() {
  //clear the contents of captcha div first 
  document.getElementById('captcha').innerHTML = "";
  document.getElementById('captchaTextBox').value = "";
  var charsArray =
  "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
  var lengthOtp = 4;
  var captcha = [];
  for (var i = 0; i < lengthOtp; i++) {
    //below code will not allow Repetition of Characters
    var index = Math.floor(Math.random() * charsArray.length + 1); //get the next character from the array
    if (captcha.indexOf(charsArray[index]) == -1)
      captcha.push(charsArray[index]);
    else i--;
  }
  var canv = document.createElement("canvas");
  canv.id = "captcha";
  canv.width = 70;
  canv.height = 40;
  var ctx = canv.getContext("2d");
  ctx.font = "25px Georgia strikethrough";
  ctx.strokeText(captcha.join(""), 0, 30);
  //storing captcha so that can validate you can save it somewhere else according to your specific requirements
  code = captcha.join("");
  document.getElementById("captcha").appendChild(canv); // adds the canvas to the body element
}
function validateCaptcha(event) {
  if(event)
    event.preventDefault();
  if (document.getElementById("captchaTextBox").value == code) {
    return true;
  }else{
    createCaptcha();
    return false;
  }
}
// End: ChangeId: 2023120605
function removeSpaces(string) {
    return string.split(' ').join('');
}

function displayDiv(str, message) {
    if (str === "signup") {
        $("#mainDiv").show();
        $("#applicationDiv").hide();
        $("#div_login").hide();
        $("#div_signup").show();
        $("#div_forgot").hide();
    } else if (str === "forgot") {
        // loadHTMLinDiv('mainDiv', 'forgot.html');
        // $("#mainDiv").load("forgot.html");
        $("#mainDiv").show();
        $("#applicationDiv").hide();
        $("#div_login").hide();
        $("#div_signup").hide();
        $("#div_forgot").show();
        init_forgot_div();
    } else if (str === "login") {
        // loadHTMLinDiv('mainDiv', 'login.html');
        // $("#mainDiv").load("login.html");
        $("#mainDiv").show();
        $("#applicationDiv").hide();
        $("#div_login").show();
        $("#div_signup").hide();
        $("#div_forgot").hide();
        init_login_div();
    } else if (str === "applicant") {
        // loadHTMLinDiv('mainDiv', 'applicant.html');
        // $("#mainDiv").load("applicant.html");
        $("#applicationDiv").load("applicant.html");
        $("#mainDiv").hide();
        $("#applicationDiv").show();
        //alert("message : " + message);

    } else if (str === "admin") {
        // loadHTMLinDiv('mainDiv', 'monitoradmin.html');
        // $("#mainDiv").load("monitoradmin.html");
        $("#mainDiv").hide();
        $("#applicationDiv").show();
        $("#applicationDiv").load("monitoradmin.html");
    }
}

function resetLogin() {
    // alert("kfhg");
    $("#sendOtpBtn_l").prop("disabled", false);
    $("#reOtpBtn_l").prop("disabled", true);
    $("#otp_l").prop("disabled", true);
    $("#otp_l").val("");
}

function loadHTMLinDiv(divName, pageName) {
    var content_div = document.getElementById(divName);
    var xmlHttp = new XMLHttpRequest();

    xmlHttp.onreadystatechange = function () {
        if (xmlHttp.readyState === 4 && xmlHttp.status === 200) {
            content_div.innerHTML = xmlHttp.responseText;
            // alert(new Date().getTime() - start);
        }
        if (this.readyState == this.HEADERS_RECEIVED) {
            var contentType = xmlHttp.getResponseHeader("Content-Type");
            // alert("content-type :" + contentType);
        }

    };
    // start = new Date().getTime();

    xmlHttp.open("POST", pageName, true); // true for asynchronous
    xmlHttp.setRequestHeader('Accept', 'text/html, */*; q=0.01');
    xmlHttp.setRequestHeader('X-Requested-With', 'XMLHttpRequest');
    // xmlHttp.setResponseHeader('Content-type', 'text/html, */*; q=0.01');
    xmlHttp.send(null);

    // 1039
}
