
var browser=navigator.appName;
var b_version=navigator.appVersion;
var version=parseFloat(b_version);

	if(browser == "Netscape"){
		document.write("<link type='text/css' href='brands/default/en/css/css_search_net.css' rel='stylesheet'/>");	
	}else{
		document.write("<link type='text/css' href='brands/default/en/css/css_search.css' rel='stylesheet'/>");
	}

function minimizeSearch(){
		var iconSimple = document.getElementById("iconSimple");
		var iconAdvanced = document.getElementById("iconAdvanced");
		var tblSimpleSearch=document.getElementById("tblSimpleSearch");
		var divPlaceSimple=document.getElementById("divPlaceSimple");
		var tblAdvancedSearch=document.getElementById("tblAdvancedSearch");
		var divPlaceAdvanced=document.getElementById("divPlaceAdvanced");

			if(tblSimpleSearch.style.display=="block"){
				 tblSimpleSearch.style.display="none";
				 divPlaceSimple.style.display="none";
				 iconSimple.className = "icon_maximize"
			}else{
				 tblSimpleSearch.style.display="block";
				 divPlaceSimple.style.display="block";
				 iconSimple.className = "icon_minimize"
			}

			if(tblAdvancedSearch.style.display=="block"){
		         tblAdvancedSearch.style.display="none";
				 divPlaceAdvanced.style.display="none";
				 iconAdvanced.className = "icon_maximize"
			}else{
				 tblAdvancedSearch.style.display="block";
				 divPlaceAdvanced.style.display="block";
				 iconAdvanced.className = "icon_minimize"
			}
	}
	

	
function showResults(div_id){
	var divId = document.getElementById(div_id);
		divId.className = "search_box_result";
	}

function showTooltip(captionText, e){
	var tooltip = document.getElementById("tooltipForPageList");	
	if(tooltip!=null){	
	    tooltip.innerHTML = captionText;
	 	tooltip.style.left = e.clientX - 75 +'px';
		tooltip.style.top = e.clientY - 40+'px';
		tooltip.style.visibility = "Visible";
	}
	}

function hideTooltip(){
	var tooltip = document.getElementById("tooltipForPageList");
	if(tooltip!=null)	
	tooltip.style.visibility = "Hidden";
	}

function switchSort(subName){
		var subheading = document.getElementById(subName);
		if(subheading.className=="icon_up"){
			subheading.className="icon_down"
			}else{
				subheading.className="icon_up"
			}
		}
function showSearchBox(divId){
	var searchBox = document.getElementById('searchBox');
		if(searchBox.className == "visible"){
			searchBox.className = "search_box";
			$('#searchBox input[type="text"]:first').focus();
		}else{
			searchBox.className = "visible";			
		}	
	}
function showResult(){
	var resultBox_1 = document.getElementById('searchResult_1');
	var resultBox_2 = document.getElementById('searchResult_2');
	var searchBox = document.getElementById('searchBox');
	var resultDescription = document.getElementById('resultDescription');
		searchBox.className = "visible";
		resultBox_1.className = "visible";
		resultBox_2.className = "null";
		resultDescription.innerHTML= "Showing 1 - 5 of 10";

	}
function checkAll(field, formObject){
		if(formObject.checked==true){
			for (i = 0; i < field.length; i++)
			field[i].checked = true;
		}else{
			for (i = 0; i < field.length; i++)
			field[i].checked = false;
			}
	}
function checkOrgGroup(my, lastRow){
			if(my.className == "icon_checkbox"){
				my.className = "icon_checked";
			}else{
				my.className = "icon_checkbox";
			}
		//
			var rows = document.getElementsByName("row");
			var	numberOfRows = rows.length;
			var	thisRowTitle = my.parentNode.title;
			for(i=0; i<=numberOfRows; i++){
				if(thisRowTitle == rows[i].title){
				//alert("My Title: "+rows[i].title);
				break;
				}
			}
			//
			for(j=i+1; j<=numberOfRows; j++){
				if(rows[j].title == lastRow){
				break;
				}else{
					//alert("My Child Childs: "+rows[j].childNodes.length);
					var cildNodesLength = rows[j].childNodes.length
					if(my.className == "icon_checked" && rows[j].childNodes[cildNodesLength-2].className == "icon_checkbox"){
						//alert(1);
						rows[j].childNodes[cildNodesLength-2].className = "icon_checked";
					}else if(my.className == "icon_checked" && rows[j].childNodes[cildNodesLength-2].className == "icon_checked"){
						//alert(2);
						rows[j].childNodes[cildNodesLength-2].className = "icon_checked";
					}else if(my.className == "icon_checkbox" && rows[j].childNodes[cildNodesLength-2].className == "icon_checked"){
						//alert(3);
						rows[j].childNodes[cildNodesLength-2].className = "icon_checkbox"
					}
				}
			}
			//
		//
		//alert(my.parentNode.childNodes.length);
	}
