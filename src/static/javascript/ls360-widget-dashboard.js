/**
 * ......................................................................................... *
 * Widget Selection Panel : jQuery Plugin Author: Niles White 2013
 * ......................................................................................... */
!function(exports, $, window, undefined) {

    /** Widget Selection Panel Plugin */
    var WidgetDashboardLayout = function() {

        /** Plugin Variables */
        var widgetDashboardLayout = {}, // private API
            WidgetDashboardLayout = {}, // public API

            /** Plugin defaults */
                defaults = {
                default1 : ""
            };

        /** Private Options */
        widgetDashboardLayout.options = {};

        /** Dashboard Widget Type Default Values */
        widgetDashboardLayout.defaultWidgetTypeLayout = {
            draggableHeight : ""
        }


        /********************************************************************
         * Widget Array Definition and Retrieval Methods
         *
         * - availableWidgets
         * - availableWidgetZones
         * - selectedWidgetArray
         * - returnAvailableWidgets
         * - returnSelectedWidgets
         *------------------------------------------------------------------*/
        /** Widget Array Definition and Retrieval Methods : Define Available Widgets Array */
        widgetDashboardLayout.availableWidgets = new Array();
        /** Widget Array Definition and Retrieval Methods : Define Available Widget Zones Array */
        widgetDashboardLayout.availableWidgetZones = new Array();
        /** Widget Array Definition and Retrieval Methods : Define Selected Widget Array */
        widgetDashboardLayout.selectedWidgetArray = new Array();
        /** Widget Array Definition and Retrieval Methods : Return Available Widgets Array */
        WidgetDashboardLayout.returnAvailableWidgets = function(){
            return widgetDashboardLayout.availableWidgets;
        }
        /** Widget Array Definition and Retrieval Methods - Return Selected Widgets Array*/
        WidgetDashboardLayout.returnSelectedWidgets = function(){
            return widgetDashboardLayout.selectedWidgetArray;
        }


        /********************************************************************
         * Widget Dashboard Methods and Objects
         *
         * - DashboardWidget
         * - toTitleCase
         *------------------------------------------------------------------*/
        /** Widget Dashboard Objects : Define Dashboard Widget Object */
        var DashboardWidget = function(id, type, title, zoneId, filters){
            this.widgetId = id;
            this.widgetType = type;
            this.widgetTitle = title;
            this.widgetZoneId = zoneId;
            this.widgetFilters = filters;
        }
        /** Widget Dashboard Objects : Convert to Title Case **/
        widgetDashboardLayout.toTitleCase = function(str){
            return str.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();});
        }

        /********************************************************************
         * Create Widget Elements
         *
         * - createWidgetZonePanelElement
         * - createAvailableWidgetElement
         * - createWidgetZoneElement
         * - retrieveAvailableWidgets
         *------------------------------------------------------------------*/
        /** Create Widget Elements : Create Widget Zone Panel*/
        widgetDashboardLayout.createWidgetZonePanelElement = function(){
            // Create Draggable Zone Element
            var draggableZoneElement = $(document.createElement("div"));
            draggableZoneElement.attr('id', 'draggableZone');
            draggableZoneElement.attr('class', 'draggable-zone');
            // Create Droppable Zone Element
            var droppableZoneElement = $(document.createElement("div"));
            droppableZoneElement.attr('id', 'droppableZone');
            droppableZoneElement.attr('class', 'droppableOverview');
            // Create Left Droppable Zone Element
            var droppableZoneLeftElement = $(document.createElement("div"));
            droppableZoneLeftElement.attr('id', 'droppableZoneLeft');
            droppableZoneLeftElement.attr('style', 'float:left;');
            // Create Right Droppable Zone Element
            var droppableZoneRightElement = $(document.createElement("div"));
            droppableZoneRightElement.attr('id', 'droppableZoneRight');
            droppableZoneRightElement.attr('style', 'float:right; padding-left:10px; ');
            // Compose Widget Panel - Add Draggable Zone, Left Droppable Zone and Right Droppable Zone
            droppableZoneElement.append(droppableZoneLeftElement);
            droppableZoneElement.append(droppableZoneRightElement);

            $('#widgetPanel').append(draggableZoneElement);
            $('#widgetPanel').append(droppableZoneElement);
        }
        /** Create Widget Elements - Create Available Dashboard Widget Element  */
        widgetDashboardLayout.createAvailableWidgetElement = function(dashboardWidget){
            // Define Widget Element - Title Element
            var titleElement = $(document.createElement("div"));
            titleElement.text(dashboardWidget.widgetTitle)
            titleElement.attr('id', "title-"+ dashboardWidget.widgetId);
            titleElement.attr('style', "float:left;");
            // Define Widget Element - Widget Element
            var widgetElement = $(document.createElement("div"));
            widgetElement.attr('id', dashboardWidget.widgetId);
            widgetElement.addClass("draggable-widget");
            widgetElement.addClass("widget-icon");
            widgetElement.addClass("widget-icon-"+dashboardWidget.widgetType.toLowerCase());
            // Return Widget Element
            return widgetElement;
        }
        /** Create Widget Elements - Create Dashboard Widget Zone */
        widgetDashboardLayout.createWidgetZoneElement = function(widgetZoneId){
            var widgetZoneElement = $(document.createElement("div"));
            widgetZoneElement.attr('id', widgetZoneId);
            widgetZoneElement.addClass("droppable-zone");
            widgetZoneElement.addClass("droppable-zone-image");
            widgetZoneElement.addClass("droppable-zone-default");
            return widgetZoneElement;
        }

        /********************************************************************
         * Retrieve Widget Data
         *
         * - retrieveAvailableWidgets
         *------------------------------------------------------------------*/
        /** Retrieve Widget Data : Retrieve Available Widgets */
        widgetDashboardLayout.retrieveAvailableWidgets = function(){
            var url = "dashboard.do?method=getAvailableWidgetList";
            $.getJSON(url, function(data){

                if(data.availableWidgets!=undefined){
                    // Populate Available Widgets in Draggable Panel
                    widgetDashboardLayout.populateAvailableWidgetPanel(data.availableWidgets);
                    // Format Populated Widgets Draggable
                    widgetDashboardLayout.formatDraggablePanels();
                    // Place Saved Widgets
                    widgetDashboardLayout.placeSavedWidgets(data.availableWidgets);
                }else{
                    return null;
                }
            });
        }

        /********************************************************************
         * Persist Widget Layout Data
         *
         * - persistDroppedWidget
         * - persistRemovedWidget
         *------------------------------------------------------------------*/
        /** Persist Widget Layout Data : Persist Dropped Widget */
        widgetDashboardLayout.persistDroppedWidget = function(widgetId, widgetZoneId){
            var zoneId = widgetZoneId.split('-')[2];
            $.post("dashboard.do?method=addDashboardWidget&id="+widgetId + "&zoneId="+zoneId, function(data){

            }, "json");
        };
        /** Persist Widget Layout Data : Persist Removed Widget */
        widgetDashboardLayout.persistRemovedWidget = function(widgetId, widgetZoneId){
            $.post("dashboard.do?method=closeWidget&id="+widgetId, function(data){

            }, "json");
        }


        /********************************************************************
         * Populate Widget Data
         *
         * - placeSavedWidgets
         * - buildWidgetData
         * - populateWidgetData
         * - buildWidgetFilterRowElement
         *------------------------------------------------------------------*/
        /** Populate Widget Data : Place Saved Widgets */
        widgetDashboardLayout.placeSavedWidgets = function(savedWidgets){
            // Place each Saved Widget
            $.each(savedWidgets, function(index, widget){
                if(widget.zoneId>-1){
                    if(widget.zoneId>0){
                        var widgetZoneId = "";
                        // Prepare widgetZoneId value
                        if(widget.zoneId<10){
                            var zoneId = "0"+widget.zoneId;
                            widgetZoneId = "widget-zone-"+zoneId;
                        }
                        // Define Dashboard Widget
                        var dashboardWidget = new DashboardWidget(widget.id, widget.type, widget.title, widgetZoneId, widget.filters);
                        // Build Widget
                        widgetDashboardLayout.buildWidget(dashboardWidget);
                        widgetDashboardLayout.selectedWidgetArray.push(dashboardWidget);
                        var widgetFilters = widget.filters;
                        var selectedWidgetFilter = "";
                        if(widgetFilters.length>0){
                            selectedWidgetFilter = widgetFilters[0];
                        }
                        widgetDashboardLayout.populateWidgetData(dashboardWidget.widgetId, dashboardWidget.widgetZoneId, dashboardWidget.widgetType, selectedWidgetFilter);
                    }
                }
            });
        }
        /** Populate Widget Data : Build Widget Data */
        widgetDashboardLayout.buildWidgetData = function(widgetId, widgetZoneId, widgetType, widgetFilterData, widgetData){
            // Widget Type must be in lower case
            var widgetType = widgetType.toLowerCase();
            // Populate Widget based on the widget type.
            switch(widgetType){
                case "affidavit":
                    widgetDashboardLayout.populateWidgetAffidavitData(widgetData.data, widgetId, widgetZoneId);
                    break;
                case "alert":
                    widgetDashboardLayout.populateWidgetAlertData(widgetData.data, widgetId, widgetZoneId);
                    break;
                case "course":
                    widgetDashboardLayout.populateWidgetCourseData(widgetData.data, widgetId, widgetZoneId, widgetFilterData);
                    break;
                case "profile":
                    widgetDashboardLayout.populateWidgetProfileData(widgetData.data, widgetId, widgetZoneId);
                    break;
                case "survey":
                    widgetDashboardLayout.populateWidgetSurveyData(widgetData.data, widgetId, widgetZoneId, widgetFilterData);
                    break;
                case "trainingplan":
                    widgetDashboardLayout.populateWidgetTrainingPlanData(widgetData.data, widgetId, widgetZoneId, widgetFilterData);
                    break;
                case "tutorial":
                    widgetDashboardLayout.populateWidgetTutorialData(widgetData.data, widgetId, widgetZoneId);
                    break;
                case "recommendations":
                    widgetDashboardLayout.populateWidgetMyRecommendationsData(widgetData.data, widgetId, widgetZoneId);
                    break;
                default:
            }
            // Format Widget Zone
            $('#'+widgetZoneId).removeClass('droppable-zone-default');
        }
        /** Populate Widget Data : Populate Dropped Widget Data */
        widgetDashboardLayout.populateWidgetData= function(widgetId, widgetZoneId, widgetType, widgetFilterData){
            var widgetDataUrl = "dashboard.do?method=getWidgetData&id="+widgetId;
            var widgetFilterLabel = "";
            var widgetFilterValue = "";

            if((widgetFilterData!=undefined)&&(widgetFilterData!="")){

                if(widgetFilterData.value!=undefined){
                    widgetFilterValue = widgetFilterData.value;
                }

                if(widgetFilterData.label!=undefined){
                    widgetFilterLabel = widgetFilterData.label;
                }
            }

            if((widgetFilterValue!=undefined) &&(widgetFilterValue!="")){
                widgetDataUrl = widgetDataUrl + "&filter="+widgetFilterValue;
            }

            // Define widget data variable
            var widgetData = "";

            // Create Widget Loading Element
            var widgetLoadingElement = $(document.createElement("div"));
            widgetLoadingElement.attr('id', 'widget-loading-element');

            // Add Widget Loading Element
            $('#'+widgetZoneId).append(widgetLoadingElement);


            var currentLoading = $('#' + widgetZoneId + '_loading-indicator-widget-loading-element');
            if(currentLoading.length>0){
                $('#'+widgetZoneId).find('#widget-loading-element').remove();
            }else{
                $('#'+widgetZoneId).find('#widget-loading-element').showLoading();
            }

            $('#loading-indicator-widget-loading-element').attr('id', widgetZoneId + '_loading-indicator-widget-loading-element');
            $('#loading-indicator-widget-loading-element-overlay').attr('id', widgetZoneId + '_loading-indicator-widget-loading-element-overlay');


            var totalTop = $('#'+widgetZoneId).offset().top;
            $('#' + widgetZoneId + '_loading-indicator-widget-loading-element').css('top', (totalTop)+'px');
            var totalLeft = (($('#'+widgetZoneId).width()-80)/2)+($('#'+widgetZoneId).offset().left);
            $('#' + widgetZoneId + '_loading-indicator-widget-loading-element').css('left', (totalLeft)+'px');


            $('.dashboard-content').scroll(function() {
                var totalTop = $('#'+widgetZoneId).offset().top;
                //console.log("widgetZoneId " + widgetZoneId);
                $('#' + widgetZoneId + '_loading-indicator-widget-loading-element').css('top', (totalTop)+'px');
                var totalLeft = (($('#'+widgetZoneId).width()-80)/2)+($('#'+widgetZoneId).offset().left);
                $('#' + widgetZoneId + '_loading-indicator-widget-loading-element').css('left', (totalLeft)+'px');

            });

            $(window).resize(function() {
                var totalTop = $('#'+widgetZoneId).offset().top;
                $('#' + widgetZoneId + '_loading-indicator-widget-loading-element').css('top', (totalTop)+'px');
                var totalLeft = (($('#'+widgetZoneId).width()-80)/2)+($('#'+widgetZoneId).offset().left);
                $('#' + widgetZoneId + '_loading-indicator-widget-loading-element').css('left', (totalLeft)+'px');

            });


            $.getJSON(widgetDataUrl, function(widgetData) {

                if(widgetData!=undefined){

                    if(widgetData.data.length>0){

                        //console.log(widgetDashboardLayout.selectedWidgetArray);
                        var populateWidgetId = false;

                        $.each(widgetDashboardLayout.selectedWidgetArray, function(index, value){
                            //console.log(value.widgetId);
                            if(widgetId==value.widgetId){
                                populateWidgetId = true;
                            }
                        });

                        if(populateWidgetId==true){
                            $('#' + widgetZoneId).find('#zone_content').html("");
                            widgetDashboardLayout.buildWidgetData(widgetId, widgetZoneId, widgetType, widgetFilterData, widgetData);
                            $('#'+widgetZoneId+ "_loading-indicator-widget-loading-element").remove();
                            $('#' + widgetZoneId + "_loading-indicator-widget-loading-element-overlay").remove();
                            widgetDashboardLayout.resizeWidgetZoneContainer();
                            $('#'+widgetZoneId).removeClass('droppable-zone-default');
                        }else{
                            $('#' + widgetZoneId + "_loading-indicator-widget-loading-element-overlay").remove();
                        }
                    }else{
                        widgetDashboardLayout.populateWidgetEmpty(widgetId, widgetZoneId, widgetFilterData, widgetType);
                        $('#'+widgetZoneId+ "_loading-indicator-widget-loading-element").remove();
                        $('#' + widgetZoneId + "_loading-indicator-widget-loading-element-overlay").remove();
                        $('#'+widgetZoneId).removeClass('droppable-zone-default');
                    }
                }
            });


            // Display Widget Zone
            $('#'+widgetZoneId).find('#zone_container').css("display", "block");
        }

        /********************************************************************
         * Build Widget Elements
         *
         * - buildWidgetFilterRowElement
         * - populateWidgetEmpty
         * - populateWidgetAffidavitData
         * - buildEmptyWidgetRowElement
         *------------------------------------------------------------------*/
        /**
         * Build Widget Filter Row Element
         */
        widgetDashboardLayout.buildWidgetFilterRowElement = function(widgetId, widgetZoneId, widgetFilterData){
            var widgetFilterRowElement = $(document.createElement("div"));
            widgetFilterRowElement.attr('id',widgetZoneId);
            widgetFilterRowElement.attr('class', 'widget-row-filter');
            var widgetFilterRowContentElement = $(document.createElement("div"));
            widgetFilterRowContentElement.attr('class', 'row-field-filter');
            //if((filterName=="viewall")||(filterName=="default")){
            //	filterName = $('body').find(":selected").text();
            //}else{
            //	filterName = filterName.replace(/\w\S*/g, function(txt){return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();})
            //}
            if(widgetFilterData.label!=undefined){
                widgetFilterRowContentElement.html(widgetFilterData.label);
            }else{
                widgetFilterRowContentElement.html("");
            }

            widgetFilterRowElement.append(widgetFilterRowContentElement);
            return widgetFilterRowElement;
        }
        /** Populate Empty Widget Element */
        widgetDashboardLayout.populateWidgetEmpty = function(widgetId, widgetZoneId, widgetFilter, widgetType){
            var emptyWidgetElementRow = widgetDashboardLayout.buildEmptyWidgetRowElement(widgetId, widgetZoneId, widgetType);
            // Add Constructed Empty Widget Row Element to Widget
            $('#' + widgetZoneId).find('#zone_content').append(emptyWidgetElementRow);
            //$('#' + widgetZoneId).find('#zone_description').css('display','none');
            //$('#' + widgetZoneId).find('.row-field-filter').html(widgetFilter.value);
        }
        /** Populate Widget Affidavit Element Data */
        widgetDashboardLayout.populateWidgetAffidavitData = function(data, widgetId, widgetZoneId){
            // Build each Affidavit row for Training Plan Widget
            $.each(data, function(index, affidafit){
                // Build Affidavit Widget Row Element
                var affidavitWidgetElementRow = widgetDashboardLayout.buildAffidavitWidgetRowElement(affidafit.dataMap, widgetId, widgetZoneId, index);
                // Add Constructed Affidavit Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(affidavitWidgetElementRow);
            });
            // Reformat Widget Zone
            //$('#'+widgetZoneId).removeClass('droppable-zone-default');
        }

        /**
         * Build Empty Widget Row Element
         */
        widgetDashboardLayout.buildEmptyWidgetRowElement = function(widgetId, widgetZoneId, widgetType){
            // Define Empty Row Element
            var emptyRowElement = $(document.createElement("div"));
            emptyRowElement.attr('id', 'row_1');
            emptyRowElement.addClass('widget-row');
            emptyRowElement.addClass('widget-empty-row');
            // Empty Name
            var emptyNameElement = $(document.createElement("div"));
            emptyNameElement.attr('id','emptyName-'+ widgetId);
            emptyNameElement.attr('class', 'row-field-empty-name');
            var emptyText = widgetDashboardLayout.toTitleCase(widgetType);
            emptyNameElement.html("There are no items to display.");
            // Construct Affidavit Widget Row HTML Element
            emptyRowElement.append(emptyNameElement);
            //$('#'+widgetZoneId).removeClass('droppable-zone-default');
            // Return Affidavit Widget Row Element
            return emptyRowElement;
        }





        /**
         * Build Affidavit Widget Row Element
         */
        widgetDashboardLayout.buildAffidavitWidgetRowElement = function(dataMap, widgetId, widgetZoneId, rowId){
            // Define Affidavit Row Element
            var affidavitRowElement = $(document.createElement("div"));
            affidavitRowElement.attr('id', 'row_'+rowId);
            affidavitRowElement.addClass('widget-row');
            affidavitRowElement.addClass('widget-affidavit-row');
            // Affidavit Name
            var affidavitNameElement = $(document.createElement("div"));
            affidavitNameElement.attr('id','affidavitName-'+ widgetId);
            affidavitNameElement.attr('class', 'row-field-affidavit-name');
            affidavitNameElement.html(dataMap.affidavitName);
            // Affidavit Link
            var affidavitStatusElement = $(document.createElement("div"));
            affidavitStatusElement.attr('id', 'affidavitLink-'+ widgetId);
            affidavitStatusElement.attr('class', 'row-field-affidavit-status');
            affidavitStatusElement.html(dataMap.status);
            // Construct Affidavit Widget Row HTML Element
            affidavitRowElement.append(affidavitNameElement);
            affidavitRowElement.append(affidavitStatusElement);
            // Return Affidavit Widget Row Element
            return affidavitRowElement;
        }

        /**
         * Alerts
         * - populateWidgetAlertData
         * - buildAlertWidgetRowElement
         */
        widgetDashboardLayout.populateWidgetAlertData = function(data, widgetId, widgetZoneId){
            // Define Alert Display Count
            var alertCount = 0;
            var maxAlertCount = 5;
            // Build each Alert row for Training Plan Widget
            $.each(data, function(index, alert){
                var alertWidgetElementRow = "";
                if(alertCount<maxAlertCount){
                    alertCount++;
                    // Build Alert Widget Row Element
                    alertWidgetElementRow = widgetDashboardLayout.buildAlertWidgetRowElement(alert.dataMap, widgetId, widgetZoneId, index);
                }
                // Add Constructed Alert Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(alertWidgetElementRow);
            });
            if(alertCount>maxAlertCount){
            	$('#' + widgetZoneId).find('#zone_content').append(widgetDashboardLayout.createShowAllWidget('/lms/lrn_myAlert.do?method=displayAlert'));
            }
        }
        widgetDashboardLayout.buildAlertWidgetRowElement = function(dataMap, widgetId, widgetZoneId, rowId){
            //Build Alert Row Text
            var alertName = "";
            var alertNumDays = "";
            var alertExpType = "";
            var alertEventDate = "";
            var alertEventType = "";
            // Define Alert Row Element
            var alertRowElement = $(document.createElement("div"));
            alertRowElement.attr('id', 'row_'+rowId);
            alertRowElement.addClass('widget-row');
            alertRowElement.addClass('widget-alert-row');
            // Alert Name
            var alertNameElement = $(document.createElement("div"));
            alertNameElement.attr('id','alertName-'+ widgetId);
            alertNameElement.attr('class', 'row-field-alert-name');
            alertNameElement.css('white-space', 'nowrap');
            alertNameElement.html(dataMap.name + " - " + dataMap.date);
            // Alert Link
            /*	var alertLinkElement = $(document.createElement("div"));
             alertLinkElement.attr('id', 'alertLink-'+ widgetId);
             alertLinkElement.attr('class', 'row-field-alert-link');
             alertLinkElement.html("View Alert");
             // Alert Link : Click Event
             alertLinkElement.live('click', function(){
             window.open(dataMap.link,"_self");
             });  */


            var offset = 5;

            alertRowElement.stop().hover(function(evt){
                $('<div id="tooltip" class="tooltip">'+ dataMap.messageBody +'</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
            }, function(){
                $('#tooltip').remove();
            });

            alertRowElement.mousemove(function(evt){
                $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
            });





            // Construct Alert Widget Row HTML Element
            alertRowElement.append(alertNameElement);
            /* alertRowElement.append(alertLinkElement); */
            // Return Alert Widget Row Element
            return alertRowElement;
        }

        /**
         * Courses Widget
         * - populateWidgetCourseData
         * - buildCourseWidgetRowElement
         */
        widgetDashboardLayout.populateWidgetCourseData = function(data, widgetId, widgetZoneId, widgetFilterData){
            // Define Course Display Count
            var courseCount = 0;
            var maxCourseCount = 5;
            // Build Selected Course Filter Row
            var selectedCourseFilterRow = widgetDashboardLayout.buildWidgetFilterRowElement(widgetId, widgetZoneId, widgetFilterData)
            $('#' + widgetZoneId).find('#zone_content').append(selectedCourseFilterRow);
            // Build each Course row for Courses Widget
            $.each(data, function(index, courseData){
                // Define Course Widget Row Element
                var courseWidgetElementRow = "";
                // Display defined number of Course Rows
                if(courseCount<maxCourseCount){
                    courseCount++;
                    courseWidgetElementRow = widgetDashboardLayout.buildCourseWidgetRowElement(courseData.dataMap, widgetId, widgetZoneId, index);
                }
                // Add Constructed Course Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(courseWidgetElementRow);
            });
            if(data.length > maxCourseCount){            
            	$('#' + widgetZoneId).find('#zone_content').append(widgetDashboardLayout.createShowAllWidget('lrn_myCourses.do?method=&search=Search&show=' + widgetFilterData.value));
            }
            //$('#'+widgetZoneId).removeClass('droppable-zone-default');
        }
        widgetDashboardLayout.buildCourseWidgetRowElement = function(dataMap, widgetId, widgetZoneId, rowId){
            // Define Course Row Element
            var courseRowElement = $(document.createElement("div"));
            courseRowElement.attr('id', 'row_'+rowId);
            courseRowElement.addClass('widget-row');
            courseRowElement.addClass('widget-course-row');
            // Course Name
            var courseNameElement = $(document.createElement("div"));
            courseNameElement.attr('id','courseName-'+ widgetId);
            courseNameElement.addClass('row-field-course-name');
            courseNameElement.css('cursor', 'pointer');
            courseNameElement.css('white-space', 'nowrap');

            // Truncate Training Plan Description
			var truncLen = 20;
			var newCourseName = dataMap.courseName;
			var shortCourseName = newCourseName.substring(0, truncLen);
            var courseStatus =  '<span style="color:#ccc;font-size:90%;margin-left:3px;font-weight:normal;">(' + dataMap.courseStatus + ')</span>';
			shortCourseName = shortCourseName.replace(/w+$/, '');
			if (newCourseName != shortCourseName) {
				newCourseName = shortCourseName + "...";
				shortCourseName = newCourseName;
			}

            shortCourseName += courseStatus;

			var offset = 5;
			
			courseRowElement.stop().hover(function(evt){
                $('<div id="tooltip" class="tooltip">'+ dataMap.courseName +'</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
            }, function(){
                $('#tooltip').remove();
            });

            courseRowElement.mousemove(function(evt){
                $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
            });


            // Replace with short course name.
            courseNameElement.html(shortCourseName);
            // Course Name Link : Click Event
            courseNameElement.live('click', function(){
                //	stopBothTimers();

                IS_LS360_SCORM = false;
                IS_LS360_SCORM = ((dataMap.courseCategoryType =="Scorm Course") ? true:false);

                var filter = $("#course-filter-select").find(":selected").val();

                if(filter!="expiredCourses"){
                    var url = 'lrn_launchCourse.do';
                    var params = "?courseId="+dataMap.courseID+"&learnerEnrollmentId="+dataMap.learnerEnrollmentID+"&method=displayLearnerProfile";
                    params = params + '&lguid=' +  $('#lguid').val();  // LMS-19305 - Invalid User GUID in LearningSession Table
                    if ( launchWindow != null ) {
                        launchWindow.close();
                    }
                    //var launchWindow = window.open(url+params,'VU360Player','address=no,resizable=yes,toolbar=no,location=no,scrollbars=yes,menubar=no,status=yes,width=1024,height=768,left=0,top=0');
                    if(!($.browser.msie || ie_ver()!=0))
                    {
                    launchWindow = window.open(url+params,'VU360Player','address=no,resizable=yes,toolbar=no,location=no,scrollbars=yes,menubar=no,status=yes,width=1024,height=768,left=0,top=0');
                    if (launchWindow.outerWidth < screen.availWidth || launchWindow.outerHeight < screen.availHeight)
                       {
                         launchWindow.moveTo(0,0);
                         launchWindow.resizeTo(screen.availWidth, screen.availHeight);
                       }
                   launchWindow.focus();
                   launchWindow.opener=window;
                    }
                    else
                    {
                         launchWindow = window.open(url+params,'VU360Player','address=no,resizable=yes,toolbar=no,location=no,scrollbars=yes,menubar=no,status=yes,width=1366,height=660,left=0,top=0');
                         launchWindow.focus();
                         launchWindow.opener=window;
                    }
                }





            });
            // Course Progress
            var courseProgressElement = $(document.createElement("div"));
            courseProgressElement.attr('id', 'courseProgress-'+ widgetId);
            courseProgressElement.addClass('row-field-course-progress');
            // Course Progress : Image Element
            var courseProgressImageElement = $(document.createElement("div"));
            courseProgressImageElement.attr('class', 'row-field-course-progress-image');
            // Course Progress : Image : Progress Bar
            var imageProgressBar = $(document.createElement("img"));
            imageProgressBar.attr('src', 'brands/default/en/images/progress_bar.jpg');
            imageProgressBar.attr('width', dataMap.percentComplete);
            imageProgressBar.attr('height', '11');
            // Course Progress : Image : Status Bar
            var imageStatusBar = $(document.createElement("img"));
            imageStatusBar.attr('src', 'brands/default/en/images/status_bar.jpg');
            imageStatusBar.attr('width', (100-dataMap.percentComplete));
            imageStatusBar.attr('height', '11');
            // Construct Course Progress HTML Element
            courseProgressImageElement.append(imageProgressBar);
            courseProgressImageElement.append(imageStatusBar);
            courseProgressElement.append(courseProgressImageElement);
            // Construct Course Widget Row HTML Element
            courseRowElement.append(courseNameElement);
            courseRowElement.append(courseProgressElement);
            // Return Course Widget Row Element
            return courseRowElement;
        }


        /**
         * Profile Widget
         * - populateWidgetProfileData
         * - buildProfileWidgetContentElement
         */
        widgetDashboardLayout.populateWidgetProfileData = function(data, widgetId, widgetZoneId){
            // Build Profile Widget Element
            var profileWidgetElement = widgetDashboardLayout.buildProfileWidgetContentElement(data[0].dataMap, widgetId, widgetZoneId);
            // Add Constructed Profile Widget Element to Widget Zone
            $('#' + widgetZoneId).find('#zone_content').append(profileWidgetElement);
            $('#' + widgetZoneId).find('#zone_content').append(widgetDashboardLayout.createShowAllWidget('lrn_learnerProfile.do'));
        }
        widgetDashboardLayout.buildProfileWidgetContentElement = function(profile, widgetId, widgetZoneId){
            // Define Profile Widget Variables
            var profileName = profile.firstName;
            if(profile.middleName!=null){
                profileName = profileName + " " + profile.middleName + " " + profile.lastName
            }else{
                profileName = profileName + " " + profile.lastName;
            }

            var profileAddress = "";
            var provileRowClass = "";
            if(profile.streetAddress!=undefined){
                profileAddress = profile.streetAddress;
            }
            if(profile.city!=null){
                profileAddress = profileAddress + " " +  profile.city;
            }
            if(profile.state!=null){
                profileAddress = profileAddress + " " + profile.state;
            }
            if(profile.zipCode!=undefined){
                profileAddress = profileAddress + " " + profile.zipCode;
            }
            if(profileAddress == ""){
                profileRowClass = "widget-profile-row-sm";
            }else{
                profileRowClass = "widget-profile-row-lg";
            }
            var profilePhone = profile.phone;
            var profileEmail = profile.emailAddress;
            // Define Profile Element
            var widgetProfileElement = $(document.createElement("div"));
            widgetProfileElement.addClass('widget-row');
            widgetProfileElement.addClass(profileRowClass);
            // Profile Name
            var widgetProfileNameElement = $(document.createElement("div"));
            widgetProfileNameElement.attr('id', 'profileName-'+ widgetId);
            widgetProfileNameElement.attr('class','row-field-profile-name')
            widgetProfileNameElement.html(profileName);
            // Profile Address
            var widgetProfileAddressElement = $(document.createElement("div"));
            widgetProfileAddressElement.attr('id', 'profileAddress-'+ widgetId);
            widgetProfileAddressElement.attr('class','row-field-profile-address')
            widgetProfileAddressElement.html(profileAddress);
            // Profile Phone Number
            var widgetProfilePhoneElement = $(document.createElement("div"));
            widgetProfilePhoneElement.attr('id', 'profilePhone-'+ widgetId);
            widgetProfilePhoneElement.attr('class','row-field-profile-phone')
            widgetProfilePhoneElement.html(profilePhone);
            // Profile Email Address
            var widgetProfileEmailElement = $(document.createElement("div"));
            widgetProfileEmailElement.attr('id', 'profileEmail-'+ widgetId);
            widgetProfileEmailElement.attr('class','row-field-profile-email')
            widgetProfileEmailElement.html(profileEmail);
            // Profile Change Password
            var widgetProfilePasswordElement = $(document.createElement("div"));
            widgetProfilePasswordElement.attr('id','profilePassword-'+ widgetId);
            widgetProfilePasswordElement.attr('class','row-field-profile-password');
            widgetProfilePasswordElement.html("Account Management");
            widgetProfilePasswordElement.live('click', function(){
                window.open(profile.link,"_self");
            });

            // Construct Profile Widget Element
            widgetProfileElement.append(widgetProfileNameElement);
            widgetProfileElement.append(widgetProfileAddressElement);
            widgetProfileElement.append(widgetProfilePhoneElement);
            widgetProfileElement.append(widgetProfileEmailElement);
            widgetProfileElement.append(widgetProfilePasswordElement);
            // Return Profile Widget Element
            return widgetProfileElement;
        }


        /**
         * Survey Widget
         * - populateWidgetSurveyData
         * - buildSurveyWidgetRowElement
         */
        widgetDashboardLayout.populateWidgetSurveyData = function(data, widgetId, widgetZoneId, widgetFilterData){
            // Define Survey Display Count
            var surveyCount = 0;
            var maxSurveyCount = 5;
            // Build Selected Survey Filter Row
            var selectedSurveyFilterRow = widgetDashboardLayout.buildWidgetFilterRowElement(widgetId, widgetZoneId, widgetFilterData)
            // Add Selected Filter Row to Widget
            $('#' + widgetZoneId).find('#zone_content').append(selectedSurveyFilterRow);
            // Build each Survey row for Survey Widget
            $.each(data, function(index, surveyData){
                // Define Survey Widget Row Element
                var surveyWidgetElementRow = "";
                // Display defined number of Survey Rows
                if(surveyCount < maxSurveyCount){
                    surveyCount++;
                    surveyWidgetElementRow = widgetDashboardLayout.buildSurveyWidgetRowElement(surveyData.dataMap, widgetId, widgetZoneId, index);
                }
                // Add Constructed Survey Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(surveyWidgetElementRow);
            });
            if(widgetFilterData.label != 'Completed' && data.length > maxSurveyCount){
            	$('#' + widgetZoneId).find('#zone_content').append(widgetDashboardLayout.createShowAllWidget('lrn_mySurveys.do?method=displaySurveys'));
            }
        }
        widgetDashboardLayout.buildSurveyWidgetRowElement = function(dataMap, widgetId, widgetZoneId, rowId){
            // Define Survey Row Element
            var surveyRowElement = $(document.createElement("div"));
            surveyRowElement.attr('id', 'row_'+rowId);
            surveyRowElement.addClass('widget-row');
            surveyRowElement.addClass('widget-survey-row');
            // Survey Name
            var surveyNameElement = $(document.createElement("div"));
            surveyNameElement.attr('id','surveyName-'+ widgetId);
            surveyNameElement.attr('class', 'row-field-survey-name');
            surveyNameElement.css('white-space', 'nowrap');
            // Truncate Training Plan Description
            var truncLen = 30;
            var newSurveyName = dataMap.name;
            var shortSurveyName = newSurveyName.substring(0, truncLen);
            shortSurveyName = shortSurveyName.replace(/w+$/, '');
            if (newSurveyName != shortSurveyName) {
                newSurveyName = newSurveyName + "...";
                shortSurveyName = newSurveyName;
            }
            // Survey Link : Click Event
            surveyNameElement.live('click', function(){
                // TODO: Replace with link opening.
                //console.log(dataMap.link);
                window.open("lrn_mySurveys.do?method=displaySurveys","_self");
            });
            // Replace with Survey short name
            surveyNameElement.html(shortSurveyName);
            // Construct Survey Widget Row HTML Element
            surveyRowElement.append(surveyNameElement);
            //surveyRowElement.append(surveyLinkElement);
            // Return Survey Widget Row Element
            return surveyRowElement;
        }


        /**
         * Training Plan Widget
         * - populateWidgetTrainingPlanData
         * - buildTrainingPlanWidgetRowElement
         */
        widgetDashboardLayout.populateWidgetTrainingPlanData = function(data, widgetId, widgetZoneId, widgetFilterData){
            // Build each Training Plan row for Training Plan Widget
            $.each(data, function(index, trainingPlan){
                // Build Training Plan Widget Row Element
                var trainingPlanWidgetElementRow = widgetDashboardLayout.buildTrainingPlanWidgetRowElement(trainingPlan.dataMap, widgetId, widgetZoneId, index);
                // Add Constructed Training Plan Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(trainingPlanWidgetElementRow);
            });
        }
        widgetDashboardLayout.buildTrainingPlanWidgetRowElement = function(trainingPlan, widgetId, widgetZoneId, rowId){
            // Define Training Plan Row Variables
            var trainingPlanName = trainingPlan.name;
            var trainingPlanDescription = trainingPlan.description;
            // Truncate Training Plan Description
            var truncLen = 35;
            var newDescription = "";
            if (trainingPlanDescription.length > truncLen) {
                newDescription = trainingPlanDescription.substring(0, truncLen);
                newDescription = newDescription.replace(/w+$/, '');
                if (trainingPlanDescription != newDescription) {
                    newDescription = newDescription + "...";
                    trainingPlanDescription = newDescription;
                }
            }
            // Define Training Plan Row Element
            var trainingPlanRowElement = $(document.createElement("div"));
            trainingPlanRowElement.attr('id', 'row_'+rowId);
            trainingPlanRowElement.addClass('widget-row');
            trainingPlanRowElement.addClass('widget-trainingplan-row');
            // Training Plan Name
            var trainingPlanNameElement = $(document.createElement("div"));
            trainingPlanNameElement.attr('id','trainingPlanName-'+ widgetId);
            trainingPlanNameElement.attr('class', 'row-field-trainingplan-name');
            // Truncate Training Plan Description
            var truncLen = 30;
            var newTrainingPlanName = trainingPlanName;
            var shortTrainingPlanName = newTrainingPlanName.substring(0, truncLen);
            shortTrainingPlanName = shortTrainingPlanName.replace(/w+$/, '');
            if (newTrainingPlanName != shortTrainingPlanName) {
                newTrainingPlanName = newTrainingPlanName + "...";
                shortTrainingPlanName = newTrainingPlanName;
            }
            // Training Plan Link : Click Event
            trainingPlanNameElement.live('click', function(){
                //console.log("// Training Plan Link");
                //console.log(trainingPlan.link);
                //console.log("// ");
                window.open(trainingPlan.link,"_self");
            });
            // Replace with Training Plan short name
            trainingPlanNameElement.html(shortTrainingPlanName);
            // Training Plan Link
            var trainingPlanLinkElement = $(document.createElement("div"));
            trainingPlanLinkElement.attr('id', 'trainingPlanLink-'+ widgetId);
            trainingPlanLinkElement.attr('class', 'row-field-trainingplan-link');
            trainingPlanLinkElement.html("View Training Plan");
            //Training Plan Description
            var trainingPlanDescriptionElement = $(document.createElement("div"));
            trainingPlanDescriptionElement.attr('id', 'trainingPlanDescription-'+ widgetId);
            trainingPlanDescriptionElement.addClass('row-field-trainingplan-description');
            trainingPlanDescriptionElement.html(trainingPlanDescription);
            // Construct Training Plan Widget Row Element
            trainingPlanRowElement.append(trainingPlanNameElement);
            //trainingPlanRowElement.append(trainingPlanLinkElement);
            //trainingPlanRowElement.append(trainingPlanDescriptionElement);
            // Return Training Plan Widget Row Element
            return trainingPlanRowElement;
        }


        /**
         * Training Plan Widget
         * - populateWidgetTutorialData
         * - buildTutorialWidgetRowElement
         */
        widgetDashboardLayout.populateWidgetTutorialData = function(data, widgetId, widgetZoneId){

            // Build each Tutorial Mode
            $.each(data, function(index, tutorial){



                if(tutorial.dataMap.videoUrls!=undefined){
                    widgetDashboardLayout.buildTutorialVideoUrls(tutorial.dataMap, widgetId, widgetZoneId, index);
                }



                if(tutorial.dataMap.pdfUrls!=undefined){
                    widgetDashboardLayout.buildTutorialPdfUrls(tutorial.dataMap, widgetId, widgetZoneId, index);
                }


            });
        }


        widgetDashboardLayout.buildTutorialVideoUrls = function(dataMap, widgetId, widgetZoneId, index){
            $.each(dataMap.videoUrls, function(index, url){
                // Build Tutorial Widget Row Element
                var tutorialWidgetElementRow = widgetDashboardLayout.buildTutorialWidgetRowElement(url, widgetId, widgetZoneId, index, "video");
                // Add Constructed Tutorial Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(tutorialWidgetElementRow);
            });
        }

        widgetDashboardLayout.buildTutorialPdfUrls = function(dataMap, widgetId, widgetZoneId, index){
            $.each(dataMap.pdfUrls, function(index, url){
                // Build Tutorial Widget Row Element
                var tutorialWidgetElementRow = widgetDashboardLayout.buildTutorialWidgetRowElement(url, widgetId, widgetZoneId, index, "pdf");
                // Add Constructed Tutorial Widget Row Element to Widget
                $('#' + widgetZoneId).find('#zone_content').append(tutorialWidgetElementRow);
            });
        }

        widgetDashboardLayout.buildTutorialWidgetRowElement = function(dataMap, widgetId, widgetZoneId, rowId, urlType){
            // Define Tutorial Row Element
            var tutorialRowElement = $(document.createElement("div"));
            tutorialRowElement.attr('id', 'row_'+rowId);
            tutorialRowElement.addClass('widget-row');
            tutorialRowElement.addClass('widget-tutorial-row');
            // Tutorial Name
            var tutorialNameElement = $(document.createElement("div"));
            tutorialNameElement.attr('id','tutorialName-'+ widgetId);
            tutorialNameElement.attr('class', 'row-field-tutorial-name');
            tutorialNameElement.html(dataMap.title);
            // Tutorial Link
            var tutorialLinkElement = $(document.createElement("div"));
            tutorialLinkElement.attr('id', 'tutorialLink-'+ widgetId);
            tutorialLinkElement.attr('class', 'row-field-tutorial-link');

            if(urlType=="video"){
                tutorialLinkElement.html("View Tutorial");
            }else if(urlType=="pdf"){
                tutorialLinkElement.html("View PDF");
            }
            // Tutorial Link : Click Event
            tutorialLinkElement.live('click', function(){
                if(urlType=="video"){
                    widgetDashboardLayout.testAddVideo(dataMap.title, dataMap.url);
                }else if(urlType=="pdf"){
                    window.open(dataMap.url);
                }

            });
            // Construct Tutorial Widget Row HTML Element
            tutorialRowElement.append(tutorialNameElement);
            tutorialRowElement.append(tutorialLinkElement);
            // Return Tutorial Widget Row Element
            return tutorialRowElement;
        }

        widgetDashboardLayout.populateWidgetMyRecommendationsData =  function(data, widgetId, widgetZoneId){

            //alert("widgetDashboardLayout.populateWidgetMyRecommendationsData");

            $.each(data, function(index, recommendations){
                widgetDashboardLayout.buildMyRecommendationsUrls(recommendations.dataMap, widgetId, widgetZoneId, index);
            });
        }

        widgetDashboardLayout.buildMyRecommendationsUrls = function(dataMap, widgetId, widgetZoneId, index){
            //alert("widgetDashboardLayout.buildMyRecommendationsUrls");
            var myRecommendationsWidgetElementRow = widgetDashboardLayout.buildMyRecommendationsWidgetRowElement(dataMap.url, dataMap.name, widgetId, widgetZoneId, index);
            var zoneContent = $('#' + widgetZoneId).find('#zone_content');
            $('#' + widgetZoneId).find('#zone_content').append(myRecommendationsWidgetElementRow);
        }

        function confirm(message, callback) {
            $('#deleteConfirmation').modal({
                close:false,
                position: ["40%",],
                width: 410,
                overlayId:'confirmModalOverlay',
                containerId:'confirmModalContainer',
                onShow: function (dialog) {
                    dialog.data.find('.message_box_text').append(message);

                    dialog.data.find('.yes').click(function () {
                        // call the callback
                        if (jQuery.isFunction(callback)) {
                            callback.apply();
                        }
                        // close the dialog
                        jQuery.modal.close();
                    });

                    dialog.data.find('.yes').click(function () {

                        jQuery.modal.close();
                    });
                }
            });
        }


        widgetDashboardLayout.buildMyRecommendationsWidgetRowElement = function(url, title, widgetId, widgetZoneId, rowId){

            //alert("widgetDashboardLayout.buildMyRecommendationsWidgetRowElement");


            var myRecommendationsRowElement = $(document.createElement("div"));
            myRecommendationsRowElement.attr('id', 'row_'+rowId);
            myRecommendationsRowElement.addClass('widget-row');
            myRecommendationsRowElement.addClass('widget-tutorial-row');

            var myRecommendationsNameElement = $(document.createElement("div"));
            myRecommendationsNameElement.attr('id','tutorialName-'+ widgetId);
            myRecommendationsNameElement.attr('class', 'row-field-tutorial-name');
            myRecommendationsNameElement.html(title);

            var myRecommendationsLinkElement = $(document.createElement("a"));
            myRecommendationsLinkElement.attr('id', 'tutorialLink-'+ widgetId);
            myRecommendationsLinkElement.attr('class', 'row-field-tutorial-link');
            myRecommendationsLinkElement.attr('href', url);
            myRecommendationsLinkElement.attr('target', "_blank");
            myRecommendationsLinkElement.attr('style',"color:blue;")
            //myRecommendationsLinkElement.html("<font color='blue'> More information</font>");
            myRecommendationsLinkElement.html("More information");

            myRecommendationsLinkElement.live('click', function(e){
                if(e.preventDefault)
                {
                    e.preventDefault();
                }

                else
                {
                    e.returnValue = false;
                }




                var href =$(this).attr('href');

                if(confirm('This will open the product page for this course in a second browser window. Do you want to continue?', function () {

                    window.open(href,'_blank');
                }));

            });


            myRecommendationsRowElement.append(myRecommendationsNameElement);
            myRecommendationsRowElement.append(myRecommendationsLinkElement);

            return myRecommendationsRowElement;

        }


        widgetDashboardLayout.testAddVideo = function(videoTitle, videourl){

        	var browser=navigator.appName;
        	
    	// Following if condition execute for all browser except IE
    	if(!($.browser.msie || ie_ver()!=0))
        {
        	var flashContentLayerElement = $(document.createElement("div"));
            flashContentLayerElement.addClass("video-WidgetZone-Layer");
            flashContentLayerElement.attr('id', 'videoLayer');
            
                        
                        
           var flashContent1 = document.getElementById("flashContent");
                        
           if(!flashContent1)
              {
              var flashContent2 = $(document.createElement("div"));
              //flashContentLayerElement.addClass("flashContent");
              flashContent2.attr('id', 'flashContent');
              flashContent2.appendTo('body');
            }
            
            if($.browser.msie)
            {
            	allsuspects = document.getElementById("flashContent");
            	//alert(allsuspects);
            	if(allsuspects == null)
            	{
            		//alert(allsuspects);
            		var flashIEContent = $(document.createElement("div"));
            		flashIEContent.attr('id', 'flashContent');
            		flashIEContent.appendTo('body');
            	}
            }


            var val = navigator.userAgent.toLowerCase();
            
            // Video Player Variables
            var d = new Date();
            var videoPlayerName= 'videoplayer'+d.getTime();
            var videoApp = "";
            var videoFile = "";
            var videoServer = "streaming.360training.com";

            // Video Player Params
            var videoUrlParams = videourl;
            paramsArray = videoUrlParams.split('&amp;');

            for (i=0; i<paramsArray.length; i++) {
                var subArray = paramsArray[i].split('=');
                if(subArray.length==2) {
                    switch(subArray[0]) {
                        case 'volumelevel':
                            volumeLevel =subArray[1];
                            break;
                        case 'server':
                            videoServer=subArray[1];
                            break;
                        case 'app':
                            videoApp=subArray[1];
                            break;
                        case 'filename':
                            videoFile=subArray[1];
                            break;
                    }
                }
            }

            videoFile =  'http://streaming.360training.com/vod/fse/'+ videoFile;
            // Build Flash Player
            //$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+videoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; }    function thisMovie(movieName) {   if (navigator.appName.indexOf('Microsoft') != -1) { return window[movieName]; } else { return document[movieName];  }    }   function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = thisMovie(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables('"+videoServer+"', '"+videoApp+"', '"+videoFile+"'); }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+videoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <object type='application/x-shockwave-flash' data='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' name='"+videoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </object>   <!--<![endif]-->  </object>");
            //$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('flv','"+videoFile+"','rtmp://flash-1.360training.com/fse/','rtmp');} ShowVideo();</script>");
            //j360Player.PlayerCall("f4v","mp4:HERA1-9.f4v","rtmp://streaming.360training.com/ndn","rtmp");

            $("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','"+videoFile+"');} ShowVideo();</script>");            
            $("#flashContent_wrapper").css('position', 'absolute');
            $("#flashContent_wrapper").css('top','223px');
            $('#flashContent_wrapper').attr('style','z-index:11;');
            $('#flashContent_wrapper').css('left', '322px');

            // Display Fade Out

            var fadeOutElement = $(document.createElement("div"));
            fadeOutElement.attr('id','fadeout-container');
            fadeOutElement.addClass("flashContentDisplay");
            fadeOutElement.css('display','block');
            //$('body').append(fadeOutElement);
            fadeOutElement.appendTo('body');


            // Build Video Title
            var videoTitleWidgetZoneLayoutElement = $(document.createElement("div"));
            videoTitleWidgetZoneLayoutElement.attr('id','videoTitleWidgetZoneLayoutElement');
            videoTitleWidgetZoneLayoutElement.addClass("video-title-widget-zone-layout");
            videoTitleWidgetZoneLayoutElement.html(videoTitle);

            // Build Video Close
            var closeVideoWidgetZoneLayoutElement = $(document.createElement("div"));
            closeVideoWidgetZoneLayoutElement.attr('id','closeVideoWidgetZoneLayoutElement');
            closeVideoWidgetZoneLayoutElement.addClass("close-vdeo-widget-zone-layout");
            closeVideoWidgetZoneLayoutElement.html("Close Video");
            closeVideoWidgetZoneLayoutElement.bind('click', function(){
                $('#videoLayer').remove();
                $('#fadeout-container').css('display','none');
                $('#flashContent_wrapper').css('display','none');
                
                if($.browser.msie)
                {
                	allsuspects = document.getElementById("flashContent");
                	//alert(allsuspects);
                	allsuspects.parentNode.removeChild(allsuspects);
                }
                	
                	
                	//$("#flashContent").html("<script type='text/javascript'></script>");	
                
                
                $('#flashContent').attr('type','');
                $('#flashContent').attr('data','');
                $('#flashContent').attr('name','');
                $("#flashContent").empty();
                $("#closeVideoWidgetZoneLayoutElement").empty();
                //$('#flashContent').attr('html',"<script type='text/javascript'>alert('hello')</script>");
                               
                //$("#flashContent").html("<script type='text/javascript'></script>");
                //$('#flashContent_wrapper').attr('style','');
            });

            // Build Video Heading
            var videoHeadingWidgetZoneLayoutElement = $(document.createElement("div"));
            videoHeadingWidgetZoneLayoutElement.attr('id','videoHeadingWidgetZoneLayout');
            videoHeadingWidgetZoneLayoutElement.attr('style', 'width:100%; background-color: gray; height:45px; z-index:11;');

            videoHeadingWidgetZoneLayoutElement.append(videoTitleWidgetZoneLayoutElement);
            videoHeadingWidgetZoneLayoutElement.append(closeVideoWidgetZoneLayoutElement);
            
                        
                        
           if((val.indexOf("chrome") > -1) )
           {
            var flashContent = $("#flashContent");
            videoHeadingWidgetZoneLayoutElement.after(flashContent);
            $('#flashContent').attr('width',"99.8%;");
            $('.video-WidgetZone-Layer #flashContent #flashContent_video_wrapper').attr('left',"100px!important;");
            
           }
           else if(val.indexOf("safari") > -1)
           {
              var flashContent = $("#flashContent");
              videoHeadingWidgetZoneLayoutElement.after(flashContent);
              $("#videoLayer").css("left: 35px!important;");
           }
                        
                        

            // Build Flash Content Layer Element
            flashContentLayerElement.append(videoHeadingWidgetZoneLayoutElement);
          flashContentLayerElement.css('top', ($('.droppableOverview').offset().top+10)+"px");


            $('.dashboard-content').scroll(function() {
                $('#videoLayer').css('top', ($('.droppableOverview').offset().top+21)+"px");
                $('#flashContent_wrapper').css('top', ($('.droppableOverview').offset().top+45)+"px");
                var vidWid = $('#videoLayer').width();
                var flashWid = $('#flashContent_wrapper').width();

                var vidLeft = ((vidWid - flashWid) /2)+($('#droppableZone').offset().left);

                $('#flashContent_wrapper').css('left', (vidLeft+"px"));

            });

            $('#flashContent_wrapper').attr('style','z-index:11;');
            $('#flashContent_wrapper').css('position', 'absolute');
            //$('#flashContent_wrapper').css('top', '125px');
            
            var isChrome = /Chrome/.test(navigator.userAgent) && /Google Inc/.test(navigator.vendor);
            
            if($.browser.msie)
	            {
	              $('#flashContent_wrapper').css('top', '130px');
	            }
           /* else if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1)
            {
            	$('#flashContent_wrapper').css('top', '223px');
            }*/
            else{
            	$('#flashContent_wrapper').css('top', '223px');
            	$('#videoLayer').css('top', '88.5px!important');
            	
            	
            }
            	
            $('#flashContent_wrapper').css('left', (($(window).width() - $('#flashContent_wrapper').width())/2) +"px");

            $('body').append(flashContentLayerElement);

            $('#videoLayer').css("left", (($(window).width() - $('#videoLayer').width())/2) + "px");
            $('#videoLayer').css('top', (($(window).height() - $('#videoLayer').height())/2) + 'px');
            $('#videoLayer').css('z-index','10');
            $('#videoLayer').css('height','583px');

            var vidWid = $('#videoLayer').width();
            var flashWid = $('#flashContent_wrapper').width();

            var vidLeft = ((vidWid - flashWid) /2)+($('#droppableZone').offset().left);

            //$('#flashContent').css('left', (vidLeft+"px"));

            $('#fadeout-container').css('display','block');
            $('#fadeout-container').css('position','absolute');
            $('#fadeout-container').css('top','0px');
            $('#fadeout-container').css('left','0px');
            $('#fadeout-container').css('z-index','10');
            
        }
    	// Code execute for IE
    	else 
    	{
        		
        		
        		var d = new Date();
                var videoPlayerName= 'videoplayer'+d.getTime();
                var videoApp = "";
                var videoFile = "";
                var videoServer = "streaming.360training.com";

                // Video Player Params
                var videoUrlParams = videourl;
                paramsArray = videoUrlParams.split('&amp;');

                for (i=0; i<paramsArray.length; i++) {
                    var subArray = paramsArray[i].split('=');
                    if(subArray.length==2) {
                        switch(subArray[0]) {
                            case 'volumelevel':
                                volumeLevel =subArray[1];
                                break;
                            case 'server':
                                videoServer=subArray[1];
                                break;
                            case 'app':
                                videoApp=subArray[1];
                                break;
                            case 'filename':
                                videoFile=subArray[1];
                                break;
                        }
                    }
                }
                
                var videotest = 'http://streaming.360training.com/vod/360/WP11.mp4';
                videoFile =  'http://streaming.360training.com/vod/fse/'+ videoFile;
               // alert(videoFile);
        		
                    // $("#mode-heading").html("Administrator Mode");        
                    // var videourl=$("#guidedTourAdministratorVideo").val();
                     //var d = new Date();  
                 	//var VideoPlayerName= 'videoplayer'+d.getTime();
                 	//alert(videourl);
                 	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
                 	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','"+videoFile+"');} ShowVideo();</script>");
                     $("#fadeout-container").css("display", "block");
                     $("#alert-box").css("display", "block");
                

                 $("#close-video").click(function(){
                     window.location.reload();
                     //$("#fadeout-container").css("display", "none");
                     //$("#alert-box").css("display", "none");
                 });
        		
        		
        	}
        }



        /********************************************************************
         * Populate Available Widgets and Widget Zones
         * - populateWidgetZonePanel
         * - populateAvailableWidgetPanel
         *------------------------------------------------------------------*/


        /** Populate Widget Panels : Populate Widget Zone Panel */
        widgetDashboardLayout.populateWidgetZonePanel = function() {
            var widgetZoneCount = widgetDashboardLayout.options.availableWidgetZones.length;
            // Arrange Widget Zones in Seperate HTML Columns
            $.each(widgetDashboardLayout.options.availableWidgetZones, function(index, value){
                // Create Widget Zone Element
                var widgetZoneElement = widgetDashboardLayout.createWidgetZoneElement(value);
                // Add Widget Zone Element to Widget Zone Panel
                if(widgetZoneCount / 2 > index){
                    $('#droppableZoneLeft').append(widgetZoneElement);
                }else{
                    $('#droppableZoneRight').append(widgetZoneElement);
                }
            });
            widgetDashboardLayout.defaultWidgetTypeLayout.draggableHeight = widgetDashboardLayout.options.availableWidgetZones.length*(105)+ "px";
            $('#draggableZone').height(widgetDashboardLayout.defaultWidgetTypeLayout.draggableHeight);

        };
        /** Populate Widget Panels : Populate Availabnle Widget Panel */
        widgetDashboardLayout.populateAvailableWidgetPanel = function(availableWidgetOptions) {
            // Create and add an Object and an HTML Element for each of the availableWidgetOptionResults.
            $.each(availableWidgetOptions, function(index, widget){
                // Define Dashboard Widget Object
                var dashboardWidget = new DashboardWidget(widget.id, widget.type, widget.title, widget.zoneId, widget.filters);
                // Define Dashboard Widget HTML Object
                var widgetHTML = widgetDashboardLayout.createAvailableWidgetElement(dashboardWidget);
                // Add Widget object to 'Draggable Zone'
                $('#draggableZone').append(widgetHTML);
            });
            // Assign available widgets array.
            widgetDashboardLayout.availableWidgets = availableWidgetOptions;
        };


        /********************************************************************
         * Build Widget Components
         *
         * - buildWidget
         * - buildCoursesWidget
         *------------------------------------------------------------------*/


        /** Build Widget Components - Build Widget */
        widgetDashboardLayout.buildWidget = function(widgetObject){
            // Define Local Widget Object Variables



            var widgetId = widgetObject.widgetId;
            var widgetZoneId = widgetObject.widgetZoneId;
            var widgetType = "";
            var widgetFilters = widgetObject.widgetFilters;
            // Define Widget Identifier
            var widgetNum
            if(widgetId>0){
                widgetNum = widgetId;
            }else{
                widgetNum = widgetId.split('-')[1];
            }
            // Find Widget Object in Available Widgets Array
            $.each(widgetDashboardLayout.availableWidgets, function(index, widget){
                // Define Variable for Widget HTML
                var widgetHTML = "";
                if(widget==undefined){
                }else{
                    if(widget.id==widgetId){
                        // Define Widget Object Clone Variable
                        var widgetElement = "";
                        var widgetTitle = widget.title;
                        var widgetDescription = widget.description;
                        widgetType = widget.type.toLowerCase();
                        // Define Widget Type
                        switch(widgetType){
                            case "affidavit":
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createBasicWidgetElement();
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                break;
                            case "alert":
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createBasicWidgetElement();
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                break;
                            case "course":
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createFilteredWidgetElement(widgetId, widgetZoneId, widgetFilters, widgetType);
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                break;
                            case "profile" :
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createBasicWidgetElement();
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                break;
                            case "survey":
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createFilteredWidgetElement(widgetId, widgetZoneId, widgetFilters, widgetType);
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                break;
                            case "trainingplan" :
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createFilteredWidgetElement(widgetId, widgetZoneId, widgetFilters, widgetType);
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                break;
                            case "tutorial" :
                            case "recommendations" :
                                // Create widget and assign styling
                                widgetElement = widgetDashboardLayout.createBasicWidgetElement();
                                widgetElement.attr('id', widgetId);
                                widgetElement.find('#zone_close').attr('id', 'close-widget-'+widgetNum);
                                widgetElement.find('#zone_title').html(widgetTitle);
                                if(widgetType == "recommendations"){
                                    widgetElement.find('#zone_intro').after("<div style='float:left;padding:3px;font-weight:bold;'>Recommended Courses</div>");
                                    //widgetElement.find('#zone_intro').after('<script>alert(' + deleteconfirm + ')</script>');
                                }
                                break;
                            default:
                            //console.log("unsupported widget type");
                        }

                        // Build Close Icon Button
                        var closeIcon = $('#close-'+widgetId);
                        closeIcon.addClass("close-widget");
                        // Remove Widget from Available Widget Panel
                        $('#draggableZone').find('#'+widgetId).addClass('widget-icon-'+widgetType + '-grey');
                        $('#draggableZone').find('#'+widgetId).attr('style','');
                        $('#'+widgetZoneId).removeClass("droppable-zone-image");
                        // Place Created Widget in Widget Zone
                        $('#'+widgetZoneId).html(widgetElement);
                        // Add Widget Close Event
                        $('#close-widget-'+widgetNum).bind('click', function(){
                            $('#'+widgetZoneId).removeAttr('style');
                            var l = $('#draggableZone').find('.widget-icon-'+ widgetType +'-grey').remove();
                            // Remove Widget
                            widgetDashboardLayout.removeDashboardWidget(widgetId, widgetZoneId, widgetTitle, widgetType);
                            $('#'+widgetZoneId).addClass("droppable-zone-image");
                            $('#'+widgetZoneId).addClass("droppable-zone-default");
                            $('#'+widgetZoneId + "_loading-indicator-widget-loading-element").remove();
                            // Resize Zone Container
                            widgetDashboardLayout.resizeWidgetZoneContainer();
                        });
                    }
                }
            });
        }

        widgetDashboardLayout.refreshFilteredWidgetElement = function(widgetId, widgetZoneId, widgetType, selectedFilter, widgetFilters){
            $("#"+widgetZoneId).find("#zone_content").html("");
            var currentlySelectedFilter = new Array();
            $.each(widgetFilters, function(index, filter){
                if(filter.value==selectedFilter){
                    currentlySelectedFilter.push(widgetFilters[index]);
                }
            });
            var widgetFilterData = currentlySelectedFilter[0];
            widgetDashboardLayout.populateWidgetData(widgetId, widgetZoneId, widgetType, widgetFilterData);
        }

        /** Build Widget Components - Build Filtered Widget Element */
        widgetDashboardLayout.createFilteredWidgetElement = function(widgetId, widgetZoneId, widgetFilters, widgetType){

            // Create Widget Zone Element
            var widgetZoneElement = $(document.createElement("div"));
            widgetZoneElement.attr('id', 'widget-zone');

            // Create Widget Container Element
            var widgetZoneContainerElement = $(document.createElement("div"));
            widgetZoneContainerElement.attr('id', 'zone_container');
            widgetZoneContainerElement.attr('class', 'widget-container');
            widgetZoneContainerElement.css('display',"none");

            // Create Widget Zone Container Intro Element
            var widgetZoneContainerIntroElement = $(document.createElement("div"));
            widgetZoneContainerIntroElement.attr('id', 'zone_intro');
            widgetZoneContainerIntroElement.attr('class', 'widget-intro');

            // Create Widget Zone Container Title Element
            var widgetZoneContainerTitleElement = $(document.createElement("div"));
            widgetZoneContainerTitleElement.attr('id', 'zone_title');
            widgetZoneContainerTitleElement.attr('class','widget-title');

            // Create Widget Zone Container Description Element
            /*var widgetZoneContainerDescriptionElement = $(document.createElement("div"));
             widgetZoneContainerDescriptionElement.attr('id', 'zone_description');
             widgetZoneContainerDescriptionElement.attr('class', 'widget-description'); */

            // Create Widget Zone Container Filter Element
            var widgetZoneContainerFilterElement = $(document.createElement("div"));
            widgetZoneContainerFilterElement.attr('id', 'zone_filter');
            widgetZoneContainerFilterElement.attr('class', 'widget-filter');

            // Create Widget Zone Container Select Element
            var widgetZoneFilterSelect = $(document.createElement("select"));
            widgetZoneFilterSelect.addClass("widget-filter-select");
            widgetZoneFilterSelect.attr('id', widgetType + '-filter-select');

            // Select Filter Change Event
            widgetZoneFilterSelect.live("change", function(){
                var selectedFilter = $("#"+widgetType+"-filter-select").find(":selected").val();
                widgetDashboardLayout.refreshFilteredWidgetElement(widgetId, widgetZoneId, widgetType, selectedFilter, widgetFilters);
            });

            // Add Select Element to Filter Element
            widgetZoneContainerFilterElement.append(widgetZoneFilterSelect);

            // Create Widget Zone Container Select Option Elements
            $.each(widgetFilters, function(index, filter){
                var filterOption = $(document.createElement("option"));
                filterOption.attr("value", filter.value);
                var filterText = widgetDashboardLayout.toTitleCase(filter.label);
                filterOption.html(filterText);
                widgetZoneFilterSelect.append(filterOption);
            });

            // Create Widget Zone Container Close Element
            var widgetZoneContainerCloseElement = $(document.createElement("div"));
            widgetZoneContainerCloseElement.attr('id', 'zone_close');
            widgetZoneContainerCloseElement.attr('class', 'widget-remove');
            widgetZoneContainerCloseElement.html("Close");

            // Create Widget Zone Container Content Element
            var widgetZoneContainerContentElement = $(document.createElement("div"));
            widgetZoneContainerContentElement.attr('id', 'zone_content');
            widgetZoneContainerContentElement.attr('class', 'widget-content');

            // Compose Widget Zone Container Intro - Add Title, Close and Description Elements
            $(widgetZoneContainerIntroElement).append(widgetZoneContainerTitleElement);
            $(widgetZoneContainerIntroElement).append(widgetZoneContainerCloseElement);
            //$(widgetZoneContainerIntroElement).append(widgetZoneContainerDescriptionElement);
            $(widgetZoneContainerIntroElement).append(widgetZoneContainerFilterElement);

            // Compose Widget Zone Container - Add Intro and Content Elements
            $(widgetZoneContainerElement).append(widgetZoneContainerIntroElement);
            $(widgetZoneContainerElement).append(widgetZoneContainerContentElement);

            // Compose Widget Zone - Add Container Element
            $(widgetZoneElement).append(widgetZoneContainerElement);

            // Return Composed Widget Zone Element
            return widgetZoneElement;

        }

        /** Build Widget Components - Build Basic Widget Element */
        widgetDashboardLayout.createBasicWidgetElement = function(){
            // Create Widget Zone Element
            var widgetZoneElement = $(document.createElement("div"));
            widgetZoneElement.attr('id', 'widget-zone');
            widgetZoneElement.attr('class', 'droppable-zone');
            widgetZoneElement.attr('class', 'ui-droppable');

            // Create Widget Container Element
            var widgetZoneContainerElement = $(document.createElement("div"));
            widgetZoneContainerElement.attr('id', 'zone_container');
            widgetZoneContainerElement.attr('class', 'widget-container');
            widgetZoneContainerElement.css('display','none');

            // Create Widget Zone Container Intro Element
            var widgetZoneContainerIntroElement = $(document.createElement("div"));
            widgetZoneContainerIntroElement.attr('id', 'zone_intro');
            widgetZoneContainerIntroElement.attr('class', 'widget-intro');

            // Create Widget Zone Container Title Element
            var widgetZoneContainerTitleElement = $(document.createElement("div"));
            widgetZoneContainerTitleElement.attr('id', 'zone_title');
            widgetZoneContainerTitleElement.attr('class','widget-title');

            // Create Widget Zone Container Description Element
            var widgetZoneContainerDescriptionElement = $(document.createElement("div"));
            widgetZoneContainerDescriptionElement.attr('id', 'zone_description');
            widgetZoneContainerDescriptionElement.attr('class', 'widget-description');
            widgetZoneContainerDescriptionElement.attr('style', 'padding-bottom:5px;')

            // Create Widget Zone Container Close Element
            var widgetZoneContainerCloseElement = $(document.createElement("div"));
            widgetZoneContainerCloseElement.attr('id', 'zone_close');
            widgetZoneContainerCloseElement.attr('class', 'widget-remove');
            widgetZoneContainerCloseElement.html("Close");

            // Create Widget Zone Container Content Element
            var widgetZoneContainerContentElement = $(document.createElement("div"));
            widgetZoneContainerContentElement.attr('id', 'zone_content');
            widgetZoneContainerContentElement.attr('class', 'widget-content');

            // Compose Widget Zone Container Intro - Add Title, Close and Description Elements
            $(widgetZoneContainerIntroElement).append(widgetZoneContainerTitleElement);
            $(widgetZoneContainerIntroElement).append(widgetZoneContainerCloseElement);
            $(widgetZoneContainerIntroElement).append(widgetZoneContainerDescriptionElement);

            // Compose Widget Zone Container - Add Intro and Content Elements
            $(widgetZoneContainerElement).append(widgetZoneContainerIntroElement);
            $(widgetZoneContainerElement).append(widgetZoneContainerContentElement);

            // Compose Widget Zone - Add Container Element
            $(widgetZoneElement).append(widgetZoneContainerElement);

            // Return Composed Widget Zone Element
            return widgetZoneElement;
        }


        /********************************************************************
         * Widget Dashboard UI Event Methods
         *
         * - snapWidgetOption
         * - removeDashboardWidget
         *------------------------------------------------------------------*/
        /** Widget Dashboard UI Event Methods - Snap Widget Option */
        widgetDashboardLayout.snapWidgetOption = function(ev, ui) {
            if( $(this).attr('id')!= "droppableZone"){

                //Get Details of dragged and dropped
                var widgetId = ui.draggable.attr('id');
                var widgetZoneClass = $(this).attr('id');
                var widgetZoneId = $(this).attr('id');
                var widgetType = "";
                var widgetTitle = "";
                var widgetFilters = new Array();

                // Define Local Array for Currently Selected Widgets
                var currentlySelectedWidgets = widgetDashboardLayout.selectedWidgetArray;
                var currentlyAvailableWidgets = widgetDashboardLayout.availableWidgets;

                // Define
                var widgetAssigned = false;
                var zoneEmpty = true;
                //
                $.each(currentlyAvailableWidgets, function(index, availableWidget){
                    if(availableWidget.id == widgetId){
                        widgetType = availableWidget.type.toLowerCase();
                        widgetTitle = availableWidget.title;
                        if(availableWidget.filters!=null){

                            $.each(availableWidget.filters, function(index, filter){
                                widgetFilters.push(filter);
                            });
                        }
                    }
                });
                //
                $.each(currentlySelectedWidgets, function(index, selectedWidget){
                    if(selectedWidget.widgetZoneId!=widgetZoneId){
                        if(selectedWidget.widgetId != widgetId){
                        }else{
                            widgetAssigned = true;
                        }
                    }else{
                        zoneEmpty = false;
                    }
                });
                //
                if((zoneEmpty==true)&&(widgetAssigned==false)){
                    // Define Dashboard Widget
                    var dashboardWidget = new DashboardWidget(widgetId, widgetType, widgetTitle, widgetZoneId, widgetFilters);
                    widgetDashboardLayout.buildWidget(dashboardWidget);
                    currentlySelectedWidgets.push(dashboardWidget);
                    widgetDashboardLayout.persistDroppedWidget(widgetId, widgetZoneId);
                    widgetDashboardLayout.populateWidgetData(widgetId, widgetZoneId, widgetType, widgetFilters[0]);
                }else{
                    ui.draggable.draggable( 'option', 'revert', true );
                }
                // Assign Currently Selected Widgets to Plugin Array
                widgetDashboardLayout.selectedWidgetArray = currentlySelectedWidgets;

            }
        };
        /** Widget Dashboard UI Event Methods - Remove Dashboard Widget */
        widgetDashboardLayout.removeDashboardWidget = function(widgetId, widgetZone, widgetTitle, widgetType){
            // Define Local Array for Currently Selected Widgets
            var currentlySelectedWidgets = widgetDashboardLayout.selectedWidgetArray;
            // Find Widget Object in Currently Selected Widgets Array
            $.each(currentlySelectedWidgets, function(index, value){
                if(value==undefined){
                }else{
                    if(value.widgetId==widgetId){
                        // Remove identified Widget from Currently Selected Widgets
                        widgetDashboardLayout.selectedWidgetArray.splice(index, 1);
                        // Remove Widget from HTML
                        $('#'+widgetId).remove();
                        // Create Dashboard Widget
                        var dashboardWidget = new DashboardWidget(widgetId, widgetType, widgetTitle, widgetZone);
                        // Create Widget Object
                        var widgetElement = widgetDashboardLayout.createAvailableWidgetElement(dashboardWidget);
                        // Append Element to Available Widgets Zone
                        $('#draggableZone').append(widgetElement);
                        // Format Draggable Elements
                        widgetDashboardLayout.formatDraggablePanels();
                    }
                }
            });
            // Persist Removed Widget
            widgetDashboardLayout.persistRemovedWidget(widgetId, widgetZone);
            // Assign Currently Selected Widgets to Plugin Array
            widgetDashboardLayout.selectedWidgetArray = currentlySelectedWidgets;
        };
        
        widgetDashboardLayout.createShowAllWidget = function(url){
            var showAllRowElement = $(document.createElement("div"));
            showAllRowElement.attr('class', 'widget-show-all-row');
            
            var showAllElement = $(document.createElement("a"));
            showAllElement.attr('href', url);
            showAllElement.attr('class', 'row-field-show-all');
            showAllElement.attr('style', 'text-align:right;font-weight:bold');
            showAllElement.html("Show ALL");
            // Construct Affidavit Widget Row HTML Element
            showAllRowElement.append(showAllElement);
            
            return showAllRowElement;
        }
        


        /********************************************************************
         * Widget Dashboard UI Format Methods
         *
         * - formatDraggablePanels
         * - formatDroppablePanels
         * - matchWidgetZoneContentPositions
         * - resizeWidgetZoneContainer
         *------------------------------------------------------------------*/
        /** Dashboard Layout UI Format Methods - Format Draggable Panels */
        widgetDashboardLayout.formatDraggablePanels = function() {
            // Format Widgets as Draggable
            $( ".draggable-widget" ).draggable({
                revert: 'invalid',
                snapMode: 'outer',
                snapTolerance: 10
            });
            // Sort Available Widgets by Id
            $('div#draggableZone>div').tsort('', {order:'acs', attr:'id'});
        };
        /** Dashboard Layout UI Format Methods - Format Droppable Panels */
        widgetDashboardLayout.formatDroppablePanels = function() {
            // Format Widget Zones as Droppable
            $('.droppable-zone').droppable({
                accept: '.draggable-widget',
                drop: widgetDashboardLayout.snapWidgetOption
            });
        };
        /** Dashboard Resize Methods : Match Widget Zones with their Content */
        widgetDashboardLayout.matchWidgetZoneContentPositions = function(){
            $('#droppableZone').find('.droppable-zone').children().css('top','-1px');
        }
        /** Resize Widget Zone Container */
        widgetDashboardLayout.resizeWidgetZoneContainer = function(){
            var draggableZoneHeight = $('#draggableZone').height();
            var droppableZoneHeight = "";
            if($('#droppableZoneLeft').height() >= $('#droppableZoneRight').height()){
                droppableZoneHeight = $('#droppableZoneLeft').height();
            }else{
                droppableZoneHeight = $('#droppableZoneRight').height();
            }

            if(draggableZoneHeight > droppableZoneHeight){
            	$('#droppableZone').attr('style','height:'+ (draggableZoneHeight+20)  +'px; float:left');
            }else{
                if(droppableZoneHeight > (draggableZoneHeight+20)){
                    $('#droppableZone').removeAttr('style');
                }
            }
            widgetDashboardLayout.matchWidgetZoneContentPositions();
        }


        /********************************************************************
         * Initialize Widget Dashboard Layout
         *------------------------------------------------------------------*/
        WidgetDashboardLayout.init = function(options) {
            // Initialize Variable Values
            $.extend(widgetDashboardLayout.options, defaults, options);
            // Create Widget Dashboard
            widgetDashboardLayout.createWidgetZonePanelElement();
            // Populate Widget Dashboard
            widgetDashboardLayout.retrieveAvailableWidgets();
            widgetDashboardLayout.populateWidgetZonePanel();
            // Format Droppable Elements
            widgetDashboardLayout.formatDroppablePanels();
            // Return Widget Dashboard Layout Plugin
            return WidgetDashboardLayout;
        };


        /********************************************************************
         * Return Widget Dashboard Layout Plugin
         *------------------------------------------------------------------*/
        return WidgetDashboardLayout;
    }
    exports.WidgetDashboardLayout = WidgetDashboardLayout;
}(this, jQuery, this.window);


function ie_ver(){  
    var iev=0;
    var ieold = (/MSIE (\d+\.\d+);/.test(navigator.userAgent));
    var trident = !!navigator.userAgent.match(/Trident\/7.0/);
    var rv=navigator.userAgent.indexOf("rv:11.0");

    if (ieold) iev=new Number(RegExp.$1);
    if (navigator.appVersion.indexOf("MSIE 10") != -1) iev=10;
    if (trident&&rv!=-1) iev=11;

    return iev;         
}


