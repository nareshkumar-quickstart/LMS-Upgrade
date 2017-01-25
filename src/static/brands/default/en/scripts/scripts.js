// Broswer Detection ---------------------------------------------
    var browser = navigator.appName;

// LMS Mode ------------------------------------------------------
    function changeLMSMode(param){
    if(param == 0){window.location.href = "../learner/courses.jsp";}
    if(param == 1){window.location.href = "../manager/users.jsp";}
    if(param == 2){window.location.href = "../instructor/dashboard.jsp";}
    if(param == 3){window.location.href = "../expert/view-profile.jsp";}
    if(param == 4){window.location.href = "../accreditation/search.jsp";}
    if(param == 5){window.location.href = "../administrator/search.jsp";}
}

// Select All Checkboxes -----------------------------------------
function selectAllCheckboxes(field, formObject){
    if(formObject.checked==true){
        for (i = 0; i < field.length; i++)
            field[i].checked = true;
    }else{
        for (i = 0; i < field.length; i++)
            field[i].checked = false;
    }
}


$(function(){
// Broswer Detection ---------------------------------------------

    

//OS Detection ---------------------------------------------------


// Layout --------------------------------------------------------

    
    
	var layoutType = "";
	
	// Determine Layout Type
	// TODO: Handling both named sidebars.  These should be consolidated into a single id.
    if(($("#table-layout #left-navigation").length > 0) || ($("#table-layout #left-sidebar").length > 0)){
    	layoutType = "doubleColumn";
    }else{
    	layoutType = "singleColumn";
    }
    	
    
    
    function getPageHeaderLayoutObject(){
    	
    	// Declare variables for Header Layout Object.
    	var pageHeaderHeight = $('#header').height();
    	var pageSubHeaderHeight = 0;
    	var headerHeight = 0;
    	
    	// If subheader exists. add it to the overallHeight.
    	// Page Subheaders are header areas that are specific to a given page.
    	if($('#page-searchbar').length>0){
    		
    		
    	}
    	if($('#row-tabs').length>0){
    		pageSubHeaderHeight = pageSubHeaderHeight +  $('#row-tabs').height();
    	
    	}
    	
    	headerHeight = pageHeaderHeight + pageSubHeaderHeight;
    	
     	
    	// Define Header Layout Object.
    	var pageHeaderLayoutObject = {
			headerHeight : pageHeaderHeight,
			subHeaderHeight : pageSubHeaderHeight,
			combinedHeaderHeight : headerHeight
    	};

    	return pageHeaderLayoutObject;
    }
    
    
    
    
    // Position Page Footer Height and Top Position
    function positionPageFooter(pageFooterHeight, pageFooterPosition){

    	$('#universal-footer').css('top', (pageFooterPosition+"px"));
		$('#universal-footer').height(pageFooterHeight);
    }
    
    // Position Page Sidebar Height
    function positionPageSidebar(pageSidebarHeight){
    	$('#sidebar-scroller').height(pageSidebarHeight);
    }
    
    // Position Page Scrollable Content Height.
    function positionPageScrollable(pageScrollableHeight){
    	// Style Scrollable Height.
		$('#scrollable').height(pageScrollableHeight);
    }
    
    function positionPageHeaderSearch(){
    	
    	
    	var searchFieldWidth = 275;
    	
    	var searchLeftSpaceWidth = $(window).width() - searchFieldWidth;
    	$('#searchbar-left').width(searchLeftSpaceWidth);
    	
    	//$('#page-searchbar').height(); 
    }
    
    // Position Page Elements
    function positionPageElements(){

    	// Calculate Positioning Values
    	var overallWindowHeight = $(window).height();
    	var pageHeaderLayoutObject = getPageHeaderLayoutObject();
    	var pageFooterHeight = 24;
    	var combinedHeaderHeight = pageHeaderLayoutObject.combinedHeaderHeight;
		var overallContentHeight = ((overallWindowHeight - combinedHeaderHeight) - pageFooterHeight);
		var pageFooterPosition = overallWindowHeight - pageFooterHeight;
	
		// Position Elements
		positionPageFooter(pageFooterHeight, pageFooterPosition);
		positionPageSidebar(overallContentHeight);
		positionPageScrollable(overallContentHeight);
		positionPageHeaderSearch();
    }
    
    
    function getCurrentAvailableScrollableWidth(layoutType){
    	// Current Window Width
    	var windowWidth = $(window).width();
    	// Max Side-bar Width
    	var pageSidebarWidth = 0;
    	
    	if(layoutType == "doubleColumn"){
    		pageSidebarWidth = 195;
    	}else if(layoutType == "singleColumn"){
    		pageSidebarWidth = 0;
    	}

    	
    	// Max supported width
    	var maxWindowWidth = 1024;
    	// Scroll-bar
    	// TODO: Expand to account for browser variation.
    	var scrollbarWidth = 30;
    	
    	var currentAvailableScrollableWidth = (windowWidth - pageSidebarWidth)- scrollbarWidth;
    	var maxScrollableWidth = (maxWindowWidth - pageSidebarWidth) - scrollbarWidth;
    	// Determine Scrollable Width
    	if(currentAvailableScrollableWidth < maxScrollableWidth){
    		currentAvailableScrollableWidth = maxScrollableWidth;		
    	}    	
    	return currentAvailableScrollableWidth;
    }
    
    
    function configurePageLayout(layoutType){
    	
    	// Double Column Page Layout
    	if(layoutType == "doubleColumn"){        	
        	var currentAvailableScrollableWidth = getCurrentAvailableScrollableWidth(layoutType);
        	
        	// Style Scrollable Content Area
        	$('#scrollable').width(currentAvailableScrollableWidth);
        	
        	// Style Side-bar
        	
        	$("#table-layout #left-navigation").width(195);
        	
        	$("#sidebar-scroller").height($(window).height());
        	
        	$("#table-layout #left-sidebar").width(195);
        	
        	// Add Navigation Row Heading for Double Column Page Layout.
        	if($('tr#navigation-row').length > 0){
        		if($('td#blank-tab').length < 1){
        			$('tr#navigation-row').prepend("<td id='blank-tab'></td>");	
        		}
			}
        	
    	// Single Column Page Layout
    	}else if(layoutType == "singleColumn"){
    		
    		
    		var currentAvailableScrollableWidth = getCurrentAvailableScrollableWidth(layoutType);
        	
        	// Style Scrollable Content Area
        	$('#scrollable').width(currentAvailableScrollableWidth);
        	
    	}else{
    		
    		//TODO Log Error.
    		// Enable error to determine other types of layout that should be handled.
    	
    	}
    
    	// Position Page Elements
    	positionPageElements();
    	
    }
  

    function resizeLayout(){
        

    	
        // Configure Page Layout
        configurePageLayout(layoutType);
        
        // Resizes Search Area.
        if($("#search-blank").length>0){
        	$("#search-blank").width($("#table-search").width() - $("#search-curve").width() - $("#search-start").width() - $("#search-stem").width() - $("#search-end").width());
        }
        // Resizes Login to be centered.
        if($("#table-login").length>0){
	        $("#table-login").css("margin-left", $(window).width()/2 - $("#table-login").width()/2);
	        $("#table-login").css("margin-top", $(window).height()/2 - $("#table-login").height()/2 - $("#table-layout #header").height());
        }
        
        // Resizes My Courses, Course Group Browser.
        if($("#browse-columns-container").length>0){
        	//var exposedCount = $(".browse-column").length;
            //var browseColumnWidth = ($(".browse-column").width()+5) * exposedCount;
            //$("#browse-columns-scroller").width(browseColumnWidth);  
            $('#browse-columns-container').width($('#scrollable').width());
        }
        
        // Repositions Large Button Container
        //$(".large-button-container").css("padding-left", $(".large-button-container").parent().parent().width() / 2 - $(".large-button-container").width() /2 + "px");
        
        // Repositions Search Box
        $("#search-box").css("margin-left",$(".content-table").width()/2 - $("#search-box").width()/2 + 10);
        $("#search-box").css("height", "1px");
        
        // If needed. Handle Process Overlay
        //if($("#process").length>0){
        	//if($("#process").css('display', 'block')){
        		//TODO: POSITION PROCESS MESSAGE
        		//$("#process").css('position', 'fixed').css('left', '100px').css('top', '100px');
        	//}
        //}

    }

    $(document).ready(function(){
    	resizeLayout();
    });

    
    window.onresize = function(){
    	resizeLayout();	
    }


    $("#column-controls").width($("#table-controls").width()/2 - $("#column-search").width()/2 - 10)


// LMS Login ------------------------------------------------------
    $("#button-login").click(function(){
        if($("#field-username").val() == "" || $("#field-password").val() == ""){
            if(browser == "Microsoft Internet Explorer"){
                $("#login-error-row").css("display", "block");
            }else{
                $("#login-error-row").css("display", "table-row");
            }
        }else{
            switch($("#field-username").val()){
            case "learner":
                window.location = "learner/courses.jsp";
            break;
            case "manager":
                window.location = "manager/users.jsp";
            break;
            case "instructor":
                window.location = "instructor/dashboard.jsp";
            break;
            case "expert":
                window.location = "expert/view-profile.jsp";
            break;
            case "accreditation":
                window.location = "accreditation/search.jsp";
            break;
            case "administrator":
                window.location = "administrator/search.jsp";
            break;
            default:
                window.location = "learner/courses.jsp";
            }
            
        }

    });

// Search Field ------------------------------------------------------
    $("#search-field").focus(function(){
        $("#search-field").val("")
    });

    $("#search-field").blur(function(){
        if($("#search-field").val() == ""){
            $("#search-field").val("Search")
        }
    });

// Search Box -----------------------------------------------

    $("#button-search").click(function(){

        $("#search-box").css("display", "block");
        $("#search-box").animate({height:(($("#search-box").find("tr").length)*25)+'px'}, 500);
    });

    $(".search-box-controls").click(function(){
        $("#search-box").animate({height:'1px'}, 500, function(){
            $("#search-box").css("display", "none");
        });
        
    })

// Check Password Strength - Learner Profile --------------------------

    $("#field-password").keyup(function(){
        password = $("#field-password").val();
        passwordStrength(password)

        if($("#field-password").val() == $("#field-confirm-password").val()){
            $("#password-status").attr("class", "icon-accept");
            $("#password-match").html("");
        }else{
            $("#password-status").attr("class", "icon-reject");
            $("#password-match").html("Passwords do no match");
        }
    });

    $("#field-confirm-password").keyup(function(){
        if($("#field-password").val() == $("#field-confirm-password").val()){
            $("#password-status").attr("class", "icon-accept");
            $("#password-match").html("");
        }else{
            $("#password-status").attr("class", "icon-reject");
            $("#password-match").html("Passwords do no match");
        }
    });




// Education - Add Institution ----------------------

    $("#button-add-institution").click(function(){
        $(this).parent().parent().parent().parent().parent().parent().append($(".hidden-content").html());
        $("#scrollable").animate({"scrollTop":$("#scrollable").height()});
    })

// Work Experience - Add Employer ----------------------

    $("#button-add-employer").click(function(){
        $(this).parent().parent().parent().parent().parent().parent().append($(".hidden-content").html());
        $("#scrollable").animate({"scrollTop":$("#scrollable").height()});
    })

// Required Fields validation (SAVE) -------------------
    $("#button-save").click(function(){
        var values = new Array;
        var errors = false;

        $(".required").each(function(){
            
            values.push($(this).val());

            if($(this).val() == ""){
                errors = true
                $(this).addClass("empty-required-field");
                $(this).parent().parent().find(".required-field").html("<img alt='!' src='../images/!mark.gif'/>");
            }else{
                $(this).removeClass("empty-required-field");
                $(this).parent().parent().find(".required-field").html("*");
            }
        });

        if(errors){
            if(browser == "Microsoft Internet Explorer"){
                $("#error-profile").css("display","block");
            }else{
                $("#error-profile").css("display","table");
            }
        }else{
            $("#error-profile").css("display","none");
                if(browser == "Microsoft Internet Explorer"){
                    $("#table-processing").css("display", "block")
                    $("#table-processing-shadow").css("display", "block")
                }else{
                    $("#table-processing").css("display", "table")
                    $("#table-processing-shadow").css("display", "table")
                }
        }
    });

// Required Fields validation (NEXT)-----------------------------------
    $("#button-next").click(function(){
        var values = new Array;
        var errors = false;
        var querystring = "?";

        $(".required").each(function(){

            values.push($(this).val());

            if($(this).val() == ""){
                errors = true
                $(this).addClass("empty-required-field");
                $(this).parent().parent().find(".required-field").html("<img alt='!' src='../images/!mark.gif'/>");
            }else{
                $(this).removeClass("empty-required-field");
                $(this).parent().parent().find(".required-field").html("*");
            }
        });

        $("input").each(function(){
            querystring += "&"+($(this).attr("name")+"="+$(this).val());
        });

        $("select").each(function(){
            querystring += "&"+($(this).attr("name")+"="+$(this).val());
        });

        $("textarea").each(function(){
            querystring += "&"+($(this).attr("name")+"="+$(this).val());
        });

        if(errors){
            if(browser == "Microsoft Internet Explorer"){
                $("#error-profile").css("display","block");
            }else{
                $("#error-profile").css("display","table");
            }
        }else{
            $("#error-profile").css("display","none");
            window.location.href = ($(this).find("div").attr("href")+querystring);
            
        }
    });


});



