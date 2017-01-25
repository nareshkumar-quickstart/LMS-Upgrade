// Use for popup window ..........

function popup(url) 
{
    var width  = 1020;
    var height = 600;
    var left   = (screen.width  - width)/2;
    var top    = (screen.height - height)/2;
    var params = 'width='+width+', height='+height;
    params += ', top='+top+', left='+left;
    params += ', directories=no';
    params += ', location=no';
    params += ', menubar=no';
    params += ', resizable=yes';
    params += ', scrollbars=no';
    params += ', status=no';
    params += ', toolbar=no';
    var newwin=window.open(url,'windowname5', params);
    if (window.focus) {
        newwin.focus()
        }
    return false;
}
	
function popupnew(url, windowtitle, width, height) 
{
    var left   = (screen.width  - width)/2;
    var top    = (screen.height - height)/2;
    var params = 'width='+width+', height='+height;
    params += ', top='+top+', left='+left;
    params += ', directories=no';
    params += ', location=no';
    params += ', menubar=no';
    params += ', resizable=yes';
    params += ', scrollbars=no';
    params += ', status=no';
    params += ', toolbar=no';
    var newwin=window.open(url, windowtitle, params);
    if (window.focus) {
        newwin.focus()
        }
    return false;
}	

/* ============================================================================== */
/* =================== for adding new button to the Wizard based forms ========== */
/* ============================================================================== */
function submitButtonForm(form,defaultName , newName)
{
    document.getElementById(defaultName).name = newName ;
    form.submit();
}

//For Custom Fields
function checkAnswer(objCheckBox){
    if(objCheckBox.checked){
        document.getElementById('hdnChoice_'+objCheckBox.id).value="true";
    }else {
        document.getElementById('hdnChoice_'+objCheckBox.id).value="false";
    }
}

/**
 * Toggles the view and also changes the icon.
 * @param toggleIconId id of toggle button icon.
 * @param toggleViewId id of the div that supposed to show or hide.
 */
function toggleView(toggleIconId, toggleViewId){
    $("#" + toggleViewId).toggle();
    toggleArrowIcon(toggleIconId);
}
	
/** 
 * Toggles the arrow icon. 
 * @param toggleIconId id of toggle button icon.
 */
function toggleArrowIcon(toggleIconId){
    //TODO: Investigate $(selector).toggleClass later.
    var selector = "#" + toggleIconId;
    if( $(selector).attr('class') == 'icon-caret-down-large') {
        $(selector).removeClass('icon-caret-down-large');
        $(selector).addClass('icon-caret-right-large');
    }else{
        $(selector).removeClass('icon-caret-right-large');
        $(selector).addClass('icon-caret-down-large');
    }
}

function restoreTable(iconDiv, name){
    toggleArrowIconObject(iconDiv);
    if( $(iconDiv).attr('class') == 'icon-caret-down-large') {
        $('table tr[name=' + name + ']').css('display','table-row');
    }else{
        $('table tr[name=' + name + ']').css('display','none');
    }
}

function toggleArrowIconObject(iconDiv){
    if( $(iconDiv).attr('class') == 'icon-caret-down-large') {
        $(iconDiv).removeClass('icon-caret-down-large');
        $(iconDiv).addClass('icon-caret-right-large');
    }else{
        $(iconDiv).removeClass('icon-caret-right-large');
        $(iconDiv).addClass('icon-caret-down-large');
    }
}

/**
 * Used on Add Course Configuraiton template page.
 * This method enables disables the input items in the form based on
 * corresponding checkbox.
 * 
 * It uses passed in checkbox to find corresponding input controls.
 * @param checkbox checkbox whose input control we want to disable.
 * @param masterCheckbox checkbox which is responsible to enable and disable the form.
 */
function checkboxChanged(checkbox, masterCheckbox) {
    var siblings = checkbox.parent('td').next();
    var select = siblings.find("select");
    var input = siblings.find("input");
    if(checkbox.is(':checked') && $(masterCheckbox).is(':checked')){
        select.removeAttr('disabled');
        input.removeAttr('disabled');
    }else{
        select.attr('disabled', true);
        input.attr('disabled', true);
    }
}

/**
 * Used on Add Course Configuraiton template pages 4,5,6.
 */

function toggleNoResults(message, afterFailing, allowreviewing)
{
    if(message=="No Results"){
        $('#NoResultsRow').css('display','block');
        if( !allowreviewing == false || allowreviewing == undefined)
            $('#allowreviewing').attr('disabled', true);
        $('#gradeQuestion').attr('disabled', true);
        if( !afterFailing == false || afterFailing == undefined){
            $('#afterFailing').attr('disabled', true);
            $('#afterFailing').removeAttr('checked');
        }
    }
    else
    {
        $('#NoResultsRow').css('display','none');
        if( !allowreviewing || allowreviewing == undefined)
            $('#allowreviewing').removeAttr('disabled');
        $('#gradeQuestion').removeAttr('disabled');
        if( !afterFailing || afterFailing == undefined){
            $('#afterFailing').removeAttr('disabled');
            $('#afterFailing').attr('checked', true);
        }
    }
}

//IE7 seems to have a bug which prevents the 'change' event from checkbox and radio to fire.
//Latest versions of jQuery have fixed this issue but for older version needs to have this hack.
//This hack right now only fixes the checkboxes for radio we will need to add to the selector.
$(function () {
    if ($.browser.msie) {
        $('input:checkbox').click(function () {
            this.blur();
            this.focus();
        });
    }
});

//InfoBar: call Toggle for Infobar
var bInfoBarShow = 0;
function setInfoBarToggle()
{	
	if (bInfoBarShow == 0)
	{
		$("#message_box").show();
		bInfoBarShow = 1;
	}	
}
