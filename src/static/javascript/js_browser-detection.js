$(function(){

//Bandwidth
    $("#bandwidth-check").attr("class", "incorrect-icon");

//Operating System
    if($.client.os == "Windows"){
        $("#os-logo").attr("class", "windows-logo");
        $("#os-text").html(" Microsoft Windows detected.");
    }else{
        $("#os-logo").attr("class", "mac-logo");
        $("#os-text").html(" Mac OS detected.");
    }

//Internet Browser
    if($.client.browser == "Explorer"){
        $("#browser-logo").attr("class", "explorer-logo");
        $("#browser-text").html(" Microsoft Internet Explorer " + $.browser.version);
        $("#browser-check").attr("class", "correct-icon");
    }else if($.client.browser == "Firefox"){
        $("#browser-logo").attr("class", "firefox-logo");
        $("#browser-text").html(" Mozilla Firefox " + $.browser.version);
        $("#browser-check").attr("class", "correct-icon");
    }else if($.client.browser == "Safari"){
        $("#browser-logo").attr("class", "safari-logo");
        $("#browser-text").html(" Safari " + $.browser.version);
        $("#browser-check").attr("class", "correct-icon");
    }else if($.client.browser == "Chrome"){
        $("#browser-logo").attr("class", "chrome-logo");
        $("#browser-text").html(" Chrome " + $.browser.version);
        $("#browser-check").attr("class", "correct-icon");
    }else if($.client.browser == "Opera"){
        $("#browser-logo").attr("class", "opera-logo");
        $("#browser-text").html(" Opera " + $.browser.version);
        $("#browser-check").attr("class", "correct-icon");
    }

//Screen Resolution
    $("#screen-text").html(" "+screen.width+" x "+screen.height);
    if(screen.width >= 1024 && screen.height >= 768){
        $("#screen-check").attr("class", "correct-icon");
    }else{
        $("#screen-text").css("color", "red");
    }

//Cookie Detection
    $.cookie("CookieTest", "Test");
    if($.cookie("CookieTest")=="Test"){
        $("#cookie-text").html("enabled");
        $("#cookie-check").attr("class", "correct-icon");
    }else{
        $("#cookie-text").css("color", "red");
        $("#cookie-text").html("disabled");
    }

//Javascript Detection
    $("#javascript-text").css("color", "black");
    $("#javascript-check").attr("class", "correct-icon");
    $("#javascript-text").html("enabled");

//Flash Detection
    if(FlashDetect.installed){
        $("#flash-text").html("Adobe Flash Player" + FlashDetect.major + " is detected. ");
        $("#flash-check").attr("class", "correct-icon");
    }else{
        $("#flash-text").css("color", "red");
        $("#flash-text").html("Adobe Flash Player is detected not detected. ");
    }

//Acrobat Detection
    if(jQuery.browser.pdf){
        $("#acrobat-text").html("detected");
        $("#acrobat-check").attr("class", "correct-icon");
    }else{
        $("#acrobat-text").css("color", "red");
        $("#acrobat-text").html("not detected");
    }
    
    

  //QuickTime Detection
      if(haveqt){
          $("#quicktime-text").html("detected.");
          $("#quicktime-check").attr("class", "correct-icon");
      }else{
          $("#quicktime-text").css("color", "red");
          $("#quicktime-text").html("not detected.");
      }

});