// Education/Experience - Remove Table ----------------------
    function removeTable(arg){
        $(arg).parent().parent().parent().parent().remove();
    }

// Experience Time Period -----------------------------------
    function experienceTimePeriod(arg){
        if($(arg).attr("checked") == true){
            $(arg).parent().parent().parent().parent().find("#period-to").html("to present");
            $(arg).parent().parent().parent().parent().find(".period-to-previous").css("display", "none");
        }else{
            $(arg).parent().parent().parent().parent().find("#period-to").html("to");
            $(arg).parent().parent().parent().parent().find(".period-to-previous").css("display", "inline");
        }
    }

// Load Browse Column -----------------------------------------
    function loadBrowseColumn(arg){
        var currentcontainer = $(arg).parent().parent().parent().parent().attr("id");
        var nextcontainer = parseInt(currentcontainer.substr(7)) + 1;
            nextcontainer = "column-" + nextcontainer;

        var browsetype = $("#browse-type").attr("class");

        var scrollamount = $(".browse-column").width() + $(".browse-column").length;

            $(arg).parent().parent().find(".browse-name-selected").removeClass("browse-name-selected");
            $(arg).find(".browse-name").addClass("browse-name-selected");

            $(arg).parent().parent().find(".icon-caret-right").attr("class", "icon-caret-disabled-right");
            $(arg).find(".icon-caret-disabled-right").attr("class", "icon-caret-right");

            if(currentcontainer == "column-1"){
                $("#column-3").remove()
                $("#column-4").remove()
                $("#browse-columns-container").css("overflow-x", "hidden");
            }

            if(currentcontainer == "column-2"){
                $("#column-4").remove()
                $(".browse-column").width($("#browse-columns-container").width()/3 - 2);
                $("#browse-columns-container").animate({scrollLeft:"-="+scrollamount});
                $("#browse-columns-container").css("overflow-x", "hidden");
            }

            if($("#"+nextcontainer).length > 0){

                if($(arg).attr("id") == "empty"){
                    $("#"+nextcontainer).html("<div class='selection-message'>No Sub-instustry</div>");
                }else{
                    $("#"+nextcontainer).load("../includes/table-browse-column.jsp?xmlfile=column-"+$(arg).attr("id")+".xml");
                }

            }else{

                if($(arg).attr("id") == "empty"){
                    $("#browse-columns-scroller").append("<div id='"+nextcontainer+"' class='browse-column'><div class='selection-message'>No Sub-instustry</div></div>");
                }else{
                    $("#browse-columns-scroller").append("<div id='"+nextcontainer+"' class='browse-column'></div>");
                    $("#"+nextcontainer).load("../includes/table-browse-column.jsp?xmlfile=column-"+$(arg).attr("id")+".xml");
                }

               $(".browse-column").width($("#browse-columns-container").width()/3 - 2);
               $("#browse-columns-scroller").width($(".browse-column").width() * ($(".browse-column").length) + 10 + $(".browse-column").length )

               if($(".browse-column").length < 4 ){
                   $("#browse-columns-container").css("overflow-x", "hidden")
               }else{
                   $("#browse-columns-container").animate({scrollLeft:"+="+scrollamount});
                   $("#browse-columns-container").css("overflow-x", "auto")
               }
               
            }

            if(browsetype == "experts"){
                $("#list-expert").load("../includes/table-experts.jsp?xmlfile=experts-"+$(arg).attr("id")+".xml");    
            }else if(browsetype == "courses"){
                $("#list-course").load("../includes/table-courses.jsp?xmlfile=courses-"+$(arg).attr("id")+".xml");
            }

    }