function cRows(my, lastRow){
	var rows = document.getElementsByName("row");
	var	numberOfRows = rows.length;
	var	thisRowTitle = my.parentNode.title;
	//
	if(my.className == "icon_expand"){
		my.className = "icon_collapse";
		}else{
		my.className = "icon_expand";
		}
	//
	var i = 0;
	var j = 0;
	//	
	for(i=0; i<=numberOfRows; i++){
		if(thisRowTitle == rows[i].title){
			break;
			}
		}
	for(j=i+1; j<=numberOfRows; j++){
		if(rows[j].title == lastRow){
			break;
		}else{
			if(my.className == "icon_expand"){
				nodeLength = rows[j].childNodes.length;
				if(rows[j].childNodes[nodeLength-nodeLength+3].className == "icon_expand"){
					rows[j].childNodes[nodeLength-nodeLength+3].className = "icon_collapse";
					}
				if(rows[j].childNodes[nodeLength-nodeLength+7].className == "icon_expand"){
					rows[j].childNodes[nodeLength-nodeLength+7].className = "icon_collapse";
					}
				rows[j].style.display="none";
			}else{
				rows[j].style.display = "";
				}
			}
		}
	}







function showMessageBox(divID){
	var div_id = document.getElementById(divID);
	div_id.style.visibility = "Visible";
	}

function hideMessageBox(divID){
	var div_id = document.getElementById(divID);
	    div_id.style.visibility = "Hidden";
	var chkBox = document.getElementById("check_box")	
	    chkBox.disabled = false;
	}

function deleteLearners(){
	var chkBox = document.getElementById("check_box")
	if(chkBox.checked == false){
		showMessageBox("selectLearner");
		chkBox.disabled = true;
	}else{
		showMessageBox("deleteConfirmation");
		chkBox.disabled = true;
		}
	}

function okFunction(){
	hideMessageBox('deleteConfirmation');
	alert("OK Function");
	}

function cancelFunction(){
	hideMessageBox('deleteConfirmation');
	alert("Cancel Function");
	}
	
function secondContact(pRef){
    var second_contact = document.getElementById("second_contact")
	if(pRef=="show"){
	      second_contact.style.display = "block";
	}else{
	      second_contact.style.display = "none";
	}
}


function addReg(){
  var asReg = document.getElementById("asReg");
  var avReg = document.getElementById("avReg");
  var y = document.createElement('option');
  y.text = avReg.options[avReg.selectedIndex].text;
      asReg.add(y, null);
	  avReg.remove(avReg.selectedIndex);
 }
 
function removeReg(){
  var asReg = document.getElementById("asReg");
  var avReg = document.getElementById("avReg");
  var y = document.createElement('option');
  y.text = asReg.options[asReg.selectedIndex].text;
      avReg.add(y, null);
	  asReg.remove(asReg.selectedIndex);
 }

function approvalType(){
  var crsApr = document.getElementById("courseApproval");
  var prvApr = document.getElementById("providerApproval");
  var insApr = document.getElementById("instructorApproval");
  
  	  if(crsApr.checked == true){
	  	  window.location = "add_approval_course_regulator.jsp"
	  }else if(prvApr.checked == true){
	  	  window.location = "add_approval_provider_regulator.jsp" 	
	  }else if(insApr.checked == true){
	      window.location = "add_approval_instructor_regulator.jsp"
	  }
	  
	  
}

function reportingType(){
  var rptCrd = document.getElementById("reportCredits");
  var prtCer = document.getElementById("printCertificates");
  
  	  if(rptCrd.checked == true){
	  	  window.location = "report_credit.jsp"
	  }else if(prtCer.checked == true){
	  	  window.location = "print_certificates.jsp" 	
	  }  
}

function skipSelection(){
		 var sltAll = document.getElementById("selectAll");

		 if(sltAll.checked == true){
	  	  window.location = "report_credit_options.jsp"
	 	  }else {
	  	  window.location = "report_credit_student.jsp" 	
	  }
		 
	}
	
function collapseTable(tableId, tableIcon){
		var collapseTable = document.getElementById(tableId);
		var tableIcon = document.getElementById(tableIcon);
			if(collapseTable.className == "search_box_result"){
		         collapseTable.className = "search_box_minimized";
				 tableIcon.className = "icon_right";
			}else{
				 collapseTable.className = "search_box_result";
				 tableIcon.className = "icon_down"
			}
	}