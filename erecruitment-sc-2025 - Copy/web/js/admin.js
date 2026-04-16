//var validFlag= true;

//alert("!2344565");

function addAdvtNos() {
	$('#messageDiv').hide();
	document.getElementById("tableParentDiv").innerHTML = "";
	var advPostNos = new Array();
	var article = new Object();
	article.action = "advtNos";
	$('#advt_no_drop_report').empty();
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
								.appendTo('#advt_no_drop_report');
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
							.appendTo('#advt_no_drop_report');
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
								'#advt_no_drop_report');
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

function getAppPostDetails() {
	addPostName();
	getQualAndDisc();
	addDesirableQual();
	$("#post_no").val("");
	for (var i = 1; i <= 9; i++) {
		$("#" + i + "_cat").prop("checked", false);
		$("#" + i + "_min_age").val(18);
		$("#" + i + "_max_age").val(45);
	}
}

function validateAddAdvertisement() {
	// alert("Add advertisement data validation method called");
	// alert($('#validFlag').val());
	/*
	 * if ($('#validFlag').val() === "false") { alert("Please rectify the error
	 * on the page."); return false; }
	 */
	var flag1 = false;
	var name = "";
	$("font[color='red']").each(function() {
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
		//		+ ")");
		return false;
	}

	document.getElementById("advt_no").style.borderColor = "#ccc";
	document.getElementById("valid_from").style.borderColor = "#ccc";
	document.getElementById("valid_till").style.borderColor = "#ccc";
	document.getElementById("ref_date").style.borderColor = "#ccc";
	document.getElementById("remarks").style.borderColor = "#ccc";

	if (document.getElementById("advt_no").value.length == 0) {
		alert("Advertisement Number cannot be left blank.");
		document.getElementById("advt_no").style.borderColor = "red";
		return false;
	}
	if (!(document.getElementById("advt_no").value).startsWith("NRSC-RMT-")) {
		alert("Please ensure the Advertisement Number starts with 'NRSC-RMT-'");
		document.getElementById("advt_no").style.borderColor = "red";
		return false;
	}
	if (document.getElementById("valid_from").value.length == 0) {
		alert("Valid from cannot be left blank.");
		document.getElementById("valid_from").style.borderColor = "red";
		return false;
	}
	if (document.getElementById("valid_till").value.length == 0) {
		alert("Valid till cannot be left blank.");
		document.getElementById("valid_till").style.borderColor = "red";
		return false;
	}
	if (document.getElementById("ref_date").value.length == 0) {
		alert("Reference date cannot be left blank.");
		document.getElementById("ref_date").style.borderColor = "red";
		return false;
	}
	if (document.getElementById("remarks").value.length == 0) {
		alert("Reamrks cannot be left blank.");
		document.getElementById("remarks").style.borderColor = "red";
		return false;
	}
	// alert("upload_adv value:"+document.getElementById("upload_adv").value);

	var filename = document.getElementById("upload_adv").value;
	var upFileName = filename.substring(filename.lastIndexOf('/') + 1);
	upFileName = upFileName.replace(/^.*[\\\/]/, '');
	var advtNo = document.getElementById("advt_no").value + ".pdf";
	// alert("upFileName is: " + upFileName+",the other:"+advtNo);
	if (upFileName != advtNo) {
		alert("Invalid Filename. Filename must be same as the advertisement number and only PDF files are allowed");
		return false;
	}

	addAdvtMethod();
}

function validateAddPost() {
	// alert("Add post data validation method called");
	var flag1 = false;
	var name = "";
	$("font[color='red']").each(function() {
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
		//		+ ")");
		return false;
	}

	document.getElementById("advt_no_drop_add_post").style.borderColor = "#ccc";
	document.getElementById("post_name_addPost").style.borderColor = "#ccc";
	document.getElementById("post_no").style.borderColor = "#ccc";
	document.getElementById("no_of_vacancies").style.borderColor = "#ccc";
	// document.getElementById("eligibility_addPost").style.borderColor =
	// "#ccc";
	// document.getElementById("essential_qualification_addPost").style.borderColor
	// = "#ccc";
	// document.getElementById("discipline_addPost").style.borderColor = "#ccc";
	// document.getElementById("specialization_addPost").style.borderColor =
	// "#ccc";
	document.getElementById("desirable_qualification_addPost").style.borderColor = "#ccc";

	if (document.getElementById("advt_no_drop_add_post").value.length == 0) {
		alert("Advertisement Number should be selected");
		document.getElementById("advt_no_drop_add_post").style.borderColor = "red";
		return false;
	}
	if (document.getElementById("post_name_addPost").value.length == 0) {
		alert("Post Name cannot be left blank.");
		document.getElementById("post_name_addPost").style.borderColor = "red";
		return false;
	}
	var postVal = document.getElementById("post_no").value;
	if (postVal.length == 0) {
		alert("Post Number cannot be left blank.");
		document.getElementById("post_no").style.borderColor = "red";
		return false;
	}
	// alert("!");
	// var $regexpost_no = "/^([1-9]{1}[0-9]{0,2})$/";
	// alert("!@@@");
	// if (!postVal.match($regexpost_no)) {
	// alert("Post Number should be numeric.");
	// document.getElementById("post_no").style.borderColor = "red";
	// return false;
	// }
	if (document.getElementById("no_of_vacancies").value.length == 0) {
		alert("No. of vacancies cannot be left blank.");
		document.getElementById("no_of_vacancies").style.borderColor = "red";
		return false;
	}
	if (document.getElementById("no_of_vacancies").value === 0) {
		alert("No. of vacancies cannot be 0 (zero).");
		document.getElementById("no_of_vacancies").style.borderColor = "red";
		return false;
	}

	// if (document.getElementById("eligibility_addPost").value.length == 0) {
	// alert("Academic Eligibility should be selected");
	// document.getElementById("eligibility_addPost").style.borderColor = "red";
	// return false;
	// }
	// if
	// (document.getElementById("essential_qualification_addPost").value.length
	// == 0) {
	// alert("Qualification should be selected");
	// document.getElementById("essential_qualification_addPost").style.borderColor
	// = "red";
	// return false;
	// }
	// if (document.getElementById("discipline_addPost").value.length == 0) {
	// alert("Disciplines should be selected");
	// document.getElementById("discipline_addPost").style.borderColor = "red";
	// return false;
	// }
	// if (document.getElementById("specialization_addPost").value.length == 0)
	// {
	// alert("Specialization should be selected");
	// document.getElementById("specialization_addPost").style.borderColor =
	// "red";
	// return false;
	// }

	var x = document.getElementById("addElibilityDiv").childElementCount;
	var flag = false;
	for (var i = 0; i < x; i++) {
		if ($("#add_cb" + i).prop("checked")) {
			flag = true;
		}
	}
	if (!flag) {
		alert("Atleast one qualification should be selected");
		return false;
	}

	// if
	// (document.getElementById("desirable_qualification_addPost").value.length
	// == 0) {
	// alert("Desirable Qualification should be selected");
	// document.getElementById("desirable_qualification_addPost").style.borderColor
	// = "red";
	// return false;
	// }
	var cnt = 0;
	for (var i = 1; i <= 9; i++) {
		if ($("#" + i + "_cat").prop("checked")) {
			if (document.getElementById(i + "_min_age").value.length == 0) {
				alert("Minimum age should be selected");
				document.getElementById(i + "_min_age").style.borderColor = "red";
				return false;
			} else if (document.getElementById(i + "_max_age").value.length == 0) {
				alert("Maximum age should be selected");
				document.getElementById(i + "_max_age").style.borderColor = "red";
				return false;
			} else
				continue;
		} else
			cnt++;
	}
	if (cnt == 9) {
		alert("Atleast one category should be selected");
		return false;
	}
	addPostMethod();
}

function postOptions_report() {
	var option = $("#advt_no_drop_report").val();
	$("#post_no_drop_report").load("LoadPostNoServlet?advt=" + option);
	var divContainerPostAdmin = document
			.getElementById("tableParentReportAdminDiv");
	divContainerPostAdmin.innerHTML = "";
	var divContainerApp = document.getElementById("tableParentReportAppDiv");
	divContainerApp.innerHTML = "";

}
function postOptions_view() {
	var option = $("#advt_no_drop_view").val();
	$("#post_no_drop_view").load("LoadPostNoServlet?advt=" + option);
	document.getElementById("tableParentDiv").innerHTML = "";
}

function postOptions_edit() {
	var option = $("#advt_no_drop_edit_ad").val();
	$("#post_no_drop_edit").load("LoadPostNoServlet?advt=" + option);
}

function postOptions_delete() {
	var option = $("#advt_no_drop_delete_ad_post").val();
	$("#post_no_drop_delete").load("LoadPostNoServlet?advt=" + option);
}

function openAdminAdv() {
	// alert("HELLO12");
	var str = document.getElementById("advt_no_drop_view").value;
	if (str == "") {
		alert("Please select the advertisement Number");
		return;
	}
	// var path = "uploads/" + str + ".pdf";
	var path = UPLOAD_DIR + str + ".pdf";
	// alert("Str : " + path);
	window
			.open(
					path,
					'_blank',
					'directories=no,height=400,width=600,left=10,top=10,scrollbars=yes,menubar=no',
					false);
	return false;
}

function addDateValidation() {
	// alert("1234");

	// var days = 10; // Days you want to subtract
	var days = 0; // Days you want to subtract
	var date = new Date();
	var last = new Date(date.getTime() - (days * 24 * 60 * 60 * 1000));
	var day = last.getDate();
	var month = last.getMonth() + 1;
	var year = last.getFullYear();

	var tempMonth = month;
	if (tempMonth < 10) {
		tempMonth = "0" + tempMonth;
	}
	var tempDay = day;
	if (tempDay < 10) {
		tempDay = "0" + tempDay;
	}
	var d = year + '-' + tempMonth + '-' + tempDay;
	var thisYear = new Date(today).getFullYear();
	var maxDate = today.replace(thisYear, (thisYear + 1));
	document.getElementById("valid_from").startDate = d;
	document.getElementById("valid_till").startDate  = d;
	document.getElementById("ref_date").startDate  = d;
	document.getElementById("valid_from").endDate = maxDate;
	document.getElementById("valid_till").endDate = maxDate;
	document.getElementById("ref_date").endDate = maxDate;
}


function addAllAdvtNos() {
	$('#messageDiv').hide();
	// alert("Getting adv nos");
	var article = new Object();
	article.action = "advtNosAll";
	$('#advt_no_drop_add_post').empty();
	$('#advt_no_drop_delete_ad').empty();
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
					alert("Problem in adding all advt nos. Please check again or contact website administrator");
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
								.appendTo('#advt_no_drop_add_post');
						$(
								'<option disabled="disabled" value="" selected>No advertisements found</option>')
								.appendTo('#advt_no_drop_delete_ad');
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#advt_no_drop_add_post');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#advt_no_drop_delete_ad');

					for (var i = 0; i < data.Results.length; ++i) {
						var advNo = data.Results[i];
						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_add_post');
						$(
								'<option value="' + advNo + '">' + advNo
										+ '</option>').appendTo(
								'#advt_no_drop_delete_ad');
					}
				}
			});
}

