/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/* ChangeId: ChangeId: 2024010801 File Newly added*/
function activateAccount() {
    var key = "param";
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
            function (m, key, value) {
                vars[key] = value;
            });
    if (vars["param"] != undefined) {
        //alert("Value : " + vars["param"]);
        var article = new Object();
        article.action = "activate";
        article.email = vars["param"];
        article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
        $
                .ajax({
                    beforeSend: function (xhr) {
                        xhr.setRequestHeader('user',
                                encodeURIComponent("user"));
                    },
                    url: "/eRecruitment_NRSC/LoginServlet",
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(article),
                    contentType: 'application/json',
                    mimeType: 'application/json',
                    error: function (data, status, er) {
                        alert("Internal error occurred. Please check again or contact website administrator");
                    },
                    success: function (data) {
                        var actionStr = data.Results[0];
                        if (data.Results[0] == "invalid_session") {
                            // 12345
                            alert("Invalid session identified. Redirecting to home page.");
                            window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
                            return;
                        }
                        var messageStr = data.Results[1];
                        if (actionStr === "error") {
                            alert(messageStr);
                        } else if (actionStr === "success") {
                            alert(messageStr);
                        }
                        window.location.href = 'https://apps.nrsc.gov.in/eRecruitment_NRSC/'; // ChangeId: 2025042203
                        //window.open("https://www.w3schools.com");
                    }
                });
    } else {
    }
    getAppType();
    createCaptcha();//ChangeId: 2023120605, 2023121601
}

function getAppType() {
    $("#signUpLabelDiv").show();
    var article = new Object();
    article.action = "getUserType";
    //		article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
    $
            .ajax({
                beforeSend: function (xhr) {
                    xhr
                            .setRequestHeader('user',
                                    encodeURIComponent("user"));
                },
                url: "/eRecruitment_NRSC/LoginServlet",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(article),
                contentType: 'application/json',
                mimeType: 'application/json',
                error: function (data, status, er) {
                    alert("Internal error occurred! Please check again or contact website administrator. Code:appType");
                },
                success: function (data) {
                    var actionStr = data.Results[0];
                    if (actionStr == "applicant") {
                        $("#signUpLabelDiv").show();
                    } else if (actionStr == "admin") {
                        $("#signUpLabelDiv").hide();
                    } else {
                        console.log("Unidentified string : " + actionStr);
                    }
                }
            });
}
window.onload = activateAccount;
