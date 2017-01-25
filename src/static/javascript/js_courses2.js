

//jQuery Functions
$(function(){


    switch($.browser.mozilla){
        case true :
            $("#sbc2").css("margin-left", "-13px");
            break
        case false :
            //do nothing
            break
    }

    $('#bar-side-bar1').css('width', $(window).width() - 253 - 31);

    $('.scrollable').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
    $('.scrollable_3').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
    $("#transcriptBox").height($(window).height() - $("#frame_header").height() - $(".tab_bar").height() - $("#frame_footer").height() - $("#pageDescription").height() - 76);

    if($('#sideBar').css('width') != undefined){
        
        $('.scrollable').css('width', $(window).width() - 230 + 'px');
        $('.scrollable_3').css('width', $(window).width() - 232 + 'px');
    }else{
    //
    }

    $('#table-frame').css('width', $('.scrollable').width() - 40 + 'px');

    $(window).resize(function(){
        $('.scrollable').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
        $('.scrollable_3').css('height',  $(window).height() -  $('.bar-shoppingcart-curve').height() - 115 + 'px')
        if( $(window).width() > 400 && $('#sideBar').css('width') != undefined){
            $('.scrollable').css('width', $(window).width() - 230 + 'px');
            $('.scrollable_3').css('width', $(window).width() - 232 + 'px');
        }
        $('#table-frame').css('width', $('.scrollable').width() - 40 + 'px');
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


    $("#sideBar").mouseenter(function(){
        $(".flyout-menu").css("display", "none");
    });

    function flyoutmenu_l1(cid, lid){
        $(".flyout-menu").height($(window).height() - 144)
        
        $("#"+cid).mouseenter(function(){
            $("#"+lid).css("top", "87px");
            $("#"+lid).css("left", "203px");
            $(".flyout-menu").css("display", "none");
            $("#"+lid).fadeIn();
        });

    }

    function flyoutmenu_l2(cid, lid){
        $(".flyout-menu").height($(window).height() - 144)

        $("#"+cid).mouseenter(function(){
            $("#"+lid).css("top", "87px");
            $("#"+lid).css("left", "426px");
            $("#level2").find(".flyout-menu").css("display", "none");
            $("#"+lid).fadeIn();
        });

    }

    flyoutmenu_l1("c1", "li1");
    flyoutmenu_l1("c2", "li2")
    flyoutmenu_l1("c3", "li3")
    flyoutmenu_l1("c4", "li4")

    flyoutmenu_l2("cli1", "li11")
    flyoutmenu_l2("cli2", "li12")
    flyoutmenu_l2("cli3", "li13")

    flyoutmenu_l2("cli8", "li81")

    function flyoutmenuhide_l1(lid){
        $("#"+lid).mouseleave(function(){
            $("#"+lid).fadeOut();
          });
    }

    flyoutmenuhide_l1("li11");
    flyoutmenuhide_l1("li12");
    flyoutmenuhide_l1("li13");
    flyoutmenuhide_l1("li81");


    //Career Training Program
    $("#c1").click(function(){
        $("#breadcrumb-text").html("You are here: <u>Career Training Program</u>");
        $("#category-list").find("li").attr("class", "")
        $(this).attr("class", "selected-category")
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "table")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
        $(".subvertical-table-5").css("display", "none")
        $(".subvertical-table-6").css("display", "none")
        $(".subvertical-table-7").css("display", "none")
    });



    //Allied Health Programs
    $("#cli1").click(function(){
        $("#breadcrumb-text").html("You are here: <u>Career Training Program</u> > <u>Allied Health Programs</u>");
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "table")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
        $(".subvertical-table-5").css("display", "none")
        $(".subvertical-table-6").css("display", "none")
        $(".subvertical-table-7").css("display", "none")
    });

    //Business Programs
    $("#cli2").click(function(){
        $("#breadcrumb-text").html("You are here: <u>Career Training Program</u> > <u>Business Programs</u>");
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "table")
        $(".subvertical-table-4").css("display", "none")
        $(".subvertical-table-5").css("display", "none")
        $(".subvertical-table-6").css("display", "none")
        $(".subvertical-table-7").css("display", "none")
    });

    //Medical Billing
    $("#cli111").click(function(){
        $("#breadcrumb-text").html("You are here: <u>Career Training Program</u> > <u>Allied Health Programs</u> > <u>Medical Billing</u>");
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "table")
        $(".subvertical-table-5").css("display", "none")
        $(".subvertical-table-6").css("display", "none")
        $(".subvertical-table-7").css("display", "none")
    });

    //Medical Billing & Coding
    $("#cli112").click(function(){
        $("#breadcrumb-text").html("You are here: <u>Career Training Program</u> > <u>Allied Health Programs</u> > <u>Medical Billing & Coding</u>");
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
        $(".subvertical-table-5").css("display", "table")
        $(".subvertical-table-6").css("display", "none")
        $(".subvertical-table-7").css("display", "none")
    });

    //Driver Education
    $("#c2").click(function(){
        $("#category-list").find("li").attr("class", "")
        $(this).attr("class", "selected-category")
        $("#breadcrumb-text").html("You are here: <u>Driver Education</u>");
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
        $(".subvertical-table-5").css("display", "none")
        $(".subvertical-table-6").css("display", "table")
        $(".subvertical-table-7").css("display", "none")
    });

    //Defensive Driving
    $("#cli4").click(function(){
        $("#breadcrumb-text").html("You are here: <u>Driver Education</u> > <u>Defensive Driving</u>");
        $(".flyout-menu").css("display", "none");
        $(".subvertical-table-1").css("display", "none")
        $(".subvertical-table-2").css("display", "none")
        $(".subvertical-table-3").css("display", "none")
        $(".subvertical-table-4").css("display", "none")
        $(".subvertical-table-5").css("display", "none")
        $(".subvertical-table-6").css("display", "none")
        $(".subvertical-table-7").css("display", "table")
    });


});