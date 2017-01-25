$(function(){

    var offset = 5;

    
    
        //Help
        $('#icon-login-help').stop().hover(function(evt){
         $('#login-tooltip').css('top',evt.pageY+offset-70).css('left', evt.pageX+offset).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#login-tooltip').hide();
        });

        $('#icon-login-help').mousemove(function(evt){
            $('#login-tooltip').css('top',evt.pageY + offset -70).css('left',evt.pageX + offset);
        });

        //Password Info
        $('#icon-password-info').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Password must be atleast 8 characters long and can include alpha-numeric and special characters.</div>').css('top',evt.pageY+offset-70).css('left', evt.pageX+offset).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#icon-password-info').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset -70).css('left',evt.pageX + offset);
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // LEARNER MODE
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Learner - My Courses
        $('#learner-my-courses').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View the list of your available courses</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#learner-my-courses').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Learner - My Transcripts
        $('#learner-my-transcripts').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View your performance statistics by course</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#learner-my-transcripts').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Learner - My Profile
        $('#learner-my-profile').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View/Edit your profile; Set Preferences and Alerts</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
        
        //Learner - My Rewards
        $('#learner-my-rewards').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View your gift box; Set Preferences and Alerts</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#learner-my-rewards').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Learner - My Recommendations
        $('#learner-my-recommendations').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View your recommendations</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#learner-my-recommendations').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });
        
        $('#learner-my-profile').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // MANAGER MODE
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        $('#manager-ls360-dashboard').stop().hover(function(evt){
        	$('<div id="tooltip" class="tooltip">Go back to SF Dashboard</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
        $('#manager-ls360-dashboard').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        }, function(){
            $('#tooltip').remove();
        });
        //Manager - Users & Groups
        $('#manager-users-groups').stop().hover(function(evt){
        	$('<div id="tooltip" class="tooltip">Add/Delete/Edit Users and Groups; Set Security levels</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#manager-users-groups').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        }, function(){
            $('#tooltip').remove();
        });

        //Manager - Plan & Enroll
        $('#manager-plan-enroll').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Enroll users, Set/Assign Training Plans, View/Manage Content</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#manager-plan-enroll').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Manager - Reports
        $('#manager-reports').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Run/View a wide range of reports</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#manager-reports').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Manager - Tools
        $('#manager-tools').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Set up Alerts, Announcements and Surveys</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#manager-tools').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Manager - External Services
        $('#manager-external-services').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Link to integrated 3rd party software</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#manager-external-services').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Manager - Profile
        $('#manager-profile').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View/Edit Customer Profile; Set Preferences</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#manager-profile').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // INSTRUCTOR MODE
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Instructor - Dashboard
        $('#instructor-dashboard').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View my customized dashboard</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#instructor-dashboard').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Instructor - Search
        $('#instructor-search').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Search for Courses or Users</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#instructor-search').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Instructor - Courses
        $('#instructor-courses').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Manage Courses and Course Groups</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#instructor-courses').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Instructor - Grades/Resources
        $('#instructor-grades-resources').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Manage Grade Books, Locations and Instructional Resources</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#instructor-grades-resources').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Instructor - Reports
        $('#instructor-reports').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Run/View a wide range of reports</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });

        $('#instructor-reports').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ADMINISTRATOR MODE
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

        //Administrator - Search
        $('#administrator-search').stop().hover(function(evt){
            $('<div id="adminsearch-tooltip" class="tooltip">Search for Users, Customers, Resellers </div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#adminsearch-tooltip').remove();
        });

        $('#administrator-search').mousemove(function(evt){
            $('#adminsearch-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Administrator - Learners
        $('#administrator-learners').stop().hover(function(evt){
            $('<div id="adminlearner-tooltip" class="tooltip">Find Users to Change Enrollments (Extend, Drop, Swap, Unlock, Expire)</div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#adminlearner-tooltip').remove();
        });

        $('#administrator-learners').mousemove(function(evt){
            $('#adminlearner-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Administrator - Reports
        $('#administrator-reports').stop().hover(function(evt){
            $('<div id="adminreports-tooltip" class="tooltip">Run/View a wide range of reports</div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#adminreports-tooltip').remove();
        });

        $('#administrator-reports').mousemove(function(evt){
            $('#adminreports-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Administrator - Tools
        $('#administrator-tools').stop().hover(function(evt){
            $('<div id="admintools-tooltip" class="tooltip">Set up Alerts, Announcements and Surveys</div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#admintools-tooltip').remove();
        });

        $('#administrator-tools').mousemove(function(evt){
            $('#admintools-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });

        //Administrator - Security
        $('#administrator-security').stop().hover(function(evt){
            $('<div id="adminsecurity-tooltip" class="tooltip">Manage Security roles at the Customer/Reseller level</div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').
                width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#adminsecurity-tooltip').remove();
        });

        $('#administrator-security').mousemove(function(evt){
            $('#adminsecurity-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 240);
        });

        //Administrator - Customers
        $('#administrator-customers').stop().hover(function(evt){
            $('<div id="admincustomer-tooltip" class="tooltip">Add/Edit Customers and Libraries</div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#admincustomer-tooltip').remove();
        });

        $('#administrator-customers').mousemove(function(evt){
            $('#admincustomer-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 240);
        });

        //Administrator - Resellers
        $('#administrator-resellers').stop().hover(function(evt){
            $('<div id="adminreseller-tooltip" class="tooltip">Add/Edit Resellers, Preferences, Permissions and Libraries</div>').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').
                width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#adminreseller-tooltip').remove();
        });

        $('#administrator-resellers').mousemove(function(evt){
            $('#adminreseller-tooltip').css('top',evt.pageY + offset + 20).css('left',evt.pageX + offset - $('#tooltip').width() - 240);
        });
        
        
        //Administrator - Branding
        $('.icon-revert').stop().hover(
            function(evt){
                $('<div id="tooltip" class="tooltip">Revert to Default</div>').
                    css('top',evt.pageY-50).
                    css('left', evt.pageX+10).
                    css('width', 95).
                    appendTo('body').
                    hide().
                    fadeIn(200);
                }, function(){
                    $('#tooltip').remove();
            });

        $('.icon-revert').mousemove(function(evt){
            $('#tooltip').css('top',evt.pageY + offset-50).css('left',evt.pageX + offset+10);
        }); 
        
        //Login Page
        $('#login-icon-help').stop().hover(function(evt){
        	$('#login-tooltip').
                css('top',evt.pageY+offset+20).
                css('left', evt.pageX-offset+$('#tooltip').
                width() - 40).
                appendTo('body').
                hide().
                fadeIn(200);
        }, function(){
            $('#login-tooltip').hide();
        });

        $('#login-icon-help').mousemove(function(evt){
            $('#login-tooltip').
                css('top',evt.pageY + offset + 20).
                css('left',evt.pageX + offset - $('#tooltip').width() - 40);
        });
        
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
        // ACCREDITATION MODE
        //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        $('#accreditation-search').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">Search for regulators, credentials, approvals, instructors using the search tool</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
        
        
        $('#accreditation-regulators').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View the list of your available Regulators</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
        
        $('#accreditation-approvals').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View the list of your available Approvals</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
        
        $('#accreditation-reports').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View the list of your available reports</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
        
        $('#accreditation-customfields').stop().hover(function(evt){
            $('<div id="tooltip" class="tooltip">View the list of your available custom fields</div>').css('top',evt.pageY+offset+20).css('left', evt.pageX-offset+$('#tooltip').width() - 40).appendTo('body').hide().fadeIn(200);
        }, function(){
            $('#tooltip').remove();
        });
                
        /*
        
     	// PROTOTYPE: Define Tooltips for User Modes 
        var learnerModeTooltips = ["learner-my-courses", "learner-my-transcripts", "learner-my-profile"];
        var managerModeTooltips = [];
        var adminModeTooltips = [];
        
        // PROTOTYPE: Show Tooltip 
        function showTooltip(elementId){
        	$('<div id="tooltip" class="tooltip"><b>Need Help?</b> - Click on the "Contact Us" link at the bottom of the page</div>').css('top',evt.pageY+offset-70).css('left', evt.pageX+offset).appendTo('body').hide().fadeIn(200);
        }

        // PROTOTYPE: Remove Tooltip 
        function removeTooltip(){
        	$('#tooltip').remove();
        }
        
        // PROTOTYPE: Prepare Tooltip
        function prepareTooltip(elementArray){
        	jQuery.each(elementArray, function(index, elementId){
        		$('#'+elementId).stop().hover(function(evt){
                   showTooltip(elementId); 
                }, function(){
                    removeTooltip();
                });

                $('#'+elementId).mousemove(function(evt){
                    $('#tooltip').css('top',evt.pageY + offset -70).css('left',evt.pageX + offset);
                });
        	});
        }
        
        // PROTOTYPE: Prepare Tooltips for User Modes
        prepareTooltip(learnerModeTooltips);
        prepareTooltip(managerModeTooltips);
        prepareTooltip(adminModeTooltips);

		*/
});