function addPostName() {
	var article = new Object();
	$('#post_name_addPost').empty();
	$('#post_name_edit').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadPostName",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding post name. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#post_name_addPost');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#post_name_edit');

					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#post_name_addPost');
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#post_name_edit');
					}
				}
			});
}

function addAcadEligib() {
	// alert("addAcadEligib loaded");
	var article = new Object();
	article.sat = "RS2";
	article.sen = "sen";

	$('#eligibility_addPost').empty();
	$('#eligibility_editPost').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAcademicEligibility",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert(" Problem in adding Academic Elgibility. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#eligibility_addPost');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#eligibility_editPost');

					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];

						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#eligibility_addPost');
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#eligibility_editPost');
					}
				}
			});
}

function getQualAndDisc() {
	// alert("getQualAndDisc loaded");

	var addDivRow = document.getElementById("addElibilityDiv");
	addDivRow.innerHTML = "";

	// $('#essential_qualification_addPost').empty();
	// $('#essential_qualification_editPost').empty();
	// $('#discipline_addPost').empty();
	// $('#discipline_editPost').empty();

	var article = new Object();
	article.name = "QualDisc";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAcademicEligibility",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding Qual and discipline. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];
						var qualName = obj.Qualification;
						var id = obj.id;
						var disciplines = obj.Discipline;
						var specs = obj.Spec;

						var checkbox = document.createElement('input');
						checkbox.type = "checkbox";
						checkbox.name = "qualName";
						checkbox.value = qualName;
						checkbox.id = "add_cb" + i;
						var qualDiv = document.createElement('div');
						qualDiv.className = 'col-md-2';
						qualDiv.appendChild(checkbox);
						qualDiv.appendChild(document.createTextNode(qualName));

						var select = document.createElement('select');
						select.name = "add_md" + i;
						select.id = "add_md" + i;
						select.className = "form-control";
						select.style.width = "80px";
						select.options[0] = new Option('Yes', 'Yes');
						select.options[1] = new Option('No', 'No');
						var mandDiv = document.createElement('div');
						mandDiv.className = 'col-md-2';
						mandDiv.appendChild(select);

						var discSelect = document.createElement('select');
						discSelect.name = "add_ds" + i;
						discSelect.id = "add_ds" + i;
						discSelect.className = "form-control";
						discSelect.onchange = function() {
							ddlOnChange(this);
						};
						// select.style.width = "80px";
						for (var j = 0; j < disciplines.length; ++j) {
							var val = disciplines[j];
							discSelect.options[j] = new Option(val, val + "-"
									+ id[j]);
						}
						var discDiv = document.createElement('div');
						discDiv.className = 'col-md-3';
						discDiv.appendChild(discSelect);

						var specSelect = document.createElement('select');
						specSelect.name = "add_sp" + i;
						specSelect.id = "add_sp" + i;
						specSelect.className = "form-control";
						specSelect.multiple = "multiple";
						var specBoxLen = (specs.length * 20);
						if (specBoxLen <= 120) {
							specBoxLen = specBoxLen + 15;
						}
						specSelect.style.height = specBoxLen + "px";
						for (var j = 0; j < specs.length; ++j) {
							var val = specs[j];
							specSelect.options[j] = new Option(val, val);
						}
						if (specs.length > 0) {
							specSelect.selectedIndex = 0;
							// } else {
							// alert("No specialization available. Please
							// contact
							// website designer.")
						}
						var specDiv = document.createElement('div');
						specDiv.className = 'col-md-4';
						specDiv.appendChild(specSelect);

						var iDiv = document.createElement('div');
						iDiv.className = 'row';
						iDiv.appendChild(qualDiv);
						iDiv.appendChild(mandDiv);
						iDiv.appendChild(discDiv);
						iDiv.appendChild(specDiv);
						addDivRow.appendChild(iDiv);
					}
				}
			});
}

