var descriptionText = "Simple Search"

var browser=navigator.appName;
var b_version=navigator.appVersion;
var version=parseFloat(b_version);
    if(browser == "Netscape"){
        document.write("<link type='text/css' href='brands/default/en/css/css_search_net.css' rel='stylesheet'/>")    
    }else{
        document.write("<link type='text/css' href='brands/default/en/css/css_search.css' rel='stylesheet'/>")    
    }

function minimizeSearch(){
        var searchBox = document.getElementById("searchBox");
        var boxDescription = document.getElementById("boxDescription");
        var boxIcon = document.getElementById("boxIcon");
        if(searchBox.className == "search_box_simple" || searchBox.className == "search_box_advance"){
            searchBox.className = "search_box_minimized";
            boxIcon.className = "icon_maximize";
        }else if(searchBox.className == "search_box_minimized"){
            if(boxDescription.innerHTML == "Simple Search"){
                searchBox.className = "search_box_simple";
                }else{
                    searchBox.className = "search_box_advance";
                    }
            boxIcon.className = "icon_minimize";
        }
    }
    
function switchSearchMode(){
        var fieldOne = document.getElementById("fieldOne");
        var fieldTwo = document.getElementById("fieldTwo");
        var fieldThree = document.getElementById("fieldThree");
        var fieldFour = document.getElementById("fieldFour");
        var boxDescription = document.getElementById("boxDescription");
        var inputFieldTwo = document.getElementById("inputFieldTwo");
        var searchButton = document.getElementById("searchButton");
            if(descriptionText == "Advance Search"){
                switchText("Name", "", "<a href='#'onmousedown='switchSearchMode()'>Advance Search</a>", "", "Simple Search", "search_box_simple")
                searchButton.className = "search_button_simple"
                //inputFieldTwo.className="visible"
                //inputFieldThree.className="visible"
            }else if(descriptionText == "Simple Search"){
                switchText("First Name", "Last Name", "Email Address", "<a href='#'onmousedown='switchSearchMode()'>Simple Search</a>", "Advance Search", "search_box_advance")
                searchButton.className = "search_button_advance"
                //inputFieldTwo.className="input_text"
                //inputFieldThree.className="input_text"
            }

    }
function switchText(field_one, field_two, field_three, field_four, box_description, search_box){
    fieldOne.innerHTML = field_one;
    fieldTwo.innerHTML = field_two
    fieldThree.innerHTML = field_three;
    fieldFour.innerHTML = field_four;
    boxDescription.innerHTML = box_description;
    descriptionText = box_description;
    var searchBox = document.getElementById("searchBox");
        searchBox.className = search_box
    }
    
function showResults(div_id){
    var divId = document.getElementById(div_id);
        divId.className = "search_box_result";
    }

function showTooltip(captionText, e){ 
    var tooltip = document.getElementById("tooltipForPageList");   
        tooltip.innerHTML = captionText;
        tooltip.style.left = e.clientX - 75 +'px';
        tooltip.style.top = e.clientY - 40+'px';
        tooltip.style.visibility = "Visible";

    }

function hideTooltip(){
    var tooltip = document.getElementById("tooltipForPageList");
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


function showAdminSearchBox(divId, searchBoxType){
    
	//Search Box Element Variables.
    var customerSearchBox = document.getElementById('searchBoxCustomer');
    var customerSearchBoxAdvance = document.getElementById('searchBoxCustomerAdvance');
    var distributorSearchBox = document.getElementById('searchBoxDistributor');
    var learnerSearchBox = document.getElementById('searchBoxLearner');
    
    //Customer Search Box
    if(searchBoxType == 'searchBoxCustomer'){
    	
    	//Hide other Admin Search Boxes
        distributorSearchBox.className = "visible";
        learnerSearchBox.className = "visible";
        customerSearchBoxAdvance.className = "visible";
        
    	//Make Customer Search Box Visible
    	customerSearchBox.className = "search_box40";
        $('#searchBoxCustomer input[type="text"]:first').focus();
        
    //Distributor Search Box
    }else if(searchBoxType == 'searchBoxDistributor'){
    	  
    	//Hide other Admin Search Boxes
        customerSearchBox.className = "visible";
        learnerSearchBox.className = "visible";
        customerSearchBoxAdvance.className = "visible";
        
    	//Make Distributor Search Box Visible
    	distributorSearchBox.className = "search_box40";
        $('#searchBoxDistributor input[type="text"]:first').focus();
        
    //Learner Search Box
    }else if(searchBoxType == 'searchBoxLearner'){
    	
    	//Hide other Admin Search Boxes
        customerSearchBox.className = "visible";
        distributorSearchBox.className = "visible";
        customerSearchBoxAdvance.className = "visible";
        
    	//Make Learner Search Box Visible
    	learnerSearchBox.className = "search_box40";
        $('#searchBoxLearner input[type="text"]:first').focus();
        
    } else if(searchBoxType == 'searchBoxCustomerAdvance'){
    	
    	//Hide other Admin Search Boxes
    	 distributorSearchBox.className = "visible";
         learnerSearchBox.className = "visible";
         customerSearchBox.className = "visible";
         
    	//Make Learner Search Box Visible
         customerSearchBoxAdvance.className = "search_box40";
        $('#searchBoxCustomerAdvance input[type="text"]:first').focus();
        
    }
}
    
    


function toggleSearchBoxDistributor(divId){
    var searchBox = document.getElementById('searchBoxDistributor');
        if(searchBox.className == "visible"){
            searchBox.className = "search_box40";
            $('#searchBoxDistributor input[type="text"]:first').focus();
        }else{
            searchBox.className = "visible";
        }   
    }

function toggleSearchBoxCustomer(divId){
    var searchBox = document.getElementById('searchBoxCustomer');
        if(searchBox.className == "visible"){
            searchBox.className = "search_box40";
            $('#searchBoxCustomer input[type="text"]:first').focus();
        }else{
            searchBox.className = "visible";
        }
    }

function toggleSearchBoxCustomerAdvance(divId){
    var searchBox = document.getElementById('searchBoxCustomerAdvance');
        if(searchBox.className == "visible"){
            searchBox.className = "search_box40";
            $('#searchBoxCustomerAdvance input[type="text"]:first').focus();
        }else{
            searchBox.className = "visible";
        }
    }

function toggleSearchBoxLearner(divId){
    var searchBox = document.getElementById('searchBoxLearner');
        if(searchBox.className == "visible"){
            searchBox.className = "search_box40";
            $('#searchBoxLearner input[type="text"]:first').focus();
        }else{
            searchBox.className = "visible";
        }
    }

function showResult(){
    var resultBox_1 = document.getElementById('searchResult_1');
    var resultBox_2 = document.getElementById('searchResult_2');
    var searchBox = document.getElementById('searchBox');
    var sideBar = document.getElementById('sideBar');
    var resultDescription = document.getElementById('resultDescription');
        searchBox.className = "visible";
        resultBox_1.className = "visible";
        resultBox_2.className = "";
        resultDescription.innerHTML= "Showing 1 - 10 of 50";

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
            var numberOfRows = rows.length;
            var thisRowTitle = my.parentNode.title;
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
    var numberOfRows = rows.length;
    var thisRowTitle = my.parentNode.title;
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