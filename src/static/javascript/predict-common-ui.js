 // common dialog

function openAlertDialog(id,title,btns){
	 if(typeof btns == 'undefined'){
		 btns =  [{ text: "Close", click: function() { $( this ).dialog( "close" ); } } ];
		 }
	 if(typeof title == 'undefined'){
		 title = "Alert";
		 }
	 $("#"+id).dialog({modal:true, title:title, width:400, dialogClass: "jiraDialog", resizable:false,
			buttons: btns});
 }
 
 function openDialog(id,title,btns){
	 if(typeof btns != 'undefined'){
		 $("#"+id).dialog({modal:true, title:title, width:800, height:350, dialogClass: "jiraDialog", resizable:false,
				buttons: btns});
	 }else{
		 $("#"+id).dialog({modal:true, title:title, width:800, height:350, dialogClass: "jiraDialog", resizable:false});
	 }
	
 }