function ddlOnChange(elementRef) {
	var d = $("#" + elementRef.id).val();
	var sElement = elementRef.id;
	sElement = sElement.replace("ds", "sp");
	// alert("sElement : " + sElement);

	var dArr = d.split("-");
	// alert("vals : " + dArr[0] + ", " + dArr[1]);
	var article = new Object();
	article.name = "Specialization";
	article.id = dArr[1];
	article.value = dArr[0];
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadAcademicEligibility",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in dd on change. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					});
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					var specSelect = document.getElementById(sElement);
					specSelect.options.length = 0;
					console.log("data.Results : " + data.Results);
					// alert("data.Results : " + data.Results.length);
					for (var i = 0; i < data.Results.length; ++i) {
						var val = data.Results[i];
						// alert("val : " + val);
						specSelect.options[i] = new Option(val, val);
					}
					if (data.Results.length > 0) {
						specSelect.selectedIndex = 0;
					} else {
						alert("No specialization available. Please contact website designer.")
					}
				}
			});
}

function addQual() {
	// alert("addQual loaded");
	var article = new Object();

	$('#essential_qualification_addPost').empty();
	$('#essential_qualification_editPost').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadQualification",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding qual. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#essential_qualification_addPost');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#essential_qualification_editPost');

					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#essential_qualification_addPost');
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#essential_qualification_editPost');
					}
				}
			});
}

function addDiscipline() {
	// alert("addDiscipline loaded");
	var article = new Object();

	$('#discipline_addPost').empty();
	$('#discipline_editPost').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadDiscipline",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding discipline. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#discipline_addPost');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#discipline_editPost');

					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#discipline_addPost');
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#discipline_editPost');
					}
				}
			});
}

function addSpecialization() {
	// alert("addSpecialization loaded");
	var article = new Object();

	$('#specialization_addPost').empty();
	$('#specialization_editPost').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadSpecialization",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding Specialization. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#specialization_addPost');
					$(
							'<option disabled="disabled" value="" selected>Select</option>')
							.appendTo('#specialization_editPost');

					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#specialization_addPost');
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#specialization_editPost');
					}
				}
			});
}

function addDesirableQual() {
	// alert("Get DesirableQual Form loaded");
	var article = new Object();

	$('#desirable_qualification_addPost').empty();
	$('#desirable_qualification_editPost').empty();
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/LoadDesirableQual",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in adding DesirableQual. Please check again or contact website administrator");
				},
				success : function(data) {
					$(document.body).css({
						'cursor' : 'default'
					})
					// console.log("data.Results : " + data.Results);
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					// $('<option disabled="disabled" value=""
					// selected>Select</option>')
					// .appendTo('#desirable_qualification_addPost');
					// $('<option disabled="disabled" value=""
					// selected>Select</option>')
					// .appendTo('#desirable_qualification_editPost');

					for (var i = 0; i < data.Results.length; ++i) {
						var obj = data.Results[i];
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#desirable_qualification_addPost');
						$('<option value="' + obj + '">' + obj + '</option>')
								.appendTo('#desirable_qualification_editPost');
					}
				}
			});
}

function addAdvtMethod() {
	// alert("addAdvt Method called");

	var article = new Object();
	article.advtno = $("#advt_no").val();
	article.valid_from = $("#valid_from").val();
	article.valid_till = $("#valid_till").val();
	article.ref_date = $("#ref_date").val();
	article.remarks = $("#remarks").val();
	article.action = "addAdvt";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/AddAdvertismentServlet",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in addAdvt. Please check again or contact website administrator");
				},
				success : function(data) {
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
						return;
					} else if (actionStr === "success") {
						$('#messageDiv').show();
						document.getElementById("messageDiv").innerHTML = messageStr;
						$('#add_adv1').removeClass("active");
						$('#aali').removeClass("active");
					} else {
						alert("Unknown result. Please contact website administrator");
					}
				}
			});
}

function editAdvtMethod() {
	// alert("editAdvt Method called");

	// Validate();

	var article = new Object();
	article.advtno = $("#advt_no_drop_edit_advt").val();
	article.valid_from = $("#valid_from_edit_advt").val();
	article.valid_till = $("#valid_till_edit_advt").val();
	article.remarks = $("#remarks_edit").val();
	article.action = "editAdvt";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/AddAdvertismentServlet",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem editAdvtMethod. Please check again or contact website administrator");
				},
				success : function(data) {
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
						return;
					} else if (actionStr === "success") {
						$('#messageDiv').show();
						document.getElementById("messageDiv").innerHTML = messageStr;
						// $('#edit_adv').hide();
					} else {
						alert("Unknown result. Please contact website administrator");
					}
				}
			});
}

function deleteAdvtMethod() {
	// alert("deleteAdvtMethod called");

	// Validate();

	var article = new Object();
	article.advtno = $("#advt_no_drop_delete_ad").val();
	article.action = "deleteAdvt";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/AddAdvertismentServlet",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in delete Advt. Please check again or contact website administrator");
				},
				success : function(data) {
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
						return;
					} else if (actionStr === "success") {
						$('#messageDiv').show();
						document.getElementById("messageDiv").innerHTML = messageStr;
						$('#delete_adv1').removeClass("active");
						$('#dali').removeClass("active");
					} else {
						alert("Unknown result. Please contact website administrator");
					}
				}
			});
}

function addPostMethod() {
	// alert("Inside addPostMethod");
	// Validate();

	var article = new Object();
	article.advt_no_drop_add_post = $("#advt_no_drop_add_post").val();
	article.post_name_addPost = $("#post_name_addPost").val();
	article.post_no = $("#post_no").val();
	article.no_of_vacancies = $("#no_of_vacancies").val();
	article.post_remarks = $("#post_remarks").val();
	article.post_exp = $("#post_exp").val();
	article.post_sel = $("#post_sel").val();
	article.action = "addPost";

	var eStr = "";
	var x = document.getElementById("addElibilityDiv").childElementCount;
	// alert("Val : " + x);
	for (var i = 0; i < x; i++) {
		if ($("#add_cb" + i).prop("checked")) {
			var q = $("#add_cb" + i).val();
			// alert(q);
			var m = $("#add_md" + i).val();
			// alert(m);
			var d = ($("#add_ds" + i).val()).split("-")[0];
			// alert(d);
			var spA = $("#add_sp" + i).val();
			// alert(spA);
			if ((spA == null) || (spA.length === 0)) {
				alert("Please select a specialization for " + q);
				return;
			}
			var sp = "";
			for (var j = 0; j < spA.length; j++) {
				sp += spA[j] + "@";
			}
			if (sp.length > 0) {
				sp = sp.substring(0, sp.length - 1);
			}

			eStr += q + "#" + m + "#" + d + "#" + sp + "|";
		}
	}
	if (eStr.length > 0) {
		eStr = eStr.substring(0, eStr.length - 1);
	}
	article.eligibility_details = eStr;

	var tempStr = "";
	var tempArray = $("#desirable_qualification_addPost").val();
	if ((tempArray == null) || (tempArray.length == 0)) {
		tempStr = "NA";
	} else {
		for (var i = 0; i < tempArray.length; i++) {
			tempStr += tempArray[i] + "|";
		}
		if (tempStr !== "") {
			tempStr = tempStr.substring(0, tempStr.length - 1);
		}
	}
	article.desirable_qualification_addPost = tempStr;

	tempStr = "";
	tempArray = $("#nes_addPost").val();
	if ((tempArray == null) || (tempArray.length == 0)) {
		tempStr = "NA";
	} else {
		for (var i = 0; i < tempArray.length; i++) {
			tempStr += tempArray[i] + "#";
		}
		if (tempStr !== "") {
			tempStr = tempStr.substring(0, tempStr.length - 1);
		}
	}
	article.net = tempStr;

	var catStr = "";
	for (var i = 0; i < 9; i++) {
		if ($("#" + i + "_cat").prop("checked")) {
			var str = $("#" + i + "_cat").val() + "#"
					+ $("#" + i + "_min_age").val() + "#"
					+ $("#" + i + "_max_age").val();
			catStr += (str + "|");
		} else
			continue;
	}
	catStr = catStr.substring(0, catStr.length - 1);
	// alert(catStr);
	article.category = catStr;
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/AddPostServlet",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in addpost. Please check again or contact website administrator");
				},
				success : function(data) {
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
						return;
					} else if (actionStr === "success") {
						$('#messageDiv').show();
						document.getElementById("messageDiv").innerHTML = messageStr;
						$('#add_post1').removeClass("active");
						$('#apli').removeClass("active");
					} else {
						alert("Unknown result. Please contact website administrator");
					}
				}
			});
}

