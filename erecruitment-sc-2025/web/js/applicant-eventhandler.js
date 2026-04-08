/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

document.getElementById("aChangePassword").addEventListener("click", function(){
    openTab('cp');
});
document.getElementById("aLogout").addEventListener("click", logout);
document.getElementById("cpButton").addEventListener("click", changePass);
document.getElementById("viewAdvButton").addEventListener("click", openAdv);
document.getElementById("homeMakePaymentDiv").addEventListener("click", function(event){
    confirmRePaymentAttempt(event)
});
document.getElementById("mdlResubmitConfirm").addEventListener("click", reSubmitApplication);
document.getElementById("mdlDeclarationConfirm").addEventListener("click", displayPostFields);


document.getElementById("Personal_Details-tab").addEventListener("click", function(){
    openPage('Experience', 'Personal_Details');
});
document.getElementById("Educational_Qualification-tab").addEventListener("click", function(){
    openPage('Personal_Details', 'Educational_Qualification');
});
document.getElementById("Experience-tab").addEventListener("click", function(){
    openPage('Educational_Qualification', 'Experience');
});

document.getElementById("sameaddressconfirmation").addEventListener("click", function(){
    CheckAddress(this.form);
});
document.getElementById("inpPreviewPhoto").addEventListener("click", getPhoto);
document.getElementById("inpPreviewSignature").addEventListener("click", getSignature);

//document.getElementById("btnPD_Back").addEventListener("click", null);
document.getElementById("btnPD_SaveDraft").addEventListener("click", saveDraft);
document.getElementById("next_personal").addEventListener("click", function(){
    openPage('Personal_Details', 'Educational_Qualification');
});

document.getElementById("addHE").addEventListener("click", addHErow);

document.getElementById("back_education").addEventListener("click", function(){
    openPage('Educational_Qualification', 'Personal_Details');
});
document.getElementById("savedraft_ed").addEventListener("click", saveDraft);
document.getElementById("next_education").addEventListener("click", function(){
    openPage('Educational_Qualification', 'Experience');
});
document.getElementById("btnAddExp").addEventListener("click", addEXrow);
document.getElementById("tnc").addEventListener("click", tncCheck);

document.getElementById("back_experience").addEventListener("click", function(){
    openPage('Experience', 'Educational_Qualification');
});
document.getElementById("savedraft_ex").addEventListener("click", saveDraft);

document.getElementById("mdlSubmitAppPreview").addEventListener("click", function(){
    submitApplication('preview'); 
});
document.getElementById("mdlSubmitAppSubmit").addEventListener("click", function(){
    submitApplication('submit');
});
document.getElementById("mdlSubmitAppPayment").addEventListener("click", function(){
    submitApplication('payment');
});
document.getElementById("mdlHomeToPayment").addEventListener("click", homeToPayment);
document.getElementById("mdlHomeToSubmit").addEventListener("click", homeToSubmit);

document.getElementById("mdlViewAndApply").addEventListener("click", checkAcceptConditions);
document.getElementById("cur_openings-tab").addEventListener("click", function(){
    openTab('openings');
});
document.getElementById("app_status-tab").addEventListener("click", function(){
    openTab('status');
});

// Start: Personal Details
document.getElementById("salutation").addEventListener("change", changeGender);
document.getElementById("category").addEventListener("change", changeCategory);
document.getElementById("upload_rc").addEventListener("change", function(){
    read_rc(this);
});
document.getElementById("category_pwd").addEventListener("change", PWDCheck);

document.getElementById("upload_disability").addEventListener("change", function(){
    read_disability(this);
});
document.getElementById("category_ser").addEventListener("change", ExServiceCheck);

document.getElementById("upload_serviceman").addEventListener("change", function(){
    read_serviceman(this);
});
document.getElementById("category_ews").addEventListener("change", changeEWS);
document.getElementById("upload_ews").addEventListener("change", function(){
    read_ews(this);
});
document.getElementById("category_spt").addEventListener("change", function(){
    MeritCheck(this);
});
document.getElementById("cgov_serv").addEventListener("change", CentralGovCheck);
document.getElementById("dobAppl").addEventListener("change", changeDateRanges);
document.getElementById("dobAppl").addEventListener("keydown",function(){
    return false;
});
document.getElementById("state").addEventListener("change", function(){
    loadDistrictOptions('NA');
});
document.getElementById("p_state").addEventListener("change", function(){
    loadPDistrictOptions('NA');
});

