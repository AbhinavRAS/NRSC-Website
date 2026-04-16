/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


function addAdvtNos2() {
	$('#messageDiv').hide();
	document.getElementById("tableParentDiv").innerHTML = "";
	var advPostNos = new Array();
	var article = new Object();
	article.action = "advtNos";
	$('#advt_no_drop_report2').empty();
	$('#advt_no_drop_view').empty();
	$('#advt_no_drop_add_post').empty();
	$('#advt_no_drop_delete_ad').empty();
	$('#advt_no_drop_delete_ad_post').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAdvtNos",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding advts. Please check again or contact website administrator");
				},
				success : function(data) {
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					if (data.Results.length === 0) {
						$(
								'<option disabled="disabled" value="" selected>No advertisements found</option>')
								.appendTo('#advt_no_drop_report2');
						$(
								'<option disabled="disabled" value="" selected>No advertisements found</option>')
								.appendTo('#advt_no_drop_view');
						$(
								'<option disabled="disabled" value="" selected>No advertisements found</option>')
								.appendTo('#advt_no_drop_delete_ad');
						$(
								'<option disabled="disabled" value="" selected>No advertisements found</option>')
								.appendTo('#advt_no_drop_delete_ad_post');
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#advt_no_drop_report2');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#advt_no_drop_view');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#advt_no_drop_delete_ad');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#advt_no_drop_delete_ad_post');
					advPostNos = data.Results;

					for (var i = 0; i < data.Results.length; ++i) {
						var advNo = data.Results[i];

						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_report2');
						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_view');
						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_add_post');
						// $('<option value="' + advNo + '">' + advNo +
						// '</option>')
						// .appendTo('#advt_no_drop_edit_advt');
						// $('<option value="' + advNo + '">' + advNo +
						// '</option>')
						// .appendTo('#advt_no_drop_edit_ad');
						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_delete_ad');
						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_delete_ad_post');
					}
				}
			});
}

function postOptions_report2() {
	var option = $("#advt_no_drop_report2").val();
	$("#post_no_drop_report2").load("LoadPostNoServlet?advt=" + option);
	var divContainerPostAdmin = document
			.getElementById("tableParentReportAdminDiv2");
	divContainerPostAdmin.innerHTML = "";
	var divContainerApp = document.getElementById("tableParentReportAppDiv2");
	divContainerApp.innerHTML = "";

}

