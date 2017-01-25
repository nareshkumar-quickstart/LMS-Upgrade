//----------------------------------------------------
// Detect Browser and Link the CSS accordingly
//----------------------------------------------------

var browser=navigator.appName;
var b_version=navigator.appVersion;
var version=parseFloat(b_version);

	if(browser == "Netscape"){
		document.write("<link type='text/css' href='brands/default/en/css/css_search_net.css' rel='stylesheet'/>");
	}else{
		document.write("<link type='text/css' href='brands/default/en/css/css_search.css' rel='stylesheet'/>");
	}

//-------------------------------------------------------
// Change security mode
//-------------------------------------------------------

function changeSecurityMode(param){
    if(param == 0){
        window.location.href = "../mycourses/index.jsp";
    }
    if(param == 1){
        window.location.href = "../Survey/manage-users.jsp";
    }
    if(param == 2){
        window.location.href = "../Instructor/index.jsp";
    }
    if(param == 3){
        window.location.href = "../Accreditation/index.jsp";
    }
    if(param == 4){
        window.location.href = "../Commissions/index.jsp";
    }   
}

//----------------------------------------------------
// Check all Checkboxes
//----------------------------------------------------

    function checkAll(field, formObject){
		if(formObject.checked==true){
			for (i = 0; i < field.length; i++)
			field[i].checked = true;
		}else{
			for (i = 0; i < field.length; i++)
			field[i].checked = false;
			}
	}

	function checkAns(count_id,ob){
	     var xx=$('#choices_'+count_id)[0];
         $(xx).find("INPUT[type='checkbox']").attr('checked', $(ob).is(':checked'));
	}
//-------------------------------------------------------
// Change Add Question Pop Layout based on Question Type
//-------------------------------------------------------
function changeQuestionType(questionType){
    if(questionType == "singleresponse"){

        $("#response-single").css("display", "block");
        $("#response-multiple").css("display", "none");
        $("#response-personal-information").css("display", "none");
        $("#question-text-limit").css("display", "none");
        $("#add-remove-responses").css("display", "none");


        if(browser == "Netscape"){
            $("#response-required-single").css("display", "table-row");
        }else{
            $("#response-required-single").css("display", "block");
        }


    }else if(questionType == "textbox"){

        $("#response-single").css("display", "none");
        $("#response-multiple").css("display", "none");
        $("#response-personal-information").css("display", "none");
        $("#add-remove-responses").css("display", "none");

        if(browser == "Netscape"){
            $("#question-text-limit").css("display", "table-row");
            $("#response-required-single").css("display", "table-row");
        }else{
            $("#question-text-limit").css("display", "block");
            $("#response-required-single").css("display", "block");
        }

    }else if(questionType == "fillblank"){

        $("#response-single").css("display", "none");
        $("#response-multiple").css("display", "none");
        $("#response-personal-information").css("display", "none");
        $("#question-text-limit").css("display", "none");
        $("#add-remove-responses").css("display", "none");

        if(browser == "Netscape"){
            $("#response-required-single").css("display", "table-row");
        }else{
            $("#response-required-single").css("display", "block");
        }

    }else if(questionType == "personalinformation"){
        $("#response-single").css("display", "none");
        $("#response-multiple").css("display", "none");
        $("#response-personal-information").css("display", "block");
        $("#response-required-single").css("display", "none");

    }else if(questionType == "multipleresponse"){
        $("#response-single").css("display", "none");
        $("#response-multiple").css("display", "block");
        $("#response-personal-information").css("display", "none");
        $("#question-text-limit").css("display", "none");
        $("#response-required-single").css("display", "none");
        $("#add-remove-responses").css("display", "block");
    }

}

//-------------------------------------------------------
// Change Response Pop Layout based on Reponse Type
//-------------------------------------------------------
function changeResponseType(responseType){
    if(responseType == "singleresponse"){
        if(browser == "Netscape"){
            $("#multiple-answer-response").css("display", "table-row");
        }else{
            $("#multiple-answer-response").css("display", "block");
        }
    }else if(responseType == "fillblank"){
        $("#multiple-answer-response").css("display", "none");
        $("#response-text-limit").css("display", "none");
    }else if(responseType == "textbox"){
        if(browser == "Netscape"){
            $("#response-text-limit").css("display", "table-row");
        }else{
            $("#response-text-limit").css("display", "block");
        }

    }
}




//----------------------------------------------------
// Electronic Signature
//----------------------------------------------------

function electronicSignature(para){
    if($(para).attr("checked") == true){
        $("#electronic-signature-required").css("visibility", "visible")
    }else{
         $("#electronic-signature-required").css("visibility", "hidden")
    }
}

//----------------------------------------------------
// Links
//----------------------------------------------------