function courseType(url){
    window.location.href = url+"-"+$('input[name=coursetype]:checked').val()+".jsp";
}

function masteryScore(arg){
    if(arg==0){
        $("#mastery-row").css("display", "none");
    }else{
        if(browser == "Microsoft Internet Explorer"){
            $("#mastery-row").css("display", "block");
        }else{
            $("#mastery-row").css("display", "table-row");
        }
    }
}

function recurrence(arg){
    $("#daily").css("display", "none")
    $("#weekly").css("display", "none")
    $("#monthly").css("display", "none")
    $("#yearly").css("display", "none")
    
    switch(arg){
        case "daily":
            if(browser == "Microsoft Internet Explorer"){
                $("#daily").css("display", "block");
             }else{
                $("#daily").css("display", "table-row");
            }
        break;
        case "weekly":
            if(browser == "Microsoft Internet Explorer"){
                $("#weekly").css("display", "block");
             }else{
                $("#weekly").css("display", "table-row");
            }
        break;
        case "monthly":
            if(browser == "Microsoft Internet Explorer"){
                $("#monthly").css("display", "block");
             }else{
                $("#monthly").css("display", "table-row");
            }
        break;
        case "yearly":
            if(browser == "Microsoft Internet Explorer"){
                $("#yearly").css("display", "block");
             }else{
                $("#yearly").css("display", "table-row");
            }
        break;
    }

}

function meetingtype(arg){
    if(arg == 1){
        if(browser == "Microsoft Internet Explorer"){
            $(".meeting-information").css("display", "block");
        }else{
            $(".meeting-information").css("display", "table-row");
        }
    }else{
        $(".meeting-information").css("display", "none")
    }

}

function goToLS360Dashboard(){
    document.location='lrn_ls360dashboard.do';
}