function createReportDynamicTable2() {

	// alert("Inside Create Dynamic Table Method..");
	if ((document.getElementById("advt_no_drop_report2").value.length == 0)
			|| (document.getElementById("advt_no_drop_report2").value === "Select")) {
		alert("Please select advertisement number");
		return;
	}
	if ((document.getElementById("post_no_drop_report2").value.length == 0)
			|| (document.getElementById("post_no_drop_report2").value === "Select")) {
		alert("Please select post number");
		return;
	}
	var divContainer = document.getElementById("tableParentReportAdminDiv2");
	divContainer.innerHTML = "";

	var article = new Object();
	article.advtNo = document.getElementById("advt_no_drop_report2").value;
	article.postNo = document.getElementById("post_no_drop_report2").value;
	article.action = "postDetails";
	article.button = "button";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAdvtNos",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in creating Report Table. Please check again or contact website administrator");
				},
				success : function(data) {
                                        //console.log("postDetails");
                                        //console.log(data);
					// alert("inside success : " + data.Results);
					if (data.Results === null) {
						alert("No records found");
						return;
					}
					if (data.Results.length === 0) {
						alert("No records found");
						return;
					}
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					var tableData = data.Results;
					document.getElementById("ifReportSubmitAdmin2").style.display = "block";
					var col = [];
					for (var i = 0; i < tableData.length; i++) {
						for ( var key in tableData[i]) {
							if (col.indexOf(key) === -1) {
								col.push(key);
							}
						}
					}

					// col.push("Button");

					var table = document.createElement("table");
					table.setAttribute("id", "admin_table");
					// table.setAttribute("style", "padding-bottom: inherit;
					// padding-top: inherit; text-align: center");

					var eltable = document.createElement("table");
					eltable.setAttribute("id", "adminel_table");
					eltable
							.setAttribute("style",
									"padding-bottom: inherit; padding-top: inherit; text-align: center");

					var cattable = document.createElement("table");
					cattable.setAttribute("id", "admincat_table");
					cattable
							.setAttribute("style",
									"padding-bottom: inherit; padding-top: inherit; text-align: center");

					var tr = table.insertRow(-1); // TABLE ROW.
					var th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Advt No.";
					tr.appendChild(th);
					var th1 = document.createElement("th");
					th1.setAttribute("style", "width: 10%;");
					th1.innerHTML = "Post No.";
					tr.appendChild(th1);
					var th2 = document.createElement("th");
					th2.setAttribute("style", "width: 15%;");
					th2.innerHTML = "Post Name";
					tr.appendChild(th2);
					var th3 = document.createElement("th");
					th3.setAttribute("style", "width: 10%;");
					th3.innerHTML = "Vacancies";
					tr.appendChild(th3);
					var th5 = document.createElement("th");
					th5.setAttribute("style", "width: 10%;");
					th5.innerHTML = "Status";
					tr.appendChild(th5);

					var trel = eltable.insertRow(-1); // TABLE ROW.
					var th = document.createElement("th");
					th.setAttribute("style", "width: 20%;");
					th.innerHTML = "Qualification";
					trel.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Mandatory";
					trel.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 20%;");
					th.innerHTML = "Discipline";
					trel.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 40%;");
					th.innerHTML = "Specialization";
					trel.appendChild(th);

					var trcat = cattable.insertRow(-1); // TABLE ROW.
					th = document.createElement("th");
					th.setAttribute("style", "width: 35%;");
					th.innerHTML = "Category";
					trcat.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 35%;");
					th.innerHTML = "Min Age";
					trcat.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 35%;");
					th.innerHTML = "Max Age";
					trcat.appendChild(th);
					// th = document.createElement("th");

					for (var i = 0; i < tableData.length; i++) {
						tr = table.insertRow(-1);
						var tabCell = tr.insertCell(-1);
						// tabCell.innerHTML = tableData[i][col[j]];
						tabCell.innerHTML = tableData[i]["advt_no"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["post_no"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["post_name"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["no_of_vacancies"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["status"];

						var eArray = tableData[i]["Eligibility"];
						for (var j = 0; j < eArray.length; j++) {
							trel = eltable.insertRow(-1);
							var tabCell = trel.insertCell(-1);
							tabCell.innerHTML = eArray[j]["essential_qualification"];
							tabCell = trel.insertCell(-1);
							tabCell.innerHTML = eArray[j]["mandatory"];
							tabCell = trel.insertCell(-1);
							tabCell.innerHTML = eArray[j]["discipline"];
							tabCell = trel.insertCell(-1);
							tabCell.innerHTML = eArray[j]["specialization"]
									.split("@");
							;
						}
                                                if (tableData[i].hasOwnProperty("net") && tableData[i]["net"] !== "NA") {
                                                    trel = eltable.insertRow(-1);
                                                    var tabCell = trel.insertCell(-1);
                                                    tabCell.innerHTML = "National Examination";
                                                    tabCell = trel.insertCell(-1);
                                                    tabCell.innerHTML = "NA";
                                                    tabCell = trel.insertCell(-1);
                                                    tabCell.innerHTML = "NA";
                                                    tabCell = trel.insertCell(-1);
                                                    tabCell.innerHTML = tableData[i]["net"].split("#").join();
                                                }
						var cArray = tableData[i]["Category"];
						for (var j = 0; j < cArray.length; j++) {
							trcat = cattable.insertRow(-1);
							var tabCell = trcat.insertCell(-1);
							tabCell.innerHTML = cArray[j]["category"];
							tabCell = trcat.insertCell(-1);
							tabCell.innerHTML = cArray[j]["age_limit_lower"];
							tabCell = trcat.insertCell(-1);
							tabCell.innerHTML = cArray[j]["age_limit_upper"];
						}
					}
					divContainer.appendChild(table);
					divContainer.appendChild(eltable);
					// divContainer.appendChild(cattable);
				}
			});
	createApplicantDetails2();

}


function downloadExcel2() {

	if ((document.getElementById("advt_no_drop_report2").value.length == 0)
			|| (document.getElementById("advt_no_drop_report2").value == "Select")) {
		alert("Please select advertisement number");
		return;
	}
	if ((document.getElementById("post_no_drop_report2").value.length == 0)
			|| (document.getElementById("post_no_drop_report2").value == "Select")) {
		alert("Please select post number");
		return;
	}
	var article = new Object();
	article.advt_No = document.getElementById("advt_no_drop_report2").value;
	article.post_No = document.getElementById("post_no_drop_report2").value;
	article.action = "downloadAppAll";
	// alert("download all");
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAdvtNos",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in Display Excel. Please check again or contact website administrator");
				},
				success : function(data) {
					var contextPath = data.Results[0];
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					// alert(""+contextPath);
					if (contextPath == "none")
						alert("There are no applicants applied for this post");
					else {
						// var messageStr = data.Results[1];
						var element = document.createElement('a');
						element.href = data.Results[0];
						element.target = "_blank";
						element.style.display = 'none';
						document.body.appendChild(element);
						element.click();
						document.body.removeChild(element);
					}

					// var contextPath ="Successfully Downloaded";
					// $('#messageDiv').show();
					// alert(messageStr);
					// document.getElementById("messageDiv").innerHTML =
					// messageStr;

				}
			});
}


