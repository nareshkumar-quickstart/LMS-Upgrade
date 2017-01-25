//for report pages leftpannel js

$(document).ready(function(){
    //hide the all of the element with class msg_body
    $(".hdnDiv").hide();
    $(".hdnDivActive").show();
    //toggle the componenet with class msg_body
    $(".M-text-position3").click(function(){
        //image object
        var pImgObject=this.getElementsByTagName("div")[0].getElementsByTagName("img")[0];

        if(pImgObject.getAttribute("isHidden")=="1") {
            pImgObject.src="brands/default/en/images/arrow-down.gif";
            pImgObject.setAttribute("isHidden","0"); 
            pImgObject.title="Collapse";
        }else{
            pImgObject.src="brands/default/en/images/arrow-forward.gif";
            pImgObject.setAttribute("isHidden","1");
            pImgObject.title="Expand";
        }

        $(this).next(".hdnDiv").slideToggle(100);
        $(this).next(".hdnDivActive").slideToggle(100);

    });
    
});

var leftPaneWidth=0;
function showHideMenu(){

    $('#content2r').css({"width":$("#content2r").css("left") == "0px"?"122.5%":"100%"});
    $('#topBar').css({"width":$("#content2r").css("left") == "0px"?"122.5%":"100%"});
    $('#sliderImage').attr("title",$("#content2r").css("left") == "0px"?"Click to expand":"Click to collapse");

    leftPaneWidth = $('#left-pannelNew').width()-8;
    var targetX = $("#content2r").css("left") == "0px"? '-'+leftPaneWidth+'px':'0px';
    $("#content2r").animate({"left": targetX}, 400,function(){
    });
    $("#topBar").animate({"left": targetX}, 400,function(){
    });
}