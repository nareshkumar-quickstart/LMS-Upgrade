var browser=navigator.appName;

function gotoPage(param){
    if(param == 0 || param == 1){
        window.location.href = "index.jsp";
    }
    if(param == 2){
        window.location.href = "course-catalog2.jsp";
    }
    if(param == 3){
       window.location.href = "completed-courses.jsp";
    }
    if(param == 4){
       window.location.href = "expired-courses.jsp";
    }
    if(param == 5){
       window.location.href = "about-to-expire.jsp";
    }


}
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
        window.location.href = "../Expert/index.jsp";
    }
    if(param == 4){
        window.location.href = "../Accreditation/index.jsp";
    }
    if(param == 5){
        window.location.href = "../Contracts/search.jsp";
    }
}
function selectLearnerOrGroups(){
    var learners = document.getElementById('learners');
    var organizationGroups = document.getElementById('organizationGroups');
    var learnerGroups = document.getElementById('learnerGroups');
    var surveyResponses = document.getElementById('surveyResponses');

    if(learners.checked == true){
        window.location.href = "enroll-learners-select-learners.jsp";
    }else if(organizationGroups.checked == true){
        window.location.href = "enroll-learners-select-organization-groups.jsp";
    }else if(learnerGroups.checked == true){
        window.location.href = "enroll-learners-select-learner-groups.jsp";
    }else if(surveyResponses.checked == true){
        window.location.href = "enroll-learners-select-surveys.jsp";
    }

}
function showSearchBox(){
    var searchBox = document.getElementById('searchBox');
    if(searchBox.className == "visible"){
        searchBox.className = "search_box";
    }else{
        searchBox.className = "visible";
    }
}
function showResult(){
    var searchResult1 = document.getElementById('search-result-1');
    var searchResult2 = document.getElementById('search-result-2');
    var searchBox = document.getElementById('searchBox');
    var tablePaginationText = document.getElementById('table-pagination-text');
    searchBox.className = "visible";
    searchResult1.className = "visible";
    searchResult2.className = "null";
    tablePaginationText.innerHTML= "Showing 1 - 3 of 10";
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

function recurrencePattern(recPat){

    var daily = document.getElementById("daily");
    var weekly = document.getElementById("weekly");
    var monthly = document.getElementById("monthly");
    var yearly = document.getElementById("yearly");
    var range = document.getElementById("range");


    daily.style.display = "none";
    weekly.style.display = "none";
    monthly.style.display = "none";
    yearly.style.display = "none";

    switch (recPat){
        case "daily":
             daily.style.display = "block";
             range.style.display = "block";
             weekly.style.display = "none";
             monthly.style.display = "none";
             yearly.style.display = "none";
             break;

        case "weekly":
             weekly.style.display = "block";
             range.style.display = "block";
             daily.style.display = "none";
             monthly.style.display = "none";
             yearly.style.display = "none";
             break;

        case "monthly":
             monthly.style.display = "block";
             range.style.display = "block";
             daily.style.display = "none";
             weekly.style.display = "none";
             yearly.style.display = "none";
             break;

        case "yearly":
             yearly.style.display = "block";
             range.style.display = "block";
             daily.style.display = "none";
             weekly.style.display = "none";
             monthly.style.display = "none";
             break;

        case "once":
              range.style.display = "none";
              daily.style.display = "none";
              weekly.style.display = "none";
              monthly.style.display = "none";
              yearly.style.display = "none"
              break;
    }

}


//jQuery Functions
$(function(){
    switch($.browser.mozilla){
        case true :
            $("#sbc2").css("margin-left", "-20px");
            $(".survey-list-checkbox").css("margin-top", "5px");
            break
        case false :
            $("#btn-browse").css("z-index", "-2");
            $(".dont-show-message").css("margin-top", "13px");
            break
    }

    $(".header-controls").css("width", "17px");



    $('.bar-shoppingcart-thin').css('width', $(window).width() - 253 - 31);

    $('.scrollable').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - $('.tab_bar').height() - 115 + 'px')
    $('.scrollable_1').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - $('.tab_bar').height() - 115 + 'px')
    $('.scrollable_2').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - $('.tab_bar').height() - 115 + 'px')
    $('.scrollable_3').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - $('.tab_bar').height() - 115 + 'px')
    $('.scrollable_4').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - $('.tab_bar').height() - 115 + 'px')
    $("#transcriptBox").height($(window).height() - $("#frame_header").height() - $(".tab_bar").height() - $("#frame_footer").height() - $("#pageDescription").height() - 76);

    if($('#sideBar').css('width') != undefined){
        
        $('.scrollable').css('width', $(window).width() - 436 + 'px');
        $('.scrollable_1').css('width', $(window).width() - 432 + 'px');
        $('.scrollable_2').css('width', $(window).width() - 222 + 'px');
        $('.scrollable_3').css('width', $(window).width() - 218 + 'px');
        $('.scrollable_4').css('width', $(window).width() - 222 + 'px');
    }else{
    //
    }


    $("#login-table").css("margin-left", $(window).width()/2 -  $("#login-table").width()/2 );


    $('#table-frame').css('width', $('.scrollable').width() - 40 + 'px');
    $('#message-table').css('width', $('.scrollable').width() - 35)

    $("#alert-box").css("left", $(window).width()/2 - $("#alert-box").width()/2);
    $("#alert-box").css("top", $(window).height()/2 - $("#alert-box").height()/2);

    $("#legend-box").css("left", $(window).width()/2 - $("#legend-box").width()/2);
    $("#legend-box").css("top", $(window).height()/2 - $("#legend-box").height()/2);

    $(window).resize(function(){
        $('.scrollable').css('height',    $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
        $('.scrollable_1').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
        $('.scrollable_2').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
        $('.scrollable_3').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
        $('.scrollable_4').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')

        $("#login-table").css("margin-left", $(window).width()/2 -  $("#login-table").width()/2 );

        if( $(window).width() > 400 && $('#sideBar').css('width') != undefined){
            $('.scrollable').css('width', $(window).width() - 422 + 'px');
            $('.scrollable_1').css('width', $(window).width() - 432 + 'px');
            $('.scrollable_2').css('width', $(window).width() - 222 + 'px');
            $('.scrollable_3').css('width', $(window).width() - 218 + 'px');
            $('.scrollable_4').css('width', $(window).width() - 222 + 'px');
        }
        $('#table-frame').css('width', $('.scrollable').width() - 40 + 'px');
        $('#message-table').css('width', $('.scrollable').width() - 35)

    $("#alert-box").css("left", $(window).width()/2 - $("#alert-box").width()/2);
    $("#alert-box").css("top", $(window).height()/2 - $("#alert-box").height()/2);

    $("#legend-box").css("left", $(window).width()/2 - $("#legend-box").width()/2);
    $("#legend-box").css("top", $(window).height()/2 - $("#legend-box").height()/2);

    });

    $('#list-order').click(function(){
        if($('#list-order').attr('class') == 'icon-asc'){
            $('#list-order').attr('class', 'icon-dsc');
        }else{
            $('#list-order').attr('class', 'icon-asc');
        }
    });

    $('#colOne').click(function(){
        if($('#colOne').attr("src") == "images/icon_expand.gif"){
            $('#colOne').attr("src", "images/icon_collapse.gif")
            $('.schedule-1').css('display', 'block');
            $('.schedule-1').css('display', 'table-row');
        }else{
            $('#colOne').attr("src", "images/icon_expand.gif")
            $('.schedule-1').css('display', 'none');
        }
    });

    $('#colTwo').click(function(){
        if($('#colTwo').attr("src") == "images/icon_expand.gif"){
            $('#colTwo').attr("src", "images/icon_collapse.gif")
            $('.schedule-2').css('display', 'block');
            $('.schedule-2').css('display', 'table-row');
        }else{
            $('#colTwo').attr("src", "images/icon_expand.gif")
            $('.schedule-2').css('display', 'none');
        }
    });

    $('#colThree').click(function(){
        if($('#colThree').attr("src") == "images/icon_expand.gif"){
            $('#colThree').attr("src", "images/icon_collapse.gif")
            $('.schedule-3').css('display', 'block');
            $('.schedule-3').css('display', 'table-row');
        }else{
            $('#colThree').attr("src", "images/icon_expand.gif")
            $('.schedule-3').css('display', 'none');
        }
    });

    $('#colFour').click(function(){
        if($('#colFour').attr("src") == "images/icon_expand.gif"){
            $('#colFour').attr("src", "images/icon_collapse.gif")
            $('.schedule-4').css('display', 'block');
            $('.schedule-4').css('display', 'table-row');
        }else{
            $('#colFour').attr("src", "images/icon_expand.gif")
            $('.schedule-4').css('display', 'none');
        }
    });

    $('#colFive').click(function(){
        if($('#colFive').attr("src") == "images/icon_expand.gif"){
            $('#colFive').attr("src", "images/icon_collapse.gif")
            $('.schedule-5').css('display', 'block');
            $('.schedule-5').css('display', 'table-row');
        }else{
            $('#colFive').attr("src", "images/icon_expand.gif")
            $('.schedule-5').css('display', 'none');
        }
    });

    $('#colSix').click(function(){
        if($('#colSix').attr("src") == "images/icon_expand.gif"){
            $('#colSix').attr("src", "images/icon_collapse.gif")
            $('.schedule-6').css('display', 'block');
            $('.schedule-6').css('display', 'table-row');
        }else{
            $('#colSix').attr("src", "images/icon_expand.gif")
            $('.schedule-6').css('display', 'none');
        }
    });


    function collapse(header, content){
        $("#"+header).click(function(){
            if( $("."+content).css("display") == "none"){
                $("."+content).css("display", "table-row");
                $("#"+header).find(".icon-right").attr("class", "icon-down")
            }else{
                $("."+content).css("display", "none");
                $("#"+header).find(".icon-down").attr("class", "icon-right")
            }

        });
    }

    //collapse("library1", "library-content-1")
    //collapse("library2", "library-content-2")
    //collapse("library3", "library-content-3")
    //collapse("library4", "library-content-4")


    // $(".subvertical-table-1").css("display", "none")
    // $(".subvertical-table-2").css("display", "none")
    // $(".subvertical-table-3").css("display", "none")
    // $(".subvertical-table-4").css("display", "none")

    $("#ahp").click(function(){
        $(".level1").animate({
            "width":"0"
        }, function(){
            $(".subvertical-table-1").css("display", "none")
            $(".subvertical-table-2").css("display", "table")
            $(".subvertical-table-3").css("display", "none")
            $(".subvertical-table-4").css("display", "none")
        })

        $(".level3").animate({
            "width":"0"
        })

    });

    $("#dhp").click(function(){
        $('.level3').find('li').attr("class", "")
        $(this).attr("class", "selected-category")
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "table")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
    });

    $("#dlp").click(function(){
        $('.level3').find('li').attr("class", "")
        $(this).attr("class", "selected-category")
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "table")
        $(".subvertical-table-4").css("display", "none")
    });

    $(".b1").click(function(){
        $('.level4').find('li').attr("class", "")
        $(".level1").animate({
            "width":"196px"
        }, function(){
            $(".subvertical-table-1").css("display", "table")
            $(".subvertical-table-2").css("display", "none")
            $(".subvertical-table-3").css("display", "none")
            $(".subvertical-table-4").css("display", "none")
        })

        $(".level3").animate({
            "width":"196px"
        })

    });

    $('#mb').click(function(){
        $('#mbc').attr("class", "")
        $(this).attr("class", "selected-category")
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "table")
        $(".subvertical-table-4").css("display", "none")

    })

    $('#mbc').click(function(){
        $('#mb').attr("class", "")
        $(this).attr("class", "selected-category")
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "table")
    })

    $('#l1ahp').click(function(){
        $('#mb').attr("class", "")
        $('#mbc').attr("class", "")
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "table")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
    })

    $('#l1dhp').click(function(){
        $('#dhp').attr("class", "")
        $('#dlp').attr("class", "")
        $(".subvertical-table-1").css("display", "table")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
    });


    $("#recurring-date").click(function(){
        var pattern = document.getElementsByName("pattern");

        if($("#recurring-date").attr("checked") == true){
            $("#recurrence-schedule").css("display", "block")
            $("#daily").css("display", "block")
            $("#range").css("display", "block")
            pattern[0].checked = true;
        }else{
            $("#recurrence-schedule").css("display", "none")
            $("#daily").css("display", "none")
            $("#weekly").css("display", "none")
            $("#monthly").css("display", "none")
            $("#yearly").css("display", "none")
            $("#range").css("display", "none")
        }
    });

    $(".alert-name").click(function(){
        $("#alert-box").css("display", "block")
    });

    $("#alert-box").find(".close").click(function(){
        $("#alert-box").css("display", "none")
    });


    $("#course-list-alt .icon").click(function(){
        $("#alert-box").css("display", "block")
    });

    var getFlashMovieObjectFunction = "function getFlashMovieObject(movieName){" +
        "if($.browser.msie) { return (parseInt($.browser.version, 10) == 10) ? document[movieName] : window[movieName];}" +
        "else {return document.embeds[movieName];}" +
        //"return $('embed[name=\"' + movieName + '\"]')[0] || $('object[id=\"' + movieName + '\"]')[0];" +
        "}";

    $("#learner-mode-video").click(function(){
    	$("#mode-heading").html("Learner Mode");    	
    	//var videourl=$("#guidedTourLearnerVideo").val();
    	var videourl="LMS_LearnerMode_4_3_v01.flv";
    	var d = new Date();  
    	var VideoPlayerName= 'videoplayer'+d.getTime();

    	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"' >  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
    	//alert(videourl);
    	//$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('flv','LMS_LearnerMode_4_3_v01.flv','rtmp://flash-1.360training.com/fse/','rtmp'); alert('LMS_LearnerMode_4_3_v01.flv');} ShowVideo();</script>");
    	//$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('flv','LMS_LearnerMode_4_3_v01.flv','rtmp://flash-1.360training.com/fse/','rtmp'); } ShowVideo();</script>");
    	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','http://streaming.360training.com/vod/fse/LMS_LearnerMode_white_v01.mp4'); } ShowVideo();</script>");
    	$("#fadeout-container").css("display", "block");
        $("#alert-box").css("display", "block");        
    });

    $("#manager-mode-video").click(function(){
        $("#mode-heading").html("Manager Mode");//LMS_Manager_Mode_v06.flv
        var videourl=$("#guidedTourManagerVideo").val();
        var d = new Date();  
    	var VideoPlayerName= 'videoplayer'+d.getTime();
    	var videourl="LMS_Manager_Mode_v06.flv";
    	//alert(videourl);
    	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
    	//$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('flv','"+videourl+"','rtmp://flash-1.360training.com/fse/','rtmp');} ShowVideo();</script>");
    	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','http://streaming.360training.com/vod/fse/LMS_Manager_Mode_v03_white.mp4');} ShowVideo();</script>");
        $("#fadeout-container").css("display", "block");
        $("#alert-box").css("display", "block");
    });

    $("#instructor-mode-video").click(function(){
        $("#mode-heading").html("Instructor Mode");//LMS_ComingSoon_v02.flv
        //var videourl=$("#guidedTourInstructorVideo").val();
        var videourl="LMS_ComingSoon_v02.flv";
        var d = new Date();  
    	var VideoPlayerName= 'videoplayer'+d.getTime();
    	//alert(videourl);
    	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
    	//$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('flv','"+videourl+"','rtmp://flash-1.360training.com/fse/','rtmp');} ShowVideo();</script>");
    	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','http://streaming.360training.com/vod/fse/Coming_soon.mp4');} ShowVideo();</script>");
    	$("#fadeout-container").css("display", "block");
        $("#alert-box").css("display", "block");
    });

    $("#expert-mode-video").click(function(){
        $("#mode-heading").html("Expert Mode");
        var videourl=$("#guidedTourExpertVideo").val();
        var d = new Date();  
    	var VideoPlayerName= 'videoplayer'+d.getTime();
    	//alert(videourl);
    	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
    	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','http://streaming.360training.com/vod/fse/Coming_soon.mp4');} ShowVideo();</script>");
        $("#fadeout-container").css("display", "block");
        $("#alert-box").css("display", "block");
    });

    $("#accreditation-mode-video").click(function(){
        $("#mode-heading").html("Accreditation Mode");//LMS_ComingSoon_v02.flv
        var videourl=$("#guidedTourAccreditationVideo").val();
        var d = new Date();  
    	var VideoPlayerName= 'videoplayer'+d.getTime();
    	var videourl="LMS_ComingSoon_v02.flv";
    	//alert(videourl);
    	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
    	//$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('flv','"+videourl+"','rtmp://flash-1.360training.com/fse/','rtmp');} ShowVideo();</script>");
    	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','http://streaming.360training.com/vod/fse/Coming_soon.mp4');} ShowVideo();</script>");
    	$("#fadeout-container").css("display", "block");
        $("#alert-box").css("display", "block");
    });

    $("#administrator-mode-video").click(function(){
        $("#mode-heading").html("Administrator Mode");        
        var videourl=$("#guidedTourAdministratorVideo").val();
        var d = new Date();  
    	var VideoPlayerName= 'videoplayer'+d.getTime();
    	//alert(videourl);
    	//$("#flashContent").html("<script type='text/javascript'> var myVideoName= '"+VideoPlayerName+"'; var PARAMS_INITIALIZED = false; var videoUrlParams =''; var paramsArray = new Array(); var volumeLevel = -1; var server= ''; var app= ''; var filename= '';  function initializeParamters() { videoUrlParams ='"+videourl+"';  paramsArray = new Array(); paramsArray = videoUrlParams.split('&');   for (i=0; i<paramsArray.length; i++) {    var subArray = paramsArray[i].split('=');   if(subArray.length==2) { switch(subArray[0]) { case 'volumelevel': {volumeLevel =subArray[1]; break;}    case 'server': {server=subArray[1]; break;}    case 'app': {app=subArray[1]; break;}   case 'filename': {filename=subArray[1]; break;}  }  } }  PARAMS_INITIALIZED = true; } "+ getFlashMovieObjectFunction +" function playerLoaded(){ if(!PARAMS_INITIALIZED) {initializeParamters (); }  var myMovie = getFlashMovieObject(myVideoName); myMovie.setVolume(volumeLevel); myMovie.sendVariables(server, app, filename);  }  initializeParamters(); </script> <object classid='clsid:d27cdb6e-ae6d-11cf-96b8-444553540000' width='100%' height='100%' id='"+VideoPlayerName+"'> <param name='movie' value='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' /> <param name='quality' value='high' /> <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' /> <param name='wmode' value='window' />  <param name='scale' value='showall' />  <param name='menu' value='true' />  <param name='devicefont' value='false' />  <param name='salign' value='' /> <param name='allowScriptAccess' value='always' />  <param name='allowFullScreen' value='true' />   <!--[if !IE]>-->  <embed type='application/x-shockwave-flash' src='flash/guidedtour/videoplayer.swf?"+d.getTime()+"' width='100%' height='100%' AllowFullscreen='true' name='"+VideoPlayerName+"'>  <param name='movie' value='flash/guidedtour/videoplayer.swf' />  <param name='quality' value='high' />  <param name='bgcolor' value='#ffffff' />  <param name='play' value='true' />  <param name='loop' value='true' />  <param name='wmode' value='window' />   <param name='scale' value='showall' />    <param name='menu' value='true' />   <param name='devicefont' value='false' />   <param name='salign' value='' />  <param name='allowScriptAccess' value='always' />   <param name='allowFullScreen' value='true' />   <!--<![endif]--> <a href='http://www.adobe.com/go/getflash'> <img src='http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif' alt='Get Adobe Flash player' /> </a> <!--[if !IE]>-->   </embed>   <!--<![endif]-->  </object>");
    	$("#flashContent").html("<script type='text/javascript'>function ShowVideo(){ j360Player.PlayerCall('mp4','http://streaming.360training.com/vod/fse/Coming_soon.mp4');} ShowVideo();</script>");
        $("#fadeout-container").css("display", "block");
        $("#alert-box").css("display", "block");
    });

    $("#close-video").click(function(){
        window.location.reload();
        //$("#fadeout-container").css("display", "none");
        //$("#alert-box").css("display", "none");
    });


});

function fieldUpdated(){
    $("#fake-file-field").val($("#file-field").val());
}

function defaultAlert(arg){
    if(arg.checked){
        $("#row-message").css("display", "none")
    }else{
        if(browser == "Netscape"){
            $("#row-message").css("display", "table-row")
        }else{
            $("#row-message").css("display", "block")
        }
    }
}




function eventType(param){
    if(param == 1){
        $("#date-row").css("display", "table-row")
    }else{
        $("#date-row").css("display", "none")
    }

}

//-------------------------------------------------------
// Change event date
//-------------------------------------------------------

function eventDate(param){
    if(param == "event"){
        $("#recurrence-schedule").css("display", "none")
        $("#daily").css("display", "none")
        $("#weekly").css("display", "none")
        $("#monthly").css("display", "none")
        $("#yearly").css("display", "none")
        $("#range").css("display", "none")
        $("#recurring-date").attr("checked", false)
        $("#recurring-date").attr("disabled", true)
    }else{
        $("#recurring-date").attr("disabled", false)
    }

}