function createApplicantDetails2() {
	var divContainer = document.getElementById("tableParentReportAppDiv2");
	divContainer.innerHTML = "";

	// onclick="sortTable(0)

	var article = new Object();
	 //alert("HELLO....");
	article.advtNo = document.getElementById("advt_no_drop_report2").value;
	article.postNo = document.getElementById("post_no_drop_report2").value;
	article.action = "reportDetails";
	article.button = "button";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAdvtNos",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in creating applicant details. Please check again or contact website administrator");
				},
				success : function(data) {
                                        //console.log(data);
					// alert("inside success : " + data.Results);
					if (data.Results === null) {
						alert("No applicants have applied for this post");
						return;
					}
					if (data.Results.length === 0) {
						alert("No applicants have applied for this post");
						return;
					}
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					var tableData = data.Results;

					document.getElementById("ifReportSubmitApp2").style.display = "block";
//					var col = [];
//					// alert(tableData.length + ":len ");
//					for (var i = 0; i < tableData.length; i++) {
//						for ( var key in tableData[i]) {
//							if (col.indexOf(key) === -1) {
//								col.push(key);
//							}
//						}
//					}

					var totalHigh = tableData[0]["HighEdCnt"];
                                        var maxPhdCnt = 0;
                                        for (var i = 0; i < tableData.length; i++) {
                                            for ( var idx in tableData[i]["Higher"]) {
                                                var p = 0;
                                                if (tableData[i]["Higher"][idx]["qualification_type"] === "PhD") {
                                                    p = p + 1;
                                                }
                                            }
                                            if (p > maxPhdCnt)
                                                maxPhdCnt = p;                                            
                                        }

					var button_excel = document.createElement("button");
					button_excel.innerHTML = "Download Table Data";
					button_excel.id = "btnExport";
					button_excel.name = "btnExport";
					button_excel.type = "button"
					// button_excel.setAttribute('onclick',
					// "alert('123');");//showFields(" + i+ ", this);");
					// button_excel.setAttribute('onclick',
					// "tableExcel(this);");

					button_excel.setAttribute('onclick',
							"xport.toCSV('personal','ApplicantsReport');");
					button_excel.className = "btn btn-primary";
					divContainer.appendChild(button_excel);
					// divContainer.appendChild(xtable);

					// col.push("Button");

					var pertable = document.createElement("table");
					pertable.setAttribute("id", "personal");
					pertable.setAttribute("style",
									"padding-bottom: inherit; padding-top: inherit; text-align: center");

					var tper = pertable.insertRow(-1); // TABLE ROW.
					var th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Registration Id";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Name";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "Gender";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Date Of Birth";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 7%;");
					th.innerHTML = "Phone";
					tper.appendChild(th);
                                        // Start: ChangeId: 2023110201 Aadhaar field added between Phone and Email
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Aadhaar";
					tper.appendChild(th);
                                        // End: ChangeId: 2023110201
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Email";
					tper.appendChild(th);
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Address";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Category";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "PWD";
					tper.appendChild(th);
                                        /*Start: ChangeId:2025041501*/
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Scribe Req";
					tper.appendChild(th);
                                        /*End: ChangeId:2025041501*/
                                        
                                        /*Start: ChangeId:2025050807*/
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Compensatory Time Req";
					tper.appendChild(th);
                                        /*End: ChangeId:2025050807*/
                                        
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "EWS";
					tper.appendChild(th);
					// th = document.createElement("th");
					// th.setAttribute("style", "width: 5%;");
					// th.innerHTML = "Marksheet";
					// tper.appendChild(th);
					//					

					// var trx = xtable.insertRow(-1); // TABLE ROW.
					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "X Percent";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "X CGPA";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "X Year Of Passing";
					tper.appendChild(th);

					// var trx = xtable.insertRow(-1); // TABLE ROW.
					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "XII Percent";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "XII CGPA";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "XII Year Of Passing";
					tper.appendChild(th);

					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "ITI Percent";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "ITI CGPA";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "ITI Year Of Passing";
					tper.appendChild(th);

					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "DIP Percent";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "DIP CGPA";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "DIP Year Of Passing";
					tper.appendChild(th);

                                        
                                        var th = document.createElement("th");
                                        th.setAttribute("style", "width: 3%;");
                                        th.innerHTML = "Graduate";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 3%;");
                                        th.innerHTML = "Percentage";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "CGPA";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "CGPA Univ Approval";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "University";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "Year of passing";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "Graduation Division";
					tper.appendChild(th);
                                        
                                        var th = document.createElement("th");
                                        th.setAttribute("style", "width: 3%;");
                                        th.innerHTML = "Postgraduate1";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 3%;");
                                        th.innerHTML = "Percentage1";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "CGPA1";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "CGPA1 Univ Approval";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "University1";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "Year of passing1";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "PG1 Division";
					tper.appendChild(th);
                                        
                                        var th = document.createElement("th");
                                        th.setAttribute("style", "width: 3%;");
                                        th.innerHTML = "Postgraduate2";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 3%;");
                                        th.innerHTML = "Percentage2";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "CGPA2";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "CGPA2 Univ Approval";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "University2";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
                                        th.setAttribute("style", "width: 5%;");
                                        th.innerHTML = "Year of passing2";
                                        tper.appendChild(th);
                                        th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "PG2 Division";
					tper.appendChild(th);
                                        
                                        if (maxPhdCnt > 0) {
                                                for (var h = 0; h < maxPhdCnt; h++) {
                                                        var th = document.createElement("th");
                                                        th.setAttribute("style", "width: 3%;");
                                                        var qualHeader = "Doctorate";
                                                        th.innerHTML = qualHeader;
                                                        tper.appendChild(th);
                                                        th = document.createElement("th");
                                                        th.setAttribute("style", "width: 5%;");
                                                        th.innerHTML = "Thesis";
                                                        tper.appendChild(th);
                                                        th = document.createElement("th");
                                                        th.setAttribute("style", "width: 5%;");
                                                        th.innerHTML = "Year of passing";
                                                        tper.appendChild(th);
                                                }
                                        }
                                        
                                        
					var th = document.createElement("th");
					th.setAttribute("style", "width: 5%;");
					th.innerHTML = "National Examination";
					tper.appendChild(th);

					var govtExpFlag = false;
					for (var f = 0; f < tableData.length; ++f) {
						govtExpFlag = tableData[f]["GovtExpFlag"];
						if (govtExpFlag == true)
							break;

					}

					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "Experience";
					tper.appendChild(th);
					// if (govtExpFlag == true) {
					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "Govt Experience";
					tper.appendChild(th);
					// }
					
					var th = document.createElement("th");
					th.setAttribute("style", "width: 3%;");
					th.innerHTML = "Experience Period";
					tper.appendChild(th);

					var uploadDir = UPLOAD_DIR;
					var uploadDir_app = UPLOAD_DIR;
					var uploadDir_admin = UPLOAD_DIR;
					var email_replace = "";
					//alert(" ....tableData.length... BEFORE:" +tableData.length);
					var totExp="NIL";
					for (var i = 0; i < tableData.length; i++) {
						uploadDir = UPLOAD_DIR;
						var pArray = tableData[i]["Personal"];
						var user = tableData[i]["applicant"];
                                                //totalHigh = tableData[i]["HighEdCnt"];
                                                totalHigh = tableData[i]["Higher"].length;
						email_replace = tableData[i]["email_replaced"];
						// alert("email_replace:"+email_replace);
						
						totExp="NIL";

						var separator = uploadDir
								.substring(uploadDir.length - 1);
						// alert("separatror is ....:"+separator);
						var userDir = "applicant" + separator + email_replace;
						uploadDir = uploadDir.replace("admin", userDir);
						uploadDir_app = uploadDir + separator + article.advtNo
								+ separator + article.postNo + separator;
						// alert("UPLOAD DIR initial:" + uploadDir);
						// alert("USER pArray.length ....." + pArray.length);
						for (var p = 0; p < pArray.length; p++) {
							uploadDir_admin = UPLOAD_DIR;
							var replaceX = "x_degree_certificate_"
									+ email_replace;
							var replaceXii = "xii_degree_certificate_"
									+ email_replace;
							var replaceiti = "iti_degree_certificate_"
									+ email_replace;
							var replacedip = "dip_degree_certificate_"
									+ email_replace;

							// alert("UPLOAD DIR App..... AFTER:" +
							// uploadDir_app);
							uploadDir_admin = uploadDir_admin + separator
									+ article.advtNo + separator
									+ article.postNo + separator;
							// alert("UPLOAD DIR ADMIN..... AFTER:" +
							// uploadDir_admin);
							var filepath_admin = uploadDir_admin + separator
									+ pArray[p]["registration_id"] + ".zip";
							var btnR = document.createElement('a');

							tper = pertable.insertRow(-1);
							btnR.href = filepath_admin;
							// alert("Path is :" + filepath_admin);
							// alert("reg id is :" +
							// pArray[p]["registration_id"]);
							btnR.target = "_blank";
							btnR.innerHTML = pArray[p]["registration_id"];
							var tabCell = tper.insertCell(-1);
							tabCell.appendChild(btnR);

							tabCell = tper.insertCell(-1);
							// tabCell.innerHTML = pArray[p]["salutation"] + " "
							+pArray[p]["first_name"] + " "
									+ pArray[p]["middle_name"] + " "
									+ pArray[p]["last_name"];
							var filepath_admin_preview = uploadDir_admin
									+ separator + pArray[p]["registration_id"]
									+ "_preview.pdf";
							var btnName = document.createElement('a');
							btnName.href = filepath_admin_preview;
							btnName.innerHTML = pArray[p]["salutation"] + " "
									+ pArray[p]["first_name"] + " "
									+ pArray[p]["middle_name"] + " "
									+ pArray[p]["last_name"];
							btnName.target = "_blank";
							tabCell.appendChild(btnName);

							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["gender"];
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["dob"];
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["contact_no"];
							tabCell = tper.insertCell(-1);
                                                        // PPEG start
                                                        tabCell.innerHTML = pArray[p]["aadhaar"];
							tabCell = tper.insertCell(-1);
                                                        // PPEG end
							tabCell.innerHTML = pArray[p]["email"];
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]['house_no']+","+pArray[p]['locality']+","
                                                                            +pArray[p]['town']+","+pArray[p]['district']+","
                                                                            +pArray[p]['state']+","+pArray[p]['pincode']+","
                                                                            +pArray[p]['contact_no']+",";
                                                                            +pArray[p]['aadhaar']+",";   // PPEG-HRD AADHAAR
                                                                            +pArray[p]["address"];
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["category"];
							tabCell = tper.insertCell(-1);
                                                        if (!pArray[p]["category_pwd"])
                                                            tabCell.innerHTML = "NA";
							else
                                                            tabCell.innerHTML = pArray[p]["category_pwd"];
                                                        
                                                        /*Start: ChangeId:2025041501*/
                                                        tabCell = tper.insertCell(-1);
                                                        if (!pArray[p]["category_pwd_scribe"])
                                                            tabCell.innerHTML = "NA";
							else
                                                            tabCell.innerHTML = pArray[p]["category_pwd_scribe"];
                                                        /*End: ChangeId:2025041501*/
                                                        
                                                        /*Start: ChangeId:2025050807*/
                                                        tabCell = tper.insertCell(-1);
                                                        if (!pArray[p]["category_pwd_comptime"])
                                                            tabCell.innerHTML = "NA";
							else
                                                            tabCell.innerHTML = pArray[p]["category_pwd_comptime"];
                                                        /*End: ChangeId:2025050807*/
                                                        
							tabCell = tper.insertCell(-1);
                                                        if (!pArray[p]["category_ews"])
                                                            tabCell.innerHTML = "NA";
							else
                                                            tabCell.innerHTML = pArray[p]["category_ews"];
							totExp=pArray[p]["totalexperience"];
							//alert("totExp:"+totExp);
							

							var xArray = tableData[i]["X"]; //console.log(tableData[i]["X"]) //DEBUG
							for (var j = 0; j < xArray.length; j++) {

								var extInd = xArray[j]["marksheet"]
										.lastIndexOf(".");
								var extn = xArray[j]["marksheet"]
										.substring(extInd);
								// alert("....:"+extn+"val is
								// :"+xArray[j]["marksheet"].substring(extn));
								// tper = pertable.insertRow(-1);
								// var filepath = uploadDir_app
								// + xArray[j]["marksheet"];

								var filepath = uploadDir_app + replaceX + extn;

								var btnM = document.createElement('a');
								btnM.href = filepath;
								btnM.target = "_blank";
								var perVal = xArray[j]["x_percentage"];
								var cgVal = xArray[j]["x_cgpa_obt"];
								if (perVal === "-1") {
									perVal = "NA";
									tabCell = tper.insertCell(-1);
									tabCell.innerHTML = perVal;
									btnM.innerHTML = cgVal;
									tabCell = tper.insertCell(-1);
									tabCell.appendChild(btnM);

								} else {
									cgVal = "NA";
									btnM.innerHTML = perVal;
									tabCell = tper.insertCell(-1);
									tabCell.appendChild(btnM);
									tabCell = tper.insertCell(-1);
									tabCell.innerHTML = cgVal;
								}
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = xArray[j]["x_year_of_passing"];

							}

							xArray = tableData[i]["XII"];
                                                        if (tableData[i].hasOwnProperty("XII")){
                                                            xArray = tableData[i]["XII"];
                                                        }
                                                        if (xArray !== null && xArray.length > 0) {
                                                            for (var j = 0; j < xArray.length; j++) {
//							for (var j = 0; j < xArray.length; j++) {

								// var filepath = uploadDir_app
								// + xArray[j]["marksheet"];
								var extInd = xArray[j]["marksheet"]
										.lastIndexOf(".");
								var extn = xArray[j]["marksheet"]
										.substring(extInd);
								var filepath = uploadDir_app + replaceXii
										+ extn;
								var btnM = document.createElement('a');
								btnM.href = filepath;
								btnM.target = "_blank";
								var perVal = xArray[j]["xii_percentage"];
								var cgVal = xArray[j]["xii_cgpa_obt"];
								if (perVal === "-1") {
									perVal = "NA";
									tabCell = tper.insertCell(-1);
									tabCell.innerHTML = perVal;
									btnM.innerHTML = cgVal;
									tabCell = tper.insertCell(-1);
									tabCell.appendChild(btnM);

								} else {
									cgVal = "NA";
									btnM.innerHTML = perVal;
									tabCell = tper.insertCell(-1);
									tabCell.appendChild(btnM);
									tabCell = tper.insertCell(-1);
									tabCell.innerHTML = cgVal;
								}
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = xArray[j]["xii_year_of_passing"];
                                                            }
							} else {
                                                            
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
                                                        }  

                                                        xArray = null;
                                                        if (tableData[i].hasOwnProperty("ITI")){
                                                            xArray = tableData[i]["ITI"];
                                                        }
                                                        if (xArray !== null && xArray.length > 0) {
                                                            j=0;
                                                                if (xArray[j]["qualification_type"].toString().toUpperCase()  === "DIPLOMA") {
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "-";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "-";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "-";
                                                                }

                                                                // var filepath = uploadDir_app
                                                                // + xArray[j]["marksheet"];
                                                                var extInd = xArray[j]["degree_certificate"]
                                                                                .lastIndexOf(".");
                                                                var extn = xArray[j]["degree_certificate"]
                                                                                .substring(extInd);
                                                                var filepath = uploadDir_app + replaceiti + extn;
                                                                if (xArray[j]["qualification_type"].toString().toUpperCase()  === "DIPLOMA") {
                                                                    filepath = uploadDir_app + replacedip + extn;
                                                                }
                                                                var btnM = document.createElement('a');
                                                                btnM.href = filepath;
                                                                btnM.target = "_blank";
                                                                var perVal = xArray[j]["percentage"];
                                                                var cgVal = xArray[j]["cgpa_obt"];
                                                                if (perVal === "-1") {
                                                                        perVal = "NA";
                                                                        tabCell = tper.insertCell(-1);
                                                                        tabCell.innerHTML = perVal;
                                                                        btnM.innerHTML = cgVal;
                                                                        tabCell = tper.insertCell(-1);
                                                                        tabCell.appendChild(btnM);

                                                                } else {
                                                                        cgVal = "NA";
                                                                        btnM.innerHTML = perVal;
                                                                        tabCell = tper.insertCell(-1);
                                                                        tabCell.appendChild(btnM);
                                                                        tabCell = tper.insertCell(-1);
                                                                        tabCell.innerHTML = cgVal;
                                                                }
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = xArray[j]["year_of_passing"];
                                                                
                                                                if (xArray[j]["qualification_type"].toString().toUpperCase() === "ITI") {
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "-";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "-";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "-";
                                                                }
                                                            
                                                        } else {
                                                            
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
                                                        }                                                        
                                                        

							var gradIntegratedFlag = false;
                                                        if (totalHigh === 0) {
                                                            // grad
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            // grad Division
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            // postgrad 1
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            // postgrad 1 Division
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            // postgrad 2
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                            // postgrad 2 Division
                                                            tabCell = tper.insertCell(-1);
                                                            tabCell.innerHTML = "NA";
                                                        } else {
                                                            var j = 0;
                                                            xArray = tableData[i]["Higher"];
                                                            var replaceGradDeg = "degree_certificate_Graduate_" + email_replace;
                                                            var replacePostDeg = "degree_certificate_Postgraduate";
                                                            var replacePostDeg2 = "_" + email_replace;
                                                            //2 postgraduates certificate how?
                                                            var replaceGradMark = "marksheet_Graduate_" + email_replace;
                                                            var qualIdx = [];
                                                            var quals = [];
                                                            for (j=0; j<totalHigh; j++) {
                                                                quals.push(xArray[j]["qualification"]);
                                                            }
                                                            if (quals.indexOf("B.Tech/B.E") !== -1) {
                                                                qualIdx.push(quals.indexOf("B.Tech/B.E"));
                                                            } else if (quals.indexOf("B.Sc") !== -1) {
                                                                qualIdx.push(quals.indexOf("B.Sc"));
                                                            }
                                                            if (quals.indexOf("Integrated M.Tech/M.E") !== -1) {
                                                                if (qualIdx.length === 0)
                                                                    qualIdx.push(-1);
                                                                qualIdx.push(quals.indexOf("Integrated M.Tech/M.E"));
                                                                gradIntegratedFlag = true;
                                                            } else if (quals.indexOf("M.Sc") !== -1) {
                                                                qualIdx.push(quals.indexOf("M.Sc"));
                                                            } else {
                                                                qualIdx.push(-1);
                                                            }
                                                             if (quals.indexOf("M.Tech") !== -1 || quals.indexOf("M.Tech/M.E") !== -1  || quals.indexOf("MSc-Tech") !== -1) {
                                                                if (quals.indexOf("M.Tech") !== -1) {
                                                                    qualIdx.push(quals.indexOf("M.Tech"));
                                                                }else if(quals.indexOf("MSc-Tech") !== -1)
                                                                {
                                                                        qualIdx.push(quals.indexOf("MSc-Tech"));
                                                                }
                                                                else {
                                                                    qualIdx.push(quals.indexOf("M.Tech/M.E"));
                                                                }
                                                            } else {
                                                                qualIdx.push(-1);
                                                            }
                                                            for (var a in qualIdx) {
                                                                var gradCnt = 0;
                                                                j = qualIdx[a];
                                                                if (j!== -1) {
                                                                    var qType = xArray[j]["qualification_type"];
//                                                                if ((qType === "Graduate" && gradCnt < 1) || 
//                                                                    (qType === "Postgraduate" && xArray[j]["qualification"] === "M.Sc" ) ||
//                                                                    (qType === "Postgraduate" && xArray[j]["qualification"] === "M.Tech/M.E" )) 
//                                                                {
                                                                    var extIndD = xArray[j]["degree_certificate"].lastIndexOf(".");
                                                                    var extnD = xArray[j]["degree_certificate"].substring(extIndD);
                                                                    var filepath_deg;
                                                                    var filepath;
                                                                    if (qType === "Graduate") 
                                                                        filepath_deg = uploadDir_app + replaceGradDeg + extnD;
                                                                    else if (qType === "Postgraduate") 
                                                                        filepath_deg = uploadDir_app + replacePostDeg + a.toString() + replacePostDeg2 + extnD;

                                                                    var extIndM = xArray[j]["marksheet"].lastIndexOf(".");
                                                                    var extnM = xArray[j]["marksheet"].substring(extIndM);
                                                                    if (qType === "Graduate") 
                                                                        filepath = uploadDir_app + replaceGradMark + extnM;
                                                                    else if (qType === "Postgraduate") 
                                                                        filepath = uploadDir_app + replacePostDeg.replace("degree_certificate","marksheet") + a.toString() + replacePostDeg2 + extnM;

                                                                    var qual = xArray[j]["qualification"];
//                                                                    if (qType === "Graduate" && qual.startsWith("Integrated"))
//                                                                        gradIntegratedFlag = true;

                                                                    var btnd = document.createElement('a');
                                                                    btnd.href = filepath_deg;
                                                                    btnd.target = "_blank";
                                                                    btnd.innerHTML += qual;

                                                                    if(qType === "Graduate" && j>=1){
                                                                        btnd.innerHTML += ",";
                                                                    }
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.appendChild(btnd);


                                                                    // var filepath = uploadDir_app+
                                                                    // xArray[j]["marksheet"];
                                                                    var btnM = document.createElement('a');
                                                                    btnM.href = filepath;
                                                                    btnM.target = "_blank";

                                                                    var perVal = xArray[j]["percentage"];
                                                                    var cgVal = xArray[j]["cgpa_obt"];
                                                                    if (perVal === "-1") {
                                                                            perVal = "NA";
                                                                            tabCell = tper.insertCell(-1);
                                                                            tabCell.innerHTML = perVal;
                                                                            btnM.innerHTML = cgVal;
                                                                            tabCell = tper.insertCell(-1);
                                                                            tabCell.appendChild(btnM);

                                                                    } else {
                                                                            cgVal = "NA";
                                                                            btnM.innerHTML = perVal;
                                                                            tabCell = tper.insertCell(-1);
                                                                            tabCell.appendChild(btnM);
                                                                            tabCell = tper.insertCell(-1);
                                                                            tabCell.innerHTML = cgVal;
                                                                    }

                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = xArray[j]["cgpa_univ_flag"];;

                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = xArray[j]["university"];

                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = xArray[j]["year_of_passing"];

                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = xArray[j]["division"];
                                                                    
                                                                    if (qType === "Graduate") gradCnt += 1;                                                                    
                                                                } else if (gradCnt >= 1) {
                                                                    //do nothing
                                                                } else {
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                    tabCell = tper.insertCell(-1);
                                                                    tabCell.innerHTML = "NA";
                                                                }
                                                            }
                                                            
                                                        }
                                                    
                                                        var phdNum = 0;
                                                        if (quals.indexOf("Phd") !== -1) {
                                                            for (var j = quals.indexOf("Phd"); j < totalHigh; j++) {
                                                                qualType  = "PhD";
                                                                phdNum += 1;
                                                                var replacePhdDeg = "degree_certificate_"
                                                                                        + qualType + "_" + email_replace;
                                                                var extIndD = xArray[j]["degree_certificate"]
                                                                                .lastIndexOf(".");
                                                                var extnD = xArray[j]["degree_certificate"]
                                                                                .substring(extIndD);
                                                                var filepath_deg = uploadDir_app
                                                                                + replacePhdDeg + extnD;

                                                                var replacePhdAbs = "abstract_" + qualType
                                                                                + "_" + email_replace;
                                                                extIndD = xArray[j]["phdabstract"]
                                                                                .lastIndexOf(".");
                                                                extnD = xArray[j]["phdabstract"]
                                                                                .substring(extIndD);
                                                                var filepath_thesis = uploadDir_app
                                                                                + replacePhdAbs + extnD;

                                                                // alert("degree_certificate:" + replacePhdDeg);
                                                                var btnd = document.createElement('a');
                                                                btnd.href = filepath_deg;
                                                                btnd.target = "_blank";
                                                                btnd.innerHTML = qualType;
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.appendChild(btnd);

                                                                var btnt = document.createElement('a');
                                                                btnt.href = filepath_thesis;
                                                                btnt.target = "_blank";
                                                                btnt.innerHTML = xArray[j]["phdtopic"];
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.appendChild(btnt);

                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = xArray[j]["year_of_passing"];
                                                            }
                                                        }
                                                        if (phdNum > 0 && phdNum < maxPhdCnt) {
                                                            for (var k = 0; k < maxPhdCnt-phdNum; k++) {
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = "NA";
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = "NA";
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = "NA";
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = "NA";
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = "NA";
                                                                tabCell = tper.insertCell(-1);
                                                                tabCell.innerHTML = "NA";
                                                            }
                                                        }
                                                        
                                                        var natExam = "NA"
                                                        if (tableData[i].hasOwnProperty("NationalExam") && tableData[i]["NationalExam"].length > 0) {
                                                            if (tableData[i]["NationalExam"][0].hasOwnProperty("net_name")) natExam = tableData[i]["NationalExam"][0].net_name;
                                                        }
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = natExam;

							xArray = tableData[i]["Experience"];
							var govtExp = tableData[i]["GovtExperience"];

							tabCell = tper.insertCell(-1);                                                        
							tabCell.innerHTML = "Yes";
                                                        
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = govtExp;
                                                        
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = totExp;
						}

					}
					divContainer.appendChild(pertable);

				}
			});
}