document.getElementById("upload_photograph").addEventListener("change", function(){
    readURL(this);
});
document.getElementById("upload_signature").addEventListener("change", function(){
    readSignature(this);
});
// End: Personal Detail

// Start: X
document.getElementById("x_edu_board").addEventListener("change", function(){
    xBoard(this);
});
document.getElementById("x_percentage_cgpa").addEventListener("change", function(){
    XCgpa_Per(this);
});
document.getElementById("x_cgpa_obt").addEventListener("change", function(){
    validateCGPA(this,'x_cgpa_obt', 'x_cgpa_max');
});
document.getElementById("x_cgpa_max").addEventListener("change", function(){
    validateCGPA(this,'x_cgpa_obt', 'x_cgpa_max');
});
document.getElementById("x_percentage").addEventListener("keyup", ctrlxFormulaUpload);
document.getElementById("upload_x_formula").addEventListener("change", function(){
    readx_formula(this);
});
document.getElementById("upload_x_degree_certificate").addEventListener("change", function(){
    readx_degree_certificate(this);
});
// End: X

// Start: XII
document.getElementById("doneXII").addEventListener("change",validateDoneXII);

document.getElementById("xii_edu_board").addEventListener("change",function(){
    xiiBoard(this);
});
document.getElementById("xii_specialization").addEventListener("change",function(){
    xiispc(this);
});
document.getElementById("xii_percentage_cgpa").addEventListener("change",function(){
    XiiCgpa_Per(this);
});
document.getElementById("xii_cgpa_obt").addEventListener("change",function(){
    validateCGPA(this,'xii_cgpa_obt', 'xii_cgpa_max');
});
document.getElementById("xii_cgpa_max").addEventListener("change",function(){
    validateCGPA(this,'xii_cgpa_obt', 'xii_cgpa_max');
});
document.getElementById("xii_percentage").addEventListener("keyup", ctrlxiiFormulaUpload);
document.getElementById("xii_formula").addEventListener("change",function(){
    readxii_formula(this);
});
document.getElementById("upload_xii_degree_certificate").addEventListener("change",function(){
    readxii_degree_certificate(this);
});
// End: XII

// Start: ITI
document.getElementById("doneITI").addEventListener("change",validateDoneITI);
document.getElementById("iti_specialization").addEventListener("change",function(){
    itispc(this);
});
document.getElementById("iti_percentage_cgpa").addEventListener("change",function(){
    itiCgpa_Per(this);
});
document.getElementById("iti_cgpa_obt").addEventListener("change",function(){
    validateCGPA(this,'iti_cgpa_obt', 'iti_cgpa_max');
});

document.getElementById("iti_cgpa_max").addEventListener("change",function(){
    validateCGPA(this,'iti_cgpa_obt', 'iti_cgpa_max');
});

document.getElementById("iti_formula").addEventListener("change",function(){
    readiti_formula(this);
});
document.getElementById("upload_iti_degree_certificate").addEventListener("change",function(){
    readiti_degree_certificate(this);
});
// End: ITI

// Start: DIP
document.getElementById("doneDIP").addEventListener("change",validateDoneDIP);
document.getElementById("dip_specialization").addEventListener("change",function(){
    dipspc(this);
});
document.getElementById("dip_percentage_cgpa").addEventListener("change",function(){
    dipCgpa_Per(this);
});
document.getElementById("dip_cgpa_obt").addEventListener("change",function(){
    validateCGPA(this,'dip_cgpa_obt', 'dip_cgpa_max');
});
document.getElementById("dip_cgpa_max").addEventListener("change",function(){
    validateCGPA(this,'dip_cgpa_obt', 'dip_cgpa_max');
});
document.getElementById("dip_formula").addEventListener("change",function(){
    readdip_formula(this);
});
document.getElementById("upload_dip_degree_certificate").addEventListener("change",function(){
    readdip_degree_certificate(this);
});
// End: DIP