function editPostMethod() {
	// alert("Inside editPostMethod");
	// Validate();
	var article = new Object();
	article.advt_no_drop_edit_ad = $("#advt_no_drop_edit_ad").val();
	article.post_no_drop_edit = $("#post_no_drop_edit").val();
	article.post_name_edit = $("#post_name_edit").val();
	article.no_of_vacancies = $("#no_of_vacancies").val();
	article.action = "editPost";

	var tempStr = "";
	var tempArray = $("#eligibility_editPost").val();
	for (var i = 0; i < tempArray.length; i++) {
		tempStr += tempArray[i] + "|";
	}
	tempStr = tempStr.substring(0, tempStr.length - 1);
	article.eligibility_editPost = tempStr;

	var tempStr = "";
	var tempArray = $("#essential_qualification_editPost").val();
	for (var i = 0; i < tempArray.length; i++) {
		tempStr += tempArray[i] + "|";
	}
	tempStr = tempStr.substring(0, tempStr.length - 1);
	article.essential_qualification_editPost = tempStr;

	var tempStr = "";
	var tempArray = $("#discipline_editPost").val();
	for (var i = 0; i < tempArray.length; i++) {
		tempStr += tempArray[i] + "|";
	}
	tempStr = tempStr.substring(0, tempStr.length - 1);
	article.discipline_editPost = tempStr;

	var tempStr = "";
	var tempArray = $("#specialization_editPost").val();
	for (var i = 0; i < tempArray.length; i++) {
		tempStr += tempArray[i] + "|";
	}
	tempStr = tempStr.substring(0, tempStr.length - 1);
	article.specialization_editPost = tempStr;

	var tempStr = "";
	var tempArray = $("#desirable_qualification_editPost").val();
	for (var i = 0; i < tempArray.length; i++) {
		tempStr += tempArray[i] + "|";
	}
	tempStr = tempStr.substring(0, tempStr.length - 1);
	article.desirable_qualification_editPost = tempStr;

	var catStr = "";
	for (var i = 0; i < 9; i++) {
		if ($("#" + i + "_cat").prop("checked")) {
			var str = $("#" + i + "_cat").val() + "#"
					+ $("#" + i + "_min_age").val() + "#"
					+ $("#" + i + "_max_age").val();
			catStr += (str + "|");
		} else
			continue;
	}
	catStr = catStr.substring(0, catStr.length - 1);
	// alert(catStr);
	article.category = catStr;
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/AddPostServlet",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in Edit Post. Please check again or contact website administrator");
				},
				success : function(data) {
					var messageStr = data.Results[0];
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$('#messageDiv').show();
					$('#edit_post').removeClass("active");
					document.getElementById("messageDiv").innerHTML = messageStr;
				}
			});
}

function deletePostMethod() {
	// alert("Inside deletePostMethod");
	// Validate();
	var article = new Object();

	article.advt_no_drop_delete_ad_post = $("#advt_no_drop_delete_ad_post")
			.val();
	article.post_no_drop_delete = $("#post_no_drop_delete").val();
	article.action = "deletePost";
	article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
	$
			.ajax({
				beforeSend : function(xhr) {
					xhr.setRequestHeader('user', encodeURIComponent(email));
				},
				url : "/eRecruitment_NRSC/AddPostServlet",
				type : 'POST',
				dataType : 'json',
				data : JSON.stringify(article),
				contentType : 'application/json',
				mimeType : 'application/json',
				error : function(data, status, er) {
					alert("Problem in delete post. Please check again or contact website administrator");
				},
				success : function(data) {
					var messageStr = data.Results[0];
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					$('#messageDiv').show();
					$('#delete_post1').removeClass("active");
					$('#dpli').removeClass("active");
					document.getElementById("messageDiv").innerHTML = messageStr;
				}
			});
}

function AddPost(that) {
	if (that.value === "1") {
		document.getElementById("ifadd_post").style.display = "block";
		document.getElementById("ifadd_post1").style.display = "block";
		document.getElementById("ifadd_post2").style.display = "block";
		document.getElementById("ifadd_post3").style.display = "block";
	} else {
		document.getElementById("ifadd_post").style.display = "none";
		document.getElementById("ifadd_post1").style.display = "none";
		document.getElementById("ifadd_post2").style.display = "none";
		document.getElementById("ifadd_post3").style.display = "none";
	}
}

function readURL(input) {
	document.getElementById("filetype").value = "advertisement";
	var fileName = document.getElementById("advt_no").value;
	var fileName1 = document.getElementById("upload_adv").value;

	$('.preview').show();
	$('#blah').hide();
	$('.preview')
			.after(
					'<img id="blah" src="#" alt="advertisement" style="display:none;"/>');

	if (input.files && input.files[0]) {
		var reader = new FileReader();
		reader.onload = function(e) {
			$('#blah').attr('src', e.target.result).width(150).height(200);
		};
		reader.readAsDataURL(input.files[0]);
	}
	document.getElementById("Preview").style.display = "block";
}


function getPreview() {
	$('.preview').hide();
	$('#blah').show();
}

