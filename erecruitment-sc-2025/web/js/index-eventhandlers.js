/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/* ChangeId: ChangeId: 2024010801 File Newly added*/
document.getElementById("sendOtpBtn_l").addEventListener("click", sendOTP_login);
document.getElementById("captcha_reload").addEventListener("click", createCaptcha);
document.getElementById("reOtpBtn_l").addEventListener("click", resendOTP_login);
document.getElementById("btnLogin").addEventListener("click", login);
document.getElementById("lblSignUp").addEventListener("click", function(){
    displayDiv('signup', '');
});
document.getElementById("sendOtpBtn_s").addEventListener("click", sendOTP_signup);
document.getElementById("reOtpBtn_s").addEventListener("click", resendOTP_signup);
document.getElementById("lblExistingLogin").addEventListener("click", function(){
    displayDiv('login', '');
});
document.getElementById("btnSignUp").addEventListener("click", signup);
document.getElementById("reOtpBtn").addEventListener("click", resendOTP_forgot);
document.getElementById("sendOtpBtn").addEventListener("click", sendOTP_forgot);

document.getElementById("lblBackToLogin").addEventListener("click", function(){
    displayDiv('login', '');
});
document.getElementById("upPass").addEventListener("click", updatePass);

document.getElementById("aGeneralInstr").addEventListener("click", function(){
    downloadGeneralInstruction('#erecruitment_general_instructions.pdf');
});

//change id: 2025052002 start
document.getElementById("aDownloadFAQ").addEventListener("click", function(){
    download('#erecruitment_FAQ.pdf');
}); 
//change id: 2025052002 end

document.getElementById("lblForgotPassword").addEventListener("click", function(){
    displayDiv('forgot', '');
});
document.getElementById("dob").addEventListener("change", resetOTP_signup);
document.getElementById("email_signup").addEventListener("change", resetOTP_signup);
document.getElementById("pass_repeat").addEventListener("change", matchPasswords);
document.getElementById("mobile").addEventListener("change", resetOTP_signup);

// ChangeId: 2025050707
document.getElementById("password_field").addEventListener("change", matchPasswords);
 
  

 