// Start: Higher Education
document.getElementById("ug_qualification1").addEventListener("change",function(){
    hequal1(this);
    document.getElementById("ifHEspec").style.display = "none"; // ChangeId: 2024011801
});
document.getElementById("ug_qualification2").addEventListener("change",function(){
    hequal2(this);
    document.getElementById("ifHEspec").style.display = "none"; // ChangeId: 2024011801
});
document.getElementById("ug_spec").addEventListener("change",function(){
    hespec(this);
});
document.getElementById("ug_university").addEventListener("change",function(){
    UgUniversity(this)
});
document.getElementById("ug_percentage_cgpa").addEventListener("change",function(){
    UgCgpa_Per(this);
});
document.getElementById("ug_cgpa_obt").addEventListener("change",function(){
    validateCGPA(this,'ug_cgpa_obt', 'ug_cgpa_max');
});
document.getElementById("ug_cgpa_max").addEventListener("change",function(){
    validateCGPA(this,'ug_cgpa_obt', 'ug_cgpa_max');
});
document.getElementById("ug_cgpa_fromuni").addEventListener("change",function(){
    UgCgpa_FromUni(this);
});
document.getElementById("upload_ug_formula").addEventListener("change",function(){
    readug_formula(this);
});
document.getElementById("upload_ug_marksheet").addEventListener("change",function(){
    readug_marksheet(this);
});
document.getElementById("upload_ug_degree_certificate").addEventListener("change",function(){
    readug_degree_certificate(this);
});
document.getElementById("upload_ug_abstract").addEventListener("change",function(){
    readug_abstract(this);
});
// End: Higher Education

// Start: Nurse
document.getElementById("upload_nurse_reg_certificate").addEventListener("change",function(){
    readnurse_reg_certificate(this);
});

// Start: Doc
document.getElementById("upload_doc_reg_certificate").addEventListener("change",function(){
    readdoc_reg_certificate(this);
});

// Start: NET
document.getElementById("netSel").addEventListener("change",function(){
    netOth(this);
});
document.getElementById("nes_ct").addEventListener("change",function(){
    read_neCert(this);
});
// End: NET

// Start: Experience
document.getElementById("experience").addEventListener("change",function(){
    exp(this);
});
document.getElementById("govtExp").addEventListener("change",changeGovtExp);
document.getElementById("upload_exp_certificate").addEventListener("change",function(){
    read_exp_certificate(this);
});
// End: Experience

//Start: ChangeId:2025041601
document.getElementById("bank_acc_doc").addEventListener("change",function(){
    readBankAccDoc(this);
});
// End: ChangeId:2025041601


// Start: ChangeId: 2025050709
function previewX_Marksheet(){
    var fname = document.getElementById("upload_x_degree_certificate").name;
    if(fname !== 'NA')
        download(fname);
    else
        alert("No preview to show");
}
document.getElementById("x_degree_certificate_preview").addEventListener("click",previewX_Marksheet);

function previewXII_Marksheet(){
    var fname = document.getElementById("upload_xii_degree_certificate").name;
    if(fname !== 'NA')
        download(fname);
    else
        alert("No preview to show");
}
document.getElementById("xii_degree_certificate_preview").addEventListener("click",previewXII_Marksheet);

function previewITI_Marksheet(){
    var fname = document.getElementById("upload_iti_degree_certificate").name;
    if(fname !== 'NA')
        download(fname);
    else
        alert("No preview to show");
}
document.getElementById("iti_degree_certificate_preview").addEventListener("click",previewITI_Marksheet);

function previewDIP_Marksheet(){
    var fname = document.getElementById("upload_dip_degree_certificate").name;
    if(fname !== 'NA')
        download(fname);
    else
        alert("No preview to show");
}
document.getElementById("dip_degree_certificate_preview").addEventListener("click",previewDIP_Marksheet);

function previewUG_DegreeCertificate(){
    var fname = document.getElementById("upload_ug_degree_certificate").name;
    if(fname !== 'NA')
        download(fname);
    else
        alert("No preview to show");
}
document.getElementById("ug_degree_certificate_preview").addEventListener("click",previewUG_DegreeCertificate);

function previewUG_Marksheet(){
    var fname = document.getElementById("upload_ug_marksheet").name;
    if(fname !== 'NA')
        download(fname);
    else
        alert("No preview to show");
}
document.getElementById("ug_marksheet_preview").addEventListener("click",previewUG_Marksheet);
// End: ChangeId: 2025050709

// Start: ChangeId: 2025050903
document.getElementById("category_pwd_scribe").addEventListener("change", ScribeCheck);
document.getElementById("category_pwd_comptime").addEventListener("change", CompTimeCheck);
// End: ChangeId: 2025050903