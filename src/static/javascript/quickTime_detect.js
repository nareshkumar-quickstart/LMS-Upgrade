var haveqt = false;

if($.client.browser != "Explorer"){
if (navigator.plugins) 
{
	for (i=0; i < navigator.plugins.length; i++ ) 
	{
		if (navigator.plugins[i].name.indexOf("QuickTime") >= 0)
		{
			haveqt = true; 
		}
	}
}
	 
	if ((navigator.appVersion.indexOf("Mac") > 0)
	&& (navigator.appName.substring(0,9) == "Microsoft")
	&& (parseInt(navigator.appVersion) < 5) )
	{ 
		haveqt = true;
	}
	
	
}else{
	try{
		new ActiveXObject('QuickTime.QuickTime');
		haveqt = true;
	}catch(ex){
		haveqt = false;
	}
}