function linksReqiured(para){
    if($(para).attr("checked") == true){
        $("#links-required").css("visibility", "visible")
    }else{
        $("#links-required").css("visibility", "hidden")
    }
}

//----------------------------------------------------
// Questions Per Page
//----------------------------------------------------

function questionsPerPage(para){
    if($(para).attr("checked") == true){
        $("#questions-per-page-required").css("visibility", "visible")
    }else{
         $("#questions-per-page-required").css("visibility", "hidden")
    }
}

//----------------------------------------------------
// Questions Per Page
//----------------------------------------------------

function allPerPage(para){
    if($(para).attr("checked") == false){
        $("#questions-per-page-required").css("visibility", "visible")
    }else{
         $("#questions-per-page-required").css("visibility", "hidden")
    }
}

//----------------------------------------------------
// JQUERY
//----------------------------------------------------
$(function(){

         switch($.browser.mozilla){
            case true :
                $("#field_questions").width(65);
                $("#status-survey-search").css("width", "174px");
                $("#retired-survey-search").css("width", "174px");
            break
            case false :
                $("#field_questions").width(56);
                $("#status-survey-search").css("width", "176px");
                $("#retired-survey-search").css("width", "176px");
            break
            }



    function resizeScrollable(){
    $(".scrollable").width($(window).width() - 221);
    $(".scrollable").height($(window).height() - 115 - $(".tab_bar").height());
    $(".survey-preview-scrollable").width($(window).width() - 15);
    $(".survey-preview-scrollable").height($(window).height() -90);
    $("#text-response").width($(window).width() - 83);

        }

    resizeScrollable();

    $(window).resize(function(){
        resizeScrollable();
    });

    $("#search-survey").click(function(){
        $("#searchBox").attr("class","search_box");
		$('#searchBox input[type="text"]:first').focus();
    });

    $("#search-survey-cancel").click(function(){
        $("#searchBox").attr("class","visible");
    });

    $("#search-survey-search").click(function(){
        $("#searchBox").attr("class","visible");
        $("#searchResult_1").css("display", "none");
        $("#searchResult_2").css("display", "block");
        $("#resultDescription").html("Showing 1 - 3 of 3");
    });

    $("#search-survey-search-manage").click(function(){
        $("#searchBox").attr("class","visible");
        $("#searchResult_1").css("display", "none");
        $("#searchResult_2").css("display", "block");
        $("#resultDescription").html("Showing 1 - 5 of 5");
    });

    $("#search-courses").click(function(){
        $("#searchBox").attr("class","search_box");
    });

    $("#search-courses-cancel").click(function(){
        $("#searchBox").attr("class","visible");
    });

    $("#search-courses-search").click(function(){
        $("#searchBox").attr("class","visible");
        $("#searchResult_1").css("display", "none");
        $("#searchResult_2").css("display", "block");
        $("#resultDescription").html("Showing 1 - 2 of 2");
    });

    //----------------------------------------------------
    // Save Survey Summary in Edit Mode
    //----------------------------------------------------
    $("#save-edit-survey").click(function(){
        var conditionone = false;
        var conditiontwo = false;
        $("#errors").html(" ");

        if($("#field_name").attr("value") == ""){
            $("#survey-name-required").html("<img src='images/!mark.gif' style='margin-top:3px; margin-left:-5px; margin-right:5px; float:left;'/>");
            $("#error-message-box").css("display", "block");
            $("#errors").html("<div class='error_message'>Survey name is required</div>");
        }else{
            $("#survey-name-required").html("<img src='images/checkmark.gif' style='margin-top:3px; margin-left:-5px; margin-right:5px; float:left;'/>");
            conditionone = true;
        }

        if($("#questions-per-page").attr("checked") == false){
            $("#questions-per-page-required").html("<div class='required_field' style='margin-left:3px; padding-top:1px;'>*</div>");
            conditiontwo = true;
        }else if($("#questions-per-page").attr("checked") == true && $("#field_questions").attr("value") == ""){
            $("#questions-per-page-required").html("<img src='images/!mark.gif' style='margin-top:4px; margin-right:3px; float:left;'/>");
            $("#error-message-box").css("display", "block");
            var existingCode = $("#errors").html()
            $("#errors").html(existingCode+"<div class='error_message'>Questions per page is required</div>");
        }else if($("#questions-per-page").attr("checked") == true && $("#field_questions").attr("value") != ""){
            $("#questions-per-page-required").html("<img src='images/checkmark.gif' style='margin-top:4px; margin-right:3px; float:left;'/>");
            conditiontwo = true;
        }

        if(conditionone == true && conditiontwo==true){
            window.location = "manage-surveys.jsp";
        }
        
    });
    
    //----------------------------------------------------
    // Add Survey Summary
    //----------------------------------------------------
    $("#add-survey-summary-next").click(function(){
        var conditionone = false;
        var conditiontwo = false;
        $("#errors").html(" ");

        if($("#field_name").attr("value") == ""){
            $("#survey-name-required").html("<img src='images/!mark.gif' style='margin-top:3px; margin-left:-5px; margin-right:5px; float:left;'/>");
            $("#error-message-box").css("display", "block");
            $("#errors").html("<div class='error_message'>Survey name is required</div>");
        }else{
            $("#survey-name-required").html("<img src='images/checkmark.gif' style='margin-top:3px; margin-left:-5px; margin-right:5px; float:left;'/>");
            conditionone = true;
        }

        if($("#questions-per-page").attr("checked") == false){
            $("#questions-per-page-required").html("<div class='required_field' style='margin-left:3px; padding-top:1px;'>*</div>");
            conditiontwo = true;
        }else if($("#questions-per-page").attr("checked") == true && $("#field_questions").attr("value") == ""){
            $("#questions-per-page-required").html("<img src='images/!mark.gif' style='margin-top:4px; margin-right:3px; float:left;'/>");
            $("#error-message-box").css("display", "block");
            var existingCode = $("#errors").html()
            $("#errors").html(existingCode+"<div class='error_message'>Questions per page is required</div>");
        }else if($("#questions-per-page").attr("checked") == true && $("#field_questions").attr("value") != ""){
            $("#questions-per-page-required").html("<img src='images/checkmark.gif' style='margin-top:4px; margin-right:3px; float:left;'/>");
            conditiontwo = true;
        }

        if(conditionone == true && conditiontwo==true){
            //window.location = "add-survey-question.jsp";
            window.location = "manage-surveys.jsp";
        }
        
    });

    //----------------------------------------------------
    // Add Survey Question
    //----------------------------------------------------
    $("#add-survey-question-next").click(function(){
        $("#errors").html(" ");

        if($("#searchResult_2").css("display") == "none"){
            $("#error-message-box").css("display", "block");
            $("#errors").html("<div class='error_message'>At least one question is reauired</div>");
        }else{
            window.location = "add-survey-course.jsp";
        }
    });

    $("#add-question").click(function(){
        window.open('question-popup.jsp','','status=yes,scrollbars=yes,width=500,height=450');
        $("#searchResult_1").css("display", "none")
        $("#searchResult_2").css("display", "block")
    });

    //----------------------------------------------------
    // Save Question
    //----------------------------------------------------
    $("#add-question-popup-save").click(function(){
        $("#errors").html(" ");

         if($(".answer-area").css("display") == "none"){
            if($("#questionArea").val() == ""){
                     $("#error-message-box").css("display", "block");
                     $("#errors").html("<div class='error_message'>Question is reauired</div>");
            }else{
                window.close();
            }
         }else{
            if($("#questionArea").val() == "" && $("#answerArea").val() == ""){
                     $("#error-message-box").css("display", "block");
                     $("#errors").html("<div class='error_message'>Question is reauired</div><div class='error_message'>Answer choices are reauired</div>");
            }else if($("#questionArea").val() == "" && $("#answerArea").val() != ""){
                     $("#error-message-box").css("display", "block");
                     $("#errors").html("<div class='error_message'>Question is reauired</div>");
            }else if($("#answerArea").val() == "" && $("#questionArea").val() != ""){
                     $("#error-message-box").css("display", "block");
                     $("#errors").html("<div class='error_message'>Answer choices are reauired</div>");
            }else{
                window.close();
            }
         }
    });

    //----------------------------------------------------
    // Edit Survey Question
    //----------------------------------------------------
    $(".edit-question").click(function(){
        window.open('question-edit-popup.jsp','','status=yes,scrollbars=yes,width=500,height=450');
    });

    //----------------------------------------------------
    // Close Error Message Box
    //----------------------------------------------------

   $("#close-errors").click(function(){
        $("#error-message-box").css("display", "none");
        $("#errors").html(" ");
   });

    //----------------------------------------------------
    // Assign Surveys
    //----------------------------------------------------

   $("#assign-survey-groups").click(function(){
       if($("#learners").attr("checked") == true){
            window.location = "assign-surveys-learners.jsp";
       }else if($("#organizationGroups").attr("checked") == true){
            window.location = "assign-surveys-organization-groups.jsp";
       }else if($("#learnerGroups").attr("checked") == true){
            window.location = "assign-surveys-learner-groups.jsp";
       }
   });

    //----------------------------------------------------
    // Collapse/Expand Organization Group
    //----------------------------------------------------

     $('#colOne').click(function(){
         if($('#colOne').attr("src") == "images/icon_expand.gif"){
             $('#colOne').attr("src", "images/icon_collapse.gif")

             if(browser == "Netscape"){
                 $('.schedule-1').css('display', 'table-row');
                 $('.schedule-2').css('display', 'table-row');
             }else{
                 $('.schedule-1').css('display', 'block');
                 $('.schedule-2').css('display', 'block');
             }
             
         }else{
             $('#colOne').attr("src", "images/icon_expand.gif")
             $('.schedule-1').css('display', 'none');
             $('.schedule-2').css('display', 'none');
         }
     });

     $('#colTwo').click(function(){
         if($('#colTwo').attr("src") == "images/icon_expand.gif"){
             $('#colTwo').attr("src", "images/icon_collapse.gif")

             if(browser == "Netscape"){
                 $('.schedule-2').css('display', 'table-row');
             }else{
                 $('.schedule-2').css('display', 'block');
             }
             
         }else{
             $('#colTwo').attr("src", "images/icon_expand.gif")
             $('.schedule-2').css('display', 'none');
         }
     });

    //----------------------------------------------------
    // Reorder Survey Questions
    //----------------------------------------------------
    //----------------------------------------------------
    //Question One Move Down
    $("#q1md").click(function(){
        currentQuestion = $("#question-1").html();
        newQuestion = $("#question-2").html();
        $("#question-1").html(newQuestion);
        $("#question-2").html(currentQuestion);
    });
    //Question One Move Bottom
    $("#q1mb").click(function(){
        currentQuestion = $("#question-1").html();
        newQuestion = $("#question-2").html();
        $("#question-1").html(newQuestion);
        $("#question-2").html($("#question-3").html());
        $("#question-3").html(currentQuestion);
    });
    //Question Top Move Top
    $("#q2mt").click(function(){
        currentQuestion = $("#question-2").html();
        newQuestion = $("#question-1").html();
        $("#question-2").html(newQuestion);
        $("#question-1").html(currentQuestion);
    });
    //Question Two Move Up
    $("#q2mu").click(function(){
        currentQuestion = $("#question-2").html();
        newQuestion = $("#question-1").html();
        $("#question-2").html(newQuestion);
        $("#question-1").html(currentQuestion);
    });
    //Question Two Move Down
    $("#q2md").click(function(){
        currentQuestion = $("#question-2").html();
        newQuestion = $("#question-3").html();
        $("#question-2").html(newQuestion);
        $("#question-3").html(currentQuestion);
    });
    //Question Two Move Bottom
    $("#q2mb").click(function(){
        currentQuestion = $("#question-2").html();
        newQuestion = $("#question-3").html();
        $("#question-2").html(newQuestion);
        $("#question-3").html(currentQuestion);
    });
    //Question Three Move Up
    $("#q3mu").click(function(){
        currentQuestion = $("#question-3").html();
        newQuestion = $("#question-2").html();
        $("#question-3").html(newQuestion);
        $("#question-2").html(currentQuestion);
    });
    //Question three Move Top
    $("#q3mt").click(function(){
        currentQuestion = $("#question-3").html();
        newQuestion = $("#question-2").html();
        $("#question-2").html($("#question-1").html());
        $("#question-1").html(currentQuestion);
        $("#question-3").html(newQuestion);
    });

    //----------------------------------------------------
    // Collapse/Expand Questions
    //----------------------------------------------------

     $('.icon-expand').click(function(){
		 var countVal=$(this).attr("countVal");
	     var xx=$('#choices_'+countVal)[0];
         if($(this).attr("src") == "brands/default/en/images/icon_expand.gif"){
             $(this).attr("src", "brands/default/en/images/icon_collapse.gif");
             if(browser == "Netscape"){
                 $(xx).css('display', 'table-row');
             }else{
                 $(xx).css('display', 'block');
             }

         }else{
             $(this).attr("src", "brands/default/en/images/icon_expand.gif")
             $(xx).css('display', 'none');
         }
     });

    //----------------------------------------------------
    // Collapse/Expand Question Feedback
    //----------------------------------------------------

    function surveyNotes(idN){
      $('#note-icon-'+idN).click(function(){
             if($('#feedback-'+idN).css("display") != "none"){
                $('#feedback-'+idN).css('display', 'none');
             }else{
                 if(browser == "Netscape"){
                     $('#feedback-'+idN).css('display', 'table-row');
                 }else{
                     $('#feedback-'+idN).css('display', 'block');
                 }
             }
         });

    }

    surveyNotes(1);
    surveyNotes(2);
    surveyNotes(3);
    surveyNotes(4);
    surveyNotes(5);
    surveyNotes(6);
    surveyNotes(7);

    //----------------------------------------------------
    // Add/Remove Question Response
    //----------------------------------------------------
        $('#add-response').click(function(){
            $("#second-response").css("display", "block");
        });

        $('#remove-response').click(function(){
            $("#second-response").css("display", "none");
        });

});