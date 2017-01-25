var Browser = navigator.userAgent.substr(25,8);

var j360Player = {
	Browser : navigator.userAgent.substr(25,8),
	Browser2: navigator.userAgent.indexOf("Firefox") != -1,
	isAnyError: String(navigator.mimeTypes.toString()) == "undefined",
	Width: '930px',
	Height: '535px',
	PlayerCall: function(videoType,hLink,strmer,provide){
			/*var alertBox = document.getElementById("alert-box");
			
			if (alertBox != null && alertBox !== undefined) {
        		//alertBox.style.left = ((document.body.offsetWidth-800)/2)+"px";
   			}*/
			
			
			
			
			if(videoType == "flv"){
					j360player("flashContent").setup({
						height: j360Player.Height,
						width: j360Player.Width,
						autostart: "true",
						//image: "preview.jpg",
						modes: [
							{ 	type: "flash",
								src: "javascript/player.swf",
								config: {
									file: hLink,
									streamer: strmer,
									provider: provide
								}
							},
							{ type: "download" }
						]
						});
				}else{
					if((j360Player.Browser == "MSIE 8.0") || (j360Player.Browser == "MSIE 7.0") || (j360Player.Browser2) || j360Player.isAnyError){
						j360player("flashContent").setup({
							height: j360Player.Height,
							width: j360Player.Width,
							autostart: "true",
							//image: "preview.jpg",
							modes: [
								{ 	type: "flash",
									src: "javascript/player.swf",
									config: {
										file: hLink
									}
								},
								{ type: "download" }
							]
						});
					}else{
						j360player("flashContent").setup({
							height: j360Player.Height,
							width: j360Player.Width,
							autostart: "true",
							//image: "preview.jpg",
							modes: [
							{ 	type: "html5",
								config: {
									file: hLink
								}
							},
							{ type: "download" }
							]
						});
					}
				}
			
			
			window.onorientationchange = function(){
				
				//if (Math.abs(window.orientation) === 90) {
			        // Landscape
					$("#alert-box").css({left:"30px"});
					$("#flashContent").width("900px");
					//$("#flashContent").height("500px");
			    //} else {
			    	// Portrait
			    //	$("#alert-box").css({left:"10"});
					//$("#flashContent").width("800px");
					//$("#flashContent").height("500px");
			   // }
				
					
			}
			
			
			if(!($.browser.msie))
	        {
				$("#flashContent").width("963px");
				$("#flashContent").height("535px");
	        }else if(navigator.platform != "iPad" && navigator.platform != "iPhone"){
				$("#flashContent").width("928px");
				$("#flashContent").height("535px");
			}else{
				//if (Math.abs(window.orientation) === 90) {
			        // Landscape
					$("#alert-box").css({left:"30px"});
					$("#flashContent").width("900px");
					//$("#flashContent").height("500px");
			   // } else {
			    	// Portrait
			    	//$("#alert-box").css({left:"10"});
					//$("#flashContent").width("800px");
					//$("#flashContent").height("500px");
			   // }
			
			}
			
			
			
			
			if(navigator.userAgent.toLowerCase().indexOf('firefox') > -1){
				$("#flashContent_wrapper").width("965px");
			}
		}
	
}