function createDynamicTable() {
	// alert("Inside Create Dynamic Table Method..");
	if ((document.getElementById("advt_no_drop_view").value.length == 0)
			|| (document.getElementById("advt_no_drop_view").value == "Select")) {
		alert("Please select advertisement number");
		return;
	}
	if ((document.getElementById("post_no_drop_view").value.length == 0)
			|| (document.getElementById("post_no_drop_view").value == "Select")) {
		alert("Please select post number");
		return;
	}
	var article = new Object();
	article.advtNo = document.getElementById("advt_no_drop_view").value;
	article.postNo = document.getElementById("post_no_drop_view").value;
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
					alert("Problem in Display Applications. Please check again or contact website administrator");
				},
				success : function(data) {
					// alert("inside success : " + data.Results);
					if (data.Results === null) {
						alert("No Records Found");
						return;
					}
					if (data.Results.length === 0) {
						alert("No Records Found");
						return;
					}
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					var tableData = data.Results;
					document.getElementById("ifViewSubmit").style.display = "block";
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
					th.innerHTML = "Adertisement No.";
					tr.appendChild(th);
					var th1 = document.createElement("th");
					th1.setAttribute("style", "width: 5%;");
					th1.innerHTML = "Post No.";
					tr.appendChild(th1);
					var th2 = document.createElement("th");
					th2.setAttribute("style", "width: 8%;");
					th2.innerHTML = "Post Name";
					tr.appendChild(th2);
					var th3 = document.createElement("th");
					th3.setAttribute("style", "width: 5%;");
					th3.innerHTML = "Vacancies";
					tr.appendChild(th3);
					var th3 = document.createElement("th");
					th3.setAttribute("style", "width: 8%;");
					th3.innerHTML = "Selection Process";
					tr.appendChild(th3);
					var th3 = document.createElement("th");
					th3.setAttribute("style", "width: 8%;");
					th3.innerHTML = "Experience";
					tr.appendChild(th3);
					th3 = document.createElement("th");
					th3.setAttribute("style", "width: 15%;");
					th3.innerHTML = "Remarks";
					tr.appendChild(th3);
//					var th4 = document.createElement("th");
//					th4.innerHTML = "Desirable Qualification";
//					th4.setAttribute("style", "width: 15%;");
//					tr.appendChild(th4);
//					var th6 = document.createElement("th");
//					th6.innerHTML = "National Examinations";
//					th6.setAttribute("style", "width: 15%;");
//					tr.appendChild(th6);
					var th5 = document.createElement("th");
					th5.setAttribute("style", "width: 15%;");
					th5.innerHTML = "Validity";
					tr.appendChild(th5);
					th5 = document.createElement("th");
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
					// th = document.createElement("th");
					// th.setAttribute("style", "width: 20%;");
					// th.innerHTML = "Discipline";
					// trel.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 30%;");
					th.innerHTML = "Specialization";
					trel.appendChild(th);
					//th = document.createElement("th");
					//th.setAttribute("style", "width: 40%;");
					//th.innerHTML = "Remarks";
					//trel.appendChild(th);

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
						tabCell.innerHTML = tableData[i]["select_process"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["experience"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["remarks"];
//						tabCell = tr.insertCell(-1);
//						tabCell.innerHTML = tableData[i]["desirable_qual"];
//						tabCell = tr.insertCell(-1);
//						tabCell.innerHTML = tableData[i]["net"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["start"]
								+ "     to     " + tableData[i]["end"];
						tabCell = tr.insertCell(-1);
						tabCell.innerHTML = tableData[i]["status"];

						var eArray = tableData[i]["Eligibility"];
						for (var j = 0; j < eArray.length; j++) {
							trel = eltable.insertRow(-1);
							var tabCell = trel.insertCell(-1);
							tabCell.innerHTML = eArray[j]["essential_qualification"];
							tabCell = trel.insertCell(-1);
							if (eArray[j]["mandatory"] === "Yes") {
								tabCell.innerHTML = "Mandatory";
							} else {
								tabCell.innerHTML = "Optional";
							}
							tabCell = trel.insertCell(-1);
							// tabCell.innerHTML = eArray[j]["discipline"];
							// tabCell = trel.insertCell(-1);
							var tempStr = eArray[j]["specialization"];
						//	alert("tempStr:"+tempStr);
							tempStr = tempStr.replace(/@/g, ' / ');
							//alert("tempStr after..:"+tempStr);
							tabCell.innerHTML = tempStr;
						//	tabCell = trel.insertCell(-1);
						//	tabCell.innerHTML = eArray[j]["remarks"];
							//alert("remarks after..:"+eArray[j]["remarks"]);
						//	tabCell = trel.insertCell(-1);
							// .split("@");
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
					var divContainer = document
							.getElementById("tableParentDiv");
					divContainer.innerHTML = "";
					divContainer.appendChild(table);
					divContainer.appendChild(eltable);
					// divContainer.appendChild(cattable);
				}
			});
}

function displayAll() {
	var article = new Object();
	article.advt_no_drop_report = $("#advt_no_drop_report").val();
	article.post_no_drop_report = $("#post_no_drop_report").val();
	article.action = "displayAll";
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
					alert("Problem in Dispalay All. Please check again or contact website administrator");
				},
				success : function(data) {
					if (data.Results[0] == "invalid_session") {
						// 12345
						alert("Invalid session identified. Redirecting to home page.");
						window.location.href = "https://apps.nrsc.gov.in/eRecruitment_NRSC/"; // ChangeId: 2025042203
						return;
					}
					// var messageStr = data.Results[0];
					// $('#messageDiv').show();
					// $('#delete_post').removeClass("active");
					// document.getElementById("messageDiv").innerHTML =
					// messageStr;
				}
			});
}

function ShowButtons(that) {
	if (that.value == "Admin_drop") {
		document.getElementById("go_admin").style.display = "block";
	} else {
		document.getElementById("go_admin").style.display = "none";
	}
}

function ShowButtons1(that) {
	if (that.value == "Applicant_drop") {
		document.getElementById("go_applicant").style.display = "block";
	} else {
		document.getElementById("go_applicant").style.display = "none";
	}
}

function ShowAdminConstraints(that) {

	document.getElementById("constraints").style.display = "block";

}
function ShowApplicantConstraints(that) {

	document.getElementById("constraints").style.display = "none";

}
function ShowReportDropdown(that) {
	document.getElementById("report_dropdown").style.display = "block";
}

