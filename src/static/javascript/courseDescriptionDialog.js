	    
    function openCourseDescriptionOverlay(courseId, enrollmentId, courseGroupId){
		
		// Open Overlay Container
		loadOverlay();
		
		// Build Course Description Overlay URL
		courseDescriptionOverlay = "lrn_courseOverlay.do";
		
		// Build Course Description Overlay URL Parameters
		var courseDescriptionOverlayUrl = courseDescriptionOverlay + "?courseId="+courseId+"&learnerEnrollmentId="+enrollmentId+"&courseGroupId="+courseGroupId;
		
		// Get JSONObject for Course Detail Overlay
        jQuery.getJSON(courseDescriptionOverlayUrl, function(courseDescriptionJSON) {            
            if(courseDescriptionJSON.courseGUID != undefined){	
				configureCourseDescriptionOverlay(courseDescriptionJSON);
			}else{
				configureGenericOverlay(getErrorOverlayObject("There was an error getting the course description."));
			}
        });
    }
    
    
	
	function loadOverlay(){

		// Reset Overlay Content Areas
		$('#overlay').find('#overlay-details').html('');
		$('#overlay').find('#overlay-header').html('');
		
		// Loading Overlay Object
		var overlayObject = getLoadingOverlayObject();
		
		// Configure Overlay
		configureGenericOverlay(overlayObject);
		
		// Dislplay Overlay
		positionOverlay(250);
		
		// Add Overlay 'Close' Click Event
		$('#overlay-close').click(function(){
			closeOverlay();
		});
	}
	
	// Hide Overlay
	function closeOverlay(){
		$('#overlay').css('display', 'none');	
	}
	
	function subscriptionloadOverlay(descriptionName){
// Add Overlay 'Close' Click Event
		
		// Reset Overlay Content Areas
		$('#overlay').find('#overlay-details').html('');
		$('#overlay').find('#overlay-header').html('');
		
		// Loading Overlay Object
		//var overlayObject = getLoadingOverlayObject();
		
		// Configure Overlay
		//configureGenericOverlay(overlayObject);
		//configureGenericOverlay(overlayObject);
		
		configureGenericSubscriptionOverlay(descriptionName);
		
		// Dislplay Overlay
		positionOverlay(80);
		
		
	}
	
	// Configure Course Description Overlay with JSONObject
	function configureCourseDescriptionOverlay(overlayJSON){
		
		// Determine Overlay Object
		var overlayObject = getCourseDescriptionOverlayObject(overlayJSON);
		
		// Determine Overlay Object Values
		var overlayHeaderMarkup = overlayObject.headerMarkup;
		var overlayDetailMarkup = overlayObject.detailMarkup;
		var overlayHeight = overlayObject.detailHeight;
		
		// Add Overlay Header Markup
		$('#overlay').find('#overlay-header').html(overlayHeaderMarkup);
		
		// Add Overlay Detail Makup
		$('#overlay').find('#overlay-details').append(overlayDetailMarkup);
		
		// Style added detail rows.
		$('#overlay-details').css('border', '1px solid #A8CAFE');
		
		// Set Overlay Position and Dimensions.
		positionOverlay(overlayHeight);
		
		// Stripe the rows of the overlay detail.
		$('#overlay-details').find('tr:odd').attr('class', 'overlay-row');
		$('#overlay-details').find('tr:even').attr('class', 'overlay-colored-row');
	}
	
		// Configure Generic Overlay with overlayObject
	function configureGenericOverlay(overlayObject){
		
		// Overlay Object Values
		var overlayHeaderMarkup = overlayObject.headerMarkup;
		var overlayDetailMarkup = overlayObject.detailMarkup;
		var overlayHeight = overlayObject.detailHeight;
		
		// Add Overlay Header Markup
		$('#overlay').find('#overlay-header').html(overlayHeaderMarkup);
		
		// Add Overlay Detail Makup
		$('#overlay').find('#overlay-details').append(overlayDetailMarkup);
		
		// Style added detail rows.
		$('#overlay-details').css('border', '1px solid #A8CAFE');
		
		// Set Overlay Position and Dimensions.
		positionOverlay(overlayHeight);
	}
	
	
    
    // Build Overlay Detail Row Object
	// TODO: This should be in getCourseDescriptionOverlayObject?
	function buildOverlayDetailRowObject(id, label, text){
		var rowObject = {
			rowId: id,
			rowLabel: label,
			rowText: text
		}
		return rowObject;
	}
	
	
		// OVERLAY CONFIGURE: START -----------------------------------

	// Configure Generic Overlay with overlayObject
	function configureGenericOverlay(overlayObject){
		
		// Overlay Object Values
		var overlayHeaderMarkup = overlayObject.headerMarkup;
		var overlayDetailMarkup = overlayObject.detailMarkup;
		var overlayHeight = overlayObject.detailHeight;
		
		// Add Overlay Header Markup
		$('#overlay').find('#overlay-header').html(overlayHeaderMarkup);
		
		// Add Overlay Detail Makup
		$('#overlay').find('#overlay-details').append(overlayDetailMarkup);
		
		// Style added detail rows.
		$('#overlay-details').css('border', '1px solid #A8CAFE');
		
		// Set Overlay Position and Dimensions.
		positionOverlay(overlayHeight);
	}
	
		// Loading Overlay Object
	function getLoadingOverlayObject(){
		var overlayObject = {
			detailMarkup : "",
			headerMarkup: "Loading",
			detailHeight: 100
		}
		return overlayObject;
	}

	
		// Error Overlay Object
	function getErrorOverlayObject(errorText){
		var errorDetailMarkup = "";
		if(errorText!= ""){
			errorDetailMarkup = errorText
		}
		var overlayObject = {
			detailMarkup : errorText,
			headerMarkup: "Error",
			detailHeight: 100
		}
		return overlayObject;
	}
	
	function configureGenericSubscriptionOverlay(descriptionName){
		
		// Overlay Object Values
		var overlayHeaderMarkup = 'Subscription Description';
		var overlayDetailMarkup = '500px';
		var overlayHeight = '100px';
		
		$('#overlay-close').click(function(){
			closeOverlay();
		});
		
		
		var overlayDetailMarkup = "<table width='100%' cellpadding='0' cellspacing='0'><tr><td class='overlay-details-label'><div id='1'>Subscription Description</div></td><td class='overlay-details-text'>"+ descriptionName +"<div></td></tr></table>";
		
		// Add Overlay Header Markup
		$('#overlay').find('#overlay-header').html(overlayHeaderMarkup);
		
		// Add Overlay Detail Makup
		$('#overlay').find('#overlay-details').html(overlayDetailMarkup);
		
		// Style added detail rows.
		$('#overlay-details').css('border', '1px solid #A8CAFE');
		
		// Set Overlay Position and Dimensions.
		positionOverlay(overlayHeight);
	}
    
    function getCourseDescriptionOverlayObject(courseDescriptionJSON){
		// Set Overlay Title
		var overlayTitle = courseDescriptionJSON.courseTitle;		
		
		// Branded Course Description Labels
		
		// Course Description Values
		var courseId = courseDescriptionJSON.courseId;
		var courseTitleText = courseDescriptionJSON.courseTitle;
		var coursePathText = courseDescriptionJSON.coursePath;
		//var courseTypeText = courseDescriptionJSON.courseType;
		var courseCreditHoursText = courseDescriptionJSON.courseCreditHours;				
		var courseDescriptionText = courseDescriptionJSON.courseDescription;
		var courseOverviewText = courseDescriptionJSON.courseOverview;
		var courseGuideText = courseDescriptionJSON.courseGuide;
		var coursePrerequisitesText = courseDescriptionJSON.coursePrerequisites;
		var courseLearningObjectivesText = courseDescriptionJSON.courseLearningObjectives;
		var courseQuizInfoText = courseDescriptionJSON.courseQuizInfo;
		var courseFinalExamInfoText = courseDescriptionJSON.courseFinalExamInfo;
		var courseEndOfCourseInstructionsText =	courseDescriptionJSON.courseEndOfCourseInstructions;
		var courseAdditionalDetailsText = courseDescriptionJSON.courseAdditionalDetails;		
		var courseDeliveryMethodText = courseDescriptionJSON.courseDeliveryMethod;
		var courseApprovedHoursText = courseDescriptionJSON.courseApprovedCourseHours;
		var courseDurationText = courseDescriptionJSON.courseDuration;
		var courseApprovalNumberText = courseDescriptionJSON.courseApprovalNumber;
		
		// Items defined in the Course Description JSONObject will be added to this Array.
		var overlayDetailRows = new Array();
		 
		if(courseId!=undefined){
			var courseIdRowObject = buildOverlayDetailRowObject("courseId", courseIdLabel, courseId);
			overlayDetailRows.push(courseIdRowObject);
		}
		//if(courseTypeText!=undefined){
		//	var courseTypeRowObject = buildOverlayDetailRowObject("courseType", courseTypeLabel, courseTypeText);
		//	overlayDetailRows.push(courseTypeRowObject);
		//}
		if(courseCreditHoursText!=undefined){
			var courseCreditHoursRowObject = buildOverlayDetailRowObject("courseCreditHours", courseCreditHoursLabel, courseCreditHoursText);
			overlayDetailRows.push(courseCreditHoursRowObject);
		}
		if(coursePathText!=undefined){
			var coursePathRowObject = buildOverlayDetailRowObject("coursePath", coursePathLabel, coursePathText);
			overlayDetailRows.push(coursePathRowObject);
		}
		if(courseDescriptionText!=undefined){
			var courseDescriptionRowObject = buildOverlayDetailRowObject("courseDescription", courseDescriptionLabel, courseDescriptionText);
			overlayDetailRows.push(courseDescriptionRowObject);
		}
		if(courseOverviewText!=undefined){
			var courseOverviewRowObject = buildOverlayDetailRowObject("courseOverview", courseOverviewLabel, courseOverviewText);
			overlayDetailRows.push(courseOverviewRowObject);
		}
		if(courseGuideText!=undefined){
			var courseGuideRowObject = buildOverlayDetailRowObject("courseGuide", courseGuideLabel, courseGuideText);
			overlayDetailRows.push(courseGuideRowObject);
		}
		if(coursePrerequisitesText!=undefined){
			var coursePrerequisitesRowObject = buildOverlayDetailRowObject("coursePrerequisites", coursePrerequisitesLabel, coursePrerequisitesText);
			overlayDetailRows.push(coursePrerequisitesRowObject);
		}
		if(courseLearningObjectivesText!=undefined){
			var courseLearningObjectivesRowObject = buildOverlayDetailRowObject("courseLearningObjectives", courseLearningObjectivesLabel, courseLearningObjectivesText);
			overlayDetailRows.push(courseLearningObjectivesRowObject);
		}
		if(courseQuizInfoText!=undefined){
			var courseQuizeInfoRowObject = buildOverlayDetailRowObject("courseQuizInfo", courseQuizInfoLabel, courseQuizInfoText);
			overlayDetailRows.push(courseQuizeInfoRowObject);
		}
		if(courseFinalExamInfoText!=undefined){
			var courseFinalExamInfoRowObject = buildOverlayDetailRowObject("courseFinalExamInfo", courseFinalExamInfoLabel, courseFinalExamInfoText);
			overlayDetailRows.push(courseFinalExamInfoRowObject);
		}
		if(courseEndOfCourseInstructionsText!=undefined){
			var courseEndOfCourseInstructionsRowObject = buildOverlayDetailRowObject("courseEndOfCourseInstructions", courseEndOfCourseInstructionsLabel, courseEndOfCourseInstructionsText);
			overlayDetailRows.push(courseEndOfCourseInstructionsRowObject);
		}
		if(courseAdditionalDetailsText!=undefined){
			var courseAdditionalDetailsRowObject = buildOverlayDetailRowObject("courseAdditionalDetails", courseAdditionalDetailsLabel, courseAdditionalDetailsText);
			overlayDetailRows.push(courseAdditionalDetailsRowObject);
		}
		if(courseDeliveryMethodText!=undefined){
			var courseDeliveryMethodRowObject = buildOverlayDetailRowObject("courseDeliveryMethod", courseDeliveryMethodLabel, courseDeliveryMethodText);
			overlayDetailRows.push(courseDeliveryMethodRowObject);
		}
		if(courseApprovedHoursText!=undefined){
			var courseApprovedHoursRowObject = buildOverlayDetailRowObject("courseApprovedHours", courseApprovedHoursLabel, courseApprovedHoursText);
			overlayDetailRows.push(courseApprovedHoursRowObject);
		}
		if(courseDurationText!=undefined){
			var courseDurationRowObject = buildOverlayDetailRowObject("courseDuration", courseDurationLabel, courseDurationText);
			overlayDetailRows.push(courseDurationRowObject);
		}
		if(courseApprovalNumberText!=undefined){
			var courseApprovalNumberRowObject = buildOverlayDetailRowObject("courseApprovalNumber", courseApprovalNumberLabel, courseApprovalNumberText);
			overlayDetailRows.push(courseApprovalNumberRowObject);
		}
				
		// Course Description Overlay Detail Markup
		var overlayDetailMarkup = "<table width='100%' cellpadding='0' cellspacing='0'>";		
		// Build Overlay Detail Rows
		jQuery.each(overlayDetailRows, function(index, rowObject){
			overlayDetailMarkup = overlayDetailMarkup + 
    			"<tr><td class='overlay-details-label'><div id='" + rowObject.rowId + "'>" + rowObject.rowLabel + 
    			"</div></td><td class='overlay-details-text'><div>" + rowObject.rowText + "</div></td></tr>"
		});
		overlayDetailMarkup = overlayDetailMarkup + "</table>";
	
		// Get Course Type Icon
		var overlayHeaderIcon = getCourseIconByType(courseDescriptionJSON.courseType);
		
		// Schedule 'Detail Header' Markup
		var overlayHeaderMarkup = "<table class='course-detail-header' width='100%' border='0' cellpadding='0' cellspacing='0'>" + 
    		"<tr><td valign='middle' style='width:40px'>" + 
    		"<img src='" + overlayHeaderIcon + "'/></td><td valign='middle' nowrap>" + overlayTitle + "</td></tr></table>";
		
		// Overlay Detail Height
		var overlayDetailHeight = overlayDetailRows.length*35;
		
		// Assign Values to return object.
		var overlayDescriptionObject = {
			headerMarkup : overlayHeaderMarkup,
			detailMarkup : overlayDetailMarkup,
			detailHeight : overlayDetailHeight
		}
		return overlayDescriptionObject;
	}
    
    function subscriptiondata(subscriptiondesc)
    {
    	var Id = 1;
    	var Label = "Description";
    	var Text = "Text";
    	
    	subscriptionloadOverlay();
		//loadOverlay()
    	
    	var overlayDetailMarkup = "<table width='100%' cellpadding='0' cellspacing='0'>";		
		// Build Overlay Detail Rows
		jQuery.each(overlayDetailRows, function(index, rowObject){
			overlayDetailMarkup = overlayDetailMarkup + 
    			"<tr><td class='overlay-details-label'><div id='" + Id + "'>" + Label + 
    			"</div></td><td class='overlay-details-text'><div>" + Text + "</div></td></tr>"
		});
		overlayDetailMarkup = overlayDetailMarkup + "</table>";
    }
	
	// Position and Resize Overlay
	function positionOverlay(overlayHeight){
		
		// Define values for calculating Overlay Dimensions, and Position.
		var containerWidth = $(window).width();
		var totalScreenHeight = $('#scrollable').height();
		var containerLeft = $('#browse-columns-container').offset().left;      
		var calculatedWidth = (containerWidth/2);
		var calculatedLeft = containerLeft+(calculatedWidth/2);
	
		// Determine Available Height
		
		var availableOverlayHeight = totalScreenHeight - 15;

		//console.log("height " + $(window).height());
		//console.log(availableOverlayHeight);
		//console.log(overlayHeight);
		
		// Determine Overlay Height
		if(overlayHeight > availableOverlayHeight){
			overlayHeight = availableOverlayHeight;
		}

		// Define Overlay Detail Height
		
		var overlayDetailHeight = overlayHeight - 40;
		overlayDetailHeight = overlayDetailHeight + "px";

		// Define Overlay Style 
        var courseOverlayStyle = "top:70px; left:" + calculatedLeft + "px; width:"+calculatedWidth+"px;";

		// Style Overlay
		$('#overlay').attr('style', courseOverlayStyle);
		$('#overlay').css('display', 'block');
		$('#overlay').css('height', overlayHeight);
		$('#overlay-details').css('overflow', 'auto');
		$('#overlay-details').css('height', overlayDetailHeight);
	}
	
	// OVERLAY GENERAL: END -------------------------------------
	
	// Get Course Icon By CourseType.
	function getCourseIconByType(courseType){
		var courseTypeIconPath = "";
		if((courseType == "Self Paced Course")||(courseType == "Scorm Course")){
			// Icon Self Paced Course Icon
			courseTypeIconPath = iconSelfpaced;
		}
		if(courseType == "Classroom Course" || courseType == "Webinar Course"){		
			// Inactive Synchronous Course Icon
			courseTypeIconPath = iconInactiveVirtualClassroom;			
		}
		if(courseType == ("Active Classroom Course") || courseType == "Active Webinar Course"){		
			// Active Synchronous Course Icon
			courseTypeIconPath = iconActiveVirtualClassroom;
		}
		if(courseType == "DFC"){
			// Forum Course
			courseTypeIconPath = iconDiscussionForum;			
		}
		if(courseType == "Weblink"){		
			// Weblink Course
			courseTypeIconPath = iconWeblinkCourse;			
		}
		if(courseType == "Legacy Course"){		
			// Weblink Course
			courseTypeIconPath = iconSelfpaced;			
		}
		return courseTypeIconPath;
	}
	
	