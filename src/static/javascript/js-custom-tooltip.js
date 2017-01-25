$(function(){

    var offset = 5;

    $('#icon-help').stop().hover(function(evt){
        $('<div id="help-text">If you need help please click on the Contact Us button at the bottom left of the page.</div>').addClass('tooltip').css('top',evt.pageY+offset-70).css('left', evt.pageX+offset).appendTo('body').hide().fadeIn(200);
    }, function(){
        $('#help-text').remove();
    });

    $('#icon-help').mousemove(function(evt){
        $('#help-text').css('top',evt.pageY + offset -70).css('left',evt.pageX + offset);
    });

    

});