function downloadExcel() {

	if ((document.getElementById("advt_no_drop_report").value.length == 0)
			|| (document.getElementById("advt_no_drop_view").value == "Select")) {
		alert("Please select advertisement number");
		return;
	}
	if ((document.getElementById("post_no_drop_report").value.length == 0)
			|| (document.getElementById("post_no_drop_view").value == "Select")) {
		alert("Please select post number");
		return;
	}
	var article = new Object();
	article.advt_No = document.getElementById("advt_no_drop_report").value;
	article.post_No = document.getElementById("post_no_drop_report").value;
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

function downloadCert() {

	if ((document.getElementById("advt_no_drop_report").value.length == 0)
			|| (document.getElementById("advt_no_drop_view").value == "Select")) {
		alert("Please select advertisement number");
		return;
	}
	if ((document.getElementById("post_no_drop_report").value.length == 0)
			|| (document.getElementById("post_no_drop_view").value == "Select")) {
		alert("Please select post number");
		return;
	}

	// advt_no = map1.get("advt_No").toString();
	// post_no = map1.get("post_No").toString();
	// String email = map1.get("email").toString();
	// String regId = map1.get("regId").toString();
	//	

	var article = new Object();
	article.advt_No = document.getElementById("advt_no_drop_report").value;
	article.post_No = document.getElementById("post_no_drop_report").value;
	article.action = "downloadCert";
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
					alert("Problem in Download certificate. Please check again or contact website administrator");
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

// function createReportDynamicTable() {
// //..................................................
// // alert("Inside Create Dynamic Table Report Method..");
// if ((document.getElementById("advt_no_drop_report").value.length == 0)
// || (document.getElementById("advt_no_drop_view").value == "Select")) {
// alert("Please select advertisement number");
// return;
// }
// if ((document.getElementById("post_no_drop_report").value.length == 0)
// || (document.getElementById("post_no_drop_view").value == "Select")) {
// alert("Please select post number");
// return;
// }
// var article = new Object();
// article.advtNo = document.getElementById("advt_no_drop_report").value;
// article.postNo = document.getElementById("post_no_drop_report").value;
// article.action = "postDetails";
// article.button = "button";
// article.vtoken = encodeURIComponent(localStorage.getItem("vtoken"));
// $
// .ajax({
// url : "/eRecruitment_NRSC/LoadAdvtNos",
// type : 'POST',
// dataType : 'json',
// data : JSON.stringify(article),
// contentType : 'application/json',
// mimeType : 'application/json',
// error : function(data, status, er) {
// alert("Webserver not responding. Please check again or contact website
// administrator");
// },
// success : function(data) {
// // alert("inside success : " + data.Results);
// if (data.Results === null) {
// alert("No records found");
// return;
// }
// if (data.Results.length === 0) {
// alert("No records found");
// return;
// }
// var tableData = data.Results;
// document.getElementById("ifViewSubmit").style.display = "block";
// var col = [];
// for (var i = 0; i < tableData.length; i++) {
// for ( var key in tableData[i]) {
// if (col.indexOf(key) === -1) {
// col.push(key);
// }
// }
// }

// // col.push("Button");

// var table = document.createElement("table");
// table.setAttribute("id", "admin_table");
// // table.setAttribute("style", "padding-bottom: inherit;
// // padding-top: inherit; text-align: center");

// var eltable = document.createElement("table");
// eltable.setAttribute("id", "adminel_table");
// eltable
// .setAttribute("style",
// "padding-bottom: inherit; padding-top: inherit; text-align: center");

// var cattable = document.createElement("table");
// cattable.setAttribute("id", "admincat_table");
// cattable
// .setAttribute("style",
// "padding-bottom: inherit; padding-top: inherit; text-align: center");

// var tr = table.insertRow(-1); // TABLE ROW.
// var th = document.createElement("th");
// th.setAttribute("style", "width: 10%;");
// th.innerHTML = "Advt No.";
// tr.appendChild(th);
// var th1 = document.createElement("th");
// th1.setAttribute("style", "width: 10%;");
// th1.innerHTML = "Post No.";
// tr.appendChild(th1);
// var th2 = document.createElement("th");
// th2.setAttribute("style", "width: 15%;");
// th2.innerHTML = "Post Name";
// tr.appendChild(th2);
// var th3 = document.createElement("th");
// th3.setAttribute("style", "width: 10%;");
// th3.innerHTML = "Vacancies";
// tr.appendChild(th3);
// var th4 = document.createElement("th");
// th4.innerHTML = "Desirable Qualification";
// th4.setAttribute("style", "width: 55%;");
// tr.appendChild(th4);
// var th5 = document.createElement("th");
// th5.setAttribute("style", "width: 10%;");
// th5.innerHTML = "Status";
// tr.appendChild(th5);

// var trel = eltable.insertRow(-1); // TABLE ROW.
// var th = document.createElement("th");
// th.setAttribute("style", "width: 20%;");
// th.innerHTML = "Qualification";
// trel.appendChild(th);
// th = document.createElement("th");
// th.setAttribute("style", "width: 10%;");
// th.innerHTML = "Mandatory";
// trel.appendChild(th);
// th = document.createElement("th");
// th.setAttribute("style", "width: 20%;");
// th.innerHTML = "Discipline";
// trel.appendChild(th);
// th = document.createElement("th");
// th.setAttribute("style", "width: 40%;");
// th.innerHTML = "Specialization";
// trel.appendChild(th);

// var trcat = cattable.insertRow(-1); // TABLE ROW.
// th = document.createElement("th");
// th.setAttribute("style", "width: 35%;");
// th.innerHTML = "Category";
// trcat.appendChild(th);
// th = document.createElement("th");
// th.setAttribute("style", "width: 35%;");
// th.innerHTML = "Min Age";
// trcat.appendChild(th);
// th = document.createElement("th");
// th.setAttribute("style", "width: 35%;");
// th.innerHTML = "Max Age";
// trcat.appendChild(th);
// // th = document.createElement("th");

// for (var i = 0; i < tableData.length; i++) {
// tr = table.insertRow(-1);
// var tabCell = tr.insertCell(-1);
// // tabCell.innerHTML = tableData[i][col[j]];
// tabCell.innerHTML = tableData[i]["advt_no"];
// tabCell = tr.insertCell(-1);
// tabCell.innerHTML = tableData[i]["post_no"];
// tabCell = tr.insertCell(-1);
// tabCell.innerHTML = tableData[i]["post_name"];
// tabCell = tr.insertCell(-1);
// tabCell.innerHTML = tableData[i]["no_of_vacancies"];
// tabCell = tr.insertCell(-1);
// tabCell.innerHTML = tableData[i]["desirable_qual"];
// tabCell = tr.insertCell(-1);
// tabCell.innerHTML = tableData[i]["status"];

// var eArray = tableData[i]["Eligibility"];
// for (var j = 0; j < eArray.length; j++) {
// trel = eltable.insertRow(-1);
// var tabCell = trel.insertCell(-1);
// tabCell.innerHTML = eArray[j]["essential_qualification"];
// tabCell = trel.insertCell(-1);
// tabCell.innerHTML = eArray[j]["mandatory"];
// tabCell = trel.insertCell(-1);
// tabCell.innerHTML = eArray[j]["discipline"];
// tabCell = trel.insertCell(-1);
// tabCell.innerHTML = eArray[j]["specialization"]
// .split("@");
// ;
// }
// var cArray = tableData[i]["Category"];
// for (var j = 0; j < cArray.length; j++) {
// trcat = cattable.insertRow(-1);
// var tabCell = trcat.insertCell(-1);
// tabCell.innerHTML = cArray[j]["category"];
// tabCell = trcat.insertCell(-1);
// tabCell.innerHTML = cArray[j]["age_limit_lower"];
// tabCell = trcat.insertCell(-1);
// tabCell.innerHTML = cArray[j]["age_limit_upper"];
// }
// }
// var divContainer = document
// .getElementById("tableParentDiv");
// divContainer.innerHTML = "";
// divContainer.appendChild(table);
// divContainer.appendChild(eltable);
// divContainer.appendChild(cattable);
// }
// });

// }
// ifReportSubmitApp

// $("#btnExport").click(function(e) {
// alert("1234");
// window.open('data:application/vnd.ms-excel,' +
// $('#ifReportSubmitApp').html());
// e.preventDefault();
// });

function tableExcel(e) {
	// alert("1234");
	window.open('data:application/vnd.ms-excel,'
			+ $('tableParentReportAdminDiv').html());
	// e.preventDefault();
}

function getAge(birth) {

	var today = new Date();
	var nowyear = today.getFullYear();
	var nowmonth = today.getMonth();
	var nowday = today.getDate();

	var birthyear = birth.getFullYear();
	var birthmonth = birth.getMonth();
	var birthday = birth.getDate();

	var age = nowyear - birthyear;
	var age_month = nowmonth - birthmonth;
	var age_day = nowday - birthday;

	if (age_month < 0 || (age_month == 0 && age_day < 0)) {
		age = parseInt(age) - 1;
	}
	alert(age);

}

function createReportDynamicTable() {

	// alert("Inside Create Dynamic Table Method..");
	if ((document.getElementById("advt_no_drop_report").value.length == 0)
			|| (document.getElementById("advt_no_drop_view").value == "Select")) {
		alert("Please select advertisement number");
		return;
	}
	if ((document.getElementById("post_no_drop_report").value.length == 0)
			|| (document.getElementById("post_no_drop_view").value == "Select")) {
		alert("Please select post number");
		return;
	}
	var divContainer = document.getElementById("tableParentReportAdminDiv");
	divContainer.innerHTML = "";

	var article = new Object();
	article.advtNo = document.getElementById("advt_no_drop_report").value;
	article.postNo = document.getElementById("post_no_drop_report").value;
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
					document.getElementById("ifReportSubmitAdmin").style.display = "block";
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
	createApplicantDetails();

}

function sortTable(n) {
	var table;
	table = document.getElementById("table");
	var rows, i, x, y, count = 0;
	var switching = true;

	// Order is set as ascending
	var direction = "ascending";

	// Run loop until no switching is needed
	while (switching) {
		switching = false;
		var rows = table.rows;

		// Loop to go through all rows
		for (i = 1; i < (rows.length - 1); i++) {
			var Switch = false;

			// Fetch 2 elements that need to be compared
			x = rows[i].getElementsByTagName("TD")[n];
			y = rows[i + 1].getElementsByTagName("TD")[n];

			// Check the direction of order
			if (direction == "ascending") {

				// Check if 2 rows need to be switched
				if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
					// If yes, mark Switch as needed and break loop
					Switch = true;
					break;
				}
			} else if (direction == "descending") {

				// Check direction
				if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
					// If yes, mark Switch as needed and break loop
					Switch = true;
					break;
				}
			}
		}
		if (Switch) {
			// Function to switch rows and mark switch as completed
			rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
			switching = true;

			// Increase count for each switch
			count++;
		} else {
			// Run while loop again for descending order
			if (count == 0 && direction == "ascending") {
				direction = "descending";
				switching = true;
			}
		}
	}
}

