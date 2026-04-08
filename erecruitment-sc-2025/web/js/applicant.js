var advertisementDetails = new Array();
var personalDetails = new Object;
var appId = "";
var appValid = true;
var a1 = "adv";
var p1 = "post";
var eligList = [];
var MAX_APPLY_COUNT = 1; // HRD ChangeId:2023081901 Reapply
var heYopObj = null;

var otpCount_um = 0;

// Start: ChangeId: 2023121902, 2024012004
var minMarks = {
    "B.Sc":{"MinPercentage":60,"MinCGPA":6.5},
    "B.Arch":{"MinPercentage":65,"MinCGPA":6.84},
    "B.Tech/B.E":{"MinPercentage":65,"MinCGPA":6.84},
    "M.Sc":{"MinPercentage":65,"MinCGPA":6.84},
    "M.Tech/M.E":{"MinPercentage":60,"MinCGPA":6.5},
    "MSc-Tech":{"MinPercentage":65,"MinCGPA":6.84},
    "M.Tech":{"MinPercentage":60,"MinCGPA":6.5}
};
// End: ChangeId: 2023121902, 2024012004

// Start: File Download ChangeId: 2023111401
function download(filename, advt_no, post_no, fileid, callback) {
    if(!filename){
        alert("No file to download");
    }
    var article = new Object();
    article.action = "download";
    article.email = $("#email_id").val();
    // ChangeId: 2023120602
    if(article.email === "") 
        article.email = "NA";
    
    if(!advt_no)
        article.advt_no = $("#advt_no").val();
    else
        article.advt_no = advt_no;
    
    // ChangeId: 2023120602
    if(article.advt_no === "") 
        article.advt_no = "NA";
        
    
    if(!post_no)
        article.post_no = $("#post_no").val();
    else
        article.post_no = post_no;
    
    // ChangeId: 2023120602
    if(article.post_no === "")
        article.post_no = "NA";
    
    article.filename = filename;
    article.ext = filename.split(".")[1];
    var apptype = 'application';
    switch(article.ext.toLowerCase()){
        case 'jpg':
        case 'jpeg':
            apptype = 'image';
            break;
        case 'pdf':
            apptype = 'application';
            break;
    }
    
    var token = $("#anticsrf").val();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
        .ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('Csrf-Token', token);
                xhr.setRequestHeader('user', encodeURIComponent(email));
            },
            url: "/eRecruitment_NRSC/DownloadFileServlet",
            type: 'POST',
            dataType: 'binary',
            data: JSON.stringify(article),
            contentType: 'application/json',
            xhrFields: {
                responseType: 'arraybuffer'
            },
            error: function (data, status, er) {
                alert("File "+filename+" couldn't be downloaded, please contact administrator.");
                return null;
            },
            success: function (data) {
                var blob = new Blob([data], { type: apptype+'/'+article.ext });
                if(fileid){
                    if(data && data.byteLength>0){ // ChangeId: 2023112805
                        const fileinput = document.getElementById(fileid);
                    
                        var myFile = new File([blob], filename, {
                            type: apptype+'/'+article.ext,
                            lastModified: new Date(),
                        });

                        const dataTransfer = new DataTransfer();
                        dataTransfer.items.add(myFile);
                        fileinput.files = dataTransfer.files;
                        document.getElementById(fileid).name = filename;
                        if(callback){
                            callback(document.getElementById(fileid));
                        }
                    }
                    // ChangeId: 2023112805
                    else{
                        document.getElementById(fileid).name = "";
                    }
                    
                }
                else{
                    window.open(window.URL.createObjectURL(blob));
                }
            }
        });
}

// End: File Download ChangeId: 2023111401

function resendOTP_um() {
    if (otpCount_um > 3) {
        $("#reOtpBtn_um").prop("disabled", true);
        alert("The OTP has been sent 3 times. Please wait for some time or check back later.")
        return;
    }

    $("#reOtpBtn_um").prop("disabled", true);
    var article = new Object();
    article.action = "resendotp";
    article.email = encodeURIComponent($("#email_id").val());
    article.password = $("#um_password").val();
    article.um_mobile = $("#um_mobile").val();
    var token = $("#anticsrf").val();
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
                    alert("Problem in Sending OTP. Please check again or contact website administrator");
                    $("#reOtpBtn_um").prop("disabled", false);
                },
                success: function (data) {
                    $("#reOtpBtn_um").prop("disabled", false);
                    var actionStr = data.Results[0];
                    if (actionStr == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                    }

                    if (actionStr === "error") {
                        var messageStr = data.Results[1];
                        alert(messageStr);
                        if (messageStr.indexOf("OTP Expired") > -1) {
                            $("#reOtpBtn_um").prop("disabled", true);
                            $("#sendOtpBtn_um").prop("disabled", false);
                        }
                    } else if (actionStr === "success") {
                        otpCount_um++;
                        alert("OTP sent again to the registered mobile number");
                    }
                }
            });
}

function sendOTP_um() {
    otpCount_forgot = 0;
    var flag = checkpass($("#um_password").val(), "Password");
    if (flag) {
        flag = checkmobile($("#um_mobile").val(), "Mobile Number");
    } else {
        return;
    }
    if (!flag) {
        return;
    }
    $("#sendOtpBtn_um").prop("disabled", true);
    // alert("ahead");
    var article = new Object();
    article.action = "sendotp_um";
    article.email = encodeURIComponent($("#email_id").val());
    article.password = encodeURIComponent($("#um_password").val());
    article.um_mobile = $("#um_mobile").val();
    var token = $("#anticsrf").val();
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
                    alert("Problem in SENDING OTP. Please check again or contact website administrator");
                    $("#sendOtpBtn_um").prop("disabled", false);
                },
                success: function (data) {
                    var actionStr = data.Results[0];
                    if (actionStr == "invalid_session") {
                        $("#sendOtpBtn_um").prop("disabled", false);
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                    } else if (actionStr === "error") {
                        alert(data.Results[1]);
                        $("#otp_um").prop("disabled", true);
                        $("#reOtpBtn_um").prop("disabled", true);
                        $("#sendOtpBtn_um").prop("disabled", false);
                    } else if (actionStr === "success") {
                        otpCount_um = 1;
                        alert("OTP sent to the registered mobile number");
                        $("#sendOtpBtn_um").prop("disabled", true);
                        $("#reOtpBtn_um").prop("disabled", false);
                        $("#otp_um").prop("disabled", false);
                    }
                }
            });
}

// function updateMobile() {
// var flag = checkpass($("#um_password").val(), "Password");
// if (flag) {
// flag = checkmobile($("#um_mobile").val(), "Mobile Number");
// } else {
// return;
// }
// if (flag) {
// if ($("#otp_um").prop("disabled")) {
// alert("Please click on 'Send OTP' to receive an OTP on your mobile number");
// flag = false;
// } else {
// flag = checkOTP($("#otp_um").val(),
// "OTP sent to your mobile number");
// }
// } else {
// return;
// }
// if (!flag) {
// return;
// }
//
// // alert("ahead");
// var article = new Object();
// article.action = "updateMobile";
// article.email = encodeURIComponent($("#email_id").val());
// article.password = encodeURIComponent($("#um_password").val());
// article.um_mobile = $("#um_mobile").val();
// article.um_otp = $("#otp_um").val();
// var token = $("#anticsrf").val();
// article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
// $
// .ajax({
// beforeSend : function(xhr) {
// xhr.setRequestHeader('Csrf-Token', token);
// xhr.setRequestHeader('user', encodeURIComponent(email));
// },
// url : "/eRecruitment_NRSC/PasswordServlet",
// type : 'POST',
// dataType : 'json',
// data : JSON.stringify(article),
// contentType : 'application/json',
// mimeType : 'application/json',
// error : function(data, status, er) {
// alert("Problem in updating mobile number. Please check again or contact
// website administrator");
// },
// success : function(data) {
// var actionStr = data.Results[0];
// if (actionStr == "invalid_session") {
// alert("Invalid session identified. Redirecting to home page.");
// window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
// } else {
// var messageStr = data.Results[1];
// alert(messageStr);
// }
// // if (actionStr === "error") {
// // alert(data.Results[1]);
// // } else if (actionStr === "success") {
// // alert("Mobile number updated succesfully");
// // }
// }
// });
//
// }

function addHErow() {
    // Start: ChangeId: 2024011001
    var ageValidationResult = validateAge(true); 
    if (ageValidationResult !== "Ok")
    {
        alert(ageValidationResult);
        return false;
    }
    // End: ChangeId: 2024011001
    
    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        return;
    }

    // Start: ChangeId: 2025050708
    if(document.getElementById("post_name_applicant").value.search("Scientist")>-1){
        var cgpa_scale = parseFloat($("#ug_cgpa_max").val());
        var cgpa_validation_min = 0.0;
        var perc_validation_min = 0.0;
        if(minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"])
            perc_validation_min = parseFloat(minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"]);
                            
        if (cgpa_scale > 9.5 && minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinCGPA"]) // ChangeId: 2023121902, 2023122702
            cgpa_validation_min = parseFloat(minMarks[$('#ug_qualification2').val().trim()]["MinCGPA"]); 
        
        if( document.getElementById("ug_per").value !== "" 
            && parseFloat(document.getElementById("ug_per").value)< perc_validation_min
            && document.getElementById("ug_cgpa_obt").value !== "" 
            && parseFloat(document.getElementById("ug_cgpa_obt").value)< cgpa_validation_min )
        {
            alert("At least one of percentage and cgpa should satisfy minimum marks criteria...")
            return;
        }
        else if( document.getElementById("ug_per").value !== "" 
            && document.getElementById("ug_cgpa_obt").value === ""
            && parseFloat(document.getElementById("ug_per").value)< perc_validation_min )
        {
            alert("Given percentage doesn't satisfy minimum percentage criteria...")
            return;
        }
        else if( document.getElementById("ug_cgpa_obt").value !== ""
            && document.getElementById("ug_per").value === ""
            && parseFloat(document.getElementById("ug_cgpa_obt").value)< cgpa_validation_min )
        {
            alert("Given CGPA doesn't satisfy minimum CGPA criteria...")
            return;
        }
    }
    // End: ChangeId: 2025050708
    var thisYear = new Date(today).getFullYear();
    document.getElementById("ug_qual_other").style.borderColor = "#ccc";
    document.getElementById("ug_university").style.borderColor = "#ccc";
    document.getElementById("ug_university_other").style.borderColor = "#ccc";
    document.getElementById("ug_college").style.borderColor = "#ccc";
    document.getElementById("ug_qualification1").style.borderColor = "#ccc";
    document.getElementById("ug_qualification2").style.borderColor = "#ccc";
    // document.getElementById("ug_discipline").style.borderColor = "#ccc";
    // document.getElementById("ug_discipline_other").style.borderColor =
    // "#ccc";
    document.getElementById("ug_spec_other").style.borderColor = "#ccc";
    document.getElementById("ug_spec").style.borderColor = "#ccc";
    document.getElementById("ug_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("ug_division").style.borderColor = "#ccc";
    document.getElementById("ug_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("ug_per").style.borderColor = "#ccc";
    document.getElementById("ug_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("ug_cgpa_max").style.borderColor = "#ccc";
    document.getElementById("ug_cgpa_fromuni").style.borderColor = "#ccc";
    document.getElementById("ug_percentage").style.borderColor = "#ccc";
    if ((document.getElementById("ug_qualification1").value == "")
            || (document.getElementById("ug_qualification1").value == "Select")) {
        alert("Please enter qualification for higher education.");
        document.getElementById("ug_qualification1").style.borderColor = "red";
        return;
    }

    var q = document.getElementById("ug_qualification2").value;
    if ((q.indexOf("Phd") > -1) || (q.indexOf("PostDoc") > -1)) {
        if (document.getElementById("ug_qual_other").value.length == 0) {
            alert("Please specify the thesis topic");
            document.getElementById("ug_qual_other").style.borderColor = "red";
            return;
        }
    }
//		console.log("heYopObj");
//		console.log(heYopObj);
    
//                heYopObj     
//                bsc:0,
//                btech:0,
//                msc:0,
//                mtech:0,   M.Tech/M.E
//                integrated:0,  Integrated M.Tech/M.E
//                phd:0
// if (document.getElementById("ug_discipline").value.length == 0) {
// alert("Please enter discipline for higher education.");
// document.getElementById("ug_discipline").style.borderColor = "red";
// return;
// }
// if (document.getElementById("ug_discipline").value == "Others") {
// if (document.getElementById("ug_discipline_other").value.length == 0) {
// alert("Discipline (Others) for higher education cannot be left blank.");
// document.getElementById("ug_discipline_other").style.borderColor = "red";
// return;
// }
// }
    if (document.getElementById("ug_spec").value.length == 0) {
        alert("Please select specilization for higher education.");
        document.getElementById("ug_spec").style.borderColor = "red";
        return;
    }
    if (document.getElementById("ug_spec").value == "Select") {
        alert("Please select Specialization for higher education.");
        document.getElementById("ug_spec").style.borderColor = "red";
        return;
    }

    if (document.getElementById("ug_spec").value == "Others") {
        if (document.getElementById("ug_spec_other").value.length == 0) {
            alert("Specialization (Others) for higher education should not be left blank.");
            document.getElementById("ug_spec_other").style.borderColor = "red";
            return;
        }
    }

    if (document.getElementById("ug_discipline").value == "Select") {
        alert("Please select Discipline for higher education.");
        document.getElementById("ug_discipline").style.borderColor = "red";
        return;
    }

    if (document.getElementById("ug_university").value.length == 0) {
        alert("Please select university for higher education.");
        document.getElementById("ug_university").style.borderColor = "red";
        return;
    }
    if (document.getElementById("ug_university").value == "Select") {
        alert("Please select university for higher education.");
        document.getElementById("ug_university").style.borderColor = "red";
        return;
    }

    if (document.getElementById("ug_university").value === "Others") {
        if (document.getElementById("ug_university_other").value.length == 0) {
            alert("University for higher education should not be left blank.");
            document.getElementById("ug_university").style.borderColor = "red";
            return;
        }
    }

    if (document.getElementById("ug_college").value.length == 0) {
        alert("Please enter college for higher education.");
        document.getElementById("ug_college").style.borderColor = "red";
        return;
    }

    if (document.getElementById("ug_year_of_passing").value.length == 0) {
        alert("Please select year of passing for higher education.");
        document.getElementById("ug_year_of_passing").style.borderColor = "red";
        return;
    }
    try {
        var yop_he = parseInt(document.getElementById("ug_year_of_passing").value);
        var yop_ss = parseInt(document.getElementById("xii_year_of_passing").value);
        var yop_s = parseInt(document.getElementById("x_year_of_passing").value);
        if (yop_he <= yop_ss) {
            alert("Year of passing for higher education cannot be less than or same as year of passing for senior secondary(12/XII) education.");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return false;
        }
        if (yop_he <= yop_s) {
            alert("Year of passing for higher education cannot be less than or same as year of passing for secondary(10/X) education.");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return false;
        }
    } catch (err) {
    }
    if (document.getElementById("ug_year_of_passing").value > thisYear) {
        alert("Year of passing for higher education cannot be greater than current year.");
        document.getElementById("ug_year_of_passing").style.borderColor = "red";
        return false;
    }

    var table = document.getElementById("he_table_body");
    var n = table.rows.length;
    if (n > 0) {
        for (var r = 0; r < n; r++) {
            var q = table.rows[r].cells[0].innerHTML;
            var d = table.rows[r].cells[1].innerHTML;
            var y = parseInt(table.rows[r].cells[5].innerHTML);
            var q1 = document.getElementById("ug_qualification2").value;
            // var d1 = document.getElementById("ug_discipline").value;
            var y1 = parseInt(document.getElementById("ug_year_of_passing").value);
            // if ((q === q1) && (d === d1) && (y === y1)) {
            // alert("Details for " + q + " in " + d + " completed in " + y
            // + " are already added");

            if (q.startsWith(q1)) {
                alert("Details for " + q1 + " are already added");
                return;
            }

            if (y === y1) {
                alert("Year of passing for " + q1
                        + " is same as year of passing for " + q
                        + ". Please check.");
                return;
            }
            
            // Start: ChangeId: 2025042302
            if( ((q === "B.Sc" 
                    || q === "B.Tech/B.E" 
                    || q === "B.Arch") 
                 && 
                 (q1 === "M.Sc" 
                 || q1 === "M.Tech" 
                 || q1 ===  "M.Tech/M.E" 
                 || q1 === "Integrated M.Tech/M.E")) && y > y1 )
            {
                alert("Year of passing for " + q
                        + " can not be greater than " + q1
                        + ". Please check.");
                return;
            }
            
            if( ((q1 === "B.Sc" 
                    || q1 === "B.Tech/B.E" 
                    || q1 === "B.Arch") 
                 && 
                 (q === "M.Sc" 
                 || q === "M.Tech" 
                 || q ===  "M.Tech/M.E" 
                 || q === "Integrated M.Tech/M.E")) && y1 > y )
            {
                alert("Year of passing for " + q1
                        + " can not be greater than " + q
                        + ". Please check.");
                return;
            }
            // End: ChangeId: 2025042302
        }
    }
    /* ChangeId: 2025051501
    if (heYopObj === null) {
        heYopObj = new Object();
    }
    if (q === "B.Sc") {
        heYopObj['bsc'] = parseInt(document.getElementById("ug_year_of_passing").value);
        if ((heYopObj['bsc'] >= heYopObj['msc']) || (heYopObj['bsc'] >= heYopObj['mtech']) || (heYopObj['bsc'] >= heYopObj['memtech'])) {
            alert("Year of passing for graduation cannot be greater than post-graduation!");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return;
        }
    } else if (q === "B.Tech/B.E") {
        heYopObj['btech'] = parseInt(document.getElementById("ug_year_of_passing").value);
        if ((heYopObj['btech'] > heYopObj['msc']) || (heYopObj['btech'] >= heYopObj['mtech']) || (heYopObj['bsc'] >= heYopObj['memtech'])) {
            alert("Year of passing for graduation cannot be greater than post-graduation!");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return;
        }
    } else if (q === "M.Sc") {
        heYopObj['msc'] = parseInt(document.getElementById("ug_year_of_passing").value);
        if ((heYopObj['msc'] <= heYopObj['bsc']) || (heYopObj['msc'] < heYopObj['btech'])) {
            alert("Year of passing for graduation cannot be greater than post-graduation!");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return;
        }
    } else if (q === "M.Tech") {
        heYopObj['mtech'] = parseInt(document.getElementById("ug_year_of_passing").value);
        if ((heYopObj['mtech'] <= heYopObj['bsc']) || (heYopObj['mtech'] <= heYopObj['btech'])) {
            alert("Year of passing for graduation cannot be greater than post-graduation!");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return;
        }
    } else if (q === "M.Tech/M.E") {
        heYopObj['memtech'] = parseInt(document.getElementById("ug_year_of_passing").value);
        if ((heYopObj['memtech'] <= heYopObj['bsc']) || (heYopObj['memtech'] <= heYopObj['btech'])) {
            alert("Year of passing for graduation cannot be greater than post-graduation!");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return;
        }
    } else if (q === "Integrated M.Tech/M.E") {
        heYopObj['integrated'] = parseInt(document.getElementById("ug_year_of_passing").value);
//            if ((heYopObj['mtech'] < heYopObj['bsc']) || (heYopObj['mtech'] < heYopObj['btech'])) {
//            if ((heYopObj['integrated'] > heYopObj['msc']) || (heYopObj['integrated'] > heYopObj['mtech'])) {
//                alert("Year of passing for graduation cannot be greater than post-graduation!");
//                document.getElementById("ug_year_of_passing").style.borderColor = "red";
//                return;
//            }
    } else if ((q.indexOf("Phd") > -1) || (q.indexOf("PostDoc") > -1)) {
        heYopObj['phd'] = parseInt(document.getElementById("ug_year_of_passing").value);
        if ((heYopObj['phd'] <= heYopObj['bsc']) || (heYopObj['phd'] <= heYopObj['btech']) ||
                (heYopObj['phd'] <= heYopObj['msc']) || (heYopObj['phd'] <= heYopObj['mtech']) || (heYopObj['phd'] <= heYopObj['memtech'])) {
            alert("Year of passing for graduation cannot be greater than post-graduation!");
            document.getElementById("ug_year_of_passing").style.borderColor = "red";
            return;
        }
    }
    */

    var qual1 = document.getElementById("ug_qualification2").value;
    if ((qual1.indexOf("Phd") < 0) && (qual1.indexOf("PostDoc") < 0)) {
        if (document.getElementById("ug_division").value.length == 0) {
            alert("Please select division for higher education.");
            document.getElementById("ug_division").style.borderColor = "red";
            return;
        }
        if (document.getElementById("ug_division").value == "Select") {
            alert("Please select division for higher education.");
            document.getElementById("ug_division").style.borderColor = "red";
            return;
        }

        // Start: ChangeId: 2023122101
        /*
        if (document.getElementById("ug_percentage_cgpa").value == "Select") {
            alert("Please select either Percentage or CGPA for higher education");
            document.getElementById("ug_percentage_cgpa").style.borderColor = "red";
            return;
        }
        */
        if (document.getElementById("ug_per").value.trim().length === 0 
                && document.getElementById("ug_cgpa_obt").value.trim().length === 0 ) {
            alert("Enter marks for higher education!");
            document.getElementById("ug_per").style.borderColor = "red";
            document.getElementById("ug_cgpa_obt").style.borderColor = "red";
            return false;
        }
        
        /*
        if (document.getElementById("ug_percentage_cgpa").value == "Percentage") {
            if (document.getElementById("ug_per").value.trim().length == 0) {
                alert("Percentage for higher education should not be left blank.");
                document.getElementById("ug_per").style.borderColor = "red";
                return;
            }
            if ( (parseFloat(document.getElementById("ug_per").value.trim()) > 100) || ((parseFloat(document.getElementById("ug_per").value.trim()) ) < 10))
            {
                alert("Percentage for higher education cannot be less than 10% and cannot be greater than 100%. Please check.");
                document.getElementById("ug_per").style.borderColor = "red";
                return false;
            }
        } else if (document.getElementById("ug_percentage_cgpa").value === "CGPA") {
            if (document.getElementById("ug_cgpa_obt").value.trim().length === 0) {
                alert("CGPA obtained for higher education left blank, please check!");
                document.getElementById("ug_cgpa_obt").style.borderColor = "red";
                return;
            }
            if (document.getElementById("ug_cgpa_max").value === "" || document.getElementById("ug_cgpa_max").value.trim().length === 0) {
                alert("Maximum CGPA left blank for higher education, please check!");
                document.getElementById("ug_cgpa_max").style.borderColor = "red";
                return;
            }

            var co = parseFloat(document.getElementById("ug_cgpa_obt").value.trim());
            var cm = parseFloat(document.getElementById("ug_cgpa_max").value.trim());
            if (cm < co) {
                alert("CGPA obtained cannot be greater than Maximum CGPA for higher education!");
                document.getElementById("ug_cgpa_obt").style.borderColor = "red";
                document.getElementById("ug_cgpa_max").style.borderColor = "red";
                return;
            }

            if (document.getElementById("ug_cgpa_fromuni").value == "Select") {
                alert("Please select if your university follows any CGPA to Percentage Conversion Factor");
                document.getElementById("ug_cgpa_fromuni").style.borderColor = "red";
                return;
            }

            if (document.getElementById("ug_cgpa_fromuni").value == "Yes") {
                if (document.getElementById("ug_percentage").value.trim().length == 0) {
                    alert("CGPA to percentage (higher education) cannot be left blank, please check!");
                    document.getElementById("ug_cgpa_obt").style.borderColor = "red";
                    return;
                }

                if (parseFloat(document.getElementById("ug_percentage").value.trim()) > 100) {
                    alert("CGPA to percentage (higher education) cannot greater than 100, please check!");
                    document.getElementById("ug_percentage").style.borderColor = "red";
                    return false;
                }

                if ((document.getElementById("upload_ug_formula").name == "NA") || (document.getElementById("upload_ug_formula").name == "")) {
                    alert("Please upload conversion formula for higher education as provided by the aforementioned University.");
                    return;
                }
            }
        }*/
        if (document.getElementById("ug_per").value.trim().length === 0 
                && document.getElementById("ug_cgpa_obt").value.trim().length === 0 ) {
            alert("Enter marks for Higher education!");
            document.getElementById("ug_per").style.borderColor = "red";
            document.getElementById("ug_cgpa_obt").style.borderColor = "red";
            return false;
        }

        if (document.getElementById("ug_per").value.trim().length !== 0) {
            if (document.getElementById("ug_per").value === 0) {
                alert("Please enter correct value for percentage for Higher education.");
                return false;
            }
            if ( (parseFloat(document.getElementById("ug_per").value.trim()) > 100) || ((parseFloat(document.getElementById("ug_per").value.trim()) ) < 10)) 
            {
                alert("Percentage for Higher education must be within 10 to 100!");
                document.getElementById("ug_per").style.borderColor = "red";
                return false;
            }
        } 
        if (document.getElementById("ug_cgpa_obt").value.trim().length !== 0) {
            if (document.getElementById("ug_cgpa_max").value === "" 
                    || document.getElementById("ug_cgpa_max").value === "Max CGPA" 
                    || document.getElementById("ug_cgpa_max").value.trim().length === 0) {
                alert("Select Maximum CGPA for Higher education!");
                document.getElementById("ug_cgpa_max").style.borderColor = "red";
                return false;
            }
            var co = parseFloat(document.getElementById("ug_cgpa_obt").value);
            var cm = parseFloat(document.getElementById("ug_cgpa_max").value);
            if (co > cm) {
                alert("CGPA obtained cannot be greater than Maximum CGPA for Higher education!");
                document.getElementById("ug_cgpa_obt").style.borderColor = "red";
                document.getElementById("ug_cgpa_max").style.borderColor = "red";
                return;
            }
            if (document.getElementById("ug_percentage").value.trim().length === 0
                    && document.getElementById("ug_cgpa_obt").value.trim().length !== 0 
                    && cm < 9.5 ) {
                alert("Enter CGPA to Percentage for Higher education!");
                document.getElementById("ug_percentage").style.borderColor = "red";
                return false;
            }

            if (    (document.getElementById("ug_percentage").value.trim().length > 0)
                    && 
                    ( 
                        parseFloat(document.getElementById("ug_percentage").value.trim()) > 100
                        || parseFloat(document.getElementById("ug_percentage").value.trim()) < 10) 
                    ){ // PPEG-HRD PercentageLess
                alert("CGPA to percentage cannot be less than 10 and greater than 100 for Higher education, please check!");
                document.getElementById("ug_percentage").style.borderColor = "red";
                return false;
            }
            if (
                    ((document.getElementById("upload_ug_formula").name === "NA")
                    || (document.getElementById("upload_ug_formula").name === "")) 
                    && (document.getElementById("ug_percentage").value.trim()
                    && cm < 9.5) // ChangeId: 2023122101 
                )
            {
                alert("Please upload CGPA conversion formula for Higher education.");
                document.getElementById("upload_ug_formula").style.borderColor = "red";
                return false;
            }
        }
        // End: ChangeId: 2023122101

        if ((document.getElementById("upload_ug_marksheet").name == "NA")
                || (document.getElementById("upload_photograph").name == "")) {
            alert("Please upload marksheet for higher education.");
            return;
        }

    } else {
        if ((document.getElementById("upload_ug_abstract").name == "NA")
                || (document.getElementById("upload_ug_abstract").name == "")) {
            alert("Please upload abstract/thesis for PhD.");
            return;
        }
    }
    if ((document.getElementById("upload_ug_degree_certificate").name == "NA")
            || (document.getElementById("upload_ug_degree_certificate").name == "")) {
        alert("Please upload degree certificate for higher education.");
        return;
    }

    var tableBody = document.getElementById("he_table_body");
    var tr = tableBody.insertRow(-1); // TABLE ROW.

    var ug_value = $("#ug_percentage_cgpa").val();
    // alert("ug_value : " + ug_value);
    if ((ug_value == "") || (ug_value == null) || (ug_value == "null")
            || (ug_value == undefined)) {
        ug_value = "NA";
    }
    var cgpa = "-";
    var per = "-";
    var convper = "-"; // ChangeId: 2023122101 newly added
    //if (ug_value == "CGPA") {  // ChangeId: 2023122101
    if($("#ug_cgpa_obt").val().trim().length>0) // ChangeId: 2023122101
    {
        cgpa = $("#ug_cgpa_obt").val().trim() + "/" + $("#ug_cgpa_max").val();
        if ($("#ug_percentage").val() != "") {
            //per = $("#ug_percentage").val(); // ChangeId: 2023122101
            convper = $("#ug_percentage").val(); // ChangeId: 2023122101
        }
    } // else if (ug_value == "Percentage") { // ChangeId: 2023122101
    if ($("#ug_per").val().trim().length>0) {  // ChangeId: 2023122101
        per = $("#ug_per").val();
    }

    
    // Start: ChangeId: 2023111401
    /*
    var tempPath = UPLOAD_DIR + "/" + document.getElementById("advt_no").value
            + "/" + document.getElementById("post_no").value + "/";
    var btn = document.createElement('a');
    if (document.getElementById("ug_cgpa_fromuni").value == "Yes") {
        if (document.getElementById("upload_ug_formula").name != "NA") {
            btn.href = tempPath + document.getElementById("upload_ug_formula").name; 
            btn.innerHTML = document.getElementById("upload_ug_formula").name;
        }
    }
    */
    var btn = document.createElement('a');
    //if (document.getElementById("ug_cgpa_fromuni").value == "Yes") { // ChangeId: 2023122101
        if (document.getElementById("upload_ug_formula").name != "NA") {
            function downloadFileHE_CFC() {
                var idx = this.id;
                var filename = document.getElementById(idx).innerText;
                download(filename);
            }
            btn.id = "heFormula"+document.getElementById("he_table_body").rows.length;
            btn.href = "#";
            btn.onclick = downloadFileHE_CFC;
            btn.innerHTML = document.getElementById("upload_ug_formula").name;
        }
        else{
            btn.innerHTML = "-na-";
        }
    //} // ChangeId: 2023122101
    
    // End: ChangeId: 2023111401
    
    // Start: ChangeId: 2023111401
    var btnM = document.createElement('a'); 
    if (document.getElementById("upload_ug_marksheet").name === "NA") {
        btnM.innerHTML = "-na-";
    } else {

        function downloadFileHE_MS() {
            var idx = this.id;

            var filename = document.getElementById(idx).innerText;

            download(filename);
        }
        btnM.id = "heMarksheet"+document.getElementById("he_table_body").rows.length;
        btnM.href = "#";
        btnM.onclick = downloadFileHE_MS;
        btnM.innerHTML = document.getElementById("upload_ug_marksheet").name;
    }
    // End: ChangeId: 2023111401
    
    // Start: ChangeId: 2023111401
    /*
    var btnD = document.createElement('a'); 
    btnD.href = tempPath + document.getElementById("upload_ug_degree_certificate").name; 
    btnD.target = "_blank"; 
    btnD.innerHTML = document.getElementById("upload_ug_degree_certificate").name;
    */
    var btnD = document.createElement('a');
    if (document.getElementById("upload_ug_degree_certificate").name === "NA") {
        btnD.innerHTML = "-na-";
    }else{
        function downloadFileHE_DC() {
            var idx = this.id;

            var filename = document.getElementById(idx).innerText;

            download(filename);
        }
        btnD.id = "heDegreeCert"+document.getElementById("he_table_body").rows.length;
        btnD.href = "#";
        btnD.onclick = downloadFileHE_DC;
        btnD.innerHTML = document.getElementById("upload_ug_degree_certificate").name;
    }
    
    // Start: ChangeId: 2023111401
    /*
    var btnT = document.createElement('a');
    if (document.getElementById("upload_ug_abstract").name === "NA") {
        btnT.innerHTML = "-na-";
    } else {
        btnT.href = tempPath + document.getElementById("upload_ug_abstract").name; 
        btnT.target = "_blank"; 
        btnT.innerHTML = document.getElementById("upload_ug_abstract").name;
    }
    */
    var btnT = document.createElement('a');
    if (document.getElementById("upload_ug_abstract").name === "NA") {
        btnT.innerHTML = "-na-";
    }else{
        function downloadFileHE_TA() {
            var idx = this.id;

            var filename = document.getElementById(idx).innerText;

            download(filename);
        }
        btnT.id = "heThesisAbstract"+document.getElementById("he_table_body").rows.length;
        btnT.href = "#";
        btnT.onclick = downloadFileHE_TA;
        btnT.innerHTML = document.getElementById("upload_ug_abstract").name;
    }
    // End: ChangeId: 2023111401
   
   
    var btnDel = document.createElement("button"); // Create a <button> element
    btnDel.style.color = "#428bca";
    btnDel.innerHTML = "Delete";
    btnDel.id = "Del_" + $("#ug_qualification2").val();
    btnDel.name = "Del_" + $("#ug_qualification2").val();
    btnDel.onclick = function () {
        deleteHE(this, $("#ug_qualification2").val());
    };
    btnDel.className = "btn btn-secondary";
    var tabCell = tr.insertCell(-1);
    var q = document.getElementById("ug_qualification2").value;
    if ((q.indexOf("Phd") > -1) || (q.indexOf("PostDoc") > -1)) {
        q = q + "(" + document.getElementById("ug_qual_other").value + ")";
    }
    tabCell.innerHTML = q;
    var dVal = document.getElementById("ug_discipline").value;
    // if (dVal == "Others") {
    // dVal = document.getElementById("ug_discipline_other").value;
    // }
    if (dVal == "") {
        dVal = "-";
    }
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = dVal;
    var sVal = document.getElementById("ug_spec").value;
    if (sVal == "Others") {
        sVal = document.getElementById("ug_spec_other").value;
    }
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = sVal;
    tabCell = tr.insertCell(-1);
    var uniVal = document.getElementById("ug_university").value;
    if (uniVal === "Others") {
        uniVal = document.getElementById("ug_university_other").value;
    }
    tabCell.innerHTML = uniVal;
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#ug_college").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#ug_year_of_passing").val();
    tabCell = tr.insertCell(-1);
    var divVal = "-";
    if ((qual1.indexOf("Phd") < 0) && (qual1.indexOf("PostDoc") < 0)) {
        divVal = $("#ug_division").val();
    }
    tabCell.innerHTML = divVal;
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = convper; // ChangeId: 2023122101 ug_value to convper
    if (document.getElementById("upload_ug_formula").name !== "NA") { // ChangeId: 2023122101  ug_value == "CGPA"
        tabCell.appendChild(btn); 
    }
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = cgpa;
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = per;
    tabCell = tr.insertCell(-1);
    tabCell.appendChild(btnM); 
    tabCell = tr.insertCell(-1);
    tabCell.appendChild(btnD); 
    tabCell = tr.insertCell(-1);
    tabCell.appendChild(btnT);
    tabCell.className = "hidden"; // ChangeId:2023090401
    tabCell = tr.insertCell(-1);
    tabCell.appendChild(btnDel);
    tabCell = tr.insertCell(-1);
    tabCell.className = "hidden";
    tabCell.innerHTML = document.getElementById("ug_qualification1").value;
    document.getElementById("tableEligAppDiv").style.display = "block";
    alert(q
            + " details are added. Please add other higher education details (if any).");
    $('#ug_qualification1').val("Select");
    $('#ug_qualification2').empty();
    $('#ug_discipline').val("");
    $('#ug_qual_other').val("");
    $('#ug_spec').val("Select");
    $('#ug_spec_other').val("");
    document.getElementById("ifHEspec").style.display = "none";
    $('#ug_university').val("Select");
    $('#ug_college').val("");
    $('#ug_year_of_passing').val("Select");
    $('#ug_division').val("Select");
    $('#ug_percentage_cgpa').val("Select");
    $("#ug_cgpa_fromuni").val("Select");
    $('#ug_per').val("");
    $('#ug_cgpa_obt').val("Select");
    $('#ug_cgpa_max').val("");
    $('#ug_percentage').val("");
    document.getElementById("upload_ug_formula").name = "NA";
    document.getElementById("upload_ug_formula").value = "";
    document.getElementById("ug_formula_size").innerHTML = "";
    document.getElementById("upload_ug_marksheet").name = "NA";
    document.getElementById("upload_ug_marksheet").value = "";
    document.getElementById("ug_marksheet_size").innerHTML = "";
    document.getElementById("upload_ug_degree_certificate").name = "NA";
    document.getElementById("upload_ug_degree_certificate").value = "";
    document.getElementById("ug_degree_certificate_size").innerHTML = "";
    document.getElementById("upload_ug_abstract").name = "NA";
    document.getElementById("upload_ug_abstract").value = "";
    document.getElementById("ug_abstract_size").innerHTML = "";
    $("#ug_CGPAPerDiv").hide(); // ChangeId: 2023122105
    $("#ug_CGPAFormDiv").hide(); // ChangeId: 2023122105
    $("#he_min_cgpa").text(""); // ChangeId: 2023122105
    $("#he_min_cgpa_to_percentage").text(""); // ChangeId: 2023122105
    $("#he_min_percentage").text(""); // ChangeId: 2023122602
}

function deleteHE(elementRef, qual) {
    //console.log(elementRef);
    var row = elementRef.parentNode.parentNode;
    row.parentNode.removeChild(row);
    var len = document.getElementById("he_table_body").rows.length;
    if (len == 0) {
        document.getElementById("tableEligAppDiv").style.display = "none";
    }
    if (qual === "B.Sc") {
        delete heYopObj['bsc'];
    } else if (qual === "B.Tech/B.E") {
        delete heYopObj['btech'];
    } else if (qual === "M.Sc") {
        delete heYopObj['msc'];
    } else if (qual === "M.Tech") {
        delete heYopObj['mtech'];
    } else if (qual === "M.Tech/M.E") {
        delete heYopObj['memtech'];
    } else if (qual === "Integrated M.Tech/M.E") {
        delete heYopObj['integrated'];
    } else if ((qual.indexOf("Phd") > -1) || (qual.indexOf("PostDoc") > -1)) {
        delete heYopObj['phd'];
    }
}

function addEXrow() {

    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        return;
    }

    document.getElementById("emp_name").style.borderColor = "#ccc";
    document.getElementById("emp_address").style.borderColor = "#ccc";
    document.getElementById("emp_desig").style.borderColor = "#ccc";
    document.getElementById("emp_work").style.borderColor = "#ccc";
    document.getElementById("emp_reason").style.borderColor = "#ccc"; // PPEG-HRD ReasonForLeaving
    document.getElementById("time_from").style.borderColor = "#ccc";
    document.getElementById("time_to").style.borderColor = "#ccc";
    document.getElementById("govtExp").style.borderColor = "#ccc";
    if (document.getElementById("emp_name").value.length == 0) {
        alert("Employer Name should not be left blank.");
        document.getElementById("emp_name").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("emp_address").value.length == 0) {
        alert("Employer Address should not be left blank.");
        document.getElementById("emp_address").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("emp_desig").value.length == 0) {
        alert("Desgination should not be left blank.");
        document.getElementById("emp_desig").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("emp_work").value.length == 0) {
        alert("Please mention the nature of work.");
        document.getElementById("emp_work").style.borderColor = "red";
        return false;
    }
    // Start PPEG-HRD: PayDrawn
    if (document.getElementById("emp_paydrawn").value.length === 0) {
        alert("Please mention the pay drawn.");
        document.getElementById("emp_paydrawn").style.borderColor = "red";
        return false;
    }
    // End PPEG-HRD: PayDrawn
    
    // Start PPEG-HRD: ReasonForLeaving
    if (document.getElementById("emp_reason").value.length === 0) {
        alert("Please mention the reason for leaving.");
        document.getElementById("emp_reason").style.borderColor = "red";
        return false;
    }
    // End PPEG-HRD: ReasonForLeaving
    if (document.getElementById("time_from").value.length == 0) {
        alert("Time From is left blank/ invalid date.");
        document.getElementById("time_from").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("time_to").value.length == 0) {
        alert("Time To is left blank/invalid date");
        document.getElementById("time_to").style.borderColor = "red";
        return false;
    }
    var nowTime = new Date(today).getTime();
    var temp = new Date(document.getElementById("time_from").value).getTime();
    if (nowTime < temp) {
        alert("The experience dates cannot be greater than current date.");
        document.getElementById("time_from").style.borderColor = "red";
        return false;
    }
    temp = new Date(document.getElementById("time_to").value).getTime();
    if (nowTime < temp) {
        alert("The experience dates cannot be greater than current date.");
        document.getElementById("time_to").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("time_to").value.length == 0) {
        alert("The experience dates cannot be greater than current date.");
        document.getElementById("time_to").style.borderColor = "red";
        return false;
    }

    if ($('#time_from').val() > $('#time_to').val()) {
        alert("To date to be greater than from date");
        return false;
    }

    if ((document.getElementById("govtExp").value == "")
            || (document.getElementById("govtExp").value == "Select")) {
        alert("Please mention if the experience is with Government Organization or Public Sector Organization.");
        document.getElementById("emp_work").style.borderColor = "red";
        return false;
    }

    if ((document.getElementById("upload_exp_certificate").name == "NA")
            || (document.getElementById("upload_exp_certificate").name == "")) {
        alert("Please upload the experience certificate.");
        return;
    }

// alert("1");
    var table = document.getElementById("ex_table_body");
    var n = table.rows.length;
    for (var r = 0; r < n; r++) {
        var e = table.rows[r].cells[0].innerHTML;
        var f = table.rows[r].cells[6].innerHTML; // from date // ChangeId:2023082901 Bugfix 5->6
        var t = table.rows[r].cells[7].innerHTML; // till date // ChangeId:2023082901 Bugfix 6->7

        var e1 = document.getElementById("emp_name").value;
        var f1 = document.getElementById("time_from").value; // this from
        // date
        var t1 = document.getElementById("time_to").value; // this till date
        if ((e === e1) && (f === f1)) {
            alert("Details for employer " + e + " with experience from " + f
                    + " are already added");
            return;
        }

        var f_time = new Date(f).getTime();
        var t_time = new Date(t).getTime();
        var f1_time = new Date(f1).getTime();
        var t1_time = new Date(t1).getTime();
        
        // Start PPEG-HRD 2023081601
        /*if (((f1_time >= f_time) && (f1_time <= t_time))
                || ((t1_time >= f_time) && (t1_time <= t_time))) */
        if (((f1_time >= f_time) && (f1_time <= t_time))
                || ((t1_time >= f_time) && (t1_time <= t_time)) || ((f1_time <= f_time) && (t1_time >= t_time)))
        // End PPEG-HRD 2023081601
        {
            alert("Experience of this time period is already added. Please change the dates.");
            return;
        }
    }
// alert("2");
    
    // Start: ChangeId: 2023111401
    /*
    var tempPath = UPLOAD_DIR + "/" + document.getElementById("advt_no").value
            + "/" + document.getElementById("post_no").value + "/";
    var btn = document.createElement('a');
    btn.target = "_blank"; 
    btn.href = "#"; 
    if (document.getElementById("upload_exp_certificate").name != "NA") {
        btn.href = tempPath + document.getElementById("upload_exp_certificate").name; 
        btn.innerHTML = document.getElementById("upload_exp_certificate").name;
    }
    */
    var btn = document.createElement('a');
    if (document.getElementById("upload_exp_certificate").name != "NA") {
        function downloadFileExp() {
            var idx = this.id;
            var filename = document.getElementById(idx).innerText;
            download(filename);
        }
        btn.id = "expcert"+document.getElementById("ex_table_body").rows.length;
        btn.href = "#";
        btn.onclick = downloadFileExp;
        btn.innerHTML = document.getElementById("upload_exp_certificate").name;
    }else{
        btn.innerHTML = "-na-";
    }
    // End: ChangeId: 2023111401

    var btnDel = document.createElement("button"); // Create a <button> element
    btnDel.innerHTML = "Delete";
    btnDel.style.color = "#428bca";
    btnDel.id = "Del_" + n;
    btnDel.name = "Del_" + n;
    btnDel.className = "btn btn-secondary";
    btnDel.onclick = function () {
        deleteEX(this);
    };
    var tr = table.insertRow(-1); // TABLE ROW.
    var tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#emp_name").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#govtExp").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#emp_address").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#emp_desig").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#emp_work").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#emp_paydrawn").val(); // PPEG-HRD: PayDrawn
    tabCell = tr.insertCell(-1); // PPEG-HRD: PayDrawn
    tabCell.innerHTML = $("#time_from").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#time_to").val();
    tabCell = tr.insertCell(-1);
    tabCell.innerHTML = $("#emp_reason").val(); // PPEG-HRD: ReasonForLeaving
    tabCell = tr.insertCell(-1); // PPEG-HRD: ReasonForLeaving
    tabCell.appendChild(btn);  
    tabCell = tr.insertCell(-1);
    tabCell.appendChild(btnDel);
    document.getElementById("tableExpAppDiv").style.display = "block";
    document.getElementById("upload_exp_certificate").name = "NA";
    document.getElementById("upload_exp_certificate").value = "";
    document.getElementById("exp_certificate_size").innerHTML = "";
    // alert("Here");
    $("#emp_name").val("");
    $("#emp_address").val("");
    $("#emp_desig").val("");
    $("#emp_work").val("");
    $("#emp_paydrawn").val(""); // PPEG-HRD: PayDrawn
    $("#emp_reason").val(""); // PPEG-HRD: ReasonForLeaving
    $("#time_from").val("");
    $("#time_to").val("");
    document.getElementById("govtExp").selectedIndex = 0;
}

function deleteEX(elementRef) {
    var row = elementRef.parentNode.parentNode;
    row.parentNode.removeChild(row);
    var len = document.getElementById("ex_table_body").rows.length;
    if (len == 0) {
        document.getElementById("tableExpAppDiv").style.display = "none";
    }
}

// var zipPath_admin = tempPath
// + tableData[i]["advt_no"] + "/"
// + tableData[i]["post_no"] + "/"
// + tableData[i]["Reg_Id"] + ".zip";

function createDynamicTable() {
    $("#loadMe").modal("show"); // ChangeId: 2024021301
    // Start: ChangeId: 2023120101
    var queryString = document.location.search;
    const urlParams = new URLSearchParams(queryString);
    var aid = urlParams.get("aid");
    if( aid == null || aid == "" ){
        aid = "NA";
    }
    // End: ChangeId: 2023120101
    
    advertisementDetails = new Array();
    document.getElementById("cur_openings-tab").style.backgroundColor = "black";
    var article = new Object();
    article.action = "ApplicantpostDetails";
    article.user_id = email;
    article.aid = aid; // ChangeId: 2023120101
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadAdvtNos",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    $("#loadMe").modal("hide"); // ChangeId: 2024021301
                    // ChangeId: 2024021301
                    setTimeout(function() {
                        alert("Webserver not responding. Please check again or contact website administrator");
                    }, 1000);
                    
                },
                success: function (data) {
                    $("#loadMe").modal("hide"); // ChangeId: 2024021301
                    document.getElementById("cur_openings-tab").style.display = "block";
                    if (data.Results.length === 0) {
                        $('#cur_openings').hide();
                        $('#messageDiv').show();
                        document.getElementById("messageDiv").innerHTML = "No posts open right now. Please check back after some time.";
                        // alert("No posts open right now. Please check back
                        // after some time.");
                        return;
                    } else if (data.Results[0] == "invalid_session") {
                        // ChangeId: 2024021301
                        setTimeout(function() {
                            alert("Invalid session identified. Redirecting to home page.");
                            window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                            return;
                        }, 1000);
                        
                    }
                    $('#cur_openings').show();
                    $('#messageDiv').hide();
                    var tableData = data.Results;
                    // alert("1 : " + tableData.length);
                    advertisementDetails = data.Results;
                    var col = [];
                    for (var i = 0; i < tableData.length; i++) {
                        for (var key in tableData[i]) {
                            if (col.indexOf(key) === -1) {
                                col.push(key);
                            }
                        }
                    }
                    // col.push("Apply");
                    

                    var table = document.createElement("table");
                    table.setAttribute('id', 'applicant_table');
                    // Start: ChangeId: 2023122701
                    table.classList.add("table"); 
                    table.classList.add("table-stripped");
                    table.classList.add("table-condensed");
                    table.classList.add("table-hover");
                    // End: ChangeId: 2023122701
                    var tr = table.insertRow(-1); // TABLE ROW.
                    tr.classList.add("info"); // ChangeId: 2023122701
                    // for (var i = 0; i < col.length; i++) {
                    // TABLE HEADER.
                    var th = document.createElement("th");
                    th.setAttribute("style", "width: 2%;");
                    th.innerHTML = "#";
                    tr.appendChild(th);
                    th = document.createElement("th");
                    th.setAttribute("style", "width: 13%;");
                    th.innerHTML = "Advertisement No.";
                    tr.appendChild(th);
                    var th1 = document.createElement("th");
                    th1.setAttribute("style", "width: 5%;");
                    th1.innerHTML = "Post No.";
                    tr.appendChild(th1);
                    var th2 = document.createElement("th");
                    th2.setAttribute("style", "width: 15%;");
                    th2.innerHTML = "Post Name";
                    tr.appendChild(th2);
                    // Start: ChangeId: 2024010101
                    var th3 = document.createElement("th");
                    th3.setAttribute("style", "width: 10%;");
                    th3.innerHTML = "Specialization";
                    // End: ChangeId: 2024010101
                    tr.appendChild(th3);
                    var th3 = document.createElement("th");
                    th3.setAttribute("style", "width: 5%;");
                    th3.innerHTML = "No of Posts";
                    tr.appendChild(th3);
                    // var th4 = document.createElement("th");
                    // th4.setAttribute("style", "width: 20%;");
                    // th4.innerHTML = "Eligibility";
                    // tr.appendChild(th4);
                    // var th5 = document.createElement("th");
                    // th5.innerHTML = "Essential Qualification";
                    // tr.appendChild(th5);
                    var th6 = document.createElement("th");
                    th6.setAttribute("style", "width: 15%;");
                    th6.innerHTML = "Qualification";
                    tr.appendChild(th6);
                    // var th6 = document.createElement("th");
                    // th6.setAttribute("style", "width: 20%;");
                    // th6.innerHTML = "National Examinations";
                    // tr.appendChild(th6);

                    // var th5 = document.createElement("th");
                    // th5.setAttribute("style", "width: 40%;");
                    // th5.innerHTML = "Desirable Qualification";
                    // tr.appendChild(th5);
                    var th7 = document.createElement("th");
                    th7.setAttribute("style", "width: 20%;");
                    th7.innerHTML = "Registration ID / Date";
                    tr.appendChild(th7);
                    th3 = document.createElement("th");
                    th3.setAttribute("style", "width: 15%;");
                    th3.innerHTML = "Application Status"; // HRD ChangeID:2023081001 PaymentChange 
                    tr.appendChild(th3);
                    // var th7 = document.createElement("th");
                    // th7.innerHTML = "Specialization";
                    // tr.appendChild(th7);
                    // }
                    // Start: ChangeId: 2023120602
                    function downloadFileAdvt() {
                        var idx = this.id;

                        var filename = "#"+idx.split("postrow-")[1]+".pdf";

                        download(filename);
                    }
                    // End: ChangeId: 2023120602
                    
                    for (var i = 0; i < tableData.length; i++) {
                        tr = table.insertRow(-1);
                        var eArray = tableData[i]["Eligibility"];
                        var qStr = "";
                        // var eStr = "";
                        // var dStr = "";
                        for (var j = 0; j < eArray.length; j++) {
                            var m = eArray[j]["mandatory"];
                            if (m === "Yes") {
                                // eStr += eArray[j]["eligibility"] + ", ";
                                // dStr += eArray[j]["discipline"] + ", ";
                                if (eArray[j]["essential_qualification"] === "X") {
                                    qStr += (eArray[j]["essential_qualification"] + ", ");
                                } else {
                                    qStr += (eArray[j]["essential_qualification"]
                                            + "("
                                            + eArray[j]["discipline"]
                                            + ")" + ", ");
                                }
                            }
                        }

                        // if (eStr.length > 0) {
                        // eStr = eStr.substring(0, eStr.length - 2);
                        // } else {
                        // eStr = "Any";
                        // }
                        //
                        // if (dStr.length > 0) {
                        // dStr = dStr.substring(0, dStr.length - 2);
                        // } else {
                        // dStr = "Any";
                        // }

                        if (qStr.length > 0) {
                            qStr = qStr.substring(0, qStr.length - 2);
                        } else {
                            qStr = "Any";
                        }
                        // Start: ChangeId: 2023110302 Advertisement shifted from /data to Webserver path
                        //var tempPath = UPLOAD_DIR.split("applicant")[0] + "admin/";
                        //var path = tempPath + tableData[i]["advt_no"] + ".pdf";
                        //var path = "docs/"+tableData[i]["advt_no"] + ".pdf"; // ChangeId: 2023120602
                        var path = "#"+tableData[i]["advt_no"] + ".pdf"; // ChangeId: 2023120602
                        // End: ChangeId: 2023110302 Advertisement shifted from /data to Webserver path
                        // for (var j = 0; j < col.length; j++) {
                        var tabCell = tr.insertCell(-1);
                        // tabCell.innerHTML = tableData[i][col[j]];
                        tabCell.innerHTML = (i + 1);
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["advt_no"];
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["post_no"];
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["post_name"];
                        // Start: ChangeId: 2024010101
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["post_spec"];
                        // End: ChangeId: 2024010101
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["no_of_vacancies"];
                        tabCell = tr.insertCell(-1);
                        // Start: ChangeId: 2023120602
                        var btnA = document.createElement('a'); 
                        btnA.href = "#";
                        //btnA.target = "_blank"; 
                        btnA.id = "postrow-"+tableData[i]["advt_no"];
                        btnA.onclick = downloadFileAdvt; 
                        btnA.innerHTML = "View advertisement"; 
                        // End: ChangeId: 2023120602
                        tabCell.appendChild(btnA);
                        // tabCell = tr.insertCell(-1);
                        // tabCell.innerHTML = eStr;
                        // tabCell = tr.insertCell(-1);
                        // tabCell.innerHTML = dStr;
                        // tabCell = tr.insertCell(-1);
                        // tabCell.innerHTML = qStr;
                        // tabCell = tr.insertCell(-1);
                        // tabCell.innerHTML = tableData[i]["desirable_qual"];
                        // tabCell = tr.insertCell(-1);
                        // tabCell.innerHTML = tableData[i]["net"].split("#");
                        tabCell = tr.insertCell(-1);
                        var reg_id = tableData[i]["Reg_Id"];
                        if (reg_id === "-") {
                            tabCell.innerHTML = reg_id;
                        } else {
                            //alert(tableData[i]["Preview_File"]);
                            
                            // Start: ChangeId: 2023111401 Zip download link replaced with preview link 
                            var btnR = document.createElement('a');

                            function downloadFile() {
                                var idx = this.id.replace("previewlink","");
                                var filename = tableData[idx]["Preview_File"].replace(/^.*[\\/]/, '');
                                var advt_no = tableData[idx]["advt_no"];
                                var post_no = tableData[idx]["post_no"];
                                
                                download(filename,advt_no,post_no);
                            }
                            btnR.href = "#";
                            btnR.onclick = downloadFile;
                            btnR.id = "previewlink"+i;
                          
                            
                            btnR.innerHTML = tableData[i]["Reg_Id"] + " / "
                                    + tableData[i]["Applied_Date"];
                            tabCell.appendChild(btnR);
                            // End: ChangeId: 2023111401 Zip download link replaced with preview link 
                        }
                        tabCell = tr.insertCell(-1);
                        // Start: ChangeID:2023081001
                        //tabCell.innerHTML = tableData[i]["no_of_attempts"]; 
                        switch(tableData[i]["application_status"]){
                            case "POSTED": 
                                tabCell.innerHTML = "<span style='color:green;'><input type='button' class='btn btn-success-light' value='Submitted'/></span>";break;
                                
                            case "PAYMENT": 
                            case "PAYMENTFAILED": // ChangeId: 2023121402
                            case "PAYMNTFAILED": // ChangeId: 2025052001
                            case "FORWARDED": // ChangeId: 2023121402
                                tabCell.innerHTML = "<span style='color:red;'><input type='button' class='btn btn-danger' id='advPayBtn-"+i+"' value='Make Payment'/></span>";break;
                                
                            case "PAID": 
                                tabCell.innerHTML = "<span style='color:blue;'><input type='button' class='btn btn-warning' id='advSubmitBtn-"+i+"' value='Submit'/></span>";break;
                                
                            case "SAVE": 
                                tabCell.innerHTML = "<span style='color:black;'><input type='button' class='btn btn-primary' id='advContinueBtn-"+i+"' value='Continue'/></span>";break;
                                
                            case "PENDING": // ChangeId: 2023121402
                                tabCell.innerHTML = "<span style='color:orange;'><input type='button' class='btn btn-warning' id='advAwaitingBtn-"+i+"' value='Awaiting Payment Status'/></span>";break;

                            default: 
                                tabCell.innerHTML = "<span style='color:gray;'><input type='button' class='btn btn-primary' id='advApplyBtn-"+i+"' value='Apply'/></span>";break;
                        }
                        
                        // End: ChangeID:2023081001
                        tr.setAttribute('id', 'postrow-'+i); // ChangeId: 2024010901 i -> 'postrow-'+i
                        if ((tableData[i]["recount"] === undefined)
                                || (tableData[i]["recount"] < 3)) {
                            //tr.setAttribute('onclick', "showFields(" + i + ", this);"); //ChangeId: 2024010901
                        }
                    }
                    $('#tableParentDivApplicant').show();
                    var divContainer = document
                            .getElementById("tableParentDivApplicant");
                    divContainer.innerHTML = "";
                    var label = document.createElement("label");
                    label.innerHTML = "Please select the advertisement and apply below:";
                    divContainer.appendChild(label);
                    divContainer.appendChild(table);
                    // Start: ChangeId: 2024010901
                    for (var i = 0; i < tableData.length; i++) {
                        // Start: ChangeId: 2025042305
                        /*
                        document.getElementById('postrow-'+i).addEventListener("click",function(){
                            showFields(this.id.split("-")[1], this);
                        });
                        */
                        
                        if(document.getElementById('advApplyBtn-'+i)){
                            document.getElementById('advApplyBtn-'+i).addEventListener("click",function(){
                                applyPost(this);
                            });
                        }
                        if(document.getElementById('advPayBtn-'+i)){
                            document.getElementById('advPayBtn-'+i).addEventListener("click",function(){
                                payPost(this);
                            });
                        }
                        if(document.getElementById('advSubmitBtn-'+i)){
                            document.getElementById('advSubmitBtn-'+i).addEventListener("click",function(){
                                submitPost(this);
                            });
                        }
                        if(document.getElementById('advContinueBtn-'+i)){
                            document.getElementById('advContinueBtn-'+i).addEventListener("click",function(){
                                continuePost(this);
                            });
                        }
                        if(document.getElementById('advAwaitingBtn-'+i)){
                            document.getElementById('advAwaitingBtn-'+i).addEventListener("click",function(){
                                awaitingPost(this);
                            });
                        }
                        // End: ChangeId: 2025042305
                    }
                    // End: ChangeId: 2024010901
                }
            });
}

// $('body').on('applicant_table', 'click', function() {
// alert("!2");
// });

function openAdv(){
// alert("HELLO12");

    //download("NRSC-RMT-2-2023_DOCTO_ramaprasad_c_nrsc_gov_in_preview.pdf");
    //return;
    var str = document.getElementById("viewAdvButton").value;
    if (str == "") {
        alert("Uploaded advertisement not found");
        return false; // ChangeId: 2023120602
    }
    // Start: ChangeId: 2023110302 Advertisement shifted to webserver
    //var tempPath = UPLOAD_DIR.split("applicant")[0] + "admin/";
    //var path = tempPath + str + ".pdf";  
    //var path = "docs/"+str + ".pdf";
    var path = "#"+str + ".pdf"; // ChangeId: 2023120602
    // End: ChangeId: 2023110302 Advertisement shifted to webserver
    //console.log(path);
    //window.open(path, 'popUpWindow',
    //        'height=400,width=600,left=10,top=10,scrollbars=yes,menubar=no');
    download(path); // ChangeId: 2023120602
    return false;
}
// Start: ChangeId: 2023111102
function validateAge(checkQualification){ // ChangeId: 2024011001 checkQualification added
    /* ChangeId: 2025041701
    var dobDate = new Date($("#dobAppl").val());
    var sd = dobDate.getTime();
    var ed = new Date().getTime();
    var ms = ed - sd;
    var hour1 = 60 * 60 * 1000, day1 = hour1 * 24, month1 = day1 * 30.4, year1 = day1 * 365.25;
    var yearT = ~~(ms / (year1));
    */
   
    // Start: ChangeId: 2025041701
    // Parse the date of birth string into a Date object
    const dob = new Date($("#dobAppl").val());
    //const today = new Date(); ChangeId: 2025050705
    const today = new Date("2025-05-30"); // ChangeId: 2025050705
    let yearT = today.getFullYear() - dob.getFullYear();
    
    // Check if the birthday has occurred yet this year
    const monthDiff = today.getMonth() - dob.getMonth();
    if (monthDiff > 0 || (monthDiff === 0 && today.getDate() > dob.getDate())) {
        yearT++; 
    }
    // End: ChangeId: 2025041701
    
    //var monthsT = ~~((ms - (year1 * yearT)) / month1);

    //Check age here
    var tabIndex = document.getElementById("indexval").value;
    var ageObjArray = advertisementDetails[tabIndex].Category;
    //console.log(ageObjArray);

    var minAge=18, maxAge=35; //defaults age limits set
    var catStr = "";

    var cat = $("#category").val().trim();
    var ageObjIndex = ageObjArray.findIndex((obj) => obj.category === cat);
    if(ageObjIndex > -1){
        minAge = parseInt(ageObjArray[ageObjIndex].age_limit_lower);
        maxAge = parseInt(ageObjArray[ageObjIndex].age_limit_upper);
    }
    catStr = catStr + cat;
    // End: Based on reservation category extract age limits
    
    // Start: Based on disability category extract age limits
    var pwd = $("#category_pwd").val().trim();
    if(pwd !== 'Select' && pwd !== "No" ){ //  multi-value selection
        pwd = 'PWD';
        catStr = catStr + ",PWBD";
    }
    var ageObjIndex = ageObjArray.findIndex((obj) => obj.category === pwd);
    if(ageObjIndex > -1){
        minAge = parseInt(ageObjArray[ageObjIndex].age_limit_lower);
        maxAge = parseInt(ageObjArray[ageObjIndex].age_limit_upper);
    }
    // End: Based on disability category extract age limits
    
    // Start: ChangeId: 2024011001
    if(checkQualification){
        if($("#ug_qualification2").val().trim() === "M.Sc"){
            catStr = catStr + ",M.Sc";
            maxAge = maxAge - 2;
        }
        
        // Start: ChangeId: 2025050704
        if($("#ug_qualification2").val().trim() === "MSc-Tech"){
            catStr = catStr + ",MSc-Tech";
            maxAge = maxAge - 2;
        }
        // End: ChangeId: 2025050704
    }
    // End: ChangeId: 2024011001
    
    // Start: Based on Central Government servant status adjust age limits
    var cgov = $("#cgov_serv").val().trim();
    if( cgov === 'Yes' ){
        maxAge = maxAge + 5;
        catStr = catStr + ",Central Government Servant";
    }
    // End: Based on Central Government servant status adjust age limits

    // Start: Based on Ex-servicemen category extract age limits
    var exs = $("#category_ser").val().trim();
    if( exs === 'Yes' ){
        exs = 'EXS';
        catStr = catStr + ",Ex-Service";
    }
    var ageObjIndex = ageObjArray.findIndex((obj) => obj.category === exs);
    if(ageObjIndex > -1){
        minAge = parseInt(ageObjArray[ageObjIndex].age_limit_lower);
        maxAge = parseInt(ageObjArray[ageObjIndex].age_limit_upper);
    }
    // End: Based on Ex-servicemen category extract age limits
    
    
    // Check aplicant age now
    var retMsg = "Ok";
    if ( (yearT < minAge) || (yearT > maxAge) ) {
        // Start: ChangeId: 2025050805
        //retMsg = "You are not eligible to apply for this post, based on your selected category [" +catStr+ "], allowed age limit is "+minAge+" to "+maxAge;
        retMsg = "You are not eligible to apply for this post, As your age is beyond Age Limit, Maximum allowed age is " + maxAge + " as on 30th May 2025.";
        // End: 2025050805
    }
    return retMsg;
}
// End: ChangeId: 2023111102
function showFields(index, el) {
    //console.log("index="+index);
    //console.log("el="+el);
    //alert("reg id : " + advertisementDetails[index]["Reg_Id"]); 
    $("#pay_form").hide(); // ChangeId: 2023110802
    var flag1 = false;
    var name = "";
    $('#regid_context').val(advertisementDetails[index]["Reg_Id"]); // ChangeId:2023091801
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;

        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
                console.log(name);
            }
        }
    });
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        return;
    }

    document.getElementById('personalData').style.display = "none";
    document.getElementById("indexval").value = index;
    var table = document.getElementById('applicant_table');
    for (var i = 0; i < table.rows.length; i++) {
        table.rows[i].style.backgroundColor = "white"; // assign the base color
    }
    table.rows[parseInt(index) + 1].style.backgroundColor = "#f9e0bb"; // ChangeId: 2024010901 ParseInt added
    var selectFlg = $(this).hasClass("highlight");
    // alert("entered..." + selectFlg);
    document.getElementById("viewAdvButton").value = advertisementDetails[index]["advt_no"];
    document.getElementById("advt_no_applicant").value = advertisementDetails[index]["advt_no"];
    document.getElementById("post_no_applicant").value = advertisementDetails[index]["post_no"];
    document.getElementById("post_name_applicant").value = advertisementDetails[index]["post_name"];
    document.getElementById("advt_no").value = advertisementDetails[index]["advt_no"];
    document.getElementById("post_no").value = advertisementDetails[index]["post_no"];
    document.getElementById("main_advt_no").value = advertisementDetails[index]["advt_no"];
    document.getElementById("main_post_no").value = advertisementDetails[index]["post_no"];
    document.getElementById("displaybuttons").style.display = "block";
    $("html, body").animate({
        scrollTop: $("#displaybuttons").offset().top - 10
    }, "slow");
    if (advertisementDetails[index]["no_of_attempts"] == 0) {
        document.getElementById("reapplyDiv").style.display = "none";
        document.getElementById("applyDiv").style.display = "block"; 
    } else if (advertisementDetails[index]["no_of_attempts"] >= MAX_APPLY_COUNT) { // ChangeId:2023091801 '==' => '>='
        document.getElementById("reapplyDiv").style.display = "none";
        document.getElementById("applyDiv").style.display = "none";
        //ChangeId: 2023111103 alert("Maximum attempts taken. Your previous application is not deleted and will be considered for the post.");
    } else {
        document.getElementById("reapplyDiv").style.display = "block";
        document.getElementById("applyDiv").style.display = "none";
    }
    // Start: ChangeId:2023091801
    
    switch(advertisementDetails[index]["application_status"]){
        case "POSTED": 
            $("#application_status").val("posted");
            document.getElementById("reapplyDiv").style.display = "none";
            document.getElementById("applyDiv").style.display = "none";
            document.getElementById("homeSubmitDiv").style.display = "none";
            document.getElementById("homeMakePaymentDiv").style.display = "none";
            break;
            
        case "PENDING": // ChangeId: 2023121402
            $("#application_status").val("pending");
            document.getElementById("reapplyDiv").style.display = "none";
            document.getElementById("applyDiv").style.display = "none";
            document.getElementById("homeSubmitDiv").style.display = "none";
            document.getElementById("homeMakePaymentDiv").style.display = "block";
            break;
            
        case "PAYMENT": 
            $("#application_status").val("payment");
            document.getElementById("reapplyDiv").style.display = "none";
            document.getElementById("applyDiv").style.display = "none";
            document.getElementById("homeSubmitDiv").style.display = "none";
            document.getElementById("homeMakePaymentDiv").style.display = "block";
            break;
        case "FORWARDED": // ChangeId: 2023121402
            $("#application_status").val("forwarded");
            document.getElementById("reapplyDiv").style.display = "none";
            document.getElementById("applyDiv").style.display = "none";
            document.getElementById("homeSubmitDiv").style.display = "none";
            document.getElementById("homeMakePaymentDiv").style.display = "block";
            break;
        case "PAYMENTFAILED": // ChangeId: 2023121402
        case "PAYMNTFAILED": // ChangeId: 2025052001
            $("#application_status").val("paymentfailed");
            document.getElementById("reapplyDiv").style.display = "none";
            document.getElementById("applyDiv").style.display = "none";
            document.getElementById("homeSubmitDiv").style.display = "none";
            document.getElementById("homeMakePaymentDiv").style.display = "block";
            break;
            
        case "PAID": 
            $("#application_status").val("paid");
            document.getElementById("reapplyDiv").style.display = "none";
            document.getElementById("applyDiv").style.display = "none";
            document.getElementById("homeSubmitDiv").style.display = "block";
            document.getElementById("homeMakePaymentDiv").style.display = "none";
            break;
            
        default: 
            // Start: ChnageId: 2023110803
            $("#application_status").val("unknown");
            document.getElementById("homeSubmitDiv").style.display = "none";
            document.getElementById("homeMakePaymentDiv").style.display = "none";
            // End: ChnageId: 2023110803
            break;
    }
    // End: ChangeId:2023091801
    
// if (advertisementDetails[index]["Reg_Id"] !== "-") {
// document.getElementById("reapplyDiv").style.display = "block";
// document.getElementById("applyDiv").style.display = "none";
// if(advertisementDetails[index]["no_of_attempts"] == MAX_APPLY_COUNT) {
// document.getElementById("reapplyDiv").style.display = "none";
// alert("Maximum attempts taken. Your previous application is not deleted
// and will be considered for the post.");
// }
// } else {
// document.getElementById("reapplyDiv").style.display = "none";
// document.getElementById("applyDiv").style.display = "block";
// }
}

function reSubmitApplication() {
    var tabIndex = document.getElementById("indexval").value;
    var regId = advertisementDetails[tabIndex]["Reg_Id"];
    if (regId == "-") {
        displayPostFields();
        return;
    }
    var article = new Object();
    article.regId = regId;
    article.email = email;
    article.advt_no = $("#advt_no").val();
    article.post_no = $("#post_no").val();
    article.post_name = $("#post_name_applicant").val();
    article.action = "resubmit";
    // alert("Re-submitting application");
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadAdvtNos",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Internal error occurred. Please check again or contact website administrator. Code:RSA");
                },
                success: function (data) {
                    if (data.Results[0] === "success") {
                        var tabIndex = document.getElementById("indexval").value;
                        var att = parseInt(advertisementDetails[tabIndex]["no_of_attempts"]) + 1;
                        var rem = 3 - att;
                        alert("Existing application has been deleted. Please apply again. This is your attempt count : "
                                + att + " (" + rem + " attempts remaining)");
                        // alert("tabIndex : " + tabIndex);
                        // table.rows[tabIndex+1].cells[6].innerHTML = "";
                        var table = document.getElementById('applicant_table');
                        var adv = $("#advt_no").val();
                        var post = $("#post_no").val();
                        var n = table.rows.length;
                        for (var r = 1; r < n; r++) {
                            var a = table.rows[r].cells[1].innerHTML;
                            var p = table.rows[r].cells[2].innerHTML;
                            // alert("a : " + a + ", " + adv + ", p : " + p + ",
                            // " + post);
                            if ((a === adv) && (p === post)) {
                                var tabCell = table.rows[r].cells[6];
                                tabCell.innerHTML = "-";
                            }
                        }

                        advertisementDetails[tabIndex]["Reg_Id"] = "-";
                        advertisementDetails[tabIndex]["Applied_Date"] = "";
                        advertisementDetails[tabIndex]["Preview_File"] = "";
                        displayPostFields();
                    } else if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    } else {
                        alert(data.Results[0]);
                    }
                }
            });
}

function displayPostFields() {
    $("#loadMe").modal("show"); // ChangeId: 2024021301
    var dobDate = new Date($("#dobAppl").val());
    var sd = dobDate.getTime();
    //var ed = new Date(today).getTime(); // ChangeId: 2025050705
    var ed = new Date("2025-05-30").getTime(); // ChangeId: 2025050705
    var ms = ed - sd;
    var hour1 = 60 * 60 * 1000, day1 = hour1 * 24, month1 = day1 * 30.4, year1 = day1 * 365.25;
    var yearT = ~~(ms / (year1));
    // alert("yearT : " + yearT);
    var monthsT = ~~((ms - (year1 * yearT)) / month1);
    // alert("monthsT : " + monthsT);
    // 
    //Check age here
     
    var tabIndex = document.getElementById("indexval").value;
    // Start: ChnageId: 2023111102
    /*console.log("tabIndex= "+tabIndex);
    var ageObj = advertisementDetails[tabIndex].Category;
    console.log(ageObj);
    var maxAge = parseInt(ageObj[0].age_limit_upper);
    if (yearT > maxAge) {
        alert("Age limit for this post is " + maxAge + ". You are not eligible as per your date of birth.");
        return;
    }*/
    // End: ChnageId: 2023111102
    //Nurse and doc here
    $("#nurseDiv").hide();
    $("#docDiv").hide();
    
    if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Nurse") > -1) {
        $("#nurseDiv").show();
        // Start: ChangeId: 2024012003
        $("#dip_division option[value='II']").remove();
        $("#dip_division option[value='III']").remove();
        $("#dip_division option[value='Pass']").remove();
        $("#dip_division option[value='NotAvailable']").remove();
        // End: ChangeId: 2024012003
    } else if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Medical") > -1) {
        $("#docDiv").show();
    } // Start: ChangeId: 2024012003
    else if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Library") > -1) {
        $("#ug_division option[value='II']").remove();
        $("#ug_division option[value='III']").remove();
        $("#ug_division option[value='Pass']").remove();
        $("#ug_division option[value='NotAvailable']").remove();
    } // End: ChangeId: 2024012003
    //Nurse and doc ends here

    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        return;
    }

    $("html, body").animate({
        scrollTop: $("#displaybuttons").offset().top
    }, "slow");
    $('#messageDiv').hide();
    // $("#personalData :input").prop("disabled", false);
    // $("#next_personal").prop("disabled", false);
    // $("#email_inp").prop("disabled", true);
    // $("#contact_no").prop("disabled", true);

    document.getElementById('personalData').style.display = "block";
    // alert("Calling from here");
    openPage('start', 'Personal_Details');
    document.getElementById('age_label').innerHTML = "(" + yearT + " yrs " + monthsT + " months) as on 30th May 2025"; // ChangeId: 2025050705
    document.getElementById('he_table_body').innerHTML = '';
    document.getElementById('tableEligAppDiv').style.display = 'none';
    

    // var tablinks = document.getElementsByClassName("tablink");
    // for (i = 0; i < tablinks.length; i++) {
    // tablinks[i].style.backgroundColor = "";
    // }

    $('#contact_no').val(document.getElementById("app_contact").value);
    $('#xii_specialization').empty();
    $('#iti').empty();
    $('#dip').empty();
    $('#iti_specialization').empty();
    $('#dip_specialization').empty();
    // $('#category').empty();
    $('#ug_discipline').empty();
    $('#ug_qualification1').empty();
    $('#ug_qualification2').empty();
    $('#ug_spec').empty();
    $('#xiiDiv').hide();
    $('#itiDiv').hide();
    $('#dipDiv').hide();
    $('#heDiv').hide();
    $('#nocExp').hide();
    $("#rcDiv").hide();
    document.getElementById("upload_rc").name = "NA";
    document.getElementById("upload_rc").value = "";
    document.getElementById("rc_size").innerHTML = "";
    $("#disDiv").hide();
    $("#disDivScribe").hide(); /* ChangeId:2025041501 */
    $("#disDivCompTime").hide(); /* ChangeId:2025050807 */
    document.getElementById("category_pwd_scribe").value = ""; /* ChangeId:2025041501 */
    document.getElementById("category_pwd_comptime").value = ""; /* ChangeId:2025050807 */
    document.getElementById("upload_disability").name = "NA";
    document.getElementById("upload_disability").value = "";
    document.getElementById("disability_size").innerHTML = "";
    $("#servicemanDiv").hide();
    document.getElementById("upload_serviceman").name = "NA";
    document.getElementById("upload_serviceman").value = "";
    document.getElementById("serviceman_size").innerHTML = "";
    $("#ewsDiv").hide();
    document.getElementById("upload_ews").name = "NA";
    document.getElementById("upload_ews").value = "";
    document.getElementById("ews_size").innerHTML = "";
    document.getElementById("upload_photograph").name = "NA";
    document.getElementById("upload_photograph").value = "";
    document.getElementById("photograph_size").innerHTML = "";
    document.getElementById("PreviewPhoto").style.display = "none";
    document.getElementById("upload_signature").name = "NA";
    document.getElementById("upload_signature").value = "";
    document.getElementById("signature_size").innerHTML = "";
    document.getElementById("PreviewSignature").style.display = "none";
    document.getElementById("upload_x_formula").name = "NA";
    document.getElementById("upload_x_formula").value = "";
    document.getElementById("x_formula_size").innerHTML = "";
    document.getElementById("upload_x_degree_certificate").name = "NA";
    document.getElementById("upload_x_degree_certificate").value = "";
    document.getElementById("x_degree_certificate_size").innerHTML = "";
    document.getElementById("xii_formula").name = "NA";
    document.getElementById("xii_formula").value = "";
    document.getElementById("xii_formula_size").innerHTML = "";
    document.getElementById("upload_xii_degree_certificate").name = "NA";
    document.getElementById("upload_xii_degree_certificate").value = "";
    document.getElementById("xii_degree_certificate_size").innerHTML = "";
    document.getElementById("iti_formula").name = "NA";
    document.getElementById("iti_formula").value = "";
    document.getElementById("iti_formula_size").innerHTML = "";
    document.getElementById("dip_formula").name = "NA";
    document.getElementById("dip_formula").value = "";
    document.getElementById("dip_formula_size").innerHTML = "";
    document.getElementById("upload_iti_degree_certificate").name = "NA";
    document.getElementById("upload_iti_degree_certificate").value = "";
    document.getElementById("iti_degree_certificate_size").innerHTML = "";
    document.getElementById("upload_dip_degree_certificate").name = "NA";
    document.getElementById("upload_dip_degree_certificate").value = "";
    document.getElementById("dip_degree_certificate_size").innerHTML = "";
    document.getElementById("upload_ug_formula").name = "NA";
    document.getElementById("upload_ug_formula").value = "";
    document.getElementById("ug_formula_size").innerHTML = "";
    document.getElementById("upload_ug_marksheet").name = "NA";
    document.getElementById("upload_ug_marksheet").value = "";
    document.getElementById("ug_marksheet_size").innerHTML = "";
    document.getElementById("upload_ug_degree_certificate").name = "NA";
    document.getElementById("upload_ug_degree_certificate").value = "";
    document.getElementById("ug_degree_certificate_size").innerHTML = "";
    // PPEG-HRD Start MedicalRegCertificate
    document.getElementById("upload_doc_reg_certificate").name = "NA";
    document.getElementById("upload_doc_reg_certificate").value = "";
    document.getElementById("doc_reg_certificate_size").innerHTML = "";
    // PPEG-HRD End MedicalRegCertificate
    
    // PPEG-HRD Start NurseRegCertificate
    document.getElementById("upload_nurse_reg_certificate").name = "NA";
    document.getElementById("upload_nurse_reg_certificate").value = "";
    document.getElementById("nurse_reg_certificate_size").innerHTML = "";
    // PPEG-HRD End NurseRegCertificate
    
    //Experience clearing
    document.getElementById('ex_table_body').innerHTML = '';
    document.getElementById('tableExpAppDiv').style.display = 'none';
    document.getElementById("upload_exp_certificate").name = "NA";
    document.getElementById("upload_exp_certificate").value = "";
    document.getElementById("exp_certificate_size").innerHTML = "";
    $("#emp_name").val("");
    $("#emp_address").val("");
    $("#emp_desig").val("");
    $("#emp_work").val("");
    $("#emp_paydrawn").val(""); // PPEG-HRD: PayDrawn
    $("#emp_reason").val(""); // PPEG-HRD: ReasonForLeaving
    $("#time_from").val("");
    $("#time_to").val("");
    $('#sameaddressconfirmation').prop('checked', false); // ChangeId: 2023110901
    $("#permanent_address_1 *").prop('disabled', false); // ChangeId: 2023110901
    $("#permanent_address_2 *").prop('disabled', false); // ChangeId: 2023110901

//   Added later    
//    document.getElementById("nes_ct").name = "NA";
//    document.getElementById("nes_ct").value = "";
//    document.getElementById("nes_size").innerHTML = "";


    // alert("Modify here ");
    // $('<option value="Select" selected disabled>Select</option>').appendTo(
    // '#category');
    changeDateRanges();
    // zone selection
    
    // var zoneVal = "NA";
    // if (zoneVal === "None") {
    // Start: ChangeID: 2023112901
    var zoneVal = advertisementDetails[tabIndex]["select_process"];
    $("#zoneDiv").hide();
    if (zoneVal === "Written Test") {
        $("#zoneDiv").show();
    } 
    // End: ChangeID: 2023112901
    //else {
    //	;
    //}

    var nArray = advertisementDetails[tabIndex]["net"].split("#");
    // alert("nArray : " + nArray);
    if (nArray.length > 0) {
        if (nArray[0] === "NA") {
            $('#nesDiv').hide();
        } else {
            $('#nesDiv').show();
            $('#netSel').empty();
            $('<option value="Select" selected disabled>Select</option>')
                    .appendTo('#netSel');
            for (var i = 0; i < nArray.length; ++i) {
                var obj = nArray[i];
                $('<option value="' + obj + '">' + obj + '</option>').appendTo(
                        '#netSel');
            }

// var addNesRow = document.getElementById("nesListDiv");
// addNesRow.innerHTML = "";
// for (var i = 0; i < nArray.length; ++i) {
// var obj = nArray[i];
// // var qualName = obj.Qualification;
// // var id = obj.id;
// // var disciplines = obj.Discipline;
// // var specs = obj.Spec;
//
// var qualDiv = document.createElement('div');
// qualDiv.className = 'col-md-4';
// var checkbox = document.createElement('input');
// checkbox.type = "checkbox";
// checkbox.name = "nesName";
// checkbox.value = obj;
// checkbox.id = "nes_cb" + i;
// checkbox.onchange = function() {
// takeCert(this);
// };
// // checkbox.onchange = function(){read_neCert(this);};
// qualDiv.appendChild(checkbox);
// qualDiv.appendChild(document.createTextNode(obj));
// if (obj === "Other") {
// var inputO = document.createElement('input');
// inputO.type = "text";
// inputO.name = "nesNameTxt";
// inputO.id = "nes_sco";
// inputO.className = "form-control";
// inputO.maxLength = "10";
// qualDiv.appendChild(inputO);
// }
//
// var input = document.createElement('input');
// input.type = "number";
// input.name = "nes_sc" + i;
// input.id = "nes_sc" + i;
// input.className = "form-control";
// input.min = "0";
// input.max = "10";
// input.value = "5.5";
// input.step = "0.1";
// var mandDiv = document.createElement('div');
// mandDiv.className = 'col-md-3';
// mandDiv.appendChild(input);
//
// var certInp = document.createElement('input');
// certInp.type = "text";
// certInp.name = "nes_ct" + i;
// certInp.id = "nes_ct" + i;
// certInp.className = "form-control-1";
// certInp.value = "NA";
// var certDiv = document.createElement('div');
// certDiv.className = 'col-md-5';
// certDiv.appendChild(certInp);
//
// var iDiv = document.createElement('div');
// iDiv.className = 'row';
// iDiv.appendChild(qualDiv);
// iDiv.appendChild(mandDiv);
// iDiv.appendChild(certDiv);
// addNesRow.appendChild(iDiv);
// }
        }
    } else {
        $('#nesDiv').hide();
    }

    var eArray = advertisementDetails[tabIndex]["Eligibility"];
    var valsSetFlag = false;
    $('<option value="Select" selected disabled>Select</option>').appendTo('#xii_specialization');
    eligList = [];
    var elArr = new Array();
    // alert("1234");
    $('<option value="Select" selected disabled>Select</option>').appendTo('#ug_qualification1');
    $('<option value="Select" selected disabled>Select</option>').appendTo('#ug_qualification2');
    for (var j = 0; j < eArray.length; j++) {
        var elig = eArray[j]["eligibility"];
        if (elig === "") {
            alert("Internal error. Please contact web server administrator. Code:DPF");
            return;
        }
        var qualification = eArray[j]["essential_qualification"];
        // alert("Elig : " + elig + ", Qual : " + qualification);

        if ((elig === "X") || (elig === "XII")) {
            continue;
        }
        if (elig === "ITI") {
            $('<option value="' + elig + '">' + elig + '</option>').appendTo(
                    '#iti');
            var itiSpec = eArray[j]["specialization"];
            var itiSpecArr = itiSpec.split("@");
            for (var k = 0; k < itiSpecArr.length; k++) {
                $(
                        '<option value="' + itiSpecArr[k] + '">'
                        + itiSpecArr[k] + '</option>').appendTo(
                        '#iti_specialization');
            }
        } else if (elig === "DIPLOMA") {
            $('<option value="' + elig + '">' + elig + '</option>').appendTo(
                    '#dip');
            var dipSpec = eArray[j]["specialization"];
            var dipSpecArr = dipSpec.split("@");
            for (var k = 0; k < dipSpecArr.length; k++) {
                $(
                        '<option value="' + dipSpecArr[k] + '">'
                        + dipSpecArr[k] + '</option>').appendTo(
                        '#dip_specialization');
            }
        } else {
            if (elArr.indexOf(elig) === -1) {
                elArr.push(elig);
                $('<option value="' + elig + '">' + elig + '</option>')
                        .appendTo('#ug_qualification1');
            }

// alert("elig : " + elig + ", " + eligList[elig] + ", " +
// qualification);
            if (eligList[elig] === undefined) {
                var tempArr = new Array();
                tempArr.push(qualification);
                eligList[elig] = tempArr;
            } else {
                var tempArr = eligList[elig];
                tempArr.push(qualification);
                eligList[elig] = tempArr;
            }
        }
    }
// var tempArr = eligList[elArr[0]];
// if (tempArr !== undefined) {
// for (j = 0; j < tempArr.length; j++) {
// var qual = tempArr[j];
// $('<option value="' + qual + '">' + qual + '</option>').appendTo(
// '#ug_qualification2');
// }
// }

    var xiiSpecFlag = false;
    for (var j = 0; j < eArray.length; j++) {
        var elig = eArray[j]["eligibility"];
        if ((elig === "ITI")) {
            $('#itiDiv').show();
        }
        if ((elig === "DIPLOMA")) {
            $('#dipDiv').show();
        }
        var specialization = eArray[j]["specialization"];
        if (elig == "XII") {
            $('#xiiDiv').show();
            var specA = specialization.split("@");
            for (var k = 0; k < specA.length; k++) {
                xiiSpecFlag = true;
                $('<option value="' + specA[k] + '">' + specA[k] + '</option>')
                        .appendTo('#xii_specialization');
            }
        }
        if ((elig == "X") || (elig == "XII") || (elig == "ITI")
                || (elig === "DIPLOMA")) {
            continue;
        }
        $('#xiiDiv').show();
        if (!valsSetFlag) {
            valsSetFlag = true;
            $('<option value="Select" selected disabled>Select</option>')
                    .appendTo('#ug_discipline');
            $('<option value="Select" selected disabled>Select</option>')
                    .appendTo('#ug_spec');
            var discipline = eArray[j]["discipline"];
            $('<option value="' + discipline + '">' + discipline + '</option>')
                    .appendTo('#ug_discipline');
            // var specA = specialization.split("@");
            // for (var k = 0; k < specA.length; k++) {
            // $('<option value="' + specA[k] + '">' + specA[k] + '</option>')
            // .appendTo('#ug_spec');
            // }
        }
        $('#heDiv').show();
    }
    if (!xiiSpecFlag) {
        $('<option value="Others">Others</option>').appendTo(
                '#xii_specialization');
    }

// experience
    $('#experience').empty();
    $('<option value="Select" selected disabled>Select</option>').appendTo(
            '#experience');
    var expVal = advertisementDetails[tabIndex]["experience"];
    // var expVal = "No";
    if (expVal === "Optional") {
        $('<option value="No" >No</option>').appendTo('#experience');
        $('<option value="Yes" >Yes</option>').appendTo('#experience');
    } else if (expVal === "Mandatory") {
        $('<option value="Yes" >Yes</option>').appendTo('#experience');
    }

    $("#personalData").focus();
    var article = new Object();
    article.email = email;
    article.action = "SavedApplicantData";
    // alert("Getting app info");
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadAdvtNos",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    $("#loadMe").modal("hide"); // ChangeId: 2024021301
                    // ChangeId: 2024021301
                    setTimeout(function() {
                        alert("Internal error occurred. Please check again or contact website administrator.");
                    }, 1000);
                    
                },
                success: function (data) {
                    $("#loadMe").modal("hide"); // ChangeId: 2024021301
                    if (data.Results[0] == "invalid_session") {
                        // ChangeId: 2024021301
                        setTimeout(function() {
                            alert("Invalid session identified. Redirecting to home page.");
                            window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                            
                        }, 1000);
                        return;
                    }

                    var tableData = data.Results;
                    if (tableData.length == 0) {
                        return;
                    }
                    var pArray = tableData[0]["Personal"];
                    var xArray = tableData[0]["X"];
                    var xiiArray = tableData[0]["XII"];
                    var exArray = tableData[0]["Experience"];
                    if (pArray[0] !== undefined) {
                        if (pArray[0]["salutation"] != "NA"
                                && pArray[0]["salutation"] != "null"
                                && pArray[0]["salutation"] != "-1") {
                            $("#salutation").val(pArray[0]["salutation"]);
                            changeGender();
                        }
                        if (pArray[0]["first_name"] != "NA"
                                && pArray[0]["first_name"] != "null"
                                && pArray[0]["first_name"] != "-1") {
                            $("#first_name").val(pArray[0]["first_name"]);
                        }
                        // if (pArray[0]["middle_name"] != "NA"
                        // && pArray[0]["middle_name"] != "null"
                        // && pArray[0]["middle_name"] != "-1") {
                        // $("#middle_name").val(pArray[0]["middle_name"]);
                        // }
                        // if (pArray[0]["last_name"] != "NA"
                        // && pArray[0]["last_name"] != "null"
                        // && pArray[0]["last_name"] != "-1") {
                        // $("#last_name").val(pArray[0]["last_name"]);
                        // }
                        if (pArray[0]["father_name"] != "NA"
                                && pArray[0]["father_name"] != "null"
                                && pArray[0]["father_name"] != "-1") {
                            $("#father_name").val(pArray[0]["father_name"]);
                        }
                        if (pArray[0]["mother_name"] != "NA"
                                && pArray[0]["mother_name"] != "null"
                                && pArray[0]["mother_name"] != "-1") {
                            $("#mother_name").val(pArray[0]["mother_name"]);
                        }
                        if (pArray[0]["gender"] != "NA"
                                && pArray[0]["gender"] != "null"
                                && pArray[0]["gender"] != "-1") {
                            $("#gender").val(pArray[0]["gender"]);
                        }
                        if (pArray[0]["marital_status"] != "NA"
                                && pArray[0]["marital_status"] != "null"
                                && pArray[0]["marital_status"] != "-1") {
                            $("#marital_status").val(
                                    pArray[0]["marital_status"]);
                        }
                        if (pArray[0]["category"] != "NA"
                                && pArray[0]["category"] != "null"
                                && pArray[0]["category"] != "-1") {
                            $("#category").val(pArray[0]["category"]);
                            // Start: ChangeId:2023083002
                            setTimeout(function() {
                                $("#category").val(pArray[0]["category"]);
                                changeCategory();
                              }, 200);
                            // End: ChangeId:2023083002
                        }
                        // Start: ChangeId:2023083002
                        if (pArray[0]["category_pwd"] != "NA"
                                && pArray[0]["category_pwd"] != "null"
                                && pArray[0]["category_pwd"] != "-1") {
                          
                            setTimeout(function() {
                                $("#category_pwd").val(pArray[0]["category_pwd"]);
                                PWDCheck();
                              }, 200);
                        }
                        // End: ChangeId:2023083002
                        
                        // Start: ChangeId:2025041501
                        if (pArray[0]["category_pwd_scribe"] != "NA"
                                && pArray[0]["category_pwd_scribe"] != "null"
                                && pArray[0]["category_pwd_scribe"] != "-1") {
                          
                            setTimeout(function() {
                                $("#category_pwd_scribe").val(pArray[0]["category_pwd_scribe"]);
                              }, 200);
                        }
                        if(pArray[0]["disability_certificate"] != "NA" &&
                            pArray[0]["disability_certificate"]!="null" && 
                            pArray[0]["disability_certificate"]!="-1") {

                            if(pArray[0]["disability_certificate"]){
                                download(pArray[0]["disability_certificate"],null,null,"upload_disability",read_disability);
                            }
                        }

                        // End: ChangeId:2025041501
                        
                        
                        // Start: ChangeId:2025050807
                        if (pArray[0]["category_pwd_comptime"] != "NA"
                                && pArray[0]["category_pwd_comptime"] != "null"
                                && pArray[0]["category_pwd_comptime"] != "-1") {
                          
                            setTimeout(function() {
                                $("#category_pwd_comptime").val(pArray[0]["category_pwd_comptime"]);
                              }, 200);
                        }
                        // End: ChangeId:2025050807
                        
                        // Start: ChangeId:2023083002
                        if (pArray[0]["category_exservice"] !== "NA"
                                && pArray[0]["category_exservice"] !== "null"
                                && pArray[0]["category_exservice"] !== "-1") {
                          
                            setTimeout(function() {
                                $("#category_ser").val(pArray[0]["category_exservice"]);
                                ExServiceCheck();
                              }, 200);
                        }

                        // End: ChangeId:2023083002
                        
                        // Start: ChangeId:2023083002
                        // Start: ChangeId: 2023110702 SportMerit ExServicemen hidden with default No selected
                        /*
                        if (pArray[0]["category_merit"] != "NA"
                                && pArray[0]["category_merit"] != "null"
                                && pArray[0]["category_merit"] != "-1") {
                          
                            setTimeout(function() {
                                $("#category_spt").val(pArray[0]["category_merit"]);
                                MeritCheck(document.getElementById("category_spt"));
                              }, 200);
                        }
                        */
                        $("category_spt").val("No");
                        // End: ChangeId: 2023110702 SportMerit ExServicemen hidden with default No selected
                        // End: ChangeId:2023083002
                        
                        // Start: ChangeId:2023083002
                        if (pArray[0]["category_ews"] != "NA"
                                && pArray[0]["category_ews"] != "null"
                                && pArray[0]["category_ews"] != "-1") {
                          
                            setTimeout(function() {
                                $("#category_ews").val(pArray[0]["category_ews"]);
                                changeEWS();
                              }, 200);
                        }
                        // End: ChangeId:2023083002
                        if (pArray[0]["place_of_birth"] != "NA"
                                && pArray[0]["place_of_birth"] != "null"
                                && pArray[0]["place_of_birth"] != "-1") {
                            $("#place_of_birth").val(
                                    pArray[0]["place_of_birth"]);
                        }
                        if (pArray[0]["nationality"] != "NA"
                                && pArray[0]["nationality"] != "null"
                                && pArray[0]["nationality"] != "-1") {
                            $("#nationality").val(pArray[0]["nationality"]);
                        }
                        if (pArray[0]["house_no"] != "NA"
                                && pArray[0]["house_no"] != "null"
                                && pArray[0]["house_no"] != "-1") {
                            $("#house_no").val(pArray[0]["house_no"]);
                        }
                        if (pArray[0]["locality"] != "NA"
                                && pArray[0]["locality"] != "null"
                                && pArray[0]["locality"] != "-1") {
                            $("#locality").val(pArray[0]["locality"]);
                        }
                        if (pArray[0]["town"] != "NA"
                                && pArray[0]["town"] != "null"
                                && pArray[0]["town"] != "-1") {
                            $("#town").val(pArray[0]["town"]);
                        }
                        if (pArray[0]["state"] != "NA"
                                && pArray[0]["state"] != "null"
                                && pArray[0]["state"] != "-1") {
                            $("#state").val(pArray[0]["state"]);
                            loadDistrictOptions(pArray[0]["district"]);
                        }
                        if (pArray[0]["pincode"] != "NA"
                                && pArray[0]["pincode"] != "null"
                                && pArray[0]["pincode"] != "-1") {
                            $("#pincode").val(pArray[0]["pincode"]);
                        }
                        if (pArray[0]["p_house_no"] != "NA"
                                && pArray[0]["p_house_no"] != "null"
                                && pArray[0]["p_house_no"] != "-1") {
                            $("#p_house_no").val(pArray[0]["p_house_no"]);
                        }
                        if (pArray[0]["p_locality"] != "NA"
                                && pArray[0]["p_locality"] != "null"
                                && pArray[0]["p_locality"] != "-1") {
                            $("#p_locality").val(pArray[0]["p_locality"]);
                        }
                        if (pArray[0]["p_town"] != "NA"
                                && pArray[0]["p_town"] != "null"
                                && pArray[0]["p_town"] != "-1") {
                            $("#p_town").val(pArray[0]["p_town"]);
                        }
                        if (pArray[0]["p_state"] != "NA"
                                && pArray[0]["p_state"] != "null"
                                && pArray[0]["p_state"] != "-1") {
                            $("#p_state").val(pArray[0]["p_state"]);
                            loadPDistrictOptions(pArray[0]["p_district"]);
                        }
                        
                        // COMMENT: p_district value is set inside loadPDistrictOptions()
                        // ChangeId: 2023110901
                        //if (pArray[0]["p_district"] != "NA"
                        //        && pArray[0]["p_district"] != "null"
                        //        && pArray[0]["p_district"] != "-1") {
                        //    $("#p_district").val(pArray[0]["p_district"]);
                            // Start: ChangeId:2023083002
                        //    setTimeout(function() {
                        //        $("#p_district").val(pArray[0]["p_district"]);
                        //      }, 300);
                        //    // End: ChangeId:2023083002
                        //}
                        if (pArray[0]["p_pincode"] != "NA"
                                && pArray[0]["p_pincode"] != "null"
                                && pArray[0]["p_pincode"] != "-1") {
                            $("#p_pincode").val(pArray[0]["p_pincode"]);
                        }
                        if (pArray[0]["zone"] != "NA"
                                && pArray[0]["zone"] != "null"
                                && pArray[0]["zone"] != "-1"
                                && pArray[0]["zone"] != "") {
                            // Start: ChangeId: 2023112001
                            $("#zone_sel").val([]); // ChangeId: 2024011201
                            var zoneArray = pArray[0]["zone"].split("@");
                            // Start: ChnageId: 2023123101
                            for(var z=0;z<zoneArray.length;z++){
                                
                                $("#zone_sel option[value='"+zoneArray[z]+"']").remove(); // ChangeId: 2024012001
                                var option = new Option(zoneArray[z], zoneArray[z], true, true);
                                var selectdata = {'text':zoneArray[z],'id':zoneArray[z],'selected':true,'disabled':false, 'element':option}
                                $("#zone_sel").trigger({
                                    type: 'select2:select',
                                    params: {
                                        data: selectdata
                                    }
                                });
                            }
                            //$("#zone_sel").val(zoneArray);
                            //$("#zone_sel").change();
                            // End: ChnageId: 2023123101
                            // Start: ChangeId: 2023112001
                        }
                        if (pArray[0]["nearest_railway_station"] != "NA"
                                && pArray[0]["nearest_railway_station"] != "null"
                                && pArray[0]["nearest_railway_station"] != "-1") {
                            $("#nearest_railway_station").val(
                                    pArray[0]["nearest_railway_station"]);
                        }
                        if (pArray[0]["contact_no"] != "NA"
                                && pArray[0]["contact_no"] != "null"
                                && pArray[0]["contact_no"] != "-1") {
                            $("#contact_no").val(pArray[0]["contact_no"]);
                        }
                        if (pArray[0]["alternate_contact"] != "NA"
                                && pArray[0]["alternate_contact"] != "null"
                                && pArray[0]["alternate_contact"] != "-1"
                                && pArray[0]["alternate_contact"] != "0") {
                            $("#alternate_contact").val(
                                    pArray[0]["alternate_contact"]);
                        }
                        // Start PPEG-HRD AADHAAR
                        if (pArray[0]["aadhaar"] !== "NA"
                                && pArray[0]["aadhaar"] !== "null"
                                && pArray[0]["aadhaar"] !== "-1") {
                            $("#aadhaar").val(pArray[0]["aadhaar"]);
                        }
                        // End PPEG-HRD AADHAAR
                        //Start: ChangeId:2025041601
                        if (pArray[0]["bank_acc_beneficiary"] != "NA"
                                && pArray[0]["bank_acc_beneficiary"] != "null"
                                && pArray[0]["bank_acc_beneficiary"] != "-1") {
                            $("#bank_acc_beneficiary").val(
                                    pArray[0]["bank_acc_beneficiary"]);
                        }
                        
                        if(pArray[0]["bank_acc_doc"] != "NA" &&
                            pArray[0]["bank_acc_doc"]!="null" && 
                            pArray[0]["bank_acc_doc"]!="-1") {

                            if(pArray[0]["bank_acc_doc"]){
                                download(pArray[0]["bank_acc_doc"],null,null,"bank_acc_doc",readBankAccDoc);
                            }
                        }
                        //End: ChangeId:2025041601
                        //
                        // Start: ChangeId: 2023111001
                        if (pArray[0]["bank_acc_no"] != "NA"
                                && pArray[0]["bank_acc_no"] != "null"
                                && pArray[0]["bank_acc_no"] != "-1") {
                            $("#bank_acc_no").val(
                                    pArray[0]["bank_acc_no"]);
                            $("#bank_acc_no_confirm").val(
                                    pArray[0]["bank_acc_no"]);  // ChangeId: 2025041601
                        }
                        if (pArray[0]["bank_ifsc_code"] != "NA"
                                && pArray[0]["bank_ifsc_code"] != "null"
                                && pArray[0]["bank_ifsc_code"] != "-1") {
                            $("#bank_ifsc_code").val(
                                    pArray[0]["bank_ifsc_code"]);
                            $("#bank_ifsc_code_confirm").val(
                                    pArray[0]["bank_ifsc_code"]); // ChangeId: 2025041601
                        }
                        
                        // End: ChangeId: 2023111001
                        
                        // Start: ChangeId: 2023111101
                        if (pArray[0]["cgov_serv"] !== "NA"
                                && pArray[0]["cgov_serv"] !== "null"
                                && pArray[0]["cgov_serv"] !== "-1") {
                            
                            setTimeout(function() {
                                $("#cgov_serv").val(pArray[0]["cgov_serv"]);
                                CentralGovCheck();
                              }, 200);
                        }
                        if (pArray[0]["cgov_serv_doj"] !== "NA"
                                && pArray[0]["cgov_serv_doj"] !== "null"
                                && pArray[0]["cgov_serv_doj"] !== "-1") {
                            $("#cgov_serv_doj").val(pArray[0]["cgov_serv_doj"]);
                        }
                        // End: ChangeId: 2023111101
                        
                        // Start: ChangeId: 2023111607
                        if(pArray[0]["photo"] !== "NA" &&
                            pArray[0]["photo"]!== "null" && 
                            pArray[0]["photo"]!== "-1") {

                            if(pArray[0]["photo"]){
                                download(pArray[0]["photo"],null,null,"upload_photograph",readURL);
                            }
                        }

                        if(pArray[0]["signature"] !== "NA" &&
                            pArray[0]["signature"]!== "null" &&
                            pArray[0]["signature"]!== "-1") {

                            if(pArray[0]["signature"]){
                                download(pArray[0]["signature"],null,null,"upload_signature",readSignature);
                            }
                        }
                        // End: ChangeId: 2023111607
                                               
                    }

                    if (xArray[0] !== undefined) {
                        var x_board = "NA";
                        if (xArray[0]["x_edu_board"] != "NA"
                                && xArray[0]["x_edu_board"] != "null"
                                && xArray[0]["x_edu_board"] != "-1") {
                            x_board = xArray[0]["x_edu_board"];
                            $("#x_edu_board").val(x_board);
                        }
                        if (xArray[0]["x_school"] != "NA"
                                && xArray[0]["x_school"] != "null"
                                && xArray[0]["x_school"] != "-1") {
                            $("#x_school").val(xArray[0]["x_school"]);
                        }
                        if (xArray[0]["x_year_of_passing"] != "NA"
                                && xArray[0]["x_year_of_passing"] != "null"
                                && xArray[0]["x_year_of_passing"] != "-1"
                                && xArray[0]["x_year_of_passing"] != "0") {
                            $("#x_year_of_passing").val(
                                    parseInt(xArray[0]["x_year_of_passing"]));
                        }
                        if (xArray[0]["x_division"] != "NA"
                                && xArray[0]["x_division"] != "null"
                                && xArray[0]["x_division"] != "-1") {
                            $("#x_division").val(xArray[0]["x_division"]);
                        }
                        // Start: ChangeID: 2023121901
                        //if (xArray[0]["x_percentage_cgpa"] != "NA"
                        //        && xArray[0]["x_percentage_cgpa"] != "null"
                        //        && xArray[0]["x_percentage_cgpa"] != "-1") {
                        //    $("#x_percentage_cgpa").val(
                        //            xArray[0]["x_percentage_cgpa"]);
                        // End: ChangeID: 2023121901
                            if (xArray[0]["x_percentage_cgpa"] == "CGPA" 
                                    || xArray[0]["x_percentage_cgpa"] == "NA") { // ChangeID: 2023121901
                                document.getElementById("ifXPer").style.display = "block"; // ChangeId 2023121901 none to block
                                document.getElementById("ifXCgpa").style.display = "block";
                                if (xArray[0]["x_cgpa_obt"] != "NA"
                                        && xArray[0]["x_cgpa_obt"] != "null"
                                        && xArray[0]["x_cgpa_obt"] != "-1"
                                        && xArray[0]["x_cgpa_obt"] != "0") {
                                    $("#x_cgpa_obt").val(
                                            xArray[0]["x_cgpa_obt"]);
                                }
                                if (xArray[0]["x_cgpa_max"] != "NA"
                                        && xArray[0]["x_cgpa_max"] != "null"
                                        && xArray[0]["x_cgpa_max"] != "-1") {
                                    $("#x_cgpa_max").val(
                                            xArray[0]["x_cgpa_max"]);
                                    var cm = parseFloat(document.getElementById("x_cgpa_max").value.trim());
                                    if(cm > 0.5 && cm < 9.5){
                                        $("#div_x_cgpa_to_percentage").show();
                                        $("#div_x_cgpa_to_percentage_formula").show();
                                    }
                                    else {
                                        $("#div_x_cgpa_to_percentage").hide();
                                        $("#div_x_cgpa_to_percentage_formula").hide();
                                    }
                                }
                                // Start: ChangeID: 2023121901
                                /*if (xArray[0]["x_percentage"] != "NA"
                                        && xArray[0]["x_percentage"] != "null"
                                        && xArray[0]["x_percentage"] != "-1") {
                                    $("#x_percentage").val(
                                            xArray[0]["x_percentage"]);
                                    ctrlxFormulaUpload(); // ChangeId: 2023112302
                                }*/
                                if (xArray[0]["x_percentage"] != "NA"
                                        && xArray[0]["x_percentage"] != "null"
                                        && xArray[0]["x_percentage"] != "-1") {
                                    $("#x_perc").val(
                                            xArray[0]["x_percentage"]);
                                    ctrlxFormulaUpload(); // ChangeId: 2023112302
                                }
                                if (xArray[0]["x_cgpa_to_perc"] != "NA"
                                        && xArray[0]["x_cgpa_to_perc"] != "null"
                                        && xArray[0]["x_cgpa_to_perc"] != "-1") {
                                    $("#x_percentage").val(
                                            xArray[0]["x_cgpa_to_perc"]);
                                    ctrlxFormulaUpload(); // ChangeId: 2023112302
                                }
                                // End: ChangeID: 2023121901
                            } // else // ChangeID: 2023121901
                                if (xArray[0]["x_percentage_cgpa"] == "Percentage"
                                        || xArray[0]["x_percentage_cgpa"] == "NA") { // ChangeID: 2023121901) { // PPEG-HRD else to elseif Percentage
                                document.getElementById("ifXPer").style.display = "block";
                                document.getElementById("ifXCgpa").style.display = "block"; // ChangeId 2023121901 none to block
                                if (xArray[0]["x_percentage"] != "NA"
                                        && xArray[0]["x_percentage"] != "null"
                                        && xArray[0]["x_percentage"] != "-1") {
                                    $("#x_per").val(xArray[0]["x_percentage"]);
                                }
                            
                            }
                            // Start: // ChangeID: 2023121901
                            /*else {
                                document.getElementById("ifXPer").style.display = "block"; // ChangeId 2023121901 none to block
                                document.getElementById("ifXCgpa").style.display = "block"; // ChangeId 2023121901 none to block
                            }*/
                            // End: // ChangeID: 2023121901
                        //} // ChangeID: 2023121901
                        
                        // Start: ChangeId: 2023111607
                        if(xArray[0]["marksheet"] != "NA" &&
                            xArray[0]["marksheet"]!="null" && 
                            xArray[0]["marksheet"]!="-1") {

                            if(xArray[0]["marksheet"]){
                                download(xArray[0]["marksheet"],null,null,"upload_x_degree_certificate",readx_degree_certificate);
                            }
                        }
                        // End: ChangeId: 2023111607
                    }

                    if (xiiArray[0] !== undefined) {
                        var xii_board = "NA";
                        if (xiiArray[0]["xii_edu_board"] != "NA"
                                && xiiArray[0]["xii_edu_board"] != "null"
                                && xiiArray[0]["xii_edu_board"] != "-1") {
                            xii_board = xiiArray[0]["xii_edu_board"];
                            $("#xii_edu_board").val(xii_board);
                        }
                        loadBoard(x_board, xii_board);
                        if (xiiArray[0]["xii_school"] != "NA"
                                && xiiArray[0]["xii_school"] != "null"
                                && xiiArray[0]["xii_school"] != "-1") {
                            $("#xii_school").val(xiiArray[0]["xii_school"]);
                        }
                        if (xiiArray[0]["xii_specialization"] != "NA"
                                && xiiArray[0]["xii_specialization"] != "null"
                                && xiiArray[0]["xii_specialization"] != "-1") {
                               
                            // Start: ChangeId: 2023111301
                            options = $("select#xii_specialization option").map(function() {return $(this).val();}).get();
                            
                            if ( options.indexOf(xiiArray[0]["xii_specialization"]) > -1 ){
                                $("#xii_specialization").val(
                                    xiiArray[0]["xii_specialization"]);
                                $("#ifXIIspc").hide();
                            } 
                            else{
                                $("#xii_specialization").val("Others");
                                $("#ifXIIspc").show();
                                $("#xii_specialization_other").val(
                                    xiiArray[0]["xii_specialization"]);
                            }
                            // End: ChangeId: 2023111301
                            
                        }
                        if (xiiArray[0]["xii_year_of_passing"] != "NA"
                                && xiiArray[0]["xii_year_of_passing"] != "null"
                                && xiiArray[0]["xii_year_of_passing"] != "-1"
                                && xiiArray[0]["xii_year_of_passing"] != "0") {
                            $("#xii_year_of_passing")
                                    .val(
                                            parseInt(xiiArray[0]["xii_year_of_passing"]));
                        }
                        if (xiiArray[0]["xii_division"] != "NA"
                                && xiiArray[0]["xii_division"] != "null"
                                && xiiArray[0]["xii_division"] != "-1") {
                            $("#xii_division").val(xiiArray[0]["xii_division"]);
                        }
                        /*
                        if (xiiArray[0]["xii_percentage_cgpa"] != "NA"
                                && xiiArray[0]["xii_percentage_cgpa"] != "null"
                                && xiiArray[0]["xii_percentage_cgpa"] != "-1") {
                            $("#xii_percentage_cgpa").val(
                                    xiiArray[0]["xii_percentage_cgpa"]);
                            if (xiiArray[0]["xii_percentage_cgpa"] == "CGPA") {
                                document.getElementById("ifXiiPer").style.display = "none";
                                document.getElementById("ifXiiCgpa").style.display = "block";
                                if (xiiArray[0]["xii_cgpa_obt"] != "NA"
                                        && xiiArray[0]["xii_cgpa_obt"] != "null"
                                        && xiiArray[0]["xii_cgpa_obt"] != "-1"
                                        && xiiArray[0]["xii_cgpa_obt"] != "0") {
                                    $("#xii_cgpa_obt").val(
                                            xiiArray[0]["xii_cgpa_obt"]);
                                }
                                if (xiiArray[0]["xii_cgpa_max"] != "NA"
                                        && xiiArray[0]["xii_cgpa_max"] != "null"
                                        && xiiArray[0]["xii_cgpa_max"] != "-1") {
                                    $("#xii_cgpa_max").val(
                                            xiiArray[0]["xii_cgpa_max"]);
                                }
                                if (xiiArray[0]["xii_percentage"] != "NA"
                                        && xiiArray[0]["xii_percentage"] != "null"
                                        && xiiArray[0]["xii_percentage"] != "-1") {
                                    $("#xii_percentage").val(
                                            xiiArray[0]["xii_percentage"]);
                                    ctrlxiiFormulaUpload(); // ChangeId: 2023112302
                                }
                            } else {
//                                document.getElementById("ifXiiPer").style.display = "block"; // PPEG HRD 
                                document.getElementById("ifXiiCgpa").style.display = "none";
                                if (xiiArray[0]["xii_percentage"] != "NA"
                                        && xiiArray[0]["xii_percentage"] != "null"
                                        && xiiArray[0]["xii_percentage"] != "-1") {
                                    $("#xii_per").val(
                                            xiiArray[0]["xii_percentage"]);
                                }
                            }
                        }
                        */
                        // Start: ChangeID: 2023122001
                        //if (xiiArray[0]["xii_percentage_cgpa"] != "NA"
                        //        && xiiArray[0]["xii_percentage_cgpa"] != "null"
                        //        && xiiArray[0]["xii_percentage_cgpa"] != "-1") {
                        //    $("#xii_percentage_cgpa").val(
                        //            xiiArray[0]["xii_percentage_cgpa"]);
                        // End: ChangeID: 2023122001
                            if (xiiArray[0]["xii_percentage_cgpa"] == "CGPA" 
                                    || xiiArray[0]["xii_percentage_cgpa"] == "NA") { // ChangeID: 2023122001
                                document.getElementById("ifXiiPer").style.display = "block"; // ChangeId 2023122001 none to block
                                document.getElementById("ifXiiCgpa").style.display = "block";
                                if (xiiArray[0]["xii_cgpa_obt"] != "NA"
                                        && xiiArray[0]["xii_cgpa_obt"] != "null"
                                        && xiiArray[0]["xii_cgpa_obt"] != "-1"
                                        && xiiArray[0]["xii_cgpa_obt"] != "0") {
                                    $("#xii_cgpa_obt").val(
                                            xiiArray[0]["xii_cgpa_obt"]);
                                }
                                if (xiiArray[0]["xii_cgpa_max"] != "NA"
                                        && xiiArray[0]["xii_cgpa_max"] != "null"
                                        && xiiArray[0]["xii_cgpa_max"] != "-1") {
                                    $("#xii_cgpa_max").val(
                                            xiiArray[0]["xii_cgpa_max"]);
                                    var cm = parseFloat(document.getElementById("xii_cgpa_max").value.trim());
                                    if(cm > 0.5 && cm < 9.5){
                                        $("#div_xii_cgpa_to_percentage").show();
                                        $("#div_xii_cgpa_to_percentage_formula").show();
                                    }
                                    else {
                                        $("#div_xii_cgpa_to_percentage").hide();
                                        $("#div_xii_cgpa_to_percentage_formula").hide();
                                    }
                                }
                                // Start: ChangeID: 2023122001
                                /*if (xiiArray[0]["xii_percentage"] != "NA"
                                        && xiiArray[0]["xii_percentage"] != "null"
                                        && xiiArray[0]["xii_percentage"] != "-1") {
                                    $("#xii_percentage").val(
                                            xiiArray[0]["xii_percentage"]);
                                    ctrlxiiFormulaUpload(); // ChangeId: 2023112302
                                }*/
                                if (xiiArray[0]["xii_percentage"] != "NA"
                                        && xiiArray[0]["xii_percentage"] != "null"
                                        && xiiArray[0]["xii_percentage"] != "-1") {
                                    $("#xii_perc").val(
                                            xiiArray[0]["xii_percentage"]);
                                    ctrlxiiFormulaUpload(); // ChangeId: 2023112302
                                }
                                if (xiiArray[0]["xii_cgpa_to_perc"] != "NA"
                                        && xiiArray[0]["xii_cgpa_to_perc"] != "null"
                                        && xiiArray[0]["xii_cgpa_to_perc"] != "-1") {
                                    $("#xii_percentage").val(
                                            xiiArray[0]["xii_cgpa_to_perc"]);
                                    ctrlxiiFormulaUpload(); // ChangeId: 2023112302
                                }
                                // End: ChangeID: 2023122001
                            } // else // ChangeID: 2023122001
                                if (xiiArray[0]["xii_percentage_cgpa"] == "Percentage"
                                        || xiiArray[0]["xii_percentage_cgpa"] == "NA") { // ChangeID: 2023122001) { // PPEG-HRD else to elseif Percentage
                                document.getElementById("ifXiiPer").style.display = "block";
                                document.getElementById("ifXiiCgpa").style.display = "block"; // ChangeId 2023122001 none to block
                                if (xiiArray[0]["xii_percentage"] != "NA"
                                        && xiiArray[0]["xii_percentage"] != "null"
                                        && xiiArray[0]["xii_percentage"] != "-1") {
                                    $("#xii_per").val(xiiArray[0]["xii_percentage"]);
                                }
                            
                            }
                            // Start: // ChangeID: 2023122001
                            /*else {
                                document.getElementById("ifXiiPer").style.display = "block"; // ChangeId 2023122001 none to block
                                document.getElementById("ifXiiCgpa").style.display = "block"; // ChangeId 2023122001 none to block
                            }*/
                            // End: // ChangeID: 2023122001
                        //} // ChangeID: 2023122001

                        
                        // Start: ChangeId: 2023111607
                        if(xiiArray[0]["marksheet"] != "NA" &&
                            xiiArray[0]["marksheet"]!="null" && 
                            xiiArray[0]["marksheet"]!="-1") {

                            if(xiiArray[0]["marksheet"]){
                                download(xiiArray[0]["marksheet"],null,null,"upload_xii_degree_certificate",readxii_degree_certificate);
                            }
                        }
                        // End: ChangeId: 2023111607
                    }

                    // if (exArray[0] !== undefined) {
                    // if (exArray[0]["experience"] != "NA"
                    // && exArray[0]["experience"] != "null"
                    // && exArray[0]["experience"] != "-1") {
                    // $("#experience").val(exArray[0]["experience"]);
                    // if (exArray[0]["experience"] == "Yes") {
                    // document.getElementById("ifExp").style.display = "block";
                    // $("#ex_table_body tr").remove();
                    // for (var i = 0; i < exArray.length; i++) {
                    // var table = document
                    // .getElementById("ex_table_body");
                    // var n = table.rows.length;
                    // var btnDel = document
                    // .createElement("button"); // Create
                    // btnDel.innerHTML = "Delete";
                    // btnDel.style.color = "#428bca";
                    // btnDel.id = "Del_" + i;
                    // btnDel.name = "Del_" + i;
                    // btnDel.className = "btn btn-secondary";
                    // btnDel.onclick = function() {
                    // deleteEX(this);
                    // };
                    //
                    // var tr = table.insertRow(-1); // TABLE
                    // // ROW.
                    // var tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["emp_name"];
                    // tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["exp_govt"];
                    // tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["emp_address"];
                    // tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["designation"];
                    // tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["designation"];
                    // tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["time_from"];
                    // tabCell = tr.insertCell(-1);
                    // tabCell.innerHTML = exArray[i]["time_to"];
                    // tabCell = tr.insertCell(-1);
                    // var tempPath = UPLOAD_DIR
                    // + "/"
                    // + document
                    // .getElementById("advt_no").value
                    // + "/"
                    // + document
                    // .getElementById("post_no").value
                    // + "/";
                    // var btn = document.createElement('a');
                    // btn.target = "_blank";
                    // btn.href = tempPath + exArray[i]["exp_cer"];
                    // btn.innerHTML = exArray[i]["exp_cer"];
                    // tabCell.appendChild(btn);
                    // tabCell = tr.insertCell(-1);
                    // tabCell.appendChild(btnDel);
                    // document.getElementById("tableExpAppDiv").style.display =
                    // "block";
                    // }
                    // } else {
                    // document.getElementById("ifExp").style.display = "none";
                    // }
                    // }
                    // }
                    
                    
                    
                    changeCategory();
                    changeEWS();
                    ExServiceCheck();
                    PWDCheck();
                }
            });
}
function changeGender() {
    var s = $("#salutation").val();
    $('#gender').empty();
    $('<option value="Select" selected disabled>Select</option>').appendTo(
            '#gender');
    if (s === "Mr.") {
        $('<option value="Male">Male</option>').appendTo('#gender');
    } else if ((s === "Mrs.") || (s === "Miss")) {
        $('<option value="Female">Female</option>').appendTo('#gender');
    } else {
        $('<option value="Male">Male</option>').appendTo('#gender');
        $('<option value="Female">Female</option>').appendTo('#gender');
    }
    $('<option value="Transgender">Transgender</option>').appendTo('#gender');
}

function changeDateRanges() {
    var dobDate = new Date(document.getElementById("dobAppl").value);
    var startYear = new Date(dobDate).getFullYear() + 1;
    var thisYear = new Date(today).getFullYear();
    // alert("dobDate : " + dobDate + ", " + dobYear + ", " + thisYear);
    document.getElementById("time_from").min = dobDate;
    document.getElementById("time_to").min = dobDate;
    document.getElementById("time_from").max = today;
    document.getElementById("time_to").max = today;
    document.getElementById("category_exservice_from").min = dobDate;
    document.getElementById("category_exservice_to").min = dobDate;
    document.getElementById("category_exservice_from").max = today;
    document.getElementById("category_exservice_to").max = today;
    // Start: ChangeId: 2023111101
    document.getElementById("cgov_serv_doj").min = dobDate;
    document.getElementById("cgov_serv_doj").max = today;
    // End: ChangeId: 2023111101
    $('#x_year_of_passing').empty();
    $('#xii_year_of_passing').empty();
    $('#iti_year_of_passing').empty();
    $('#dip_year_of_passing').empty();
    $('#ug_year_of_passing').empty();
    $('<option value="Select" disabled="disabled" selected>Select</option>')
            .appendTo('#x_year_of_passing');
    $('<option value="Select" disabled="disabled" selected>Select</option>')
            .appendTo('#xii_year_of_passing');
    $('<option value="Select" disabled="disabled" selected>Select</option>')
            .appendTo('#iti_year_of_passing');
    $('<option value="Select" disabled="disabled" selected>Select</option>')
            .appendTo('#dip_year_of_passing');
    $('<option value="Select" disabled="disabled" selected>Select</option>')
            .appendTo('#ug_year_of_passing');
    for (var j = startYear; j <= thisYear; j++) {
        $('<option value="' + j + '">' + j + '</option>').appendTo(
                '#x_year_of_passing');
        $('<option value="' + j + '">' + j + '</option>').appendTo(
                '#xii_year_of_passing');
        $('<option value="' + j + '">' + j + '</option>').appendTo(
                '#iti_year_of_passing');
        $('<option value="' + j + '">' + j + '</option>').appendTo(
                '#dip_year_of_passing');
        $('<option value="' + j + '">' + j + '</option>').appendTo(
                '#ug_year_of_passing');
    }
// document.getElementById("x_year_of_passing").value = startYear + 14;
// document.getElementById("xii_year_of_passing").value = startYear + 16;
// document.getElementById("iti_year_of_passing").value = startYear + 16;
// if (thisYear < (startYear + 20)) {
// document.getElementById("ug_year_of_passing").value = thisYear;
// } else {
// document.getElementById("ug_year_of_passing").value = startYear + 20;
// }

// document.getElementById("x_year_of_passing").min = dobYear;
// document.getElementById("x_year_of_passing").max = thisYear;
// document.getElementById("x_year_of_passing").value = dobYear;
// document.getElementById("xii_year_of_passing").min = dobYear;
// document.getElementById("xii_year_of_passing").max = thisYear;
// document.getElementById("xii_year_of_passing").value = dobYear;
// document.getElementById("iti_year_of_passing").min = dobYear;
// document.getElementById("iti_year_of_passing").max = thisYear;
// document.getElementById("iti_year_of_passing").value = dobYear;
// document.getElementById("ug_year_of_passing").min = dobYear;
// document.getElementById("ug_year_of_passing").max = thisYear;
// document.getElementById("ug_year_of_passing").value = dobYear;
}

function changeCategory() {
    $('#category_ews').empty();
    $('<option value="" disabled selected>Select</option>').appendTo(
            '#category_ews');
    $('<option value="No">No</option>').appendTo('#category_ews');
    if ($('#category').val() === "General") {
        $('<option value="Yes">Yes</option>').appendTo('#category_ews');
        $("#rcDiv").hide();
    } else {
        $("#rcDiv").show();
        $('#category_ews option[value="No"]').attr("selected", "selected"); // ChangeId: 2023121602
    }

    var origFileName = document.getElementById("upload_rc").name;
    if (origFileName !== "NA") {
        var article = new Object();
        article.action = "deletePrevUpload";
        article.email = email;
        article.advtNo = $("#advt_no").val();
        article.postNo = $("#post_no").val();
        article.fieldName = "rc"
        article.filename = origFileName;
        article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
        $.ajax({
            beforeSend: function (xhr) {
                xhr.setRequestHeader('user', encodeURIComponent(email));
            },
            url: "/eRecruitment_NRSC/LoadAdvtNos",
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(article),
            contentType: 'application/json',
            mimeType: 'application/json',
            error: function (data, status, er) {
                alert("Problem in deleting previous certificate");
            },
            success: function (data) {
                $(document.body).css({
                    'cursor': 'default'
                });
                //console.log("data.Results : " + data.Results);
            }
        });
    }
    document.getElementById("upload_rc").name = "NA";
    document.getElementById("upload_rc").value = "";
    document.getElementById("rc_size").innerHTML = "";
}

// function takeCert(input) {
// var flag = $(input).is(":checked");
//
// var thisId = $(input).attr('id').toString();
// if (flag) {
// alert("Upload certificate using the 'Upload Certificate' button");
// // set the nes_ct button name to this index and examination
// var thisName = $(input).val().toString();
// thisName = thisName.substring(0, thisName.indexOf("("));
// thisId = thisId.replace("nes_cb", "");
// document.getElementById("nes_ct").name = thisId + "_" + thisName;
// } else {
// // un-set the nes_ct button name
// document.getElementById("nes_ct").name = "";
//
// // un-set the file name in the display
// thisId = thisId.replace("nes_cb", "nes_ct");
// document.getElementById(thisId).value = "NA";
// }
// }

// function read_neCert(input) {
// // Set the fileType from button name
// var str = $(input).attr('name').toString();
// if (str == "") {
// alert("Please select the Examination for which you want to upload the
// certificate.");
// return;
// }
// document.getElementById("filetype").value = "nes_" + str.split("_")[1];
//
// // Set the file name in the display
// var fileName = $(input).val().split("\\").pop();
// var id = "nes_ct" + str.split("_")[0];
// document.getElementById(id).value = fileName;
// }

function changeEWS() {
    if ($('#category_ews').val() === "Yes") {
        $("#ewsDiv").show();
    } else {
        $("#ewsDiv").hide();
    }
}

function applicantStatusTable() {
    document.getElementById("cur_openings-tab").style.backgroundColor = "#428bca";
    $('#tableParentDivApplicant').hide();
    advertisementDetails = new Array();
    var article = new Object();
    article.email = email;
    article.user_id = email;
    // article.action = "applicationStatus";
    article.action = "ApplicantAppliedPost";
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadAdvtNos",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in Application status table. Please check again or contact website administrator");
                },
                success: function (data) {
                    // document.getElementById("cur_openings-tab").style.display
                    // =
                    // "block";
                    if (data.Results.length === 0) {
                        $('#app_status').hide();
                        $('#messageDiv').show();
                        document.getElementById("messageDiv").innerHTML = "No applications available.";
                        // alert("No posts open right now. Please check back
                        // after some time.");
                        return;
                    }
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    $('#app_status').show();
                    $('#messageDiv').hide();
                    var tableData = data.Results;
                    // advertisementDetails = data.Results;

                    var col = [];
                    for (var i = 0; i < tableData.length; i++) {
                        for (var key in tableData[i]) {
                            if (col.indexOf(key) === -1) {
                                col.push(key);
                            }
                        }
                    }

                    var tableData = data.Results;
                    // alert("1 : " + tableData.length);
                    advertisementDetails = data.Results;
                    var col = [];
                    for (var i = 0; i < tableData.length; i++) {
                        for (var key in tableData[i]) {
                            if (col.indexOf(key) === -1) {
                                col.push(key);
                            }
                        }
                    }
                    // col.push("Apply");
                    var tempPath = UPLOAD_DIR.split("applicant")[0] + "/admin/";
                    var table = document.createElement("table");
                    table.setAttribute("id", "application_status_table");
                    var tr = table.insertRow(-1); // TABLE ROW.

                    // for (var i = 0; i < col.length; i++) {
                    // TABLE HEADER.
                    var th = document.createElement("th");
                    th.setAttribute("style", "width: 5%;");
                    th.innerHTML = "Sl No.";
                    tr.appendChild(th);
                    th = document.createElement("th");
                    th.setAttribute("style", "width: 20%;");
                    th.innerHTML = "Advertisement No.";
                    tr.appendChild(th);
                    var th1 = document.createElement("th");
                    th1.setAttribute("style", "width: 5%;");
                    th1.innerHTML = "Post No.";
                    tr.appendChild(th1);
                    var th2 = document.createElement("th");
                    th2.setAttribute("style", "width: 20%;");
                    th2.innerHTML = "Post Name";
                    tr.appendChild(th2);
                    var th7 = document.createElement("th");
                    th7.setAttribute("style", "width: 30%;");
                    th7.innerHTML = "Registration ID / Date";
                    tr.appendChild(th7);
                    var th7 = document.createElement("th");
                    th7.innerHTML = "Status";
                    tr.appendChild(th7);
                    for (var i = 0; i < tableData.length; i++) {
                        tr = table.insertRow(-1);
                        var eArray = tableData[i]["Eligibility"];
                        var qStr = "";
                        // var eStr = "";
                        // var dStr = "";
                        var path = tempPath + tableData[i]["advt_no"] + ".pdf";
                        // for (var j = 0; j < col.length; j++) {
                        var tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = (i + 1);
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["advt_no"];
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["post_no"];
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["post_name"];
                        tabCell = tr.insertCell(-1);
                        var reg_id = tableData[i]["Reg_Id"];
                        if (reg_id === "-") {
                            tabCell.innerHTML = reg_id;
                        } else {
                            // Start: ChangeId: 2023111401 Zip download link replaced with preview link 
                            var btnR = document.createElement('a');
                            function downloadFile() {
                                var idx = this.id.replace("previewlink","");
                                var filename = tableData[idx]["Preview_File"].replace(/^.*[\\/]/, '');
                                var advt_no = tableData[idx]["advt_no"];
                                var post_no = tableData[idx]["post_no"];
                                
                                download(filename,advt_no,post_no);
                            }
                            btnR.href = "#";
                            btnR.onclick = downloadFile;
                            btnR.id = "previewlink"+i;
                            btnR.innerHTML = tableData[i]["Reg_Id"] + " / "
                                    + tableData[i]["Applied_Date"];
                            tabCell.appendChild(btnR);
                            //var btnR = document.createElement('a');
                            //// btnR.href = tableData[i]["Preview_File"];
                            //var zipPath_admin = tempPath
                            //        + tableData[i]["advt_no"] + "/"
                            //        + tableData[i]["post_no"] + "/"
                            //        + tableData[i]["Reg_Id"] + ".zip";
                            //btnR.href = zipPath_admin;
                            //btnR.target = "_blank";
                            //btnR.innerHTML = tableData[i]["Reg_Id"] + " / "
                            //        + tableData[i]["Applied_Date"];
                            //tabCell.appendChild(btnR);
                            //tabCell.innerHTML = tableData[i]["Reg_Id"] + " / "
                                    //+ tableData[i]["Applied_Date"];
                            // End: ChangeId: 2023111401 Zip download link replaced with preview link 
                        }
                        tabCell = tr.insertCell(-1);
                        tabCell.innerHTML = tableData[i]["status"];
                        tr.setAttribute('id', i);
                    }

                    var divContainer = document
                            .getElementById("tableApplicationStatus");
                    divContainer.innerHTML = "";
                    divContainer.appendChild(table);
                }
            });
}

function loadDistrictOptions(str) {
    var option = $("#state").val();
    // $("#district").load("LoadDistricts?state=" + option);

    var article = new Object();
    article.state = option;
    article.action = "district";
    $('#district').empty();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadDistricts",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in loading Districts. Please check again or contact website administrator");
                },
                success: function (data) {
                    $(document.body).css({
                        'cursor': 'default'
                    });
                    //console.log("data.Results : " + data.Results);
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    $(
                            '<option disabled="disabled" value="Select District" selected>Select District</option>')
                            .appendTo('#district');
                    for (var i = 0; i < data.Results.length; ++i) {
                        var obj = data.Results[i];
                        $('<option value="' + obj + '">' + obj + '</option>')
                                .appendTo('#district');
                    }
                    if (str != "NA" && str != "null" && str != "-1") {
                        $("#district").val(str);
                    }
                }
            });
}

function loadPDistrictOptions(str) {
    var option1 = $("#p_state").val();
    //alert("In loadPDistrict : " + $("#p_state").val());

    if (option1 === "") {
        return;
    }
    var article = new Object();
    article.state = option1;
    article.action = "district";
    $('#p_district').empty();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadDistricts",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in loading permanent districts. Please check again or contact website administrator");
                },
                success: function (data) {
                    $(document.body).css({
                        'cursor': 'default'
                    });
                    // console.log("data.Results : " + data.Results);
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    // console.log("data.Results : " + data.Results);

                    $('<option disabled="disabled" value="Select District" >Select District</option>').appendTo('#p_district');
                    for (var i = 0; i < data.Results.length; ++i) {
                        var obj = data.Results[i];
                        $('<option value="' + obj + '">' + obj + '</option>')
                                .appendTo('#p_district');
                    }
                    
                    if ($("#sameaddressconfirmation").prop("checked")) {
                        document.getElementById("p_district").selectedIndex = document
                                .getElementById("district").selectedIndex;
                    }
                    if (str != "NA" && str != "null" && str != "-1") {
                        $("#p_district").val(str);
                    }

                }
            });
}

function loadState() {
    //alert("Called loadState");
    var article = new Object();
    article.action = "state";
    $('#state').empty();
    $('#p_state').empty();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadStates",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in loading states. Please check again or contact website administrator");
                },
                success: function (data) {
                    $(document.body).css({
                        'cursor': 'default'
                    })
                    // console.log("data.Results : " + data.Results);
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    $(
                            '<option disabled="disabled" value="" selected>State/UT</option>')
                            .appendTo('#state');
                    $(
                            '<option disabled="disabled" value="" selected>State/UT</option>')
                            .appendTo('#p_state');
                    for (var i = 0; i < data.Results.length; ++i) {
                        var obj = data.Results[i];
                        $('<option value="' + obj + '">' + obj + '</option>')
                                .appendTo('#state');
                        $('<option value="' + obj + '">' + obj + '</option>')
                                .appendTo('#p_state');
                    }
                }
            });
}

function loadBoard(str, str1) {
    var article = new Object();
    $('#x_edu_board').empty();
    $('#xii_edu_board').empty();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadEducationBoards",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in loading board. Please check again or contact website administrator");
                },
                success: function (data) {
                    $(document.body).css({
                        'cursor': 'default'
                    })
                    // console.log("data.Results : " + data.Results);
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    $(
                            '<option disabled="disabled" value="" selected>Select</option>')
                            .appendTo('#x_edu_board');
                    $(
                            '<option disabled="disabled" value="" selected>Select</option>')
                            .appendTo('#xii_edu_board');
                    for (var i = 0; i < data.Results.length; ++i) {
                        var obj = data.Results[i].trim();
                        if (obj === str) {
                            $(
                                    '<option value="' + obj + '" selected>'
                                    + obj + '</option>').appendTo(
                                    '#x_edu_board');
                        } else {
                            $(
                                    '<option value="' + obj + '">' + obj
                                    + '</option>').appendTo(
                                    '#x_edu_board');
                        }
                        if (obj === str1) {
                            $(
                                    '<option value="' + obj + '" selected>'
                                    + obj + '</option>').appendTo(
                                    '#xii_edu_board');
                        } else {
                            $(
                                    '<option value="' + obj + '">' + obj
                                    + '</option>').appendTo(
                                    '#xii_edu_board');
                        }
                    }
                    // Start: ChangeId: 2023121401
                    $(
                            '<option value="Others">Others</option>')
                            .appendTo('#x_edu_board');
                    $(
                            '<option value="Others">Others</option>')
                            .appendTo('#xii_edu_board');
                    // End: ChangeId: 2023121401
                }
            });
}

function loadUni() {
    var dataList = document.getElementById('univlist');
    var input = document.getElementById('ug_university');
    var article = new Object();
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadUniversity",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Problem in loading University. Please check again or contact website administrator");
                    input.placeholder = "Couldn't load university list :(";
                },
                success: function (data) {
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    var dataArray = data.Results;
                    // alert("Uni list :" + data.Results.length);
                    // console.log(data.Results);
                    $(
                            '<option value="Select" disabled selected>Select</option>')
                            .appendTo('#ug_university');
                    for (var i = 0; i < data.Results.length; ++i) {
                        var obj = data.Results[i];
                        $('<option value="' + obj + '">' + obj + '</option>')
                                .appendTo('#ug_university');
                    }
                    $('<option value="Others">Others</option>').appendTo(
                            '#ug_university');
                    //
                    // for (var i = 0; i < data.Results.length; ++i) {
                    //
                    // var str = data.Results[i];
                    // var option = document.createElement('option');
                    // option.text = str;
                    // option.value = str;
                    // dataList.appendChild(option);
                    // }
                }
            });
}

function validatePersonalData() {
// alert("Personal data validation method called");

    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        return false;
    }

    document.getElementById("salutation").style.borderColor = "#ccc";
    document.getElementById("first_name").style.borderColor = "#ccc";
    document.getElementById("father_name").style.borderColor = "#ccc";
    document.getElementById("mother_name").style.borderColor = "#ccc";
    document.getElementById("gender").style.borderColor = "#ccc";
    document.getElementById("marital_status").style.borderColor = "#ccc";
    document.getElementById("dobAppl").style.borderColor = "#ccc";
    document.getElementById("place_of_birth").style.borderColor = "#ccc";
    document.getElementById("nationality").style.borderColor = "#ccc";
    document.getElementById("category").style.borderColor = "#ccc";
    document.getElementById("category_pwd").style.borderColor = "#ccc";
    document.getElementById("category_pwd_scribe").style.borderColor = "#ccc";  /*ChangeId:2025041501*/
    document.getElementById("category_pwd_comptime").style.borderColor = "#ccc";  /*ChangeId:2025050807*/
    document.getElementById("category_ews").style.borderColor = "#ccc";
    document.getElementById("category_spt").style.borderColor = "#ccc";
    document.getElementById("category_ser").style.borderColor = "#ccc";
    document.getElementById("category_exservice_from").style.borderColor = "#ccc";
    document.getElementById("cgov_serv").style.borderColor = "#ccc"; // ChangeId: 2023111101
    document.getElementById("cgov_serv_doj").style.borderColor = "#ccc"; // ChangeId: 2023111101
    document.getElementById("category_exservice_to").style.borderColor = "#ccc";
    document.getElementById("category_merit_sportname").style.borderColor = "#ccc";
    document.getElementById("category_merit_sportlevel").style.borderColor = "#ccc";
    document.getElementById("house_no").style.borderColor = "#ccc";
    document.getElementById("locality").style.borderColor = "#ccc";
    document.getElementById("state").style.borderColor = "#ccc";
    document.getElementById("district").style.borderColor = "#ccc";
    document.getElementById("town").style.borderColor = "#ccc";
    document.getElementById("pincode").style.borderColor = "#ccc";
    document.getElementById("p_house_no").style.borderColor = "#ccc";
    document.getElementById("p_locality").style.borderColor = "#ccc";
    document.getElementById("p_state").style.borderColor = "#ccc";
    document.getElementById("p_district").style.borderColor = "#ccc";
    document.getElementById("p_town").style.borderColor = "#ccc";
    document.getElementById("p_pincode").style.borderColor = "#ccc";
    document.getElementById("contact_no").style.borderColor = "#ccc";
    document.getElementById("aadhaar").style.borderColor = "#ccc"; // PPEG-HRD AADHAAR
    document.getElementById("nearest_railway_station").style.borderColor = "#ccc";
    document.getElementById("bank_acc_beneficiary").style.borderColor = "#ccc"; //ChangeId:2025041601
    document.getElementById("bank_acc_no").style.borderColor = "#ccc"; //ChangeId: 2023111001
    document.getElementById("bank_ifsc_code").style.borderColor = "#ccc"; //ChangeId: 2023111001
    document.getElementById("upload_photograph").style.borderColor = "#ccc";
    document.getElementById("upload_signature").style.borderColor = "#ccc";
    document.getElementById("upload_photograph").style.borderColor = "#ccc";
    document.getElementById("upload_signature").style.borderColor = "#ccc";
    if (document.getElementById("salutation").value.length == 0) {
        alert("Salutation should not be left blank.");
        document.getElementById("salutation").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("salutation").value == "Select") {
        alert("Salutation should not be left blank.");
        document.getElementById("salutation").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("first_name").value.length == 0) {
        alert("First Name should not be left blank.");
        document.getElementById("first_name").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("father_name").value.length == 0) {
        alert("Father's Name should not be left blank.");
        document.getElementById("father_name").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("mother_name").value.length == 0) {
        alert("Mother's Name should not be left blank.");
        document.getElementById("mother_name").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("gender").value.length == 0) {
        alert("Gender should not be left blank.");
        document.getElementById("gender").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("gender").value == "Select") {
        alert("Please select the gender");
        document.getElementById("gender").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("marital_status").value.length == 0) {
        alert("Marital Status should not be left blank.");
        document.getElementById("marital_status").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("marital_status").value == "Select") {
        alert("Marital Status should not be left blank.");
        document.getElementById("marital_status").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("dobAppl").value.length == 0) {
        alert("Date of Birth(DOB) should not be left blank.");
        document.getElementById("dobAppl").style.borderColor = "red";
        return false;
    }
    
    if (document.getElementById("place_of_birth").value.length == 0) {
        alert("Place of birth should not be left blank.");
        document.getElementById("place_of_birth").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("nationality").value.length == 0) {
        alert("Nationality should not be left blank.");
        document.getElementById("nationality").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("category").value == "Select") {
        alert("Please select a Category");
        document.getElementById("category").style.borderColor = "red";
        return false;
    } else if (document.getElementById("category").value.length == 0) {
        alert("Please select a Category");
        document.getElementById("category").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("category").value !== "General") {
        if ((document.getElementById("upload_rc").name == "NA")
                || (document.getElementById("upload_rc").name == "")) {
            alert("Please upload Certificate in proof of reservation");
            document.getElementById("upload_rc").style.borderColor = "red";
            return false;
        }
    }

    if (document.getElementById("category_pwd").value.length == 0) {
        alert("Please select one of the options for 'Person with Benchmark Disabilities'");
        document.getElementById("category_pwd").style.borderColor = "red";
        return false;
    }
    
    

    if (document.getElementById("category_pwd").value !== "No") {
        if ((document.getElementById("upload_disability").name == "NA")
                || (document.getElementById("upload_disability").name == "")) {
            alert("Please upload Disability Certiticate");
            document.getElementById("upload_disability").style.borderColor = "red";
            return false;
        }
        /*Start: ChangeId:2025041501*/
        if ((document.getElementById("category_pwd_scribe").value == "NA")
                || (document.getElementById("category_pwd_scribe").value == "")) {
            alert("Please select whether you need Scribe or not.");
            document.getElementById("category_pwd_scribe").style.borderColor = "red";
            return false;
        }
        /*End: ChangeId:2025041501*/
        
        /*Start: ChangeId:2025050807*/
        if ((document.getElementById("category_pwd_comptime").value == "NA")
                || (document.getElementById("category_pwd_comptime").value == "")) {
            alert("Please select whether you need Compensatory Time or not.");
            document.getElementById("category_pwd_comptime").style.borderColor = "red";
            return false;
        }
        /*End: ChangeId:2025050807*/
    }

    if (document.getElementById("category_ews").value.length == 0) {
        alert("Please select one of the options for 'EWS'");
        document.getElementById("category_ews").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("category_ews").value !== "No") {
        if ((document.getElementById("upload_ews").name == "NA")
                || (document.getElementById("upload_ews").name == "")) {
            alert("Please upload EWS Certiticate");
            document.getElementById("upload_ews").style.borderColor = "red";
            return false;
        }
    }

    if (document.getElementById("category_ser").value.length == 0) {
        alert("Please select one of the options for 'Ex-servicemen'");
        document.getElementById("category_ser").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("category_ser").value === 'Yes') {
        if (document.getElementById("category_exservice_from").value.length == 0) {
            alert("ExService From is empty");
            document.getElementById("category_exservice_from").style.borderColor = "red";
            return false;
        } else if (document.getElementById("category_exservice_to").value.length == 0) {
            alert("ExService To is empty");
            document.getElementById("category_exservice_to").style.borderColor = "red";
            return false;
        }
        if ((document.getElementById("upload_serviceman").name == "NA")
                || (document.getElementById("upload_serviceman").name == "")) {
            alert("Please upload Ex-serviceman Proof");
            document.getElementById("upload_serviceman").style.borderColor = "red";
            return false;
        }
    }
    
    // Start: ChangeId:2023111101
    if (document.getElementById("cgov_serv").value === 'Yes') {
        if (document.getElementById("cgov_serv_doj").value.length === 0) {
            alert("Central Government Date of Joining is empty");
            document.getElementById("cgov_serv_doj").style.borderColor = "red";
            return false;
        } 
    }
    // End: ChangeId:2023111101

    if (document.getElementById("category_spt").value.length == 0) {
        alert("Please select one of the options for 'Sports Merit'");
        document.getElementById("category_spt").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("category_spt").value === 'Yes') {
        if (document.getElementById("category_merit_sportname").value.length == 0) {
            alert("Sport Name should not be left blank");
            document.getElementById("category_merit_sportname").style.borderColor = "red";
            return false;
        } else if (document.getElementById("category_merit_sportlevel").value.length == 0) {
            alert("Sport Level should not be left blank");
            document.getElementById("category_merit_sportlevel").style.borderColor = "red";
            return false;
        }
    }
    // Start: ChangeId: 2023111102
    var ageValidationResult = validateAge(false); // ChangeId: 2024011001 checkQualification false
    if (ageValidationResult !== "Ok")
    {
        alert(ageValidationResult);
        return false;
    }
    // End: ChangeId: 2023111102
    if (document.getElementById("house_no").value.length == 0) {
        alert("House No. in present address is empty");
        document.getElementById("house_no").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("locality").value.length == 0) {
        alert("Locality in present address is empty");
        document.getElementById("locality").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("state").value.length == 0) {
        alert("State in present address is empty");
        document.getElementById("state").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("district").value.length == 0) {
        alert("District in present address is empty");
        document.getElementById("district").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("district").value == "Select District") {
        alert("Please select district in present address");
        document.getElementById("district").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("town").value.length == 0) {
        alert("Town in present address is empty");
        document.getElementById("town").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("pincode").value.length == 0) {
        alert("Pincode in present address is empty");
        document.getElementById("pincode").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_house_no").value.length == 0) {
        alert("House No. in permanent adddress is empty");
        document.getElementById("p_house_no").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_locality").value.length == 0) {
        alert("Locality in permanent adddress is empty");
        document.getElementById("p_locality").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_state").value.length == 0) {
        alert("State in permanent adddress is empty");
        document.getElementById("p_state").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_district").value.length == 0) {
        alert("District in permanent adddress is empty");
        document.getElementById("p_district").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_district").value == "Select District") {
        alert("Please select district in permanent address");
        document.getElementById("p_district").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_town").value.length == 0) {
        alert("Town in permanent adddress is empty");
        document.getElementById("p_town").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("p_pincode").value.length == 0) {
        alert("Pincode in permanent adddress is empty");
        document.getElementById("p_pincode").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("contact_no").value.length == 0) {
        alert("Contact Number is left blank");
        document.getElementById("contact_no").style.borderColor = "red";
        return false;
    }
    // PPEG-HRD: Start AADHAAR
    if (document.getElementById("aadhaar").value.length !== 12) { // ChangeId: 2023122703
        alert("Aadhaar Number is invalid");
        document.getElementById("aadhaar").style.borderColor = "red";
        return false;
    }
    // PPEG-HRD: End AADHAAR
    if (document.getElementById("nearest_railway_station").value.length == 0) {
        alert("Nearest Railway Station is left blank");
        document.getElementById("nearest_railway_station").style.borderColor = "red";
        return false;
    }
    
    
    if ($('#zoneDiv').is(':visible')) {
        if ( $("#zone_sel").val().length !== 3 ) { // ChangeId: 2023112001
            alert("Please select three exam centre location preferences"); // ChangeId: 2023112001
            document.getElementById("zone_sel").style.borderColor = "red";
            return false;
        }
    }

    if ((document.getElementById("upload_photograph").name == "NA")
            || (document.getElementById("upload_photograph").name == "")) {
        alert("Please upload photograph");
        document.getElementById("upload_photograph").style.borderColor = "red";
        return false;
    }
    if ((document.getElementById("upload_signature").name == "NA")
            || (document.getElementById("upload_signature").name == "")) {
        alert("Please upload signature");
        document.getElementById("upload_signature").style.borderColor = "red";
        return false;
    }

    // Start: ChangeId: 2023111001
    if (document.getElementById("bank_acc_no").value.length == 0) {
        alert("Bank account no is left blank");
        document.getElementById("bank_acc_no").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("bank_ifsc_code").value.length == 0) {
        alert("Bank IFSC code is left blank");
        document.getElementById("bank_ifsc_code").style.borderColor = "red";
        return false;
    }
    // End: ChangeId: 2023111001
    
    //Start: ChangeId:2025041601
    if (document.getElementById("bank_acc_beneficiary").value.length == 0) {
        alert("Bank Account Beneficiary Name left blank");
        document.getElementById("bank_acc_beneficiary").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("bank_acc_no_confirm").value.length == 0) {
        alert("Bank Account No (confirm) is left blank");
        document.getElementById("bank_acc_no_confirm").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("bank_acc_no_confirm").value != document.getElementById("bank_acc_no").value) {
        alert("'Bank Account No' and 'Bank Account No (confirm)' values do not match");
        document.getElementById("bank_ifsc_code_confirm").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("bank_ifsc_code_confirm").value.length == 0) {
        alert("Bank IFSC code (confirm) is left blank");
        document.getElementById("bank_ifsc_code_confirm").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("bank_ifsc_code_confirm").value != document.getElementById("bank_ifsc_code").value) {
        alert("'Bank IFSC Code' and 'Bank IFSC Code (confirm)' values do not match");
        document.getElementById("bank_ifsc_code_confirm").style.borderColor = "red";
        return false;
    }
    if ((document.getElementById("bank_acc_doc").name == "NA")
            || (document.getElementById("bank_acc_doc").name == "")) {
        alert("Please upload bank account document.");
        return;
    }
    //End: ChangeId:2025041601
    
    var tabIndex = document.getElementById("indexval").value;
    var arr = advertisementDetails[tabIndex]["Eligibility"];
    var eligibility = new Array();
    for (var j = 0; j < arr.length; j++) {
        eligibility[j] = arr[j]["eligibility"];
    }

// var eligibility = advertisementDetails[Index]["Academic
// Eligibility"].split("|");
    document.getElementById("applicant_eligibility").value = eligibility;
    return true;
}

function checkEligibility() {
    //alert("Checking eligibility");
    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        return false;
    }

    var flag = false;
    document.getElementById("doneXII").style.borderColor = "#ccc";
    document.getElementById("doneITI").style.borderColor = "#ccc";
    document.getElementById("doneDIP").style.borderColor = "#ccc";
    //Nurse and doc here
    document.getElementById("nurse_reg_no").style.borderColor = "#ccc";
    document.getElementById("nurse_council").style.borderColor = "#ccc";
    document.getElementById("nurse_reg_date").style.borderColor = "#ccc";
    document.getElementById("nurse_valid_date").style.borderColor = "#ccc";
    document.getElementById("doc_reg_no").style.borderColor = "#ccc";
    document.getElementById("doc_council").style.borderColor = "#ccc"; // ChangeId:2023090402
    document.getElementById("doc_reg_date").style.borderColor = "#ccc";
    document.getElementById("doc_valid_date").style.borderColor = "#ccc";
    //Nurse and doc ends here
    var tabIndex = document.getElementById("indexval").value;
    var thisYear = new Date(today).getFullYear();
    var arr = advertisementDetails[tabIndex]["Eligibility"];
    var eligibility = new Array();
    var mandatory = new Array();
    var qual = new Array();
    var spec = new Array();
    for (var j = 0; j < arr.length; j++) {
        eligibility[j] = arr[j]["eligibility"];
        mandatory[j] = arr[j]["mandatory"];
        qual[j] = arr[j]["essential_qualification"];
        spec[j] = arr[j]["specialization"];
    }

    var eAdded = "";
    var bscSpec = "";
    var mscSpec = "";
    var table = document.getElementById("he_table_body");
    var n = table.rows.length;
    for (var r = 0; r < n; r++) {
        eAdded += table.rows[r].cells[0].innerHTML + ",";
        if (table.rows[r].cells[0].innerHTML === "B.Sc") {
            bscSpec = table.rows[r].cells[2].innerHTML;
        }
        if (table.rows[r].cells[0].innerHTML === "M.Sc") {
            mscSpec = table.rows[r].cells[2].innerHTML;
        }
    }
    
    // Start: ChangeId: 2023121801
    if(
            eAdded.split(",").includes("B.Tech/B.E") 
            || eAdded.split(",").includes("B.Arch") 
        ){
        // Reset mandatory for All Masters
        for(var zz = 0; zz < arr.length; zz++){
            if(
                    qual[zz] === "B.Tech/B.E" 
                    || qual[zz] === "B.Arch" 
                ){
                mandatory[zz] = false;
            }
        }
        //console.log(mandatory);
    }
    // End: ChangeId: 2023121801

    // Start: ChangeId: 2023112301
    if(
            eAdded.split(",").includes("M.Sc") 
            || eAdded.split(",").includes("MSc-Tech") 
            || eAdded.split(",").includes("M.Tech") 
            || eAdded.split(",").includes("M.Tech/M.E") 
        ){
        // Reset mandatory for All Masters
        for(var zz = 0; zz < arr.length; zz++){
            if(
                    qual[zz] === "M.Sc" 
                    || qual[zz] === "MSc-Tech" 
                    || qual[zz] === "M.Tech"
                    || qual[zz] === "M.Tech/M.E"
                ){
                mandatory[zz] = false;
            }
        }
        //console.log(mandatory);
    }
    // End: ChangeId: 2023112301
    // 
//alert("El : " + eligibility + "Mn : " + mandatory + "Ql : " + qual + "Added : " + eAdded);

    var doneXIIval = $("#doneXII").val();
    var doneITIval = $("#doneITI").val();
    var doneDIPval = $("#doneDIP").val();

    for (var i = 0; i < eligibility.length; i++) {

        if (eligibility[i] == 'X') {
            var thisRes = validateX(thisYear);
            if (!thisRes) {
                return thisRes;
            }
            eAdded += "X,";
        }

        if (eligibility[i] == 'XII') {
            if ((mandatory[i] == "Yes") && (doneXIIval != "Yes")) {
                document.getElementById("doneXII").style.borderColor = "#red";
                alert("Please specify senior secondary(12/XII) education details");
                return false;
            }

            if (doneXIIval == "Yes") {
                var thisRes = validateXII(thisYear);
                if (!thisRes) {
                    return thisRes;
                }
                eAdded += "XII,";
            }
        }

        if (eligibility[i] == 'ITI') {
            if ((mandatory[i] == "Yes") && (doneITIval != "Yes")) {
                document.getElementById("doneITI").style.borderColor = "#red";
                alert("Please specify ITI details");
                return false;
            }

            if (doneITIval == "Yes") {
                var thisRes = validateITI(thisYear);
                if (!thisRes) {
                    return thisRes;
                }
                eAdded += "ITI,";
            }
        }

        if (eligibility[i].toUpperCase() == 'DIPLOMA') {
            if ((mandatory[i] == "Yes") && (doneDIPval != "Yes")) {
                document.getElementById("doneDIP").style.borderColor = "#red";
                alert("Please specify Diploma education details");
                return false;
            }

            if (doneDIPval == "Yes") {
                var thisRes = validateDIP(thisYear);
                if (!thisRes) {
                    return thisRes;
                }
                eAdded += "Diploma,";
            }
        }
        
        if (qual[i] == "B.Sc") {
            if ((doneXIIval != "Yes") && (doneITIval != "Yes")
                    && (doneDIPval != "Yes")) {
                alert("Please fill either XII or ITI or Diploma details");
                return false;
            }
        }

        if (qual[i] == "B.Tech/B.E") {
            if ((doneXIIval != "Yes") && (doneDIPval != "Yes")) {
                alert("Please fill either XII or Diploma details");
                return false;
            }
        }

        if (mandatory[i] == "Yes") {
            if (eAdded.indexOf(qual[i]) == -1) {
                alert("Please specify " + qual[i] + " or equivalent details"); // ChangeId: 2023112301 "or equivalent" added
                return;
            }
        }

        //      Check this condition
//        if (((qual[i] == "M.Tech/M.E") && (mandatory[i] == "Yes")) || ((qual[i] == "M.Tech") && (mandatory[i] == "Yes"))) {
//            if ((eAdded.indexOf("B.Sc") > -1) && (eAdded.indexOf("M.Sc") < 0)) {
//                alert("Please specify M.Sc details.");
//                return;
//            }
//        }

//        console.log("I : " + i + ", eligibility[i] : " + eligibility[i] + ", qual[i] : " + qual[i] + ", spec[i] : " + spec[i]);

//        if ((eAdded.includes("B.Sc") && !bscSpec.includes("4 years")) //B.Sc 3 yr course
//                && !(eAdded.includes("M.Sc") || eAdded.includes("MSc-Tech"))) {
//            alert("As you have specified B.Sc details, please add M.Sc details in the Higher Education details by selecting the qualification, filling the details and clicking the 'Add and Save' button.");
//            return false;
//        }
        // Start: ChangeId: 2023122103
        /*if ((eAdded.includes("M.Sc") || eAdded.includes("MSc-Tech")) && !eAdded.includes("B.Sc")) {
            alert("As you have specified M.Sc details, please add B.Sc details in the Higher Education details by selecting the qualification, filling the details and clicking the 'Add and Save' button");
            return false;
        }*/
        // End: ChangeId: 2023122103

//        if ((($("#post_no").val().toString() === "JRF6") || ($("#post_no").val().toString() === "JRF06"))
//                && (eAdded.includes("M.Sc") && (mscSpec === "Physics")) && !eAdded.includes("M.Tech")) {
//            alert("Please specify M.Tech details.");
//            return false;
//        }

//            if (qual.includes("B.Tech/B.E") && eligibility.includes("Postgraduate") && // applicant should have minimum BTech equivalent
//                eAdded.includes("B.Sc") && !eAdded.includes("M.Sc")) { // applicant entered B.Sc but did not enter M.Sc details
//                alert("Please add M.Sc details in the Higher Education details by selecting the qualification, filling the details and clicking the 'Add and Save' button");
//                return false;
//            }

        if (eligibility[i] === 'Graduate' || eligibility[i] === 'Postgraduate'
                || eligibility[i] === 'PhD' || eligibility[i] === 'PostDoctoral') {
            var list = eligList[eligibility[i]];
            //list[k] : B.Sc
            //list[k] : B.Tech/B.E
            //list[k] : M.Sc

            var added = false;
            for (var k = 0; k < list.length; k++) {
                if (eAdded.indexOf(list[k]) !== -1) {
                    added = true;
                }
                if ((eligibility[i] === 'Postgraduate') && !(list.includes("M.Tech") || list.includes("M.Tech/M.E")) && eAdded.includes("B.Tech")) {
                    added = true;
                }
            }

            if (!added && mandatory[i] === "Yes") { // ChangeId: 2023111403 applicant did not add mandatory higher education details
            //if (!added) { // ChangeId: 2023111403
                alert("Please add " + eligibility[i]
                        + " details in the Higher Education details by selecting the qualification, filling the details and clicking the 'Add and Save' button");
                return false;
            }
        }
    }


    if ($("#nesDiv").is(":visible")) {
        var nesVal = $("#netSel").val();
        if ((nesVal == null) || (nesVal == "") || (nesVal == "Select")) {
            alert("Please select any one National Examination");
            return false;
        }
        if (nesVal.indexOf("Other") > -1) {
            nesVal = $("#nes_sco").val();
            if (nesVal == "") {
                alert("Please specify the other national examination");
                return false;
            }
        }
        var c = $("#nes_ct").val();
        if (c == "") {
            alert("Please upload the certificate for national examination");
            return false;
        }
    }

    //Nurse and doc here
    if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Nurse") > -1) {
        if ($("#nurse_reg_no").val() === "") {
            alert("Please specify the Nursing Council Reg No.");
            document.getElementById("nurse_reg_no").style.borderColor = "red";
            return false;
        }
        if ($("#nurse_council").val() === "") {
            alert("Please specify the Name of the council registered under.");
            document.getElementById("nurse_reg_no").style.borderColor = "red";
            return false;
        }
        if ($("#nurse_reg_date").val() === "") {
            alert("Please specify the Nursing Council Registration Date");
            document.getElementById("nurse_reg_date").style.borderColor = "red";
            return false;
        }
        if ($("#nurse_valid_date").val() === "") {
            alert("Please specify the Nursing Council Registration Validity Date");
            document.getElementById("nurse_valid_date").style.borderColor = "red";
            return false;
        }

        // PPEG-HRD Start NurseRegCertificate
        if ((document.getElementById("upload_nurse_reg_certificate").name == "NA")
                || (document.getElementById("upload_nurse_reg_certificate").name == "")) {
            alert("Please upload Nursing Council Registration certificate");
            return false;
        }
        // PPEG-HRD End NurseRegCertificate
    } else if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Medical") > -1) {
        if ($("#doc_reg_no").val() === "") {
            alert("Please specify the Medical Council Reg No.");
            document.getElementById("doc_reg_no").style.borderColor = "red";
            return false;
        }
        // Start: ChangeId:2023090402
        if ($("#doc_council").val() === "") {
            alert("Please specify the Name of the council registered under.");
            document.getElementById("doc_reg_no").style.borderColor = "red";
            return false;
        }
        // End: ChangeId:2023090402
        if ($("#doc_reg_date").val() === "") {
            alert("Please specify the Medical Registration Date");
            document.getElementById("doc_reg_date").style.borderColor = "red";
            return false;
        }
        if ($("#doc_valid_date").val() === "") {
            alert("Please specify the Medical Registration Validity Date");
            document.getElementById("doc_valid_date").style.borderColor = "red";
            return false;
        }
        // PPEG-HRD Start MedicalRegCertificate
        if ((document.getElementById("upload_doc_reg_certificate").name == "NA")
                || (document.getElementById("upload_doc_reg_certificate").name == "")) {
            alert("Please upload Medical Council Registration certificate");
            return false;
        }
        // PPEG-HRD End MedicalRegCertificate
    }
    //Nurse and doc ends here
    heYopObj = null;
    return true;
}

// Start: ChangeId: 2023121901
/*

function validateX(thisYear) {
    document.getElementById("x_edu_board").style.borderColor = "#ccc";
    document.getElementById("x_board_other").style.borderColor = "#ccc";
    document.getElementById("x_school").style.borderColor = "#ccc";
    document.getElementById("x_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("x_division").style.borderColor = "#ccc";
    document.getElementById("x_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("x_per").style.borderColor = "#ccc";
    document.getElementById("x_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("x_cgpa_max").style.borderColor = "#ccc";
    // document.getElementById("x_percentage").style.borderColor = "#ccc";
    document.getElementById("upload_x_formula").style.borderColor = "#ccc";
    if (document.getElementById("x_edu_board").value.length == 0) {
        alert("Please select an education board for secondary(10/X) education.");
        document.getElementById("x_edu_board").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_edu_board").value === "Others") {
        if (document.getElementById("x_board_other").value.length == 0) {
            alert("Please specify the Education Board for secondary(10/X) education.");
            document.getElementById("x_board_other").style.borderColor = "red";
            return;
        }
    }

    if (document.getElementById("x_school").value.length == 0) {
        alert("Please enter school for secondary(10/X) education.");
        document.getElementById("x_school").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_year_of_passing").value.length == 0) {
        alert("Please enter year of passing for secondary(10/X) education.");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_year_of_passing").value == "Select") {
        alert("Please enter year of passing for secondary(10/X) education.");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
    var dobYear = new Date(document.getElementById("dobAppl").value)
            .getFullYear();
    if (document.getElementById("x_year_of_passing").value <= dobYear) {
        alert("Year of passing for secondary(10/X) education cannot be less than year of birth ("
                + dobYear + ").");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_year_of_passing").value > thisYear) {
        alert("Year of passing for secondary(10/X) education cannot be greater than current year.");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
// if (document.getElementById("x_division").value.length == 0)
// {
// alert("Please enter division for secondary(10/X)
// education.");
// document.getElementById("x_division").style.borderColor =
// "red";
// return false;
// }

    if (document.getElementById("x_percentage_cgpa").value.trim().length == 0) {
        alert("Please select either Percentage or CGPA for secondary(10/X) education");
        document.getElementById("x_percentage_cgpa").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_percentage_cgpa").value == "Percentage") {
        if (document.getElementById("x_per").value.trim().length == 0) {
            alert("Percentage for secondary(10/X) education should not be left blank!");
            document.getElementById("x_per").style.borderColor = "red";
            return false;
        }
        if (document.getElementById("x_per").value == 0) {
            alert("Please enter correct value for percentage for secondary(10/X) education.");
            // document.getElementById("x_per").style.borderColor =
            // "red";
            return false;
        }
        if ( (parseFloat(document.getElementById("x_per").value.trim()) > 100) || ((parseFloat(document.getElementById("x_per").value.trim()) ) < 10))
        {
            alert("Percentage for secondary(10/X) education cannot be less than 10% and cannot be greater than 100%. Please check.");
            document.getElementById("x_per").style.borderColor = "red";
            return false;
        }
    } else if (document.getElementById("x_percentage_cgpa").value == "CGPA") {
        if (document.getElementById("x_cgpa_obt").value.trim().length == 0) {
            alert("CGPA obtained for secondary(10/X) education should not be left blank!");
            document.getElementById("x_cgpa_obt").style.borderColor = "red";
            return false;
        }
        if (document.getElementById("x_cgpa_max").value === "" || document.getElementById("x_cgpa_max").value.trim().length === 0) {
            alert("Maximum CGPA for secondary(10/X) education should not be left blank!");
            document.getElementById("x_cgpa_max").style.borderColor = "red";
            return false;
        }
        var co = parseFloat(document.getElementById("x_cgpa_obt").value.trim());
        var cm = parseFloat(document.getElementById("x_cgpa_max").value.trim());
        if (co > cm) {
            alert("CGPA obtained cannot be more than max CGPA for secondary(10/X) education!");
            document.getElementById("x_cgpa_obt").style.borderColor = "red";
            document.getElementById("x_cgpa_max").style.borderColor = "red";
            return;
        }
        if (    (document.getElementById("x_percentage").value.trim().length > 0)
                && 
                ( 
                    parseFloat(document.getElementById("x_percentage").value.trim()) > 100
                    || parseFloat(document.getElementById("x_percentage").value.trim()) < 10) 
                ){ // PPEG-HRD PercentageLess
            alert("CGPA to percentage cannot be less than 10 and greater than 100 for secondary (10/X) education, please check!");
            document.getElementById("x_percentage").style.borderColor = "red";
            return false;
        }
    if ((
            (document.getElementById("upload_x_formula").name == "NA")
            || (document.getElementById("upload_x_formula").name == "")) 
            && (document.getElementById("x_percentage").value.trim()) // ChangeId: 2023112302
            ){
            alert("Please upload CGPA conversion formula for secondary.");
            document.getElementById("upload_x_formula").style.borderColor = "red";
            return false;
        }
    }
    if ((document.getElementById("upload_x_degree_certificate").name == "NA")
            || (document.getElementById("upload_x_degree_certificate").name == "")) {
        alert("Please upload certificate for secondary(10/X) education");
        return false;
    }
    
    return true;
}

 */
// End: ChangeId: 2023121901
// Start: ChangeId: 2023121901
function validateX(thisYear) {
    document.getElementById("x_edu_board").style.borderColor = "#ccc";
    document.getElementById("x_board_other").style.borderColor = "#ccc";
    document.getElementById("x_school").style.borderColor = "#ccc";
    document.getElementById("x_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("x_division").style.borderColor = "#ccc";
    document.getElementById("x_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("x_per").style.borderColor = "#ccc";
    document.getElementById("x_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("x_cgpa_max").style.borderColor = "#ccc";
    document.getElementById("x_percentage").style.borderColor = "#ccc";
    document.getElementById("upload_x_formula").style.borderColor = "#ccc";
    if (document.getElementById("x_edu_board").value.length == 0) {
        alert("Please select an education board for secondary(10/X) education.");
        document.getElementById("x_edu_board").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_edu_board").value === "Others") {
        if (document.getElementById("x_board_other").value.length == 0) {
            alert("Please specify the Education Board for secondary(10/X) education.");
            document.getElementById("x_board_other").style.borderColor = "red";
            return;
        }
    }

    if (document.getElementById("x_school").value.length == 0) {
        alert("Please enter school for secondary(10/X) education.");
        document.getElementById("x_school").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_year_of_passing").value.length == 0) {
        alert("Please enter year of passing for secondary(10/X) education.");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_year_of_passing").value == "Select") {
        alert("Please enter year of passing for secondary(10/X) education.");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
    var dobYear = new Date(document.getElementById("dobAppl").value)
            .getFullYear();
    if (document.getElementById("x_year_of_passing").value <= dobYear) {
        alert("Year of passing for secondary(10/X) education cannot be less than year of birth ("
                + dobYear + ").");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("x_year_of_passing").value > thisYear) {
        alert("Year of passing for secondary(10/X) education cannot be greater than current year.");
        document.getElementById("x_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("x_per").value.trim().length === 0 
            && document.getElementById("x_cgpa_obt").value.trim().length === 0 ) {
        alert("Enter marks for secondary(10/X) education!");
        document.getElementById("x_per").style.borderColor = "red";
        document.getElementById("x_cgpa_obt").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("x_per").value.trim().length !== 0) {
        if (document.getElementById("x_per").value === 0) {
            alert("Please enter correct value for percentage for secondary(10/X) education.");
            return false;
        }
        if ( (parseFloat(document.getElementById("x_per").value.trim()) > 100) || ((parseFloat(document.getElementById("x_per").value.trim()) ) < 10))
        {
            alert("Percentage for secondary(10/X) education must be within 10 to 100!");
            document.getElementById("x_per").style.borderColor = "red";
            return false;
        }
    }  
    if (document.getElementById("x_cgpa_obt").value.trim().length !== 0) {
        if (document.getElementById("x_cgpa_max").value === "" 
                || document.getElementById("x_cgpa_max").value === "Max CGPA" 
                || document.getElementById("x_cgpa_max").value.trim().length === 0) {
            alert("Select Maximum CGPA for secondary(10/X) education!");
            document.getElementById("x_cgpa_max").style.borderColor = "red";
            return false;
        }
        var co = parseFloat(document.getElementById("x_cgpa_obt").value.trim());
        var cm = parseFloat(document.getElementById("x_cgpa_max").value.trim());
        if (co > cm) {
            alert("CGPA obtained cannot be more than max CGPA for secondary(10/X) education!");
            document.getElementById("x_cgpa_obt").style.borderColor = "red";
            document.getElementById("x_cgpa_max").style.borderColor = "red";
            return;
        }
        if (document.getElementById("x_percentage").value.trim().length === 0 && cm < 9.5 ) {
            alert("Enter CGPA to Percentage for secondary(10/X) education!");
            document.getElementById("x_percentage").style.borderColor = "red";
            return false;
        }
        
        if (    (document.getElementById("x_percentage").value.trim().length > 0)
                && 
                ( 
                    parseFloat(document.getElementById("x_percentage").value.trim()) > 100
                    || parseFloat(document.getElementById("x_percentage").value.trim()) < 10) 
                ){ // PPEG-HRD PercentageLess
            alert("CGPA to percentage cannot be less than 10 and greater than 100 for secondary (10/X) education, please check!");
            document.getElementById("x_percentage").style.borderColor = "red";
            return false;
        }
    if ((
            (document.getElementById("upload_x_formula").name == "NA")
            || (document.getElementById("upload_x_formula").name == "")) 
            && (document.getElementById("x_percentage").value.trim()
            && cm < 9.5) 
            ){
            alert("Please upload CGPA to Percentage conversion formula for secondary.");
            document.getElementById("upload_x_formula").style.borderColor = "red";
            return false;
        }
    }
    if ((document.getElementById("upload_x_degree_certificate").name == "NA")
            || (document.getElementById("upload_x_degree_certificate").name == "")) {
        alert("Please upload certificate for secondary(10/X) education");
        return false;
    }
    
    return true;
}
// End: ChangeId: 2023121901
// Start: ChangeId: 2023122001
/*
function validateXII(thisYear) {
    document.getElementById("xii_edu_board").style.borderColor = "#ccc";
    document.getElementById("xii_board_other").style.borderColor = "#ccc";
    document.getElementById("xii_school").style.borderColor = "#ccc";
    document.getElementById("xii_specialization").style.borderColor = "#ccc";
    document.getElementById("xii_specialization_other").style.borderColor = "#ccc";
    document.getElementById("xii_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("xii_division").style.borderColor = "#ccc";
    document.getElementById("xii_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("xii_per").style.borderColor = "#ccc";
    document.getElementById("xii_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("xii_cgpa_max").style.borderColor = "#ccc";
    // document.getElementById("xii_percentage").style.borderColor = "#ccc";
    document.getElementById("xii_formula").style.borderColor = "#ccc";
    if (document.getElementById("xii_edu_board").value.length == 0) {
        alert("Please select an education board for senior secondary(12/XII) education.");
        document.getElementById("xii_edu_board").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("xii_edu_board").value === "Others") {
        if (document.getElementById("xii_board_other").value.length === 0) {
            alert("Please specify the Education Board for senior secondary(12/XII) education.");
            document.getElementById("xii_board_other").style.borderColor = "red";
            return;
        }
    }

    if (document.getElementById("xii_school").value.length === 0) {
        alert("Please enter the school for senior secondary(12/XII) education.");
        document.getElementById("xii_school").style.borderColor = "red";
        return false;
    }

    if ((document.getElementById("xii_specialization").value.length === 0)
            || (document.getElementById("xii_specialization").value == "Select")) {
        alert("Please select subjects for senior secondary(12/XII) education.");
        document.getElementById("xii_specialization").style.borderColor = "red";
        return;
    }

    if (document.getElementById("xii_specialization").value == "Others") {
        if (document.getElementById("xii_specialization_other").value.length == 0) {
            alert("Senior Secondary specialization should not be left blank.");
            document.getElementById("xii_specialization_other").style.borderColor = "red";
            return false;
        }
    }

    if (document.getElementById("xii_year_of_passing").value.length == 0) {
        alert("Please enter year of passing for senior secondary(12/XII) education.");
        document.getElementById("xii_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("xii_year_of_passing").value == "Select") {
        alert("Please enter year of passing for senior secondary(12/XII) education.");
        document.getElementById("xii_year_of_passing").style.borderColor = "red";
        return false;
    }

    try {
        var yop_se = parseInt(document.getElementById("x_year_of_passing").value);
        var yop_ss = parseInt(document.getElementById("xii_year_of_passing").value);
        if (yop_ss <= yop_se) {
            alert("Year of passing for senior secondary(12/XII) education cannot be less than or same as year of passing for secondary(10/X) education.");
            document.getElementById("xii_year_of_passing").style.borderColor = "red";
            return false;
        }
    } catch (err) {
    }

    if (document.getElementById("xii_year_of_passing").value > thisYear) {
        alert("Year of passing for senior secondary(12/XII) education cannot be greater than current year.");
        document.getElementById("xii_year_of_passing").style.borderColor = "red";
        return false;
    }

// if (document.getElementById("xii_division").value.length == 0) {
// alert("Please enter year of division for senior secondary(12/XII)
// education.");
// document.getElementById("xii_division").style.borderColor =
// "red";
// return false;
// }
    if (document.getElementById("xii_percentage_cgpa").value.trim().length == 0) {
        alert("Please select either Percentage or CGPA for senior secondary(12/XII) education");
        document.getElementById("xii_percentage_cgpa").style.borderColor = "red";
        return false;
    }
// alert("test : " +
// document.getElementById("xii_percentage_cgpa").value);
    if (document.getElementById("xii_percentage_cgpa").value == "Percentage") {
        if (document.getElementById("xii_per").value.trim().length == 0) {
            alert("Percentage for senior secondary(12/XII) education should not be left blank!");
            document.getElementById("xii_per").style.borderColor = "red";
            return false;
        }
// alert("test1 : " + document.getElementById("xii_per").value);
        if (document.getElementById("xii_per").value.trim() == "0") {
            alert("Please enter correct value for percentage for senior secondary(12/XII) education.");
            // document.getElementById("x_per").style.borderColor =
            // "red";
            return false;
        }
// if (document.getElementById("xii_per").value < 20) {
// alert("Please verify the percentage for senior
// secondary(12/XII) education.");
// // document.getElementById("x_per").style.borderColor =
// // "red";
// // return true;
// }
        if ( (parseFloat(document.getElementById("xii_per").value.trim()) > 100) || ((parseFloat(document.getElementById("xii_per").value.trim()) ) < 10)) {
            alert("Percentage for senior secondary(12/XII) education cannot be less than 10% and cannot be greater than 100%. Please check.");
            document.getElementById("xii_per").style.borderColor = "red";
            return false;
        }

// if (document.getElementById("xii_per").value < 65) {
// alert("Percentage for senior secondary(12/XII) education
// cannot less than
// 65%");
// document.getElementById("xii_per").style.borderColor = "red";
// return false;
// }
    } else if (document.getElementById("xii_percentage_cgpa").value == "CGPA") {
        if (document.getElementById("xii_cgpa_obt").value.trim().length == 0) {
            alert("CGPA obtained for senior secondary(12/XII) education should not be left blank!");
            document.getElementById("xii_cgpa_obt").style.borderColor = "red";
            return false;
        }
        if (document.getElementById("xii_cgpa_max").value === "" || document.getElementById("xii_cgpa_max").value.trim().length === 0) {
            alert("Maximum CGPA for senior secondary(12/XII) education should not be left blank!");
            document.getElementById("xii_cgpa_max").style.borderColor = "red";
            return false;
        }
        var co = parseFloat(document.getElementById("xii_cgpa_obt").value);
        var cm = parseFloat(document.getElementById("xii_cgpa_max").value);
        if (cm < co) {
            alert("CGPA obtained cannot be greater than Maximum CGPA for senior secondary(12/XII) education!");
            document.getElementById("xii_cgpa_obt").style.borderColor = "red";
            document.getElementById("xii_cgpa_max").style.borderColor = "red";
            return;
        }

        if ((document.getElementById("xii_percentage").value.trim().length > 0)
                && ( (parseFloat(document.getElementById("xii_percentage").value.trim()) > 100) || ((parseFloat(document.getElementById("xii_percentage").value.trim()) ) < 10))
) {
            alert("CGPA to percentage (senior secondary) cannot be less than 10 and cannot be greater than 100, please check!");
            document.getElementById("xii_percentage").style.borderColor = "red";
            return false;
        }

// if ((co / cm) < 0.65) {
// alert("In senior secondary details, CGPA obtained/ Max CGPA
// cannot be less than 65%");
// document.getElementById("xii_cgpa_obt").style.borderColor =
// "red";
// return;
// }

// if ((document.getElementById("xii_percentage").value.length >
// 0)
// && (document.getElementById("xii_percentage").value < 65)) {
// alert("CGPA to percentage (senior secondary) cannot less than
// 65, please check!");
// document.getElementById("xii_percentage").style.borderColor =
// "red";
// return false;
// }

// if (document.getElementById("xii_percentage").value.length ==
// 0) {
// alert("CGPA to percentage for senior secondary(12/XII)
// education is blank,
// please check!");
// document.getElementById("xii_percentage").style.borderColor =
// "red";
// return false;
// }
        if (
                ((document.getElementById("xii_formula").name == "NA")
                || (document.getElementById("xii_formula").name == "")) 
                && (document.getElementById("xii_percentage").value.trim()) // ChangeId: 2023112302 
            )
        {
            alert("Please upload CGPA conversion formula for senior secondary.");
            document.getElementById("xii_formula").style.borderColor = "red";
            return false;
        }
    }
    if ((document.getElementById("upload_xii_degree_certificate").name == "NA")
            || (document.getElementById("upload_xii_degree_certificate").name == "")) {
        alert("Please upload certificate for senior secondary(12/XII) education");
        return false;
    }

    return true;
}
*/
// End: ChangeId: 2023122001
// Start: ChangeId: 2023122001
function validateXII(thisYear) {
    document.getElementById("xii_edu_board").style.borderColor = "#ccc";
    document.getElementById("xii_board_other").style.borderColor = "#ccc";
    document.getElementById("xii_school").style.borderColor = "#ccc";
    document.getElementById("xii_specialization").style.borderColor = "#ccc";
    document.getElementById("xii_specialization_other").style.borderColor = "#ccc";
    document.getElementById("xii_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("xii_division").style.borderColor = "#ccc";
    document.getElementById("xii_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("xii_per").style.borderColor = "#ccc";
    document.getElementById("xii_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("xii_cgpa_max").style.borderColor = "#ccc";
    document.getElementById("xii_percentage").style.borderColor = "#ccc";
    document.getElementById("xii_formula").style.borderColor = "#ccc";
    if (document.getElementById("xii_edu_board").value.length == 0) {
        alert("Please select an education board for senior secondary(12/XII) education.");
        document.getElementById("xii_edu_board").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("xii_edu_board").value === "Others") {
        if (document.getElementById("xii_board_other").value.length === 0) {
            alert("Please specify the Education Board for senior secondary(12/XII) education.");
            document.getElementById("xii_board_other").style.borderColor = "red";
            return;
        }
    }

    if (document.getElementById("xii_school").value.length === 0) {
        alert("Please enter the school for senior secondary(12/XII) education.");
        document.getElementById("xii_school").style.borderColor = "red";
        return false;
    }

    if ((document.getElementById("xii_specialization").value.length === 0)
            || (document.getElementById("xii_specialization").value == "Select")) {
        alert("Please select subjects for senior secondary(12/XII) education.");
        document.getElementById("xii_specialization").style.borderColor = "red";
        return;
    }

    if (document.getElementById("xii_specialization").value == "Others") {
        if (document.getElementById("xii_specialization_other").value.length == 0) {
            alert("Senior Secondary specialization should not be left blank.");
            document.getElementById("xii_specialization_other").style.borderColor = "red";
            return false;
        }
    }

    if (document.getElementById("xii_year_of_passing").value.length == 0) {
        alert("Please enter year of passing for senior secondary(12/XII) education.");
        document.getElementById("xii_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("xii_year_of_passing").value == "Select") {
        alert("Please enter year of passing for senior secondary(12/XII) education.");
        document.getElementById("xii_year_of_passing").style.borderColor = "red";
        return false;
    }

    try {
        var yop_se = parseInt(document.getElementById("x_year_of_passing").value);
        var yop_ss = parseInt(document.getElementById("xii_year_of_passing").value);
        if (yop_ss <= yop_se) {
            alert("Year of passing for senior secondary(12/XII) education cannot be less than or same as year of passing for secondary(10/X) education.");
            document.getElementById("xii_year_of_passing").style.borderColor = "red";
            return false;
        }
    } catch (err) {
    }

    if (document.getElementById("xii_year_of_passing").value > thisYear) {
        alert("Year of passing for senior secondary(12/XII) education cannot be greater than current year.");
        document.getElementById("xii_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("xii_per").value.trim().length === 0 
            && document.getElementById("xii_cgpa_obt").value.trim().length === 0 ) {
        alert("Enter marks for secondary(12/XII) education!");
        document.getElementById("xii_per").style.borderColor = "red";
        document.getElementById("xii_cgpa_obt").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("xii_per").value.trim().length !== 0) {
        if (document.getElementById("xii_per").value === 0) {
            alert("Please enter correct value for percentage for secondary(12/XII) education.");
            return false;
        }
        if ( (parseFloat(document.getElementById("xii_per").value.trim()) > 100) || ((parseFloat(document.getElementById("xii_per").value.trim()) ) < 10)) 
	{
            alert("Percentage for secondary(12/XII) education must be within 10 to 100!");
            document.getElementById("xii_per").style.borderColor = "red";
            return false;
        }
    } 
    if (document.getElementById("xii_cgpa_obt").value.trim().length !== 0) {
        if (document.getElementById("xii_cgpa_max").value === "" 
                || document.getElementById("xii_cgpa_max").value === "Max CGPA" 
                || document.getElementById("xii_cgpa_max").value.trim().length === 0) {
            alert("Select Maximum CGPA for secondary(12/XII) education!");
            document.getElementById("xii_cgpa_max").style.borderColor = "red";
            return false;
        }
        var co = parseFloat(document.getElementById("xii_cgpa_obt").value);
        var cm = parseFloat(document.getElementById("xii_cgpa_max").value);
        if (co > cm) {
            alert("CGPA obtained cannot be greater than Maximum CGPA for senior secondary(12/XII) education!");
            document.getElementById("xii_cgpa_obt").style.borderColor = "red";
            document.getElementById("xii_cgpa_max").style.borderColor = "red";
            return;
        }
	if (document.getElementById("xii_percentage").value.trim().length === 0 && cm < 9.5 ) {
            alert("Enter CGPA to Percentage for secondary(12/XII) education!");
            document.getElementById("xii_percentage").style.borderColor = "red";
            return false;
        }
        
        if (    (document.getElementById("xii_percentage").value.trim().length > 0)
                && 
                ( 
                    parseFloat(document.getElementById("xii_percentage").value.trim()) > 100
                    || parseFloat(document.getElementById("xii_percentage").value.trim()) < 10) 
                ){ // PPEG-HRD PercentageLess
            alert("CGPA to percentage cannot be less than 10 and greater than 100 for secondary (12/XII) education, please check!");
            document.getElementById("xii_percentage").style.borderColor = "red";
            return false;
        }
        if (
                ((document.getElementById("xii_formula").name == "NA")
                || (document.getElementById("xii_formula").name == "")) 
                && (document.getElementById("xii_percentage").value.trim()
		&& cm < 9.5) // ChangeId: 2023112302 
            )
        {
            alert("Please upload CGPA conversion formula for senior secondary.");
            document.getElementById("xii_formula").style.borderColor = "red";
            return false;
        }
    }
    if ((document.getElementById("upload_xii_degree_certificate").name == "NA")
            || (document.getElementById("upload_xii_degree_certificate").name == "")) {
        alert("Please upload certificate for senior secondary(12/XII) education");
        return false;
    }

    return true;
}
// End: ChangeId: 2023122001
function validateITI(thisYear) {
    document.getElementById("iti_college").style.borderColor = "#ccc";
    document.getElementById("iti_specialization").style.borderColor = "#ccc";
    document.getElementById("iti_specialization_other").style.borderColor = "#ccc";
    document.getElementById("iti_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("iti_division").style.borderColor = "#ccc";
    document.getElementById("iti_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("iti_per").style.borderColor = "#ccc";
    document.getElementById("iti_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("iti_cgpa_max").style.borderColor = "#ccc";
    // document.getElementById("iti_percentage").style.borderColor = "#ccc";
    document.getElementById("iti_formula").style.borderColor = "#ccc";
    if (document.getElementById("iti_college").value.length == 0) {
        alert("Please enter the college for ITI.");
        document.getElementById("iti_college").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("iti_specialization").value == "Others") {
        if (document.getElementById("iti_specialization_other").value.length == 0) {
            alert("ITI specialization should not be left blank.");
            document.getElementById("iti_specialization_other").style.borderColor = "red";
            return false;
        }
    } else if (document.getElementById("iti_specialization").value.length == 0) {
        alert("Please select specialization for ITI.");
        document.getElementById("iti_specialization").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("iti_year_of_passing").value.length == 0) {
        alert("Please enter year of passing for ITI.");
        document.getElementById("iti_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("iti_year_of_passing").value <= document.getElementById("x_year_of_passing").value) {
        alert("Year of passing for ITI cannot be less than or same as year of passing for secondary(10/X) education.");
        document.getElementById("iti_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("iti_year_of_passing").value > thisYear) {
        alert("Year of passing for ITI cannot be greater than current year.");
        document.getElementById("iti_year_of_passing").style.borderColor = "red";
        return false;
    }

// if (document.getElementById("iti_division").value.trim().length == 0) {
// alert("Please enter year of division for ITI.");
// document.getElementById("iti_division").style.borderColor =
// "red";
// return false;
// }
    if (document.getElementById("iti_percentage_cgpa").value.length == 0) {
        alert("Please select either Percentage or CGPA for ITI");
        document.getElementById("iti_percentage_cgpa").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("iti_percentage_cgpa").value == "Percentage") {
        if (document.getElementById("iti_per").value.trim().length == 0) {
            alert("Percentage should not be left blank for ITI!");
            document.getElementById("iti_per").style.borderColor = "red";
            return false;
        }
        if ( (parseFloat(document.getElementById("iti_per").value.trim()) > 100) || ((parseFloat(document.getElementById("iti_per").value.trim()) ) < 10))
{
            alert("Percentage for ITI cannot be less than 10% and cannot be greater than 100%. Please check.");
            document.getElementById("iti_per").style.borderColor = "red";
            return false;
        }

// if (document.getElementById("iti_per").value < 65) {
// alert("Percentage for ITI cannot less than 65%");
// document.getElementById("iti_per").style.borderColor = "red";
// return false;
// }
    } else if (document.getElementById("iti_percentage_cgpa").value == "CGPA") {
        if (document.getElementById("iti_cgpa_obt").value.trim().length == 0) {
            alert("CGPA obtained should not be left blank for ITI!");
            document.getElementById("iti_cgpa_obt").style.borderColor = "red";
            return false;
        }
        if (document.getElementById("iti_cgpa_max").value === "" || document.getElementById("iti_cgpa_max").value.trim().length === 0) {
            alert("Maximum CGPA should not be left blank for ITI!");
            document.getElementById("iti_cgpa_max").style.borderColor = "red";
            return false;
        }
        var co = parseFloat(document.getElementById("iti_cgpa_obt").value.trim());
        var cm = parseFloat(document.getElementById("iti_cgpa_max").value.trim());
        if (cm < co) {
            alert("CGPA obtained cannot be greater than Maximum CGPA for ITI!");
            document.getElementById("iti_cgpa_obt").style.borderColor = "red";
            document.getElementById("iti_cgpa_max").style.borderColor = "red";
            return;
        }
        if (document.getElementById("iti_percentage").value.trim().length == 0) {
            alert("CGPA to Percentage field should not be left blank for ITI!");
            document.getElementById("iti_percentage").style.borderColor = "red";
            return false;
        }
        if ((document.getElementById("iti_percentage").value.trim().length > 0)
                && ( (parseFloat(document.getElementById("iti_percentage").value.trim()) > 100) || ((parseFloat(document.getElementById("iti_percentage").value.trim()) ) < 10))) {
            alert("CGPA to percentage (ITI) cannot be less than 10 and cannot be greater than 100, please check!");
            document.getElementById("iti_percentage").style.borderColor = "red";
            return false;
        }

// if ((co / cm) < 0.65) {
// alert("In ITI details, CGPA obtained/ Max CGPA cannot
// be less than 65%");
// document.getElementById("iti_cgpa_obt").style.borderColor =
// "red";
// return;
// }

// if ((document.getElementById("iti_percentage").value.length >
// 0)
// && (document.getElementById("iti_percentage").value < 65)) {
// alert("CGPA to percentage (ITI) cannot less than 65,
// please check!");
// document.getElementById("iti_percentage").style.borderColor =
// "red";
// return false;
// }

// if (document.getElementById("iti_percentage").value.length ==
// 0) {
// alert("CGPA to percentage is blank for ITI!");
// document.getElementById("iti_percentage").style.borderColor =
// "red";
// return false;
// }
        if ((document.getElementById("iti_formula").name == "NA")
                || (document.getElementById("iti_formula").name == "")) {
            alert("Please upload CGPA conversion formula for ITI.");
            document.getElementById("iti_formula").style.borderColor = "red";
            return false;
        }
    }
    if ((document.getElementById("upload_iti_degree_certificate").name == "NA")
            || (document.getElementById("upload_iti_degree_certificate").name == "")) {
        alert("Please upload certificate for ITI");
        return false;
    }
    return true;
}

function validateDIP(thisYear) {
    document.getElementById("dip_college").style.borderColor = "#ccc";
    document.getElementById("dip_specialization").style.borderColor = "#ccc";
    document.getElementById("dip_specialization_other").style.borderColor = "#ccc";
    document.getElementById("dip_year_of_passing").style.borderColor = "#ccc";
    document.getElementById("dip_division").style.borderColor = "#ccc";
    document.getElementById("dip_percentage_cgpa").style.borderColor = "#ccc";
    document.getElementById("dip_per").style.borderColor = "#ccc";
    document.getElementById("dip_cgpa_obt").style.borderColor = "#ccc";
    document.getElementById("dip_cgpa_max").style.borderColor = "#ccc";
    // document.getElementById("dip_percentage").style.borderColor = "#ccc";
    document.getElementById("dip_formula").style.borderColor = "#ccc";
    if (document.getElementById("dip_college").value.length == 0) {
        alert("Please enter the college for Diploma.");
        document.getElementById("dip_college").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("dip_specialization").value == "Others") {
        if (document.getElementById("dip_specialization_other").value.length == 0) {
            alert("Diploma specialization should not be left blank.");
            document.getElementById("dip_specialization_other").style.borderColor = "red";
            return false;
        }
    } else if (document.getElementById("dip_specialization").value.length == 0) {
        alert("Please select specialization for Diploma.");
        document.getElementById("dip_specialization").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("dip_year_of_passing").value.length == 0) {
        alert("Please enter year of passing for Diploma.");
        document.getElementById("dip_year_of_passing").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("dip_year_of_passing").value == "Select") {
        alert("Please enter year of passing for Diploma.");
        document.getElementById("dip_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("dip_year_of_passing").value <= document
            .getElementById("x_year_of_passing").value) {
        alert("Year of passing for Diploma cannot be less than or same as year of passing for secondary(10/X) education.");
        document.getElementById("dip_year_of_passing").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("dip_year_of_passing").value > thisYear) {
        alert("Year of passing for Diploma cannot be greater than current year.");
        document.getElementById("dip_year_of_passing").style.borderColor = "red";
        return false;
    }

// if (document.getElementById("dip_division").value.length == 0) {
// alert("Please enter year of division for Diploma.");
// document.getElementById("dip_division").style.borderColor =
// "red";
// return false;
// }
    if (document.getElementById("dip_percentage_cgpa").value.trim().length == 0) {
        alert("Please select either Percentage or CGPA for Diploma");
        document.getElementById("dip_percentage_cgpa").style.borderColor = "red";
        return false;
    }
    if (document.getElementById("dip_percentage_cgpa").value == "Percentage") {
        if (document.getElementById("dip_per").value.trim().length == 0) {
            alert("Percentage should not be left blank for Diploma!");
            document.getElementById("dip_per").style.borderColor = "red";
            return false;
        }
        if ( (parseFloat(document.getElementById("dip_per").value.trim()) > 100) || ((parseFloat(document.getElementById("dip_per").value.trim()) ) < 10))
        {
            alert("Percentage for Diploma cannot be less than 10% and cannot be greater than 100%. Please check.");
            document.getElementById("dip_per").style.borderColor = "red";
            return false;
        }

// if (document.getElementById("dip_per").value < 65) {
// alert("Percentage for Diploma cannot less than 65%");
// document.getElementById("dip_per").style.borderColor = "red";
// return false;
// }
    } else if (document.getElementById("dip_percentage_cgpa").value == "CGPA") {
        if (document.getElementById("dip_cgpa_obt").value.trim().length == 0) {
            alert("CGPA obtained should not be left blank for Diploma!");
            document.getElementById("dip_cgpa_obt").style.borderColor = "red";
            return false;
        }
        if (document.getElementById("dip_cgpa_max").value === "" || document.getElementById("dip_cgpa_max").value.trim().length === 0) {
            alert("Maximum CGPA should not be left blank for Diploma!");
            document.getElementById("dip_cgpa_max").style.borderColor = "red";
            return false;
        }
        var co = parseFloat(document.getElementById("dip_cgpa_obt").value.trim());
        var cm = parseFloat(document.getElementById("dip_cgpa_max").value.trim());
        if (cm < co) {
            alert("CGPA obtained cannot be greater than Maximum CGPA for Diploma!");
            document.getElementById("dip_cgpa_obt").style.borderColor = "red";
            document.getElementById("dip_cgpa_max").style.borderColor = "red";
            return;
        }
        if (document.getElementById("dip_percentage").value.trim().length == 0) {
            alert("CGPA to Percentage field should not be left blank for Diploma!");
            document.getElementById("dip_percentage").style.borderColor = "red";
            return false;
        }
        if ((document.getElementById("dip_percentage").value.trim().length > 0)
                && ( (parseFloat(document.getElementById("dip_percentage").value.trim()) > 100) || ((parseFloat(document.getElementById("dip_percentage").value.trim()) ) < 10))) {
            alert("CGPA to percentage (Diploma) cannot be less than 10 and cannot be greater than 100, please check!");
            document.getElementById("dip_percentage").style.borderColor = "red";
            return false;
        }

// if ((co / cm) < 0.65) {
// alert("In Diploma details, CGPA obtained/ Max CGPA cannot
// be less than 65%");
// document.getElementById("dip_cgpa_obt").style.borderColor =
// "red";
// return;
// }

// if ((document.getElementById("dip_percentage").value.length >
// 0)
// && (document.getElementById("dip_percentage").value < 65)) {
// alert("CGPA to percentage (Diploma) cannot less than 65,
// please check!");
// document.getElementById("dip_percentage").style.borderColor =
// "red";
// return false;
// }

// if (document.getElementById("dip_percentage").value.length ==
// 0) {
// alert("CGPA to percentage is blank for Diploma!");
// document.getElementById("dip_percentage").style.borderColor =
// "red";
// return false;
// }
        if (
                ((document.getElementById("dip_formula").name == "NA")
                || (document.getElementById("dip_formula").name == "")) 
                && (document.getElementById("dip_percentage").value.trim()) // ChangeId: 2023112302
        )
        {
            alert("Please upload CGPA conversion formula for Diploma.");
            document.getElementById("dip_formula").style.borderColor = "red";
            return false;
        }
    }
    if ((document.getElementById("upload_dip_degree_certificate").name == "NA")
            || (document.getElementById("upload_dip_degree_certificate").name == "")) {
        alert("Please upload certificate for Diploma");
        return false;
    }
    return true;
}

function validateCGPA(ele,cgpa_obt, cgpa_max) { // ChangeId: 2023122101 ele passed in validateCGPA()"
// var co = parseFloat(document.getElementById("x_cgpa_obt").value);
// var cm = parseFloat(document.getElementById("x_cgpa_max").value);
    var co = parseFloat(document.getElementById(cgpa_obt).value.trim());
    var cm = parseFloat(document.getElementById(cgpa_max).value.trim());
    if (cm < co) {
        alert("CGPA obtained cannot be greater than Maximum CGPA!");
        $("#" + cgpa_obt).val(""); // ChangeId: 2023121902
        $("#" + cgpa_obt).focus();
        document.getElementById(cgpa_obt).style.borderColor = "red";
        document.getElementById(cgpa_max).style.borderColor = "red";
    }
    // Start: ChangeId: 2023121901, 2023122001, 2023122101
    switch(cgpa_obt) 
    {
        case "x_cgpa_obt":
            if(cm > 0.5 && cm < 9.5){
                $("#div_x_cgpa_to_percentage").show();
                $("#div_x_cgpa_to_percentage_formula").show();
                //$("#x_min_cgpa").hide();
            }
            else {
                $("#div_x_cgpa_to_percentage").hide();
                $("#div_x_cgpa_to_percentage_formula").hide();
                $("#x_percentage").val("");

                // ChangeId: 2023121902
                //if( cm > 9.5) 
                //    $("#x_min_cgpa").show();
            }
            break;
        
        case "xii_cgpa_obt":
            if(cm > 0.5 && cm < 9.5){
                $("#div_xii_cgpa_to_percentage").show();
                $("#div_xii_cgpa_to_percentage_formula").show();
                //$("#xii_min_cgpa").hide();
            }
            else {
                $("#div_xii_cgpa_to_percentage").hide();
                $("#div_xii_cgpa_to_percentage_formula").hide();
                $("#xii_percentage").val("");

                // ChangeId: 2023121902
                //if( cm > 9.5) 
                //    $("#xii_min_cgpa").show();
            }
            break;
        
        case "ug_cgpa_obt":
            if(cm > 0.5 && cm < 9.5){
                $("#ug_CGPAPerDiv").show();
                $("#ug_CGPAFormDiv").show();
                $("#he_min_cgpa").hide();
            }
            else {
                $("#ug_CGPAPerDiv").hide();
                $("#ug_CGPAFormDiv").hide();
                $("#ug_percentage").val("");

                // ChangeId: 2023121902
                if( cm > 9.5) 
                    $("#he_min_cgpa").show();
            }
            if(ele.id === "ug_cgpa_max" && document.getElementById(ele.id).value === "10.0"){
                $("#ug_cgpa_obt").val("");
            }
            break;
    }
    // End: ChangeId: 2023121901, 2023122001, 2023122101
}

function validateExperience() {
    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        alertError(name); // ChangeId: 2023110809 
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        return false;
    }

    document.getElementById("experience").style.borderColor = "#ccc";
    // document.getElementById("exp_years").style.borderColor = "#ccc";
    // document.getElementById("exp_months").style.borderColor = "#ccc";

    if (document.getElementById("experience").value.length == 0) {
        alert("Experience should not be left blank.");
        document.getElementById("experience").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("experience").value == "Select") {
        alert("Please select Experience.");
        document.getElementById("experience").style.borderColor = "red";
        return false;
    }

    if (document.getElementById("experience").value == 'Yes') {
// if (document.getElementById("exp_years").value.length == 0) {
// alert("Years in experience duration should not be left blank.");
// document.getElementById("exp_years").style.borderColor = "red";
// return false;
// }
// if (document.getElementById("exp_months").value.length == 0) {
// alert("Months in experience duration should not be left blank.");
// document.getElementById("exp_months").style.borderColor = "red";
// return false;
// }
        var table = document.getElementById("ex_table_body");
        var n = table.rows.length;
        if (n < 1) {
            alert("Please add the experience details.");
            return false;
        }
    }
    return true;
}
// Start: ChangeId: 2023110809
function alertError(name){
    var selector = "[for=\""+name.replace("e_", "").toLowerCase()+"\"]";
    var field_label = $(selector).text();
    alert("Error in '" + field_label + "' field"); // ChangeId: 'Rectify the error' => 'Validation error'
}
// End: ChangeId: 2023110809

function saveDraft() {
    var flag1 = false;
    var name = "";
    $("font[color='red']").each(function () {
        var c = this.parentNode.className;
        if (c.indexOf("e_") === 0) {
            if (c.indexOf("hidden") === -1) {
                flag1 = true;
                name = c;
            }
        }
    });
    if (flag1) {
        // Start: ChangeId: 2023110809
        alertError(name);
        //alert("Please rectify the error on the page (" + name.replace("e_", "")
        //        + ")");
        // End: ChangeId: 2023110809
        return false;
    }

    var article = new Object();
    article.buttonType = "save";
    article.action = "submitApplicant";
    article.email = email;
    article.advt_no = $("#advt_no").val();
    article.post_no = $("#post_no").val();
    article.post_name = $("#post_name_applicant").val();
    article.upload_dir = UPLOAD_DIR;
    article.user_id = email;
    // initialize
    article.salutation = "NA";
    article.first_name = "NA";
    article.middle_name = "NA";
    article.last_name = "NA";
    article.father_name = "NA";
    article.mother_name = "NA";
    article.gender = "NA";
    article.marital_status = "NA";
    article.dob = "NA";
    article.place_of_birth = "NA";
    article.nationality = "NA";
    article.zone = "NA";
    article.category = "NA";
    article.category_pwd = "NA";
    article.category_pwd_scribe = "NA"; /* ChangeId:2025041501*/
    article.category_pwd_comptime = "NA"; /* ChangeId:2025050807*/
    article.category_ews = "NA";
    article.category_exservice = "NA";
    article.category_exservice_from = "NA";
    article.category_exservice_to = "NA";
    // Start: ChangeId: 2023111101
    article.cgov_serv = "NA"; 
    article.cgov_serv_doj = "NA";
    // End: ChangeId: 2023111101
    article.category_merit_sport = "NA";
    article.category_merit_sportname = "NA";
    article.category_merit_sportlevel = "NA";
    article.category_proof = "NA";
    article.category_pwd_certificate = "NA";
    article.category_ews_certificate = "NA";
    article.category_exservice_proof = "NA";
    article.house_no = "NA";
    article.locality = "NA";
    article.state = "NA";
    article.district = "NA";
    article.town = "NA";
    article.pincode = "-1";
    article.sameaddressconfirmation = "NA";
    article.p_house_no = "NA";
    article.p_locality = "NA";
    article.p_state = "NA";
    article.p_district = "NA";
    article.p_town = "NA";
    article.p_pincode = "-1";
    article.contact_no = "NA";
    article.alternate_contact = "NA";
    article.bank_acc_beneficiary = "NA"; //  ChangeId:2025041601
    article.bank_acc_no = "NA"; //  ChangeId: 2023111001
    article.bank_ifsc_code = "NA"; // ChangeId: 2023111001
    article.bank_acc_doc = "NA"; // ChangeId: 2025041601
    article.aadhaar = "NA"; // PPEG-HRD AADHAAR
    article.nearest_railway_station = "NA";
    article.photograph = "NA";
    article.signature = "NA";
    article.doneXII = "No"; // ChangeId: 2023111301 moved at begining
    article.doneITI = "No"; // ChangeId: 2023111301 moved at begining
    article.doneDIP = "No"; // ChangeId: 2023111301 moved at begining
    article.x_edu_board = "NA";
    article.x_school = "NA";
    article.x_year_of_passing = "-1";
    article.x_division = "NA";
    article.x_percentage_cgpa = "NA"; // ChangeId: 2023121901 -1 to NA
    article.x_cgpa_obt = "-1";
    article.x_cgpa_max = "-1";
    article.x_percentage = "-1";
    article.x_cgpa_to_perc = "-1"; // ChangeId: 2023121901 newly added
    article.x_cf = "NA";
    article.x_dc = "NA";
    article.xii_edu_board = "NA";
    article.xii_school = "NA";
    article.xii_specialization = "NA";
    article.xii_year_of_passing = "-1";
    article.xii_division = "NA";
    article.xii_percentage_cgpa = "NA"; // ChangeId: 2023122001 -1 to NA
    article.xii_cgpa_obt = "-1";
    article.xii_cgpa_max = "-1";
    article.xii_percentage = "-1";
    article.xii_cgpa_to_perc = "-1"; // ChangeId: 2023122001 newly added
    article.xii_cf = "NA";
    article.xii_dc = "NA";
    article.iti_type = "NA";
    article.iti_school = "NA";
    article.iti_specialization = "NA";
    article.iti_year_of_passing = "-1";
    article.iti_division = "NA";
    article.iti_percentage_cgpa = "-1";
    article.iti_cgpa_obt = "-1";
    article.iti_cgpa_max = "-1";
    article.iti_percentage = "-1";
    article.iti_cf = "NA";
    article.iti_dc = "NA";
    article.net = "NA";
    article.edu_others = "NA";
    article.tnc = "NA";
    article.university = "NA";
    article.topic = "NA";
    article.college = "NA";
    article.qualification = "NA";
    article.discipline = "NA";
    article.specialization = "NA";
    article.year_of_passing = "-1";
    article.division = "NA";
    article.percentage_cgpa = "NA";
    article.cgpa_obt = "-1";
    article.cgpa_max = "-1";
    article.percentage = "-1";
    article.marksheet = "NA";
    article.dc = "NA";
    article.abs = "NA";
    article.cf = "NA";
    article.eligibility = "NA";
    article.elg = "NA";
    article.experience = "NA";
    article.exp_years = "-1";
    article.exp_months = "-1";
    article.emp_name = "NA";
    article.emp_address = "NA";
    article.emp_desig = "NA";
    article.emp_work = "NA";
    article.emp_paydrawn = "-1"; // PPEG-HRD: PayDrawn
    article.time_from = "NA";
    article.time_to = "NA";
    article.emp_reason = "NA"; // PPEG-HRD: ReasonForLeaving
    article.govtExp = "NA";
    article.exp_years = -1;
    article.exp_months = -1;
    article.doneXII = "No"; // ChangeId: 2023111301 moved at begining
    article.doneITI = "No"; // ChangeId: 2023111301 moved at begining
    article.doneDIP = "No"; // ChangeId: 2023111301 moved at begining
    // alert("init complete");
                
    // define values
    // article.salutation = $("#salutation").val();
    // Start: ChangeId: 2023111301
    var doneXIIval = $("#doneXII").val();
    var doneITIval = $("#doneITI").val();
    var doneDIPval = $("#doneDIP").val();
    if ((doneXIIval !== null) && (doneXIIval !== "") && (doneXIIval === "Yes")) {
        article.doneXII = "Yes";
    }

    if ((doneITIval !== null) && (doneITIval !== "") && (doneITIval === "Yes")) {
        article.doneITI = "Yes";
    }

    if ((doneDIPval !== null) && (doneDIPval !== "") && (doneDIPval === "Yes")) {
        article.doneDIP = "Yes";
    }
    // End: ChangeId: 2023111301

    if ($("#salutation").val() !== null) {
        article.salutation = encodeURIComponent($("#salutation").val());
    }

    if ($("#first_name").val() == "") {
        article.first_name = encodeURIComponent("NA");
    } else {
        article.first_name = encodeURIComponent($("#first_name").val());
    }
    article.middle_name = encodeURIComponent(" ");
    article.last_name = encodeURIComponent(" ");
    // if ($("#middle_name").val() == "") {
    // article.middle_name = encodeURIComponent("NA");
    // } else {
    // article.middle_name = encodeURIComponent($("#middle_name").val());
    // }
    // if ($("#last_name").val() == "") {
    // article.last_name = encodeURIComponent("NA");
    // } else {
    // article.last_name = encodeURIComponent($("#last_name").val());
    // }
    if ($("#father_name").val() == "") {
        article.father_name = encodeURIComponent("NA");
    } else {
        article.father_name = encodeURIComponent($("#father_name").val());
    }
    if ($("#mother_name").val() == "") {
        article.mother_name = encodeURIComponent("NA");
    } else {
        article.mother_name = encodeURIComponent($("#mother_name").val());
    }
    if ($("#gender").val() !== null) {
        article.gender = $("#gender").val();
    }
    if ($("#marital_status").val() !== null) {
        article.marital_status = $("#marital_status").val();
    }
    article.dob = $("#dobAppl").val();
    if ($("#place_of_birth").val() !== "") {
        article.place_of_birth = $("#place_of_birth").val();
    }

    if ($("#nationality").val() !== null) {
        article.nationality = $("#nationality").val();
    }

    if ($("#category").val() !== null) {
        article.category = $("#category").val();
    }

    if ($("#zone_sel").val() !== null && $("#zone_sel").val().length !== 0) { // ChangeId: 2023120104 array length checked
        article.zone = $("#zone_sel").val().join("@"); // ChangeId: 2023112001;
    }
    if ($("#category_pwd").val() !== null) {
        article.category_pwd = $("#category_pwd").val();
    }
    /*Start: ChangeId:2025041501*/
    if ($("#category_pwd_scribe").val() !== null) {
        article.category_pwd_scribe = $("#category_pwd_scribe").val();
    }
    if ($("#upload_disability").val()) {
        article.category_pwd_certificate = $("#upload_disability").val().split("\\").pop();
    }
    /*End: ChangeId:2025041501*/
    
    /*Start: ChangeId:2025050807*/
    if ($("#category_pwd_comptime").val() !== null) {
        article.category_pwd_comptime = $("#category_pwd_comptime").val();
    }
    /*End: ChangeId:2025050807*/
    
    if ($("#category_ews").val() !== null) {
        article.category_ews = $("#category_ews").val();
    }
    if ($("#category_ser").val() !== null) {
        article.category_exservice = $("#category_ser").val();
    }
    article.category_exservice_from = "NA";
    article.category_exservice_to = "NA";
    if ($("#category_ser").val() == "Yes") {
        if ($("#category_exservice_from").val() != "") {
            article.category_exservice_from = $("#category_exservice_from")
                    .val();
        }
        if ($("#category_exservice_to").val() != "") {
            article.category_exservice_to = $("#category_exservice_to").val();
        }
    }
    if ($("#category_spt").val() !== null) {
        article.category_merit_sport = $("#category_spt").val();
    }
    article.category_merit_sportname = "NA";
    article.category_merit_sportlevel = "NA";

    if ($("#category_spt").val() == "Yes") {
        if ($("#category_merit_sportname").val() != "") { // ChangeId:2023083002 newly added
            article.category_merit_sportname = $("#category_merit_sportname").val();
        }
        if ($("#category_merit_sportlevel").val() != "") { // ChangeId:2023083002 newly added
            article.category_merit_sportname = $("#category_merit_sportlevel").val();
        }
    }

    if ($("#house_no").val() == "") {
        article.house_no = "NA";
    } else {
        article.house_no = encodeURIComponent($("#house_no").val());
    }
    if ($("#locality").val() == "") {
        article.locality = "NA";
    } else {
        article.locality = encodeURIComponent($("#locality").val());
    }
    if ($("#town").val() == "") {
        article.town = "NA";
    } else {
        article.town = $("#town").val();
    }
    if ($("#pincode").val() == "") {
        article.pincode = "NA";
    } else {
        article.pincode = $("#pincode").val();
    }
    if ($("#state").val() !== null) {
        article.state = $("#state").val();
    }
    if ($("#district").val() !== null) {
        article.district = $("#district").val();
    }
    article.sameaddressconfirmation = $("#sameaddressconfirmation").val();
    if ($("#p_house_no").val() == "") {
        article.p_house_no = "NA";
    } else {
        article.p_house_no = encodeURIComponent($("#p_house_no").val());
    }
    if ($("#p_locality").val() == "") {
        article.p_locality = "NA";
    } else {
        article.p_locality = encodeURIComponent($("#p_locality").val());
    }
    if ($("#p_town").val() == "") {
        article.p_town = "NA";
    } else {
        article.p_town = $("#p_town").val();
    }
    if ($("#p_pincode").val() == "") {
        article.p_pincode = "NA";
    } else {
        article.p_pincode = $("#p_pincode").val();
    }
    if (($('#p_state :selected').text() !== null) && ($('#p_state :selected').text() !== "State/UT")) {
        article.p_state = $('#p_state :selected').text();
    }
    if ($("#p_district").val() !== null) {
        article.p_district = $("#p_district").val();
    }
    article.contact_no = $("#contact_no").val();
    article.alternate_contact = "NA";
    if ($("#alternate_contact").val() != "") {
        article.alternate_contact = $("#alternate_contact").val();
    }
    // PPEG-HRD: Start AADHAAR
    article.aadhaar = $("#aadhaar").val();
    article.aadhaar = "NA";
    if ($("#aadhaar").val() != "") {
        article.aadhaar = $("#aadhaar").val();
    }
    // PPEG-HRD: End AADHAAR
    if ($("#nearest_railway_station").val() !== "") {
        article.nearest_railway_station = $("#nearest_railway_station").val();
    }
    // Start: ChangeId: 2023120103
    if(document.getElementById("upload_photograph").name !== ""){
        article.photograph = document.getElementById("upload_photograph").name;
    }
    if(document.getElementById("upload_signature").name){
        article.signature = document.getElementById("upload_signature").name;
    }
    // End: ChangeId: 2023120103
    
    //Start: ChangeId:2025041601
    if ( $("#bank_acc_beneficiary").val() && $("#bank_acc_beneficiary").val().trim() !== "") {
        article.bank_acc_beneficiary = $("#bank_acc_beneficiary").val();
    }
    
    if ($("#bank_acc_doc").val()) {
        article.bank_acc_doc = $("#bank_acc_doc").val().split("\\").pop();
    }
    //End: ChangeId:2025041601
    
    // Start: ChangeId: 2023111001
    if ( $("#bank_acc_no").val() && $("#bank_acc_no").val().trim() !== "") {
        article.bank_acc_no = $("#bank_acc_no").val();
    }
    if ( $("#bank_ifsc_code").val() && $("#bank_ifsc_code").val().trim() !== "") {
        article.bank_ifsc_code = $("#bank_ifsc_code").val();
    }
    // End: ChangeId: 2023111001
    
    // Start: ChangeId: 2023111101
    if ($("#cgov_serv").val() && $("#cgov_serv").val().trim() !== "") {
        article.cgov_serv = $("#cgov_serv").val();
    }
    if ($("#cgov_serv").val() === "Yes") {
        if ($("#cgov_serv_doj").val() && $("#cgov_serv_doj").val().trim() !== "") {
            article.cgov_serv_doj = $("#cgov_serv_doj").val();
        }
    }
    // End: ChangeId: 2023111101
    var tabIndex = document.getElementById("indexval").value;
    var arr = advertisementDetails[tabIndex]["Eligibility"];
    var eligibility = new Array();
    for (var j = 0; j < arr.length; j++) {
        eligibility[j] = arr[j]["eligibility"];
    }

    var elStr = "";
    for (var i = 0; i < eligibility.length; i++) {
// alert("ENTRED :" + eligibility[i]);
        elStr += eligibility[i] + "|";
        if (eligibility[i] == 'X' || eligibility[i] == "XII"
                || eligibility[i] == "ITI" || eligibility[i] == "DIPLOMA"
                || eligibility[i] == 'Graduate'
                || eligibility[i] == 'Postgraduate' || eligibility[i] == 'PhD'
                || eligibility[i] == 'PostDoctoral') {

// if ($("#x_edu_board").val() !== null) {
// article.x_edu_board = $("#x_edu_board").val();
// }
            var board = document.getElementById("x_edu_board").value;
            if (board === "Others") {
                board = document.getElementById("x_board_other").value;
            }
            if ((board !== "") && (board !== null)) {
                article.x_edu_board = board;
            }

            if ($("#x_school").val() !== "") {
                article.x_school = encodeURIComponent($("#x_school").val());
            }

            if (($("#x_year_of_passing").val() !== null)
                    && ($("#x_year_of_passing").val() !== "Select")) {
                article.x_year_of_passing = $("#x_year_of_passing").val();
            }

            if ($("#x_division").val() !== null) {
                article.x_division = $("#x_division").val();
            }
            
            // Start: ChangeId: 2023121901
            if ($("#x_cgpa_obt").val() !== "") {
                article.x_cgpa_obt = $("#x_cgpa_obt").val();
            }
            if ($("#x_cgpa_max").val() !== "null") {
                article.x_cgpa_max = $("#x_cgpa_max").val();
            }
            if ($("#x_percentage").val() !== "" && $("#x_cgpa_max").val() !== "10.0") { // ChangeId: 2023121901 x_cgpa_to_perc
                article.x_cgpa_to_perc = $("#x_percentage").val(); // ChangeId: 2023121901 x_cgpa_to_perc
            }
            if ($("#x_per").val() !== "") {
                article.x_percentage = $("#x_per").val();
            }
            /*
            var x_value = $("#x_percentage_cgpa").val();
            if (x_value !== null) {
                article.x_percentage_cgpa = x_value.trim();
                if (x_value == "CGPA") {
                    if ($("#x_cgpa_obt").val() !== "") {
                        article.x_cgpa_obt = $("#x_cgpa_obt").val();
                    }
                    if ($("#x_cgpa_max").val() !== "null") {
                        article.x_cgpa_max = $("#x_cgpa_max").val();
                    }
                    if ($("#x_percentage").val() != "") {
                        article.x_percentage = $("#x_percentage").val();
                    }
                } else {
                    if ($("#x_per").val() != "") {
                        article.x_percentage = $("#x_per").val();
                    }
                }
            }*/
            // End: ChangeId: 2023121901
            
            // Start: ChangeId: 2023111607
            if ($("#upload_x_degree_certificate").val()) {
                article.x_dc = $("#upload_x_degree_certificate").val().split("\\").pop();
            }
            // End: ChangeId: 2023111607
        }
        if (eligibility[i] == "XII" || eligibility[i] == 'Graduate'
                || eligibility[i] == 'Postgraduate' || eligibility[i] == 'PhD'
                || eligibility[i] == 'PostDoctoral') {
// alert("Inside XII");

// if ($("#xii_edu_board").val() !== null) {
// article.xii_edu_board = $("#xii_edu_board").val();
// }
            
            var board = document.getElementById("xii_edu_board").value;
            if (board === "Others") {
                board = document.getElementById("xii_board_other").value;
            }
            if ((board !== "") && (board !== null)) {
                article.xii_edu_board = board;
            }

            if ($("#xii_school").val() !== "") {
                article.xii_school = encodeURIComponent($("#xii_school").val());
            }

            if ($("#xii_year_of_passing").val() !== null) {
                article.xii_year_of_passing = $("#xii_year_of_passing").val();
            }

            if ($("#xii_division").val() !== null) {
                article.xii_division = $("#xii_division").val();
            }
            // Start: ChangeId: 2023122001
            if ($("#xii_cgpa_obt").val() !== "") {
                article.xii_cgpa_obt = $("#xii_cgpa_obt").val();
            }
            if ($("#xii_cgpa_max").val() !== "null") {
                article.xii_cgpa_max = $("#xii_cgpa_max").val();
            }
            if ($("#xii_percentage").val() !== "" && $("#xii_cgpa_max").val() !== "10.0") { // ChangeId: 2023122001 x_cgpa_to_perc
                article.xii_cgpa_to_perc = $("#xii_percentage").val(); // ChangeId: 2023122001 x_cgpa_to_perc
            }
            if ($("#xii_per").val() !== "") {
                article.xii_percentage = $("#xii_per").val();
            }
            /*
            var xii_value = $("#xii_percentage_cgpa").val();
            if (xii_value !== null) {
                article.xii_percentage_cgpa = xii_value;
                if (xii_value == "CGPA") {
                    if ($("#xii_cgpa_obt").val() !== "") {
                        article.xii_cgpa_obt = $("#xii_cgpa_obt").val();
                    }
                    if ($("#xii_cgpa_max").val() !== "null") {
                        article.xii_cgpa_max = $("#xii_cgpa_max").val();
                    }
                    if ($("#xii_percentage").val() != "") {
                        article.xii_percentage = $("#xii_percentage").val();
                    }
                } else {
                    if ($("#xii_per").val() != "") {
                        article.xii_percentage = $("#xii_per").val();
                    }
                }
            }*/
            // End: ChangeId: 2023122001
            article.xii_dc = document.getElementById("upload_xii_degree_certificate").name; // ChangeId: 2023111301
            
            var tempVal = $("#xii_specialization").val();
            if (tempVal === "Others") {
                tempVal = $("#xii_specialization_other").val();
            } else if ((tempVal === "") || (tempVal === null)) {
                tempVal = "NA";
            }
            article.xii_specialization = encodeURIComponent(tempVal);
        }
        if (eligibility[i] === 'DIPLOMA') {
            if ($("#dip").val() !== "") {
                article.dip_type = $("#dip").val();
            }
            if ($("#dip_edu_board").val() !== "") {
                article.dip_edu_board = $("#dip_edu_board").val();
            }
            if ($("#dip_college").val() !== "") {
                article.dip_school = encodeURIComponent($("#dip_college").val());
            }

            if ($("#dip_year_of_passing").val() !== null) {
                article.dip_year_of_passing = $("#dip_year_of_passing").val();
            }

            if ($("#dip_division").val() !== "Select") {
                article.dip_division = $("#dip_division").val();
            }
            var dip_value = $("#dip_percentage_cgpa").val();
            if (dip_value !== "") {
                article.dip_percentage_cgpa = dip_value;
                if (dip_value === "CGPA") {
                    article.dip_cgpa_obt = $("#dip_cgpa_obt").val();
                    if ($("#dip_cgpa_max").val() !== "") {
                        article.dip_cgpa_max = $("#dip_cgpa_max").val();
                    }
                    if ($("#dip_percentage").val() != "") {
                        article.dip_percentage = $("#dip_percentage").val();
                    }
                } else {
                    if ($("#dip_per").val() != "") {
                        article.dip_percentage = $("#dip_per").val();
                    }
                }
            }
            var tempVal = $("#dip_specialization").val();
            if (tempVal === "Others") {
                tempVal = $("#dip_specialization_other").val();
            } else if (tempVal === "") {
                tempVal = "NA";
            }
            article.dip_specialization = encodeURIComponent(tempVal);
        }
        if (eligibility[i] === 'ITI') {
            if ($("#iti_dip").val() !== "") {
                article.iti_type = $("#iti_dip").val();
            }
            if ($("#iti_edu_board").val() !== "") {
                article.iti_edu_board = $("#iti_edu_board").val();
            }
            if ($("#iti_college").val() !== "") {
                article.iti_school = encodeURIComponent($("#iti_college").val());
            }

            if ($("#iti_year_of_passing").val() !== null) {
                article.iti_year_of_passing = $("#iti_year_of_passing").val();
            }

            if ($("#iti_division").val() !== null) {
                article.iti_division = $("#iti_division").val();
            }

            var iti_value = $("#iti_percentage_cgpa").val();
            if ((iti_value != null) && (iti_value != "")) {
                article.iti_percentage_cgpa = iti_value;
                if (iti_value == "CGPA") {
                    article.iti_cgpa_obt = $("#iti_cgpa_obt").val();
                    if ($("#iti_cgpa_max").val() !== "") {
                        article.iti_cgpa_max = $("#iti_cgpa_max").val();
                    }
                    if ($("#iti_percentage").val() != "") {
                        article.iti_percentage = $("#iti_percentage").val();
                    }
                } else {
                    if ($("#iti_per").val() != "") {
                        article.iti_percentage = $("#iti_per").val();
                    }
                }
            }
            var tempVal = $("#iti_specialization").val();
            if (tempVal === "Others") {
                tempVal = $("#iti_specialization_other").val();
            } else if (tempVal === "") {
                tempVal = "NA";
            }
            article.iti_specialization = encodeURIComponent(tempVal);
        }
    }

    article.edu_others = "NA";
    if ($("#he_oth").val().length > 0) {
// alert(encodeURIComponent($("#he_oth").val()));
        article.edu_others = encodeURIComponent($("#he_oth").val());
    }

// if ($("#tnc").prop("checked")) {
// article.tnc = "Yes";
// } else {
// article.tnc = "No";
// }
// var table = document.getElementById("he_table_body");
// var n = table.rows.length;
// var elg = "";
//
// var eAdded = "";
// for (var r = 0; r < n; r++) {
// eAdded += table.rows[r].cells[0].innerHTML;
// }
//
// if (n > 0) {
// // var m = table.rows[0].cells.length;
// var uni = "";
// var top = "";
// var col = "";
// var qual = "";
// var spec = "";
// var disc = "";
// var yop = "";
// var div = "";
// var cp = "";
// var cobt = "";
// var cmax = "";
// var cf = "";
// var per = "";
// var ms = "";
// var dc = "";
// var abs = "";
// for (var r = 0; r < n; r++) {
// var tmp = table.rows[r].cells[0].innerHTML;
// var ta = tmp.split("(");
//
// for (var i = 0; i < eligibility.length; i++) {
// if (eligibility[i] == 'Graduate'
// || eligibility[i] == 'Postgraduate'
// || eligibility[i] == 'PhD'
// || eligibility[i] == 'PostDoctoral') {
// var list = eligList[eligibility[i]]; // Applicable set of
// // Graduation, PG, Phd,
// // PostDoc
// // alert("list : " + list + "| qual : " + ta[0]);
// var added = false;
// for (var k = 0; k < list.length; k++) {
// if (list[k].indexOf(ta[0]) !== -1) {
// added = true;
// if (elg.indexOf(eligibility[i]) === -1) {
// elg += eligibility[i] + "|";
// }
// }
// }
// }
// }
// // alert("qual : " + ta[0] + ", elg : " + elg);
//
// qual += ta[0] + "|";
// if (ta.length > 1) {
// top += ta[1].split(")")[0] + "|";
// } else {
// top += "-" + "|";
// }
// // alert("Topic : " + top);
// disc += encodeURIComponent(table.rows[r].cells[1].innerHTML) + "|";
// spec += encodeURIComponent(table.rows[r].cells[2].innerHTML) + "|";
// uni += encodeURIComponent(table.rows[r].cells[3].innerHTML) + "|";
// col += encodeURIComponent(table.rows[r].cells[4].innerHTML) + "|";
// yop += table.rows[r].cells[5].innerHTML + "|";
// div += table.rows[r].cells[6].innerHTML + "|";
// cp += table.rows[r].cells[7].innerHTML + "|";
// per += table.rows[r].cells[9].innerHTML + "|";
// // alert("Val : " +
// // ((table.rows[r].cells[10].innerHTML).split(">")[1]).split("<")[0]);
// ms += ((table.rows[r].cells[10].innerHTML).split(">")[1])
// .split("<")[0]
// + "|";
// dc += ((table.rows[r].cells[11].innerHTML).split(">")[1])
// .split("<")[0]
// + "|";
// abs += ((table.rows[r].cells[12].innerHTML).split(">")[1])
// .split("<")[0]
// + "|";
// var cgpaStr = table.rows[r].cells[8].innerHTML;
// if (cgpaStr == "-") {
// cobt += "-" + "|";
// cmax += "-" + "|";
// cf += "-" + "|";
// } else {
// var tempArr = cgpaStr.split("/");
// cobt += tempArr[0] + "|";
// var temp = tempArr[1].split("<");
// cmax += temp[0] + "|";
// var strTemp = temp[1].split(">")[1];
// if (strTemp !== "") {
// cf += strTemp + "|";
// } else {
// cf += "-" + "|";
// }
// }
// }
// elg = elg.substring(0, elg.length - 1);
// uni = uni.substring(0, uni.length - 1);
// top = top.substring(0, top.length - 1);
// col = col.substring(0, col.length - 1);
// qual = qual.substring(0, qual.length - 1);
// disc = disc.substring(0, disc.length - 1);
// spec = spec.substring(0, spec.length - 1);
// yop = yop.substring(0, yop.length - 1);
// div = div.substring(0, div.length - 1);
// cp = cp.substring(0, cp.length - 1);
// cobt = cobt.substring(0, cobt.length - 1);
// cmax = cmax.substring(0, cmax.length - 1);
// per = per.substring(0, per.length - 1);
// ms = ms.substring(0, ms.length - 1);
// dc = dc.substring(0, dc.length - 1);
// abs = abs.substring(0, abs.length - 1);
// cf = cf.substring(0, cf.length - 1);
// article.university = uni;
// article.topic = top;
// article.college = col;
// article.qualification = qual;
// article.discipline = disc;
// article.specialization = spec;
// article.year_of_passing = yop;
// article.division = div;
// article.percentage_cgpa = cp;
// article.cgpa_obt = cobt;
// article.cgpa_max = cmax;
// article.percentage = per;
// article.marksheet = ms;
// article.dc = dc;
// article.abs = abs;
// article.cf = cf;
// }
//
    elStr = elStr.substring(0, elStr.length - 1);
    article.eligibility = elStr; // eligibility
    // if (elg === "") {
    // elg = "NA";
    // }
    // article.elg = elg; // eligibility as per he_table
    // //alert("education complete");

    var exp_value = $("#experience").val();
    if (exp_value !== null) {
        article.experience = exp_value;
    }

    if (exp_value === "Yes") {
        var table = document.getElementById("ex_table_body");
        var c = table.rows.length;
        if ((table == undefined) || (c == 0)) {
            alert("Please add the experience details.");
            return;
        }
        var ss = 0, ee = 0;
        for (var r = 0; r < c; r++) {
            var s = table.rows[r].cells[6].innerHTML; // PPEG-HRD: PayDrawn shifted because of paydrwan
            var e = table.rows[r].cells[7].innerHTML; // PPEG-HRD: PayDrawn shifted because of paydrwan
            var sd = new Date(s).getTime();
            var ed = new Date(e).getTime();
            if (ss === 0) {
                ss = sd;
                ee = ed;
            }
            if (ss > sd) {
                ss = sd;
            }
            if (ee < ed) {
                ee = ed;
            }
        }
        var ms = (ee - ss);
        var hour = 60 * 60 * 1000, day = hour * 24, month = day * 30;
        var monthsT = parseInt(ms / month);
        article.exp_years = 0;
        article.exp_months = monthsT;
        if (c > 0) {
            var n = "";
            var a = "";
            var d = "";
            var w = "";
            var p = ""; // PPEG-HRD: PayDrawn
            var f = "";
            var t = "";
            var rsn = ""; // PPEG-HRD: ReasonForLeaving
            var g = "";
            var exp = "";
            for (var r = 0; r < c; r++) {
// alert("1");
                n += encodeURIComponent(table.rows[r].cells[0].innerHTML) + "|";
                g += table.rows[r].cells[1].innerHTML + "|";
                a += encodeURIComponent(table.rows[r].cells[2].innerHTML) + "|";
                d += encodeURIComponent(table.rows[r].cells[3].innerHTML) + "|";
                w += encodeURIComponent(table.rows[r].cells[4].innerHTML) + "|";
                p += encodeURIComponent(table.rows[r].cells[5].innerHTML) + "|"; // PPEG-HRD: ReasonForLeaving : all below index shifted
                f += table.rows[r].cells[6].innerHTML + "|";
                t += table.rows[r].cells[7].innerHTML + "|";
                rsn += table.rows[r].cells[8].innerHTML + "|"; // PPEG-HRD: ReasonForLeaving
                exp += ((table.rows[r].cells[9].innerHTML).split(">")[1]) // PPEG-HRD: ReasonForLeaving: Cells[7] => cells[8]
                        .split("<")[0]
                        + "|";              
            }
            n = n.substring(0, n.length - 1);
            a = a.substring(0, a.length - 1);
            d = d.substring(0, d.length - 1);
            w = w.substring(0, w.length - 1);
            p = p.substring(0, p.length - 1); // PPEG-HRD: PayDrawn
            f = f.substring(0, f.length - 1);
            t = t.substring(0, t.length - 1);
            rsn = rsn.substring(0, rsn.length - 1);// PPEG-HRD: ReasonForLeaving
            g = g.substring(0, g.length - 1);
            exp = exp.substring(0, exp.length - 1);
            // console.log("n : " + n + ", " + a + ", " + f + ", " + t);
            article.emp_name = n;
            article.emp_address = a;
            article.emp_desig = d;
            article.emp_work = w;
            article.emp_paydrawn = p; // PPEG-HRD: PayDrawn
            article.time_from = f;
            article.time_to = t;
            article.emp_reason = rsn; // PPEG-HRD: ReasonForLeaving
            article.govtExp = g;
            article.exp_cer = exp;
        }
    }

// alert("exp complete");
// alert("article : " + article);
    // Start: ChangeId: 2023120701
    $("#loadMe").modal({
        backdrop: "static", //remove ability to close modal with click
        keyboard: false, //remove option to close with keyboard
        show: true //Display loader!
    });
    // End: ChangeId: 2023120701
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr.setRequestHeader('user', encodeURIComponent(email));
                },
                url: "/eRecruitment_NRSC/LoadAdvtNos",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    $("#loadMe").modal("hide"); // ChangeId: 2023120701
                    $(document.body).css({
                        'cursor': 'default'
                    });
                    // ChangeId: 2023120701
                    setTimeout(function() {
                        alert("Problem in saving application draft. Please check again or contact website administrator");
                    }, 1000);
                    
                },
                success: function (data) {
                    $("#loadMe").modal("hide"); // ChangeId: 2023120701
                    $(document.body).css({
                        'cursor': 'default'
                    });
                    if (data.Results[0] == "invalid_session") {
                        alert("Invalid session identified. Redirecting to home page.");
                        window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                        return;
                    }

                    var msg = data.Results[1];
                    // ChangeId: 2023120701
                    setTimeout(function() {
                        alert(msg);
                      }, 1000);
                    
                }
            });
}

function openPage(fromPage, toPage) { // blue - #2262c3, grey - #777

    if (fromPage !== 'start') {
        if (document.getElementById("Personal_Details").style.display === "block") {
            fromPage = 'Personal_Details';
        } else if (document.getElementById("Educational_Qualification").style.display === "block") {
            fromPage = 'Educational_Qualification';
        } else if (document.getElementById("Experience").style.display === "block") {
            fromPage = 'Experience';
        } else if (document.getElementById("Payment").style.display === "block") { // HRD ChangeID:2023081001 PaymentChange
            fromPage = 'Payment';
        }
    }

    var flag = false;
    // alert("fromPage : " + fromPage);
    if (fromPage === 'start') {
        if (toPage === 'Payment') {
            document.getElementById("Personal_Details").style.display = "none";
            document.getElementById("Personal_Details-tab").style.backgroundColor = "#777";
            document.getElementById("Educational_Qualification").style.display = "none";
            document.getElementById("Educational_Qualification-tab").style.backgroundColor = "#777";
            document.getElementById("Experience").style.display = "none";
            document.getElementById("Experience-tab").style.backgroundColor = "#777";
            document.getElementById("Payment").style.display = "block"; // HRD ChangeID:2023081001 PaymentChange
            document.getElementById("Payment-tab").style.backgroundColor = "#D23615"; // HRD ChangeID:2023081001 PaymentChange
        }
        else {
            document.getElementById("Personal_Details").style.display = "block";
            document.getElementById("Personal_Details-tab").style.backgroundColor = "#2262c3";
            document.getElementById("Educational_Qualification").style.display = "none";
            document.getElementById("Educational_Qualification-tab").style.backgroundColor = "#777";
            document.getElementById("Experience").style.display = "none";
            document.getElementById("Experience-tab").style.backgroundColor = "#777";
            document.getElementById("Payment").style.display = "none"; // HRD ChangeID:2023081001 PaymentChange
            document.getElementById("Payment-tab").style.backgroundColor = "#777"; // HRD ChangeID:2023081001 PaymentChange
        }
        
        // Comment Start: ChangeID:2023091801 PaymentChange
        //document.getElementById("Personal_Details").style.display = "block";
        //document.getElementById("Personal_Details-tab").style.backgroundColor = "#2262c3";
        //document.getElementById("Educational_Qualification").style.display = "none";
        //document.getElementById("Educational_Qualification-tab").style.backgroundColor = "#777";
        //document.getElementById("Experience").style.display = "none";
        //document.getElementById("Experience-tab").style.backgroundColor = "#777";
        //document.getElementById("Payment").style.display = "none"; // HRD ChangeID:2023081001 PaymentChange
        //document.getElementById("Payment-tab").style.backgroundColor = "#777"; // HRD ChangeID:2023081001 PaymentChange
        // Comment End: ChangeID:2023091801 PaymentChange
        
        // $("font[color='red']").each(function() {
        // var c = this.parentNode.className;
        // if (c.indexOf("e_") === 0) {
        // if (c.indexOf("hidden") === -1) {
        // this.parentNode.className = c + " hidden";
        // }
        // }
        // });

    } else if (fromPage == 'Personal_Details') {
        flag = validatePersonalData();
    } else if (fromPage == 'Educational_Qualification') {
        
        // Start: ChangeId: 2025042304
        // flag = checkEligibility();
        if(toPage !== 'Personal_Details'){
            flag = checkEligibility();
        }
        else{
            flag = true;
        }
        // End: ChangeId: 2025042304
            
    } else if (fromPage == 'Experience') {
        // Start: ChangeId: 2025042304
        //flag = validateExperience();
        if(toPage !== 'Personal_Details' && toPage !== 'Educational_Qualification' ){
            flag = validateExperience();
        }
        else{
            flag = true;
        }
        // End: ChangeId: 2025042304
    } else if (fromPage == 'Payment') { // HRD ChangeID:2023081001 PaymentChange, define validatePayment() if required
        //flag = validatePayment();  
        alert("Operation is not allowed");
        return;
    }

    if (flag && (toPage == 'Experience') && (fromPage == 'Personal_Details')) {
        flag = checkEligibility();
        if (!flag) {
            alert("Please complete the Educational Qualification details before moving to Work Experience details");
            toPage = 'Educational_Qualification';
            flag = true;
        }
    }
// alert("flagf " + flag);
    if (flag) {
// saveDraft();
        document.getElementById(fromPage + "-tab").style.backgroundColor = "#777";
        document.getElementById(fromPage).style.display = "none";
        document.getElementById(toPage).style.display = "block";
        document.getElementById(toPage + "-tab").style.backgroundColor = "#2262c3";
        if (toPage === "Educational_Qualification") {
            if ($('#heDiv').is(':visible')) {
                hequal1(document.getElementById("ug_qualification1"));
            }
        }
        // Start: ChangeID:2023081001 PaymentChange
        if (toPage === "Payment") {
            document.getElementById(toPage + "-tab").style.backgroundColor = "#D23615";
        }
        // End: ChangeID:2023081001 PaymentChange
    } else {
        if (fromPage != 'start') {
            alert("Please complete the details on this page before moving on the other page");
        }
    }
    if (toPage == 'Experience') {
        tncCheck();
    }
}

function Nationality(that) {
    if (that.value == "Others") {
        $("#personalData :input").prop("disabled", true);
        $("#nationality").prop("disabled", false);
        alert("Only Indian citizens are eligible to apply")
    } else {
        $("#personalData :input").prop("disabled", false);
    }
}

function ExServiceCheck() {
    if ($('#category_ser').val() == "Yes") {
        document.getElementById("ifExService").style.display = "block";
        $("#servicemanDiv").show();
    } else {
        document.getElementById("upload_serviceman").name = "NA"; // ChangeId: 2025050902
        document.getElementById("ifExService").style.display = "none";
        $("#servicemanDiv").hide();
    }
}

// Start: ChangeId:2023111101
function CentralGovCheck() {
    if ($('#cgov_serv').val() == "Yes") {
        document.getElementById("ifCentralGovService").style.display = "block";
        //$("#cgov_serv_upload").show();
    } else {
        document.getElementById("ifCentralGovService").style.display = "none";
        //$("#cgov_serv_upload").hide();
    }
}
// End: ChangeId:2023111101

function PWDCheck() {
// alert($('#category_pwd').val());
    if ($('#category_pwd').val() === null
            || $('#category_pwd').val() === "null"
            || $('#category_pwd').val() == "No") {
        $("#disDiv").hide();
        $("#disDivScribe").hide(); /* ChangeId:2025041501 */
        $("#disDivCompTime").hide(); /* ChangeId:2025050807 */
        $("#category_pwd_scribe").val("No"); /* ChangeId:2025050701 */
    } else {
        $("#disDiv").show();
        $("#disDivScribe").show(); /* ChangeId:2025041501 */
        $("#disDivCompTime").show(); /* ChangeId:2025050807 */
    }
}

function MeritCheck(that) {
    if (that.value == "Yes") {
        document.getElementById("ifMeritSports").style.display = "block";
    } else {
        document.getElementById("ifMeritSports").style.display = "none";
    }
}
// function EWSCheck(that) {
// if (that.value == "EWS") {
// document.getElementById("ifEWS").style.display = "block";
// } else {
// document.getElementById("ifEWS").style.display = "none";
// }
// }

/*
 * Preview photo & signature after upload Line 62 - 152
 */
function readURL(input) {
    document.getElementById("filetype").value = "photograph";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_photograph").name = fileName;
    $('.preview').show();
    $('#photograph_preview').hide();
    $('.preview')
            .after(
                    '<img id="photograph_preview" src="#" alt="photograph" style="display:none;"/>');
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#photograph_preview').attr('src', e.target.result).width(110)
                    .height(140);
        };
        reader.readAsDataURL(input.files[0]);
        document.getElementById("PreviewPhoto").style.display = "block";
    }
}

function getPhoto() {
// $('.preview').show();
// $('.preview1').hide();
// $('#photograph_preview').hide();
// $('#signature_preview').show();
//
    $('.preview').hide();
    $('.preview1').show();
    $('#signature_preview').hide();
    $('#photograph_preview').show();
}

// function readURL1(input)
// {
// document.getElementById("upload_photograph").onchange = function ()
// {
// var reader = new FileReader();
// if(this.files[0].size>1048576)
// {
// alert("Image Size should not be greater than 1MB");
// $('#upload_photograph').wrap('<form>').closest('form').get(0).reset();
// $('#upload_photograph').unwrap();
// return false;
// }
// if(this.files[0].type.indexOf("image")==-1)
// var fileExtension = ['jpeg', 'jpg'];
// if ($.inArray($(this).val().split('.').pop().toLowerCase(), fileExtension) ==
// -1)
// {
// alert("Only jpeg/jpg format is allowed");
// $('#upload_photograph').wrap('<form>').closest('form').get(0).reset();
// $('#upload_photograph').unwrap();
// return false;
// }
// reader.readAsDataURL(this.files[0]);
// };
// }

function readSignature(input) {
    document.getElementById("filetype").value = "signature";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_signature").name = fileName;
    $('.preview1').show();
    $('#signature_preview').hide();
    $('.preview1')
            .after(
                    '<img id="signature_preview" src="#" alt="your image" style="display:none;"/>');
    if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = function (e) {
            $('#signature_preview').attr('src', e.target.result).width(200)
                    .height(50);
        };
        reader.readAsDataURL(input.files[0]);
    }
    document.getElementById("PreviewSignature").style.display = "block";
}

function read_neCert(input) {
    document.getElementById("filetype").value = "nes";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("nes_ct").name = fileName;
}

function read_rc(input) {
    document.getElementById("filetype").value = "rc";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_rc").name = fileName;
}

function read_ews(input) {
    document.getElementById("filetype").value = "ews";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_ews").name = fileName;
}

function read_disability(input) {
    document.getElementById("filetype").value = "disability";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_disability").name = fileName;
}

function read_serviceman(input) {
    document.getElementById("filetype").value = "serviceman";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_serviceman").name = fileName;
}

function read_exp(input) {
    document.getElementById("filetype").value = "exp";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("exp").name = fileName;
}

function readx_formula(input) {
    document.getElementById("filetype").value = "x_formula";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_x_formula").name = fileName;
}

function readxii_formula(input) {
    document.getElementById("filetype").value = "xii_formula";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("xii_formula").name = fileName;
}

function readiti_formula(input) {
    document.getElementById("filetype").value = "iti_formula";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("iti_formula").name = fileName;
}

function readdip_formula(input) {
    document.getElementById("filetype").value = "dip_formula";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("dip_formula").name = fileName;
}

function readx_degree_certificate(input) {
    document.getElementById("filetype").value = "x_degree_certificate";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_x_degree_certificate").name = fileName;
    // alert("Val : " + fileName);
    /*
     * $('.preview1').show(); $('#signature_preview').hide(); $('.preview1')
     * .after( '<img id="signature_preview" src="#" alt="your image"
     * style="display:none;"/>'); if (input.files && input.files[0]) { var
     * reader = new FileReader(); reader.onload = function(e) {
     * $('#signature_preview').attr('src', e.target.result).width(150)
     * .height(200); }; reader.readAsDataURL(input.files[0]); }
     * document.getElementById("PreviewSignature").style.display = "block";
     */
}

//Start PPEG-HRD MedicalRegCertificate
function readdoc_reg_certificate(input) {
    document.getElementById("filetype").value = "doc_reg_certificate";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_doc_reg_certificate").name = fileName;
}
//End PPEG-HRD MedicalRegCertificate

//Start PPEG-HRD NurseRegCertificate
function readnurse_reg_certificate(input) {
    document.getElementById("filetype").value = "nurse_reg_certificate";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_nurse_reg_certificate").name = fileName;
}
//End PPEG-HRD NurseRegCertificate

function readxii_degree_certificate(input) {
    document.getElementById("filetype").value = "xii_degree_certificate";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_xii_degree_certificate").name = fileName;
    // alert("Val : " + fileName);
}

function readiti_degree_certificate(input) {
    document.getElementById("filetype").value = "iti_degree_certificate";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_iti_degree_certificate").name = fileName;
    // alert("Val : " + fileName);
}

function readdip_degree_certificate(input) {
    document.getElementById("filetype").value = "dip_degree_certificate";
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_dip_degree_certificate").name = fileName;
    // alert("Val : " + fileName);
}

function readug_degree_certificate(input) {
    var ftype = "degree_certificate_" + $("#ug_qualification1").val();
    if ($("#ug_qualification1").val().toString().toUpperCase() === "POSTGRADUATE") {
        if (($("#ug_qualification2").val() === "M.Tech/M.E") || ($("#ug_qualification2").val() === "M.Tech"))
            ftype += "2"
        else
            ftype += "1"
    }
    document.getElementById("filetype").value = ftype;
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_ug_degree_certificate").name = fileName;
}

function readug_marksheet(input) {
// alert("marksheet:" + $("#ug_qualification2").val() );
    var ftype = "marksheet_" + $("#ug_qualification1").val();
    if ($("#ug_qualification1").val().toString().toUpperCase() === "POSTGRADUATE") {
        if (($("#ug_qualification2").val() === "M.Tech/M.E") || ($("#ug_qualification2").val() === "M.Tech"))
            ftype += "2";
        else
            ftype += "1";
    }
    document.getElementById("filetype").value = ftype;
//	document.getElementById("filetype").value = "marksheet_" + $("#ug_qualification1").val();
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_ug_marksheet").name = fileName;
    // alert("Val : " + fileName);
}

function readug_formula(input) {
    document.getElementById("filetype").value = "formula_"
            + $("#ug_qualification1").val();
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_ug_formula").name = fileName;
    // alert("Val : " + fileName);
}

function readug_abstract(input) {
    document.getElementById("filetype").value = "abstract_"
            + $("#ug_qualification1").val();
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_ug_abstract").name = fileName;
    // alert("Val : " + fileName);
}

function read_exp_certificate(input) {
    document.getElementById("filetype").value = "exp_" + $("#emp_name").val()
            + "_" + $("#time_from").val();
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("upload_exp_certificate").name = fileName;
}

function getSignature() {
    $('.preview').show();
    $('.preview1').hide();
    $('#photograph_preview').hide();
    $('#signature_preview').show();
}

// function readSignature1(input){
// document.getElementById("upload_signature").onchange = function ()
// {
// var reader = new FileReader();
// if(this.files[0].size>1048576)
// {
// alert("Image Size should not be greater than 1MB");
// $('#upload_signature').wrap('<form>').closest('form').get(0).reset();
// $('#upload_signature').unwrap();
// return false;
// }
// //if(this.files[0].type.indexOf("image")==-1)
// var fileExtension = ['jpeg', 'jpg'];
// if ($.inArray($(this).val().split('.').pop().toLowerCase(), fileExtension) ==
// -1)
// {
// alert("Only jpeg/jpg format is allowed");
// $('#upload_signature').wrap('<form>').closest('form').get(0).reset();
// $('#upload_signature').unwrap();
// return false;
// }

// //read the image file as a data URL.
// reader.readAsDataURL(this.files[0]);
// };}

function XCgpa_Per(that) {
    $("font[color='red']").each(
            function () {
                var c = this.parentNode.className;
                if (c !== "")
                    var inElId = $("." + c.split("hidden")[0])[0].parentNode.previousElementSibling.id;
                if (c.indexOf("e_") === 0) {
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("X_cgpa") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "CGPA";
                        $("#" + inElId).val("");
                        $(c).addClass("hidden");
                        return;
                    }
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("X_per") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "Percentage";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                }
            });
    if (that.value == "Percentage") {
// alert("in Per");
        document.getElementById("ifXPer").style.display = "block";
        // document.getElementById("x_percentage_1").style.display = "block";
        document.getElementById("ifXCgpa").style.display = "block"; // ChangeId 2023121901 none to block
        // document.getElementById("x_cgpa_1").style.display = "none";
    } else if (that.value == "CGPA") {
// alert("in CGPA");
        document.getElementById("ifXPer").style.display = "block"; // ChangeId 2023121901 none to block
        // document.getElementById("x_percentage_1").style.display = "none";
        document.getElementById("ifXCgpa").style.display = "block";
        // document.getElementById("x_cgpa_1").style.display = "block";
    }
    // PPEG-HRD Start PercentageCGPA
    else {
        document.getElementById("ifXPer").style.display = "block"; // ChangeId 2023121901 none to block
        document.getElementById("ifXCgpa").style.display = "block"; // ChangeId 2023121901 none to block
    }
    // PPEG-HRD End PercentageCGPA
}

function xBoard(that) {
    if (that.value == "Others") {
        document.getElementById("xBoardDiv").style.display = "block";
    } else {
        document.getElementById("xBoardDiv").style.display = "none";
    }
}

function xiiBoard(that) {
    if (that.value == "Others") {
        document.getElementById("xiiBoardDiv").style.display = "block";
    } else {
        document.getElementById("xiiBoardDiv").style.display = "none";
    }
}

function xiispc(that) {
    if (that.value == "Others") {
        document.getElementById("ifXIIspc").style.display = "block";
    } else {
        document.getElementById("ifXIIspc").style.display = "none";
    }
}

function hequal1(that) {
// alert("that : " + that + ", value : " + that.value);
    if (that.value == "Select") {
        return;
    }
    var tempArr = eligList[that.value];
    $('#ug_qualification2').empty();
    for (j = 0; j < tempArr.length; j++) {
        var qual = tempArr[j];
        $('<option value="' + qual + '">' + qual + '</option>').appendTo(
                '#ug_qualification2');
    }
    hequal2(document.getElementById("ug_qualification2"));
}

function hequal2(that) {
    
    // Start: ChangeId: 2023121902
    if($('#ug_qualification2').val() !== 'Select' 
            && $('#ug_qualification2').val() !== ''
            && $('#ug_qualification2').val() !== null)
    {
        $("#ug_per").prop("disabled",false);
        
        // Start: ChangeId: 2023122702, ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
        if((document.getElementById("post_name_applicant").value.search("Scientist")>-1) && minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"]){
            $("#he_min_percentage").text("Minimum qualifying percentage is "+minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"]+"%");
            $("#he_min_cgpa_to_percentage").text("Minimum qualifying percentage is "+minMarks[$('#ug_qualification2').val().trim()]["MinPercentage"]+"%");
        }else{
            $("#he_min_percentage").text("");
            $("#he_min_cgpa_to_percentage").text("");
        }
        // End: ChangeId: 2023122702
        
        
        
        $("#ug_cgpa_obt").prop("disabled",false);
        
        // Start: ChangeId: 2023122702, ChangeId: 2024012005 bypass percentage/cgpa validation for non scientist post
        if((document.getElementById("post_name_applicant").value.search("Scientist")>-1) && minMarks[$('#ug_qualification2').val().trim()] && minMarks[$('#ug_qualification2').val().trim()]["MinCGPA"])
            $("#he_min_cgpa").text("Minimum qualifying CGPA is "+minMarks[$('#ug_qualification2').val().trim()]["MinCGPA"]);
        else
            $("#he_min_cgpa").text("");
        // End: ChangeId: 2023122702
        
    }
    else{
        $("#ug_per").prop("disabled",true);
        $("#ug_cgpa_obt").prop("disabled",true);
    }
    // End: ChangeId: 2023121902
    
    if (((that.value).indexOf("Phd") > -1)
            || ((that.value).indexOf("PostDoc") > -1)) {
        document.getElementById("ifHEqual").style.display = "block";
        document.getElementById("ugDivDiv").style.display = "none";
        document.getElementById("ugCPDiv").style.display = "none";
        document.getElementById("ifUgPer").style.display = "block"; // ChangeId: 2023122101 none to block
        document.getElementById("ifUgCgpa").style.display = "block"; // ChangeId: 2023122101 none to block
        document.getElementById("heMarkDiv").style.display = "none";
        document.getElementById("ugAbsDiv").style.display = "block";
    } else {
        $("#ug_percentage_cgpa").val("Percentage");
        document.getElementById("ifHEqual").style.display = "none";
        document.getElementById("ugDivDiv").style.display = "block";
        document.getElementById("ugCPDiv").style.display = "none"; // ChangeId: 2023122101 block to none
        document.getElementById("ifUgPer").style.display = "block";
        document.getElementById("heMarkDiv").style.display = "block";
        document.getElementById("ifUgCgpa").style.display = "block"; // ChangeId: 2023122101 none to block
        document.getElementById("ugAbsDiv").style.display = "none";
    }

    var tabIndex = document.getElementById("indexval").value;
    // alert("Yabindex : " + tabIndex);

    var eArray = advertisementDetails[tabIndex]["Eligibility"];
    for (var j = 0; j < eArray.length; j++) {
        var elig = eArray[j]["essential_qualification"];
        // alert(elig + ", " + that.value);
        if (elig === that.value) {
// alert("Matched");
// $('#ug_discipline').empty();
            $('#ug_spec').empty();
            // $('<option value="Select" selected disabled>Select</option>')
            // .appendTo('#ug_discipline');
            $('<option value="Select" selected disabled>Select</option>')
                    .appendTo('#ug_spec');
            var discipline = eArray[j]["discipline"];
            var specialization = eArray[j]["specialization"];
            // $('<option value="' + discipline + '">' + discipline +
            // '</option>')
            // .appendTo('#ug_discipline');
            var specA = specialization.split("@");
            var isOthers = false; // ChangeId: 2024011703 
            for (var k = 0; k < specA.length; k++) {
                // Start: ChangeId: 2024011702 
                if(specA[k] !== "Others"){
                    $('<option value="' + specA[k] + '">' + specA[k] + '</option>')
                        .appendTo('#ug_spec');
                }else{
                    isOthers = true; // ChangeId: 2024011703
                }
                
                // End: ChangeId: 2024011702
            }
            // Start: ChangeId: 2024011703
            if(isOthers){
                $('<option disabled>──────────</option><option value="Others">Equivalent</option>')
                        .appendTo('#ug_spec');
            }
            // Start: ChangeId: 2024011703
        }
    }
// alert("Clearing values");
    $("#ug_division").val("Select");
    $("#ug_discipline").val("");
    $("#ug_university").val("Select");
    $("#ug_college").val("");
    // $("#ug_year_of_passing").val("");
    $("#ug_percentage").val("");
    $("#ug_cgpa_obt").val("");
    $('#ug_cgpa_max').val('10.0') // ChangeId: 2024012006
    $("#ug_percentage").val("");
    $("#ug_per").val("");
    $("#ug_percentage_cgpa").val("Select");
    $("#ug_cgpa_fromuni").val("Select");
    document.getElementById("upload_ug_formula").name = "NA";
    document.getElementById("upload_ug_formula").value = "";
    document.getElementById("ug_formula_size").innerHTML = "";
    document.getElementById("upload_ug_marksheet").name = "NA";
    document.getElementById("upload_ug_marksheet").value = "";
    document.getElementById("ug_marksheet_size").innerHTML = "";
    document.getElementById("upload_ug_degree_certificate").name = "NA";
    document.getElementById("upload_ug_degree_certificate").value = "";
    document.getElementById("ug_degree_certificate_size").innerHTML = "";
    document.getElementById("upload_ug_abstract").name = "NA";
    document.getElementById("upload_ug_abstract").value = "";
    document.getElementById("ug_abstract_size").innerHTML = "";
}

function hedisc(that) {
    if (that.value === "Others") {
        document.getElementById("ifHEdisc").style.display = "block";
    } else {
        document.getElementById("ifHEdisc").style.display = "none";
    }
}

function hespec(that) {
    if (that.value === "Others") {
        document.getElementById("ifHEspec").style.display = "block";
    } else {
        document.getElementById("ifHEspec").style.display = "none";
    }
}

function itispc(that) {
    if (that.value !== "" && that.value === "Others") {
        document.getElementById("ifitispc").style.display = "block";
    } else {
        document.getElementById("ifitispc").style.display = "none";
    }
}

function dipspc(that) {
    if (that.value == "Others") {
        document.getElementById("ifdipspc").style.display = "block";
    } else {
        document.getElementById("ifdipspc").style.display = "none";
    }
}

function netOth(that) {
    if (that.value.toString().indexOf("Other") > -1) {
        document.getElementById("ifNETOth").style.display = "block";
    } else {
        document.getElementById("ifNETOth").style.display = "none";
    }
}

function validateDoneXII() {
    var doneXIIval = $("#doneXII").val();
    if (doneXIIval === "No") {
        $("#XII_body").hide();
    } else {
        $("#XII_body").show();
        xiispc(document.getElementById("xii_specialization"));
    }
}

function validateDoneITI() {
    var doneITIval = $("#doneITI").val();
    if (doneITIval === "No") {
        $("#ITI_body").hide();
    } else {
        $("#ITI_body").show();
        itispc(document.getElementById("iti_specialization"));
    }
}

function validateDoneDIP() {
    var doneDIPval = $("#doneDIP").val();
    if (doneDIPval === "No") {
        $("#DIP_body").hide();
    } else {
        $("#DIP_body").show();
        dipspc(document.getElementById("dip_specialization"));
    }
}

function XiiCgpa_Per(that) {
    $("font[color='red']").each(
            function () {
                var c = this.parentNode.className;
                if (c !== "")
                    var inElId = $("." + c.split("hidden")[0])[0].parentNode.previousElementSibling.id;
                if (c.indexOf("e_") === 0) {
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("XII_cgpa") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "CGPA";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("XII_per") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "Percentage";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                }
            });
    if (that.value == "Percentage") {
        document.getElementById("ifXiiPer").style.display = "block";
        // document.getElementById("xii_percentage_1").style.display = "block";
        document.getElementById("ifXiiCgpa").style.display = "block"; // ChangeId: 2023122001 none to block
        // document.getElementById("xii_cgpa_1").style.display = "none";
    } else if (that.value == "CGPA") {
        document.getElementById("ifXiiPer").style.display = "block"; // ChangeId: 2023122001 none to block
        // document.getElementById("xii_percentage_1").style.display = "none";
        document.getElementById("ifXiiCgpa").style.display = "block";
        // document.getElementById("xii_cgpa_1").style.display = "block";
    }
    // PPEG-HRD Start PercentageCGPA
    else {
        document.getElementById("ifXiiPer").style.display = "block";  // ChangeId: 2023122001 none to block
        document.getElementById("ifXiiCgpa").style.display = "block"; // ChangeId: 2023122001 none to block
    }
    // PPEG-HRD End PercentageCGPA
}

function itiCgpa_Per(that) {
    $("font[color='red']").each(
            function () {
                var c = this.parentNode.className;
                if (c !== "")
                    var inElId = $("." + c.split("hidden")[0])[0].parentNode.previousElementSibling.id;
                if (c.indexOf("e_") === 0) {
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("ITI_cgpa") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "CGPA";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("ITI_per") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "Percentage";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                }
            });
    if (that.value == "Percentage") {
        document.getElementById("ifitiPer").style.display = "block";
        // document.getElementById("iti_percentage_1").style.display = "block";
        document.getElementById("ifitiCgpa").style.display = "none";
        // document.getElementById("iti_cgpa_1").style.display = "none";
    } else if (that.value == "CGPA") {
        document.getElementById("ifitiPer").style.display = "none";
        // document.getElementById("iti_percentage_1").style.display = "none";
        document.getElementById("ifitiCgpa").style.display = "block";
        // document.getElementById("iti_cgpa_1").style.display = "block";
    }
    // PPEG-HRD Start PercentageCGPA
    else {
        document.getElementById("ifitiPer").style.display = "none";
        document.getElementById("ifitiCgpa").style.display = "none";
    }
    // PPEG-HRD End PercentageCGPA
}

function dipCgpa_Per(that) {
    $("font[color='red']").each(
            function () {
                var c = this.parentNode.className;
                if (c !== "")
                    var inElId = $("." + c.split("hidden")[0])[0].parentNode.previousElementSibling.id;
                if (c.indexOf("e_") === 0) {
                    if ((c.indexOf("hidden") === -1)
//							&& (c.indexOf("ITI_cgpa") !== -1)) {
                            && (c.indexOf("dip_cgpa") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "CGPA";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                    if ((c.indexOf("hidden") === -1)
//							&& (c.indexOf("ITI_per") !== -1)) {
                            && (c.indexOf("dip_per") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "Percentage";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                }
            });
    if (that.value == "Percentage") {
        document.getElementById("ifdipPer").style.display = "block";
        // document.getElementById("dip_percentage_1").style.display = "block";
        document.getElementById("ifdipCgpa").style.display = "none";
        // document.getElementById("dip_cgpa_1").style.display = "none";
    } else if (that.value == "CGPA") {
        document.getElementById("ifdipPer").style.display = "none";
        // document.getElementById("dip_percentage_1").style.display = "none";
        document.getElementById("ifdipCgpa").style.display = "block";
        // document.getElementById("dip_cgpa_1").style.display = "block";
    }
    // PPEG-HRD Start PercentageCGPA
    else {
        document.getElementById("ifdipPer").style.display = "none";
        document.getElementById("ifdipCgpa").style.display = "none";
    }
    // PPEG-HRD End PercentageCGPA
}

function UgUniversity(that) {
    if (that.value == "Others") {
        document.getElementById("ifUgUniversity").style.display = "block";
    } else {
        document.getElementById("ifUgUniversity").style.display = "none";
    }
}

function UgCgpa_Per(that) {
    $("font[color='red']").each(
            function () {
                var c = this.parentNode.className;
                if (c !== "")
                    var inElId = $("." + c.split("hidden")[0])[0].parentNode.previousElementSibling.id;
                if (c.indexOf("e_") === 0) {
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("Higher_Education_cgpa") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "CGPA";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("Higher_Education_per") !== -1)
                            && ($("#" + inElId).val().trim() !== "")) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "Percentage";
                        $("#" + inElId).val("");
                        $("." + c).addClass("hidden");
                        return;
                    }
                }
            });
    if (that.value === "Percentage") {
        document.getElementById("ifUgPer").style.display = "block";
        document.getElementById("ifUgCgpa").style.display = "block"; // ChangeId: 2023122101 none to block
    } else if (that.value === "CGPA") {
        document.getElementById("ifUgPer").style.display = "block"; // ChangeId: 2023122101 none to block
        document.getElementById("ifUgCgpa").style.display = "block";
    }
    // PPEG-HRD Start PercentageCGPA
    else {
        document.getElementById("ifUgPer").style.display = "block"; // ChangeId: 2023122101 none to block
        document.getElementById("ifUgCgpa").style.display = "block"; // ChangeId: 2023122101 none to block
    }
    // PPEG-HRD End PercentageCGPA
}

function UgCgpa_FromUni(that) {
    $("font[color='red']").each(
            function () {
                var c = this.parentNode.className;
                if (c.indexOf("e_") === 0) {
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("Higher_Education_cgpa") !== -1)) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "CGPA";
                        return;
                    }
                    if ((c.indexOf("hidden") === -1)
                            && (c.indexOf("Higher_Education_per") !== -1)) {
                        alertError(c); // ChangeId: 2023110809 
                        //alert("Please rectify the error on the page ("
                        //        + c.replace("e_", "") + ")");
                        that.value = "Percentage";
                        return;
                    }
                }
            });
    if (that.value === "Yes") {
        document.getElementById("ug_CGPAFormDiv").style.display = "block";
        document.getElementById("ug_CGPAPerDiv").style.display = "block";
    } else if (that.value === "No") {
        document.getElementById("ug_CGPAFormDiv").style.display = "none";
        document.getElementById("ug_CGPAPerDiv").style.display = "none";
    }
}

function exp(that) {
    if (that.value == "Yes") {
        document.getElementById("ifExp").style.display = "block";
    } else {
        document.getElementById("ifExp").style.display = "none";
    }
}

/*
 * Preview form tab before submit
 */
function postOptions() {
    var option = $("#advtnodrop").val();
    $("#postnodrop").load("Option?advt=" + option);
}

function submitApplication(buttonType) {
    $(document.body).css({
        'cursor': 'wait'
    });
    var flag = validateExperience();
    if (!flag) {
        $(document.body).css({
            'cursor': 'default'
        });
        // Start: ChnageId: 2023110805
        $('#tnc').prop('checked', false);
        $("#previewButton").prop("disabled", true);
        // End: ChnageId: 2023110805
        return;
    }

    if (buttonType === "preview") { 
        $("#previewButton").prop("disabled", true);
    }

// if (buttonType === "submit") {
    $("#submitBtn_applicant").prop("disabled", true);
    // }

    // alert("submitting");
    var adv = $("#advt_no").val();
    var post = $("#post_no").val();
    var article = new Object();
    article.buttonType = buttonType;
    article.action = "submitApplicant";
    article.email = email;
    article.advt_no = $("#advt_no").val();
    article.post_no = $("#post_no").val();
    article.post_name = $("#post_name_applicant").val();
    article.salutation = $("#salutation").val();
    article.first_name = encodeURIComponent($("#first_name").val());
    article.middle_name = encodeURIComponent(" ");
    article.last_name = encodeURIComponent(" ");
    // if ($("#middle_name").val() == "") {
    // article.middle_name = encodeURIComponent("NA");
    // } else {
    // article.middle_name = encodeURIComponent($("#middle_name").val());
    // }
    // if ($("#last_name").val() == "") {
    // article.last_name = encodeURIComponent("NA");
    // } else {
    // article.last_name = encodeURIComponent($("#last_name").val());
    // }
    article.father_name = encodeURIComponent($("#father_name").val());
    article.mother_name = encodeURIComponent($("#mother_name").val());
    article.gender = $("#gender").val();
    article.marital_status = $("#marital_status").val();
    article.dob = $("#dobAppl").val();
    article.place_of_birth = $("#place_of_birth").val();
    article.nationality = $("#nationality").val();
    article.zone = "NA";
    if ($("#zone_sel").val() !== null && $("#zone_sel").val().length !== 0) { // ChangeId: 2023120104 array length checked
        article.zone = $("#zone_sel").val().join("@"); // ChangeId: 2023112001;
    } 

    article.category = $("#category").val();
    article.reservation_proof = document.getElementById("upload_rc").name;
    article.category_pwd = $("#category_pwd").val();
    /*Start: ChangeId:2025041501*/
    if(document.getElementById("category_pwd_scribe").value !== ""){
        article.category_pwd_scribe = document.getElementById("category_pwd_scribe").value; 
    }
    else
    {
        article.category_pwd_scribe = "NA";
    }
    /*End: ChangeId:2025041501*/
    
    /*Start: ChangeId:2025050807*/
    if(document.getElementById("category_pwd_comptime").value !== ""){
        article.category_pwd_comptime = document.getElementById("category_pwd_comptime").value; 
    }
    else
    {
        article.category_pwd_comptime = "NA";
    }
    /*End: ChangeId:2025050807*/
    
    article.category_pwd_certifcate = document.getElementById("upload_disability").name; /*ChangeId:2025041501*/
    article.category_ews = $("#category_ews").val();
    article.ews_cerfitificate = document.getElementById("upload_ews").name;
    article.category_exservice = $("#category_ser").val();
    article.category_exservice_from = "NA";
    article.category_exservice_to = "NA";
    article.serviceman_proof = document.getElementById("upload_serviceman").name; /*ChnageId: 2025050703*/
    article.cgov_serv = $("#cgov_serv").val();
    article.cgov_serv_doj = "NA";
    article.category_merit_sport = $("#category_spt").val();
    article.category_merit_sportname = "NA";
    article.category_merit_sportlevel = "NA";
    article.upload_dir = UPLOAD_DIR;
    article.user_id = email;
    if ($("#category_ser").val() == "Yes") {
        article.category_exservice_from = $("#category_exservice_from").val();
        article.category_exservice_to = $("#category_exservice_to").val();
    }
    // Start: ChangeId: 2023111101
    if ($("#cgov_serv").val() == "Yes") {
        article.cgov_serv_doj = $("#cgov_serv_doj").val();
    } 
    // End: ChangeId: 2023111101
    if ($("#category_spt").val() == "Yes") {
        article.category_merit_sportname = $("#category_merit_sportname").val();
        article.category_merit_sportlevel = $("#category_merit_sportlevel")
                .val();
    }

    article.house_no = encodeURIComponent($("#house_no").val());
    article.locality = encodeURIComponent($("#locality").val());
    article.state = $("#state").val();
    article.district = $("#district").val();
    article.town = $("#town").val();
    article.pincode = $("#pincode").val();
    article.sameaddressconfirmation = $("#sameaddressconfirmation").val();
    article.p_house_no = encodeURIComponent($("#p_house_no").val());
    article.p_locality = encodeURIComponent($("#p_locality").val());
    article.p_state = $('#p_state :selected').text();
    article.p_district = $("#p_district").val();
    article.p_town = $("#p_town").val();
    article.p_pincode = $("#p_pincode").val();
    article.contact_no = $("#contact_no").val();
    article.aadhaar = $("#aadhaar").val(); // PPEG-HRD AADHAAR
    article.alternate_contact = "NA";
    if ($("#alternate_contact").val() !== "") {
        article.alternate_contact = $("#alternate_contact").val();
    }
    article.nearest_railway_station = $("#nearest_railway_station").val();
    article.photograph = "NA";
    article.signature = "NA";
    article.bank_acc_beneficiary = $("#bank_acc_beneficiary").val(); // ChangeId:2025041601
    
    article.bank_acc_no = $("#bank_acc_no").val(); // ChangeId: 2023111001
    article.bank_ifsc_code = $("#bank_ifsc_code").val(); // ChangeId: 2023111001
    article.bank_acc_doc = document.getElementById("bank_acc_doc").name; // ChangeId:2025041601
    // Start: ChangeId: 2023120103
    if(document.getElementById("upload_photograph").name !== ""){
        article.photograph = document.getElementById("upload_photograph").name;
    }
    if(document.getElementById("upload_signature").name){
        article.signature = document.getElementById("upload_signature").name;
    }
    // End: ChangeId: 2023120103
    // alert("personal complete");
    var tabIndex = document.getElementById("indexval").value;
    article.doneXII = "No";
    article.doneITI = "No";
    article.doneDIP = "No";
    var doneXIIval = $("#doneXII").val();
    var doneITIval = $("#doneITI").val();
    var doneDIPval = $("#doneDIP").val();
    if ((doneXIIval !== null) && (doneXIIval !== "") && (doneXIIval === "Yes")) {
        article.doneXII = "Yes";
    }

    if ((doneITIval !== null) && (doneITIval !== "") && (doneITIval === "Yes")) {
        article.doneITI = "Yes";
    }

    if ((doneDIPval !== null) && (doneDIPval !== "") && (doneDIPval === "Yes")) {
        article.doneDIP = "Yes";
    }

    var arr = advertisementDetails[tabIndex]["Eligibility"];
    var eligibility = new Array();
    for (var j = 0; j < arr.length; j++) {
        eligibility[j] = arr[j]["eligibility"];
    }

    var elStr = "";
    for (var i = 0; i < eligibility.length; i++) {
// alert("ENTRED :" + eligibility[i]);
        elStr += eligibility[i] + "|";
        if (eligibility[i] == 'X' || eligibility[i] == "XII"
                || eligibility[i] == "ITI" || eligibility[i] == "DIPLOMA"
                || eligibility[i] == 'Graduate'
                || eligibility[i] == 'Postgraduate' || eligibility[i] == 'PhD'
                || eligibility[i] == 'PostDoctoral') {
// alert("Inside X");
            var board = document.getElementById("x_edu_board").value;
            if (board === "Others") {
                board = document.getElementById("x_board_other").value;
            }
            article.x_edu_board = board;
            article.x_school = encodeURIComponent($("#x_school").val());
            article.x_year_of_passing = $("#x_year_of_passing").val();
            if ($("#x_division").val() === "Select") {
                article.x_division = "NA";
            } else {
                article.x_division = $("#x_division").val();
            }
            // ChangeId: 2023121901
            article.x_percentage_cgpa = "NA";
            if($("#x_percentage_cgpa").val() !== "Select")
                article.x_percentage_cgpa = $("#x_percentage_cgpa").val();
            article.x_cgpa_obt = "-1"; // ChangeId: 2023121901 NA to -1
            article.x_cgpa_max = "-1"; // ChangeId: 2023121901 NA to -1
            article.x_percentage = "-1";
            article.x_cgpa_to_perc = "-1"; // ChangeId: 2023121901 newly added
            article.x_cf = "NA";
            // Start: ChangeId: 2023121901
            if ($("#x_cgpa_obt").val() !== "") {
                article.x_cgpa_obt = $("#x_cgpa_obt").val();
            }
            if ($("#x_cgpa_max").val() !== "null") {
                article.x_cgpa_max = $("#x_cgpa_max").val();
            }
            if ($("#x_percentage").val() !== "" && $("#x_cgpa_max").val() !== "10.0") { // ChangeId: 2023121901 x_cgpa_to_perc
                article.x_cgpa_to_perc = $("#x_percentage").val(); // ChangeId: 2023121901 x_cgpa_to_perc
            }
            if ($("#x_per").val() !== "") {
                article.x_percentage = $("#x_per").val();
            }
            // Start: ChangeId: 2023111607
            if ($("#upload_x_degree_certificate").val()) {
                article.x_dc = $("#upload_x_degree_certificate").val().split("\\").pop();
            }
            // End: ChangeId: 2023111607
            article.x_dc = document
                    .getElementById("upload_x_degree_certificate").name;
            // End: ChangeId: 2023121901
            //var x_value = $("#x_percentage_cgpa").val(); // ChangeId: 2023121901
            /*if (x_value == "CGPA") {
                article.x_cgpa_obt = $("#x_cgpa_obt").val();
                article.x_cgpa_max = $("#x_cgpa_max").val();
                if ($("#x_percentage").val() != "") {
                    article.x_percentage = $("#x_percentage").val();
                }
                if (document.getElementById("upload_x_formula").name !== "NA") {
                    article.x_cf = document.getElementById("upload_x_formula").name;
                }
            } else {
                article.x_percentage = $("#x_per").val();
            }
            article.x_dc = document
                    .getElementById("upload_x_degree_certificate").name;
            */
            // alert("x complete");
        }
        if (eligibility[i] == "XII" || eligibility[i] == 'Graduate'
                || eligibility[i] == 'Postgraduate' || eligibility[i] == 'PhD'
                || eligibility[i] == 'PostDoctoral') {
// alert("Inside XII");
            var board = document.getElementById("xii_edu_board").value;
            if (board === "Others") {
                board = document.getElementById("xii_board_other").value;
            }
            article.xii_edu_board = board;
            article.xii_school = encodeURIComponent($("#xii_school").val());
            var tempVal = $("#xii_specialization").val();
            if (tempVal === "Others") {
                tempVal = $("#xii_specialization_other").val();
            }
            article.xii_specialization = encodeURIComponent(tempVal);
            article.xii_year_of_passing = $("#xii_year_of_passing").val();
            if ($("#xii_division").val() === "Select") {
                article.xii_division = "NA";
            } else {
                article.xii_division = $("#xii_division").val();
            }
            // ChangeId: 2023122001
            article.xii_percentage_cgpa = "NA";
            if($("#xii_percentage_cgpa").val() !== "Select")
                article.xii_percentage_cgpa = $("#xii_percentage_cgpa").val();
            article.xii_cgpa_obt = "-1"; // ChangeId: 2023122001
            article.xii_cgpa_max = "-1"; // ChangeId: 2023122001
            article.xii_percentage = "-1";
            article.xii_cgpa_to_perc = "-1"; // ChangeId: 2023122001 newly added
            article.xii_cf = "NA";
            // Start: ChangeId: 2023122001
            if ($("#xii_cgpa_obt").val() !== "") {
                article.xii_cgpa_obt = $("#xii_cgpa_obt").val();
            }
            if ($("#xii_cgpa_max").val() !== "null") {
                article.xii_cgpa_max = $("#xii_cgpa_max").val();
            }
            if ($("#xii_percentage").val() !== "" && $("#xii_cgpa_max").val() !== "10.0") { // ChangeId: 2023122001 x_cgpa_to_perc
                article.xii_cgpa_to_perc = $("#xii_percentage").val(); // ChangeId: 2023122001 x_cgpa_to_perc
            }
            if ($("#xii_per").val() !== "") {
                article.xii_percentage = $("#xii_per").val();
            }
            article.xii_dc = document
                    .getElementById("upload_xii_degree_certificate").name;
            
            var tempVal = $("#xii_specialization").val();
            if (tempVal === "Others") {
                tempVal = $("#xii_specialization_other").val();
            } else if ((tempVal === "") || (tempVal == null)) {
                tempVal = "NA";
            }
            article.xii_specialization = encodeURIComponent(tempVal);
            /*
            var xii_value = $("#xii_percentage_cgpa").val();
            if (xii_value == "CGPA") {
                article.xii_cgpa_obt = $("#xii_cgpa_obt").val();
                article.xii_cgpa_max = $("#xii_cgpa_max").val();
                if ($("#xii_percentage").val() != "") {
                    article.xii_percentage = $("#xii_percentage").val();
                }
                if (document.getElementById("xii_formula").name.length > 0) {
                    article.xii_cf = document.getElementById("xii_formula").name;
                }
            } else {
                article.xii_percentage = $("#xii_per").val();
            }
            article.xii_dc = document
                    .getElementById("upload_xii_degree_certificate").name;*/
            
        }
        if (eligibility[i] == 'ITI') {
            article.iti_type = $("#iti").val();
            article.iti_school = encodeURIComponent($("#iti_college").val());
            article.iti_specialization = encodeURIComponent($(
                    "#iti_specialization").val());
            article.iti_year_of_passing = $("#iti_year_of_passing").val();
            if ($("#iti_division").val() === "Select") {
                article.iti_division = "NA";
            } else {
                article.iti_division = $("#iti_division").val();
            }
            article.iti_percentage_cgpa = $("#iti_percentage_cgpa").val();
            article.iti_cgpa_obt = "NA";
            article.iti_cgpa_max = "NA";
            article.iti_percentage = "-1";
            article.iti_cf = "NA";
            var iti_value = $("#iti_percentage_cgpa").val();
            if (iti_value == "CGPA") {
                article.iti_cgpa_obt = $("#iti_cgpa_obt").val();
                article.iti_cgpa_max = $("#iti_cgpa_max").val();
                if ($("#iti_percentage").val() != "") {
                    article.iti_percentage = $("#iti_percentage").val();
                }
                if (document.getElementById("iti_formula").name.length > 0) {
                    article.iti_cf = document.getElementById("iti_formula").name;
                }
            } else {
                article.iti_percentage = $("#iti_per").val();
            }
            article.iti_dc = document
                    .getElementById("upload_iti_degree_certificate").name;
        }
        if (eligibility[i] == 'DIPLOMA') {
            article.dip_type = $("#dip").val();
            article.dip_school = encodeURIComponent($("#dip_college").val());
            article.dip_specialization = encodeURIComponent($(
                    "#dip_specialization").val());
            article.dip_year_of_passing = $("#dip_year_of_passing").val();
            if ($("#dip_division").val() === "Select") {
                article.dip_division = "NA";
            } else {
                article.dip_division = $("#dip_division").val();
            }
            article.dip_percentage_cgpa = $("#dip_percentage_cgpa").val();
            article.dip_cgpa_obt = "NA";
            article.dip_cgpa_max = "NA";
            article.dip_percentage = "-1";
            article.dip_cf = "NA";
            var dip_value = $("#dip_percentage_cgpa").val();
            if (dip_value == "CGPA") {
                article.dip_cgpa_obt = $("#dip_cgpa_obt").val();
                article.dip_cgpa_max = $("#dip_cgpa_max").val();
                if ($("#dip_percentage").val() != "") {
                    article.dip_percentage = $("#dip_percentage").val();
                }
                if (document.getElementById("dip_formula").name.length > 0) {
                    article.dip_cf = document.getElementById("dip_formula").name;
                }
            } else {
                article.dip_percentage = $("#dip_per").val();
            }
            article.dip_dc = document
                    .getElementById("upload_dip_degree_certificate").name;
        }
    }

    //Nurse and doc here
    article.nurse_reg_no = "NA";
    article.nurse_council = "NA";
    article.nurse_reg_date = "NA";
    article.nurse_valid_date = "NA";
    article.doc_reg_no = "NA";
    article.doc_council = "NA"; // ChangeId:2023090402
    article.doc_reg_date = "NA";
    article.doc_valid_date = "NA";

    if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Nurse") > -1) {
        article.nurse_reg_no = $("#nurse_reg_no").val();
        article.nurse_council = $("#nurse_council").val();
        article.nurse_reg_date = $("#nurse_reg_date").val();
        article.nurse_valid_date = $("#nurse_valid_date").val();
    } else if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Medical") > -1) {
        article.doc_reg_no = $("#doc_reg_no").val();
        article.doc_council = $("#doc_council").val(); // ChangeId:2023090402
        article.doc_reg_date = $("#doc_reg_date").val();
        article.doc_valid_date = $("#doc_valid_date").val();
    }
    //Nurse and doc ends here

    var nesStr = "";
    var nesVal = $("#netSel").val();
    if ((nesVal == null) || (nesVal == "") || (nesVal == "Select")) {
        //console.log("No Nes");
        nesStr = "NA";
    } else {
        //console.log("Yes Nes");
        if (nesVal.indexOf("Other") > -1) {
            nesVal = $("#nes_sco").val();
        }
        var c = document.getElementById("nes_ct").name;
        nesStr += (nesVal + "_" + c);
    }

// alert("!!!");
// alert($('#nesDiv').is(':visible'));
// if ($('#nesDiv').is(':visible')) {
// alert("Here in nes");
// var nesVal = $("#netSel").val();
// if ((nesVal == null) || (nesVal == "") || (nesVal == "Select")) {
// console.log("No Nes");
// } else {
// console.log("Yes Nes");
// if (nesVal.indexOf("Other") > -1) {
// nesVal = $("#nes_sco").val();
// }
// var c = $("#nes_ct").val();
// nesStr += (nesVal + "_" + c + "#");
// }
// //
//		
// var x = document.getElementById("nesListDiv").childElementCount;
// for (var i = 0; i < x; i++) {
// if ($("#nes_cb" + i).prop("checked")) {
// var e = $("#nes_cb" + i).val();
// var s = $("#nes_sc" + i).val();
// var c = $("#nes_ct" + i).val();
// nesStr += (e + "_" + s + "_" + c + "#");
// }
// }
// if(nesStr === "") {
// alert("Atleast one examination is mandatory. Please select and enter
// the score");
// return;
// }
// }
// if (nesStr.length > 0) {
// nesStr = nesStr.substring(0, nesStr.length - 1);
// } else {
// nesStr = "NA";
// }
// alert("nesStr : " + nesStr);
    article.net = nesStr;
    article.edu_others = "NA";
    if ($("#he_oth").val().length > 0) {
// alert(encodeURIComponent($("#he_oth").val()));
        article.edu_others = encodeURIComponent($("#he_oth").val());
    }
    if ($("#tnc").prop("checked")) {
        article.tnc = "Yes";
    } else {
        article.tnc = "No";
    }
    var table = document.getElementById("he_table_body");
    var n = table.rows.length;
    var elg = "";
    var eAdded = "";
    for (var r = 0; r < n; r++) {
        eAdded += table.rows[r].cells[0].innerHTML;
    }
// eAdded:B.Tech/B.EM.Tech/M.E or B.ScM.Sc
// alert("eAdded : " + eAdded);
// for (var i = 0; i < eligibility.length; i++) {
// if (eligibility[i] == 'Graduate' || eligibility[i] == 'Postgraduate'
// || eligibility[i] == 'PhD' || eligibility[i] == 'PostDoctoral') {
// var list = eligList[eligibility[i]]; // Applicable set of
// // Graduation, PG, Phd,
// // PostDoc
// //alert("list : " + list + "| added : " + eAdded);
// var added = false;
// for (var k = 0; k < list.length; k++) {
// if (eAdded.indexOf(list[k]) !== -1) {
// added = true;
// if (elg.indexOf(eligibility[i]) === -1) {
// elg += eligibility[i] + "|";
// }
// }
// }
// }
// }
// elg:Graduate|Postgraduate
// alert("elg : " +elg);
    article.elg = "NA";
    if (n > 0) {
// var m = table.rows[0].cells.length;
        var el = "";
        var uni = "";
        var top = "";
        var col = "";
        var qual = "";
        var spec = "";
        var disc = "";
        var yop = "";
        var div = "";
        var cp = "";
        var cobt = "";
        var cmax = "";
        var cf = "";
        var c_univ = "";
        var per = "";
        var ms = "";
        var dc = "";
        var abs = "";
        for (var r = 0; r < n; r++) {
            var tmp = table.rows[r].cells[0].innerHTML;
            var ta = tmp.split("(");
            // for (var i = 0; i < eligibility.length; i++) {
            // if (eligibility[i] == 'Graduate'
            // || eligibility[i] == 'Postgraduate'
            // || eligibility[i] == 'PhD'
            // || eligibility[i] == 'PostDoctoral') {
            // var list = eligList[eligibility[i]]; // Applicable set of
            // // Graduation, PG, Phd,
            // // PostDoc
            // // alert("list : " + list + "| qual : " + ta[0]);
            // var added = false;
            // for (var k = 0; k < list.length; k++) {
            // if (list[k].indexOf(ta[0]) !== -1) {
            // added = true;
            // if (elg.indexOf(eligibility[i]) === -1) {
            // elg += eligibility[i] + "|";
            // }
            // }
            // }
            // }
            // }
            // alert("qual : " + ta[0] + ", elg : " + elg);

            qual += ta[0] + "|";
            if (ta.length > 1) {
                top += ta[1].split(")")[0] + "|";
            } else {
                top += "-" + "|";
            }
// alert("Topic : " + top);
            elg += table.rows[r].cells[14].innerHTML + "|";
            disc += encodeURIComponent(table.rows[r].cells[1].innerHTML) + "|";
            spec += encodeURIComponent(table.rows[r].cells[2].innerHTML) + "|";
            uni += encodeURIComponent(table.rows[r].cells[3].innerHTML) + "|";
            col += encodeURIComponent(table.rows[r].cells[4].innerHTML) + "|";
            yop += table.rows[r].cells[5].innerHTML + "|";
            div += table.rows[r].cells[6].innerHTML + "|";
            var cper = table.rows[r].cells[7].innerHTML;
            if (cper == "-") {
                cp += "-" + "|";
                cf += "-" + "|";
                c_univ += "-" + "|";
            } else {
// CGPA<a target="_blank"
// href="/eRecruitment_NRSC\Recruitment_data//applicant\applicant\abcdefg.jpg">abcdefg.jpg</a>
// tempArr[0] = CGPA (split str with <)
// tempArr[1] = a target="_blank"
// href="/eRecruitment_NRSC\Recruitment_data//applicant\applicant\abcdefg.jpg">abcdefg.jpg
// strTemp[1] = abcdefg.jpg (split tempArr[1] with >)
                var tempArr = cper.split("<"); 
                cp += tempArr[0] + "|";
                if (tempArr.length > 1) {
                    var strTemp = tempArr[1].split(">")[1]; 
                    if (strTemp !== "") {
                        cf += strTemp + "|";
                        c_univ += "Yes" + "|";
                    } else {
                        cf += "-" + "|";
                        c_univ += "No" + "|";
                    }
                } else {
                    cf += "-" + "|";
                    c_univ += "No" + "|";
                }
// alert("cp : " + cp + ", " + cf);
            }
            var cgpaStr = table.rows[r].cells[8].innerHTML;
            if (cgpaStr == "-") {
                cobt += "-" + "|";
                cmax += "-" + "|";
            } else {
// 6.7/9.0
// tempArr[0] = 6.7 (split str with /)
// tempArr[1] = remaining string
                var tempArr = cgpaStr.split("/");
                cobt += tempArr[0] + "|";
                cmax += tempArr[1] + "|";
                // 6.7/9.0<a target="_blank"
                // href="/eRecruitment_NRSC\Recruitment_data//applicant\applicant\abcdefg.jpg">abcdefg.jpg</a>
                // tempArr[0] = 6.7 (split str with /)
                // tempArr[1] = remaining string
                // temp[0] = 9.0 (split tempArr[1] with <)
                // temp[1] = a target="_blank"
                // href="/eRecruitment_NRSC\Recruitment_data//applicant\applicant\abcdefg.jpg">abcdefg.jpg
                // strTemp[1] = abcdefg.jpg (split temp[1] with >)

                // var tempArr = cgpaStr.split("/");
                // cobt += tempArr[0] + "|";
                // var temp = tempArr[1].split("<");
                // cmax += temp[0] + "|";
                // var strTemp = temp[1].split(">")[1];
                // if (strTemp !== "") {
                // cf += strTemp + "|";
                // } else {
                // cf += "-" + "|";
                // }
            }
// alert("CGPA : " + cobt + ", " + cmax);
            var tempPer = table.rows[r].cells[9].innerHTML;
            if (tempPer === "-") {
                tempPer = "-1";
            }
            per += tempPer + "|";
            // alert("Val : " +
            // ((table.rows[r].cells[10].innerHTML).split(">")[1]).split("<")[0]);
            ms += ((table.rows[r].cells[10].innerHTML).split(">")[1]).split("<")[0] + "|";
            dc += ((table.rows[r].cells[11].innerHTML).split(">")[1]).split("<")[0] + "|";
            abs += ((table.rows[r].cells[12].innerHTML).split(">")[1]).split("<")[0]+ "|";
        }
        elg = elg.substring(0, elg.length - 1);
        uni = uni.substring(0, uni.length - 1);
        top = top.substring(0, top.length - 1);
        col = col.substring(0, col.length - 1);
        qual = qual.substring(0, qual.length - 1);
        disc = disc.substring(0, disc.length - 1);
        spec = spec.substring(0, spec.length - 1);
        yop = yop.substring(0, yop.length - 1);
        div = div.substring(0, div.length - 1);
        cp = cp.substring(0, cp.length - 1);
        cobt = cobt.substring(0, cobt.length - 1);
        cmax = cmax.substring(0, cmax.length - 1);
        per = per.substring(0, per.length - 1);
        ms = ms.substring(0, ms.length - 1);
        dc = dc.substring(0, dc.length - 1);
        abs = abs.substring(0, abs.length - 1);
        cf = cf.substring(0, cf.length - 1);
        c_univ = c_univ.substring(0, c_univ.length - 1);
        article.university = uni;
        article.topic = top;
        article.college = col;
        article.qualification = qual;
        article.discipline = disc;
        article.specialization = spec;
        article.year_of_passing = yop;
        article.division = div;
        article.percentage_cgpa = cp;
        article.cgpa_obt = cobt;
        article.cgpa_max = cmax;
        article.percentage = per;
        article.marksheet = ms;
        article.dc = dc;
        article.abs = abs;
        article.cf = cf;
        article.c_univ = c_univ;
        article.elg = elg;
    }

    elStr = elStr.substring(0, elStr.length - 1);
    article.eligibility = elStr; // eligibility
    // if (elg === "") {
    // elg = "NA";
    // }
    // article.elg = elg; // eligibility as per he_table

    var exp_value = $("#experience").val();
    article.experience = exp_value;
    article.exp_years = "NA";
    article.exp_months = "NA";
    article.emp_name = "NA";
    article.emp_address = "NA";
    article.emp_desig = "NA";
    article.emp_work = "NA";
    article.emp_paydrawn = "NA"; // PPEG-HRD: PayDrawn
    article.emp_reason = "NA"; // PPEG-HRD: ReasonForLeaving
    article.time_from = "NA";
    article.time_to = "NA";
    article.exp_cer = "NA";
    article.govtExp = "NA";
    if (exp_value === "Yes") {
        var table = document.getElementById("ex_table_body");
        // alert("Table : " + table);
        var c = table.rows.length;
        var ss = 0, ee = 0;
        for (var r = 0; r < c; r++) {
            var s = table.rows[r].cells[6].innerHTML; // PPEG-HRD: PayDrawn shifted because of paydrwan
            var e = table.rows[r].cells[7].innerHTML; // PPEG-HRD: PayDrawn shifted because of paydrwan
            var sd = new Date(s).getTime();
            var ed = new Date(e).getTime();
            if (ss === 0) {
                ss = sd;
                ee = ed;
            }
            if (ss > sd) {
                ss = sd;
            }
            if (ee < ed) {
                ee = ed;
            }
        }
        var ms = (ee - ss);
        var hour = 60 * 60 * 1000, day = hour * 24, month = day * 30;
        // alert("ms : " + ms + ", month : " + month);
        var monthsT = parseInt(ms / month);
        // var months = ((document.getElementById("exp_years").value) * 12)
        // + parseInt(document.getElementById("exp_months").value);
        // alert("month sum : " + monthsT + ", months total : " + months);
        // if (monthsT != months) {
        // alert("The experience details duration is not same as the total
        // duration. Please check!");
        // return false;
        // }
        // alert("monthsT : " + monthsT);
        article.exp_years = 0;
        article.exp_months = monthsT;

        //Nurse and doc here
        if (advertisementDetails[tabIndex]["post_name"].toString().indexOf("Medical") > -1) {
            if (monthsT < 24) {
                alert("The experience required is of minimum 2 years. Cannot proceed.");
                // Start: ChangeId:2023082902
                $(document.body).css({
                    'cursor': 'default'
                });
                // End: ChangeId:2023082902
                return;
            }
        }
        //Nurse and doc ends here

        if (c > 0) {
            var n = "";
            var a = "";
            var d = "";
            var w = "";
            var p = ""; // PPEG-HRD: PayDrawn
            var f = "";
            var t = "";
            var rsn = ""; // PPEG-HRD: ReasonForLeaving
            var g = "";
            var exp = "";
            for (var r = 0; r < c; r++) {
                n += encodeURIComponent(table.rows[r].cells[0].innerHTML) + "|";
                g += table.rows[r].cells[1].innerHTML + "|";
                a += encodeURIComponent(table.rows[r].cells[2].innerHTML) + "|";
                d += encodeURIComponent(table.rows[r].cells[3].innerHTML) + "|";
                w += encodeURIComponent(table.rows[r].cells[4].innerHTML) + "|";
                p += encodeURIComponent(table.rows[r].cells[5].innerHTML) + "|"; // PPEG-HRD: PayDrawn
                f += table.rows[r].cells[6].innerHTML + "|";
                t += table.rows[r].cells[7].innerHTML + "|";
                rsn += table.rows[r].cells[8].innerHTML + "|"; // PPEG-HRD: ReasonForLeaving
                exp += ((table.rows[r].cells[9].innerHTML).split(">")[1]).split("<")[0] + "|"; // PPEG-HRD: ReasonForLeaving cells[7] => cells[9] 
            }
            n = n.substring(0, n.length - 1);
            a = a.substring(0, a.length - 1);
            d = d.substring(0, d.length - 1);
            w = w.substring(0, w.length - 1);
            p = p.substring(0, p.length - 1); // PPEG-HRD: PayDrawn
            f = f.substring(0, f.length - 1);
            t = t.substring(0, t.length - 1);
            rsn = rsn.substring(0, rsn.length - 1); // PPEG-HRD: ReasonForLeaving
            g = g.substring(0, g.length - 1);
            exp = exp.substring(0, exp.length - 1);
            // console.log("n : " + n + ", " + a + ", " + f + ", " + t);
            article.emp_name = n;
            article.emp_address = a;
            article.emp_desig = d;
            article.emp_work = w;
            article.emp_paydrawn = p; // PPEG-HRD: PayDrawn
            article.time_from = f;
            article.time_to = t;
            article.emp_reason = rsn;// PPEG-HRD: ReasonForLeaving
            article.govtExp = g;
            article.exp_cer = exp;
        }
    }
    // Start: ChangeId: 2023120701
    $("#loadMe").modal({
        backdrop: "static", //remove ability to close modal with click
        keyboard: false, //remove option to close with keyboard
        show: true //Display loader!
    });
    // End: ChangeId: 2023120701
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $.ajax({
        beforeSend: function (xhr) {
            xhr.setRequestHeader('user', encodeURIComponent(email));
        },
        url: "/eRecruitment_NRSC/LoadAdvtNos",
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(article),
        contentType: 'application/json',
        mimeType: 'application/json',
        error: function (data, status, er) {   
            $("#loadMe").modal("hide"); // ChangeId: 2023120701
            $(document.body).css({
                'cursor': 'default'
            });
            if (buttonType === "submit") {
                // ChangeId: 2023120701
                setTimeout(function() {
                    alert("Problem in Submitting Application. Please check again or contact website administrator");
                }, 1000);
                
                $("#submitBtn_applicant").prop("disabled", false);
            }
            if (buttonType === "preview") {
                // ChangeId: 2023120701
                setTimeout(function() {
                    alert("Problem in Application Preview. Please check again or contact website administrator");
                }, 1000);
                
                $("#previewButton").prop("disabled", false);
                $("#submitBtn_applicant").prop("disabled", true);
            }
            // Start: ChangeID:2023081001 PaymentChange
            if (buttonType === "payment") {
                // ChangeId: 2023120701
                setTimeout(function() {
                    alert("Problem redirecting payment page. Please check again or contact website administrator");
                }, 1000);
                
                $("#previewButton").prop("disabled", false);
                $("#paymentBtn_applicant").prop("disabled", true);
            }
            // End: ChangeID:2023081001 PaymentChange
        },
        success: function (data) {
            $("#loadMe").modal("hide"); // ChangeId: 2023120701
            $(document.body).css({
                'cursor': 'default'
            });
            var action = data.Results[0];
            if (data.Results[0] == "invalid_session") {
                // ChangeId: 2023120701
                setTimeout(function() {
                    alert("Invalid session identified. Redirecting to home page.");
                }, 1000);
                
                window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                return;
            }

            if (action === "preview") {  
                $("#previewButton").prop("disabled", false);
                // alert("Pdf path : " + messageStr);
                var messageStr = data.Results[1];
                if (messageStr.startsWith("Error")) {
                    // ChangeId: 2023120701
                    setTimeout(function() {
                        alert(messageStr);
                    }, 1000);
                    
                } else {
                    //$("#submitBtn_applicant").prop("disabled", false); // ChangeID:2023081001 PaymentChange
                    $("#paymentBtn_applicant").prop("disabled", false); // ChangeID:2023081001 PaymentChange
                    //var element = document.createElement('a');
                    // Start: ChangeId: 2023110402 Copy preview file to server path
                    //var tempPath = UPLOAD_DIR + "/"
                    //        + document.getElementById("advt_no").value
                    //        + "/"
                    //        + document.getElementById("post_no").value
                    //        + "/";
                    //element.href = tempPath + "/" + messageStr;
                    // element.href = "downloads/"+messageStr;
                    // End: ChangeId: 2023110402 Copy preview file to server path
                    //element.target = "_blank";
                    //element.style.display = 'none';
                    //document.body.appendChild(element);
                    //element.click();
                    //document.body.removeChild(element);
                    download(messageStr); // ChangeId: 2023111401
                }
            } else if (action === "error") {
                // ChangeId: 2023120701
                setTimeout(function() {
                    alert(data.Results[1]);
                }, 1000);
                
                // $("#submitBtn_applicant").prop("disabled", false);

            } else if (action === "submit") {
                // $("#submitBtn_applicant").prop("disabled", false);
                var messageStr = data.Results[1];
                $('#messageDiv').show();
                document.getElementById("messageDiv").innerHTML = messageStr;
                if (messageStr.toString().indexOf("Sorry") > -1) {
                    alert(messageStr);
                }
                $('#personalData').hide();
                $('#displaybuttons').hide();
                var table = document.getElementById('applicant_table');
                var n = table.rows.length;
                for (var r = 1; r < n; r++) {
                    var a = table.rows[r].cells[1].innerHTML;
                    var p = table.rows[r].cells[2].innerHTML;
                    // alert("a : " + a + ", " + adv + ", p : " + p + ",
                    // " + post);
                    if ((a === adv) && (p === post)) {
                        // alert("Matched");
                        table.rows[r].style.backgroundColor = ""; // assign
                        var tabCell = table.rows[r].cells[7]; // ChangeId: 2024010101 6->7
                        // tabCell.innerHTML = data.Results[2];

                        var tempPath = UPLOAD_DIR.split("applicant")[0]
                                + "/admin/";
                        var zipPath_admin = tempPath + adv + "/" + post
                                + "/" + data.Results[2] + ".zip";
                        var btnR = document.createElement('a');
                        btnR.href = zipPath_admin;
                        // btnR.href = data.Results[3];
                        btnR.target = "_blank";
                        btnR.innerHTML = data.Results[2] + " / "
                                + data.Results[4];
                        tabCell.innerHTML = "";
                        tabCell.appendChild(btnR);
                        table.rows[r].cells[8].innerHTML = data.Results[5]; // ChangeId: 2024010101 7->8
                    }
                }
                advertisementDetails[tabIndex]["Reg_Id"] = data.Results[2];
                advertisementDetails[tabIndex]["Applied_Date"] = data.Results[4];
                advertisementDetails[tabIndex]["Preview_File"] = data.Results[3];
                advertisementDetails[tabIndex]["no_of_attempts"] = data.Results[5];
                // var element = document.createElement('a');
                // element.href = data.Results[3];
                // element.target = "_blank";
                // element.style.display = 'none';
                // document.body.appendChild(element);
                // element.click();
                // document.body.removeChild(element);

            }
            // Start: ChangeID:2023081001 PaymentChange
            else if (action === "payment") {
                // $("#submitBtn_applicant").prop("disabled", false);
                var messageStr = data.Results[1];
                $('#messageDiv').show();
                document.getElementById("messageDiv").innerHTML = messageStr;
                if (messageStr.toString().indexOf("Sorry") > -1) {
                    alert(messageStr);
                }
                //$('#personalData').hide();
                $('#displaybuttons').hide();
                var table = document.getElementById('applicant_table');
                var n = table.rows.length;
                for (var r = 1; r < n; r++) {
                    var a = table.rows[r].cells[1].innerHTML;
                    var p = table.rows[r].cells[2].innerHTML;
                    // alert("a : " + a + ", " + adv + ", p : " + p + ",
                    // " + post);
                    if ((a === adv) && (p === post)) {
                        // alert("Matched");
                        table.rows[r].style.backgroundColor = ""; // assign
                        var tabCell = table.rows[r].cells[7]; // ChangeId: 2024010101 6->7
                        // tabCell.innerHTML = data.Results[2];
                        // Start: ChangeId: 2023111401 Zip download link replaced with preview link 
                        /*
                        var tempPath = UPLOAD_DIR.split("applicant")[0]
                                + "/admin/";
                        var zipPath_admin = tempPath + adv + "/" + post
                                + "/" + data.Results[2] + ".zip";
                        var btnR = document.createElement('a');
                        btnR.href = zipPath_admin;
                        // btnR.href = data.Results[3];
                        btnR.target = "_blank";
                        btnR.innerHTML = data.Results[2] + " / "
                                + data.Results[4];
                        tabCell.innerHTML = "";
                        tabCell.appendChild(btnR);*/
                        var btnR = document.createElement('a');
                        function downloadFile() {
                            var idx = this.id.replace("previewlink","");
                            var filename = advertisementDetails[idx]["Preview_File"].replace(/^.*[\\/]/, '');
                            var advt_no = advertisementDetails[idx]["advt_no"];
                            var post_no = advertisementDetails[idx]["post_no"];

                            download(filename,advt_no,post_no);
                        }
                        btnR.href = "#";
                        btnR.onclick = downloadFile;
                        btnR.id = "previewlink"+(r-1);
                        btnR.innerHTML = data.Results[2] + " / "
                                + data.Results[4];
                        tabCell.innerHTML = "";
                        tabCell.appendChild(btnR);
                        //tabCell.innerHTML = data.Results[2] + " / "
                            //+ data.Results[4];
                        //table.rows[r].cells[7].innerHTML = data.Results[5];
                        table.rows[r].cells[8].innerHTML = "<span style='color:red;'>Payment Due</span>"; // ChangeId: 2024010101 7->8
                        // End: ChangeId: 2023111401 Zip download link replaced with preview link 
                        
                    }
                }
                advertisementDetails[tabIndex]["Reg_Id"] = data.Results[2];
                advertisementDetails[tabIndex]["Applied_Date"] = data.Results[4];
                advertisementDetails[tabIndex]["Preview_File"] = data.Results[3];
                advertisementDetails[tabIndex]["no_of_attempts"] = data.Results[5];
                
                openPage('Experience', 'Payment');
                document.getElementById("pay_email").value = document.getElementById("email_inp").value;;
                document.getElementById("pay_registrationid").value = advertisementDetails[tabIndex]["Reg_Id"];
                document.getElementById("pay_advt_no").value = document.getElementById("advt_no").value;
                document.getElementById("pay_post_no").value = document.getElementById("post_no").value;
                document.getElementById("pay_post_name").value = document.getElementById("post_name_applicant").value;
                document.getElementById("pay_amount").value = "750 INR"; // ChangeId: 2023110806
                
                $("#svDraft").hide();;
                
                document.getElementById("pay_registrationid_gateway").value = advertisementDetails[tabIndex]["Reg_Id"]+"-"+Date.now(); // ChangeId: 2023110202 Timestamp was missing in direct payment path
                document.getElementById("pay_advt_no_gateway").value = $('#pay_advt_no').val(); //ChangeId: 2023120102
                document.getElementById("pay_post_no_gateway").value = $('#pay_post_no').val(); //ChangeId: 2023120102
                document.getElementById("pay_email_gateway").value = $('#pay_email').val(); //ChangeId: 2025040701
                // Start: ChangeId: 2023120101
                var queryString = document.location.search;
                const urlParams = new URLSearchParams(queryString);
                var aid = urlParams.get("aid");
                // End: ChangeId: 2023120101
                document.getElementById("aid_gateway").value = aid; //ChangeId: 2023120101
                $("#pay_form").show();
                
            }
            // End: ChangeID:2023081001 PaymentChange
            else {
                alert("Unknown result. Please contact website administrator");
            }
        }
    });
}

// Start: ChangeId:2023101301

function submitHome(buttonType) {
    $(document.body).css({
        'cursor': 'wait'
    });

    $("#homeSubmitBtn").prop("disabled", true);


    var adv = $("#advt_no").val();
    var post = $("#post_no").val();
    var article = new Object();
    article.buttonType = buttonType;
    article.action = "submitApplicantHome";
    article.email = email;
    article.advt_no = $("#advt_no").val();
    article.post_no = $("#post_no").val();
    article.regId = $("#regid_context").val();
    

    
    article.user_id = email;
    article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $.ajax({
        beforeSend: function (xhr) {
            xhr.setRequestHeader('user', encodeURIComponent(email));
        },
        url: "/eRecruitment_NRSC/LoadAdvtNos",
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(article),
        contentType: 'application/json',
        mimeType: 'application/json',
        error: function (data, status, er) {
            $(document.body).css({
                'cursor': 'default'
            });
            alert("Problem in Submitting Application. Please check again or contact website administrator");
            $("#submitBtn_applicant").prop("disabled", false);
        },
        success: function (data) {
            $(document.body).css({
                'cursor': 'default'
            });

            if (data.Results[0] == "invalid_session") {
                alert("Invalid session identified. Redirecting to home page.");
                window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                return;
            }
            // Start: ChnageId: 2023111606
            alert(data.Results[0]);
            if (data.Results[0] === "Your application is successfully submitted")
            {
                createDynamicTable();
            }
            // End: ChnageId: 2023111606
            /*
            var messageStr = data.Results[0];
            $('#messageDiv').show();
            document.getElementById("messageDiv").innerHTML = messageStr;
            alert(messageStr);
            $('#personalData').hide();
            $('#displaybuttons').hide();
            var table = document.getElementById('applicant_table');
            var n = table.rows.length;
            for (var r = 1; r < n; r++) {
                var a = table.rows[r].cells[1].innerHTML;
                var p = table.rows[r].cells[2].innerHTML;

                if ((a === adv) && (p === post)) {

                    table.rows[r].style.backgroundColor = ""; // assign
                    var tabCell = table.rows[r].cells[6];

                    var tempPath = UPLOAD_DIR.split("applicant")[0]
                            + "/admin/";
                    var zipPath_admin = tempPath + adv + "/" + post
                            + "/" + data.Results[2] + ".zip";
                    var btnR = document.createElement('a');
                    btnR.href = zipPath_admin;
                    btnR.target = "_blank";
                    btnR.innerHTML = data.Results[2] + " / "
                            + data.Results[4];
                    tabCell.innerHTML = "";
                    tabCell.appendChild(btnR);
                    table.rows[r].cells[7].innerHTML = data.Results[5];
                }
            }
            advertisementDetails[tabIndex]["Reg_Id"] = data.Results[2];
            advertisementDetails[tabIndex]["Applied_Date"] = data.Results[4];
            advertisementDetails[tabIndex]["Preview_File"] = data.Results[3];
            advertisementDetails[tabIndex]["no_of_attempts"] = data.Results[5];*/
        }
    });
}
// End: ChangeId:2023101301
// Start: ChangeId: 2023121402
function confirmRePaymentAttempt(event){
    // Start: ChangeId: 2023121402
    var resp;
    switch($("#application_status").val()){
        case 'pending':
            resp = confirm("IMPORTANT! Your previous transaction is pending from bank end! Confirm you want to attempt another payment?");break; // ChangeId: 2025042305 break added
        case 'forwarded':
            resp = confirm("IMPORTANT! We are still trying to get the status of your last transaction! Confirm you want to attempt another payment?");break; // ChangeId: 2025042305 break
    }
    if( resp === false){
        event.stopPropagation();
        return false;
    }
    return true;
    // End: ChangeId: 2023121402
}
// End: ChangeId: 2023121402
// Start: ChangeID:2023091801
function homeToPayment(){
    
    document.getElementById('personalData').style.display = "block";
    openPage('start', 'Payment');
    document.getElementById("pay_email").value = document.getElementById("email_inp").value;
    document.getElementById("pay_registrationid").value = $('#regid_context').val();
    document.getElementById("pay_advt_no").value = document.getElementById("advt_no").value;
    document.getElementById("pay_post_no").value = document.getElementById("post_no").value;
    document.getElementById("pay_post_name").value = document.getElementById("post_name_applicant").value;
    document.getElementById("pay_amount").value = "750 INR"; // ChangeId: 2023110806

    $("#svDraft").hide();;

    document.getElementById("pay_registrationid_gateway").value = $('#regid_context').val()+"-"+Date.now();
    document.getElementById("pay_advt_no_gateway").value = $('#pay_advt_no').val(); //ChangeId: 2023120102
    document.getElementById("pay_post_no_gateway").value = $('#pay_post_no').val(); //ChangeId: 2023120102
    document.getElementById("pay_email_gateway").value = $('#pay_email').val(); //ChangeId: 2025040701
    // Start: ChangeId: 2023120101
    var queryString = document.location.search;
    const urlParams = new URLSearchParams(queryString);
    var aid = urlParams.get("aid");
    // End: ChangeId: 2023120101
    document.getElementById("aid_gateway").value = aid; //ChangeId: 2023120101

    $("#pay_form").show();
    window.scrollTo(0, document.body.scrollHeight); // ChangeId: 2024011603
}
// End: ChangeID:2023091801

// Start: ChangeID:2023101201
function homeToSubmit(){
    submitHome('submitApplicantHome');
}
// End: ChangeID:2023101201

function tncCheck() {
    if ($("#tnc").prop("checked")) {
        $("#previewButton").prop("disabled", false);
    } else {
        $("#previewButton").prop("disabled", true);
    }
}

function personalData() {
    document.getElementById("personalData").style.display = "block";
}

function changeGovtExp() {
// alert("jdf : " + $("#govtExp").val());
    if ($("#govtExp").val() === "Yes") {
        $("#nocExp").show();
    } else if ($("#govtExp").val() === "No") {
        $("#nocExp").hide();
    }
}

function checkAcceptConditions() {
    var chk = $("#accCheck").prop("checked");
    if (!chk) {
        $("#accDeclare").css("color", "ff2929");
        alert("Read the instructions and complete the declaration by checking the checkbox before proceeding!");
    } else {
        $("#accDeclare").css("color", "3276b1");
        $('#acceptconditions').modal('toggle');
        openTab('openings');
    }
}


// Start: ChangeId: 2023112302
function ctrlxFormulaUpload(){
    if( $("#x_percentage").val() && $("#x_percentage").val().trim() !== "" ){
        $("#upload_x_formula").prop("disabled",false);
    }
    else{
        $("#upload_x_formula").prop("disabled",true);
    }
}
// End: ChangeId: 2023112302

// Start: ChangeId: 2023112302
function ctrlxiiFormulaUpload(){
    if( $("#xii_percentage").val() && $("#xii_percentage").val().trim() !== "" ){
        $("#xii_formula").prop("disabled",false);
    }
    else{
        $("#xii_formula").prop("disabled",true);
    }
}
// End: ChangeId: 2023112302

// Start: ChangeId: 2023112302
function ctrldipFormulaUpload(){
    if( $("#dip_percentage").val() && $("#dip_percentage").val().trim() !== "" ){
        $("#dip_formula").prop("disabled",false);
    }
    else{
        $("#dip_formula").prop("disabled",true);
    }
}
// End: ChangeId: 2023112302

function downloadGeneralInstruction(fname) {
    download(fname);
}

//Start: ChangeId:2025041601
function readBankAccDoc(input) {
    var ftype = "bank_acc_doc"; 
    document.getElementById("filetype").value = ftype;
    var fileName = $(input).val().split("\\").pop();
    document.getElementById("bank_acc_doc").name = fileName;
}
//End: ChangeId:2025041601

// Start: ChangeId:2025050903
function ScribeCheck() {
    if ($('#category_pwd_scribe').val() === "Yes") {
        document.getElementById("div_scribe_yes").style.display = "block";
    } else {
        document.getElementById("div_scribe_yes").style.display = "none";
    }
}
function CompTimeCheck() {
    if ($('#category_pwd_comptime').val() === "Yes") {
        document.getElementById("div_comptime_yes").style.display = "block";
    } else {
        document.getElementById("div_comptime_yes").style.display = "none";
    }
}
// End: ChangeId:2025050903