function createApplicantDetails() {
	var divContainer = document.getElementById("tableParentReportAppDiv");
	divContainer.innerHTML = "";

	// onclick="sortTable(0)

	var article = new Object();
	 //alert("HELLO....");
	article.advtNo = document.getElementById("advt_no_drop_report").value;
	article.postNo = document.getElementById("post_no_drop_report").value;
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

					document.getElementById("ifReportSubmitApp").style.display = "block";
					var col = [];
					// alert(tableData.length + ":len ");
					for (var i = 0; i < tableData.length; i++) {
						for ( var key in tableData[i]) {
							if (col.indexOf(key) === -1) {
								col.push(key);
							}
						}
					}

					var totalHigh = tableData[0]["HighEdCnt"];

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

					var itiSize = tableData[0]["ITISize"];
					var pertable = document.createElement("table");
					pertable.setAttribute("id", "personal");
					pertable
							.setAttribute("style",
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
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Email";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "Category";
					tper.appendChild(th);
					th = document.createElement("th");
					th.setAttribute("style", "width: 10%;");
					th.innerHTML = "PWD";
					tper.appendChild(th);
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

					if (itiSize > 0) {
						// var trx = xtable.insertRow(-1); // TABLE ROW.
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
					}

					 //alert("TOTAL is :" + totalHigh);
					var qualHeader = "Graduate";

					if (totalHigh > 0) {
						var qualLen = 2;
						if (totalHigh == 1)
							qualLen = 1;

						for (var h = 0; h < qualLen; h++) {
							var th = document.createElement("th");
							th.setAttribute("style", "width: 3%;");
							if (h == 1)
								qualHeader = "Postgraduate";
							th.innerHTML = qualHeader;
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
						}
						if (totalHigh > 2) {
							for (var h = 2; h < totalHigh; h++) {
								var th = document.createElement("th");
								th.setAttribute("style", "width: 3%;");
								if (h == 2)
									qualHeader = "Doctorate";
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
					}

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
							tabCell.innerHTML = pArray[p]["email"];
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["category"];
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["category_pwd"];
                                                        
                                                        /*Start: ChangeId:2025041501*/
                                                        tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["category_pwd_scribe"];
                                                        /*End: ChangeId:2025041501*/
                                                        
                                                        /*Start: ChangeId:2025050807*/
                                                        tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["category_pwd_comptime"];
                                                        /*End: ChangeId:2025050807*/
                                                        
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = pArray[p]["category_ews"];
							totExp=pArray[p]["totalexperience"];
							//alert("totExp:"+totExp);
							

							var xArray = tableData[i]["X"];
							for (var j = 0; j < xArray.length; j++) {
//								alert("j:"+j);

								var extInd = xArray[j]["marksheet"]
										.lastIndexOf(".");
								var extn = xArray[j]["marksheet"]
										.substring(extInd);
//								 alert("....x:"+extn+"val is	:"+xArray[j]["marksheet"].substring(extn));
								// tper = pertable.insertRow(-1);
								// var filepath = uploadDir_app
								// + xArray[j]["marksheet"];

								var filepath = uploadDir_app + replaceX + extn;

								var btnM = document.createElement('a');
								btnM.href = filepath;
								btnM.target = "_blank";
								var perVal = xArray[j]["x_percentage"];
								var cgVal = xArray[j]["x_cgpa_obt"];
								if (perVal == -1) {
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
							for (var j = 0; j < xArray.length; j++) {

								// var filepath = uploadDir_app
								// + xArray[j]["marksheet"];
								var extInd = xArray[j]["marksheet"]
										.lastIndexOf(".");
								var extn = xArray[j]["marksheet"]
										.substring(extInd);
//								 alert("....xii:"+extn+"val is	:"+xArray[j]["marksheet"].substring(extn));
								var filepath = uploadDir_app + replaceXii
										+ extn;
								var btnM = document.createElement('a');
								btnM.href = filepath;
								btnM.target = "_blank";
								var perVal = xArray[j]["xii_percentage"];
								var cgVal = xArray[j]["xii_cgpa_obt"];
								if (perVal == -1) {
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

							if (itiSize > 0) {
								xArray = tableData[i]["ITI"];
								for (var j = 0; j < xArray.length; j++) {

									// var filepath = uploadDir_app
									// + xArray[j]["marksheet"];
									var extInd = xArray[j]["marksheet"]
											.lastIndexOf(".");
									var extn = xArray[j]["marksheet"]
											.substring(extInd);
									var filepath = uploadDir_app + replaceiti
											+ extn;
									var btnM = document.createElement('a');
									btnM.href = filepath;
									btnM.target = "_blank";
									var perVal = xArray[j]["iti_percentage"];
									var cgVal = xArray[j]["iti_cgpa_obt"];
									if (perVal == -1) {
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
									tabCell.innerHTML = xArray[j]["iti_year_of_passing"];
								}
							}

							if (xArray.length === 0) {
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = "-";
							}

//							alert(totalHigh + " is therefore");
							var gradIntegratedFlag = false;
							if (totalHigh > 0) {
								xArray = tableData[i]["Higher"];
								var xArrLen = xArray.length;
								var j = 0;
								{
									var replaceGradDeg = "degree_certificate_Graduate_"
											+ email_replace;
									var replacePostDeg = "degree_certificate_Postgraduate_"
											+ email_replace;

									var replaceGradMark = "marksheet_Graduate_"
											+ email_replace;

									var qualType = xArray[j]["qualification_type"];
									var qualFlag = false;
									var postqualFlag = false;
									var phdFlag = false;
                                                                        gradIntegratedFlag = false;

									if (qualType == "Graduate")
										qualFlag = true;
                                                                        if (xArray[j]["qualification"].startsWith("Integrated"))
                                                                            gradIntegratedFlag = true;
//									 alert(qualType + "," + qualFlag + "," + j+","+gradIntegratedFlag);

									if (qualFlag) {
										var extIndD = xArray[j]["degree_certificate"]
												.lastIndexOf(".");
										var extnD = xArray[j]["degree_certificate"]
												.substring(extIndD);
										var filepath_deg = uploadDir_app
												+ replaceGradDeg + extnD;

										var extIndM = xArray[j]["marksheet"]
												.lastIndexOf(".");
										var extnM = xArray[j]["marksheet"]
												.substring(extIndM);
										var filepath = uploadDir_app
												+ replaceGradMark + extnM;

										var qual = xArray[j]["qualification"];
										if (qual.startsWith("Integrated"))
											gradIntegratedFlag = true;
//										 alert(qualType + "," + qualFlag + ","+ j+","+gradIntegratedFlag);

										var btnd = document.createElement('a');
										btnd.href = filepath_deg;
										btnd.target = "_blank";
										btnd.innerHTML += qual;
										
																				
										//btnd = document.createElement('a');
										//btnd.href = ",";
										//btnd.target = "_blank";
										if(j>=1)
                                                                                    btnd.innerHTML += ",";
										//btnd.innerHTML += "hello";
										
										tabCell = tper.insertCell(-1);
										tabCell.appendChild(btnd);										

										// var filepath = uploadDir_app+
										// xArray[j]["marksheet"];
										var btnM = document.createElement('a');
										btnM.href = filepath;
										btnM.target = "_blank";

										var perVal = xArray[j]["percentage"];
										var cgVal = xArray[j]["cgpa_obt"];
                                                                                var cgValMax = xArray[j]["cgpa_max"];
										if (perVal == -1) {
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
										j = j + 1;

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

									}

									//if (totalHigh > 1) 
									{
										/***************** 15 SEP 2020*****************************************
										// alert("j:"+j+","+xArray[0]["qualification_type"]
										// + "," +
										// xArray[1]["qualification_type"] + ","
										// + xArray[2]["qualification_type"]);
										if (xArrLen > 1) {
											qualType = xArray[j]["qualification_type"];
											if (qualType == "Postgraduate")
												qualFlag = true;
											else
												qualFlag = false;
										} else if (xArrLen == 1)// if
										// (gradIntegratedFlag)
										{
											qualFlag = false;
										}
                                      ***************************************************************/
                                                                                
                                                                                qualFlag = false;
										if ((xArrLen === 1 && gradIntegratedFlag && j===0) || xArrLen > 1) {
											qualType = xArray[j]["qualification_type"];
											if (qualType === "Postgraduate") {
												qualFlag = true;
                                                                                        } else {
												qualFlag = false;
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
									
										if (xArrLen >= 1 && qualFlag) {

											var extIndD = xArray[j]["degree_certificate"]
													.lastIndexOf(".");
											var extnD = xArray[j]["degree_certificate"]
													.substring(extIndD);
											var filepath_deg = uploadDir_app
													+ replacePostDeg + extnD;
											// alert("filepath_deg:"+filepath_deg+","+extnD);

											var extIndM = xArray[j]["marksheet"]
													.lastIndexOf(".");
											var extnM = xArray[j]["marksheet"]
													.substring(extIndM);
											var filepath = uploadDir_app
													+ replacePostDeg
															.replace(
																	"degree_certificate",
																	"marksheet")
													+ extnM;

											var btnd = document
													.createElement('a');
											btnd.href = filepath_deg;
											btnd.target = "_blank";
											btnd.innerHTML = xArray[j]["qualification"];
											tabCell = tper.insertCell(-1);
											tabCell.appendChild(btnd);

											// var filepath = uploadDir_app +
											// xArray[j]["marksheet"];
											var btnM = document
													.createElement('a');
											btnM.href = filepath;
											btnM.target = "_blank";

											var perVal = xArray[j]["percentage"];
											var cgVal = xArray[j]["cgpa_obt"];
											
											if (perVal == -1) {
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
											j = j + 1;

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
										}
									}

								}
							}

							if (totalHigh > 2) {
								// alert("j is ...: "+j);
								// j=1;
								var qualType = xArray[j]["qualification"];
								if (qualType == "Phd")
									qualType = "PhD";
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

								// tabCell = tper.insertCell(-1);
								// tabCell.innerHTML =
								// xArray[j]["qualification"];

								// filepath_thesis = uploadDir_app+
								// xArray[j]["phdabstract"];
								var btnt = document.createElement('a');
								btnt.href = filepath_thesis;
								btnt.target = "_blank";
								btnt.innerHTML = xArray[j]["phdtopic"];
								tabCell = tper.insertCell(-1);
								tabCell.appendChild(btnt);

								tabCell = tper.insertCell(-1);
								tabCell.innerHTML = xArray[j]["year_of_passing"];

							}

							xArray = tableData[i]["Experience"];
							var govtExp = tableData[i]["GovtExperience"];

							tabCell = tper.insertCell(-1);
							// if (xArray.length > 0) {
							tabCell.innerHTML = "Yes";
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = govtExp;
							// } else {
							// tabCell.innerHTML = "No";
							// }
							tabCell = tper.insertCell(-1);
							tabCell.innerHTML = totExp;
						}

					}
					divContainer.appendChild(pertable);

				}
			});
}

var xport = {
	_fallbacktoCSV : true,
	toXLS : function(tableId, filename) {
		this._filename = (typeof filename == 'undefined') ? tableId : filename;

		// var ieVersion = this._getMsieVersion();
		// Fallback to CSV for IE & Edge
		if ((this._getMsieVersion() || this._isFirefox())
				&& this._fallbacktoCSV) {
			return this.toCSV(tableId);
		} else if (this._getMsieVersion() || this._isFirefox()) {
			alert("Not supported browser");
		}

		// Other Browser can download xls
		var htmltable = document.getElementById(tableId);
		var html = htmltable.outerHTML;

		this._downloadAnchor("data:application/vnd.ms-excel"
				+ encodeURIComponent(html), 'xls');
	},
	toCSV : function(tableId, filename) {
		this._filename = (typeof filename === 'undefined') ? tableId : filename;
		// Generate our CSV string from out HTML Table
		var csv = this._tableToCSV(document.getElementById(tableId));
		// Create a CSV Blob
		var blob = new Blob([ csv ], {
			type : "text/csv"
		});

		// Determine which approach to take for the download
		if (navigator.msSaveOrOpenBlob) {
			// Works for Internet Explorer and Microsoft Edge
			navigator.msSaveOrOpenBlob(blob, this._filename + ".csv");
		} else {
			this._downloadAnchor(URL.createObjectURL(blob), 'csv');
		}
	},
	_getMsieVersion : function() {
		var ua = window.navigator.userAgent;

		var msie = ua.indexOf("MSIE ");
		if (msie > 0) {
			// IE 10 or older => return version number
			return parseInt(ua.substring(msie + 5, ua.indexOf(".", msie)), 10);
		}

		var trident = ua.indexOf("Trident/");
		if (trident > 0) {
			// IE 11 => return version number
			var rv = ua.indexOf("rv:");
			return parseInt(ua.substring(rv + 3, ua.indexOf(".", rv)), 10);
		}

		var edge = ua.indexOf("Edge/");
		if (edge > 0) {
			// Edge (IE 12+) => return version number
			return parseInt(ua.substring(edge + 5, ua.indexOf(".", edge)), 10);
		}

		// other browser
		return false;
	},
	_isFirefox : function() {
		if (navigator.userAgent.indexOf("Firefox") > 0) {
			return 1;
		}

		return 0;
	},
	_downloadAnchor : function(content, ext) {
		var anchor = document.createElement("a");
		anchor.style = "display:none !important";
		anchor.id = "downloadanchor";
		document.body.appendChild(anchor);

		// If the [download] attribute is supported, try to use it

		if ("download" in anchor) {
			anchor.download = this._filename + "." + ext;
		}
		anchor.href = content;
		anchor.click();
		anchor.remove();
	},
	_tableToCSV : function(table) {
		// We'll be co-opting `slice` to create arrays
		var slice = Array.prototype.slice;

		return slice.call(table.rows).map(function(row) {
			return slice.call(row.cells).map(function(cell) {
				return '"t"'.replace("t", cell.textContent);
			}).join(",");
		}).join("\r\n");
	}
};


